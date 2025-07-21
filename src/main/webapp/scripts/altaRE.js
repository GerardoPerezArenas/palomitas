// Funciones generales de altaRE.jsp
var primera_carga = true;
var finDigitalizar = null;

// Calendario
function mostrarCalDesde(event) {
    if(window.event) event = window.event;
    if (document.getElementById("calDesde").className.indexOf("fa-calendar") != -1 ) {
      showCalendar('forms[0]','fechaDocumento',null,null,null,'','calDesde','',null,null,null,null,null,null,null,'',event);
      }
}
function mostrarCalDocumento(event) {
    if(window.event) event = window.event;
    if (document.getElementById("calDocumento").className.indexOf("fa-calendar") != -1 ) {
      showCalendar('forms[0]','fechaDoc',null,null,null,'','calDocumento','',null,null,null,null,null,null,null,'',event);
      }
}

function mostrarCalFechaAnotacion(event) {
    if(window.event) event = window.event;
    if (document.getElementById("calFechaAnotacion").className.indexOf("fa-calendar") != -1 )
      showCalendar('forms[0]','fechaAnotacion',null,null,null,'','calFechaAnotacion','',null,null,null,null,null,null,null, null,event);
}

// Diligencia diarias y de anulacion
// Precond.: div's diligencia, capaDiligencia, capaEstado, capaDesAnular, capaDiligenciasAnulacion
function ocultarCapaDiligencia() {
  domlay('diligencia',0,0,0,null);
  domlay('capaDiligencia',0,0,0,null);
}

function ocultarCapaAnulacion() {
  domlay('capaEstado',0,0,0,null);
  domlay('capaDesAnular',0,0,0,null);
  domlay('capaDiligenciasAnulacion',0,0,0,null);
}

function ocultarCapaDiligenciasAnulacion() {
  domlay('capaDiligenciasAnulacion',0,0,0,null);
}

// Precond.: vars. diligenciaAnulacion
function pulsarDiligenciasAnulacion() {
	if (document.getElementById('capaDiligenciasAnulacion').style.visibility == 'visible'){
		domlay('capaDiligenciasAnulacion',0,0,0,null);
	}
	else {
		botonActual = 'botonDiligenciasAnulacion';
		posicionaCapa(50,25);
		document.getElementById('capaDiligenciasAnulacion').style.position = "absolute";
		document.getElementById('capaDiligenciasAnulacion').style.width    = 120;
		document.getElementById('capaDiligenciasAnulacion').style.height   = 40;
		var tabla = '<table cellpadding=0 cellspacing=0 border=0 width="100%"><tr><td align="right" >'
		+'<span class="fa fa-window-close-o" name="botonCerrar" onClick="ocultarCapaDiligenciasAnulacion();"></span>'
		+'</td></tr><tr><td align="left" bgcolor="#FFFFFF">'
		+'<textarea class="textareaTexto" style="width:300px;height:150px;border:0px;" name="areaDil">'+diligenciaAnulacion+'</textarea>'+'</td></tr></table>';
		domlay('capaDiligenciasAnulacion',1,ppcX,ppcY,tabla);
  }
}

function mostrarCapaAnotacionContestada(mostrar, ejercicio, numero, etiqueta){
  var texto='';
  if (mostrar) {
    texto = '<span class="textoSuelto">'+ etiqueta+ ':<br/> '+ejercicio+'/'+numero+'</span>';
    domlay('capaAnotacionContestada',1,0,0,texto);
  } else {
     domlay('capaAnotacionContestada',0,0,0,texto);
  }
}

function pulsarDiligencia() {
	if (document.getElementById('diligencia').style.visibility == 'visible'){
		domlay('diligencia',0,0,0,null);
	} else {
		botonActual = 'botonDiligencias';
		
		document.getElementById('diligencia').style.position = "absolute";
		document.getElementById('diligencia').style.width  = 120;
		document.getElementById('diligencia').style.height = 40;
		var tabla = '<table cellpadding=0 cellspacing=0 border=0 width="100%"><tr><td align="right" >'+
                                       '<span title="Cerrar" class="fa fa-times-circle" style="cursor:hand;" onclick="javascript:pulsarCerrarDiligencia();" name="botonCerrar" alt="Cerrar"></span></td></tr>' +
                                        '<tr><td bgcolor="#FFFFFF">'+
                                        '<textarea class="textareaTexto" style="width:300px;height:150px;border:0px;" name="areaDil">'+textoDil+'</textarea>'+ '</td></tr></table>';
		domlay('diligencia',1,ppcX,ppcY,tabla);
	}
}

function pulsarCerrarDiligencia() {
 domlay('diligencia',0,0,0,textoDil);
}

// Capas y botones
function mostrarCapasBotones(nombreCapa) {
  if (document.getElementById('capaBotones2')) document.getElementById('capaBotones2').style.display='none';
  if (document.getElementById('capaBotones5')) document.getElementById('capaBotones5').style.display='none';
  if (document.getElementById('capaBotones3'))document.getElementById('capaBotones3').style.display='none';
  if (document.getElementById('capaCunho')) document.getElementById('capaCunho').style.visibility='hidden';
  if (document.getElementById('capaBotones4')) document.getElementById('capaBotones4').style.display='none';
  if (document.getElementById('capaBotonesConsulta')) document.getElementById('capaBotonesConsulta').style.display='none';
  
  if (document.getElementById(nombreCapa)) 
    if (nombreCapa=="capaCunho")  
        document.getElementById(nombreCapa).style.visibility='visible';
    else
        document.getElementById(nombreCapa).style.display='';
  
//En el antiguo diseño los radio buttons estaban en la misma capa que los botones de consulta, pero
  //con el nuevo diseño hay que separarlos; por este motivo, mostramos la capa de los radio buttons siempre
  //que se muestra la capa de botones de consulta.
  if (nombreCapa == 'capaBotones3') document.getElementById('capaCunho').style.visibility='visible';

}

function activarFormulario() {        
	habilitarDatos(document.forms[0]);
	habilitarImagenCal("calDocumento",true);
	habilitarImagenCal("calFechaAnotacion",true);
	deshabilitarIconos(document.all["desp"], false);
	activado = true;
	if(typeof integracionSIRLanbide != 'undefined' && integracionSIRLanbide == "1"){
	    // Para desactivar las opciones de Procedncias de otros registros en caso de Opcion 1 - Tipo Combo Entrada - Se envia a SIR
	    if($('[name="cbTipoEntrada"]').val()==1  && $("#TEOtroReg #prueba2") != undefined && $("#TEOtroReg #prueba2") != null){
	        deshabilitarGeneral($("#TEOtroReg #prueba2").find('input,span,a'));
	        deshabilitarIconos($("#TEOtroReg #prueba2").find('span'), true);
	    }
	}
}

function desactivarFormulario() {
	deshabilitarDatos(document.forms[0]);
        deshabilitarDatos(document.forms[0].descRolTercero);
	var vector = crearVectorBotones();
	habilitarGeneral(vector);
	vector = new Array(document.forms[0].ano, document.forms[0].numero);
	habilitarGeneral(vector);
	deshabilitarImagenCal("calDocumento",true);
	deshabilitarImagenCal("calFechaAnotacion",true);	
	deshabilitarIconos(document.all["desp"], true);
	activado = false;
}

function crearVectorBotones(){
    var vector = new Array();
    if(document.forms[0].cmdAltaDesdeConsulta){
        vector.push(document.forms[0].cmdAltaDesdeConsulta);
    }
    if(document.forms[0].cmdModificar){
        vector.push(document.forms[0].cmdModificar);
    }
    if(document.forms[0].cmdAnular){
        vector.push(document.forms[0].cmdAnular);
    }
    if(document.forms[0].cmdDuplicar){
        vector.push(document.forms[0].cmdDuplicar);
    }
    if(document.forms[0].cmdRelacionar){
        vector.push(document.forms[0].cmdRelacionar);
    }
    if(document.forms[0].cmdCancelarBuscada){
        vector.push(document.forms[0].cmdCancelarBuscada);
    }
    if(document.forms[0].cmdListado){
        vector.push(document.forms[0].cmdListado);
    }
    if(document.forms[0].cmdVerDoc){
        vector.push(document.forms[0].cmdVerDoc);
    }
    if(document.forms[0].cmdRelaciones){
        vector.push(document.forms[0].cmdRelaciones);
    }
    if(document.forms[0].cmdActuaciones){
        vector.push(document.forms[0].cmdActuaciones);
    }
    if(document.forms[0].cmdDigitalizarDesdeConsulta){
        vector.push(document.forms[0].cmdDigitalizarDesdeConsulta);
    }
    if(document.forms[0].cmdCotejarDoc){
        vector.push(document.forms[0].cmdCotejarDoc);
    }
    if(document.forms[0].cmdRegRetramitarDoc){
        vector.push(document.forms[0].cmdRegRetramitarDoc);
    }
    if(document.forms[0].cmdRegRetramitarDoc){
        vector.push(document.forms[0].cmdRegRetramitarDoc);
    }
    if(document.forms[0].registrarDokusiTodosDocs){
        vector.push(document.forms[0].registrarDokusiTodosDocs);
    }
    if(document.forms[0].registrarDokusiUnDocsEspecifico){
        vector.push(document.forms[0].registrarDokusiUnDocsEspecifico);
    }
    
    return vector;
}

function deshabilitarBuscar() {
	var vector = new Array(document.forms[0].ano, document.forms[0].numero);
	deshabilitarGeneral(vector);
}

function habilitarBuscar() {
	var vector = new Array(document.forms[0].ano, document.forms[0].numero);
	habilitarGeneral(vector);
}

function desactivarOrigenYExpediente() {
}

function desactivarInteresado() {
	var vector = new Array(document.forms[0].txtInteresado, document.forms[0].txtTelefono,
	document.forms[0].txtCorreo, document.forms[0].txtPart,
	document.forms[0].txtApell1, document.forms[0].txtPart2,
	document.forms[0].txtApell2, document.forms[0].txtPais,
	document.forms[0].txtProv, document.forms[0].txtMuni,
	document.forms[0].txtDomicilio, document.forms[0].txtPoblacion,
	document.forms[0].txtCP, document.forms[0].txtDNI);
	deshabilitarGeneral(vector);
}

function activarInteresado() {
	var vector = new Array(document.forms[0].txtInteresado, document.forms[0].txtTelefono,
	document.forms[0].txtCorreo, document.forms[0].txtPart,
	document.forms[0].txtApell1, document.forms[0].txtPart2,
	document.forms[0].txtApell2, document.forms[0].txtPais,
	document.forms[0].txtProv, document.forms[0].txtMuni,
	document.forms[0].txtDomicilio, document.forms[0].txtPoblacion,
	document.forms[0].txtCP, document.forms[0].txtDNI);
	habilitarGeneral(vector);
}

function ocultarInfSoloConsulta() {
    if (document.getElementById("capaTiposEstadoAnotacion"))
        document.getElementById("capaTiposEstadoAnotacion").style.visibility = 'hidden';

    if (document.getElementById("divRegistroTelematico")) {

        document.getElementById("divRegistroTelematico").style.visibility = 'hidden';

    }
}
function activaInfSoloConsulta() {
    document.getElementById("capaTiposEstadoAnotacion").style.visibility = 'visible';
    document.getElementById("divRegistroTelematico").style.visibility = 'visible';
}


function deshabilitarDatosSGA(){	
    var vectorBloqueSga=new Array(document.forms[0].codigoSga,document.forms[0].expedienteSga);	
        deshabilitarGeneral(vectorBloqueSga);	
}
function noObligatorioToObligatorioConsulta() {
	noObligatorioToObligatorio(document.forms[0]);
	var otros = new Array(document.forms[0].cbTipoEntrada,
	document.forms[0].cod_orgDestino,
	document.forms[0].desc_orgDestino,
	document.forms[0].descTipoDoc);
	noObligatorioToObligatorioGeneral(otros);
}


function borrarDestinoOrdinaria() {
    document.forms[0].cod_procedimiento.value = '';
    document.forms[0].desc_procedimiento.value = '';
}

function borrarDestinoOtroReg() {
    document.forms[0].cod_orgDestino.value='';
    document.forms[0].desc_orgDestino.value='';
    document.forms[0].cod_uniRegDestino.value='';
    document.forms[0].desc_uniRegDestino.value='';
}

function borrarProcedenteOtroReg() {
    document.forms[0].cod_orgOrigen.value='';
    document.forms[0].desc_orgOrigen.value='';
    document.forms[0].cod_unidadeRexistroOrixe.value='';
    document.forms[0].desc_unidadeRexistroOrixe.value='';
    document.forms[0].txtExp1Orixe.value='';
    document.forms[0].txtExp2Orixe.value='';
    
    var anoValue = document.forms[0].ano.value;
    var numValue = document.forms[0].numero.value;
    // Diferenciamos cuando debemos mostrar el anyo actual por defecto y cuando no
    // No: cuando se carga la pantalla de busqueda, el anyo de entrada relacionada debe cargarse sin valor
    if(anoValue!=undefined && anoValue!='' && (numValue==undefined || numValue=='') && (responderASalida=="null"))
        document.forms[0].txtExp1Orixe.value='';
    else {
        var fecha = new Date();
        var anoActual = fecha.getFullYear();
        document.forms[0].txtExp1Orixe.value=anoActual;
    }
}

function borrarInteresado(){
	var inicio;
	if(document.forms[0].modificar.value == 1) {
		inicio=document.forms[0].descTipoDoc.value;
		document.forms[0].modificar.value = 0;
	}
	if(inicio != document.forms[0].descTipoDoc.value) {
		document.forms[0].codTerc.value='0';
		document.forms[0].txtIdTercero.value='0';
		document.forms[0].numModifTerc.value='0';
		document.forms[0].txtVersion.value='0';
		document.forms[0].codDomTerc.value='0';
		document.forms[0].txtDNI.value=''; // Tipo documento.
		document.forms[0].txtInteresado.value=''; // Razon Social.
		document.forms[0].txtApell1.value=''; // Apellidos.
		document.forms[0].txtApell2.value='';
		document.forms[0].txtPart.value=''; // Partículas.
		document.forms[0].txtPart2.value='';
		document.forms[0].txtTelefono.value=''; // Telefono.
		document.forms[0].txtCorreo.value=''; // Email.
		document.forms[0].txtPais.value=''; // Pais.
		document.forms[0].txtProv.value=''; // Provincia.
		document.forms[0].txtMuni.value=''; // Municipio.
		document.forms[0].txtDomicilio.value=''; // Nombre vía.
		document.forms[0].txtPoblacion.value='';
		document.forms[0].txtCP.value=''; // Codigo.
	}
}

function borrarInteresado1(){
	if(dTipoDoc != document.forms[0].descTipoDoc.value || tipoDoc != document.forms[0].cbTipoDoc.value) {
		document.forms[0].codTerc.value='0';
		document.forms[0].txtIdTercero.value='0';
		document.forms[0].numModifTerc.value='0';
		document.forms[0].txtVersion.value='0';
		document.forms[0].codDomTerc.value='0';
		document.forms[0].txtDNI.value=''; // Tipo documento.
		document.forms[0].txtInteresado.value=''; // Razon Social.
		document.forms[0].txtApell1.value=''; // Apellidos.
		document.forms[0].txtApell2.value='';
		document.forms[0].txtPart.value=''; // Partículas.
		document.forms[0].txtPart2.value='';
		document.forms[0].txtTelefono.value=''; // Telefono.
		document.forms[0].txtCorreo.value=''; // Email.
		document.forms[0].txtPais.value=''; // Pais.
		document.forms[0].txtProv.value=''; // Provincia.
		document.forms[0].txtMuni.value=''; // Municipio.
		document.forms[0].txtDomicilio.value=''; // Nombre vía.
		document.forms[0].txtPoblacion.value='';
		document.forms[0].txtCP.value=''; // Codigo.
		if (consultando) {
			var v = new Array(document.forms[0].txtInteresado);
			habilitarGeneral(v);
		}
	}
}


// Campos q. permiten consulta
var longMaxInputCodigoConsulta=4000;
var longMaxInputCodigo=3;
var longMaxInputFecha=10;
var operadorConsulta = '|&:<>!=';
var operadorConsultaNulo=operadorConsulta+'#';

function cambiarLongMaxInput(vector, valor){
	for (i=0;i<vector.length;i++){
		var campo = vector[i];
		campo.maxlength = valor;
	}
}

function camposFecha(){
	var camposFecha = new Array(document.forms[0].fechaAnotacion,document.forms[0].fechaDocumento);
	return camposFecha;
}
function camposCodigo() {
    // TODO document.forms[0].cod_dptoDestinoORD rmved
    var camposCodigo = new Array(document.forms[0].txtCodigoDocumento, document.forms[0].cod_tipoRemitente,
            document.forms[0].cod_tipoTransporte, document.forms[0].cod_uor,
            document.forms[0].cod_orgDestino, document.forms[0].cod_uniRegDestino,
            document.forms[0].cod_actuacion, document.forms[0].cbTipoDoc);
	return camposCodigo;
}

function contieneOperadoresConsulta(campo,cjtoOp){
    var contiene=false;
    if(campo != null) {
        var v = campo.value;
        for (i = 0; i < v.length; i++){
            var c = v.charAt(i);
            if (cjtoOp.indexOf(c) != -1)  contiene=true;
        }
    }
    return contiene;
}

// Listas
function actualizarDescripcion(campoCodigo, campoDescripcion, listaCodigos, listaDescripciones) {
	cargarDesc(campoCodigo,campoDescripcion, listaCodigos, listaDescripciones);
}

function borrarActuacion(){
	if (!(Trim(document.forms[0].fechaAnotacion.value) ==""))
	{
		document.forms[0].cod_actuacion.value='';
		document.forms[0].txtNomeActuacion.value='';
	} // else // No hay fecha --> vale cq actuacion
}

function mostrarListaProcedimientos(){
    if (primera_carga){
        primera_carga = false;
    } else {
        document.forms[0].opcion.value = "listaProcedimientos";
        document.forms[0].target="oculto";
        document.forms[0].action= APP_CONTEXT_PATH + "/MantAnotacionRegistro.do";

        document.forms[0].submit();
    }
}

function mostrarListaProcedimientosExpRel(){	
    if (primera_carga)
        primera_carga = false;
    else {
        document.forms[0].opcion.value = "listaProcedimientosExpRel";
        document.forms[0].target="oculto";
        document.forms[0].action= APP_CONTEXT_PATH + "/MantAnotacionRegistro.do";

        document.forms[0].submit();
    }
}

function mostrarListaDepartamentoDestinoORD(){
}

function mostrarListaEntidadDestino(){
}

function mostrarListaOrganizacionDestino(){
	var condiciones = new Array();
	muestraListaTabla('ORGEX_COD','ORGEX_DES',EsquemaGenerico+'A_ORGEX A_ORGEX',condiciones,'cod_orgDestino','desc_orgDestino','botonOrganizacionDestino','100');
}

function mostrarListaOrganizacionOrigen(){
	var condiciones = new Array();
	muestraListaTabla('ORG_COD','ORG_DES','A_ORG',condiciones,'cod_orgOrigen','desc_orgOrigen','botonOrganizacionOrigen','100');
}


function mostrarListaEntidadOrigen(){
	if ( Trim(document.forms[0].cod_orgOrigen.value) != ''){
		  var condiciones = new Array();
		  condiciones[0]='ENT_ORG'+'§¥';
		  condiciones[1]= document.forms[0].cod_orgOrigen.value+'§¥';
		  muestraListaTabla('ENT_COD','ENT_NOM','A_ENT',condiciones,'cod_entidadOrigen','desc_entidadOrigen','botonEntidadOrigen','100');
	}
}


function mostrarListaDepartamentoOrigen(){
}

function mostrarListaUnidRegOrigen(){
	if ( (Trim(document.forms[0].cod_orgOrigen.value) != '') ) {
		var condiciones = new Array();
		condiciones[0]='UOR_TIP'+'§¥';
		condiciones[1]='1'+'§¥';
                                    condiciones[2]='UOR_OCULTA'+'§¥';
                                    condiciones[3]='N';
		muestraListaTabla('UOR_COD','UOR_NOM','A_UOR',condiciones,'cod_unidadeRexistroOrixe','desc_uniRegOrigen', 'botonUnidadeRexistroOrigen','100');
		}
}

function mostrarListaUnidRegDestinoORD(){
	var condiciones = new Array();
    if (!consultando){
    condiciones[0]='UOR_NO_VIS'+'§¥';
    condiciones[1]='0';
        condiciones[2]='UOR_ESTADO'+'§¥';
        condiciones[3]='A';
    } else {
        condiciones[0]='UOR_OCULTA'+'§¥';
        condiciones[1]='N';
    }
    muestraListaTabla('UOR_COD_VIS','UOR_NOM','A_UOR',condiciones,'cod_uor','desc_uniRegDestinoORD', 'botonUnidadeRexistroORD','100');
   }
   
   
   function mostrarListaUnidRegDestinoORDFiltroUsu(organizacion, usuario){
	var condiciones = new Array();
        var condicionCompleja="";
    if (!consultando){
        condiciones[0]='UOR_NO_VIS'+'§¥';
        condiciones[1]='0';
        condiciones[2]='UOR_ESTADO'+'§¥';
        condiciones[3]='A';
    } else {
        condiciones[0]='UOR_OCULTA'+'§¥';
        condiciones[1]='N';
    }
    condicionCompleja= "uor_cod in (select uou_uor from "+EsquemaGenerico+"A_UOU a_uou where uou_usu="+usuario+" and uou_org="+organizacion+")"    
    muestraListaTabla('UOR_COD_VIS','UOR_NOM','A_UOR',condiciones,'cod_uor','desc_uniRegDestinoORD', 'botonUnidadeRexistroORD','100',null,null,condicionCompleja);
   }

function onchangeCampoCodigo(nmbCampoCod, nmbCampoDesc, vCod, vDesc,cjtoOp){
	if (consultando) {
		var campoCod = eval('document.forms[0].'+nmbCampoCod);
		var campoDesc = eval('document.forms[0].'+nmbCampoDesc);
		if (!contieneOperadoresConsulta(campoCod,cjtoOp)){
			divSegundoPlano=true;
			inicializarValores(nmbCampoCod,nmbCampoDesc,vCod, vDesc);
		} else 	campoDesc.value='';
	} else {
			divSegundoPlano=true;
			inicializarValores(nmbCampoCod,nmbCampoDesc,vCod, vDesc);
	}
}

function onFocusCampoDesc(nmbCampoCod, nmbCampoDesc, vCod, vDesc,cjtoOp){
	if (consultando) {
		var campoCod = eval('document.forms[0].'+nmbCampoCod);
		if (!contieneOperadoresConsulta(campoCod,cjtoOp)){
			divSegundoPlano=true;
			inicializarValores(nmbCampoCod,nmbCampoDesc,vCod, vDesc);
		}
	} else {
			divSegundoPlano=true;
			inicializarValores(nmbCampoCod,nmbCampoDesc,vCod, vDesc);
	}
}

function onClickCampoDesc(nmbCampoCod, nmbCampoDesc, vCod, vDesc){
	 if (consultando) {
		 var campoCod = eval('document.forms[0].'+nmbCampoCod);
		 campoCod.value ='';
	 }
	 divSegundoPlano=false;
	 inicializarValores(nmbCampoCod,nmbCampoDesc,vCod, vDesc);
}

function onchangeCampoCodigoBD(nmbCampoCod, nmbCampoDesc, nmbFuncionMostrarLista,cjtoOp){

	var campoCod = eval('document.forms[0].'+nmbCampoCod);
	var campoDesc = eval('document.forms[0].'+nmbCampoDesc);
	if (consultando) {
		if (!contieneOperadoresConsulta(campoCod,cjtoOp)){
			actualizarValDiv(nmbCampoCod,nmbCampoDesc);
			divSegundoPlano=true;
			eval(nmbFuncionMostrarLista+'()');
		} else 	campoDesc.value='';
	} else {
			actualizarValDiv(nmbCampoCod,nmbCampoDesc);
			divSegundoPlano=true;
			eval(nmbFuncionMostrarLista+'()');
	}
}

function onFocusCampoDescBD(nmbCampoCod, nmbFuncionMostrarLista,cjtoOp){
	if (consultando) {
		var campoCod = eval('document.forms[0].'+nmbCampoCod);
		if (!contieneOperadoresConsulta(campoCod,cjtoOp)){
			divSegundoPlano=true;
			eval(nmbFuncionMostrarLista+'()');
		}
	} else {
			divSegundoPlano=true;
			eval(nmbFuncionMostrarLista+'()');
	}
}

function onClickCampoDescBD(nmbCampoCod, nmbFuncionMostrarLista){
	 if (consultando) {
		 var campoCod = eval('document.forms[0].'+nmbCampoCod);
		 campoCod.value ='';
	 }
	divSegundoPlano=false;
	eval(nmbFuncionMostrarLista+'()');
}

function onchangeCod_uniRegDestinoORD() {    
    if ((!consultando) ||
        (consultando && !contieneOperadoresConsulta(document.forms[0].cod_dptoDestinoORD,operadorConsulta) &&
          !contieneOperadoresConsulta(document.forms[0].cod_uniRegDestinoORD,operadorConsulta))) {
        var uor = buscarUorPorCodVisibleEstado(uors, document.forms[0].cod_uor.value, 'A');
        if(uor != null) {
            document.forms[0].cod_uniRegDestinoORD.value = uor.uor_cod;
            document.forms[0].desc_uniRegDestinoORD.value = uor.uor_nom;            
            
        }
        else { // ha dado null para alta, buscamos de baja solo en el caso de estar consultando
            if (consultando){
            uor = buscarUorPorCodVisibleEstado(uors, document.forms[0].cod_uor.value, 'B');
            if(uor != null) {
                document.forms[0].cod_uniRegDestinoORD.value = uor.uor_cod;
                document.forms[0].desc_uniRegDestinoORD.value = uor.uor_nom;
                
            }
        }
        }
        
        // uor cod visible no existe: resetear campos
        if(uor == null || (uor.uor_no_reg=='1' && !consultando)) {            
            document.forms[0].cod_uniRegDestinoORD.value = '';
            document.forms[0].desc_uniRegDestinoORD.value = '';
            document.forms[0].cod_uor.value = '';
        }
    }
    
}

function onchangeCod_procedimiento(){
    primera_carga = false;
    if (!consultando) {
		actualizarValDiv('cod_procedimiento','desc_procedimiento');
		divSegundoPlano=true;
		mostrarListaProcedimientos();
	} else {
         if (!contieneOperadoresConsulta(document.forms[0].cod_uor,operadorConsulta) && !contieneOperadoresConsulta(document.forms[0].cod_procedimiento,operadorConsulta) ) {
            actualizarValDiv('cod_procedimiento','desc_procedimiento');
			divSegundoPlano=true;
			mostrarListaProcedimientos();
		} else {
			document.forms[0].desc_procedimiento.value='';
			if (Trim(document.forms[0].cod_uniRegDestinoORD.value)=='')
				document.forms[0].cod_procedimiento.value='';
		}
	}
}

function onchangeCod_procedimientoExpRel(){
    primera_carga = false;
    if (!consultando) {
		actualizarValDiv('cod_procedimiento','desc_procedimiento');
		divSegundoPlano=true;
		mostrarListaProcedimientosExpRel();
	} else {
         if (!contieneOperadoresConsulta(document.forms[0].cod_uor,operadorConsulta) && !contieneOperadoresConsulta(document.forms[0].cod_procedimiento,operadorConsulta) ) {
            actualizarValDiv('cod_procedimiento','desc_procedimiento');
			divSegundoPlano=true;
			mostrarListaProcedimientosExpRel();
		} else {
			document.forms[0].desc_procedimiento.value='';
			if (Trim(document.forms[0].cod_uniRegDestinoORD.value)=='')
				document.forms[0].cod_procedimiento.value='';
		}
	}
}

function onFocusDesc_procedimiento(){    
    if (!consultando) {
		divSegundoPlano=true;
		mostrarListaProcedimientos();
	} else {
		if (!contieneOperadoresConsulta(document.forms[0].cod_uniRegDestinoORD,operadorConsulta) && !contieneOperadoresConsulta(document.forms[0].cod_procedimiento,operadorConsulta) ) {
			divSegundoPlano=true;
			mostrarListaProcedimientos();
		} else {
			document.forms[0].desc_procedimiento.value = '';
		}
	}
}


function onClickDesc_uniRegDestinoORD(){  
    if(!document.forms[0].cod_uor.readOnly){
	if (consultando )  {
            if (contieneOperadoresConsulta(document.forms[0].cod_uniRegDestinoORD,operadorConsulta) )
			document.forms[0].cod_uniRegDestinoORD.value='';
		divSegundoPlano=false;
		mostrarListaUnidRegDestinoORD();
	} else if (!consultando) {
             	divSegundoPlano=false;
		mostrarListaUnidRegDestinoORD();
	}
    }
}

function onClickDesc_uniRegDestinoORDFiltroUsu(organizacion, usuario){ 
  
    if(!document.forms[0].cod_uor.readOnly){
	if (consultando )  {
            if (contieneOperadoresConsulta(document.forms[0].cod_uniRegDestinoORD,operadorConsulta) )
			document.forms[0].cod_uniRegDestinoORD.value='';
		divSegundoPlano=false;
		mostrarListaUnidRegDestinoORDFiltroUsu(organizacion, usuario);
	} else if (!consultando) {
             	divSegundoPlano=false;
		mostrarListaUnidRegDestinoORDFiltroUsu(organizacion, usuario);
	}
    }
}


function onClickHref_uniRegDestinoORD() {
    
    var arbolConsulta = "no";
    if (consultando) arbolConsulta = "si";
    

    var argumentos = new Array();
    argumentos[0] = document.forms[0].cod_uor.value;
    abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + APP_CONTEXT_PATH + 
            "/MantAnotacionRegistro.do?opcion=seleccionUOR&consultando="+arbolConsulta,argumentos,    
            'width=670,height=650',function(datos){
                    if(datos != null) {
                        document.forms[0].cod_uniRegDestinoORD.value = datos[2];
                        document.forms[0].desc_uniRegDestinoORD.value = datos[1];
                        document.forms[0].cod_uor.value = datos[0];
                    }    

                    // si se pulsa xa ver el arbol con algo en el campo codigo, y damos a cancelar en la ventana modal
                    // puede q tengamos algo en código y nada en la descripción
                    if((document.forms[0].cod_uor.value != '') && (document.forms[0].desc_uniRegDestinoORD.value == '')) {        
                        document.forms[0].cod_uniRegDestinoORD.value = '';
                        document.forms[0].cod_uor.value = '';
                    }

                    // Actualizar notificaciones por correo
                    cambiaUnidadOrganica();
            });
}


function onClickHref_uniRegDestinoORDFiltroUsu() {
    
    var arbolConsulta = "no";
    if (consultando) 
        arbolConsulta = "si";

    var argumentos = new Array();
    argumentos[0] = document.forms[0].cod_uor.value;
    
    abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + APP_CONTEXT_PATH +
        "/MantAnotacionRegistro.do?opcion=seleccionUOR&consultando="+arbolConsulta,argumentos,
        'width=670,height=650',function(datos){
                if(datos != null) {
                    document.forms[0].cod_uniRegDestinoORD.value = datos[2];
                    document.forms[0].desc_uniRegDestinoORD.value = datos[1];
                    document.forms[0].cod_uor.value = datos[0];
                }    
                // si se pulsa xa ver el arbol con algo en el campo codigo, y damos a cancelar en la ventana modal
                // puede q tengamos algo en código y nada en la descripción
                if((document.forms[0].cod_uor.value != '') && (document.forms[0].desc_uniRegDestinoORD.value == '')) {        
                    document.forms[0].cod_uniRegDestinoORD.value = '';
                    document.forms[0].cod_uor.value = '';
                }
                // Actualizar notificaciones por correo
                cambiaUnidadOrganica();
            });
}

function onClickDesc_procedimiento(){
    primera_carga = false;
    if(!document.forms[0].cod_procedimiento.readOnly){
        if(estaPluginExpRelacionadosFlexiaCargado=="SI"){
            if (consultando && !contieneOperadoresConsulta(document.forms[0].cod_uniRegDestinoORD,operadorConsulta))  {
                if (contieneOperadoresConsulta(document.forms[0].cod_procedimiento,operadorConsulta) )
                    document.forms[0].cod_procedimiento.value='';
                divSegundoPlano=false;
                mostrarListaProcedimientos();
            } else if (!consultando) {
                divSegundoPlano=false;
                mostrarListaProcedimientos();
            }
        }
    }
}

function onchangeCod_uniRegDestino(){
	if (!consultando) {
		actualizarValDiv('cod_uniRegDestino','desc_uniRegDestino');
		divSegundoPlano=true;
		mostrarListaUnidRegDestino();
	} else {
		if (!contieneOperadoresConsulta(document.forms[0].cod_uniRegDestino,operadorConsulta) ) {
			actualizarValDiv('cod_uniRegDestino','desc_uniRegDestino');
			divSegundoPlano=true;
			mostrarListaUnidRegDestino();
		} else {
			document.forms[0].desc_uniRegDestino.value='';
		}
	}
}

function onchangeCod_uniRegOrigen(){
	if (!consultando) {
		actualizarValDiv('cod_unidadeRexistroOrixe','desc_unidadeRexistroOrixe');
		divSegundoPlano=true;
		mostrarListaUnidRegOrigen();
	} else {
		if (!contieneOperadoresConsulta(document.forms[0].cod_unidadeRexistroOrixe,operadorConsulta) ) {
			actualizarValDiv('cod_unidadeRexistroOrixe','desc_unidadeRexistroOrixe');
			divSegundoPlano=true;
			mostrarListaUnidRegOrigen();
		} else {
			document.forms[0].cod_unidadeRexistroOrixe.value='';
		}
	}
}

function onFocusDesc_uniRegDestino(){
	if (!consultando) {
		divSegundoPlano=true;
		mostrarListaUnidRegDestino();
	} else {
		//if (!contieneOperadoresConsulta(document.forms[0].cod_dptoDestino,operadorConsulta) && !contieneOperadoresConsulta(document.forms[0].cod_uniRegDestino,operadorConsulta) ) {
		if (!contieneOperadoresConsulta(document.forms[0].cod_uniRegDestino,operadorConsulta) ) {
			divSegundoPlano=true;
			mostrarListaUnidRegDestino();
		} else {
			document.forms[0].desc_uniRegDestino.value='';
		}
	}
}

function onClickDesc_uniRegDestino(){
        if(!document.forms[0].cod_uor.readOnly){
            if (consultando )  {
                    if (contieneOperadoresConsulta(document.forms[0].cod_uniRegDestino,operadorConsulta) )
                            document.forms[0].cod_uniRegDestino.value='';
                    divSegundoPlano=false;
                    mostrarListaUnidRegDestino();
            } else if (!consultando) {
                    divSegundoPlano=false;
                    mostrarListaUnidRegDestino();
            }
        }
}

function onClickDesc_uniRegOrigen(){
     if(!document.forms[0].cod_unidadeRexistroOrixe.readOnly){
	if (consultando )  {
		if (contieneOperadoresConsulta(document.forms[0].cod_unidadeRexistroOrixe,operadorConsulta) )
			document.forms[0].cod_unidadeRexistroOrixe.value='';
		divSegundoPlano=false;
		mostrarListaUnidRegOrigen();
	} else if (!consultando) {
		divSegundoPlano=false;
		mostrarListaUnidRegOrigen();
	}
     }
}

function onchangeCod_dptoDestino(){
}

function onFocusDesc_dptoDestino(){
}

function onClickDesc_dptoDestino(){
}

function onchangeCod_entDestino(){
}

function onFocusDesc_entDestino(){
}

function onClickDesc_entDestino(){
}

function onchangeCod_orgDestino(){
	if (!consultando) {
		actualizarValDiv('cod_orgDestino','desc_orgDestino');
		divSegundoPlano=true;
		mostrarListaOrganizacionDestino();
	} else {
		if (!contieneOperadoresConsulta(document.forms[0].cod_orgDestino,operadorConsulta)){
			actualizarValDiv('cod_orgDestino','desc_orgDestino');
			divSegundoPlano=true;
			mostrarListaOrganizacionDestino();
		} else { // borramos los dependientes.
			document.forms[0].desc_orgDestino.value='';
			document.forms[0].cod_uniRegDestino.value='';
			document.forms[0].desc_uniRegDestino.value='';
		}
	}
}

function onFocusDesc_orgDestino(){
	if (!consultando) {
		divSegundoPlano=true;
		mostrarListaOrganizacionDestino();
	} else {
		if (!contieneOperadoresConsulta(document.forms[0].cod_orgDestino,operadorConsulta)){
			divSegundoPlano=true;
			mostrarListaOrganizacionDestino();
		} else { // borramos los dependientes.
			document.forms[0].desc_orgDestino.value='';
			document.forms[0].cod_uniRegDestino.value='';
			document.forms[0].desc_uniRegDestino.value='';
		}
	}
}

function onClickDesc_orgDestino(){
      if(!document.forms[0].cod_orgDestino.readOnly){
	if (consultando && contieneOperadoresConsulta(document.forms[0].cod_orgDestino,operadorConsulta))
		document.forms[0].cod_orgDestino.value='';
	divSegundoPlano=false;
	mostrarListaOrganizacionDestino();
      }
}


function onchangeCod_orgOrigen(){
	if (!consultando) {
		actualizarValDiv('cod_orgOrigen','desc_orgOrigen');
		divSegundoPlano=true;
		mostrarListaOrganizacionOrigen();
	} else {
		if (!contieneOperadoresConsulta(document.forms[0].cod_orgOrigen,operadorConsulta)){
			actualizarValDiv('cod_orgOrigen','desc_orgOrigen');
			divSegundoPlano=true;
			mostrarListaOrganizacionOrigen();
		} else { // borramos los dependientes.
			document.forms[0].desc_orgOrigen.value='';
			document.forms[0].cod_unidadeRexistroOrixe.value='';
			document.forms[0].desc_unidadeRexistroOrixe.value='';
		}
	}
}

function mostrarListaOrganizacionOrigen(){
	var condiciones = new Array();
	muestraListaTabla('ORGEX_COD','ORGEX_DES',EsquemaGenerico+'A_ORGEX A_ORGEX',condiciones,'cod_orgOrigen','desc_orgOrigen','botonOrganizacionOrigen','100');
}

function onClickDesc_orgOrigen(){
     if(!document.forms[0].cod_orgOrigen.readOnly){
	if (consultando && contieneOperadoresConsulta(document.forms[0].cod_orgOrigen,operadorConsulta))
		document.forms[0].cod_orgOrigen.value='';
	divSegundoPlano=false;
	mostrarListaOrganizacionOrigen();
     }
}

function onchangeCbTipoDoc(){
	if (!consultando) {
		actualizarValDiv('cbTipoDoc','descTipoDoc');divSegundoPlano=true;	inicializarValores('cbTipoDoc','descTipoDoc',cod_tiposIdInteresado,desc_tiposIdInteresado);
		documentoNoValido("cbTipoDoc","txtDNI",0);
	} else {
		if (!contieneOperadoresConsulta(document.forms[0].cbTipoDoc,operadorConsulta)){
			actualizarValDiv('cbTipoDoc','descTipoDoc');divSegundoPlano=true;	inicializarValores('cbTipoDoc','descTipoDoc',cod_tiposIdInteresado,desc_tiposIdInteresado);
		} else { // borramos los dependientes.
			document.forms[0].descTipoDoc.value='';
		}
	}
}

function onFocusDescTipoDoc(){
	if (!consultando) {
		divSegundoPlano=true;
		inicializarValores('cbTipoDoc','descTipoDoc',cod_tiposIdInteresado,desc_tiposIdInteresado);
	} else {
		if (!contieneOperadoresConsulta(document.forms[0].cbTipoDoc,operadorConsulta)){
			divSegundoPlano=true;
			inicializarValores('cbTipoDoc','descTipoDoc',cod_tiposIdInteresado,desc_tiposIdInteresado);
		} else { // borramos los dependientes.
			document.forms[0].descTipoDoc.value='';
		}
	}
}

function onClickDescTipoDoc(){
    if(!document.forms[0].cbTipoDoc.readOnly){
	if (consultando && contieneOperadoresConsulta(document.forms[0].cbTipoDoc,operadorConsulta))
		document.forms[0].descTipoDoc.value='';
		divSegundoPlano=false;
		inicializarValores('cbTipoDoc','descTipoDoc',cod_tiposIdInteresado,desc_tiposIdInteresado);
    }
}

function onblurTipoDocumento(cod, des){
  if(!consultando)
			documentoNoValido("cbTipoDoc","txtDNI",0);
	if(camposDistintosDiv(cod, des)){
		borrarInteresado1();
	}
}

function onblurDepartamentoOtroReg(cod, des){	
	if(camposDistintosDiv(cod, des)){				
		document.forms[0].cod_uniRegDestino.value='';
		document.forms[0].desc_uniRegDestino.value='';		
	}
}
// Comprobaciones
function comparaFecha() {
// Precondicion: la fecha actual local es correcta.
   var fecha = new Date(document.forms[0].fechaDocumento.value.substring(6,10), 
                        eval(document.forms[0].fechaDocumento.value.substring(3,5)-1), 
                        document.forms[0].fechaDocumento.value.substring(0,2)
                         );
                         
      
   // Tomamos la fecha actual
   var dFechaHoy = new Date();
         
   if(comparaFechas(fecha,dFechaHoy) == 1) {    
    return false;
   } else return true;
}

function soloCaracteresHora(e) {                      
    PasaAMayusculas(e);    
    var tecla, caracter;
    var alfanumericos = '0123456789:';
    tecla = e.keyCode;   	
    if (tecla == null) return true;
    caracter = String.fromCharCode(tecla);        
    if ((alfanumericos.indexOf(caracter) != -1) || tecla==32)
		return true;
    return false;   
}

function soloCaracteresFechaConsultando(e) {                      
    PasaAMayusculas(e);    
    var tecla, caracter;
    var alfanumericos = '0123456789/_-.:|&><!=';
    tecla = e.keyCode;
   	if (tecla == null) return true;
    caracter = String.fromCharCode(tecla);        
    if ((alfanumericos.indexOf(caracter) != -1) || tecla==32)
		return true;
    return false;   
}

function ValidarFechaConFormato(fecha, formato) { 
    if (formato==null) formato ="dd/mm/yyyy";
	if (formato=="mm/yyyy") 
			fecha = "01/"+fecha;
	else if (formato=="yyyy") 
			fecha ="01/01/"+fecha;
	else if (formato =="mmyyyy")
			fecha = "01"+fecha;
	
	var D = DataValida(fecha);
	if (formato == "dd/mm/yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr() : fecha; 
	else if (formato == "mm/yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(3) : fecha;
	else if (formato == "yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(6) : fecha;
	else if (formato == "mmyyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(3) : fecha;
	return D;  
 }

function formatoFecha(f){

	var parts = Trim(f).split(/[_\-\.\/]/);
    if (parts.length == 3) return 'dd/mm/yyyy';
	else if (parts.length == 2) return 'mm/yyyy';
	else if (parts.length == 1) {
		if (f.length == 4) return 'yyyy'
//		else if (f.length == 6) return 'mmyyyy';
		else return 'dd/mm/yyyy';
	}
}

function comprobarHora(elemento, mensaje){
	if(elemento.value != "") {
		var hora= elemento.value;
		var hm=hora.split(':');
		if (hm.length != 2) {
			jsp_alerta("A",mensaje);
			tp1.setSelectedIndex(0);
			elemento.focus();
			return false;
		}
		if ( (hm[0]<0) || (hm[0]>23)|| (hm[1]<0) || (hm[1]>60) ) {
			jsp_alerta("A",mensaje);
			tp1.setSelectedIndex(0);
			elemento.focus();
			return false;
		}
		if ((hm[0]<10) && (hm[0].length<2)) hora = '0'+hm[0]+':';
		else hora = hm[0]+":";
		if ((hm[1]<10) && (hm[1].length<2)) hora = hora + '0'+hm[1];
		else hora = hora + hm[1];
		elemento.value = hora;
		return true;
	} else return true;
}

// Funcionalidad
function pulsarLimpiar() {
     borrarPaginaBuscada();
     clean();
     document.forms[0].codTerc.value="0";
     document.forms[0].codDomTerc.value="0";
     document.forms[0].numModifTerc.value="0";
     document.getElementById("capaTipoDocNoexiste").style.visibility = 'hidden';     
     inicializar();
}

// Navegación de anotaciones
var anotacionActual = 1;

function calcularLimites(anotacionSelec) {
    anotacionActual = anotacionSelec + 1;
}

function desactivarNavegacion(){
    domlay('capaNavegacionConsulta',0,0,0,' ');
    domlay('capaNavegacionConsulta',1,0,0,navegacionConsultaDesactivada());
    document.forms[0].cmdListado.disabled=true;
}

function navegacionConsultaDesactivada() {   // Repintamos capa sin href's
    return enlacesPaginacion(litMosPagDePags,litAnterior,litSiguiente,anotacionActual,numAnotaciones,null);    
}

function navegacionConsulta() {
    return enlacesPaginacion(litMosPagDePags,litAnterior,litSiguiente,anotacionActual,numAnotaciones,'determinarAnotacion');    
}

function onblurOrganizacionOtroReg(cod, des){
    if(camposDistintosDiv(cod, des))
        {
            with (document.forms[0])
            {
                cod_uniRegDestino.value='';
                desc_uniRegDestino.value='';
            }
        }
    }
    
    function onblurOrganizacionOrigen(cod, des){
        if(camposDistintosDiv(cod, des)){
            with (document.forms[0]) {
                cod_unidadeRexistroOrixe.value='';
                desc_unidadeRexistroOrixe.value='';
            }
        }
    }
    

// Pestañas
function antesDeCambiarPestana() {
    ocultarCalendario();
    ocultarLista();
        
    if (layerVisible||divSegundoPlano) ocultarDivNoFocus();
    
    if (mod != undefined) {
        // Comprobamos si las pestanhas deben remarcarse
        compruebaModificadoRegistro();
    }
    
}

function compruebaModificadoRegistro(){
         
    //tabPage1 datos generales
    var selected1 = document.getElementById("pestana1").className;
    var changeSelected1 = false;
    if ((selected1 == "tab selected") || (selected1 == "tab-remark selected")) {
        changeSelected1 = true;
    }
    if ((document.forms[0].observaciones.value!="") || (document.forms[0].txtNomeTipoRemitente.value!="") 
        || (document.forms[0].desc_procedimiento.value!="") || (document.forms[0].txtExp1.value!="") 
        || (document.forms[0].desc_tipoTransporte.value!="") || (document.forms[0].txtNumTransp.value!="")) {
        if (changeSelected1) document.getElementById("pestana1").className="tab-remark selected";
        else document.getElementById("pestana1").className="tab-remark";
    } else{
        if (changeSelected1) document.getElementById("pestana1").className="tab selected";
        else document.getElementById("pestana1").className="tab";
    }

    //tabPage2 otros datos
    var selected2 = document.getElementById("pestana2").className;
    var changeSelected2 = false;
    if ((selected2 == "tab selected") || (selected2 == "tab-remark selected")) {
        changeSelected2 = true;
    }    
    compruebaTemas();
    compruebaRelaciones();
    if ((document.forms[0].autoridad.value!="") || (document.forms[0].txtNomeActuacion.value!="") || (listaDoc.length > 0) 
        || (lista.length > 0) || (relaciones.length > 0)) {
        
        if (changeSelected2) document.getElementById("pestana2").className="tab-remark selected"; 
        else document.getElementById("pestana2").className="tab-remark";
        
    } else {
        if (changeSelected2) document.getElementById("pestana2").className="tab selected"; 
        else document.getElementById("pestana2").className="tab";
    }
    
}

function compruebaRelaciones() {    
    // Estilo del boton
    if(document.all){
        // Internet Explorer
    if (relaciones.length > 0)       
        document.all.cmdRelaciones.className = 'botonGeneralResaltado';
    else
        document.all.cmdRelaciones.className = 'botonGeneral';
    }else{
        // Firefox u otro navegador
        if (relaciones.length > 0)
            document.getElementById('cmdRelaciones').className = 'botonGeneralResaltado';
        else
            document.getElementById('cmdRelaciones').className = 'botonGeneral';
    }
}

function compruebaTemas(){
    
    if(document.all){
        // Internet Explorer
    if (lista.length > 0)
        document.all.cmdActuaciones.className = 'botonGeneralResaltado';
    else
        document.all.cmdActuaciones.className = 'botonGeneral';
    }else{
        // Firefox u otro navegador        
        if (lista.length > 0)
            document.getElementById('cmdActuaciones').className = 'botonGeneralResaltado';
        else
            document.getElementById('cmdActuaciones').className = 'botonGeneral';
    }    
}

/** 
    Devuelve un objeto XMLHttpRequest
   */
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
    
/* Deshabilita el combo de tipos de asunto. */
function desactivarComboAsuntos() {
    indice_asunto_anterior = 0;
    comboAsuntos.selectItem(0);
    comboAsuntos.deactivate();
} 

function desactivarCampoDocumento() {
    if(document.forms[0].cbTipoDoc.value == "0") obligatorioAnormalDoc(document.forms[0].txtDNI);
}

function registroAregistro(totalAnotaciones,pos){
    consultando=false;
    ocultarInfSoloConsulta();
    mostrarCapasBotones('capaBotones3');
    noObligatorioToObligatorioConsulta();
    
    numAnotaciones = totalAnotaciones;
    determinarAnotacion(pos);
}

function recargaConsulta2(){
    desactivarFormulario();
    deshabilitarBuscar();
    ocultarInfSoloConsulta();
    actualizaAnotacionNavegacion(0);
}

/********************************************************************/
/* FUNCION RECUPERA BUSQUEDA TERCEROS								*/
/********************************************************************/
function recuperaBusquedaTercerosSeleccionar(datos, posTerc, posDom) {
    recuperaBusquedaTerceros(datos);
}

function buildRequestInsercionDirectaTercero(datos) { 
    var tercAInsertar = new Array();
    var domAInsertar = new Array();
    tercAInsertar = datos;
    domAInsertar = datos[18][0];
    
    var valoresCamposSuplementarios = "";
    for(i=0;i<datos[22].length;i++){
        var elemento = datos[22][i];

        var codigoCampo = elemento[1];
        var valorCampo = elemento[4]; 
        valoresCamposSuplementarios = valoresCamposSuplementarios + codigoCampo + ";" + valorCampo + '§¥';																				
    }    

    var request = 'txtIdTercero=' + tercAInsertar[0] + '&codTipoDoc=' + tercAInsertar[2] + '&txtDNI=' + tercAInsertar[3] +
    '&txtInteresado=' + tercAInsertar[4] + '&txtApell1=' + tercAInsertar[5] + '&txtApell2=' + tercAInsertar[6] + 
    '&txtPart=' + tercAInsertar[7] + '&txtPart2=' + tercAInsertar[8] + '&txtTelefono=' + tercAInsertar[9] +
    '&txtCorreo=' + tercAInsertar[10] + '&txtCorreo=' + tercAInsertar[10] +  '&codTerceroOrigen=' + tercAInsertar[21] +  '&codPais=' + domAInsertar[6] + '&codProvincia=' + domAInsertar[7] +
    '&codMunicipio=' + domAInsertar[8] + '&descVia=' + domAInsertar[29] + '&codTVia=' + domAInsertar[19] + 
    '&txtNormalizado=' + domAInsertar[24] + '&codUso=' + domAInsertar[21] + '&txtCodVia=' + domAInsertar[23] + 
    '&txtNumDesde=' + domAInsertar[9] + '&txtNumHasta=' + domAInsertar[11] + '&domActual=0&descUso=' + domAInsertar[22] +
    '&descTVia=' + domAInsertar[20] + '&descProvincia=' + domAInsertar[1] + '&descMunicipio=' + domAInsertar[2] + 
    '&descPostal=' + domAInsertar[4] + '&txtBarriada=' + domAInsertar[18] + '&txtDomicilio=' + domAInsertar[3] + 
    '&txtLetraDesde=' + domAInsertar[10] + '&txtLetraHasta=' + domAInsertar[12] + '&txtBloque=' + domAInsertar[13] + 
    '&txtPortal=' + domAInsertar[14] + '&txtEsc=' + domAInsertar[15] + '&txtPlta=' + domAInsertar[16] + 
    '&txtPta=' + domAInsertar[17] + '&codESI=' + domAInsertar[27] + "&CAMPOS_SUPLEMENTARIOS=" + valoresCamposSuplementarios;
    return request;    
}


/********************************************************************/
/* FUNCION TIPO DE REMITENTE POR DEFECTO                            */
/********************************************************************/
function tipoDeRemitentePorDefecto() {
    for(var j=0;j<desc_tiposRemitentes.length;j++) {
        if(desc_tiposRemitentes[j] == "ADMINISTRACION LOCAL") {
            document.forms[0].cod_tipoRemitente.value = cod_tiposRemitentes[j];
            document.forms[0].txtNomeTipoRemitente.value = desc_tiposRemitentes[j];
        }
    }
}     

/********************************************************************/
/* FUNCION TIPO DE DOCUMENTO POR DEFECTO                            */
/********************************************************************/
// Situa el NIF como Tipo de Identificador del Interesado por defecto
function tipoDeDocumentoPorDefecto() {
    for(var j=0;j<cod_tiposIdInteresado.length;j++) {
        if(cod_tiposIdInteresado[j] == "1") {
            document.forms[0].cbTipoDoc.value = cod_tiposIdInteresado[j];
            document.forms[0].descTipoDoc.value = desc_tiposIdInteresado[j];
        }
    }
}

function tipoDeDocumentoAnotacionPorDefecto() {
      valor=0;
      for(i=0;i<=cod_tiposDocumentos.length;i++){
          if(act_tiposDocumentos[i]=='SI'){
              document.forms[0].txtCodigoDocumento.value=cod_tiposDocumentos[i];
              valor=1;
          }
      }
      //si no esta ninguno activo dejo por defecto el que tenia o en blanco
      if(valor==0){
          document.forms[0].txtCodigoDocumento.value='';
      }

      actualizarDescripcion('txtCodigoDocumento','txtNomeDocumento',cod_tiposDocumentos,desc_tiposDocumentos);
}

/* Devuelve la hora y minuto actuales en un string de formato hh:mm */
function getHoraMin() {
    var today = new Date();
    var hoy = today.toString();;
    var hora;
    var min;
    
    if (hoy.substring(9,10)==" ") {
        hora = hoy.substring(10,12);
        min = hoy.substring(13,15);
    } else {
        hora = hoy.substring(11,13);
        min = hoy.substring(14,16);
    }
    return hora+":"+min;
 }
 
function actualizaEliminarDoc() {
    var list = new Array();
    var listEnt = new Array();
    tamIndex=tabDoc.selectedIndex;
    tamLength=tabDoc.lineas.length;
    for (i=tamIndex - 1; i < listaDoc.length - 1; i++){
        if (i + 1 <= listaDoc.length - 2){
            listaDoc[i + 1]=listaDoc[i + 2];
            listaDocEntregados[i+1]=listaDocEntregados[i+2];
        }
    }
    for(j=0; j < listaDoc.length-1 ; j++){
        list[j] = listaDoc[j];
        listEnt[j] = listaDocEntregados[j];
    }
    tabDoc.lineas=list;
    listaDocEntregados=listEnt;
    refrescaDoc();
    listaDoc=list;
}

function actualizaEliminarAnt(lista){
    var list=new Array();
    var listEnt=new Array();
    tamIndex=tabAnt.selectedIndex;
    tamLenght=tabAnt.lineas.length;
    for(i=tamIndex-1; i<listaAnt.length-1; i++){
        if(i+1<=listaAnt.length-2){
            listaAnt[i+1]=listaAnt[i+2];
            listaAntEntregados[i+1]=listaAntEntregados[i+2]
        }
    }
    for(j=0; j<listaAnt.length-1; j++){
        list[j]=listaAnt[j];
        listEnt[j]=listaAntEntregados[j];
    }
    tabAnt.lineas=list;
    listaAntEntregados=listEnt;
    tabAnt.displayTabla();
	agregarLinkDocumentosAportados();
    listaAnt=list;
    
}

function cambiaUnidadOrganica() {
    
    onchangeCod_uniRegDestinoORD();
    // Comprobamos si la unidad tiene email, si no tiene no se activa notificar.
    var cod_vis = document.forms[0].cod_uor.value;
    
    var tiene_email = false;
    for (i=0; i<uors.length; i++) {
        if (uors[i].uor_cod_vis == cod_vis) {
            
            cod_uor = uorcodsinternos[i];
            if (uors[i].uor_email != null && uors[i].uor_email != '')
              
                tiene_email = true;
        }
    }
    
    if (document.forms[0].cod_uor.value != '' && changeNotifs && tiene_email) {
          
        document.getElementById('enviarCorreo').disabled = false;
        document.getElementById('checkBoxNotificar').className = "etiqueta";
        
        // Sacar la unidad de la lista de uors a notificar (si estaba)
        var nuevaListaCorreos = new Array();
        var j=0;
        for(i=0; i<listaUorsCorreo.length; i++){
            if(listaUorsCorreo[i] != cod_uor){
                nuevaListaCorreos[j] = listaUorsCorreo[i];                
                j++;
            }
        }
        listaUorsCorreo = nuevaListaCorreos;
        
    } else {
        listaUorsCorreo = new Array();
        desactivaNotificaciones();
    }
    //mostrarListaProcedimientos();
    ocultarDivNoFocus();
}

function actualizaDocs(listaDocumentos) {  
    // Se copia el array elemento a elemento pq si no se pasa por referencia y se pierde.              
    listaDoc = new Array();
    for (i=0; i<listaDocumentos.length; i++) {
        if(mostrarDigitalizar){
            listaDoc[i] = [listaDocumentos[i][0],listaDocumentos[i][1],listaDocumentos[i][2],listaDocumentos[i][3],listaDocumentos[i][4],listaDocumentos[i][5],listaDocumentos[i][6],listaDocumentos[i][7]];
        }else{
            listaDoc[i] = [listaDocumentos[i][0],listaDocumentos[i][1],listaDocumentos[i][2],listaDocumentos[i][3],listaDocumentos[i][4],listaDocumentos[i][5],listaDocumentos[i][6]];
        }
    }      
    tabDoc.lineas=listaDoc;
    refrescaDoc();
}

function actualizaAnt(listaDocumentos) {  
    // Se copia el array elemento a elemento pq si no se pasa por referencia y se pierde. 
    listaAnt = new Array();
    for (i=0; i<listaDocumentos.length; i++) {
        
        listaAnt[i] = [listaDocumentos[i][0],listaDocumentos[i][1],listaDocumentos[i][2],listaDocumentos[i][3],listaDocumentos[i][4]];
    }      
    tabAnt.lineas=listaAnt;
    tabAnt.displayTabla();
	agregarLinkDocumentosAportados();
}
                
/* Desactiva el boton de relaciones */
function desactivarRelaciones() {
    //document.all.cmdRelaciones.disabled = true;
    document.getElementById('cmdRelaciones').disabled = true;  document.getElementById('cmdRelaciones').style.color = '#CCCCCC';
}

/* Desactiva el boton de actuaciones (temas) */
function desactivarActuaciones() {
    document.getElementById('cmdActuaciones').disabled = true;
    document.getElementById('cmdActuaciones').style.color = '#CCCCCC';
}

/* Carga el asiento que origina la contestacion/respuesta/relacion */
function pulsarCancelarContestar() {
    verAnotacion(tipoBuscada, ejercicioBuscada, numeroBuscada);
}


/* Crea las listas en forma de string para pasar al form las relaciones.
   Tambien cambia el estilo del boton segun haya relaciones o no. */
function crearListasRelacionesTxt() {
  
    if(!Array.isArray(relaciones)){	
      relaciones=getRelaciones(relaciones);	
  }
   var listaTipos = '';
   var listaEjercicios = '';
   var listaNumeros = '';
   
   for(i=0; i<relaciones.length; i++) {
       listaTipos += relaciones[i][0] + '§¥';
       listaEjercicios += relaciones[i][1] + '§¥';
       listaNumeros += relaciones[i][2] + '§¥';
   }
   
   document.forms[0].txtTiposRelaciones.value = listaTipos;
   document.forms[0].txtEjerciciosRelaciones.value = listaEjercicios;
   document.forms[0].txtNumerosRelaciones.value = listaNumeros;
   // Remarcar pestaña
   compruebaModificadoRegistro();     
}

function getRelaciones(datosRelaciones){	
    if(datosRelaciones!=undefined && datosRelaciones!=null){	
        datosRel=datosRelaciones.split(',');	
        relacionesArray=new Array();	
        count= datosRel.length/3;	
        var j=-1	
        for(i=0; i<count; i++){	
            relacionesArray[i]=[datosRel[++j],datosRel[++j],datosRel[++j]]	
        }	
        return relacionesArray;	
    }	
 }

function getArrayTemas(listaTemas){	
    if(listaTemas!=undefined && listaTemas!= null){	
        lt=listaTemas.split(',');	
        var arrayListaTemas=new Array();	
        var temas= new Array();	
        arrayListaTemas[0]=lt[0];	
        var j=0;	
        var count=Math.floor((lt.length)/2);	
        for(i=0; i<count; i++){	
            temas[i]=[lt[++j],lt[++j]];	
        }	
        arrayListaTemas.push(temas);	
        return arrayListaTemas;	
    }	
   }

///// Funciones para manejo de las notificaciones por correo

/* Desactiva el checkbox y el boton 'Notificar a' */
function desactivaNotificaciones() {
    document.forms[0].enviarCorreo.disabled = true;
    document.forms[0].enviarCorreo.checked = false;
    document.getElementById('checkBoxNotificar').className = "etiquetaDeshabilitada";
    var vector = new Array(document.forms[0].cmdNotificar); 
    deshabilitarGeneral(vector);              
}

/* Activa el checkbox de notificar , el boton se queda deshabilitado hasta que se marque el checkbox */
function activaNotificaciones() {
    document.forms[0].enviarCorreo.disabled = false;
    document.getElementById('checkBoxNotificar').className = "etiqueta";
    var vector = new Array(document.forms[0].cmdNotificar); 
    deshabilitarGeneral(vector);              
}

/* Activa o desactiva el boton 'Notificar a' según este marcado el checkbox o no */
function onChangeNotificar() {
   if(!document.forms[0].enviarCorreo.readOnly){
    var vector = new Array(document.forms[0].cmdNotificar);
    if (document.forms[0].enviarCorreo.checked == true)
        habilitarGeneral(vector);
    else
        deshabilitarGeneral(vector);
   }
}

/* Carga los roles de un procedimiento en la pagina */
function cargarDatosProcedimiento(codRolDefecto, descRolDefecto,
                  codRolesAnteriores, codRolesNuevos, descRolesNuevos, listaDocs,
                  lista_cod_roles, lista_desc_roles, lista_defecto_roles) {

    // Se recarga el combo de roles
    cod_roles = lista_cod_roles;
    desc_roles = lista_desc_roles;
    defecto_roles = lista_defecto_roles;
    comboRol.addItems(cod_roles, desc_roles);
    comboRol.buscaCodigo(codRolDefecto);

    // Actualizamos el codigo de procedimiento usado para cargar los interesados
    document.forms[0].codProcedimientoRoles.value=document.forms[0].cod_procedimiento.value;;
    
    rolPorDefecto = codRolDefecto;
    descRolPorDefecto = descRolDefecto;
    cambiarRolesTerceros(codRolesAnteriores, codRolesNuevos, descRolesNuevos, codRolDefecto);
    crearListas();
    if (terceroActual != -1) {
        mostrarInteresado(terceroActual);
    }
    
    // Se recarga la tabla de documentos.
    listaDoc = listaDocs;
    tabDoc.lineas=listaDocs;
    refrescaDoc();
    
    //se comprueba si el procedimiento admite digitalización
    var codPro = document.forms[0].cod_procedimiento.value;
    for(i=0; i<cod_procedimientos.length;i++){
        if(codPro==cod_procedimientos[i]){
             comprobarDigitalizacionProcedimiento(digit_procedimientos,i);
             break;
        }
    }
}

/* Carga los datos de algun asunto en la pagina */
function cargarDatosAsunto(unidadTram, procedimiento, descProcedimiento, digitProcedimiento, mun_procedimiento, codRolDefecto, descRolDefecto,
     listaDocs, notificar, listaCorreo, codRolesAnteriores, codRolesNuevos, descRolesNuevos,
     lista_cod_roles, lista_desc_roles, lista_defecto_roles,bloquearDestino,bloquearProcedimiento) { 
    document.forms[0].cod_uniRegDestinoORD.value = "";
    document.forms[0].desc_uniRegDestinoORD.value = "";
    document.forms[0].cod_uor.value = "";
    
     compruebaTipoDoc(document.forms[0].txtCodigoDocumento.value,document.forms[0].txtNomeDocumento.value);

    
    if (unidadTram!='-1') {
        // Buscamos el cod visible de la unidad mediante una funcion de uor.js que realiza 
        // la busqueda en el array 'uors' de esta pagina.
        var codVis = (buscarUorPorCod(unidadTram)).uor_cod_vis;     
        document.forms[0].cod_uor.value = codVis;
        onchangeCod_uniRegDestinoORD();     
        cambiaUnidadOrganica();
    } else {        
        //mostrarListaProcedimientos();
        pleaseWait('off');
        
        // No hay seleccionada ninguna unidad tramitadora, desactivamos los campos de notificaciones.
        document.forms[0].enviarCorreo.disabled = true;
        document.getElementById('checkBoxNotificar').className = 'etiquetaDeshabilitada';
    }
    
    // Ahora toca el procedimiento.
    if (procedimiento!='' && procedimiento!='null') {
        // Si el tipo de entrada es 'Destino otro registro' no hay procedimiento, 
        // asi que se cambia.
        if (document.forms[0].cbTipoEntrada.value == '1') {
            // Si no esta activo se cambia
            if(!(typeof integracionSIRLanbide != undefined  && integracionSIRLanbide == "1")){
                document.forms[0].cbTipoEntrada.value = '0';
                mostrarDestino();
            }
        }

        // El javascript se pierde en algun sitio si no estamos en la pestaña con el
        // combo de procedimiento al cambiarlo, asi que vamos a esa pestaña.
        // Se asigna el mismo valor a cod_procedimiento_anterior pq ya se cargan los roles 
        // con el asunto, si no se volverían a cargar a través de onFocus_CodProcedimiento.

        tp1.setSelectedIndex(0);
        if(estaPluginExpRelacionadosFlexiaCargado=="SI"){
            document.forms[0].cod_procedimiento.value = procedimiento;
            cod_procedimiento_anterior = procedimiento;
            // Buscar el municipio del procedimiento
            for(i=0; i<cod_procedimientos.length; i++) {
                if (procedimiento == cod_procedimientos[i])
                    document.forms[0].mun_procedimiento.value = mun_procedimientos[i];
            }
            if(descProcedimiento != '' && descProcedimiento != 'null' && bloquearProcedimiento) {
                document.forms[0].desc_procedimiento.value = descProcedimiento;
                pleaseWait('off');
                inicializaComprobarDigitalizacionProcedimiento(procedimiento, descProcedimiento, digitProcedimiento, mun_procedimiento);
                comprobarDigitalizacionProcedimiento(digit_procedimientos,0);
            } else {
                onchangeCod_procedimiento();
            }
        }
    } else { // Borrar procedimiento 
         pleaseWait('off');
        if(estaPluginExpRelacionadosFlexiaCargado=="SI"){
            document.forms[0].cod_procedimiento.value = '';
            document.forms[0].desc_procedimiento.value = '';
            document.forms[0].txtExp1.value = '';
            $("[name='cmdDigitalizarDesdeAlta']").hide();
        }
    }

    if(estaPluginExpRelacionadosFlexiaCargado=="SI"){
        // Gestion de los roles, se copian los valores devueltos, se recarga el combo
        // y se usa el mapeo devuelto para cambiar los roles de los interesados.
        cod_roles = lista_cod_roles;
        desc_roles = lista_desc_roles;
        defecto_roles = lista_defecto_roles;
        comboRol.addItems(cod_roles, desc_roles);
        comboRol.buscaCodigo(codRolDefecto);

        document.forms[0].codProcedimientoRoles.value = document.forms[0].cod_procedimiento.value;
        rolPorDefecto = codRolDefecto;
        descRolPorDefecto = descRolDefecto;
        cambiarRolesTerceros(codRolesAnteriores, codRolesNuevos, descRolesNuevos, codRolDefecto);
        crearListas();
        if (terceroActual != -1) {
            mostrarInteresado(terceroActual);
        }
    }

    // Se recarga la tabla de documentos.
    listaDoc = listaDocs;
    tabDoc.lineas=listaDocs;
    refrescaDoc();

    // Lista de uors a notificar por correo
    if (notificar) {
        document.forms[0].enviarCorreo.checked = true;
        listaUorsCorreo = listaCorreo;
    } else {
        document.forms[0].enviarCorreo.checked = false;
    }
    onChangeNotificar();
    if(bloquearDestino=="true" | bloquearDestino)  bloquearUnidadDestino(bloquearDestino);
    else desBloquearUnidadDestino();  
    if(bloquearProcedimiento=="true" | bloquearProcedimiento)  bloquearUnidadProcedimiento(bloquearProcedimiento);
    else desBloquearUnidadProcedimiento();
}

function desBloquearUnidadDestino(){
	
        // habilitamos el primer input de la unidad de destino
    $("input[name='cod_uor']").prop('disabled', false);
    $("input[name='cod_uor']").prop('readonly', false);
    //var desbloquearDatos = new Array(document.forms[0].cod_uor);
    //habilitarGeneral(desbloquearDatos);
    if (document.getElementsByName("cbTipoEntrada")[0].value == 1){
        $("input[name='cod_uor']").hasClass("inputTextoDeshabilitado") ? $("input[name='cod_uor']").removeClass("inputTextoDeshabilitado").addClass("inputTexto") : $("input[name='cod_uor']").addClass("inputTexto");    
    } else {
        $("input[name='cod_uor']").hasClass("inputTextoDeshabilitado") ? $("input[name='cod_uor']").removeClass("inputTextoDeshabilitado").addClass("inputTextoObligatorio") : $("input[name='cod_uor']").addClass("inputTextoObligatorio");
    }

    // habilitamos el segundo input
    $("input[name='desc_uniRegDestinoORD']").prop('disabled', false);
    $("input[name='desc_uniRegDestinoORD']").prop('readonly', true);
    $("input[name='desc_uniRegDestinoORD']").hasClass("inputTextoDeshabilitado") ? $("input[name='desc_uniRegDestinoORD']").removeClass("inputTextoDeshabilitado").addClass("inputTexto") : $("input[name='desc_uniRegDestinoORD']").addClass("inputTexto");


    // habilitamos el selector
    $("span[name='botonUnidadeRexistroORD']").hasClass("faDeshabilitado") ? $("span[name='botonUnidadeRexistroORD']").removeClass("faDeshabilitado").addClass("fa") : $("span[name='botonUnidadeRexistroORD']").addClass("fa");
    $("a[name='anchorUnidadeRexistroORD']").hasClass("faDeshabilitado") ? $("a[name='anchorUnidadeRexistroORD']").removeClass("faDeshabilitado").addClass("fa") : $("a[name='anchorUnidadeRexistroORD']").addClass("fa");
    document.getElementsByName('anchorUnidadeRexistroORD')[0].style.cursor = 'pointer';
     
 }
 
 function desBloquearUnidadProcedimiento() {
     
     // ponemos a readOnly = false el campo del código del procedimiento
        document.getElementsByName('cod_procedimiento')[0].readOnly = false;
        // habilitamos la función de al clickar en el campo de la descripción del procedimiento
        document.getElementsByName('desc_procedimiento')[0].setAttribute("onclick", "javascript:{onClickDesc_procedimiento();}");      
        // deshabilitamos el botón selector
        var vectorBoton1 = new Array(document.getElementsByName('anchorProcedimiento')[0]);
        deshabilitarIconos(vectorBoton1, false);
        if ($("span[name='botonProcedimiento']").hasClass("faDeshabilitado")) {
            $("span[name='botonProcedimiento']").removeClass("faDeshabilitado");
        }
        $("span[name='botonProcedimiento']").addClass("fa");
        document.getElementsByName('anchorProcedimiento')[0].style.cursor = 'pointer';
}
 


/* Utiliza un mapeo de roles para asignar roles nuevos a los terceros */
function cambiarRolesTerceros(codRolesAnteriores, codRolesNuevos, descRolesNuevos, codRolDefecto) {
    // Para cada tercero
    for(i=0; i<terceros.length; i++) {
        codRolTercero = terceros[i][7];
        //Buscamos el codigo en el mapeo
        for(j=0; j<codRolesAnteriores.length; j++) {
            if (codRolesAnteriores[j] == codRolTercero) {
                terceros[i][7] = codRolesNuevos[j];
                terceros[i][3] = descRolesNuevos[j];
                // Indicamos si el rol nuevo es el rol por defecto
                if (codRolesNuevos[j] == codRolDefecto) {
                    terceros[i][6] = "SI";
                } else {
                    terceros[i][6] = "NO";
                }
                break;
            }
        }
    }
}

/* Redirige a un asiento relacionado*/
function verAnotacion(tipo, ejercicio, numero){
        
  document.forms[0].tipoAsiento.value=tipo;
  document.forms[0].ejercicioAsiento.value=ejercicio;
  document.forms[0].numeroAsiento.value=numero;
     
  document.forms[0].opcion.value="consultar";
  document.forms[0].tipoConsulta.value='registro'; // Registro a registro
  document.forms[0].target="mainFrame";
  document.forms[0].submit();
}

function cargarListaTipoDocs(){
    inicializarValores('cbTipoDoc','descTipoDoc',cod_tiposIdInteresado,desc_tiposIdInteresado);
}

function mostrarDescripcionTipoDoc(){
    if (!(Trim(document.forms[0].cbTipoDoc.value) == '')) {
        actualizarDescripcion('cbTipoDoc','descTipoDoc',cod_tiposIdInteresado,desc_tiposIdInteresado);
    } else document.forms[0].descTipoDoc.value="";
}

function recuperaListaProcedimientos(cP,dP,munP,digitP) {
    var auxCodPro = document.forms[0].cod_procedimiento.value; 
    
    cod_procedimientos = cP;
    desc_procedimientos = dP;
    mun_procedimientos = munP;
    digit_procedimientos = digitP;
    if (auxCodPro!='' && auxCodPro!='null') {
        var i = 0;
        for(i=0; i<cod_procedimientos.length; i++) {
            if (auxCodPro == cod_procedimientos[i])
                break;
        }
        if (i>=cod_procedimientos.length){
            document.forms[0].cod_procedimiento.value = '';
            document.forms[0].desc_procedimiento.value = '';
        } 
    } 
    //Al desplegar los procedimientos vaciamos el expediente relacionado si lo hubiese
    document.forms[0].txtExp1.value = '';
    inicializarValores('cod_procedimiento','desc_procedimiento',cP,dP);
    comprobarDigitalizacionProcedimiento(digit_procedimientos, i);
    pleaseWait('off');    
}

function recuperaListaProcedimientosExpRel(cP,dP,munP) {
    cod_procedimientos = cP;
    desc_procedimientos = dP;
    mun_procedimientos = munP;
    var codP = document.forms[0].cod_procedimiento.value;
    var encontrado = false;
    for (var i=0;i<cod_procedimientos.length;i++) {
        if (cod_procedimientos[i]==codP) {
            encontrado = true;
            document.forms[0].desc_procedimiento.value = desc_procedimientos[i];
            break;
        }
    }    
    if (encontrado) {        
        actualizarValDiv('cod_procedimiento','desc_procedimiento');
        document.forms[0].expRel.value='1';
        onFocus_CodProcedimiento();
    } else {
        jsp_alerta('A', 'El expediente no pertenece a la unidad destino/origen seleccionada');
        document.forms[0].cod_procedimiento.value='';
        document.forms[0].desc_procedimiento.value='';
        document.forms[0].txtExp1.value='';
    }
}

function ocultarBotonesInteresados() {
    ocultarBotonesNavegacionInteresados();
    ocultarBotonesEdicionInteresados();
}

function ocultarBotonesNavegacionInteresados() {
    hacerInvisibles(['flechaMenos','ordenTercero','flechaMas','botonListaTerceros']);
    
}

function ocultarBotonesEdicionInteresados() {
    hacerInvisibles(['botonSeleccionTerceros','botonEliminarTerceros','botonListaDomicilios']);
    comboRol.deactivate();
}

function mostrarOcultarCamposBusqueda() {
    if (document.getElementById('camposDatos').style.visibility == 'hidden') {
        document.getElementById('camposDatos').style.visibility = 'visible';
    } else document.getElementById('camposDatos').style.visibility = 'hidden';
}

function ocultarCapaDatosBusqueda() {
    document.getElementById('cargaDatos').style.visibility = 'hidden';
    document.getElementById('camposDatos').style.visibility = 'hidden';
    document.forms[0].anoCarga.value = '';
    document.forms[0].numeroCarga.value = '';
}

function mostrarCapaDatosBusqueda() {
    document.getElementById('cargaDatos').style.visibility = 'visible';    
}

function mostrarBotonesNavegacionInteresados() {
    hacerVisibles(['flechaMenos','ordenTercero','flechaMas','botonListaTerceros']);
}

function mostrarBotonesEdicionInteresados() {
    hacerVisibles(['botonSeleccionTerceros']);
    comboRol.activate();
    comboRol.obligatorio(true);
}

function hacerInvisibles(vector) {
    for (var i=0; i<vector.length; i++) {
        document.getElementById(vector[i]).style.visibility = 'hidden';
        
    }
}

function hacerVisibles(vector) {
    for (var i=0; i<vector.length; i++) {
        document.getElementById(vector[i]).style.visibility = 'visible';
        document.getElementById(vector[i]).disabled = false;
    }
}

function actualizarBotonesTercero() {
    if (terceros.length > 1) {
        mostrarBotonesNavegacionInteresados();

        // Orden dentro de los terceros
        document.getElementById('ordenTercero').firstChild.data =
            (terceroActual + 1) + ' de ' + terceros.length;

        // Flechas de navegación
        if (terceroActual < 1) {
            deshabilitarImagen([document.getElementById('flechaAnterior')], true);
        } else {
            habilitarImagen([document.getElementById('flechaAnterior')], true);
        }
        if (terceroActual > terceros.length - 2) {
            deshabilitarImagen([document.getElementById('flechaSiguiente')], true);
        } else {
            habilitarImagen([document.getElementById('flechaSiguiente')], true);
        }

        // Activar boton lista terceros
        habilitarImagen([document.getElementById('botonListTer')], true);
    } else {
        ocultarBotonesNavegacionInteresados();
    }

    // Botones de eliminar tercero y ver domicilios
    var vector = ['botonEliminarTerceros','botonListaDomicilios'];
    if (top.menu.modificando == 'S' && terceros.length > 0) {
        hacerVisibles(vector);
    } else {
        hacerInvisibles(vector);
    }

}


//Volcado de funciones de AltaRE.jsp (dia 30/12/2010)
    /********************************************************************/
     /* FUNCION INSERTARTERCEROPORDEFECTO                                */
     /********************************************************************/
     /**
     * Mete el tercero devuelto en 'datos' en la lista de terceros, con el rol
     * por defecto. Si el tercero ya existe se sobreescriben sus datos con los
     * devueltos. Si no existe en la lista de terceros, se inserta en el lugar
     * del primer tercero con rol por defecto.
     */
     function insertarTerceroPorDefecto(datos) {

         // Determinamos en que posicion se insertara el tercero
         var pos = determinarPosicionInsercion(datos[2]);
         // Determinamos si es una nueva entrada en la lista, en tal caso
         // se usa el rol por defecto, si no se conserva el rol actual.
         var esNuevo = (terceros[pos].length == 0);

         // Introducimos el nuevo tercero con rol por defecto
         terceros[pos][0]  = datos[2];
         terceros[pos][1]  = datos[3];
         terceros[pos][2]  = datos[6];
         if (esNuevo) {
             terceros[pos][3]  = descRolPorDefecto;
         }
         terceros[pos][4]  = datos[14];
         terceros[pos][5]  = datos[18];
         if (esNuevo) {
             terceros[pos][6]  = "SI";
             terceros[pos][7]  = rolPorDefecto;
         }
         terceros[pos][8]  = datos[11];
         terceros[pos][9]  = datos[12];
         terceros[pos][10] = datos[15];
         terceros[pos][11] = datos[16];
         terceros[pos][12] = datos[17];
         terceros[pos][13] = datos[19];
         terceros[pos][14] = datos[4];
         terceros[pos][15] = datos[5];

         // Si hay mas de un interesado activar navegacion
         if (terceros.length > 1) mostrarBotonesNavegacionInteresados();

         // Se recrean las listas
         crearListas();
     }

     /**
     * Determina la posicion en el array de terceros en la que hay que introducir
     * el nuevo tercero. Si ya existe se toma su posicion actual. Si
     * no existe se toma la posicion posterior a la ultima del array.
     */
     function determinarPosicionInsercion(codigoTercero) {
         // Comprobamos que no exista ya
         var pos = existeTercero(codigoTercero);

         if (pos == -1) { // No existe
             pos = terceros.length;
             terceros[pos] = new Array(); // Hacemos sitio para el nuevo
         }

         return pos;
     }

     /**
     * Mira en la lista de terceros si existe el tercero con el codigo pasado y en ese
     * caso devuelve su posicion en la lista, si no existe devuelve -1.
     */
     function existeTercero(codigo) {
         for(i=0; i<terceros.length; i++) if (terceros[i][0] == codigo) return i;
         return -1;
     }

     /**
     * Crea las listas de terceros, versiones, domicilios y roles que son las que despues se usan
     * en la action y en el dao para guardar la lista de interesados.
     */
     function crearListas() {
         var listaCodTercero = "";
         var listaVersionTercero = "";
         var listaCodDomicilios= "";
         var listaRol = "";
         var listaDescRol = "";
         for (i=0; i < terceros.length; i++) {
             listaCodTercero     += terceros[i][0] + '§¥';
             listaVersionTercero += terceros[i][1] + '§¥';
             listaRol            += terceros[i][7] + '§¥';
             listaCodDomicilios  += terceros[i][4] + '§¥';
             listaDescRol        += terceros[i][3] + '§¥';
         }
         document.forms[0].listaCodTercero.value = listaCodTercero;
         document.forms[0].listaVersionTercero.value = listaVersionTercero;
         document.forms[0].listaCodDomicilio.value = listaCodDomicilios;
         document.forms[0].listaRol.value = listaRol;
         document.forms[0].listaDescRol.value = listaDescRol;
     }


function deshabilitarTipoDocTerceroYDoc() {
var campos=null;
   if(document.all){//IE
        campos = new Array(document.forms[0].elements('txtDNI') ,
                           document.forms[0].elements('cbTipoDoc'),
                           document.forms[0].elements('descTipoDoc') );
   }
   else
   {
          var txtDNI = document.getElementsByName("txtDNI");
          var cbTipoDoc = document.getElementsByName("cbTipoDoc");
          var descTipoDoc = document.getElementsByName("descTipoDoc");
	  if((txtDNI!=null && txtDNI.length>=1)&&(cbTipoDoc!=null && cbTipoDoc.length>=1)&&(descTipoDoc!=null && descTipoDoc.length>=1))
		campos =  new Array(txtDNI[0],cbTipoDoc[0],descTipoDoc[0]);
   }
   deshabilitarGeneral(campos);

   var vectorBoton1 =  new Array(document.getElementsByName('botonTipoDoc')[0]/*,document.getElementsByName('botonTipoRemitente')[0] comentado para solucionar tarea 13243 */);
   var vectorAnchor1;
   if(document.all)
        vectorAnchor1 =  new Array(document.all.anchorTipoDoc/*,document.all.anchorTipoRemitente comentado para solucionar tarea 13243 */);
    else{
		var anchorsTipoDoc = document.getElementsByName("anchorTipoDoc");
		if(anchorsTipoDoc!=null && anchorsTipoDoc.length>=1)
			vectorAnchor1 =  new Array(anchorsTipoDoc[0]);
	}
   deshabilitarImagenBotonGeneral(vectorBoton1, true);
}

function habilitarTipoDocTerceroYDoc() {
    
   var campos=null;
   if(document.all){//IE
      
        campos = new Array(document.forms[0].elements('txtDNI') ,
                           document.forms[0].elements('cbTipoDoc'),
                           document.forms[0].elements('descTipoDoc') );
   }
   else
   {
          var txtDNI = document.getElementsByName("txtDNI");
          var cbTipoDoc = document.getElementsByName("cbTipoDoc");
          var descTipoDoc = document.getElementsByName("descTipoDoc");
	  if((txtDNI!=null && txtDNI.length>=1)&&(cbTipoDoc!=null && cbTipoDoc.length>=1)&&(descTipoDoc!=null && descTipoDoc.length>=1))
		campos =  new Array(txtDNI[0],cbTipoDoc[0],descTipoDoc[0]);
   }
   habilitarGeneral(campos);


   var vectorBoton1 =  new Array(document.getElementsByName('botonTipoDoc')[0],document.getElementsByName('botonTipoRemitente')[0]);
   var vectorAnchor1;
   if(document.all)
       vectorAnchor1 =  new Array(document.all.anchorTipoDoc,document.all.anchorTipoRemitente);
   else{
	   var anchorsTipoDoc = document.getElementsByName("anchorTipoDoc");
	   if(anchorsTipoDoc!=null && anchorsTipoDoc.length>=1)
		vectorAnchor1 =  new Array(anchorsTipoDoc[0]);
	}

   habilitarImagenBotonGeneral(vectorBoton1, true);
}

     function mostrarInteresado(indice) {
         // Asignamos a la var global terceroCargado que controla el funcionamiento
         // de mostrarDatosTercero el codigo del tercero que queremos mostrar.
         terceroCargado = terceros[indice][0];
         mostrarDatosTercero();
     }
 /********************************************************************/
/* FUNCION CARGAR FORMULARIOS							*/
 /********************************************************************/
         function cargarFormularios(listFormularios, listFormulariosOriginal){

             listaFormularios = listFormularios;
             listaFormulariosOriginal = listFormulariosOriginal;
             tabFormularios.lineas=listaFormularios;
             tabFormularios.displayTabla();
         }








function pulsarCancelarAltaDesdeTramite() {
    var datos = new Array();
    datos['resultado'] = 'cancelar';
    self.parent.opener.retornoXanelaAuxiliar(datos);
}

function altaTramitarRegistrada(dia, hora, ejerNum) {
    var datos = new Array();
    datos['resultado'] = 'ok';
    datos['dia'] = dia;
    datos['hora'] = hora;
    datos['anotacion'] = ejerNum;
    mostrarMensajeAltaRegistroDesdeTramitar(datos)
    self.parent.opener.retornoXanelaAuxiliar(datos);
}



function pulsarAnteriorTercero() {
    if (terceroActual > 0) {
        mostrarInteresado(terceroActual - 1);
    } else {
        alert('Error interesado anterior');
    }
}

function pulsarSiguienteTercero() {
    if (terceroActual < terceros.length - 1) {
        mostrarInteresado(terceroActual + 1);
    } else {
        alert('Error interesado siguiente');
    }
}






function bloquearFechaHoraAnotacion(){
	if(BLOQUEO_FECHA_HORA_ANOTACION){
		document.forms[0].fechaAnotacion.readOnly = true;
		document.forms[0].horaMinAnotacion.readOnly = true;
	}
}

//FIN Volcado de funciones de AltaRE.jsp (dia 30/12/2010)

function habilitarDocumento(valor){
    var vectorI =	["txtDNI"];
    habilitarGeneralInputs(vectorI,valor);
}

function analizarDocumento(){
  if(!document.forms[0].cbTipoDoc.readOnly){
    if(document.forms[0].cbTipoDoc.value =="0"){
        habilitarDocumento(false);
    }
    else{habilitarDocumento(true);}
  }
}

function transformarJSONArrayTerceros(jsonTerceros) {
    var terceros = new Array();
    for (var i = 0; i < jsonTerceros.length; i++) {
        var unTercero = new Array();
        for (var j = 0; j < jsonTerceros[i].length; j++) {
            unTercero[j] = unescape(jsonTerceros[i][j]);
        }
        terceros[i] = unTercero;
    }
    return terceros;
}

// Al cambiar el combo rol asigna el rol seleccionado al tercero mostrado
function onChangeComboRol() {
    if (top.menu.modificando == 'S' && terceros.length > 0) {
        var indiceRol = comboRol.selectedIndex;
        terceros[terceroActual][7] = cod_roles[indiceRol];
        terceros[terceroActual][3] = desc_roles[indiceRol];
        terceros[terceroActual][6] = defecto_roles[indiceRol];
        crearListas();
    }
}

function getLetraNif(dni) {
    var lockup = 'TRWAGMYFPDXBNJZSQVHLCKE';
    return lockup.charAt(dni % 23);
}

function pulsarCancelarDuplicar() {
    pulsarCancelarModificar();
    //Cambiamos el valor de mod porque en pulsarCancelarModificar() se puso a 1 y necesitamos que siga siendo undefined
    mod='undefined';
    ocultarCapaDatosBusqueda();
}


function SoloCaracteresHora(objeto) {
    xAMayusculas(objeto);
    var tecla, caracter;
    var alfanumericos = '0123456789:';
    if (objeto){
        var original = objeto.value;
        var salida = "";
        for(i=0;original!=undefined && i<original.length;i++){
            if(alfanumericos.indexOf(original.charAt(i).toUpperCase())!=-1){
                salida = salida + original.charAt(i);
            }
     }
     objeto.value=salida.toUpperCase();
  }

}  
function tratarDocumentosPresentadosAlta()
{
    var txtListaDocEntregados='';
	
    for(i=0; i<listaDoc.length; i++) {
		
		if("SI"==listaDoc[i][0])
		{
			txtListaDocEntregados=txtListaDocEntregados+'S'+'§¥';
			
		}
		else if("NO"==listaDoc[i][0])
		{
			txtListaDocEntregados=txtListaDocEntregados+'N'+'§¥';
		}
		else 
		{
			txtListaDocEntregados=txtListaDocEntregados+listaDocEntregados[i]+'§¥';
			
		}
		
        
    }
	document.forms[0].txtListaDocEntregados.value=txtListaDocEntregados;
	
	
}
function marcarDocumentosPresentadosAlta(i)
{	

    if('S'==(listaDocEntregados[i]))
	{
		
        listaDocEntregados[i]='N';
	}
	else{
		listaDocEntregados[i]='S';
		
	}

	
		
}

function limpiarListaDocumentosSeleccionados()
{
	listaDocEntregados= new Array();
}

function refrescaDoc() {
	if(alta)
	{	
		var listaDocNuevo = tabDoc.lineas;
		 for(i=0; i<listaDocNuevo.length; i++) {
                 var str='';
				 //Si se ha adjuntado un documento. Es que se ha entregado en el alta.
	     if(listaDocNuevo[i][3]!='') 
		 {
		 listaDocEntregados[i]='S'; 
		 str='<input type="checkbox" class="check" name="entregadoAlta" disabled checked onclick="marcarDocumentosPresentadosAlta('+i+')"';
			
		 }
		 else if('S'==listaDocEntregados[i]) str='<input type="checkbox" class="check" name="entregadoAlta" checked onclick="marcarDocumentosPresentadosAlta('+i+')"';    
		 else {
			listaDocEntregados[i]='N'; 
			str='<input type="checkbox" class="check" name="entregadoAlta"  onclick="marcarDocumentosPresentadosAlta('+i+')"';  
		 }
		
		 listaDocNuevo[i][0]=str;
		 
		}
		
		tabDoc.lineas=listaDocNuevo;
	}

    tabDoc.displayTabla();

}

function clean() {
    lista=new Array();
    lista2=new Array();
    tabDoc.lineas=lista2;
    refrescaDoc();
}


function borrarYDeshabilitarExpediente() {
    document.forms[0].cod_procedimiento.value='';
    document.forms[0].desc_procedimiento.value='';
    document.forms[0].txtExp1.value = '';
    var vectorExp = [document.forms[0].txtExp1];
    deshabilitarGeneral(vectorExp);
    var vectorImg = new Array(document.getElementsByName("consultaExpediente")[0]);
    deshabilitarImagen(vectorImg, true);
}

function habilitarExpediente() {
    var vectorImg = new Array(document.getElementsByName("consultaExpediente")[0]);
    habilitarImagen(vectorImg, true);
    var vectorExp = [document.forms[0].txtExp1];
    habilitarGeneral(vectorExp);

}

function deshabilitarTipoDocYRemitente() {
    document.forms[0].txtCodigoDocumento.disabled = true;
    document.forms[0].txtNomeDocumento.disabled = true;
    document.forms[0].cod_tipoRemitente.disabled = true;
    document.forms[0].txtNomeTipoRemitente.disabled = true;
    var vectorBoton1 =  new Array(document.getElementsByName('botonTipoDocumento')[0],document.getElementsByName('botonTipoRemitente')[0]);
    deshabilitarIconos(vectorBoton1, true);
}

function habilitarTipoDocYRemitente() {
    document.forms[0].txtCodigoDocumento.disabled = false;
    document.forms[0].txtNomeDocumento.disabled = false;
    document.forms[0].cod_tipoRemitente.disabled = false;
    document.forms[0].txtNomeTipoRemitente.disabled = false;

    var vectorBoton1 =  new Array(document.getElementsByName('botonTipoDocumento')[0],document.getElementsByName('botonTipoRemitente')[0]);
    deshabilitarIconos(vectorBoton1, false);
}


function noRecuperaDatos() {
    borrarPaginaBuscada();
    clean();
    desactivarFormulario();
    deshabilitarBuscar();
    ocultarInfSoloConsulta();
    modificando('N');
    mostrarDestino();
    if ( document.getElementById("capaBotones3") ) {
        document.getElementById("capaBotones3").style.display = '';
        document.getElementById("capaCunho").style.visibility = 'visible';
        document.forms[0].cmdCancelarBuscada.disabled=false;
        document.forms[0].cmdCuneus.disabled=false;
        if (document.forms[0].cmdModificar) document.forms[0].cmdModificar.disabled=true; document.getElementById('cmdModificar').style.color = '#CCCCCC';
        if (document.forms[0].cmdAnular) document.forms[0].cmdAnular.disabled=true; document.getElementById('cmdAnular').style.color = '#CCCCCC';
        if (document.forms[0].cmdDuplicar) document.forms[0].cmdDuplicar.disabled=true;
        if (document.forms[0].cmdAltaDesdeConsulta) document.forms[0].cmdAltaDesdeConsulta.disabled=true;
        if (document.forms[0].cmdContestar) document.forms[0].cmdContestar.disabled=true; 
        if (document.forms[0].cmdResponder) document.forms[0].cmdResponder.disabled=true;
        if (document.forms[0].cmdRelacionar) document.forms[0].cmdRelacionar.disabled=true;
    }
    document.forms[0].cmdListado.disabled=false;
}


function anotacionAnulada(mnsj) {
    jsp_alerta("A", mnsj);
    modificando('N');
    mostrarDestino();
}

function iniciarDuplicar(datos,datosTercero,listaTemas,lista_CODtiposDocumentos,
        lista_DESCtiposDocumentos, lista_ACTtiposDocumentos,	
        lista_CODtiposDocumentosAlta, lista_DESCtiposDocumentosAlta, lista_ACTtiposDocumentosAlta,	
        lista_CODactuaciones, lista_DESCactuaciones,        lista_CODtiposTransportes, lista_DESCtiposTransportes, lista_ACTtiposTransportes, lista_CODtiposRemitentes,
        lista_DESCtiposRemitentes, lista_ACTtiposRemitentes, lista_CODtemas, lista_DESCtemas,lista_CODidInteresado,
        lista_DESCidInteresado,lista_CODdpto, lista_DESCdpto, fecha, listaDocs,listaAnteriores, listFormularios, listFormulariosOriginal,
        listaInteresados,listaRelaciones, lista_uni_asuntos, lista_cod_asuntos, lista_desc_asuntos,
        lista_cod_roles, lista_desc_roles, lista_defecto_roles, fechaServidor, horaServidor,asuntoCodificadoObligatorio,bloquearDestino,bloquearProcedimiento,procedimientoDigitalizacion) {
    modificando('S');
    recuperaDatos(datos,datosTercero,listaTemas,lista_CODtiposDocumentos, lista_DESCtiposDocumentos, lista_ACTtiposDocumentos,
            lista_CODtiposDocumentosAlta, lista_DESCtiposDocumentosAlta, lista_ACTtiposDocumentosAlta,
            lista_CODactuaciones, lista_DESCactuaciones, lista_CODtiposTransportes, lista_DESCtiposTransportes, lista_ACTtiposTransportes,
            lista_CODtiposRemitentes, lista_DESCtiposRemitentes, lista_ACTtiposRemitentes, lista_CODtemas, lista_DESCtemas,lista_CODidInteresado,
            lista_DESCidInteresado,lista_CODdpto, lista_DESCdpto, fecha, listaDocs, listaAnteriores, listFormularios, listFormulariosOriginal,
            listaInteresados, listaRelaciones, lista_uni_asuntos, lista_cod_asuntos, lista_desc_asuntos,
            lista_cod_roles, lista_desc_roles, lista_defecto_roles,'',true,bloquearDestino,undefined,undefined,false,false,procedimientoDigitalizacion);          
    terceros=listaInteresados;
    fechaHoy = fecha;

    document.forms[0].fechaAnotacion.value=fechaServidor;
    document.forms[0].fechaDocumento.value=fechaServidor;
    document.forms[0].horaMinAnotacion.value = horaServidor;

        ejercicioBuscada = document.forms[0].ano.value;
	numeroBuscada = document.forms[0].numero.value;
        document.forms[0].ano.value="";
        document.forms[0].numero.value="";
        activarFormulario();        
        // Mantenemos deshabilitados los campos de notificaciones.
        desactivaNotificaciones();  
        deshabilitarDatosSGA();
        changeNotifs = true;
	//cambiaUnidadOrganica();
	deshabilitarBuscar();
	desactivarOrigenYExpediente();
	desactivarInteresado();
	var vCamposCodigo= camposCodigo();
        cambiarLongMaxInput(vCamposCodigo,longMaxInputCodigo);
        var vCamposFecha = camposFecha();
        cambiarLongMaxInput(vCamposFecha,longMaxInputFecha);
        if (document.getElementById('capaNavegacionConsulta')) domlay('capaNavegacionConsulta',0,0,0,'');
        ocultarCapaAnulacion();
	ocultarCapaDiligencia();    
	ocultarInfSoloConsulta();    
	mostrarDestino();
	tp1.setSelectedIndex(0);
        mostrarCapasBotones('capaBotones5');
        desactivarCampoDocumento();
	comboAsuntos.activate();
	deshabilitarTipoDocTerceroYDoc();
	mostrarCapaDatosBusqueda();
	var vectorFecDoc = [document.forms[0].fechaDocumento];
        deshabilitarGeneral(vectorFecDoc);        
        bloquearFechaHoraAnotacion();
        
        
        if(asuntoCodificadoObligatorio){        
            cambiarEstadoComboAsuntoCodificado(true);
        }
        else{            
            cambiarEstadoComboAsuntoCodificado(false);
        }
        bloquearUnidadDestino(bloquearDestino);
        bloquearUnidadProcedimiento(bloquearProcedimiento);
        
        // Marcar el estado de finalizacion de digitalizacion como no finalizado, ya que es una nueva anotacion
        document.forms[0].finDigitalizacion.value=false;
}


     
     
     function horaMin() {

                 var today = new Date();
                 var hoy;
                 var hora;
                 var min;

                 hoy = today.toString();
                 if (hoy.substring(9,10)==" ") {
                     hora = hoy.substring(10,12);
                     min = hoy.substring(13,15);
                 } else {
                 hora = hoy.substring(11,13);
                 min = hoy.substring(14,16);
             }
             document.forms[0].horaMinAnotacion.value=hora+":"+min;
         }
     
     
       ////////////////////////////////////////////////////
     function mostrarArbolClasifAsuntos() {
     
      var codigo="";
      //Recogemos el codAsunto que está seleccionado en la jsp, (si es que hay alguno seleccionado)
      if (document.forms[0].codAsunto!=null){
         codigo=document.forms[0].codAsunto.value;
      }
      //Abrimos la venta del arbol, pasamos el codigo Asunto si hay, para saber
      //que nodo se tiene que desplegar
      abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + APP_CONTEXT_PATH + 
                                        "/MantAnotacionRegistro.do?opcion=seleccionClasificacion&codAsunto="+codigo,'',
                                            'width=670,height=470',function(datos){
                //Se devuelve el codAsunto seleccionado en el arbol  
                if( datos!=undefined ){

                    if(document.getElementById("codAsunto")!=null){
                       document.getElementById("codAsunto").value=datos[1];      

                    }
                   if(document.getElementById("descAsunto")!=null){
                      document.getElementById("descAsunto").value=datos[0];     
                   }

                 comboAsuntos.buscaCodigo(datos[1]);
                //Ejecutamos la funcion onChangeComboAsuntos();
                 onChangeComboAsuntos("arbolClasif");
               }
           });
  } 
     
     function comprobarFecha(inputFecha,mensaje) {
     var formato = 'dd/mm/yyyy';
     if (Trim(inputFecha.value)!='') {

         if (consultando) {
             var validas = true;
             var fechaFormateada=inputFecha.value;
             var pos=0;
             var fechas = Trim(inputFecha.value);
             var fechas_array = fechas.split(/[:|&<>!=]/);


             for (var loop=0; loop < fechas_array.length; loop++)
                 {
                     f = fechas_array[loop];
                     formato = formatoFecha(Trim(f));
                     var D = ValidarFechaConFormato(f,formato);
                     if (!D[0]) validas=false;
                     else {
                         if (fechaFormateada.indexOf(f,pos) != -1) {
                             var toTheLeft = fechaFormateada.substring(0, fechaFormateada.indexOf(f));
                             var toTheRight = fechaFormateada.substring(fechaFormateada.indexOf(f)+f.length, fechaFormateada.length);
                             pos=fechaFormateada.indexOf(f,pos);
                             fechaFormateada = toTheLeft + D[1]+ toTheRight;
                         }
                     }
                 }

                 if (!validas) {
                     jsp_alerta("A",mensaje);
                         tp1.setSelectedIndex(0);
                         inputFecha.focus();
                         return false;
                     } else {
                     inputFecha.value = fechaFormateada;
                     return true;
                 }

             } else { // No consultando. Unico formato posible: dd/mm/yy o dd/mm/yyyy
                inputFecha.value = inputFecha.value.substring(0,10);
                var D = ValidarFechaConFormato(inputFecha.value,formato);
             if (!D[0]){
                 jsp_alerta("A",mensaje);
                     tp1.setSelectedIndex(0);
                     inputFecha.focus();
                     return false;
                 } else {
                 inputFecha.value = D[1];
                 return true;
             }
         }
     }
     return true;
 }
 
 
 //function cargarComboAsuntos(lista_uni_asuntos, lista_cod_asuntos, lista_desc_asuntos, asunto) {
 function cargarComboAsuntos(lista_uni_asuntos, lista_cod_asuntos, lista_desc_asuntos, asunto, bajaAsunto) {                
        // Cargamos el combo
        uni_asuntos = lista_uni_asuntos;
        cod_asuntos = lista_cod_asuntos;
        desc_asuntos = lista_desc_asuntos;
        comboAsuntos.addItems(cod_asuntos, desc_asuntos);
        // Seleccion del asunto indicado
        var i=0;
        
        while ((i<lista_cod_asuntos.length) && (asunto != lista_cod_asuntos[i])) i++;        
                
        if (i == lista_cod_asuntos.length && (bajaAsunto=="false" || bajaAsunto=="")){            
            // No encontrado
            if (asunto != '' && asunto != null) jsp_alerta('A','Código de asunto no encontrado: ' + asunto);            
            comboAsuntos.selectItem(0);
            indice_asunto_anterior = 0;
        } else { 
            indice_asunto_anterior = i+1;
            //comboAsuntos.selectItem(i+1);
            comboAsuntos.buscaCodigo(asunto);
            
            document.forms[0].uniRegAsunto.value=uni_asuntos[i];
        }
    }

    function habilitarDivMsgAsuntoBaja(flag){        
        document.getElementById("divAsuntoBaja").style.visibility = "hidden";
        if(flag){
            document.getElementById("divAsuntoBaja").style.visibility = "visible";
        }
        
    }
    
    function pulsarEliminarTercero(mensaje) {
        if(jsp_alerta('', mensaje)) {
            var nuevosTerceros = new Array();
            var j=0;
            for (var i=0; i< terceros.length; i++) {
                if (i!=terceroActual) nuevosTerceros[j++] = terceros[i];
            }
            terceros = nuevosTerceros;
            mostrarDatosTercero();
            //Recrear listas
            crearListas();
        }
    }
    
    function cargarComboExpedientesRelacionados(listaExpedientesRelacionados, contIdNumsExpedientesRelacionados){        
        document.forms[0].codListaExpedientesRelacionados.value = "";
        document.forms[0].descListaExpedientesRelacionados.value = "";
        comboExpedientesRelacionados.addItems(contIdNumsExpedientesRelacionados, listaExpedientesRelacionados);
        comboExpedientesRelacionados.activate();        
        if(document.getElementById("imgListaExpedientesSeleccionados")){
            document.getElementById("imgListaExpedientesSeleccionados").className = 
                    document.getElementById("imgListaExpedientesSeleccionados").className.replace("faDeshabilitado","");
        }        
    }//cargarComboExpedientesRelacionados
    
    function limpiarComboExpedientesRelacionados(){
        var listaExpedientesRelacionados = new Array();
        var contIdNumsExpedientesRelacionados = new Array();
        comboExpedientesRelacionados.addItems(contIdNumsExpedientesRelacionados, listaExpedientesRelacionados);
    }//limpiarComboExpedientesRelacionados

    /*****************/
    function cambiarEstadoComboAsuntoCodificado(obligatorio){
            var color = '#FFFFFF';	
            if(obligatorio) color='#fffea3';

        if(document.getElementById("descAsunto")){        
                    document.getElementById("descAsunto").style.backgroundColor  = color;                 
        }
        if(document.getElementById("codAsunto")){        
            document.getElementById("codAsunto").setAttribute ('class', 'inputTextoObligatorio');                 
        }
    }

function actualizarComboAsuntos(listaUnidadRegistro,listaCodigosAsuntos,listaDescAsuntos){  
    uni_asuntos = listaUnidadRegistro;
    listaCodigosAsuntos =  listaCodigosAsuntos;
    listaDescAsuntos =   listaDescAsuntos;
    
    comboAsuntos.addItems(listaCodigosAsuntos, listaDescAsuntos);
    comboAsuntos.selectItem(0);
}

function onClickmostrarMenos(){
	document.getElementById("capaInfoAuxInteresado").style.display="none";
    document.getElementById("capaInfoAuxInteresado2").style.display="none";
	document.getElementById("mostrarMas").style.display="block";
	document.getElementById("mostrarMenos").style.display="none";
}

function onClickmostrarMas(){
	document.getElementById("capaInfoAuxInteresado").style.display="";
    document.getElementById("capaInfoAuxInteresado2").style.display="";
	document.getElementById("mostrarMas").style.display="none";
	document.getElementById("mostrarMenos").style.display="block";
}


function OcultaTipoDocLabel(){
    document.getElementById("capaTipoDocNoexiste").style.visibility = 'hidden';
}
function cargarTipoDocAux(opcion){
    if (opcion == "Todos"){
        cod_tiposDocumentosAux = cod_tiposDocumentos;
        desc_tiposDocumentosAux = desc_tiposDocumentos;
      } else if(opcion == 'Alta'){
        cod_tiposDocumentosAux = cod_tiposDocumentosAlta;
        desc_tiposDocumentosAux = desc_tiposDocumentosAlta;
     } else if(opcion == 'conPreSel'){
        cod_tiposDocumentosAux = cod_tiposDocumentosAlta;
        desc_tiposDocumentosAux = desc_tiposDocumentosAlta;
     } 
    document.getElementById("capaTipoDocNoexiste").style.visibility = 'hidden';
	
}
    
    



function pulsarBuzon(){    
    document.forms[0].action = APP_CONTEXT_PATH + "/Buzon.do";
    document.forms[0].target="oculto";
    document.forms[0].submit();
}

function pulsarResponder() { 
    habilitarBuscar();
    mostrarDestino();
    activarInteresado();
    document.forms[0].opcion.value="responder";
    document.forms[0].modificar.value="1";
    document.forms[0].target="mainFrame";
    document.forms[0].action=APP_CONTEXT_PATH + "/MantAnotacionRegistro.do";
    document.forms[0].submit();
}

function pulsarRelacionar() { 
    habilitarBuscar();
    mostrarDestino();
    activarInteresado();
    activarTipoDoc();	
    cargarTipoDocAux("Alta");
    document.forms[0].opcion.value="relacionar";
    document.forms[0].modificar.value="1";
    document.forms[0].target="oculto";    
    document.forms[0].action=APP_CONTEXT_PATH + "/MantAnotacionRegistro.do";    
    document.forms[0].submit();
    estadolabelTipoDoc = 0;

}


     function ActivaBotonVerAnexosFormularios(){

    var marcado2=false;
    if (document.forms[0].cmdVerAnexo!=null){
        if (listaFormularios.length >0){
            document.forms[0].cmdVerAnexo.disabled=false;
            marcado2=true;
        }else{
            document.forms[0].cmdVerAnexo.style.display='none';
            marcado2=false;
        }
        if (document.getElementById("pestana7") != null) {
            var selected4 = document.getElementById("pestana7").className;
            var changeSelected4 = false;
            if ((selected4 == "tab page") || (selected4 == "tab-remark selected")|| (selected4 == "tab anexos")) {
                changeSelected4 = true;
            }
            if (marcado2) {
                if (existenAnexosFormularios=='si')document.getElementById("pestana7").className="tab anexos";
                else if (changeSelected4) document.getElementById("pestana7").className="tab-remark selected";
                else document.getElementById("pestana7").className="tab-remark";
            } else{
                if (changeSelected4) document.getElementById("pestana7").className="tab selected";
                else document.getElementById("pestana7").className="tab";

            }
        }
    }
}



function pulsarNotificar(){
    var args = new Array();
    var codsvi = new Array();
    var cods = new Array();
     var uord = new Array();
     var uorviDest=document.forms[0].cod_uor.value;

 //Sacamos del listado de unidades notificables la que pertenece a la uor de destino
 //y solo metemos las que tengan email.
    var j=0;
    for(i=0;i<uorcods.length;i++){
      if(uorcods[i]!=uorviDest && uors[i].uor_email!=null && uors[i].uor_email!=''){
         codsvi[j] = uorcods[i];
         cods[j] = uorcodsinternos[i];
         uord[j] = uordescs[i];
         j++;
      }
    }

    args[0] = codsvi;
    args[1] = cods;
    args[2] = uord;
    args[3] = listaUorsCorreo;

    listaAntigua = listaUorsCorreo;
    var source = APP_CONTEXT_PATH + "/jsp/registro/mainVentana.jsp?source=" + APP_CONTEXT_PATH + 
            "/jsp/registro/entrada/listaUorsCorreo.jsp?dummy=";
    abrirXanelaAuxiliar(source,args,
            'width=540,height=350',function(listaUorsCorreoResp){
                // Para el caso de que el usuario cierre la ventana con la X y se pierda la lista
                if (listaUorsCorreoResp == null || listaUorsCorreoResp == undefined) 
                    listaUorsCorreo = listaAntigua;
                else
                    listaUorsCorreo = listaUorsCorreoResp;
            });
}


function pulsarHistoricoAnotacion() {
    var source = APP_CONTEXT_PATH + "/jsp/registro/mainVentana.jsp?source=" + APP_CONTEXT_PATH +
            "/MantAnotacionRegistro.do?opcion=cargarHistorico";
    
    abrirXanelaAuxiliar(source,null,'width=842,height=432',function(){});
}

function pulsarImprimirModelo(opcion) {
    recuperarCodigoOfiReg(opcion);
}

function pulsarJustificanteEntrada(/*opcion*/){
	// Creemos que el parámetro opcion no se usa, por lo que comentamos todo lo referente a él
    /*if(opcion){
        recuperarCodigoOfiReg(opcion);
    } else {*/
       if('E' === tipoActual && !document.forms[0].listaAnotaciones){
          // if(!mostrarDigitalizar){
            //    mostrarMensaje("1");
           //}else{
                generarInforme();
          // }
        }else if('E'===tipoActual && document.forms[0].listaAnotaciones){
            //comprobamos que todas las anotaciones seleccionadas se encuentran finalizadas para imprimir el justificante
            var digitalizacionFinalizada = true;
           // var listaAnotaciones = document.forms[0].listaAnotaciones.value;
            var separador = '§¥';
            var listaDatosAnotaciones = new Array();
            var anotacionesSeleccionadas = document.forms[0].listaAnotaciones.value.split(separador);
            for(i=0; i<anotacionesSeleccionadas.length ; i++){
                if(anotacionesSeleccionadas[i]!=''){
                    listaDatosAnotaciones[i] = anotacionesSeleccionadas[i].split(';'); 
                    if(listaDatosAnotaciones[i][3]=='0'){
                        digitalizacionFinalizada = false;
                        break;
                    }
                }
            }
            if(!mostrarDigitalizar || digitalizacionFinalizada){
                generarInforme();
            }else{
                  mostrarError("1");
            }

        } else{
            generarInforme();
        }
    //}
    
 }

function mostrarGenerarModelo(tipo){
    var mostrarGenerarModelo = document.forms[0].mostrarGenerarModelo.value;   
    if(tipo=="S" && mostrarGenerarModelo=="si"){
        $("#filaModelo").show();
    } else   $("#filaModelo").hide();
}

function recuperarCodigoOfiReg(tipo,origen){
    var url = APP_CONTEXT_PATH+"/MantAnotacionRegistro.do?";
    var ejerc = $("input[name=ano]").val();
    if(!ejerc) ejerc = $('#ejercicioAnotacion').val();
    var numero = $("input[name=numero]").val();
    var tipoAnotacion;
    
    if(typeof tipoActual==="undefined") tipoAnotacion=document.forms[0].tipo.value;
    else tipoAnotacion = tipoActual;
    
    $.ajax({
        url: url,
        type: 'POST',
        async: true,
         data: {'opcion':'obtenerOficinaRegistro','tipoRegistro':tipoAnotacion,'ejercicio':ejerc,'numeroAnotacion':numero,'tipoInforme':tipo,'origen':origen},
        success: procesarRespuestaRecuperarCodigo,
        error: muestraErrorRespuestaRecuperarCodigo
    });
}

function procesarRespuestaRecuperarCodigo(result){
    var datos = JSON.parse(result);
    datos = datos.tabla;
    
   var tipoInforme = datos.tipoInforme;	
    var codOfiReg = datos.oficina;	    	
    if(codOfiReg!="SINOFI"){	
        if(codOfiReg!="VARIAS"){	
            generarInforme(tipoInforme,datos);	
            //TODO Etiquetas
        } else alert("Los registros tienen distintas oficinas");	
    } else alert("No hay oficina de registro para la/s anotacion/es");
}

function muestraErrorRespuestaRecuperarCodigo(){alert("Error al recuperar la oficina de registro");}

function generarInforme(tipo,datosParam){
    var parametros;
    var aplicacion;	
    var idioma;	
    if(tipo) {	
        parametros = "opcion=justificanteEntrada&tipoJustificante=" + tipo + "&codOfiReg=" + datosParam.oficina	
                + "&ejercicioAnotacion=" + datosParam.ejercicioAnotacion + "&tipoAnotacion=" + datosParam.tipoAnotacion;	
        aplicacion = datosParam.codApp;	
        idioma = datosParam.idioma;
    } else if(!document.forms[0].listaAnotaciones){
        parametros = "opcion=justificanteEntrada";
        aplicacion = aplic;
        idioma = idiom;
    } else {
        parametros = "opcion=justificanteEntradaDesdeConsulta";
        aplicacion = aplic;
        idioma = idiom;

    }

    // Justificnate SIR
    if($('[name="codigoUnidadDestinoSIRHidden"]') != undefined){
        $('[name="codigoUnidadDestinoSIRHidden"]').val($('[name="codigoUnidadDestinoSIR"]').val());
        $('[name="listaCodTerceroSIRHidden"]').val($('[name="listaCodTercero"]').val());
        $('[name="listaVersionTerceroSIRHidden"]').val($('[name="listaVersionTercero"]').val());
        $('[name="listaCodDomicilioSIRHidden"]').val($('[name="listaCodDomicilio"]').val());
    }

    
    pleaseWait('on');
    document.forms[0].codAplicacion.value = aplicacion;
    document.forms[0].idiomaCuneus.value = idioma;
    document.forms[0].target="oculto";
    document.forms[0].action= APP_CONTEXT_PATH+"/MantAnotacionRegistro.do?" + parametros;
    document.forms[0].submit();
}

 // Carga la ventana de terceros con el tercero pasado
 function terceroBuscado(tercero) { //oculto

     var argumentos = new Array();
     argumentos['modo'] = 'seleccion';
     argumentos['terceros'] = tercero;
     argumentos['domicilio'] = document.forms[0].codDomTerc.value;

     var source = APP_CONTEXT_PATH+'/BusquedaTerceros.do?opcion=inicializar&ventana=true';
     abrirXanelaAuxiliar(APP_CONTEXT_PATH+'/jsp/terceros/mainVentana.jsp?source='+source,argumentos,
            'width=990,height=750,status=' + '<%=statusBar%>',function(datos){
                tratarTerceroDevuelto(datos);
            });
}

function onFocus_CodProcedimiento() {
    var cod = document.forms[0].cod_procedimiento.value;
    if (cod != cod_procedimiento_anterior && !consultando) {

         // Si no hay terceros ni documentos no hace falta confirmacion
        if (terceros.length == 0 && listaDoc.length == 0) {
            confirmarCargarProcedimiento('confirmado');
        } else {
            // Buscar el municipio del procedimiento
            for(i=0; i<cod_procedimientos.length; i++) {
                if (cod == cod_procedimientos[i])
                    document.forms[0].mun_procedimiento.value = mun_procedimientos[i];
            }
            document.forms[0].action=APP_CONTEXT_PATH+"/MantAnotacionRegistro.do";
            document.forms[0].opcion.value="comprobarCargarProcedimiento";
            document.forms[0].target="oculto";
            document.forms[0].submit();
        }
    }
}

function mostrarListaDomicilios(Terceros) { //oculto
    pleaseWait('off');
    // Abrimos dialogo
    var argumentos = new Array();
    domiciliosAux = Terceros[0][18];
    argumentos['domicilios'] = domiciliosAux;
    var source = APP_CONTEXT_PATH+"/jsp/terceros/listaDomicilios.jsp?opcion=";
    
     abrirXanelaAuxiliar(APP_CONTEXT_PATH+'/jsp/terceros/mainVentana.jsp?source='+source,argumentos,
            'width=800,height=500,status=' + '<%=statusBar%>',function(codigoNuevoDom){
                if (codigoNuevoDom!=undefined) {
                  for(var i = 0; i<domiciliosAux.length; i++) {
                        if (domiciliosAux[i][5] == codigoNuevoDom) {
                            var tercero = formatearArrayTercero(Terceros[0], i);
                            insertarTerceroPorDefecto(tercero);
                            mostrarInteresado(terceroActual);
                        }
                    }
                }
            });

}


function mostrarDatosTercero() {
         // Si no hay terceros se borran los campos del formulario
         if (terceros.length < 1) {
             document.forms[0].codTerc.value = "-2";
             document.forms[0].codDomTerc.value = "";
             document.forms[0].txtInteresado.value=""; // Razon Social.
             document.forms[0].txtTelefono.value=""; // Telefono.
             document.forms[0].txtCorreo.value=""; // Email.
             document.forms[0].txtPais.value=""; // Pais.
             document.forms[0].txtProv.value=""; // Provincia.
             document.forms[0].txtMuni.value=""; // Municipio.
             document.forms[0].txtDomicilio.value=""; // Nombre vía.z
             document.forms[0].txtCP.value=""; // Codigo.
             document.forms[0].cbTipoDoc.value=""; // codDNI.
             document.forms[0].txtDNI.value=""; // dni.
             comboRol.buscaCodigo(rolPorDefecto); // cod rol
             mostrarDescripcionTipoDoc();
             terceroActual = -1;
             habilitarTipoDocTerceroYDoc();

         } else { // Hay al menos un tercero

             // Mostramos el primer tercero con rol por defecto o
             // el indicado por la variable 'terceroCargado', si no es nula
             var porDefecto=0;
             var i=0;
             if (terceroCargado == '') {
                 while (i<terceros.length) {
                     if(terceros[i][6]=="SI") {
                         porDefecto=i;
                         break;
                     }
                     i++;
                 }
             } else {
                 while (i<terceros.length) {
                     if(terceros[i][0]==terceroCargado) {
                         porDefecto=i;
                         terceroCargado = '';
                         break;
                     }
                     i++;
                 }
             }

             // relleno los campos del formulario de la pestaña interesados
             document.forms[0].codTerc.value      =terceros[porDefecto][0];
             document.forms[0].codDomTerc.value   =terceros[porDefecto][4];
             document.forms[0].numModifTerc.value =1;
             document.forms[0].txtInteresado.value=terceros[porDefecto][2]; // Razon Social.
             document.forms[0].txtTelefono.value  =terceros[porDefecto][8]; // Telefono.
             document.forms[0].txtCorreo.value    =terceros[porDefecto][9]; // Email.
             document.forms[0].txtPais.value      =terceros[porDefecto][10]; // Pais.
             document.forms[0].txtProv.value      =terceros[porDefecto][11]; // Provincia.
             document.forms[0].txtMuni.value      =terceros[porDefecto][12]; // Municipio.
             document.forms[0].txtDomicilio.value =terceros[porDefecto][5]; // Nombre vía.z
             document.forms[0].txtCP.value        =terceros[porDefecto][13]; // Codigo.
             document.forms[0].cbTipoDoc.value    =terceros[porDefecto][14]; // codDNI.
             document.forms[0].txtDNI.value       =terceros[porDefecto][15]; // dni.

             // Cambiar el tercero antes de modificar el combo (se produce onchange)
             terceroActual = porDefecto;
             var codRol = terceros[porDefecto][7];
             if (codRol > ROL_TER_ANULAR_RES) {
                 comboRol.buscaCodigo(codRol);      // rol
             }
             mostrarDescripcionTipoDoc();
             deshabilitarTipoDocTerceroYDoc();
         }
         actualizarBotonesTercero();
         pleaseWait('off');
     }
 
	  
 function determinarAnotacion(i){ 
     mostrarMensajeRegistroTelematico('');
     document.forms[0].posicionAnotacion.value=i;
     document.forms[0].opcion.value="determinar_anotacion";
     document.forms[0].target="oculto";
     document.forms[0].action=APP_CONTEXT_PATH+"/MantAnotacionRegistro.do";
     document.forms[0].submit();
 }

 function determinarEncontrada() { 
     document.getElementById("capaBotonesConsulta").style.visibility = 'hidden';
     document.forms[0].opcion.value="determinarEncontrada";
     document.forms[0].target="oculto";
     document.forms[0].action=APP_CONTEXT_PATH+"/MantAnotacionRegistro.do";
     document.forms[0].submit();
 }
 
 
function mostrarMensajeRegistroTelematico(msg){    
    document.getElementById("divRegistroTelematico").innerHTML = msg;
}

function cumpleTipoDocumentoObligatorio(){
    var esTipoDocOblig = $('#asuntoConTipoDocOblig').val();
    var hayIntSinDoc = 0;
    
    for(var j=0;j<terceros.length;j++){
        if(terceros[j][14]==0){
            hayIntSinDoc = 1;
            break;
        }
    }
    
    if(esTipoDocOblig=='true' && hayIntSinDoc==1) return false;
    return true;      
}

// Añadida en #230158; Modificada en #240292
function comprobarCodProcedimientoValido(opcion){
    var codigo = document.forms[0].cod_procedimiento.value;
    var descripcion = document.forms[0].desc_procedimiento.value;
    
    comprobarFormatoNombreInteresado(function(continuar1){
        if (continuar1){
            comprobarCod(codigo,descripcion,function(continuar){
                if(continuar){
           
                        if(finDigitalizar!= null && opcion=='Mod'){
                            document.forms[0].finDigitalizacion.value = finDigitalizar;
                        }else if(opcion=='Alta' || opcion =='Dup'){
                            document.forms[0].finDigitalizacion.value = false;
                            if(document.forms[0].tipoRegistro != undefined && document.forms[0].tipoRegistro.value=="S" && document.forms[0].cbTipoEntrada.value==1){
                                document.forms[0].finDigitalizacion.value = true;
                            }
                        }

                        if(opcion=="Alta") pulsarRegistrarAlta();
                        if(opcion=='AltaFinDigitalizar') pulsarRegistrarAlta();
                        if(opcion=="Mod") pulsarRegistrarModificar();
                        if(opcion=='ModFinDigitalizar') pulsarRegistrarModificar();
                        if(opcion=="Dup") pulsarRegistrarDuplicar();
                        if(opcion=="DupFinDigitalizar") pulsarRegistrarDuplicar();
                    }
                });
    
        } else {
             jsp_alerta('A','Formato inválido. Un interesado con tipo de documento NIF debe de tener al menos el primer apellido');
        }
    });

    
    function comprobarCod(codigo,descripcion,callback){
        pleaseWait('on');
        var exito = false;

        if(codigo==""){
            exito = true;
            if(descripcion=="") callback(true);
        } else {
            if(descripcion=="" || (descripcion!="" && cod_procedimientos.length==0)) {
                // Para que no se despliege el combo al hacer la comprobacion ponemos divSegundoPlano=true
                var divSegundoPlanoAnterior=divSegundoPlano;
                divSegundoPlano=true;
                
                primera_carga = false;
                mostrarListaProcedimientos();
            }
            setTimeout(function(){
                // Para evitar problemas se vuelve a asignar dar el valor que ten?a anteriormente.	
                divSegundoPlano=divSegundoPlanoAnterior;
                for(var i=0; i<cod_procedimientos.length; i++){
                    if(codigo==cod_procedimientos[i]){
                        break;
                    }
                }
                if(desc_procedimientos[i]==descripcion) exito=true;
                else {
                    pleaseWait('off');
                    if(i>=cod_procedimientos.length) jsp_alerta('A','Código inexistente');
                    exito = false;
                }
                callback(exito);
            },2000);
        }
    }
    
    
     /**
 * 
 * @returns true si cumple el formato (si el tipo de documento es nif, debe de tener un nombre y un apellido1)
 */    

function comprobarFormatoNombreInteresado (callback) {
    
    var codigoTercero = document.forms[0].codTerc.value;
    var tipoDocumento = $('input[name="cbTipoDoc"]').val();
    var formatoValido = true;
 
    if (tipoDocumento == 1 || tipoDocumento == 3) {
        try {
            $.ajax({
                url: APP_CONTEXT_PATH + "/MantAnotacionRegistro.do",
                type: "GET",
                async: false,
                data: {
                    "opcion": "comprobarFormatoInteresado",
                    "codigoTercero": codigoTercero
                },
                dataType:'json',
                success: function(data) { 
                   formatoValido  = data ? true : false;
                }
            });
        } catch (Err) {
            console.log("Se ha producido un error validando el formato del interesado.");
            console.log(Err);
        }
    }
    
    callback(formatoValido);
}
}


// #230158: detecta un cambio en el codigo de uor y carga los procedimientos de la nueva uor
function registrarCambioUor(){
    var clase = $('input[name=cod_uor]').prop('class').toString();
    if(clase.indexOf('Deshabilitado')==-1 && document.forms[0].cod_uor_anterior.value != document.forms[0].cod_uor.value){
        onchangeCod_uniRegDestinoORD();
        mostrarListaProcedimientos();
        ocultarDiv();
        document.forms[0].cod_uor_anterior.value = document.forms[0].cod_uor.value;
    }
}

function comprobarSiJustificanteCSV(nombreFichero) {

    var PATRON_NOMBRE_DOC_JUSTIFICANTES_CSV = /^JE_[0-9]{17}.pdf$/;    	
    return PATRON_NOMBRE_DOC_JUSTIFICANTES_CSV.test(nombreFichero);
	
}

function pasarFocoACod(){

    $('input[name=cbTipoEntrada]').focus();

}

function pasarFocoADesc(){
       $('input[name=txtNomeTipoEntrada]').focus();

}


//bloquea de unidad de destino seg?n asunto	
function bloquearUnidadDestino(bloquearDestino){

    // codigo de PRE
    if(bloquearDestino=="true" | bloquearDestino){
        var bloquearDatos = new Array(document.forms[0].cod_uor);	
        deshabilitarGeneral(bloquearDatos);	
        var vectorBoton1 =  new Array(document.getElementsByName('anchorUnidadeRexistroORD')[0]);	
        deshabilitarIconos(vectorBoton1, true);	
        $("span[name='botonUnidadeRexistroORD']" ).addClass( "faDeshabilitado" );	
        $("input[name='desc_uniRegDestinoORD']" ).addClass( "inputTextoDeshabilitado");	
        $("input[name='desc_uniRegDestinoORD']" ).prop( 'readonly',true);	
    }
}


//bloquea de unidad de procedimiento según el asunto	
function bloquearUnidadProcedimiento(bloquearProcedimiento){
    if(bloquearProcedimiento=="true" | bloquearProcedimiento){	
        // ponemos a readOnly el campo del código del procedimiento
        document.getElementsByName('cod_procedimiento')[0].readOnly = true;
        // eliminamos la función de al clickar en el campo de la descripción del procedimiento
        document.getElementsByName('desc_procedimiento')[0].setAttribute("onclick", "");
         $("input[name='desc_procedimiento']").attr("readonly",true)
        // deshabilitamos el botón selector
        var vectorBoton1 = new Array(document.getElementsByName('anchorProcedimiento')[0]);
        deshabilitarIconos(vectorBoton1, true);
        $("span[name='botonProcedimiento']").addClass("faDeshabilitado");
        document.getElementsByName('anchorProcedimiento')[0].style.cursor = 'default';

    }
}

function comprobarEntradaRelacionada(valInput, msjError){
    var anoValue = document.forms[0].ano.value;
    var numValue = document.forms[0].numero.value;

    // Diferenciamos cuando se  debe validar el anyo de entrada relacionada y cuando no.
    // No: en la pantalla de busqueda podemos buscar por cualquier anyo de entrada relacionada
    // En los demas casos hacemos la validacion
    if((anoValue==undefined || anoValue=='') || (numValue!=undefined && numValue!='') || responderASalida=="S"){
        var fecha = new Date();
        var anoActual = fecha.getFullYear();

        valInput = parseInt(valInput);
        anoActual = parseInt(anoActual);

        if(valInput > anoActual || valInput < (anoActual - 5)){
            jsp_alerta("A", msjError);
            var elemento = $("input[name='txtExp1Orixe']");
            elemento.val("");
            elemento.focus();
            return false;
        }
    }   
    
}

function enviarPeticionGenerarJustificante(url,datos,tipoJustif){
    if (tipoJustif!=="ODT"){
        document.forms[0].codAplicacion.value =datos.codAplicacion;
        document.forms[0].descripcionJus.value =datos.descripcionJus;
        document.forms[0].aplicacion.value =datos.aplicacion;
        document.forms[0].editorJustif.value =datos.editorJustif;
        document.forms[0].idiomaCuneus.value = datos.idiomaCuneus;
        document.forms[0].target="oculto";
        document.forms[0].action=url;
        document.forms[0].submit();
    } else {
        pleaseWait('on');
        try{
            $.ajax({
                url:  url,
                type: 'POST',
                async: true,
                data: datos,
                success: procesarRespuestaGenerarJustificante,
                error: mostrarErrorRespuestaGenerarJustificante
            });           
        }catch(Err){
            pleaseWait('off');
            mostrarErrorRespuestaJustificante(2);
        }
    }
    
    function procesarRespuestaGenerarJustificante(ajaxResult){
        pleaseWait('off');
        if(ajaxResult){
            var datos = JSON.parse(ajaxResult);
            if(datos.status !== 0){
                mostrarErrorRespuestaJustificante(datos.status);
            } else {
               var rutaFichero = decodeURIComponent(datos.resultado);
                var url = APP_CONTEXT_PATH + "/flexia/JustificanteRegistroPDF.do?fichero=" + rutaFichero + "&sinBorrado=on";                    
                window.open(url,'ventana',"left=10, top=10, width=610, height=800, scrollbars=no, menubar=no, location=no, resizable=yes");
            }
        } else {
            mostrarErrorRespuestaJustificante(2);
        }
    }
    
    function  mostrarErrorRespuestaGenerarJustificante(){
        mostrarErrorRespuestaJustificante(2);
    }
}

function gestionarVisibilidadDigitalizacion(opcion,estado){
    if(opcion=='disabled'){
      
        var botonesDigitalizar = [document.forms[0].cmdDigitalizarDesdeConsulta];
        if(estado==true){
            deshabilitarGeneral(botonesDigitalizar);
        } else {
            habilitarGeneral(botonesDigitalizar);
        }
    } else if(opcion=='mostrar'){
        if(top.menu.modificando=='S' && estado == false){
            $("[name='cmdRexistrarModificacion']").show();
            $("[name='cmdFinDigitalizarMod']").show();
        } else if(top.menu.modificando=='S' && estado == true){
            $("[name='cmdRexistrarModificacion']").show();
            $("[name='cmdFinDigitalizarMod']").hide();
            
        }
        $("[name='cmdDigitalizarDesdeConsulta']").show();
        $("[name='cmdDigitalizarDesdeAlta']").show();
             
    } else if(opcion=='ocultar'){
        if(top.menu.modificando=='S'){
            $("[name='cmdRexistrarModificacion']").show();
            $("[name='cmdFinDigitalizarMod']").hide();
        }
        $("[name='cmdDigitalizarDesdeConsulta']").hide();
        $("[name='cmdDigitalizarDesdeAlta']").hide();
             
    } else return;
}

// Envía el registro a la oficina asignada a través del CIR
function pulsarEnviar(contexto) {
    var numeroRegistro = $("#numeroRegistro").val();
    var anoEjercicio = $("#anoEjercicio").val();

    try {
	console.log("Contexto " + contexto);
	$.ajax({
	    url: contexto + "/MantAnotacionRegistro.do",
	    type: "POST",
	    async: true,
	    data: {
		"opcion": "enviarRegistro",
		"numeroRegistro": numeroRegistro,
		"anoEjercicio": anoEjercicio
	    },
	    success: envioRegistroCorrecto,
	    error: envioRegistroFallido
	});
    } catch (Err) {
	// TODO
	console.log("bad things happened");
	console.log(Err);
    }
}

// Inicializa los arrays de procedimiento en caso de que dispongamos de un unico procedimiento
function inicializaComprobarDigitalizacionProcedimiento(codProcedimiento, descProcedimiento, digitProcedimiento, munProcedimiento){
    cod_procedimientos = [codProcedimiento];
    desc_procedimientos = [descProcedimiento];
    mun_procedimientos = [munProcedimiento];
    digit_procedimientos = [digitProcedimiento];
}

function comprobarDigitalizacionProcedimiento(listadoDigit,procedimiento){
    
    if(mostrarDigitalizar){
        var procDigitalizacion = listadoDigit[procedimiento]; 
        if(!consultando && uorPermiteDigitalizacion && procDigitalizacion === "SI"){ 
            if (finDigitalizar == null) {
                finDigitalizar = document.forms[0].finDigitalizacion.value;
            }
             $("[name='cmdDigitalizarDesdeAlta']").show();
             if(top.menu.modificando=='S' && !finDigitalizar){
                $("[name='cmdFinDigitalizarMod']").hide();
             }
             $("[name='cmdRegistrarAlta']").show();
             $("[name='cmdRegistrarDuplicar']").show(); 
        } else {
             $("[name='cmdDigitalizarDesdeAlta']").hide();
             $("[name='cmdRegistrarAlta']").hide();
             $("[name='cmdRegistrarDuplicar']").hide();
             
        }
        
        actualizarBotoneraGuardarModificar(listadoDigit, procDigitalizacion);
        
    } 
   
}


function envioRegistroFallido() {
    jsp_alerta("A", "Ha habido un error al enviar el registro.");
}

function actualizarBotoneraGuardarModificar(listadoDigit, procDigitalizacion){ 
	 digitalizacionFinalizada = document.forms[0].finDigitalizacion.value;

	if(finDigitalizar==null){
		finDigitalizar = document.forms[0].finDigitalizacion.value;
	 }
	 var pro_anterior_digit='NO';
	//al realizar una modificación se comprueba si se pasa de un procedimiento digitalizable a no digitalizable
	if(top.menu.modificando=='S'){
		for(i=0; i<cod_procedimientos.length; i++){
			if(cod_procedimientos[i]==cod_procedimiento_anterior){
				pro_anterior_digit=listadoDigit[i];
				break
			}
		}
	}

	//se controla el cambio de procedimiento en el combo de digitalizable a no digitalizable vice..
	if(procDigitalizacion === 'SI' && pro_anterior_digit ==='NO'){
		if(finDigitalizar=='true'){ 
			finDigitalizar = 'false';
			 $("[name='cmdFinDigitalizarMod']").show();
		 }
	 }else if(procDigitalizacion === 'SI' && pro_anterior_digit ==='SI'){
		 if(finDigitalizar=='false' || digitalizacionFinalizada =='false'){
			 finDigitalizar = 'false';
			 $("[name='cmdFinDigitalizarMod']").show();
		 }   
	} else {
		$("[name='cmdFinDigitalizarMod']").hide();
		finDigitalizar= 'true'; 
	}        
}


function mostrarFiltrosProcOtraAdmin() {
    var tipo = document.forms[0].cbTipoEntrada ? document.forms[0].cbTipoEntrada.value : "";
    var div = document.getElementById("filtrosProcedentesOtraAdmin");
    if (div) {
        if (tipo === "2") {
            div.style.display = "";
        } else {
            div.style.display = "none";
        }
    }
}

