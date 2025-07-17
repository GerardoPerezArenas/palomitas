<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.registro.InformesRegistroForm" %>
<%@page import="es.altia.agora.business.registro.RegistroValueObject" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils" %>

<%@page import="java.util.Vector" %>

<html>
<head>

<title>Oculto Busqueda Informe Registro </title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<%
            int idioma = 1;
            int apl = 1;
            String css = "";
            if (session != null && session.getAttribute("usuario") != null) {
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    apl = usuario.getAppCod();
                    css = usuario.getCss();
                }
            }
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  
                     type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        
<script type="text/javascript">
function redirecciona() {
    
        var listadoAnotaciones = new Array();
       <%
        InformesRegistroForm f= (InformesRegistroForm)  session.getAttribute("InformesRegistroForm");
        Vector<RegistroValueObject> anotacionesLibro=f.getAnotacionesLibro();
        
         for(int i=0;i<anotacionesLibro.size();i++){
              RegistroValueObject rVO=(RegistroValueObject )anotacionesLibro.get(i);
              String asunto=StringEscapeUtils.escapeJavaScript(rVO.getAsunto());
              
              String nombre=rVO.getNombreInteresado();
              String apellido1=rVO.getApellido1Interesado();
              String apellido2=rVO.getApellido2Interesado();
              String nombreCompleto="";
              if(nombre!=null && !"".equals (nombre) && !("null").equals(nombre)) nombreCompleto=nombreCompleto+nombre;
              if(apellido1!=null && !"".equals (apellido1) && !("null").equals(apellido1)) nombreCompleto=nombreCompleto+" "+apellido1;
              if(apellido2!=null && !"".equals (apellido2) && !("null").equals(apellido2)) nombreCompleto=nombreCompleto+" "+apellido2;
              
              %>
                      
                       listadoAnotaciones[<%=i%>] = ["<input type='checkbox' class='checkLinea' value='<%= rVO.getNumReg() %>'/>",           
                                "<%= rVO.getNumReg() %>",
                                "<%= rVO.getFecEntrada()%>",
                                "<%= asunto%>",
                               "<%= nombreCompleto%> ", 
                                "<%= rVO.getOrganizacionOrigen()%>"];         
          <%     
         } %>
 
        parent.mainFrame.recuperaListado(listadoAnotaciones);
    }


</script>
</head>
<body onLoad="redirecciona();">
<p>&nbsp;</p><center/>
</body>
</html>    

                                