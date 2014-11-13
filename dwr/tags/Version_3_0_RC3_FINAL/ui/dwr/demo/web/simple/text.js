
function update() {
  var name = dwr.util.getValue("demoName");
  Demo.sayHello(name, loadinfo);
}

function loadinfo(data) {
    dwr.util.setValue("demoReply", data);
}
