<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib prefix="c" uri="/WEB-INF/tlds/c.tld" %>

<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<html>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<title>::: Oculto Mostar Domicilios del Tercero :::</title>
<script type="text/javascript">
function inicializar(){
    var rows = new Array();
    <logic:iterate id="tercero" name="DepuracionTerceros.terceros" scope="session">
        <c:if test="${tercero.codTercero==param.codTercero}">
            <logic:iterate id="elemento" name="tercero" property="domicilios">
                rows[rows.length] = ["<bean:write name="elemento" property="codProvincia"/>",
                        "<bean:write name="elemento" property="provincia"/>",
                        "<bean:write name="elemento" property="codMunicipio"/>",
                        "<bean:write name="elemento" property="municipio"/>",
                        "<bean:write name="elemento" property="codDomicilio"/>",
                        "<bean:write name="elemento" property="domicilio"/>",
                        "<bean:write name="elemento" property="cp"/>"];
            </logic:iterate>
        </c:if>
    </logic:iterate>

    parent.mainFrame.tablaDomicilios.setLineas(rows);
    parent.mainFrame.tablaDomicilios.displayTabla();
}
</script>
<body onload="inicializar();">
<p>&nbsp;</p>
</body>
</html>