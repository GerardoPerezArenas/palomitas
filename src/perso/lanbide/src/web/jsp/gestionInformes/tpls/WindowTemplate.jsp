<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@ page import="es.altia.common.service.config.Config" %>
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
    

    String userAgent = request.getHeader("user-agent");
%>
<html:html locale="true" >
    <fmt:setLocale value='${sessionScope["org.apache.struts.action.LOCALE"]}' scope="session"/>
    <jsp:useBean id="descriptor" scope="page" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>

    <head>
  
        <tiles:useAttribute name="title"/>
        <title><fmt:message key="${title}"/></title>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <jsp:include page="/jsp/gestionInformes/tpls/app-constants.jsp" />
        <jsp:include page="/jsp/gestionInformes/tpls/Metas.jsp" />
        <script type="text/javascript" language="JavaScript" src="<%=dominio%>/scripts/general.js"></script>
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >

  <link rel="stylesheet" type="text/css" href="<%=dominio%>/<%=css%>" media="screen">

 <style type="text/css">
     .capaFooter{
    width: 100%;
    background-color: #ffffff;
    position: absolute;
    bottom: 10;
    z-index: 1;
    height: 60px;
}

.botoneraPrincipal {
    margin-left: 12px;
    text-align: left;
    position: inherit;
    padding-left: 15px !important;
    bottom: -10px;
}

.contenidoPantalla {
    width: 100%;
    height: calc(100% - 515px);
    height: 85% \9;
    -moz-box-sizing: border-box;
    box-sizing: border-box; 
    background-color: #ffffff;
    overflow-x: hidden;
    overflow-y: auto;
    position: absolute;
    padding-right: 10px;
    padding-left: 15px !important;
    padding-top: 30px !important;
    z-index: 0;
}
</style>
        <script type='text/javascript'>
            function lanzarProceso(proceso){
                if(proceso!=""){
                  //proceso = "<%//=request.getContextPath()%>" + proceso;
                  if (proceso == "<c:url value='/gestionInformes/GestionInformes.do?opcion=inicio'/>" ||
                      proceso=="<c:url value='/gestionInformes/SolicitudesInformes.do?opcion=generacion'/>" ||
                      proceso=="<c:url value='/gestionInformes/SolicitudesInformes.do?opcion=inicio'/>"||
                      proceso=="<c:url value='/gestionInformes/MantenimientoInformes.do?opcion=administracion'/>"||
                      proceso=="<c:url value='/gestionInformes/MantenimientoInformes.do?opcion=administracionModo'/>"||
                      proceso=="<c:url value='/gestionInformes/MantenimientoInformes.do?opcion=administracionCampos'/>"){
                      document.location.href=proceso;
                  } else if (proceso == "<c:url value='/sge/DefinicionProcedimientos.do?opcion=catalogoProcedimientos'/>") {
                        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+proceso,null,
                                'width=760,height=510,status='+ '<%=statusBar%>',function(){});
                  } else {
                        modificando = "N";
                        document.forms[1].target = "_top";
                        document.forms[1].action = "<c:url value='/jsp/escritorio/container.jsp?opcion='/>"+proceso;
                        document.forms[1].submit();
                  }
                }
            }
            VerCorrect=-1;
        </script>
 </head>
  
 <body class="nuevo" onLoad="inicializar();">
    <jsp:include page="/jsp/hidepage.jsp" flush="true">
        <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
    </jsp:include>
    <div id="lyrBodyContent" class="contedorGlobal">
        <div name="menu" class="iframeMenu">
            <jsp:include page="/jsp/menu.jsp" flush="true"/>
        </div>
        <div name="mainFrame" class="iframeMainFrame">
                <tiles:useAttribute name="altiaAppFormTitle"/>
                <div class="txttitblanco">
                    <span>
                        <fmt:message key="${altiaAppFormTitle}"/>
                    </span>
                </div>
                <div class="contenidoPantalla">
                    <tiles:insert attribute="altiaAppFormContent"/>
                    <!-- BOTONES P�GINA -->
                   
                    <div class="capaFooter" >
                    <div class="botoneraPrincipal" >
                        <tiles:insert attribute="altiaAppFormBotons"/>
                    </div>
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
