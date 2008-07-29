// http://www.helephant.com/Article.aspx?ID=675

var tests = {};
var groups = { 'Global':[] };
var groupNames = [ 'Global' ];

var currentTest = null;

var status = { notrun:0, executing:1, asynchronous:2, pass:3, fail:4 };
var statusBackgrounds = [ "#EEE", "#888", "#FFA", "#8F8", "#F00" ];
var statusColors = [ "#000", "#FFF", "#000", "#000", "#FFF" ];

/**
 *
 */
function init() {
  for (var prop in window) {
    if (prop.match(/test/) && typeof window[prop] == "function" && prop != "testEquals") {
      addTest(prop, window[prop]);
    }
  }
  displayTestTable();
  updateTestResults();
  if (window.location.href.match(/autoRun=true/)) {
    runAllTests();
  }
}

/**
 *
 */
function addTest(testName, test) {
  var groupName = "Global";
  for (var i = 0; i < groupNames.length; i++) {
    if (testName.indexOf("test" + groupNames[i]) == 0) {
      groupName = groupNames[i];
    }
  }

  groups[groupName].push(testName);
  tests[testName] = test;
  test.name = testName;
  test.status = status.notrun;
  test.group = groupName;
}

/**
 * 
 */
function createTestGroup(groupName) {
  groupNames.push(groupName);
  groups[groupName] = [];
}

/**
 *
 */
function displayTestTable() {
  for (var i = 0; i < groupNames.length; i++) {
    var groupName = groupNames[i];
    var testNames = groups[groupName];
    testNames.sort();

    dwr.util.cloneNode("groupTemplate", { idSuffix:groupName });

    dwr.util.setValue("groupTitle" + groupName, groupName);
    dwr.util.byId("groupDetail" + groupName).style.display = "none";

    dwr.util.addRows("groupTests" + groupName, testNames, [
      function num(testName, options) { return options.rowNum + 1; },
      function name(testName, options) {
        if (groupName == "Global") {
          return _addSpaces(testName.substring(4));
        }
        else {
          return _addSpaces(testName.substring(4 + groupName.length));
        }
      },
      function async(testName, options) {
        return "<span id='asyncReturn" + testName + "'>0</span>/<span id='asyncSent" + testName + "'>0</span>";
      },
      function action(testName, options) {
        return "<input type='button' value='Run' onclick='runTest(\"" + testName + "\");'/>";
      },
      function results(testName, options) { return ""; },
      function scratchSpace(testName, options) { return "<div id='scratch" + testName + "'></div>"; }
    ], {
      escapeHtml:false,
      cellCreator:function(options) {
        var td = document.createElement("td");
        if (options.cellNum == 4) {
          td.setAttribute("id", options.rowData);
        }
        return td;
      }
    });
  }

  dwr.util.addRows("testSummary", groupNames, [
    function name(groupName) {
      return groupName + '<span class="headInline"> [' +
        '<a href="#" id="groupDisplay' + groupName + '" onclick="_toggleDetail(\'' + groupName + '\');">Show</a>' +
        ']</span>';
    },
    function started(groupName) { return ""; },
    function outstanding(groupName) { return ""; },
    function failed(groupName) { return ""; },
    function passed(groupName) { return ""; },
    function count(groupName) { return ""; },
    function actions(groupName) { return '<input type="button" value="Run" onclick="runTestGroup(\'' + groupName + '\')"/>'; }
  ], {
    escapeHtml:false,
    cellCreator:function(options) {
      if (options.cellNum == 0) {
        return document.createElement("th");
      }
      var td = document.createElement("td");
      if (options.cellNum == 1) td.setAttribute("id", "groupStarted" + options.rowData);
      if (options.cellNum == 2) td.setAttribute("id", "groupOutstanding" + options.rowData);
      if (options.cellNum == 3) td.setAttribute("id", "groupFailed" + options.rowData);
      if (options.cellNum == 4) td.setAttribute("id", "groupPassed" + options.rowData);
      if (options.cellNum == 5) td.setAttribute("id", "groupCount" + options.rowData);
      return td;
    }
  });
  dwr.util.addRows("testSummary", [ 1 ], [
    function name() { return "All"; },
    function started() { return ""; },
    function outstanding() { return ""; },
    function failed() { return ""; },
    function passed() { return ""; },
    function count() { return ""; },
    function actions() { return '<input type="button" value="Run All" onclick="runAllTests();"/>'; }
  ], {
    escapeHtml:false,
    cellCreator:function(options) {
      if (options.cellNum == 0) {
        return document.createElement("th");
      }
      var td = document.createElement("td");
      if (options.cellNum == 1) td.setAttribute("id", "testsStarted");
      if (options.cellNum == 2) td.setAttribute("id", "testsOutstanding");
      if (options.cellNum == 3) td.setAttribute("id", "testsFailed");
      if (options.cellNum == 4) td.setAttribute("id", "testsPassed");
      if (options.cellNum == 5) td.setAttribute("id", "testCount");
      return td;
    }
  });

  var test;
  for (var testName in tests) {
    test = tests[testName];
    test.scratch = dwr.util.byId("scratch" + testName);
  }
}

/**
 *
 */
function _toggleDetail(groupName) {
  var detail = dwr.util.byId("groupDetail" + groupName);
  if (detail.style.display == "none") {
    detail.style.display = "block";
    dwr.util.setValue("groupDisplay" + groupName, "Hide");
  }
  else {
     detail.style.display = "none";
     dwr.util.setValue("groupDisplay" + groupName, "Show");
  }
}

/**
 *
 */
function updateTestResults() {
  var counts = [ 0, 0, 0, 0, 0 ];
  var groupCounts = {};
  for (var i = 0; i < groupNames.length; i++) {
    groupCounts[groupNames[i]] = [ 0, 0, 0, 0, 0 ];
  }

  var testCount = 0;
  for (var testName in tests) {
    var test = tests[testName];
    counts[test.status]++;
    groupCounts[test.group][test.status]++
    testCount++;
  }

  var outstanding = counts[status.asynchronous] + counts[status.executing];
  var failed = counts[status.fail];
  var passed = counts[status.pass];

  dwr.util.setValue("testCount", testCount);
  dwr.util.setValue("testsStarted", testCount - counts[status.notrun]);
  dwr.util.setValue("testsOutstanding", outstanding);
  dwr.util.setValue("testsFailed", failed);
  dwr.util.setValue("testsPassed", passed);

  dwr.util.byId("testsOutstanding").style.backgroundColor = "";
  dwr.util.byId("testsOutstanding").style.color = "";
  dwr.util.byId("testsFailed").style.backgroundColor = "";
  dwr.util.byId("testsFailed").style.color = "";
  dwr.util.byId("testsPassed").style.backgroundColor = "";
  dwr.util.byId("testsPassed").style.color = "";

  if (failed > 0) {
    dwr.util.byId("testsFailed").style.backgroundColor = statusBackgrounds[status.fail];
    dwr.util.byId("testsFailed").style.color = statusColors[status.fail];
  }
  if (outstanding > 0 && failed > 0) {
    dwr.util.byId("testsOutstanding").style.backgroundColor = statusBackgrounds[status.asynchronous];
    dwr.util.byId("testsOutstanding").style.color = statusColors[status.asynchronous];
  }
  if (passed == testCount) {
    dwr.util.byId("testsPassed").style.backgroundColor = statusBackgrounds[status.pass];
    dwr.util.byId("testsPassed").style.color = statusColors[status.pass];
  }

  for (var i = 0; i < groupNames.length; i++) {
    var groupName = groupNames[i];
    var groupCount = groups[groupName].length;
    var groupStatus = groupCounts[groupName];

    outstanding = groupStatus[status.asynchronous] + groupStatus[status.executing];
    failed = groupStatus[status.fail];
    passed = groupStatus[status.pass];

    dwr.util.setValue("groupCount" + groupName, groupCount);
    dwr.util.setValue("groupStarted" + groupName, groupCount - groupStatus[status.notrun]);
    dwr.util.setValue("groupOutstanding" + groupName, outstanding);
    dwr.util.setValue("groupFailed" + groupName, failed);
    dwr.util.setValue("groupPassed" + groupName, passed);

    dwr.util.byId("groupOutstanding" + groupName).style.backgroundColor = "";
    dwr.util.byId("groupOutstanding" + groupName).style.color = "";
    dwr.util.byId("groupFailed" + groupName).style.backgroundColor = "";
    dwr.util.byId("groupFailed" + groupName).style.color = "";
    dwr.util.byId("groupPassed" + groupName).style.backgroundColor = "";
    dwr.util.byId("groupPassed" + groupName).style.color = "";

    if (failed > 0) {
      dwr.util.byId("groupFailed" + groupName).style.backgroundColor = statusBackgrounds[status.fail];
      dwr.util.byId("groupFailed" + groupName).style.color = statusColors[status.fail];
    }
    if (outstanding > 0 && failed > 0) {
      dwr.util.byId("groupOutstanding" + groupName).style.backgroundColor = statusBackgrounds[status.asynchronous];
      dwr.util.byId("groupOutstanding" + groupName).style.color = statusColors[status.asynchronous];
    }
    if (passed == groupCount) {
      dwr.util.byId("groupPassed" + groupName).style.backgroundColor = statusBackgrounds[status.pass];
      dwr.util.byId("groupPassed" + groupName).style.color = statusColors[status.pass];
    }
  }
}

/**
 *
 */
function runTest(testName) {
  currentTest = tests[testName];
  _setStatus(currentTest, status.executing, true);
  dwr.util.setValue(currentTest.name, "");

  var scope = currentTest.scope || window;
  try {
    currentTest.apply(scope);
  }
  catch (ex) {
    _setStatus(currentTest, status.fail);
    if (ex.message && ex.message.length > 0) {
      _record(currentTest, ex.message);
    }
    console.trace();
  }
  if (_getStatus(currentTest) == status.executing) {
    _setStatus(currentTest, status.pass, true);
  }
  currentTest = null;
  updateTestResults();
}

/**
 *
 */
function runTestGroup(groupName) {
  var testNames = groups[groupName];
  for (var i = 0; i < testNames.length; i++) {
    runTest(testNames[i]);
  }
}

/**
 *
 */
function runAllTests() {
  var testNames = _getTestNames();
  var delay = dwr.util.getValue("delay");
  var pauseAndRun = function(index) {
    if (index >= testNames.length) {
      return;
    }
    var testName = testNames[index];
    runTest(testName);

    setTimeout(function() {
      pauseAndRun(index + 1);
    }, delay);
  };
  pauseAndRun(0);
}

/**
 *
 */
function createDelayed(func, scope) {
  _setStatus(currentTest, status.asynchronous, true);
  var delayedTest = currentTest;
  if (!delayedTest.outstanding) {
    delayedTest.outstanding = 1;
  }
  else {
    delayedTest.outstanding++;
  }
  var sent = dwr.util.getValue("asyncSent" + currentTest.name) - 0;
  dwr.util.setValue("asyncSent" + currentTest.name, sent + 1);

  return function() {
    var isSync = (currentTest != null);
    currentTest = delayedTest;
    var obj = scope || window;
    if (func) {
      try {
        func.apply(obj, arguments);
      }
      catch (ex) {
        _setStatus(currentTest, status.fail);
        if (ex.message && ex.message.length > 0) {
          _record(currentTest, ex.message);
        }
        console.trace();
      }
    }
    delayedTest.outstanding--;
    if (delayedTest.outstanding == 0 && _getStatus(delayedTest) == status.asynchronous) {
      _setStatus(currentTest, status.pass, true);
    }
    var returned = dwr.util.getValue("asyncReturn" + currentTest.name) - 0;
    dwr.util.setValue("asyncReturn" + currentTest.name, returned + 1);

    if (!isSync) {
      currentTest = null;
    }
    updateTestResults();
  };
}

/**
 *
 */
function createDelayedError(func, scope) {
  var delayedTest = currentTest;
  return function() {
    currentTest = delayedTest;
    _setStatus(currentTest, status.fail);
    _record(currentTest, "Executing delayed error handler");
    var obj = scope || window;
    if (func) {
      func.apply(obj, arguments);
    }
    currentTest = null;
    updateTestResults();
  };
}

/**
 *
 */
function fail(message) {
  _record(currentTest, "fail", message);
  throw new Error();
}

/**
 *
 */
function assertTrue(value) {
  if (!value) {
    _record(currentTest, "assertTrue", value);
    throw new Error();
  }
}

/**
 *
 */
function verifyTrue(value) {
  if (!value) {
    _recordTrace(currentTest, "verifyTrue", value);
  }
}

/**
 *
 */
function assertFalse(value) {
  if (value) {
    _record(currentTest, "assertFalse", value);
    throw new Error();
  }
}

/**
 *
 */
function verifyFalse(value) {
  if (value) {
    _recordTrace(currentTest, "verifyFalse", value);
  }
}

/**
 *
 */
function assertNull(value) {
  if (value !== null) {
    _record(currentTest, "assertNull", value);
    throw new Error();
  }
}

/**
 *
 */
function verifyNull(value) {
  if (value !== null) {
    _recordTrace(currentTest, "verifyNull", value);
  }
}

/**
 *
 */
function assertNotNull(value) {
  if (value === null) {
    _record(currentTest, "assertNotNull", value);
    throw new Error();
  }
}

/**
 *
 */
function verifyNotNull(value) {
  if (value === null) {
    _recordTrace(currentTest, "verifyNotNull", value);
  }
}

/**
 *
 */
function assertUndefined(value) {
  if (value !== undefined) {
    _record(currentTest, "assertUndefined", value);
    throw new Error();
  }
}

/**
 *
 */
function verifyUndefined(value) {
  if (value !== undefined) {
    _recordTrace(currentTest, "verifyUndefined", value);
  }
}

/**
 *
 */
function assertNotUndefined(value) {
  if (value === undefined) {
    _record(currentTest, "assertNotUndefined", value);
    throw new Error();
  }
}

/**
 *
 */
function verifyNotUndefined(value) {
  if (value === undefined) {
    _recordTrace(currentTest, "verifyNotUndefined", value);
  }
}

/**
 *
 */
function assertNaN(value) {
  if (value !== NaN) {
    _record(currentTest, "assertNaN", value);
    throw new Error();
  }
}

/**
 *
 */
function verifyNaN(value) {
  if (value !== NaN) {
    _recordTrace(currentTest, "verifyNaN", value);
  }
}

/**
 *
 */
function assertNotNaN(value) {
  if (value === NaN) {
    _record(currentTest, "assertNotNaN", value);
    throw new Error();
  }
}

/**
 *
 */
function verifyNotNaN(value) {
  if (value === NaN) {
    _recordTrace(currentTest, "verifyNotNaN", value);
  }
}

/**
 *
 */
function assertEqual(value1, value2) {
  if (!_isEqual(value1, value2)) {
    _record(currentTest, "assertEqual", value);
    throw new Error();
  }
}

/**
 *
 */
function verifyEqual(value1, value2) {
  if (!_isEqual(value1, value2)) {
    _recordTrace(currentTest, "verifyEqual", value1, value2);
  }
}

/**
 *
 */
function assertNotEqual(value1, value2) {
  if (_isEqual(value1, value2)) {
    _record(currentTest, "assertNotEqual", value1, value2);
    throw new Error();
  }
}

/**
 *
 */
function verifyNotEqual(value1, value2) {
  if (_isEqual(value1, value2)) {
    _recordTrace(currentTest, "verifyNotEqual", value1, value2);
  }
}

/**
 *
 */
function _recordTrace() {
  _record.apply(this, arguments);
  console.trace();
}

/**
 *
 */
function success(message) {
  _appendMessage(currentTest, message);
}

/**
 *
 */
function _record() {
  console.error(arguments);
  var test = arguments[0];
  _setStatus(test, status.fail);
  var message = arguments[1] + "(";
  for (var i = 2; i < arguments.length; i++) {
    if (i != 2) {
      message += ", ";
    }
    message += arguments[i];
  }
  message += ")";
  _appendMessage(test, message);
}

/**
 *
 */
function _appendMessage(test, message) {
  var existing = dwr.util.getValue(test.name, { escapeHtml:false });
  var output = message;
  if (existing != null && existing.length > 0) {
    output = existing + "<br />" + message;
  }
  dwr.util.setValue(test.name, output, { escapeHtml:false });
}

/**
 *
 */
function _setStatus(test, status, override) {
  if (typeof test == "string") {
    test = tests[test];
  }
  if (test.status < status || override) {
    test.status = status;
  }
  dwr.util.byId(test.name).style.backgroundColor = statusBackgrounds[status];
  dwr.util.byId(test.name).style.color = statusColors[status];
}

/**
 *
 */
function _getStatus(test) {
  if (typeof test == "string") {
    test = tests[test];
  }
  return test.status;  
}

/**
 *
 */
function _isEqual(actual, expected, depth) {
  if (!depth) depth = 0;
  // Rather than failing we assume that it works!
  if (depth > 10) return true;

  if (expected == null) {
    if (actual != null) {
      return "expected: null, actual non-null: " + dwr.util.toDescriptiveString(actual);
    }
    return true;
  }

  if (isNaN(expected)) {
    if (!isNaN(actual)) {
      return "expected: NaN, actual non-NaN: " + dwr.util.toDescriptiveString(actual);
    }
    return true;
  }

  if (actual == null) {
    if (expected != null) {
      return "actual: null, expected non-null: " + dwr.util.toDescriptiveString(expected);
    }
    return true; // we wont get here of course ...
  }

  if (typeof expected == "object") {
    if (!(typeof actual == "object")) {
      return "expected object, actual not an object";
    }

    var actualLength = 0;
    for (var prop in actual) {
      if (typeof actual[prop] != "function" || typeof expected[prop] != "function") {
        var nest = testEquals(actual[prop], expected[prop], depth + 1);
        if (typeof nest != "boolean" || !nest) {
          return "element '" + prop + "' does not match: " + nest;
        }
      }
      actualLength++;
    }

    // need to check length too
    var expectedLength = 0;
    for (prop in expected) expectedLength++;
    if (actualLength != expectedLength) {
      return "expected object size = " + expectedLength + ", actual object size = " + actualLength;
    }
    return true;
  }

  if (actual != expected) {
    return "expected = " + expected + " (type=" + typeof expected + "), actual = " + actual + " (type=" + typeof actual + ")";
  }

  if (expected instanceof Array) {
    if (!(actual instanceof Array)) {
      return "expected array, actual not an array";
    }
    if (actual.length != expected.length) {
      return "expected array length = " + expected.length + ", actual array length = " + actual.length;
    }
    for (var i = 0; i < actual.length; i++) {
      var inner = testEquals(actual[i], expected[i], depth + 1);
      if (typeof inner != "boolean" || !inner) {
        return "element " + i + " does not match: " + inner;
      }
    }

    return true;
  }

  return true;
}

/**
 *
 */
function _addSpaces(funcName) {
  funcName = funcName.replace(/([a-z0-9])([A-Z])/g, "$1 $2");
  funcName = funcName.replace(/([a-zA-Z])([0-9])/g, "$1 $2");
  return funcName;
}

function _getTestNames() {
  var testNames = [];
  for (var testName in tests) {
    testNames.push(testName);
  }
  testNames.sort();
  return testNames;
}

/*
Each test must conform to the following:
- Its name must begin 'test'
- If the test is asynchronous, it should use createDelayed(). For example:
  window.setTimeout(createDelayed(function(args) {...}), 1000);
  An error handler (that should not be called) can be created with createDelayedError()
- xUnit assert...() functions are available to halt execution
- verify...() functions are also available that do not halt execution
- If you wish to record a failure use fail()
- When a function ends it will be assumed that it failed if an assert or verify
  failed or if fail was called. If testDelayed was called then the final
  assessment will only be made when testCompleted is called. The test
  will be considered a failure until this happens.
  Otherwise the test passes ;-)
*/
