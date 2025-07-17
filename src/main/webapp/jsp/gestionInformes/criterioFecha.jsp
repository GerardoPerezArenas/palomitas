<%--
  Created by IntelliJ IDEA.
  User: susana.rodriguez
  Date: 10-may-2007
  Time: 10:23:39
  To change this template use File | Settings | File Templates.
--%>
<%  String titulo = request.getParameter("titulo");
    String campo = request.getParameter("campo");
    String etiquetaEsNulo = request.getParameter("etiquetaEsNulo");
    String etiquetaEsNoNulo = request.getParameter("etiquetaEsNoNulo");
%>

<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/dojo/dojo.js"></script>
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>

<div id="criterio<%=campo%>" class="tableContainerComboCriteriosParametro">
    <span class="etiq" id="tituloCriterio<%=campo%>" style="width:275px"><%=titulo%></span>
    <select name="valorOperCriterio<%=campo%>" id="valorOperCriterio<%=campo%>" class="inputTexto" onchange="comprobarOperFecha(<%=campo%>);">
        <option value="0"> > </option>
        <option value="1"> >= </option>
        <option value="2"> &lt </option>
        <option value="3"> &lt= </option>
        <option value="4"> ENTRE </option>       
        <option value="8"> <%=etiquetaEsNulo%> </option>
        <option value="9"> <%=etiquetaEsNoNulo%> </option>
    </select>
    <input name="valor1Criterio<%=campo%>" id="valor1Criterio<%=campo%>" dojoType="dropdowndatepicker" displayFormat="dd/MM/yyyy" saveFormat="dd/MM/yyyy" class="dojoCalendar">
    <input name="valor2Criterio<%=campo%>" id="valor2Criterio<%=campo%>" dojoType="dropdowndatepicker" displayFormat="dd/MM/yyyy" saveFormat="dd/MM/yyyy" class="dojoCalendar">
    <input type="hidden" name="origenCriterio<%=campo%>" id="origenCriterio<%=campo%>" disabled>
    <input type="hidden" name="tablaCriterio<%=campo%>" id="tablaCriterio<%=campo%>" disabled>
</div>