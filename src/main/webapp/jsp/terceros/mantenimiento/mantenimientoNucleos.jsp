<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<%@ page contentType="text/html;charset=ISO_8859-1"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title> Mantenimiento de Nucleos INE </title>
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
            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
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
        var codECOs = new Array();
        var descECOs = new Array();
        var codECOESI = new Array();
        var codESIs = new Array();
        var descESIs = new Array();
        var codESIsDefecto = new Array();
        var descESIsDefecto = new Array();
        var listaNucleosOriginal = new Array();
        var listaNucleos = new Array();
        var paisOld = "";
        var provinciaOld = "";
        var municipioOld = "";
        
        // FUNCIONES DE CARGA E INICIALIZACION DE DATOS  
        function recuperaDatosIniciales(){
    <%
            Vector listaEsis = mantForm.getListaEsis();
            int lengthEsis = listaEsis.size();
            int i = 0;
    %>
        var j=0;
        <%for (i = 0; i < lengthEsis; i++) {%>
        codESIs[j] = "<%=(String) ((GeneralValueObject) listaEsis.get(i)).getAtributo("codEntidadSingular")%>";
        descESIs[j] = "<%=(String) ((GeneralValueObject) listaEsis.get(i)).getAtributo("nombreOficial")%>";
        codECOESI[j] = "<%=(String) ((GeneralValueObject) listaEsis.get(i)).getAtributo("codEntidadColectiva")%>";
        j++;
        <%}%>
        codESIsDefecto = codESIs;
        descESIsDefecto = descESIs;
         <%	Vector listaEcos = mantForm.getListaEcos();
            int lengthEcos = listaEcos.size();
            i = 0;
         %>
             j=0;
             <%for (i = 0; i < lengthEcos; i++) {%>
             codECOs[j] = "<%=(String) ((GeneralValueObject) listaEcos.get(i)).getAtributo("codECO")%>";
             descECOs[j] = "<%=(String) ((GeneralValueObject) listaEcos.get(i)).getAtributo("descECO")%>";
             j++;
             <%}%>
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
     
     // FUNCIONES DE LIMPIEZA, HABILITACION Y DESHABILITACION DE CAMPOS
     var vectorCamposBusqueda = ["codESI","descESI","codECO","descECO"];
     var vectorCamposRejilla = ["codNUC","codINE","descNUC","nombreLargo","situacion"];
     //var vectorCombosBusqueda = ["comboESI"];
     var vectorBotones = ["botonAlta","botonModificar","botonBorrar","botonLimpiar", "botonModReg"];
     
     
     function habilitarCamposRejilla(habilitar){
         habilitarGeneralInputs(vectorCamposRejilla,habilitar);
         habilitarGeneralInputs(vectorBotones,habilitar);
     }
     
     function limpiarFormulario(){
         limpiarCamposBusqueda();
         limpiarCamposRejilla();
         tablaNucleos.lineas = new Array();
         refresca(tablaNucleos);
     }
     
     function limpiarCamposBusqueda(){
         limpiar(vectorCamposBusqueda);
     }
     
     function limpiarCamposRejilla(){
         var vector = [document.forms[0].codNUC];
         habilitarGeneral(vector);
         limpiar(vectorCamposRejilla);
     }
     
     // FUNCIONES DE CARGA DE DATOS DINAMICA
     
     function cargarListaNucleos(lista){
         listaNucleos = lista;
         tablaNucleos.lineas = lista;
         refresca(tablaNucleos);
     }
     
     function valoresPorDefecto(){
         document.forms[0].codPais.value ="<%=ptVO.getPais()%>";
         document.forms[0].codProvincia.value = "<%=ptVO.getProvincia()%>";
         document.forms[0].descProvincia.value = "<%=ptVO.getNomProvincia()%>";
         document.forms[0].codMunicipio.value ="<%=ptVO.getMunicipio()%>";
         document.forms[0].descMunicipio.value = "<%=ptVO.getNomMunicipio()%>";
         codESIs = codESIsDefecto;
         descESIs = descESIsDefecto;
         comboESI.addItems(codESIs,descESIs);
         comboECO.addItems(codECOs,descECOs);
     }
     
     function rellenarCamposBusqueda(datos){
         rellenar(datos,vectorCamposBusqueda);
     }
     
     // FUNCIONES DE VALIDACION DE CAMPOS
     function validarCamposBusqueda(){
         var pais = document.forms[0].codPais.value;
         var provincia = document.forms[0].codProvincia.value;
         var municipio = document.forms[0].codMunicipio.value;
         var codESI = document.forms[0].codESI.value;
         if((pais!="")&&(provincia!="")&&(municipio!="")&&(codESI!=""))
             return true;
         return false;
     }
     
     function validarCamposRejilla(){
         var codNUC = document.forms[0].codNUC.value;
         var codINE = document.forms[0].codINE.value;
         var descNUC = document.forms[0].descNUC.value;
         if((codINE!="")&&(descNUC!=""))
             return true;
         return false;
     }
     
     function noEsta(indice){
         var cod = document.forms[0].codINE.value;
         for(i=0;(i<listaNucleos.length);i++){
             if(i!=indice){
                 if((listaNucleos[i][0]) == cod)
                     return false;
             }
         }
         return true;
     }
     
     function filaSeleccionada(tabla){
         var i = tabla.selectedIndex;
         if((i>=0)&&(!tabla.ultimoTable))
             return true;
         return false;
     }
     
     // FUNCIONES DE PULSACION DE BOTONES
     
     function pulsarBuscar(){
         var botonBuscar = ["botonBuscar"];
         if(validarCamposBusqueda()){
             document.forms[0].opcion.value="cargarNucleosTodos";
             document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
             document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Nucleos.do";
             document.forms[0].submit();
             habilitarGeneralInputs(botonBuscar,false);
             //habilitarCamposBusqueda(false);
             comboESI.deactivate();
             comboECO.deactivate();
             habilitarCamposRejilla(true);
         }else
         jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>")
         }
         
         
         function pulsarAlta(){
             if(validarCamposRejilla()){
                 if(!noEsta()){
                     jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>");
                     }else{
                     document.forms[0].situacion.value="A";
                     //habilitarCamposBusqueda(true);
                     comboESI.activate();
                     document.forms[0].opcion.value="alta";
                     document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                     document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Nucleos.do";
                     document.forms[0].submit();
                     //habilitarCamposBusqueda(false);
                     comboESI.deactivate();
                     limpiarCamposRejilla();
                 }
             }else
             jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>")
             }
             
             function pulsarModificar(situacion){
                 if(filaSeleccionada(tablaNucleos)){
                     if(listaNucleos[tablaNucleos.selectedIndex][5] != "B") {
                         if(validarCamposRejilla()){
                             if(noEsta(tablaNucleos.selectedIndex)){
                                 var vector = [document.forms[0].codNUC];
                                 habilitarGeneral(vector);
                                 document.forms[0].situacion.value=situacion;
                                 //habilitarCamposBusqueda(true);
                                 comboESI.activate();
                                 document.forms[0].opcion.value="modificar";
                                 document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                                 document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Nucleos.do";
                                 document.forms[0].submit();
                                 //habilitarCamposBusqueda(false);
                                 comboESI.deactivate();
                                 deshabilitarGeneral(vector);
                                 limpiarCamposRejilla();
                             }else
                             jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>");
                             }else
                             jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>")
                             } else 
                             jsp_alerta("A","<%=descriptor.getDescripcion("msjEstaBaja")%>");
                             }else
                             jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>")
                             }
                             
                             function pulsarBorrar(){
                                 if(filaSeleccionada(tablaNucleos)){
                                     if(validarCamposBusqueda()){
                                         if(jsp_alerta("C",'<%=descriptor.getDescripcion("desElimNucleo")%>') ==1) {
                                             //habilitarCamposBusqueda(true);
                                             var vector = [document.forms[0].codNUC];
                                             habilitarGeneral(vector);
                                             comboESI.activate();
                                             document.forms[0].opcion.value="eliminar";
                                             document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                                             document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Nucleos.do";
                                             document.forms[0].submit();
                                             //habilitarCamposBusqueda(false);
                                             comboESI.deactivate();
                                             deshabilitarGeneral(vector);
                                             limpiarCamposRejilla();
                                             if(tablaNucleos.selectedIndex != -1 ) {
                                                 tablaNucleos.selectLinea(tablaNucleos.selectedIndex);
                                                 tablaNucleos.selectedIndex = -1;
                                             }
                                         }
                                     }else
                                     jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>")
                                     }else
                                     jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>")
                                     }
                                     
                                     function noEliminarNucl() {
                                         jsp_alerta("A","<%=descriptor.getDescripcion("msjNoElimNucl")%>");
                                         }
                                         
                                         function pulsarCancelarBuscar(){
                                             limpiarFormulario();
                                             var botonBuscar = ["botonBuscar"];
                                             habilitarGeneralInputs(botonBuscar,true);
                                             //habilitarCamposBusqueda(true);
                                             comboESI.activate();
                                             comboECO.activate();
                                             habilitarCamposRejilla(false);
                                             valoresPorDefecto();
                                         }
                                         
                                         function pulsarLimpiar(){
                                             limpiarCamposRejilla();
                                             if(tablaNucleos.selectedIndex != -1 ) {
                                                 tablaNucleos.selectLinea(tablaNucleos.selectedIndex);
                                                 tablaNucleos.selectedIndex = -1;
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
                                                 datosRetorno = listaNucleosOriginal[indice];
                                             self.parent.opener.retornoXanelaAuxiliar(datosRetorno);
                                         }
                                     }
                                     
                                     
                                     function haCambiadoValor(){
                                         var i= tablaNucleos.focusedIndex;
                                         var codINE = document.forms[0].codINE.value;
                                         var descNUC = document.forms[0].descNUC.value;
                                         if((codINE!=listaNucleos[i][1])||(descNUC!=listaNucleos[i][2]))
                                             return true;
                                         return false;
                                     }
                                     
                                     function pulsarModificarRegistro(situacion){
                                         if(filaSeleccionada(tablaNucleos)){
                                             if(listaNucleos[tablaNucleos.selectedIndex][5] != "B") {
                                                 if(haCambiadoValor()){
                                                     if(validarCamposRejilla()){
                                                         if(noEsta(tablaNucleos.selectedIndex)){
                                                            var source = "<%=request.getContextPath()%>/jsp/terceros/mantenimiento/datosConRegistro.jsp?opcion=null";
                                                            abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source="+source,'',
                                                                    'width=640,height=380,status='+ '<%=statusBar%>',function(resp){
                                                                    if(resp!=undefined){
                                                                        document.forms[0].fechaOperacion.value = resp[0];
                                                                        document.forms[0].generarOperaciones.value= resp[1];
                                                                        var vector = [document.forms[0].codNUC];
                                                                        habilitarGeneral(vector);
                                                                        document.forms[0].situacion.value=situacion;	        
                                                                        comboESI.activate();				
                                                                        document.forms[0].opcion.value="modificarNucTerritorio";
                                                                        document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                                                                        document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Nucleos.do";
                                                                        document.forms[0].submit();					
                                                                        comboESI.deactivate();
                                                                        deshabilitarGeneral(vector);
                                                                        limpiarCamposRejilla();
                                                                    }
                                                             });                                                             
                                                         }else
                                                         jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>");
                                                         }else
                                                         jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>")
                                                         }else{
                                                         jsp_alerta("A","No se ha producido ningún cambio"); 
                                                     }				 
                                                 } else 
                                                 jsp_alerta("A","<%=descriptor.getDescripcion("msjEstaBaja")%>");
                                                 }else
                                                 jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>")
                                                 }

                                                     function cargarListaESIS() {
                                                         document.forms[0].opcion.value="cargarEsis";
                                                         document.forms[0].target="oculto";
                                                         document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Nucleos.do";
                                                         document.forms[0].submit();	
                                                     }
                                                     
                                                     function valorECO(indice) {
                                                         if(document.forms[0].codECO.value == "") {
                                                             comboECO.buscaCodigo(codECOESI[indice]);
                                                             var cESI = codESIs[indice];
                                                             comboESI.buscaCodigo(cESI);
                                                         }
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
    <input type="hidden" name="codNUCAntiguo" value="">
    <input type="hidden" name="codNUC" value="">
    <input type="hidden" name="ventana" value="<%=mantForm.getVentana()%>">
    <input type="hidden" name="codProvincia" value="<%=ptVO.getProvincia()%>">
    <input type="hidden" name="descProvincia" value="<%=ptVO.getNomProvincia()%>%>">
    <input type="hidden" name="codMunicipio" value="<%=ptVO.getMunicipio()%>">
    <input type="hidden" name="descMunicipio" value="<%=ptVO.getNomMunicipio()%>">
    <!-- Modificacion con registro -->
    <input type="hidden" name="fechaOperacion" value="">
    <input type="hidden" name="generarOperaciones" value="">

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_ModNucleos")%></div>
<div class="contenidoPantalla">
    <table>
        <tr>
            <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td>
                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td style="width: 35%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_ECO")%></td>
                                    <td style="width: 65%" class="etiqueta" align="left"><%=descriptor.getDescripcion("gEtiq_ESI")%></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
                                <tr>
                                    <td class="etiqueta">
                                        <input name="codECO" type="text" class="inputTextoObligatorio" id="codECO" size="3"> 
                                        <input name="descECO" type="text" class="inputTextoObligatorio" id="descECO" style="width:180" 
                                               readonly> 
                                        <a id="anchorECO" name="anchorECO" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonECO" name="botonECO"
                                            style="cursor:hand;"></span></a>
                                    </td>
                                    <td class="etiqueta">
                                        <input name="codESI" type="text" class="inputTextoObligatorio" id="codESI" size="3"> 
                                        <input name="descESI" type="text" class="inputTextoObligatorio" id="descESI" style="width:180" 
                                               readonly> 
                                        <a id="anchorESI" name="anchorESI" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonESI" name="botonESI"
                                            style="cursor:hand;"></span></a>
                                    </td>
                                    <td align="right">
                                        <input name="botonBuscar" type="button"  class="botonGeneral" id="botonBuscar" 
                                               value="<%=descriptor.getDescripcion("gbBuscar")%>"
                                               onClick="pulsarBuscar();" accesskey="B">
                                        <input name="botonCancelar" type="button" class="botonGeneral" id="botonCancelar"
                                               value="<%=descriptor.getDescripcion("gbCancelar")%>"
                                               onClick="pulsarCancelarBuscar();" accesskey="C">
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td> 
                <table width="100%" cellspacing="0" cellpadding="5px" align="center">

                    <tr>
                        <td>
                            <table border="0" align="center">
                             <tr>
                                 <td id="tablaNucleos"> </td>
                             </tr>
                            </table>
                        </td>


                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td> 
                <input name="codINE" type="text" class="inputTextoObligatorio" style="width: 110px" maxlength=2
                       onkeyup="return SoloDigitosNumericos(this);">
                <input name="descNUC" type="text" class="inputTextoObligatorio" style="width: 300px" maxlength=25
                       onkeyup = "return xAMayusculas(this);">
                <input name="nombreLargo" type="text" class="inputTexto" style="width: 400px" maxlength=60
                       onkeyup = "return xAMayusculas(this);">
                <input class="inputTextoObligatorio" type="text" name="situacion" style="width: 60px"
                       maxlength="1" readonly> 
            </td>
        </tr>                                                                        
    </table>
    <div class="botoneraPrincipal">
        <input type="button" class="botonGeneral"  name="botonAlta" onClick="pulsarAlta();" accesskey="A" value="<%=descriptor.getDescripcion("gbAlta")%>">
        <input type="button" class="botonGeneral"  name="botonModificar" onClick="pulsarModificar('A');" accesskey="M" value="<%=descriptor.getDescripcion("gbModificar")%>"> 
        <input type="button" class="botonGeneral" name="botonModReg" onClick="pulsarModificarRegistro();" accesskey="R" value="<%=descriptor.getDescripcion("gbModReg")%>">
        <input type="button" class="botonGeneral" name="botonBorrar" onClick="pulsarBorrar();" accesskey="O" value="<%=descriptor.getDescripcion("gbEliminar")%>">
        <input type="button" class="botonGeneral"  name="botonLimpiar" onClick="pulsarLimpiar();" accesskey="L" value="<%=descriptor.getDescripcion("gbLimpiar")%>">
        <input type="button" class="botonGeneral" name="botonSalir" onClick="pulsarSalir();" accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>">
    </div>
</div>
</form>
<script type="text/javascript">
            var indice;
            var tablaNucleos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaNucleos"));
            tablaNucleos.addColumna("100","left",'<%=descriptor.getDescripcion("gEtiq_CodigoINE")%>');
            tablaNucleos.addColumna("300","left",'<%=descriptor.getDescripcion("gEtiq_NomOficial")%>');
            tablaNucleos.addColumna("400","left",'<%=descriptor.getDescripcion("gEtiq_NomLargo")%>');
            tablaNucleos.addColumna("60","left",'<%=descriptor.getDescripcion("gEtiq_Situacion")%>');
                            
            tablaNucleos.displayCabecera=true;
            tablaNucleos.displayTabla();
                            
                            function refresca(tabla){
                                tabla.displayTabla();
                            }
                            
                            function rellenarDatos(tableName,rowID){
                                if(tablaNucleos==tableName){
                                    var i=rowID;
                                    indice = rowID;
                                    limpiarCamposRejilla();
                                    if((i>=0)&& (!tableName.ultimoTable)){
                                        var vector = [document.forms[0].codNUC];
                                        deshabilitarGeneral(vector);
                                        var vectorDatosRejilla = [listaNucleosOriginal[i][4],listaNucleosOriginal[i][7],
                                            listaNucleosOriginal[i][5],listaNucleosOriginal[i][6],
                                            listaNucleosOriginal[i][9]];
                                        rellenar(vectorDatosRejilla,vectorCamposRejilla);
                                        document.forms[0].codNUCAntiguo.value = listaNucleosOriginal[i][4];
                                    }
                                }
                            }	//Se ejecuta al cambiar la selección en una Tabla Simple. 
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
                                    upDownTable(tablaNucleos,listaNucleos,teclaAuxiliar);
                                }

                                if(teclaAuxiliar == 38){
                                    upDownTable(tablaNucleos,listaNucleos,teclaAuxiliar);
                                }
                                if(teclaAuxiliar == 1){
                                    if (comboESI.base.style.visibility == "visible" && isClickOutCombo(comboESI,coordx,coordy)) setTimeout('comboESI.ocultar()',20);
                                    if (comboECO.base.style.visibility == "visible" && isClickOutCombo(comboECO,coordx,coordy)) setTimeout('comboECO.ocultar()',20);
                                }
                                if(teclaAuxiliar == 9){
                                    if (comboESI.base.style.visibility == "visible") comboESI.ocultar();
                                    if (comboECO.base.style.visibility == "visible") comboECO.ocultar();
                                }
                            }
                            
                            // COMBOS
                            var comboESI = new Combo("ESI");
                            var comboECO = new Combo("ECO");
                            
                            comboECO.change = 
                            function() { 
                                auxCombo="comboESI"; 
                                if(comboECO.cod.value.length!=0){ 
                                    comboESI.selectItem(-1);
                                    cargarListaESIS();    
                                }else{
                                comboESI.addItems([],[]);
                            }
                        }
                        
                        comboESI.change = 
                        function() { 
                            if(comboESI.cod.value.length!=0){ 
                                valorECO(comboESI.selectedIndex);    
                            }
                        }
                        
                        function cargarComboBox(cod, des){
                            eval(auxCombo+".addItems(cod,des)");
                        }
        </script>
    </body>
</html>
