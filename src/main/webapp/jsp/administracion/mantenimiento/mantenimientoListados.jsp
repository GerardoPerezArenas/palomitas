<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Mantenimiento de Procesos </title>
        <!-- Estilos -->
   
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 5;
            String estilo="";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                          estilo=usuarioVO.getCss();
            }%>
            
             <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=estilo%>">
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript">
            
            var lista = new Array();
            var listado = new Array();
            var codlistado = new Array();
            var desclistado = new Array();

            
    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function inicializar(){
        window.focus();
        cargaTablaListados();
    }


    function pulsarConsultar(fila) {
        if(fila == -1) {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
        } else {
           document.forms[0].opcion.value = 'consultarCampos';
           document.forms[0].codigo.value= listado[fila][0];
           document.forms[0].descripcion.value = listado[fila][1];
           
             document.forms[0].target = "mainFrame";
             document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Listados.do';
            document.forms[0].submit();
        }
    }

                
     // FUNCIONES DE PULSACION DE BOTONES
                    function pulsarSalir(){
                        
                        document.forms[0].target = "mainFrame";
                        document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
                        document.forms[0].submit();
                    }

    function callFromTableTo(rowID,tableName){
        pulsarConsultar(rowID);
    }
        </script>
    </head>
    
    <body class="bandaBody" onload="javascript:{pleaseWait('off');
        inicializar();}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
    
        
<form  method="post">
    <input  type="hidden"  name="opcion" id="opcion">
    <input  type="hidden"  name="codigo" value="">
    <input  type="hidden" name="descripcion" value="">

    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gTit_MantList")%></div>
    <div class="contenidoPantalla">
        <table style="width: 100%">
            <tr> 
                <td id="tabla"></td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> 
                   name="botonSalir" onClick="pulsarSalir();" accesskey="S">  
        </div>                        
    </div>                        
</form>

<script type="text/javascript">  
            
            //Creamos tablas donde se cargan las listas
            var tablaListados = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
            // Anadir tipo de dato a cada columna;
            tablaListados.addColumna('0','left','CODIGO','String');
             tablaListados.addColumna('875','center','<%=descriptor.getDescripcion("gEtiq_MantList")%>','String');
            tablaListados.displayCabecera=true;
            
            tablaListados.displayTabla();
            function cargaTablaListados(){


                 <%
                    MantenimientosAdminForm bForm = (MantenimientosAdminForm) session.getAttribute("MantenimientosAdminForm");
                    Vector listaListados= bForm.getListadosParametrizables();
                    int lengthListados= listaListados.size();
                    int i = 0;
                 %>

                     //sacamos listado de estilos

                     var z=0;
                <%for (i = 0; i < lengthListados; i++) {
                    GeneralValueObject listado = (GeneralValueObject) listaListados.get(i);%>
                    listado[z]=['<%=(String) listado.getAtributo("codigo")%>','<%=(String) listado.getAtributo("descripcion")%>'];
                    codlistado[z]= listado[z][0];
                    lista[z]= listado[z][1];
                    z++;
                    <%}%>
                    tablaListados.lineas=listado;
                    tablaListados.displayTabla();
            }       
                       
            
            
            
            
        </script>
        
        
    </body>
</html>
