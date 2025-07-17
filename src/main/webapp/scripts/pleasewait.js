function isLayerVisible( objName ) {
	var theProp = "style.display";
	var theValue = "block";
	var obj = MM_findObj(objName);	
	var result = false;
	if (obj && (theProp.indexOf("style.")==-1 || obj.style)) result=eval("obj."+theProp+"=='"+theValue+"'");
	return result;
}

function showHideLayer(layerName, whattodo) {
  var obj=MM_findObj(layerName);
  var newState=( whattodo?whattodo:(isLayerVisible(layerName)?'hide':'show') );
  if ( (obj!=null) && (obj.style) ) {
  	obj.style.display= ( (newState=='show')? 'block' : ( (newState=='hide')? 'none' : newState ) ) ;
  }
}

function hideLoading() {
        showHideLayer("hidepage", "hide");
        showHideLayer("iFrameHidePage", "hide");
        showHideLayer("divframe", "hide");
}

function showSending() {

     if (navigator.appName.indexOf("Explorer") == -1){
          document.getElementById("hidepageSending").style.left="450px";
          document.getElementById("divframeSending").style.left="450px";
     }
     
     showHideLayer("hidepageSending", "show");
     showHideLayer("iFrameHidePageSending", "show");
     showHideLayer("divframeSending", "show");
}

function sendAndJump(url) {
    if (url) {
        showSending();
        document.location.href=url;
    }
}