<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.escritorio.RegistroUsuarioValueObject" %>
<%@page import="es.altia.agora.business.registro.BuzonWCValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.registro.BuzonWCForm" %>

<%@page import="java.util.Vector" %>

<html>
<head>
<title> Oculto buzón Web ciudadano </title>
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

<% /* Recuperar el vector de registros de la web del ciudadano de la sesion para el buzon. */
   BuzonWCForm f= (BuzonWCForm)  session.getAttribute("BuzonWCForm");
   BuzonWCValueObject buzonVO = f.getBuzonWC();
   int numPagina = Integer.parseInt(buzonVO.getPaginaListado());
   int numLineasPagina = Integer.parseInt(buzonVO.getNumLineasPaginaListado());
   int ini= (numPagina -1)*numLineasPagina;
   int fin= ini+numLineasPagina;
%>
<%
   Vector relacionRegistros = (Vector) session.getAttribute("RelacionRegistros");
   int numRelacionRegistros = relacionRegistros.size();

    if ( relacionRegistros != null ) {
     if (numRelacionRegistros < fin) fin=numRelacionRegistros;
     int j=0;
     for (int i=ini; i< fin; i++ ) {
      BuzonWCValueObject tram = (BuzonWCValueObject) relacionRegistros.elementAt(i);
      String fec = tram.getFecha();
	  String n = tram.getNombre();
	  String e = tram.getEstado();
	  String eje = tram.getEjercicio();
	  String num = tram.getNumero();

%>
   parent.mainFrame.listaOriginal[<%= j %>]  = ['<%= fec %>','<%= n %>','<%= e %>','<%= eje %>','<%= num %>'];
   parent.mainFrame.lista[<%= j++ %>]  = ['<%= fec %>','<%= n %>'];

<% 	   }
      }

%>


  var numPagina = '<%= numPagina%>';
  var numeroRelacionRegistros = <%=numRelacionRegistros%>;
  parent.mainFrame.listaSel =parent.mainFrame.lista;
  parent.mainFrame.inicializaLista(numPagina,numeroRelacionRegistros);
}
</script>

</head>
<body onLoad="redirecciona();">

<p>&nbsp;<p><center>


</body>
</html>
