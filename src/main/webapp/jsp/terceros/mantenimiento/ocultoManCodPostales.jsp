<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head>
<title>Oculto Mantenimiento CodPostales</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<script src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script language="javascript">
	<% 
    MantenimientosTercerosForm mantForm =(MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");
  %>    
  var ventana = "<%=mantForm.getVentana()%>";
	var opcion = "<%=request.getParameter("opcion")%>";
	var codMunicipios = new Array();
	var descMunicipios = new Array();
	var codPostales = new Array();
  var lista = new Array();
  var frame;
  frame=(ventana=="true")?parent1.mainFrame1:parent.mainFrame;

  function cargarListaMunicipios(){
    <%	
		  Vector listaMunicipios = mantForm.getListaMunicipios();
      int lengthMuns = listaMunicipios.size();
    	int i = 0;
    %>
      var j=0;
    <%for(i=0;i<lengthMuns;i++){%>
			codMunicipios[j] = "<%=(String)((GeneralValueObject)listaMunicipios.get(i)).getAtributo("codMunicipio")%>";
			descMunicipios[j] = "<%=(String)((GeneralValueObject)listaMunicipios.get(i)).getAtributo("nombreOficial")%>";
			j++;
    <%}%>
    frame.codMunicipios = codMunicipios;
    frame.descMunicipios = descMunicipios;
    frame.cargarComboBox(codMunicipios,descMunicipios);
  }
  
  function cargarCodPostales(){
    <%	
			Vector listaCodPostales = mantForm.getListaCodPostales();
      int lengthCodPostales = listaCodPostales.size();
    %>
    var j=0;
    var defecto;
    <%for(i=0;i<lengthCodPostales;i++){%>
        codPostales[j] =["<%=(String)((GeneralValueObject)listaCodPostales.get(i)).getAtributo("codPostal")%>",
          "<%=(String)((GeneralValueObject)listaCodPostales.get(i)).getAtributo("defecto")%>"];
        defecto = (codPostales[j][1]=="1") ? "SI":"NO";
        lista[j] = [codPostales[j][0],defecto];
        j++;
    <%}%>
    frame.listaCodPostalesOriginal = codPostales;
    frame.cargarListaCodPostales(lista);
  }
	
  if(opcion=="cargarMunicipios"){
    cargarListaMunicipios();
  }else{
    cargarCodPostales();
  }
</script>
</head>

<body>

</body>
</html>
