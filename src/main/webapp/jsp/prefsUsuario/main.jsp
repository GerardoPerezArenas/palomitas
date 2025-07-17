<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page contentType="text/html; charset=iso-8859-1" language="java" import="es.altia.agora.business.escritorio.UserPreferences"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioEscritorioValueObject" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<% String offsetH = request.getParameter("offsetH");
    String offsetV = request.getParameter("offsetV");
    if (offsetH==null) offsetH="30";
    if (offsetV==null) offsetV="30";
    int idioma = 1;
    UsuarioEscritorioValueObject usuarioEscritorioVO = null;
    UserPreferences userPreferences = null;
    UsuarioValueObject usuarioVO = null;
    String aplicacion = "";
    if (session!=null){
        usuarioEscritorioVO = (UsuarioEscritorioValueObject)session.getAttribute("usuarioEscritorio");
        idioma =  usuarioEscritorioVO.getIdiomaEsc();
        userPreferences = (UserPreferences) usuarioEscritorioVO.getPreferences();

        usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuarioVO != null){
          aplicacion = usuarioVO.getApp().toUpperCase();
        }
    }
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<SCRIPT type ="text/javascript">
var APP_CONTEXT_PATH="<c:out value='${pageContext.request.contextPath}'/>";
function cambiarClave() {
  var datosAEnviar = new Array();
  var source = "<c:url value='/jsp/escritorio/cambiarClave.jsp?opcion=null'/>";
  abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/escritorio/mainVentana.jsp?source='+source,datosAEnviar,
	'width=400,height=300,status='+ '<%=statusBar%>',function(datosConsulta){});
}

function CambioIdioma(valor){
	var resultado = jsp_alerta("",'<%= descriptor.getDescripcion("g_confirmIdioma")%>');
	if (resultado == 1){
        document.forms[0].option.value = 'changeIdiomaEsc';
        document.forms[0].value.value = valor;
      	document.forms[0].action = "<c:url value='/UserPref.do'/>";
      	document.forms[0].submit();
   	}
}function inicio() {
      var resultado = jsp_alerta("",'<%= descriptor.getDescripcion("g_confirmSalirApp")%>');
      if (resultado == 1){
        document.forms[0].target = '_top';
        document.forms[0].action = "<c:url value='/SalirApp.do?app='/><%= usuarioVO.getAppCod()%>";
        document.forms[0].submit();
      }
}
</SCRIPT>
<html:html locale="true">
 <fmt:setLocale value='${sessionScope["org.apache.struts.action.LOCALE"]}' scope="session"/>
 <head>
    <title></title>
    <%@ include file="/jsp/plantillas/Metas.jsp" %>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>" media="screen">
 </head>
 <BODY class="noappbody">
<html:form action="/MantAnotacionRegistro.do" target="_self">
<input type="hidden" name="value">
<input type="hidden" name="option">
    <table width="100%" cellpadding="0px" cellspacing="0px">
        <tr><td align="right" valign="middle"><img src="<c:url value='/images/logo_tr.gif'/>" border="0px"></td></tr>
    </table>
    <div id='main' style='visibility:visible;position:absolute;top:25;left:25'>
        <!-- Cabecera. -->
        <table width="450px" height="60px" cellpadding="0px" cellspacing="0px">
            <tr>
                <td width="449px" height="60px">
                    <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                        <tr>
                            <td>
                                <table width="100%" height="100%" cellpadding="0px" cellspacing="0px" class="tablaGris">
                                    <tr>
                                        <td class="txttitcabecera" valign="bottom">&nbsp;<%=descriptor.getDescripcion("etiq_PreUsu")%></td>
                                        <td width="22px" align="center" valign="top">
                                            <a href="javascript:inicio();">
                                                <span class="fa fa-sign-out" aria-hidden="true" title="<%= descriptor.getDescripcion("ico_SalirApp")%>"></span>
                                            </a>                                        
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2" height="5px"></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
                <!-- Sombra lateral. -->
                <td width="1px" height="60px">
                    <table width="100%" height="60px" cellpadding="0px" cellspacing="0px">
                        <tr>
                            <td height="1px"></td>
                        </tr>
                        <tr>
                            <td height="59px" bgcolor="#393939"></td>
                        </tr>
                    </table>
                </td>
                <!-- Fin sombra lateral. -->
            </tr>
            <!-- Sombra inferior. -->
            <tr>
                <td colspan="2" height="1px">
                    <table cellpadding="0px" cellspacing="0px">
                        <tr>
                            <td width="1px" height="1px"></td>
                            <td width="449px" height="1px" bgcolor="#393939"></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <!-- Fin sombra inferior. -->
            <!-- Separador. -->
            <tr>
                <td colspan="2" height="2px"></td>
            </tr>
            <!-- Fin separador. -->
        </table>
        <!-- Fin cabecera. -->
        <!-- Preferencias de usuario -->
        <table width="450px"  height="180px" cellpadding="0px" cellspacing="0px">
            <tr>
                <td width="449px" height="180px">
                    <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                        <tr>
                            <td>
                                <table width="100%" height="100%" cellpadding="0px" cellspacing="1px" border="0px" bgcolor="#ffffff">
                                    <tr>
                                        <td width="100%" height="30px" class="txttitsmallblanco">&nbsp;<%=descriptor.getDescripcion("etiq_PreUsu")%></td>
                                    </tr>
                                    <tr>
                                        <td width="100%" height="1px" bgcolor="#666666"></td>
                                    </tr>
                                    <tr>
                                        <td width="100%" height="18px">
                                            <table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
                                                <tr>
                                                    <td width="45%" height="18px" align="center" valign="middle" class="cabeceiraCol"></td>
                                                    <td width="55%" height="18px" align="center" valign="middle" class="cabeceiraCol"></td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="100%">
                                          <div id="tabla" style="width:100%;height:100%;overflow:auto;">
                                            <table width="100%" height="100%" cellpadding="0px" cellspacing="1px" border="0px" bgcolor="#cccccc">
                                                <tr>
                                                    <td bgcolor="#ffffff" class="fila" width="20%" height="23px" align="center">
                                                        &nbsp; <fmt:message key="Escritorio.userPreferences.Clave"/>
                                                    </td>
                                                    <td bgcolor="#ffffff" width="80%" height="23px" align="left" >
                                                        &nbsp;
                                                        <a class="enlace" href='javascript: cambiarClave();'><%=descriptor.getDescripcion("etiq_CambClave")%></a>
                                                    </td>
                                                </tr>
                                                    <% if (usuarioEscritorioVO.isMultipleIdioma()) { %>
                                                    <tr>
                                                      <% if (idioma == 1) { %>
                                                        <td bgcolor="#ffffff" class="fila" width="20%" height="23px" align="center">
                                                            &nbsp; <fmt:message key="Escritorio.userPreferences.Idioma"/>
                                                        </td>
                                                        <td bgcolor="#ffffff" width="80%" height="23px" align="left" >
                                                            &nbsp;
                                                            <a class="enlace" href='javascript: CambioIdioma("2");'><span class="enlace" title="Cambio de idioma a gallego">Gallego</span></a>
                                                        </td>
                                                     <% } else if(idioma == 2) { %>
                                                        <td bgcolor="#ffffff" class="fila" width="20%" height="23px" align="center">
                                                            &nbsp; <fmt:message key="Escritorio.userPreferences.Idioma"/>
                                                        </td>
                                                        <td bgcolor="#ffffff" width="80%" height="23px" align="left" >
                                                            &nbsp;
                                                            <a class="enlace" href='javascript: CambioIdioma("1");'><span class="enlace" title="Cambio de idioma a castelán">Castelán</span></a>
                                                        </td>
                                                      <% } %>
                                                     </tr>
                                                  <% } %>
                                            </table>
                                          </div>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
                <!-- Sombra lateral. -->
                <td width="1px" height="180px">
                    <table width="100%" height="180px" cellpadding="0px" cellspacing="0px">
                        <tr>
                            <td height="1px"></td>
                        </tr>
                        <tr>
                            <td height="179px" bgcolor="#393939"></td>
                        </tr>
                    </table>
                </td>
                <!-- Fin sombra lateral. -->
            </tr>
            <!-- Sombra inferior. -->
            <tr>
                <td colspan="2" height="1px">
                    <table cellpadding="0px" cellspacing="0px">
                        <tr>
                            <td width="1px" height="1px"></td>
                            <td width="449px" height="1px" bgcolor="#393939"></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <!-- Fin sombra inferior. -->
            <!-- Separador. -->
            <tr><td colspan="2" height="2px"></td></tr>
            <!-- Fin separador. -->
        </table>
        <!-- Fin preferencias usuario. -->
    </div>
</body>
</html:form>
</html:html>
