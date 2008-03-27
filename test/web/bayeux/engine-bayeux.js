dojo.require("dojox.io.cometd");

dojo.connect(cometd, "finishInit",
  function(data) {
    dojox.io.cometd.subscribe("/dwr/" + dojox.io.cometd.clientId, true, 
        function(msg){ dwr.engine._eval(msg.data); }
    );
  }
);

dojox.io.cometd.init("/dwr-test/bayeux");

/** @private Actually send the block of data in the batch object. */
dwr.engine._sendData = function(batch) {


  batch.map.batchId = dwr.engine._nextBatchId;
  dwr.engine._nextBatchId++;
  dwr.engine._batches[batch.map.batchId] = batch;
  dwr.engine._batchesLength++;
  batch.completed = false;

  for (var i = 0; i < batch.preHooks.length; i++) {
    batch.preHooks[i]();
  }
  batch.preHooks = null;

  // timeout - maybe we should pass the timeout value into dojo?

  var request = dwr.engine._constructRequest(batch);

  dojox.io.cometd.publish("/dwr", request.body);
};
