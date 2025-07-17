<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%
    String tituloVentana = request.getParameter("titulo");
    String esObligatorio = request.getParameter("obligatorio");

%>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/sge_basica_popUp.css'/>" media="screen">

<script type="text/javascript">

    function pulsarSiguiente() {
        self.parent.opener.retornoXanelaAuxiliar(document.forms[1].valor.value);
    }
</script>
<body class="nuevo">
    <form name="form">
    <div class="cajonTexto">
        <br/>
        <span class="etiqSW"><%=tituloVentana%></span>
        <% if (Boolean.parseBoolean(esObligatorio)) { %>
        <input type="text" class="inputTextoObligatorio" id="valor" name="valor" style="width:60%;">
        <% } else { %>
        <input type="text" class="inputTexto" id="valor" name="valor" style="width:60%;">
        <% } %>
        <input type= "button" class="botonAplicacion" value="Siguiente" name="botonSiguiente"
               onClick="pulsarSiguiente();" accesskey="S">
    </div>
    </form>

</body>