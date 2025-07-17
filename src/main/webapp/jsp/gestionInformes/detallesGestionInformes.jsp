<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config" %>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@ page import="es.altia.agora.interfaces.user.web.gestionInformes.FichaInformeForm"%>
<%@ page import="java.util.Vector"%>
<%@ page import="es.altia.agora.business.gestionInformes.persistence.CampoValueObject"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>

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
    FichaInformeForm fichaInformeForm = (FichaInformeForm) session.getAttribute("FichaInformeForm");
    Vector listaCriInfDisponibles = fichaInformeForm.getListaCriInfDisponibles();
    Vector listaCriInfSeleccionados = fichaInformeForm.getListaCriInf();
    CampoValueObject campoVO;

    Log log = LogFactory.getLog(this.getClass());
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />



<script type="text/javascript">
    // loop through all links of inside finder
    function pulsarNuevaVersion() {
        lista = document.getElementById('listaCampos1');
        for (i=0; i<lista.childNodes.length; i++) {
            node = lista.childNodes[i];
            if (node.nodeName=="LI") {
            }
        }
    }
    function pulsarAlta() {
        lista1 = document.getElementById('listaCampos1');
        for (i=0; i<lista1.childNodes.length; i++) {
            node = lista1.childNodes[i];
            lista1.removeChild(node);
        }
        lista2 = document.getElementById('listaCampos2');
        for (i=0; i<lista2.childNodes.length; i++) {
            node = lista2.childNodes[i];
            lista2.removeChild(node);
        }
        elemento = document.createElement("li");
        elemento.id = lista1.id;
        elemento.appendChild(document.createTextNode("NODO"));
        lista1.appendChild(elemento);

    }

// return the value of the radio button that is checked
// return an empty string if none are checked, or
// there are no radio buttons
function getCheckedValue(radioObj) {
	if(!radioObj)
		return "";
	var radioLength = radioObj.length;
	if(radioLength == undefined)
		if(radioObj.checked)
			return radioObj.value;
		else
			return "";
	for(var i = 0; i < radioLength; i++) {
		if(radioObj[i].checked) {
			return radioObj[i].value;
		}
	}
	return "";
}

// set the radio button with the given value as being checked
// do nothing if there are no radio buttons
// if the given value does not exist, all the radio buttons
// are reset to unchecked
function setCheckedValue(radioObj, newValue) {
	if(!radioObj)
		return;
	var radioLength = radioObj.length;
	if(radioLength == undefined) {
		radioObj.checked = (radioObj.value == newValue.toString());
		return;
	}
	for(var i = 0; i < radioLength; i++) {
		radioObj[i].checked = false;
		if(radioObj[i].value == newValue.toString()) {
			radioObj[i].checked = true;
		}
	}
}

function addEvent(elemento,nomevento,funcion,captura)
{
  if (elemento.attachEvent)
  {
    elemento.attachEvent('on'+nomevento,funcion);
    return true;
  }
  else
    if (elemento.addEventListener)
    {
      elemento.addEventListener(nomevento,funcion,captura);
      return true;
    }
    else
      return false;
}   
</script>

<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/dojo/dojo.js"></script>
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript">
    dojo.require("dojo.widget.*");
    dojo.require("dojo.io.*");
    dojo.require("dojo.event.*");
    dojo.require("dojo.html.*");
    dojo.require("dojo.dnd.*");

    //CREAMOS UN ARRAY EN MEMORIA CON TODOS LOS CAMPOS DISPONIBLES
    var listaCamposArray = new Array();
    var listaCriInfArray = new Array();
    var listaCabInfArray = new Array();
    var listaPieInfArray = new Array();
    var listaCabPagArray = new Array();
    var listaPiePagArray = new Array();
    var listaPermisosArray = new Array();    		 

    function inicializar() {
        var publicado = '<bean:write name="FichaInformeForm" property="publicado"/>';
        document.getElementById("codProcedimiento").checked=true;
        if(publicado == "1") {
            document.getElementById("publicado").checked = true;
        } else {
            document.getElementById("publicado").checked = false;
        }
        var papel = '<bean:write name="FichaInformeForm" property="papel"/>';
        if (papel != "A3") {
            document.getElementById("papel").selectedIndex = 0;
        } else {
            document.getElementById("papel").selectedIndex = 1;
        }

        var orientacion = '<bean:write name="FichaInformeForm" property="orientacion"/>';
        setCheckedValue(document.getElementsByName("orientacion"), orientacion);

        function ventanaCampos(id) {
            document.getElementById("idCampoActual").value=id;
            document.getElementById("valorCampoActual").value=listaCamposArray[id][8];
            document.getElementById("tituloCampoActual").value=listaCamposArray[id][1];
            if ("LEFT" == listaCamposArray[id][5]) {
                document.getElementById("alignCampoActual").selectedIndex = 0;
            } else if ("RIGHT" == listaCamposArray[id][5]) {
                document.getElementById("alignCampoActual").selectedIndex = 1;
            } else {
                document.getElementById("alignCampoActual").selectedIndex = 2;
            }
            document.getElementById("anchoCampoActual").value=listaCamposArray[id][6];
         
             if ("0" == listaCamposArray[id][9]) {
                document.getElementById("ordenCampoActual").selectedIndex = 0;
            } else if ("1" == listaCamposArray[id][9]) {
                document.getElementById("ordenCampoActual").selectedIndex = 1;
            } else {
                document.getElementById("ordenCampoActual").selectedIndex = 2;
            }
            if ("1" == listaCamposArray[id][7]) {
                document.getElementById("elipsisCampoActual").checked=true;
            } else {
                document.getElementById("elipsisCampoActual").checked=false;
            }
            dlgCampo.show();
        }

        function ventanaCriterio(id) {
            var pos = encontrarPosCriterio(id);
            
            if (listaCriInfArray[pos][7]=='F') {
                eval ("var nulos = (listaCriInfArray[pos][2]=='8') || (listaCriInfArray[pos][2]=='9')");
                eval ("if (nulos) document.getElementById('valor1Criterio"+id+"').value='';");
                if (nulos) eval("document.getElementById('valor1Criterio"+id+"').style.visibility='hidden';");
                eval("document.getElementById('valorOperCriterio"+id+"').value = listaCriInfArray[pos][2];");
                eval("dojo.widget.byId('valor1Criterio"+id+"').inputNode.value = listaCriInfArray[pos][3];");
                eval("dojo.widget.byId('valor2Criterio"+id+"').inputNode.value = (listaCriInfArray[pos][4]=='null') ? '' : listaCriInfArray[pos][4];");
                eval("dojo.widget.byId('valor1Criterio"+id+"').onInputChange();");
                eval("dojo.widget.byId('valor2Criterio"+id+"').onInputChange();");
                comprobarOperFecha(id);
                
                eval("dlgCriterio"+id+".show();");
            } else {
                 eval("document.getElementById('valorOperCriterio"+id+"').value = listaCriInfArray["+pos+"][2];");
                 eval("var opcionEntre = document.getElementById('valorOperCriterio"+id+"').value==4;");
                 if (opcionEntre) {
                     eval("document.getElementById('valor2Criterio"+id+"').value = listaCriInfArray["+pos+"][4];");
                 }

                if (listaCriInfArray[pos][7]=='D') {
                  
                    eval("document.getElementById('valor1Criterio"+id+"').value = listaCriInfArray["+pos+"][3];");
                } else {
                    eval("document.getElementById('valor1Criterio"+id+"').value = listaCriInfArray["+pos+"][3];");
                }

                eval("dlgCriterio"+id+".show();");

                if (listaCriInfArray[pos][7]=='N') {
                    comprobarOperNumerico(id);
                }
                if (listaCriInfArray[pos][7]=='A') {
                    comprobarOperTexto(id);
                }
                if (listaCriInfArray[pos][7]=='I') {
                    comprobarOperInteresado(id);
                }

            }
         }


        var lista1 = document.getElementById('listaCampos1');
        //Campos disponibles
        <logic:present name="FichaInformeForm" property="listaCamposDisponibles">
        <logic:iterate id="campo" name="FichaInformeForm" property="listaCamposDisponibles">
            elemento = document.createElement("div");
            elemento.id = '<bean:write name="campo" property="idCampo"/>';
            addEvent(elemento,
                     "dblclick",
                     function() { ventanaCampos('<bean:write name="campo" property="idCampo"/>')},
                     false);
            elemento.appendChild(document.createTextNode('<bean:write name="campo" property="titulo"/>'));

            lista1.appendChild(elemento);
            detallesCampo = new Array(9);
            detallesCampo[0] = '<bean:write name="campo" property="origen"/>'; // CODIGO
            detallesCampo[1] = '<bean:write name="campo" property="titulo"/>'; // NOMBRE
            detallesCampo[2] = '<bean:write name="campo" property="tabla"/>'; // TABLA
            detallesCampo[3] = '0'; // POSX
            detallesCampo[4] = '0'; // POSY
            detallesCampo[5] = 'LEFT'; // ALIGN
            detallesCampo[6] = '20'; // ANCHO
            detallesCampo[7] = '1'; // ELIPSIS
            detallesCampo[8] = '<bean:write name="campo" property="tituloOriginal"/>'; // NOMBRE
            detallesCampo[9] = '0'; 
            listaCamposArray.push(detallesCampo);
        </logic:iterate>
        </logic:present>

        var d1 = document.getElementById("listaCampos1");
        new dojo.dnd.HtmlDropTarget(d1, ["li1"]);
        var divs = d1.getElementsByTagName("div");
        for(var x=0; x<divs.length; x++){
            new dojo.dnd.HtmlDragSource(divs[x], "li1");
        }
        //Campos seleccionados
        var lista2 = document.getElementById('listaCampos2');
        <logic:present name="FichaInformeForm" property="listaCampos">
        <logic:iterate id="campo" name="FichaInformeForm" property="listaCampos">
            elemento = document.createElement("div");
            elemento.id = '<bean:write name="campo" property="idCampo"/>';
            addEvent(elemento,
                 "dblclick",
                 function() { ventanaCampos('<bean:write name="campo" property="idCampo"/>'); },
                 false);
            elemento.appendChild(document.createTextNode('<bean:write name="campo" property="titulo"/>'));
            lista2.appendChild(elemento);
            detallesCampo = new Array(9);
            detallesCampo[0] = '<bean:write name="campo" property="origen"/>'; // CODIGO
            detallesCampo[1] = '<bean:write name="campo" property="titulo"/>'; // NOMBRE
            detallesCampo[2] = '<bean:write name="campo" property="tabla"/>'; // TABLA
            detallesCampo[3] = '<bean:write name="campo" property="posx"/>'; // POSX
            detallesCampo[4] = '<bean:write name="campo" property="posy"/>'; // POSY
            detallesCampo[5] = '<bean:write name="campo" property="align"/>'; // ALIGN
            detallesCampo[6] = '<bean:write name="campo" property="ancho"/>'; // ANCHO
            detallesCampo[7] = '<bean:write name="campo" property="elipsis"/>'; // ELIPSIS
            detallesCampo[8] = '<bean:write name="campo" property="tituloOriginal"/>'; // NOMBRE
            detallesCampo[9] = '<bean:write name="campo" property="orden"/>';//ORDENACION
            listaCamposArray.push(detallesCampo);
        </logic:iterate>
        </logic:present>
        var d2 = document.getElementById("listaCampos2");
        new dojo.dnd.HtmlDropTarget(d2, ["li1"]);
        var divs2 = d2.getElementsByTagName("div");
        for(var x=0; x<divs2.length; x++){
            new dojo.dnd.HtmlDragSource(divs2[x], "li1");
        }

        // Criterios Informe disponibles
        var listaCriInf1 = document.getElementById('listaCriInf1');
        var cont=0;
        <logic:present name="FichaInformeForm" property="listaCriInfDisponibles">
        <logic:iterate id="criInfDisp" name="FichaInformeForm" property="listaCriInfDisponibles">
            elemento = document.createElement("div");
            elemento.id = cont;
            addEvent(elemento,
                "dblclick",
                function() { ventanaCriterio('<bean:write name="criInfDisp" property="idCampo"/>'); },
                false);
            elemento.appendChild(document.createTextNode('<bean:write name="criInfDisp" property="titulo"/>'));
            listaCriInf1.appendChild(elemento);
            detallesCampo = new Array(8);
            detallesCampo[0] = '<bean:write name="criInfDisp" property="idCampo"/>'; // CODIGO
            detallesCampo[1] = '<bean:write name="criInfDisp" property="titulo"/>'; // NOMBRE
            detallesCampo[2] = ''; // CONDICION
            detallesCampo[3] = ''; // VALOR1
            detallesCampo[4] = '<bean:write name="criInfDisp" property="valor2Criterio"/>'; // VALOR2
            detallesCampo[5] = '<bean:write name="criInfDisp" property="origen"/>'; // ORIGEN
            detallesCampo[6] = '<bean:write name="criInfDisp" property="tabla"/>'; // TABLA
            detallesCampo[7] = '<bean:write name="criInfDisp" property="tipo"/>'; // TIPO DE DATOS
            listaCriInfArray.push(detallesCampo);
            cont++;
        </logic:iterate>
        </logic:present>
        d1 = document.getElementById("listaCriInf1");
        new dojo.dnd.HtmlDropTarget(d1, ["li1"]);
        lis = d1.getElementsByTagName("div");
        for(var x=0; x<lis.length; x++){
            new dojo.dnd.HtmlDragSource(lis[x], "li1");
        }

        //Criterios informe ya seleccionados
        var listaCriInf2 = document.getElementById('listaCriInf2');
        <logic:present name="FichaInformeForm" property="listaCriInf">
        <logic:iterate id="criInf" name="FichaInformeForm" property="listaCriInf">
            elemento = document.createElement("div");
            elemento.id = cont;
            addEvent(elemento,
                "dblclick",
                function() { ventanaCriterio('<bean:write name="criInf" property="idCampo"/>'); },
                false);
            elemento.appendChild(document.createTextNode('<bean:write name="criInf" property="titulo"/>'));
            listaCriInf2.appendChild(elemento);
            detallesCampo = new Array(8);
            detallesCampo[0] = '<bean:write name="criInf" property="idCampo"/>'; // CODIGO
            detallesCampo[1] = '<bean:write name="criInf" property="titulo"/>'; // NOMBRE
            detallesCampo[2] = '<bean:write name="criInf" property="condicionCriterio"/>'; // CONDICION
            detallesCampo[3] = '<bean:write name="criInf" property="valor1Criterio"/>'; // VALOR1
            detallesCampo[4] = '<bean:write name="criInf" property="valor2Criterio"/>'; // VALOR2
            detallesCampo[5] = '<bean:write name="criInf" property="origen"/>'; // ORIGEN
            detallesCampo[6] = '<bean:write name="criInf" property="tabla"/>'; // TABLA
            detallesCampo[7] = '<bean:write name="criInf" property="tipo"/>'; // TIPO DE DATOS
            listaCriInfArray.push(detallesCampo);
            cont++;
        </logic:iterate>
        </logic:present>
        d2 = document.getElementById("listaCriInf2");
        new dojo.dnd.HtmlDropTarget(d2, ["li1"]);
        lis2 = d2.getElementsByTagName("div");
        for(var x=0; x<lis2.length; x++){
            new dojo.dnd.HtmlDragSource(lis2[x], "li1");
        }

        // Cab Informe
        var listaCabInf1 = document.getElementById('listaCabInf1');
        cont=0;
        <logic:present name="FichaInformeForm" property="listaCabInfDisponibles">
        <logic:iterate id="cabInf" name="FichaInformeForm" property="listaCabInfDisponibles">
            elemento = document.createElement("div");
            elemento.id = cont;
            elemento.appendChild(document.createTextNode('<bean:write name="cabInf" property="titulo"/>'));
            listaCabInf1.appendChild(elemento);
            detallesCampo = new Array(4);
            detallesCampo[0] = '<bean:write name="cabInf" property="idCampo"/>'; // CODIGO
            detallesCampo[1] = '<bean:write name="cabInf" property="titulo"/>'; // NOMBRE
            detallesCampo[2] = '0'; // POSX
            detallesCampo[3] = '0'; // POSY
            listaCabInfArray.push(detallesCampo);
            cont++;
        </logic:iterate>
        </logic:present>
        d1 = document.getElementById("listaCabInf1");
        new dojo.dnd.HtmlDropTarget(d1, ["li1"]);
        lis = d1.getElementsByTagName("div");
        for(var x=0; x<lis.length; x++){
            new dojo.dnd.HtmlDragSource(lis[x], "li1");
        }
        //Cab informe disponibles
        var listaCabInf2 = document.getElementById('listaCabInf2');
        <logic:present name="FichaInformeForm" property="listaCabInf">
        <logic:iterate id="cabInf" name="FichaInformeForm" property="listaCabInf">
            elemento = document.createElement("div");
            elemento.id = cont;
            elemento.appendChild(document.createTextNode('<bean:write name="cabInf" property="titulo"/>'));
            listaCabInf2.appendChild(elemento);
            detallesCampo = new Array(4);
            detallesCampo[0] = '<bean:write name="cabInf" property="idCampo"/>'; // CODIGO
            detallesCampo[1] = '<bean:write name="cabInf" property="titulo"/>'; // NOMBRE
            detallesCampo[2] = '<bean:write name="cabInf" property="posx"/>'; // POSX
            detallesCampo[3] = '<bean:write name="cabInf" property="posy"/>'; // POSY
            listaCabInfArray.push(detallesCampo);
            cont++;
        </logic:iterate>
        </logic:present>
        d2 = document.getElementById("listaCabInf2");
        new dojo.dnd.HtmlDropTarget(d2, ["li1"]);
        lis2 = d2.getElementsByTagName("div");
        for(var x=0; x<lis2.length; x++){
            new dojo.dnd.HtmlDragSource(lis2[x], "li1");
        }

        // Pie Informe
        var listaPieInf1 = document.getElementById('listaPieInf1');
        cont=0;
        <logic:present name="FichaInformeForm" property="listaPieInfDisponibles">
        <logic:iterate id="pieInf" name="FichaInformeForm" property="listaPieInfDisponibles">
            elemento = document.createElement("div");
            elemento.id = cont;
            elemento.appendChild(document.createTextNode('<bean:write name="pieInf" property="titulo"/>'));
            listaPieInf1.appendChild(elemento);
            detallesCampo = new Array(4);
            detallesCampo[0] = '<bean:write name="pieInf" property="idCampo"/>'; // CODIGO
            detallesCampo[1] = '<bean:write name="pieInf" property="titulo"/>'; // NOMBRE
            detallesCampo[2] = '0'; // POSX
            detallesCampo[3] = '0'; // POSY
            listaPieInfArray.push(detallesCampo);
            cont++;
        </logic:iterate>
        </logic:present>
        d1 = document.getElementById("listaPieInf1");
        new dojo.dnd.HtmlDropTarget(d1, ["li1"]);
        lis = d1.getElementsByTagName("div");
        for(var x=0; x<lis.length; x++){
            new dojo.dnd.HtmlDragSource(lis[x], "li1");
        }
        //Pie informe disponibles
        var listaPieInf2 = document.getElementById('listaPieInf2');
        <logic:present name="FichaInformeForm" property="listaPieInf">
        <logic:iterate id="pieInf" name="FichaInformeForm" property="listaPieInf">
            elemento = document.createElement("div");
            elemento.id = cont;
            elemento.appendChild(document.createTextNode('<bean:write name="pieInf" property="titulo"/>'));
            listaPieInf2.appendChild(elemento);
            detallesCampo = new Array(4);
            detallesCampo[0] = '<bean:write name="pieInf" property="idCampo"/>'; // CODIGO
            detallesCampo[1] = '<bean:write name="pieInf" property="titulo"/>'; // NOMBRE
            detallesCampo[2] = '<bean:write name="pieInf" property="posx"/>'; // POSX
            detallesCampo[3] = '<bean:write name="pieInf" property="posy"/>'; // POSY
            listaPieInfArray.push(detallesCampo);
            cont++;
        </logic:iterate>
        </logic:present>
        d2 = document.getElementById("listaPieInf2");
        new dojo.dnd.HtmlDropTarget(d2, ["li1"]);
        lis2 = d2.getElementsByTagName("div");
        for(var x=0; x<lis2.length; x++){
            new dojo.dnd.HtmlDragSource(lis2[x], "li1");
        }

        // Cab Pagina
        var listaCabPag1 = document.getElementById('listaCabPag1');
        cont=0;
        <logic:present name="FichaInformeForm" property="listaCabPagDisponibles">
        <logic:iterate id="cabPag" name="FichaInformeForm" property="listaCabPagDisponibles">
            elemento = document.createElement("div");
            elemento.id = cont;
            elemento.appendChild(document.createTextNode('<bean:write name="cabPag" property="titulo"/>'));
            listaCabPag1.appendChild(elemento);
            detallesCampo = new Array(4);
            detallesCampo[0] = '<bean:write name="cabPag" property="idCampo"/>'; // CODIGO
            detallesCampo[1] = '<bean:write name="cabPag" property="titulo"/>'; // NOMBRE
            detallesCampo[2] = '0'; // POSX
            detallesCampo[3] = '0'; // POSY
            listaCabPagArray.push(detallesCampo);
            cont++;
        </logic:iterate>
        </logic:present>
        d1 = document.getElementById("listaCabPag1");
        new dojo.dnd.HtmlDropTarget(d1, ["li1"]);
        lis = d1.getElementsByTagName("div");
        for(var x=0; x<lis.length; x++){
            new dojo.dnd.HtmlDragSource(lis[x], "li1");
        }
        //Cab pagina disponibles
        var listaCabPag2 = document.getElementById('listaCabPag2');
        <logic:present name="FichaInformeForm" property="listaCabPag">
        <logic:iterate id="cabPag" name="FichaInformeForm" property="listaCabPag">
            elemento = document.createElement("div");
            elemento.id = cont;
            elemento.appendChild(document.createTextNode('<bean:write name="cabPag" property="titulo"/>'));
            listaCabPag2.appendChild(elemento);
            detallesCampo = new Array(4);
            detallesCampo[0] = '<bean:write name="cabPag" property="idCampo"/>'; // CODIGO
            detallesCampo[1] = '<bean:write name="cabPag" property="titulo"/>'; // NOMBRE
            detallesCampo[2] = '<bean:write name="cabPag" property="posx"/>'; // POSX
            detallesCampo[3] = '<bean:write name="cabPag" property="posy"/>'; // POSY
            listaCabPagArray.push(detallesCampo);
            cont++;
        </logic:iterate>
        </logic:present>
        d2 = document.getElementById("listaCabPag2");
        new dojo.dnd.HtmlDropTarget(d2, ["li1"]);
        lis2 = d2.getElementsByTagName("div");
        for(var x=0; x<lis2.length; x++){
            new dojo.dnd.HtmlDragSource(lis2[x], "li1");
        }

        // Pie Pagina
        var listaPiePag1 = document.getElementById('listaPiePag1');
        cont=0;
        <logic:present name="FichaInformeForm" property="listaPiePagDisponibles">
        <logic:iterate id="piePag" name="FichaInformeForm" property="listaPiePagDisponibles">
            elemento = document.createElement("div");
            elemento.id = cont;
            elemento.appendChild(document.createTextNode('<bean:write name="piePag" property="titulo"/>'));
            listaPiePag1.appendChild(elemento);
            detallesCampo = new Array(4);
            detallesCampo[0] = '<bean:write name="piePag" property="idCampo"/>'; // CODIGO
            detallesCampo[1] = '<bean:write name="piePag" property="titulo"/>'; // NOMBRE
            detallesCampo[2] = '0'; // POSX
            detallesCampo[3] = '0'; // POSY
            listaPiePagArray.push(detallesCampo);
            cont++;
        </logic:iterate>
        </logic:present>
        d1 = document.getElementById("listaPiePag1");
        new dojo.dnd.HtmlDropTarget(d1, ["li1"]);
        lis = d1.getElementsByTagName("div");
        for(var x=0; x<lis.length; x++){
            new dojo.dnd.HtmlDragSource(lis[x], "li1");
        }
        //Pie pagina disponibles
        var listaPiePag2 = document.getElementById('listaPiePag2');
        <logic:present name="FichaInformeForm" property="listaPiePag">
        <logic:iterate id="piePag" name="FichaInformeForm" property="listaPiePag">
            elemento = document.createElement("div");
            elemento.id = cont;
            elemento.appendChild(document.createTextNode('<bean:write name="piePag" property="titulo"/>'));
            listaPiePag2.appendChild(elemento);
            detallesCampo = new Array(4);
            detallesCampo[0] = '<bean:write name="piePag" property="idCampo"/>'; // CODIGO
            detallesCampo[1] = '<bean:write name="piePag" property="titulo"/>'; // NOMBRE
            detallesCampo[2] = '<bean:write name="piePag" property="posx"/>'; // POSX
            detallesCampo[3] = '<bean:write name="piePag" property="posy"/>'; // POSY
            listaPiePagArray.push(detallesCampo);
            cont++;
        </logic:iterate>
        </logic:present>
        d2 = document.getElementById("listaPiePag2");
        new dojo.dnd.HtmlDropTarget(d2, ["li1"]);
        lis2 = d2.getElementsByTagName("div");
        for(var x=0; x<lis2.length; x++){
            new dojo.dnd.HtmlDragSource(lis2[x], "li1");
        }

        // Permisos
        var listaPermisos1 = document.getElementById('listaPermisos1');
        cont=0;
        <logic:present name="FichaInformeForm" property="listaUORDisponibles">
        <logic:iterate id="permiso" name="FichaInformeForm" property="listaUORDisponibles">

            elemento = document.createElement("div");
            elemento.id = cont;            
            elemento.appendChild(document.createTextNode("<str:escape><bean:write name="permiso" property="titulo" filter="false"/></str:escape>"));
            listaPermisos1.appendChild(elemento);
            detallesCampo = new Array(4);
            detallesCampo[0] = "<str:escape><bean:write name="permiso" property="idCampo" filter="false"/></str:escape>"; // CODIGO
            detallesCampo[1] = "<str:escape><bean:write name="permiso" property="titulo" filter="false"/></str:escape>"; // NOMBRE
            detallesCampo[2] = '0'; // POSX
            detallesCampo[3] = '0'; // POSY
            listaPermisosArray.push(detallesCampo);
            cont++;
        </logic:iterate>
        </logic:present>
        d1 = document.getElementById("listaPermisos1");
        new dojo.dnd.HtmlDropTarget(d1, ["li1"]);
        lis = d1.getElementsByTagName("div");
        for(var x=0; x<lis.length; x++){
            new dojo.dnd.HtmlDragSource(lis[x], "li1");
        }
        //Permisos disponibles
        var listaPermisos2 = document.getElementById('listaPermisos2');
        <logic:present name="FichaInformeForm" property="listaUOR">
        <logic:iterate id="permiso" name="FichaInformeForm" property="listaUOR">
            elemento = document.createElement("div");
            elemento.id = cont;
            elemento.appendChild(document.createTextNode("<str:escape><bean:write name="permiso" property="titulo" filter="false"/></str:escape>"));
            listaPermisos2.appendChild(elemento);
            detallesCampo = new Array(4);
            detallesCampo[0] = "<str:escape><bean:write name="permiso" property="idCampo" filter="false"/></str:escape>"; // CODIGO
            detallesCampo[1] = "<str:escape><bean:write name="permiso" property="titulo" filter="false"/></str:escape>"; // NOMBRE
            detallesCampo[2] = '0'; // POSX
            detallesCampo[3] = '0'; // POSY
            listaPermisosArray.push(detallesCampo);
            cont++;
        </logic:iterate>
        </logic:present>
        d2 = document.getElementById("listaPermisos2");
        new dojo.dnd.HtmlDropTarget(d2, ["li1"]);
        lis2 = d2.getElementsByTagName("div");
        for(var x=0; x<lis2.length; x++){
            new dojo.dnd.HtmlDragSource(lis2[x], "li1");
        }

        var dlgCampo;
         <% if (listaCriInfDisponibles != null)
            for (int i=0;i<listaCriInfDisponibles.size();i++) {
                campoVO = (CampoValueObject) listaCriInfDisponibles.get(i); %>
                id = <%=campoVO.getIdCampo()%>;
                eval("var dlgCriterio"+id+";");
         <% } %>
         <% if (listaCriInfSeleccionados != null)
            for (int i=0;i<listaCriInfSeleccionados.size();i++) {
                campoVO = (CampoValueObject) listaCriInfSeleccionados.get(i); %>
                id = <%=campoVO.getIdCampo()%>;
                eval("var dlgCriterio"+id+";");
         <% } %>

        function init(e) {
            dlgCampo = dojo.widget.byId("DialogCampo");
            var btn = document.getElementById("cancelarDialogCampo");
            dlgCampo.setCloseControl(btn);
            var id;
         <% if (listaCriInfDisponibles != null)
            for (int i=0;i<listaCriInfDisponibles.size();i++) {
                campoVO = (CampoValueObject) listaCriInfDisponibles.get(i); %>
                id = <%=campoVO.getIdCampo()%>;
                eval("dlgCriterio"+id+" = dojo.widget.byId('DialogCriterio"+id+"');");
                eval("var cancelarCriterio"+id+" = document.getElementById('cancelarDialogCriterio"+id+"');");
                eval("dlgCriterio"+id+".setCloseControl(cancelarCriterio"+id+");");
        <%  } %>
         <% if (listaCriInfSeleccionados != null)
            for (int i=0;i<listaCriInfSeleccionados.size();i++) {
                campoVO = (CampoValueObject) listaCriInfSeleccionados.get(i); %>
                id = <%=campoVO.getIdCampo()%>;
                eval("dlgCriterio"+id+" = dojo.widget.byId('DialogCriterio"+id+"');");
                eval("var cancelarCriterio"+id+" = document.getElementById('cancelarDialogCriterio"+id+"');");
                eval("dlgCriterio"+id+".setCloseControl(cancelarCriterio"+id+");");
        <%  } %>
        }
        dojo.addOnLoad(init);
    }

    function encontrarPosCriterio(id) { 
        var pos = 0;
        var encontrado = false;
        while (!encontrado && pos<listaCriInfArray.length) {
            if (listaCriInfArray[pos][0]==id) {
                encontrado = true;
            } else {
                pos++;
            }
        }
        return pos;
    }

    function validarCampoSup(){
        var tituloCampoActual = document.getElementById("tituloCampoActual").value;
        var anchoCampoActual = document.getElementById("anchoCampoActual").value;
        if (tituloCampoActual!="" && anchoCampoActual!="")
            return true;
        return false;
    }

    function actualizarCampo(){

        if (validarCampoSup()){
            var w=dojo.widget.byId("DialogCampo");
            if(w){
                var id = document.getElementById("idCampoActual").value;
                listaCamposArray[id][1]=document.getElementById("tituloCampoActual").value;
                listaCamposArray[id][5]=document.getElementById("alignCampoActual").value;
                listaCamposArray[id][6]=document.getElementById("anchoCampoActual").value;
                if (document.getElementById("elipsisCampoActual").checked == true) {
                    listaCamposArray[id][7]="1";
                } else {
                    listaCamposArray[id][7]="0";
                }
                listaCamposArray[id][9]=document.getElementById("ordenCampoActual").value;
                document.getElementById("cancelarDialogCampo").click();
            }
        } else {
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
        }
    }

    function validarCamposObligatoriosFecha(id) {
        eval("var opcionNulo = document.getElementById('valorOperCriterio"+id+"').value=='8';");
        eval("var opcionNoNulo = document.getElementById('valorOperCriterio"+id+"').value=='9';");
        eval ("var nulos = opcionNulo || opcionNoNulo;");
        eval ("if (nulos) document.getElementById('valor1Criterio"+id+"').value='';");
        eval("var noVacio = document.getElementById('valor1Criterio"+id+"').value!='';");
        if (nulos) {return true;}
        if (noVacio) {
            eval("var opcionEntre = document.getElementById('valorOperCriterio"+id+"').value==4;");
            if (opcionEntre) {
                eval("noVacio = dojo.widget.byId('valor2Criterio"+id+"').getValue()!='';");
                if (noVacio) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    function validarCamposObligatorios(id) {

        eval("var noVacio = document.getElementById('valor1Criterio"+id+"').value!='';");
        eval("var opcionNulo = document.getElementById('valorOperCriterio"+id+"').value=='8';");
        eval("var opcionNoNulo = document.getElementById('valorOperCriterio"+id+"').value=='9';");
        eval ("var nulos = opcionNulo || opcionNoNulo;");

        if (noVacio) {
            eval("var opcionEntre = document.getElementById('valorOperCriterio"+id+"').value==4;");
            if (nulos) eval("document.getElementById('valor1Criterio"+id+"').value='';");
            if (opcionEntre) {
                eval("var valida = document.getElementById('valor2Criterio"+id+"').value!='';");
                return valida;
            } else {
                return true;
            }
        } else {
            if (nulos) return true;
            return false;
        }
    }

    function validaNumericoAnterior(id) {
        eval("var valor1Criterio = new Number(document.getElementById('valor1Criterio"+id+"').value);");
        eval("var valor2Criterio = new Number(document.getElementById('valor2Criterio"+id+"').value);");
        if (valor1Criterio<=valor2Criterio) {
            return true;
        } else {
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjNumAnterior")%>');
            return false;
        }
    }

    function actualizarCriterio(id) {
        var pos = encontrarPosCriterio(id);
        if (listaCriInfArray[pos][7]=='F') {
            //Tipo fecha
            if (validarCamposObligatoriosFecha(id)) {
                eval("var valida = document.getElementById('valorOperCriterio"+id+"').value!=4 || (document.getElementById('valorOperCriterio"+id+"').value==4 && " +
                     "validarFechaAnterior(dojo.widget.byId('valor1Criterio"+id+"').getValue(),dojo.widget.byId('valor2Criterio"+id+"').getValue()));");
                if (valida) {
                    eval("listaCriInfArray["+pos+"][2]=document.getElementById('valorOperCriterio"+id+"').value;");
                    eval("listaCriInfArray["+pos+"][3]=dojo.widget.byId('valor1Criterio"+id+"').getValue();");
                    eval("listaCriInfArray["+pos+"][4]=dojo.widget.byId('valor2Criterio"+id+"').getValue();");
                    eval("document.getElementById('cancelarDialogCriterio"+id+"').click();");
                }
            } else {
          	listaCriInfArray[pos][2]='null';
          	eval("document.getElementById('cancelarDialogCriterio"+id+"').click();");
                //jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
            }
        } else if (listaCriInfArray[pos][7]=='I') {      
                eval("var nulos= document.getElementById('valorOperCriterio"+id+"').value=='8';");
                eval("nulos = nulos ||  document.getElementById('valorOperCriterio"+id+"').value=='9';");
                if (nulos) {
                    eval("document.getElementById('valor1Criterio"+id+"').value='';");
                    eval("document.getElementById('valor2Criterio"+id+"').value='';");
                }
                eval ("document.getElementById('valorOperCriterio"+id+"').value") == '8'
                eval("listaCriInfArray["+pos+"][2] = document.getElementById('valorOperCriterio"+id+"').value;");
                //aqui el nombre del interesado
                eval("listaCriInfArray["+pos+"][3] = document.getElementById('valor1Criterio"+id+"').value;");
                //aqui el codigo del interesado
                eval("listaCriInfArray["+pos+"][4] = document.getElementById('valor2Criterio"+id+"').value;");
                eval("document.getElementById('cancelarDialogCriterio"+id+"').click();");
        }else {
            if (validarCamposObligatorios(id)) {
                var valida = true;
                eval("var operadorEntre = document.getElementById('valorOperCriterio"+id+"').value == 4;");

                if (operadorEntre) {
                   valida = validaNumericoAnterior(id);
                }                
                eval("listaCriInfArray["+pos+"][2] = document.getElementById('valorOperCriterio"+id+"').value;");
          
                if (valida) {
                    eval("listaCriInfArray["+pos+"][3] = document.getElementById('valor1Criterio"+id+"').value;");
                    if (operadorEntre) {
                        eval("listaCriInfArray["+pos+"][4] = document.getElementById('valor2Criterio"+id+"').value;");
                    }
                    eval("document.getElementById('cancelarDialogCriterio"+id+"').click();");
                }
            } else {
          	listaCriInfArray[pos][2]='null';
          	eval("document.getElementById('cancelarDialogCriterio"+id+"').click();");
            }
        }
    }

    function validarCampos() {
        var margenSup = document.forms[1].margenSup.value;
        var margenInf = document.forms[1].margenInf.value;
        var margenDer = document.forms[1].margenDer.value;
        var margenIzq = document.forms[1].margenIzq.value;
        var orientacion = document.forms[1].orientacion.value;
        if (margenSup!="" && margenInf!="" && margenDer!="" && margenIzq!="" && orientacion!="")
            return true;
        return false;
    }

    function pulsarModificar() {
        if (validarCampos()){
            //CRITERIOS INFORME
            var d2 = document.getElementById("listaCriInf2");
            new dojo.dnd.HtmlDropTarget(d2, ["li1"]);
            var lis2 = d2.getElementsByTagName("div");
            var listaCriInfCodigo="";
            var listaCriInfCondCri="";
            var listaCriInfValor1="";
            var listaCriInfValor2="";
            var listaCriInfTitulo="";
            var listaCriInfOrigen="";
            var listaCriInfTabla="";
            for(var x=0; x<lis2.length; x++){
                var item = lis2[x].id;
                listaCriInfCodigo += listaCriInfArray[item][0] + 'зе';
                listaCriInfTitulo += (listaCriInfArray[item][1]!='') ? listaCriInfArray[item][1] + 'зе' : 'null' + 'зе';
                listaCriInfCondCri += (listaCriInfArray[item][2]!='') ? listaCriInfArray[item][2] + 'зе' : 'null' + 'зе';
                listaCriInfValor1 += (listaCriInfArray[item][3]!='') ? listaCriInfArray[item][3] + 'зе' : 'null' + 'зе';
                listaCriInfValor2 += (listaCriInfArray[item][4]!='') ? listaCriInfArray[item][4] + 'зе' : 'null' + 'зе';
                listaCriInfOrigen += (listaCriInfArray[item][5]!='') ? listaCriInfArray[item][5] + 'зе' : 'null' + 'зе';
                listaCriInfTabla += (listaCriInfArray[item][6]!='') ? listaCriInfArray[item][6] + 'зе' : 'null' + 'зе';
               
            }
            document.forms[1].listaCriInfSeleccionadosCodigo.value =  listaCriInfCodigo;
            document.forms[1].listaCriInfSeleccionadosCondCri.value = listaCriInfCondCri;
            document.forms[1].listaCriInfSeleccionadosValor1.value = listaCriInfValor1;
            document.forms[1].listaCriInfSeleccionadosValor2.value = listaCriInfValor2;
            document.forms[1].listaCriInfSeleccionadosTitulo.value = listaCriInfTitulo;
            document.forms[1].listaCriInfSeleccionadosOrigen.value = listaCriInfOrigen;
            document.forms[1].listaCriInfSeleccionadosTabla.value = listaCriInfTabla;

            //CABECERA INFORME
            d2 = document.getElementById("listaCabInf2");
            new dojo.dnd.HtmlDropTarget(d2, ["li1"]);
            lis2 = d2.getElementsByTagName("div");
            var listaCabInfCodigo="";
            var listaCabInfNombre="";
            var listaCabInfPosx="";
            var listaCabInfPosy="";
            for(var x=0; x<lis2.length; x++){
                var item = lis2[x].id;
                listaCabInfCodigo += listaCabInfArray[item][0] + 'зе';
                listaCabInfPosx += listaCabInfArray[item][2] + 'зе';
                listaCabInfPosy += listaCabInfArray[item][3] + 'зе';
            }
            document.forms[1].listaCabInfSeleccionadosCodigo.value =  listaCabInfCodigo;
            document.forms[1].listaCabInfSeleccionadosPosx.value =  listaCabInfPosx;
            document.forms[1].listaCabInfSeleccionadosPosy.value =  listaCabInfPosy;
            //PIE INFORME
            d2 = document.getElementById("listaPieInf2");
            new dojo.dnd.HtmlDropTarget(d2, ["li1"]);
            lis2 = d2.getElementsByTagName("div");
            var listaPieInfCodigo="";
            for(var x=0; x<lis2.length; x++){
                var item = lis2[x].id;
                listaPieInfCodigo += listaPieInfArray[item][0] + 'зе';
            }
            document.forms[1].listaPieInfSeleccionadosCodigo.value =  listaPieInfCodigo;
            //CABECERA P┴GINA
            var d2 = document.getElementById("listaCabPag2");
            new dojo.dnd.HtmlDropTarget(d2, ["li1"]);
            var lis2 = d2.getElementsByTagName("div");
            var listaCabPagCodigo="";
            var listaCabPagNombre="";
            var listaCabPagPosx="";
            var listaCabPagPosy="";
            for(var x=0; x<lis2.length; x++){
                var item = lis2[x].id;
                listaCabPagCodigo += listaCabPagArray[item][0] + 'зе';
                listaCabPagPosx += listaCabPagArray[item][2] + 'зе';
                listaCabPagPosy += listaCabPagArray[item][3] + 'зе';
            }
            document.forms[1].listaCabPagSeleccionadosCodigo.value =  listaCabPagCodigo;
            document.forms[1].listaCabPagSeleccionadosPosx.value =  listaCabPagPosx;
            document.forms[1].listaCabPagSeleccionadosPosy.value =  listaCabPagPosy;
            //PIE PAGINA
            d2 = document.getElementById("listaPiePag2");
            new dojo.dnd.HtmlDropTarget(d2, ["li1"]);
            lis2 = d2.getElementsByTagName("div");
            var listaPiePagCodigo="";
            for(var x=0; x<lis2.length; x++){
                var item = lis2[x].id;
                listaPiePagCodigo += listaPiePagArray[item][0] + 'зе';
            }
            document.forms[1].listaPiePagSeleccionadosCodigo.value =  listaPiePagCodigo;
            //PERMISOS
            d2 = document.getElementById("listaPermisos2");
            new dojo.dnd.HtmlDropTarget(d2, ["li1"]);
            lis2 = d2.getElementsByTagName("div");
            var listaPermisosCodigo="";
            for(var x=0; x<lis2.length; x++){
                var item = lis2[x].id;
                listaPermisosCodigo += listaPermisosArray[item][0] + 'зе';
            }
            document.forms[1].listaPermisosSeleccionadosCodigo.value =  listaPermisosCodigo;
            //CUERPO : CAMPOS
            d2 = document.getElementById("listaCampos2");
            new dojo.dnd.HtmlDropTarget(d2, ["li1"]);
            lis2 = d2.getElementsByTagName("div");
            var listaCamposCodigo="";
            var listaCamposNombre="";
            var listaCamposTabla="";
            var listaCamposPosx="";
            var listaCamposPosy="";
            var listaCamposAlign="";
            var listaCamposAncho="";
            var listaCamposElipsis="";
            var listaCamposOrden="";
            for(var x=0; x<lis2.length; x++){
                var item = lis2[x].id;
                listaCamposCodigo += listaCamposArray[item][0] + 'зе';
                listaCamposNombre += listaCamposArray[item][1] + 'зе';
                listaCamposTabla += listaCamposArray[item][2] + 'зе';
                listaCamposPosx += listaCamposArray[item][3] + 'зе';
                listaCamposPosy += listaCamposArray[item][4] + 'зе';
                listaCamposAlign += listaCamposArray[item][5] + 'зе';
                listaCamposAncho += listaCamposArray[item][6] + 'зе';
                listaCamposElipsis += listaCamposArray[item][7] + 'зе';
                listaCamposOrden += listaCamposArray[item][9] + 'зе';
            }
            document.forms[1].listaCamposSeleccionadosCodigo.value =  listaCamposCodigo;
            document.forms[1].listaCamposSeleccionadosNombre.value =  listaCamposNombre;
            document.forms[1].listaCamposSeleccionadosTabla.value =  listaCamposTabla;
            document.forms[1].listaCamposSeleccionadosPosx.value =  listaCamposPosx;
            document.forms[1].listaCamposSeleccionadosPosy.value =  listaCamposPosy;
            document.forms[1].listaCamposSeleccionadosAlign.value =  listaCamposAlign;
            document.forms[1].listaCamposSeleccionadosAncho.value =  listaCamposAncho;
            document.forms[1].listaCamposSeleccionadosElipsis.value =  listaCamposElipsis;
                document.forms[1].listaCamposSeleccionadosOrden.value =  listaCamposOrden;
            document.forms[1].opcion.value = "grabarInforme";
            document.forms[1].action = DOMAIN_NAME+"<c:url value='/gestionInformes/FichaInforme.do'/>";
            document.forms[1].submit();
        } else {
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
        }
    }

    function pulsarCancelar() {
        document.forms[1].opcion.value = "inicio";
        document.forms[1].action =DOMAIN_NAME+ "<c:url value='/gestionInformes/GestionInformes.do'/>";
        document.forms[1].submit();
    }

    function comprobarOperFecha(id) {
        comprobarVisibilidadNulos(id);
        eval("var opcionEntre = document.getElementById('valorOperCriterio"+id+"').value==4;");
       if (opcionEntre) {
            eval("document.getElementById('valor2Criterio"+id+"').style.visibility='visible';");
        } else {
            eval("document.getElementById('valor2Criterio"+id+"').style.visibility='hidden';");
            eval("document.getElementById('valor2Criterio"+id+"').value = '';");
        }
    }

    function comprobarOperNumerico(id) {
        comprobarVisibilidadNulos(id);
        eval("var opcionEntre = document.getElementById('valorOperCriterio"+id+"').value==4;");
        if (opcionEntre) {
            eval("document.getElementById('valor2Criterio"+id+"').style.visibility='visible';");
        } else {
            eval("document.getElementById('valor2Criterio"+id+"').style.visibility='hidden';");
            eval("document.getElementById('valor2Criterio"+id+"').value = '';");
        }
    }

    function comprobarOperTexto(campo) {
        comprobarVisibilidadNulos(campo);
    }

    function comprobarOperInteresado(campo) {
        comprobarVisibilidadNulosInteresado(campo);
    }

    function comprobarVisibilidadNulos(campo) {
        eval("var nulos= document.getElementById('valorOperCriterio"+campo+"').value=='8';");
        eval("nulos = nulos ||  document.getElementById('valorOperCriterio"+campo+"').value=='9';");
        if (nulos)
            eval("document.getElementById('valor1Criterio"+campo+"').style.visibility='hidden';");
        else
            eval("document.getElementById('valor1Criterio"+campo+"').style.visibility='visible';");
    }

    function comprobarVisibilidadNulosInteresado(campo) {
        eval("var nulos= document.getElementById('valorOperCriterio"+campo+"').value=='8';");
        eval("nulos = nulos ||  document.getElementById('valorOperCriterio"+campo+"').value=='9';");
        if (nulos) {
            eval("document.getElementById('valor1Criterio"+campo+"').style.visibility='hidden';");
            eval("document.getElementById('botonBuscar').style.visibility='hidden';");
        }
        else {
            eval("document.getElementById('valor1Criterio"+campo+"').style.visibility='visible';");
            eval("document.getElementById('botonBuscar').style.visibility='visible';");
        }
    }


    function pulsarBuscarInteresado(campo) {
        var source = DOMAIN_NAME + "<c:url value='/BusquedaTerceros.do?opcion=inicializar&ventana=true&destino=seleccionDesdeInformes'/>";
        var argumentos = new Array();
        argumentos['modo'] = 'seleccion';
        var terceros = new Array();
        argumentos['terceros'] = terceros;        
        abrirXanelaAuxiliar(DOMAIN_NAME + "<c:url value='/jsp/terceros/mainVentana.jsp?source='/>" + source,argumentos,
	'width=990,height=650,status='+ '<%=statusBar%>',function(datos){
                        if (datos != null) {            
                            //aqui va el nombre
                            eval ("document.getElementById('valor1Criterio"+campo+"').value = '" + datos[6] +"';");
                            //aqui va el codigo
                            eval ("document.getElementById('valor2Criterio"+campo+"').value = '" + datos[0] +"';");
                        }
                    });
    }

    function limpiarCriterio(campo) {
        eval ("document.getElementById('valor1Criterio"+campo+"').value = ''");
        eval ("document.getElementById('valor2Criterio"+campo+"').value = ''");
        eval ("document.getElementById('valorOperCriterio"+campo+"').value = ''");

    }
</script>

<div class="fichaGestionInformes">
<html:form action="/gestionInformes/FichaInforme.do" target="_self">

<!-- VENTANA DE CAMPOS -->
<div class="dialogCombo" dojoType="dialog" id="DialogCampo" bgColor="white" bgOpacity="0.5" toggle="fade" toggleDuration="250">
    <div class="tableContainerComboCampos" style="text-align:left">
        <input type="hidden" name="idCampoActual" id="idCampoActual" disabled>
        <div class="tableContainerBloque">
            <span class="etiqBold"><%=descriptor.getDescripcion("gbInformeCampo").toUpperCase()%></span>
            <input type="text" name="valorCampoActual" id="valorCampoActual" disabled class="inputTexto" style="width:225px">
        </div>
        <div class="tableContainerBloque">
            <div class="tableContainerComboCamposDefinicion">
                <span class="etiqBold"><%=descriptor.getDescripcion("gbInformeParam").toUpperCase()%></span>
            </div>
            <div class="tableContainerComboCamposParametro">
                <span class="etiq"><%=descriptor.getDescripcion("gbInformeTitCol")%></span>
                <input type="text" name="tituloCampoActual" id="tituloCampoActual" class="inputTextoObligatorio" style="width:175px">
            </div>
            <div class="tableContainerComboCamposParametro">
                <span class="etiq"><%=descriptor.getDescripcion("gbInformeAncCol")%></span>
                <input type="text" name="anchoCampoActual" id="anchoCampoActual" class="inputTextoObligatorio" style="width:175px" onkeypress="javascript:return SoloDigitos(event);">
            </div>
            <div class="tableContainerComboCamposParametro">
                <span class="etiq"><%=descriptor.getDescripcion("gbInformeAlgCol")%></span>
                <select name="alignCampoActual"  id="alignCampoActual" class="inputTextoObligatorio" style="width:175px">
                    <option value="LEFT"><%=descriptor.getDescripcion("gbInformeIzq")%></option>
                    <option value="RIGHT"><%=descriptor.getDescripcion("gbInformeDcha")%></option>
                    <option value="CENTER"><%=descriptor.getDescripcion("gbInformeCent")%></option>
                </select>
            </div>
             <div class="tableContainerComboCamposParametro">
                <span class="etiq">Ordenaciєn</span>
                <select name="ordenCampoActual" id="ordenCampoActual" class="inputTextoObligatorio" style="width:175px">
                    <option value="0">Ninguna</option>
                    <option value="1">Ascendente</option>
                    <option value="2">Descendente</option>
                </select>
            </div>
            <div class="tableContainerComboCamposParametro">
                <input type="checkbox" name="elipsisCampoActual" id="elipsisCampoActual" class="inputTextoObligatorio">
                <%=descriptor.getDescripcion("gbInformeElipsis")%>
            </div>
        </div>
        
    </div>
    <div>
        <input type= "button" id="aceptarDialogCampo" class="botonAplicacion" value="Aceptar" name="aceptarDialogCampo" onclick="actualizarCampo();">
        <input type= "button" id="cancelarDialogCampo" class="botonAplicacion" value="Cancelar" name="cancelarDialogCampo">
    </div>
</div>
<!-- VENTANA DE CAMPOS -->

<%  if (listaCriInfDisponibles != null)
    for (int i=0;i<listaCriInfDisponibles.size();i++) {
        campoVO = (CampoValueObject) listaCriInfDisponibles.get(i); %>
        <div class="dialogCriterio" dojoType="dialog" id="DialogCriterio<%=campoVO.getIdCampo()%>" bgColor="white" bgOpacity="0.5" toggle="fade" toggleDuration="250">
            <div id="criterio<%=campoVO.getIdCampo()%>" class="criterio">
                <jsp:include page="criterioGeneral.jsp" flush="true">
                    <jsp:param name="campo" value="<%=campoVO.getIdCampo()%>"/>
                    <jsp:param name="titulo" value="<%=campoVO.getTitulo()%>"/>
                    <jsp:param name="tabla" value="<%=campoVO.getTabla()%>"/>                    
                    <jsp:param name="codigoCampoDesplegable" value="<%=campoVO.getValor2Criterio()%>"/>
                    <jsp:param name="formulario" value='<%="FichaInformeForm"%>'/>
                    <jsp:param name="tipo" value="<%=campoVO.getTipo()%>"/>
                </jsp:include>
            </div>
            <div>
                <input type= "button" id="aceptarDialogCriterio<%=campoVO.getIdCampo()%>"  class="botonCriterio" value="Aceptar" name="aceptarDialogCriterio<%=campoVO.getIdCampo()%>" onclick="actualizarCriterio(<%=campoVO.getIdCampo()%>);">
                <input type= "button" id="cancelarDialogCriterio<%=campoVO.getIdCampo()%>"  class="botonCriterio" value="Cancelar" name="cancelarDialogCriterio<%=campoVO.getIdCampo()%>">
              <% if ("I".equals(campoVO.getTipo())){ %>
                <input type= "button" id="limpiarDialogCriterio<%=campoVO.getIdCampo()%>"  class="botonCriterio" value="Limpiar" name="limpiarDialogCriterio<%=campoVO.getIdCampo()%>" onclick="limpiarCriterio(<%=campoVO.getIdCampo()%>);">
              <%}%>

            </div>
        </div>
 <% } %>

<%  if (listaCriInfSeleccionados != null)
    for (int i=0;i<listaCriInfSeleccionados.size();i++) {
        campoVO = (CampoValueObject) listaCriInfSeleccionados.get(i); %>
        <div class="dialogCriterio" dojoType="dialog" id="DialogCriterio<%=campoVO.getIdCampo()%>" bgColor="white" bgOpacity="0.5" toggle="fade" toggleDuration="250">
            <div id="criterio<%=campoVO.getIdCampo()%>" class="criterio">
                <jsp:include page="criterioGeneral.jsp" flush="true">
                    <jsp:param name="campo" value="<%=campoVO.getIdCampo()%>"/>
                    <jsp:param name="titulo" value="<%=campoVO.getTitulo()%>"/>
                    <jsp:param name="tabla" value="<%=campoVO.getTabla()%>"/>
                    <jsp:param name="codigoCampoDesplegable" value="<%=campoVO.getValor2Criterio()%>"/>
                    <jsp:param name="formulario" value='<%="FichaInformeForm"%>'/>
                    <jsp:param name="tipo" value="<%=campoVO.getTipo()%>"/>
                </jsp:include>
            </div>
            <div>
                <input type= "button" id="aceptarDialogCriterio<%=campoVO.getIdCampo()%>"  class="botonCriterio" value="Aceptar" name="aceptarDialogCriterio<%=campoVO.getIdCampo()%>" onclick="actualizarCriterio(<%=campoVO.getIdCampo()%>);">
                <input type= "button" id="cancelarDialogCriterio<%=campoVO.getIdCampo()%>"  class="botonCriterio" value="Cancelar" name="cancelarDialogCriterio<%=campoVO.getIdCampo()%>">
              <% if ("I".equals(campoVO.getTipo())){ %>
                <input type= "button" id="limpiarDialogCriterio<%=campoVO.getIdCampo()%>"  class="botonCriterio" value="Limpiar" name="limpiarDialogCriterio<%=campoVO.getIdCampo()%>" onclick="limpiarCriterio(<%=campoVO.getIdCampo()%>);">
              <%}%>
            </div>
        </div>
 <% } %>


    <input type="hidden" name="listaCamposSeleccionadosCodigo" id="listaCamposSeleccionadosCodigo">
    <input type="hidden" name="listaCamposSeleccionadosNombre" id="listaCamposSeleccionadosNombre">
    <input type="hidden" name="listaCamposSeleccionadosTabla" id="listaCamposSeleccionadosTabla">
    <input type="hidden" name="listaCamposSeleccionadosPosx" id="listaCamposSeleccionadosPosx">
    <input type="hidden" name="listaCamposSeleccionadosPosy" id="listaCamposSeleccionadosPosy">
    <input type="hidden" name="listaCamposSeleccionadosAlign" id="listaCamposSeleccionadosAlign">
    <input type="hidden" name="listaCamposSeleccionadosAncho" id="listaCamposSeleccionadosAncho">
    <input type="hidden" name="listaCamposSeleccionadosElipsis" id="listaCamposSeleccionadosElipsis">
    <input type="hidden" name="listaCamposSeleccionadosOrden" id="listaCamposSeleccionadosOrden">
    <input type="hidden" name="listaCriInfSeleccionadosCodigo" id="listaCriInfSeleccionadosCodigo">
    <input type="hidden" name="listaCriInfSeleccionadosCondCri" id="listaCriInfSeleccionadosCondCri">
    <input type="hidden" name="listaCriInfSeleccionadosValor1" id="listaCriInfSeleccionadosValor1">
    <input type="hidden" name="listaCriInfSeleccionadosValor2" id="listaCriInfSeleccionadosValor2">
    <input type="hidden" name="listaCriInfSeleccionadosTitulo" id="listaCriInfSeleccionadosTitulo">
    <input type="hidden" name="listaCriInfSeleccionadosOrigen" id="listaCriInfSeleccionadosOrigen">
    <input type="hidden" name="listaCriInfSeleccionadosTabla" id="listaCriInfSeleccionadosTabla">
    <input type="hidden" name="listaCabInfSeleccionadosCodigo" id="listaCabInfSeleccionadosCodigo">
    <input type="hidden" name="listaCabInfSeleccionadosPosx" id="listaCabInfSeleccionadosPosx">
    <input type="hidden" name="listaCabInfSeleccionadosPosy" id="listaCabInfSeleccionadosPosy">
    <input type="hidden" name="listaPieInfSeleccionadosCodigo" id="listaPieInfSeleccionadosCodigo">
    <input type="hidden" name="listaCabPagSeleccionadosCodigo" id="listaCabPagSeleccionadosCodigo">
    <input type="hidden" name="listaCabPagSeleccionadosPosx" id="listaCabPagSeleccionadosPosx">
    <input type="hidden" name="listaCabPagSeleccionadosPosy" id="listaCabPagSeleccionadosPosy">
    <input type="hidden" name="listaPiePagSeleccionadosCodigo" id="listaPiePagSeleccionadosCodigo">
    <input type="hidden" name="listaPermisosSeleccionadosCodigo" id="listaPermisosSeleccionadosCodigo">
    <html:hidden property="opcion" />
    <html:hidden property="codPlantilla" />
    <div class="cabecera">
        <table style="width:100%">
            <tr>
                <td><span class="etiq"><%=descriptor.getDescripcion("gbInformeNombre")%></span></td>
                <td>
                    <html:text styleId="nombre" property="nombre" styleClass="inputTextoObligatorio" size="120" style="width:70%" readonly="true" />
                    <span class="etiq" style="margin-left:1%"><%=descriptor.getDescripcion("gbInformeAmbito")%></span>
                    <html:text styleId="nombre" property="descAmbito" styleClass="inputTextoObligatorio" size="15" style="width:10%" readonly="true" />
                </td>
            </tr>
            <tr>
                <td><span class="etiq"><%=descriptor.getDescripcion("gbInformeProc")%></span></td>
                <td>
                    <html:text styleId="codProcedimiento" property="codProcedimiento" styleClass="inputTextoObligatorio" style="width:5%" readonly="true" />
                    <html:text styleId="descProcedimiento" property="descProcedimiento" styleClass="inputTextoObligatorio" style="width:50%" readonly="true" />
                    <span class="etiq" style="margin-left:1%"><%=descriptor.getDescripcion("gbInformePub")%></span>
                    <html:checkbox property="publicado" styleId="publicado" styleClass="inputTextoObligatorio" disabled="true" />
                </td>
            </tr>
        </table>
    </div>
    <div id="mainTabContainer" dojoType="TabContainer" style="width: 100%; height:300px" selectedTab="tab1">
        <div class="contenidoTab" id="tab1" dojoType="ContentPane" label="<%=descriptor.getDescripcion("labelGIGeneral")%>">


            <div class="contenidoTabcampo">
            <span class="etiqBoldMaxi2" ><%=descriptor.getDescripcion("gbInformePapel")%></span>
                <select name="papel" id="papel" class="inputTextoObligatorio" style="width:200px">
                    <option value="A4">A4 : 210 mm x 297 mm</option>
                    <option value="A3">A3 : 297 mm x 420 mm</option>
                </select>
            </div>
            


            <div class="contenidoTabcampo">
                <span class="etiqBoldMaxi2" ><%=descriptor.getDescripcion("gbInformeOrient")%></span>
                <label for="orientacion0"><html:radio property="orientacion" value="Horizontal" styleClass="inputTextoObligatorio" styleId="orientacion0" /> Horizontal</label>
                <label for="orientacion1"><html:radio property="orientacion" value="Vertical" styleClass="inputTextoObligatorio" styleId="orientacion1" /> Vertical</label>
            </div>



            <div class="contenidoTabcampo">
                <span class="etiqBoldMaxi2" ><%=descriptor.getDescripcion("gbInformeMargSup")%></span>
                <html:text styleId="margenSup" property="margenSup" styleClass="inputTextoObligatorio" size="10" onkeypress="javascript:return SoloDigitos(event);" />
            </div>

            <div class="contenidoTabcampo">
                <span class="etiqBoldMaxi2" ><%=descriptor.getDescripcion("gbInformeMargInf")%></span>
                <html:text styleId="margenInf" property="margenInf" styleClass="inputTextoObligatorio" size="10" onkeypress="javascript:return SoloDigitos(event);" />
            </div>
            <div class="contenidoTabcampo">
                <span class="etiqBoldMaxi2" ><%=descriptor.getDescripcion("gbInformeMargIzq")%></span>
                <html:text styleId="margenIzq" property="margenIzq" styleClass="inputTextoObligatorio" size="10" onkeypress="javascript:return SoloDigitos(event);" />
            </div>
            <div class="contenidoTabcampo">
                <span class="etiqBoldMaxi2" ><%=descriptor.getDescripcion("gbInformeMargDer")%></span>
                <html:text styleId="margenDer" property="margenDer" styleClass="inputTextoObligatorio" size="10" onkeypress="javascript:return SoloDigitos(event);" />
            </div>
        </div>
        <div dojoType="ContentPane" label="<%=descriptor.getDescripcion("gbInformeCriSel")%>" style="width:800px;">
            <div class="contenidoTab" id="tab2" dojoType="ContentPane" label="<%=descriptor.getDescripcion("gbInformeCriSel")%>" align="center">
                
                        <div class="camposSeleccionables" style="height:240px;width:50%;" align="center">
                            <span class="etiqBold" style="float:center;"><%=descriptor.getDescripcion("gbInformeCriDisp")%></span>
                            <div align="center">
                                <ul id="listaCriInf1" class="listaCampos">
                                </ul>
                            </div>
                        </div>
                
                        <div class="camposSeleccionables"  style="height:240px;" align="right">
                            <span class="etiqBold"><%=descriptor.getDescripcion("gbInformeCriUso")%></span>
                            <div align="center">
                                <ul id="listaCriInf2" class="listaCampos">
                                </ul>
                            </div>
                        </div>

                <%--
                <div class="camposSeleccionables" style="border: 1px solid red; height:257px;">
                   
                    <span class="etiqBold" style="float:center;"><%=descriptor.getDescripcion("gbInformeCriDisp")%></span>
                   
                    <ul id="listaCriInf1" class="listaCampos">
                    </ul>
                </div>
                <div class="camposSeleccionables"  style="border: 1px solid red; height:257px;" align="right">
                    
                    <span class="etiqBold"><%=descriptor.getDescripcion("gbInformeCriUso")%></span>
                    
                    <ul id="listaCriInf2" class="listaCampos">
                    </ul>
                </div>

                --%>
                <div class="contenidoEtiq" style="float:left;">
                    <%=descriptor.getDescripcion("msgArrastrar")%>
                </div>
            </div>
        </div>
        <div dojoType="ContentPane" label="<%=descriptor.getDescripcion("labelGICabInforme")%>">
            <div class="contenidoTab" id="tab3" dojoType="ContentPane" label="<%=descriptor.getDescripcion("gbInformeCabInf")%>">
                <div class="camposSeleccionables" style="height:240px;width:50%;" align="center">
                    <span class="etiqBold"><%=descriptor.getDescripcion("gbInformeCriDisp")%></span>
                    <div align="center">
                    <ul id="listaCabInf1" class="listaCampos">
                    </ul>
                </div>
                </div>
                <div class="camposSeleccionables" style="height:240px;" align="right">
                    <span class="etiqBold"><%=descriptor.getDescripcion("gbInformeCriUso")%></span>
                    <div align="center">
                    <ul id="listaCabInf2" class="listaCampos">
                    </ul>
                </div>
                </div>
                <div class="contenidoEtiq" style="float:left;">
                    <%=descriptor.getDescripcion("msgArrastrar")%>
                </div>
            </div>
        </div>
        <div dojoType="ContentPane" label="<%=descriptor.getDescripcion("labelGICabPagina")%>">
            <div class="contenidoTab" id="tab4" dojoType="ContentPane" label="<%=descriptor.getDescripcion("gbInformeCabPag")%>">
                <div class="camposSeleccionables" style="height:240px;width:50%;" align="center">
                    <span class="etiqBold"><%=descriptor.getDescripcion("gbInformeCriDisp")%></span>
                    <div align="center">
                    <ul id="listaCabPag1" class="listaCampos">
                    </ul>
                </div>
                </div>
                <div class="camposSeleccionables" style="height:240px;" align="right">
                    <span class="etiqBold"><%=descriptor.getDescripcion("gbInformeCriUso")%></span>
                    <div align="center">
                    <ul id="listaCabPag2" class="listaCampos">
                    </ul>
                </div>
                </div>
                <div class="contenidoEtiq">
                    <%=descriptor.getDescripcion("msgArrastrar")%>
                </div>
            </div>
        </div>
        <div dojoType="ContentPane" label="<%=descriptor.getDescripcion("labelGICuerpo")%>">
            <div class="contenidoTab" id="tab5" dojoType="ContentPane" label="<%=descriptor.getDescripcion("gbInformeCampos")%>">
                <div class="camposSeleccionables" style="height:240px;width:50%;" align="center">
                    <span class="etiqBold"><%=descriptor.getDescripcion("gbInformeCriDisp")%></span>
                    <div align="center">
                    <ul id="listaCampos1" class="listaCampos">
                    </ul>
                </div>
                </div>
                <div class="camposSeleccionables"  style="height:240px;" align="right">
                    <span class="etiqBold"><%=descriptor.getDescripcion("gbInformeCriUso")%></span>
                    <div align="center">
                    <ul id="listaCampos2" class="listaCampos">
                    </ul>
                </div>
                </div>
                <div class="contenidoEtiq">
                    <%=descriptor.getDescripcion("msgArrastrar")%>
                </div>
            </div>
        </div>
        <div dojoType="ContentPane" label="<%=descriptor.getDescripcion("labelGIPieInforme")%>">
            <div class="contenidoTab" id="tab6" dojoType="ContentPane" label="<%=descriptor.getDescripcion("gbInformePieInf")%>">
                <div class="camposSeleccionables" style="height:240px;width:50%;" align="center">
                    <span class="etiqBold"><%=descriptor.getDescripcion("gbInformeCriDisp")%></span>
                    <div align="center">
                    <ul id="listaPieInf1" class="listaCampos">
                    </ul>
                </div>
                </div>
                <div class="camposSeleccionables" style="height:240px;" align="right">
                    <span class="etiqBold"><%=descriptor.getDescripcion("gbInformeCriUso")%></span>
                    <div align="center">
                    <ul id="listaPieInf2" class="listaCampos">
                    </ul>
                </div>
                </div>
                <div class="contenidoEtiq">
                    <%=descriptor.getDescripcion("msgArrastrar")%>
                </div>
            </div>
        </div>
        <div dojoType="ContentPane" label="<%=descriptor.getDescripcion("labelGIPiePagina")%>">
            <div class="contenidoTab" id="tab7" dojoType="ContentPane" label="<%=descriptor.getDescripcion("gbInformePiePag")%>">
                <div class="camposSeleccionables"  style="height:240px;width:50%;" align="center">
                    <span class="etiqBold"><%=descriptor.getDescripcion("gbInformeCriDisp")%></span>
                    <div align="center">
                    <ul id="listaPiePag1" class="listaCampos">
                    </ul>
                </div>
                </div>
                <div class="camposSeleccionables"  style="height:240px;" align="right">
                    <span class="etiqBold"><%=descriptor.getDescripcion("gbInformeCriUso")%></span>
                    <div align="center">
                    <ul id="listaPiePag2" class="listaCampos">
                    </ul>
                </div>
                </div>
                <div class="contenidoEtiq">
                    <%=descriptor.getDescripcion("msgArrastrar")%>
                </div>
            </div>
        </div>
        <div dojoType="ContentPane" label="<%=descriptor.getDescripcion("labelGIPermisos")%>">
            <div class="contenidoTab" id="tab8" dojoType="ContentPane" label="<%=descriptor.getDescripcion("gbInformePermisos")%>">
                <div class="camposSeleccionables" style="height:240px;width:50%;" align="center">
                    <span class="etiqBold"><%=descriptor.getDescripcion("gbInformeCriDisp")%></span>
                    <div align="center">
                    <ul id="listaPermisos1" class="listaCampos">
                    </ul>
                </div>
                </div>
                <div class="camposSeleccionables" style="height:240px;" align="right">
                    <span class="etiqBold"><%=descriptor.getDescripcion("gbInformeCriUso")%></span>
                    <div align="center">
                    <ul id="listaPermisos2" class="listaCampos">
                    </ul>
                </div>
                </div>
                <div class="contenidoEtiq">
                    <%=descriptor.getDescripcion("msgArrastrar")%>
                </div>
            </div>
        </div>
    </div>
</html:form>
</div>

<script type="text/javascript">
    inicializar();
</script>
