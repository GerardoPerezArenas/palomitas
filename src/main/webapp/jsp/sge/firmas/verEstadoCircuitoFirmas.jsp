<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
    <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Ver el estado del circuito de firmas</title>

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
        <script type="text/javascript" src="<html:rewrite page='/scripts/TablaNueva.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/sge/firmas/verEstadoCircuitoFirmas.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript">
            var opcionesViewVerEstadoCircuitoFirmas = {
                claveDocumento: '<c:out value="${requestScope.claveDocumento}" escapeXml="false" />',
                estadosFirmas: '<c:out value="${requestScope.listaEstadoFirmas}" escapeXml="false" />',
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
                    , eqTblVerFirmaOrden: '<%=descriptor.getDescripcion("etiqOrdenFirma")%>'
                    , eqTblVerFirmaUsuario: '<%=descriptor.getDescripcion("etiqUsuarioFirma")%>'
                    , eqTblVerFirmaEstado: '<%=descriptor.getDescripcion("etiqEstadoFirma")%>'
                    , eqEstadoPendiente: '<%=descriptor.getDescripcion("gEtiq_estadoFirmaP")%>'
                    , eqEstadoRechazado: '<%=descriptor.getDescripcion("gEtiq_estadoFirmaR")%>'
                    , eqEstadoFirmado: '<%=descriptor.getDescripcion("gEtiq_estadoFirmaF")%>'
                    , eqVerDatosFirma: '<%=descriptor.getDescripcion("etiqVerDatosFirma")%>'
                    , msjErrorInternoAplicacion: '<%=descriptor.getDescripcion("msjErrorInterno")%>'
                }
            }
        </script>
    </head>

    <body class="bandaBody" onload="javascript:{
                viewVerEstadoCircuitoFirmas = new ViewVerEstadoCircuitoFirmas();
                callFromTableTo = viewVerEstadoCircuitoFirmas.callFromTableToFlujosFirma;
                viewVerEstadoCircuitoFirmas.inicializar(opcionesViewVerEstadoCircuitoFirmas);
                checkKeysLocal = viewVerEstadoCircuitoFirmas.checkKeysLocal;
            }">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        <form name="formVerEstadoCircuitosFirma" method="post">
            <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiqEstadoCircuitoFirmas")%></div>
            <div class="contenidoPantalla">
                <div class="contenidoInformacion">
                    <div class="contenidoInformacionFila row">
                        <div id="tablaEstadoFirmas" class="col-sm-12"></div>
                    </div>
                </div>
                <div class="row">
                    <div class="botoneraPrincipal col-sm-12">
                        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSalir")%>"
                               id="cmdSalirVerFirma" name="cmdSalirVerFirma" onClick="viewVerEstadoCircuitoFirmas.pulsarSalir();" accesskey="S" />
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>
