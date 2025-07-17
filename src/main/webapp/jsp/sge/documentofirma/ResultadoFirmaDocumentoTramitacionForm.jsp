<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@ taglib uri="http://jakarta.apache.org/taglibs/log-1.0" prefix="log" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="string" %>


<tiles:insert page="/jsp/portafirmas/tpls/WindowTemplate.jsp" flush="true">
    <tiles:put name="title" type="string">
        <fmt:message key='Sge.FirmaDocumentoTramitacionForm.title'/>
    </tiles:put>

    <tiles:put name="head-content" type="string">
        <script type="text/javascript">            
            <jsp:include page="/jsp/portafirmas/documentoportafirmas/jsEtiquetaEstadoFirma.jsp" />
            <jsp:include page="/jsp/portafirmas/documentoportafirmas/jsCerrarFirmaDocumentoPortafirmas.jsp" />
            <jsp:include page="/jsp/portafirmas/documentoportafirmas/jsGetUrlDocumentoPortafirmas.jsp" />

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
                    <center>
                        <table width="90%" border="0" align="center" >
                            <tr>
                                <td colspan="2" align="center" style="font-size:11px;font-weight: bold;">
                                    <%-- Mensajes --%>
                                    <logic:messagesPresent message="true">
                                        <span class="etiqueta" align="center">
                                        <html:messages message="true" id="msg">
                                            <bean:write name="msg"/>.
                                        </html:messages>
                                        </span>
                                    </logic:messagesPresent>
                                    <%-- Mensajes (fin) --%>
                            
                                </td>
                            </tr>
                            <tr>
                               <td colspan="2" align="center">
                                 <input type="button" class="botonGeneral" name="btnClose" value="<fmt:message key='Buttons.close'/>" onclick="btnClose_click(this,this.form)">
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
                <logic:messagesNotPresent message="true">
                    waituntilready();
                </logic:messagesNotPresent>
                <logic:messagesPresent message="true">
                    pleaseWait1("off",this);
                </logic:messagesPresent>
            </script>
	</tiles:put>
   
</tiles:insert>