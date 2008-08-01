
function testDataSimple() {
  // This is a demo of the simplest use-case of the data API
  // The first param is the widget ID must be unique to this page. The second,
  // is the server data ID. In this case 'testServerData' should have been
  // registered on the server with something like this:
  //  import org.directwebremoting.datasync.Directory;
  //  import org.directwebremoting.datasync.MapStoreProvider;
  //  Directory.register("testServerData", new MapStoreProvider(data));
  // In this case MapStoreProvider is a global object that uses a java.util.Map
  // there are several implementation of the StoreProvider interface.
  // widgetCallback is a callback and used to receive the data. Note that it is
  // not like a normal DWR callback: it is not the last parameter (see later)
  // and it is likely to be called a number of times. Our implementation is just
  // a routine to debug how it is called
  dwr.data.view('widgetA', 'testServerData', widgetCallback);

  // Send an update request. This will cause the object in the server data store
  // with the id 12 to have it's name field set to "Joe". It will also cause the
  // server to tell us (and anyone else subscribed to the same view) to be
  // informed. widgetCallback should debug the following:
  // widgetCallback: subscriptionId=widgetA, reason=2, data.length=1, matchCount=5
  //   - data[0].key='12', data[0].value={ name:"Joe", ... }
  // In order to see this happening we're waiting 2 seconds before the request
  // is sent.
  setTimeout(function() {
    dwr.data.update('testServerData', '12', { name:"Joe" });
  }, 2000);

  // It's not vital to de-register, however it can help to tidy up server side
  // resources. widgetCallback will not be called
  setTimeout(function() {
    //dwr.data.unsubscribe('widgetA');
  }, 4000);
}

function widgetCallback(subscriptionId, reason, data, matchCount) {
  var reasons = [ "initial", "insert", "update", "remove" ];
  dwr.engine._debug("widgetCallback: subscriptionId=" + subscriptionId + ", reason=" + reasons[reason] + ", data.length=" + data.length + ", matchCount=" + matchCount);
  for (var i = 0; i < data.length; i++) {
    dwr.engine._debug("  - array[" + i + "].itemId=" + data[i].itemId + ", array[" + i + "].data=" + dwr.util.toDescriptiveString(data[i].data));
  }
}
