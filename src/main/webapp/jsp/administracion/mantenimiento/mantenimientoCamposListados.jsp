<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.administracion.mantenimiento.CamposListadosParametrizablesVO"%>
<%@page language="java" contentType="text/html" pageEncoding="ISO-8859-15"%>


<%@page import="java.util.Vector"%>

<html>
    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-15">
        <title>Mantenimiento de Procesos </title>
        <!-- Estilos -->
   
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 5;
            String estilo="";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                estilo=usuarioVO.getCss();
            }%>
            
        <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=estilo%>">
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        
<script type="text/javascript">
    
var lista = new Array();
var lista2 = new Array();

// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
function inicializar(){
    window.focus();
    cargaTablaCampos();
}

function pulsarModificar(){
    //esta funcion solo modifica el listado en javascript no la bbdd
    //el vector lista es el que se modifica para la vista
    //el vecto lista2 queda preparado para grabar en la bbdd
    if (!document.forms[0].activo.checked) {
        document.forms[0].tamanoCampo.value = '0';
    }

    var id=tablaCampos.selectedIndex;
    if(tablaCampos.selectedIndex != -1){
        var tamano = document.forms[0].tamanoCampo.value;
        if (tamano == '') tamano = 0;
        if (tamano == 0) document.forms[0].activo.checked = false;

        if(tamano<=100){
            lista[id][2] = tamano + '%';
            lista2[id][2] = tamano;

            var activo;
            if (document.forms[0].activo.checked){
                activo='SI';
            }else{
                activo='NO';
            }
            lista[id][3]=activo;
            lista2[id][3]=activo;
            limpiarInputs();
        }else{ 
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjAnchuraMenor100")%>');
        }
    }else{
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }
    
    tablaCampos.lineas=lista;
    tablaCampos.displayTabla();
    tablaCampos.selectLinea(id);
}

function pulsarGrabar(){
    //Graba los valores almacenados en el vector lista en la bbdd
    var listas=crearListas();
    document.forms[0].listaCodCampo.value = listas[0];
    document.forms[0].listaNomCampo.value = listas[1];
    document.forms[0].listTamanoCampo.value = listas[2];
    document.forms[0].listaActCampo.value = listas[3];
    
    document.forms[0].opcion.value = 'grabar';
    document.forms[0].target = "mainFrame";
    document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Listados.do';
    document.forms[0].submit();
}

function crearListas() {
    var listaCodCampo = "";
    var listaNomCampo= "";
    var listTamanoCampoAnt = new Array();
    var listTamanoCampo = "";
    var listaActCampo = "";
    
    //lista2 contiene todos los campos de la tabla
    //lo separamos para hacer el insert
    for (i=0; i < lista2.length; i++) {
        listaCodCampo +=lista2[i][0]+'§¥';
        listaNomCampo +=lista2[i][1]+'§¥';
        if(lista2[i][3]=='NO'){
            listTamanoCampoAnt[i]='0';
        }else{
            listTamanoCampoAnt[i]= lista2[i][2];
        }
        listaActCampo += lista2[i][3]+'§¥';
    }
    
    //calcular el ancho de las columnas antes de crear las listas
    listTamanoCampo=calcularTamano(listTamanoCampoAnt);
    listaCampos = [listaCodCampo,listaNomCampo,listTamanoCampo,listaActCampo];
    return listaCampos;
}

function calcularTamano(listTamanoCampoAnt) {
    var listTamanoCampo = "";
    var suma=0;
    for (i=0; i < listTamanoCampoAnt.length; i++) {
        suma=suma+parseInt(listTamanoCampoAnt[i]);
    }
    for (i=0; i < listTamanoCampoAnt.length; i++) {
        listTamanoCampo+=(100*parseInt(listTamanoCampoAnt[i]))/suma+'§¥';
    }   
    return listTamanoCampo;
}

function pulsarCancelar(){
    document.forms[0].opcion.value ="salir";
    document.forms[0].target = "mainFrame";
    document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Listados.do';
    document.forms[0].submit();
}
</script>
</head>

<body class="bandaBody" onload="javascript:{pleaseWait('off');
    inicializar();}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<form  method="post">
    <input  type="hidden"  name="opcion" id="opcion">
    <input  type="hidden"  name="codigo" value="">
    <input  type="hidden" name="descripcion" value="">
    <input  type="hidden" name="listaCodCampo" value="">
    <input  type="hidden" name="listaNomCampo" value="">
    <input  type="hidden" name="listTamanoCampo" value="">
    <input  type="hidden" name="listaActCampo" value="">
    
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gTit_MantListCamp")%>&nbsp;<bean:write name="MantenimientosAdminForm" property="nomCampo"/></div>
    <div class="contenidoPantalla" >
        <table >
            <tr> 
                <td id="tabla"></td>
            </tr>
            <tr>
                <td>
                    <input name="codCampo" type="hidden" class="inputTextoObligatorio" style="width:0%" maxlength="80"  readOnly> 
                    <input name="nomCampo" type="text" class="inputTextoObligatorio" style="width:77%" maxlength="80" readOnly> 
                    <input name="tamanoCampo" type="text" class="inputTextoObligatorio" style="width:11%" maxlength="3" onKeyPress="return SoloDigitosNumericos(this);">
                    <input type="checkbox"  id="obligatorio" name="activo" onkeyup="return xAMayusculas(this);" style="margin-left:4.5%">
                </td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> 
                   name="botonModificar" onClick="pulsarModificar();" accesskey="M">  
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%> 
                   name="botonLimpiar" onClick="limpiarInputs();" accesskey="L">  
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbGrabar")%> 
                   name="botonGrabar" onClick="pulsarGrabar();" accesskey="g">  
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> 
                   name="botonCancelar" onClick="pulsarCancelar();" accesskey="S">  
        </div>                        
    </div>                        
</form>

<script type="text/javascript">  
            
//Creamos tablas donde se cargan las listas
tablaCampos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

// Anadir tipo de dato a cada columna;
tablaCampos.addColumna('0','center',"<str:escape><%=descriptor.getDescripcion("etiqCodigoAsunto")%></str:escape>");
tablaCampos.addColumna('675','left',"<str:escape><%=descriptor.getDescripcion("etiqNomCampo")%></str:escape>");
tablaCampos.addColumna('100','center',"<str:escape><%=descriptor.getDescripcion("etiqAnchura")%></str:escape>");
tablaCampos.addColumna('100','center',"<str:escape><%=descriptor.getDescripcion("etiqIncluido")%></str:escape>");
tablaCampos.displayCabecera=true;
            
tablaCampos.displayTabla();

function cargaTablaCampos(){
    var i = 0;    
    <logic:iterate id="campos" name="MantenimientosAdminForm" property="camposListados">
        lista[i] = ['<bean:write name="campos" property="codCampo" />',
                '<bean:write name="campos" property="nomCampo" />',
                '<bean:write name="campos" property="tamanoCampo"/>'+'%',
                '<bean:write name="campos" property="actCampo"/>'];
        
        lista2[i] = ['<bean:write name="campos" property="codCampo" />',
                '<bean:write name="campos" property="nomCampo" />',
                '<bean:write name="campos" property="tamanoCampo"/>',
                '<bean:write name="campos" property="actCampo"/>'];
        i++;
    </logic:iterate>
    
    tablaCampos.lineas=lista;
    tablaCampos.displayTabla();
}

function limpiarInputs(){
    document.forms[0].nomCampo.value = "";
    document.forms[0].tamanoCampo.value = "";
    document.forms[0].activo.checked=false;
}

function rellenarDatos(tableName,rowID){
    var i = rowID;
    limpiarInputs();
    
    if((i>=0)&&!tableName.ultimoTable){
        document.forms[0].codCampo.value = lista2[i][0];
        document.forms[0].nomCampo.value = lista2[i][1];
        document.forms[0].tamanoCampo.value = lista2[i][2];
        if(lista2[rowID][3]=='SI') {
            document.forms[0].activo.checked=true;
        }else{
            document.forms[0].activo.checked=false;
        }
    }
}
</script>
</body>
</html>
