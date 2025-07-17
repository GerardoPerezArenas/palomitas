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
  //out.println("desactivaformulario: " + desactivaFormulario);
  if(m_log.isDebugEnabled()) m_log.debug("\n\tFormulario Desactivado = " + desactivaFormulario);
  String valor = beanVO.getString(CAMPO_BEAN.getCodCampo());
  String activo = CAMPO_BEAN.getActivo();

  //out.println("valor: " + valor + ", activo: " + activo);
  String name = "";
  String estilo = "";
  name = "T" + CAMPO_BEAN.getCodCampo();
  //out.println("name: " + name + "<br>");
  //out.println("soloLectura: " + CAMPO_BEAN.getSoloLectura());
  
  
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
  
  
    if (mostrar) {
%>
<table id="<%=name%>" name="<%=name%>" width="100%" border="0px" cellspacing="2px" cellpadding="1px">
<% if ("true".equalsIgnoreCase(CAMPO_BEAN.getSoloLectura())) {
    String tramite = (String)session.getAttribute("tramiteCodigo"); // Para pintar el titulo del trámite o no
    if(m_log.isDebugEnabled()) m_log.debug("TRAMITE = " + tramite);
    if (tramite == null) {
        session.setAttribute("tramiteCodigo", CAMPO_BEAN.getCodTramite()); %>
        <tr>
            <td colspan="3" class="deTramite" align="left">
                Trámite <%=CAMPO_BEAN.getDescripcionTramite().toUpperCase()%>
            </td>
        </tr>
        <tr>
            <td class="<%= estilo %>" style="width: 3%" align="left"></td>
<%  } else if (!tramite.equalsIgnoreCase(CAMPO_BEAN.getCodTramite())) { %>
      <tr>
        <td colspan="3" class="deTramite" align="left">
            Trámite <%=CAMPO_BEAN.getDescripcionTramite().toUpperCase()%>
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


  <td class="etiqueta" width="15%" align="left">
    <%=CAMPO_BEAN.getDescCampo().toLowerCase()%>
  </td>
  <td class="columnP" align="left">
    <jsp:include page="<%=CAMPO_BEAN.getURLPlantilla()%>" flush="true" />
  </td>
</tr>
</table>
<% } %>