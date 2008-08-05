
/**
 *
 */
function testAreIdentical() {
  var data = [ 1, 2, 3, 4 ];
  Test.areIdentical(data, data, function(reply) {
    assertTrue(reply);
  });
}
