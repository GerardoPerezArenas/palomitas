<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.Integer"%>
<%@page import="es.altia.agora.business.util.LabelValueTO"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<html:html>    
 <%
    //response.setHeader("Cache-control","no-cache");
    //response.setHeader("Pragma","no-cache");
    //response.setDateHeader ("Expires", 0);

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

<link rel="stylesheet" type="text/css" href="<c:url value='/css/sge_basica.css'/>" media="screen">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
    var mensaje = "<%=(String)request.getAttribute("procedimientoImportado")%>";

    var tabProcedimientos;    
    <c:if test="${not empty requestScope.error}">
            jsp_alerta('A', '<c:out value="${requestScope.error}"/>');
    </c:if>
   
    var listaProcedimientos = new Array();
    var conProc=0;
    <logic:iterate id="proc" name="ListaProcedimientosXPDLForm" property="listaProcedimientos">               
        listaProcedimientos[conProc] = ['<bean:write name="proc" property="txtCodigo"/>',
                '<bean:write name="proc" property="txtDescripcion"/>'];
        conProc++;
    </logic:iterate>
        

   function inicializar(){
        tabProcedimientos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaProcedimientos'));

        tabProcedimientos.addColumna('165','center','<%=descriptor.getDescripcion("etiqCodProcedimientoXPDL")%>');
        tabProcedimientos.addColumna('785','center','<%=descriptor.getDescripcion("procName")%>');
        tabProcedimientos.lineas = listaProcedimientos;
        tabProcedimientos.displayCabecera=true;
        tabProcedimientos.displayTabla();
        if(mensaje=="SI")
            jsp_alerta("A","TODO: El procedimiento se ha insertado correctamente")
   }


    function callFromTableTo(rowID,tableName){               
          fila=parseInt(rowID);          
     }


    function refrescarProcedimientos() {
        tabProcedimientos.displayTabla();
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


    function pulsarGenerar(){                
        var fila = tabProcedimientos.selectedIndex;        
        if(fila!=-1){
            var codProcedimiento   = listaProcedimientos[fila][0];
            var descProcedimiento = listaProcedimientos[fila][1];
        
           if (codProcedimiento!="" && descProcedimiento!="") {               
                abrirXanelaAuxiliar("<%=request.getContextPath()%>/GenerarXPDL?codigo="+ codProcedimiento+"&descripcion="+descProcedimiento+"&"+getParametros()+"&opcion=generar",
                        null,'width=800px,height=550px,scrollbars=1,status='+ '<%=statusBar%>' + ',menubar=1',function(){});
            }else {
                jsp_alerta('A', "<%=descriptor.getDescripcion("noProcSelecc")%>");
            }
        }else {
                jsp_alerta('A', "<%=descriptor.getDescripcion("noProcSelecc")%>");
        }        
    }
    function pulsarDescargar(){                
        var fila = tabProcedimientos.selectedIndex;        
        if(fila!=-1){
            var codProcedimiento   = listaProcedimientos[fila][0];
            var descProcedimiento = listaProcedimientos[fila][1];
        
           if (codProcedimiento!="" && descProcedimiento!="") {               
                abrirXanelaAuxiliar("<%=request.getContextPath()%>/GenerarXPDL?codigo="+ codProcedimiento+"&descripcion="+descProcedimiento+"&"+getParametros()+"&opcion=descargar",
                        null,'width=800px,height=550px,scrollbars=1,status='+ '<%=statusBar%>' + ',menubar=1',function(){});
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
    function pulsarImportar(){    
        abrirXanelaAuxiliar("<html:rewrite page='/xpdl/DocumentoXPDL.do'/>?opcion=documentoNuevo",null,
	'left=10, top=10, width=550, height=300, scrollbars=no, menubar=no, location=no',function(){});
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

    function cargarSeleccionUnidadesOrganicasProcedimiento(){
        var ruta = "<%=request.getContextPath() %>/jsp/xpdl/seleccionarUorsProcedimiento.jsp";        
        window.location=ruta;
    }


     function cargarSeleccionUnidadesOrganicasTramite(){
        var ruta = "<%=request.getContextPath() %>/xpdl/DocumentoXPDL.do?opcion=actualizarUnidadesTramite";
        window.location=ruta;
    }

    function cargarSeleccionUnidadesOrganicasProcedimientoModificacion(){
        var ruta = "<%=request.getContextPath() %>/jsp/xpdl/seleccionarUorsProcedimientoActualizacion.jsp";
        window.location=ruta;
    }

     function cargarSeleccionUnidadesOrganicasTramiteModificacion(){
        var ruta = "<%=request.getContextPath() %>/xpdl/DocumentoXPDL.do?opcion=actualizarUnidadesTramiteActualizacion";        
        window.location=ruta;
    }
    function rellenarDatos(tableName,listName){
                
    }

</script>
</head>
<title><%=descriptor.getDescripcion("ConvXPDLFormTitle")%></title>

<BODY class="bandaBody" onload="inicializar();" style="width:100%">
    <div class="txttitblanco"><%=descriptor.getDescripcion("ConvXPDLFormSubTitle")%></div>
    <div class="contenidoPantalla" style="width:100%;">
        <table width="100%">
            <tr>                                               
                  <td id="tablaProcedimientos" style="width: 100%" align="left"></td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
              <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("XPDLVisualizar")%>"
                    name="botonGenerar" onClick="pulsarGenerar();">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("XPDLDescargar")%>"
                    name="botonDescarga" onClick="pulsarDescargar();">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("XPDLImportar")%>"
                       name="botonImportar" onClick="pulsarImportar();">
        </div>
    </div>
</BODY>
</html:html>
