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
  if(m_log.isDebugEnabled()) m_log.debug("\n\tFormulario Desactivado = " + desactivaFormulario);
  String valor = beanVO.getString(CAMPO_BEAN.getCodCampo());
  String activo = CAMPO_BEAN.getActivo();
  String name = "";
  String estilo = "";
  name = "T" + CAMPO_BEAN.getCodTramite() +  CAMPO_BEAN.getOcurrencia() + CAMPO_BEAN.getCodCampo();
  if("true".equals(CAMPO_BEAN.getSoloLectura())) {
  //  name = "T" + CAMPO_BEAN.getCodTramite() + CAMPO_BEAN.getCodCampo();
    estilo = "deTramite";
    if (valor == null) {
        mostrar = mostrar & false;
    } else {
        if (valor.equalsIgnoreCase("")) {
            mostrar = mostrar & false;
        }
    }
  } else {
    //name = CAMPO_BEAN.getCodCampo();
    estilo = "etiqueta";
  }
    if(m_log.isDebugEnabled()) m_log.debug("CAMPO = " + CAMPO_BEAN.getCodCampo() + " VALOR = " + valor);
    if(m_log.isDebugEnabled()) m_log.debug("1.- MOSTRAR = " + mostrar);
    if (activo == null) {
        mostrar = mostrar & false;
    } else {
        if (desactivaFormulario.equalsIgnoreCase("false")) { // Desactiva es FALSE, Formulario  MODIFICABLE
            if (activo.equalsIgnoreCase("SI")) {
                mostrar = mostrar & true;
            } else {
                mostrar = mostrar & false;
            }
            if(m_log.isDebugEnabled()) m_log.debug("2.- MOSTRAR en Formulario modificable = " + mostrar);
        } else { // Desactiva es TRUE, Formulario NO MODIFICABLE
            // Se debe pintar siempre
            if (!activo.equalsIgnoreCase("SI")) { // El campo esta activo, pero puede que no tenga valor
                if ((valor != null) && (!valor.equalsIgnoreCase(""))) {
                    mostrar = mostrar & true;
                } else {
                    mostrar = mostrar & false;
                }
                if(m_log.isDebugEnabled()) m_log.debug("3.- MOSTRAR en Formulario NO modificable y Campo DESACTIVO = " + mostrar);
            }
        }
    }
    if(m_log.isDebugEnabled()) m_log.debug("1.- NAME = " + name);
    if (mostrar) {
%>
<table id="<%=name%>" name="<%=name%>" width="100%" border="0px" cellspacing="2px" cellpadding="1px">
<tr>
  <td class="<%= estilo %>" onclick="recargarCamposCalculados();" width="150px" align="left">
    <%=CAMPO_BEAN.getRotulo().toLowerCase()%> :
  </td>
  <td class="columnP" align="left">
    <jsp:include page="<%=CAMPO_BEAN.getURLPlantilla()%>" flush="true" />
  </td>
</tr>
</table>
<% } %>
