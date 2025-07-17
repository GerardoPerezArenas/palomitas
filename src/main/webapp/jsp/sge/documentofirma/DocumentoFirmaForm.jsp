<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<script>
        
    function ver(){        

        var finalURL = '<c:url value="/ViewFirma?"/>';
        finalURL = finalURL + "idMunicipio="+'<c:out value="${requestScope.DocumentoFirmaForm.idMunicipio}"/>';
        finalURL = finalURL + "&idProcedimiento="+'<c:out value="${requestScope.DocumentoFirmaForm.idProcedimiento}"/>';
        finalURL = finalURL + "&idEjercicio="+'<c:out value="${requestScope.DocumentoFirmaForm.idEjercicio}"/>';
        finalURL = finalURL + "&idNumeroExpediente="+'<c:out value="${requestScope.DocumentoFirmaForm.idNumeroExpediente}"/>';
        finalURL = finalURL + "&idTramite="+'<c:out value="${requestScope.DocumentoFirmaForm.idTramite}"/>';
        finalURL = finalURL + "&idOcurrenciaTramite="+'<c:out value="${requestScope.DocumentoFirmaForm.idOcurrenciaTramite}"/>';
        finalURL = finalURL + "&idNumeroDocumento="+'<c:out value="${requestScope.DocumentoFirmaForm.idNumeroDocumento}"/>';
        finalURL = finalURL + "&tipoDocumento=" + '<c:out value="${requestScope.DocumentoFirmaForm.tipoDocumento}"/>';
        finalURL = finalURL + "&idNumFirma=" + '<c:out value="${requestScope.DocumentoFirmaForm.idNumFirma}"/>';                    
        abrirXanelaAuxiliar(finalURL,[],
	'width=900,height=100',function(){});
    }
</script>

<c:set var="JS_DBG_INFO" value="false"/>

<tiles:insert page="/jsp/portafirmas/tpls/WindowTemplate.jsp" flush="true">
    <tiles:put name="title" type="string">
        <fmt:message key='Sge.DocumentoFirmaForm.title'/>
    </tiles:put>

    <tiles:put name="head-content" type="string">    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
        
        
        <script type="text/javascript" language="JavaScript">
        </script>
    </tiles:put>
    <tiles:put name="content" type="string">
        <tiles:insert page="/jsp/portafirmas/tpls/AltiaPopUpTemplate.jsp" flush="false">
            <tiles:put name="altia-app-form-title" type="string">
                <fmt:message key='Sge.DocumentoFirmaForm.title'/>
            </tiles:put>
            <tiles:put name="altia-app-form-content" type="string">
                <logic:messagesPresent message="true">
                    <span class="etiqueta" align="center">
                        <html:messages message="true" id="msg">
                            <bean:write name="msg"/>.
                        </html:messages>
                    </span>
                </logic:messagesPresent>
                <center>                  
                    <table width="100%" border="0" >
                        <c:if test="${requestScope.DocumentoFirmaForm.usuarioFirmante >= 0}">
                            <tr>
                                <td class="etiqueta"><fmt:message key="Sge.DocumentoFirmaForm.usuarioFirmante"/>:</td>
                                <td nowrap="true" class="textoSuelto"><c:out value="${requestScope.DocumentoFirmaForm.usuarioFirmante}"/> - (<c:out value="${requestScope.DocumentoFirmaForm.nombreUsuarioFirmante}"/>) </td>
                            </tr>
                        </c:if>
                        <tr>
                            <td class="etiqueta"><fmt:message key="Sge.DocumentoFirmaForm.usuarioDelegadoFirmante"/>:</td>
                            <td nowrap="true" class="textoSuelto"><c:out value="${requestScope.DocumentoFirmaForm.usuarioDelegadoFirmante}"/> - (<c:out value="${requestScope.DocumentoFirmaForm.nombreUsuarioDelegadoFirmante}"/>) </td>
                        </tr>
                        <tr>
                            <td class="etiqueta"><fmt:message key="Sge.DocumentoFirmaForm.estadoFirma"/>:</td>
                            <td nowrap="true" class="textoSuelto">
                                <c:choose>
                                    <c:when test="${requestScope.DocumentoFirmaForm.estadoFirma == 'O'}">
                                        <fmt:message key="Sge.DocumentoFirmaForm.estadoFirma.pendiente"/>
                                    </c:when>
                                    <c:when test="${requestScope.DocumentoFirmaForm.estadoFirma == 'F'}">
                                        <fmt:message key="Sge.DocumentoFirmaForm.estadoFirma.firmado"/>
                                    </c:when>
                                    <c:when test="${requestScope.DocumentoFirmaForm.estadoFirma == 'R'}">
                                        <fmt:message key="Sge.DocumentoFirmaForm.estadoFirma.rechazado"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="${requestScope.DocumentoFirmaForm.estadoFirma}"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <c:if test="${not empty requestScope.DocumentoFirmaForm.firma}">
                            <tr>
                                <td class="etiqueta"><fmt:message key="Sge.DocumentoFirmaForm.firma"/>:</td>
                                <td nowrap="true" class="textoSuelto" onclick="ver();" >
                                    <span class="fa fa-search" aria-hidden="true" /></span>
                                </td>
                            </tr>
                        </c:if>
                        <c:if test="${not empty requestScope.DocumentoFirmaForm.fechaFirma}">
                            <tr>
                                <td class="etiqueta"><fmt:message key="Sge.DocumentoFirmaForm.fechaFirma"/>:</td>
                                <td nowrap="true" class="textoSuelto">
                                    <c:out value="${requestScope.DocumentoFirmaForm.fechaFirma}"/>
                                </td>
                            </tr>
                        </c:if>
                        <c:if test="${not empty requestScope.DocumentoFirmaForm.observaciones}">
                            <tr>
                                <td class="etiqueta"><fmt:message key="Sge.DocumentoFirmaForm.observaciones"/>:</td>
                                <td class="textoSuelto"><c:out value="${requestScope.DocumentoFirmaForm.observaciones}"/></td>
                            </tr>
                        </c:if>

                    </table>
                </center>
                <br>
                <input type="button" class="botonGeneral" name="btnCerrar" value="<fmt:message key='Buttons.close'/>" onclick="top.close();">
            </tiles:put>
            <tiles:put name="altia-app-form-footer" type="string">
                <!-- Separador. -->
                <table height="2px" cellpadding="0px" cellspacing="0px"><tr><td></td></tr></table>
                <!-- Fin separador. -->
            </tiles:put>
        </tiles:insert>
    </tiles:put>
    <tiles:put name="finalJavascript" type="string">
        <script type="text/javascript" language="JavaScript">
            pleaseWait1("off",this);
        </script>
    </tiles:put>
</tiles:insert>
