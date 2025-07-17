<%@ page import="es.altia.agora.interfaces.user.web.gestionInformes.SolicitudesInformesForm"%>
<%--
  Created by IntelliJ IDEA.
  User: susana.rodriguez
  Date: 10-may-2007
  Time: 10:47:10
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>

<%  String campo = request.getParameter("campo");
    String titulo = request.getParameter("titulo");
    SolicitudesInformesForm solicitudesInformesForm = (SolicitudesInformesForm) session.getAttribute("SolicitudesInformesForm");
%>

<div id="criterio<%=campo%>" class="tableContainerComboCriteriosParametro">
     <span class="etiq" id="tituloCriterio<%=campo%>" style="width:80px"><%=titulo%></span>
     <select name="valorOperCriterio<%=campo%>" id="valorOperCriterio<%=campo%>" class="inputTexto">
         <option value="0"> = </option>
         <option value="1"> &lt> </option>
     </select>
     <select name="valor1Criterio<%=campo%>" id="valor1Criterio<%=campo%>" class="inputTexto" style="width:275px">
     <% if (solicitudesInformesForm!=null) { %>
        <logic:iterate id="elemento" name="SolicitudesInformesForm" property="listaProcedimientos">
            <option value="<bean:write name="elemento" property="codigo" />"><bean:write name="elemento" property="descripcion" />  </option>
        </logic:iterate>
     <% } else { %>
         <logic:iterate id="elemento" name="FichaInformeForm" property="listaProcedimientos">
             <option value="<bean:write name="elemento" property="codigo" />"><bean:write name="elemento" property="descripcion" />  </option>
         </logic:iterate>
     <% } %>
     </select>
    <input type="hidden" name="valor2Criterio<%=campo%>" id="valor2Criterio<%=campo%>" disabled>
    <input type="hidden" name="origenCriterioCampo<%=campo%>" id="origenCriterioCampo<%=campo%>" disabled>
    <input type="hidden" name="tablaCriterioCampo<%=campo%>" id="tablaCriterioCampo<%=campo%>" disabled>
 </div>
