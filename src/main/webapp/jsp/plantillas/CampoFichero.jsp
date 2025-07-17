<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="java.text.MessageFormat"%>
<!-- VISTA DE CAMPO FICHERO   -->
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
    texto = "inputTextoObligatorio2";
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
       name =  CAMPO_BEAN.getCodCampo() + "_" + CAMPO_BEAN.getOcurrencia();
    }
  if(ob.equals("obligatorio")) {
    id = ob;
  } else {
    id = name;
  }
  String opcion="0";
  if (CAMPO_BEAN.getCodTramite() != null) {
     opcion = "1";
  }    

  Config m_Config = ConfigServiceHelper.getConfig("common");
  
%>

<table cellpadding="0px" cellspacing="0px">
    <tr>
        <td>
            <input style="height: 17px;width:450px" type="text" name="<%= name %>" id="<%= name %>" class="<%= texto %>" size=75 readonly="true" >
                        
            <% if (!read.equals("readonly")) { %><html:rewrite page=""/>
                <A href="javascript:ventanaPopUpModal('inicio','<%=name%>');" style="text-decoration:none;" >
                    <span class="fa fa-paperclip" id="imagenBoton<%=name%>" alt='Fichero' name="botonFichero" ></span>
                </A>
            <% } %>
        </td>
        <td style="width: 12px"></td>
        <td>
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbVisualizar")%>" name="cmdVisualizar<%=name%>" onclick="onClickDocumento('<%=name%>','<%=valor%>');">
        </td>
        <% if (!read.equals("readonly")) { %>
        <td style="width: 2px"></td>
        <td>
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>" name="cmdEliminar<%=name%>" onclick="onClickEliminarDocumento('<%=name%>');">            
        </td>
            <%
           if ("si".equals(m_Config.getString("JSP.BotonCSV"))){
           %>
               <td style="width: 2px"></td>
               <td>
                   <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCSV")%>" name="cmdCSV<%=name%>" onclick="onClickCrearCSVDocumento('<%=name%>');">            
               </td>
           <% } %>
        <% } %>
    </tr>
</table>

<script type="text/javascript">
var valorCampo = "<%=valor%>";
document.getElementById('<%=name%>').value = valorCampo;
if (valorCampo==""){
    //var boton = [document.forms[0].cmdVisualizar<%=name%>];
    //deshabilitarGeneral(boton);
    deshabilitarBoton(document.forms[0].cmdVisualizar<%=name%>);
    <% if (!read.equals("readonly")) {%>
        deshabilitarBoton(document.forms[0].cmdEliminar<%=name%>);
        <%if ("si".equals(m_Config.getString("JSP.BotonCSV"))){%>
        deshabilitarBoton(document.forms[0].cmdCSV<%=name%>);
        <%}%>
    <%}%>
}
</script>