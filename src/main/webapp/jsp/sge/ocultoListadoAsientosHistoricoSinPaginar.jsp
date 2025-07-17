<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.sge.TramitacionValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionForm" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@page import="java.util.Vector" %>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title> Oculto cargar pagina </title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <%
      int idioma=1;
      int apl=1;
      if ((session.getAttribute("usuario") != null) && (session.getAttribute("registroUsuario") != null)){
            UsuarioValueObject usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        idioma = usuarioVO.getIdioma();
        apl = usuarioVO.getAppCod();
      }

      String numTotalAnotaciones = (String)session.getAttribute("numero_total_anotaciones_historico");
    %>
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

    <script>        
    function redirecciona() { 
    
        
        var numTotal = "<%=numTotalAnotaciones%>";
                
        parent.mainFrame.listaOriginal = new Array();
        parent.mainFrame.lista= new Array();
        parent.mainFrame.listaSel = new Array();
        parent.mainFrame.numRelacionAsientosHistorico = numTotal;
                
       <% /* Recuperar el vector de asientos de la sesion para el buzon. */
        TramitacionForm f= (TramitacionForm)  session.getAttribute("TramitacionForm");
        TramitacionValueObject tVO = f.getTramitacion();
        int numPagina = Integer.parseInt(tVO.getPaginaListado());
        int numLineasPagina = Integer.parseInt(tVO.getNumLineasPaginaListado());
        int ini= (numPagina -1)*numLineasPagina;
        int fin= ini+numLineasPagina;
        %>
                
         <%
       Vector relacionAsientosHistorico = (Vector) request.getAttribute("anotaciones_historico_pagina");
       int numRelacionAsientosHistorico = relacionAsientosHistorico.size();

        if ( relacionAsientosHistorico != null ) {
         if (numRelacionAsientosHistorico < fin) fin=numRelacionAsientosHistorico;
         int j=0;
         for (int i=0; i< numRelacionAsientosHistorico; i++ ) {
          TramitacionValueObject tram = (TramitacionValueObject) relacionAsientosHistorico.elementAt(i);
          String d = tram.getCodDepartamento();
          String uR = tram.getCodUnidadRegistro();
          String tR = tram.getTipoRegistro();
          String eN = tram.getEjerNum();
          String fA = tram.getFechaAnotacion();
          String r = StringEscapeUtils.escapeJavaScript(tram.getRemitente());
          String a = tram.getAsunto();
          String e = tram.getEstado();
          String tecnicoReferencia = tram.getTecnicoReferencia() != null ? tram.getTecnicoReferencia() : "";
          if("0".equals(e)) {
            e = "PENDIENTE";
          } else if("1".equals(e)) {
            e = "ACEPTADO";
          } else if("2".equals(e)) {
            e = "RECHAZADO";
          } else if("9".equals(e)) {
            e = "ANULADO";
          } else if("3".equals(e)) {
            e = "EXPEDIENTE ASOCIADO";
          }
          String origen = tram.getOrigen();
          String numTerceros = tram.getNumTerceros();
          
          if(numTerceros.equals("1")){
              r=r;
          }else{
              r=r+" "+ descriptor.getDescripcion("gEtiq_YOTROS");
          }
        %> 
                
             // Se construye la listaObs para resaltar los campos con observaciones
       var observacion = '<%=StringEscapeUtils.escapeJavaScript(tram.getObservaciones())%>';
       
       if (observacion!='null' && observacion.length>0) {
           parent.mainFrame.listaObs[<%=j%>] = true;
       } else {
           parent.mainFrame.listaObs[<%=j%>] = false; 
       }
    
       asunto = unescape('<%= StringEscapeUtils.escapeJavaScript(a) %>');
       parent.mainFrame.listaOriginal[<%= j %>]  = ['<%= d %>','<%= uR %>','<%= tR %>','<%= eN %>',
               '<%= fA %>','<%= r %>',asunto,'<%= e %>', '<%= origen %>'];
       if (asunto.length > 40 ) asunto = asunto.substring(0,40) + "...";
       parent.mainFrame.lista[<%= j++ %>]  = ['<%= eN %>','<%= fA %>','<%= r %>',asunto,'<%= e %>', '<%= tecnicoReferencia %>'];

    <% 	   }
            }

    %>
      var numPagina = '<%= numPagina%>';
      parent.mainFrame.listaSel =parent.mainFrame.lista;
      parent.mainFrame.inicializaLista(numPagina);
    
    }
    </script>
</head>
    <body onLoad="redirecciona();">
        <p>&nbsp;<p>
    </body>
</html>