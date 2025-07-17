<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.sge.DefinicionProcedimientosValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.DefinicionProcedimientosForm" %>
<%@page import="java.util.Vector"%>
<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<TITLE>::: EXPEDIENTES - LISTADO RELACION PROCEDIMIENTOS :::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<%
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  int idioma=0;
  int apl=0;
  String funcion = "";

  if ((session.getAttribute("usuario") != null)){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    idioma = usuarioVO.getIdioma();
    apl = usuarioVO.getAppCod();
  }
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<style type="text/css">
   TR.gris TD {background-color:#C0C0C0;color:white;}
</style>
<SCRIPT type="text/javascript">    
    var organizaciones = new Array();
    var organizacionesTabla = new Array();

    function inicializar(){
        var contador = 0;
        <logic:iterate name="organizacionesImportacion" id="organizacion" scope="request">
            organizaciones[contador]=['<bean:write name="organizacion" property="codOrganizacion" ignore="true"/>','<bean:write name="organizacion" property="descripcionOrganizacion" ignore="true"/>'];
            organizacionesTabla[contador]=['<bean:write name="organizacion" property="descripcionOrganizacion" ignore="true"/>','<bean:write name="organizacion" property="descripcionOrganizacion" ignore="true"/>'];
            contador++;
        </logic:iterate>

        // Se indica cual es el contenido de la tabla con las posibles organizaciones a seleccionar por el usuario
        tab.lineas = organizacionesTabla;
        refresh();
    }

    function pulsarSalirCancelar(){        
        window.location.href='<c:url value="/sge/DefinicionProcedimientos.do?opcion=inicio"/>';
    }
    
    function seleccionRegistro(fila){        
        if(fila!=null && fila>=0 && fila<organizaciones.length){
            var idOrganizacion   = organizaciones[fila][0];
            var descOrganizacion = organizaciones[fila][1];
            var url = '<c:url value="/sge/DefinicionProcedimientos.do"/>?opcion=importarDesdeOrganizacion&codMunicipio=' + idOrganizacion + "&importar=si" ;            
            window.location.href=url;
        }
    }

</SCRIPT>
</head>
<BODY class="bandaBody" onload="javascript:{
              inicializar();}">

    <div class="txttitblanco"><%=descriptor.getDescripcion("tit_Sel")%></div>
    <div class="contenidoPantalla">
        <TABLE class="cuadroFondoBlanco" width="100%" cellspacing="0px" cellpadding="0px">
            <TR>
                <TD id="tabla" align="center"></TD>
            </TR>
            <TR>
                <TD align="center" valign="top">                                             

                </TD>
            </TR>
        </TABLE>
        <DIV id="capaBotonesConsulta" name="capaBotonesConsulta" class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdSalir" onclick="pulsarSalirCancelar();return false;">
        </DIV>
    </div>

<script type="text/javascript">
    var tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
    tab.addColumna('200','center',"<%=descriptor.getDescripcion("tit_Org")%>");
    tab.addColumna('700','left',"<%=descriptor.getDescripcion("tit_Ent")%>");
    tab.displayCabecera=true;
    tab.displayTabla();
    tab.colorLinea=function(rowID) {      
    }

  function refresh(){
      tab.displayTabla();
  }

  function callFromTableTo(rowID,tableName){
    if(tab.id == tableName){      
      fila=parseInt(rowID);
      seleccionRegistro(fila);
    }
  }
</script>

</BODY>
</html:html>

