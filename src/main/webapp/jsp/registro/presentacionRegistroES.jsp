<%@page contentType="text/html; charset=iso-8859-1" language="java"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>


<%
            int aplicacionCod = 0;
            String aplicacion = "";
            String usu = "";
            int idioma = 0;
            int codAplicacion = 0;
            UsuarioValueObject usuarioVO = null;
            String css = null;
            if (session != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
            }

            if (usuarioVO != null) {
                aplicacion = usuarioVO.getApp().toUpperCase();
                codAplicacion = usuarioVO.getAppCod();
                usu = usuarioVO.getNombreUsu();
                idioma = usuarioVO.getIdioma();
                css = usuarioVO.getCss();
            }

            String nombreFichero = request.getContextPath() + usuarioVO.getOrgIco().substring(0, usuarioVO.getOrgIco().length() - 4);
            String extension = usuarioVO.getOrgIco().substring(usuarioVO.getOrgIco().length() - 4);
            String rutaFichero = nombreFichero + "_grande" + extension;

%>




<html>
    <head>
        <title>PANTALLA DE PRESENTACIÓN DEL REGISTRO</title>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
        <jsp:setProperty name="descriptor" property="idi_cod" value="<%= usuarioVO.getIdioma()%>" />
        <jsp:setProperty name="descriptor" property="apl_cod" value="<%= usuarioVO.getAppCod()%>" />
        
        <LINK REL="stylesheet" MEDIA="screen" TYPE="text/css" HREF="<%=request.getContextPath()%><%=css%>">
    </head>
    <body>
        <div id="titulo" class="txttitblanco">
            <%= descriptor.getDescripcion("tituloReg") %>
        </div>
        <div class="contenidoPantalla" valign="middle">
            <table width="100%" cellpadding="0px" cellspacing="3px">
                <tr>
                    <td id="tabla" style="width: 100%; text-align:center">
                        <img height="100px" src='<%=rutaFichero%>' border="0">
                    </td>
                </tr>                                                
            </table>
        </div>
    </body>
</html>