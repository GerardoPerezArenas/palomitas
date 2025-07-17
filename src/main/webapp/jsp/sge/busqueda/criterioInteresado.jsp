<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%
  int idioma=1;
  int apl=4;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
        }
  }
  
  String codigo   = (String)request.getParameter("codigo");
  String codCampoSeleccionado   = (String)request.getAttribute("codCampoSeleccionado");
  String dValores   = (String)request.getAttribute("valores");
    
  String nombre="";
  String apellido1= "";
  String apellido2 = "";
  String[] datos = dValores.split("зе");

  if(datos!=null && datos.length==1){
      nombre = datos[0].trim();
  }else
  if(datos!=null && datos.length==2){
      nombre    = datos[0].trim();
      apellido1 = datos[1].trim();
  }else
  if(datos!=null && datos.length==3){
      nombre    = datos[0].trim();
      apellido1 = datos[1].trim();
      apellido2 = datos[2].trim();
  } 

%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />


<form name="criterioInteresado">
<table border="0" align="center">   
<tr>
    <td class="etiqueta" align="center">
        <%=descriptor.getDescripcion("gEtiqNombreRazon")%>
    </td>
    <td class="etiqueta" align="center">
        <%=descriptor.getDescripcion("gEtiqApellido1Part")%>
    </td>
    <td class="etiqueta" align="center">
        <%=descriptor.getDescripcion("gEtiqApellido2Part")%>
    </td>
</tr>
<tr>
    <td>
        <input type="text" name="nombre" id="nombre" size="40" class="inputTexto" maxlength="80"
               onkeyup="javascript:return SoloAlfanumericos_espacio(this);"/>
    </td>
    <td>
        <input type="text" name="apellido1" id="apellido1" size="40" class="inputTexto" maxlength="25"
               onkeyup="javascript:return SoloAlfanumericos_espacio(this);"/>
    </td>
    <td>
        <input type="text" name="apellido2" id="apellido2" size="40" class="inputTexto" maxlength="25"
               onkeyup="javascript:return SoloAlfanumericos_espacio(this);"/>
    </td>
</tr>
<tr>
    <td></td>
</tr>
</table>
</form>

<script type="text/javascript">

var codigo               = "<%=codigo%>";
var codCampoSeleccionado = "<%=codCampoSeleccionado%>";

if(codigo==codCampoSeleccionado){
    document.forms[0].nombre.value =    "<%=nombre%>";
    document.forms[0].apellido1.value = "<%=apellido1%>";
    document.forms[0].apellido2.value = "<%=apellido2%>";
}

</script>