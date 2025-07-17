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
<%@page import="java.util.Hashtable" %>
<%@page import="java.util.Enumeration" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.agora.technical.ConstantesDatos"%>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.CamposListadoParametrizablesProcedimientoVO"%>
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

  Vector<CamposListadoParametrizablesProcedimientoVO> campos = (Vector<CamposListadoParametrizablesProcedimientoVO>)request.getAttribute("campos_listado_pendientes_procedimiento");
  
  
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<script>
    
    function esNumero(dato){
        if(/^[0-9]+$/.test(dato)) return true
        return false;
    }

    function buscarValor(valoresCampo,codigoCampo){
        var valor = '';
        
        for(j=0;j<valoresCampo.length;j++){            
            if(valoresCampo[j][0]==codigoCampo){
                valor = valoresCampo[j][1];
                break;
            }
        } 
        return valor;
    }
        
    function cargarValoresExpediente(columnas,listaCamposFijosExpediente,valoresCampo){
        var salida = new Array();
        
       
        
       
        
        var contador = 0;
        
        for(i=0;i<columnas.length;i++){
            var columna = columnas[i];
            
            var posicionColumna = columna[0];            
            var esCampoSuplementario = columna[6];
            
                
            if(esCampoSuplementario!="true" && esNumero(posicionColumna)){   
                salida[contador] = listaCamposFijosExpediente[0][posicionColumna];               
            }else // La columna hace referencia a un campo suplementario
            if(esCampoSuplementario=="true"){
                // Con el código de la columna se extrae el valor del campo del array al que se hace
                //  referencia en el parámetro valoresCampo
                salida[contador] = buscarValor(valoresCampo,posicionColumna);                
            }else
                salida[contador] = '';
            
            contador++;
        }// for
        
        return salida;        
    }
    
function redirecciona()
{ 
<%
    TramitacionForm f= (TramitacionForm)  session.getAttribute("TramitacionForm");
    ValoresCriterioBusquedaExpPendientesVO criterio = f.getValoresCriterioBusquedaExpPendientes();       
    
%>
    var columnas = parent.mainFrame.listaCampos;
    
    // HABRÁ QUE COMPROBAR SI ENTRE LAS COLUMNAS HAY CAMPOS SUPLEMENTARIOS, PORQUE EN ESE CASO, ESO QUIERE DECIR
    // QUE SE ESTÁ FILTRANDO POR PROCEDIMIENTO Y, QUE ESTE, TIENE UNA VISTA PERSONALIZADA
   
parent.mainFrame.listaOriginalE = new Array();
parent.mainFrame.listaE= new Array();
parent.mainFrame.listaSelE = new Array();
parent.mainFrame.listaPreVis = new Array();

var posicionesCamposFijos = new Array();
var contador = 0;
var lineas = new Array();
<%  
    for(int z=0;z<campos.size();z++)
    {
        CamposListadoParametrizablesProcedimientoVO columna = campos.get(z);
        String codigo = columna.getCodCampo();
        String nombreCampo = columna.getNomCampo();        
        if(!columna.isCampoSuplementario()){
%>
            posicionesCamposFijos[contador] = parseInt(<%=codigo%>);
            contador++;
<%
        }// if                  
   }// for
%>    


<% 
   /* Recuperar el vector de Expedientes de la sesion. */
   TramitacionValueObject tVO = f.getTramitacion();
   int numPaginaE = Integer.parseInt(tVO.getPaginaListado());
   int numLineasPaginaE = Integer.parseInt(tVO.getNumLineasPaginaListado());
//   int iniE= (numPaginaE -1)*numLineasPaginaE;
   int iniE = 0;
//   int finE= iniE+numLineasPaginaE;
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
      String tramites="";

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
      if (tramE.getExpImportante().equals("S")){
         expedienteImportante = "<span class='fa fa-flag' title='" +  descriptor.getDescripcion("etiq_expDestacado") + "'>";
      }else{
            expedienteImportante = "&nbsp;";
      }

      String TipoInicio = tramE.getTipoInicio();
      if(TipoInicio!=null && "Instancia".equals(TipoInicio))
          TipoInicio = descriptor.getDescripcion("gbDeInstancia");
      else
          TipoInicio = descriptor.getDescripcion("gbDeOficio");

      
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
       
  
   var DATO_EXPEDIENTE = new Array();
   DATO_EXPEDIENTE[0] = ["<%= expedienteConImagen %>","<%= n %>",descProc,"<%= t %>",asuntoExp,"<%= l %>","<%= fI %>","<%= fF %>","<%=estado%>","<%=usuario%>","<%=uor%>","<%= unidadesTramitadoraspendientes %>", "<%=expedienteImportante %>","<%=TipoInicio%>","<%=alarmaVencida %>"];
   
   parent.mainFrame.listaE[<%= jE %>]  = ["<%= expedienteConImagen %>","<%= n %>",descProc,"<%= t %>",asuntoExp,"<%= l %>","<%= fI %>","<%= fF %>","<%=estado%>","<%=usuario%>","<%=uor%>","<%= unidadesTramitadoraspendientes %>", "<%=expedienteImportante %>","<%=TipoInicio%>","<%=alarmaVencida %>"];
   
   var auxiliar = new Array();
   for(xx=0;xx<posicionesCamposFijos.length;xx++){       
       auxiliar[xx] = parent.mainFrame.listaE[<%= jE %>][posicionesCamposFijos[xx]];       
   }

    var VALORES_CAMPO = new Array();
    var CONTADOR_CAMPOS = 0;
<%
     
    // Valores de los campos suplementarios de cada expediente que forman parte de la vista de expedientes del procedimiento
     Hashtable<String,String> valoresCampos = tramE.getValoresCamposVistaPendientes();     
     for(int g=0;g<campos.size();g++){
        CamposListadoParametrizablesProcedimientoVO columna = campos.get(g);
        
        if(columna.isCampoSuplementario())
        {
            String codigoAux = columna.getCodCampo();
            String valorAux  = valoresCampos.get(codigoAux);
            if(columna.isCampoTexto()){            
%>
               var texto =unescape("<%=valorAux%>");
               if (texto.length > 55 ) texto = texto.substring(0,55) + "...";
               auxiliar.push(texto);
               VALORES_CAMPO[CONTADOR_CAMPOS] = ['<%=codigoAux%>',texto];
               CONTADOR_CAMPOS++;
<%
            }else{
%>
                auxiliar.push("<%=valorAux%>");
                VALORES_CAMPO[CONTADOR_CAMPOS] = ['<%=codigoAux%>','<%=valorAux%>'];
                CONTADOR_CAMPOS++;
<%
            }//else
        }// if
     } //for   
  %>         
       
   lineas[<%=jE%>] = cargarValoresExpediente(columnas,DATO_EXPEDIENTE,VALORES_CAMPO);   
 
<%  jE++; }// for
  }
%>
        
  var numPaginaE = "<%= numPaginaE%>";
  
  parent.mainFrame.listaSelE = lineas;

  
  parent.mainFrame.inicializaLista(numPaginaE,<%=tVO.getNumLineasPaginaListado()%>);
  <%
    if(codProcedimientoFiltro!=null && !"".equals(codProcedimientoFiltro) && !"null".equals(codProcedimientoFiltro))
    {
    %>

        if(parent.mainFrame.comboFiltroProc!=null){
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
  <%
    if(criterio!=null && criterio.getTipoCampoCriterioBusqueda()!=null)
    {
  %>
          tipoCampo = "<%=criterio.getTipoCampoCriterioBusqueda()%>";
  <%
    }
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
      valoresCriterio = "<%=sValores.toString()%>";
  <%
    }
  %>

   parent.mainFrame.actualizarVariablesFormularioCriterioBusqueda(codigoCampoCriterio,tipoCampo,operadorCriterio,campoSuplementario,tipoCampoSuplementario,codigoDesplegable,valoresCriterio);

}
</script>

</head>
<body onLoad="redirecciona();">

<p>&nbsp;<p>

</body>
</html>