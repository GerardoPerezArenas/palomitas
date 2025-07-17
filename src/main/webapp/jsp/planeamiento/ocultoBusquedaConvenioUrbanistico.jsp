<%@ page import="java.util.Collection"%>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>


<html>
<head><jsp:include page="/jsp/planeamiento/tpls/app-constants.jsp" />
<title> Oculto Busqueda Convenio Urbanistico </title>

<script>

function redirecciona()
{
    <%
    int numRegistros = ((Collection) session.getAttribute("registrosConvenio")).size();
    String registroActual = (String) request.getAttribute("registroActual");
    String nombreFirmante =  (String) request.getAttribute("nombreFirmante");
    %>
    parent.mainFrame.navegacionConsulta(<%=numRegistros%>, <%=registroActual%>);
    parent.mainFrame.showBarraNavegacion();
    <% if (numRegistros>0) { %>
        var comentario;
        var busca = /@intro@/gi;
        var pStrReemplaza = new String(String.fromCharCode(13)+String.fromCharCode(10));
        var anho = '<bean:write name="ConvenioUrbanisticoForm" property="anho" scope="session"/>';
        var numero = '<bean:write name="ConvenioUrbanisticoForm" property="numero" scope="session"/>';
        var codigoSubseccion = '<bean:write name="ConvenioUrbanisticoForm" property="codigoSubseccion" scope="session"/>';
        var objetoConvenio = '<bean:write name="ConvenioUrbanisticoForm" property="objetoConvenio" scope="session"/>';
        objetoConvenio = objetoConvenio.replace(busca,pStrReemplaza);
        var codigoAmbito = '<bean:write name="ConvenioUrbanisticoForm" property="codigoAmbito" scope="session"/>';
        var parcela = '<bean:write name="ConvenioUrbanisticoForm" property="parcela" scope="session"/>';
        var codigoOrganoAprobacion = '<bean:write name="ConvenioUrbanisticoForm" property="codigoOrganoAprobacion" scope="session"/>';
        var fechaAprobacion = '<bean:write name="ConvenioUrbanisticoForm" property="fechaAprobacion" scope="session"/>';
        var fechaBaja = '<bean:write name="ConvenioUrbanisticoForm" property="fechaBaja" scope="session"/>';
        var fechaPublicacion = '<bean:write name="ConvenioUrbanisticoForm" property="fechaPublicacion" scope="session"/>';
        var numeroPublicacion = '<bean:write name="ConvenioUrbanisticoForm" property="numeroPublicacion" scope="session"/>';
        var observaciones = '<bean:write name="ConvenioUrbanisticoForm" property="observaciones" scope="session"/>';
        observaciones = observaciones.replace(busca, pStrReemplaza);
        var file = '<bean:write name="ConvenioUrbanisticoForm" property="archivo" scope="session"/>';
        file = file.replace(busca, pStrReemplaza);
        var nombreFirmante = "<%=nombreFirmante%>";
        var cont = 0;
        var lista = new Array();
        <logic:iterate id="elemento" name="ConvenioUrbanisticoForm" property="anotaciones" scope="session">
            comentario = '<bean:write name="elemento" property="comentarioAnotacion"/>';
            comentario = comentario.replace(busca,pStrReemplaza);
            lista[cont] = ['<bean:write name="elemento" property="numeroAnotacion" />', '<bean:write name="elemento" property="fechaAnotacion" />', comentario];
            cont = cont + 1;
        </logic:iterate>
        cont = 0;
        var listaRect = new Array();
        <logic:iterate id="elemento" name="ConvenioUrbanisticoForm" property="rectificaciones" scope="session">
            comentario = '<bean:write name="elemento" property="comentarioRectificacion"/>';
            comentario = comentario.replace(busca,pStrReemplaza);
            listaRect[cont] = ['<bean:write name="elemento" property="numeroRectificacion" />', '<bean:write name="elemento" property="fechaRectificacion" />', comentario];
            cont = cont + 1;
        </logic:iterate>
        parent.mainFrame.rellenaDatosRegistro(anho, numero, codigoSubseccion, objetoConvenio,
                codigoAmbito, parcela, codigoOrganoAprobacion, fechaAprobacion, fechaBaja, fechaPublicacion,
                numeroPublicacion, observaciones, file, nombreFirmante, lista, listaRect);
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
