<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<!-- VISTA DE CAMPO FECHA   -->
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:useBean id="CAMPO_BEAN" scope="request" class="es.altia.agora.technical.EstructuraCampo"/>
<jsp:useBean id="beanVO" scope="request" class="es.altia.agora.technical.CamposFormulario"/>
<script>
    var consultando = false;
</script>
<%Config m_Config = ConfigServiceHelper.getConfig("common");
  String statusBar = m_Config.getString("JSP.StatusBar");
  String valor = beanVO.getString(CAMPO_BEAN.getCodCampo());
  String fechaVencimiento ="";
  String campoActivo = CAMPO_BEAN.getCampoActivo();
  String plazo = CAMPO_BEAN.getPlazoFecha();
  String checkPlazoFecha = CAMPO_BEAN.getCheckPlazoFecha();
  String alarma ="";
  String activar ="";
  String mostrarAlarma = "";
  
  //1. El bton[Desactivar] estara habilitado si hay una fecha de fin de plazo para el campo suplementario
  Calendar today = Calendar.getInstance();
 
 //Obtenemos el campo plazoActivo de la tabla
  if (("0").equals(CAMPO_BEAN.getCampoActivo())){
     activar ="desactivada" ;
  }else{
      activar ="activada";
  }
 
  //si no hay definido plazo-> no se visualizan bton Activar/Desactivar y Ver e icono Alarma
  if (CAMPO_BEAN.getPlazoFecha()== null){
      alarma ="";
  }else{// la campana es verde
     if((valor!=null)&&(!"".equals(valor))){//hay que comprobar si el plazo no ha vencido siempre y cuando haya un valor en el campo fecha
            String [] dataTemp = valor.split("/");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar c = Calendar.getInstance();
            c.set(Integer.parseInt(dataTemp[2]), Integer.parseInt(dataTemp[1])- 1, Integer.parseInt(dataTemp[0]));
            if (CAMPO_BEAN.getCheckPlazoFecha().equals("D")){
                c.add(Calendar.DAY_OF_YEAR, Integer.parseInt(CAMPO_BEAN.getPlazoFecha()));
            }else {
                if (CAMPO_BEAN.getCheckPlazoFecha().equals("M")){
                    c.add(Calendar.MONTH, Integer.parseInt(CAMPO_BEAN.getPlazoFecha()));
               }else{
                   c.add(Calendar.YEAR, Integer.parseInt(CAMPO_BEAN.getPlazoFecha()));
                }
            }
            //Comprobar si fecha vencimiento es menor que hoy
            if (c.compareTo(today)<0){
                    alarma ="desactivada";//alarma vencida
                }else{
                alarma="activada";//alarma NO vencida
                }
            fechaVencimiento = sdf.format(c.getTime());
            //se graba la fecha de vencimiento calculada
            CAMPO_BEAN.setFechaVencimiento(fechaVencimiento);
            
    
      }else{
          //campoActivo="activada";
      }
  }
  
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
  String name_activar="";
  String id_activar="";
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
  id = name;
  id_activar=id+"_activar";
  name_activar=name+"_activar";  
  
  String consulta = request.getParameter("consulta");
  if ((consulta != null) && (!"".equalsIgnoreCase(consulta))) {
    CAMPO_BEAN.setTamano("25"); // Para que tenga el tamaño justo para poder consultar

  mostrarAlarma = request.getParameter("mostrarAlarma");
  
  
%> <script> consultando = true;</script>
<% } %>

<SCRIPT language="JavaScript">


function <%= funcion %> (evento){ 
  if (document.getElementById("<%= nameIMG %>").className.indexOf("fa-calendar") != -1 ) {
    showCalendar('forms[0]','<%= name %>',null,null,null,'','<%= nameIMG %>','',null,null,null,null,null,null,null,null,evento);
  }
}

function nuevo<%=funcion%>(event){ 
    if (!document.getElementById("<%= name %>").disabled){
        var event = (event) ? event : ((window.event) ? window.event : "");    
        <%=funcion%>(event); 
    }else
        {
            document.getElementById("<%= nameIMG %>").className="fa fa-calendar";
        }
}

function cambiosCampoSupl(){
if (consultando!=true) modificaVariableCambiosCamposSupl();
}
</SCRIPT>
  <input type="text" id="<%= id %>" class="<%= texto %>" value="<%=valor%>"
    maxlength="<%= CAMPO_BEAN.getTamano() %>" name="<%= name %>"
    title="<%= CAMPO_BEAN.getDescCampo() %>" <%= read %>
    onkeyup = "javascript:if (consultando) return SoloCaracteresFechaConsultando(this); else return SoloCaracteresFecha(this);"
    onblur = "if(!consultando) recargarCamposCalculados();javascript:return comprobarFecha(this);"    
    onfocus = "this.select();"
    size="10" >
<% if((!"true".equals(CAMPO_BEAN.getSoloLectura()))&&(!"SI".equals(CAMPO_BEAN.getBloqueado()))) { %>
    <A href="javascript:calClick(event);return false;" onClick="nuevo<%=funcion%>(event);cambiosCampoSupl();return false;"  onblur="javascript: document.getElementById('<%= id %>').focus();ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;" >
        <span class="fa fa-calendar" aria-hidden="true" id="<%= nameIMG %>" name="<%= nameIMG %>" alt="Fecha" ></span>
    </A>
<% } %>
<% if ((alarma.equals("activada")) && (campoActivo.equals("1")) && (mostrarAlarma==null || mostrarAlarma=="")) {%>
        <A style="text-decoration:none;" >
            <span class="fa fa-bell" alt='Alarma' id="imagenBoton<%=name%>" name="imagenBoton<%=name%>"></span>
        </A>
<% }else if ((alarma.equals("desactivada"))  && (campoActivo.equals("1")) && (mostrarAlarma==null || mostrarAlarma=="")) {%>
        <A style="text-decoration:none;" >
            <span class="fa fa-bell corAlarmaVencida" alt='Alarma' id="imagenBoton<%=name%>" name="imagenBoton<%=name%>"></span>
        </A>    
   <%}else{%>        
    <A style="text-decoration:none;" >
        <span class="fa" alt='Alarma' id="imagenBoton<%=name%>" name="imagenBoton<%=name%>"></span>
    </A>
    
<%}%>

<%if (!(null==plazo) && (mostrarAlarma==null || mostrarAlarma=="")){ %>
<input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("etiq_verAlarma")%>" name="cmdVer<%=name%>" onclick="onClickVer('<%=name%>', '<%=plazo%>', '<%=checkPlazoFecha%>', '<%=campoActivo%>');">
<% } %>

<%if (!(null==plazo) && (mostrarAlarma==null || mostrarAlarma=="")){
    if (activar.equals("desactivada")){%>     
        <!--inline Activar e none Desactivar-->
        <A id="enlaceActivar<%=id%>" style="text-decoration:none;visibility:visible;display:inline;" onclick="onClickActivar('<%= id %>');">
            <span class='fa fa-times-circle' id="cmdActivar<%=id%>" name="cmdActivar<%=name%>"
                 title="<%=descriptor.getDescripcion("gbActivar")%>"></span>
        </A>
        <A id="enlaceDesactivar<%=id%>" style="text-decoration:none;visibility:hidden;display:none;" onclick="onClickDesactivar('<%= id %>');">
            <span class='fa fa-check' id="cmdDesactivar<%=id%>" name="cmdDesactivar<%=id%>"
                  title="<%=descriptor.getDescripcion("gbDesactivar")%>"></span>
        </A>
        <%--<input style="visibility:visible;display:inline" type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbActivar")%>" id="cmdActivar<%=id%>" name="cmdActivar<%=name%>" onclick="onClickActivar('<%= id %>');">--%>
        <%--<input style="visibility:hidden;display:none" type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbDesactivar")%>" id ="cmdDesactivar<%=id%>" name="cmdDesactivar<%=name%>" onclick="onClickDesactivar('<%= id %>');">--%>
    <% }else{ %>
        <!--inline Desactivar e none Activar-->
        <A id="enlaceActivar<%=id%>" style="text-decoration:none;visibility:hidden;display:none;" onclick="onClickActivar('<%= id %>');">
            <span class='fa fa-times-circle' id="cmdActivar<%=id%>" name="cmdActivar<%=name%>"
                  title="<%=descriptor.getDescripcion("gbActivar")%>"></span>
        </A>
        <A id="enlaceDesactivar<%=id%>" style="text-decoration:none;visibility:visible;display:inline;" onclick="onClickDesactivar('<%= id %>');">
            <span class='fa fa-check' id="cmdDesactivar<%=id%>" name="cmdDesactivar<%=id%>"
                  title="<%=descriptor.getDescripcion("gbDesactivar")%>"></span>
        </A>
        <%--<input style="visibility:hidden;display:none" type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbActivar")%>" id="cmdActivar<%=id%>" name="cmdActivar<%=name%>" onclick="onClickActivar('<%= id %>');">--%>
        <%--<input style="visibility:visible;display:inline"type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbDesactivar")%>" id ="cmdDesactivar<%=id%>" name="cmdDesactivar<%=name%>" onclick="onClickDesactivar('<%= id %>');">--%>
    <% }
}%>
        
<input type="text" style="visibility:hidden;display:none" id="activar<%= id %>" name="activar<%= name %>" value="<%=activar%>" size="10" maxlength="10"readonly="true"/>	        


<!-- FIN VISTA DE CAMPO FECHA   -->
