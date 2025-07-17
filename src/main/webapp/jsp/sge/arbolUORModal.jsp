<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.UsuariosGruposForm" %>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO" %>
<%@ page import="es.altia.arboles.impl.ArbolImpl" %>
<%@ page import="java.util.ArrayList" %>
<%--
Muestra el árbol de Unidades Orgánicas y permite seleccionar uno de los nodos y devolverlo
a la pantalla que la creó
--%>
<%@ page contentType="text/html;charset=ISO_8859-1" language="java" %>
<html>
<head>
        <title>Unidades Orgánicas</title>
    
    
    
    <%
            Log mLog = LogFactory.getLog(this.getClass().getName());
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 5;
            String css = "";
            boolean esAplicacionExpedientes = true;
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                 if(apl!=4) esAplicacionExpedientes = false;
                idioma = usuarioVO.getIdioma();
                css = usuarioVO.getCss();
            }
            String opcion = request.getParameter("opcion");
            mLog.debug("OPCION EN arbolUORModal = " + opcion );
            String organizacion = (String) request.getAttribute("codOrganizacion");
            String entidad = (String) request.getAttribute("codEntidad");
            
            String marcarCodigo="";
            if(request.getAttribute("marcarCodigo")!=null){
                marcarCodigo =(String) request.getAttribute("marcarCodigo");
            }
            mLog.debug("MARCARCODIGO  EN arbolUORModal = " + marcarCodigo );
           
            
    %>
          
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
    <script type="text/javascript">var APP_CONTEXT_PATH="<%=request.getContextPath()%>";</script>
    
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
    <jsp:setProperty name="descriptor" property="idi_cod" value="<%=idioma%>"/>
    <jsp:setProperty name="descriptor" property="apl_cod" value="<%=apl%>"/>
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/xtree.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/WebFXImgRadioButtonTreeItem.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/uor.js"></script>
    
    <script type="text/javascript">
        var uors = new Array();
        var uorcods = new Array();
        var icoUsuario = 'fa fa-folder-open';
        var icoUsuarioBaW ='fa fa-folder-open-o';
        var icoUsuarioNV = 'fa fa-folder-open color-nv';// no visible en registro y de alta
        var icoUsuarioNVBaW = 'fa fa-folder-open-o color-nv';// no visible en registro y de baja
        var treeUORs = new WebFXTree('Unidades Organizativas');
        var camino = new Array();
        var nodo;
        var enc=false;
        treeUORs.icon = icoUsuario;
        treeUORs.openIcon = icoUsuario;
        treeUORs.estilo = 'webfx-tree-item-area';
        treeUORs.action = 'javascript:{seleccion();}';
        
        function inicializar() {
            var argumentos = self.parent.opener.xanelaAuxiliarArgs;
            var codUOR = argumentos[0];
            argumentos[0] = null;
            if (codUOR!=null && codUOR!='') {
                var childNodes = treeUORs.childNodes;
                var uor = buscarUorPorCodVisible(uors, codUOR);
                if (uor==null) return;
                encuentraNodo(childNodes, uor);
            }
        }
        
        function buscaArbol(uor, childNodes) {
            for (var i=0;(i<childNodes.length)&&!enc;i++) {
                if (nodo2CodVis(childNodes[i].text)==uor.uor_cod_vis) {
                    nodo = childNodes[i];                
                    enc=true;  
                    break;
                }
                else if ((childNodes[i].childNodes==[])||(childNodes[i].childNodes=='')||(childNodes[i].childNodes==null)) {
                    continue;
                }else  {                
                    childNodes[i].expand();
                    buscaArbol(uor, childNodes[i].childNodes);            
                }
            }
        }
    
        function buscaArbolRaices(uor, childNodes) {
            for (var i=0;(i<childNodes.length)&&!enc;i++) {
                if (nodo2CodVis(childNodes[i].text)==uor.uor_cod_vis) {                             
                    nodo = childNodes[i];                
                    enc=true;  
                    break;
                }            
                else if ((childNodes[i].childNodes==[])||(childNodes[i].childNodes=='')||(childNodes[i].childNodes==null)) {
                    continue;
                }else  {                
                    childNodes[i].expand();
                    buscaArbol(uor, childNodes[i].childNodes);            
                }          
                if (!enc) childNodes[i].collapseAll();
            }
        }    
        
        function encuentraNodo(childNodes, uor) {
            buscaArbolRaices(uor,childNodes);
            nodo.select();
            enc = false;        
        }

        function seleccion() {}

        function pulsarAceptar() {        
            if(treeUORs.getSelected() == null) {
                return;
            }

            var argumentos = self.parent.opener.xanelaAuxiliarArgs;
            var funcion;
            if(argumentos[0] != null) {
                funcion = argumentos[0];
            }
    
            var estado;
            if((treeUORs.getSelected().icon == icoUsuario) || (treeUORs.getSelected().icon == icoUsuarioNV)) {
                estado = 'A';
            }
            else {
                estado = 'B';
            }

            var desc = buscarUorPorCodVisibleEstado(uors, nodo2CodVis(treeUORs.getSelected().text), estado);

            if (((treeUORs.getSelected().text == treeUORs.text))||(treeUORs.getSelected().text == '')) {
                            alert("Error interno del árbol de UORs");
                return;
            }
    
            var correcto = false;
            if(funcion != null) {
                correcto = funcion(desc);

                if(correcto == false) {
                    return;
                }
            }

            var resultado = new Array();
            resultado[0] = '';
            resultado[1] = '';
            resultado[2] = '';
            resultado[0] = nodo2CodVis(treeUORs.getSelected().text);
            var desc = buscarUorPorCodVisibleEstado(uors, nodo2CodVis(treeUORs.getSelected().text), estado);
            resultado[1] = desc.uor_nom;
            resultado[2] = desc.uor_cod;
            self.parent.opener.retornoXanelaAuxiliar(resultado);
        }

        function pulsarCancelar() {
            self.parent.opener.retornoXanelaAuxiliar();
        }
        function pulsarNombre(){
            <% if (!esAplicacionExpedientes){
                UsuariosGruposForm bForm =(UsuariosGruposForm)session.getAttribute("UsuariosGruposForm");
                bForm.getUsuariosGrupos().setCodOrganizacion(organizacion);
                bForm.getUsuariosGrupos().setCodEntidad(entidad);
            %>
                    document.forms[0].opcion.value= 'cargarNombre';
                    document.forms[0].target= "mainFrame"
                    document.forms[0].action = "<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
                    document.forms[0].submit();
            <%} else{ %>
                    document.forms[0].opcion.value= 'cargarNombre';
                    document.forms[0].target= "mainFrame";
                    document.forms[0].action = "<%=request.getContextPath()%>/sge/FichaExpediente.do";
                    document.forms[0].submit();
            <%}%>
                }

        function pulsarCodigo(){
            <% if (!esAplicacionExpedientes){
                UsuariosGruposForm bForm =(UsuariosGruposForm)session.getAttribute("UsuariosGruposForm");
                bForm.getUsuariosGrupos().setCodOrganizacion(organizacion);
                bForm.getUsuariosGrupos().setCodEntidad(entidad);
            %>
                    document.forms[0].opcion.value= 'modalUOR';
                    document.forms[0].target= "mainFrame"
                    document.forms[0].action = "<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
                    document.forms[0].submit();
            <%} else{ %>
                    document.forms[0].opcion.value= 'seleccionUOR';
                    document.forms[0].vengoPagina.value="true";
                    document.forms[0].target= "mainFrame";
                    document.forms[0].action = "<%=request.getContextPath()%>/sge/FichaExpediente.do";
                    document.forms[0].submit();
            <%}%>
        }
        
        function cambiarOrden(ordenPulsado){
            var opcion = "cargarNombre";
            
            <% if (!esAplicacionExpedientes){
                UsuariosGruposForm bForm =(UsuariosGruposForm)session.getAttribute("UsuariosGruposForm");
                bForm.getUsuariosGrupos().setCodOrganizacion(organizacion);
                bForm.getUsuariosGrupos().setCodEntidad(entidad);
            %>
                if(ordenPulsado=="Codigo") opcion = "modalUOR";
                document.forms[0].opcion.value= opcion;
                document.forms[0].target= "mainFrame"
                document.forms[0].action = "<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
                document.forms[0].submit();
            <%} else{ %>
                if(ordenPulsado=="Codigo") {
                    opcion = "seleccionUOR";
                    document.forms[0].vengoPagina.value="true";
                }
                document.forms[0].opcion.value= opcion;
                document.forms[0].target= "mainFrame";
                document.forms[0].action = "<%=request.getContextPath()%>/sge/FichaExpediente.do";
                document.forms[0].submit();
            <%}%>
        }
    
        function actualizarEstilosUltimaBusqueda(){

        }

    // rellenar Array javascript con objetos UORs y otro para indexar con los uor_cod
    <%
        ArrayList<UORDTO> listaUORDTOs;
        ArbolImpl arbol;

        listaUORDTOs = (ArrayList<UORDTO>) request.getAttribute("listaUORs");
        arbol = (ArbolImpl) request.getAttribute("arbolUORs");

        for (int j = 0; j < listaUORDTOs.size(); j++) {
            UORDTO dto = (UORDTO) listaUORDTOs.get(j);
    %>
            // array con los objetos tipo uor mapeados por el array de arriba
            uors[<%=j%>] = new Uor<%=dto.toJavascriptArgs()%>;
            // array con los códigos visibles
            uorcods[<%=j%>] = '<%=dto.getUor_cod_vis()%>';
    <%
        }
    %>
        
    <%= arbol.toJavascript("treeUORs", "icoUsuario", "icoUsuarioBaW", "icoUsuarioNV", "icoUsuarioNVBaW",
                        "seleccion()", "webfx-tree-item-area")%>
                        
    </script>
    
</head>

<body  onload="inicializar()">
<form name="campos" id="campos" action="">
    <input type="hidden" name="opcion" id="opcion">
    <input type="hidden" name="marcarCodigo" id="marcarCodigo">
        <!-- información sobre el UOR seleccionado que no se muestra en pantalla -->
    <input type="hidden" name="codUOR" id="codUOR">
    <input type="hidden" name="codPad" id="codPad">
    <!-- Necesito ter este atributo vengoPagina, para a ordenacion da arbore de uors,inicialmente
    cargase por codigo ou nombre, segun o valor dunha propiedad Registro.properties, pero logo
    se se indica que recarge por nome ou codigo dende a paxina, hai que pasar de esa propiedade-->
    <input type="hidden" name="vengoPagina" id="vengoPagina">

    <div class="txttitblanco">Unidades Org&aacute;nicas</div>
    <div class="contenidoPantalla">						
        <table style="width:100%">
            <tr>
                <td class="subsubtitulo"><!-- Arbol Usuarios -->
                    <div class="webfx-tree-div" style="min-height:295px;width:100%">
                        <script type="text/javascript">
                            treeUORs.icon = icoUsuario;
                            treeUORs.openIcon = icoUsuario;
                            treeUORs.estilo = 'webfx-tree-item-area';
                            treeUORs.action = 'javascript:{seleccion();}';
                            document.write(treeUORs);
                        </script>
                    </div>
                </td>
            </tr>
            <tr>
                <td class="etiqueta" align="center">
                    <%=descriptor.getDescripcion("etiqOrdenacionUOR")%>:                        
                </td>
                <td class="etiqueta" align="left">
                <% if (opcion.equals("cargarNombre")) {%>
                    <input type="radio" name="ordenar" id="ordenar_codigo" value="A"   onclick="pulsarCodigo();" /><%=descriptor.getDescripcion("etiqOrdCodUOR")%>
                    <input type="radio" name="ordenar" id="ordenar_nombre" value="B"  checked="true" onclick="pulsarNombre();"/><%=descriptor.getDescripcion("etiqOrdNomUOR")%>
                <%} else {
                      if ("false".equals(marcarCodigo.trim())){ 
                 %>     
                        <input type="radio" name="ordenar" id="ordenar_codigo" value="A"   onclick="pulsarCodigo();" /><%=descriptor.getDescripcion("etiqOrdCodUOR")%>
                        <input type="radio" name="ordenar" id="ordenar_nombre" value="B"  checked="true" onclick="pulsarNombre();"/><%=descriptor.getDescripcion("etiqOrdNomUOR")%>
                    <% } else{%>
                        <input type="radio" name="ordenar" id="ordenar_codigo" value="A" checked="true" onclick="pulsarCodigo();" /><%=descriptor.getDescripcion("etiqOrdCodUOR")%>
                        <input type="radio" name="ordenar" id="ordenar_nombre" value="B"  onclick="pulsarNombre();"/><%=descriptor.getDescripcion("etiqOrdNomUOR")%>
                <% }}%>                            
                </td>   
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbAceptar")%>' name="cmdAceptar" id="cmdAceptar" onclick="pulsarAceptar();" accesskey="A">
            <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelar" id="cmdCancelar" onclick="pulsarCancelar();" accesskey="C">
        </div>
    </div>
</form>
</body>
</html>
