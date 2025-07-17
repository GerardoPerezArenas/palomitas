<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ page import="es.altia.agora.technical.ConstantesDatos"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="es.altia.flexia.notificacion.vo.AdjuntoNotificacionVO"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.notificacion.form.NotificacionForm" %>

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

        String opcion = (String)request.getAttribute("ADJUNTO_ARCHIVO_OPCION");
        String sBytes  = ConstantesDatos.DESCRIPCION_MEGABYTES;


        NotificacionForm form = (NotificacionForm) session.getAttribute("notificacionForm");
    %>  
    
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    


   <script type="text/javascript">
    function redirecciona(){
        var opcion='<%=opcion%>';        
        var listaAdjuntosExternos = new Array();
        var listaAdjuntosExternosTabla = new Array();
        var h = 0;
        <%    
            ArrayList<AdjuntoNotificacionVO> adjuntosExternos = (ArrayList<AdjuntoNotificacionVO>)request.getAttribute("LISTA_ADJUNTOS_EXTERNOS");

            for(int i=0;adjuntosExternos!=null && i<adjuntosExternos.size();i++){
                int codExterno  = adjuntosExternos.get(i).getIdDocExterno();
                String fileNameExt = adjuntosExternos.get(i).getNombre();                        
                String estadoFirma = adjuntosExternos.get(i).getEstadoFirma();
                String fileTypeExt = adjuntosExternos.get(i).getContentType();
                String descEstadoFirma = "";
                String enlace = "";
                int tipoCertificado = adjuntosExternos.get(i).getTipoCertificadoFirma();
                String descTipoCertificado = "";
                if(estadoFirma!=null && estadoFirma.equalsIgnoreCase("O")){
                    descEstadoFirma = descriptor.getDescripcion("etiqPendiente");                
                }else
                if(estadoFirma!=null && estadoFirma.equalsIgnoreCase("F")){
                    descEstadoFirma = descriptor.getDescripcion("etiqFirmado");                
                }    
                
                if(estadoFirma!=null && estadoFirma.equals("F")){
                    descEstadoFirma = descriptor.getDescripcion("etiqFirmado") + "&nbsp;&nbsp;(<a href=\"#\" onclick=\"javascript:verFirma(" + codExterno + ");\">" + descriptor.getDescripcion("titVerificarFirma") + "</a>)";
                    if(tipoCertificado==0)
                        // Certificado de organismo
                        descTipoCertificado = descriptor.getDescripcion("etiqCertificadoOrganismo");
                    else
                        //Certificado de usuario
                        descTipoCertificado = descriptor.getDescripcion("etiqCertificadoUsuario");
                }
                if(estadoFirma!=null && estadoFirma.equals("R")) descEstadoFirma = descriptor.getDescripcion("etiqRechazo");
                if(estadoFirma!=null && estadoFirma.equals("O") && !"F".equals(form.getEstadoNotificacion())){
                    enlace = "<a href=\"#\" onclick=\"javascript:enviarFirmar(" + codExterno + "," + i + ");\">Firmar</a>";
                }
    %>         
            listaAdjuntosExternos[h] = ['<%=fileNameExt%>','<%=estadoFirma%>','<%=codExterno%>'];     
            listaAdjuntosExternosTabla[h] = ['<%=fileNameExt%>','<%=descEstadoFirma%>','<%=descTipoCertificado%>'];
             <%--Si se quiere añadir de nuevo la columna "Enlace" con el enlace para firmar añadir ,'<%=enlace%>' en la parte final de este array.--%>
            h++;
    <%         
        }// for
    %>        
    
        if(opcion!=null && opcion=="ADJUNTAR"){
            var msgError = "";            
            
            <c:if test="${requestScope.FICHERO_EXISTE eq 'si'}">   
                msgError = '<%=descriptor.getDescripcion("msgFichNotifExiste") %>';
            </c:if>        
        
            <c:if test="${requestScope.EXTENSION_FILE_INCORRECT eq 'si' and requestScope.TAM_MAX_FILE_EXCEED eq 'si'}">        
                msgError = '<%=descriptor.getDescripcion("etiqTamMaxExceed")%> <c:out value='${requestScope.TAM_MAX_FILE_BYTE}'/> <%= sBytes %> <br> <%=descriptor.getDescripcion("etiqExtensionInvalid")%> <c:out value='${requestScope.EXTENSION_PERMITED}'/>';
            </c:if>
    
           <c:if test="${requestScope.EXTENSION_FILE_INCORRECT eq 'si' and requestScope.TAM_MAX_FILE_EXCEED ne 'si'}">
                msgError = '<%=descriptor.getDescripcion("etiqExtensionInvalid")%> <c:out value='${requestScope.EXTENSION_PERMITED}'/>';        
            </c:if>
    
            <c:if test="${requestScope.EXTENSION_FILE_INCORRECT ne 'si' and requestScope.TAM_MAX_FILE_EXCEED eq 'si'}">        
                msgError = '<%=descriptor.getDescripcion("etiqTamMaxExceed")%> <c:out value='${requestScope.TAM_MAX_FILE_BYTE}'/> <%= sBytes %>';
            </c:if>
    
            <c:if test="${requestScope.ERROR_FILESIZE_UPLOAD eq 'si'}">        
               msgError = '<%=descriptor.getDescripcion("etiqTamMaxExceed") %> <c:out value="${requestScope.TAM_MAX_FILE_BYTE}"/> <%= sBytes %>';
            </c:if>  
        
            <c:if test="${requestScope.ERROR_ALTA_ARCHIVO_BBDD eq 'si' || requestScope.ERROR_ALTA_ARCHIVO_BBDD eq 'SI'}">        
                msgError = '<%=descriptor.getDescripcion("msgErrorAltaAdjuntoNotif") %>';
            </c:if>     
            
            <c:if test="${requestScope.TIPO_MIME_INCORRECTO eq 'si'}">        
                msgError = '<%=descriptor.getDescripcion("msgErrorTipoMimeNoValido") %>';
            </c:if>   
            
            if(msgError!=null && msgError!='')
               parent.mainFrame.mostrarMensaje(msgError);
            else{
               msgError = '<%=descriptor.getDescripcion("msgExitoAltaAdjuntoNotif")%>';
               parent.mainFrame.mostrarMensaje(msgError);
               parent.mainFrame.actualizarListaAdjuntosExternos(listaAdjuntosExternos,listaAdjuntosExternosTabla);
            }
                

        }else
        if(opcion!=null && opcion=="ELIMINAR"){                
            var exito = "<%=(String)request.getAttribute("EXITO_ELIMINAR_ADJUNTO")%>";            
            var msg = "";
            if(exito!=null && exito=="SI"){
                msg = "<%=descriptor.getDescripcion("msgExitoElimAdjuntoNotif")%>";
                // Se actualiza la vista de documentos externos                
                parent.mainFrame.actualizarListaAdjuntosExternos(listaAdjuntosExternos,listaAdjuntosExternosTabla);                
                
            }
            else
                msg = "<%=descriptor.getDescripcion("msgErrElimAdjuntoNotif")%>";
            
            parent.mainFrame.mostrarMensaje(msg);    
            
        }else
        if(opcion!=null && opcion=="FIRMAR_CERTIFICADO_ORGANISMO"){                            
            var recuperarAdjunto = "<%=(String)request.getAttribute("ERROR_RECUPERAR_ADJUNTO_FIRMA") %>";
            if(recuperarAdjunto!=null && recuperarAdjunto=="NO"){                
                parent.mainFrame.mostrarMensaje("<%=descriptor.getDescripcion("msgErrFirmDescargaDoc")%>");
            }else{
                
                var firmaAlmacenada = "<%=(String)request.getAttribute("FIRMA_CERTIFICADO_ORGANISMO_BBDD_EXITO") %>";
                
                if(firmaAlmacenada!=null && firmaAlmacenada=="SI"){
                    parent.mainFrame.mostrarMensaje("<%=descriptor.getDescripcion("msgExitoFirmaCertOrganis")%>");
                    parent.mainFrame.actualizarListaAdjuntosExternos(listaAdjuntosExternos,listaAdjuntosExternosTabla);                
                }else
                    parent.mainFrame.mostrarMensaje("<%=descriptor.getDescripcion("msgErrFirmaCertOrganis")%>");                    
            }
        }else
        if(opcion!=null && opcion=="FIRMAR_NOTIFICACION_CERTIFICADO_ORGANISMO"){
            parent.mainFrame.barraProgresoDocumento('off');
            var recuperarNotificacion = "<%=(String)request.getAttribute("ERROR_RECUPERAR_NOTIFICACION") %>";
            if(recuperarNotificacion!=null && recuperarNotificacion=="SI"){
                var msg = "<%=descriptor.getDescripcion("msgErrRecupNotificacionFirma")%>";
                parent.mainFrame.mostrarMensaje(msg);
            }
            else{
                var guardarFirma = "<%=(String)request.getAttribute("ERROR_GUARDAR_FIRMA_NOTIFICACION") %>";
                if(guardarFirma!=null && guardarFirma=="SI"){                    
                    var msg = "<%=descriptor.getDescripcion("msgNotificacionFirmadaExito")%>";
                    parent.mainFrame.mostrarMensaje(msg);
                    parent.mainFrame.notificacionFirmada();


                }else{
                    var msg = "<%=descriptor.getDescripcion("msgErrGuardarNotificacionBD")%>";
                    parent.mainFrame.mostrarMensaje(msg);
                }
            }
       }else
       if(opcion!=null && opcion=="ACTUALIZAR_LISTA_ADJUNTOS"){
            parent.mainFrame.actualizarListaAdjuntosExternos(listaAdjuntosExternos,listaAdjuntosExternosTabla);                
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