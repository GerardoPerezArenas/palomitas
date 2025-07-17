<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<html><head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Mantenimiento de Tipos de Vias</title>
        <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
        
        <%	UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 3;
            String css="";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                css=usuarioVO.getCss();
            }%>
            
            
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>">    
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript">
            var lista = new Array();
            var datosTiposVias = new Array();
            function inicializar(){
                window.focus();
                pleaseWait("off");
                
                    
                cargaTablaTiposVias();
            }
            function cargaTablaTiposVias(){
  <%MantenimientosTercerosForm bForm = (MantenimientosTercerosForm) session.getAttribute("MantenimientosTercerosForm");
            Vector listaTiposVias = bForm.getListaTipoVias();
            int lengthTiposVias = listaTiposVias.size();
            int i = 0;%>
                var j=0;
    <%for (i = 0; i < lengthTiposVias; i++) {
                GeneralValueObject tiposVias = (GeneralValueObject) listaTiposVias.get(i);%>
                    datosTiposVias[j] = ["<%=(String) tiposVias.getAtributo("codTipoVia")%>",
                        "<%=(String) tiposVias.getAtributo("abreviatura")%>",
                        "<%=(String) tiposVias.getAtributo("descTipoVia")%>"];
                        lista[j] = datosTiposVias[j];
                        j++;
                        <%}%>
                        tablaTiposVias.lineas = lista;
                        refresca(tablaTiposVias);
                    }
                    function limpiarFormulario(){
                        tablaTiposVias.lineas = new Array();
                        refresca(tablaTiposVias);
                        limpiarInputs();
                    }
                    function limpiarInputs(){
                        var vector = [document.forms[0].codTipoVia];
                        habilitarGeneral(vector);
                        document.forms[0].codTipoVia.value = "";
                        document.forms[0].codTipoViaAntiguo.value = "";
                        document.forms[0].abreviatura.value = "";
                        document.forms[0].descTipoVia.value = "";
                    }
                    function pulsarLimpiar() {
                        limpiarInputs();
                        if(tablaTiposVias.selectedIndex != -1 ) {
                            tablaTiposVias.selectLinea(tablaTiposVias.selectedIndex);
                            tablaTiposVias.selectedIndex = -1;
                        }
                    }
                    function validarCamposRejilla(){
                        var codTipoVia = document.forms[0].codTipoVia.value;
                        if(codTipoVia!="") return true;
                        return false;
                    }
                    function noEsta(indice){
                        var cod = document.forms[0].codTipoVia.value;
                        for(i=0;(i<lista.length);i++){
                            if(i!=indice){
                                if((lista[i][0]) == cod)
                                    return false;
                            }
                        }
                        return true;
                    }
                    function pulsarSalir(){
                        document.forms[0].target = "mainFrame";
                        document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
                        document.forms[0].submit();
                    }
                    function pulsarEliminar() {
                        if(tablaTiposVias.selectedIndex != -1) {
                            if(jsp_alerta("C",'<%=descriptor.getDescripcion("desElimTipoVia")%>') ==1) {
                                var vector = [document.forms[0].codTipoVia];
                                habilitarGeneral(vector);
                                document.forms[0].identificador.value = lista[tablaTiposVias.selectedIndex][0];
                                document.forms[0].opcion.value = "eliminar";
                                document.forms[0].target = "oculto";
                                document.forms[0].action = "<%=request.getContextPath()%>/terceros/mantenimiento/TiposVias.do";
                                document.forms[0].submit();
                                deshabilitarGeneral(vector);
                                limpiarInputs();
                                if(tablaTiposVias.selectedIndex != -1 ) {
                                    tablaTiposVias.selectLinea(tablaTiposVias.selectedIndex);
                                    tablaTiposVias.selectedIndex = -1;
                                }
                            }
                        }
                        else jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoSelecFila")%>");
                        }
                        function noEliminarTVia() {
                            jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoElimTVia")%>");
                            }
                            function pulsarModificar(){
                                var indice = tablaTiposVias.selectedIndex;
                                if(indice!=-1){
                                    if(noEsta(indice)){
                                        var vector = [document.forms[0].codTipoVia];
                                        habilitarGeneral(vector);
                                        document.forms[0].opcion.value = "modificar";
                                        document.forms[0].target = "oculto";
                                        document.forms[0].action = "<%=request.getContextPath()%>/terceros/mantenimiento/TiposVias.do";
                                        document.forms[0].submit();
                                        habilitarGeneral(vector);
                                    }else
                                    jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>");
                                    }else
                                    jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoSelecFila")%>");
                                    }
                                    function pulsarAlta(){
                                        var cod = document.forms[0].codTipoVia.value;
                                        var existe = 0;
                                        if (validarCamposRejilla()){
                                            for(var i=0; (i < lista.length) && (existe == 0); i++){
                                                if((lista[i][0]) == cod)
                                                    existe = 1;
                                            }
                                            if(existe == 0){
                                                document.forms[0].opcion.value = "alta";
                                                document.forms[0].target = "oculto";
                                                document.forms[0].action = "<%=request.getContextPath()%>/terceros/mantenimiento/TiposVias.do";
                                                document.forms[0].submit();
                                            }else
                                            jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>");
                                            }else
                                            jsp_alerta("A", "<%=descriptor.getDescripcion("msjObligTodos")%>");
                                            }
                                            
                                            function recuperaDatos(lista2) {
                                                limpiarInputs();
                                                lista = lista2;
                                                tablaTiposVias.lineas=lista;
                                                refresca(tablaTiposVias);
                                            }
</script></head>
<body class="bandaBody" onload="inicializar();">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
<form action="" name="formulario" method="post">
    <input  type="hidden"  name="opcion" id="opcion">
    <input  type="hidden"  name="identificador" value="">
    <input  type="hidden"  name="codTipoViaAntiguo" value="">

    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiq_MantTiposVias")%></div>
    <div class="contenidoPantalla">
        <table style="width:100%">
            <tr>
                <td >
                    <div id="tabla"></div>
                </td>
            </tr>
            <TR>
                <TD>
                    <table width="100%" rules="none"  border="0" cellpadding="1" cellspacing="0">
                        <tr style="text-align:center">
                            <td style="width: 25%">
                                <input type="text" class="inputTextoObligatorio" id="obligatorio" name="codTipoVia" size="30" maxlength="5" onkeyup="return SoloDigitosNumericos(this);">
                            </td>
                            <td style="width: 25%">
                                <input name="abreviatura" type="text" class="inputTexto" size="30" maxlength="5" onblur="return xAMayusculas(this);">
                            </td>
                            <td style="width: 50%">
                                <input name="descTipoVia" type="text" class="inputTexto" size="70" maxlength="60" onblur="return xAMayusculas(this);">
                            </td>
                        </tr>
                    </table>
                </TD>
            </TR>                                                    
        </TABLE>
        <div class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbAlta")%>'
                                       name="cmdAlta" onClick="pulsarAlta();" accesskey="A">
            <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbModificar")%>'
                                       name="cmdModificar" onClick="pulsarModificar();" accesskey="M">
            <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbEliminar")%>'
                                       name="cmdEliminar" onClick="pulsarEliminar();" accesskey="E">
            <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbLimpiar")%>'
                                       name="cmdLimpiar" onClick="pulsarLimpiar();" accesskey="L">
            <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbSalir")%>'
                                       name="cmdSalir" onClick="pulsarSalir();" accesskey="S">
        </div>            
    </div>            
</form>
<script type="text/javaScript">
        var tablaTiposVias = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tabla"));
            tablaTiposVias.addColumna("225","center",'<%= descriptor.getDescripcion("gEtiq_Codigo")%>');
                tablaTiposVias.addColumna("225","center",'<%= descriptor.getDescripcion("gEtiq_Abrev")%>');
                    tablaTiposVias.addColumna("450","left",'<%= descriptor.getDescripcion("gEtiq_Descrip")%>');
                        tablaTiposVias.displayCabecera=true;
                        tablaTiposVias.displayTabla();
                        
                        function refresca(tabla){
                            tabla.displayTabla();
                        }
                        function rellenarDatos(tableName,rowID){
                            var i = rowID;
                            limpiarInputs();
                            if(i>=0){
                                var vector = [document.forms[0].codTipoVia];
                                deshabilitarGeneral(vector);
                                document.forms[0].codTipoVia.value = lista[i][0];
                                document.forms[0].abreviatura.value = lista[i][1];
                                document.forms[0].descTipoVia.value = lista[i][2];
                                document.forms[0].codTipoViaAntiguo.value = lista[i][0];
                            }
                        }
                        document.onmouseup = checkKeys;
                        function checkKeysLocal(evento, tecla){
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
                            keyDel(aux);
                            if (tecla == 38) upDownTable(tablaTiposVias,lista,tecla);
                            if (tecla == 40) upDownTable(tablaTiposVias,lista,tecla);
                            if (tecla == 13) pushEnterTable(tablaTiposVias,lista);
                        }
                        </script>
    </body>
</html>
