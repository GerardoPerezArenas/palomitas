<!-- JSP de mantenimiento de temas -->

<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
<head><jsp:include page="/jsp/planeamiento/tpls/app-constants.jsp" />
<title>Mantenimiento de Tipos de Planeamiento</title>

<!-- ************************ 	ESTILOS 	******************************    -->




<%
    UsuarioValueObject usuarioVO = null;
    int idioma = 1;

    if (session!=null){
        usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        idioma =  usuarioVO.getIdioma();
    }
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= usuarioVO.getAppCod()%>" />


<!-- ***********************		FICHERO JAVASCRIPT 	**************************    -->

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/listasValores/listas/listas.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/listasValores/listas/tiposIdioma.js'/>"></script>

<script type="text/javascript" SRC="<html:rewrite page='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" SRC="<html:rewrite page='/scripts/domlay.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<html:rewrite page='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<html:rewrite page='/css/estilo.css'/>" media="screen" >
<script type="text/javascript">

var lista= new Array();
var listaCodigosSubseccion = new Array();
var listaSubsecciones = new Array();
var i;

function borrarDatos(){
    document.forms[0].codigoSubseccion.value = '';
    document.forms[0].subseccion.value = '';
    document.forms[0].codigo.value = '';
    document.forms[0].descripcion.value = '';
}

function Inicio() {
    window.focus();
    cargaTabla();
}

function pulsarModificar() {
    var subsec = document.forms[0].codigoSubseccion.value;
    var cod = document.forms[0].codigo.value;
    var existe = 0;
    if(tab.selectedIndex != -1) {
        if (validarFormulario()) {
            for(l=0; l < lista.length; l++){
                if (((lista[l][0]) == subsec ) && ((lista[l][2]) == cod )){
                    existe = 1;
                }
            }
            if(existe == 1) {
                document.forms[0].target = "oculto";
                document.forms[0].action = "<html:rewrite page='/planeamiento/mantenimiento/ModificarTipoPlaneamiento.do'/>";
                document.forms[0].submit();
            } else {
                jsp_alerta('A','<%=descriptor.getDescripcion("msjNoModificar")%>');
            }
        }
    } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

function pulsarAlta() {
    var subsec = document.forms[0].codigoSubseccion.value;
    var cod = document.forms[0].codigo.value;
    var yaExiste = 0;
    if (validarFormulario()) {
        for(l=0; l < lista.length; l++){
            if (((lista[l][0]) == subsec ) && ((lista[l][2]) == cod )){
                yaExiste = 1;
            }
        }
        if(yaExiste == 0) {
            document.forms[0].target = "oculto";
            document.forms[0].action = "<html:rewrite page='/planeamiento/mantenimiento/InsertarTipoPlaneamiento.do'/>";
            document.forms[0].submit();
        } else {
            jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
        }
    }
}

function pulsarEliminar() {
    if(tab.selectedIndex != -1) {
        if (validarFormulario()) {
            document.forms[0].target = "oculto";
            document.forms[0].action = "<html:rewrite page='/planeamiento/mantenimiento/EliminarTipoPlaneamiento.do'/>";
            document.forms[0].submit();
            limpiarInputs();
        }
    } else {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }
}

function recuperaDatos(lis) {
    lista = new Array();

    lista = lis;

    limpiarInputs();

    tab.lineas=lista;
    refresh();
}

function pulsarSalir(){
    window.location = "<html:rewrite page='/planeamiento/mantenimiento/SalirTipoPlaneamiento.do'/>";
}

function limpiarInputs() {
    tab.selectLinea(tab.selectedIndex);
    borrarDatos();
}

function errorEliminar() {
    jsp_alerta('A', '<%=descriptor.getDescripcion("msjManUtil")%>');
}

/////////////// Búsqueda rápida.

function rellenarDatos(tableObject, rowID){
    if(rowID>-1 && !tableObject.ultimoTable){
        document.forms[0].codigoSubseccion.value = lista[rowID][0];
        document.forms[0].subseccion.value = lista[rowID][1];
        document.forms[0].codigo.value = lista[rowID][2];
        document.forms[0].descripcion.value = lista[rowID][3];
    }else borrarDatos();
}

function selecFila(des){
    var indexOld = tab.selectedIndex;
    if(des.length != 0){
        for (var x=0; x<lista.length; x++){
            var auxLis = new String(lista[x][3]);
            auxLis = auxLis.substring(0,des.length);
            if(auxLis == des){
                if (x!=indexOld)tab.selectLinea(x);
                break;
            }
        }
    }else tab.selectLinea(-1);
}

function buscar(){
    var auxCodSubsec = new String("");
    var auxSubsec = new String("");
    var auxCod = new String("");
    var auxDes = new String("");
    if((event.keyCode != 40)&&(event.keyCode != 38)&&(event.keyCode != 8)){
        if(event.keyCode != 13){
            auxcodSubsec = document.forms[0].codigoSubseccion.value;
            auxSubsec = document.forms[0].subseccion.value;
            auxCod = document.forms[0].codigo.value;
            auxDes = document.forms[0].descripcion.value;
            if(event.keyCode == 8 && auxDes.length == 0) borrarDatos();
            selecFila(auxDes);
        }else{
            if((tab.selectedIndex>-1)&&(tab.selectedIndex < lista.length)){
                document.forms[0].codigoSubseccion.value = lista[tab.selectedIndex][0];
                document.forms[0].subseccion.value = lista[tab.selectedIndex][1];
                document.forms[0].codigo.value = lista[tab.selectedIndex][2];
                document.forms[0].descripcion.value = lista[tab.selectedIndex][3];
                auxDes = lista[tab.selectedIndex][3];
            }
        }
    }
}

/////////////// Control teclas.

function checkKeysLocal(evento,tecla) {
      var teclaAuxiliar = "";
        if(window.event){
            evento = window.event;
            teclaAuxiliar = evento.keyCode;
        }else
            teclaAuxiliar = evento.which;

    if('Alt+M'==tecla) pulsarModificar();
    if('Alt+A'==tecla) pulsarAlta();
    if('Alt+E'==tecla) pulsarEliminar();
    if('Alt+L'==tecla) limpiarInputs();
    if('Alt+S'==tecla) pulsarSalir();

    if (teclaAuxiliar == 38 || teclaAuxiliar == 40){
        upDownTable(tab,lista,teclaAuxiliar);
    }

    if (teclaAuxiliar == 13) buscar();
    keyDel(evento);
}

document.onkeydown=checkKeys;

</SCRIPT>

</head>

<body class="bandaBody" onload="javascript:{pleaseWait('off');
                              }" >


        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<form method="post">
<table width="100%" cellpadding="0px" cellspacing="0px">
    <tr>
        <td width="791px" height="415px">
            <table width="100%" height="100%" cellpadding="0px" cellspacing="1px" border="0px" bgcolor="#ffffff">
                <tr>
                    <td id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("tit_mantTipoPlan")%></td>
                </tr>
                <tr>
                    <td width="100%" height="1px" bgcolor="#666666"></td>
                </tr>
                <tr/>
                <tr>
                    <td>
                        <table width="100%" height="80%" align="center" cellpadding="0px" cellspacing="3px" bgcolor="#FFFFFF">
                            <tr>
                                <td>
                                    <table width="60%" rules="cols" align="center" cellspacing="0" cellpadding="0" class="fondoCab" border="0">
                                        <tr>
                                            <td id="tabla">
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>

                            <tr>
                                <td>
                                    <table bgColor="#FFFFFF" width="750px" heigth="75%" align="center">
                                        <tr>
                                            <html:hidden name="MantenimientoTiposForm" property="tipoRegistro"/>
                                            <!-- Codigo Tipo -->
                                            <td width="37%" align="left">
                                                <input name="codigoSubseccion" type="text" class="inputTextoObligatorio" id="codSubseccion" size="3"/>
                                                <input name="subseccion" type="text" class="inputTextoObligatorio" id="descSubseccion" style="width:180" readonly/>
                                                <a id="anchorSubseccion" name="anchorSubseccion" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonSubseccion" name="botonSubseccion"
                                                    style="cursor:hand;"></span></a>
                                            </td>
                                            <!-- Codigo -->
                                            <td width="8%" align="left">
                                                <input type="text" class="inputTextoObligatorio" id="obligatorio"  name="codigo"  size="4" maxlength="3" onKeyPress="javascript:PasaAMayusculas(event);" >
                                            </td>
                                            <!-- Descripcion -->
                                            <td width="55%" align="left">
                                                <input type="text" class="inputTextoObligatorio" id="obligatorio" name="descripcion" size="75" maxlength="45" onKeyPress="javascript:PasaAMayusculas(event);" onKeyUp="buscar();">
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>

<div id="tabla2" class="botoneraPrincipal">
    <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbAlta")%>' name="cmdAlta" onClick="pulsarAlta();"/>
    <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbModificar")%>' name="cmdModificar" onClick="pulsarModificar();"/>
    <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbEliminar")%>' name="cmdEliminar" onClick="pulsarEliminar();"/>
    <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbLimpiar")%>' name="cmdLimpiar" onClick="limpiarInputs();"/>
    <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbSalir")%>' name="cmdSalir" onClick="pulsarSalir();"/>
</div>
</form>
<script type="text/javascript" src="<html:rewrite page='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript">
    var comboSubsecciones = new Combo("Subseccion");
    var tab;
    if(document.all) tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.all.tabla,750);
    else tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('div_tabla'),150);
    tab.addColumna('0','left','');
    tab.addColumna('230','left','<%= descriptor.getDescripcion("gEtiq_subsecc")%>');
    tab.addColumna('40','left','<%= descriptor.getDescripcion("gEtiq_codigo")%>');
    tab.addColumna('478','left','<%= descriptor.getDescripcion("gEtiq_desc")%>');
    tab.height = 270;
    tab.displayCabecera=true;

    function cargaTabla(){
        var cont = 0;
        var cont2 = 0;
        var cont3 = 0;

        lista = new Array();
        listaCodigosSubseccion = new Array();
        listaSubsecciones = new Array();
    <logic:iterate id="elemento" name="MantenimientoTiposForm" property="tipos">
        lista[cont] = ['<bean:write name="elemento" property="codigoSubseccion" />', '<bean:write name="elemento" property="subseccion" />', '<bean:write name="elemento" property="codigo" />', '<bean:write name="elemento" property="descripcion"/>'];
        cont = cont + 1;
    </logic:iterate>
    <logic:iterate id="elemento" name="MantenimientoTiposForm" property="subsecciones">
        listaCodigosSubseccion[cont2] = ['<bean:write name="elemento" property="codigo" />'];
        listaSubsecciones[cont2] = ['<bean:write name="elemento" property="descripcion"/>'];
        cont2 = cont2 + 1;
    </logic:iterate>

        tab.lineas=lista;
        refresh();
        comboSubsecciones.addItems(listaCodigosSubseccion, listaSubsecciones);
    }

    function refresh(){
        tab.displayTabla();
    }

</script>

<script> Inicio(); </script>

</BODY>

</html>
