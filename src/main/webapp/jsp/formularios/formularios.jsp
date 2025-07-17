<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@ page import="java.util.Vector"%>
<%@ page import="es.altia.agora.business.util.ElementoListaValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.formularios.FormulariosForm"%>

<html:html>
<head>
<jsp:include page="/jsp/formularios/tpls/app-constants.jsp" />
<TITLE>::: GESTI�N DE FORMULARIOS :::</TITLE>
<%
int idioma = 1;
int apl = 1;
String css = "";
if (session != null) {
    UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
    if (usuario != null) {
        idioma = usuario.getIdioma();
        apl = usuario.getAppCod();
        css = usuario.getCss();
    }
}
%>
<!-- Estilos -->
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<!-- Ficheros JavaScript -->
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
<script language="JavaScript">
var cod_tipo= new Array();
var desc_tipo= new Array();
var cod_proc= new Array();
var desc_proc= new Array();
var listaForms = new Array();

function inicializar() {
    <%
    FormulariosForm bForm2 = (FormulariosForm) session.getAttribute("FormulariosForm");
    Vector listaTipos = bForm2.getListaTipos();%>

    var j=0;

    <%
    for (int i = 0; i < listaTipos.size(); i++) {
        GeneralValueObject tipos = (GeneralValueObject) listaTipos.get(i);%>
    cod_tipo[j] = '<%=(String) tipos.getAtributo("codigo")%>';
    desc_tipo[j++] = '<%=(String) tipos.getAtributo("descripcion")%>';
    <%}%>
    comboTipos.addItems(cod_tipo, desc_tipo);

   //COMBO PROCEDIMIENTOS
    <%
    Vector listaProcedimientos = bForm2.getListaProcedimientos();%>
    j=0;

    <%for (int i = 0; i < listaProcedimientos.size(); i++) {
        ElementoListaValueObject elvo = (ElementoListaValueObject) listaProcedimientos.get(i);%>
        cod_proc[j] = '<%=elvo.getCodigo()%>';
        desc_proc[j++] = '<%=elvo.getDescripcion()%>';
    <%}%>
    comboProcedimientos.addItems(cod_proc, desc_proc);

    var datosFormularios = new Array();

    <%Vector listaFormularios = bForm2.getListaFormularios();
    int lengthFormularios = listaFormularios.size();%>
    j=0;
    <%String imagen = "";

    for (int i = 0; i < lengthFormularios; i++) {
        GeneralValueObject tipos = (GeneralValueObject) listaFormularios.get(i);
        if (tipos.getAtributo("fechaBajaForm") != null) {
            imagen = "fa-times";
        } else {
            imagen = "fa-check";
        }%>

    datosFormularios[j] = ['<%=(String) tipos.getAtributo("codForm")%>'
        , '<%=(String) tipos.getAtributo("codVisibleForm")%>'
        , '<%=(String) tipos.getAtributo("descForm")%>'
        , '<%=(String) tipos.getAtributo("versionForm")%>'
        , '<%=(String) tipos.getAtributo("fechaAltaForm")%>'
        , '<span class="fa <%=imagen%>"></span>'];
    listaForms[j] = datosFormularios[j];
    j++;
    <%}%>

    tabForms.lineas=listaForms;
    refrescaFormularios();
}

function pulsarSalir() {
    document.forms[0].target = "mainFrame";
    document.forms[0].action = '<%=request.getContextPath()%>/jsp/formularios/presentacionADM.jsp';
    document.forms[0].submit();
}

function pulsarCancelarBuscar() {
    listaForms = new Array();
    tabForms.lineas=listaForms;
    refrescaFormularios();
    document.forms[0].codTipo.value = "";
    document.forms[0].descTipo.value = "";
    document.forms[0].codProcedimiento.value = "";
    document.forms[0].descProcedimiento.value = "";
    document.forms[0].codFormulario.value = "";
    document.forms[0].descFormulario.value = "";
    comboTipos.activate();
    comboProcedimientos.activate();
    habilitarDatos(document.forms[0]);
    var botonBuscar = [document.forms[0].botonBuscar];
    habilitarGeneral(botonBuscar);
    var botones = [document.forms[0].botonNuevaVersion,document.forms[0].botonModificar,
        document.forms[0].botonBorrar,document.forms[0].botonLimpiar];
    deshabilitarGeneral(botones);
}

function pulsarBuscar() {
    document.forms[0].opcion.value="buscarForms";
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/formularios/Formularios.do";
    document.forms[0].submit();
    comboTipos.deactivate();
    comboProcedimientos.deactivate();
    deshabilitarDatos(document.forms[0]);
    var botonBuscar = [document.forms[0].botonBuscar];
    deshabilitarGeneral(botonBuscar);
    var botones = [document.forms[0].botonNuevaVersion,document.forms[0].botonAlta,document.forms[0].botonModificar,document.forms[0].botonBorrar,
        document.forms[0].botonLimpiar];
    habilitarGeneral(botones);
}

function validarCamposBusqueda(){
    var codOrg = document.forms[0].codTipo.value;
    if(codOrg!="")
        return true;
    return false;
}

function cargarTablaFormularios(lista1) {
    listaForms = lista1;
    tabForms.lineas=listaForms;
    refrescaFormularios();
    pulsarLimpiar();
}

function pulsarAlta(){
    document.forms[0].opcion.value = 'alta';
    document.forms[0].target = "mainFrame";
    document.forms[0].action = '<%=request.getContextPath()%>/formularios/FichaFormulario.do';
    document.forms[0].submit();
}

function comprobarObligatorios() {
}

function pulsarModificar(){
    if(tabForms.selectedIndex != -1) {
        var cod = listaForms[tabForms.selectedIndex][0];
        document.forms[0].formCod.value = cod;
        document.forms[0].opcion.value = 'cargar';
        document.forms[0].target = "mainFrame";
        document.forms[0].action = '<%=request.getContextPath()%>/formularios/FichaFormulario.do';
        document.forms[0].submit();
    }
    else {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }
}

function pulsarNuevaVersion(){
    if(tabForms.selectedIndex != -1) {
        var cod = listaForms[tabForms.selectedIndex][0];
        document.forms[0].formCod.value = cod;
        document.forms[0].opcion.value = 'nuevaVersion';
        document.forms[0].target = "mainFrame";
        document.forms[0].action = '<%=request.getContextPath()%>/formularios/FichaFormulario.do';
        document.forms[0].submit();
    }
    else {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }
}

function pulsarEliminar() {
    if(tabForms.selectedIndex != -1) {
        if(jsp_alerta("C",'<%=descriptor.getDescripcion("desEliminar")%>') ==1) {
            var cod = listaForms[tabForms.selectedIndex][0];
            document.forms[0].formCod.value = cod;
            document.forms[0].opcion.value = 'eliminar';
            document.forms[0].target = "mainFrame";
            document.forms[0].action = '<%=request.getContextPath()%>/formularios/FichaFormulario.do';
            document.forms[0].submit();
        }
    }
    else {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }
}

function pulsarLimpiar() {
}
</script>
</head>
<body class="bandaBody"  onload="javascript:{ pleaseWait('off'); inicializar()}">
<jsp:include page="/jsp/hidepage.jsp" flush="true">
    <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
</jsp:include>
<html:form action="/formularios/Formularios.do" target="_self">
    <html:hidden  property="opcion" value=""/>
    <input  type="hidden"  name="formCod" id="formCod">
    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("etiq_Formularios")%></div>
    <div  class="contenidoPantalla">
        <table style="width:100%">
            <tr>
                <td class="etiqueta" style="width:15%"><%=descriptor.getDescripcion("fmFormulario")%>:</td>
                <td class="etiqueta" style="width:85%">
                    <input type="text" name="codFormulario" id="codFormulario" size="6" class="inputTexto" value="" onkeyup="return xAMayusculas(this);" maxLenght=6>
                    <input type="text" name="descFormulario"  id="descFormulario" size="75" class="inputTexto" style="width:450px" value="" onkeyup="return xAMayusculas(this);">
                </td>
            </tr>
            <tr>
                <td class="etiqueta" style="width:15%"><%=descriptor.getDescripcion("gbTipo")%>:</td>
                <td class="etiqueta" style="width:85%">
                    <input type="text" name="codTipo" id="codTipo" size="6" class="inputTexto" value="" onkeyup="return xAMayusculas(this);"/>
                    <input type="text" name="descTipo"  id="descTipo" size="75" class="inputTexto" style="width:450px" readonly="true" value=""/>
                    <A href="" id="anchorTipo" name="anchorTipo" style="text-decoration:none;">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipo"
                             name="botonTipo" style="cursor:hand;"></span>
                    </A>
                </td>                                                                                        
            </tr>
            <tr>
                <td style="width:15%" class="etiqueta"><%=descriptor.getDescripcion("gbProcedimiento")%>:</td>
                <td style="width:85%" class="columnP">
                    <input type=text name="codProcedimiento" id="codProcedimiento" size=6 class="inputTexto" onkeyup="return xAMayusculas(this);"/>
                    <input type=text name="descProcedimiento" id="descProcedimiento" size=75 class="inputTexto" style="width:450px" readonly="true"/>
                    <A id="anchorProcedimiento" name="anchorProcedimiento">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonProcedimiento"
                             name="botonProcedimiento" style="cursor:hand;"></span>
                    </A>
                    <input name="botonBuscar" type="button"  class="botonGeneral" id="botonBuscar" style="float:right"
                            value="<%=descriptor.getDescripcion("gbBuscar")%>" onClick="pulsarBuscar();" accesskey="B">
                </td>
            </tr>
            <tr>
                <td colspan="2" id="tablaFormularios"></td>
            </tr>
        </TABLE>
        <div id="tablaBotones" class="botoneraPrincipal">
                    <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbNuevaVersion")%>
                           name="botonNuevaVersion" onClick="pulsarNuevaVersion();" accesskey="V">
                    <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%>
                           name="botonAlta" onClick="pulsarAlta();" accesskey="A">
                    <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%>
                           name="botonModificar" onClick="pulsarModificar();" accesskey="M">
                    <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%>
                           name="botonBorrar" onClick="pulsarEliminar();" accesskey="E">
                    <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%>
                           name="botonLimpiar" onClick="pulsarCancelarBuscar();" accesskey="L">
                    <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%>
                           name="botonSalir" onClick="pulsarSalir();" accesskey="S">
        </div>
    </div>
</html:form>
<script language="JavaScript1.2">
// JAVASCRIPT DE LA TABLA UTR
var tabForms = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaFormularios'));

tabForms.addColumna('0','left',''); //Columna para el c�digo interno
tabForms.addColumna('150','left','<%= descriptor.getDescripcion("gEtiq_Codigo")%>');
tabForms.addColumna('550','left','<%= descriptor.getDescripcion("gEtiq_Descrip")%>');
tabForms.addColumna('80','left','<%= descriptor.getDescripcion("gbVersion")%>');
tabForms.addColumna('85','left','<%= descriptor.getDescripcion("gbFecAlta")%>');
tabForms.addColumna('25','left','');
tabForms.displayCabecera=true;

function refrescaFormularios() {
    tabForms.displayTabla();
}
// FIN DE LOS JAVASCRIPT'S DE LA TABLA UTR

var coordx=0;
var coordy=0;

document.onmouseup = checkKeys;

function checkKeysLocal(evento, tecla){

    var aux=null;
    if(window.event)
        aux = window.event;
    else
        aux = evento;

    var tecla = 0;
    if(aux.keyCode)
        tecla = aux.keyCode;
    else
        tecla = aux.which;


    if(tecla == 40){
        upDownTable(tabForms,listaForms,tecla);
    }
    if (tecla == 38){
        upDownTable(tabForms,listaForms,tecla);
    }
    if(tecla == 13){
        //if((tabForms.selectedIndex>-1)&&(tabForms.selectedIndex < listaForms.length)){
        //   pulsarModificar();
        //}
    }
    if(tecla == 1){
        if (comboTipos.base.style.visibility == "visible" && isClickOutCombo(comboTipos,coordx,coordy)) setTimeout('comboTipos.ocultar()',20);
        if (comboProcedimientos.base.style.visibility == "visible" && isClickOutCombo(comboProcedimientos,coordx,coordy)) setTimeout('comboProcedimientos.ocultar()',20);
    }
    if(tecla == 9){
       if (comboTipos.base.style.visibility == "visible") comboTipos.ocultar();
        if (comboProcedimientos.base.style.visibility == "visible") comboProcedimientos.ocultar();
    }

    keyDel(aux);
}

var comboTipos = new Combo("Tipo");
var comboProcedimientos = new Combo("Procedimiento");
</script>
</BODY>
</html:html>
