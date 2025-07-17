<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %><html>
<head>
<meta charset="ISO-8859-1">
<title>Servicios de almacenamiento </title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />        
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
%>        
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-1.9.1.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/json2.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>">        
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript">
            
var tabla;
var lineasTabla = new Array();
var lineasTablaOriginal = new Array();
var listaPlugin = new Array();
var comboPlugin;
var marcar = false;
var seleccion="";
var mostrarDigitalizacion = false;


/*** nuevo ***/
var listadoModificaciones = new Array();
var table = $('#tb_tabla0').DataTable();

$(function(){      
    <logic:present scope="session" name="servicioDigitalizacionActivo">
        mostrarDigitalizacion = true;
    </logic:present>
        
   cargarCabeceraTabla();   
   cargarContenidoTabla();
   refresca();
   cargarComboPlugin();
      
   $("#enlaceDesmarcar").on('click',function(){                                        
      // Desmarcar todos los elementos de tipo check
       $('.checkInicio', table.rows().nodes()).prop('checked', false);  
   });
   
   $("#enlaceMarcar").on('click',function(){        
      // Seleccionar todos los elementos de tipo check
      $('.checkInicio', table.rows().nodes()).prop('checked', true);  
   });
      
   $("#cmdModificar").on('click',function(){ 
      // Se recuperan todos los checkbox que están seleccionados/marcados
      var checkboxes = $(".checkInicio:checked", table.rows().nodes());
      var plugin     = $("#descPlugin").val();
      var digitalizacion=0;
      if(mostrarDigitalizacion){
        if($("#digitalizarDocumentos").is(':checked')){
            digitalizacion='SI';
        }else{
            digitalizacion='NO';
        }
     }
      
      if(checkboxes==null || checkboxes==undefined || checkboxes.length==0){
          jsp_alerta('A','<%=descriptor.getDescripcion("msgErrProcSelected")%>');
      }else{    
          var elementosSeleccionados = new Array();          
          for(i=0;i<checkboxes.length;i++){
             elementosSeleccionados.push(checkboxes[i].id);
          }
          //si no se cambia el plugin sale un mensaje para asegurar que se desea cambiar la
          // digitalización de los documentos, si la digitalizacion está activa en Registro.properties
          var ejeMod = false;
          if((plugin==null || plugin==undefined || plugin.lenght==0 || plugin=='') && mostrarDigitalizacion){
              if(jsp_alerta("",'<%=descriptor.getDescripcion("msgConfirmDig")%>')){
                  ejeMod = true;
               }
           } else if((plugin==null || plugin==undefined || plugin.lenght==0 || plugin=='') && !mostrarDigitalizacion){
               jsp_alerta('A','<%=descriptor.getDescripcion("msgErrPluginProc")%>');
           }else{
             ejeMod = true;
           }
           
           if(ejeMod){
               actualizarContenidoTabla(elementosSeleccionados,plugin,digitalizacion);
                actualizarListadoModif(elementosSeleccionados,plugin, digitalizacion);
                refresca();
           }
      }
   });
      

    $("#cmdLimpiar").on('click',function(){
        
       if(jsp_alerta('C','<%=descriptor.getDescripcion("msgErrLimpiarPlugProc")%>')){
            // Vacía la selecciones que se hayan realizado de los plugin asignados
            // a cada procedimiento. Actualiza el contenido de la tabla
            for(var i=0;i<lineasTabla.length;i++){
                lineasTabla[i][2] = "";
                lineasTablaOriginal[i][2]="";
            }
            tabla.lineas = lineasTabla;
            refresca();
       }       
    });
    
    
    $("#cmdGrabar").on('click',function(){
        pleaseWait('on');
        try{
            var parametro = "";
            var idPlugin = $("#codPlugin").val();
            if(JSON){                
                parametro = JSON.stringify(listadoModificaciones);                
                // Vaciamos el listado de modificaciones después de crear el json
                listadoModificaciones = new Array();
            }
            
            
            $.ajax({
                 url:  '<c:url value="/mantenimiento/MantenimientoAlmacenDocumentosProcedimiento.do"/>',
                 type: 'POST',
                 async: true,
                 data: 'opcion=grabar&parametro=' + parametro,
                 success: procesarRespuestaGrabar,
                 error: muestraErrorGrabar
            });      
        }catch(Err){
            pleaseWait('off');
            jsp_alerta('A','<%=descriptor.getDescripcion("msgErrorGenerico")%>' + ' ' + Err.description);            
        } 
    });

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
        if (tecla == 38) upDownTable(tabla,lista,tecla);
        if (tecla == 40) upDownTable(tabla,lista,tecla);
        if (tecla == 13) pushEnterTable(tabla,lista);
   }
   
   function rellenarDatos(tableName,rowID){          
   } 
});
             

function procesarRespuestaGrabar(ajaxResult){    
    pleaseWait('off');
    
    var respuesta = JSON.parse(ajaxResult);    
    if(respuesta!=null){        
        switch(respuesta.status){
            case 0:
                jsp_alerta('A','<%=descriptor.getDescripcion("msgDatosGrabados")%>');
                break;
            case 1:
                jsp_alerta('A','<%=descriptor.getDescripcion("msgErrConexionBD")%>');
                break;
            case 2:
                jsp_alerta('A','<%=descriptor.getDescripcion("msgErrGrabarDatos")%>');
                break;                      
            case 3:
                jsp_alerta('A','<%=descriptor.getDescripcion("msgErrorGenerico")%>');
                break;    
        }
    }
}


function muestraErrorGrabar(ajaxResult){
    pleaseWait('off');
    jsp_alerta('A','<%=descriptor.getDescripcion("msgErrorGenerico")%>');    
}
  
   
function actualizarContenidoTabla(elementosSeleccionados,nombrePlugin,digitalizacion){
    
    for(var i=0;i<elementosSeleccionados.length;i++){                      
        for(var j=0;j<lineasTablaOriginal.length;j++){
            if(lineasTablaOriginal[j][0]==elementosSeleccionados[i]){
                if(nombrePlugin!=undefined && nombrePlugin!=null &&nombrePlugin!=''){
                    lineasTabla[j][2] = nombrePlugin;
                    lineasTablaOriginal[j][2] = nombrePlugin;
                    if(digitalizacion){
                        lineasTabla[j][3]=digitalizacion;
                        lineasTablaOriginal[j][3]=digitalizacion;
                    }
                    break;   
                }else if(digitalizacion){
                    lineasTabla[j][3]=digitalizacion;
                    lineasTablaOriginal[j][3]=digitalizacion;
                    break;
                }
             }
        }        
    }      
   
    tabla.lineas = lineasTabla;
}



function actualizarListadoModif(elementosSeleccionados,nombrePlugin, digitalizacion){  
    for(var i=0;i<elementosSeleccionados.length;i++){
        var pos = obtenerPosicionElemento(elementosSeleccionados[i]);
        if(nombrePlugin==undefined || nombrePlugin==null || nombrePlugin==''){
            nombrePlugin = lineasTablaOriginal[pos][2];
        }
        var salida = buscarDescripcionPlugin(nombrePlugin); 
        var objeto = {
            id: pos, 
            codProcedimiento: lineasTablaOriginal[pos][0],
            nombreProcedimiento: lineasTablaOriginal[pos][1],
            nombrePlugin: salida.nombre, 
            implClassPlugin: salida.implClass,
            idPlugin: salida.id,
            digitalizacion: digitalizacion
        };
        listadoModificaciones.push(objeto);
    }      
}

function buscarDescripcionPlugin(nombrePlugin){
    var salida;
    for(var i=0;listaPlugin!=null && i<listaPlugin.length;i++){
        if(listaPlugin[i][1] == nombrePlugin){
            salida = {id:listaPlugin[i][0],nombre:listaPlugin[i][1],implClass:listaPlugin[i][2]};
            break;
        }
    }
    return salida;
}

function obtenerPosicionElemento(codigo){
    var posicion = -1;
    for(var i=0;lineasTablaOriginal!=null && i<lineasTablaOriginal.length;i++){
        if(lineasTablaOriginal[i][0] == codigo){
            posicion = i
            break;
        }
    }
    return posicion;
}


function cargarCabeceraTabla(){ 
   try{
        tabla = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tabla"));

        tabla.addColumna("30","center",'');
        tabla.addColumna('600','left','<%=descriptor.getDescripcion("gEtiqProc")%>');
        tabla.addColumna('250','center','<%=descriptor.getDescripcion("etiqAlmacenDocsProc")%>');
        if(mostrarDigitalizacion){
            tabla.addColumna('150','center','<%=descriptor.getDescripcion("etiqDigitalizacion")%>');     
        }
        tabla.displayCabecera=true;                 

    }catch(Err){
        alert("Error: " + Err.description);
    }
 }
 
 function cargarContenidoTabla(){
     
    var contador = 0; 
    <c:forEach items="${requestScope.asignaciones}" var="asignacion">              
       if(mostrarDigitalizacion){
           lineasTabla[contador] = ['<input type="checkbox" class="checkInicio" id="<c:out value='${asignacion.codProcedimiento}'/>"/>','<c:out value="${asignacion.codProcedimiento}"/> - <c:out value="${asignacion.nombreProcedimiento}"/>','<c:out value="${asignacion.nombrePlugin}"/>','<c:out value="${asignacion.digitalizacion}"/>'];
           lineasTablaOriginal[contador] = ['<c:out value="${asignacion.codProcedimiento}"/>','<c:out value="${asignacion.nombreProcedimiento}"/>','<c:out value="${asignacion.nombrePlugin}"/>','<c:out value="${asignacion.digitalizacion}"/>'];
       } else {
           lineasTabla[contador] = ['<input type="checkbox" class="checkInicio" id="<c:out value='${asignacion.codProcedimiento}'/>"/>','<c:out value="${asignacion.codProcedimiento}"/> - <c:out value="${asignacion.nombreProcedimiento}"/>','<c:out value="${asignacion.nombrePlugin}"/>'];
           lineasTablaOriginal[contador] = ['<c:out value="${asignacion.codProcedimiento}"/>','<c:out value="${asignacion.nombreProcedimiento}"/>','<c:out value="${asignacion.nombrePlugin}"/>'];
       }
       contador++;    
    </c:forEach>        

    tabla.lineas = lineasTabla;
 }      
    
 function refresca(){
   tabla.displayTabla();
   table = $('#tb_tabla0').DataTable();
 }
 
 function cargarComboPlugin(){
    comboPlugin = new Combo("Plugin"); 
    var contador = 0; 
    var codigos = new Array();
    var descripciones = new Array();
    
    <c:forEach items="${requestScope.plugins}" var="plugin">            
       codigos[contador] = ['<c:out value="${plugin.id}"/>'];
       descripciones[contador] = ['<c:out value="${plugin.nombre}"/>'];     
       listaPlugin[contador] = ['<c:out value="${plugin.id}"/>','<c:out value="${plugin.nombre}"/>','<c:out value="${plugin.implClass}"/>']
       contador++;     
    </c:forEach>           
        
    comboPlugin.addItems(codigos,descripciones);        
}                            
</script>
</head>
    <body class="bandaBody" onload="javascript:{pleaseWait('off');}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
<form name="formulario" method="post">
    <input  type="hidden"  name="opcion" id="opcion">
    <input  type="hidden"  name="identificador" id="identificador">                                    

    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("etiqMantDocProcedimiento")%></div>
    <div class="contenidoPantalla">
        <table>
            <tr style="text-align:center"> 
                <td colspan="2"> 
                    <div id="tabla" ></div>
                </td>
            </tr>
            <tr> 
               <td style="width:70%">
                    <a id="enlaceMarcar" href="#"><%=descriptor.getDescripcion("bSeleccTodos")%></a>
                    <a id="enlaceDesmarcar" href="#"><%=descriptor.getDescripcion("bDesmarcarTodos")%></a>
                </td>
                <td style="width:30%;text-align:right">
                     <input id="codPlugin" name="codPlugin" type="text" class="inputTexto" style="width:8%" onkeyup="return SoloDigitosNumericos(this);">
                     <input id="descPlugin" name="descPlugin" type="text" class="inputTexto" style="width:70%" readonly  >
                     <a id="anchorPlugin" name="anchorPlugin" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonPlugin" name="botonPlugin" style="cursor:hand;"	alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>" border="0" style="cursor:hand;"></span></a>
                     <logic:present scope="session" name="servicioDigitalizacionActivo">
                         <input type="checkbox" id="digitalizarDocumentos" style="margin-left:10px"/>        
                     </logic:present>
                 </td>
             </tr>
        </table>
        <div class="botoneraPrincipal"> 
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbGrabar")%>" 
               name="cmdGrabar" id="cmdGrabar" accesskey="G"> 
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbModificar")%>" 
               name="cmdModificar" id="cmdModificar" accesskey="M">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbLimpiar")%>" 
               name="cmdLimpiar" id="cmdLimpiar" accesskey="M">
        </div>            
    </div>            
</form>    
</body>
</html>
