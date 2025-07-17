<%@ page import="es.altia.agora.interfaces.user.web.integracionsw.MantenimientoOperacionSWForm" %>
<%@ page import="java.util.Vector" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="es.altia.agora.business.integracionsw.DefinicionArraySWVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    response.setHeader("Cache-control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    MantenimientoOperacionSWForm mantOpSwForm =
            (MantenimientoOperacionSWForm) session.getAttribute("MantenimientoOperacionSWForm");
    Vector listaArrays = mantOpSwForm.getListaArrays();

    JSONArray listaDescArray = new JSONArray();
    JSONArray listaEntSalArray = new JSONArray();
    JSONArray listaNumReps = new JSONArray();

    for (int i = 0; i < listaArrays.size(); i++) {
        DefinicionArraySWVO defArray = (DefinicionArraySWVO) listaArrays.get(i);
        listaDescArray.put(defArray.getDescParamArray());
        listaEntSalArray.put(defArray.getEntradaSalida());
        listaNumReps.put(defArray.getNumRepeticiones());
    }

    mantOpSwForm.setListaArrayDef(listaDescArray);
    mantOpSwForm.setListaArrayEntSal(listaEntSalArray);
    mantOpSwForm.setListaArrayNumReps(listaNumReps);

    out.println(mantOpSwForm.toJSONStringArray());

%>