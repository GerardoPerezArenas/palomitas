<!-- VISTA DE CAMPOTEXTOLARGO  -->
<jsp:useBean id="CAMPO_BEAN" scope="request" class="es.altia.agora.technical.EstructuraCampo"/>
<jsp:useBean id="beanVO" scope="request" class="es.altia.agora.technical.CamposFormulario"/>
<%
  String valor = beanVO.getString(CAMPO_BEAN.getCodCampo());
  if(valor==null) valor="";
  String obligatorio = CAMPO_BEAN.getObligatorio();
  String texto = "";
  String ob = "";
  if("1".equals(obligatorio)) {
    texto = "textareaTextoObligatorio";
    ob = "obligatorio";
  } else {
    texto = "textareaTexto";
  }
  String read = "";
  String id="";
  String name = "";
  String onChange="";
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
  //name = "T" + CAMPO_BEAN.getCodTramite() + CAMPO_BEAN.getCodCampo();
  if(ob.equals("obligatorio")) {
    id = ob;
  } else {
    id = name;
  }
%>
  <textarea class="<%= texto %>" style="width:95%;height:130px !important;text-transform: none" name="<%= name %>"
    id="<%= id %>" title="<%= CAMPO_BEAN.getDescCampo() %>"  <%= read %> onchange="modificaVariableCambiosCamposSupl()"
    onkeyup="return xValidarCaracteres(this);"><%=valor.trim()%></textarea>
    <a href="javascript:void(0);" onclick="verTexto('<%= name %>','<%= CAMPO_BEAN.getDescCampo() %>');" 
            style="top: 105px;position: relative;">
        <span class="fa fa-expand" alt="Maximizar campo"></span>
    </a>
<!-- FIN VISTA DE CAMPOTEXTOLARGO   -->
<script type="text/javascript">
    function verTexto(name,codigo){
        var texto = document.getElementsByName(name)[0].value;
        mostrarVentana(texto,"Contenido de campo suplementario "+codigo);
    }
</script>
    