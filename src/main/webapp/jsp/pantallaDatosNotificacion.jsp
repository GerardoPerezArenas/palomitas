<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page language="java" contentType="text/html" pageEncoding="ISO-8859-15"%>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="java.util.Vector"%>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO"%>
<%@ page import="es.altia.agora.business.sge.DefinicionProcedimientosValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.DefinicionProcedimientosForm"%>
<%@page import="es.altia.flexia.notificacion.vo.NotificacionVO" %>
<%@page import="es.altia.flexia.notificacion.vo.AdjuntoNotificacionVO" %>
<%@page import="es.altia.flexia.notificacion.vo.AutorizadoNotificacionVO" %>
<%@page import="es.altia.flexia.notificacion.form.NotificacionForm" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ResourceBundle"%>
<%@ page import="es.altia.agora.technical.ConstantesDatos"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%
    UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int idioma=1;
    int apl=4;
    int codOrg = 0;
    int codEnt = 1;

    NotificacionForm form = null;
    NotificacionVO notificacionVO=null;


    if (session.getAttribute("usuario") != null){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    apl = usuarioVO.getAppCod();
    idioma = usuarioVO.getIdioma();
    codOrg = usuarioVO.getOrgCod();
    codEnt = usuarioVO.getEntCod();

        form = (NotificacionForm) session.getAttribute("notificacionForm");
        notificacionVO=(NotificacionVO) session.getAttribute("notificacionVO");
    }

    String sBytes  = ConstantesDatos.DESCRIPCION_MEGABYTES;     
    Log log = LogFactory.getLog(this.getClass());
    
    int codNotificacionGeneral = form.getCodNotificacion();
    boolean admiteFirmaCertificadoOrganismo = form.getAdmiteFirmaCertificadoOrganismo();

    
    String extensionesValidas = "";
    String sizeFicherosAdjuntosValidos = "";
    String permitirAnhadirExternos = "";
    try{
        ResourceBundle configNotif = ResourceBundle.getBundle("notificaciones");
        extensionesValidas = configNotif.getString("extension.upload.correct");
        sizeFicherosAdjuntosValidos = configNotif.getString("filesize.upload.correct");
        permitirAnhadirExternos = configNotif.getString("PERMITIR_ANHADIR_EXTERNOS");
    }catch(Exception e){

    }



%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<html>
<head>
    <jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title>Notificacion</title>
    <%@ include file="/jsp/plantillas/Metas.jsp" %>

    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
     <script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>

    <script type="text/javascript">


    var listaAdjuntos = new Array();
    var listaAutorizados = new Array();
    var listaAdjuntosSeleccionados='';
    var listaAutorizadosSeleccionados='';
    var listaAdjuntosExternos = new Array();
    var listaAdjuntosExternosTabla = new Array();    
    var existenDatos=false;
    var codNotificacion;
    var admiteFirmaCertificadoOrganismo = "<%=admiteFirmaCertificadoOrganismo%>";
    
 function inicializar()
 {    
    if('<%=form.getCaducidadNotificacion()%>'=='-1') document.forms[0].caducidadNotificacion.value='';


    document.forms[0].textoNotificacion.value= unescape('<bean:write name="NotificacionForm" property="textoNotificacion"/>');

    var j=0;
    var k=0;

    <%ArrayList adjuntos = (ArrayList) form.getAdjuntos();
     if (adjuntos != null) {
        for (int i = 0; i < adjuntos.size(); i++) {
            AdjuntoNotificacionVO adjuntosVO=new AdjuntoNotificacionVO();
            adjuntosVO= (AdjuntoNotificacionVO) adjuntos.get(i);
            if("SI".equals(adjuntosVO.getSeleccionado()))
            {
            %>
                listaAdjuntos[j]=["<input type='checkbox' name='adjunto"+j+"' checked onchange='javascript:existenDatosSinGrabar();'>" ,'<%=adjuntosVO.getNombre()%>','<%=adjuntosVO.getFirmado()%>'];
            <%}else{%>
                listaAdjuntos[j]=["<input type='checkbox' name='adjunto"+j+"' onchange='javascript:existenDatosSinGrabar();'>" ,'<%=adjuntosVO.getNombre()%>','<%=adjuntosVO.getFirmado()%>'];
            <%}%>

            j++;
       <%}
    }
   ArrayList autorizados = (ArrayList) form.getAutorizados();
   if (autorizados != null) {
       for (int i = 0; i < autorizados.size(); i++) {
           AutorizadoNotificacionVO autorizadosVO=new AutorizadoNotificacionVO();
           autorizadosVO= (AutorizadoNotificacionVO) autorizados.get(i); 
           if("SI".equals(autorizadosVO.getSeleccionado()))
           {
           %>
                listaAutorizados[k]=["<input type='checkbox' name='autorizado"+k+"' checked onchange='javascript:existenDatosSinGrabar();'>",'<%=autorizadosVO.getNif()%>','<%=autorizadosVO.getNombreCompleto()%>'];
           <%}else{%>
                listaAutorizados[k]=["<input type='checkbox' name='autorizado"+k+"' onchange='javascript:existenDatosSinGrabar();'>",'<%=autorizadosVO.getNif()%>','<%=autorizadosVO.getNombreCompleto()%>'];
            <%}%>
           k++;
      <% }
    }
    %>
            
  
    var h =0;
    
    
   <%    
        ArrayList<AdjuntoNotificacionVO> adjuntosExternos = form.getAdjuntosExternos();        
        log.debug("************* adjuntosExternos: " + adjuntosExternos.size());
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
            if(codNotificacionGeneral!=-1 && estadoFirma!=null && estadoFirma.equals("O") && !"F".equals(form.getEstadoNotificacion())){
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
  
   tablaDocumentos.lineas=listaAdjuntos;
   tablaAutorizados.lineas=listaAutorizados;
   tablaDocumentosExternos.lineas = listaAdjuntosExternosTabla;
   refresca();


   <%if("F".equals(form.getEstadoNotificacion())){%>       
            deshabilita();
       
    <%}%>
    
  }


  function verFirma(codAdjunto){     
      var codOrganizacion   = document.forms[0].codigoMunicipio.value;      
      var codProcedimiento  = document.forms[0].codigoProcedimiento.value;
      var ejercicio         = document.forms[0].ejercicio.value;
      var numExpediente     = document.forms[0].numExpediente.value;
      var codigoTramite     = document.forms[0].codigoTramite.value;
      var ocurrenciaTramite = document.forms[0].ocurrenciaTramite.value;      
      var parametros = "&codAdjunto=" + codAdjunto + "&codOrganizacion=" + codOrganizacion + "&codProcedimiento=" + codProcedimiento + "&ejercicio=" + ejercicio + "&numExpediente=" + numExpediente + "&codigoTramite=" + codigoTramite + "&ocurrenciaTramite=" + ocurrenciaTramite;
      
      var url = "<%=request.getContextPath()%>/Notificacion.do?opcion=verFirmaAdjunto" + parametros;      
      abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp'/>?source=" + url,'ventana',
            'width=640,height=300,scrollbars=no,resizable=no,status=no',function(result){});
}

  function notificacionFirmada(){
      deshabilita();
      document.getElementById('divMsgNotificacionFirmada').innerHTML = "<%=descriptor.getDescripcion("msgNotificacionFirmada")%>";

  }

  function firmarNotificacionCertificadoOrganismo(){
      var codNotificacion = document.forms[0].codNotificacion.value;
      var codOrganizacion = document.forms[0].codigoMunicipio.value;
      document.forms[0].target="oculto";
      document.forms[0].action="<%=request.getContextPath()%>/Notificacion.do?opcion=firmaNotificacionCertificadoOrganismo&codNotificacion=" + codNotificacion + "&codOrganizacion=" + codOrganizacion;      
      document.forms[0].submit();
  }

  

  function firmarNotificacion(){    
      var codNotificacion = document.forms[0].codNotificacion.value;
      var url = "<%=request.getContextPath()%>/portafirmas/documentoportafirmas/PrepareFirmaNotificacion.do?codNotificacion=" + codNotificacion + "&tipoMime=application/xml";
      abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp'/>?source=" + url,'ventana',
            'width=780,height=450,scrollbars=no,resizable=no,status=no',function(result){
                if(result!=null && result.length>0){
                    if(result[0]=="FIRMA_NO_VALIDA"){
                        jsp_alerta("A","<%=descriptor.getDescripcion("msgErrFirmaDocNotifNoValida")%>");
                    }
                    else
                    if(result[0]=="FIRMA_OK"){
                        jsp_alerta("A","<%=descriptor.getDescripcion("msgExitoFirmaCertUsuario")%>");
                        // Se actualiza la lista de documentos externos de la notificacion para mostrarse al usuario
                        notificacionFirmada();
                    }else
                    if(result[0]=="FIRMA_NO_ALMACENADA"){
                        jsp_alerta("A","<%=descriptor.getDescripcion("msgErrGuardarFirmaDocNotif")%>");

                    }
               }
         });
  }

  function enviarFirmar(adjunto, filaDocumentoAdjunto){ 
      
      if(adjunto!=null){
          // Si la notificación admite firma con certificado de organismo
          if(admiteFirmaCertificadoOrganismo=="true"){
              if(jsp_alerta("C","<%=descriptor.getDescripcion("msgTipoCertificadoNotificacion")%>")){
                  barraProgresoDocumento("on");     
                  // Firma con certificado de usuario
                  document.forms[0].target = "oculto";
                  document.forms[0].action = "<%=request.getContextPath()%>/Notificacion.do?opcion=firmaCertificadoOrganismo&codAdjunto=" + adjunto + "&codNotificacion=" + document.forms[0].codNotificacion.value; + "&codOrganizacion=" + document.forms[0].codigoMunicipio.value;
                  document.forms[0].submit();
              }else{
                  var url = "<%=request.getContextPath()%>/portafirmas/documentoportafirmas/PrepareFirmaDocExternoNotificacion.do?codAdjunto=" + adjunto + "&codNotificacion=" + document.forms[0].codNotificacion.value;
                  abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp'/>?source=" + url,'ventana',
                        'width=780,height=450,scrollbars=no,resizable=no,status=no',function(result){
                            if(result!=null && result.length>0){                      
                                if(result[0]=="FIRMA_NO_VALIDA"){
                                    jsp_alerta("A","<%=descriptor.getDescripcion("msgErrFirmaDocNotifNoValida")%>");
                                }
                                else
                                if(result[0]=="FIRMA_OK"){
                                    jsp_alerta("A","<%=descriptor.getDescripcion("msgExitoFirmaCertUsuario")%>");
                                    // Se actualiza la lista de documentos externos de la notificacion para mostrarse al usuario
                                    barraProgresoDocumento("on");
                                    document.forms[0].target="oculto";
                                    document.forms[0].action="<%=request.getContextPath()%>/Notificacion.do?opcion=actualizarListaAdjuntos&codNotificacion=" + document.forms[0].codNotificacion.value;
                                    document.forms[0].submit();
                                }else
                                if(result[0]=="FIRMA_NO_ALMACENADA"){
                                    jsp_alerta("A","<%=descriptor.getDescripcion("msgErrGuardarFirmaDocNotif")%>");

                                }
                           }
                     });
              }
                  
          }else{
            var url = "<%=request.getContextPath()%>/portafirmas/documentoportafirmas/PrepareFirmaDocExternoNotificacion.do" +
                "?codAdjunto=" + adjunto +
                "&codNotificacion=" + document.forms[0].codNotificacion.value +
                "&codProcedimiento=" + document.forms[0].codigoProcedimiento.value +
                "&numExpediente=" + document.forms[0].numExpediente.value +
                "&codigoTramite=" + document.forms[0].codigoTramite.value +
                "&ocurrenciaTramite=" + document.forms[0].ocurrenciaTramite.value +
                "&ejercicio=" + document.forms[0].ejercicio.value +
                "&codMunicipio=" + document.forms[0].codigoMunicipio.value +
                "&tipoMime=" + listaAdjuntosExternos[filaDocumentoAdjunto][3];              
            
            abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp'/>?source=" + url,'ventana',
                        'width=780,height=450,scrollbars=no,resizable=no,status=no',function(result){
                            if(result!=null && result.length>0){                  
                                if(result[0]=="FIRMA_NO_VALIDA"){
                                    jsp_alerta("A","<%=descriptor.getDescripcion("msgErrFirmaDocNotifNoValida")%>");
                                }
                                else
                                if(result[0]=="FIRMA_OK"){
                                    jsp_alerta("A","<%=descriptor.getDescripcion("msgExitoFirmaCertUsuario")%>");
                                    // Se actualiza la lista de documentos externos de la notificacion para mostrarse al usuario
                                    barraProgresoDocumento("on");
                                    document.forms[0].target="oculto";
                                    document.forms[0].action="<%=request.getContextPath()%>/Notificacion.do?opcion=actualizarListaAdjuntos&codNotificacion=" + document.forms[0].codNotificacion.value;
                                    document.forms[0].submit();
                                }else
                                if(result[0]=="FIRMA_NO_ALMACENADA"){
                                    jsp_alerta("A","<%=descriptor.getDescripcion("msgErrGuardarFirmaDocNotif")%>");
                                }
                            }
                      });
          }
          
          
      }      
  }
  
  function actualizarListaAdjuntosExternos(listaAdjuntos,listaAdjuntosTabla){
      barraProgresoDocumento("off");     
      listaAdjuntosExternos = listaAdjuntos;
      listaAdjuntosExternosTabla = listaAdjuntosTabla;
      tablaDocumentosExternos.lineas = listaAdjuntosExternosTabla;
      tablaDocumentosExternos.displayTabla();  
  }
  


  function getXMLHttpRequest(){
    var aVersions = [ "MSXML2.XMLHttp.5.0",
        "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
        "MSXML2.XMLHttp","Microsoft.XMLHttp"
      ];

    if (window.XMLHttpRequest){
            // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
            return new XMLHttpRequest();
    }else if (window.ActiveXObject){
        // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
        for (var i = 0; i < aVersions.length; i++) {
                try {
                    var oXmlHttp = new ActiveXObject(aVersions[i]);
                    return oXmlHttp;
                }catch (error) {
                //no necesitamos hacer nada especial
                }
         }
    }
 }

    function estaNotificacionPreparadaFirmar(){
        var exito =false;
        var ajax = getXMLHttpRequest();
        var numExpediente = document.forms[0].numExpediente.value;
        var codNotificacion = document.forms[0].codNotificacion.value;

        /****/
        if(ajax!=null){
            var url = "<%= request.getContextPath() %>" + "/Notificacion.do";
            var parametros = "&opcion=notificacionGrabadaCorrectamente&numExpediente=" + numExpediente + "&codNotificacion=" + codNotificacion;            
            ajax.open("POST",url,false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
            ajax.setRequestHeader("Accept", "text/html, application/xml, text/plain");
            ajax.send(parametros);

            try{
                if (ajax.readyState==4 && ajax.status==200){

                    var xmlDoc = null;
                    var text = ajax.responseText;                    
                    var datosRespuesta = text.split("=");
                    if(datosRespuesta.length==2 && datosRespuesta[0]=="NOTIFICACION_CORRECTA" && datosRespuesta[1]=="true"){
                       exito =true;
                    }

					
                }
            }
            catch(err){
                alert("Error al verificar si la notificacion esta preparada para firmar: " + err);
            }
        }
        return exito;
    }




 function pulsarFirmar()
 {
     if(!estaNotificacionPreparadaFirmar()){
        jsp_alerta("A","<%=descriptor.getDescripcion("msgErrFirmarNotif")%>");
     }else{

         var selecAdjunto;
         var selecAutoriz;
         var existenNoFirmados=false;
         var existenAlgunAdjunto=false;
         listaAdjuntosSeleccionados='';
         listaAutorizadosSeleccionados='';

         for (i=0; i < listaAdjuntos.length; i++) {

             var cajaAdjunto = "adjunto" + i;
             if(eval("document.forms[0]." + cajaAdjunto + ".checked") == true)
             {
                 if(listaAdjuntos[i][2]=='NO') existenNoFirmados=true;
                 listaAdjuntosSeleccionados += 'SI'+'§¥';
             }
             else listaAdjuntosSeleccionados += 'NO'+'§¥';
         }


         for (i=0; i < listaAutorizados.length; i++) {
             var cajaAutorizado = "autorizado" + i;
             if(eval("document.forms[0]." + cajaAutorizado + ".checked") == true)
             {
                existenAlgunAdjunto=true;
                listaAutorizadosSeleccionados += 'SI'+'§¥';
             }
             else listaAutorizadosSeleccionados += 'NO'+'§¥';
         }

         var contDocsExternosConFirma = 0;
         for (i=0; i < listaAdjuntosExternos.length; i++) {

             if(listaAdjuntosExternos[i][1]=="null"){
                 contDocsExternosConFirma++;
             }
         }


        if (existenNoFirmados){
            jsp_alerta('A',"<%=descriptor.getDescripcion("msjDocsNoFirmados")%>");

        }
        else{
             if(existenAlgunAdjunto){
                 document.forms[0].lAutorizadosSeleccionados.value=listaAutorizadosSeleccionados;
                 document.forms[0].lAdjuntosSeleccionados.value=listaAdjuntosSeleccionados;
                 document.forms[0].tipoDocumento.value=2;
                 document.forms[0].opcion.value="firmar";
                 document.forms[0].target="oculto";
                 document.forms[0].action="<c:url value='/Notificacion.do'/>";
                 document.forms[0].submit();
             }
             else jsp_alerta('A',"<%=descriptor.getDescripcion("msjNoAutorizSel")%>");
       }
   }// else
}

   

 function pulsarGuardar()
 {   
          
     if(document.forms[0].actoNotificado.disabled==true)
     {     
        habilita();
        //document.forms[0].cmdFirma.disabled=true;
        //document.forms[0].cmdEnviar.disabled=true;
     
     }
     else
     {         
         var selecAdjunto;
         var selecAutoriz;
         listaAdjuntosSeleccionados='';
         listaAutorizadosSeleccionados='';

         for (i=0; i < listaAdjuntos.length; i++) {
             var cajaAdjunto = "adjunto" + i;
             if(eval("document.forms[0]." + cajaAdjunto + ".checked") == true)
             {
                 listaAdjuntosSeleccionados += 'SI'+'§¥';
             }
             else listaAdjuntosSeleccionados += 'NO'+'§¥';
         }

         for (i=0; i < listaAutorizados.length; i++) {
             var cajaAutorizado = "autorizado" + i;
             if(eval("document.forms[0]." + cajaAutorizado + ".checked") == true)
             {
                listaAutorizadosSeleccionados += 'SI'+'§¥';
             }
             else listaAutorizadosSeleccionados += 'NO'+'§¥';
         }

         document.forms[0].lAutorizadosSeleccionados.value=listaAutorizadosSeleccionados;
         document.forms[0].lAdjuntosSeleccionados.value=listaAdjuntosSeleccionados;         
         document.forms[0].opcion.value="guardar";
         document.forms[0].target="oculto";
         document.forms[0].action="<c:url value='/Notificacion.do'/>?opcion=guardar";
         document.forms[0].submit();
    }
}


function pulsarEnviar()
 {

  if(jsp_alerta("C",'<%=descriptor.getDescripcion("msjEnviarNotificacion")%>')) {
     barraProgresoDocumento("on");
     document.forms[0].opcion.value="enviar";
     document.forms[0].target="oculto";
     document.forms[0].action="<c:url value='/Notificacion.do'/>";
     document.forms[0].submit();
  }

}

function mostrarMensajeGrabacionNotificacionDatos(check, mensaje)
{
    jsp_alerta('A',mensaje);
    if(check=='1')
    {
        habilita();
        
    }
            
}

function pulsarCancelar()
{
     self.parent.parent.opener.retornoXanelaAuxiliar();
}

function postNotificacion()
{    
    var datos = new Array();
    datos[0] = "ACTUALIZAR"
    self.parent.parent.opener.retornoXanelaAuxiliar(datos);
}


function deshabilitarCheckBox(){
    for (i=0;ele=document.forms[0].elements[i];i++){

        if (ele.type=='checkbox'){
           ele.disabled = true;
        }
    }
}



function deshabilita()
{
    try{
    
    document.forms[0].cmdGuardar.disabled=true;
    document.forms[0].eliminarAdjunto.disabled=true;
    document.forms[0].adjuntarFichero.disabled=true;
    document.forms[0].cmdFirma.disabled=true;
    document.forms[0].cmdEnviar.disabled=false;

    document.forms[0].cmdCancelar.disabled=false;    
    deshabilitarCheckBox();
    }catch(err){
        alert("error: " + err);
    }

}
function habilita()
{
    habilitarDatos(document.forms[0]);
    document.forms[0].cmdGuardar.value="Guardar";
    habilitaTablas();
}

function deshabilitaTablas()
{
    var j=0;
    var k=0;
    <% adjuntos = (ArrayList) notificacionVO.getAdjuntos();
     if (adjuntos != null) {
        for (int i = 0; i < adjuntos.size(); i++) {
            AdjuntoNotificacionVO adjuntosVO=new AdjuntoNotificacionVO();
            adjuntosVO= (AdjuntoNotificacionVO) adjuntos.get(i);
            if("SI".equals(adjuntosVO.getSeleccionado()))
            {
            %>
                listaAdjuntos[j]=["<input type='checkbox' name='adjunto"+j+"' disabled checked onchange='javascript:existenDatosSinGrabar();'>" ,'<%=adjuntosVO.getNombre()%>','<%=adjuntosVO.getFirmado()%>'];
            <%}else{%>
                listaAdjuntos[j]=["<input type='checkbox' name='adjunto"+j+"' disabled onchange='javascript:existenDatosSinGrabar();'>" ,'<%=adjuntosVO.getNombre()%>','<%=adjuntosVO.getFirmado()%>'];
            <%}%>

            j++;
       <%}
    }
    autorizados = (ArrayList) notificacionVO.getAutorizados();
   if (autorizados != null) {
       for (int i = 0; i < autorizados.size(); i++) {
           AutorizadoNotificacionVO autorizadosVO=new AutorizadoNotificacionVO();
           autorizadosVO= (AutorizadoNotificacionVO) autorizados.get(i);
           if("SI".equals(autorizadosVO.getSeleccionado()))
           {
           %>
                listaAutorizados[k]=["<input type='checkbox' name='autorizado"+k+"' checked disabled>",'<%=autorizadosVO.getNif()%>','<%=autorizadosVO.getNombreCompleto()%>'];
           <%}else{%>
                listaAutorizados[k]=["<input type='checkbox' name='autorizado"+k+"' disabled>",'<%=autorizadosVO.getNif()%>','<%=autorizadosVO.getNombreCompleto()%>'];
            <%}%>
           k++;
      <% }
    }
    %>

   tablaDocumentos.lineas=listaAdjuntos;
   tablaAutorizados.lineas=listaAutorizados;
   refresca();
}
function habilitaTablas()
{
    var j=0;
    var k=0;
    <% adjuntos = (ArrayList) notificacionVO.getAdjuntos();
     if (adjuntos != null) {
        for (int i = 0; i < adjuntos.size(); i++) {
            AdjuntoNotificacionVO adjuntosVO=new AdjuntoNotificacionVO();
            adjuntosVO= (AdjuntoNotificacionVO) adjuntos.get(i);
            if("SI".equals(adjuntosVO.getSeleccionado()))
            { 
            %>
                listaAdjuntos[j]=["<input type='checkbox' name='adjunto"+j+"'  onchange='javascript:existenDatosSinGrabar();' checked>" ,'<%=adjuntosVO.getNombre()%>','<%=adjuntosVO.getFirmado()%>'];
            <%}else{%>
                listaAdjuntos[j]=["<input type='checkbox' name='adjunto"+j+"' onchange='javascript:existenDatosSinGrabar();'>" ,'<%=adjuntosVO.getNombre()%>','<%=adjuntosVO.getFirmado()%>'];
            <%}%>

            j++;
       <%}
    }
    autorizados = (ArrayList) notificacionVO.getAutorizados();
   if (autorizados != null) {
       for (int i = 0; i < autorizados.size(); i++) {
           AutorizadoNotificacionVO autorizadosVO=new AutorizadoNotificacionVO();
           autorizadosVO= (AutorizadoNotificacionVO) autorizados.get(i);
           if("SI".equals(autorizadosVO.getSeleccionado()))
           {
           %>
                listaAutorizados[k]=["<input type='checkbox' name='autorizado"+k+"' checked  onchange='javascript:existenDatosSinGrabar();'>",'<%=autorizadosVO.getNif()%>','<%=autorizadosVO.getNombre()%>'];
           <%}else{%>
                listaAutorizados[k]=["<input type='checkbox' name='autorizado"+k+"' onchange='javascript:existenDatosSinGrabar();'>",'<%=autorizadosVO.getNif()%>','<%=autorizadosVO.getNombre()%>'];
            <%}%>
           k++;
      <% }
    }
    %>

   tablaDocumentos.lineas=listaAdjuntos;
   tablaAutorizados.lineas=listaAutorizados;
   refresca();
}

function existenDatosSinGrabar()
{
    existenDatos=true;
}


function adjuntarFicheroExterno(){
    
    var codMunicipio     = document.forms[0].codigoMunicipio.value;
    var codProcedimiento = document.forms[0].codigoProcedimiento.value;
    var ejercicio        = document.forms[0].ejercicio.value;
    var numExpediente    = document.forms[0].numExpediente.value;
    
    var codigoTramite = document.forms[0].codigoTramite.value;
    var ocurrenciaTramite = document.forms[0].ocurrenciaTramite.value;
    var descripcionProcedimiento = document.forms[0].descripcionProcedimiento.value;
    var codNotificacion = document.forms[0].codNotificacion.value;
    
    var parametros = "&codMunicipio=" + codMunicipio + "&codProcedimiento=" + codProcedimiento + 
                     "&ejercicio=" + ejercicio + "&numero=" + numExpediente + "&codTramite=" + codigoTramite + 
                     "&ocurrenciaTramite=" + ocurrenciaTramite + "&procedimiento=" + descripcionProcedimiento + "&codNotificacion=" + codNotificacion + "&recargar=1";   
                 
    var url = "<%=request.getContextPath()%>/Notificacion.do?opcion=mostrarPantallaNotificacion" + parametros;
         
      
    var dato = document.forms[0].ficheroExterno.value;        
    if(dato!=null && dato!=undefined && dato!=""){
        barraProgresoDocumento("on");   
        document.forms[0].target = "oculto";
        document.forms[0].action = "<%=request.getContextPath() %>/Notificacion.do?opcion=adjuntarFicheroExterno" + parametros;
        document.forms[0].submit();
    }else 
        jsp_alerta("A","<%= descriptor.getDescripcion("msgFichNotifNoSeleccionado")%>");        
    
}



    function verificarEliminar(){
    
        var index = tablaDocumentosExternos.selectedIndex;        
        if(index!=-1){                
        
            var estadoFirma = listaAdjuntosExternos[index][1];            
            if(estadoFirma!=null && estadoFirma=='F'){
                jsp_alerta('A','<%=descriptor.getDescripcion("msgErrorDeleteDocFirmado")%>');
            }else{
                if(jsp_alerta('C','<%=descriptor.getDescripcion("confirmDeleteFileNot")%>')){
                    var nombreDocumento = listaAdjuntosExternos[index][0];
                    var codigo = listaAdjuntosExternos[index][2];
                    var codMunicipio     = document.forms[0].codigoMunicipio.value;
                    var codProcedimiento = document.forms[0].codigoProcedimiento.value;
                    var ejercicio        = document.forms[0].ejercicio.value;
                    var numExpediente    = document.forms[0].numExpediente.value;
                    var codNotificacion  = document.forms[0].codNotificacion.value;

                    var codigoTramite = document.forms[0].codigoTramite.value;
                    var ocurrenciaTramite = document.forms[0].ocurrenciaTramite.value;
                    var descripcionProcedimiento = document.forms[0].descripcionProcedimiento.value;

                    var parametros = "&codMunicipio=" + codMunicipio + "&codProcedimiento=" + codProcedimiento + 
                                    "&ejercicio=" + ejercicio + "&numero=" + numExpediente + "&codTramite=" + codigoTramite + 
                                    "&ocurrenciaTramite=" + ocurrenciaTramite + "&procedimiento=" + descripcionProcedimiento + "&codigo=" + codigo + "&codNotificacion=" + codNotificacion + "&nombreDocumento=" + escape(nombreDocumento);   

                    var url = "<%=request.getContextPath()%>/Notificacion.do?opcion=eliminarArchivoExternoNotificacion" + parametros;
                    // Se procede a eliminar el fichero de la lista de ficheros adjuntos externos de la notificación
                    document.forms[0].target = "oculto";
                    document.forms[0].action = url;
                    document.forms[0].submit();
                }
           }
        }else
            jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorNoSelectDocExterno")%>');
    }


    function descargarAdjunto(){                
        var index = tablaDocumentosExternos.selectedIndex;        
        if(index!=-1){                
            var codigo = listaAdjuntosExternos[index][2];
            var nombreDocumento = listaAdjuntosExternos[index][0];
            
            var codMunicipio     = document.forms[0].codigoMunicipio.value;
            var codProcedimiento = document.forms[0].codigoProcedimiento.value;
            var ejercicio        = document.forms[0].ejercicio.value;
            var numExpediente    = document.forms[0].numExpediente.value;

            var codigoTramite = document.forms[0].codigoTramite.value;
            var ocurrenciaTramite = document.forms[0].ocurrenciaTramite.value;
            var descripcionProcedimiento = document.forms[0].descripcionProcedimiento.value;

            var parametros = "&codMunicipio=" + codMunicipio + "&codProcedimiento=" + codProcedimiento + 
                            "&ejercicio=" + ejercicio + "&numero=" + numExpediente + "&codTramite=" + codigoTramite + 
                            "&ocurrenciaTramite=" + ocurrenciaTramite + "&procedimiento=" + descripcionProcedimiento + "&recargar=1&codigo=" + codigo + "&nombreDocumento=" + nombreDocumento;   

            var url = "<%=request.getContextPath()%>/Notificacion.do?opcion=descargarArchivoExternoNotificacion" + parametros;
            // Se procede a eliminar el fichero de la lista de ficheros adjuntos externos de la notificación
            document.forms[0].action = url;
            document.forms[0].submit();        
        }else
            jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorNoSelectDocExterno")%>')
    }
    
    
    function mostrarMensaje(error){        
        barraProgresoDocumento("off");     
        jsp_alerta('A',error);
    }
    
    
    function barraProgresoDocumento(valor) {        
      if(valor=='on'){            
         document.getElementById('hidepage').style.visibility = 'inherit';          
      } 
      else 
      if(valor=='off'){           
         document.getElementById('hidepage').style.visibility = 'hidden';                   
      }
    }
    
  
</script>
             
</head>

<body class="bandaBody" onload="inicializar();pleaseWait('off');">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

    <html:form action="/Notificacion.do" method="post" enctype="multipart/form-data">

        <html:hidden  property="opcion" value=""/>
        <html:hidden  property="lAutorizadosSeleccionados" value=""/>
        <html:hidden  property="lAdjuntosSeleccionados" value=""/>
        <html:hidden  property="tipoDocumento" value=""/>
        <html:hidden  property="estadoNotificacion" value=""/>
        <html:hidden property="codigoMunicipio"/>
        <html:hidden property="codigoProcedimiento"/>
        <html:hidden property="ejercicio"/>    
        <html:hidden property="codigoTramite"/>
        <html:hidden property="ocurrenciaTramite"/>
        <html:hidden property="descripcionProcedimiento"/>    
        <html:hidden property="codNotificacion"/>           
         
        <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("etiqDatosNotificacion")%></div>
        <div class="contenidoPantalla">
            <table cellpadding="1px" cellspacing="0px" border="0px">
                <tr>
                    <td class="etiqueta" style="width:16%" align="left">
                            &nbsp;<%=descriptor.getDescripcion("etiq_numExp")%>:</td>
                    <td align="left">
                        <html:text styleId="numExpediente" property="numExpediente" styleClass="inputTexto" size="20" readonly="true" onmouseover="titulo(this);"/>
                    </td>
                    <td align="left">
                        <html:text styleId="nombreExpediente" property="nombreExpediente" styleClass="inputTexto" size="105" readonly="true" onmouseover="titulo(this);"/>
                    </td>
                    <td>
                    </td>
                </tr>
                <tr style="height:10px"></tr>
                <tr>
                    <td class="etiqueta" style="width:16%" align="left">
                         &nbsp;<%=descriptor.getDescripcion("etiqTipoNotificacion")%>:</td>
                    <td colspan="2" align="left">
                        <html:text styleId="tipoNotificacion" property="tipoNotificacion" styleClass="inputTexto" size="60" readonly="true" onmouseover="titulo(this);"/>
                    </td>
                </tr>
                        <tr style="height:10px"></tr>
                        <tr>

                            <td class="etiqueta" style="width:16%" align="left">
                                 &nbsp;<%=descriptor.getDescripcion("etiqActoNotificacion")%>:</td>
                            <td colspan="2" align="left">
                                <html:text styleId="actoNotificado" property="actoNotificado" styleClass="inputTexto" size="60" readonly="" onchange="javascript:existenDatosSinGrabar();" onmouseover="titulo(this);"/>
                            </td>

                        </tr>
                        <tr style="height:10px"></tr>
                        <tr>
                            <td class="etiqueta" style="width:16%" align="left">
                                 &nbsp;<%=descriptor.getDescripcion("etiqCaducidadNotificacion")%>:</td>
                            <td colspan="2" align="left">
                                <html:text styleId="caducidadNotificacion" property="caducidadNotificacion" styleClass="inputTexto" size="10" readonly="" maxlength="4" onchange="javascript:existenDatosSinGrabar();"  onkeyup="javascript:return SoloDigitosNumericos(this);" onmouseover="titulo(this);"/>
                            </td>

                        </tr>
                        <tr style="height:10px"></tr>
                        <tr>

                            <td class="etiqueta" style="width:16%" align="left" valign="top">
                                 &nbsp;<%=descriptor.getDescripcion("etiqTextoNotificacion")%>:</td>


                            <td class="columnP" colspan="4" align="left" >
                            <html:textarea styleClass="textareaTexto" cols="125" rows="4"  property="textoNotificacion" onchange="javascript:existenDatosSinGrabar();"
                            value=""></html:textarea>
                            </td>

                         </tr>
                          <tr style="height:10px">
                              <td style="color:red;font-size:12px;" align="center" colspan="5">
                                  <div id="divMsgNotificacionFirmada" name="divMsgNotificacionFirmada" style="text-transform:bold;border:1px">
                                  <%if("F".equals(form.getEstadoNotificacion()))
                                  {
                                  %>

                                      <strong>
                                          <%=descriptor.getDescripcion("msgNotificacionFirmada") %>
                                      </strong>                                                          
                                  <%}%>
                                  </div>
                              </td>
                          </tr>

                          <tr <%if ("N".equals(permitirAnhadirExternos)) {%>style="display:none"<%}%>>
                        <td colspan="4">
                            <table>
                                <tr>
                                    <td class="sub3titulo" colspan="4"><%=descriptor.getDescripcion("msgArchivosExtNotificacion")%></td>                                            
                                </tr>                                                                                    
                                <tr>
                                    <td id="tablaDocumentosExternos" name="tablaDocumentosExternos" colspan="4" valign="top">&nbsp;</td>
                                </tr>    
                                <tr>
                                    <td colspan="4" align="right">
                                        <input type="file" name="ficheroExterno" size="30"/>
                                        <input type="button" name="adjuntarFichero" id="adjuntarFichero" value="Adjuntar" class="botonGeneral" onclick="javascript: return adjuntarFicheroExterno();"/>
                                        <input type="button" id="btnDescargarAdjunto" name="btnDescargarAdjunto" class="botonLargo" value="<%=descriptor.getDescripcion("msgDescargarAdjuntoNotif")%>" onclick="javascript:descargarAdjunto();"/>
                                        <input type="button" id="eliminarAdjunto" name="eliminarAdjunto" class="botonLargo" value="<%=descriptor.getDescripcion("msgDeleteAdjuntoNotif")%>" onclick="verificarEliminar();"/>
                                    </td>
                                </tr>                                            
                                <tr>
                                    <td colspan="4" style="height:5px;" class="etiquetaLeyendaPendientes" align="right">
                                        <%=descriptor.getDescripcion("msgNotaArchivosNotif")%><br/>
                                        <%=descriptor.getDescripcion("msgExtArchivosNotif")%> <%=extensionesValidas %><br/>
                                        <%=descriptor.getDescripcion("msgTamMaxArchivosNotif")%> <%=sizeFicherosAdjuntosValidos %> bytes
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>                                                                               
                <!-- prueba -->
                <TR>
                    <TD class="sub3titulo"  align="left" colspan="4">&nbsp;<%=descriptor.getDescripcion("etiqAdjuntos")%>:</TD>
                </TR>
                <tr>
                    <td id="tablaDocumentos" align="left" colspan="4"></td>
                </tr>
                <tr>
                    <td colspan="4" style="height:5px;">&nbsp;</td>                                            
                </tr>                                                                                

                <TR>
                    <TD class="sub3titulo"  align="left" colspan="4">&nbsp;<%=descriptor.getDescripcion("etiqAutorizados")%>:</TD>
                </TR>
                <tr>
                    <td id="tabla" align="left" colspan="4"></td>
                </tr>
            </table>
            <div class="botoneraPrincipal">                                        
                <input type= "button" class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbGrabar")%>" name="cmdGuardar" onclick="pulsarGuardar();">
                <input type= "button" class="botonGeneral" accesskey="E" value="<%=descriptor.getDescripcion("etiqFirmar")%>" name="cmdFirma" onclick="pulsarFirmar();">
                <input type= "button" class="botonGeneral" accesskey="E" value="<%=descriptor.getDescripcion("etiqEnviar")%>" name="cmdEnviar" onclick="pulsarEnviar();">
                <input type= "button"  class="botonGeneral" accesskey="C" value="<%=descriptor.getDescripcion("gbCerrar")%>" name="cmdCancelar"  onclick="pulsarCancelar();">
            </div>                                        
        </div>                                        
</html:form>

    <script language="JavaScript1.2">
      var tablaAutorizados = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>',
                '<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>',
                '<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', 
                '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>',
                '<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',
                document.getElementById('tabla'));

      tablaAutorizados.addColumna('100','center',"");
      tablaAutorizados.addColumna('200','left',"<%=descriptor.getDescripcion("gEtiqDocumento")%>");
      tablaAutorizados.addColumna('600','left',"<%= descriptor.getDescripcion("gEtiq_ApNombre")%>");
   
      tablaAutorizados.displayCabecera=true;

      tablaAutorizados.displayDatos = pintaDatosAutorizados;

      

      function pintaDatosAutorizados(rowID){
        var i = tablaAutorizados.selectedIndex;
        if(i != -1) {
          
        }
      }

      document.onmouseup = checkKeys;
      function checkKeysLocal(evento,tecla){

        var aux=null;
        if(window.event)
            aux = window.event;
        else
            aux = evento;

        var tecla = 0;
        if(aux.keyCode)
            tecla = aux.keyCode;
        else
            tecla = aux.which;

         if( (tecla == 40)|| (tecla == 38) ){
                upDownTable(tablaAutorizados,lista);

        }

      
        keyDel(aux);
      }


        var tablaDocumentos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>',
                '<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>',
                '<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', 
                '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>',
                '<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',
                document.getElementById('tablaDocumentos'));


        tablaDocumentos.addColumna('100','center',"");
        tablaDocumentos.addColumna('800','left',"<%= descriptor.getDescripcion("etiqNombreDocumento")%>");
        tablaDocumentos.addColumna('0','left',"");

        tablaDocumentos.displayCabecera=true;
        tablaDocumentos.displayDatos = pintaDatosDocumentos;

        /**** TABLA DOCUMENTOS ADJUNTOS EXTERNOS **/
        var tablaDocumentosExternos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>',
                '<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>',
                '<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', 
                '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>',
                '<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',
                document.getElementById('tablaDocumentosExternos'));
        
        tablaDocumentosExternos.addColumna('450','left',"<%= descriptor.getDescripcion("etiqNombreDocumento")%>");
        tablaDocumentosExternos.addColumna('200','center',"<%=descriptor.getDescripcion("etiqEstadoFirmaNotif")%>");
        tablaDocumentosExternos.addColumna('150','center',"<%=descriptor.getDescripcion("etiqTipoCertificado")%>");
        //tablaDocumentosExternos.addColumna('100','center',"");
        //Columna de enlace para Firmar, cuidado al descomentar sin añadir al array el elemento necesario.
        
        tablaDocumentosExternos.displayCabecera=true;

        function pintaDatosDocumentos(rowID){
            var i = tablaDocumentos.selectedIndex;
            if(i != -1) {

            }
        }

      
      function refresca() {
        tablaAutorizados.displayTabla();
        tablaDocumentos.displayTabla();
        tablaDocumentosExternos.displayTabla();
        
      }



    </script>
    
    

    <script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>
    
       
    <script type="text/javascript">
        
      <c:if test="${requestScope.FICHERO_EXISTE eq 'si'}">   
            jsp_alerta("A",'<%=descriptor.getDescripcion("msgFichNotifExiste") %>');
      </c:if>
        
        
      <c:if test="${requestScope.EXTENSION_FILE_INCORRECT eq 'si' and requestScope.TAM_MAX_FILE_EXCEED eq 'si'}">        
        jsp_alerta("A",'<%=descriptor.getDescripcion("etiqTamMaxExceed")%> <c:out value='${requestScope.TAM_MAX_FILE_BYTE}'/> <%= sBytes %> <br> <%=descriptor.getDescripcion("etiqExtensionInvalid")%> <c:out value='${requestScope.EXTENSION_PERMITED}'/>');
      </c:if>
    
     <c:if test="${requestScope.EXTENSION_FILE_INCORRECT eq 'si' and requestScope.TAM_MAX_FILE_EXCEED ne 'si'}">
        jsp_alerta("A",'<%=descriptor.getDescripcion("etiqExtensionInvalid")%> <c:out value='${requestScope.EXTENSION_PERMITED}'/>');        
     </c:if>
    
     <c:if test="${requestScope.EXTENSION_FILE_INCORRECT ne 'si' and requestScope.TAM_MAX_FILE_EXCEED eq 'si'}">        
        jsp_alerta("A",'<%=descriptor.getDescripcion("etiqTamMaxExceed")%> <c:out value='${requestScope.TAM_MAX_FILE_BYTE}'/> <%= sBytes %>');
     </c:if>
    
    <c:if test="${requestScope.ERROR_FILESIZE_UPLOAD eq 'si'}">        
        jsp_alerta("A",'<%=descriptor.getDescripcion("etiqTamMaxExceed") %> <c:out value="${requestScope.TAM_MAX_FILE_BYTE}"/> <%= sBytes %>');
    </c:if>  
        
        
     <c:if test="${requestScope.ERROR_ALTA_ARCHIVO_BBDD eq 'si' || requestScope.ERROR_ALTA_ARCHIVO_BBDD eq 'SI'}">        
        jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorAltaAdjuntoNotif") %>');
    </c:if>     
        
</script>
    
</body>
</html>
