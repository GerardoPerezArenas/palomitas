<%--
  Created by IntelliJ IDEA.
  User: susana.rodriguez
  Date: 10-may-2007
  Time: 10:45:59
  To change this template use File | Settings | File Templates.
--%>
<%
    String campo = request.getParameter("campo");
    String titulo = request.getParameter("titulo");
%>
<div id="criterio<%=campo%>" class="tableContainerComboCriteriosParametro">
    <span class="etiq" id="tituloCriterio<%=campo%>" style="width:80px"><%=titulo%></span>
    <select name="valorOperCriterio<%=campo%>" id="valorOperCriterio<%=campo%>" class="inputTexto">
        <option value="0"> = </option>
        <option value="1"> &lt> </option>
    </select>
    <select name="valor1Criterio<%=campo%>" id="valor1Criterio<%=campo%>" class="inputTexto">
        <option value="0"> Abierto </option>
        <option value="1"> Cerrado </option>
    </select>
    <input type="hidden" name="valor2Criterio<%=campo%>" id="valor2Criterio<%=campo%>" disabled>
    <input type="hidden" name="origenCriterioCampo<%=campo%>" id="origenCriterioCampo<%=campo%>" disabled>
    <input type="hidden" name="tablaCriterioCampo<%=campo%>" id="tablaCriterioCampo<%=campo%>" disabled>
</div>
