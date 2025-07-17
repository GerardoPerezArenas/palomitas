<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.ResourceBundle"%>

<html>
    <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Seleccionar usuario de firma</title>

        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 1;
             int munic = 0;
            String css = "";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                css = usuarioVO.getCss();
                munic = usuarioVO.getOrgCod();
            }
            
            String PORTAFIRMAS  = "";
            try{
                ResourceBundle portafirmas = ResourceBundle.getBundle("Portafirmas");
                PORTAFIRMAS  = portafirmas.getString(munic+"/Portafirmas");
            }catch(Exception e){
              PORTAFIRMAS = "";
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
        <script type="text/javascript" src="<html:rewrite page='/scripts/sge/firmas/eleccionUsuarioFirmante.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript">
            var index = 0;
            var datosUsuariosFirma = [];
            <c:forEach items="${requestScope.listaUsuariosFirma}" var="usuarioFirma"> 
                    datosUsuariosFirma[index] = [
                        '<c:out value="${usuarioFirma.idUsuario}" />',
                        '<c:out value="${usuarioFirma.login}" />',
                        '<c:out value="${usuarioFirma.documento}" />',
                        <% if(PORTAFIRMAS!=null && !"".equals(PORTAFIRMAS) && "LAN".equals(PORTAFIRMAS)){ %>
                            '<c:out value="${usuarioFirma.buzonFirma}"/>',
                        <% }%>
                        '<c:out value="${usuarioFirma.nombre}" />'
                
                    ];
                    index++;
            </c:forEach>
            
            var opcionesViewEleccionUsuarioFirmante = {
                datosTabUsuariosFirma: datosUsuariosFirma,
                portafirmas: "<%=PORTAFIRMAS%>",
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
                    , eqTblUsuarioLogin: '<%=descriptor.getDescripcion("gEtiq_Usuario")%>'
                    , eqTblUsuarioNIF: '<%=descriptor.getDescripcion("gEtiq_NIF")%>'
                    , eqTblUsuarioBuzFir: '<%=descriptor.getDescripcion("gEtiq_BuzFir")%>'
                    , eqTblUsuarioNombre: '<%=descriptor.getDescripcion("gEtiq_Nombre")%>'
                    , msjNoSelecFila: '<%=descriptor.getDescripcion("msjNoSelecFila")%>'
                    , msjErrorInternoAplicacion: '<%=descriptor.getDescripcion("msjErrorInterno")%>'
                    , msjUsuarioNoExistePortafirmas: '<%=descriptor.getDescripcion("msjUsuarioNoExistePortafirmas")%>'
                    , msjUsuarioNoAnhadirFlujo: '<%=descriptor.getDescripcion("msjUsuarioNoAnhadirFlujo")%>'
                    , msjUsuarioYaExiste: '<%=descriptor.getDescripcion("msjUsuarioYaExiste")%>'
                }
            }   
        </script>
    </head>

    <body class="bandaBody" onload="javascript:{
                viewEleccionUsuarioFirmante = new ViewEleccionUsuarioFirmante();
                viewEleccionUsuarioFirmante.inicializar(opcionesViewEleccionUsuarioFirmante);
                checkKeysLocal = viewEleccionUsuarioFirmante.checkKeysLocal;
            }">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        <form name="formEleccionUsuarioFirmante" method="post">
            <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("titSelUsuario")%></div>
            <div class="contenidoPantalla">
                <div class="contenidoInformacion">
                    <div class="contenidoInformacionFila row">
                        <div id="tablaUsuariosFirma" class="col-sm-12"></div>
                    </div>
                </div>
                <div class="row">
                    <div class="botoneraPrincipal col-sm-12">
                        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAceptar")%>"
                               id="cmdAceptarCircuitoFirma" name="cmdAceptarCircuitoFirma" onClick="viewEleccionUsuarioFirmante.pulsarAceptar();" accesskey="A" />
                        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>"
                               id="cmdCancelarCircuitoFirma" name="cmdCancelarCircuitoFirma" onClick="viewEleccionUsuarioFirmante.pulsarCancelar();" accesskey="C" />
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>
