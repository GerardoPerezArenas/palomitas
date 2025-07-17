<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/displaytag-el.tld" prefix="display" %>

<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm"%>

<html>
<head>
<TITLE>::: LISTA OPERACIONES EXPEDIENTE:::</TITLE>
<meta http-equiv="X-UA-Compatible" content="IE=EDGE"/>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>" type="text/css">

<%
    int idioma = 1;
    int apl = 4;
    UsuarioValueObject usuario = new UsuarioValueObject();
    if (session != null) {
        usuario = (UsuarioValueObject) session.getAttribute("usuario");
        if (usuario != null) {
            idioma = usuario.getIdioma();
        }
    }
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.select.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<script type="text/javascript">

var tablaListadoOperacionesExpediente = null;

function inicializar() {
    top.focus();
    
    crearTablaListadoOperacionesExpediente();
}

function crearTablaListadoOperacionesExpediente() {
    if (tablaListadoOperacionesExpediente !== null) {
        tablaListadoOperacionesExpediente.destroy();
    }

    var columnas = [
                {title:"idOperacion", sWidth:'5%', sClass: "centrado", bVisible: false, bSortable: false},
                {title:'<%=descriptor.getDescripcion("gEtiqFecOpe")%>', sWidth:'20%', sClass: "centrado", bSortable: true},
                {title:'<%=descriptor.getDescripcion("gEtiqTipoMovimiento")%>', sWidth:'75%', sClass: "centrado", bSortable: true}
                ];
                    
    tablaListadoOperacionesExpediente = $("#tablaOperacionesExpediente").DataTable( {
            data: cargarDatosListadoOperacionesExpediente(),
            aoColumns: columnas,
            sort : true,
            info : true,
            paginate : true,
            autoWidth: false,
            select: { style: 'single',
                      info: false
            },
            language: {
                "search": "<%=descriptor.getDescripcion("buscar")%>",
                "previous": "<%=descriptor.getDescripcion("anterior")%>",
                "next": "<%=descriptor.getDescripcion("siguiente")%>",
                "lengthMenu": "<%=descriptor.getDescripcion("mosFilasPag")%>",
                "zeroRecords": "<%=descriptor.getDescripcion("msgNoResultBusq")%>",
                "info": "<%=descriptor.getDescripcion("mosPagDePags")%>",
                "infoEmpty": "<%=descriptor.getDescripcion("noRegDisp")%>",
                "infoFiltered": "<%=descriptor.getDescripcion("filtrDeTotal")%>"
              }
    });
    
    $('#tablaOperacionesExpediente tbody').on('dblclick', 'tr', dblclickTablaOperacionesExpediente);
    $('#tablaOperacionesExpediente tbody').on('click', 'tr', dblclickTablaOperacionesExpediente);
    
    tablaListadoOperacionesExpediente.displayCabecera = true;
};

function cargarDatosListadoOperacionesExpediente() {
    var datosListadoOperacionesExpediente = new Array();

    <logic:iterate id="operaciones" name="FichaExpedienteForm" property="listaOperacionesExpediente">
    datosListadoOperacionesExpediente.push([
            '<bean:write name="operaciones" property="idOperacion" />',
            '<bean:write name="operaciones" property="fechaOperacionTxt" />',
            '<bean:write name="operaciones" property="tipoOperacionTxt"/>'
            ]);
    </logic:iterate>
    
    return datosListadoOperacionesExpediente;
}

function pulsarVer() {
    var fila = tablaListadoOperacionesExpediente.row({ selected: true });
alert(fila);
    if (fila && fila.data()) {
        verDetalleOperacion(fila.data()[0]);
    } else {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }
}

function dblclickTablaOperacionesExpediente() {
    var data = tablaListadoOperacionesExpediente.row( this ).data();
    verDetalleOperacion(data[0]);
}

function verDetalleOperacion(idOperacionSeleccionada) {
    if (idOperacionSeleccionada >= 0) {
        var source = "<c:url value='/sge/FichaExpediente.do?opcion=cargarDetalleOperacionExpediente&operacion='/>" + idOperacionSeleccionada;        
        abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp'/>?source=" + source,"aux",
            'width=700,height=550',function(){});
    } else {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjErrorInterno")%>');
    }
}

function pulsarSalir() {
    self.parent.parent.opener.retornoXanelaAuxiliar();
}

</script>
</head>
    <body class="bandaBody" onload="{inicializar();}">
    <html:form action="/sge/FichaExpediente" method="post">    
        <html:hidden property="listaOperacionesExpediente"/>
        <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("tit_listMovExp")%></div>
        <div class="contenidoPantalla scrollable-x scrollable-y">
            <div class="row">
                <table id="tablaOperacionesExpediente" class="xTabla compact tablaDatos"></table>
            </div>
            <!-------------------------------------- BOTONES. ------------------------------------------>
            <div class="row">
                <div class="botoneraPrincipal col-sm-12">
                   
                    <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSalir")%>" name="cmdSalir" onclick="pulsarSalir();" accesskey="C">
                </div>
            </div>
        </div>
    </html:form>
</body>    
</html>
