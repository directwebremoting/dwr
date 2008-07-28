
function testNothing() {
}

function testWillFail() {
  verifyTrue(false);
  fail("This should fail");
  verifyFalse(true);
}

function testWillFailLater() {
  setTimeout(createDelayed(function() {
    verifyTrue(false);
    fail("This should fail");
    verifyFalse(true);
  }), 1000);
}

function testWillDelayError() {
  setTimeout(createDelayedError(function() {
    verifyTrue(false);
  }), 1000);
}

function testWillNotComplete() {
  createDelayed(function() {
    verifyTrue(true);
  });
}

function testSyncAsync() {
  createDelayed(function() {
    verifyTrue(true);
  })();
}
