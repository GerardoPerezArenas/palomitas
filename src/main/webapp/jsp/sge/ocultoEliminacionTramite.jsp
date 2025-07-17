<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title> Eliminacion Tramite cuando no hay mas tramites </title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

    <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma=1;
      int apl=1;

            if (session.getAttribute("usuario") != null){
                    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
                    idioma = usuarioVO.getIdioma();
                    apl = usuarioVO.getAppCod();
            }
      String enviar = "";
      if(session.getAttribute("enviar")!=null) {
        enviar = (String) session.getAttribute("enviar");
        session.removeValue("enviar");
      }

    %>

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>

    <script>
    function redirecciona() {
      var datos = new Array();
      var cod_departamento = new Array();
      var desc_departamento = new Array();
      var cod_estado = new Array();
      var desc_estado = new Array();

      datos[1] = '<bean:write name="DefinicionTramitesForm" property="tramiteEliminado"/>';
      datos[2] = '<bean:write name="DefinicionTramitesForm" property="codMunicipio"/>';
      datos[3] = '<bean:write name="DefinicionTramitesForm" property="txtCodigo"/>';
      datos[4] = '<bean:write name="DefinicionTramitesForm" property="txtDescripcion"/>';
      datos[5] = '<%= enviar %>';
      datos[6] = '<bean:write name="DefinicionTramitesForm" property="tramiteActual"/>';
      datos[7] ='<bean:write name="DefinicionTramitesForm" property="numeroTramites"/>';

      parent.mainFrame.tramiteEliminado(datos);
    }
    </script>
</head>
<body onLoad="redirecciona();">
    <form>
    <input type="hidden" name="opcion" value="">
    </form>
    <p>&nbsp;<p><center>
</body>
</html>
