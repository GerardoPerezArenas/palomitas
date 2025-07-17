<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head>
<title>Oculto Mantenimiento Trameros</title>
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
	var listaDistritos = new Array();
  var lista = new Array();
  var frame;
  frame=(ventana=="true")?parent1.mainFrame1:parent.mainFrame;

  function cargarListaMunicipios(){
    <%	
		  Vector listaMunicipios = mantForm.getListaMunicipios();
      int lengthMuns = listaMunicipios.size();
    	int i = 0;
      String codMunicipios="";
      String descMunicipios="";
      for(i=0;i<lengthMuns-1;i++){
        GeneralValueObject municipios = (GeneralValueObject)listaMunicipios.get(i);
        codMunicipios+="\""+(String)municipios.getAtributo("codMunicipio")+"\",";
        descMunicipios+="\""+(String)municipios.getAtributo("nombreOficial")+"\",";
      }
      GeneralValueObject municipios = (GeneralValueObject)listaMunicipios.get(i);
      codMunicipios+="\""+(String)municipios.getAtributo("codMunicipio")+"\"";
      descMunicipios+="\""+(String)municipios.getAtributo("nombreOficial")+"\"";
      %>
      codMunicipios = [<%=codMunicipios%>];
      descMunicipios = [<%=descMunicipios%>];
      frame.codMunicipios = codMunicipios;
      frame.descMunicipios = descMunicipios;
      frame.cargarComboBox(codMunicipios,descMunicipios);
  }    

  function cargarListaDistritos(){
    <%	
			Vector listaDistritos = mantForm.getListaDistritos();
      int lengthDistritos = listaDistritos.size();
      String listaDist = "";
      String lista = "";
      for(i=0;i<lengthDistritos-1;i++){
        GeneralValueObject distrito = (GeneralValueObject)listaDistritos.get(i);
        listaDist += "[\""+(String)distrito.getAtributo("codPais")+"\","+// 0
          "\""+(String)distrito.getAtributo("codProvincia")+"\","+// 1
          "\""+(String)distrito.getAtributo("codMunicipio")+"\","+// 2
          "\""+(String)distrito.getAtributo("codDistrito")+"\","+// 3
          "\""+(String)distrito.getAtributo("descDistrito")+"\","+// 4
          "\""+(String)distrito.getAtributo("nombreLargo")+"\","+// 5
          "\""+(String)distrito.getAtributo("idObjetoGrafico")+"\"],";// 6
        lista += "[\""+(String)distrito.getAtributo("codDistrito")+"\","+// 3
          "\""+(String)distrito.getAtributo("descDistrito")+"\","+// 4
          "\""+(String)distrito.getAtributo("nombreLargo")+"\","+// 5
          "\""+(String)distrito.getAtributo("idObjetoGrafico")+"\"],";// 6
      }
      if (lengthDistritos!=0){
	      GeneralValueObject distrito = (GeneralValueObject)listaDistritos.get(i);
	        listaDist += "[\""+(String)distrito.getAtributo("codPais")+"\","+// 0
	          "\""+(String)distrito.getAtributo("codProvincia")+"\","+// 1
	          "\""+(String)distrito.getAtributo("codMunicipio")+"\","+// 2
	          "\""+(String)distrito.getAtributo("codDistrito")+"\","+// 3
	          "\""+(String)distrito.getAtributo("descDistrito")+"\","+// 4
	          "\""+(String)distrito.getAtributo("nombreLargo")+"\","+// 5
	          "\""+(String)distrito.getAtributo("idObjetoGrafico")+"\"]";// 6
	        lista += "[\""+(String)distrito.getAtributo("codDistrito")+"\","+// 3
	          "\""+(String)distrito.getAtributo("descDistrito")+"\","+// 4
	          "\""+(String)distrito.getAtributo("nombreLargo")+"\","+// 5
	          "\""+(String)distrito.getAtributo("idObjetoGrafico")+"\"]";// 6
          }
      %>
    frame.listaDistritosOriginal = [<%=listaDist%>];
    frame.cargarListaDistritos([<%=lista%>]);    
  }
  
  if(opcion=="cargarMunicipios"){
    cargarListaMunicipios();
  }else{
    cargarListaDistritos();
  }
</script>
</head>

<body>

</body>
</html>
