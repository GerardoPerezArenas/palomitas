<%@ page import="java.util.Vector" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONStringer" %>
<%@ page import="es.altia.agora.interfaces.user.web.gestionInformes.GestionInformesForm" %>
<%@ page import="es.altia.agora.interfaces.user.web.gestionInformes.MantenimientoInformesForm" %>
<%@ page import="es.altia.agora.business.gestionInformes.InformeListaValueObject" %>
<%@ page import="es.altia.agora.business.gestionInformes.AmbitoListaValueObject" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    response.setHeader("Cache-control","no-cache");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader ("Expires", 0);
    Log m_Log = LogFactory.getLog(MantenimientoInformesForm.class.getName());
    MantenimientoInformesForm informesForm = (MantenimientoInformesForm) session.getAttribute("MantenimientoInformesForm");	
    Vector listaCampos = informesForm.getListaCampos();
   
    JSONArray jsonArrayCodigo = new JSONArray();
    JSONArray jsonArrayNome = new JSONArray();
    JSONArray jsonArrayCampo = new JSONArray();
    JSONArray jsonArrayTipo = new JSONArray();
     JSONArray jsonArrayLonxitude= new JSONArray();
    JSONArray jsonArrayOri = new JSONArray();
     JSONArray jsonArrayDescOri = new JSONArray();
    JSONArray jsonArrayNomeas = new JSONArray();
    JSONArray jsonArrayCriterio = new JSONArray();
     JSONArray jsonArrayCri = new JSONArray();
    JSONArray jsonArrayEli= new JSONArray();


       for (int i = 0; i < listaCampos.size(); i++){
		 AmbitoListaValueObject elvo1 = (AmbitoListaValueObject)listaCampos.get(i);
		
		jsonArrayCodigo.put(elvo1.getCodigo());
		jsonArrayNome.put(elvo1.getNome());
    		jsonArrayCampo.put(elvo1.getCampo());
    		jsonArrayTipo.put(elvo1.getTipo());
    		jsonArrayLonxitude.put(elvo1.getLonxitude());
    		jsonArrayOri.put(elvo1.getOrigen());
    		jsonArrayDescOri.put(elvo1.getDescOrigen());
    		jsonArrayNomeas.put(elvo1.getNomeas());
    		jsonArrayCriterio.put(elvo1.getCriterio());
    		jsonArrayCri.put(elvo1.getCri());
    		
		 }
		 m_Log.debug("******************    en el jsP    -> "+informesForm.getEliminado());
    		jsonArrayEli.put(informesForm.getEliminado());
    		 m_Log.debug("******************    en el eli    -> "+informesForm.getEli());
		 
	informesForm.setCodigo(jsonArrayCodigo);	
	informesForm.setNome(jsonArrayNome);
	informesForm.setCampo(jsonArrayCampo);
	informesForm.setTipo(jsonArrayTipo);
	informesForm.setLonxitude(jsonArrayLonxitude);
	informesForm.setOri(jsonArrayOri);
	informesForm.setDescOri(jsonArrayDescOri);
	informesForm.setNomeas(jsonArrayNomeas);
	informesForm.setCriterio(jsonArrayCriterio);
	informesForm.setCri(jsonArrayCri);
	informesForm.setEli(jsonArrayEli);
	
	
	  m_Log.debug("******************    en el eli    -> "+informesForm.getEli());

	m_Log.debug("LISTA getDescripcion *******: " + informesForm.getCri());
	//m_Log.debug("LISTA getDescripcion *******: " + informesForm.getDescripcion()); 
     	out.println(informesForm.toJSONStringCampos());

%>
