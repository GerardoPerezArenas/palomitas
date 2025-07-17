<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html:html>
<head>
    <jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<TITLE>::: Lista de anexos de un formulario PDF :::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
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

<SCRIPT language="JavaScript">
function inicializar() {
  //error del formulario
  var error = document.getElementById("errorGlobal").innerHTML;
  if ((error!=null) && (error!="")){
    jsp_alerta("A",error);
  }
  //si el formulario esta pendiente se pueden agregar anexos
  //si el formulario esta enviado y sin anexos no hay nada que hacer en esta pantalla
  <logic:empty name="anexos">
    <c:if test="${soloVer=='si'}">
        self.parent.opener.retornoXanelaAuxiliar("<%=descriptor.getDescripcion("msjNoAnexos")%>");
    </c:if>
  </logic:empty>
  <logic:notEmpty name="anexos">
    window.focus();
  </logic:notEmpty>
}

function pulsarAceptar() {
    self.parent.opener.retornoXanelaAuxiliar(null);
}

function pulsarView(codigo) {
  var formulario = document.forms[0].formulario.value;
  if((formulario != null) && (codigo != null)){
    window.open("<%=request.getContextPath()%>/VerDocumentoDatosSuplementarios?formPDF=" + formulario +
                  "&anexo=" + codigo + "&opcion=4", "ventana1",
                  "left=10, top=10, width=900, height=600, scrollbars=no, menubar=no, location=no, resizable=yes");
  }else{
      errorFaltanDatos();
  }
}

function pulsarDelete(codigo){
  var formulario = document.forms[0].formulario.value;
  if((formulario != null) && (codigo != null)){
      document.forms[0].accion.value = "del";
      document.forms[0].anexo.value = codigo;
      document.forms[0].submit();
  }else{
      errorFaltanDatos();
  }
}

function pulsarAdd(){
  var formulario = document.forms[0].formulario.value;
  if ((formulario != null) && (document.forms[0].descripcion.value!=null) && (document.forms[0].descripcion.value!="")
       && (document.forms[0].file.value!=null)){
      document.forms[0].accion.value = "add";
      document.forms[0].submit();
  }else{
      errorFaltanDatos();
  }
}

function errorFaltanDatos(){
  jsp_alerta("A","<%=descriptor.getDescripcion("msjErrorFaltanDatos")%>");
}
</SCRIPT>
</head>
<BODY class="bandaBody" onload="javascript:{inicializar();}">
    <div class="txttitblanco"><%=descriptor.getDescripcion("etListaAnexos")%></div>
    <div class="contenidoPantalla">
        <table style="width:100%">
        <tr>
            <td width="100%" height="30px" bgcolor="#7B9EC0" class="titulo">&nbsp;<%=descriptor.getDescripcion("etListaAnexos")%></td>
        </tr>
        <tr>
            <td style="width: 100%; background-color:#e6e6e6; text-align: left; vertical-align:top; padding-left: 20px; padding-top: 10px;">
                <html:form action="/sge/TratarAnexos.do" method="POST" enctype="multipart/form-data">
                <html:hidden property="formulario"/>
                <html:hidden property="anexo"/>
                <html:hidden property="accion"/>
                <!--------------------- Tabla anexos actuales ---------------------->
                <table style="width:95%;">
                    <c:forEach items="${anexos}" var="anexo">
                    <tr>
                        <td width="*" class="textoSuelto">- <c:out value="${anexo.descripcion}"/></td>
                        <td width="98">
                            <input style="borde-style: solid;  border-width: 1px 2px 2px 1px; border-color: #999999 #465736 #465736 #999999;" 
                                   type="button" name="cmdView" class="botonGeneral" accesskey="V" value="<%=descriptor.getDescripcion("gbVisualizar")%>"
                                   onClick="pulsarView(<c:out value="${anexo.codigo}"/>)">
                        </td>
                        <c:if test="${soloVer!='si'}">
                        <td width="98">
                            <input style="border-style: solid;  border-width: 1px 2px 2px 1px; border-color: #999999 #465736 #465736 #999999;" 
                                   type="button" name="cmdDelete" class="botonGeneral" accesskey="D" value="<%=descriptor.getDescripcion("gbEliminar")%>"
                                   onClick="pulsarDelete(<c:out value="${anexo.codigo}"/>)">
                        </td>
                        </c:if>
                    </tr>
                  </c:forEach>
                </table>
                <!--------------------- Tabla nuevo anexo ---------------------->
                <c:if test="${soloVer!='si'}">
                <table style="width:95%;margin-top: 18px">
                  <TR>
                      <td class="etiqueta"><%=descriptor.getDescripcion("etDesForm")%>:&nbsp;&nbsp; </td>
                      <TD>
                          <html:text maxlength="100" property="descripcion" styleClass="inputTextoObligatorio" size="47" />
                          <span class="error"><html:errors bundle="AnexosPDFErrorMessages" property="descripcion"/></span>
                      </td>
                  </tr>
                  <tr>
                      <td class="etiqueta"><%=descriptor.getDescripcion("etiqFichero")%>: &nbsp;&nbsp;</td>
                      <td><html:file size="33" property="file" styleClass="inputTextoObligatorio"/>
                         <span class="error"><html:errors bundle="AnexosPDFErrorMessages" property="file"/></span>
                      </td>
                  </tr>
                  <tr style="padding-top: 10px">
                      <TD colspan="2" align="center"><input style="border-style: solid;  border-width: 1px 2px 2px 1px; border-color: #999999 #465736 #465736 #999999;" 
                                type="button" name="cmdAdd" class="botonGeneral" accesskey="N" value="<%=descriptor.getDescripcion("gbAnadir")%>"
                                onClick="pulsarAdd()">
                      </td>
                  </tr>
                </table>
                </c:if>
                </html:form>
            </td>
        </tr>
    </table>
    <span style="position:absolute;height:0px;visibility:hidden"> id="errorGlobal"><html:errors bundle='AnexosPDFErrorMessages' property='org.apache.struts.action.GLOBAL_MESSAGE'/></span>
    <div class="botoneraPrincipal">
        <input class="botonGeneral" type="button" name="cmdAceptar" onclick="pulsarAceptar();" accesskey="A" value="<%=descriptor.getDescripcion("gbAceptar")%>">
    </div>
</div>
</BODY>

</html:html>
