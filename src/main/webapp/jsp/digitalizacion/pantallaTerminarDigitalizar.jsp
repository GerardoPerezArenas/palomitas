<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Terminar de digitalizar documentos</title>

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
        <link rel="stylesheet" type="text/css" href="<c:url value='/login/css/color_admin/assets/plugins/bootstrap/css/bootstrap.min.css'/>?version=<c:out value="${applicationScope.appVersion}"/>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap-override.css'/>?version=<c:out value="${applicationScope.appVersion}"/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>?version=<c:out value="${applicationScope.appVersion}"/>" media="screen">

        <!-- JS Scripts -->
        <script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/jquery/jquery-1.9.1.min.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/digitalizacion.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript">
            var ventanaComponente = null;
            
            function abrirComponente(){
              var argVentana = self.parent.opener.xanelaAuxiliarArgs;
              // Abrimos el componente en una ventana separada para evitar problemas
              // de contenido mixto y de dominio cruzado
              ventanaComponente = mostrarComponenteDigitalizacion(argVentana.url, "ventanaFinDigitalizacion", screen.width, screen.height);
            }
            
            function pulsarAceptarTerminarDigitalizar(){
                if(ventanaComponente.closed)
                    volverPantallaPrincipal();
                else {
                    jsp_alerta("A","<%= descriptor.getDescripcion("msgErrDigitAnotFinSI")%>");
                }
            }
            
            $(window).on("beforeunload", function(e) { 
                if(!ventanaComponente.closed){
                    e.preventDefault();
                    pulsarAceptarTerminarDigitalizar();
                }
                
            })
        </script>
    </head>

    <body class="bandaBody" onload="javascript:{abrirComponente();}">
        <form name="formTerminarDigitalizar" method="post">
            <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("tituloTerminarDigitalizar")%></div>
            <div class="contenidoPantalla">
                <div class="contenidoInformacion">
                    <div class="contenidoInformacionFila row">
                        <p id="textoTerminarDigitalizacion" class="col-sm-12"><%= descriptor.getDescripcion("msgTerminarDigitalizar")%></p>
                    </div>
                </div>
                <div class="row">
                    <div class="botoneraPrincipal col-sm-12" style="width: 100%; text-align: center;">
                        <input type= "button" class="botonLargo" value="<%=descriptor.getDescripcion("gbVolverRegistro")%>"
                               id="cmdAceptarTerminarDigitalizacion" name="cmdAceptarTerminarDigitalizacion" onClick="pulsarAceptarTerminarDigitalizar();" accesskey="T" />
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>
