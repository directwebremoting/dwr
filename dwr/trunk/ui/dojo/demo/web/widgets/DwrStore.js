if (!dojo._hasResource["DwrStore"]) {
  dojo._hasResource["DwrStore"] = true;
  dojo.provide("DwrStore");

  dojo.require("dojo.data.util.simpleFetch");

  dojo.declare("DwrStore", null, {
    constructor: function(params) {
      if (params.readMethod == null || typeof params.readMethod != "function") {
        throw new Error("readMethod is null or not a function");
      }
      this._readMethod = params.readMethod;
      this._data = [];
      this._dataLoaded = false;
      this._dataLoading = false;
    },

    getFeatures: function() {
      return { 'dojo.data.api.Read':true };
    },

    /**
     * dojo.data.util.simpleFetch calls this to load data
     */
    _fetchItems: function(args, findCallback, errorCallback) {

      // Re-use loaded data
      if (this._dataLoaded) {
        var filteredItems = _filter(args);
        findCallback(filteredItems, args);
        return;
      }

      // Ignore if we're still working on it.
      // BUG: What if the filter is different this time, and shouldn't we
      // schedule a second call of findCallback
      if (this._dataLoading) {
        return;
      }

      this._dataLoading = true;
      var self = this;

      this._readMethod({
        callback: function(data) {
          self._data = data;
          self._dataLoaded = true;
          self._dataLoading = false;
          var filteredItems = _filter(args);
          findCallback(filteredItems, args);
        },
        exceptionHandler: function(msg, ex) {
          errorCallback(ex, args);
        }
      });
    },

    close: function(request) { },

    getValue: function(item, attribute, defaultValue) {
      return item[attribute];
    },

    // Function to apply filter to data set
    _filter: function(args) {
      var filteredItems = [];
      dojo.forEach(self._data, function(candidateItem, i) {
        if (candidateItem == null) {
          return;
        }
        var match = true;
        dojo.forEach(args.query, function(queryValue, attribute) {
          var itemValue = this.getValue(candidateItem, attribute);
          if (itemValue.indexOf(queryValue.substr(0, queryValue.length - 1)) == -1) {
            match = false;
          }
        });
        if (match) {
          filteredItems.push(candidateItem);
        }
      });
      return filteredItems;
    }
  });

  dojo.extend(DwrStore, dojo.data.util.simpleFetch);
}
