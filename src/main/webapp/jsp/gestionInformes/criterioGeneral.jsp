<%--
  Created by IntelliJ IDEA.
  User: susana.rodriguez
  Date: 10-may-2007
  Time: 11:26:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%
    response.setHeader("Cache-control","no-cache");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader ("Expires", 0);

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
<jsp:setProperty name="descriptor" property="apl_cod" value="<%= apl %>"/>

<%  String campo = request.getParameter("campo");
    String titulo = request.getParameter("titulo");
    String tabla = request.getParameter("tabla");
    String formulario = request.getParameter("formulario");
    String codigoCampoDesplegable = request.getParameter("codigoCampoDesplegable");
    String tipo = request.getParameter("tipo");
    String etiquetaEsNulo   = descriptor.getDescripcion("etiqEsNulo");
    String etiquetaEsNoNulo = descriptor.getDescripcion("etiqEsNoNulo");

    if (tipo.equals("F")) {
%>

        <jsp:include page="criterioFecha.jsp" flush="true">
            <jsp:param name="campo" value="<%=campo%>"/>
            <jsp:param name="titulo" value="<%=titulo%>"/>
            <jsp:param name="etiquetaEsNulo" value="<%=etiquetaEsNulo%>"/>
            <jsp:param name="etiquetaEsNoNulo" value="<%=etiquetaEsNoNulo%>"/>
        </jsp:include>
<%
    } else if (tipo.equals("A")) {
%>
        <jsp:include page="criterioTexto.jsp" flush="true">
            <jsp:param name="campo" value="<%=campo%>"/>
            <jsp:param name="titulo" value="<%=titulo%>"/>
            <jsp:param name="etiquetaEsNulo" value="<%=etiquetaEsNulo%>"/>
            <jsp:param name="etiquetaEsNoNulo" value="<%=etiquetaEsNoNulo%>"/>
        </jsp:include>

<%  } else if (tipo.equals("N")) {
%>
        <jsp:include page="criterioNumerico.jsp" flush="true">
            <jsp:param name="campo" value="<%=campo%>"/>
            <jsp:param name="titulo" value="<%=titulo%>"/>
            <jsp:param name="etiquetaEsNulo" value="<%=etiquetaEsNulo%>"/>
            <jsp:param name="etiquetaEsNoNulo" value="<%=etiquetaEsNoNulo%>"/>
        </jsp:include>

<%  }  else if (tipo.equals("D")) {
%>
        <jsp:include page="criterioDesplegable.jsp" flush="true">
            <jsp:param name="campo" value="<%=campo%>"/>
            <jsp:param name="titulo" value="<%=titulo%>"/>
            <jsp:param name="formulario" value="<%=formulario%>"/>
            <jsp:param name="codigoCampoDesplegable" value="<%=codigoCampoDesplegable%>"/>
            <jsp:param name="etiquetaEsNulo" value="<%=etiquetaEsNulo%>"/>
            <jsp:param name="etiquetaEsNoNulo" value="<%=etiquetaEsNoNulo%>"/>
        </jsp:include>
<%  }  else if (tipo.equals("I")) {
%>
        <jsp:include page="criterioInteresado.jsp" flush="true">
            <jsp:param name="campo" value="<%=campo%>"/>
            <jsp:param name="titulo" value="<%=titulo%>"/>
            <jsp:param name="formulario" value="<%=formulario%>"/>
            <jsp:param name="codigoCampoDesplegable" value="<%=codigoCampoDesplegable%>"/>
            <jsp:param name="etiquetaEsNulo" value="<%=etiquetaEsNulo%>"/>
            <jsp:param name="etiquetaEsNoNulo" value="<%=etiquetaEsNoNulo%>"/>
        </jsp:include>
<%  } %>