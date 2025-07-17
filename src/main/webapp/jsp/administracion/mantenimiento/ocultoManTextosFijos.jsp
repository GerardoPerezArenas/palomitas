<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<title>Oculto Mantenimiento Textos Fijos</title>
<script src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script language="javascript">
	<% 
    MantenimientosAdminForm mantForm =(MantenimientosAdminForm)session.getAttribute("MantenimientosAdminForm");
  %>    
  var ventana = '<%=mantForm.getVentana()%>';
	var opcion = '<%=request.getParameter("opcion")%>';
	var listaTextosFijosOriginal = new Array();
  var listaTextosFijos = new Array();
  var frame;
  frame=(ventana=="true")?parent1.mainFrame1:parent.mainFrame;
    

  function cargarListaTextosFijos(){
    <%	
			Vector listaTextosFijos = mantForm.getListaTextosFijos();
    %>
    var j=0;
    <%for(int i=0;i<listaTextosFijos.size();i++)
		  {
        GeneralValueObject textoFijo = (GeneralValueObject)listaTextosFijos.elementAt(i);
    %>
        listaTextosFijosOriginal[j] = ['<%=(String)textoFijo.getAtributo("codApli")%>',//0
          '<%=(String)textoFijo.getAtributo("codText")%>',//1
          '<%=(String)textoFijo.getAtributo("codIdi")%>',//2
          '<%=(String)textoFijo.getAtributo("descIdi")%>',//3
          '<%=StringEscapeUtils.escapeJavaScript((String)textoFijo.getAtributo("texto"))%>',//4
          '<%=(String)textoFijo.getAtributo("estado")%>',//5
          '<%=(String)textoFijo.getAtributo("fechaCreMod")%>'];//6

        listaTextosFijos[j]=[listaTextosFijosOriginal[j][1],listaTextosFijosOriginal[j][3],
						listaTextosFijosOriginal[j][4],listaTextosFijosOriginal[j][5],listaTextosFijosOriginal[j][6]];
					
        j++;
    <%}%>
		//Referenciamos la lista que tendrá todos los datos que vienen del DAO
    frame.listaTextosFijosOriginal = listaTextosFijosOriginal;
//		alert(frame.listaTextosFijosOriginal);
		//Cargamos la lista que se le pasará a la tabla de TextosFijos sólo con los datos que necesitamos
    frame.cargarListaTextosFijos(listaTextosFijos);    
  }
//  cargarListaTextosFijos();
</script>
</head>

<body onLoad="cargarListaTextosFijos();">

</body>
</html>
