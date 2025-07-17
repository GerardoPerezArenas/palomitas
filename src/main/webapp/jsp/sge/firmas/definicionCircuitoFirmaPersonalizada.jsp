<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
    <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Definición del circuito del flujo de firma personalizados</title>

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

        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        
        <!-- Estilos -->
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>?version=<c:out value="${applicationScope.appVersion}"/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<c:url value='/scripts/DataTables/datatables.min.css'/>?version=<c:out value="${applicationScope.appVersion}"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value='/login/css/color_admin/assets/plugins/bootstrap/css/bootstrap.min.css'/>?version=<c:out value="${applicationScope.appVersion}"/>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap-override.css'/>?version=<c:out value="${applicationScope.appVersion}"/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>?version=<c:out value="${applicationScope.appVersion}"/>" media="screen">

        <!-- JS Scripts -->
        <script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/jquery/jquery-1.9.1.min.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/DataTables/datatables.min.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/listas.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/listaComboBox.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/TablaNueva.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/sge/firmas/definicionCircuitoFirmaPersonalizada.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript">
            // Convertir listado de tipos de firma a javascript
            var index = 0;
            var datosTipoFirma = { codigo: [], descripcion: [] };
            <c:forEach items="${requestScope.listaFirmaTipo}" var="firmaTipo"> 
                    datosTipoFirma.codigo[index] = '<c:out value="${firmaTipo.id}" />';
                    datosTipoFirma.descripcion[index] = '<c:out value="${firmaTipo.nombre}" />';
                    index++;
            </c:forEach>
            
            var opcionesViewDefinicionCircuitoFirmaPersonalizada = {
                datosFlujoUsuariosFirma: '<c:out value="${requestScope.firmaFlujoUsuarios}" escapeXml="false" />',
                datosComboTipoFirma: datosTipoFirma,
                textos: {
                      buscar: '<%=descriptor.getDescripcion("buscar")%>'
                    , anterior: '<%=descriptor.getDescripcion("anterior")%>'
                    , siguiente: '<%=descriptor.getDescripcion("siguiente")%>'
                    , mosFilasPag: '<%=descriptor.getDescripcion("mosFilasPag")%>'
                    , msgNoResultBusq: '<%=descriptor.getDescripcion("msgNoResultBusq")%>'
                    , mosPagDePags: '<%=descriptor.getDescripcion("mosPagDePags")%>'
                    , noRegDisp: '<%=descriptor.getDescripcion("noRegDisp")%>'
                    , filtrDeTotal: '<%=descriptor.getDescripcion("filtrDeTotal")%>'
                    , primero: '<%=descriptor.getDescripcion("primero")%>'
                    , ultimo: '<%=descriptor.getDescripcion("ultimo")%>'
                    , eqTblCircuitosFirmaOrden: '<%=descriptor.getDescripcion("etiq_orden")%>'
                    , eqTblCircuitosFirmaUsuario: '<%=descriptor.getDescripcion("gEtiq_Usuario")%>'
                    , eqTblCircuitosFirmaDocumento: '<%=descriptor.getDescripcion("gEtiqDocumento")%>'
                    , etiqModoFirma: '<%=descriptor.getDescripcion("etiqModoFirma")%>'
                    , msjNoSelecFila: '<%=descriptor.getDescripcion("msjNoSelecFila")%>'
                    , msjErrorInternoAplicacion: '<%=descriptor.getDescripcion("msjErrorInterno")%>'
                    , msjTipoFlujoFirmaObligatorio: '<%=descriptor.getDescripcion("msjTipoFlujoFirmaObligatorio")%>'
                    , preguntaEliminarUsuarioCircuitoFirma: '<%=descriptor.getDescripcion("preguntaElimUserCircuitoFirma")%>'
                    , msjCircuitoFirmaUsuarioOblig: '<%=descriptor.getDescripcion("msjCircuitoFirmaUsuarioOblig")%>'
                }
            }
        </script>
    </head>

    <body class="bandaBody" onload="javascript:{
                viewDefinicionCircuitoFirmaPersonalizada = new ViewDefinicionCircuitoFirmaPersonalizada();
                viewDefinicionCircuitoFirmaPersonalizada.inicializar(opcionesViewDefinicionCircuitoFirmaPersonalizada);
                comprobarUsuarioDuplicado = viewDefinicionCircuitoFirmaPersonalizada.comprobarUsuarioDuplicado;
                checkKeysLocal = viewDefinicionCircuitoFirmaPersonalizada.checkKeysLocal;
            }">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        <form name="formDefinicionCircuitoFirmaPersonalizada" method="post">
            <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiqMantCircuitoFirmas")%></div>
            <div class="contenidoPantalla">
                <div class="contenidoInformacion">
                    <div class="contenidoInformacionFila row">
                        <div id="radioTipoFirma" class="col-sm-12 radiobuttons radiobuttons-horizontal"></div>
                    </div>
                    <div class="contenidoInformacionFila row">
                        <div id="tablaCircuitosFirmaPersonalizada" class="col-sm-12"></div>
                    </div>
                </div>
                <div class="row">
                    <div class="botoneraPrincipal col-sm-12">
                        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAlta")%>"
                               id="cmdAltaCircuitoFirma" name="cmdAltaCircuitoFirma" onClick="viewDefinicionCircuitoFirmaPersonalizada.pulsarAltaFirmanteCircuitoFirma();" accesskey="A" />
                        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>"
                               id="cmdEliminarCircuitoFirma" name="cmdEliminarCircuitoFirma" onClick="viewDefinicionCircuitoFirmaPersonalizada.pulsarEliminarFirmanteCircuitoFirma();" accesskey="E" />
                        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAceptar")%>"
                               id="cmdAceptarCircuitoFirma" name="cmdAceptarCircuitoFirma" onClick="viewDefinicionCircuitoFirmaPersonalizada.pulsarAceptarCircuitoFirma();" accesskey="P" />
                        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>"
                               id="cmdCancelarCircuitoFirma" name="cmdCancelarCircuitoFirma" onClick="viewDefinicionCircuitoFirmaPersonalizada.pulsarCancelarCircuitoFirma();" accesskey="C" />
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>
