
createTestGroup("Combination");

/**
 *
 */
function testCombinationMarshalling() {
  dwr.engine.setAsync(true);
  runTestGroup('Marshall');
  dwr.engine.setAsync(false);
}
