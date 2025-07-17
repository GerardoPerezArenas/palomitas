<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html:html>

<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

<TITLE>::: LISTA TRAMITES DEL FLUJO DE SALIDA :::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />

<!-- Estilos -->


<%
  int idioma=1;
  int apl=1;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
        }
  }

%>


<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">


<SCRIPT language="JavaScript">



var datos = new Array();
var cont = 0;

function inicializar() {
  document.forms[0].codMunicipio.value = '<bean:write name="TablasIntercambiadorasForm" property="codMunicipio"/>';
  document.forms[0].codProcedimiento.value = '<bean:write name="TablasIntercambiadorasForm" property="codProcedimiento"/>';
  document.forms[0].codTramite.value = '<bean:write name="TablasIntercambiadorasForm" property="codTramite"/>';
  document.forms[0].numeroCondicionSalida.value = '<bean:write name="TablasIntercambiadorasForm" property="numeroCondicionSalida"/>';
  <logic:iterate id="elemento" name="TablasIntercambiadorasForm" property="listaTramitesSeleccion">
    datos[cont] = ['<bean:write name="elemento" property="codTramiteFlujoSalida"/>',
                      '<bean:write name="elemento" property="nombreTramiteFlujoSalida"/>'];
    cont++;
  </logic:iterate>
  tab.lineas=datos;
  refresca();
}

function pulsarCancelar() {
  top.close();
}

</SCRIPT>

</head>

<BODY class="bandaBody" onload="javascript:{inicializar();}">

<html:form action="/sge/TablasIntercambiadoras.do" target="_self">

<html:hidden  property="opcion" value=""/>
<html:hidden  property="codMunicipio" />
<html:hidden  property="codProcedimiento" />
<html:hidden  property="codTramite" />
<html:hidden  property="numeroCondicionSalida" />

<div class="txttitblanco"><%=descriptor.getDescripcion("tit_listTramSal")%></div>
<div class="contenidoPantalla">
    <table style="width: 100%">
    <tr>
        <TD id="tabla"></TD>
    </tr>
    </table>
    <DIV STYLE="position:absolute;height:0px;" class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdCancelar" onClick="pulsarCancelar();">
    </div>    
</div>    
</html:form>

<script language="JavaScript1.2">

// TABLA PRIMERA
tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

tab.addColumna('150','center','<%= descriptor.getDescripcion("gEtiq_codigo")%>');
tab.addColumna('500','center','<%= descriptor.getDescripcion("gEtiq_nombre")%>');
tab.displayCabecera=true;
tab.displayTabla();
tab.readOnly = true;

function refresca() {
  tab.displayTabla();
}


function checkKeysLocal(evento,tecla) {
      var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else
        teclaAuxiliar = evento.which;

  if (teclaAuxiliar == 38 || teclaAuxiliar == 40){
    upDownTable(tab,datos,teclaAuxiliar);
  }

  keyDel(evento);
}


</script>


<script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>

</BODY>

</html:html>
