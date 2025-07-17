<%@ page import="java.util.Vector"%>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<html>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<title>::: Oculto Buscar Terceros	:::</title>
<script type="text/javascript">
function inicializar(){
    <%
        if (((Vector)session.getAttribute("DepuracionTerceros.tercerosRepetidos")).isEmpty()) {
    %>
            parent.mainFrame.noTerceros();
    <%
        } else {
    %>
            var numResultados = <bean:write name="DepuracionTerceros.numResultados"/>;
            var rows = new Array();
            <logic:iterate id="elemento" name="DepuracionTerceros.tercerosRepetidos" scope="session">
            rows[rows.length] = ["<bean:write name="elemento" property="tipoDocumento"/>",
                    "<bean:write name="elemento" property="documento"/>",
                    "<bean:write name="elemento" property="codTercero"/>",
                    "<bean:write name="elemento" property="nombre"/>",
                    "<bean:write name="elemento" property="apellido1"/>",
                    "<bean:write name="elemento" property="apellido2"/>"];
            </logic:iterate>

            parent.mainFrame.tabTerceros.setLineas(rows);
            parent.mainFrame.tabTerceros.displayTabla();
            parent.mainFrame.actualizarNavegacion(numResultados);
        <%
            }
        %>
    }

</script>
<body onload="inicializar();">
<p>&nbsp;</p>
</body>
</html>
