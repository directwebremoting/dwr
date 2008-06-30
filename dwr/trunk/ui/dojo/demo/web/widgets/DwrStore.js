
if (!dojo._hasResource["DwrStore"]) {
  dojo._hasResource["DwrStore"] = true;
  dojo.provide("DwrStore");

  /**
   * The params object contains a number of
   * @param {String} storeId The id of StoreProvider as provided to DWR in
   * org.directwebremoting.datasync.Directory.register(storeId, store);
   * @param {Object} params An optional set of customizations to how the data
   * is fetched from the store. The options are:
   * - query: A set of name/value pairs used to filter the matching items. The
   *          implementation is StoreProvider defined, however the general
   *          pattern is to include items which have attributes named by each
   *          query name and where the value of the item's attribute is equal to
   *          the query value. Other stores implement queryOptions, the common
   *          use-case being to allow ignoreCase:true|false. With DwrStore,
   *          this is StoreProvider defined.
   *          If query is null or an empty object, no filtering will be done
   *          and all data will be included.
   * - subscribe: The DwrStore implements dojo.data.api.Notification but will
   *          only send updates if subscribe=true. The updates will be sent in a
   *          timely manner iff dwr.engine.setActiveReverseAjax=true.
   */
  dojo.declare("DwrStore", null, {
    constructor: function(/* string */ storeId, params) {
      if (storeId == null || typeof storeId != "string") {
        throw new Error("storeId is null or not a string");
      }
      this._storeId = storeId;
      this._subscriptionId = "sid" + DwrStore.prototype._nextGlobalSubscriptionId;
      DwrStore.prototype._nextGlobalSubscriptionId++;

      if (params == null) params = { };

      this._query = params.query || { };
      this._subscribe = params.subscribe || false;

      // Important: you'll need to know this to grok the rest of the file.
      // We implement the data store concept of an 'item' as:
      //   { itemId:"some-id", data:{...}, label:"..." }
      // The ID is some primary key derivative, the data is what you would
      // expect DWR to return as the data converted from Javaland, and the
      // label is like java.lang.Object.toString.
      // See org.directwebremoting.io.Item and dojo.data.api.Read.getLabel

      // TODO: Consider caching attributes for dojo.data.api.Read.getAttributes
      // Since we're generally going to be storing sets of homogeneous objects
      // we might pass an offical attribute list across the wire and cache it

      // TODO: Add label and itemId as attributes. They're special as they are
      // not part of the data, but various things assume that they are

      // We store items against their itemIds in here
      this._data = {};
    },

    // A unique identifier for this store.
    _nextGlobalSubscriptionId: 0,

    // There are lots of checks that we are supposed to make, that are costly
    _pedantic: false,

    /** @see dojo.data.api.Read.getFeatures */
    getFeatures: function() {
      return {
        // 'dojo.data.api.Write': true,
        // 'dojo.data.api.Notification': true,
        'dojo.data.api.Identity': true,
        'dojo.data.api.Read': true
      };
    },

    /** @see dojo.data.api.Read.getValue */
    getValue: function(/* item */ item, /* attribute-name-string */ attribute, /* value? */ defaultValue) {
      if (this._pedantic && !isItem(item)) throw new Error("non item passed to getValue()");
      var reply = item.data[attribute];
      if (reply === undefined) return defaultValue;
      return (dojo.isArray(reply)) ? null : reply;
    },

    /** @see dojo.data.api.Read.getValues */
    getValues: function(/* item */ item, /* attribute-name-string */ attribute) {
      if (this._pedantic && !isItem(item)) throw new Error("non item passed to getValues()");
      var reply = item.data[attribute];
      return (dojo.isArray(reply)) ? reply : [ reply ];
    },

    /** @see dojo.data.api.Read.getAttributes */
    getAttributes: function(/* item */ item) {
      if (this._pedantic && !isItem(item)) throw new Error("non item passed to getAttributes()");
      // TODO: I can't use dojo.map to iterate over the properties of an assoc array?!
      /* return dojo.map(item.data, function(item, index) { return index; }); */
      var attributes = [];
      for (var attributeName in item.data) {
        if (typeof item.data[attributeName] != "function") {
          attributes.push(attributeName);
        }
      }
      return attributes;
    },

    /** @see dojo.data.api.Read.hasAttribute */
    hasAttribute: function(/* item */ item, /* attribute-name-string */ attribute) {
      if (this._pedantic && !isItem(item)) throw new Error("non item passed to hasAttribute()");
      var reply = item.data[attribute];
      return reply !== undefined;
    },

    /** @see dojo.data.api.Read.containsValue */
    containsValue: function(/* item */ item, /* attribute-name-string */ attribute, /* anything */ value) {
      if (this._pedantic && !isItem(item)) throw new Error("non item passed to containsValue()");
      var reply = item.data[attribute];
      return reply == value;
    },

    /** @see dojo.data.api.Read.isItem */
    isItem: function(/* anything */ something) {
      if (something == null || !dojo.isObject(something)) {
        return false;
      }
      if (!dojo.isString(something.itemId)) {
        return false;
      }
      // TODO: for speed, we could check for !dojo.isObject(something.data) but
      // this is covered in the lookup below, and it allows for lazy loading

      // So it looks like an item, but is it one of ours?
      return dojo.any(this._data, function(item) {
        return item == something;
      });
    },

    /** @see dojo.data.api.Read.isItemLoaded */
    isItemLoaded: function(/* anything */ something) {
      if (!isItem(something)) return false;
      // TODO: I'm not sure that we are going lazy loading like this, but just in case
      return something.data != null;
    },

    /** @see dojo.data.api.Read.loadItem */
    loadItem: function(/* object */ keywordArgs) {
      // We're not doing lazy loading at this level.
      return;
    },

    /** @see dojo.data.api.Read.fetch */
    fetch: function(/* object */ request) {
      // TODO: I only discovered from reading the QueryReadStore that the param
      // passed in here is a dojo.data.api.Request
      request = request || {};
      if (!request.store) {
          request.store = this;
      }

      // TODO: maybe we should support these. Where are they used?
      if (request.queryOptions != null) {
        console.log("queryOptions is not currently supported by DwrStore");
      }

      var region = {
        count:request.count,
        start:request.start,
        query:request.query,
        queryOptions:request.queryOptions,
        sort:request.sort
      };

      // The parameters to this callback are defined by DWR
      // TODO: Is there any advantage in using dojo.hitch?
      var callback = function(/* string */ subscriptionId, /* integer */ reason, /* array */ viewedMatches, /* integer */ totalMatchCount) {
        request.store._callback(request, subscriptionId, reason, viewedMatches, totalMatchCount);
      };

      if (this._subscribe) {
        dwr.data.subscribe(this._subscriptionId, this._storeId, callback, region);
      }
      else {
        dwr.data.view(this._subscriptionId, this._storeId, callback, region);
      }

      return request;
    },

    /**
     * This function is called by a closure in DwrStore.fetch which is in turn
     * called by the client side of the DWR store
     */
    _callback: function(/* object */ request, /* string */ subscriptionId, /* integer */ reason, /* array */ viewedMatches, /* integer */ totalMatchCount) {
      var scope = request.scope || dojo.global;

      var aborted = false;
      var originalAbort = request.abort;
      request.abort = function() {
        aborted = true;
        if (dojo.isFunction(originalAbort)) {
          originalAbort.call(request);
        }
      };

      // TODO: We should probably sort out something more formal about errors
      if (viewedMatches == null) {
        if (dojo.isFunction(request.onError)) {
          request.onError.call(scope, { /* TODO: something better than nothing */ }, request);
        }
        return;
      }

      switch (reason) {
        case dwr.data.reason.insert:
          if (dojo.isFunction(this.onNew)) {
            // TODO: Notifications from the server are more corse grained than this
            // We probably need to loop and call this for every attribute?
            var parentInfo = null; // The DWR store is not hierarchical
            request.onNew.call(newItem, parentInfo);
          }
          break;

        case dwr.data.reason.update:
          if (dojo.isFunction(this.onSet)) {
            // TODO: Notifications from the server are more corse grained than this
            // We probably need to loop and call this for every attribute?
            request.onSet.call(item, attribute, oldValue, newValue);
          }
          //  summary:
          //      This function is called any time an item is modified via setValue, setValues, unsetAttribute, etc.  
          //  description:
          //      This function is called any time an item is modified via setValue, setValues, unsetAttribute, etc.  
          //      Its purpose is to provide a hook point for those who wish to monitor actions on items in the store 
          //      in a simple manner.  The general expected usage is to dojo.connect() to the store's 
          //      implementation and be called after the store function is called.
          //  item:
          //      The item being modified.
          //  attribute:
          //      The attribute being changed represented as a string name.
          //  oldValue:
          //      The old value of the attribute.  In the case of single value calls, such as setValue, unsetAttribute, etc,
          //      this value will be generally be an atomic value of some sort (string, int, etc, object).  In the case of 
          //      multi-valued attributes, it will be an array.
          //  newValue:
          //      The new value of the attribute.  In the case of single value calls, such as setValue, this value will be 
          //      generally be an atomic value of some sort (string, int, etc, object).  In the case of multi-valued attributes, 
          //      it will be an array.  In the case of unsetAttribute, the new value will be 'undefined'.
          //
          //  returns:
          //      Nothing.
          break;

        case dwr.data.reason.remove:
          if (dojo.isFunction(this.onDelete)) {
            // TODO: What do we know about deleted items?
            request.onDelete.call(deletedItem);
          }
          break;

        case dwr.data.reason.initial:
          if (dojo.isFunction(request.onBegin)) {
            request.onBegin.call(scope, totalMatchCount, request);
          }

          if (dojo.isFunction(request.onItem)) {
            dojo.forEach(viewedMatches, function(item) {
              if (!aborted) {
                request.onItem.call(scope, item, request);
              }
            });
          }

          var startIndex = request.start ? request.start : 0;
          var endIndex = request.count ? (startIndex + request.count) : items.length;

          if (dojo.isFunction(request.onComplete) && !aborted) {
            // TODO: The logic for this in simpleFetch is different to this.
            // I thought that I'd followed the spec. Either I can't read, or the
            // author of simpleFetch can't read, or things have moved on.
            if (dojo.isFunction(request.onItem)) {
              request.onComplete.call(scope, null, request);
            }
            else {
              request.onComplete.call(scope, viewedMatches, request);
            }
          }
          break;
        }
    },

    /** @see dojo.data.api.Read.close */
    close: function(/*dojo.data.api.Request || keywordArgs || null */ request) {
      // TODO: what cleanup do we need to do?
    },

    /** @see dojo.data.api.Read.getLabel */
    getLabel: function(/* item */ item) {
      // org.directwebremoting.io.Item exposes Object#toString as a label on
      // our items if the data implements ExposeToStringToTheOutside. Otherwise
      // it just used the itemId
      if (this._pedantic && !isItem(item)) throw new Error("non item passed to getLabel()");
      return item.label || item.itemId;
    },

    /** @see dojo.data.api.Read.getLabelAttributes */
    getLabelAttributes: function(/* item */ item) {
      // TODO: See dojo.data.api.Read.getLabel for details.
      return [ "label" ];
    },

    /** @see dojo.data.api.Identity.getIdentity */
    getIdentity: function(/* item */ item) {
      if (this._pedantic && !isItem(item)) throw new Error("non item passed to getIdentity()");
      return item.itemId;
    },

    /** @see dojo.data.api.Identity.getIdentityAttributes */
    getIdentityAttributes: function(/* item */ item) {
      if (this._pedantic && !isItem(item)) throw new Error("non item passed to getIdentityAttributes()");
      return [ "itemId" ];
    },

    /** @see dojo.data.api.Identity.fetchItemByIdentity */
    fetchItemByIdentity: function(/* object */ request) {

      // TODO: It's an assumption that the param here is a dojo.data.api.Request
      // This assumption is based on symmetry with undocumented features
      // discovered by reading QueryReadStore. See the impl of fetch() above.
      request = request || {};
      if (!request.store) {
          request.store = this;
      }

      // TODO: maybe we should support these. Where are they used?
      if (request.queryOptions != null) {
        console.log("queryOptions is not currently supported by DwrStore");
      }

      var region = {
        count:1,
        start:1,
        query:{ itemId:request.identity.toString }
      };

      // The parameters to this callback are defined by DWR
      var callback = function(/* string */ subscriptionId, /* integer */ reason, /* array */ viewedMatches, /* integer */ totalMatchCount) {
        request.store._callback(request, subscriptionId, reason, viewedMatches, totalMatchCount);
      };

      dwr.data.view(this._subscriptionId, this._storeId, callback, region);

      return request;
    },

    /** @see dojo.data.api.Notification.onSet */
    onSet: function(/* item */ item, /* attribute-name-string */ attribute, /* object | array */ oldValue, /* object | array */ newValue) {
      // It's up to other to override. We just need to call this from _callback()
    },

    /** @see dojo.data.api.Notification.onNew */
    onNew: function(/* item */ newItem, /*object?*/ parentInfo) {
      // It's up to other to override. We just need to call this from _callback()
    },

    /** @see dojo.data.api.Notification.onDelete */
    onDelete: function(/* item */ deletedItem) {
      // It's up to other to override. We just need to call this from _callback()
    },

    /** @see dojo.data.api.Notification.onDelete */
    newItem: function(/* Object? */ keywordArgs, /*Object?*/ parentInfo) {
      //  summary:
      //      Returns a newly created item.  Sets the attributes of the new
      //      item based on the *keywordArgs* provided.  In general, the attribute
      //      names in the keywords become the attributes in the new item and as for
      //      the attribute values in keywordArgs, they become the values of the attributes
      //      in the new item.  In addition, for stores that support hierarchical item 
      //      creation, an optional second parameter is accepted that defines what item is the parent
      //      of the new item and what attribute of that item should the new item be assigned to.
      //      In general, this will assume that the attribute targetted is multi-valued and a new item
      //      is appended onto the list of values for that attribute.  
      //
      //  keywordArgs:
      //      A javascript object defining the initial content of the item as a set of JavaScript 'property name: value' pairs.
      //  parentInfo:
      //      An optional javascript object defining what item is the parent of this item (in a hierarchical store.  Not all stores do hierarchical items), 
      //      and what attribute of that parent to assign the new item to.  If this is present, and the attribute specified
      //      is a multi-valued attribute, it will append this item into the array of values for that attribute.  The structure
      //      of the object is as follows:
      //      {
      //          parent: someItem,
      //          attribute: "attribute-name-string"
      //      }
      //
      //  exceptions:
      //      Throws an exception if *keywordArgs* is a string or a number or
      //      anything other than a simple anonymous object.  
      //      Throws an exception if the item in parentInfo is not an item from the store
      //      or if the attribute isn't an attribute name string.
      //  example:
      //  |   var kermit = store.newItem({name: "Kermit", color:[blue, green]});
      throw new Error('Unimplemented API: dojo.data.api.Write.newItem');
    },

    /** @see dojo.data.api.Notification.onDelete */
    deleteItem: function(/* item */ item) {
      //  summary:
      //      Deletes an item from the store.
      //
      //  item: 
      //      The item to delete.
      //
      //  exceptions:
      //      Throws an exception if the argument *item* is not an item 
      //      (if store.isItem(item) returns false).
      //  example:
      //  |   var success = store.deleteItem(kermit);
      throw new Error('Unimplemented API: dojo.data.api.Write.deleteItem');
    },

    /** @see dojo.data.api.Notification.setValue */
    setValue: function(/* item */ item, /* string */ attribute, /* almost anything */ value) {
      //  summary:
      //      Sets the value of an attribute on an item.
      //      Replaces any previous value or values.
      //
      //  item:
      //      The item to modify.
      //  attribute:
      //      The attribute of the item to change represented as a string name.
      //  value:
      //      The value to assign to the item.
      //
      //  exceptions:
      //      Throws an exception if *item* is not an item, or if *attribute*
      //      is neither an attribute object or a string.
      //      Throws an exception if *value* is undefined.
      //  example:
      //  |   var success = store.set(kermit, "color", "green");
      throw new Error('Unimplemented API: dojo.data.api.Write.setValue');
      return false; // boolean
    },

    /** @see dojo.data.api.Notification.setValues */
    setValues: function(/* item */ item, /* string */ attribute, /* array */ values) {
      //  summary:
      //      Adds each value in the *values* array as a value of the given
      //      attribute on the given item.
      //      Replaces any previous value or values.
      //      Calling store.setValues(x, y, []) (with *values* as an empty array) has
      //      the same effect as calling store.unsetAttribute(x, y).
      //
      //  item:
      //      The item to modify.
      //  attribute:
      //      The attribute of the item to change represented as a string name.
      //  values:
      //      An array of values to assign to the attribute..
      //
      //  exceptions:
      //      Throws an exception if *values* is not an array, if *item* is not an
      //      item, or if *attribute* is neither an attribute object or a string.
      //  example:
      //  |   var success = store.setValues(kermit, "color", ["green", "aqua"]);
      //  |   success = store.setValues(kermit, "color", []);
      //  |   if (success) {assert(!store.hasAttribute(kermit, "color"));}
      throw new Error('Unimplemented API: dojo.data.api.Write.setValues');
      return false; // boolean
    },

    /** @see dojo.data.api.Notification.unsetAttribute */
    unsetAttribute: function(/* item */ item, /* string */ attribute) {
      //  summary:
      //      Deletes all the values of an attribute on an item.
      //
      //  item:
      //      The item to modify.
      //  attribute:
      //      The attribute of the item to unset represented as a string.
      //
      //  exceptions:
      //      Throws an exception if *item* is not an item, or if *attribute*
      //      is neither an attribute object or a string.
      //  example:
      //  |   var success = store.unsetAttribute(kermit, "color");
      //  |   if (success) {assert(!store.hasAttribute(kermit, "color"));}
      throw new Error('Unimplemented API: dojo.data.api.Write.clear');
      return false; // boolean
    },

    /** @see dojo.data.api.Notification.save */
    save: function(/* object */ keywordArgs) {
      //  summary:
      //      Saves to the server all the changes that have been made locally.
      //      The save operation may take some time and is generally performed
      //      in an asynchronous fashion.  The outcome of the save action is 
      //      is passed into the set of supported callbacks for the save.
      //   
      //  keywordArgs:
      //      {
      //          onComplete: function
      //          onError: function
      //          scope: object
      //      }
      //
      //  The *onComplete* parameter.
      //      function();
      //
      //      If an onComplete callback function is provided, the callback function
      //      will be called just once, after the save has completed.  No parameters
      //      are generally passed to the onComplete.
      //
      //  The *onError* parameter.
      //      function(errorData); 
      //
      //      If an onError callback function is provided, the callback function
      //      will be called if there is any sort of error while attempting to
      //      execute the save.  The onError function will be based one parameter, the
      //      error.
      //
      //  The *scope* parameter.
      //      If a scope object is provided, all of the callback function (
      //      onComplete, onError, etc) will be invoked in the context of the scope
      //      object.  In the body of the callback function, the value of the "this"
      //      keyword will be the scope object.   If no scope object is provided,
      //      the callback functions will be called in the context of dojo.global.  
      //      For example, onComplete.call(scope) vs. 
      //      onComplete.call(dojo.global)
      //
      //  returns:
      //      Nothing.  Since the saves are generally asynchronous, there is 
      //      no need to return anything.  All results are passed via callbacks.
      //  example:
      //  |   store.save({onComplete: onSave});
      //  |   store.save({scope: fooObj, onComplete: onSave, onError: saveFailed});
      throw new Error('Unimplemented API: dojo.data.api.Write.save');
    },

    /** @see dojo.data.api.Notification.revert */
    revert: function() {
      //  summary:
      //      Discards any unsaved changes.
      //  description:
      //      Discards any unsaved changes.
      //
      //  example:
      //  |   var success = store.revert();
      throw new Error('Unimplemented API: dojo.data.api.Write.revert');
      return false; // boolean
    },

    /** @see dojo.data.api.Notification.isDirty */
    isDirty: function(/* item? */ item) {
      //  summary:
      //      Given an item, isDirty() returns true if the item has been modified 
      //      since the last save().  If isDirty() is called with no *item* argument,  
      //      then this function returns true if any item has been modified since
      //      the last save().
      //
      //  item:
      //      The item to check.
      //
      //  exceptions:
      //      Throws an exception if isDirty() is passed an argument and the
      //      argument is not an item.
      //  example:
      //  |   var trueOrFalse = store.isDirty(kermit); // true if kermit is dirty
      //  |   var trueOrFalse = store.isDirty();       // true if any item is dirty
      throw new Error('Unimplemented API: dojo.data.api.Write.isDirty');
      return false; // boolean
    }
  });
}
