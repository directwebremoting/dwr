

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

/**
 *
 */
function testScopeAndArgs() {
  var args = [ 1, "two" ];
  var scope = {
    callback:createDelayed(function(data, passedArgs) {
      verifyEqual("data", data);
      verifyEqual(this, scope);
      verifyEqual(passedArgs, args);
    })
  };

  Test.stringParam("data", {
    callback:scope.callback,
    errorHandler:createDelayedError(),
    scope:scope,
    args:args
  });
}

/**
 *
 */
function testXss() {
  assertTrue(dwr.util.containsXssRiskyCharacters("dd<"));
  assertFalse(dwr.util.containsXssRiskyCharacters("dd"));
}

/**
 *
 */
function testAreIdentical() {
  var data = [ 1, 2, 3, 4 ];
  Test.areIdentical(data, data, function(reply) {
    assertTrue(reply);
  });
}

/**
 *
 */
function testError1() {
  var callData = {
    callback:createDelayedError(),
    errorHandler:createDelayedError(),
    warningHandler:createDelayedError(),
    exceptionHandler:createDelayed(function(message, ex) {
      verifyNotNull(message);
      verifyNotNull(ex);
      verifyNotNull(ex.message);
      verifyEqual(typeof ex.message, "string");
      verifyEqual(ex.message, message);
      verifyEqual(ex.javaClassName, "java.lang.NullPointerException");
    })
  };

  callData.httpMethod = "POST";
  callData.rpcType = dwr.engine.XMLHttpRequest;
  Test.throwNPE(callData);

  callData.rpcType = dwr.engine.IFrame;
  Test.throwNPE(callData);

  callData.rpcType = dwr.engine.ScriptTag;
  Test.throwNPE(callData);

  callData.httpMethod = "GET";
  callData.rpcType = dwr.engine.XMLHttpRequest;
  Test.throwNPE(callData);

  callData.rpcType = dwr.engine.IFrame;
  Test.throwNPE(callData);

  callData.rpcType = dwr.engine.ScriptTag;
  Test.throwNPE(callData);
}
testError1.description = "Check exception handling on all remoting types";

/**
 *
 */
function testError2() {
  // Setup
  var oldWarningHandler = dwr.engine._warningHandler;
  var oldErrorHandler = dwr.engine._errorHandler;

  // Check bounce to global error handler
  dwr.engine.setWarningHandler(createDelayedError());
  dwr.engine.setErrorHandler(createDelayed());
  Test.throwNPE();

  // Check bouncing to batch handler
  dwr.engine.setErrorHandler(createDelayedError());
  dwr.engine.beginBatch();
  Test.throwNPE();
  dwr.engine.endBatch({
    errorHandler:createDelayed(),
    warningHandler:createDelayedError()
  });

  // Check bouncing to call handler
  Test.throwNPE({
    errorHandler:createDelayed(),
    warningHandler:createDelayedError()
  });

  // Undo setup
  dwr.engine.setWarningHandler(oldWarningHandler);
  dwr.engine.setErrorHandler(oldErrorHandler);
}

/**
 *
 */
function test404Handling() {
  var oldPath = Test._path;
  Test._path = "/thisPathDoesNotExist/dwr";
  Test.intParam(1, {
    callback:createDelayedError(),
    errorHandler:createDelayed(function(message, ex) {
      verifyNotNull(message);
      verifyNotNull(ex);
      verifyNotNull(ex.message);
      verifyEqual(typeof ex.message, "string");
      verifyEqual(ex.message, message);
      verifyEqual(ex.javaClassName, "java.lang.NullPointerException");
      verifyEqual(ex.name, "dwr.engine.http.404");
      var isHtml = message.match(/<html>/);
      if (!isHtml) {
        fail("Message: ", message);
      }
      verifyTrue(isHtml);
    })
  });
  Test._path = oldPath;
  Test.intParam(1, createDelayed());
}

/**
 *
 */
function testAsyncNesting() {
  var count = 0;
  Test.slowStringParam("1", 100, createDelayed(function(data1) {
    assertEqual(data1, 1);
    count++;
    assertEqual(count, 2);

    Test.slowStringParam("2", 200, createDelayed(function(data2) {
      assertEqual(data2, 2);
      count++;
      assertEqual(count, 4);
    }));

    count++;
    assertEqual(count, 3);
  }));

  count++;
  assertEqual(count, 1);
}

/**
 *
 */
function testSyncNesting() {
  dwr.engine.setAsync(false);
  var count = 0;

  Test.slowStringParam("1", 100, createDelayed(function(data1) {
    assertEqual(data1, "1");
    count++;
    assertEqual(count, 1);

    Test.slowStringParam("2", 200, createDelayed(function(data2) {
      assertEqual(data2, "2");
      count++;
      assertEqual(count, 2);
    }));

    count++;
    assertEqual(count, 3);
  }));

  count++;
  assertEqual(count, 4);

  dwr.engine.setAsync(true);
}

/**
 *
 */
function testSyncReturning() {
  dwr.engine.setAsync(false);
  var data1 = Test.slowStringParam("1", 100);
  assertEqual(data1, "1");
  var data2 = Test.slowStringParam("SyncNesting-2", 100);
  assertEqual(data2, "2");
  dwr.engine.setAsync(true);
}

/**
 *
 */
function testSyncInCallMetaData() {
  var count = 0;
  Test.slowStringParam("1", 100, {
    async:false,
    callback:createDelayed(function(data) {
      assertEqual(data, "1");
      count++;
      assertEqual(count, 1);
    })
  });
  count++;
  assertEqual(count, 2);
}

/**
 *
 */
function testAsyncInCallMetaData() {
  var count = 0;
  Test.slowStringParam("1", 100, {
    async:true,
    callback:function(data) {
      assertEqual(data, "1");
      count++;
      assertEqual(count, 2);
    }
  });
  count++;
  assertEqual(count, 1);
}

/**
 *
 */
function testParameterPassing() {
  Test.listParameters({
    callback:createDelayed(function(data) {
      assertEqual(data.param1, "value1");
      assertEqual(data.param2, "value2");
      assertUndefined(data.param3);
      assertEqual(data.length, 2);
    }),
    parameters:{
      'param1':'value1',
      'param2':'value2'
    }
  });
}

/**
 *
 */
function testHeaderPassing() {
  Test.listHeaders({
    callback:createDelayed(function(data) {
      assertEqual(data.param1, "value1");
      assertEqual(data.param2, "value2");
      assertUndefined(data.param3);
      assertEqual(data.length, 2);
    }),
    headers:{
      'param1':'value1',
      'param2':'value2'
    }
  });
}

/**
 *
 */
function testExceptionDetail1() {
  Test.throwNPE({
    callback:createDelayedError(),
    exceptionHandler:createDelayed(function(message, ex) {
      verifyEqual(message, "NullPointerException");
      verifyNotNull(ex);
      verifyEqual(ex.message, "NullPointerException");
      verifyEqual(ex.javaClassName, "java.lang.NullPointerException");
    })
  });
}

/**
 *
 */
function testExceptionDetail2() {
  Test.throwIAE({
    callback:createDelayedError(),
    exceptionHandler:createDelayed(function(message, ex) {
      verifyEqual(message, "Error");
      verifyNotNull(ex);
      verifyEqual(ex.message, "Error");
      verifyEqual(ex.javaClassName, "java.lang.Throwable");
    })
  });
}

/**
 *
 */
function testExceptionDetail3() {
  Test.throwSPE({
    callback:createDelayedError(),
    exceptionHandler:createDelayed(function(message, ex) {
      verifyEqual(message, "SAXParseException");
      verifyNotNull(ex);
      verifyEqual(ex.message, "SAXParseException");
      verifyEqual(ex.javaClassName, "org.xml.sax.SAXParseException");
      verifyEqual(ex.lineNumber, 42);
      verifyEqual(ex.exception.javaClassName, "java.lang.NullPointerException");
    })
  });
}

function testTimeout() {
  Test.waitFor(7000, {
    callback:createDelayedError(),
    timeout:6000,
    errorHandler:createDelayed(function(message, ex) {
      verifyNotNull(ex);
    })
  });
}
