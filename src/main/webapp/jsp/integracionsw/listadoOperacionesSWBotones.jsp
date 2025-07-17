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
 
<input type= "button" class="botonAplicacion" value="<%=descriptor.getDescripcion("iswNueva")%>"
       name="botonNueva" onClick="pulsarAlta();" accesskey="N"> 
<input type= "button" class="botonAplicacion" value="<%=descriptor.getDescripcion("iswModificar")%>"
       name="botonModificar" onClick="pulsarModificar();" accesskey="M">
<input type= "button" class="botonAplicacion" value="<%=descriptor.getDescripcion("iswPublicar")%>"
       name="botonBorrar" onClick="pulsarPublicar();" accesskey="P">
<input type= "button" class="botonAplicacion" value="<%=descriptor.getDescripcion("iswDespublicar")%>"
       name="botonLimpiar" onClick="pulsarDespublicar();" accesskey="D">
<input type= "button" class="botonAplicacion" value="<%=descriptor.getDescripcion("iswTest")%>"
       name="botonTest" onClick="pulsarTest();" accesskey="T">
<input type= "button" class="botonAplicacion" value="<%=descriptor.getDescripcion("iswVolver")%>"
       name="botonVolver" onClick="pulsarVolver();" accesskey="V">       
       