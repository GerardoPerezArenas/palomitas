<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<!--
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
-->
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm" %>
<%@ page import="es.altia.arboles.impl.ArbolImpl" %>
<%@ page import="java.util.Vector" %>
<%--
Muestra el árbol de Unidades Orgánicas y permite seleccionar uno de los nodos y devolverlo
a la pantalla que la creó
--%>
<%@ page contentType="text/html;charset=ISO_8859-1" language="java" %>
<html>
<head>
    <title>Unidades Orgánicas</title>
    <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 5;
            String css = "";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                css = usuarioVO.getCss();
            }
            String opcion = request.getParameter("opcion");
            // Variable que indica si se ha pulsado el botón de buscar
            String realizarBusqueda = (String)request.getAttribute("realizarBusqueda");
            // Variable que contiene el criterio de búsqueda
            String criterio = (String)request.getAttribute("criterio");
            String ordenUor = (String)request.getAttribute("ordenUor");
    %>    

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
    <script type="text/javascript">var APP_CONTEXT_PATH="<%=request.getContextPath()%>";</script>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>">


    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
    <jsp:setProperty name="descriptor" property="idi_cod" value="<%=idioma%>"/>
    <jsp:setProperty name="descriptor" property="apl_cod" value="<%=apl%>"/>
    
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/xtree.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/WebFXImgRadioButtonTreeItem.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/uor.js'/>"></script>    
    

    <script type="text/javascript">    
        // Variable que indica si se ha pulsado el botón de buscar
        var realizarBusqueda = "<%=realizarBusqueda%>";
        // Variable que contiene el criterio de búsqueda
        var criterio = '';
        <% if(criterio!=null){ %>
            criterio         = "<%=criterio%>";
        <%}%>
        // Variable que contiene el nombre del trámite
        var nombreTramite = "";
        var postNotificacionEnviada = false;
        var tipoAvance = "";
        
        var uors = new Array();
        var uorcods = new Array();
        var icoUsuario = 'fa fa-folder-open';
        var icoUsuarioBaW ='fa fa-folder-open-o';
        var icoUsuarioNV = 'fa fa-folder-open';// no visible en registro y de alta
        var icoUsuarioNVBaW = 'fa fa-folder-open-o';// no visible en registro y de baja
        var treeUORs = new WebFXTree('Unidades Organizativas');        
        var nodo;
        var enc=false;
        treeUORs.icon = icoUsuario;
        treeUORs.openIcon = icoUsuario;
        treeUORs.estilo = 'webfx-tree-item-area';
        treeUORs.action = 'javascript:{seleccion();}';
        var ultimosNodosBuscados = new Array();
        var uorsBusqueda = new Array();
        var codigosNodosBuscados = "";
        function inicializar() {    
            // #289948: Recuperamos los argumentos de la ventana
            var argVentana = self.parent.opener.xanelaAuxiliarArgs;
            nombreTramite = argVentana.nombreTramite;
            postNotificacionEnviada = argVentana.postNotificacionEnviada;
            tipoAvance = argVentana.tipoAvance;
            // #289948: La acción no se puede cancelar, se deshabilita el botón
            if(postNotificacionEnviada && tipoAvance==1)
                document.getElementById("cmdCancelar").disabled = true;
            
            if(criterio!=null && criterio!='')
            {
                // Cierra el árbol
                cerrarNodos(treeUORs.childNodes);
                
                // Se eliminan los estilos de los nodos que coincidían con el criterio en una busqueda anterior
                if(uorsBusqueda!=null && uorsBusqueda.length>0)
                    quitarEstilosUltimaBusqueda(uorsBusqueda);

                /** Se recuperan los códigos de uors visibles que cumplen el criterio de busqueda actual **/
                uorsBusqueda = new Array();
                for(var i=0;i<uors.length;i++)
                {
                    var descripcion = retirarTildes(uors[i].uor_nom);
                    var busqueda    = retirarTildes(criterio);

                    if(descripcion.indexOf(busqueda)!=-1)
                        uorsBusqueda.push(uorcods[i]);
                }

                if(uorsBusqueda.length>0)
                {
                    for(var i=0;i<uorsBusqueda.length;i++)
                    {
                        var codUOR = uorsBusqueda[i];

                        if (codUOR!=null && codUOR!='')
                        {
                            var childNodes = treeUORs.childNodes;
                            var uor = buscarUorPorCodVisible(uors, codUOR);
                            if (uor==null) return;
                            /** Se busca el nodo por codigo visible de la uor */
                            encuentraNodo(childNodes, uor);
                            /** Se cambia el estilo */
                            document.getElementById(nodo.id + '-anchor').className = 'selected';
                            // Se almacena el identificador del nodo recién buscado
                            ultimosNodosBuscados.push(nodo.id);
                            if(nodo!=null)
                            {
                                var buscarAncestros = true;
                                var padre = nodo.parentNode;

                                if(padre!=null){
                                    /** Se buscan los ancestros del nodo encontrado y se expanden hasta llegar al raíz */
                                    while(buscarAncestros)
                                    {
                                        padre.expand();
                                        if(padre.parentNode!=null && padre.parentNode.text.indexOf("Unidades")==-1)
                                            padre = padre.parentNode;
                                        else buscarAncestros = false;

                                    }// while
                                }//if
                                // Se cambia el estilo del nodo recien encontrado

                            }
                        }// if
                    }// for

                }else{
                    jsp_alerta('A', '<%=descriptor.getDescripcion("msgNoResultBusq")%>');
                }
            }
        }


        function actualizarEstilosUltimaBusqueda()
        {
            for(var x=0;x<ultimosNodosBuscados.length;x++){
                document.getElementById(ultimosNodosBuscados[x] + '-anchor').className = 'selected';
            }
        }
        
        function cerrarNodos(childNodes){
            var childNodes = treeUORs.childNodes;
            for(var j=0;j<childNodes.length;j++)
            {
                var elemento = childNodes[j];
                document.getElementById(elemento.id + '-anchor').className = '';
                childNodes[j].collapseAll();
            }// for
        }


        /**
         *Para los nodos que coincidian con el anterior estilo de busqueda se les cambia el estilo */
        function quitarEstilosUltimaBusqueda(uorsBusqueda)
        {
            for(var j=0;j<ultimosNodosBuscados.length;j++)
            {
                document.getElementById(ultimosNodosBuscados[j] + '-anchor').className = '';
            }

            ultimosNodosBuscados=new Array();
        }
       
        function retirarTildes(cadena){
            cadena = cadena.toUpperCase();
            cadena = cadena.replace("á","a").replace("é","e").replace("í","i").replace("ó","o").replace("ú","u");
            cadena = cadena.replace("Á","A").replace("É","E").replace("Í","I").replace("Ó","O").replace("Ú","U");
            cadena = cadena.replace("'","");
            return cadena;

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
              //  childNodes[i].expand();
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
        
            buscaArbol(uor, childNodes[i].childNodes);
        }
        
    }

}
function encuentraNodo(childNodes, uor) {
    buscaArbolRaices(uor,childNodes);    
    //nodo.select();
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
    // comprobaciones: q no se haya seleccionado la raíz, q el texto no sea vacio
    if (((treeUORs.getSelected().text == treeUORs.text))||(treeUORs.getSelected().text == '')) {
        alert("Error interno del árbol de UORs");
        return;
    }

    // callback
    /*
    var correcto = false;
    if(funcion != null) {
        correcto = funcion(desc);
        if(correcto == false) {
            return;
        }
    }*/
    
    self.parent.opener.retornoXanelaAuxiliar(desc.uor_cod);
}

function pulsarCancelar() {
self.parent.opener.retornoXanelaAuxiliar(null);
}

function pulsarNombre(){        
    document.forms[0].opcion.value = 'ordenarArbolUORNombre';
    document.forms[0].target       = "mainFrame";    
    document.forms[0].action       = "<%=request.getContextPath()%>/sge/TramitacionExpedientes.do";
    document.forms[0].submit();
}

function pulsarBuscar(){ 
    var elementos = document.getElementsByName("tDescripcion");    
    if(elementos!=null && elementos.length>0){
        var texto = elementos[0].value;
        if(texto!=null && texto!=""){
            document.forms[0].criterio.value = texto;
            criterio = document.forms[0].criterio.value;
            inicializar();           
        }
    }else alert("no hay elementos de busqueda");
}

function pulsarCodigo(){        
    document.forms[0].opcion.value= 'ordenarArbolUORCodigo';
    document.forms[0].target= "mainFrame";
    document.forms[0].action = "<%=request.getContextPath()%>/sge/TramitacionExpedientes.do";    
    document.forms[0].submit();
}       
            // rellenar Array javascript con objetos UORs y otro para indexar con los uor_cod
            <%
            Vector listaUORDTOs;
            ArbolImpl arbol;

            listaUORDTOs = (Vector)session.getAttribute("listaUors");
            arbol = (ArbolImpl)session.getAttribute("arbolUorsTramitadoras");
                       
            for (int j = 0; j < listaUORDTOs.size(); j++)
            {
                UORDTO dto = (UORDTO) listaUORDTOs.get(j);
            %>
                // array con los objetos tipo uor mapeados por el array de arriba
                uors[<%=j%>] = new Uor<%=dto.toJavascriptArgs()%>;

                // array con los códigos visibles
                uorcods[<%=j%>] = '<%=dto.getUor_cod_vis()%>';
    <%
            } // for
    %>    
    
<%
   if(realizarBusqueda==null)        
       out.print(arbol.toJavascript("treeUORs", "icoUsuario", "icoUsuarioBaW", "icoUsuarioNV", "icoUsuarioNVBaW","seleccion()", "webfx-tree-item-area"));
%>   

    var mensaje = '<%=descriptor.getDescripcion("msjUtrTramite")%>' + '&nbsp;&nbsp;' + nombreTramite;
    
    </script>
</head>

<body class="bandaBody" onload="inicializar()">
    
    <form name="campos" id="campos" action="/sge/TramitacionExpedientes.do" onsubmit="pulsarBuscar();return false;">
        <input type="hidden" name="opcion" id="opcion">
        <!-- información sobre el UOR seleccionado que no se muestra en pantalla -->
        <input type="hidden" name="codUOR" id="codUOR">
        <input type="hidden" name="codPad" id="codPad">
        <input type="hidden" name="criterio" id="criterio"/>

        <div class="txttitblanco"><%=descriptor.getDescripcion("titUtrTramite")%></div>
        <div class="contenidoPantalla">
            <table style="width:100%">
                <tr>
                    <td colspan="2">
                        <div id="mensaje" class="etiqueta" align="left"><script language="javascript">document.write(mensaje);</script></div>
                    </td>
                </tr>
                <tr>
                    <td class="etiqueta" colspan="2">
                        <%=descriptor.getDescripcion("etiq_BusqUor")%>:<br/>
                        <input type="text" name="tDescripcion" size="70" class="inputTexto" value="<%if(criterio!=null) out.print(criterio);%>"/>&nbsp;&nbsp;
                        <input type="button" class="boton" name="busquedaUor" value="Buscar" onclick="javascript:pulsarBuscar();"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2"><!-- Arbol Usuarios -->
                        <table cellpadding="4" class="subsubtitulo" style="height:100%;width:100%">
                            <tr>
                                <td>
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
                        </table>
                    </td>
                </tr>
                <tr>
                    <td class="etiqueta" align="center">
                        <%=descriptor.getDescripcion("etiqOrdenacionUOR")%>:
                    </td>
                    <% if (ordenUor!=null && ordenUor.equals("nombre")) {%>
                    <td class="etiqueta" align="left">
                        <input type="radio" name="ordenar" id="ordenar_codigo" value="A"   onclick="pulsarCodigo();" /><%=descriptor.getDescripcion("etiqOrdCodUOR")%>
                        <input type="radio" name="ordenar" id="ordenar_nombre" value="B"  checked="true" onclick="pulsarNombre();"/><%=descriptor.getDescripcion("etiqOrdNomUOR")%>
                    </td>
                    <%} else {%>
                    <td class="etiqueta" align="left">
                        <input type="radio" name="ordenar" id="ordenar_codigo" value="A" checked="true" onclick="pulsarCodigo();" /><%=descriptor.getDescripcion("etiqOrdCodUOR")%>
                        <input type="radio" name="ordenar" id="ordenar_nombre" value="B"  onclick="pulsarNombre();"/><%=descriptor.getDescripcion("etiqOrdNomUOR")%>
                    </td>
                    <%}%>
                </tr>
            </table>
        </div>
        <div class="capaFooter">
            <div class="botoneraPrincipal">
                <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbAceptar")%>' name="cmdAceptar" id="cmdAceptar" onclick="pulsarAceptar();" accesskey="A">
                <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelar" id="cmdCancelar" onclick="pulsarCancelar();" accesskey="C">
            </div>
        </div>
    </form>
</body>
</html>
