<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@ taglib uri="http://jakarta.apache.org/taglibs/log-1.0" prefix="log" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="string" %>

<%@ page import="es.altia.util.struts.StrutsUtilOperations" %>
<%@ page import="es.altia.common.service.config.*" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>

<%
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String hVirtual = m_Config.getString("hostVirtual");
    String host="";

    Config m_Docus = ConfigServiceHelper.getConfig("Portafirmas");
    String organizacion = "0";
    String clienteFirma = m_Docus.getString(organizacion+"/PluginPortafirmas");

    // Para el cliente de autofirma se necesita el algoritmo
    String autoFirmaAlgoritmo = "";
    if ("AUTOFIRMA".equals(clienteFirma)) {
        autoFirmaAlgoritmo = m_Docus.getString("PluginPortafirmas/AUTOFIRMA/algoritmoFirma");
    }

    int apl = 4;
    int idioma = 1;
    if (session!=null){
        UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
            apl = usuario.getAppCod();
            idioma = usuario.getIdioma();
        }
    }

    try {
        // Si se utiliza la anterior, hay que definir en el fichero de hosts, la propiedad que se defina en el common.properties
        String protocolo = StrutsUtilOperations.getProtocol(request);
        if (hVirtual.equals("")) host = protocolo + "://" + request.getHeader("host") + request.getContextPath() + "/sge/documentofirma/ProcessFirmaDocumentoTramitacion.do";
        else host = hVirtual + request.getContextPath() + "/sge/documentofirma/ProcessFirmaDocumentoTramitacion.do";

    } catch (Exception e) {
        e.printStackTrace();
    }
    
%>
<jsp:useBean id="descriptor" scope="page" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<c:set var="jspClienteFirma"><%=clienteFirma%></c:set>
<c:set var="hostVirtual"><%=hVirtual%></c:set>

<meta http-equiv="X-UA-Compatible" content="IE=9"/>
<tiles:insert page="/jsp/portafirmas/tpls/WindowTemplate.jsp" flush="true">
    <tiles:put name="title" type="string">
        <fmt:message key='Sge.FirmaDocumentoTramitacionForm.title'/>
    </tiles:put>

    <tiles:put name="head-content" type="string">
        <c:choose>
            <c:when test = "${jspClienteFirma eq 'AUTOFIRMA'}">
                <%@ include file="/jsp/portafirmas/resources/autofirma/functionsJsAutofirma.jspf" %>
            </c:when>
        </c:choose>

        <script type="text/javascript">            
            <jsp:include page="/jsp/portafirmas/documentoportafirmas/jsEtiquetaEstadoFirma.jsp" />
            <jsp:include page="/jsp/portafirmas/documentoportafirmas/jsCerrarFirmaDocumentoPortafirmas.jsp" />
            <jsp:include page="/jsp/portafirmas/documentoportafirmas/jsGetUrlDocumentoPortafirmas.jsp" />

            // Referencia a la ventana principal
            var THAT = this;

            /*Se cargan los applets de firma*/
            function cargar() {
                // Establecer configuracion propia del plugin de firma
                var funcionCargaApplet = null;
                var argsCargaApplet = null;
                    
                if(CLIENTE_FIRMA=="AUTOFIRMA") {
                    var funcionCargaApplet = inicializarClienteAutofirma;
                    var argsCargaApplet = '<c:out value="${hostVirtual}"/>';
                }
                    
                // Carga del applet
                try {
                    $.when(funcionCargaApplet(argsCargaApplet)).then(
                           successCargaApplet,
                           errorCargaApplet
                    );
                } catch (error) {
                    errorCargaApplet();
                }
            }
            
            function successCargaApplet(data) {
                var theForm = document.DocumentoFirmaForm;
                theForm.action='<%=host%>';
                theForm.btnFirmar.disabled = false;
                pleaseWait1("off", THAT);
            }

            function errorCargaApplet(error) {
                mostrarError(error, '<%=descriptor.getDescripcion("ErrCargarApplet")%>', APP_TITLE);
                btnClose_click(THAT,THAT.form);
            }

            function btnFirmar_click(theSender, theForm) {
                var ficheroFirmado;

                if ( (theSender) && (theForm) ) {
                    theForm.action='<%=host%>';
                    pleaseWait1("on", THAT);

                    // Construimos la URL de descarga del fichero a firmar
                    var url = "";
                    var tipoDocumentoFirmar = "<%=(String)request.getAttribute("tipoDocumentoFirmar")%>";
                    if (tipoDocumentoFirmar==0){
                        url = getUrlForDownloadDocument(
                                theForm.idMunicipio.value,
                                theForm.idNumeroExpediente.value,
                                theForm.idTramite.value,
                                theForm.idOcurrenciaTramite.value,
                                theForm.idNumeroDocumento.value,
                                false);
                    } else if (tipoDocumentoFirmar==1) {
                        url = getUrlForDownloadRelationDocument(theForm.idMunicipio.value,
                                theForm.idNumeroExpediente.value,
                                theForm.idTramite.value,
                                theForm.idOcurrenciaTramite.value,
                                theForm.idNumeroDocumento.value,
                                "**buscarNombreDocRelacion**",
                                theForm.editorTexto.value);
                    }

                    // Establecemos la funcion de firma de fichero y los argumentos a pasarle segun
                    // el cliente establecido en Portafirmas.properties
                    if(CLIENTE_FIRMA=="AUTOFIRMA") {
                        var funcionFirmaDocumento = firmarFicheroClienteAutofirmaDownloadRemote;
                        var formatoFirma = determinarFormatoAutoFirmaPorMimeType(theForm.tipoMime.value);

                        var args = {
                            url: url,
                            algoritmoFirma: '<%= autoFirmaAlgoritmo %>',
                            formatoFirma: formatoFirma
                        };
                    }

                    // Firma de fichero
                    try {
                        $.when(funcionFirmaDocumento(args)).then(
                            successFirmaDocumento,
                            errorFirmaDocumento
                        );
                    } catch (error) {
                        errorFirmaDocumento();
                    }

                    // Callback success de $.when(funcionFirmaDocumento(args)).then(...)
                    function successFirmaDocumento(data) {
                        ficheroFirmado = data;

                        if (ficheroFirmado) {
                            jsp_alerta('A', '<%=descriptor.getDescripcion("msjDocLocalFirmadoOk")%>', APP_TITLE);
                        theForm.submit();
                    } else {
                            mostrarError(null, '<%=descriptor.getDescripcion("msjDocLocalNoFirmado")%>', APP_TITLE);
                            btnClose_click(theSender,theForm);
                        }
                    }

                    // Callback error de $.when(funcionFirmaDocumento(args)).then(...)
                    function errorFirmaDocumento(error) {
                        mostrarError(error, '<%=descriptor.getDescripcion("msjErrorFirmarDocumento")%>', APP_TITLE);
                        btnClose_click(theSender,theForm);
                    }
                }//if ( (theSender) && (theForm) ) 
            }//btnFirmar_click

            // Muestra un error y vuelve a la pantalla principal
            function mostrarError(error, msjDefecto, titulo) {
                var msjError = error || msjDefecto;

                jsp_alerta('A', msjError, titulo);
                pleaseWait1("off", THAT);
            }

            function btnClose_click( theSender, theForm ) {                
                self.parent.opener.retornoXanelaAuxiliar();
            }        
        </script>
    </tiles:put>
    <tiles:put name="content" type="string">
        <tiles:insert page="/jsp/portafirmas/tpls/AltiaPopUpTemplate.jsp" flush="false">
            <tiles:put name="altia-app-form-title" type="string">
                <fmt:message key='Sge.FirmaDocumentoTramitacionForm.title'/>
            </tiles:put>
            <tiles:put name="altia-app-form-content" type="string">
                <html:form action="/sge/documentofirma/ProcessFirmaDocumentoTramitacion.do">
                    <%-- Campos del documento --%>
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
                    <html:hidden property="firma"/>
                    <html:hidden property="hashB64"/>
                    <html:hidden property="tipoMime"/>
                    
                    <%-- ORLY? --%>
                    
                    <%-- Campos del documento --%>
                    <html:hidden property="usuario"/>
                    <html:hidden property="nombreUsuarioFirmante"/>
                    <html:hidden property="estadoFirma"/>
                    <html:hidden property="fechaFirma"/>
                    <html:hidden property="observaciones"/>
                    <html:hidden property="firmaBase64"/>
                    <html:hidden property="editorTexto"/>

                    <center>
                        <table width="90%" border="0" align="center" >
                           <tr>
                               <td colspan="2" align="center" style="font-size:11px;font-weight: bold;">
                                   <fmt:message key="Portafirmas.FirmaDocumentoPortafirmasForm.ConfirmacionFirma"/>
                               </td>
                           </tr>                    
                           <tr>
                               <td colspan="2" align="center">
                                    &nbsp;
                               </td>
                           </tr>
                           <tr>
                                <td colspan="2" align="center">
                                    <input type="button" class="botonGeneral" name="btnFirmar" value="<fmt:message key='Buttons.sign'/>" onclick="btnFirmar_click(this,this.form)" DISABLED>
                                    <input type="button" class="botonGeneral" name="btnClose" value="<fmt:message key='Buttons.cancel'/>" onclick="btnClose_click(this,this.form)">
                                </td>
                            </tr>
                            <logic:messagesPresent message="true">
                                <tr>
                                    <td colspan="2" align="center">
                                        <input type="button" class="boton" name="btnClose" value="<fmt:message key='Buttons.cancel'/>" onclick="btnClose_click(this,this.form)">
                                    </td>
                                </tr>
                            </logic:messagesPresent>
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
            /*Se cargan los applets de firma*/
            $(document).ready(cargar);
        
           <logic:messagesPresent message="true">
              <html:messages message="true" id="msg">
                    jsp_alerta('A','<bean:write name="msg"/>');
              </html:messages>
           </logic:messagesPresent>
           
       </script>
    </tiles:put>
</tiles:insert>
