<%@page contentType="text/html; charset=iso-8859-1"	language="java" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="java.util.Vector"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<html>
    <head>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
        <title>Búsqueda</title>

        <%
            ParametrosTerceroValueObject ptVO = null;
            int idioma = 1;
            int apl = 3;
            int cod_org = 1;
            int cod_dep = 1;
            int entCod = 1;
            String css="";
            String funcion = "";
            String inicioTercero = "";
            if (session.getAttribute("usuario") != null) {
                UsuarioValueObject usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                ptVO = (ParametrosTerceroValueObject) session.getAttribute("parametrosTercero");
                idioma = usuarioVO.getIdioma();
                apl = usuarioVO.getAppCod();
                cod_org = usuarioVO.getUnidadOrgCod();
                cod_dep = usuarioVO.getDepCod();
                entCod = usuarioVO.getEntCod();
                css=usuarioVO.getCss();
            }
            if (session.getAttribute("inicioTercero") != null) {
                inicioTercero = (String) session.getAttribute("inicioTercero");
            } else {
                inicioTercero = "no";
            }
            session.removeAttribute("inicioTercero");
            Config m_Conf = ConfigServiceHelper.getConfig("common");
            String JSP_entidadColectiva = m_Conf.getString("JSP.EntidadColectiva");
            String JSP_entidadSingular = m_Conf.getString("JSP.EntidadSingular");

            Config confTerceros = ConfigServiceHelper.getConfig("Terceros");
            String mostrarOrigen = confTerceros.getString("Terceros.mostrarOrigen");

            String ecv = "visible";
            String esv = "visible";
            if ("no".equals(JSP_entidadColectiva)) {
                ecv = "hidden";
            }
            if ("no".equals(JSP_entidadSingular)) {
                esv = "hidden";
            }
            Log _log = LogFactory.getLog(this.getClass());
            if (_log.isDebugEnabled()) {
                _log.debug("terceros/ventanaSeleccion.jsp: session->inicioTercero = " + inicioTercero);
            }
            String statusBar = m_Conf.getString("JSP.StatusBar");
        %>


        <link	rel="stylesheet" href="<%=request.getContextPath()%><%=css%>" type="text/css">


        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"	property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"	property="apl_cod" value="<%=apl%>"	/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaBusquedaTerceros.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/seleccionBusquedaTerceros.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript">
            <!-- Listas	de valores. -->
            var codTipoDocs	= new	Array();
            var descTipoDocs = new Array();
            var persFJ = new Array();
            var codPaises =	new Array();
            var descPaises = new Array();
            var codProvincias = new Array();
            var descProvincias = new Array();
            var codMunicipios = new Array();
            var descMunicipios = new Array();
            var codMunicipiosDefecto = new Array();
            var descMunicipiosDefecto = new Array();
            var operadorConsulta = '|&:<>!=';
            var ventana = true;
            var tipo;
  <%BusquedaTercerosForm bForm = (BusquedaTercerosForm) session.getAttribute("BusquedaTercerosForm");
            Vector listaDocs = bForm.getListaTipoDocs();
            Vector listaProvincias = bForm.getListaProvincias();
            Vector listaMunicipios = bForm.getListaMunicipios();
            if (_log.isDebugEnabled()) {
                _log.debug("el tamaño de la lista de municipios es : " + listaMunicipios.size());
            }

            int lengthDocs = listaDocs.size();
            int lengthProvs = listaProvincias.size();
            int lengthMun = listaMunicipios.size();
            int i = 0;

            String lcodProv = "";
            String ldescProv = "";
            String lcodMun = "";
            String ldescMun = "";
            String lcodDocs = "";
            String ldescDocs = "";
            String lpersJF = "";

            if (lengthDocs > 0) {
                for (i = 0; i < lengthDocs - 1; i++) {
                    GeneralValueObject doc = (GeneralValueObject) listaDocs.get(i);
                    lcodDocs += "'" + (String) doc.getAtributo("codTipoDoc") + "',";
                    ldescDocs += "'" + (String) doc.getAtributo("descTipoDoc") + "',";
                    lpersJF += "'" + (String) doc.getAtributo("persFJ") + "',";
                }
                GeneralValueObject doc = (GeneralValueObject) listaDocs.get(i);
                lcodDocs += "'" + (String) doc.getAtributo("codTipoDoc") + "'";
                ldescDocs += "'" + (String) doc.getAtributo("descTipoDoc") + "'";
                lpersJF += "'" + (String) doc.getAtributo("persFJ") + "'";
            }

            if (lengthProvs > 0) {
                for (i = 0; i < lengthProvs - 1; i++) {
                    GeneralValueObject prov = (GeneralValueObject) listaProvincias.get(i);
                    lcodProv += "\"" + (String) prov.getAtributo("codigo") + "\",";
                    ldescProv += "\"" + (String) prov.getAtributo("descripcion") + "\",";
                }
                GeneralValueObject prov = (GeneralValueObject) listaProvincias.get(i);
                lcodProv += "\"" + (String) prov.getAtributo("codigo") + "\"";
                ldescProv += "\"" + (String) prov.getAtributo("descripcion") + "\"";
            }

            if (lengthMun > 0) {
                for (i = 0; i < lengthMun - 1; i++) {
                    GeneralValueObject mun = (GeneralValueObject) listaMunicipios.get(i);
                    lcodMun += "\"" + (String) mun.getAtributo("codMunicipio") + "\",";
                    ldescMun += "\"" + (String) mun.getAtributo("nombreOficial") + "\",";
                }
                GeneralValueObject mun = (GeneralValueObject) listaMunicipios.get(i);
                lcodMun += "\"" + (String) mun.getAtributo("codMunicipio") + "\"";
                ldescMun += "\"" + (String) mun.getAtributo("nombreOficial") + "\"";
            }

            Vector listaECOs = bForm.getListaECOs();
            Vector listaESIs = bForm.getListaESIs();
            //Vector listaVias = bForm.getListaVias();
            int lengthECOs = listaECOs.size();
            int lengthESIs = listaESIs.size();
            //int lengthVias = listaVias.size();
            String lcodECOS = "";
            String ldescECOS = "";
            String lcodESIS = "";
            String ldescESIS = "";
            String lECOESIS = "";
            i = 0;

            if (lengthECOs > 0) {
                for (i = 0; i < lengthECOs - 1; i++) {
                    GeneralValueObject eco = (GeneralValueObject) listaECOs.get(i);
                    lcodECOS += "\"" + (String) eco.getAtributo("codECO") + "\",";
                    ldescECOS += "\"" + (String) eco.getAtributo("descECO") + "\",";
                }
                GeneralValueObject eco = (GeneralValueObject) listaECOs.get(i);
                lcodECOS += "\"" + (String) eco.getAtributo("codECO") + "\"";
                ldescECOS += "\"" + (String) eco.getAtributo("descECO") + "\"";
            }

            if (lengthESIs > 0) {
                for (i = 0; i < lengthESIs - 1; i++) {
                    GeneralValueObject esi = (GeneralValueObject) listaESIs.get(i);
                    lcodESIS += "\"" + (String) esi.getAtributo("codEntidadSingular") + "\",";
                    ldescESIS += "\"" + (String) esi.getAtributo("nombreOficial") + "\",";
                    lECOESIS += "[\"" + (String) esi.getAtributo("codEntidadColectiva") + "\",\"" + (String) esi.getAtributo("descEntidadColectiva") + "\"],";
                }
                GeneralValueObject esi = (GeneralValueObject) listaESIs.get(i);
                lcodESIS += "\"" + (String) esi.getAtributo("codEntidadSingular") + "\"";
                ldescESIS += "\"" + (String) esi.getAtributo("nombreOficial") + "\"";
                lECOESIS += "[\"" + (String) esi.getAtributo("codEntidadColectiva") + "\",\"" + (String) esi.getAtributo("descEntidadColectiva") + "\"]";
            }%>
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
                    codESIs= [<%=lcodESIS%>];
                        descESIs= [<%=ldescESIS%>];
                            listaECOESIs = [<%=lECOESIS%>];

                                codESIsOld = codESIs;
                                descESIsOld = descESIs;
                                listaECOESIsOld = listaECOESIs;


                                function cargarListas(){
                                    document.forms[0].opcion.value="cargarListas";
                                    document.forms[0].target="oculto";
                                    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                                    document.forms[0].submit();
                                }

                                function desHabilitarECOESIVIA(valor) {
                                    limpiarECOESIVIA();
                                    if (valor) {
                                        comboECO.deactivate();
                                        comboESI.deactivate();
                                        //comboVia.deactivate();
                                        /* Permitir vias de otro municipio 14/06/05
                                        habilitarImagen([document.getElementById("botonT")], false);
                                        deshabilitarGeneral([document.forms[0].codVia,document.forms[0].descVia]);
                                        */
                                    } else {
                                    comboECO.activate();
                                    comboESI.activate();
                                    //comboVia.activate();
                                    /* Permitir vias de otro municipio 14/06/05
                                    habilitarImagen([document.getElementById("botonT")], true);
                                    habilitarGeneral([document.forms[0].codVia,document.forms[0].descVia]);
                                    */
                                }
                            }

                            function limpiarECOESIVIA(){
                                comboECO.selectItem(-1);
                                comboESI.selectItem(-1);
                                //comboVia.selectItem(-1);
                                document.forms[0].codVia.value="";
                                document.forms[0].descVia.value="";
                                limpiarVia();
                            }
                            /* fin anadir ECO/ESI */
                            function cargarListaTerceros(){
                                if(Terceros.length>0){
                                    var lista = new Array();
                                    for (var i = 0; i < Terceros.length; i++) {
                                        <% if (mostrarOrigen.equals("si")) {%>
                                        lista[i] = [Terceros[i][3],Terceros[i][4],Terceros[i][5],Terceros[i][6],Terceros[i][19]];
                                        <%} else {%>
                                        lista[i] = [Terceros[i][3],Terceros[i][4],Terceros[i][5],Terceros[i][6]];
                                        <%}%>
                                    }
                                    listaSel = lista;
                                    tab.lineas=lista;
                                    refresco();
                                    var frameOculto ="oculto";
                                    if (ventana) frameOculto = "oculto";
                                    numeroRegistros =	eval("top."+frameOculto+".numeroPaginas");
                                    numeroPaginas = Math.ceil(numeroRegistros/lineasPagina);
                                    domlay('enlace',1,0,0,enlaces(tab,listaSel));

                                } else{
                                jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoRegCoinc")%>');
                                    <% if (((request.getParameter("preguntaAlta") == null) || (request.getParameter("preguntaAlta").equals("si")))) {%>
                                    if (jsp_alerta("",'<%=descriptor.getDescripcion("msjNuevoInteresado")%>')) {
                                        pulsarAltaDesdeBuscar();
                                    }
                                    <% }%>
                                }
                            }

                            function cerrar(){
                                var retorno;
                                if(tipo=="TERCEROS"){
                                    var array =	new Array();
                                    if((Terceros.length>0)&&(indice>=0))
                                        array = [Terceros[indice]];
                                    self.parent.opener.retornoXanelaAuxiliar(array);
                                } else  {
                                    self.parent.opener.retornoXanelaAuxiliar(TerceroSel);
                                //opener.rellenarTercero(TerceroSel,vectorCampos);
                            }
                        }

                        function cambiarSituacion(i){
                            var situacion	= Terceros[i][12];
                            if(situacion=="B"){
                                if(jsp_alerta("",'<%=descriptor.getDescripcion("msjUsuarBaja")%>')){
                                    document.forms[0].situacion.value="A";
                                    document.forms[0].opcion.value="cambiaSituacionTercero";
                                    document.forms[0].target="oculto";
                                    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                                    document.forms[0].submit();
                                    Terceros[i][12]="A";
                                }else{
                                //borrarInteresado();
                            }
                        }else{
                        /*  mostrarDescripcionTipoDoc();
                        domlay('desplegable',0,0,0,null);
                        capaVisible	= false;*/
                    }
                }

                function cargaDomicilios(idTercero){
                    listaDoms = new Array();
                    var vectorCampos = new Array();
                    var vectorDatos = new Array();
                    vectorCampos = ['codTerc','numModifTerc','txtIdTercero','txtVersion','codTipoDoc','txtDNI','txtInteresado',
                        'txtApell1','txtApell2','txtPart','txtPart2','txtTelefono','txtCorreo'];
                    vectorDatos =	[Terceros[idTercero][0],Terceros[idTercero][1],Terceros[idTercero][0],Terceros[idTercero][1],
                        Terceros[idTercero][2],Terceros[idTercero][3],Terceros[idTercero][4],Terceros[idTercero][5],
                        Terceros[idTercero][6],Terceros[idTercero][7],Terceros[idTercero][8],Terceros[idTercero][9],
                        Terceros[idTercero][10]];
                    comboTipoDoc.buscaCodigo(document.forms[0].codTipoDoc.value);
                    rellenar(vectorDatos,vectorCampos);
                    comboTipoDoc.buscaCodigo(document.forms[0].codTipoDoc.value);
                    cambiarSituacion(idTercero);
                    if(Terceros[idTercero][12]=="A"){
                        lCasas = Terceros[idTercero][18];
                        for (var i=0; i <	lCasas.length; i++){
                            var	domicilio =	"";

                            if(lCasas[i][29] == "") {
                                domicilio += lCasas[i][3];
                            } else {
                            domicilio = (lCasas[i][20]!="") ? domicilio+lCasas[i][20]+" ":domicilio;      // DESCRIPCION DEL TIPO DE VIA
                            domicilio = (lCasas[i][29] != "") ? domicilio + lCasas[i][29] + " " : domicilio;
                            domicilio = (lCasas[i][9]!=0) ? domicilio+" "+lCasas[i][9]:domicilio;         // PRIMER NUMERO DE LA VIA
                            domicilio = (lCasas[i][10]!="") ? domicilio+" "+lCasas[i][10]+" ":domicilio;  // PRIMERA LETRA DE LA VIA
                            domicilio = (lCasas[i][11]!=0) ? domicilio+" "+lCasas[i][11]:domicilio;       // ULTIMO NUMERO DE LA VIA
                            domicilio = (lCasas[i][12]!="") ? domicilio+" "+lCasas[i][12]:domicilio;      // ULTIMA LETRA DE LA VIA
                            domicilio = (lCasas[i][13]!="") ? domicilio+" Bl. "+lCasas[i][13]:domicilio;  // BLOQUE
                            domicilio = (lCasas[i][14]!="") ? domicilio+" Portal "+lCasas[i][14]:domicilio; // PORTAL
                            domicilio = (lCasas[i][15]!="") ? domicilio+" Esc. "+lCasas[i][15]:domicilio; // ESCALERA
                            domicilio = (lCasas[i][16]!="") ? domicilio+" "+lCasas[i][16]+"º ":domicilio; // PLANTA
                            domicilio = (lCasas[i][17]!="") ? domicilio+lCasas[i][17]:domicilio;          // PUERTA
                        }

                        <% if (mostrarOrigen.equals("si")) {%>
                        listaDoms[i] = [lCasas[i][1],lCasas[i][2],domicilio,lCasas[i][4],lCasas[i][30]];
                        <%} else {%>
                        listaDoms[i] = [lCasas[i][1],lCasas[i][2],domicilio,lCasas[i][4]];
                        <%}%>
                    }
                    tabD.lineas= listaDoms;
                    refreshD();
                }

                //inicializaListaCasas(1);
            }

            /* Para navegacion */
            var	listaSel = new Array();
            var	enlacesPagina  = 10;
            var	lineasPagina   = 10;

            var	paginaActual   = 1;
            var	paginaInferior = 1;
            var	paginaSuperior = enlacesPagina;

            var	numeroPaginas;
            var	numeroRegistros;

            function irPrimeraPagina(tab,lista) {
                paginaActual	 = 1;
                paginaInferior = 1;
                paginaSuperior = enlacesPagina;
                inicializaLista(paginaActual,tab,lista);
            }

            function irUltimaPagina(tab, lista) {
                paginaActual   = Math.ceil(numeroRegistros/lineasPagina);
                paginaInferior = 1;
                paginaSuperior = enlacesPagina;
                while	(paginaActual > paginaSuperior) {
                    paginaInferior = paginaSuperior +1;
                    if (numeroPaginas > paginaInferior-1+enlacesPagina)
                        paginaSuperior = paginaInferior-1+enlacesPagina;
                    else paginaSuperior =	numeroPaginas;
                }
                inicializaLista(paginaActual,tab,lista);
            }

            function irNPaginasSiguientes(pagActual, tab, lista){

                pagActual =	parseInt(pagActual);
                var incremento = paginaSuperior	+ 1 -	pagActual;
                if (pagActual +	incremento > numeroPaginas) {
                    pagActual	= Math.ceil(numeroRegistros/lineasPagina); // Ultima
                }else {
                pagActual	+= incremento;
                paginaInferior = pagActual;
                if (paginaInferior + enlacesPagina > numeroPaginas)
                    paginaSuperior=numeroPaginas;
                else paginaSuperior= paginaInferior + enlacesPagina	-1;
            }
            inicializaLista(pagActual,tab,lista);
        }

        function irNPaginasAnteriores(pagActual, tab, lista){
            pagActual =	parseInt(pagActual);
            var incremento = enlacesPagina + (pagActual - paginaInferior);
            if (pagActual - incremento <=	0)
                pagActual	= 1; // Primera
            else {
                pagActual	-= incremento;
                paginaInferior = pagActual;
                if (paginaInferior + enlacesPagina > numeroPaginas)
                    paginaSuperior=numeroPaginas;
                else paginaSuperior= paginaInferior + enlacesPagina	-1;
            }
            inicializaLista(pagActual,tab,lista);
        }


        function enlaces(tab,listaSel){
            var htmlString = " ";
            if(numeroPaginas > 1){
                htmlString += '<table class="fondoNavegacion" cellpadding="2" cellspacing="0" align="center"><tr>'
                if(paginaActual >	1) {
                    htmlString += '<td width="35" class="botonNavegacion">';
                    htmlString += '<a href="javascript:irPrimeraPagina(tab,listaSel);" class="linkNavegacion"	target="_self">';
                    htmlString += '  |<< ';
                    htmlString += '</a></td>';
                    htmlString += '<td width="5">&nbsp;</td>';
                    htmlString += '<td width="35" class="botonNavegacion">';
                    htmlString += '<a class="linkNavegacion" href="javascript:irNPaginasAnteriores('+ eval(paginaActual) + ',tab,listaSel)" target="_self">';
                    htmlString += ' << ';
                    htmlString += '</a></td>';
                }else
                htmlString += '<td width="75">&nbsp;</td>';
                htmlString += '</td><td	align="center" width="300">';

                for(var i=0; i < numeroPaginas; i++){
                    if(((i+1)>= paginaInferior)	&& (i<paginaSuperior)){
                        if((i+1) == paginaActual)
                            htmlString += '<span class="indiceNavSelected">'+ (i+1) + '</span>&nbsp;&nbsp;';
                        else
                            htmlString += '<a class="indiceNavegacion" href="javascript:inicializaLista('+ (i+1) + ',tab,listaSel)" target="_self">'+ (i+1) +	'</a>&nbsp;&nbsp;';
                    }
                }

                if(paginaActual <	numeroPaginas){
                    htmlString +=	'</td><td width="35" class="botonNavegacion">';
                    htmlString +=	'<a class="linkNavegacion"	href="javascript:irNPaginasSiguientes('+ eval(eval(paginaActual))+ ',tab,listaSel)"	target="_self">';
                    htmlString += ' >> ';
                    htmlString += '</a></td>';
                    htmlString +=	'<td width="5"></td>';
                    htmlString +=	'<td width="35" class="botonNavegacion">';
                    htmlString +=	'<a href="javascript:irUltimaPagina(tab,listaSel);" class="linkNavegacion" target="_self">';
                    htmlString +=	' >>| ';
                    htmlString +=	'</a></td>';
                } else
                htmlString += '</td><td width="70">&nbsp;</td>';
                htmlString +=	'</tr></table>';
            }

            var registroInferior = ((paginaActual	- 1) * lineasPagina) + 1;
            var registroSuperior = (paginaActual * lineasPagina);

            if(paginaActual == numeroPaginas)
                registroSuperior = numeroRegistros;

            if (listaSel.length	> 0)
                htmlString += '<center><font class="textoSuelto">Resultados&nbsp;' + registroInferior + '&nbsp;a&nbsp;' + registroSuperior + '&nbsp;de&nbsp;' +	numeroRegistros +	'&nbsp;encontrados.</font></center>'
            else
                htmlString += '<center><font class="textoSuelto">&nbsp;' + numeroRegistros + '&nbsp;encontrados.</font></center>'

            return (htmlString);
        }

        function inicializaLista(numeroPagina,tab,listaSel){
            paginaActual = numeroPagina;
            pleaseWait1('on',top.mainFrame);
            document.forms[0].lineasPagina.value = lineasPagina;
            document.forms[0].pagina.value = numeroPagina;
            document.forms[0].opcion.value="recargaBusquedaTerceros";
            if(ventana){
                document.forms[0].target="oculto";
                document.forms[0].ventana.value="true";
            }else
            document.forms[0].target="oculto";
            document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
            document.forms[0].submit();
            limpiarTodo2();
            /*var j	= 0;
            paginaActual = numeroPagina;
            inicio = (numeroPagina - 1) * lineasPagina;
            fin = Math.min(numeroPagina * lineasPagina,listaSel.length);
            listaP = new Array();
            for(var	i=inicio;i<fin;i++){
            listaP[j] =	listaSel[i];
            j++;
            }
            tab.lineas=listaP;
            */
        }

        function mostrarDescripcionTipoDoc(){
            comboTipoDoc.buscaCodigo(document.forms[0].codTipoDoc.value);
            actualizaPersonaFJ();
        }

        function esPersonaFisica(){
            var pFJ	= document.forms[0].perFJ.value;
            if(pFJ==1){
                return true;
            }else{
            return false;
        }
    }

    function actualizaPersonaFJ(){
        var tipo = document.forms[0].codTipoDoc.value;
        var i;
        if (tipo!=""){
            for(i=0;i<codTipoDocs.length;i++){
                if(codTipoDocs[i]==tipo) break;
            }
            if(i<codTipoDocs.length){
                document.forms[0].perFJ.value= persFJ[i];
            }else{
            document.forms[0].perFJ.value=1;
        }
        var vector = new Array(document.forms[0].txtApell1,document.forms[0].txtApell2);
        if(!esPersonaFisica()){
            deshabilitarGeneral(vector);
        }else
        habilitarGeneral(vector);
    }
}

function cargarListaMunicipios(){
    document.forms[0].opcion.value="cargarMunicipios";
    document.forms[0].target=(ventana)?"oculto":"oculto";
    document.forms[0].ventana.value=(ventana)?"true":"false";
    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
    document.forms[0].submit();
}

function limpiaVial(){
    var codVia =	document.forms[0].txtCodVia.value;
    if (codVia!=""){
        document.forms[0].txtDomicilio.value = "";
        document.forms[0].txtCodVia.value =	"";
        document.forms[0].idVia.value =	"";
        document.forms[0].codTVia.value =	"";
    }
}

function limpiaCodPostal(){
    document.forms[0].descPostal.value = "";
}

function borrarProvMun(){
    document.forms[0].codProvincia.value = "";
    document.forms[0].descProvincia.value	= "";
    document.forms[0].codMunicipio.value = "";
    document.forms[0].descMunicipio.value	= "";
    limpiaVial();
    //limpiaCodPostal();
}

function borrarMun(){
    document.forms[0].codMunicipio.value = "";
    document.forms[0].descMunicipio.value	= "";
    limpiaVial();
    //limpiaCodPostal();
}

function recuperaDatosIniciales(){
    ventana=true;
    codTipoDocs = [<%=lcodDocs%>];
    descTipoDocs = [<%=ldescDocs%>];
    persFJ = [<%=lpersJF%>];
    codProvincias = [<%=lcodProv%>];
    descProvincias = [<%=ldescProv%>];
    codMunicipios = [<%=lcodMun%>];
    codMunicipiosDefecto = [<%=lcodMun%>];
    descMunicipios = [<%=ldescMun%>];
    descMunicipiosDefecto = [<%=ldescMun%>];

      comboTipoDoc.addItems(codTipoDocs,descTipoDocs);
      comboProvincia.addItems(codProvincias,descProvincias);
      comboMunicipio.addItems(codMunicipios,descMunicipios);
      var args = self.parent.opener.xanelaAuxiliarArgs;
      Terceros = args[0];
      tipo = args[1];
      if(tipo=="SGE"){
          document.getElementById("Domicilios").style.visibility = 'hidden';
          document.getElementById("tablaDomicilios").style.visibility	= 'hidden';
      }
      if(Terceros.length>0){
          document.forms[0].codTipoDoc.value = Terceros[0][2];
          document.forms[0].txtDNI.value = Terceros[0][3];
          mostrarDescripcionTipoDoc();
          cargarListaTerceros();
          numeroRegistros =	1
          paginaActual=1;
          paginaInferior=1;
          paginaSuperior=1;
          numeroPaginas=1;
          domlay('enlace',1,0,0,enlaces(tab,listaSel));
      }
     /* anadir ECO/ESI */
      comboECO.addItems(codECOs,descECOs);
      comboESI.addItems(codESIs,descESIs);
      /* Fin anadir ECO/ESI */
      pleaseWait1('off',top.mainFrame);
  }

  function inicio(){
      ventana=false;
      codTipoDocs = [<%=lcodDocs%>];
      descTipoDocs = [<%=ldescDocs%>];
      persFJ =[<%=lpersJF%>];
      codProvincias = [<%=lcodProv%>];
      descProvincias = [<%=ldescProv%>];
      codMunicipios = [<%=lcodMun%>];
      codMunicipiosDefecto = [<%=lcodMun%>];
      descMunicipios = [<%=ldescMun%>];
      descMunicipiosDefecto = [<%=ldescMun%>];
      comboTipoDoc.addItems(codTipoDocs,descTipoDocs);
      comboProvincia.addItems(codProvincias,descProvincias);
      comboMunicipio.addItems(codMunicipios,descMunicipios);
        comboProvincia.selectItem(-1);
      comboMunicipio.selectItem(-1);
      /* anadir ECO/ESI */
      comboECO.addItems(codECOs,descECOs);
      comboESI.addItems(codESIs,descESIs);
      desHabilitarECOESIVIA (true);
      /* Fin anadir ECO/ESI */
      pleaseWait1('off',top.mainFrame);
   }

   function algunoNoVacio(vector){
       var res	= false;
       for(i=0;i<vector.length;i++){
           var valor =	eval("document.forms[0]."+vector[i]+".value");
           if(valor!=""){
               res	= true;
               break;
           }
       }
       return res;
   }

   function pulsarBuscar(){
       var vectorCampos = new Array();
       vectorCampos = ['codTipoDoc','descTipoDoc','txtDNI','txtInteresado','txtApell1', 'txtApell2','txtCodVia',
           'txtDomicilio','txtNumDesde', 'txtLetraDesde','txtNumHasta','txtLetraHasta','codTerc','txtVersion',
           'codECO','codESI','codVia'];
       if(algunoNoVacio(vectorCampos)){
         if (viaBuscada()) {
               pleaseWait1('on',top.mainFrame);
               document.forms[0].txtDNI.value=document.forms[0].txtDNI.value.toUpperCase();
               document.forms[0].opcion.value="buscarTerceros";
               if(ventana){
                   document.forms[0].target="oculto";
                   document.forms[0].ventana.value="true";
               } else {
               document.forms[0].target="oculto";
           }
           document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
           document.forms[0].submit();
           limpiarTablaDomicilio();
           paginaActual=1;
           paginaInferior=1;
           paginaSuperior=enlacesPagina;
         } else {
             jsp_alerta("A",'<%=descriptor.getDescripcion("msjViaNoBusc")%>');
         }
       } else {
           jsp_alerta("A",'<%=descriptor.getDescripcion("msjCritBusqTerc")%>');
       }
           limpiarResultadosBusqueda();
       }

       function buscarPorId(){
           pleaseWait1('on',top.mainFrame);
           document.forms[0].txtDNI.value=document.forms[0].txtDNI.value.toUpperCase();
           document.forms[0].opcion.value="buscar_por_id";
           if(ventana){
               document.forms[0].target="oculto";
               document.forms[0].ventana.value="true";
           } else document.forms[0].target="oculto";

           document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
           document.forms[0].submit();
           limpiarTablaDomicilio();
           paginaActual=1;
           paginaInferior=1;
           paginaSuperior=enlacesPagina;
       }


       function pulsarSalir(){
           self.parent.opener.retornoXanelaAuxiliar();
       }

       function recuperaBusquedaTerceros(datos) {
           pleaseWait1('off',top.mainFrame);
           //limpiarTodo();
           Terceros = datos;
           cargarListaTerceros();
       }

       function recuperaBusquedaTercerosSeleccionar(datos,posTerc,posDom) {
           pleaseWait1('off', top.mainFrame);
           //limpiarTodo();
           // En teoria solo debe recuperar un tercero y un domicilio, asi que lo forzamos a que tenga unicamente esos
           // datos.
           Terceros = [datos[posTerc]];
           lCasas = datos[posTerc][18];
           enviaSeleccionDoms(posTerc, posDom);
       }

       function limpiarResultadosBusqueda(){
           tab.lineas =	new Array();
           tabD.lineas =	new Array();
           refresco();
       }

       function limpiarDomicilio(){
           document.forms[0].txtIdDomicilio.value = "";
           /*document.forms[0].codPais.value = "";*/ //Es usado en BusquedaTercerosAction, tiene valor por defecto, no borrar
           document.forms[0].descPais.value = "";
           document.forms[0].codProvincia.value = ""; //Es usado en VialesAction
           document.forms[0].descProvincia.value	= "";
           document.forms[0].codMunicipio.value = ""; // Es usado en VialesAction
           document.forms[0].descMunicipio.value	= "";
           document.forms[0].idVia.value =	"";
           document.forms[0].descVia.value = "";
           document.forms[0].codTVia.value	= "";
           document.forms[0].descTVia.value = "";
           document.forms[0].txtCodVia.value = "";
           document.forms[0].txtDomicilio.value = "";
           document.forms[0].descPostal.value = "";
           document.forms[0].txtNumDesde.value =	"";
           document.forms[0].txtLetraDesde.value	= "";
           document.forms[0].txtNumHasta.value =	"";
           document.forms[0].txtLetraHasta.value	= "";
           document.forms[0].txtBloque.value = "";
           document.forms[0].txtPortal.value = "";
           document.forms[0].txtEsc.value = "";
           document.forms[0].txtPlta.value	= "";
           document.forms[0].txtPta.value = "";
           document.forms[0].txtBarriada.value =	"";
           document.forms[0].txtKm.value =	"";
           document.forms[0].txtHm.value =	"";
       }

       function limpiarTablaDomicilio(){
           tabD.lineas =	new Array();
           refreshD();
       }

       function limpiarInteresado(){
           document.forms[0].codTerc.value = "";
           document.forms[0].txtIdTercero.value = "";
           document.forms[0].situacion.value = "";
           document.forms[0].txtVersion.value = "";
           document.forms[0].codTipoDoc.value = "";
           document.forms[0].descTipoDoc.value =	"";
           document.forms[0].txtDNI.value = "";
           document.forms[0].txtInteresado.value	= "";
           document.forms[0].txtApell1.value = "";
           document.forms[0].txtPart.value	= "";
           document.forms[0].txtApell2.value = "";
           document.forms[0].txtPart2.value = "";
           document.forms[0].txtTelefono.value =	"";
           document.forms[0].txtCorreo.value = "";
       }

       function limpiarTodo(){
           limpiarInteresado();
           limpiarDomicilio();
           Terceros = new Array();
           lista =	new Array();
           listaDoms = new Array();
           lCasas = new Array();
           tab.lineas = new Array();
           tabD.lineas =	new Array();
           refresco();
           document.forms[0].pagina.value="1";
           document.forms[0].lineasPagina.value="10";
           domlay('enlace',0,0,0,null);
           limpiarECOESIVIA();
           comboProvincia.selectItem(-1);
           comboMunicipio.selectItem(-1);
           /* anadir ECOESI */
           comboECO.selectItem(-1);
           comboESI.selectItem(-1);
           document.forms[0].codVia.value="";
           document.forms[0].descVia.value="";
           codESIs = codESIsOld;
           descESIs = descESIsOld;
           listaECOESIs = listaECOESIsOld;
           comboESI.addItems(codESIs,descESIs);

           /* fin anadir ECOESI */

       }

       function limpiarTodo2() {
           limpiarInteresado();
           limpiarDomicilio();
           Terceros = new Array();
           lista =	new Array();
           listaDoms = new Array();
           lCasas = new Array();
           tab.lineas = new Array();
           tabD.lineas =	new Array();
           refresco();
           document.forms[0].pagina.value="1";
           document.forms[0].lineasPagina.value="10";
           domlay('enlace',0,0,0,null);
       }

       function valoresPorDefecto(){
           document.forms[0].codPais.value	="<%=ptVO.getPais()%>";
           document.forms[0].descPais.value = "<%=ptVO.getNomPais()%>";
           document.forms[0].codProvincia.value = "<%=ptVO.getProvincia()%>";
           document.forms[0].descProvincia.value	= "<%=ptVO.getNomProvincia()%>";
           document.forms[0].codMunicipio.value ="<%=ptVO.getMunicipio()%>";
           document.forms[0].descMunicipio.value	= "<%=ptVO.getNomMunicipio()%>";
       }

       document.onmouseup = checkKeys;

       var pagin;

 function checkKeysLocal(evento,tecla){
     var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else
        teclaAuxiliar = evento.which;

                      keyDel(evento);
                      if(teclaAuxiliar == 40){
                          if(tabD==tableObject){
                              upDownTable(tabD,listaDoms,teclaAuxiliar);
                          }else{
                          upDownTable(tab,lista,teclaAuxiliar);
                      }
                  }
                  if(teclaAuxiliar == 38){
                      if(tabD==tableObject){
                          upDownTable(tabD,listaDoms,teclaAuxiliar);
                      }else{
                      upDownTable(tab,lista,teclaAuxiliar);
                  }
              }
              if(teclaAuxiliar == 13){

                  if(tabD==tableObject){
                      if((tabD.selectedIndex>-1)&&(tabD.selectedIndex < listaDoms.length))
                          pushEnterTable(tabD,listaDoms);
                  }else{
                  if((tab.selectedIndex>-1)&&(tab.selectedIndex < lista.length))
                      pushEnterTable(tab,lista);
              }
          }
          if(tecla=="Alt+B"){
              pulsarBuscar();
          }else if(tecla=="Alt+L"){
          limpiarTodo();
      }
  }



   function onBlurPais(cod, des){
       if(camposDistintosDiv(cod, des)){
           borrarProvMun();
       }
   }

   function onBlurProvincia(cod, des){
       if(camposDistintosDiv(cod, des)){
           borrarMun();
       }
   }

   function onBlurMunicipio(cod, des){
       if(camposDistintosDiv(cod, des)){
           limpiaVial();
           limpiaCodPostal();
       }
   }


    function pulsarAlta() {
        var source = '<%=request.getContextPath()%>/BusquedaTerceros.do?opcion=inicializarDesdeTercero&destino=alta';
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,'terceros',
                'width=998,height=530,status='+ '<%=statusBar%>',function(datos){
                        // Comprobacion de que no se ha dejado un tercero sin domicilio
                        if (datos!=null && datos[0]!=null && datos[0][0] == "terceroSinDomicilio"){
                                terceroSinDomicilio(datos[0][1],"<%=descriptor.getDescripcion("msjNoGrabDomTercExt")%>",
                                        '<%=request.getContextPath()%>',function (datos2){
                                            if(datos2!=undefined){
                                                limpiarTodo();
                                                if(datos2[0] != null && datos2[0].length != 0) {
                                                    if(datos2[0][0] != null && datos2[0][0] != "" && datos2[0][0] != "eliminarTercero") {
                                                        document.forms[0].codTerc.value = datos2[0][0];
                                                        document.forms[0].txtVersion.value = datos2[0][1];
                                                        buscarPorId();
                                                    }
                                                }
                                            }
                                    });
                        } else
                            if(datos!=undefined){
                                limpiarTodo();
                                if(datos[0] != null && datos[0].length != 0) {
                                    if(datos[0][0] != null && datos[0][0] != "" && datos[0][0] != "eliminarTercero") {
                                        document.forms[0].codTerc.value = datos[0][0];
                                        document.forms[0].txtVersion.value = datos[0][1];
                                        buscarPorId();
                                    }
                                }
                            }
                 });
    }

    function pulsarAltaDesdeBuscar() {
        var tipoDoc = document.forms[0].codTipoDoc.value;
        var descTipo = document.forms[0].descTipoDoc.value;
        var docu = document.forms[0].txtDNI.value;
        var nombre = document.forms[0].txtInteresado.value;
        var txtApell1 = document.forms[0].txtApell1.value;
        var txtApell2 = document.forms[0].txtApell2.value;
        // Se ha incluido el parametro origen para saber desde que ventana se abre mantTerceros.jsp, se usara
        // en ese jsp en la función devolverValores()
        var source = '<%=request.getContextPath()%>/BusquedaTerceros.do?opcion=inicializar&destino=altaDesdeBuscar&tipo='
                +tipoDoc+'&descDoc='+descTipo+'&docu='+docu+'&nombre='+nombre+'&apell1='+txtApell1
                +'&apell2='+txtApell2+'&origen=VentanaSeleccion';
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,'terceros',
	'width=998,height=530,status='+ '<%=statusBar%>',function(datos){
                            // Comprobacion de que no se ha dejado un tercero sin domicilio
                            if (datos!=null && datos[0]!=null && datos[0][0] == "terceroSinDomicilio")
                                terceroSinDomicilio(datos[0][1],"<%=descriptor.getDescripcion("msjNoGrabDomTercExt")%>",
                                        '<%=request.getContextPath()%>',function (datos2){
                                                if (datos2!=undefined) {
                                                    Terceros=datos2;
                                                    limpiarTodo();
                                                    if(datos2[0] != null && datos2[0].length != 0 && datos2[0][0] != "eliminarTercero") {
                                                        if(datos2[0][0] != null && datos2[0][0] != "") {
                                                            document.forms[0].codTerc.value = datos2[0][0];
                                                            document.forms[0].txtVersion.value = datos2[0][1];
                                                            buscarPorId();
                                                        }
                                                    }
                                                }
                                        });
                            else
                                if (datos!=undefined) {
                                    Terceros=datos;
                                    limpiarTodo();
                                    if(datos[0] != null && datos[0].length != 0 && datos[0][0] != "eliminarTercero") {
                                        if(datos[0][0] != null && datos[0][0] != "") {
                                            document.forms[0].codTerc.value = datos[0][0];
                                            document.forms[0].txtVersion.value = datos[0][1];
                                            buscarPorId();
                                        }
                                    }
                                }
                    });
    }

    function pulsarModificarTercero(indice) {
        var source = '<%=request.getContextPath()%>/BusquedaTerceros.do?opcion=inicializarDesdeTercero&destino=modificarTercero';
        var array = new Array();
        if ((Terceros.length > 0) && (indice >= 0)) {
            array = [Terceros[indice]];
        }
        document.forms[0].posElemento.value = (eval(paginaActual - 1) * eval(enlacesPagina)) + eval(indice);
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source=' + source + 
                    "&posElemento=" + document.forms[0].posElemento.value, array,
                    'width=998,height=530,status='+ '<%=statusBar%>',function(datos){
                        // Comprobacion de que no se ha dejado un tercero sin domicilio
                         if (datos!=null && datos[0]!=null && datos[0][0] == "terceroSinDomicilio")
                                terceroSinDomicilio(datos[0][1],"<%=descriptor.getDescripcion("msjNoGrabDomTercExt")%>",
                                        '<%=request.getContextPath()%>',function (datos2){
                                                if (datos2 != undefined){
                                                    if (datos2[0][0] == "eliminarTercero"){
                                                        document.forms[0].opcion.value = "eliminarTercero";
                                                        document.forms[0].target = "oculto";
                                                        document.forms[0].action = "<%=request.getContextPath()%>/BusquedaTerceros.do";
                                                        document.forms[0].submit();
                                                    }else{
                                                            Terceros[indice] = datos2[0];
                                                            limpiarInteresado();
                                                            limpiarDomicilio();
                                                            limpiarTablaDomicilio();
                                                            recuperaBusquedaTerceros(Terceros);
                                                    }
                                                }
                                        });
                          else
                            if (datos != undefined){
                                if (datos[0][0] == "eliminarTercero"){
                                    document.forms[0].opcion.value = "eliminarTercero";
                                    document.forms[0].target = "oculto";
                                    document.forms[0].action = "<%=request.getContextPath()%>/BusquedaTerceros.do";
                                    document.forms[0].submit();
                                }else{
                                        Terceros[indice] = datos[0];
                                        limpiarInteresado();
                                        limpiarDomicilio();
                                        limpiarTablaDomicilio();
                                        recuperaBusquedaTerceros(Terceros);
                                }
                            }
                  });
    }

          function grabarTerceroExterno(posTerc,posDom) {

              if (document.forms[0].codTVia.value==null || document.forms[0].codTVia.value=='') {
                  document.forms[0].codTVia.value = Terceros[posTerc][18][posDom][19];
              }

              document.forms[0].txtBloque.value = Terceros[posTerc][18][posDom][13];
              document.forms[0].txtPortal.value = Terceros[posTerc][18][posDom][14];
              document.forms[0].txtEsc.value = Terceros[posTerc][18][posDom][15];
              document.forms[0].txtPlta.value = Terceros[posTerc][18][posDom][16];
              document.forms[0].txtPta.value = Terceros[posTerc][18][posDom][17];
              document.forms[0].txtNormalizado.value = Terceros[posTerc][18][posDom][24];

              document.forms[0].opcion.value = "grabarTercDomExterno";
              document.forms[0].target = "oculto";
              document.forms[0].action = '<%=request.getContextPath()%>/BusquedaTerceros.do?posTerc='+posTerc+'&posDom='+posDom;
              document.forms[0].submit();
          }

          function grabado(id) {
              if (id!=0) document.forms[0].txtIdTercero.value = id;
              if (id!=0) document.forms[0].codTerc.value = id;
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

              var indTercero = tab.selectedIndex;
              var indDomicilio = tabD.selectedIndex;

              grabarTerceroExterno(indTercero, indDomicilio);
          }


          function actualizarBotonesDom() {}
          var cargandoTerceroExt = true;

          function SoloDigitosConsulta(e) {
              PasaAMayusculas(e);

              var tecla, caracter;
              var numeros= "0123456789&|():><!=";

              tecla = e.keyCode;

              if (tecla == null) return true;

              caracter = String.fromCharCode(tecla);

              if (numeros.indexOf(caracter) != -1) {
                  return true;
              } else {
              return false;
          }
      }


      function limpiarVia(){
          var descVia =	document.forms[0].descVia.value;
          if (descVia==""){
              document.forms[0].txtDomicilio.value = "";
              document.forms[0].txtCodVia.value =	"";
              document.forms[0].codVia.value = "";
              document.forms[0].idVia.value = "";
              document.forms[0].codTVia.value =	"";
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

  function cargarListaViasBuscadas(total, idVia, codVia, descVia, descTipoVia, codECO, codESI, codTipoVia, codProvincia, descProvincia, codMunicipio, descMunicipio){
      if (total==0){
          document.forms[0].codProvincia.value = "";
          document.forms[0].descProvincia.value = "";
          document.forms[0].codMunicipio.value = "";
          document.forms[0].descMunicipio.value = "";
          document.forms[0].txtCodVia.value = "";
          document.forms[0].codVia.value = "";
          document.forms[0].descVia.value = "";
          document.forms[0].txtDomicilio.value = "";
          document.forms[0].idVia.value = "";
          document.forms[0].codTVia.value = "";
          jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoDatos")%>');
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
          document.forms[0].txtDomicilio.value = descVia;
          document.forms[0].idVia.value = idVia;
          document.forms[0].codTVia.value = codTipoVia;
      } else {
      var ver = "0,";
      var tamanoVentana=740;
      if (document.forms[0].codProvincia.value!=null && document.forms[0].codProvincia.value!="") {
          ver = "1,";
          tamanoVentana = tamanoVentana - 120;
      }
      if (document.forms[0].codMunicipio.value!=null && document.forms[0].codMunicipio.value!="") {
          ver = ver + "1";
          tamanoVentana = tamanoVentana - 120;
      } else ver = ver + "0";
          var source = "<%=request.getContextPath()%>/jsp/terceros/listaViasBuscadas.jsp?opcion="+ ver;
           abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source="+source,'',
	'width='+tamanoVentana+',height=400,status='+ '<%=statusBar%>',function(datosConsulta){
                            if(datosConsulta!=undefined && datosConsulta[0] == "altaViaDirecta"){
                                iniciarAltaViaDirecta();
                            } else if(datosConsulta!=undefined){

                            if (datosConsulta[4] == 'null' || datosConsulta[4] == undefined) datosConsulta[4] = '';
                            document.forms[0].codECO.value = datosConsulta[4];
                            if ( datosConsulta[4] != '') comboESI.buscaCodigo(datosConsulta[5]);
                            document.forms[0].txtCodVia.value = datosConsulta[1];
                            document.forms[0].codVia.value = datosConsulta[1];
                            document.forms[0].descVia.value = datosConsulta[2];
                            document.forms[0].txtDomicilio.value = datosConsulta[2];
                            document.forms[0].idVia.value = datosConsulta[0];
                            document.forms[0].codTVia.value = datosConsulta[5];
                            document.forms[0].codProvincia.value = datosConsulta[7];
                            document.forms[0].descProvincia.value = datosConsulta[8];

                            cargarListaMunicipios();
                            document.forms[0].codMunicipio.value = datosConsulta[9];
                            document.forms[0].descMunicipio.value = datosConsulta[10];

                        }
                });
  }
}
function iniciarAltaViaDirecta(){
  pleaseWait1("on",top.mainFrame);
  document.forms[0].opcion.value="iniciarAltaDirecta";
  document.forms[0].target="oculto";
  document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Viales.do";
  document.forms[0].submit();
}
function altaViaDirectaIniciada(codTipoVia,descTipoVia) {
  var	argumentos = new Array();
  argumentos = [document.forms[0].codProvincia.value,document.forms[0].codMunicipio.value,
      codTipoVia, descTipoVia];
  var source = "<%=request.getContextPath()%>/jsp/terceros/altaViaDirecta.jsp?opcion=altaViaDirecta";
  pleaseWait1("off",top.mainFrame); // Viene del buscar via
  abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source="+source, argumentos,
	'width=500,height=310,status='+ '<%=statusBar%>',function(datosConsulta){
                        if(datosConsulta!=undefined){
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

function pulsarBuscarVia(opcion) {

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

    document.forms[0].opcion.value=opcion;
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Viales.do";
    document.forms[0].submit();
}

function incorporarDomicilio(codigoTercero) {
    grabado(codigoTercero);
    document.forms[0].opcion.value="grabarDomicilioExt";
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
    document.forms[0].submit();
}

function domicilioGrabadoExt(idDomicilio, domActual) {
    buscarPorId(document.forms[0].txtIdTercero.value);
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


function validarDocumento(){
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
}
        </script>
    </head>

    <body class="bandaBody"
          <% if (inicioTercero.equals("si")) {%>
          onLoad="inicio();">
        <% } else {%>
        onLoad="recuperaDatosIniciales();">
        <% }%>


        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
<form	name="formulario"	METHOD=POST	target="_self">
    <input  type="hidden" name="opcion">
    <input type="hidden" name="codTerc">
    <input type="hidden" name="codDomTerc">
    <input type="hidden" name="numModifTerc">
    <input type="hidden" name="txtIdTercero">
    <input type="hidden" name="situacion">
    <input type="hidden" name="txtVersion">
    <input type="hidden" name="txtIdDomicilio">
    <input type="hidden" name="txtNormalizado">
    <input type="hidden" name="ventana"	value="false">
    <input type="hidden" name="perFJ" value=1>
    <input TYPE="hidden" name="txtPart"	class="inputTexto" SIZE=5 MAXLENGTH=5 onKeyPress="javascript:PasaAMayusculas(event);">
    <input TYPE="hidden" name="txtPart2" class="inputTexto" SIZE=5 MAXLENGTH=5 onKeyPress="javascript:PasaAMayusculas(event);">
    <input type="hidden" name="txtTelefono">
    <input type="hidden" name="txtCorreo">
    <input type="hidden" name="txtPoblacion">
    <input type="hidden" name="txtBarriada">
    <input type="hidden" name="descPostal">
    <input type="hidden" name="idVia">
    <input type="hidden" name="codTVia">
    <input type="hidden" name="descTVia">
    <input type="hidden" name="txtBloque">
    <input type="hidden" name="txtPortal">
    <input type="hidden" name="txtEsc">
    <input type="hidden" name="txtPlta">
    <input type="hidden" name="txtPta">
    <input type="hidden" name="txtKm">
    <input type="hidden" name="txtHm">
    <input type="hidden" name="codPais"	size="3" value="<%=ptVO.getPais()%>">
    <input type="hidden" name="descPais" size="3">
    <input type="hidden" name="lineasPagina" value="10">
    <input type="hidden" name="pagina" value="1">
    <!-- anadir ECO/ESI -->
    <input type="hidden" name="txtDomicilio">
    <input type="hidden" name="txtCodVia">
    <input type="hidden" name="posElemento">

    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("titBusqTerDom")%></div>
    <div class="contenidoPantalla">
        <table width="100%">
            <tr>
                <td class="sub3titulo">
                    <%= descriptor.getDescripcion("manTer_TitDatos")%>
                </td>
            </tr>
            <tr>
                <td style="width: 100%" align="center">
                    <table width="100%" cellspacing="0px" cellpadding="0px" border="0">
                        <tr>
                            <td style="width: 16%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqTiDoc")%>:</td>
                            <td style="width: 33%" class="columnP">
                                <input id="codTipoDoc" name="codTipoDoc" type="text" class="inputTexto" size="3"
                                       onkeypress="javascript:return SoloDigitosConsulta(event);">
                                <input id="descTipoDoc"	name="descTipoDoc" type="text" class="inputTexto" style="width:169" readonly>
                                <a id="anchorTipoDoc" name="anchorTipoDoc" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoDoc" name="botonTipoDoc" style="cursor:hand;"	alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>" border="0" style="cursor:hand;"></span></a>
                            </td>
                            <td style="width: 10%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqDocumento")%>:</td>
                            <td class="columnP">
                                <input type="text"  class="inputTexto" size="35" maxlength=16 name="txtDNI" onKeyPress="javascript:PasaAMayusculas(event);"
                                       onchange="javascript:validarDocumento();">
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td style="width: 100%" align="center">
                    <table width="100%" cellspacing="0px" cellpadding="0px" border="0px">
                        <tr>
                            <td style="width: 16%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqNombreRazon")%>:</td>
                            <td>
                                <input TYPE="text" class="inputTexto" style="width:626;" MAXLENGTH=80  NAME="txtInteresado" onKeyPress="javascript:PasaAMayusculas(event);">
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td style="width: 100%" align="center">
                    <table width="100%" cellspacing="0px" cellpadding="0px" border="0px">
                        <tr>
                            <td style="width: 16%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqApellido1Part")%>:</td>
                            <td style="width: 33%" class="columnP">
                                <input TYPE="text" name="txtApell1" class="inputTexto" SIZE=35	MAXLENGTH=25 onKeyPress="javascript:PasaAMayusculas(event);">
                            </td>
                            <td style="width: 10%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqApellido2Part")%>:</td>
                            <td class="columnP">
                                <input name="txtApell2" TYPE="text" class="inputTexto" SIZE=35	MAXLENGTH=25 onKeyPress="javascript:PasaAMayusculas(event);">
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <table width="100%">
            <tr>
                <TD class="sub3titulo"><%=descriptor.getDescripcion("manTer_TitDomis")%></td>
            </tr>
            <tr>
                <td style="width: 100%" align="center" >
                    <table border="0"	width="100%" cellspacing="0px" cellpadding="0px">
                        <tr>
                            <td style="width: 12%"	class="etiqueta"><%=descriptor.getDescripcion("gEtiqProvincia")%>:</td>
                            <td style="width: 38%" class="columnP">
                                <input class="inputTexto" type="text"	id="codProvincia" name="codProvincia" size="3" onkeypress="javascript:return SoloDigitos(event);">
                                <input class="inputTexto" type="text"	id="descProvincia" name="descProvincia" style="width:210" readonly>
                                <a id="anchorProvincia" name="anchorProvincia" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonProvincia" name="botonProvincia" style="cursor:hand;" alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>" border="0" style="cursor:hand;"></span></a>
                            </td>
                            <td style="width: 12%"	class="etiqueta"><%=descriptor.getDescripcion("gEtiqMunicipio")%>:</td>
                            <td class="columnP">
                                <input class="inputTexto"	type="text"	id="codMunicipio"	name="codMunicipio"
                                       size="3" onkeypress="javascript:return SoloDigitos(event);">
                                <input id="descMunicipio"	name="descMunicipio" type="text" class="inputTexto"	style="width:210"	readonly>
                                <a id="anchorMunicipio" name="anchorMunicipio" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonMunicipio" name="botonMunicipio" style="cursor:hand;" alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>" border="0" style="cursor:hand;"></span></a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <!-- anadir ECO/ESI -->
            <tr>
                <td>
                    <table border="0px"	width="100%" cellspacing="0px" cellpadding="0px">
                        <tr>
                            <td style="visibility:<%=ecv%>; width: 12%"	class="etiqueta"><%=descriptor.getDescripcion("gEtiqParroquia")%>:</td>
                            <td style="visibility:<%=ecv%>; width: 38%"	class="columnP" >
                                <input type="text" id="codECO" name="codECO" class="inputTexto" size="3">
                                <input type="text" id="descECO" name="descECO" class="inputTexto" style="width:210" readonly="true">
                                <a href="" id="anchorECO" name="anchorECO"><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonECO" name="botonECO" style="cursor:hand;" alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>" border="0" style="cursor:hand;"></span></a>
                            </td>
                            <td style="width: 12%; visibility:<%=esv%>" class="etiqueta"><%=descriptor.getDescripcion("gEtiqLugar")%>:</td>
                            <td style="visibility:<%=esv%>"	class="etiqueta">
                                <input type="text" id="codESI" name="codESI" class="inputTexto" size="3">
                                <input type="text" id="descESI" name="descESI" class="inputTexto" style="width:210" readonly="true">
                                <a href="" id="anchorESI" name="anchorESI"><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonESI" name="botonESI" style="cursor:hand;" alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>" border="0" style="cursor:hand;"></span></a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <!-- fin anadir ECO/ESI -->
            <tr>
                <td>
                    <table border="0px"	width="100%" cellspacing="0px" cellpadding="0px">
                        <tr>
                            <td	style="width: 12%"	class="etiqueta"><%=descriptor.getDescripcion("etiqVia")%>:</td>
                            <td	style="width: 38%"	class="columnP" >
                            <input type="hidden" name="codVia">
                            <input type="text" name="descVia" class="inputTexto" style="width:220px" maxlength=50 onkeypress="PasaAMayusculas(event);" onchange="limpiarVia();">
                            <span class="fa fa-search" aria-hidden="true"  name="botonT" alt="Buscar Vía" style="cursor:hand;" onclick="pulsarBuscarVia('buscarVias');"></span>
                            <td	style="width: 5%"	class="etiqueta"><%=descriptor.getDescripcion("manTer_EtiqNLD")%>:</td>
                            <td	style="width: 13%"	class="columnP">
                                <input name="txtNumDesde"	TYPE="text"	class="inputTexto" id="txtNumDesde" SIZE=2 MAXLENGTH=3 onKeyPress = "return SoloDigitos(event);">
                                <input name="txtLetraDesde" TYPE="text" class="inputTexto" id="txtLetraDesde" SIZE=2 MAXLENGTH=1 onKeyPress="PasaAMayusculas(event);">
                            </td>
                            <td	style="width: 10%" class="etiqueta"><%=descriptor.getDescripcion("manTer_EtiqNLH")%>:</td>
                            <td class="columnP">
                                <input name="txtNumHasta"	TYPE="text"	class="inputTexto" id="txtNumHasta" SIZE=2 MAXLENGTH=3 onKeyPress = "return SoloDigitos(event);">
                                <input name="txtLetraHasta" TYPE="text" class="inputTexto" id="txtLetraHasta" SIZE=2 MAXLENGTH=1 onKeyPress="PasaAMayusculas(event);">
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <table width="100%">
            <tr>
                <td id="tabla" style="overflow-x:no; overflow-y:auto"></td>
            </tr>
            <tr>
                <td>
                    <div id="Domicilios" style="overflow-x:no; overflow-y:auto">
                        <div id="tablaDomicilios" style="overflow-x:no; overflow-y:auto"></div>
                    </div>
                </td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <% if (inicioTercero.equals("si")) {%>
                <input type="button" title='<%=descriptor.getDescripcion("toolTip_bAlta")%>' alt='<%=descriptor.getDescripcion("toolTip_bAlta")%>' class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbAlta")%>" name="cmdAlta" onClick="pulsarAlta();return false;">
            <% }%>
                <input type="button" title='<%=descriptor.getDescripcion("toolTip_bBuscar")%>' alt='<%=descriptor.getDescripcion("toolTip_bBuscar")%>' class="botonGeneral" value="<%=descriptor.getDescripcion("gbBuscar")%>" name="cmdBuscar" onClick="pulsarBuscar();return false;">
                <input type="button" title='<%=descriptor.getDescripcion("toolTip_bLimpiar")%>' alt='<%=descriptor.getDescripcion("toolTip_bLimpiar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbLimpiar")%>' name="cmdLimpiar" onClick="limpiarTodo();return false;">
            <% if (!inicioTercero.equals("si")) {%>
                <input type= "button"  title='<%=descriptor.getDescripcion("toolTip_bSalir")%>' alt='<%=descriptor.getDescripcion("toolTip_bSalir")%>' class="botonGeneral" value="<%=descriptor.getDescripcion("gbSalir")%>" name="cmdSalir" onClick="pulsarSalir();return false;">
            <% }%>
        </div>
    </div>
</form>
<div id="desplegable" style="overflow-x: no; overflow-y: auto;  visibility: hidden; BORDER: 0px"></div>

<script type="text/javascript">

var tab;
<% if (mostrarOrigen.equals("si")) {%>
    if (document.all) tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.all.tabla, 965);
    else tab = new TablaNueva(document.getElementById('tabla'), 965);
    tab.addColumna('98', null, '<%=descriptor.getDescripcion("gEtiq_Documento")%>');
    tab.addColumna('239', null, '<%=descriptor.getDescripcion("gEtiqNombre")%>');
    tab.addColumna('239', null, '<%=descriptor.getDescripcion("gEtiqApellido1Part")%>');
    tab.addColumna('239', null, '<%=descriptor.getDescripcion("gEtiqApellido2Part")%>');
    tab.addColumna('120', 'center', '<%=descriptor.getDescripcion("etiqOrig")%>');
<%} else {%>

    if (document.all) tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.all.tabla, 965);
    else tab = new TablaNueva(document.getElementById('tabla'), 965);
    tab.addColumna('98', null, '<%=descriptor.getDescripcion("gEtiq_Documento")%>');
    tab.addColumna('285', null, '<%=descriptor.getDescripcion("gEtiqNombre")%>');
    tab.addColumna('285', null, '<%=descriptor.getDescripcion("gEtiqApellido1Part")%>');
    tab.addColumna('285', null, '<%=descriptor.getDescripcion("gEtiqApellido2Part")%>');
<%}%>

tab.height = 130;
tab.displayCabecera=true;

function refresco(){
    tab.displayTabla();
    tabD.displayTabla();
}

tab.displayTabla();

var tabD;
<% if (mostrarOrigen.equals("si")) {%>
    if (document.all) tabD = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.all.tablaDomicilios, 965);
    else tabD = new TablaNueva(document.getElementById('tablaDomicilios'), 965);
    tabD.addColumna('120', null, '<%=descriptor.getDescripcion("gEtiqProvincia")%>');
    tabD.addColumna('170', null, '<%=descriptor.getDescripcion("gEtiqMunicipio")%>');
    tabD.addColumna('475', null, '<%=descriptor.getDescripcion("gEtiq_Domicilio")%>');
    tabD.addColumna('100', 'center', '<%=descriptor.getDescripcion("gEtiqCodPostal")%>');
    tabD.addColumna('70', 'center', '<%=descriptor.getDescripcion("etiqOrig")%>');
<%} else {%>
    if (document.all) tabD = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.all.tablaDomicilios, 965);
    else tabD = new TablaNueva(document.getElementById('tablaDomicilios'), 965);
    tabD.addColumna('120', null, '<%=descriptor.getDescripcion("gEtiqProvincia")%>');
    tabD.addColumna('170', null, '<%=descriptor.getDescripcion("gEtiqMunicipio")%>');
    tabD.addColumna('545', null, '<%=descriptor.getDescripcion("gEtiq_Domicilio")%>');
    tabD.addColumna('100', 'center', '<%=descriptor.getDescripcion("gEtiqCodPostal")%>');
<%}%>
tabD.height = 90;
tabD.displayCabecera=true;

function refreshD(){
tabD.displayTabla();
                                                                                    }
                                                                                    tabD.displayTabla();
                                                                                    var	indice;
                                                                                    function pintaDatos(datos){
                                                                                        var i=tab.selectedIndex;
                                                                                        limpiarInteresado();
                                                                                        limpiarDomicilio();
                                                                                        limpiarTablaDomicilio();
                                                                                        if((i>=0)&&(!tab.ultimoTable)){
                                                                                            indice=i;
                                                                                            if(tipo!="SGE"){
                                                                                                cargaDomicilios(i);
                                                                                            }
                                                                                        }
                                                                                    }

                                                                                    tab.displayDatos = pintaDatos;

                                                                                    var	indice1;
                                                                                    function pintaDatos1(datos){
                                                                                        var i=tabD.selectedIndex;
                                                                                        limpiarDomicilio();
                                                                                        if((i>=0)&&(!tabD.ultimoTable)){
                                                                                            indice1=i;
                                                                                            var vectorCampos = new Array();
                                                                                            var vectorDatos =	new Array();
                                                                                            var codVia = (lCasas[i][23]==0)?'':lCasas[i][23];
                                                                                            var numDesde = (lCasas[i][9]==0)?'':lCasas[i][9];
                                                                                            var numHasta = (lCasas[i][11]==0)?'':lCasas[i][11];
                                                                                            vectorCampos=['codTerc','numModifTerc','txtIdTercero','txtVersion','codTipoDoc',
                                                                                                'txtDNI','txtInteresado','txtApell1','txtApell2','txtPart','txtPart2','txtTelefono',
                                                                                                'txtCorreo','codDomTerc','txtIdDomicilio','codPais','codProvincia','codMunicipio',
                                                                                                'descPais','descProvincia','descMunicipio','txtDomicilio','descPostal','txtPoblacion',
                                                                                                'txtBarriada','txtCodVia','txtNumDesde','txtLetraDesde','txtNumHasta','txtLetraHasta','descVia','codVia'];

                                                                                            vectorDatos	=[Terceros[indice][0],Terceros[indice][1],Terceros[indice][0],Terceros[indice][1],
                                                                                                Terceros[indice][2],Terceros[indice][3],Terceros[indice][4],Terceros[indice][5],
                                                                                                Terceros[indice][6],Terceros[indice][7],Terceros[indice][8],Terceros[indice][9],
                                                                                                Terceros[indice][10],lCasas[i][5],lCasas[i][5],lCasas[i][6],lCasas[i][7],lCasas[i][8],
                                                                                                lCasas[i][0],lCasas[i][1],lCasas[i][2],listaDoms[i][2],lCasas[i][4],lCasas[i][18],
                                                                                                lCasas[i][18],codVia,numDesde,lCasas[i][10],numHasta,lCasas[i][12],lCasas[i][29],codVia];
                                                                                            rellenar(vectorDatos,vectorCampos);
                                                                                        }
                                                                                    }

                                                                                    tabD.displayDatos = pintaDatos1;

function callFromTableTo(rowID,tableName){
<% if (!inicioTercero.equals("si")) {%> // Aplicaciones de Registro y Sge
    if(tab.id == tableName){
        if(tipo=="TERCEROS"){
            indice=rowID;
            cerrar();
        } else if(tipo=="SGE"){ // No parece que venga por aqui nunca
            var	i=tab.selectedIndex;
            if(i>=0){
                enviaSeleccion(i);
            }
        }
    }else if(tabD.id ==	tableName){
        if(tipo=="TERCEROS"){
            var	i=tab.selectedIndex;
            if(i>=0){
                indice=i;
                cerrar();
            }
            limpiarInteresado();
        } else if(tipo!="TERCEROS"){ // Doble click en domicilios desde Registro y Sge
            var i = tabD.selectedIndex;
            if (i >= 0) {
                indice1 = i;
                if (Terceros[indice][0] == 0) {
                    grabarTerceroExterno(indice,indice1);
                } else {
                    if (lCasas[indice1][30] == 'SGE') {
                        enviaSeleccionDoms(indice, indice1);
                    } else {
                        jsp_alerta("A", '<%=descriptor.getDescripcion("msjDomExtNoCargar")%>');
                    }
                }
            }
        }
    }
<% } else if (inicioTercero.equals("si")) { // Aplicacion de Terceros y Territorio %>
    if(tab.id == tableName) { // Doble click en tercero desde Terceros y Territorio (se abre ventana de mantenimiento)
        indice = rowID;
        pulsarModificarTercero(indice);
    }
    else if(tabD.id ==	tableName) { // Doble click en domicilio desde Terceros y Territorio (se abre ventana de mantenimiento)
        var	i=tab.selectedIndex;
        indice = i;
        pulsarModificarTercero(indice);
    }
<% }%>
}


var	tableObject=tab;
function rellenarDatos(tableName,listName){
    tableObject =	tableName;
    if(tab == tableName){
        pintaDatos(listName);
    }else if(tabD	== tableName){
    pintaDatos1(listName);
}
}

var	comboProvincia = new Combo("Provincia");
var	comboMunicipio = new Combo("Municipio");
var	comboTipoDoc = new Combo("TipoDoc");

var	auxCombo = 'comboMunicipio';


comboProvincia.change	=
function() {
    auxCombo='comboMunicipio';
    limpiar(['codMunicipio','descMunicipio','txtCodVia','txtDomicilio','codTVia']);
    if(comboProvincia.cod.value.length!=0){
        desHabilitarECOESIVIA(true);
        cargarListaMunicipios();
    } else{
    comboMunicipio.addItems([],[]);
}
}

comboMunicipio.change	=
function() {
    limpiar(['txtCodVia','txtDomicilio', 'codTVia']);
    if(comboMunicipio.cod.value.length!=0){
        if( (comboMunicipio.cod.value=="<%=ptVO.getMunicipio()%>") &&  ( comboProvincia.cod.value=="<%=ptVO.getProvincia()%>"))
            desHabilitarECOESIVIA(false);
            else desHabilitarECOESIVIA(true);
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
        </script>

    </body>
</html>
