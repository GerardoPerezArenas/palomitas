<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<HTML>
    <head>
        <%      int idioma = 1;
            String css = "";
            if (session != null) {
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    css = usuario.getCss();
                }

            }
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="1" />
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
        <TITLE>
            <%=descriptor.getDescripcion("res_tituloE1")%>
        </TITLE>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        
        <script type="text/javascript">
            
            var anho = new Array();
            var num = new Array();
            
            function pulsarSair() {
                
                document.forms[0].opcion.value = "salirE";
                document.forms[0].action = "<%=request.getContextPath()%>/ReservaOrden.do";
                document.forms[0].submit();
            }
            
            function inicializar() {
                window.focus();
                cargaTabla();
                document.all[tab.id].style.cursor = 'default';
                document.all[tab.id].style.marginTop = 0;//calcularPosTabla(document.all.tabla1);
            }
            
            function checkKeysLocal(evento,tecla) {
                var teclaAuxiliar;
                if(window.event){
                    evento = window.event;
                    teclaAuxiliar = evento.keyCode;
                }else
                    teclaAuxiliar = evento.which;

                if('Alt+S'==tecla) pulsarSair();
                
                //if (event.keyCode == 38 || event.keyCode == 40) upDownTable(tab,lista);
                if (teclaAuxiliar == 38 || teclaAuxiliar == 40) upDownTable(tab,lista,teclaAuxiliar);
            }
            
            function callFromTableTo(rowID,tableName){
                if(tab.id == tableName){
                    fec = lista[rowID][0];
                    ejeNum = lista[rowID][1];
                    enviar(rowID);
                    self.close();
                }
            }                  
            
            
            function enviar(i) {            
                document.forms[0].modificar.value = "1";
                document.forms[0].reservas.value = "1";
                document.forms[0].ano.value=anho[i];
                document.forms[0].numero.value=num[i];
                document.forms[0].opcion.value="buscar";
                document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";                      
                document.forms[0].submit();           
            }  
            
        </script>
        
    </head>
    
<body class="bandaBody" onload="javascript:{}">
<html:form action="/ReservaOrden.do">
<INPUT type="hidden" name="opcion">
<html:hidden property="modificar" value="0"/>
<html:hidden property="ano" value="0"/>
<html:hidden property="numero" value="0"/>
<html:hidden property="reservas" value="0"/>
<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("res_numeroE")%></div>
<div class="contenidoPantalla">
    <table>
        <tr> 
            <td style="width:60%; height:22px;" bgcolor="#DCDCCC" class="etiqueta"> &nbsp;<%=descriptor.getDescripcion("res_numero")%>&nbsp;<bean:write name="ReservaOrdenForm" property="fechaString"/>: 
            </td>
        </tr>
        <tr bgcolor="#e6e6e6">
            <td>
                <table align="center">
                    <tr>
                       <td id="tabla" width="100%" heigth="50%" bgcolor="#e6e6e6"></td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSair" onclick="pulsarSair();">
    </div>  
</div>  
</html:form>
<script language="JavaScript1.2">
            
            var lista= new Array();       
            var cont = 0;
            var tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
            tab.addColumna('225','center','<%=descriptor.getDescripcion("rer_ejercicio")%>');
                tab.displayCabecera=true;
                tab.displayTabla();
                function cargaTabla(){
                    var ejercicio='<bean:write name="ReservaOrdenForm" property="ejercicio"/>';                                 
                    <logic:iterate id="elemento" name="ReservaOrdenForm" property="numeros">
                    lista[cont]= [ejercicio + "/" +  '<bean:write name="elemento" property="txtNumRegistrado"/>'];
                    anho[cont] = ejercicio;                                
                    num[cont] = ['<bean:write name="elemento" property="txtNumRegistrado"/>'];
                    cont = cont + 1;
                    </logic:iterate>
                    
                    tab.lineas=lista;
                    refresh();
                }
                
                function pintaDatos(datos){
                    var selRow = eval("document.all." + tab.id + "_Row" + tab.selectedIndex);
                    selRow.bgColor = TB_Fondo;
                    document.all[tab.id].style.cursor = 'default';
                }
                
                //tab.displayDatos = pintaDatos;
                
                function refresh(){
                    tab.displayTabla();
                }
        </script>
    </CENTER>
    
    
        <SCRIPT>
            inicializar(); // Inicializaciones.
        </SCRIPT>
    </BODY>
    
</HTML>
