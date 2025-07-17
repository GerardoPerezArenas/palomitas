<%@ page import="java.util.Vector" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="es.altia.agora.interfaces.user.web.gestionInformes.GestionInformesForm" %>
<%@ page import="es.altia.agora.interfaces.user.web.gestionInformes.MantenimientoInformesForm" %>
<%@ page import="es.altia.agora.business.gestionInformes.InformeListaValueObject" %>
<%@ page import="es.altia.agora.business.util.ElementoListaValueObject" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    response.setHeader("Cache-control","no-cache");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader ("Expires", 0);
    Log m_Log = LogFactory.getLog(MantenimientoInformesForm.class.getName());
    MantenimientoInformesForm informesForm = (MantenimientoInformesForm) session.getAttribute("MantenimientoInformesForm");	
    Vector listaAmbitos = informesForm.getListaAmbitos();
    JSONArray jsonArrayCodigo = new JSONArray();
    JSONArray jsonArrayDescripcion = new JSONArray();
    JSONArray jsonArrayTab = new JSONArray();
    JSONArray jsonArrayModo = new JSONArray();


       for (int i = 0; i < listaAmbitos.size(); i++){
		 ElementoListaValueObject elvo1 = (ElementoListaValueObject)listaAmbitos.get(i);
		
		 jsonArrayCodigo.put(elvo1.getCodigo());
		 jsonArrayDescripcion.put(elvo1.getDescripcion());
		 jsonArrayTab.put(elvo1.getTab());
		 jsonArrayModo.put(elvo1.getModo());
		 
		  m_Log.debug("HOLA  JSP<  -"+elvo1.getTab());
		 }
		 
		 
	informesForm.setCodigo(jsonArrayCodigo);	
	informesForm.setDescripcion(jsonArrayDescripcion);
	informesForm.setTab(jsonArrayTab);
	informesForm.setModo(jsonArrayModo);
	
	m_Log.debug("LISTA getDescripcion *******: " + informesForm.getDescripcion()); 
     	out.println(informesForm.toJSONStringAmbito());

%>
