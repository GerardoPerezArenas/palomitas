<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="java.util.ArrayList" %>
<%@page import="es.altia.agora.business.administracion.mantenimiento.TipoDocumentoVO" %>
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

  ArrayList<TipoDocumentoVO> campos = (ArrayList<TipoDocumentoVO>)request.getAttribute("tipos_documento");
  String operador                   = (String)request.getAttribute("operador");
  String codAux                     = (String)request.getParameter("codigo");
  String codCampoSeleccionado       = (String)request.getAttribute("codCampoSeleccionado");
  
%>
<form name="criterioDocumento">
<table border="0" align="center" cellspacing="4" cellpadding="4">
<tr>
    <td class="etiqueta">
        <c:out value="${requestScope.nombre}"/>:
    </td>   
    <td>
        <select name="documento" id="documento" class="inputTexto" onchange="comprobarOperadorDocumento();">            
            <%
                for(int i=0;i<campos.size();i++)
                {
                    String codigo = campos.get(i).getCodigo();
                    String valor =  campos.get(i).getDescripcion();
            %>
                    <option value="<%=codigo%>" <% if(codAux.equalsIgnoreCase(codCampoSeleccionado) && operador.equalsIgnoreCase(codigo)){ %> selected <% } %>><%=valor%></option>
            <%
                }// for
            %>
        </select>
    </td>
    <td>
        <input type="text" name="txtDocumento" id="txtDocumento" value="" class="inputTextoObligatorio" maxlength="16" size="20"/>
    </td>
</tr>
</table>
</form>
<script type="text/javascript">
    var separador = "зе";
    var valores = '<c:out value="${requestScope.valores}"/>';
    var datos = new Array();
    datos = valores.split(separador);
    var codCampo             = '<c:out value="${requestScope.codigo}"/>';
    var codCampoSeleccionado = '<c:out value="${requestScope.codCampoSeleccionado}"/>';

    if(datos!=null && datos.length==1 && datos[0]!="" && codCampo!="" && codCampoSeleccionado!="" && codCampo.trim()==codCampoSeleccionado.trim()){
        document.forms[0].txtDocumento.value = datos[0];
    }

    comprobarOperadorDocumento();

</script>

