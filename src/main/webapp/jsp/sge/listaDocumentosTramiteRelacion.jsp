<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html:html>
    
    <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
        
        <TITLE>::: Lista de documentos de un tramite :::</TITLE>
        <jsp:include page="/jsp/plantillas/Metas.jsp" />
        <%
            int idioma = 1;
            int apl = 1;
            if (session != null) {
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    apl = usuario.getAppCod();
                }
            }
            Config m_Config = ConfigServiceHelper.getConfig("common");
        %>
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
        <script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
        
        <SCRIPT type="text/javascript">
            
            var datos = new Array();
            var datosOriginal = new Array();
            var cont = 0;
            
            function inicializar() {
                window.focus();
                
                <logic:iterate id="elemento" name="TramitacionExpedientesForm" property="listaDocumentosTramite">
                var rel = '<bean:write name="elemento" property="relacion"/>';
                var relac='';
                if (rel.trim() == 'S') {
                    relac = 'UNICO';
                } else {
                relac = 'EXPEDIENTE';
            }
            datos[cont] = ['<bean:write name="elemento" property="codigoDocumento"/>',
                '<bean:write name="elemento" property="descripcionDocumento"/>',
                '<bean:write name="elemento" property="interesado"/>',relac,
                '<bean:write name="elemento" property="editorTexto"/>'];
            datosOriginal[cont] = ['<bean:write name="elemento" property="codigoDocumento"/>',
                '<bean:write name="elemento" property="descripcionDocumento"/>',
                '<bean:write name="elemento" property="codPlantilla"/>',
                '<bean:write name="elemento" property="interesado"/>',
                relac,'<bean:write name="elemento" property="editorTexto"/>'];
            cont++;
            </logic:iterate>
            tab.lineas=datos;
            refresca();
            window.focus();
        }
        
        function pulsarAceptar() {
            if(tab.selectedIndex != -1) {
                i=tab.selectedIndex;
                var retorno = new Array();
                retorno = [datosOriginal[i][0],datosOriginal[i][2],datosOriginal[i][1],datosOriginal[i][3],datosOriginal[i][4],datosOriginal[i][5]];
                self.parent.opener.retornoXanelaAuxiliar(retorno);
            } else {
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
        }
        
        function pulsarAceptarDesdeTabla(i) {
            var retorno = new Array();
            retorno = [datosOriginal[i][0],datosOriginal[i][2],datosOriginal[i][1],datosOriginal[i][3],datosOriginal[i][4],datosOriginal[i][5]];
            self.parent.opener.retornoXanelaAuxiliar(retorno);
        }
        
        function callFromTableTo(rowID,tableName){
            if(tab.id == tableName){
                pulsarAceptarDesdeTabla(rowID);
            }
        }
        
        function pulsarCancelar() {
            self.parent.opener.retornoXanelaAuxiliar();
        }
        
        </SCRIPT>
        
    </head>
    
<BODY class="bandaBody" onload="javascript:{inicializar();}">

    <html:form action="/sge/TramitacionExpedientes.do" target="_self">
        <html:hidden  property="opcion" value=""/>

        <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("etiq_listaDocTram")%></div>
        <div class="contenidoPantalla">
            <div id="tabla" style="width: 100%"></div>
            <div class="botoneraPrincipal">                
                <input type="button" class="botonGeneral" accesskey="A" value='<%=descriptor.getDescripcion("gbAceptar")%>' name="cmdAceptar" onClick="pulsarAceptar();"/>
                <input type="button" class="botonGeneral" accesskey="C" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelar" onClick="pulsarCancelar();"/>
            </div>            
        </div>            
    </html:form>

    <script type="text/javascript">
        tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

        tab.addColumna('80','center','<%= descriptor.getDescripcion("gEtiq_codigo")%>');
        tab.addColumna('335','center','<%= descriptor.getDescripcion("gEtiq_nombre")%>');
        tab.addColumna('80','center','<%= descriptor.getDescripcion("gEtiq_interesado")%>');
        tab.addColumna('80','center','<%= descriptor.getDescripcion("gEtiqTipo")%>');
        tab.addColumna('70','center','<%= descriptor.getDescripcion("editorTexto")%>');
        tab.displayCabecera=true;  
        tab.displayTabla();

                        function refresca() {
                            tab.displayTabla();
                        }

                        document.onmousedown = checkKeys;

        function checkKeysLocal(evento,tecla) {
            var teclaAuxiliar = "";
            if(window.event){
                evento = window.event;
                teclaAuxiliar = evento.keyCode;
            }else
                teclaAuxiliar = evento.which;

            if (teclaAuxiliar == 38 || teclaAuxiliar == 40){
                upDownTable(tab,datos,teclaAuxiliar);
                            }

            if(teclaAuxiliar == 13){
                                if((tab.selectedIndex>-1)&&(tab.selectedIndex < datos.length)&&(!ultimo)){
                                    callFromTableTo(tab.selectedIndex,tab.id);
                                }
                            }
            keyDel(evento);
                        }

    </script>        
</BODY>
</html:html>
