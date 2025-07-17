<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<html>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<title>::: Depuracion Terceros :::</title>
<%
    UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int idioma=1;
    int apl=1;
    if (session.getAttribute("usuario") != null){
        usuarioVO =	(UsuarioValueObject)session.getAttribute("usuario");
        idioma = usuarioVO.getIdioma();
        apl =	usuarioVO.getAppCod();
    }
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>"></script>
<script type="text/javascript">
    function inicio() {
        var opcion = "<c:out value="${param.opcion}"/>";
        if (opcion == "error") {
            jsp_alerta('A', 'Se produjo un error en la copia de domicilios');
        } else if (opcion == "yaExisten") {
            jsp_alerta('A', 'El tercero a depurar ya tiene asociados todos los domicilios seleccionados para copiar');
        }
    }

    function noTerceros() {
        rows = new Array();
        tabla.setLineas(rows);
        tabla.displayTabla();
        jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoTercerosRep")%>");
    }

    function pulsarEliminarDomicilios() {
        if (tablaDomiciliosSelec.selectedIndex>=0) {
            if (document.forms[0].codDomPrincipal.value!="" && document.forms[0].codDomPrincipal.value!=null) {
                if (jsp_alerta("B", "<%=descriptor.getDescripcion("msjElimDom")%>")) {
                    var codDomAEliminar = "";
                    codDomAEliminar = codDomAEliminar + tablaDomiciliosSelec.getLinea(tablaDomiciliosSelec.selectedIndex)[4] + ",";
                    codDomAEliminar = codDomAEliminar.substring(0,codDomAEliminar.length-1);
                    document.forms[0].codDomAEliminar.value = codDomAEliminar;
                    document.forms[0].target="oculto";
                    document.forms[0].action="<%=request.getContextPath()%>/terceros/EliminarDepuracionDomicilios.do?";
                    document.forms[0].submit();
                }
            } else {
                jsp_alerta("A", "<%=descriptor.getDescripcion("msjDomPrinc")%>");
            }
        } else {
            jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoSelecFila")%>");
        }
    }

    function pulsarEliminarTercSimilares() {
        var posDomPrincipal = document.forms[0].posDomPrincipal.value;
        if (posDomPrincipal == "null" || posDomPrincipal == "") {
            posDomPrincipal = 0;
        }
        if (tablaDomiciliosSelec.lineas.length>0) {
            var codDomPrincipal = tablaDomiciliosSelec.getLinea(posDomPrincipal)[4];
            if (tabla.selectedIndex>=0) {
                if (jsp_alerta("B",  "<%=descriptor.getDescripcion("msjElimTerc")%>")) {
                    var codTercDepurado = tablaSelec.getLinea(tablaSelec.selectedIndex)[2];
                    var codTerAEliminar = "";
                    codTerAEliminar = codTerAEliminar + tabla.getLinea(tabla.selectedIndex)[2] + ",";
                    codTerAEliminar = codTerAEliminar.substring(0,codTerAEliminar.length-1);
                    document.forms[0].codTerAEliminar.value = codTerAEliminar;
                    document.forms[0].codTerDepurado.value = codTercDepurado;
                    document.forms[0].codDomPrincipal.value = codDomPrincipal;
                    document.forms[0].target="oculto";
                    document.forms[0].action="<%=request.getContextPath()%>/terceros/EliminarDepuracionTerceros.do?";
                    document.forms[0].submit();
                }
        } else {
            jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoSelecFila")%>");
        }
        } else {
            jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoDepTercSinDom")%>");
        }
    }

    function pulsarCopiarDomicilios() {
        if (tabla.selectedIndex>=0) {
            if (jsp_alerta("B",  "<%=descriptor.getDescripcion("msjCopiarDoms")%>")) {
                var codTercDepurado = tablaSelec.getLinea(tablaSelec.selectedIndex)[2];
                var codTerAEliminar = new Array();
                codTerAEliminar = codTerAEliminar + tabla.getLinea(tabla.selectedIndex)[2] + ",";
                codTerAEliminar = codTerAEliminar.substring(0, codTerAEliminar.length - 1);
                document.forms[0].target = "oculto";
                document.forms[0].action="<%=request.getContextPath()%>/jsp/terceros/ocultoCopiarDoms.jsp?" +
                    "codTercero=" + codTercDepurado + "&codTerSelec=" + codTerAEliminar;
                document.forms[0].submit();
            }
        } else {
            jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoSelecFila")%>");
        }
    }

    function copiarDomicilios(codTercero, codDoms) {
        if (codDoms!="") {
            document.forms[0].codTerDepurado.value = codTercero;
            document.forms[0].codDomiciliosACopiar.value = codDoms;
            document.forms[0].target="mainFrame";
            document.forms[0].action="<%=request.getContextPath()%>/terceros/DepuracionCopiarDomicilios.do?";
            document.forms[0].submit();
        } else {
            jsp_alerta("A", "El tercero seleccionado no tiene asociado ningún domicilio");
        }
    }

    function pulsarVolver() {
        document.forms[0].target="mainFrame";
        document.forms[0].action="<%=request.getContextPath()%>/terceros/CargarDepuracionTerceros.do?actionOrigen=volver";
        document.forms[0].submit();
    }

    function pulsarDomPrincipal(posDomPrincipal) {
        document.forms[0].codDomPrincipal.value = rowsDomSeleccionado[posDomPrincipal][4];
        document.forms[0].posDomPrincipal.value = posDomPrincipal;
    }

    function setSelectionCellSimil() {
        activarBotonEliminarTercSim(tabla.selectedIndex>=0);
        activarBotonCopiarDomicilio(tabla.selectedIndex>=0);
    }

    function activarBotonEliminarTercSim(activado){ 
        document.forms[0].cmdEliminarTercSim.disabled=!activado;
        var botones = document.getElementsByName("cmdEliminarTercSim");
        if(!activado){              
              if(botones!=null && botones.length==1){
                    botones[0].className = "botonMasLargoDeshabilitado";
              }
        }else{            
              if(botones!=null && botones.length==1){
                    botones[0].className = "botonMasLargo";
              }
        }
    }

    function activarBotonCopiarDomicilio(activado) { 
        document.forms[0].cmdCopiarDomicilios.disabled = !activado;
        var botones = document.getElementsByName("cmdCopiarDomicilios");
        if(!activado){
              if(botones!=null && botones.length==1){
                    botones[0].className = "botonMasLargoDeshabilitado";
              }
        }else{            
              if(botones!=null && botones.length==1){
                    botones[0].className = "botonMasLargo";
              }
        }
    }

    function activarBotonEliminarDomicilios(activado){
        document.forms[0].cmdEliminarDoms.disabled=!activado;
        if(!activado){
              var botones = document.getElementsByName("cmdEliminarDoms");
              if(botones!=null && botones.length==1){
                    botones[0].className = "botonLargoDeshabilitado";
              }
        }else{
            var botones = document.getElementsByName("cmdEliminarDoms");
              if(botones!=null && botones.length==1){
                    botones[0].className = "botonLargo";
              }
        }
    }

    function errorEliminarDomicilios() {
        jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoElimDepDom")%>");
    }

    function errorEliminarTerceros() {
        jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoElimDepTer")%>");
    }
    
 <!--  ------------------- -------------- -----------  -->

    function rellenarDatos(tableName,rowID) {
        if (tableName.parent.id=="tablaDomSelec")
            activarBotonEliminarDomicilios(rowID>=0);
        else if (tableName.parent.id=="tablaTer"){
            activarBotonEliminarTercSim(rowID>=0);
            activarBotonCopiarDomicilio(rowID>=0);
            document.forms[0].target="oculto";
            document.forms[0].action="<%=request.getContextPath()%>/jsp/terceros/ocultoMostrarDomTercero.jsp?codTercero=" +
                    tabla.getLinea(rowID)[2];
            document.forms[0].submit();
        }
    }

    function selectCellDom(rowId) {
        gridJstContainerDom.setSelection(rowId, !gridJstContainerDom.getRowById(rowId).selected);
    }

    function selectCellDomSelec(rowId) {
        if (rowId!=document.forms[0].posDomPrincipal.value && rowsDomSeleccionado.length>1)
            gridJstContainerDomSeleccionado.setSelection(rowId, !gridJstContainerDomSeleccionado.getRowById(rowId).selected);
    }

    function selectCellSimil(rowId) {
        document.forms[0].target="oculto";
        document.forms[0].action="<%=request.getContextPath()%>/jsp/terceros/ocultoMostrarDomTercero.jsp?codTercero=" +
                gridJstContainer.getRowById(rowId).values[2];
        document.forms[0].submit();
    }

    function setSelectionCellSimil() {
        gridJstContainer.setSelection = function(rowId, flag) {
            this.getRowById(rowId).selected = flag;
            if (this.form != null) {
                var selection = this.form.elements[this.getSelectionName() + rowId];
                if (selection != null) {
                    selection.checked = flag;
                }
            }
        var selectedRows = gridJstContainer.getSelectedRows();
        activarBotonEliminarTercSim(selectedRows.length>0);
        activarBotonCopiarDomicilio(selectedRows.length > 0);
    }
    }

    function setAllCellSimil() {
        gridJstContainer.selectAll = function(flag) {
            for (var i = 0; i < this.rows.length; i++) {
               var row = this.rows[i];
            this.setSelection(row.id, flag);
            }
            var selectedRows = gridJstContainer.getSelectedRows();
            activarBotonEliminarTercSim(selectedRows.length>0);
            activarBotonCopiarDomicilio(selectedRows.length > 0);
        }
    }
</script>
<body class="bandaBody" onload="javascript:{pleaseWait('off'); inicio();}" >
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
<form  method="post" target="_self">
    <input type="hidden" name="codDomAEliminar">
    <input type="hidden" name="codDomPrincipal"> 
    <input type="hidden" name="posDomPrincipal">
    <input type="hidden" name="codTerAEliminar">
    <input type="hidden" name="codTerDepurado">
    <input type="hidden" name="tipoBusqueda">
    <input type="hidden" name="nombre">
    <input type="hidden" name="apellido1">
    <input type="hidden" name="apellido2">
    <input type="hidden" name="fechaInicio">
    <input type="hidden" name="fechaFin">
    <input type="hidden" name="codTipoDocumento">
    <input type="hidden" name="documento">
    <input type="hidden" name="codDomiciliosACopiar">
            
    <div class="txttitblanco"><%=descriptor.getDescripcion("titDepTerceros")%></div>
    <div class="contenidoPantalla">
        <div class="sub3titulo" style="padding-left: 5px"><%= descriptor.getDescripcion("depTerceros_selecc")%></div>
        <table style="width:100%">
            <tr>
                <td style="text-align:center">
                    <div id="tablaTerSelec" style="width:100%"></div>
                    <div id="tablaDomSelec" style="width:100%"></div>
                    <input type="button" title="<%=descriptor.getDescripcion("toolTip_bElimDom")%>" class="botonLargoDeshabilitado" value="<%=descriptor.getDescripcion("toolTip_bElimDom")%>" name="cmdEliminarDoms" onClick="pulsarEliminarDomicilios();" disabled>
                </td>
            </tr>                                                                                                
        </table>
        <div class="sub3titulo" style="margin-top:15px"><%= descriptor.getDescripcion("depTerceros_simil")%></div>
        <table style="width:100%">
            <tr>
                <td style="text-align:center">
                    <div id="tablaTer" style="width:100%"></div>
                        <input type="button" title="<%=descriptor.getDescripcion("toolTip_bElimTercSim")%>" class="botonMasLargoDeshabilitado" value="<%=descriptor.getDescripcion("toolTip_bElimTercSim")%>" name="cmdEliminarTercSim" onClick="pulsarEliminarTercSimilares();" disabled>
                        <input type="button" title="<%=descriptor.getDescripcion("toolTip_bCopiarDom")%>" class="botonMasLargoDeshabilitado" value="<%=descriptor.getDescripcion("toolTip_bCopiarDom")%>" name="cmdCopiarDomicilios" onClick="pulsarCopiarDomicilios();" disabled>
                    </div>
                    <div id="tablaDom" style="width:100%"></div>
                </td>                                                    
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input type="button" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbVolver")%>' name="cmdVolver" onClick="pulsarVolver();">
        </div>            
    </div>            
</form>
<script type="text/javascript">
    var tipoBusqueda = "<%=request.getParameter("tipoBusqueda")%>";
    var nombre = "<%=request.getParameter("nombre")%>";
    var apellido1 = "<%=request.getParameter("apellido1")%>";
    var apellido2 = "<%=request.getParameter("apellido2")%>";
    var fechaInicio = "<%=request.getParameter("fechaInicio")%>";
    var fechaFin = "<%=request.getParameter("fechaFin")%>";
    var codTipoDocumento = "<%=request.getParameter("codTipoDocumento")%>";
    var documento = "<%=request.getParameter("documento")%>";
    
    if (tipoBusqueda=='null' && nombre=='null' && apellido1=='null' && apellido2=='null' &&
        fechaInicio=='null' && fechaFin=='null' && codTipoDocumento=='null' && documento=='null') {
        document.forms[0].tipoBusqueda.value = "<%=session.getAttribute("tipoBusqueda")%>";
        document.forms[0].nombre.value = "<%=(String)session.getAttribute("nombre")%>";
        document.forms[0].apellido1.value = "<%=(String)session.getAttribute("apellido1")%>";
        document.forms[0].apellido2.value = "<%=(String)session.getAttribute("apellido2")%>";
        document.forms[0].fechaInicio.value = "<%=(String)session.getAttribute("fechaInicio")%>";
        document.forms[0].fechaFin.value = "<%=(String)session.getAttribute("fechaFin")%>";
        document.forms[0].codTipoDocumento.value = "<%=(String)session.getAttribute("codTipoDocumento")%>";
        document.forms[0].documento.value = "<%=(String)session.getAttribute("documento")%>";
    } else {
        document.forms[0].tipoBusqueda.value = "<%=request.getParameter("tipoBusqueda")%>";
        document.forms[0].nombre.value = "<%=request.getParameter("nombre")%>";
        document.forms[0].apellido1.value = "<%=request.getParameter("apellido1")%>";
        document.forms[0].apellido2.value = "<%=request.getParameter("apellido2")%>";
        document.forms[0].fechaInicio.value = "<%=request.getParameter("fechaInicio")%>";
        document.forms[0].fechaFin.value = "<%=request.getParameter("fechaFin")%>";
        document.forms[0].codTipoDocumento.value = "<%=request.getParameter("codTipoDocumento")%>";
        document.forms[0].documento.value = "<%=request.getParameter("documento")%>";
    }
    document.forms[0].posDomPrincipal.value = null;
    
    var rowsSeleccionado = new Array();
    rowsSeleccionado[rowsSeleccionado.length] = ["<bean:write name="DepuracionTerceros.tercero" property="tipoDocDesc" scope="session"/>",
            "<bean:write name="DepuracionTerceros.tercero" property="documento" scope="session"/>",
            "<bean:write name="DepuracionTerceros.tercero" property="codTercero" scope="session"/>",
            "<bean:write name="DepuracionTerceros.tercero" property="nombre" scope="session"/>",
            "<bean:write name="DepuracionTerceros.tercero" property="apellido1" scope="session"/>",
            "<bean:write name="DepuracionTerceros.tercero" property="apellido2" scope="session"/>"];
    var tablaSelec = new Tabla(false,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>',
            '<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>',
            '<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', 
            '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>',
            '<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaTerSelec'));
    
    tablaSelec.addColumna('130','center',"<%=descriptor.getDescripcion("gEtiqTiDoc")%>",'String');
    tablaSelec.addColumna('100','center',"<%=descriptor.getDescripcion("gEtiqDocumento")%>",'String');
    tablaSelec.addColumna('0','center',"codTercero",'String');
    tablaSelec.addColumna('220','center',"<%=descriptor.getDescripcion("gEtiqNombre")%>",'String');
    tablaSelec.addColumna('220','center',"<%=descriptor.getDescripcion("gEtiqApellido1Part")%>",'String');
    tablaSelec.addColumna('220','center',"<%=descriptor.getDescripcion("gEtiqApellido2Part")%>",'String');
    
    tablaSelec.displayCabecera=true;
    tablaSelec.setLineas(rowsSeleccionado);
    tablaSelec.displayTabla();
    
    var rowsDomSeleccionado = new Array();
    var incideSeleccionado;
    <logic:iterate id="elemento" name="DepuracionTerceros.tercero" property="domicilios" scope="session" indexId="index">
        rowsDomSeleccionado[rowsDomSeleccionado.length] = ["<bean:write name="elemento" property="codProvincia"/>",
                "<bean:write name="elemento" property="provincia"/>",
                "<bean:write name="elemento" property="codMunicipio"/>",
                "<bean:write name="elemento" property="municipio"/>",
                "<bean:write name="elemento" property="codDomicilio"/>",
                "<bean:write name="elemento" property="domicilio"/>",
                "<bean:write name="elemento" property="cp"/>",
                <logic:equal name="elemento" property="esPrincipal" value="1">
                    "<input type='radio' checked  name='domPrincipal' onclick='pulsarDomPrincipal("+rowsDomSeleccionado.length+")'"];
                        incideSeleccionado = <c:out value="${index}"/>;
                </logic:equal>
                <logic:equal name="elemento" property="esPrincipal" value="0">
                    "<input type='radio' name='domPrincipal' onclick='pulsarDomPrincipal("+rowsDomSeleccionado.length+")'"];
                </logic:equal>
                
    </logic:iterate>
    
    var tablaDomiciliosSelec = new Tabla(false,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>',
            '<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>',
            '<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', 
            '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>',
            '<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaDomSelec'));
    
    tablaDomiciliosSelec.addColumna('0','left',"codProvincia",'String');
    tablaDomiciliosSelec.addColumna('130','center',"<%=descriptor.getDescripcion("gEtiqProvincia")%>",'String');
    tablaDomiciliosSelec.addColumna('0','left',"codMunicipio",'String');
    tablaDomiciliosSelec.addColumna('170','center',"<%=descriptor.getDescripcion("gEtiqMunicipio")%>",'String');
    tablaDomiciliosSelec.addColumna('0','left',"codDomicilio",'String');
    tablaDomiciliosSelec.addColumna('413','center',"<%=descriptor.getDescripcion("gEtiq_Domicilio")%>",'String');
    tablaDomiciliosSelec.addColumna('100','center',"<%=descriptor.getDescripcion("gEtiqCodPostal")%>",'String');
    tablaDomiciliosSelec.addColumna('50','center',"<%=descriptor.getDescripcion("gEtiq_DomPrincipal")%>",'String');
    
    tablaDomiciliosSelec.displayCabecera=true;
    tablaDomiciliosSelec.setLineas(rowsDomSeleccionado);
    tablaDomiciliosSelec.displayTabla();

    var rows = new Array();
    <logic:iterate id="elemento" name="DepuracionTerceros.terceros" scope="session">
    rows[rows.length] = ["<bean:write name="elemento" property="tipoDocDesc"/>",
            "<bean:write name="elemento" property="documento"/>",
            "<bean:write name="elemento" property="codTercero"/>",
            "<bean:write name="elemento" property="nombre"/>",
            "<bean:write name="elemento" property="apellido1"/>",
            "<bean:write name="elemento" property="apellido2"/>"];
    </logic:iterate>
    
    var tabla = new Tabla(false,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>',
            '<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>',
            '<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', 
            '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>',
            '<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaTer'));
    
    tabla.addColumna('130','center',"<%=descriptor.getDescripcion("gEtiqTiDoc")%>",'String');
    tabla.addColumna('100','center',"<%=descriptor.getDescripcion("gEtiqDocumento")%>",'String');
    tabla.addColumna('0','center',"codTercero",'String');
    tabla.addColumna('220','center',"<%=descriptor.getDescripcion("gEtiqNombre")%>",'String');
    tabla.addColumna('220','center',"<%=descriptor.getDescripcion("gEtiqApellido1Part")%>",'String');
    tabla.addColumna('220','center',"<%=descriptor.getDescripcion("gEtiqApellido2Part")%>",'String');
    
    tabla.displayCabecera=true;
    tabla.setLineas(rows);
    tabla.displayTabla();
    tabla.rellenarDatos = setSelectionCellSimil;
    
    var rowsDom = new Array();
    var tablaDomicilios = new Tabla(false,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>',
            '<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>',
            '<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', 
            '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>',
            '<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaDom'));
    
    tablaDomicilios.addColumna('0','left',"codProvincia",'String');
    tablaDomicilios.addColumna('130','center',"<%=descriptor.getDescripcion("gEtiqProvincia")%>",'String');
    tablaDomicilios.addColumna('0','left',"codMunicipio",'String');
    tablaDomicilios.addColumna('170','center',"<%=descriptor.getDescripcion("gEtiqMunicipio")%>",'String');
    tablaDomicilios.addColumna('0','left',"codDomicilio",'String');
    tablaDomicilios.addColumna('413','center',"<%=descriptor.getDescripcion("gEtiq_Domicilio")%>",'String');
    tablaDomicilios.addColumna('100','center',"<%=descriptor.getDescripcion("gEtiqCodPostal")%>",'String');
    
    tablaDomicilios.displayCabecera=true;
    tablaDomicilios.setLineas(rowsDom);
    tablaDomicilios.displayTabla();
    
    // ponemos como principal el íncide del domicilio creado que es el princial del tercero
    this.pulsarDomPrincipal(incideSeleccionado);
</script>
</body>
</html>
