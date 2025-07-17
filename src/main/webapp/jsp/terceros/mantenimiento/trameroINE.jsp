<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="java.util.Vector"%>

<%@ page contentType="text/html;charset=ISO_8859-1"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title> Mantenimiento de Trameros INE </title>
        <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            ParametrosTerceroValueObject ptVO = null;
            int idioma = 1;
            int apl = 3;
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
                apl = usuarioVO.getAppCod();
                ptVO = (ParametrosTerceroValueObject) session.getAttribute("parametrosTercero");
            }
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        
        <!-- Ficheros JavaScript -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <link rel="stylesheet" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>" type="text/css">
        <script type="text/javascript">
            // VARIABLES GLOBALES
                 <%
            MantenimientosTercerosForm mantForm = (MantenimientosTercerosForm) session.getAttribute("MantenimientosTercerosForm");
    %>  
        var ventana = "<%=mantForm.getVentana()%>";    
        var codECOESIs = new Array();
        var codECOESIsOld = new Array();
        var codESIs = new Array();
        var descESIs = new Array();
        var codECOESINUCs = new Array();
        var codECOESINUCsOld = new Array();
        var codNUCs = new Array();
        var descNUCs = new Array();
        var codESIsOld = new Array();
        var descESIsOld = new Array();
        var codNUCsOld = new Array();
        var descNUCsOld = new Array();
        var idVias = new Array();
        var codVias = new Array();
        var descVias = new Array();
        var idViasOld = new Array();
        var codViasOld = new Array();
        var descViasOld = new Array();
        var codECOESIVias = new Array();
        var codECOESIViasOld = new Array();
        
        var codTipoNumeraciones = new Array();
        var descTipoNumeraciones = new Array();
        var codDistritos = new Array();
        var descDistritos = new Array();
        var codSecciones = new Array();
        var letraSecciones = new Array();
        var descSecciones = new Array();
        var codSubSecciones = new Array();
        var descSubSecciones = new Array();
        var codManzanas = new Array();
        var descManzanas = new Array();
        var codPostales = new Array();
        var listaTramosOriginal = new Array();
        var listaTramos = new Array();
        
        // FUNCIONES DE CARGA E INICIALIZACION DE DATOS  
        function recuperaDatosIniciales()
        {
            <%
            Vector listaProvincias = mantForm.getListaProvincias();
            Vector listaNumeraciones = mantForm.getListaNumeraciones();
            Vector listaCodPostales = mantForm.getListaCodPostales();
            int lengthProvs = listaProvincias.size();
            int lengthNumeraciones = listaNumeraciones.size();
            int lengthCodPostales = listaCodPostales.size();
            int i = 0;
%>
    var j=0;
    j=0;
    <%
            for (i = 0; i < lengthNumeraciones; i++) {
%>
    codTipoNumeraciones[j] = "<%=(String) ((GeneralValueObject) listaNumeraciones.get(i)).getAtributo("codigo")%>";
    descTipoNumeraciones[j] = "<%=(String) ((GeneralValueObject) listaNumeraciones.get(i)).getAtributo("descripcion")%>";
    j++;
    <%
            }
%>
    j=0;
    <%
            for (i = 0; i < lengthCodPostales; i++) {
%>
    codPostales[j] ="<%=(String) ((GeneralValueObject) listaCodPostales.get(i)).getAtributo("codPostal")%>";
    j++;
    <%
            }

            // Entidades individuales
            Vector listaESIs = mantForm.getListaEsis();
            int lengthESIs = listaESIs.size();
            String codESIs = "";
            String descESIs = "";
            String codECOESIs = "";
            for (i = 0; i < lengthESIs - 1; i++) {
                GeneralValueObject esis = (GeneralValueObject) listaESIs.get(i);
                codESIs += "\"" + (String) esis.getAtributo("codEntidadSingular") + "\",";
                descESIs += "\"" + (String) esis.getAtributo("nombreOficial") + "\",";
                codECOESIs += "[\"" + (String) esis.getAtributo("codEntidadColectiva") + "\"," +
                        "\"" + (String) esis.getAtributo("descEntidadColectiva") + "\"," +
                        "\"" + (String) esis.getAtributo("codEntidadSingular") + "\"," +
                        "\"" + (String) esis.getAtributo("nombreOficial") + "\"],";
            }
            if (lengthESIs > 0) {
                GeneralValueObject esis = (GeneralValueObject) listaESIs.get(i);
                codESIs += "\"" + (String) esis.getAtributo("codEntidadSingular") + "\"";
                descESIs += "\"" + (String) esis.getAtributo("nombreOficial") + "\"";
                codECOESIs += "[\"" + (String) esis.getAtributo("codEntidadColectiva") + "\"," +
                        "\"" + (String) esis.getAtributo("descEntidadColectiva") + "\"," +
                        "\"" + (String) esis.getAtributo("codEntidadSingular") + "\"," +
                        "\"" + (String) esis.getAtributo("nombreOficial") + "\"]";
            }

            // Nucleos
            Vector listaNUCs = mantForm.getListaNucleos();
            int lengthNUCs = listaNUCs.size();
            String codNUCs = "";
            String descNUCs = "";
            String codECOESINUCs = "";
            for (i = 0; i < lengthNUCs - 1; i++) {
                GeneralValueObject nucs = (GeneralValueObject) listaNUCs.get(i);
                codNUCs += "\"" + (String) nucs.getAtributo("codNUC") + "\",";
                descNUCs += "\"" + (String) nucs.getAtributo("descNUC") + "\",";
                codECOESINUCs += "[\"" + (String) nucs.getAtributo("codECO") + "\"," +
                        "\"" + (String) nucs.getAtributo("descECO") + "\"," +
                        "\"" + (String) nucs.getAtributo("codESI") + "\"," +
                        "\"" + (String) nucs.getAtributo("descESI") + "\"],";
            }
            if (lengthNUCs > 0) {
                GeneralValueObject nucs = (GeneralValueObject) listaNUCs.get(i);
                codNUCs += "\"" + (String) nucs.getAtributo("codNUC") + "\"";
                descNUCs += "\"" + (String) nucs.getAtributo("descNUC") + "\"";
                codECOESINUCs += "[\"" + (String) nucs.getAtributo("codECO") + "\"," +
                        "\"" + (String) nucs.getAtributo("descECO") + "\"," +
                        "\"" + (String) nucs.getAtributo("codESI") + "\"," +
                        "\"" + (String) nucs.getAtributo("descESI") + "\"]";
            }
            // Vias
            Vector listaVias = mantForm.getListaVias();
            int lengthVias = listaVias.size();
            String idVias = "";
            String codVias = "";
            String descVias = "";
            String codECOESIVias = "";
            for (i = 0; i < lengthVias - 1; i++) {
                GeneralValueObject vias = (GeneralValueObject) listaVias.get(i);
                idVias += "\"" + (String) vias.getAtributo("idVia") + "\",";
                codVias += "\"" + (String) vias.getAtributo("codVia") + "\",";
                descVias += "\"" + (String) vias.getAtributo("descVia") + "\",";
                codECOESIVias += "[\"" + (String) vias.getAtributo("codECO") + "\"," +
                        "\"" + (String) vias.getAtributo("descECO") + "\"," +
                        "\"" + (String) vias.getAtributo("codESI") + "\"," +
                        "\"" + (String) vias.getAtributo("descESI") + "\"," +
                        "\"" + (String) vias.getAtributo("codNUC") + "\"," +
                        "\"" + (String) vias.getAtributo("descNUC") + "\"],";
            }
            if (lengthVias > 0) {
                GeneralValueObject vias = (GeneralValueObject) listaVias.get(i);
                idVias += "\"" + (String) vias.getAtributo("idVia") + "\"";
                codVias += "\"" + (String) vias.getAtributo("codVia") + "\"";
                descVias += "\"" + (String) vias.getAtributo("descVia") + "\"";
                codECOESIVias += "[\"" + (String) vias.getAtributo("codECO") + "\"," +
                        "\"" + (String) vias.getAtributo("descECO") + "\"," +
                        "\"" + (String) vias.getAtributo("codESI") + "\"," +
                        "\"" + (String) vias.getAtributo("descESI") + "\"," +
                        "\"" + (String) vias.getAtributo("codNUC") + "\"," +
                        "\"" + (String) vias.getAtributo("descNUC") + "\"]";
            }
            Vector listaDistritos = mantForm.getListaDistritos();
            int lengthDistritos = listaDistritos.size();
            String codDistritos = "";
            String descDistritos = "";
            if (lengthDistritos > 0) {
                for (i = 0; i < lengthDistritos - 1; i++) {
                    GeneralValueObject distritos = (GeneralValueObject) listaDistritos.get(i);
                    codDistritos += "[\"" + (String) distritos.getAtributo("codDistrito") + "\"],";
                    descDistritos += "[\"" + (String) distritos.getAtributo("descDistrito") + "\"],";
                }
                GeneralValueObject distritos = (GeneralValueObject) listaDistritos.get(i);
                codDistritos += "[\"" + (String) distritos.getAtributo("codDistrito") + "\"]";
                descDistritos += "[\"" + (String) distritos.getAtributo("descDistrito") + "\"]";
            }
%>
    codESIs = [<%=codESIs%>];
        descESIs = [<%=descESIs%>];
            codECOESIs = [<%=codECOESIs%>];
                codECOESIsOld = [<%=codECOESIs%>];
                    codECOESINUCs = [<%=codECOESINUCs%>];
                        codECOESINUCsOld = [<%=codECOESINUCs%>];
                            codNUCs = [<%=codNUCs%>];
                                descNUCs = [<%=descNUCs%>];
                                    idVias = [<%=idVias%>];
                                        codVias = [<%=codVias%>];
                                            descVias = [<%=descVias%>];
                                                codECOESIVias = [<%=codECOESIVias%>];
                                                    codDistritos = [<%=codDistritos%>];
                                                        descDistritos = [<%=descDistritos%>];
                                                            codESIsOld = codESIs;
                                                            descESIsOld = descESIs;
                                                            codNUCsOld = codNUCs;
                                                            descNUCsOld = descNUCs;
                                                            idViasOld = idVias;
                                                            codViasOld = codVias;
                                                            descViasOld = descVias;
                                                            codECOESIViasOld = codECOESIVias;      
                                                        }
                                                        
                                                        function inicializar(){
                                                            window.focus();
                                                            recuperaDatosIniciales();
                                                            valoresPorDefecto();
                                                            pulsarCancelarBuscar();
                                                            if(ventana=="false"){
                                                                pleaseWait1("off",top.mainFrame);
                                                            }else{
                                                            pleaseWait1("off",top1.mainFrame1);
                                                            var parametros = self.parent.opener.xanelaAuxiliarArgs;
                                                            rellenarCamposBusqueda(parametros);
                                                            pulsarBuscar();
                                                        }
                                                    }
                                                    
                                                    function valoresPorDefecto(){
                                                        document.forms[0].codPais.value ="<%=ptVO.getPais()%>";
                                                        document.forms[0].codProvincia.value = "<%=ptVO.getProvincia()%>";
                                                        document.forms[0].codMunicipio.value ="<%=ptVO.getMunicipio()%>";
                                                        codESIs = codESIsOld;
                                                        descESIs = descESIsOld;
                                                        codECOESIs = codECOESIsOld;
                                                        codECOESINUCs = codECOESINUCsOld;
                                                        codNUCs = codNUCsOld;
                                                        descNUCs = descNUCsOld;
                                                        idVias = idViasOld;
                                                        codVias = codViasOld;
                                                        descVias = descViasOld;
                                                        codECOESIVias = codECOESIViasOld;
                                                        comboVia.addItems(codVias,descVias);
                                                        comboTipoNumeracion.addItems(codTipoNumeraciones,descTipoNumeraciones);
                                                        comboPostal.addItems(codPostales,codPostales);
                                                        comboDistrito.addItems(codDistritos,descDistritos);
                                                        
                                                        comboESITramo.addItems(codESIs,descESIs);
                                                        codsNUCTramo = codNUCs;
                                                        descsNUCTramo = descNUCs;  
                                                        codECOESINUCTramo = codECOESINUCs;
                                                    }
                                                    
                                                    function pulsarCancelarBuscar(){					
                                                        limpiarFormulario();
                                                        document.forms[0].codECO.value = "";
                                                        var botonBuscar = ["botonBuscar"];
                                                        habilitarGeneralInputs(botonBuscar,true);
                                                        habilitarCamposBusqueda(true);
                                                        habilitarCamposRejilla(false);
                                                        valoresPorDefecto();			
                                                        //cargarListaVias();
                                                    }
                                                    
                                                    // FUNCIONES DE LIMPIEZA, HABILITACION Y DESHABILITACION DE CAMPOS
                                                    var vectorCamposBusqueda = ["codVia","descVia","codTipoNumeracion","descTipoNumeracion"];
                                                    var vectorCamposRejilla1 = ["txtNumDesde","txtLetraDesde","txtNumHasta","txtLetraHasta","situacion"];
                                                    var vectorCamposRejilla = ["txtNumDesde","txtLetraDesde","txtNumHasta","txtLetraHasta",
                                                        "codDistrito","descDistrito","codSeccion1","descSeccion1","codSubSeccion","descSubSeccion",			
                                                        "descPostal","situacion"];
                                                    var vectorCombosBusqueda = ["comboVia","comboTipoNumeracion"];
                                                    var vectorCombosRejilla = ["comboDistrito","comboSeccion1","comboSubSeccion","comboPostal"];
                                                    var vectorBotones = ["botonAlta","botonModificar","botonBorrar","botonLimpiar"];
                                                    var vectorCombosEsiNucTramo = ["comboESITramo","comboNUCTramo"];
                                                    
                                                    function habilitarCamposBusqueda(habilitar){
                                                        habilitarGeneralCombos(vectorCombosBusqueda,habilitar);
                                                    }
                                                    
                                                    
                                                    function habilitarCamposRejilla(habilitar){
                                                        habilitarGeneralInputs(vectorCamposRejilla1,habilitar);
                                                        habilitarGeneralInputs(vectorBotones,habilitar);
                                                        habilitarGeneralCombos(vectorCombosRejilla,habilitar);
                                                        habilitarGeneralCombos(vectorCombosEsiNucTramo,habilitar);
                                                    }
                                                    
                                                    function limpiarFormulario(){
                                                        limpiarCamposBusqueda();
                                                        limpiarCamposRejilla();
                                                        tablaTramos.lineas = new Array();
                                                        refresca(tablaTramos);
                                                    }
                                                    
                                                    function limpiarCamposBusqueda(){
                                                        limpiar(vectorCamposBusqueda);
                                                    }
                                                    
                                                    function limpiarCamposRejilla(){
                                                        limpiar(vectorCamposRejilla);
                                                        document.forms[0].codESITramo.value = "";
                                                        document.forms[0].descESITramo.value = "";
                                                        document.forms[0].codNUCTramo.value = "";
                                                        document.forms[0].descNUCTramo.value = "";
                                                    }
                                                    
                                                    function limpiaVial(){
                                                        var codVia = document.forms[0].codVia.value;
                                                        if (codVia!=""){
                                                            document.forms[0].descVia.value = "";
                                                            document.forms[0].codVia.value = "";
                                                        }
                                                    }
                                                    
                                                    function borrarNUC(){
                                                        var vector = new Array();
                                                        vector = ["codNUC","descNUC"];
                                                        limpiar(vector);
                                                    }
                                                    
                                                    function borrarSeccion(){
                                                        var vector = new Array();
                                                        vector = ["codSeccion1","descSeccion1"];
                                                        limpiar(vector);
                                                    }
                                                    
                                                    function borrarSubSeccion(){
                                                        var vector = new Array();
                                                        vector = ["codSubSeccion","descSubSeccion"];
                                                        limpiar(vector);
                                                    }
                                                    
                                                    
                                                    // FUNCIONES DE CARGA DE DATOS DINAMICA
                                                    function cargarListas(){
                                                        document.forms[0].opcion.value="cargarListas";
                                                        document.forms[0].target="oculto";
                                                        document.forms[0].action="<%=request.getContextPath()%>/padron/Gestion.do";
                                                        document.forms[0].submit();
                                                    }
                                                    
                                                    function cargarListaMunicipios(){	
                                                        document.forms[0].opcion.value="cargarMunicipios";
                                                        document.forms[0].target="oculto";
                                                        document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Trameros.do";
                                                        document.forms[0].submit();
                                                    }
                                                    
                                                    function cargarListaNUCs(){
                                                        document.forms[0].opcion.value="cargarNucleos";
                                                        document.forms[0].target="oculto";
                                                        document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Trameros.do";
                                                        document.forms[0].submit();
                                                    }	
                                                    
                                                    function cargarListaVias(){
                                                        document.forms[0].opcion.value="cargarVias";
                                                        document.forms[0].target="oculto";
                                                        document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Trameros.do";
                                                        document.forms[0].submit();
                                                    }
                                                    
                                                    function cargarListaDistritos(){
                                                        document.forms[0].opcion.value="cargarDistritos";
                                                        document.forms[0].target="oculto";
                                                        document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Trameros.do";
                                                        document.forms[0].submit();
                                                    }
                                                    
                                                    function cargarListaSecciones(){
                                                        habilitarCamposBusqueda(true);
                                                        document.forms[0].opcion.value="cargarSecciones";
                                                        document.forms[0].target="oculto";
                                                        document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Trameros.do";
                                                        document.forms[0].submit();
                                                        habilitarCamposBusqueda(false);
                                                    }
                                                    
                                                    
                                                    function cargarListaSubSeccion() {
                                                        document.forms[0].codSeccion.value = codSecciones[comboSeccion1.selectedIndex];
                                                        document.forms[0].letraSeccion.value = letraSecciones[comboSeccion1.selectedIndex];
                                                        document.forms[0].opcion.value="cargarSubSecciones";
                                                        document.forms[0].target="oculto";
                                                        document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Trameros.do";
                                                        document.forms[0].submit();
                                                    }	
                                                    
                                                    
                                                    function cargarListaTramos(lista){
                                                        limpiarCamposRejilla();
                                                        listaTramos = lista;
                                                        tablaTramos.lineas = lista;
                                                        refresca(tablaTramos);
                                                        habilitarCamposRejilla(true);
                                                        if(comboTipoNumeracion.cod.value.length!=0){
                                                            if (comboTipoNumeracion.cod.value == 0 ) {
                                                                document.forms[0].txtNumDesde.className="inputTexto";
                                                                document.forms[0].txtNumHasta.className="inputTexto";
                                                            } else {
                                                            document.forms[0].txtNumDesde.className="inputTextoObligatorio";
                                                            document.forms[0].txtNumHasta.className="inputTextoObligatorio";
                                                            
                                                        }
                                                    }
                                                    
                                                    
                                                }
                                                
                                                function cargarSeccion1(list1,list2,list3,list4) {
                                                    codSecciones = list3;
                                                    letraSecciones = list4;
                                                    comboSeccion1.addItems(list1,list2);
                                                    comboSeccion1.buscaCodigo(document.forms[0].codSeccion1.value);
                                                    cargarListaSubSeccion();
                                                }
                                                
                                                function cargarSubSecciones(list1,list2) {
                                                    comboSubSeccion.addItems(list1,list2);
                                                }
                                                
                                                function cargarNUC(list1,list2) {
                                                    if(document.forms[0].codESITramo.value != "") {
                                                        comboNUCTramo.addItems(list1,list2);
                                                    }
                                                }
                                                
                                                function rellenarCamposBusqueda(datos){
                                                    rellenar(datos,vectorCamposBusqueda);
                                                }
                                                
                                                function buscarIdVia(){
                                                    document.forms[0].idVia.value = idVias[comboVia.selectedIndex];
                                                    document.forms[0].idESI.value = codECOESIVias[comboVia.selectedIndex][2];
                                                    document.forms[0].idNucleo.value = codECOESIVias[comboVia.selectedIndex][4];
                                                }
                                                
                                                // FUNCIONES DE VALIDACION DE CAMPOS
                                                function validarCamposBusqueda(){
                                                    var pais = document.forms[0].codPais.value;
                                                    var provincia = document.forms[0].codProvincia.value;
                                                    var municipio = document.forms[0].codMunicipio.value;
                                                    var codVia = document.forms[0].codVia.value;
                                                    var codTipoNumeracion = document.forms[0].codTipoNumeracion.value;
                                                    if((pais!="")&&(provincia!="")&&(municipio!="")&&
                                                        (codVia!=""))
                                                    return true;
                                                    return false;
                                                }
                                                
                                                function validarCamposRejilla(){
                                                    var numDesde = document.forms[0].txtNumDesde.value;
                                                    var letraDesde = document.forms[0].txtLetraDesde.value;
                                                    var numHasta = document.forms[0].txtNumHasta.value;
                                                    var letraHasta = document.forms[0].txtLetraHasta.value;
                                                    var codDistrito = document.forms[0].codDistrito.value;
                                                    var codSeccion = document.forms[0].codSeccion1.value;
                                                    var descPostal = document.forms[0].descPostal.value;
                                                    var codESITRamo = document.forms[0].codESITramo.value;
                                                    var codNUCTramo = document.forms[0].codNUCTramo.value;
                                                    if (comboTipoNumeracion.cod.value!=0){
                                                        if((numDesde=="")||(numHasta=="")) return false;
                                                    }
                                                    if((codDistrito!="")&&(codSeccion!="")&&
                                                        (descPostal!="")&&(codESITRamo!="")&&(codNUCTramo!=""))
                                                    return true;
                                                    return false;
                                                }
                                                
                                                
                                                function validarIntervalo(){
                                                    var numDesde = parseInt(document.forms[0].txtNumDesde.value);
                                                    var numHasta = parseInt(document.forms[0].txtNumHasta.value);
                                                    if (document.forms[0].codTipoNumeracion.value == 1) { // Impar
                                                        if ( (numDesde%2)==0 || (numHasta%2)==0)
                                                            return false;
                                                    } else if (document.forms[0].codTipoNumeracion.value == 2) { // Par
                                                    if ( (numDesde%2)!=0|| (numHasta%2!=0))
                                                        return false;
                                                } else if(document.forms[0].codTipoNumeracion.value == 0 && document.forms[0].codTipoNumeracion.value !="") { // Sin numero
                                                if(document.forms[0].txtNumDesde.value != "" || document.forms[0].txtNumHasta.value != "")
                                                    return false;
                                            }
                                            
                                            var tipoNum = "";
                                            if(document.forms[0].txtNumDesde.value == "") {
                                                tipoNum = "0";
                                            } else if(numDesde%2==0) {
                                            tipoNum = "2";
                                        } else if(numDesde%2!=0) {
                                        tipoNum = "1";
                                    }
                                    
                                    if(document.forms[0].txtNumDesde.value != "") {
                                        if(numDesde%2 != numHasta%2) {
                                            return false;
                                        }
                                    }
                                    
                                    if(tablaTramos.selectedIndex != -1) {
                                        if(tipoNum != listaTramosOriginal[tablaTramos.selectedIndex][0])
                                            return false;
                                    }
                                    
                                    return true;
                                }				
                                
                                function validarNumDesdeHasta(indice){
                                    var numDesde = parseInt(document.forms[0].txtNumDesde.value);
                                    var letraDesde = document.forms[0].txtLetraDesde.value;
                                    var numHasta = parseInt(document.forms[0].txtNumHasta.value);
                                    var letraHasta = document.forms[0].txtLetraHasta.value;
                                    var tipoNum = "";
                                    if(document.forms[0].txtNumDesde.value == "") {
                                        tipoNum = "0";
                                    } else if(numDesde%2==0) {
                                    tipoNum = "2";
                                } else if(numDesde%2!=0) {
                                tipoNum = "1";
                            }
                            if (numDesde > numHasta)
                                return false;
                            
                            var numeroTramos = listaTramosOriginal.length;
                            for(i=0;i<numeroTramos;i++){
                                if(i!=indice && listaTramosOriginal[i][32] !='B' && tipoNum==listaTramosOriginal[i][0]){										
                                    var numDTramo = parseInt(listaTramosOriginal[i][27]);
                                    var letraDTramo = listaTramosOriginal[i][28];
                                    var numHTramo = parseInt(listaTramosOriginal[i][29]);
                                    var letraHTramo = listaTramosOriginal[i][30];	
                                    if (!( (numHasta<numDTramo)||(numDesde>numHTramo) ) ) {
                                        if((numDesde==numDTramo)&&(numHTramo==numHasta)){
                                            if(((letraDesde<=letraDTramo)&&(letraDTramo<=letraHasta))||
                                                ((letraDesde<=letraHTramo)&&(letraHTramo<=letraHasta))){
                                            return false;
                                        } else return true;
                                    }else return false;
                                }
                            } 
                        }
                        return true;
                    }
                    
                    // FUNCIONES DE PULSACION DE BOTONES
                    function pulsarBuscar(){
                        var botonBuscar = ["botonBuscar"];
                        if(validarCamposBusqueda()){
                            buscarIdVia();
                            document.forms[0].opcion.value="cargarTramos";
                            document.forms[0].target="oculto";
                            document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Trameros.do";
                            document.forms[0].submit();
                            habilitarGeneralInputs(botonBuscar,false);
                            habilitarCamposBusqueda(false);
                            //habilitarCamposRejilla(true);
                        }else
                        jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>")
                        }
                        
                        
                        function pulsarAlta(){
                            if(validarCamposRejilla()){
                                if(validarIntervalo() && validarNumDesdeHasta()){
                                    habilitarCamposBusqueda(true);
                                    if(document.forms[0].codTipoNumeracion.value == "") {
                                        var numDesde = parseInt(document.forms[0].txtNumDesde.value);
                                        if(document.forms[0].txtNumDesde.value == "") {
                                            document.forms[0].tipoNumeracion.value = "0";
                                        } else if(numDesde%2==0) {
                                        document.forms[0].tipoNumeracion.value = "2";
                                    } else if(numDesde%2!=0) {
                                    document.forms[0].tipoNumeracion.value = "1";
                                }
                            } else {
                            document.forms[0].tipoNumeracion.value = document.forms[0].codTipoNumeracion.value;
                        }
                        document.forms[0].codSeccion.value = codSecciones[comboSeccion1.selectedIndex];
                        document.forms[0].letraSeccion.value = letraSecciones[comboSeccion1.selectedIndex];
                        document.forms[0].opcion.value="alta";
                        document.forms[0].target="oculto";
                        document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Trameros.do";
                        document.forms[0].submit();
                        habilitarCamposBusqueda(false);
                    }else jsp_alerta("A","<%=descriptor.getDescripcion("msjIntervNumInco")%>");
                    }else jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>")
                    }
                    
                    function pulsarModificar(){
                        if(tablaTramos.selectedIndex != -1) {
                            if(validarCamposRejilla()){
                                if(validarIntervalo() && validarNumDesdeHasta(tablaTramos.selectedIndex)){
                                    habilitarCamposBusqueda(true);
                                    if(document.forms[0].codTipoNumeracion.value == "") {
                                        var numDesde = parseInt(document.forms[0].txtNumDesde.value);
                                        if(document.forms[0].txtNumDesde.value == "") {
                                            document.forms[0].tipoNumeracion.value = "0";
                                        } else if(numDesde%2==0) {
                                        document.forms[0].tipoNumeracion.value = "2";
                                    } else if(numDesde%2!=0) {
                                    document.forms[0].tipoNumeracion.value = "1";
                                }
                            } else {
                            document.forms[0].tipoNumeracion.value = document.forms[0].codTipoNumeracion.value;
                        }
                        document.forms[0].codSeccion.value = codSecciones[comboSeccion1.selectedIndex];
                        document.forms[0].letraSeccion.value = letraSecciones[comboSeccion1.selectedIndex];
                        document.forms[0].opcion.value="modificar";
                        document.forms[0].target="oculto";
                        document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Trameros.do";
                        document.forms[0].submit();
                        habilitarCamposBusqueda(false);
                    }else
                    jsp_alerta("A","<%=descriptor.getDescripcion("msjIntervNumInco")%>");
                    }else
                    jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                    } else 
                    jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
                    }
                    
                    function pulsarBorrar(){
                        if(tablaTramos.selectedIndex != -1) {
                            if(validarCamposBusqueda()){
                                if(jsp_alerta("C",'<%=descriptor.getDescripcion("desElimTram")%>') ==1) {
                                    habilitarCamposBusqueda(true);
                                    if(document.forms[0].codTipoNumeracion.value == "") {
                                        var numDesde = parseInt(document.forms[0].txtNumDesde.value);
                                        if(document.forms[0].txtNumDesde.value == "") {
                                            document.forms[0].tipoNumeracion.value = "0";
                                        } else if(numDesde%2==0) {
                                        document.forms[0].tipoNumeracion.value = "2";
                                    } else if(numDesde%2!=0) {
                                    document.forms[0].tipoNumeracion.value = "1";
                                }
                            } else {
                            document.forms[0].tipoNumeracion.value = document.forms[0].codTipoNumeracion.value;
                        }
                        document.forms[0].codSeccion.value = codSecciones[comboSeccion1.selectedIndex];
                        document.forms[0].letraSeccion.value = letraSecciones[comboSeccion1.selectedIndex];
                        document.forms[0].opcion.value="eliminar";
                        document.forms[0].target="oculto";
                        document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Trameros.do";
                        document.forms[0].submit();
                        habilitarCamposBusqueda(false);
                        limpiarCamposRejilla();
                    }
                }else
                jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                } else 
                jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
                }
                
                function pulsarLimpiar(){
                    limpiarCamposRejilla();
                    comboSeccion1.addItems([],[]);
                    comboSubSeccion.addItems([],[]);
                    comboNUCTramo.addItems([],[]);
                    comboDistrito.selectedIndex = -1;
                    comboESITramo.selectedIndex = -1;
                    if(tablaTramos.selectedIndex != -1 ) {
                        tablaTramos.selectLinea(tablaTramos.selectedIndex);
                        tablaTramos.selectedIndex = -1;
                    }
                }
                
                function pulsarSalir(){
                    if(ventana=="false"){
                        document.forms[0].opcion.value="inicializarTerc";
                        document.forms[0].target="mainFrame";
                        document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                        document.forms[0].submit();
                    }else{
                    var datosRetorno;
                    if(indice>-1)
                        datosRetorno = listaTramosOriginal[indice];
                    self.parent.opener.retornoXanelaAuxiliar(datosRetorno);
                }
            }
            
            var codECOESINUCTramo = new Array();
            var codsNUCTramo = new Array();
            var descsNUCTramo = new Array();
            
            var esperarListaSecc = false;
            var esperarListaNUC = false;
            
            function actualizarSeccion(){
                esperarListaSecc = true;
                cargarListaSecciones();    	
            }
            
            function noEliminarTram() {
                jsp_alerta("A","<%=descriptor.getDescripcion("msjNoElimTram")%>");
                }
        </script>
    </head>
    
    <body class="bandaBody" onLoad="inicializar();">
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        <form action="" method="get" name="formulario" target="_self">
            <input type="hidden" name="opcion">
            <input type="hidden" name="codPais" size="3" value="<%=ptVO.getPais()%>">
            <input type="hidden" name="idVia" value="">
            <input type="hidden" name="idESI" value="">
            <input type="hidden" name="idNucleo" value="">
            <input type="hidden" name="codTramo" value="">
            <input type="hidden" name="codSeccion" value="">
            <input type="hidden" name="letraSeccion" value="">
            <input type="hidden" name="ventana" value="<%=mantForm.getVentana()%>">
            <input name="codECO" type="hidden" class="inputTexto" id="codECO" size="3"> 
            <input name="descECO" type="hidden" class="inputTexto" id="descECO" size="3"> 
            <input type="hidden" name="codManzana" value=""> 
            <input type="hidden" name="descManzana" value="">
            <input type="hidden" name="tipoNumeracion" value="">
            <input type="hidden" name="codProvincia"/>
            <input type="hidden" name="codMunicipio"/>
            
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_TrameroINE")%></div>
    <div class="contenidoPantalla">
        <table>						
            <tr>
                <td>
                    <table border="0" width="100%" cellspacing="5px" cellpadding="0px">
                        <tr>
                            <td>
                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td style="width: 65%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Vial")%></td>
                                        <td style="width: 35%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_TipoNum")%></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td style="width: 65%" class="etiqueta">
                                            <input type="text" class="inputTextoObligatorio" size=3 id="codVia" name="codVia">
                                            <input id="descVia" name="descVia" type="text" class="inputTextoObligatorio" readonly="true" style="width:450" maxlength=50>
                                            <a href="" name="anchorVia" id="anchorVia">
                                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonVia" name="botonVia" style="cursor:hand;"></span>
                                            </a>
                                        </td>
                                        <td style="width: 35%" align="left" class="etiqueta">
                                            <input id="codTipoNumeracion" name="codTipoNumeracion" type="text" class="inputTexto" size="3"> 
                                            <input name="descTipoNumeracion" type="text" class="inputTexto"   style="width:250" readonly> 
                                            <a id="anchorTipoNumeracion" name="anchorTipoNumeracion" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoNumeracion" name="botonTipoNumeracion" style="cursor:hand;"></span>
                                            </a>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 10px"></td>
                        </tr>
                        <tr> 
                            <td> 
                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td align="right">
                                            <input name="botonBuscar" type="button"  class="botonGeneral" id="botonBuscar" value="<%=descriptor.getDescripcion("gbBuscar")%>" onClick="pulsarBuscar();" accesskey="B">
                                            <input name="botonCancelar" type="button" class="botonGeneral" id="botonCancelar" value="<%=descriptor.getDescripcion("gbCancelar")%>" onClick="pulsarCancelarBuscar();" accesskey="C">
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

            <tr>
                <td style="height: 15px"></td>
            </tr>
            <tr>
                <td> 
                    <table rules="cols"  border="0" cellspacing="0" cellpadding="2">                      
                        <tr>					  
                            <td id="tablaTramos" style="border:none"></td>
                        </tr>
                        <tr>					  
                            <td colspan="9" >
                                <table style="width: 100%" rules="none"  border="0px" cellpadding="0" cellspacing="0">
                                    <tr>
                                        <td style="width: 36px;vertical-align:bottom" align="center">
                                            <input name="txtNumDesde" type="text" class="inputTextoObligatorio" id="numero" size=2 maxlength=4 onkeyup = "return SoloDigitosNumericos(this);">
                                        </td>
                                        <td style="width: 30px;vertical-align:bottom" align="center">
                                            <input name="txtLetraDesde" type="text" class="inputTexto" id="txtLetraDesde" size=2 maxlength=1 onkeyup="return xAMayusculas(this);">
                                        </td>
                                        <td style="width: 36px;vertical-align:bottom" align="center">
                                            <input name="txtNumHasta" type="text" class="inputTextoObligatorio" id="numero" size=2 maxlength=4 onkeyup = "return SoloDigitosNumericos(this);">
                                        </td>
                                        <td style="width: 32px;vertical-align:bottom"  align="center">
                                            <input name="txtLetraHasta" type="text" class="inputTexto" id="txtLetraHasta" size=2 maxlength=1 onkeyup="return xAMayusculas(this);">
                                        </td>
                                        <td style="width: 144px;vertical-align:bottom"  align="center">
                                            <input class="inputTextoObligatorio" type="text" id="codDistrito" name="codDistrito" 
                                                   size="3"> 
                                            <input id="descDistrito" name="descDistrito" type="text" class="inputTextoObligatorio"  
                                                   style="width:74" readonly>
                                            <a id="anchorDistrito" name="anchorDistrito" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonDistrito" name="botonDistrito"
                                                style="cursor:hand;"></span></a>
                                        </td>
                                        <td style="width: 144px"  align="center">
                                            <input class="inputTextoObligatorio" type="text" id="codSeccion1" name="codSeccion1" 
                                                   size="3">
                                            <input id="descSeccion1" name="descSeccion1" type="text" class="inputTextoObligatorio"  
                                                   style="width:74" readonly>
                                            <a id="anchorSeccion1" name="anchorSeccion1" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonSeccion1" name="botonSeccion1"
                                                    style="cursor:hand;"></span>
                                            </a>
                                        </td>
                                        <td style="width: 144px"  align="center">
                                            <input class="inputTexto" type="text" id="codSubSeccion" name="codSubSeccion" size="3"> 
                                            <input id="descSubSeccion" name="descSubSeccion" type="text" class="inputTexto" 
                                                   style="width:74" readonly>
                                            <a id="anchorSubSeccion" name="anchorSubSeccion" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonSubSeccion" name="botonSubSeccion"
                                                    style="cursor:hand;"></span>
                                            </a>
                                        </td>
                                        <td style="width: 90px"  align="center">
                                            <input type="text" class="inputTextoObligatorio" style="width:54" maxlength=5  
                                                   id="descPostal" name="descPostal">
                                            <a id="anchorPostal" name="anchorPostal" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonPostal" name="botonPostal"
                                                    style="cursor:hand;"></span>
                                            </a>
                                        </td>
                                        <td style="width: 154px" align="center">
                                            <input class="inputTextoObligatorio" type="text" id="obligatorio" name="codESITramo" size="3"> 
                                            <input id="obligatorio" name="descESITramo" type="text" class="inputTextoObligatorio"  style="width:80px" readonly>
                                            <a id="anchorESITramo" name="anchorESITramo" href="">
                                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonESITramo" name="botonESITramo" style="cursor:hand;"></span>
                                            </a>
                                        </td>
                                        <td style="width: 154px" align="center">
                                            <input class="inputTextoObligatorio" type="text" id="obligatorio" name="codNUCTramo" size="3"> 
                                            <input id="obligatorio" name="descNUCTramo" type="text" class="inputTextoObligatorio"  style="width:80px" readonly>
                                            <a id="anchorNUCTramo" name="anchorNUCTramo" href="">
                                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonNUCTramo" name="botonNUCTramo" style="cursor:hand;"></span>
                                            </a>
                                        </td>
                                        <td style="width: 28px" align="center">
                                            <input name="situacion" type="text" class="inputTexto" id="situacion" size="2" readonly>
                                        </td>
                                    </tr>                                                                                                    
                                </table>								
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input type="button" class="botonGeneral" name="botonAlta" onClick="pulsarAlta();" accesskey="A" value="<%=descriptor.getDescripcion("gbAlta")%>">
            <input type="button" class="botonGeneral" name="botonModificar" onClick="pulsarModificar();" accesskey="M" value="<%=descriptor.getDescripcion("gbModificar")%>">
            <input type="button" class="botonGeneral" name="botonBorrar" onClick="pulsarBorrar();" accesskey="E" value="<%=descriptor.getDescripcion("gbEliminar")%>"> 
            <input type="button" class="botonGeneral"  name="botonLimpiar"	onClick="pulsarLimpiar();" accesskey="L" value="<%=descriptor.getDescripcion("gbLimpiar")%>">
            <input type="button" class="botonGeneral" name="botonSalir" onClick="pulsarSalir();" accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>">
        </div>             
    </div>             
</form>
<script type="text/javascript">
            var indice;
            var tablaTramos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaTramos"),954);
            
            tablaTramos.addColumna("30","center","<%=descriptor.getDescripcion("manTer_EtiqNLD")%>");
            tablaTramos.addColumna("25","center","<%=descriptor.getDescripcion("manTer_EtiqLet")%>");
            tablaTramos.addColumna("30","center","<%=descriptor.getDescripcion("manTer_EtiqNLH")%>");
            tablaTramos.addColumna("25","center","<%=descriptor.getDescripcion("manTer_EtiqLet")%>");
            tablaTramos.addColumna("135","center","<%=descriptor.getDescripcion("gEtiq_Distrito")%>");
            tablaTramos.addColumna("135","center","<%=descriptor.getDescripcion("gEtiq_Seccion")%>");
            tablaTramos.addColumna("135","center","<%=descriptor.getDescripcion("gEtiq_SubSeccion")%>");
            tablaTramos.addColumna("80","center","<%=descriptor.getDescripcion("gEtiqCodPostal")%>");
            tablaTramos.addColumna("135","center","<%=descriptor.getDescripcion("gEtiq_ESI")%>");
            tablaTramos.addColumna("135","center","<%=descriptor.getDescripcion("gEtiq_NUC")%>");
            tablaTramos.addColumna("23","center","<%=descriptor.getDescripcion("gEtiq_Situacion")%>");
            tablaTramos.displayCabecera=true;
            tablaTramos.displayTabla();
                                                        
                                                        function refresca(tabla){
                                                            tabla.displayTabla();
                                                        }
                                                        
                                                        function rellenarDatos(tableName,rowID){
                                                            if(tablaTramos==tableName){
                                                                var i=rowID;
                                                                indice = rowID;
                                                                limpiarCamposRejilla();
                                                                if(i>=0 && !tableName.ultimoTable){
                                                                    document.forms[0].txtNumDesde.value = listaTramosOriginal[i][27];
                                                                    document.forms[0].txtLetraDesde.value = listaTramosOriginal[i][28];
                                                                    document.forms[0].txtNumHasta.value = listaTramosOriginal[i][29];
                                                                    document.forms[0].txtLetraHasta.value = listaTramosOriginal[i][30];
                                                                    document.forms[0].codDistrito.value = listaTramosOriginal[i][12];
                                                                    document.forms[0].descDistrito.value = listaTramosOriginal[i][13];
                                                                    document.forms[0].codSeccion1.value = listaTramosOriginal[i][14];
                                                                    document.forms[0].descSeccion1.value = listaTramosOriginal[i][15];
                                                                    document.forms[0].codSubSeccion.value = listaTramosOriginal[i][16];
                                                                    document.forms[0].descSubSeccion.value = listaTramosOriginal[i][17];
                                                                    document.forms[0].descPostal.value = listaTramosOriginal[i][26];
                                                                    document.forms[0].codManzana.value = listaTramosOriginal[i][18];
                                                                    document.forms[0].descManzana.value = listaTramosOriginal[i][19];
                                                                    document.forms[0].codDistrito.value = listaTramosOriginal[i][12];
                                                                    document.forms[0].descDistrito.value = listaTramosOriginal[i][13];	
                                                                    document.forms[0].codESITramo.value = listaTramosOriginal[i][22];
                                                                    document.forms[0].descESITramo.value = listaTramosOriginal[i][23];
                                                                    document.forms[0].codNUCTramo.value = listaTramosOriginal[i][24];
                                                                    document.forms[0].descNUCTramo.value = listaTramosOriginal[i][25];
                                                                    document.forms[0].codSeccion.value = listaTramosOriginal[i][14];
                                                                    document.forms[0].letraSeccion.value = listaTramosOriginal[i][31];	
                                                                    document.forms[0].codTramo.value = listaTramosOriginal[i][11];
                                                                    document.forms[0].situacion.value = listaTramosOriginal[i][32];
                                                                    cargarListaSecciones();
                                                                }
                                                            }
                                                        } 
                                                        
                                                        <%String Agent = request.getHeader("user-agent");%>

                                                        var coordx=0;
                                                        var coordy=0;


                                                        <%if(Agent.indexOf("MSIE")==-1) {%> //Que no sea IE
                                                            window.addEventListener('mousemove', function(e) {
                                                                coordx = e.clientX;
                                                                coordy = e.clientY;
                                                            }, true);
                                                        <%}%>
                                                                                        // FUNCION DE CONTROL DE TECLAS

                                                        document.onmouseup = checkKeys; 
                                                        
                                                        function checkKeysLocal(evento,tecla){
                                                            var teclaAuxiliar = "";
                                                            if(window.event){
                                                                evento         = window.event;
                                                                teclaAuxiliar =  evento.keyCode;
                                                            }else
                                                                teclaAuxiliar =  evento.which;

                                                            //** Esta funcion se debe implementar en cada JSP para particularizar  **//
                                                            //** las acciones a realizar de las distintas combinaciones de teclas  **//
                                                            keyDel(evento);
                                                            if(teclaAuxiliar == 40){
                                                                upDownTable(tablaTramos,listaTramos,teclaAuxiliar);
                                                            }
                                                            if(teclaAuxiliar == 38){
                                                                upDownTable(tablaTramos,listaTramos,teclaAuxiliar);
                                                            }
                                                            if (teclaAuxiliar == 1){
                                                               if (comboVia.base.style.visibility == "visible" && isClickOutCombo(comboVia,coordx,coordy)) setTimeout('comboVia.ocultar()',20);
                                                               if (comboTipoNumeracion.base.style.visibility == "visible" && isClickOutCombo(comboTipoNumeracion,coordx,coordy)) setTimeout('comboTipoNumeracion.ocultar()',20);
                                                               if (comboDistrito.base.style.visibility == "visible" && isClickOutCombo(comboDistrito,coordx,coordy)) setTimeout('comboDistrito.ocultar()',20);
                                                               if (comboSeccion1.base.style.visibility == "visible" && isClickOutCombo(comboSeccion1,coordx,coordy)) setTimeout('comboSeccion1.ocultar()',20);
                                                               if (comboSubSeccion.base.style.visibility == "visible" && isClickOutCombo(comboSubSeccion,coordx,coordy)) setTimeout('comboSubSeccion.ocultar()',20);
                                                               if (comboPostal.base.style.visibility == "visible" && isClickOutCombo(comboPostal,coordx,coordy)) setTimeout('comboPostal.ocultar()',20);
                                                               if (comboESITramo.base.style.visibility == "visible" && isClickOutCombo(comboESITramo,coordx,coordy)) setTimeout('comboESITramo.ocultar()',20);
                                                               if (comboNUCTramo.base.style.visibility == "visible" && isClickOutCombo(combooNUCTramo,coordx,coordy)) setTimeout('combooNUCTramo.ocultar()',20);
                                                            }
                                                            if (teclaAuxiliar == 9){
                                                              comboVia.ocultar();
                                                              comboTipoNumeracion.ocultar();
                                                              comboDistrito.ocultar();
                                                              comboSeccion1.ocultar();
                                                              comboSubSeccion.ocultar();
                                                              comboPostal.ocultar();
                                                              comboESITramo.ocultar();
                                                              comboNUCTramo.ocultar();

                                                            }
                                                        }
                                                        
                                                        var comboVia = new Combo("Via");
                                                        var comboTipoNumeracion = new Combo("TipoNumeracion");
                                                        var comboDistrito = new Combo("Distrito");
                                                        var comboSeccion1 = new Combo("Seccion1");
                                                        var comboSubSeccion = new Combo("SubSeccion");
                                                        var comboPostal = new Combo("Postal");
                                                        
                                                        var comboESITramo = new Combo("ESITramo");
                                                        var comboNUCTramo = new Combo("NUCTramo");
                                                        
                                                        
                                                        var auxCombo = "comboVia";
                                                        
                                                        function cargarListasBusqueda(){
                                                            document.forms[0].opcion.value="cargarListasBusqueda";
                                                            document.forms[0].target="oculto";
                                                            document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Trameros.do";
                                                            document.forms[0].submit();
                                                        }
                                                        
                                                        function cargarListasRejilla(){
                                                            habilitarCamposBusqueda(true);
                                                            
                                                            document.forms[0].opcion.value="cargarListasRejilla";
                                                            document.forms[0].target="oculto";
                                                            document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Trameros.do";
                                                            document.forms[0].submit();
                                                            habilitarCamposBusqueda(false);
                                                        }
                                                        
                                                        comboDistrito.change = 
                                                        function() { 
                                                            auxCombo="comboSeccion1"; 
                                                            borrarSeccion();
                                                            borrarSubSeccion();
                                                            //borrarManzana();
                                                            if(comboDistrito.cod.value.length!=0){ 
                                                                cargarListaSecciones();    
                                                            }else{
                                                            comboSeccion1.addItems([],[]);
                                                            comboSubSeccion.addItems([],[]);
                                                            comboManzana.addItems([],[]);
                                                        }
                                                    }
                                                    
                                                    comboSeccion1.change = 
                                                    function() { 
                                                        borrarSubSeccion();
                                                        //borrarManzana();
                                                        if(comboSeccion1.cod.value.length!=0){
                                                            var i = comboSeccion1.selectedIndex;
                                                            document.forms[0].codSeccion.value = codSecciones[i];
                                                            document.forms[0].letraSeccion.value = letraSecciones[i];
                                                            cargarListaSubSeccion();
                                                        }else {
                                                        comboSubSeccion.addItems([],[]);
                                                        comboManzana.addItems([],[]);
                                                    }
                                                }
                                                
                                                function controlVia(){
                                                    idVias = idViasDefecto;
                                                    codVias = codViasDefecto;
                                                    descVias = descViasDefecto;
                                                    comboVia.addItems(codVias,descVias);
                                                }
                                                
                                                function cargarComboBox(cod, des){
                                                    eval(auxCombo+".addItems(cod,des)");
                                                }  
                                                
                                                comboESITramo.change = 
                                                function() { 
                                                    auxCombo='comboNUCTramo'; 
                                                    limpiar(['codNUCTramo','descNUCTramo']);
                                                    if(comboESITramo.cod.value.length!=0){
                                                        var i = comboESITramo.selectedIndex;
                                                        if(i>=0){
                                                            document.forms[0].codECO.value = codECOESIs[i][0];
                                                            document.forms[0].descECO.value = codECOESIs[i][1];
                                                        }
                                                        cargarListaNUCs(); 			
                                                    }else{
                                                    //valoresPorDefecto();
                                                }		
                                            } 
                                            /*
                                            comboTipoNumeracion.change =
                                            function() {
                                            if(comboTipoNumeracion.cod.value.length!=0){
                                            if (comboTipoNumeracion.cod.value == 0 ) {
                                            document.forms[0].txtNumDesde.className="inputTexto";
                                            document.forms[0].txtNumHasta.className="inputTexto";
                                            } else {
                                            document.forms[0].txtNumDesde.className="inputTextoObligatorio";
                                            document.forms[0].txtNumHasta.className="inputTextoObligatorio";
                                            
                                            }
                                            }
                                            }
                                            */		
        </script>
    </body>
</html>
