<%@ page import="java.util.Vector" %>
<%@ page import="es.altia.agora.interfaces.user.web.gestionInformes.FichaInformeForm" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="es.altia.agora.business.util.GeneralValueObject" %>
<%--
  Created by IntelliJ IDEA.
  User: daniel.sambad
  Date: 07-feb-2007
  Time: 16:40:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% Log m_Log = LogFactory.getLog(FichaInformeForm.class.getName());
    FichaInformeForm fichaInformeForm = (FichaInformeForm) session.getAttribute("FichaInformeForm");
    Vector listaCampos = fichaInformeForm.getListaCampos();
    JSONArray jsonArrayCod = new JSONArray();
    JSONArray jsonArrayNom = new JSONArray();
    for (int i = 0; i < listaCampos.size(); i++) {
        GeneralValueObject campo = (GeneralValueObject) listaCampos.get(i);
        jsonArrayCod.put(campo.getAtributo("codigo"));
        jsonArrayNom.put(campo.getAtributo("nombre"));
    }
    fichaInformeForm.setListaCamposCod(jsonArrayCod);
    fichaInformeForm.setListaCamposNom(jsonArrayNom);
    m_Log.debug("LISTA CAMPOS COD *******: " + fichaInformeForm.getListaCamposCod());
    m_Log.debug("LISTA CAMPOS NOM *******: " + fichaInformeForm.getListaCamposNom());
    out.println(fichaInformeForm.toJSONString());
%>
