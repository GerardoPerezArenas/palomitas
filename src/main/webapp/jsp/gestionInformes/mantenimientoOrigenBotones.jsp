<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%
    int idioma = 1;
    int apl = 1;
    if (session != null) {
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        if (usuario != null) {
            idioma = usuario.getIdioma();
            apl = usuario.getAppCod();
        }
    }
%>


<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<input type= "button" class="botonAplicacion" value="Campos"
       name="botonNuevo" onClick="pulsarAdminCampos();" accesskey="C">
<input type= "button" class="botonAplicacion" value="<%=descriptor.getDescripcion("gbAlta")%>"
       name="botonNuevo" onClick="pulsarNuevo();" accesskey="N">
<input type= "button" class="botonAplicacion" value="<%=descriptor.getDescripcion("gbModificar")%>"
       name="botonModificar" onClick="modifyParamOut();" accesskey="M">
<input type= "button" class="botonAplicacion" value="<%=descriptor.getDescripcion("gbEliminar")%>"
       name="botonEliminar" onClick="pulsarEliminar();" accesskey="E">
<input type= "button" class="botonAplicacion" value="<%=descriptor.getDescripcion("gbLimpiar")%>"
       name="botonLimpiar" onClick="pulsarLimpiar();" accesskey="L">
                    