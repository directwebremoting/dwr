console.warn("dijit-all may include much more code than your application actually requires. We strongly recommend that you investigate a custom build or the web build tool");
dojo.provide("dijit.dijit-all");

/*=====
dijit["dijit-all"] = { 
	// summary: A rollup that includes every dijit. You probably don't need this.
};
=====*/

dojo.require("dijit.dijit");

dojo.require("dijit.ColorPalette");
dojo.require("dijit.Declaration");
dojo.require("dijit.Dialog");
dojo.require("dijit.Editor");
dojo.require("dijit.Menu");
dojo.require("dijit.ProgressBar");
dojo.require("dijit.TitlePane");
dojo.require("dijit.Toolbar");
dojo.require("dijit.Tooltip");
dojo.require("dijit.Tree");
dojo.require("dijit.InlineEditBox");

dojo.require("dijit.form.Button");
dojo.require("dijit.form.CheckBox");
dojo.require("dijit.form.ComboBox");
dojo.require("dijit.form.CurrencyTextBox");
dojo.require("dijit.form.DateTextBox");
dojo.require("dijit.form.FilteringSelect");
dojo.require("dijit.form.NumberSpinner");
dojo.require("dijit.form.NumberTextBox");
dojo.require("dijit.form.Slider");
dojo.require("dijit.form.Textarea");
dojo.require("dijit.form.TextBox");
dojo.require("dijit.form.ValidationTextBox");
dojo.require("dijit.form._FormWidget");
dojo.require("dijit.form._Spinner");

dojo.require("dijit.layout.AccordionContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.LayoutContainer"); //deprecated
dojo.require("dijit.layout.LinkPane");
dojo.require("dijit.layout.SplitContainer"); //deprecated
dojo.require("dijit.layout.StackContainer");
dojo.require("dijit.layout.TabContainer");
dojo.require("dijit.layout._LayoutWidget");
