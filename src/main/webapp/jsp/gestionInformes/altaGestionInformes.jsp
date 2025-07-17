<%@ page import="java.util.Vector" %>
<%@ page import="es.altia.agora.interfaces.user.web.gestionInformes.GestionInformesForm" %>
<%@ page import="es.altia.agora.business.util.ElementoListaValueObject" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%
    response.setHeader("Cache-control","no-cache");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader ("Expires", 0);

    int idioma = 1;
    int apl = 1;
    if (session != null) {
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        if (usuario != null) {
            idioma = usuario.getIdioma();
            apl = usuario.getAppCod();
        }
    }
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<script type="text/javascript">
    // loop through all links of inside finder
    function pulsarNuevaVersion() {
        lista = document.getElementById('dragList1');
        for (i=0; i<lista.childNodes.length; i++) {
            node = lista.childNodes[i];
            if (node.nodeName=="LI") {
                alert("VALUE" + node.text);
            }
        }
    }
    function pulsarAlta() {
        lista1 = document.getElementById('dragList1');
        for (i=0; i<lista1.childNodes.length; i++) {
            node = lista1.childNodes[i];
            lista1.removeChild(node);
        }
        lista2 = document.getElementById('dragList2');
        for (i=0; i<lista2.childNodes.length; i++) {
            node = lista2.childNodes[i];
            lista2.removeChild(node);
        }
        elemento = document.createElement("li");
        elemento.id = lista1.id;
        elemento.appendChild(document.createTextNode("NODO"));
//        elemento.appendChild(document.createTextNode(lista.firstChild.nodeValue));
        lista1.appendChild(elemento);

//            elemento.id = lista.id;
//            elemento.value = 1;
//            elemento.appendChild(document.createTextNode(lista.firstChild.nodeValue));
//            lista.appendChild(elemento);
//            lista.removeChild(node);
    }

</script>

<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/dojo/dojo.js"></script>
<script type="text/javascript">
    dojo.require("dojo.widget.TabContainer");
    dojo.require("dojo.widget.ContentPane");
    dojo.require("dojo.widget.LayoutContainer");
    dojo.require("dojo.widget.ComboBox");
    dojo.require("dojo.widget.DropdownDatePicker");
    dojo.require("dojo.io.*");
    dojo.require("dojo.event.*");
    dojo.require("dojo.html.*");
    dojo.require("dojo.dnd.*");

    //Recojo el form de memoria
    <%GestionInformesForm gestionInformesForm = (GestionInformesForm)session.getAttribute("GestionInformesForm");
    Vector listaProcedimientos = gestionInformesForm.getListaProcedimientos();%>

    // Event handler when a user hovers a mouse over
    function actualizaCampos(key) {
        var params = new Array();
        params['codProcedimiento'] = key;
        // Perform remote operation using JSON as data format
        // that will be returned from the server
        var bindArgs = {
            url: DOMAIN_NAME+"<c:url value='/gestionInformes/FichaInforme.do?opcion=cargarListaCampos'/>",
            error: function(type, data, evt){alert("error");},
            mimetype: "text/json",
            content: params
        };
        var req = dojo.io.bind(bindArgs);

        // The "populateDiv" gets called as an event handler
        dojo.event.connect(req, "load", this, "populateDiv");
    }

    // Function call to populate the "bookInfo" div element that is
    // defined at the end of this file.
    function populateDiv(type, data, evt) {
        if (!data) {
            alert("Returned data FALSE");
        } else {
            //Eliminar elementos de las listas
            lista1 = document.getElementById('dragList1');
            while (lista1.childNodes.length>0) {
                lista1.removeChild(lista1.firstChild);
            }
            lista2 = document.getElementById('dragList2');
            while (lista2.childNodes.length>0) {
                lista2.removeChild(lista2.firstChild);
            }

            for (k=0; k< data.listaCamposCod.length; k++) {
                elemento = document.createElement("li");
                elemento.id = lista1.id;
                elemento.appendChild(document.createTextNode(data.listaCamposNom[k]));
        //        elemento.appendChild(document.createTextNode(lista.firstChild.nodeValue));
                lista1.appendChild(elemento);
            }
            // Campos
                var d1 = document.getElementById("dragList1");
                new dojo.dnd.HtmlDropTarget(d1, ["li1"]);
                var lis = d1.getElementsByTagName("li");
                for(var x=0; x<lis.length; x++){
                    new dojo.dnd.HtmlDragSource(lis[x], "li1");
                }

                // list two
                var d2 = document.getElementById("dragList2");
                new dojo.dnd.HtmlDropTarget(d2, ["li1"]);
                var lis2 = d2.getElementsByTagName("li");
                for(var x=0; x<lis2.length; x++){
                    new dojo.dnd.HtmlDragSource(lis2[x], "li1");
                }
        }
    }
</script>
<style type="text/css">
    div.contenidoTab {	/* IE only hack */
        font-family: Verdana, Arial, Helvetica, sans-serif;
        font-size: 11px;
        font-style:normal;
        font-weight: normal;
        font-variant: normal;
        color: #999999;
        border:3px solid #ccc;
        height: 100%;
        overflow-x:hidden;
        overflow-y: auto;
        padding:0.5em;
    }
    div.contenidoTab div.camposSeleccionables {
        float:left;
        width:"35%";
        border:0px solid red;
        height:110px;
        text-align:left;
    }
    div.contenidoTab div.camposSeleccionados {
        float:left;
        width:"35%";
        border:0px solid red;
        height:110px;
        text-align:left;
    }
</style>

<div class="fichaGestionInformes">
    <form  name="form" action="/gestionInformes/FichaInforme.do" target="_self">
        <input type="hidden" name="opcion" id="opcion" value="">
        <input type="hidden" name="codProcedimiento" id="codProcedimiento" value="">
        <input type="hidden" name="codMunicipio" id="codMunicipio" value="">
        <input type="hidden" name="codAplicacion" id="codAplicacion" value="">

        <div id="mainTabContainer" dojoType="TabContainer" style="width: 100%; height:80%" selectedTab="tab1">
            <div class="contenidoTab" id="tab1" dojoType="ContentPane" label="<%=descriptor.getDescripcion("gbInformeDatPr")%>">
                <span style="display:block;"><%=descriptor.getDescripcion("gbInformeNombre")%></span>
                <input type="text" name="nombre"
                    dojoType="ValidationTextBox"
                    trim="true"
                    ucfirst="true" class="inputTextoObligatorio"/>
                <br>
                <span style="display:block;"><%=descriptor.getDescripcion("gbInformeProc")%></span>

                <select class="inputTextoObligatorio" name="procedimiento" style="width: 300px;" id="procedimiento"
                        onchange="actualizaCampos(this.value);">
                    <%for (int i = 0; i < listaProcedimientos.size(); i++) {
                        ElementoListaValueObject elvo = (ElementoListaValueObject) listaProcedimientos.get(i);
                        String codProc = elvo.getCodigo();
                        String nomProc = elvo.getDescripcion();%>
                        <option value="<%=codProc%>"><%=nomProc%></option>
                    <%}%>
                </select>
                <br>
                <span style="display:block;"><%=descriptor.getDescripcion("gbInformeFecha")%></span>
                <div dojoType="dropdowndatepicker"></div>
                <br>
            </div>
            <div dojoType="ContentPane" label="Campos">
                <div class="contenidoTab" id="tab2" dojoType="ContentPane" label="<%=descriptor.getDescripcion("gbInformeCampos")%>">
                    <div class="camposSeleccionables" scrolling=auto>
                        <%=descriptor.getDescripcion("gbInformeCriDisp")%>
                        <ul id="dragList1" style="border:2px solid #eeeeee;height:200px;width:160px;background:#FFFFFF;">
                        </ul>
                    </div>
                    <div class="camposSeleccionados">
                        <%=descriptor.getDescripcion("gbInformeCriUso")%>
                        <ul id="dragList2" style="border:2px solid #eeeeee;height:200px;width:160px;background:#FFFFFF;">
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
