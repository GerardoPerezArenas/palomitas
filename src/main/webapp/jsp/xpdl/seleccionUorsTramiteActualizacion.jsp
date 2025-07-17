<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.Integer"%>
<%@page import="es.altia.agora.business.util.LabelValueTO"%>
<%@page import="es.altia.agora.business.sge.ExistenciaUorImportacionVO"%>
<%@page import="es.altia.agora.business.sge.ExistenciaUorImportacionTramiteVO"%>
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
    var tabTramite;
    var tabTramiteUorManual;
    var obligatorioUorInicioManual = '<bean:write name="ExistenciaUorImportacionTramiteVO" property="obligatorioUorInicioManual" ignore="true" scope="request"/>'

    <c:if test="${not empty requestScope.error}">
            jsp_alerta('A', '<c:out value="${requestScope.error}"/>');
    </c:if>

    var listaCodigosUnidadesTramitadorasExistentes = new Array();
    var listaUnidadesTramitadorasExistentes            = new Array();
    var listaCodigosUnidadesInicioManualExistentes  = new Array();
    var listaCodigosInternosUnidadInicioManualExistentes = new Array();

    var conProc=0;

    <logic:iterate name="ExistenciaUorImportacionTramiteVO" scope="request" property="uorsExisten" id="existe">
            listaUnidadesTramitadorasExistentes[conProc] = ['<c:out value="${existe.codigoUorVisible}"/>','<c:out value="${existe.nombre}"/>'];
            listaCodigosUnidadesTramitadorasExistentes[conProc] = '<c:out value="${existe.codigoUor}"/>';
            conProc++;
    </logic:iterate>

    var contadorNoExistentes = 0;
    <logic:iterate name="ExistenciaUorImportacionTramiteVO" scope="request"  property="uorsNoExisten" id="noexiste">
        contadorNoExistentes++;
    </logic:iterate>

    var contadorManualesExisten = 0;
    <logic:iterate name="ExistenciaUorImportacionTramiteVO" scope="request" property="uorsManualesExisten" id="existe">
            listaCodigosUnidadesInicioManualExistentes[contadorManualesExisten] = ['<c:out value="${existe.codigoUorVisible}"/>','<c:out value="${existe.nombre}"/>'];
            listaCodigosInternosUnidadInicioManualExistentes[contadorManualesExisten] = ['<c:out value="${existe.codigoUor}"/>'];
            contadorManualesExisten++;
    </logic:iterate>

   function inicializar(){
       document.forms[0].codigoTramite.value = '<bean:write name="ExistenciaUorImportacionTramiteVO" ignore="true" property="codigoTramite"/>';
       //document.forms[0].codigoUorInicioManualTramite.value = '<bean:write name="ExistenciaUorImportacionTramiteVO" ignore="true" property="codigoUorInicioManual"/>';

        actualizarCamposFormulario();

        if(listaCodigosUnidadesInicioManualExistentes!=null && listaCodigosUnidadesInicioManualExistentes.length>=1 && listaCodigosInternosUnidadInicioManualExistentes!=null && listaCodigosInternosUnidadInicioManualExistentes.length>=1){
            document.forms[0].codigoUorInicioManualTramite.value = listaCodigosInternosUnidadInicioManualExistentes[0];        
        }
        

        // Si no hay unidades que no existen en la organizacion no se muestra la tabla con las unidades tramitadoras existentes y ya seleccionadas
        if(contadorNoExistentes>0){
            tabTramite = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaTramite'));

            tabTramite.addColumna('65','center','<%=descriptor.getDescripcion("etiqOrdCodUOR")%>');
            tabTramite.addColumna('550','center','<%=descriptor.getDescripcion("etiqOrdNomUOR")%>');
            tabTramite.lineas = listaUnidadesTramitadorasExistentes;
            tabTramite.displayCabecera=true;
            tabTramite.displayTabla();
        }


        if(obligatorioUorInicioManual=="true"){
            tabTramiteUorManual = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaUnidadManualTramite'));

            tabTramiteUorManual.addColumna('65','center','<%=descriptor.getDescripcion("etiqOrdCodUOR")%>');
            tabTramiteUorManual.addColumna('550','center','<%=descriptor.getDescripcion("etiqOrdNomUOR")%>');
            tabTramiteUorManual.lineas = listaCodigosUnidadesInicioManualExistentes;
            tabTramiteUorManual.displayCabecera=true;
            tabTramiteUorManual.displayTabla();
       }
   }


    function callFromTableTo(rowID,tableName){
        if(tabProcedimientos.id == tableName){
          fila=parseInt(rowID);
        }
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
                listaUnidadesTramitadorasExistentes.push(nuevo);
                listaCodigosUnidadesTramitadorasExistentes.push(codUor);
                tabTramite.lineas = listaUnidadesTramitadorasExistentes;
                tabTramite.displayTabla();
            }else
                jsp_alerta("A","<%=descriptor.getDescripcion("msgUnidadExiste")%>");
        }
    }


    function actualizarCamposFormulario(){
        var separador = "§¥";
        var sListaCodigosInternos="";
        var sListaCodVisibleUor="";

        for(i=0;listaCodigosUnidadesTramitadorasExistentes!=null && i<listaCodigosUnidadesTramitadorasExistentes.length;i++){
            sListaCodigosInternos += listaCodigosUnidadesTramitadorasExistentes[i];

            if(listaCodigosUnidadesTramitadorasExistentes.length-i>1)
                sListaCodigosInternos += separador;
        }// for

        if(obligatorioUorInicioManual!="true") document.forms[0].codigoUorInicioManualTramite.value = "";
        document.forms[0].listaCodigosUorsTramitadorasTramite.value = sListaCodigosInternos;
    }


    /**
     * Comprueba si una determinada uor existe entre las seleccionadas
     */
    function existeUorEntreExistentes(codUorVisible){
        var existe = false;

         for(i=0;listaUnidadesTramitadorasExistentes!=null && i<listaUnidadesTramitadorasExistentes.length;i++){
            if(listaUnidadesTramitadorasExistentes[i][0]==codUorVisible){
                existe = true;
                break;
            }
        }// for
        return existe;
    }


    function pulsarEliminarUor(){
        var index = tabTramite.selectedIndex;
        if(index!=-1){
            var aux = new Array();
            var pos = 0;

            for(i=0;i<listaUnidadesTramitadorasExistentes.length;i++){
                if(i!=index){
                    aux[pos] = listaUnidadesTramitadorasExistentes[i];
                    pos++;
                }
            }// for

            // SE actualiza la tabla con las unidades de inicio del procedimiento seleccionadas
           listaUnidadesTramitadorasExistentes = aux;
           tabTramite.lineas = listaUnidadesTramitadorasExistentes;
           tabTramite.displayTabla();

           // Se actualiza la lista de códigos internos de las unidades de inicio del procedimiento seleccionadas
           var auxCodigoInterno = new Array();
           pos = 0;
           for(j=0;j<listaCodigosUnidadesTramitadorasExistentes.length;j++){
                if(j!=index){
                    auxCodigoInterno[pos] = listaCodigosUnidadesTramitadorasExistentes[j];
                    pos++;
                }
           }// for

           listaCodigosUnidadesTramitadorasExistentes = auxCodigoInterno;
           actualizarCamposFormulario();

        }else
            jsp_alerta("A","<%=descriptor.getDescripcion("msgUnidadNoSeleccionada")%>");
    }



    function pulsarAnadirUorManual(){
         var argumentos = new Array();

         if(listaCodigosUnidadesInicioManualExistentes.length==0){
            var source ="<%=request.getContextPath()%>/xpdl/CargarListaUorAction.do?opcion=cargarUors";
            abrirXanelaAuxiliar("<%=request.getContextPath()%>" +  "/jsp/sge/mainVentana.jsp?source=" + source, argumentos,
	'width=750,height=490,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                            actualizarCodigoUnidadInicioManual(datos);
                        }
                });
        }else
            jsp_alerta("A","<%=descriptor.getDescripcion("msgSoloUnicaUnidadInicioTram")%>");
    }

    function actualizarCodigoUnidadInicioManual(datos){ 
         if(datos!=null && datos.length==3){
            var codVisible = datos[0];
            var descripcion = datos[1];
            var codUor = datos[2];

            var nuevo = new Array();
            nuevo[0] = codVisible;
            nuevo[1] = descripcion;

            document.forms[0].codigoUorInicioManualTramite.value = codUor;
            listaCodigosUnidadesInicioManualExistentes.push(nuevo);
            tabTramiteUorManual.lineas = listaCodigosUnidadesInicioManualExistentes;
            tabTramiteUorManual.displayTabla();
         }
    }

    function pulsarSiguiente(){
        var codigoUorInicioManualTramite = document.forms[0].codigoUorInicioManualTramite.value;        
        var contador = 0;
        if(obligatorioUorInicioManual=="true" && codigoUorInicioManualTramite=="") {
            contador++;
        }

        if(listaCodigosUnidadesTramitadorasExistentes.length==0){
            contador++;
        }
        
        if(contador==0){
            document.forms[0].action = "<%=request.getContextPath()%>/xpdl/DocumentoXPDL.do?opcion=actualizarUnidadesTramiteActualizacion";
            document.forms[0].submit();
        }else
            jsp_alerta("A","<%=descriptor.getDescripcion("msgNoSeleccionUnidadTramite")%>");
    }


    function pulsarEliminarUorManual(){
        var index = tabTramiteUorManual.selectedIndex;
        if(index!=-1){
            var aux = new Array();

           // Se actualiza la tabla con las unidades de inicio del procedimiento seleccionadas
           tabTramiteUorManual.lineas = aux;
           tabTramiteUorManual.displayTabla();
          document.forms[0].codigoUorInicioManualTramite.value="";
          listaCodigosUnidadesInicioManualExistentes = new Array();
        }else
            jsp_alerta("A","<%=descriptor.getDescripcion("msgUnidadNoSeleccionada")%>");
    }

    function pulsarCancelar(){
        window.top.location.href="<%=request.getContextPath()%>" + "/xpdl/DocumentoXPDL.do?opcion=cancelarImportacion";
    }

</script>
</head>
<title><%=descriptor.getDescripcion("ConvXPDLFormTitle")%></title>

<html:form method="POST" action="/xpdl/DocumentoXPDL.do">
<html:hidden property="listaCodigosUorsTramitadorasTramite"/>
<html:hidden property="codigoUorInicioManualTramite"/>
<html:hidden property="codigoTramite"/>

<BODY class="bandaBody"style="width:100%;height:100%" onload="inicializar();">
        <div class="txttitblanco"><%=descriptor.getDescripcion("titleSelecUnidadesTram")%>&nbsp;<bean:write name="ExistenciaUorImportacionTramiteVO" ignore="true" property="nombreTramite"/></div>
        <div class="contenidoPantalla" width="100%">
            <table border="0px" width="75%" align="center">
             <%-- Se comprueba si hay que seleccionar la unidad de inicio del trámite --%>
             <c:if test="${requestScope.ExistenciaUorImportacionTramiteVO.codigoUorInicioManual ne null}">
             <tr>
                 <td>
                    <span class="etiqueta"><strong>
                        <%=descriptor.getDescripcion("etiqUnidIniManualTramNoExiste")%>
                    </strong>
                    </span>
                 </td>
             </tr>
             <tr>
                 <td>
                        <li>
                        <span class="etiqueta">
                            <c:out value="${requestScope.ExistenciaUorImportacionTramiteVO.codigoUorInicioManual}"/> -
                            <c:out value="${requestScope.ExistenciaUorImportacionTramiteVO.descripcionUorInicioManual}"/>
                        </span>
                        </li>
                 </td>
             </tr>
             <tr>
                 <td id="tablaUnidadManualTramite" style="width: 100%" align="center"></td>
             </tr>
             <tr>
                 <td align="right">
                     <table border="0px">
                     <tr>
                         <td width="82%">
                             <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("btnAnadirUor")%>"
                                name="botonGenerarManual" onClick="pulsarAnadirUorManual();">

                            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("btnEliminarUor")%>"
                                name="botonEliminarManual" onClick="pulsarEliminarUorManual();">

                         </td>
                         <td>&nbsp;</td>
                     </tr>
                     </table>
                 </td>
             </tr>
            </c:if>

            <%-- Se muestran las unidades tramitadoras que no existen --%>
            <logic:notEmpty name="ExistenciaUorImportacionTramiteVO" scope="request"  property="uorsNoExisten">
             <tr>
                 <td>
                     <span class="etiqueta"><strong>
                         <%=descriptor.getDescripcion("titleUnidadesTramNoExisten")%>
                     </strong>
                     </span>
                 </td>
             </tr>
             <logic:iterate name="ExistenciaUorImportacionTramiteVO" scope="request"  property="uorsNoExisten" id="noExiste">
             <tr>
                 <td>
                     <span class="etiqueta">
                     <li>
                         <bean:write name="noExiste" property="codigoUorVisible" ignore="true"/> - <bean:write name="noExiste" property="nombre" ignore="true"/>
                     </li>
                     </span>
                 </td>
             </tr>
             </logic:iterate>

             <tr>
                   <td id="tablaTramite" style="width: 100%" align="center"></td>
             </tr>
             <tr>
                 <td align="right">
                     <table border="0px">
                     <tr>
                         <td width="82%">
                             <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("btnAnadirUor")%>"
                                name="botonGenerar" onClick="pulsarAnadirUor();">

                            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("btnEliminarUor")%>"
                                name="botonEliminar" onClick="pulsarEliminarUor();">

                         </td>
                         <td>&nbsp;</td>
                    </tr>
                   </table>
               </td>
           </tr>
          </logic:notEmpty>
      </table>
        <DIV id="capaBotones1" name="capaBotones1" class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSiguiente")%>"
                    name="botonSiguiente" onClick="pulsarSiguiente();">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>"
                       name="botonImportar" onClick="pulsarCancelar();">
        </DIV>
    </div>
</BODY>
</html:form>
</html:html>
