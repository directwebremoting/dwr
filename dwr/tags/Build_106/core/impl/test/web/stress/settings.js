
function init() {
  Settings.getSettingValues(function(map) {
    dwr.util.addRows("settings", map, [
      function(setting) {
        return "<span title='" + setting.classNames.join("<br/>") + "'>" + setting.name + "</span>";
      },
      function(setting) {
        if (setting.writable) {
          return "<input id='setting." + setting.name + "'" +
                 " value='" + setting.value + "'" +
                 " type='text'" +
                 " onchange='settingSave(this)'/> &nbsp;" +
                 "<span class='small' id='reply." + setting.name + "'></span>";
        } else {
          return setting.value;
        }
      }
    ], { escapeHtml:false });
  });
}

function settingSave(ele) {
  var name = ele.id.replace(/setting\./, "");
  var value = dwr.util.getValue(ele);
  Settings.setValue(name, value, function(reply) {
    var name = ele.id.replace(/setting/, "reply");
    dwr.util.setValue(name, reply);
    setTimeout(function() { dwr.util.setValue(name, ""); }, 3000);
  });
}
