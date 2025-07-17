<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="java.util.Vector"%>


<!-- VISTA DE CAMPO DESPLEGABLE   -->
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:useBean id="CAMPO_BEAN" scope="request" class="es.altia.agora.technical.EstructuraCampo"/>
<jsp:useBean id="beanVO" scope="request" class="es.altia.agora.technical.CamposFormulario"/>
<%
  String valor = beanVO.getString(CAMPO_BEAN.getCodCampo());
  if(valor==null) valor="";
  String obligatorio = CAMPO_BEAN.getObligatorio();
  String texto = "";
  String ob = "";
  Vector<String> codigos=(Vector)CAMPO_BEAN.getListaCodDesplegable();
  Vector<String> valores=(Vector)CAMPO_BEAN.getListaDescDesplegable();


  String valorC="";
  String vaux="'"+valor+"'";

  for(int i=0;i<codigos.size();i++)
  {
      String v=(String)codigos.elementAt(i);
      
      if(v.equals(vaux))  valorC=((String)valores.elementAt(i)).replace("'","");      
  }

  if("1".equals(obligatorio)) {
    texto = "inputTextoObligatorio";
    ob = "obligatorio";
  } else {
    texto = "inputTexto";
  }
  String read = "";
  String id="";
  String name ="";
  String disabled ="";
    if("true".equals(CAMPO_BEAN.getSoloLectura())) {
      read = "readonly";
    }
    if("SI".equals(CAMPO_BEAN.getBloqueado())) {     
      read = "readonly";
      texto="inputTexto inputTextoDeshabilitado";
      disabled="disabled";
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

     if(!"SI".equals(CAMPO_BEAN.getBloqueado())) {
%>

<input type="text" id="<%= id %>" name="cod<%=name%>" onkeyup=" return xAMayusculas(this);"
       class="<%= texto %>" <%=disabled%> style="width: 10%" maxlength="8" >
<input type="text" id="<%= id %>" name="desc<%=name%>" <%=disabled%> class="<%= texto %>"  style="width: 80%" readonly="true"  >



<A href="" id="anchor<%=name%>" name="anchor<%=name%>" style="text-decoration:none;" >
    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="boton<%=name%>" name="boton<%=name%>" style="cursor:hand;"></span>
</A>

<%}else{%>
<input type="hidden" id="<%= id %>" name="cod<%=name%>"
       class="<%= texto %>" disabled style="width: 10%" maxlength="8" >
<input type="hidden" id="<%= id %>" name="desc<%=name%>" disabled class="<%= texto %>"  style="width: 80%" readonly="true"  >


 <input type="text" id="texto1" name="texto1"
       class="<%= texto %>" disabled style="width: 10%" maxlength="8" value="<%=valor%>" readonly="true" >
 <input type="text" id="texto2" name="texto2" disabled class="<%= texto %>"  style="width: 80%" readonly="true" value="<%=valorC%>" >
 <A href="" id="anchor<%=name%>" name="anchor<%=name%>" style="text-decoration:none;" >
    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="boton<%=name%>" name="boton<%=name%>" style="cursor:hand;"></span>
</A>
<%}%>

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript">
    
</script>
<!-- FIN VISTA DE CAMPO DESPLEGABLE   -->
