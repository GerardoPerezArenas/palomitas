<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config" %>
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
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");       
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/dojo/dojo.js"></script>
<script type="text/javascript">


    dojo.require("dojo.io.*");
    dojo.require("dojo.event.*");
    dojo.require("dojo.html.*");
    dojo.require("dojo.dnd.*");
	dojo.require("dojo.widget.FilteringTable");
	dojo.require("dojo.widget.Dialog");
    dojo.require("dojo.widget.Textbox");
	dojo.require("dojo.widget.Button");



    var paramsEntrada = new Array();
    var indexIn = 0;
    var j=0;

function cursor() {
    var valorParametro = dojo.widget.byId("ValorDefecto");
    valorParametro.textbox.focus();
}

function sigcerrar(e) {
    var keycode;
    if (window.event) keycode = window.event.keyCode;
    else if (e) keycode = e.which;
    else return true;

    if (keycode == 13) {
        cerrarDialog();
        return false;
    }
    else
        return true;
}
var valorParametro;

function sig() {
    valorParametro = dojo.widget.byId("ValorDefecto");
    valorParametro.textbox.value = "";
    valorParametro.textbox.focus();
}

function addEvent(elemento, nomevento, funcion, captura)
{
    if (elemento.attachEvent)
    {
        elemento.attachEvent('on' + nomevento, funcion);
        return true;
    }
    else
        if (elemento.addEventListener)
        {
            elemento.addEventListener(nomevento, funcion, captura);
            return true;
        }
        else
            return false;
}
function mostrarPrimero(tituloParam, esObligatorio) {
    valorParametro = dojo.widget.byId("ValorDefecto");

    var mensaje = dojo.widget.byId("TituloTextBox");
    mensaje.textbox.value = tituloParam;
    var dlg = dojo.widget.byId("DialogContent");
    dlg.show();

    <!--		alert(valorParametro.textbox);-->
    <!--		addEvent(valorParametro.textbox,-->
    <!--                 "KeyPress",-->
    <!--                 function() { sigcerrar(event);},false);     -->

}


function cerrarDialog() {

    var dlg = dojo.widget.byId("DialogContent");
    var mensaje = dojo.widget.byId("TituloTextBox");
    j++;
    if (j >= paramsEntrada.length) {
        paramsEntrada[j - 1][4] = valorParametro.textbox.value;
        previaEjecutarTest();
        dlg.hide();
    } else {
        if (valorParametro.textbox.value == "") {
            jsp_alerta('A', 'Parametro Obligatorio.Introduce un valor');
            mostrarPrimero(paramsEntrada[j - 1][1], true);
            j--;
        }
        else {
            paramsEntrada[j - 1][4] = valorParametro.textbox.value;
            while (j < paramsEntrada.length && !((paramsEntrada[j][3] == 1) && (paramsEntrada[j][2] == "true"))) j++;
            if (j >= paramsEntrada.length) {
                previaEjecutarTest();
                dlg.hide();
            }
            else {

                //alert(valorParametro);
                valorParametro.textbox.value = "";
                valorParametro.textbox.focus();
                mensaje.textbox.value = paramsEntrada[j][1];
            }
        }
    }
}
<logic:iterate id="param" name="TestOperacionSWForm" property="listaParamsIn">
paramsEntrada[indexIn] = ['<bean:write name="param" property="descParam"/>',
        '<bean:write name="param" property="tituloParam"/>',
        '<bean:write name="param" property="esObligatorio"/>',
        '<bean:write name="param" property="tipoParametro"/>',
        '<bean:write name="param" property="valorDefecto"/>'];
indexIn++;
</logic:iterate>

var paramsSalida = new Array();
var indexOut = 0;
<logic:iterate id="param" name="TestOperacionSWForm" property="listaParamsOut">
paramsSalida[indexOut] = ['<bean:write name="param" property="descParam"/>',
        '<bean:write name="param" property="tituloParam"/>',
        ''];
indexOut++;
</logic:iterate>

function pulsarTest() {
    j = 0;
    var encontrado = false;
    while (j < paramsEntrada.length && (!encontrado)) {
        if ((paramsEntrada[j][4] == "") && (paramsEntrada[j][2] == "true")) {
            encontrado = true;
            //Aqui encontramos al primero para abrir la ventana flotante
            mostrarPrimero(paramsEntrada[j][1], paramsEntrada[j][2]);
        }
        else {
            j++;
        }
    }
}

function previaEjecutarTest() {
    clearData("tablaParamsIn");
    var theJSONData = [];
    for (var j = 0; j < paramsEntrada.length; j++) {
        var o = {
            Id:paramsEntrada[j][0],
            Parametro:paramsEntrada[j][0],
            Titulo:paramsEntrada[j][1],
            Valor:paramsEntrada[j][4]
        };
        theJSONData.push(o);
    }
    var w = dojo.widget.byId("tablaParamsIn");
    w.store.setData(theJSONData);

    limpiarDatosSalida();

    document.forms[1].areaEstado.value = '';

    ejecutarTest();
}

function clearData(key) {
    dojo.widget.byId(key).store.clearData();
}

function limpiarDatosSalida() {
    var theJSONData = [];
    for (var j = 0; j < paramsSalida.length; j++) {
        var o = {
            Id:paramsSalida[j][0],
            Parametro:paramsSalida[j][0],
            Titulo:paramsSalida[j][1],
            Valor:''
        };
        theJSONData.push(o);
    }
    var w = dojo.widget.byId("tablaParamsOut");
    w.store.setData(theJSONData);
}
/*
function introducirValor(tituloParam, esObligatorio) {
    var ruta = DOMAIN_NAME+ "<c:url value='/integracionsw/introducirValor.jsp?titulo='/>" + tituloParam +
               '&obligatorio=' + esObligatorio+"'/>";
    
    abrirXanelaAuxiliar(ruta, 'ventana',
	'width=350,height=50,scrollbar=no,titlebar=no,status='+ '<%=statusBar%>',function(datos){});
}
*/
function ejecutarTest() {
    var strParams = '';
    for (var j = 0; j < paramsEntrada.length; j++) {
        strParams += paramsEntrada[j][0] + "|" + paramsEntrada[j][4] + "|";
    }
    strParams = strParams.substring(0, strParams.length - 1);

    var params = new Array();
    params["opcion"] = "ejecutarTest";
    params["codigoOpDef"] = "<bean:write name="TestOperacionSWForm" property="codOpDef"/>";
    params["cadenaParams"] = strParams;

    var bindArgs = {
        url: DOMAIN_NAME+"<c:url value='/integracionsw/TestOperacionSW.do'/>",
        error: function(type, data, evt) {
        },
        mimetype: "text/json",
        content: params
    };

    var req = dojo.io.bind(bindArgs);

    dojo.event.connect(req, "load", this, "reloadParamsSalida");
    pleaseWait("on", this);
}

function reloadParamsSalida(type, data, evt) {
    pleaseWait("off", this);
    if (data) {
        clearData("tablaParamsOut");
        var theJSONData = [];
        var results = data.jsonResultados;

        if (results) {
            for (var j = 0; j < paramsSalida.length; j++) {

                var o = {
                    Id:paramsSalida[j][0],
                    Parametro:paramsSalida[j][0],
                    Titulo:paramsSalida[j][1],
                    Valor:results[j]
                };
                theJSONData.push(o);

                paramsSalida[j] = [paramsSalida[j][0], paramsSalida[j][1], results[j]];
            }
            var w = dojo.widget.byId("tablaParamsOut");
            w.store.setData(theJSONData);
        }

        var estado = data.jsonEstado;
        document.forms[1].areaEstado.value = estado[0];

    }

}

</script>


<form name="form" action="/integracionsw/TestOperacionSW.do" target="_self">

    <input type="hidden" name="opcion" id="opcion" value="">
    <input type="hidden" name="codigoSW" id="codigoSW" value="">
    <input type="hidden" name="codOpDef" id="codOpDef" value="">

    <div class="cajonTexto" dojoType="dialog" id="DialogContent" bgColor="white" bgOpacity="0.5"
    	toggle="fade" toggleDuration="250" dojoAttachPoint="divNode" >
		<fieldset class="datos">
		<br><br>

		<input dojoType="textbox" class="inputTitulo" id="TituloTextBox">


        <input dojoType="textbox" class="inputTextoObligatorio" id="ValorDefecto" name="ValorDefecto"
        		style="width:230px;" dojoAttachPoint="dojoTextbox" value="" >
		<br><br><br>
		<input type= "button" class="botonAplicacion" value="Siguiente" id="botonSiguiente"
			onClick="cerrarDialog()" name="botonSiguiente" accesskey="&#13">
		<br><br>
		</fieldset>

    </div>

    <fieldset>
        <legend class="etiqSW">Identificaci&oacute;n</legend>
        <div style="float:left;margin-top:5px">
            <span class="etiqSW" style="float:left;">Servicio Web</span>
            <html:text property="tituloSW" styleClass="inputTextoObligatorio" readonly="true" size="85"
                       name="TestOperacionSWForm" style="float:left; width:400px;margin-left:40px"/>
        </div>
        <div style="float:left;margin-top:5px">
            <span class="etiqSW" style="float:left;">WDSL</span>
            <html:text property="wsdlSW" styleClass="inputTextoObligatorio" readonly="true" size="85"
                       name="TestOperacionSWForm" style="float:left; width:400px;margin-left:40px"/>
        </div>
        <div style="float:left;margin-top:5px">
            <span class="etiqSW" style="float:left;">T&iacute;tulo de operaci&oacute;n</span>
            <html:text property="tituloOpSW" styleClass="inputTextoObligatorio" readonly="true" size="85"
                       name="TestOperacionSWForm" style="float:left; width:400px;margin-left:40px"/>
        </div>
        <div style="float:left;margin-top:5px">
            <span class="etiqSW" style="float:left;">Operaci&oacute;n Servicio Web</span>
            <html:text property="nombreOpSW" styleClass="inputTextoObligatorio" readonly="true" size="85"
                       name="TestOperacionSWForm" style="float:left; width:400px;margin-left:40px"/>
        </div>
    </fieldset>
    <br><br>
    <fieldset>
        <legend class="etiqSW">Par&aacute;metros de entrada</legend>
        <div class="tablaPopUpTest">
            <table class="xTabla" dojoType="filteringTable" id="tablaParamsIn" multiple="false"
                   alternateRows="true" maxSortable="2"
                   cellpadding="0" cellspacing="0" border="0" style="width:100%">
                <thead>
                    <tr>
                        <th field="Parametro" dataType="String" width="237px">Parametro</th>
                        <th field="Titulo" dataType="String" width="237px">T&iacute;tulo</th>
                        <th field="Valor" dataType="String" width="238px">Valor</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${not empty TestOperacionSWForm.listaParamsIn}">
                        <logic:iterate id="param" name="TestOperacionSWForm" property="listaParamsIn">
                            <tr value="<bean:write name="param" property="descParam"/>">
                                <td>
                                    <bean:write name="param" property="descParam"/>
                                </td>
                                <td>
                                    <bean:write name="param" property="tituloParam"/>
                                </td>
                                <td>
                                    <bean:write name="param" property="valorDefecto"/>
                                </td>
                            </tr>
                        </logic:iterate>
                    </c:if>
                </tbody>
            </table>
        </div>
    </fieldset>
    <br><br>
    <fieldset>
        <legend class="etiqSW">Par&aacute;metros de salida</legend>
        <div class="tablaPopUpTest">
            <table class="xTabla" dojoType="filteringTable" id="tablaParamsOut" multiple="false"
                   alternateRows="true" maxSortable="2"
                   cellpadding="0" cellspacing="0" border="0" style="width:100%">
                <thead>
                    <tr>
                        <th field="Parametro" dataType="String" width="237px">Parametro</th>
                        <th field="Titulo" dataType="String" width="237px">T&iacute;tulo</th>
                        <th field="Valor" dataType="String" width="238px">Valor</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${not empty TestOperacionSWForm.listaParamsOut}">
                        <logic:iterate id="param" name="TestOperacionSWForm" property="listaParamsOut">
                            <tr value="<bean:write name="param" property="descParam"/>">
                                <td>
                                    <bean:write name="param" property="descParam"/>
                                </td>
                                <td>
                                    <bean:write name="param" property="tituloParam"/>
                                </td>
                                <td></td>
                            </tr>
                        </logic:iterate>
                    </c:if>
                </tbody>
            </table>
        </div>
    </fieldset>
    <br><br>
    <fieldset>
        <legend class="etiqSW">Estado</legend>
        <textarea class="textareaTexto" rows="5" cols="121" name="areaEstado" id="areaEstado" style="width:100%"></textarea>
    </fieldset>
</form>
<script>
    $(document).ready(function() {
        $(".xTabla").DataTable( {
            "sort" : false,
            "info" : false,
            "paginate" : false,
            "autoWidth": false,
            "language": {
                "search": "<%=descriptor.getDescripcion("buscar")%>",
                "zeroRecords": "<%=descriptor.getDescripcion("msgNoResultBusq")%>",
                "info": "<%=descriptor.getDescripcion("mosPagDePags")%>",
                "infoEmpty": "<%=descriptor.getDescripcion("noRegDisp")%>",
                "infoFiltered": "<%=descriptor.getDescripcion("filtrDeTotal")%>",
            }
        } );
    });
</script>