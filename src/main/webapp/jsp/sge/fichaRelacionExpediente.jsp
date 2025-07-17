<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.technical.EstructuraCampo"%>
<%@page import="java.util.Vector"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.business.sge.persistence.manual.DatosSuplementariosDAO"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.FichaRelacionExpedientesForm"%>
<%@ page import="es.altia.agora.technical.CamposFormulario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.DatosPantallaModuloVO"%>

<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title>::: Ficha Relacion Expedientes:::</title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<%
    String fechaFinExpediente = "";
    int idioma=1;
    int apl=4;
    int pestana = 0;
	String soloConsultarExp = "no";
	String codUsu = "";
    UsuarioValueObject usuario=new UsuarioValueObject();
    FichaRelacionExpedientesForm fichaRelExpForm=new FichaRelacionExpedientesForm();
    Log _log = LogFactory.getLog(this.getClass());
    if (session!=null){
      if (usuario!=null) {
        usuario = (UsuarioValueObject)session.getAttribute("usuario");
        fichaRelExpForm = (FichaRelacionExpedientesForm)session.getAttribute("FichaRelacionExpedientesForm");
        idioma = usuario.getIdioma();
        apl = usuario.getAppCod();
		int cUsu = usuario.getIdUsuario();
        codUsu = Integer.toString(cUsu);
		soloConsultarExp = usuario.getSoloConsultarExp();
      }

      fechaFinExpediente = fichaRelExpForm.getFechaFin();
      String pestanaInicial = (String) session.getAttribute("pestana");
      if (pestanaInicial != null) {
        if ("tramites".equals(pestanaInicial) )
          pestana=0;
      session.removeValue("pestana");
      }
    }
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String campos="", ocurrencias="";

    String userAgent = request.getHeader("user-agent");  
%>

<!-- Estilos -->

<!-- Beans -->
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<style type="text/css">
   TR.rojo TD {background-color:red;color:white;}
   TR.gris TD {background-color:white;color:#a5a5a5;}
</style>
<script type="text/javascript">
    
    function ventanaPopUpModal(opcion,codigo) {    
        if (document.forms[0].modoConsulta.value!="si") {
            var source = "<html:rewrite page='/sge/DatosSuplementariosFichero.do'/>?opcion=" + opcion + "&codigo="+ codigo;
            abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp'/>?source=" + source ,"ventana1",
	'width=450,height=450,scrollbars=no,status='+ '<%=statusBar%>',function(result){
                        if (result != undefined) {
                            actualizarFicheros(result[0], result[1]);
                        }
                    });
            }        
        }
        
function actualizarFicheros(campo, path){
    eval("document.forms[0]."+campo+".value='"+path+"';");
    eval("habilitarBoton(document.forms[0].cmdVisualizar"+campo+");");
    eval("habilitarBoton(document.forms[0].cmdEliminar"+campo+");");
}

function onClickDocumento(codigo) {
    window.open("<html:rewrite page='/VerDocumentoDatosSuplementarios'/>?codigo=" + codigo +"&opcion=2" ,"ventana1","left=10, top=10, width=650, height=500, scrollbars=no, menubar=no, location=no, resizable=no")
}

function onClickEliminarDocumento(codigo) {
    if(jsp_alerta("C",'<%=descriptor.getDescripcion("msjBorrarFichero")%>') ==1) {
        eval("document.forms[0]."+codigo+".value='';");
        eval("deshabilitarBoton(document.forms[0].cmdVisualizar"+codigo+");");
        eval("deshabilitarBoton(document.forms[0].cmdEliminar"+codigo+");");
    }
}

function onClickVer(nameCampo, plazo, periodoPlazo, campoActivo){               
               var campo = document.getElementByName(nameCampo);
               if (campo==null) return;
               //Si no se ha definido una fecha
               if ((campo.value =='') || (campo.value =="")){ 
                   jsp_alerta('A','<%=descriptor.getDescripcion("msgNoFecha")%>');
               }else{                   
                   if ((plazo=='null')||(plazo=="null")||(plazo==null)){
                       jsp_alerta('A','<%=descriptor.getDescripcion("msgNoPlazo")%>');
                   }else{//se ha definido un plazo
                        var parametros ="opcion=verFechaVencimiento"
                        parametros += "&fecha=";
                        parametros += campo.value;
                        parametros += "&plazo=";
                        parametros += plazo;
                        parametros += "&periodoPlazo=";
                        parametros += periodoPlazo;
                        abrirXanelaAuxiliar('<%=request.getContextPath()%>/sge/TramitacionExpedientes.do?' + parametros,null,
                                'width=400,height=450,status='+ '<%=statusBar%>',function(rslt){});
                   }
               }
            }

            function onClickDesactivar(idCampo){
                if (idCampo==null)return;

               var cmdActivar = document.getElementById("cmdActivar"+idCampo);
               var cmdDesactivar = document.getElementById("cmdDesactivar"+idCampo);
               var campo =  document.getElementById("activar"+idCampo);
               //al pulsar bton desactivar, se oculta el bton activar
               cmdActivar.style.display='';
               cmdActivar.style.display='inline';
               cmdDesactivar.style.display='none';
               campo.value="desactivada"; 

            }
            function onClickActivar(idCampo){
                 if (idCampo==null)return;

               var cmdActivar = document.getElementById("cmdActivar"+idCampo);
               var cmdDesactivar = document.getElementById("cmdDesactivar"+idCampo);
               var campo =  document.getElementById("activar"+idCampo);

             //al pulsar bton desactivar, se oculta el bton activar
             cmdDesactivar.style.display='';
             cmdActivar.style.display='none';
             cmdDesactivar.style.display='inline';
             campo.value="activada"; 

            }


  var tableObject;
  var relacionExpediente = new Array();
  var listaTramites = new Array();
  var listaTramitesOriginal = new Array();
  var listaPermisosTramites = new Array();
  var listaExpedientes = new Array();
  var listaEnlaces = new Array();
  var estructuraDS = new Array();
  var valoresDS = new Array();
  var nombreCombos = new Array();

  var i=0;

  var codigoUsuario = <%= codUsu %>;

  function inicializar(){
	  window.focus();

      tp1.setSelectedIndex(<%=pestana%>);
    <%
    GeneralValueObject fichaRelExpVO = fichaRelExpForm.getFichaRelExpVO();
    String notificacionRealizada = (String) fichaRelExpVO.getAtributo("notificacionRealizada");

    if (notificacionRealizada.equals("no")) { %>
       msnj = "<%=descriptor.getDescripcion("msjNotifNoRealiz")%>";
       jsp_alerta("A",msnj);
    <% } %>

    relacionExpediente = ["<%=fichaRelExpVO.getAtributo("codMunicipio")%>",
                  "<%=fichaRelExpVO.getAtributo("codProcedimiento")%>",
                  "<%=fichaRelExpVO.getAtributo("ejercicio")%>",
                  "<%=fichaRelExpVO.getAtributo("numero")%>"];

      document.forms[0].ejercicio.value = relacionExpediente[2];
    <%
      Vector tramites = fichaRelExpForm.getTramites();
      if (tramites != null) {
          int lengthTramites = tramites.size();
      %>
      var j=0;
      <%
      for(int i=0;i<lengthTramites;i++){
        GeneralValueObject tramiteVO = (GeneralValueObject)tramites.get(i);%>
        listaTramites[j] = ["<%=tramiteVO.getAtributo("tramite")%>",
          "<%=tramiteVO.getAtributo("fehcaInicio")%>",
          "<%=tramiteVO.getAtributo("fechaFin")%>",
          "<%=tramiteVO.getAtributo("unidad")%>",
          "<%=tramiteVO.getAtributo("clasificacion")%>",
          ""];
        <%if (!tramiteVO.getAtributo("usuarioBloq").equals("")) {
            if (tramiteVO.getAtributo("codUsuarioBloq").toString().equals(String.valueOf(usuario.getIdUsuario()))) {%>
              listaTramites[j][5]='<span class="fa fa-lock" alt="<%=tramiteVO.getAtributo("usuarioBloq")%>"></span>';
            <%} else {%>
              listaTramites[j][5]='<span class="fa fa-unlock" alt="<%=tramiteVO.getAtributo("usuarioBloq")%>"></span>';
            <%}
        }%>
        listaTramitesOriginal[j] = ["<%=tramiteVO.getAtributo("ocurrenciaTramite")%>",
          "<%=tramiteVO.getAtributo("codTramite")%>",
          "<%=tramiteVO.getAtributo("tramite")%>",
          "<%=tramiteVO.getAtributo("fehcaInicio")%>",
          "<%=tramiteVO.getAtributo("fechaFin")%>",
          "<%=tramiteVO.getAtributo("unidad")%>",
          "<%=tramiteVO.getAtributo("usuario")%>",
          "<%=tramiteVO.getAtributo("clasificacion")%>",
          "<%=tramiteVO.getAtributo("consultar")%>",
          "<%=tramiteVO.getAtributo("fueraDePlazo")%>",
          "<%=tramiteVO.getAtributo("codUniTramTramite")%>",
		  "<%=tramiteVO.getAtributo("tramiteInicio")%>",
		  "<%=tramiteVO.getAtributo("codUsuario")%>",
		  "<%=tramiteVO.getAtributo("codUsuarioFinalizacion")%>",
          "<%=tramiteVO.getAtributo("usuarioBloq")%>",
          "<%=tramiteVO.getAtributo("codUsuarioBloq")%>"];
          
        j++;
      <%
      }
    }
      %>
    tabTramites.lineas = listaTramites;
    tabTramites.displayTabla();

   <%Vector permisosTramites = null;
    permisosTramites = (Vector) fichaRelExpForm.getPermisosTramites();
    if (permisosTramites != null) {
        int lengthPermisosTramites = permisosTramites.size();%>
        var j=0;
        <%for(int i=0;i<lengthPermisosTramites;i++){%>
            listaPermisosTramites[j] = "<%=(String)permisosTramites.get(i)%>";
            j++;
        <%}
    }%>
    document.forms[0].modoConsulta.value = "<%=fichaRelExpVO.getAtributo("modoConsulta")%>";
    document.forms[0].desdeConsulta.value = "<%=fichaRelExpVO.getAtributo("desdeConsulta")%>";
    document.forms[0].deAdjuntar.value = "<%=fichaRelExpVO.getAtributo("deAdjuntar")%>";
	document.forms[0].desdeInformesGestion.value = "<%=fichaRelExpVO.getAtributo("desdeInformesGestion")%>";
	document.forms[0].todos.value = "<%=fichaRelExpVO.getAtributo("todos")%>";
    document.forms[0].observaciones.value = unescape("<%=fichaRelExpVO.getAtributo("observaciones")%>");
	document.forms[0].asunto.value = unescape("<%=fichaRelExpVO.getAtributo("asunto")%>");
    cargarDocumentos();
    mostrarCapasBotones('capaBotones3');

    document.forms[0].desdeExpRel.value="<%=request.getParameter("desdeExpRel")%>";
    document.forms[0].porCampoSup.value="<%=request.getParameter("porCampoSup")%>";

    if(document.forms[0].modoConsulta.value == "si") {
	  desactivarFormulario();
      mostrarCapasBotones('capaBotones1');
      tabDocumentos.readOnly = true;
      document.forms[0].cmdTramitacionManual.disabled = true;
      document.forms[0].cmdTramitacionManual.style.cursor = '';
	<%if(soloConsultarExp.equals("no")) { %>
	  if(document.forms[0].fechaFin.value == "") {
	    document.forms[0].cmdRetrocederExpediente.style.cursor = 'hand';
	    document.forms[0].cmdRetrocederExpediente.disabled = false;
	  }
	<% } %>
      if(document.forms[0].deAdjuntar.value == "si") {
        mostrarCapasBotones('capaBotones2');
        document.forms[0].cmdCancelar.disabled = false;
        document.forms[0].cmdAceptar.disabled = false;
        document.forms[0].cmdCancelar.style.cursor = 'hand';
        document.forms[0].cmdAceptar.style.cursor = 'hand';
		document.forms[0].cmdRetrocederExpediente.style.cursor = '';
	    document.forms[0].cmdRetrocederExpediente.disabled = true;
      }
	  if(document.forms[0].desdeInformesGestion.value == "si") {
	    document.forms[0].cmdRetrocederExpediente.style.cursor = '';
	    document.forms[0].cmdRetrocederExpediente.disabled = true;
	  }
    }
      
  <%
  Vector expedientes = fichaRelExpForm.getExpedientes();
  if (expedientes != null) {
      int lengthExpedientes = expedientes.size();
  %>
      var m = 0;
  <%
  for(int l=0;l<lengthExpedientes;l++){
      String asuntoExp = (String)((GeneralValueObject)expedientes.get(l)).getAtributo("asuExp");      
      asuntoExp = asuntoExp.replaceAll("\r", "\n");
      asuntoExp = asuntoExp.replaceAll("\n", " ");

  %>
      listaExpedientes[m] = ["<%=((GeneralValueObject)expedientes.get(l)).getAtributo("numExp")%>",
              "<%=((GeneralValueObject)expedientes.get(l)).getAtributo("titExp")%>",
              unescape("<%=StringEscapeUtils.escapeJavaScript(asuntoExp)%>")];
       m++;
  <%}
  }%>
    tabExp.lineas = listaExpedientes;
    tabExp.displayTabla();
    //refrescaExpedientes();
    <%
      Vector enlaces = fichaRelExpForm.getEnlaces();
      if (enlaces != null) {
      int lengthEnlaces = enlaces.size();
    %>
    m=0;
    <%
      for(int l=0;l<lengthEnlaces;l++){
    %>
      listaEnlaces[m] = ["<%=((GeneralValueObject)enlaces.get(l)).getAtributo("descEnlace")%>",
                         "<%=((GeneralValueObject)enlaces.get(l)).getAtributo("url")%>"];
      m++;
    <%
      }
    }
    %>
    domlay('capaEnlaces',1,0,0,enlaces());
    if (document.getElementById('capaDatosSuplementarios'))
      document.getElementById('capaDatosSuplementarios').style.display='';
    inicializarBotonesDatosSuplementarios();
  }

function enlaces() {
  var htmlString = "";
  if (listaEnlaces.length > 0){
    htmlString += '<table border="0px" cellpadding="2" cellspacing="4" align="left" width="100%">';
    for(i=0;i<listaEnlaces.length;i++) {
      htmlString += '<tr><td width="100%" align="left" class="etiqueta">';
      htmlString += '<a href=' + listaEnlaces[i][1] + ' target=blank><b>';
      htmlString += listaEnlaces[i][0] + '</font></b></a></td></tr>';
    }
    htmlString += '</table>';
  }
  return (htmlString);
}

  function desactivarFormulario() {
    deshabilitarDatos(document.forms[0]);
    document.forms[0].cmdExpRel.disabled = false;
    document.forms[0].cmdExpRel.style.cursor = 'hand';
    for(var i=0;i<listaDocumentos.length;i++) {
      eval("document.forms[0].documentoEntregado" + i + ".disabled=true");
    }
    document.forms[0].cmdVolver.disabled = false;
    document.forms[0].cmdVolver.style.cursor = "hand";
  }

  function activarFormulario() {
    habilitarDatos(document.forms[0]);
    for(var i=0;i<listaDocumentos.length;i++) {
      eval("document.forms[0].documentoEntregado" + i + ".disabled=false");
    }
  }


/* Pestaña documentos */

  var listaDocumentos = new Array();
  var listaDocumentosOriginal = new Array();
  //var tabDocumentos;

  function cargarDocumentos(){
  <%
      Vector listaDocumentos= new Vector();
      listaDocumentos = (Vector) fichaRelExpForm.getDocumentos();
      String checkEntreg1 = "<input type='checkbox' class='check' name='documentoEntregado";
      String checkEntreg2 = "' value='SI'";
      String checkEntreg3 =" CHECKED ";
      String checkEntreg4 = ">";

      String entreg;

      if ( listaDocumentos != null ) {
      for (int i=0; i< listaDocumentos.size(); i++ ) {
        GeneralValueObject gVO = (GeneralValueObject) listaDocumentos.elementAt(i);
        String cD = (String) gVO.getAtributo("codigo");
        String nD = (String) gVO.getAtributo("nombre");
        String condD = (String) gVO.getAtributo("condicion");
        String entregado =(String) gVO.getAtributo("ENTREGADO");

        entreg = checkEntreg1+i+checkEntreg2;
        if ("SI".equals(entregado)) entreg += checkEntreg3;
        entreg += checkEntreg4;
  %>
  listaDocumentosOriginal[<%= i %>]  = ["<%= cD %>","<%= nD %>","<%= condD %>"];
  listaDocumentos[<%= i %>]  = ["<%=entreg%>","<%= nD %>","<%= condD %>"];

  <% 	   }
        }
  %>
  tabDocumentos.lineas=listaDocumentos;
  tabDocumentos.displayTabla();
  if (listaDocumentos.length > 0) document.getElementById("capaBotonesDocumentos").style.display='';
  else document.getElementById("capaBotonesDocumentos").style.display='none';
  }


  function desActivarBotonesDocumentos(desactivar) {
    if (document.getElementById("capaBotonesDocumentos").style.display=='') {
      document.forms[0].cmdActualizarDocumentos.disabled = desactivar;
      document.forms[0].cmdCancelarActualizarDocumentos.disabled = desactivar;
    }
  }

  function pulsarCancelarActualizarDocumentos(){
    tabDocumentos.lineas=listaDocumentos;
    tabDocumentos.displayTabla();
  }
/* Fin pestaña documentos */


/* Pestaña tramitacion */
function cargarTramitacion(i) {
    if(listaPermisosTramites[i] == "si") {  //Comprueba si el usuario tiene el mismo cargo del tramite
        if(listaTramitesOriginal[i][4] == null || listaTramitesOriginal[i][4] == "") {
            if(listaTramitesOriginal[i][8] == "no") {
                i = -1;
                jsp_alerta('A',"<%=descriptor.getDescripcion("msjNoCons")%>");
            }
        }
        if(i != -1) {
            pleaseWait('on');
            activarFormulario();
            document.forms[0].ocurrenciaTramite.value = listaTramitesOriginal[i][0];
            document.forms[0].codTramite.value = listaTramitesOriginal[i][1];
            document.forms[0].codUnidadTramitadoraTram.value = listaTramitesOriginal[i][10];
            document.forms[0].opcion.value="inicio";
            document.forms[0].target="mainFrame";
            document.forms[0].action="<c:url value='/sge/TramitacionRelacionExpedientes.do'/>";
            document.forms[0].submit();
        }
    } else {
        jsp_alerta('A',"<%=descriptor.getDescripcion("msjNoCons")%>");
    }
}

function pulsarTramitacionManual() {

  var datos = new Array();
  datos = listaTramitesOriginal;
  var opcion = "iniciar_tramitacion_manual";
  var source = "<c:url value='/sge/FichaRelacionExpedientes.do?opcion='/>"+opcion
  +"&codMun="  + document.forms[0].codMunicipio.value
  +"&codProc=" + document.forms[0].codProcedimiento.value
  +"&eje=" + document.forms[0].ejercicio.value
  +"&num=" + document.forms[0].numero.value
  +"&numRelacion=" + document.forms[0].numeroRelacion.value;
  abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,datos,
	'width=990,height=550,scrollbars=no,status='+ '<%=statusBar%>',function(datosRespuesta){
                        if(datosRespuesta!=undefined) {
                            actualizarTramites(datosRespuesta);
                        }
                    });
}

function pulsarRetrocederExpediente() { 
  if(preguntarRetroceder()) {
      pleaseWait('on');
    var source = "<c:url value='/sge/FichaRelacionExpedientes.do?'/>"
    +"&codMun="  + document.forms[0].codMunicipio.value
    +"&codProc=" + document.forms[0].codProcedimiento.value
    +"&eje=" + document.forms[0].ejercicio.value
    +"&num=" + document.forms[0].numero.value
    +"&numRelacion=" + document.forms[0].numeroRelacion.value;
    document.forms[0].opcion.value="retroceder_relacion";
    document.forms[0].target="oculto";
    document.forms[0].action=source;
    document.forms[0].submit();

  }
}

function preguntarRetroceder() {
  var indiceTramiteRetroceder = tabTramites.selectedIndex;  
  if(listaTramites.length == 0) {
		jsp_alerta('A',"<%=descriptor.getDescripcion("msjNoHayTram")%>");
		return false;
  } else if(listaTramites.length == 1) {
      //Comprobar primero si el trámite esta bloqueado y luego el usuario que lo bloquea
      if (listaTramitesOriginal[0][14] == "" ||
          ((listaTramitesOriginal[0][14] != "") && (codigoUsuario == listaTramitesOriginal[0][15]))) {
            jsp_alerta('A',"<%=descriptor.getDescripcion("msjTramInic")%>");
            return false;
  } else { 
          jsp_alerta('A',"<%=descriptor.getDescripcion("msjBlqOtroUsu")%>");
      }          
  } else { 
      //Comprobar primero si el trámite esta bloqueado y luego el usuario que lo bloquea
      if (listaTramitesOriginal[0][14] == "" ||
          ((listaTramitesOriginal[0][14] != "") && (codigoUsuario == listaTramitesOriginal[0][15]))) {
		var lT = new Array();
		for(z=0;z<listaTramites.length;z++) {
			if(codigoUsuario == listaTramitesOriginal[z][13] && z==indiceTramiteRetroceder) {//Si el usuario es el que finaliza                
				if(listaTramites[z][2] !="" && listaTramitesOriginal[z][11]!="si") {//Si esta finalizado y no es el de inicio
				  lT[0] = listaTramites[z][0];                
				  document.forms[0].tramiteRetroceder.value = listaTramitesOriginal[z][2];
				  document.forms[0].codTramiteRetroceder.value = listaTramitesOriginal[z][1];
				  document.forms[0].ocurrenciaTramiteRetroceder.value = listaTramitesOriginal[z][0];
				  break;
				} else {
				  break;
				}
			} else if(codigoUsuario == listaTramitesOriginal[z][12] && z==indiceTramiteRetroceder) {//Si el usuario es el que inicia
				//if(listaTramites[z][2] =="") {// Si esta iniciado
                if(listaTramitesOriginal[z][11]!="si") {// Si esta iniciado                
				  var ocurrencia = listaTramitesOriginal[z][0];
				  if(listaTramitesOriginal[z][11] != "si" && z==indiceTramiteRetroceder) {//Si no es tramite de inicio                      
					lT[0] = listaTramites[z][0];
					document.forms[0].codTramiteRetroceder.value = listaTramitesOriginal[z][1];
					document.forms[0].tramiteRetroceder.value = listaTramitesOriginal[z][2];
					document.forms[0].ocurrenciaTramiteRetroceder.value = listaTramitesOriginal[z][0];
					if((z+1)<listaTramites.length && listaTramites[z][2] =="" ) {
					  z++;                      
					  lT = preguntarRetroceder2(lT,z);                      
					}
					break;
				  } else if(listaTramitesOriginal[z][11] == "si" && ocurrencia>1 && z==indiceTramiteRetroceder) {//Si es tramite de inicio pero hay más                      
				    lT[0] = listaTramites[z][0];
					document.forms[0].codTramiteRetroceder.value = listaTramitesOriginal[z][1];
					document.forms[0].tramiteRetroceder.value = listaTramitesOriginal[z][2];
					document.forms[0].ocurrenciaTramiteRetroceder.value = listaTramitesOriginal[z][0];
					if((z+1)<listaTramites.length && listaTramites[z][2] =="" ) {
					  z++;                      
					  lT = preguntarRetroceder2(lT,z);
					}
					break;
				  }
				} else {
				  break;
				}
			}
		}

        
		if(lT.length >0) {
	      var mensaje = "";
		  if(lT.length == 1) {
		    mensaje = "<%=descriptor.getDescripcion("msjRetrocTram")%>" + " ";
		  } else {
		    mensaje = "<%=descriptor.getDescripcion("msjRetrocTrams")%>" + ": ";
		  }
	      mensaje += lT[0];
	      for(c=1;c<lT.length;c++) {
	        mensaje += "," + lT[c] ;
	      }
	      mensaje += " ?";
	      return jsp_alerta('C',mensaje);
	    } else {        
	      jsp_alerta('A',"<%=descriptor.getDescripcion("msjNoUsuario")%>");
		  return false;
	    }
      } else {
             jsp_alerta('A',"<%=descriptor.getDescripcion("msjBlqOtroUsu")%>");
      }
  }
}

function preguntarRetroceder2(lista,indice) {
  for(z=indice;z<listaTramites.length;z++) {
    if(listaTramites[z][2] =="" && codigoUsuario == listaTramitesOriginal[z][12]) {        
	  break;
	} else if(listaTramites[z][2] !="" && codigoUsuario == listaTramitesOriginal[z][13]) {//Si esta finalizado y el usuario es el que finaliza

      if((z-1)>=0 && listaTramites[z-1]!=undefined && listaTramites[z-1]!=null && listaTramites[z-1][11]=="si"){
          lista[1] = listaTramites[z][0];
          document.forms[0].codTramiteRetroceder2.value = listaTramitesOriginal[z][1];
          document.forms[0].tramiteRetroceder2.value = listaTramitesOriginal[z][2];
          document.forms[0].ocurrenciaTramiteRetroceder2.value = listaTramitesOriginal[z][0];
      }
      //lista[1] = listaTramites[z][0];      
      break;
    }
  }  
  return lista;
}

function actualizarTramites(listaTramitesNueva){
    listaTramitesOriginal= new Array();
    listaTramites=new Array();
    listaTramitesOriginal = listaTramitesNueva;
    var i=0;
    for (i=0; i<listaTramitesOriginal.length; i++) {
        listaTramites[i] = [listaTramitesOriginal[i][2], listaTramitesOriginal[i][3], listaTramitesOriginal[i][4], listaTramitesOriginal[i][5], listaTramitesOriginal[i][7], listaTramitesOriginal[i][14]];
        if (!listaTramites[i][5]=="") {
            if ("<%=codUsu%>" == listaTramitesOriginal[i][15]) {
                listaTramites[i][5]='<span class="fa fa-lock" alt="' + listaTramites[i][5]+'"></span>'
            } else {
                listaTramites[i][5]='<span class="fa fa-unlock" alt="' + listaTramites[i][5]+'"></span>'
            }
        }
    }
    tabTramites.lineas = listaTramites;
    tabTramites.displayTabla();
}

/* Fin pestaña tramitacion */


function pulsarVolver() {
	document.forms[0].opcion.value="consultarListado";
    document.forms[0].target="mainFrame";
    document.forms[0].action="<c:url value='/sge/ConsultaExpedientes.do'/>";
    document.forms[0].submit();
}

function mostrarCapas(nombreCapa) {
  document.getElementById(nombreCapa).style.display='';
}

function pulsarCancelar() {
  self.parent.opener.retornoXanelaAuxiliar();
}

function pulsarAceptar() {
  var retorno = new Array();
  retorno[0] = document.forms[0].numeroRelacion.value;
  self.parent.opener.retornoXanelaAuxiliar(retorno);
}

function grabacionRelacionExpedientes(respOpcion,listaOriginal,lista) {
  window.focus();
  if(respOpcion == "grabado") {
    listaDocumentosOriginal = listaOriginal;
    listaDocumentos  = lista;
    tabDocumentos.lineas=listaDocumentos;
    tabDocumentos.displayTabla();
    jsp_alerta('A',"<%=descriptor.getDescripcion("msjDatRelExpGrab")%>");
  } else {
    jsp_alerta('A',"<%=descriptor.getDescripcion("msjDatRelExpNoGrab")%>");
  }
}

function comprobarFecha(inputFecha) {
 if (Trim(inputFecha.value)!='') {
  if (!ValidarFechaConFormato(document.forms[0],inputFecha)){
    jsp_alerta("A","<%=descriptor.getDescripcion("fechaNoVal")%>");
    return false;
  }
 }
 return true;
}

function pulsarGrabarGeneral() {
  if( validarObligatorios("<%=descriptor.getDescripcion("msjObligTodos")%>")) {
    document.forms[0].opcion.value="grabarExpediente";
    document.forms[0].target="oculto";
    document.forms[0].action="<c:url value='/sge/FichaRelacionExpedientes.do'/>";
    document.forms[0].submit();
  }
}

function pulsarVolverTramitacion() {
  document.forms[0].opcion.value="pendientes";
  document.forms[0].target="mainFrame";
  document.forms[0].action="<c:url value='/sge/RelacionExpedientes.do'/>";
  document.forms[0].submit();
}
function modificaVariableCambiosCamposSupl() //Solo tiene sentido en la ficha de expediente, pero es necesario definirla aqui tambien
{}
</script>

</head>
<body class="bandaBody" onload="javascript:{ pleaseWait('off');
         inicializar();}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
    
<html:form action="/sge/FichaRelacionExpedientes.do" method="post">

<html:hidden  property="opcion" value=""/>
<html:hidden  property="codDepartamento" value=""/>
<html:hidden  property="codUnidadRegistro" value=""/>
<html:hidden  property="tipoRegistro" value=""/>
<html:hidden  property="ejerNum" value=""/>
<html:hidden  property="codMunicipio" />
<html:hidden  property="codProcedimiento" />
<html:hidden  property="codTramite" />
<html:hidden  property="ocurrenciaTramite" value=""/>
<html:hidden  property="ejercicio" />
<html:hidden  property="numero" />
<html:hidden  property="codUnidadOrganicaExp" />
<html:hidden  property="numeroRelacion" />
<input type="hidden" name="codUnidadTramitadoraTram" >

<input type="hidden" name="modoConsulta">
<input type="hidden" name="desdeConsulta">
<input type="hidden" name="desdeExpRel">
<input type="hidden" name="deAdjuntar">
<input type="hidden" name="porCampoSup">

<input type="hidden" name="desdeInformesGestion">
<input type="hidden" name="todos">

<html:hidden  property="codMunExpIni"/>
<html:hidden  property="ejercicioExpIni"/>
<html:hidden  property="numeroExpIni"/>

<input type="hidden" name="listaCodTercero" value="">
<input type="hidden" name="listaVersionTercero" value="">
<input type="hidden" name="listaCodDomicilio" value="">
<input type="hidden" name="listaRol" value="">

<input type="hidden" name="codTramiteRetroceder" value="">
<input type="hidden" name="tramiteRetroceder" value="">
<input type="hidden" name="ocurrenciaTramiteRetroceder" value="">
<input type="hidden" name="codTramiteRetroceder2" value="">
<input type="hidden" name="tramiteRetroceder2" value="">
<input type="hidden" name="ocurrenciaTramiteRetroceder2" value="">

<div class="txttitblanco"><%=descriptor.getDescripcion("tit_fichRelExp")%></div>
<div class="encabezadoGris">
    <span class="etiqueta" style="width:10%">&nbsp;&nbsp;<%=descriptor.getDescripcion("etiq_numExp")%>:</span>
    <html:text styleId="numeroRelacionMostrar" property="numeroRelacionMostrar" styleClass="inputTexto" style="width:15%;;margin-left:1%" readonly="true" onmouseover="titulo(this);"/>
    <html:text styleId="procedimiento" property="procedimiento" styleClass="inputTexto" style="width:72%;margin-left:1%" readonly="true" onmouseover="titulo(this);"/>
</div>
<div class="contenidoPantalla">
    <div class="tab-pane" id="tab-pane-1">
        <script type="text/javascript">
            tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
        </script>
        <!-- CAPA 1: DATOS ------------------------------ -->
        <div class="tab-page" id="tabPage1">
            <h2 class="tab"><%=descriptor.getDescripcion("etiqDatos")%></h2>
            <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>
            <TABLE id ="tablaDatosGral" class="contenidoPestanha" cellspacing="0px" cellpadding="0px">
                <tr>
                    <td class="etiqueta"  colspan="4" style="width: 50%; padding-right: 5px"><%=descriptor.getDescripcion("gEtiq_Asunto")%>:</td>
                    <td class="etiqueta" colspan="2"><%=descriptor.getDescripcion("etiqObs")%>:</td>
                </tr>
                <tr>
                    <td class="columnP" colspan="4" style="width: 50%; padding-right: 5px; padding-bottom: 5px">
                        <html:textarea styleClass="textareaTexto" cols="70" rows="4" property="asunto"
                                       onkeypress="javascript:controlarCaracteres(event);" value="" style="text-transform: none;width:99%"></html:textarea>
                    </td>
                    <td class="columnP" colspan="2" style="padding-bottom: 5px">
                        <html:textarea styleClass="textareaTexto" cols="73" rows="4" property="observaciones"
                                       onkeypress="javascript:controlarCaracteres(event);" value="" style="text-transform: none;width:99%"></html:textarea>
                    </td>
                </tr>
                <tr>
                    <td class="etiqueta" style="padding-bottom: 5px; padding-right: 5px"><%=descriptor.getDescripcion("gEtiq_fecIni")%>:</td>
                    <td class="columnP" style="text-align:left;padding-bottom: 5px">
                        <html:text styleClass="inputTxtFecha" size="10" maxlength="9" property="fechaInicio" readonly="true"
                                   onkeypress = "javascript:return soloCaracteresFecha(event);"
                                   onfocus = "this.select();"/>
                    </td>
                    <td class="etiqueta" style="text-align:right;padding-bottom: 5px;padding-right: 5px"><%=descriptor.getDescripcion("gEtiq_fecFin")%>:</td>
                    <td class="columnP" style="text-align:left; padding-right: 10px;padding-bottom: 5px">
                        <html:text styleClass="inputTxtFecha" size="10" maxlength="9" property="fechaFin" readonly="true"
                                   onkeypress = "javascript:return soloCaracteresFecha(event);"
                                   onfocus = "this.select();"/>
                    </td>                                                                                                                     
                </tr>
                <tr style="padding-bottom: 5px; ">
                    <td class="etiqueta" style="padding-bottom: 5px"><%=descriptor.getDescripcion("gEtiq_usuar")%>:</td>
                    <td class="columnP" style="text-align:left; padding-right: 10px;padding-bottom: 5px" colspan="3">
                        <html:text styleClass="inputTexto" property="usuario" style="width: 331px" maxlength="255" readonly="true"
                                   onkeypress="javascript:PasaAMayusculas(event);"/>
                    </td>
                    <td class="etiqueta" style="width:10%;padding-bottom: 5px;"><%=descriptor.getDescripcion("etiq_unidIni")%>:</td>
                    <td class="columnP" style="padding-bottom: 5px;">
                        <html:text styleClass="inputTexto" property="descUnidadOrganicaExp" style="width: 300px" maxlength="255" readonly="true"
                                   onkeypress="javascript:PasaAMayusculas(event);"/>
                    </td>
                </tr>

                <TR>
                    <TD class="sub3titulo" colspan="6">&nbsp;<%=descriptor.getDescripcion("res_pestana2")%></TD>
                </TR>
                <tr style="padding-top: 3px; padding-bottom: 5px">
                    <td align="left" id="tablaTramites" class="tablaP" colspan="6"></td>
                </tr>
                <tr style="padding-left: 5px; padding-right: 5px;">
                    <td align="center" colspan="6" style="padding-top: 3px;"">
                        <input type="button" class="botonMasLargo" accesskey="I" value="<%=descriptor.getDescripcion("bIniciarTramMan")%>" name="cmdTramitacionManual" onclick="pulsarTramitacionManual();return false;">
                        <input type="button" class="botonMasLargo" accesskey="R" value="<%=descriptor.getDescripcion("bRetrocederRel")%>" name="cmdRetrocederExpediente" onclick="pulsarRetrocederExpediente();return false;">
                    </td>
                </tr>
            </table>
        </div>
        <%
Vector estructuraDatosSuplementarios = (Vector) fichaRelExpForm.getEstructuraDatosSuplementarios();
Vector valoresDatosSuplementarios = valoresDatosSuplementarios = (Vector) fichaRelExpForm.getValoresDatosSuplementarios();
if (estructuraDatosSuplementarios != null) {
    int lengthEstructuraDatosSuplementarios = estructuraDatosSuplementarios.size();
    if (lengthEstructuraDatosSuplementarios > 0) {
        for (int i = 0; i < lengthEstructuraDatosSuplementarios; i++) {
            EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);
            if (eC.getCodTipoDato().equals("5")) {
                campos = campos + "," + eC.getCodCampo().toUpperCase();
                ocurrencias = ocurrencias + "," + eC.getOcurrencia();
            }
        }
        if (campos.length() > 0) {
            campos = campos.substring(1);
        }
        if (ocurrencias.length() > 0) {
            ocurrencias = ocurrencias.substring(1);
        }

        boolean dibujarPest = false;
        for (int k = 0; k < lengthEstructuraDatosSuplementarios; k++) {
            es.altia.agora.technical.EstructuraCampo eds = (es.altia.agora.technical.EstructuraCampo) estructuraDatosSuplementarios.get(k);
            es.altia.agora.technical.CamposFormulario vds = (es.altia.agora.technical.CamposFormulario) valoresDatosSuplementarios.get(k);
            if ("true".equals(eds.getSoloLectura())) { // De tramite
                String valor = vds.getString(eds.getCodCampo());
                if (valor != null) {
                    if (!"".equals(valor)) {
                        dibujarPest = true;
                    }
                }
            } else {
                dibujarPest = true;
            }
        }

        if (dibujarPest) {
                                %>
        <!-- CAPA 4: DATOS SUPLEMENTARIOS------------------------------ -->

        <div class="tab-page" id="tabPage4">
            <h2 class="tab"><%=descriptor.getDescripcion("etiqDatosSupl")%></h2>
            <script type="text/javascript">tp1_p4 = tp1.addTabPage( document.getElementById( "tabPage4" ) );</script>
            <TABLE id ="tablaDatosSuplementarios" class="contenidoPestanha">
                <TR>
                    <TD class="sub3titulo"><%=descriptor.getDescripcion("etiqDatosSupl")%></TD>
                </TR>
                <TR style="padding-bottom:5px">
                    <TD style="width: 100%" valign="top">
                        <DIV id="capaDatosSuplementarios" name="capaDatosSuplementarios" style="width: 100%">
                                                    <%
            for (int k = 0; k < lengthEstructuraDatosSuplementarios; k++) {
                request.setAttribute("CAMPO_BEAN", estructuraDatosSuplementarios.get(k));
                request.setAttribute("beanVO", valoresDatosSuplementarios.get(k));
                if ((fechaFinExpediente != null) && (!fechaFinExpediente.equalsIgnoreCase(""))) {
                %>
                <jsp:include page="/jsp/plantillas/CampoVistaExpediente.jsp?desactivaFormulario=true" flush="true" />
                <%    } else {
                %>
                <jsp:include page="/jsp/plantillas/CampoVistaExpediente.jsp?desactivaFormulario=false" flush="true" />
                <%    }
            }
            session.removeAttribute("tramiteCodigo"); // Borramos de la sesion lo utilizado en la JSP anterior
                                                    %>
                        </DIV>
                    </TD>
                </TR>
            </TABLE>
        </div>

                                <% 
        } 
    } 
}
                                %>
<!-- CAPA 3: REGISTRO ------------------------------ -->

<div class="tab-page" id="tabPage3">
<h2 class="tab">Otros datos</h2>
<script type="text/javascript">tp1_p3 = tp1.addTabPage( document.getElementById( "tabPage3" ) );</script>
<table width="100%" border="0">
    <TR>
        <TD class="sub3titulo"><%=descriptor.getDescripcion("etiqDocs")%></TD>
    </TR>
    <tr style="padding-top:5px">
        <td align="left" id="tablaDocumentos"></td>
    </tr>
    <tr align="center">
        <td>
            <DIV id="capaBotonesDocumentos" name="capaBotonesDocumentos" STYLE="position:relative; width:0%; height: 0px; display: none">                                                                
            </div>
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelarActualizarDocumentos" onClick="pulsarCancelarActualizarDocumentos();return false;">                                                            
        </td>                                                        
    </tr>                                                    
    <TR>
        <TD class="sub3titulo"><%=descriptor.getDescripcion("inf_Expedientes")%></TD>
    </TR>
    <tr>
        <td align="left" id="tablaExpedientes"></td>
    </tr>
    <%
Vector lEnlaces = (Vector) fichaRelExpForm.getEnlaces();
if (lEnlaces != null) {
    if (lEnlaces.size() > 0) {
%>
<TR>
    <TD class="sub3titulo"><%=descriptor.getDescripcion("gbEnlaces")%></TD>
</TR>
<TR>
    <TD style="width: 100%">
        <DIV id="capaEnlaces" name="capaEnlaces" style="height:100px;overflow:auto;"></DIV>
    </TD>
</TR>
<%
            } else {
%>
<DIV id="capaEnlaces" name="capaEnlaces" style="height:100px;overflow:auto;display:none"></DIV>
<%
            }
        } else {
%>
<DIV id="capaEnlaces" name="capaEnlaces" style="height:100px;overflow:auto;display:none"></DIV>
<%
}
            %> 
        </table>

    </div>
    <%----- INCORPORACIÓN DE MÓDULOS EXTERNOS -------%>

<%
    ArrayList<ModuloIntegracionExterno> modulos = fichaRelExpForm.getModulosExternos();
    for(int i=0;modulos!=null && i<modulos.size();i++)
    {
        ModuloIntegracionExterno modulo = modulos.get(i);
        String nombreModulo = modulo.getNombreModulo();
        ArrayList<DatosPantallaModuloVO> pantallas = modulo.getListaPantallasExpediente();

        for(int j=0;pantallas!=null && j<pantallas.size();j++)
        {
            String url = pantallas.get(j).getUrl();
            String operacion = pantallas.get(j).getOperacionProceso();
 %>
        <jsp:include page="<%=url%>" flush="true">
            <jsp:param name="nombreModulo" value="<%=nombreModulo%>"/>
            <jsp:param name="codOrganizacionModulo" value="<%=fichaRelExpForm.getCodMunicipio()%>"/>
            <jsp:param name="numero" value="<%=fichaRelExpForm.getNumero()%>"/>
            <jsp:param name="ejercicio" value="<%=fichaRelExpForm.getEjercicio()%>"/>
            <jsp:param name="operacionProceso" value="<%=operacion%>"/>
            <jsp:param name="idioma" value="<%=idioma%>"/>
            <jsp:param name="tipo" value="0"/>
            <jsp:param name="codProcedimiento" value="<%=fichaRelExpForm.getCodProcedimiento()%>"/>
        </jsp:include>
<%
        }// for pantallas
    }// for modulos
%>

<%----- INCORPORACIÓN DE MÓDULOS EXTERNOS -------%>
    </div>
    <DIV id="capaBotones1" name="capaBotones1" STYLE="display:none;height:0px;" class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbVolver")%>" name="cmdVolver" onclick="pulsarVolver();">
    </div>

    <DIV id="capaBotones2" name="capaBotones2" STYLE="display:none;height:0px;" class="botoneraPrincipal" ">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar" onclick="pulsarAceptar();">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelar" onclick="pulsarCancelar();">
    </div>

    <DIV id="capaBotones3" name="capaBotones3" STYLE="display:none;height:0px;" class="botoneraPrincipal" ">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbGrabar")%>" name="cmdGrabarGeneral" onclick="pulsarGrabarGeneral();">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdVolverTramitacion" onclick="pulsarVolverTramitacion();">
    </div>
</div>
</html:form>

<%
Vector estructuraDatosSuplementariosAux = null;
Vector valoresDatosSuplementariosAux = null;
estructuraDatosSuplementariosAux = (Vector) fichaRelExpForm.getEstructuraDatosSuplementarios();
valoresDatosSuplementariosAux = (Vector) fichaRelExpForm.getValoresDatosSuplementarios();

boolean dibujarPest = false;
for(int k=0;k<estructuraDatosSuplementariosAux.size();k++){
    es.altia.agora.technical.EstructuraCampo eds = (es.altia.agora.technical.EstructuraCampo) estructuraDatosSuplementariosAux.get(k);
    es.altia.agora.technical.CamposFormulario vds = (es.altia.agora.technical.CamposFormulario) valoresDatosSuplementariosAux.get(k);
    if("true".equals(eds.getSoloLectura())) { // De tramite
        String valor = vds.getString(eds.getCodCampo());
        if (valor != null)
            if (!"".equals(valor)) dibujarPest = true;
    } else dibujarPest = true;
}
if (dibujarPest) {
    if (estructuraDatosSuplementariosAux != null) {
        int lengthEstructuraDatosSuplementariosAux = estructuraDatosSuplementariosAux.size();
        int lengthValoresDatosSuplementariosAux = valoresDatosSuplementariosAux.size();
        if (lengthEstructuraDatosSuplementariosAux>0 ) {
            int j=0;
            for (int i=0;i<lengthEstructuraDatosSuplementariosAux;i++)
            {
                EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementariosAux.elementAt(i);
                CamposFormulario cF = (CamposFormulario) valoresDatosSuplementariosAux.elementAt(i);
                String nombre = eC.getCodCampo();
                String valor = cF.getString(nombre);

                if (eC.getCodTramite() != null) {
                    if (eC.getOcurrencia() != null)
                        nombre = "T_" + eC.getCodTramite() + "_" + eC.getOcurrencia() + "_" + eC.getCodCampo();
                    else
                        nombre = "T_" + eC.getCodTramite() + "_" + eC.getCodCampo();
                }

                String tipo = eC.getCodTipoDato();
                if (tipo.equals(m_Config.getString("E_PLT.CodigoCampoDesplegable")) || tipo.equals(m_Config.getString("E_PLT.CodigoCampoDesplegableExt")) || tipo.equals(m_Config.getString("E_PLT.CodigoCampoDesplegableExt"))) {%>
                    <script type="text/javascript">
                         eval("var combo<%=nombre%> = new Combo('<%=nombre%>',<%=idioma%>)");
                         eval("combo<%=nombre%>.addItems(<%=eC.getListaCodDesplegable()%>,<%=eC.getListaDescDesplegable()%>)");
                         eval("combo<%=nombre%>.buscaLinea('<%=valor%>');");
                         <%if (eC.getSoloLectura().equals("true")) {%>
                            eval("combo<%=nombre%>.deactivate();");                            
                         <%}%>
                         nombreCombos[<%=j%>]=['<%=nombre%>'];
                         <%j++;%>
                    </script>
                <%}
            }
        }
    }
}
%>

<script>
function mostrarCapasBotones(nombreCapa) {
  document.getElementById('capaBotones1').style.display='none';
  document.getElementById('capaBotones2').style.display='none';
  document.getElementById('capaBotones3').style.display='none';
  document.getElementById(nombreCapa).style.display='';
}

  var tabTramites = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaTramites'));

  tabTramites.addColumna('385','left',"<%=descriptor.getDescripcion("gEtiq_tramite")%>");
  tabTramites.addColumna('75','left',"<%=descriptor.getDescripcion("etiqFechIni")%>");
  tabTramites.addColumna('75','left',"<%=descriptor.getDescripcion("etiqFechFin")%>");
  tabTramites.addColumna('205','left',"<%=descriptor.getDescripcion("res_etiqUnid")%>");
  tabTramites.addColumna('140','left',"<%=descriptor.getDescripcion("etiqClasif")%>");
  tabTramites.addColumna('0','center',"");
  tabTramites.displayCabecera=true;
  
  tabTramites.colorLinea=function(rowID) {
    var fueraDePlazo = listaTramitesOriginal[rowID][9];
    var finalizado = listaTramitesOriginal[rowID][4];
    if(finalizado!="")
      return 'gris';
    else if(fueraDePlazo=="si")
      return 'rojo';
  }

  tabTramites.displayDatos = pintaDatosTramites;
  function pintaDatosTramites() {
    tableObject = tabTramites;
  }

  tableObject = tabTramites;

  // JAVASCRIPT DE LA TABLA EXPEDIENTES
  var tabExp = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaExpedientes'));

  tabExp.addColumna('200','left','<%= descriptor.getDescripcion("etiq_numExp")%>');
  tabExp.addColumna('350','left','<%= descriptor.getDescripcion("etiq_IntPrin")%>');
  tabExp.addColumna('350','center','<%= descriptor.getDescripcion("gEtiq_Asunto")%>');
  tabExp.displayCabecera=true;
  
  tableObject=tabExp;

  function refrescaExpedientes() {
    tabExp.displayTabla();
  }

  tabExp.displayDatos = pintaDatosExpedientes;

  function pintaDatosExpedientes() {
    tableObject = tabExp;
  }

  // FIN DE LOS JAVASCRIPT'S DE LA TABLA EXPEDIENTES

    // JAVASCRIPT DE LA TABLA DE DOCUMENTOS
    var tabDocumentos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaDocumentos'));

    tabDocumentos.addColumna('20','left','');
    tabDocumentos.addColumna('540','left',"<%= descriptor.getDescripcion("gEtiq_nombre")%>");
    tabDocumentos.addColumna('340','left',"<%= descriptor.getDescripcion("gEtiq_cond")%>");
    tabDocumentos.displayCabecera=true;
    tabDocumentos.readOnly = true;    
    // FIN DE LOS JAVASCRIPT'S DE LA TABLA DE DOCUMENTOS

    /* GENERALES */

    function callFromTableTo(rowID,tableName){
      if(tabTramites.id == tableName){
        cargarTramitacion(rowID)
      }
    }


    var coordx=0;
    var coordy=0;


    <%if(userAgent.indexOf("MSIE")==-1) {%> //Que no sea IE
        window.addEventListener('mousemove', function(e) {
            coordx = e.clientX;
            coordy = e.clientY;
        }, true);
    <%}%>

    document.onmouseup = checkKeys;

    function checkKeysLocal(evento,tecla){
        var teclaAuxiiar = "";
        if(window.event){
           evento = window.event;
           teclaAuxiliar = evento.keyCode;
        }else
            teclaAuxiliar = evento.which;
        
      if('Alt+V'==tecla) {
	    if(document.getElementById('capaBotones1').style.display=='')
		  pulsarVolver();
		else if(document.getElementById('capaBotones3').style.display=='')
		  pulsarVolverTramitacion();
	  } else if('Alt+A'==tecla) {
	    if(document.getElementById('capaBotones2').style.display=='')
		  pulsarAceptar();
	  } else if('Alt+C'==tecla) {
	    if(document.getElementById('capaBotones2').style.display=='')
		  pulsarCancelar();
		if(tp1.getSelectedIndex() == 4)
		  pulsarCancelarActualizarDocumentos();
	  } else if('Alt+G'==tecla) {
	    if(document.getElementById('capaBotones3').style.display=='')
		  pulsarGrabarGeneral();
	  }

      //if( (event.keyCode == 40)|| (event.keyCode == 38) ){
      if( (teclaAuxiliar == 40)|| (teclaAuxiliar == 38) ){
        if(tabTramites==tableObject){
          upDownTable(tabTramites,listaTramites,teclaAuxiliar);
        } else if(tabDocumentos==tableObject){
          upDownTable(tabDocumentos,listaDocumentos,teclaAuxiliar);
        }
      }

	  //if(event.keyCode == 13){
      if(teclaAuxiliar == 13){
	    if((tabTramites.selectedIndex>-1)&&(tabTramites.selectedIndex < listaTramites.length)){
	      callFromTableTo(tabTramites.selectedIndex,tabTramites.id);
	    }
	  }
      if (teclaAuxiliar == 1){

            if(IsCalendarVisible) replegarCalendario(coordx,coordy);
            for(i=0;i<nombreCombos.length;i++) {
                var nCombo=eval('combo'+nombreCombos[i]);
                eval('if (nCombo.base.style.display == "") nCombo.ocultar()');

            }

        }
      if (tecla == 9){

            if(IsCalendarVisible) hideCalendar();
            for(i=0;i<nombreCombos.length;i++) {
                var nCombo=eval('combo'+nombreCombos[i]);
                eval('nCombo.ocultar()');

            }
        }
      keyDel(evento);
    }
function inicializarBotonesDatosSuplementarios(){
    var botones=[];
    var j=0;
    if("<%=(String)fichaRelExpVO.getAtributo("modoConsulta")%>" == "si") {
        <%String[] matriz= campos.split(",");
        String[] matrizOcurrencias= ocurrencias.split(",");
        String aux="";
        for (int i=0;i<matriz.length;i++){
            aux=matriz[i];
            if (matrizOcurrencias[i] != null && !"".equals(matrizOcurrencias[i]) && !matrizOcurrencias[i].equals("null")) aux=matriz[i]+"_"+matrizOcurrencias[i];%>
            if ((<%=(aux!=null)%>) && (<%=(!aux.equals(""))%>) && (eval("document.forms[0].<%=aux%>.value")!="")) {
                botones[j]= document.forms[0].cmdVisualizar<%=aux.toUpperCase()%>;
                j=j+1;
            }
          if (document.getElementById("imagenBoton<%=matriz[i].toUpperCase()%>")!=null) 
            document.getElementById("imagenBoton<%=matriz[i].toUpperCase()%>").style.color="#f6f6f6";
        <%}%>
        habilitarGeneral(botones);
    }
}
</script>

</body>
</html:html>
