<!-- JSP de mantenimiento de asuntos -->

<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html:html>
    <head>
        <title>Mantenimiento Asuntos</title>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />


    <%
        int idioma = 1;
        int apl = 1;
        String css = "";
        if (session != null && session.getAttribute("usuario") != null) {
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            if (usuario != null) {
                idioma = usuario.getIdioma();
                apl = usuario.getAppCod();
                css = usuario.getCss();
            }
        }
    %>

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
                 type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
    <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />


    <!-- *********************	FICHEROS JAVASCRIPT **************************    -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<html:rewrite page='/scripts/TablaNueva.js'/>"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >
<script type="text/javascript">    
    // Guarda los asuntos con: 0-codigo, 1-unidadRegistro y 2-tipoRegistro (su clave primaria)
    var lista_claves = new Array();
    // Guarda los asuntos con 0-codigo, 1-descripcion, 2-unidad de registro y 3-tipoRegistro (para dibujar la tabla)
    var lista = new Array();
    var url = '<c:url value="/MantAsuntos.do"/>';
    /////////////// Funcion de inicializacion de la pagina.

    function Inicio() {
        window.focus();
        cargaTabla();
    }

    /////////////// Funciones de los botones.
    <%-- original
    function pulsarEliminar() {
        var uniRegAsunto = lista_claves[tablaAsuntos.selectedIndex][1];
        var aplicacion = '<bean:write name="MantAsuntosForm" property="apl"/>';
        if(tablaAsuntos.selectedIndex == -1) {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
        } else if (aplicacion == 'registro' && uniRegAsunto == '-1') { //Para todos los registros
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjTodosRegistros")%>');
        } else if (jsp_alerta('', "<str:escape><%=descriptor.getDescripcion("msjBorrarAsunto")%></str:escape>")) {
            document.forms[0].opcion.value = 'eliminar';
            document.forms[0].codigo.value         = lista_claves[tablaAsuntos.selectedIndex][0];
            document.forms[0].unidadRegistro.value = lista_claves[tablaAsuntos.selectedIndex][1];
            document.forms[0].tipoRegistro.value   = lista_claves[tablaAsuntos.selectedIndex][2];
            document.forms[0].target = 'oculto';
            document.forms[0].submit();
        }
    }
    --%>
    // Nueva función pulsarAltaBaja
  function pulsarAltaBaja() {
      // Verificar si se ha seleccionado una fila en la tabla
      if (tablaAsuntos.selectedIndex == -1) {
          jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
          return;
      }

      // Obtener el estado actual del asunto en la columna "Estado"
      var estadoActual = tablaAsuntos.lineas[tablaAsuntos.selectedIndex][6].trim();

      // Alternar estado
      if (estadoActual === 'ALTA') {
          // Confirmar dar de baja
          if (confirm('<%=descriptor.getDescripcion("msjDarBaja")%>')) {
              pulsarBaja();
          }
      } else if (estadoActual === 'BAJA') {
          // Confirmar dar de alta
          if (confirm('<%=descriptor.getDescripcion("msjVolverAlta")%>')) {
              darAltaLogica();
          }
      }
  }

 function pulsarBaja() {
     if (tablaAsuntos.selectedIndex == -1) {
         jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
         return;
     }

     var uniRegAsunto = lista_claves[tablaAsuntos.selectedIndex][1];
     var aplicacion = '<bean:write name="MantAsuntosForm" property="apl"/>';

     if (aplicacion == 'registro' && uniRegAsunto == '-1') {
         jsp_alerta('A', '<%=descriptor.getDescripcion("msjTodosRegistros")%>');
     } else {
         document.forms[0].opcion.value = 'eliminar';
         document.forms[0].codigo.value = lista_claves[tablaAsuntos.selectedIndex][0];
         document.forms[0].unidadRegistro.value = lista_claves[tablaAsuntos.selectedIndex][1];
         document.forms[0].tipoRegistro.value = lista_claves[tablaAsuntos.selectedIndex][2];
         document.forms[0].target = 'oculto';
         document.forms[0].submit();

         // Refrescar la página después de enviar el formulario
         setTimeout(function() {
             location.reload();
         }, 1000); // Esperar 1 segundo para asegurar que el formulario se ha enviado
     }
 }

function darAltaLogica() {
    if (tablaAsuntos.selectedIndex == -1) {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
        return;
    }

    var uniRegAsunto = lista_claves[tablaAsuntos.selectedIndex][1];
    var aplicacion = '<bean:write name="MantAsuntosForm" property="apl"/>';

    if (aplicacion == 'registro' && uniRegAsunto == '-1') {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjTodosRegistros")%>');
    } else {
        document.forms[0].opcion.value = 'altaLogica';
        document.forms[0].codigo.value = lista_claves[tablaAsuntos.selectedIndex][0];
        document.forms[0].unidadRegistro.value = lista_claves[tablaAsuntos.selectedIndex][1];
        document.forms[0].tipoRegistro.value = lista_claves[tablaAsuntos.selectedIndex][2];
        document.forms[0].target = 'oculto';
        document.forms[0].submit();

        // Refrescar la página después de enviar el formulario
        setTimeout(function() {
            location.reload();
        }, 1000); // Esperar 1 segundo para asegurar que el formulario se ha enviado
    }
}

    /** Comprueba si el asunto ya ha sido eliminado **/
    function estaAsuntoEliminado(index){
        var exito = false;
        if(lista!=null && index<lista.length){
            if(lista[index][4]=='S' || lista[index][4]=='s'){
                exito =true;
            }
        }

        return exito;
    }


    function pulsarModificar() {
        if(tablaAsuntos.selectedIndex == -1) {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
        } else {
            var uniRegAsunto = lista_claves[tablaAsuntos.selectedIndex][1];
            var aplicacion = '<bean:write name="MantAsuntosForm" property="apl"/>';
            if (aplicacion == 'registro' && uniRegAsunto == '-1') { //Para todos los registros
               jsp_alerta('A', '<%=descriptor.getDescripcion("msjTodosRegistros")%>');
            } else {
                if(estaAsuntoEliminado(tablaAsuntos.selectedIndex)){
                    jsp_alerta("A",'<%=descriptor.getDescripcion("errModifAsuEliminado")%>');
                }else{
                   document.forms[0].opcion.value = 'modificar';
                   document.forms[0].codigo.value         = lista_claves[tablaAsuntos.selectedIndex][0];
                   document.forms[0].unidadRegistro.value = lista_claves[tablaAsuntos.selectedIndex][1];
                   document.forms[0].tipoRegistro.value   = lista_claves[tablaAsuntos.selectedIndex][2];
                   document.forms[0].target = 'mainFrame';
                   document.forms[0].submit();
                }
            }
        }
    }

    function pulsarConsultar(rowId) {
        if(rowId == -1) {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
        } else {
           document.forms[0].opcion.value = 'consultar';
           document.forms[0].codigo.value         = lista_claves[rowId][0];
           document.forms[0].unidadRegistro.value = lista_claves[rowId][1];
           document.forms[0].tipoRegistro.value   = lista_claves[rowId][2];
           document.forms[0].target = 'mainFrame';
           document.forms[0].submit();
        }
    }
    ///funcion para alta nueva
    function pulsarAlta() {
        document.forms[0].opcion.value = 'alta';
        document.forms[0].target = 'mainFrame';
        document.forms[0].submit();
    }

    function pulsarSalir(){
        document.forms[0].opcion.value = 'salir';
        document.forms[0].target = 'mainFrame';
        document.forms[0].submit();
    }

    /////////////// Funciones del frame oculto.
  function pulsarExportarExcel(){
        pleaseWait("on");

        var datos = {'datos_asuntos':"datos", 'opcion': 'exportarExcel'};

                    try{
                        $.ajax({
                            url: url,
                            type: 'POST',
                            async: true,
                            data: datos,
                            success: procesarRespuestaImprimirResultados,
                            error: mostrarErrorRespuestaImprimirResultados
                        });
                    }catch(Err){
                        pleaseWait('off');
                        jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
                    }

    }
    function procesarRespuestaImprimirResultados(result){
        pleaseWait('off');
        if(result){
                    var resultado = JSON.parse(result);
                    var datos = resultado.tabla;
                    if(datos!=undefined && datos.error!=undefined && datos.error.length>0)
                        jsp_alerta("A",datos.error)
                    else {
                        var jsonFichero = datos.fichero;

                        var inputs = '';
                        for(var i=0;i<jsonFichero.length;i++){
                            inputs += '<input type="hidden" name="jsonFichero" value="'+jsonFichero[i]+'" />'
                        }

                        if($('input[name=jsonFichero]').length>0){
                            $('input[name=jsonFichero]').each(function(index){
                               $(this).remove();
                            });
                        }
                        $('#mantAsuntos').append(inputs);
                        $('#mantAsuntos').attr("action",url+"?opcion=descargarFichero");
                        $('#mantAsuntos').submit();
                    }
                } else jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
                $('#mantAsuntos').removeAttr("action");

    }
    function mostrarErrorRespuestaImprimirResultados(){
        pleaseWait('off');
        jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
    }


    /* Recupera la lista de asuntos */
    function recuperaListaAsuntos(lista_asuntos, lista_asuntos_claves){
        lista_claves = lista_asuntos_claves;
        lista = lista_asuntos;
        tablaAsuntos.lineas = lista;
        tablaAsuntos.displayTabla();
    }

    /////////////// Control teclas.

    // Funcion redefinida para procesar las pulsaciones sobre la tabla.
    function callFromTableTo(rowId, tableName) {
        if (tableName==tablaAsuntos.id) pulsarConsultar(rowId);
    }

</SCRIPT>

</head>

<body class="bandaBody" onload="javascript:{ pleaseWait('off');}" >

    <!-- Mensaje de esperar mientras carga  -->
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
<!-- Fin mensaje de esperar mientras carga  -->
<html:form styleId="mantAsuntos" action="MantAsuntos" method="post">
<html:hidden property="opcion"/>
<html:hidden property="apl"/>
<html:hidden property="codigo"/>
<html:hidden property="unidadRegistro"/>
<html:hidden property="tipoRegistro"/>

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titMantAsu")%></div>
<div class="contenidoPantalla">
    <table width="100%">
        <tr>
            <td id="tabla">
            </td>
        </tr>
    </table>
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbNuevo")%> name="cmdAlta" onClick="pulsarAlta();" title='<%=descriptor.getDescripcion("toolTip_bAlta")%>' alt='<%=descriptor.getDescripcion("toolTip_bAlta")%>'>
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> name="cmdModificar" onClick="pulsarModificar();" title='<%=descriptor.getDescripcion("toolTip_bModificarAsunto")%>' alt='<%=descriptor.getDescripcion("toolTip_bModificar")%>'>
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAltaBaja")%> name="cmdAltaBaja" onClick="pulsarAltaBaja();" title='<%=descriptor.getDescripcion("toolTip_bAltaBaja")%>' alt='<%=descriptor.getDescripcion("toolTip_bAltaBaja")%>'>
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbExportarExcel")%> name="cmdExportarExcel" onclick="pulsarExportarExcel();" title='<%=descriptor.getDescripcion("toolTip_Exportar")%>'   alt='<%=descriptor.getDescripcion("toolTip_Exportar")%>' >
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir" onClick="pulsarSalir();" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>'>

    </div>
</div>
</html:form>
  <script type="text/javascript">
    var tablaAsuntos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tabla"));

    tablaAsuntos.addColumna('100','center',"<str:escape><%=descriptor.getDescripcion("etiqCodigoAsunto")%></str:escape>");
    tablaAsuntos.addColumna('300','center',"<str:escape><%=descriptor.getDescripcion("etiqDescAsunto")%></str:escape>");
    tablaAsuntos.addColumna('250','center',"<str:escape><%=descriptor.getDescripcion("gEtiqUnidReg")%></str:escape>");
    tablaAsuntos.addColumna('88','center',"<str:escape><%=descriptor.getDescripcion("etiqTipoRegistro")%></str:escape>");
    tablaAsuntos.addColumna('100','center',"<str:escape><%=descriptor.getDescripcion("etiqCodProc")%></str:escape>");
    tablaAsuntos.addColumna('250', 'center',"<str:escape><%=descriptor.getDescripcion("etiqDesProc")%></str:escape>");
    tablaAsuntos.addColumna('250', 'center',"<str:escape><%=descriptor.getDescripcion("etiqEstado")%></str:escape>");
    tablaAsuntos.displayCabecera=true;

    function cargaTabla(){
       var i = 0;
       <logic:iterate id="asunto" name="MantAsuntosForm" property="asuntos">
         lista_claves[i] = ['<bean:write name="asunto" property="codigo" />',
                            '<bean:write name="asunto" property="unidadRegistro" />',
                            '<bean:write name="asunto" property="tipoRegistro"/>'];

         var descripcionUor = "<str:escape><bean:write name="asunto" property="descUor" filter="false"/></str:escape>";
         if (descripcionUor == null || descripcionUor == 'null' || descripcionUor == '')
             descripcionUor = "<str:escape><%=descriptor.getDescripcion("etiq_TodosRegistros")%></str:escape>";

         lista[i] = ['<bean:write name="asunto" property="codigo" />',
                     "<str:escape><bean:write name="asunto" property="descripcion" filter="false"/></str:escape>",
                     descripcionUor,
                      '<bean:write name="asunto" property="tipoRegistro"/>',
                     '<bean:write name="asunto" property="procedimiento"/>',
                     '<bean:write name="asunto" property="desProcedimiento"/>',
                     '<bean:write name="asunto" property="asuntoBaja"/>'];
         i++;
      </logic:iterate>

      tablaAsuntos.lineas=lista;
      tablaAsuntos.displayTabla();      
    }

   </script>
    <script> Inicio(); </script>
    </body>
    </html:html>
