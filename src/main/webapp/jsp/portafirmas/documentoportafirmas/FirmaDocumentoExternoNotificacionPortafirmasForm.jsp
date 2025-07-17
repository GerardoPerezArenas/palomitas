<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page import="es.altia.util.struts.StrutsUtilOperations" %>

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


    String servlet="";
    String urlApplet="";
    if ("".equals(hVirtual)) {
        servlet= "http://"+request.getHeader("Host")+request.getContextPath();
        urlApplet =StrutsUtilOperations.getProtocol(request)+"://"+request.getHeader("Host")+request.getContextPath()+"/jsp/portafirmas/resources/firma";
    }else {
        servlet= hVirtual+request.getContextPath();
        urlApplet =hVirtual+request.getContextPath()+"/jsp/portafirmas/resources/firma";
    }//if ("".equals(hVirtual)) 

    try {
        // Si se utiliza la anterior, hay que definir en el fichero de hosts, la propiedad que se defina en el common.properties
        String protocolo = StrutsUtilOperations.getProtocol(request);
        if (hVirtual.equals("")) host = protocolo + "://" + request.getHeader("host") + request.getContextPath() + "/portafirmas/documentoportafirmas/ProcessFirmaDocumentoExternoNotificacionPortafirmas.do";
        else host = hVirtual + request.getContextPath() + "/portafirmas/documentoportafirmas/ProcessFirmaDocumentoExternoNotificacionPortafirmas.do";
    } catch (Exception e) {
        e.printStackTrace();
    }//try-catch
%>
    <c:set var="jspClienteFirma">
       <%=clienteFirma%>
    </c:set>
    
 <meta http-equiv="X-UA-Compatible" content="IE=9"/>
    <tiles:insert page="/jsp/portafirmas/tpls/WindowTemplate.jsp" flush="true">
        <tiles:put name="title" type="string">
            <fmt:message key='Portafirmas.FirmaDocumentoPortafirmasForm.title'/>
        </tiles:put>

        <tiles:put name="head-content" type="string">
            
            
            <script type="text/javascript" src="<c:url value='/scripts/firma.js'/>"></script>
            <script type="text/javascript" src="<c:url value='/jsp/portafirmas/resources/firma/firma.js'/>"></script>
        
            <script type="text/javascript" src="<c:url value='/jsp/portafirmas/resources/afirma3/common-js/deployJava.js'/>"></script>
            <script type="text/javascript" src="<c:url value='/jsp/portafirmas/resources/afirma3/common-js/instalador.js'/>"></script>
            <script type="text/javascript" src="<c:url value='/jsp/portafirmas/resources/afirma3/common-js/firma.js'/>"></script>
            <script type="text/javascript" src="<c:url value='/jsp/portafirmas/resources/afirma3/constantes.js'/>"></script>
        
         
            <c:if test="${jspClienteFirma eq 'ASF'}">
                <%@ include file="/jsp/portafirmas/resources/asf/functionsJsFirma.jspf" %>
            </c:if>
            
            <c:if test="${jspClienteFirma eq 'VIAFIRMA'}">
                <%@ include file="/jsp/portafirmas/resources/viafirma/functionsJsViaFirma.jspf" %>
            </c:if>
            
            <script type="text/javascript">
                <%@ include file="jsEtiquetaEstadoFirma.jsp" %>
                <%@ include file="jsCerrarFirmaOtroDocumentoPortafirmasForm.jsp" %>
                <%@ include file="jsGetUrlDocumentoPortafirmas.jsp" %>

                function btnFirmar_click(theSender, theForm) {                    
                    document.forms[0].action='<%=host%>';
                    var ficheroFirmado;
          
                    if ( (theSender) && (theForm) ) {                   
                        /** URL DE LA QUE SE DESCARGA EL DOCUMENTO EXTERNO PARA PROCEDER A SU FIRMADO */
                        var url =  getUrlForDownloadDocumentoExternoNotificacion(
                        document.forms[0].idMunicipio.value, document.forms[0].idEjercicio.value, 
                        document.forms[0].idNumeroExpediente.value, document.forms[0].idNumeroDocumento.value,
                        document.forms[0].idProcedimiento.value);

                        var modoOperacionCerts = MODO_CERTIFICADO_NIF;
                        var idFirmante = '<c:out value="${requestScope.FirmaDocumentoPortafirmasForm.nifUsuarioFirmante}"/>';
                
                        if (idFirmante == '') modoOperacionCerts = MODO_CERTIFICADO_LISTACA;
                        var dnCA = "<fmt:message key='Portafirmas.FirmaDocumentoPortafirmasForm.CA'/>";
                        
                        var rutaFicheroCliente = descargarFichero(url);
                        var plataformaFirma = "";
                        if (CLIENTE_FIRMA=="FLEXIA"){
                            ficheroFirmado = firmarFichero(rutaFicheroCliente,theForm);
                            plataformaFirma = "FLEXIA";
                        } else if (CLIENTE_FIRMA=="AFIRMA"){ 
                            ficheroFirmado = firmarFicheroAFirma(rutaFicheroCliente,theForm);
                            plataformaFirma = "AFIRMA";
                        }else if (CLIENTE_FIRMA=="ASF"){
                             ficheroFirmado = firmarFicheroClienteAsf(rutaFicheroCliente);
                             plataformaFirma = "ASF";
                        }else if(CLIENTE_FIRMA=="VIAFIRMA"){
                            ficheroFirmado = firmarFicheroClienteVia(rutaFicheroCliente);
                            plataformaFirma = "VIAFIRMA";
                        }//if CLIENTE_FIRMA
                        
                        document.forms[0].plataformaFirma.value = plataformaFirma;
                                                
                        if (theForm.appletFirmaHayError.value == "N" && theForm.firma.value!="") {
                            jsp_alerta('A','<fmt:message key='Portafirmas.FirmaDocumentoPortafirmasForm.FirmadoCorrectamente'/>',APP_TITLE);
                            pleaseWait1("on",this);                                  
                            theForm.submit();
                        } else if (theForm.appletFirmaHayError.value == "N" && theForm.firma.value=="") {
                            jsp_alerta('A','<fmt:message key='Portafirmas.FirmaDocumentoPortafirmasForm.NoFirmado'/>',
                            APP_TITLE);
                            btnClose_click(theSender,theForm);
                        } else {
                            var cadenaError = "("+theForm.appletFirmaCodigoError.value+") " + theForm.appletFirmaMensajeError.value;
                            jsp_alerta('A',cadenaError,APP_TITLE);
                            btnClose_click(theSender,theForm);
                        }//if
                    }//if ( (theSender) && (theForm) ) 
                }//btnFirmar_click

                var blnComprobacionAppletListo = false;

                function waituntilready() {
                    if (CLIENTE_FIRMA=="FLEXIA" ){
                        try {
                            var applet = document.applets.APLFIRMANUEVO;
                            blnComprobacionAppletListo = (applet) && (applet.isActive());
                        } catch (jsEx) {
                            blnComprobacionAppletListo = false;
                        }
                        if ( blnComprobacionAppletListo ) {
           
                            var theForm = document.FirmaDocumentoPortafirmasForm;
                            theForm.action='<%=host%>';
                            theForm.btnFirmar.disabled = false;
                            pleaseWait1("off",this);
                        } else {
                            setTimeout('waituntilready()',3000);
                        }
                    } else if (CLIENTE_FIRMA=="AFIRMA"){
                        cargarAppletInstalador();
                        cargarAppletFirma('COMPLETA');
                        var theForm = document.FirmaDocumentoPortafirmasForm;
                        theForm.action='<%=host%>';
                        theForm.btnFirmar.disabled = false;
                        pleaseWait1("off",this);
                    }else if (CLIENTE_FIRMA=="ASF"){
                        inicializarClienteFirma();
                         var theForm = document.FirmaDocumentoPortafirmasForm;
                        theForm.action='<%=host%>';
                        theForm.btnFirmar.disabled = false;
                         pleaseWait1("off",this);

                    }else if(CLIENTE_FIRMA=="VIAFIRMA"){
                        inicializarClienteViaFirma();
                        var theForm = document.FirmaDocumentoPortafirmasForm;
                        theForm.action='<%=host%>';
                        theForm.btnFirmar.disabled = false;
                        pleaseWait1("off",this);
                    }//if CLIENTE_FIRMA
                }//waituntilready
        </script>
    </tiles:put>
    <tiles:put name="content" type="string">
        <tiles:insert page="/jsp/portafirmas/tpls/AltiaTemplate.jsp" flush="false">
            <tiles:put name="altia-app-firsttitle" type="string">
                <c:out value="${sessionScope.usuario.ent}" />
            </tiles:put>
            <tiles:put name="altia-app-secondtitle" type="string">
                <fmt:message key='Portafirmas.Application.title'/>
            </tiles:put>
            <tiles:put name="altia-app-user" type="string">
                <c:out value="${sessionScope.usuario.nombreUsu}"/>
            </tiles:put>
            <tiles:put name="altia-app-form-title" type="string">
                <fmt:message key='Portafirmas.FirmaDocumentoPortafirmasForm.title'/>
            </tiles:put>
            <tiles:put name="altia-app-form-content" type="string">
                <html:form action="/portafirmas/documentoportafirmas/ProcessFirmaDocumentoExternoNotificacionPortafirmas.do">
                    <%-- Campos de la búsqueda --%>
                    <html:hidden property="startIndex"/>
                    <html:hidden property="count"/>
                    <html:hidden property="totalCount"/>
                    <html:hidden property="startIndex"/>
                    <html:hidden property="doPopUp"/>
                    <html:hidden property="doPrintPreview"/>
                    <html:hidden property="doPageByPage" value="false"/>
                    <html:hidden property="doSearch"/>
                    <html:hidden property="canEdit"/>
                    <html:hidden property="sort"/>
                    <html:hidden property="sortField"/>
                    <html:hidden property="chkEstado"/>
                    <html:hidden property="estadoFirma"/>

                    <html:hidden property="idNumeroDocumento"/>
                    <html:hidden property="usuarioFirmante"/>
                    <html:hidden property="descripcionDocumento"/>                    
                    <html:hidden property="idPresentado"/>
                    <html:hidden property="idEjercicio"/>
                    <html:hidden property="idProcedimiento"/>
                    <html:hidden property="idNumeroExpediente"/>
                    <html:hidden property="idMunicipio"/>
                    <html:hidden property="plataformaFirma"/>
                    <%-- Campos para el applet de firma --%>
                    <input type="hidden" name="dirtFichero" value="/">
                    <input type="hidden" name="nameFichero" value="tempFile">

                    <input type="hidden" name="appletFirmaHayError" value="N"/>
                    <input type="hidden" name="appletFirmaCodigoError" value="0"/>
                    <input type="hidden" name="appletFirmaMensajeError" value=""/>
                    <input type="hidden" name="fichero" value="">
                    <html:hidden property="firma"/>
                    <html:hidden property="firmaBase64"/>
                    <html:hidden property="hashB64"/>

                    <center>
                        <table width="90%" border="0" align="center" >
                            <tr>
                                <td colspan="2" align="center" style="font-size:11px;font-weight: bold;">
                                    <fmt:message key="Portafirmas.FirmaDocumentoPortafirmasForm.ConfirmacionFirma"/>
                                </td>
                            </tr>
                            <tr>                       
                                <td align="center">
                                    <%-- APPLET DE DESCARGA DEL DOCUMENTO A FIRMAR --%>
                                    <applet id="APLDESCARGA"  code="es.altia.flexia.applet.AppletDescargaFichero" codebase='<%=urlApplet%>' archive="AppletDescargaDocumento.jar"
                                            height="25" width="25" align="middle">
                                        <PARAM name="IDIOMA" value="E">
                                        <PARAM name="java_codebase" value="<c:url value='/jsp/portafirmas/resources/firma'/>">
                                        <PARAM name="java_archive" value="AppletDescargaDocumento.jar">
                                        <PARAM name="type" value="application/x-java-applet;version=1.5">
                                        <PARAM name="session_id" value="<%=session.getId()%>">
                                    </applet>
                                    <div id="divApplets">
                                        <c:if test="${jspClienteFirma eq 'VIAFIRMA'}">
                                            <applet id="appletUtils"  code="es.altia.applets.UtilsApplet" codebase="/jsp/portafirmas/resources/viafirma" archive="appletUtilidades.jar"
                                                    height="25" width="25" align="middle">
                                                <PARAM name="Locale" value="es_ES">
                                                <PARAM name="java_codebase" value="<c:url value='/jsp/portafirmas/resources/viafirma'/>">
                                                <PARAM name="java_archive" value="AppletUtils.jar">
                                                <PARAM name="type" value="application/x-java-applet;version=1.5">
                                                <PARAM name="session_id" value="<%=session.getId()%>"
                                                <PARAM name="UserAgent" value="<%=request.getHeader("User-Agent")%>">
                                            </applet>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                            <c:if test="${jspClienteFirma eq 'ASF'}">
                                <tr>
                                    <td colspan="2" align="center">
                                        <%@ include file="/jsp/portafirmas/resources/asf/asfFirma.jspf" %>
                                    </td>
                                </tr>
                           </c:if>
                            <tr>
                                <td colspan="2" align="center">
                                    <input type="button" class="boton" name="btnFirmar" value="<fmt:message key='Buttons.sign'/>" onclick="btnFirmar_click(this,this.form)" DISABLED>
                                    <input type="button" class="boton" name="btnClose" value="<fmt:message key='Buttons.cancel'/>" onclick="btnClose_click(this,this.form)">
                                </td>
                            </tr>
                        </table>
                    </center>
                </html:form>
            </tiles:put>
        </tiles:insert>
    </tiles:put>
    <tiles:put name="finalJavascript" type="string">
        <script type="text/javascript" language="JavaScript">
            
            /*Se cargan los applets de firma*/
            if (CLIENTE_FIRMA=="FLEXIA"){                
               setTimeout('cargarAppletFirmaSLE()',3000);
            } else if (CLIENTE_FIRMA=="AFIRMA"){
                cargarAppletInstalador();
                cargarAppletFirma('COMPLETA');
            }else if (CLIENTE_FIRMA=="ASF"){
                 inicializarClienteFirma();
            }else if(CLIENTE_FIRMA=="VIAFIRMA"){
                inicializarClienteViaFirma();
            }//if CLIENTE_FIRMA
            
            waituntilready();
            
            <logic:messagesPresent message="true">
            <html:messages message="true" id="msg">
            jsp_alerta('A','<bean:write name="msg"/>');
            </html:messages>
            </logic:messagesPresent>
        </script>
    </tiles:put>
</tiles:insert>