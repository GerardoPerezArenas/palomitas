<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>


<html>
<head><jsp:include page="/jsp/planeamiento/tpls/app-constants.jsp" />
<title> Oculto Anotaciones de Rectificación Convenio </title>

<script>

function redirecciona()
{
    var cont = 0;

    var lista = new Array();
    var comentario;
    var busca = /@intro@/gi;
    var pStrReemplaza = new String(String.fromCharCode(13)+String.fromCharCode(10));
    <logic:iterate id="elemento" name="ConvenioUrbanisticoForm" property="rectificaciones" scope="session">
        comentario = '<bean:write name="elemento" property="comentarioRectificacion"/>';
        comentario = comentario.replace(busca,pStrReemplaza);
        lista[cont] = ['<bean:write name="elemento" property="numeroRectificacion" />', '<bean:write name="elemento" property="fechaRectificacion" />', comentario];
        cont = cont + 1;
    </logic:iterate>
    parent.mainFrame.recuperaRectificaciones(lista);
}
</script>

</head>
<body onLoad="redirecciona();">

<p>&nbsp;<p><center>


</body>
</html>
