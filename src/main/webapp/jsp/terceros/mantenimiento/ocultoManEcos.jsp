<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>


<html>
<head>
<title>Oculto Mantenimiento Ecos</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<%
  int idioma=1;
  int apl=1;
  int munic = 0;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
          munic = usuario.getOrgCod();
        }
  }
%>

	<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
	<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
	<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<script src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script language="javascript">
	<% 
    MantenimientosTercerosForm mantForm =(MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");
  %>    
  var ventana = "<%=mantForm.getVentana()%>";
	var opcion = "<%=mantForm.getOpcion()%>";
  var operacion = "<%=mantForm.getOperacion()%>";
	var codMunicipios = new Array();
	var descMunicipios = new Array();
	var listaEcos = new Array();
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
      if (lengthMuns!=0){
	      GeneralValueObject municipios = (GeneralValueObject)listaMunicipios.get(i);
	      codMunicipios+="\""+(String)municipios.getAtributo("codMunicipio")+"\"";
	      descMunicipios+="\""+(String)municipios.getAtributo("nombreOficial")+"\"";
      }
      %>
      codMunicipios = [<%=codMunicipios%>];
      descMunicipios = [<%=descMunicipios%>];
      frame.codMunicipios = codMunicipios;
      frame.descMunicipios = descMunicipios;
      frame.cargarComboBox(codMunicipios,descMunicipios);
  }    

  function cargarListaEcos(){
    <%	
			Vector listaEcos = mantForm.getListaEcos();
      int lengthEcos = listaEcos.size();
      String listaEco = "";
      String lista = "";
      for(i=0;i<lengthEcos-1;i++){
        GeneralValueObject eco = (GeneralValueObject)listaEcos.get(i);
        listaEco += "[\""+(String)eco.getAtributo("codPais")+"\","+// 0
          "\""+(String)eco.getAtributo("codProvincia")+"\","+// 1
          "\""+(String)eco.getAtributo("codMunicipio")+"\","+// 2
          "\""+(String)eco.getAtributo("codECO")+"\","+// 3
          "\""+(String)eco.getAtributo("descECO")+"\","+// 4
          "\""+(String)eco.getAtributo("nombreLargo")+"\","+// 5
          "\""+(String)eco.getAtributo("codINE")+"\","+// 6
          "\""+(String)eco.getAtributo("idObjetoGrafico")+"\","+// 7
          "\""+(String)eco.getAtributo("situacion")+"\"],";// 8
          
        lista += "[\""+(String)eco.getAtributo("codINE")+"\","+// 6
          "\""+(String)eco.getAtributo("descECO")+"\","+// 4
          "\""+(String)eco.getAtributo("nombreLargo")+"\","+// 5
          "\""+(String)eco.getAtributo("situacion")+"\"],";// 8
      } 
      if (lengthEcos!=0){
      
	      GeneralValueObject eco = (GeneralValueObject)listaEcos.get(i);
	        listaEco += "[\""+(String)eco.getAtributo("codPais")+"\","+// 0
	          "\""+(String)eco.getAtributo("codProvincia")+"\","+// 1
	          "\""+(String)eco.getAtributo("codMunicipio")+"\","+// 2
	          "\""+(String)eco.getAtributo("codECO")+"\","+// 3
	          "\""+(String)eco.getAtributo("descECO")+"\","+// 4
	          "\""+(String)eco.getAtributo("nombreLargo")+"\","+// 5
	          "\""+(String)eco.getAtributo("codINE")+"\","+// 6
	          "\""+(String)eco.getAtributo("idObjetoGrafico")+"\","+// 7
	          "\""+(String)eco.getAtributo("situacion")+"\"]";// 8
	          
	        lista += "[\""+(String)eco.getAtributo("codINE")+"\","+// 6  
	          "\""+(String)eco.getAtributo("descECO")+"\","+// 4
	          "\""+(String)eco.getAtributo("nombreLargo")+"\","+// 5
	          "\""+(String)eco.getAtributo("situacion")+"\"]";// 6
	}
      %>
    frame.listaEcosOriginal = [<%=listaEco%>];
    frame.cargarListaEcos([<%=lista%>]);    
  }
  
  if(opcion=="cargarMunicipios"){
    cargarListaMunicipios();
  }else if(opcion=="modificarEcoTerritorio"){
    if(operacion=="SI"){
      jsp_alerta("A","<%=descriptor.getDescripcion("msjModRealizada")%>");
    }else if(operacion=="NO"){
      jsp_alerta("A","<%=descriptor.getDescripcion("msjModNonRealizada")%>");
    }
    cargarListaEcos();
  }else{
    cargarListaEcos();
  }
</script>
</head>

<body>

</body>
</html>
