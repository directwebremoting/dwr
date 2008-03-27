
/**
 * From Justin Ashworth
 */
dwr.util.getValues = function(data) {
  var ele;
  if (typeof data == "string") ele = $(data);
  if (dwr.util._isHTMLElement(data)) ele = data;
  if (ele != null) {
    if (ele.elements == null) {
      alert("getValues() requires an object or reference to a form element.");
      return null;
    }
    var reply = {};
    var value;
    for (var i = 0; i < ele.elements.length; i++) {
     // Skip checkboxes which aren't checked.  This mimics the same behavior as a form submit.
     if ((ele[i].type == 'checkbox' || ele[i].type == 'check-box') && !ele[i].checked)
       continue;

     if (ele[i].id != null && ele[i].id.length > 0) value = ele[i].id;
     else if (ele[i].value != null) value = ele[i].value;
     else value = "element" + i;

     // Retrieve the value of this form element
     var tmpVal = dwrl.util.getValue(ele[i]);

     // If there is an existing value for this slot in the reply array, convert the entry to an array, adding all
     // previous values and the new value.
     if (reply[value] != null)
      {
          var existing = reply[value];
          var tmpArr = new Array();
          if (isArray(existing))
          {
              var j = 0;
              for (j=0; j < existing.length; j++)
              {
                  tmpArr[j] = existing[j];
              }

              tmpArr[j] = tmpVal;
          }
          else
          {
              tmpArr[0] = existing;
              tmpArr[1] = tmpVal;
          }

          reply[value] = tmpArr;
      }
      else
      {
          reply[value] = tmpVal;
      }
    }
    return reply;
  }
  else {
    for (var property in data) {
      // Are there any elements with that id or name
      if ($(property) != null || document.getElementsByName(property).length >= 1) {
        // TODO: Probably want to check for an existing value here, as we do above
        data[property] = dwr.util.getValue(property);
      }
    }
    return data;
  }
};
