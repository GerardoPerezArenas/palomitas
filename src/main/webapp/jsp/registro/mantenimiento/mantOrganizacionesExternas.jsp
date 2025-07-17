<!-- JSP de mantenimiento de temas -->

<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
    <head>
        <title> Mantenimiento Organizaciones Externas</title>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
        
        
        <%
            int idioma = 1;
            int apl = 1;
            String css = "";
            if (session != null) {
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    apl = usuario.getAppCod();
                    css = usuario.getCss();
                }
            }
        %>
        
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >
        <script type="text/javascript" >
            var lista_ide = new Array();
            var lista= new Array();
            var i;
            var res = 0;
            var ultimo = false;
            var existe = 0;
            
            function borrarDatos(){
                document.forms[0].codOrganizacionExterna.value = '';
                document.forms[0].desOrganizacionExterna.value = '';
                habilitarGeneralInputs(['codOrganizacionExterna'],true);
            }
            
            function Inicio() {
                window.focus();
                cargaTabla();
            }
            
            function pulsarEliminar() {
                
                if((tab.selectedIndex != -1)  && !tab.ultimoTable ) {
                    if (jsp_alerta("C",'<%=descriptor.getDescripcion("msjElimOrgExt")%>')==1) {
                        var id = lista[tab.selectedIndex][0];
                        document.forms[0].idOrganizacionExterna.value = id;
                        document.forms[0].opcion.value = 'eliminarOrganizacionExterna';
                        document.forms[0].target = "oculto";
                        document.forms[0].action = '<%=request.getContextPath()%>/registro/mantenimiento/MantRegistroExterno.do';
                        document.forms[0].submit();
                    }
                } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                }
                
                function pulsarModificar() {
                    
                    var cod = document.forms[0].codOrganizacionExterna.value;
                    if((tab.selectedIndex != -1)  && !tab.ultimoTable ) {
                        if (validarFormulario()) {
                            var id = lista[tab.selectedIndex][0];
                            document.forms[0].idOrganizacionExterna.value = id;
                            document.forms[0].opcion.value = 'modificarOrganizacionExterna';
                            document.forms[0].target = "oculto";
                            document.forms[0].action = '<%=request.getContextPath()%>/registro/mantenimiento/MantRegistroExterno.do';
                            document.forms[0].submit();
                        }
                    } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                    }
                    
                    function pulsarAlta() {
                        
                        var cod = document.forms[0].codOrganizacionExterna.value;
                        var yaExiste = 0;
                        if (validarFormulario()) {
                            for(l=0; l < lista.length; l++){
                                if ((lista[l][0]) == cod ){
                                    yaExiste = 1;
                                }
                            }
                            if(yaExiste == 0) {
                                document.forms[0].idOrganizacionExterna.value = cod;
                                document.forms[0].opcion.value = 'altaOrganizacionExterna';
                                document.forms[0].target = "oculto";
                                document.forms[0].action = '<%=request.getContextPath()%>/registro/mantenimiento/MantRegistroExterno.do';
                                document.forms[0].submit();
                            } else {
                            jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
                            }
                        }
                    }
                    
                    function pulsarSalir(){
                        document.forms[0].target = "mainFrame";
                        document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
                        document.forms[0].submit();
                    }
                    
                    function limpiarInputs() {    
                        tab.selectLinea(tab.selectedIndex);
                        borrarDatos();
                    }
                    
                    function recuperaDatos(resultado, nuevaLista) {
                        lista = new Array();
                        lista = nuevaLista;
                        tab.lineas=lista;
                        refresh();
                        if (resultado == 'modificada' || resultado=='eliminada' || resultado=='insertada') {
                            limpiarInputs();
                        } else {
                        if (resultado=='no insertada')
                            jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoAltaOrgExt")%>');
                                else if (resultado=='no modificada')
                                    jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoModOrgExt")%>');
                                        else if (resultado=='no eliminada')
                                            jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoElimOrgExt")%>');
                                                else if (resultado=='tiene dependencias')
                                                    jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoElimOrgExtDep")%>');
                                                    }
                                                    
                                                }
                                                
                                                function rellenarDatos(tableObject, rowID){
                                                    if(rowID>-1 && !tableObject.ultimoTable){
                                                        document.forms[0].codOrganizacionExterna.value = lista[rowID][0];
                                                        document.forms[0].desOrganizacionExterna.value = lista[rowID][1];
                                                        habilitarGeneralInputs(['codOrganizacionExterna'],false);
                                                    } else borrarDatos();
                                                }
                                                
                                                /////////////// Control teclas.
                                                
                                    function checkKeysLocal(evento, tecla) {
                                        var aux=null;
                                        if(window.event)
                                            aux = window.event;
                                        else
                                            aux = evento;

                                        var tecla = 0;
                                        if(aux.keyCode)
                                            tecla = aux.keyCode;
                                        else
                                            tecla = aux.which;
                                        
                                        if (tecla == 38 || tecla == 40){
                                            upDownTable(tab,lista,tecla);
                                                    }
                                                    //if (event.keyCode == 13) buscar();
                                        keyDel(aux);
                                                }
                                                
                                                
                                                document.onkeydown=checkKeys;
                                                
        </SCRIPT>
        
    </head>
    
    <BODY class="bandaBody" onload="javascript:{  pleaseWait('off');
       }" >
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        
<form method="post">
    <input  type="hidden"  name="opcion" id="opcion">
    <input  type="hidden"  name="idOrganizacionExterna" id="idOrganizacionExterna">
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titMantOrgExt")%></div>
    <div class="contenidoPantalla">
        <table>
            <tr> 
                <td> 
                    <div id="tabla"></div>
                </td>
            </tr>
            <tr> 
                <td> 
                    <input type="text" class="inputTextoObligatorio" id="obligatorio"  name="codOrganizacionExterna" maxlength="2" 
                           style="width: 13%" onkeyup="return SoloDigitosNumericos(this);">
                    <input type="text" class="inputTextoObligatorio" id="obligatorio" name="desOrganizacionExterna" maxlength="100" 
                           style="width: 86%" onblur="return xAMayusculas(this);">
                </td>
            </tr>
        </table>								
        <div class="botoneraPrincipal">
            <input accesskey="A" type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbAlta")%>' name="cmdAlta" onClick="pulsarAlta();" >
            <input accesskey="M" type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbModificar")%>' name="cmdModificar" onClick="pulsarModificar();">
            <input accesskey="E" type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbEliminar")%>' name="cmdEliminar" onClick="pulsarEliminar();">
            <input accesskey="L" type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbLimpiar")%>' name="cmdLimpiar" onClick="limpiarInputs();">
            <input accesskey="S" type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbSalir")%>' name="cmdSalir" onClick="pulsarSalir();">
        </div>  
    </div>  
</form>
        
        <script type="text/javascript">
   
              var tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
            
            tab.addColumna('120','center','<%=descriptor.getDescripcion("gEtiq_codigo")%>');
                tab.addColumna('780','center','<%=descriptor.getDescripcion("gEtiq_desc")%>');
                    tab.displayCabecera=true;   
                    
                    function cargaTabla(){
                        var cont = 0;
                        lista = new Array();
                        <logic:iterate id="elemento" name="MantRegistroExternoForm" property="listaOrganizacionesExternas">
                        lista[cont] = ['<bean:write name="elemento" property="codigo" />', '<bean:write name="elemento" property="descripcion"/>'];
                            cont = cont + 1;
                            </logic:iterate>
                            tab.lineas=lista;
                            refresh();
                        }
                        
                        function refresh(){
                            tab.displayTabla();
                        }
                        
        </script>
        
        <script> Inicio(); </script>
        
    </BODY>
    
</html>

