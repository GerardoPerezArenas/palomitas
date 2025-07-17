<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html:html>

<head>

<TITLE>::: Consulta Temas :::</TITLE>

<!-- Estilos -->
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
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

<!-- Ficheros JavaScript -->

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>

<SCRIPT language="JavaScript">
function inicializar() {
  window.focus();
}

function pulsarCancelar() {
  self.close();
}


function pulsarBuscar() {
  if(document.forms[0].nombreTema.value != "") {
	document.forms[0].opcion.value="buscarTemas";
    document.forms[0].target="mainFrame";
    document.forms[0].action="<%=request.getContextPath()%>/MantTema.do";
    document.forms[0].submit();
  } else {
    jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>')
  }
}
</SCRIPT>

</head>

<BODY onload="javascript:{inicializar();}">

<html:form action="/MantTema.do" target="_self">

<input type="Hidden" name="opcion" value ="">

<table width="100%" height="20px" cellpadding="0px" cellspacing="0px">
	<tr>
		<td width="100%" height="10px"></td>
	</tr>
</table>

<table id="tabla1" width="450px" height="150px" align="center" cellpadding="0px" cellspacing="0px">
    <tr>
        <td width="449px" height="150px">
                <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                        <tr>
                                <td>
                                        <table width="100%" height="100%" cellpadding="0px" cellspacing="1px" border="0px" bgcolor="#ffffff">
                                                <tr>
                                                        <td width="100%" height="30px" bgcolor="#7B9EC0" class="titulo">&nbsp;<%=descriptor.getDescripcion("tit_consTemas")%></td>
                                                </tr>
                                                <tr>
                                                        <td width="100%" height="1px" bgcolor="#666666"></td>
                                                </tr>
                                                <tr>
                                                        <td width="100%" bgcolor="#e6e6e6" align="center" valign="middle">
                                                                <table width="100%" cellspacing="0px" cellpadding="0px" border="0px">
                                                                        <tr>
                                                                                <td width="10%"></td>
                                                                                <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("etiqNomTema")%>:</td>
                                                                                <td width="70%" align="left"><input type="Text" class="inputTextoObligatorio" name="nombreTema" size="50" maxlength="255" onkeypress="javascript:PasaAMayusculas(event);"></td>
                                                                        </tr>
                                                                        <tr>
                                                                                <td width="100%" colspan="3" height="10px"></td>
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
<div class="botoneraPrincipal">
    <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbBuscar")%> name="cmdBuscar" onclick="pulsarBuscar();" accesskey="B">
    <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdCancelar" onclick="pulsarCancelar();" accesskey="C">
</div>

</html:form>

<script language="JavaScript1.2">

document.onmousedown = checkKeys;

function checkKeysLocal(tecla) {
  keyDel();
  
  if (event.keyCode == 13)
  	pulsarBuscar();
}

</script>


</BODY>

</html:html>
