<%@ page import="es.altia.agora.business.util.GeneralValueObject" %>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

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
   
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/dojo/dojo.js"></script>

<script type="text/javascript">

    <c:if test="${not empty requestScope.error}">
            jsp_alerta('A', '<c:out value="${requestScope.error}"/>');
    </c:if>

    <c:if test="${not empty requestScope.estado}">
            jsp_alerta('A', '<c:out value="${requestScope.estado}"/>');
    </c:if>

    <c:if test="${not empty requestScope.opcion}">

            <c:if test="${requestScope.opcion == 'cerrarVentana'}">
                cerrarVentana();
            </c:if>
    </c:if>

    dojo.require("dojo.widget.SortableTable");
    dojo.require("dojo.widget.Dialog");

    var listaOpsSW = new Array();
    var cont=0;
    <logic:iterate id="elemento" name="AltaOperacionSWForm" property="operacionesCombo">
      listaOpsSW[cont] = ['<c:out value="${elemento}"/>'];
      cont++;
    </logic:iterate>

    var dlg;
    function init(e) {
        dlg = dojo.widget.byId("DialogContent");
        var btn = document.getElementById("cancelarDialog");
        dlg.setCloseControl(btn);
    }
    dojo.addOnLoad(init);

    function validarTitulo(){
        var nombre = document.forms[0].titulo.value;
        if (nombre!="")
            return true;
        return false;
    }

    function validarOpSW(){
        var opSW = document.forms[0].opSW.value;
        if (opSW!="")
            return true;
        return false;
    }


    function cerrarVentana() {
        var datos = new Array();
        datos[0] = "cargaMantenimientoOp";
        datos[1] = <bean:write name="AltaOperacionSWForm" property="codigoOpDef"/>
        self.parent.opener.retornoXanelaAuxiliar(datos);
    }

    function actualizarOperacion() {
        var w = dojo.widget.byId("parsedFromHtml");
        if (w) {
            document.forms[0].nombreOpSW.value = w.getValue();
            document.getElementById("cancelarDialog").click();
        }
    }

    function pulsarCrear() {
        document.forms[0].target = "";
        document.forms[0].opcion.value = "confirmaAltaOp";
	    document.forms[0].action = DOMAIN_NAME+ "<c:url value='/integracionsw/AltaOperacionSW.do'/>";
	    document.forms[0].submit();
    }

</script>
    <html:form action="/integracionsw/AltaOperacionSW.do" target="_self">

    	<input type="hidden" name="codOpSW" id="codOpSW" value="">

	    <div class="dialogCombo" dojoType="dialog" id="DialogContent" bgColor="white" bgOpacity="0.5" toggle="fade" toggleDuration="250">
                        <table class="xTable" dojoType="SortableTable" widgetId="parsedFromHtml" headClass="fixedHeader" tbodyClass="scrollContent"
                               enableMultipleSelect="false" enableAlternateRows="true" rowAlternateClass="alternateRow" cellpadding="0" cellspacing="0" border="0"
                               onSelect=actualizarOperacion();>
                             <thead>
                                 <tr>
                                    <th field="Id" dataType="String">Operaci&oacuten</th>
                                         </tr>
                             </thead>
                             <logic:iterate id="elemento" name="AltaOperacionSWForm" property="operacionesCombo">
                                 <tr>
                                    <td><c:out value="${elemento}"/></td>
                                         </tr>
                            </logic:iterate>
                        </table>
	        <div>
	            <input type= "button" id="cancelarDialog" class="botonAplicacion" value="<%=descriptor.getDescripcion("iswCancelar")%>" name="cancelarDialog">
	        </div>
	    </div>
	    
	    <input type="hidden" name="opcion" value="">

	    <div class="nuevaOpSW">
	    	<span class="etiq"><%=descriptor.getDescripcion("iswEtiqTitOp")%></span>
	       	<html:text property="tituloOpSW" styleId="titulo" styleClass="inputTextoObligatorio" size="56" value=""
                          name="AltaOperacionSWForm"/>
	       	<br><br>
	       	<span class="etiq" style="float:left;"><%=descriptor.getDescripcion("iswEtiqOpSW")%></span>
            <input type="text" name="nombreOpSW"  id="nombreOpSW" size="56" class="inputTextoObligatorio" readonly="true" value="">
	        <a href="javascript:dlg.show()"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonEligeOp"
				name="botonEligeOp" style="cursor:hand;"></span></a>
	
	    </div>
          
    </html:form>
