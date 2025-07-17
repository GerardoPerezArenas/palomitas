<%@ page import="java.util.Vector" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="es.altia.agora.interfaces.user.web.integracionsw.TestOperacionSWForm" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    response.setHeader("Cache-control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    TestOperacionSWForm testOpForm =
            (TestOperacionSWForm) session.getAttribute("TestOperacionSWForm");

    JSONArray jsonEstado = new JSONArray();
    jsonEstado.put(testOpForm.getEstadoEjecucion());
    testOpForm.setJsonEstado(jsonEstado);

    Vector resultados = testOpForm.getResultados();
    if (resultados != null) {

        JSONArray jsonResultados = new JSONArray();
        for (int i = 0; i < resultados.size(); i++) {
            jsonResultados.put(resultados.get(i));
        }
        testOpForm.setJsonResultados(jsonResultados);
    }

    out.println(testOpForm.toJSONStringResultados());
%>