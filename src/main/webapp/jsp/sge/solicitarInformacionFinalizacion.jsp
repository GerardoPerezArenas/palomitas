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
 var motivo="";
  var persAutoriza="";

function inicializar(){
    motivo='<bean:write name="TramitacionExpedientesForm" property="justificacion"/>';
    persAutoriza='<bean:write name="TramitacionExpedientesForm" property="personaAutoriza"/>';
    document.forms[0].cmdAceptar.style.display = 'none';
    document.forms[0].motivo.readOnly = true;
    document.forms[0].autorizo.readOnly = true;
    document.forms[0].motivo.value = motivo;
    document.forms[0].autorizo.value = persAutoriza;
}
function pulsarAceptar() {
 
    var retorno = new Array();
    var autorizo= document.forms[0].autorizo.value;
    var motivo=  document.forms[0].motivo.value;   
    if (autorizo!="" && motivo!=""){
        retorno = [motivo, autorizo];
        window.opener.retornoXanelaAuxiliar(retorno);
    }else{
         jsp_alerta("A","<%=descriptor.getDescripcion("msjRellenarCampos")%>");
    }

}


function pulsarCancelar() {
   window.opener.retornoXanelaAuxiliar();
}

</SCRIPT>

</head>

<body class="bandaBody" onload="inicializar();">

<html:form action="/sge/FichaExpediente.do" target="_self">

<html:hidden  property="opcion" value=""/>

<table id="tabla1" style="width: 100%;" cellpadding="0px" cellspacing="0px">
        <tr>
            <td id="titulo" style="width: 100%" class="txttitblanco">&nbsp;<%=descriptor.getDescripcion("etiq_finalExpediente")%></td>
        </tr>
        <tr>
            
                 <table cellspacing="0px" cellpadding="0px" border="0px" >
                    <tr>
                        <td class="sub3titulo">&nbsp;<%=descriptor.getDescripcion("etiq_motivo")%></td>
                    </tr>
                    <tr>
                        <td style="padding-top: 10px; padding-bottom:10px">
                              <html:textarea styleClass="textareaTexto" cols="140" rows="8" property="motivo"
                           onkeyup="return xValidarCaracteres(this);" value=""></html:textarea>
                        </td>
                    </tr>
                    <tr>
                         <td class="sub3titulo">&nbsp;<%=descriptor.getDescripcion("etiq_personaAutoriza")%></td>
                    </tr>
                    <tr>
                         <td style="padding-top: 10px; padding-bottom:30px">
                             <html:text styleClass="inputTexto" property="autorizo" size="151" maxlength="80"  onkeyup="return xValidarCaracteres(this);" value=""></html:text>
                         </td>
                    </tr>
                </table>
                     
           
        </tr>
    </table>
<div class="botoneraPrincipal">
    <input type="button" class="botonGeneral" accesskey="A" value='<%=descriptor.getDescripcion("gbAceptar")%>' name="cmdAceptar" onClick="pulsarAceptar();"/>
    <input type="button" class="botonGeneral" accesskey="C" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelar" onClick="pulsarCancelar();"/>
</div>
</html:form>
</body>
</html:html>
