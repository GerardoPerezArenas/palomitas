<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.ResourceBundle"%>

<c:set var="JS_DBG_INFO" value="false"/>

<tiles:insert page="/jsp/portafirmas/tpls/WindowTemplate.jsp" flush="true">
<tiles:put name="title" type="string">
    <fmt:message key='Sge.PlantillaFirmaForm.title'/>
</tiles:put>

<tiles:put name="head-content" type="string">
<%
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    
%>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<html:rewrite page='/scripts/jquery/jquery-1.9.1.min.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript">

var tipoFirmaAnterior = null;

function comprobarCambioTipoFirma() {
    var tipoFirma = document.getElementById("codFirma").value;
    
    if (tipoFirmaAnterior !== tipoFirma) {
        tipoFirmaAnterior = tipoFirma;
        updateFrmMantenimientoDocTramite();
    }
}
function updateFrmMantenimientoDocTramite() {
<c:if test="${JS_DBG_INFO}">
    alert('PlantillaFirmaForm.updateFrmMantenimientoDocTramite(): BEGIN');
</c:if>
    // Para un usuario
    var s1 = document.getElementById("codFirma").value;
   
    if ((s1 === 'O') || (s1 === 'U')) {
        $('#filaOtroUsuario').removeClass('hide');
        habilitarGeneral([document.getElementById("edtIdFirmanteDoc")]);
        document.getElementById("edtIdFirmanteDoc").readOnly=true;
        var btnUsuariosFirmantes = document.getElementById("btnUsuariosFirmantes");
        if (btnUsuariosFirmantes) {
            btnUsuariosFirmantes.disabled=false;
            btnUsuariosFirmantes.style.cursor = 'pointer';
        }
    } else {
        $('#filaOtroUsuario').addClass('hide');
        deshabilitarGeneral([document.getElementById("edtIdFirmanteDoc")]);
        var btnUsuariosFirmantes = document.getElementById("btnUsuariosFirmantes");
        if (btnUsuariosFirmantes) {
            btnUsuariosFirmantes.disabled=true;
            btnUsuariosFirmantes.style.cursor = 'default';
        }
        document.getElementById("edtIdFirmanteDoc").value='';
        document.getElementById("nombreUsuarioFirmante").value = '';
    }
    
    // Para flujos
    if (s1 === 'L') {
        $('#filaFlujo').removeClass('hide');
        habilitarGeneral([document.getElementById("edtIdFlujoFirmanteDoc")]);
        document.getElementById("edtIdFlujoFirmanteDoc").readOnly=true;
        var btnFlujoFirmanteDoc = document.getElementById("btnFlujoFirmanteDoc");
        if (btnFlujoFirmanteDoc) {
            btnFlujoFirmanteDoc.disabled=false;
            btnFlujoFirmanteDoc.style.cursor = 'pointer';
        }
    } else {
        $('#filaFlujo').addClass('hide');
        deshabilitarGeneral([document.getElementById("edtIdFlujoFirmanteDoc")]);
        var btnFlujoFirmanteDoc = document.getElementById("btnFlujoFirmanteDoc");
        if (btnFlujoFirmanteDoc) {
            btnFlujoFirmanteDoc.disabled=true;
            btnFlujoFirmanteDoc.style.cursor = 'default';
        }
        document.getElementById("edtIdFlujoFirmanteDoc").value='';
        document.getElementById("nombreFlujoFirmanteDoc").value = '';
    }
<c:if test="${JS_DBG_INFO}">
    alert('PlantillaFirmaForm.updateFrmMantenimientoDocTramite(): END');
</c:if>
}

function initDatosUsuarioFirmante(v, l) {
    if (v) document.PlantillaFirmaForm.edtIdFirmanteDoc.value = v;
    else document.PlantillaFirmaForm.edtIdFirmanteDoc.value = "";
    if (l) {
        document.getElementById("nombreUsuarioFirmante").value = l;
    } else {
        document.getElementById("nombreUsuarioFirmante").value = "";
    }
}

function initDatosFlujo(id, nombre) {
    document.PlantillaFirmaForm.edtIdFlujoFirmanteDoc.value = id || '';
    $('#nombreFlujoFirmanteDoc').val(nombre || '');
}

function setPopUpResultUsuario( value, label ) {
<c:if test="${JS_DBG_INFO}">
    alert('PlantillaFirmaForm.setPopUpResultUsuario(): BEGIN');
</c:if>
    if ( (document.PlantillaFirmaForm) && (value) ) {
        initDatosUsuarioFirmante(value, label);
    }
<c:if test="${JS_DBG_INFO}">
    alert('PlantillaFirmaForm.setPopUpResultUsuario(): END');
</c:if>
}

function setPopUpResultFlujo( id, valor ) {
<c:if test="${JS_DBG_INFO}">
    alert('PlantillaFirmaForm.setPopUpResultFlujo(): BEGIN');
</c:if>
    if ( (document.PlantillaFirmaForm) && (id) ) {
        initDatosFlujo(id, valor);
    }
<c:if test="${JS_DBG_INFO}">
    alert('PlantillaFirmaForm.setPopUpResultFlujo(): END');
</c:if>
}

function pulsarBuscarUsuario() {
    var source = '<c:url value="/portafirmas/delegacionfirma/PopUpSeleccionUsuario.do?opcion=ConComprobacionUsuarioPortafirmas"/>';
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,null,
        'width=700,height=550,status=no',function(rslt){
                    if (rslt != undefined) {
                        setPopUpResultUsuario(rslt[0], rslt[1]);
                    }
            });
}

function pulsarBuscarFlujo() {
    var source = '<c:url value="/sge/DefinicionFlujosFirma.do?opcion=cargarPantallaEleccionFlujoFirma"/>';
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,null,
        'width=700,height=550,status=no',function(rslt){
                    if (rslt != undefined) {
                        setPopUpResultFlujo(rslt.id, rslt.nombre);
                    }
            });
}

function pulsarAceptar() {
    var theForm = document.PlantillaFirmaForm;
    if (theForm) {
        var s1 = document.getElementById("codFirma").value;
        if ( ((s1=='O') || (s1=='U')) && (theForm.edtIdFirmanteDoc.value=='') ) {
            jsp_alerta("A","<fmt:message key='Sge.PlantillaFirmaForm.RequiereFirma.Message.MustSelectUser'/>","SIGP");
        } else if ( (s1=='L') && (theForm.edtIdFlujoFirmanteDoc.value=='') ) {
            jsp_alerta("A","<fmt:message key='Sge.PlantillaFirmaForm.RequiereFirma.Message.MustSelectFlujo'/>","SIGP");
        } else {
            theForm.idsUsuariosFirmantesAsString.value = theForm.edtIdFirmanteDoc.value;
            theForm.idFlujo.value = theForm.edtIdFlujoFirmanteDoc.value;
            pleaseWait("on");
            theForm.submit();
        }
    }
}

function pulsarSalir() {
    var plant = document.PlantillaFirmaForm.idPlantilla.value;
    var firma ="";
    try{
        firma = document.getElementById("codFirma").value;
    }catch(e){
        
    }
    var salida = [plant, firma];
    self.parent.opener.retornoXanelaAuxiliar(salida);
}
</script>
</tiles:put>
<tiles:put name="content" type="string">
<tiles:insert page="/jsp/portafirmas/tpls/AltiaPopUpTemplate.jsp" flush="false">
<tiles:put name="altia-app-form-title" type="string">
    <fmt:message key='Sge.PlantillaFirmaForm.title'/>
</tiles:put>
<tiles:put name="altia-app-form-content" type="string">
    <logic:messagesPresent message="true">
    </logic:messagesPresent>
        <html:form action="/sge/plantillafirma/ProcessPlantillaFirma.do" >
            <html:hidden property="usuario"/>
            <html:hidden property="idMunicipio"/>
            <html:hidden property="idProcedimiento"/>
            <html:hidden property="idTramite"/>
            <html:hidden property="idPlantilla"/>
            <html:hidden property="idsUsuariosFirmantesAsString"/>
            <html:hidden property="nombresUsuariosFirmantesAsString"/>
            <html:hidden property="idFlujo"/>
            <html:hidden property="nombreFlujo"/>

            <table class="contenidoPestanha" border=0 cellspacing="0px" cellpadding="0px">
                <tr>
                    <td style="width: 25%" class="etiqueta"><fmt:message key='Sge.PlantillaFirmaForm.RequiereFirma'/>:</td>
                    <td style="width: 75%">
                        <input name="requiereFirma" type="hidden" class="inputTextoObligatorio" id="codFirma"/>
                        <input name="requiereFirmaDesc" type="text" class="inputTextoObligatorio" id="descFirma" style="width:84%" readonly/>
                        <a id="anchorFirma" name="anchorFirma" href="">
                            <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonFirma" name="botonFirma"></span>
                        </a>
                    </td>
                </tr>
                <tr id="filaOtroUsuario">
                    <td style="width: 25%" class="etiqueta"><fmt:message key='Sge.PlantillaFirmaForm.Firmantes'/>:</td>
                    <td style="width: 75%">
                        <input name="edtIdFirmanteDoc" type="text" style="width:8%" class="inputTextoObligatorio" id="edtIdFirmanteDoc">
                        <input name="nombreUsuarioFirmante" type="text" class="inputTextoObligatorio" id="nombreUsuarioFirmante" style="width:75%" readOnly>
                        <a onclick="pulsarBuscarUsuario();">
                            <span class="fa fa-search" aria-hidden="true"  id="btnUsuariosFirmantes" name="btnUsuariosFirmantes"></span>
                        </a>
                    </td>
                </tr>
                <tr id="filaFlujo">
                    <td style="width: 25%" class="etiqueta"><fmt:message key='Sge.PlantillaFirmaForm.FlujoFirmantes'/>:</td>
                    <td style="width: 75%">
                        <input name="edtIdFlujoFirmanteDoc" type="hidden" style="width:8%" class="inputTextoObligatorio" id="edtIdFlujoFirmanteDoc">
                        <input name="nombreFlujoFirmanteDoc" type="text" class="inputTextoObligatorio" id="nombreFlujoFirmanteDoc" style="width:84%" readOnly>
                        <a onclick="pulsarBuscarFlujo();">
                            <span class="fa fa-search" aria-hidden="true"  id="btnFlujoFirmanteDoc" name="btnFlujoFirmanteDoc"></span>
                        </a>
                    </td>
                </tr>
            </table>            
        </html:form>
</tiles:put>
<tiles:put name="altia-app-form-footer" type="string">
<DIV id="capaBotones1" name="capaBotones1"  class="botoneraPrincipal">
    <div id="divAling">
        <INPUT type= "button" class="botonGeneral" value="<fmt:message key='Buttons.save'/>" name="cmdAceptar" id="cmdAceptar" onClick="pulsarAceptar();">
        <INPUT type= "button" class="botonGeneral" value="<fmt:message key='Buttons.cancel'/>" name="cmdSalir"  onClick="pulsarSalir();">
    </div>
</DIV>
<!-- BOTONES (fin) -->
</tiles:put>
</tiles:insert>
</tiles:put>
<tiles:put name="finalJavascript" type="string">
    <script type="text/javascript">
        
        <%
             int munic = 0;
            if (session != null) {
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    munic = usuario.getOrgCod();
                }
            }

            String PORTAFIRMAS  = "";
            try{
                ResourceBundle portafirmas = ResourceBundle.getBundle("Portafirmas");
                PORTAFIRMAS  = portafirmas.getString(munic+"/Portafirmas");
            }catch(Exception e){
              PORTAFIRMAS = "";
            }
            
        %>
        
        initDatosUsuarioFirmante(document.PlantillaFirmaForm.idsUsuariosFirmantesAsString.value, document.PlantillaFirmaForm.nombresUsuariosFirmantesAsString.value );
        initDatosFlujo(document.PlantillaFirmaForm.idFlujo.value, document.PlantillaFirmaForm.nombreFlujo.value );
        
        var comboFirma = new Combo("Firma");
        var listaCod = null;
        var listaDesc = null;
        var portafirmas = "<%=PORTAFIRMAS%>";
     
        if (""!=portafirmas && "LAN"==portafirmas) {
            listaCod = [[""], ["U"]];
            listaDesc = [["<fmt:message key='Sge.PlantillaFirmaForm.RequiereFirma.NoRequiere'/>"],
            ["<fmt:message key='Sge.PlantillaFirmaForm.RequiereFirma.Usuario'/>"]];
        } else {
            listaCod = [[""], ["T"], ["O"], ["U"], ["L"]];
            listaDesc = [["<fmt:message key='Sge.PlantillaFirmaForm.RequiereFirma.NoRequiere'/>"],
            ["<fmt:message key='Sge.PlantillaFirmaForm.RequiereFirma.Tramitador'/>"],
            ["<fmt:message key='Sge.PlantillaFirmaForm.RequiereFirma.Otro'/>"],
            ["<fmt:message key='Sge.PlantillaFirmaForm.RequiereFirma.Usuario'/>"],
            ["<fmt:message key='Sge.PlantillaFirmaForm.RequiereFirma.Flujo'/>"]];
        }
        var cont = 0;

        <%-- Redefinimos el evento onkeydown para el combo --%>
        comboFirma.base.onkeydown=function(){
            
            if(event.keyCode == 40){
                this.combo.selectItem(this.combo.selectedIndex+1);
            } else if(event.keyCode == 38){
                this.combo.selectItem(this.combo.selectedIndex-1);
            } else if(event.keyCode==13){
                this.combo.ocultar();
                this.combo.des.focus();
            }else{
                var letra = String.fromCharCode(event.keyCode);
                this.combo.buscaItem(letra);
            }
            event.cancelBubble=true;
            event.returnValue=false;
            return false;
        };
        comboFirma.addItems(listaCod, listaDesc);
        comboFirma.buscaLinea('<bean:write name="PlantillaFirmaForm" property="requiereFirma"/>');
        updateFrmMantenimientoDocTramite();

        <%--
             Para que devuelva correctamente el codigo del tipo de la firma al
             callback de la pantalla padre es necesario crear primero el combo
        --%>
        <c:if test = "${requestScope.EXIT_SUCCESS == true}">
            pulsarSalir();
        </c:if>
        
        <%--
            Debido a como funcionan los eventos del combo, la unica solicion
            que se ha encontrado es utilizar un timer para comprobar si se 
            cambia el valor de las opciones de firma para habilitar/deshabilitar
            los inputs correspondientes.
        --%>
        setInterval(comprobarCambioTipoFirma, 100);

<%String Agent = request.getHeader("user-agent");%>

var coordx=0;
var coordy=0;

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
            if (comboFirma.base.style.visibility == "visible" && isClickOutCombo(comboFirma,coordx,coordy)) setTimeout('comboFirma.ocultar()',20);
        }
        if (teclaAuxiliar == 9){
            comboFirma.ocultar();
        }
}

pleaseWait("off");
    </script>
</tiles:put>
</tiles:insert>
