<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.FusionDivisionForm" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<html>
    <head>
        <title>Fusion y Division de secciones</title>
        <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 3;
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
            }
        %>
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        
        <!-- Ficheros JavaScript -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <link rel="stylesheet" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>" type="text/css">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript">
            // VARIABLES GLOBALES
    <%
            FusionDivisionForm fdForm = (FusionDivisionForm) session.getAttribute("FusionDivisionForm");
    %>
        var lista = new Array();
        var codDistritos = new Array();
        var descDistritos = new Array();
        var codSeccionesOrigen = new Array();
        var descSeccionesOrigen = new Array();
        var letraSeccionesOrigen = new Array();
        var codSeccionesDestino = new Array();
        var descSeccionesDestino = new Array();
        var letraSeccionesDestino = new Array();
        
        // INICIALIZACIÓN Y CARGA DE DATOS
        function inicializar(){
            var argumentos = self.parent.opener.xanelaAuxiliarArgs;
           
                
            recuperarDatosIniciales();
            document.forms[0].tipoOperacion.value = argumentos;
        }
        
        function recuperarDatosIniciales(){
    <%
            Vector listaDistritos = fdForm.getListaDistritos();
            int lengthDistritos = listaDistritos.size();
            int i = 0;
            String codDistritos = "";
            String descDistritos = "";
            for (i = 0; i < lengthDistritos; i++) {
                if (i == lengthDistritos - 1) {
                    GeneralValueObject usuarios = (GeneralValueObject) listaDistritos.get(i);
                    codDistritos += "\"" + usuarios.getAtributo("codDistrito") + "\"";
                    descDistritos += "\"" + usuarios.getAtributo("descDistrito") + "\"";
                } else {
                    GeneralValueObject usuarios = (GeneralValueObject) listaDistritos.get(i);
                    codDistritos += "\"" + usuarios.getAtributo("codDistrito") + "\",";
                    descDistritos += "\"" + usuarios.getAtributo("descDistrito") + "\",";
                }
            }
    %>
        codDistritos = [<%=codDistritos%>];
            descDistritos = [<%=descDistritos%>];
                // CARGAR COMBOS
                comboDistritoOrigen.addItems(codDistritos,descDistritos);
                comboDistritoDestino.addItems(codDistritos,descDistritos);
            }
            
            function cargarSecciones(opcion){
                var codDistrito = "";
                if(opcion=="cargarSeccionesOrigen")
                    codDistrito = document.forms[0].codDistritoOrigen.value;
                else
                    codDistrito = document.forms[0].codDistritoDestino.value;
                document.forms[0].codDistrito.value = codDistrito;
                document.forms[0].opcion.value=opcion;
                document.forms[0].target="oculto";
                document.forms[0].action="<%=request.getContextPath()%>/territorio/FusionDivisionSecciones.do";
                document.forms[0].submit();
            }
            
            /**************  FUNCIONES PARA LA CARGA DE LOS CALENDARIOS ***********************/
            function mostrarCalendario(img,campoFecha,evento){
                if(window.event) evento = window.event;
                var indice = document.getElementById(img).src.indexOf('fa-calendar');
                if (indice!=-1)
                    showCalendar('forms[0]',campoFecha,null,null,null,'',img,'',null,null,null,
                        null,null,null,null,'',evento);
                }//de la funcion
                
                //Funcion para la verificación de un campo fecha
                function comprobarFecha(inputFecha){
                    if(Trim(inputFecha.value)!=''){
                        if(!ValidarFechaConFormato(document.forms[0],inputFecha)){
                            jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
                                inputFecha.focus();
                                return false;
                            }
                        }
                        return true;
                    }//de la funcion
                    
                    function actualizarTablaProcesos(){
                        tablaProcesos.lineas = lista;
                        refresca(tablaProcesos);
                    }
                    
                    // FUNCIONES DE PULSACION DE BOTONES
                    function pulsarAceptar(){
                        if(validarFormulario()){
                            document.forms[0].opcion.value="insertarProceso";
                            document.forms[0].target="oculto";
                            document.forms[0].action="<%=request.getContextPath()%>/territorio/FusionDivisionSecciones.do";
                            document.forms[0].submit();                            
                        }
                    }
                    
                    function pulsarCancelar(){
                        self.parent.opener.retornoXanelaAuxiliar();
                    }
                    
                    function procesoCreado(){
                        self.parent.opener.retornoXanelaAuxiliar();
                    }
    </script>

</head>
<body class="bandaBody" onload="inicializar()">
    <form action="" method="post" name="formulario" id="formulario">
        <input type="hidden" name="opcion" value="">
        <input type="hidden" name="codDistrito" value="">
        <input type="hidden" name="tipoOperacion" value="">
        <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titFusDivSecc")%></div>
        <div class="contenidoPantalla">
            <table style="width: 100%">
                <tr>
                    <td>												  	
                        <table width="100%">
                            <tr>
                                <td class="etiqueta" style="width: 22%"><%=descriptor.getDescripcion("etiqDescProc")%>:</td>
                                <td>
                                    <input class="inputTextoObligatorio" name="descripcion" 
                                           type="text" size="95" maxlength="25"
                                           onblur="return xAMayusculas(this);">
                                </td>
                            </tr>
                            <tr>
                                <td style="width: 22%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqFecha")%>:</td>
                                <td >
                                    <input class="inputTxtFechaObligatorio" type="text" size="11"
                                           maxlength="10" name="fecha"
                                           onkeyup = "return SoloCaracteresFecha(this);"
                                           onblur = "javascript:return comprobarFecha(this);"
                                           onfocus = "this.select();">
                                    <a name="anchor" id="anchor"
                                       href="javascript:calClick(event);return false;"
                                       onClick="mostrarCalendario('Desde','fecha',event);return false;"
                                       style="text-decoration:none;">
                                        <span class="fa fa-calendar" aria-hidden="true" style="border: 0px" name="Desde" id="Desde"
                                            ></span>
                                    </a>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table width="100%" border="0" cellspacing="4" cellpadding="0">
                            <tr>
                                <td style="width: 48%"> 
                                    <table class="cuadroFondoBlanco" style="padding-top: 5px; padding-bottom: 5px" cellspacing="2">
                                        <tr>
                                            <td class="sub3titulo"><%=descriptor.getDescripcion("etiqOrig")%></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                        <td class="etiqueta" style="width: 20%"><%=descriptor.getDescripcion("gEtiq_Distrito")%>: </td>
                                                        <td>
                                                            <input name="codDistritoOrigen" type="text" class="inputTextoObligatorio"
                                                                   id="codDistritoOrigen" size="3">
                                                            <input name="descDistritoOrigen" type="text" class="inputTextoObligatorio"
                                                                   id="descDistritoOrigen" style="width:200" readonly>
                                                            <a id="anchorDistritoOrigen" name="anchorDistritoOrigen" href="">
                                                                <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                                                                     id="botonDistritoOrigen" name="botonDistritoOrigen"
                                                                    
                                                                     style="cursor:hand;"></span>
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                        <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Seccion")%>: </td>
                                                        <td>
                                                            <input name="codSeccionOrigen" type="text" class="inputTextoObligatorio"
                                                                   id="codSeccionOrigen" size="3">
                                                            <input name="descSeccionOrigen" type="text" class="inputTextoObligatorio"
                                                                   id="descSeccionOrigen" style="width:200" readonly>
                                                            <a id="anchorSeccionOrigen" name="anchorSeccionOrigen" href="">
                                                                <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                                                                     id="botonSeccionOrigen" name="botonSeccionOrigen"
                                                                    
                                                                     style="cursor:hand;"></span>
                                                            </a>
                                                            <input name="letraOrigen" type="hidden" class="inputTexto"
                                                                   id="letraOrigen" readonly>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table> 
                                </td>
                                <td style="width: 48%"> 
                                    <table class="cuadroFondoBlanco" style="padding-top: 5px; padding-bottom: 5px" cellspacing="2">
                                        <tr>
                                            <td class="sub3titulo"><%=descriptor.getDescripcion("etiqDest")%></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                        <td class="etiqueta" style="width: 20%"><%=descriptor.getDescripcion("gEtiq_Distrito")%>: </td>
                                                        <td>
                                                            <input name="codDistritoDestino" type="text" class="inputTextoObligatorio"
                                                                   id="codDistritoDestino" size="3">
                                                            <input name="descDistritoDestino" type="text" class="inputTextoObligatorio"
                                                                   id="descDistritoDestino" style="width:200px" readonly>
                                                            <a id="anchorDistritoDestino" name="anchorDistritoDestino" href="">
                                                                <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                                                                     id="botonDistritoDestino" name="botonDistritoDestino"
                                                                    
                                                                     style="cursor:hand;"></span>
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                        <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Seccion")%>: </td>
                                                        <td>
                                                            <input name="codSeccionDestino" type="text" class="inputTextoObligatorio"
                                                                   id="codSeccionDestino" size="3">
                                                            <input name="descSeccionDestino" type="text" class="inputTextoObligatorio"
                                                                   id="descSeccionDestino" style="width:200px" readonly>
                                                            <a id="anchorSeccionDestino" name="anchorSeccionDestino" href="">
                                                                <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                                                                     id="botonSeccionDestino" name="botonSeccionDestino"
                                                                    
                                                                     style="cursor:hand;"></span>
                                                            </a>
                                                            <input name="letraDestino" type="hidden" class="inputTexto"
                                                                   id="letraDestino" readonly>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <div class="botoneraPrincipal">
                <input name="botonAceptar" type="button" class="botonGeneral" id="botonAceptar"
                                           accesskey="A" value="<%=descriptor.getDescripcion("gbAceptar")%>"
                                           onclick="pulsarAceptar();">
                <input name="botonCancelar" type="button" class="botonGeneral" id="botonCancelar"
                                           accesskey="C" value="<%=descriptor.getDescripcion("gbCancelar")%>"
                                           onclick="pulsarCancelar();">
            </div>            
        </div>            
</form>


<script type="text/javascript">
            // COMBOS
            var comboDistritoOrigen = new Combo("DistritoOrigen");
            var comboDistritoDestino = new Combo("DistritoDestino");
            var comboSeccionOrigen = new Combo("SeccionOrigen");
            var comboSeccionDestino = new Combo("SeccionDestino");
            var auxCombo = "";
            comboDistritoOrigen.change = function(){
                auxCombo = "comboSeccionOrigen";
                cargarSecciones("cargarSeccionesOrigen");
            }
            
            comboDistritoDestino.change = function(){
                auxCombo = "comboSeccionDestino";
                cargarSecciones("cargarSeccionesDestino");
            }
            
            comboSeccionOrigen.change = function(){
                var i = comboSeccionOrigen.selectedIndex;
                if(i>=0){
                    document.forms[0].letraOrigen.value = letraSeccionesOrigen[i];
                }
            }
            
            comboSeccionDestino.change = function(){
                var i = comboSeccionDestino.selectedIndex;
                if(i>=0){
                    document.forms[0].letraDestino.value = letraSeccionesDestino[i];
                }
            }
            
            
            function cargarComboBox(cod, des){
                eval(auxCombo+".addItems(cod,des)");
            }
        </script>
    </body>
</html>
