
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

      // This is an map of items, as defined above. It is currently not used
      // as a cache - only as a way correctly answer isItem(). We store items
      // against their itemIds in here
      this._data = {};

      // If we were to get serious about caching then we'd need something like...
      // this._dataLoaded = false;
      // this._dataLoading = false;
    },

    // A unique identifier for this store.
    _nextGlobalSubscriptionId: 0,

    // There are lots of checks that we are supposed to make, that are costly
    _pedantic: false,

    /** @see dojo.data.api.Read.getFeatures */
    getFeatures: function() {
      return { 'dojo.data.api.Read':true };
    },

    /** @see dojo.data.api.Read.getValue */
    getValue: function(/* item */ item, /* attribute-name-string */ attribute, /* value? */ defaultValue) {
      if (this._pedantic && !isItem(item)) throw new Error("non item passed to getValue");
      var reply = item.data[attribute];
      if (reply === undefined) return defaultValue;
      return (dojo.isArray(reply)) ? null : reply;
    },

    /** @see dojo.data.api.Read.getValues */
    getValues: function(/* item */ item, /* attribute-name-string */ attribute) {
      if (this._pedantic && !isItem(item)) throw new Error("non item passed to getValues");
      var reply = item.data[attribute];
      return (dojo.isArray(reply)) ? reply : [ reply ];
    },

    /** @see dojo.data.api.Read.getAttributes */
    getAttributes: function(/* item */ item) {
      if (this._pedantic && !isItem(item)) throw new Error("non item passed to getAttributes");
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
      if (this._pedantic && !isItem(item)) throw new Error("non item passed to hasAttribute");
      var reply = item.data[attribute];
      return reply !== undefined;
    },

    /** @see dojo.data.api.Read.containsValue */
    containsValue: function(/* item */ item, /* attribute-name-string */ attribute, /* anything */ value) {
      if (this._pedantic && !isItem(item)) throw new Error("non item passed to containsValue");
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

      request = request || {};
      if (!request.store) {
          request.store = this;
      }

      // The parameters to this callback are defined by DWR
      // TODO: Is there any advantage in using dojo.hitch?
      var callback = function(/* string */ subscriptionId, /* integer */ reason, /* array */ viewedMatches, /* integer */ totalMatchCount) {
        request.viewedMatches = viewedMatches;
        request.totalMatchCount = totalMatchCount;
        request.store._callback(request);
      };

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
    _callback: function(/* object */ request) {
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
      if (request.viewedMatches == null) {
        if (dojo.isFunction(request.onError)) {
          request.onError.call(scope, { /* TODO: something better than nothing */ }, request);
        }
        return;
      }

      if (dojo.isFunction(request.onBegin)) {
        request.onBegin.call(scope, request.totalMatchCount, request);
      }

      if (dojo.isFunction(request.onItem)) {
        dojo.forEach(request.viewedMatches, function(item) {
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
          request.onComplete.call(scope, request.viewedMatches, request);
        }
      }
    },

    /** @see dojo.data.api.Read.close */
    close: function(/*dojo.data.api.Request || keywordArgs || null */ request) {
      // TODO: what cleanup do we need to do?
    },

    /** @see dojo.data.api.Read.getLabel */
    getLabel: function(/* item */ item) {
      // TODO: There's lots of ways to implement this:
      // - bouncing to Object#toString (but there could be security issues)
      // - allowing user definition in some way (could be complex and there
      //   doesn't appear to be much of a use-case for this)
      // - just returning item.itemId for now and seeing what happens
      // org.directwebremoting.io.Item exposes Object#toString as a label on
      // our items if the data implements ExposeToStringToTheOutside
      if (this._pedantic && !isItem(item)) throw new Error("non item passed to containsValue");
      return item.label || item.itemId;
    },

    /** @see dojo.data.api.Read.getLabelAttributes */
    getLabelAttributes: function(/* item */ item) {
      // TODO: See dojo.data.api.Read.getLabel for details.
      return [ "label" ];
    }
  });
}
