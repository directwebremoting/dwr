
var arrayFive = [ 'One', 'Two', 'Three', 'Four', 'Five' ];

var arrayObject = [
  { name:'One', value:'1' },
  { name:'Two', value:'2' },
  { name:'Three', value:'3' },
  { name:'Four', value:'4' },
  { name:'Five', value:'5' }
];

var map = {
  one:1,
  two:2,
  three:3,
  four:4,
  five:5
};

function success() {
}

function failure(message) {
  alert(message);
}

function runTests() {
  DWRUtil.selectRange("selectRangeBasic", 1, 2);
  if (getSelectedText() == "1") success();
  else failure("DWRUtil.selectRange('selectRangeBasic', 1, 2); -> " + getSelectedText());

  DWRUtil.selectRange("selectRangeBasic", 0, 20);
  if (getSelectedText() == "01234567890123456789") success();
  else failure("DWRUtil.selectRange('selectRangeBasic', 0, 20); -> " + getSelectedText());

  DWRUtil.selectRange("selectRangeBasic", 1, 1);
  if (getSelectedText() == "") success();
  else failure("DWRUtil.selectRange('selectRangeBasic', 1, 1); -> " + getSelectedText());

  DWRUtil.selectRange("selectRangeBasic", 5, 15);
  if (getSelectedText() == "5678901234") success();
  else failure("DWRUtil.selectRange('selectRangeBasic', 5, 15); -> " + getSelectedText());

  DWRUtil.removeAllOptions('removeOptions');

  DWRUtil.addOptions('addOptionsBasic', arrayFive);
  if (DWRUtil.getValue('addOptionsBasic') == "One") success();
  else failure("DWRUtil.getValue('addOptionsBasic') = " + DWRUtil.getValue('addOptionsBasic'));
  if (DWRUtil.getText('addOptionsBasic') == "One") success();
  else failure("DWRUtil.getText('addOptionsBasic') = " + DWRUtil.getText('addOptionsBasic'));

  DWRUtil.addOptions('addOptionsObject1', arrayObject, "name");
  if (DWRUtil.getValue('addOptionsObject1') == "One") success();
  else failure("DWRUtil.getValue('addOptionsObject1') = " + DWRUtil.getValue('addOptionsObject1'));
  if (DWRUtil.getText('addOptionsObject1') == "One") success();
  else failure("DWRUtil.getText('addOptionsObject1') = " + DWRUtil.getText('addOptionsObject1'));

  DWRUtil.addOptions('addOptionsObject2', arrayObject, "name", "value");
  if (DWRUtil.getValue('addOptionsObject2') == "One") success();
  else failure("DWRUtil.getValue('addOptionsObject2') = " + DWRUtil.getValue('addOptionsObject2'));
  if (DWRUtil.getText('addOptionsObject2') == "1") success();
  else failure("DWRUtil.getText('addOptionsObject2') = " + DWRUtil.getText('addOptionsObject2'));

  DWRUtil.addOptions('addOptionsObject3', arrayObject, "value");
  if (DWRUtil.getValue('addOptionsObject3') == "1") success();
  else failure("DWRUtil.getValue('addOptionsObject3') = " + DWRUtil.getValue('addOptionsObject3'));
  if (DWRUtil.getText('addOptionsObject3') == "1") success();
  else failure("DWRUtil.getText('addOptionsObject3') = " + DWRUtil.getText('addOptionsObject3'));

  DWRUtil.addOptions('addOptionsObject4', arrayObject, "value", "name");
  if (DWRUtil.getValue('addOptionsObject4') == "1") success();
  else failure("DWRUtil.getValue('addOptionsObject4') = " + DWRUtil.getValue('addOptionsObject4'));
  if (DWRUtil.getText('addOptionsObject4') == "One") success();
  else failure("DWRUtil.getText('addOptionsObject4') = " + DWRUtil.getText('addOptionsObject4'));

  DWRUtil.addOptions('addOptionsMap1', map);
  if (DWRUtil.getValue('addOptionsMap1') == "one") success();
  else failure("DWRUtil.getValue('addOptionsMap1') = " + DWRUtil.getValue('addOptionsMap1'));
  if (DWRUtil.getText('addOptionsMap1') == "1") success();
  else failure("DWRUtil.getText('addOptionsMap1') = " + DWRUtil.getText('addOptionsMap1'));

  DWRUtil.addOptions('addOptionsMap2', map, true);
  if (DWRUtil.getValue('addOptionsMap2') == "1") success();
  else failure("DWRUtil.getValue('addOptionsMap2') = " + DWRUtil.getValue('addOptionsMap2'));
  if (DWRUtil.getText('addOptionsMap2') == "one") success();
  else failure("DWRUtil.getText('addOptionsMap2') = " + DWRUtil.getText('addOptionsMap2'));

  DWRUtil.removeAllOptions('removeItems');
  DWRUtil.addOptions('addItemsBasic', arrayFive);

  /*
  DWRUtil.addOptions('addItemsObject1', arrayObject, "name");
  DWRUtil.addOptions('addItemsObject2', arrayObject, "name", "value");
  DWRUtil.addOptions('addItemsObject3', arrayObject, "value");
  DWRUtil.addOptions('addItemsObject4', arrayObject, "value", "name");
  */

  DWRUtil.addRows('addRowsBasic', arrayFive, [
    function(data) { return data; },
    function(data) { return data.toUpperCase(); },
    function(data) {
      var input = document.createElement("input");
      input.setAttribute("type", "button");
      input.setAttribute("value", "DOM Test");
      input.setAttribute("onclick", "alert('" + data + "');");
      return input;
    },
    function(data) { return "<input type='button' value='innerHTML Test' onclick='alert(\"" + data + "\");'>"; }
  ]);

  var settings = {
    setValuesDiv:"setValuesDiv",
    setValuesSpan:"setValuesSpan",
    setValuesSelect:"two",
    setValuesText:"setValuesText",
    setValuesPassword:"AB",
    setValuesTextarea:"setValuesTextarea",
    setValuesButton1:"B1-Two",
    setValuesButton2:"B2-Two",
    setValuesRadio1:true,
    setValuesRadio2:false,
    setValuesRadio3:"one",
    setValuesRadio4:"two",
    setValuesCheckbox1:true,
    setValuesCheckbox2:false
  };
  DWRUtil.setValues(settings);

  var empty = {
    setValuesDiv:null,
    setValuesSpan:null,
    setValuesSelect:null,
    setValuesText:null,
    setValuesPassword:null,
    setValuesTextarea:null,
    setValuesButton1:null,
    setValuesButton2:null,
    setValuesRadio1:null,
    setValuesRadio2:null,
    setValuesRadio3:null,
    setValuesRadio4:null,
    setValuesCheckbox1:null,
    setValuesCheckbox2:null
  };
  DWRUtil.getValues(empty);
  var reply = testEquals(settings, empty, 0);
  if (reply != true) {
    alert(reply);
  }
}

function submitFunction() {
  $("alert").style.display = "inline";
  setTimeout("$('alert').style.display = 'none';", 1000);
}

