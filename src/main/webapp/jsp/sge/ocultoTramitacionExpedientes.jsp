<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionExpedientesForm" %>
<%@page import="es.altia.agora.business.sge.TramitacionExpedientesValueObject" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@ page import="es.altia.common.service.config.Config" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<html>
    <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
        <title> Tramitacion expedientes </title>

        <jsp:include page="/jsp/plantillas/Metas.jsp" />

        <%
        Log m_log = LogFactory.getLog(this.getClass().getName());
        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        int idioma = 1;
        int apl = 1;

        if (session.getAttribute("usuario") != null) {
            usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
            idioma = usuarioVO.getIdioma();
            apl = usuarioVO.getAppCod();
        }
        Config m_Config = ConfigServiceHelper.getConfig("common");
        String statusBar = m_Config.getString("JSP.StatusBar");

        String noPulsarVolver = (String)request.getAttribute("noPulsarVolver");
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>

        <script>
            
            var TEXTOS_I18_EDITOR_TEXTO_NULL = '<%=descriptor.getDescripcion("etiqNoEditable")%>';
            var noPulsarVolver = "<%=noPulsarVolver%>";            
            var listaTramitesPendientes = new Array();
            var listaDocumentos = new Array();
            var listaDocumentosOriginal = new Array();
            var listaTramitesNoFinalizados = new Array();
            var cont = 0;
            var listaInteresados = new Array();
            var listaInteresadosOriginal = new Array();
            var uorInicioExp;
            
            function notificacionRealizada(notifRealizada) {
                if (notifRealizada=="no"){
                    msnj = "<%=descriptor.getDescripcion("msjNotifNoRealiz")%>";                    
                    jsp_alerta("A",msnj);
                }
            }

            function expAsociadoIniciado() {
                var numeroRelacion ='<bean:write name="TramitacionExpedientesForm" property="numeroRelacion"/>';
                var numero ='<bean:write name="TramitacionExpedientesForm" property="expAsociadoIniciado"/>';
                if (numeroRelacion != "" && numeroRelacion !=null) {
                    if (numero != "" && numero !=null){
                        jsp_alerta("A","<%=descriptor.getDescripcion("msjExpAsocInicRel")%> ");
                    }
                } else {
                    if (numero != "" && numero !=null){
                        jsp_alerta("A","<%=descriptor.getDescripcion("msjExpAsocInic1")%> " + numero + " <%=descriptor.getDescripcion("msjExpAsocInic2")%>");
                    }
                }
            }

function redirecciona() {
    var notifRealizada ='<bean:write name="TramitacionExpedientesForm" property="notificacionRealizada"/>';
    var respOpcion ='<bean:write name="TramitacionExpedientesForm" property="respOpcion"/>';
    var mensajeSW ='<bean:write name="TramitacionExpedientesForm" property="mensajeSW"/>';
    var resultadoFinalizar = '<bean:write name="TramitacionExpedientesForm" property="resultadoFinalizar"/>';
    var procedimientoAsociado ='<bean:write name="TramitacionExpedientesForm" property="procedimientoAsociado"/>';
    var codUnidadTramitadoraManual = '<bean:write name="TramitacionExpedientesForm" property="codUnidadTramitadoraManual"/>';
    if (mensajeSW != "") {
        jsp_alerta("A", mensajeSW);
    }
    
    var errorGrabarDatosSuplFichero = "<%=request.getAttribute("errorGrabarDatosSuplFichero")%>";
    if (errorGrabarDatosSuplFichero == "1")
        jsp_alerta("A","<%=descriptor.getDescripcion("errGrabarDatosFichero")%> ");
    
        
    if ( ("noGrabado"==respOpcion) || ("grabado"==respOpcion) ){
        parent.mainFrame.grabacionTramite(respOpcion);
        parent.mainFrame.cargaDocumentos();
        if("grabado"==respOpcion){
           /** LLAMADA A OPERACIONES JAVASCRIPTS DE LAS PESTAÑAS DE TRÁMITE DE MODULOS EXTERNOS **/
            var funciones = new Array();
            var contador = 0;
            <%
              TramitacionExpedientesForm tramitacionExpForm = (TramitacionExpedientesForm)session.getAttribute("TramitacionExpedientesForm");
              ArrayList<String> funciones = tramitacionExpForm.getFuncionesJavascriptModulosExternos();
              
              Calendar today = Calendar.getInstance();
              String alarma ="";
	      String name = "";
	      String valor = "";
              String codigo = "";
              String campoActivo = "";
              String fechaVencimiento = "";
              
              Vector estructuraDatosSuplementarios = null;
	      Vector valoresDatosSuplementarios = null;
              estructuraDatosSuplementarios = (Vector) tramitacionExpForm.getEstructuraDatosSuplementarios();
              valoresDatosSuplementarios = (Vector) tramitacionExpForm.getValoresDatosSuplementarios();
	      int lengthEstructuraDatosSuplementarios=0;
              if(estructuraDatosSuplementarios!=null) lengthEstructuraDatosSuplementarios= estructuraDatosSuplementarios.size();
              
              for(int k=0;k<lengthEstructuraDatosSuplementarios;k++){
		  es.altia.agora.technical.EstructuraCampo eds = (es.altia.agora.technical.EstructuraCampo) estructuraDatosSuplementarios.get(k);
		  es.altia.agora.technical.CamposFormulario vds = (es.altia.agora.technical.CamposFormulario) valoresDatosSuplementarios.get(k);
                  valor = vds.getString(eds.getCodCampo());
                  codigo = vds.getString(eds.getCodCampo()+"_CODSEL");
                  if(valor==null) valor="";
                  if(codigo==null) codigo="";
                  
                  String obligatorio = eds.getObligatorio();
                  String texto = "";
                  String ob = "";
                  if("1".equals(obligatorio)) {
                    texto = "inputTextoObligatorio";
                    ob = "obligatorio";
                  } else {
                    texto = "inputTexto";
                  }
                  name = eds.getCodCampo();
		    if (eds.getCodTramite() != null) {
		        if (eds.getOcurrencia() != null)
		            name = "T_" + eds.getCodTramite() + "_" + eds.getOcurrencia() + "_" + eds.getCodCampo();
		        else
		            name = "T_" + eds.getCodTramite() + "_" + eds.getCodCampo();
		    }
		    //name = "T" + eds.getCodTramite() + eds.getCodCampo();

                    m_log.debug("\n\t\t\t ocultoTramitacionExpedientes   codCampo()= " + eds.getCodCampo());                    

                   fechaVencimiento = vds.getString("fechaVencimiento"+eds.getCodCampo());
                   campoActivo = vds.getString(eds.getCampoActivo());
                   if ("3".equalsIgnoreCase(eds.getCodTipoDato())) {
                     m_log.debug("\n\t\t\t ocultoTramitacionExpedientes   codCampo() de fecha = " + eds.getCodCampo());
                     
                      //En el caso de fecha, hay que actualizar el icono de alarma
                      //Siempre y cuando la fecha de Vencimiento es distinto de null
                     if ((fechaVencimiento!= null)&&(!"".equals(fechaVencimiento))){
                          String [] dataTemp = fechaVencimiento.split("/");
                          Calendar c = Calendar.getInstance();
                          c.set(Integer.parseInt(dataTemp[2]), Integer.parseInt(dataTemp[1])- 1, Integer.parseInt(dataTemp[0]));
                          if (c.compareTo(today)<0){
                                alarma ="desactivada";//alarma vencida
                          }else{
                            alarma="activada";//alarma NO vencida
                          }

                          //actualizamos el vqalor del icono alarma
                          if ((alarma!=null && alarma.equals("activada")) && (campoActivo!=null && campoActivo.equals("1"))) {%>                                          
                              parent.mainFrame.document.getElementById("imagenBoton"+'<%=name%>').style.color = "#6aaf23";
                              parent.mainFrame.document.getElementById("imagenBoton"+'<%=name%>').alt = "<%=descriptor.getDescripcion("etiqAlarma")%>";
                          <%}else {
                                if ((alarma!=null && alarma.equals("desactivada"))  && (campoActivo!=null && campoActivo.equals("1"))) {%>
                                     parent.mainFrame.document.getElementById("imagenBoton"+'<%=name%>').style.color = "#004595";
                                     parent.mainFrame.document.getElementById("imagenBoton"+'<%=name%>').alt = "<%=descriptor.getDescripcion("etiqAlarma")%>";
                                <%}
                           }
                       //cierre if fechaVencimiento   
                       }else{%>                                                            
                            parent.mainFrame.document.getElementById("imagenBoton"+'<%=name%>').className = "fa";
                            parent.mainFrame.document.getElementById("imagenBoton"+'<%=name%>').alt = "";
                      <%}
                  }//cierre if tipo de dato fecha
                  
              }//cierre del for
              for(int cont = 0;funciones!=null && cont<funciones.size();cont++){
            %>
                funciones[contador] = "<%=funciones.get(cont)%>";
                contador++;
            <%
              }// for
            %>
            
            for(i=0;funciones!=null && i<funciones.length;i++){
                eval("parent.mainFrame." + funciones[i] + "();");
            }
           /** LLAMADA A OPERACIONES JAVASCRIPTS DE PANTALLAS DE EXPEDIENTES DE MODULOS EXTERNOS **/
       }
    }
    else if ("ErrorExpresion" ==  respOpcion.substring(0,14)){
        
        var campos = respOpcion.substring(15,respOpcion.length);
        var campos_fin = campos.split("#");
        
        
        var codOperacion = campos_fin[0];        
        // Si el código es un 0 o un 1 => O no hay expresiones de validación del campos numéricos o, estas son correctas        
        if(codOperacion!="0" && codOperacion!="1"){
                        
            msnj = "<%=descriptor.getDescripcion("msjExpresionErr")%>" + ":" ;
            for (i = 0; i < campos_fin.length; i++) 
            {            
                msnj = msnj + "<br>" + "     " + campos_fin[i];                 
            }      
            jsp_alerta("A",msnj);                        
       }
    }
    else if ("grabadoFinalizar" == respOpcion) {
        var errorGrabarDatosSuplFichero = "<%=request.getAttribute("errorGrabarDatosSuplFichero")%>";
        if (errorGrabarDatosSuplFichero == "1")
            jsp_alerta("A","<%=descriptor.getDescripcion("errGrabarDatosFichero")%> ");
        parent.mainFrame.procesoFinalizar();
    }else if ( ("finalizado"==respOpcion) || ("noFinalizado"==respOpcion) || ("yaFinalizado"==respOpcion)
            || ("noFinalizadoConTramites"==respOpcion) || ("noFinalizadoConResolucion"==respOpcion)
            || ("noFinalizadoConPregunta"==respOpcion) ) {
        parent.mainFrame.finalizacionTramite(respOpcion, procedimientoAsociado);
        
        if ("finalizado"==respOpcion) {
            notificacionRealizada(notifRealizada);
        } else if ("noFinalizado"==respOpcion) {
            if(noPulsarVolver==null || noPulsarVolver!="SI")
                parent.mainFrame.pulsarVolver();            
        }
    } else if ( ("expedienteFinalizado"==respOpcion) || ("expedienteNoFinalizado"==respOpcion) ||
                    ("expedienteConTramitesIniciados"==respOpcion) || ("noFinalizadoFirmasExpediente"==respOpcion)) {
                    parent.mainFrame.finalizacionExpediente(respOpcion, procedimientoAsociado);
                    if ("expedienteFinalizado"==respOpcion) {
                        notificacionRealizada(notifRealizada);                        
                    }
                } else if ( "iniciado"==respOpcion) {
                    var listaTramites = new Array(
          <%
        TramitacionExpedientesForm teForm = (TramitacionExpedientesForm) session.getAttribute("TramitacionExpedientesForm");
        TramitacionExpedientesValueObject tEVO1 = teForm.getTramitacionExpedientes();
        Vector tramites = new Vector();
        tramites = tEVO1.getListaTramitesExpediente();
        if (tramites != null) {
            int lengthTramites = tramites.size();
            if (lengthTramites > 0) {%>

                        new Array("<%=(String) ((GeneralValueObject) tramites.get(0)).getAtributo("ocurrenciaTramite")%>",
                        "<%=(String) ((GeneralValueObject) tramites.get(0)).getAtributo("codTramite")%>",
                        "<%=(String) ((GeneralValueObject) tramites.get(0)).getAtributo("tramite")%>",
                        "<%=(String) ((GeneralValueObject) tramites.get(0)).getAtributo("fehcaInicio")%>",
                        "<%=(String) ((GeneralValueObject) tramites.get(0)).getAtributo("fechaFin")%>",
                        "<%=(String) ((GeneralValueObject) tramites.get(0)).getAtributo("unidad")%>",
                        "<%=(String) ((GeneralValueObject) tramites.get(0)).getAtributo("usuario")%>",
                        "<%=(String) ((GeneralValueObject) tramites.get(0)).getAtributo("clasificacion")%>",
                        "<%=(String) ((GeneralValueObject) tramites.get(0)).getAtributo("consultar")%>",
                        "<%=(String) ((GeneralValueObject) tramites.get(0)).getAtributo("fueraDePlazo")%>",
                        "<%=(String) ((GeneralValueObject) tramites.get(0)).getAtributo("codUniTramTramite")%>",
                        "<%=(String) ((GeneralValueObject) tramites.get(0)).getAtributo("tramiteInicio")%>",
                        "<%=(String) ((GeneralValueObject) tramites.get(0)).getAtributo("codUsuario")%>",
                        "<%=(String) ((GeneralValueObject) tramites.get(0)).getAtributo("codUsuarioFinalizacion")%>",
                        "<%=(String) ((GeneralValueObject) tramites.get(0)).getAtributo("usuarioBloq")%>")
          <%        }
                for (int i = 1; i < lengthTramites; i++) {%>

                            , new Array("<%=(String) ((GeneralValueObject) tramites.get(i)).getAtributo("ocurrenciaTramite")%>",
                            "<%=(String) ((GeneralValueObject) tramites.get(i)).getAtributo("codTramite")%>",
                            "<%=(String) ((GeneralValueObject) tramites.get(i)).getAtributo("tramite")%>",
                            "<%=(String) ((GeneralValueObject) tramites.get(i)).getAtributo("fehcaInicio")%>",
                            "<%=(String) ((GeneralValueObject) tramites.get(i)).getAtributo("fechaFin")%>",
                            "<%=(String) ((GeneralValueObject) tramites.get(i)).getAtributo("unidad")%>",
                            "<%=(String) ((GeneralValueObject) tramites.get(i)).getAtributo("usuario")%>",
                            "<%=(String) ((GeneralValueObject) tramites.get(i)).getAtributo("clasificacion")%>",
                            "<%=(String) ((GeneralValueObject) tramites.get(i)).getAtributo("consultar")%>",
                            "<%=(String) ((GeneralValueObject) tramites.get(i)).getAtributo("fueraDePlazo")%>",
                            "<%=(String) ((GeneralValueObject) tramites.get(i)).getAtributo("codUniTramTramite")%>",
                            "<%=(String) ((GeneralValueObject) tramites.get(i)).getAtributo("tramiteInicio")%>",
                            "<%=(String) ((GeneralValueObject) tramites.get(i)).getAtributo("codUsuario")%>",
                            "<%=(String) ((GeneralValueObject) tramites.get(i)).getAtributo("codUsuarioFinalizacion")%>",
                            "<%=(String) ((GeneralValueObject) tramites.get(i)).getAtributo("usuarioBloq")%>")

          <%        }
        }%>

                );
                    parent.mainFrame.tramiteIniciado(listaTramites,notifRealizada);
                } else if ( ("tramitesPendientes"==respOpcion) ) {
                    var j = 0;
                <%
        TramitacionExpedientesForm tEF = (TramitacionExpedientesForm) session.getAttribute("TramitacionExpedientesForm");
        TramitacionExpedientesValueObject tEVO = tEF.getTramitacionExpedientes();
        Vector tramitesPendientes = tEVO.getListaTramitesPendientes();

        if (tramitesPendientes != null) {
            for (int i = 0; i < tramitesPendientes.size(); i++) {
                GeneralValueObject tramitePendiente = (GeneralValueObject) tramitesPendientes.get(i);
                Vector condicionesNoCumplidas = (Vector) tramitePendiente.getAtributo("listaCondiciones");
                if (condicionesNoCumplidas != null) {
                    for (int z = 0; z < condicionesNoCumplidas.size(); z++) {
                        GeneralValueObject condicion = (GeneralValueObject) condicionesNoCumplidas.get(z);
%>
            var camposAnalizados = new Array();
<%
                Vector nombresAnalizados = (Vector) condicion.getAtributo("nombresCampos");
                if (nombresAnalizados != null) {

                    for (int k = 0; k < nombresAnalizados.size(); k++) {
                %>
                            camposAnalizados[<%=k%>] = "<%=nombresAnalizados.get(k)%>";
<%
                    }
                }
%>
            listaTramitesPendientes[j] = ["<%=tramitePendiente.getAtributo("numeroExpediente")%>",
                "<%=tramitePendiente.getAtributo("codTramite")%>",
                "<%=tramitePendiente.getAtributo("descTramite")%>",
                "<%=condicion.getAtributo("tipoCondicion")%>",
                "<%=condicion.getAtributo("descTramite")%>",
                "<%=condicion.getAtributo("estadoTramite")%>",
                camposAnalizados,
                "<%=condicion.getAtributo("descDocumento")%>",
                "<%=condicion.getAtributo("estadoFirma")%>"];
            j++;
<%
                    }
                }
            }
        }
%>
            if (codUnidadTramitadoraManual == 'null' || codUnidadTramitadoraManual == '' || codUnidadTramitadoraManual == null) {
            parent.mainFrame.tramitesPendientes(notifRealizada, resultadoFinalizar);
            } else {
                parent.mainFrame.tramitesPendientes(listaTramitesPendientes);
            }
            
        } else if ( ("tramitesSinFinalizar" == respOpcion) ) {
            parent.mainFrame.finalizarExpediente("<%=descriptor.getDescripcion("msjTramitesAbiertos")%>");
        } else if ( ("domicilioNoGrabado"==respOpcion) || ("domicilioGrabado"==respOpcion) ) {
            var codLoc = "";
            if(("domicilioGrabado"==respOpcion) ) {
                codLoc = '<bean:write name="TramitacionExpedientesForm" property="codLocalizacion"/>';
            }
            parent.mainFrame.grabacionDomicilio(respOpcion,codLoc);
        } else if ( ("localizacionNoGrabado"==respOpcion) || ("localizacionGrabado"==respOpcion) ) {
            var codLoc = "";
            if(("localizacionGrabado"==respOpcion) ) {
                codLoc = '<bean:write name="TramitacionExpedientesForm" property="codLocalizacion"/>';
            }	else codLoc="";
            parent.mainFrame.grabacionLocalizacion(respOpcion,codLoc);
        } else if ( ("localizacionNoEliminado"==respOpcion) || ("localizacionEliminado"==respOpcion) ) {
            parent.mainFrame.eliminarLocalizacion(respOpcion);
        } else if ( ("diasHabiles"==respOpcion) ) {
            var fechaLimite = '<bean:write name="TramitacionExpedientesForm" property="fechaLimite"/>';
            parent.mainFrame.diasHabiles(fechaLimite);
        } else if ( ("volverAlJSP"==respOpcion) ) {
            parent.mainFrame.editor();
        } else if ( ("volverAlJSP2"==respOpcion) ) {
            parent.mainFrame.editorModificar();
        } else if ( ("eliminadoCRD"==respOpcion) || ("noEliminadoCRD"==respOpcion) ) {
            parent.mainFrame.eliminacionCRD(respOpcion);
        } else if ( ("cambiadoEstadoFirmaDocumentoCRD"==respOpcion) || ("noCambiadoEstadoFirmaDocumentoCRD"==respOpcion) || (respOpcion == "cambiadoEstadoFirmaDocumentoCRDNoEnviadoMail")) {
            var respNuevoEstadoFirma ='<bean:write name="TramitacionExpedientesForm" property="estadoFirma"/>';
            var respCodigoDocumento ='<bean:write name="TramitacionExpedientesForm" property="codDocumento"/>';
            parent.mainFrame.callbackCambioEstadoDocumento(respOpcion, respCodigoDocumento, respNuevoEstadoFirma);
        } else if ( ("actualizarTablaDocumentos"==respOpcion) ) {

            // Para que muestre los resultados de grabaar documentos.



            var esInsercion = '<%=session.getAttribute("resGrabarDocumento")%>';
            if(esInsercion != "null" && esInsercion=="resGrabarDocumentoOK"){
                msnj = "<%=descriptor.getDescripcion("resGrabarDocumentoOK")%>";
                jsp_alerta("A",msnj);
            }else if(esInsercion=="resGrabarDocumentoKO"){
                msnj = "<%=descriptor.getDescripcion("resGrabarDocumentoKO")%>";
                jsp_alerta("A",msnj);
            }
            <% session.removeAttribute("resGrabarDocumento");%>
                        <% int posicionDocumento=0; %>
                            <logic:iterate id="elemento" name="TramitacionExpedientesForm" property="listaDocumentos">
                            
                            var rel = "";
                        var relac = '<bean:write name="elemento" property="relacion" />';
                        if(relac == "S") {
                            rel = "UNICO";
                        } else {
                            rel = "EXPEDIENTE";
                        }
                        
                         var cadenaEditorTexto = '<bean:write name="elemento" property="editorTexto"/>';
                          if (cadenaEditorTexto === '') {
                                     cadenaEditorTexto = TEXTOS_I18_EDITOR_TEXTO_NULL;
                         }
                        if (listaDocumentos) listaDocumentos[cont] = ['<bean:write name="elemento" property="descDocumento" />',
                            '<bean:write name="elemento" property="fechaCreacion" />',
                            '<bean:write name="elemento" property="fechaModificacion"/>',
                            <c:if test="${elemento.fechaInforme ne ' '}">
                                '<bean:write name="elemento" property="fechaInforme"/>&nbsp;<a href="javascript:modificarFechaInforme(<c:out value='${elemento.codDocumento}'/>,<%=posicionDocumento%>);"><span class="fa fa-calendar" aria-hidden="true" title="<%=descriptor.getDescripcion("etiq_modFecInformeDoc")%>" /></span></a>',
                            </c:if>
                            <c:if test="${elemento.fechaInforme eq ' '}">
                                '<a href="javascript:modificarFechaInforme(<c:out value='${elemento.codDocumento}'/>,<%=posicionDocumento%>);"><span class="fa fa-calendar" aria-hidden="true" title="<%=descriptor.getDescripcion("etiq_modFecInformeDoc")%>"></span></a>',
                            </c:if>
                            '<bean:write name="elemento" property="usuario"/>'
                            , parent.mainFrame.getEstadoFirmaVisual('<bean:write name="elemento" property="codDocumento" />',
                                    '<bean:write name="elemento" property="estadoFirma"/>',rel),
                           cadenaEditorTexto
                        ];
                        if (listaDocumentosOriginal) listaDocumentosOriginal[cont] = ['<bean:write name="elemento" property="codDocumento" />',
                            '<bean:write name="elemento" property="descDocumento" />',
                            '<bean:write name="elemento" property="fechaCreacion" />',
                            '<bean:write name="elemento" property="fechaModificacion"/>',
                            '<bean:write name="elemento" property="fechaInforme"/>',
                            '<bean:write name="elemento" property="usuario"/>',
                            '<bean:write name="elemento" property="interesado"/>'
                            ,'<bean:write name="elemento" property="estadoFirma"/>'
                            ,'<bean:write name="elemento" property="relacion"/>'
                            ,'<bean:write name="elemento" property="editorTexto"/>'
                        ];
                        
                        cont++;
                        <% posicionDocumento++; %>
                            </logic:iterate>
                            parent.mainFrame.actualizaTabla(listaDocumentos,listaDocumentosOriginal);
                    } else if ( ("actualizarTablaInteresados"==respOpcion) ) {
                        var cont1 = 0;
                        var checkEntreg1 = "<input type='checkbox' class='check' name='box";
                        var checkEntreg2 = "' value='SI'>";
          <% Vector listaInteresados = new Vector();
        listaInteresados = teForm.getListaInteresados();
        if (listaInteresados != null) {
            for (int i = 0; i < listaInteresados.size(); i++) {
                GeneralValueObject g = new GeneralValueObject();
                g = (GeneralValueObject) listaInteresados.elementAt(i);
          %>
                      var check = checkEntreg1+cont1+checkEntreg2;
                      listaInteresados[cont1] = [check,"<%=(String) g.getAtributo("titular")%>","<%=(String) g.getAtributo("descRol")%>"];
                      listaInteresadosOriginal[cont1] = ["<%=(String) g.getAtributo("codTercero")%>",
                          "<%=(String) g.getAtributo("versTercero")%>","<%=(String) g.getAtributo("titular")%>",
                          "<%=(String) g.getAtributo("codRol")%>","<%=(String) g.getAtributo("descRol")%>",
                          "<%=(String) g.getAtributo("rolPorDefecto")%>","<%=(String) g.getAtributo("codMunicipio")%>",
                          "<%=(String) g.getAtributo("codProcedimiento")%>","<%=(String) g.getAtributo("numeroExpediente")%>",
                          "<%=(String) g.getAtributo("ejercicio")%>"];
                      cont1++;
            <% }
        }%>

                    parent.mainFrame.actualizaTablaInteresados(listaInteresados,listaInteresadosOriginal);

                    //diana- recargar la tabla de formularios

                } else if ("noEliminadoFormPDF"==respOpcion) {
                    jsp_alerta('A', '<%= descriptor.getDescripcion("msjNoElimForm")%>');
                } else if ("eliminadoFormPDF"==respOpcion) {

                    cont = 0;
                    parent.mainFrame.listaFormsPDF = new Array();
                    parent.mainFrame.listaFormsPDFOriginal = new Array();
                    var estados = ['<%= descriptor.getDescripcion("etEstado0Form")%>','<%= descriptor.getDescripcion("etEstado1Form")%>','<%= descriptor.getDescripcion("etEstado2Form")%>','<%= descriptor.getDescripcion("etEstado3Form")%>'];

                    // Comprobar que la colección del action no sea null.
                        <logic:notEmpty name="TramitacionExpedientesForm" property="listaFormsPDF">
                        <logic:iterate id="elem" name="TramitacionExpedientesForm" property="listaFormsPDF">

                        parent.mainFrame.listaFormsPDF[cont] = ['<bean:write name="elem" property="codigo" />',
                        '<bean:write name="elem" property="descripcion" />',
                        '<fmt:formatDate value="${elem.fecMod.time}" type="both" pattern="dd/MM/yyyy HH:mm"/>',
                        '<bean:write name="elem" property="usuario"/>',
                        estados[<bean:write name="elem" property="estado"/>]
                    ];
                    parent.mainFrame.listaFormsPDFOriginal[cont] = ['<bean:write name="elem" property="tipo" />',
                        '<bean:write name="elem" property="codigo" />',
                        '<bean:write name="elem" property="descripcion" />',
                        '<fmt:formatDate value="${elem.fecMod.time}" type="both" pattern="dd/MM/yyyy HH:mm"/>',
                        '<bean:write name="elem" property="usuario"/>',
                        '<bean:write name="elem" property="estado"/>'
                    ];
                    cont++;
                        </logic:iterate>
                        </logic:notEmpty>
                        parent.mainFrame.tabFormsPDF.lineas=parent.mainFrame.listaFormsPDF;
                    parent.mainFrame.calcularPuedeAltaAdjunto();
                    parent.mainFrame.tabFormsPDF.displayTabla();

                    //fin diana
                } else if ("iniciadoProcAsociado" == respOpcion) {
                    // Se ha iniciado un procedimiento relacionado.
                    // Se muestra aviso.
                    expAsociadoIniciado();
                    
                    // Decidimos accion despues del aviso para redirigir al usuario
                    if ("expedienteFinalizado" == resultadoFinalizar)
                        parent.mainFrame.pulsarVolverBuzonExpPendientes();
                    else
                        parent.mainFrame.pulsarVolver1();                        
                } else if ("seleccionarInfoInicio" == respOpcion) {
                    var msj = "<%=descriptor.getDescripcion("msjIniProcAsocPre")%> " + procedimientoAsociado + 
                            ". <%=descriptor.getDescripcion("msjIniProcAsocPost")%>";
                    jsp_alerta('A', msj);
                    // Hay que seleccionar la información de inicio del procedimiento relacionado
                    var unidadesExp = new Array();
                    var indexUndExp = 0;
                    <c:forEach var="unidadExp" items="${requestScope.unidadesExpediente}">
                        unidadesExp[indexUndExp] = ['<c:out value="${unidadExp.identificador}"/>',
                                '<c:out value="${unidadExp.descripcion}"/>',
                                '<c:out value="${unidadExp.codigo}"/>'];
                        indexUndExp++;
                    </c:forEach>
                    if (unidadesExp.length > 1) {
                        var source = "<c:url value='/jsp/sge/listaUORsUsuario.jsp?tipoSel=1'/>";
                        abrirXanelaAuxiliar(source,unidadesExp,
                                'width=600,height=365,scrollbars=no,status='+ '<%=statusBar%>',function(resultado){
                                    uorInicioExp = resultado;
                                    envioUnidadesTram();
                                });
                    } else {
                        uorInicioExp = unidadesExp[0][2];
                        envioUnidadesTram();
                    }
                }
            }

function envioUnidadesTram() {
    var unidadesTram = new Array();
    var indexUndTram = 0;
    <c:forEach var="unidadTram" items="${requestScope.unidadesTramite}">
        unidadesTram[indexUndTram] = ['<c:out value="${unidadTram.uor_cod_vis}"/>',
                '<c:out value="${unidadTram.uor_nom}"/>',
                '<c:out value="${unidadTram.uor_cod}"/>'];
        indexUndTram++;
    </c:forEach>
    if (unidadesTram.length == 0) {
        parent.mainFrame.enviarInfoExpIniciado(uorInicioExp, uorInicioExp);
    } else if (unidadesTram.length > 1) {
        var source = "<c:url value='/jsp/sge/listaUORsUsuario.jsp?tipoSel=2'/>";
        abrirXanelaAuxiliar(source,unidadesTram,
	'width=600,height=365,scrollbars=no,status='+ '<%=statusBar%>',function(uorInicioTram){
                        parent.mainFrame.enviarInfoExpIniciado(uorInicioExp, uorInicioTram);
                    });
    } else {
        parent.mainFrame.enviarInfoExpIniciado(uorInicioExp, unidadesTram[0][2]);
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
