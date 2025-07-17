<%@ page import="java.util.Vector" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="es.altia.agora.interfaces.user.web.gestionInformes.GestionInformesForm" %>
<%@ page import="es.altia.agora.business.gestionInformes.InformeListaValueObject" %>
<%--
  Created by IntelliJ IDEA.
  User: daniel.sambad
  Date: 07-feb-2007
  Time: 16:40:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    response.setHeader("Cache-control","no-cache");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader ("Expires", 0);

    Log m_Log = LogFactory.getLog(GestionInformesForm.class.getName());
    GestionInformesForm informesForm = (GestionInformesForm) session.getAttribute("GestionInformesForm");
    Vector listaInformes = informesForm.getListaInformes();
    JSONArray jsonArrayCod = new JSONArray();
    JSONArray jsonArrayOrig = new JSONArray();
    JSONArray jsonArrayNombre = new JSONArray();
    JSONArray jsonArrayProc = new JSONArray();
    JSONArray jsonArrayPub = new JSONArray();
    for (int i = 0; i < listaInformes.size(); i++) {
        InformeListaValueObject informe = (InformeListaValueObject) listaInformes.get(i);
        jsonArrayCod.put(informe.getCodigo());
        jsonArrayOrig.put(informe.getOrigen());
        jsonArrayProc.put(informe.getCodProcedimiento());
        jsonArrayNombre.put(informe.getTitulo());
        jsonArrayPub.put(informe.getPublicado());
    }
    informesForm.setListaInformesCod(jsonArrayCod);
    informesForm.setListaInformesOrig(jsonArrayOrig);
    informesForm.setListaInformesNombre(jsonArrayNombre);
    informesForm.setListaInformesProc(jsonArrayProc);
    informesForm.setListaInformesPub(jsonArrayPub);
    m_Log.debug("LISTA INFORMES COD *******: " + informesForm.getListaInformesCod());
    m_Log.debug("LISTA INFORMES ORIG *******: " + informesForm.getListaInformesOrig());
    m_Log.debug("LISTA INFORMES NOM *******: " + informesForm.getListaInformesNombre());
    m_Log.debug("LISTA INFORMES PRO *******: " + informesForm.getListaInformesProc());
    m_Log.debug("LISTA INFORMES PUB *******: " + informesForm.getListaInformesPub());
    m_Log.debug("CODIGO PLANTILLA ACTUAL *******: " + informesForm.getCodPlantilla());
    m_Log.debug("PUBLICAR *******: " + informesForm.getPublicar());
    out.println(informesForm.toJSONString());
%>
