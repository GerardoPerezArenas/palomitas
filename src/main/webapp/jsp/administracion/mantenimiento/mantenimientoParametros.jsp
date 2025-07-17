<!-- JSP de mantenimiento de parametros -->

<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.technical.ConstantesDatos" %>

<html:html>
    <head>
        <title>Mantenimiento Parametros</title>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />

    <%
        int idioma = 1;
        int apl = 1;
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

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
                 type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
    <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />


    <!-- *********************	FICHEROS JAVASCRIPT **************************    -->

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >
<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
<script type="text/javascript">

    function Inicio() {
        window.focus();
        pleaseWait('off');
        var opcion = '<%= request.getParameter("opcion") %>';
        if (opcion == 'grabarOK') {
            jsp_alerta("A", '<%=descriptor.getDescripcion("msjGrabCorrect")%>');
        } else if (opcion == 'fallo') {
            jsp_alerta("A", '<%=descriptor.getDescripcion("msjProbTecnico")%>');
        }
    }

    function pulsarGrabar() {
        document.forms[0].opcion.value = 'grabar';
        document.forms[0].target = 'mainFrame';
        document.forms[0].submit();
    }

    function pulsarSalir(){
        document.forms[0].opcion.value = 'salir';
        document.forms[0].target = 'mainFrame';
        document.forms[0].submit();
    }


</script>

    </head>

    <body class="bandaBody" onload="javascript:{ pleaseWait('off');
       }" >

        <!-- Mensaje de esperar mientras carga  -->
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

        <!-- Fin mensaje de esperar mientras carga  -->

<html:form action="/administracion/mantenimiento/Parametros.do" method="post">

<input type="hidden" name="opcion"/>
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titParams")%></div>
    <div class="contenidoPantalla">
        <table style="width:100%;">
            <tr>
                <td width="25%" class="etiqueta" align="left"><%=descriptor.getDescripcion("etiqObsRechazar")%>:</td>
                <td>
                    <html:select styleClass="inputTexto" property="obsObligatoriasRechazar"> 
                        <html:option value="<%= ConstantesDatos.PARAMS_OBS_RECHAZAR_NO %>"><%=descriptor.getDescripcion("etiqObsNoOblig")%></html:option>
                        <html:option value="<%= ConstantesDatos.PARAMS_OBS_RECHAZAR_SI %>"><%=descriptor.getDescripcion("etiqObsOblig")%></html:option>
                    </html:select>
                </td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAceptar")%> name="cmdGrabar" onClick="pulsarGrabar();" alt="<%= descriptor.getDescripcion("toolTip_bGrabarParm")%>" title="<%= descriptor.getDescripcion("toolTip_bGrabarParm")%>">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir" onClick="pulsarSalir();" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>'>
        </div>
    </div>
</html:form>
<script> Inicio(); </script>
</body>
</html:html>

