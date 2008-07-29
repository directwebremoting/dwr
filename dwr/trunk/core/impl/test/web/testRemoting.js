
createTestGroup("Remoting");

/**
 *
 */
function testRemotingScopeAndArgs() {
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
function testRemotingAsyncNesting() {
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
function testRemotingSyncNesting() {
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
function testRemotingSyncReturning() {
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
function testRemotingSyncCallMetaData() {
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
function testRemotingAsyncCallMetaData() {
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
function testRemotingParameters() {
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
function testRemotingHeaders() {
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

function testRemotingTimeout() {
  Test.waitFor(3000, {
    callback:createDelayedError(),
    timeout:1000,
    errorHandler:createDelayed(function(message, ex) {
      verifyNotNull(ex);
    })
  });
}
