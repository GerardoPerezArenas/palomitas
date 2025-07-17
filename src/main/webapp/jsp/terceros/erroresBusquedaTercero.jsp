<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>

<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
    <jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title><fmt:message key="BusquedaTercero/tituloDialogo"/></title>

    

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

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
                 type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>"/>
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>"/>

    <script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
    
    <script language="JavaScript">
        function inicializar() {
            var argVentana = self.parent.opener.xanelaAuxiliarArgs;
            domlay('capaEscritura',1,0,0,escribir(argVentana));
        }

        function escribir(lista) {
            var htmlString = '<ul>';
            for (var i = 0; i < lista.length; i++) {
                htmlString += '<li>' + lista[i] + '</li>\n';
            }
            htmlString += '</ul>'
            return htmlString;
        }

      window.focus();
    </script>
</head>

<body onload="inicializar();" >	
<div class="contenidoPantalla">
<div style="margin-left:7%;margin-right:3%;text-align:center;padding-top:5px;padding-bottom:5px"
     class="txttitcabecera" width="90%">
    <%=descriptor.getDescripcion("etiqInfoBusqueda")%>
</div>

<div id="capaEscritura" class="etiqueta" style="margin-top:3%;padding-left:5%;padding-right:5%">
</div>

<div class="capaBotonesBusqueda" style="text-align:center;margin-bottom:5px;margin-top:5px;">	
    <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAceptar")%>"  accesskey="A" onclick="self.parent.opener.retornoXanelaAuxiliar();"/>
</div>
</div>
</body>
</html>