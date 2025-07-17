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
<html>
<head>
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
       '<bean:write name="elemento" property="descOrigen" />',
      '<bean:write name="elemento" property="cri" />'];
      cont++;

    </logic:iterate>
  

    var listaAmbitos = new Array();
    var cont=0;
    <logic:iterate id="elemento" name="MantenimientoInformesForm" property="listaAmbitos">
      listaAmbitos[cont] = ['<bean:write name="elemento" property="codigo" />', '<bean:write name="elemento" property="descripcion" />', '<bean:write name="elemento" property="tab" />', '<bean:write name="elemento" property="modo" />'];
      
      cont++;
     
    </logic:iterate>
    
  function copiarDatosSalida() {
  	pulsarLimpiar();
	var w=dojo.widget.byId("parsedFromHtml");
	if (w) {
		var s=getValueSalida(w);
		 if(s != null) {
			document.forms[1].codParamNew.value = s[0];
			document.forms[1].nomeParamNew.value = s[1];
			document.forms[1].campoParamNew.value = s[2];
			document.forms[1].tipoParamNew.value = s[3];
			document.forms[1].lonxitudeParamNew.value = s[4];
			document.forms[1].descParamNew.value = s[5];
			document.forms[1].nomeasParamNew.value = s[6];
			//document.forms[1].criterioParamNew.value = s[7];
			if(s[7]==1){
				document.forms[1].criterioParamNew.checked=true;
			}
			
			// creo los options y que me aparezca en el select el valor del selecionado
			
		} else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
	}
    } 
   	    
  
   
  function pulsarNuevo() {

   	//var codigo=PonerCodigo();

	var params = new Array();
	//params["codParamNew"] = codigo;
        params["nomeParamNew"] = document.forms[1].nomeParamNew.value;
        params["campoParamNew"] = document.forms[1].campoParamNew.value;
        params["tipoParamNew"] = document.forms[1].tipoParamNew.value;
        params["lonxitudeParamNew"] = document.forms[1].lonxitudeParamNew.value;
        params["descParamNew"] =  document.forms[1].descParamNew.value;
        params["nomeasParamNew"] = document.forms[1].nomeasParamNew.value;
        if(document.forms[1].criterioParamNew.checked==true){
        	params["criterioParamNew"] = 1;
      	}else{
      		params["criterioParamNew"]=0;
      	}
      	
      valido = Validar (params["nomeParamNew"]);
	if(valido==0){
	       var bindArgs = {
	            url: DOMAIN_NAME + "<c:url value='/gestionInformes/MantenimientoInformes.do?opcion=altaCampo'/>",
	            error: function(type, data, evt){},
	            mimetype: "text/json",
	            content: params
	        };
	
	        var req = dojo.io.bind(bindArgs);
		
	        dojo.event.connect(req, "load", this, "reloadParamsOut");
	}else{
		jsp_alerta("A","'<%=descriptor.getDescripcion("msjNombreYaExiste")%>'");
		pulsarLimpiar();
	} 
	     
       
}

function eliminarCampo(Key) {

        var params = new Array();
        params['codParamNew'] = Key[0];
        params['nomParamNew'] = Key[1];
         params['descParamNew'] = Key[5];
        var bindArgs = {
            url: DOMAIN_NAME + "<c:url value='/gestionInformes/MantenimientoInformes.do?opcion=eliminarCampo'/>",
            error: function(type, data, evt){},
            mimetype: "text/json",
            content: params
        };
	   
        var req = dojo.io.bind(bindArgs);
        dojo.event.connect(req, "load", this, "reloadParamsOutEliminar");   
}
function reloadParamsOutEliminar(type, data, evt) {
	 var theJSONData=[];
            var codigo=data.codigo;
            var nome=data.nome;
            var campo=data.campo;
            var tipo=data.tipo;
            var lonxitude=data.lonxitude;
            var ori=data.ori;
            var origen=data.descori;
            var nomeas=data.nomeas;
            var criterio=data.criterio;
             var cri=data.cri;
          var eli=data.eli;
          if(eli==""){
	            for(var j=0; j<codigo.length; j++){
	                var o ={
	                    Id:codigo[j],
	                    Nome:nome[j],
	                    Campo:campo[j],
	                    Tipo:tipo[j],
	                    Lonxitude:lonxitude[j],
	                    Origen:origen[j],
	                    Nomeas:nomeas[j],
	                    Cri:cri[j]
	                   
	                };
	                theJSONData.push(o);
	                listaCampos[j] = [codigo[j],nome[j],campo[j],tipo[j],lonxitude[j],ori[j],nomeas[j],criterio[j],origen[j],cri[j]];     
	            }
	            
	           pulsarLimpiar();
	            var w=dojo.widget.byId("parsedFromHtml");
	            w.store.setData(theJSONData);
	   }else{
	  	jsp_alerta("C",'<%=descriptor.getDescripcion("msjNoBorrarCamp")%>')
	   }
        
    }
function pulsarEliminar(){
        var w=dojo.widget.byId("parsedFromHtml");
        if (w) {
            var s=getValueSalida(w);
            if (s != null) {
           
                if(jsp_alerta("C",'<%=descriptor.getDescripcion("desElimSol")%>') ==1) {
                    eliminarCampo(s);
                    // document.forms[1].eliminar.value = '<bean:write name="MantenimientoInformesForm" property="eliminado"/>';
                    //alert( document.forms[1].eliminar.value );
                }
            } else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
        }

        var w=dojo.widget.byId("parsedFromHtml");
    }
    
 function modifyParamOut() {
        var params = new Array();
 
      	params["codParamNew"] =  document.forms[1].codParamNew.value;
        params["nomeParamNew"] = document.forms[1].nomeParamNew.value;
        params["campoParamNew"] = document.forms[1].campoParamNew.value;
        params["tipoParamNew"] = document.forms[1].tipoParamNew.value;
        params["lonxitudeParamNew"] = document.forms[1].lonxitudeParamNew.value;
        params["descParamNew"] =  document.forms[1].descParamNew.value;
        params["nomeasParamNew"] = document.forms[1].nomeasParamNew.value;
         if(document.forms[1].criterioParamNew.checked==true){
        	params["criterioParamNew"] = 1;
      	}else{
      		params["criterioParamNew"]=0;
      	}
      
 
	        var bindArgs = {
	            url: DOMAIN_NAME + "<c:url value='/gestionInformes/MantenimientoInformes.do?opcion=modificarCampo'/>",
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
            var nome=data.nome;
            var campo=data.campo;
            var tipo=data.tipo;
            var lonxitude=data.lonxitude;
            var ori=data.ori;
            var origen=data.descori;
            var nomeas=data.nomeas;
            var criterio=data.criterio;
             var cri=data.cri;
          
            for(var j=0; j<codigo.length; j++){
                var o ={
                    Id:codigo[j],
                    Nome:nome[j],
                    Campo:campo[j],
                    Tipo:tipo[j],
                    Lonxitude:lonxitude[j],
                    Origen:origen[j],
                    Nomeas:nomeas[j],
                    Cri:cri[j]
                   
                };
                theJSONData.push(o);
                listaCampos[j] = [codigo[j],nome[j],campo[j],tipo[j],lonxitude[j],ori[j],nomeas[j],criterio[j],origen[j],cri[j]];     
            }
           pulsarLimpiar();
            var w=dojo.widget.byId("parsedFromHtml");
            w.store.setData(theJSONData);
        }
    }

function pulsarLimpiar(){
	document.forms[1].nomeParamNew.value='';
	document.forms[1].campoParamNew.value='';
	document.forms[1].tipoParamNew.value='';
	document.forms[1].lonxitudeParamNew.value='';
	document.forms[1].nomeasParamNew.value='';
	document.forms[1].criterioParamNew.checked=false;

}
 
 function Validar(nombre){
  var cadena;
  var valido=0;
  	for(i=0;i<listaCampos.length;i++){
  		cadena=listaCampos[i][1];
  		if(cadena==nombre){
  			valido=1;
  		}
  	}
  	return valido;
  } 
  
  function getValueSalida(w) {
        var data = w.store.get();
        for (var i = 0; i < data.length; i++) {
            if (data[i].isSelected) {
                var datos = listaCampos[i];
            }
        }
        return datos;
    }
    
</script>
</head>
<body>
<form name="form" action="/gestionInformes/MantenimientoInformes.do" target="_self">
 
  


    <!-- TABLA DE PROCEDIMIENTOS -->

    <input type="hidden" name="opcion" id="opcion" value="">
     <input type="hidden" name="eliminar" value="">
   
    <div class="tablaGestionAmbito">
        <table class="xTabla" dojoType="filteringTable" id="parsedFromHtml" multiple="false" alternateRows="true" maxSortable="2"
               onDblClick="copiarDatosSalida();">
            <thead>
                <tr>
                    <th field="Nome" dataType="String" width="19%" align="center"><%=descriptor.getDescripcion("etiqGINome")%></th>
                    <th field="Campo" dataType="String" width="14%" align="center"><%=descriptor.getDescripcion("etiqGICampo")%></th>
                    <th field="Tipo" dataType="String" width="6%" align="center"><%=descriptor.getDescripcion("etiqGITipo")%></th>
                    <th field="Lonxitude" dataType="Number" width="6%" align="center"><%=descriptor.getDescripcion("etiqGILonxitude")%></th>
                    <th field="Nomeas" dataType="String" width="19%" align="center"><%=descriptor.getDescripcion("etiqGINomeas")%></th>   
                    <th field="Cri" dataType="String" width="6%" align="center"><%=descriptor.getDescripcion("etiqGICriterio")%></th>
                </tr>
            </thead>
            <tbody>
                <logic:iterate id="elemento" name="MantenimientoInformesForm" property="listaCampos" >
                    <tr value="<bean:write name="elemento" property="codigo"/>">
                        <td align="center"><bean:write name="elemento" property="nome"/></td>
                        <td align="center"><bean:write name="elemento" property="campo"/></td>
                        <td align="center"><bean:write name="elemento" property="tipo"/></td>
                        <td align="center"><bean:write name="elemento" property="lonxitude"/></td>
                        <td align="center"><bean:write name="elemento" property="nomeas"/></td>
                        <td align="center"><bean:write name="elemento" property="cri"/></td>
                    </tr>
                </logic:iterate>
            </tbody>
        </table>
        <div id="parsedFromHtml2" align="left" style="padding:4px;width:705px;" >
            <input type="hidden" class="inputTexto" id="codParamNew" name="codParamNew">
            <input type="hidden" id="descParamNew" class="inputTextoObligatorio">
            <input type="text" class="inputTextoObligatorio" id="nomeParamNew" name="nomeParamNew" style="width:180px;">
            <input type="text" class="inputTextoObligatorio" id="campoParamNew" name="campoParamNew" style="width:128px;" onKeyPress="javascript:PasaAMayusculas(event);">
            <input type="text" class="inputTextoObligatorio" id="tipoParamNew" name="tipoParamNew" style="width:58px;" maxlength="1"  onKeyPress="javascript:PasaAMayusculas(event);">
            <input type="text" class="inputTextoObligatorio" id="lonxitudeParamNew" name="lonxitudeParamNew" style="width:60px;" onKeyPress="javascript:return SoloDigitos(event);">
            <input type="text" class="inputTextoObligatorio" id="nomeasParamNew" name="nomeasParamNew" style="width:178px;" onKeyPress="javascript:PasaAMayusculas(event);">
            <input type="checkbox" id="criterioParamNew" name="criterioParamNew" style="width:21px;margin-left:16px;">
        </div>
    </div>
</form>
</body>
</html>

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