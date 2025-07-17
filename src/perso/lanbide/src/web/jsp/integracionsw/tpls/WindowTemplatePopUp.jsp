<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%
    String size = request.getHeader("user-agent");
    String estiloSize = "";
    if(size.indexOf("MSIE")!=-1) {
        estiloSize = "";
    }else
        estiloSize = "height:170px;";

%>
<html:html locale="true" >
<fmt:setLocale value='${sessionScope["org.apache.struts.action.LOCALE"]}' scope="session"/>
<jsp:useBean id="descriptor" scope="page" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<head>
    <jsp:include page="/jsp/gestionInformes/tpls/app-constants.jsp" />
    <jsp:include page="/jsp/gestionInformes/tpls/Metas.jsp" />

    <script type="text/javascript" language="JavaScript" src="<c:url value='/scripts/general.js'/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/sge_basica_popUp.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
 </head>
 <body class="nuevo" onLoad="inicializar();">
    <jsp:include page="/jsp/hidepage.jsp" flush="true">
        <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
    </jsp:include>
    <div id="lyrBodyContent" >

        <%--<div style="text-align: center">--%>
            <!-- CUERPO PÁGINA -->
            <div style="height:100%;width:100%">
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

    <script type="text/javascript" language="JavaScript">
        pleaseWait1("off",this);
        function inicializar()
        {
        }
    </script>

 </body>
</html:html>
