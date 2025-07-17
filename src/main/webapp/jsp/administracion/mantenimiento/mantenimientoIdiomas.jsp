<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Mantenimiento de Tipos de Idiomas</title>
<%
    UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int idioma = 1;
    int apl = 3;
    String css = "";
    if (session.getAttribute("usuario") != null) {
        usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
        apl = usuarioVO.getAppCod();
        idioma = usuarioVO.getIdioma();
        css = usuarioVO.getCss();
    }
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");

%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
<script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript">
var lista = new Array();
var datosIdiomas = new Array();
var vectorCamposRejilla = ['codigo','clave','descripcion'];
            
// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
function inicializar(){
    window.focus();
    cargaTablaIdiomas();
}

function cargaTablaIdiomas(){

     <%
        MantenimientosAdminForm bForm = (MantenimientosAdminForm) session.getAttribute("MantenimientosAdminForm");
        Vector listaIdiomas = bForm.getListaIdiomas();
        int lengthIdiomas = listaIdiomas.size();
        int i = 0;
       %>

var j=0;

     <%
for (i = 0; i < lengthIdiomas; i++) {
    GeneralValueObject idiomas = (GeneralValueObject) listaIdiomas.get(i);
             %>

                 datosIdiomas[j] = ['<%=(String) idiomas.getAtributo("codigo")%>',
                    '<%=(String) idiomas.getAtributo("clave")%>', '<%=(String) idiomas.getAtributo("descripcion")%>'];
                     lista[j] = datosIdiomas[j];
                     j++;
                     <%}%>
                     tablaIdiomas.lineas = lista;
                     refresca(tablaIdiomas);
                 }

                 // FUNCIONES DE LIMPIEZA DE CAMPOS
                 function limpiarFormulario(){
                     tablaIdiomas.lineas = new Array();
                     refresca(tablaIdiomas);
                     limpiarCamposRejilla();
                 }

                 function limpiarCamposRejilla(){
                     limpiar(vectorCamposRejilla);
                     var vector = [document.forms[0].codigo];
                     habilitarGeneral(vector);
                 }

                 function pulsarLimpiar() {
                     tablaIdiomas.selectLinea(-1);
                     limpiarCamposRejilla();
                 }

                 // FUNCIONES DE PULSACION DE BOTONES
                 function pulsarSalir(){

                     document.forms[0].target = "mainFrame";
                     document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
                     document.forms[0].submit();
                 }

                 function pulsarEliminar() {
                     var vector = [document.forms[0].codigo];
                     habilitarGeneral(vector);
                     if(filaSeleccionada(tablaIdiomas)) {
                         document.forms[0].identificador.value = document.forms[0].codigo.value;
                         document.forms[0].opcion.value = 'eliminar';
                         document.forms[0].target = "oculto";
                         document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Idiomas.do';
                         document.forms[0].submit();
                         limpiarCamposRejilla();
                     }else 
                     jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                     }

 function asignarIdiomaUsuarios() {
     if (jsp_alerta('C','<%=descriptor.getDescripcion("msjUsuarioConIdioma")%>')) {
         var source = "<%=request.getContextPath()%>/jsp/administracion/mantenimiento/listaIdiomas.jsp?opcion=cargar";
        abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/administracion/mainVentana.jsp?source="+source,null,
	'width=780,height=380,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                            document.forms[0].nuevoCodIdioma.value = datos[0];
                            document.forms[0].codIdiomaEliminar.value = datos[1];
                            document.forms[0].opcion.value = "actualizarIdiomaUsuarios";
                            //document.forms[0].target = "oculto";
                            document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Idiomas.do';
                            document.forms[0].submit();
                            limpiarCamposRejilla();
                        }
                    });
                }
     }

     function pulsarModificar(){
         if(filaSeleccionada(tablaIdiomas)){
             if(validarCamposRejilla()){			
                 document.forms[0].identificador.value = document.forms[0].codigo.value;
                 document.forms[0].opcion.value = 'modificar';
                 document.forms[0].target = "oculto";
                 document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Idiomas.do';
                 document.forms[0].submit();
                 limpiarCamposRejilla();
       
             }else  jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
          }else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
      }

             function pulsarAlta(){
                 if (validarCamposRejilla()){
                     if(noEsta(-1)){
                         document.forms[0].opcion.value = 'alta';
                         document.forms[0].target = "oculto";
                         document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Idiomas.do';
                         document.forms[0].submit();
                         limpiarCamposRejilla();
                     }
                 }else
                 jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
                 }

                 // FUNCIONES DE VALIDACION Y COMPROBACION DEL FORMULARIO
                 function noEsta(indice){
                     var cod = document.forms[0].codigo.value;
                     for(i=0;(i<lista.length);i++){
                         if(i!=indice){
                             if((lista[i][0]) == cod) {
                                 jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
                                     return false;
                                 }
                             }
                         }
                         return true;
                     }

                     function filaSeleccionada(tabla){
                         var i = tabla.selectedIndex;
                         if((i>=0)&&(!tabla.ultimoTable))
                             return true;
                         return false;
                     }

                     function validarCamposRejilla(){
                         var codigo = document.forms[0].codigo.value;
                         var descrip = document.forms[0].descripcion.value;
                          var clave = document.forms[0].clave.value;
                         if((codigo!="")&&(descrip!="")&&(clave!=""))
                             return true;
                         return false;
                     }

                     function recuperaDatos(lista2) {
                         limpiarCamposRejilla();
                         lista = lista2;
                         tablaIdiomas.lineas=lista;
                         refresca(tablaIdiomas);
                     }

</script>
</head>
<body class="bandaBody" onload="javascript:{pleaseWait('off');inicializar();}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

    <form name="formulario" method="post">
        <input  type="hidden"  name="opcion" id="opcion">
        <input  type="hidden"  name="identificador" value="">
        <input  type="hidden"  name="codIdiomaEliminar" value="">
        <input  type="hidden"  name="nuevoCodIdioma" value="">
        <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiq_MantIdiomas")%></div>
        <div class="contenidoPantalla">
            <table style="width:100%">
               <tr> 
                    <td> 
                        <div id="tabla"></div>
                    </td>
                </tr>
                <tr> 
                    <td>
                        <input type="text" class="inputTextoObligatorio" id="obligatorio"  
                               name="codigo" maxlength="1"  style="width:24.5%" 
                               onkeyup="return SoloDigitosNumericos(this);">
                        <input name="clave" type="text" class="inputTextoObligatorio" id="clave" 
                                accept="" maxlength="2" style="width:24.5%" onkeyup="return xAMayusculas(this);">
                        <input name="descripcion" type="text" class="inputTextoObligatorio" id="obligatorio" 
                                maxlength="20" style="width:49%" onkeyup="return xAMayusculas(this);">
                    </td>
                </tr>
            </table>	
            <div class="botoneraPrincipal">
                <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%> 
                       name="cmdAlta" onClick="pulsarAlta();" accesskey="A"> 
                <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> 
                       name="cmdModificar" onClick="pulsarModificar();" accesskey="M"> 
                <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> 
                       name="cmdEliminar" onClick="pulsarEliminar();" accesskey="E"> 
                <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%>
                       name="cmdLimpiar" onClick="pulsarLimpiar();" accesskey="L">
                <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> 
                       name="cmdSalir" onClick="pulsarSalir();" accesskey="S"> 
            </div>
        </div>
    </form>
<script type="text/javascript">
            var tablaIdiomas = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
            
            tablaIdiomas.addColumna('225','center','<%= descriptor.getDescripcion("gEtiq_Codigo")%>');
            tablaIdiomas.addColumna('225','center','<%= descriptor.getDescripcion("gEtiq_Abrev")%>');        
            tablaIdiomas.addColumna('450','left','<%= descriptor.getDescripcion("gEtiq_Descrip")%>');
                    tablaIdiomas.displayCabecera=true;
                    tablaIdiomas.displayTabla();
                    
                    function refresca(tabla){
                        tabla.displayTabla();
                    }
                    
                    function rellenarDatos(tableName,rowID){
                        var i = rowID;
                        limpiarCamposRejilla();
                        var vector = [document.forms[0].codigo];
                        deshabilitarGeneral(vector);
                        if((i>=0)&&!tableName.ultimoTable){
                            var datosRejilla = [lista[i][0],lista[i][1],lista[i][2]];
                            rellenar(datosRejilla,vectorCamposRejilla);
                        }
                    } 
                    
                    document.onmouseup = checkKeys; 
                    
                    function checkKeysLocal(evento, tecla){
                            var teclaAuxiliar="";
                          if(window.event){
                            evento = window.event;
                            teclaAuxiliar =evento.keyCode;
                          }else
                                teclaAuxiliar =evento.which;

                            keyDel(evento);

                            if (teclaAuxiliar == 38) upDownTable(tablaIdiomas,lista,teclaAuxiliar);
                            if (teclaAuxiliar == 40) upDownTable(tablaIdiomas,lista,teclaAuxiliar);
                            if (teclaAuxiliar == 13) pushEnterTable(tablaIdiomas,lista);
                    }
                    
        </script>
        
    </body>
</html>
