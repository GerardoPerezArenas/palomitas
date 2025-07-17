function getUrlDigitalizacion(){
    return APP_CONTEXT_PATH + "/registro/digitalizacionDocumentosLanbide.do";
}

function pulsarDigitalizar(codUsu,origen){
    var url = getUrlDigitalizacion();
    if(codUsu!=null && codUsu!="" && codUsu>0){
        $.post(url, {'codUsu':codUsu,'opcion':'obtenerParametrosDigitalizacion'}, function(ajaxResult){
            if(ajaxResult){
                var datos = JSON.parse(ajaxResult);
                invocarDigitalizacion(datos,origen);
            } else jsp_alerta("A","Ha ocurrido un error al recuperar los datos que necesita el complemento de digitalización para ejecutarse.")
        });
    }
    
}

function invocarDigitalizacion(datos,origen){
    var numRegistro = "";
    var numAnotacion = $("input[name='numero']").val();
    var anoAnotacion = $("input[name='ano']").val();
    var horaMinAnotacion = $("input[name='horaMinAnotacion']").val();
    var fechaAnotacion = $("input[name='fechaAnotacion']").val();
    if(fechaAnotacion==null || fechaAnotacion=="")
        fechaAnotacion = $("input[name='fecPresRes']").val();
    fechaAnotacion += " " + horaMinAnotacion;
    if(numAnotacion!=null && numAnotacion!="" && anoAnotacion!=null && anoAnotacion!="")
        numRegistro = anoAnotacion + "/" + numAnotacion;
    
    if(numRegistro==null || numRegistro=="")
        jsp_alerta("A","No se pueden digitalizar documentos sin número de registro asociado");
    else if(fechaAnotacion==null || fechaAnotacion=="")
        jsp_alerta("A","La fecha del registro es incorrecta");
    else {
        // Datos necesarios para componente de digitalizacion
        //var source = APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source="+datos.urlServicio;
        var source = datos.urlServicio;
        var parametros = "?tipo="+datos.tipo+"&numRegistro="+numRegistro
                +"&fechaRegistro="+fechaAnotacion+"&aplicacion="
                +datos.aplicacion+"&auditUser="+datos.auditUser+"&idioma="+datos.idioma;
        var datosDigit = {
          'url' : source + parametros
        };
        // Popup para indicar que se ha terminado de digitalizar
        var urlPopupAceptarDigitalizacion =
                APP_CONTEXT_PATH
                + "/jsp/sge/mainVentana.jsp?source="
                + getUrlDigitalizacion()
                + "?opcion=cargarPantallaTerminarDigitalizar";
        //abrirXanelaAuxiliar(urlPopupAceptarDigitalizacion,datosDigit,'width=900,height=900,status=no',function(){
        abrirXanelaAuxiliar(urlPopupAceptarDigitalizacion,datosDigit,'width=' + screen.width + ',height=' + screen.height+',status=no',function(){
            pulsarModificar("pestana1");
        });
    }
}

function mostrarComponenteDigitalizacion(url, name, ancho, alto) {
    var left = screen.width / 2 - ancho / 2;
    var top = screen.height / 2 - alto / 2;
    var ventanaComponente = window.open(url, name,
            'menubar=no,resizable=yes,titlebar=no,toolbar=no,status=no,'
          + 'width=' + ancho + ', height=' + alto + ', top=' + top + ', left=' + left);
   
    return ventanaComponente;
}

function pulsarCatalogarDoc(){
     
        var ejercicio=document.forms[0].ano.value;
        var numero=document.forms[0].numero.value;
        var uor = document.forms[0].unidadOrgan.value; 
        var tipRegOrigen = document.forms[0].tipoRegistroOrigen.value; 
        var codDepartamento = document.forms[0].identDepart.value;
        var cod_uor = document.forms[0].cod_uor.value; 
        var procedimiento = $('input[name=cod_procedimiento]').val();
     
        var parametros  = "&numero=" + numero
                       +"&ejercicio="+ejercicio
                       +"&uorRegistro="+uor
                       +"&tipoRegOrigen="+tipRegOrigen
                       +"&codDepartamento="+codDepartamento
                       +"&cod_uor="+cod_uor
                       +"&codProcedimiento="+ procedimiento; 
     
        var source = getUrlDigitalizacion() + "?opcion=cargarPantallaCatalogar" + parametros;
        var argumentos = {"hayDatos" : 'NO'};
    
        abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + source,argumentos,    
                'width=1350,height=850',function(){
                    resfrescaTablaDocumentos();
                });
}



function resfrescaTablaDocumentos(){
     var ejercicio=document.forms[0].ano.value;
     var numero=document.forms[0].numero.value;
     var uor = document.forms[0].unidadOrgan.value;
     $.post(getUrlDigitalizacion(), {'ejercicio': ejercicio, 'numero': numero,'uor':uor, 'opcion': 'recuperarDocumentosConsulta'}, function(ajaxResult) {
        if (ajaxResult) {
            var datos = JSON.parse(ajaxResult);
            rellenaTablaDocConsulta(datos);

        } else
            mostrarErrorCatalogar('9');
    });
       
}

function rellenaTablaDocConsulta(datos){
    var docs = new Array();    
    for(i=0; i<datos.length;i++) {
        var fecha;
        if(datos[i].fecha===undefined) fecha='';
        else fecha=datos[i].fecha; 
       
        var catalogado;
        var digit;
        var tipoDocumental;
        if(datos[i].extension=='') {
            catalogado = '';
            digit = '';
            tipoDocumental = '';
        } else {
            catalogado = datos[i].catalogado;
            digit = datos[i].digitalizado;
            tipoDocumental = datos[i].tipoDocumental;
        }  
        
        docs[i]= [datos[i].nombre, datos[i].extension, fecha,tipoDocumental, catalogado, digit, datos[i].unidadOrg];        
    }              
    listaDocs = docs;
    tabDocs.lineas=listaDocs;
    refresh("tabDocs");
}

function comprobarDocCatalogados(){
    var documentos = tabDocs.lineas;
    if(documentos.length <= 0) {
        return true;
    } else {
        for(var index=0; index<documentos.length; index++) {
            var catalogado = documentos[index][4];
            var compulsado = documentos[index][5];
            if(compulsado == 'SI' || compulsado == 'NO'){
                if(catalogado == 'NO') {
                    mostrarErrorCatalogar(10);
                    return false;
                }
            }
        }
        return true;
    }
}

function comprobarFinDigitalizacion(procedimientoDigitalizacion,finDigitalizacion){
    var  digitalizacionFinalizada = true;
    
    if(procedimientoDigitalizacion && !finDigitalizacion){
        mostrarErrorCatalogar(11);
        digitalizacionFinalizada = false;
    } 
    
    return digitalizacionFinalizada;
}

function comprobarContenidoDocs(){
    var contenido = false;
    for(var i = 0; i<listaDocs.length; i++){
        if(listaDocs[i][1]!="" && listaDocs[i][1]!=undefined){
            contenido = true;
            break
        } 
    }
    return contenido;
}