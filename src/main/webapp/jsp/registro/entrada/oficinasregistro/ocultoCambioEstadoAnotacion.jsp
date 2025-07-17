<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%
    response.setHeader("Cache-control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
 %>
<%@ page contentType="text/html;charset=ISO-8859-15" language="java" pageEncoding="ISO-8859-15"%>
<html>
 <head>
     <title></title>
<script type="text/javascript">

    var anotaciones = new Array();
    var contador = 0;

    function redirecciona(){
        var pagina = "<%=(String)request.getAttribute("pagina_recargar")%>";
        var error   = "<%=(String)request.getAttribute("error_operacion")%>";
        
        if(pagina!=null && pagina!="" && (error==null || error=="" || error=="null")){         
            for (i=0;i<parent.mainFrame.anotacionesCheck.length;i++) {
                parent.mainFrame.anotacionesCheck[i] = 0;
            }
            parent.mainFrame.valoresAnotacionesCheck = new Array();
            parent.mainFrame.valoresAnotacionesEstado = new Array();
            
            parent.mainFrame.cargarPagina(pagina);
        }
        else
        if((pagina==null || pagina=="") && error!=null && error!=""){            
            parent.mainFrame.mostrarErrorOperacion(error);
        }

   }

</script>
 </head>
 <body onload="javascript:redirecciona();">
 </body>
</html>