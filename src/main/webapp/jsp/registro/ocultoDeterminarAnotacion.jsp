<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>

<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.agora.business.registro.RegistroValueObject" %>
<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm" %>

<%@ page import="java.util.Vector" %>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp"/>
<html>
<head>
    <title> Oculto cargar pagina </title>
    <%
        int idioma = 1;
        int apl = 1;
        if ((session.getAttribute("usuario") != null) && (session.getAttribute("registroUsuario") != null)) {
            UsuarioValueObject usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
            idioma = usuarioVO.getIdioma();
            apl = usuarioVO.getAppCod();
        }
    %>

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
                 type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
    <jsp:setProperty name="descriptor" property="idi_cod" value="<%= idioma%>"/>
    <jsp:setProperty name="descriptor" property="apl_cod" value="<%= apl %>"/>

    <script>

        function redirecciona()
        {

        <% /* Recuperar el vector de anotaciones de la sesion. */
               MantAnotacionRegistroForm f= (MantAnotacionRegistroForm)  session.getAttribute("MantAnotacionRegistroForm");
               RegistroValueObject arVO = f.getRegistro();

               Vector relacionAnotaciones = (Vector) session.getAttribute("RelacionAnotaciones");
               int numRelacionAnotaciones = ((Integer) session.getAttribute("NumRelacionAnotaciones")).intValue();
               int posAnotacion = 1;               
               
               try{
                    posAnotacion = Integer.parseInt(arVO.getPosicionAnotacion());
                }catch(NumberFormatException e){
                }

               String strAno="";
               String strNum="";

               if ((numRelacionAnotaciones > 0) && (relacionAnotaciones.size() > 0)) {
                   Vector cp_anotacion = (Vector) relacionAnotaciones.elementAt(0);
                   strAno = (String) cp_anotacion.elementAt(3);
                   strNum = (String) cp_anotacion.elementAt(4);
               } else {
                   posAnotacion = -1;
               }
        %>

            parent.mainFrame.numAnotaciones = '<%=numRelacionAnotaciones%>';
            parent.mainFrame.anoSeleccionada = '<%=strAno%>';
            parent.mainFrame.numeroSeleccionada = '<%=strNum%>';
            parent.mainFrame.actualizaAnotacionNavegacion('<%=posAnotacion%>');	             
        }
    </script>

</head>
<body onLoad="redirecciona();">

<p>&nbsp;

<p>
<center/>


</body>
</html>
