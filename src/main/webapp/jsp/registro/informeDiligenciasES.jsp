<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.technical.Fecha" %>
<%@page import="java.util.Date" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="java.util.Vector" %>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.IdiomasManager" %>
<%@ page import="es.altia.agora.business.util.GeneralValueObject" %>

<HTML>
    <head>
        <%
            String tipo = request.getParameter("tipo");
            char t = tipo.charAt(0);
            int idioma = 1;
            String css = "";
            String[] params = null;
            UsuarioValueObject usuario = null;
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
            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");

            Vector listaIdiomas = IdiomasManager.getInstance().getListaIdiomas(params);

            String userAgent = request.getHeader("user-agent");
        %>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
        <jsp:useBean id="desc" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
        <jsp:setProperty name="desc"  property="idi_cod" value="<%=idioma%>" />
        <jsp:setProperty name="desc"  property="apl_cod" value="1" />
        <TITLE>::: REGISTRO DE ENTRADA - Informe Dilixencias Diarias :::</TITLE>
        <!-- Estilos -->
        <link rel="stylesheet" href="<%=request.getContextPath()%><%=css%>" type="text/css">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >

        <!-- Ficheros javascript -->
        <script src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
        <script src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <SCRIPT>

            var cod_idiomas = new Array();
            var desc_idiomas = new Array();
            var indice_idioma_usuario = 0;
            <% for(int j=0; j<listaIdiomas.size(); j++) {
              GeneralValueObject idi = (GeneralValueObject)listaIdiomas.get(j); %>
              cod_idiomas[<%=j%>] = '<%=idi.getAtributo("codigo")%>';
              desc_idiomas[<%=j%>] = '<%=idi.getAtributo("descripcion")%>';
              if (cod_idiomas[<%=j%>] == <%=idioma%>) indice_idioma_usuario = <%=j%>;
            <% } %>


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

            function checkKeysLocal(evento,tecla){
                var teclaAuxiliar = "";
                if(window.event){
                    evento = window.event;
                    teclaAuxiliar = evento.keyCode;
                }else{
                    teclaAuxiliar = evento.which;
                }
                //** Esta funcion se debe implementar en cada JSP para particularizar  **//
                //** las acciones a realizar de las distintas combinaciones de teclas  **//
                if(tecla=="Alt+I"){
                    pulsarImprimir();
                }else if(tecla=="Alt+S"){
                pulsarSalir();
                }

                if (teclaAuxiliar == 9){
                    if(IsCalendarVisible) hideCalendar();
                }
                if (teclaAuxiliar == 1){
                    if(IsCalendarVisible) replegarCalendario(coordx,coordy);
                }

            }

        // --- Calendario

        function mostrarCalDesde(evento) {
            if(window.event) evento = window.event;
            if (document.getElementById("calDesde").className.indexOf("fa-calendar") != -1 )
                showCalendar('forms[0]','fechaDesde',null,null,null,'','calDesde','',null,null,null,null,null,null,null,null,evento);
        }

        function mostrarCalHasta(evento) {
            if(window.event) evento = window.event;
            if (document.getElementById("calHasta").className.indexOf("fa-calendar") != -1 )
                showCalendar('forms[0]','fechaHasta',null,null,null,'','calHasta','',null,null,null,null,null,null,null,null,evento);
        }


        // --- Funciones de la página

        var ventanaInforme;

        function pulsarSalir() {
            if(ventanaInforme)
                ventanaInforme.close();
            window.location = "<%=request.getContextPath()%>/MantAnotacionRegistro.do?opcion=<%=tipo%>";
        }


        function comprobarCampos() {
            var r = 1;

            if(document.forms[0].fechaDesde.value == '') {
                var r = jsp_alerta("A", '<%=desc.getDescripcion("msjAbrirCond10")%>');
                    document.forms[0].fechaDesde.select();
                    r = 0;
                }
                else {
                    if(document.forms[0].fechaHasta.value == ''){
                        // var fecha = obtenerFechaString();
                        var fecha = '<%=fechaServidor%>';
                        document.forms[0].fechaHasta.value=fecha;
                    }
                    var fd = new Date(document.forms[0].fechaDesde.value.substring(6,10), eval(document.forms[0].fechaDesde.value.substring(3,5)-1), document.forms[0].fechaDesde.value.substring(0,2));
                    var fh = new Date(document.forms[0].fechaHasta.value.substring(6,10), eval(document.forms[0].fechaHasta.value.substring(3,5)-1), document.forms[0].fechaHasta.value.substring(0,2));

                    if(comparaFechas(fd,fh) == 1){
                        jsp_alerta("A", '<%=desc.getDescripcion("msjFechasNV")%>');
                            r = 0;
                        }
                    }
                    return r;
                }


                function pulsarImprimir() {
                    if(validarFormulario()){
                        document.forms[0].idioma.value = document.forms[0].codIdioma.value;
                        if (comprobarFecha(document.forms[0].fechaDesde) && comprobarFecha(document.forms[0].fechaHasta) ) {
                            if(comprobarCampos() == 1){
                                var anoD = document.forms[0].fechaDesde.value.substring(6,10);
                                var anoH = document.forms[0].fechaHasta.value.substring(6,10);

                                if(anoH == anoD){

                                    var resultadoConfirm = jsp_alerta("C",'<%=desc.getDescripcion("msjImp")%>: <br><%=desc.getDescripcion("gEtiq_desde")%> ' +
                                        document.forms[0].fechaDesde.value.substring(0,2) + "/" +
                                        document.forms[0].fechaDesde.value.substring(3,5)+ "/" +
                                        document.forms[0].fechaDesde.value.substring(6,10)+ '<br><%=desc.getDescripcion("gEtiq_hasta")%> ' +
                                        document.forms[0].fechaHasta.value.substring(0,2)+ "/" +
                                        document.forms[0].fechaHasta.value.substring(3,5) + "/" +
                                        document.forms[0].fechaHasta.value.substring(6,10));
                                    if (resultadoConfirm == "1"){
                                        formulario.fechaInicio.value = document.forms[0].fechaDesde.value.substring(0,2) + "/" +
                                        document.forms[0].fechaDesde.value.substring(3,5)+ "/" +
                                        document.forms[0].fechaDesde.value.substring(6,10);
                                        formulario.fechaFin.value = document.forms[0].fechaHasta.value.substring(0,2) + "/" +
                                        document.forms[0].fechaHasta.value.substring(3,5)+ "/" +
                                        document.forms[0].fechaHasta.value.substring(6,10);
                                        formulario.opcion.value="informeDiligencias";
                                        formulario.target="oculto";
                                        formulario.action="<%=request.getContextPath()%>/InformesRegistro.do";
                                        formulario.submit();
                                    }else return;
                                } else jsp_alerta("A",'<%=desc.getDescripcion("msjAnDistinto")%>');
                                }
                            }
                        }
                    }

                    function inicializar() {
                        if(document.all)
                        document.all.tabla1.style.marginTop = 0; //calcularPosTabla(document.all.tabla1);
                        else
                            document.getElementById("tabla1").style.marginTop = 0;
                        // var fecha = obtenerFechaString();
                        var fecha = '<%=fechaServidor%>';
                        document.forms[0].fechaDesde.value=fecha;
                        document.forms[0].fechaHasta.value=fecha;
                        document.forms[0].fechaDesde.focus();

                        habilitarImagenCal("calDesde",true);
                        habilitarImagenCal("calHasta",true);

                    }

                    function abrirInforme(nombre){

                        // A otra página que contiene el fichero PDF.
                        if (nombre == "NO EXISTE") {
                            jsp_alerta('A', '<%=desc.getDescripcion("msjNoDatos")%>');
                            }else if (!(nombre =='')) {
                            // PDFS NUEVA SITUACION
                            var sourc = "<%=request.getContextPath()%>/jsp/verPdf.jsp?opcion=null&nombre="+nombre;
                            ventanaInforme = window.open("<%=request.getContextPath()%>/jsp/mainVentana.jsp?source="+sourc,'ventanaInforme','width=1000px,height=750px,status='+ '<%=statusBar%>' + ',toolbar=no,resizable=yes');
                                ventanaInforme.focus();
                                // FIN PDFS NUEVA SITUACION

                                //ventanaInforme = window.open("/jsp/registro/ver_pdf.jsp?fichero='/pdf/"+nombre+".pdf'", "informe", "width=800px,height=600px,toolbar=no,resizable=yes");
                                //ventanaInforme.focus();
                            } else {
                            jsp_alerta('A','<%=desc.getDescripcion("msjNoPDF")%>');
                            }
                        }


                        function comprobarFecha(inputFecha) {
                            if (Trim(inputFecha.value)!='') {
                                if (!ValidarFechaConFormato(document.forms[0],inputFecha)){
                                    jsp_alerta("A",'<%=desc.getDescripcion("fechaNoVal")%>');
                                        inputFecha.focus();
                                        return false;
                                    }
                                }
                                return true;
                            }

        </SCRIPT>
    </HEAD>
    <body class="bandaBody" onload="javascript:{ pleaseWait('off'); 
       }">



        <div id="hidepage" style="position: absolute; top:150px; z-index:10; visibility: inherit;">
            <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
                <tr>
                    <td align="center" valign="middle">
                        <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                            <tr>
                                <td>
                                    <table width="349px" height="100%">
                                        <tr>
                                            <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                                <span><%=desc.getDescripcion("msjCargDatos")%></span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="width:5%;height:20%;"></td>
                                            <td class="imagenHide"></td>
                                            <td style="width:5%;height:20%;"></td>
                                        </tr>
                                        <tr>
                                            <td colspan="3" style="height:10%" ></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </div>



        <FORM name = "formulario" METHOD=POST target="_self">
            <input type="hidden" name="pdfFile" value="informeDiligencias">
            <input type="hidden" name="xslFile" value="diligencias">
            <input type="hidden" name="fechaInicio" value="">
            <input type="hidden" name="fechaFin" value="">
            <input type="hidden" name="opcion" value="">
            <input type="hidden" name="tipo" value="<%=t%>">

          <% if ('S' == t) {%>
             <div id="titulo" class="txttitblancoder"><%=desc.getDescripcion("infReg_rbDil" + t)%>&nbsp;&nbsp;&nbsp;</div>
          <% } else {%>
             <div id="titulo"  class="txttitblanco"><%=desc.getDescripcion("infReg_rbDil" + t)%></div>
          <% }%>
                <div class="contenidoPantalla">
                    <table id="tabla1" width="100%" cellpadding="2" cellspacing="2" align="center">
                        <tr>
                             <TD colspan="2" class="sub3titulo"><%=desc.getDescripcion("InfUniPer")%></TD>
                        </tr>
                        <tr>
                             <TD width="40%" class="etiqueta"><%=desc.getDescripcion("gEtiqFechaDesde")%>:</TD>
                             <TD width="60%" class="columnP">
                                    <INPUT TYPE="text" id="obligatorio" class="inputTxtFechaObligatorio" size="12" maxlength="10" name="fechaDesde" onkeyup="javascript:return SoloCaracteresFecha(this);" onblur="javascript:return comprobarFecha(this);" onfocus="this.select();">
                                    <A href="javascript:calClick(event);return false;" onClick="mostrarCalDesde(event);return false;"onblur="ocultarCalendarioOnBlur(event); return false;">
                                        <span class="fa fa-calendar" aria-hidden="true" style="border: 0; height: 17px; width: 25px;" name="calDesde" id="calDesde" ></span>
                                    </A>
                             </TD>
                        </tr>
                        <tr>
                             <TD class="etiqueta"><%=desc.getDescripcion("gEtiqFechaHasta")%>:</TD>
                             <TD class="columnP">
                                <INPUT TYPE="text"  class="inputTxtFecha" size="12" maxlength="10" name="fechaHasta"
                                       onkeyup = "javascript:return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFecha(this);"
                                       onfocus = "this.select();">
                                       &nbsp;
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalHasta(event);return false;">
                                    <span class="fa fa-calendar" aria-hidden="true" style="border: 0; height: 17px; width: 25px;" name="calHasta" id="calHasta"></span>
                                </A>
                             </TD>
                        </tr>
                        <tr>
                            <TD colspan="2" class="sub3titulo"><%=desc.getDescripcion("infOpcion")%></TD>
                        </tr>
                        <tr>
                            <TD class="etiqueta"> <%=desc.getDescripcion("etiq_escudo")%>:</TD>
                            <TD><INPUT TYPE="checkbox" NAME="escudo" value="0"></TD>
                       </tr>
                       <tr>
                            <TD class="etiqueta"> <%=desc.getDescripcion("gEtiqNombre")%>&nbsp;<%=desc.getDescripcion("tit_Org")%>:</TD>
                            <TD><INPUT TYPE="checkbox" NAME="nombre" value="0"></TD>
                       </tr>
                       <tr>
                            <TD class="etiqueta"><%=desc.getDescripcion("etiq_Idioma")%>:</TD>
                            <TD colspan="3">
                                <input type="hidden" name="idioma"/>
                                <input type="hidden" name="codIdioma"/>
                                <input type="text" class="inputTextoObligatorio" name="descIdioma" size="16" maxlength="16" readonly="true"/>
                                <a href="javascript:{}" style="text-decoration:none;" id="anchorIdioma" name="anchorIdioma">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonIdioma" style="cursor:hand;" 
                                          alt="<%=desc.getDescripcion("altDesplegable")%>" title="<%=desc.getDescripcion("altDesplegable")%>"></span>
                                </a>
                            </TD>
                       </tr>
                 </table>
                                                  
                
            <!-------------------------------------- BOTONES. ------------------------------------------>
               <div class="botoneraPrincipal">
                    <INPUT type= "button" class="botonGeneral" value=<%=desc.getDescripcion("gbImprimir")%> name="cmdImprimir" onClick="pulsarImprimir();"
                           alt="<%=desc.getDescripcion("toolTip_bImprimir")%>" title="<%=desc.getDescripcion("toolTip_bImprimir")%>">
                                  
                   <INPUT type= "button" class="botonGeneral" value=<%=desc.getDescripcion("gbSalir")%> name="cmdSalir"  onClick="pulsarSalir();"
                          alt="<%=desc.getDescripcion("toolTip_bVolver")%>" title="<%=desc.getDescripcion("toolTip_bVolver")%>">
                                    
                </div>
                </div>
        </FORM>


        <div id="popupcalendar" class="textoSuelto"></div>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <SCRIPT>

            var comboIdiomas = new Combo("Idioma");
            comboIdiomas.addItems(cod_idiomas,desc_idiomas);
            comboIdiomas.selectItem(indice_idioma_usuario);

            inicializar();
        </SCRIPT>
    </BODY>
</HTML>
