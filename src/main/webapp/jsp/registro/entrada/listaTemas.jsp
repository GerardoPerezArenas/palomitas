<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.agora.technical.ConstantesDatos"%>
<%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = ConstantesDatos.APP_REGISTRO_ENTRADA_SALIDA;
            String css = "";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
                css = usuarioVO.getCss();
            }
%>
<jsp:useBean id="descriptor" scope="request"
             class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
             type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor" property="idi_cod" value="<%=idioma%>" />
<jsp:setProperty name="descriptor" property="apl_cod" value="<%=apl%>" />
<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
 <title>Asientos relacionados</title>
 <%@ include file="/jsp/plantillas/Metas.jsp" %>
 
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
 <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
 <script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
 <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
 <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
 <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
 <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
 <script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
 <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
 <script type="text/javascript">

var tab;

var listaCodActuaciones = new Array();
var listaDescActuaciones = new Array();
var i = 0;
<c:forEach var="actuacion" items="${requestScope.actuaciones}">
    listaCodActuaciones[i] = '<c:out value="${actuacion.codigo}"/>';
    listaDescActuaciones[i] = '<c:out value="${actuacion.descripcion}"/>';
    i++;    
</c:forEach>
    
var listaCodTemas = new Array();
var listaDescTemas = new Array();
i = 0;
<c:forEach var="tema" items="${requestScope.temas}">
    listaCodTemas[i] = '<c:out value="${tema.codigo}"/>';
    listaDescTemas[i] = '<c:out value="${tema.txtNomeDescripcion}"/>';
    i++;    
</c:forEach>
    
var listaTemas = new Array();
i = 0;
<c:forEach var="tema" items="${requestScope.temasSeleccionados}">
    listaTemas[i] = ['<c:out value="${tema.codigo}"/>', '<c:out value="${tema.txtNomeDescripcion}"/>'];    
    i++;     
</c:forEach>
    
var modificando;

function inicializar() {
    comboActuaciones.addItems(listaCodActuaciones, listaDescActuaciones);
    var codSeleccionado = '<c:out value="${requestScope.codActuacion}"/>';
    var index = -1;
    for (var j = 0; j < listaCodActuaciones.length; j++) {
        if (listaCodActuaciones[j] == codSeleccionado) {
            index = j;            
        }
    }
    comboActuaciones.selectItem(index+1);
    comboTemas.addItems(listaCodTemas, listaDescTemas);
    comboTemas.selectItem(0);
    
    modificando = '<c:out value="${requestScope.modificando}"/>';
    if (modificando == 'N') {
        comboActuaciones.deactivate();
        comboTemas.deactivate();
        domlay('botonAlta',1,0,0,"&nbsp;");        
    }
    
    tratarModificar('N');
    
    cargaTabla();
}

/* Cierra la ventana de dialogo */
function pulsarSalir() {
    var resultados = new Array();
    resultados[0] = document.forms[0].codActuacion.value;
    resultados[1] = listaTemas;
    self.parent.opener.retornoXanelaAuxiliar(String(resultados));
}


  </script>
</head>
<body class="bandaBody" onload="inicializar();">
<html:form action="MantAnotacionRegistro">
<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiqClasifTemas")%></div>
<div class="contenidoPantalla">
    <div style="width:100%">
        <span style="width: 8%" class="etiqueta"> <%=descriptor.getDescripcion("res_actuacion")%>: </span>
        <span class="columnP">
            <input type="text" class="inputTexto" id="codActuacion" style="width:8%"/>
            <input type="text" class="inputTexto" id="descActuacion" style="width:70%" readonly="true"/>
            <A style="text-decoration:none;" id="anchorActuacion" name="anchorActuacion">
                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp" name="desp" style="cursor:hand;"></span>
            </A>
        </span>
    </div>
    <!-- fin actuacion -->
    <div style="width:100%;margin-top:15px" id="tabla" align="left"></div>
    <div style="width:100%">
        <span class="columnP">
            <input type="text" class="inputTexto" id="codTema" style="width:8%"/>
            <input type="text" class="inputTexto" id="descTema" style="width:70%" readonly="true"/>                                                                    
            <A style="text-decoration:none;" id="anchorTema" name="anchorTema">
                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp" name="desp" style="cursor:hand;"></span>
            </A>                                                                    
        </span>
        <span id="botonAlta" style="margin-left: 1%" class="columnP">
            <a id="boton" onclick="add()">
                <span class="fa fa-paperclip" name="anadir" alt=""></span>
            </a>
        </span>
        <span id="botonesEdicion" style="margin-left: 1%" class="columnP">
            <a id="boton" onclick="del()">
                <span class="fa fa-trash" name="eliminar"></span>
            </a>
        </span>
    </div>
    <div class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelar" onClick="self.parent.opener.retornoXanelaAuxiliar();" accesskey="C">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSalir")%>" name="cmdSalir" onClick="pulsarSalir();" accesskey="S">
    </div>
    <div id="desplegable" style="overflow-y: auto; overflow-x: no; visibility: hidden; BORDER: 0px"></div>
</div>
</html:form>
<script type="text/javascript" src="<html:rewrite page='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript">
var comboActuaciones = new Combo("Actuacion");
var comboTemas = new Combo("Tema");

tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

tab.addColumna('100','center','<%= descriptor.getDescripcion("gEtiq_codigo")%>');
tab.addColumna('690','center','<%= descriptor.getDescripcion("gEtiq_desc")%>');
tab.displayCabecera=true;
tab.displayTabla();
    
function cargaTabla() {
    tab.lineas=listaTemas;
    tab.displayTabla();
}

function add() {
    if ((document.forms[0].codTema.value != '') && (document.forms[0].descTema.value != '') &&  (comprobarCodigo2())) {
        listaTemas[listaTemas.length]=[document.forms[0].codTema.value, document.forms[0].descTema.value];
        tab.lineas=listaTemas;
        
        tab.displayTabla();    
        borrarDatos();
    }
}

function del(){
    var j=tab.selectedIndex;
    if(tab.selectedIndex<0){
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoElimTema")%>');
        return;
    }
    if(jsp_alerta('', '<%=descriptor.getDescripcion("msjPregElimTem")%>'+tab.lineas[tab.selectedIndex][1]+'?')) {
        var list = new Array();
        tamIndex=tab.selectedIndex;
        tamLength=tab.lineas.length;
        for (i=tamIndex - 1; i < listaTemas.length - 1; i++){
            if (i + 1 <= listaTemas.length - 2){
                listaTemas[i + 1]=listaTemas[i + 2];
            }
        }
        for(j=0; j<listaTemas.length-1 ; j++){
            list[j] = listaTemas[j];
        }
        tab.lineas=list;
        tab.displayTabla();
        listaTemas=list;
        borrarDatos();
    } else {
        tab.selectLinea(tab.selectedIndex);
    }
}

function comprobarCodigo2(){
    for (i=0; i < tab.lineas.length; i++){
        if (tab.lineas[i][0] == document.forms[0].codTema.value) {
            jsp_alerta('A','<%=descriptor.getDescripcion("msjCodigoRep")%>');
            return false;
        }
    }
    return true;
}

function pintaDatos(datos) {
    // Carga los inputs con los valores de la fila seleccionada
    if (modificando) {        
        document.forms[0].codTema.value = datos[0];
        document.forms[0].descTema.value = datos[1];
        if(datos[0]=='') tratarModificar('N');
        else tratarModificar('S');
    }
}

tab.displayDatos = pintaDatos;
                  
// Temas
function borrarDatos() {
    document.forms[0].codTema.value = '';
    document.forms[0].descTema.value = '';
    tratarModificar('N');
}

function tratarModificar(estado) {
    var htmlString = '&nbsp;';
    if (estado == 'S') {
        if (modificando == 'S') {
            htmlString += '<a id="boton" href="javascript:del();">';
            htmlString += '<span class="fa fa-trash" name="eliminar"></span>';
            htmlString += '</a>';
        } 
    }
    domlay('botonesEdicion',1,0,0,htmlString);
}

<%String userAgent = request.getHeader("user-agent");%>

var coordx=0;
var coordy=0;


<%if(userAgent.indexOf("MSIE")==-1) {%> //Que no sea IE
    window.addEventListener('mousemove', function(e) {
        coordx = e.clientX;
        coordy = e.clientY;
    }, true);
<%}%>

document.onmouseup = checkKeys;

function checkKeysLocal(evento,tecla) {
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else{
        teclaAuxiliar = evento.which;
    }


	if (teclaAuxiliar == 1){
            if (comboActuaciones.base.style.visibility == "visible" && isClickOutCombo(comboActuaciones,coordx,coordy)) setTimeout('comboActuaciones.ocultar()',20);
            if (comboTemas.base.style.visibility == "visible" && isClickOutCombo(comboTemas,coordx,coordy)) setTimeout('comboTemas.ocultar()',20);
        }
        if (teclaAuxiliar == 9){
             if (comboActuaciones.base.style.visibility == "visible")comboActuaciones.ocultar();
             if (comboTemas.base.style.visibility == "visible")comboTemas.ocultar();
        }
}
    </script>
    </body>
</html>
