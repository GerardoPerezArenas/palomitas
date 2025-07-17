<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.escritorio.RegistroUsuarioValueObject" %>
<%@page import="es.altia.agora.business.sge.TramitacionValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionForm" %>

<%@page import="java.util.Vector" %>
<%@ page import="es.altia.agora.business.sge.RelacionExpedientesValueObject"%>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title> Oculto consulta expediente </title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <%UsuarioValueObject usuarioVO = new UsuarioValueObject();
      RegistroUsuarioValueObject regUsuarioVO = new RegistroUsuarioValueObject();
      int idioma=1;
      int apl=1;
      if ((session.getAttribute("usuario") != null) && (session.getAttribute("registroUsuario") != null))
      {
        usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        regUsuarioVO = (RegistroUsuarioValueObject)session.getAttribute("registroUsuario");
        idioma = usuarioVO.getIdioma();
        apl = usuarioVO.getAppCod();
      }
    %>

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

    <script>
    <%
       /* Recupero el numero de expediente */
       String numRelExp = request.getParameter("numeroRelacionExpediente").toString();
       int i;
       /* Recupera el vector de Expedientes de la sesion. */
       Vector relacionExpedientes = (Vector) session.getAttribute("RelacionExpedientes");

       if (relacionExpedientes != null )
        {
         for (i=0;i<relacionExpedientes.size();i++)
         {
            RelacionExpedientesValueObject relE = (RelacionExpedientesValueObject) relacionExpedientes.elementAt(i);
            String n = relE.getNumeroRelacion().trim();
            if (n.equals(numRelExp))
            {
                i = relacionExpedientes.size();
    %>
                parent.mainFrame.document.forms[0].codMunicipio.value = '<%=relE.getCodMunicipio()%>';
                parent.mainFrame.document.forms[0].codProcedimiento.value = '<%=relE.getCodProcedimiento()%>';
                parent.mainFrame.document.forms[0].ejercicio.value = '<%=relE.getEjercicio()%>';
                parent.mainFrame.document.forms[0].numero.value = '<%=relE.getNumeroRelacion()%>';
                parent.mainFrame.cargarExpedienteDirecto();
    <%
            }
         }
         if (i == relacionExpedientes.size())
         {
    %>
            parent.mainFrame.mostrarAviso();
    <%
         }
        }
    %>
    </script>
</head>
<body>
</body>
</html>