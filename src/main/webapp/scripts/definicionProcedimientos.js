function guardarDatosComposicionVistaEnSesion(url, parametros){
    var codigo = 0;

	var ajax = getXMLHttpRequest();

	if(ajax!=null){
		
		ajax.open("POST",url,false);
		ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
		ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
		ajax.send(parametros);

		try{            
			if (ajax.readyState==4 && ajax.status==200){
                            var text = null;
                            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                                // En IE el XML viene en responseText y no en la propiedad responseXML
                                text = ajax.responseText;					   
                            }else{
                                // En el resto de navegadores el XML se recupera de la propiedad responseXML
                                text = ajax.responseText;
                            }
                            
                            if(text.trim()=="0")
                                codigo = 0;
                            else
                            if(text.trim()=="1")
                                codigo = 1;
                            else
                            if(text.trim()=="2")    
                                codigo = 2;                         
                        }
				

		}catch(err){
			alert("Error en generación de vista de campos suplementarios del trámite: " + err.description);
		} 		
	}
	return codigo;
}



function cambiarSeparador(lista){
    var salida = "";
    
    if(lista!=null && lista.length>0){
        var datos = lista.split("§¥");
        for(i=0;i<datos.length;i++){
            salida = salida + datos[i];
            if(datos.length-i>1)
                salida = salida + "##";
        }// for        
    }
    return salida;    
}


function getXMLHttpRequest(){
    var aVersions = [ "MSXML2.XMLHttp.5.0",
        "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
        "MSXML2.XMLHttp","Microsoft.XMLHttp"
      ];

    if (window.XMLHttpRequest){
            // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
            return new XMLHttpRequest();
    }else if (window.ActiveXObject){
        // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
        for (var i = 0; i < aVersions.length; i++) {
                try {
                    var oXmlHttp = new ActiveXObject(aVersions[i]);
                    return oXmlHttp;
                }catch (error) {
                //no necesitamos hacer nada especial
                }
         }
    }
}

