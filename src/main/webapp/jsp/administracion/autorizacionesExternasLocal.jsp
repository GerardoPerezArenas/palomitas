<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.administracion.AutorizacionesExternasForm" %>
<%@page import="java.util.Vector"%>

<html:html>
<head>
    <jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
    <TITLE>::: ADMINISTRACIÓN LOCAL: Autorizaciones Externas Local(Usuarios-Aplicaciones):::</TITLE>

<!-- Estilos -->


<%
  int idioma=1;
  int apl=1;
  int codOrg = 0;
  int codEnt = 0;
  if (session!=null){
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    if (usuario!=null) {
      idioma = usuario.getIdioma();
      apl = usuario.getAppCod();
	  codOrg = usuario.getOrgCod();
	  codEnt = usuario.getEntCod();
    }
  }%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
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
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
<SCRIPT type="text/javascript">
var listaUsuarios = new Array();
var listaUsuariosOriginal = new Array();
var listaAplicaciones = new Array();
var listaAplicacionesOriginal = new Array();
var lineaUsuario = -1;
function inicializar() {
  <% 
      AutorizacionesExternasForm bForm = (AutorizacionesExternasForm)session.getAttribute("AutorizacionesExternasForm");
    %>
    var j=0;
    <%
      Vector listaUsuarios = bForm.getListaUsuarios();
      if (listaUsuarios == null) listaUsuarios = new Vector();
    %>
	j=0;
	<%for(int i=0;i<listaUsuarios.size();i++){
        GeneralValueObject aplicacion = (GeneralValueObject)listaUsuarios.get(i);%>
      	listaUsuarios[j] = ['<%=(String) aplicacion.getAtributo("nombreUsuario")%>'];
      	listaUsuariosOriginal[j++] = ['<%=(String) aplicacion.getAtributo("codUsuario")%>',
		                              '<%=(String) aplicacion.getAtributo("nombreUsuario")%>'];
    <%}%>
  
  tabUsuarios.lineas=listaUsuarios;
  refrescaUsuarios();
  
  tabAplicaciones.lineas=listaAplicaciones;
  refrescaAplicaciones();
}

function pulsarSalir() {
  document.forms[0].opcion.value = 'cargar';
  document.forms[0].target = "mainFrame";
  document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/UORs.do';
  document.forms[0].submit();
}

function callFromTableTo(rowID,tableName){
  if(tabUsuarios.id == tableName){
    buscarListaAplicacionesUsuarios(rowID);
  } else if(tabAplicaciones.id == tableName){
    
  }
}

function buscarListaAplicacionesUsuarios(rowID) {
  listaAplicaciones = new Array();
  listaAplicacionesOriginal = new Array();
  tabAplicaciones.lineas=listaAplicaciones;
  refrescaAplicaciones();
  if(lineaUsuario == rowID) {
    lineaUsuario = -1;
  } else {
    document.forms[0].codUsuario.value = listaUsuariosOriginal[rowID][0];
    document.forms[0].opcion.value="buscarAplicacionesUsuariosAdmLocal";
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/administracion/AutorizacionesExternas.do";
    document.forms[0].submit();
	lineaUsuario = rowID;
  }
}

function actualizarTablaAplicaciones(lista1,lista2) {
  listaAplicaciones = lista1;
  listaAplicacionesOriginal = lista2;
  tabAplicaciones.lineas=listaAplicaciones;
  refrescaAplicaciones();
}

function crearListas() {
  var lUsuarios = "";
  var lAplicaciones = "";
  var lUsuar = "";
  var lAplic = "";
  var lOrg = "";
  var lEnt = "";
  for (i=0; i < listaAplicaciones.length; i++) {
    var caja = "boxA" + i;
	lUsuarios +=listaAplicacionesOriginal[i][2]+'§¥';
    if(eval("document.forms[0]." + caja + ".checked") == true) {
	  lAplicaciones +=listaAplicacionesOriginal[i][1]+'§¥';
	}
  }
  
  lUsuar += listaUsuariosOriginal[lineaUsuario][0]+'§¥';
  lAplic += lAplicaciones;
  lOrg +=<%=codOrg%>+'§¥';
  lEnt +=<%=codEnt%>+'§¥';
  var listas = new Array();
  listas = [lAplicaciones,lUsuarios,lOrg,lEnt,lUsuar,lAplic];
  return listas;
}

function pulsarGrabar() {
  if(tabUsuarios.selectedIndex != -1) {
	  var listas = crearListas();
	  document.forms[0].lAplicaciones.value = listas[0];
	  document.forms[0].lUsuarios.value = listas[1];
	  document.forms[0].lOrg.value = listas[2];
	  document.forms[0].lEnt.value = listas[3];
	  document.forms[0].lUsuar.value = listas[4];
	  document.forms[0].lAplic.value = listas[5];
	  if(listas[5] != "") {  
	    document.forms[0].opcion.value="grabarListasAdmLocal";
	    document.forms[0].target="oculto";
	    document.forms[0].action="<%=request.getContextPath()%>/administracion/AutorizacionesExternas.do";
	    document.forms[0].submit();
	  } else {
	    jsp_alerta('A','<%=descriptor.getDescripcion("msjAplicNoEleg")%>');
	  }
  } else {
    jsp_alerta('A','<%=descriptor.getDescripcion("msjUsuarNoEleg")%>');
  }
}

function listasGrabadas() {
  jsp_alerta('A','<%=descriptor.getDescripcion("msjGrabCorrect")%>');
  listaAplicaciones = new Array();
  listaAplicacionesOriginal = new Array();
  tabAplicaciones.lineas=listaAplicaciones;
  refrescaAplicaciones();
  if(tabUsuarios.selectedIndex != -1 ) {
    tabUsuarios.selectLinea(tabUsuarios.selectedIndex);
    tabUsuarios.selectedIndex = -1;
  }
  lineaUsuario = -1;
  lineaAplicacion = -1;
}
</SCRIPT>
</head>
<body class="bandaBody"  onload="javascript:{ pleaseWait('off'); inicializar()}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<html:form action="/administracion/AutorizacionesExternas.do" target="_self">

<html:hidden  property="opcion" value=""/>
<input  type="hidden" name="codUsuario">
<input  type="hidden" name="codAplic">
<input type="hidden" name="lUsuarios" value="">
<input type="hidden" name="lAplicaciones" value="">
<input type="hidden" name="lUsuar" value="">
<input type="hidden" name="lOrg" value="">
<input type="hidden" name="lEnt" value="">
<input type="hidden" name="lAplic" value="">

<div class="txttitblanco"><%=descriptor.getDescripcion("tit_usuarAplic")%></div>
<div class="contenidoPantalla">
    <TABLE width="100%" height="350px">
        <TR>
            <td align="center" id="tablaUsuarios">
            </td>
         </tr> 
        <tr>
            <td height="5px"></td>
         </tr> 
        <tr>
              <td align="center" id="tablaAplicaciones"></td>
        </tr>
    </table>
    <div class="botoneraPrincipal"> 
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbGrabar")%> name="cmdGrabar" onClick="pulsarGrabar();" accesskey="G">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir" onClick="pulsarSalir();" accesskey="S">
    </div>				
</div>				
</html:form>



<script language="JavaScript1.2">

// JAVASCRIPT DE LA TABLA USUARIOS

var tabUsuarios = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaUsuarios'));

tabUsuarios.addColumna('660','left','<%= descriptor.getDescripcion("tit_usuar")%>');
tabUsuarios.displayCabecera=true;

function refrescaUsuarios() {
  tabUsuarios.displayTabla();
}

tabUsuarios.displayDatos = pintaDatosUsuarios;

function pintaDatosUsuarios() {
  tableObject = tabUsuarios;
}


// FIN DE LOS JAVASCRIPT'S DE LA TABLA USUARIOS

// JAVASCRIPT DE LA TABLA APLICACIONES

var tabAplicaciones = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaAplicaciones'));

tabAplicaciones.addColumna('60','center','<%= descriptor.getDescripcion("etiq_Autor")%>');
tabAplicaciones.addColumna('594','left','<%= descriptor.getDescripcion("gEtiq_Aplics")%>');
tabAplicaciones.displayCabecera=true;
tabAplicaciones.readOnly=true;

function refrescaAplicaciones() {
  tabAplicaciones.displayTabla();
}

tabAplicaciones.displayDatos = pintaDatosAplicaciones;

function pintaDatosAplicaciones() {
  tableObject = tabAplicaciones;
}


// FIN DE LOS JAVASCRIPT'S DE LA TABLA APLICACIONES

document.onmouseup = checkKeys;

function checkKeysLocal(evento,tecla){

      var teclaAuxiliar="";
      if(window.event){
        evento = window.event;
        teclaAuxiliar =evento.keyCode;
      }else
            teclaAuxiliar =evento.which;

  if(teclaAuxiliar == 40){
    if(tabUsuarios==tableObject) {
      upDownTable(tabUsuarios,listaUsuarios,teclaAuxiliar);
    } else if(tabAplicaciones==tableObject) {
      upDownTable(tabAplicaciones,listaAplicaciones,teclaAuxiliar);
    }
  }
  if (teclaAuxiliar == 38){
    if(tabUsuarios==tableObject) {
      upDownTable(tabUsuarios,listaUsuarios,teclaAuxiliar);
    } else if(tabAplicaciones==tableObject) {
      upDownTable(tabAplicaciones,listaAplicaciones,teclaAuxiliar);
    }
  }

  if(teclaAuxiliar == 13){
    if((tabUsuarios.selectedIndex>-1)&&(tabUsuarios.selectedIndex < listaUsuarios.length)&&(!ultimo)){
      if(tabUsuarios==tableObject)
	    callFromTableTo(tabUsuarios.selectedIndex,tabUsuarios.id);
    }
	if((tabAplicaciones.selectedIndex>-1)&&(tabAplicaciones.selectedIndex < listaAplicaciones.length)&&(!ultimo)){
      if(tabAplicaciones==tableObject)
	    callFromTableTo(tabAplicaciones.selectedIndex,tabAplicaciones.id);
    }
  }
  keyDel(evento);
}

</script>

</BODY>

</html:html>
