<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%
    int idioma = 1;
    int apl = 1;
    int municipio = -1;
    boolean importando = (request.getAttribute("importando")!=null && request.getAttribute("importando").equals("si"));
    if (session != null) {
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        if (usuario != null) {
            idioma = usuario.getIdioma();
            apl = usuario.getAppCod();
            municipio = usuario.getOrgCod();
        }
    }
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<% if (municipio != 0 && !importando) { %>
<input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbImportar")%>"
    name="botonImportar" onClick="pulsarImportar();" accesskey="I">
<% } else if (municipio != 0 && importando) { %>
<input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbDescargar")%>"
    name="botonDescargar" onClick="pulsarDescargar();" accesskey="D">
<input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbVerificar")%>"
    name="botonVerificar" onClick="pulsarVerificar();" accesskey="V">
<input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>"
    name="botonCancelar" onClick="pulsarCancelar();" accesskey="V">
<% } %>
<%--<input type= "button" class="botonAplicacion" value="<%=descriptor.getDescripcion("gbDuplicar")%>"--%>
       <%--name="botonNuevaVersion" onClick="dlgNuevo.show();" accesskey="U">--%>
<% if (!importando) { %>

<input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>"
       name="botonEliminar" onClick="pulsarEliminar();" accesskey="E">
<input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbModificar")%>"
       name="botonModificar" onClick="pulsarModificar();" accesskey="M">
<input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbPublicar")%>"
       name="botonBorrar" onClick="pulsarPublicar();" accesskey="P">
<input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbDespublicar")%>"
       name="botonLimpiar" onClick="pulsarDespublicar();" accesskey="D">
<input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAlta")%>"
       name="botonAlta" onClick="pulsarAlta();" accesskey="N">  
<% } %>