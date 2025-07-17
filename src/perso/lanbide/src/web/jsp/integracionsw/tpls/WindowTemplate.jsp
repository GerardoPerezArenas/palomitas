<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.Integer"%>
<%@page import="es.altia.agora.business.util.LabelValueTO"%>

<%
	UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
	String css = null;
        int idioma  =0;
	if(usuario!=null){
		css = usuario.getCss();
        idioma = usuario.getIdioma();
    }

        Config m_Config = ConfigServiceHelper.getConfig("common");
        String statusBar = m_Config.getString("JSP.StatusBar");

    String dominio = m_Config.getString("hostVirtual");
    if (dominio == null || dominio.length() == 0) dominio = request.getContextPath();
    else dominio=dominio+request.getContextPath();


        String icono = usuario.getOrgIco();
           ArrayList idiomas = (ArrayList)session.getAttribute("listaIdiomas");
%>
<html:html locale="true" >
    <fmt:setLocale value='${sessionScope["org.apache.struts.action.LOCALE"]}' scope="session"/>

    <head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <tiles:useAttribute name="title"/>
        <title><fmt:message key="${title}"/></title>
        <script type="text/javascript">
        </script>

        <jsp:useBean id="descriptor" scope="page" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
        <jsp:include page="/jsp/integracionsw/tpls/app-constants.jsp" />
        <jsp:include page="/jsp/integracionsw/tpls/Metas.jsp" />
        <script type="text/javascript" language="JavaScript" src="<%=dominio%>/scripts/general.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=dominio%>/css/sge_basica.css">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<%=dominio%>/<%=css%>" media="screen">
 </head>
  
 <body class="nuevo" onLoad="inicializar();">
    <jsp:include page="/jsp/hidepage.jsp" flush="true">
        <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
    </jsp:include>
    <div id="lyrBodyContent" style="height:100%;width:100%">
        <div name="menu" class="iframeMenu">
            <jsp:include page="/jsp/menu.jsp" flush="true"/>
        </div>
        <div name="mainFrame" class="iframeMainFrame">
            <tiles:useAttribute name="altiaAppFormTitle"/>
            <div class="txttitblanco"><fmt:message key="${altiaAppFormTitle}"/></div>
            <div class="contenidoPantalla">
                <tiles:insert attribute="altiaAppFormContent"/>
                <!-- BOTONES PÁGINA -->
                <div class="botoneraPrincipal">
                    <tiles:insert attribute="altiaAppFormBotons"/>
                </div>
            </div>
        </div>
    </div>

    <script type="text/javascript">
        pleaseWait1("off",this);
            function inicializar(){
        }
    </script>

 </body>
</html:html>
