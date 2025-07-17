<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>

<script type="text/javascript">

    function verInforme() {
        window.open(DOMAIN_NAME+"<c:url value='/VerInforme'/>?tipo=informeParticular&ruta=<c:out value="${requestScope.InformeParticularForm.rutaInforme}"/>&formato=<c:out value="${requestScope.InformeParticularForm.tipoFichero}"/>");
    }

    function volver() {
        document.forms[1].tipoInforme.value = '<c:out value="${requestScope.InformeParticularForm.tipoInforme}"/>';
        document.forms[1].action =DOMAIN_NAME+ "<c:url value='/gestionInformes/InformeParticular.do'/>";
        document.forms[1].submit();
    }

</script>

<form name="form" action="/gestionInformes/GenerarInformeParticular.do" target="_self">

    <input type="hidden" name="tipoInforme"/>
    <div class="filtroGestionInformes">
        <div class="filtroGestionInformesCampos">

            <c:choose>
                <c:when test="${empty requestScope.InformeParticularForm.errores}">
                    <div align="center">
                        <span class="etiqBoldMaxiFullWidth">
                            <fmt:message key="InformeParticular.exitoInforme"/>
                        </span>
                    </div>
                </c:when>
                <c:otherwise>
                    <div align="center">
                        <span class="etiqBoldMaxiFullWidth">
                            <fmt:message key="InformeParticular.errorInforme"/>
                        </span>
                    </div>
                    <p></p>
                    <c:forEach items="${requestScope.InformeParticularForm.errores}" var="error">
                        <div align="center">
                            <span class="etiqBoldMaxiFullWidth">
                                <c:out value="${error}"/> 
                            </span>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </div>



</form>
