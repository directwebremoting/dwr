
/**
 *
 */
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

/**
 *
 */
function testServerChecks() {
  Test.serverChecks({
    callback:createDelayed(),
    exceptionHandler:createDelayedError()
  });
}

