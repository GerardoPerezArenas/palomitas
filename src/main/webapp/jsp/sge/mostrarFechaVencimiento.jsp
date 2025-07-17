<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="java.text.MessageFormat"%>

<html:html>

<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

<TITLE>::: Fecha de vencimiento:::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />

<!-- Estilos -->



<%
  int idioma=1;
  int apl=1;
  int munic = 0;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
          munic = usuario.getOrgCod();
        }
  }
  String municipio = Integer.toString(munic);
  String aplicacion = Integer.toString(apl);
  String modoInicio = "";
  if (session.getAttribute("modoInicio") != null) {
    modoInicio = (String) session.getAttribute("modoInicio");
    session.removeAttribute("modoInicio");
  }
  String lectura = "";
  if (session.getAttribute("lectura") != null) {
    lectura = (String) session.getAttribute("lectura");
    session.removeAttribute("lectura");
  }



%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />



<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>

<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">

<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>



<SCRIPT type="text/javascript">
var argumentos = self.parent.opener.xanelaAuxiliarArgs;

function getXMLHttpRequest(){
    var aVersions = [ "MSXML2.XMLHttp.5.0",
    "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
    "MSXML2.XMLHttp","Microsoft.XMLHttp"
    ];

    if (window.XMLHttpRequest){
        // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
        return new XMLHttpRequest();
    }
    else
        if (window.ActiveXObject){
        // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
        for (var i = 0; i < aVersions.length; i++) {
            try {
                var oXmlHttp = new ActiveXObject(aVersions[i]);
                return oXmlHttp;
            }catch (error) {
            //no necesitamos hacer nada especial
            }
        }
        }
    }
function pulsarSalir() {
  self.parent.opener.retornoXanelaAuxiliar();
}


</SCRIPT>

</head>

<BODY class="bandaBody" >
    <div class="txttitblanco">Fecha de vencimiento</div>
    <div class="contenidoPantalla">
        <table >
            <TR>
                <td class="etiqueta"><%= descriptor.getDescripcion("etiqFechaOriginal")%></td>
            </tr>    
            <tr>
                <td>
                    <input type="text" id="fecha" name="fecha" value="<%=request.getAttribute("fecha")%>" size="25" maxlength="20"readonly="true"/>
                </td>
            </tr>
            <TR>
                <td class="etiqueta"><%= descriptor.getDescripcion("etiqPlazo")%></td>
            </tr>    
            <tr>
                <td colspan="2">
                    <input type="text" id="rdoPeriodoPlazo" name="rdoPeriodoPlazo" value="<%=request.getAttribute("rdoPeriodoPlazo")%>" size="25" maxlength="20"readonly="true"/>
                </td>
            </tr>
            <TR>
                <td class="etiqueta"><%= descriptor.getDescripcion("etiqFechaVencimiento")%></td>
            </tr>    
            <tr>
                <td>
                    <input type="text" id="fechaVencimiento" name="fechaVencimiento" value="<%=request.getAttribute("fechaVencimiento")%>" size="25" maxlength="20"readonly="true"/>
                </td>
            </TR>
        </table>
        <DIV id="capaBotones1" name="capaBotones1" class="botoneraPrincipal">
            <INPUT type= "button" class="botonGeneral" accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>" name="cmdSalir"  onclick="pulsarSalir();">
        </DIV>
    </div>
</BODY>
</html:html>



