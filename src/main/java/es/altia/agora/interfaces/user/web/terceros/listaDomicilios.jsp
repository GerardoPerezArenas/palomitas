<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<html>
<head>
<TITLE>::: LISTA DOMICILIOS:::</TITLE>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<link rel="stylesheet" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>" type="text/css">

<%
    int idioma = 1;
    int apl = 3;
    UsuarioValueObject usuario = new UsuarioValueObject();
    if (session != null) {
        usuario = (UsuarioValueObject) session.getAttribute("usuario");
        if (usuario != null) {
            idioma = usuario.getIdioma();
        }
    }
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
             type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/gestionTerceros.js"></script>

<script type="text/javascript">

var listaDomicilios = new Array();

function inicializar() {
    window.focus();
    listaDomicilios = self.parent.parent.opener.xanelaAuxiliarArgs['domicilios'];
    var listaTabla = new Array();
    var domicilio = new Array();
    for (var i=0; i<listaDomicilios.length; i++) {
        domicilio = listaDomicilios[i];
        listaTabla[i] = [formatearDireccion(domicilio)];
    }
    tab.lineas=listaTabla;
    tab.displayTabla();
    // Mostrar mensaje si se trata de elegir un nuevo domicilio principal
    if (self.parent.parent.opener.xanelaAuxiliarArgs['elegirDomPrincipal']) {
        document.getElementById('msjDomPrincipal').style.display = 'block';
    }
 }

function pulsarSeleccionar() {
    if(tab.selectedIndex == -1) {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    } else {
        var codigo = listaDomicilios[tab.selectedIndex][5];
        self.parent.parent.opener.retornoXanelaAuxiliar(codigo);
    }
}
function pulsarSalir() {
    self.parent.parent.opener.retornoXanelaAuxiliar(undefined);
}

</script>
</head>
    <body class="bandaBody" onload="{inicializar();}">
        <form target="_self">

 <table width="100%" height="320px" cellpadding="0px" cellspacing="0px" border="0px">
    <tr>
        <td style="height: 320px">
            <table class="bordeExteriorPantalla" cellspacing="0px" cellpadding="1px" style="width: 597px">
                <tr>
                    <td>
                    <table class="bordeInteriorPantalla" cellspacing="1px" style="width: 597px">
                        <tr>
                            <td id="titulo" style="width: 100%; height: 30px" class="txttitblanco">
                               <%=descriptor.getDescripcion("titListadoDoms")%>
                            </td>
                        </tr>
                        <tr>
                            <td class="separadorTituloPantalla"></td>
                        </tr>
                        <tr>
                         <td class="contenidoPantalla" valign="top">
                         <table>
                                 <tr>
                                    <td class="etiqueta" align="left">
                                      <span id="msjDomPrincipal" style="display:none; margin-bottom:5px">
                                          <%=descriptor.getDescripcion("msjElegirDomPrin")%>
                                      </span>
                                    </td>
                                 </tr>
                                 <tr>
                                      <td align="center" id="tabla" ondblclick="javascript:pulsarSeleccionar();"></td>
                                 </tr>
                                  <tr>
                                      <td height="30px"></td>
                                </tr>
                        </table>
                       </td>
                        </tr>
                    </table>
                    </td>
                </tr>
            </table>
            </td>
		  <!-- Sombra lateral. -->
                    <td class="sombraLateral">
                        <table width="1px" cellpadding="0px" cellspacing="0px" border="0px">
                            <tr>
                                <td style="height: 1px" class="parteSuperior"></td>
                            </tr>
                            <tr>
                                <td style="height: 322px" class="parteInferior"></td>
                            </tr>
                        </table>
                    </td>
                    <!-- Fin sombra lateral. -->
                </tr>

                <!-- Sombra inferior. -->
                <tr>
                    <td colspan="2" class="sombraInferior">
                        <table cellpadding="0px" cellspacing="0px" border="0px">
                            <tr>
                                <td style="width: 1px" class="parteIzquierda"></td>
                                <td style="width: 599px" class="parteDerecha"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <!-- Fin sombra inferior. -->
            </table>
            <!-- Separador. -->
            <table height="2px" cellpadding="0px" cellspacing="0px" border="0px">
                <tr>
                    <td></td>
                </tr>
            </table>
            <!-- Fin separador. -->
            <!-------------------------------------- BOTONES. ------------------------------------------>
            <div style="width: 100%; border: 0; text-align: right">
                <table cellpadding="0px" cellspacing="0px" style="border: 0;">
                    <tr>
                        <td>
                            <table cellpadding="0px" cellspacing="0px" border="0px">
                                <tr>
                                    <td>
                                        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSeleccionar")%>" name="cmdSeleccionar" onclick="pulsarSeleccionar();" accesskey="A">
                                    </td>
                                    <TD style="width:2px"></TD>
                                    <td>
                                        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSalir")%>" name="cmdSalir" onclick="pulsarSalir();" accesskey="C">
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
        </form>

<script type="text/javascript">
    if (document.all) var tab = new Tabla(document.all.tabla, 570);
    else var tab = new Tabla(document.getElementById('tabla', 570));

    tab.addColumna('564','left','');
    tab.height = 240;
    tab.displayCabecera=false;
    tab.displayTabla();

</script>
</body>
</html>
