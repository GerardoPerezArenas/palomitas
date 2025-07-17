<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
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


/*** nuevo ***/
var listado = new Array();

$(function(){     
   cargarCabeceraTabla();   
   cargarContenidoTabla();
   refresca();
   cargarComboPlugin();
      
   $("#enlaceDesmarcar").on('click',function(){                                        
      // Desmarcar todos los elementos de tipo check
      $("input:checkbox").prop('checked', false);
   });
   
   $("#enlaceMarcar").on('click',function(){        
      // Seleccionar todos los elementos de tipo check
      $("input:checkbox").prop('checked', true);
   });
      
   $("#cmdModificar").on('click',function(){    
      // Se recuperan todos los checkbox que están seleccionados/marcados
      var checkboxes = $("input:checkbox:checked");
      var plugin     = $("#descPlugin").val();
      var idPlugin   = $("#codPlugin").val();
      
      if(checkboxes==null || checkboxes==undefined || checkboxes.length==0){          
          jsp_alerta('A','<%=descriptor.getDescripcion("msgServicioNoSelect")%>');
      }else
      if(plugin==null || plugin==undefined || plugin.length==0){          
          jsp_alerta('A','<%=descriptor.getDescripcion("msgErrPluginProc")%>');
      }else{
          
          var elementosSeleccionados = new Array();          
          for(i=0;i<checkboxes.length;i++){
             elementosSeleccionados.push(checkboxes[i].id);
          }
          actualizarContenidoTabla(elementosSeleccionados,plugin);
          actualizarListado(elementosSeleccionados,plugin);
          refresca();
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
                parametro = JSON.stringify(listado);                
            }
            
            $.ajax({
                 url:  '<c:url value="/mantenimiento/MantenimientoAlmacenDocumentosRegistro.do"/>',
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
     
function actualizarContenidoTabla(elementosSeleccionados,nombrePlugin){    
    for(var i=0;i<elementosSeleccionados.length;i++){                                      
        for(var j=0;j<lineasTablaOriginal.length;j++){
            
            if(lineasTablaOriginal[j][0]==elementosSeleccionados[i]){
                lineasTabla[j][1] = nombrePlugin;
                lineasTablaOriginal[j][1] = nombrePlugin;
                break;                
            }
        }        
    }      
    
    tabla.lineas = lineasTabla;
}

function actualizarListado(elementosSeleccionados,nombrePlugin){        
    //for(var i=0;i<elementosSeleccionados.length;i++){                      
        for(var j=0;j<listado.length;j++){
            
            if(listado[j].codProcedimiento==elementosSeleccionados[i]){       
                var salida = buscarDescripcionPlugin(nombrePlugin);                   
                listado[j].nombrePlugin    = salida.nombre;                
                listado[j].implClassPlugin = salida.implClass;
                listado[j].idAlmacen = salida.id;
                break;                
            }
        }        
    //}      
    
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

function cargarCabeceraTabla(){ 
   try{
      tabla = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tabla"));
      tabla.addColumna("30","center",'');      
      tabla.addColumna('900','center','<%=descriptor.getDescripcion("etiqAlmacenDocsProc")%>');      
      tabla.displayCabecera=true;
    }catch(Err){
        alert("Error: " + Err.description);
    }
 }
 
 
 function cargarContenidoTabla(){     
    var contador = 0; 
    <c:forEach items="${requestScope.asignaciones}" var="asignacion">              
       var objeto = {id: '0', nombrePlugin:'<c:out value="${asignacion.nombrePlugin}"/>',implClassPlugin:'<c:out value="${asignacion.implClassPlugin}"/>',idAlmacen:'<c:out value="${asignacion.idAlmacen}"/>'};
       listado.push(objeto);
       lineasTabla[contador] = ['<input type="checkbox" id="<c:out value='${asignacion.idAlmacen}'/>"/>','<c:out value="${asignacion.nombrePlugin}"/>'];
       lineasTablaOriginal[contador] = ['<c:out value="${asignacion.idAlmacen}"/>','<c:out value="${asignacion.nombrePlugin}"/>'];
       contador++;    
    </c:forEach>        

    tabla.lineas = lineasTabla;
 }           
    
 function refresca(){
   tabla.displayTabla();
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
        <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("etiqMantDocRegistro")%></div>
        <div class="contenidoPantalla">
            <table>
                <tr>
                    <td colspan="2"> 
                        <div id="tabla" ></div>
                    </td>
                </tr>
                <tr> 
                    <td style="width:25%">
                        <a id="enlaceMarcar" href="#"><%=descriptor.getDescripcion("bSeleccTodos")%></a>
                        <a id="enlaceDesmarcar" href="#"><%=descriptor.getDescripcion("bDesmarcarTodos")%></a>
                    </td>
                    <td style="width:75%;text-align:right">
                         <input id="codPlugin" name="codPlugin" type="text" class="inputTexto" style="width:4%" onkeyup="return SoloDigitosNumericos(this);">
                         <input id="descPlugin" name="descPlugin" type="text" class="inputTexto" style="width:90%" readonly  >
                         <a id="anchorPlugin" name="anchorPlugin" href="">
                             <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonPlugin" name="botonPlugin" 
                                   alt ="<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>">
                             </span>
                         </a>
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
