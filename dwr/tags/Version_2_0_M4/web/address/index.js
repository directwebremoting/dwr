
function fillAddress() {
  var postcode = DWRUtil.getValue("postcode");
  AddressLookup.fillAddress(postcode, function(address) {
    DWRUtil.setValue("line2", address.line2);
    DWRUtil.setValue("line3", address.line3);
    DWRUtil.setValue("line4", address.line4);
  });
}
