<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title>::: Definicion Documento Tramite :::</title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<%
  int idioma=1;
  int apl=4;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
        }
  }

 String mostrarEtiq = request.getParameter("listaEtiquetas"); 
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>

<script type="text/javascript">

var mostrarEtiquetas = '<%=mostrarEtiq%>';

function inicializar() {
  
}

function pulsarAceptar() {
    var resposta;
    
  if (document.getElementById("paraRelacionS").checked)
        resposta = ["S","N"];
    else
        resposta = ["N",document.getElementById("porInteresadoS").checked?"S":"N"];
    
    if(mostrarEtiquetas!='SI') resposta.push(document.getElementById("editorTextoW").checked?"WORD":"OOFFICE");
    
    self.parent.opener.retornoXanelaAuxiliar(resposta);
}

function pulsarCancelar() {
    self.parent.opener.retornoXanelaAuxiliar();
}

function onClickParaRelacion(){
    if (document.getElementById("paraRelacionS").checked){
        document.getElementById("porInteresadoN").checked = true;
        document.getElementById("filaPorInteresado").style.display = "none";
    } else
        document.getElementById("filaPorInteresado").style.display = "";
}

</script>
</head>

<body onload="javascript:{inicializar();}">
<html:hidden  property="opcion" value=""/>
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titDefDocTramite")%></div>
    <div class="contenidoPantalla">
        <table width="100%">
            <tr>                                                       
                <td style="width: 50%" class="columnP">
                    <%=descriptor.getDescripcion("msjGenParaRelac")%>
                </td>
                <td style="width: 25%" class="columnP">
                    <input type="radio" id="paraRelacionS" name="paraRelacion" styleClass="textoSuelto" value="S" onclick="onClickParaRelacion();"/> <%=descriptor.getDescripcion("etiq_si")%>&nbsp;
                </td>
                <td style="width: 25%" class="columnP">
                    <input type="radio" id="paraRelacionN" name="paraRelacion" styleClass="textoSuelto" value="N" onclick="onClickParaRelacion();" checked/> <%=descriptor.getDescripcion("etiq_no")%>&nbsp;
                </td>
            </tr>
            <tr id="filaPorInteresado">                                                       
                <td style="width: 50%" class="columnP">
                    <%=descriptor.getDescripcion("msjGenPorInt")%>
                </td>
                <td style="width: 25%" class="columnP">
                    <input type="radio" id="porInteresadoS" name="porInteresado" styleClass="textoSuelto" value="S"/> <%=descriptor.getDescripcion("etiq_si")%>&nbsp;
                </td>
                <td style="width: 25%" class="columnP">
                    <input type="radio" id="porInteresadoN" name="porInteresado" styleClass="textoSuelto" value="N" checked/> <%=descriptor.getDescripcion("etiq_no")%>&nbsp;
                </td>
            </tr>
              <%
                    if (!"SI".equals(mostrarEtiq)) {
               %>          
            <tr>                                                       
                <td style="width: 50%" class="columnP">
                    <%=descriptor.getDescripcion("editorTexto")%>
                </td>
                <td style="width: 25%" class="columnP">
                    <input type="radio" id="editorTextoW" name="editorTexto" styleClass="textoSuelto" value="WORD" checked/> Microsoft Word&nbsp;
                </td>
                <td style="width: 25%" class="columnP">
                    <input type="radio" id="editorTextoO" name="editorTexto" styleClass="textoSuelto" value="OOFFICE" /> Open Office&nbsp;
                </td>
            </tr>
              <%} %>
        </table>
        <div class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAnadir" onclick="pulsarAceptar();">
            <input type= "button"  class="botonGeneral" accesskey="C" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelar"  onclick="pulsarCancelar();">
        </div>
    </div>
</body>
</html:html>