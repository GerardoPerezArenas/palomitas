<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html" language="java" pageEncoding="UTF-8" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.escritorio.RegistroUsuarioValueObject" %>
<%@page import="es.altia.agora.business.sge.TramitacionValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionForm" %>

<%@page import="java.util.Vector" %>
<%@ page import="es.altia.agora.interfaces.user.web.sge.RelacionExpedientesForm"%>
<%@ page import="es.altia.agora.business.sge.RelacionExpedientesValueObject"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="java.util.regex.*, java.lang.*"%>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title> Oculto cargar pagina </title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<%
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  RegistroUsuarioValueObject regUsuarioVO = new RegistroUsuarioValueObject();
  int idioma=1;
  int apl=1;
  if ((session.getAttribute("usuario") != null) && (session.getAttribute("registroUsuario") != null)){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    regUsuarioVO = (RegistroUsuarioValueObject)session.getAttribute("registroUsuario");
    idioma = usuarioVO.getIdioma();
    apl = usuarioVO.getAppCod();
  }
    Config m_Config = ConfigServiceHelper.getConfig("common");
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<script>
function redirecciona()
{
parent.mainFrame.listaOriginalE = new Array();
parent.mainFrame.listaE= new Array();
parent.mainFrame.listaSelE = new Array();

<% /* Recuperar el vector de Expedientes de la sesion. */
   RelacionExpedientesForm f= (RelacionExpedientesForm)  session.getAttribute("RelacionExpedientesForm");
   RelacionExpedientesValueObject reVO = f.getRelacionExpedientes();
   int numPaginaE = Integer.parseInt(reVO.getPaginaListadoE());
   int numLineasPaginaE = Integer.parseInt(reVO.getNumLineasPaginaListadoE());
   int iniE= (numPaginaE -1)*numLineasPaginaE;
   int finE= iniE+numLineasPaginaE;
   Vector relacionExpedientes = (Vector) session.getAttribute("RelacionExpedientes");
   int numRelacionExpedientes = relacionExpedientes.size();
    if ( relacionExpedientes != null ) {
     if (numRelacionExpedientes < finE) finE=numRelacionExpedientes;
     int jE=0;
     String unicodeRegex = "\\\\u([0-9a-fA-F]{4})";
     Pattern unicodePattern = Pattern.compile(unicodeRegex);
     for (int iE=iniE; iE< finE; iE++ ) {
      RelacionExpedientesValueObject RE = (RelacionExpedientesValueObject) relacionExpedientes.elementAt(iE);
      String m = RE.getCodMunicipio();
      String p = RE.getCodProcedimiento();
      String dP = StringEscapeUtils.escapeJava(RE.getDescProcedimiento());
        if (dP != null) {
            Matcher matcher = unicodePattern.matcher(dP);
          StringBuffer decodedMessage = new StringBuffer();
          while(matcher.find()) {
              matcher.appendReplacement(decodedMessage, String.valueOf((char) Integer.parseInt(matcher.group(1), 16)));
          }

          matcher.appendTail(decodedMessage);

          dP = decodedMessage.toString();
        }
      String e = RE.getEjercicio();
      String n = RE.getNumeroRelacion();
      String fI = RE.getFechaInicio();
      String fP = RE.getFueraDePlazo();
	  String pend = RE.getPendiente();
	  String asunto = RE.getAsunto();
%>
   descProc = unescape("<%= dP %>");
   asuntoExp = unescape("<%= asunto %>");
   if (asuntoExp.length > 55 ) asuntoExp = asuntoExp.substring(0,55) + "...";
   parent.mainFrame.listaOriginalE[<%= jE %>]  = ["<%= m %>","<%= p %>","<%= dP %>","<%= e %>",
                                                            "<%= n %>","<%= fI %>","<%= fP %>","<%= pend %>"];
   if (descProc.length > 55 ) descProc = descProc.substring(0,55) + "...";
   parent.mainFrame.listaE[<%= jE++ %>]  = ["<%=m_Config.getString("constante.relacion")%><%= n %>",descProc,asuntoExp];

<% 	   }
        }

%>

  var numPaginaE = "<%= numPaginaE%>";
  parent.mainFrame.listaSelE =parent.mainFrame.listaE;
  parent.mainFrame.inicializaLista(numPaginaE);
}
</script>

</head>
<body onLoad="redirecciona();">

<p>&nbsp;<p><center>


</body>
</html>
