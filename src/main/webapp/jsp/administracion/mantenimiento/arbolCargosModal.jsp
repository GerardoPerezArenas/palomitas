<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm" %>
<%@ page import="es.altia.arboles.impl.ArbolImpl" %>
<%@ page import="java.util.Vector" %>
<html>
    <head>
        <title>Cargos</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
        <script type="text/javascript">var APP_CONTEXT_PATH="<%=request.getContextPath()%>";</script>
        
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 5;
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
            }
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
        <jsp:setProperty name="descriptor" property="idi_cod" value="<%=idioma%>"/>
        <jsp:setProperty name="descriptor" property="apl_cod" value="<%=apl%>"/>
        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/xtree.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/WebFXImgRadioButtonTreeItem.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/uor.js"></script>
        
        <%--<script type="text/javascript" src="C:\Programas\jsTrace-v1.3-drag\jsTrace.js"></script>
        <script type="text/javascript" src="C:\Programas\jsTrace-v1.3-drag\dom-drag.js"></script>--%>
        
        <script type="text/javascript">
            
            var uors = new Array();
            var uorcods = new Array();
            var icoUsuario = 'fa fa-folder-open';
            var icoUsuarioBaW ='fa fa-folder-open-o';
            var icoUsuarioNV = 'fa fa-folder-open';// no visible en registro y de alta
            var icoUsuarioNVBaW = 'fa fa-folder-open-o';// no visible en registro y de baja
            var treeUORs = new WebFXTree('Cargos');
            var camino = new Array();
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
            
            function encuentraNodo(childNodes, uor) {
                for (var i=0;i<childNodes.length;i++) {
                    if (nodo2CodVis(childNodes[i].text)==uor.uor_cod_vis) {
                        treeUORs.selectByPath(i, camino);
                        break;
                    }
                    if (uor.uor_cod_vis.indexOf(nodo2CodVis(childNodes[i].text))!=-1) {
                        camino[camino.length] = i;
                        encuentraNodo(childNodes[i].childNodes, uor);
                    }
                }
                camino = new Array();
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
                if(treeUORs.getSelected().icon == icoUsuario) {
                    estado = 'A';
                }
                else {
                    estado = 'B';
                }
                
                var desc = buscarUorPorCodVisibleEstado(uors, nodo2CodVis(treeUORs.getSelected().text), estado);
                
                // comprobaciones: q no se haya seleccionado la ra�z q el texto no sea vacio
                if (((treeUORs.getSelected().text == treeUORs.text))||(treeUORs.getSelected().text == '')) {
                    alert("Error interno del �rbol de cargos");
                    return;
                }
                
                // callback
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
                self.parent.opener.retornoXanelaAuxiliar(null);
            }
            
            
            // rellenar Array javascript con objetos Cargos y otro para indexar con los uor_cod
            <%
            MantAnotacionRegistroForm mantARForm;
            if (request.getAttribute("MantAnotacionRegistroForm") != null) {
                mantARForm = (MantAnotacionRegistroForm) request.getAttribute("MantAnotacionRegistroForm");
            } else {
                mantARForm = (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm");
            }

            Vector listaUORDTOs;
            ArbolImpl arbol;

            listaUORDTOs = (Vector) request.getAttribute("listaCargos");
            arbol = (ArbolImpl) request.getAttribute("arbolCargos");


            for (int j = 0; j < listaUORDTOs.size(); j++) {
                UORDTO dto = (UORDTO) listaUORDTOs.get(j);
            %>
                // array con los objetos tipo uor mapeados por el array de arriba
                uors[<%=j%>] = new Uor<%=dto.toJavascriptArgs()%>;
                    // array con los c�digos visibles
                    uorcods[<%=j%>] = '<%=dto.getUor_cod_vis()%>';
    <%
            }
    %>
        
    <%= arbol.toJavascript("treeUORs", "icoUsuario", "icoUsuarioBaW", "icoUsuarioNV", "icoUsuarioNVBaW",
                        "seleccion()", "webfx-tree-item-area")%>
                        
        </script>
        
    </head>
    
    <body class="bandaBody" onload="inicializar()">
        <div class="txttitblanco">Cargos</div>
        <div class="contenidoPantalla">						
            <form name="campos" id="campos" action="">
                <input type="hidden" name="opcion" id="opcion">
                <!-- informaci�n sobre el UOR seleccionado que no se muestra en pantalla -->
                <input type="hidden" name="codUOR" id="codUOR">
                <input type="hidden" name="codPad" id="codPad">
            
                <table class="subsubtitulo" style="width:100%">
                    <tr>
                        <td>														
                            <div class="webfx-tree-div" style="min-height:295px;width:100%;">
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
                </table>
            </form>                
            <div class="botoneraPrincipal">
                <form action="" name="botones" id="botones">
                    <input  type="hidden" name="opcion">
                        <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbAceptar")%>' name="cmdAceptar" id="cmdAceptar" onclick="pulsarAceptar();" accesskey="A">
                        <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelar" id="cmdCancelar" onclick="pulsarCancelar();" accesskey="C">
                </form>        
            </div>            
        </div>            
    </body>
</html>
