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
        TraductorAplicacionBean descriptor = new TraductorAplicacionBean();
        descriptor.setApl_cod(apl);
        descriptor.setIdi_cod(idioma);
    %>
<script type="text/javascript">
    function redirecciona() { 
        parent.mainFrame.listaOriginal = new Array();
        parent.mainFrame.lista = new Array();
        parent.mainFrame.listaSel = new Array();
    <% /* Recuperar el vector de anotaciones de la sesion. */
       ConsultaExpedientesForm f= (ConsultaExpedientesForm)  session.getAttribute("ConsultaExpedientesForm");
       ConsultaExpedientesValueObject cEVO = f.getConsultaExpedientes();

       int numTotalExpedientes = f.getNumRelacionExpedientes();    

       boolean hayExpedientesHistorico=false;
       int numPagina = Integer.parseInt(cEVO.getPaginaListado());
       String respOpcion = cEVO.getRespOpcion();

       Vector relacionExpedientes = f.getResultadoConsulta();

        if ( relacionExpedientes != null ) {
             // Etiquetas para el estado del expediente
             String etiqPendiente = StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("gbPendiente"));
             String etiqAnulado = StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("gbAnulado"));
             String etiqFinalizado = StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("gbFinalizado"));
            
             // Creación del listado
             for (int i=0; i< relacionExpedientes.size(); i++ ) {
                TramitacionValueObject tram = (TramitacionValueObject) relacionExpedientes.elementAt(i);
                String l = StringEscapeUtils.escapeJavaScript(tram.getLocalizacion());

                if (l==null) 
                    l = "";
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

               if (tram.isExpHistorico()){
                      numConEstado = numConEstado + "<br/><font style=\"font-weight:bold\">" +
                              StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("historico")) + "</font>";

                      hayExpedientesHistorico=true;
                } else
                      numConEstado = numConEstado + "<br/><font style=\"font-weight:bold\">" +
                              StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("etiqActivo")) + "</font>";

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
                String TipoInicio = tram.getTipoInicio();

                if(TipoInicio!=null && "Instancia".equals(TipoInicio))
                    TipoInicio = descriptor.getDescripcion("gbDeInstancia");
                else
                    TipoInicio = descriptor.getDescripcion("gbDeOficio");

                if (perteneceRelacionAbierta==null) 
                    perteneceRelacionAbierta="";

                 String fechaF = "";
                 if (fF!=null) { // Pendiente
                    fechaF =fF;
                }
        %>               
           
            asuntoExp = unescape("<%= asuntoExp %>");
            if (asuntoExp.length > 55) asuntoExp = asuntoExp.substring(0, 55) + "...";

             parent.mainFrame.listaOriginal[<%= i %>] = ["<%= cM %>","<%= cP %>","<%= e %>","<%= nE %>",
                    "<%= p %>","<%= t %>","<%= fI %>","<%=perteneceRelacionAbierta%>","<%=tram.isExpHistorico()%>"];
          parent.mainFrame.lista[<%= i %>] = ['<%=numConEstado%>',"<%=p%>","<%= t %>",asuntoExp,"<%= l %>","<%= fI %>","<%= fechaF %>",'<%= etiqEstado %>',"<%= usuario %>","<%= uor %>","<%= TipoInicio %>"];
            <% 	  // }
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
            parent.mainFrame.ocultaBotonesParaHistorico("<%=hayExpedientesHistorico%>");
        <% } %>
        }
       parent.mainFrame.pleaseWait('off');
    </script>
</head>
<body onLoad="redirecciona();">
</body>
</html>