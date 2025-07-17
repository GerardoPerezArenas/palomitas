
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
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
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
            if ("no".equals(JSP_entidadColectiva)) {
                ecv = "hidden";
            }
            if ("no".equals(JSP_entidadSingular)) {
                esv = "hidden";
            }
            if ("no".equals(JSP_emplazamiento)) {
                emplv = "hidden";
            }
            if ("no".equals(JSP_poblacion)) {
                pv = "hidden";
            }

            StringTokenizer st = new StringTokenizer(JSP_altaViaDirecta, ",");
            while (st.hasMoreTokens()) {
                if (st.nextToken().trim().equals(String.valueOf(usuarioVO.getOrgCod()))) {
                    bAltaViaDirecta = true;
                }
            }
            ParametrosTerceroValueObject ptVO = null;
            int idioma = 1;
            int apl = 1;
            String css="";
            String origen = (String) session.getAttribute("origen");
            String existeTramero = "";
            String inicioAlta = "";
            String inicioAltaDesdeBuscar = "";
            String inicioModificar = "";
            String cargarTercero = "";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                ptVO = (ParametrosTerceroValueObject) session.getAttribute("parametrosTercero");
                idioma = usuarioVO.getIdioma();
                apl = usuarioVO.getAppCod();
                css=usuarioVO.getCss();
                existeTramero = usuarioVO.getExisteTramero();
            }
            if (session.getAttribute("inicioAlta") != null) {
                inicioAlta = (String) session.getAttribute("inicioAlta");
            } else {
                inicioAlta = "no";
            }
            session.removeAttribute("inicioAlta");
            if (session.getAttribute("inicioAltaDesdeBuscar") != null) {
                inicioAltaDesdeBuscar = (String) session.getAttribute("inicioAltaDesdeBuscar");
            } else {
                inicioAltaDesdeBuscar = "no";
            }
            session.removeAttribute("inicioAltaDesdeBuscar");
            if (session.getAttribute("inicioModificar") != null) {
                inicioModificar = (String) session.getAttribute("inicioModificar");
            } else {
                inicioModificar = "no";
            }
            session.removeAttribute("inicioModificar");
            if (session.getAttribute("cargarTercero") != null) {
                cargarTercero = (String) session.getAttribute("cargarTercero");
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
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
         <LINK	REL="stylesheet" MEDIA="screen" type="text/css"	href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaBusquedaTerceros.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/seleccionBusquedaTerceros.js"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>"></script>
        <SCRIPT type="text/javascript" >
            <%	BusquedaTercerosForm bForm = (BusquedaTercerosForm) session.getAttribute("BusquedaTercerosForm");
            Vector listaDocs = bForm.getListaTipoDocs();
            Vector listaProvincias = bForm.getListaProvincias();
            Vector listaMunicipios = bForm.getListaMunicipios();
            Vector listaUsoViviendas = bForm.getListaUsoViviendas();
            Vector listaCodPostales = bForm.getListaCodPostales();
            int lengthDocs = listaDocs.size();
            int lengthProvs = listaProvincias.size();
            int lengthMun = listaMunicipios.size();
            int lengthUsoViviendas = listaUsoViviendas.size();
            int lengthCodPostales = listaCodPostales.size();
            int i;
            String lcodProv = "";
            String ldescProv = "";
            String lcodMun = "";
            String ldescMun = "";
            String lcodDocs = "";
            String ldescDocs = "";
            String lpersJF = "";
            String lcodTOC = "";
            String ldescTOC = "";
            String lcodCP = "";
            if (lengthDocs > 0) {
                for (i = 0; i < lengthDocs - 1; i++) {
                    GeneralValueObject doc = (GeneralValueObject) listaDocs.get(i);
                    lcodDocs += "'" + doc.getAtributo("codTipoDoc") + "',";
                    ldescDocs += "'" + doc.getAtributo("descTipoDoc") + "',";
                    lpersJF += "'" + doc.getAtributo("persFJ") + "',";
                }
                GeneralValueObject doc = (GeneralValueObject) listaDocs.get(i);
                lcodDocs += "'" + doc.getAtributo("codTipoDoc") + "'";
                ldescDocs += "'" + doc.getAtributo("descTipoDoc") + "'";
                lpersJF += "'" + doc.getAtributo("persFJ") + "'";
            }
            if (lengthProvs > 0) {
                for (i = 0; i < lengthProvs - 1; i++) {
                    GeneralValueObject prov = (GeneralValueObject) listaProvincias.get(i);
                    lcodProv += "'" + prov.getAtributo("codigo") + "',";
                    ldescProv += "'" + prov.getAtributo("descripcion") + "',";
                }
                GeneralValueObject prov = (GeneralValueObject) listaProvincias.get(i);
                lcodProv += "'" + prov.getAtributo("codigo") + "'";
                ldescProv += "'" + prov.getAtributo("descripcion") + "'";
            }
            if (lengthMun > 0) {
                for (i = 0; i < lengthMun - 1; i++) {
                    GeneralValueObject mun = (GeneralValueObject) listaMunicipios.get(i);
                    lcodMun += "\"" + mun.getAtributo("codMunicipio") + "\",";
                    ldescMun += "\"" + mun.getAtributo("nombreOficial") + "\",";
                }
                GeneralValueObject mun = (GeneralValueObject) listaMunicipios.get(i);
                lcodMun += "\"" + mun.getAtributo("codMunicipio") + "\"";
                ldescMun += "\"" + mun.getAtributo("nombreOficial") + "\"";
            }
            if (lengthUsoViviendas > 0) {
                for (i = 0; i < lengthUsoViviendas - 1; i++) {
                    GeneralValueObject toc = (GeneralValueObject) listaUsoViviendas.get(i);
                    lcodTOC += "'" + toc.getAtributo("codUsoVivienda") + "',";
                    ldescTOC += "'" + toc.getAtributo("descUsoVivienda") + "',";
                }
                GeneralValueObject toc = (GeneralValueObject) listaUsoViviendas.get(i);
                lcodTOC += "'" + toc.getAtributo("codUsoVivienda") + "'";
                ldescTOC += "'" + toc.getAtributo("descUsoVivienda") + "'";
            }
            if (lengthCodPostales > 0) {
                for (i = 0; i < lengthCodPostales - 1; i++) {
                    GeneralValueObject cp = (GeneralValueObject) listaCodPostales.get(i);
                    lcodCP += "'" + cp.getAtributo("codPostal") + "',";
                }
                GeneralValueObject cp = (GeneralValueObject) listaCodPostales.get(i);
                lcodCP += "'" + cp.getAtributo("codPostal") + "'";
            }
            Vector listaECOs = bForm.getListaECOs();
            Vector listaESIs = bForm.getListaESIs();
            int lengthECOs = listaECOs.size();
            int lengthESIs = listaESIs.size();
            String codESIs = "";
            String descESIs = "";
            String listaECOESIs = "";
            String lcodECOS = "";
            String ldescECOS = "";
            if (lengthECOs > 0) {
                for (i = 0; i < lengthECOs - 1; i++) {
                    GeneralValueObject eco = (GeneralValueObject) listaECOs.get(i);
                    lcodECOS += "'" + eco.getAtributo("codECO") + "',";
                    ldescECOS += "'" + eco.getAtributo("descECO") + "',";
                }
                GeneralValueObject eco = (GeneralValueObject) listaECOs.get(i);
                lcodECOS += "'" + eco.getAtributo("codECO") + "'";
                ldescECOS += "'" + eco.getAtributo("descECO") + "'";
            }
            if (lengthESIs > 0) {
                for (int j = 0; j < lengthESIs - 1; j++) {
                    GeneralValueObject esiss = (GeneralValueObject) listaESIs.get(j);
                    codESIs += "\"" + (String) esiss.getAtributo("codEntidadSingular") + "\",";
                    descESIs += "\"" + (String) esiss.getAtributo("nombreOficial") + "\",";
                    listaECOESIs += "[\"" + (String) esiss.getAtributo("codEntidadColectiva") + "\",\"" + (String) esiss.getAtributo("descEntidadColectiva") + "\"],";
                }
                GeneralValueObject esiss = (GeneralValueObject) listaESIs.get(lengthESIs - 1);
                codESIs += "\"" + (String) esiss.getAtributo("codEntidadSingular") + "\"";
                descESIs += "\"" + (String) esiss.getAtributo("nombreOficial") + "\"";
                listaECOESIs += "[\"" + (String) esiss.getAtributo("codEntidadColectiva") + "\",\"" + (String) esiss.getAtributo("descEntidadColectiva") + "\"]";
            }%>
            var Domicilios = new Array();
            var lista =	new Array();
            // Listas de valores.
            var	codTipoDocs	= new	Array();
            var	descTipoDocs = new Array();
            var	persFJ = new Array();
            var codPaises = new Array();
            var descPaises = new Array();
            var codProvincias	= new	Array();
            var descProvincias = new Array();
            var codMunicipios	= new	Array();
            var descMunicipios = new Array();
            var codMunicipiosDefecto = new Array();
            var descMunicipiosDefecto = new Array();
            <!--var	codUsoViviendas = new Array();-->
            <!--var descUsoViviendas = new Array();-->
            var	codPostales	= new	Array();
            var paisOld	= "";
            var	provinciaOld = "";
            var	municipioOld = "";
            //  Campos de la tabla Terceros
            var Terceros = new Array();
            var idTercero;
            var DomActual=0;
            var MAX=0;
            var etiq="";
            var codTipoDocOld	= "";
            var descTipoDocOld = "";
            var documentoOld = "";
            var nombreOld = "";
            var	apellido1Old = "";
            var	apellido2Old = "";
            var parAp1Old = "";
            var parAp2Old = "";
            var telfOld	= "";
            var emailOld = "";
            var idDomOld = "";
            var codUsoOld = "";
            var descUsoOld = "";
            var codPaisOld = "";
            var descPaisOld =	"";
            var codProvOld = "";
            var descProvOld =	"";
            var codMunOld = "";
            var descMunOld = "";
            var codTVIOld = "";
            var descTVIOld = "";
            var codViaOld = "";
            var domicilioOld = "";
            var lugarOld = "";
            var codPostalOld = "";
            var numDesdeOld =	"";
            var letraDesdeOld	= "";
            var numHastaOld =	"";
            var letraHastaOld	= "";
            var bloqueOld = "";
            var portalOld = "";
            var escaleraOld =	"";
            var plantaOld = "";
            var puertaOld = "";
            var	normalizadoOld = false;
            var	idViaOld = "";
            /* anadir ECOESI */
            var codECOOld="";
            var codESIOld="";
            var descESIOld="";
            /* fin anadir ECOESI */
            var descViaOld="";
            var registrando=false;
            var modificando=false;
            var cargandoTerceroExt=false;
            var nombreViaGeneral ="";

            function setTerceroAntiguo(){
                codTipoDocOld = document.forms[0].codTipoDoc.value;
                descTipoDocOld = document.forms[0].descTipoDoc.value;
                documentoOld = document.forms[0].txtDNI.value;
                nombreOld =	document.forms[0].txtInteresado.value;
                apellido1Old = document.forms[0].txtApell1.value;
                parApe1Old = document.forms[0].txtPart.value;
                apellido2Old = document.forms[0].txtApell2.value;
                parApe2Old = document.forms[0].txtPart2.value;
                telfOld = document.forms[0].txtTelefono.value;
                emailOld = document.forms[0].txtCorreo.value;
            }
            function getTerceroAntiguo(){
                document.forms[0].codTipoDoc.value = codTipoDocOld;
                document.forms[0].descTipoDoc.value	= descTipoDocOld;
                document.forms[0].txtDNI.value = documentoOld;
                document.forms[0].txtInteresado.value = nombreOld;
                document.forms[0].txtApell1.value =	apellido1Old;
                document.forms[0].txtPart.value = parAp1Old;
                document.forms[0].txtApell2.value =	apellido2Old;
                document.forms[0].txtPart2.value = parApe2Old;
                document.forms[0].txtTelefono.value	= telfOld;
                document.forms[0].txtCorreo.value =	emailOld;
                habilitarSalir();
            }
            function setDomicilioAntiguo(){
                idDomOld = document.forms[0].txtIdDomicilio.value;
                <!--codUsoOld =	document.forms[0].codUso.value;-->
                <!--descUsoOld = document.forms[0].descUso.value;-->
                codPaisOld = document.forms[0].codPais.value;
                descPaisOld	= document.forms[0].descPais.value;
                codProvOld = document.forms[0].codProvincia.value;
                descProvOld	= document.forms[0].descProvincia.value;
                codMunOld =	document.forms[0].codMunicipio.value;
                descMunOld = document.forms[0].descMunicipio.value;
                codViaOld =	document.forms[0].txtCodVia.value;
                domicilioOld = document.forms[0].txtDomicilio.value;
                lugarOld = document.forms[0].txtBarriada.value;
                codPostalOld = document.forms[0].descPostal.value;
                numDesdeOld	= document.forms[0].txtNumDesde.value;
                letraDesdeOld = document.forms[0].txtLetraDesde.value;
                numHastaOld	= document.forms[0].txtNumHasta.value;
                letraHastaOld = document.forms[0].txtLetraHasta.value;
                bloqueOld =	document.forms[0].txtBloque.value;
                portalOld =	document.forms[0].txtPortal.value;
                escaleraOld	= document.forms[0].txtEsc.value;
                plantaOld =	document.forms[0].txtPlta.value;
                puertaOld =	document.forms[0].txtPta.value;
                normalizadoOld = document.forms[0].normalizado.checked;
                idViaOld = document.forms[0].idVia.value;
                /* anadir ECOESI */
                codECOOld=document.forms[0].codECO.value;
                codESIOld=document.forms[0].codESI.value;
                descESIOld=document.forms[0].descESI.value;
                /* fin anadir ECOESI */
                descViaOld=document.forms[0].descVia.value;
                codTVIOld = document.forms[0].codTVia.value;
                descTVIOld = document.forms[0].codTVia.value == '<%=CodigoSinTipoVia%>'?"":document.forms[0].descTVia.value;
            }
            function getDomicilioAntiguo(){
                document.forms[0].txtIdDomicilio.value = idDomOld;
                <!--document.forms[0].codUso.value = codUsoOld;-->
                <!--document.forms[0].descUso.value = descUsoOld;-->
                document.forms[0].codPais.value = codPaisOld;
                document.forms[0].descPais.value = descPaisOld;
                document.forms[0].codProvincia.value = codProvOld;
                document.forms[0].descProvincia.value = descProvOld;
                document.forms[0].codMunicipio.value = codMunOld;
                document.forms[0].descMunicipio.value = descMunOld;
                document.forms[0].txtCodVia.value =	codViaOld;
                document.forms[0].txtDomicilio.value = domicilioOld;
                document.forms[0].txtBarriada.value	= lugarOld;
                document.forms[0].descPostal.value = codPostalOld;
                document.forms[0].txtNumDesde.value	= numDesdeOld;
                document.forms[0].txtLetraDesde.value = letraDesdeOld;
                document.forms[0].txtNumHasta.value	= numHastaOld;
                document.forms[0].txtLetraHasta.value = letraHastaOld;
                document.forms[0].txtBloque.value =	bloqueOld;
                document.forms[0].txtPortal.value =	portalOld;
                document.forms[0].txtEsc.value = escaleraOld;
                document.forms[0].txtPlta.value = plantaOld;
                document.forms[0].txtPta.value = puertaOld;
                document.forms[0].normalizado.checked=normalizadoOld;
                document.forms[0].idVia.value =	idViaOld;
                /* anadir ECOESI */
                comboECO.buscaCodigo(codECOOld);
                document.forms[0].codESI.value=codESIOld;
                document.forms[0].descESI.value=descESIOld;
                document.forms[0].txtCodVia.value =	codViaOld;
                /* fin anadir ECOESI */
                document.forms[0].descVia.value=descViaOld;
                document.forms[0].codTVia.value = codTVIOld;
                document.forms[0].descTVia.value = codTVIOld == '<%=CodigoSinTipoVia%>'?"":descTVIOld;
            }
            function esPersonaFisica(){
                var pFJ	= document.forms[0].perFJ.value;
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

//** Esta funcion se debe implementar en cada JSP para particularizar  **//
//** las acciones a	realizar de las distintas combinaciones de teclas  **//
document.onmouseup = checkKeys;
function checkKeysLocal(tecla){
    if(tecla=="Alt+G"){
        if(document.getElementById("capaBotones2").style.visibility	== "visible"){
            pulsarGrabarTercero();
        }else	if(document.getElementById("capaBotones4").style.visibility	== "visible"){
        pulsarRegistrarModificar();
    }else	if(document.getElementById("capaBotones6").style.visibility	== "visible"){
    grabarDomicilioExt();
}else if(document.getElementById("capaBotones8").style.visibility == "visible"){
pulsarRegistrarModificarDomicilio();
}
}else if(tecla=="Alt+M"){
if(document.getElementById("capaBotones3").style.visibility	== "visible"){
    pulsarModificar();
}else if(document.getElementById("capaBotones9").style.visibility	== "visible"){
pulsarModificar();
}
}else if(tecla=="Alt+O"){
if(document.getElementById("capaBotones7").style.visibility	== "visible"){
    pulsarModificarDomicilio();
}
}else if(tecla=="Alt+C"){
if(document.getElementById("capaBotones2").style.visibility	== "visible"){
    pulsarCancelarAlta();
}else	if(document.getElementById("capaBotones3").style.visibility	== "visible"){
pulsarCancelarBuscar();
}else	if(document.getElementById("capaBotones4").style.visibility	== "visible"){
pulsarCancelarModificar();
}else if(document.getElementById("capaBotones6").style.visibility == "visible"){
pulsarCancelarAltaDomicilio();
}
}else if(tecla=="Alt+N"){
if(document.getElementById("capaBotones4").style.visibility	== "visible"){
    pulsarCancelarModificar();
}else if(document.getElementById("capaBotones6").style.visibility == "visible"){
pulsarCancelarAltaDomicilio();
}else if(document.getElementById("capaBotones8").style.visibility == "visible"){
pulsarCancelarModificarDomicilio();
}
}else if(tecla=="Alt+A"){
if(document.getElementById("capaBotones1").style.visibility	== "visible"){
    pulsarAlta();
}else	if(document.getElementById("capaBotones7").style.visibility	== "visible"){
pulsarAltaDomicilio();
}
}else if(tecla=="Alt+B"){
if(document.getElementById("capaBotones3").style.visibility	== "visible"){
    pulsarBaja();
}else if(document.getElementById("capaBotones9").style.visibility	== "visible"){
pulsarBaja();
}
}else if((tecla=="Alt+J")||(tecla=="Alt+X")){
if(document.getElementById("capaBotones7").style.visibility	== "visible"){
    pulsarBajaDomicilio();
}
}else if(tecla=="Alt+S"){
if(document.forms[0].cmdSalir.disabled==false)
    pulsarCancelar();
}
keyDel();
}

function cargarListaMunicipios(){
    document.forms[0].opcion.value="cargarMunicipios";
    document.forms[0].target=(ventana=="true")?"oculto":"oculto";
    document.forms[0].ventana.value=(ventana)?"true":"false";
    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
    document.forms[0].submit();
}

function cargarListaCodPostales(){
    document.forms[0].opcion.value="cargarCodPostales";
    if(ventana){
        document.forms[0].target="oculto";
        document.forms[0].ventana.value="true";
    }else
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do?codProvincia="+document.forms[0].codProvincia.value+"&codMunicipio="+document.forms[0].codMunicipio.value;
    document.forms[0].submit();
}
/***************************************************************************/
/*Deshabilita todos los	campos del formulario para no	poder	introducir datos*/
/***************************************************************************/
function deshabilitarBuscarDomicilio(){
    deshabilitarBoton(document.forms[0].cmdBuscarDomicilio);
}

function deshabilitarSalir(){
    //var vector = new Array(document.forms[0].cmdSalir);
    deshabilitarBoton(document.forms[0].cmdSalir);
}

function deshabilitarBuscar(){
    var vectorC =	["comboTipoDoc"];
    var vectorI =	["txtDNI"];
    habilitarGeneralCombos(vectorC,false);
    habilitarGeneralInputs(vectorI,false);
}

function deshabilitarTercero(){
    var vector = ["txtInteresado","txtApell1","txtPart","txtApell2","txtPart2",
        "txtTelefono","txtCorreo"];
    habilitarGeneralInputs(vector,false);
}

function deshabilitarDomicilio(){
    var vectorCombos = ["comboProvincia","comboMunicipio",
        "comboPostal"];
    var vectorInputs = ["txtCodVia","txtDomicilio","txtNumDesde","txtLetraDesde","txtNumHasta",
        "txtLetraHasta","txtBloque","txtPortal","txtEsc","txtPlta","txtPta","txtBarriada","txtKm",
        "txtHm"];
    var vectorP =	new Array();
    vectorP =[document.forms[0].botonV];

    deshabilitarImagen(vectorP,true);
    habilitarGeneralInputs(vectorInputs,false);
    habilitarGeneralCombos(vectorCombos,false);
    comboECO.deactivate();
    comboESI.deactivate();
    habilitarGeneralInputs(["codVia","descVia"],false);

}
<!--function deshabilitarUsoViviendas(){-->
    <!--var vector = ["comboUsoVivienda"];-->
    <!--habilitarGeneralCombos(vector,false);-->
<!--}-->
function deshabilitarCampos(){
    deshabilitarTercero();
    deshabilitarDomicilio();
}
/***************************************************************************/
/*Habilita todos los campos del forms[0] para	poder	introducir datos*/
/***************************************************************************/
function desHabilitarNavegacionDom(valor){
    if  (valor){
        deshabilitarBoton(document.forms[0].cmdAnterior);
        deshabilitarBoton(document.forms[0].cmdSiguiente);
    } else actualizaBotonSA();
}
function habilitarBuscarDomicilio(){
    habilitarBoton(document.forms[0].cmdBuscarDomicilio);
}
function habilitarSalir(){
    //var vector = new Array(document.forms[0].cmdSalir);
    habilitarBoton(document.forms[0].cmdSalir);
}
function recuperarModificar() {
    habilitarSalir();
    var dom = new Array();
    dom = Terceros[0][18];
    var v11 = Terceros[0][11];
    var v12 = Terceros[0][12];
    var v13 = Terceros[0][13];
    var v14 = Terceros[0][14];
    var v15 = Terceros[0][15];
    var v16 = Terceros[0][16];
    var v17 = Terceros[0][17];
    var v19 = Terceros[0][19];

    Terceros[0]	= [ document.forms[0].txtIdTercero.value,document.forms[0].txtVersion.value,
        document.forms[0].codTipoDoc.value,document.forms[0].txtDNI.value,
        document.forms[0].txtInteresado.value,document.forms[0].txtApell1.value,
        document.forms[0].txtApell2.value,document.forms[0].txtPart.value,
        document.forms[0].txtPart2.value,document.forms[0].txtTelefono.value,
        document.forms[0].txtCorreo.value,v11,v12,v13,v14,v15,v16,v17,dom,v19];

}
function habilitarBuscar(){
    var vectorC =	["comboTipoDoc"];
    var vectorI =	["txtDNI"];
    habilitarGeneralCombos(vectorC,true);
    habilitarGeneralInputs(vectorI,true);
}
function habilitarTercero(){
    var vector = ["txtInteresado","txtApell1","txtPart","txtApell2","txtPart2",
        "txtTelefono","txtCorreo"];
    habilitarGeneralInputs(vector,true);
}
function habilitarDomicilio(){
    var vectorCombos = ["comboProvincia","comboMunicipio",<!--"comboUsoVivienda",-->
        "comboPostal"];
    var vectorInputs = ["txtCodVia","txtDomicilio","txtNumDesde","txtLetraDesde","txtNumHasta",
        "txtLetraHasta","txtBloque","txtPortal","txtEsc","txtPlta","txtPta","txtBarriada","txtKm","txtHm"];
    var vectorP =	new Array();
    vectorP =[document.forms[0].botonV];

    deshabilitarImagen(vectorP,false);
    habilitarGeneralInputs(vectorInputs,true);
    habilitarGeneralCombos(vectorCombos,true);
    comboECO.activate();
    comboESI.activate();
    habilitarGeneralInputs(["codVia","descVia"],true);
}

//function habilitarUsoViviendas(){
//var vector = ["comboUsoVivienda"];
//habilitarGeneralCombos(vector,true);
//}

function habilitarCampos(){
    habilitarTercero();
    habilitarDomicilio();
}

function recuperaDatosIniciales(){
    codTipoDocs = [<%=lcodDocs%>];
        descTipoDocs =	[<%=ldescDocs%>];
            persFJ =[<%=lpersJF%>];
                codProvincias = [<%=lcodProv%>];
                    descProvincias	= [<%=ldescProv%>];
                        codMunicipios = [<%=lcodMun%>];
                            descMunicipios	= [<%=ldescMun%>];
                            codMunicipiosDefecto = [<%=lcodMun%>];
                            descMunicipiosDefecto = [<%=ldescMun%>];
                                <!--codUsoViviendas = [<%=lcodTOC%>];-->
                                    <!--descUsoViviendas = [<%=ldescTOC%>];-->
                                        codPostales = [<%=lcodCP%>];
                                            comboProvincia.addItems(codProvincias,descProvincias);
                                            comboMunicipio.addItems(codMunicipios,descMunicipios);
                                            comboTipoDoc.addItems(codTipoDocs,descTipoDocs);
                                            <!--comboUsoVivienda.addItems(codUsoViviendas,descUsoViviendas);-->
                                            comboPostal.addItems(codPostales,codPostales);
                                            /* anadir ECO/ESI */
                                            comboECO.addItems(codECOs,descECOs);
                                            comboESI.addItems(codESIs,descESIs);
                                            /* Fin anadir ECO/ESI */
                                        }

                                        function recuperaBusquedaTerceros(datos){
    this.pulsarBuscarTerceros = pulsarBuscarTercerosImpl;
                                            Terceros = datos;
                                            if (Terceros.length>0){
                                                cargarTercero(datos,"cmdBuscar");
                                                idTercero =	IndiceTercero();
        if (idTercero != -1) {
                                                var numDoc = Terceros[idTercero][3];
                                                comboTipoDoc.buscaCodigo(Terceros[idTercero][2]);
                                                document.forms[0].txtDNI.value = numDoc;
                                                var situacion = Terceros[idTercero][12];
                                                DomActual =	0;
                                                MAX=Terceros[idTercero][18].length;
                                                document.forms[0].cmdAnterior.disabled=true;
                                                document.forms[0].cmdSiguiente.disabled=true;
                                                if (MAX>0){
                                                    actualizaBotonSA();
                                                    cargarDomicilio(DomActual);
                                                    actualizarBotonesDom();
                                                }else{
                                                document.getElementById("capaBotones7").style.visibility = "visible";
                                                document.forms[0].cmdModificarDomicilio.disabled=true;
                                                document.forms[0].cmdBajaDomicilio.disabled=true;
                                            }
                                            deshabilitarCampos();
                                            deshabilitarBuscar();
                                            habilitarSalir();
                                            document.getElementById("capaBotones1").style.visibility = "hidden";
                                            document.getElementById("capaBotones2").style.visibility = "hidden";
                                            <% if ((inicioModificar.equals("si")) || (cargarTercero.equals("si"))) {%>
                                            if(Terceros[0][0] == "0"){
                                                document.getElementById("capaBotones2").style.visibility = "visible";
                                                document.getElementById("capaBotones3").style.visibility = "hidden";
                                            }
                                            else
                                                document.getElementById("capaBotones9").style.visibility = "visible";
                                            <% } else {%>
                                            document.getElementById("capaBotones3").style.visibility = "visible";
                                            <% }%>
                                            document.getElementById("capaBotones4").style.visibility = "hidden";
                                            if(situacion=="B"){
                                                if(jsp_alerta("",'<%=descriptor.getDescripcion("msjUsuarBaja")%>')){
                                                    document.forms[0].situacion.value="A";
                                                    document.forms[0].opcion.value="cambiaSituacionTercero";
                                                    if(ventana)
                                                        document.forms[0].target="oculto";
                                                    else document.forms[0].target="oculto";
                                                    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                                                    document.forms[0].submit();
                                                }else{
                                                pulsarCancelarBuscar();
                                                document.forms[0].txtDNI.focus();
                                            }
                                        }
                                    }else{
            pulsarCancelarBuscar();
            comboTipoDoc.buscaCodigo(0);
            document.forms[0].txtDNI.focus();

        }
    }else{
                                    jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoRegCoinc")%>');
                                        if (jsp_alerta("",'<%=descriptor.getDescripcion("msjNuevoInteresado")%>')) {
                                            pulsarAlta();
                                        } else {
                                        pulsarCancelarBuscar();
                                        document.forms[0].txtDNI.focus();
                                    }
                                }
                            }

                            function actualizaBotonSA(){
                                if (DomActual<=0){
                                    if(MAX==1){
                                        deshabilitarBoton(document.forms[0].cmdAnterior);
                                        deshabilitarBoton(document.forms[0].cmdSiguiente);
                                    }else{
                                    deshabilitarBoton(document.forms[0].cmdAnterior);
                                    habilitarBoton(document.forms[0].cmdSiguiente);
                                }
                            }else if (DomActual>=MAX-1){
                            habilitarBoton(document.forms[0].cmdAnterior);
                            deshabilitarBoton(document.forms[0].cmdSiguiente);
                        }else{
                        habilitarBoton(document.forms[0].cmdAnterior);
                        habilitarBoton(document.forms[0].cmdSiguiente);
                    }
                }

                function IndiceTercero(){
                    var i=0;
                    var id=document.forms[0].txtIdTercero.value;
                    for(i=0;i<Terceros.length;i++){
                        if (Terceros[i][0]==id)	break;
                    }

    if (i == Terceros.length) return -1;
                    return i;
                }

                function actualizarDomicilios(){
                    var nuevoDomicilio = [descPaisOld,descProvOld,descMunOld,domicilioOld,codPostalOld,idDomOld,
                        codPaisOld,	codProvOld,codMunOld,numDesdeOld,letraDesdeOld,numHastaOld,letraHastaOld,bloqueOld,
                        portalOld,escaleraOld,plantaOld,puertaOld,lugarOld,codTVIOld,descTVIOld,codUsoOld,descUsoOld,
                        codViaOld,normalizadoOld,idViaOld,codECOOld, codESIOld, descESIOld, descViaOld, 'SGE'];
                    var listaDomiciliosNueva = new Array();
                    for(i=0;i<MAX;i++){
                        listaDomiciliosNueva[i]	= Terceros[idTercero][18][i];
                    }
                    listaDomiciliosNueva[i]	= nuevoDomicilio;
                    MAX++;
                    Terceros[idTercero][18]=listaDomiciliosNueva;
                    DomActual=i;
                    actualizaBotonSA();
                    cargarDomicilio(DomActual);
                }

                function cargarDomicilio(i){
                    document.forms[0].txtIdDomicilio.value = Terceros[idTercero][18][i][5];
                    document.forms[0].codPais.value = Terceros[idTercero][18][i][6];
                    document.forms[0].descPais.value = Terceros[idTercero][18][i][0];
                    document.forms[0].codProvincia.value = Terceros[idTercero][18][i][7];
                    document.forms[0].descProvincia.value = Terceros[idTercero][18][i][1];
                    var vectorInputs = ["codProvincia"];
                    habilitarGeneralInputs(vectorInputs,true);
                    var aux=document.forms[0].codMunicipio.value;
                    var aux2=document.forms[0].descMunicipio.value;
                    var aux3=document.forms[0].codVia.value;
                    var aux4=document.forms[0].descVia.value;
                    comboProvincia.change();
                    document.forms[0].codMunicipio.value=aux;
                    document.forms[0].descMunicipio.value=aux2;
                    document.forms[0].codVia.value=aux3;
                    document.forms[0].descVia.value=aux4;
                    habilitarGeneralInputs(vectorInputs,false);
                    document.forms[0].codMunicipio.value = Terceros[idTercero][18][i][8];
                    document.forms[0].descMunicipio.value = Terceros[idTercero][18][i][2];
                    document.forms[0].txtDomicilio.value = Terceros[idTercero][18][i][3];
                    document.forms[0].descPostal.value = Terceros[idTercero][18][i][4];
                    document.forms[0].txtNumDesde.value	= Terceros[idTercero][18][i][9]== 0	? "": Terceros[idTercero][18][i][9];
                    document.forms[0].txtLetraDesde.value = Terceros[idTercero][18][i][10];
                    document.forms[0].txtNumHasta.value	= Terceros[idTercero][18][i][11]== 0 ? "": Terceros[idTercero][18][i][11];
                    document.forms[0].txtLetraHasta.value = Terceros[idTercero][18][i][12];
                    document.forms[0].txtBloque.value =	Terceros[idTercero][18][i][13];
                    document.forms[0].txtPortal.value =	Terceros[idTercero][18][i][14];
                    document.forms[0].txtEsc.value = Terceros[idTercero][18][i][15];
                    document.forms[0].txtPlta.value = Terceros[idTercero][18][i][16];
                    document.forms[0].txtPta.value = Terceros[idTercero][18][i][17];
                    document.forms[0].txtBarriada.value	= Terceros[idTercero][18][i][18];
                    document.forms[0].codTVia.value = Terceros[idTercero][18][i][19];
                    document.forms[0].descTVia.value = Terceros[idTercero][18][i][19] == '<%=CodigoSinTipoVia%>' ? "": Terceros[idTercero][18][i][20];
                    <!--document.forms[0].codUso.value = Terceros[idTercero][18][i][21]==	0 ? "": Terceros[idTercero][18][i][21];-->
                    <!--document.forms[0].descUso.value = Terceros[idTercero][18][i][22];-->
                    document.forms[0].txtCodVia.value =	Terceros[idTercero][18][i][23];
                    document.forms[0].normalizado.checked = Terceros[idTercero][18][i][24]== 1 ? true: false;
                    document.forms[0].idVia.value =	Terceros[idTercero][18][i][25];
                    if(document.forms[0].normalizado.checked)
                        domlay("capaNormalizado",1,0,0,null);
                    else domlay("capaNormalizado",0,0,0,null);
                    /* Anadir ECOESI */
                    var codigoECO =	Terceros[idTercero][18][i][26];
                    var codigoESI =	Terceros[idTercero][18][i][27];
                    var descripcionESI =	Terceros[idTercero][18][i][28];
                    if( (document.forms[0].codMunicipio.value=="<%=ptVO.getMunicipio()%>") &&  ( document.forms[0].codProvincia.value=="<%=ptVO.getProvincia()%>")){

                        if (codigoECO!="" && codigoESI!="" && codigoESI!="null"){
                            comboECO.buscaCodigo(codigoECO);
                            //comboESI.buscaCodigo(codigoESI);
                            document.forms[0].codESI.value=codigoESI;
                            document.forms[0].descESI.value=descripcionESI;
                        } else {
                        comboECO.selectItem(-1);
                        comboESI.selectItem(-1);
                    }
                } else {
                comboECO.selectItem(-1);
                comboESI.selectItem(-1);
            }
            document.forms[0].txtCodVia.value =	Terceros[idTercero][18][i][23];
            if (document.forms[0].txtCodVia.value!='') {
                document.forms[0].codVia.value =	Terceros[idTercero][18][i][23];
                document.forms[0].descVia.value =	Terceros[idTercero][18][i][29];
            } else {
            document.forms[0].codVia.value =	"";
            document.forms[0].descVia.value =	"";
            document.forms[0].txtCodVia.value =	"";
            document.forms[0].idVia.value = "";
        }

        /* Fin anadir ECOESI */
        document.forms[0].txtBarriada.value	= Terceros[idTercero][18][i][18];
        etiq="Domicilio ("+(DomActual+1)+"/"+MAX+")";
        domlay("domis",1,0,0,etiq);
    }
    /*
    function cargarDomicilioSinComboMun(i){
        document.forms[0].txtIdDomicilio.value = Terceros[idTercero][18][i][5];
        document.forms[0].codPais.value = Terceros[idTercero][18][i][6];
        document.forms[0].descPais.value = Terceros[idTercero][18][i][0];
        document.forms[0].codProvincia.value = Terceros[idTercero][18][i][7];
        document.forms[0].descProvincia.value = Terceros[idTercero][18][i][1];
        document.forms[0].codMunicipio.value = Terceros[idTercero][18][i][8];
        document.forms[0].descMunicipio.value = Terceros[idTercero][18][i][2];
        document.forms[0].txtDomicilio.value = Terceros[idTercero][18][i][3];
        document.forms[0].descPostal.value = Terceros[idTercero][18][i][4];
        document.forms[0].txtNumDesde.value	= Terceros[idTercero][18][i][9]== 0	? "": Terceros[idTercero][18][i][9];
        document.forms[0].txtLetraDesde.value = Terceros[idTercero][18][i][10];
        document.forms[0].txtNumHasta.value	= Terceros[idTercero][18][i][11]== 0 ? "": Terceros[idTercero][18][i][11];
        document.forms[0].txtLetraHasta.value = Terceros[idTercero][18][i][12];
        document.forms[0].txtBloque.value =	Terceros[idTercero][18][i][13];
        document.forms[0].txtPortal.value =	Terceros[idTercero][18][i][14];
        document.forms[0].txtEsc.value = Terceros[idTercero][18][i][15];
        document.forms[0].txtPlta.value = Terceros[idTercero][18][i][16];
        document.forms[0].txtPta.value = Terceros[idTercero][18][i][17];
        document.forms[0].txtBarriada.value	= Terceros[idTercero][18][i][18];
        document.forms[0].codTVia.value = Terceros[idTercero][18][i][19];
        document.forms[0].descTVia.value = Terceros[idTercero][18][i][19] == '<%=CodigoSinTipoVia%>' ? "": Terceros[idTercero][18][i][20];
        document.forms[0].txtCodVia.value =	Terceros[idTercero][18][i][23];
        document.forms[0].normalizado.checked = Terceros[idTercero][18][i][24]== 1 ? true: false;
        document.forms[0].idVia.value =	Terceros[idTercero][18][i][25];
        if(document.forms[0].normalizado.checked)
            domlay("capaNormalizado",1,0,0,null);
        else domlay("capaNormalizado",0,0,0,null);
        var codigoECO =	Terceros[idTercero][18][i][26];
        var codigoESI =	Terceros[idTercero][18][i][27];
        var descripcionESI =	Terceros[idTercero][18][i][28];
        if( (document.forms[0].codMunicipio.value=="<%=ptVO.getMunicipio()%>") &&  ( document.forms[0].codProvincia.value=="<%=ptVO.getProvincia()%>")){

            if (codigoECO!="" && codigoESI!="" && codigoESI!="null"){
                comboECO.buscaCodigo(codigoECO);
                document.forms[0].codESI.value=codigoESI;
                document.forms[0].descESI.value=descripcionESI;
            } else {
            comboECO.selectItem(-1);
            comboESI.selectItem(-1);
        }
    } else {
    comboECO.selectItem(-1);
    comboESI.selectItem(-1);
}
document.forms[0].txtCodVia.value =	Terceros[idTercero][18][i][23];
if (document.forms[0].txtCodVia.value!='') {
    document.forms[0].codVia.value =	Terceros[idTercero][18][i][23];
    document.forms[0].descVia.value =	Terceros[idTercero][18][i][29];
} else {
document.forms[0].codVia.value =	"";
document.forms[0].descVia.value =	"";
document.forms[0].txtCodVia.value =	"";
document.forms[0].idVia.value = "";
}

document.forms[0].txtBarriada.value	= Terceros[idTercero][18][i][18];
etiq="Domicilio ("+(DomActual+1)+"/"+MAX+")";
domlay("domis",1,0,0,etiq);
}
*/
function limpiarDomicilio(){
    var vectorCamposDomicilio	= ["idVia","txtIdDomicilio",<!--"codUso","descUso",-->
        "codPais","descPais","codProvincia","descProvincia","codMunicipio",
        "descMunicipio"
        ,"txtCodVia","txtDomicilio",
        "descPostal","txtNumDesde","txtLetraDesde","txtNumHasta","txtLetraHasta",
        "txtBloque","txtPortal","txtEsc","txtPlta","txtPta","txtBarriada",
        "txtKm","txtHm"];
    limpiar(vectorCamposDomicilio);
    // anadir ECOESI
    document.forms[0].txtCodVia.value =	"";
    document.forms[0].codVia.value = "";
    document.forms[0].descVia.value = "";
    document.forms[0].idVia.value = "";
    comboECO.selectItem(-1);
    comboESI.selectItem(-1);
    // fin anadir ECOESI
    document.forms[0].normalizado.checked=false;
    domlay("domis",0,0,0,etiq);
    valoresPorDefecto();
}

function limpiarTercero(){
    var vectorCamposTercero =	["txtIdTercero","situacion","txtVersion","txtDNI",
        "txtInteresado","txtApell1","txtPart","txtApell2","txtPart2","txtTelefono",
        "txtCorreo"]
    limpiar(vectorCamposTercero);
}

function limpiarTodo(){
    MAX = 0;
    limpiarTercero();
    limpiarDomicilio();
}

function limpiaVial(){
    var codVia =	document.forms[0].txtCodVia.value;
    if (codVia!=""){
        document.forms[0].txtCodVia.value =	"";
        document.forms[0].descVia.value = "";
        document.forms[0].idVia.value =	"";

    }
}

function limpiaCodPostal(){
    document.forms[0].descPostal.value = "";
}

// ------ Funciones de página
var ventana=false;

function inicializar() {
    var destino	= "<%=request.getParameter("destino")%>";
    var tipo = "<%=request.getParameter("tipo")%>";
    if (tipo!='B') {
        var tDoc = tipo.charAt(0);
        var t = tipo.charAt(1);
    } else
    var t = tipo;
    var descDoc	= "<%=request.getParameter("descDoc")%>";
    var docu = "<%=request.getParameter("docu")%>";
    var nombre = "<%=request.getParameter("nombre")%>";
    var apell1 = "<%=request.getParameter("apell1")%>";
    var apell2 = "<%=request.getParameter("apell2")%>";
    var codTerc = "<%=request.getParameter("codTerc")%>";
    recuperaDatosIniciales();
    comboTipoDoc.buscaCodigo(codTipoDocs[1]); // Remiendo para que se desactive txtDNI
    comboTipoDoc.buscaCodigo(codTipoDocs[0]);
    <!--valorPorDefecto("codTipoDoc","descTipoDoc",codTipoDocs,descTipoDocs);-->
    document.getElementById("capaBotones1").style.visibility = "visible";
    <!--document.forms[0].cmdBuscarDomicilio.disabled=true;-->
    document.forms[0].normalizado.disabled=true;
    document.forms[0].cmdAnterior.disabled=true;
    document.forms[0].cmdSiguiente.disabled=true;
    <% if (inicioAlta.equals("si") || inicioModificar.equals("si") || cargarTercero.equals("si")) {%>
    destino = "";
    <% }%>
    if((destino!="")&&(destino!="null")){
        ventana=true;
        document.forms[0].ventana.value="true";
        pleaseWait1("off",top.mainFrame);
        if(tipo=="NoAlta"){
            document.forms[0].txtDNI.focus();
            limpiarTodo();
            deshabilitarCampos();
        }else if(t=="B"){
        <!--document.forms[0].codTipoDoc.value = tDoc;-->
        <!--document.forms[0].descTipoDoc.value = descDoc;-->
        comboTipoDoc.buscaCodigo(tDoc);
        if (docu!="null") {
            document.forms[0].txtDNI.value = docu;
        }
        if (nombre!="null") {
            document.forms[0].txtInteresado.value = nombre;
        }
        if (apell1!="null") {
            document.forms[0].txtApell1.value = apell1;
        }
        if (apell2!="null") {
            document.forms[0].txtApell2.value = apell2;
        }
        if (codTerc!="null") {
            document.forms[0].codTerc.value = codTerc;
        }
        pulsarBuscar();
    }else{
    <!--document.forms[0].codTipoDoc.value = tipo;-->
    <!--document.forms[0].descTipoDoc.value = descDoc;-->
    comboTipoDoc.buscaCodigo(tDoc);
    pulsarAlta();
    if (docu!="null") {
        document.forms[0].txtDNI.value = docu;
    }
    if (nombre!="null") {
        document.forms[0].txtInteresado.value = nombre;
    }
    if (apell1!="null") {
        document.forms[0].txtApell1.value = apell1;
    }
    if (apell2!="null") {
        document.forms[0].txtApell2.value = apell2;
    }
    deshabilitarDomicilio();
}
}else {
pleaseWait1("off",top.mainFrame);
document.forms[0].ventana.value="false";
document.forms[0].txtDNI.focus();
limpiarTodo();
deshabilitarCampos();
}
<% if (inicioAlta.equals("si") || inicioAltaDesdeBuscar.equals("si")) {%>
    pulsarAlta();
    <% }%>
    <% if (inicioModificar.equals("si")) {%>
    var args = self.parent.opener.xanelaAuxiliarArgs;
    deshabilitarBuscar();
    recuperaBusquedaTerceros(args);
    <% }%>
    <% if (cargarTercero.equals("si")) {
                TercerosValueObject tercero = (TercerosValueObject) bForm.getListaTerceros().get(0);%>
                    var _terceros = new Array();
                    // Construimos un vector con los mismos campos que los esperados en la funcion
                    // cargarTercero en listaBusquedaTerceros.js
                    var dom = new Array();
                    _terceros[0] = ["<%=tercero.getIdentificador()%>","<%=tercero.getVersion()%>",
                        "<%=tercero.getTipoDocumento()%>","<%=tercero.getDocumento()%>",
                        "<%=tercero.getNombre()%>","<%=tercero.getApellido1()%>",
                        "<%=tercero.getApellido2()%>","<%=tercero.getPartApellido1()%>",
                        "<%=tercero.getPartApellido2()%>","<%=tercero.getTelefono()%>",
                        "<%=tercero.getEmail()%>","<%=tercero.getNormalizado()%>",
                        "<%=tercero.getSituacion()%>","<%=tercero.getFechaAlta()%>",
                        "<%=tercero.getUsuarioAlta()%>","<%=tercero.getModuloAlta()%>",
                        "<%=tercero.getFechaBaja()%>","<%=tercero.getUsuarioBaja()%>",dom,"<%=tercero.getOrigen()%>"];

                        deshabilitarBuscar();
                        recuperaBusquedaTerceros(_terceros);
                        <% }%>
                    }

                    function pulsarAlta(){
                        //limpiarTodo();
                        if (document.forms[0].codTipoDoc.value == <%=m_Conf.getString("tercero.codUOR")%>) {
                            jsp_alerta("A",'<%=descriptor.getDescripcion("msjTercUOR")%>');
                            } else {
                            registrando=true;
                            document.forms[0].grabarDirecto.value="no";
                            habilitarBuscar();
                            habilitarTercero();
                            actualizaPersonaFJ();
                            <!--document.forms[0].cmdBuscar.disabled = true;-->
                            document.getElementById("capaBotones2").style.visibility = "visible";
                            document.getElementById("capaBotones1").style.visibility = "hidden";
                        }
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
                        /* fin anadir ECOESI */
                        paisOld	= document.forms[0].codPais.value;
                        provinciaOld = document.forms[0].codProvincia.value;
                        municipioOld = document.forms[0].codMunicipio.value;
                    }

                    function pulsarAltaDomicilio(){
                        if ((Tercero[idTercero]!=null) && (Tercero[idTercero][0] == "0")) {
                            if (jsp_alerta("",'<%=descriptor.getDescripcion("msjDatosExtGrabar")%>'))
                                pulsarGrabarTercero();
                            } else {
                            limpiarDomicilio();
                            comboProvincia.selectItem(-1);
                            deshabilitarSalir();
                            desHabilitarNavegacionDom(true);
                            //habilitarBuscarDomicilio();
                            habilitarDomicilio();
                            document.forms[0].normalizado.checked=false;
                            valoresPorDefecto();
                            document.getElementById("capaBotones6").style.visibility = "visible";
                            document.getElementById("capaBotones7").style.visibility = "hidden";
                            document.getElementById("capaBotones10").style.visibility = "hidden";

                            var aux=document.forms[0].codMunicipio.value;
                            var aux2=document.forms[0].descMunicipio.value;
                            comboProvincia.change();
                            document.forms[0].codMunicipio.value=aux;
                            document.forms[0].descMunicipio.value=aux2;
                            auxCombo = "comboPostal";
                            cargarComboBox(codPostales,codPostales);
                        }
                    }

                    function pulsarBaja()
                    {
                        if ((Tercero[0]!=null) && (Tercero[0][0] == "0"))
                            //TODO:Añadir a la base de datos esta frase
                        jsp_alerta("A","Es de una busqueda externa. No se puede dar de Baja");
                        else{
                            if(jsp_alerta("",'<%=descriptor.getDescripcion("msjDarBaja")%>' + " " +
                                document.forms[0].txtInteresado.value + " " + document.forms[0].txtApell1.value + " " +
                                document.forms[0].txtApell2.value + " con DNI: " + document.forms[0].txtDNI.value))
                            {
                                document.forms[0].situacion.value="B";
                                document.forms[0].opcion.value="cambiaSituacionTercero";
                                habilitarBuscar();
                                document.forms[0].target="oculto";
                                document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                                document.forms[0].submit();
                                deshabilitarBuscar();
                                document.forms[0].eliminarTercero.value="si";
                                // Se llama a cerrar desde ocultoBusqueda al volver de borrar al tercero.
                                //cerrar();
                            }
                        }
                    }




function pulsarGrabarTercero() {
    var tipo = document.forms[0].codTipoDoc.value;

	if (tipo != <%=m_Conf.getString("tercero.codUOR")%>) {
	//if ((validarTercero())&&(!documentoNoValido("codTipoDoc","txtDNI",1)))
	if (validarTercero())
	{
		if((Tercero[0]!=null) && (Tercero[0][0] == "0"))
			document.forms[0].opcion.value="grabarTerceroExt";
		else {
			document.forms[0].opcion.value="grabarTercero";
		}
		habilitarBuscar();
		habilitarTercero(); //Modificado
		if(ventana) document.forms[0].target="oculto";
		else document.forms[0].target="oculto";
		document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
		document.forms[0].submit();
		deshabilitarBuscar();
		}
	} else {
		jsp_alerta("A",'<%=descriptor.getDescripcion("msjTercUOR")%>');
	}
}

function confirmarDocDuplicado() {
    if (jsp_alerta("", "<%=descriptor.getDescripcion("msjTerDocRepetido")%>")) {
       document.forms[0].opcion.value="grabarTerceroConfirmado";
       habilitarBuscar();
       habilitarTercero();
       document.forms[0].target="oculto";
       document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
       document.forms[0].submit();
       deshabilitarBuscar();
    } else {
        habilitarBuscar();
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

                            function recargaDatosTercero(numVersion, codTipoDoc, descTipoDoc, numDoc, nombre, apellido1, apellido2, partApe1, partApe2, telefono, correo) {
                                document.forms[0].txtVersion.value = numVersion;
                                document.forms[0].codTipoDoc.value = codTipoDoc;
                                document.forms[0].descTipoDoc.value = descTipoDoc;
                                document.forms[0].txtDNI.value = numDoc;
                                document.forms[0].txtInteresado.value = nombre;
                                document.forms[0].txtApell1.value = apellido1;
                                document.forms[0].txtPart.value = partApe1;
                                document.forms[0].txtApell2.value = apellido2;
                                document.forms[0].txtPart2.value = partApe2;
                                document.forms[0].txtTelefono.value = telefono;
                                document.forms[0].txtCorreo.value = correo;
                            }

                            function quitarBotones()
                            {
                                document.getElementById("capaBotones9").style.visibility = "hidden";
                                document.getElementById("capaBotones3").style.visibility = "hidden";
                                document.getElementById("capaBotones7").style.visibility = "hidden";

                            }

                            function actualizarBotonesDom() {
                                if (document.forms[0].txtIdDomicilio.value=="0") {
                                    document.getElementById("capaBotones7").style.visibility = "hidden";
                                    document.getElementById("capaBotones8").style.visibility = "hidden";
                                    document.getElementById("capaBotones10").style.visibility = "visible";
                                } else {
                                document.getElementById("capaBotones7").style.visibility = "visible";
                                document.getElementById("capaBotones8").style.visibility = "hidden";
                                document.getElementById("capaBotones10").style.visibility = "hidden";
                            }
                        }

                        function grabado(id){
                            if (id!=0){
                                document.forms[0].txtIdTercero.value=id;
                                document.forms[0].txtVersion.value="1";
                                deshabilitarTercero();
                                document.forms[0].cmdModificarDomicilio.disabled=true;
                                document.forms[0].cmdBajaDomicilio.disabled=true;
                                <!--document.forms[0].cmdBuscar.disabled = false;-->
                                document.getElementById("capaBotones2").style.visibility = "hidden";
                                <% if (inicioModificar.equals("si")) {%>
                                document.getElementById("capaBotones9").style.visibility = "visible";
                                <% } else {%>
                                document.getElementById("capaBotones3").style.visibility = "visible";
                                <% }%>
                                actualizarBotonesDom();
                                setTerceroAntiguo();
                                idTercero =	0;
                                if ((Terceros[0]!=null) && (Terceros[0][18] != null))
                                    Terceros[0]	= [id,"1",codTipoDocOld,documentoOld,nombreOld,apellido1Old,
                                        apellido2Old,parAp1Old,parAp2Old,telfOld,emailOld,"1","A","","","","","",Terceros[0][18],"SGE"];
                                    else
                                        Terceros[0]	= [id,"1",codTipoDocOld,documentoOld,nombreOld,apellido1Old,
                                            apellido2Old,parAp1Old,parAp2Old,telfOld,emailOld,"1","A","","","","","",new Array(),"SGE"];
                                    }else{
                                    var tipoDoc	= document.forms[0].codTipoDoc.value;
                                    if(tipoDoc==0){
                                        jsp_alerta("A",'<%=descriptor.getDescripcion("msjEncontDup")%>');
                                            var	source = "<%=request.getContextPath()%>/jsp/terceros/seleccionSinDocumento.jsp?opcion=nada";
                                            var	argumentos = new Array();
                                            argumentos = [[document.forms[0].codTipoDoc.value,
                                                document.forms[0].txtInteresado.value,document.forms[0].txtApell1.value,
                                                document.forms[0].txtApell2.value],"TERCEROS"];
                                            abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source="+source,argumentos,
                                                    'width=700,height=450,status='+ '<%=statusBar%>',function(datos){
                                                            if(datos!=undefined){
                                                                if(datos[0]=="NUEVO"){
                                                                    var tipo = document.forms[0].codTipoDoc.value;
                                                                    if ((validarTercero())&&(!documentoNoValido("codTipoDoc","txtDNI",1))){
                                                                        document.forms[0].opcion.value="grabarTerceroDuplicado";
                                                                        habilitarBuscar();
                                                                        if(ventana)
                                                                            document.forms[0].target="oculto";
                                                                        else document.forms[0].target="oculto";
                                                                        document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                                                                        document.forms[0].submit();
                                                                        deshabilitarBuscar();
                                                                    }
                                                                }else if(datos.length==2)
                                                                recuperaBusquedaTerceros(datos[1]);
                                                            }
                                                    });
                                            }else
                                            jsp_alerta("A",'<%=descriptor.getDescripcion("msjTercDuplic")%>');
                                            }
                                        }

                                        function domicilioGrabado(id){
                                            if (id!=0){
                                                document.forms[0].txtIdDomicilio.value=id;
                                                document.forms[0].cmdModificarDomicilio.disabled=false;
                                                document.forms[0].cmdBajaDomicilio.disabled=false;
                                                document.getElementById("capaBotones6").style.visibility = "hidden";
                                                document.getElementById("capaBotones7").style.visibility = "visible";
                                                document.getElementById("capaBotones10").style.visibility = "hidden";
                                                deshabilitarDomicilio();
                                                setDomicilioAntiguo();
                                                actualizarDomicilios();
                                                desHabilitarNavegacionDom(false);
                                                habilitarSalir();
                                            }else{
                                        }
                                    }
                                    /*
                                    function domicilioGrabadoExt(id, domActual){
                                    document.forms[0].cmdModificarDomicilio.disabled=false;
                                    document.forms[0].cmdBajaDomicilio.disabled=false;
                                    document.getElementById("capaBotones7").style.visibility = "visible";
                                    document.getElementById("capaBotones8").style.visibility = "hidden";
                                    Terceros[idTercero][18][domActual][5] = id;
                                    if (id!=0){
                                    Terceros[idTercero][18][domActual][30] = "A";
                                    }
                                    }
                                    */
                                    function domicilioGrabadoExtTercAgora(id){
                                        document.forms[0].txtIdDomicilio.value=id;
                                        document.forms[0].cmdModificarDomicilio.disabled=false;
                                        document.forms[0].cmdBajaDomicilio.disabled=false;
                                        domicilioModificado("domicilioModificado");
                                        if (id!=0) {
                                            Terceros[idTercero][18][DomActual][30] = "SGE";
                                        }
                                    }
                                    /*
                                    function grabarDomicilioExt(){

                                        DomActual = 0;
                                        cargarDomicilioSinComboMun(DomActual);
                                        var domActual = 0;
                                        var dom = 0;
                                        var hayMasDomicilios = true;
                                        while (hayMasDomicilios)
                                            {
                                                if (jsp_alerta("B","¿Desea grabar este domicilio?")){
                                                    if (validarDomicilio()){
                                                        habilitarDomicilio();
                                                        document.forms[0].txtNormalizado.value = document.forms[0].normalizado.checked ? "1":"2";
                                                        if(ventana){
                                                            document.forms[0].ventana.value="true";
                                                            document.forms[0].target="oculto";
                                                        }else{
                                                        document.forms[0].ventana.value="false";
                                                        document.forms[0].target="oculto";
                                                    }
                                                    document.forms[0].opcion.value="grabarDomicilioExt";
                                                    document.forms[0].domActual.value=DomActual;
                                                    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                                                    document.forms[0].submit();
                                                    deshabilitarDomicilio();
                                                } else {
                                                jsp_alerta("A",'<%=descriptor.getDescripcion("msjDomNoGrab")%>');
                                                }
                                            }
                                            domActual = DomActual;
                                            pulsarSiguienteDom();
                                            dom = DomActual;
                                            if (dom>domActual) {
                                                cargarDomicilioSinComboMun(dom);
                                                if (!(dom <= (MAX -1))) {
                                                    hayMasDomicilios = false;
                                                }
                                            } else {
                                            document.getElementById("capaBotones7").style.visibility = "visible";
                                            document.getElementById("capaBotones8").style.visibility = "hidden";
                                            deshabilitarDomicilio();
                                            desHabilitarNavegacionDom(false);
                                            habilitarSalir();
                                            hayMasDomicilios = false;
                                        }
                                    }
                                }
                                */
                                function pulsarCancelarAlta()	{
                                    limpiarTodo();
                                    habilitarBuscar();
                                    deshabilitarTercero();
                                    habilitarSalir();
                                    <!--document.forms[0].txtDNI.focus();-->
                                    document.getElementById("capaBotones1").style.visibility = "visible";
                                    document.getElementById("capaBotones2").style.visibility = "hidden";
                                }

                                function pulsarCancelarAltaDomicilio() {
                                    limpiarDomicilio();
                                    comboProvincia.selectItem(-1);
                                    if(MAX>0)
                                        if (Terceros[idTercero][18].length>0){
                                            //Forzar a que el combo haga un change para que recargue bien los valores
                                            //cargarDomicilio(DomActual);
                                            var vectorInputs = ["codProvincia"];
                                            //habilitarGeneralInputs(vectorInputs,true);
                                            //comboProvincia.change();
                                            cargarDomicilio(DomActual);
                                            habilitarGeneralInputs(vectorInputs,false);
                                        }
                                        deshabilitarDomicilio();
                                        //deshabilitarBuscarDomicilio();
                                        desHabilitarNavegacionDom(false);
                                        habilitarSalir();
                                        actualizarBotonesDom();
                                    }

                                    function pulsarRegistrarModificar()	{
                                        var tipo = document.forms[0].codTipoDoc.value;
                                        if ((validarTercero())&&(!documentoNoValido("codTipoDoc","txtDNI",1)))
                                            {
                                                if (!cambioEnDocumento()) {
                                                    <% if (inicioModificar.equals("si")) {%>
                                                    document.getElementById("capaBotones9").style.visibility = "visible";
                                                    <% } else {%>
                                                    document.getElementById("capaBotones3").style.visibility = "visible";
                                                    <% }%>
                                                    document.getElementById("capaBotones4").style.visibility = "hidden";
                                                    if ((Tercero[0]!=null) && (Tercero[0][0] == "0")){
                                                        //TODO:Añadir a la base de datos esta frase
                                                        if(!jsp_alerta("","Es de una busqueda externa. Se Insertara no se Modificara ")){
                                                            pulsarCancelarModificar();
                                                            return false;
                                                        }
                                                    }
                                                    if (document.forms[0].codTipoDoc.value==codTipoDocOld)
                                                        document.forms[0].insertarDoc.value="no";
                                                    else document.forms[0].insertarDoc.value="si";
                                                    document.forms[0].opcion.value="modificarTercero";
                                                    habilitarBuscar();
                                                    if(ventana)
                                                        document.forms[0].target="oculto";
                                                    else document.forms[0].target="oculto";
                                                    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                                                    document.forms[0].submit();
                                                } else {
                                                jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoCambiarDoc")%>');
                                                    pulsarCancelarModificar();
                                                    return false;
                                                }
                                            }
                                            deshabilitarTercero();
                                            deshabilitarBuscar();
                                        }


                                        function pulsarRegistrarModificarDomicilio(){
                                            if ((Tercero[idTercero]!=null) && (Tercero[idTercero][0] == "0")) {
                                                if (jsp_alerta("",'<%=descriptor.getDescripcion("msjDatosExtGrabar")%>'))
                                                    pulsarGrabarTercero();
                                                } else {
                                                if (validarDomicilio()){
                                                    if (viaBuscada()) {
                                                        habilitarDomicilio();
                                                        document.forms[0].txtNormalizado.value = document.forms[0].normalizado.checked ? "1":"2";
                                                        if (document.forms[0].txtIdDomicilio.value=="0") {
                                                            document.forms[0].opcion.value="grabarDomicilioExtTercAgora"
                                                        } else if((Tercero[idTercero]!=null) &&
                                                            (Tercero[idTercero][DomActual]!=null && Tercero[idTercero][DomActual][5]=="0"))
                                                        document.forms[0].opcion.value="grabarDomicilio"
                                                        else document.forms[0].opcion.value="modificarDomicilio";
                                                        if(ventana)
                                                            document.forms[0].target="oculto";
                                                        else document.forms[0].target="oculto";
                                                        document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                                                        document.forms[0].submit();
                                                    } else jsp_alerta("A",'<%=descriptor.getDescripcion("msjViaNoBusc")%>');
                                                    } else {
                                                    habilitarDomicilio();
                                                }
                                            }
                                        }

                                        function domicilioModificado(resultado) {
                                            if ("domicilioModificado"== resultado) {
                                                var i = DomActual;
                                                Terceros[idTercero][18][i][5] = document.forms[0].txtIdDomicilio.value ;
                                                Terceros[idTercero][18][i][6] = document.forms[0].codPais.value ;
                                                Terceros[idTercero][18][i][0] = document.forms[0].descPais.value;
                                                Terceros[idTercero][18][i][7] = document.forms[0].codProvincia.value;
                                                Terceros[idTercero][18][i][1] = document.forms[0].descProvincia.value;
                                                Terceros[idTercero][18][i][8] = document.forms[0].codMunicipio.value;
                                                Terceros[idTercero][18][i][2] = document.forms[0].descMunicipio.value;
                                                Terceros[idTercero][18][i][3] = document.forms[0].txtDomicilio.value;
                                                Terceros[idTercero][18][i][4] = document.forms[0].descPostal.value;
                                                Terceros[idTercero][18][i][9] = document.forms[0].txtNumDesde.value;
                                                Terceros[idTercero][18][i][10] = document.forms[0].txtLetraDesde.value;
                                                Terceros[idTercero][18][i][11] = document.forms[0].txtNumHasta.value;
                                                Terceros[idTercero][18][i][12] = document.forms[0].txtLetraHasta.value;
                                                Terceros[idTercero][18][i][13] = document.forms[0].txtBloque.value;
                                                Terceros[idTercero][18][i][14] = document.forms[0].txtPortal.value;
                                                Terceros[idTercero][18][i][15] = document.forms[0].txtEsc.value;
                                                Terceros[idTercero][18][i][16] = document.forms[0].txtPlta.value;
                                                Terceros[idTercero][18][i][17] = document.forms[0].txtPta.value;
                                                Terceros[idTercero][18][i][18] = document.forms[0].txtBarriada.value;
                                                Terceros[idTercero][18][i][19] = document.forms[0].codTVia.value;
                                                Terceros[idTercero][18][i][20] = document.forms[0].codTVia.value == '<%=CodigoSinTipoVia%>'?"":document.forms[0].descTVia.value;
                                                Terceros[idTercero][18][i][21] = "";<!--document.forms[0].codUso.value ;-->
                                                Terceros[idTercero][18][i][22] = "";<!--document.forms[0].descUso.value;-->
                                                Terceros[idTercero][18][i][23] = document.forms[0].codVia.value;
                                                Terceros[idTercero][18][i][24] = (document.forms[0].normalizado.checked?1:0);
                                                Terceros[idTercero][18][i][25] = document.forms[0].idVia.value ;
                                                Terceros[idTercero][18][i][26] = document.forms[0].codECO.value;
                                                Terceros[idTercero][18][i][27] = document.forms[0].codESI.value;
                                                Terceros[idTercero][18][i][28] = document.forms[0].descESI.value;
                                                Terceros[idTercero][18][i][29] = document.forms[0].descVia.value;
                                            } else {
                                            jsp_alerta("A",'<%=descriptor.getDescripcion("msjDomNGrab")%>');
                                                cargarDomicilio(domActual);
                                            }
                                            document.getElementById("capaBotones7").style.visibility = "visible";
                                            document.getElementById("capaBotones8").style.visibility = "hidden";
                                            document.getElementById("capaBotones10").style.visibility = "hidden";
                                            deshabilitarDomicilio();
                                            desHabilitarNavegacionDom(false);
                                            habilitarSalir();
                                        }

                                        /* Función llamada desde ocultoBusqueda.jsp en el caso de que mas de un tercero este asociado
                                        * a un mismo domicilio. Ya se ha alertado al usuario y se actuará segun la respuesta obtenida. */
                                        function domicilioCompartido(respuesta) {
                                            if (respuesta) {
                                                document.forms[0].opcionDomicilioCompartido.value="modificar";
                                                pulsarRegistrarModificarDomicilio();
                                            } else {
                                            pulsarCancelarModificarDomicilio();
                                        }
                                    }

                                    function pulsarAnterior() {
                                        if(MAX>0){
                                            DomActual--;
                                            if(DomActual>=0){
                                                //cargarDomicilio(DomActual);
                                                var vectorInputs = ["codProvincia"];
                                                //habilitarGeneralInputs(vectorInputs,true);
                                                //comboProvincia.change();
                                                cargarDomicilio(DomActual);
                                                habilitarGeneralInputs(vectorInputs,false);
                                            }else
                                            DomActual=0;
                                            actualizaBotonSA();
                                        }
                                        actualizarBotonesDom();
                                    }

                                    function pulsarSiguiente() {
                                        if(MAX>0){
                                            DomActual++;
                                            if(DomActual<MAX){
                                                //cargarDomicilio(DomActual);
                                                var vectorInputs = ["codProvincia"];
                                                //habilitarGeneralInputs(vectorInputs,true);
                                                //comboProvincia.change();
                                                cargarDomicilio(DomActual);
                                                habilitarGeneralInputs(vectorInputs,false);
                                            }else
                                            DomActual=MAX-1;
                                            actualizaBotonSA();
                                        }
                                        actualizarBotonesDom();
                                    }
                                    /*
                                    function pulsarSiguienteDom() {
                                    if(MAX>0){
                                    DomActual++;
                                    if(DomActual<MAX){
                                    cargarDomicilioSinComboMun(DomActual);
                                    }else
                                    DomActual=MAX-1;
                                    actualizaBotonSA();
                                    }
                                    }
                                    */
                                    function pulsarBajaDomicilio() {

                                        if((Tercero[idTercero]!=null) && (Tercero[idTercero][18][DomActual][5] == "0")){
                                            jsp_alerta("A","Son datos del exterior");
                                        }else{
                                        if (jsp_alerta("","<%=descriptor.getDescripcion("msjBajaDomicilio")%>")) {
                                            document.forms[0].opcion.value="borrarDomicilio";
                                            if(ventana)
                                                document.forms[0].target="oculto";
                                            else document.forms[0].target="oculto";
                                            document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                                            document.forms[0].submit();
                                            actualizarBajaDomicilio();
                                        }
                                    }
                                }

                                function actualizarBajaDomicilio(){
                                    var listaDomiciliosNueva = new Array();
                                    var j=0;
                                    for(i=0;i<MAX;i++){
                                        if(i!=DomActual){
                                            listaDomiciliosNueva[j] = Terceros[idTercero][18][i];
                                            j++;
                                        }
                                    }
                                    Terceros[idTercero][18]=listaDomiciliosNueva;
                                    MAX--;
                                    if(MAX>0){
                                        DomActual= (DomActual==MAX)?MAX-1:DomActual;
                                        cargarDomicilio(DomActual);
                                        var vectorInputs = ["codProvincia"];
                                        habilitarGeneralInputs(vectorInputs,true);
                                        comboProvincia.change();
                                        cargarDomicilio(DomActual);
                                        habilitarGeneralInputs(vectorInputs,false);
                                        actualizaBotonSA();
                                        actualizarBotonesDom();
                                    }else{
                                    DomActual=0;
                                    limpiarDomicilio();
                                    document.forms[0].cmdModificarDomicilio.disabled=true;
                                    document.forms[0].cmdBajaDomicilio.disabled=true;
                                    document.forms[0].cmdAnterior.disabled=true;
                                    document.forms[0].cmdSiguiente.disabled=true;
                                }

                            }



function pulsarBuscar(){
	this.pulsarBuscarTerceros = pulsarBuscarTercerosEmpty;
	var tipo = document.forms[0].codTipoDoc.value;
	var codTerc = document.forms[0].codTerc.value;

	////////////////
	var LONGITUD = 9;
	var ZERO = "0";
	var documento = document.forms[0].txtDNI.value;

	if(tipo=="4" || tipo=="5"){
		// Si se trata de un CIF
		if(!validarCIF(documento)){
			jsp_alerta('A','<%=descriptor.getDescripcion("msjDocIncorrecto")%>');
		}
	}

	if(tipo=="1"){
		// Si se trata de un NIF
		if(documento.length==8 && !isNaN(parseInt(documento))){
			var num = parseInt(documento);
			var letra = getLetraNif(documento);
			if(letra=="-1"){
				jsp_alerta('A','<%=descriptor.getDescripcion("msjFormatoNifIncorrecto")%>');
			}
			else{
				jsp_alerta('A','<%=descriptor.getDescripcion("msjLetraNif")%> ' + letra);
			}
		}
		else{
			if(LONGITUD<documento.length){
				jsp_alerta('A','<%=descriptor.getDescripcion("msjFormatoNifIncorrecto")%>');
			}

			if(LONGITUD==documento.length){
				if(!validarNif(documento))
				{
					var letra = getLetraNif(documento.substring(0,documento.length-1));
					if(letra=="-1"){
						jsp_alerta('A','<%=descriptor.getDescripcion("msjFormatoNifIncorrecto")%>');
					}
					else{
						jsp_alerta('A','<%=descriptor.getDescripcion("msjLetraNif")%> ' + letra);
					}
				}
			}
			else{

				// HAY QUE COMPLETAR EL NIF
				var docCompleto = "";
				var limite  =0;
				limite = LONGITUD - documento.length;
				for(i=0;i<limite;i++){
					docCompleto = ZERO + docCompleto;
				}// for

				docCompleto = docCompleto + documento;
				document.forms[0].txtDNI.value = docCompleto;

				if(!validarNif(docCompleto)){
					if(documento.length==8 && !isNaN(parseInt(documento)))
					{
						var num = parseInt(documento);
						var letra = getLetraNif(documento);
						if(letra=="-1"){
							jsp_alerta('A','<%=descriptor.getDescripcion("msjFormatoNifIncorrecto")%>');
						}
						else{
							jsp_alerta('A','<%=descriptor.getDescripcion("msjLetraNif")%> ' + letra);
						}
					}
					else{
						jsp_alerta('A','<%=descriptor.getDescripcion("msjDocIncorrecto")%>');
					}
				}
			}
		}
	}// if
	////////////////
                               if((document.getElementById("capaBotones1").style.visibility == "visible")){

                                    if ((tipo!="")&&(!documentoNoValido("codTipoDoc","txtDNI",1))){

                    document.forms[0].txtDNI.value=document.forms[0].txtDNI.value.toUpperCase();
            if ((codTerc!=null) && (codTerc!="")) document.forms[0].opcion.value="buscar_por_id";
            else document.forms[0].opcion.value="buscar";

                                        if(ventana){
                                            document.forms[0].target="oculto";
                                            document.forms[0].ventana.value="true";
            }else{ document.forms[0].target="oculto"; }
                                        document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                                        document.forms[0].submit();
                                        deshabilitarCampos();
                                        deshabilitarBuscar();
        } else {
            jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
            pulsarBuscarTercerosEmpty();
        }
    }
                }


function pulsarBuscarTercerosImpl(){
    var argumentos = new Array();
    argumentos =[new Array(),"TERCEROS"];
    var source = "<html:rewrite page='/BusquedaTerceros.do?opcion=inicializarBusquedaTerc&ventana=true&preguntaAlta=no'/>";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,argumentos,
	'width=995,height=650,status='+ '<%=statusBar%>',function(datos){
                            if(datos!=undefined){
                                deshabilitarBuscar();
                                recuperaBusquedaTerceros(datos);
                                document.forms[0].codTerc.value = Terceros[0][0];
                                if(Terceros[0][0] == "0"){
                                    document.getElementById("capaBotones2").style.visibility = "visible";
                                    document.getElementById("capaBotones3").style.visibility = "hidden";
                                }
                            }
                    });
}

function pulsarBuscarTerceros(){};

this.pulsarBuscarTerceros = pulsarBuscarTercerosImpl;

function pulsarBuscarTercerosEmpty(){
    this.pulsarBuscarTerceros = pulsarBuscarTercerosImpl;
}

                                    function pulsarCancelarBuscar() {
                                        // Comprobar que no se deja algun tercero grabado sin domicilio
                                        var idDom = document.forms[0].txtIdDomicilio.value;
                                        var idTercero = document.forms[0].txtIdTercero.value;
    if ((idTercero!="")&&(idDom=="")) {
                                            jsp_alerta("A","<%=descriptor.getDescripcion("msjNoGrabDomTercExt")%>");
                                            } else {
                                            habilitarSalir();
                                            limpiarTodo();
                                            deshabilitarCampos();
                                            habilitarBuscar();
                                            document.forms[0].cmdAnterior.disabled=true;
                                            document.forms[0].cmdSiguiente.disabled=true;

                                            document.getElementById("capaBotones1").style.visibility = "visible";
                                            document.getElementById("capaBotones3").style.visibility = "hidden";
                                            document.getElementById("capaBotones9").style.visibility = "hidden";
                                            document.getElementById("capaBotones6").style.visibility = "hidden";
                                            document.getElementById("capaBotones7").style.visibility = "hidden";
                                            document.getElementById("capaBotones8").style.visibility = "hidden";
                                            document.getElementById("capaBotones10").style.visibility = "hidden";

        document.forms[0].codTerc.value = "";
                                        }
                                    }

                                    function pulsarModificar() {
                                        modificando=true;
                                        deshabilitarSalir();
                                        setTerceroAntiguo();
                                        habilitarTercero();
                                        habilitarBuscar();
                                        actualizaPersonaFJ();
                                        <!--document.forms[0].cmdBuscar.disabled = true;-->
                                        document.getElementById("capaBotones3").style.visibility = "hidden";
                                        document.getElementById("capaBotones9").style.visibility = "hidden";
                                        document.getElementById("capaBotones4").style.visibility = "visible";
                                    }

                                    function pulsarModificarDomicilio()	{
                                        document.forms[0].opcionDomicilioCompartido.value = "alertar"; // Valor por defecto
                                        if ((Tercero[idTercero]!=null) && (Tercero[idTercero][0] == "0")){
                                            if (jsp_alerta("",'<%=descriptor.getDescripcion("msjDatosExtGrabar")%>'))
                                                pulsarGrabarTercero();
                                            }else{
                                            var normalizado=document.forms[0].normalizado.checked;
                                            if(!normalizado) {
                                                comboProvincia.selectItem(-1);
                                                //comboProvincia.change();
                                                cargarDomicilio(DomActual);
                                                cargarListaCodPostales();
                                                habilitarDomicilio();
                                                deshabilitarSalir();
                                                desHabilitarNavegacionDom(true);
                                                document.getElementById("capaBotones7").style.visibility = "hidden";
                                                document.getElementById("capaBotones8").style.visibility = "visible";
                                                document.getElementById("capaBotones10").style.visibility = "hidden";
                                            } else {
                                            jsp_alerta("A",'<%=descriptor.getDescripcion("gMsgNoModDomNorm")%>');
                                            }
                                        }
                                    }

                                    function pulsarCancelarModificar() {

                                        getTerceroAntiguo();
                                        habilitarSalir();
                                        <!--document.forms[0].cmdBuscar.disabled = false;-->
                                        deshabilitarTercero();
                                        deshabilitarBuscar();
                                        <% if (inicioModificar.equals("si")) {%>
                                        document.getElementById("capaBotones9").style.visibility = "visible";
                                        <% } else {%>
                                        document.getElementById("capaBotones3").style.visibility = "visible";
                                        <% }%>
                                        document.getElementById("capaBotones4").style.visibility = "hidden";
                                    }

                                    function pulsarCancelarModificarDomicilio() {

                                        <!--recogerDatosPaginaBuscada();-->
                                        limpiarDomicilio();
                                        comboProvincia.selectItem(-1);
                                        if (MAX>0){
                                            //Forzar a que el combo haga un change para que recargue bien los valores
                                            //cargarDomicilio(DomActual);
                                            var vectorInputs = ["codProvincia"];
                                            //habilitarGeneralInputs(vectorInputs,true);
                                            //comboProvincia.change();
                                            cargarDomicilio(DomActual);
                                            habilitarGeneralInputs(vectorInputs,false);
                                        }
                                        deshabilitarDomicilio();
                                        <!--deshabilitarUsoViviendas();-->
                                        desHabilitarNavegacionDom(false);
                                        habilitarSalir();
                                        document.getElementById("capaBotones7").style.visibility = "visible";
                                        document.getElementById("capaBotones8").style.visibility = "hidden";
                                        document.getElementById("capaBotones10").style.visibility = "hidden";
                                    }
                                    function pulsarGrabarDomicilio() {
                                        if (validarDomicilio()){
                                            if (viaBuscada()){
                                                if ((Tercero[idTercero]!=null) && (Tercero[idTercero][0] == "0")){
                                                    //TODO:Añadir a la base de datos esta frase
                                                    if(jsp_alerta("","Es de una busqueda externa. Se Insertara no se Modificara "))
                                                        pulsarGrabarTercero();
                                                }else{
                                                habilitarDomicilio();
                                                document.forms[0].txtNormalizado.value = document.forms[0].normalizado.checked ? "1":"2";
                                                document.forms[0].opcion.value="grabarDomicilio";
                                                // Si no hay tercero no se puede grabar el domicilio
                                                if (document.forms[0].txtIdTercero.value == "") {
                                                    jsp_alerta("A",'<%=descriptor.getDescripcion("msjGrabarDomNoTer")%>');
                                                    return;
                                                }
                                                if(ventana){
                                                    document.forms[0].ventana.value="true";
                                                    document.forms[0].target="oculto";
                                                }else{
                                                document.forms[0].ventana.value="false";
                                                document.forms[0].target="oculto";
                                            }
                                            document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                                            document.forms[0].submit();
                                            deshabilitarDomicilio();
                                        }
		}else {jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoViaDom")%>');}
                                    }
                                }

                                function algunDomicilioGrabado() {
                                    var algunDomGrabado = false;
                                    var i = 0;
                                    while(i<MAX && !algunDomGrabado) {
                                        if (Terceros[0][18][i][5]!="0" && Terceros[0][18][i][5]!="") {
                                            algunDomGrabado = true;
                                        }
                                        i++;
                                    }
                                    return algunDomGrabado;
                                }

                                function pulsarCancelar() {
                                    document.forms[0].eliminarTercero.value="no";
                                    var idDomSel = document.forms[0].txtIdDomicilio.value;
                                    var idsSel = document.forms[0].txtIdTercero.value;
                                    if (!algunDomicilioGrabado() && idsSel!="" && idsSel!="0") {
                                        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoGrabDomTercExt")%>");
                                        } else {
                                        if((idsSel!="")&&(idDomSel=="")){
                                            jsp_alerta("A","<%=descriptor.getDescripcion("msjNoTerDom")%>");
                                            }else{
                                            if((idsSel=="")&&(idDomSel==""))
                                                self.parent.opener.retornoXanelaAuxiliar();
                                            else {
                                                devolverValores();
                                            }
                                        }
                                    }
                                }

                                // Devuelve datos en uno u otro formato según el caso
                                function devolverValores() {
                                    // En expedientes o registro, apl!=3 
                                    <% if (apl != 3) {%>
                                    if (!cargandoTerceroExt) {
                                        lCasas = Terceros[0][18];
                                        enviaSeleccionDoms(0,DomActual);
                                    } else {
                                        self.parent.opener.retornoXanelaAuxiliar(Terceros);
                                }
                                <% } else {%>
                                    self.parent.opener.retornoXanelaAuxiliar(Terceros);
                                <% }%>

                            }

                            // Se llama al dar de baja a un tercero, por lo que la comprobacion de eliminarTercero="si" siempre se cumple...
                            function cerrar(){
                                <% if (inicioAlta.equals("si") || inicioModificar.equals("si") || inicioAltaDesdeBuscar.equals("si") ||
                    (cargarTercero.equals("si") && apl == 3)) {
                // Cuando la aplicacion != 3 (terceros) se asume que se abre desde interesados.jsp, en ese caso hay
                // que devolver TerceroSel
%>
    
    // Esta comprobacion se lleva a cabo en cualquier aplicacion o caso.
    if (document.forms[0].eliminarTercero.value=="si"){
            var devolver = new Array();
            var lista = new Array();
            lista[0] = "eliminarTercero";
            devolver[0] = lista;
            self.parent.opener.retornoXanelaAuxiliar(devolver);
        }else{
            <% if (inicioAltaDesdeBuscar.equals("si")) {%>
                Terceros[0][19]=DomActual;
                self.parent.opener.retornoXanelaAuxiliar(Terceros);
            <% } %>
            //}
            <% } else {%>
                self.parent.opener.retornoXanelaAuxiliar(TerceroSel);
            <% }%>
        }
    }

    function validarTercero(){
        var codTipoDoc = document.forms[0].codTipoDoc;
        var documento = document.forms[0].txtDNI;
        var nombre = document.forms[0].txtInteresado;
        var apel1 = document.forms[0].txtApell1.value;
        if((codTipoDoc.value=="")||((codTipoDoc.value!="0")&&(documento.value==""))||(nombre.value=="") || ((codTipoDoc.value==1) && (apel1==""))) {
            jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
            // comentado: NO cancelar alta o modificacion pq falten datos
            //if (modificando) {
            //    pulsarCancelarModificar();
            //} else if (registrando) {
            //    pulsarCancelarAlta();
            //}
            //modificando=false;
            //registrando=false;
            return false;
        }
        return true;
    }

    function cambioEnDocumento(){
        if (((document.forms[0].codTipoDoc.value==codTipoDocOld) && (document.forms[0].descTipoDoc.value==descTipoDocOld)
            && (document.forms[0].txtDNI.value==documentoOld)) || (codTipoDocOld=="0"))
        return false;
        else return true;
    }

    function validarDomicilio(){
        var pais = document.forms[0].codPais;
        var prov = document.forms[0].codProvincia;
        var muni = document.forms[0].codMunicipio;
        var cpostal	= document.forms[0].descPostal;
        var domicilio	= document.forms[0].txtDomicilio;
        var via	= document.forms[0].codVia;
        if(('<%=emplv%>' == "hidden" && Trim(via.value) == "") || (Trim(pais.value)=="")||(Trim(prov.value)=="")||(Trim(muni.value)=="")){
            jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                return false;
            } else {
            if (Trim(via.value) == ''  && Trim(domicilio.value) == '' ){
                jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoViaDom")%>');
                    return false;
                }
            }
            /* anadir ECOESI */
            if( (document.forms[0].codMunicipio.value=="<%=ptVO.getMunicipio()%>")
                && ( document.forms[0].codProvincia.value=="<%=ptVO.getProvincia()%>")) {

                    var ecv = '<%=ecv%>';
                    var esv = '<%=esv%>';
                    if (ecv == 'visible' && esv == 'visible') {
                        if (( document.forms[0].codECO.value=="")&&( document.forms[0].codESI.value=="") ){
                            jsp_alerta("A","<%=descriptor.getDescripcion("msjRellenarESI")%>");
                                return false;
                            }
                        }
                    }
                    /* fin anadir ECOESI */

                    if(cpostal.value!=""){
                        if (prov.value.length==1) prov.value="0"+prov.value;
                        if (!validarCodigoPostal(prov,cpostal)) {
                            document.forms[0].descPostal.select();
                            return false;
                        }
                    }
                    return true;
                }

                function validaBuscarDomicilio(){
                    var res = false;
                    var prov = document.forms[0].codProvincia.value;
                    var muni = document.forms[0].codMunicipio.value;
                    if( (muni=="<%=ptVO.getMunicipio()%>") &&  ( prov =="<%=ptVO.getProvincia()%>")){
                        res = true;
                    }
                    return res;
                }

                function validaBuscarDomicilioNormalizado(){
                    var res = false;
                    var prov = document.forms[0].codProvincia.value;
                    var muni = document.forms[0].codMunicipio.value;
                    if( (muni=="<%=ptVO.getMunicipio()%>") &&  ( prov =="<%=ptVO.getProvincia()%>")){
                        if ( document.forms[0].codESI.value!=""
                            || document.forms[0].codVia.value!="" || document.forms[0].txtDomicilio.value!="") {
                        res = true;
                    }
                }
                return res;
            }


            function pulsarBuscarDomicilioExt(){

                document.forms[0].ventana.value="mainFrame";
                document.forms[0].target="oculto";
                if(ventana){
                    document.forms[0].ventana.value="mainFrame1";
                    document.forms[0].target="oculto";
                }
                document.forms[0].txtDocumentos.value = document.forms[0].txtDNI.value;
                document.forms[0].opcion.value="buscarDomicilioExt";
                document.forms[0].action="<html:rewrite page='/BusquedaTerceros.do'/>";
                document.forms[0].submit();
            }


            function recuperaBusquedaDomicilio(){
                var frame="oculto";
                var win=document.forms[0].ventana.value;
                var longitud=0;
                if(ventana)
                    frame="oculto";
                Domicilios=eval("top."+frame+".Domicilio");
                lista=eval("top."+frame+".lista");
                var argumentos = new Array();
                longitud=Domicilios.length;
                if(longitud==1){
                    cargaDomicilio();
                    <!--Comprobamos que el domicilio recuperado no es de otra base de datos.-->
                    if(document.forms[0].txtIdDomicilio.value == "0"){
                        document.forms[0].normalizado.checked=false;
                    }else{
                    document.forms[0].normalizado.checked=true;
                }
                document.forms[0].normalizado.disabled=true;
                deshabilitarDomicilio();
                //deshabilitarBuscarDomicilio();
                habilitarUsoViviendas();
                if(domicilioAsociado()){
                    jsp_alerta("A",'<%=descriptor.getDescripcion("msjDomAsoc")%>');
                        limpiarDomicilio();
                        habilitarDomicilio();
                        //habilitarBuscarDomicilio();
                        document.forms[0].normalizado.checked=false;
                    }
                } else if((longitud==0)||(longitud>1)){

                if(longitud==0)
                    jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoDatos")%>');

                        var	datosBusqueda = new Array();
                        datosBusqueda =	[document.forms[0].txtCodVia.value,document.forms[0].txtDomicilio.value,
                            document.forms[0].codPais.value,document.forms[0].descPais.value,document.forms[0].codProvincia.value,
                            document.forms[0].descProvincia.value,document.forms[0].codMunicipio.value,document.forms[0].descMunicipio.value,
                            document.forms[0].txtNumDesde.value,
                            document.forms[0].txtLetraDesde.value,document.forms[0].txtNumHasta.value,document.forms[0].txtLetraHasta.value,
                            document.forms[0].txtBloque.value,document.forms[0].txtPortal.value,document.forms[0].txtEsc.value,
                            document.forms[0].txtPlta.value,document.forms[0].txtPta.value,document.forms[0].txtKm.value,
                            document.forms[0].txtHm.value, document.forms[0].idVia.value,document.forms[0].codECO.value,
                            document.forms[0].codESI.value,document.forms[0].descESI.value];
                        argumentos = [Domicilios,lista,datosBusqueda];
                        var source = "<%=request.getContextPath()%>/BusquedaTerceros.do?opcion=inicializarBusquedaDoms&ventana="+win;
                        abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source="+source, argumentos,
                                'width=790,height=550,status='+ '<%=statusBar%>',function(datos){
                                        if(Domicilios.length==1){
                                            cargaDomicilio();
                                            <!--Comprobamos que el domicilio recuperado no es de otra base de datos.-->
                                            if(document.forms[0].txtIdDomicilio.value == "0"){
                                                document.forms[0].normalizado.checked=false;
                                            }else{
                                            document.forms[0].normalizado.checked=true;
                                        }
                                        document.forms[0].normalizado.disabled=true;
                                        deshabilitarDomicilio();
                                        //deshabilitarBuscarDomicilio();
                                        //	habilitarUsoViviendas();
                                        if(domicilioAsociado()){
                                            jsp_alerta("A",'<%=descriptor.getDescripcion("msjDomAsoc")%>');
                                                limpiarDomicilio();
                                                habilitarDomicilio();
                                                //habilitarBuscarDomicilio();
                                                document.forms[0].normalizado.checked=false;
                                            }
                                        }
                                });
                        }
                    }
                    
                    function domicilioAsociado(){
                        var idDom = document.forms[0].txtIdDomicilio.value;
                        var longitud = Terceros[idTercero][18].length;
                        for(i=0;i<longitud;i++){
                            if(Terceros[idTercero][18][i][5]==idDom)
                                return true;
                        }
                        return false;
                    }

                    function cargaDomicilio(){
                        document.forms[0].txtCodVia.value =	Domicilios[0][0];
                        document.forms[0].txtDomicilio.value = Domicilios[0][1];
                        document.forms[0].codPais.value = Domicilios[0][2];
                        document.forms[0].descPais.value = Domicilios[0][3];
                        document.forms[0].codProvincia.value = Domicilios[0][4];
                        document.forms[0].descProvincia.value = Domicilios[0][5];
                        document.forms[0].codMunicipio.value = Domicilios[0][6];
                        document.forms[0].descMunicipio.value = Domicilios[0][7];
                        document.forms[0].codTVia.value = Domicilios[0][8];
                        document.forms[0].descTVia.value = Domicilios[0][8]== <%=CodigoSinTipoVia%> ? "": Domicilios[0][9];
                        document.forms[0].txtNumDesde.value	= Domicilios[0][10]== 0	? "":	Domicilios[0][10];
                        document.forms[0].txtLetraDesde.value = Domicilios[0][11];
                        document.forms[0].txtNumHasta.value	= Domicilios[0][12]== 0	? "":	Domicilios[0][12];
                        document.forms[0].txtLetraHasta.value = Domicilios[0][13];
                        document.forms[0].txtBloque.value =	Domicilios[0][14];
                        document.forms[0].txtPortal.value =	Domicilios[0][15];
                        document.forms[0].txtEsc.value = Domicilios[0][16];
                        document.forms[0].txtPlta.value = Domicilios[0][17];
                        document.forms[0].txtPta.value = Domicilios[0][18];
                        document.forms[0].txtKm.value	= Domicilios[0][19];
                        document.forms[0].txtHm.value	= Domicilios[0][20];
                        document.forms[0].descPostal.value = Domicilios[0][22];
                        <!--document.forms[0].codUso.value = Domicilios[0][23]== 0 ? "": Domicilios[0][23];-->
                        <!--document.forms[0].descUso.value = Domicilios[0][24];-->
                        document.forms[0].txtIdDomicilio.value = Domicilios[0][25];
                        document.forms[0].idVia.value	= Domicilios[0][26];
                        /* Anadir ECOESI */
                        var codigoECO =	Domicilios[0][27];
                        var codigoESI =	Domicilios[0][28];
                        var descripcionESI =	Domicilios[0][29];
                        if( (document.forms[0].codMunicipio.value=="<%=ptVO.getMunicipio()%>") &&  ( document.forms[0].codProvincia.value=="<%=ptVO.getProvincia()%>")){
                            if (codigoECO!="" && codigoESI!=""){
                                comboECO.buscaCodigo(codigoECO);
                                //comboESI.buscaCodigo(codigoESI);
                                document.forms[0].codESI.value=codigoESI;
                                document.forms[0].descESI.value=descripcionESI;
                            } else {
                            comboECO.selectItem(-1);
                            comboESI.selectItem(-1);
                        }
                        if (document.forms[0].txtCodVia.value!='') {
                            document.forms[0].codVia.value =	Domicilios[0][0];
                            document.forms[0].descVia.value =	Domicilios[0][1];
                        }

                    } else {
                    comboECO.selectItem(-1);
                    comboESI.selectItem(-1);
                    document.forms[0].txtCodVia.value =	"";
                    document.forms[0].codVia.value = "";
                    document.forms[0].descVia.value = Domicilios[0][1];//"";

                }
                document.forms[0].txtBarriada.value	= Domicilios[0][21];
                <!-- Fin anadir ECOESI -->

                /*DomicilioBien = new Array();
                DomiciliosBien=[Domicilios[i][3],Domicilios[i][5],Domicilios[i][7],Domicilios[i][1],Domicilios[i][22],
                Domicilios[i][25],Domicilios[i][2],Domicilios[i][4],Domicilios[i][6],
                Domicilios[i][10],Domicilios[i][11],Domicilios[i][12],Domicilios[i][13],Domicilios[i][14],
                Domicilios[i][15],Domicilios[i][16],Domicilios[i][17],Domicilios[i][18],Domicilios[i][21],
                Domicilios[i][18],Domicilios[i][19],Domicilios[i][23],Domicilios[i][24],Domicilios[i][0],
                "1",Domicilios[i][26],Domicilios[i][27],Domicilios[i][28],Domicilios[i][29]];
                Domicilios=DomiciliosBien;
                */
            }
            function deshabilitarImagen(vector, valor){
                for (var i = 0; i < vector.length; i++) {
                    if (vector[i].name == "botonVias"){
                        if (valor) {
                            vector[i].disabled=true;
                            vector[i].style.cursor = 'default';
                            vector[i].style.color="#f6f6f6 !important";
                        }else{
                        vector[i].disabled=false;
                        vector[i].style.cursor = 'pointer';
                        vector[i].style.color="#0B3090 !important";
                    }
                }
            }
        }
        function limpiarVia(){
            var descVia =	document.forms[0].descVia.value;
            if (descVia==""){
                document.forms[0].txtCodVia.value =	"";
                document.forms[0].codVia.value = "";
                document.forms[0].idVia.value = "";
                document.forms[0].codTVia.value = "";
                document.forms[0].descTVia.value = "";
            }
        }

        function viaBuscada(){
            var buscada = false;
            if(document.forms[0].descVia.value != "") {
                if (document.forms[0].codVia.value !="")
                    buscada = true;
            } else {
            limpiarVia();
            buscada = true; <!--Por si no ha saltado el onchange-->
        }
        return buscada;
    }
    function cargarListaViasBuscadas(total, idVia, codVia, descVia, descTipoVia, codECO, codESI, codTipoVia, codProvincia, descProvincia, codMunicipio, descMunicipio, nombreVia){
         if(nombreVia!=null && nombreVia.length>0)
             nombreViaGeneral = nombreVia;

         if (total==0){
            document.forms[0].txtCodVia.value = "";
            document.forms[0].codVia.value = "";
            document.forms[0].descVia.value = "";
            document.forms[0].txtDomicilio.value = "";
            document.forms[0].idVia.value = "";
            document.forms[0].codTVia.value = "";
            document.forms[0].descTVia.value = "";
            pleaseWait1("off",top.mainFrame);
            if(<%=bAltaViaDirecta%>){;
                if ( jsp_alerta("C",'<%=descriptor.getDescripcion("msjNoDatosPregNueva")%>') == 1 ){
                    iniciarAltaViaDirecta();
                }
            }else{
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoDatos")%>');
            }
        } else 	if (total==1){
        document.forms[0].codProvincia.value = codProvincia;
        document.forms[0].descProvincia.value = descProvincia;
        comboProvincia.change();
        document.forms[0].codMunicipio.value = codMunicipio;
        document.forms[0].descMunicipio.value = descMunicipio;
        document.forms[0].codECO.value = codECO;
        if (codESI != '') comboESI.buscaCodigo(codESI);
        document.forms[0].txtCodVia.value = codVia;
        document.forms[0].codVia.value = codVia;
        document.forms[0].descVia.value = descVia;
        document.forms[0].idVia.value = idVia;
        document.forms[0].codTVia.value = codTipoVia;
        document.forms[0].descTVia.value = codTipoVia == '<%=CodigoSinTipoVia%>'?"":descTipoVia;
        pleaseWait1("off",top.mainFrame);
    } else {
    var ver = "0,";
    var tamanoVentana=940;
    if (document.forms[0].codProvincia.value!=null && document.forms[0].codProvincia.value!="") {
        ver = "1,";
        tamanoVentana = tamanoVentana - 120;
    }
    if (document.forms[0].codMunicipio.value!=null && document.forms[0].codMunicipio.value!="") {
        ver = ver + "1";
        tamanoVentana = tamanoVentana - 120;
    } else ver = ver + "0";
    var source = "<%=request.getContextPath()%>/jsp/terceros/listaViasBuscadas.jsp?opcion="+ ver;
    abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source="+source,'',
	'width='+tamanoVentana+',height=600,status='+ '<%=statusBar%>',function(datosConsulta){
                            if(datosConsulta!=undefined && datosConsulta[0] == "altaViaDirecta"){
                                iniciarAltaViaDirecta();
                            } else if(datosConsulta!=undefined){
                            if (datosConsulta[4]=='null') datosConsulta[4] = '';
                            document.forms[0].codECO.value = datosConsulta[4];
                            if ( datosConsulta[4] != '') comboESI.buscaCodigo(datosConsulta[5]);
                            document.forms[0].txtCodVia.value = datosConsulta[1];
                            document.forms[0].codVia.value = datosConsulta[1];
                            document.forms[0].descVia.value = datosConsulta[2];
                            //document.forms[0].txtDomicilio.value = datosConsulta[2];
                            document.forms[0].idVia.value = datosConsulta[0];
                            document.forms[0].codTVia.value = datosConsulta[6];
                            document.forms[0].descTVia.value = datosConsulta[6] == '<%=CodigoSinTipoVia%>'?"":datosConsulta[3];
                        }
                        pleaseWait1("off",top.mainFrame);
                });
    }
}


function pulsarBuscarVia(opcion) {
    if (!document.forms[0].descVia.disabled) {
        if (advertirBusquedaTramero()) {

            // Si no hay provincia, usamos valores por defecto
            if (document.forms[0].codProvincia.value == "") {

                document.forms[0].codProvincia.value = "<%=ptVO.getProvincia()%>";
                document.forms[0].descProvincia.value = "<%=ptVO.getNomProvincia()%>";
                comboMunicipio.addItems(codMunicipiosDefecto,descMunicipiosDefecto);
                document.forms[0].codMunicipio.value = "<%=ptVO.getMunicipio()%>";
                document.forms[0].descMunicipio.value = "<%=ptVO.getNomMunicipio()%>";

            // Si hay provincia pero no hay municipio...
            } else if (document.forms[0].codMunicipio.value == "") {

                // Si la provincia es la 'por defecto', usamos el municipio por defecto.
                if (document.forms[0].codProvincia.value == "<%=ptVO.getProvincia()%>") {

                    document.forms[0].codMunicipio.value = "<%=ptVO.getMunicipio()%>";
                    document.forms[0].descMunicipio.value = "<%=ptVO.getNomMunicipio()%>";

                // Si la provincia no es la 'por defecto', se pide al usuario que
                // introduzca municipio.
                } else {
                    jsp_alerta("A", '<%=descriptor.getDescripcion("msjIntroMun")%>');
                    return;
                }
            }

            pleaseWait1("on",top.mainFrame);
            document.forms[0].opcion.value=opcion;
            document.forms[0].target="oculto";
            var urlParams;
            urlParams="codProvincia=" + document.forms[0].codProvincia.value;
            urlParams= urlParams + "&codMunicipio=" + document.forms[0].codMunicipio.value;
            <% if (existeTramero.equals("si")) {%>
            document.forms[0].action="<html:rewrite page='/terceros/mantenimiento/Viales.do?'/>" + urlParams;
            <% } else {%>
            urlParams = urlParams + '&txtNumDesde=&txtNumHasta=';
            document.forms[0].action="<html:rewrite page='/terceros/mantenimiento/Viales.do?'/>" + urlParams;
            <% }%>
            document.forms[0].submit();
        }
    }
}

function advertirBusquedaTramero() {
    var numDesde = document.forms[0].txtNumDesde.value;
    var numHasta = document.forms[0].txtNumHasta.value;
    <% if (existeTramero.equals("si")) {%>
    if ((numDesde!="" && numDesde!=null) || (numHasta!="" && numHasta!=null)) {
        if (jsp_alerta("",'<%=descriptor.getDescripcion("msjBusqTramero")%>')) {
            return true;
        } else
        return false;
    } else
    return true;
    <% } else {%>
    return true;
    <% }%>
}

function iniciarAltaViaDirecta(){
    document.forms[0].viaDarAlta.value = nombreViaGeneral;
    pleaseWait1("on",top.mainFrame);
    document.forms[0].opcion.value="iniciarAltaDirecta";
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Viales.do";
    document.forms[0].submit();
}
function altaViaDirectaIniciada(codTipoVia,descTipoVia,nombreViaAlta) {
    var	argumentos = new Array();
    argumentos = [document.forms[0].codProvincia.value,document.forms[0].codMunicipio.value,
        codTipoVia, descTipoVia,nombreViaAlta];
    var source = "<%=request.getContextPath()%>/jsp/terceros/altaViaDirecta.jsp?opcion=altaViaDirecta";
    pleaseWait1("off",top.mainFrame); // Viene del buscar via
    abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source="+source,argumentos,
	'width=500,height=400,status='+ '<%=statusBar%>',function(datosConsulta){
                            if(datosConsulta!=undefined){
                                document.forms[0].codECO.value = datosConsulta[4];
                                if ( datosConsulta[4] != '') comboESI.buscaCodigo(datosConsulta[5]);
                                document.forms[0].txtCodVia.value = datosConsulta[1];
                                document.forms[0].codVia.value = datosConsulta[1];
                                document.forms[0].descVia.value = datosConsulta[2];
                                document.forms[0].idVia.value = datosConsulta[0];
                                document.forms[0].codTVia.value = datosConsulta[6];
                                document.forms[0].descTVia.value = datosConsulta[6] == '<%=CodigoSinTipoVia%>'?"":datosConsulta[3];
                            }
                    });
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

    codECOs= [<%=lcodECOS%>];
        descECOs= [<%=ldescECOS%>];

            codESIsOld = [<%= codESIs %>];
                descESIsOld = [<%= descESIs %>];
                    listaECOESIsOld = [<%= listaECOESIs %>];
                        codESIs= [<%= codESIs %>];
                            descESIs= [<%= descESIs %>];
                                listaECOESIs = [<%= listaECOESIs %>];

                                    function cargarListas(){
                                        if( (document.forms[0].codMunicipio.value=="<%=ptVO.getMunicipio()%>") &&  ( document.forms[0].codProvincia.value=="<%=ptVO.getProvincia()%>")){
                                            var deshabilt=false;
                                            if (document.forms[0].codProvincia.disabled) {
                                                deshabilt=true;
                                                comboProvincia.activate();
                                                comboMunicipio.activate();
                                            }
                                            document.forms[0].opcion.value="cargarListas";
                                            document.forms[0].target="oculto";
                                            document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                                            document.forms[0].submit();
                                            if (deshabilt){
                                                comboProvincia.deactivate();
                                                comboMunicipio.deactivate();
                                            }
                                        }
                                    }

                                    function desHabilitarECOESIVIA(valor) {
                                        limpiarECOESIVIA();
                                        if (valor) {
                                            comboECO.deactivate();
                                            comboESI.deactivate();
                                            /* Permitir vias de otro municipio 14/06/05
                                            deshabilitarGeneral([document.forms[0].codVia,document.forms[0].descVia]);
                                            deshabilitarImagen(new Array( document.forms[0].botonV),true);
                                            */
                                        } else {
                                        comboECO.activate();
                                        comboESI.activate();
                                        /* Permitir vias de otro municipio 14/06/05
                                        habilitarGeneral([document.forms[0].codVia,document.forms[0].descVia]);
                                        habilitarImagen(new Array( document.forms[0].botonV),true);
                                        */
                                    }
                                }

                                function limpiarECOESIVIA(){
                                    comboECO.selectItem(-1);
                                    comboESI.selectItem(-1);
                                    document.forms[0].codVia.value="";
                                    document.forms[0].descVia.value="";
                                    document.forms[0].txtBarriada.value="";
                                    document.forms[0].txtDomicilio.value="";
                                    limpiarVia();
                                }

                                /* fin anadir ECO/ESI */

                                function antesDeSalir() {
                                    // Para detectar que no se trate de una baja (en ese caso no hay nada que comprobar)
                                    if (document.forms[0].eliminarTercero.value != "si") {
                                        var idDom = document.forms[0].txtIdDomicilio.value;
                                        var idTercero = document.forms[0].txtIdTercero.value;
                                        // Aqui comprobar si el tercero tiene domicilio o no
                                        // MAX es el numero de domicilios del tercero
                                        if ((idTercero!="")&&(MAX == 0)) {
                                            //Esta cosa rara con los arrays es para satisfacer el formato que se espera recibir
                                            //como valor de retorno de esta pagina al abrirla con abrirXanelaAuxiliar(...).
                                            var devolver = new Array();
                                            var lista = new Array();
                                            lista[0] = "terceroSinDomicilio";
                                            lista[1] = idTercero;
                                            devolver[0] = lista;
                                            self.parent.opener.retornoXanelaAuxiliar(devolver);
                                            // Esta parte de codigo es igual que en pulsarCancelar() (Salir)
                                        } else if ((idTercero=="")&&(idDom=="")) {
                                        self.parent.opener.retornoXanelaAuxiliar();
                                    } else {
                                    devolverValores();
                                }
                            }
                        }


function validarNif(abc)
{
    dni=abc.substring(0,abc.length-1)
    let=abc.charAt(abc.length-1)

    if (!isNaN(let))
    {
      return false
    }
    else
    {
      cadena="TRWAGMYFPDXBNJZSQVHLCKET"
      posicion = dni % 23
      letra = cadena.substring(posicion,posicion+1)
      if (letra!=let.toUpperCase())
       {
        return false
       }
    }
    return true;
}

function getLetraNif(dni) {
    var lockup = 'TRWAGMYFPDXBNJZSQVHLCKE';
    return lockup.charAt(dni % 23);
}

 function validarCIF(F)
  {
   var v1 = new Array(0,2,4,6,8,1,3,5,7,9);
   var temp = 0;
   for( i = 2; i <= 6; i += 2 )
   {
        temp = temp + v1[ parseInt(F.substr(i-1,1))];
        temp = temp + parseInt(F.substr(i,1));
   }
   temp = temp + v1[ parseInt(F.substr(7,1))];
   temp = (10 - ( temp % 10));

   if( temp != F.substring(F.length-1,F.length))
        return false;

   return true;
  }

        </SCRIPT>
    </head>
    <BODY class="bandaBody" onload="inicializar();" onBeforeUnload="antesDeSalir()">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

    <form  method=POST target="_self">
        <input type="hidden" name="opcion">
        <input type="hidden" name="opcionDomicilioCompartido" value="alertar">
        <input type="hidden" name="insertarDoc">
        <input type="hidden" name="grabarDirecto" value="no">
        <input type="hidden" name="codTerc">
        <input type="hidden" name="numModifTerc">
        <input type="hidden" name="txtIdTercero">
        <input type="hidden" name="situacion">
        <input type="hidden" name="txtVersion">
        <input type="hidden" name="txtIdDomicilio">
        <input type="hidden" name="idVia" value="">
        <input type="hidden" name="txtNormalizado">
        <input type="hidden" name="ventana" value="false">
        <input type="hidden" name="perFJ" value=1>
        <input type="hidden" name="txtPart" class="inputTexto">
        <input type="hidden" name="txtPart2" class="inputTexto">
        <input type="hidden" name="codPais" size="3" value="<%=ptVO.getPais()%>">
        <input type="hidden" name="descPais" size="3">
        <input type="hidden" name="lineasPagina" value="10">
        <input type="hidden" name="pagina" value="1">
        <input type="hidden" name="txtCodVia">
        <input type="hidden" name="eliminarTercero" value="no">
        <input type="hidden" name="codTVia">
        <input type="hidden" name="descTVia">
        <input type="hidden" name="txtDocumentos">
        <input type="hidden" name="domActual">
        <input type="hidden" name="viaDarAlta" value=""/>
        
        <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titTerDom")%></div>
        <div class="contenidoPantalla">
            <div class="sub3titulo" style="width:100"><%= descriptor.getDescripcion("manTer_TitDatos")%></div>
            <table width="100%">
                <tr>
                    <td>
                        <table width="100%" cellspacing="0px" cellpadding="0px" border="0px">
                            <tr>
                                <td style="width: 17%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqTiDoc")%>:</td>
                                <td style="width: 38%" class="columnP">
                                    <input id="codTipoDoc" name="codTipoDoc" type="text" class="inputTextoObligatorio" size="3">
                                    <input id="descTipoDoc" name="descTipoDoc" type="text" class="inputTextoObligatorio" style="width:174" readonly>
                                    <a id="anchorTipoDoc" name="anchorTipoDoc" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoDoc" name="botonTipoDoc"	style="cursor:pointer;" alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>" border="0" style="cursor:hand;"></span></a>
                                </td>
                                <td style="width: 10%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqDocumento")%>:</td>
                                <td style="width: 13%" class="columnP">
                                    <input type="text" class="inputTextoObligatorio" size=16 maxlength=16 name="txtDNI" onkeypress="javascript:PasaAMayusculas(event);" onchange="pulsarBuscar();">
                                </td>
                                <% if (!inicioAlta.equals("si") && !inicioModificar.equals("si")) {%>
                                <td>
                                    <span class="fa fa-search" aria-hidden="true"  name="botonT" alt = "<%=descriptor.getDescripcion("altBuscInt")%>" title = "<%=descriptor.getDescripcion("altBuscInt")%>" style="cursor:pointer;" onclick= "pulsarBuscarTerceros();"></span>
                                </td>
                                <% } else { %>
                                <td>&nbsp;</td>
                                <% } %>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table width="100%" cellspacing="0px" cellpadding="0px" border="0px">
                            <tr>
                                <td style="width: 17%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqNombreRazon")%>:</td>
                                <td>
                                    <input type="text" id="obligatorio" class="inputTextoObligatorio" style="width:761" maxlength=80 name="txtInteresado" onKeyPress="javascript:PasaAMayusculas(event);">
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table width="100%" cellspacing="0px" cellpadding="0px" border="0px">
                            <tr>
                                <td style="width: 17%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqApellido1Part")%>:</td>
                                <td style="width: 38%" class="columnP">
                                    <input type="text" name="txtApell1" class="inputTexto" size=51 maxlength=25 onKeyPress="javascript:PasaAMayusculas(event);">
                                </td>
                                <td style="width: 10%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqApellido2Part")%>:</td>
                                <td class="columnP">
                                    <input name="txtApell2" type="text" class="inputTexto" size=51 maxlength=25 onKeyPress="javascript:PasaAMayusculas(event);">
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table width="100%" cellspacing="0px" cellpadding="0px" border="0px">
                            <tr>
                                <td style="width: 17%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqTelfFax")%>:</td>
                                <td style="width: 38%" class="etiqueta">
                                    <input id="telefono" type="text" name="txtTelefono" class="inputTexto" size=51 maxlength=40 >
                                </td>
                                <td style="width: 10%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqEmail")%>:</td>
                                <td class="etiqueta">
                                    <input id="email" type="text" name="txtCorreo" class="inputTexto"	size=51 maxlength=50>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td style="height: 30px">
                        <div id="capaBotones1" name="capaBotones1" style="position:absolute; width:100%; top:155px; border: 0; text-align: right">
                            <table cellpadding="0px" cellspacing="0px" style="border: 0;">
                                <tr>
                                    <td>
                                        <table cellpadding="0px" cellspacing="0px">
                                            <tr>
                                                <td>
                                                    <input type= "button" title='<%=descriptor.getDescripcion("toolTip_bAlta")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbAlta")%>' name="cmdAlta" onClick= "pulsarAlta();return false;">
                                                </td>

                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <!-- Dando de Alta:
                                                                                         1. Registrar alta.
                                                                                         2. Cancelar alta.
                        -->
                        <div id="capaBotones2" name="capaBotones2" style="position:absolute;width:100%;top:155px; visibility:hidden; border: 0; text-align: right">
                            <table cellpadding="0px" cellspacing="0px" style="border: 0;">
                                <tr>
                                    <td>
                                        <table cellpadding="0px" cellspacing="0px">
                                            <tr>
                                                <td>
                                                    <input type= "button" title='<%=descriptor.getDescripcion("toolTip_bGrabar")%>' alt='<%=descriptor.getDescripcion("toolTip_bGrabar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbGrabar")%>' name="cmdGrabarTercero" onClick= "pulsarGrabarTercero();return false;">
                                                </td>
                                                <TD style="width:2px"></TD>
                                                <td>
                                                    <input type="button" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bGrabar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelarAlta" onClick="pulsarCancelarAlta();return false;">
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>

                        </div>
                        <!-- Página buscada:
                                                                                         1. Modificar.
                                                                                         2. Anular.
                                                                                         3. Cancelar página buscada.
                        -->
                        <div id="capaBotones3" name="capaBotones3" style="position:absolute;width:100%;top:155px;visibility:hidden; border: 0; text-align: right">
                            <table cellpadding="0px" cellspacing="0px" style="border: 0;">
                                <tr>
                                    <td>
                                        <table cellpadding="0px" cellspacing="0px">
                                            <tr>
                                                <td>
                                                    <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbModificar")%>' name="cmdModificar" onClick= "pulsarModificar();return false;">
                                                </td>
                                                <TD style="width:2px"></TD>
                                                <td>
                                                    <input type= "button" title='<%=descriptor.getDescripcion("toolTip_bBaja")%>' alt='<%=descriptor.getDescripcion("toolTip_bBaja")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbBaja")%>' name="cmdBaja" onClick= "pulsarBaja();return false;">
                                                </td>
                                                <TD style="width:2px"></TD>
                                                <td>
                                                    <input type="button" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelarBuscada" onClick="pulsarCancelarBuscar();return false;">
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <!-- Página buscada:
                                                                                         1. Modificar.
                                                                                         2. Anular.
                        -->
                        <div id="capaBotones9" name="capaBotones9" style="position:absolute;width:100%;top:155px;visibility:hidden;border: 0; text-align: right">
                            <table cellpadding="0px" cellspacing="0px" style="border: 0;">
                                <tr>
                                    <td>
                                        <table cellpadding="0px" cellspacing="0px">
                                            <tr>
                                                <td>
                                                    <input type= "button" title='<%=descriptor.getDescripcion("toolTip_bModificar")%>' alt='<%=descriptor.getDescripcion("toolTip_bModificar")%>' class="botonGeneral" name="cmdModificar" onClick= "pulsarModificar();return false;" value="<%=descriptor.getDescripcion("gbModificar")%>" >
                                                </td>
                                                <TD style="width:2px"></TD>
                                                <td>
                                                    <input type= "button" title='<%=descriptor.getDescripcion("toolTip_bBaja")%>' alt='<%=descriptor.getDescripcion("toolTip_bBaja")%>' class="botonGeneral" name="cmdBaja" onClick= "pulsarBaja();return false;" value="<%=descriptor.getDescripcion("gbBaja")%>" >
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <!-- Modificando:
                                                                                         1. Registrar cambios.
                                                                                         2. Cancelar	cambios.
                        -->
                        <div id="capaBotones4" name="capaBotones4" style="position:absolute;width:100%;top:155px;visibility:hidden; border: 0; text-align: right">
                            <table cellpadding="0px" cellspacing="0px" style="border: 0;">
                                <tr>
                                    <td>
                                        <table cellpadding="0px" cellspacing="0px">
                                            <tr>
                                                <td>
                                                    <input type= "button" title='<%=descriptor.getDescripcion("toolTip_bGrabar")%>' alt='<%=descriptor.getDescripcion("toolTip_bGrabar")%>' class="botonGeneral" name="cmdRegistrarModificacion" onClick= "pulsarRegistrarModificar();return false;" value="<%=descriptor.getDescripcion("gbGrabar")%>" >
                                                </td>
                                                <TD style="width:2px"></TD>
                                                <td>
                                                    <input type="button" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>' class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelarModificar" onClick="pulsarCancelarModificar();return false;">
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </td>
                </tr>
            </table>
            <div class="sub3titulo" style="width:100%"><%= descriptor.getDescripcion("manTer_TitDomis")%></div>
            <table width="100%" cellspacing="2px" cellpadding="0px" border="0px">
                <tr>
                    <td style="width: 100%" align="center">
                        <table width="100%" cellspacing="0px" cellpadding="0px" border="0">
                            <tr>
                                <td style="width: 25%" class="etiqueta">
                                    <div id="domis" class="text"></div>
                                </td>

                                <!-- Se ha quitado el CHECKBOX porque no se utiliza -->
                                <td style="width: 55%" class="etiqueta">
                                    <input type="hidden" name="normalizado" >
                                </td>

                                <td class="etiqueta">
                                    <table cellpadding="0px" cellspacing="0px" style="border: 0;">
                                        <tr>
                                            <td>
                                                <!-- Botón ANTERIOR -->
                                                <table cellpadding="0px" cellspacing="0px">
                                                    <tr>
                                                        <td>
                                                            <input type= "button" class="botonGeneral" value="<<" name="cmdAnterior" onClick= "pulsarAnterior();return false;" style="width:25px;height:22px" alt = "<%=descriptor.getDescripcion("toolTip_DomAnt")%>" title = "<%=descriptor.getDescripcion("toolTip_DomAnt")%>">
                                                        </td>
                                                        <TD style="width:50px"></TD>
                                                        <td>
                                                            <input type= "button" class="botonGeneral" value=">>" name="cmdSiguiente" onClick= "pulsarSiguiente();return false;" style="width:25px;height:22px" alt = "<%=descriptor.getDescripcion("toolTip_DomSig")%>" title = "<%=descriptor.getDescripcion("toolTip_DomSig")%>">
                                                        </td>
                                                    </tr>
                                                </table>

                                                <!-- Fin botón ANTERIOR -->
                                            </td>
                                        </tr>
                                    </table>
                                </td>

                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td style="width: 100%" align="center">
                        <table border="0" width="100%" cellspacing="0px" cellpadding="0px">

                            <tr>
                                <td style="width: 17%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqProvincia")%>:</td>
                                <td style="width: 34%" class="columnP">
                                    <input class="inputTextoObligatorio" type="text" id="codProvincia" name="codProvincia" size="3">
                                    <input class="inputTextoObligatorio" type="text" id="descProvincia" name="descProvincia" style="width:215" readonly>
                                    <a id="anchorProvincia" name="anchorProvincia" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonProvincia" name="botonProvincia" style="cursor:pointer;" alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>"></span></a>
                                </td>
                                <td style="width: 14%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqMunicipio")%>:</td>
                                <td class="columnP">
                                    <input class="inputTextoObligatorio" type="text" id="codMunicipio" name="codMunicipio" size="3">
                                    <input id="descMunicipio" name="descMunicipio" type="text" class="inputTextoObligatorio" style="width:210" readonly>
                                    <a id="anchorMunicipio" name="anchorMunicipio" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonMunicipio" name="botonMunicipio" style="cursor:pointer;" alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>"></span></a>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <!-- anadir ECO/ESI -->
                <tr>
                    <td style="width: 100%" align="center">
                        <table border="0" width="100%" cellspacing="0px" cellpadding="0px">
                            <tr >
                                <td style="visibility:<%=ecv%>; width: 17%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqParroquia")%>:</td>
                                <td style="visibility:<%=ecv%>; width: 34%">
                                    <input type="text" id="codECO" name="codECO" class="inputTexto" size="3">
                                    <input type="text" id="descECO" name="descECO" class="inputTexto" style="width:215" readonly="true">
                                    <a href="" id="anchorECO" name="anchorECO"><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonECO" name="botonECO" style="cursor:pointer;" alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>"></span></a>
                                </td>
                                <td style="visibility:<%=esv%>; width: 14%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqLugar")%>:</td>
                                <td style="visibility:<%=esv%>">
                                    <input type="text" id="codESI" name="codESI" class="inputTexto" size="3">
                                    <input type="text" id="descESI" name="descESI" class="inputTexto" style="width:210" readonly="true">
                                    <a href="" id="anchorESI" name="anchorESI"><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonESI" name="botonESI" style="cursor:pointer;" alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>"></span></a>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td style="width: 100%" align="center">
                        <table border="0" width="100%" cellspacing="0px" cellpadding="0px">
                            <tr>
                                <td style="width: 17%" class="etiqueta"><%=descriptor.getDescripcion("etiqVia")%>:</td>
                                <td style="width: 34%" class="columnP">
                                    <input type="hidden" name="codVia">
                                    <input type="text" name="descVia" class="inputTexto" style="width:220px" maxlength=50 onkeypress="PasaAMayusculas(event);" onchange="limpiarVia();">
                                    <span class="fa fa-search" aria-hidden="true"  name="botonV" style="cursor:pointer" onclick="pulsarBuscarVia('buscarVias');" alt = "<%=descriptor.getDescripcion("altBuscarVia")%>" title = "<%=descriptor.getDescripcion("altBuscarVia")%>"></span>

                                </td>
                                <td style="visibility:<%=emplv%>; width: 14%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqEmplazamiento")%>:</td>
                                <td style="visibility:<%=emplv%>" class="columnP">
                                    <input type="text" id="txtDomicilio" name="txtDomicilio" class="inputTexto" style="width:264" maxlength=50
                                           onKeyPress="return PasaAMayusculas(event);">
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <!-- fin anadir ECO/ESI -->
                <tr>
                    <td>
                        <table border="0" width="100%" cellspacing="0px" cellpadding="0px">
                            <tr>
                                <td style="width: 81%">
                                    <table border="0" width="100%" cellspacing="0px" cellpadding="0px">
                                        <tr>
                                            <td style="width: 11%" class="etiqueta" align="center"><%=descriptor.getDescripcion("gEtiq_PNumero")%></td>
                                            <td style="width: 11%" class="etiqueta" align="center"><%=descriptor.getDescripcion("gEtiq_Letra")%></td>
                                            <td style="width: 11%" class="etiqueta" align="center"><%=descriptor.getDescripcion("gEtiq_UNumero")%></td>
                                            <td style="width: 11%" class="etiqueta" align="center"><%=descriptor.getDescripcion("gEtiq_Letra")%></td>
                                            <td style="width: 11%" class="etiqueta" align="center"><%=descriptor.getDescripcion("manTer_EtiqBLQ")%></td>
                                            <td style="width: 11%" class="etiqueta" align="center"><%=descriptor.getDescripcion("manTer_EtiqPTL")%></td>
                                            <td style="width: 11%" class="etiqueta" align="center"><%=descriptor.getDescripcion("gEtiq_Escalera")%></td>
                                            <td style="width: 11%" class="etiqueta" align="center"><%=descriptor.getDescripcion("gEtiq_Planta")%></td>
                                            <td style="width: 11%" class="etiqueta" align="center"><%=descriptor.getDescripcion("gEtiq_Puerta")%></td>
                                        </tr>
                                        <tr>
                                            <td style="width: 11%" class="columnP" align="center">
                                                <input name="txtNumDesde" type="text" class="inputTexto" id="txtNumDesde" size=6 maxlength=4 onKeyPress="javascript:return SoloDigitos(event);">
                                            </td>
                                            <td style="width: 11%" class="columnP" align="center">
                                                <input name="txtLetraDesde" type="text" class="inputTexto" id="txtLetraDesde" size=6 maxlength=1 onKeyPress="javascript:PasaAMayusculas(event);">
                                            </td>
                                            <td style="width: 11%" class="columnP" align="center">
                                                <input name="txtNumHasta" type="text" class="inputTexto" id="txtNumHasta" size=6 maxlength=4 onKeyPress="javascript:return SoloDigitos(event);">
                                            </td>
                                            <td style="width: 11%" class="columnP" align="center">
                                                <input name="txtLetraHasta" type="text" class="inputTexto" id="txtLetraHasta" size=6 maxlength=1 onKeyPress="javascript:PasaAMayusculas(event);">
                                            </td>
                                            <td style="width: 11%" class="columnP" align="center">
                                                <input type="text" class="inputTexto" name="txtBloque" size=6 maxlength=3 onKeyPress="javascript:PasaAMayusculas(event);">
                                            </td>
                                            <td style="width: 11%" class="columnP" align="center">
                                                <input type="text" class="inputTexto" name="txtPortal" size=6 maxlength=2 onKeyPress="javascript:PasaAMayusculas(event);">
                                            </td>
                                            <td style="width: 11%" class="columnP" align="center">
                                                <input type="text" class="inputTexto" name="txtEsc" size=6 maxlength=2 onKeyPress="javascript:PasaAMayusculas(event);">
                                            </td>
                                            <td style="width: 11%" class="columnP" align="center">
                                                <input type="text" class="inputTexto" name="txtPlta" size=6	maxlength=3 onKeyPress="javascript:PasaAMayusculas(event);">
                                            </td>
                                            <td style="width: 11%" class="columnP" align="center">
                                                <input type="text" class="inputTexto" name="txtPta" size=6 maxlength=4 onKeyPress="javascript:PasaAMayusculas(event);">
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td style="width: 19%">
                                    <div id="capaNormalizado" class="text" style="position:relative; width:100%; visibility:hidden;">
                                        <table class="tablaNormalizado" width="100%" border="0">
                                            <tr>
                                                <td style="width: 50%; height: 24px" class="etiqueta" align="center">Km.</td>
                                                <td style="width: 50%" class="etiqueta" align="center">Hm.</td>
                                            </tr>
                                            <tr>
                                                <td style="width: 50%" class="columnP" align="center">
                                                    <input type="text" class="inputTexto" name="txtKm" size=6 maxlength=3>
                                                </td>
                                                <td style="width: 50%" class="columnP" align="center">
                                                    <input type="text" class="inputTexto" name="txtHm" size=6 maxlength=3>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td style="width: 100%" align="center">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td style="width: 7%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqCodPostal")%>:</td>
                                <td style="width: 44%" class="columnP" >
                                    <input type="text" class="inputTexto" style="width:60px" maxlength=8 id="descPostal" name="descPostal">
                                    <a id="anchorPostal" name="anchorPostal" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonPostal" name="botonPostal" style="cursor:pointer" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>"></span></a>
                                </td>
                                <td style="visibility:<%=pv%>" style="width: 10%" colspan="2" class="etiqueta"><%=descriptor.getDescripcion("gEtiqPoblacion")%>:</td>
                                <td style="visibility:<%=pv%>" class="etiqueta">
                                    <input name="txtBarriada" type="text" class="inputTexto" id="txtBarriada" maxlength=40 style="width:274" onKeyPress="javascript:PasaAMayusculas(event);">
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td style="width: 100%; height: 30px">
                        <!-- Dando de Alta:
                                                                                         1. Registrar alta.
                                                                                         2. Cancelar alta.
                        -->
                        <div id="capaBotones6" name="capaBotones6" style="position:absolute;width:100%;top:390px; border: 0; text-align: right">
                            <table cellpadding="0px" cellspacing="0px" style="border: 0;">
                                <tr>
                                    <td>
                                        <table cellpadding="0px" cellspacing="0px">
                                            <tr>
                                                <td>
                                                    <input type= "button" title='<%=descriptor.getDescripcion("toolTip_bAltaDom")%>' alt='<%=descriptor.getDescripcion("toolTip_bAltaDom")%>' class="botonGeneral" name="cmdGrabarDomicilio" onClick = "pulsarGrabarDomicilio();return	false;" value="<%=descriptor.getDescripcion("gbGrabar")%>" >
                                                </td>
                                                <TD style="width:2px"></TD>
                                                <td>
                                                    <input type="button" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' class="botonGeneral" name="cmdCancelarAltaDomic" onClick="pulsarCancelarAltaDomicilio();return false;" value="<%=descriptor.getDescripcion("gbcaNcelar")%>" >
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <!-- Página buscada:
                                                                                         1. Modificar.
                                                                                         2.Anular.
                                                                                         3.Cancelar página buscada.
                        -->
                        <div id="capaBotones7" name="capaBotones7" style="position:absolute;width:100%;top:390px;visibility:hidden; border: 0; text-align: right">
                            <table cellpadding="0px" cellspacing="0px" style="border: 0;">
                                <tr>
                                    <td>
                                        <table cellpadding="0px" cellspacing="0px">
                                            <tr>
                                                <td>
                                                    <input type= "button" title='<%=descriptor.getDescripcion("toolTip_bAltaDom")%>' alt='<%=descriptor.getDescripcion("toolTip_bAltaDom")%>' class="botonGeneral" name="cmdAltaDom" onClick = "pulsarAltaDomicilio();return false;" value="<%=descriptor.getDescripcion("gbAlta")%>" >
                                                </td>
                                                <TD style="width:2px"></TD>
                                                <td>
                                                    <input type= "button" title='<%=descriptor.getDescripcion("toolTip_bModificaDom")%>' alt='<%=descriptor.getDescripcion("toolTip_bModificaDom")%>' class="botonGeneral" name="cmdModificarDomicilio" onClick = "pulsarModificarDomicilio();return false;" value="<%=descriptor.getDescripcion("gbmOdificar")%>" >
                                                </td>
                                                <TD style="width:2px"></TD>
                                                <td>
                                                    <input type= "button" title='<%=descriptor.getDescripcion("toolTip_bBajaDom")%>' alt='<%=descriptor.getDescripcion("toolTip_bBajaDom")%>' class="botonGeneral" name="cmdBajaDomicilio" onClick = "pulsarBajaDomicilio();return false;" value="<%=descriptor.getDescripcion("gbBaJa")%>" >
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <!-- Modificando:
                                                                                         1. Registrar cambios.
                                                                                         2. Cancelar cambios.
                        -->
                        <div id="capaBotones8" name="capaBotones8" style="position:absolute;width:100%;top:390px;visibility:hidden; border: 0; text-align: right">
                            <table cellpadding="0px" cellspacing="0px" style="border: 0;">
                                <tr>
                                    <td>
                                        <table cellpadding="0px" cellspacing="0px">
                                            <tr>
                                                <td>
                                                    <input type= "button" title='<%=descriptor.getDescripcion("toolTip_bModificaDom")%>' alt='<%=descriptor.getDescripcion("toolTip_bModificaDom")%>' class="botonGeneral" name="cmdRegistrarModificacion" onClick = "pulsarRegistrarModificarDomicilio();return	false;" value="<%=descriptor.getDescripcion("gbGrabar")%>" >
                                                </td>
                                                <TD style="width:2px"></TD>
                                                <td>
                                                    <input type="button" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>' class="botonGeneral" name="cmdCancelarModificar" onClick="pulsarCancelarModificarDomicilio();return false;" value="<%=descriptor.getDescripcion("gbcaNcelar")%>" >
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div id="capaBotones10" name="capaBotones10" style="position:absolute;width:100%;top:390px;visibility:hidden; border: 0; text-align: right">
                            <table cellpadding="0px" cellspacing="0px" style="border: 0;">
                                <tr>
                                    <td>
                                        <table cellpadding="0px" cellspacing="0px">
                                            <tr>
                                                <td>
                                                    <input type= "button" title='<%=descriptor.getDescripcion("toolTip_bAltaDom")%>' alt='<%=descriptor.getDescripcion("toolTip_bAltaDom")%>' class="botonGeneral" name="cmdAltaDom" onClick = "pulsarAltaDomicilio();return false;" value="<%=descriptor.getDescripcion("gbAlta")%>" >
                                                </td>
                                                <TD style="width:2px"></TD>
                                                <td>
                                                    <input type= "button" title='<%=descriptor.getDescripcion("toolTip_bModificaDom")%>' alt='<%=descriptor.getDescripcion("toolTip_bModificaDom")%>' class="botonGeneral" name="cmdRegistrarModificacion" onClick = "pulsarRegistrarModificarDomicilio();return	false;" value="<%=descriptor.getDescripcion("gbGrabar")%>" >
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </td>
                </tr>
            </table>
            <div class="botoneraPrincipal">
                <input type= "button" title='<%=descriptor.getDescripcion("toolTip_bSalir")%>' class="botonGeneral" name="cmdSalir" onClick="pulsarCancelar();return false;" value="<%=descriptor.getDescripcion("gbSalir")%>" >
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
            var	comboPostal	= new	Combo("Postal");
            var	auxCombo = "comboMunicipio";

            comboTipoDoc.change = function() {
                document.forms[0].txtDNI.value="";
                actualizaPersonaFJ();
            }
            comboProvincia.change	= function() {
                auxCombo="comboMunicipio";
                limpiar(["codMunicipio","descMunicipio"]);
                if(comboProvincia.cod.value.length!=0){
                    desHabilitarECOESIVIA(true);
                    cargarListaMunicipios();
                }else{
                comboMunicipio.addItems([],[]);
            }
        }

        comboMunicipio.change	= function() {
            limpiaVial();
            limpiaCodPostal();
            auxCombo = "comboPostal";
            if(comboMunicipio.cod.value.length!=0){
                if( (comboMunicipio.cod.value=="<%=ptVO.getMunicipio()%>") &&  ( comboProvincia.cod.value=="<%=ptVO.getProvincia()%>"))
                    desHabilitarECOESIVIA(false);
                    else desHabilitarECOESIVIA(true);
                    cargarListaCodPostales();
                }else{
                comboMunicipio.selectItem(0);
            }
        }
        function cargarComboBox(cod, des){
            eval(auxCombo+".addItems(cod,des)");
        }
        /* anadir ECO/ESI */
        var comboECO = new Combo("ECO");
        var comboESI = new Combo("ESI");
        comboECO.change = function() {
            auxCombo='comboESI';
            limpiar(['codESI','descESI','codVia','descVia','txtCodVia', 'idVia', 'txtBarriada']);
            if(comboECO.cod.value.length!=0){
                var i = comboECO.selectedIndex-1;
                if(i>=0){
                    document.forms[0].txtBarriada.value = descECOs[i];
                }
            }
            cargarListas();
        }
        comboESI.change = function() {
            limpiar(['codVia','descVia','txtCodVia', 'idVia','txtBarriada']);
            if(comboESI.cod.value.length!=0){
                var i = comboESI.selectedIndex-1;
                if(i>=0){
                    //comboECO.buscaLinea(listaECOESIs[i][0]);
                    document.forms[0].codECO.value = listaECOESIs[i][0];
                    document.forms[0].descECO.value = listaECOESIs[i][1];
                    document.forms[0].txtBarriada.value = descESIs[i] +" - "+document.forms[0].descECO.value;
                }
                cargarListas();
            }
        }
        function mostrarDescripcionTipoDoc(){}
        </script></BODY></HTML>
