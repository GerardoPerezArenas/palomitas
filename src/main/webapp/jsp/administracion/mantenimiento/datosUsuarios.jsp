<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.UsuariosGruposForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.UsuariosGruposValueObject"%>
<%@page import="java.util.ResourceBundle"%>

<html:html>
    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <TITLE>::: ADMINISTRACION  Datos de Usuarios:::</TITLE>
            <%
            int idioma = 1;
            int apl = 1;
            int munic = 0;
            String css="";
            if (session != null) {
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    apl = usuario.getAppCod();
                    munic = usuario.getOrgCod();
                    css=usuario.getCss();
                }
            }
            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
            
            String PORTAFIRMAS  = "";
            try{
                ResourceBundle portafirmas = ResourceBundle.getBundle("Portafirmas");
                PORTAFIRMAS  = portafirmas.getString(munic+"/Portafirmas");
            }catch(Exception e){
              PORTAFIRMAS = "";
            }
              
        %>
         <!-- Estilos -->
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/gestionTerceros.js"></script>
        
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>" media="screen" >
        
        <SCRIPT type="text/javascript"> 
            var cod_idioma = new Array();
            var desc_idioma = new Array();
            var listaOrganizaciones = new Array();
            var listaOrganizacionesOriginal = new Array();
            var listaGrupos = new Array();
            var listaGruposOriginal = new Array();
            var listaUnidOrganicas = new Array();
            var listaUnidOrganicasOriginal = new Array();
            var cont = 0;
            var cont1 = 0;
            var cont2 = 0;
            var listaUsuariosTodos = new Array();
            var loginOriginal = "";
            var listaCodDirectivas = new Array();
            var CAMBIO_PASSWORD_OBLIGATORIO_ALTA = "<%=(String)request.getAttribute("OBLIGATORIO_CAMBIO_PASSWORD")%>";
            
            var longitudMinima = "<%=(String)request.getAttribute("RESTRICCION_LONGITUD_PASSWORD")%>";

            var MODO_CARGA_PANTALLA= "";
            

            // lista con las unidades organicas dependientes
            var unidadesDependientesUOR = new Array();
            var unidadesDependientesUORTemporal = new Array();
            
            <% Vector relacionUsuarios = (Vector) session.getAttribute("RelacionUsuarios");
            if (relacionUsuarios != null) {
                int j = 0;
                for (int i = 0; i < relacionUsuarios.size(); i++) {
                    GeneralValueObject g = (GeneralValueObject) relacionUsuarios.elementAt(i);
                    String l = (String) g.getAtributo("login");
%>
    listaUsuariosTodos[<%= j++ %>]  = ['<%= l %>'];
        <% 	   }
            }
%> 
    
    function inicializar() {
        var argVentana = self.parent.opener.xanelaAuxiliarArgs;
        if(argVentana != null ) {
            loginOriginal = argVentana[0];
        }
        tp1.setSelectedIndex(0);
        tabOrganizaciones.readOnly=true;
        <logic:iterate id="elemento" name="UsuariosGruposForm" property="listaOrganizaciones">
        var aut = '<bean:write name="elemento" property="autorizacion" />';
        var codOrg = '<bean:write name="elemento" property="codOrganizacion" />';
        var checkEntreg1 = "<input type='checkbox' class='check' name='box" + cont + "' value='SI' onclick='compruebaOrganizaciones(" + codOrg + "," + cont + ");' ";
        if(aut == "si") {
            checkEntreg1 += "checked ";
        }
        checkEntreg1 += ">";
        
        listaOrganizaciones[cont] = [checkEntreg1,
            '<bean:write name="elemento" property="nombreOrganizacion" />'];
            
            listaOrganizacionesOriginal[cont] =[checkEntreg1,
                '<bean:write name="elemento" property="nombreOrganizacion" />',
                '<bean:write name="elemento" property="codOrganizacion" />',
                '<bean:write name="elemento" property="autorizacion" />'];
                cont++;
                </logic:iterate>
                
                tabOrganizaciones.lineas = listaOrganizaciones;
                refrescaOrganizaciones();
				
				
					<logic:iterate id="elemento" name="UsuariosGruposForm" property="listaGrupos">
					listaGrupos[cont1] = ['<bean:write name="elemento" property="nombreGrupo" />',
						'<bean:write name="elemento" property="nombreOrganizacion" />',
						'<bean:write name="elemento" property="nombreEntidad" />',
						'<bean:write name="elemento" property="nombreAplicacion" />'];
						listaGruposOriginal[cont1] =['<bean:write name="elemento" property="codOrganizacion" />',
							'<bean:write name="elemento" property="codEntidad" />',
							'<bean:write name="elemento" property="codAplicacion" />',
							'<bean:write name="elemento" property="codGrupo" />'];
							cont1++;
					</logic:iterate>
                        
                        tabGrupos.lineas = listaGrupos;
                        refrescaGrupos();
                        
                        <logic:iterate id="elemento" name="UsuariosGruposForm" property="listaUnidOrganicas">
                        listaUnidOrganicas[cont2] = ['<bean:write name="elemento" property="nombreUnidOrganica" />',
                            '<bean:write name="elemento" property="nombreOrganizacion" />',
                            '<bean:write name="elemento" property="nombreEntidad" />',
                            '<bean:write name="elemento" property="nombreCargo" />'];
                            listaUnidOrganicasOriginal[cont2] =['<bean:write name="elemento" property="codOrganizacion" />',
                                '<bean:write name="elemento" property="codEntidad" />',
                                '<bean:write name="elemento" property="codUnidOrganica" />',
                                '<bean:write name="elemento" property="codCargo" />',
                                '<bean:write name="elemento" property="codUnidadOrganicaPadre" />'];
                                
                                cont2++;
                                </logic:iterate>
                                
                                
                                tabUnidOrganicas.lineas = listaUnidOrganicas;
                                refrescaUnidOrganicas();
                                
         <%
            String modo = "";
            modo = (String) session.getAttribute("modo");
            
            session.removeAttribute("modo");
            Log m_log = LogFactory.getLog(this.getClass().getName());
            if (m_log.isDebugEnabled()) {
                m_log.debug("El modo es: " + modo);
            }

            UsuariosGruposForm bForm = (UsuariosGruposForm) session.getAttribute("UsuariosGruposForm");
            Vector listaIdiomas = bForm.getListaIdiomas();
            int lengthIdiomas = listaIdiomas.size();
            int m = 0;
            
         %>
             MODO_CARGA_PANTALLA = "<%=modo%>";
             var n=0;
         <%for (m = 0; m < lengthIdiomas; m++) {
                GeneralValueObject idiomas = (GeneralValueObject) listaIdiomas.get(m);%>
                    cod_idioma[n] = '<%=(String) idiomas.getAtributo("codigo")%>';
                    desc_idioma[n] = '<%=(String) idiomas.getAtributo("descripcion")%>';
                    n++;
                    <%}%>
                    
                    comboIdioma.addItems(cod_idioma,desc_idioma);
                    
                    <%UsuariosGruposValueObject uVO = new UsuariosGruposValueObject();
            uVO = bForm.getUsuariosGrupos();
            if (uVO.getFicheroFirmaFisico() == null) {%>
                    deshabilitarBoton(document.forms[0].cmdVisualizarFichero);
                    deshabilitarBoton(document.forms[0].cmdEliminarFichero);
                    <%} else {%>
                    document.forms[0].fichero.value="Firma";
                    <%}%>
        
        // Directivas: ocultar divs de las aplicaciones con directivas y 
        // guardar lista de codigos de las directivas.
        ocultarDivsAplDirectivas();
        var i = 0;
		
		<logic:iterate id="directiva" name="UsuariosGruposForm" property="directivas">
			listaCodDirectivas[i] = '<bean:write name="directiva" property="codigo"/>';
			i++;
		</logic:iterate>
        
        deshabilitarSiEliminado();
        habilitarCambioPasswordObligatoriaAltaUsuario();
        habilitarDeshabilitarBuzonFirma();     

    }
    
    function habilitarDeshabilitarBuzonFirma() {
       var portafirmas = "<%=PORTAFIRMAS%>";
     
       if (""!=portafirmas && "LAN"==portafirmas) {
            var firmante = document.forms[0].firmante.value;
            var inputBuzonFirma = document.forms[0].buzonFirma;

            if (firmante == 0) {
                inputBuzonFirma.setAttribute('disabled', true); 
            } else if (firmante == 1 && (inputBuzonFirma.getAttribute('disabled')!= null || inputBuzonFirma.getAttribute('disabled')!= "")) {
                inputBuzonFirma.removeAttribute('disabled'); 
            }
       }
    }


	function habilitarCambioPasswordObligatoriaAltaUsuario(){
		if(CAMBIO_PASSWORD_OBLIGATORIO_ALTA=="SI" && MODO_CARGA_PANTALLA=="alta"){
			document.forms[0].cambioPantallaObligatorio.checked = true;
		}
	}
	
                // Esta funcion comprueba si hay algun Grupo o Unidad Organica relacionada
                // con la organizacion que se desmarca y muestra un mensaje de aviso
                // antes de borrar dichos Grupos y Unidades Organicas
                function compruebaOrganizaciones(codigo, cont){  
                    var i,j;
                    if (eval("document.forms[0].box" +  cont + ".checked == false")){
                        if(jsp_alerta('', '<%=descriptor.getDescripcion("msjBorrarOrg")%>')) {
                            
                            // Comprobacion en Grupos
                            var auxGrupos = new Array();
                            var auxGOrig = new Array();
                            j=0;
                            for (i=0; i<listaGruposOriginal.length; i++){
                                if (codigo != listaGruposOriginal[i][0]){
                                    auxGrupos[j] = listaGrupos[i];
                                    auxGOrig[j] = listaGruposOriginal[i]
                                    j++;
                                }
                            }
                            listaGruposOriginal = auxGOrig;
                            listaGrupos = auxGrupos;
                            tabGrupos.lineas = auxGrupos;
                            refrescaGrupos();
                            
                            // Comprobacion en Unidades Organicas
                            var auxUnidOrganicas = new Array();
                            var auxUOrig = new Array();
                            j=0;
                            for (i=0; i<listaUnidOrganicasOriginal.length; i++){
                                if (codigo != listaUnidOrganicasOriginal[i][0]){
                                    auxUnidOrganicas[j] = listaUnidOrganicas[i];
                                    auxUOrig[j] = listaUnidOrganicasOriginal[i];
                                    j++;
                                }   
                            }
                            listaUnidOrganicasOriginal = auxUOrig;
                            listaUnidOrganicas = auxUnidOrganicas;
                            tabUnidOrganicas.lineas = auxUnidOrganicas;
                            refrescaUnidOrganicas();
                        }
                        else eval("document.forms[0].box" +  cont + ".checked = true")
                        } 	
                    }
                    
                    
                    function pulsarSalir() {
                        self.parent.opener.retornoXanelaAuxiliar();
                    }
                    
                    var JS_DEBUG_LEVEL = 50;
                    
                    function validarNifObligatorio() {
                        if (JS_DEBUG_LEVEL >= 80) alert("datosUsuarios.validarNifObligatorio() BEGIN");
                        var edtNif = document.forms[0].nif;
                        var cmbFirmante = document.forms[0].firmante;
                        if (edtNif && cmbFirmante && (cmbFirmante.selectedIndex >= 0) ) {
                            var nif = edtNif.value;
                            var optionFirmante = cmbFirmante.options[cmbFirmante.selectedIndex];
                            var firmante = ( (optionFirmante!=null) ? (optionFirmante.value) : ('0') );
                            if (JS_DEBUG_LEVEL >= 80) alert("datosUsuarios.validarNifObligatorio() nif="+nif+";firmante="+firmante+";");
                            if (firmante == '1') {
                                var result = ( (nif!=null) && (nif.length > 0 ) );
                                if (!result) {
                                    jsp_alerta("A","Debe rellenar el NIF o CIF para los usuarios con capacidad de firma");
                                } else {
                                result = (validarNif(edtNif.value,0)||validarCIF(edtNif.value)||validarNie(edtNif));
                            }
                            return result;
                        } else {
                        if ( (nif!=null) && (nif.length > 0 ) ) return (validarNif(edtNif.value,0)||validarCIF(edtNif.value)||validarNie(edtNif)); else return true;
                    }
                } else {
                if (JS_DEBUG_LEVEL >= 80) alert("datosUsuarios.validarNifObligatorio() nif="+edtNif+";firmante="+cmbFirmante+";");
            }
            return true;
        }

function validarNif(abc) {
    dni=abc.substring(0,abc.length-1);
    let=abc.charAt(abc.length-1);
    if (!isNaN(let)) {
        return false;
    } else {
        cadena="TRWAGMYFPDXBNJZSQVHLCKET";
        posicion = dni % 23;
        letra = cadena.substring(posicion,posicion+1);
        if (letra!=let.toUpperCase()) {
            return false;
        }
    }
    return true;
}



/********/

/** 
    Devuelve un objeto XMLHttpRequest
   */
   function getXMLHttpRequest(){
        var aVersions = [ "MSXML2.XMLHttp.5.0",
                "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
                "MSXML2.XMLHttp","Microsoft.XMLHttp"
        ];

        if (window.XMLHttpRequest){
                // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
                return new XMLHttpRequest();
        }else if (window.ActiveXObject){
                // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
                for (var i = 0; i < aVersions.length; i++) {
                        try {
                            var oXmlHttp = new ActiveXObject(aVersions[i]);
                            return oXmlHttp;
                        }catch (error) {
                        //no necesitamos hacer nada especial
                        }
                }// for
        }// if
    }// function
    
    function verificarAsignacionPermisosCorrecta(){
        
        var exito = true;
        var ajax = getXMLHttpRequest();
        var url  = "<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
        var listaUnidades = document.forms[0].lUnidOrganicas.value;
        var listaOrganizaciones=document.forms[0].lOrganizacionesUOR.value;
        var listaEntidades=document.forms[0].lEntidadesUOR.value;
                        
        var parametros = "opcion=verificarAsignacionPermisosCorrecta&listaUnidades=" + escape(listaUnidades)+"&listaOrganizaciones=" + escape(listaOrganizaciones)+"&listaEntidades=" + escape(listaEntidades);        
        ajax.open("POST",url,false);
        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
        ajax.send(parametros);
        
        try{
            if (ajax.readyState==4 && ajax.status==200){                
                var xmlDoc = null;
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    // En IE el XML viene en responseText y no en la propiedad responseXML
                        var text = ajax.responseText;                               
                        xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
                        xmlDoc.async="false";
                        xmlDoc.loadXML(text);

                }else{
                    // En el resto de navegadores el XML se recupera de la propiedad responseXML
                    xmlDoc = ajax.responseXML;                
                }


                nodos = xmlDoc.getElementsByTagName("SALIDA_CUENTA_USUARIO");
                var elemento = nodos[0];
                
                var hijos = elemento.childNodes;
                var codigoOperacion = null;

                    var status = "";
                    var codVisiblePadre = "";
                    var descUorPadre = "";
                    var descError = "";
                    var oficinas = new Array();
                    
                    /*****/
                    var codError = "";
                    var errores = new Array();                    
                    /*****/
                    
                    for(j=0;hijos!=null && j<hijos.length;j++){
                                        
                        /******/                
                        if(hijos[j].nodeName=="CODIGO_ERROR"){                            
                            codError = hijos[j].childNodes[0].nodeValue;                            
                        }                
                        
                        if(hijos[j].nodeName=="ERRORES"){                            
                            var listaErrores = hijos[j].childNodes;                            
                            
                            for(z=0;listaErrores!=null && z<listaErrores.length;z++){                                
                                var unError = listaErrores[z]
                                
                                 if(unError.nodeName=="ERROR"){                                     
                                     var datosError = unError.childNodes;
                                     var contenidoError = "";
                                     var contenidoOficinas = "";
                                     for(k=0;k<datosError.length;k++){                                     
                                         if(datosError[k].nodeName=="STATUS"){                                             
                                             contenidoError = contenidoError + datosError[k].childNodes[0].nodeValue + "#";
                                         }
                                         
                                         if(datosError[k].nodeName=="PADRE_COD_VISIBLE"){                                             
                                             contenidoError = contenidoError + datosError[k].childNodes[0].nodeValue + "#";
                                         }
                                                                                  
                                         if(datosError[k].nodeName=="PADRE_DESCRIPCION"){                                             
                                             contenidoError = contenidoError + datosError[k].childNodes[0].nodeValue;
                                         }
                                         
                                         if(datosError[k].nodeName=="OFICINAS"){
                                             var listaOficinas = datosError[k].childNodes;
                                             for(y=0;listaOficinas!=null && y<listaOficinas.length;y++){
                                                 
                                                  if(listaOficinas[y].nodeName=="OFICINA"){                            
                                                        var valor = listaOficinas[y].childNodes[0].nodeValue;
                                                        if(valor!=null && valor!=""){
                                                            var datos = valor.split("#");

                                                            if(datos!=null && datos.length==2){
                                                                contenidoOficinas = contenidoOficinas + "(" + datos[0] + ") " + datos[1] + ";";
                                                            }
                                                        }//if
                                                  }
                                             }                                                                                          
                                         }
                                         
                                     }// for                     
                                     
                                     errores[z] = contenidoError;                                     
                                     oficinas[z] = contenidoOficinas;
                                     
                                 }// if                                
                            }
                        }              
                        
                    }// for
                    
                    
                    var mensaje = "";
                    for(i=0;i<errores.length;i++){
                        
                        var datos = errores[i].split("#");
                        var status = datos[0];
                        var codVisibleUorPadre = datos[1];
                        var descUorPadre = datos[2];
                        
                        if(status!="" && status=="0"){                       
                            exito = true;
                        }else
                        if(status!="" && status=="1"){
                            // SE HA ASIGNADO PERMISO SOBRE UNA UNIDAD DE REGISTRO CON UNIDADES HIJAS (OFICINAS DE REGISTRO) Y NO SE HA DADO 
                            // PERMISO AL USUARIO SOBRE UNA DE LAS OFICINAS DE REGISTRO HIJAS.
                            mensaje = mensaje + "<p style=\"font-size:14px;\">"
                            mensaje = mensaje + "<%=descriptor.getDescripcion("msgErrUorRegSinOfiReg1") %>" + "  " + codVisibleUorPadre + "-" + descUorPadre + " " + "<%=descriptor.getDescripcion("msgErrUorRegConOfiReg2")%>";
                            mensaje = mensaje + "</p>";
                            mensaje = mensaje + "<p style=\"font-size:11px;margin-left:9px;\">";                            
                            var oficina = oficinas[i].split(";");         
                            var texto = "";
                            for(k=0;oficina!=null && k<oficina.length;k++){                                                                
                                texto = texto + oficina[k];
                                if(oficina.length-k>1) texto = texto + ",";                                
                            }
                            mensaje =  mensaje + texto + "</p>";                                                                                        
                            exito = false;
                        }else
                        if(status!="" && status=="2"){
                            // SE HA ASIGNADO PERMISO SOBRE UNA UNIDAD DE REGISTRO CON UNIDADES HIJAS (OFICINAS DE REGISTRO) Y NO SE HA DADO 
                            // PERMISO AL USUARIO SOBRE UNA DE LAS OFICINAS DE REGISTRO HIJAS.                            
                            mensaje = mensaje + "<p style=\"font-size:14px;\">"
                            mensaje = mensaje + "<%=descriptor.getDescripcion("msgErrUorRegSinOfiReg1") %>" + "  " + codVisibleUorPadre + "-" + descUorPadre + " " + "<%=descriptor.getDescripcion("msgErrUorRegSinOfiReg2")%>";                                                       
                            mensaje = mensaje + "</p>";
                            mensaje = mensaje + "<p style=\"font-size:11px;;margin-left:9px;\">";                            
                            var oficina = oficinas[i].split(";");                                  
                            
                            var texto = "";
                            for(k=0;oficina!=null && k<oficina.length;k++){                             
                                texto = texto + oficina[k];
                                if(oficina.length-k>1) texto = texto + ",";                             
                            }
                            mensaje =  mensaje + texto + "</p>";                                                                                                                    
                            exito = false;
                        }else
                        if(status!="" && status=="3"){
                            // SE HA ASIGNADO PERMISO SOBRE UNA OFICINA DE REGISTRO PERO NO SOBRE LA UNIDAD DE REGISTRO PADRE                            
                            mensaje = mensaje + "<p style=\"font-size:14px;\">"
                            mensaje = mensaje + "<%=descriptor.getDescripcion("msgErrOfiRegSinUorReg1")%> " + "  (" + codVisibleUorPadre + ") " + descUorPadre + " " + " <%=descriptor.getDescripcion("msgErrOfiRegSinUorReg2")%>";
                            mensaje = mensaje + "</p>";
                            mensaje = mensaje + "<p style=\"font-size:11px;;margin-left:9px;\">";                            
                            var oficina = oficinas[i].split(";");                                
                            
                            var texto = "";
                            for(k=0;oficina!=null && k<oficina.length;k++){                                
                                texto = texto + oficina[k];
                                if(oficina.length-k>1) texto = texto + ",";                                
                            }                            
                            mensaje =  mensaje + texto + "</p>";                                                                                        
                            exito = false;
                        }                            
                    }
                    
                    if(!exito) jspAlerta3("A",mensaje);
            }
        }catch(err){
            alert("Error.descripcion: " + err.description);
        }
        
        return exito;
    }

/********/        


        function pulsarAceptar () { 
            var desc = document.forms[0].login.value;
            var yaExiste = 0;
            for(l=0; l < listaUsuariosTodos.length; l++){
                if(loginOriginal != "") {
                    if ((listaUsuariosTodos[l][0]).toUpperCase() == desc.toUpperCase() && loginOriginal.toUpperCase() != desc.toUpperCase()){
                        yaExiste = 1;
                    }
                } else {
                if ((listaUsuariosTodos[l][0]).toUpperCase() == desc.toUpperCase()){
                    yaExiste = 1;
                }
                }
            }
            
            document.forms[0].listaDirectivasTxt.value = crearListaDirectivasChecked();
        
            var contrasena = document.forms[0].contrasena.value;
            var contrasena2 = document.forms[0].contrasena2.value;
            if (contrasena == contrasena2) {           
            if(validarPassword(contrasena) && validarBuzonFirma()) {
                if( validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
                    if (validarNifObligatorio()&&validarEmails()) {
                        var listasOrg = crearListaOrganizacionesUsuarios();
                        document.forms[0].listaOrgan.value = listasOrg;
                        var listasGrupos = crearListasGrupos();
                        document.forms[0].lOrganizaciones.value = listasGrupos[0];
                        document.forms[0].lEntidades.value = listasGrupos[1];
                        document.forms[0].lAplicaciones.value = listasGrupos[2];
                        document.forms[0].lGrupos.value = listasGrupos[3];
                        var listasUnidOrganicas = crearListasUnidOrganicas();
                        document.forms[0].lOrganizacionesUOR.value = listasUnidOrganicas[0];
                        document.forms[0].lEntidadesUOR.value = listasUnidOrganicas[1];
                        document.forms[0].lUnidOrganicas.value = listasUnidOrganicas[2];
                        document.forms[0].lCargosUOR.value = listasUnidOrganicas[3];
						
                        <% if ("alta".equals(modo)) {%>
                            var exito = verificarAsignacionPermisosCorrecta();
                            if(exito){                            
                                // Al estar en el modo de alta de usuario, se comprueba si en la configuración
                                // se ha indicado si es obligatorio que esté marcado el check de "Cambio de contraseña
                                // obligatorio próximo acceso"

                                var continuar = true;
                                if(CAMBIO_PASSWORD_OBLIGATORIO_ALTA=="SI" && !document.forms[0].cambioPantallaObligatorio.checked)
                                    continuar = false;


                                if(continuar){
                                    document.forms[0].opcion.value="insertarUsuario"; 
                                    document.forms[0].target="oculto";
                                    document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
                                    document.forms[0].submit();
                                }else
                                    jsp_alerta('A','<%=descriptor.getDescripcion("msgErrCheckPasswdObligatorio")%>');
                        }
                        <%} else {%>
                        if(yaExiste == 0 ) {
                            var exito = verificarAsignacionPermisosCorrecta();
                            if(exito){
                                document.forms[0].opcion.value="modificarUsuario";
                                document.forms[0].target="oculto";
                                document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
                                document.forms[0].submit();
                            }
                        } else {
                        jsp_alerta('A','<%=descriptor.getDescripcion("msjLoginExist")%>');
                        }
                        <% }%>
                    }else{
                        if(!validarNifObligatorio()){
                            jsp_alerta('A','<%=descriptor.getDescripcion("nifNoVal")%>');
                        }
                    }
                }
            }
        } else {
        jsp_alerta('A','<%=descriptor.getDescripcion("msjContrDif")%>');
        }
    }
    
    function validarBuzonFirma() {
    
        var portafirmas = "<%=PORTAFIRMAS%>";
     
       if (""!=portafirmas && "LAN"==portafirmas) { 
           var firmante = document.forms[0].firmante.value;
           var inputBuzonFirma = document.forms[0].buzonFirma.value;
            if (firmante==1 && inputBuzonFirma=="") {
                jsp_alerta('A','<%=descriptor.getDescripcion("msgValBuzonFirma")%>');
                return false;
            }
        }
        
        return true;
    }
    
    function devolver () {
        var retorno = new Array();
        self.parent.opener.retornoXanelaAuxiliar(retorno);
    }
    
    // Crea una lista separada por §¥ de las directivas que estan marcadas
    function crearListaDirectivasChecked() {
        var listaDirectivasTxt = '';
        for(var i=0; i<listaCodDirectivas.length; i++) {
            if (document.getElementById('checkDirectiva' + listaCodDirectivas[i]).checked) {
                listaDirectivasTxt += listaCodDirectivas[i] + '§¥';
            }
        }
        return listaDirectivasTxt;
    }
    
    function crearListaOrganizacionesUsuarios(escapar) {
        var listaOrg = "";
        var separador = '§¥';
        if(escapar && escapar=='si') separador = escape(separador);
        for (i=0; i < listaOrganizaciones.length; i++) {
            var caja = "box" + i;
            if(eval("document.forms[0]." + caja + ".checked") == true) {
                listaOrg +=listaOrganizacionesOriginal[i][2]+separador;
            }
        }
        return listaOrg;
    }
    
    function callFromTableTo(rowID,tableName){
        if(tabGrupos.id == tableName){
            //verGrupos(rowID);
        }
        if(tabUnidOrganicas.id == tableName){
            //verUnidadesOrganicas(rowID);
        }
    }
    
    // JavaScript de la tabla Grupos
    
    function verGrupos(rowID) {
        var datosAEnviar = new Array();
        datosAEnviar[0] = listaGruposOriginal[rowID][0];
        datosAEnviar[1] = listaGruposOriginal[rowID][1];
        datosAEnviar[2] = listaGruposOriginal[rowID][2];
        datosAEnviar[3] = listaGruposOriginal[rowID][3];
        datosAEnviar[4] = listaGrupos[rowID][0];
        datosAEnviar[5] = listaGrupos[rowID][1];
        datosAEnviar[6] = listaGrupos[rowID][2];
        datosAEnviar[7] = listaGrupos[rowID][3];
        var source = "<%=request.getContextPath()%>/administracion/UsuariosGrupos.do?opcion=verGrupos&primero="+crearListaOrganizacionesUsuarios();
        abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/administracion/mainVentana.jsp?source="+source,datosAEnviar,
	'width=600,height=390,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                            modificarGrupo(datos);
                        }
                  });
        }
        
        function altaGrupo(datos) {
            var yaExiste = 0;
            for(l=0; l < listaGrupos.length; l++){
                if (listaGruposOriginal[l][0] == datos[0] && listaGruposOriginal[l][1] == datos[2] && listaGruposOriginal[l][2] == datos[4] && listaGruposOriginal[l][3] == datos[6]){
                    yaExiste = 1;
                    break;
                }
            }
            if(yaExiste == 0) {
                var lineas = tabGrupos.lineas;
                for (i=0; i < lineas.length; i++) {
                }
                listaGrupos[i]=[datos[7],datos[1],datos[3],datos[5]];
                listaGruposOriginal[i]=[datos[0],datos[2],datos[4],datos[6]];
                tabGrupos.lineas=listaGrupos;
                refrescaGrupos();
            } else {
            var mensaje = listaGrupos[l][0] + "," + listaGrupos[l][1] + "," + listaGrupos[l][2] + "," +
            listaGrupos[l][3] + " : " + '<%=descriptor.getDescripcion("msjExGrup")%>';
            jspAlerta('A',mensaje);
        }
    }
    
    function modificarGrupo(datos) {
        var yaExiste = 0;
        for(l=0; l < listaGrupos.length; l++){
            var lineaSeleccionada;
            lineaSeleccionada = tabGrupos.selectedIndex;
            if(l == lineaSeleccionada) {
                l= l;
            } else {
            if (listaGruposOriginal[l][0] == datos[0] && listaGruposOriginal[l][1] == datos[2] && listaGruposOriginal[l][2] == datos[4] && listaGruposOriginal[l][3] == datos[6] ){
                yaExiste = 1;
            }
        }
    }
    if(yaExiste == 0) {
        var j = tabGrupos.selectedIndex;
        listaGrupos[j]=[datos[7],datos[1],datos[3],datos[5]];
        listaGruposOriginal[j]=[datos[0],datos[2],datos[4],datos[6]];
        tabGrupos.lineas=listaGrupos;
        refrescaGrupos();
    } else {
    jsp_alerta('A','<%=descriptor.getDescripcion("msjExGrup")%>');
    }
}

function pulsarEliminarGrupo() {
    if(tabGrupos.selectedIndex != -1) {
        if(jsp_alerta('', '<%=descriptor.getDescripcion("msjBorrarGrp")%>')) {
            var list = new Array();
            var listOrig = new Array();
            tamIndex=tabGrupos.selectedIndex;
            tamLength=tabGrupos.lineas.length;
            for (i=tamIndex - 1; i < listaGrupos.length - 1; i++){
                if (i + 1 <= listaGrupos.length - 2){
                    listaGrupos[i + 1]=listaGrupos[i + 2];
                    listaGruposOriginal[i + 1]=listaGruposOriginal[i + 2];
                }
            }
            for(j=0; j<listaGrupos.length-1 ; j++){
                list[j] = listaGrupos[j];
                listOrig[j] = listaGruposOriginal[j];
            }
            tabGrupos.lineas=list;
            refrescaGrupos();
            listaGrupos=list;
            listaGruposOriginal = listOrig;
        }
    } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }
    
    
    function pulsarAltaGrupo() {
        var datosAEnviar = new Array();
        datosAEnviar[0] = "";
        datosAEnviar[1] = "";
        datosAEnviar[2] = "";
        datosAEnviar[3] = "";
        datosAEnviar[4] = "";
        datosAEnviar[5] = "";
        datosAEnviar[6] = "";
        datosAEnviar[7] = "";
        var source = "<%=request.getContextPath()%>/administracion/UsuariosGrupos.do?opcion=verGrupos&primero="+crearListaOrganizacionesUsuarios('si');
        abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/administracion/mainVentana.jsp?source="+source,datosAEnviar,
	'width=790,height=450,status='+ '<%=statusBar%>',function(datos){
            if(datos!=undefined){
                if(datos[4].length <= 3) {
                    altaGrupo(datos);
                } else {
                    procesarDatosGrupo(datos);
                }
            }
        });
    }
    
    function procesarDatosGrupo(d) {
        var lista = new Array();
        lista[0] = d[0];
        lista[1] = d[1];
        lista[2] = d[2];
        lista[3] = d[3];
        for(var i=0;i<d[4].length;i++) {
            lista[4] = d[4][i];
            lista[5] = d[5][i];
            if(d[4][i] != 999) {
                lista[6] = d[6];
                lista[7] = d[7];
                altaGrupo(lista);
            }
        }
    }
    
    function crearListasGrupos() {
        var lOrganizaciones = "";
        var lEntidades = "";
        var lAplicaciones = "";
        var lGrupos ="";
        for (i=0; i < listaGrupos.length; i++) {
            lOrganizaciones +=listaGruposOriginal[i][0]+'§¥';
            lEntidades +=listaGruposOriginal[i][1]+'§¥';
            lAplicaciones +=listaGruposOriginal[i][2]+'§¥';
            lGrupos +=listaGruposOriginal[i][3]+'§¥';
        }
        var listas = new Array();
        listas = [lOrganizaciones,lEntidades,lAplicaciones,lGrupos];
        return listas;
    }
    
    // Fin del JavaScript de la tabla Grupos
    
    // JavaScript de la tabla Unidades Organicas
    
    function verUnidadesOrganicas(rowID) {
        var datosAEnviar = new Array();
        datosAEnviar[0] = listaUnidOrganicasOriginal[rowID][0];
        datosAEnviar[1] = listaUnidOrganicasOriginal[rowID][1];
        datosAEnviar[2] = listaUnidOrganicasOriginal[rowID][2];
        datosAEnviar[3] = listaUnidOrganicas[rowID][0];
        datosAEnviar[4] = listaUnidOrganicas[rowID][1];
        datosAEnviar[5] = listaUnidOrganicas[rowID][2];
        var source = "<%=request.getContextPath()%>/administracion/UsuariosGrupos.do?opcion=verUnidOrganicas&primero="+crearListaOrganizacionesUsuarios();
        abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/administracion/mainVentana.jsp?source="+source,datosAEnviar,
	'width=600,height=390,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                            modificarUnidOrganica(datos);
                        }
                  });
    }
        
    function altaUnidOrganica(datos) {
        if(!existeUOR(datos)) {
            if (datos[10] == true && datos[4] != -1 && datos[11] == true){ // Añadimos la uor seleccionada y sus unidades dependientes (datos[11] indica si la uor seleccionada tiene o no descendencia)
                var codUors = datos[8];     // lista con los codigos  de las uors que debemos insertar
                var descUors = datos[9];    // lista con los nombres  de las uors que debemos insertar
                var padresUors = datos[13]; // lista con los padres de las uors que debemos insertar
                var listaCodUors = codUors.split('%$');
                var listaDescUors = descUors.split('%$');
                var listaPadresUors = padresUors.split('%$');

                for(var i=0; i < listaCodUors.length; i++){
                    datos[4] = listaCodUors[i];
                    datos[5] = listaDescUors[i];
                    datos[13] = listaPadresUors[i];
                    if (datos[13]=='-1') datos[13]= '';
                    if (!existeUOR(datos)){     // Si no existe la damos de alta
                        var lineas = tabUnidOrganicas.lineas;
                        for (j=0; j < lineas.length; j++) {
                        }
                        
                        listaUnidOrganicas[j]=[datos[5],datos[1],datos[3],datos[7]];
                        listaUnidOrganicasOriginal[j]=[datos[0],datos[2],datos[4],datos[6], datos[13] ];                       
                        tabUnidOrganicas.lineas=listaUnidOrganicas;
                        refrescaUnidOrganicas();
                    }
                }

            }else{      // Añadimos la uor seleccionada solamente
                var lineas = tabUnidOrganicas.lineas;
                for (i=0; i < lineas.length; i++) {
                }
                listaUnidOrganicas[i]=[datos[5],datos[1],datos[3],datos[7]];
                if (datos[13]=='-1') datos[13]= '';
                listaUnidOrganicasOriginal[i]=[datos[0],datos[2],datos[4],datos[6], datos[13]];               
                tabUnidOrganicas.lineas=listaUnidOrganicas;
                refrescaUnidOrganicas();
            }

        } else {
            var mensaje = listaUnidOrganicas[l][0] + "," + listaUnidOrganicas[l][1] + "," +
                listaUnidOrganicas[l][2] + " : " + '<%=descriptor.getDescripcion("msjExUOR")%>';
            jsp_alerta('A',mensaje);
        }
    }

    function existeUOR(datos){
        var yaExiste = false;
        for(l=0; l < listaUnidOrganicas.length; l++){
            if ((listaUnidOrganicasOriginal[l][0] == datos[0] && listaUnidOrganicasOriginal[l][1] == datos[2] && listaUnidOrganicasOriginal[l][2] == datos[4]) ||
                (listaUnidOrganicasOriginal[l][0] == datos[0] && listaUnidOrganicasOriginal[l][1] == datos[2] && listaUnidOrganicasOriginal[l][2] == -1)){
                yaExiste = true;
                break;
            }
        }
        return yaExiste;
    }


    function modificarUnidOrganica(datos) {
        var yaExiste = 0;
        for(l=0; l < listaUnidOrganicas.length; l++){
            var lineaSeleccionada;
            lineaSeleccionada = tabUnidOrganicas.selectedIndex;
            if(l == lineaSeleccionada) {
                l= l;
            } else {
            if (listaUnidOrganicasOriginal[l][0] == datos[0] && listaUnidOrganicasOriginal[l][1] == datos[2] && listaUnidOrganicasOriginal[l][2] == datos[4]){
                yaExiste = 1;
            }
        }
    }
    if(yaExiste == 0) {
        var j = tabUnidOrganicas.selectedIndex;
        listaUnidOrganicas[j]=[datos[5],datos[1],datos[3]];
        listaUnidOrganicasOriginal[j]=[datos[0],datos[2],datos[4]];
        tabUnidOrganicas.lineas=listaUnidOrganicas;
        refrescaUnidOrganicas();
    } else {
    jsp_alerta('A','<%=descriptor.getDescripcion("msjExUOR")%>');
    }
}


    function pulsarEliminarUnidOrganica() {
        if(tabUnidOrganicas.selectedIndex != -1) {
            if(jsp_alerta('', '<%=descriptor.getDescripcion("msjBorrarUOR")%>')) {
                    eliminarUnidadOrganica();
            }
        } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }


    // Elimina una unidad organica que tiene asociado el usuario.
    // Se tendra en cuenta si en la eliminacion de la unidad se quiere tambien
    // la eliminacion de las demas unidades dependientes.
    function eliminarUnidadOrganica(){
        if(jsp_alerta('', '<%=descriptor.getDescripcion("msjBorrarUORCascada")%>')) {
            // borramos todas las unidades organicas dependientes (o sea todas aquellas descendientes)
            eliminarUnidadesOrganicasDescendientes(tabUnidOrganicas.selectedIndex);

        }else{
            eliminarUnidadOrganicaSeleccionada(tabUnidOrganicas.selectedIndex);
        }
    }


    function eliminarUnidadOrganicaSeleccionada(tamIndece){
            var list = new Array();
            var listOrig = new Array();
            tamLength=tabUnidOrganicas.lineas.length;
        for (i=tamIndece - 1; i < listaUnidOrganicas.length - 1; i++){
                if (i + 1 <= listaUnidOrganicas.length - 2){
                    listaUnidOrganicas[i + 1]=listaUnidOrganicas[i + 2];
                    listaUnidOrganicasOriginal[i + 1]=listaUnidOrganicasOriginal[i + 2];
                }
            }
            for(j=0; j<listaUnidOrganicas.length-1 ; j++){
                list[j] = listaUnidOrganicas[j];
                listOrig[j] = listaUnidOrganicasOriginal[j];
            }
            tabUnidOrganicas.lineas=list;
            refrescaUnidOrganicas();
            listaUnidOrganicas=list;
            listaUnidOrganicasOriginal = listOrig;
        }
       
    // Funcion que elimina tanto la unidad seleccionada como todas sus unidades descendientes.
    function eliminarUnidadesOrganicasDescendientes(indice){      
        var codUorSeleccionada = listaUnidOrganicasOriginal[indice][2];
        var codOrgUorSeleccionada = listaUnidOrganicasOriginal[indice][0];
       
        // miramos si la uor seleccionada tiene descendencia
        var tieneDescendencia = uorTieneDescendencia(codUorSeleccionada,codOrgUorSeleccionada);
        eliminarUnidadOrganicaSeleccionada(tabUnidOrganicas.selectedIndex);
        if (tieneDescendencia){
            var list = new Array();
            var listOrig = new Array();

            for (var k =0; k < unidadesDependientesUOR.length; k++) {
                tamIndex=obtenerIndiceFila(unidadesDependientesUOR[k]);
            tamLength=tabUnidOrganicas.lineas.length;

                if (tamIndex !=-1){
                    // debo marcar la fila
                    seleccionarFila(tamIndex,tabUnidOrganicas);
                    eliminarUnidadOrganicaSeleccionada(tabUnidOrganicas.selectedIndex);
                }
            }
            // despuues de borrar, inicializamos el array unidadesDependientesUOR
            unidadesDependientesUOR = new Array();            
        }
    }

    // Funcion que recorre el arbol de UORS y rellena un array con las unidades organicas descendientes.
    function uorTieneDescendencia(codUorSeleccionada,codOrgUorSeleccionada){
        var tieneDescendencia = false;

        // recorremos todos los nodos distintos del seleccionado
        for (var k =0; k < listaUnidOrganicasOriginal.length; k++) {
            if(codOrgUorSeleccionada==listaUnidOrganicasOriginal[k][0]){
                //Miramos las unidades de la lista de la misma organizacion
                if (codUorSeleccionada != listaUnidOrganicasOriginal[k][2] ){
                    // miramos si la uor seleccionada es padre de alguna
                    // y si lo es, agreegamos su hijo para despues borrarlo
                    if (codUorSeleccionada == listaUnidOrganicasOriginal[k][4]){
                        tieneDescendencia = true;                    
                        unidadesDependientesUOR[unidadesDependientesUOR.length] = listaUnidOrganicasOriginal[k][2];
                        uorTieneDescendencia(listaUnidOrganicasOriginal[k][2]);
                    }
                }
            }
        }
        return tieneDescendencia;
    }


    function seleccionarFila(rowID,tableName){       
        if(tabUnidOrganicas.selectedIndex >= 0) {
            tabUnidOrganicas.desactivaRow(tabUnidOrganicas.selectedIndex);
        }
        tabUnidOrganicas.selectedIndex = rowID;
        tabUnidOrganicas.activaRow(rowID);
    }


    // Obtiene el indice de la fila (de la tabla de unidades organicas) para un determiando codigo interno de una our.
    function obtenerIndiceFila(codUnidadDependienteUOR){       
         for (var k =0; k < listaUnidOrganicasOriginal.length; k++) {
             if(codUnidadDependienteUOR == listaUnidOrganicasOriginal[k][2]){
                 return  k;
             }
         }
         return -1;
    }
    
    function pulsarEliminarTodasUOROrgEntidad(codOrg, codEnt) {
        var list = new Array();
        var listOrig = new Array();
        var j = 0;
        for (var k =0; k < listaUnidOrganicasOriginal.length; k++) {
            if ((listaUnidOrganicasOriginal[k][0] != codOrg) && (listaUnidOrganicasOriginal[k][2] != codEnt)) {
                list[j] = listaUnidOrganicas[k];
                listOrig[j++] = listaUnidOrganicasOriginal[k];
            }
        }
        tabUnidOrganicas.lineas=list;
        refrescaUnidOrganicas();
        listaUnidOrganicas=list;
        listaUnidOrganicasOriginal = listOrig;
    }
    
    
    function pulsarAltaUnidOrganica() {
        var datosAEnviar = new Array();
        datosAEnviar[0] = "";
        datosAEnviar[1] = "";
        datosAEnviar[2] = "";
        datosAEnviar[3] = "";
        datosAEnviar[4] = "";
        datosAEnviar[5] = "";
        
        var isCodOrgSelected = 'false';
        for (i=0; i < listaOrganizaciones.length; i++) {
            var caja = "box" + i;
            if(eval("document.forms[0]." + caja + ".checked") == true) {
                isCodOrgSelected = 'true';
            }
        }
        if (isCodOrgSelected=='false') {
            jsp_alerta('A',  '<%=descriptor.getDescripcion("msjAlMenosUnaOrg")%>');
                tp1.setSelectedIndex(1);
            } else {
            var source = "<%=request.getContextPath()%>/administracion/UsuariosGrupos.do?opcion=verUnidOrganicas&primero="+crearListaOrganizacionesUsuarios('si');
            abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/administracion/mainVentana.jsp?source="+source,datosAEnviar,
	'width=790,height=300,status='+ '<%=statusBar%>',function(datos){
                    if(datos!=undefined){
                        if (datos[4] == -1) {
                            pulsarEliminarTodasUOROrgEntidad(datos[0], datos[2]);
                            seleccionarTodasLasUnidades(datos[0], datos[1], datos[2], datos[3], datos[4], datos[5], datos[6], datos[7], datos[8],
                                datos[9], datos[10], datos[11], datos[13]);
                        }else{
                            altaUnidOrganica(datos);
                        }
                    }
                });
            }
        }
        
        function seleccionarTodasLasUnidades(codOrg, descOrg, codEnt, descEnt, codUorSel, descUorSel, cargo, descCargo, listaUorsCascada, infoListasUors,
            permisoCascadaChecked, tieneDescendencia, infoPadreListasUors){
            //En función de los parámetros seleccionados hacemos una búsqueda asincrona para recuperar la lista con todas las UOR
            //que coincidan con los datos introducidos.
            document.forms[0].target="oculto";
            document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do?opcion=buscarTodasUOR&codOrg="+ codOrg 
                + "&descOrg=" + descOrg
                + "&codEnt=" + codEnt 
                + "&descEnt=" + descEnt
                + "&codCargo=" + cargo
                + "&descCargo=" + descCargo
                + "&listaUorsCascada=" + listaUorsCascada
                + "&infoListasUors=" + infoListasUors
                + "&permisoCascadaChecked=" + permisoCascadaChecked
                + "&tieneDescendencia=" + tieneDescendencia
                + "&infoPadreListasUors" + infoPadreListasUors;
            
            document.forms[0].submit();
        }//seleccionarTodasLasUnidades
        
        function rellenarTodasLasUnidades(uorCods, uorDescs, uorPad ,codOrganizacion, descOrganizacion, codEntidad, descEntidad, codCargo,
                                            descCargo, listaUorsCascada, infoListasUors, permisoCascadaChecked, tieneDescendencia,
                                            infoPadreListasUors){
           var arrayDatos = new Array();
            for(var i=0; i<uorCods.length; i++){
                var datos = new Array();
                    datos[0]= codOrganizacion;
                    datos[1]= descOrganizacion;
                    datos[2]= codEntidad;
                    datos[3]= descEntidad;
                    datos[4]= uorCods[i];
                    datos[5]= uorDescs[i];
                    datos[6]= codCargo;
                    datos[7]= descCargo;
                    datos[8]= '';
                    datos[9]= '';
                    datos[10]= '';
                    datos[11]= '';
                    datos[12]= '';
                    datos[13]= uorPad[i];
                    
                    arrayDatos[i] = datos;
            }//for(var i=0; i<uors_dto.length; i++)
            altaTodasUnidadesOrganicas(arrayDatos);
        }//rellenarTodasLasUnidades
        
        function altaTodasUnidadesOrganicas(arrayDatos){
            var lineas = tabUnidOrganicas.lineas;
            for (j=0; j < lineas.length; j++) {
            }
         
            for(var i=0; i<arrayDatos.length; i++){
                var datos = new Array();
                datos = arrayDatos[i];
                listaUnidOrganicas[j+i]=[datos[5],datos[1],datos[3],datos[7]];
                listaUnidOrganicasOriginal[j+i]=[datos[0],datos[2],datos[4],datos[6], datos[13] ]; 
            }//for(var i=0; i<datos.length; i++)    
            tabUnidOrganicas.lineas=listaUnidOrganicas;
            refrescaUnidOrganicas();
        }//altaTodasUnidOrganica(datos)
        
        function procesarDatosUnidOrganica(d) {
            var lista = new Array();
            lista[0] = d[0];
            lista[1] = d[1];
            lista[2] = d[2];
            lista[3] = d[3];
            lista[6] = d[6];
            lista[7] = d[7];
            for(var i=0;i<d[4].length;i++) {
                lista[4] = d[4][i];
                lista[5] = d[5][i];
                if(d[4][i] != 999) {
                    altaUnidOrganica(lista);
                }
            }
        }
        
        function crearListasUnidOrganicas() {
            var lOrganizaciones = "";
            var lEntidades = "";
            var lUnidOrganicas ="";
            var lCargos ="";
            for (i=0; i < listaUnidOrganicas.length; i++) {
                lOrganizaciones +=listaUnidOrganicasOriginal[i][0]+'§¥';
                lEntidades +=listaUnidOrganicasOriginal[i][1]+'§¥';
                lUnidOrganicas +=listaUnidOrganicasOriginal[i][2]+'§¥';
                if (listaUnidOrganicasOriginal[i][3]=="") lCargos +=null+'§¥';
                else lCargos +=listaUnidOrganicasOriginal[i][3]+'§¥';
            }
            var listas = new Array();
            listas = [lOrganizaciones,lEntidades,lUnidOrganicas,lCargos];
            return listas;
        }
        
        // Fin del JavaScript de la tabla Grupos
        
        function onClickFirma(codigo) {
            window.open("<html:rewrite page='/VerFicheroFirma'/>","ventana1","left=10, top=10, width=650, height=500, scrollbars=no, menubar=no, location=no, resizable=no")
            }
            function ventanaPopUpModal() {
                var datosAEnviar = new Array();
                var source = "<%=request.getContextPath()%>/administracion/UsuariosGrupos.do?opcion=ficheroFirma";
                abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/administracion/mainVentana.jsp?source="+source,datosAEnviar,
                    'width=440,height=190,status=no,scrollbars=no,resizable:no',function(){
                        if (res=="OK") actualizarFicheros();
                    });
                }
                function actualizarFicheros(){
                    eval("document.forms[0].fichero.value='Firma';");
                    eval("habilitarBoton(document.forms[0].cmdVisualizarFichero);");
                    eval("habilitarBoton(document.forms[0].cmdEliminarFichero);");
                }
                
                function onClickEliminarFirma() {
                    if(jsp_alerta("C",'<%=descriptor.getDescripcion("msjBorrarFirma")%>') ==1) {
                        eval("document.forms[0].fichero.value='';");
                        eval("deshabilitarBoton(document.forms[0].cmdVisualizarFichero);");
                        eval("deshabilitarBoton(document.forms[0].cmdEliminarFichero);");
                        document.forms[0].opcion.value="eliminarFicheroFirma";
                        document.forms[0].target="oculto";
                        document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
                        document.forms[0].submit();
                    }
                }
                
        function pulsarBuscarAplDirectivas() {
            if (comboAplicacion.selectedIndex < 0) { 
              jsp_alerta("A", '<%=descriptor.getDescripcion("msjAplicNoEleg")%>');
            } else {
              ocultarDivsAplDirectivas();
              var codApl = cod_Aplicaciones[comboAplicacion.selectedIndex];
              document.getElementById('divApl' + codApl).style.display = 'inline-table';
              
              var botonBuscar = [document.forms[0].botonBuscar];
              deshabilitarGeneral(botonBuscar);
              comboAplicacion.deactivate();
            }
        }
        
        function pulsarCancelarBuscarAplDirectivas() {
           ocultarDivsAplDirectivas();
           comboAplicacion.selectItem(-1);
           
           var botonBuscar = [document.forms[0].botonBuscar];
			     habilitarGeneral(botonBuscar);
			     comboAplicacion.activate();
        }
        
        function ocultarDivsAplDirectivas() {
          for (var i=0; i<cod_Aplicaciones.length; i++) {
            document.getElementById('divApl' + cod_Aplicaciones[i]).style.display = 'none';
          }
        }
        
        function pulsarRecuperar(){            
            document.forms[0].fechaEliminado.value="";
            document.getElementById("datosEliminacion").style.display="none";
            habilitarBoton(document.forms[0].cmdAlta);
            habilitarBoton(document.forms[0].cmdEliminar);
            habilitarBoton(document.forms[0].cmdAltaUnidOrg);
            deshabilitarBoton(document.forms[0].cmdRecuperar);
            habilitarBoton(document.forms[0].cmdEliminarUnidOrg);
            document.getElementById("tablaOrganizacion").disabled=false;
            comboAplicacion.activate();
            habilitarPestañaGeneral(1);
        }



        function deshabilitarSiEliminado(){
            if (document.forms[0].fechaEliminado.value!=""){
                comboAplicacion.deactivate();
                habilitarPestañaGeneral(0);
                deshabilitarBoton(document.forms[0].cmdAlta);
                deshabilitarBoton(document.forms[0].cmdEliminar);
                deshabilitarBoton(document.forms[0].cmdAltaUnidOrg);
                deshabilitarBoton(document.forms[0].cmdEliminarUnidOrg);                
                document.getElementById("tablaOrganizacion").disabled=true;
            }
            else{
                document.getElementById("datosEliminacion").style.display="none";
                document.getElementById("cmdRecuperar").style.display="none";
            }
        }

        function habilitarPestañaGeneral(opcion){
            var campos = new Array();
            campos[0] = document.forms[0].nombreUsuario;
            campos[1] = document.forms[0].nif;
            campos[2] = document.forms[0].firmante;
            campos[3] = document.forms[0].codIdioma;
            campos[4] = document.forms[0].descIdioma;
            campos[5] = document.forms[0].contrasena;
            campos[6] = document.forms[0].contrasena2;
            campos[7] = document.forms[0].email;
            campos[8] = document.forms[0].login;
            campos[9] = document.forms[0].estado;
            if (PORTAFIRMAS == "LAN") {
                campos[10] = document.forms[0].buzonFirma;
            }
            if (opcion==0) deshabilitarGeneral(campos);
            else habilitarGeneral(campos);
        }


        function mostrarProcedimientosRestringidos(){
            var datosAEnviar = new Array();
            var source = "<%=request.getContextPath()%>/administracion/UsuariosGrupos.do?opcion=cargarSeleccionProcedimientosRestringidos";

            // En el caso de dar de alta el usuario , hay que pasar a la ventana de los procedimientos seleccionados
            datosAEnviar = tratarParametrosEntrada();
            
            var codigoUsuario = document.forms[0].codUsuario.value;            
            abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/administracion/mainVentana.jsp?source="+source,datosAEnviar,
	'width=950,height=550,status=no,scrollbars=no',function(resultado){
                        if (resultado != undefined) {
                            var lista = tratarListaProcedimientosRestringidos(resultado);                

                            document.forms[0].listaProcedimientosRestringidos.value=lista;                
                        }
                    });
        }


        function tratarParametrosEntrada(){
            var salida = new Array();
            var separador ="§¥";
            var datos = document.forms[0].listaProcedimientosRestringidos.value.split(separador);
            var codUsuario = document.forms[0].codUsuario.value;

            salida[0] = crearListaOrganizacionesUsuarios();
            if(codUsuario!=null)
                salida[1] = "M"; // Se indica que se modifica el usuario
            else
                salida[1] = "A"; // Se indica que se da de alta el usuario

            var contador=2;
            for(i=0;i<datos.length;i++){
                salida[contador] = datos[i]
                contador++;
            }

            return salida;
        }

        function tratarListaProcedimientosRestringidos(lista){
            var separador ="§¥";
            var salida = "";
            for(i=0;i<lista.length;i++){
                salida = salida  + lista[i];
                if(lista.length-i>1)
                    salida  =salida + separador;
            }// for
            return salida;
        }




   
        /**
         * Muestra u oculta la capa que contiene el acceso a la ventana de seleccion de los procedimientos restringidos
         */
        function activarProcedimientosRestringidos(){ 
            if(document.getElementById("checkDirectivaPROCEDIMIENTOS_RESTRINGIDOS")!=undefined){

                if(document.forms[0].checkDirectivaPROCEDIMIENTOS_RESTRINGIDOS.checked){                    
                    document.getElementById("imgProcedimientosRestringidos").style.visibility = "visible";
                }else{
                    document.getElementById("imgProcedimientosRestringidos").style.visibility = "hidden";
                }
            }
        }




 function validarPassword(password){         
	var space  = " ";
        var cumpleLongitud = true;
        var contador = 0;
        var longitud = 0;
        
        if(longitudMinima!=null && longitudMinima!="" && longitudMinima!="NO" ){
            
            try{
                longitud = parseInt(longitudMinima);                
                                               
                if(password.length<longitud){
                    contador++;
                    cumpleLongitud = false;
                }
            }catch(err){                
            }            
        }
         
	//It must not contain a space
	if (password.indexOf(space) > -1) {
            contador++;
	}     
	 
	//It must contain at least one number character
	if (!(password.match(/\d/))) {
             contador++;
	}
		
	//It must start with at least one letter     
	if (!(password.match(/[a-zA-Z]/))) {
	   //errorMsg += "La contraseña debe contener letras<\br>";
           contador++;
	}
	
	//If there is aproblem with the form then display an error
	if (contador>0){
            if(longitud>0)
           
             jsp_alerta('A', '<%=descriptor.getDescripcion("msgFormatoLongitudPassword1")%>' + " " + longitud + " " +  '<%=descriptor.getDescripcion("msgFormatoLongitudPassword2")%>');
           
            else{                 
              jsp_alerta('A', '<%=descriptor.getDescripcion("msgFormatoPassword")%>');
              
            }
             return false;
	}		 
        return true;
        
    }


</SCRIPT>
</head>

<body class="bandaBody" onload="javascript:{ pleaseWait('off');inicializar()}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
<html:form action="/administracion/UsuariosGrupos.do" target="_self">
    <html:hidden  property="opcion" value=""/>
    <html:hidden  property="codUsuario"/>
    <html:hidden  property="listaDirectivasTxt" value=""/>
    <input type="hidden" name="listaOrgan" value="">
    <input type="hidden" name="lOrganizaciones" value="">
    <input type="hidden" name="lEntidades" value="">
    <input type="hidden" name="lAplicaciones" value="">
    <input type="hidden" name="lGrupos" value="">

    <input type="hidden" name="lOrganizacionesUOR" value="">
    <input type="hidden" name="lEntidadesUOR" value="">
    <input type="hidden" name="lUnidOrganicas" value="">
    <input type="hidden" name="lCargosUOR" value="">
    <input type="hidden" name="listaProcedimientosRestringidos" value=""/>

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("tit_datUsu")%></div>
    <div class="contenidoPantalla" >
        <div class="tab-pane" id="tab-pane-1">
            <script type="text/javascript">
                tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
            </script>

            <!-- CAPA 1: DATOS GENERALES
------------------------------ -->
            <div class="tab-page" id="tabPage1" >
                <h2 class="tab" style="margin-left:15px;"><%=descriptor.getDescripcion("gEtiq_datGen")%></h2>
                <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>
                <TABLE id ="tablaDatosGral" class="contenidoPestanha" cellpadding="4" cellspacing="5">
                    <tr>
                        <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Nombre")%>:</td>
                        <td class="columnP">
                            <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="nombreUsuario" size="70" maxlength="40"
                                       onblur="return xAMayusculas(this);"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("etiq_login")%>:</td>
                        <td class="columnP">
                            <html:text styleId="obligatorio" styleClass="inputTextoObligatorioSinMayusculas" property="login" size="70" maxlength="250"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("etiq_cont")%>:</td>
                        <td class="columnP">
                            <html:password styleId="obligatorio" styleClass="inputTextoObligatorioSinMayusculas" property="contrasena" size="25" maxlength="15"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("etiq_cont2")%>:</td>
                        <td class="columnP">
                            <html:password styleId="obligatorio" styleClass="inputTextoObligatorioSinMayusculas" property="contrasena2" size="25" maxlength="15"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Idioma")%>:</td>
                        <td class="columnP">
                            <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="codIdioma" size="2"
                                       onkeyup="return SoloDigitosNumericos(this);"/>
                            <html:text styleId="obligatorio" styleClass="inputTextoObligatorioSinMayusculas" property="descIdioma" size="62" readonly="true"/>
                            <A href="" id="anchorIdioma" name="anchorIdioma">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonIdioma" name="botonIdioma"></span>
                            </A>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("etiq_mail")%>:</td>
                        <td class="columnP">
                            <html:text styleClass="inputTextoSinMayusculas" property="email" size="70" maxlength="100"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("etiq_nif")%>:</td>
                        <td class="columnP">
                            <html:text styleClass="inputTexto" property="nif" size="25" maxlength="10" onblur="return xAMayusculas(this);"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("etiq_estado")%>:</td>
                        <td class="columnP">
                            <html:select styleId="obligatorio" styleClass="inputTextoObligatorio" property="estado">
                                <html:option value="0"><%=descriptor.getDescripcion("gEtiq_Dblq")%></html:option>
                                <html:option value="1"><%=descriptor.getDescripcion("gEtiq_Bloq")%></html:option>
                            </html:select>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("etiq_firmante")%>:</td>
                        <td class="columnP">
                            <html:select styleId="obligatorio" styleClass="inputTextoObligatorio" property="firmante" onchange="habilitarDeshabilitarBuzonFirma();">
                                <html:option value="0"><%=descriptor.getDescripcion("gEtiq_No")%></html:option>
                                <html:option value="1"><%=descriptor.getDescripcion("gEtiq_Si")%></html:option>
                            </html:select>
                        </td>
                    </tr>
                    <% if(PORTAFIRMAS!=null && !"".equals(PORTAFIRMAS) && "LAN".equals(PORTAFIRMAS)){ %>
                        <tr>
                            <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("etiq_buzonFirma")%>:</td>
                            <td class="columnP">
                                <html:text styleClass="inputTextoObligatorio" property="buzonFirma" size="8" maxlength="8"/>
                            </td>
                        </tr>
                    <% } %>
                    <tr>
                        <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_firma")%>:</td>
                        <td class="etiqueta">
                            <table cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td>
                                        <input type="text" name="fichero" id="fichero" class="inputTexto" size=22 readonly="true">
                                        <A href="javascript:ventanaPopUpModal('inicio','fichero');" style="text-decoration:none;" >
                                            <span class="fa fa-paperclip" id="botonFichero" name="botonFichero" ></span>
                                        </A>
                                    </td>
                                    <td style="width: 2px"></td>
                                    <div style="border: 0">
                                        <td style="width: 60%; text-align: right">
                                            <table cellpadding="0px" cellspacing="0px">
                                                <tr>
                                                    <td>
                                                        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gEtiq_Visualizar")%>" name="cmdVisualizarFichero" onclick="onClickFirma();">
                                                    </td>
                                                    <td style="width: 2px"></td>
                                                    <td>
                                                        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>" name="cmdEliminarFichero" onclick="onClickEliminarFirma();">
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>        

                                    </div>
                                </tr>
                            </table>
                        </td>
                    </tr>
                     <tr>
                        <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("etiqCambioPasswdObligatorio")%>:</td>
                        <td  class="etiqueta">
                            <table cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td>                                                                     
                                        <html:checkbox property="cambioPantallaObligatorio"/>
                                    </td>
                                    <td style="width:2px;"></td>    
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr id="datosEliminacion">
                        <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("msjFechaUsuEliminado")%>:</td>
                        <td class="columnP">
                            <html:text styleClass="inputTxtFechaDeshabilitado" property="fechaEliminado" size="10" maxlength="100" readonly="true"/>
                        </td>
                    </tr>
                </table>
            </div>

            <!-- CAPA 2: ORGANIZACIONES  ------------------------------ -->
            <div class="tab-page" id="tabPage2" >
                <h2 class="tab"><%=descriptor.getDescripcion("gEtiq_Organs")%></h2>
                <script type="text/javascript">tp1_p2 = tp1.addTabPage( document.getElementById( "tabPage2" ) );</script>
                <TABLE id ="tablaDatosGral" class="contenidoPestanha">
                    <tr id="tablaOrganizacion">
                        <td colspan="2" id="tablaOrganizaciones">
                    </tr>
                </TABLE>
            </div>
            <!-- CAPA 3: GRUPOS   ------------------------------ -->
            <div class="tab-page" id="tabPage3" >
                <h2 class="tab"><%=descriptor.getDescripcion("tit_grup")%></h2>
                <script type="text/javascript">tp1_p3 = tp1.addTabPage( document.getElementById( "tabPage3" ) );</script>
                <TABLE id ="tablaDatosGral" class="contenidoPestanha" style="margin-bottom: 5px">
                    <tr>
                        <td>
                            <table align="center">
                            <tr>
                                <td style="width: 100%" id="tablaGrupos"></td>
                            </tr>
                            </table>
                        </td>
                    </tr>
                </TABLE>
                <div style="width:100%; text-align: center">
                    <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%> name="cmdAlta"  onClick="pulsarAltaGrupo();">
                    <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> name="cmdEliminar"  onClick="pulsarEliminarGrupo();">
                </div>                                                                    
            </div>
            <div class="tab-page" id="tabPage4" style="height:400px;">
                <h2 class="tab"><%=descriptor.getDescripcion("etiqUnidOrg")%></h2>
                <script type="text/javascript">tp1_p4 = tp1.addTabPage( document.getElementById( "tabPage4" ) );</script>
                <TABLE id ="tablaDatosGral" class="contenidoPestanha" style="margin-bottom: 5px">
                    <tr>
                        <td>
                            <table align="center">
                            <tr>
                                <td style="width: 100%" id="tablaUnidOrganicas"></td>
                            </tr>
                            </table>
                        </td>
                    </tr>
                </TABLE>
                <!-------------------------------------- BOTONES. ------------------------------------------>
                <div style="border: 0; text-align: center">
                    <table cellpadding="0px" cellspacing="0px" style="border: 0;" align="right">
                        <tr>
                            <td>
                                <table cellpadding="0px" cellspacing="0px">
                                    <tr>
                                        <td>
                                            <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%> name="cmdAltaUnidOrg"  onClick="pulsarAltaUnidOrganica();">
                                        </td> 
                                        <TD style="width:2px"></TD>
                                        <td>
                                            <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> name="cmdEliminarUnidOrg"  onClick="pulsarEliminarUnidOrganica();">
                                        </td>                                                                      
                                    </tr>
                                </table>
                            </td>        
                        </tr>
                    </table>
                </div>
            </div>
            <!-- CAPA 5: DIRECTIVAS -->
            <div class="tab-page" id="tabPage5">
                <h2 class="tab"><%=descriptor.getDescripcion("etiqDirectivas")%></h2>
                <script type="text/javascript">tp1_p5 = tp1.addTabPage( document.getElementById( "tabPage5" ) );</script>
                <table class="contenidoPestanha" style="margin-bottom: 5px">
                    <tr>
                        <td height="5px"></td>
                    </tr>
                    <tr>
                        <td>
                            <table border="0" width="100%" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td class="etiqueta" style="width: 10%"><%=descriptor.getDescripcion("gEtiq_Aplicacion")%>:</td>
                                    <td class="etiqueta" style="width: 60%">
                                        <input type="text" name="codAplicacion" id="codAplicacion" size="3" class="inputTextoObligatorio" value="">
                                        <input type="text" name="descAplicacion"  id="descAplicacion" size="43" class="inputTextoObligatorio" readonly="true" value="">
                                        <a href="" id="anchorAplicacion" name="anchorAplicacion">
                                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                                 name="botonAplicacion" style="cursor:hand;"></span>
                                        </a>
                                    </td>
                                    <td align="right" style="width: 30%">
                                        <input name="botonBuscar" type="button"  class="botonGeneral" id="botonBuscar"
                                               value="<%=descriptor.getDescripcion("gbBuscar")%>"
                                               onClick="pulsarBuscarAplDirectivas();" accesskey="B">
                                        <input name="botonCancelar" type="button" class="botonGeneral" id="botonCancelar"
                                               value='<%=descriptor.getDescripcion("gbCancelar")%>'
                                               onClick="pulsarCancelarBuscarAplDirectivas();" accesskey="C">
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td height="10px"></td>
                    </tr>
                    <tr>
                        <td>
                            <table width="100%" cellspacing="0px" cellpadding="0px">
                                <tr>
                                    <td class="sub3titulo">&nbsp;<%=descriptor.getDescripcion("titDirectivasApl")%></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                      <td align="left">
                        <!-- Iteracion sobre la lista de aplicaciones -->
                        <logic:iterate id="codigoAplicacion" name="UsuariosGruposForm" property="codAplDirectivas">

                          <div id ="divApl<bean:write name='codigoAplicacion'/>" style="width:100%; height:100%; overflow-y:auto; padding-left:20px; padding-right:20px; padding-top:10px;">
                           <table border="0" cellspacing="0" cellpadding="0" style="width:90%">
                             <!-- Iteracion sobre la lista de directivas -->
                             <logic:iterate id="directiva" name="UsuariosGruposForm" property="directivas">
                               <logic:equal name="directiva" property="aplCod" value="<%= (String) codigoAplicacion %>">
                                <tr>
                                    <td class="etiqueta" style="width:55%">
                                        <c:if test="${directiva.codigo ne 'PROCEDIMIENTOS_RESTRINGIDOS'}">
                                            <%-- SI LA DIRECTIVA NO ES LA DE PROCEDIMIENTOS RESTRINGIDOS --%>
                                              <input type="checkbox" id='checkDirectiva<bean:write name="directiva" property="codigo"/>'
                                                 <logic:notEmpty name="directiva" property="permiso"> checked </logic:notEmpty>
                                                 >&nbsp;<bean:write name="directiva" property="mensaje"/>
                                              </input>
                                        </c:if>
                                        <c:if test="${directiva.codigo eq 'PROCEDIMIENTOS_RESTRINGIDOS'}">
                                         <%-- SI LA DIRECTIVA ES LA DE PROCEDIMIENTOS RESTRINGIDOS --%>
                                            <input type="checkbox" onclick="javascript:activarProcedimientosRestringidos();" id='checkDirectiva<bean:write name="directiva" property="codigo"/>'
                                                <logic:notEmpty name="directiva" property="permiso"> checked </logic:notEmpty>
                                                >&nbsp;<bean:write name="directiva" property="mensaje"/>
                                            </input>
                                         </c:if>
                                         <%-- FIN DE LA COMPROBACIÓN DE SI LA DIRECTIVA ES LA DE PROCEDIMIENTOS RESTRINGIDOS --%>
                                    </td>
                                    <td>
                                         <c:if test="${directiva.codigo eq 'PROCEDIMIENTOS_RESTRINGIDOS'}">
                                         <%-- SI LA DIRECTIVA ES LA DE PROCEDIMIENTOS RESTRINGIDOS --%>
                                             <logic:notEmpty name="directiva" property="permiso">
                                             <DIV id="imgProcedimientosRestringidos" name="imgProcedimientosRestringidos" style="width:100%;visibility:visible;" >
                                                 <span class="fa fa-search" aria-hidden="true" 
                                                    alt='<%=descriptor.getDescripcion("gEtiqDirRestringidos")%>' title='<%=descriptor.getDescripcion("gEtiqDirRestringidos")%>'
                                                    onclick="javascript:mostrarProcedimientosRestringidos();"></span>
                                             </DIV>
                                             </logic:notEmpty>
                                             <logic:empty name="directiva" property="permiso">
                                                 <DIV id="imgProcedimientosRestringidos" name="imgProcedimientosRestringidos" style="width:100%;visibility:hidden;" >
                                                 <span class="fa fa-search" aria-hidden="true" 
                                                    alt='<%=descriptor.getDescripcion("gEtiqDirRestringidos")%>' title='<%=descriptor.getDescripcion("gEtiqDirRestringidos")%>'
                                                    onclick="javascript:mostrarProcedimientosRestringidos();"></span>
                                             </DIV>
                                             </logic:empty>
                                         </c:if>
                                         <%-- FIN DE LA COMPROBACIÓN DE SI LA DIRECTIVA ES LA DE PROCEDIMIENTOS RESTRINGIDOS --%>
                                    </td>
                                </tr>
                                <tr>
                                    <td height="5px"></td>
                                </tr>
                              </logic:equal>
                             </logic:iterate>
                           </table>
                          </div>
                        </logic:iterate>
                      </td>
                    </tr>
                </table>
            </div>
        </div>
</div>
    <div class="capaFooter">
        <div class="botoneraPrincipal"> 
            <input type="button" class="botonLargo" value="<%=descriptor.getDescripcion("msjRecuperarUsuario")%>" name="cmdRecuperar"  id="cmdRecuperar" onclick="pulsarRecuperar();" >
            <INPUT type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar"  onClick="pulsarAceptar();">
            <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir"  onClick="pulsarSalir();">
        </div>            
</div>
</html:form> 
<script  type="text/javascript">
            // Se guardan en el campo de formulario correspondiente la lista de procedimientos restringidos
            <%
                String jListaProcedimientosRestringidos = bForm.getListaProcedimientosRestringidos();
            %>
            document.forms[0].listaProcedimientosRestringidos.value = "<%=jListaProcedimientosRestringidos %>";
            
            // Javascript del combo de aplicaciones de directivas
            var comboAplicacion = new Combo("Aplicacion");
            var cod_Aplicaciones = new Array();
            var desc_Aplicaciones = new Array();

            var i=0;
	        <logic:iterate id="codApl" name="UsuariosGruposForm" property="codAplDirectivas">
	           cod_Aplicaciones[i]='<bean:write name="codApl"/>';
	           i++;
	        </logic:iterate>

            i=0;
	        <logic:iterate id="descApl" name="UsuariosGruposForm" property="descAplDirectivas">
	           desc_Aplicaciones[i]="<str:escape><bean:write name="descApl" filter="false"/></str:escape>";
	           i++;
	        </logic:iterate>

            comboAplicacion.addItems(cod_Aplicaciones, desc_Aplicaciones);

            // JAVASCRIPT DE LA TABLA ORGANIZACIONES
            
            var tabOrganizaciones = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaOrganizaciones'));
            
            tabOrganizaciones.addColumna('120','center','<%= descriptor.getDescripcion("etiq_Autor")%>');
                tabOrganizaciones.addColumna('780','left','<%= descriptor.getDescripcion("gEtiq_Nombre")%>');
                    tabOrganizaciones.displayCabecera=true;
                    
                    function refrescaOrganizaciones() {
                        tabOrganizaciones.displayTabla();
                    }
                    
                    
                    // FIN DE LOS JAVASCRIPT'S DE LA TABLA ORGANIZACIONES
                    
                    // JAVASCRIPT DE LA TABLA GRUPOS
                    
                    var tabGrupos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaGrupos'));
                    
                    tabGrupos.addColumna('225','left','<%= descriptor.getDescripcion("etiq_grup")%>');
                    tabGrupos.addColumna('225','left','<%= descriptor.getDescripcion("gEtiq_Organizacion")%>');
                    tabGrupos.addColumna('225','left','<%= descriptor.getDescripcion("gEtiq_Entidad")%>');
                    tabGrupos.addColumna('225','left','<%= descriptor.getDescripcion("gEtiq_Aplicacion")%>');
                                    
                    tabGrupos.displayCabecera=true;
                                    
                                    function refrescaGrupos() {
                                        tabGrupos.displayTabla();
                                    }
                                    
                                    tabGrupos.displayDatos = pintaDatosGrupos;
                                    
                                    function pintaDatosGrupos() {
                                        tableObject = tabGrupos;
                                    }
                                    
                                    
                                    // FIN DE LOS JAVASCRIPT'S DE LA TABLA GRUPOS
                                    
                                    // JAVASCRIPT DE LA TABLA UNIDADES ORGANICAS
                                    
                                    var tabUnidOrganicas = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaUnidOrganicas'));
                                    
                                    tabUnidOrganicas.addColumna('225','left','<%= descriptor.getDescripcion("gEtiq_unidOrg")%>');
                                        tabUnidOrganicas.addColumna('225','left','<%= descriptor.getDescripcion("gEtiq_Organizacion")%>');
                                            tabUnidOrganicas.addColumna('225','left','<%= descriptor.getDescripcion("gEtiq_Entidad")%>');
                                                tabUnidOrganicas.addColumna('225','left','<%= descriptor.getDescripcion("gEtiq_Cargo")%>');
                                                    tabUnidOrganicas.displayCabecera=true;
                                                    
                                                    function refrescaUnidOrganicas() {
                                                        tabUnidOrganicas.displayTabla();
                                                    }
                                                    
                                                    tabUnidOrganicas.displayDatos = pintaDatosUnidOrganicas;
                                                    
                                                    function pintaDatosUnidOrganicas() {
                                                        tableObject = tabUnidOrganicas;
                                                    }
                                                    
                                                    // FIN DE LOS JAVASCRIPT'S DE LA TABLA UNIDADES ORGANICAS
                                                    <%String Agent = request.getHeader("user-agent");%>

                                                    var coordx=0;
                                                    var coordy=0;


                                                    <%if(Agent.indexOf("MSIE")==-1) {%> //Que no sea IE
                                                        window.addEventListener('mousemove', function(e) {
                                                            coordx = e.clientX;
                                                            coordy = e.clientY;
                                                        }, true);
                                                    <%}%>
                                                    
                                                    document.onmouseup = checkKeys;
                                                    
                                                    function checkKeysLocal(evento, tecla){

                                                         var teclaAuxiliar = "";
                                                         if(window.event){
                                                             evento = window.event;
                                                             teclaAuxiliar = evento.keyCode;
                                                         }else
                                                             teclaAuxiliar = evento.which;
                                                        
                                                        if(teclaAuxiliar == 40){
                                                            if(tabGrupos==tableObject) {
                                                                upDownTable(tabGrupos,listaGrupos,teclaAuxiliar);
                                                            } else{
                                                            upDownTable(tabUnidOrganicas,listaUnidOrganicas,teclaAuxiliar);
                                                        }
                                                    }

                                                    if (teclaAuxiliar == 38){
                                                        if(tabGrupos==tableObject) {
                                                            upDownTable(tabGrupos,listaGrupos,teclaAuxiliar);
                                                        } else{
                                                        upDownTable(tabUnidOrganicas,listaUnidOrganicas,teclaAuxiliar);
                                                    }
                                                }
                                                
                                                if(teclaAuxiliar == 13){
                                                    if((tabGrupos.selectedIndex>-1)&&(tabGrupos.selectedIndex < listaGrupos.length)){
                                                        if(tabGrupos==tableObject)
                                                            callFromTableTo(tabGrupos.selectedIndex,tabGrupos.id);
                                                    }
                                                    if((tabUnidOrganicas.selectedIndex>-1)&&(tabUnidOrganicas.selectedIndex < listaUnidOrganicas.length)){
                                                        if(tabUnidOrganicas==tableObject)
                                                            callFromTableTo(tabUnidOrganicas.selectedIndex,tabUnidOrganicas.id);
                                                    }
                                                }
                                                if(teclaAuxiliar == 1){
                                                   if (comboIdioma.base.style.visibility == "visible" && isClickOutCombo(comboIdioma,coordx,coordy)) setTimeout('comboIdioma.ocultar()',20);
                                                   if (comboAplicacion.base.style.visibility == "visible" && isClickOutCombo(comboAplicacion,coordx,coordy)) setTimeout('comboAplicacion.ocultar()',20);
                                                }
                                                if(teclaAuxiliar == 9){
                                                    comboIdioma.ocultar();
                                                    comboAplicacion.ocultar();

                                                }
                                                keyDel(evento);
                                            }
                                            
                                            var comboIdioma = new Combo("Idioma");
                                            
        </script>
        
    </BODY>
    
    </html:html>
    
