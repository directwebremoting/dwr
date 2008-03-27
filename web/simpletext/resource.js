
function forward() {
  Demo.getInclude(function(data) {
    DWRUtil.setValue("forward", data);
  });
}
