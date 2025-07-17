<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.Vector" %>

<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title>Lista de plantillas</title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<!-- Estilos -->
<%
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  int idioma=1;
  int apl = 2;
  if (session.getAttribute("usuario") != null){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    idioma = usuarioVO.getIdioma();
    apl = usuarioVO.getAppCod();
  }

%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
<script type="text/vbscript" src="<c:url value='/scripts/documentos.vbs'/>"></script>
<script type="text/javascript">

function pulsarAceptar(){
  var datos = new Array();
  datos[0] = document.forms[0].nuevoCodigo.value
  self.parent.opener.retornoXanelaAuxiliar(datos);
}

function pulsarSalir(){
  self.parent.opener.retornoXanelaAuxiliar();
}

function inicializar() {

}


function SoloCaracterValidos(objeto) {
   var valores='1234567890ABCDEFGHIJKLMNÑOPQRSTUVWYXZ';
   var numeros='1234567890';
   xAMayusculas(objeto);
   
    if (objeto){
        var original = objeto.value;

        var salida = "";
        for(i=0;i<original.length;i++){

            if(valores.indexOf(original.charAt(i).toUpperCase())!=-1 || numeros.indexOf(original.charAt(i).toUpperCase())!=-1 || original.charAt(i)==" "){
               var valido = true;
               if(numeros.indexOf(original.charAt(i).toUpperCase())!=-1 && original.length==1) valido = false;
               if(valido) salida = salida + original.charAt(i);
            }
        }
        objeto.value=salida.toUpperCase();
    }
}
                    
</script>

</head>

<body onload="javascript:{inicializar();}" >
<html:form action="/sge/DefinicionProcedimientos.do" target="_self">

<html:hidden property="opcion" value=""/>
<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titNueCodProc")%></div>
<div class="contenidoPantalla">
    <table style="width:100%">
        <tr>
            <td style="width:50%;text-align:right;padding-right:10px" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_codigo")%>:</td>
            <td style="width:50%;">
                <input type="Text" name="nuevoCodigo" class="inputTextoObligatorio" size="8" maxlength="5" onkeyup="javascript:return SoloCaracterValidos(this);">
            </td>
        </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbAceptar")%>' name="botonAceptar" onClick="pulsarAceptar();">
        <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbSalir")%>' name="botonSalir" onClick="pulsarSalir();">
    </div>
</div>
</html:form>

<script language="JavaScript1.2">

</script>

</body>

</html:html>
