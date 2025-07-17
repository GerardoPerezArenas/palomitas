function chkEstado_change(theCheckbox, theForm) {
            theForm.estadoFirma.disabled = !(theCheckbox.checked);
          
}
function btnSubmit_click(theSender, theForm) { 
    theForm.estadoFirma.value = theForm.codEstadoFirma.value;    
    if ( (theSender) && (theForm) ) {        
        theForm.doSearch.value='true';
        theSender.disabled=true;
        pleaseWait1("on",this);        
        theForm.submit();
    }
}