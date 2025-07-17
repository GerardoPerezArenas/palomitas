<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<html:html>
    <head>
        <TITLE>::: ALTA VIA DIRECTA:::</TITLE>
        <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
        <%int idioma = 1;
            int apl = 1;
            if (session != null) {
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    apl = usuario.getAppCod();
                }
            }%>

        <%!
          // Funcion para escapar strings para javascript
          private String escape(String str) {
              return StringEscapeUtils.escapeJavaScript(str);
          }
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <link rel="stylesheet" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>" type="text/css">
        <script type="text/javascript"	src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <SCRIPT type="text/javascript">
            var codTiposVia = new Array();
            var descTiposVia = new Array();
            function inicializar() {
                top.focus();
                var args = self.parent.opener.xanelaAuxiliarArgs;
                document.forms[0].codProvincia.value = args[0];
                document.forms[0].codMunicipio.value = args[1];
                codTiposVia = args[2];
                descTiposVia = args[3];
				var descripcionVia = "";
				if(args[4]!=null){
					document.forms[0].descVia.value = args[4];
					document.forms[0].nombreCorto.value = args[4];
				}

                comboTipoVia.addItems(codTiposVia,descTiposVia);
            }function pulsarAceptar() {

            if(Trim(document.getElementById('nombreCorto').value) != '' && Trim(document.getElementById('descVia').value) != '' &&
                Trim(document.getElementById('codTipoVia').value) != '' && Trim(document.getElementById('descTipoVia').value) != '' ){
            document.forms[0].opcion.value="altaDirecta";
            document.forms[0].target="oculto";
            document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Viales.do";
            document.forms[0].submit();
        }else{
        jsp_alerta("A","<%=escape(descriptor.getDescripcion("msjObligTodos"))%>");
        }
    }
function pulsarCancelar() {
    self.parent.opener.retornoXanelaAuxiliar();
}
function respuestaAltaViaDirecta(respuesta, idVia, codVia, descVia, descTipoVia, codECO, codESI, codTipoVia){
var retorno = new Array();
if (respuesta=="altaNoRealizada"){
    jsp_alerta("A",'<%=escape(descriptor.getDescripcion("msjViaNGrab"))%>');
    } else if (respuesta=="altaRealizada"){
    retorno[0] = idVia;
    retorno[1]  = codVia;
    retorno[2] = descVia;
    retorno[3] = descTipoVia;
    retorno[4] = codECO;
    retorno[5] = codESI;
    retorno[6] = codTipoVia;
    self.parent.opener.retornoXanelaAuxiliar(retorno);
}
}function rellenarCampoCorto(){
var valor = Trim(document.getElementById('descVia').value);
if(valor != undefined && valor.length >25){ valor = document.getElementById('descVia').value.substring(0,25);}
document.getElementById('nombreCorto').value = valor;
}
        </SCRIPT>
    </head>
    <BODY class="bandaBody" onload="javascript:{inicializar();}">
        <html:form action="/terceros/mantenimiento/Viales.do" target="_self">
            <input type="hidden" name="opcion" value="">
            <input type="hidden" name="codProvincia">
            <input type="hidden" name="codMunicipio">
    <div class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_NuevoVial")%></div>
    <div class="contenidoPantalla" valign="top" style="text-align: left; padding: 3px">
        <table>
            <tr>
                <td style="width: 27%" class="etiqueta">
                    <%=descriptor.getDescripcion("manTer_EtiqTVI")%>
                </td>
                <td>
                    <input class="inputTextoObligatorio" type="text" id="codTipoVia" name="codTipoVia"
                           size="3" maxlength="5" onKeyPress = "javascript:return SoloDigitos(event);">
                    <input id="descTipoVia" name="descTipoVia" type="text" class="inputTextoObligatorio"
                           style="width:150" readonly>
                    <a id="anchorTipoVia" name="anchorTipoVia" href="">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" alt="" id="botonTipoVia" name="botonTipoVia"
                             style="cursor:'hand'"></span>
                    </a>
                </td>
            </tr>
            <tr>
                <td style="width: 27%" class="etiqueta">
                    <%=descriptor.getDescripcion("gEtiq_NomLargo")%>
                </td>
                <td>
                    <input id="descVia" name="descVia" type="text" class="inputTextoObligatorio"
                           size=60 maxlength=50 onblur="javascript:xAMayusculas(this);rellenarCampoCorto();">
                </td>
            </tr>
            <tr>
                <td style="width: 27%" class="etiqueta">
                    <%=descriptor.getDescripcion("gEtiq_NomCorto")%>
                </td>
                <td class="etiqueta">
                    <input id="nombreCorto" name="nombreCorto" type="text" class="inputTextoObligatorio"
                           size=60 maxlength=25 onblur="javascript:xAMayusculas(this);">
                </td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbAceptar")%>' name="cmdAceptar" onclick="pulsarAceptar();" accesskey="A">
            <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelar" onclick="pulsarCancelar();" accesskey="C">
        </div>
    </div>
</html:form>
        <script type="text/javascript">
            var comboTipoVia  = new Combo("TipoVia");
            function cargarComboBox(cod, des){
                eval(auxCombo+".addItems(cod,des)");
            }
    </script></BODY></html:html>
    
