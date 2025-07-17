<%@ page import="java.util.Vector" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="es.altia.agora.interfaces.user.web.integracionsw.IntegracionSWForm" %>
<!--	AQUI EL IMPORT PARA EL VO DE SW
 <%@ page import="es.altia.agora.business.gestionInformes.InformeListaValueObject" %>
-->
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

    Log m_Log = LogFactory.getLog(IntegracionSWForm.class.getName());
    IntegracionSWForm swForm = (IntegracionSWForm) session.getAttribute("IntegracionSWForm");
    Vector listaSW = swForm.getListaSW();
    JSONArray jsonArrayCod = new JSONArray();
    JSONArray jsonArrayNombre = new JSONArray();
    JSONArray jsonArrayPub = new JSONArray();
    for (int i = 0; i < listaSW.size(); i++) {
		//	AQUI EL VO DE SW
    	//        InformeListaValueObject informe = (InformeListaValueObject) listaInformes.get(i);
        jsonArrayCod.put(sw.getCodigo());
        jsonArrayNombre.put(sw.getTitulo());
        jsonArrayPub.put(sw.getPublicado());
    }
    swForm.setListaInformesCod(jsonArrayCod);
    swForm.setListaInformesNombre(jsonArrayNombre);
    swForm.setListaInformesPub(jsonArrayPub);
    m_Log.debug("LISTA SERVICIOS WEB COD *******: " + swForm.getListaSWCod());
    m_Log.debug("LISTA SERVICIOS WEB NOM *******: " + swForm.getListaSWNombre());
    m_Log.debug("LISTA SERVICIOS WEB PUB *******: " + swForm.getListaSWPub());
    out.println(swForm.toJSONString());
%>
