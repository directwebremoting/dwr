
createTestGroup("RemoteDwr");

/**
 * 
 */
function testRemoteDwrSetValue() {
  useHtml('<input id="remoteDwrSetValue" value="start"/>');
  
  Test.setValue("remoteDwrSetValue", "changed", createDelayed(function(data) {
    assertEqual("changed", data);
    var remoteDwrSetValue = dwr.util.getValue("remoteDwrSetValue");
    assertEqual("changed", remoteDwrSetValue);
  }));
}
