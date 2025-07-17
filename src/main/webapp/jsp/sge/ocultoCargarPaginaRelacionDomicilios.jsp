<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.escritorio.RegistroUsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionExpedientesForm" %>
<%@page import="es.altia.agora.business.sge.TramitacionExpedientesValueObject" %>

<%@page import="java.util.Vector" %>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title> Oculto cargar pagina relación domicilios</title>
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

<% /* Recuperar el vector de anotaciones de la sesion. */

   BusquedaTercerosForm f= (BusquedaTercerosForm)  session.getAttribute("BusquedaTercerosForm");
   TramitacionExpedientesForm tEF = (TramitacionExpedientesForm)  session.getAttribute("TramitacionExpedientesForm");
   TramitacionExpedientesValueObject tEVO = tEF.getTramitacionExpedientes();
   int numPagina = Integer.parseInt(tEVO.getPaginaListado());
   int numLineasPagina = Integer.parseInt(tEVO.getNumLineasPaginaListado());
   int ini= (numPagina -1)*numLineasPagina;
   int fin= ini+numLineasPagina;
%>
<%
   Vector relacionDomicilios = (Vector) session.getAttribute("RelacionDomicilios");
   int numRelacionDomicilios = relacionDomicilios.size();

    if ( relacionDomicilios != null ) {
     if (numRelacionDomicilios < fin) fin=numRelacionDomicilios;
     int j=0;
     for (int i=ini; i< fin; i++ ) {
      GeneralValueObject gVO = (GeneralValueObject) relacionDomicilios.elementAt(i);
      String normalizado=(String)gVO.getAtributo("normalizado");
      String descMunicipio = "";
      String domicilio = "";
      String lugar = "";
      String codPostal = "";
      String idDomicilio = "";
      String descTVia = (String)gVO.getAtributo("descTipoVia");
      String numDesde = "";
      String letraDesde = "";
      String numHasta = "";
      String letraHasta = "";
      String bloque = "";
      String portal = "";
      String escal = "";
      String planta = "";
      String puerta = "";
		descMunicipio = (String)gVO.getAtributo("descMunicipio");
		descTVia = (String)gVO.getAtributo("descTipoVia");
		numDesde = (String)gVO.getAtributo("numDesde");
		letraDesde = (String)gVO.getAtributo("letraDesde");
		numHasta = (String)gVO.getAtributo("numHasta");
		letraHasta = (String)gVO.getAtributo("letraHasta");
		bloque = (String)gVO.getAtributo("bloque");
		portal = (String)gVO.getAtributo("portal");
		escal = (String)gVO.getAtributo("escalera");
		planta = (String)gVO.getAtributo("planta");
		puerta = (String)gVO.getAtributo("puerta");

      if(normalizado.equals("si")) {
		if ("SIN TIPO DE VIA".equals(descTVia)){
	        domicilio = (String)gVO.getAtributo("descVia");
		} else domicilio = (String)gVO.getAtributo("descVia") +" ("+ descTVia + ")";
		domicilio = (!numDesde.equals("")) ? domicilio+" "+numDesde:domicilio;
        domicilio = (!letraDesde.equals("")) ? domicilio+" "+letraDesde+" ":domicilio;
        domicilio = (!numHasta.equals("")) ? domicilio+" - "+numHasta:domicilio;
        domicilio = (!letraHasta.equals("")) ? domicilio+" "+letraHasta:domicilio;
        domicilio = (!bloque.equals("")) ? domicilio+" Blq. "+bloque:domicilio;
        domicilio = (!portal.equals("")) ? domicilio+" Port. "+portal:domicilio;
        domicilio = (!escal.equals("")) ? domicilio+" Esc. "+escal:domicilio;
        domicilio = (!planta.equals("")) ? domicilio+" Plta. "+planta:domicilio;
        domicilio = (!puerta.equals("")) ? domicilio+" Pta. "+puerta:domicilio;

		/*
		domicilio = (!descTVia.equals("")) ? domicilio+descTVia+" ":domicilio;
        domicilio += (String)gVO.getAtributo("descVia");
        domicilio = (!numDesde.equals("")) ? domicilio+" "+numDesde:domicilio;
        domicilio = (!letraDesde.equals("")) ? domicilio+" "+letraDesde+" ":domicilio;
        domicilio = (!numHasta.equals("")) ? domicilio+" "+numHasta:domicilio;
        domicilio = (!letraHasta.equals("")) ? domicilio+" "+letraHasta:domicilio;
        domicilio = (!bloque.equals("")) ? domicilio+" Bl. "+bloque:domicilio;
        domicilio = (!portal.equals("")) ? domicilio+" Portal "+portal:domicilio;
        domicilio = (!escal.equals("")) ? domicilio+" Esc. "+escal:domicilio;
        domicilio = (!planta.equals("")) ? domicilio+" "+planta+"º ":domicilio;
        domicilio = (!puerta.equals("")) ? domicilio+puerta:domicilio;
		*/
        lugar = (String)gVO.getAtributo("lugar");
        codPostal = (String)gVO.getAtributo("codPostal");
        idDomicilio = (String)gVO.getAtributo("idDomicilio");
      } else {
        descMunicipio = (String)gVO.getAtributo("descMunicipio");
        domicilio = (String)gVO.getAtributo("domicilio");
		domicilio = (!numDesde.equals("")) ? domicilio+" "+numDesde:domicilio;
        domicilio = (!letraDesde.equals("")) ? domicilio+" "+letraDesde+" ":domicilio;
        domicilio = (!numHasta.equals("")) ? domicilio+" - "+numHasta:domicilio;
        domicilio = (!letraHasta.equals("")) ? domicilio+" "+letraHasta:domicilio;
        domicilio = (!bloque.equals("")) ? domicilio+" Blq. "+bloque:domicilio;
        domicilio = (!portal.equals("")) ? domicilio+" Port. "+portal:domicilio;
        domicilio = (!escal.equals("")) ? domicilio+" Esc. "+escal:domicilio;
        domicilio = (!planta.equals("")) ? domicilio+" Plta. "+planta:domicilio;
        domicilio = (!puerta.equals("")) ? domicilio+" Pta. "+puerta:domicilio;		
        lugar = (String)gVO.getAtributo("poblacion");
        codPostal = (String)gVO.getAtributo("codPostal");
        idDomicilio = (String)gVO.getAtributo("idDomicilio");
      }
%>
   parent.mainFrame.listaOriginal[<%= j %>]  = ['<%= idDomicilio %>','<%= descMunicipio %>','<%= domicilio %>',
                                             '<%= lugar %>','<%= codPostal %>'];
   parent.mainFrame.lista[<%= j++ %>]  = ['<%= descMunicipio %>','<%= domicilio %>',
                                             '<%= lugar %>','<%= codPostal %>'];

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
