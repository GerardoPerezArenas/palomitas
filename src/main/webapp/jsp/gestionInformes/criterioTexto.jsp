<%--
  Created by IntelliJ IDEA.
  User: susana.rodriguez
  Date: 10-may-2007
  Time: 13:04:45
  To change this template use File | Settings | File Templates.
--%>
<%  String campo = request.getParameter("campo");
    String titulo = request.getParameter("titulo");
    String etiquetaEsNulo = request.getParameter("etiquetaEsNulo");
    String etiquetaEsNoNulo = request.getParameter("etiquetaEsNoNulo");
%>
<div id="criterio<%=campo%>" class="tableContainerComboCriteriosParametro">
    <span class="etiq" id="tituloCriterio<%=campo%>" style="width:175px"><%=titulo%></span>
    <select name="valorOperCriterio<%=campo%>" id="valorOperCriterio<%=campo%>" class="inputTexto" onchange="comprobarOperTexto(<%=campo%>);">
        <option value="5"> = </option>
        <option value="6"> &lt> </option>
        <option value="7"> LIKE </option>
        <option value="8"> <%=etiquetaEsNulo%> </option>
        <option value="9"> <%=etiquetaEsNoNulo%> </option>
    </select>

    <input type="text" name="valor1Criterio<%=campo%>" id="valor1Criterio<%=campo%>" class="inputTextoObligatorio" style="width:200px" maxlength="100" onblur="javascript:return xAMayusculas(this);">
    <input type="hidden" name="valor2Criterio<%=campo%>" id="valor2Criterio<%=campo%>" disabled>
    <input type="hidden" name="origenCriterio<%=campo%>" id="origenCriterio<%=campo%>" disabled>
    <input type="hidden" name="tablaCriterio<%=campo%>" id="tablaCriterio<%=campo%>" disabled>
</div>