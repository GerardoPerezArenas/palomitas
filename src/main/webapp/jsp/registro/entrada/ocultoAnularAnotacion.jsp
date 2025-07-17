<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<% 
   int idioma=1;
   int apl=1;
   if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
      if (usuario!=null) {
      	idioma = usuario.getIdioma();
      	apl = usuario.getAppCod();
      	}
   }
%>

<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<html>
<head>
<title> Anular Anotacion</title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<script>

function redirecciona()
{
   var respAnularAnot = '<bean:write name="MantAnotacionRegistroForm" property="respOpcion"/>';
   var ejer;
   var numAnot;
   var mnsj="";

   ejer = '<bean:write name="MantAnotacionRegistroForm" property="ejercicioAnotacion"/>';
   numAnot = '<bean:write name="MantAnotacionRegistroForm" property="numeroAnotacion"/>';
   
   if (respAnularAnot== 'anulacion_autorizada' )
   {   
   	parent.mainFrame.iniciarAnular();
   	//window.location = "anulacionRE.jsp";
   } else if (respAnularAnot== 'desAnulacion_realizada') {
		mnsj = 'Anotación '+ejer+'/'+numAnot+' <%=descriptor.getDescripcion("msjAnotRecuperadaP2")%>';
	    parent.mainFrame.anotacionRecuperada(mnsj);   
   } else
   {
   	if (respAnularAnot== 'anulacion_realizada')
   	{
   		mnsj = 'Anotación '+ejer+'/'+numAnot+' <%=descriptor.getDescripcion("msjAnotAnul1")%>';
   	
   	} else if (respAnularAnot== 'anulacion_no_realizada')
   	{
   		mnsj = 'Anotación '+ejer+'/'+numAnot+' <%=descriptor.getDescripcion("msjAnotAnul2")%>';
   		
   	} else if (respAnularAnot== 'anulacion_no_autorizada')
   	{
   	  	mnsj = 'Anotación '+ejer+'/'+numAnot+' <%=descriptor.getDescripcion("msjAnotAnul3")%>';
    } else if (respAnularAnot == 'registro_cerrado') {
			mnsj = 'Anotación '+ejer+'/'+numAnot+ '<%=descriptor.getDescripcion("msjAnotNoRecuperadP2")%>';
	}

   	parent.mainFrame.anotacionAnulada(mnsj);   
   }
}
</script>

</head>
<body onLoad="redirecciona()">

<p>&nbsp;<p><center>
        

</body>
</html>
