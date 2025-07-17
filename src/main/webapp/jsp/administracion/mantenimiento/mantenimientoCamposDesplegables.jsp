<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Mantenimiento de Campos Desplegables</title>


<%
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  int idioma=1;
  int apl=5;
  String css="";
  if (session.getAttribute("usuario") != null){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    apl = usuarioVO.getAppCod();
    idioma = usuarioVO.getIdioma();
    css=usuarioVO.getCss();
  }%>
  
  
  
<%    Config m_Config = ConfigServiceHelper.getConfig("common");%>
<!-- Estilos -->

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
<script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>

<script type="text/javascript">
var lista = new Array(); 
var datosCamposDesplegables = new Array();

    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function inicializar(){
	  window.focus();
      cargaTablaCamposDesplegables();
    }

    function cargaTablaCamposDesplegables(){
    <%
      MantenimientosAdminForm bForm =(MantenimientosAdminForm)session.getAttribute("MantenimientosAdminForm");
      Vector listaCampoDesplegables = bForm.getListaCamposDesplegables();
      int lengthCampoDesplegables = listaCampoDesplegables.size();
      int i = 0;
    %>
      var j=0;
    <%for(i=0;i<lengthCampoDesplegables;i++){
        GeneralValueObject CampoDesplegable = (GeneralValueObject)listaCampoDesplegables.get(i);%>
      datosCamposDesplegables[j] = ['<%=(String)CampoDesplegable.getAtributo("codigo")%>',
        '<%=(String)CampoDesplegable.getAtributo("descripcion")%>'];
      lista[j] = datosCamposDesplegables[j];
      j++;
    <%}%>
      tablaCamposDesplegables.lineas = lista;
      refresca(tablaCamposDesplegables);
    }

    // FUNCIONES DE LIMPIEZA DE CAMPOS
    function limpiarFormulario(){
      tablaCamposDesplegables.lineas = new Array();
      refresca(tablaCamposDesplegables);
      limpiarInputs();
    }

    function limpiarInputs(){
      activarFormBoolean();
      document.forms[0].Codigo.value = '';
      document.forms[0].Descripcion.value = '';
	  var vector = [document.forms[0].Codigo];
	  habilitarGeneral(vector);
    }

    function limpiar() {
      limpiarInputs();
      tablaCamposDesplegables.selectLinea(-1);
    }

    // FUNCIONES DE PULSACION DE BOTONES
    function pulsarSalir(){
      document.forms[0].target = "mainFrame";
      document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
      document.forms[0].submit();
    }




   function getXMLHttpRequest(){
        var aVersions = [ "MSXML2.XMLHttp.5.0",
                "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
                "MSXML2.XMLHttp","Microsoft.XMLHttp"
        ];

        if (window.XMLHttpRequest){
                // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
                return new XMLHttpRequest();
        }else if (window.ActiveXObject){
                // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
                for (var i = 0; i < aVersions.length; i++) {
                        try {
                            var oXmlHttp = new ActiveXObject(aVersions[i]);
                            return oXmlHttp;
                        }catch (error) {
                        //no necesitamos hacer nada especial
                        }
                }
        }
    }

    function pulsarEliminar(i){    
        var ajax = getXMLHttpRequest();
        if(ajax!=null){
            var indice = tablaCamposDesplegables.selectedIndex;        
            var identificador = lista[indice][0];

            var url        = "<%=request.getContextPath()%>//administracion/mantenimiento/CamposDesplegables.do";       
            var parametros = "opcion=comprobarEliminacionCampo&identificador=" + identificador;

            ajax.open("POST",url,false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
            ajax.send(parametros);

            try{
                if (ajax.readyState==4 && ajax.status==200){                                
                    // En IE el XML viene en responseText y no en la propiedad responseXML
                   var text = ajax.responseText;               

                   if(text!=null && text!=undefined && text!="null" && text!=""){
                       if(text==1)
                           jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrCampoAsigSup")%>');
                       else
                       if(text==2)
                           jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrCampoAsigSupT")%>');
                       else
                       if(text==-1 || text==-2)
                           jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrEliminarCampoExt")%>');
                       else
                       if(text==0){                                              
                            eliminarCampo(identificador);
                       }

                   }
                }            
            }catch(Err){
                alert("1 --> Error.descripcion: " + Err.description);
            }
        }
    }


    function eliminarCampo(){        
        var vector = [document.forms[0].Codigo];
	habilitarGeneral(vector);
	if(tablaCamposDesplegables.selectedIndex != -1) {
            document.forms[0].identificador.value = lista[tablaCamposDesplegables.selectedIndex][0];
            document.forms[0].opcion.value = 'eliminar';
            document.forms[0].target = "oculto";
            document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/CamposDesplegables.do';
            document.forms[0].submit();
        }
        else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }

   
    function pulsarModificar(){
      var vector = [document.forms[0].Codigo];
	  habilitarGeneral(vector);
	  var cod = document.forms[0].Codigo.value;
      var yaExiste = 0;
	  if(tablaCamposDesplegables.selectedIndex != -1){
        if(validarFormulario()){
          for(l=0; l < lista.length; l++){
            var lineaSeleccionada;
            lineaSeleccionada = tablaCamposDesplegables.selectedIndex;
            if(l == lineaSeleccionada) {
              l= l;
            } else {
              if ((lista[l][0]) == cod ){
                yaExiste = 1;
              }
            }
          }
		  if(yaExiste == 0) {
		    document.forms[0].identificador.value = lista[tablaCamposDesplegables.selectedIndex][0];
            document.forms[0].opcion.value = 'modificar';
            document.forms[0].target = "oculto";
            document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/CamposDesplegables.do';
            document.forms[0].submit();
		  } else {
		    jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
		  }
        }
      }
      else
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }

        function pulsarValores(){
        if(tablaCamposDesplegables.selectedIndex != -1) {
            var vector = [document.forms[0].Codigo];
            habilitarGeneral(vector);
            document.forms[0].opcion.value = 'valores';
            document.forms[0].target = "mainFrame";
            document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/CamposDesplegables.do';
            document.forms[0].submit();
        }
        else
          jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }

    function pulsarAlta(){
      var cod = document.forms[0].Codigo.value;
      var existe = 0;
      if (validarFormulario()){
        for(var i=0; (i < lista.length) && (existe == 0); i++){
          if((lista[i][0]) == cod)
            existe = 1;
        }
        if(existe == 0){
          document.forms[0].opcion.value = 'alta';
          document.forms[0].target = "oculto";
          document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/CamposDesplegables.do';
          document.forms[0].submit();
        }
        else
          jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
      }
    }

    function buscar(tabla){
      var auxDes = "";
      if((event.keyCode != 40)&&(event.keyCode != 38)){
        if(event.keyCode != 13){
          auxDes = document.forms[0].Descripcion.value;
          if(event.keyCode == 8){
            if(auxDes.length == 0){
              limpiarInputs();
            }
          }
          selecFila(auxDes);
        }else{
          if((tabla.selectedIndex>-1)&&(tabla.selectedIndex < lista.length)){
            pintaDatos(tabla.getLinea());
          }
        }
      }
    }
	
	function pintaDatos(datos){
      document.forms[0].Codigo.value = datos[0];
      document.forms[0].Descripcion.value = datos[1];
    }


    function selecFila(des){
      if(des.length != 0){
        for (var x=0; x<lista.length; x++){
          var auxLis = new String(lista[x][1]);
          auxLis = auxLis.substring(0,des.length);
          if(auxLis == des){
            if(x!=tablaCamposDesplegables.selectedIndex) tablaCamposDesplegables.selectLinea(x);
            return;
          }
        }
      }
    }

    function recuperaDatos(lista2) {
      limpiarInputs();
      lista = lista2;
      tablaCamposDesplegables.lineas=lista;
      refresca(tablaCamposDesplegables);
    }

function desactivarFormBoolean() {
    var vector = [document.forms[0].Codigo, document.forms[0].Descripcion];
    deshabilitarGeneral(vector);
    var vectorBotones = new Array(document.forms[0].cmdValores,document.forms[0].cmdAlta,
                                  document.forms[0].cmdModificar,document.forms[0].cmdEliminar);
    deshabilitarGeneral(vectorBotones);
}

function activarFormBoolean() {
    vector = [document.forms[0].Descripcion];
    habilitarGeneral(vector);
    var vectorBotones = new Array(document.forms[0].cmdValores,document.forms[0].cmdAlta,
                                  document.forms[0].cmdModificar,document.forms[0].cmdEliminar);
    habilitarGeneral(vectorBotones);
}

  </script>
</head>

<body class="bandaBody" onload="javascript:{pleaseWait('off');
        inicializar();}">

    <jsp:include page="/jsp/hidepage.jsp" flush="true">
        <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
    </jsp:include>

<form name="formulario" method="post">
    <input  type="hidden"  name="opcion" id="opcion">
    <input  type="hidden"  name="identificador" id="identificador">
    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiq_MantCamposDesp")%></div>
    <div class="contenidoPantalla">
        <table>
            <tr>
                <td>
                    <div id="tabla"></div>
                </td>
            </tr>
            <tr>
                <td>
                    <input type="text" class="inputTextoObligatorio" id="obligatorio" name="Codigo" maxlength="4" size="14" style="width:13%" onkeyup="return xAMayusculas(this);">
                    <input name="Descripcion" type="text" class="inputTextoObligatorio" id="obligatorio" maxlength="90" style="width:86%" onblur="return xAMayusculas(this);" size="125">
                </td>
            </tr>
        </table>								
        <div id="tablaBotones" class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbValores")%>"
                    name="cmdValores" onClick="pulsarValores();" accesskey="V">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAlta")%>"
                        name="cmdAlta" onClick="pulsarAlta();" accesskey="A"> 
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbModificar")%>"
                        name="cmdModificar" onClick="pulsarModificar();" accesskey="M">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>"
                        name="cmdEliminar" onClick="pulsarEliminar();" accesskey="E">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbLimpiar")%>"
                        name="cmdLimpiar" onClick="limpiar();" accesskey="L">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSalir")%>"
                        name="cmdSalir" onClick="pulsarSalir();" accesskey="S"> 
        </div>      
    </div>      
</form>
<script type="text/javascript">
  var tablaCamposDesplegables = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

  tablaCamposDesplegables.addColumna('120','center','<%= descriptor.getDescripcion("gEtiq_Codigo")%>');
  tablaCamposDesplegables.addColumna('780',null,'<%= descriptor.getDescripcion("gEtiq_Descrip")%>');
  tablaCamposDesplegables.displayCabecera = true;
  function refresca(tabla){
    tabla.displayTabla();
  }

  function rellenarDatos(tableName,rowID){
    var i = rowID;
    limpiarInputs();
	var vector = [document.forms[0].Codigo];
	deshabilitarGeneral(vector);
    if((i>=0)&&!tableName.ultimoTable){
        document.forms[0].Codigo.value = lista[i][0];
        document.forms[0].Descripcion.value = lista[i][1];
        if (lista[i][0]=="<%=m_Config.getString("E_PLT.CodigoCampoDesplegableBoolean")%>") {
            desactivarFormBoolean();
        } else {
            activarFormBoolean();
        }
    }
  }

  document.onmouseup = checkKeys;

            function checkKeysLocal(evento, tecla){

              var teclaAuxiliar = "";
                if(window.event){
                    evento = window.event;
                    teclaAuxiliar =evento.keyCode;
                }else
                    teclaAuxiliar =evento.which;
                keyDel(evento);

                if (teclaAuxiliar == 38) upDownTable(tablaCamposDesplegables,lista,teclaAuxiliar);
                if (teclaAuxiliar == 40) upDownTable(tablaCamposDesplegables,lista,teclaAuxiliar);
                if (teclaAuxiliar == 13) pushEnterTable(tablaCamposDesplegables,lista);
  }

</script>

</body>
</html>
