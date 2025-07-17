<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<html>
    <head>
        <title>Búsqueda</title>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
        <%
            int idioma = 1;
            String opcion = "";
            String css = "";
            int codUsu=-1;
            if (session != null) {
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    css = usuario.getCss();
                    codUsu = usuario.getIdUsuario();
                }
                opcion = (String) session.getAttribute("tipoAnotacion");
            }
            if (opcion == null) {
                opcion = "E";
            }

        %>
        
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="1" />
        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <link rel="stylesheet" href="<%=request.getContextPath()%><%=css%>" type="text/css">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        
        
<script type="text/javascript">
            
        var lista= new Array();
        var anho = new Array();
        var num = new Array();
        var data = new Array();
        var usuario = new Array();
        var ultimo = false;
        var cont = 0;
        var fec = "";
        var ejeNum = "";

        var controlUsuario=<bean:write name="ReservaOrdenForm" property="reservasPorUsuario" />;

        function cargaTabla() {
            window.focus();

            <logic:iterate id="elemento" name="ReservaOrdenForm" property="codigos">
                lista[cont]= ['<bean:write name="elemento" property="dia" />' + "/" + '<bean:write name="elemento" property="mes" />' +
                        "/" + '<bean:write name="elemento" property="ano" />' + " " + '<bean:write name="elemento" property="hora" />' +
                        ":" + '<bean:write name="elemento" property="min" />', '<bean:write name="elemento" property="ejercicio"/>' + "/" + '<bean:write name="elemento" property="txtNumRegistrado"/>' ,'<bean:write name="elemento" property="nombreUsuario"/>'];
                anho[cont] = ['<bean:write name="elemento" property="ejercicio"/>'];
                num[cont] = ['<bean:write name="elemento" property="txtNumRegistrado"/>'];
                usuario[cont]=['<bean:write name="elemento" property="usuario"/>'];
                cont = cont + 1;
            </logic:iterate>           
            tab.lineas=lista;
            tab.displayTabla();
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

            var ejecutaAccion=true;
            if(controlUsuario){
                ejecutaAccion=compruebaUsuario(i);
            }

            if(ejecutaAccion)
            {
               document.forms[0].modificar.value = "1";
                document.forms[0].reservas.value = "1";
                document.forms[0].ano.value=anho[i];
                document.forms[0].numero.value=num[i];
                document.forms[0].target='mainFrame';
                document.forms[0].opcion.value="buscar";
                document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
                document.forms[0].submit();
            }else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoUsuarioRes")%>');
            
        }

        function compruebaUsuario(i)
        {
            if ((usuario[i]==<%=codUsu%>) ||(usuario[i]==0))
            {
                return true;
            }
            else return false;
        }

        function pulsarAnular() {
            k = tab.selectedIndex;
            if (k == -1) {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjEscogerRes")%>');
            } else {
                
                var ejecutaAccion=true;
                if(controlUsuario){
                    ejecutaAccion=compruebaUsuario(k);
                }
                if(ejecutaAccion)
                {
                    var numero = tab.lineas[k][1];
                    var mensaje = '<%=descriptor.getDescripcion("msjAnularRes")%>' + ' ' + numero + ' ' +
                                  '<%=descriptor.getDescripcion("msjContinuar")%>';

                    // Confirmar anulacion reserva
                    if (jsp_alerta('', mensaje)) {
                        var source = "<html:rewrite page='/jsp/registro/anulacionReserva.jsp?dummy='/>";
                        abrirXanelaAuxiliar("<html:rewrite page='/jsp/registro/mainVentana.jsp?source='/>" + source,
                            '<%=descriptor.getDescripcion("etiqReserva")%>' + ' ' + numero,
                            'width=760,height=500',function(datos){
                                    if (datos!=undefined && datos['aceptar']) {
                                        document.forms[0].diligencia.value = datos['diligencia'];
                                        document.forms[0].ejercicio.value=anho[k];
                                        document.forms[0].txtNumRegistrado.value=num[k];
                                        document.forms[0].fechaTxt.value=tab.lineas[k][0];
                                        document.forms[0].opcion.value='anular';
                                        document.forms[0].target='oculto';
                                        document.forms[0].action="<html:rewrite page='/RelacionReserva.do'/>";
                                        document.forms[0].submit();
                                    }
                            });
                    }

                }//ejecutaAccion
                else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoUsuarioRes")%>');
            }
        }
            
        function pulsarSalir() {
            document.formulario.opcion.value = "salir" + "<%=opcion%>";
            document.formulario.target ="mainFrame";
            document.formulario.action = "<html:rewrite page='/ReservaOrden.do'/>";
            document.formulario.submit();
        }

        function checkKeysLocal(evento,tecla) {
            var teclaAuxiliar = "";
            if(window.event){
                evento = window.event;
                teclaAuxiliar = evento.keyCode;
            }else
                teclaAuxiliar = evento.which;

            if('Alt+S'==tecla) pulsarSalir();

            if (teclaAuxiliar == 38 || teclaAuxiliar == 40) upDownTable(tab,lista,teclaAuxiliar);
            if(teclaAuxiliar == 13){
                if(tab.selectedIndex>-1 && !tab.ultimoTable) callFromTableTo(tab.selectedIndex,tab.id);
            }
            keyDel(evento);
        }
        
        // Funciones del frame oculto
        function falloAnular() {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjFalloAnularRes")%>');
        }
        
        function anularCorrecto() {
            tab.lineas=lista;
            tab.displayTabla();
            jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjOkAnularRes"))%>');
        }
                        
</script>
        
    </head>
    
    <body class="bandaBody" onload="javascript:{ }">
        
        <form name="formulario" METHOD=POST target="_self">
            
            <html:hidden  property="opcion" value=""/>
            <html:hidden property="modificar" value="0"/>
            <html:hidden property="ano" value="0"/>
            <html:hidden property="numero" value="0"/>
            <html:hidden property="reservas" value="0"/>
            <html:hidden property="diligencia" value=""/>
            <html:hidden property="ejercicio" value=""/>
            <html:hidden property="txtNumRegistrado" value=""/>
            <html:hidden property="fechaTxt" value =""/>
            <html:hidden property="reservasPorUsuario" value =""/>
            
    <%if ("S".equals(opcion)) {%>
    <div id="titulo" class="txttitblancoder"><%=descriptor.getDescripcion("rel_titulo")%>&nbsp;&nbsp;&nbsp;</div>
    <% } else {%>
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("rel_titulo")%></div>
    <% }%>	
    <div class="contenidoPantalla">
        <table>
            <tr>	
                <td id="tabla" ></td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbAnular") %>' 
            name='<%=descriptor.getDescripcion("gbAnular") %>' onClick="pulsarAnular()" 
            alt="<%=descriptor.getDescripcion("toolTip_bAnularRes")%>" title="<%=descriptor.getDescripcion("toolTip_bAnularRes")%>">
            <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbSalir") %>' 
            name='<%=descriptor.getDescripcion("gbSalir") %>' onClick="pulsarSalir()" 
            alt="<%=descriptor.getDescripcion("toolTip_bVolver")%>" title="<%=descriptor.getDescripcion("toolTip_bVolver")%>">
        </div>
        <div id="enlace">
        </div>
    </div>
</form>
        
       <script type="text/javascript">
            var tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tabla"));

            var tamCol='450';
            var tamColUsu='0';
            if(controlUsuario==true){
              tamCol = '350';
              tamColUsu='200';
            }
            tab.addColumna(tamCol,'center','<%=descriptor.getDescripcion("gEtiqFecha")%>');
            tab.addColumna(tamCol,'center','<%=descriptor.getDescripcion("rer_ejercicio")%>');
            tab.addColumna(tamColUsu,'center','<%=descriptor.getDescripcion("rer_usuarioReserva")%>');
            tab.displayCabecera=true;   
                    
        </script>
        
        <script>
            cargaTabla(); // Inicializaciones.
        </script>
        
    </body>
</html>
