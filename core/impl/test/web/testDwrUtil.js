
createTestGroup("DwrUtil");

/**
 *
 */
function testDwrUtilXss() {
  assertTrue(dwr.util.containsXssRiskyCharacters("dd<"));
  assertFalse(dwr.util.containsXssRiskyCharacters("dd"));
}

var arrayFive = [ 'One', 'Two', 'Three', 'Four', 'Five' ];
var arrayObject = [
  { name:'One', value:'1' },
  { name:'Two', value:'2' },
  { name:'Three', value:'3' },
  { name:'Four', value:'4' },
  { name:'Five', value:'5' }
];
var map = { one:1, two:2, three:3, four:4, five:5 };

/**
 * 
 */
function testDwrUtilAddOptionsBasic() {
  useHtml('<select id="addOptionsBasic"> </select>');

  dwr.util.addOptions('addOptionsBasic', arrayFive);

  assertEqual("One", dwr.util.getValue('addOptionsBasic'));
  assertEqual("One", dwr.util.getText('addOptionsBasic'));
  assertTrue(dwr.util.byId('addOptionsBasic').innerHTML.match(/option/));
}

/**
 * 
 */
function testDwrUtilRemoveAllOptions() {
  useHtml('<select id="removeAllOptions">' +
    '<option value="v1">One</option>' +
    '<option value="v2">Two</option>' +
    '<option value="v3">Three</option>' +
    '<option value="v4">Four</option>' +
    '<option value="v5">Five</option>' +
    '</select>');

  dwr.util.removeAllOptions('removeAllOptions');
  var value = dwr.util.getValue('removeAllOptions');
  // TODO: shouldn't this be better defined?
  assertTrue(value == null || value.length == 0);
  assertFalse(dwr.util.byId('removeAllOptions').innerHTML.match(/option/));
}

/**
 * 
 */
function testDwrUtilAddOptionsObject1() {
  useHtml('<select id="addOptionsObject1"> </select>');

  dwr.util.addOptions('addOptionsObject1', arrayObject, "name");

  assertEqual("One", dwr.util.getValue('addOptionsObject1'));
  assertEqual("One", dwr.util.getText('addOptionsObject1'));
  assertTrue(dwr.util.byId('addOptionsObject1').innerHTML.match(/option/));
}

/**
 * 
 */
function testDwrUtilAddOptionsObject2() {
  useHtml('<select id="addOptionsObject2"> </select>');

  dwr.util.addOptions('addOptionsObject2', arrayObject, "name", "value");

  assertEqual("One", dwr.util.getValue('addOptionsObject2'));
  assertEqual("1", dwr.util.getText('addOptionsObject2'));
  assertTrue(dwr.util.byId('addOptionsObject2').innerHTML.match(/option/));
}

/**
 * 
 */
function testDwrUtilAddOptionsObject3() {
  useHtml('<select id="addOptionsObject3"> </select>');

  dwr.util.addOptions('addOptionsObject3', arrayObject, "value");

  assertEqual("1", dwr.util.getValue('addOptionsObject3'));
  assertEqual("1", dwr.util.getText('addOptionsObject3'));
  assertTrue(dwr.util.byId('addOptionsObject3').innerHTML.match(/option/));
}

/**
 * 
 */
function testDwrUtilAddOptionsObject4() {
  useHtml('<select id="addOptionsObject4"> </select>');

  dwr.util.addOptions('addOptionsObject4', arrayObject, "value", "name");

  assertEqual("1", dwr.util.getValue('addOptionsObject4'));
  assertEqual("One", dwr.util.getText('addOptionsObject4'));
  assertTrue(dwr.util.byId('addOptionsObject4').innerHTML.match(/option/));
}

/**
 * 
 */
function testDwrUtilAddOptionsMap1() {
  useHtml('<select id="addOptionsMap1"> </select>');

  dwr.util.addOptions('addOptionsMap1', map);

  assertEqual("one", dwr.util.getValue('addOptionsMap1'));
  assertEqual("1", dwr.util.getText('addOptionsMap1'));
  assertTrue(dwr.util.byId('addOptionsMap1').innerHTML.match(/option/));
}

/**
 * 
 */
function testDwrUtilAddOptionsMap2() {
  useHtml('<select id="addOptionsMap2"> </select>');

  dwr.util.addOptions('addOptionsMap2', map, true);

  assertEqual("1", dwr.util.getValue('addOptionsMap2'));
  assertEqual("one", dwr.util.getText('addOptionsMap2'));
  assertTrue(dwr.util.byId('addOptionsMap2').innerHTML.match(/option/));
}

/**
 * 
 */
function testDwrUtilRemoveItems() {
  useHtml('<ul id="removeItems">' +
    '<li>One</li> <li>Two</li> <li>Three</li> <li>Four</li> <li>Five</li>' +
    '</ul>');

  dwr.util.removeAllOptions('removeItems');

  var value = dwr.util.getValue('removeItems');
  // TODO: shouldn't this be better defined?
  assertTrue(value == null || value.length == 0);
  assertFalse(dwr.util.byId('removeItems').innerHTML.match(/option/));
}

/**
 * 
 */
function testDwrUtilAddItemsBasic() {
  useHtml('<ul id="addItemsBasic"> </ul>');

  dwr.util.addOptions('addItemsBasic', arrayFive);

  var innerHtml = dwr.util.byId('addItemsBasic').innerHTML;
  assertTrue(innerHtml.match(/li/));
  assertTrue(innerHtml.match(/One/));
  assertFalse(innerHtml.match(/1/));
}

/**
 * 
 */
function testDwrUtilAddItemsObject1() {
  useHtml('<ul id="addItemsObject1"> </ul>');

  dwr.util.addOptions('addItemsObject1', arrayObject, "name");

  var innerHtml = dwr.util.byId('addItemsObject1').innerHTML;
  assertTrue(innerHtml.match(/li/));
  assertTrue(innerHtml.match(/One/));
  assertFalse(innerHtml.match(/1/));
}

/**
 * 
 */
function testDwrUtilAddItemsObject2() {
  useHtml('<ul id="addItemsObject2"> </ul>');

  dwr.util.addOptions('addItemsObject2', arrayObject, "name", "value");

  var innerHtml = dwr.util.byId('addItemsObject2').innerHTML;
  assertTrue(innerHtml.match(/li/));
  assertTrue(innerHtml.match(/One/));
  assertFalse(innerHtml.match(/1/));
}

/**
 * 
 */
function testDwrUtilAddItemsObject3() {
  useHtml('<ul id="addItemsObject3"> </ul>');

  dwr.util.addOptions('addItemsObject3', arrayObject, "value");

  var innerHtml = dwr.util.byId('addItemsObject3').innerHTML;
  assertTrue(innerHtml.match(/li/));
  assertFalse(innerHtml.match(/One/));
  assertTrue(innerHtml.match(/1/));
}

/**
 * 
 */
function testDwrUtilAddItemsObject4() {
  useHtml('<ul id="addItemsObject4"> </ul>');

  dwr.util.addOptions('addItemsObject4', arrayObject, "value", "name");

  var innerHtml = dwr.util.byId('addItemsObject4').innerHTML;
  assertTrue(innerHtml.match(/li/));
  assertFalse(innerHtml.match(/One/));
  assertTrue(innerHtml.match(/1/));
}

/**
 * 
 */
function testDwrUtilCloneNode1() {
  useHtml('<div id="cloneNode1">' +
    '<ul id="cloneNode1Inner1">' +
    '<li>One</li><li>Two</li><li>Three</li><li>Four</li>' +
    '</ul>' +
    '<select id="cloneNode1Inner2">' +
    '<option value="v1">One</option><option value="v2">Two</option>' +
    '<option value="v3">Three</option><option value="v4">Four</option>' +
    '</select>' +
    '</div>');

  dwr.util.cloneNode('cloneNode1', { idPrefix:'pre', idSuffix:'suf' });
  dwr.util.byId("precloneNode1suf").style.backgroundColor = "#AFA";
  dwr.util.setValue("precloneNode1Inner2suf", "v2");
  var clonetext = dwr.util.getValue("precloneNode1Inner1suf", { textContent:true });
  dwr.util.setValue("cloneNode1Results", clonetext);

  // TODO: we need to assert more
  assertNotNull(dwr.util.byId('precloneNode1suf'));
  assertNotNull(dwr.util.byId('precloneNode1Inner2suf'));
  assertNotNull(dwr.util.byId('precloneNode1Inner1suf'));
}

/**
 * 
 */
function testDwrUtilAddRowsBasic() {
  useHtml('<table border="1" id="addRowsBasic"></table>');

  dwr.util.addRows('addRowsBasic', arrayFive, [
    function(data) { return data; },
    function(data) { return data.toUpperCase(); },
    function(data) {
      var input = document.createElement("input");
      input.setAttribute("type", "button");
      input.setAttribute("value", "DOM Test");
      return input;
    },
    function(data) { return "<input type='button' value='innerHTML Test'>"; }
  ], { escapeHtml:true });

  var innerHtml = dwr.util.byId('addRowsBasic').innerHTML;
  assertTrue(innerHtml.match(/One/));
  assertTrue(innerHtml.match(/ONE/));
  assertTrue(innerHtml.match(/td/));
  assertTrue(innerHtml.match(/innerHTML Test/));
  assertTrue(innerHtml.match(/DOM Test/));
}

