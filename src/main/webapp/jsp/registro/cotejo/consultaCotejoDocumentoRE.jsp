<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Consulta de cotejo electrónico de documento</title>

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
        <script type="text/javascript" src="<html:rewrite page='/scripts/jquery/plugins/jquery.placeholder.min.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/registro/cotejo/consultaCotejoDocumentoRE.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript">
            var opcionesViewConsultaCotejoDocumentoRE = {
                textos: {
                      errFormatoEmailIncorrecto: '<%=descriptor.getDescripcion("errFormatoEmailIncorrecto")%>'
                    , errEnviarEmail: '<%=descriptor.getDescripcion("errEnviarEmail")%>'
                    , msjEnviarEmailCorrecto: '<%=descriptor.getDescripcion("msjEnviarEmailCorrecto")%>'
                }
            }   
        </script>
    </head>

    <body class="bandaBody" onload="javascript:{
                viewConsultaCotejoDocumentoRE = new ViewConsultaCotejoDocumentoRE();
                viewConsultaCotejoDocumentoRE.inicializar(opcionesViewConsultaCotejoDocumentoRE);
                checkKeysLocal = viewConsultaCotejoDocumentoRE.checkKeysLocal;
            }">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        <form name="formulario" method="post">
            <input type="hidden" id="indexFichero" name="indexFichero" value="<c:out value="${requestScope.indexFichero}"/>" />
            
            <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiqCotejoElectronico")%></div>
            <div class="contenidoPantalla">
                <div class="contenidoInformacion">
                    <div class="contenidoInformacionFila row">
                        <div class="camposFormulario camposFormulario-inputUnico col-sm-12">
                            <label class="etiqueta"><%=descriptor.getDescripcion("gEtiqFechaCotejo")%>:</label> 
                            <input type="text" name="fechaCaptura" id="fechaCaptura" class="inputTexto inputText-overrideReadonly" value="<c:out value="${requestScope.fechaCaptura}"/>" readonly />
                        </div>
                        <div class="camposFormulario camposFormulario-inputUnico col-sm-12">
                            <label class="etiqueta"><%=descriptor.getDescripcion("etiqOrig")%>:</label>
                            <input type="text" name="origen" id="origen" class="inputTexto inputText-overrideReadonly" value="<c:out value="${requestScope.origen}"/>" readonly />
                        </div>
                        <div class="camposFormulario camposFormulario-inputUnico col-sm-12">
                            <label class="etiqueta"><%=descriptor.getDescripcion("gEtiqOrgano")%>:</label>
                            <input type="text" name="organo" id="organo" class="inputTexto inputText-overrideReadonly" value="<c:out value="${requestScope.organo}"/>" readonly />
                        </div>
                        <div class="camposFormulario camposFormulario-inputUnico col-sm-12">
                            <label class="etiqueta"><%=descriptor.getDescripcion("gEtiqTipoFirma")%>:</label>
                            <input type="text" name="tipoFirma" id="tipoFirma" class="inputTexto inputText-overrideReadonly" value="<c:out value="${requestScope.tipoFirma}"/>" readonly />
                        </div>
                    </div>
                    <div class="contenidoInformacionFila row">
                        <div id="columnaEmail" class="camposFormulario camposFormulario-inputUnico col-sm-12 hideVisibility">
                            <input type="text" name="correo" id="correo" class="inputTexto" placeholder="direccion@envio.com" />
                            <A href="javascript:return false;" onClick="return viewConsultaCotejoDocumentoRE.enviarEmail();">
                                <span class="fa fa-paper-plane-o" aria-hidden="true"  id="btnCorreo" name="btnCorreo" alt='<%=descriptor.getDescripcion("gEtiqFecha")%>'></span>
                            </A>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="botoneraPrincipal col-sm-12">
                        <input type= "button" class="botonGeneral fuente-9" value="<%=descriptor.getDescripcion("gbDescargarCopia")%>"
                               name="cmdDescargarCopia" onClick="viewConsultaCotejoDocumentoRE.pulsarDescargarCopia();" accesskey="D" />
                        <input type= "button" class="botonGeneral fuente-9" value="<%=descriptor.getDescripcion("gbEnviarPorEmail")%>"
                               name="cmdEnviarEmail" onClick="viewConsultaCotejoDocumentoRE.pulsarEnviarEmail();" accesskey="E" />
                        <input type= "button" class="botonGeneral fuente-9" value="<%=descriptor.getDescripcion("gbVerificarFirma")%>"
                               name="cmdVerificarFirma" onClick="viewConsultaCotejoDocumentoRE.pulsarVerificarFirma();" accesskey="V" />
                        <input type= "button" class="botonGeneral fuente-9" value="<%=descriptor.getDescripcion("gbSalir")%>"
                               name="cmdSalir" onClick="viewConsultaCotejoDocumentoRE.pulsarSalir();" accesskey="S" />
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>
