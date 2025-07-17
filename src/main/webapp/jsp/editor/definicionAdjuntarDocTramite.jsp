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
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>

<script type="text/javascript">

function inicializar() {
  document.getElementById("cmdAnadir").disabled = true;
}

function pulsarAceptar() {
    var respuesta;
    var tituloFichero=document.getElementById("tituloFichero").value;
    if(document.getElementById("relacionS").checked){
        respuesta = ["S","N"];
    }else{
        respuesta = ["N",document.getElementById("interesadoS").checked?"S":"N"];
    }
    respuesta.push(tituloFichero);
    var archivo= document.getElementById("ficheroAdjuntar").files[0];
    respuesta.push(archivo);
    self.parent.opener.retornoXanelaAuxiliar(respuesta);
}

function pulsarCancelar() {
    self.parent.opener.retornoXanelaAuxiliar();
}

function onClickParaRelacion(){
    if (document.getElementById("relacionS").checked){
        document.getElementById("interesadoN").checked = true;
        document.getElementById("filaInteresado").style.display = "none";
    } else
        document.getElementById("filaInteresado").style.display = "";
}

function habilitarAceptar(){
    var fichero = document.getElementById("ficheroAdjuntar").value;
    var nombreFichero=document.getElementById("tituloFichero").value;
    if(nombreFichero!="" && (fichero!=null || fichero.length>=1)){
        // Se comprueba que su extensión sea una de las válidas
        var num = fichero.lastIndexOf(".");
        var ext = fichero.substring(num + 1,fichero.length);
        if(comprobarExtension(ext)){
            //Se habilita el botón aceptar
            document.getElementById("cmdAnadir").disabled = false;
        }else{
            // Se deshabilita el botón de aceptar
            document.getElementById("cmdAnadir").disabled = true;
                          
        }
    }
 }
                
 /**Comprobariamos que la extensión del fichero adjunto es .odt */
 function comprobarExtension(ext){
    return true;
 }
          
 /** Reseteamos los campos del formulario */
  function resetearForm(){
    document.getElementById("relacionN").checked=true;
    document.getElementById("interesadoN").checked=true;
    document.getElementById("tituloFichero").value="";
    document.getElementById("ficheroAdjuntar").value="";
    document.getElementById("filaInteresado").style.display="";
 }
          
</script>
</head>

<body onload="javascript:{inicializar();}">
<html:hidden  property="opcion" value=""/>
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titDefDocTramite")%></div>
    <div class="contenidoPantalla">
      
        <table width="100%">
            <tr>                                                       
                <td style="width: 50%" class="etiqueta">
                    <%=descriptor.getDescripcion("msjGenParaRelac")%>
                </td>
                <td style="width: 25%" class="etiqueta">
                    <input type="radio" id="relacionS" name="paraRelacion" styleClass="textoSuelto" value="S" onclick="onClickParaRelacion();"/> <%=descriptor.getDescripcion("etiq_si")%>&nbsp;
                </td>
                <td style="width: 25%" class="etiqueta">
                    <input type="radio" id="relacionN" name="paraRelacion" styleClass="textoSuelto" value="N" onclick="onClickParaRelacion();" checked/> <%=descriptor.getDescripcion("etiq_no")%>&nbsp;
                </td>
            </tr>
            <tr id="filaInteresado">                                                       
                <td style="width: 50%" class="etiqueta">
                    <%=descriptor.getDescripcion("msjGenPorInt")%>
                </td>
                <td style="width: 25%" class="etiqueta">
                    <input type="radio" id="interesadoS" name="porInteresado" styleClass="textoSuelto" value="S"/> <%=descriptor.getDescripcion("etiq_si")%>&nbsp;
                </td>
                <td style="width: 25%" class="etiqueta">
                    <input type="radio" id="interesadoN" name="porInteresado" styleClass="textoSuelto" value="N" checked/> <%=descriptor.getDescripcion("etiq_no")%>&nbsp;
                </td>
           </tr>
           <tr>
                <td colspan="2" class="etiqueta"><%= descriptor.getDescripcion("etiqTitDoc")%></td>
           </tr>
           <tr>
                <td colspan="2">
                    <input type="text" id="tituloFichero" name="tituloFichero" style="width: 356px" id="" class="inputTexto"/>
                </td>
           </tr>
           <tr id="etiqFichero">
                <td  colspan="2" class="etiqueta"><%= descriptor.getDescripcion("etiqRutaFichero") %></td>
           </tr>
           <tr id="txtFichero" style="display:inline;">
                <td colspan="2">
                    <input type="file" name="fichero" id="ficheroAdjuntar" class="inputTexto" onchange="habilitarAceptar();" title="Fichero" size="57">
                </td>      
           </tr>
           <tr>
               <td class="columnP">*Tipo permitido: .odt </td>
           </tr>
        </table>
        <div class="botoneraPrincipal">
            <input type="button" value="<%= descriptor.getDescripcion("etiq_limpiar") %>" class="botonGeneral" name="Vaciar" onclick="resetearForm();"/>
            <input type= "button" class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbAceptar")%>" id="cmdAnadir" name="cmdAnadir" onclick="pulsarAceptar();">
            <input type= "button"  class="botonGeneral" accesskey="C" value="<%=descriptor.getDescripcion("gbCancelar")%>" id="cmdCancelar" name="cmdCancelar"  onclick="pulsarCancelar();">
        </div>
 
    </div>
</body>
</html:html>