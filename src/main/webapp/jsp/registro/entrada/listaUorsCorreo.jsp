<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.agora.technical.ConstantesDatos"%>
<%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = ConstantesDatos.APP_REGISTRO_ENTRADA_SALIDA;
            String css = "";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
                css = usuarioVO.getCss();
            }
%>
<jsp:useBean id="descriptor" scope="request"
             class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
             type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor" property="idi_cod" value="<%=idioma%>" />
<jsp:setProperty name="descriptor" property="apl_cod" value="<%=apl%>" />
<html>
    <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
        <title>Unidades a notificar</title>
        <jsp:include page="/jsp/plantillas/Metas.jsp"/>
        
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
        <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript">
            
            
            // Los combos añaden una posicion vacia al principio por lo
            // que los datos de los vectores comienzan en el indice 1 del combo.
            var cod_interno_unidadRegistro = new Array();
            var cod_unidadRegistro = new Array();
            var desc_unidadRegistro = new Array();
            
            var codigosUorsCorreo = new Array();
            var descsUorsCorreo = new Array();
            
            //// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
            
            /* Realiza las funciones de carga de la pagina */
            function inicializar(){
                var args = self.parent.opener.xanelaAuxiliarArgs;
                // Combo de uors
                cod_unidadRegistro = args[0];
                cod_interno_unidadRegistro = args[1];
                desc_unidadRegistro = args[2];
                cargarCombo();
                // Lista de uors
                codigosUorsCorreo = args[3];
                // Construimos lista de descripciones de uors para la tabla
                descsUorsCorreo = construirListaDescripciones(codigosUorsCorreo);              
                cargaTablaUors();
                // En modo consulta desactivamos combo y botones
                var consulta = args[4];
                if (consulta) {
                    deshabilitarGeneral(new Array(document.forms[0].cmdSeleccionar, 
                        document.forms[0].cmdEliminar));
                    comboUnidadRegistro.deactivate();
                }
            }
            
            //// FUNCIONES DE LOS BOTONES
            
            /* Añade la uor seleccionada en el combo a la lista de uors */
            function pulsarSeleccionar() {
                if (comboUnidadRegistro.selectedIndex > 0) {
                    var codigo = cod_interno_unidadRegistro[comboUnidadRegistro.selectedIndex - 1];
                    if (!existeCodigo(codigo)) {
                        codigosUorsCorreo[codigosUorsCorreo.length] = codigo;
                        descsUorsCorreo = construirListaDescripciones(codigosUorsCorreo);                
                        cargaTablaUors();               
                    } else {
                        jsp_alerta('A',"<str:escape><%=descriptor.getDescripcion("msjCodExiste")%></str:escape>");
                    }        
                }
                comboUnidadRegistro.selectItem(0);
            }
            
            /* Elimina de la lista de uors la uor seleccionada en la tabla */
            function pulsarEliminar() {
                if (tablaUors.selectedIndex == -1) {
                    jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                    } else {
                    var eliminada = tablaUors.selectedIndex;
                    listaAux = new Array();
                    var j=0;
                    for (i=0; i<codigosUorsCorreo.length; i++) {
                        if (i!=eliminada) {
                            listaAux[j] = codigosUorsCorreo[i];
                            j++;
                        }
                    }
                    codigosUorsCorreo = listaAux;
                    descsUorsCorreo = construirListaDescripciones(codigosUorsCorreo);                
                    cargaTablaUors();         
                }
            }
            
            /* Cierra la ventana de dialogo y devuelve la lista de uors */
            function pulsarSalir() {
                self.parent.opener.retornoXanelaAuxiliar(codigosUorsCorreo);
            }
            
            //// FUNCIONES AUXILIARES
            
            /* Construye la lista de descripciones para mostrar en la tabla */
            function construirListaDescripciones(codigos) {
                descs = new Array();
                for(var i=0; i<codigos.length; i++) {
                    descs[i] = [buscarDescripcion(codigos[i])];
                }
                return descs;
            }
            
            /* Busca la descripcion de una UOR por su codigo interno */
            function buscarDescripcion(codigo) {
                for(var i=0; i<cod_interno_unidadRegistro.length; i++) {
                    if (cod_interno_unidadRegistro[i] == codigo) return desc_unidadRegistro[i];
                }
                return ('ERROR: CODIGO DE UOR DESCONOCIDO');
            }
            
            /* Devuelve 'true' si existe el codigo pasado en la lista de uors y 'false' en otro caso */
            function existeCodigo(codigo) {
                for(var i=0; i<codigosUorsCorreo.length; i++) {
                    if (codigosUorsCorreo[i] == codigo) return true;
                }
                return false;
            }
            
        </script>
    </head>
    
<body class="bandaBody" onload="inicializar();">
<form name="formulario" method="post" action="">
    <input type="hidden" name="opcion" id="opcion"> 
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("etiqUnidNotificar")%></div>
    <div class="contenidoPantalla">
    <table style="width:100%;">
        <!-- Tabla de uors -->
        <tr>
            <td align="left" id="tabla"></td>
        </tr>
        <!-- Combo de uors -->
        <TR >
            <TD>
                <TABLE style="text-align: center; padding-top: 5px" width="100%" border="0" cellspacing="0" cellpadding="0">
                    <TR>
                        <TD width="80%" valign="top">
                            <input type="hidden" name="unidadTram"/>
                            <input type="text" class="inputTexto" name="codUnidadTram" size="7" maxlength="6"/>
                            <input type="text" class="inputTexto"  name="descUnidadTram" style="width:350px;height:17" readonly="true"/>
                            <A href="javascript:{}" style="text-decoration:none;" id="anchorUnidadTram" name="anchorUnidadTram">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonUnidadTram" style="cursor:hand;"></span>
                            </A>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
    </table>
    <div class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbSeleccionar")%>' name="cmdSeleccionar" 
               onClick="pulsarSeleccionar();">
        <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbEliminar")%>'
               name="cmdEliminar" onClick="pulsarEliminar();" accesskey="E">
        <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbSalir")%>'
               name="cmdSalir" onClick="pulsarSalir();" accesskey="S">
    </div>   
</div>   
</form>
<script type="text/javascript">
            // Tabla de uors seleccionadas
            var tablaUors = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
            
            tablaUors.addColumna('450','center',"<str:escape><%=descriptor.getDescripcion("etiqUnidsNotif")%></str:escape>");
                tablaUors.displayCabecera=true;
                tablaUors.lineas = new Array();
                tablaUors.displayTabla();
                
                function cargaTablaUors() { 
                    tablaUors.lineas=descsUorsCorreo;
                    tablaUors.displayTabla();
                }
                
                // Combo de unidades organicas
                var comboUnidadRegistro = new Combo("UnidadTram");
                
                function cargarCombo() {
                    comboUnidadRegistro.addItems(cod_unidadRegistro,desc_unidadRegistro);
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


function checkKeysLocal(evento,tecla) {
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else{
        teclaAuxiliar = evento.which;
    }


	if (teclaAuxiliar == 1){
           if (comboUnidadRegistro.base.style.visibility == "visible" && isClickOutCombo(comboUnidadRegistro,coordx,coordy)) setTimeout('comboUnidadRegistro.ocultar()',20);
        }
        if (teclaAuxiliar == 9){
            comboUnidadRegistro.ocultar();
        }
}
        </script>
    </body>
</html>

