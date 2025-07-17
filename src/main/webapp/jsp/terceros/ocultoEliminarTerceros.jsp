<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<html>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp"/>
<title>::: Oculto Eliminar Terceros :::</title>

<script type="text/javascript">
function inicializar(){
    parent.mainFrame.rows = new Array();
    <logic:iterate id="elemento" name="DepuracionTerceros.terceros" scope="session">
    parent.mainFrame.rows[parent.mainFrame.rows.length] = ["<bean:write name="elemento" property="tipoDocumento"/>",
        "<bean:write name="elemento" property="documento"/>",
        "<bean:write name="elemento" property="codTercero"/>",
        "<bean:write name="elemento" property="nombre"/>",
        "<bean:write name="elemento" property="apellido1"/>",
        "<bean:write name="elemento" property="apellido2"/>"];
    </logic:iterate>

    parent.mainFrame.tabla.setLineas(parent.mainFrame.rows);
    parent.mainFrame.tabla.displayTabla();

    parent.mainFrame.rowsDom = new Array();
    parent.mainFrame.tablaDomicilios.setLineas(parent.mainFrame.rowsDom);
    parent.mainFrame.tablaDomicilios.displayTabla();

    parent.mainFrame.activarBotonEliminarTercSim(false);
    parent.mainFrame.activarBotonCopiarDomicilio(false);
}
</script>
<body onload="inicializar();">
<p>&nbsp;</p>
</body>
</html>
