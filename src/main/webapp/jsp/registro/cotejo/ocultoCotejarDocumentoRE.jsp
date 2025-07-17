<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<html>
<head>
    <jsp:include page="/jsp/formularios/tpls/app-constants.jsp" />
    <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
    <title>Oculto Cotejar Documentos</title>
    <script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-1.9.1.min.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/miniapplet.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
    <%
        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        int idioma = 1;
        int apl = 1;
        if (session.getAttribute("usuario") != null) {
            usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
            apl = usuarioVO.getAppCod();
            idioma = usuarioVO.getIdioma();
        }
    %>
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    
    <script type="text/javascript">
        var URL_FICHERO = APP_CONTEXT_PATH + '/registro/DocumentoRegistro.do?&opcion=obtenerFicheroTemporalCotejo&tituloDoc=' + '<c:out value="${requestScope.tituloDoc}"/>';
		var URL_AUTOFIRMA = '<c:out value="${requestScope.hostVirtual}"/>' + APP_CONTEXT_PATH;
        var msjErroresDetectados = null;
        <c:if test="${not empty requestScope.MENSAJE_ERRORES_COTEJO}">
            msjErroresDetectados = '<c:out value="${requestScope.MENSAJE_ERRORES_COTEJO}"/>';
        </c:if>
        
        // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
        function inicializar(){
            if (!msjErroresDetectados) {
                MiniApplet.cargarAppAfirma(URL_AUTOFIRMA);
                MiniApplet.downloadRemoteData(URL_FICHERO, successDescargarFichero, errorDescargarFichero);
            } else {
                volverMainFrame(msjErroresDetectados);
            }
        };

        // Callbacks para la descarga del fichero
        function successDescargarFichero(datosB64) {
            if (datosB64) {
				MiniApplet.checkTime(MiniApplet.CHECKTIME_RECOMMENDED, 60000, URL_AUTOFIRMA);
                MiniApplet.sign(datosB64,
                                '<c:out value="${requestScope.algoritmoCifrado}"/>',
                                '<c:out value="${requestScope.formatoFichero}"/>',
                                null,
                                successFirma,
                                errorFirma);
            } else {
                errorDescargarFichero();
            }
        };

        function errorDescargarFichero() {
            var msjError = '<%=descriptor.getDescripcion("msjErrorInternoCotejo")%>';
            
            volverMainFrame(msjError);
        };
        
        // Callbacks para la firma del fichero
        function successFirma(signatureB64) {
            $('#firmaBase64').val(signatureB64);
            verificarFirma();
        };
        
        function errorFirma(error) {
            var msjError = '';
            if (error && !error.includes("AOCancelledOperationException")) {
                msjError = '<%=descriptor.getDescripcion("msjErrorFirmarDocumento")%>';
            }
    
            $('#firmaBase64').val(null);
            volverMainFrame(msjError);
        };
        
        function verificarFirma() {
            var criterio = {};
            criterio.opcion = 'verificarOperacionCotejo';
            criterio.firmaBase64 = $('#firmaBase64').val();
            criterio.tituloDoc = $('#tituloDoc').val();
            
            var desfaseSegundos = <c:out value="${requestScope.desfaseEnvioServidor}"/> * 1000;
            setTimeout(function() {
                $.ajax({
                    url: APP_CONTEXT_PATH + '/registro/DocumentoRegistro.do',
                    type: 'POST',
                    async: true,
                    data: criterio,
                    success: successVerificarFirma,
                    error: errorVerificarFirma
                });
            }, desfaseSegundos);
        }
        
        // Callbacks para la verificacion de la firma
        function successVerificarFirma() {
            parent.mainFrame.viewCotejarDocumentoRE.finalizarCotejo($('#firmaBase64').val());
        };
        
        function errorVerificarFirma(error) {
            var msjError = error.responseText;
            $('#firmaBase64').val(null);
            
            volverMainFrame(msjError);
        };
        
        function volverMainFrame(msjError) {
            parent.mainFrame.viewCotejarDocumentoRE.mostrarMsjError(msjError);
        }
    </script>
</head>
<body onload="inicializar();">
    <form name="formulario" method="post">
        <input type="hidden" id="tituloDoc" name="tituloDoc" value="<c:out value="${requestScope.tituloDoc}"/>" />
        <input type="hidden" id="firmaBase64" name="firmaBase64" />
    </form>
</body>
</html>
