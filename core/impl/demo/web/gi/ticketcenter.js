
function getCaller() {
  return {
    name:ticketcenter.getJSXByName('textName').getValue(),
    address:ticketcenter.getJSXByName('textAddress').getValue(),
    notes:ticketcenter.getJSXByName('textNotes').getValue()
  };
}

function getSelectedId() {
  return ticketcenter.getJSXByName('listCallers').getSelectedNodes().get(0).getAttribute('jsxid');
}
