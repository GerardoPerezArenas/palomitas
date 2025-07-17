<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<form name="criterioNumerico">
<%
    String operador = (String)request.getAttribute("operador");
    String codigo   = (String)request.getParameter("codigo");
    String codCampoSeleccionado   = (String)request.getAttribute("codCampoSeleccionado");
%>
<div id="criterio">
    <table align="center" cellpadding="4" cellspacing="4">
    <tr>
        <td class="etiqueta">
            <c:out value="${requestScope.nombre}"/>:
        </td>
        <td>
            <select name="valorOperadorCriterio" id="valorOperadorCriterio" class="inputTexto" onchange="javascript:comprobarOperadorNumerico();">
                <%--
                <option value=""></option>
                --%>
                <option value="0" <% if("0".equals(operador) && (codigo.equalsIgnoreCase(codCampoSeleccionado))){ %> selected <%}%>> = </option>
                <option value="1" <% if("1".equals(operador) && (codigo.equalsIgnoreCase(codCampoSeleccionado))){ %> selected <%}%>> > </option>
                <option value="2" <% if("2".equals(operador) && (codigo.equalsIgnoreCase(codCampoSeleccionado))){ %> selected <%}%>> >= </option>
                <option value="3" <% if("3".equals(operador) && (codigo.equalsIgnoreCase(codCampoSeleccionado))){ %> selected <%}%>> &lt </option>
                <option value="4" <% if("4".equals(operador) && (codigo.equalsIgnoreCase(codCampoSeleccionado))){ %> selected <%}%>> &lt= </option>
                <option value="5" <% if("5".equals(operador) && (codigo.equalsIgnoreCase(codCampoSeleccionado))){ %> selected <%}%>> ENTRE </option>
                <option value="6" <% if("6".equals(operador) && (codigo.equalsIgnoreCase(codCampoSeleccionado))){ %> selected <%}%>> &lt> </option>
            </select>
        </td>
        <td>
            <input type="text" name="valorBusqueda1" id="valorBusqueda1" class="inputTextoObligatorio" style="width:80px" maxlength="11" onkeyup="javascript:return SoloDigitosNumericos(this);">
        </td>    
        <td id="segundoCriterio" style="visibility:hidden;">
            <input type="text" name="valorBusqueda2" id="valorBusqueda2" class="inputTextoObligatorio" style="width:80px" maxlength="11" onkeyup="javascript:return SoloDigitosNumericos(this);">
        </td>    
    </tr>
    </table>
</div>
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
    else
    if(datos!=null && datos.length==2 && datos[0]!="" && datos[1]!="" && codCampo!="" && codCampoSeleccionado!="" && codCampo.trim()==codCampoSeleccionado.trim()){
        document.forms[0].valorBusqueda1.value = datos[0];
        document.forms[0].valorBusqueda2.value = datos[1];
        comprobarOperadorNumerico();
    }
</script>