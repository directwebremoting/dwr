
createTestGroup("JUnit");

/**
 *
 */
function testJUnit() {
  var scratch = currentTest.scratch;
  var noteProgressInScratch = function(passed, failed, total) {
    dwr.util.setValue(scratch, "Pass:" + passed + " Fail:" + failed + " Total:" + total);
  };

  Test.runAllJUnitTests(noteProgressInScratch, createDelayed(function(reply) {
    if (reply != null && reply.length != 0) {
      fail(reply);
    }
  }));
}
