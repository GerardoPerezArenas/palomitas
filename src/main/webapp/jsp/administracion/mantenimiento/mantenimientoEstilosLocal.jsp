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
            var listaOrganizaciones = new Array();
            var datosOrganizaciones = new Array();
            var listaCss = new Array();
            var codCss = new Array();
            var descCss = new Array();
            
            
            
            
            // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
            function inicializar(){
                window.focus();
                cargaTablaOrganizaciones();
            }
            
            function cargaTablaOrganizaciones(){
                
             <%
            MantenimientosAdminForm bForm = (MantenimientosAdminForm) session.getAttribute("MantenimientosAdminForm");
            Vector listaOrganizaciones = bForm.getListaOrganizaciones();
            Vector listaCss = bForm.getListaCss();
            int lengthOrganizaciones = listaOrganizaciones.size();
            int lengthCss = listaCss.size();
            int i = 0;
             %>
                 //sacamos listado de estilos
                 
                 var z=0;
            <%for (i = 0; i < lengthCss; i++) {
                GeneralValueObject css = (GeneralValueObject) listaCss.get(i);%>
                    listaCss[z]=['<%=(String) css.getAtributo("codigo")%>','<%=(String) css.getAtributo("ruta")%>','<%=(String) css.getAtributo("descripcion")%>','<%=(String) css.getAtributo("general")%>'];
                        codCss[z]= listaCss[z][0];
                        descCss[z]= listaCss[z][2];
                        z++;
                        <%}%>
                        
                        var j=0;
                        var l=0;
                        
                        //sacamos listado de organizaciones
            <%for (i = 0; i < lengthOrganizaciones; i++) {
                GeneralValueObject organizaciones = (GeneralValueObject) listaOrganizaciones.get(i);%>
                    listaOrganizaciones[j] = ['<%=(String) organizaciones.getAtributo("codigo")%>', '<%=(String) organizaciones.getAtributo("descripcion")%>', '<%=(String) organizaciones.getAtributo("icono")%>', '<%=(String) organizaciones.getAtributo("css")%>'];
                        //meto la descripcion del estilo en la columna de la tabla
                        for(l=0; l < listaCss.length; l++){   
                            if(listaCss[l][0]==listaOrganizaciones[j][3]){
                                datosOrganizaciones[j]=[listaOrganizaciones[j][1],listaCss[l][2]];
                            }
                        }
                        lista[j] = datosOrganizaciones[j];
                        j++;
                        <%}%>
                        tablaOrganizaciones.setLineas(lista);
                        comboCss.addItems(codCss, descCss);
                        refresca(tablaOrganizaciones);
                     
                        
                    }       
                    
                    
                    function refresca(tabla){
                        tabla.displayTabla();
                    }
                    
                    function filaSeleccionada(tabla){
                        var i = tabla.selectedIndex;
                        if((i>=0)&&(!tabla.ultimoTable))
                            return true;
                        return false;
                    }
                    function buscarCodigoOrg(descOrg){
                        var i;
                        var codOrg;
                        for(i=0;i<listaOrganizaciones.length;i++){
                            if(listaOrganizaciones[i][1]==descOrg){
                                codOrg=listaOrganizaciones[i][0];
                            }
                        }
                        return codOrg;
                    }
                    
                    function pulsarSeleccionar(){
                        if(filaSeleccionada(tablaOrganizaciones)){	
                            var descOrg= document.forms[0].descOrg.value;
                            var cod=buscarCodigoOrg(descOrg);
                            
                            document.forms[0].codOrg.value = cod;
                            document.forms[0].opcion.value = 'modificarOrgCss';
                            document.forms[0].target = "oculto";
                            document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Estilos.do';
                            document.forms[0].submit();
                            pulsarLimpiarOrg(); 
                        } else{
                        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                        }
                        
                        
                    }
                    
                    function recuperaDatos(lista2,lista3) {
                        //limpiarCamposRejilla();
                        lista = lista2;
                        listaCss=lista3;
                        // Tabla con columnas ordenables.
                        //tablaOrganizaciones.lineas=lista;
                        tablaOrganizaciones.setLineas(lista);
                        refresca(tablaOrganizaciones);
                    }
                    
                    function pulsarLimpiarOrg(){
                        comboCss.selectItem(-1);
                        document.forms[0].descOrg.value='';
                        
                    }
                    function limpiarCss(){
                        document.forms[0].codiCss.value = '';
                        document.forms[0].rutaCss.value = '';
                        document.forms[0].descrCss.value = '';
                        document.forms[0].activo.checked=false;
                    }
                   
                    function pulsarTest(){
                        if(filaSeleccionada(tablaOrganizaciones)){	
                            var css=document.forms[0].codCss.value;
                            window.open("<html:rewrite page='/administracion/mantenimiento/Estilos.do'/>?codCss="+css+"&opcion=prototipo","ventana1","width=1018, height=800, scrollbars=no, menubar=no, location=no, resizable=no");
   
                        } else{
                        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                        }
                         
                        
                    }
                    
                    //segunda parte de funciones
                
     // FUNCIONES DE PULSACION DE BOTONES
                    function pulsarSalir(){
                        
                        document.forms[0].target = "mainFrame";
                        document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
                        document.forms[0].submit();
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
    <input  type="hidden"  name="codOrg" value="">
    <input  type="hidden" name="act" value="">

    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gTit_Css")%></div>
    <div class="contenidoPantalla">
        <table>
            <tr> 
                <td id="tabla"></td>
            </tr>
            <tr>
                <td> 
                    <input name="descOrg" type="text" class="inputTexto"maxlength="80" style="width:58%" readOnly> 
                    <input type="text" name="codCss" id="codCss" class="inputTextoObligatorio" value="" style="width:5%">
                    <input type="text" name="descCss"  id="descCss" class="inputTextoObligatorio" style="width:29%" readonly="true" value="">
                    <A href="" id="anchorCss" name="anchorCss"> 
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonCss"
                             name="botonCss" style="cursor:hand;"></span>
                    </A>
                </td>
            </tr>
            <tr style="padding-top:5px;">
                <td align="right" style="width:100%;">
                      <input name="botonSeleccionarr" type="button"  class="botonGeneral" id="botonSeleccionar" 
                           value="<%=descriptor.getDescripcion("gbSeleccionar")%>"
                           onClick="pulsarSeleccionar();" accesskey="B">
                   <input name="botonTest" type="button" class="botonGeneral" id="botonTest"
                           value='<%=descriptor.getDescripcion("gEtiq_Test")%>'
                           onClick="pulsarTest();" accesskey="C">
                    <input name="botonLimpiar" type="button" class="botonGeneral" id="botonLimpiar"
                           value='<%=descriptor.getDescripcion("gbLimpiar")%>'
                           onClick="pulsarLimpiarOrg();" accesskey="C">
                </td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="botonSalir" onClick="pulsarSalir();" accesskey="S">  
        </div>                        
    </div>                        
</form>
        
<script type="text/javascript">  
            
            //Creamos tablas donde se cargan las listas
            var tablaOrganizaciones = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
            // Anadir tipo de dato a cada columna;
             tablaOrganizaciones.addColumna('505','left','<%= descriptor.getDescripcion("gEtiq_Organizacion")%>','String');
            tablaOrganizaciones.addColumna('370','left','<%= descriptor.getDescripcion("gEtiq_Estilo")%>','String');
            tablaOrganizaciones.displayCabecera=true;
            
            tablaOrganizaciones.displayTabla();
            var comboCss= new Combo("Css");
            
                function rellenarDatos(tableObject,rowID){
                    
                    if(tablaOrganizaciones == tableObject){
                        if(rowID>-1 && !tableObject.ultimoTable){
                            document.forms[0].descOrg.value = lista[rowID][0];
                            var i = rowID;
                            if((i>=0)&&!tableObject.ultimoTable){			
                                comboCss.buscaCodigo(listaOrganizaciones[i][3]);
                                document.forms[0].descCss.value=lista[i][1];
                            }
                        }
                    } 
                
            }

<%String Agent = request.getHeader("user-agent");%>

var coordx=0;
var coordy=0;


<%if(Agent.indexOf("MSIE")==-1) {%> //Que no sea IE
    window.addEventListener('mousemove', function(e) {
        coordx = e.clientX;
        coordy = e.clientY;
    }, true);
<%}%>
            document.onmouseup = checkKeys;

function checkKeysLocal(evento,tecla) {
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else{
        teclaAuxiliar = evento.which;
    }


	if (teclaAuxiliar == 1){
            if (comboCss.base.style.visibility == "visible" && isClickOutCombo(comboCss,coordx,coordy)) setTimeout('comboCss.ocultar()',20);
        }
        if (teclaAuxiliar == 9){
            comboCss.ocultar();
        }
}
            
            
            
            
            
        </script>
        
        
    </body>
</html>
