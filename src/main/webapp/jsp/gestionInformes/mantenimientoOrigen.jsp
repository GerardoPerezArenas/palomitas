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



    

    var listaAmbitos = new Array();
    var cont=0;
    <logic:iterate id="elemento" name="MantenimientoInformesForm" property="listaAmbitos">
      listaAmbitos[cont] = ['<bean:write name="elemento" property="codigo" />', '<bean:write name="elemento" property="descripcion" />', '<bean:write name="elemento" property="tab" />', '<bean:write name="elemento" property="modo" />'];
      
      cont++;
     
    </logic:iterate>
    
     var listaModoAmbitos = new Array();
    var cont=0;
    <logic:iterate id="elemento" name="MantenimientoInformesForm" property="listaModoAmbitos">
      listaModoAmbitos[cont] = ['<bean:write name="elemento" property="codigo" />', '<bean:write name="elemento" property="descripcion" />'];
      cont++;
    </logic:iterate>

//la necesito poruqe si hay algun rigen en un campo no lo puedo eliminar
 var listaCampos = new Array();
    var cont=0;
    <logic:iterate id="elemento" name="MantenimientoInformesForm" property="listaCampos">
      listaCampos[cont] = ['<bean:write name="elemento" property="codigo" />', 
      '<bean:write name="elemento" property="nome" />', 
      '<bean:write name="elemento" property="campo" />',
      '<bean:write name="elemento" property="tipo" />',
      '<bean:write name="elemento" property="lonxitude" />',
      '<bean:write name="elemento" property="origen" />',
      '<bean:write name="elemento" property="nomeas" />',
      '<bean:write name="elemento" property="criterio" />',
      '<bean:write name="elemento" property="descOrigen" />'];
      cont++;
    </logic:iterate>

	
    
     function copiarDatosSalida() {
	var w=dojo.widget.byId("parsedFromHtml");
	if (w) {
		var s=getValueSalida(w);
		 if(s != null) {
			document.forms[1].codParamNew.value = s[0];
			document.forms[1].descripcionParamNew.value = s[1];
			document.forms[1].tabParamNew.value = s[2];
			
			// creo los options y que me aparezca en el select el valor del selecionado
			removeOption();
			var lista = document.getElementById("modoParamNew");
			for(i=0; i<listaModoAmbitos.length; i++) {
				var opcion = document.createElement('option');
				opcion.text = listaModoAmbitos[i][1]; 
				opcion.value = listaModoAmbitos[i][1]; 
				lista.add(opcion, lista.selectedIndex); 
				if (opcion.value==s[3]) {opcion.selected=true;}
				lista.appendChild(opcion);  
			}
			document.forms[1].codParamNew.select();
		} else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
	}
    }
    
    function getValueSalida(w) {
        var data = w.store.get();
        for (var i = 0; i < data.length; i++) {
            if (data[i].isSelected) {
                var datos = listaAmbitos[i];
            }
        }
        return datos;
    }
 
  function eliminarAmbito(Key) {

        var params = new Array();
        params['codParamNew'] = Key[0];
         params['desParamNew'] = Key[1];
         var ok=ComprueboEliminar(params['desParamNew']);
          if(ok==1){
		      
		        var bindArgs = {
		            url: DOMAIN_NAME + "<c:url value='/gestionInformes/MantenimientoInformes.do?opcion=eliminarAmbito'/>",
		            error: function(type, data, evt){},
		            mimetype: "text/json",
		            content: params
		        };
		
		        var req = dojo.io.bind(bindArgs);
			
		        dojo.event.connect(req, "load", this, "reloadParamsOut");   
	       }else{
	   	 jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoBorrarFila")%>');
	   } 
}


function ComprueboEliminar(descripcion){

	var ok=1;
	for(i=0;i<listaCampos.length;i++){

		if(descripcion==listaCampos[i][8]){
			ok=0;
		}
	
	}
return ok;
}

function pulsarEliminar(){
        var w=dojo.widget.byId("parsedFromHtml");
        if (w) {
            var s=getValueSalida(w);
            if (s != null) {
           
                if(jsp_Alerta("C",'<%=descriptor.getDescripcion("desElimSol")%>') ==1) {
                    eliminarAmbito(s);
                }
            } else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
        }

        var w=dojo.widget.byId("parsedFromHtml");
    }
 function pulsarNuevo() {

   	var codigo=PonerCodigo();
	var params = new Array();
	params["codParamNew"] = codigo;
        params["descripcionParamNew"] = document.forms[1].descripcionParamNew.value;
        params["tabParamNew"] = document.forms[1].tabParamNew.value;
        var index=document.forms[1].modoParamNew.selectedIndex;
        params["modoParamNew"] = listaModoAmbitos[index][1];
	 valido = Validar(params["descripcionParamNew"]);
        if(valido==0){
        var bindArgs = {
            url: DOMAIN_NAME + "<c:url value='/gestionInformes/MantenimientoInformes.do?opcion=altaAmbito'/>",
            error: function(type, data, evt){},
            mimetype: "text/json",
            content: params
        };

        var req = dojo.io.bind(bindArgs);
	
        dojo.event.connect(req, "load", this, "reloadParamsOut");
  	}else{
		jsp_alerta("A",'<%=descriptor.getDescripcion("msjNombreYaExiste")%>');
		pulsarLimpiar();
	} 
}
   
    function modifyParamOut() {
        var params = new Array();
        params["codParamNew"] = document.forms[1].codParamNew.value;
        params["descripcionParamNew"] = document.forms[1].descripcionParamNew.value;
        params["tabParamNew"] = document.forms[1].tabParamNew.value;
        var index=document.forms[1].modoParamNew.selectedIndex;
        params["modoParamNew"] = listaModoAmbitos[index][1];

        var bindArgs = {
            url: DOMAIN_NAME + "<c:url value='/gestionInformes/MantenimientoInformes.do?opcion=modificar'/>",
            error: function(type, data, evt){},
            mimetype: "text/json",
            content: params
        };
        var req = dojo.io.bind(bindArgs);
        dojo.event.connect(req, "load", this, "reloadParamsOut");
    }
    function reloadParamsOut(type, data, evt) {
	if (data) {
           var theJSONData=[];
            var codigo=data.codigo;
            var descripcion=data.descripcion;
            var tab=data.tab;
            var modo=data.modo;
            for(var j=0; j<codigo.length; j++){
                var o ={
                    Id:codigo[j],
                    Descripcion:descripcion[j],
                    Tab:tab[j],
                    Modo:modo[j]
                };
                theJSONData.push(o);
                listaAmbitos[j] = [codigo[j], descripcion[j], tab[j], modo[j]];
                
                pulsarLimpiar();
                
            }
            var w=dojo.widget.byId("parsedFromHtml");
            w.store.setData(theJSONData);
        }
    }
   
	function pulsarLimpiar(){
	    document.forms[1].codParamNew.value='';
	    document.forms[1].descripcionParamNew.value='';
	    document.forms[1].tabParamNew.value='';
	
	    }
    function removeOption()
	{
	  var lista = document.getElementById('modoParamNew'); 
	var i;
  	for (i = lista.length - 1; i>=0; i--) {

		lista.remove(i);
	}
	}
function PonerCodigo(){
  var num;
  var cosa=0;
  	for(i=0;i<listaAmbitos.length;i++){
  		num=listaAmbitos[i][0];
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
  	for(i=0;i<listaAmbitos.length;i++){
  		cadena=listaAmbitos[i][1];
  		if(cadena==nombre){
  			valido=1;
  		}
  	}
  	return valido;
  }
function pulsarAdminCampos(){
	 var w=dojo.widget.byId("parsedFromHtml");
        if (w) {
            var s=getValueSalida(w);
           
            if (s != null) {
                    document.forms[1].codigoAmbito.value =s[0];
		    document.forms[1].opcion.value = "administracionCampos";
		    document.forms[1].action = DOMAIN_NAME + "<c:url value='/gestionInformes/MantenimientoInformes.do'/>";
		    document.forms[1].submit();
            } else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
        	
            
	}
}

</script>

<form name="form" action="/gestionInformes/MantenimientoInformes.do" target="_self">
     <input type="hidden" name="codigoAmbito" id="codigoAmbito" value="">
    <input type="hidden" name="opcion" id="opcion" value="">
  
   
    <div class="tablaGestionAmbito">
        <table class="xTabla" dojoType="filteringTable" id="parsedFromHtml" multiple="false" alternateRows="true" maxSortable="2"
               onDblClick="copiarDatosSalida();">
            <thead>
                <tr>
                    <th field="Descripcion" dataType="String" width="33%" align="center"><%=descriptor.getDescripcion("etiqGIDescrip")%></th>
                    <th field="Tab" dataType="String" width="33%" align="center"><%=descriptor.getDescripcion("etiqGITablaOri")%></th>
                    <th field="Modo" dataType="String" width="33%" align="center"><%=descriptor.getDescripcion("etiqGIModoOri")%></th>
                </tr>
            </thead>
            <tbody>
                <logic:iterate id="elemento" name="MantenimientoInformesForm" property="listaAmbitos">
                    <tr value="<bean:write name="elemento" property="codigo"/>">
                        <td align="center"><bean:write name="elemento" property="descripcion"/></td>
                        <td align="center"><bean:write name="elemento" property="tab"/></td>
                        <td align="center"><bean:write name="elemento" property="modo"/></td>

                    </tr>
                </logic:iterate>

            </tbody>
        </table>
        <div id="parsedFromHtml2" align="left" style="padding:4px;width:705px;" >
            <input type="hidden" class="inputTexto" id="codParamNew" name="codParamNew" >
            <input type="text" class="inputTextoObligatorio" id="descripcionParamNew" name="descripcionParamNew" style="width:233px;" onKeyPress="javascript:PasaAMayusculas(event);">
            <input type="text" class="inputTextoObligatorio" id="tabParamNew" name="tabParamNew" style="width:230px;" onKeyPress="javascript:PasaAMayusculas(event);">

            <select id="modoParamNew" class="inputTextoObligatorio" style="width:225px">
             <logic:iterate id="elemento" name="MantenimientoInformesForm" property="listaModoAmbitos">
                    <option value="elemento"><bean:write name="elemento" property="descripcion"/></option>
               </logic:iterate>
            </select>
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