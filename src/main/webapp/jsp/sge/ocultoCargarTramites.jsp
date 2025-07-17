<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %> 

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>


<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.agora.business.sge.RelacionExpedientesValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.RelacionExpedientesForm"%>
<%@ page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title> Cargar listas de tramites en el combo </title>
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
        RelacionExpedientesForm relExpForm = (RelacionExpedientesForm) session.getAttribute("RelacionExpedientesForm");%>

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>

    <script type="text/javascript">

    function redirecciona() {
        var cod_tramite = new Array();
        var desc_tramite = new Array();
        var cod_oculto_tramite = new Array();
        cont3=0;
        <logic:iterate id="elemento" name="RelacionExpedientesForm" property="listaTramites">
          cod_oculto_tramite[cont3] = '<bean:write name="elemento" property="codigoTramite"/>';
          cod_tramite[cont3] = '<bean:write name="elemento" property="numeroTramite"/>';
          desc_tramite[cont3] = '<bean:write name="elemento" property="descClasificacionTramite"/>'+' - '+'<bean:write name="elemento" property="nombreTramite"/>';
          cont3++;
        </logic:iterate>
        parent.mainFrame.comboTramite.addItems(cod_tramite,desc_tramite);
        parent.mainFrame.cod_tramite=cod_tramite;
        parent.mainFrame.desc_tramite=desc_tramite;
        parent.mainFrame.cod_oculto_tramite=cod_oculto_tramite;
    }
    </script>
</head>
<body onLoad="redirecciona();">

</body>
</html>