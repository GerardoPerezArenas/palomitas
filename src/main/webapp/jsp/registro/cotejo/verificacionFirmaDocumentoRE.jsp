<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Verificación de firma electrónica del documento</title>

        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 1;
            String css = "";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                css = usuarioVO.getCss();
            }
        %>

        <!-- Estilos -->
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>?version=<c:out value="${applicationScope.appVersion}"/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>?version=<c:out value="${applicationScope.appVersion}"/>" media="screen" >
        <link rel="stylesheet" href="<c:url value='/login/css/color_admin/assets/plugins/bootstrap/css/bootstrap.min.css'/>?version=<c:out value="${applicationScope.appVersion}"/>">
        <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap-override.css?version=<c:out value="${applicationScope.appVersion}"/>">
        <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>?version=<c:out value="${applicationScope.appVersion}"/>">

        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/jquery/jquery-1.9.1.min.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/registro/cotejo/verificacionFirmaDocumentoRE.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript">
            var opcionesViewVerificacionFirmaDocumentoRE = {
                textos: {}
            }   
        </script>
    </head>

    <body class="bandaBody" onload="javascript:{
                viewVerificacionFirmaDocumentoRE = new ViewVerificacionFirmaDocumentoRE();
                viewVerificacionFirmaDocumentoRE.inicializar(opcionesViewVerificacionFirmaDocumentoRE);
                checkKeysLocal = viewVerificacionFirmaDocumentoRE.checkKeysLocal;
            }">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        <form name="formulario" method="post">
            <input type="hidden" id="indexFichero" name="indexFichero" value="<c:out value="${requestScope.indexFichero}"/>" />
            
            <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiqVerificacionFirma")%></div>
            <div class="contenidoPantalla">
                <div class="contenidoInformacion">
                    <div class="contenidoInformacionFila row">
                        <div class="camposFormulario camposFormulario-inputUnico col-sm-12">
                            <p><strong><c:out value="${requestScope.msjEstadoFirmaCertificado}"/></strong></p>
                            <br/>
                        </div>
                        <c:if test="${requestScope.firmaValida == true}">
                            <div class="camposFormulario camposFormulario-inputUnico col-sm-12">
                                <label class="etiqueta"><%=descriptor.getDescripcion("gEtiqDatosCertificado")%>:</label> 
                                <textarea name="datosCertificado" id="datosCertificado" class="inputTexto textArea-overrideReadonly" rows="4" readonly><c:out value="${requestScope.datosCertificado}"/></textarea>
                            </div>
                            <div class="camposFormulario camposFormulario-inputUnico col-sm-12">
                                <label class="etiqueta"><%=descriptor.getDescripcion("gEtiqIdFirmante")%>:</label>
                                <input type="text" name="idFirmante" id="idFirmante" class="inputTexto inputText-overrideReadonly" value="<c:out value="${requestScope.idFirmante}"/>" readonly />
                            </div>
                            <div class="camposFormulario camposFormulario-inputUnico col-sm-12">
                                <label class="etiqueta"><%=descriptor.getDescripcion("gEtiqNombreFirmante")%>:</label>
                                <input type="text" name="nombreFirmante" id="nombreFirmante" class="inputTexto inputText-overrideReadonly" value="<c:out value="${requestScope.nombreFirmante}"/>" readonly />
                            </div>
                            <div class="camposFormulario camposFormulario-inputUnico col-sm-12">
                                <label class="etiqueta"><%=descriptor.getDescripcion("gEtiqEmisorCertificado")%>:</label>
                                <textarea name="emisorCertificado" id="emisorCertificado" class="inputTexto textArea-overrideReadonly" rows="4" readonly><c:out value="${requestScope.emisorCertificado}"/></textarea>         
                            </div>
                            <div class="camposFormulario camposFormulario-inputUnico col-sm-12">
                                <label class="etiqueta"><%=descriptor.getDescripcion("gEtiqValidezCertificado")%>:</label>
                                <input type="text" name="validezCertificado" id="validezCertificado" class="inputTexto inputText-overrideReadonly" value="<c:out value="${requestScope.validezCertificado}"/>" readonly />
                            </div>
                        </c:if>
                    </div>
                </div>
                <div class="row">
                    <div class="botoneraPrincipal col-sm-12">
                        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSalir")%>"
                               name="cmdSalir" onClick="viewVerificacionFirmaDocumentoRE.pulsarSalir();" accesskey="S" />
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>
