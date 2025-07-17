<%@ page import="java.util.Enumeration"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@ taglib uri="http://jakarta.apache.org/taglibs/log-1.0" prefix="log" %>

 <meta http-equiv="X-UA-Compatible" content="IE=10"/>
<tiles:insert page="/jsp/portafirmas/tpls/WindowTemplate.jsp" flush="true">
<tiles:put name="title" type="string">
    <fmt:message key='Portafirmas.DelegacionFirmaForm.title'/>
</tiles:put>

<tiles:put name="head-content" type="string">


    
    
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
    <script type="text/javascript" language="JavaScript" src="<c:url value='/scripts/calendario.js'/>"></script>
    
    <script type="text/javascript" language="JavaScript">
        
        var popup = null;
        function setPopUpResult( value, label ) {
            if ( (document.DelegacionFirmaForm) && (value) ) {
                document.DelegacionFirmaForm.usuarioDelegado.value=value;
                document.DelegacionFirmaForm.nombreUsuarioDelegado.value=label;
                document.getElementById("lblNombreUsuarioDelegado").value=label;
                
            }
            if (popup!=null) {
                popup.close();
                popup=null;
            }
        }

        function inicializar() {
        
            if ( (document.DelegacionFirmaForm) && (parseInt(document.DelegacionFirmaForm.usuarioDelegado.value,10)<=0) ) {
                document.DelegacionFirmaForm.usuarioDelegado.value='';                                
            }
            
            if ( (document.DelegacionFirmaForm) && (document.DelegacionFirmaForm.actionCode.value=='C') ) {
                document.DelegacionFirmaForm.btnDelete.disabled=true;
                var botones = document.getElementsByName("btnDelete");
                if(botones!=null && botones.length==1){
                        botones[0].className = "botonGeneralDeshabilitado";
                }
            }
            
            if ( (document.DelegacionFirmaForm) && (document.DelegacionFirmaForm.nombreUsuarioDelegado.value) && (document.DelegacionFirmaForm.nombreUsuarioDelegado.value!='') ) {
                document.getElementById("lblNombreUsuarioDelegado").value = document.DelegacionFirmaForm.nombreUsuarioDelegado.value;
            }
            
            <logic:messagesPresent message="true">
                <html:messages message="true" id="msg">
                    jsp_alerta('A','<bean:write name="msg"/>');
                </html:messages>
            </logic:messagesPresent>
        }

        function lanzarPopUpModalConCristal(url, theHeight, theWidth, scrollable, resizable) {
            abrirXanelaAuxiliar(url,null,'width='+theWidth+',height='+theHeight+',status=no,resizable='+resizable+
            ',scrollbars='+scrollable, function(){});
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
            <fmt:message key='Portafirmas.DelegacionFirmaForm.title'/>
        </tiles:put>
        
        <tiles:put name="altia-app-form-content" type="string">
            <html:form action="/portafirmas/delegacionfirma/ProcessDelegacionFirma.do">
                <html:hidden property="actionCode"/>
                <html:hidden property="versionNumber"/>
                <html:hidden property="nombreUsuarioDelegado"/>
                <table>
                    <tr>
                        <td class="etiqueta"><fmt:message key='Portafirmas.DelegacionFirmaForm.UsuarioDelegado'/>:</td>
                        <td nowrap="true">
                            <span nowrap="true">
                        <html:text property="usuarioDelegado" size="2" maxlength="3" styleClass="inputTextoObligatorio" readonly="true"/>
                        <input id="lblNombreUsuarioDelegado" size="35" type="text" class="inputTextoObligatorio" readonly="true" id="prueba">
                        <span class="fa fa-search" aria-hidden="true" name="btnUsuarioDelegado" id="btnUsuarioDelegado"  onclick="lanzarPopUpModalConCristal( '<c:url value="/portafirmas/delegacionfirma/PopUpSeleccionUsuario.do"/>', 500, 800 ,'no', 'no' );" style="cursor:pointer">
                        <logic:messagesPresent property="usuarioDelegado">
                            <img src="<c:url value='/images/window/warningIcon.gif'/>" height="14" width="14" alt="<html:errors bundle="PortafirmasStrutsErrorMessages" property='usuarioDelegado'/>" onclick="jsp_alerta('A',this.alt,APP_TITLE);">
                        </logic:messagesPresent>
                    </span>
                        </td>
                    </tr>
                    <tr>
                        <td class="etiqueta"><fmt:message key='Portafirmas.DelegacionFirmaForm.FechaDesde'/>:</td>
                        <td nowrap="true" class="columnP">
                            <html:text property="fechaDesde" size="11" maxlength="10" styleClass="inputTxtFecha"/>
                            <span class="fa fa-calendar" aria-hidden="true"  name="btnCalendarDesde" id="btnCalendarDesde" onclick="
                    showCalendar('DelegacionFirmaForm','fechaDesde',null,null,null,'','btnCalendarDesde','',null,null,null,null,null,null,null, '',event);" style="cursor:pointer"></span>
                            <logic:messagesPresent property="fechaDesde">
                                <img src="<c:url value='/images/window/warningIcon.gif'/>" height="14" width="14" alt="<html:errors bundle="PortafirmasStrutsErrorMessages" property='fechaDesde'/>" onclick="jsp_alerta('A',this.alt,APP_TITLE);">
                            </logic:messagesPresent>
                        </td>
                    </tr>
                    <tr>
                        <td class="etiqueta"><fmt:message key='Portafirmas.DelegacionFirmaForm.FechaHasta'/>:</td>
                        <td nowrap="true" class="columnP">
                            <html:text property="fechaHasta"  size="11" maxlength="10" styleClass="inputTxtFecha"/>
                            <span class="fa fa-calendar" aria-hidden="true"  name="btnCalendarHasta" id="btnCalendarHasta" onclick="
                    showCalendar('DelegacionFirmaForm','fechaHasta',null,null,null,'','btnCalendarHasta','',null,null,null,null,null,null,null, '',event);" style="cursor:pointer"></span>
                            <logic:messagesPresent property="fechaHasta">
                                <img src="<c:url value='/images/window/warningIcon.gif'/>" height="14" width="14" alt="<html:errors bundle="PortafirmasStrutsErrorMessages" property='fechaHasta'/>" onclick="jsp_alerta('A',this.alt,APP_TITLE);">
                            </logic:messagesPresent>
                        </td>
                    </tr>
                </table>
                <div class="botoneraPrincipal">
                     <input type="button" name="btnSubmit" class="botonGeneral" value="<fmt:message key='Buttons.save'/>" onclick="pleaseWait1("on",this);this.form.submit();"> 
                    <input type="button" name="btnLimpiar" class="botonGeneral" value="<fmt:message key='Buttons.reset'/>" onclick="this.form.reset();setPopUpResult(-1,'');inicializar();">
                    <input type="button" name="btnDelete" class="botonGeneral" value="<fmt:message key='Buttons.revoke'/>" onclick="pleaseWait1("on",this);this.form.actionCode.value='D';this.form.submit();">
                    <input type="button" name="btnCancel" class="botonGeneral" value="<fmt:message key='Buttons.close'/>" onclick="pleaseWait1("on",this);document.location.href='<c:url value="/jsp/portafirmas/main.jsp"/>';">
                </div>
            </html:form>
        </tiles:put>
    </tiles:insert>
</tiles:put>
<tiles:put name="finalJavascript" type="string">
    <script type="text/javascript" language="JavaScript">
        pleaseWait1("off",this);

        //Usado para el calendario
var coordx=0;
var coordy=0;


var navegador = navigator.appName
if (navegador != "Microsoft Internet Explorer") {
    window.addEventListener('mousemove', function(e) {
        coordx = e.clientX;
        coordy = e.clientY;
    }, true);
}

document.onmouseup = checkKeys;

function checkKeysLocal(evento,tecla) {
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else{
        teclaAuxiliar = evento.which;
    }


	if (teclaAuxiliar == 1){
           if(IsCalendarVisible) replegarCalendario(coordx,coordy);
        }
        if (teclaAuxiliar == 9){
            if(IsCalendarVisible) hideCalendar();
        }

}
    </script>
</tiles:put>
</tiles:insert>
