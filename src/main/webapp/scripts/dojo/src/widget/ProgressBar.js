dojo.provide("dojo.widget.ProgressBar");

dojo.require("dojo.widget.*"); 
dojo.require("dojo.event.*");
dojo.require("dojo.dom");
dojo.require("dojo.html.style");
dojo.require("dojo.string.*");
dojo.require("dojo.lfx.*");


dojo.widget.defineWidget(
	"dojo.widget.ProgressBar",
	dojo.widget.HtmlWidget,
	{
		// summary:
		// a progress widget, with some calculation and server polling capabilities
		//
		// description: 
		// (implementation) four overlapped divs:
		// (1) lower z-index
		// (4) higher z-index
		// back and front percent label have the same content: when the vertical line (*)
		// partially hides the backPercentLabel, the frontPercentLabel becomes visible
		// 
		//  ________________________(1)_containerNode_________________________________
		// |__(3)_internalProgress____________                                        |
		// |                                  | <--- (*)                              |
		// |     (4) frontPercentLabel        | (2) backPercentLabel                  |
		// |__________________________________|                                       |
		// |__________________________________________________________________________| 
		//
		// usage:
		// <div dojoType="ProgressBar" frontBarClass="..." backBarClass="..."
		//   backBarClass="..." frontBarClass="..." duration="..."
		//   showOnlyIntegers="true|false" width="..." height="..." dataSource="..."
		//   pollInterval="..." 
		//   hasText="true|false" isVertical="true|false" 
		//   progressValue="..." maxProgressValue="..."></div>
	
		// progressValue: String
		// initial progress value. 
		// with "%": percentual value, 0% <= progressValue <= 100%
		// or without "%": absolute value, 0 <= progressValue <= maxProgressValue
		progressValue: 0,
		
		// maxProgressValue: Float
		// max sample number
		maxProgressValue: 100,

		// width: Integer
		// ProgressBar width (pixel)
		width: 300,

		// height: Integer
		// ProgressBar height, (pixel)
		height: 30,
		
		// frontPercentClass: String
		// css class for frontPercentLabel (4)
		frontPercentClass: "frontPercent",

		// backPercentClass: String
		// css class for backPercentLabel (2)
		backPercentClass: "backPercent",

		// frontBarClass: String
		// css class for containerNode (1)
		frontBarClass: "frontBar",

		// backBarClass: String
		// css class for internalProgress (3)
		backBarClass: "backBar",

		// hasText: Boolean
		// if true, the percent label is visible
		hasText: false,

		// isVertical: Boolean
		// if true, the widget is vertical
		isVertical: false,
		
		// showOnlyIntegers: Boolean
		// if true, the percent label shows only integer values
		showOnlyIntegers: false,
		
		// dataSource: String
		// dataSource uri for server polling
		dataSource: "",
		
		// pollInterval: Integer
		// server poll interval
		pollInterval: 3000,
		
		// duration: Integer
		// duration of the animation
		duration: 1000,

		templatePath: dojo.uri.moduleUri("dojo.widget", "templates/ProgressBar.html"),
		templateCssPath: dojo.uri.moduleUri("dojo.widget", "templates/ProgressBar.css"),
		
	
		// attach points
		containerNode: null,
		internalProgress: null,
	
		// private members
		_pixelUnitRatio: 0.0,
		_pixelPercentRatio: 0.0,
		_unitPercentRatio: 0.0,
		_unitPixelRatio: 0.0,
		_floatDimension: 0.0,
		_intDimension: 0,
		_progressPercentValue: "0%",
		_floatMaxProgressValue: 0.0,
		_dimension: "width",
		_pixelValue: 0,
		_oInterval: null,
		_animation: null,
		_animationStopped: true,
		_progressValueBak: false,
		_hasTextBak: false,

		// public functions
		fillInTemplate: function(args, frag){
			this.internalProgress.className = this.frontBarClass;
			this.containerNode.className = this.backBarClass;
			if (this.isVertical){
				this.internalProgress.style.bottom="0px";
				this.internalProgress.style.left="0px";
				this._dimension = "height";
			} else {
				this.internalProgress.style.top="0px";
				this.internalProgress.style.left="0px";
				this._dimension = "width";
			}
			this.frontPercentLabel.className = this.frontPercentClass;
			this.backPercentLabel.className = this.backPercentClass;
			this.progressValue = "" + this.progressValue; 
			this.domNode.style.height = this.height + "px"; 
			this.domNode.style.width = this.width + "px";
			this._intDimension = parseInt("0" + eval("this." + this._dimension));
			this._floatDimension = parseFloat("0" + eval("this."+this._dimension));
			this._pixelPercentRatio = this._floatDimension/100;
			this.setMaxProgressValue(this.maxProgressValue, true);
			this.setProgressValue(dojo.string.trim(this.progressValue), true);
			dojo.debug("float dimension: " + this._floatDimension);
			dojo.debug("this._unitPixelRatio: " + this._unitPixelRatio);
			this.showText(this.hasText);
		},
		showText: function(visible){
			// summary: shows or hides the labels
			if (visible){
				this.backPercentLabel.style.display="block";
				this.frontPercentLabel.style.display="block";
			} else {
				this.backPercentLabel.style.display="none";
				this.frontPercentLabel.style.display="none";
			}
			this.hasText = visible;
		},
		postCreate: function(args, frag){
			this.render();
		},
		_backupValues: function(){
			this._progressValueBak = this.progressValue;
			this._hasTextBak = this.hasText;
		},
		_restoreValues: function(){
				this.setProgressValue(this._progressValueBak);
				this.showText(this._hasTextBak);
		},
		_setupAnimation: function(){
			var _self = this;
			dojo.debug("internalProgress width: " + this.internalProgress.style.width);
			this._animation = dojo.lfx.html.slideTo(this.internalProgress, 
				{top: 0, left: parseInt(this.width)-parseInt(this.internalProgress.style.width)}, parseInt(this.duration), null, 
					function(){
						var _backAnim = dojo.lfx.html.slideTo(_self.internalProgress, 
						{ top: 0, left: 0 }, parseInt(_self.duration));
						dojo.event.connect(_backAnim, "onEnd", function(){
							if (!_self._animationStopped){
								_self._animation.play();
							}
							});
						if (!_self._animationStopped){
							_backAnim.play();
						}
						_backAnim = null; // <-- to avoid memory leaks in IE
					}
				);
		},
		getMaxProgressValue: function(){
			// summary: returns the maxProgressValue
			return this.maxProgressValue;
		},
		setMaxProgressValue: function(maxValue, noRender){
			// summary: sets the maxProgressValue
			// if noRender is true, only sets the internal max progress value
			if (!this._animationStopped){
				return;
			}
			this.maxProgressValue = maxValue;
			this._floatMaxProgressValue = parseFloat("0" + this.maxProgressValue);
			this._pixelUnitRatio = this._floatDimension/this.maxProgressValue;
			this._unitPercentRatio = this._floatMaxProgressValue/100;
			this._unitPixelRatio = this._floatMaxProgressValue/this._floatDimension;
			this.setProgressValue(this.progressValue, true);
			if (!noRender){
				this.render();
			}
		},
		setProgressValue: function(value, noRender){
			// summary: sets the progressValue
			// if value ends width "%", does a normalization
			// if noRender is true, only sets the internal value: useful if
			// there is a setMaxProgressValue call
			if (!this._animationStopped){
				return;
			}
			// transformations here
			this._progressPercentValue = "0%";
			var _value=dojo.string.trim("" + value);
			var _floatValue = parseFloat("0" + _value);
			var _intValue = parseInt("0" + _value);
			var _pixelValue = 0;
			if (dojo.string.endsWith(_value, "%", false)){
				this._progressPercentValue = Math.min(_floatValue.toFixed(1), 100) + "%";
				_value = Math.min((_floatValue)*this._unitPercentRatio, this.maxProgressValue);
				_pixelValue = Math.min((_floatValue)*this._pixelPercentRatio, eval("this."+this._dimension));
			} else {
				this.progressValue = Math.min(_floatValue, this.maxProgressValue);
				this._progressPercentValue = Math.min((_floatValue/this._unitPercentRatio).toFixed(1), 100) + "%";
				_pixelValue = Math.min(_floatValue/this._unitPixelRatio, eval("this."+this._dimension));
			}
			this.progressValue = dojo.string.trim(_value);
			this._pixelValue = _pixelValue;
			if (!noRender){
				this.render();
			}
		},
		getProgressValue: function(){
			// summary: returns the progressValue
			return this.progressValue;
		},
		getProgressPercentValue: function(){
			// summary: returns the percentual progressValue
			return this._progressPercentValue;
		},
		setDataSource: function(dataSource){
			// summary: sets the dataSource
			this.dataSource = dataSource;
		},
		setPollInterval: function(pollInterval){
			// summary: sets the pollInterval
			this.pollInterval = pollInterval;
		},
		start: function(){
			// summary: starts the server polling
			var _showFunction = dojo.lang.hitch(this, this._showRemoteProgress);
			this._oInterval = setInterval(_showFunction, this.pollInterval);
		},
		startAnimation: function(){
			// summary: starts the left-right animation, useful when
			// the user doesn't know how much time the operation will last
			if (this._animationStopped) {
				this._backupValues();
				this.setProgressValue("10%");
				this._animationStopped = false;
				this._setupAnimation();
				this.showText(false);
				this.internalProgress.style.height="105%";
				this._animation.play();
			}
		},
		stopAnimation: function(){
			// summary: stops the left-right animation
			if (this._animation) {
				this._animationStopped = true;
				this._animation.stop();
				this.internalProgress.style.height="100%";
				this.internalProgress.style.left = "0px";
				this._restoreValues();
				this._setLabelPosition();
			}
		},
		_showRemoteProgress: function(){
			var _self = this;
			if ( (this.getMaxProgressValue() == this.getProgressValue()) &&
				this._oInterval){
				clearInterval(this._oInterval);
				this._oInterval = null;
				this.setProgressValue("100%");
				return;	
			}
			var bArgs = {
				url: _self.dataSource,
				method: "POST",
				mimetype: "text/json",
				error: function(type, errorObj){
					dojo.debug("[ProgressBar] showRemoteProgress error");
				},
				load: function(type, data, evt){
					_self.setProgressValue(
						(_self._oInterval ? data["progress"] : "100%")
					);
				}
			};
			dojo.io.bind(bArgs);
		},
		render: function(){
			// summary: renders the ProgressBar, based on current values
			this._setPercentLabel(dojo.string.trim(this._progressPercentValue));
			this._setPixelValue(this._pixelValue);
			this._setLabelPosition();
		},

		_setLabelPosition: function(){
			var _widthFront = 
				dojo.html.getContentBox(this.frontPercentLabel).width;
			var _heightFront = 
				dojo.html.getContentBox(this.frontPercentLabel).height;
			var _widthBack = 
				dojo.html.getContentBox(this.backPercentLabel).width;
			var _heightBack = 
				dojo.html.getContentBox(this.backPercentLabel).height;
			var _leftFront = (parseInt(this.width) - _widthFront)/2 + "px";
			var _bottomFront = (parseInt(this.height) - parseInt(_heightFront))/2 + "px";
			var _leftBack = (parseInt(this.width) - _widthBack)/2 + "px";
			var _bottomBack = (parseInt(this.height) - parseInt(_heightBack))/2 + "px";
			this.frontPercentLabel.style.left = _leftFront;
			this.backPercentLabel.style.left = _leftBack; 
			this.frontPercentLabel.style.bottom = _bottomFront;
			this.backPercentLabel.style.bottom = _bottomBack; 
		},
		_setPercentLabel: function(percentValue){
			dojo.dom.removeChildren(this.frontPercentLabel);
			dojo.dom.removeChildren(this.backPercentLabel);
			var _percentValue = this.showOnlyIntegers == false ? 
				percentValue : parseInt(percentValue) + "%";
			this.frontPercentLabel.
				appendChild(document.createTextNode(_percentValue));
			this.backPercentLabel.
				appendChild(document.createTextNode(_percentValue));
		},
		_setPixelValue: function(value){
			eval("this.internalProgress.style." + this._dimension + " = " + value + " + 'px'");
			this.onChange();
		},
		onChange: function(){
		}
	});
	
