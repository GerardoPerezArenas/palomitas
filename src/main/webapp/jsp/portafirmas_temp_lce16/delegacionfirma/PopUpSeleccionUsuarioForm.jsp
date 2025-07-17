<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%
    UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int idioma=1;
    int apl=4;
    String css="";
    if (session.getAttribute("usuario") != null){
        usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        apl = usuarioVO.getAppCod();
        idioma = usuarioVO.getIdioma();
        css=usuarioVO.getCss();
    }
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

 <meta http-equiv="X-UA-Compatible" content="IE=10"/>
<tiles:insert page="/jsp/portafirmas/tpls/WindowTemplate.jsp" flush="true">
<tiles:put name="title" type="string">
    <fmt:message key='Portafirmas.PopUpSeleccionUsuarioForm.title'/>
</tiles:put>

<tiles:put name="head-content" type="string">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript">
var rows = new Array();
var keys = new Array();
var tbl = null;

function pulsarAceptar() {
    if (tablaUsuarios.selectedIndex>=0) {
        var resultValue = keys[tablaUsuarios.selectedIndex][0];
        var resultLabel = rows[tablaUsuarios.selectedIndex][0];
        if ((opener) && (opener.setPopUpResult) && (resultValue)) {
            //alert("PopUp.pulsarAceptar(): about to set result");
            opener.setPopUpResult(resultValue,resultLabel);
            pulsarSalir();
        } else {
            self.parent.opener.retornoXanelaAuxiliar([resultValue, resultLabel]);
        }
    } else {
        alert('<fmt:message key="ErrorMessages.OneMustSelected"/>');
    }
}

function pulsarSalir() {
    self.parent.opener.retornoXanelaAuxiliar();
}
function rowFunctionClick(){                
}
function rowFunction(){                
}

    </script>
</tiles:put>
<tiles:put name="content" type="string">
    <tiles:insert page="/jsp/portafirmas/tpls/AltiaPopUpTemplate.jsp" flush="false">
        <tiles:put name="altia-app-form-title" type="string">
            <fmt:message key='Portafirmas.PopUpSeleccionUsuarioForm.title'/>
        </tiles:put>
        <tiles:put name="altia-app-form-content" type="string">
            <c:if test="${requestScope.PopUpSeleccionUsuarioForm.totalCount <= 0}" >
                <fmt:message key="ErrorMessages.NoResultsFound"/>
            </c:if>
            <c:if test="${requestScope.PopUpSeleccionUsuarioForm.totalCount > 0}" >
                <table style="width:100%">
                    <tr>
                        <td id="tabla"></td>
                    </tr>
                </table>
                <script>
                    <c:forEach var="jspResultItem" items="${requestScope.PopUpSeleccionUsuarioForm.listaUsuarios}" varStatus="jspResultItemStatus">
                        keys[keys.length]=['<c:out value="${jspResultItem.id}"/>'];
                        rows[rows.length]=['<c:out value="${jspResultItem.login}"/>','<c:out value="${jspResultItem.nif}"/>','<c:out value="${jspResultItem.nombre}"/>'];
                    </c:forEach>
                        
                    var tablaUsuarios = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>',
                            '<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>',
                            '<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', 
                            '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>',
                            '<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

                    tablaUsuarios.addColumna('100','center',"<fmt:message key='Portafirmas.PopUpSeleccionUsuarioForm.Login'/>",'Number');
                    tablaUsuarios.addColumna('80','center',"<fmt:message key='Portafirmas.PopUpSeleccionUsuarioForm.NIF'/>",'String');
                    tablaUsuarios.addColumna('280','left',"<fmt:message key='Portafirmas.PopUpSeleccionUsuarioForm.Nombre'/>",'String');

                    tablaUsuarios.displayCabecera=true;
                    tablaUsuarios.setLineas(rows);
                    tablaUsuarios.displayTabla();
                </script>
            </c:if>            
        </tiles:put>
        <tiles:put name="altia-app-form-footer" type="string">
            <!-- Separador. -->
            <table height="2px" cellpadding="0px" cellspacing="0px"><tr><td></td></tr></table>
            <!-- Fin separador. -->
          <!-- BOTONES -->
            <DIV id="capaBotones1" name="capaBotones1" class="botoneraPrincipal">
                <INPUT type= "button" class="botonGeneral" value="<fmt:message key='Buttons.Accept'/>" name="cmdAceptar" id="cmdAceptar" onClick="pulsarAceptar();">
                <INPUT type= "button" class="botonGeneral" value="<fmt:message key='Buttons.cancel'/>" name="cmdSalir"  onClick="pulsarSalir();">
            </DIV>            
            <!-- BOTONES (fin) -->
        </tiles:put>
    </tiles:insert>
</tiles:put>
<tiles:put name="finalJavascript" type="string">
    <script type="text/javascript">
        <c:if test="${requestScope.PopUpSeleccionUsuarioForm.totalCount <= 0}">
        var btnAceptar = document.getElementById("cmdAceptar");
        if (btnAceptar) btnAceptar.disabled=true;
        </c:if>
        pleaseWait1("off",this);
    </script>
</tiles:put>
</tiles:insert>
