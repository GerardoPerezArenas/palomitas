<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.informes.GeneradorInformesForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head>
    <title>Oculto Generador Informes</title>

    <jsp:include page="/jsp/informes/tpls/app-constants.jsp" />
    <script type="text/javascript">
	var listaCampos = new Array();
	var listaCamposOriginal = new Array();
	<%
      GeneradorInformesForm bForm =(GeneradorInformesForm)session.getAttribute("GeneradorInformesForm");
	  String operacion = bForm.getOperacion();
	%>
		// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
		function inicializar(){
		  <% if("altaCorrecta".equals(operacion)) { %>
		    altaRealizada();
		  <% } else if("altaIncorrecta".equals(operacion)) { %>
		    altaNoRealizada();
		  <% } else if("modificacionCorrecta".equals(operacion)) { %> 
		    modificacionRealizada();
		  <% } else if("modificacionIncorrecta".equals(operacion)) { %> 
		  	modificacionNoRealizada();
		  <% } else { %>
		    cargaCamposDisponibles();
		  <% } %>
		}
		
		function altaRealizada() {
		  parent.mainFrame.altaRealizada();
		}
		
		function altaNoRealizada() {
		  parent.mainFrame.altaNoRealizada();
		}
		
		function modificacionRealizada() {
		  parent.mainFrame.modificacionRealizada();
		}
		
		function modificacionNoRealizada() {
		  parent.mainFrame.modificacionNoRealizada();
		}
		
		function cargaCamposDisponibles(){
		<% Vector listaCamposDisponibles = bForm.getListaCamposDisponibles();
      	   int lengthCampos = listaCamposDisponibles.size();
           int i = 0;
    	%>
    	var j=0;
    	<%for(i=0;i<lengthCampos;i++){
        	GeneralValueObject campos = (GeneralValueObject)listaCamposDisponibles.get(i);%>
      		listaCampos[j] = ['<%=(String)campos.getAtributo("nombreCampo")%>'];
			listaCamposOriginal[j] = ['<%=(String)campos.getAtributo("codCampo")%>',
        						 '<%=(String)campos.getAtributo("nombreCampo")%>',
						         '<%=(String)campos.getAtributo("tipo")%>',
						         '<%=(String)campos.getAtributo("longitud")%>',
								 '<%=(String)campos.getAtributo("campo")%>',
								 '<%=(String)campos.getAtributo("nombreAs")%>'];
      		j++;
    	<%}%>
			parent.mainFrame.cargarTablaDisponibles(listaCampos,listaCamposOriginal);
		}
  </script>
</head>
<body onload="inicializar();">
</body>
</html>
