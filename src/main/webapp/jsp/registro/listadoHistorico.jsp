<%-- 
    Document   : listadoHistorico
    Created on : 25-sep-2008, 16:57:18
    Author     : juan.jato
--%>

<!-- JSP de listado de movimientos de anotaciones -->
<%@page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html:html>
    <head>
        <title>Listado de movimientos de una anotación</title>
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


    <!-- ********************* JAVASCRIPT **************************    -->

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<html:rewrite page='/scripts/TablaNueva.js'/>"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >
<script type="text/javascript">

    // Guarda los códigos de los movimientos
    var lista_codigos = new Array();
    // Guarda los movimientos con 0-descripcion del tipo de movimiento, 2-usuario y 3-fecha
    var lista = new Array();

    /////////////// Funcion de inicializacion de la pagina.

    function Inicio() {
        window.focus();
        cargaTabla();
    }

    /////////////// Funciones de los botones.
    
    function pulsarVer() {
        if(tablaMovimientos.selectedIndex == -1) {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
        } else {
           codigo = lista_codigos[tablaMovimientos.selectedIndex];
           var source = "<html:rewrite page='/MantAnotacionRegistro.do'/>?opcion=cargarMovimientoHistorico&operacion=" + codigo;
           abrirXanelaAuxiliar("<html:rewrite page='/jsp/registro/mainVentana.jsp?source='/>" + source,undefined,
	'width=800,height=600',function(relaciones){});
        }
    }

    function pulsarSalir(){
        top.close();
    }

    /////////////// Control teclas.

    /* Controla la pulsacion de un doble clic sobre alguna de las filas de la tabla*/
    function checkDobleClic(){
        if(tablaMovimientos.selectedIndex > -1 && !tablaMovimientos.ultimoTable) pulsarVer();
    }

</script>

    </head>

    <body class="bandaBody" onload="javascript:{ pleaseWait('off');
       }" >

        <!-- Mensaje de esperar mientras carga  -->
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

        <!-- Fin mensaje de esperar mientras carga  -->
    <div id="titulo" class="txttitblanco">
        <%= descriptor.getDescripcion("tit_Historico")%>&nbsp;
        <bean:write name="MantAnotacionRegistroForm" property="ejercicioAnotacion"/>&nbsp;/
        <bean:write name="MantAnotacionRegistroForm" property="numeroAnotacion"/>
    </div>
    <div class="contenidoPantalla">
        <table align="center">
                 <tr>
                <td id="tabla" ondblclick="javascript:checkDobleClic();"></td>
                 </tr>
                 <tr>
                    <td height="30px"></td>
                 </tr>
         </table>
        <div class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbVer")%> name="cmdAlta" onClick="pulsarVer();" alt="<%= descriptor.getDescripcion("toolTip_verMovHist")%>" title="<%= descriptor.getDescripcion("toolTip_verMovHist")%>">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir" onClick="pulsarSalir();" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>'>
        </div>
    </div>
  <script type="text/javascript">
    var tablaMovimientos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

    tablaMovimientos.addColumna('320','left',"<str:escape><%=descriptor.getDescripcion("etiqDescMov")%></str:escape>");
    tablaMovimientos.addColumna('301','center',"<str:escape><%=descriptor.getDescripcion("etUsuarioForm")%></str:escape>");
    tablaMovimientos.addColumna('150','center',"<str:escape><%=descriptor.getDescripcion("gEtiqFechaHora")%></str:escape>");
    tablaMovimientos.displayCabecera=true;

    function cargaTabla(){
       var i = 0;
       <logic:iterate id="movimiento" name="MantAnotacionRegistroForm" property="movimientosHistorico">
         lista[i] = ["<str:escape><bean:write name="movimiento" property="descMovimiento" filter="false"/></str:escape>",
                     "<str:escape><bean:write name="movimiento" property="nombreUsuario" filter="false"/></str:escape>",
                     '<bean:write name="movimiento" property="fecha"/>'];
                            
         lista_codigos[i] = '<bean:write name="movimiento" property="codigo" />';
         i++;
       </logic:iterate>

      tablaMovimientos.lineas=lista;
      tablaMovimientos.displayTabla();
    }

   </script>
    <script> Inicio(); </script>
    </body>
  </html:html>
