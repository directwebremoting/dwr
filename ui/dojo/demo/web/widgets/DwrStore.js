
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
   * TODO: do we need to check that we don't have prototype polution?
   * save() and (maybe) other functions do 'for(a in b) {...}' without checking
   * that b[a] isn't a function.
   * TODO: The logic for the call to onComplete in the callback in simpleFetch
   * is different to our implementation.I thought that I'd followed the spec.
   * Either I can't read, or the author of simpleFetch can't read, wibble.
   */
  dojo.declare("DwrStore", null, {

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
      this.dwrCache = new dwr.data.Cache(storeId, params);

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

      // Should we check the server for fetchItemByIdentity? I think we don't need to.
      this.fastFetchItemByIdentity = true;
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
      var value = this._getAttributeValue(id, attribute, undefined);
      return value !== undefined;
    },

    /** @see dojo.data.api.Read.containsValue */
    containsValue: function(/*item*/ id, /*string*/ attribute, /*anything*/ value) {
      var test = this._getAttributeValue(id, attribute, undefined);
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
      var store = this;
      var scope = request.scope || dojo.global;

      if (request.queryOptions != null) {
        console.log("queryOptions is not currently supported by DwrStore");
      }

      var region = {
        count:request.count,
        start:request.start,
        query:request.query,
        sort:request.sort
      };

      var callbackObj = {
        callback:function(matchedItems) {
          var aborted = false;
          var originalAbort = request.abort;
          request.abort = function() {
            aborted = true;
            if (dojo.isFunction(originalAbort)) {
              originalAbort.call(request);
            }
          };

          dojo.forEach(matchedItems.viewedMatches, store._importItem, store);

          if (dojo.isFunction(request.onBegin)) {
            request.onBegin.call(scope, matchedItems.totalMatchCount, request);
          }

          if (dojo.isFunction(request.onItem)) {
            dojo.forEach(matchedItems.viewedMatches, function(entry) {
              if (!aborted) {
                request.onItem.call(scope, entry.$id, request);
              }
            });
          }

          if (dojo.isFunction(request.onComplete) && !aborted) {
            if (dojo.isFunction(request.onItem)) {
              request.onComplete.call(scope, null, request);
            }
            else {
              var all = [];
              dojo.forEach(matchedItems.viewedMatches, function(entry) {
                all.push(entry.$id);
              });
              request.onComplete.call(scope, all, request);
            }
          }
        },

        errorHandler:function(msg, ex) {
          if (dojo.isFunction(request.onError)) {
            request.onError(ex);
          }
        }
      };

      this.dwrCache.viewRegion(region, callbackObj);

      return request;
    },

    /** @see dojo.data.api.Read.close */
    close: function(/*dojo.data.api.Request*/ request) {
      this._entries = {};
      this._updated = {};
      this.dwrCache.unsubscribe({
        exceptionHandler:function(msg, ex) {
          console.error(ex);
        }
      });
    },

    /** @see dojo.data.api.Read.getLabel */
    getLabel: function(/*item*/ id) {
      // org.directwebremoting.io.Item exposes Object#toString as a label on
      // our items if the data implements ExposeToStringToTheOutside.
      return this._getAttributeValue(id, "$label");
    },

    /** @see dojo.data.api.Read.getLabelAttributes */
    getLabelAttributes: function(/*item*/ id) {
      if (!this.isItem(id)) throw new Error("non item passed to getLabelAttributes()");
      return [ "$label" ];
    },

    /** @see dojo.data.api.Identity.getIdentity */
    getIdentity: function(/*item*/ id) {
      // We could just return id, however we should be checking validity, which this does
      return this._getAttributeValue(id, "$id");
    },

    /** @see dojo.data.api.Identity.getIdentityAttributes */
    getIdentityAttributes: function(/*item*/ id) {
      if (!this.isItem(id)) throw new Error("non item passed to getIdentityAttributes()");
      return [ "$id" ];
    },

    /** @see dojo.data.api.Identity.fetchItemByIdentity */
    fetchItemByIdentity: function(/*object*/ request) {
      var scope = request.scope || dojo.global;
      var itemId = request.identity.toString();
      var store = this;

      if (this.fastFetchItemByIdentity) {
        if (dojo.isFunction(request.onItem)) {
          request.onItem.call(scope, itemId);
        }
      }
      else {
        this.dwrCache.viewItem(itemId, {
          callback: function(entry) {
            entry.updates = {};
            entry.isDeleted = false;
            entry.isDirty = false;
            entry.$id = entry.itemId;
            store._entries[entry.$id] = entry;
            delete store._updated[entry.$id];
            if (dojo.isFunction(request.onItem)) {
              request.onItem.call(scope, data);
            }
          },
          exceptionHandler: function(msg, ex) {
            if (dojo.isFunction(request.onError)) {
              request.onError.call(scope, ex);
            }
          }
        });
      }

      return request;
    },

    /**
     * Utility to take an item as passed by DWR and place it as an entry into
     * the local cache
     */
    _importItem: function(/*item*/ item) {
      item.updates = {};
      item.isDeleted = false;
      item.isDirty = false;
      item.$id = item.itemId;
      this._entries[item.$id] = item;
      delete this._updated[item.$id];
    },

    /** @see dwr.data.StoreChangeListener.itemRemoved */
    itemRemoved:function(/*StoreProvider*/ source, /*string*/ itemId) {
      delete this._entries[itemId];
      delete this._updated[itemId];
      if (dojo.isFunction(this.onDelete)) {
        this.onDelete.call(itemId);
      }
    },

    /** @see dwr.data.StoreChangeListener.itemAdded */
    itemAdded:function(/*StoreProvider*/ source, /*Item*/ item) {
      this._importItem(item);
      if (dojo.isFunction(this.onNew)) {
        this.onNew.call(item.itemId, null);
      }
    },

    /** @see dwr.data.StoreChangeListener.itemChanged */
    itemChanged:function(/*StoreProvider*/ source, /*Item*/ item, /*string[]*/ changedAttributes) {
      if (this._updated[item.itemId]) {
        console.log("Warning server changes to " + item.itemId + " override local changes");
      }
      this._importItem(item);
      if (dojo.isFunction(this.onSet)) {
        dojo.forEach(changedAttributes, function(attribute) {
          var oldValue = this._getAttributeValue(item.itemId, attribute);
          this.onSet.call(item.itemId, attribute, oldValue, item.data[attribute]);
        });
      }
    },

    /** @see dojo.data.api.Notification.onSet */
    onSet: function(/*item*/ item, /*string*/ attribute, /*object|array*/ oldValue, /*object|array*/ newValue) {
    },

    /** @see dojo.data.api.Notification.onNew */
    onNew: function(/*item*/ newItem, /*object?*/ parentInfo) {
    },

    /** @see dojo.data.api.Notification.onDelete */
    onDelete: function(/*item*/ deletedItem) {
    },

    /** @see dojo.data.api.Write.newItem */
    newItem: function(/*object?*/ data, /*object?*/ parentInfo) {
      var entry = {
        itemId:-1,
        $id:DwrStore.prototype.autoIdPrefix + this._nextLocalId,
        data:data,
        $label:"",
        isDeleted:false,
        isDirty:true,
        updates:data
      };
      this._nextLocalId++;
      this._entries[entry.$id] = entry;
      this._updated[entry.$id] = entry;

      if (dojo.isFunction(this.onNew)) {
        this.onNew(entry.$id, null);
      }

      return entry.$id;
    },

    /** @see dojo.data.api.Write.onDelete */
    deleteItem: function(/*item*/ item) {
      var entry = this._entries[item];
      if (entry == null) throw new Error("non item passed to deleteItem()");
      delete this._entries[entry.$id];
      entry.isDeleted = true;
      this._updated[entry.$id] = entry;

      if (dojo.isFunction(this.onDelete)) {
        this.onDelete(entry.$id);
      }
    },

    /** @see dojo.data.api.Write.setValue */
    setValue: function(/*item*/ item, /*string*/ attribute, /*anything*/ value) {
      if (value === undefined) throw new Error("value is undefined");
      if (!attribute) throw new Error("attribute is undefined");
      var entry = this._entries[item];
      if (entry == null) throw new Error("non item passed to setValue()");

      entry.updates[attribute] = value;
      entry.isDirty = true;
      this._updated[entry.$id] = entry;

      if (dojo.isFunction(this.onSet)) {
        this.onSet(entry.$id, attribute, entry.data[attribute], value);
      }
    },

    /** @see dojo.data.api.Write.setValues */
    setValues: function(/*item*/ item, /*string*/ attribute, /*array*/ values) {
      if (!dojo.isArray(values)) throw new Error("value is not an array");
      if (!attribute) throw new Error("attribute is undefined");
      var entry = this._entries[item];
      if (entry == null) throw new Error("non item passed to setValues()");

      entry.updates[attribute] = values;
      entry.isDirty = true;
      this._updated[entry.$id] = entry;

      if (dojo.isFunction(this.onSet)) {
        this.onSet(entry.$id, attribute, entry.data[attribute], values);
      }
    },

    /** @see dojo.data.api.Write.unsetAttribute */
    unsetAttribute: function(/*item*/ item, /*string*/ attribute) {
      if (!attribute) throw new Error("attribute is undefined");
      var entry = this._entries[item];
      if (entry == null) throw new Error("non item passed to unsetAttribute()");

      entry.updates[attribute] = null;
      entry.isDirty = true;
      this._updated[entry.$id] = entry;

      if (dojo.isFunction(this.onSet)) {
        this.onSet(entry.$id, attribute, entry.data[attribute], null);
      }
    },

    /** @see dojo.data.api.Write.save */
    save: function(/*object*/ keywordArgs) {
      var entriesToSend = [];
      for (var itemId in this._updated) {
        var entry = this._updated[itemId];
        if (entry.isDeleted) {
          entriesToSend.push({
            itemId:itemId,
            attribute:"$delete"
          });
        }
        if (entry.isNew) {
          entriesToSend.push({
            itemId:itemId,
            attribute:"$create"
          });
        }
        else {
          for (var attribute in entry.updates) {
            entriesToSend.push({
              itemId:itemId,
              attribute:attribute,
              newValue:entry.updates[attribute]
            });
          }
        }
      }

      this.dwrCache.update(entriesToSend, {
        callback:keywordArgs.onComplete,
        exceptionHandler:function(msg, ex) { keywordArgs.onError(ex); },
        scope:keywordArgs.scope
      });
    },

    /** @see dojo.data.api.Write.revert */
    revert: function() {
      for (var id in this._entries) {
        var entry = this._entries[id];
        entry.isDeleted = false;
        entry.isDirty = false;
        entry.updates = {};
      }
      this._updated = {};
      return true;
    },

    /** @see dojo.data.api.Write.isDirty */
    isDirty: function(/*item?*/ item) {
      var entry = this._entries[item];
      if (entry == null) throw new Error("non item passed to isDirty()");
      return entry.isDirty;
    }
  });
}
