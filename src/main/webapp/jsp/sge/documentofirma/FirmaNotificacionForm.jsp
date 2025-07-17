<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<tiles:insert page="/jsp/portafirmas/tpls/WindowTemplate.jsp" flush="true">
	<tiles:put name="title" type="string">
        <fmt:message key='Sge.FirmaDocumentoTramitacionForm.title'/>
    </tiles:put>

	<tiles:put name="head-content" type="string">
    		
    		
    		<script type="text/javascript" language="JavaScript" src="<c:url value='/scripts/firma.js'/>"></script>
            <script type="text/javascript" language="JavaScript">                
                var tipoDocumentoFirmar = "<%=(String)request.getAttribute("tipoDocumentoFirmar")%>";
                var nombreDocumentoFirmar = "<%=(String)request.getAttribute("nombreDocumentoFirmar")%>";
                var textoFirmar = "<%=(String)request.getAttribute("textoFirmar")%>";


                <c:set var="JS_DEBUG_LEVEL" value="0" />
                    
<logic:messagesNotPresent message="true">
                    
                    <%--
	            <jsp:include page="/jsp/portafirmas/documentoportafirmas/jsGetUrlDocumentoPortafirmas.jsp" />
                    --%>
                function btnFirmar_click(theSender, theForm) {
                    <c:if test="${JS_DEBUG_LEVEL >= 90}">
                    alert("FirmaDocumentoTramitacion.btnFirmar_click() BEGIN");
                    </c:if>
                    if ( (theSender) && (theForm) ) {
                        var url = "";

                        


                        if(tipoDocumentoFirmar==2) var ficheroFirmado = firmarTexto(textoFirmar,theForm);


                        //firmarURL(url, modoOperacionCerts, idFirmante, theForm, dnCA);
                        if (theForm.appletFirmaHayError.value == "N") {

                            
                            
                            jsp_alerta('A','<fmt:message key='Sge.FirmaNotificacionForm.FirmadoCorrectamente'/>',APP_TITLE);
                            
                             document.forms[0].codigoNotificacion.value='<%=(Integer) session.getAttribute("codNotificacion")%>';
                             document.forms[0].firma.value=theForm.firma.value;
                             document.forms[0].opcion.value="guardaFirma";
                             document.forms[0].target="oculto";
                             document.forms[0].action="<c:url value='/Notificacion.do'/>";
                             document.forms[0].submit();
                             window.close();

                        } else {
                            <c:if test="${JS_DEBUG_LEVEL >= 40}">
                            alert("FirmaDocumentoTramitacion.btnFirmar_click() Después de firmar... HayError = "+theForm.appletFirmaHayError.value+"; CodigoError="+theForm.appletFirmaCodigoError.value+"; MensajeError="+theForm.appletFirmaMensajeError.value+";");
                            </c:if>
                            var cadenaError = "("+theForm.appletFirmaCodigoError.value+") " + theForm.appletFirmaMensajeError.value;
                            jsp_alerta('A',cadenaError,APP_TITLE);
                            btnClose_click(theSender,theForm);
                        }
                    }
                    <c:if test="${JS_DEBUG_LEVEL >= 90}">
                    alert("FirmaDocumentoTramitacion.btnFirmar_click() END");
                    </c:if>
                }
                var blnComprobacionAppletListo = false;

                function waituntilready() {
                    <c:if test="${JS_DEBUG_LEVEL >= 90}">
                    alert("FirmaDocumentoTramitacion.waituntilready() BEGIN");
                    </c:if>
                    try {
                        var applet = document.applets.APLFIRMANUEVO;
                        blnComprobacionAppletListo = (applet) && (applet.isActive()); <%--  && (applet.appletPreparado()); --%>
                    } catch (jsEx) {
                        blnComprobacionAppletListo = false;
                    }
                    if ( blnComprobacionAppletListo ) {
                        <c:if test="${JS_DEBUG_LEVEL >= 80}">
                        alert("FirmaDocumentoTramitacion.waituntilready() Applet preparado !!!");
                        </c:if>
                        var theForm = document.NotificacionForm;
                        theForm.btnFirmar.disabled = false;
                        pleaseWait1("off",this);
                    } else {
                        setTimeout('waituntilready()',3000);
                    }
                    <c:if test="${JS_DEBUG_LEVEL >= 90}">
                    alert("FirmaDocumentoTramitacion.waituntilready() END");
                    </c:if>
                }


</logic:messagesNotPresent>


                function btnClose_click( theSender, theForm ) {
                    <c:if test="${JS_DEBUG_LEVEL >= 90}">
                    alert("FirmaDocumentoTramitacion.btnClose_click() BEGIN");
                    </c:if>
                    <%--
                    if ( (opener) && (opener.cargaDocumentos) ) {
                        <c:if test="${JS_DEBUG_LEVEL >= 70}">
                        alert("FirmaDocumentoTramitacion.btnClose_click() Antes de refrescar lista documentos");
                        </c:if>
                        alert("FirmaDocumentoTramitacion.btnClose_click() Antes de refrescar lista documentos");
                        opener.top.mainFrame.cargaDocumentos();
                        <c:if test="${JS_DEBUG_LEVEL >= 70}">
                        alert("FirmaDocumentoTramitacion.btnClose_click() Después de refrescar lista documentos");
                        </c:if>
                    }
                    --%>
                    window.close();
                }
            </script>
	</tiles:put>
	<tiles:put name="content" type="string">
		<tiles:insert page="/jsp/portafirmas/tpls/AltiaPopUpTemplate.jsp" flush="false">
			<tiles:put name="altia-app-form-title" type="string">
                <fmt:message key='Sge.FirmaDocumentoTramitacionForm.title'/>
            </tiles:put>
			<tiles:put name="altia-app-form-content" type="string">
                <html:form action="/Notificacion.do">
                    <%-- Campos del documento --%>
                    <html:hidden property="codigoMunicipio"/>
                    <html:hidden property="codigoProcedimiento"/>
                    <html:hidden property="ejercicio"/>
                    <html:hidden property="numExpediente"/>
                    <html:hidden property="codigoTramite"/>
                    <html:hidden property="ocurrenciaTramite"/>
                  
                    <html:hidden property="codigoNotificacion"/>
                  


                    <%-- Campos para el applet de firma --%>
	                <input type="hidden" name="dirtFichero" value="/">
	                <input type="hidden" name="nameFichero" value="tempFile">

	                <input type="hidden" name="appletFirmaHayError" value="N"/>
	                <input type="hidden" name="appletFirmaCodigoError" value="0"/>
	                <input type="hidden" name="appletFirmaMensajeError" value=""/>
	                <input type="hidden" name="fichero" value="">
                        
                        <html:hidden  property="opcion" value=""/>
                    <html:hidden property="firma"/>


                  <br/>
                  <%-- Mensajes --%>
                  <logic:messagesPresent message="true">
                    <span class="etiqueta" align="center">
                    <html:messages message="true" id="msg">
                        <bean:write name="msg"/>.
                    </html:messages>
                    </span>
                  </logic:messagesPresent>
                  <%-- Mensajes (fin) --%>
                  <br/>
                  <center>
                    <table width="90%" border="0" align="center" >
<logic:messagesNotPresent message="true">
                     <tr>
                        <td align="center" style="font-size:11px;font-weight: bold;">
                            <fmt:message key="Sge.FirmaDocumentoTramitacionForm.ConfirmacionFirma"/>
                        </td>
                     </tr>
                     <tr>
                        <%--
                        <td align="center">
                            <applet code="AppletFirma.class" width="198" height="30" align="ABSMIDDLE" name="APLFIRMA" VIEWASTEXT>
                                <param NAME="ALTO" value="1">
                                <param NAME="ANCHO" value="1">
                                <param NAME="CFONDO" value="#E6E6E6">
                                <param NAME="CFRONTAL" value="#E6E6E6">
                                <param NAME="IDIOMA" value="ES">
                                <param NAME="CA" value="<fmt:message key='Sge.FirmaDocumentoTramitacionForm.CA'/>">
                                <param NAME="cabbase" VALUE="<c:url value='/jsp/portafirmas/resources/firma/appletFirma.cab'/>">
                            </applet>
                        </td>
                        --%>
                        <td align="center">
                              <%-- APPLET DE DESCARGA DEL DOCUMENTO A FIRMAR --%>
                            <applet id="APLDESCARGA"  code="es.altia.flexia.applet.AppletDescargaFichero.class"
                                    height="25" width="25" align="middle">
                                <PARAM name="IDIOMA" value="E">
                                 <PARAM name="java_codebase" value="<c:url value='/jsp/portafirmas/resources/firma'/>">
                                 <PARAM name="java_archive" value="AppletDescargaDocumento.jar">
                                 <PARAM name="type" value="application/x-java-applet;version=1.5">
                            </applet>
                            <%-- APPLET DE FIRMA --%>

                             <!--[if !IE]> -->
                                <object name="APLFIRMANUEVO"
                                      type="application/x-java-applet"
                                      width="25" height="25">
                                    <param name="code" value="es.altia.applets.firma.Applet.class">
                                    <param name="codebase" value="<c:url value='/jsp/portafirmas/resources/firma'/>">
                                    <param name="archive" value="AppletFirmaSLE.jar">
                                    <param name="scriptable" value="true">
                                    <param name="mayscript" value="false">
                                    <param name="BROWSER" value="Mozilla">
                               </object>
                               <!--<![endif]-->
                                    <!--[if IE]>
                                <object name="APLFIRMANUEVO"
                                            classid="clsid:CAFEEFAC-0016-0000-0000-ABCDEFFEDCBA"
                                            width="10" height="10">
                                        <param name="code" value="es.altia.applets.firma.Applet.class">
                                        <param name="codebase" value='<c:url value='/jsp/portafirmas/resources/firma'/>'>
                                        <param name="archive" value="AppletFirmaSLE.jar">
                                        <param name="scriptable" value="true">
                                        <param name="mayscript" value="false">
                                </object>
                                <![endif]-->

                            <%--
                            <OBJECT classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93" name="APLFIRMA"
                                    width="25" height="25" align="middle"
                                    codebase="http://java.sun.com/update/1.4.2/jinstall-1_4-windows-i586.cab#Version=1,4,0,0">
                                <PARAM name="java_code" value="applets.AppletFirma.class">
                                <PARAM name="java_codebase" value="<c:url value='/jsp/portafirmas/resources/firma'/>">
                                <PARAM name="java_archive" value="AppletFirma.jar">
                                <PARAM name="type" value="application/x-java-applet;version=1.4">
                                <PARAM name="IDIOMA" value="E">
                                <COMMENT>
                                <EMBED type="application/x-java-applet;version=1.4" name="APLFIRMA"
                                       width="25" height="25" align="middle"
                                       pluginspage="http://java.sun.com/products/plugin/" java_code="applets.AppletFirma.class"
                                       java_codebase="/jsp/portafirmas/resources/firma" java_archive="AppletFirma.jar"/>
                                <NOEMBED>
                                 Su explorador no ha podido ejecutar el applet java. Compruebe que tiene instalado el plug-in de java
                                </NOEMBED>
                                </COMMENT>
                            </OBJECT>
                            --%>
                        </td>
                     </tr>
                     <tr>
                        <td align="center">
                            <input type="button" class="botonGeneral" name="btnFirmar" value="<fmt:message key='Buttons.sign'/>" onclick="btnFirmar_click(this,this.form)" DISABLED>
                            <input type="button" class="botonGeneral" name="btnClose" value="<fmt:message key='Buttons.cancel'/>" onclick="btnClose_click(this,this.form)">
                        </td>
                     </tr>
                    </logic:messagesNotPresent>
                    <logic:messagesPresent message="true">
                     <tr>
                        <td align="center">
                            <input type="button" class="boton" name="btnClose" value="<fmt:message key='Buttons.close'/>" onclick="btnClose_click(this,this.form)">
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
            <logic:messagesNotPresent message="true">
                waituntilready();
            </logic:messagesNotPresent>
            <logic:messagesPresent message="true">
                pleaseWait1("off",this);
            </logic:messagesPresent>
        </script>
	</tiles:put>
</tiles:insert>
