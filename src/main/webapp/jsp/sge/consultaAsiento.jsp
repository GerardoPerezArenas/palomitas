<%@page contentType="text/html; charset=iso-8859-1"	language="java" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.escritorio.RegistroUsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm"%>

<%@page import="java.util.Vector" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm" %>
<%@ page import="es.altia.agora.business.registro.RegistroValueObject" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>

<html:html>
    <!-- consultaAsiento.jsp -->
    <head>
        <jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
        <TITLE> CONSULTA DE ASIENTOS</TITLE>
        <jsp:include page="/jsp/plantillas/Metas.jsp" />
        
        <!-- Estilos -->
        <%
            UsuarioValueObject usuarioVO;

            int idioma = 1;
            int apl = 1;
            int cod_org = 1;
            int cod_dep = 1;
            int cod_ent = 1;
            int cod_unidOrg = 1;
            String funcion = "";

            String desc_org = "";
            String desc_ent = "";
            String respOpcion = "";

            String dil = "";


            if ((session.getAttribute("usuario") != null)) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
                apl = usuarioVO.getAppCod();
                cod_org = usuarioVO.getOrgCod();
                cod_ent = usuarioVO.getEntCod();
                desc_org = usuarioVO.getOrg();
                desc_ent = usuarioVO.getEnt();
            }

            boolean hayBuscada = true;

            String modoInicio = "manipularExpediente";
            String munExp = "";
            String proExp = "";
            String ejeExp = "";
            String numExp = "";

            if (session.getAttribute("modoInicio") != null) {
                modoInicio = (String) session.getAttribute("modoInicio");
            }

            if ("soloConsulta".equals(modoInicio)) {
                FichaExpedienteForm expForm = (FichaExpedienteForm) session.getAttribute("FichaExpedienteForm");
                GeneralValueObject gVO = expForm.getExpedienteVO();
                munExp = (String) gVO.getAtributo("codMunicipio");
                proExp = (String) gVO.getAtributo("codProcedimiento");
                ejeExp = (String) gVO.getAtributo("ejercicio");
                numExp = (String) gVO.getAtributo("numero");
            }

            //session.removeAttribute("modoInicio");
            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");

            String JSP_autoridad = m_Config.getString("JSP.Registro.Autoridad");
            String aut = "visible";
            if ("no".equals(JSP_autoridad)) {
                aut = "hidden";
            }
           
            String tipoAnotacion = "E";
            session.removeValue("tipoAnotacion");

            int estadoAnotacionA=1;            
            String diligenciaAnotacion="";
            if ((session.getAttribute("MantAnotacionRegistroForm") != null)) {
               MantAnotacionRegistroForm form= (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm"); 
               if(form.getRegistro()!=null){
                   RegistroValueObject laAnotacion=form.getRegistro();
                   estadoAnotacionA=laAnotacion.getEstAnotacion();  
                   diligenciaAnotacion=laAnotacion.getDilAnulacion();  
                   tipoAnotacion=laAnotacion.getTipoReg();
               }
            }

            String titPag;
            String tipo;
            String fech;
            String hora;
            String numOrden;
            String destino;
            String origen;

            if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion))) {
                titPag = "tit_AnotE";
                tipo = "res_tipoEntrada";
                fech = "res_fecE";
                hora = "res_HoraEnt";
                numOrden = "res_numOrdE";
                destino = "res_etiqDestino";
                origen = "res_etiqOrigen";
            } else {
                titPag = "tit_AnotS";
                tipo = "res_tipoSalida";
                fech = "res_fecS";
                hora = "res_HoraSal";
                numOrden = "res_numOrdS";
                destino = "res_etiqOrigen";
                origen = "res_etiqDestino";
            }

            // #291976
            boolean mostrarCatalogar = false;
            String servicioDigitalizacionActivo = (String) session.getAttribute("servicioDigitalizacionActivo");
            if((tipoAnotacion.equals("E") || tipoAnotacion.equals("Relacion_E"))
                && servicioDigitalizacionActivo != null && servicioDigitalizacionActivo.equalsIgnoreCase("si")){
                    mostrarCatalogar = true;
            }
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />        
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
        <script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/digitalizacion.js'/>?<%=System.currentTimeMillis()%>"></script>
        <SCRIPT type="text/javascript">     
            //Combos
            var comboExpedientesRelacionados;
            var dilAnotacion="<%=diligenciaAnotacion%>";
           
            // Listas de valores.
            var cod_tiposDocumentos = new Array();
            var desc_tiposDocumentos = new Array();
            var cod_tiposTransportes = new Array();
            var desc_tiposTransportes = new Array();
            var cod_tiposRemitentes= new Array();
            var desc_tiposRemitentes= new Array();
            var cod_procedimientos= new Array();
            var desc_procedimientos= new Array();
            var desc_temas = new Array();
            var cod_temas = new Array();
            var cod_tipoEntrada=new Array();
            var desc_tipoEntrada=new Array();
            var cod_tiposIdInteresado = new Array();
            var desc_tiposIdInteresado = new Array();
            var cod_departamentos= new Array();
            var desc_departamentos= new Array();
            var cod_roles = new Array();
            var desc_roles = new Array();
            var defecto_roles = new Array();
            // #291976
            var mostrarCatalogar = <%=mostrarCatalogar%>;
            var tipoActual;
            <% if ("S".equals(tipoAnotacion) || "Relacion_S".equals(tipoAnotacion)) {%>
                tipoActual = 'S';
            <% } else {%>
                tipoActual = 'E';
            <% }%>
            
            var numsExpedientesRelacionados = new Array();
            var contIdNumsExpedientesRelacionados = new Array();
            
            <% if ("S".equals(tipoAnotacion) || "Relacion_S".equals(tipoAnotacion)) {%>
                cod_tipoEntrada = [0,1];
                desc_tipoEntrada = ['<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("entradaOrd"))%>'
                    ,'<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("salidaSIR"))%>'];
            <% } else {%>
                cod_tipoEntrada = [0,1,2];
                desc_tipoEntrada = ['<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("entradaOrd"))%>',
                        '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("DestOtroReg"))%>',
                        '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("procOtroReg"))%>'];
            <% }%>
                
                var terceros = new Array();		//  Campos de la tabla Terceros
                var terceroActual = -1; // Indice en el vector 'terceros' del tercero mostrado
                var terceroCargado = '';
                var relaciones = new Array();  // Lista de asientos relacionados
                
                var tabDocs;
                var activado = false;
                
                var datosConsultaGlobal = new Array();
                var listaDocs = new Array();
                var lista= new Array();			// Funciones rejilla
                var tipoDoc;					// Variable tipo de documento
                var dTipoDoc;
                var modoConsulta = false;
                
                <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTiposIdInteresado">
                cod_tiposIdInteresado['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
                desc_tiposIdInteresado['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
                </logic:iterate>
                
                // ------ Funciones de página
                function inicializar() {
                    <c:if test="${not empty requestScope.errorCambiaEstadoAsiento}">
                        jsp_alerta('A', '<c:out value="${requestScope.errorCambiaEstadoAsiento}"/>');
                    </c:if>
                        window.focus();
                        document.forms[0].modoConsulta.value="<%=request.getParameter("modoConsulta")%>";
                        document.forms[0].desdeConsulta.value="<%=request.getParameter("desdeConsulta")%>";
                        document.forms[0].desdeExpRel.value="<%=request.getParameter("desdeExpRel")%>";
                        document.forms[0].expRelacionado.value="<%=request.getParameter("expRelacionado")%>";
                        document.forms[0].codMunicipioIni.value="<%=request.getParameter("codMunExpIni")%>";
                        document.forms[0].ejercicioIni.value="<%=request.getParameter("ejercicioExpIni")%>";
                        document.forms[0].numeroIni.value="<%=request.getParameter("numeroExpIni")%>";
                        document.forms[0].porCampoSup.value="<%=request.getParameter("porCampoSup")%>";
                        document.forms[0].deHistorico.value = '<bean:write name="MantAnotacionRegistroForm" property="deHistorico"/>';   
                        document.forms[0].observaciones.value = unescape('<bean:write name='MantAnotacionRegistroForm' property='observaciones'/>');    
                        mostrarDestino();
                        actualizarDescripcion('cbTipoEntrada','txtNomeTipoEntrada',cod_tipoEntrada,desc_tipoEntrada);
                        cambiaBotonera();
                        inicializarRecarga();
                        tp1.setSelectedIndex(0);
                        cargaTablaDocs();
                        //document.forms[0].cmdVisualizar.style.cursor="hand";
                        //document.forms[0].cmdVisualizar.disabled=false;
                        // Antes se habilitaba el boton cmdVisualizar manualmente
                        // #291976: se añade el boton cmdCatalagor, ahora para habilitar se usa habilitarGeneral(vector), vector es un array con los botones
                        var vector = [document.forms[0].cmdVisualizar];
                        <% if (mostrarCatalogar) {%>
                            if(listaDocs.length > 0 && comprobarContenidoDocs()) {
                                vector.push(document.forms[0].cmdCatalogar);
                            }
                        <% } %>
                        habilitarGeneral(vector);
                        numTerceros = <bean:write name="MantAnotacionRegistroForm" property="numTerceros"/>;
                        //Cargamos los numeros y los procedimientos de los expedientes relacionados.
                        var contNumExpedientes = 0;
                        <logic:iterate id="numExpediente" name="MantAnotacionRegistroForm" property="listaNumExpedientesRelacionados">
                            numsExpedientesRelacionados[contNumExpedientes] = ['<bean:write name="numExpediente"/>'];
                            contIdNumsExpedientesRelacionados[contNumExpedientes] = contNumExpedientes;
                            contNumExpedientes = contNumExpedientes + 1;
                        </logic:iterate>
                        comboExpedientesRelacionados = new Combo("ListaExpedientesRelacionados");
                        comboExpedientesRelacionados.addItems(contIdNumsExpedientesRelacionados, numsExpedientesRelacionados);
                        if(numsExpedientesRelacionados.length){
                            comboExpedientesRelacionados.buscaCodigo(contIdNumsExpedientesRelacionados[contIdNumsExpedientesRelacionados.length - 1]);
                        }//if(numsExpedientesRelacionados.length)
                        //
                        if(contIdNumsExpedientesRelacionados.length>0) comboExpedientesRelacionados.selectItem(1);
                        
                    // Impedimos la navegacion entre anotaciones si estamos en el buzon
                    <% if ("soloConsulta".equals(modoInicio)) {%>
                    modoConsulta = true;
                    <% } else {%>
                    modoConsulta = false; 
                    <% }%>   
                    document.all.cmdRelaciones.disabled = false;
                    compruebaRelaciones();    
                    compruebaTemas();

                    // Carga de interesados
                    var listaInteresados = new Array();
                    <%
                    MantAnotacionRegistroForm mantARForm = (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm");
                    Vector listaInteresados = null;
                    if (mantARForm != null) listaInteresados = (Vector) mantARForm.getListaInteresados();
                    if (listaInteresados != null) {
                        int lengthInteresados = listaInteresados.size();
                        int i = 0;
                        %>
                        var j=0;
                        <% for (i = 0; i < lengthInteresados; i++) { %>
                            listaInteresados[j] = ['<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("codigoTercero")%>',
                                    '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("versionTercero")%>',
                                    '<%=StringEscapeUtils.escapeJavaScript((String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("titular"))%>',
                                    '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("descRol")%>',
                                    '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("domicilio")%>',
                                    '<%=StringEscapeUtils.escapeJavaScript((String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("descDomicilio"))%>',
                                    '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("porDefecto")%>',
                                    '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("rol")%>',
                                    '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("telefono")%>',
                                    '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("email")%>',
                                    '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("pais")%>',
                                    '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("provincia")%>',
                                    '<%=StringEscapeUtils.escapeJavaScript((String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("municipio"))%>',
                                    '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("cp")%>',
                                    '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("tip")%>',
                                    '<%=(String) ((GeneralValueObject) listaInteresados.get(i)).getAtributo("doc")%>',];
                            j++;
                    <%    }
                    } %>
                    terceros=listaInteresados;
                    document.forms[0].codTerc.value= '<bean:write name="MantAnotacionRegistroForm" property="codTerc"/>';
                    terceroCargado = document.forms[0].codTerc.value;

                    if(terceros.length > 0)document.forms[0].hayListaTerc.value="si";//Para saber si la lista de terceros tiene terceros o no
                    // Si hay mas de un interesado activar navegacion
                    if (terceros.length > 1) mostrarBotonesNavegacionInteresados();
                    else ocultarBotonesNavegacionInteresados();

                    // Carga de roles
                    var m = 0;
                    <%
                    Vector listaRoles = null;
                    if (mantARForm != null) listaRoles = mantARForm.getListaRoles();
                    if (listaRoles != null) {
                        for(int t=0;t<listaRoles.size();t++) {
                        %>
                            cod_roles[m] = [ '<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("codRol")%>'];
                            desc_roles[m] = [ '<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("descRol")%>'];
                            defecto_roles[m] = [ '<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("porDefecto")%>'];
                            m++;
                     <% }
                    } %>
                    comboRol.addItems(cod_roles, desc_roles);
                    comboRol.selectedIndex=-1;
                    if (terceros.length > 0) mostrarDatosTercero();
                    
                    // fin digitalizacion 
                    var finDigitalizacion= <bean:write name="MantAnotacionRegistroForm" property="finDigitalizacion"/>;
                    var procedimientoDigitalizacion =<bean:write name="MantAnotacionRegistroForm" property="procedimientoDigitalizacion"/>;
                    
                    // #291976: Se comprueba si todos los documentos han sido catalogados antes de aceptar la anotación y que la digitalización haya finalizado
                    <% if (!"soloConsulta".equals(modoInicio)) {%>
                        var esRegTel = false;
                        <c:if test="${sessionScope.MantAnotacionRegistroForm.registro.registroTelematico eq true}">
                            esRegTel = true;
                        </c:if>
                        $('#capaBotones1 input').each(function(i,el) {
                               $(el).click(function(){
                                   var boton = $(el).prop('name');
                                   
                                   if(!esRegTel) {
                                        if(boton !== 'cmdCancelar' && comprobarFinDigitalizacion(procedimientoDigitalizacion, finDigitalizacion)){

                                             if(boton !== 'cmdRechazar' && comprobarDocCatalogados()){
                                                 ejecutarAccion(boton);
                                            } else if(boton === "cmdRechazar"){
                                                 ejecutarAccion("cmdRechazar");
                                            }
                                        }
                                    } else {
                                        ejecutarAccion(boton);
                                    }
                               });
                            });
                
                <% } %>
                }
                
                function ejecutarAccion(boton){
                    if(boton === 'cmdAceptar') {
                        pulsarAceptar();
                    } else if(boton === 'cmdAdjuntarExp') {
                        pulsarAdjuntarExp();
                    } else if(boton === 'cmdAdjuntarExpInt') {
                        pulsarAdjuntarExp('interesado');
                    } else if(boton === 'cmdIniciarExp') {
                        pulsarIniciarExp();
                    } else if(boton === "cmdRechazar"){
                        pulsarRechazar();                                           
                    }
                }
                
                function findProcedimiento(){
                    if(document.forms[0].descListaExpedientesRelacionados.value != null 
                        || document.forms[0].descListaExpedientesRelacionados.value.length > 0){
                        var numero = document.forms[0].descListaExpedientesRelacionados.value;
                        var expediente = numero.split("/");
                        var ejercicio = expediente[0];
                        var codProcedimiento = expediente[1];
                        document.forms[0].descListaExpedientesRelacionados.value = codProcedimiento
                    }/*if(document.forms[0].descListaExpedientesRelacionados.value != null 
                        || document.forms[0].descListaExpedientesRelacionados.value.length > 0)*/
                }//findProcedimiento
                
function mostrarDatosTercero() {

     // Mostramos el primer tercero con rol por defecto o
     // el indicado por la variable 'terceroCargado', si no es nula
     var porDefecto=0;
     var i=0;
     if (terceroCargado == '') {
         while (i<terceros.length) {
             if(terceros[i][6]=="SI") {
                 porDefecto=i;
                 break;
             }
             i++;
         }
     } else {
         while (i<terceros.length) {
             if(terceros[i][0]==terceroCargado) {
                 porDefecto=i;
                 terceroCargado = '';
                 break;
             }
             i++;
         }
     }

     // relleno los campos del formulario de la pestaña interesados
     document.forms[0].codTerc.value      =terceros[porDefecto][0];
     document.forms[0].codDomTerc.value   =terceros[porDefecto][4];
     document.forms[0].numModifTerc.value =1;
     document.forms[0].txtInteresado.value=terceros[porDefecto][2]; // Razon Social.
     document.forms[0].txtTelefono.value  =terceros[porDefecto][8]; // Telefono.
     document.forms[0].txtCorreo.value    =terceros[porDefecto][9]; // Email.
     document.forms[0].txtPais.value      =terceros[porDefecto][10]; // Pais.
     document.forms[0].txtProv.value      =terceros[porDefecto][11]; // Provincia.
     document.forms[0].txtMuni.value      =terceros[porDefecto][12]; // Municipio.
     document.forms[0].txtDomicilio.value =terceros[porDefecto][5]; // Nombre vía.z
     document.forms[0].txtCP.value        =terceros[porDefecto][13]; // Codigo.
     document.forms[0].cbTipoDoc.value    =terceros[porDefecto][14]; // codDNI.
     document.forms[0].txtDNI.value       =terceros[porDefecto][15]; // dni.

     terceroActual = porDefecto;
     comboRol.buscaCodigo(terceros[porDefecto][7]);                  // rol

     mostrarDescripcionTipoDoc();
     deshabilitarTipoDocTerceroYDoc();

     actualizarBotonesTercero();
}

 function mostrarInteresado(indice) {
     // Asignamos a la var global terceroCargado que controla el funcionamiento
     // de mostrarDatosTercero el codigo del tercero que queremos mostrar.
     terceroCargado = terceros[indice][0];
     mostrarDatosTercero();
 }

function actualizarBotonesTercero() {
    if (terceros.length > 1) {
        mostrarBotonesNavegacionInteresados();

        // Orden dentro de los terceros
        document.getElementById('ordenTercero').firstChild.data =
            (terceroActual + 1) + ' de ' + terceros.length;

        // Flechas de navegación
        if (terceroActual < 1) {
            deshabilitarImagen([document.getElementById('flechaAnterior')], true);
        } else {
            habilitarImagen([document.getElementById('flechaAnterior')], true);
        }
        if (terceroActual > terceros.length - 2) {
            deshabilitarImagen([document.getElementById('flechaSiguiente')], true);
        } else {
            habilitarImagen([document.getElementById('flechaSiguiente')], true);
        }

        // Activar boton lista terceros
        deshabilitarImagen([document.getElementById('botonListTer')],true);
    } else {
        ocultarBotonesNavegacionInteresados();
    }
}

function mostrarBotonesNavegacionInteresados() {
    hacerVisibles(['flechaMenos','ordenTercero','flechaMas','botonListaTerceros']);
}

function ocultarBotonesNavegacionInteresados() {
    hacerInvisibles(['flechaMenos','ordenTercero','flechaMas','botonListaTerceros']);
}

function hacerInvisibles(vector) {
    for (var i=0; i<vector.length; i++) {
        document.getElementById(vector[i]).style.visibility = 'hidden';
    }
}

function hacerVisibles(vector) {
    for (var i=0; i<vector.length; i++) {
        document.getElementById(vector[i]).style.visibility = 'visible';
        document.getElementById(vector[i]).disabled = false;
    }
}

function pulsarAnteriorTercero() {
    if (terceroActual > 0) {
        mostrarInteresado(terceroActual - 1);
    } else {
        alert('Error interesado anterior');
                }
}
                
function pulsarSiguienteTercero() {
    if (terceroActual < terceros.length - 1) {
        mostrarInteresado(terceroActual + 1);
    } else {
        alert('Error interesado siguiente');
    }
}

function deshabilitarTipoDocTerceroYDoc() {

   var campos = new Array(document.getElementById('txtDNI'),
                          document.getElementById('cbTipoDoc'),
                          document.getElementById('descTipoDoc'));
   deshabilitarGeneral(campos);

   var vectorBoton1 =  new Array(document.getElementById("botonTipoDoc"),document.getElementById("botonTipoRemitente"));
   var vectorAnchor1 =  new Array(document.all.anchorTipoDoc,document.all.anchorTipoRemitente);
   deshabilitarImagenBotonGeneral(vectorBoton1, true);
   deshabilitarImagen(vectorAnchor1, true);
}
                
                function mostrarDestino() {
                    
                    if (document.forms[0].cbTipoEntrada.value=='')
                        {
                            borrarDestinoOrdinaria();
                            borrarDestinoOtroReg();
                            document.getElementById("TEOtroReg").style.visibility='hidden';
                            document.getElementById("TEOrdinaria").style.visibility='visible';
                            tp1_p3.setPrimerElementoFoco(document.forms[0].cod_uniRegDestinoORD);
                        } else if (document.forms[0].cbTipoEntrada.value==0){
                        borrarDestinoOtroReg();
                        document.getElementById("TEOtroReg").style.visibility='hidden';
                        document.getElementById("TEOrdinaria").style.visibility='visible';
                        tp1_p3.setPrimerElementoFoco(document.forms[0].cod_uniRegDestinoORD);
                    }
                    else if (document.forms[0].cbTipoEntrada.value==1) {
                        borrarDestinoOrdinaria();
                        document.getElementById("TEOrdinaria").style.visibility='hidden';
                        document.getElementById("TEOtroReg").style.visibility='visible';
                        tp1_p3.setPrimerElementoFoco(document.forms[0].cod_orgDestino);
                    }
                    
                }
                
                function inicializarRecarga() {
                    document.forms[0].cod_orgDestino.value= '<%= cod_org%>';
                    document.forms[0].desc_orgDestino.value= '<%= desc_org%>';
                    document.forms[0].codTerc.value= '<bean:write name="MantAnotacionRegistroForm" property="codTerc"/>';
                    document.forms[0].codDomTerc.value= '<bean:write name="MantAnotacionRegistroForm" property="codDomTerc"/>';
                    document.forms[0].numModifTerc.value= '<bean:write name="MantAnotacionRegistroForm" property="numModifTerc"/>';
                    
                    var cont=0;
                    <logic:iterate id="relacion" name="MantAnotacionRegistroForm" property="relaciones">
                    relaciones[cont] = ['<bean:write name="relacion" property="tipo"/>', '<bean:write name="relacion" property="ejercicio"/>', '<bean:write name="relacion" property="numero"/>'];
                    cont = cont + 1;
                    </logic:iterate>
                    compruebaRelaciones();
                    
                    desactivarFormulario();
                    var listaTemas = new Array();
                    cont=0;
                    <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTemasAsignados">
                    listaTemas[cont]= [ '<bean:write name="elemento" property="codigoTema"/>', '<bean:write name="elemento" property="descTema"/>'];
                    cont= cont +1;
                    </logic:iterate>
                    lista=listaTemas;
                    compruebaTemas();
                    tp1.setSelectedIndex(0);
                    
                    mostrarDestino();
                    
                    
                    actualizarDescripcion('cbTipoEntrada','txtNomeTipoEntrada',cod_tipoEntrada,desc_tipoEntrada);
                    mostrarDescripcionTipoDoc();
                    document.forms[0].asunto.value = unescape('<bean:write name="MantAnotacionRegistroForm" property="asunto"/>');
                    document.forms[0].autoridad.value = unescape('<bean:write name="MantAnotacionRegistroForm" property="autoridad"/>');
                    if(document.forms[0].deHistorico.value == "si") {
                        document.forms[0].cmdCancelarDeHistorico.disabled = false;
                    }
                }                                                                                
            
            function desactivarFormulario() { 
                deshabilitarDatos(document.forms[0]);                
                deshabilitarImagen(document.getElementsByClassName("fa-calendar"),true);                
                deshabilitarImagen(document.getElementsByClassName("fa-chevron-circle-down"),true);                
                deshabilitarImagenBoton("desp", true);                
                desActivarBotones(false);// Activar botones.
            }
            
            function borrarDestinoOrdinaria() {
                document.forms[0].cod_uniRegDestinoORD.value='';
                document.forms[0].desc_uniRegDestinoORD.value='';
            }
            
            function borrarDestinoOtroReg() {
                document.forms[0].cod_orgDestino.value='';
                document.forms[0].desc_orgDestino.value='';
                document.forms[0].cod_uniRegDestino.value='';
                document.forms[0].desc_uniRegDestino.value='';
            }
            
            function actualizarDescripcion(campoCodigo, campoDescripcion, listaCodigos, listaDescripciones) {
                cargarDesc(campoCodigo,campoDescripcion, listaCodigos, listaDescripciones);
            }
            
            function mostrarDescripcionTipoDoc(){
                if (!(Trim(document.forms[0].cbTipoDoc.value) == '')) {
                    actualizarDescripcion('cbTipoDoc','descTipoDoc',cod_tiposIdInteresado,desc_tiposIdInteresado);
                }
            }
            
            function pulsarRechazar() {
                abrirXanelaAuxiliar("<c:url value='/jsp/sge/mainVentana.jsp?source='/>"+
                        "<c:url value='/sge/Tramitacion.do'/>?opcion=ventanaRechazar",window.self,
                     'width=950,height=620,scrollbars=no,status=no',function(datos){
                        if (datos) {
                            pleaseWait('on');
                            document.forms[0].observacionesRechazo.value = datos.obs;
                            document.forms[0].opcion.value  ="rechazarAsiento";                    
                            document.forms[0].target        ="mainFrame";            
                            document.forms[0].action        ="<c:url value='/sge/Tramitacion.do'/>";            
                            document.forms[0].submit();
                        }
                    });
            }
            
            function pulsarAceptar() {
                // Se comenta la siguiente linea y se descomenta el bloque de codigo a
                // continuacion para no pedir observaciones al aceptar.
                //abrirXanelaAuxiliar("<%--c:url value='/sge/Tramitacion.do'/--%>?opcion=ventanaAceptar",window.self,"dialogLeft:10px; dialogTop:10px; dialogWidth:640px; dialogHeight:315px; scroll:no; status:no; resizable:no");
                var mensaje;
                if(document.forms[0].descListaExpedientesRelacionados.value == "") {
                    mensaje = '<%=descriptor.getDescripcion("desAceptar")%>';
                }else{
                    var expedientesAsociados = "";
                    for(x=0; i<numsExpedientesRelacionados.length; i++){
                        expedientesAsociados += numsExpedientesRelacionados[x] + " <br/>";
                    }//for(x=0; i<numsExpedientesRelacionados.length; i++)
                    mensaje = '<%=descriptor.getDescripcion("desAceptar1")%>' + " " + numsExpedientesRelacionados + "?";
                }//if(document.forms[0].txtExp1.value == "") 

                if(jsp_alerta("C",mensaje) ==1) {
                    document.forms[0].opcion.value="aceptarAsiento";
                    document.forms[0].target="mainFrame";
                    document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
                    document.forms[0].submit();
                }//if(jsp_alerta("C",mensaje) ==1)
            }//pulsarAceptar
        
        function pulsarAdjuntarExp(origen){
            var source = "<c:url value='/sge/Tramitacion.do?opcion=adjuntarExpediente&hayListaInt='/>"+document.forms[0].hayListaTerc.value
                +"&registroOrigen=" + document.forms[0].tipoRegistroOrigen.value + "&origenExp=" + origen; 
            var ancho = '550';
            var alto = '300';
            if(origen=="interesado"){
                ancho = '880';
                alto = '570';
            }
            abrirXanelaAuxiliar("<c:url value='/jsp/sge/mainVentana.jsp?source='/>"+source,'ventana',
	'width='+ancho+',height='+alto+',scrollbars=no,status='+ '<%=statusBar%>',function(datosConsulta){
                        if(datosConsulta!=undefined){
                            if(datosConsulta[0] =="si") {
                                pleaseWait('on');
                                document.forms[0].codMunicipio.value=datosConsulta[1];
                                document.forms[0].codProcedimiento.value=datosConsulta[2];
                                document.forms[0].ejercicio.value=datosConsulta[3];
                                document.forms[0].numeroExp.value=datosConsulta[4];
                                document.forms[0].opcion.value="cargar";
                                document.forms[0].target="mainFrame";
                                document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>";
                                document.forms[0].submit();
                            }
                        }
                });
            }

            function pulsarIniciarExp() {
                 var source = "<c:url value='/sge/Tramitacion.do?opcion=iniciarExpedienteAsiento'/>"+"&registroOrigen=" + document.forms[0].tipoRegistroOrigen.value	
                    + "&hayListaTerc=" + document.forms[0].hayListaTerc.value;
                abrirXanelaAuxiliar("<c:url value='/jsp/sge/mainVentana.jsp?source='/>"+source,'ventana',
                    'width=800,height=500,scrollbars=no,status='+ '<%=statusBar%>',function(datosConsulta){
                        if(datosConsulta!=undefined){
                            if(datosConsulta[0] =="si"){
                                datosConsultaGlobal = [datosConsulta[0],datosConsulta[1],datosConsulta[2],
                                    datosConsulta[3],datosConsulta[4]];
                                comprobarPermanenciaAnotacion(datosConsulta,document.forms[0].tipoRegistroOrigen.value);
                            } else {//if(datosConsulta[0] =="si")	
                                pleaseWait('on');	
                                document.forms[0].codMunicipio.value=datosConsulta[1];	
                                document.forms[0].codProcedimiento.value=datosConsulta[2];	
                                document.forms[0].ejercicio.value=datosConsulta[3];	
                                document.forms[0].numeroExp.value=datosConsulta[4];	
                                document.forms[0].opcion.value="cargar";	
                                document.forms[0].target="mainFrame";	
                                document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>";	
                                document.forms[0].submit();	
                            }
                        }//if(datosConsulta!=undefined)
                    });
            }//pulsarIniciarExp
                
            function comprobarPermanenciaAnotacion(datosConsulta, registroOrigen){
                document.forms[0].codMunicipio.value=datosConsulta[1];
                document.forms[0].codProcedimiento.value=datosConsulta[2];
                document.forms[0].uor.value=datosConsulta[3];
                document.forms[0].unidadTramiteInicioSeleccionada.value = datosConsulta[4];
                document.forms[0].tipoRegistroOrigen.value = registroOrigen;
                document.forms[0].opcion.value="comprobarAsociacionMultipleNuevoProcedimiento";
                document.forms[0].target="oculto";
                document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
                document.forms[0].submit();  
            }//comprobarPermanenciaAnotacion
                
            function iniciarExpediente(permanencia, tipoRegistroOrigen){
                if(permanencia == "true"){
                    mensaje = "<%=descriptor.getDescripcion("permanecerAnotacionEnBuzon")%>";
                    
                    if(jsp_alerta("C",mensaje) ==1) {
                        document.forms[0].valorOpcionPermanencia.value = 'true';
                    }else{
                        document.forms[0].valorOpcionPermanencia.value = 'false';
                    }//if(jsp_alerta("C",mensaje) ==1) 
                }else{
                    document.forms[0].valorOpcionPermanencia.value = 'false';
                }//if(permanencia == "true")
                pleaseWait('on');
                document.forms[0].codMunicipio.value=datosConsultaGlobal[1];
                document.forms[0].codProcedimiento.value=datosConsultaGlobal[2];
                document.forms[0].uor.value=datosConsultaGlobal[3];
                document.forms[0].unidadTramiteInicioSeleccionada.value = datosConsultaGlobal[4];
                document.forms[0].tipoRegistroOrigen.value = tipoRegistroOrigen;
                document.forms[0].opcion.value="iniciarExpedienteAsiento";
                document.forms[0].target="oculto";
                document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>";
                document.forms[0].submit();
            }//iniciarExpediente
                
            function recibirNumero(datos) {
                jsp_alerta("A",'<%=descriptor.getDescripcion("msjIniExp1")%>' + " " + datos);
                document.forms[0].numeroExp.value = datos;
                document.forms[0].ejercicio.value = datos.substring(0,4);
                document.forms[0].opcion.value="cargar";
                document.forms[0].target="mainFrame";
                document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>";
                document.forms[0].submit();
            }//recibirNumero
                
                <% if ("soloConsulta".equals(modoInicio)) {%>
                
                function desActivarBotones(desActivar) {
                    with(document.forms[0]){
                        if (!desActivar) {
                            cmdCancelar.style.cursor="hand";
                        }
                        
                        cmdCancelar.disabled=desActivar;
                    }
                }
                
                function pulsarCancelar(){
                    pleaseWait('on');
                    //document.forms[0].numeroExp.value;
                    document.forms[0].opcion.value="cargar";
                    document.forms[0].target="mainFrame";
                    document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>";
                    document.forms[0].submit();
                }
                
                function cambiaBotonera() {
                    mostrarCapasBotones('capaBotones1');
                }
                
                function mostrarCapasBotones(nombreCapa) {
                    document.getElementById(nombreCapa).style.display='';
                }
                
                <% } else {%>
                
                function desActivarBotones(desActivar) {
                    with(document.forms[0]){
                        if(document.getElementById('capaBotones1').style.display=='') {
                            if (!desActivar) {
                                cmdAceptar.style.cursor="hand";
                                cmdRechazar.style.cursor="hand";
                                cmdAdjuntarExp.style.cursor="hand";
                                cmdAdjuntarExpInt.style.cursor="hand";
                                cmdIniciarExp.style.cursor="hand";
                                cmdCancelar.style.cursor="hand";
                            }
                            
                            cmdAceptar.disabled=desActivar;
                            cmdRechazar.disabled=desActivar;
                            cmdAdjuntarExp.disabled=desActivar;
                            cmdAdjuntarExpInt.disabled=desActivar;
                            cmdIniciarExp.disabled=desActivar;
                            cmdCancelar.disabled=desActivar;
                        } 
                    }
                }
                
                function pulsarCancelar(){
                    pleaseWait('on');
                    document.forms[0].opcion.value="inicio";
                    document.forms[0].target="mainFrame";
                    document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
                    document.forms[0].submit();
                }
                
                function cambiaBotonera() {
                    if(document.forms[0].deHistorico.value == "si") {
                        mostrarCapasBotones('capaBotones3');
                    }else{ 
                    mostrarCapasBotones('capaBotones1');
                }
                
            }
            
            function mostrarCapasBotones(nombreCapa) {
                
                document.getElementById('capaBotones1').style.display='none';
                document.getElementById('capaBotones3').style.display='none';
                document.getElementById(nombreCapa).style.display='';
                
            }
            
            <% }%>
            
            function pulsarCancelarDeHistorico() {
                //self.close();
                var opener = self.parent.opener.xanelaAuxiliarArgs[0];
                var fechaDesde = opener.document.forms[0].fechaDesde.value;
                var fechaHasta = opener.document.forms[0].fechaHasta.value;
                var params = '?fechaDesde='+fechaDesde+"&fechaHasta="+fechaHasta;
                document.forms[0].opcion.value="historico";
                document.forms[0].target="mainFrame";
                document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>" + params;
                document.forms[0].submit();
            }
            
            // Funciones de la pestanha de Documentos referentes a las acciones del SW de FiramDoc.
            
            // Funcion redefinida para procesar las pulsaciones sobre la tabla (evento onDbClick).
            function callFromTableTo(rowId, tableName) {
                if (tableName==tabDocs.id) visualizaDocumento(rowId);
            }
            
            
            // Funcion que lanza la visualizacion de un documento.
            function visualizaDocumento(rowId) {
                var year   = document.forms[0].ano.value;
                var numero = document.forms[0].numero.value;
                var cod_uor = document.forms[0].cod_uor.value;
                var tipRegOrigen = document.forms[0].tipoRegistroOrigen.value;
                var tipoRegistro = document.forms[0].tipoRegistro.value;
                var codUorRegistro = document.forms[0].unidadOrgan.value;                
                var codDepartamento = document.forms[0].identDepart.value;
                
                var destino = "<%=request.getContextPath()%>/VerDocumentoAnexo?codigo=" + listaDocs[rowId][2] + "&nombreFichero=" + listaDocs[rowId][0] +
                        "&ejercicio=" + year + "&numero=" + numero + "&uor=" + cod_uor + "&tipoReg=" + tipRegOrigen + "&fechaFichero=" + listaDocs[rowId][2] +
                        "&extensionFichero=" + listaDocs[rowId][1] + "&tipoRegistro=" + tipoRegistro + "&codUorRegistro=" + codUorRegistro + "&codDepartamento=" + codDepartamento + "&embedded=true";
                window.open(destino, "ventana1", "left=10, top=10, width=850, height=800, scrollbars=no, menubar=no, location=no, resizable=no");
            }
            
            // Funcion para controlar el pulsar sobre el boton visualizar.
            function pulsarVisualizar() {
                if(tabDocs.selectedIndex>=0){
                    visualizaDocumento(tabDocs.selectedIndex);
                }
            }
            
            // Fin de las funciones de la pestanha documentos.
            
            function pulsarInteresados(){
                
                var expediente = new Array();
                
                codMun= document.forms[0].codDomTerc.value;
                codEje=<bean:write name="MantAnotacionRegistroForm" property="ano"/>;
                codNum=<bean:write name="MantAnotacionRegistroForm" property="numero"/>;
                codOur=<bean:write name="MantAnotacionRegistroForm" property="unidadOrgan"/>;
                codDep=<bean:write name="MantAnotacionRegistroForm" property="identDepart"/>;
                codProc='<bean:write name="MantAnotacionRegistroForm" property="codProcedimientoRoles"/>';
                munProc='<bean:write name="MantAnotacionRegistroForm" property="mun_procedimiento"/>';
                codTip='<bean:write name="MantAnotacionRegistroForm" property="tipoRegistro"/>';
                
                expediente[0]=[codMun];
                expediente[1]=[codEje];
                expediente[2]=[codNum];
                expediente[3]=[codOur];
                expediente[5]=undefined;
                
                expediente[4] = terceros;
                expediente[6] = 'N';
                
                abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + APP_CONTEXT_PATH + 
                    "/Interesados.do?opcion=cargarRegistro&codMun="+codMun+"&codEje="+
                    codEje+"&codNum="+codNum+"&codOur="+codOur+"&codTip="+codTip+"&codProc="+codProc+
                    "&munProc="+munProc+"&codDep=" + codDep,
                    expediente,
                    'width=850,height=410',function(datos){});
            }
            
function consultarExpRelacionado()
{
var EQUAL = "=";
var AMPERSAND = "&";
var GUION = "-";
//if(document.forms[0].txtExp1.value==null || document.forms[0].txtExp1.value.length==0){
if(document.forms[0].descListaExpedientesRelacionados.value==null || document.forms[0].descListaExpedientesRelacionados.value.length==0){
    jsp_alerta('A', '<%=descriptor.getDescripcion("msjExpRelNotFound")%>');
}else{
	//var source = "<%= request.getContextPath() %>" + "/sge/VerificarConsultaExpRelacionado.do?codExp=" + document.forms[0].txtExp1.value;
        var source = "<%= request.getContextPath() %>" + "/sge/VerificarConsultaExpRelacionado.do?codExp=" + document.forms[0].descListaExpedientesRelacionados.value;

	var request = getXMLHttpRequest();
	if(request!=null){

		var url = "<%= request.getContextPath() %>" + "/sge/VerificarConsultaExpRelacionado.do";
		//var parametros = "codExp="+ document.forms[0].txtExp1.value;
                var parametros = "codExp="+ document.forms[0].descListaExpedientesRelacionados.value;
		var ajax = getXMLHttpRequest();
		ajax.open("POST",url,false);
		ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
		ajax.send(parametros);
		var res = ajax.responseText;


			if (res!=null && res.length>0 && ajax.readyState==4)
			{
					if (ajax.status==200){
						var res = ajax.responseText;
						var permiso = res.split(EQUAL);
						if(permiso!=null){

							var tienePermiso = permiso[1].split(GUION);
							if(permiso[0].trim()=="permiso" && tienePermiso[0].trim()=="si")
								{
									var codMunicipio = tienePermiso[1].trim();
									//var numero = document.forms[0].txtExp1.value;
                                                                        var numero = document.forms[0].descListaExpedientesRelacionados.value;

									var expediente = numero.split("/");
									var ejercicio = expediente[0];
									var codProcedimiento = expediente[1];
									var modoConsulta = "si";
									var opcion ="cargar";

									var source2="<%= request.getContextPath() %>/sge/FichaExpediente.do?opcion=" + opcion + "&modoConsulta=" + modoConsulta + "&codMunicipio=" + codMunicipio + "&ejercicio=" + ejercicio + "&numero=" + numero + "&codProcedimiento=" + codProcedimiento;

									abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source2,'ventana','width=1100px,height=1000px,status='+ '<%=statusBar%>' + ',toolbar=no,scrollbars=yes',function(datosConsulta){});
									}
								}
								else {
									jsp_alerta('A','<%=descriptor.getDescripcion("msjExpRelNotAccess")%>');
									}
								}
			}
	}
	else{
		jsp_alerta('A','<%=descriptor.getDescripcion("msjExpRelNotAccess")%>');
		}
	}
} 
                                        
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
                                        }
                                    }
                                }
                                
                                
                                /* Abre la ventana para ver la lista de asientos relacionados */
                                function pulsarRelaciones() {
                                    var args = new Array();
                                    args[0]=relaciones;
                                    args[1]=window.self;
                                    args[2]=modoConsulta;
                                    
                                    var source = "<html:rewrite page='/jsp/sge/listadoRelacionesAsiento.jsp?dummy='/>";
                                    abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp?source='/>" + source,args,
                                        'width=350,height=410',function(datos){});
                                }
                                
                                /* Redirige a un asiento relacionado*/
                                function verAnotacion(tipo, ejercicio, numero){
                                    
                                    document.forms[0].codDepartamento.value = '<bean:write name="MantAnotacionRegistroForm" property="identDepart"/>';
                                    document.forms[0].codUnidadRegistro.value = '<bean:write name="MantAnotacionRegistroForm" property="unidadOrgan"/>';
                                    document.forms[0].tipoRegistro.value = tipo;
                                    document.forms[0].ejerNum.value = ejercicio + '/' + numero;
                                    document.forms[0].origenServicio.value = '<%= request.getParameter("origenServicio") %>';
                                    
                                    document.forms[0].opcion.value="consultaAsiento";
                                    document.forms[0].target="mainFrame";
                                    document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
                                    document.forms[0].submit();   
                                }
                                
                                /* Remarca el boton de relaciones si hay relaciones */
                                function compruebaRelaciones(){                                
                                    var estilo = "";
                                    if (relaciones.length > 0)
                                        estilo = 'botonGeneralResaltado';
                                    else
                                        estilo = 'botonGeneral';

                                    var botonesRelaciones = document.getElementsByName("cmdRelaciones");
                                    if(botonesRelaciones!=null && botonesRelaciones.length==1){
                                            botonesRelaciones[0].className = estilo;
                                    }
                                }

                                function compruebaTemas(){
                                    var estilo = "";
                                    if (lista.length > 0)
                                        estilo = 'botonGeneralResaltado';
                                    else
                                        estilo = 'botonGeneral';

                                    var botonesActuaciones = document.getElementsByName("cmdActuaciones");
                                    if(botonesActuaciones!=null && botonesActuaciones.length==1){
                                            botonesActuaciones[0].className = estilo;
                                    }
                                }
                                
            /**
             * Muestra los errores ocurridos en el proceso de catalogar un documento. codError puede ser:
             * -1: La operación ha fallado
             * -2: El formato de los datos de entrada es incorrecto
             * 1: No ha seleccionado ninguna fila
             * 2: Ha ocurrido un error genérico en la comunicación cliente-servidor.
             * 3: El documento seleccionado no admite catalogación.
             * 4: Error al obtener una conexión a la BBDD
             * 5: Ha ocurrido un error al grabar la catalogación del documento
             * 6: Ha ocurrido un error al recuperar la catalogación del documento
             * 7: Error al recuperar los tipos documentales de Lanbide
             * 10: Todos los documentos que hayan sido digitalizados deben estar catalogados
            */
            function mostrarErrorCatalogar(codError){
                if(codError=="1"){
                    jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                } else if(codError=="2"){
                    jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
                } else if(codError=="3"){
                    jsp_alerta("A",'El documento seleccionado no admite catalogación.');
                } else if(codError=="4"){
                    jsp_alerta("A",'Error al obtener una conexión a la BBDD');
                } else if(codError=="5"){
                    jsp_alerta("A",'Ha ocurrido un error al grabar la catalogación del documento.');
                } else if(codError=="6"){
                    jsp_alerta("A",'Ha ocurrido un error al recuperar la catalogación del documento.');
                } else if(codError=="7"){
                    jsp_alerta("A",'Ha ocurrido un error al recuperar los tipos documentales.');
                } else if(codError=="-1"){
                    jsp_alerta("A",'La operación ha fallado.');
                } else if(codError=="-2"){
                    jsp_alerta("A",'El formato de los datos de entrada es incorrecto.');
                } else if(codError=="9"){
                    jsp_alerta("A",'Ha ocurrido un error al recargar la tabla de documentos.');
                } else if(codError=="10"){
                    jsp_alerta("A",'Antes de gestionar este registro, debe catalogar todos los documentos digitalizados que tiene asociados.');
                } else if(codError =="11"){
                    jsp_alerta("A",'No se puede gestionar ahora el registro porque el proceso de grabación de documentos de este registro aún no ha terminado');
                }
            }                                
                                
        </SCRIPT>
        
    </head>
    
    <BODY class="bandaBody" onload="javascript:{ pleaseWait('off'); 
        
        inicializar();
    }">
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include> 
        
        <html:form action="/MantAnotacionRegistro.do" target="_self">
            
            <!-- Para navegacion a otros asientos relacionados -->
            <input type="hidden"  name="codDepartamento"/>
            <input type="hidden"  name="codUnidadRegistro"/>
            <input type="hidden"  name="tipoRegistro"/>
            <input type="hidden"  name="ejerNum"/>
            <input type="hidden"  name="origenServicio"/>
            <input type="hidden" name="valorOpcionPermanencia">
            
            <html:hidden  property="opcion" value=""/>
            <html:hidden property="identDepart" />
            <html:hidden property="unidadOrgan" />
            <html:hidden property="tipoReg" />
            <html:hidden property="estadoAnotacion" />
            
            <html:hidden  property="codTerc" value="0"/>
            <html:hidden  property="codDomTerc" value="0"/>
            <html:hidden  property="numModifTerc" value="0"/>
            <html:hidden  property="txtIdTercero" value=""/>
            <html:hidden  property="txtIdDomicilio" value=""/>
            <html:hidden  property="txtVersion" value=""/>
            <html:hidden property="observacionesRechazo" value=""/>
            
            <input type="hidden" name="situacion">
            <input type="hidden" name="modoConsulta">
            <input type="hidden" name="desdeConsulta">
            <input type="hidden" name="desdeExpRel">
            <input type="hidden" name="expRelacionado">
            <input type="hidden" name="porCampoSup">
            
            <input type="hidden" name="codMunicipioIni">
            <input type="hidden" name="ejercicioIni">
            <input type="hidden" name="numeroIni">
            <input type="hidden" name="hayListaTerc">

            
            <html:hidden  property="hayTexto" value=""/>
            
            <html:hidden  property="listaTemas" value="" />
            
            <input type="hidden" name="codMunicipio" value="<%=munExp%>">
            <input type="hidden" name="codProcedimiento" value="<%=proExp%>">
            <input type="hidden" name="ejercicio" value="<%=ejeExp%>">
            <input type="hidden" name="numeroExp" value="<%=numExp %>">
            <input type="hidden" name="uor" value="">
            
            <input type="hidden" name="deHistorico">
            <input type="hidden" name="unidadTramiteInicioSeleccionada" value=""/>
            
            <!-- SW de FirmaDoc -->
            <input type="hidden" name="codigoDoc">
         
        <% if ("S".equals(tipoAnotacion) || "Relacion_S".equals(tipoAnotacion)) {%>
            <div class="txttitblancoder"><%=descriptor.getDescripcion(titPag)%></div>
        <% } else {%>
            <div class="txttitblanco"><%=descriptor.getDescripcion(titPag)%></div>
        <% } %>
            <div class="encabezadoGris">
                <TABLE  cellpadding="0px" cellspacing="0px">
                    <TR>
                        <TD  class="etiqueta">&nbsp;&nbsp<%=descriptor.getDescripcion(numOrden)%></TD>
                        <TD class="columnP">
                            <html:text  styleClass='inputTexto' size="4" maxlength="4" property="ano" onkeypress = "javascript:return SoloDigitos(event);" onfocus="this.select();" /> /
                            <html:text  styleClass='inputTexto' size="5" maxlength="4" property="numero" onkeypress = "javascript:return SoloDigitos(event);" onfocus="this.select();" />
                        </TD> 

                        <TD class="etiqueta">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp</TD>

                 <%    

                if (estadoAnotacionA==9){    

                  %> 

                     <TD>
                         <TABLE>
                            <TR>
                                <TD  valign='middle'> 
                                     <A href="#" style="text-decoration:none;" onclick="pulsarDiliAnulacion();return false;">
                                       <span class="fa-fa-pencil-square-o" alt="<%=descriptor.getDescripcion("altDiligAnul")%>" 
                                             title="<%=descriptor.getDescripcion("altDiligAnul")%>"  id="botonDiligenciasAnu" 
                                             name="botonDiligenciasAnu" style="cursor:hand;text-decoration:line-through;" ></span>
                                    </A>
                                </TD>
                                <TD valign='middle' style="color:red; font-size:10px;">&nbsp;<%=descriptor.getDescripcion("etiq_anulada")%> </TD>
                            </TR>
                        </TABLE>
                        <div id="capaDiligenciasAnu" style="position:absolute; z-index:10; visibility: hidden;font-size: 10px; font-family: Verdana, Arial, Helvetica, Sans-serif; font-style: normal; color: #006500; background-color: #FFFFFF; border-top: #666666 1px solid; border-bottom: #666666 1px solid; border-left: #666666 1px solid;border-right: #666666 1px solid; overflow-x:no; overflow-y:yes"></div>
                     </TD>
              <%    }     %>    
               </TR>
             </TABLE>
            </div>
            <div class="contenidoPantalla">
               <div class="tab-pane" id="tab-pane-1">
                    <script type="text/javascript">
                        tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
                    </script>
                    <!-- CAPA 1: DATOS GENERALES
------------------------------ -->
                    <div class="tab-page" id="tabPage1">

                        <h2 class="tab"><%=descriptor.getDescripcion("res_pestana1")%></h2>

                        <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>

                        <TABLE id ="tablaDatosGral" class="contenidoPestanha">
                            <TR>
                                <TD>
                                    <TABLE width="100%">
                                        <TR>
                                            <TD  class="etiqueta"> <%=descriptor.getDescripcion("etiq_fecGrab")%>: </TD>
                                            <TD class="columnP">
                                                <html:text styleId="obligatorio"  styleClass="inputTxtFechaObligatorio" size="10" property="fechaDocumento"/>
                                                &nbsp;
                                                <A id="anclaD" style="text-decoration:none;">
                                                    <span class="fa fa-calendar" aria-hidden="true" name="calFechaGrabacion" alt="Data" ></span>
                                                </A>
                                            </TD>
                                            <TD  class="etiqueta"><%=descriptor.getDescripcion("res_fecPres")%>: </TD>
                                            <TD class="columnP">
                                                <html:text styleId="obligatorio"  styleClass="inputTxtFechaObligatorio" size="10"  property="fechaAnotacion"/>
                                                &nbsp;
                                                <A id="anclaD" style="text-decoration:none;">
                                                    <span class="fa fa-calendar" aria-hidden="true" name="calFechaAnotacion" alt="Data" ></span>
                                                </A>
                                            </TD>
                                            <TD   class="etiqueta"><%=descriptor.getDescripcion("res_HoraPres")%>:</TD>
                                            <TD class="columnP">
                                                <html:text styleClass="inputTxtFecha" size="6" maxlength="5" property="horaMinAnotacion"/>                                                                                
                                            </TD>
                                            <TD  class="etiqueta"><%=descriptor.getDescripcion("etiq_fecDoc")%>: </TD>
                                            <TD class="columnP">
                                                <html:text styleClass="inputTxtFecha" size="10" maxlength="5" property="fechaDoc"/>
                                                &nbsp;
                                                <A id="anclaD" style="text-decoration:none;">
                                                    <span class="fa fa-calendar" aria-hidden="true" name="calFechaDocumento" alt="Data" ></span>
                                                </A>                                                       
                                            </TD>
                                        </TR>
                                    </TABLE>
                                </TD>
                            </TR>
                            <TR>
                                <TD class="sub3titulo"><%=descriptor.getDescripcion("gEtiqIntDom")%></TD>
                            </TR>
                            <TR>
                                <TD>
                                    <TABLE width="100%">
                                        <TR>
                                            <TD style="width: 12%" class="etiqueta"><%=descriptor.getDescripcion("res_tipoDoc")%>:</TD>
                                            <TD style="width: 22%" class="columnP">
                                                <html:text styleId="obligatorio" property="cbTipoDoc" styleClass="inputTextoObligatorio" style="width:10%"/>
                                                <html:text styleClass="inputTextoObligatorio" styleId="obligatorio" property="descTipoDoc" style="width:70%" readonly="true" />
                                                <A style="text-decoration:none;" id="anclaD" name="anchorTipoDoc" onclick="javascript:this.focus();">
                                                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonTipoDoc" name="botonTipoDoc" style="cursor:hand; border: 0px none"></span>
                                                </A>
                                            </TD>
                                            <TD style="width: 10%" class="columnP">
                                                <html:text styleClass="inputTextoObligatorio" size="12" maxlength="9" property="txtDNI" styleId="obligatorio" />
                                            </TD>
                                             <TD style="width: 46%">

                                              <table width="85%" style="text-align:left; display:inline">
                                                <!-- Separador --> <span style="display:inline">&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                                <span id="flechaMenos" style="display:inline;visibility:hidden">&nbsp;
                                                    <span class="fa fa-arrow-circle-o-left" aria-hidden="true" onclick="javascript:pulsarAnteriorTercero();" id="flechaAnterior" name="flechaAnterior" style="cursor:hand" alt="<%=descriptor.getDescripcion("tipIntAnterior")%>" title="<%=descriptor.getDescripcion("tipIntAnterior")%>"></span>&nbsp
                                                </span>
                                                <span id="ordenTercero" class="etiqueta" style="display:inline; vertical-align:middle;visibility:hidden">&nbsp;</span>
                                                <span id="flechaMas" style="vertical-align:bottom;display:inline;visibility:hidden">&nbsp;
                                                    <span class="fa fa-arrow-circle-o-right" aria-hidden="true" onclick="javascript:pulsarSiguienteTercero();" id="flechaSiguiente" name="flechaSiguiente" style="cursor:hand" alt="<%=descriptor.getDescripcion("tipIntSiguiente")%>" title="<%=descriptor.getDescripcion("tipIntSiguiente")%>"></span>
                                                </span>
                                                <!-- Separador --> <span style="display:inline">&nbsp;</span>
                                                <span id="botonListaTerceros" style="display:inline;visibility:hidden">
                                                    <span class="fa fa-list" id="botonListTer" name="botonListTer" alt="<%=descriptor.getDescripcion("tipListaInter")%>" 
                                                          title="<%=descriptor.getDescripcion("tipListaInter")%>" onclick="javascript:pulsarInteresados();return false;"></span>
                                                </span>
                                              </table>
                                              <table width="15%" style="text-align:right; display:inline"> </table>
                                             </TD>
                                        </TR>
                                    </TABLE>
                                </TD>
                            </TR>
                            <TR>
                                <TD>
                                    <TABLE width="100%">
                                        <TR>
                                            <TD style="width: 12%" class="etiqueta"> <%=descriptor.getDescripcion("gEtiqNombr")%>/
                                            <BR><%=descriptor.getDescripcion("gEtiqRazonSoc")%>: </TD>
                                            <TD>
                                                <html:text  styleClass="inputTexto" style="width:99%;" maxlength="50"  property="txtInteresado" readonly="false" tabindex="-1"/>
                                            </TD>
                                        </TR>
                                    </TABLE>
                                </TD>
                            </TR>
                            <TR>
                                <TD>
                                    <TABLE width="100%">
                                        <TR>
                                            <TD style="width: 12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqDomicilio")%>:</TD>
                                            <TD class="columnP">
                                                <html:text  styleClass="inputTexto" property="txtDomicilio" style="width:99%;" maxlength="60" readonly="true" tabindex="-1"/>
                                            </TD>
                                        </TR>
                                    </TABLE>
                                </TD>
                            </TR>
                            <TR>
                                <TD>
                                    <TABLE width="100%">
                                        <TR>
                                            <TD style="width: 12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqProvincia")%>:</TD>
                                            <TD style="width: 24%" class="columnP">
                                                <html:hidden property="txtPais"  styleClass="inputTexto" styleId="txtPais" size="27" readonly="true" tabindex="-1"/>
                                            <html:text property="txtProv"  styleClass="inputTexto" styleId="txtProv" style="width:211;" readonly="true" tabindex="-1"/> </TD>
                                            <TD style="width: 10%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqMunicipio")%>:</TD>
                                            <TD style="width: 37%" class="columnP">
                                            <html:text property="txtMuni"  styleClass="inputTexto" styleId="txtMuni" style="width:300;" readonly="true" tabindex="-1"/></TD>
                                            <td style="width: 8%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqCodPostal")%>:</td>
                                            <td class="columnP">
                                                <html:text  styleClass="inputTexto" style="width:75;" maxlength="5" property="txtCP" readonly="true" tabindex="-1"/>
                                            </td>   
                                        </TR>
                                    </TABLE>
                                </TD>
                            </TR>
                            <TR>
                                <TD>
                                    <TABLE width="100%" cellspacing="0px" cellpadding="0px">
                                        <TR>
                                            <TD style="width: 12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqTelfFax")%>:</TD>
                                            <TD style="width: 24%" class="etiqueta">
                                                <html:text  property="txtTelefono" styleClass="inputTexto" style="width:211;" maxlength="20" readonly="true" tabindex="-1"/>
                                            </TD>
                                            <TD style="width: 10%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqEmail")%>:</TD>
                                            <TD class="columnP" style="padding-left: 1px">
                                                <html:text  property="txtCorreo" styleClass="inputTexto" style="width:300;" maxlength="30" readonly="true" tabindex="-1"/>
                                            </TD>
                                            <td width="20px" class="etiqueta" align="center"><%=descriptor.getDescripcion("etiqRol")%>:</td>
                                            <td align="right" style="padding-right:7px">
                                                <input type="hidden" class="inputTextoObligatorio" id="codRolTercero" name="codRolTercero">
                                                <input type="text" class="inputTextoObligatorio" id="descRolTercero" name="descRolTercero" style="width: 100px; text-align:center" readOnly="true">&nbsp;
                                                <a id="anchorRolTercero" name="anchorRolTercero" style="display:none; visibility:hidden;">
                                                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonRol" name="botonRol" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor: pointer;"></span>
                                                </a>
                                            </td>
                                        </TR>
                                    </TABLE>
                                </TD>
                            </TR>
                            <TR>
                                <TD class="sub3titulo"><%=descriptor.getDescripcion("gEtiqDatosDocumento")%></TD>
                            </TR>
                            <TR>
                                <TD>
                                    <TABLE width="100%">
                                        <TR>
                                            <TD style="width: 50%" class="etiqueta"><%=descriptor.getDescripcion("res_asunto")%>:
                                            </TD>
                                            <TD class="etiqueta"><%=descriptor.getDescripcion("etiq_observaciones")%>:</TD>

                                        </TR>
                                        <TR>
                                            <TD class="columnP">
                                                <html:textarea styleId="obligatorio" styleClass="textareaTextoObligatorio" cols="75" rows="3" property="asunto"  maxlength="2" value="" style="width:99%"></html:textarea>
                                            </TD>
                                            <TD class="columnP">
                                                <html:textarea styleId="obligatorio" styleClass="textareaTextoObligatorio" cols="75" rows="3"  property="observaciones" maxlength="2" value="" style="width:99%"></html:textarea>
                                            </TD>
                                        </TR>
                                    </TABLE>
                                </TD>
                            </TR>
                            <TR>
                                <TD>
                                    <TABLE width="100%">
                                        <TR>
                                            <%
                                            if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion))) {
                                            %>
                                                <TD style="width: 8%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqUnidDestino")%>:</TD>
                                            <% } else { %>
                                                <TD style="width: 8%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqUnidOrigen")%>:</TD>
                                            <% } %>
                                            <TD class="columnP">
                                                <html:hidden property="cod_uniRegDestinoORD"/>
                                                <html:text styleClass="inputTextoObligatorio"  property="cod_uor" style="width:6%"/>
                                                <html:text styleClass="inputTextoObligatorio"  property="desc_uniRegDestinoORD" style="width:85%" readonly="true"/>
                                                <A style="text-decoration:none;" id="anclaD" name="anchorUnidadeRexistroORD" >
                                                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonUnidadeRexistroORD"></span>
                                                </A>
                                            </TD>
                                        </TR>
                                    </TABLE>
                                </TD>
                            </TR>
                            <TR>
                                <TD>
                                    <TABLE width="100%">
                                        <TR>
                                            <TD style="width: 50%" class="etiqueta"><%=descriptor.getDescripcion(tipo)%>:
                                            </TD>
                                            <TD class="etiqueta"><%=descriptor.getDescripcion("res_tipoDoc")%>:
                                            </TD>                                                                
                                        </TR>
                                        <TR>
                                            <TD class="columnP">
                                                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio"  property="cbTipoEntrada" maxlength="1" style="width: 10%" />
                                                <html:text styleClass="inputTextoObligatorio" styleId="obligatorio"  property="txtNomeTipoEntrada" style="width:75%" readonly="true" />
                                                <A style="text-decoration:none;" id="anclaD" name="anchorTipoEntrada"
                                                   >
                                                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonTipoEntrada"></span>
                                                </A>
                                            </TD>
                                            <TD class="columnP">
                                                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio"  property="txtCodigoDocumento" style="width: 10%" />
                                                <html:text styleClass="inputTextoObligatorio" styleId="obligatorio" property="txtNomeDocumento" style="width:75%" readonly="true"/>
                                                <A style="text-decoration:none;" name="anchorTipoDocumento" id="anclaD" >
                                                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonTipoDocumento"></span>
                                                </A>
                                            </TD>
                                        </TR>
                                    </TABLE>                                                        
                                </TD>
                            </TR>
                            <TR>
                                <TD>
                                    <TABLE width="100%">
                                        <TR>
                                            <TD style="width: 35%" class="etiqueta"> <%=descriptor.getDescripcion("res_tipoTrans")%>: </TD>
                                            <TD style="width: 15%" class="etiqueta"> <%=descriptor.getDescripcion("res_numTransp")%>:</TD>
                                            <TD class="etiqueta"><%=descriptor.getDescripcion("res_tipoRemit")%>:</TD>                                                                
                                        </TR>
                                        <TR>
                                            <TD class="columnP">
                                                <html:text styleClass="inputTexto" property="cod_tipoTransporte" size="8" style="align:right;width: 10%" />
                                                <html:text styleClass="inputTexto" property="desc_tipoTransporte" style="width:75%" readonly="true" />
                                                <A style="text-decoration:none;" id="anclaD" name="anchorTipoTransporte"
                                                   >
                                                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonTipoTransporte"></span>
                                                </A>
                                            </TD>
                                            <TD class="columnP">
                                                <html:text  styleClass="inputTexto" style="width:99%" maxlength="4" property="txtNumTransp"/>
                                            </TD>
                                            <TD class="columnP">
                                                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio"  property="cod_tipoRemitente" style="width:10%" />
                                                <html:text styleClass="inputTextoObligatorio" styleId="obligatorio" property="txtNomeTipoRemitente" style="width:75%" readonly="true" />
                                                <A style="text-decoration:none;" id="anclaD" name="anchorTipoRemitente"
                                                   >
                                                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonTipoRemitente" name="botonTipoRemitente"></span>
                                                </A>
                                            </TD>
                                        </TR>                                                            
                                    </TABLE>
                                </TD>
                            </TR>
                            <TR>
                                <TD>
                                    <TABLE width="100%">
                                        <TR>
                                            <TD style="width: 12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqProc")%>:</TD>
                                            <TD style="width: 47%" class="columnP">
                                                <html:text styleClass="inputTexto"  property="cod_procedimiento" style="width:10%" />
                                                <html:text styleClass="inputTexto" property="desc_procedimiento" style="width:75%" readonly="true" />
                                                <A style="text-decoration:none;" id="anclaD" name="anchorProcedimiento">
                                                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonProcedimiento"></span>
                                                </A>
                                            </TD>
                                            <TD style="width: 18%" class="etiqueta"><%=descriptor.getDescripcion("res_ExpRel")%>:</TD>
                                            <TD class="columnP">
                                                <%--<html:text  styleClass="inputTexto" style="width:193px;" maxlength="30"  property="txtExp1" onfocus="this.select();" onchange="cambiaBotonera();"   />--%>
                                                <input type="text" name="codListaExpedientesRelacionados" id="codListaExpedientesRelacionados" class="inputTexto" value="" onkeyup="xAMayusculas(this);" style="display:none; width:15%;"/>
                                                <input type="text" name="descListaExpedientesRelacionados"  id="descListaExpedientesRelacionados" class="inputTexto" readonly="true" value="" style="width:75%"/>
                                                <a id="anchorListaExpedientesRelacionados" name="anchorListaCertificados">
                                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:hand;"></span>
                                                </a>
                                                <a onclick="consultarExpRelacionado();">
                                                    <span class="fa fa-share" aria-hidden="true"  style="cursor:hand"></span>
                                                </a>
                                            </TD>
                                        </TR>                                           
                                    </TABLE>
                                </TD>
                            </TR>
                            <TR>
                                <TD>
                                    <TABLE width="100%">
                                        <TR>
                                            <% if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion))) {%>
                                            <TD style="width: 12%;visibility:<%=aut%>" class="etiqueta"><%=descriptor.getDescripcion("etiq_autoridad_a")%>:</TD>
                                            <TD style="width: 88%;visibility:<%=aut%>" class="columnP">
                                            <%} else {%>
                                            <TD style="width: 12%;visibility:<%=aut%>" class="etiqueta"><%=descriptor.getDescripcion("etiq_autoridad_desde")%>:</TD>
                                            <TD style="width: 88%;visibility:<%=aut%>" class="columnP">                  
                                            <% }%>
                                                <html:text styleClass="inputTexto" property="autoridad" style="width:99%" value=""></html:text>                      
                                            </TD>
                                        </TR>


                                        <c:if test="${sessionScope.MantAnotacionRegistroForm.registro.registroTelematico eq true}">
                                        <tr>
                                            <td colspan="2">&nbsp;</td>
                                        </tr>    
                                        <TR>                                                                         
                                            <TD COLSPAN="2" class="etiqueta" align="center">Anotacion procedente del registro telemático</TD>

                                        </TR>
                                        </c:if>
                                    </TABLE>
                                </TD>
                            </TR>                                                                                                                                                                                                                                                                                                
                        </TABLE>

                    </div>
                    <!-- CAPA 2: OTROS DATOS ------------------------------ -->

                    <div class="tab-page" id="tabPage3">

                        <h2 class="tab"><%=descriptor.getDescripcion("etiq_pestanaOtrosDatos")%></h2>

                        <script type="text/javascript">tp1_p3 = tp1.addTabPage( document.getElementById( "tabPage3" ) );</script>

                        <TABLE id="tablaDestino" class="contenidoPestanha">
                            <tr>
                                <td>
                                    <table width="100%">
                                        <TR>
                                            <TD colspan="2" class="sub3titulo"><%=descriptor.getDescripcion("gEtiqDocuAportados")%></TD>
                                        </TR>
                                        <TR>
                                            <TD id="tablaDocs"></TD>
                                            <td align="center" style="width:150px;padding-top: 10px">
                                                <% if (mostrarCatalogar) { %>
                                                    <input type= "button" class="botonGeneral" value="Catalogación" alt="Catalogar documento" title="Catalogar documento" name="cmdCatalogar" onclick="pulsarCatalogarDoc();">
                                                <% } %>
                                                <input type= "button" class="botonGeneral" accesskey="H" style="margin-top:5px" value='<%=descriptor.getDescripcion("gbSWVisualizar")%>' name="cmdVisualizar" onclick="pulsarVisualizar();" style="cursor:hand">
                                            </td>
                                        </TR>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td class="sub3titulo"><%=descriptor.getDescripcion("etiqDocAportados")%></td>               
                            </tr>
                            <tr>
                                <td id="tablaDocAportados"></td>
                            </tr>
                            <tr>
                                <td>
                                    <DIV id="TEOtroReg" name="TEOtroReg" STYLE="width:100%;display:hidden;" >
                                        <table style="width:90%;">
                                            <TR>
                                                <TD colspan="2" class="sub3titulo"><%=descriptor.getDescripcion(origen)%></TD>
                                            </TR>                                                
                                            <TR>
                                                <TD style="width: 18%" class="etiqueta"> <%=descriptor.getDescripcion("tit_Org")%>:</TD>
                                                <TD class="columnP">
                                                    <table style="width:100%;">
                                                    <tr>
                                                        <td>
                                                            <html:text styleClass="inputTextoObligatorio"  property="cod_orgDestino" size="8"/>
                                                        </td>
                                                         <td>
                                                            <html:text styleClass="inputTextoObligatorio"  property="desc_orgDestino" style="width:580" readonly="true"/>
                                                            <A style="text-decoration:none;" id="anclaD" name="anchorOrganizacionDestino">
                                                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonOrganizacionDestino"></span>
                                                            </A>
                                                        </td>
                                                    </tr>                                                                                                
                                                    </table>
                                                </TD>
                                            </TR>
                                            <TR>
                                                <TD class="etiqueta"><%=descriptor.getDescripcion("gEtiqUnidReg")%>:</TD>
                                                <TD class="columnP">
                                                    <table style="width:100%;">
                                                    <tr>
                                                        <td>
                                                            <html:text styleClass="inputTexto"  property="cod_uniRegDestino" size="8" onfocus="this.select();"/>
                                                        </td>
                                                        <td>
                                                            <html:text styleClass="inputTexto" property="desc_uniRegDestino" style="width:580" readonly="true" />
                                                            <A style="text-decoration:none;" id="anclaD" name="anchorUnidadeRexistro">
                                                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonUnidadeRexistro"></span>
                                                            </A>
                                                        </td>
                                                    </tr>                                                                                                
                                                    </table>
                                                </TD>
                                            </TR>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <DIV id="TEOrdinaria" name="TEOrdinaria" STYLE="width:100%;display:none;" >
                                        <table>
                                            <TR>
                                                <TD colspan="2" class="sub3titulo"><%=descriptor.getDescripcion(origen)%></TD>
                                            </TR>
                                            <TR>
                                                <TD style="width: 18%" class="etiqueta"> <%=descriptor.getDescripcion("tit_Org")%>:</TD>
                                                <TD class="columnP">
                                                    <table>
                                                    <tr>
                                                        <td>
                                                            <html:text styleClass="inputTexto"  property="cod_orgOrigen" size="8" />
                                                        </td>
                                                         <td>
                                                            <html:text styleClass="inputTexto" property="desc_orgOrigen" style="width:290" readonly="true" />
                                                            <A style="text-decoration:none;" id="anclaD" name="anchorOrganizacionOrigen" >
                                                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonOrganizacionOrigen"></span>
                                                            </A>
                                                        </td>
                                                    </tr>                                                                                            
                                                  </table>
                                                </TD>
                                            </TR>
                                            <TR>
                                                <TD class="etiqueta"><%=descriptor.getDescripcion("gEtiqUnidReg")%>:</TD>
                                                <TD class="columnP">
                                                    <table>
                                                    <tr>
                                                        <td>
                                                            <html:text styleClass="inputTexto"  property="cod_unidadeRexistroOrixe" size="8" />
                                                        </td>
                                                        <td>
                                                            <html:text styleClass="inputTexto" property="desc_unidadeRexistroOrixe" style="width:290" readonly="true" />
                                                            <A style="text-decoration:none;" id="anclaD" name="anchorUnidadeRexistroOrigen">
                                                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonUnidadeRexistroOrigen"></span>
                                                            </A>
                                                        </td>
                                                        </tr>                                                                                            
                                                    </table>
                                                </TD>
                                            </TR>
                                            <TR>
                                                <TD class="etiqueta"><%=descriptor.getDescripcion("res_EntRel")%>:</TD>
                                                <TD wclass="columnP">
                                                    <html:text  styleClass="inputTexto" size="4" maxlength="4" property="txtExp1Orixe" readonly="true" tabindex="-1" onfocus="this.select();" />
                                                    <html:hidden property="tipoRegistroOrigen"/>
                                                </TD>
                                            </TR>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                            <TR>
                                <TD class="sub3titulo"><%=descriptor.getDescripcion("gEtiqOtrasOpciones")%></TD>
                            </TR>
                            <TR>
                                <TD>
                                    <table style="padding-left: 3px; padding-top: 5px;" cellspacing="0px" cellpadding="0px">                                                    
                                        <tr>
                                            <td align="right">
                                                <input type="button" 
                                                       class="botonGeneral" value="<%=descriptor.getDescripcion("tit_Rel")%>" 
                                                       alt="<%=descriptor.getDescripcion("altRelaciones")%>" title="<%=descriptor.getDescripcion("altRelaciones")%>" 
                                                       name="cmdRelaciones" onClick="pulsarRelaciones();"/>
                                            </td>
                                            <td>&nbsp;</td>
                                            <td align="right">
                                                <input type="button" title="Incluir los temas y actuaciones relativos a la anotación"
                                                       class="botonGeneral" value="Temas"
                                                       name="cmdActuaciones" onClick="pulsarActuaciones();"/>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
            <% if ("soloConsulta".equals(modoInicio)) {%>
            <DIV id="capaBotones1"  name="capaBotones1" style="display:none" class="botoneraPrincipal">
                <INPUT type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbVolver")%>' name="cmdCancelar" onclick = "pulsarCancelar();">
            </DIV>
            <% } else {%>
            <DIV id="capaBotones1" name="capaBotones1" style="display:none" class="botoneraPrincipal">
                <!-- #291976: 
                    Antes el evento click de cmdRechazar,cmdAceptar,cmdAdjuntarExp,cmdAdjuntarExpInt y cmdIniciarExp se capturaba con html (onclick='accion')
                    Ahora el evento se captura con jQuery ($.click(accion)) para ejecutar la función de comprobacion del estado de catalogacion de los documentos
                    antes de lanzar la función concreta del boton
                -->
                <INPUT type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbRechazar")%>' name="cmdRechazar" >
                <INPUT type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbAceptar")%>' name="cmdAceptar" >
                <INPUT type= "button" class="botonLargo" value='<%=descriptor.getDescripcion("gbaDjuntarExp")%>' name="cmdAdjuntarExp" >
                <INPUT type= "button" class="botonMasLargo" value='<%=descriptor.getDescripcion("gbAdjuntarExpInt")%>' name="cmdAdjuntarExpInt" >
                <INPUT type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbIniciarExp")%>' name="cmdIniciarExp" >
                <INPUT type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelar" onclick = "pulsarCancelar();">
            </DIV>
            <DIV id="capaBotones3" name="capaBotones3" style="display:none" class="botoneraPrincipal">
                    <INPUT type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelarDeHistorico" onclick = "pulsarCancelarDeHistorico();">
            </DIV>
            <% }%>  
        </div>
    </html:form>

<script type="text/javascript" src="<html:rewrite page='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript">
        function pulsarDiliAnulacion() {
            if (document.getElementById('capaDiligenciasAnu').style.visibility == 'visible'){
                          domlay('capaDiligenciasAnulacion',0,0,0,null);
                  }
                  else {
                          botonActual = 'botonDiligenciasAnu';
                          posicionaCapa(50,25);

                          document.getElementById('capaDiligenciasAnu').style.position = "absolute";
                          document.getElementById('capaDiligenciasAnu').style.width    = 120;
                          document.getElementById('capaDiligenciasAnu').style.height   = 40;
                          var tabla = '<table width="100%"><tr><td align="right" >'
                          +'<span class="fa fa-window-close-o" name="botonCerrar" onClick="ocultarCapaDiligenciasAnu();"></span>'
                          +'</td></tr><tr><td bgcolor="#FFFFFF">'
                          +'<textarea class="textareaTexto" style="width:300px;height:150px;border:0px;" name="areaDil">'+dilAnotacion+'</textarea>'+'</td></tr></table>';
                          domlay('capaDiligenciasAnu',1,ppcX,ppcY,tabla);
                          
                  }
            }
        
        function ocultarCapaDiligenciasAnu() {
         domlay('capaDiligenciasAnu',0,0,0,null);
        }
            
            // Combo de roles
            var comboRol = new Combo("RolTercero");

            tp1_p1.setPrimerElementoFoco(document.forms[0].fechaAnotacion);
            tp1_p3.setPrimerElementoFoco(document.forms[0].cod_uniRegDestinoORD);
            
            var listaDocsAportados= new Array();
            var tabDocumentosAportados =new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaDocAportados'));
            tabDocumentosAportados.addColumna('80','left','<%=descriptor.getDescripcion("etiqTipo")%>');
            tabDocumentosAportados.addColumna('180','left','<%=descriptor.getDescripcion("gEtiqNombre")%>');
            tabDocumentosAportados.addColumna('180','left','<%=descriptor.getDescripcion("gEtiqOrgano")%>');
            tabDocumentosAportados.addColumna('100','left','<%=descriptor.getDescripcion("gEtiqFechaEntrega")%>');
            tabDocumentosAportados.displayCabecera=true;
            cont=0;
            <c:if test="${not empty MantAnotacionRegistroForm.registro.listaDocsAnteriores}">                        
                <c:forEach items="${MantAnotacionRegistroForm.registro.listaDocsAnteriores}" var="docAnterior" varStatus="count">
                        listaDocsAportados[cont++] = ['<c:out value="${docAnterior.tipoDocAnterior}"/>',
                           '<c:out value="${docAnterior.nombreDocAnterior}"/>',
                           '<c:out value="${docAnterior.organoDocAnterior}"/>',
                           '<c:out value="${docAnterior.fechaDocAnterior}"/>'];
                </c:forEach>
            </c:if>
            tabDocumentosAportados.lineas=listaDocsAportados;
            tabDocumentosAportados.displayTabla();
            
            tabDocs = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaDocs'));
                    
            tabDocs.addColumna('500','left',' Nombre');
            tabDocs.addColumna('100','center','Tipo');
            tabDocs.addColumna('100','center','Fecha');
            if(mostrarCatalogar) {
                tabDocs.addColumna('100','center','<%=descriptor.getDescripcion("etiqTipoDocumental")%>');
                tabDocs.addColumna('80','center','<%=descriptor.getDescripcion("etiqCatalogado")%>');
                tabDocs.addColumna('80','center','<%=descriptor.getDescripcion("etiqCompulsado")%>');
                tabDocs.addColumna('0','center',''); // Indicamos en esta columna la uor del documento. Añadiendo una columna de ancho 0 creamos una columna oculta
            }
            tabDocs.addColumna('0','center','');
            tabDocs.displayCabecera=true;


                function cargaTablaDocs()
                {
                    var cont=0;
            <c:if test="${not empty MantAnotacionRegistroForm.registro.documentos}">                        
                <c:forEach items="${MantAnotacionRegistroForm.registro.documentos}" var="documento" varStatus="count">
                            if(mostrarCatalogar) {
                                var catalogado;
                                var digit;
                                var tipoDocumental;
                                
                                if('<c:out value="${documento.extension}"/>' == '') {
                                    tipoDocumental = '';
                                    catalogado = '';
                                    digit = '';
                                } else {
                                    tipoDocumental = '<c:out value="${documento.tipoDocumental}"/>';
                                    catalogado = '<c:out value="${documento.catalogado}"/>';
                                    digit = '<c:out value="${documento.digitalizado}"/>';
                                }
                                
                    listaDocs[cont++] = ['<c:out value="${documento.nombre}"/>',
                       '<c:out value="${documento.extension}"/>',
                                   '<c:out value="${documento.fecha}"/>',tipoDocumental,
                                   catalogado,digit,'<c:out value="${documento.unidadOrg}"/>'];
                            } else {
                                listaDocs[cont++] = ['<c:out value="${documento.nombre}"/>',
                                   '<c:out value="${documento.extension}"/>',
                       '<c:out value="${documento.fecha}"/>'];
                            } 
                </c:forEach>
            </c:if>
                    tabDocs.lineas=listaDocs;
                    refresh("tabDocs");
                }

                function refresh(capa){
                    if(capa =="tabDiv")  tabDiv.displayTabla();
                    if(capa =="tabDocs")  tabDocs.displayTabla();

                }

                function comprobarCodigo2(lineas,codigo){
                    for (i=0; i < lineas.length; i++){
                        if(lineas[i][0]==(eval("document.forms[0]."+codigo+".value"))){
                            jsp_alerta('A','<%=descriptor.getDescripcion("msjCodigoRep")%>');
                                return false;
                            }
                        }
                        return true;
                    }


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

                    function checkKeysLocal(evento, tecla) {
                            var aux=null;
                            if(window.event)
                            aux = window.event;
                            else
                            aux = evento;

                            var tecla = 0;
                            if(aux.keyCode)
                            tecla = aux.keyCode;
                            else
                            tecla = aux.which;


                            <% if ("soloConsulta".equals(modoInicio)) {%>
                        if('Alt+V'==tecla)
                            pulsarCancelar();
                            <% } else {%>
                        if('Alt+R'==tecla)
                            pulsarRechazar();
                        else if ('Alt+A'==tecla)
                            pulsarAceptar();
                        else if ('Alt+D'==tecla)
                            pulsarAdjuntarExp();
                        else if ('Alt+I'==tecla) {
                            if(document.forms[0].txtExp1.value == "") {
                                pulsarIniciarExp();
                            }
                        }
                        else if ('Alt+C'==tecla)
                            pulsarCancelar();
                            <% } %>

                        if (tecla == 9){

                            comboRol.ocultar();

                            return false;
                        }
                        if (tecla == 1){
                            if (comboRol.base.style.visibility == "visible" && isClickOutCombo(comboRol,coordx,coordy)) setTimeout('comboRol.ocultar()',20);

                        }
                        keyDel(aux);
                    }
                    
        </script>
    </BODY>
</html:html>
