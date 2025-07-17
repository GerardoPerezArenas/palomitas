<%@ page import="java.util.Vector"%>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<html>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<title>::: Oculto Buscar Vias :::</title>
<script type="text/javascript">
function inicializar(){
<%
    if (((Vector)session.getAttribute("DepuracionVias.viasRepetidas")).isEmpty()) {
%>
        parent.mainFrame.noVias();
<%
    } else {
%>
        var rows = new Array();
        <logic:iterate id="elemento" name="DepuracionVias.viasRepetidas" scope="session">
            rows[rows.length] = ["<bean:write name="elemento" property="codTipoVia"/>",
                    "<bean:write name="elemento" property="tipoVia"/>",
                    "<bean:write name="elemento" property="codVia"/>",
                    "<bean:write name="elemento" property="descVia"/>",
                    "<bean:write name="elemento" property="codProvincia"/>",
                    "<bean:write name="elemento" property="provincia"/>",
                    "<bean:write name="elemento" property="codMunicipio"/>",
                    "<bean:write name="elemento" property="municipio"/>"]
        </logic:iterate>

        parent.mainFrame.tabVias.setLineas(rows);
        parent.mainFrame.tabVias.displayTabla();
<%
    }
%>
}
</script>
<body onload="inicializar();">
<p>&nbsp;</p>
</body>
</html>
