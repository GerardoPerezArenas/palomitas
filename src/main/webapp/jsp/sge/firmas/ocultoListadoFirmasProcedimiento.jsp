<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<html>
    <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Oculto Guardado Circuito</title>

        <script type="text/javascript">
                function inicializar(){
                    parent.mainFrame.datosGuardados('<c:out value="${requestScope.exito}"/>','<c:out value="${requestScope.numeroFirmas}"/>');
                }

        </script>
    </head>
    <body onload="inicializar();">
    </body>
</html>
