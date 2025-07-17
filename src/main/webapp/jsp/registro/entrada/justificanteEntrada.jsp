<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.InteresadosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm"%>

<%	UsuarioValueObject usuarioVO = new UsuarioValueObject();
	int idioma=1;
	int apl=4;
          String css = "";
	if (session.getAttribute("usuario") != null){
		usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
		apl = usuarioVO.getAppCod();
		idioma = usuarioVO.getIdioma();
                 css = usuarioVO.getCss();
	}
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title>Justificante Entrada</title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
  <link rel="stylesheet" href="<%=request.getContextPath()%><%=css%>" type="text/css">
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listaBusquedaTerceros.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
    <script type="text/javascript">
    
       
       
        // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function inicializar(){
        var argVentana = self.parent.opener.xanelaAuxiliarArgs;
        aplic = argVentana[0];
        codlistaPlantillas = argVentana[1];
        listaPlantillas = argVentana[2];
        editorPlantillas = argVentana[3];
        var plantillas = new Array();


        for(var i = 0; i < codlistaPlantillas.length; i++){
            plantillas[i] = [listaPlantillas[i], editorPlantillas[i]];
        }
        TablaPlantillas.lineas = plantillas;
        TablaPlantillas.displayTabla();
        window.focus();
     }

    function pulsarCancelar(){
        self.parent.opener.retornoXanelaAuxiliar();
    }
    function pulsarAceptar() {
        if(TablaPlantillas.selectedIndex != -1) {
            var i=TablaPlantillas.selectedIndex;
            var retorno = [codlistaPlantillas[i][0], listaPlantillas[i][0], editorPlantillas[i][0]];
            self.parent.opener.retornoXanelaAuxiliar(retorno);
          } else {
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoSelecFila")%>');
          }
          
          
     }
    </script>
</head>

<body class="bandaBody" onload="inicializar();">
<form name="formulario" method="post">
<div id="titulo" class="txttitblanco">Plantillas Disponibles</div>
<div class="contenidoPantalla" valign="top">
    <table width="100%" cellspacing="0px" cellpadding="0px"  border="0px">
        <tr> 
            <td width="100%" height="10px">&nbsp</td>	
        </tr>
        <tr>
            <td id="TablaPlantillas" width="100%" align="center"></td>
        </tr>
    </table>             
    <div class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbAceptar")%>' name="cmdVer" onClick="pulsarAceptar();" accesskey="V">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdAlta" onClick="pulsarCancelar();" accesskey="A">
    </div>
</div>
</form>
 <script type="text/javascript">
    var TablaPlantillas = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('TablaPlantillas'));
    TablaPlantillas.addColumna('389', 'center', '');
    TablaPlantillas.addColumna('60', 'center', '');
</script>
