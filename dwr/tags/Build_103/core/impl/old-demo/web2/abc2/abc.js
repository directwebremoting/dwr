
function init() {
    DWRUtil.useLoadingMessage();
    DWREngine.setErrorHandler(new Errors().report);

    services = new Services(Service, Actions.getAllServices, Actions.deleteService);
    service = new Service(Services, Actions.writeService);

    extendsLaters();
    /*
    Customers.init();
    Customer.init();
    Services.init();
    Service.init();
    Sales.init();
    Sale.init();
    Reminders.init();
    Reminder.init();
    */
}

function update() {
}

Object.extend = function(destination, source) {
  for (property in source) {
    //if (!source[property]) alert("source."+property+"="+source[property]);
    destination[property] = source[property];
  }
  return destination;
}

Object.extendLater = function(destination, source) {
    laters.push({ destination:destination, source:source });
}
var laters = [];
function extendsLaters() {
    for (var i = 0; i < laters.length; i++) {
        var dest = laters[i].destination;
        var source = eval("[" + laters[i].source + "][0]");
        Object.extend(dest, source);
    }
}

var Errors = function() {};
Errors.prototype = {
    report:function(message) {
        alert(message);
        update();
    }
};

var Base = function() {};
Base.prototype = {
    baseInitialize:function(baseName) {
        this.baseName = baseName;
        new Draggable(this.baseName + "Div", { handle:this.baseName + "Title" });
    },

    show:function() {
        if (update) update();
        Effect.Appear(this.baseName + "Div");
    },

    hide:function() {
        Effect.Fade(this.baseName + "Div");
    }
};

var Collection = function() {};
Object.extend(Object.extend(Collection.prototype, Base.prototype), {
    collectionInitialize:function(baseName, child, updateFunction, deleteFunction) {
        this.baseInitialize(baseName);
        this.data = null;
        this.child = child;
        this.updateFunction = updateFunction;
        this.deleteFunction = deleteFunction;
    },

    fillTable:function(data) {
        this.data = data;
        DWRUtil.removeAllRows(this.baseName + "TBody");
        DWRUtil.addRows(this.baseName + "TBody", this.data, this.fillTableFunctions);
    },

    getItem:function(id) {
        return this.data[id];
    },

    update:function() {
        this.updateFunction(this.fillTable(this.data));
    },

    remove:function(id, name) {
        if (confirm("Are you sure you want to delete " + name + "?")) {
            this.deleteFunction(this.update, id);
        }
    },

    add:function() {
        this.child.clear();
        this.child.show();
    }
});

var Customers = {};
Object.extend(Customers, Collection);
Object.extend(Customers, {
    baseName:"customers",

    fillTableFunctions:[
        function getName(id) {
            return this.data[id].name;
        },
    ]
});
Object.extendLater(Customers, "{ child:Customer, updateFunction:Actions.getAllCustomers, deleteFunction:Actions.deleteCustomer }");

var Services = Class.create();
Object.extend(Object.extend(Services.prototype, Collection.prototype), {
    initialize:function(child, updateFunction, deleteFunction) {
        this.collectionInitialize("services", child, updateFunction, deleteFunction);
    },

    fillTableFunctions:[
        function getName(servid) {
            return this.data[servid].serviceName /*+ "<small>(" + servid + ")</small>"*/;
        },

        function getDeftPrice(servid) {
            return "$" + this.data[servid].defaultPrice;
        },

        function getBillFreq(servid) {
            return this.frequencies[this.data[servid].billingFreq];
        },

        function getActions(servid) {
            return "<input onclick='Service.read(" + servid + ")' type='button' value='Edit'/>" +
            	"&nbsp;<input onclick=\"Services.remove(" + servid + ", '" + this.data[servid].name + "')\" type='button' value='Delete'/>";
        }
    ],

    frequencies:{
        M:"Monthly",
        O:"Once Only",
        Q:"Quaterly",
        A:"Annually"
    }
});
var services;

var Sales = {};
Object.extend(Sales, Collection);
Object.extend(Sales, {
    baseName:"sales",

    fillTableFunctions:[
        function getName(id) {
            return this.data[id].name;
        },
    ]
});
Object.extendLater(Sales, "{ child:Sale, updateFunction:Actions.getAllSales, deleteFunction:Actions.deleteSale }");

var Reminders = {};
Object.extend(Reminders, Collection);
Object.extend(Reminders, {
    baseName:"reminders",

    fillTableFunctions:[
        function getDescription(action) {
            return action.description;
        },

        function getAction(action) {
            return "<input onclick=\"readAction(" + action.actionid + ")\" type='button' value='Create Reminder'/>";
        }
    ]
});
Object.extendLater(Reminders, "{ child:Reminder, updateFunction:Actions.getAllReminders }");


var Item = function() {};
Object.extend(Object.extend(Item.prototype, Base.prototype), {
    itemInitialize:function(baseName, parent, saveFunction) {
        this.baseInitialize(baseName);
        this.parent = parent;
        this.saveFunction = saveFunction;
        this.current = null;
    },

    clear:function() {
        this.current = {};
        Object.extend(this.current, this.blank);
        DWRUtil.setValues(this.current);
    },

    saved:function(errors) {
        if (errors) {
            this.show();
            Errors.report(errors);
        }
        else {
            this.clear();
            parent.update();
        }
    },

    read:function(id) {
        this.current = parent.getItem(id);
        DWRUtil.setValues(this.current);
        this.show();
    },

    save:function() {
        DWRUtil.getValues(this.current);
        this.saveImpl();
        this.hide();
    },

    saveImpl:function() {
        this.saveFunction(this.saved, this.current);
    }
});

var Customer = {};
Object.extend(Customer, Item);
Object.extend(Customer, {
    baseName:"customer",

    blank:{
    }
});
Object.extendLater(Customer, "{ parent:Customers, saveFunction:Actions.writeCustomer }");

var Service = Class.create();
Object.extend(Object.extend(Service.prototype, Item.prototype), {
    initialize:function(parent, saveFunction) {
        this.itemInitialize("service", parent, saveFunction);
    },

    blank:{
        servid:-1,
        serviceName:"",
        defaultPrice:0,
        billingFreq:"O",
        servLastUpdate:null
    }
});
var service;

var Sale = {};
Object.extend(Sale, Item);
Object.extend(Sale, {
    baseName:"sale",

    blank:{
        servid:-1,
        name:"",
        other:""
    }
});
Object.extendLater(Sale, "{ parent:Sales, saveFunction:Actions.writeSale }");

var Reminder = {};
Object.extend(Reminder, Item);
Object.extend(Reminder, {
    baseName:"reminder",

    blank: {
        id:-1,
        email:""
    }
});
Object.extendLater(Reminder, "{ parent:Reminders, saveFunction:Actions.takeAction }");

callOnLoad(init);
