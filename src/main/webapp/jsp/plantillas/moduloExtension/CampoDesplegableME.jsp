<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@page import="java.util.ArrayList"%>


<!-- VISTA DE CAMPO DESPLEGABLE   -->
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:useBean id="CAMPO_BEAN" scope="request" class="es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.EstructuraCampoModuloIntegracionVO"/>

<%
  String valor =CAMPO_BEAN.getCodCampo();
  if(valor==null) valor="";
 
  String texto = "inputTexto";
  String id="";
  String name ="";
  name = CAMPO_BEAN.getCodCampo();
  id = name;
   
%>

<input type="text" id="<%= id %>" name="cod<%=name%>" onkeyup=" return xAMayusculas(this);"
       class="<%= texto %>" style="width: 10%" maxlength="8" >
<input type="text" id="<%= id %>" name="desc<%=name%>" class="<%= texto %>"  style="width: 80%" readonly="true"  >



<A href="" id="anchor<%=name%>" name="anchor<%=name%>" style="text-decoration:none;" >
    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="boton<%=name%>" name="boton<%=name%>" style="cursor:hand;"></span>
</A>




    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript">
    
</script>
<!-- FIN VISTA DE CAMPO DESPLEGABLE   -->
