<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.Vector"%>

<html>
    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Lista de Procesos </title>
   
        
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 5;
            String css="";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                css=usuarioVO.getCss();
            }%>
            
             <!-- Estilos -->
    
            
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
        <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript">
            var datosProcesos = new Array();
            var datosProcesosOriginal = new Array();
            function inicializar(){
                window.focus();
                datosProcesosOriginal = self.parent.opener.xanelaAuxiliarArgs;
                for (var i=0; i<datosProcesosOriginal.length; i++) 
                    datosProcesos[i]=[datosProcesosOriginal[i][3],datosProcesosOriginal[i][1]];
                tablaProcesos.lineas = datosProcesos;
                tablaProcesos.displayTabla();				
            }
            
            // FUNCIONES DE LIMPIEZA DE CAMPOS
            function limpiarFormulario(){
                tablaProcesos.lineas = new Array();
                tablaProcesos.displayTabla();
            }
            
            // FUNCIONES DE PULSACION DE BOTONES
            
            function pulsarCerrar(retorno){
                self.parent.opener.retornoXanelaAuxiliar(retorno);
            }
            
            function pulsarAceptar(){
                if(document.forms[0].todos.checked == false) {
                    var i = tablaProcesos.selectedIndex;
                    if((i>=0)&&(!tablaProcesos.ultimoTable)) {
                        var datos = new Array();
                        datos[0] = datosProcesosOriginal[i][0];
                        pulsarCerrar(datos);
                    } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                    } else {
                        self.parent.opener.retornoXanelaAuxiliar("todos");
                }
            }
            
        </script>
    </head>
    
    <body class="bandaBody" onload="javascript:{pleaseWait('off');
        inicializar();}">
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        
<form  method="post">
    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiq_listProcesos")%></div>
    <div class="contenidoPantalla">
        <table style="width:100%">                                                    
            <tr>
                <td id="tabla"></td>
            </tr>
            <tr>
                <td class="columnP">
                    &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox"  name="todos" value="si">&nbsp;&nbsp;<span style="text-color: #000000"><%=descriptor.getDescripcion("anadTodos")%></span>
                </td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAceptar")%> 
                                       name="botonAceptar" onClick="pulsarAceptar();" accesskey="A"> 
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> 
                                       name="botonCancelar" onClick="pulsarCerrar();" accesskey="M"> 
        </div>            				            
    </div>            				            
</form>
        
        <script type="text/javascript">  
            
            var tablaProcesos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));  
            tablaProcesos.addColumna('400',null,'<%= descriptor.getDescripcion("gEtiq_Descrip")%>');
                tablaProcesos.addColumna('500',null,'<%= descriptor.getDescripcion("gEtiq_Formulario")%>');
                    tablaProcesos.displayCabecera = true;  
                    
                    function callFromTableTo(rowID,tableName){
                        if((rowID>=0)&&(!tableName.ultimoTable)) {
                            pulsarAceptar();	
                        }
                    }	
                    
                    tablaProcesos.colorLinea=function(rowID) {
                        if(datosProcesosOriginal[rowID][5]==1)
                            return 'gris';
                    }
                    
                    document.onmouseup = checkKeys; 
                    
                    function checkKeysLocal(evento,tecla){
                        var teclaAuxiliar ="";
                        if(window.event){
                            evento = window.event;
                            teclaAuxiliar = evento.keyCode;
                        }else
                            teclaAuxiliar = evento.which;

                        keyDel(evento);

                        if(teclaAuxiliar == 40 || teclaAuxiliar == 38){
                            upDownTable(tablaProcesos,datosProcesos,teclaAuxiliar);
                        }    
                        if(teclaAuxiliar == 13){
                            pushEnterTable(tablaProcesos,datosProcesos);
                        }
                    }
                    
        </script>
        
        
    </body>
</html>

