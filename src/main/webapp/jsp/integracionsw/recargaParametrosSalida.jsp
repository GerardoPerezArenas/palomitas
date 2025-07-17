<%@ page import="es.altia.agora.interfaces.user.web.integracionsw.MantenimientoOperacionSWForm" %>
<%@ page import="java.util.Vector" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="es.altia.agora.business.integracionsw.DefinicionParametroSalidaVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    response.setHeader("Cache-control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    MantenimientoOperacionSWForm mantOpSwForm =
            (MantenimientoOperacionSWForm) session.getAttribute("MantenimientoOperacionSWForm");
    Vector listaParamsOut = mantOpSwForm.getListaParamsOut();

    JSONArray listaDescParamOut = new JSONArray();
    JSONArray listaTituloParamOut = new JSONArray();
    JSONArray listaTipoParamOut = new JSONArray();
    JSONArray listaValorParamOut = new JSONArray();

    for (int i = 0; i < listaParamsOut.size(); i++) {
        DefinicionParametroSalidaVO defParam = (DefinicionParametroSalidaVO) listaParamsOut.get(i);
        listaDescParamOut.put(defParam.getDescParam());
        listaTituloParamOut.put(defParam.getTituloParam());
        listaTipoParamOut.put(defParam.getTipoDato());
        listaValorParamOut.put(defParam.getValorCorrecto());
    }

    mantOpSwForm.setListaDescParamOut(listaDescParamOut);
    mantOpSwForm.setListaTituloParamOut(listaTituloParamOut);
    mantOpSwForm.setListaTipoParamOut(listaTipoParamOut);
    mantOpSwForm.setListaValorParamOut(listaValorParamOut);

    out.println(mantOpSwForm.toJSONStringParamOut());

%>