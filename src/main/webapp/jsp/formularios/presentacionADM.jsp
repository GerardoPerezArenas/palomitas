<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.util.CargaMenu" %>
<style type="text/css">

 html>body .logoCentro{
    width: 100%;
    height: 50%;
    padding-left: 440px;

}
</style>

<%
        int aplicacionCod = 0;
        String aplicacion = "";
        String usu = "";
        String entidad = "";
        String css = "";
        int idioma = 0;
        int codAplicacion = 0;
        UsuarioValueObject usuarioVO = null;

        if (session != null) {
            usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
        }

        if (usuarioVO != null) {
            aplicacion = usuarioVO.getApp().toUpperCase();
            codAplicacion = usuarioVO.getAppCod();
            usu = usuarioVO.getNombreUsu();
            entidad = usuarioVO.getEnt();
            idioma = usuarioVO.getIdioma();
            css = usuarioVO.getCss();
        }

        String nombreFichero = request.getContextPath() + usuarioVO.getOrgIco().substring(0, usuarioVO.getOrgIco().length() - 4);
        String extension = usuarioVO.getOrgIco().substring(usuarioVO.getOrgIco().length() - 4);
        String rutaFichero = nombreFichero + "_grande" + extension;
%>
<html>
    <head>
        <jsp:include page="/jsp/formularios/tpls/app-constants.jsp" />
        <title>GESTIÓN AYUNTAMIENTOS - Páxina de inicio</title>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%= usuarioVO.getIdioma()%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= usuarioVO.getAppCod()%>" />
        <LINK REL="stylesheet" MEDIA="screen" TYPE="text/css" HREF="<%=request.getContextPath()%><%=css%>">
    </head>
    <!-- onLoad para que los submenus se vean en este frame -->
    <body class="bandaBody" onload="javascript:{ }">
        <div id="titulo" class="txttitblanco">
            <%= descriptor.getDescripcion("etiq_Formularios")%>
        </div>
        <div class="contenidoPantalla">
            <table>
                <tr>
                    <td id="tabla" class="logoCentro" style="width: 100%; height: 50%" align="center" valign="bottom">
                        <img height="100px" src='<%=request.getContextPath() + usuarioVO.getOrgIco().substring(0, usuarioVO.getOrgIco().length() - 4)%>_grande.gif' border="0">
                    </td>
                </tr>
                <tr>
                    <td style="width: 100%; height: 50%" align="center" valign="top">
                        <% if (codAplicacion == 9) {%>
                        <span class="txttitcabecera"><%=entidad%></span>
                        <% }%>
                    </td>
                </tr>
            </table>
        </div>
    </body>
</html>
