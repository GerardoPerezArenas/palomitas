
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<html>
<head>
<TITLE>::: LISTA INTERESADOS:::</TITLE>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />

<%
    int idioma = 1;
    int apl = 1;
    UsuarioValueObject usuario = new UsuarioValueObject();
    if (session != null) {
        usuario = (UsuarioValueObject) session.getAttribute("usuario");
        if (usuario != null) {
            idioma = usuario.getIdioma();
        }
    }
%>

<%!
  // Funcion para escapar strings para javascript
  private String escape(String str) {
      return StringEscapeUtils.escapeJavaScript(str);
  }
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
             type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>" type="text/css">

<script type="text/javascript">
            
var elementoSeleccionado = false;
var listaDomicilios = new Array();

function inicializar() {
    window.focus();
    var terceros =   self.parent.opener.xanelaAuxiliarArgs;
    var listaTabla = new Array();
    for (var i=0; i<terceros.length; i++) {
        listaTabla[i] = [terceros[i][15],terceros[i][2],terceros[i][5],terceros[i][3]];
    }
    tab.lineas=listaTabla;
    tab.displayTabla();
 }

function pulsarSeleccionar(posicion) {     
    if(!elementoSeleccionado && posicion==undefined && tab.selectedIndex == -1) {
        jsp_alerta('A', '<%=escape(descriptor.getDescripcion("msjNoSelecFila"))%>');
    } else {
        if(posicion!=undefined && posicion!=-1){            
            self.parent.opener.retornoXanelaAuxiliar(posicion);
        }else
            if(tab.selectedIndex!=-1){      
                self.parent.opener.retornoXanelaAuxiliar(tab.selectedIndex);
            }
    }
}

function pulsarSalir() {
    self.parent.opener.retornoXanelaAuxiliar();
}

function callFromTableTo(rowID,tableName){    
    elementoSeleccionado = true;
    pulsarSeleccionar(rowID);  
}
</script>
</head>
<body class="bandaBody" onload="{inicializar();}">
    <form target="_self">
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titListadoInter")%></div>
     <div class="contenidoPantalla">
     <table>
        <tr>
           <td class="etiqueta" align="left">
           </td>
        </tr>
        <tr>
             <td align="left" id="tabla" ondblclick="javascript:pulsarSeleccionar();"></td>
        </tr>
         <tr>
             <td height="30px"></td>
       </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSeleccionar")%>" name="cmdSeleccionar" onclick="pulsarSeleccionar();" accesskey="A">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSalir")%>" name="cmdSalir" onclick="pulsarSalir();" accesskey="C">
    </div>            
</div>            
</form>
        
<script type="text/javascript">
    var tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla', 935));
    tab.addColumna('100','center',"<%=escape(descriptor.getDescripcion("gEtiqDocumento"))%>");
    tab.addColumna('300','center',"<%=escape(descriptor.getDescripcion("gEtiqNombr"))%>");
    tab.addColumna('390','center',"<%=escape(descriptor.getDescripcion("gEtiq_Domicilio"))%>");
    tab.addColumna('121','center',"<%=escape(descriptor.getDescripcion("etiqRol"))%>");
    tab.displayCabecera=true;
    tab.displayTabla();

</script>
</body>    
</html>
