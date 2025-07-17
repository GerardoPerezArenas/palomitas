<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<form name="criterioTexto">
<%
    String operador = (String)request.getAttribute("operador");
    String codigo   = (String)request.getParameter("codigo");
    String codCampoSeleccionado   = (String)request.getAttribute("codCampoSeleccionado");

%> 
<table border="0" align="center" cellspacing="4" cellpadding="4">
<tr>
    <td class="etiqueta">
        <c:out value="${requestScope.nombre}"/>:        
    </td>
    <td>
        <select name="valorOperadorCriterio" id="valorOperadorCriterio" class="inputTexto">
            <%--
            <option value=""></option>
            --%>
            <option value="0" <% if("0".equals(operador) && (codigo.equalsIgnoreCase(codCampoSeleccionado))){ %> selected <%}%>> = </option>
            <option value="6" <% if("6".equals(operador) && (codigo.equalsIgnoreCase(codCampoSeleccionado))){ %> selected <%}%>> &lt> </option>
            <option value="7" <% if("7".equals(operador) && (codigo.equalsIgnoreCase(codCampoSeleccionado))){ %> selected <%}%>> LIKE </option>            
        </select>
    </td>
    <td>
        <input type="text" name="valorBusqueda1" id="valorBusqueda1" class="inputTextoObligatorio" style="width:350px" maxlength="4000"
               onkeyup="javascript:return SoloAlfanumericos(this);">
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
        document.forms[0].valorBusqueda1.value = datos[0];
    }
    
</script>