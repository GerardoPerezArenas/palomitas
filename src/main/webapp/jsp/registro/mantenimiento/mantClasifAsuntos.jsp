<!-- JSP de mantenimiento de CLASIFICACION DE ASUNTOS -->

<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html:html>
    <head>
        <title>Clasificacion Asuntos</title>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />


    <%
        int idioma = 1;
        int apl = 1;
        String css = "";
        if (session != null && session.getAttribute("usuario") != null) {
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            if (usuario != null) {
                idioma = usuario.getIdioma();
                apl = usuario.getAppCod();
                css = usuario.getCss();
            }
        }
        
        String opcionError=  request.getParameter("opcionError");
        if(opcionError!=null){
            opcionError=opcionError.trim();
               }
    %>

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
                 type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
    <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />


    <!-- *********************	FICHEROS JAVASCRIPT **************************    -->

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<html:rewrite page='/scripts/TablaNueva.js'/>"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >
<script type="text/javascript"> 
  
   //Lista de clasificacion de asuntos (guarda codigo y descripcion)
    var lista = new Array();
    //Lista de codigos de asunto
    var lista_claves = new Array();
    
    /////////////// Funcion de inicializacion de la pagina.

    function Inicio() {       
        window.focus();
        cargaTabla();
    }
  

    function pulsarSalir(){       
        document.forms[0].opcion.value = 'salir';
        document.forms[0].target = 'mainFrame';
        document.forms[0].submit();
    }

  

    /* Recupera la lista de asuntos */
    function recuperaListaClasifAsuntos(listaClasifAsuntos){       
        lista = listaClasifAsuntos;
        tablaClasifAsuntos.lineas = lista;
        tablaClasifAsuntos.displayTabla();
    }


    function existeCodigoAsunto(cod) {
        var exito = false;
        
        // Si no cambia -> estamos modificando -> se puede usar        
        for(i=0; i<lista.length; i++) {
            if (lista[i][0] == cod){ 
                exito =true;
            }
        }        
        return exito;
    }

   function pulsarGrabar() {        
        var codigo      = document.forms[0].codAsunto.value;
        var descripcion = document.forms[0].descripcion.value;
        
        if (codigo!=null && codigo!="" && descripcion!=null && descripcion!="") {
          if (existeCodigoAsunto(codigo)) {
              jsp_alerta('A',"<str:escape><%=descriptor.getDescripcion("msjCodigoExiste")%></str:escape>");
          } else {
            document.forms[0].action = "<%=request.getContextPath()%>/MantClasifAsuntos.do";              
            document.forms[0].opcion.value = 'grabar_alta';
            document.forms[0].target="oculto";
            document.forms[0].submit();
           }
        }else
            jsp_alerta('A','<str:escape><%=descriptor.getDescripcion("msjErrDatosObligAlta")%></str:escape>');
   }
   
   
   function repintarTabla(listaClaves,listaGeneral){
        lista_claves = listaClaves;
        lista = listaGeneral;

        tablaClasifAsuntos.lineas=listaGeneral;
        tablaClasifAsuntos.displayTabla(); 
   }
   
   
 
  function validarEntrada(){  
        var numero="";
        if(document.getElementById("codigo")!=null){
           numero=document.getElementById("codigo").value; 
        }

        if (! /^-?\d+$/.test(numero)) {
            jsp_alerta('A',"<str:escape><%=descriptor.getDescripcion("msjCodigoNoEsNumero")%></str:escape>");
            return false;
        }
        else {
            return true;
        }
  }



    /*************/
    
    function rellenarDatos(tableObject, rowID){ 
        if(rowID>-1 && !tableObject.ultimoTable){
            document.forms[0].codAsunto.value = lista[rowID][0];
            document.forms[0].descripcion.value = lista[rowID][1];
            document.forms[0].codAsunto.disabled = true;
            
        }else borrarDatos();
    }
    
    function borrarDatos(){
        document.forms[0].codAsunto.value = "";
        document.forms[0].descripcion.value = "";
    }


 /*************/
</SCRIPT>

    </head>

    <body class="bandaBody" onload="javascript:{ pleaseWait('off');
       }" >

        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<html:form action="MantClasifAsuntos" method="post">
<html:hidden property="opcion"/>
<html:hidden property="codigo"/>
<html:hidden property="unidadRegistro"/>

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titMantClasifAsu")%></div>
<div class="contenidoPantalla" valign="top">
    <table>
        <tr>
            <td valign="top">
                <table>
                    <tr>
                        <td id="tabla">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="text" class="inputTextoObligatorio" name="codAsunto" value="" onkeyup="return SoloDigitosNumericos(this);" style="width:11%"/>
                            <input type="text" class="inputTextoObligatorio" name="descripcion" value="" maxlength="225" onkeyup="return xAMayusculas(this);" style="width:88%"/>
                        </td>
                    </tr>    
                </table>
            </td>
        </tr>
        <tr>
            <td>
                 <label id="textoErrorEliminar" />
            </td>                                                        
        </tr>
    </table>
        <div class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%> name="cmdAlta" onClick="pulsarGrabar();" alt="<%= descriptor.getDescripcion("toolTip_bAltaTAsunto")%>" title="<%= descriptor.getDescripcion("toolTip_bAltaTAsunto")%>">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> name="cmdModificar" onClick="pulsarModificar();" title='<%=descriptor.getDescripcion("toolTip_bModificarAsunto")%>' alt='<%=descriptor.getDescripcion("toolTip_bModificar")%>'>
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> name="cmdEliminar" onClick="pulsarEliminar();" title='<%=descriptor.getDescripcion("toolTip_bEliminar")%>' alt='<%=descriptor.getDescripcion("toolTip_bEliminar")%>'>
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%> name="cmdEliminar" onClick="limpiar();" title='<%=descriptor.getDescripcion("toolTip_bEliminar")%>' alt='<%=descriptor.getDescripcion("toolTip_bLimpiar")%>'>
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir" onClick="pulsarSalir();" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>'>
        </div>
    </div>
 </html:form>
  <script type="text/javascript">
   var tablaClasifAsuntos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tabla"));

    tablaClasifAsuntos.addColumna('100','center',"<str:escape><%=descriptor.getDescripcion("etiqCodigoAsunto")%></str:escape>");
    tablaClasifAsuntos.addColumna('783','center',"<str:escape><%=descriptor.getDescripcion("etiqDescAsunto")%></str:escape>");
    tablaClasifAsuntos.displayCabecera=true;

///////////////////////////////////////////////////////////
    function cargaTabla(){
      
       var i = 0;
       <logic:iterate id="clasifAsunto" name="MantClasifAsuntosForm" property="clasifAsuntos">
         lista_claves[i] = ['<bean:write name="clasifAsunto" property="codigo" />']
         lista[i] = ['<bean:write name="clasifAsunto" property="codigo" />',
                     '<bean:write name="clasifAsunto" property="descripcion"/>'];
         i++;
      </logic:iterate>

      tablaClasifAsuntos.lineas=lista;
      tablaClasifAsuntos.displayTabla(); 
     
    }
    
/////////////////////////////////////////////////////////    
    
   function pulsarModificar() {  
        if(tablaClasifAsuntos.selectedIndex == -1) {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
        } else {
        
            var codigo      = document.forms[0].codAsunto.value;
            var descripcion = document.forms[0].descripcion.value;
                        
            if(codigo!=null && codigo!="" && descripcion!=null && descripcion!=""){
                document.forms[0].opcion.value = 'grabar_modificar';        
                document.forms[0].codigo.value = lista_claves[tablaClasifAsuntos.selectedIndex];            
                document.forms[0].target = 'oculto';
                document.forms[0].submit();            
            }else
                jsp_alerta('A','<%=descriptor.getDescripcion("msjErrDatosObligEdit")%>');
        }
   } 


    function limpiar(){
        document.forms[0].codAsunto.disabled = false;
        document.forms[0].codAsunto.value = "";
        document.forms[0].descripcion.value = "";
    }


  function pulsarEliminar() { 
    
    if(tablaClasifAsuntos.selectedIndex == -1) {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    } else{
            if (jsp_alerta('', "<str:escape><%=descriptor.getDescripcion("msjBorrarAsunto")%></str:escape>")) {
                limpiar();
                document.forms[0].opcion.value = 'eliminar';
                document.forms[0].codigo.value = lista_claves[tablaClasifAsuntos.selectedIndex];                
                document.forms[0].target = 'oculto';
                document.forms[0].submit();
            }
        }
   
    }

//////////////////////////////////////////////////////////////////

  var opcionError="<%=opcionError%>";
    
    if(opcionError=="errorEliminar"){
      
        pintarErrorEliminar();
        
    }
    
/////////////////////////////////////////////////////////////////////    
function pintarErrorEliminar(){

var error= "<%=descriptor.getDescripcion("errorEliminarTexto")%> ";

var texto='<div class="etiquetaError" width="100%" align="center">';
    texto+= error;
    texto+='<div/>';
   
   jsp_alerta("A",error);
   
   /**
  if(document.getElementById("textoErrorEliminar")!=null){ 
     
         document.getElementById("textoErrorEliminar").innerHTML=texto;
   }*/

}
   



 </script>
    <script> Inicio(); </script>
    </body>
    </html:html>
