<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionExpedientesForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html:html>

<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

<TITLE>::: Solicitar informacion de finalizacion de un expediente :::</TITLE>
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
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>

<SCRIPT type="text/javascript">

function inicializar(){


         document.forms[0].cmdAceptar.disabled=false;
         document.forms[0].motivo.readOnly = false;
         document.forms[0].autorizo.readOnly = false;

     document.forms[0].motivo.value = "";
     document.forms[0].autorizo.value = "";
}
function pulsarAceptar() {

    var retorno = new Array();
    var autorizo= document.forms[0].autorizo.value;
    var motivo=  document.forms[0].motivo.value;
    if (autorizo!="" && motivo!=""){
        retorno = [motivo, autorizo];
        self.parent.opener.retornoXanelaAuxiliar(retorno);
    }else{
         jsp_alerta("A","<%=descriptor.getDescripcion("msjRellenarCampos")%>");
    }
}



function xValidarCaracteresAnulacion(objeto)
{
        var cadena1 = "\"";
        var cadena2 = "\\";
        var cadena3 = "\%";
        var cadena4 = "'";
        var retornar=false;

        if (objeto){
                var original = objeto.value;
                var salida = "";
                for(i=0;original!=undefined && i<original.length;i++){                   
                        if(cadena1.indexOf(original.charAt(i))==-1 && cadena2.indexOf(original.charAt(i))==-1 && cadena3.indexOf(original.charAt(i))==-1 && cadena4.indexOf(original.charAt(i))==-1){
                                salida = salida + original.charAt(i);
                        }else retornar=true;
                }
                if(retornar)objeto.value=salida;
        }
}

function pulsarCancelar() {
  self.parent.opener.retornoXanelaAuxiliar();
}

</SCRIPT>

</head>

<BODY class="bandaBody" onload="inicializar();">

<html:form action="/sge/FichaExpediente.do" target="_self">
<html:hidden  property="opcion" value=""/>
    <div id="titulo" class="txttitblanco">&nbsp;<%=descriptor.getDescripcion("etiq_finalExpediente")%></div>
    <div class="contenidoPantalla">
    <table id="tabla1" style="width: 100%;" cellpadding="0px" cellspacing="0px">
        <tr>
            <td class="sub3titulo"><%=descriptor.getDescripcion("etiq_motivo")%></td>
        </tr>
        <tr>
            <td>
                  <html:textarea styleClass="textareaTexto" cols="160" rows="8" property="motivo" 
                        style="width:100%;margin-bottom:10px"
                        onkeyup="return xValidarCaracteresAnulacion(this);" value=""></html:textarea>
            </td>
        </tr>
        <tr>
             <td class="sub3titulo"><%=descriptor.getDescripcion("etiq_personaAutoriza")%></td>
        </tr>
        <tr>
             <td>
               <html:text styleClass="inputTexto" property="autorizo" style="width:100%" maxlength="80"  onkeyup="return xValidarCaracteresAnulacion(this);" value=""/>
             </td>
        </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type="button" class="botonGeneral" accesskey="A" value='<%=descriptor.getDescripcion("gbAceptar")%>' name="cmdAceptar" onClick="pulsarAceptar();"/>
        <input type="button" class="botonGeneral" accesskey="C" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelar" onClick="pulsarCancelar();"/>
    </div>
</div>
</html:form>
</BODY>
</html:html>
