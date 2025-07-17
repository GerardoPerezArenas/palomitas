<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<jsp:useBean id="CAMPO_BEAN" scope="request" class="es.altia.agora.technical.EstructuraCampo"/>
<jsp:useBean id="beanVO" scope="request" class="es.altia.agora.technical.CamposFormulario"/>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.ArrayList"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="java.text.MessageFormat"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.business.sge.TramitacionExpedientesValueObject"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<html>
<head>
<jsp:include page="/jsp/sge/tpls/app-constants.jsp"/>
    <title> Oculto Ficha Expediente </title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

    <%
        int idioma=1;
        int apl=4;
        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        FichaExpedienteForm expForm=new FichaExpedienteForm();

            if (session.getAttribute("usuario") != null){
                    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
                expForm = (FichaExpedienteForm)session.getAttribute("FichaExpedienteForm");

                    idioma = usuarioVO.getIdioma();
                    apl = usuarioVO.getAppCod();
            }

        Log m_log = LogFactory.getLog(this.getClass().getName());
        String mensajeTramiteFinalizadoConTramitesAbiertos ="";
        String mensajeTramiteOrigenConTramitesAbiertos ="";
    %>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
             type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<%
        if(expForm.getRespOpcion()=="tramFinalizadoConTramitesPosteriores")
         {
            ArrayList<TramitacionExpedientesValueObject> tramitesDestino = expForm.getTramitesDestino();
            mensajeTramiteFinalizadoConTramitesAbiertos = descriptor.getDescripcion("etiq_notRetrocesoTramAbiertos") + "</br>";
            for(int i=0;tramitesDestino!=null && i<tramitesDestino.size();i++){
                TramitacionExpedientesValueObject destino = tramitesDestino.get(i);
                String unidad = destino.getUnidadTramitadora();
                String descripcion = destino.getDescripcionTramiteFlujoSalida();
                String msgTramite = "<li>" + descriptor.getDescripcion("etiqTramite") + " " + descripcion + " " + descriptor.getDescripcion("etiq_conUnidad") + " " + unidad + "</li></br>";
                mensajeTramiteFinalizadoConTramitesAbiertos += msgTramite;               
                
            }// for
        }// if

        if(expForm.getRespOpcion()=="tramiteOrigenConTramitesAbiertos")
        {
            ArrayList<TramitacionExpedientesValueObject> tramitesDestino = expForm.getTramitesDestino();
            if(tramitesDestino!=null && tramitesDestino.size()>0){
                mensajeTramiteOrigenConTramitesAbiertos = descriptor.getDescripcion("etiq_notRetrocesoTramOrigen1") + " " + tramitesDestino.get(0).getTramite() + "  " + descriptor.getDescripcion("etiq_notRetrocesoTramOrigen2") + "</br>";
                for(int i=0;tramitesDestino!=null && i<tramitesDestino.size();i++){
                    TramitacionExpedientesValueObject destino = tramitesDestino.get(i);
                    String unidad = destino.getUnidadTramitadora();
                    String descripcion = destino.getDescripcionTramiteFlujoSalida();
                    String msgTramite = "<li>" + descriptor.getDescripcion("etiqTramite") + " " + descripcion + " " + descriptor.getDescripcion("etiq_conUnidad") + " " + unidad + "</li></br>";
                    mensajeTramiteOrigenConTramitesAbiertos += msgTramite;                   

                }// for
            }//if
        }// if
      
        
%>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/formateador.js'/>"></script>
<script>
    var consultando = false;
</script>
<!--/////////////////////////PARA VER COMO RECUPERO LOS CAMPO BEAN-->
<!--NO VAN A ESTAR EN LA REQUEST PORQUE LOS PILLO DEL FORM DIRECTAMENTE-->

<!--/////////////////////////////////////////-->
    <script>        
      var listaTramites = new Array();
      var listaTramitesOriginal = new Array();
      var listaTramitesPlugin = new Array();
      var listaOriginal = new Array();
      var lista = new Array();
      var frame;
      if(parent.mainFrame){
        frame = parent.mainFrame;
      } else {
        frame = parent;
      }
      
      var listaDocumentosOriginal = new Array();
      var listaDocumentos = new Array();
      var listaDocumentosOriginalAuxiliar = new Array();
      
      // Genera el enlace de verificar la firma de un documento de inicio que ha sido firmado
      function getEnlaceVerificarFirmaDocumentoInicio(codDocumentoAdjuntoExpediente,codigoDocumento){                        
            return "<a href=\"javascript:verificarFirmaDoc('" + codDocumentoAdjuntoExpediente + "','"+ codigoDocumento + "');\">" + "<%=descriptor.getDescripcion("linkVerificarFirma")%>" + "</a>";
      }  

      function redirecciona(){ 
          if(parent.mainFrame){
              pleaseWait('off');
          } else {
              pleaseWait1('off',frame);
          }
          
        var respOpcion ='<bean:write name="FichaExpedienteForm" property="respOpcion"/>';  
        var respOpcion2 ='';  
        
        if ( ("noGrabado"==respOpcion) || ("grabado"==respOpcion) || ("grabadoSoloObs"==respOpcion)){
            if("grabadoSoloObs"==respOpcion){
                respOpcion = "grabado";
                respOpcion2 = "grabadoSoloObs";
            }
            frame.grabacionExpediente(respOpcion, listaOriginal, lista);
            /** LLAMADA A OPERACIONES JAVASCRIPTS DE PANTALLAS DE EXPEDIENTES DE MODULOS EXTERNOS **/
            var funciones = new Array();
            var contador = 0;
            <%
              ArrayList<String> funciones = expForm.getOperacionesJavascript();
              for(int cont = 0;funciones!=null && cont<funciones.size();cont++){
            %>
                funciones[contador] = "<%=funciones.get(cont)%>";
                contador++;
            <%
              }// for
            %>
             
            for(i=0;funciones!=null && i<funciones.length;i++){                
                eval("frame." + funciones[i] + "();");
            }  
            /** LLAMADA A OPERACIONES JAVASCRIPTS DE PANTALLAS DE EXPEDIENTES DE MODULOS EXTERNOS **/
            
        }else if("tramiteConFormulariosDocumentosFirmados"  ==respOpcion){
            var msnj = "<%=descriptor.getDescripcion("msg_tramDocsFormFirmados")%>";
            jsp_alerta("A", msnj);
        }
        else if ("ErrorExpresion" == respOpcion.substring(0,14)){                        
            var campos = respOpcion.substring(15,respOpcion.length);
            var campos_fin = campos.split("#");
            msnj = "<%=descriptor.getDescripcion("msjExpresionErr")%>" + ":" ;
            for (i = 0; i < campos_fin.length; i++) 
            {            
                msnj = msnj + "<br>" + "     " + campos_fin[i];                 
            }      
            jsp_alerta("A",msnj);                        
        }
        else if("tramiteConFormulariosFirmados"  ==respOpcion){ 
            var msnj = "<%=descriptor.getDescripcion("msg_tramFormsFirmados")%>";
            jsp_alerta("A", msnj);
        }
        if("tramiteConDocumentosFirmados"  ==respOpcion){
                // Se retrocede el trámite seleccionado pero no se puede reabrir el trámite de origen porque es el origen
                // de otros tramites que se deben retroceder antes
                var msnj = "<%=descriptor.getDescripcion("msg_tramDocsFirmados")%>";
                jsp_alerta("A", msnj);
         }
        else
        if("expedienteFinalizadoNoRetroceder"  ==respOpcion){
            var msnj = "No se puede retroceder el expediente porque está finalizado";
            jsp_alerta("A", msnj);
        }
        else if("tramFinalizadoConTramitesPosteriores"  ==respOpcion){
            var msnj = "<%=mensajeTramiteFinalizadoConTramitesAbiertos%>";
             if("<%=mensajeTramiteFinalizadoConTramitesAbiertos.length()%>">=200)jsp_alerta("A", msnj);
             else jsp_alerta("A", msnj);
        }
        else if ("noFinalizado" == respOpcion) {
            var msnj = "<%=descriptor.getDescripcion("msjTramNoRetrocedido")%>";
            jsp_alerta("A", msnj);
            /** SE COMPRUEBA SI HAY UN MENSAJE DE FALLO DEBIDO A LA EJECUCION DE UN MODULO EXTERNO O DE UN SERVICIO WEB PARA MOSTRAR LA RAZON POR LA CUAL
            NO SE PUEDE RETROCEDER UN TRAMITE **/
            var mensajeErrorRetrocesoExpediente = "<%=expForm.getMensajeSW()%>";
            if(mensajeErrorRetrocesoExpediente!="") jsp_alerta("A",mensajeErrorRetrocesoExpediente);

        } else if ("noFinalizadoPorFormularios" == respOpcion) {
            var msnj = "<%=descriptor.getDescripcion("msjNoRetrocPorForms")%>";
            jsp_alerta("A", msnj);
        } else if (respOpcion == "validacionRolErrorUno") {
            var msnj = "<%=descriptor.getDescripcion("msgErrorUnoValRolGrabarExp")%>";
			jsp_alerta("A", msnj);
		} else if (respOpcion == "validacionRolErrorDos") {
			var msnj = "<%=descriptor.getDescripcion("msgErrorDosValRolGrabarExp")%>";
			jsp_alerta("A", msnj);
		} else if (respOpcion == "validacionRolErrorTres") {
			var msnj = "<%=descriptor.getDescripcion("msgErrorTresValRolGrabarExp")%>";
			jsp_alerta("A", msnj);
		} else {
            if("tramiteOrigenConTramitesAbiertos"  ==respOpcion){
                var msnj = "<%=mensajeTramiteOrigenConTramitesAbiertos%>";
                if("<%=mensajeTramiteOrigenConTramitesAbiertos.length()%>">=200)jsp_alerta("A", msnj);
                else jsp_alerta("A", msnj);
                
            }
            
        <%
          Vector tramites = expForm.getTramites();          
              if (tramites != null) {
                  int lengthTramites = tramites.size();
            %>
                var j=0;
            <%
                for(int i=0;i<lengthTramites;i++){
            %>
            listaTramites[j] = ['<%=((GeneralValueObject)tramites.get(i)).getAtributo("tramite")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("fehcaInicio")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("fechaFin")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("unidad")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("clasificacion")%>',
                    ""];
            <%if (!((GeneralValueObject)tramites.get(i)).getAtributo("usuarioBloq").equals("")) {
                if (((GeneralValueObject)tramites.get(i)).getAtributo("codUsuarioBloq").toString().equals(String.valueOf(usuarioVO.getIdUsuario()))) {%>
                    listaTramites[j][5]='<span class="fa fa-lock" alt="<%=((GeneralValueObject)tramites.get(i)).getAtributo("usuarioBloq")%>"></span>';
            <%} else {%>
                    listaTramites[j][5]='<span class="fa fa-unlock" alt="<%=((GeneralValueObject)tramites.get(i)).getAtributo("usuarioBloq")%>"></span>':
                <%}
            }%>
            listaTramitesOriginal[j] = ['<%=((GeneralValueObject)tramites.get(i)).getAtributo("ocurrenciaTramite")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("codTramite")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("tramite")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("fehcaInicio")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("fechaFin")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("unidad")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("usuario")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("clasificacion")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("consultar")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("fueraDePlazo")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("codUniTramTramite")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("tramiteInicio")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("codUsuario")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("codUsuarioFinalizacion")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("usuarioBloq")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("codUsuarioBloq")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("plazoCercaFin")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("traNotificacionElectronica")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("tieneTareasPendientesInicio")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("notificacionObligatoria")%>',
                    '<%=((GeneralValueObject)tramites.get(i)).getAtributo("certificadoOrganismo")%>'
                ];

             listaTramitesPlugin[j] = ["<%=((GeneralValueObject)tramites.get(i)).getAtributo("tramite")%>","<%=((GeneralValueObject)tramites.get(i)).getAtributo("codTramite")%>","<%=((GeneralValueObject)tramites.get(i)).getAtributo("ocurrenciaTramite")%>","<%=((GeneralValueObject)tramites.get(i)).getAtributo("extPlugin")%>","<%=((GeneralValueObject)tramites.get(i)).getAtributo("extUrl")%>","<%=((GeneralValueObject)tramites.get(i)).getAtributo("extImplClass")%>","<%=((GeneralValueObject)tramites.get(i)).getAtributo("extIdEnlace")%>","<%=((GeneralValueObject)tramites.get(i)).getAtributo("extBloqueoFinalizarTramite")%>","<%=((GeneralValueObject)tramites.get(i)).getAtributo("extBloqueoRetrocesoTramite")%>"];
             j++;
          <%
                }
            }
          %>
                      
          /*************/            
          var listaPermisosTramites = new Array();
        <%
            Vector permisosTramites = null;
            permisosTramites = (Vector) expForm.getPermisosTramites();
            if (permisosTramites != null) {
                int lengthPermisosTramites = permisosTramites.size();
        %>
                    var j=0;
        <%
                for (int i = 0; i < lengthPermisosTramites; i++) {
        %>
                    listaPermisosTramites[j] = "<%=(String) permisosTramites.get(i)%>";
                    j++;
        <%      
                }
            }
        %>
                      
          /************/          
          frame.listaTramites = listaTramites;
          frame.listaTramitesOriginal = listaTramitesOriginal;
          frame.listaTramitesPlugin = listaTramitesPlugin;
          frame.tabTramites.lineas = listaTramites;
          frame.listaPermisosTramites = listaPermisosTramites;
          frame.tabTramites.displayTabla();
          		
        }
          if(respOpcion2!="grabadoSoloObs") frame.habilitarBotonRetroceso(); 
      }
    </script>
</head>
<body onLoad="redirecciona();">
    <form>
    <input type="hidden" name="opcion" value="">
    </form>
<p>&nbsp;

<p>
</body>
</html>