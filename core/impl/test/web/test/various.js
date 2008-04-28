
function init() {
  var prop;
  for (prop in window) {
    if (prop.match(/test/) && typeof window[prop] == "function" && prop != "testEquals") {
      dwr.util.addOptions("buttons", [ prop ], function(data) {
        return "<button onclick='dwr.util.setValue(\"output\", \"\"); " + prop + "()'>" + prop.substring(4) + "</button>";
        // return "<input name='submit' type='image' src='imagebutton.png' onclick='dwr.util.setValue(\"output\", \"\"); " + prop + "()' />";
      }, { escapeHtml:false });
    }
  }
}

function success(message) {
  var output = $('output').innerHTML + "<br/>\n" + message;
  dwr.util.setValue('output', output, { escapeHtml:false });
}

function failure(message) {
  alert(message);
}

function assertTrue(value) {
  if (!value) {
    try { throw new Error(); }
    catch (ex) { alert("Failue at: " + ex.stack); }
  }
}

function assertFalse(value) {
  if (value) {
    try { throw new Error(); }
    catch (ex) { alert(ex.stack); }
  }
}

var PASS = 1;
var FAIL = 0;
var DELAYED = 2;

function completedDelayedResult(testFunction) {
}

function expectDelayedResult(testFunction) {
}

function annotate(testName, message) {
}

function delayNextTest(delay) {
}

/*
Each test must conform to the following:
- Its name must be test*
- It must return one of [PASS|FAIL|DELAYED]
- If the test is lengthy and asynchronous, the test should call delayNextTest(N)
  where N is a delay in milliseconds, before the next test starts
- If the test is asynchronous, it should call expectDelayedResult(X) and then
  call recordDelayedResult(functionName, X, [PASS|FAIL|DELAYED], "message") at
  some later time
- xUnit assert...() functions are available to halt execution
- verify...() functions are also available that do not halt execution
- If you wish to record a failure use fail()
*/
