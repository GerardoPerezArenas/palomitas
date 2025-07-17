<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String hVirtual = m_Config.getString("hostVirtual");
    String host="";
    try {
        // Si se utiliza la anterior, hay que definir en el fichero de hosts, la propiedad que se defina en el common.properties
        String protocolo = StrutsUtilOperations.getProtocol(request);

        if (hVirtual.equals("")) host = protocolo + "://" + request.getHeader("host");
        else host = hVirtual;
    } catch (Exception e) {
        e.printStackTrace();
    }

    Config documentosConfig = ConfigServiceHelper.getConfig("documentos");
    String enlace = documentosConfig.getString("enlace_asistente_firma_portafirmas");

    UsuarioValueObject user = new UsuarioValueObject();
    int apl=11;
    int idioma =1;
    if (session != null) {
        user = (UsuarioValueObject) session.getAttribute("usuario");
        idioma = user.getIdioma();
    }

    // Idioma del asistente si está configurada la url al mismo en documentos.properties
    String languageAsistente ="es";
    if(idioma==1 && !"".equals(enlace))
        languageAsistente = "es";
    else
    if(idioma==2)
        languageAsistente = "ga";

    String statusBar = m_Config.getString("JSP.StatusBar");
%>
 <meta http-equiv="X-UA-Compatible" content="IE=10"/>
<tiles:insert page="/jsp/portafirmas/tpls/WindowTemplate.jsp" flush="true">
<tiles:put name="title" type="string">
    <fmt:message key='Portafirmas.SearchDocumentoPortafirmasForm.title'/>
</tiles:put>
<tiles:put name="head-content" type="string">
<script type="text/javascript" language="JavaScript"> 
<%@ include file="jsGetUrlDocumentoPortafirmas.jsp" %>
var ERROR_ONE_MUST_BE_SELECTED = '<fmt:message key="ErrorMessages.OneMustSelected"/>';
var ERROR_ESTADO_FIRMA_INCOMPATIBLE = '<fmt:message key="Portafirmas.SearchDocumentoPortafirmasForm.Errors.EstadoFirmaIncompatible"/>';
var ERROR_ESTADO_SUBSANACION_INCOMPATIBLE ='<fmt:message key="Portafirmas.SearchDocumentoPortafirmasForm.Errors.EstadoSubsanacionIncompatible"/>';
var ERROR_OPENING_WORD = '<fmt:message key="ErrorMessages.Word.ErrorOpening"/>';
var ERROR_USUARIO_NO_AUTORIZADO_SUBSANACION = '<fmt:message key="Portafirmas.SearchDocumentoPortafirmasForm.Errors.UsuarioNoAutorizadoSubsanacion"/>';

var data = new Array();
var rows = new Array();
<c:forEach var="jspResultItem" items="${requestScope.SearchDocumentoPortafirmasForm.results}" varStatus="jspResultItemStatus">
    var idNumeroExpediente = '<c:out value="${jspResultItem.idNumeroExpediente}"/>';
    <c:if test="${jspResultItem.tipoDocumento == 1}">
        idNumeroExpediente = "REL_" + idNumeroExpediente;
    </c:if>
    var url = "";

    <c:if test="${jspResultItem.estadoFirma == 'F'}">
    var tt = '<c:out value="${jspResultItemStatus.count}"/>'-1;
    url = '<a href="#" onclick="javascript:btnVerDatos_click(data['+tt+']);" />'+
        '  (<fmt:message key='Portafirmas.SearchDocumentoPortafirmasForm.datosFirma'/>)</a>';
    </c:if>
    <c:if test="${jspResultItem.estadoFirma == 'R'}">                
        var tt = '<c:out value="${jspResultItemStatus.count}"/>'-1;
        url = '<a href="#" onclick="javascript:btnVerDatos_click(data['+tt+']);" />'+
            '  (<fmt:message key='Portafirmas.SearchDocumentoPortafirmasForm.datosFirma'/>)</a>';
    </c:if>
    data[data.length]=['<c:out value="${jspResultItem.idMunicipio}"/>', 
        '<c:out value="${jspResultItem.idProcedimiento}"/>',
        '<c:out value="${jspResultItem.idEjercicio}"/>', 
        '<c:out value="${jspResultItem.idNumeroExpediente}"/>', 
        '<c:out value="${jspResultItem.idTramite}"/>', 
        '<c:out value="${jspResultItem.idOcurrenciaTramite}"/>',
        '<c:out value="${jspResultItem.idNumeroDocumento}"/>', 
        '<c:out value="${jspResultItem.usuarioFirmante}"/>', 
        '<c:out value="${jspResultItem.estadoFirma}"/>', 
        '<c:out value="${jspResultItem.nombreDocumento}"/>', 
        '<c:out value="${jspResultItem.tipoDocumento}"/>',
        '<c:out value="${jspResultItem.idNumFirma}"/>',
        '<c:out value="${jspResultItem.idPresentado}"/>',
        '<c:out value="${jspResultItem.firmaBase64}"/>',
        '<c:out value="${jspResultItem.finalizaRechazo}"/>',
        '<c:out value="${jspResultItem.editorTexto}"/>'];

    rows[rows.length]=['<c:out value="${jspResultItem.nombreProcedimiento}"/>',
        '<c:out value="${jspResultItem.nombreTramite}"/>', 
        idNumeroExpediente, 
        '<c:out value="${jspResultItem.nombreDocumento}"/>',
        getEtiquetaEstadoFirma('<c:out value="${jspResultItem.estadoFirma}"/>') + url,
        '<fmt:formatDate value="${jspResultItem.fechaEnvioFirmaAsDate}" type="date" dateStyle="medium" pattern="dd/MM/yyyy"/>',
        '<fmt:formatDate value="${jspResultItem.fechaFirmaAsDate}" type="date" dateStyle="medium" pattern="dd/MM/yyyy"/>'];

    <c:if test="${jspResultItemStatus.last}"><c:set var="jspResultsCount" value="${jspResultItemStatus.count}"/></c:if>
</c:forEach>

var error = false;

function inicializar(){
    if (CLIENTE_FIRMA!="FLEXIA" && document.getElementById("muestraAsistente")!=null){
        document.getElementById("muestraAsistente").style.visibility = 'hidden';
    }

    if(error)
        jsp_alerta("A","<fmt:message key="ErrorMessages.NoResultsFound"/>");
    if(document.SearchDocumentoPortafirmasForm.chkEstado.checked)
        comboEstadoFirma.activate();
    else
        comboEstadoFirma.deactivate();

        <logic:messagesPresent message="true">
        <html:messages message="true" id="msg">
        jsp_alerta('A','<bean:write name="msg"/>');
        </html:messages>
        </logic:messagesPresent>

    }

function btnFirmar_click(theSender, theForm) {
    if ( (theSender) && (theForm)) {
        if (tablaDocumentos.selectedIndex>=0) {
            var theDataRow = data[tablaDocumentos.selectedIndex];
            if (theDataRow[8]=='O') {    
                if (theDataRow[10]=='0' || theDataRow[10]=='1') {
                    theForm.name   = "FirmaDocumentoPortafirmasForm";
                    theForm.action = '<%=host%>'+"<c:url value='/portafirmas/documentoportafirmas/PrepareFirmaDocumentoPortafirmas.do'/>";
                    theForm.idMunicipio.value = theDataRow[0]; theForm.idMunicipio.disabled = false;
                    theForm.idProcedimiento.value = theDataRow[1]; theForm.idProcedimiento.disabled = false;
                    theForm.idEjercicio.value = theDataRow[2]; theForm.idEjercicio.disabled = false;
                    theForm.idNumeroExpediente.value = theDataRow[3]; theForm.idNumeroExpediente.disabled = false;
                    theForm.idTramite.value = theDataRow[4]; theForm.idTramite.disabled = false;
                    theForm.idOcurrenciaTramite.value = theDataRow[5]; theForm.idOcurrenciaTramite.disabled = false;
                    theForm.idNumeroDocumento.value = theDataRow[6]; theForm.idNumeroDocumento.disabled = false;
                    theForm.usuarioFirmante.value = theDataRow[7]; theForm.usuarioFirmante.disabled = false;
                    theForm.descripcionDocumento.value = theDataRow[9]; theForm.descripcionDocumento.disabled = false;
                    theForm.tipoDocumento.value = theDataRow[10]; theForm.tipoDocumento.disabled = false;                                    
                    theForm.selectedIndex.value = tablaDocumentos.selectedIndex; theForm.selectedIndex.disabled = false;
                    theForm.editorTexto.value = theDataRow[15];
                    
                    theForm.submit();
                }else if (theDataRow[10]=='2'){
                    // Se prepara la firma del documento externo                                    
                    theForm.name   = "FirmaDocumentoPortafirmasForm";
                    theForm.action = '<%=host%>'+"<c:url value='/portafirmas/documentoportafirmas/PrepareFirmaOtroDocumentoPortafirmas.do'/>";
                    theForm.idNumeroDocumento.value = theDataRow[6]; theForm.idNumeroDocumento.disabled = false;                                    
                    theForm.selectedIndex.value = tablaDocumentos.selectedIndex; theForm.selectedIndex.disabled = false;
                    
                    theForm.submit();
                }else if (theDataRow[10]=='3'){
                    theForm.name = "FirmaDocumentoPortafirmasForm";
                    theForm.action = '<%=host%>'+"<c:url value='/portafirmas/documentoportafirmas/PrepareFirmaDocumentoProcedimientoPortafirmas.do'/>";
                    theForm.idNumeroDocumento.value = theDataRow[6]; 
                    theForm.idNumeroDocumento.disabled = false;
                    theForm.usuarioFirmante.value = theDataRow[7]; 
                    theForm.usuarioFirmante.disabled = false;  
                    theForm.idMunicipio.value = theDataRow[0]; 
                    theForm.idMunicipio.disabled = false;
                    theForm.idProcedimiento.value = theDataRow[1]; 
                    theForm.idProcedimiento.disabled = false;
                    theForm.idEjercicio.value = theDataRow[2]; 
                    theForm.idEjercicio.disabled = false;
                    theForm.idNumeroExpediente.value = theDataRow[3]; 
                    theForm.idNumeroExpediente.disabled = false;
                    theForm.selectedIndex.value = tablaDocumentos.selectedIndex; 
                    theForm.selectedIndex.disabled = false;
                    theForm.idNumFirma.value = theDataRow[11];                                    
                    theForm.idNumFirma.disabled =false;
                    theForm.idPresentado.value = theDataRow[12];
                    theForm.idPresentado.disabled = false;
                    theForm.tipoDocumento.value = theDataRow[10];
                    theForm.tipoDocumento.disabled = false;
                    theForm.firmaBase64.value = theDataRow[13];
                    theForm.firmaBase64.disabled = false;
                    theForm.descripcionDocumento.value = theDataRow[9];
                    theForm.descripcionDocumento.disabled = false;
                    
                    theForm.submit();
                }else if (theDataRow[10]=='4'){
                    // Se prepara la firma de un documento externo asociado a una notificación electrónica
                    theForm.name   = "FirmaDocumentoPortafirmasForm";
                    theForm.action = '<%=host%>'+"<c:url value='/portafirmas/documentoportafirmas/PrepareFirmaDocumentoExternoNotificacionPortafirmas.do'/>";

                    /*****/
                    theForm.idNumeroDocumento.value = theDataRow[6]; 
                    theForm.idNumeroDocumento.disabled = false;
                    theForm.usuarioFirmante.value = theDataRow[7]; 
                    theForm.usuarioFirmante.disabled = false;  
                    theForm.idMunicipio.value = theDataRow[0]; 
                    theForm.idMunicipio.disabled = false;
                    theForm.idProcedimiento.value = theDataRow[1]; 
                    theForm.idProcedimiento.disabled = false;
                    theForm.idEjercicio.value = theDataRow[2]; 
                    theForm.idEjercicio.disabled = false;
                    theForm.idNumeroExpediente.value = theDataRow[3]; 
                    theForm.idNumeroExpediente.disabled = false;
                    theForm.selectedIndex.value = tablaDocumentos.selectedIndex; 
                    theForm.selectedIndex.disabled = false;
                    theForm.idNumFirma.value = theDataRow[11];                                    
                    theForm.idNumFirma.disabled =false;
                    theForm.idPresentado.value = theDataRow[12];
                    theForm.idPresentado.disabled = false;
                    theForm.tipoDocumento.value = theDataRow[10];
                    theForm.tipoDocumento.disabled = false;
                    theForm.firmaBase64.value = theDataRow[13];
                    theForm.firmaBase64.disabled = false;
                    theForm.descripcionDocumento.value = theDataRow[9];
                    theForm.descripcionDocumento.disabled = false;

                    /*****/                                    
                    theForm.selectedIndex.value = tablaDocumentos.selectedIndex; theForm.selectedIndex.disabled = false;
                    
                    theForm.submit();
                }

            } else {
                tablaDocumentos.desactivaRow(tablaDocumentos.selectedIndex);
                jsp_alerta('A',ERROR_ESTADO_FIRMA_INCOMPATIBLE,APP_TITLE);
            }
        } else {
            jsp_alerta('A',ERROR_ONE_MUST_BE_SELECTED,APP_TITLE);
        }
    }
}

function btnRechazar_click(theSender, theForm) {   
    if (theSender && theForm) {
        if (tablaDocumentos.selectedIndex>=0) {                                                  
            var theDataRow = data[tablaDocumentos.selectedIndex];

            if (theDataRow[8]=='O') {
                if (theDataRow[10]=='0' || theDataRow[10]=='1') {
                    theForm.name   = "FirmaDocumentoPortafirmasForm";
                    theForm.action = '<%=host%>'+"<c:url value='/portafirmas/documentoportafirmas/PrepareRechazoDocumentoPortafirmas.do'/>";
                    theForm.idMunicipio.value = theDataRow[0]; theForm.idMunicipio.disabled = false;
                    theForm.idProcedimiento.value = theDataRow[1]; theForm.idProcedimiento.disabled = false;
                    theForm.idEjercicio.value = theDataRow[2]; theForm.idEjercicio.disabled = false;
                    theForm.idNumeroExpediente.value = theDataRow[3]; theForm.idNumeroExpediente.disabled = false;
                    theForm.idTramite.value = theDataRow[4]; theForm.idTramite.disabled = false;
                    theForm.idOcurrenciaTramite.value = theDataRow[5]; theForm.idOcurrenciaTramite.disabled = false;
                    theForm.idNumeroDocumento.value = theDataRow[6]; theForm.idNumeroDocumento.disabled = false;
                    theForm.usuarioFirmante.value = theDataRow[7]; theForm.usuarioFirmante.disabled = false;
                    theForm.descripcionDocumento.value = theDataRow[9]; theForm.descripcionDocumento.disabled = false;
                    theForm.tipoDocumento.value = theDataRow[10]; theForm.tipoDocumento.disabled = false;                                    
                    theForm.selectedIndex.value = tablaDocumentos.selectedIndex; theForm.selectedIndex.disabled = false;
                    
                    theForm.submit();
                }else if (theDataRow[10]=='2'){
                    theForm.name   = "FirmaDocumentoPortafirmasForm";
                    theForm.action = '<%=host%>'+"<c:url value='/portafirmas/documentoportafirmas/PrepareRechazoOtroDocumentoPortafirmas.do'/>";
                    theForm.idNumeroDocumento.value = theDataRow[6]; theForm.idNumeroDocumento.disabled = false;
                    theForm.usuarioFirmante.value = theDataRow[7]; theForm.usuarioFirmante.disabled = false;                                    
                    theForm.selectedIndex.value = tablaDocumentos.selectedIndex; theForm.selectedIndex.disabled = false;
                    
                    theForm.submit();
                }else if (theDataRow[10]=='3'){
                    theForm.name   = "FirmaDocumentoPortafirmasForm";
                    theForm.action = '<%=host%>' + "<c:url value='/portafirmas/documentoportafirmas/PrepareRechazoDocumentoProcedimientoPortafirmas.do'/>";
                    theForm.idNumeroDocumento.value = theDataRow[6]; 
                    theForm.idNumeroDocumento.disabled = false;
                    theForm.usuarioFirmante.value = theDataRow[7];
                    theForm.idMunicipio.value = theDataRow[0]; 
                    theForm.idMunicipio.disabled = false;
                    theForm.idProcedimiento.value = theDataRow[1]; 
                    theForm.idProcedimiento.disabled = false;
                    theForm.idEjercicio.value = theDataRow[2]; 
                    theForm.idEjercicio.disabled = false;
                    theForm.idNumeroExpediente.value = theDataRow[3]; 
                    theForm.idNumeroExpediente.disabled = false;
                    theForm.usuarioFirmante.disabled = false;                                    
                    theForm.selectedIndex.value = tablaDocumentos.selectedIndex; 
                    theForm.selectedIndex.disabled = false;
                    theForm.idNumFirma.value = theDataRow[11];                                    
                    theForm.idNumFirma.disabled =false;
                    theForm.idPresentado.value = theDataRow[12];
                    theForm.idPresentado.disabled = false;
                    theForm.tipoDocumento.value = theDataRow[10];
                    theForm.tipoDocumento.disabled = false;
                    theForm.finalizaRechazo.value  = theDataRow[14];
                    theForm.finalizaRechazo.disabled = false;
                    
                    theForm.submit();
                }else if (theDataRow[10]=='4'){ 
                    theForm.name   = "FirmaDocumentoPortafirmasForm";
                    theForm.action = '<%=host%>'+ "<c:url value='/portafirmas/documentoportafirmas/PrepareRechazoDocumentoExternoNotificacionPortafirmas.do'/>";                                                                        
                    theForm.idNumeroDocumento.value = theDataRow[6]; 
                    theForm.idNumeroDocumento.disabled = false;
                    theForm.usuarioFirmante.value = theDataRow[7];
                    theForm.idMunicipio.value = theDataRow[0]; 
                    theForm.idMunicipio.disabled = false;
                    theForm.idProcedimiento.value = theDataRow[1]; 
                    theForm.idProcedimiento.disabled = false;
                    theForm.idEjercicio.value = theDataRow[2]; 
                    theForm.idEjercicio.disabled = false;
                    theForm.idNumeroExpediente.value = theDataRow[3]; 
                    theForm.idNumeroExpediente.disabled = false;
                    theForm.usuarioFirmante.disabled = false;                                    
                    theForm.selectedIndex.value = tablaDocumentos.selectedIndex; 
                    theForm.selectedIndex.disabled = false;
                    theForm.idNumFirma.value = theDataRow[11];                                    
                    theForm.idNumFirma.disabled =false;
                    theForm.idPresentado.value = theDataRow[12];
                    theForm.idPresentado.disabled = false;
                    theForm.tipoDocumento.value = theDataRow[10];
                    theForm.tipoDocumento.disabled = false;
                    theForm.finalizaRechazo.value  = theDataRow[14];
                    theForm.finalizaRechazo.disabled = false;
                    
                    theForm.submit();
                }
            } else {
                tablaDocumentos.desactivaRow(tablaDocumentos.selectedIndex);
                jsp_alerta('A',ERROR_ESTADO_FIRMA_INCOMPATIBLE,APP_TITLE);
            }
        } else {
            jsp_alerta('A',ERROR_ONE_MUST_BE_SELECTED,APP_TITLE);
        }
    }
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

function verificarAccesoUsuarioSubsanacion(codDocumento,codProcedimiento,numExpediente){
    var salida = false;
    var ajax = getXMLHttpRequest();

    var CONTEXT_PATH = '<%=request.getContextPath()%>';

    if(ajax!=null){                        

         var url = CONTEXT_PATH + "/VerificarUsuarioAccesoSubsanacionDocumentoInicio.do";
         var parametros = "&codProcedimiento=" + codProcedimiento + "&numDocumento=" + codDocumento + "&numExpediente=" + numExpediente;

         ajax.open("POST",url,false);
         ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
         ajax.setRequestHeader("Accept", "text/html, application/xml, text/plain");
         ajax.send(parametros);

         try{
             if (ajax.readyState==4 && ajax.status==200){
                 var text = ajax.responseText;	                                        
                 var datosRespuesta = text.split("=");
                 if(datosRespuesta!=null && datosRespuesta[0]=="autorizado"){

                     var valor = datosRespuesta[1];                                    
                     if(valor=="1"){                                        
                         salida = true;                                
                     } 
                 }                             
             }
         }
         catch(Err){
             alert("ERROR EN LLAMADA AJAX: " + Err.description);
         }  
    }
    return salida;

 }//verificarAccesoUsuarioSubsanacion


 function btnSubsanar_click(theSender, theForm) { 

     if (theSender && theForm) {
         if (tablaDocumentos.selectedIndex>=0) {                                                  
             var theDataRow = data[tablaDocumentos.selectedIndex];
             if (theDataRow[8]=='O') {
                 if (theDataRow[10]=='3') {
                     theForm.name   = "FirmaDocumentoPortafirmasForm";
                     theForm.action = '<%=host%>' + "<c:url value='/portafirmas/documentoportafirmas/PrepareSubsanarDocumentoProcedimientoPortafirmas.do'/>";

                     theForm.idNumeroDocumento.value = theDataRow[6]; 

                     theForm.idNumeroDocumento.disabled = false;
                     theForm.usuarioFirmante.value = theDataRow[7];
                     theForm.idMunicipio.value = theDataRow[0]; 
                     theForm.idMunicipio.disabled = false;
                     theForm.idProcedimiento.value = theDataRow[1]; 
                     theForm.idProcedimiento.disabled = false;
                     theForm.idEjercicio.value = theDataRow[2]; 
                     theForm.idEjercicio.disabled = false;
                     theForm.idNumeroExpediente.value = theDataRow[3]; 
                     theForm.idNumeroExpediente.disabled = false;
                     theForm.usuarioFirmante.disabled = false;                                    
                     theForm.selectedIndex.value = tablaDocumentos.selectedIndex; 
                     theForm.selectedIndex.disabled = false;
                     theForm.idNumFirma.value = theDataRow[11];                                    
                     theForm.idNumFirma.disabled =false;
                     theForm.idPresentado.value = theDataRow[12];
                     theForm.idPresentado.disabled = false;
                     theForm.tipoDocumento.value = theDataRow[10];
                     theForm.tipoDocumento.disabled = false;
                     theForm.idTramite.value = theDataRow[4];
                     theForm.idTramite.disabled = false;


                     /**** VERIFICACION DE SI EL USUARIO ESTÁ AUTORIZADO A SUBSANAR EL DOCUMENTO ****/
                     var exito = verificarAccesoUsuarioSubsanacion(theForm.idNumeroDocumento.value,theForm.idProcedimiento.value,theForm.idNumeroExpediente.value);                                    
                     if(exito){
                         
                         theForm.submit();
                     }else                                        
                         jsp_alerta('A',ERROR_USUARIO_NO_AUTORIZADO_SUBSANACION,APP_TITLE);

                     /**** VERIFICACION DE SI EL USUARIO ESTÁ AUTORIZADO A SUBSANAR EL DOCUMENTO ****/                                    

                 } else {
                     theGrid.selectAll(false);                                    
                     jsp_alerta('A',ERROR_ESTADO_SUBSANACION_INCOMPATIBLE, APP_TITLE);
                 }
             } else {                                
                 theGrid.selectAll(false);
                 jsp_alerta('A',ERROR_ESTADO_FIRMA_INCOMPATIBLE,APP_TITLE);
             }
         } else {                            
             jsp_alerta('A',ERROR_ONE_MUST_BE_SELECTED,APP_TITLE);
         }
     }
 }

 function btnDocumentacionAnexaExpediente_click(theSender, theForm) {
     if (theSender && theForm) {
         if (tablaDocumentos.selectedIndex>=0) {
             var theDataRow = data[tablaDocumentos.selectedIndex];

             var codProcedimiento = theDataRow[1];
             var numeroExpediente = theDataRow[3];
             var codDocumento     = theDataRow[6];

             if((numeroExpediente==null || numeroExpediente=='') && (codProcedimiento==null || codProcedimiento=='')){
                 // Si el documento no tiene un expediente y procedimiento asociado => Se trata de un documento externo
                 // dado de alta a través del WS de envio de documentos al portafirmas                                
                numeroExpediente = localizarExpedienteDeDocumentoExterno(codDocumento); 
             }

             if(numeroExpediente==null || numeroExpediente==""){
                 jsp_alerta('A','<fmt:message key="Error.documento.externo.noesta.asociado.expediente"/>');

             }else{
                 var url = '<%=host%>' + "<c:url value='/portafirmas/documentoportafirmas/ListadoDocumentoExpedientePortafirmas.do'/>" + "?numExpediente=" + numeroExpediente;
                 var datos = new Array();
                 abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+url,datos,
                     'width=973,height=750,status='+ '<%=statusBar%>',function(){});
             }
         } else {                            
             jsp_alerta('A',ERROR_ONE_MUST_BE_SELECTED,APP_TITLE);
         }
     }
 }

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
          var xmlDoc = null;											
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

function getExceptionMessage(jsEx) {
    if (jsEx) {
        if (jsEx.description == null) return jsEx.message;
        else return jsEx.description;
    } else {
        return "";
    }
}

function newActiveXObject(clsname) {
    try {
        return new ActiveXObject(clsname);
    } catch (jsEx) {
        jsp_alerta('A',ERROR_OPENING_WORD + ': ' + getExceptionMessage(jsEx),APP_TITLE);
        return null;
    }
}

function wordOpenDocument(oWord, docUrl) {
    try {
        return oWord.Documents.Open(docUrl);
    } catch (jsEx) {
        jsp_alerta('A',ERROR_OPENING_WORD + ': ' + getExceptionMessage(jsEx),APP_TITLE);
        return null;
    }
}



function getElementoSeleccionado(){                    
    var longitud = rows.length;

    for(i=0;i<longitud;i++){
        try{
            document.getElementById("selection_jstTablejstContainerId" + i).checked
            if(document.getElementById("selection_jstTablejstContainerId" + i).checked){
                return i;
            }
        }catch(Err){                            
        }

    }// for

}// getElementoSeleccionado


    function btnVerFirmas_click(theSender, theForm) {
        if (theSender && theForm) {
            if (tablaDocumentos.selectedIndex>=0) {
                var theDataRow = data[tablaDocumentos.selectedIndex];
                if (theDataRow[10]=='3'){ //Es un documento de procedimiento y por tanto tiene circuito
                    var codDocumentoAdjunto = theDataRow[12];
                    var source = "<c:url value='/expediente/FichaExpedienteDocumentosPresentados.do?opcion=verFirmas'/>" +
                        "&codDocumentoAdjunto="+codDocumentoAdjunto;
                    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,[],
                        'width=800,height=350,status='+ '<%=statusBar%>',function(){});
                } else {
                    //El resto de documentos no poseen circuito de firmas
                    jsp_alerta("A","<fmt:message key="Portafirmas.SearchDocumentoPortafirmasForm.NoCircuitoFirmas"/>");
                }
            }
            else {
                jsp_alerta('A',ERROR_ONE_MUST_BE_SELECTED,APP_TITLE);
            }
        }
    }

    function btnVer_click(theSender, theForm) { 
        if (theSender && theForm) {
            if (tablaDocumentos.selectedIndex>=0) {
                var theDataRow = data[tablaDocumentos.selectedIndex];
                var theRowsRow = rows[tablaDocumentos.selectedIndex];

                var url="";
                var docTramitacion = true;

                if (theDataRow[10] == '0') {
                    url = getUrlForDownloadDocument(theDataRow[0], theDataRow[3], theDataRow[4], theDataRow[5], theDataRow[6], theRowsRow[3],theDataRow[15]);
                } else
                    if (theDataRow[10] == '1') {
                        url = getUrlForDownloadRelationDocument(theDataRow[0], theDataRow[3], theDataRow[4], theDataRow[5], theDataRow[6], theRowsRow[3],theDataRow[15]);
                    }else
                    if (theDataRow[10] == '2') {
                        docTramitacion = false;
                        url =  "<%=host%><%=request.getContextPath()%>/MostrarDocumentoExternoPortafirmas.do?codDocumento=" +  theDataRow[6];
                    }  else if (theDataRow[10] == '3') {
                    docTramitacion = false;                                
                    url = getUrlForDownloadProcedimientoDocument
                    (theDataRow[0], theDataRow[2], theDataRow[3], theDataRow[6], theDataRow[1]);
                    }else
                    if (theDataRow[10] == '4') { 
                        docTramitacion = false;
                        url =  "<%=host%><%=request.getContextPath()%>/MostrarDocumentoExternoNotificacionElectronica.do?codDocumento=" +  theDataRow[6];
                    } 

                if(docTramitacion){
                    if (theDataRow[15]=="OOFFICE"){
                        var array = new Array();
                        array=url.split("?");
                        var direccion = array[0];
                        window.open(direccion,'','toolbar=no,location=no,status=no,menubar=no');
                    }
                    else if (theDataRow[15]=="WORD"){
                        var oWord = newActiveXObject("Word.Application");
                        if (oWord) {
                            oWord.Visible = false;
                            var oWordDoc = wordOpenDocument(oWord, url);
                            if (oWordDoc) {
                                oWordDoc.AttachedTemplate = "";
                                oWord.Visible = true;
                            }
                        }
                    }
                }else{
                    // Se descarga el contenido del documento externo.                                
                    var ventanaSecundaria = window.open(url, "ventana1","left=10, top=10, width=75, height=75, scrollbars=no, menubar=no, location=no, resizable=yes");                               
                }
            } else {
                jsp_alerta('A',ERROR_ONE_MUST_BE_SELECTED,APP_TITLE);
            }
        }
    }


    function btnVerDatos_click(data){ 
        var finalURL = '<c:url value="/sge/documentofirma/ViewDocumentoFirma.do?"/>';
        finalURL = finalURL + "idMunicipio="+data[0];
        finalURL = finalURL + "&idProcedimiento="+data[1];
        finalURL = finalURL + "&idEjercicio="+data[2];
        finalURL = finalURL + "&idNumeroExpediente="+data[3];
        finalURL = finalURL + "&idTramite="+data[4];
        finalURL = finalURL + "&idOcurrenciaTramite="+data[5];
        finalURL = finalURL + "&idNumeroDocumento="+data[6];
        finalURL = finalURL + "&tipoDocumento=" + data[10];
        finalURL = finalURL + "&codigoFirmaDocProcedimiento=" + data[11];
        finalURL = finalURL + "&contador="+ Math.random();                   

        abrirXanelaAuxiliar(finalURL, null,'width='+650+',height='+360+',status=no,resizable=no,scrollbars=no',function(){});
    }

    function rowFunctionClick(){                
    }
<%@ include file="jsEtiquetaEstadoFirma.jsp" %>
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
                <fmt:message key='Portafirmas.SearchDocumentoPortafirmasForm.title'/>                
            </tiles:put>
            <tiles:put name="altia-app-form-content" type="string">
                <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
                <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
                <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
                <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
                <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
                <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/SearchDocumentoPortafirmasForm.js"></script>
                <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
                <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>       
                <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
                <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
                <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
                <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
                <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
                
                <html:form action="/portafirmas/documentoportafirmas/SearchDocumentoPortafirmas.do" >
                    <html:hidden property="startIndex"/>
                    <html:hidden property="count"/>
                    <html:hidden property="totalCount"/>
                    <html:hidden property="startIndex"/>
                    <html:hidden property="doPopUp"/>
                    <html:hidden property="doPrintPreview"/>
                    <html:hidden property="doPageByPage" value="false"/>
                    <html:hidden property="doSearch"/>
                    <html:hidden property="canEdit"/>
                    <html:hidden property="sort" value="1"/>
                    <html:hidden property="sortField" value="fechaEnvio"/>
                    <html:hidden property="estadoFirma"/>

                    <input type="hidden" name="idMunicipio" id="idMunicipio" value="" disabled>
                    <input type="hidden" name="idProcedimiento" id="idProcedimiento" value="" disabled>
                    <input type="hidden" name="idEjercicio" id="idEjercicio" value="" disabled>
                    <input type="hidden" name="idNumeroExpediente" id="idNumeroExpediente" value="" disabled>
                    <input type="hidden" name="idTramite" id="idTramite" value="" disabled>
                    <input type="hidden" name="idOcurrenciaTramite" id="idOcurrenciaTramite" value="" disabled>
                    <input type="hidden" name="idNumeroDocumento" id="idNumeroDocumento" value="" disabled>
                    <input type="hidden" name="usuarioFirmante" id="usuarioFirmante" value="" disabled>
                    <input type="hidden" name="descripcionDocumento" id="descripcionDocumento" value="" disabled>
                    <input type="hidden" name="tipoDocumento" id="tipoDocumento" value="" disabled>
                    <input type="hidden" name="editorTexto" id="editorTexto" value="" disabled>
                    <input type="hidden" name="idNumFirma" id="idNumFirma" value="" disabled>
                    <input type="hidden" name="idPresentado" id="idPresentado" value="" disabled>
                    <input type="hidden" name="firmaBase64" id="firmaBase64" value="" disabled>
                    <input type="hidden" name="selectedIndex" id="selectedIndex" value="" disabled>
                    <input type="hidden" name="finalizaRechazo" id="finalizaRechazo" value="" disabled>

                    <div id="desplegable" style="overflow-y: auto; overflow-x: no; visibility: hidden; BORDER: 0px"></div>
                    <table style="width:100%">					
                        <TR>
                            <td>
                                <div class="sub3titulo"><fmt:message key="Portafirmas.SearchDocumentoPortafirmasForm.SearchCriteria"/></div>
                            </td>
                        </TR>
                        <TR>
                            <td style="padding:10px 0 0 5px">
                                <span class="etiqueta">
                                    <fmt:message key="Portafirmas.SearchDocumentoPortafirmasForm.SearchCriteria.PorEstado"/>
                                </span>
                                <html:checkbox property="chkEstado" styleClass="inputTextoObligatorio" style="background-color:transparent;border:0;" 
                                               onchange="cambio(this);chkEstado_change(this,this.form);" onclick="cambio(this);"/>
                                <html:text styleId="inputTextoObligatorio" property="codEstadoFirma" styleClass="inputTexto"  size="3" readonly="true" style="margin-left:20px"/>
                                <html:text styleId="inputTextoObligatorio" property="descEstadoFirma" styleClass="inputTexto"  size="25" readonly="true"/>
                                <A href="" id="anchorEstadoFirma" name="anchorEstadoFirma" style="text-decoration:none;" >
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonEstadoFirma" name="botonEstadoFirma" style="cursor:hand;"></span>
                                </A>
                                <script>

                                    chkEstado_change(document.SearchDocumentoPortafirmasForm.chkEstado, document.SearchDocumentoPortafirmasForm);

                                </script>
                                <input type="button" class="botonGeneral" name="btnSubmit" style="margin-left:40px" value="<fmt:message key='Buttons.search'/>" onclick="btnSubmit_click(this,this.form,'<%=host%>')">
                            </td>
                        </TR>
                        <TR>
                            <td>
                    <c:if test="${requestScope.SearchDocumentoPortafirmasForm.doSearch}">

                        <c:if test="${requestScope.SearchDocumentoPortafirmasForm.totalCount <= 0}" >
                            <script> </script>
                        </c:if>
                        <c:if test="${requestScope.SearchDocumentoPortafirmasForm.totalCount > 0}" >
                            <table width="100%">
                                <tr>
                                    <td id="tabla">
                                        <!-- GRID BEGIN -->
                                        <script>                                                            
                                            var tablaDocumentos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>',
                                                    '<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>',
                                                    '<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', 
                                                    '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>',
                                                    '<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

                                            tablaDocumentos.addColumna('100','left',"<fmt:message key='Portafirmas.SearchDocumentoPortafirmasForm.NombreProcedimiento'/>",'String');
                                            tablaDocumentos.addColumna('100','left',"<fmt:message key='Portafirmas.SearchDocumentoPortafirmasForm.NombreTramite'/>",'String');
                                            tablaDocumentos.addColumna('120','left',"<fmt:message key='Portafirmas.SearchDocumentoPortafirmasForm.IdNumeroExpediente'/>",'String');
                                            tablaDocumentos.addColumna('160','left',"<fmt:message key='Portafirmas.SearchDocumentoPortafirmasForm.NombreDocumento'/>",'String');
                                            tablaDocumentos.addColumna('100','center',"<fmt:message key='Portafirmas.SearchDocumentoPortafirmasForm.EstadoFirma'/>",'String');
                                            tablaDocumentos.addColumna('75','center',"<fmt:message key='Portafirmas.SearchDocumentoPortafirmasForm.FechaEnvioFirma'/>",'String');
                                            tablaDocumentos.addColumna('75','left',"<fmt:message key='Portafirmas.SearchDocumentoPortafirmasForm.FechaFirma'/>",'String');
                                            tablaDocumentos.displayCabecera=true;
                                            tablaDocumentos.setLineas(rows);
                                            tablaDocumentos.displayTabla();
                                            <c:if test="${not empty requestScope.selectedIndex}">
                                                tablaDocumentos.selectLinea(<c:out value="${requestScope.selectedIndex}"/>, true);
                                            </c:if>
                                        </script>
                                        <!-- GRID END -->
                                    </td>
                                </tr>
                                <tr>
                                    <td align="right">
                                        <%
                                            if(!"".equals(enlace)){
                                        %>
                                            <a id="muestraAsistente" href="<%=enlace%>?lang=<%=languageAsistente%>" target="blank" title='<%=descriptor.getDescripcion("enlaceAsistConfigPortafirmas")%>' alt='<%=descriptor.getDescripcion("enlaceAsistConfigPortafirmas")%>'><%=descriptor.getDescripcion("enlaceAsistConfigPortafirmas")%></a>
                                        <%
                                            }
                                        %>
                                    </td>
                                </tr>
                            </table>
                        </c:if>
                    </c:if>
                    </td>
                    </tr>
                    </table>
                    <c:if test="${requestScope.SearchDocumentoPortafirmasForm.doSearch}">
                        <c:if test="${requestScope.SearchDocumentoPortafirmasForm.totalCount > 0}" >
                            <div class="botoneraPrincipal">
                                <input type="button" class="botonMasLargo" name="btnDocAnexaExpediente" value="<fmt:message key='Portafirmas.VerDocumentacionAnexa.Expediente'/>" onclick="btnDocumentacionAnexaExpediente_click(this,this.form)"></td>
                                <input type="button" class="botonMasLargo" name="btnVerFirmas" value="<fmt:message key='Portafirmas.SearchDocumentoPortafirmasForm.VerFirmasDocumento'/>" onclick="btnVerFirmas_click(this,this.form)"></td>
                                <input type="button" class="botonLargo" name="btnVer" value="<fmt:message key='Portafirmas.SearchDocumentoPortafirmasForm.VerDocumento'/>" onclick="btnVer_click(this,this.form)"></td>
                                <input type="button" class="botonGeneral" name="btnFirmar" value="<fmt:message key='Buttons.sign'/>" onclick="btnFirmar_click(this,this.form)"></td>
                                <input type="button" class="botonGeneral" name="btnRechazar" value="<fmt:message key='Buttons.reject'/>" onclick="btnRechazar_click(this,this.form)"></td>                                                
                            </div>
                        </c:if>
                    </c:if>
                </html:form>
            </tiles:put>
        </tiles:insert>
    </tiles:put>
    <tiles:put name="finalJavascript" type="string">
        <script type="text/javascript" language="JavaScript">
            pleaseWait1("off",this);
            var comboEstadoFirma = new Combo("EstadoFirma");

            function cargarComboBox(cod, des){
                eval(auxCombo+".addItems(cod,des)");
            }

            var codEstado = new Array();
            var desEstado = new Array();
            codEstado[0] = "O";
            codEstado[1] = "F";
            codEstado[2] = "R";
            codEstado[3] = "S";

            desEstado[0] = "<fmt:message key="Sge.DocumentoFirmaForm.estadoFirma.pendiente"/>";
            desEstado[1] = "<fmt:message key="Sge.DocumentoFirmaForm.estadoFirma.firmado"/>";
            desEstado[2] = "<fmt:message key="Sge.DocumentoFirmaForm.estadoFirma.rechazado"/>";
            desEstado[3] = "<fmt:message key="Sge.DocumentoFirmaForm.estadoFirma.subsanado"/>";
            
            
            comboEstadoFirma.addItems(codEstado,desEstado);
            comboEstadoFirma.deactivate();

            function cambio(test){
                if (test.checked)
                    comboEstadoFirma.activate();
                else
                    comboEstadoFirma.deactivate();
            }

            <%String Agent = request.getHeader("user-agent");%>

                var coordx=0;
                var coordy=0;


            <%if(Agent.indexOf("MSIE")==-1) {%> //Que no sea IE
                window.addEventListener('mousemove', function(e) {
                    coordx = e.clientX;
                    coordy = e.clientY;
                }, true);
            <%}%>

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
                        if (comboEstadoFirma.base.style.visibility == "visible" && isClickOutCombo(comboEstadoFirma,coordx,coordy)) setTimeout('comboEstadoFirma.ocultar()',20);
                    }
                    if (teclaAuxiliar == 9){
                        comboEstadoFirma.ocultar();
                    }
                }
        </script>
    </tiles:put>
</tiles:insert>
