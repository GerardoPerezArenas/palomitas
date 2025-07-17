<%-- ////////////////////// --%>
<%--  VISTA DE CAMPOVISTA CONSULTA  --%>
<%-- /////////////////////  --%>
<jsp:useBean id="CAMPO_BEAN" scope="request" class="es.altia.agora.technical.EstructuraCampo"/>
<%
  String estilo = "etiqueta";
  String plantilla = CAMPO_BEAN.getURLPlantilla();
   plantilla = plantilla + "?consulta=true&mostrarAlarma=NO";
%>
<% if (CAMPO_BEAN.getDescripcionTramite() != null) {
    String tramite = (String)session.getAttribute("tramiteCodigo"); // Para pintar el titulo del trámite o no
    if (tramite == null) {
        session.setAttribute("tramiteCodigo", CAMPO_BEAN.getCodTramite()); %>
        <tr>
            <td colspan="2" class="deTramite" align="left">
                Campos del Trámite <%=CAMPO_BEAN.getDescripcionTramite().toUpperCase()%>
            </td>
        </tr>
<%  } else if (!tramite.equalsIgnoreCase(CAMPO_BEAN.getCodTramite())) { %>
      <tr>
        <td colspan="2" class="deTramite" align="left">
            Campos del Trámite <%=CAMPO_BEAN.getDescripcionTramite().toUpperCase()%>
        </td>
      </tr>
<%    session.setAttribute("tramiteCodigo", CAMPO_BEAN.getCodTramite());
    }
   } %>
<tr>
  <td class="<%= estilo %>" width="15%" align="left">
    <%=CAMPO_BEAN.getRotulo().toLowerCase()%> :
  </td>
  <td class="columnP" width="80%" align="left">
    <jsp:include page="<%=plantilla%>" flush="true" />
  </td>
</tr>

