<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<!-- VISTA DE CAMPO FECHA   -->
<jsp:useBean id="CAMPO_BEAN" scope="request"  class="es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.EstructuraCampoModuloIntegracionVO"/>

<script>
    var consultando = false;
</script>
<%
  String valorC = CAMPO_BEAN.getCodCampo();
  String  valor="";
  
  String texto = "";
  
  texto = "inputTxtFecha";

 
  String nameIMG = "cal" + CAMPO_BEAN.getCodCampo();
  String read = "";
  String id=valorC;
  String name =valorC;
 
  
  String consulta = request.getParameter("consulta");
  if ((consulta != null) && (!"".equalsIgnoreCase(consulta))) {
    CAMPO_BEAN.setTamanho(25); // Para que tenga el tamaño justo para poder consultar
%> <script> consultando = true;</script>
<% } %>

<SCRIPT language="JavaScript">

function mostrarFecha(evento){ 
  if (document.getElementById("<%= nameIMG %>").className.indexOf("fa-calendar") != -1 ) {
    showCalendar('forms[0]','<%= name %>',null,null,null,'','<%= nameIMG %>','',null,null,null,null,null,null,null,null,evento);
  }
}

function nuevoMostrarFecha(event){ 
    var event = (event) ? event : ((window.event) ? window.event : "");    
    mostrarFecha(event);    
}



</SCRIPT>
  <input type="text" id="<%= id %>" class="<%= texto %>" value="<%=valor%>"
    maxlength="<%= CAMPO_BEAN.getTamanho() %>" name="<%= name %>"
    title="<%= CAMPO_BEAN.getRotulo() %>" <%= read %>
    onkeyup = "javascript:if (consultando) return SoloCaracteresFechaConsultando(this); else return SoloCaracteresFecha(this);"
    onblur = "javascript:return comprobarFecha(this);"
    onfocus = "this.select();"  >

    <A href="javascript:calClick(event);return false;" onClick="nuevoMostrarFecha(event);return false;"  onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;" >
        <span class="fa fa-calendar" aria-hidden="true" id="<%= nameIMG %>" name="<%= nameIMG %>" alt="Fecha" ></span>
    </A>


<!-- FIN VISTA DE CAMPO FECHA   -->




