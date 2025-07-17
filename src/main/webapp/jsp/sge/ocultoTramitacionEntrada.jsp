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
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@page import="java.util.Vector" %>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title> Oculto cargar pagina </title>

<jsp:include page="/jsp/plantillas/Metas.jsp" />
<%
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  
  int idioma=1;
  int apl=1;
  if (session.getAttribute("usuario") != null) {
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    idioma = usuarioVO.getIdioma();
    apl = usuarioVO.getAppCod();
  }
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<script>
    function redirecciona() { 
        
        


parent.mainFrame.listaOriginal = new Array();
parent.mainFrame.lista= new Array();
parent.mainFrame.listaSel = new Array();
var DOT_COMMA = ";";

<% /* Recuperar el vector de asientos de la sesion para el buzon. */
   TramitacionForm f= (TramitacionForm)  session.getAttribute("TramitacionForm");
   TramitacionValueObject tVO = f.getTramitacion();
   int numPagina = Integer.parseInt(tVO.getPaginaListado());
   int numLineasPagina = Integer.parseInt(tVO.getNumLineasPaginaListado());
   int ini= (numPagina -1)*numLineasPagina;
   int fin= ini+numLineasPagina;
   Vector relacionAsientos = (Vector) session.getAttribute("RelacionAsientos");
   int numRelacionAsientos = 0;

    if ( relacionAsientos != null ) {
     numRelacionAsientos = relacionAsientos.size();
     if (numRelacionAsientos < fin) fin=numRelacionAsientos;
     int j=0;
     for (int i=ini; i< fin; i++ ) {
      TramitacionValueObject tram = (TramitacionValueObject) relacionAsientos.elementAt(i);
      String d = tram.getCodDepartamento();
      String uR = tram.getCodUnidadRegistro();
      String tR = tram.getTipoRegistro();
      String eN = tram.getEjerNum();
      String fA = tram.getFechaAnotacion();
      String r = tram.getRemitente();//StringEscapeUtils.escapeJavaScript(tram.getRemitente());
      String numTerceros = tram.getNumTerceros();
      String uor=tram.getUnidadTramitadora();
      String procedimiento=tram.getProcedimiento();
      if(procedimiento==null){procedimiento="";}
      String tipo= tram.getTipoRegistro();
      String fechaDoc=tram.getFechaDocumento();
      String numExp=tram.getNumExpediente();
       if(numExp==null){numExp="";}
      String usu=tram.getUsuarioAlta();
      if(numTerceros.equals("1")){
          r=r;
      }else{
          r=r+" "+ descriptor.getDescripcion("gEtiq_YOTROS");
      }
      
      String a = tram.getAsunto();
      a = a.replaceAll("\"", "");
      String origenServicio = tram.getOrigen();

      String tipoTransporte = tram.getTipoTransporte();
      String uorInicio = tram.getUnidadInicio();
      
      String codAsuntoCodif = tram.getCodigoAsuntoCodificado();	
      String descAsuntoCodif = tram.getDescripcionAsuntoCodificado();
      
	  String[] numAnotacion = eN.split("/");
%>
    var asunto = unescape("<%= a %>");
    parent.mainFrame.listaOriginal[<%= j %>] = ["<%= d %>","<%= uR %>","<%= tR %>","<%= eN %>","<%= fA %>",
           "<%= r %>",asunto, "<%=origenServicio%>", "<%=codAsuntoCodif%>", "<%=descAsuntoCodif%>"];
   if (asunto.length > 55 ) asunto = asunto.substring(0,55) + "...";
   
   var valorCheck =<%=numAnotacion[0]%>+DOT_COMMA+<%=numAnotacion[1]%>+DOT_COMMA+"E"+DOT_COMMA+<%=uR%>;
   var check = '<input type="checkbox" class="checkLinea" onclick="pulsarCheck()" value="'+valorCheck+'" />';
   parent.mainFrame.lista[<%= j %>]  = [check,"Pendiente","<%=procedimiento%>","<%=numExp%>","<%=usu%>","<%=eN%>","<%=tipo%>","<%= fA %>",
               "<%= fechaDoc %>","<%= r %>",asunto,"<%= uor %>","Ordinaria","<%=tipoTransporte%>","<%=uorInicio%>", "<%=codAsuntoCodif%>", "<%=descAsuntoCodif%>"];

<%j++;
 	 }
   }
%>
  
  var numPagina = '<%= numPagina%>';
  parent.mainFrame.listaSel =parent.mainFrame.lista; 
  //alert("llama a inicializaLista desde redirecciona");
  parent.mainFrame.inicializaLista(numPagina);
}
</script>

</head>
<body onLoad="redirecciona();">

<p>&nbsp;<p><center>


</body>
</html>
