<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>

<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<html>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<title>::: Oculto Eliminar Domicilios :::</title>

<script type="text/javascript">
function inicializar(){
    parent.mainFrame.rowsDomSeleccionado = new Array();
    var nuevaPosDomPrincipal;
    var codDomPrincipal = "<bean:write name="DepuracionTerceros.tercero" property="codDomPrincipal" scope="session"/>";
<logic:iterate id="elemento" name="DepuracionTerceros.tercero" property="domicilios" scope="session">
    if (codDomPrincipal=="<bean:write name="elemento" property="codDomicilio"/>") {
        parent.mainFrame.rowsDomSeleccionado[parent.mainFrame.rowsDomSeleccionado.length] =
            ["<bean:write name="elemento" property="codProvincia"/>",
            "<bean:write name="elemento" property="provincia"/>",
            "<bean:write name="elemento" property="codMunicipio"/>",
            "<bean:write name="elemento" property="municipio"/>",
            "<bean:write name="elemento" property="codDomicilio"/>",
            "<bean:write name="elemento" property="domicilio"/>",
            "<bean:write name="elemento" property="cp"/>",
            "<input type='radio' name='domPrincipal' onclick='pulsarDomPrincipal("+parent.mainFrame.rowsDomSeleccionado.length+")' checked>"];
        nuevaPosDomPrincipal = (parent.mainFrame.rowsDomSeleccionado.length)-1;
    } else {
        parent.mainFrame.rowsDomSeleccionado[parent.mainFrame.rowsDomSeleccionado.length] =
            ["<bean:write name="elemento" property="codProvincia"/>",
            "<bean:write name="elemento" property="provincia"/>",
            "<bean:write name="elemento" property="codMunicipio"/>",
            "<bean:write name="elemento" property="municipio"/>",
            "<bean:write name="elemento" property="codDomicilio"/>",
            "<bean:write name="elemento" property="domicilio"/>",
            "<bean:write name="elemento" property="cp"/>",
            "<input type='radio' name='domPrincipal' onclick='pulsarDomPrincipal("+parent.mainFrame.rowsDomSeleccionado.length+")'"];
    }
</logic:iterate>
    parent.mainFrame.tablaDomiciliosSelec.setLineas(parent.mainFrame.rowsDomSeleccionado);
    parent.mainFrame.tablaDomiciliosSelec.displayTabla();
    parent.mainFrame.activarBotonEliminarDomicilios(false);
    parent.mainFrame.document.forms[0].posDomPrincipal.value = nuevaPosDomPrincipal;
    parent.mainFrame.document.forms[0].codDomPrincipal.value = codDomPrincipal;
}
</script>
<body onload="inicializar();">
<p>&nbsp;</p>
</body>
</html>
