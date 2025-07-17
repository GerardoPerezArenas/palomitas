<%@ page import="java.util.Vector" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="es.altia.agora.interfaces.user.web.integracionsw.ListadoSWForm" %>
<%@ page import="es.altia.agora.business.integracionsw.InfoServicioWebVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    response.setHeader("Cache-control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    ListadoSWForm listadoSWForm =
            (ListadoSWForm) session.getAttribute("ListadoSWForm");
    Vector listaSW = listadoSWForm.getListaSW();

    JSONArray listaCodigosSW = new JSONArray();
    JSONArray listaTitulosSW = new JSONArray();
    JSONArray listaServiciosPub = new JSONArray();
    JSONArray error = new JSONArray();
    if (listadoSWForm.getError() != null) error.put(listadoSWForm.getError());

    for (int i = 0; i < listaSW.size(); i++) {
        InfoServicioWebVO infoSW = (InfoServicioWebVO) listaSW.get(i);
        listaCodigosSW.put(infoSW.getCodigoSW());
        listaTitulosSW.put(infoSW.getTituloSW());
        listaServiciosPub.put(infoSW.isEstaPublicado());
    }

    listadoSWForm.setJsonError(error);
    listadoSWForm.setListaSWCod(listaCodigosSW);
    listadoSWForm.setListaSWNombre(listaTitulosSW);
    listadoSWForm.setListaSWPub(listaServiciosPub);

    out.println(listadoSWForm.toJSONStringServicios());

%>