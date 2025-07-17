<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%
    response.setHeader("Cache-control","no-cache");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader ("Expires", 0);

    int idioma = 0;
    int apl = 1;
    if (session != null) {
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        if (usuario != null) {
            idioma = usuario.getIdioma();
            apl = usuario.getAppCod();
        }
    }
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<html:html locale="true" >
     <fmt:setLocale value='${sessionScope["org.apache.struts.action.LOCALE"]}' scope="session"/>
    <head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <tiles:useAttribute name="title"/>
        <title><%=descriptor.getDescripcion("ConvXPDLTitle")%></title>

        <jsp:include page="/jsp/xpdl/tpls/app-constants.jsp" />
	    <jsp:include page="/jsp/xpdl/tpls/Metas.jsp" />

        <html:base target="_self"/>
        <script type="text/javascript" language="JavaScript" src="<c:url value='/scripts/general.js'/>"></script>
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/sge_basica.css'/>" media="screen">
 </head>
 <body class="nuevo">
    <div id="lyrBodyContent" style="height:100%;width:100%">
            <!-- CABECERA PAGINA -->
        <div name="menu" class="iframeMenu">
            <jsp:include page="/jsp/menu.jsp" flush="true"/>
        </div>
        <div name="mainFrame" class="iframeMainFrame">
            <div class="txttitblanco" >
                <%=descriptor.getDescripcion("ConvXPDLFormTitle")%>
            </div>
            <div class="contenidoPantalla">
                <tiles:insert attribute="altiaAppFormContent"/>
                <!-- BOTONES PÁGINA -->
                <div class="botoneraPrincipal">
                    <tiles:insert attribute="altiaAppFormBotons"/>
                </div>
            </div>
        </div>
    </div>
 </body>
</html:html>
