
dwr.bayeux = { _origSendData: dwr.engine.transport.send };

dojo.require("dojox.cometd");

dwr.engine.transport.send = function(batch) {
  batch.map.batchId = dwr.engine._nextBatchId;
  dwr.engine._nextBatchId++;
  dwr.engine._batches[batch.map.batchId] = batch;
  dwr.engine._batchesLength++;
  batch.completed = false;

  for (var i = 0; i < batch.preHooks.length; i++) {
    batch.preHooks[i]();
  }
  batch.preHooks = null;
  
  // convert integers to strings
  var count=batch.map['callCount'];
  batch.map['callCount']=''+count;
  batch.map['batchId']=''+batch.map['batchId'];
  for (var i=0; i<count; i++) {
    batch.map['c'+i+'-id']=''+batch.map['c'+i+'-id'];
  }
  // alert(dojo.toJson(batch.map));
  dojox.cometd.publish("/dwr", batch.map);
};

dojo.connect(dojox.cometd, "finishInit", 
  function(type, data, evt, request) {
    dojox.cometd.subscribe("/dwr/" + dojox.cometd.clientId,
    function(msg) {
      var batchId = Math.round(msg.id);
      var batch = dwr.engine._batches[batchId];      
      dwr.engine._callPostHooks(batch);
      dwr.engine._receivedBatch = batch;
      eval(msg.data);
      dwr.engine._receivedBatch = null;
      dwr.engine._validateBatch(batch);
      dwr.engine._clearUp(batch);
    });
  }
);

dojox.cometd.init(new String(document.location).replace(/http:\/\/[^\/]*/,'').replace(/\/examples\/.*$/,'')+"../cometd");
