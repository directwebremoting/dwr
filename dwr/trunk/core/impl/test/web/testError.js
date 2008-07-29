createTestGroup("Error");

/**
 *
 */
function testErrorTransportTypes() {

  var exceptionHandler = function(message, ex) {
    verifyNotNull(message);
    verifyNotNull(ex);
    verifyNotNull(ex.message);
    verifyEqual(typeof ex.message, "string");
    verifyEqual(ex.message, message);
    verifyEqual(ex.javaClassName, "java.lang.NullPointerException");
  };

  var callData = {
    callback:createDelayedError(),
    errorHandler:createDelayedError(),
    warningHandler:createDelayedError()
  };

  callData.httpMethod = "POST";
  callData.rpcType = dwr.engine.XMLHttpRequest;
  callData.exceptionHandler = createDelayed(exceptionHandler);
  Test.throwNPE(callData);

  callData.rpcType = dwr.engine.IFrame;
  callData.exceptionHandler = createDelayed(exceptionHandler);
  Test.throwNPE(callData);

  callData.rpcType = dwr.engine.ScriptTag;
  callData.exceptionHandler = createDelayed(exceptionHandler);
  Test.throwNPE(callData);

  callData.httpMethod = "GET";
  callData.rpcType = dwr.engine.XMLHttpRequest;
  callData.exceptionHandler = createDelayed(exceptionHandler);
  Test.throwNPE(callData);

  callData.rpcType = dwr.engine.IFrame;
  callData.exceptionHandler = createDelayed(exceptionHandler);
  Test.throwNPE(callData);

  callData.rpcType = dwr.engine.ScriptTag;
  callData.exceptionHandler = createDelayed(exceptionHandler);
  Test.throwNPE(callData);

  callData.rpcType = dwr.engine.XMLHttpRequest;
}

/**
 *
 */
function testErrorLevels() {
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
function testError404Handling() {
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
    })
  });
  Test._path = oldPath;
  Test.intParam(1, createDelayed());
}

/**
 *
 */
function testErrorExceptionDetail() {
  Test.throwNPE({
    callback:createDelayedError(),
    exceptionHandler:createDelayed(function(message, ex) {
      verifyEqual(message, "NullPointerException");
      verifyNotNull(ex);
      verifyEqual(ex.message, "NullPointerException");
      verifyEqual(ex.javaClassName, "java.lang.NullPointerException");
    })
  });

  Test.throwIAE({
    callback:createDelayedError(),
    exceptionHandler:createDelayed(function(message, ex) {
      verifyEqual(message, "Error");
      verifyNotNull(ex);
      verifyEqual(ex.message, "Error");
      verifyEqual(ex.javaClassName, "java.lang.Throwable");
    })
  });

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
