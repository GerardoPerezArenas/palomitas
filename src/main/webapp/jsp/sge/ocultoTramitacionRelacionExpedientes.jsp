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
<%@page import="java.util.ArrayList"%>
<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title> Tramitacion expedientes </title>
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
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>

<script>

var listaTramitesPendientes = new Array();
var listaDocumentos = new Array();
var listaDocumentosOriginal = new Array();
var listaTramitesNoFinalizados = new Array();
var cont = 0;
var listaInteresados = new Array();
var listaInteresadosOriginal = new Array();

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
function redirecciona(){
        var notifRealizada ='<bean:write name="TramitacionExpedientesForm" property="notificacionRealizada"/>';
        var respOpcion ='<bean:write name="TramitacionExpedientesForm" property="respOpcion"/>';
		var mensajeSW ='<bean:write name="TramitacionExpedientesForm" property="mensajeSW"/>';
        var resultadoFinalizar = '<bean:write name="TramitacionExpedientesForm" property="resultadoFinalizar"/>';
		if (mensajeSW != "") {
			jsp_alerta("A", mensajeSW)
		}
                
        var errorGrabarDatosSuplFichero = "<%=request.getAttribute("errorGrabarDatosSuplFichero")%>";
        if (errorGrabarDatosSuplFichero == "1")
            jsp_alerta("A","<%=descriptor.getDescripcion("errGrabarDatosFichero")%> ");

        if ( ("noGrabado"==respOpcion) || ("grabado"==respOpcion) ){
           parent.mainFrame.grabacionTramite(respOpcion);
           if("grabado"==respOpcion){
               /** LLAMADA A OPERACIONES JAVASCRIPTS DE LAS PESTAÑAS DE TRÁMITE DE MODULOS EXTERNOS **/
                var funciones = new Array();
                var contador = 0;
                <%
                  TramitacionExpedientesForm tramitacionExpForm = (TramitacionExpedientesForm)session.getAttribute("TramitacionExpedientesForm");
                  ArrayList<String> funciones = tramitacionExpForm.getFuncionesJavascriptModulosExternos();
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
           }//if
           
            var errorGrabarDatosSuplFichero = "<%=request.getAttribute("errorGrabarDatosSuplFichero")%>";
            if (errorGrabarDatosSuplFichero == "1")
                jsp_alerta("A","<%=descriptor.getDescripcion("errGrabarDatosFichero")%> ");

        }else if ( ("finalizado"==respOpcion)
                || ("noFinalizado"==respOpcion)
                || ("noFinalizadoConTramites"==respOpcion)
                || ("noFinalizadoConResolucion"==respOpcion)
                || ("noFinalizadoConPregunta"==respOpcion) ) {
          parent.mainFrame.finalizacionTramite(respOpcion);
          if ("finalizado"==respOpcion) {
              notificacionRealizada(notifRealizada);
              expAsociadoIniciado();
          } else if ("noFinalizado"==respOpcion) {
			  parent.mainFrame.pulsarVolver();
          }
        } else if ( ("expedienteFinalizado"==respOpcion) || ("expedienteNoFinalizado"==respOpcion) || ("expedienteConTramitesIniciados"==respOpcion) ) {
          parent.mainFrame.finalizacionExpediente(respOpcion);
          if ("expedienteFinalizado"==respOpcion) {
            notificacionRealizada(notifRealizada);
              expAsociadoIniciado();
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
              if (lengthTramites > 0 ) { %>

                  new Array("<%=(String)((GeneralValueObject)tramites.get(0)).getAtributo("ocurrenciaTramite")%>",
                  "<%=(String)((GeneralValueObject)tramites.get(0)).getAtributo("codTramite")%>",
                  "<%=(String)((GeneralValueObject)tramites.get(0)).getAtributo("tramite")%>",
                  "<%=(String)((GeneralValueObject)tramites.get(0)).getAtributo("fehcaInicio")%>",
                  "<%=(String)((GeneralValueObject)tramites.get(0)).getAtributo("fechaFin")%>",
                  "<%=(String)((GeneralValueObject)tramites.get(0)).getAtributo("unidad")%>",
                  "<%=(String)((GeneralValueObject)tramites.get(0)).getAtributo("usuario")%>",
                  "<%=(String)((GeneralValueObject)tramites.get(0)).getAtributo("clasificacion")%>",
				  "<%=(String)((GeneralValueObject)tramites.get(0)).getAtributo("consultar")%>",
		          "<%=(String)((GeneralValueObject)tramites.get(0)).getAtributo("fueraDePlazo")%>",
		          "<%=(String)((GeneralValueObject)tramites.get(0)).getAtributo("codUniTramTramite")%>",
				  "<%=(String)((GeneralValueObject)tramites.get(0)).getAtributo("tramiteInicio")%>",
				  "<%=(String)((GeneralValueObject)tramites.get(0)).getAtributo("codUsuario")%>",
                  "<%=(String)((GeneralValueObject)tramites.get(0)).getAtributo("codUsuarioFinalizacion")%>",
                  "<%=(String)((GeneralValueObject)tramites.get(0)).getAtributo("usuarioBloq")%>")
          <%        }
                  for(int i=1;i<lengthTramites;i++){ %>

                  , new Array("<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("ocurrenciaTramite")%>",
                  "<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("codTramite")%>",
                  "<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("tramite")%>",
                  "<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("fehcaInicio")%>",
                  "<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("fechaFin")%>",
                  "<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("unidad")%>",
                  "<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("usuario")%>",
                  "<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("clasificacion")%>",
				  "<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("consultar")%>",
		          "<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("fueraDePlazo")%>",
		          "<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("codUniTramTramite")%>",
				  "<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("tramiteInicio")%>",
				  "<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("codUsuario")%>",
                  "<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("codUsuarioFinalizacion")%>",
                  "<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("usuarioBloq")%>")

          <%        }
              }        %>

            );
          parent.mainFrame.tramiteIniciado(listaTramites,notifRealizada);
        } else if ( ("tramitesPendientes"==respOpcion) ) {
          var j = 0;
   		        <%
        TramitacionExpedientesForm tEF = (TramitacionExpedientesForm)  session.getAttribute("TramitacionExpedientesForm");
        TramitacionExpedientesValueObject tEVO = tEF.getTramitacionExpedientes();
    Vector tramitesPendientes = tEVO.getListaTramitesPendientes();

        if ( tramitesPendientes != null) {
        for(int i=0; i<tramitesPendientes.size(); i++) {
            GeneralValueObject tramitePendiente = (GeneralValueObject)tramitesPendientes.get(i);
            Vector condicionesNoCumplidas = (Vector)tramitePendiente.getAtributo("listaCondiciones");
            if(condicionesNoCumplidas!= null){
            for (int z=0; z < condicionesNoCumplidas.size(); z++) {
                GeneralValueObject condicion = (GeneralValueObject)condicionesNoCumplidas.get(z);
%>
            var camposAnalizados = new Array();
<%
                Vector nombresAnalizados = (Vector)condicion.getAtributo("nombresCampos");
            if (nombresAnalizados!=null) {
     
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
                    camposAnalizados];
            j++;
<%
            }
        }
        }
        }
%>
   		    

        parent.mainFrame.tramitesPendientes(listaTramitesPendientes,notifRealizada, resultadoFinalizar);
        } else if ( ("tramitesSinFinalizar"==respOpcion) ) {
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
          <logic:iterate id="elemento" name="TramitacionExpedientesForm" property="listaDocumentos">
              if (listaDocumentos) {
                  opcionGrabar='<bean:write name="elemento" property="opcionGrabar"/>';
                  if (opcionGrabar == '1') img ="<span class="fa fa-check 2x"></span>";
                  else img ="";
                  

                  listaDocumentos[cont] = ['<bean:write name="elemento" property="descDocumento" />',
                        '<bean:write name="elemento" property="fechaCreacion" />',
                        '<bean:write name="elemento" property="fechaModificacion"/>',
                        '<bean:write name="elemento" property="usuario"/>',
                          parent.mainFrame.getEstadoFirmaVisual('<bean:write name="elemento" property="codDocumento" />',
                            '<bean:write name="elemento" property="estadoFirma"/>'),
                          '<bean:write name="elemento" property="editorTexto"/>'
                         ];
                  }
              if (listaDocumentosOriginal) listaDocumentosOriginal[cont] = ['<bean:write name="elemento" property="codDocumento" />',
                        '<bean:write name="elemento" property="descDocumento" />',
                        '<bean:write name="elemento" property="fechaCreacion" />',
                        '<bean:write name="elemento" property="fechaModificacion"/>',
                        '<bean:write name="elemento" property="usuario"/>',
                        '<bean:write name="elemento" property="interesado"/>',
                        '<bean:write name="elemento" property="estadoFirma"/>',
                        '<bean:write name="elemento" property="opcionGrabar"/>',
                        '<bean:write name="elemento" property="editorTexto"/>'
                            ];
              cont++;
          </logic:iterate>
          parent.mainFrame.actualizaTabla(listaDocumentos,listaDocumentosOriginal);
        } else if ( ("actualizarTablaInteresados"==respOpcion) ) {
		  var cont1 = 0;
		  var checkEntreg1 = "<input type='checkbox' class='check' name='box";
          var checkEntreg2 = "' value='SI'>";
		  <% Vector listaInteresados = new Vector();
		     listaInteresados = teForm.getListaInteresados();
			 if(listaInteresados != null) {
			   for(int i=0;i<listaInteresados.size();i++) {
               GeneralValueObject g = new GeneralValueObject();
	           g = (GeneralValueObject) listaInteresados.elementAt(i);
          %>
	      var check = checkEntreg1+cont1+checkEntreg2;
	      listaInteresados[cont1] = [check,"<%=(String)g.getAtributo("titular")%>","<%=(String)g.getAtributo("descRol")%>"];
		  listaInteresadosOriginal[cont1] = ["<%=(String)g.getAtributo("codTercero")%>",
		                       "<%=(String)g.getAtributo("versTercero")%>","<%=(String)g.getAtributo("titular")%>",
							   "<%=(String)g.getAtributo("codRol")%>","<%=(String)g.getAtributo("descRol")%>",
							   "<%=(String)g.getAtributo("rolPorDefecto")%>","<%=(String)g.getAtributo("codMunicipio")%>",
							   "<%=(String)g.getAtributo("codProcedimiento")%>","<%=(String)g.getAtributo("numeroExpediente")%>",
							   "<%=(String)g.getAtributo("ejercicio")%>"];
		  cont1++;
            <% } 
		    }%>
		  
          parent.mainFrame.actualizaTablaInteresados(listaInteresados,listaInteresadosOriginal);

            //diana- recargar la tabla de formularios

        } else if ("noEliminadoFormPDF"==respOpcion) {
               alert("No se pudo eliminar el formulario");
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
