<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.escritorio.RegistroUsuarioValueObject" %>
<%@page import="es.altia.agora.business.registro.RegistroValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm" %>

<%@page import="java.util.Vector" %>

<html>
<head>
<title> Oculto cargar pagina </title>
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
%>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<script>

function redirecciona()
{

parent.mainFrame.listaOriginal = new Array();
parent.mainFrame.lista= new Array();
parent.mainFrame.listaSel = new Array();

var asunto;
var estado;


<% /* Recuperar el vector de anotaciones de la sesion. */

   MantAnotacionRegistroForm f= (MantAnotacionRegistroForm)  session.getAttribute("MantAnotacionRegistroForm");
   RegistroValueObject arVO = f.getRegistro();
   int numPagina = Integer.parseInt(arVO.getPaginaListado());
   int numLineasPagina = Integer.parseInt(arVO.getNumLineasPaginaListado());
   int ini= (numPagina -1)*numLineasPagina;
   int fin= ini+numLineasPagina;
%>	
<%
   String nombreCompleto="";
   String apellido1 =null;
   String apellido2 =null;
   Vector relacionAnotaciones = (Vector) session.getAttribute("RelacionAnotaciones");
   int numRelacionAnotaciones = relacionAnotaciones.size();

    if ( relacionAnotaciones != null ) {
     if (numRelacionAnotaciones < fin) fin=numRelacionAnotaciones;
	 int j=0;
     for (int i=ini; i< fin; i++ ) {
      Vector cp_anotacion = (Vector) relacionAnotaciones.elementAt(i);
	  nombreCompleto = (String) cp_anotacion.elementAt(7);
	  apellido1 = (String) cp_anotacion.elementAt(8);
	  apellido2 = (String) cp_anotacion.elementAt(9);
	  if (apellido1 != null) {
		  if (apellido2 == null) apellido2="";
		  if (!"".equals(apellido1.trim())){
			  nombreCompleto = apellido1 + " "+apellido2+", "+nombreCompleto; 
		  }
	  }

%>

   asunto = unescape('<%= (String) cp_anotacion.elementAt(6) %>');	
   parent.mainFrame.listaOriginal[<%= j %>]  = ['<%= (String) cp_anotacion.elementAt(3) %>','<%= (String) cp_anotacion.elementAt(4) %>','<%= (String) cp_anotacion.elementAt(5) %>',asunto,'<%= nombreCompleto%>','<%= (String) cp_anotacion.elementAt(10) %>','<%= (String) cp_anotacion.elementAt(11) %>'];   
   if (asunto.length > 52 ) asunto = asunto.substring(0,27)+'<WBR>'+asunto.substring(27,52)+'...';
   
   if ('<%= (String) cp_anotacion.elementAt(12) %>' == '9') 
   	estado='<FONT style="color:red"> <%=descriptor.getDescripcion("etiq_anulada")%></FONT><BR>';
    
   /* Enlace con SGE. 
    * Fecha: 22/07/2003. */
   else if ('<%= (String) cp_anotacion.elementAt(12) %>' == '1')
   	estado='<FONT style="color:green"> <%=descriptor.getDescripcion("etiq_aceptada")%></FONT><BR>';
   else if ('<%= (String) cp_anotacion.elementAt(12) %>' == '2')
   	estado='<FONT style="color:red"> <%=descriptor.getDescripcion("etiq_rechazada")%></FONT><BR>';
   /* Fin enlace con SGE. */

   else estado='';
    
   parent.mainFrame.lista[<%= j++ %>]  = [estado+'<%= (String) cp_anotacion.elementAt(3) %>'+'/'+'<%= (String) cp_anotacion.elementAt(4) %>','<%= (String) cp_anotacion.elementAt(5) %>','<%= nombreCompleto%>'+' <BR>'+ asunto,'<%= (String) cp_anotacion.elementAt(10) %>','<%= (String) cp_anotacion.elementAt(11) %>'];

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

<p>&nbsp;<p><center>


</body>
</html>
