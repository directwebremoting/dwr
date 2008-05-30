

/*
Each test must conform to the following:
- Its name must begin 'test'
- If the test is lengthy and asynchronous, the test should call delayNextTest(N)
  where N is a delay in milliseconds before the next test should start
- If the test is asynchronous, it should call expectDelayedResult(X) and then
  call completedDelayedResult(X) at some later time
- xUnit assert...() functions are available to halt execution
- verify...() functions are also available that do not halt execution
- If you wish to record a failure use fail()
- When a function ends it will be assumed that it failed if an assert or verify
  failed or if fail was called. If expectDelayedResult was called then the final
  assessment will only be made when completedDelayedResult is called. The test
  will be considered a failure until this happens.
  Otherwise the test passes ;-)
*/
// http://www.helephant.com/Article.aspx?ID=675

function testScopeAndArgs() {
  var args = [ 1, "two" ];
  var scope = {
    callback:function(data, passedArgs) {
      verifyTrue(this === scope, testScopeAndArgs);
      verifyTrue(passedArgs === args, testScopeAndArgs);
      completedDelayedResult(testScopeAndArgs);
    }
  };

  Test.stringParam("data", {
    callback:scope.callback,
    errorHandler:function(msg, ex) {
      failure("Test.stringParam() threw: " + msg, testScopeAndArgs);
    },
    scope:scope,
    args:args
  });
  expectDelayedResult(testScopeAndArgs);
}

function testNothing() {
}

function testWillFail() {
  verifyTrue(false);
  fail();
}

function testWillFailLater() {
  verifyTrue(false);
  fail();
}

/*
function testXss() {
  assertTrue(dwr.util.containsXssRiskyCharacters("dd<"));
  assertFalse(dwr.util.containsXssRiskyCharacters("dd"));
}

function testAreIdentical() {
  var data = [ 1, 2, 3, 4 ];
  Test.areIdentical(data, data, function(reply) {
    assertTrue(reply);
  });
}

function testError1() {
  var counter = 0;
  var exceptionHandler = function(message, ex) {
    if (message == null) failure("message is null");
    else if (ex == null) failure("ex is null");
    else if (ex.message == null) failure("ex.message is null");
    else if (ex.message != message) failure("ex.message [" + ex.message + "] != message [" + message + "]");
    else if (ex.javaClassName != "java.lang.NullPointerException") failure("ex.javaClassName is not NPE [" + ex.javaClassName + "]");
    else counter++;
  };
  var warningHandler = function(data) {
    failure("Wrong call of warningHandler with data: " + data);
  };
  var callback = function(data) {
    failure("Wrong call of callback with data: " + data);
  };
  var errorHandler = function(data) {
    failure("Wrong call of errorHandler with data: " + data);
  };
  var callData = {
    callback:callback,
    errorHandler:errorHandler,
    warningHandler:warningHandler,
    exceptionHandler:exceptionHandler
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
  callData.exceptionHandler = function(message, ex) {
    exceptionHandler(message, ex);
    if (counter != 5) failure("Some errors did not complete (Sync issue?) counter = " + counter);
    else success("Error test passed");
  };
  Test.throwNPE(callData);
}

function testError2() {
  var incorrectHandler = function(data) {
    failure("Wrong call of warningHandler with data: " + data + ". See console for more");
    dwr.engine._debug("Wrong call of warningHandler with data: " + data, true);
  }
  dwr.engine.setWarningHandler(incorrectHandler);
  var counter = 0;
  var correctHandler = function(data) {
    counter++;
  };
  var temp = dwr.engine._errorHandler;

  // Test exceptions bouncing to global errors
  dwr.engine.setErrorHandler(correctHandler);
  Test.throwNPE();

  // Test exceptions bouncing to batch errors
  dwr.engine.setErrorHandler(incorrectHandler);
  dwr.engine.beginBatch();
  Test.throwNPE();
  dwr.engine.endBatch({
    errorHandler:correctHandler,
    warningHandler:incorrectHandler
  });

  // Test exceptions bouncing to call errors
  Test.throwNPE({
    errorHandler:function(data) {
      correctHandler(data);
      if (counter != 3) failure("Some errors did not complete (Sync issue?) counter = " + counter);
      else success("Error test passed");
    },
    warningHandler:incorrectHandler
  });

  dwr.engine.setErrorHandler(temp);
}

function test404Handling() {
  var oldPath = Test._path;
  Test._path = "/thisPathDoesNotExist/dwr";
  Test.intParam(1, {
    callback:function(data) {
      failure("New path did not take effect");
    },
    errorHandler:function(message, ex) {
      if (ex.name != "dwr.engine.http.404") failure("Error name is: " + ex.name);
      else if (ex.message != message) failure("errorHandler message != ex.message");
      else if (!message.match(/<html>/)) failure("Expected HTML in response");
      else success("Correct 404 handling");
    }
  });
  Test._path = oldPath;
  Test.intParam(1, function(data) {
    success("Path handling returned to normal");
  });
}

function testAsyncNesting() {
  Test.slowStringParam("AsyncNesting-2", 100, function(data1) {
    success(data1);
    Test.slowStringParam("AsyncNesting-End", 200, function(data2) {
      success(data2);
    });
    success("AsyncNesting-3");
  });
  success("AsyncNesting-1");
}

function testSyncNesting() {
  dwr.engine.setAsync(false);
  Test.slowStringParam("SyncNesting-1", 100, function(data1) {
    success(data1);
    Test.slowStringParam("SyncNesting-2", 100, function(data2) {
      success(data2);
    });
    success("SyncNesting-3");
  });
  success("SyncNesting-End");
  dwr.engine.setAsync(true); 
}

function testSyncReturning() {
  dwr.engine.setAsync(false);
  var data1 = Test.slowStringParam("SyncNesting-1", 100);
  success(data1);
  var data2 = Test.slowStringParam("SyncNesting-2", 100);
  success(data2);
  success("SyncNesting-End");
  dwr.engine.setAsync(true); 
}

function testSyncInCallMetaData() {
  Test.slowStringParam("SyncInCallMetaData-1", 100, {
    async:false,
    callback:function(data) {
      success(data);
    }
  });
  success("SyncInCallMetaData-End");
}

function testAsyncInCallMetaData() {
  Test.slowStringParam("AsyncInCallMetaData-End", 100, {
    async:true,
    callback:function(data) {
      success(data);
    }
  });
  success("AsyncInCallMetaData-1");
}

function testDefaultAsyncInCallMetaData() {
  Test.slowStringParam("DefaultAsyncInCallMetaData-End", 100, {
    callback:function(data) {
      success(data);
    }
  });
  success("DefaultAsyncInCallMetaData-1");
}

function testParameterPassing() {
  Test.listParameters({
    callback:function(data) {
      if (data.param1 != 'value1') failure("Parameter passing failure: data.param1=" + data.param1);
      else if (data.param2 != 'value2') failure("Parameter passing failure: data.param2=" + data.param2);
      else success("ParameterPassing-1");
    },
    parameters:{
      'param1':'value1',
      'param2':'value2'
    }
  });
}

function testHeaderPassing() {
  Test.listHeaders({
    callback:function(data) {
      if (data.param1 != 'value1') failure("Header passing failure: data.param1=" + data.param1);
      else if (data.param2 != 'value2') failure("Header passing failure: data.param2=" + data.param2);
      else success("HeaderPassing-1");
    },
    headers:{
      'param1':'value1',
      'param2':'value2'
    }
  });
}

function testServerChecks() {
  Test.serverChecks({
    callback:function(data) { success(data); },
    exceptionHandler:function(ex) { fail(ex.message); }
  });
}

function testExceptionDetail1() {
  Test.throwNPE({
    callback:function(data) { fail(data); },
    exceptionHandler:function(message, ex) {
      if (message != "NullPointerException") failure("message is not 'NullPointerException': " + message);
      else if (ex == null) failure("ex is null");
      else if (ex.message != "NullPointerException") failure("ex.message is not 'NullPointerException': " + ex.message);
      else if (ex.javaClassName != "java.lang.NullPointerException") failure("ex.javaClassName is not NPE [" + ex.javaClassName + "]");
      else success("testExceptionDetail1");
    }
  });
}

function testMemoryLeaks(count) {
  if (count == null) {
    count = 0;
  }
  if (count > 10000) {
    success("testMemoryLeaks");
  }
  else {
    var next = count + 1;
    Test.doNothing(function() {
      setTimeout(function() {
        testMemoryLeaks(next);
      }, 0);
    });
  }
}

function testExceptionDetail2() {
  Test.throwIAE({
    callback:function(data) { fail(data); },
    exceptionHandler:function(message, ex) {
      if (message != "Error") failure("message is not 'Error': " + message);
      else if (ex == null) failure("ex is null");
      else if (ex.message != "Error") failure("ex.message is not 'Error': " + ex.message);
      else if (ex.javaClassName != "java.lang.Throwable") failure("ex.javaClassName is not Throwable [" + ex.javaClassName + "]");
      else success("testExceptionDetail2");
    }
  });
}

function testExceptionDetail3() {
  Test.throwSPE({
    callback:function(data) { fail(data); },
    exceptionHandler:function(message, ex) {
      if (message != "SAXParseException") failure("message is not 'SAXParseException': " + message);
      else if (ex == null) failure("ex is null");
      else if (ex.message != "SAXParseException") failure("ex.message is not 'SAXParseException': " + ex.message);
      else if (ex.lineNumber != 42) failure("ex.lineNumber is not 42: " + ex.lineNumber);
      else if (ex.javaClassName != "org.xml.sax.SAXParseException") failure("ex.javaClassName is not SPE [" + ex.javaClassName + "]");
      else if (ex.exception.javaClassName != "java.lang.NullPointerException") failure("ex.exception.javaClassName is not NPE [" + ex.exception.javaClassName + "]");
      else success("testExceptionDetail3");
    }
  });
}
*/