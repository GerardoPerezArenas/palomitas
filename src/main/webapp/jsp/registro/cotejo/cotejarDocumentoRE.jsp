<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.util.persistance.KeyValueObject"%>
<%@page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<html>
    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Cotejo electrónico de documento</title>

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

            List<KeyValueObject<Integer, String>> tiposDocumentales = (List<KeyValueObject<Integer, String>>) request.getAttribute("listaTipoDocumental");
            List<KeyValueObject<Integer, String>> tiposEstadoElaboraciones = (List<KeyValueObject<Integer, String>>) request.getAttribute("listaEstadoElaboracion");
            Integer valorDefectoTipoDocumental = (Integer) request.getAttribute("valorDefectoTipoDocumental");
            Integer valorDefectoEstadoElaboracion = (Integer) request.getAttribute("valorDefectoEstadoElaboracion");
            String tituloDoc = (String) request.getAttribute("tituloDoc");
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
        <script type="text/javascript" src="<html:rewrite page='/scripts/registro/cotejo/cotejarDocumentoRE.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript">
            var listaTipoDocumental = { codigo: [], descripcion: [] };
            var listaEstadoElaboracion = { codigo: [], descripcion: [] };
            
            <% for (int i = 0; i < tiposDocumentales.size(); i++) { %>
               listaTipoDocumental.codigo[<%=i%>] = [ '<%=(tiposDocumentales.get(i)).getKey()%>' ];
               listaTipoDocumental.descripcion[<%=i%>] = [ '<%=(tiposDocumentales.get(i)).getValue()%>' ];
            <% } %>
                
            <% for (int i = 0; i < tiposEstadoElaboraciones.size(); i++) { %>
               listaEstadoElaboracion.codigo[<%=i%>] = [ '<%=(tiposEstadoElaboraciones.get(i)).getKey()%>' ];
               listaEstadoElaboracion.descripcion[<%=i%>] = [ '<%=(tiposEstadoElaboraciones.get(i)).getValue()%>' ];
            <% } %>
            
            var opcionesViewCotejarDocumentoRE = {
                textos: {
                    msjErrorInternoCotejo: '<%=descriptor.getDescripcion("msjErrorInternoCotejo")%>'
                },
                listaTipoDocumental: listaTipoDocumental,
                listaEstadoElaboracion: listaEstadoElaboracion,
                valorDefectoTipoDocumental: '<%=valorDefectoTipoDocumental%>',
                valorDefectoEstadoElaboracion: '<%=valorDefectoEstadoElaboracion%>'
            }
        </script>
    </head>

    <body class="bandaBody" onload="javascript:{
                viewCotejarDocumentoRE = new ViewCotejarDocumentoRE();
                viewCotejarDocumentoRE.inicializar(opcionesViewCotejarDocumentoRE);
                checkKeysLocal = viewCotejarDocumentoRE.checkKeysLocal;
            }">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        <form name="formulario" method="post">
            <input type="hidden" id="tituloDoc" name="tituloDoc" value="<%=tituloDoc%>" />
            
            <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiqCotejoElectronico")%></div>
            <div class="contenidoPantalla">
                <div class="contenidoInformacion">
                    <div class="contenidoInformacionFila row">
                        <div class="camposFormulario camposFormulario-inputUnico col-sm-12">
                            <label class="etiqueta"><%=descriptor.getDescripcion("etiqTipoDocumental")%>:</label>
                            <input type="hidden" name="codTipoDocumental" id="codTipoDocumental" class="inputTextoObligatorio" />
                            <input type="text" name="descTipoDocumental" id="descTipoDocumental" class="inputTextoObligatorio inputText-overrideReadonly" readonly />
                            <a href="" style="text-decoration:none;" id="anchorTipoDocumental" name="anchorTipoDocumental">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoDocumental" name="botonTipoDocumental" alt= "<%=descriptor.getDescripcion("altTipoDocumental")%>" title= "<%=descriptor.getDescripcion("altTipoDocumental")%>" style="cursor:hand;"></span>
                            </a>
                        </div>
                        <div class="camposFormulario camposFormulario-inputUnico col-sm-12">
                            <label class="etiqueta"><%=descriptor.getDescripcion("etiqEstadoElaboracion")%>:</label>
                            <input type="hidden" name="codEstadoElaboracion" id="codEstadoElaboracion" class="inputTextoObligatorio" />
                            <input type="text" name="descEstadoElaboracion" id="descEstadoElaboracion" class="inputTextoObligatorio inputText-overrideReadonly" readonly />
                            <a href="" style="text-decoration:none;" id="anchorEstadoElaboracion" name="anchorEstadoElaboracion">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonEstadoElaboracion" name="botonEstadoElaboracion" alt= "<%=descriptor.getDescripcion("altEstadoElaboracion")%>" title= "<%=descriptor.getDescripcion("altEstadoElaboracion")%>" style="cursor:hand;"></span>
                            </a>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="botoneraPrincipal col-sm-12">
                        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCotejar")%>"
                               name="cmdCotejar" onClick="viewCotejarDocumentoRE.pulsarCotejar();" accesskey="C" />
                        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSalir")%>"
                               name="cmdSalir" onClick="viewCotejarDocumentoRE.pulsarSalir();" accesskey="S" />
                    </div>
                </div>
            </div>
        </form>
    </body>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaCombo.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
</html>
