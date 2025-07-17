<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html" language="java" pageEncoding="UTF-8" import="java.util.Map" %>


<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.agora.business.sge.RelacionExpedientesValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.RelacionExpedientesForm"%>
<%@ page import="java.util.Vector"%>
<%@ page import="es.altia.agora.business.sge.TramitacionValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.TramitacionForm"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="java.util.regex.*, java.lang.*"%>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title> Cargar listas de tramites en el combo </title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />


    <%UsuarioValueObject usuarioVO = new UsuarioValueObject();
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

    <script type="text/javascript">

    function redirecciona() {
        <%// EXPEDIENTES
        String parametroProc = request.getParameter("codProcedimiento");
        /* Recuperar el vector de Expedientes de la sesion. */
        TramitacionForm f= (TramitacionForm)  session.getAttribute("TramitacionForm");
        TramitacionValueObject tVO = f.getTramitacion();
        Vector relacionExpedientes = (Vector) session.getAttribute("RelacionExpedientes");
        int numRelacionExpedientes = relacionExpedientes.size();
        String limiteExpedientes = (String) session.getAttribute("NumeroExpedientesAMostrar");
         if ( relacionExpedientes != null ) {
          int jE=0;
          String unicodeRegex = "\\\\u([0-9a-fA-F]{4})";
          Pattern unicodePattern = Pattern.compile(unicodeRegex);
          for (int iE=0; iE< numRelacionExpedientes; iE++ ) {
            TramitacionValueObject tramE = (TramitacionValueObject) relacionExpedientes.elementAt(iE);
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
            String fP = tramE.getFueraDePlazo();
            String pend = tramE.getPendiente();
            String asuntoExp = tramE.getAsuntoExp();
            String bloqUsuCod = tramE.getBloqUsuCod();
            String bloqUsuNom = tramE.getBloqUsuNom();
            String bloqueo = "";
            String lock = "";
            String img = "";
            if (!bloqUsuCod.equals("")){
                img = "OFF";
                if (String.valueOf(usuarioVO.getIdUsuario()).equals(bloqUsuCod)) img = "ON";
                lock = "<img src=" + request.getContextPath() + "/images/lock" + img + ".jpg alt=" + bloqUsuNom + " />";
                if (!bloqUsuCod.equals(String.valueOf(usuarioVO.getIdUsuario()))) { //Si no está bloqueado por el usuario entonces bloquear expediente
                    bloqueo = "disabled";
                }
            }%>
            asuntoExp = unescape("<%= asuntoExp %>");
            if (asuntoExp.length > 55 ) asuntoExp = asuntoExp.substring(0,55) + "...";
            parent.mainFrame.listaOriginalE[<%= jE %>]  = ["<%= m %>","<%= p %>","<%= dP %>","<%= e %>",
                                                         "<%= n %>","<%= fI %>","<%= t %>","<%= fP %>","<%= pend %>", "<%= img %>"];
            <%
            if ((Boolean) request.getAttribute("SeleccionarTodos")) {
            %>    
                parent.mainFrame.listaE[<%= jE %>]  = ["<%= n %>",
                                                                 "<%= t %>",
                                                                 asuntoExp,
                                                                 "<input type=checkbox id="+<%= jE %>+" class=texto name=exp" + <%= jE++ %> + " value=1 checked " + "<%=bloqueo%>" + " >",
                                                                 "<%=lock%>"];
            <%
            }
            else {
            %>
                parent.mainFrame.listaE[<%= jE %>]  = ["<%= n %>",
                                                                 "<%= t %>",
                                                                 asuntoExp,
                                                                 "<input type=checkbox id="+<%= jE %>+" class=texto name=exp" + <%= jE++ %> + " value=1 " + "<%=bloqueo%>" + " >",
                                                                 "<%=lock%>"];
            
            <%}              
         }
        }%>
                
        parent.mainFrame.tabExp.lineas=parent.mainFrame.listaE;       
        parent.mainFrame.refrescaExpedientes("<%=limiteExpedientes%>");
        pleaseWait('off');
    }
    </script>
</head>
<body onLoad="redirecciona();">

</body>
</html>