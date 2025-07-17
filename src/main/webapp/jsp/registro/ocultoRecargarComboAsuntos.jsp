<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<html>
<head>
<title> Recargar combo asuntos registro </title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />

<script>
    function redirecciona(){ 
        var cod_asuntos = new Array();
        var desc_asuntos = new Array();
        var uni_asuntos = new Array();

        var cont = 0;
        <c:forEach var="asunto" items="${requestScope.asuntos_combo}">
            uni_asuntos[cont] = '<c:out value="${asunto.unidadRegistro}"/>';
            cod_asuntos[cont]  ='<c:out value="${asunto.codigo}"/>';
            desc_asuntos[cont] ="<str:escape><c:out value="${asunto.descripcion}"/></str:escape>";
            cont++;
        </c:forEach>
        
        parent.mainFrame.actualizarComboAsuntos(uni_asuntos,cod_asuntos,desc_asuntos);
    }
</script>
</head>
<body onLoad="redirecciona();">
</body>
</html>