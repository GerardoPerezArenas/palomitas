<%@ page import="es.altia.agora.interfaces.user.web.gestionInformes.FichaInformeForm"%>
<%@ page import="es.altia.agora.interfaces.user.web.gestionInformes.SolicitudesInformesForm"%>
<%@ page import="java.util.Vector"%>
<%@ page import="es.altia.agora.business.gestionInformes.CamposDesplegablesValueObject"%>
<%@ page import="es.altia.agora.business.gestionInformes.ValoresCamposDesplegablesValueObject"%>
<%--
  Created by IntelliJ IDEA.
  User: susana.rodriguez
  Date: 15-may-2007
  Time: 11:19:02
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>

<%  String campo = request.getParameter("campo");
    String titulo = request.getParameter("titulo");
    String formulario = request.getParameter("formulario");
    String etiquetaEsNulo = request.getParameter("etiquetaEsNulo");
    String etiquetaEsNoNulo = request.getParameter("etiquetaEsNoNulo");
    String codigoCampoDesplegable = request.getParameter("codigoCampoDesplegable");
    Vector listaCamposDesplegables;
    Vector listaValores = new Vector();
    CamposDesplegablesValueObject camposDesplegablesVO;
    ValoresCamposDesplegablesValueObject valoresCamposDesplegablesVO;


    if (formulario.equals("FichaInformeForm")) {
        FichaInformeForm fichaInformeForm = (FichaInformeForm) session.getAttribute("FichaInformeForm");
        listaCamposDesplegables = fichaInformeForm.getListaCamposDesplegables();
    } else {
        SolicitudesInformesForm solicitudesInformesForm = (SolicitudesInformesForm) session.getAttribute("SolicitudesInformesForm");
        listaCamposDesplegables = solicitudesInformesForm.getListaCamposDesplegables();
    }
    int i=0;
    boolean encontrado = false;
    while (!encontrado && i<listaCamposDesplegables.size()) {
        camposDesplegablesVO = (CamposDesplegablesValueObject) listaCamposDesplegables.get(i);
        if (camposDesplegablesVO.getCodigoCampo().equals(codigoCampoDesplegable)) {
            listaValores = camposDesplegablesVO.getListaValores();
            encontrado = true;
        }
        i++;
    }
%>

<div id="criterio<%=campo%>" class="tableContainerComboCriteriosParametro">
     <span class="etiq" id="tituloCriterio<%=campo%>" style="width:80px"><%=titulo%></span>
     <select name="valorOperCriterio<%=campo%>" id="valorOperCriterio<%=campo%>" class="inputTexto" onchange="comprobarOperTexto(<%=campo%>);">
         <option value="5"> = </option>
         <option value="6"> &lt> </option>
         <option value="8">  <%=etiquetaEsNulo%> </option>
         <option value="9"> <%=etiquetaEsNoNulo%> </option>
     </select>
     <select name="valor1Criterio<%=campo%>" id="valor1Criterio<%=campo%>" class="inputTexto" style="width:275px">
         <% for (int j=0;j<listaValores.size();j++) {
            valoresCamposDesplegablesVO = (ValoresCamposDesplegablesValueObject) listaValores.get(j); %>
            <option value="<%=valoresCamposDesplegablesVO.getCodigoValor()%>"><%=valoresCamposDesplegablesVO.getDescripcionValor()%></option>
        <% } %>
     </select>
     <input type="hidden" name="valor2Criterio<%=campo%>" id="valor2Criterio<%=campo%>" disabled>
     <input type="hidden" name="origenCriterioCampo<%=campo%>" id="origenCriterioCampo<%=campo%>" disabled>
     <input type="hidden" name="tablaCriterioCampo<%=campo%>" id="tablaCriterioCampo<%=campo%>" disabled>
 </div>
