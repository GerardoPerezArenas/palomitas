<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>
<%@ page import="es.altia.agora.interfaces.user.web.informes.InformesForm" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.agora.business.util.GeneralValueObject" %>
<%@ page import="java.util.Vector" %>

<%
    UsuarioValueObject usuarioVO    = null;
    int                idioma       = 1;
    Vector             estadisticas = new Vector();
    String             tipoSalida   = "";
    String             usu          = "";
    String             entidad      = "";
    boolean            esHtml       = true;

    if (session != null) {
      usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
      idioma = usuarioVO.getIdioma();

      InformesForm iForm = (InformesForm)session.getAttribute("InformesForm");

      estadisticas = iForm.getEstadisticas();
      tipoSalida = iForm.getTipoSalida();
      usu = usuarioVO.getNombreUsu();
      entidad = usuarioVO.getEnt();
    }
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor" property="idi_cod" value="<%= usuarioVO.getIdioma() %>" />
<jsp:setProperty name="descriptor" property="apl_cod" value="<%= usuarioVO.getAppCod() %>" />


<html>
  <head>
    <title>INFORME</title>

    <jsp:include page="/jsp/informes/tpls/app-constants.jsp" />
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <link href="<c:url value='/css/estilos_informe.css'/>" rel="stylesheet" type="text/css">
  </head>
  <body bgcolor="#e6e6e6">
    <table width="800px" border=0 cellspacing=0 cellpading=0>
      <tr>
        <td width="100%" height="100%" valign="top">
          <DIV id="divTabla" name="divTabla" style="width:800px;height:600px;overflow:auto;visibility:visible">
            <table width="800px" border="0px" rules=all cellspacing=0 cellpading=0 align="center">
              <tr>
                <td height="50px" width="80%" class="titulo1" colspan="2"><img align="middle" src=<%= request.getContextPath() + usuarioVO.getOrgIco() %> height="52"><%= entidad %>
                </td>
                <td height="50px" width="20%" align="left" class="titulo1">
                  &nbsp;&nbsp;
                  <script>
                    document.write(fechaHoy());
                  </script>
                </td>
              </tr>
              <tr>
                <td height="40px" width="100%" valign="top" colspan="3">
                  <hr size=2 color="#666699" style="solid">
                </td>
              </tr>
              <tr>
                <td width="20%"></td>
                <td class="encabezado" height="20px" width="60%" align="center">
                  <%= descriptor.getDescripcion("msjNoDatosCons") %>
                </td>
                <td width="20%"></td>
              </tr>
            </table>
          </div>
        </td>
      </tr>
    </table>
  </body>
</html>
