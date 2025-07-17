<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@ taglib uri="http://jakarta.apache.org/taglibs/log-1.0" prefix="log" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="string" %>

 <meta http-equiv="X-UA-Compatible" content="IE=10"/>
<tiles:insert page="/jsp/portafirmas/tpls/WindowTemplate.jsp" flush="true">
	<tiles:put name="title" type="string">
        <fmt:message key='Portafirmas.RechazoDocumentoPortafirmasForm.title'/>
    </tiles:put>

	<tiles:put name="head-content" type="string">
    		
    		
    		<script type="text/javascript" language="JavaScript" src="<c:url value='/scripts/firma.js'/>"></script>
            <script type="text/javascript" language="JavaScript">
	            <%@ include file="jsEtiquetaEstadoFirma.jsp" %>
	            <%@ include file="jsCerrarFirmaDocumentoPortafirmas.jsp" %>
	            <%@ include file="jsGetUrlDocumentoPortafirmas.jsp" %>
                <c:set var="JS_DEBUG_LEVEL" value="0" /> 
                function btnRechazar_click(theSender, theForm) {
                    <c:if test="${JS_DEBUG_LEVEL >= 90}">
                    alert("RechazoDocumentoPortafirma.btnRechazar_click() BEGIN");
                    </c:if>
                    
                    if ( (theSender) && (theForm) ) {
                        pleaseWait1("on",this);
                        theForm.submit();
                    }
                    <c:if test="${JS_DEBUG_LEVEL >= 90}">
                    alert("RechazoDocumentoPortafirma.btnRechazar_click() END");
                    </c:if>
                }
                
                
                
                
                function getXMLHttpRequest(){
                    var aVersions = [ "MSXML2.XMLHttp.5.0",
                        "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
                        "MSXML2.XMLHttp","Microsoft.XMLHttp"
                    ];

                    if (window.XMLHttpRequest){
                            // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
                            return new XMLHttpRequest();
                    }else if (window.ActiveXObject){
                        // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
                        for (var i = 0; i < aVersions.length; i++) {
                                try {
                                    var oXmlHttp = new ActiveXObject(aVersions[i]);
                                    return oXmlHttp;
                                }catch (error) {
                                //no necesitamos hacer nada especial
                                }
                        }
                    }
                }// getXMLHttpRequest
            
            
            
                function localizarExpedienteDeDocumentoExterno(codDocumento) {   
           
                    var ajax = getXMLHttpRequest();
                    var salida = "";

                    if(ajax!=null){

                      var url = "<%= request.getContextPath() %>" + "/portafirmas/documentoportafirmas/VerificarNumeroExpedienteDocumentoExterno.do";
                      var parametros = "&codDocumento=" + codDocumento;
                      ajax.open("POST",url,false);
                      ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
                      ajax.setRequestHeader("Accept", "text/html, application/xml, text/plain");
                      ajax.send(parametros);
                      try{
                          if (ajax.readyState==4 && ajax.status==200){            
                              var text = ajax.responseText;											                      
                              if(text!=null && text!="")
                                 salida = text;
                          }
                      }catch(err){
                          alert("Error al tratar del ocalizar expediente asociado a un documento externo: " + err.description);
                      }
                    }             
                    return salida;
                }
                
                
                 function btnDocumentacionAnexaExpediente_click(theSender, theForm) {                                 
                    if ( (theSender) && (theForm) ) {
                       
                       
                       var codDocumento = document.forms[0].idNumeroDocumento.value;
                       var numeroExpediente = localizarExpedienteDeDocumentoExterno(codDocumento);
                       
                       if(numeroExpediente==null || numeroExpediente==""){
                            jsp_alerta('A','<fmt:message key="Error.documento.externo.noesta.asociado.expediente"/>');

                        }else{
                            var url = "<c:url value='/portafirmas/documentoportafirmas/ListadoDocumentoExpedientePortafirmas.do'/>" + "?numExpediente=" + numeroExpediente;                        
                            var datos = new Array();
                            abrirXanelaAuxiliar(url,datos,
                                'width=973,height=685,status=no',function(resp){});
                        }
                    }
                }
                
                
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
                <fmt:message key='Portafirmas.RechazoDocumentoPortafirmasForm.title'/>
            </tiles:put>
			<tiles:put name="altia-app-form-content" type="string">
                <html:form action="/portafirmas/documentoportafirmas/ProcessRechazoOtroDocumentoPortafirmas.do">
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

                    <%-- Campos del documento 
                    <html:hidden property="idMunicipio"/>
                    <html:hidden property="idProcedimiento"/>
                    <html:hidden property="idEjercicio"/>
                    <html:hidden property="idNumeroExpediente"/>
                    <html:hidden property="idTramite"/>
                    <html:hidden property="idOcurrenciaTramite"/>
                    --%>
                    <html:hidden property="idNumeroDocumento"/>
                    <html:hidden property="usuarioFirmante"/>
                    <html:hidden property="descripcionDocumento"/>
                    <html:hidden property="tipoDocumento"/>


                  <br/>
                  <br/>
                  <center>
                    <table width="90%" border="0" align="center" >
                     <tr>
                        <td align="center">
                            <table>
                                <tr>
                                    <td class="etiqueta"><fmt:message key="Portafirmas.RechazoDocumentoPortafirmasForm.MotivoRechazo"/>:</td>
                                    <td><html:textarea property="observaciones" styleClass="textareaTexto" rows="5" cols="60"></html:textarea></td>
                                </tr>
                            </table>
                        </td>
                     </tr>
                     
                     <tr>
                        <td align="center">
                            &nbsp;
                        </td>
                     </tr>
                     
                     <tr>
                        <td align="center">
                            <input type="button" class="botonGeneral" name="btnRechazar" value="<fmt:message key='Buttons.reject'/>" onclick="btnRechazar_click(this,this.form)">
                            <input type="button" class="botonMasLargo" name="btnVerDocExpediente" value="<fmt:message key='Portafirmas.VerDocumentacionAnexa.Expediente'/>" onclick="btnDocumentacionAnexaExpediente_click(this,this.form)">
                            <input type="button" class="botonGeneral" name="btnClose" value="<fmt:message key='Buttons.cancel'/>" onclick="btnCloseRechazoOtroDocumento_click(this,this.form)">
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
			pleaseWait1("off",this);
            <logic:messagesPresent message="true">
                <html:messages message="true" id="msg">
                    jsp_alerta('A','<bean:write name="msg"/>');
                </html:messages>
            </logic:messagesPresent>
		</script>
	</tiles:put>
</tiles:insert>

