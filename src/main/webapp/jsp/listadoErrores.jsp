<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>

<html>
<head>
    <jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title><fmt:message key="ListadoErrores/tituloDialogo"/></title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

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
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
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

    </script>
</head>

<body onload="inicializar();">


 <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("etiqInfoBusqueda")%></div>
<div class="contenidoPantalla">
    <TABLE cellspacing="10px" height="30%" border="0" cellpadding="0px" border="0px" style="margin-bottom:10px" class="cuadroFondoBlanco">
        <TR>
            <TD class="etiqueta" align="left">

                <div id="capaEscritura" class="etiqueta" style="width:100%;"></div>
            </td>
        </TR>
    </table>

<div style="text-align:center;margin-bottom:5px;margin-top:5px;">
    <input type="button" class="boton" accesskey="A" value="Aceptar" name="cmdAceptar" onclick="self.parent.opener.retornoXanelaAuxiliar();">
</div>
</div>
</body>
</html>