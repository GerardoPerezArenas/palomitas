<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html:html>

<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

<TITLE>::: Lista de interesados :::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />



<%
  int idioma=1;
  int apl=1;
  String css = "";
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
        css = usuario.getCss();
                }
            }

    %>
  
<!-- Estilos -->
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
  <link rel="stylesheet" href="<%=request.getContextPath()%><%=css%>" type="text/css">
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>

<SCRIPT language="JavaScript">

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
<%      MantAnotacionRegistroForm mantARForm = (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm");
  Vector listaInteresados = new Vector();
  listaInteresados = mantARForm.getListaInteresados();
  for(int i=0;i<listaInteresados.size();i++) {
    GeneralValueObject g = new GeneralValueObject();
	g = (GeneralValueObject) listaInteresados.elementAt(i);
  %>
    var check = checkEntreg1+cont+checkEntreg2;
    datos[cont] = [check,"<%=(String)g.getAtributo("titular")%>","<%=(String)g.getAtributo("descRol")%>"];
    datosOriginal[cont] = ["<%=(String)g.getAtributo("codigoTercero")%>",
                            "<%=(String)g.getAtributo("versionTercero")%>","<%=(String)g.getAtributo("titular")%>",
                            "<%=(String)g.getAtributo("rol")%>","<%=(String)g.getAtributo("descRol")%>",
                            "<%=(String)g.getAtributo("porDefecto")%>","<%=(String)g.getAtributo("codProcedimiento")%>",
                            "<%=(String)g.getAtributo("ejercicio")%>","<%=(String)g.getAtributo("uor")%>",
                            "<%=(String)g.getAtributo("tip")%>","<%=(String)g.getAtributo("num")%>","<%=(String)g.getAtributo("dep")%>",
                            "<%=(String)g.getAtributo("munProc")%>"];

        cont++;
  
<% } %>


function inicializar() {
  window.focus();
  <%
		Vector listaRoles = new Vector();
		listaRoles = (Vector) mantARForm.getListaRoles();
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
  self.parent.opener.retornoXanelaAuxiliar(retorno);
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
  document.forms[0].ejercicio.value=datosOriginal[0][7];
  document.forms[0].numero.value=datosOriginal[0][10];
  document.forms[0].codTip.value=datosOriginal[0][9];
  document.forms[0].codOur.value=datosOriginal[0][8];
  document.forms[0].codProcedimiento.value=datosOriginal[0][6];
  document.forms[0].munProc.value=datosOriginal[0][12];
    document.forms[0].rol.value=cod_rol[comboRol.selectedIndex-1];
     document.forms[0].roldes.value=desc_rol[comboRol.selectedIndex-1];
  
     
    document.forms[0].opcion.value="actualizarTablaInteresados";
    document.forms[0].target="oculto";
    document.forms[0].action="<html:rewrite page='/MantAnotacionRegistro.do'/>";
    document.forms[0].submit();
}

function actualizaTablaInteresados(lista,lista1) {
    if(lista[0][1]==""){
          datos = "";
      tab.lineas=datos;
      refresca();
    }else{
      datos = lista;
      tab.lineas=datos;
      refresca();
  }
     datosOriginal = lista1;
}

</SCRIPT>

</head>

<BODY class="bandaBody" onload="javascript:{inicializar();}">
<html:form action="/MantAnotacionRegistro.do" target="_self">
<html:hidden  property="opcion" value=""/>
<input type="Hidden" name="numeroExpediente">
<input type="Hidden" name="codMunicipio">
<input type="Hidden" name="ejercicio">
<input type="hidden" name="numero">
<input type="hidden" name="codTip">
<input type="hidden" name="codOur">
<input type="Hidden" name="codProcedimiento">
<input type="Hidden" name="munProc">
<input type="hidden" name="rol">
<input type="hidden" name="roldes">
<input type="hidden" name="dep">
        
<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("etiq_listaInt")%></div>
<div class="contenidoPantalla">
    <table cellspacing="0px" cellpadding="0px" border="0px">
        <tr>
            <td height="10px"></td>
        </tr>
        <tr>
            <td width="100%">
                <table width="100%" cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td width="35%" align="center" class="etiqueta">
                            <%=descriptor.getDescripcion("etiqRol")%>
                        </td>
                        <td width="65%">
                            <input type="text" class="inputTexto" name="codRol" size="3" onkeypress="javascript:return SoloDigitos(event);"/>
                            <input type="text" class="inputTexto" name="descRol" style="width:200;height:17" readonly="true"/>
                            <A href="" id="anchorRol" name="anchorRol">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonRol" name="botonRol"></span>
                            </A>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td height="10px"></td>
        </tr>
        <tr>
            <td	id="tabla" width="100%" align="center"></td>
        </tr>
    </table>
    <div class="botoneraPrincipal">           
        <input type="button" class="botonGeneral" accesskey="T" value=<%=descriptor.getDescripcion("gbTodos")%> name="cmdTodos" onClick="pulsarTodos();">
        <input type="button" class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar" onClick="pulsarAceptar();"/>
        <input type="button" class="botonGeneral" accesskey="C" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdCancelar" onClick="pulsarCancelar();">
    </div>
</div>
</html:form>

<script language="JavaScript1.2">
tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

tab.addColumna('40','center','');
tab.addColumna('230','center',"<%= descriptor.getDescripcion("etiq_IntPr")%>");
tab.addColumna('125','center',"<%= descriptor.getDescripcion("etiqRol")%>");
tab.displayCabecera=true;  
tab.displayTabla();

function refresca() {
  tab.displayTabla();
}

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
	keyDel(evento);
}

var comboRol = new Combo("Rol");

comboRol.change = function() { 		
  actualizarTabla();			
}


</script>

</BODY>

</html:html>
