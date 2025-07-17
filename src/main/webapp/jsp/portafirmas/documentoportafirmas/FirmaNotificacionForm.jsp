<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@ taglib uri="http://jakarta.apache.org/taglibs/log-1.0" prefix="log" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="string" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<%
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String hVirtual = m_Config.getString("hostVirtual");
    String host="";

    Config m_Docus = ConfigServiceHelper.getConfig("Portafirmas");
    String organizacion = "0";
    String clienteFirma = m_Docus.getString(organizacion+"/PluginPortafirmas");

    // Para el cliente de autofirma se necesita el algoritmo y el formato de la firma
    String autoFirmaAlgoritmo = "";
    String autoFirmaDesfaseEnvio = "";
    if ("AUTOFIRMA".equals(clienteFirma)) {
        autoFirmaAlgoritmo = m_Docus.getString("PluginPortafirmas/AUTOFIRMA/algoritmoFirma");
        autoFirmaDesfaseEnvio = m_Docus.getString("PluginPortafirmas/AUTOFIRMA/desfaseEnvioServidor");
    }

    int apl = 11;
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
        if (hVirtual.equals("")) {
            hVirtual = protocolo + "://" + request.getHeader("host");
        }
        host = hVirtual + request.getContextPath() + "/sge/documentofirma/ProcessFirmaNotificacion.do";
    } catch (Exception e) {
        e.printStackTrace();
    }//try-catch
%>

<jsp:useBean id="descriptor" scope="page" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<c:set var="jspClienteFirma"><%=clienteFirma%></c:set>
<c:set var="hostVirtual"><%=hVirtual%></c:set>

 <meta http-equiv="X-UA-Compatible" content="IE=9"/>
    <tiles:insert page="/jsp/portafirmas/tpls/WindowTemplate.jsp" flush="true">
        <tiles:put name="title" type="string">
            <fmt:message key='Portafirmas.FirmaDocumentoPortafirmasForm.title'/>
        </tiles:put>

        <tiles:put name="head-content" type="string">
            <c:choose>
                <c:when test = "${jspClienteFirma eq 'AUTOFIRMA'}">
                    <%@ include file="/jsp/portafirmas/resources/autofirma/functionsJsAutofirma.jspf" %>
                </c:when>
            </c:choose>

            <script type="text/javascript">
                <%@ include file="/jsp/portafirmas/documentoportafirmas/jsEtiquetaEstadoFirma.jsp" %>
                <%@ include file="/jsp/portafirmas/documentoportafirmas/jsCerrarFirmaDocumentoPortafirmas.jsp" %>
                <%@ include file="/jsp/portafirmas/documentoportafirmas/jsGetUrlDocumentoPortafirmas.jsp" %>

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
                    var theForm = document.DocumentoExternoNotificacionForm;
                    theForm.action='<%=host%>';
                    theForm.btnFirmar.disabled = false;
                    pleaseWait1("off", THAT);
                }
                
                function errorCargaApplet(error) {
                    mostrarError(error, '<%=descriptor.getDescripcion("ErrCargarApplet")%>', APP_TITLE);
                    cerrar();
                }

                function btnFirmar_click(theSender, theForm) {
                    document.forms[0].action='<%=host%>';
                    var ficheroFirmado;

                    if ( (theSender) && (theForm) ) {
                        pleaseWait1("on", THAT);

                        // Construimos la URL de descarga del fichero a firmar
                        var url = getUrlForDownloadNotificacion(theForm.codNotificacion.value);

                        // Establecemos la funcion de firma de fichero y los argumentos a pasarle segun
                        // el cliente establecido en Portafirmas.properties
                        if(CLIENTE_FIRMA=="AUTOFIRMA") {
                            var funcionFirmaNotificacion = firmarFicheroClienteAutofirmaDownloadRemote;
                            var formatoFirma = determinarFormatoAutoFirmaPorMimeType(document.forms[0].tipoMime.value);
                            
                            var args = {
                                url: url,
                                algoritmoFirma: '<%= autoFirmaAlgoritmo %>',
                                formatoFirma: formatoFirma
                            };
                        }

                        // Firma de fichero
                        try {
                            $.when(funcionFirmaNotificacion(args)).then(
                                successFirmaNotificacion,
                                errorFirmaNotificacion
                            );
                        } catch (error) {
                            errorFirmaNotificacion();
                        }

                        // Callback success de $.when(funcionFirmaNotificacion(args)).then(...)
                        function successFirmaNotificacion(data) {
                            ficheroFirmado = data;

                            if (ficheroFirmado) {
                                var desfaseSegundos = <%=autoFirmaDesfaseEnvio%> * 1000;
                                setTimeout(function() {
                                    jsp_alerta('A', '<%=descriptor.getDescripcion("msjDocLocalFirmadoOk")%>', APP_TITLE);
                                    theForm.submit();
                                }, desfaseSegundos);
                        } else {
                                mostrarError(null, '<%=descriptor.getDescripcion("msjDocLocalNoFirmado")%>', APP_TITLE);
                                btnClose_click(theSender,theForm);
                            }
                        }

                        // Callback error de $.when(funcionFirmaNotificacion(args)).then(...)
                        function errorFirmaNotificacion(error) {
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

                // Volver a la pantalla principal
                function cerrar(objeto,theForm){
                    var salida = new Array();
                    salida[0] = "CANCEL_FIRMA";
                    
                    volverPantallaPrincipal(salida);
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
                    <html:hidden property="firma"/>
                    <html:hidden property="hashB64"/>
                    <html:hidden property="tipoMime"/>

                    <center>
                        <table width="90%" border="0" align="center" >
                            <tr>
                                <td colspan="2" align="center" style="font-size:11px;font-weight: bold;">
                                    <fmt:message key="Portafirmas.FirmaDocumentoPortafirmasForm.ConfirmacionFirma"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" align="center">
                                    <input type="button" class="boton" name="btnFirmar" value="<fmt:message key='Buttons.sign'/>" onclick="btnFirmar_click(this,this.form)" DISABLED>
                                    <input type="button" class="boton" name="btnClose" value="<fmt:message key='Buttons.cancel'/>" onclick="cerrar(this,this.form)">
                                </td>
                           </tr>
                           <logic:messagesPresent message="true">
                                <tr>
                                    <td colspan="2" align="center">
                                        <input type="button" class="boton" name="btnClose" value="<fmt:message key='Buttons.cancel'/>" onclick="cerrar(this,this.form)">
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
                $(document).ready(cargar);

                <logic:messagesPresent message="true">
                <html:messages message="true" id="msg">
                jsp_alerta('A','<bean:write name="msg"/>');
                </html:messages>
                </logic:messagesPresent>
            </script>
    </tiles:put>
</tiles:insert>