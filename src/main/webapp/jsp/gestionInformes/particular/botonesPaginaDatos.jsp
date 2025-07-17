<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<c:if test="${requestScope.InformeParticularForm.estadoInforme == 'pedirDatos'}">
<input type="button" class="botonAplicacion" value="<fmt:message key="InformeParticular.botonGenerar"/>" id="botonImportar" name="botonImportar" onClick="generarInforme();">
</c:if>

<c:if test="${requestScope.InformeParticularForm.estadoInforme == 'informeGenerado'}">
    <input type="button" class="botonAplicacion" value="<fmt:message key="InformeParticular.botonVolver"/>" name="botonImportar" onClick="volver();">
    <c:if test="${empty requestScope.InformeParticularForm.errores}">
        <input type="button" class="botonAplicacion" value="<fmt:message key="InformeParticular.botonVerInforme"/>" name="botonImportar" onClick="verInforme();">
    </c:if>
</c:if>
