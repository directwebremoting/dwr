OpenAjax.subscribe("*", "*", publish, PubSubHubSynchronizer);

// Things to think about:
// 1. Use a timeout and batches to reduce traffic
// 2. Have 2 sets of prefix/name filters so local filtering can happen

var PubSubHubSynchronizer = {

  _synchronizedPrefixes:[],

  _synchronizedNames:[],

  _hubId:'OpenAjax.' + Math.round(Math.random() * 100000),

  publish:function(prefix, name, subscriberData, publisherData) {
    var i, temp;
    var prefixMatch = false;
    for (i = 0; i < PubSubHubSynchronizer._synchronizedPrefixes.length; i++) {
      temp = PubSubHubSynchronizer._synchronizedPrefixes[i];
      if (temp == "*" || temp == prefix) {
        prefixMatch = true;
        break;
      }
    }
    if (!prefixMatch) return;
    var nameMatch = false;
    for (i = 0; i < PubSubHubSynchronizer._synchronizedNames.length; i++) {
      temp = PubSubHubSynchronizer._synchronizedNames[i];
      if (temp == "*" || temp == prefix) {
        nameMatch = true;
        break;
      }
    }
    if (!nameMatch) return;
    OpenAjaxSynchronizer.publish(prefix, name, [ _hubId ]);
  },

  synchronizeOn:function(prefixes, names) {
    PubSubHubSynchronizer._synchronizedPrefixes = prefixes;
    PubSubHubSynchronizer._synchronizedNames = names;
  }

};
