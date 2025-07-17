<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionExpedientesForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html:html>

<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

<TITLE>::: Lista de interesados :::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />

<!-- Estilos -->


<%
  int idioma=1;
  int apl=1;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
        }
  }
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>

<SCRIPT type="text/javascript">

var datos = new Array();
var datosOriginal = new Array();
var cod_rol = new Array();
var desc_rol = new Array();
var cont = 0;
var checkEntreg1 = "<input type='checkbox' class='check' name='box";
var checkEntreg2 = "' value='SI'>";
var cMun = "";
var cProc = "";
var numExp = "";
var ejerc = "";
<% TramitacionExpedientesForm tEF = new TramitacionExpedientesForm();
  tEF = (TramitacionExpedientesForm)session.getAttribute("TramitacionExpedientesForm");
  Vector listaInteresados = new Vector();
  listaInteresados = tEF.getListaInteresados();
  for(int i=0;i<listaInteresados.size();i++) {
    GeneralValueObject g = new GeneralValueObject();
	g = (GeneralValueObject) listaInteresados.elementAt(i);
  %>
    var check = checkEntreg1+cont+checkEntreg2;
    datos[cont] = [check,"<%=(String)g.getAtributo("titular")%>","<%=(String)g.getAtributo("descRol")%>"];
	datosOriginal[cont] = ["<%=(String)g.getAtributo("codTercero")%>",
	                       "<%=(String)g.getAtributo("versTercero")%>","<%=(String)g.getAtributo("titular")%>",
						   "<%=(String)g.getAtributo("codRol")%>","<%=(String)g.getAtributo("descRol")%>",
						   "<%=(String)g.getAtributo("rolPorDefecto")%>","<%=(String)g.getAtributo("codMunicipio")%>",
						   "<%=(String)g.getAtributo("codProcedimiento")%>","<%=(String)g.getAtributo("numeroExpediente")%>",
						   "<%=(String)g.getAtributo("ejercicio")%>"];
	cont++;
<% } %>

function inicializar() {
  window.focus();
  <%
		Vector listaRoles = new Vector();
		listaRoles = (Vector) tEF.getListaRoles();
		int lengthRoles = listaRoles.size();
	%>
	var m=0;
	<%
		for(int t=0;t<lengthRoles;t++){
	%>      
			cod_rol[m] = [	"<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("codRol")%>"];
			desc_rol[m] = ["<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("descRol")%>"];
			m++;
	<%
		}
	%>
  comboRol.addItems(cod_rol,desc_rol);
  tab.lineas=datos;
  refresca();
  window.focus();
  if(datosOriginal.length >0) {
    cMun =  datosOriginal[0][6];
    cProc = datosOriginal[0][7];
    numExp = datosOriginal[0][8];
    ejerc = datosOriginal[0][9];
  }
}

function pulsarAceptar() {
  var lC = crearListaCodigos();
  var lV = crearListaVersiones();
  if(lC != "" && lV!="") {
    var retorno = new Array();
    retorno = ["si",lC,lV];
    self.parent.opener.retornoXanelaAuxiliar(retorno);
  } else {
    jsp_alerta('A',"<%=descriptor.getDescripcion("msjNoSelecFila")%>");
  }
}

function crearListaCodigos() {
  var listaCodInteresados = "";
  for (i=0; i < datos.length; i++) {
    var caja = "box" + i;
    if(eval("document.forms[0]." + caja + ".checked") == true) {
      listaCodInteresados += datosOriginal[i][0]+'зе';
    }
  }
  return listaCodInteresados;
}

function crearListaVersiones() {
  var listaVersInteresados = "";
  for (i=0; i < datos.length; i++) {
    var caja = "box" + i;
    if(eval("document.forms[0]." + caja + ".checked") == true) {
      listaVersInteresados += datosOriginal[i][1]+'зе';
    }
  }
  return listaVersInteresados;
}

function pulsarCancelar() {
  self.parent.opener.retornoXanelaAuxiliar();
}

function pulsarTodos() {
  for(var i=0;i<datos.length;i++) {
    var box = "box" + i;
	eval("document.forms[0]." + box + ".checked= true");
  }
}

function pulsarNinguno() {
  for(var i=0;i<datos.length;i++) {
    var box = "box" + i;
	eval("document.forms[0]." + box + ".checked= false");
  }
}

function actualizarTabla() {
  if(datosOriginal.length == 0) {
    document.forms[0].codMunicipio.value = cMun;
	document.forms[0].codProcedimiento.value = cProc;
	document.forms[0].numeroExpediente.value = numExp;
	document.forms[0].ejercicio.value = ejerc;
  } else {
    document.forms[0].codMunicipio.value = datosOriginal[0][6];
    document.forms[0].codProcedimiento.value = datosOriginal[0][7];
    document.forms[0].numeroExpediente.value = datosOriginal[0][8];
    document.forms[0].ejercicio.value = datosOriginal[0][9];
  }
  document.forms[0].opcion.value="actualizarTablaInteresados";
  document.forms[0].target="oculto";
  document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
  document.forms[0].submit();
}

function actualizaTablaInteresados(lista,lista1) {
  datos = lista;
  datosOriginal = lista1;
  tab.lineas=datos;
  refresca();
}

</SCRIPT>

</head>

<BODY class="bandaBody" onload="javascript:{inicializar();}">

<html:form action="/sge/TramitacionExpedientes.do" target="_self">

<html:hidden  property="opcion" value=""/>
<input type="Hidden" name="codMunicipio">
<input type="Hidden" name="codProcedimiento">
<input type="Hidden" name="numeroExpediente">
<input type="Hidden" name="ejercicio">

<table id="tabla1" style="width: 100%;" cellpadding="0px" cellspacing="0px">
    <tr>
        <td id="titulo" style="width: 100%; height: 25px" class="titulo">&nbsp;<%=descriptor.getDescripcion("etiq_listaInt")%></td>
    </tr>
    <tr>
        <div class="contenidoPantalla" valign="top">
            <table cellspacing="0px" cellpadding="0px" border="0px" style="padding-top: 10px; padding-bottom:10px">
                <tr>
                    <td align="left">
                        <table width="100%" cellpadding="0px" cellspacing="0px">
                            <tr>
                                <td style="width: 10%" class="etiqueta">
                                    <%=descriptor.getDescripcion("etiqRol")%>
                                </td>
                                <td>
                                    <input type="text" class="inputTexto" name="codRol" size="6" onkeypress="javascript:return SoloDigitos(event);"/>
                                    <input type="text" class="inputTexto" name="descRol" style="width:300;height:17" readonly="true"/>
                                    <A href="" id="anchorRol" name="anchorRol">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonRol" name="botonRol" style="border: 0px none"></span>
                                    </A>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td id="tabla" style="width: 100%" align="center"></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<div class="botoneraPrincipal">
    <input type="button" class="botonGeneral" accesskey="T" value=<%=descriptor.getDescripcion("gbTodos")%> name="cmdTodos" onClick="pulsarTodos();">
    <input type="button" class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar" onClick="pulsarAceptar();"/>
    <input type="button" class="botonGeneral" accesskey="C" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdCancelar" onClick="pulsarCancelar();">
</div>
</html:form>

<script type="text/javascript">
tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

tab.addColumna('40','center','');
tab.addColumna('350','center',"<%= descriptor.getDescripcion("etiq_IntPrin")%>");
tab.addColumna('245','center',"<%= descriptor.getDescripcion("etiqRol")%>");
tab.displayCabecera=true;  
tab.displayTabla();

function refresca() {
  tab.displayTabla();
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

document.onmousedown = checkKeys;

function checkKeysLocal(evento,tecla) {
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else
        teclaAuxiliar = evento.which;

    if (teclaAuxiliar == 38 || teclaAuxiliar == 40){
		upDownTable(tab,datos,teclaAuxiliar);
	}
        if (teclaAuxiliar == 1){
            if (comboRol.base.style.visibility == "visible" && isClickOutCombo(comboRol,coordx,coordy)) setTimeout('comboRol.ocultar()',20);
        }
        if (teclaAuxiliar == 9){
            comboRol.ocultar();
        }
	keyDel(evento);
}

var comboRol = new Combo("Rol");

comboRol.change = function() { 		
  actualizarTabla();			
}


</script>

</BODY>

</html:html>
