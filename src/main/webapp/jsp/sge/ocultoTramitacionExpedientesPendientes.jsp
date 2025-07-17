<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html" pageEncoding="UTF-8" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.escritorio.RegistroUsuarioValueObject" %>
<%@page import="es.altia.agora.business.sge.TramitacionValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionForm" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="java.util.regex.*, java.lang.*"%>

<%@page import="java.util.Vector" %>
<%@page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.ValoresCriterioBusquedaExpPendientesVO" %>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title> Oculto cargar pagina </title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<%
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  int idioma=1;
  int apl=1;
  if ((session.getAttribute("usuario") != null)){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    idioma = usuarioVO.getIdioma();
    apl = usuarioVO.getAppCod();
  }
  Config m_Config = ConfigServiceHelper.getConfig("common");
  String mostrarLocalizacion = m_Config.getString("ListaExpedientes.localizacion");

  Log log = LogFactory.getLog(this.getClass());
  // Se recupera el código del procedimiento utilizado para realizar el filtro en la bandeja de expedientes pendientes
  String codProcedimientoFiltro = (String)session.getAttribute("codigo_procedimiento_exp_pendientes");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<script>
function redirecciona()
{   
<%
    TramitacionForm f= (TramitacionForm)  session.getAttribute("TramitacionForm");
    ValoresCriterioBusquedaExpPendientesVO criterio = f.getValoresCriterioBusquedaExpPendientes();

%>
parent.mainFrame.listaOriginalE = new Array();
parent.mainFrame.listaE= new Array();
parent.mainFrame.listaPreVis = new Array();


<% /* Recuperar el vector de Expedientes de la sesion. */
   TramitacionValueObject tVO = f.getTramitacion();
   int numPaginaE = Integer.parseInt(tVO.getPaginaListado());
   int numLineasPaginaE = Integer.parseInt(tVO.getNumLineasPaginaListado());
   int iniE = 0;
   int finE = numLineasPaginaE;
   Vector relacionExpedientes = (Vector) session.getAttribute("RelacionExpedientes");
   int numRelacionExpedientes = relacionExpedientes.size();
    if ( relacionExpedientes != null ) {
     if (numRelacionExpedientes < finE) finE=numRelacionExpedientes;
     int jE=0;
     String unicodeRegex = "\\\\u([0-9a-fA-F]{4})";
     Pattern unicodePattern = Pattern.compile(unicodeRegex);
     for (int iE=iniE; iE< finE; iE++ ) {
      TramitacionValueObject tramE = (TramitacionValueObject) relacionExpedientes.elementAt(iE);
      String l = tramE.getLocalizacion();
      if (l==null) l = "";
      String m = tramE.getCodMunicipio();
      String p = tramE.getCodProcedimiento();
      String dP = StringEscapeUtils.escapeJava(tramE.getDescProcedimiento());
      if (dP != null) {
          Matcher matcher = unicodePattern.matcher(dP);
        StringBuffer decodedMessage = new StringBuffer();
        while(matcher.find()) {
            matcher.appendReplacement(decodedMessage, String.valueOf((char) Integer.parseInt(matcher.group(1), 16)));
        }
        
        matcher.appendTail(decodedMessage);
        
        dP = decodedMessage.toString();
      }
      String e = tramE.getEjercicio();
      String n = tramE.getNumero();
      String fI = tramE.getFechaInicioExpediente();
       String fF = tramE.getFechaFinExpediente();
       if (fF==null) {fF = "";}
      String t = StringEscapeUtils.escapeJava(tramE.getTitular());
      if (t == null){
          t="";
      } else {
        Matcher matcher = unicodePattern.matcher(t);
        StringBuffer decodedMessage = new StringBuffer();
        while(matcher.find()) {
            matcher.appendReplacement(decodedMessage, String.valueOf((char) Integer.parseInt(matcher.group(1), 16)));
        }
        
        matcher.appendTail(decodedMessage);
        
        t = decodedMessage.toString();
      }     
      
      if(t!=null && t.length()>40) t= t.substring(0,40) + "...";
      String fP = tramE.getFueraDePlazo();
      String pend = tramE.getPendiente();
      String asuntoExp = tramE.getAsuntoExp();
      String numRelacion = tramE.getNumeroRelacion();
      String plazoCercaFin=tramE.getPlazoCercaFin();
      String estado = tramE.getEstado();
      if("0".equals(estado)){estado="Pendiente";}else{estado="Finalizado";}
       String uor=tramE.getUnidadInicio();
      String usuario=tramE.getUsuarioIni();
      /*String interesados = StringEscapeUtils.escapeJavaScript(tramE.getListaInteresados());
      interesados = interesados.replaceAll(";", "<br/>");
      String tramites = StringEscapeUtils.escapeJavaScript(tramE.getListaTramitesPendientes());
      tramites = tramites.replaceAll(";", "<br/>");*/ 
      String interesados="";
      String tramites ="";
      String TipoInicio = tramE.getTipoInicio();         

      if(TipoInicio!=null && "Instancia".equals(TipoInicio))
          TipoInicio = descriptor.getDescripcion("gbDeInstancia");
      else
          TipoInicio = descriptor.getDescripcion("gbDeOficio");

      // chequear la lista de unidades tramitadoras
      String unidadesTramitadoraspendientes = "";
      String aux = "";
      if (tramE.getListaUnidadesTramitadorasPendientes() != null){
        aux = StringEscapeUtils.escapeJavaScript(tramE.getListaUnidadesTramitadorasPendientes());
        unidadesTramitadoraspendientes = StringEscapeUtils.escapeJavaScript(tramE.getListaUnidadesTramitadorasPendientes());
        unidadesTramitadoraspendientes = unidadesTramitadoraspendientes.replaceAll(";", "<br/>");            
      }

      String usuarioIni = StringEscapeUtils.escapeJavaScript(tramE.getUsuarioIni());
      String unidadIni = StringEscapeUtils.escapeJavaScript(tramE.getUnidadInicio());
      String expedienteConImagen ="";      
       if(tramE.isCercaPlazoExpediente()){
           expedienteConImagen = "<span class='fa fa-clock-o' title='Cerca de fin de plazo'></span>";
      }else
      if(tramE.isFueraPlazoExpediente()){
          expedienteConImagen = "<span class='fa fa-exclamation-triangle' title='Fuera de plazo'></span>";
      }else
        expedienteConImagen = "&nbsp;";
      
      // comprobamos si el expediente esta marcado como importante
      String expedienteImportante ="";
      if (tramE.getExpImportante() != null && tramE.getExpImportante().equals("S")){
         expedienteImportante = "<span class='fa fa-flag' title='" +  descriptor.getDescripcion("etiq_expDestacado") + "'></span>";
      
      }else{         
            expedienteImportante = "&nbsp;";          
      }
      
      // comprobar si el expediente tiene fecha de vencimiento- estado
      String alarmaVencida ="";
       
      if (tramE.getAlarmaVencida().equals("si")){
          alarmaVencida = "<span class='fa fa-bell corAlarmaVencida' alt='" + 
                descriptor.getDescripcion("etiq_expAlarmas") +" '></span>";
      }else if (tramE.getAlarmaVencida().equals("no")){
        alarmaVencida = "<span class='fa fa-bell' title='Alarma no vencida'/>"; 
      }else{
        alarmaVencida = "&nbsp;";          
      }
%>
        
   descProc = unescape("<%= dP %>");
   asuntoExp = unescape("<%= asuntoExp %>");
   if (asuntoExp.length > 55 ) asuntoExp = asuntoExp.substring(0,55) + "...";
   parent.mainFrame.listaOriginalE[<%= jE %>]  = ["<%= m %>","<%= p %>","<%= dP %>","<%= e %>",
                                                            "<%= n %>","<%= fI %>","<%= t %>","<%= fP %>",
                                                            "<%= pend %>","<%= numRelacion %>","<%= plazoCercaFin %>"];
   if (descProc.length > 55 ) descProc = descProc.substring(0,55) + "...";
   parent.mainFrame.listaPreVis[<%= jE %>] = ["<%= n %>","<%= interesados %>","<%= tramites %>",
                                                        asuntoExp, "<%= usuarioIni %>","<%= unidadIni %>","<%= fI %>", "<%= tramE.getExpImportante()%>"];
   
   var claseFila = 'pendienteOtraUnidad'; // Clase por defecto
   var fueraDePlazo = "<%= fP %>";
   var pendiente = "<%= pend %>";
   var plazoCercaFin="<%= plazoCercaFin %>";
   if (fueraDePlazo=="si") 
       claseFila = 'fueraDePlazo';
   else if (plazoCercaFin=="si") 
       claseFila = 'plazoCercaDeFin';
   else if (pendiente=="si") 
       claseFila = 'pendienteEstaUnidad';
   
   
   parent.mainFrame.listaE[<%= jE %>]  = {"0":"<%= expedienteConImagen %>",
               "1":"<%= n %>",
               "2":descProc,
               "3":"<%= t %>",
               "4":asuntoExp,
               "5":"<%= l %>",
               "6":"<%= fI %>",
               "7":"<%= fF %>",
               "8":"<%=estado%>",
               "9":"<%=usuario%>",
               "10":"<%=uor%>",
               "11":"<%= unidadesTramitadoraspendientes %>", 
               "12":"<%=expedienteImportante %>",
               "13":"<%=TipoInicio%>",
               "14":"<%=alarmaVencida %>",
                "DT_RowId":"<%= jE %>",
                "DT_RowClass":claseFila};
     <%  jE++; }
  }
%>
  var numPaginaE = "<%= numPaginaE%>";
  parent.mainFrame.inicializaLista(numPaginaE,<%=tVO.getNumLineasPaginaListado()%>);
  
  <%
    if(codProcedimientoFiltro!=null && !"".equals(codProcedimientoFiltro) && !"null".equals(codProcedimientoFiltro))
    {
    %>

        if(parent.mainFrame.comboFiltroProc!=null){
            //parent.mainFrame.comboFiltroProc.deactivate();
            parent.mainFrame.comboFiltroProc.buscaCodigo("<%=codProcedimientoFiltro%>");
        }
  <%
    } // if
  %>


  <%
    if(criterio!=null && criterio.getCodigoCriterioBusqueda()!=null && criterio.getCodigoCriterioBusqueda().length()>0){
  %>
        // Se marca como seleccionado el criterio adecuado
        if(parent.mainFrame.comboCriterioBusqueda!=null){            
            parent.mainFrame.comboCriterioBusqueda.buscaCodigo("<%=criterio.getCodigoCriterioBusqueda()%>");            
        }//if
  <%
    }//if
  %>


  var tipoCampo = "";
  var codigoCampoCriterio = "";
  var operadorCriterio = "";
  var valoresCriterio = "";
  var campoSuplementario = "";
  var tipoCampoSuplementario = "";
  var codigoDesplegable = "";
  <%
    if(criterio!=null && criterio.getTipoCampoCriterioBusqueda()!=null)
    {
        ArrayList<String> valores = criterio.getValoresCriterioBusqueda();
        StringBuffer sValores = new StringBuffer();
        String separador ="§¥";
        for(int z=0;z<valores.size();z++){
            sValores.append(valores.get(z));
            if(valores.size()-z>1)
                sValores.append(separador);
        }// for

  %>
      codigoCampoCriterio    = "<%=criterio.getCodigoCriterioBusqueda()%>";
      tipoCampo              = "<%=criterio.getTipoCampoCriterioBusqueda()%>";
      operadorCriterio       = "<%=criterio.getOperadorCriterioBusqueda()%>";
      campoSuplementario     = "<%=criterio.isCampoSuplementarioCriterioBusqueda() %>";
      tipoCampoSuplementario = "<%=criterio.getTipoCampoSuplementarioCriterioBusqueda() %>";
      codigoDesplegable      = "<%=criterio.getCodigoDesplegable()%>";
      valoresCriterio        = "<%=sValores.toString()%>";
  <%
    }
  %>

   parent.mainFrame.actualizarVariablesFormularioCriterioBusqueda(codigoCampoCriterio,tipoCampo,operadorCriterio,campoSuplementario,tipoCampoSuplementario,codigoDesplegable,valoresCriterio);

}
</script>

</head>
<body onLoad="redirecciona();">

<p>&nbsp;<p><center>


</body>
</html>