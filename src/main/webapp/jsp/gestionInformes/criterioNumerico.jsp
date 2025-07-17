<%--
  Created by IntelliJ IDEA.
  User: susana.rodriguez
  Date: 10-may-2007
  Time: 13:13:11
  To change this template use File | Settings | File Templates.
--%>
<%  String campo = request.getParameter("campo");
    String titulo = request.getParameter("titulo");
    String etiquetaEsNulo = request.getParameter("etiquetaEsNulo");
    String etiquetaEsNoNulo = request.getParameter("etiquetaEsNoNulo");
%>
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>

<div id="criterio<%=campo%>" class="tableContainerComboCriteriosParametro">
    <span class="etiq" id="tituloCriterio<%=campo%>" style="width:175px"><%=titulo%></span>
    <select name="valorOperCriterio<%=campo%>" id="valorOperCriterio<%=campo%>" class="inputTexto" onchange="comprobarOperNumerico(<%=campo%>);">
        <option value="0"> > </option>
        <option value="1"> >= </option>
        <option value="2"> &lt </option>
        <option value="3"> &lt= </option>
        <option value="4"> ENTRE </option>
        <option value="5"> = </option>
        <option value="6"> &lt> </option>
        <option value="8"> <%=etiquetaEsNulo%> </option>
        <option value="9"> <%=etiquetaEsNoNulo%> </option>
    </select>
    <input type="text" name="valor1Criterio<%=campo%>" id="valor1Criterio<%=campo%>" class="inputTextoObligatorio" style="width:80px" maxlength="50" onkeypress="javascript:return SoloDigitosNumericos(this);">
    <input type="text" name="valor2Criterio<%=campo%>" id="valor2Criterio<%=campo%>" class="inputTextoObligatorio" style="width:80px" maxlength="50" onkeypress="javascript:return SoloDigitosNumericos(this);">
    <input type="hidden" name="origenCriterio<%=campo%>" id="origenCriterio<%=campo%>" disabled>
    <input type="hidden" name="tablaCriterio<%=campo%>" id="tablaCriterio<%=campo%>" disabled>
</div>
