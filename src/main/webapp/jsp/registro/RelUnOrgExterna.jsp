<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.agora.technical.Fecha" %>
<%@ page import="es.altia.common.service.config.Config" %>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Vector" %>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager" %>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO" %>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.IdiomasManager" %>
<%@ page import="es.altia.agora.business.util.GeneralValueObject" %>



<HTML>
<head>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/uor.js"></script>
<%
    String tipo = request.getParameter("tipo");
    char t = tipo.charAt(0);
    int idioma = 1;
            String css = "";
    UsuarioValueObject usuario = null;
    if (session != null) {
        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    css = usuario.getCss();

                }
    }
    // Fecha actual del servidor.
    Fecha f = new Fecha();
    Date dateServidor = new Date();
    String fechaServidor = f.obtenerString(dateServidor);
    String idSesion = session.getId();
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");

    // 10/05/2006
    // parte para que poniendo un código visible en el input se rellene la descripción
    UsuarioValueObject usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
    String[] params = usuarioVO.getParamsCon();
    Vector listaUORDTOs = UORsManager.getInstance().getListaUORs(false,params);

    // Carga de la lista de idiomas
    Vector listaIdiomas = IdiomasManager.getInstance().getListaIdiomas(params);

    String userAgent = request.getHeader("user-agent");
%>

<script type="text/javascript">
    //=== UORs
    var uors = new Array();
    var uorcods = new Array();

 <% for(int j=0; j<listaUORDTOs.size(); j++) {
        UORDTO dto = (UORDTO)listaUORDTOs.get(j); %>
        // array con los objetos tipo uor mapeados por el array de arriba
        uors[<%=j%>] = new Uor<%=dto.toJavascriptArgs()%>;
        // array con los códigos visibles
        uorcods[<%=j%>] = '<%=dto.getUor_cod_vis()%>';
 <% } %>

 // Idiomas
    var cod_idiomas = new Array();
    var desc_idiomas = new Array();
    var indice_idioma_usuario = 0;
 <% for(int j=0; j<listaIdiomas.size(); j++) {
        GeneralValueObject idi = (GeneralValueObject)listaIdiomas.get(j); %>
        cod_idiomas[<%=j%>] = '<%=idi.getAtributo("codigo")%>';
        desc_idiomas[<%=j%>] = '<%=idi.getAtributo("descripcion")%>';
        if (cod_idiomas[<%=j%>] == <%=idioma%>) indice_idioma_usuario = <%=j%>;
 <% } %>
    // === fin seccion UORs

    var EsquemaGenerico = "<%=es.altia.agora.business.util.GlobalNames.ESQUEMA_GENERICO%>";
    var operadorConsulta = '|&:<>!=';
    var informesDireccion = false;
    var ventanaPadre = null;
    if (window.dialogArguments != undefined) {
        if (window.dialogArguments['informesDireccion']) informesDireccion = true;
        ventanaPadre = window.dialogArguments['ventanaPadre'];
    }

</script>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
             type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp"/>
<jsp:useBean id="desc" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
             type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="desc" property="idi_cod" value="<%=idioma%>"/>
<jsp:setProperty name="desc" property="apl_cod" value="1"/>
<TITLE>::: REGISTRO DE ENTRADA - Relación Unidades Otras Administraciones ::: </TITLE>
        <LINK REL="StyleSheet" MEDIA="screen" TYPE="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<SCRIPT type="text/javascript">
function mostrarCalDesde(evento) {
    if(window.event) evento = window.event;
    if (document.getElementById("calDesde").className.indexOf("fa-calendar") != -1)
        showCalendar('forms[0]', 'fechaDesde', null, null, null, '', 'calDesde', '',null,null,null,null,null,null,null,null,evento);
}

function mostrarCalHasta(evento) {
    if(window.event) evento = window.event;
    if (document.getElementById("calHasta").className.indexOf("fa-calendar") != -1)
        showCalendar('forms[0]', 'fechaHasta', null, null, null, '', 'calHasta', '',null,null,null,null,null,null,null,null,evento);
}
var ventanaInforme;
function pulsarSalir() {
    if (informesDireccion) {
        top.close();
    } else {
    if (ventanaInforme) ventanaInforme.close();
        window.location = "<%=request.getContextPath()%>/MantAnotacionRegistro.do?opcion=<%=tipo%>";
    }
}

function pulsarImprimir() {
     document.forms[0].idioma.value = document.forms[0].codIdioma.value;
     if (document.forms[0].todas.checked){
         document.forms[0].txtCodigoUnidad.value = "0";
         //document.forms[0].txtCodigoOrganizacion.value = document.forms[0].cod_orgDestino.value;
         document.forms[0].txtCodigoOrganizacion.value = "0";
     }
     else{
         document.forms[0].txtCodigoUnidad.value = document.forms[0].cod_uniRegDestino.value;
         document.forms[0].txtCodigoOrganizacion.value = document.forms[0].cod_orgDestino.value;
     }
    if (validarFormulario()) {        
        if (comprobarFecha(document.forms[0].fechaDesde) && comprobarFecha(document.forms[0].fechaHasta)) {
            if (comprobarCampos() == 1) {
                var anoD = document.forms[0].fechaDesde.value.substring(6, 10);
                var anoH = document.forms[0].fechaHasta.value.substring(6, 10);
                if (anoH == anoD) {
                    var numDesde = document.forms[0].regDesde.value;
                    var numHasta = document.forms[0].regHasta.value;

                    if ((numDesde != '') && (numHasta != '')) {
                        var resultadoConfirm = jsp_alerta("C", '<%=desc.getDescripcion("msjImpLibro")%>  ' + anoD + ' : <br><%=desc.getDescripcion("gEtiq_desde")%> ' +
                                                               numDesde + '<br><%=desc.getDescripcion("gEtiq_hasta")%> ' +
                                                               numHasta);
                        if (resultadoConfirm == "1") {
                            pleaseWait('on');
                            formulario.fechaInicio.value = document.forms[0].fechaDesde.value.substring(0, 2) + "/" +
                                                           document.forms[0].fechaDesde.value.substring(3, 5) + "/" +
                                                           document.forms[0].fechaDesde.value.substring(6, 10);
                            formulario.fechaFin.value = document.forms[0].fechaHasta.value.substring(0, 2) + "/" +
                                                        document.forms[0].fechaHasta.value.substring(3, 5) + "/" +
                                                        document.forms[0].fechaHasta.value.substring(6, 10);
                            formulario.opcion.value = "relacionUnidadesOrganicasExternas";
                            formulario.target = "oculto";
                            formulario.action = "<%=request.getContextPath()%>/InformesRegistro.do";
                            formulario.submit();
                        }
                        else return;

                    } else {

                        var resultadoConfirm = jsp_alerta("C", '<%=desc.getDescripcion("msjImp")%>: <br><%=desc.getDescripcion("gEtiq_desde")%> ' +
                                                               document.forms[0].fechaDesde.value.substring(0, 2) + "/" +
                                                               document.forms[0].fechaDesde.value.substring(3, 5) + "/" +
                                                               document.forms[0].fechaDesde.value.substring(6, 10) + '<br><%=desc.getDescripcion("gEtiq_hasta")%> ' +
                                                               document.forms[0].fechaHasta.value.substring(0, 2) + "/" +
                                                               document.forms[0].fechaHasta.value.substring(3, 5) + "/" +
                                                               document.forms[0].fechaHasta.value.substring(6, 10));
                        if (resultadoConfirm == "1") {
                            pleaseWait('on');
                            formulario.fechaInicio.value = document.forms[0].fechaDesde.value.substring(0, 2) + "/" +
                                                           document.forms[0].fechaDesde.value.substring(3, 5) + "/" +
                                                           document.forms[0].fechaDesde.value.substring(6, 10);
                            formulario.fechaFin.value = document.forms[0].fechaHasta.value.substring(0, 2) + "/" +
                                                        document.forms[0].fechaHasta.value.substring(3, 5) + "/" +
                                                        document.forms[0].fechaHasta.value.substring(6, 10);
                            formulario.opcion.value = "relacionUnidadesOrganicasExternas";
                            formulario.target = "oculto";
                            formulario.action = "<%=request.getContextPath()%>/InformesRegistro.do";
                            formulario.submit();
                        } else return;
                        }
                    } else jsp_alerta("A", '<%=desc.getDescripcion("msjAnDistinto")%>');
                }
            }
        }
    }
    
    function abrirInforme(nombre) {
        // A otra página que contiene el fichero PDF.

        if (nombre == "NO EXISTE") {
            jsp_alerta('A', '<%=desc.getDescripcion("msjNoDatos")%>');
        } else if (nombre != '') {
            if (informesDireccion) {
                ventanaPadre.verInforme(nombre);
            } else {
                var sourc = "<%=request.getContextPath()%>/jsp/verPdf.jsp?opcion=null&nombre=" + nombre;
                ventanaInforme = window.open("<%=request.getContextPath()%>/jsp/mainVentana.jsp;jsessionid=<%=idSesion%>?source=" + sourc, 'ventanaInforme', 'width=1000px,height=750px,status=' + '<%=statusBar%>' + ',toolbar=no,resizable=yes');
                ventanaInforme.focus();
            }
        } else {
            jsp_alerta('A', '<%=desc.getDescripcion("msjNoPDF")%>');
        }
    }

    function comprobarCampos() {
        var r = 1;

        if (document.forms[0].fechaDesde.value == '') {
            var r = jsp_alerta("A", '<%=desc.getDescripcion("msjAbrirCond10")%>');
            //document.formulario.txtDiaE.select();
            document.forms[0].fechaDesde.select();
            r = 0;
        } else {
            if (document.forms[0].fechaHasta.value == '') {
                var fecha = '<%=fechaServidor%>';
                document.forms[0].fechaHasta.value = fecha;
            }
            var fd = new Date(document.forms[0].fechaDesde.value.substring(6, 10), eval(document.forms[0].fechaDesde.value.substring(3, 5) - 1), document.forms[0].fechaDesde.value.substring(0, 2));
            var fh = new Date(document.forms[0].fechaHasta.value.substring(6, 10), eval(document.forms[0].fechaHasta.value.substring(3, 5) - 1), document.forms[0].fechaHasta.value.substring(0, 2));
            if (comparaFechas(fd, fh) == 1) {
                jsp_alerta("A", '<%=desc.getDescripcion("msjFechasNV")%>');
                r = 0;
            }
        }
        return r;
    }


    function inicializar() {
        document.all.tabla1.style.marginTop = 0;

        var fecha = '<%=fechaServidor%>';
        document.forms[0].fechaDesde.value = fecha;
        document.forms[0].fechaHasta.value = fecha;
        document.forms[0].fechaDesde.focus();

        habilitarImagenCal("calDesde", true);
        habilitarImagenCal("calHasta", true);
    }



    function mostrarListaOrganizacionDestino(){
        var condiciones = new Array();
       // function muestraListaTabla(colCod, colDesc,nomTabla, arrayWhere, inputCod, inputDesc, boton, ancho, target1, target2, condicionCompleja)
       // muestraListaTabla('UOR_COD_VIS', 'UOR_NOM', 'A_UOR', condiciones, 'cod_visible_uor', 'desc_uniRegDestinoORD', 'botonUnidadeRexistroORD', '100', null,null,condicionCompleja);
	muestraListaTabla('ORGEX_COD','ORGEX_DES',EsquemaGenerico+'A_ORGEX A_ORGEX',condiciones,'cod_orgDestino','desc_orgDestino','botonOrganizacionDestino','100',null, null,null);
    }


    /*****************  Funciones COMBO ORGANIZACION Y UNIDAD ORGANICA  ****************/
    function onchangeCod_orgDestino(){
        actualizarValDiv('cod_orgDestino','desc_orgDestino');
        divSegundoPlano=true;
        mostrarListaOrganizacionDestino();
    }

     function onClickDesc_orgDestino(){
	if ( contieneOperadoresConsulta(document.forms[0].cod_orgDestino,operadorConsulta))
		document.forms[0].cod_orgDestino.value='';
	divSegundoPlano=false;
	mostrarListaOrganizacionDestino();
    }


    function onchangeCod_uniRegDestino(){
        if (!contieneOperadoresConsulta(document.forms[0].cod_uniRegDestino,operadorConsulta) ) {
            actualizarValDiv('cod_uniRegDestino','desc_uniRegDestino');
            divSegundoPlano=true;
            mostrarListaUnidRegistro();
        } else {
            document.forms[0].desc_uniRegDestino.value='';

        }
    }

     function onClickDesc_uniRegDestino(){
         if (contieneOperadoresConsulta(document.forms[0].cod_uniRegDestino,operadorConsulta) )
             document.forms[0].cod_uniRegDestino.value='';
         divSegundoPlano=false;
         mostrarListaUnidRegistro();
    }


    function contieneOperadoresConsulta(campo,cjtoOp){
        var contiene=false;
        if(campo != null) {
            var v = campo.value;
            for (i = 0; i < v.length; i++){
                var c = v.charAt(i);
                if (cjtoOp.indexOf(c) != -1)  contiene=true;
            }
        }
        return contiene;
    }

    function onblurOrganizacionOtroReg(cod, des){       
        if(camposDistintosDiv(cod, des)) {
            with (document.forms[0]) {
                cod_uniRegDestino.value='';
                desc_uniRegDestino.value='';
                document.forms[0].todas.checked = false;
            }
        }
    }


    // Funcion Mostrar LISTA UNIDADES REG DESTINO
    function mostrarListaUnidRegistro(){
        if (Trim(document.forms[0].cod_orgDestino.value) != '') {
            var condiciones = new Array();            
            condiciones[0]='UOREX_ORG'+'§¥';
            condiciones[1]=document.forms[0].cod_orgDestino.value ;
           muestraListaTabla('UOREX_COD','UOREX_NOM',EsquemaGenerico + 'A_UOREX A_UOREX',condiciones,'cod_uniRegDestino','desc_uniRegDestino','botonUnidadeRexistro','100',null,null,null);
        } else {
            document.forms[0].cod_uniRegDestino.value=''; // Si viene del onchange permaneceria
        }
    }


    /**********  FIN Funciones COMBO ORGANIZACION Y UNIDAD ORGANICA  ****************/
   


    function comprobarFecha(inputFecha) {
        if (Trim(inputFecha.value) != '') {
            if (!ValidarFechaConFormato(document.forms[0], inputFecha)) {
                jsp_alerta("A", '<%=desc.getDescripcion("fechaNoVal")%>');
                inputFecha.focus();
                return false;
            }
        }
        return true;
    }

    function pulsarTodas() {
        if (document.forms[0].todas.checked) {
            document.forms[0].txtCodigoUnidad.value = "0";
            //document.forms[0].txtCodigoOrganizacion.value = document.forms[0].cod_orgDestino.value;
            document.forms[0].txtCodigoOrganizacion.value = "0";

            document.forms[0].cod_uniRegDestino.value = "TODAS";
            document.forms[0].desc_uniRegDestino.value = "TODAS";
            document.forms[0].cod_orgDestino.value = "TODAS";
            document.forms[0].desc_orgDestino.value = "TODAS";
            
        } else {
            document.forms[0].cod_uniRegDestino.value = '';
            document.forms[0].desc_uniRegDestino.value = '';
            document.forms[0].cod_orgDestino.value = '';
            document.forms[0].desc_orgDestino.value = '';

            document.forms[0].txtCodigoUnidad.value = '';
            document.forms[0].txtCodigoOrganizacion.value = '';
        }
    }
    
    
</SCRIPT>
</HEAD>

<body class="bandaBody" onload="javascript:{ pleaseWait('off');  }">    
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>


<FORM name="formulario" METHOD=POST target="_self">
<input type="hidden" name="txtCodigoUnidad"/>
<input type="hidden" name="txtCodigoOrganizacion"/>
<input type="hidden" name="tipo_select"/>
<input type="hidden" name="col_cod"/>
<input type="hidden" name="col_desc"/>
<input type="hidden" name="nom_tabla"/>
<input type="hidden" name="input_cod"/>
<input type="hidden" name="input_desc"/>
<input type="hidden" name="whereComplejo"/>
<input type="hidden" name="column_valor_where"/>
<input type="hidden" name="pdfFile" value="informeUnidades">
<input type="hidden" name="xslFile" value="defecto">
<input type="hidden" name="fechaInicio" value="">
<input type="hidden" name="fechaFin" value="">
<input type="hidden" name="opcion" value="">
<input type="hidden" name="tipo" value="<%=t%>">


    <% if ('S' == t) { %>
        <div id="titulo"  class="txttitblancoder"><%=desc.getDescripcion("infReg_TitPrin" + t)%> <%=desc.getDescripcion("infReg_rbUniTramExternaS")%>&nbsp;&nbsp;&nbsp;</div>
    <% } else { %>
        <div id="titulo"  class="txttitblanco"><%=desc.getDescripcion("infReg_TitPrin" + t)%> <%=desc.getDescripcion("infReg_rbUniTramExternaE")%>&nbsp;&nbsp;&nbsp;</div>
    <% } %>
                     
    <div class="contenidoPantalla">
                               
                                            <TABLE id="tabla1" width="100%" cellpadding="2" cellspacing="2">
                                                <TR> <!-- Periodo de Consulta-->
                                                    <TD class="sub3titulo"><%=desc.getDescripcion("InfUniPer")%></TD>
                                                </TR>

                                                <TR>
                                                    <TD>
                                                        <TABLE width="100%" height="100%" cellspacing="0px" cellpadding="0px">
                                                            <TR>
                                                                <TD style="width: 30%" class="etiqueta"><%=desc.getDescripcion("gEtiqFechaDesde")%>:</TD>
                                                                <TD width="60%" class="columnP">
                                                                    <INPUT TYPE="text" id="obligatorio" class="inputTxtFechaObligatorio" size="12" maxlength="10"
                                                                           name="fechaDesde"
                                                                           onkeyup="javascript:return SoloCaracteresFecha(this);"
                                                                           onblur="javascript:return comprobarFecha(this);"
                                                                           onfocus="this.select();">
                                                                    <A href="javascript:calClick(event);return false;" onClick="mostrarCalDesde(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;">
                <span class="fa fa-calendar" aria-hidden="true" name="calDesde" id="calDesde" alt="<%=descriptor.getDescripcion("altFecha")%>" title="<%=descriptor.getDescripcion("altFecha")%>"
                     ></span>
            </A>
                                                                </TD>
                                                            </tr>
                                                            <tr>
                                                                <td width="30%" class="etiqueta"><%=desc.getDescripcion("gEtiqFechaHasta")%>:</td>
                                                                <td width="60%" class="columnP">
                                                                    <INPUT TYPE="text" class="inputTxtFecha" size="12" maxlength="10" name="fechaHasta"
                                                                           onkeyup="javascript:return SoloCaracteresFecha(this);"
                                                                           onblur="javascript:return comprobarFecha(this);"
                                                                           onfocus="this.select();">
                                                                    <A href="javascript:calClick(event);return false;" onClick="mostrarCalHasta(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;">
                                                                         <span class="fa fa-calendar" aria-hidden="true" name="calHasta" id="calHasta" alt="<%=descriptor.getDescripcion("altFecha")%>" title="<%=descriptor.getDescripcion("altFecha")%>" ></span>
                    
                                                                    </A>
                                                                </td>
                                                            </tr>

                                                            <tr>
                                                                <TD width="30%" class="etiqueta"><%=desc.getDescripcion("rer_numero")%>&nbsp;<%=desc.getDescripcion("gEtiq_desde")%>:
                                                                </TD>
                                                                <TD><INPUT TYPE="text" class="inputTexto"
                                                                           NAME="regDesde" size=4 value=""
                                                                           onkeyup="javascript:return SoloDigitosNumericos(this);"></TD>
                                                            </tr>
                                                            <tr>
                                                                <TD width="30%" class="etiqueta"><%=desc.getDescripcion("rer_numero")%>&nbsp;<%=desc.getDescripcion("gEtiq_hasta")%>:
                                                                </TD>
                                                                <TD><INPUT TYPE="text" class="inputTexto"
                                                                           NAME="regHasta" size=4 value=""
                                                                           onkeyup="javascript:return SoloDigitosNumericos(this);"></TD>
                                                            </tr>

                                                        </TABLE>
                                                        <!--Fin de Periodo de Consulta-->

                                                <TR> <!-- UNIDADES TRAMITADORAS-->
                                                    <TD width="100%" class="sub3titulo">
                                                        <c:if test="${param.tipo == 'E'}">
                                                            <%=desc.getDescripcion("infReg_UniDestinoExterna")%>
                                                        </c:if>
                                                        <c:if test="${param.tipo == 'Salida'}">
                                                            <%=desc.getDescripcion("infReg_UniOrigenExterna")%>
                                                        </c:if>
                                                    </TD>
                                                </TR>

                                                <TR>
                                                    <TD>
                                                        <TABLE width="100%" height="100%" cellspacing="0px" cellpadding="0px">                                                           
                                                            <TR>
                                                                <TD width="30%" class="etiqueta"><%=desc.getDescripcion("tit_Org")%>:</TD>
                                                                <td width="70%" class="columnP">
                                                                    <input class="inputTextoObligatorio" type="text" name="cod_orgDestino" size="8"
                                                                           onkeypress="javascript:PasaAMayusculas(event);"
                                                                           onfocus="javascript:this.select();"
                                                                           onchange="javascript:{onchangeCod_orgDestino();}"
                                                                           onblur="javascript:{onblurOrganizacionOtroReg('cod_orgDestino','desc_orgDestino');}"/>

                                                                    <input id="obligatorio" class="inputTextoObligatorio" type="text" name="desc_orgDestino"  style="width:300;height:17"
                                                                           onchange="javascript:{onClickDesc_orgDestino();}" 
                                                                           onclick="javascript:{onClickDesc_orgDestino();}" readonly/>
                                                                          

                                                                    <A href="javascript:{divSegundoPlano=false;mostrarListaOrganizacionDestino();}" style="text-decoration:none;" name="anchorOrganizacionDestino"
                                                                        onclick="javascript:this.focus();">
                                                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp2" name="botonOrganizacionDestino" style="cursor:pointer;" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>"></span>
                                                                    </A>
                                                                </td>
                                                            </TR>

                                                            <TR>
                                                                <TD width="30%" class="etiqueta"><%=desc.getDescripcion("gEtiqUnidReg")%>:</TD>
                                                                <TD width="70%" class="columnP">
                                                                    <input class="inputTextoObligatorio" type="text" name="cod_uniRegDestino" size="8"
                                                                           onkeypress="javascript:PasaAMayusculas(event);"
                                                                           onfocus="this.select();"
                                                                           onchange="javascript:{onchangeCod_uniRegDestino();}"
                                                                           />
                                                                    <input id="obligatorio" class="inputTextoObligatorio" type="text" name="desc_uniRegDestino"
                                                                           style="width:300;height:17"
                                                                            onclick="javascript:{onClickDesc_uniRegDestino();}" readonly/>

                                                                    <A href="javascript: {divSegundoPlano=false;mostrarListaUnidRegistro();}" style="text-decoration:none;"
                                                                       onclick="javascript:this.focus();" >
                                                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp" name="botonUnidadeRexistroORD" style="cursor:pointer;" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>"></span>
                                                                    </A>
                                                                </TD>
                                                            </TR>

                                                            <tr>
                                                                <td width="30%" class="etiqueta"><%=desc.getDescripcion("gEtiq_TodasUnidades")%>:</td>
                                                                <td width="60%" class="columnP">
                                                                    <input type="checkbox" id="todas" align="left" name="checktodos" onclick="javascript:{pulsarTodas();}"/>
                                                                </td>
                                                            </tr>

                                                        </TABLE>
                                                        <!-- FIN DE UNIDADES TRAMITADORAS-->
                                                    </TD>
                                                </TR>

                                                <TR> <!-- Opciones del Informe-->
                                                    <TD class="sub3titulo">
                                                        <%=desc.getDescripcion("infOpcion")%>
                                                    </TD>
                                                </TR>

                                                <TR>
                                                    <TD>
                                                        <TABLE width="100%" height="100%" cellspacing="0px" cellpadding="0px" >
                                                            <TR>
                                                                <TD width="30%" class="etiqueta">
                                                                    <%=desc.getDescripcion("etiq_escudo")%>:
                                                                </TD>
                                                                <TD width="15%">
                                                                    <INPUT TYPE="checkbox" NAME="escudo" value="0">
                                                                </TD>
                                                            </TR>
                                                            <TR>
                                                                <TD class="etiqueta">
                                                                    <%=desc.getDescripcion("gEtiqNombre")%>&nbsp;<%=desc.getDescripcion("gEtiqAy")%>:
                                                                </TD>
                                                                <TD>
                                                                    <INPUT TYPE="checkbox" NAME="nombre" value="0">
                                                                </TD>
                                                            </TR>
                                                            <TR>
                                                                <TD class="etiqueta"><%=desc.getDescripcion("etiq_Idioma")%>:</TD>
                                                                <TD colspan="3">
                                                                    <input type="hidden" name="idioma"/>
                                                                    <input type="hidden" name="codIdioma"/>
                                                                    <input type="text" class="inputTextoObligatorio" name="descIdioma" size="20" maxlength="20" readonly="true"/>
                                                                    <A href="javascript:{;}" style="text-decoration:none;" id="anchorIdioma" name="anchorIdioma">
                                                                        <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonIdioma" style="cursor:pointer; border: 0px"
                                                                            alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>"></span>
                                                                    </A>
                                                                </TD>
                                                            </TR>
                                                            <TR>
                                                                <TD class= "etiqueta"><%=desc.getDescripcion("etiq_interopcion")%>:</TD>

                                                                <TD style="width:10%" class="textoSuelto">
                                                                    <input type="Radio" name="interopcion" class="textoSuelto" value="op1" onclick="" checked=true><%=desc.getDescripcion("etiq_interop1")%>
                                                                </TD>

                                                                <TD style="width:20%" class="textoSuelto">
                                                                    <input type="Radio" name="interopcion" class="textoSuelto" value="op2" onclick=""><%=desc.getDescripcion("etiq_interop2")%>
                                                                </TD>

                                                                <TD style="width:30%" class="textoSuelto">
                                                                    <input  type="Radio" name="interopcion" class="textoSuelto" value="op3" onclick=""><%=desc.getDescripcion("etiq_interop3")%>
                                                                </TD>
                                                            </TR>
                                                        </TABLE>
                                                    </TD>
                                                </TR>
                                </TABLE>

        <div class="botoneraPrincipal">
    
                            <input type="button" class="botonGeneral" value='<%=desc.getDescripcion("gbImprimir")%>' name="cmdImprimir" onclick="pulsarImprimir();"
                                   alt="<%=desc.getDescripcion("toolTip_bImprimir")%>" title="<%=desc.getDescripcion("toolTip_bImprimir")%>">
                        
                            <input type="button" class="botonGeneral" value='<%=desc.getDescripcion("gbSalir")%>' name="cmdSalir" onclick="pulsarSalir();"
                                   alt="<%=desc.getDescripcion("toolTip_bVolver")%>" title="<%=desc.getDescripcion("toolTip_bVolver")%>">
                      

                
        </div>
    </div>                                   
</FORM>




<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaCombo.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<SCRIPT type="text/javascript">

var comboIdiomas = new Combo("Idioma");
comboIdiomas.addItems(cod_idiomas,desc_idiomas);
comboIdiomas.selectItem(indice_idioma_usuario);

inicializar();

function posicionaCapaElemento(ancho, alto, ele) {
    pX = getOffsetLeft1(ele);
    var anchura = 0;
    if (pX + ancho > 675) {
        anchura = pX + ancho + 16 - 675;
    }
    pX = pX - anchura;
    var altoPx = ele.style.height;
    altoPx = altoPx.slice(0, altoPx.length - 2);
    var altoPxReal = new Number(altoPx);
    pY = getOffsetTop1(ele) + altoPxReal;
    var altura = 0;
    if (pY + alto > 450) {
        altura = 2 * altoPxReal + alto + 4;
    }
    pY = pY - altura;
}

//Usado para el calendario
var coordx=0;
var coordy=0;


<%if(userAgent.indexOf("MSIE")==-1) {%> //Que no sea IE
    window.addEventListener('mousemove', function(e) {
        coordx = e.clientX;
        coordy = e.clientY;
    }, true);
<%}%>

document.onmouseup = checkKeys;

function checkKeysLocal(evento,tecla) {
        var teclaAuxiliar;
        if(window.event){
            evento = window.event;
            teclaAuxiliar = evento.keyCode;
        }else
            teclaAuxiliar = evento.which;

        if (tecla == "Alt+I") {
            pulsarImprimir();
        } else if (tecla == "Alt+S") {
            pulsarSalir();
        }

        if ((layerVisible) || (divSegundoPlano)) buscar(evento);
        keyDel(evento);

        //if (event.keyCode == 9) {
        if (teclaAuxiliar == 9) {
            if (layerVisible) ocultarDiv();
            comboIdiomas.ocultar();
            if (divSegundoPlano) divSegundoPlano = false;
            if(IsCalendarVisible) hideCalendar();
            return false;
        }

        //if (event.keyCode == 40) {
        if (teclaAuxiliar == 40) {
            if ((layerVisible) || (divSegundoPlano)) upDown(teclaAuxiliar);
            return false;
        }

       // if (event.keyCode == 38) {
        if (teclaAuxiliar == 38) {
            if ((layerVisible) || (divSegundoPlano)) upDown(teclaAuxiliar);
            return false;
        }
         if (teclaAuxiliar == 1) {
            if(layerVisible) setTimeout('ocultarDivNoFocus()',40);
            if (comboIdiomas.base.style.visibility == "visible") setTimeout('comboIdiomas.ocultar()',20);
            if(IsCalendarVisible) replegarCalendario(coordx,coordy);
            if (divSegundoPlano) divSegundoPlano = false;
        }

        //if (event.button == 1) {
        if (evento.button == 1) {
            if(layerVisible) setTimeout('ocultarDivNoFocus()',40);
            if (comboIdiomas.base.style.visibility == "visible" && isClickOutCombo(comboIdiomas,coordx,coordy)) setTimeout('comboIdiomas.ocultar()',20);
            if (divSegundoPlano) divSegundoPlano = false;
        }
    }        

/*    function onBlurDepartamentoDestino(cod, des) {
       if (camposDistintosDiv(cod, des)) {
           document.formulario.txtCodigoUnidad.value = '';
           document.formulario.desc_uniRegDestinoORD.value = '';
       }
   } */
</SCRIPT></BODY></HTML>
