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
    response.setHeader("Cache-control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

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
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
             type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor" property="idi_cod" value="<%= idioma%>"/>
<jsp:setProperty name="descriptor" property="apl_cod" value="<%= apl %>"/>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/dojo/dojo.js"></script>
<script type="text/javascript">

    dojo.require("dojo.widget.FilteringTable");
    dojo.require("dojo.io.*");

    var listaArrays = new Array();
    var index = 0;
    <logic:iterate id="param" name="MantenimientoOperacionSWForm" property="listaArrays">
            listaArrays[index] = ['<bean:write name="param" property="descParamArray"/>',
                    '<bean:write name="param" property="entradaSalida"/>',
                    '<bean:write name="param" property="numRepeticiones"/>'];
            index++;
    </logic:iterate>

    function getValue(w) {
        var datos;
        var data = w.store.get();
        for (var i = 0; i < data.length; i++) {
            if (data[i].isSelected) {
                datos = listaArrays[i];
            }
        }
        return datos;
    }
            
    function copiarDatos() {
        var w=dojo.widget.byId("tablaArrays");
        if (w) {
            var s=getValue(w);
            document.forms[1].descArray.value = s[0];
            if (s[2] != -1) document.forms[1].numRepsNew.value = s[2];
            else document.forms[1].numRepsNew.value = '';

            document.forms[1].numRepsNew.select();
        }
    }

    function modifyArray() {
        var params = new Array();
        params["opcion"] = "modificarArray";
        params["descParamArray"] = document.forms[1].descArray.value;
        params["numRepsNew"] = document.forms[1].numRepsNew.value;
        params["codigoOpDef"] = <bean:write name="MantenimientoOperacionSWForm" property="codDefOp"/>

        var bindArgs = {
            url: "<c:url value='/integracionsw/MantenimientoOperacionSW.do'/>",
            error: function(type, data, evt){},
            mimetype: "text/json",
            content: params
        };

        var req = dojo.io.bind(bindArgs);

        dojo.event.connect(req, "load", this, "reloadTablaArray");
    }

    function reloadTablaArray(type, data, evt) {
        if (data) {
            clearData("tablaArrays");
            var theJSONData=[];
            var descs=data.listaArrayDef;
            var entSal=data.listaArrayEntSal;
            var numReps=data.listaArrayNumReps;
            for(var j=0; j<descs.length; j++){
                var strEntSal;
                if (entSal[j] == 0) strEntSal = 'SALIDA';
                if (entSal[j] == 1) strEntSal = 'ENTRADA';
                var strNumReps = numReps[j];
                if (numReps[j] == -1) strNumReps = 'NO DEFINIDO'
                var o ={
                    Id:descs[j],
                    Array:descs[j],
                    EntSal:strEntSal,
                    NumEnt:strNumReps
                };
                theJSONData.push(o);

                listaArrays[j] = [descs[j], entSal[j], numReps[j]];
            }
            var w=dojo.widget.byId("tablaArrays");
            w.store.setData(theJSONData);
        }
    }

    function clearData(key){
        dojo.widget.byId(key).store.clearData();
    }

    // Funcion para avanzar a la pagina de definicion de parametros.
    function irAParametros() {
        document.forms[1].codigoOpDef.value = <bean:write name="MantenimientoOperacionSWForm" property="codDefOp"/>;
        document.forms[1].opcion.value = "cargaMantenimientoOp";
        document.forms[1].action = "<c:url value='/integracionsw/MantenimientoOperacionSW.do'/>";
        document.forms[1].submit();
    }

    function volverAOperaciones() {
        document.forms[1].codigoSW.value = <bean:write name="MantenimientoOperacionSWForm" property="codigoSW"/>;
        document.forms[1].opcion.value = "iniciaCargaOperaciones";
        document.forms[1].action = "<c:url value='/integracionsw/ListadoOperacionesSW.do'/>";
        document.forms[1].submit();
    }

</script>

<form name="form" action="/integracionsw/MantenimientoOperacionSW.do" target="_self">

    <input type="hidden" name="codigoSW" id="codigoSW" value="">
    <input type="hidden" name="codigoOpDef" id="codigoOpDef" value="">
    <input type="hidden" name="codParam" id="codParam" value="">
    <input type="hidden" name="opcion" id="opcion" value="">

    <div class="filtroGestionInformes">
        <div class="filtroIntegracionSWCampos">
            <div>
                <span class="etiq">Servicio Web</span>
                <html:text property="tituloSW" styleClass="inputTextoObligatorio" readonly="true" size="80"
                           name="MantenimientoOperacionSWForm"/>
                <span class="etiq">Titulo de operaci&oacuten</span>
                <html:text property="tituloOp" styleClass="inputTextoObligatorio" readonly="true" size="80"
                           name="MantenimientoOperacionSWForm"/>
                <span class="etiq">Operaci&oacuten Servicio Web</span>
                <html:text property="nombreOpSW" styleClass="inputTextoObligatorio" readonly="true" size="80"
                           name="MantenimientoOperacionSWForm"/>
            </div>
        </div>
    </div>
    <div class="tablaIntegracionSW">
        <table class="xTabla" dojoType="filteringTable" id="tablaArrays" multiple="false"
               alternateRows="true" maxSortable="2"
               cellpadding="0" cellspacing="0" border="0" onDblClick="copiarDatos();">
            <thead>
                <tr>
                    <th field="Array" dataType="String" width="60%">Array</th>
                    <th field="EntSal" dataType="String" width="20%" align="center">Entrada/Salida</th>
                    <th field="NumEnt" dataType="String" width="20%" align="center">Num. Instacias</th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${not empty MantenimientoOperacionSWForm.listaArrays}">
                    <logic:iterate id="param" name="MantenimientoOperacionSWForm" property="listaArrays">
                        <tr value="<bean:write name="param" property="descParamArray"/>">
                            <td><bean:write name="param" property="descParamArray"/></td>
                            <td align="center">
                                <logic:equal name="param" property="entradaSalida" value="1">ENTRADA</logic:equal>
                                <logic:equal name="param" property="entradaSalida" value="0">SALIDA</logic:equal>
                            </td>
                            <td align="center">
                                <logic:equal name="param" property="numRepeticiones" value="-1">NO DEFINIDO</logic:equal>
                                <logic:notEqual name="param" property="numRepeticiones" value="-1">
                                    <bean:write name="param" property="numRepeticiones"/>
                                </logic:notEqual>
                            </td>
                        </tr>
                    </logic:iterate>
                </c:if>
            </tbody>
        </table>
        <div>
            <span class="etiq" width="10%">Array</span>
            <input type="text" class="inputTexto" id="descArray" name="descArray" style="width:39%;" disabled="disabled">
            <span class="etiq">Num. Intancias</span>
            <input type="text" class="inputTextoObligatorio" id="numRepsNew" name="numRepsNew" style="width:19%;">
            <br/><br/>
            <input type= "button" class="botonAplicacion" value="Actualizar" name="botonConfirmar" onClick="modifyArray();" accesskey="C">
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
