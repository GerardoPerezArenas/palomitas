function navegacionConsultaDesactivada() {
    return enlacesPaginacion(litMosPagDePags,litAnterior,litSiguiente,tramiteActual,numeroTramites,null);    
}

function navegacion() {
    return enlacesPaginacion(litMosPagDePags,litAnterior,litSiguiente,tramiteActual,numeroTramites,'enlaceTramite');
}

function tramiteEnNavegacion(actual, total){
    tramiteActual = actual;
    numeroTramites = total;
    domlay('capaNavegacion',1,0,0,navegacion());
}

function cargarCombos(){
    tabAux = tabDoc;
    listaAux = listaDoc;
    comboClasifTramite.addItems(cod_clasifTramite,desc_clasifTramite);
    comboExpRel.addItems(cod_expRel,desc_expRel);
    var menos1 = new Array();
    var vacia = new Array();
    menos1[0] = -1;
    vacia[0] = "";
    comboAvanzar.addItems(menos1.concat(cod_webServices),vacia.concat(desc_webServices));
    comboRetroceder.addItems(menos1.concat(cod_webServices),vacia.concat(desc_webServices));
    comboIniciar.addItems(menos1.concat(cod_webServices),vacia.concat(desc_webServices));
    limpiarCombosWS();	

    comboCondicionTabla.addItems(condicionTabla,condicionTabla);
  
    comboEstadoEnlace.addItems(estado_enlaces,estado_enlaces);

    comboNotCercaFinPlazo.addItems(codNotificacionTramite,descNotificacionTramite);
    comboNotFueraDePlazo.addItems(codNotificacionTramite,descNotificacionTramite);
    comboAdmiteNotifElect.addItems(codigo_tipoNotificacionElectronica,nombre_tipoNotificacionElectronica);
  
    activarSelectNotCercaFinPlazo();
    activarSelectNotFueraDePlazo();
    activarAdmiteNotifElect();

}

function comprobarConfiguracionSW(){

    var codigoTramite = document.forms[0].codigoTramite.value;
    if(codigoTramite==null || codigoTramite==''){
      
        comboAvanzar.deactivate();
        comboRetroceder.deactivate();
        comboIniciar.deactivate();
        comboTipoRetroceder.deactivate();
        var botonesCapaUno = [document.forms[0].cmdAvanConfSW,document.forms[0].cmdRetConfSW,document.forms[0].cmdAltaConfSW,document.forms[0].cmdGrabarConfSW,document.forms[0].cmdEliminarConfSW,document.forms[0].cmdLimpiarrConfSW];
        deshabilitarGeneral(botonesCapaUno);
      
    }
}

function setPopUpFirmaPlantillaResult( newDocCod, newFirmaValue ) { 
    if (newDocCod) {
        for (var pfprCount = 0; pfprCount < listaDoc.length; pfprCount++) {
            if (listaDoc[pfprCount][0] == newDocCod) 
                listaDoc[pfprCount][3] = getEstadoFirmaVisual(newDocCod,newFirmaValue);
            if (listaDocOriginal[pfprCount][0] == newDocCod) 
                listaDocOriginal[pfprCount][6] = newFirmaValue;
        }
        tabDoc.lineas=listaDoc;
        refrescaDoc();
    }
}

function borrarDatos() {
    document.forms[0].codigoInternoTramite.value = "";
    document.forms[0].numeroTramite.value = "";
    document.forms[0].nombreTramite.value = "";
    document.forms[0].ocurrencias.value = "";
    document.forms[0].codClasifTramite.value = "";
    document.forms[0].descClasifTramite.value = "";
    document.forms[0].codExpRel.value = "";
    document.forms[0].descExpRel.value = "";
    document.forms[0].plazo.value = "";
    document.forms[0].unidadesPlazo[0].checked= false;
    document.forms[0].unidadesPlazo[1].checked= false;
    document.forms[0].unidadesPlazo[2].checked= false;
    document.forms[0].radioUnidadInicio[0].checked= false;
    document.forms[0].radioUnidadInicio[1].checked= false;
    document.forms[0].radioUnidadInicio[2].checked= false;
    document.forms[0].radioUnidadInicio[3].checked= false;
    document.forms[0].codUnidadTramite[0].checked= false;
    document.forms[0].codUnidadTramite[1].checked= false;
    document.forms[0].codUnidadTramite[2].checked= false;
    document.forms[0].codUnidadTramite[3].checked= false;
    document.forms[0].codUnidadTramite[4].checked= false;
    listaUnidadesTramitadoras = new Array();
    mostrarListaUnidadTramite();
    document.forms[0].disponible.checked = false;
    document.forms[0].tramiteInicio.checked = false;
    document.forms[0].codUnidadInicio.value = "";
    document.forms[0].codVisibleUnidadInicio.value = "";
    document.forms[0].descUnidadInicio.value = "";
    document.forms[0].codCargo.value = "";
    document.forms[0].cod_visible_cargo.value = "";
    document.forms[0].descCargo.value = "";
    document.forms[0].texto.value = "";
    document.forms[0].tramiteInicio.value = "";
    document.forms[0].soloEsta.checked = false;
    document.forms[0].soloEsta.value = "";

    //Informacion
    document.forms[0].instrucciones.value="";
    
    // Borrar datos de plazos.
    onClickUnidadesPlazo();
    document.forms[0].plazoFin.disabled = true;
    document.forms[0].generarPlazos.disabled = true;
	
    comboAdmiteNotifElect.selectItem(-1);
    document.forms[0].admiteNotificacionElectronica.checked = false;
    var arrayNEO = document.getElementsByName("notificacionElectronicaObligatoria");
    for (k =0; k < arrayNEO.length; k++){
        if (arrayNEO[k]){
            arrayNEO[k].checked = false;
            arrayNEO[k].disabled=true;
        
        }
    }
    document.forms[0].firma.value= '';	
    document.forms[0].codigoTipoNotificacionElectronica.value='';
    document.forms[0].tipoUsuarioFirma.value='';
    document.forms[0].codigoOtroUsuarioFirma.value='';
    document.forms[0].nombreOtroUsuarioFirma.value='';
    document.forms[0].firma.value= '';

    tipoUsuarioFirma='';
    codigoOtroUsuarioFirma='';
    nombreOtroUsuarioFirma='';
   
}

function activarBotonera(){
    var botones = new Array();
    document.forms[0].cmdAlta.disabled = false;
    document.forms[0].cmdAlta.cursor = 'hand';
    var botones = document.getElementsByName('cmdAlta');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }


    document.forms[0].cmdModificar.disabled = false;
    document.forms[0].cmdModificar.cursor = 'hand';
    var botones = document.getElementsByName('cmdModificar');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }


    document.forms[0].cmdEliminarTram.disabled = false;
    document.forms[0].cmdEliminarTram.cursor = 'hand';
    var botones = document.getElementsByName('cmdEliminarTram');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonLargo';
    }


    document.forms[0].cmdDefProc.disabled = false;
    document.forms[0].cmdDefProc.cursor = 'hand';
    var botones = document.getElementsByName('cmdDefProc');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonLargo';
    }

    document.forms[0].cmdGrabarAlta.disabled = false;
    document.forms[0].cmdGrabarAlta.cursor = 'hand';
    var botones = document.getElementsByName('cmdGrabarAlta');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }


    document.forms[0].cmdCancelarAlta.disabled = false;
    document.forms[0].cmdCancelarAlta.cursor = 'hand';
    var botones = document.getElementsByName('cmdCancelarAlta');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    
    document.forms[0].cmdListaTramite.disabled = false;
    document.forms[0].cmdListaTramite.cursor = 'hand';
    var botones = document.getElementsByName('cmdListaTramite');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }


    document.forms[0].cmdListaTramiteSiFavorable.disabled = false;
    document.forms[0].cmdListaTramiteSiFavorable.cursor = 'hand';
    var botones = document.getElementsByName('cmdListaTramiteSiFavorable');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdListaTramiteNoFavorable.disabled = false;
    document.forms[0].cmdListaTramiteNoFavorable.cursor = 'hand';
    var botones = document.getElementsByName('cmdListaTramiteNoFavorable');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

}

function desactivarTablas() {
    if(document.forms[0].cmdAltaDoc.disabled == true) {
        tabDoc.readOnly = true;
    }
    if(document.forms[0].cmdAltaTablaEntrada.disabled == true) {
        tab.readOnly = true;
    }
    if(document.forms[0].cmdAltaEnlaces.disabled == true) {
        tabEnlaces.readOnly = true;
    }
    if(document.forms[0].cmdAltaConfSW.disabled == true) {
        tabConfSW.readOnly = true;
    }
  
}

function activarTablas() {
    if(document.forms[0].cmdAltaDoc.disabled == false) {
        tabDoc.readOnly = false;
    }
    if(document.forms[0].cmdAltaTablaEntrada.disabled == false) {
        tab.readOnly = false;
    }
    if(document.forms[0].cmdAltaEnlaces.disabled == false) {
        tabEnlaces.readOnly = false;
    }
    if(document.forms[0].cmdAltaCampo.disabled == false) {
        tabCampos.readOnly = false;
    }
    if(document.forms[0].cmdAltaConfSW.disabled == false) {
        tabConfSW.readOnly = false;
    }  
}

function actualizarDescripcion(campoCodigo, campoDescripcion, listaCodigos, listaDescripciones) {
    cargarDesc(campoCodigo,campoDescripcion, listaCodigos, listaDescripciones);
}

function insercionRealizada(posicion,cT,deIns) {
    document.forms[0].codigoTramite.value = cT;
    document.forms[0].codigoInternoTramite.value = cT;
    deInsercion = deIns;
    var vector = new Array(document.forms[0].txtCodigo,document.forms[0].txtDescripcion);
    deshabilitarGeneral(vector);
    document.forms[0].cmdEliminarTram.disabled = false;
    if(document.forms[0].tipoCondicion[1].checked == true) {
        document.forms[0].cmdListaTramite.disabled = false;
    }
    if(document.forms[0].tipoFavorableSI[0].checked == true) {
        document.forms[0].cmdListaTramiteSiFavorable.disabled = false;
    }
    if(document.forms[0].tipoFavorableNO[0].checked == true) {
        document.forms[0].cmdListaTramiteNoFavorable.disabled = false;
    }

    desactivarFormulario();
    enlaceTramite(numeroTramites+1);
    mostrarCapasBotones('capaBotones1');
}

function actualizacionRealizada() { 
    window.focus();
    desactivarFormulario();
    domlay('capaNavegacion',0,0,0,' ');
    domlay('capaNavegacion',1,0,0,navegacion());
    mostrarCapasBotones('capaBotones1');
    document.forms[0].cmdAlta.disabled = false;
    document.forms[0].cmdModificar.disabled = false;
    document.forms[0].cmdEliminarTram.disabled = false;
    document.forms[0].cmdDefProc.disabled = false;
 
    cargaDocumentos();
    pintaTramiteActual();    
}

function limpiarCondicionesSalida() {
    if(document.forms[0].tipoCondicion[0].checked || document.forms[0].tipoCondicion[1].checked
        || document.forms[0].tipoCondicion[2].checked) {
        document.forms[0].texto.value = "";
        document.forms[0].tipoFavorableSI[0].checked = false;
        document.forms[0].tipoFavorableSI[1].checked = false;
        document.forms[0].tipoFavorableNO[0].checked = false;
        document.forms[0].tipoFavorableNO[1].checked = false;
        document.forms[0].listaCodTramitesFlujoSalidaSiFavorable.value == "";
        document.forms[0].listaNombreTramitesFlujoSalidaSiFavorable.value = "";
        document.forms[0].listaNumerosSecuenciaFlujoSalidaSiFavorable.value = "";
        document.forms[0].listaDescTramitesFlujoSalidaSiFavorable.value = "";
        document.forms[0].obligSiFavorable.value = "";
        document.forms[0].numeroCondicionSalidaSiFavorable.value = "";
        document.forms[0].listaCodTramitesFlujoSalidaNoFavorable.value == "";
        document.forms[0].listaNombreTramitesFlujoSalidaNoFavorable.value = "";
        document.forms[0].listaNumerosSecuenciaFlujoSalidaNoFavorable.value = "";
        document.forms[0].listaDescTramitesFlujoSalidaNoFavorable.value = "";
        document.forms[0].obligNoFavorable.value = "";
        document.forms[0].numeroCondicionSalidaNoFavorable.value = "";
        if (document.forms[0].tipoCondicion[0].checked || document.forms[0].tipoCondicion[2].checked) {
            document.forms[0].listaCodTramitesFlujoSalida.value == "";
            document.forms[0].listaNombreTramitesFlujoSalida.value = "";
            document.forms[0].listaNumerosSecuenciaFlujoSalida.value = "";
            document.forms[0].listaDescTramitesFlujoSalida.value = "";
            document.forms[0].oblig.value = "";
            document.forms[0].numeroCondicionSalida.value = "";
        }
    }
    if(document.forms[0].tipoFavorableSI[0].checked || (document.forms[0].tipoFavorableNO[0].checked)) {
        document.forms[0].listaCodTramitesFlujoSalida.value == "";
        document.forms[0].listaNombreTramitesFlujoSalida.value = "";
        document.forms[0].listaNumerosSecuenciaFlujoSalida.value = "";
        document.forms[0].listaDescTramitesFlujoSalida.value = "";
        document.forms[0].oblig.value = "";
        document.forms[0].numeroCondicionSalida.value = "";
        if(document.forms[0].tipoFavorableNO[1].checked) {
            document.forms[0].listaCodTramitesFlujoSalidaNoFavorable.value == "";
            document.forms[0].listaNombreTramitesFlujoSalidaNoFavorable.value = "";
            document.forms[0].listaNumerosSecuenciaFlujoSalidaNoFavorable.value = "";
            document.forms[0].listaDescTramitesFlujoSalidaNoFavorable.value = "";
            document.forms[0].obligNoFavorable.value = "";
            document.forms[0].numeroCondicionSalidaNoFavorable.value = "";
        }
        if(document.forms[0].tipoFavorableSI[1].checked) {
            document.forms[0].listaCodTramitesFlujoSalidaSiFavorable.value == "";
            document.forms[0].listaNombreTramitesFlujoSalidaSiFavorable.value = "";
            document.forms[0].listaNumerosSecuenciaFlujoSalidaSiFavorable.value = "";
            document.forms[0].listaDescTramitesFlujoSalidaSiFavorable.value = "";
            document.forms[0].obligSiFavorable.value = "";
            document.forms[0].numeroCondicionSalidaSiFavorable.value = "";
        }
    }
}

function seleccTipoCondicionPregunta(activar) {
    with (document.forms[0]) {
        tipoFavorableSI[0].disabled = !activar;
        tipoFavorableSI[1].disabled = !activar;
        tipoFavorableNO[0].disabled = !activar;
        tipoFavorableNO[1].disabled = !activar;
        tipoFavorableSI[0].checked = activar;
        tipoFavorableNO[0].checked = activar;
        cmdListaTramiteSiFavorable.disabled = !activar;
        cmdListaTramiteNoFavorable.disabled = !activar;
        if (!activar) {
            tipoFavorableSI[1].checked = activar;
            tipoFavorableNO[1].checked = activar;
        }
        }
}

function onFocusPregunta() {      
    if (document.forms[0].texto.value.trim()=='') {
        document.forms[0].cmdListaTramite.disabled = true;
        document.forms[0].tipoCondicion[4].checked = true;
    }
}

function onBlurPregunta() {    
    if (document.forms[0].texto.value.trim()==''){
        document.forms[0].tipoCondicion[0].click();
    }
}

function onChangePregunta() {    
    if (document.forms[0].texto.value.trim()=='')
        seleccTipoCondicionPregunta(false);
    else seleccTipoCondicionPregunta(true);
}

function pulsarAltaExpresionTablaEntrada(sql) {    
    if (sql!="") {
        var lineas = tab.lineas;
        i = lineas.length;
        listaTablaEntrada[i]=["EXPRESION", sql];
        listaTablaEntradaOriginal[i]=["","EXPRESION",0,""," ",i+1];        
        tab.lineas=listaTablaEntrada;
    }
    refresca();
    borrarDatosTablaEntrada();
}

function pulsarModificarExpresionTablaEntrada(sql) {
    if (sql!="") {
        var j = tab.selectedIndex;
        listaTablaEntrada[j]=["EXPRESION", sql];
        listaTablaEntradaOriginal[j]=["","EXPRESION",0,""," ",j+1];        
        tab.lineas=listaTablaEntrada;
    }
    refresca();
    borrarDatosTablaEntrada();
}

function pulsarLimpiarTablaEntrada() {
    borrarDatosTablaEntrada();
    if(tab.selectedIndex != -1 ) {
        tab.selectLinea(tab.selectedIndex);
        tab.selectedIndex = -1;
    }
}

function limpiarInputsEnlaces() {
    borrarDatosEnlaces();
    if(tabEnlaces.selectedIndex != -1 ) {
        tabEnlaces.selectLinea(tabEnlaces.selectedIndex);
        tabEnlaces.selectedIndex = -1;
    }
}

function comprobarObligatoriosEnlaces(){
    if(document.forms[0].txtDescUrl.value == '' || document.forms[0].txtUrl.value == '' || document.forms[0].descEstUrl.value == ''){
        return false;
    }else{
        return true;
    }
}

function comprobarObligatoriosTablaEntrada() {
    if(document.forms[0].descTramiteTabla.value == '' ||
        document.forms[0].descEstadoTramiteTabla.value == '') {
        return false;
    } else {
        return true;
    }
}

function borrarDatosTablaEntrada() {
    comboTramiteTabla.selectItem(-1);
    comboEstadoTramiteTabla.selectItem(-1);
    comboCondicionTabla.selectItem(-1);
    comboTramiteTabla.deactivate();
    comboEstadoTramiteTabla.deactivate();
}

function crearListasTablaEntrada() {
    var listaTiposTabla = "";
    var listaCodTramitesTabla = "";
    var listaTramitesTabla = "";
    var listaEstadosTabla = "";
    var listaExpresionesTabla = "";
    var listaCodCondicion = "";
    var listaCodDocumentos = "";
    
    for (i=0; i < listaTablaEntrada.length; i++) {
        
        if (listaTablaEntradaOriginal[i][0]!="" && listaTablaEntradaOriginal[i][0].trim()!=""){
            listaCodTramitesTabla +=listaTablaEntradaOriginal[i][0]+'§¥';
        } else {
            listaCodTramitesTabla +="_"+'§¥';
        }
        if (listaTablaEntradaOriginal[i][2]!="" && listaTablaEntradaOriginal[i][2].trim()!=""){
            listaTramitesTabla +=listaTablaEntradaOriginal[i][2]+'§¥';
        } else {
            listaTramitesTabla +="_"+'§¥';
        }
        listaTiposTabla +=listaTablaEntrada[i][0]+'§¥';
        if (listaTablaEntradaOriginal[i][4]!="" && listaTablaEntradaOriginal[i][4].trim()!=""){
            listaEstadosTabla +=listaTablaEntradaOriginal[i][4]+'§¥';
        } else {
            listaEstadosTabla +="_"+'§¥';
        }
        
        //if (listaTablaEntradaOriginal[i][6]!="" && listaTablaEntradaOriginal[i][6].trim()!=""){
        if (listaTablaEntradaOriginal[i].length>=7 && listaTablaEntradaOriginal[i][6]!="" && listaTablaEntradaOriginal[i][6].trim()!=""){
            listaCodDocumentos +=listaTablaEntradaOriginal[i][6]+'§¥';
        } else {
            listaCodDocumentos +="_"+'§¥';
        }

        listaCodCondicion +=listaTablaEntradaOriginal[i][3]+'§¥';
        listaExpresionesTabla +=listaTablaEntrada[i][1]+'§¥';

    }

    var listasCondicionEntrada = new Array();

    document.forms[0].listaCodTramitesTabla.value = listaCodTramitesTabla;//listasCondicionesEntrada[0];
    document.forms[0].listaTiposTabla.value = listaTiposTabla;//listasCondicionesEntrada[4];
    document.forms[0].listaTramitesTabla.value = listaTramitesTabla;//listasCondicionesEntrada[1];
    document.forms[0].listaEstadosTabla.value = listaEstadosTabla;//listasCondicionesEntrada[2];
    document.forms[0].listaCodCondicionTabla.value = listaCodCondicion;//listasCondicionesEntrada[3];
    document.forms[0].listaExpresionesTabla.value = listaExpresionesTabla;//listasCondicionesEntrada[2];
    document.forms[0].listaCodigosDocTabla.value = listaCodDocumentos;//listasCondicionesEntrada[2];
    
    return listasCondicionEntrada;
}

function borrarTablaEntrada() {
    listaTablaEntrada = new Array();
    tab.lineas=listaTablaEntrada;
    refresca();
    borrarDatosTablaEntrada();
}

function activarBotonesDoc() {
    var vectorBotones = new Array(document.forms[0].cmdAltaDoc,document.forms[0].cmdAdjuntarDoc,document.forms[0].cmdModificarDoc,document.forms[0].cmdEliminarDoc);
    habilitarGeneral(vectorBotones);
}

function desactivarBotonesDoc() {
    var vectorBotones = new Array(document.forms[0].cmdAltaDoc,document.forms[0].cmdAdjuntarDoc,document.forms[0].cmdModificarDoc,document.forms[0].cmdEliminarDoc);
    deshabilitarGeneral(vectorBotones);
}

function pulsarLimpiarDoc() {
    if(tabDoc.selectedIndex != -1 ) {
        tabDoc.selectLinea(tabDoc.selectedIndex);
        tabDoc.selectedIndex = -1;
    }
}

function borrarDatosEnlaces() {
    document.forms[0].txtDescUrl.value = '';
    document.forms[0].txtUrl.value = '';
    comboEstadoEnlace.selectItem(-1);
}

function borrarTablaDoc() {
    listaDoc = new Array();
    listaDocOriginal = new Array();
    tabDoc.lineas=listaDoc;
    refrescaDoc();
}

function comprobarObligatoriosDoc() {
    if(document.forms[0].codigoDoc.value == '' ||
        document.forms[0].nombreDoc.value == '' ||
        document.forms[0].descVisibleInternet.value == '' ||
        document.forms[0].plantilla.value == ''
        ) {
        return false;
    } else {
        return true;
    }
}

function crearListasDoc() {
    var listaCodigosDoc = "";
    var listaNombresDoc = "";
    var listaVisibleDoc = "";
    var listaPlantillaDoc = "";
    var listaCodPlantilla = "";
    var listaFirmaPlantilla = "";
    var listaActivos = "";
    for (i=0; i < listaDoc.length; i++) {
        listaCodigosDoc +=listaDoc[i][0]+'§¥';
        listaNombresDoc +=listaDoc[i][1]+'§¥';
        listaVisibleDoc +="S"+'§¥';
        listaPlantillaDoc +=listaDocOriginal[i][3]+'§¥';
        listaCodPlantilla +=listaDocOriginal[i][4]+'§¥';
        if (  (!(listaDocOriginal[i][6])) || (listaDocOriginal[i][6]=='') ) {
            listaFirmaPlantilla +='N§¥';
        } else {
            listaFirmaPlantilla +=listaDocOriginal[i][6]+'§¥';
        }
        listaActivos += listaDocOriginal[i][7]+'§¥';
    }
  
    document.forms[0].listaCodigosDoc.value = listaCodigosDoc;
    document.forms[0].listaNombresDoc.value = listaNombresDoc;
    document.forms[0].listaVisibleDoc.value = listaVisibleDoc;
    document.forms[0].listaPlantillaDoc.value = listaPlantillaDoc;
    document.forms[0].listaCodPlantilla.value = listaCodPlantilla;
    document.forms[0].listaFirmaPlantilla.value = listaFirmaPlantilla;
    document.forms[0].listaDocActivos.value = listaActivos;
}

function crearListasEnlaces() {
    var listaCodigoEnlaces = "";
    var listaDescripcionEnlaces = "";
    var listaUrlEnlaces = "";
    var listaEstadoEnlaces = "";

    for (i=0; i < listaEnlacesOriginal.length; i++) {
        listaCodigoEnlaces +=listaEnlacesOriginal[i][0]+'§¥';
        listaDescripcionEnlaces +=listaEnlacesOriginal[i][1]+'§¥';
        listaUrlEnlaces +=listaEnlacesOriginal[i][2]+'§¥';
        listaEstadoEnlaces +=listaEnlacesOriginal[i][3]+'§¥';
    }

    document.forms[0].listaCodigoEnlaces.value = listaCodigoEnlaces;
    document.forms[0].listaDescripcionEnlaces.value = listaDescripcionEnlaces;
    document.forms[0].listaUrlEnlaces.value = listaUrlEnlaces;
    document.forms[0].listaEstadoEnlaces.value = listaEstadoEnlaces;
}

function radioTramite() {
    if(document.forms[0].tipoCondicion[1].checked == true) {
        document.forms[0].cmdListaTramite.disabled = false;
        document.forms[0].texto.value='';
        seleccTipoCondicionPregunta(false);
    } else {
        document.forms[0].cmdListaTramite.disabled = true; 
        if (document.forms[0].tipoCondicion[4].checked == true) {
            if (document.forms[0].texto.value.trim()=='') {
                seleccTipoCondicionPregunta(false);
                document.forms[0].tipoCondicion[0].click();
            }
        } else {
            document.forms[0].texto.value='';
            seleccTipoCondicionPregunta(false);
        }
    }
    if(document.forms[0].tipoCondicion[3].checked == true) {
        seleccTipoCondicionPregunta(true);
    }
}

function radioTramiteSI() {
    if(document.forms[0].tipoCondicion[3].checked == true || document.forms[0].tipoCondicion[4].checked == true) {
        if(document.forms[0].tipoFavorableSI[0].checked == true ) {
            document.forms[0].cmdListaTramiteSiFavorable.disabled = false;
        } else if(document.forms[0].tipoFavorableSI[1].checked == true) {
            document.forms[0].cmdListaTramiteSiFavorable.disabled = true;
        }
    } else document.forms[0].tipoCondicion[0].click();
}

function radioTramiteNO() {

    if(document.forms[0].tipoCondicion[3].checked == true || document.forms[0].tipoCondicion[4].checked == true) {
        if(document.forms[0].tipoFavorableNO[0].checked == true) {
            document.forms[0].cmdListaTramiteNoFavorable.disabled = false;
        } else if(document.forms[0].tipoFavorableNO[1].checked == true) {
            document.forms[0].cmdListaTramiteNoFavorable.disabled = true;
        }
    } else {
        document.forms[0].tipoCondicion[0].click();
    }
}

function desactivarBotonesListasTramites() {
    document.forms[0].tipoFavorableSI[0].disabled= true;
    document.forms[0].tipoFavorableSI[1].disabled = true;
    document.forms[0].tipoFavorableNO[0].disabled= true;
    document.forms[0].tipoFavorableNO[1].disabled = true;
    document.forms[0].cmdListaTramite.disabled = true;
    document.forms[0].cmdListaTramiteSiFavorable.disabled = true;
    document.forms[0].cmdListaTramiteNoFavorable.disabled = true; 

  
}

function activarButtonsAcciones() {
    document.forms[0].tipoFavorableSI[0].disabled= false;
    document.forms[0].tipoFavorableSI[1].disabled = false;
    document.forms[0].tipoFavorableNO[0].disabled= false;
    document.forms[0].tipoFavorableNO[1].disabled = false;
}

function desactivarButtonsAcciones() {
    document.forms[0].tipoFavorableSI[0].disabled= true;
    document.forms[0].tipoFavorableSI[1].disabled = true;
    document.forms[0].tipoFavorableNO[0].disabled= true;
    document.forms[0].tipoFavorableNO[1].disabled = true;
}

function pulsarCancelarCatalogo() {
    top.close();
}

function tipoCondicion(){
    if(tipoCond == "Tramite") {
        document.forms[0].tipoCondicion[1].checked = true;
        document.forms[0].cmdListaTramite.disabled = false;
    } else if(tipoCond == "Pregunta") {
        document.forms[0].tipoCondicion[4].checked = true;
    } else if(tipoCond == "Finalizacion") {
        document.forms[0].tipoCondicion[2].checked = true;
    } else if(tipoCond == "Resolucion") {
        document.forms[0].tipoCondicion[3].checked = true;
    } else {
        document.forms[0].tipoCondicion[0].checked = true;
    }
    if(tipoCondSF == "TramiteSI") {
        document.forms[0].tipoFavorableSI[0].checked = true;
        document.forms[0].cmdListaTramiteSiFavorable.disabled = false;
        document.forms[0].cmdListaTramiteSiFavorable.style.color = '#ffffff';
    } else if(tipoCondSF == "FinalizacionSI") {
        document.forms[0].tipoFavorableSI[1].checked = true;
    } else {
        document.forms[0].tipoFavorableSI[0].checked = false;
        document.forms[0].tipoFavorableSI[1].checked = false;
    }
    if(tipoCondNF == "TramiteNO") {
        document.forms[0].tipoFavorableNO[0].checked = true;
        document.forms[0].cmdListaTramiteNoFavorable.disabled = false;
    } else if(tipoCondNF == "FinalizacionNO") {
        document.forms[0].tipoFavorableNO[1].checked = true;
    } else {
        document.forms[0].tipoFavorableNO[0].checked = false;
        document.forms[0].tipoFavorableNO[1].checked = false;
    }
}

function actualizarTipoCondicion(){
    if(document.forms[0].tipoCondicion[1].checked == true) {
        tipoCond = "Tramite";
    } else if(document.forms[0].tipoCondicion[2].checked == true) {
        tipoCond = "Finalizacion";
    } else if(document.forms[0].tipoCondicion[3].checked == true) {
        tipoCond = "Resolucion";
    } else if(document.forms[0].tipoCondicion[4].checked == true) {
        tipoCond = "Pregunta";
    } else {
        tipoCond = "";
    }
    if(document.forms[0].tipoFavorableSI[0].checked == true) {
        tipoCondSF = "TramiteSI";
    } else if(document.forms[0].tipoFavorableSI[1].checked == true) {
        tipoCondSF = "FinalizacionSI";
    } else {
        tipoCondSF = "";
    }
    if(document.forms[0].tipoFavorableNO[0].checked == true) {
        tipoCondNF = "TramiteNO";
    } else if(document.forms[0].tipoFavorableNO[1].checked == true) {
        tipoCondNF = "FinalizacionNO";
    } else {
        tipoCondNF = "";
    }
}

function activarBotonesPulsarModificar(){

    /** BOTONES DOCUMENTOS DESHABILITADOS **/
    document.forms[0].cmdAltaDoc.disabled = false;
    document.forms[0].cmdAltaDoc.cursor = 'hand';
    var botones = document.getElementsByName('cmdAltaDoc');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    /** BOTONES DOCUMENTOS DESHABILITADOS **/
    document.forms[0].cmdAdjuntarDoc.disabled = false;
    document.forms[0].cmdAdjuntarDoc.cursor = 'hand';
    var botones = document.getElementsByName('cmdAdjuntarDoc');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdModificarDoc.disabled = false;
    document.forms[0].cmdModificarDoc.cursor = 'hand';
    var botones = document.getElementsByName('cmdModificarDoc');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdEliminarDoc.disabled = false;
    document.forms[0].cmdEliminarDoc.cursor = 'hand';
    var botones = document.getElementsByName('cmdEliminarDoc');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdActivarDoc.disabled = false;
    document.forms[0].cmdActivarDoc.cursor = 'hand';
    var botones = document.getElementsByName('cmdActivarDoc');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }


    /** BOTONES ENLACES DESHABILITADOS **/
    document.forms[0].cmdAltaEnlaces.disabled = false;
    document.forms[0].cmdAltaEnlaces.cursor = 'hand';
    var botones = document.getElementsByName('cmdAltaEnlaces');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }


    document.forms[0].cmdModificarEnlaces.disabled = false;
    document.forms[0].cmdModificarEnlaces.cursor = 'hand';
    var botones = document.getElementsByName('cmdModificarEnlaces');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdEliminarEnlaces.disabled = false;
    document.forms[0].cmdEliminarEnlaces.cursor = 'hand';
    var botones = document.getElementsByName('cmdEliminarEnlaces');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdLimpiarEnlaces.disabled = false;
    document.forms[0].cmdLimpiarEnlaces.cursor = 'hand';
    var botones = document.getElementsByName('cmdLimpiarEnlaces');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }


    /** BOTONES CAMPOS SUPLEMENTARIOS **/
    document.forms[0].cmdAltaCampo.disabled = false;
    document.forms[0].cmdAltaCampo.cursor = 'hand';
    var botones = document.getElementsByName('cmdAltaCampo');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdModificarCampo.disabled = false;
    document.forms[0].cmdModificarCampo.cursor = 'hand';
    var botones = document.getElementsByName('cmdModificarCampo');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdEliminarCampo.disabled = false;
    document.forms[0].cmdEliminarCampo.cursor = 'hand';
    var botones = document.getElementsByName('cmdEliminarCampo');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdOrdenarCampo.disabled = false;
    document.forms[0].cmdOrdenarCampo.cursor = 'hand';
    var botones = document.getElementsByName('cmdOrdenarCampo');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdVista.disabled = false;
    document.forms[0].cmdVista.cursor = 'hand';
    var botones = document.getElementsByName('cmdVista');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }
    
    document.forms[0].cmdAltaAgrupacion.disabled = false;
    document.forms[0].cmdAltaAgrupacion.cursor = 'hand';
    var botones = document.getElementsByName('cmdAltaAgrupacion');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }
    
    document.forms[0].cmdModificarAgrupacion.disabled = false;
    document.forms[0].cmdModificarAgrupacion.cursor = 'hand';
    var botones = document.getElementsByName('cmdModificarAgrupacion');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }
    
    document.forms[0].cmdEliminarAgrupacion.disabled = false;
    document.forms[0].cmdEliminarAgrupacion.cursor = 'hand';
    var botones = document.getElementsByName('cmdEliminarAgrupacion');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    /** CONDICION ENTRADA **/
    document.forms[0].cmdAltaTablaEntrada.disabled = false;
    document.forms[0].cmdAltaTablaEntrada.cursor = 'hand';
    var botones = document.getElementsByName('cmdAltaTablaEntrada');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdModificarTablaEntrada.disabled = false;
    document.forms[0].cmdModificarTablaEntrada.cursor = 'hand';
    var botones = document.getElementsByName('cmdModificarTablaEntrada');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdEliminarTablaEntrada.disabled = false;
    document.forms[0].cmdEliminarTablaEntrada.cursor = 'hand';
    var botones = document.getElementsByName('cmdEliminarTablaEntrada');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdLimpiarTablaEntrada.disabled = false;
    document.forms[0].cmdLimpiarTablaEntrada.cursor = 'hand';
    var botones = document.getElementsByName('cmdLimpiarTablaEntrada');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }
	
    document.forms[0].cmdAltaConfSW.disabled = false;
    document.forms[0].cmdAltaConfSW.cursor   = 'hand';
    var botones = document.getElementsByName('cmdAltaConfSW');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdGrabarConfSW.disabled = false;
    document.forms[0].cmdGrabarConfSW.cursor   = 'hand';
    var botones = document.getElementsByName('cmdGrabarConfSW');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }


    document.forms[0].cmdEliminarConfSW.disabled = false;
    document.forms[0].cmdEliminarConfSW.cursor   = 'hand';
    var botones = document.getElementsByName('cmdEliminarConfSW');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdLimpiarrConfSW.disabled = false;
    document.forms[0].cmdLimpiarrConfSW.cursor   = 'hand';
    var botones = document.getElementsByName('cmdLimpiarrConfSW');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdAvanConfSW.disabled = false;
    document.forms[0].cmdAvanConfSW.cursor   = 'hand';
    var botones = document.getElementsByName('cmdAvanConfSW');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }


    document.forms[0].cmdRetConfSW.disabled = false;
    document.forms[0].cmdRetConfSW.cursor   = 'hand';
    var botones = document.getElementsByName('cmdRetConfSW');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }


    document.forms[0].cmdIniciarConfSW.disabled = false;
    document.forms[0].cmdIniciarConfSW.cursor   = 'hand';
    var botones = document.getElementsByName('cmdIniciarConfSW');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }


    document.forms[0].cmdCancelarEliminar.disabled = false;
    document.forms[0].cmdCancelarEliminar.cursor   = 'hand';
    var botones = document.getElementsByName('cmdCancelarEliminar');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdGrabarModificar.disabled = false;
    document.forms[0].cmdGrabarModificar.cursor   = 'hand';
    var botones = document.getElementsByName('cmdGrabarModificar');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdLimpiarPantallasTramExterna.disabled = false;
    document.forms[0].cmdLimpiarPantallasTramExterna.cursor   = 'hand';
    var botones = document.getElementsByName('cmdLimpiarPantallasTramExterna');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }
}

function pulsarCancelarModificar() {
    tipoCondicion();  
    limpiarCondicionesSalida();  
    desactivarFormulario();  
    domlay('capaNavegacion',0,0,0,' ');
    domlay('capaNavegacion',1,0,0,navegacion());
    mostrarCapasBotones('capaBotones1');
  
    var botonesCapaUno = [document.forms[0].cmdAlta, document.forms[0].cmdModificar, document.forms[0].cmdEliminarTram, document.forms[0].cmdDefProc];
    habilitarGeneral(botonesCapaUno);
  
    var botonesListaTramites = [document.forms[0].cmdListaTramite, document.forms[0].cmdListaTramiteSiFavorable, document.forms[0].cmdListaTramiteNoFavorable];
    habilitarGeneral(botonesListaTramites);
    enlaceTramite(tramiteActual);
    pintaTramiteActual();  
}

function activarBotonesPulsarAlta(){

    /** BOTONES DOCUMENTOS DESHABILITADOS **/
    document.forms[0].cmdAltaDoc.disabled = true;
    document.forms[0].cmdAltaDoc.cursor = 'default';
    var botones = document.getElementsByName('cmdAltaDoc');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneralDeshabilitado';
    }

    document.forms[0].cmdAdjuntarDoc.disabled = true;
    document.forms[0].cmdAdjuntarDoc.cursor = 'default';
    var botones = document.getElementsByName('cmdAdjuntarDoc');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneralDeshabilitado';
    }

    document.forms[0].cmdModificarDoc.disabled = true;
    document.forms[0].cmdModificarDoc.cursor = 'default';
    var botones = document.getElementsByName('cmdModificarDoc');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneralDeshabilitado';
    }

    document.forms[0].cmdEliminarDoc.disabled = true;
    document.forms[0].cmdEliminarDoc.cursor = 'default';
    var botones = document.getElementsByName('cmdEliminarDoc');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneralDeshabilitado';
    }

    document.forms[0].cmdActivarDoc.disabled = true;
    document.forms[0].cmdActivarDoc.cursor = 'default';
    var botones = document.getElementsByName('cmdActivarDoc');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneralDeshabilitado';
    }


    /** BOTONES ENLACES DESHABILITADOS **/
    document.forms[0].cmdAltaEnlaces.disabled = false;
    document.forms[0].cmdAltaEnlaces.cursor = 'hand';
    var botones = document.getElementsByName('cmdAltaEnlaces');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }


    document.forms[0].cmdModificarEnlaces.disabled = false;
    document.forms[0].cmdModificarEnlaces.cursor = 'hand';
    var botones = document.getElementsByName('cmdModificarEnlaces');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdEliminarEnlaces.disabled = false;
    document.forms[0].cmdEliminarEnlaces.cursor = 'hand';
    var botones = document.getElementsByName('cmdEliminarEnlaces');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdLimpiarEnlaces.disabled = false;
    document.forms[0].cmdLimpiarEnlaces.cursor = 'hand';
    var botones = document.getElementsByName('cmdLimpiarEnlaces');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }


    /** BOTONES CAMPOS SUPLEMENTARIOS **/
    document.forms[0].cmdAltaCampo.disabled = false;
    document.forms[0].cmdAltaCampo.cursor = 'hand';
    var botones = document.getElementsByName('cmdAltaCampo');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdModificarCampo.disabled = false;
    document.forms[0].cmdModificarCampo.cursor = 'hand';
    var botones = document.getElementsByName('cmdModificarCampo');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdEliminarCampo.disabled = false;
    document.forms[0].cmdEliminarCampo.cursor = 'hand';
    var botones = document.getElementsByName('cmdEliminarCampo');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdOrdenarCampo.disabled = false;
    document.forms[0].cmdOrdenarCampo.cursor = 'hand';
    var botones = document.getElementsByName('cmdOrdenarCampo');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdVista.disabled = false;
    document.forms[0].cmdVista.cursor = 'hand';
    var botones = document.getElementsByName('cmdVista');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }
    
    document.forms[0].cmdAltaAgrupacion.disabled = false;
    document.forms[0].cmdAltaAgrupacion.cursor = 'hand';
    var botones = document.getElementsByName('cmdAltaAgrupacion');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }
    
    document.forms[0].cmdModificarAgrupacion.disabled = false;
    document.forms[0].cmdModificarAgrupacion.cursor = 'hand';
    var botones = document.getElementsByName('cmdModificarAgrupacion');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }
    
    document.forms[0].cmdEliminarAgrupacion.disabled = false;
    document.forms[0].cmdEliminarAgrupacion.cursor = 'hand';
    var botones = document.getElementsByName('cmdEliminarAgrupacion');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    /** CONDICION ENTRADA **/
    document.forms[0].cmdAltaTablaEntrada.disabled = false;
    document.forms[0].cmdAltaTablaEntrada.cursor = 'hand';
    var botones = document.getElementsByName('cmdAltaTablaEntrada');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdModificarTablaEntrada.disabled = false;
    document.forms[0].cmdModificarTablaEntrada.cursor = 'hand';
    var botones = document.getElementsByName('cmdModificarTablaEntrada');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdEliminarTablaEntrada.disabled = false;
    document.forms[0].cmdEliminarTablaEntrada.cursor = 'hand';
    var botones = document.getElementsByName('cmdEliminarTablaEntrada');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdLimpiarTablaEntrada.disabled = false;
    document.forms[0].cmdLimpiarTablaEntrada.cursor = 'hand';
    var botones = document.getElementsByName('cmdLimpiarTablaEntrada');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }


    /** CONDICIÓN DE SALIDA **/
    document.forms[0].cmdListaTramite.disabled = true;
    document.forms[0].cmdListaTramite.cursor = 'default';
    var botones = document.getElementsByName('cmdListaTramite');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneralDeshabilitado';
    }

    document.forms[0].cmdListaTramiteSiFavorable.disabled = true;
    document.forms[0].cmdListaTramiteSiFavorable.cursor = 'default';
    var botones = document.getElementsByName('cmdListaTramiteSiFavorable');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneralDeshabilitado';
    }

    document.forms[0].cmdListaTramiteNoFavorable.disabled = true;
    document.forms[0].cmdListaTramiteNoFavorable.cursor = 'default';
    var botones = document.getElementsByName('cmdListaTramiteNoFavorable');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneralDeshabilitado';
    }


    /** BOTONES CONFIGURACIÓNS WEB SERVICES **/
    document.forms[0].cmdAltaConfSW.disabled = false;
    document.forms[0].cmdAltaConfSW.cursor   = 'hand';
    var botones = document.getElementsByName('cmdAltaConfSW');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdGrabarConfSW.disabled = false;
    document.forms[0].cmdGrabarConfSW.cursor   = 'hand';
    var botones = document.getElementsByName('cmdGrabarConfSW');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }


    document.forms[0].cmdEliminarConfSW.disabled = false;
    document.forms[0].cmdEliminarConfSW.cursor   = 'hand';
    var botones = document.getElementsByName('cmdEliminarConfSW');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdLimpiarrConfSW.disabled = false;
    document.forms[0].cmdLimpiarrConfSW.cursor   = 'hand';
    var botones = document.getElementsByName('cmdLimpiarrConfSW');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }

    document.forms[0].cmdAvanConfSW.disabled = false;
    document.forms[0].cmdAvanConfSW.cursor   = 'hand';
    var botones = document.getElementsByName('cmdAvanConfSW');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }


    document.forms[0].cmdRetConfSW.disabled = false;
    document.forms[0].cmdRetConfSW.cursor   = 'hand';
    var botones = document.getElementsByName('cmdRetConfSW');
    if(botones!=null && botones.length==1){
        botones[0].className = 'botonGeneral';
    }
}

function actualizaComboCondEnt(lista1,lista2,lista3) {
    window.focus();
    id_tramiteTabla=lista1;
    codigo_tramiteTabla=lista2;
    nombre_tramiteTabla = lista3;
    cargarCombos();
    limpiarCombosWS();
}

function limpiarListasSalida() {
    document.forms[0].listaCodTramitesFlujoSalida.value == "";
    document.forms[0].listaNombreTramitesFlujoSalida.value = "";
    document.forms[0].listaNumerosSecuenciaFlujoSalida.value = "";
    document.forms[0].listaDescTramitesFlujoSalida.value = "";
    document.forms[0].oblig.value = "";
    document.forms[0].numeroCondicionSalida.value = "";
    document.forms[0].listaCodTramitesFlujoSalidaSiFavorable.value == "";
    document.forms[0].listaNombreTramitesFlujoSalidaSiFavorable.value = "";
    document.forms[0].listaNumerosSecuenciaFlujoSalidaSiFavorable.value = "";
    document.forms[0].listaDescTramitesFlujoSalidaSiFavorable.value = "";
    document.forms[0].obligSiFavorable.value = "";
    document.forms[0].numeroCondicionSalidaSiFavorable.value = "";
    document.forms[0].listaCodTramitesFlujoSalidaNoFavorable.value == "";
    document.forms[0].listaNombreTramitesFlujoSalidaNoFavorable.value = "";
    document.forms[0].listaNumerosSecuenciaFlujoSalidaNoFavorable.value = "";
    document.forms[0].listaDescTramitesFlujoSalidaNoFavorable.value = "";
    document.forms[0].obligNoFavorable.value = "";
    document.forms[0].numeroCondicionSalidaNoFavorable.value = "";
    return true;
}

function desactivarBotonesCapa1() {
    document.forms[0].cmdAlta.disabled = true;
    document.forms[0].cmdAlta.style.cursor = 'default';
    document.forms[0].cmdModificar.disabled = true;
    document.forms[0].cmdModificar.style.cursor = 'default';
    document.forms[0].cmdEliminarTram.disabled = true;
    document.forms[0].cmdEliminarTram.style.cursor = 'default';
}

function pulsarCancelarAlta() {
    desactivarFormulario();
    if(numeroTramites >0 ) {
        enlaceTramite(tramiteActual);
    }
    domlay('capaNavegacion',0,0,0,' ');
    domlay('capaNavegacion',1,0,0,navegacion());
    mostrarCapasBotones('capaBotones1');
  
    var botonesCapaUno = [document.forms[0].cmdAlta, document.forms[0].cmdModificar, document.forms[0].cmdEliminarTram, document.forms[0].cmdDefProc];
    habilitarGeneral(botonesCapaUno);
  
    var botonesListaTramites = [document.forms[0].cmdListaTramite, document.forms[0].cmdListaTramiteSiFavorable, document.forms[0].cmdListaTramiteNoFavorable];
    habilitarGeneral(botonesListaTramites);
}

function revisarNumeroOrden() {
    for (i=0;i<listaCamposOriginal.length;i++) {
        listaCamposOriginal[i][7] = i + 1;
    }
}

function borrarTablaCampos () {
    listaCampos = new Array();
    listaCamposOriginal = new Array();
    tabCampos.lineas=listaCampos;
    refrescaCampos();
}

function crearListasCampos() {
    var listaCodCampos = "";
    var listaDescCampos = "";
    var listaCodPlantilla = "";
    var listaCodTipoDato = "";
    var listaTamano = "";
    var listaMascara = "";
    var listaObligatorio = "";
    var listaOrden = "";
    var listaRotulo = "";
    var listaVisible = "";
    var listaActivo = "";
    var listaOcultos = "";
    var listaBloqueados = "";
    var listaPlazoFecha = "";
    var listaCheckPlazoFecha = "";
    var listaValidacion = "";
    var listaOperacion = "";
    var listaCodAgrupacion = "";
    var listaPosicionXCampo = "";
    var listaPosicionYCampo = "";
    for (i=0; i < listaCampos.length; i++) {
        listaCodCampos += listaCamposOriginal[i][0]+'§¥';
        listaDescCampos += listaCamposOriginal[i][1]+'§¥';
        listaCodPlantilla += listaCamposOriginal[i][2]+'§¥';
        listaCodTipoDato += listaCamposOriginal[i][3]+'§¥';
        listaTamano += listaCamposOriginal[i][4]+'§¥';
        if(listaCamposOriginal[i][5] == "") {
            listaMascara += " "+'§¥';
        } else {
            listaMascara += listaCamposOriginal[i][5]+'§¥'; 
        }
        listaObligatorio += listaCamposOriginal[i][6]+'§¥';
        listaOrden += listaCamposOriginal[i][7]+'§¥';
        listaRotulo += listaCamposOriginal[i][10]+'§¥';
        listaVisible += listaCamposOriginal[i][11]+'§¥';
        listaActivo += listaCamposOriginal[i][12]+'§¥';
        listaOcultos += listaCamposOriginal[i][13]+'§¥';
        listaBloqueados += listaCamposOriginal[i][14]+'§¥';
        //PlazoFecha y CheckPlazoFecha
        if(listaCamposOriginal[i][15] == "") {
             listaPlazoFecha += " "+'§¥';
        } else {
            listaPlazoFecha += listaCamposOriginal[i][15]+'§¥';
        }
        if(listaCamposOriginal[i][16] == "") {
             listaCheckPlazoFecha += " "+'§¥';
        } else {
            listaCheckPlazoFecha += listaCamposOriginal[i][16]+'§¥';
        }	
        if(listaCamposOriginal[i][17] == "") {
            listaValidacion += " "+'§¥';
        } else {
            listaValidacion += listaCamposOriginal[i][17]+'§¥';
        }
        if(listaCamposOriginal[i][18] == "") {
            listaOperacion += " "+'§¥';
        } else {
            listaOperacion += listaCamposOriginal[i][18]+'§¥';
        }
        if(listaCamposOriginal[i][19] == ""){
            listaCodAgrupacion += " "+'§¥';            
        }else
        {
            listaCodAgrupacion += listaCamposOriginal[i][19]+'§¥';
        }
        if(listaCamposOriginal[i][20] === ""){
            listaPosicionXCampo += null + '§¥';
        }else{
            listaPosicionXCampo += listaCamposOriginal[i][20]+'§¥';
        }//if(listaCamposOriginal[i][20] == "")
        if(listaCamposOriginal[i][21] === ""){
            listaPosicionYCampo += null + '§¥';
        }else{
            listaPosicionYCampo += listaCamposOriginal[i][21]+'§¥';
        }//if(listaCamposOriginal[i][21] == "")
    } 
    document.forms[0].listaCodCampos.value = listaCodCampos;
    document.forms[0].listaDescCampos.value = listaDescCampos;
    document.forms[0].listaCodPlantill.value = listaCodPlantilla;
    document.forms[0].listaCodTipoDato.value = listaCodTipoDato;
    document.forms[0].listaTamano.value = listaTamano;
    document.forms[0].listaMascara.value = listaMascara;
    document.forms[0].listaObligatorio.value = listaObligatorio;
    document.forms[0].listaOrden.value = listaOrden;
    document.forms[0].listaRotulo.value = listaRotulo;
    document.forms[0].listaVisible.value = listaVisible;
    document.forms[0].listaActivo.value = listaActivo;
    document.forms[0].listaOcultos.value = listaOcultos;
    document.forms[0].listaBloqueados.value = listaBloqueados;
    document.forms[0].listaPlazoFecha.value = listaPlazoFecha;
    document.forms[0].listaCheckPlazoFecha.value = listaCheckPlazoFecha;
    document.forms[0].listaValidacion.value = listaValidacion;
    document.forms[0].listaOperacion.value = listaOperacion;
    document.forms[0].listaCodAgrupacion.value = listaCodAgrupacion;
    document.forms[0].listaPosicionesX.value = listaPosicionXCampo;
    document.forms[0].listaPosicionesY.value = listaPosicionYCampo;
}

function crearListasAgrupaciones(){
    var listaCodAgrupaciones = "";
    var listaDescAgrupaciones = "";
    var listaOrdenAgrupaciones = "";
    var listaAgrupacionesActivas = "";
    
    for (i=0; i < listaAgrupacionesOriginal.length; i++) {
        listaCodAgrupaciones += listaAgrupacionesOriginal[i][0] +'§¥';
        listaDescAgrupaciones += listaAgrupacionesOriginal[i][1] +'§¥';
        listaOrdenAgrupaciones +=listaAgrupacionesOriginal[i][2] +'§¥';
        listaAgrupacionesActivas += listaAgrupacionesOriginal[i][3] +'§¥';
    }//for (i=0; i < listaAgrupaciones.length; i++)

    document.forms[0].listaCodAgrupaciones.value = listaCodAgrupaciones;
    document.forms[0].listaDescAgrupaciones.value = listaDescAgrupaciones;
    document.forms[0].listaOrdenAgrupaciones.value = listaOrdenAgrupaciones;
    document.forms[0].listaAgrupacionesActivas.value = listaAgrupacionesActivas;
}//crearListasAgrupaciones

function callFromTableTo(rowID,tableName){
    if(tabCampos.id == tableName){
        verCampo(rowID);
    } else if (tabConfSW.id == tableName) {
        verAvanzarRetroceder(rowID);
    }
}

function mostrarListaUnidadInicio(){
    var condiciones = new Array();
    condiciones[0]='UOR_NO_VIS'+'§¥';
    condiciones[1]='0';
    condiciones[2]='UOR_ESTADO'+'§¥';
    condiciones[3]='A';
    muestraListaTabla('UOR_COD_VIS','UOR_NOM','A_UOR',condiciones,'codVisibleUnidadInicio','descUnidadInicio', 'botonUnidadInicio','100');
}

function onClickDescUnidadInicio(){
    divSegundoPlano=false;
    mostrarListaUnidadInicio();
}

function onchangeCodUnidadInicio() {
    var uor = buscarUorPorCodVisibleEstado(uors, document.forms[0].codVisibleUnidadInicio.value, 'A');
    if(uor != null) {
        document.forms[0].codUnidadInicio.value = uor.uor_cod;
        document.forms[0].descUnidadInicio.value = uor.uor_nom;
    }
    else { // ha dado null para alta, buscamos de baja
        uor = buscarUorPorCodVisibleEstado(uors, document.forms[0].codVisibleUnidadInicio, 'B');
        if(uor != null) {
            document.forms[0].codUnidadInicio.value = uor.uor_cod;
            document.forms[0].descUnidadInicio.value = uor.uor_nom;
        }
    }
    if(uor == null) {            
        document.forms[0].codUnidadInicio.value = '';
        document.forms[0].descUnidadInicio.value = '';
        document.forms[0].codVisibleUnidadInicio.value = '';
    }
}

function mostrarListaUnidadTramite() {
    var listaTabla = new Array();
    for (var i = 0; i<listaUnidadesTramitadoras.length; i++) {
        var uor = buscarUorPorCod(listaUnidadesTramitadoras[i]);
        listaTabla[i] = [uor.uor_cod_vis, uor.uor_nom];
    }
    tablaUnidadesTramite.lineas=listaTabla;
    tablaUnidadesTramite.displayTabla();
}

function antesDeCambiarPestana() {
    ocultarCalendario();
    ocultarLista();
    ocultarDivNoFocus();
}
 
function onClickDescCargo(){
    divSegundoPlano=false;
    mostrarListaCargo();
}

function mostrarListaCargo(){
    var condiciones = new Array();
    muestraListaTabla('CAR_COD_VIS','CAR_NOM','A_CAR',condiciones,'cod_visible_cargo','descCargo', 'botonCargo','100');
}

function limpiarCombosWS() {
    document.forms[0].cfoAvanzar.value=-1;
    document.forms[0].cfoRetroceder.value=-1;
    document.forms[0].cfoIniciar.value=-1;
    comboAvanzar.buscaLinea('-1');
    comboRetroceder.buscaLinea('-1');
    comboIniciar.buscaLinea('-1');
    comboTipoRetroceder.buscaLinea('-1');
}

function desactivarCombosWS() {
    document.forms[0].cfoAvanzar.value=-1;
    document.forms[0].cfoRetroceder.value=-1;
    document.forms[0].cfoIniciar.value=-1;
    comboAvanzar.buscaLinea('-1');
    comboRetroceder.buscaLinea('-1');
    comboIniciar.buscaLinea('-1');
    comboTipoRetroceder.buscaLinea('-1');
    comboAvanzar.deactivate();
    comboIniciar.deactivate();
    comboRetroceder.deactivate();
    comboTipoRetroceder.deactivate();
    tipoOperacionRetroceso = new Array();
}

function recuperaConfSW(lista1,lista2,listaTipos,listaOps,listaModulos,listaTipoOperacionRetroceso) {    
    listaConfSW = lista1;
    listaConfSWCfos = lista2;
    howManySW = listaConfSW.length;
    tiposOperaciones = listaTipos;
    nombresOperaciones = listaOps;
    nombresModulo = listaModulos;    
    listNom = getNombresConfSW(listaConfSW);
    tipoOperacionRetroceso = listaTipoOperacionRetroceso;
    tabConfSW.lineas = listNom;
    tabConfSW.displayTabla();
}

function activarSelectNotFueraDePlazo(){
    if(document.forms[0].notificarFueraDePlazo.checked ){
        comboNotFueraDePlazo.activate();
        document.forms[0].notificarFueraDePlazo.checked=true;
    }else{
        comboNotFueraDePlazo.deactivate();
        document.forms[0].notificarFueraDePlazo.checked=false;
        document.forms[0].descNotFueraDePlazo.value="";
    }
    activarDestinatariosFuncionPlazo();
}

function activarSelectNotCercaFinPlazo(){
    if(document.forms[0].notificarCercaFinPlazo.checked ){
        comboNotCercaFinPlazo.activate();
        document.forms[0].notificarCercaFinPlazo.checked=true;
    }else{
        comboNotCercaFinPlazo.deactivate();
        document.forms[0].notificarCercaFinPlazo.checked=false;
        document.forms[0].descNotCercaFinPlazo.value="";
    }
    activarDestinatariosFuncionPlazo();
}

function activarAdmiteNotifElect(){ 
    if(document.forms[0].admiteNotificacionElectronica.checked ){     
        comboAdmiteNotifElect.activate();
        comboDepartamento.activate();
        document.forms[0].admiteNotificacionElectronica.checked=true;
        //document.forms[0].botonTipoNotificacion.disabled = false;
        //document.forms[0].botonTipoNotificacion.style.cursor = 'pointer';
        
        document.forms[0].notificacionElectronicaObligatoria.disabled=false;
        document.forms[0].certificadoOrganismoFirmaNotificacion.disabled=false;
        document.forms[0].codDepartamentoNotificacion.disabled=false;
        document.forms[0].descDepartamentoNotificacion.disabled=false;

    }else{
        comboAdmiteNotifElect.deactivate();
        comboDepartamento.deactivate();
        document.forms[0].admiteNotificacionElectronica.checked=false;
        document.forms[0].codAdmiteNotifElect.value="";
        document.forms[0].descAdmiteNotifElect.value="";
        document.forms[0].codDepartamentoNotificacion.value="";
        document.forms[0].descDepartamentoNotificacion.value="";
        //document.forms[0].botonTipoNotificacion.disabled = true;
        //document.forms[0].botonTipoNotificacion.style.cursor = 'default';
	  
        document.forms[0].codigoTipoNotificacionElectronica.value='';
        document.forms[0].tipoUsuarioFirma.value='';
        document.forms[0].codigoOtroUsuarioFirma.value='';
        document.forms[0].nombreOtroUsuarioFirma.value='';
        document.forms[0].firma.value= '';
        
        document.forms[0].notificacionElectronicaObligatoria.disabled=true;
        document.forms[0].notificacionElectronicaObligatoria.checked=false;
        document.forms[0].certificadoOrganismoFirmaNotificacion.disabled=true;
        document.forms[0].certificadoOrganismoFirmaNotificacion.checked=false;
        document.getElementsByName("notificacionElectronicaObligatoriaValue")[0].value = "off";
        document.getElementsByName("certificadoOrganismoFirmaNotificacionValue")[0].value = "off";
	  
        tipoUsuarioFirma='';
        codigoOtroUsuarioFirma='';
        nombreOtroUsuarioFirma='';
    }
    
}

function modificarNotifObligatoria(){
    var checkOblig = document.getElementsByName("notificacionElectronicaObligatoria");
    if (checkOblig && checkOblig[0]){
        document.getElementsByName("notificacionElectronicaObligatoriaValue")[0].value = (checkOblig[0].checked)?"on":"off";
    }
}

function modificarCertificadoOrganismo(){
    var checkCertOrganismo = document.getElementsByName("certificadoOrganismoFirmaNotificacion");
    if (checkCertOrganismo && checkCertOrganismo[0]){
        document.getElementsByName("certificadoOrganismoFirmaNotificacionValue")[0].value = (checkCertOrganismo[0].checked)?"on":"off";
    }    
}

function actualizarNotAutomaticas(notificarCercaFinPlazo,notificarFueraDePlazo,tipoNotCercaFinPlazo,tipoNotFueraDePlazo,admiteNotifElectr,codTipoNotifElect,tipoUsuFirma,codigoOtroUsuFirma,nombreOtroUsuFirma, notificacionObligatoria, certificadoOrganismo,codDepartamento){
    tipoUsuarioFirma=tipoUsuFirma;
    nombreOtroUsuarioFirma=nombreOtroUsuFirma;
    codigoOtroUsuarioFirma=codigoOtroUsuFirma;
    
    if(notificarCercaFinPlazo==true ){
        document.forms[0].notificarCercaFinPlazo.checked=true;
        if (comboActivo==true) comboNotCercaFinPlazo.activate();
        if (comboActivo==false) comboNotCercaFinPlazo.deactivate();              
        document.forms[0].codNotCercaFinPlazo.value=tipoNotCercaFinPlazo;
        if(tipoNotCercaFinPlazo==1)document.forms[0].descNotCercaFinPlazo.value=descNotificacionTramite[0];
        else document.forms[0].descNotCercaFinPlazo.value=descNotificacionTramite[1];
    }else{
        document.forms[0].notificarCercaFinPlazo.checked=false;
        comboNotCercaFinPlazo.deactivate();
        document.forms[0].descNotCercaFinPlazo.value="";
    }

    if(notificarFueraDePlazo==true ){
        document.forms[0].notificarFueraDePlazo.checked=true;
        if (comboActivo==true) comboNotFueraDePlazo.activate();
        if (comboActivo==false) comboNotFueraDePlazo.deactivate();
        document.forms[0].codNotFueraDePlazo.value=tipoNotFueraDePlazo;
        if(tipoNotFueraDePlazo==1)document.forms[0].descNotFueraDePlazo.value=descNotificacionTramite[0];
        else document.forms[0].descNotFueraDePlazo.value=descNotificacionTramite[1];
    }else{
        document.forms[0].notificarFueraDePlazo.checked=false;
        comboNotFueraDePlazo.deactivate();
        document.forms[0].descNotFueraDePlazo.value="";
    }

    if(admiteNotifElectr == '1') {       
        document.forms[0].admiteNotificacionElectronica.checked = true;        
        if (comboActivo==true) { 
            comboAdmiteNotifElect.activate();
            comboDepartamento.activate();
            //document.forms[0].botonTipoNotificacion.disabled = false;
            //document.forms[0].botonTipoNotificacion.style.cursor = 'pointer';
            
            var arrayNEO = document.getElementsByName("notificacionElectronicaObligatoria");
            for (k =0; k < arrayNEO.length; k++){
                if(arrayNEO[k]){arrayNEO[k].disabled=false;}
            }
        }
        if (comboActivo==false) { 
            comboAdmiteNotifElect.deactivate();
            comboDepartamento.deactivate();
            //document.forms[0].botonTipoNotificacion.disabled = true;
            //document.forms[0].botonTipoNotificacion.style.cursor = 'default';
            var arrayNEO = document.getElementsByName("notificacionElectronicaObligatoria");
            for (k =0; k < arrayNEO.length; k++){
                if(arrayNEO[k]){arrayNEO[k].disabled=true;}
            }

            document.forms[0].certificadoOrganismoFirmaNotificacion.disabled = true;
        }
        document.forms[0].codAdmiteNotifElect.value = codTipoNotifElect;
        document.forms[0].descAdmiteNotifElect.value = obtenerDescTipoNotifElect(codTipoNotifElect);
        document.forms[0].codDepartamentoNotificacion.value = codDepartamento;
        document.forms[0].descDepartamentoNotificacion.value = obtenerDescripcionDepartamento(codDepartamento);
        if (document.forms[0].notificacionElectronicaObligatoria && notificacionObligatoria!= undefined){
            var arrayNEO = document.getElementsByName("notificacionElectronicaObligatoria");
            for (k =0; k < arrayNEO.length; k++){
                if(arrayNEO[k]){arrayNEO[k].checked=notificacionObligatoria.toUpperCase() =="ON";}
            }
            var arrayNEO = document.getElementsByName("notificacionElectronicaObligatoriaValue");	
            for (k =0; k < arrayNEO.length; k++){	
                if(arrayNEO[k]){arrayNEO[k].value=notificacionObligatoria;}	
            }
        }
    } else {         
        document.forms[0].admiteNotificacionElectronica.checked = false;
        comboAdmiteNotifElect.deactivate();
        comboDepartamento.deactivate();
        document.forms[0].codAdmiteNotifElect.value = "";
        document.forms[0].descAdmiteNotifElect.value = "";
        document.forms[0].codDepartamentoNotificacion.value  = "";
        document.forms[0].descDepartamentoNotificacion.value = "";
        document.forms[0].certificadoOrganismoFirmaNotificacion.disabled=true;
        //document.forms[0].botonTipoNotificacion.disabled = true;
        //document.forms[0].botonTipoNotificacion.style.cursor = 'default';
        
         var arrayNEO = document.getElementsByName("notificacionElectronicaObligatoria");
         for (k =0; k < arrayNEO.length; k++){
                if(arrayNEO[k] && arrayNEO[k].checked){
                    arrayNEO[k].checked=false;
                }
                if(arrayNEO[k]){
                    arrayNEO[k].disabled=true;
                }
         }

    }

    if(certificadoOrganismo=="on" || certificadoOrganismo=="ON")
        document.forms[0].certificadoOrganismoFirmaNotificacion.checked=true;
    else
        document.forms[0].certificadoOrganismoFirmaNotificacion.checked=false;
    
    modificarEtiquetaFirma();	

}


function obtenerDescripcionDepartamento(codDepartamento){
    var desc = "";
    for(i=0;i<codigosDepartamentos.length;i++){

        if(codigosDepartamentos[i]==codDepartamento){
            desc = descripcionesDepartamentos[i];
            break;
        }
    }

    return desc;
}

function obtenerDescTipoNotifElect(codTipoNotifElect)
{    
    var cont=0;
    while(cont<codigo_tipoNotificacionElectronica.length) {	  
        if(codigo_tipoNotificacionElectronica[cont]==codTipoNotifElect)
        {
            return (nombre_tipoNotificacionElectronica[cont]);
        }

        cont++;
    }	
}

function onblurClasifTram(cod, des){
    if(camposDistintosDiv(cod, des))
        document.forms[0].plantilla.value='';
}

/**
  * Función que busca en el array con las descripciones de las operaciones, el código que le 
  * corresponde a la misma, para que pueda ser seleccionado en el combo de operaciones si así se desea
  * @param descripcionOperacion: Descripción de la operación
  * @return Un int
  */
function buscarCodigoOperacionWebservices(nombreOperacionBusqueda) { 
    var codigoOperacion;
    
    for(var z=0;z<desc_webServices.length;z++) { 
        var datosDesc = desc_webServices[z].split("(");
        if(datosDesc!=undefined && datosDesc.length>=2) {
            var descripcion = datosDesc[0].trim();
            if(descripcion==nombreOperacionBusqueda) {                         
                codigoOperacion = cod_webServices[z];                        
                break;
            }// if
        }// if
    }// for           

    return codigoOperacion;
}

function verAvanzarRetroceder(i) {
    if(document.forms[0].cmdAltaEnlaces.disabled == false) {          		        
        document.forms[0].codIniciarSW.value ="";
        document.forms[0].descIniciarSW.value ="";
        document.forms[0].codAvanzarSW.value ="";
        document.forms[0].descAvanzarSW.value ="";
        document.forms[0].codRetrocederSW.value ="";
        document.forms[0].descRetrocederSW.value ="";
        document.forms[0].cfoIniciar.value = "";
        document.forms[0].cfoAvanzar.value = "";
        document.forms[0].cfoRetroceder.value = "";
        document.forms[0].codTipoRetrocesoSW.value = "";
        document.forms[0].descTipoRetrocesoSW.value = "";

        var existeOperacionIniciar =  buscarOperacion(tiposOperaciones[i][0],nombresOperaciones[i][0],nombresModulo[i][0],desc_webServices,tipoOrigenOperaciones);
        var existeOperacionAvanzar =  buscarOperacion(tiposOperaciones[i][1],nombresOperaciones[i][1],nombresModulo[i][1],desc_webServices,tipoOrigenOperaciones);
        var existeOperacionRetroceder =  buscarOperacion(tiposOperaciones[i][2],nombresOperaciones[i][2],nombresModulo[i][2],desc_webServices,tipoOrigenOperaciones);

        if(existeOperacionIniciar){                        
            document.forms[0].cfoIniciar.value = listaConfSWCfos[i][0];		
            var codigoOperacion = buscarCodigoOperacionWebservices(nombresOperaciones[i][0].trim());            
            comboIniciar.buscaLinea(codigoOperacion);	            
        }	
        
        if(existeOperacionAvanzar){                        
            document.forms[0].cfoAvanzar.value = listaConfSWCfos[i][1];                        
            var codigoOperacion = buscarCodigoOperacionWebservices(nombresOperaciones[i][1].trim());            
            comboAvanzar.buscaLinea(codigoOperacion);            
        }	
        
        if(existeOperacionRetroceder){ 		
            document.forms[0].cfoRetroceder.value = listaConfSWCfos[i][2];                                    
            var codigoOperacion = buscarCodigoOperacionWebservices(nombresOperaciones[i][2].trim());            
            comboRetroceder.buscaLinea(codigoOperacion);                    
            habilitarComboTipoRetroceso(true);
            comboTipoRetroceder.buscaLinea(tipoOperacionRetroceso[i][2]);
        }
    }
}

function habilitarComboTipoRetroceso(flag){ 
    if(flag){
        comboTipoRetroceder.activate();        
    }
    else
        comboTipoRetroceder.deactivate();        
}

function refrescaCampos() {
    tabCampos.displayTabla();
}

function refrescaDoc() {
    tabDoc.displayTabla();
}

function refresca() {
    tab.displayTabla();
}

function mostrarCapasBotones(nombreCapa) {
    document.getElementById('capaBotones1').style.display='none';
    document.getElementById('capaBotones2').style.display='none';
    document.getElementById('capaBotones3').style.display='none';
    document.getElementById('capaBotones4').style.display='none';
    document.getElementById(nombreCapa).style.display='';
}

function limpiarPantallasTramitacionExterna() {
    document.forms[0].codPluginPantallaTramitacionExterna.value     = "";
    document.forms[0].descPluginPantallaTramitacionExterna.value    = "";
    document.forms[0].codUrlPluginPantallaTramitacionExterna.value  = "";
    document.forms[0].descUrlPluginPantallaTramitacionExterna.value = "";
    document.forms[0].implClassPluginPantallaTramExterna.value      = "";
    codPluginSeleccionado	    = "";urlPluginSeleccionado		= "";implClassPluginSeleccionado = "";
    comboTramitacionExterna.selectItem(-1);
    comboUrlTramitacionExterna.addItems([],[]);
}


function comprobarPluginPantallaTramitacionExterna(){
    var exito = false;    
    var codPlugin  = document.forms[0].codPluginPantallaTramitacionExterna.value;
    var descPlugin = document.forms[0].descPluginPantallaTramitacionExterna.value;
    var codUrl     = document.forms[0].codUrlPluginPantallaTramitacionExterna.value;
    var descUrl    = document.forms[0].descUrlPluginPantallaTramitacionExterna.value;
    var implClass  = document.forms[0].implClassPluginPantallaTramExterna.value;

    var sinCondicion  = document.forms[0].tipoCondicion[0].checked;
    var tramite 	  = document.forms[0].tipoCondicion[1].checked;
	var finalizacion  = document.forms[0].tipoCondicion[2].checked;

    if(codPlugin!="" && descPlugin!="" && codUrl!="" && descUrl!="" && implClass!=""){
        if(!(sinCondicion || tramite || finalizacion)){
            // HAY PLUGIN DE TRAMITACIÓN EXTERNO SELECCIONADO, PERO LA CONDICIÓN DE SALIDA DEL TRÁMITE NO ES DE ESTOS TIPOS: Sin condición de salida, Trámite o Finalización            
            exito = true;
        }
    }

    return exito;
}

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

function activarCapaNotificacionesElectronicas(){
    document.forms[0].admiteNotificacionElectronica.disabled = false;
    document.forms[0].notificacionElectronicaObligatoria.disabled = false;
    document.forms[0].certificadoOrganismoFirmaNotificacion.disabled = false;
    comboAdmiteNotifElect.activate();
    comboDepartamento.activate();
}

function desactivarCapaNotificacionesElectronicas(){
    document.forms[0].admiteNotificacionElectronica.disabled = true;
    document.forms[0].notificacionElectronicaObligatoria.disabled = true;
    document.forms[0].certificadoOrganismoFirmaNotificacion.disabled = true;
    comboAdmiteNotifElect.deactivate();
    comboDepartamento.deactivate();
}

function activarTramiteNotificado(valor){
    if(valor==true){
        activarCapaNotificacionesElectronicas();
        comprobarAdmiteNotificacion();
    } else {
        var existeCheckSel = false;
        $('#capaNotificacionesElectronicas input[type="checkbox"]').each(function(){
            if($(this).is(":checked")) {
                existeCheckSel = true;
                return false;
            }
        });
        if(!existeCheckSel)
            desactivarCapaNotificacionesElectronicas();
        else
            $('#capaTramiteNotificado input[name="tramiteNotificado"]').prop("checked","checked");
    }
}

function comprobarAdmiteNotificacion(){
    if(document.forms[0].admiteNotificacionElectronica.disabled==false){
        if(document.forms[0].admiteNotificacionElectronica.checked==true) { 
            comboAdmiteNotifElect.activate();
            comboDepartamento.activate();
        } else {
            comboAdmiteNotifElect.deactivate();
            comboDepartamento.deactivate();
            document.forms[0].codDepartamentoNotificacion.value="";
            document.forms[0].descDepartamentoNotificacion.value="";
        }
         var arrayNEO = document.getElementsByName("notificacionElectronicaObligatoria");
        for (k =0; k < arrayNEO.length; k++){
            if (arrayNEO[k]){
                arrayNEO[k].disabled =  !(document.forms[0].admiteNotificacionElectronica.checked);
            }
        }
    }
}


function comprobarDestinatariosNofificacionFinPlazo(){
    if(document.forms[0].notificarCercaFinPlazo.checked==true || document.forms[0].notificarFueraDePlazo.checked==true){
        if( document.forms[0].checkNotUsuTraFinPlazo.checked==false && document.forms[0].checkNotUsuExpFinPlazo.checked==false && document.forms[0].checkNotUORFinPlazo.checked==false){
            return true;
        }else {
            return false;
        }
    }
}

function comprobarNotificacionesFuncionPlazo(){
if( document.forms[0].checkNotUsuTraFinPlazo.checked==true || document.forms[0].checkNotUsuExpFinPlazo.checked==true || document.forms[0].checkNotUORFinPlazo.checked==true){
    if(document.forms[0].notificarCercaFinPlazo.checked==false & document.forms[0].notificarFueraDePlazo.checked==false){
        return true;
    } else {
        return false;
    }

   }    
}

function activarDestinatariosFuncionPlazo(){
    if(document.forms[0].notificarCercaFinPlazo.checked || document.forms[0].notificarFueraDePlazo.checked){
        if(document.forms[0].notificarCercaFinPlazo.disabled==false){
            document.forms[0].checkNotUsuTraFinPlazo.disabled=false;
            document.forms[0].checkNotUORFinPlazo.disabled=false;
            document.forms[0].checkNotUsuExpFinPlazo.disabled=false;
        }
    }else{
        document.forms[0].checkNotUsuTraFinPlazo.checked=false;
        document.forms[0].checkNotUORFinPlazo.checked=false;
        document.forms[0].checkNotUsuExpFinPlazo.checked=false;
        document.forms[0].checkNotUsuTraFinPlazo.disabled=true;
        document.forms[0].checkNotUORFinPlazo.disabled=true;
        document.forms[0].checkNotUsuExpFinPlazo.disabled=true;
    }
}

function pulsarVerEtiquetas() {    
    var source = APP_CONTEXT_PATH + '/jsp/editor/definicionDocumentoTramite.jsp?listaEtiquetas=SI';
    abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + source ,null,
        'width=700,height=500,scrollbars=no,status=no',function(arrayResp){

    if (arrayResp != undefined && arrayResp != null){
        var url = APP_CONTEXT_PATH + "/editor/DocumentosAplicacion.do";
        var datos = {
            'codProcedimiento' : document.forms[0].txtCodigo.value,
            'codTramite' : document.forms[0].codigoTramite.value,
            'codDocumento' : "",
            'nombreDocumento' : "",
            'docActivo' : "SI",
            'relacion' : arrayResp[0],
            'interesado' : arrayResp[1],
            'editorTexto' : arrayResp[2],
            'opcion' : "verEtiquetasDocumento"
        };
        
        recuperarEtiquetas(url,datos);
   }
  });
}

function recuperarEtiquetas(url,datos){
    pleaseWait('on');
    try{
        $.ajax({
            url:  url,
            type: 'POST',
            async: true,
            data: datos,
            success: procesarRespuestaRecuperarEtiquetas,
            error: mostrarErrorRespuestaRecuperarEtiquetas
        });           
    }catch(Err){
        pleaseWait('off');
        mostrarErrorCargarEtiquetas(3);
    }
}

function procesarRespuestaRecuperarEtiquetas(ajaxResult){
        pleaseWait('off');
        var respuesta = JSON.parse(ajaxResult);
        var datos = respuesta.tabla;
        var error = datos.error;
        var paraInteresado = datos.paraInteresado;
        
        if(error===0){
            var source = APP_CONTEXT_PATH + '/jsp/sge/documentos/relacionEtiquetas.jsp?paraInteresado='+paraInteresado;
            abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + source ,datos.etiquetas,
                'width=1100,height=650,scrollbars=no,status=no',function(){
            });
        } else {
            mostrarErrorCargarEtiquetas(error);
        }
        
}

function mostrarErrorRespuestaRecuperarEtiquetas(){
    mostrarErrorCargarEtiquetas(3);
}