
function giLoaded() {
  OpenAjax.subscribe("gidemo", "corporation", objectPublished);
  dwr.engine.setActiveReverseAjax(true);
}

function objectPublished(prefix, name, handlerData, corporation) {
  var matrix = giApp.getJSXByName("matrix");
  var inserted = matrix.getRecordNode(corporation.jsxid);
  matrix.insertRecord(corporation, null, inserted == null);
  matrix.repaintData();
}
