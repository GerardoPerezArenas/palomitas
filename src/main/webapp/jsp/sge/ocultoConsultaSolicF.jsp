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
    <title> Oculto Consulta Expedientes Finalizados por solicitante </title>
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
    %>

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>

    <script>
      function redirecciona(){
        var consulta  = new Array();
        var cont = 0;
        <logic:iterate id="elemento" name="PendientesPorSolicitanteForm" property="consulta">
          consulta[cont]=['<bean:write name="elemento" property="entrada"/>',
                          '<bean:write name="elemento" property="codProcedimiento"/>' + "." +
                          '<bean:write name="elemento" property="ano"/>' + "." +
                          '<bean:write name="elemento" property="numero"/>',
                          '<bean:write name="elemento" property="codProcedimiento"/>',
                          '<bean:write name="elemento" property="fechaInicio"/>',
                          '<bean:write name="elemento" property="fechaFin"/>'];
          cont++;
        </logic:iterate>
        parent.mainFrame.recuperaConsulta(consulta);
      }
    </script>
</head>
<body onLoad="redirecciona();">
</body>
</html>
