<%@ page import="es.altia.agora.interfaces.user.web.integracionsw.MantenimientoOperacionSWForm" %>
<%@ page import="java.util.Vector" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="es.altia.agora.business.integracionsw.DefinicionParametroEntradaVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    response.setHeader("Cache-control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    MantenimientoOperacionSWForm mantOpSwForm =
            (MantenimientoOperacionSWForm) session.getAttribute("MantenimientoOperacionSWForm");
    Vector listaParamsIn = mantOpSwForm.getListaParamsIn();

    JSONArray listaDescParamIn = new JSONArray();
    JSONArray listaTituloParamIn = new JSONArray();
    JSONArray listaOblParamIn = new JSONArray();
    JSONArray listaTipoParamIn = new JSONArray();
    JSONArray listaValorParamIn = new JSONArray();
    JSONArray listaCodParamIn = new JSONArray();

    for (int i = 0; i < listaParamsIn.size(); i++) {
        DefinicionParametroEntradaVO defParam = (DefinicionParametroEntradaVO) listaParamsIn.get(i);
        listaDescParamIn.put(defParam.getDescParam());
        listaTituloParamIn.put(defParam.getTituloParam());
        listaOblParamIn.put(defParam.isEsObligatorio());
        listaTipoParamIn.put(defParam.getTipoParametro());
        listaValorParamIn.put(defParam.getValorDefecto());
        listaCodParamIn.put(defParam.getCodigoParam());
    }

    mantOpSwForm.setListaDescParamIn(listaDescParamIn);
    mantOpSwForm.setListaTituloParamIn(listaTituloParamIn);
    mantOpSwForm.setListaOblParamIn(listaOblParamIn);
    mantOpSwForm.setListaTipoParamIn(listaTipoParamIn);
    mantOpSwForm.setListaValorParamIn(listaValorParamIn);
    mantOpSwForm.setListaCodParamIn(listaCodParamIn);

    out.println(mantOpSwForm.toJSONStringParamIn());

%>