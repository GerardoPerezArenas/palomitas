<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="java.text.MessageFormat"%>

<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
                 
<%
  int idioma=1;
  int apl=1;
  int munic = 0;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();          
        }
  }
%>
    
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<%String mensaje = MessageFormat.format(descriptor.getDescripcion((String) request.getAttribute("msgAlerta")), new Object[] { request.getAttribute("numeroExpediente") });%>
<script>
parent.mainFrame.document.forms[0].numeroExpediente.value='';
parent.mainFrame.jsp_alerta("A",'<%=mensaje%>');
</script>          


