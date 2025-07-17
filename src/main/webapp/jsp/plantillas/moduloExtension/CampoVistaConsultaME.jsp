
<jsp:useBean id="CAMPO_BEAN" scope="request" class="es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.EstructuraCampoModuloIntegracionVO"/>
<%
  String estilo = "etiqueta";
  String plantilla = CAMPO_BEAN.getURLPlantilla();
  
%>

<tr>
  <td class="<%= estilo %>" width="15%" align="left">
    <%=CAMPO_BEAN.getRotulo().toLowerCase()%> :
  </td>
   <td class="columnP" width="80%" align="left">
    <jsp:include page="<%=plantilla%>" flush="true" />
  </td>
  
</tr>

