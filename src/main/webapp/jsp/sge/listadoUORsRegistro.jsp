<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html:html>
<head>
<jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title> Lista de unidades de registro </title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<%
    int idioma=1;
    int apl=1;
    String css = "";
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
          css = usuario.getCss();
        }
  }
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<html:rewrite page='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >
<script type="text/javascript">

var listaCodUorsRegistro = new Array();

function inicializar() {
    window.focus();
    cargaTabla();
}

function pulsarAceptar() {
  if (tablaUors.selectedIndex < 0) {
      jsp_alerta('A','<%=descriptor.getDescripcion("msjNoSelecFila")%>');
  } else {
      self.parent.opener.retornoXanelaAuxiliar(listaCodUorsRegistro[tablaUors.selectedIndex]);
  }
}

function pulsarCancelar() {
  self.parent.opener.retornoXanelaAuxiliar();
}

</script>
</head>
<body class="bandaBody">
<html:form action="MantAsuntos" >
    <div class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_selOfiReg")%></div>
    <div class="contenidoPantalla">
        <table style="width:100%">
            <tr>
                <td id="tabla" width="100%"  valign="middle"  ondblclick="javascript:pulsarAceptar();"></td>
            </tr>
        </table>
        <div id="capaBotones" name="capaBotones" class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAceptar")%> name="cmdAceptar" onClick="pulsarAceptar();" alt="<%= descriptor.getDescripcion("gbAceptar")%>" title="<%= descriptor.getDescripcion("gbAceptar")%>"/>
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdCancelar" onClick="pulsarCancelar();" alt="<%= descriptor.getDescripcion("gbCancelar")%>" title="<%= descriptor.getDescripcion("gbCancelar")%>"/>
        </div>
    </div>
</html:form>
<script type="text/javascript">
var tablaUors=new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>',
        '<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>',
        '<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', 
        '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>',
        '<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

tablaUors.addColumna('420','center','<%= descriptor.getDescripcion("gEtiq_nombre")%>');
tablaUors.displayCabecera=true;

function cargaTabla(){

  var listaDescUorsRegistro = new Array();
  // Cargar lista de uors de registro
  <logic:present name="TramitacionExpedientesForm" property="uorsDeRegistro">
    var i=0;
    <logic:iterate id="uor" name="TramitacionExpedientesForm" property="uorsDeRegistro">
      listaCodUorsRegistro[i] = <bean:write name="uor" property="uor_cod"/>;
      listaDescUorsRegistro[i] = ["<str:escape><bean:write name="uor" property="uor_nom" filter="false"/></str:escape>"];
      i++;
    </logic:iterate>
  </logic:present>

  tablaUors.lineas=listaDescUorsRegistro;
  tablaUors.displayTabla();
}
</script>
<script> inicializar(); </script>
</body>
</html:html>
