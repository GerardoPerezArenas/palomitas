<%--
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
 --%>
function getEtiquetaEstadoFirma(est) {
    if (est=='O') return '<fmt:message key="Sge.DocumentoFirmaForm.estadoFirma.pendiente"/>';
    if (est=='F') return '<fmt:message key="Sge.DocumentoFirmaForm.estadoFirma.firmado"/>';
    if (est=='R') return '<fmt:message key="Sge.DocumentoFirmaForm.estadoFirma.rechazado"/>';
    if (est=='S') return '<fmt:message key="Sge.DocumentoFirmaForm.estadoFirma.subsanado"/>';
    return (est?(est):(''));
}