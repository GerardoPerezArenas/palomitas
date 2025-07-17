<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%
        String css = null;
         int apl = 11;
         int idioma = 1;
        if (session!=null){
                UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
                if (usuario!=null) {
                        css = usuario.getCss();
                        idioma = usuario.getIdioma();

                         
                }
        }	
%>

<script type="text/javascript">
    function inicializar(){}
    //Para evitar petes si los content no definen esta función
</script>

<html:html locale="true">
    <fmt:setLocale value='${sessionScope["org.apache.struts.action.LOCALE"]}' scope="session"/>
    <jsp:useBean id="descriptor" scope="page" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
     <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <head>
        <jsp:include page="/jsp/portafirmas/tpls/app-constants.jsp" />
        <title><tiles:insert attribute="title"/></title>
        <%@ include file="/jsp/portafirmas/tpls/Metas.jsp" %>
        <meta http-equiv="X-UA-Compatible" content="IE=10"/>

        <script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-1.9.1.min.js'/>?v=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript" language="JavaScript" src="<c:url value='/scripts/general.js'/>?v=<c:out value="${applicationScope.appVersion}"/>"></script>

        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen">        	    
        <!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx -->
        <!-- BEGIN HEAD-CONTENT                               -->
        <!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx -->
    <tiles:insert attribute="head-content"/>
    <!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx -->
    <!-- END HEAD-CONTENT                                 -->
    <!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx -->
</head>

<body class="nuevo"  onLoad="inicializar();">
    <jsp:include page="/jsp/hidepage.jsp" flush="true">
        <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
    </jsp:include>
    <%-- Body Content layer --%>
    <div id="lyrBodyContent" >
        <!-- XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX -->
        <!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx -->
        <!-- BEGIN CONTENT                                    -->
        <!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx -->
        <tiles:insert attribute="content"/>
        <!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx -->
        <!-- END CONTENT                                      -->
        <!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx -->
        <!-- XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX -->
    </div>
    <!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx -->
    <!-- BEGIN FINAL JAVASCRIPT                           -->
    <!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx -->
<tiles:insert attribute="finalJavascript"/>
<!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx -->
<!-- END FINAL JAVASCRIPT                             -->
<!-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx -->
</body>
</html:html>
