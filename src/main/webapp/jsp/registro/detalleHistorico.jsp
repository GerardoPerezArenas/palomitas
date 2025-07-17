<!-- JSP de detalles de un movimiento de una anotación -->
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html:html>
    <head>
        <title>Detalle de un movimiento de una anotación</title>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
    <%
        // Tomamos codigos de aplicación y de idioma del usuario
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

    <!-- ********************* JAVASCRIPT **************************    -->

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >
<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
<script type="text/javascript">

    function pulsarSalir(){
        top.close();
    }

</script>

<style TYPE="text/css">
  .textoDetalles { background-color:white; 
                   text-align:left;
                   vertical-align:top;                                                                
                   font-family:monospace;
                   font-size: 13px;
  }

  textarea { padding-left:20px;
             margin-bottom: 5px;
             border:0px;
             font-family:monospace;
             font-size: 13px;
             width:650px;
             overflow-y:visible; }
             
  div { padding-left:10px; }
  
  ul { padding-left:0px; 
       margin-top: 0px; 
       margin-bottom: 10px; }
</style>

    </head>

    <body class="bandaBody" onload="pleaseWait('off');" >

        <!-- Mensaje de esperar mientras carga  -->
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

        <!-- Fin mensaje de esperar mientras carga  -->

        <div id="titulo" class="txttitblanco">
            <%= descriptor.getDescripcion("tit_DetallesHist")%>
        </div>
        <div class="contenidoPantalla">
           <div style="overflow-y:auto;">         
               <table style="width:100%" >
                       <tr>
                           <td name="detalles" class="textoDetalles"><bean:write name="MantAnotacionRegistroForm" property="detallesMovimientoHTML" filter="false"/></td>
                       </tr>
               </table>
           </div>   
            <div class="botoneraPrincipal">
                <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir" onClick="pulsarSalir();" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>'>
            </div>
        </div>
    </body>
    </html:html>
