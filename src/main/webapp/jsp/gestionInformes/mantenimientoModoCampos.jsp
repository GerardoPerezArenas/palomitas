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
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript">

    dojo.require("dojo.widget.*");
    dojo.require("dojo.event.*");

    dojo.require("dojo.widget.SortableTable");
    dojo.require("dojo.widget.FilteringTable");
    dojo.require("dojo.widget.Dialog");

	
    var listaModoAmbitos = new Array();
    var cont=0;
    <logic:iterate id="elemento" name="MantenimientoInformesForm" property="listaModoAmbitos">
      listaModoAmbitos[cont] = ['<bean:write name="elemento" property="codigo" />', '<bean:write name="elemento" property="descripcion" />'];

      cont++;
    </logic:iterate>



 function copiarDatosSalida() {
	var w=dojo.widget.byId("parsedFromHtml");
	if (w) {
		var s=getValueSalida(w);
		 if(s != null) {
			 document.forms[1].codParamNew.value = s[0];
			document.forms[1].descripcionParamNew.value = s[1];
			
		} else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
	}
    }	
    
 function getValueSalida(w) {
        var data = w.store.get();
        for (var i = 0; i < data.length; i++) {
            if (data[i].isSelected) {
                var datos = listaModoAmbitos[i];
            }
        }
        return datos;
    }
    
function pulsarNuevo() {
	
   	var codigo=PonerCodigo();
	var params = new Array();
	params["codParamNew"] = codigo;
        params["descripcionParamNew"] = document.forms[1].descripcionParamNew.value;
        valido = Validar(params["descripcionParamNew"]);
        if(valido==0){
	        var bindArgs = {
	            url: DOMAIN_NAME + "<c:url value='/gestionInformes/MantenimientoInformes.do?opcion=altaModoAmbito'/>",
	            error: function(type, data, evt){},
	            mimetype: "text/json",
	            content: params
	        };
	
	        var req = dojo.io.bind(bindArgs);
		
	        dojo.event.connect(req, "load", this, "reloadParamsOut");
	}else{
		jsp_alerta("A","Este nombre ya exite no es válido");
		pulsarLimpiar();
	}
  	 
}

 function modifyParamOut() {
        var params = new Array();
      	 params["codParamNew"] = document.forms[1].codParamNew.value;
        params["descripcionParamNew"] = document.forms[1].descripcionParamNew.value;
 
	        var bindArgs = {
	            url: DOMAIN_NAME + "<c:url value='/gestionInformes/MantenimientoInformes.do?opcion=modificarModoAmbito'/>",
	            error: function(type, data, evt){},
	            mimetype: "text/json",
	            content: params
	        };
	        var req = dojo.io.bind(bindArgs);
	        dojo.event.connect(req, "load", this, "reloadParamsOut");

    }	

 function eliminarAmbito(Key) {

        var params = new Array();
        params['codParamNew'] = Key[0];
       
        var bindArgs = {
            url: DOMAIN_NAME + "<c:url value='/gestionInformes/MantenimientoInformes.do?opcion=eliminarModoAmbito'/>",
            error: function(type, data, evt){},
            mimetype: "text/json",
            content: params
        };

        var req = dojo.io.bind(bindArgs);
        dojo.event.connect(req, "load", this, "reloadParamsOut");   
}

function pulsarEliminar(){
        var w=dojo.widget.byId("parsedFromHtml");
        if (w) {
            var s=getValueSalida(w);
            if (s != null) {
           
                if(jsp_alerta("C",'<%=descriptor.getDescripcion("desElimSol")%>') ==1) {
                    eliminarAmbito(s);
                }
            } else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
        }

        var w=dojo.widget.byId("parsedFromHtml");
    }

 function reloadParamsOut(type, data, evt) {
	if (data) {
           var theJSONData=[];
            var codigo=data.codigo;
            var descripcion=data.descripcion;
            for(var j=0; j<codigo.length; j++){
                var o ={
                    Id:codigo[j],
                    Descripcion:descripcion[j]
                };
                theJSONData.push(o);
                listaModoAmbitos[j] = [codigo[j], descripcion[j]];     
            }
           pulsarLimpiar();
            var w=dojo.widget.byId("parsedFromHtml");
            w.store.setData(theJSONData);
        }
    }

//sacar el numero mayor de listaModoAmbitos para guardar en la bbdd como codigo
 function PonerCodigo(){
  var num;
  var cosa=0;
  	for(i=0;i<listaModoAmbitos.length;i++){
  		num=listaModoAmbitos[i][0];
  		num=parseInt(num);
  		if(num > cosa){
  			cosa=num;
  		}
  	}
  	cosa++;
  	return (cosa);
  }
  
//comprobar si el valor es valido(ninguno se puede repetir)
function Validar(nombre){
  var cadena;
  var valido=0;
  	for(i=0;i<listaModoAmbitos.length;i++){
  		cadena=listaModoAmbitos[i][1];
  		if(cadena==nombre){
  			valido=1;
  		}
  	}
  	return valido;
  }



  
function pulsarLimpiar(){
	document.forms[1].descripcionParamNew.value='';

}

</script>

<form name="form" action="/gestionInformes/MantenimientoInformes.do" target="_self">
 
    <input type="hidden" name="listaModoOrigen" value="">


    <!-- TABLA DE PROCEDIMIENTOS -->

    <input type="hidden" name="opcion" id="opcion" value="">
   
    <div class="tablaGestionAmbito">
        <table class="xTabla" dojoType="filteringTable" id="parsedFromHtml" multiple="false" alternateRows="true" maxSortable="2"
               onDblClick="copiarDatosSalida();">
            <thead>
                <tr>
                    <th field="Descripcion" dataType="String" width="90%" align="center">Descripcion</th>

                </tr>
            </thead>
            <tbody>
                <logic:iterate id="elemento" name="MantenimientoInformesForm" property="listaModoAmbitos" >
                    <tr value="<bean:write name="elemento" property="codigo"/>">
                        <td align="center"><bean:write name="elemento" property="descripcion"/></td>

                    </tr>
                </logic:iterate>

            </tbody>
        </table>
        <div id="parsedFromHtml2" align="left" style="padding:4px;width:705px;" >
            <input type="hidden" class="inputTexto" id="codParamNew" name="codParamNew" style="width:68px;" disabled="disabled">
            <input type="text" class="inputTextoObligatorio" id="descripcionParamNew" name="descripcionParamNew" style="width:698px;" onKeyPress="javascript:PasaAMayusculas(event);">
        </div>
    </div>
</form>


<!-- TABLA DE CRITERIOS -->
<script type="text/javascript">
    $(document).ready(function() {
        $(".xTabla").not(".dialogCombo .xTabla").DataTable( {
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