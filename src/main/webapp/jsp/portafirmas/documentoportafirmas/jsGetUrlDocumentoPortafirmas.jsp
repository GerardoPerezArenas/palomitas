<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject,
         es.altia.common.service.config.Config,
         es.altia.common.service.config.ConfigServiceHelper,
         es.altia.util.struts.StrutsUtilOperations"%>
<%

    String[] parametros = null;
    if (session != null) {
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        if (usuario != null) {
            parametros = usuario.getParamsCon();
        }
    }

    String urlMaquina = "";
    Config conf = ConfigServiceHelper.getConfig("common");
    String maquinaVirtual = conf.getString("hostVirtual");

    try {

        // Si se utiliza la anterior, hay que definir en el fichero de hosts, la propiedad que se defina en el common.properties
        String protocolo = StrutsUtilOperations.getProtocol(request);
        if (maquinaVirtual.equals("")) {
            urlMaquina = protocolo + "://" + request.getHeader("host");
        } else {
            urlMaquina = maquinaVirtual;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
%>

function getUrlForDownloadDocument(idMunicipio, idNumeroExpediente, idTramite, idOcurrenciaTramite, 
    idNumeroDocumento, nombreDocumento, historico, editorTexto) {
    var url = "<%=urlMaquina%>" + "<%=request.getContextPath()%>";
    url = url + "/temp/expedientes/<%=parametros[0]%>/<%=parametros[6]%>/" + idMunicipio + "/" +
    idNumeroExpediente.replace("/","+").replace("/","+") + "/" +
    idTramite + "/" + idOcurrenciaTramite + "/" +
    idNumeroDocumento + "/" + nombreDocumento + getExtensionForPlantilla(editorTexto);
    
    if(historico!=undefined && historico!=null && historico=='true'){ 
        url += "/historico";
    }else 
        url += "/activo";
    
    url += "?jsessionid=" + "<%=session.getId()%>";
    
    return url;
}

function getUrlForDownloadRelationDocument(idMunicipio, idNumeroExpediente, idTramite, 
    idOcurrenciaTramite, idNumeroDocumento, nombreDocumento, editorTexto) {
var url = "<%=urlMaquina%>" + "<%=request.getContextPath()%>";
url = url + "/temp/relaciones/<%=parametros[0]%>/<%=parametros[6]%>/" + idMunicipio + "/" +
idNumeroExpediente.replace("/","+").replace("/","+") + "/" +
idTramite + "/" + idOcurrenciaTramite + "/" +
idNumeroDocumento + "/" + nombreDocumento + getExtensionForPlantilla(editorTexto) +
"?jsessionid=" + "<%=session.getId()%>";
return url;
}


function getUrlForDownloadExternalDocument(codigoDocumento){
var url = "<%=urlMaquina%>" + "<%=request.getContextPath()%>" + "/MostrarDocumentoExternoPortafirmas.do?codDocumento=" + codigoDocumento;
return url;
}

function getUrlForDownloadProcedimientoDocument (codMunicipio, ejercicio, numExpediente, codigoDocumento, codProcedimiento){

var parametros = "?codigoDocumento=" + codigoDocumento + "&codMunicipio=" + codMunicipio +
"&ejercicio=" + ejercicio + "&numExpediente=" + numExpediente +
"&codProcedimiento=" + codProcedimiento;    
var url = "<%=urlMaquina%>" + "<%=request.getContextPath()%>" + "/VerDocumentoPresentado" + parametros;
return url;
}


function getUrlForDownloadDocExtNotificacion(codDocumento,numExpediente,codTramite,ocurrenciaTramite,codProcedimiento,ejercicio,codMunicipio){
    var parametros = "?codDocumento=" + codDocumento + "&numExpediente=" + numExpediente + "&codTramite=" + codTramite + "&ocurrenciaTramite=" + ocurrenciaTramite + "&codProcedimiento=" + codProcedimiento + "&ejercicio=" + ejercicio + "&codMunicipio=" + codMunicipio;    
    var url = "<%=urlMaquina%>" + "<%=request.getContextPath()%>" + "/MostrarDocumentoExternoNotificacionElectronica.do" + parametros;
    return url;
}

function getUrlForDownloadNotificacion(codNotificacion){
    var parametros = "&codNotificacion=" + codNotificacion;
    var url = "<%=urlMaquina%>" + "<%=request.getContextPath()%>" + "/Notificacion.do?opcion=descargarNotificacionXML&codNotificacion=" + codNotificacion;
    return url;
}

function getUrlForDownloadDocumentServlet(codMunicipio, numeroExpediente, codTramite,
    ocurrenciaTramite, numeroDocumento, expHistorico) {
    
    // Construimos el codigo para el servlet de visualización de documentos:
    // DOC-<codMunicipio>-<numExpediente>-<codTramite>-<ocuTramite>-<numDocumento>
    var codigo = "DOC-"+codMunicipio+"-"+numeroExpediente+"-"+codTramite+"-"+ocurrenciaTramite+"-"+numeroDocumento;

    var url = "<%=urlMaquina%><%=request.getContextPath()%>/VerDocumentoDatosSuplementarios;" +
        "jsessionid=" + '<%=session.getId()%>' +
        "?codigo=" + codigo +
        "&opcion=7" +
        "&expHistorico="+expHistorico +
        "&embedded=false";
       
    return url;
}

function getExtensionForPlantilla(tipoPlantilla) {
    var extension = "";
    if (tipoPlantilla=="OOFFICE") {
        extension = ".odt";
    } else if (tipoPlantilla=="WORD") {
        extension = ".doc";
    }
    
    return extension;
}