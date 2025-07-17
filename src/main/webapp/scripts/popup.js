function lanzarPopUpModal( url, theHeight, theWidth, scrollable, resizable,despois) {
    if (url) {
        var h=640;
        var w=480;
        var s="no";
        var r="no";
        if (theHeight) h = theHeight;
        if (theWidth) w = theWidth;
        var t=( (screen.height)?((screen.height-h)/2):(0) );
        var l=( (screen.width)?((screen.width-w)/2):(0) );
        if (scrollable!=null) s = scrollable;
        if (resizable!=null) r = resizable;
        abrirXanelaAuxiliar(url, null,
	'top='+t+',left='+l+',width='+w+',height='+h+',status=no,resizable='+r+'scrollbars='+s,despois);
    }
}

function saveAndReturn( value, label ) {
    if (opener!=null)
	    opener.setPopUpResult(value, label);
}
function returnWithoutSave() {
    if (opener!=null)
	    opener.closePopUp();
}
function closePopUp() {
    if (popup!=null)
	    popup.close();
}
