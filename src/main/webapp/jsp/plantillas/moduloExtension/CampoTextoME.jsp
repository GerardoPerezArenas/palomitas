<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="java.text.MessageFormat"%>
<!-- VISTA DE CAMPOTEXTO   -->
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:useBean id="CAMPO_BEAN" scope="request"  class="es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.EstructuraCampoModuloIntegracionVO"/>

<%
    String valorCodigo = CAMPO_BEAN.getCodCampo();
    String valor="";

    String texto = "inputTexto";

    String read = "";
    String id=valorCodigo;
    String name =valorCodigo;

    name = CAMPO_BEAN.getCodCampo();
  
    Config m_Conf = ConfigServiceHelper.getConfig("common");
    String campo="TamMaximo.CampoTexto";
    Integer tamanoMaximo = new Integer(m_Conf.getString(campo));
    int tamanoMaxTexto = tamanoMaximo.intValue();
    String mensaje = MessageFormat.format(descriptor.getDescripcion("msjNumMaximoCampoTex"), new Object[] { tamanoMaximo });

%>
<script>
    var consultando = false;
</script>
<SCRIPT type="text/javascript">
function validarLongitudTexto(campo) {
    var valor = campo.value;
    if (valor != '') {
      if (!(valor.length<=<%=tamanoMaxTexto%>)) {
        jsp_alerta("A",'<%=mensaje%>');
        return false;
      } else return true;
    } else
        return true;
}


</SCRIPT>

  <input type="text" name="<%= name %>" value="<%=valor%>"
    maxlength="<%= CAMPO_BEAN.getTamanho() %>" id="<%= id %>" class="<%= texto %>" 
    title="<%= CAMPO_BEAN.getRotulo() %>"  <%= read %>
    style="width:100%;text-transform: none" onkeyup="return xValidarCaracteres(this)" onblur="javascript:if (!validarLongitudTexto(this)) {document.getElementById('<%=name%>').value = '';focus(this);}">
<!-- FIN VISTA DE CAMPOTEXTO   -->
