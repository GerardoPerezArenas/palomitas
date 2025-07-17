<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm"%>	
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.Vector"%>	

<html>
<head>
<title>SELECCION DE DOMICILIOS</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<meta http-equiv="" content="text/html;	charset=iso-8859-1">

<!-- Estilos -->

<%
	UsuarioValueObject usuarioVO = new UsuarioValueObject();
	ParametrosTerceroValueObject ptVO = null;
	int idioma=1;	
	int apl=3;
	if (session.getAttribute("usuario") != null){
		usuarioVO =	(UsuarioValueObject)session.getAttribute("usuario");
		idioma = usuarioVO.getIdioma();
		apl =	usuarioVO.getAppCod();
		ptVO = (ParametrosTerceroValueObject)session.getAttribute("parametrosTercero");
	}
     Config m_Conf = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Conf.getString("JSP.StatusBar");
    String JSP_entidadColectiva = m_Conf.getString("JSP.EntidadColectiva");
    String JSP_entidadSingular = m_Conf.getString("JSP.EntidadSingular");
    String JSP_emplazamiento = m_Conf.getString("JSP.Emplazamiento");
    String ecv = "visible";
    String esv = "visible";
    String emplv = "visible";
    if("no".equals(JSP_entidadColectiva)) ecv = "hidden";
    if("no".equals(JSP_entidadSingular)) esv = "hidden";
    if("no".equals(JSP_emplazamiento)){ emplv = "hidden";}
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"	type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>"	/>
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<!-- Ficheros JavaScript -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script language="JavaScript1.2">

    <%
	BusquedaTercerosForm bForm =(BusquedaTercerosForm)session.getAttribute("BusquedaTercerosForm");	
    %>
    var Domicilios = new Array();
    var DatosBusqueda =	new Array();
    var longitud = 0;
    var lista = new Array();
    var listaVacia = new Array();
    var ventana;
    var frame;

    var codPaises	= new	Array();
    var descPaises = new Array();
    var codProvincias =	new Array();
    var descProvincias = new Array();
    var codMunicipios =	new Array();
    var descMunicipios = new Array();
    var codMunicipiosDefecto = new Array();
    var descMunicipiosDefecto	= new	Array();
    
    function recuperaDatosIniciales(){
		<%

		Vector listaProvincias = bForm.getListaProvincias();
		Vector listaMunicipios = bForm.getListaMunicipios();
		int	lengthProvs	= listaProvincias.size();
		int	lengthMuns = listaMunicipios.size();
		int	i = 0;
		String codProvincias="";
		String descProvincias="";
		for(i=0;i<lengthProvs-1;i++){
			GeneralValueObject provincias =	(GeneralValueObject)listaProvincias.get(i);
			codProvincias+="\""+(String)provincias.getAtributo("codigo")+"\",";	
			descProvincias+="\""+(String)provincias.getAtributo("descripcion")+"\",";	
		}
		GeneralValueObject provincias = (GeneralValueObject)listaProvincias.get(i);	
		codProvincias+="\""+(String)provincias.getAtributo("codigo")+"\"";
		descProvincias+="\""+(String)provincias.getAtributo("descripcion")+"\"";
		String codMunicipios="";
		String descMunicipios="";
		for(i=0;i<lengthMuns-1;i++){
			GeneralValueObject municipios =	(GeneralValueObject)listaMunicipios.get(i);
			codMunicipios+="\""+(String)municipios.getAtributo("codMunicipio")+"\",";	
			descMunicipios+="\""+(String)municipios.getAtributo("nombreOficial")+"\",";
		}
		GeneralValueObject municipios = (GeneralValueObject)listaMunicipios.get(i);	
		codMunicipios+="\""+(String)municipios.getAtributo("codMunicipio")+"\"";
		descMunicipios+="\""+(String)municipios.getAtributo("nombreOficial")+"\"";
		%>
		codProvincias = [<%=codProvincias%>];
		descProvincias = [<%=descProvincias%>];
		codMunicipios = [<%=codMunicipios%>];
		descMunicipios = [<%=descMunicipios%>];
		// CARGAR COMBOS
		comboProvincia.addItems(codProvincias,descProvincias);
		comboMunicipio.addItems(codMunicipios,descMunicipios);
		/* anadir ECO/ESI */
		comboECO.addItems(codECOs,descECOs);
		comboESI.addItems(codESIs,descESIs);
		/* Fin anadir ECO/ESI */	

    }	
    
    function cargarListaMunicipios(){	
		document.forms[0].opcion.value="cargarMunicipios";
		document.forms[0].target="oculto";
		document.forms[0].ventana.value="true";
		document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
		document.forms[0].submit();
    }	
    

    function inicializar() {
		ventana = "<%=bForm.getVentana()%>";
		frame="oculto";
		if(ventana=="mainFrame")
			frame="oculto";	
		else if (ventana=="mainFrame1")
			frame="oculto";	
		recuperaDatosIniciales();
		var attr = self.parent.opener.xanelaAuxiliarArgs;
		Domicilios=	attr[0];
		lista=attr[1];
		DatosBusqueda=attr[2];
        document.forms[0].codProvincia.value = DatosBusqueda[4];
        document.forms[0].descProvincia.value = DatosBusqueda[5];
        document.forms[0].codMunicipio.value = DatosBusqueda[6];
        document.forms[0].descMunicipio.value = DatosBusqueda[7];
		//cargaDatosBusqueda();
        tab.lineas=lista;
        refresco();
        //frame="oculto2";
		/*var	tipo = "<%=request.getParameter("tipo")%>";
		var descDoc	= "<%=request.getParameter("descDoc")%>";	
		var docu = "<%=request.getParameter("docu")%>";	
		*/
		desHabilitarProvinciaMunicipio(true);
    }	
    
    function cargaDatosBusqueda(){	
		document.forms[0].txtCodVia.value =	DatosBusqueda[0];
		document.forms[0].txtDomicilio.value = DatosBusqueda[1];
		document.forms[0].codPais.value = DatosBusqueda[2];
		document.forms[0].descPais.value = DatosBusqueda[3];
		document.forms[0].codProvincia.value = DatosBusqueda[4];
		document.forms[0].descProvincia.value = DatosBusqueda[5];
		document.forms[0].codMunicipio.value = DatosBusqueda[6];
		document.forms[0].descMunicipio.value = DatosBusqueda[7];
		document.forms[0].txtNumDesde.value	= DatosBusqueda[8]== 0	? "":	DatosBusqueda[10];
		document.forms[0].txtLetraDesde.value = DatosBusqueda[9];
		document.forms[0].txtNumHasta.value	= DatosBusqueda[10]== 0	? "":	DatosBusqueda[12];
		document.forms[0].txtLetraHasta.value = DatosBusqueda[11];
		document.forms[0].txtBloque.value =	DatosBusqueda[12];
		document.forms[0].txtPortal.value =	DatosBusqueda[13];
		document.forms[0].txtEsc.value = DatosBusqueda[14];
		document.forms[0].txtPlta.value = DatosBusqueda[15];
		document.forms[0].txtPta.value = DatosBusqueda[16];
		document.forms[0].txtKm.value	= DatosBusqueda[17];
		document.forms[0].txtHm.value	= DatosBusqueda[18];
		document.forms[0].idVia.value	= DatosBusqueda[19];
		/* Anadir ECO/ESI */		
		var codigoECO =	DatosBusqueda[20];
		var codigoESI =	DatosBusqueda[21];
		var descripcionESI = DatosBusqueda[22];
	//	if( (document.forms[0].codMunicipio.value=="<%=ptVO.getMunicipio()%>") &&  ( document.forms[0].codProvincia.value=="<%=ptVO.getProvincia()%>")){

			if (codigoECO!=""){
				comboECO.buscaCodigo(codigoECO);
			} else comboECO.selectItem(-1);
			
			if (codigoECO!="" && codigoESI!=""){				
				//comboESI.buscaCodigo(codigoESI);
				document.forms[0].codESI.value=codigoESI;
				document.forms[0].descESI.value=descripcionESI;
			} else {
				comboESI.selectItem(-1);
			}
	//		document.forms[0].txtCodVia.value =	DatosBusqueda[0];
			if (document.forms[0].txtCodVia.value!='') {
				document.forms[0].codVia.value = Terceros[idTercero][18][i][23];
				document.forms[0].descVia.value = Terceros[idTercero][18][i][3];
			} else {
				document.forms[0].codVia.value = "";
				document.forms[0].descVia.value = "";			
			}
	/*	 } else {
			comboECO.selectItem(-1);
			comboESI.selectItem(-1);
			document.forms[0].codVia.value = Terceros[idTercero][18][i][23];//"";
			document.forms[0].descVia.value = Terceros[idTercero][18][i][3]//"";
			document.forms[0].txtDomicilio.value = "";
			document.forms[0].txtCodVia.value = "";						
		 }
	*/
		/* Fin anadir ECO/ESI */		
    }	

    function cargaDomicilio(i){

        document.forms[0].txtCodVia.value =	Domicilios[i][0];
		document.forms[0].txtDomicilio.value = Domicilios[i][1];
		document.forms[0].codPais.value = Domicilios[i][2];
		document.forms[0].descPais.value = Domicilios[i][3];
		document.forms[0].codProvincia.value = Domicilios[i][4];
		document.forms[0].descProvincia.value = Domicilios[i][5];
		document.forms[0].codMunicipio.value = Domicilios[i][6];
		document.forms[0].descMunicipio.value = Domicilios[i][7];
		document.forms[0].txtNumDesde.value	= Domicilios[i][10]== 0	? "":	Domicilios[i][10];
		document.forms[0].txtLetraDesde.value = Domicilios[i][11];
		document.forms[0].txtNumHasta.value	= Domicilios[i][12]== 0	? "":	Domicilios[i][12];
		document.forms[0].txtLetraHasta.value = Domicilios[i][13];
		document.forms[0].txtBloque.value =	Domicilios[i][14];
		document.forms[0].txtPortal.value =	Domicilios[i][15];
		document.forms[0].txtEsc.value = Domicilios[i][16];
		document.forms[0].txtPlta.value = Domicilios[i][17];
		document.forms[0].txtPta.value = Domicilios[i][18];
		document.forms[0].txtKm.value = Domicilios[i][19];
		document.forms[0].txtHm.value = Domicilios[i][20];
		document.forms[0].txtBarriada.value	= Domicilios[i][21];
		document.forms[0].txtCodPostal.value = Domicilios[i][22];
		document.forms[0].codUso.value = Domicilios[i][23]== 0 ? "": Domicilios[i][23];
		document.forms[0].descUso.value = Domicilios[i][24];
		document.forms[0].txtIdDomicilio.value = Domicilios[i][25];	
		document.forms[0].idVia.value	= Domicilios[i][26];
		/* Anadir ECO/ESI */		
		var codigoECO =	Domicilios[i][27];
		var codigoESI =	Domicilios[i][28];
		var descripcionESI = Domicilios[i][29];
        
      //  if( (document.forms[0].codMunicipio.value=="<%=ptVO.getMunicipio()%>") &&  ( document.forms[0].codProvincia.value=="<%=ptVO.getProvincia()%>")){

			if (codigoECO!="" && codigoESI!=""){
				comboECO.buscaCodigo(codigoECO);
				//comboESI.buscaCodigo(codigoESI);
				document.forms[0].codESI.value=codigoESI;
				document.forms[0].descESI.value=descripcionESI;
			} else {
				comboECO.selectItem(-1);
				comboESI.selectItem(-1);
			}

       //     document.forms[0].txtCodVia.value =	Domicilios[i][0];
            if (document.forms[0].txtCodVia.value!='') {
				document.forms[0].codVia.value = Domicilios[i][0];
				document.forms[0].descVia.value = Domicilios[i][1];
			} else {
				document.forms[0].codVia.value = "";
				document.forms[0].descVia.value = "";			
			}
	/*	 } else {

            comboECO.selectItem(-1);
			comboESI.selectItem(-1);
			document.forms[0].codVia.value =  "";

            document.forms[0].descVia.value = Domicilios[i][1];//"";
			document.forms[0].txtDomicilio.value = "";
			document.forms[0].txtCodVia.value =  "";
		 }   */
		 
		/* Fin anadir ECO/ESI */

    }	

    function pulsarLimpiar(){	
		limpiarDomicilio();
		valoresPorDefecto();
		tab.lineas=listaVacia;
		refresco();	
    }	

    function refresco(){
		tab.displayTabla();
    }	
    
    function ReadOnly(){
		var codVia = document.forms[0].txtCodVia.value;	
		var vector = ['txtBarriada'];	
		if (codVia!=""){
			habilitarGeneralInputs(vector,false);
			document.forms[0].txtDomicilio.readOnly=true;	
		}else{
			habilitarGeneralInputs(vector,true);
			document.forms[0].txtDomicilio.readOnly=false;
		}
    }	

    function limpiarDomicilio(){
		document.forms[0].idVia.value	= "";	
		document.forms[0].txtIdDomicilio.value = "";
		document.forms[0].txtCodVia.value =	"";
		document.forms[0].txtDomicilio.value = "";
		document.forms[0].txtNumDesde.value	= "";	
		document.forms[0].txtLetraDesde.value = "";
		document.forms[0].txtNumHasta.value	= "";	
		document.forms[0].txtLetraHasta.value = "";
		document.forms[0].txtBloque.value =	"";
		document.forms[0].txtPortal.value =	"";
		document.forms[0].txtEsc.value = "";
		document.forms[0].txtPlta.value = "";
		document.forms[0].txtPta.value = "";
		document.forms[0].txtKm.value	= "";	
		document.forms[0].txtHm.value	= "";	
		document.forms[0].codUso.value = "";
		document.forms[0].descUso.value = "";
		document.forms[0].txtCodPostal.value = "";
		document.forms[0].txtBarriada.value	= "";
		comboECO.selectItem(-1);
		comboESI.selectItem(-1);
		document.forms[0].codVia.value = "";
		document.forms[0].descVia.value = "";						
    }	

	function limpiaVial(){
		var codVia	= document.forms[0].txtCodVia.value;
		if (codVia!=""){
			document.forms[0].txtDomicilio.value = "";
			document.forms[0].txtCodVia.value	= "";	
			document.forms[0].idVia.value	= "";	
		}
	}	

    function pulsarBuscarDomicilio(){
		//if(validarFormulario()){
		var criteriosBusqueda = false;
		if( (comboMunicipio.cod.value=="<%=ptVO.getMunicipio()%>") &&  ( comboProvincia.cod.value=="<%=ptVO.getProvincia()%>")){
			if (document.forms[0].codESI.value!="" 
				|| document.forms[0].codVia.value!="" || document.forms[0].txtDomicilio.value!="") {
					criteriosBusqueda = true;
			} else {
					  jsp_alerta("A",'<%=descriptor.getDescripcion("gMsg_CritBus")%>');	
			}
		}		
		if (criteriosBusqueda) {
			if (viaBuscada()){
				desHabilitarProvinciaMunicipio(false);
				document.forms[0].ventana.value="mainFrame1";	
				document.forms[0].target="oculto";
				document.forms[0].opcion.value="buscarDomicilio";
				document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
				document.forms[0].submit();	
				desHabilitarProvinciaMunicipio(true);
			}  else jsp_alerta("A",'<%=descriptor.getDescripcion("msjViaNoBusc")%>');	
		}
    }	

    function recuperaBusquedaDomicilio(){	
		var frame="oculto";

        Domicilios=eval("top."+frame+".Domicilio");
		lista=eval("top."+frame+".lista");
		longitud=Domicilios.length;
		if(longitud==0)
			jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoDatos")%>');
		tab.lineas=lista;	
		refresco();	
    }	

    function pulsarAceptar(){	
		var idDomicilio =	document.forms[0].txtIdDomicilio.value;
		var i=tab.selectedIndex;
		var domicilio = new	Array();
      	if((i>=0)&&(!tab.ultimoTable)) {
			if(idDomicilio!=""){			
				domicilio[0]=Domicilios[i];
			}
		}
		self.parent.opener.retornoXanelaAuxiliar(domicilio);
    }	
    
    var ultimo = false;	
    var pagin;

    document.onmouseup = checkKeys;	

    function checkKeysLocal(evento,tecla){
       var teclaAuxiliar = "";
      if(window.event){
          evento = window.event;
          teclaAuxiliar = evento.keyCode;
      }else{
          teclaAuxiliar = evento.which;
      }
		keyDel(evento);
		if (teclaAuxiliar	== 40){
			upDownTable(tab,lista,teclaAuxiliar);
		}
		if (teclaAuxiliar	== 38){
			upDownTable(tab,lista,teclaAuxiliar);
		}
		if(teclaAuxiliar == 13){
			pushEnterTable(tab,lista);
		}
    }
	
	/* Anadir ECO/ESI */
	
 	var codECOs = new Array();
	var descECOs = new Array();	
	var codESIs = new Array();
	var descESIs = new Array();	
	var listaECOESIs = new Array();
	    
    var codESIsOld = new Array();
    var descESIsOld = new Array();
	var listaECOESIsOld = new Array();
	
	<% 
		Vector listaECOs = bForm.getListaECOs();
		Vector listaESIs = bForm.getListaESIs();

		int lengthECOs = listaECOs.size();
		int lengthESIs = listaESIs.size();
		
		String codESIs ="";
		String descESIs ="";
		String listaECOESIs ="";
		String lcodECOS = "";
		String ldescECOS = "";
		
		if(lengthECOs>0) {	
	      for(i=0;i<lengthECOs-1;i++){
	        GeneralValueObject eco = (GeneralValueObject)listaECOs.get(i);
			lcodECOS+= "'"+(String)eco.getAtributo("codECO")+"',";
			ldescECOS+= "'"+(String)eco.getAtributo("descECO")+"',";
		  }
		  GeneralValueObject eco = (GeneralValueObject)listaECOs.get(i);
		  lcodECOS+= "'"+(String)eco.getAtributo("codECO")+"'";
		  ldescECOS+= "'"+(String)eco.getAtributo("descECO")+"'";
		}

		if (lengthESIs > 0) {
			for(int j=0;j<lengthESIs-1;j++) {
				GeneralValueObject esiss = (GeneralValueObject)listaESIs.get(j);
				codESIs += "\""+ (String) esiss.getAtributo("codEntidadSingular") + "\",";
				descESIs += "\""+ (String) esiss.getAtributo("nombreOficial") + "\",";
				listaECOESIs += "[\""+  (String) esiss.getAtributo("codEntidadColectiva")+ "\",\""
							+  (String) esiss.getAtributo("descEntidadColectiva")+ "\"],";
			}
			GeneralValueObject esiss = (GeneralValueObject)listaESIs.get(lengthESIs-1);
			codESIs += "\""+ (String) esiss.getAtributo("codEntidadSingular") + "\"";
			descESIs += "\""+ (String) esiss.getAtributo("nombreOficial") + "\"";
			listaECOESIs += "[\""+  (String) esiss.getAtributo("codEntidadColectiva")+ "\",\""
							+  (String) esiss.getAtributo("descEntidadColectiva")+ "\"]";
		}
%>

	codECOs= [<%=lcodECOS%>];
	descECOs= [<%=ldescECOS%>];
	
    codESIsOld = [<%= codESIs %>];
    descESIsOld = [<%= descESIs %>];
	listaECOESIsOld = [<%= listaECOESIs %>];
	
    codESIs= [<%= codESIs %>];
    descESIs= [<%= descESIs %>];
	listaECOESIs = [<%= listaECOESIs %>];
		

    function cargarListas(){
		desHabilitarProvinciaMunicipio(false);				
        document.forms[0].opcion.value="cargarListas";
        document.forms[0].target="oculto";
        document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
		document.forms[0].ventana.value="true";
        document.forms[0].submit();
		desHabilitarProvinciaMunicipio(true);
	}
	
	function desHabilitarECOESIVIA(valor) {
		limpiarECOESIVIA();
		if (valor) {
			comboECO.deactivate();
			comboESI.deactivate();
			deshabilitarGeneral([document.forms[0].codVia,document.forms[0].descVia]);			
			deshabilitarImagen(new Array( document.forms[0].botonV),true);			
		} else {
			comboECO.activate();
			comboESI.activate();
			habilitarGeneral([document.forms[0].codVia,document.forms[0].descVia]);			
			habilitarImagen(new Array( document.forms[0].botonV),true);
		}
	}
	
	function limpiarECOESIVIA(){
		comboECO.selectItem(-1);
		comboESI.selectItem(-1);	
		document.forms[0].codVia.value="";
		document.forms[0].descVia.value="";
		limpiarVia();
	}

  function valoresPorDefecto(){
    	document.forms[0].codPais.value	="<%=ptVO.getPais()%>";	
		document.forms[0].descPais.value = "<%=ptVO.getNomPais()%>"	
		document.forms[0].codProvincia.value = "<%=ptVO.getProvincia()%>";
		document.forms[0].descProvincia.value = "<%=ptVO.getNomProvincia()%>";
		document.forms[0].codMunicipio.value ="<%=ptVO.getMunicipio()%>";	
    	document.forms[0].descMunicipio.value	= "<%=ptVO.getNomMunicipio()%>";
    	codMunicipios	= codMunicipiosDefecto;	
    	descMunicipios = descMunicipiosDefecto;		
		/* anadir ECOESI */
		codESIs = codESIsOld;
    	descESIs = descESIsOld;
		listaECOESIs = listaECOESIsOld;
		comboESI.addItems(codESIs,descESIs);
		comboESI.selectItem(-1);
		comboECO.selectItem(-1);
		document.forms[0].codVia.value="";
		document.forms[0].descVia.value="";
		document.forms[0].txtCodVia.value="";
		document.forms[0].idVia.value="";		
		/* fin anadir ECOESI */
		desHabilitarProvinciaMunicipio(true);
  }
	
	
	function limpiarVia(){
		var descVia =	document.forms[0].descVia.value;
    	if (descVia==""){
			document.forms[0].txtDomicilio.value = "";
			document.forms[0].txtCodVia.value =	"";
			document.forms[0].codVia.value = "";
			document.forms[0].idVia.value = "";
			document.forms[0].codTVia.value = "";			
    	}	
  	}
	
	
	function viaBuscada(){
		var buscada = false;
		if(document.forms[0].descVia.value != "") {
			if (document.forms[0].codVia.value !="")
				buscada = true;				
		} else {
			limpiarVia();
			buscada = true; // Por si no ha saltado el onchange
		}
		return buscada;
	}
	
	function cargarListaViasBuscadas(total, idVia, codVia, descVia, descTipoVia, codECO, codESI, codTipoVia){	
		if (total==0){
			document.forms[0].txtCodVia.value = "";
			document.forms[0].codVia.value = "";
			document.forms[0].descVia.value = "";
			document.forms[0].txtDomicilio.value = "";			
			document.forms[0].idVia.value = "";			
			document.forms[0].codTVia.value = "";		
			jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoDatos")%>');			
		} else 	if (total==1){		
			document.forms[0].codECO.value = codECO;			
			if (codESI != '') comboESI.buscaCodigo(codESI);			
			document.forms[0].txtCodVia.value = codVia;
			document.forms[0].codVia.value = codVia;
			document.forms[0].descVia.value = descVia;
			document.forms[0].txtDomicilio.value = descVia;			
			document.forms[0].idVia.value = idVia;			
		} else {
                                        var source = "<%=request.getContextPath()%>/jsp/terceros/listaViasBuscadas.jsp?opcion=null";
                                        abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source="+source,'',
                                                'width=500,height=400,status='+ '<%=statusBar%>',function(datosConsulta){
                                                        if(datosConsulta!=undefined){
                                                            if (datosConsulta[4]=='null') datosConsulta[4] = '';
                                                            document.forms[0].codECO.value = datosConsulta[4];						
                                                            if ( datosConsulta[4] != '') comboESI.buscaCodigo(datosConsulta[5]);			
                                                            document.forms[0].txtCodVia.value = datosConsulta[1];
                                                            document.forms[0].codVia.value = datosConsulta[1];
                                                            document.forms[0].descVia.value = datosConsulta[2];	
                                                            document.forms[0].txtDomicilio.value = datosConsulta[2];					  	
                                                            document.forms[0].idVia.value = datosConsulta[0];	
                                                            document.forms[0].codTVia.value = datosConsulta[6];		
                                                        }					
                                                });
                                    }
	}
	

	function pulsarBuscarVia() {	
  		if(document.forms[0].descVia.value != "") {
			document.forms[0].opcion.value="buscarVias";
    		document.forms[0].target="oculto";
    		document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Viales.do";
    		document.forms[0].submit();
  		} else {
    		jsp_alerta("A",'<%=descriptor.getDescripcion("gMsg_CritBus")%>');
  		}
    }

/* fin anadir ECO/ESI */	
/* otras cosas */

	function desHabilitarProvinciaMunicipio(valor) {
	
		if (valor){
			comboProvincia.deactivate();
			comboMunicipio.deactivate();
		} else {
			comboProvincia.activate();
			comboMunicipio.activate();		
		}	
	}

  </script>	
</head>

<body class="bandaBody" onload="inicializar();"	onunload="pulsarAceptar();">

<form METHOD=POST	target="_self">
<input  type="hidden" name="opcion">
<input type="hidden" name="ventana"	value="mainFrame2">
<input type="hidden" name="txtIdDomicilio">
<input type="hidden" name="txtBarriada">
<input type="hidden" name="txtCodPostal">	
<input type="hidden" name="codUso">	
<input type="hidden" name="descUso">
<input type="hidden" name="codPais"	size="3" value="<%=ptVO.getPais()%>">
<input type="hidden" name="descPais" size="3">
<input type="hidden" name="idVia" value="">
<!-- anadir ECO/ESI -->
<input type="hidden" name="txtCodVia">	
<input type="hidden" name="codTVia">
<!-- fin anadir ECO/ESI -->	

<div class="txttitblanco"><%=descriptor.getDescripcion("manTer_TitSelDomicil")%></div>
<div class="contenidoPantalla">
    <table width="100%">
        <tr>
                <td width="12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqProvincia")%>:</td>
                <td width="39%">	
                        <input class="inputTextoObligatorio" type="text" id="codProvincia" name="codProvincia" size="3">
                        <input class="inputTextoObligatorio" type="text" id="descProvincia" name="descProvincia" style="width:215" readonly>
                        <a id="anchorProvincia" name="anchorProvincia" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonProvincia" name="botonProvincia" style="cursor:hand;"></span></a>
                </td>
                <td width="12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqMunicipio")%>:</td>
                <td width="37%">	
                        <input class="inputTextoObligatorio" type="text" id="codMunicipio" name="codMunicipio" size="3">
                        <input id="descMunicipio" name="descMunicipio" type="text" class="inputTextoObligatorio"	style="width:210"	readonly>
                        <a id="anchorMunicipio" name="anchorMunicipio" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonMunicipio" name="botonMunicipio" style="cursor:hand;"></span></a>
                </td>
        </tr>
    </table>	
    <table width="100%">
        <tr>
                <td style="visibility:<%=ecv%>" width="12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqParroquia")%>:</td>
                <td style="visibility:<%=ecv%>" width="39%">
                        <input type="text" id="codECO" name="codECO" class="inputTexto" size="3">
                        <input type="text" id="descECO" name="descECO" class="inputTexto" style="width:215" readonly="true"> 
                        <a href="" id="anchorECO" name="anchorECO"><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonECO" name="botonECO" style="cursor:hand;"></span></a>
                </td>
                <td style="visibility:<%=esv%>" width="12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqLugar")%>:</td>
                <td style="visibility:<%=esv%>" width="37%">
                        <input type="text" id="codESI" name="codESI" class="inputTexto" size="3">
                        <input type="text" id="descESI" name="descESI" class="inputTexto" style="width:210" readonly="true"> 
                        <a href="" id="anchorESI" name="anchorESI"><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonESI" name="botonESI" style="cursor:hand;"></span></a>
                </td>
        </tr>
    </table>
    <table width="100%">
        <tr>
                <td width="5%" class="etiqueta"><%=descriptor.getDescripcion("etiqVia")%>:</td>	
                <td width="46%" class="columnP">
                        <input type="hidden" name="codVia">
                        <input type="text" name="descVia" class="inputTexto" style="width:220" maxlength=50 onkeypress="javascript:PasaAMayusculas(event);" onchange="javascript:limpiarVia();"> 
                        <span class="fa fa-search" aria-hidden="true"  name="botonV" alt="Buscar Vía" style="cursor:hand;" onclick="javascript:pulsarBuscarVia();"></span>
                </td>
                <td style="visibility:<%=emplv%>" width="15%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqEmplazamiento")%>:</td>
                <td width="37%" class="columnP">
                                <input type="text" id="txtDomicilio" name="txtDomicilio" class="inputTexto" style="width:264" maxlength=50
                                        onKeyPress="return PasaAMayusculas(event);"> 																			
                </td>
        </tr>
    </table>
    <table width="100%">
        <tr>
                <td width="7%" class="etiqueta" align="left"><%=descriptor.getDescripcion("gEtiq_PNumero")%></td>
                <td width="9%" class="etiqueta" align="center"><%=descriptor.getDescripcion("gEtiq_Letra")%></td>
                <td width="2%" class="etiqueta" align="left"></td>
                <td width="7%" class="etiqueta" align="left"><%=descriptor.getDescripcion("gEtiq_UNumero")%></td>
                <td width="9%" class="etiqueta" align="center"><%=descriptor.getDescripcion("gEtiq_Letra")%></td>
                <td width="2%" class="etiqueta" align="left"></td>
                <td width="9%" class="etiqueta" align="center"><%=descriptor.getDescripcion("manTer_EtiqBLQ")%></td>
                <td width="9%" class="etiqueta" align="center"><%=descriptor.getDescripcion("manTer_EtiqPTL")%></td>
                <td width="9%" class="etiqueta" align="center"><%=descriptor.getDescripcion("gEtiq_Escalera")%></td>
                <td width="9%" class="etiqueta" align="center"><%=descriptor.getDescripcion("gEtiq_Planta")%></td>
                <td width="9%" class="etiqueta" align="center"><%=descriptor.getDescripcion("gEtiq_Puerta")%></td>
                <td width="9%" class="etiqueta" align="center">Km.</td>
                <td width="9%" class="etiqueta" align="center">Hm.</td>
        </tr>
        <tr>
                <td width="7%" class="columnP" align="left">	
                        <input name="txtNumDesde" TYPE="text" class="inputTexto"	id="numero"	SIZE=6 MAXLENGTH=3 onKeyPress =	"javascript:return SoloDigitos(event);">
                </td>
                <td width="9%" class="columnP" align="center">	
                        <input name="txtLetraDesde" TYPE="text" class="inputTexto" id="txtLetraDesde" SIZE=6 MAXLENGTH=3 onKeyPress="javascript:PasaAMayusculas(event);">
                </td>
                <td width="2%" class="etiqueta" align="left"></td>																			
                <td width="7%" class="columnP" align="left">
                        <input name="txtNumHasta" TYPE="text" class="inputTexto"	id="numero"	SIZE=6 MAXLENGTH=3 onKeyPress =	"javascript:return SoloDigitos(event);"> 
                </td>
                <td width="9%" class="columnP" align="center">
                        <input name="txtLetraHasta" TYPE="text" class="inputTexto" id="txtLetraHasta" SIZE=6 MAXLENGTH=3 onKeyPress="javascript:PasaAMayusculas(event);">
                </td>
                <td width="2%" class="etiqueta" align="left"></td>																			
                <td width="9%" class="columnP"  align="center">
                        <input TYPE="text" class="inputTexto" name="txtBloque" SIZE=6 MAXLENGTH=3 onKeyPress="javascript:PasaAMayusculas(event);">	
                </td>			
                <td width="9%" class="columnP"  align="center">
                        <input TYPE="text" class="inputTexto" name="txtPortal" SIZE=6 MAXLENGTH=3 onKeyPress="javascript:PasaAMayusculas(event);">	
                </td>
                <td width="9%" class="columnP"  align="center">
                        <input TYPE="text" class="inputTexto" name="txtEsc" SIZE=6 MAXLENGTH=3 onKeyPress="javascript:PasaAMayusculas(event);">
                </td>
                <td width="9%" class="columnP"  align="center">
                        <input TYPE="text" class="inputTexto" name="txtPlta" SIZE=6 MAXLENGTH=3 onKeyPress="javascript:PasaAMayusculas(event);">
                </td>
                <td width="9%" class="columnP"  align="center">
                        <input TYPE="text" class="inputTexto" name="txtPta" SIZE=6 MAXLENGTH=3 onKeyPress="javascript:PasaAMayusculas(event);">
                </td>
                <td width="9%" class="columnP"  align="center">
                        <input TYPE="text" class="inputTexto" name="txtKm"	SIZE=6 MAXLENGTH=3>
                </td>
                <td width="9%" class="columnP"  align="center">
                        <input TYPE="text" class="inputTexto" name="txtHm"	SIZE=6 MAXLENGTH=3>
                </td>
            </tr>
    </table>	
    <table width="100%">
        <tr> 
            <td>
                <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbBuscar")%>" name="cmdBuscar" accesskey="B" onClick="pulsarBuscarDomicilio();return false;">
                <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbLimpiar")%>" name="cmdLimpiar" accesskey="L" onClick="pulsarLimpiar();return false;">
                </td>
            </tr>
    </table>	
    <table border="0px" cellspacing="0" cellpadding="0" class="fondoCab">
        <tr height="160px"> 
            <td>
                    <div id="tabla"></div>
            </td>
        </tr>
        <tr>
            <td id="enlace" height="10px"></td>
        </tr>
    </table>	
    <div class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" name="cmdAceptar" onClick="pulsarAceptar();return false;" value="<%=descriptor.getDescripcion("gbAceptar")%>">	
    </div>
</div>
<!-- FIN	Código del cuerpo	-->
</form>

<script language="JavaScript1.2">
    var tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tabla"));
    tab.addColumna("190","center",'<%= descriptor.getDescripcion("domEtiq_Num")%>');
    tab.addColumna("120","center",'<%= descriptor.getDescripcion("domEtiq_BloquePortal")%>');
    tab.addColumna("100","center",'<%= descriptor.getDescripcion("domEtiq_Escalera")%>');
    tab.addColumna("120","center",'<%= descriptor.getDescripcion("domEtiq_PlantaPuerta")%>');
    tab.addColumna("100","center",'<%= descriptor.getDescripcion("domEtiq_KmHm")%>');
    tab.addColumna("30","center",'<%=descriptor.getDescripcion("etiqOrig")%>');

    tab.displayCabecera=true;
  
	function refresco(){
		tab.displayTabla();
	}
  
	function pintaDatos(datos){	
		var i=tab.selectedIndex;
      	if((i>=0)&&(!tab.ultimoTable)){
			cargaDomicilio(i);
		} else {
			limpiarDomicilio();
		}
	}		
	tab.displayDatos = pintaDatos;
  
	function callFromTableTo(rowID,tableName){
		if(tab.id == tableName){						
			pulsarAceptar();
		}	
	}

	var	comboProvincia = new Combo("Provincia");
	var	comboMunicipio = new Combo("Municipio");
  
	function cargarComboBox(cod, des){
		eval(auxCombo+".addItems(cod,des)");
	}

	comboProvincia.change	=   function() {	
	  auxCombo='comboMunicipio'; 
	  limpiar(["codMunicipio","descMunicipio"]);
	  limpiarDomicilio();
	  limpiaVial();
	  if(comboProvincia.cod.value.length!=0){
		desHabilitarECOESIVIA(true);
		cargarListaMunicipios();
	  } else{
	    comboMunicipio.addItems([],[]);
	  }		
    }	

  comboMunicipio.change	= 
    function() {	
		limpiarDomicilio();
		limpiaVial();
		if(comboMunicipio.cod.value.length!=0){
		 if( (comboMunicipio.cod.value=="<%=ptVO.getMunicipio()%>") &&  ( comboProvincia.cod.value=="<%=ptVO.getProvincia()%>"))
		 	desHabilitarECOESIVIA(false);
		 else desHabilitarECOESIVIA(true);
		 }
    }	

 /* anadir ECO/ESI */ 
  	var comboECO = new Combo("ECO");
	var comboESI = new Combo("ESI");

	comboECO.change = function() { 			
			auxCombo='comboESI'; 
			limpiar(['codESI','descESI','codVia','descVia','txtCodVia', 'idVia']);
			cargarListas();						
		}
	
    comboESI.change = function() { 			
			limpiar(['codVia','descVia','txtCodVia', 'idVia']);  			
			if(comboESI.cod.value.length!=0){
				var i = comboESI.selectedIndex-1;
				if(i>=0){
					//comboECO.buscaLinea(listaECOESIs[i][0]);					
					document.forms[0].codECO.value = listaECOESIs[i][0];
          			document.forms[0].descECO.value = listaECOESIs[i][1];					
				}	
				cargarListas(); 			
			}	
	} 
	
	
 /* anadir ECO/ESI */ 
  

</script>
</body>
</html>
