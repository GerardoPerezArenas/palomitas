<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

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
            String tipo = "E"; // Sólo se trabaja con anotaciones de entrada
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

            /** Como código de aplicación se utiliza el 1 que es el de registro para las etiquetas, puesto que son las mismas y así no hay que dar de alta unas nuevas para este módulo **/
        %>

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript">

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

       var informesDireccion = false;
       var ventanaPadre = null;
       
        if (self.parent.opener) 
            if (self.parent.opener.xanelaAuxiliarArgs) {
               if (self.parent.opener.xanelaAuxiliarArgs['informesDireccion']) informesDireccion = true;
               ventanaPadre = self.parent.opener.xanelaAuxiliarArgs['ventanaPadre'];
            }

       var codigoError  = '<%=(String) session.getAttribute("CODIGO_ERROR_LISTADO_LANBIDE") %>';
       
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



    /** Se encarga de mostrar el mensaje de error correspondiente si es necesario */
    function tratarMensajeError(){
        var error = false;
        var etiquetaError = "";
        
         if(codigoError=="ERROR_TECNICO_VERIFICACION_OFICINA_REGISTRO"){
             error = true;             
             etiquetaError = "<%=descriptor.getDescripcion("msgErrVerificarPermOfRegistro")%>";
         }
         else        
         if(codigoError=="NO_PERMISO_UNIDAD_TIPO_REGISTRO"){
             error = true;
             //etiquetaError = "Error: No dispone de ningún permiso sobre una unidad organizativa de registro. Consulte con el administrador.";
             etiquetaError = "<%=descriptor.getDescripcion("msgNoPermisoUndRegistro")%>";
         }
         else
         if(codigoError=="PERMISO_MAS_UNA_UNIDAD_TIPO_REGISTRO"){
             error = true;
             //etiquetaError = "Error: Dispone de permisos sobre más de una unidad organizativa de registro. Consulte con el administrador.";
             etiquetaError = "<%=descriptor.getDescripcion("msgPermisoMasUndRegistro")%>";
         }
         else
         if(codigoError=="NO_PERMISO_NINGUNA_OFICINA_REGISTRO"){
             error = true;
             //etiquetaError ="Error: No dispone de permiso sobre una oficina de registro. Consulte con el administrador.";
             etiquetaError = "<%=descriptor.getDescripcion("msgNoPermisoOfiRegistro")%>";
         }
         else
         if(codigoError=="PERMISO_MAS_UNA_OFICINA_REGISTRO"){
             error = true;
             //etiquetaError = "Error: Dispone de permisos sobre más de una oficina de registro. Consulte con el administrador.";
             etiquetaError = "<%=descriptor.getDescripcion("msgPermisoMasOfiRegistro")%>";
         }

         if(error){            
            
            // Se desactivan los campos pertinentes del formulario
            document.forms[0].fechaDesde.disabled = true;
            document.forms[0].fechaHasta.disabled = true;
            deshabilitarImagenCal("calDesde",true);
            deshabilitarImagenCal("calHasta",true);
            document.forms[0].regDesde.disabled = true;
            document.forms[0].regHasta.disabled = true;
            //document.forms[0].orden.disabled = true;
            document.forms[0].pagDesde.disabled = true;
            document.forms[0].numDesde.disabled = true;
            document.forms[0].escudo.disabled = true;            
            document.forms[0].nombre.disabled = true;            
            comboIdiomas.deactivate();
            
            var radio = document.getElementsByName("interopcion");
            for(i=0;i<radio.length;i++){
                radio[i].disabled = true;                
            }

            jsp_alerta("A",etiquetaError);
         }
         return error;
    }



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
    var resultado = jsp_alerta("",'<%= descriptor.getDescripcion("g_confirmSalirApp")%>');
	if (resultado == 1){
		document.forms[0].target = '_top';
      	document.forms[0].action = '<%=request.getContextPath()%>/SalirApp.do?app=<%= usuario.getAppCod()%>';
      	document.forms[0].submit();
	}
}

            function pulsarImprimir() { 
                var msjError = tratarMensajeError();                
                if(!msjError){
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
                                    formulario.opcion.value = "informe";
                                    formulario.target = "oculto";                                    
                                    formulario.action = "<%=request.getContextPath()%>/ListadoEntradasOficinaRegistro.do";                                    
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
                                        formulario.opcion.value = "informe";
                                        formulario.target = "oculto";                                        
                                        formulario.action = "<%=request.getContextPath()%>/ListadoEntradasOficinaRegistro.do";                                        
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
                }// pulsarImprimir

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

    <body class="bandaBody" onload="javascript:{ tratarMensajeError();pleaseWait('off'); 
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
            <input type="hidden" name="codOficinaRegistroUsuario" value="<%=session.getAttribute("cod_oficina_registro_usuario")%>"/>
            <input type="hidden" name="codUnidadRegistroUsuario" value="<%=session.getAttribute("cod_unidad_registro_usuario")%>"/>
            

            <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titListadoOfiRegistro")%></div>
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
                        <TD colspan="2"style="width:100%;" class="sub3titulo"><%=descriptor.getDescripcion("infOpcion")%></TD>
                    </TR>
                    <%--
                    <TR>
                        <TD class="etiqueta"><%=descriptor.getDescripcion("gEtiq_ordenReg")%>:</TD>
                        <TD><INPUT TYPE="checkbox" NAME="orden" value="1"></TD>
                    </TR>
                    --%>

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
                        <TD><INPUT TYPE="checkbox" NAME="escudo" value="0"></TD>                                                                                
                    </TR>
                    <TR>
                        <TD class="etiqueta"><%=descriptor.getDescripcion("gEtiqNombre")%>
                            &nbsp;<%=descriptor.getDescripcion("gEtiqAy")%>:
                        </TD>
                        <TD><INPUT TYPE="checkbox" NAME="nombre" value="0"></TD>
                    </TR>
                    <TR>
                        <TD class="etiqueta"><%=descriptor.getDescripcion("etiq_Idioma")%>:</TD>
                        <TD>
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
                         <TD class= "etiqueta"><%=descriptor.getDescripcion("etiq_interopcion")%>:</TD>

                        <TD class="textoSuelto">
                         <input type="radio" id="interopcion" name="interopcion" class="textoSuelto" value="op1" onclick="" checked=true><%=descriptor.getDescripcion("etiq_interop1")%>
                        <input type="radio" id="interopcion" name="interopcion" class="textoSuelto" value="op2" onclick=""><%=descriptor.getDescripcion("etiq_interop2")%>
                        <input  type="radio" id="interopcion" name="interopcion" class="textoSuelto" value="op3" onclick=""><%=descriptor.getDescripcion("etiq_interop3")%>
                        </TD>
                    </TR>
                </TABLE>
        <div class="botoneraPrincipal">
             <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbImprimir")%> name="cmdImprimir" onClick="pulsarImprimir();"
                     alt="<%=descriptor.getDescripcion("toolTip_bImprimirLibro")%>" title="<%=descriptor.getDescripcion("toolTip_bImprimirLibro")%>">
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
