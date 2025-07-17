<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title> Notificaciones </title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

    <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma=1;
      int apl=1;

            if (session.getAttribute("usuario") != null){
                    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
                    idioma = usuarioVO.getIdioma();
                    apl = usuarioVO.getAppCod();
            }


            String existenDocumentos = (String)request.getAttribute("existenDocumentos");
            String existenInteresados = (String)request.getAttribute("existenInteresados");
            String codTramite = (String)request.getAttribute("codTramite");
            String ocurrenciaTramite = (String)request.getAttribute("ocurrenciaTramite");
            String codUnidadTramitadoraTram = (String)request.getAttribute("codUnidadTramitadoraTram");
            String urlPantallaDatosNotificacion=(String)request.getAttribute("urlPantallaDatosNotificacion");
            String estadoNotif=(String)request.getAttribute("estadoNotif");
            String existenDocumentosPendientesFirma=(String)request.getAttribute("existenDocumentosPendientesFirma");
            


    %>

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>


   <script type="text/javascript">
       var frame;
      if(parent.mainFrame){
        frame = parent.mainFrame;
      } else {
        frame = parent;
      }
    function redirecciona(){

        //var existenDocumentos = "<%=existenDocumentos%>";
        var estadoNotif = "<%=estadoNotif%>";
        var existenInteresados = "<%=existenInteresados%>";
         var existenDocumentosPendientesFirma = "<%=existenDocumentosPendientesFirma%>";
        var urlPantallaDatosNotificacion = "<%=urlPantallaDatosNotificacion%>";
        var mensaje="";
       
       if(existenDocumentosPendientesFirma=='si')
       {
           jsp_alerta('A', 'No se puede finalizar el trámite. Tiene documentos pendientes de firma. No es posible enviar la notificación');
       } else{
       
        if (estadoNotif=="enviada")
        {   
            frame.finalizaTramitePostNotificacion();
        }
        else
       {
                frame.mostrarVentanaNotificacionDatos(urlPantallaDatosNotificacion);
        
       }
   }

        

    }
    </script>
</head>
<body onLoad="redirecciona();">
    <form>
    <input type="hidden" name="opcion" value="">
    </form>
    <p>&nbsp;<p><center>
</body>
</html>