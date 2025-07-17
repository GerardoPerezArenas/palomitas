<%@ page import="java.util.Vector" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="es.altia.agora.interfaces.user.web.integracionsw.ListadoOperacionesSWForm" %>
<%@ page import="es.altia.agora.business.integracionsw.DefinicionOperacionVO" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    response.setHeader("Cache-control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);


    ListadoOperacionesSWForm listaOperacionesForm =
            (ListadoOperacionesSWForm) session.getAttribute("ListadoOperacionesSWForm");

    String error = listaOperacionesForm.getError();
    JSONArray errorArray = new JSONArray();
    if (error != null) errorArray.put(error);

    Vector listaOperaciones = listaOperacionesForm.getListaOperaciones();
    JSONArray jsonArrayCod = new JSONArray();
    JSONArray jsonArrayNombre = new JSONArray();
    JSONArray jsonArrayPub = new JSONArray();
    for (int i = 0; i < listaOperaciones.size(); i++) {
        DefinicionOperacionVO operacion = (DefinicionOperacionVO) listaOperaciones.get(i);
        jsonArrayCod.put(operacion.getCodigoDefinicionOp());
        jsonArrayNombre.put(operacion.getNombreDefinicionOp());
        if (operacion.isPublicada()) jsonArrayPub.put("SI");
        else jsonArrayPub.put("NO");
    }
    listaOperacionesForm.setListaOperacionesCod(jsonArrayCod);
    listaOperacionesForm.setListaOperacionesNombre(jsonArrayNombre);
    listaOperacionesForm.setListaOperacionesPub(jsonArrayPub);
    listaOperacionesForm.setMsjError(errorArray);

    out.println(listaOperacionesForm.toJSONStringParamIn());
%>
