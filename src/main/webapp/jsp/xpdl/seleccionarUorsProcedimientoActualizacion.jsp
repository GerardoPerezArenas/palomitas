<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.Integer"%>
<%@page import="es.altia.agora.business.util.LabelValueTO"%>
<%@page import="es.altia.agora.business.sge.ExistenciaUorImportacionVO"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<html:html>
 <%
    int idioma = 1;
    int aplicacion = 20;
    UsuarioValueObject usuario = null;
    String entidad = "";
    String usu = "";
    if (session != null) {
        usuario = (UsuarioValueObject) session.getAttribute("usuario");
        if (usuario != null) {
            idioma = usuario.getIdioma();
            aplicacion = usuario.getAppCod();
            entidad = usuario.getEnt();
            usu = usuario.getNombreUsu();
        }
    }

    String parametros [] = usuario.getParamsCon();
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    ArrayList idiomas = (ArrayList)session.getAttribute("listaIdiomas");

%>
<head>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= aplicacion %>" />

<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>

<link rel="stylesheet" type="text/css" href="<c:url value='/css/sge_basica.css'/>" media="screen">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
    var tabProcedimientos;
    <c:if test="${not empty requestScope.error}">
            jsp_alerta('A', '<c:out value="${requestScope.error}"/>');
    </c:if>

    var listaCodigosUorExistentes = new Array();
    var listaUnidadesProcExistentes = new Array();
    var conProc=0;

    <c:forEach var="unidadExistente" items="${sessionScope.unidades_proc_existen}">
            listaUnidadesProcExistentes[conProc] = ['<c:out value="${unidadExistente.codigoUorVisible}"/>','<c:out value="${unidadExistente.nombre}"/>'];
            listaCodigosUorExistentes[conProc] = '<c:out value="${unidadExistente.codigoUor}"/>';
            conProc++;
    </c:forEach>

   function inicializar(){
        actualizarCamposFormulario();
        tabProcedimientos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaProcedimientos'));

        tabProcedimientos.addColumna('65','center','<%=descriptor.getDescripcion("etiqOrdCodUOR")%>');
        tabProcedimientos.addColumna('550','center','<%=descriptor.getDescripcion("etiqOrdNomUOR")%>');
        tabProcedimientos.lineas = listaUnidadesProcExistentes;
        tabProcedimientos.displayCabecera=true;
        tabProcedimientos.displayTabla();
   }


    function callFromTableTo(rowID,tableName){
        if(tabProcedimientos.id == tableName){
          fila=parseInt(rowID);
        }
     }


    function refrescarProcedimientos() {
        //tabProcedimientos.displayTabla();
    }


    function getProcedimiento(w) {
       var data = w.store.get();
        var txtCodigo = -1;
        var txtDescripcion=-1;
        for (var i = 0; i < data.length; i++) {
            if (data[i].isSelected) {
                txtCodigo = listaProcedimientos[i][0];
                txtDescripcion = listaProcedimientos[i][1];
            }
        }
        var resultado = new Array ();
        resultado[0] = txtCodigo;
        resultado[1] = txtDescripcion;
        return resultado;
    }


    function salir(){
        var resultado = jsp_alerta("C",'<%= descriptor.getDescripcion("g_confirmSalirApp")%>');
        if (resultado == 1){
            window.location.href=  '<c:url value='/SalirApp.do?app='/><%= usuario.getAppCod()%>';
        }
    }

    function pulsarGenerar(){
        var fila = tabProcedimientos.selectedIndex;
        if(fila!=-1){
            var codProcedimiento   = listaProcedimientos[fila][0];
            var descProcedimiento = listaProcedimientos[fila][1];

           if (codProcedimiento!="" && descProcedimiento!="") {
                ventanaProcXPDL = window.open("<%=request.getContextPath()%>/procedimientoEnXPDL?codigo="+ codProcedimiento+"&descripcion="+descProcedimiento+"&"+getParametros(),'ventanaProcXPDL','width=800px,height=550px,scrollbars=1,status='+ '<%=statusBar%>' + ',toolbar=no,menubar=yes');
                ventanaProcXPDL.focus();
            }else {
                jsp_alerta('A', "<%=descriptor.getDescripcion("noProcSelecc")%>");
            }
        }else {
                jsp_alerta('A', "<%=descriptor.getDescripcion("noProcSelecc")%>");
        }
    }


    function actualizaProcs(listaProcedimientosVO) {
        // Se copia el array elemento a elemento pq si no se pasa por referencia y se pierde.
        listaProc = new Array();
        for (i=0; i<listaProcedimientosVO.length; i++) {
          listaProc[i] = [listaProcedimientosVO[i][0],listaProcedimientosVO[i][1]];
        }
        listaProcedimientos.lineas=listaProc;
        refrescarLista();
    }

    function refrescarLista(){
        location.reload(true);
    }


    function pulsarExportar(){
     var w = dojo.widget.byId("tablaProc");
        if (w) {
            var s = getProcedimiento(w);
            if (s[0] != -1) {
                document.forms[0].opcion.value = "generarXPDL";
                document.forms[0].codigo.value = s[0];
                document.forms[0].descripcion.value = s[1];
                document.forms[0].action="<c:url value='/xpdl/GenerarProcedimientoEnXPDL.do'/>";
                document.forms[0].submit();

                jsp_alerta('A',  "<%=descriptor.getDescripcion("procExportado")%>");

             } else {
                jsp_alerta('A', "<%=descriptor.getDescripcion("noProcSelecc")%>");
            }
        }

    }


    function getParametros(){
        var parametros="";
         <% for (int i=0;i<parametros.length;i++){
            String aux;
             if (i==0)
                 aux = "params="+parametros[i];
             else
                 aux ="&params="+parametros[i];
         %>
        parametros = parametros+"<%=aux%>";
        <%}
        %>
      return parametros;
    }


    /** Muestra los mensajes de error producidos durante la importación de un procedimiento en formato XPDL */
    function mostrarError(error){
        if(error==-1){
            jsp_alerta("A","<%=descriptor.getDescripcion("msgErrorImportarProc")%>");
        }
    }

    /** Muestra un mensaje en función del parámetro que se le pase */
    function mostrarMensaje(error){
        if(error==0){
            jsp_alerta("A","<%=descriptor.getDescripcion("msgExitoImportarProc")%>");
        }
    }

    function pulsarAnadirUor(){
        var argumentos = new Array();
        var source ="<%=request.getContextPath()%>/xpdl/CargarListaUorAction.do?opcion=cargarUors";
        abrirXanelaAuxiliar("<%=request.getContextPath()%>" +  "/jsp/sge/mainVentana.jsp?source=" + source, argumentos,
	'width=750,height=490',function(datos){
                            if(datos!=undefined){
                                actualizarListaUnidadesInicio(datos);
                                actualizarCamposFormulario();
                            }
                    });
    }


    /** Actualiza la lista de unidades de inicio */
    function actualizarListaUnidadesInicio(datos){
        if(datos!=null && datos.length==3){
            var codVisible = datos[0];
            var descripcion = datos[1];
            var codUor = datos[2];

            if(!existeUorEntreExistentes(codVisible)){
                var nuevo = new Array();
                nuevo[0] = codVisible;
                nuevo[1] = descripcion;
                listaUnidadesProcExistentes.push(nuevo);

                listaCodigosUorExistentes.push(codUor);
                tabProcedimientos.lineas = listaUnidadesProcExistentes;
                tabProcedimientos.displayTabla();
            }else
                jsp_alerta("A","<%=descriptor.getDescripcion("msgUnidadExiste")%>");
        }
    }


    function actualizarCamposFormulario(){
        var separador = "§¥";
        var sListaCodigosInternos="";
        var sListaCodVisibleUor="";

        for(i=0;listaCodigosUorExistentes!=null && i<listaCodigosUorExistentes.length;i++){
            sListaCodigosInternos += listaCodigosUorExistentes[i];

            if(listaCodigosUorExistentes.length-i>1)
                sListaCodigosInternos += separador;
        }// for


        for(i=0;listaUnidadesProcExistentes!=null && i<listaUnidadesProcExistentes.length;i++){
            sListaCodVisibleUor += listaUnidadesProcExistentes[i][0];
            if(listaUnidadesProcExistentes.length-i>1)
                sListaCodVisibleUor += separador;
        }// for

        document.forms[0].listaCodigosUorsImportacion.value = sListaCodigosInternos;
        document.forms[0].listaCodigosVisibleUorsImportacion.value = sListaCodVisibleUor;
    }


    /**
     * Comprueba si una determinada uor existe entre las seleccionadas
     */
    function existeUorEntreExistentes(codUorVisible){
        var existe = false;

         for(i=0;listaUnidadesProcExistentes!=null && i<listaUnidadesProcExistentes.length;i++){
            if(listaUnidadesProcExistentes[i][0]==codUorVisible){
                existe = true;
                break;
            }
        }// for
        return existe;
    }


    function pulsarEliminarUor(){
        var index = tabProcedimientos.selectedIndex;
        if(index!=-1){
            var aux = new Array();
            var pos = 0;

            for(i=0;i<listaUnidadesProcExistentes.length;i++){
                if(i!=index){
                    aux[pos] = listaUnidadesProcExistentes[i];
                    pos++;
                }
            }// for

            // SE actualiza la tabla con las unidades de inicio del procedimiento seleccionadas
           listaUnidadesProcExistentes = aux;
           tabProcedimientos.lineas = listaUnidadesProcExistentes;
           tabProcedimientos.displayTabla();

           // Se actualiza la lista de códigos internos de las unidades de inicio del procedimiento seleccionadas
           var auxCodigoInterno = new Array();
           pos = 0;
           for(j=0;j<listaCodigosUorExistentes.length;j++){
                if(j!=index){
                    auxCodigoInterno[pos] = listaCodigosUorExistentes[j];
                    pos++;
                }
           }// for

           listaCodigosUorExistentes = auxCodigoInterno;
           actualizarCamposFormulario();

        }else
            jsp_alerta("A","<%=descriptor.getDescripcion("msgUnidadNoSeleccionada")%>");
    }


    function pulsarSiguiente(){
        if(listaCodigosUorExistentes.length>0){
            document.forms[0].action = "<%=request.getContextPath()%>/xpdl/DocumentoXPDL.do?opcion=actualizarUnidadesInicioProcedimientoActualizacion";
            document.forms[0].submit();
        }else
            jsp_alerta("A","<%=descriptor.getDescripcion("msgNoSeleccionUnidadProc")%>");
    }

    function pulsarCancelar(){
        window.top.location.href="<%=request.getContextPath()%>" + "/xpdl/DocumentoXPDL.do?opcion=cancelarImportacion";
    }

</script>
</head>
<title><%=descriptor.getDescripcion("ConvXPDLFormTitle")%></title>

<html:form method="POST" action="/xpdl/DocumentoXPDL.do">
<html:hidden property="listaCodigosUorsImportacion"/>
<html:hidden property="listaCodigosVisibleUorsImportacion"/>

<BODY class="bandaBody"style="width:100%;height:100%" onload="inicializar();">
        <div class="txttitblanco"><%=descriptor.getDescripcion("titleSelecUnidadesProc")%></div>
        <div class="contenidoPantalla">
            <table style="width:100%">
                <tr>
                    <td>
                       <span class="etiqueta">
                           <%=descriptor.getDescripcion("etiqUnidIniProcNoExisten")%>
                       </span>
                    </td>
                </tr>
                <tr>
                    <td>
                        <c:forEach var="unidad" items="${sessionScope.unidades_proc_no_existen}">
                            <li>
                               <span class="etiqueta">
                                   <c:out value="${unidad.codigoUorVisible}"/> - <c:out value="${unidad.nombre}"/>
                               </span>
                            </li>
                       </c:forEach>
                    </td>
                </tr>
                <tr>
                    <td height="1px;">&nbsp;</td>
                </tr>
                <tr>
                   <td align="left">
                        <span class="etiqueta">
                              <%=descriptor.getDescripcion("etiqUnidIniProcNoExistent")%>
                        </span>
                   </td>
            </tr>
            <tr>
                  <td id="tablaProcedimientos" style="width: 100%" align="center"></td>
            </tr>
            <tr>
                <td align="right">
                   <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("btnAnadirUor")%>"
                      name="botonGenerar" onClick="pulsarAnadirUor();">

                  <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("btnEliminarUor")%>"
                      name="botonEliminar" onClick="pulsarEliminarUor();">
                </td>
            </tr>
        </table>
        <div id="capaBotones1" name="capaBotones1" class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSiguiente")%>"
                    name="botonSiguiente" onClick="pulsarSiguiente();">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>"
                       name="botonImportar" onClick="pulsarCancelar();">
        </div>
    </div>
</BODY>
</html:form>
</html:html>
