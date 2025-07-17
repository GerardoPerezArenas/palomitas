<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.agora.technical.Fecha" %>
<%@ page import="java.util.Date" %>
<%@ page import="es.altia.common.service.config.Config" %>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@ page import="java.util.Vector" %>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.IdiomasManager" %>
<%@ page import="es.altia.agora.business.util.GeneralValueObject" %>

<HTML>
    <head>
        <%
            String tipo = request.getParameter("tipo");
            char t = tipo.charAt(0);
            int idioma = 1;
            String[] params = null;
            UsuarioValueObject usuario = null;
            String css = "";
            if (session != null) {
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    css = usuario.getCss();
                    params = usuario.getParamsCon();
                }
            }
            // Fecha actual del servidor.
            Fecha f = new Fecha();
            Date dateServidor = new Date();
            String fechaServidor = f.obtenerString(dateServidor);
            String idSesion = session.getId();

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
            // Carga de la lista de idiomas
            Vector listaIdiomas = IdiomasManager.getInstance().getListaIdiomas(params);

            String userAgent = request.getHeader("user-agent");
        %>
        
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <script type="text/javascript" src="<html:rewrite page='/scripts/TablaNueva.js'/>"></script>
        <script type="text/javascript">
    
          // Idiomas
          var cod_idiomas = new Array();
          var desc_idiomas = new Array();
          var indice_idioma_usuario = 0;
          var listado_visible=false;
       <% for(int j=0; j<listaIdiomas.size(); j++) {
              GeneralValueObject idi = (GeneralValueObject)listaIdiomas.get(j); %>
              cod_idiomas[<%=j%>] = '<%=idi.getAtributo("codigo")%>';
              desc_idiomas[<%=j%>] = '<%=idi.getAtributo("descripcion")%>';
              if (cod_idiomas[<%=j%>] == <%=idioma%>) indice_idioma_usuario = <%=j%>; 
       <% } %>
       
       var informesDireccion = false;
       var ventanaPadre = null;
       if (self.parent.opener != undefined) 
            if (self.parent.opener.xanelaAuxiliarArgs) {
                if (self.parent.opener.xanelaAuxiliarArgs['informesDireccion']) 
                    informesDireccion = true;
                ventanaPadre = self.parent.opener.xanelaAuxiliarArgs['ventanaPadre'];
            }
    </script>
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
                     type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
        <jsp:setProperty name="descriptor" property="idi_cod" value="<%=idioma%>"/>
        <jsp:setProperty name="descriptor" property="apl_cod" value="1"/>
        <TITLE>::: REGISTRO DE ENTRADA - Consulta del libro de registro :::</TITLE>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%><%=css%>" type="text/css">
        <!-- Ficheros javascript -->
        <script src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
        <SCRIPT>
            
         
        
        // Calendario.
        function mostrarCalDesde(evento) {
            if(window.event) evento = window.event;
            if (document.getElementById("calDesde").className.indexOf("fa-calendar") != -1) {
                showCalendar('forms[0]', 'fechaDesde', null, null, null, '', 'calDesde', '',null,null,null,null,null,null,null,null,evento);
            }
        }
        
        function mostrarCalHasta(evento) {
            if(window.event) evento = window.event;
            if (document.getElementById("calHasta").className.indexOf("fa-calendar") != -1) {
                showCalendar('forms[0]', 'fechaHasta', null, null, null, '', 'calHasta', '',null,null,null,null,null,null,null,null,evento);

            }
        }
        
        
        var ventanaInforme;
        
   function pulsarSalir() {

        window.location = "<%=request.getContextPath()%>/MantAnotacionRegistro.do?opcion=<%=tipo%>";
}
            
            function pulsarImprimir() {
                var listadoImprimir="";
                if(listado_visible) listadoImprimir= cargarSeleccionados();
         
                document.forms[0].idioma.value = document.forms[0].codIdioma.value;
                if (comprobarFecha(document.forms[0].fechaDesde) && comprobarFecha(document.forms[0].fechaHasta)) {
                    
                    if (validarFormulario()) {
                        
                        if (document.forms[0].fechaHasta.value == '') {
                            // var fecha = obtenerFechaString();
                            var fecha = '<%=fechaServidor%>';
                            document.forms[0].fechaHasta.value = fecha;
                        }
                        
                        var diaD = document.forms[0].fechaDesde.value.substring(0, 2);
                        var mesD = document.forms[0].fechaDesde.value.substring(3, 5);
                        var anoD = document.forms[0].fechaDesde.value.substring(6, 10);
                        var diaH = document.forms[0].fechaHasta.value.substring(0, 2);
                        var mesH = document.forms[0].fechaHasta.value.substring(3, 5);
                        var anoH = document.forms[0].fechaHasta.value.substring(6, 10);
                        var fechaD = new Date(anoD, mesD - 1, diaD);
                        var fechaH = new Date(anoH, mesH - 1, diaH);
                        var numDesde = document.forms[0].regDesde.value;
                        var numHasta = document.forms[0].regHasta.value;
                        
                        if ((numDesde != '') && (numHasta != '')){
                            var resultadoConfirm = jsp_alerta("C", '<%=descriptor.getDescripcion("msjImpLibro")%>  '+  anoD + ' : <br><%=descriptor.getDescripcion("gEtiq_desde")%> ' +
                                numDesde + '<br><%=descriptor.getDescripcion("gEtiq_hasta")%> ' +
                                numHasta);
                            if (resultadoConfirm == "1") {
                                pleaseWait('on');
                                formulario.fechaInicio.value = diaD + "/" + mesD + "/" + anoD;
                                formulario.fechaFin.value = diaH + "/" + mesH + "/" + anoH;
                                formulario.elementosImprimir.value=listadoImprimir;
                                formulario.opcion.value = "informeLibroRegistro";
                                formulario.target = "oculto";
                                formulario.action = "<%=request.getContextPath()%>/InformesRegistro.do";
                                formulario.submit();
                            }
                            else return;
                            
                        }else {
                        
                        
                        
                        if (anoH == anoD) {
                            
                            if ((comparaFechas(fechaD, fechaH) == 0) || (comparaFechas(fechaD, fechaH) == 2)) {
                                var resultadoConfirm = jsp_alerta("C", '<%=descriptor.getDescripcion("msjImpLibro")%>: <br><%=descriptor.getDescripcion("gEtiq_desde")%> ' +
                                    diaD + "/" + mesD + "/" + anoD + '<br><%=descriptor.getDescripcion("gEtiq_hasta")%> ' +
                                    diaH + "/" + mesH + "/" + anoH);
                                if (resultadoConfirm == "1") {
                                    pleaseWait('on');
                                    formulario.fechaInicio.value = diaD + "/" + mesD + "/" + anoD;
                                    formulario.fechaFin.value = diaH + "/" + mesH + "/" + anoH;
                                    formulario.opcion.value = "informeLibroRegistro";
                                    formulario.elementosImprimir.value=listadoImprimir;
                                    formulario.target = "oculto";
                                    formulario.action = "<%=request.getContextPath()%>/InformesRegistro.do";
                                    formulario.submit();
                                }
                                else return;
                            }
                            else jsp_alerta("A", '<%=descriptor.getDescripcion("msjFechasNV")%>');
                            } else jsp_alerta("A", '<%=descriptor.getDescripcion("msjAnDistinto")%>');
                            }
                        }
                        
                    }
                }
                
                function pulsarBuscarListado(){
                    document.forms[0].idioma.value = document.forms[0].codIdioma.value;
                    if (comprobarFecha(document.forms[0].fechaDesde) && comprobarFecha(document.forms[0].fechaHasta)) {
                    if (validarFormulario()) {
                        if (document.forms[0].fechaHasta.value == '') {
                            var fecha = '<%=fechaServidor%>';
                            document.forms[0].fechaHasta.value = fecha;
                        }
                        var diaD = document.forms[0].fechaDesde.value.substring(0, 2);
                        var mesD = document.forms[0].fechaDesde.value.substring(3, 5);
                        var anoD = document.forms[0].fechaDesde.value.substring(6, 10);
                        var diaH = document.forms[0].fechaHasta.value.substring(0, 2);
                        var mesH = document.forms[0].fechaHasta.value.substring(3, 5);
                        var anoH = document.forms[0].fechaHasta.value.substring(6, 10);
                        var fechaD = new Date(anoD, mesD - 1, diaD);
                        var fechaH = new Date(anoH, mesH - 1, diaH);
                        var numDesde = document.forms[0].regDesde.value;
                        var numHasta = document.forms[0].regHasta.value;
                        if ((numDesde != '') && (numHasta != '')){
                            var resultadoConfirm = jsp_alerta("C", '<%=descriptor.getDescripcion("msjListadoLibro")%>  '+  anoD + ' : <br><%=descriptor.getDescripcion("gEtiq_desde")%> ' +
                                numDesde + '<br><%=descriptor.getDescripcion("gEtiq_hasta")%> ' +
                                numHasta);
                            if (resultadoConfirm == "1") {
                                pleaseWait('on');
                                formulario.fechaInicio.value = diaD + "/" + mesD + "/" + anoD;
                                formulario.fechaFin.value = diaH + "/" + mesH + "/" + anoH;
                                formulario.opcion.value = "informeBuscarLibroRegistro";
                                formulario.target = "oculto";
                                formulario.action = "<%=request.getContextPath()%>/InformesRegistro.do";
                                formulario.submit();
                            }
                            else return;
                            
                        }else {
                        if (anoH == anoD) {
                            if ((comparaFechas(fechaD, fechaH) == 0) || (comparaFechas(fechaD, fechaH) == 2)) {
                                var resultadoConfirm = jsp_alerta("C", '<%=descriptor.getDescripcion("msjListadoLibro")%>: <br><%=descriptor.getDescripcion("gEtiq_desde")%> ' +
                                    diaD + "/" + mesD + "/" + anoD + '<br><%=descriptor.getDescripcion("gEtiq_hasta")%> ' +
                                    diaH + "/" + mesH + "/" + anoH);
                                if (resultadoConfirm == "1") {
                                    pleaseWait('on');
                                    formulario.fechaInicio.value = diaD + "/" + mesD + "/" + anoD;
                                    formulario.fechaFin.value = diaH + "/" + mesH + "/" + anoH;
                                    formulario.opcion.value = "informeBuscarLibroRegistro";
                                    formulario.target = "oculto";
                                    formulario.action = "<%=request.getContextPath()%>/InformesRegistro.do";
                                    formulario.submit();
                                }
                                else return;
                            }
                            else jsp_alerta("A", '<%=descriptor.getDescripcion("msjFechasNV")%>');
                            } else jsp_alerta("A", '<%=descriptor.getDescripcion("msjAnDistinto")%>');
                            }
                        }
                        
                    }
                }
                
                function recuperaListado(listaResultados){
                    pleaseWait("off");
                    $('#tablaResultados').css('display', 'inline-block');
                    tablaResultados.lineas=listaResultados;
                    tablaResultados.displayTabla();
                    listado_visible=true;
                }
                
                
                function cargarSeleccionados(){
                    checkboxes=new Array();
                    var seleccionImprimir="";
                    var tabla = $('#tb_tabla0').DataTable();
                    checkboxes = $(".checkLinea:checked", tabla.rows().nodes());
                    for(i=0; i<checkboxes.length; i++){
                        seleccionImprimir+=checkboxes[i].value+";";
                    }
                    return seleccionImprimir;
                }
                
                
                function abrirInforme(nombre) {
                    // A otra página que contiene el fichero PDF.
                    if (nombre == "NO EXISTE") {
                        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoDatos")%>');
                    } else if (nombre != '') {
                        if (informesDireccion) {
                            // verPdf.jsp no funciona desde el popup
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
                    
                    function inicializar() {
                        //document.all.tabla1.style.marginTop = 0; // calcularPosTabla(document.all.tabla1);
                        // var fecha = obtenerFechaString();
                        var fecha = '<%=fechaServidor%>';
                        document.forms[0].fechaDesde.value = fecha;
                        document.forms[0].fechaHasta.value = fecha;
                        document.forms[0].fechaDesde.focus();
                        habilitarImagenCal("calDesde", true);
                        habilitarImagenCal("calHasta", true);
                        cargaTabla();
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
                        
        </SCRIPT>
    </head>
    
    <body class="bandaBody" onload="javascript:{ pleaseWait('off'); 
       }">
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        
<FORM name="formulario" METHOD=POST target="_self">
    <input type="hidden" name="pdfFile" value="informeLibroRegistro">
    <input type="hidden" name="xslFile" value="defecto">
    <input type="hidden" name="cabFile" value="informeLibroRegistro">
    <input type="hidden" name="fechaInicio" value="">
    <input type="hidden" name="fechaFin" value="">
    <input type="hidden" name="opcion" value="">
    <input type="hidden" name="tipo" value="<%=t%>">
    <input type="hidden" name="elementosImprimir" value="">

    <%     if ('S' == t) {%>
    <div id="titulo" class="txttitblancoder"> <%=descriptor.getDescripcion("infLibroTit" + t)%></div>
    <% } else {%>
    <div id="titulo" class="txttitblanco"> <%=descriptor.getDescripcion("infLibroTit" + t)%></div>
    <% }%>
    <div class="contenidoPantalla">
        <TABLE id="tabla1" width="100%" cellpadding="2" cellspacing="2" align="center">
                <TR>
                    <td style="width: 30%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqFechaDesde")%>:
                    </td>
                    <TD style="width: 70%;" class="columnP">
                        <INPUT TYPE="text" id="obligatorio" class="inputTxtFechaObligatorio"
                               size="12" maxlength="10" name="fechaDesde"
                               onkeyup="javascript:return SoloCaracteresFecha(this);"
                               onblur="javascript:return comprobarFecha(this);"
                               onfocus="this.select();">
                        <A href="javascript:calClick(event);return false;"
                           onClick="mostrarCalDesde(event);return false;"onblur="ocultarCalendarioOnBlur(event); return false;">
                            <span class="fa fa-calendar" aria-hidden="true" style="border: 0; height: 17px; width: 25px;" name="calDesde" id="calDesde" alt="<%=descriptor.getDescripcion("altFecha")%>" title="<%=descriptor.getDescripcion("altFecha")%>" ></span>
                        </A>
                    </td>
                </TR>
                <TR>
                    <td class="etiqueta"><%=descriptor.getDescripcion("gEtiqFechaHasta")%>:</td>
                    <TD class="columnP">
                        <INPUT TYPE="text" class="inputTxtFecha" size="12" maxlength="10"
                               name="fechaHasta"
                               onkeyup="javascript:return SoloCaracteresFecha(this);"
                               onblur="javascript:return comprobarFecha(this);"
                               onfocus="this.select();">
                        <A href="javascript:calClick(event);return false;"
                           onClick="mostrarCalHasta(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;">
                            <span class="fa fa-calendar" aria-hidden="true" style="border: 0; height: 17px; width: 25px;" name="calHasta" id="calHasta" alt="<%=descriptor.getDescripcion("altFecha")%>" title="<%=descriptor.getDescripcion("altFecha")%>"
                                 ></span>
                        </A>
                    </td>
                </TR>
                <TR>
                    <TD class="etiqueta"><%=descriptor.getDescripcion("rer_numero")%>
                        &nbsp;<%=descriptor.getDescripcion("gEtiq_desde")%>:
                    </TD>
                    <TD>
                        <INPUT TYPE="text" class="inputTexto" NAME="regDesde" size=4 value=""
                               onkeyup="return SoloDigitosNumericos(this);">
                    </TD>
                </TR>
                <TR>
                    <TD class="etiqueta"><%=descriptor.getDescripcion("rer_numero")%>
                        &nbsp;<%=descriptor.getDescripcion("gEtiq_hasta")%>:
                    </TD>
                    <TD>
                        <INPUT TYPE="text" class="inputTexto" NAME="regHasta" size=4 value=""
                               onkeyup="javascript:return SoloDigitosNumericos(this);">
                    </TD>
                </TR>
                <TR>
                    <TD colspan="2"  style="width:100%;" class="sub3titulo"><%=descriptor.getDescripcion("infOpcion")%></TD>
                </TR>
                <TR>
                    <TD class="etiqueta"><%=descriptor.getDescripcion("gEtiq_ordenReg")%>:</TD>
                    <TD><INPUT TYPE="checkbox" NAME="orden" value="1"></TD>
                </TR>

                <TR>
                    <TD class="etiqueta"><%=descriptor.getDescripcion("gEtiqPagina")%>
                        &nbsp;<%=descriptor.getDescripcion("gEtiq_desde")%>:
                    </TD>
                    <TD>
                        <INPUT TYPE="text" id="obligatorio" class="inputTextoObligatorio"
                               NAME="pagDesde" size=4 value="1"
                               onkeyup="return SoloDigitosNumericos(this);">
                    </TD>
                </TR>
                <TR>
                    <TD class="etiqueta"><%=descriptor.getDescripcion("rer_numero")%>
                        &nbsp;<%=descriptor.getDescripcion("gEtiq_desde")%>:
                    </TD>
                    <TD>
                        <INPUT TYPE="text" id="obligatorio" class="inputTextoObligatorio"
                               NAME="numDesde" size=4 value="1"
                               onkeyup="return SoloDigitosNumericos(this);">
                    </TD>
                </TR>
                <TR>
                    <TD class="etiqueta"><%=descriptor.getDescripcion("etiq_escudo")%>:</TD>
                    <TD>
                        <INPUT TYPE="checkbox" NAME="escudo" value="0">
                        <span class="etiqueta"><%=descriptor.getDescripcion("gEtiqPortada")%>:&nbsp;</span>
                        <INPUT type="checkbox" NAME="portada" value="0">
                    </TD>
                </TR>
                <TR>
                    <TD class="etiqueta"><%=descriptor.getDescripcion("gEtiqNombre")%>
                        &nbsp;<%=descriptor.getDescripcion("gEtiqAy")%>:
                    </TD>
                    <TD><INPUT TYPE="checkbox" NAME="nombre" value="0"></TD>
                </TR>
                <TR>
                    <TD class="etiqueta"><%=descriptor.getDescripcion("etiq_Idioma")%>:</TD>
                    <TD >
                       <input type="hidden" name="idioma"/>
                       <input type="hidden" name="codIdioma"/>
                       <input type="text" class="inputTextoObligatorio" name="descIdioma" size="16" maxlength="16" readonly="true"/>
                       <A href="javascript:{}" style="text-decoration:none;" id="anchorIdioma" name="anchorIdioma">
                          <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonIdioma" style="cursor:hand;" 
                          alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>"></span>
                       </A>
                    </TD>
                </TR>
                <TR>
                     <TD class= "etiqueta">
                         <%=descriptor.getDescripcion("etiq_interopcion")%>:
                     </TD>
                    <TD class="textoSuelto">
                        <input type="Radio" name="interopcion" class="textoSuelto" style="padding-right: 20px" value="op1" onclick="" checked=true><%=descriptor.getDescripcion("etiq_interop1")%>
                       <input type="Radio" name="interopcion" class="textoSuelto" style="padding-right: 20px" value="op2" onclick=""><%=descriptor.getDescripcion("etiq_interop2")%>
                       <input  type="Radio" name="interopcion" class="textoSuelto" value="op3" onclick=""><%=descriptor.getDescripcion("etiq_interop3")%>
                    </TD>
                </TR>
        </TABLE>
        <div>        
            <table width="100%" >
                <tr>
                <td id="tablaResultados" style="display:none"></td>
                </tr>
             </table>
        </div>
    <div class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbImprimir")%> name="cmdImprimir" onClick="pulsarImprimir();" 
                alt="<%=descriptor.getDescripcion("toolTip_bImprimirLibro")%>" title="<%=descriptor.getDescripcion("toolTip_bImprimirLibro")%>">
        <%     if ('S' == t) {%>
        <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbListado")%>' name="cmdListado" onclick="pulsarBuscarListado();" alt='<%=descriptor.getDescripcion("gbListado")%>' title='<%=descriptor.getDescripcion("gbListado")%>'>
        <%     }%>
        <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir"  onClick="pulsarSalir();" 
        alt="<%=descriptor.getDescripcion("toolTip_bVolver")%>" title="<%=descriptor.getDescripcion("toolTip_bVolver")%>">
    </div>
    
</div>
  
</FORM>
       
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>

<SCRIPT>
            var comboIdiomas = new Combo("Idioma");
            comboIdiomas.addItems(cod_idiomas,desc_idiomas);
            comboIdiomas.selectItem(indice_idioma_usuario);
            
            var tablaResultados = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaResultados"),null);
            tablaResultados.addColumna('50','center',"<i class='fa fa-check' aria-hidden='true'></i>");
            tablaResultados.addColumna('50','center','<%=descriptor.getDescripcion("etiqNumero")%>');
            tablaResultados.addColumna('70','center','<%=descriptor.getDescripcion("gEtiqFecha")%>');
            tablaResultados.addColumna('300','center','<%=descriptor.getDescripcion("etiq_Extracto")%>');
            tablaResultados.addColumna('120','center','<%=descriptor.getDescripcion("rotuloDestinatario")%>');
            tablaResultados.addColumna('100','center','<%=descriptor.getDescripcion("etiqOrig")%>');
            tablaResultados.displayCabecera=true;
            
            
            function cargaTabla(){
                tablaResultados.displayTabla();
            }
    inicializar();


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
        var teclaAuxiliar = "";
        if(window.event){
            evento = window.event;
            teclaAuxiliar = evento.keyCode;
        }else{
            teclaAuxiliar = evento.which;
        }

        if (tecla == "Alt+I") {
            pulsarImprimir();
        } else if (tecla == "Alt+S") {
            pulsarSalir();
        }


        if (teclaAuxiliar == 9){
            
            comboIdiomas.ocultar();
            if(IsCalendarVisible) hideCalendar();  //Si hay calendario

            return false;
        }
        if (teclaAuxiliar == 1){

            if (comboIdiomas.base.style.visibility == "visible") setTimeout('comboIdiomas.ocultar()',20);
            if(IsCalendarVisible) replegarCalendario(coordx,coordy); //Si hay calendario
        }
        if(evento.button == 9){

            comboIdiomas.ocultar(); //Combos creados con new Combo
        }

    }
        </SCRIPT>
    </BODY>
</HTML>
