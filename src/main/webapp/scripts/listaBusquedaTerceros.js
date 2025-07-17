var Tercero = new Array();
var datosLength;

function cargarListaB(datos, boton) {
    Tercero = datos;
    datosLength = datos.length;
    var lenCasas = 0;
    for(i=0;i<datosLength;i++){
        if (datos[i][18].length>lenCasas)
            lenCasas=datos[i][18].length;
    }
    if ((datosLength >1)||(lenCasas>1)){
        var argumentos = new Array();
        argumentos =[datos,""];
        var source = APP_CONTEXT_PATH + "/BusquedaTerceros.do?opcion=inicializarBusquedaTerc&ventana=true";
        abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/terceros/mainVentana.jsp?source="+source,argumentos,
	'width=998,height=700',function(TerSel){
                            if (TerSel==undefined) {
                                TerSel=['','','','','','','','','','','','','','','','','','','','',''];
                            }
                            vectorCampos = ['codTerc','numModifTerc','txtIdTercero','txtVersion','cbTipoDoc','txtDNI','txtInteresado',
                                'txtApell1','txtApell2','txtPart','txtPart2','txtTelefono','txtCorreo','codDomTerc',
                                'txtIdDomicilio','txtPais','txtProv','txtMuni','txtDomicilio','txtCP','txtPoblacion'];
                            insertarTerceroPorDefecto(TerSel);                      
                            rellenarTercero(TerSel,vectorCampos);
                    });
    }else if(lenCasas==1){
    var listaCasas = new Array();
    var vectorCampos = new Array();
    var vectorDatos = new Array();
    var domicilio = "";
    var nombre = formatearNombre(Terceros[0][4], Terceros[0][5], Terceros[0][6]);
    // nombre = Tercero[0][4] + " " + Tercero[0][7] + " " + Tercero[0][5] + " " + 
    //   Tercero[0][8] + " " + Tercero[0][6];
    listaCasas = Tercero[0][18];
    if (listaCasas[0][29] != "") {
        domicilio = (listaCasas[0][20]!="") ? domicilio+listaCasas[0][20]+" ":domicilio;      // DESCRIPCION DEL TIPO DE VIA
        domicilio = (listaCasas[0][29]!="") ? domicilio+listaCasas[0][29]+" ":domicilio;      // DESCRIPCION DE LA VIA
        domicilio = (listaCasas[0][9]!=0) ? domicilio+" "+listaCasas[0][9]:domicilio;         // PRIMER NUMERO DE LA VIA
        domicilio = (listaCasas[0][10]!="") ? domicilio+" "+listaCasas[0][10]+" ":domicilio;  // PRIMERA LETRA DE LA VIA
        domicilio = (listaCasas[0][11]!=0) ? domicilio+" "+listaCasas[0][11]:domicilio;       // ULTIMO NUMERO DE LA VIA
        domicilio = (listaCasas[0][12]!="") ? domicilio+" "+listaCasas[0][12]:domicilio;      // ULTIMA LETRA DE LA VIA
        domicilio = (listaCasas[0][13]!="") ? domicilio+" Bl. "+listaCasas[0][13]:domicilio;  // BLOQUE
        domicilio = (listaCasas[0][14]!="") ? domicilio+" Portal "+listaCasas[0][14]:domicilio; // PORTAL
        domicilio = (listaCasas[0][15]!="") ? domicilio+" Esc. "+listaCasas[0][15]:domicilio; // ESCALERA
        domicilio = (listaCasas[0][16]!="") ? domicilio+" "+listaCasas[0][16]+"º ":domicilio; // PLANTA
        domicilio = (listaCasas[0][17]!="") ? domicilio+listaCasas[0][17]:domicilio;          // PUERTA
    } else {
    domicilio = (listaCasas[0][3]);
}

vectorCampos = ['codTerc','numModifTerc','txtIdTercero','txtVersion','cbTipoDoc','txtDNI','txtInteresado',
    'txtApell1','txtApell2','txtPart','txtPart2','txtTelefono','txtCorreo','codDomTerc',
    'txtIdDomicilio','txtPais','txtProv','txtMuni','txtDomicilio','txtCP','txtPoblacion'];
vectorDatos = [Tercero[0][0],Tercero[0][1],Tercero[0][0],Tercero[0][1],Tercero[0][2],Tercero[0][3],nombre,
    Tercero[0][5],Tercero[0][6],Tercero[0][7],Tercero[0][8],Tercero[0][9],Tercero[0][10],
    listaCasas[0][5],listaCasas[0][5],listaCasas[0][0],listaCasas[0][1],listaCasas[0][2],domicilio,
    listaCasas[0][4],listaCasas[0][18]];
// Se refiere a la función de altaRE que inserta un tercero en la
// lista de interesados con el rol por defecto.
insertarTerceroPorDefecto(vectorDatos);
rellenarTercero(vectorDatos,vectorCampos);
capaVisible = false;
}else {
var vectorCampos = new Array();
var vectorDatos = new Array();

 var nombre = formatearNombre(Terceros[0][4], Terceros[0][5], Terceros[0][6]);

// nombre = Tercero[0][4] + " " + Tercero[0][7] + " " + Tercero[0][5] + " " + 
//   Tercero[0][8] + " " + Tercero[0][6];
vectorCampos = ['codTerc','numModifTerc','txtIdTercero','txtVersion','cbTipoDoc','txtDNI','txtInteresado',
    'txtApell1','txtApell2','txtPart','txtPart2','txtTelefono','txtCorreo'];
vectorDatos = [Tercero[0][0],Tercero[0][1],Tercero[0][0],Tercero[0][1],Tercero[0][2],Tercero[0][3],nombre,
    Tercero[0][5],Tercero[0][6],Tercero[0][7],Tercero[0][8],Tercero[0][9],Tercero[0][10]];
rellenarTercero(vectorDatos,vectorCampos);
capaVisible = false;
}	
}

function cargarTercero(datos, boton) {
    var vectorCampos = new Array();
    var vectorDatos = new Array();
    vectorCampos = ['codTerc','numModifTerc','txtIdTercero','txtVersion','codTipoDoc','txtDNI','txtInteresado',
        'txtApell1','txtApell2','txtPart','txtPart2','txtTelefono','txtCorreo'];
    Tercero = datos;                    
    if (Tercero.length >1){
        var argumentos = new Array();
        argumentos =[datos,""];
        var source = APP_CONTEXT_PATH + "/BusquedaTerceros.do?opcion=inicializarBusquedaTerc&ventana=true";
        abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/terceros/mainVentana.jsp?source="+source,argumentos,
	'width=998,height=700,status='+ '<%=statusBar%>',function(TerSel){
                        if (TerSel == undefined) {
                            TerSel=['','','','','','','','','','','','','','','','','','','','','',''];
                        }
                        vectorDatos = [TerSel[0],TerSel[1],TerSel[0],TerSel[1],TerSel[4],TerSel[5],TerSel[21],
                            TerSel[7],TerSel[8],TerSel[9],TerSel[10],TerSel[11],TerSel[12]];
                        rellenar(vectorDatos,vectorCampos);
                        mostrarDescripcionTipoDoc();		
                        capaVisible = false;
                });
    } else {
        vectorDatos = [Tercero[0][0],Tercero[0][1],Tercero[0][0],Tercero[0][1],Tercero[0][2],Tercero[0][3],Tercero[0][4],
            Tercero[0][5],Tercero[0][6],Tercero[0][7],Tercero[0][8],Tercero[0][9],Tercero[0][10]];

        rellenar(vectorDatos,vectorCampos);
        mostrarDescripcionTipoDoc();		
        capaVisible = false;
    }
}	

function rellenarTercero(vectorDatos,vectorCampos) {
    rellenar(vectorDatos,vectorCampos);
    mostrarDescripcionTipoDoc();		
}

/* Carga la ventana de mantenimiento de terceros con el tercero correspondiente al id pasado como argumento.
* Cuando datos[0][0] != terceroSinDomicilio, termina la recursion y se ejecuta el código indicado en "despois". Debe ser
* llamada inmediatamente despues de window.showModalDialog, para no interferir en el funcionamiento normal.*/
function terceroSinDomicilio(idTercero,mensaje,appContextPath,despois) {        
    jsp_alerta("A",mensaje);
    var source = appContextPath + '/BusquedaTerceros.do?opcion=cargarTercero';
    abrirXanelaAuxiliar(appContextPath + '/jsp/terceros/mainVentana.jsp?source='+source+'&codTerc=' + idTercero,'',
	'width=998,height=530,status=no',function(datos){
                        if (datos!=null && datos[0]!=null && datos[0][0] == "terceroSinDomicilio") {       
                                datos = terceroSinDomicilio(idTercero,mensaje,appContextPath);
                        } else{
                            despois(datos);
                        }
                });
}

function indiceTercero(id){
    var i=0;
    for(i=0;i<datosLength;i++){
        if (Tercero[i][0]==id) break;
    }
    return i;
}

 function formatearNombre(nombre, apellido1, apellido2) {
      var nombreFormateado = "";
      if (apellido1!="" && apellido1 != undefined) nombreFormateado += apellido1.trim();
      if (apellido2!="" && apellido2 != undefined) {
          if (nombreFormateado.length > 0) nombreFormateado += " " + apellido2.trim();
          else nombreFormateado = apellido2.trim();
      }
      if (nombre!="" && nombre != undefined) {
          if (nombreFormateado.length > 0) nombreFormateado += ", " + nombre.trim();
          else nombreFormateado = nombre.trim();
      }
      return nombreFormateado;
  }

