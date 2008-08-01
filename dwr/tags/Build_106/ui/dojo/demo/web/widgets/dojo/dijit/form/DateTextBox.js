dojo.provide("dijit.form.DateTextBox");

dojo.require("dijit._Calendar");
dojo.require("dijit.form._DateTimeTextBox");

dojo.declare(
	"dijit.form.DateTextBox",
	dijit.form._DateTimeTextBox,
	{
		// summary:
		//		A validating, serializable, range-bound date text box with a popup calendar

		baseClass: "dijitTextBox dijitDateTextBox",
		popupClass: "dijit._Calendar",
		_selector: "date"
	}
);
