<%@ page import="java.util.Collection"%>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>


<html>
<head><jsp:include page="/jsp/planeamiento/tpls/app-constants.jsp" />
<title> Oculto Busqueda Instrumento Planeamiento </title>

<script>

function redirecciona()
{
    <%
    int numRegistros = ((Collection) session.getAttribute("registrosPlaneamiento")).size();
    String registroActual = (String) request.getAttribute("registroActual");
    %>
    parent.mainFrame.navegacionConsulta(<%=numRegistros%>, <%=registroActual%>);
    parent.mainFrame.showBarraNavegacion();
    <% if (numRegistros>0) { %>
        var comentario;
        var busca = /@intro@/gi;
        var pStrReemplaza = new String(String.fromCharCode(13)+String.fromCharCode(10));
        var anho = '<bean:write name="InstrumentoPlaneamientoForm" property="anho" scope="session"/>';
        var numero = '<bean:write name="InstrumentoPlaneamientoForm" property="numero" scope="session"/>';
        var codigoSubseccion = '<bean:write name="InstrumentoPlaneamientoForm" property="codigoSubseccion" scope="session"/>';
        var codigoTipo = '<bean:write name="InstrumentoPlaneamientoForm" property="codigoTipo" scope="session"/>';
        var codigoProcedimiento = '<bean:write name="InstrumentoPlaneamientoForm" property="codigoProcedimiento" scope="session"/>';
        var numeroProcedimiento = '<bean:write name="InstrumentoPlaneamientoForm" property="numeroProcedimiento" scope="session"/>';
        var codigoAmbito = '<bean:write name="InstrumentoPlaneamientoForm" property="codigoAmbito" scope="session"/>';
        var parcela = '<bean:write name="InstrumentoPlaneamientoForm" property="parcela" scope="session"/>';
        var promotor = '<bean:write name="InstrumentoPlaneamientoForm" property="promotor" scope="session"/>';
        var codigoOrganoAprobacion = '<bean:write name="InstrumentoPlaneamientoForm" property="codigoOrganoAprobacion" scope="session"/>';
        var fechaVigencia = '<bean:write name="InstrumentoPlaneamientoForm" property="fechaVigencia" scope="session"/>';
        var fechaAprobacion = '<bean:write name="InstrumentoPlaneamientoForm" property="fechaAprobacion" scope="session"/>';
        var fechaBaja = '<bean:write name="InstrumentoPlaneamientoForm" property="fechaBaja" scope="session"/>';
        var fechaPublicacion = '<bean:write name="InstrumentoPlaneamientoForm" property="fechaPublicacion" scope="session"/>';
        var numeroPublicacion = '<bean:write name="InstrumentoPlaneamientoForm" property="numeroPublicacion" scope="session"/>';
        var observaciones = '<bean:write name="InstrumentoPlaneamientoForm" property="observaciones" scope="session"/>';
        observaciones = observaciones.replace(busca, pStrReemplaza);
        var file = '<bean:write name="InstrumentoPlaneamientoForm" property="archivo" scope="session"/>';
        file = file.replace(busca, pStrReemplaza);
        var cont = 0;
        var lista = new Array();
        <logic:iterate id="elemento" name="InstrumentoPlaneamientoForm" property="anotaciones" scope="session">
            comentario = '<bean:write name="elemento" property="comentarioAnotacion"/>';
            comentario = comentario.replace(busca,pStrReemplaza);
            lista[cont] = ['<bean:write name="elemento" property="numeroAnotacion" />', '<bean:write name="elemento" property="fechaAnotacion" />', comentario];
            cont = cont + 1;
        </logic:iterate>
        cont = 0;
        var listaRect = new Array();
        <logic:iterate id="elemento" name="InstrumentoPlaneamientoForm" property="rectificaciones" scope="session">
            comentario = '<bean:write name="elemento" property="comentarioRectificacion"/>';
            comentario = comentario.replace(busca,pStrReemplaza);
            listaRect[cont] = ['<bean:write name="elemento" property="numeroRectificacion" />', '<bean:write name="elemento" property="fechaRectificacion" />', comentario];
            cont = cont + 1;
        </logic:iterate>
        parent.mainFrame.rellenaDatosRegistro(anho, numero, codigoSubseccion, codigoTipo, codigoProcedimiento,
                numeroProcedimiento, codigoAmbito, parcela, promotor, codigoOrganoAprobacion, fechaVigencia, fechaAprobacion,
                fechaBaja, fechaPublicacion, numeroPublicacion, observaciones, file, lista, listaRect);
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
