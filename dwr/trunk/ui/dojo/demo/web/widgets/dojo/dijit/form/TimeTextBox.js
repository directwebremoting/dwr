/*
	Copyright (c) 2004-2008, The Dojo Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
*/


if(!dojo._hasResource["dijit.form.TimeTextBox"]){dojo._hasResource["dijit.form.TimeTextBox"]=true;dojo.provide("dijit.form.TimeTextBox");dojo.require("dijit._TimePicker");dojo.require("dijit.form._DateTimeTextBox");dojo.declare("dijit.form.TimeTextBox",dijit.form._DateTimeTextBox,{popupClass:"dijit._TimePicker",_selector:"time"});}