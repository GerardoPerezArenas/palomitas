<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<!-- VISTA DE CAMPO FECHA   -->
<jsp:useBean id="CAMPO_BEAN" scope="request" class="es.altia.agora.technical.EstructuraCampo"/>
<jsp:useBean id="beanVO" scope="request" class="es.altia.agora.technical.CamposFormulario"/>
<script>
    var consultando = false;
</script>
<%
  String valor = beanVO.getString(CAMPO_BEAN.getCodCampo());
  if(valor==null) valor="";
  String obligatorio = CAMPO_BEAN.getObligatorio();
  String texto = "";
  String ob = "";
  if("1".equals(obligatorio)) {
    texto = "inputTxtFechaObligatorio";
    ob = "obligatorio";
  } else {
    texto = "inputTxtFecha";
  }
  String funcion  ="";
  if(CAMPO_BEAN.getCodTramite()!=null && CAMPO_BEAN.getCodTramite().length()>=1){
        funcion = "mostrar" + CAMPO_BEAN.getCodCampo() + "_" + CAMPO_BEAN.getCodTramite();
  }
  else{
      funcion = "mostrar" + CAMPO_BEAN.getCodCampo();
  }
  
  String nameIMG = "cal" + CAMPO_BEAN.getCodCampo();
  String read = "";
  String id="";
  String name = "";
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
  String consulta = request.getParameter("consulta");
  if ((consulta != null) && (!"".equalsIgnoreCase(consulta))) {
    CAMPO_BEAN.setTamano("25"); // Para que tenga el tamaño justo para poder consultar
%> <script> consultando = true;</script>
<% } %>

<SCRIPT language="JavaScript">

function <%= funcion %> (evento){ 
  if (document.getElementById("<%= nameIMG %>").className.indexOf("fa-calendar") != -1 ) {
    showCalendar('forms[0]','<%= name %>',null,null,null,'','<%= nameIMG %>','',null,null,null,null,null,null,null,null,evento);
  }
}

function nuevo<%=funcion%>(event){ 
    var event = (event) ? event : ((window.event) ? window.event : "");    
    <%=funcion%>(event);    
}

function cambiosCampoSupl(){
if (consultando!=true) modificaVariableCambiosCamposSupl();
}
</SCRIPT>
  <input type="text" id="<%= id %>" class="<%= texto %>" value="<%=valor%>"
    maxlength="<%= CAMPO_BEAN.getTamano() %>" name="<%= name %>"
    title="<%= CAMPO_BEAN.getDescCampo() %>" <%= read %>
    onkeyup = "javascript:if (consultando) return SoloCaracteresFechaConsultando(this); else return SoloCaracteresFecha(this);"
    onblur = "javascript:return comprobarFecha(this);"
    onfocus = "this.select();"  >
<% if((!"true".equals(CAMPO_BEAN.getSoloLectura()))&&(!"SI".equals(CAMPO_BEAN.getBloqueado()))) { %>
    <A href="javascript:calClick(event);return false;" onClick="nuevo<%=funcion%>(event);cambiosCampoSupl();return false;"  onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;" >
        <span class="fa fa-calendar" aria-hidden="true" id="<%= nameIMG %>" name="<%= nameIMG %>" alt="Fecha" ></span>
    </A>
<% } %>

<!-- FIN VISTA DE CAMPO FECHA   -->




