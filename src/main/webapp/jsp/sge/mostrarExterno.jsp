<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.formularios.FichaFormularioForm"%>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title>::: Seleccionar Campo Externo:::</title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <!-- Estilos -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
    <!-- Beans -->
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
	<!-- Javascript -->
	<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
	<script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
    <style type="text/css">
       TR.rojo TD {background-color:red;color:white;}
       TR.gris TD {background-color:white;color:#a5a5a5;}
</style>
</head>
<body class="bandaBody" onload="rellena_valores();">
<script type="text/javascript">
    var codCampos = new Array();
    var tiposCampos = new Array();
    var j=0;
    
        
</script>
<%
    String codProcedimiento = request.getParameter("codProcedimiento");
    String codMunicipio = request.getParameter("codMunicipio");
    String nombreCampo = request.getParameter("nombreCampo");
    String valor_dato = request.getParameter("valor_dato");
    String opcion_pr_tra = request.getParameter("opcion_pr_tra");
    String datos_ext = (String) request.getAttribute("datos_externos");    
%>
<form action="" name="f" target="_self">
    <input type="hidden" name="opcion" value=""/>
                  
    <div class="txttitblanco">Campo Externo</div>
    <div class="contenidoPantalla">
        <input id="codExterno" name="codExterno" type="text" class="inputTexto" size="10" onkeyup="return SoloCaracterValidos(this);">
        <input id="descExterno" name="descExterno" type="text" class="inputTexto" size="75" readonly style="width:450px">
        <a id="anchorTipoDoc" name="anchorExterno" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonExterno" name="botonExterno" style="cursor:hand;" border="0" style="cursor:hand;"></span></a>
        <div id="capaBotones1" name="capaBotones1" class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar" onclick="pulsarAceptar();">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbVolver")%>" name="cmdVolver" onclick="pulsarVolver();">
        </div>
    </div>
</form>
<script type="text/javascript">     
function pulsarVolver(){    
    self.parent.opener.retornoXanelaAuxiliar();
}    
function pulsarAceptar(){
    var datos = new Array();
    datos.push(document.forms[0].codExterno.value);
    datos.push(document.forms[0].descExterno.value);
    self.parent.opener.retornoXanelaAuxiliar(datos);
}
function rellena_valores()
{  
   var opcion = <%=opcion_pr_tra%>;
   if (opcion == "procedimiento")
       rellena_procedimiento();
   else
       rellena_tramite();       
}
function rellena_procedimiento()
{
    var ajax = getXMLHttpRequest();
    var codProcedimiento = <%=codProcedimiento%>;
    var codMunicipio     = <%=codMunicipio%>;
    var nombre = <%=nombreCampo%>;
    var valor = <%=valor_dato%>;
    var posicion = 0;
    if(ajax!=null)
    {                     
        var url = "<%=request.getContextPath() %>" + "/sge/FichaExpediente.do";       
        var parametros = "&opcion=recuperarExterno&codMunicipio=" + escape(codMunicipio) + "&codProcedimiento=" + escape(codProcedimiento)
                            + "&campo=" + nombre;                    
        ajax.open("POST",url,false);
        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
        ajax.send(parametros);
        try
        {            
            if (ajax.readyState==4 && ajax.status==200)
            {            
                // En IE el XML viene en responseText y no en la propiedad responseXML
               var texto = ajax.responseText;     
           
               if (texto.trim() != "")                   
                    {           
                        cubrirCombo(texto,valor);      
                    }
            }               
        }catch(Err){    
            alert("Error.descripcion: " + Err.description);
        } 
    }//if(ajax!=null) 
}
function rellena_tramite()
{                
    var valor = <%=valor_dato%>; 
    var datos_externos = '<%=datos_ext%>';
    
    
    
    if (datos_externos.trim() != "")                   
    {            
          cubrirCombo (datos_externos,valor)         
    }
}   



function cubrirCombo(datos_externos,valor)
{    
    var posicion=0;
    var valoresRecuperados=datos_externos.split("$$");
    var codigosExternos=valoresRecuperados[0];
    var valoresExternos=valoresRecuperados[1];
    var array_cod = codigosExternos.split("||");
    var array_desc = valoresExternos.split("||");
   
    var codExterno = new Array();
    var descExterno = new Array();
    var comboExterno = new Combo("Externo");
    
    var i=0;

    for (i=0;i<array_cod.length;i++) 
     {     
        
         codExterno[i] = ""+array_cod[i]+"";
         descExterno[i] = array_desc[i];
                              
     }
     
     for (j=0;j<array_desc.length;j++)
     {
         
          if(array_desc[j]==valor)
          {
              posicion=j;
          }
          if((valor!='') && (valor!=null) && (j==array_desc.length-1) && posicion==0) posicion=j;
     }

     comboExterno.addItems(codExterno,descExterno);     
 
     if (array_cod.length > 0)
     {                   

         document.forms[0].codExterno.value = codExterno[posicion];
         document.forms[0].descExterno.value = valor;
     }
     
     if ((valor=='')||(valor=='undefined') ) comboExterno.selectItem(-1);
  
     
    
 
     
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
</script>
</body>
</html>
