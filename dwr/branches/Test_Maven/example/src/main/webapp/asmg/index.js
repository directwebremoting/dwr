
function verifyName() {
  var name = DWRUtil.getValue("name");
  if (name == "") {
    DWRUtil.setValue("nameError", "Please enter a name.");
  }
  else {
    DWRUtil.setValue("nameError", "");
  }
}

function verifyAddress() {
  var address = DWRUtil.getValue("address");
  EmailValidator.isValid(address, function(valid) {
    DWRUtil.setValue("addressError", valid ? "" : "Please enter a valid email address");
  });
}

function process() {
  var address = DWRUtil.getValue("address");
  var name = DWRUtil.getValue("name");
  Generator.generateAntiSpamMailto(name, address, function(contents) {
    DWRUtil.setValue("outputFull", contents);
    $("output").style.display = "block";
  });
}
