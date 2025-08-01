/**
* @file bootstrap1.js
*
* summary: First file that is loaded that 'bootstraps' the entire dojo library suite.
* note:  Must run before hostenv_*.js file.
*
* @author  Copyright 2004 Mark D. Anderson (mda@discerning.com)
* TODOC: should the copyright be changed to Dojo Foundation?
* @license Licensed under the Academic Free License 2.1 http://www.opensource.org/licenses/afl-2.1.php
*
* $Id: bootstrap1.js,v 1.1 2007/04/04 09:55:10 susana.rodriguez Exp $
*/

// TODOC: HOW TO DOC THE BELOW?
// @global: djConfig
// summary:
//		Application code can set the global 'djConfig' prior to loading
//		the library to override certain global settings for how dojo works.
// description:  The variables that can be set are as follows:
//			- isDebug: false
//			- allowQueryConfig: false
//			- baseScriptUri: ""
//			- baseRelativePath: ""
//			- libraryScriptUri: ""
//			- iePreventClobber: false
//			- ieClobberMinimal: true
//			- locale: undefined
//			- extraLocale: undefined
//			- preventBackButtonFix: true
//			- searchIds: []
//			- parseWidgets: true
// TODOC: HOW TO DOC THESE VARIABLES?
// TODOC: IS THIS A COMPLETE LIST?
// note:
//		'djConfig' does not exist under 'dojo.*' so that it can be set before the
//		'dojo' variable exists.
// note:
//		Setting any of these variables *after* the library has loaded does
//		nothing at all.


//TODOC:  HOW TO DOC THIS?
// @global: dj_global
// summary:
//		an alias for the top-level global object in the host environment
//		(e.g., the window object in a browser).
// description:
//		Refer to 'dj_global' rather than referring to window to ensure your
//		code runs correctly in contexts other than web browsers (eg: Rhino on a server).
var dj_global = this;

//TODOC:  HOW TO DOC THIS?
// @global: dj_currentContext
// summary:
//		Private global context object. Where 'dj_global' always refers to the boot-time
//    global context, 'dj_currentContext' can be modified for temporary context shifting.
//    dojo.global() returns dj_currentContext.
// description:
//		Refer to dojo.global() rather than referring to dj_global to ensure your
//		code runs correctly in managed contexts.
var dj_currentContext = this;


// ****************************************************************
// global public utils
// TODOC: DO WE WANT TO NOTE THAT THESE ARE GLOBAL PUBLIC UTILS?
// ****************************************************************

function dj_undef(/*String*/ name, /*Object?*/ object){
	//summary: Returns true if 'name' is defined on 'object' (or globally if 'object' is null).
	//description: Note that 'defined' and 'exists' are not the same concept.
	return (typeof (object || dj_currentContext)[name] == "undefined");	// Boolean
}

// make sure djConfig is defined
if(dj_undef("djConfig", this)){
	var djConfig = {};
}

//TODOC:  HOW TO DOC THIS?
// dojo is the root variable of (almost all) our public symbols -- make sure it is defined.
if(dj_undef("dojo", this)){
	var dojo = {};
}

dojo.global = function(){
	// summary:
	//		return the current global context object
	//		(e.g., the window object in a browser).
	// description:
	//		Refer to 'dojo.global()' rather than referring to window to ensure your
	//		code runs correctly in contexts other than web browsers (eg: Rhino on a server).
	return dj_currentContext;
}

// Override locale setting, if specified
dojo.locale  = djConfig.locale;

//TODOC:  HOW TO DOC THIS?
dojo.version = {
	// summary: version number of this instance of dojo.
	major: 0, minor: 4, patch: 1, flag: "+",
	revision: Number("$Rev: 6986 $".match(/[0-9]+/)[0]),
	toString: function(){
		with(dojo.version){
			return major + "." + minor + "." + patch + flag + " (" + revision + ")";	// String
		}
	}
}

dojo.getObject = function(/*String*/name, /*Boolean*/create, /*Object*/obj, /*Boolean*/returnWrapper){
	// summary: 
	//		gets an object from a dot-separated string, such as "A.B.C"
	//	description: 
	//		useful for longer api chains where you have to test each object in
	//		the chain
	//	name: 	
	//		Path to an object, in the form "A.B.C".
	//	obj:
	//		Optional. Object to use as root of path. Defaults to
	//		'dojo.global()'. Null may be passed.
	//	create: 
	//		Optional. If true, Objects will be created at any point along the
	//		'path' that is undefined.
	//	returnWrapper:
	//		Optional. Returns an object with two properties, 'obj' and 'prop'.
	//		'obj[prop]' is the reference indicated by 'name'.
	var tobj, tprop;
	if(typeof name != "string"){
		// clearly an error
		// FIXME: why is this here?
		return undefined; // Undefined
	}
	tobj = obj;
	if(!tobj){ tobj = dojo.global(); }
	var parts=name.split("."), i=0, lobj, tmp, tname;
	do{
		lobj = tobj;
		tname = parts[i];
		tmp = tobj[parts[i]];
		if((create)&&(!tmp)){
			tmp = tobj[parts[i]] = {};
		}
		tobj = tmp;
		i++;
	}while(i<parts.length && tobj);
	tprop = tobj;
	tobj = lobj;
	return (returnWrapper) ? { obj: tobj, prop: tname } : tprop; // Object
}

dojo.exists = function(/*String*/name, /*Object*/obj){
	// summary: 
	//		determine if an object supports a given method
	// description: 
	//		useful for longer api chains where you have to test each object in
	//		the chain
	// name: 	
	//		Path to an object, in the form "A.B.C".
	// obj:
	//		Optional. Object to use as root of path. Defaults to
	//		'dojo.global()'. Null may be passed.
	if(typeof obj == "string"){
		// back-compat, should be removed at some point
		dojo.deprecated("dojo.exists(obj, name)", "use dojo.exists(name, obj, /*optional*/create)", "0.6");
		// swap them
		var tmp = name;
		name = obj;
		obj = tmp;
	}
	return (!!dojo.getObject(name, false, obj)); // Boolean
}

dojo.evalProp = function(/*String*/name, /*Object*/object, /*Boolean?*/create){
	// summary: 
	//		DEPRECATED. Returns 'object[name]'.  If not defined and 'create' is
	//		true, will return a new Object.
	// description:
	//		Returns null if 'object[name]' is not defined and 'create' is not true.
	// 		Note: 'defined' and 'exists' are not the same concept.
	dojo.deprecated("dojo.evalProp", "just use hash syntax. Sheesh.", "0.6");
	return object[name] || (create ? (object[name]={}) : undefined);	// mixed
}

dojo.parseObjPath = function(/*String*/ path, /*Object?*/ context, /*Boolean?*/ create){
	// summary: 
	//		DEPRECATED. Parse string path to an object, and return
	//		corresponding object reference and property name.
	// description:
	//		Returns an object with two properties, 'obj' and 'prop'.
	//		'obj[prop]' is the reference indicated by 'path'.
	// path: Path to an object, in the form "A.B.C".
	// context: Object to use as root of path.  Defaults to 'dojo.global()'.
	// create: 
	//		If true, Objects will be created at any point along the 'path' that
	//		is undefined.
	dojo.deprecated("dojo.parseObjPath", "use dojo.getObject(path, create, context, true)", "0.6");
	return dojo.getObject(path, create, context, true); // Object: {obj: Object, prop: String}
}

dojo.evalObjPath = function(/*String*/path, /*Boolean?*/create){
	// summary: 
	//		DEPRECATED. Return the value of object at 'path' in the global
	//		scope, without using 'eval()'.
	// path: Path to an object, in the form "A.B.C".
	// create: 
	//		If true, Objects will be created at any point along the 'path' that
	//		is undefined.
	dojo.deprecated("dojo.evalObjPath", "use dojo.getObject(path, create)", "0.6");
	return dojo.getObject(path, create); // Object
}

dojo.errorToString = function(/*Error*/ exception){
	// summary: Return an exception's 'message', 'description' or text.

	// TODO: overriding Error.prototype.toString won't accomplish this?
 	// 		... since natively generated Error objects do not always reflect such things?
	/*
	if(!dj_undef("message", exception)){
		return exception.message;		// String
	}else if(!dj_undef("description", exception)){
		return exception.description;	// String
	}else{
		return exception;				// Error
	}
	*/
	return (exception["message"]||exception["description"]||exception);
}

dojo.raise = function(/*String*/ message, /*Error?*/ exception){
	// summary:
	//		Common point for raising exceptions in Dojo to enable logging.
	//		Throws an error message with text of 'exception' if provided, or
	//		rethrows exception object.

	if(exception){
		message = message + ": "+dojo.errorToString(exception);
	}else{
		message = dojo.errorToString(message);
	}

	// print the message to the user if hostenv.println is defined
	try{ 
		if(djConfig.isDebug){
			dojo.hostenv.println("FATAL exception raised: "+message);
		}
	}catch(e){}

	throw exception || Error(message);
}

//Stub functions so things don't break.
//TODOC:  HOW TO DOC THESE?
dojo.debug = function(){};
dojo.debugShallow = function(obj){};
dojo.profile = { 
	start: function(){}, 
	end: function(){}, 
	stop: function(){}, 
	dump: function(){}
};

function dj_eval(/*String*/ scriptFragment){
	// summary: 
	//		Perform an evaluation in the global scope.  Use this rather than
	//		calling 'eval()' directly.
	// description: 
	//		Placed in a separate function to minimize size of trapped
	//		evaluation context.
	// note:
	//	 - JSC eval() takes an optional second argument which can be 'unsafe'.
	//	 - Mozilla/SpiderMonkey eval() takes an optional second argument which is the
	//  	 scope object for new symbols.
	return dj_global.eval ? dj_global.eval(scriptFragment) : eval(scriptFragment); 	// mixed
}

dojo.unimplemented = function(/*String*/ funcname, /*String?*/ extra){
	// summary: Throw an exception because some function is not implemented.
	// extra: Text to append to the exception message.
	var message = "'" + funcname + "' not implemented";
	if(extra != null){ message += " " + extra; }
	dojo.raise(message);
}

dojo.deprecated = function(/*String*/ behaviour, /*String?*/ extra, /*String?*/ removal){
	// summary: 
	//		Log a debug message to indicate that a behavior has been
	//		deprecated.
	// extra: Text to append to the message.
	// removal: 
	//		Text to indicate when in the future the behavior will be removed.
	var message = "DEPRECATED: " + behaviour;
	if(extra){ message += " " + extra; }
	if(removal){ message += " -- will be removed in version: " + removal; }
	dojo.debug(message);
}

dojo.render = (function(){
	//	summary: 
	//		Builds the dojo.render object which details rendering support, OS
	//		and browser of the current environment.
	//	description:
	//		commonly used properties of dojo.render (in HTML environments)
	//		include:
	//
	//			dojo.render.html.ie
	//			dojo.render.html.opera
	//			dojo.render.html.khtml
	//			dojo.render.html.safari
	//			dojo.render.html.moz
	//			dojo.render.html.mozilla
	//
	//		additional objects are provided to detail support for other types
	//		of media and environments. For instance, the following determines
	//		if there's native SVG support in a browser:
	//			
	//			if(dojo.render.svg.capable && dojo.render.svg.builtin){
	//				...
	//			}
	//
	//		Other properties of note:
	//
	//			dojo.render.os.win
	//			dojo.render.os.osx
	//			dojo.render.os.linux
	//			dojo.render.html.UA (navigator.userAgent)
	//			dojo.render.html.AV (navigator.appVersion)
	//			dojo.render.html.ie50 (MSIE 5.0)
	//			dojo.render.html.ie55 (MSIE 5.5)
	//			dojo.render.html.ie60 (MSIE 6.0)
	//			dojo.render.html.ie70 (MSIE 7.0)
	//
	//		All reasonable measures are taken to ensure that the values in
	//		dojo.render represent the real environment and not, say, a browser
	//		"masquerading" as a different browser version. Only supported
	//		environments and browsers will likely have entires in dojo.render.
	function vscaffold(prefs, names){
		var tmp = {
			capable: false,
			support: {
				builtin: false,
				plugin: false
			},
			prefixes: prefs
		};
		for(var i=0; i<names.length; i++){
			tmp[names[i]] = false;
		}
		return tmp;
	}

	return {
		name: "",
		ver: dojo.version,
		os: { win: false, linux: false, osx: false },
		html: vscaffold(["html"], ["ie", "opera", "khtml", "safari", "moz"]),
		svg: vscaffold(["svg"], ["corel", "adobe", "batik"]),
		vml: vscaffold(["vml"], ["ie"]),
		swf: vscaffold(["Swf", "Flash", "Mm"], ["mm"]),
		swt: vscaffold(["Swt"], ["ibm"])
	};
})();

// ****************************************************************
// dojo.hostenv methods that must be defined in hostenv_*.js
// ****************************************************************

/**
 * The interface definining the interaction with the EcmaScript host environment.
*/

/*
 * None of these methods should ever be called directly by library users.
 * Instead public methods such as loadModule should be called instead.
 */
dojo.hostenv = (function(){
	// TODOC:  HOW TO DOC THIS?
	// summary: 
	//		Provides encapsulation of behavior that changes across different
	//		'host environments' (different browsers, server via Rhino, etc).
	// description: 
	//		None of these methods should ever be called directly by library
	//		users.  Use public methods such as 'loadModule' instead.

	// default configuration options
	var config = {
		isDebug: false,
		allowQueryConfig: false,
		baseScriptUri: "",
		baseRelativePath: "",
		libraryScriptUri: "",
		iePreventClobber: false,
		ieClobberMinimal: true,
		preventBackButtonFix: true,
		delayMozLoadingFix: false,
		searchIds: [],
		parseWidgets: true
	};

	if(typeof djConfig == "undefined"){
		djConfig = config;
	}else{
		for(var option in config){
			if(typeof djConfig[option] == "undefined"){
				djConfig[option] = config[option];
			}
		}
	}

	return {
		name_: '(unset)',
		version_: '(unset)',


		getName: function(){
			// sumary: Return the name of the host environment.
			return this.name_; 	// String
		},


		getVersion: function(){
			// summary: Return the version of the hostenv.
			return this.version_; // String
		},

		getText: function(/*String*/ uri){
			//	summary:	
			//		Read the plain/text contents at the specified 'uri'.
			//	description:
			//		If 'getText()' is not implemented, then it is necessary to
			//		override 'loadUri()' with an implementation that doesn't
			//		rely on it.
			dojo.unimplemented('getText', "uri=" + uri);
		}
	};
})();


dojo.hostenv.getBaseScriptUri = function(){
	// summary: 
	//		Return the base script uri that other scripts are found relative to.
	//		MAYBE:  Return the base uri to scripts in the dojo library.	 ???
	// return: Empty string or a path ending in '/'.


	// TODOC: 
	//		HUH? This comment means nothing to me. What other scripts? Is
	//		this the path to other dojo libraries?
	if(djConfig.baseScriptUri.length){
		return djConfig.baseScriptUri;
	}

	// MOW: Why not:
	//			uri = djConfig.libraryScriptUri || djConfig.baseRelativePath
	//		??? Why 'new String(...)'
	var uri = new String(djConfig.libraryScriptUri||djConfig.baseRelativePath);
	if(!uri){ 
		dojo.raise("Nothing returned by getLibraryScriptUri(): " + uri);
	}
	// MOW: 
	//		uri seems to not be actually used.  Seems to be hard-coding to
	//		djConfig.baseRelativePath... ???

	djConfig.baseScriptUri = djConfig.baseRelativePath;
	return djConfig.baseScriptUri;	// String
}
// vim:ai:ts=4:noet:textwidth=80
