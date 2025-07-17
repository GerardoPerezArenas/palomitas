<%@page contentType="text/html; charset=iso-8859-1"	language="java" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm"%>	
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="es.altia.agora.business.terceros.TercerosValueObject" %>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="java.util.StringTokenizer"%>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<HTML><head>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<TITLE>::: TERCEROS - Mantenimiento	:::</TITLE>	
<%	UsuarioValueObject usuarioVO = new UsuarioValueObject();
    boolean bAltaViaDirecta = false;
    Config m_Conf = ConfigServiceHelper.getConfig("common");
    String JSP_altaViaDirecta = m_Conf.getString("JSP.altaViaDirecta");
    String JSP_entidadColectiva = m_Conf.getString("JSP.EntidadColectiva");
    String JSP_entidadSingular = m_Conf.getString("JSP.EntidadSingular");
    String JSP_emplazamiento = m_Conf.getString("JSP.Emplazamiento");
    String JSP_poblacion = m_Conf.getString("JSP.Poblacion");
    String CodigoSinTipoVia = m_Conf.getString("T_TVI.CodigoSinTipoVia");
    String ecv = "visible";
    String esv = "visible";
    String emplv = "visible";
    String pv = "visible";
    if("no".equals(JSP_entidadColectiva)) ecv = "hidden";
    if("no".equals(JSP_entidadSingular)) esv = "hidden";
    if("no".equals(JSP_emplazamiento)){ emplv = "hidden";}
    if("no".equals(JSP_poblacion)) pv = "hidden";

    StringTokenizer st = new StringTokenizer(JSP_altaViaDirecta, ",");
    while (st.hasMoreTokens()){
        if(st.nextToken().trim().equals(String.valueOf(usuarioVO.getOrgCod()))){ bAltaViaDirecta = true;}
    }
    ParametrosTerceroValueObject ptVO	= null;
  	int	idioma=1;
	int apl=1;
	String origen = (String) session.getAttribute("origen");
    String existeTramero = "";
    String inicioAlta = "";
    String inicioAltaDesdeBuscar = "";
    String inicioModificar = "";
    String cargarTercero = "";
  	if (session.getAttribute("usuario") != null){
		usuarioVO =	(UsuarioValueObject)session.getAttribute("usuario");
		ptVO = (ParametrosTerceroValueObject)session.getAttribute("parametrosTercero");
    	idioma = usuarioVO.getIdioma();
		apl =	usuarioVO.getAppCod();
        existeTramero = usuarioVO.getExisteTramero();
    }
    if(session.getAttribute("inicioAlta") != null) {
      inicioAlta = (String)session.getAttribute("inicioAlta");
    } else {
      inicioAlta = "no";
    }
    session.removeAttribute("inicioAlta");
    if(session.getAttribute("inicioAltaDesdeBuscar") != null) {
      inicioAltaDesdeBuscar = (String)session.getAttribute("inicioAltaDesdeBuscar");
    } else {
      inicioAltaDesdeBuscar = "no";
    }
    session.removeAttribute("inicioAltaDesdeBuscar");
  	if(session.getAttribute("inicioModificar") != null) {
		inicioModificar = (String)session.getAttribute("inicioModificar");
  	} else {
    	inicioModificar = "no";
  	}
  	session.removeAttribute("inicioModificar");
  	if(session.getAttribute("cargarTercero") != null) {
  		cargarTercero = (String)session.getAttribute("cargarTercero");
    } else {
    	cargarTercero = "no";
    }
    session.removeAttribute("cargarTercero");
     String statusBar = m_Conf.getString("JSP.StatusBar");
%>
<jsp:useBean id="descriptor" scope="request"
  class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"	property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"	property="apl_cod" value="<%=apl%>"	/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<LINK	REL="stylesheet" MEDIA="screen" type="text/css"	href="<%=request.getContextPath()%>/css/estilo.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaBusquedaTerceros.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/seleccionBusquedaTerceros.js"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>"></script>
<SCRIPT type="text/javascript" >
<%	BusquedaTercerosForm bForm =(BusquedaTercerosForm)session.getAttribute("BusquedaTercerosForm");
	Vector listaDocs = bForm.getListaTipoDocs();
	Vector listaProvincias = bForm.getListaProvincias();
	Vector listaMunicipios = bForm.getListaMunicipios();
	int lengthDocs = listaDocs.size();
	int lengthProvs = listaProvincias.size();
	int lengthMun = listaMunicipios.size();
	int i;
	String lcodProv= "";
	String ldescProv = "";
	String lcodMun = "";
	String ldescMun = "";
	String lcodDocs= "";
	String ldescDocs = "";
	String lpersJF = "";
	if(lengthDocs>0) {
	    for(i=0;i<lengthDocs-1;i++){
	        GeneralValueObject doc = (GeneralValueObject)listaDocs.get(i);
			lcodDocs+= "'"+doc.getAtributo("codTipoDoc")+"',";
			ldescDocs+= "'"+doc.getAtributo("descTipoDoc")+"',";
			lpersJF+= "'"+doc.getAtributo("persFJ")+"',";
		}
		GeneralValueObject doc = (GeneralValueObject)listaDocs.get(i);
		lcodDocs+= "'"+doc.getAtributo("codTipoDoc")+"'";
		ldescDocs+= "'"+doc.getAtributo("descTipoDoc")+"'";
		lpersJF+= "'"+doc.getAtributo("persFJ")+"'";
	}
	if(lengthProvs>0) {
	      for(i=0;i<lengthProvs-1;i++){
	        GeneralValueObject  prov= (GeneralValueObject)listaProvincias.get(i);
			lcodProv += "'"+prov.getAtributo("codigo")+"',";
			ldescProv += "'"+prov.getAtributo("descripcion")+"',";
		  }
		  GeneralValueObject  prov= (GeneralValueObject)listaProvincias.get(i);
		  lcodProv += "'"+prov.getAtributo("codigo")+"'";
		  ldescProv += "'"+prov.getAtributo("descripcion")+"'";
	}
	if(lengthMun>0) {
	      for(i=0;i<lengthMun-1;i++){
	        GeneralValueObject  mun= (GeneralValueObject)listaMunicipios.get(i);
			lcodMun+= "\""+ mun.getAtributo("codMunicipio")+"\",";
			ldescMun+= "\""+ mun.getAtributo("nombreOficial")+"\",";
		  }
		  GeneralValueObject  mun = (GeneralValueObject)listaMunicipios.get(i);
		  lcodMun+= "\""+ mun.getAtributo("codMunicipio")+"\"";
		  ldescMun+= "\""+ mun.getAtributo("nombreOficial")+"\"";
	}
	%>

	// ------ Funciones de página
	var ventana=false;

function inicializar() {
    var tipo = "<%=request.getParameter("tipo")%>";
    if (tipo!='B') {
        var tDoc = tipo.charAt(0);
        var t = tipo.charAt(1);
    }
    
    var descDoc	= "<%=request.getParameter("descDoc")%>";
    var docu = "<%=request.getParameter("docu")%>";
    
    recuperaDatosIniciales();
    valoresPorDefecto();
    
    comboTipoDoc.buscaCodigo(tDoc);
    
    if (docu!="null") {
        document.forms[0].txtDNI.value = docu;
    }
}

         function valoresPorDefecto(){
            document.forms[0].codProvincia.value = "<%=ptVO.getProvincia()%>";
            document.forms[0].descProvincia.value = "<%=ptVO.getNomProvincia()%>";
            document.forms[0].codMunicipio.value ="<%=ptVO.getMunicipio()%>";
            document.forms[0].descMunicipio.value	= "<%=ptVO.getNomMunicipio()%>";
            codMunicipios	= codMunicipiosDefecto;
            descMunicipios = descMunicipiosDefecto;
            paisOld	= document.forms[0].codPais.value;
            provinciaOld = document.forms[0].codProvincia.value;
            municipioOld = document.forms[0].codMunicipio.value;
        }

        
        
         function recuperaDatosIniciales(){
            codTipoDocs = [<%=lcodDocs%>];
            descTipoDocs =	[<%=ldescDocs%>];
            persFJ =[<%=lpersJF%>];
            codProvincias = [<%=lcodProv%>];
            descProvincias	= [<%=ldescProv%>];
            codMunicipios = [<%=lcodMun%>];
            descMunicipios	= [<%=ldescMun%>];
            codMunicipiosDefecto = codMunicipios;
            descMunicipiosDefecto =	descMunicipios;
            comboProvincia.addItems(codProvincias,descProvincias);
            comboMunicipio.addItems(codMunicipios,descMunicipios);
            comboTipoDoc.addItems(codTipoDocs,descTipoDocs);
	}
        function esPersonaFisica(){
            var pFJ= document.forms[0].perFJ.value;
    		if(pFJ==1) return true;
    		else  return false;		
         }
       //** Función que actualiza el estado de los campos de entrada 
	//** dependiendo de si un tercero es persona fisica o juridica.
	function actualizaPersonaFJ(){
            var tipo = document.forms[0].codTipoDoc.value;
            var i;

                    // Comprueba si el tipo de documento es valido
            documentoNoValido("codTipoDoc","txtDNI",0);

                    // Recupera el tipo de documento.
            for(i=0;i<codTipoDocs.length;i++){
                            if(codTipoDocs[i]==tipo) break;
            }
            if(i<codTipoDocs.length){
                            document.forms[0].perFJ.value= persFJ[i];
            }else{
                            document.forms[0].perFJ.value=1;
            }

                    // Se activan o desactivan los campos de apellidos dependiendo del tipo de documento.
            var vector = ["txtApell1","txtApell2"];
                            if(!esPersonaFisica()){
                    document.getElementById("txtApell1").className = "inputTexto";
                                    habilitarGeneralInputs(vector,false);
                                    document.forms[0].txtApell1.value="";
                                    document.forms[0].txtApell2.value="";
                }else {
                    habilitarGeneralInputs(vector,true);
                    if (document.forms[0].codTipoDoc.value == 1) {
                        if (document.getElementById("txtApell1").disabled == false) {
                            document.getElementById("txtApell1").className = "inputTextoObligatorio";
                        }
                    } else {
                            document.getElementById("txtApell1").className = "inputTexto";
                    }
                }
       }
  function cargarListaMunicipios(){
    document.forms[0].opcion.value="cargarMunicipios";
    document.forms[0].target=(ventana=="true")?"oculto":"oculto";
    document.forms[0].ventana.value=(ventana)?"true":"false";
    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
    document.forms[0].submit();
  }		
	
 function pulsarGrabarTercero(){     
     if (!validarTercero()) return;
     document.forms[0].opcion.value="grabarTerceroRapido";
     document.forms[0].target="oculto";
     document.forms[0].action='<c:url value="/BusquedaTerceros.do"/>';
     document.forms[0].submit();
 }
 
 function confirmarDocDuplicado() {
    if (jsp_alerta("", "<%=descriptor.getDescripcion("msjTerDocRepetido")%>")) {
       document.forms[0].opcion.value="grabarTerceroRapidoConfirmado";
       document.forms[0].target="oculto";
       document.forms[0].action='<c:url value="/BusquedaTerceros.do"/>';
       document.forms[0].submit();
    } else {
       document.forms[0].txtDNI.select();
    }
}

function confirmarExisteTercero() {
 if (jsp_alerta("",'<%=descriptor.getDescripcion("msjExTerSinDoc")%>')) {
     document.forms[0].grabarDirecto.value="si";
     pulsarGrabarTercero();
 } else {
     pulsarCancelarAlta();
     pulsarAlta();
 }
}
 
function validarTercero(){
    var codTipoDoc = document.forms[0].codTipoDoc.value;
    var documento = document.forms[0].txtDNI.value;
    var nombre = document.forms[0].txtInteresado.value;
    var apel1 = document.forms[0].txtApell1.value;
    var codProvincia = document.forms[0].codProvincia.value ;
    var codMuni = document.forms[0].codMunicipio.value;
          
    if((codTipoDoc == "" || codProvincia == "" || codMuni == "") || (codTipoDoc != "0" && documento == "") || (nombre == "") || (codTipoDoc == 1 && apel1 == "")) {
        jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
        return false;
    }
    if (documentoNoValido('codTipoDoc','txtDNI',1)) {
        return false;
    }
    return true;
}
 
     function altaCorrecta(id,idDomicilio){
      if (id!=0){		    
            document.forms[0].txtIdTercero.value=id;   
          var codTipoDoc=document.forms[0].codTipoDoc.value;
          var descTipoDoc=document.forms[0].descTipoDoc.value;
          var txtDNI=document.forms[0].txtDNI.value;
          var razonSocial=document.forms[0].txtInteresado.value;
          var apellido1=document.forms[0].txtApell1.value;
          var apellido2=document.forms[0].txtApell2.value;
          var codProvincia=document.forms[0].codProvincia.value ;
          var provincia=document.forms[0].descProvincia.value;
          var codMuni=document.forms[0].codMunicipio.value;
          var muni=document.forms[0].descMunicipio.value;
          var domicilio=document.forms[0].txtDomicilio.value;
          razonSocial=formatearNombre(document.forms[0].txtInteresado.value, document.forms[0].txtApell1.value, document.forms[0].txtApell2.value);
          retorno=[id,idDomicilio,codTipoDoc,descTipoDoc,txtDNI,razonSocial,apellido1,apellido2,codProvincia,provincia,codMuni,muni,domicilio];
         self.parent.opener.retornoXanelaAuxiliar(retorno);
        }else if(id==0){
             jsp_alerta("A",'No se ha podido grabar, algún dato es incorrecto.');
            }  
    }    
      function pulsarCancelarTercero(){
   
        self.parent.opener.retornoXanelaAuxiliar();

    }
    
function formatearNombre(nombre, apellido1, apellido2) {
    var nombreFormateado = "";
    if (apellido1!="" && apellido1 != undefined) nombreFormateado += apellido1.trim();
    if (apellido2!="" && apellido2 != undefined) {
        if (nombreFormateado.length > 0) nombreFormateado += " " + apellido2.trim();
        else nombreFormateado = apellido2.trim();
    }
    if (nombre!="" && nombre != undefined) {
        if (nombreFormateado.length > 0) nombreFormateado += ", " + nombre.trim();
        else nombreFormateado = nombre.trim();
    }
    return nombreFormateado;
}

function pulsarBuscar(){
    var tipo = document.forms[0].codTipoDoc.value;
    if (tipo == "" || documentoNoValido("codTipoDoc","txtDNI",1)) {
        return false;
    }
}

function pulsarCancelarAlta()	{    
}

function pulsarAlta()	{    
}
    
</SCRIPT>
</head>
<BODY class="bandaBody" onload="inicializar();" >

<form  method=POST target="_self">
    <input type="hidden" name="opcion">
    <input type="hidden" name="ventana" value="false">
    <input type="hidden" name="perFJ" value=1>
    <input type="hidden" name="codPais" size="3" value="<%=ptVO.getPais()%>">
    <input type="hidden" name="txtIdTercero">


<div id="titulo" class="txttitblanco">Alta Rápida de Tercero</div>
<div class="contenidoPantalla">
    <div class="sub3titulo" style="width: 100%"><%= descriptor.getDescripcion("manTer_TitDatos")%></div>
    <table width="100%">
        <tr>
            <td style="width: 22%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqTiDoc")%>:</td>
            <td style="width: 33%" class="columnP">
                <input id="codTipoDoc" name="codTipoDoc" type="text" class="inputTextoObligatorio" size="3">
                <input id="descTipoDoc" name="descTipoDoc" type="text" class="inputTextoObligatorio" style="width:174" readonly>
                <a id="anchorTipoDoc" name="anchorTipoDoc" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoDoc" name="botonTipoDoc"	style="cursor:pointer;" alt=""></span></a>
            </td>
            <td style="width: 12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqDocumento")%>:</td>
            <td style="width: 33%" class="columnP">
                <input type="text" class="inputTextoObligatorio" size=38 maxlength=16 name="txtDNI" onkeypress="javascript:PasaAMayusculas(event);" onchange="pulsarBuscar();">
            </td>
        </tr>
    </table>
    <table width="100%">
        <tr>
            <td style="width: 22%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqNombreRazon")%>:</td>
            <td style="width: 78%">
                <input type="text" id="obligatorio" class="inputTextoObligatorio" style="width:576" maxlength=80 name="txtInteresado" onKeyPress="javascript:PasaAMayusculas(event);">
            </td>
        </tr>
    </table>
    <table width="100%" cellspacing="0px" cellpadding="0px" border="0px">
        <tr>
            <td style="width: 22%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqApellido1Part")%>:</td>
            <td style="width: 33%" class="columnP">
                <input type="text" name="txtApell1" class="inputTexto" size=36 maxlength=25 onKeyPress="javascript:PasaAMayusculas(event);">
            </td>
            <td style="width: 12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqApellido2Part")%>:</td>
            <td style="width: 43%" class="columnP">
                <input name="txtApell2" type="text" class="inputTexto" size=38 maxlength=25 onKeyPress="javascript:PasaAMayusculas(event);">
            </td>
        </tr>
    </table>
    <div class="sub3titulo"><%= descriptor.getDescripcion("manTer_TitDomis")%></div>
    <table width="100%">
        <tr>
            <td style="width: 12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqProvincia")%>:</td>
            <td style="width: 39%" class="columnP">
                <input class="inputTextoObligatorio" type="text" id="codProvincia" name="codProvincia" size="3">
                <input class="inputTextoObligatorio" type="text" id="descProvincia" name="descProvincia" style="width:215" readonly>
                <a id="anchorProvincia" name="anchorProvincia" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonProvincia" name="botonProvincia" style="cursor:pointer;"></span></a>
            </td>
            <td style="width: 12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqMunicipio")%>:</td>
            <td style="width: 37%" class="columnP">
                <input class="inputTextoObligatorio" type="text" id="codMunicipio" name="codMunicipio" size="3">
                <input id="descMunicipio" name="descMunicipio" type="text" class="inputTextoObligatorio" style="width:210" readonly>
                <a id="anchorMunicipio" name="anchorMunicipio" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonMunicipio" name="botonMunicipio" style="cursor:pointer;"></span></a>
            </td>
        </tr>
    </table>
    <table width="100%">
    <tr >
        <td style="width: 15%" class="etiqueta"><%= descriptor.getDescripcion("gEtiqDomicilio")%>:</td>
        <td  style="width: 85%" class="columnP">
                <input type="text" id="txtDomicilio" name="txtDomicilio" class="inputTexto"style="width:650" maxlength=100
                onKeyPress="return PasaAMayusculas(event);">
            </td>
        </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type= "button" title='<%=descriptor.getDescripcion("toolTip_bAltaDom")%>' class="botonGeneral" name="cmdGrabarDomicilio" onClick = "pulsarGrabarTercero();return false;" value="<%=descriptor.getDescripcion("gbGrabar")%>" >
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' class="botonGeneral" name="cmdCancelarAltaDomic" onClick="pulsarCancelarTercero();return false;" value="<%=descriptor.getDescripcion("gbcaNcelar")%>" >
    </div>
</div>
</form>
<div id="desplegable" style="overflow-y: auto; overflow-x: no; visibility: hidden; BORDER: 0px"></div>
<script type="text/javascript">
/* Clase Combo importada de /scripts/listaComboBox.js. Antepone al nombre pasado como argumento
 los prefijos 'cod', 'desc' y 'anchor' para referirse a los campos apropiados de esta página. */
var	comboProvincia = new Combo("Provincia");
var	comboMunicipio = new Combo("Municipio");
var	comboTipoDoc = new Combo("TipoDoc");
var	auxCombo = "comboMunicipio";

comboTipoDoc.change = function() {
    document.forms[0].txtDNI.value="";
    actualizaPersonaFJ();
}
comboProvincia.change	= function() {
	auxCombo="comboMunicipio";
	limpiar(["codMunicipio","descMunicipio"]);
	if(comboProvincia.cod.value.length!=0){
	  cargarListaMunicipios();
	}else{
	  comboMunicipio.addItems([],[]);
	}
}

comboMunicipio.change	= function() {
    if(comboMunicipio.cod.value.length!=0){
    }else{
        comboMunicipio.selectItem(0);        
    }
}
function cargarComboBox(cod, des){
    eval(auxCombo+".addItems(cod,des)");
}

function mostrarDescripcionTipoDoc(){}
</script></BODY></HTML>
