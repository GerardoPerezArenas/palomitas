<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
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
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.UsuariosGruposManager"%>
<%@ page import="es.altia.agora.technical.ConstantesDatos"%>

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
    
    String directiva_salidas_uor_usuario ="NO";
    
    UsuarioValueObject usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
    String[] params = usuarioVO.getParamsCon();
    
   
    if ("Salida".equals(tipo)){  
        
        if (UsuariosGruposManager.getInstance().tienePermisoDirectiva(ConstantesDatos.REGISTRO_S_SOLO_UORS_USUARIO, usuario.getIdUsuario(), params)) {                
                 directiva_salidas_uor_usuario="SI";
        }
    }

   
    // 10/05/2006
    // parte para que poniendo un código visible en el input se rellene la descripción
    Vector listaUORDTOs=new Vector();
    
    if("SI".equals(directiva_salidas_uor_usuario)){
        listaUORDTOs = UORsManager.getInstance().getListaUORsPermisoUsuarioNuevo(usuarioVO,params);
    }else{
        listaUORDTOs = UORsManager.getInstance().getListaUORs(false,params);
    }
    
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
    
    var informesDireccion = false;
    var ventanaPadre = null;
    if (self.parent.opener != undefined) 
        if (self.parent.opener.xanelaAuxiliarArgs) {
            if (self.parent.opener.xanelaAuxiliarArgs['informesDireccion']) informesDireccion = true;
            ventanaPadre = self.parent.opener.xanelaAuxiliarArgs['ventanaPadre'];
        }

</script>

<jsp:include page="/jsp/registro/tpls/app-constants.jsp"/>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
             type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor" property="idi_cod" value="<%=idioma%>"/>
<jsp:setProperty name="descriptor" property="apl_cod" value="1"/>
<TITLE>::: REGISTRO DE ENTRADA - Relación Unidades Orgánicas ::: </TITLE>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <LINK REL="stylesheet" MEDIA="screen" TYPE="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<SCRIPT type="text/javascript">
    
var EsquemaGenerico = "<%=es.altia.agora.business.util.GlobalNames.ESQUEMA_GENERICO%>"
    
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
        
            window.location = "<%=request.getContextPath()%>/MantAnotacionRegistro.do?opcion=<%=tipo%>";
}

function pulsarImprimir() {

     document.forms[0].idioma.value = document.forms[0].codIdioma.value;

     if (document.forms[0].todas.checked)
         document.forms[0].txtCodigoUnidad.value = "0";
    else
         document.forms[0].txtCodigoUnidad.value = document.forms[0].cod_visible_uor.value;

    if (validarFormulario()) {
        if (comprobarFecha(document.forms[0].fechaDesde) && comprobarFecha(document.forms[0].fechaHasta)) {
            if (comprobarCampos() == 1) {
                var anoD = document.forms[0].fechaDesde.value.substring(6, 10);
                var anoH = document.forms[0].fechaHasta.value.substring(6, 10);
                if (anoH == anoD) {

                    var numDesde = document.forms[0].regDesde.value;
                    var numHasta = document.forms[0].regHasta.value;

                    if ((numDesde != '') && (numHasta != '')) {
                        var resultadoConfirm = jsp_alerta("C", '<%=descriptor.getDescripcion("msjImpLibro")%>  ' + anoD + ' : <br><%=descriptor.getDescripcion("gEtiq_desde")%> ' +
                                                               numDesde + '<br><%=descriptor.getDescripcion("gEtiq_hasta")%> ' +
                                                               numHasta);
                        if (resultadoConfirm == "1") {
                            pleaseWait('on');
                            formulario.fechaInicio.value = document.forms[0].fechaDesde.value.substring(0, 2) + "/" +
                                                           document.forms[0].fechaDesde.value.substring(3, 5) + "/" +
                                                           document.forms[0].fechaDesde.value.substring(6, 10);
                            formulario.fechaFin.value = document.forms[0].fechaHasta.value.substring(0, 2) + "/" +
                                                        document.forms[0].fechaHasta.value.substring(3, 5) + "/" +
                                                        document.forms[0].fechaHasta.value.substring(6, 10);
                            formulario.opcion.value = "relacionUnidadesOrganicas";
                            formulario.target = "oculto";
                            formulario.action = "<%=request.getContextPath()%>/InformesRegistro.do";
                            formulario.submit();
                        }
                        else return;

                    } else {

                        var resultadoConfirm = jsp_alerta("C", '<%=descriptor.getDescripcion("msjImp")%>: <br><%=descriptor.getDescripcion("gEtiq_desde")%> ' +
                                                               document.forms[0].fechaDesde.value.substring(0, 2) + "/" +
                                                               document.forms[0].fechaDesde.value.substring(3, 5) + "/" +
                                                               document.forms[0].fechaDesde.value.substring(6, 10) + '<br><%=descriptor.getDescripcion("gEtiq_hasta")%> ' +
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
                            formulario.opcion.value = "relacionUnidadesOrganicas";
                            formulario.target = "oculto";
                            formulario.action = "<%=request.getContextPath()%>/InformesRegistro.do";
                            formulario.submit();
                        } else return;
                        }
                    } else jsp_alerta("A", '<%=descriptor.getDescripcion("msjAnDistinto")%>');
                }
            }
        }
    }
    function abrirInforme(nombre) {
        // A otra página que contiene el fichero PDF.

        if (nombre == "NO EXISTE") {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoDatos")%>');
        } else if (nombre != '') {
            if (informesDireccion) {
                ventanaPadre.verInforme(nombre);
            } else {
                var sourc = "<%=request.getContextPath()%>/jsp/verPdf.jsp?opcion=null&nombre=" + nombre;
                ventanaInforme = window.open("<%=request.getContextPath()%>/jsp/mainVentana.jsp;jsessionid=<%=idSesion%>?source=" + sourc, 'ventanaInforme', 'width=1000px,height=750px,status=' + '<%=statusBar%>' + ',toolbar=no');
                ventanaInforme.focus();
            }
        } else {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoPDF")%>');
        }
    }
    
    function comprobarCampos() {
        var r = 1;

        if (document.forms[0].fechaDesde.value == '') {
            var r = jsp_alerta("A", '<%=descriptor.getDescripcion("msjAbrirCond10")%>');
            //document.formulario.txtDiaE.select();
            document.forms[0].fechaDesde.select();
            r = 0;
        }
        else {
            if (document.forms[0].fechaHasta.value == '') {
                // var fecha = obtenerFechaString();
                var fecha = '<%=fechaServidor%>';
                document.forms[0].fechaHasta.value = fecha;
            }
            var fd = new Date(document.forms[0].fechaDesde.value.substring(6, 10), eval(document.forms[0].fechaDesde.value.substring(3, 5) - 1), document.forms[0].fechaDesde.value.substring(0, 2));
            var fh = new Date(document.forms[0].fechaHasta.value.substring(6, 10), eval(document.forms[0].fechaHasta.value.substring(3, 5) - 1), document.forms[0].fechaHasta.value.substring(0, 2));
            if (comparaFechas(fd, fh) == 1) {
                jsp_alerta("A", '<%=descriptor.getDescripcion("msjFechasNV")%>');
                r = 0;
            }
        }
        return r;
    }
    function inicializar() {  
        document.all.tabla1.style.marginTop = 0;
        //calcularPosTabla(document.all.tabla1);
        // var fecha = obtenerFechaString();
        var fecha = '<%=fechaServidor%>';
        document.forms[0].fechaDesde.value = fecha;
        document.forms[0].fechaHasta.value = fecha;
        document.forms[0].fechaDesde.focus();

        habilitarImagenCal("calDesde", true);
        habilitarImagenCal("calHasta", true);
    }

    function onClickDesc_uniRegDestinoORD() {
        var condiciones = new Array();
        var condicionCompleja="";
        var codUsu='<%=usuario.getIdUsuario()%>';
        var codOrg='<%=usuario.getOrgCod()%>';

        condiciones[0]='UOR_OCULTA'+'§¥';
        condiciones[1]='N';
        
        <% if ("SI".equals(directiva_salidas_uor_usuario)){%>
        condicionCompleja= 'uor_cod in (select uou_uor from '+EsquemaGenerico+'A_UOU a_uou where uou_usu='+codUsu+' and uou_org='+codOrg+')';   
        <%}%>
    muestraListaTabla('UOR_COD_VIS', 'UOR_NOM', 'A_UOR', condiciones, 'cod_visible_uor', 'desc_uniRegDestinoORD', 'botonUnidadeRexistroORD', '100', null,null,condicionCompleja);
    }

    function mostrarListaUnidRegDestinoORD() {
        var datos;
        var argumentos = new Array();
        argumentos[0] = document.forms[0].cod_visible_uor.value;

        abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + APP_CONTEXT_PATH + "/MantAnotacionRegistro.do?opcion=seleccionUOR&consultando=si",argumentos,
	'width=650,height=440',function(datos){
                        if (datos != null) {
                            document.forms[0].txtCodigoUnidad.value = datos[2];
                            document.forms[0].desc_uniRegDestinoORD.value = datos[1];
                            document.forms[0].cod_visible_uor.value = datos[0];
                        }

                        // si se pulsa xa ver el arbol con algo en el campo codigo, y damos a cancelar en la ventana modal
                        // puede q tengamos algo en código y nada en la descripción
                        if ((document.forms[0].cod_visible_uor.value != '') && (document.forms[0].desc_uniRegDestinoORD.value == '')) {
                            document.forms[0].txtCodigoUnidad.value = '';
                            document.forms[0].cod_visible_uor.value = '';
                        }
                        
                        document.forms[0].todas.checked = false;
                    });
    }
    
     function mostrarListaUnidRegDestinoORDFiltroUsu() {
        var argumentos = new Array();
        argumentos[0] = document.forms[0].cod_visible_uor.value;

        abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + APP_CONTEXT_PATH + "/MantAnotacionRegistro.do?opcion=seleccionUORFiltroUsu&consultando=si",
                                        argumentos,  'width=650,height=440;',function(datos){
                if (datos != null) {
                    document.forms[0].txtCodigoUnidad.value = datos[2];
                    document.forms[0].desc_uniRegDestinoORD.value = datos[1];
                    document.forms[0].cod_visible_uor.value = datos[0];
                }

                // si se pulsa xa ver el arbol con algo en el campo codigo, y damos a cancelar en la ventana modal
                // puede q tengamos algo en código y nada en la descripción
                if ((document.forms[0].cod_visible_uor.value != '') && (document.forms[0].desc_uniRegDestinoORD.value == '')) {
                    document.forms[0].txtCodigoUnidad.value = '';
                    document.forms[0].cod_visible_uor.value = '';
                }

                document.forms[0].todas.checked = false;
            });
    }
    
    
    
    
    function onchangeCod_uniRegDestinoORD() {
        // estas condiciones son tomadas directa/ del código existente
        var uor = buscarUorPorCodVisibleEstado(uors, document.forms[0].cod_visible_uor.value, 'A');
        if (uor != null) {
            document.forms[0].txtCodigoUnidad.value = uor.uor_cod;
            document.forms[0].desc_uniRegDestinoORD.value = uor.uor_nom;
        }
        else { // ha dado null para alta, buscamos de baja
            uor = buscarUorPorCodVisibleEstado(uors, document.forms[0].cod_visible_uor.value, 'B');
            if (uor != null) {
                document.forms[0].txtCodigoUnidad.value = uor.uor_cod;
                document.forms[0].desc_uniRegDestinoORD.value = uor.uor_nom;
            }
        }

        // uor cod visible no existe: resetear campos
        if (uor == null) {
            //alert('no existe');
            document.forms[0].cod_visible_uor.value = '';
            document.forms[0].desc_uniRegDestinoORD.value = '';
            document.forms[0].txtCodigoUnidad.value = '';
        }
        // si existe, asegurarse q el TODAS no esta marcado
        else {
            document.forms[0].todas.checked = false;
        }
    }

    function comprobarFecha(inputFecha) {
        if (Trim(inputFecha.value) != '') {
            if (!ValidarFechaConFormato(document.forms[0], inputFecha)) {
                jsp_alerta("A", '<%=descriptor.getDescripcion("fechaNoVal")%>');
                inputFecha.focus();
                return false;
            }
        }
        return true;
    }
    function pulsarTodas() {
        if (document.forms[0].todas.checked) {
            document.forms[0].txtCodigoUnidad.value = "0";
            document.forms[0].desc_uniRegDestinoORD.value = "TODAS";
            document.forms[0].cod_visible_uor.value = '';
             document.forms[0].desc_uniRegDestinoORD.disabled=true;	
            document.forms[0].cod_visible_uor.disabled=true;	
            document.getElementById("anchorUnidadeRexistroORD").removeAttribute('href');	
            document.getElementById("desp").className='fa fa-chevron-circle-down faDeshabilitado';
        } else {
            document.forms[0].cod_visible_uor.value = '';
            document.forms[0].txtCodigoUnidad.value = '';
            document.forms[0].desc_uniRegDestinoORD.value = '';
            document.forms[0].desc_uniRegDestinoORD.disabled=false;	
            document.forms[0].cod_visible_uor.disabled=false;	
            document.getElementById("anchorUnidadeRexistroORD").setAttribute('href', 'javascript:{divSegundoPlano=false;mostrarListaUnidRegDestinoORDFiltroUsu();}');	
            document.getElementById("desp").className='fa fa-chevron-circle-down';
        }
    }
</SCRIPT>
</head>

<body class="bandaBody" onload="javascript:{ pleaseWait('off');     
                               }">                          

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
                                

<FORM name="formulario" METHOD=POST target="_self">
<input type="hidden" name="txtCodigoUnidad"/>
<input type="hidden" name="tipo_select"/>
<input type="hidden" name="whereComplejo"/>
<input type="hidden" name="col_cod"/>
<input type="hidden" name="col_desc"/>
<input type="hidden" name="nom_tabla"/>
<input type="hidden" name="input_cod"/>
<input type="hidden" name="input_desc"/>
<input type="hidden" name="column_valor_where"/>
<input type="hidden" name="pdfFile" value="informeUnidades">
<input type="hidden" name="xslFile" value="defecto">
<input type="hidden" name="fechaInicio" value="">
<input type="hidden" name="fechaFin" value="">
<input type="hidden" name="opcion" value="">
<input type="hidden" name="tipo" value="<%=t%>">

<% if ('S' == t) { %>
<div id="titulo" class="txttitblancoder"><%=descriptor.getDescripcion("infReg_TitPrin" + t)%> <%=descriptor.getDescripcion("infReg_rbUniTram")%></div>
<% } else { %>
<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("infReg_TitPrin" + t)%> <%=descriptor.getDescripcion("infReg_rbUniTram")%></div>
<% } %>
<div class="contenidoPantalla">
<TABLE id="tabla1" width="100%" cellpadding="2" cellspacing="2">
<TR> <!-- Periodo de Consulta-->
    <TD class="sub3titulo"><%=descriptor.getDescripcion("InfUniPer")%></TD>
</TR>
<TR>
<TD>
<TABLE width="100%" height="100%" cellspacing="0px" cellpadding="0px">
    <TR>
        <TD style="width: 30%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqFechaDesde")%>:</TD>
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
        <td width="30%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqFechaHasta")%>:</TD>
        <td width="60%" class="columnP">
            <INPUT TYPE="text" class="inputTxtFecha" size="12" maxlength="10" name="fechaHasta"
                   onkeyup="javascript:return SoloCaracteresFecha(this);"
                   onblur="javascript:return comprobarFecha(this);"
                   onfocus="this.select();">
            <A href="javascript:calClick(event);return false;" onClick="mostrarCalHasta(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;">
                <span class="fa fa-calendar" aria-hidden="true" name="calHasta" id="calHasta" alt="<%=descriptor.getDescripcion("altFecha")%>" title="<%=descriptor.getDescripcion("altFecha")%>"
                     ></span>
            </A>
        </TD>
    </TR>

    <TR>
        <TD width="30%" class="etiqueta"><%=descriptor.getDescripcion("rer_numero")%>&nbsp;<%=descriptor.getDescripcion("gEtiq_desde")%>:
        </TD>
        <TD><INPUT TYPE="text" class="inputTexto"
                   NAME="regDesde" size=4 value=""
                   onkeyup="javascript:return SoloDigitosNumericos(this);"></TD>
    </TR>
    <TR>
        <TD width="30%" class="etiqueta"><%=descriptor.getDescripcion("rer_numero")%>&nbsp;<%=descriptor.getDescripcion("gEtiq_hasta")%>:
        </TD>
        <TD><INPUT TYPE="text" class="inputTexto"
                   NAME="regHasta" size=4 value=""
                   onkeyup="javascript:return SoloDigitosNumericos(this);"></TD>
    </TR>

</TABLE>
<!--Fin de Periodo de Consulta-->
<TR> <!-- UNIDADES TRAMITADORAS-->
    <TD width="100%" class="sub3titulo">
        <%=descriptor.getDescripcion("infReg_UniTram")%>
    </TD>
</TR>
<TR>
    <TD>
        <TABLE width="100%" height="100%" cellspacing="0px" cellpadding="0px">
            <TR>
                <TD width="30%" class="etiqueta"><%=descriptor.getDescripcion("infReg_UniTram1")%>:</TD>
                
                <%
                    if ((("S".equals(tipo)) || ("Salida".equals(tipo)))&&("SI".equals(directiva_salidas_uor_usuario))) {
                %> 
                <TD width="60%" class="columnP">
                    <input class="inputTextoObligatorio" type="text" name="cod_visible_uor" size="6" maxlength="6"
                           onfocus="this.select();"
                           onkeypress="javascript:PasaAMayusculas(event);"
                           onchange="javascript:{onchangeCod_uniRegDestinoORD();}"
                            />
                    <input id="obligatorio" class="inputTextoObligatorio" type="text" name="desc_uniRegDestinoORD"
                           style="width:195;height:17" onclick="javascript:{onClickDesc_uniRegDestinoORD();}" readonly/>
                     <A id="anchorUnidadeRexistroORD" href="javascript:{divSegundoPlano=false;mostrarListaUnidRegDestinoORDFiltroUsu();}"
                       style="text-decoration:none;"
                       onfocus="javascript:this.focus();">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp"
                             name="botonUnidadeRexistroORD" style="cursor:hand;"
                             alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>"></span>
                    </A>
                </TD>
                 <% } else { %>
                  <TD width="60%" class="columnP">
                    <input class="inputTextoObligatorio" type="text" name="cod_visible_uor" size="6" maxlength="6"
                           onfocus="this.select();"
                           onkeypress="javascript:PasaAMayusculas(event);"
                           onchange="javascript:{onchangeCod_uniRegDestinoORD();}"
                            />
                    <input id="obligatorio" class="inputTextoObligatorio" type="text" name="desc_uniRegDestinoORD"
                           style="width:195;height:17" onclick="javascript:{onClickDesc_uniRegDestinoORD();}" readonly/>
                    <A id="anchorUnidadeRexistroORD" href="javascript:{divSegundoPlano=false;mostrarListaUnidRegDestinoORD();}"
                       style="text-decoration:none;"
                       onfocus="javascript:this.focus();">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp"
                             name="botonUnidadeRexistroORD" style="cursor:hand;"
                             alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>"></span>
                    </A>
                </TD>
                  <% }  %>
            </TR>
            <tr>
                <td width="30%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_TodasUnidades")%>:</td>
                <td width="60%" class="columnP">
                    <input type="checkbox" id="todas" name="checktodos" onclick="javascript:{pulsarTodas();}"/>
                </td>
            </tr>
        </TABLE>
        <!-- FIN DE UNIDADES TRAMITADORAS-->
    </TD>
</TR>
<TR> <!-- Opciones del Informe-->
    <TD class="sub3titulo">
        <%=descriptor.getDescripcion("infOpcion")%>
    </TD>
</TR>
                                                                    
<TR>
    <TD>
        <TABLE width="100%" height="100%" cellspacing="0px" cellpadding="0px" >
            <TR>
                <TD width="30%" class="etiqueta">
                    <%=descriptor.getDescripcion("etiq_escudo")%>:
                </TD>
                <TD width="15%">
                    <INPUT TYPE="checkbox" NAME="escudo" value="0">
                </TD>
            </TR>
            <TR>
                <TD class="etiqueta">
                    <%=descriptor.getDescripcion("gEtiqNombre")%>&nbsp;<%=descriptor.getDescripcion("gEtiqAy")%>:
                </TD>
                <TD>
                    <INPUT TYPE="checkbox" NAME="nombre" value="0">
                </TD>
            </TR>
            <TR>
                <TD class="etiqueta"><%=descriptor.getDescripcion("etiq_Idioma")%>:</TD>
                <TD colspan="3">
                   <input type="hidden" name="idioma"/>
                   <input type="hidden" name="codIdioma"/>
                   <input type="text" class="inputTextoObligatorio" name="descIdioma" size="16" maxlength="16" readonly="true"/>
                   <A href="javascript:{}" style="text-decoration:none;" id="anchorIdioma" name="anchorIdioma">
                      <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonIdioma" style="cursor:hand; border: 0px"
                      alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>"></span>
                   </A>
                </TD>
            </TR>    
             <TR>
                       <TD class= "etiqueta"><%=descriptor.getDescripcion("etiq_interopcion")%>:</TD>
                                                
                       <TD style="width:10%" class="textoSuelto">
                       <input type="Radio" name="interopcion" class="textoSuelto" value="op1" onclick="" checked=true><%=descriptor.getDescripcion("etiq_interop1")%>
                       </TD>
                                                    
                       <TD style="width:20%" class="textoSuelto">
                       <input type="Radio" name="interopcion" class="textoSuelto" value="op2" onclick=""><%=descriptor.getDescripcion("etiq_interop2")%>
                       </TD>
                                                    
                       <TD style="width:30%" class="textoSuelto">
                       <input  type="Radio" name="interopcion" class="textoSuelto" value="op3" onclick=""><%=descriptor.getDescripcion("etiq_interop3")%>
                       </TD>
               </TR>
        </TABLE>
    </td>
</tr>
</TABLE>
<div class="botoneraPrincipal">
    <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbImprimir")%>' name="cmdImprimir" onclick="pulsarImprimir();"
    alt="<%=descriptor.getDescripcion("toolTip_bImprimir")%>" title="<%=descriptor.getDescripcion("toolTip_bImprimir")%>">
    <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbSalir")%>' name="cmdSalir" onclick="pulsarSalir();"
    alt="<%=descriptor.getDescripcion("toolTip_bVolver")%>" title="<%=descriptor.getDescripcion("toolTip_bVolver")%>">
</div>
</div>
</FORM>
</BODY>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaCombo.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<SCRIPT type="text/javascript">

    var comboIdiomas = new Combo("Idioma");
    comboIdiomas.addItems(cod_idiomas,desc_idiomas);
    comboIdiomas.selectItem(indice_idioma_usuario);

    inicializar();

    

    document.onmouseup = checkKeys;

    function checkKeysLocal(evento,tecla) {
        var teclaAuxiliar = evento.which;	
        var coordx = evento.clientX;	
        var coordy = evento.clientY;

        if (tecla == "Alt+I") {
            pulsarImprimir();
        } else if (tecla == "Alt+S") {
            pulsarSalir();
        }

        if ((layerVisible) || (divSegundoPlano)) buscar(evento);
        keyDel(evento);

        if (teclaAuxiliar == 9) {
            if (layerVisible) ocultarDiv();
            comboIdiomas.ocultar();
            if (divSegundoPlano) divSegundoPlano = false;
            if(IsCalendarVisible) hideCalendar();
            return false;
        }

        if (teclaAuxiliar == 40) {
            if ((layerVisible) || (divSegundoPlano)) upDown(teclaAuxiliar);
            return false;
        }

        if (teclaAuxiliar == 38) {
            if ((layerVisible) || (divSegundoPlano)) upDown(teclaAuxiliar);
            return false;
        }
        
         if (teclaAuxiliar == 1) {
            if (layerVisible) setTimeout('ocultarDiv()', 50);
             if (comboIdiomas.base.style.display != "none" && isClickOutCombo(comboIdiomas,coordx,coordy)) setTimeout('comboIdiomas.ocultar()',20);
            if (divSegundoPlano) divSegundoPlano = false;
            if(IsCalendarVisible) replegarCalendarioGeneral(evento);
        }
        if (evento.button == 1) {
            if (layerVisible) setTimeout('ocultarDiv()', 50);
            if (comboIdiomas.base.style.display != "none" && isClickOutCombo(comboIdiomas,coordx,coordy)) setTimeout('comboIdiomas.ocultar()',20);
            if (divSegundoPlano) divSegundoPlano = false;
        }
    }        

        
</SCRIPT>
</HTML>
