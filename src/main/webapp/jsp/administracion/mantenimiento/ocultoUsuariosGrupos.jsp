<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.escritorio.RegistroUsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.agora.business.administracion.mantenimiento.UsuariosGruposValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.UsuariosGruposForm" %>

<%@page import="java.util.Vector" %>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<title> Oculto usuario grupos </title>
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
  
    Config m_Conf = ConfigServiceHelper.getConfig("formulariosPdf");
    String usuarioCatalogo=m_Conf.getString("usuarioParaSGE");
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<script>

function redirecciona()
{

parent.mainFrame.listaOriginal = new Array();
parent.mainFrame.lista= new Array();
parent.mainFrame.listaSel = new Array();

parent.mainFrame.listaOriginalG = new Array();
parent.mainFrame.listaG= new Array();
parent.mainFrame.listaSelG = new Array();


<% /* Recuperar el vector de usuarios de la sesion. */
   UsuariosGruposForm f= (UsuariosGruposForm)  session.getAttribute("UsuariosGruposForm");
   UsuariosGruposValueObject ugVO = f.getUsuariosGrupos();
   int numPagina = Integer.parseInt(ugVO.getPaginaListado());
   int numLineasPagina = Integer.parseInt(ugVO.getNumLineasPaginaListado());
   int ini= (numPagina -1)*numLineasPagina;
   int fin= ini+numLineasPagina;
   int numRelacionUsuarios = 0;
%>
<%
   Vector relacionUsuarios = (Vector) session.getAttribute("RelacionUsuarios");
   if(relacionUsuarios != null) {
	   numRelacionUsuarios = relacionUsuarios.size();
	   if ( relacionUsuarios != null ) {
	     if (numRelacionUsuarios < fin) fin=numRelacionUsuarios;
	     int j=0;
	     for (int i=ini; i< fin; i++ ) {
	      GeneralValueObject g = (GeneralValueObject) relacionUsuarios.elementAt(i);
	      if(!usuarioCatalogo.equals((String) g.getAtributo("login"))){
	      	String cU = (String) g.getAtributo("codUsuario");
	      	String l = (String) g.getAtributo("login");
	      	String n = (String) g.getAtributo("nombreUsuario");
            String uE = (String) g.getAtributo("fechaEliminado");
			if (uE != null && !uE.isEmpty())
				uE = descriptor.getDescripcion("gbBaja");
			else
				uE = descriptor.getDescripcion("gbAlta");
	%>
	   	parent.mainFrame.listaOriginal[<%= j %>]  = ['<%= cU %>','<%= l %>','<%= n %>','<%= uE %>'];
	   	parent.mainFrame.lista[<%= j++ %>]  = ['<%= l %>','<%= n %>','<%= uE %>'];
	
	<% 	   	}
		   }	
	     }
	 }
%>

<% /* Recuperar el vector de Grupos de la sesion. */

   int numPaginaG = Integer.parseInt(ugVO.getPaginaListadoG());
   int numLineasPaginaG = Integer.parseInt(ugVO.getNumLineasPaginaListadoG());
   int iniG= (numPaginaG -1)*numLineasPaginaG;
   int finG= iniG+numLineasPaginaG;
   int numRelacionGrupos = 0;
%>
<%
   Vector relacionGrupos = (Vector) session.getAttribute("RelacionGrupos");
   if(relacionGrupos != null) {
	   numRelacionGrupos = relacionGrupos.size();
	
	    if ( relacionGrupos != null ) {
	     if (numRelacionGrupos < finG) finG=numRelacionGrupos;
	     int jG=0;
	     for (int iG=iniG; iG< finG; iG++ ) {
	      GeneralValueObject g = (GeneralValueObject) relacionGrupos.elementAt(iG);
		  String cG = (String) g.getAtributo("codGrupo");
		  String nG = (String) g.getAtributo("nombreGrupo");
	%>
	   parent.mainFrame.listaOriginalG[<%= jG %>]  = ['<%= cG %>','<%= nG %>'];
	   parent.mainFrame.listaG[<%= jG++ %>]  = ['<%= cG %>','<%= nG %>'];
	
	<% 	  }
	    }
	}

%>

  var numPagina = '<%= numPagina%>';
  var numPaginaG = '<%= numPaginaG%>';
  parent.mainFrame.listaSel =parent.mainFrame.lista;
  parent.mainFrame.listaSelG =parent.mainFrame.listaG;
  
  var numeroPaginas=Math.ceil(<%=numRelacionUsuarios%>/<%=numLineasPagina%>);
  
  parent.mainFrame.inicializaLista(numPagina,numPaginaG,<%= numRelacionUsuarios%>,<%= numRelacionGrupos%>);
}
</script>

</head>
<body onLoad="redirecciona();">

<p>&nbsp;<p><center>


</body>
</html>
