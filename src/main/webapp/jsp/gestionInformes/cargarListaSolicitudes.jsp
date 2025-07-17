<%@ page import="java.util.Vector" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="es.altia.agora.interfaces.user.web.gestionInformes.SolicitudesInformesForm" %>
<%@ page import="es.altia.agora.business.gestionInformes.SolicitudesListaValueObject" %>
<%--
  Created by IntelliJ IDEA.
  User: daniel.sambad
  Date: 07-feb-2007
  Time: 16:40:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%  response.setHeader("Cache-control","no-cache");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader ("Expires", 0); 

    Log m_Log = LogFactory.getLog(SolicitudesInformesForm.class.getName());
    SolicitudesInformesForm solicitudesForm = (SolicitudesInformesForm) session.getAttribute("SolicitudesInformesForm");
    Vector listaSolicitudes = solicitudesForm.getListaSolicitudes();
    JSONArray jsonArrayCod = new JSONArray();
    JSONArray jsonArrayNombre = new JSONArray();
    JSONArray jsonArrayAmbito = new JSONArray();
    JSONArray jsonArrayProc = new JSONArray();
    JSONArray jsonArrayEstado = new JSONArray();
    JSONArray jsonArrayFecha = new JSONArray();
    for (int i = 0; i < listaSolicitudes.size(); i++) {
        SolicitudesListaValueObject solicitud = (SolicitudesListaValueObject) listaSolicitudes.get(i);
        jsonArrayCod.put(solicitud.getCodigo());
        jsonArrayProc.put(solicitud.getCodProcedimiento());
        jsonArrayNombre.put(solicitud.getTitulo());
        jsonArrayAmbito.put(solicitud.getOrigen());
        jsonArrayEstado.put(solicitud.getEstado());
        jsonArrayFecha.put(solicitud.getFecha());
    }
    solicitudesForm.setListaInformesCod(jsonArrayCod);
    solicitudesForm.setListaInformesNombre(jsonArrayNombre);
    solicitudesForm.setListaInformesAmbito(jsonArrayAmbito);
    solicitudesForm.setListaInformesProc(jsonArrayProc);
    solicitudesForm.setListaInformesEstado(jsonArrayEstado);
    solicitudesForm.setListaInformesFecha(jsonArrayFecha);
    m_Log.debug("LISTA INFORMES COD *******: " + solicitudesForm.getListaInformesCod());
    m_Log.debug("LISTA INFORMES NOM *******: " + solicitudesForm.getListaInformesNombre());
    m_Log.debug("LISTA INFORMES NOM *******: " + solicitudesForm.getListaInformesAmbito());
    m_Log.debug("LISTA INFORMES PRO *******: " + solicitudesForm.getListaInformesProc());
    m_Log.debug("LISTA INFORMES EST *******: " + solicitudesForm.getListaInformesEstado());
    m_Log.debug("LISTA INFORMES FEC *******: " + solicitudesForm.getListaInformesFecha());
    out.println(solicitudesForm.toJSONStringSolicitudes());
%>
