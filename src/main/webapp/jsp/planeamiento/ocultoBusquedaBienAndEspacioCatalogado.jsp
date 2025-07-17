<%@ page import="java.util.Collection"%>
<%@ page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>


<html>
<head><jsp:include page="/jsp/planeamiento/tpls/app-constants.jsp" />
<title> Oculto Busqueda Bienes y Espacios Catalogados </title>

<script>

function redirecciona()
{
    <%
    int numRegistros = ((Collection) session.getAttribute("registrosBien")).size();
    String registroActual = (String) request.getAttribute("registroActual");
    GeneralValueObject gVO = (GeneralValueObject) request.getAttribute("domicilio");
    %>
    parent.mainFrame.navegacionConsulta(<%=numRegistros%>, <%=registroActual%>);
    parent.mainFrame.showBarraNavegacion();
    <% if (numRegistros>0) { %>
        var comentario;
        var busca = /@intro@/gi;
        var pStrReemplaza = new String(String.fromCharCode(13)+String.fromCharCode(10));
        var anho = '<bean:write name="BienAndEspacioCatalogadoForm" property="anho" scope="session"/>';
        var numero = '<bean:write name="BienAndEspacioCatalogadoForm" property="numero" scope="session"/>';
        var denominacionBien = '<bean:write name="BienAndEspacioCatalogadoForm" property="denominacionBien" scope="session"/>';
        denominacionBien = denominacionBien.replace(busca, pStrReemplaza);
        var codigoDomicilio = '<bean:write name="BienAndEspacioCatalogadoForm" property="codigoDomicilio" scope="session"/>';
        var codigoCatalogacion = '<bean:write name="BienAndEspacioCatalogadoForm" property="codigoCatalogacion" scope="session"/>';
        var codigoGradoProteccion = '<bean:write name="BienAndEspacioCatalogadoForm" property="codigoGradoProteccion" scope="session"/>';
        var codigoRelacionBien = '<bean:write name="BienAndEspacioCatalogadoForm" property="codigoRelacionBien" scope="session"/>';
        var fechaAprobacion = '<bean:write name="BienAndEspacioCatalogadoForm" property="fechaAprobacion" scope="session"/>';
        var fechaBaja = '<bean:write name="BienAndEspacioCatalogadoForm" property="fechaBaja" scope="session"/>';
        var fechaPublicacion = '<bean:write name="BienAndEspacioCatalogadoForm" property="fechaPublicacion" scope="session"/>';
        var numeroPublicacion = '<bean:write name="BienAndEspacioCatalogadoForm" property="numeroPublicacion" scope="session"/>';
        var observaciones = '<bean:write name="BienAndEspacioCatalogadoForm" property="observaciones" scope="session"/>';
        observaciones = observaciones.replace(busca, pStrReemplaza);
        var file = '<bean:write name="BienAndEspacioCatalogadoForm" property="archivo" scope="session"/>';
        file = file.replace(busca, pStrReemplaza);
        var cont = 0;
        var lista = new Array();
        <logic:iterate id="elemento" name="BienAndEspacioCatalogadoForm" property="anotaciones" scope="session">
            comentario = '<bean:write name="elemento" property="comentarioAnotacion"/>';
            comentario = comentario.replace(busca,pStrReemplaza);
            lista[cont] = ['<bean:write name="elemento" property="numeroAnotacion" />', '<bean:write name="elemento" property="fechaAnotacion" />', comentario];
            cont = cont + 1;
        </logic:iterate>
        cont = 0;
        var listaRect = new Array();
        <logic:iterate id="elemento" name="BienAndEspacioCatalogadoForm" property="rectificaciones" scope="session">
            comentario = '<bean:write name="elemento" property="comentarioRectificacion"/>';
            comentario = comentario.replace(busca,pStrReemplaza);
            listaRect[cont] = ['<bean:write name="elemento" property="numeroRectificacion" />', '<bean:write name="elemento" property="fechaRectificacion" />', comentario];
            cont = cont + 1;
        </logic:iterate>

        var domCompleto = parent.mainFrame.domicilioCompleto("<%=gVO.getAtributo("codTipoVia")%>",
                 "<%=gVO.getAtributo("descTipoVia")%>", "<%=gVO.getAtributo("descVia")%>", "<%=gVO.getAtributo("numDesde")%>",
                 "<%=gVO.getAtributo("letraDesde")%>", "<%=gVO.getAtributo("numHasta")%>", "<%=gVO.getAtributo("letraHasta")%>",
                 "<%=gVO.getAtributo("bloque")%>", "<%=gVO.getAtributo("portal")%>", "<%=gVO.getAtributo("escalera")%>",
                 "<%=gVO.getAtributo("planta")%>", "<%=gVO.getAtributo("puerta")%>", "<%=gVO.getAtributo("domicilio")%>",
                 "<%=gVO.getAtributo("descESI")%>", "<%=gVO.getAtributo("codECO")%>", "<%=gVO.getAtributo("descECO")%>",
                 "<%=gVO.getAtributo("refCatastral")%>");
        parent.mainFrame.rellenaDatosRegistro(anho, numero, denominacionBien, codigoDomicilio, domCompleto,
                codigoCatalogacion, codigoGradoProteccion, codigoRelacionBien, fechaAprobacion, fechaBaja, fechaPublicacion,
                numeroPublicacion, observaciones, file, lista, listaRect);
        parent.mainFrame.mostrarBotonesModificacion();
        parent.mainFrame.ocultarModificacion();
    <%}%>
}
</script>

</head>
<body onLoad="redirecciona();">

<p>&nbsp;<p><center>


</body>
</html>
