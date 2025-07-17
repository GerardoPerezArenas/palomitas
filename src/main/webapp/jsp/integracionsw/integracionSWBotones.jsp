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

<%--<input type= "button" class="botonAplicacion" value="<%=descriptor.getDescripcion("gbImportar")%>"--%>
       <%--name="botonNuevaVersion" onClick="pulsarImportar;" accesskey="I">--%>
<%--<input type= "button" class="botonAplicacion" value="<%=descriptor.getDescripcion("gbDuplicar")%>"--%>
       <%--name="botonNuevaVersion" onClick="dlgNuevo.show();" accesskey="U">--%>
       
<!--        
<input type= "button" class="botonAplicacion" value="<%=descriptor.getDescripcion("gbModificar")%>"
       name="botonModificar" onClick="pulsarModificar();" accesskey="M">
<input type= "button" class="botonAplicacion" value="<%=descriptor.getDescripcion("gbPublicar")%>"
       name="botonBorrar" onClick="pulsarPublicar();" accesskey="P">
<input type= "button" class="botonAplicacion" value="<%=descriptor.getDescripcion("gbDespublicar")%>"
       name="botonLimpiar" onClick="pulsarDespublicar();" accesskey="D">
<input type= "button" class="botonAplicacion" value="<%=descriptor.getDescripcion("gbAlta")%>"
       name="botonAlta" onClick="pulsarAlta();" accesskey="N">
 -->
<input type= "button" class="botonAplicacion" value="Modificar"
       name="botonModificar" onClick="pulsarModificar();" accesskey="M">
<input type= "button" class="botonAplicacion" value="Publicar"
       name="botonBorrar" onClick="pulsarPublicar();" accesskey="P">
<input type= "button" class="botonAplicacion" value="Despublicar"
       name="botonLimpiar" onClick="pulsarDespublicar();" accesskey="D">
<input type= "button" class="botonAplicacion" value="Nuevo"
       name="botonAlta" onClick="pulsarAlta();" accesskey="N">