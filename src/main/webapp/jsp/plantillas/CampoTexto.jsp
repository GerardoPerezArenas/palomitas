<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="java.text.MessageFormat"%>
<!-- VISTA DE CAMPOTEXTO   -->
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:useBean id="CAMPO_BEAN" scope="request" class="es.altia.agora.technical.EstructuraCampo"/>
<jsp:useBean id="beanVO" scope="request" class="es.altia.agora.technical.CamposFormulario"/>
<%
  String valor = beanVO.getString(CAMPO_BEAN.getCodCampo());
  if(valor==null) valor="";
  String obligatorio = CAMPO_BEAN.getObligatorio();
  String texto = "";
  String ob = "";
  if("1".equals(obligatorio)) {
    texto = "inputTextoObligatorio";
    ob = "obligatorio";
  } else {
    texto = "inputTexto";
  }
  String read = "";
  String id="";
  String name ="";
    if("true".equals(CAMPO_BEAN.getSoloLectura())) {
      read = "readonly";
    }
  
  if("SI".equals(CAMPO_BEAN.getBloqueado())) {
      read = "readonly";
    }
    name = CAMPO_BEAN.getCodCampo();
    if (CAMPO_BEAN.getCodTramite() != null) {
        if (CAMPO_BEAN.getOcurrencia() != null)
            name = "T_" + CAMPO_BEAN.getCodTramite() + "_" + CAMPO_BEAN.getOcurrencia() + "_" + CAMPO_BEAN.getCodCampo();
        else
            name = "T_" + CAMPO_BEAN.getCodTramite() + "_" + CAMPO_BEAN.getCodCampo();
    }
  if(ob.equals("obligatorio")) {
    id = ob;
  } else {
    id = name;
  }
    Config m_Conf = ConfigServiceHelper.getConfig("common");
    String campo="TamMaximo.CampoTexto";
    Integer tamanoMaximo = new Integer(m_Conf.getString(campo));
    int tamanoMaxTexto = tamanoMaximo.intValue();
    String mensaje = MessageFormat.format(descriptor.getDescripcion("msjNumMaximoCampoTex"), new Object[] { tamanoMaximo });
    
        int tamanoVista;	
    if(Integer.parseInt(CAMPO_BEAN.getTamano())<50){	
        tamanoVista=39;	
    }else{	
        tamanoVista=90;    	
    }    

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

function cambiosCampoSupl(){
if (consultando!=true) modificaVariableCambiosCamposSupl();
}
</SCRIPT>

  <input type="text" name="<%= name %>" value="<%=valor%>"
    maxlength="<%= CAMPO_BEAN.getTamano() %>" id="<%= id %>" class="<%= texto %>" onchange="cambiosCampoSupl()"
    title="<%= CAMPO_BEAN.getDescCampo() %>"  <%= read %>
    style="width:<%=tamanoVista%>%;text-transform: none" onkeyup="return xValidarCaracteres(this)" onblur="javascript:if (!validarLongitudTexto(this)) {document.getElementById('<%=name%>').value = '';focus(this);}">
<!-- FIN VISTA DE CAMPOTEXTO   -->
