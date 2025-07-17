<%@ page import="org.json.JSONObject" %>
<%@ page contentType="text/html;charset=ISO-8859-15" language="java" pageEncoding="ISO-8859-15"%>

<%
    response.setHeader("Cache-control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    String error = (String)request.getAttribute("error");
    
    if (error == null) {
        JSONObject jsonExisteDoc = new JSONObject();
        //System.out.println("ERROR: " + request.getAttribute("existeDocumento"));
        jsonExisteDoc.put("existeDocumento", request.getAttribute("existeDocumento"));
        out.println(jsonExisteDoc.toString());
    } else {
        JSONObject jsonError = new JSONObject();
        jsonError.put("error", error);
        out.println(jsonError.toString());
    }
%>