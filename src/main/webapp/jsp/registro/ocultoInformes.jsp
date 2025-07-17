<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
<head>
<%
int idioma = 1;
UsuarioValueObject usuario = null;
if (session != null) {
    usuario = (UsuarioValueObject) session.getAttribute("usuario");
    if (usuario != null) idioma = usuario.getIdioma();
}
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
             type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor" property="idi_cod" value="<%=idioma%>"/>
<jsp:setProperty name="descriptor" property="apl_cod" value="1"/>
<title> Informe Registro </title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript">
var tipoVentana='<%=request.getAttribute("tipoVentana")%>';
//alert("Tipo de Ventana en ocultoInformes de Registro"+tipoVentana);

function cargar() {
  pleaseWait('off');
  var fallo = '<%=request.getAttribute("fallo")%>'; 
  if (fallo == '' || fallo == 'null' || fallo == null) { // Ningun problema
  var nombre ='<%=request.getAttribute("nombre")%>';
      var tipoFichero ='<%=request.getAttribute("tipoFichero")%>';
      var opcion ='<%=request.getAttribute("opcion")%>';	
      var directorio='<%=request.getAttribute("dir")%>';	
      if(opcion=="exportarCSV" || opcion=="exportarBuzonEntradaCSV"){	
          parent.mainFrame.abrirInformeCSV(nombre,directorio);
      }else{	
        parent.mainFrame.abrirInforme(nombre,tipoFichero);	
      }  
  } else { // Ha habido algun problema con el numero de asientos (ninguno o demasiados)
      if (fallo == 'noAsientos') {
          jsp_alerta("A", '<%=descriptor.getDescripcion("msjNoAsientos")%>');
      } else if (fallo == 'maxAsientos') {
          var asientos ='<%=request.getAttribute("numAsientos")%>';
          var max ='<%=request.getAttribute("maxAsientos")%>';
          jsp_alerta("A", '<%=descriptor.getDescripcion("msjMaxAsientos")%> ' + asientos + ' / ' + max);
      }
  }
  
}
</script>
</head>
<body onLoad="cargar();">

</body>
</html>
