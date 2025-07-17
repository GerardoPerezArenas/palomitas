<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
    <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Seleccionar flujo de firma</title>

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
        <script type="text/javascript" src="<html:rewrite page='/scripts/sge/firmas/eleccionFlujoFirma.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript">
            var index = 0;
            var datosTabFlujosFirma = [];
            <c:forEach items="${requestScope.listaFlujoUsuariosFirma}" var="flujoUsuario">
                var idFlujo = '<c:out value="${flujoUsuario.flujo.id}" />';
                var nombreFlujo = '<c:out value="${flujoUsuario.flujo.nombre}" />';
                var numLinea = 0;
                var usuarios = '';
                <c:forEach items="${flujoUsuario.usuariosCircuito}" var="usuarios">
                    usuarios += '<c:out value="${usuarios.nombreUsuario}" />;';
                    numLinea++;
                </c:forEach>
                usuarios = usuarios.slice(0, -1);
                
                datosTabFlujosFirma[index] = [
                    idFlujo,
                    nombreFlujo,
                    numLinea,
                    usuarios
                ];
                index++;
            </c:forEach>
            
            var opcionesViewEleccionFlujoFirma = {
                datosTabFlujosFirma: datosTabFlujosFirma,
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
                    , eqTblFlujoNombre: '<%=descriptor.getDescripcion("etiqFlujo")%>'
                    , eqTblFlujoNumero: '<%=descriptor.getDescripcion("etiqNumero")%>'
                    , eqTblNombresUsuarios: '<%=descriptor.getDescripcion("etiqNombres")%>'
                    , msjNoSelecFila: '<%=descriptor.getDescripcion("msjNoSelecFila")%>'
                }
            }   
        </script>
    </head>

    <body class="bandaBody" onload="javascript:{
                viewEleccionFlujoFirma = new ViewEleccionFlujoFirma();
                viewEleccionFlujoFirma.inicializar(opcionesViewEleccionFlujoFirma);
                checkKeysLocal = viewEleccionFlujoFirma.checkKeysLocal;
            }">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        <form name="formEleccionFlujoFirma" method="post">
            <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("titSelFlujo")%></div>
            <div class="contenidoPantalla">
                <div class="contenidoInformacion">
                    <div class="contenidoInformacionFila row">
                        <div id="tablaFlujosFirma" class="col-sm-12"></div>
                    </div>
                </div>
                <div class="row">
                    <div class="botoneraPrincipal col-sm-12">
                        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAceptar")%>"
                               id="cmdAceptarFlujoFirma" name="cmdAceptarFlujoFirma" onClick="viewEleccionFlujoFirma.pulsarAceptar();" accesskey="A" />
                        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>"
                               id="cmdCancelarFlujoFirma" name="cmdCancelarFlujoFirma" onClick="viewEleccionFlujoFirma.pulsarCancelar();" accesskey="C" />
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>
