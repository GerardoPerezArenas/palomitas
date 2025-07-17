<%-- 
    Document   : ocultoRelacionReserva
    Created on : 11-nov-2008, 14:30:10
    Author     : juan.jato
--%>

<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<html>
<head>
<title> Oculto relacion de reservas </title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<script type="text/javascript">

function redirecciona(){
   var opcion = '<%= request.getParameter("opcion") %>';
   
   if (opcion == 'ok') {
        // Se recarga el listado de reservas
        parent.mainFrame.lista = new Array();
        parent.mainFrame.anho = new Array();
        parent.mainFrame.num = new Array();
        var cont = 0;
        <logic:iterate id="elemento" name="ReservaOrdenForm" property="codigos">
                parent.mainFrame.lista[cont]= ['<bean:write name="elemento" property="dia" />' + "/" + '<bean:write name="elemento" property="mes" />' +
                 "/" + '<bean:write name="elemento" property="ano" />' + " " + '<bean:write name="elemento" property="hora" />' +
                        ":" + '<bean:write name="elemento" property="min" />', '<bean:write name="elemento" property="ejercicio"/>' + "/" + '<bean:write name="elemento" property="txtNumRegistrado"/>' ,'<bean:write name="elemento" property="nombreUsuario"/>'];
                parent.mainFrame.anho[cont] = ['<bean:write name="elemento" property="ejercicio"/>'];
                parent.mainFrame.num[cont] = ['<bean:write name="elemento" property="txtNumRegistrado"/>'];
                parent.mainFrame.usuario[cont]=['<bean:write name="elemento" property="usuario"/>'];
            cont = cont + 1;
        </logic:iterate>
        parent.mainFrame.anularCorrecto();
   } else {
        parent.mainFrame.falloAnular();
   }
}

</script>
</head>
<body onLoad="redirecciona();">
<p>&nbsp;</p><center/>
</body>
</html>
