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

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/dojo/dojo.js"></script>
<script type="text/javascript">

	dojo.require("dojo.widget.TabContainer");
	dojo.require("dojo.widget.ContentPane");
	dojo.require("dojo.widget.FilteringTable");

    
    function pulsarTest() {
        var ruta = '<%=request.getContextPath()%>/integracionsw/TestOperacionSW.do?opcion=cargaTest&codigoOpDef=' +
                           <bean:write name="MantenimientoOperacionSWForm" property="codDefOp"/>;
        abrirXanelaAuxiliar(ruta, 'ventana','width=800,height=700',function(datosConsulta){});
    }

    var paramsEntrada = new Array();
    var indexIn = 0;
    <logic:iterate id="param" name="MantenimientoOperacionSWForm" property="listaParamsIn">
            paramsEntrada[indexIn] = ['<bean:write name="param" property="descParam"/>',
                    '<bean:write name="param" property="tituloParam"/>',
                    '<bean:write name="param" property="esObligatorio"/>',
                    '<bean:write name="param" property="tipoParametro"/>',
                    '<bean:write name="param" property="valorDefecto"/>',
                    '<bean:write name="param" property="codigoParam"/>'];
            indexIn++;
    </logic:iterate>

    var paramsSalida = new Array();
    var indexOut = 0;
    <logic:iterate id="param" name="MantenimientoOperacionSWForm" property="listaParamsOut">
            paramsSalida[indexOut] = ['<bean:write name="param" property="descParam"/>',
                    '<bean:write name="param" property="tituloParam"/>',
                    '<bean:write name="param" property="tipoDato"/>',
                    '<bean:write name="param" property="valorCorrecto"/>'];
            indexOut++;
    </logic:iterate>

    function copiarDatosEntrada() {
        var w=dojo.widget.byId("tablaParamsIn");
        if (w) {
            var s=getValueEntrada(w);
            document.forms[1].descParamNew.value = s[0];
            document.forms[1].tituloParamNew.value = s[1];
            document.forms[1].obligParamNew.value = s[2];
            document.forms[1].tipoAsignNew.value = s[3];
            document.forms[1].valorDefectoNew.value = s[4];
            document.forms[1].codParam.value = s[5];
            document.forms[1].tituloParamNew.select();
        }
    }

    function getValueEntrada(w) {
        var data = w.store.get();
        for (var i = 0; i < data.length; i++) {
            if (data[i].isSelected) {
                var datos = paramsEntrada[i];
            }
        }
        return datos;
    }

    function modifyParamIn() {
        var params = new Array();
        params["opcion"] = "modificarParamIn";
        params["descParamNew"] = document.forms[1].descParamNew.value;
        params["tituloParamNew"] = document.forms[1].tituloParamNew.value;
        params["obligParamNew"] = document.forms[1].obligParamNew.value;
        params["tipoAsignNew"] = document.forms[1].tipoAsignNew.value;
        params["valorDefectoNew"] = document.forms[1].valorDefectoNew.value;
        params["codigoOpDef"] = <bean:write name="MantenimientoOperacionSWForm" property="codDefOp"/>
        params["codParam"] = document.forms[1].codParam.value;

        var bindArgs = {
            url: "<c:url value='/integracionsw/MantenimientoOperacionSW.do'/>",
            error: function(type, data, evt){},
            mimetype: "text/json",
            content: params
        };

        var req = dojo.io.bind(bindArgs);

        dojo.event.connect(req, "load", this, "reloadParamsIn");
    }

    function reloadParamsIn(type, data, evt) {
        if (data) {
            clearData("tablaParamsIn");
            var theJSONData=[];
            var cods=data.listaDescParamIn;
            var titulos=data.listaTituloParamIn;
            var oblgs=data.listaOblParamIn;
            var tipos=data.listaTipoParamIn;
            var valores=data.listaValorParamIn;
            var codParams = data.listaCodParamIn;
            for(var j=0; j<cods.length; j++){
                var strTitulo = titulos[j];
                if (strTitulo == null) strTitulo = '';
                var strOblig = oblgs[j];
                if (strOblig) strOblig = 'SI';
                else strOblig = 'NO';
                var strTipo = '';
                if (tipos[j] == '1') strTipo = 'VARIABLE';
                if (tipos[j] == '2') strTipo = 'CONSTANTE';
                if (tipos[j] == '3') strTipo = 'AÑO VARIABLE';
                var strValor = valores[j];
                if (strValor == null) strValor = '';
                var o ={
                    Id:cods[j],
                    Parametro:cods[j],
                    Titulo:strTitulo,
                    Ob:strOblig,
                    Asignacion:strTipo,
                    Valor:strValor
                };
                theJSONData.push(o);

                paramsEntrada[j] = [cods[j], strTitulo, oblgs[j], tipos[j], strValor, codParams[j]];
            }
            var w=dojo.widget.byId("tablaParamsIn");
            w.store.setData(theJSONData);
        }
    }

    function clearData(key){
        dojo.widget.byId(key).store.clearData();
    }

    function copiarDatosSalida() {
        var w=dojo.widget.byId("tablaParamsOut");
        if (w) {
            var s=getValueSalida(w);
            document.forms[1].descParamNewOut.value = s[0];
            document.forms[1].tituloParamNewOut.value = s[1];
            document.forms[1].tipoDatoNewOut.value = s[2];
            document.forms[1].valorCorrectoNewOut.value = s[3];
            document.forms[1].tituloParamNewOut.select();
        }
    }

    function getValueSalida(w) {
        var data = w.store.get();
        for (var i = 0; i < data.length; i++) {
            if (data[i].isSelected) {
                var datos = paramsSalida[i];
            }
        }
        return datos;
    }

    function modifyParamOut() {
        var params = new Array();
        params["opcion"] = "modificarParamOut";
        params["descParamNew"] = document.forms[1].descParamNewOut.value;
        params["tituloParamNew"] = document.forms[1].tituloParamNewOut.value;
        params["tipoDatoNew"] = document.forms[1].tipoDatoNewOut.value;
        params["valorCorrectoNew"] = document.forms[1].valorCorrectoNewOut.value;
        params["codigoOpDef"] = <bean:write name="MantenimientoOperacionSWForm" property="codDefOp"/>

        var bindArgs = {
            url: "<c:url value='/integracionsw/MantenimientoOperacionSW.do'/>",
            error: function(type, data, evt){},
            mimetype: "text/json",
            content: params
        };

        var req = dojo.io.bind(bindArgs);

        dojo.event.connect(req, "load", this, "reloadParamsOut");
    }

    function reloadParamsOut(type, data, evt) {
        if (data) {
            clearData("tablaParamsOut");
            var theJSONData=[];
            var cods=data.listaDescParamOut;
            var titulos=data.listaTituloParamOut;
            var tipos = data.listaTipoParamOut;
            var valores = data.listaValorParamOut;
            for(var j=0; j<cods.length; j++){
                var strTitulo = titulos[j];
                if (strTitulo == null) strTitulo = '';
                var strTipo = '';
                var valor = valores[j];
                if (valor == null) valor = '';
                if (tipos[j] == '0') strTipo = '<%=descriptor.getDescripcion("iswEtiqDatoSalidaM")%>';
                if (tipos[j] == '1') strTipo = '<%=descriptor.getDescripcion("iswEtiqComprobacionM")%>';
                if (tipos[j] == '2') strTipo = '<%=descriptor.getDescripcion("iswEtiqMensajeErrorM")%>';
                var o ={
                    Id:cods[j],
                    Parametro:cods[j],
                    Titulo:strTitulo,
                    Tipo:strTipo,
                    ValorCorrecto:valor
                };
                theJSONData.push(o);

                paramsSalida[j] = [cods[j], strTitulo, tipos[j], valor];
            }
            var w=dojo.widget.byId("tablaParamsOut");
            w.store.setData(theJSONData);
        }
    }

    function pulsarVolver() {
        document.forms[1].codigoSW.value = <bean:write name="MantenimientoOperacionSWForm" property="codigoSW"/>;
        document.forms[1].opcion.value = "iniciaCargaOperaciones";
        document.forms[1].action = "<c:url value='/integracionsw/ListadoOperacionesSW.do'/>";
        document.forms[1].submit();
    }

</script>

<style type="text/css">
    .dojoContentPane{
        height:375px;
    }

    .dojoTabPane{
        height:375px;
    }

</style>

<form name="form" action="/integracionsw/MantenimientoOperacionSW.do" target="_self">

    <input type="hidden" name="codParam" id="codParam" value="">
    <input type="hidden" name="codigoSW" id="codigoSW" value="">
    <input type="hidden" name="opcion" id="opcion" value="">
      <div class="filtroGestionInformes" style="height:95px;">
        <div class="filtroIntegracionSWCampos">
            <table border="0" cellspacing="0" cellpadding="1">
            <tr>
                <td width="30%">
                    <span class="etiq" style="width: 200px; text-align: left; font-weight: normal;"><b><%=descriptor.getDescripcion("iswEtiqSW")%></b></span>
                </td>
                <td>
                    <html:text property="tituloSW"  styleClass="inputTextoObligatorio" readonly="true" size="80" name="MantenimientoOperacionSWForm" style="float:left;"/>
                </td>
            </tr>   
            <tr>
                <td width="30%">
                    <span class="etiq" style="width: 200px; text-align: left; font-weight: normal "><b><%=descriptor.getDescripcion("iswEtiqTitOp")%></b></span>
                </td>
                <td>
                    <html:text property="tituloOp"  styleClass="inputTextoObligatorio" readonly="true" size="80" name="MantenimientoOperacionSWForm"/>
                </td>
            </tr>   
            <tr>
                <td width="30%">
                    <span class="etiq" style="width: 200px; text-align: left; font-weight: normal"><b><%=descriptor.getDescripcion("iswEtiqOpSW")%><b></span>
                </td>
                <td>
                    <html:text property="nombreOpSW"  styleClass="inputTextoObligatorio" readonly="true" size="80" name="MantenimientoOperacionSWForm"/>
                </td>
            </tr>    
            </table>         
        </div>
     </div>

    <div id="mainTabContainer" dojoType="TabContainer" style="width: 100%; height:80%" selectedTab="tab1">
        <div id="tab1" dojoType="ContentPane" label="<%=descriptor.getDescripcion("iswEtiqEntrada")%>" style="height:375px;">
        	<div>
                        <div class="tablaIntegracionSW" style="height:350px;">
                                <table class="xTabla" dojoType="filteringTable" id="tablaParamsIn" multiple="false" alternateRows="true" maxSortable="2"
                                       style="width:612px;" cellpadding="0" cellspacing="0" border="0" onDblclick="copiarDatosEntrada();">
                                    <thead>
                                        <tr>
                                            <th field="Parametro" dataType="String" width="214px"><%=descriptor.getDescripcion("iswEtiqParam")%></th>
                                            <th field="Titulo" dataType="String" width="143px"><%=descriptor.getDescripcion("iswEtiqTit")%></th>
                                            <th field="Ob" dataType="String" width="71px" align="center"><%=descriptor.getDescripcion("iswEtiqOb")%></th>
                                            <th field="Asignacion" dataType="String" width="142px" align="center"><%=descriptor.getDescripcion("iswEtiqAsig")%></th>
                                            <th field="Valor" dataType="String" width="142px"><%=descriptor.getDescripcion("iswEtiqValor")%></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                <c:if test="${not empty MantenimientoOperacionSWForm.listaParamsIn}">
                                    <logic:iterate id="param" name="MantenimientoOperacionSWForm" property="listaParamsIn">
                                        <tr value="<bean:write name="param" property="descParam"/>">
                                            <td><bean:write name="param" property="descParam"/></td>
                                            <td><bean:write name="param" property="tituloParam"/></td>
                                            <td align="center">
                                            	<logic:equal name="param" property="esObligatorio" value="true"><%=descriptor.getDescripcion("iswEtiqSi")%></logic:equal>
                                                <logic:equal name="param" property="esObligatorio" value="false"><%=descriptor.getDescripcion("iswEtiqNo")%></logic:equal>
                                            </td>
                                            <td align="center">
                                                <logic:equal name="param" property="tipoParametro" value="1">VARIABLE</logic:equal>
                                                <logic:equal name="param" property="tipoParametro" value="2">CONSTANTE</logic:equal>
                                                <logic:equal name="param" property="tipoParametro" value="3">AÑO VARIABLE</logic:equal>
                                            </td>
                                            <td><bean:write name="param" property="valorDefecto"/></td>
                                        </tr>
                                    </logic:iterate>
                                </c:if>
                            </tbody>
                        </table>
                    <div >
                        <input type="text" class="inputTexto" id="descParamNew" name="descParamNew" style="width:225px;" disabled="disabled">
                        <input type="text" class="inputTextoObligatorio" id="tituloParamNew" name="tituloParamNew" style="width:132px;">
                        <select id="obligParamNew" class="inputTextoObligatorio" style="width:77px">
		        			<option value="true"><%=descriptor.getDescripcion("iswEtiqSi")%></option>
		        			<option value="false"><%=descriptor.getDescripcion("iswEtiqNo")%></option>
		        		</select>
                        <select id="tipoAsignNew" class="inputTextoObligatorio" style="width:144px">
		        			<option value="1"><%=descriptor.getDescripcion("iswEtiqVariable")%></option>
		        			<option value="2"><%=descriptor.getDescripcion("iswEtiqConst")%></option>
		        			<option value="3"><%=descriptor.getDescripcion("iswEtiqAnovar")%></option>
		        		</select>
                        <input type="text" class="inputTextoObligatorio" id="valorDefectoNew" name="valorDefectoNew" style="width:83px;">
						<input type= "button" class="botonOperacion" value="<%=descriptor.getDescripcion("iswEtiqC")%>" name="botonAlta" onClick="modifyParamIn();" accesskey="C">
			        </div>
			    </div>

        	</div>
        </div>
        <div id="tab2" dojoType="ContentPane" label="<%=descriptor.getDescripcion("iswEtiqSalida")%>">
            <div>
                <div class="tablaIntegracionSW">
                        <table class="xTabla" dojoType="filteringTable" id="tablaParamsOut" multiple="false" alternateRows="true" maxSortable="2"
                               style="width:712px" cellpadding="0" cellspacing="0" border="0" onDblClick="copiarDatosSalida();">
                            <thead>
                                <tr>
                                    <th field="Parametro" dataType="String" width="285px"><%=descriptor.getDescripcion("iswEtiqParam")%></th>
                                    <th field="Titulo" dataType="String" width="143px"><%=descriptor.getDescripcion("iswEtiqTit")%></th>
                                    <th field="Tipo" dataType="String" width="120px" align="center"><%=descriptor.getDescripcion("iswEtiqTipoDato")%></th>
                                    <th field="ValorCorrecto" dataType="String" width="164px"><%=descriptor.getDescripcion("iswEtiqVCorrecto")%></th>
                                </tr>
			                </thead>
			                <tbody>
                                <c:if test="${not empty MantenimientoOperacionSWForm.listaParamsOut}">
                                    <logic:iterate id="param" name="MantenimientoOperacionSWForm" property="listaParamsOut">
                                        <tr value="<bean:write name="param" property="descParam"/>">
                                            <td width="285px"><bean:write name="param" property="descParam"/></td>
                                            <td width="143px"><bean:write name="param" property="tituloParam"/></td>
                                            <td align="center">
                                                <logic:equal name="param" property="tipoDato" value="0"><%=descriptor.getDescripcion("iswEtiqDatoSalidaM")%></logic:equal>
                                                <logic:equal name="param" property="tipoDato" value="1"><%=descriptor.getDescripcion("iswEtiqComprobacionM")%></logic:equal>
                                                <logic:equal name="param" property="tipoDato" value="2"><%=descriptor.getDescripcion("iswEtiqMensajeErrorM")%></logic:equal>
                                            </td>
                                            <td width="142px"><bean:write name="param" property="valorCorrecto"/></td>
                                        </tr>
                                    </logic:iterate>
                                </c:if>
                            </tbody>
                            </table>
                        <div>
                        <input type="text" class="inputTexto" id="descParamNewOut" name="descParamNewOut" style="width:285px;" disabled="disabled">
                        <input type="text" class="inputTextoObligatorio" id="tituloParamNewOut" name="tituloParamNewOut" style="width:145px;">
                        <select id="tipoDatoNewOut" class="inputTextoObligatorio" style="width:115px">
                            <option value="0"><%=descriptor.getDescripcion("iswEtiqDatoSalida")%></option>
                            <option value="1"><%=descriptor.getDescripcion("iswEtiqComprobacion")%></option>
                            <option value="2"><%=descriptor.getDescripcion("iswEtiqMensajeError")%></option>
                        </select>
                        <input type="text" class="inputTextoObligatorio" id="valorCorrectoNewOut" name="valorCorrectoNewOut" style="width:115px;">
                        <input type= "button" class="botonOperacion" value="C" name="botonAlta" onClick="modifyParamOut();" accesskey="C">
                    </div>
                </div>
            </div>        
        </div>
    </div>     
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
