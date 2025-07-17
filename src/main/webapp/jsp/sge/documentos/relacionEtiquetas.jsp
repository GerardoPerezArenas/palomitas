<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>

<%@page import="java.util.Vector"%>

<%@ page import="java.text.MessageFormat"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>

<%@ page import="es.altia.agora.technical.ConstantesDatos"%>
<%@page language="java" contentType="text/html" pageEncoding="ISO-8859-15"%>

<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title>::: Relacion Etiquetas Documento Tramite :::</title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<%
  int idioma=1;
  int apl=4;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl=usuario.getAppCod();
        }
  }

 String paraInteresado = request.getParameter("paraInteresado"); 
String codApp = request.getParameter("codApp"); 


%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
 <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>

<script type="text/javascript">
    
    var paraInteresado = '<%=paraInteresado%>';
    var codApp = '<%=codApp%>';
    var lista = new Array();
    /*
function inicializar() {
    var etiquetas = self.parent.opener.xanelaAuxiliarArgs;
    var contenidoCapaTodas = "";
    for(var i=0; i<etiquetas.length; i++){
        var elemento = etiquetas[i].tabla;
        var codigo = "\$\{" + elemento.codigo + "\}";
        var nombre = elemento.nombre + ":";
        var capaNombre = "<div style='width: 35%; display: inline; float: left;'>" + nombre + "</div>";
        var capaCodigo = "<div style='color: black; font-weight: bold'>" + codigo + "</div>";
        var contenidoCapaEtiqueta = capaNombre + capaCodigo + "<br>";
        var id = "etiq_" + i + "_" + elemento.codigo;
        contenidoCapaTodas += "<div id='" + id + "'>" + contenidoCapaEtiqueta + "</div>";
    }
    
    $('#capaEtiquetas').append(contenidoCapaTodas);
}
*/
function inicializar() {

    var etiquetas = self.parent.opener.xanelaAuxiliarArgs;


    for(var i=0; i<etiquetas.length; i++){
        var nombre="";
         var elemento = etiquetas[i].tabla;
         var codigo="";
          if(paraInteresado=='S'){ 
              codigo = "\$\{item." + elemento.codigo + "!\}";
          }else{
             codigo = "\$\{" + elemento.codigo + "!\}";
          }
         if(paraInteresado!='S'){
            if (codigo.indexOf("item.")>0) nombre=elemento.nombre+"*";
            else nombre=elemento.nombre;
        } else nombre=elemento.nombre;
        var boton=   '<span class="fa fa-clipboard" style="cursor:hand;" onclick="copiarEtiqueta('+i+');"></span>';
        
         lista[i]= [nombre,codigo,boton];
     

    }
 
    tab.lineas=lista;
    tab.displayTabla();
    
   
    var contenidoCapaLeyenda = "";
    
    if(codApp!='1'){
    
        if(paraInteresado=='S'){
            contenidoCapaLeyenda="<p style='color:red'><%= descriptor.getDescripcion("gEtiq_plantillaBloqueIntODT1")%></p>";
            contenidoCapaLeyenda+="<p style='font-style: italic'><%= descriptor.getDescripcion("gEtiq_plantillaBloqueIntODT2")%></p>";
            contenidoCapaLeyenda+="<p style='color:red'><%= descriptor.getDescripcion("gEtiq_plantillaBloqueIntODT3")%></p>";
            contenidoCapaLeyenda+="<p style='font-style: italic'><%= descriptor.getDescripcion("gEtiq_plantillaBloqueIntODT4")%></p>";

            $('#capaLeyenda').append(contenidoCapaLeyenda);
             $('#titulo').append(" Por Interesado");
        }else{
            contenidoCapaLeyenda="<p style='color:red'>*<%= descriptor.getDescripcion("gEtiq_plantillaBloqueODT")%></p>";
            contenidoCapaLeyenda+="<p style='font-style: italic'><%= descriptor.getDescripcion("gEtiq_plantillaBloqueIntODT2")%></p>";
            contenidoCapaLeyenda+="<p style='font-style: italic'><%= descriptor.getDescripcion("gEtiq_plantillaBloqueIntODT4")%></p>";
            $('#capaLeyenda').append(contenidoCapaLeyenda);


        }
    }
   
    
    
    
}

function pulsarAceptar(){
    self.parent.opener.retornoXanelaAuxiliar();
}


function copiarEtiqueta(id){
  var texto=lista[id][1];
  
  var aux = document.createElement("input");
  aux.setAttribute("value", texto);
  document.body.appendChild(aux);
  aux.select();
  document.execCommand("copy");
  document.body.removeChild(aux);

}
</script>
</head>

<body onload="javascript:{inicializar();}">
    <div id="titulo" class="txttitblanco">Etiquetas para plantilla ODT </div>
    <div class="contenidoPantalla">
        
         <div id="capaEtiquetas" style="float: left; width: 60%">       
        <table style="width: 100%">
                <tr> 
                    <td id="tabla">
                    </td>
                </tr>
        </table>
             </div>
         <div id="capaLeyenda" class="etiqueta" style="float: right; width: 35%; padding-top :25px; padding-right:  10px">       
         </div>
       
    </div>
    <div class="capaFooter">
         <div class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAnadir" onclick="pulsarAceptar();">
        </div>
    </div>
        
        
     
        
        
        
        
        <script type="text/javascript">
    
var tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

tab.addColumna('40','center','<%= descriptor.getDescripcion("gEtiq_nombre")%>');
tab.addColumna('40','center','<%= descriptor.getDescripcion("gEtiq_etiqueta")%>');
tab.addColumna('20','center','');

tab.displayCabecera=true;
tab.displayTabla();
    </script>
    
</body>

</html:html>

