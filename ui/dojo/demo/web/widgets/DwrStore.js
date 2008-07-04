
if (!dojo._hasResource["DwrStore"]) {
  dojo._hasResource["DwrStore"] = true;
  dojo.provide("DwrStore");

  /**
   * An implementation of all 4 DataStore APIs
   * TODO: Consider support for queryOptions (see fetch)
   * TODO: Consider caching attributes for dojo.data.api.Read.getAttributes
   * Since we're generally going to be storing sets of homogeneous objects
   * we might pass an official attribute list across the wire and cache it
   * TODO: Something more formal about errors in _callback()?
   * TODO: I only discovered from reading the QueryReadStore that the param
   * passed into fetch() is a dojo.data.api.Request. I'm assuming that the same
   * is true for fetchItemByIdentity()
   */
  dojo.declare("DwrStore", null, {

    // A unique identifier for this store.
    _nextGlobalSubscriptionId: 0,

    // When we need to generate a local $id from a call to newItem()
    autoIdPrefix:"_auto_",

    /**
     * @param {String} storeId The id of StoreProvider as provided to DWR in
     * org.directwebremoting.datasync.Directory.register(storeId, store);
     * @param {Object} params An optional set of customizations to how the data
     * is fetched from the store. The options are:
     * - subscribe: The DwrStore implements dojo.data.api.Notification but will
     *          only send updates if subscribe=true. The updates will be sent in a
     *          timely manner iff dwr.engine.setActiveReverseAjax=true.
     */
    constructor: function(/*string*/ storeId, /*object*/ params) {
      if (storeId == null || typeof storeId != "string") {
        throw new Error("storeId is null or not a string");
      }
      this._storeId = storeId;
      this._subscriptionId = "sid" + DwrStore.prototype._nextGlobalSubscriptionId;
      DwrStore.prototype._nextGlobalSubscriptionId++;

      if (params == null) params = { };

      this._subscribe = params.subscribe;

      // Important: you'll need to know this to grok the rest of the file.
      // We store data in a set of entries. What we give to the outside as an
      // 'item' is actually just a string aka the id of each entry.
      // Each entry is:
      // { $id:.., $label:.., data:.., updates:.., isDeleted:.., isDirty:.. }
      // $id and $label have $ prefixes because they are partially exposed
      // - itemId: primary key derivative, set to what the server gives us.
      //   If this entry was loaded from the server then $id == itemId
      // - $id: key that is exposed to the client. different to itemId if this
      //   item came from newItem() when we didn't have time to ask for a new ID
      // - $label: is like java.lang.Object.toString. For more details see
      //   org.directwebremoting.io.Item and dojo.data.api.Read.getLabel
      // - data: is what you would expect DWR to return from Java-Land
      // - updates: data that has been altered locally, but not save()d yet
      // - isDeleted: Has this item been deleted?
      // - isDirty: Has this item been updated?
      // The first 3 are sent from the server, the second 3 are client side only

      // We store items against their itemIds in here. Currently the former is
      // going to grow and grow. When do we need to clear it out?
      this._entries = {};
      this._updated = {};

      // We need to generate unique local ids for newItem();
      this._nextLocalId = 0;
    },

    /** @see dojo.data.api.Read.getFeatures */
    getFeatures: function() {
      return {
        'dojo.data.api.Write': true,
        'dojo.data.api.Notification': true,
        'dojo.data.api.Identity': true,
        'dojo.data.api.Read': true
      };
    },

    /**
     * Get the entry as indexed by the given id.
     * @param id The id of the item (and hence it's data)
     */
    _getAttributeValue: function(/*string*/ id, /*string*/ attribute, /*anything*/ defaultValue) {
      var entry = this._entries[id];
      if (entry == null) throw new Error("non item");
      if (attribute == "$id") return entry.$id;
      if (attribute == "$label") return entry.$label;
      var value = entry.updates[attribute];
      if (value === undefined) value = entry.data[attribute];
      if (value === undefined) value = defaultValue;
      return value;
    },

    /** @see dojo.data.api.Read.getValue */
    getValue: function(/*item*/ id, /*string*/ attribute, /*anything*/ defaultValue) {
      var value = this._getAttributeValue(id, attribute, defaultValue);
      return (dojo.isArray(value)) ? null : value;
    },

    /** @see dojo.data.api.Read.getValues */
    getValues: function(/*item*/ id, /*string*/ attribute) {
      var value = this._getAttributeValue(id, attribute, []);
      return (dojo.isArray(value)) ? value : [ value ];
    },

    /** @see dojo.data.api.Read.getAttributes */
    getAttributes: function(/*item*/ id) {
      var entry = this._entries[id];
      if (entry == null) throw new Error("non item passed to getAttributes()");
      var attributes = [ '$id', '$label' ];
      for (var attributeName in entry.data) {
        if (typeof entry.data[attributeName] != "function") {
          attributes.push(attributeName);
        }
      }
      return attributes;
    },

    /** @see dojo.data.api.Read.hasAttribute */
    hasAttribute: function(/*item*/ id, /*string*/ attribute) {
      var value = this._getAttributeValue(id, attribute, defaultValue);
      return value !== undefined;
    },

    /** @see dojo.data.api.Read.containsValue */
    containsValue: function(/*item*/ id, /*string*/ attribute, /*anything*/ value) {
      var test = this._getAttributeValue(id, attribute, defaultValue);
      return test == value;
    },

    /** @see dojo.data.api.Read.isItem */
    isItem: function(/*anything*/ something) {
      return dojo.isString(something) && this._entries[something] != null;
    },

    /** @see dojo.data.api.Read.isItemLoaded */
    isItemLoaded: function(/*anything*/ something) {
      var entry = this._entries[something];
      return entry != null && entry.data != null;
    },

    /** @see dojo.data.api.Read.loadItem */
    loadItem: function(/*object*/ keywordArgs) {
      // We're not doing lazy loading at this level.
      return true;
    },

    /** @see dojo.data.api.Read.fetch */
    fetch: function(/*object*/ request) {
      request = request || {};
      if (!request.store) {
          request.store = this;
      }

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
      var callback = function(/*string*/ subscriptionId, /*integer*/ reason, /*array*/ viewedMatches, /*integer*/ totalMatchCount) {
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
    _callback: function(/*object*/ request, /*string*/ subscriptionId, /*integer*/ reason, /*array*/ viewedMatches, /*integer*/ totalMatchCount) {
      var scope = request.scope || dojo.global;

      var aborted = false;
      var originalAbort = request.abort;
      request.abort = function() {
        aborted = true;
        if (dojo.isFunction(originalAbort)) {
          originalAbort.call(request);
        }
      };

      if (viewedMatches == null) {
        if (dojo.isFunction(request.onError)) {
          request.onError.call(scope, { /* something is better than nothing? */ }, request);
        }
        return;
      }

      // Cache the data
      dojo.forEach(viewedMatches, function(entry) {
        entry.updates = {};
        entry.isDeleted = false;
        entry.isDirty = false;
        entry.$id = entry.itemId;
        request.store._entries[entry.$id] = entry;
      });

      switch (reason) {
        case dwr.data.reason.insert:
          // This is part of Notification so the methods are on the store
          if (dojo.isFunction(request.store.onNew)) {
            var parentInfo = null; // The DWR store is not hierarchical
            dojo.forEach(viewedMatches, function(entry) {
              if (!aborted) {
                request.store.onNew.call(scope, entry, request);
              }
            });
          }
          break;

        case dwr.data.reason.update:
          if (dojo.isFunction(request.onSet)) {
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
          if (dojo.isFunction(request.onDelete)) {
            // TODO: What do we know about deleted items?
            request.onDelete.call(deletedItem);
          }
          break;

        case dwr.data.reason.initial:
          if (dojo.isFunction(request.onBegin)) {
            request.onBegin.call(scope, totalMatchCount, request);
          }

          if (dojo.isFunction(request.onItem)) {
            dojo.forEach(viewedMatches, function(entry) {
              if (!aborted) {
                request.onItem.call(scope, entry.$id, request);
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
              var all = [];
              dojo.forEach(viewedMatches, function(entry) {
                all.push(entry.$id);
              });
              request.onComplete.call(scope, all, request);
            }
          }
          break;
        }
    },

    /** @see dojo.data.api.Read.close */
    close: function(/*dojo.data.api.Request|keywordArgs|null*/ request) {
      this._entries = {};
      this._updated = {};
      if (this._subscribe) {
        dwr.data.unsubscribe(this._subscriptionId);
      }
    },

    /** @see dojo.data.api.Read.getLabel */
    getLabel: function(/*item*/ id) {
      // org.directwebremoting.io.Item exposes Object#toString as a label on
      // our items if the data implements ExposeToStringToTheOutside.
      return _getAttributeValue(id, "$label");
    },

    /** @see dojo.data.api.Read.getLabelAttributes */
    getLabelAttributes: function(/*item*/ id) {
      if (!this.isItem(id)) throw new Error("non item passed to getLabelAttributes()");
      return [ "$label" ];
    },

    /** @see dojo.data.api.Identity.getIdentity */
    getIdentity: function(/*item*/ id) {
      return _getAttributeValue(id, "$id");
    },

    /** @see dojo.data.api.Identity.getIdentityAttributes */
    getIdentityAttributes: function(/*item*/ id) {
      if (!this.isItem(id)) throw new Error("non item passed to getIdentityAttributes()");
      return [ "$id" ];
    },

    /** @see dojo.data.api.Identity.fetchItemByIdentity */
    fetchItemByIdentity: function(/*object*/ request) {
      request = request || {};
      if (!request.store) {
          request.store = this;
      }

      if (request.queryOptions != null) {
        console.log("queryOptions is not currently supported by DwrStore");
      }

      var region = {
        count:1,
        start:1,
        query:{ itemId:request.identity.toString }
      };

      // The parameters to this callback are defined by DWR
      var callback = function(/*string*/ subscriptionId, /*integer*/ reason, /*array*/ viewedMatches, /*integer*/ totalMatchCount) {
        request.store._callback(request, subscriptionId, reason, viewedMatches, totalMatchCount);
      };

      dwr.data.view(this._subscriptionId, this._storeId, callback, region);

      return request;
    },

    /** @see dojo.data.api.Notification.onSet */
    onSet: function(/*item*/ item, /*string*/ attribute, /*object|array*/ oldValue, /*object|array*/ newValue) {
console.log("onSet", item, attribute, oldValue, newValue);
      // It's up to others to override. We just need to call this from _callback()
    },

    /** @see dojo.data.api.Notification.onNew */
    onNew: function(/*item*/ newItem, /*object?*/ parentInfo) {
console.log("onNew", newItem, parentInfo);
      // It's up to others to override. We just need to call this from _callback()
    },

    /** @see dojo.data.api.Notification.onDelete */
    onDelete: function(/*item*/ deletedItem) {
console.log("onDelete", deletedItem);
      // It's up to others to override. We just need to call this from _callback()
    },

    /** @see dojo.data.api.Notification.onDelete */
    newItem: function(/*object?*/ keywordArgs, /*object?*/ parentInfo) {
console.log("newItem", keywordArgs, parentInfo);
      var entry = {
        itemId:-1,
        $id:DwrStore.prototype.autoIdPrefix + this._nextLocalId,
        data:{},
        $label:"",
        isDeleted:false,
        isDirty:true,
        updates:{}
      };
      this._nextLocalId++;
      for (var attribute in keywordArgs) {
        entry.updates[attribute] = keywordArgs[attribute];
      }
      this._entries[entry.$id] = entry;
      this._updated[entry.$id] = entry;
      return entry.$id;
    },

    /** @see dojo.data.api.Notification.onDelete */
    deleteItem: function(/*item*/ item) {
console.log("deleteItem", item);
      var entry = this._entries[item];
      if (entry == null) throw new Error("non item passed to deleteItem()");
      delete this._entries[entry.$id];
      entry.isDeleted = true;
      this._updated[entry.$id] = entry;
      return true;
    },

    /** @see dojo.data.api.Notification.setValue */
    setValue: function(/*item*/ item, /*string*/ attribute, /*anything*/ value) {
console.log("setValue", item, attribute, value);
      if (value === undefined) throw new Error("value is undefined");
      if (!attribute) throw new Error("attribute is undefined");
      var entry = this._entries[item];
      if (entry == null) throw new Error("non item passed to setValue()");

      entry.updates[attribute] = value;
      entry.isDirty = true;
      this._updated[entry.$id] = entry;
      return true;
    },

    /** @see dojo.data.api.Notification.setValues */
    setValues: function(/*item*/ id, /*string*/ attribute, /*array*/ values) {
console.log("setValues", id, attribute, values);
      if (!dojo.isArray(values)) throw new Error("value is not an array");
      if (!attribute) throw new Error("attribute is undefined");
      var entry = this._entries[item];
      if (entry == null) throw new Error("non item passed to setValues()");

      entry.updates[attribute] = values;
      entry.isDirty = true;
      this._updated[entry.$id] = entry;
      return true;
    },

    /** @see dojo.data.api.Notification.unsetAttribute */
    unsetAttribute: function(/*item*/ id, /*string*/ attribute) {
console.log("unsetAttribute", id, attribute);
      if (!attribute) throw new Error("attribute is undefined");
      var entry = this._entries[item];
      if (entry == null) throw new Error("non item passed to unsetAttribute()");

      entry.updates[attribute] = null;
      entry.isDirty = true;
      this._updated[entry.$id] = entry;
      return true;
    },

    /** @see dojo.data.api.Notification.save */
    save: function(/*object*/ keywordArgs) {
console.log("save", keywordArgs);
      var entriesToSend = [];
      for (var id in this._updated) {
        var entry = this._updated[id];
        var toSend = {
          itemId:entry.itemId,
          data:entry.data
        };
        for (var attribute in entry.updates) {
          toSend[attribute] = entry.updates[attribute];
        }
        if (entry.isDeleted) {
          toSend.data = null;
        }
        entriesToSend.push(toSend);
      }

      dwr.data.update(this._storeId, entriesToSend, keywordArgs.onComplete, keywordArgs.onError);
    },

    /** @see dojo.data.api.Notification.revert */
    revert: function() {
console.log("revert");
      for (var id in this._entries) {
        var entry = this._entries[id];
        entry.isDeleted = false;
        entry.isDirty = false;
        entry.updates = {};
      }
      this._updated = {};
      return true;
    },

    /** @see dojo.data.api.Notification.isDirty */
    isDirty: function(/*item?*/ item) {
console.log("isDirty", item);
      var entry = this._entries[item];
      if (entry == null) throw new Error("non item passed to isDirty()");
      return entry.isDirty;
    }
  });
}
