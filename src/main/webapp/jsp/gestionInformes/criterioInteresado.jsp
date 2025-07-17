<%-- 
    Document   : criterioInteresado
    Created on : 05-oct-2009, 10:18:57
    Author     : ivan
--%>

<%  String campo = request.getParameter("campo");
    String titulo = request.getParameter("titulo");
    String etiquetaEsNulo = request.getParameter("etiquetaEsNulo");
    String etiquetaEsNoNulo = request.getParameter("etiquetaEsNoNulo");
%>
<div id="criterio<%=campo%>" class="tableContainerComboCriteriosParametro">
    <span class="etiq" id="tituloCriterio<%=campo%>" style="width:100px"><%=titulo%></span>
    <select name="valorOperCriterio<%=campo%>" id="valorOperCriterio<%=campo%>" class="inputTexto" onchange="comprobarOperInteresado(<%=campo%>);">
        <option value="5"> = </option>
        <option value="6"> &lt> </option>
        <option value="8"> <%=etiquetaEsNulo%> </option>
        <option value="9"> <%=etiquetaEsNoNulo%> </option>
    </select>

    <input type="text" name="valor1Criterio<%=campo%>" id="valor1Criterio<%=campo%>" class="inputTextoDeshabilitado" style="width:323px" maxlength="100" readonly ="true">
    <input type= "button" class="botonCriterio" value="Buscar"
       name="botonBuscar" onClick="pulsarBuscarInteresado(<%=campo%>);" accesskey="B">
    <input type="hidden" name="valor2Criterio<%=campo%>" id="valor2Criterio<%=campo%>" disabled>
    <input type="hidden" name="origenCriterio<%=campo%>" id="origenCriterio<%=campo%>" disabled>
    <input type="hidden" name="tablaCriterio<%=campo%>" id="tablaCriterio<%=campo%>" disabled>
</div>