
dojo.require("dojo.parser");
dojo.require("dojox.grid.Grid");
dojo.require("dojox.grid._data.model");
dojo.require("DwrStore");

function init() {
  var view = {
    cells: [[
      { name:'Name', field:"name" },
      { name:'Address', field:"address" },
      { name:'Salary', field:"salary" },
      { name:'Number', field:"phoneNumber" }
    ]]
  };
  var grid = dijit.byId("grid");
  grid.setStructure([ view ]);
  grid.store = new DwrStore({ readMethod:People.getAllPeople });

  //People.getAllPeople(function(people) {
  //  grid.setModel(new dojox.grid.data.Objects(null, people));
  //});
}

dojo.addOnLoad(init);


/*
var registry = {
  serialize:function(smd, method, data) {
    // this is called when a client side service method is called
    var i;
    var target = dojox.rpc.getTarget(smd, method);
    // this will generate the target URL if you want that configurable, you may prefer to have it hard-coded
    // ... process the arguments and turn it into a DWR message, data will contain the arguments, which can be an array or an object/map depending on whether how it is called and whether you include parameter names in the SMD...
    return {
      data:'', // this is a string that will be the POST body
      target: target // this is the URL that the POST will go to
    }
  },
  deserialize:function(results) {
    // this is called when the remote call is returned
    // ... process the results ...
   return results;
  }
};

dojox.rpc.envelopeRegistry.register("DWR", function(str) { return str == "DWR"; }, registry);
*/


var dwrTransportRegistry = {
  /**
   * 
   * @param {Object} func  the default executor
   * @param {Object} method the method object from the SMD
   * @param {Object} service
   */
  getExecutor: function(func, method, service) {
    var dotpos = method.name.indexOf(".");
    // For the example above this gives remoteProxy = "Remoted"
    var remoteProxy = method.name.substring(0, dotpos);
    // 
    return function() {
      return dwr.engine._execute(CallCenter._path, 'CallCenter', 'load', callback);
    };
  }
};

dojox.rpc.transportRegistry.register("DWR", function(str) { return str == "DWR"; }, dwrTransportRegistry);
