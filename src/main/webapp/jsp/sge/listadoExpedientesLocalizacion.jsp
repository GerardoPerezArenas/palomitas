<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.agora.business.sge.ConsultaExpedientesValueObject" %>
<%@ page import="es.altia.agora.interfaces.user.web.sge.ConsultaExpedientesForm" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.lang.Integer" %>
<%@ page import="es.altia.common.service.config.Config" %>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<html:html>
<head>
<jsp:include page="/jsp/sge/tpls/app-constants.jsp"/>
<TITLE>::: EXPEDIENTES - LISTADO RELACION EXPEDIENTES :::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<%
    UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int idioma = 0;
    int apl = 0;
    String funcion = "";

    if ((session.getAttribute("usuario") != null)) {
        usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
        idioma = usuarioVO.getIdioma();
        apl = usuarioVO.getAppCod();
    }
    String idSesion = session.getId();
    String porCampoSup = request.getParameter("porCampoSup");
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String mostrarLocalizacion = m_Config.getString("ListaExpedientes.localizacion");

%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
             type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor" property="idi_cod" value="<%= idioma%>"/>
<jsp:setProperty name="descriptor" property="apl_cod" value="<%= apl %>"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<SCRIPT type="text/javascript">
var tabExpedientes;

var expedientesSeleccionados = new Array();
var numExpediente = '<%=(String)request.getAttribute("numExpedienteTratado")%>'
var localizacion = '<%=(String)request.getAttribute("localizacionExpediente")%>';
var lista = new Array();
var listaOriginal = new Array();
var listaOrd = new Array();
var listaP = new Array();
var listaSel = new Array();
var fila;
var ultimo = false;
var pagin;
var asunto;
var apellido1;
var estado;
var columnaOrden=0;
var tipoOrdenacion=true;
var listaCampos=new Array();
//vector que me indica que campos estan activos en las columnas para saber por cual ordenar.
var listaOrden=new Array();
var listaOrdenVisible=new Array();
var rowIdSelec=0;
var modoConsultaExpRel;

<% /* Recuperar el vector de procedimientos de la sesion. */
   int numRelacionExpedientes = (Integer)session.getAttribute("numExpedientesRelacionadosConLocalizacion");
   boolean encontrada=false;

   String desdeInformesGestion = (String) session.getAttribute("desdeInformesGestion");
   session.removeValue("desdeInformesGestion");

   if(desdeInformesGestion != null && desdeInformesGestion.equals("si")) {
     desdeInformesGestion = "si";
   } else desdeInformesGestion = "no";
   String todos = (String) session.getAttribute("todos");
   if(todos == null) {
     todos = "";
   }
   String desdeConsulta = request.getParameter("desdeConsulta");
   if (desdeConsulta==null || desdeConsulta.equals("null") || desdeConsulta.equals("")){
        desdeConsulta="si";
   }
   String informesDireccion = request.getParameter("informesDireccion");
   if (informesDireccion == null) informesDireccion = "false";

   boolean enInformesGestion = false;
%>

/* Para navegacion */
var lineasPagina = 10;

var paginaActual = 1;
var enInformesGestion = <%=enInformesGestion%>;
var inicio = 0;
var fin = 0;
var numeroPaginas;
// Nº de expedientes que cumplen las condiciones de búsqueda
var numRelacionExpedientes = <%=numRelacionExpedientes%>;

function cargarInicio() {
    <c:if test="${not empty sessionScope.errorConsulta}">
        jsp_alerta('A', "<fmt:message key="${sessionScope.errorConsulta}"/>");
        document.forms[0].cmdImprimir.disabled = true;
    </c:if>

    tabExpedientes = new Tabla(false,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>',
            '<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>',
            '<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', 
            '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>',
            '<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',
            document.getElementById("tabla"));

    tabExpedientes.addColumna('5','center','');
    tabExpedientes.addColumna('25','center','<%= descriptor.getDescripcion("etiqNumExpediente")%>');
    tabExpedientes.addColumna('25','center','<%= descriptor.getDescripcion("gEtiq_interesado")%>');
    tabExpedientes.addColumna('25','center','<%= descriptor.getDescripcion("gEtiq_Asunto")%>');
    tabExpedientes.addColumna('25','center','<%= descriptor.getDescripcion("gEtiq_Local")%>');
    tabExpedientes.addColumna('15','center','<%= descriptor.getDescripcion("gEtiq_fecIni")%>');
    tabExpedientes.addColumna('15','center','<%= descriptor.getDescripcion("gEtiq_fecFin")%>');
    tabExpedientes.addColumna('15','center','<%= descriptor.getDescripcion("etiq_estado")%>');
    tabExpedientes.addColumna('20','center','<%= descriptor.getDescripcion("etiqUsuInicio")%>');
    tabExpedientes.addColumna('15','center','<%= descriptor.getDescripcion("etiqUnidadInicio")%>');
    tabExpedientes.displayCabecera=true;

    document.forms[0].desdeConsulta.value = "<%=desdeConsulta%>";
    document.forms[0].porCampoSup.value = "<%=porCampoSup%>";
    window.focus();
    listaSel = lista;
    
    document.forms[0].expRelacionado.value = "<bean:write name="ConsultaExpedientesForm" property="expRelacionado"/>";
    if (document.forms[0].expRelacionado.value == "si") {
        document.forms[0].codMunExpIni.value = "<bean:write name="ConsultaExpedientesForm" property="codMunicipioIni"/>";
        document.forms[0].ejercicioExpIni.value = "<bean:write name="ConsultaExpedientesForm" property="ejercicioIni"/>";
        document.forms[0].numeroExpIni.value = "<bean:write name="ConsultaExpedientesForm" property="numeroExpedienteIni"/>";
    }
    document.forms[0].deAdjuntar.value = "<bean:write name="ConsultaExpedientesForm" property="deAdjuntar"/>";
    //Los necesito para hacer la recarga con criterios de busqueda
     document.forms[0].codProcedimiento.value ="<bean:write name="ConsultaExpedientesForm" property="codigoProcedimiento"/>";
     document.forms[0].numeroExpedienteBus.value ="<bean:write name="ConsultaExpedientesForm" property="numeroExpediente"/>";
     modoConsultaExpRel="<bean:write name="ConsultaExpedientesForm" property="modoConsultaExpRel"/>";

    cargaPagina(1);
}

function cargaPagina(numeroPagina) {
    lista = new Array();
    listaOriginal = new Array();
    pleaseWait('on');
    document.forms[0].paginaListado.value = numeroPagina;
    document.forms[0].numLineasPaginaListado.value = lineasPagina;
    //document.forms[0].opcion.value = "cargar_pagina";
    document.forms[0].opcion.value ="cargarPaginaExpedientesRelacionadosConLocalizacion";
    document.forms[0].target = "oculto";
    document.forms[0].action = "<c:url value='/sge/ConsultaExpedientes.do'/>?loc=" + localizacion + "&num=" + numExpediente;    
    document.forms[0].submit();
}

// Cambia pq ahora es todo el vector.
function inicializaLista(numeroPagina,numTotExpedientes) { 
    if(numTotExpedientes!=null && numTotExpedientes>0 && enInformesGestion)
        numRelacionExpedientes = numTotExpedientes;
    var j = 0;
    paginaActual = Number(numeroPagina);
    listaP = new Array();
    listaO=new Array();
    inicio = 0;
    fin = lineasPagina;
    listaP = listaSel;
    listaO=listaOrd;
    tabExpedientes.setLineas(listaSel);
    tabExpedientes.displayTabla();
    domlay('enlace',1,0,0,enlaces());
}

function enlaces() {
    var numeroPaginas = Math.ceil(numRelacionExpedientes /lineasPagina);
    return enlacesPaginacion('<%=descriptor.getDescripcion("mosPagDePags")%>',
                '<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>',
                paginaActual,numeroPaginas,'cargaPagina');
}

function mostrarAvisoRelacion(relacion,indice) {
    var opcionExp = jsp_alerta('B','<br> <%=descriptor.getDescripcion("msjExpRelAbierta")%> <br> <%=descriptor.getDescripcion("etiq_numRel")%> : '+ relacion
                    + '<br> <br><%=descriptor.getDescripcion("msjAbrirExpModConsul")%> <br><br>' );
    if (opcionExp == 1){

        document.forms[0].codMunicipio.value = listaOriginal[indice][0];
        document.forms[0].codProcedimiento.value = listaOriginal[indice][1];
        document.forms[0].ejercicio.value = listaOriginal[indice][2];
        document.forms[0].numero.value = listaOriginal[indice][3];
        document.forms[0].modoConsulta.value = "si";
        document.forms[0].opcionExpRel.value = "1";
        document.forms[0].opcion.value = "cargar";
        document.forms[0].target = "mainFrame";
        document.forms[0].action = "<c:url value='/sge/FichaExpediente.do'/>";
        document.forms[0].submit();
    }
}


function grabarExpRel(respOpcion) {
    if (respOpcion == "grabarExpRel") {
        document.forms[0].opcion.value = "expRel";
        document.forms[0].target = "mainFrame";
        document.forms[0].action = "<c:url value='/sge/ConsultaExpedientes.do'/>";
        document.forms[0].submit();
    } else {
        jsp_alerta("A", "<%=descriptor.getDescripcion("altaNoHecha")%>");
    }
}


function salir(){    
   var retorno = new Array();   
   self.parent.opener.retornoXanelaAuxiliar(retorno);
}

function pulsarSalirConsultar() {
    self.parent.opener.retornoXanelaAuxiliar();
    
<% if(desdeInformesGestion.equals("si")) { %>

    document.forms[0].todos.value = '<%= todos %>';
    document.forms[0].opcion.value = "volverCargar";
    document.forms[0].target = "mainFrame";
    document.forms[0].action = "<c:url value='/informes/Informes.do'/>";
    document.forms[0].submit();

<% } else { %>
    if ((document.forms[0].expRelacionado.value == "si")) {
        document.forms[0].codMunicipio.value = document.forms[0].codMunExpIni.value;
        document.forms[0].ejercicio.value = document.forms[0].ejercicioExpIni.value;
        document.forms[0].numeroExpediente.value = document.forms[0].numeroExpIni.value;
    }
    document.forms[0].opcion.value = "inicio";
    document.forms[0].target = "mainFrame";
    if (document.forms[0].porCampoSup.value == "si") {
        document.forms[0].action = "<c:url value='/sge/ConsultaExpedientePorCampoSup.do'/>";
    } else {
        document.forms[0].action = "<c:url value='/sge/ConsultaExpedientes.do'/>";
    }
    document.forms[0].submit();
<% } %>
}

/* IMPRIMIR consulta */

function pulsarImprimir() {
    pleaseWait('on');
    document.forms[0].opcion.value = "imprimir";
    document.forms[0].target = "oculto";
    document.forms[0].action = "<c:url value='/sge/ConsultaExpedientes.do'/>";
    document.forms[0].submit();
}

function pulsarAnadir() {
   // Se devuelve el array con los números de los expedientes seleccionados    
    expedientesSeleccionados[expedientesSeleccionados.length] = "modificarExpedientesRelacionados" ;    
    self.parent.opener.retornoXanelaAuxiliar(expedientesSeleccionados);
}

function abrirInforme(nombre) {
    pleaseWait('off');
    var source = "<c:url value='/jsp/verPdf.jsp?opcion=null&nombre='/>" + nombre;
    var nombreVentana = (top.name == 'ventana' ? 'ventana2' : 'ventana');
    ventanaInforme = window.open("<%=request.getContextPath()%>/jsp/mainVentana.jsp;jsessionid=<%=idSesion%>?source=" + source, nombreVentana, 'width=800px,height=550px,status=' + '<%=statusBar%>' + ',toolbar=no');
    ventanaInforme.focus();
}

/* Fin IMPRIMIR consulta */

function checkKeysLocal(evento,tecla) {
        var teclaAuxiliar="";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else
        teclaAuxiliar = evento.which;
    
    if ('Alt+C' == tecla) pulsarSalirConsultar();

    if (teclaAuxiliar == 38 || teclaAuxiliar == 40) {
        upDownTable(tabExpedientes, lista,teclaAuxiliar);
    }
    if (teclaAuxiliar == 13) {
        if ((tabExpedientes.selectedIndex > -1) && (!tabExpedientes.ultimoTable)) {
            callFromTableTo(tabExpedientes.selectedIndex, tabExpedientes.id);
        }
    }
    keyDel(evento);
}

    function marcarExpedientesSeleccionados(objeto){
        if(objeto!=null && objeto.checked==true){
            expedientesSeleccionados.push(objeto.value);
        }

        if(objeto!=null && objeto.checked==false){            
            // Se ha deshabilitado el check => Se elimina el expediente del array de expedientes seleccionados
            var aux = new Array();
           for(i=0;i<expedientesSeleccionados.length;i++){               
               if(expedientesSeleccionados[i]!=objeto.value){
                    aux.push(expedientesSeleccionados[i]);
               }//if
           }//for

           expedientesSeleccionados = new Array();
           expedientesSeleccionados = aux;
        }//if        
    }// marcarExpedientesSeleccionados
</SCRIPT>
</head>
<BODY class="bandaBody" onload="cargarInicio();">
<jsp:include page="/jsp/hidepage.jsp" flush="true">
    <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
</jsp:include>
<html:form action="/sge/ConsultaExpedientes.do" target="_self">
<html:hidden property="opcion" value=""/>
<html:hidden property="paginaListado" value="1"/>
<html:hidden property="numLineasPaginaListado" value="10"/>
<html:hidden property="numeroExpedienteBus" value=""/>
<input type="hidden" name="codMunicipio">
<input type="hidden" name="codProcedimiento">
<input type="hidden" name="numeroExpediente">
<input type="hidden" name="ejercicio">
<input type="hidden" name="numero">
<input type="hidden" name="codMunExpIni" value="">
<input type="hidden" name="ejercicioExpIni" value="">
<input type="hidden" name="numeroExpIni" value="">
<input type="hidden" name="modoConsulta">
<input type="hidden" name="expRelacionado" value="">
<input type="hidden" name="deAdjuntar" value="">
<input type="hidden" name="desdeInformesGestion" value="no">
<input type="hidden" name="todos" value="">
<input type="hidden" name="idioma" value="es">
<input type="hidden" name="desdeConsulta">
<input type="hidden" name="insertarExpRel" value="no">
<input type="hidden" name="porCampoSup">
<input type="hidden" name="opcionExpRel">
<html:hidden property="columna" value=""/>
<html:hidden property="tipoOrden" value=""/>

<div class="txttitblanco"><%=descriptor.getDescripcion("inf_Expedientes")%></div>
<div class="contenidoPantalla">
    <table style="width: 100%">
        <tr>
            <td id="tabla"></td>
        </tr>
        <tr>
            <td id="enlace" class="dataTables_wrapper"></td>
        </tr>
    </table>
    <DIV id="capaBotones1" name="capaBotones1" class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAnadir")%>" name="cmdAnadir" onClick="pulsarAnadir();return false;">                                
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdSalir" onClick="salir();">
    </DIV>
</div>
</html:form>
</BODY>
</html:html>
