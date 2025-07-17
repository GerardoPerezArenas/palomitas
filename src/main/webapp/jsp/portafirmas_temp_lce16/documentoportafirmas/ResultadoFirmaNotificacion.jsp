<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@ taglib uri="http://jakarta.apache.org/taglibs/log-1.0" prefix="log" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="string" %>

<%
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String editor = m_Config.getString("editorPlantillas");
    String hVirtual = m_Config.getString("hostVirtual");
    String host="";

    Config m_Docus = ConfigServiceHelper.getConfig("Portafirmas");
    String organizacion = "0";
    String clienteFirma = m_Docus.getString(organizacion+"/PluginPortafirmas");

    try {
        // Si se utiliza la anterior, hay que definir en el fichero de hosts, la propiedad que se defina en el common.properties
        String protocolo = StrutsUtilOperations.getProtocol(request);
        if (hVirtual.equals("")) host = protocolo + "://" + request.getHeader("host") + request.getContextPath() + "/sge/documentofirma/ProcessFirmaNotificacion.do";
        else host = hVirtual + request.getContextPath() + "/sge/documentofirma/ProcessFirmaNotificacion.do";

    } catch (Exception e) {
        e.printStackTrace();
    }

    String FIRMA_VALIDA = (String)request.getAttribute("FIRMA_VALIDA");
    String FIRMA_ALMACENADA = (String)request.getAttribute("FIRMA_ALMACENADA");


%>

    <c:set var="jspClienteFirma">
       <%=clienteFirma%>
    </c:set>

 <meta http-equiv="X-UA-Compatible" content="IE=10"/>
<tiles:insert page="/jsp/portafirmas/tpls/WindowTemplate.jsp" flush="true">
    <tiles:put name="title" type="string">
        <fmt:message key='Portafirmas.FirmaDocumentoPortafirmasForm.title'/>
    </tiles:put>

    <tiles:put name="head-content" type="string">

            
            
            <script type="text/javascript" src="<c:url value='/scripts/firma.js'/>"></script>
            <script type="text/javascript" src="<c:url value='/jsp/portafirmas/resources/firma/firma.js'/>"></script>


        <script type="text/javascript">

            <%@ include file="/jsp/portafirmas/documentoportafirmas/jsEtiquetaEstadoFirma.jsp" %>
            <%@ include file="/jsp/portafirmas/documentoportafirmas/jsCerrarFirmaDocumentoPortafirmas.jsp" %>
            <%@ include file="/jsp/portafirmas/documentoportafirmas/jsGetUrlDocumentoPortafirmas.jsp" %>


                pleaseWait1("off",this);

                var FIRMA_VALIDA = "<%=FIRMA_VALIDA%>";
                var FIRMA_ALMACENADA = "<%=FIRMA_ALMACENADA%>";

                function cerrar(objeto,theForm){
                    var salida = new Array();

                    if(FIRMA_VALIDA!=null && FIRMA_VALIDA=="NO")
                        salida[0] = "FIRNA_NO_VALIDA";
                    else
                    if(FIRMA_VALIDA!=null && FIRMA_VALIDA=="SI" && FIRMA_ALMACENADA!=null && FIRMA_ALMACENADA=="SI")
                        salida[0] = "FIRMA_OK";
                    else
                        salida[0] = "FIRMA_NO_ALMACENADA";

                    self.parent.opener.retornoXanelaAuxiliar(salida);
                }

        </script>
    </tiles:put>
    <tiles:put name="content" type="string">
        <tiles:insert page="/jsp/portafirmas/tpls/AltiaPopUpTemplate.jsp" flush="false">
            <tiles:put name="altia-app-form-title" type="string">
                <fmt:message key='Sge.FirmaNotificacionForm.title'/>
            </tiles:put>
            <tiles:put name="altia-app-form-content" type="string">
                <html:form action="/sge/documentofirma/ProcessFirmaNotificacion.do">
                    <%-- Campos de la búsqueda --%>
                    <html:hidden property="estadoFirma"/>

                    <%-- Campos del documento --%>
                    <html:hidden property="codAdjunto"/>
                    <html:hidden property="codNotificacion"/>
                    <html:hidden property="idMunicipio"/>
                    <html:hidden property="idProcedimiento"/>
                    <html:hidden property="idEjercicio"/>
                    <html:hidden property="idNumeroExpediente"/>
                    <html:hidden property="idTramite"/>
                    <html:hidden property="idOcurrenciaTramite"/>
                    <html:hidden property="idNumeroDocumento"/>
                    <html:hidden property="usuarioFirmante"/>
                    <html:hidden property="tipoDocumento"/>

                    <%-- Campos para el applet de firma --%>
                    <input type="hidden" name="dirtFichero" value="/">
                    <input type="hidden" name="nameFichero" value="tempFile">

                    <input type="hidden" name="appletFirmaHayError" value="N"/>
                    <input type="hidden" name="appletFirmaCodigoError" value="0"/>
                    <input type="hidden" name="appletFirmaMensajeError" value=""/>
                    <input type="hidden" name="fichero" value="">
                    <html:hidden property="firmaBase64"/>
                    <html:hidden property="firma"/>
                    <html:hidden property="hashB64"/>

                    <center>
                        <table width="90%" border="0" align="center" >
                            <tr>
                                <td colspan="2" align="center" style="font-size:11px;font-weight: bold;">
                                    <fmt:message key="Portafirmas.FirmaNotificacion.ConfirmacionFirma"/>
                                </td>
                            </tr>


                     <tr>
                        <td align="center" class="etiqueta">
                            <html:messages message="true" id="msg">
                                '<bean:write name="msg"/>'
                            </html:messages>
                     </tr>


                     <tr>
                         <td colspan="2" align="center">
                             <input type="button" class="boton" name="btnClose" value="<fmt:message key='Buttons.close'/>" onclick="cerrar(this,this.form)">
                         </td>
                     </tr>



                        </table>
                    </center>
                </html:form>
            </tiles:put>
            <tiles:put name="altia-app-form-footer" type="string">
                <%-- Separador. --%>
                <table height="2px" cellpadding="0px" cellspacing="0px"><tr><td></td></tr></table>
                <%-- Fin separador. --%>
			</tiles:put>
        </tiles:insert>
	</tiles:put>
    <tiles:put name="finalJavascript" type="string">
        <script type="text/javascript" language="JavaScript">
            pleaseWait1("off",this);
            <html:messages message="true" id="msg">
            jsp_alerta('A','<bean:write name="msg"/>');

            </html:messages>

        </script>
    </tiles:put>
</tiles:insert>