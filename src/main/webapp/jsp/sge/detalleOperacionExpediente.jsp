<!-- JSP de detalles de un movimiento de una anotaci�n -->
<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html:html>
    <head>
        <title>Detalle de una operaci�n de un expediente</title>
         <meta http-equiv="X-UA-Compatible" content="IE=EDGE"/>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
         <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <%
        // Tomamos codigos de aplicaci�n y de idioma del usuario
        int idioma = 1;
        int apl = 4;
        String css = "";
        if (session != null && session.getAttribute("usuario") != null) {
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            if (usuario != null) {
                idioma = usuario.getIdioma();
                apl = usuario.getAppCod();
                css = usuario.getCss();
            }
        }
    %>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
    <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

    <script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
    <script type="text/javascript">

        function pulsarSalir(){
            self.parent.parent.opener.retornoXanelaAuxiliar();
        }

    </script>
    </head>

    <body class="bandaBody" onload="javascript:{ pleaseWait('off');}" >
        <!-- Mensaje de esperar mientras carga  -->
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        <!-- Fin mensaje de esperar mientras carga  -->
        
        <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("tit_detMovExp")%></div>
        <div class="contenidoPantalla scrollable-x scrollable-y">
            <div name="detalles" class="movExpArea"><bean:write name="detalleOperacionExpediente" filter="false"/></div>
            <!-------------------------------------- BOTONES. ------------------------------------------>
            <div class="row">
                <div class="botoneraPrincipal col-sm-12">
                <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir" onClick="pulsarSalir();" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>'>
                </div>
            </div>
        </div>
    </body>
    </html:html>
