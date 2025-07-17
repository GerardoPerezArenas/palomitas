<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.UsuariosGruposForm"%>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.agora.business.registro.mantenimiento.HojaArbolClasifAsuntosValueObject" %>
<%@ page import="es.altia.agora.business.registro.mantenimiento.MantAsuntosValueObject" %>
<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm" %>
<%@page import="java.util.ArrayList"%>
<%@ page import="java.util.Vector" %>

<%@ page contentType="text/html;charset=ISO_8859-1" language="java" %>
<html>
<head>
    <title>Asuntos</title>
    
    <%
        int idioma = 1;
        int apl = 1;
        int i=0;
        String css = "";
        if (session != null && session.getAttribute("usuario") != null) {
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            if (usuario != null) {
                idioma = usuario.getIdioma();
                apl = usuario.getAppCod();
                css = usuario.getCss();
            }
        }

        Integer hijoDesplegar=(Integer)request.getAttribute("hijoParaDesplegar");
        String codAsuntoSel=(String)request.getAttribute("codAsuntoSel");
        
    %>
  
     <style type="text/css">
			.arbore a:link {text-decoration:none;color:black;}
			.arbore a:visited {text-decoration:none;color:black;}
			.arbore a:active {text-decoration:none;color:black;}
			.arbore a:hover {text-decoration:none;color:black;}	
     </style>
  
          
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
    <script type="text/javascript">var APP_CONTEXT_PATH="<%=request.getContextPath()%>";</script>
    

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
    <jsp:setProperty name="descriptor" property="idi_cod" value="<%=idioma%>"/>
    <jsp:setProperty name="descriptor" property="apl_cod" value="<%=apl%>"/>
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/xtree.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/WebFXImgRadioButtonTreeItem.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/uor.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
  
    
</head>

<body>
<div class="txttitblanco">
    Clasificación de Asuntos
</div>
<div class="contenidoPantalla">						
    <table class="subsubtitulo" width="100%">
        <tr>
            <td>														
                <div id="arbol" style="min-height:295px;width:100%;" class="arbore"></div>
            </td>
        </tr>
    </table>
</div>
<script type="text/javascript"> 
//Definimos las estructuras javascript
//Inicializamos los hijos, que son los asuntos, 

var descripcionesArbol=new Array();
var codigosArbol=new Array();

var hijos=new Array();
var arbol= new Array();

var hijoParaDesplegar=<%=hijoDesplegar%>;

if(hijoParaDesplegar==null){
   hijoParaDesplegar=-1; 
}
  
//Inicializamos a estructura das descripcions da arbore (nomes das clasificacions)
  var contador=0;
  <logic:iterate id="descripcion" name="descripcions">
    descripcionesArbol[contador]='<bean:write name="descripcion"/>'
    contador++;
  </logic:iterate>   
  
//Inicializamos a estructura dos codigos da arbore (codigos das clasificacions)
  var contador=0;
  <logic:iterate id="codigo" name="codigos">
     codigosArbol[contador]='<bean:write name="codigo"/>'
     contador++;
  </logic:iterate>   
    
    
    
//Inicializamos a estructura dos fillos da arbore

var contador=0;
var contadorFuera=0;
  <logic:iterate id="hijosHoja" name="hijos" scope="request">
     contador=0;
    hijos[contadorFuera]=new Array();
    <logic:iterate id="asunto" name="hijosHoja">
        
    asunto=['<bean:write name="asunto" property="descripcion"/>',
            '<bean:write name="asunto" property="codigo"/>',
            '<bean:write name="asunto" property="codigoClasificacion"/>',
            '<bean:write name="asunto" property="unidadRegistro"/>',
            '<bean:write name="asunto" property="tipoRegistro"/>'];
     hijos[contadorFuera][contador]=asunto;
     contador++;
    </logic:iterate>   
    contadorFuera++;
 </logic:iterate> 
  

//Creamos la estructura de datos del arbol
//El valor boolean del final, que inicialmente estï¿½ puesto a true
//se utiliza para saber plegar/desplegar el arbol
   for(var i=0; i<descripcionesArbol.length; i++){
       arbol[i]=[hijos[i],descripcionesArbol[i],codigosArbol[i],true];
   }
   

  function creaArbore(idElemento) { //recibe como parametro o elemento dentro do que se crea 
                                   // a arbore  
        var espacio = "\u00a0";
	
        for (var i = 0; i < arbol.length; i++) {
                //Creamos todos os pais
		var pai = document.createElement("div");
               
		//Establecemos identificador
                pai.id = "pai" + i;
                 //pai.style="text-decoration:none";
                //pai.setAttribute("class","estiloNoLinkPai");
		//Creo un elemento link (a indica link)
		var iconoPai = document.createElement('a');
		iconoPai.setAttribute('href',"javascript:plegar(" + i + ");");
		var imaxePai = document.createElement('span');
		imaxePai.id = "imaxePai" + i;
		imaxePai.className='fa fa-folder-open';
		iconoPai.appendChild(imaxePai);
		pai.appendChild(iconoPai);
		
                var textoPai=document.createElement('a');
                textoPai.setAttribute('href',"javascript:plegar(" + i + ");");
                textoPai.appendChild(document.createTextNode(arbol[i][1]));
		
                 
                
                pai.appendChild(document.createTextNode(espacio)); 
                pai.appendChild(textoPai);
		idElemento.appendChild(pai);
               
               	plegar(-1); //por defecto saen todos plegados
    }
}
 
function plegar(nodo) {
    
     var espacio = "\u00a0";
     var lineamedio = "\u251c";
     var lineafin = "\u2514";
     var barra = "\u2502" 
     var moitoEspacio="\u00a0\u00a0\u00a0\u00a0\u00a0";
     
    if(arbol[nodo]!=null){
        
        if (arbol[nodo][3]) { //hai que desplegar
            var fillo = document.createElement("div");
            fillo.id = "fillo" + nodo;
            fillo.appendChild(document.createElement("BR")); //salto de liña
            for (var k = 0; k < arbol[nodo][0].length; k++) {
                if (k == arbol[nodo][0].length - 1) lineamedio = lineafin;
                
                fillo.appendChild(document.createTextNode(moitoEspacio + lineamedio));
                var iconoFillo = document.createElement('a');
                iconoFillo.setAttribute('href',"javascript:pulsaFillo(" + nodo + "," + k + ");");
                var imaxe = document.createElement('span');
                imaxe.id = "imaxefillo" + k;
                imaxe.className = 'fa fa-folder-open';
                
                iconoFillo.appendChild(imaxe);
                fillo.appendChild(iconoFillo);

                var textoFillo = document.createElement('a');
                textoFillo.setAttribute('href',"javascript:pulsaFillo(" + nodo + "," + k + ");");
                var descripcionCompletaAsunto=arbol[nodo][0][k][1]+" - "+arbol[nodo][0][k][0];
                textoFillo.appendChild(document.createTextNode(descripcionCompletaAsunto));
                fillo.appendChild(document.createTextNode(espacio)); 
                //inserta o enlace fillo
                // resaltamos o fillo seleccionado (#209576)
                if(arbol[nodo][2]=="<%=hijoDesplegar%>" && arbol[nodo][0][k][1]=="<%=codAsuntoSel%>") {
                    textoFillo.style.backgroundColor="#2E9AFE";
                    textoFillo.style.border="1px dotted #D8D8D8";
                } 
                fillo.appendChild(textoFillo);
                // dous saltos de linea
                fillo.appendChild(document.createElement("BR"));
                //fillo.appendChild(document.createElement(barra));

                document.getElementById("pai" + nodo).appendChild(fillo);
            }
	}
	else { //hai que plegar
		
            if(document.getElementById("pai"+nodo)!=null){ 
                if(document.getElementById("fillo"+nodo)!=null){
                   document.getElementById("pai" + nodo).removeChild(document.getElementById("fillo" + nodo));
                }    
            }
	}
        //Cambiamos o estado do nodo de plegado a replegado ou ï¿½ revï¿½s
	arbol[nodo][3] = !arbol[nodo][3];
    }
}

//Esta ï¿½ a funciï¿½n que hay que modificar co que queremos que faga ao pulsar o fillo
function pulsaFillo(pai,fillo) { //recibe como parï¿½metro o array, pai, e o fillo pulsado
	//alert(arbol[pai][0][fillo]);
         //mandamos como datos el asunto seleccionado
        var datos = new Array();
        datos= arbol[pai][0][fillo];
      
        self.parent.opener.retornoXanelaAuxiliar(datos);          
}

creaArbore(document.getElementById("arbol"));
//aqui hai que desplegar el nodo adecuado

plegar(hijoParaDesplegar);
//-->
  </script>
</body>
</html>
