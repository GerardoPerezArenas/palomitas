<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionExpedientesForm" %>
<%@page import="java.util.Vector" %>
<%@page import="java.util.Hashtable" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>


<html:html>

<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

<TITLE>::: Información sobre los trámites :::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />

<!-- Estilos -->

<%
  int idioma=1;
  int apl=1;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
        }
  }

  TramitacionExpedientesForm teForm = (TramitacionExpedientesForm)session.getAttribute("TramitacionExpedientesForm");
  Hashtable<String,GeneralValueObject> errores = teForm.getListaTramitesCondEntradaNoCumplidas();
   
  Log log = LogFactory.getLog(this.getClass().getName());


  Vector listado = new Vector();
  java.util.Enumeration e = errores.keys();
  Object obj;
  
  while (e.hasMoreElements()) {
    obj = e.nextElement();
    GeneralValueObject gvo = (GeneralValueObject)errores.get(obj);
    listado.add(gvo);

  }

%>


<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>

<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">

<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>



<SCRIPT type="text/javascript">

var htmlString = "";

function inicializar() {
  var argVentana = self.parent.opener.xanelaAuxiliarArgs;
  var lista = new Array();
  var j = 0;
  lista[0] = argVentana[0];
  for(i=1; i<argVentana.length; i++) {
    if(argVentana[i][9] != argVentana[i-1][9]) {
      domlay('capaEscritura',1,0,0,escribir(lista));
      lista = new Array();
    }
    j = lista.length;
    lista[j] = argVentana[i];
  }
  domlay('capaEscritura',1,0,0,escribir(lista));
}

function escribir(lista) {
  if(htmlString == "") {
    htmlString = '<table border="0px" cellpadding="4" cellspacing="0" align="center">';
  } else {
    htmlString += '<table border="0px" cellpadding="4" cellspacing="0" align="center">';
  }
        

  var exp = " <%=descriptor.getDescripcion("gEtiq_exped")%> ";
  exp = exp.toUpperCase();
  htmlString += '<tr><td class="etiqueta" colspan="3">' + exp + '</td></tr>';
  htmlString += '<tr><td class="etiqueta" colspan="3">' + "* <%=descriptor.getDescripcion("msgNoIniTram")%> " + lista[0][2] + " <%=descriptor.getDescripcion("msgxq")%>:";
  htmlString += '</td></tr>';
  for(var k=0; k<lista.length; k++) {      
    if (lista[k][3]=="ESTADO_TRAMITE") {
        htmlString += '<tr><td class="columnP" width="10%"></td><td colspan="2" class="etiqueta" width="90%">' +
            "- El trámite " + lista[k][4] + " no está " + lista[k][5] + ".";

    }
    else if (lista[k][3]=="EXPRESION") {
        htmlString += '</td></tr>';
        var nombresCampos = new Array();
        nombresCampos = lista[k][6];
        if (nombresCampos.length > 1) {
            htmlString += '<tr><td class="columnP" width="10%"></td><td colspan="2" class="etiqueta" width="90%">' +
                "- <%=descriptor.getDescripcion("msgNoCondEnts")%>:";
        } else {
            htmlString += '<tr><td class="columnP" width="10%"></td><td colspan="2" class="etiqueta" width="90%">' +
                "- <%=descriptor.getDescripcion("msgNoCondEnt")%>:";
        }
        for (var x = 0; x < nombresCampos.length; x++) {
          htmlString += '<tr><td colspan="2" class="columnP" width="20%"></td><td class="etiqueta" width="80%">' +
                '· ' + nombresCampos[x] + '</td></tr>';
        }
    }
    else if (lista[k][3]=="FIRMA") {
        htmlString += '<tr><td class="columnP" width="10%"></td><td colspan="2" class="etiqueta" width="90%">' +
            "- El documento " + lista[k][7] + " del trámite " + lista[k][4] + " no está " + lista[k][8] + ".";

    }    
    htmlString += '</td></tr>';

  }


  htmlString += '<tr><td height="2px" colspan="2"></td></tr>';
  htmlString += '</table>';
  return htmlString;
}

function pulsarAceptar() {
    var retorno = new Array();
    self.parent.opener.retornoXanelaAuxiliar(retorno);
}

function mostrarCapasBotones(nombreCapa) {
  document.getElementById('capaResolucion').style.visibility='hidden';
  document.getElementById(nombreCapa).style.visibility='visible';
}


 
function vaciarListaErrores()
{  
   var ajax = getXMLHttpRequest();
   var url = "<%= request.getContextPath() %>" + "/sge/TramitacionExpedientes.do";
   var parametros = "opcion=vaciarListadoErroresCondicionesEntrada";
   ajax.open("POST",url,false); // Llamada síncrona, mientras el servidor no de una respuesta no se continua con la ejecución
   ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
   ajax.send(parametros);
   var res = ajax.responseText;
   if(res.indexOf("si")!=-1)
       return true;
   else return false;
}

function getXMLHttpRequest(){
    var aVersions = [ "MSXML2.XMLHttp.5.0",
    "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
    "MSXML2.XMLHttp","Microsoft.XMLHttp"
    ];

    if (window.XMLHttpRequest){
        // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
        return new XMLHttpRequest();
    }
    else
        if (window.ActiveXObject){
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


</SCRIPT>

</head>    
<BODY scroll="auto">
<html:form action="/sge/TramitacionExpedientes.do" target="_self">

<html:hidden  property="opcion" value=""/>

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titInfTram")%></div>
<div class="contenidoPantalla">
    <TABLE cellspacing="10px" height="30%" border="0" cellpadding="0px" border="0px" style="margin-bottom:10px" class="cuadroFondoBlanco">
        <TR>
            <TD class="etiqueta" align="left">

                <div id="capaEscritura" class="text" style="width:100%;">

                    <table border="0" align="left" width="70%" height="50%">
                    <%
                        for(int h=0;h<listado.size();h++) {
                            GeneralValueObject gvo = (GeneralValueObject)listado.get(h);
                            Vector listaCondiciones = (Vector)gvo.getAtributo("listaCondiciones");                                                            
                     %>
                            <tr>
                                <td class="etiqueta"><li><%=descriptor.getDescripcion("msgNoIniTram")%> <%= (String)gvo.getAtributo("descTramite") %> &nbsp;<%=descriptor.getDescripcion("msgxq")%>:
                                </td>
                            </tr>                                                           
                            <%
                                if(listaCondiciones!=null && (listaCondiciones instanceof Vector) && listaCondiciones.size()>=1) {
                                    Vector erroresExpresion = new Vector();
                                    Vector erroresEstado = new Vector();
                                    Vector erroresFirmaDoc = new Vector();
                                    for(int ff=0;ff<listaCondiciones.size();ff++) {
                                        GeneralValueObject condicion = (GeneralValueObject)listaCondiciones.get(ff);
                                        String tipoCondicion = (String)condicion.getAtributo("tipoCondicion");
                                        if (tipoCondicion.equals("ESTADO_TRAMITE")) erroresEstado.add(condicion);
                                        else if (tipoCondicion.equals("EXPRESION")) erroresExpresion.add(condicion);
                                        else if (tipoCondicion.equals("FIRMA")) erroresFirmaDoc.add(condicion);
                                    }

                                    if (erroresExpresion.size() > 0) {
                            %>
                                <tr>
                                    <td class="etiqueta">&nbsp;&nbsp;<%=descriptor.getDescripcion("msgNoCondEnt")%>
                                    </td>
                                </tr>

                            <%
                                    for(int ff=0;ff<erroresExpresion.size();ff++) {                                                                        
                                        GeneralValueObject condicion = (GeneralValueObject)erroresExpresion.get(ff);
                                        Vector nombresCampos = (Vector)condicion.getAtributo("nombresCampos");
                            %>
                            <tr>
                                <td class="etiqueta">&nbsp;&nbsp; 
                                <%
                                    if (nombresCampos != null) {
                                        for(int gg=0;gg<nombresCampos.size();gg++) {
                                            out.print(nombresCampos.get(gg) + "&nbsp;");
                                        }
                                    }
                                 %>
                                </td>
                            </tr>
                            <%
                                    }// for
                                }
                                    if (erroresEstado.size() > 0) {
                                        for(int ff=0;ff<erroresEstado.size();ff++) {
                                            GeneralValueObject condicion = (GeneralValueObject)erroresEstado.get(ff);                                                                            
%>
                                <tr>
                                    <td class="etiqueta">&nbsp;&nbsp;El tramite <%=condicion.getAtributo("descTramite")%> no esta <%=condicion.getAtributo("estadoTramite")%>.
                                    </td>
                                </tr>

<%
                                        }
                                    }
                                    if (erroresFirmaDoc.size() > 0) {
                                        for(int ff=0;ff<erroresFirmaDoc.size();ff++) {
                                            GeneralValueObject condicion = (GeneralValueObject)erroresFirmaDoc.get(ff);
                                            String estado = (String)condicion.getAtributo("estadoFirma");
                                            String textoEstado="";
                                            if ("F".equals(estado)) textoEstado= descriptor.getDescripcion("gEtiq_estadoFirmaF");
                                            else if ("R".equals(estado)) textoEstado= descriptor.getDescripcion("gEtiq_estadoFirmaR");
                                            else if ("O".equals(estado)) textoEstado= descriptor.getDescripcion("gEtiq_estadoFirmaP");
                                            else if ("C".equals(estado)) textoEstado= descriptor.getDescripcion("gEtiq_estadoFirmaC");
                                            if(textoEstado==null)textoEstado="";
%>
                                <tr>
                                    <td class="etiqueta">
                                        &nbsp;&nbsp;- <%=descriptor.getDescripcion("etiqElDocumento")%> <%=condicion.getAtributo("descDocumento")%> <%=descriptor.getDescripcion("etiqNoEsta")%> <%=textoEstado.toLowerCase()%>.
                                    </td>
                                </tr>

<%
                                        }
                                    }

                                }
                             %>

                    <%
                        }
                    %>
                    </table>

                </div>

            </TD>
        </TR>
    </table>
    <div class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar" onclick="pulsarAceptar();">
    </div>
</div>
</html:form>

<script type="text/javascript">

vaciarListaErrores();
document.onmousedown = checkKeys;

function checkKeysLocal(evento,tecla) {
   if(window.event) evento = window.event;
  keyDel(evento);
}

</script>
</BODY>
</html:html>
