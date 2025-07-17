<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title> Grabar Notificacion Tramite </title>
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

            String  exitoGrabarNotificacion=(String) session.getAttribute("exitoGrabarNotificacion");
            String opcion=(String) session.getAttribute("opcion");
            //String textoFirmar= StringEscapeUtils.escapeJavascript((String) session.getAttribute("textoFirmar"));
            String exitoFirmarNotificacion=(String) session.getAttribute("exitoFirmarNotificacion");
            String exitoEnvio=(String) session.getAttribute("exitoEnvio");
            String nombreUsuarioFirmanteNotificacion=(String) session.getAttribute("nombreUsuarioFirmanteNotificacion");
         
                           

    %>

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>


   <script type="text/javascript">
    function redirecciona(){

        var exitoGrabarNotificacion='<%=exitoGrabarNotificacion%>';
        var exitoFirmarNotificacion='<%=exitoFirmarNotificacion%>';
        var exitoEnvio='<%=exitoEnvio%>';
        var opcion='<%=opcion%>';
        
        
        var mensaje='';
        var aux=0;

        if(opcion=="firmar")
        {
            
            if(exitoFirmarNotificacion=="true")
            {
                var codNotif='<%=(Integer) session.getAttribute("codNotificacion")%>';
            

                /***** SE COMPRUEBA SI SE ACEPTA LA FIRMA CON CERTIFICADO DE ORGANISMO, SI ES ASÍ SE PREGUNTA AL USUARIO QUE ELIJA
                 ***** CON QUE TIPO DE CERTIFICADO DESEA FIRMAR ***/
                var CERT_ORGANISMO = parent.mainFrame.admiteFirmaCertificadoOrganismo;
            
                if(CERT_ORGANISMO!=null && CERT_ORGANISMO=="true"){

                    if(jsp_alerta('C','<%=descriptor.getDescripcion("msgTipoCertificadoNotificacion")%>')){
                        parent.mainFrame.barraProgresoDocumento('on');
                        parent.mainFrame.firmarNotificacionCertificadoOrganismo();
                    }else
                        parent.mainFrame.firmarNotificacion();
                }else
                        parent.mainFrame.firmarNotificacion();
           

            }
            else if(exitoFirmarNotificacion=="false")
            {
                 mensaje="<%=descriptor.getDescripcion("msjCertNoValido")%>";
                 jsp_alerta('A',mensaje);
            }
            else
            {
                 mensaje="<%=descriptor.getDescripcion("msjNotifDebeFirmaPor")%>"+" "+'<%=nombreUsuarioFirmanteNotificacion%>'+ " "+"<%=descriptor.getDescripcion("msjOUsuDel")%>";
                 jsp_alerta('A',mensaje);
            }
            

               
             
        }
        else if (opcion=="guardar")
        {
            if(exitoGrabarNotificacion=="true")
            {
                mensaje="<%=descriptor.getDescripcion("msj_guardarNotificacionSi")%>";
                //aux=1;
                parent.mainFrame.mostrarMensajeGrabacionNotificacionDatos(aux,mensaje);
                
                
            }

            else
            {
                mensaje="<%=descriptor.getDescripcion("msj_guardarNotificacionError")%>";
                aux=0;
                parent.mainFrame.mostrarMensajeGrabacionNotificacionDatos(aux,mensaje);
            }
           
        }
         else if (opcion=="guardaFirma")
        {
        
            if(exitoFirmarNotificacion=="firmaFail")
            {
                mensaje="<%=descriptor.getDescripcion("msjCertNoValido")%>";


            }

            else if(exitoFirmarNotificacion=="firmaSave")
            {
                mensaje="<%=descriptor.getDescripcion("msjGuardarFirma")%>";
              
            }
            else if(exitoFirmarNotificacion=="firmaSaveFail")
            {
                mensaje="<%=descriptor.getDescripcion("msjErrorGuardarFirma")%>";

            }
            else
            {
                mensaje="<%=descriptor.getDescripcion("msjErrorGuardarFirma")%>";

            }
            jsp_alerta('A',mensaje);

        }
        
        else if (opcion=="enviar")
        {
            if(exitoEnvio=="envioSucess")
            {             
                mensaje="<%=descriptor.getDescripcion("msjEnvioNotif")%>";
                jsp_alerta('A',mensaje);
                parent.mainFrame.postNotificacion();
            }

            else if(exitoEnvio=="envioFail")
            {
                mensaje="<%=descriptor.getDescripcion("msjErrorEnvioNotif")%>";
                jsp_alerta('A',mensaje);
                parent.mainFrame.barraProgresoDocumento("off");
            }
            else if(exitoEnvio=="noFirmado")
            {
                mensaje="<%=descriptor.getDescripcion("msjEnvioSinFirmar")%>";
                jsp_alerta('A',mensaje);
                parent.mainFrame.barraProgresoDocumento("off");

            }
            else 
            {
                mensaje="<%=descriptor.getDescripcion("msjErrorEnvioNotif")%>";
                jsp_alerta('A',mensaje);
                parent.mainFrame.barraProgresoDocumento("off");

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