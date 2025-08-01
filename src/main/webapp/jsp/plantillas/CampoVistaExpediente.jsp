<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<!-- ////////////////////// -->
<!--  VISTA DE CAMPOVISTA   -->
<!-- /////////////////////  -->
<jsp:useBean id="CAMPO_BEAN" scope="request" class="es.altia.agora.technical.EstructuraCampo"/>
<jsp:useBean id="beanVO" scope="request" class="es.altia.agora.technical.CamposFormulario"/>
<%Log m_log = LogFactory.getLog(this.getClass().getName());
  boolean mostrar = true;
  String desactivaFormulario = (String)request.getParameter("desactivaFormulario");
  
  String valor = beanVO.getString(CAMPO_BEAN.getCodCampo());
  String activo = CAMPO_BEAN.getActivo();
  String name = "";
  String estilo = "";
  name = "T" + CAMPO_BEAN.getCodTramite() + CAMPO_BEAN.getOcurrencia() + CAMPO_BEAN.getCodCampo();
  //name = CAMPO_BEAN.getCodCampo();
  if("true".equals(CAMPO_BEAN.getSoloLectura())) {  // Estamos en campo que es de tramite pero visible en expediente
  //  name = "T" + CAMPO_BEAN.getCodTramite() + CAMPO_BEAN.getCodCampo();
    estilo = "deTramite";
      if (activo !=null) {
          if (activo.equalsIgnoreCase("NO")) {
              if ((valor == null) || (valor.equalsIgnoreCase(""))) {
                  mostrar = mostrar & false;
              }
          /*} else { // Activo es SI, pero el valor es nulo o vacio, no se pinta
              if ((valor == null) || (valor.equalsIgnoreCase(""))) {
                  mostrar = mostrar & false;
              }
          */}
      }
  } else { // Campo normal de expediente
    //name = CAMPO_BEAN.getCodCampo();
    estilo = "etiqueta";
    if (activo == null) {
        mostrar = mostrar & false;
    } else {
        if (desactivaFormulario.equalsIgnoreCase("false")) { // Desactiva es FALSE, Formulario  MODIFICABLE
            if (activo.equalsIgnoreCase("SI")) {
                mostrar = mostrar & true;
            } else {
                mostrar = mostrar & false;
            }
        } else { // Desactiva es TRUE, Formulario NO MODIFICABLE
            // Se debe pintar siempre
            if (!activo.equalsIgnoreCase("SI")) { // El campo esta activo, pero puede que no tenga valor
                if ((valor != null) && (!valor.equalsIgnoreCase(""))) {
                    mostrar = mostrar & true;
                } else {
                    mostrar = mostrar & false;
                }
            }
        }
    }
  }
    
    if (mostrar) {
%>
<table id="<%=name%>" name="<%=name%>" width="100%" border="0px" cellspacing="2px" cellpadding="1px">
<% if ("true".equalsIgnoreCase(CAMPO_BEAN.getSoloLectura())) {
    String tramite = (String)session.getAttribute("tramiteCodigo"); // Para pintar el titulo del tr�mite o no
    
    if (tramite == null) {
        session.setAttribute("tramiteCodigo", CAMPO_BEAN.getCodTramite()); %>
        <tr>
            <td colspan="3" class="deTramite" align="left">
                Tr�mite <%=CAMPO_BEAN.getDescripcionTramite().toUpperCase()%>
            </td>
        </tr>
        <tr>
            <td class="<%= estilo %>" style="width: 3%" align="left"></td>
<%  } else if (!tramite.equalsIgnoreCase(CAMPO_BEAN.getCodTramite())) { %>
      <tr>
        <td colspan="3" class="deTramite" align="left">
            Tr�mite <%=CAMPO_BEAN.getDescripcionTramite().toUpperCase()%>
        </td>
      </tr>
      <tr>
        <td class="<%= estilo %>" style="width: 3%" align="left"></td>
<%    session.setAttribute("tramiteCodigo", CAMPO_BEAN.getCodTramite());
    } else { %>
        <tr>
            <td class="<%= estilo %>" style="width: 3%" align="left"></td>
<%  } %>
<% } %>
  <td class="etiqueta" width="150px" align="left">
            <%=CAMPO_BEAN.getDescCampo().toLowerCase()%>
        </td>
        <td class="columnP" align="left">            
            <jsp:include page="<%=CAMPO_BEAN.getURLPlantilla()%>" flush="true" />
        </td>
    </tr>
</table>
<% } %>
