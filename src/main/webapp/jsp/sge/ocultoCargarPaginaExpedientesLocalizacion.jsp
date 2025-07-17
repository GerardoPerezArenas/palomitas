<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.agora.business.escritorio.RegistroUsuarioValueObject" %>
<%@ page import="es.altia.agora.business.sge.ConsultaExpedientesValueObject" %>
<%@ page import="es.altia.agora.business.sge.TramitacionValueObject" %>
<%@ page import="es.altia.agora.interfaces.user.web.sge.ConsultaExpedientesForm" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="es.altia.common.service.config.Config" %>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<html>
<head>
    <jsp:include page="/jsp/sge/tpls/app-constants.jsp"/>
    <title> Oculto cargar pagina </title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <%
        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        int idioma = 1;
        int apl = 1;
        if ((session.getAttribute("usuario") != null)) {
            usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
            idioma = usuarioVO.getIdioma();
            apl = usuarioVO.getAppCod();
        }
        Config m_Config = ConfigServiceHelper.getConfig("common");
        String mostrarLocalizacion = m_Config.getString("ListaExpedientes.localizacion");
        TraductorAplicacionBean descriptor = new TraductorAplicacionBean();
        descriptor.setApl_cod(apl);
        descriptor.setIdi_cod(idioma);

        Log log = LogFactory.getLog(this.getClass().getName());
        ArrayList<String> chequeados = (ArrayList<String>)request.getAttribute("expedientesChequeados");
        
    %>
    <script type="text/javascript">

        var expedientesChequeados = new Array(); // Almacena los expedientes de la página que deben estar chequados
        
        function estaExpedienteChequeado(numero){
            var exito = false;
            for(i=0;expedientesChequeados!=null && i<expedientesChequeados.length;i++){
                if(expedientesChequeados[i]==numero){
                    exito = true; break;
                }
            }
            return exito;
        }

        function redirecciona() {            
            parent.mainFrame.listaOriginal = new Array();
            parent.mainFrame.lista = new Array();
            parent.mainFrame.listaSel = new Array();

      <%
            for(int i=0;chequeados!=null && i<chequeados.size();i++){
      %>
                expedientesChequeados[<%=i%>] = "<%=chequeados.get(i)%>";                
      <%
            }// for
        %>

           parent.mainFrame.expedientesSeleccionados = expedientesChequeados;

        <% /* Recuperar el vector de anotaciones de la sesion. */
           ConsultaExpedientesForm f= (ConsultaExpedientesForm)  session.getAttribute("ConsultaExpedientesForm");
           ConsultaExpedientesValueObject cEVO = f.getConsultaExpedientes();

           int numTotalExpedientes = f.getNumRelacionExpedientes();           

           // Muestra sólo los que tienen los trámites abiertos
           boolean mostrarSoloTramitesAbiertos = cEVO.isVerTramitesAbiertos();
           // Se obtiene el tipo de consulta si es sinEstado o expedientesPendientes
           String tipoConsulta = cEVO.getEstado();

           String codPro=cEVO.getCodProcedimiento();
           int numPagina = Integer.parseInt(cEVO.getPaginaListado());
           int numLineasPagina = Integer.parseInt(cEVO.getNumLineasPaginaListado());
           int ini = 0;
           //int fin = 15;
           int fin = 0;
           
           String respOpcion = cEVO.getRespOpcion();

           Vector relacionExpedientes = f.getResultadoConsulta();
           int numRelacionExpedientes = relacionExpedientes.size();
            
            
            if ( relacionExpedientes != null ) {
             if (numRelacionExpedientes < fin) fin=numRelacionExpedientes;
             int j=0;
             fin = numRelacionExpedientes;

             // Etiquetas para el estado del expediente
             String etiqPendiente = StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("gbPendiente"));
             String etiqFinalizado = StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("gbFinalizado"));
             String etiqAnulado = StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("gbAnulado"));

             log.debug("** ini :  " + ini);
             log.debug("** fin :  " + fin);
             log.debug("** numRelacionExpedientes :  " + numRelacionExpedientes);


             // Creación del listado
             for (int i=ini; i< fin; i++ ) {
              TramitacionValueObject tram = (TramitacionValueObject) relacionExpedientes.elementAt(i);
              String l = StringEscapeUtils.escapeJavaScript(tram.getLocalizacion());

              // Se recuperan las colecciones de los trámites abiertos y de los finalizados del expediente
              java.util.Vector listaTramitesAbiertosExp = tram.getListaTramitesAbiertos();
              java.util.Vector listaTramitesFinalizadosExp = tram.getListaTramitesFinalizados();
              // Se comprueba si se debe o no mostrar el expediente
              boolean mostrar = false;
              mostrar = true;

              if(mostrar)
              {
                  if (l==null) l = "";
                  String nE = tram.getNumero();

                  // Añadimos la etiqueta con el estado al numero de expediente
                  String estado = tram.getEstado();
                  String numConEstado = null;
                  
                  if (estado.equals("0")) { // Pendiente
                      numConEstado = nE + "<br/><font class=\"expPendiente\">" + etiqPendiente + "</font>";
                   } else if(estado.equals("1")){ //Anulado
                      numConEstado = nE + "<br/><font class=\"expAnulado\">" + etiqAnulado + "</font>";
                  }else { //Finalizado
                      numConEstado = nE + "<br/><font class=\"expFinalizado\">" + etiqFinalizado + "</font>";
                  }

                   String etiqEstado = "";
                   if (estado.equals("0")) { // Pendiente
                      etiqEstado = "<br/><font class=\"expPendiente\">" + etiqPendiente + "</font>";
                   }else if(estado.equals("1")) { //Anulado
                      etiqEstado = "<br/><font class=\"expAnulado\">" + etiqAnulado + "</font>";
                  } else { //Finalizado
                      etiqEstado ="<br/><font class=\"expFinalizado\">" + etiqFinalizado + "</font>";
                  }
                 
                  
                  String p = StringEscapeUtils.escapeJavaScript(tram.getDescProcedimiento());
                  String t = (StringEscapeUtils.escapeJavaScript(tram.getTitular())).trim();
                  String fI = tram.getFechaInicioExpediente();
                  String fF = tram.getFechaFinExpediente();
                  String cM = tram.getCodMunicipio();
                  String cP = tram.getCodProcedimiento();
                  String e = tram.getEjercicio();
                  String asuntoExp = StringEscapeUtils.escapeJavaScript(tram.getAsuntoExp());
                  String uor=tram.getUnidadInicio();
                  String usuario=tram.getUsuarioIni();
                  String perteneceRelacionAbierta = tram.getNumeroRelacion();
                  if (perteneceRelacionAbierta==null) perteneceRelacionAbierta="";
                   
                   String fechaF = "";
                   if (fF!=null) { // Pendiente
                      fechaF =fF;
                  }

                log.debug("** num expedientes: " + numTotalExpedientes);
        %>

            asuntoExp = unescape("<%= asuntoExp %>");            
            if (asuntoExp.length > 55) asuntoExp = asuntoExp.substring(0, 55) + "...";

            parent.mainFrame.listaOriginal[<%= j %>] = ["<%= cM %>","<%= cP %>","<%= e %>","<%= nE %>",
                    "<%= p %>","<%= t %>","<%= fI %>","<%=perteneceRelacionAbierta%>"];

          var cheq =  "<input type='checkbox' onclick='javascript:marcarExpedientesSeleccionados(this);' id='expedientesSeleccionados' name='expedientesSeleccionados' value='<%=nE%>'/>" ;
          if(estaExpedienteChequeado('<%=nE%>'))
              cheq =  "<input type='checkbox' onclick='javascript:marcarExpedientesSeleccionados(this);' id='expedientesSeleccionados' name='expedientesSeleccionados' checked value='<%=nE%>'/>" ;

          parent.mainFrame.lista[<%= j %>] =     [cheq,'<%=numConEstado%>',"<%=t%>",asuntoExp,"<%= l %>","<%= fI %>","<%= fechaF %>",'<%= etiqEstado %>',"<%= usuario %>","<%= uor %>"];
            <% 	  // }
               j++;
               log.debug("valor de j: " + j);
               }// if listaTramitesAbiertos
              }// for

          }
        %>
        <% if("grabarExpRel".equals(respOpcion) || "NoGrabadoExpRel".equals(respOpcion)) { %>
            parent.mainFrame.grabarExpRel("<%= respOpcion %>");
        <% } else { %>
            var numPagina = "<%= numPagina%>";
            var salida = "";
            for(i=0;i<parent.mainFrame.listaOriginal.length;i++){
                salida += parent.mainFrame.listaOriginal[i] + "<>";
            }
            parent.mainFrame.listaSel = parent.mainFrame.lista;            
            parent.mainFrame.inicializaLista(numPagina,"<%=numTotalExpedientes%>");
        <% } %>
        }
       parent.mainFrame.pleaseWait('off');
    </script>
</head>
<body onLoad="redirecciona();">
</body>
</html>
