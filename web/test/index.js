
function init() {
  DWREngine.setErrorHandler(function(message) { alert(message); });
  DWREngine.setWarningHandler(function(message) { alert(message); });
  DWRUtil.useLoadingMessage();
}

function test1() {
  Test.booleanParam(null, true);
}

function results(message) {
  DWRUtil.addRows("results", message);
}
