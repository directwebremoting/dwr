dojo.provide("dijit._Calendar");

dojo.require("dojo.cldr.supplemental");
dojo.require("dojo.date");
dojo.require("dojo.date.locale");

dojo.require("dijit._Widget");
dojo.require("dijit._Templated");

dojo.declare(
	"dijit._Calendar",
	[dijit._Widget, dijit._Templated],
	{
	//	
	//	summary:
	//		A simple GUI for choosing a date in the context of a monthly calendar.
	//
	//	description:
	//		A simple GUI for choosing a date in the context of a monthly calendar.
	//		This widget is used internally by other widgets and is not accessible
	//		as a standalone widget.
	//		This widget can't be used in a form because it doesn't serialize the date to an
	//		`<input>` field.  For a form element, use dijit.form.DateTextBox instead.
	//
	//		Note that the parser takes all dates attributes passed in the
	//		[RFC 3339 format](http://www.faqs.org/rfcs/rfc3339.html), e.g. `2005-06-30T08:05:00-07:00`
	//		so that they are serializable and locale-independent.
	//
	//	example:
	//	|	var calendar = new dijit._Calendar({}, dojo.byId("calendarNode"));
	//
	//	example:
	//	|	<div dojoType="dijit._Calendar"></div>
	//	
		templatePath: dojo.moduleUrl("dijit", "templates/Calendar.html"),

		// value: Date
		// 	the currently selected Date
		value: new Date(),

		// dayWidth: String
		// 	How to represent the days of the week in the calendar header. See dojo.date.locale
		dayWidth: "narrow",

		setValue: function(/*Date*/ value){
			// summary: set the current date and update the UI.  If the date is disabled, the selection will
			//	not change, but the display will change to the corresponding month.
			if(!this.value || dojo.date.compare(value, this.value)){
				value = new Date(value);
				this.displayMonth = new Date(value);
				if(!this.isDisabledDate(value, this.lang)){
					this.value = value;
					this.value.setHours(0,0,0,0);
					this.onChange(this.value);
				}
				this._populateGrid();
			}
		},

		_setText: function(node, text){
			while(node.firstChild){
				node.removeChild(node.firstChild);
			}
			node.appendChild(dojo.doc.createTextNode(text));
		},

		_populateGrid: function(){
			var month = this.displayMonth;
			month.setDate(1);
			var firstDay = month.getDay();
			var daysInMonth = dojo.date.getDaysInMonth(month);
			var daysInPreviousMonth = dojo.date.getDaysInMonth(dojo.date.add(month, "month", -1));
			var today = new Date();
			var selected = this.value;

			var dayOffset = dojo.cldr.supplemental.getFirstDayOfWeek(this.lang);
			if(dayOffset > firstDay){ dayOffset -= 7; }

			// Iterate through dates in the calendar and fill in date numbers and style info
			dojo.query(".dijitCalendarDateTemplate", this.domNode).forEach(function(template, i){
				i += dayOffset;
				var date = new Date(month);
				var number, clazz = "dijitCalendar", adj = 0;

				if(i < firstDay){
					number = daysInPreviousMonth - firstDay + i + 1;
					adj = -1;
					clazz += "Previous";
				}else if(i >= (firstDay + daysInMonth)){
					number = i - firstDay - daysInMonth + 1;
					adj = 1;
					clazz += "Next";
				}else{
					number = i - firstDay + 1;
					clazz += "Current";
				}

				if(adj){
					date = dojo.date.add(date, "month", adj);
				}
				date.setDate(number);

				if(!dojo.date.compare(date, today, "date")){
					clazz = "dijitCalendarCurrentDate " + clazz;
				}

				if(!dojo.date.compare(date, selected, "date")){
					clazz = "dijitCalendarSelectedDate " + clazz;
				}

				if(this.isDisabledDate(date, this.lang)){
					clazz = "dijitCalendarDisabledDate " + clazz;
				}

				var clazz2 = this.getClassForDate(date, this.lang);
				if(clazz2){
					clazz = clazz2 + " " + clazz;
				}

				template.className =  clazz + "Month dijitCalendarDateTemplate";
				template.dijitDateValue = date.valueOf();
				var label = dojo.query(".dijitCalendarDateLabel", template)[0];
				this._setText(label, date.getDate());
			}, this);

			// Fill in localized month name
			var monthNames = dojo.date.locale.getNames('months', 'wide', 'standAlone', this.lang);
			this._setText(this.monthLabelNode, monthNames[month.getMonth()]);

			// Fill in localized prev/current/next years
			var y = month.getFullYear() - 1;
			var d = new Date();
			dojo.forEach(["previous", "current", "next"], function(name){
				d.setFullYear(y++);
				this._setText(this[name+"YearLabelNode"],
					dojo.date.locale.format(d, {selector:'year', locale:this.lang}));
			}, this);

			// Set up repeating mouse behavior
			var _this = this;
			var typematic = function(nodeProp, dateProp, adj){
				_this._connects.push(
					dijit.typematic.addMouseListener(_this[nodeProp], _this, function(count){
						if(count >= 0){ _this._adjustDisplay(dateProp, adj); }
					}, 0.8, 500)
				);
			};
			typematic("incrementMonth", "month", 1);
			typematic("decrementMonth", "month", -1);
			typematic("nextYearLabelNode", "year", 1);
			typematic("previousYearLabelNode", "year", -1);
		},

		goToToday: function(){
			this.setValue(new Date());
		},

		postCreate: function(){
			this.inherited(arguments);
			
			var cloneClass = dojo.hitch(this, function(clazz, n){
				var template = dojo.query(clazz, this.domNode)[0];
	 			for(var i=0; i<n; i++){
					template.parentNode.appendChild(template.cloneNode(true));
				}
			});

			// clone the day label and calendar day templates 6 times to make 7 columns
			cloneClass(".dijitCalendarDayLabelTemplate", 6);
			cloneClass(".dijitCalendarDateTemplate", 6);

			// now make 6 week rows
			cloneClass(".dijitCalendarWeekTemplate", 5);

			// insert localized day names in the header
			var dayNames = dojo.date.locale.getNames('days', this.dayWidth, 'standAlone', this.lang);
			var dayOffset = dojo.cldr.supplemental.getFirstDayOfWeek(this.lang);
			dojo.query(".dijitCalendarDayLabel", this.domNode).forEach(function(label, i){
				this._setText(label, dayNames[(i + dayOffset) % 7]);
			}, this);

			// Fill in spacer element with all the month names (invisible) so that the maximum width will affect layout
			var monthNames = dojo.date.locale.getNames('months', 'wide', 'standAlone', this.lang);
			dojo.forEach(monthNames, function(name){
				var monthSpacer = dojo.doc.createElement("div");
				this._setText(monthSpacer, name);
				this.monthLabelSpacer.appendChild(monthSpacer);
			}, this);

			this.value = null;
			this.setValue(new Date());
		},

		_adjustDisplay: function(/*String*/part, /*int*/amount){
			this.displayMonth = dojo.date.add(this.displayMonth, part, amount);
			this._populateGrid();
		},

		_onDayClick: function(/*Event*/evt){
			var node = evt.target;
			dojo.stopEvent(evt);
			while(!node.dijitDateValue){
				node = node.parentNode;
			}
			if(!dojo.hasClass(node, "dijitCalendarDisabledDate")){
				this.setValue(node.dijitDateValue);
				this.onValueSelected(this.value);
			}
		},

		_onDayMouseOver: function(/*Event*/evt){
			var node = evt.target;
			if(node && (node.dijitDateValue || node == this.previousYearLabelNode || node == this.nextYearLabelNode) ){
				dojo.addClass(node, "dijitCalendarHoveredDate");
				this._currentNode = node;
			}
		},

		_onDayMouseOut: function(/*Event*/evt){
			if(!this._currentNode){ return; }
			for(var node = evt.relatedTarget; node;){
				if(node == this._currentNode){ return; }
				try{
					node = node.parentNode;
				}catch(x){
					node = null;
				}
			}
			dojo.removeClass(this._currentNode, "dijitCalendarHoveredDate");
			this._currentNode = null;
		},

		onValueSelected: function(/*Date*/date){
			// summary: a date cell was selected.  It may be the same as the previous value.
		},

		onChange: function(/*Date*/date){
			// summary: called only when the selected date has changed
		},

		isDisabledDate: function(/*Date*/dateObject, /*String?*/locale){
			// summary:
			//	May be overridden to disable certain dates in the calendar e.g. `isDisabledDate=dojo.date.locale.isWeekend`
/*=====
			return false; // Boolean
=====*/
		},

		getClassForDate: function(/*Date*/dateObject, /*String?*/locale){
			// summary:
			//  May be overridden to return CSS classes to associate with the date entry for the given dateObject,
			//  for example to indicate a holiday in specified locale.

/*=====
			return ""; // String
=====*/
		}
	}
);
