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
<title>Oculto Mantenimiento Nucleos</title>
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
	var codESIs = new Array();
	var descESIs = new Array();
	var listaNucleos = new Array();
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
  
  function cargarListaEsis(){
    <%  
      Vector listaEsis = mantForm.getListaEsis();
	  if (listaEsis == null) listaEsis = new Vector();	  
      int lengthEsis = listaEsis.size();
    %>  
      j=0;
    <%for(i=0;i<lengthEsis;i++){%>
			codESIs[j] = "<%=(String)((GeneralValueObject)listaEsis.get(i)).getAtributo("codEntidadSingular")%>";
			descESIs[j] = "<%=(String)((GeneralValueObject)listaEsis.get(i)).getAtributo("nombreOficial")%>";
      j++;
    <%}%>
    frame.codESIs = codESIs;
    frame.descESIs = descESIs;
    frame.cargarComboBox(codESIs,descESIs);
  }

  function cargarListaNucleos(){
    <%	
			Vector listaNucleos = mantForm.getListaNucleos();
			if (listaNucleos == null) listaNucleos=new Vector();
      int lengthNucleos = listaNucleos.size();
    %>
    var j=0;
    <%for(i=0;i<lengthNucleos;i++){
        GeneralValueObject nucleo = (GeneralValueObject)listaNucleos.get(i);
    %>
        listaNucleos[j] = ["<%=(String)nucleo.getAtributo("codPais")%>",// 0
          "<%=(String)nucleo.getAtributo("codProvincia")%>",// 1
          "<%=(String)nucleo.getAtributo("codMunicipio")%>",// 2
          "<%=(String)nucleo.getAtributo("codESI")%>",// 3
          "<%=(String)nucleo.getAtributo("codNUC")%>",// 4
          "<%=(String)nucleo.getAtributo("descNUC")%>",// 5
          "<%=(String)nucleo.getAtributo("nombreLargo")%>",// 6
          "<%=(String)nucleo.getAtributo("codINE")%>",// 7
          "<%=(String)nucleo.getAtributo("idObjetoGrafico")%>",// 8
          "<%=(String)nucleo.getAtributo("situacion")%>"];// 9
        lista[j]=[listaNucleos[j][7],listaNucleos[j][5],
          listaNucleos[j][6],listaNucleos[j][9]];
        j++;
    <%}%>
    frame.listaNucleosOriginal = listaNucleos;
    frame.cargarListaNucleos(lista);    
  }
  
  if(opcion=="cargarMunicipios"){
    cargarListaMunicipios();
  }else if(opcion=="cargarEsis"){
    cargarListaEsis();
  }else if(opcion=="modificarNucTerritorio"){
    if(operacion=="SI"){
      jsp_alerta("A","<%=descriptor.getDescripcion("msjModRealizada")%>");
    }else if(operacion=="NO"){
      jsp_alerta("A","<%=descriptor.getDescripcion("msjModNonRealizada")%>");
    }
    cargarListaNucleos();
  }else{
    cargarListaNucleos();
  }
</script>
</head>

<body>

</body>
</html>
