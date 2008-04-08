
function giLoaded() {
  Corporations.getCorporations(function(corporations) {
    var cdf = dwr.gi.toCdfDocument(corporations, "jsxroot");
    giApp.getCache().setDocument("corporations", cdf);
    giApp.getJSXByName('matrix').repaint();
  });
}
