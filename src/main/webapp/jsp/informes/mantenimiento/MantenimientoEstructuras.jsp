<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="es.altia.agora.business.geninformes.utils.ConstantesXerador"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%
            Log m_log = LogFactory.getLog(this.getClass().getName());
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 3;
            String[] params = null;
            String css = "";

            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
                apl = usuarioVO.getAppCod();
                params = usuarioVO.getParamsCon();
                css = usuarioVO.getCss();
            }

            es.altia.common.service.config.Config m_ConfigTechnical = es.altia.common.service.config.ConfigServiceHelper.getConfig("techserver");
            String url = request.getContextPath() + m_ConfigTechnical.getString("URL.caducaSesion");
            try {
                session = request.getSession(true);
                m_log.debug("checkSession: " + session.getId());
                if ((session.getAttribute("usuarioEscritorio") == null)) {
                    if (m_log.isDebugEnabled()) {
                        m_log.debug("checkSession: session.isNew()");
                    }
                    response.sendRedirect(response.encodeURL(url));

                }

            } catch (Exception e) {
                    m_log.error("Error en checkSession");

            }
            String arrayJScript = null;
            String estado = ((request.getParameter("op") != null) && (request.getParameter("op").equals("A"))) ? "ENGADIR" : ((request.getParameter("op").equals("M")) ? "MODIFICAR" : "CONSULTA");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<html:html>
<head>
<title>Estructuras de Informes</title>
<jsp:include page="/jsp/informes/tpls/app-constants.jsp" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/utilidadesXerador.js'/>"></script>
<script type="text/javascript">
            var tp1;
            var tp1_p1;
            var tp1_p2;
            var tp1_p3;
            var tp1_p4;
            var tp1_p5;
            var tp1_p6;
        </script>
    </head>
    
    
    
    
    <body  class="bandaBody" onLoad="pleaseWait('off');" scroll="no"> <!-- onLoad="inicializar();" --><!---->
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        
        <table  width="100%" height="300px" cellpadding="0px" cellspacing="1px" border="0px" align="center">
            <tr>
                <td id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiq_MantEstructura")%>  </td>
            </tr>
            <tr>
                <td class="contenidoPantalla">
                    
                    <html:form action="/informes/mantenimiento/MantenimientosInformesAction.do" method="POST" target="oculto" styleId="XeradorInformesForm">
                        <html:hidden property="textOperacion" styleId="textOperacion" />
                        <html:hidden property="COD_INFORMEXERADOROculto" styleId="COD_INFORMEXERADOROculto" />
                        <html:hidden property="negritaCabeceira" styleId="negritaCabeceira" />
                        
                        <html:hidden property="subraiadoCabeceira" styleId="subraiadoCabeceira" />
                        <html:hidden property="cursivaCabeceira" styleId="cursivaCabeceira" />
                        <html:hidden property="cabeceiraOficial" styleId="cabeceiraOficial" />
                        <html:hidden property="cabeceiraColumnas" styleId="cabeceiraColumnas" />
                        <html:hidden property="cabeceiraCentro" styleId="cabeceiraCentro" />
                        <html:hidden property="dataInformePe" styleId="dataInformePe" />
                        <html:hidden property="orientacionPaxina" styleId="orientacionPaxina" />
                        <html:hidden property="numeroPaxinaPe" styleId="numeroPaxinaPe" />
                        <html:hidden property="etBordeEtiqueta" styleId="etBordeEtiqueta" />
                        <html:hidden property="MNUCODOculto" styleId="MNUCODOculto" />
                        <html:hidden property="codCampoInformeOculto" styleId="codCampoInformeOculto" />
                        
                        <html:hidden property="tipoFicheiroSaida" styleId="tipoFicheiroSaida" />
                        
                        <html:hidden property="numerarLinhas" styleId="numerarLinhas" />
                        
                        <html:hidden styleId="codAplicacion" property="codAplicacion"/>
                        
                        <html:select property="listaSubentidadeCodigo" styleId="listaSubentidadeCodigo" styleClass="selectOculto" multiple="true" style="width:0;height:0;">
                        </html:select>
                        
                        <TABLE  width="100%" height="335" border="0" cellPadding="0" cellSpacing="0">
                            <tr>
                                <td>
                                    <table border="0" cellpadding="0" cellspacing="0">
                                        <tr>
                                            <td class="etiqueta"><%=descriptor.getDescripcion("gNome_Est")%></td><td>&nbsp;<html:text property="textNomeInforme" styleId="textNomeInforme" size="20" maxlength="30" styleClass="inputTexto" onkeyup="return xAMayusculas(this);"/></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr><td>&nbsp;</td></tr>
                            <tr>
                                <td align="center" >
                                    <div class="tab-pane" id="tab-pane-1">
                                        
                                        <script type="text/javascript">
                                            tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
                                        </script>
                                        
                                        
                                        <div class="tab-page" id="tabPage1" style="height:380px">
                                            
                                            <h2 class="tab">Campos<!--<%=descriptor.getDescripcion("tit_ExpPend")%>--></h2>
                                            
                                            <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>
                                            
                                            <TABLE id ="tablaDatosGral" class="contenidoPestanha"  border="0px">
                                                <tr>
                                                    <td style="width: 30%;height:20px;" class="sub3titulo"><%=descriptor.getDescripcion("gEtiq_CampInforme")%></td>
                                                 </tr>
                                                
                                                <tr>
                                                    <td style="width:100%;" valign="top">
                                                        <table cellspacing="0px" class="cuadroFondoBlanco"  style="padding-top: 5px; padding-bottom: 5px; width:100%" cellpadding="5px" border="0">
                                                            <tr>
                                                                <td class="etiqueta" style="width:31%"><%=descriptor.getDescripcion("gEnt_Informe")%></td>
                                                                <td class="textoc"  style="width:31%">&nbsp;</td>
                                                                <td class="etiqueta"  style="width:31%"><!--Formato de sa&iacute;da--></td>
                                                                <td class="textoc"  style="width:7%">&nbsp;</td>
                                                            </tr>
                                                            <tr>
                                                                <td class="textoc"><html:text style="visibility:hidden;height:0;width:0;" styleId="codEntidadeInforme" property="codEntidadeInforme"/> <input class="inputTexto" type="text" id="descEntidadeInforme" name="descEntidadeInforme" style="width:170;"><a href="" id="anchorEntidadeInforme" name="anchorEntidadeInforme"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonEntidadeInforme" name="botonEntidadeInforme" style="cursor:hand;"></span></a><html:select property="comboEntidadeInforme" styleId="comboEntidadeInforme" styleClass="pequena" style="visibility:hidden;height:0;width:0;" onchange="javascript:EventoComboEntidades();"></html:select></td>
                                                                <td class="textoc">&nbsp;</td>
                                                                <td class="textoc"><html:text style="visibility:hidden;height:0;width:0;" styleId="codFormatoInforme" property="codFormatoInforme"/> <input class="inputTexto" type="text" id="descFormatoInforme" name="descFormatoInforme" style="width:170;visibility:hidden;">
                                                                    <a href="" id="anchorFormatoInforme" name="anchorFormatoInforme" style="visibility:hidden;"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonFormatoInforme" name="botonFormatoInforme" style="cursor:hand;visibility:hidden;"></span></a>
                                                                <html:select property="comboFormatoInforme" styleId="comboFormatoInforme" styleClass="pequena" style="visibility:hidden;height:0;width:0;"></html:select></td>
                                                                <td class="textoc">&nbsp;</td>
                                                            </tr>
                                                            <tr>
                                                                <td class="textoc" align="center">&nbsp;</td>
                                                                <td class="etiqueta" align="left">&nbsp;&nbsp;&nbsp;<%=descriptor.getDescripcion("gCampos_Dispon")%></td>
                                                                <td class="etiqueta" align="center"><%=descriptor.getDescripcion("gCampos_Elegid")%></td>
                                                                <td class="textoc" align="center">&nbsp;</td>
                                                            </tr>
                                                            <tr>
                                                                <td class="textoc" align="center">&nbsp;</td>
                                                                <td class="textoc"  align="center">
                                                                    <table border="0" cellpadding="0" cellspacing="0"  width="100%">
                                                                        <tr><td width="95%">
                                                                                <select name="comboCamposDisponibles" id="comboCamposDisponibles" multiple size="10" style="width:150;border:#999999 1px solid;"></select>
                                                                            </td>
                                                                            <td width="5%"><table border="0" cellpadding="2" cellspacing="0"  width="100%">
                                                                                    <tr>
                                                                                        <td><INPUT NAME="BotonAsignar" TYPE="button" class="botonGeneral" style="width:30;height:30;" ONCLICK="PulsaBoton(event);" VALUE="&gt;"></td>
                                                                                    </tr>
                                                                                    <tr><td height="5"></td></tr>
                                                                                    <tr>
                                                                                        <td><INPUT NAME="BotonAsignarTodos" TYPE="button" class="botonGeneral" style="width:30;height:30;" ONCLICK="PulsaBoton(event);" VALUE="&gt;&gt;"></td>
                                                                                    </tr>
                                                                                    <tr><td height="5"></td></tr>
                                                                                    <tr>
                                                                                        <td><INPUT NAME="BotonDesAsignar" TYPE="button" class="botonGeneral" style="width:30;height:30;" ONCLICK="PulsaBoton(event);" VALUE="&lt"></td>
                                                                                    </tr>
                                                                                    <tr><td height="5"></td></tr>
                                                                                    <tr>
                                                                                        <td><INPUT NAME="BotonDesAsignarTodos" TYPE="button" class="botonGeneral" style="width:30;height:30;" ONCLICK="PulsaBoton(event);" VALUE="&lt;&lt;"></td>
                                                                                    </tr>
                                                                                </table>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </td>
                                                                <td class="textoc" align="center"><html:select property="comboCamposElexidos" styleId="comboCamposElexidos" multiple="true" size="10" style="width:150;border:#999999 1px solid;"></html:select></td>
                                                                <td class="textoc"  align="center">
                                                                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                                                        <tr>
                                                                            <td><a href="#" onclick="AccionArriba();"><span class="fa fa-chevron-circle-up" aria-hidden="true" id="imgBotonArriba"  style="cursor:hand;"></span></a><INPUT style="width:30;height:30;visibility:hidden;" TYPE="button" class="botonGeneral" NAME="BotonArriba" VALUE="^" ONCLICK="PulsaBoton(event);"></td>
                                                                        </tr>
                                                                        <tr  height="1"><td height="1"></td></tr>
                                                                        <tr>
                                                                            <td><a href="#" onclick="AccionAbaixo();"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="imgBotonAbaixo"  style="cursor:hand;"></span></a><INPUT style="width:30;height:30;visibility:hidden;" TYPE="button" class="botonGeneral" NAME="BotonAbaixo" VALUE="V" ONCLICK="PulsaBoton(event);"></td>
                                                                        </tr>
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                            
                                                        </table>
                                                    </td>
                                                </tr>
                                                
                                            </table>
                                        </div>
                                        <!-- Fin de DIV de campos -->
                                        <div class="tab-page" id="tabPage2"  style="height:380px;">
                                            
                                            <h2 class="tab">Ordenaci&oacute;n<!--<%=descriptor.getDescripcion("tit_ExpPend")%>--></h2>
                                            
                                            <script type="text/javascript">tp1_p2 = tp1.addTabPage( document.getElementById( "tabPage2" ) );</script>
                                            
                                            <TABLE id ="tablaDatosGral" class="contenidoPestanha"  border="0px">
                                                <tr>
                                                    <td style="width: 30%;height:20px;" class="sub3titulo"><%=descriptor.getDescripcion("gEtiq_OrdInforme")%></td>
                                                </tr>
                                                
                                                <tr>
                                                    <td style="width:100%;" valign="top">
                                                        <table cellspacing="0px" class="cuadroFondoBlanco"  style="padding-top: 5px; padding-bottom: 5px; width:100%" cellpadding="5px" border="0">
                                                          
                                                            <tr><td width="100%" align="center"><table width="50%" cellspacing="0" cellpadding="0" border="0">
                                                                        <tr>
                                                                            <td class="etiqueta" >&nbsp;&nbsp;Campos de ordenaci&oacute;n</td>
                                                                            <td class="etiqueta" >&nbsp;&nbsp;Sentido</td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td class="textoc"><html:text style="visibility:hidden;height:0;width:0;" styleId="codCampoOrdenacion_1" property="codCampoOrdenacion_1"/> <input class="inputTexto" type="text" id="descCampoOrdenacion_1" name="descCampoOrdenacion_1" style="width:200;">
                                                                                <a href="" id="anchorCampoOrdenacion_1" name="anchorCampoOrdenacion_1"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampoOrdenacion_1" name="botonCampoOrdenacion_1" style="cursor:hand;"></span></a>
                                                                            <html:select property="comboCampoOrdenacion_1" styleId="comboCampoOrdenacion_1" style="visibility:hidden;height:0;width:0;"></html:select></td>
                                                                            <td class="textoc">&nbsp;<html:text style="visibility:hidden;height:0;width:0;" styleId="codSentidoOrdenacion_1" property="codSentidoOrdenacion_1"/> <input class="inputTexto" type="text" id="descSentidoOrdenacion_1" name="descSentidoOrdenacion_1" style="width:60;">
                                                                                <a href="" id="anchorSentidoOrdenacion_1" name="anchorSentidoOrdenacion_1"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonSentidoOrdenacion_1" name="botonSentidoOrdenacion_1" style="cursor:hand;"></span></a>
                                                                            <html:select property="comboSentidoOrdenacion_1" styleId="comboSentidoOrdenacion_1" style="visibility:hidden;height:0;width:0;"></html:select></td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td class="textoc"><html:text style="visibility:hidden;height:0;width:0;" styleId="codCampoOrdenacion_2" property="codCampoOrdenacion_2"/> <input class="inputTexto" type="text" id="descCampoOrdenacion_2" name="descCampoOrdenacion_2" style="width:200;">
                                                                                <a href="" id="anchorCampoOrdenacion_2" name="anchorCampoOrdenacion_2"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampoOrdenacion_2" name="botonCampoOrdenacion_2" style="cursor:hand;"></span></a>
                                                                            <html:select property="comboCampoOrdenacion_2" styleId="comboCampoOrdenacion_2" style="visibility:hidden;height:0;width:0;"></html:select></td>
                                                                            <td class="textoc">&nbsp;<html:text style="visibility:hidden;height:0;width:0;" styleId="codSentidoOrdenacion_2" property="codSentidoOrdenacion_2"/> <input class="inputTexto" type="text" id="descSentidoOrdenacion_2" name="descSentidoOrdenacion_2" style="width:60;">
                                                                                <a href="" id="anchorSentidoOrdenacion_2" name="anchorSentidoOrdenacion_2"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonSentidoOrdenacion_2" name="botonSentidoOrdenacion_2" style="cursor:hand;"></span></a>
                                                                            <html:select property="comboSentidoOrdenacion_2" styleId="comboSentidoOrdenacion_2" style="visibility:hidden;height:0;width:0;"></html:select></td>
                                                                        </tr>
                                                                        
                                                                        
                                                                        <tr>
                                                                            <td class="textoc"><html:text style="visibility:hidden;height:0;width:0;" styleId="codCampoOrdenacion_3" property="codCampoOrdenacion_3"/> <input class="inputTexto" type="text" id="descCampoOrdenacion_3" name="descCampoOrdenacion_3" style="width:200;">
                                                                                <a href="" id="anchorCampoOrdenacion_3" name="anchorCampoOrdenacion_3"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampoOrdenacion_3" name="botonCampoOrdenacion_3" style="cursor:hand;"></span></a>
                                                                            <html:select property="comboCampoOrdenacion_3" styleId="comboCampoOrdenacion_3" style="visibility:hidden;height:0;width:0;"></html:select></td>
                                                                            <td class="textoc">&nbsp;<html:text style="visibility:hidden;height:0;width:0;" styleId="codSentidoOrdenacion_3" property="codSentidoOrdenacion_3"/> <input class="inputTexto" type="text" id="descSentidoOrdenacion_3" name="descSentidoOrdenacion_3" style="width:60;">
                                                                                <a href="" id="anchorSentidoOrdenacion_3" name="anchorSentidoOrdenacion_3"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonSentidoOrdenacion_3" name="botonSentidoOrdenacion_3" style="cursor:hand;"></span></a>
                                                                            <html:select property="comboSentidoOrdenacion_3" styleId="comboSentidoOrdenacion_3" style="visibility:hidden;height:0;width:0;"></html:select></td>
                                                                        </tr>
                                                                        
                                                                        <tr>
                                                                            <td class="textoc"><html:text style="visibility:hidden;height:0;width:0;" styleId="codCampoOrdenacion_4" property="codCampoOrdenacion_4"/> <input class="inputTexto" type="text" id="descCampoOrdenacion_4" name="descCampoOrdenacion_4" style="width:200;">
                                                                                <a href="" id="anchorCampoOrdenacion_4" name="anchorCampoOrdenacion_4"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampoOrdenacion_4" name="botonCampoOrdenacion_4" style="cursor:hand;"></span></a>
                                                                            <html:select property="comboCampoOrdenacion_4" styleId="comboCampoOrdenacion_4" style="visibility:hidden;height:0;width:0;"></html:select></td>
                                                                            <td class="textoc">&nbsp;<html:text style="visibility:hidden;height:0;width:0;" styleId="codSentidoOrdenacion_4" property="codSentidoOrdenacion_4"/> <input class="inputTexto" type="text" id="descSentidoOrdenacion_4" name="descSentidoOrdenacion_4" style="width:60;">
                                                                                <a href="" id="anchorSentidoOrdenacion_4" name="anchorSentidoOrdenacion_4"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonSentidoOrdenacion_4" name="botonSentidoOrdenacion_4" style="cursor:hand;"></span></a>
                                                                            <html:select property="comboSentidoOrdenacion_4" styleId="comboSentidoOrdenacion_4" style="visibility:hidden;height:0;width:0;"></html:select></td>
                                                                        </tr>
                                                                        
                                                                        <tr>
                                                                            <td class="textoc"><html:text style="visibility:hidden;height:0;width:0;" styleId="codCampoOrdenacion_5" property="codCampoOrdenacion_5"/> <input class="inputTexto" type="text" id="descCampoOrdenacion_5" name="descCampoOrdenacion_5" style="width:200;">
                                                                                <a href="" id="anchorCampoOrdenacion_5" name="anchorCampoOrdenacion_5"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampoOrdenacion_5" name="botonCampoOrdenacion_5" style="cursor:hand;"></span></a>
                                                                            <html:select property="comboCampoOrdenacion_5" styleId="comboCampoOrdenacion_5" style="visibility:hidden;height:0;width:0;"></html:select></td>
                                                                            <td class="textoc">&nbsp;<html:text style="visibility:hidden;height:0;width:0;" styleId="codSentidoOrdenacion_5" property="codSentidoOrdenacion_5"/> <input class="inputTexto" type="text" id="descSentidoOrdenacion_5" name="descSentidoOrdenacion_5" style="width:60;">
                                                                                <a href="" id="anchorSentidoOrdenacion_5" name="anchorSentidoOrdenacion_5"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonSentidoOrdenacion_5" name="botonSentidoOrdenacion_5" style="cursor:hand;"></span></a>
                                                                            <html:select property="comboSentidoOrdenacion_5" styleId="comboSentidoOrdenacion_5" style="visibility:hidden;height:0;width:0;"></html:select></td>
                                                                        </tr>
                                                                        
                                                                        <!--	<tr>
                                         <td class="textoc"><html:select property="comboCampoOrdenacion_3" styleId="comboCampoOrdenacion_3" style="width:150;"></html:select></td>
                                         <td class="textoc">&nbsp;<html:select property="comboSentidoOrdenacion_3" styleId="comboSentidoOrdenacion_3" style="width:100;"></html:select></td>
                                         </tr>
                                         <tr>
                                         <td class="textoc"><html:select property="comboCampoOrdenacion_4" styleId="comboCampoOrdenacion_4" style="width:150;"></html:select></td>
                                         <td class="textoc">&nbsp;<html:select property="comboSentidoOrdenacion_4" styleId="comboSentidoOrdenacion_4" style="width:100;"></html:select></td>
                                         </tr>
                                         <tr>
                                         <td class="textoc"><html:select property="comboCampoOrdenacion_5" styleId="comboCampoOrdenacion_5" style="width:150;"></html:select></td>
                                         <td class="textoc">&nbsp;<html:select property="comboSentidoOrdenacion_5" styleId="comboSentidoOrdenacion_5" style="width:100;"></html:select></td>
                                         </tr> -->
                                                        </table></td></tr></table>
                                                    </td>
                                                </tr>
                                                
                                            </table>
                                        </div>
                                        <!-- FIn de DIV Ordenacion -->
                                        <div class="tab-page" id="tabPage3"  style="height:380px;">
                                            
                                            <h2 class="tab">Filtro <!--<%=descriptor.getDescripcion("tit_ExpPend")%>--></h2>
                                            
                                            <script type="text/javascript">tp1_p3 = tp1.addTabPage( document.getElementById( "tabPage3" ) );</script>
                                            
                                           <TABLE id ="tablaDatosGral" class="contenidoPestanha"  border="0px">
                                                
                                                <tr> 
                                                      <td style="width: 30%;height:20px;" class="sub3titulo"><%=descriptor.getDescripcion("gEtiq_FiltInforme")%></td>
                                               </tr>
                                                
                                                <tr>
                                                    <td style="width:100%;" valign="top">
                                                        <table cellspacing="0px" class="cuadroFondoBlanco"  style="padding-top: 5px; padding-bottom: 5px; width:100%" cellpadding="5px" border="0">
                                                          
                                                            <tr>
                                                                <td class="textoc">&nbsp;</td>
                                                                <td class="etiqueta">Campo</td>
                                                                <td class="etiqueta">&nbsp;Condici&oacute;n</td>
                                                                <td class="etiqueta">&nbsp;Valor</td>
                                                                <td class="textoc">&nbsp;</td>
                                                            </tr>
                                                            <tr>
                                                                <td class="textoc">&nbsp;</td>
                                                                <td class="textoc"><html:text style="visibility:hidden;height:0;width:0;" styleId="codCampo_1" property="codCampo_1"/><input class="inputTexto" type="text" id="descCampo_1" name="descCampo_1" style="width:200;">
                                                                    <a href="" id="anchorCampo_1" name="anchorCampo_1"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampo_1" name="botonCampo_1" style="cursor:hand;"></span></a>
                                                                <html:select property="comboCampo_1" styleId="comboCampo_1" style="height:0;width:0;visibility:hidden;" onchange="javascript:CambiaCondicionYValor(1);"></html:select></td>
                                                                <td class="textoc">&nbsp;<html:text style="visibility:hidden;height:0;width:0;" styleId="codCondicion_1" property="codCondicion_1"/> <input class="inputTexto" type="text" id="descCondicion_1" name="descCondicion_1" style="width:50;">
                                                                    <a href="" id="anchorCondicion_1" name="anchorCondicion_1"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCondicion_1" name="botonCondicion_1" style="cursor:hand;"></span></a>
                                                                <html:select property="comboCondicion_1" styleId="comboCondicion_1" style="width:0;visibility:hidden;" onchange="javascript:EventoComboCondicion(1);"></html:select></td>
                                                                <td class="textoc">&nbsp;<html:text property="textValor_1" styleId="textValor_1" style="width:160;" onkeypress="campoValor_onKeyPress(1,event);" onchange="campoValor_onChange(1,event);"  styleClass="inputTexto"/></td>
                                                                <td class="textoc">&nbsp;</td>
                                                            </tr>
                                                            
                                                            <tr>
                                                                <td class="textoc"><html:text style="visibility:hidden;height:0;width:0;" styleId="codOperador_2" property="codOperador_2"/><input class="inputTexto" type="text" id="descOperador_2" name="descOperador_2" style="width:50;">
                                                                <a href="" id="anchorOperador_2" name="anchorOperador_2"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonOperador_2" name="botonOperador_2" style="cursor:hand;"></span></a><html:select property="comboOperador_2" styleId="comboOperador_2" style="width:0;height:0;visibility:hidden;"></html:select></td>
                                                                <td class="textoc"><html:text style="visibility:hidden;height:0;width:0;" styleId="codCampo_2" property="codCampo_2"/><input class="inputTexto" type="text" id="descCampo_2" name="descCampo_2" style="width:200;">
                                                                    <a href="" id="anchorCampo_2" name="anchorCampo_2"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampo_2" name="botonCampo_2" style="cursor:hand;"></span></a>
                                                                <html:select property="comboCampo_2" styleId="comboCampo_2" style="width:0;visibility:hidden;height:0;" onchange="javascript:CambiaCondicionYValor(2);"></html:select></td>
                                                                <td class="textoc">&nbsp;<html:text style="visibility:hidden;height:0;width:0;" styleId="codCondicion_2" property="codCondicion_2"/> <input class="inputTexto" type="text" id="descCondicion_2" name="descCondicion_2" style="width:50;">
                                                                <a href="" id="anchorCondicion_2" name="anchorCondicion_2"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCondicion_2" name="botonCondicion_2" style="cursor:hand;"></span></a><html:select property="comboCondicion_2" styleId="comboCondicion_2" style="width:0;height:0;visibility:hidden;" onchange="javascript:EventoComboCondicion(2);"></html:select></td>
                                                                <td class="textoc">&nbsp;<html:text property="textValor_2" styleId="textValor_2" style="width:160;"  onkeypress="campoValor_onKeyPress(2,event);" onchange="campoValor_onChange(2,event);"  styleClass="inputTexto"/></td>
                                                                <td class="textoc">&nbsp;</td>
                                                            </tr>
                                                            
                                                            <tr>
                                                                <td class="textoc"><html:text style="visibility:hidden;height:0;width:0;" styleId="codOperador_3" property="codOperador_3"/><input class="inputTexto" type="text" id="descOperador_3" name="descOperador_3" style="width:50;">
                                                                <a href="" id="anchorOperador_3" name="anchorOperador_3"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonOperador_3" name="botonOperador_3" style="cursor:hand;"></span></a><html:select property="comboOperador_3" styleId="comboOperador_3" style="width:0;height:0;visibility:hidden;"></html:select></td>
                                                                <td class="textoc"><html:text style="visibility:hidden;height:0;width:0;" styleId="codCampo_3" property="codCampo_3"/><input class="inputTexto" type="text" id="descCampo_3" name="descCampo_3" style="width:200;">
                                                                    <a href="" id="anchorCampo_3" name="anchorCampo_3"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampo_3" name="botonCampo_3" style="cursor:hand;"></span></a>
                                                                <html:select property="comboCampo_3" styleId="comboCampo_3" style="width:0;height:0;visibility:hidden;" onchange="javascript:CambiaCondicionYValor(3);"></html:select></td>
                                                                <td class="textoc">&nbsp;<html:text style="visibility:hidden;height:0;width:0;" styleId="codCondicion_3" property="codCondicion_3"/> <input class="inputTexto" type="text" id="descCondicion_3" name="descCondicion_3" style="width:50;">
                                                                <a href="" id="anchorCondicion_3" name="anchorCondicion_3"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCondicion_3" name="botonCondicion_3" style="cursor:hand;"></span></a><html:select property="comboCondicion_3" styleId="comboCondicion_3" style="width:0;height:0;visibility:hidden;" onchange="javascript:EventoComboCondicion(3);"></html:select></td>
                                                                <td class="textoc">&nbsp;<html:text property="textValor_3" styleId="textValor_3" style="width:160;"  onkeypress="campoValor_onKeyPress(3,event);" onchange="campoValor_onChange(3,event);"  styleClass="inputTexto"/></td>
                                                                <td class="textoc">&nbsp;</td>
                                                            </tr>
                                                            
                                                            <tr>
                                                                <td class="textoc"><html:text style="visibility:hidden;height:0;width:0;" styleId="codOperador_4" property="codOperador_4"/><input class="inputTexto" type="text" id="descOperador_4" name="descOperador_4" style="width:50;">
                                                                <a href="" id="anchorOperador_4" name="anchorOperador_4"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonOperador_4" name="botonOperador_4" style="cursor:hand;"></span></a><html:select property="comboOperador_4" styleId="comboOperador_4" style="width:0;height:0;visibility:hidden;"></html:select></td>
                                                                <td class="textoc"><html:text style="visibility:hidden;height:0;width:0;" styleId="codCampo_4" property="codCampo_4"/><input class="inputTexto" type="text" id="descCampo_4" name="descCampo_4" style="width:200;">
                                                                    <a href="" id="anchorCampo_4" name="anchorCampo_4"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampo_4" name="botonCampo_4" style="cursor:hand;"></span></a>
                                                                <html:select property="comboCampo_4" styleId="comboCampo_4" style="width:0;height:0;visibility:hidden;" onchange="javascript:CambiaCondicionYValor(4);"></html:select></td>
                                                                <td class="textoc">&nbsp;<html:text style="visibility:hidden;height:0;width:0;" styleId="codCondicion_4" property="codCondicion_4"/> <input class="inputTexto" type="text" id="descCondicion_4" name="descCondicion_4" style="width:50;">
                                                                <a href="" id="anchorCondicion_4" name="anchorCondicion_4"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCondicion_4" name="botonCondicion_4" style="cursor:hand;"></span></a><html:select property="comboCondicion_4" styleId="comboCondicion_4" style="width:0;height:0;visibility:hidden;" onchange="javascript:EventoComboCondicion(4);"></html:select></td>
                                                                
                                                                <td class="textoc">&nbsp;<html:text property="textValor_4" styleId="textValor_4" style="width:160;"  onkeypress="campoValor_onKeyPress(4,event);" onchange="campoValor_onChange(4,event);"  styleClass="inputTexto"/></td>
                                                                <td class="textoc">&nbsp;</td>
                                                            </tr>
                                                            <tr>
                                                                <td class="textoc"><html:text style="visibility:hidden;height:0;width:0;" styleId="codOperador_5" property="codOperador_5"/><input class="inputTexto" type="text" id="descOperador_5" name="descOperador_5" style="width:50;">
                                                                <a href="" id="anchorOperador_5" name="anchorOperador_5"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonOperador_5" name="botonOperador_5" style="cursor:hand;"></span></a><html:select property="comboOperador_5" styleId="comboOperador_5" style="width:0;height:0;visibility:hidden;"></html:select></td>
                                                                <td class="textoc"><html:text style="visibility:hidden;height:0;width:0;" styleId="codCampo_5" property="codCampo_5"/><input class="inputTexto" type="text" id="descCampo_5" name="descCampo_5" style="width:200;">
                                                                    <a href="" id="anchorCampo_5" name="anchorCampo_5"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampo_5" name="botonCampo_5" style="cursor:hand;"></span></a>
                                                                <html:select property="comboCampo_5" styleId="comboCampo_5" style="width:0;height:0;visibility:hidden;" onchange="javascript:CambiaCondicionYValor(5);"></html:select></td>
                                                                <td class="textoc">&nbsp;<html:text style="visibility:hidden;height:0;width:0;" styleId="codCondicion_5" property="codCondicion_5"/> <input class="inputTexto" type="text" id="descCondicion_5" name="descCondicion_5" style="width:50;">
                                                                <a href="" id="anchorCondicion_5" name="anchorCondicion_5"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCondicion_5" name="botonCondicion_5" style="cursor:hand;"></span></a><html:select property="comboCondicion_5" styleId="comboCondicion_5" style="width:0;height:0;visibility:hidden;" onchange="javascript:EventoComboCondicion(5);"></html:select></td>
                                                                
                                                                <td class="textoc">&nbsp;<html:text property="textValor_5" styleId="textValor_5" style="width:160;"  onkeypress="campoValor_onKeyPress(5,event);" onchange="campoValor_onChange(5,event);"  styleClass="inputTexto"/></td>
                                                                <td class="textoc">&nbsp;</td>
                                                            </tr>
                                                            <tr>
                                                                <td class="textoc"><html:text style="visibility:hidden;height:0;width:0;" styleId="codOperador_6" property="codOperador_6"/><input class="inputTexto" type="text" id="descOperador_6" name="descOperador_6" style="width:50;">
                                                                <a href="" id="anchorOperador_6" name="anchorOperador_6"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonOperador_6" name="botonOperador_6" style="cursor:hand;"></span></a><html:select property="comboOperador_6" styleId="comboOperador_6" style="width:0;height:0;visibility:hidden;"></html:select></td>
                                                                <td class="textoc"><html:text style="visibility:hidden;height:0;width:0;" styleId="codCampo_6" property="codCampo_6"/><input class="inputTexto" type="text" id="descCampo_6" name="descCampo_6" style="width:200;">
                                                                    <a href="" id="anchorCampo_6" name="anchorCampo_6"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampo_6" name="botonCampo_6" style="cursor:hand;"></span></a>
                                                                <html:select property="comboCampo_6" styleId="comboCampo_6" style="width:0;height:0;visibility:hidden;" onchange="javascript:CambiaCondicionYValor(6);"></html:select></td>
                                                                <td class="textoc">&nbsp;<html:text style="visibility:hidden;height:0;width:0;" styleId="codCondicion_6" property="codCondicion_6"/> <input class="inputTexto" type="text" id="descCondicion_6" name="descCondicion_6" style="width:50;">
                                                                <a href="" id="anchorCondicion_6" name="anchorCondicion_6"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCondicion_6" name="botonCondicion_6" style="cursor:hand;"></span></a><html:select property="comboCondicion_6" styleId="comboCondicion_6" style="width:0;height:0;visibility:hidden;" onchange="javascript:EventoComboCondicion(6);"></html:select></td>
                                                                <td class="textoc">&nbsp;<html:text property="textValor_6" styleId="textValor_6" style="width:160;"  onkeypress="campoValor_onKeyPress(6,event);" onchange="campoValor_onChange(6,event);"  styleClass="inputTexto"/></td>
                                                                <td class="textoc">&nbsp;</td>
                                                            </tr>
                                                            <tr>
                                                                <td class="textoc"><html:text style="visibility:hidden;height:0;width:0;" styleId="codOperador_7" property="codOperador_7"/><input class="inputTexto" type="text" id="descOperador_7" name="descOperador_7" style="width:50;">
                                                                <a href="" id="anchorOperador_7" name="anchorOperador_7"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonOperador_7" name="botonOperador_7" style="cursor:hand;"></span></a><html:select property="comboOperador_7" styleId="comboOperador_7" style="width:0;height:0;visibility:hidden;"></html:select></td>
                                                                <td class="textoc"><html:text style="visibility:hidden;height:0;width:0;" styleId="codCampo_7" property="codCampo_7"/><input class="inputTexto" type="text" id="descCampo_7" name="descCampo_7" style="width:200;">
                                                                    <a href="" id="anchorCampo_7" name="anchorCampo_7"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampo_7" name="botonCampo_7" style="cursor:hand;"></span></a>
                                                                <html:select property="comboCampo_7" styleId="comboCampo_7" style="width:0;height:0;visibility:hidden;" onchange="javascript:CambiaCondicionYValor(7);"></html:select></td>
                                                                <td class="textoc">&nbsp;<html:text style="visibility:hidden;height:0;width:0;" styleId="codCondicion_7" property="codCondicion_7"/> <input class="inputTexto" type="text" id="descCondicion_7" name="descCondicion_7" style="width:50;">
                                                                <a href="" id="anchorCondicion_7" name="anchorCondicion_7"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCondicion_7" name="botonCondicion_7" style="cursor:hand;"></span></a><html:select property="comboCondicion_7" styleId="comboCondicion_7" style="width:0;height:0;visibility:hidden;" onchange="javascript:EventoComboCondicion(7);"></html:select></td>
                                                                <td class="textoc">&nbsp;<html:text property="textValor_7" styleId="textValor_7" style="width:160;"  onkeypress="campoValor_onKeyPress(7,event);" onchange="campoValor_onChange(7,event);"  styleClass="inputTexto"/></td>
                                                                <td class="textoc">&nbsp;</td>
                                                            </tr>
                                                            
                                                            
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                        <!-- Fin de DIV de Filtro -->
           
         
         
                                        <!-- FIn de DIV Ordenacion -->
                                        <div class="tab-page" id="tabPage4"   style="height:380px;">
                                            
                                            <h2 class="tab">Subestructuras <!--<%=descriptor.getDescripcion("tit_ExpPend")%>--></h2>
                                            
                                            <script type="text/javascript">tp1_p4 = tp1.addTabPage( document.getElementById( "tabPage4" ) );</script>
                                            
                                             <TABLE id ="tablaDatosGral" class="contenidoPestanha"  border="0px">
                                                
                                                <tr> 
                                                      <td style="width: 30%;height:20px;" class="sub3titulo"><%=descriptor.getDescripcion("gEtiq_subestruc")%></td>
                                                </tr>
                                                <tr>
                                                    <td style="width:100%;" valign="top">
                                                        <table cellspacing="0px" class="cuadroFondoBlanco"  style="padding-top: 5px; padding-bottom: 5px; width:100%" cellpadding="5px" border="0">
                                                          
                                                            <tr><td width="100%" align="center"><table width="50%" cellspacing="0" cellpadding="0" border="0">
                                                                        <tr><!-- Aquí la tabla con la lista de subentidades -->
                                                                        <tr> <td align="left">
                                                                                <table border="0" cellpadding="2" cellspacing="0">
                                                                                    <tr><td rowspan="2" id="tablaE" width="100%" height="85%"  valign="bottom"></td><td valign="bottom">&nbsp;<input type="button" class="botonGeneral" name="BotonMaisSubentidade" id="BotonMaisSubentidade" value="+" onclick="javascript:PulsaBoton(event);" style="width:25;"></td>
                                                                                    </tr>
                                                                                    <tr><td valign="top">&nbsp;<input type="button" class="botonGeneral" name="BotonMenosSubentidade" id="BotonMenosSubentidade" value="-" onclick="javascript:PulsaBoton(event);" style="width:25;"></td>
                                                                                    </tr>
                                                                                </table>
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <!-- Aquí el combo de entidades -->
                                                                            <td align="center" width="100%"> <html:text style="width:30;" disabled="<%=true%>" styleId="codEstructuraInforme" property="codEstructuraInforme" styleClass="inputTexto"/> <input class="inputTexto" type="text" id="descEstructuraInforme" name="descEstructuraInforme" style="width:200;">
                                                                            <a href="" id="anchorEstructuraInforme" name="anchorEstructuraInforme"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonEstructuraInforme" name="botonEstructuraInforme" style="cursor:hand;"></span></a>&nbsp;&nbsp;<input type="button" class="botonGeneral" name="BotonValidarSubentidade" id="BotonValidarSubentidade" value="Validar" onclick="javascript:PulsaBoton(event);"  >&nbsp; </td>
                                                                        </tr>
                                                                        
                                                            </table></td></tr>
                                                            
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                        <!-- Fin de DIV de Filtro -->
           
                                    </div> <!-- Fin del div de fuera tab-pane -->
                                </td>
                            </tr>
                        </TABLE>
                    </html:form>
                    
                    <!-- -->
                    
        </td></tr></table> 
        <table width="770px" height="50px" align="right" border="0">
            <tr> 
                <td width="100%" align="right"> 
                    <input class="botonGeneral" name="BotonAceptar" type="button" value="Aceptar" accesskey="A" 
                           onClick="PulsaBoton(event)"> 
                    <input name="BotonCancelar" type="button" class="botonGeneral" id="BotonCancelar" accesskey="C" 
                       onClick="PulsaBoton(event)" value="Cancelar"> </td>
            </tr>
        </table>
        
    </body>
    
    <script type='text/javascript'>
        tp1.setSelectedIndex(0);
        //alert('Holaaaa');
        
        var tabEntidades = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaE'));
        
        tabEntidades.addColumna('85','center','Código');
        tabEntidades.addColumna('395','left','Nombre');
        tabEntidades.displayCabecera=true;  
        tabEntidades.displayTabla();
        tabEntidades.displayDatos=EventoCambioTablaEstructura;
        var ncomboEstructuraInforme=new Combo('EstructuraInforme');
        var ncomboEntidadeInforme =new Combo('EntidadeInforme');
        var ncomboFormatoInforme =new Combo('FormatoInforme');
        var ncomboCampoOrdenacion_1 =new Combo('CampoOrdenacion_1');
        var ncomboSentidoOrdenacion_1 =new Combo('SentidoOrdenacion_1');
        var ncomboCampoOrdenacion_2 =new Combo('CampoOrdenacion_2');
        var ncomboSentidoOrdenacion_2 =new Combo('SentidoOrdenacion_2');
        var ncomboCampoOrdenacion_3 =new Combo('CampoOrdenacion_3');
        var ncomboSentidoOrdenacion_3 =new Combo('SentidoOrdenacion_3');
        var ncomboCampoOrdenacion_4 =new Combo('CampoOrdenacion_4');
        var ncomboSentidoOrdenacion_4 =new Combo('SentidoOrdenacion_4');
        var ncomboCampoOrdenacion_5 =new Combo('CampoOrdenacion_5');
        var ncomboSentidoOrdenacion_5 =new Combo('SentidoOrdenacion_5');
        
        
        
        var ncomboCampo_1 =new Combo('Campo_1');
        var ncomboCondicion_1 =new Combo('Condicion_1');
        
        //var textValor_1 =new Combo('textValor_1');
        
        var ncomboOperador_2 =new Combo('Operador_2');
        var ncomboCampo_2 =new Combo('Campo_2');
        var ncomboCondicion_2 =new Combo('Condicion_2');
        
        var ncomboOperador_3 =new Combo('Operador_3');
        var ncomboCampo_3 =new Combo('Campo_3');
        var ncomboCondicion_3 =new Combo('Condicion_3');
        
        var ncomboOperador_4 =new Combo('Operador_4');
        var ncomboCampo_4 =new Combo('Campo_4');
        var ncomboCondicion_4 =new Combo('Condicion_4');
        
        var ncomboOperador_5 =new Combo('Operador_5');
        var ncomboCampo_5 =new Combo('Campo_5');
        var ncomboCondicion_5 =new Combo('Condicion_5');
        
        var ncomboOperador_6 =new Combo('Operador_6');
        var ncomboCampo_6 =new Combo('Campo_6');
        var ncomboCondicion_6 =new Combo('Condicion_6');
        
        var ncomboOperador_7 =new Combo('Operador_7');
        var ncomboCampo_7 =new Combo('Campo_7');
        var ncomboCondicion_7 =new Combo('Condicion_7');
        
        
        var mensaxeIncorrectoInforme='Por favor, introduza alomenos o nome do informe e un campo para o informe.';
        
        //var ncomboFonteCabeceira =new Combo('FonteCabeceira');
        //var ncomboTamanoCabeceira =new Combo('TamanoCabeceira');
        
        //var ncomboFontePe =new Combo('FontePe');
        //var ncomboTamanoPe =new Combo('TamanoPe');
        
        
        
        
        //var ncomboFonteDetalle =new Combo('FonteDetalle');
        //var ncomboTamanoDetalle =new Combo('TamanoDetalle');
        
        
        //var ncomboEtFonteDetalle =new Combo('EtFonteDetalle');
        //var ncomboEtTamanoDetalle =new Combo('EtTamanoDetalle');
        
        //var combo =new Combo('');
        //var combo =new Combo('');
        //var combo =new Combo('');
        //var combo =new Combo('');
        //var combo =new Combo('');
        //var combo =new Combo('');
        //var combo =new Combo('');
        //var combo =new Combo('');
        //var combo =new Combo('');
        //var combo =new Combo('');
        
        
        
        //
        //
        //
        function mostrarDiv(param)
        {
            //
            // Esta guarda es para que no permita ir a pestañas de formato no seleccionado
            //
            if (
                ( (param==5) && (comboFormatoInforme.options[comboFormatoInforme.selectedIndex].value=='<%=ConstantesXerador.CODIGO_FORMATO_LISTADO%>') )
                    ||
                    ( (param==5) && (comboFormatoInforme.options[comboFormatoInforme.selectedIndex].value=='<%=ConstantesXerador.CODIGO_FORMATO_ESTATISTICA%>') )
                        ||
                        ( (param==6) && (comboFormatoInforme.options[comboFormatoInforme.selectedIndex].value=='<%=ConstantesXerador.CODIGO_FORMATO_ETIQUETA%>') )
                            ||
                            ( (param<5) )
                        )
                        {
                            
                            OcultaTodo();
                            
                            switch (param)
                            {
                                case 1: tp1_p1.show();
                                break;
                                case 2: tp1_p2.show();
                                break;
                                case 3: // Pestaña de ordenacion
                                
                                tp1_p3.show();
                                //if (CAMBIOCAMPOSDISPONIBLES) CambiaCamposOrdenacion();
                                
                                if (VectorInforme.length>10) PosicionaCombosOrde(VectorInforme[10]);
                                
                                break;
                                case 4: // Pestaña de Filtro
                                
                                tp1_p4.show();
                                //if (CAMBIOCAMPOSDISPONIBLES) CambiaCamposFiltro();
                                if (VectorInforme.length>10) PosicionaCombosFiltro(VectorInforme[8]);
                                
                                break;
                                case 5: tp1_p5.show();
                                comboFonteCabeceira.style.visibility='visible';
                                comboTamanoCabeceira.style.visibility='visible';
                                comboFontePe.style.visibility='visible';
                                comboTamanoPe.style.visibility='visible';
                                comboFonteDetalle.style.visibility='visible';
                                comboTamanoDetalle.style.visibility='visible';
                                
                                break;
                                
                                case 6: tp1_p6.show();
                                comboTipoEtiqueta.style.visibility='visible';
                                etComboFonteDetalle.style.visibility='visible';
                                etComboTamanoDetalle.style.visibility='visible';
                                
                                
                                break;
                                default:break;
                            }
                        }
                    }
                    
                    
                    function OcultaTodo()
                    {
                        tp1_p1.hide();
                        tp1_p2.hide();
                        tp1_p3.hide();
                        tp1_p4.hide();
                        tp1_p5.hide();
                        //tp1_p6.hide();
                        
                    }
                    
                    
                    
                    //
                    //
                    //
                    
                    
                    function antesDeCambiarPestana()
                    {
                        //
                        // Aquí debo comprobar si Cambio los campos de selección, recargar los combos de orden
                        //
                        
                        
                        
                    }
                    
                    
                    //
                    // Desde principio
                    //
                    var URLBaseAction='<c:url value='/informes/mantenimiento/MantenimientosInformesAction.do?operacion='/>';
                    
                    
                    //
                    // Código nuevo a partir de aquí.
                    //
                    
                    // Esta variable define si esta el debug activo en javascript (la testearé antes de los alerts)
                    var DebugXADE=false;    
                    var DebugXADE2=false;    
                    
                    
                    var VectorPartidas;
                    var VectorIngresos;
                    var VectorPartidasExercicio;
                    var VectorIngresosExercicio;
                    var VectorContactos;
                    var VectorGlobalCamposPorEntidade=new Array();
                    
                    
                    var ActualINGNUM=-1;
                    var ActualIndice=-1;
                    var ActualIndiceCON=-1;
                    
                    var Estado = '<%=estado%>';
                    
                    var TiposActualesCamposCondicion=new Array();
                    
                    //
                    // Objetos Javascript represenatando a los campos HTML
                    //
                    var formulario=document.all.XeradorInformesForm;
                    
                    var textOperacion=formulario.textOperacion;
                    
                    
                    var comboEntidadeInforme = formulario.comboEntidadeInforme;
                    var comboFormatoInforme = formulario.comboFormatoInforme;
                    
                    
                    var comboCamposElexidos = formulario.comboCamposElexidos;
                    var COD_INFORMEXERADOROculto = formulario.COD_INFORMEXERADOROculto;
                    
                    var comboCamposDisponibles = formulario.comboCamposDisponibles;
                    var BotonAsignar = formulario.BotonAsignar;
                    var BotonAsignarTodos = formulario.BotonAsignarTodos;
                    var BotonDesAsignar = formulario.BotonDesAsignar;
                    var BotonDesAsignarTodos = formulario.BotonDesAsignarTodos;
                    var BotonArriba = formulario.BotonArriba;
                    var BotonAbaixo = formulario.BotonAbaixo;
                    var codAplicacion= formulario.codAplicacion;
                    
                    
                    //
                    //
                    // Propiedades de Ordenacion
                    //
                    var comboCampoOrdenacion_1 = formulario.comboCampoOrdenacion_1;
                    var comboSentidoOrdenacion_1 = formulario.comboSentidoOrdenacion_1;
                    
                    var comboCampoOrdenacion_2 = formulario.comboCampoOrdenacion_2;
                    var comboSentidoOrdenacion_2 = formulario.comboSentidoOrdenacion_2;
                    var comboCampoOrdenacion_3 = formulario.comboCampoOrdenacion_3;
                    var comboSentidoOrdenacion_3 = formulario.comboSentidoOrdenacion_3;
                    var comboCampoOrdenacion_4 = formulario.comboCampoOrdenacion_4;
                    var comboSentidoOrdenacion_4 = formulario.comboSentidoOrdenacion_4;
                    var comboCampoOrdenacion_5 = formulario.comboCampoOrdenacion_5;
                    var comboSentidoOrdenacion_5 = formulario.comboSentidoOrdenacion_5;
                    //
                    // FIN DE: Propiedades de Ordenacion
                    //
                    
                    // Propiedades de Filtro
                    //
                    var comboCampo_1 = formulario.comboCampo_1;
                    var comboCondicion_1 = formulario.comboCondicion_1;
                    var textValor_1 = formulario.textValor_1;
                    
                    var comboOperador_2 = formulario.comboOperador_2;
                    var comboCampo_2 = formulario.comboCampo_2;
                    var comboCondicion_2 = formulario.comboCondicion_2;
                    var textValor_2 = formulario.textValor_2;
                    
                    
                    var comboOperador_3 = formulario.comboOperador_3;
                    var comboCampo_3 = formulario.comboCampo_3;
                    var comboCondicion_3 = formulario.comboCondicion_3;
                    var textValor_3 = formulario.textValor_3;
                    
                    var comboOperador_4 = formulario.comboOperador_4;
                    var comboCampo_4 = formulario.comboCampo_4;
                    var comboCondicion_4 = formulario.comboCondicion_4;
                    var textValor_4 = formulario.textValor_4;
                    
                    var comboOperador_5 = formulario.comboOperador_5;
                    var comboCampo_5 = formulario.comboCampo_5;
                    var comboCondicion_5 = formulario.comboCondicion_5;
                    var textValor_5 = formulario.textValor_5;
                    
                    var comboOperador_6 = formulario.comboOperador_6;
                    var comboCampo_6 = formulario.comboCampo_6;
                    var comboCondicion_6 = formulario.comboCondicion_6;
                    var textValor_6 = formulario.textValor_6;
                    
                    var comboOperador_7 = formulario.comboOperador_7;
                    var comboCampo_7 = formulario.comboCampo_7;
                    var comboCondicion_7 = formulario.comboCondicion_7;
                    var textValor_7 = formulario.textValor_7;
                    //
                    // FIN DE: Propiedades de Filtro
                    //
                    
                    //
                    // Propiedades de listado
                    //
                    var textCabeceira = formulario.textCabeceira;
                    var comboFonteCabeceira = formulario.comboFonteCabeceira;
                    var comboTamanoCabeceira = formulario.comboTamanoCabeceira;
                    
                    var negritaCabeceira = formulario.negritaCabeceira;
                    var subraiadoCabeceira = formulario.subraiadoCabeceira;
                    var cursivaCabeceira = formulario.cursivaCabeceira;
                    var cabeceiraOficial = formulario.cabeceiraOficial;
                    var cabeceiraColumnas = formulario.cabeceiraColumnas;
                    var cabeceiraCentro = formulario.cabeceiraCentro;
                    
                    var textPe = formulario.textPe;
                    var comboFontePe = formulario.comboFontePe;
                    var comboTamanoPe = formulario.comboTamanoPe;
                    
                    var dataInformePe = formulario.dataInformePe;
                    var orientacionPaxina = formulario.orientacionPaxina;
                    var numeroPaxinaPe = formulario.numeroPaxinaPe;
                    
                    var comboFonteDetalle = formulario.comboFonteDetalle;
                    var comboTamanoDetalle = formulario.comboTamanoDetalle;
                    var textMarxeEsquerdo = formulario.textMarxeEsquerdo;
                    var textMarxeDereito = formulario.textMarxeDereito;
                    
                    var checkCabeceiraOficial= formulario.checkCabeceiraOficial;
                    var checkNegrita= formulario.checkNegrita;
                    var checkDatosCentro= formulario.checkDatosCentro;
                    var checkSulinado= formulario.checkSulinado;
                    var checkTitulosColumnas= formulario.checkTitulosColumnas;
                    var checkCentrada= formulario.checkCentrada;
                    var checkNumeroPaxina= formulario.checkNumeroPaxina;
                    var checkDataInforme= formulario.checkDataInforme;
                    var checkOrientacionVertical= formulario.checkOrientacionVertical;
                    var checkOrientacionHorizontal= formulario.checkOrientacionHorizontal;
                    
                    var checkNumerarLinhas= formulario.checkNumerarLinhas;
                    
                    //
                    // Propiedades catalogación
                    // 
                    var textDescripcion = formulario.textDescripcion;
                    var textNomeInforme = formulario.textNomeInforme;
                    
                    //
                    // Propiedades de etiquetas
                    //
                    var comboTipoEtiqueta = formulario.comboTipoEtiqueta;
                    var etTextAncho = formulario.etTextAncho;
                    var etTextAlto = formulario.etTextAlto;
                    var etTextHorizontal = formulario.etTextHorizontal;
                    var etTextVertical = formulario.etTextVertical;
                    var etTextSuperior = formulario.etTextSuperior;
                    var etTextInferior = formulario.etTextInferior;
                    var etTextEsquerdo = formulario.etTextEsquerdo;
                    var etTextDereito = formulario.etTextDereito;
                    var etTextSuperiorEtiqueta = formulario.etTextSuperiorEtiqueta;
                    var etTextEsquerdoEtiqueta = formulario.etTextEsquerdoEtiqueta;
                    var etComboFonteDetalle = formulario.etComboFonteDetalle;
                    var etComboTamanoDetalle = formulario.etComboTamanoDetalle;
                    var etBordeEtiqueta = formulario.etBordeEtiqueta;
                    var etCodEtiqueta = formulario.etCodEtiqueta;
                    var checkBordeEtiqueta  = formulario.checkBordeEtiqueta;
                    var BotonMantementoEtiquetas = formulario.BotonMantementoEtiquetas;
                    
                    var codCampoInformeOculto=formulario.codCampoInformeOculto;
                    //	var BotonVisualizar=getObjetoPorId('BotonVisualizar');
                    
                    //alert('Hola5');	 
                    
                    
                    
                    var VectorGlobalEstructuras;
                    var VectorSubents;
                    var BotonMaisSubentidade=formulario.BotonMaisSubentidade;
                    var BotonMenosSubentidade=formulario.BotonMenosSubentidade;
                    var BotonValidarSubentidade=formulario.BotonValidarSubentidade;
                    
                    var COD_ENTIDADEINFORMEOculto=formulario.COD_ENTIDADEINFORMEOculto;
                    var listaSubentidadeCodigo=formulario.listaSubentidadeCodigo;
                    
                    
                    function InicializaPestana()
                    {
                        
                        
                        // Mostramos lo que hay que mostrar
                        
                        CargaVectorGlobalCamposPorEntidade();
                        
                        CargaVectorGlobalEstructuras();
                        
                        CargaComboEntidades();
                        
                        CargaComboEstructuras();
                        
                        CargaCombosOperador();
                        CargaCombosConector();
                        CargaCombosSentidoOrde();
                        
                        //cargaComboTipoSaida();
                        
                        
                        AsignaEventoOnChange();
                        if ( (Estado=='CONSULTA') || (Estado=='MODIFICAR') ) 
                            {
                                COD_INFORMEXERADOROculto.value='<%=request.getParameter("id")%>';
                                codAplicacion.value='<%=request.getParameter("codApliInforme")%>';
                                //alert('Estado es:'+Estado+'.id:<%=request.getParameter("id")%>');
                                CubreControlesInforme();
                            } else {
                            codAplicacion.value='<%=request.getParameter("id")%>';
                            //alert('Estado es:'+Estado+'.id:<%=request.getParameter("id")%>');
                            EstableceEstadoEngadir();
                        }
                        
                        
                        
                        
                        
                    }
                    
                    
                    
                    function PulsaBoton(evento)
                    {
                        var objeto;
                        if (document.all) objeto = evento.srcElement;
                        else objeto=evento.target;
                        
                        switch (objeto.name)
                        {
                            
                            case 'BotonAceptar':
                            AccionAceptar();
                            break;
                            case 'BotonCancelar':
                            AccionCancelar();
                            break;
                            case 'BotonAsignar':
                            AccionAsignar();
                            break;
                            case 'BotonAsignarTodos':
                            AccionAsignarTodos();
                            break;
                            case 'BotonDesAsignar':
                            AccionDesAsignar();
                            break;
                            case 'BotonDesAsignarTodos':
                            AccionDesAsignarTodos();
                            break;
                            case 'BotonArriba':
                            AccionArriba();
                            break;
                            case 'BotonAbaixo':
                            AccionAbaixo();
                            break;
                            case 'BotonMaisSubentidade':
                            AccionMaisSubentidade();
                            break;
                            case 'BotonMenosSubentidade':
                            AccionMenosSubentidade();
                            break;
                            case 'BotonValidarSubentidade':
                            AccionValidarSubentidade();
                            break;
                            
                            default:break;
                        }
                    }
                    
                    function AccionArriba()
                    {
                        var opcionSel;
                        var opcionArriba;
                        var sel=comboCamposElexidos.selectedIndex;
                        
                        if (comboCamposElexidos.selectedIndex>0)
                            {
                                opcionSel=comboCamposElexidos.options[sel];
                                opcionArriba=comboCamposElexidos.options[sel-1];
                                comboCamposElexidos.options[sel]=new Option(opcionArriba.text,opcionArriba.value);
                                comboCamposElexidos.options[sel-1]=new Option(opcionSel.text,opcionSel.value);
                                comboCamposElexidos.selectedIndex=sel-1;
                            }
                            
                        }
                        
                        function AccionAbaixo()
                        {
                            var opcionSel;
                            var opcionArriba;
                            var sel=comboCamposElexidos.selectedIndex;
                            
                            if ( (comboCamposElexidos.selectedIndex>-1) && (comboCamposElexidos.selectedIndex<(comboCamposElexidos.options.length-1)) )
                                {
                                    opcionSel=comboCamposElexidos.options[sel];
                                    opcionArriba=comboCamposElexidos.options[sel+1];
                                    comboCamposElexidos.options[sel]=new Option(opcionArriba.text,opcionArriba.value);
                                    comboCamposElexidos.options[sel+1]=new Option(opcionSel.text,opcionSel.value);
                                    comboCamposElexidos.selectedIndex=sel+1;
                                }
                                
                            }
                            
                            
                            
                            function AccionAsignar()
                            {
                                moveSelectedOptions(comboCamposDisponibles,comboCamposElexidos,false);	
                                //alert('Asignando');
                                CambiaCamposOrdenacion();
                                CambiaCamposFiltro();
                            }
                            
                            function AccionAsignarTodos()
                            {
                                moveAllOptions(comboCamposDisponibles,comboCamposElexidos,false);
                                CambiaCamposOrdenacion();
                                CambiaCamposFiltro();
                            }
                            
                            function AccionDesAsignar()
                            {
                                // Reordena de novo debido a petición expresa
                                //
                                moveSelectedOptions(comboCamposElexidos,comboCamposDisponibles,true);
                                //alert('DesaAsignando');
                                CambiaCamposOrdenacion();
                                CambiaCamposFiltro();
                            }
                            
                            function AccionDesAsignarTodos()
                            {
                                // Reordena de novo debido a petición expresa
                                moveAllOptions(comboCamposElexidos,comboCamposDisponibles,true);
                                CambiaCamposOrdenacion();
                                CambiaCamposFiltro();
                            }
                            
                            
                            
                            
                            
                            
                            function EventoComboEntidades()
                            {
                                LimpiaCombo(comboCamposElexidos);
                                CubreListaCamposDisponibles();
                                //cargaObjetoComboDesdeCombo(ncomboEntidadeInforme,comboEntidadeInforme);
                            }
                            
                            
                            
                            function CubreListaCamposDisponibles(dame) 
                            {
                                //VectorGlobalCamposPorEntidade[] [CODIGO,NOME,COD_CAMPOINFORME, NOMECAMPO,TIPO,LONXITUDE]
                                var codentidade=document.all.codEntidadeInforme.value;//comboEntidadeInforme.options[comboEntidadeInforme.selectedIndex].value;
                                var fila;
                                var miOption;
                                var salida=new Array();
                                
                                if (!(dame)) LimpiaCombo(comboCamposDisponibles);
                                //alert('codentidade e:'+codentidade+'.VectorGlobalCamposPorEntidade:'+VectorGlobalCamposPorEntidade+'.');
                                if (ncomboEntidadeInforme.selectedIndex>-1) {
                                    for (var i=0;i<VectorGlobalCamposPorEntidade.length;i++)
                                        {
                                            fila=VectorGlobalCamposPorEntidade[i];
                                            //alert('fila0='+fila[0]+'.codentidade='+codentidade+'.');
                                            if (fila[0]==codentidade) {
                                                if (!(dame)) 
                                                    {
                                                        //alert('En cubrelistacamposdisp.Con dame==false.');
                                                        miOption=new Option(fila[3],fila[2]);
                                                        comboCamposDisponibles.options[comboCamposDisponibles.options.length]=miOption;
                                                    } else	{
                                                    salida[salida.length]=[fila[2],fila[3]];
                                                    //alert('En cubrelistacamposdisp.Con dame==true.');
                                                }
                                            }
                                            
                                        }
                                        
                                    }
                                    return salida;
                                }
                                
                                
                                
                                function CargaComboEstructuras()
                                {
                                    //VectorGlobalEstructuras[] [COD_ESTRUCTURA,A_DOC.DOC_NOM AS NOME,POSICION]
                                    var fila;
                                    var miOption;
                                    var anterior='';
                                    var cods=new Array();
                                    var descs=new Array();
                                    //
                                    // Arrays para comboNuevo
                                    //
                                    
                                    
                                    
                                    for (var i=0;i<VectorGlobalEstructuras.length;i++)
                                        {
                                            fila=VectorGlobalEstructuras[i];
                                            cods[i]=fila[0];
                                            descs[i]=fila[1];
                                            
                                        }
                                        ncomboEstructuraInforme.addItems(cods,descs);
                                        
                                    }
                                    
                                    
                                    
                                    function CargaComboEntidades()
                                    {
                                        //VectorGlobalCamposPorEntidade[] [CODIGO,NOME,COD_CAMPOINFORME, NOMECAMPO,TIPO,LONXITUDE]
                                        var fila;
                                        var miOption;
                                        var anterior='';
                                        
                                        //
                                        // Arrays para comboNuevo
                                        //
                                        //	var codsEntidade=new Array();
                                        //	var descsEntidade=new Array();
                                        
                                        LimpiaCombo(comboEntidadeInforme);
                                        
                                        for (var i=0;i<VectorGlobalCamposPorEntidade.length;i++)
                                            {
                                                fila=VectorGlobalCamposPorEntidade[i];
                                                if (fila[0]!=anterior) {
                                                    miOption=new Option(fila[1],fila[0]);
                                                    comboEntidadeInforme.options[comboEntidadeInforme.options.length]=miOption;
                                                    //
                                                    // Aquí preparo los arrays para el combo Nuevo
                                                    //
                                                    //codsEntidade[codsEntidade.length]=fila[0];
                                                    //descsEntidade[descsEntidade.length]=fila[1];
                                                    anterior=fila[0];
                                                }
                                            }
                                            if (comboEntidadeInforme.options.length>0) {
                                                comboEntidadeInforme.selectedIndex=0;
                                                //ncomboEntidadeInforme.selectedIndex=0;
                                                EventoComboEntidades();
                                            }
                                            
                                            cargaObjetoComboDesdeCombo(ncomboEntidadeInforme,comboEntidadeInforme);
                                        }
                                        
                                        
                                        
                                        
                                        
                                        
                                        
                                        function AccionEliminar()
                                        {
                                            EventoCancelarTipoSaida();
                                            //if (window.confirm(estaSeguroBen()))
                                            if (jsp_alerta('C','¿Está seguro?')==1)
                                                {
                                                    //mostrarDiv(1);
                                                    
                                                    textOperacion.value='B';
                                                    
                                                    COD_INFORMEXERADOROculto.value=tablaLateral.lineas[tablaLateral.selectedIndex]['COD_INFORMEXERADOR'];
                                                    
                                                    //
                                                    // divEspera
                                                    //
                                                    MostrarMensajeEspera(getObjetoPorId('divEspera'));
                                                    getObjetoPorId('divEspera').style.visibility='visible';
                                                    
                                                    formulario.action = URLBaseAction+'B';
                                                    formulario.target = 'oculto';
                                                    formulario.submit();
                                                } 
                                            }
                                            
                                            
                                            function callbackAccionEliminar() 
                                            {
                                                // Quito la primera fila de VectorDestino
                                                tablaLateral.removeLinea(tablaLateral.selectedIndex);
                                                
                                                //CubreControles();
                                                InicializaControles();	
                                                EstableceEstadoConsulta();
                                                
                                                //
                                                // divEspera
                                                //
                                                MostrarMensajeEspera(getObjetoPorId('divEspera'));
                                                getObjetoPorId('divEspera').style.visibility='hidden';
                                                
                                            }
                                            
                                            function AccionEngadir()
                                            {
                                                
                                                VectorInforme=new Array();
                                                EstableceEstadoEngadir();
                                            }
                                            
                                            function AccionModificar()
                                            {
                                                
                                                EstableceEstadoModificar();
                                            }
                                            
                                            function AccionAceptar() 
                                            {
                                                if (Estado=='ENGADIR') AceptarEngadir();
                                                else if (Estado=='MODIFICAR') AceptarModificar(); 
                                                else if (Estado=='CONSULTA') AceptarConsulta();  
                                            }
                                            
                                            function AccionCancelar() 
                                            {
                                                
                                                //if (window.confirm(estaSeguroBen()))
                                                //{
                                                //	if (Estado=='ENGADIR') {
                                                //		EstableceEstadoConsulta();
                                                //		CubreControlesInforme();
                                                //		} else {
                                                //		EstableceEstadoConsulta();
                                                //		CubreControles();
                                                //		}
                                                //}
                                                self.parent.opener.retornoXanelaAuxiliar();
                                                
                                                
                                            }
                                            
                                            function AceptarConsulta()
                                            {
                                                self.parent.opener.retornoXanelaAuxiliar();
                                            }
                                            
                                            
                                            function AceptarEngadir()
                                            {
                                                if (estaCorrectoInforme()) {
                                                    if (jsp_alerta('C','¿Está seguro?')==1)
                                                        //if (window.confirm(estaSeguroBen()))
                                                    {  
                                                        //mostrarDiv(1);
                                                        
                                                        textOperacion.value='A';
                                                        //MTRIDEOculto.value=comboAnoAcademico.options[comboAnoAcademico.selectedIndex].value;
                                                        GlobalTemporal=ConstruyeFilaInforme();
                                                        // Para que envie los campos elexidos debo hacerlos seleccionados.
                                                        // Debo meter los datos de los checks en sus variables.
                                                        PreparaInformeEnviar();
                                                        
                                                        //alert('Envio en comboEntidadeInforme:'+comboEntidadeInforme.options[comboEntidadeInforme.selectedIndex].value+'.');
                                                        //textNumero.disabled=false;
                                                        //
                                                        // divEspera
                                                        //
                                                        //MostrarMensajeEspera(getObjetoPorId('divEspera'));
                                                        //getObjetoPorId('divEspera').style.visibility='visible';
                                                        domlay("hidepage",1,0,0,null);
                                                        
                                                        formulario.action = URLBaseAction+'A';
                                                        formulario.target = 'oculto';
                                                        formulario.submit();
                                                    } 
                                                } else jsp_alerta('A',mensaxeIncorrectoInforme);
                                            }
                                            
                                            
                                            
                                            function callbackAceptarEngadir(num)
                                            {
                                                //tablaLateral.addLinea(GlobalTemporal);
                                                //tablaLateral.selectUltimo();
                                                //tablaLateral.lineas[tablaLateral.selectedIndex]['COD_INFORMEXERADOR']=num;
                                                //var temp=tablaLateral.selectedIndex;
                                                //tablaLateral.displayTabla();
                                                //tablaLateral.selectLinea(temp);
                                                
                                                
                                                //for (var i=0;i<comboCamposElexidos.options.length;i++)
                                                //{
                                                //comboCamposElexidos.options[i].selected=false;
                                                //}
                                                
                                                //CubreControles();
                                                //EstableceEstadoConsulta();
                                                
                                                //
                                                // divEspera
                                                //
                                                //MostrarMensajeEspera(getObjetoPorId('divEspera'));
                                                //getObjetoPorId('divEspera').style.visibility='hidden';
                                                
                                                //getObjetoPorId('BotonEngadir').focus();
                                                self.parent.opener.retornoXanelaAuxiliar(num);
                                            }
                                            
                                            
                                            function AceptarModificar()
                                            {
                                                if (estaCorrectoInforme()) {
                                                    if (jsp_alerta('C','¿Está seguro?')==1)
                                                        //if (window.confirm(estaSeguroBen()))
                                                    {
                                                        //mostrarDiv(1);
                                                        
                                                        
                                                        GlobalTemporal=ConstruyeFilaInforme();
                                                        //COD_INFORMEXERADOROculto.value=tablaLateral.lineas[tablaLateral.selectedIndex]['COD_INFORMEXERADOR'];
                                                        
                                                        textOperacion.value='M';
                                                        //textNumero.disabled=false;
                                                        PreparaInformeEnviar();
                                                        //
                                                        // divEspera
                                                        //
                                                        //MostrarMensajeEspera(getObjetoPorId('divEspera'));
                                                        //getObjetoPorId('divEspera').style.visibility='visible';
                                                        
                                                        
                                                        formulario.action = URLBaseAction+'M';
                                                        formulario.target = 'oculto';
                                                        formulario.submit();
                                                    } 
                                                } else jsp_alerta("A",mensaxeIncorrectoInforme);
                                            }
                                            
                                            
                                            function callbackAceptarModificar()
                                            {
                                                
                                                //tablaLateral.lineas[tablaLateral.selectedIndex]=GlobalTemporal;
                                                
                                                //CalculaTotales();
                                                //for (var i=0;i<comboCamposElexidos.options.length;i++)
                                                //{
                                                //comboCamposElexidos.options[i].selected=false;
                                                //}
                                                
                                                //EstableceEstadoConsulta();
                                                
                                                //
                                                // divEspera
                                                //
                                                //MostrarMensajeEspera(getObjetoPorId('divEspera'));
                                                //getObjetoPorId('divEspera').style.visibility='hidden';
                                                
                                                //var selec=tablaLateral.selectedIndex;
                                                //tablaLateral.displayTabla();
                                                //tablaLateral.selectLinea(selec);
                                                //getObjetoPorId('BotonModificar').focus();
                                                
                                                self.parent.opener.retornoXanelaAuxiliar('OK');
                                                
                                            }
                                            
                                            
                                            
                                            
                                            
                                            
                                            
                                            function EstableceEstadoConsulta()
                                            {
                                                Estado = 'CONSULTA';
                                                //HabilitaMenuPrincipal(true);
                                                //HabilitaMenuFuncional(true);	
                                                //HabilitaPestanas(true);
                                                
                                                //HabilitaEliminar(true);
                                                //HabilitaEngadir(true);
                                                //HabilitaModificar(true);
                                                HabilitaAceptar(false);			
                                                HabilitaCancelar(false);
                                                //HabilitaVisualizar(true);
                                                EditableControlesBusqueda(false);
                                                EditableControles(false);
                                                //
                                            }
                                            
                                            function EstableceEstadoEngadir()
                                            {
                                                Estado = 'ENGADIR';
                                                //HabilitaMenuPrincipal(false);
                                                //HabilitaMenuFuncional(false);	
                                                //HabilitaPestanas(false);
                                                
                                                //HabilitaEliminar(false);
                                                //HabilitaEngadir(false);
                                                //HabilitaModificar(false);
                                                EditableControlesBusqueda(true);
                                                InicializaControles();
                                                
                                                HabilitaAceptar(true);			
                                                HabilitaCancelar(true);	
                                                //HabilitaVisualizar(false);
                                                
                                                EditableControles(true);
                                                //textNomeInforme.focus();
                                                
                                                //mostrarDiv(1);
                                                CubreListaCamposDisponibles();
                                                
                                                //EventoCancelarTipoSaida();
                                                textNomeInforme.focus();
                                            }
                                            
                                            
                                            
                                            function EstableceEstadoModificar()
                                            {
                                                Estado = 'MODIFICAR';
                                                //HabilitaMenuPrincipal(false);
                                                //HabilitaMenuFuncional(false);	
                                                //HabilitaPestanas(false);
                                                
                                                //HabilitaEliminar(false);
                                                //HabilitaEngadir(false);
                                                //HabilitaModificar(false);
                                                HabilitaAceptar(true);			
                                                HabilitaCancelar(true);	
                                                //HabilitaVisualizar(false);
                                                // para probar
                                                EditableControlesBusqueda(true);
                                                // para probar
                                                EditableControles(true);
                                                
                                                //EventoCancelarTipoSaida();
                                                //textNomeInforme.focus();
                                            }
                                            
                                            
                                            
                                            
                                            
                                            
                                            function CargaCombosOperador(cond)
                                            {
                                                var miOption;
                                                var comboOrde;
                                                var codConector=['=','LIKE','<','>','<>','IS NOT NULL','IS NULL'];
                                                var descConector=['Igual','Como','Menor','Maior','Distinto','Si','Non'];
                                                
                                                var codConectorSin=['=','<','>','<>','IS NOT NULL','IS NULL'];
                                                var descConectorSin=['Igual','Menor','Maior','Distinto','Si','Non'];
                                                
                                                
                                                for (var i=1;i<8;i++)
                                                    {
                                                        comboOrde=eval('ncomboCondicion_'+i);
                                                        //LimpiaCombo(comboOrde);
                                                        
                                                        // Si no viene cond, lo normal, si viene cond no metemos el like
                                                        if (!(cond)) comboOrde.addItems(codConector,descConector);
                                                        else comboOrde.addItems(codConectorSin,descConectorSin);
                                                    }
                                                    
                                                    for (var i=1;i<8;i++)
                                                        {
                                                            comboOrde=eval('comboCondicion_'+i);
                                                            LimpiaCombo(comboOrde);
                                                            miOption=new Option('Igual','=');
                                                            comboOrde.options[comboOrde.options.length]=miOption;
                                                            // Si no viene cond, lo normal, si viene cond no metemos el like
                                                            if (!(cond)) {
                                                                miOption=new Option('Como','LIKE');
                                                                comboOrde.options[comboOrde.options.length]=miOption;
                                                            }
                                                            miOption=new Option('Menor','<');
                                                            comboOrde.options[comboOrde.options.length]=miOption;
                                                            miOption=new Option('Maior','>');
                                                            comboOrde.options[comboOrde.options.length]=miOption;
                                                            miOption=new Option('Distinto','<>');
                                                            comboOrde.options[comboOrde.options.length]=miOption;
                                                            miOption=new Option('Si','IS NOT NULL');
                                                            comboOrde.options[comboOrde.options.length]=miOption;
                                                            miOption=new Option('Non','IS NOT NULL');
                                                            comboOrde.options[comboOrde.options.length]=miOption;
                                                        }
                                                        
                                                        
                                                        
                                                    }
                                                    
                                                    function CargaCombosConector()
                                                    {
                                                        var miOption;
                                                        var comboOrde;
                                                        var codConector=['AND','OR'];
                                                        var descConector=['E','OU'];
                                                        
                                                        for (var i=2;i<8;i++)
                                                            {
                                                                comboOrde=eval('ncomboOperador_'+i);
                                                                comboOrde.addItems(codConector,descConector);
                                                            }
                                                            
                                                            for (var i=2;i<8;i++)
                                                                {
                                                                    comboOrde=eval('comboOperador_'+i);
                                                                    miOption=new Option('E','AND');
                                                                    comboOrde.options[comboOrde.options.length]=miOption;
                                                                    miOption=new Option('OU','OR');
                                                                    comboOrde.options[comboOrde.options.length]=miOption;
                                                                }
                                                                
                                                            }
                                                            
                                                            function CargaCombosSentidoOrde()
                                                            {
                                                                var miOption;
                                                                var comboOrde;
                                                                var codOrde=['A','D'];
                                                                var descOrde=['Asc.','Desc.'];
                                                                
                                                                for (var i=1;i<6;i++)
                                                                    {
                                                                        comboOrde=eval('ncomboSentidoOrdenacion_'+i);
                                                                        comboOrde.addItems(codOrde,descOrde);
                                                                    }
                                                                    for (var i=1;i<6;i++)
                                                                        {
                                                                            comboOrde=eval('comboSentidoOrdenacion_'+i);
                                                                            miOption=new Option('Asc.','A');
                                                                            comboOrde.options[comboOrde.options.length]=miOption;
                                                                            miOption=new Option('Desc.','D');
                                                                            comboOrde.options[comboOrde.options.length]=miOption;
                                                                        }
                                                                        
                                                                        
                                                                        
                                                                    }
                                                                    
                                                                    
                                                                    
                                                                    
                                                                    function cargaComboTipoEtiqueta()
                                                                    {
                                                                        var codTipoEtiquetas=['0','1'];
                                                                        var descTipoEtiquetas=['desc 0','desc 1'];
                                                                        ncomboTipoEtiqueta.addItems(codTipoEtiquetas,descTipoEtiquetas);
                                                                        
                                                                    }
                                                                    
                                                                    
                                                                    function estaCorrectoInforme()
                                                                    //
                                                                    // Comproba que alomenos fosen introducidos os datos mínimos para construir un informe
                                                                    //
                                                                    {
                                                                        var salida=false;
                                                                        salida=(comboCamposElexidos.options.length>0) && (Trim(textNomeInforme.value)!='');
                                                                        
                                                                        return salida;
                                                                    }
                                                                    
                                                                    
                                                                    function HabilitaVisualizar(estado)
                                                                    {
                                                                        var cond=!(estado && true);
                                                                        BotonVisualizar.disabled=cond;
                                                                    }
                                                                    
                                                                    
                                                                    CargaCombosOperador();
                                                                    CargaCombosSentidoOrde();
                                                                    CargaCombosConector();
                                                                    AsignaEventoOnChange();
                                                                    //comboCondicion_3.onchange();
                                                                    
                                                                    //mostrarDiv(3);
                                                                    
                                                                    
                                                                    //
                                                                    // Esta función asigna a los nuevos combos sus "event-handlers" 
                                                                    // para el evento onChange
                                                                    //
                                                                    function AsignaEventoOnChange() {
                                                                        
                                                                        
                                                                        ncomboEntidadeInforme.change=EventoComboEntidades;
                                                                        
                                                                        ncomboCampo_1.change =comboCampo_1.onchange;
                                                                        ncomboCondicion_1.change =comboCondicion_1.onchange;
                                                                        
                                                                        
                                                                        //var textValor_1 =new Combo('textValor_1');
                                                                        
                                                                        
                                                                        ncomboCampo_2.change =comboCampo_2.onchange;
                                                                        ncomboCondicion_2.change =comboCondicion_2.onchange;
                                                                        
                                                                        
                                                                        ncomboCampo_3.change =comboCampo_3.onchange;
                                                                        ncomboCondicion_3.change =comboCondicion_3.onchange;
                                                                        
                                                                        
                                                                        ncomboCampo_4.change =comboCampo_4.onchange;
                                                                        ncomboCondicion_4.change =comboCondicion_4.onchange;
                                                                        
                                                                        
                                                                        ncomboCampo_5.change =comboCampo_5.onchange;
                                                                        ncomboCondicion_5.change =comboCondicion_5.onchange;
                                                                        
                                                                        
                                                                        ncomboCampo_6.change =comboCampo_6.onchange;
                                                                        ncomboCondicion_6.change =comboCondicion_6.onchange;
                                                                        
                                                                        
                                                                        ncomboCampo_7.change =comboCampo_7.onchange;
                                                                        ncomboCondicion_7.change =comboCondicion_7.onchange;
                                                                        
                                                                    }
                                                                    
                                                                    
                                                                    
                                                                    function EventoComboCondicion(param)
                                                                    {
                                                                        var combo=eval('comboCondicion_'+param);
                                                                        var texto=eval('textValor_'+param);
                                                                        var comboNuevo=eval('comboCondicion_'+param);
                                                                        
                                                                        var codCondicion=getObjetoPorId('codCondicion_'+param);
                                                                        
                                                                        texto.disabled=(codCondicion.value=='IS NOT NULL') || (codCondicion.value=='IS NULL');
                                                                    }
                                                                    
                                                                    
                                                                    function CargaVectorGlobalCamposPorEntidade()
                                                                    {
                                                                        // Contiene un array de [CENCOD, CENNOM ]
                                                                        <%
            arrayJScript = null;
            try {
                // es.altia.util.Debug.crearDirectorio();	
                //
                //
                es.altia.util.HashtableWithNull tempTabla = new es.altia.util.HashtableWithNull();
                //String pestanaOrixe=request.getParameter(ConstantesXerador.COD_PESTANAORIXE);
                String cualApli = null;

                if (request.getParameter("op").equals("A")) {
                    cualApli = request.getParameter("id");
                } else {
                    cualApli = request.getParameter("codApliInforme");
                }
                System.err.println("Codapliinf=" + request.getParameter("codApliInforme") + "id=" + request.getParameter("id") + ".");
                tempTabla.put("APL_COD", cualApli);//USUARIO.getCOD_PESTANA());
                tempTabla.put("PARAMS", params);//USUARIO.getCOD_PESTANA());
                //es.altia.util.Debug.println("EN JSPIII MNUCOD ES:"+pestanaOrixe+".");
                arrayJScript = es.altia.agora.business.geninformes.utils.UtilidadesXerador.ObtenerArrayJSEntidadesCamposInforme(tempTabla);
            } catch (Exception e) {
            //es.altia.util.Debug.printException(e.getExcepcion());
            }
%>
    VectorGlobalCamposPorEntidade=unescapeArray(<%=arrayJScript%>);
    }
    
    
    
    
    
    
    
    
    
    function CambiaCamposOrdenacion()
    {
        var miOption;
        var comboOrde;
        
        LimpiaCombo(comboCampoOrdenacion_1);
        LimpiaCombo(comboCampoOrdenacion_2);
        LimpiaCombo(comboCampoOrdenacion_3);
        LimpiaCombo(comboCampoOrdenacion_4);
        LimpiaCombo(comboCampoOrdenacion_5);
        
        var ningun='Ning'+unescape('%FA')+'n';
        miOption=new Option(ningun,'');
        comboCampoOrdenacion_1.options[comboCampoOrdenacion_1.options.length]=miOption;
        miOption=new Option(ningun,'');
        comboCampoOrdenacion_2.options[comboCampoOrdenacion_2.options.length]=miOption;
        miOption=new Option(ningun,'');
        comboCampoOrdenacion_3.options[comboCampoOrdenacion_3.options.length]=miOption;
        miOption=new Option(ningun,'');
        comboCampoOrdenacion_4.options[comboCampoOrdenacion_4.options.length]=miOption;
        miOption=new Option(ningun,'');
        comboCampoOrdenacion_5.options[comboCampoOrdenacion_5.options.length]=miOption;
        
        if (comboCamposElexidos.options.length>0)
            {
                
                for (var j=0;j<comboCamposElexidos.options.length;j++)
                    {
                        miOption=new Option(comboCamposElexidos.options[j].text,comboCamposElexidos.options[j].value);
                        //comboOrde=eval('comboCampoOrdenacion_'+i);
                        //eval('comboCampoOrdenacion_'+i+'.options[comboCampoOrdenacion_'+i+'.options.length]=miOption');
                        //comboOrde=getObjetoPorId('comboCampoOrdenacion_'+i);
                        comboCampoOrdenacion_1.options[comboCampoOrdenacion_1.options.length]=miOption;
                        miOption=new Option(comboCamposElexidos.options[j].text,comboCamposElexidos.options[j].value);
                        comboCampoOrdenacion_2.options[comboCampoOrdenacion_2.options.length]=miOption;
                        miOption=new Option(comboCamposElexidos.options[j].text,comboCamposElexidos.options[j].value);
                        comboCampoOrdenacion_3.options[comboCampoOrdenacion_3.options.length]=miOption;
                        miOption=new Option(comboCamposElexidos.options[j].text,comboCamposElexidos.options[j].value);
                        comboCampoOrdenacion_4.options[comboCampoOrdenacion_4.options.length]=miOption;
                        miOption=new Option(comboCamposElexidos.options[j].text,comboCamposElexidos.options[j].value);
                        comboCampoOrdenacion_5.options[comboCampoOrdenacion_5.options.length]=miOption;
                        
                        
                    }	  
                    
                }
                
                var objetoCombo;
                var combo;
                
                for (var i=1;i<6;i++) {
                    objetoCombo=eval('ncomboCampoOrdenacion_'+i);
                    objetoCombo.clearItems();
                    eval('ncomboSentidoOrdenacion_'+i).selectItem(-1);
                    combo=eval('comboCampoOrdenacion_'+i);
                    cargaObjetoComboDesdeCombo(objetoCombo,combo);
                    
                }
                
            }
            
            function CambiaCamposFiltro()
            {
                var miOption;
                var comboOrde;
                
                LimpiaCombo(comboCampo_1);
                LimpiaCombo(comboCampo_2);
                LimpiaCombo(comboCampo_3);
                LimpiaCombo(comboCampo_4);
                LimpiaCombo(comboCampo_5);
                LimpiaCombo(comboCampo_6);
                LimpiaCombo(comboCampo_7);
                
                
                //if (comboCamposDisponibles.options.length>0)
                //{
                //alert('Entro en cCamFil');
                var temp=CubreListaCamposDisponibles(true);
                
                
                
                
                if (temp.length>0) {
                    //for (var j=0;j<temp.length;j++)
                    //	{
                    var ningun='Ning'+unescape('%FA')+'n';
                    //comboOrde=getObjetoPorId('comboCampo_'+i);
                    CubreComboValueTexto(comboCampo_1,temp,ningun);
                    
                    CubreComboValueTexto(comboCampo_2,temp,ningun);
                    
                    CubreComboValueTexto(comboCampo_3,temp,ningun);
                    
                    CubreComboValueTexto(comboCampo_4,temp,ningun);
                    
                    CubreComboValueTexto(comboCampo_5,temp,ningun);
                    
                    CubreComboValueTexto(comboCampo_6,temp,ningun);
                    
                    CubreComboValueTexto(comboCampo_7,temp,ningun);
                    
                    //	}	  
                    
                    
                    
                }
                
                var objetoCombo;
                var combo;
                
                for (var i=1;i<8;i++) {
                    objetoCombo=eval('ncomboCampo_'+i);
                    objetoCombo.clearItems();
                    if (i<1) eval('ncomboOperador_'+i).selectItem(-1);
                    eval('ncomboCondicion_'+i).selectItem(-1);
                    eval('textValor_'+i).value='';
                    combo=eval('comboCampo_'+i);
                    cargaObjetoComboDesdeCombo(objetoCombo,combo);
                }
                
            }
            
            
            
            
            function EditableControles(NuevoEstado)
            {
                var cond=!(NuevoEstado && true);
                var cond2=!(NuevoEstado && true && Estado=='ENGADIR');
                var que;
                
                if (NuevoEstado) que='activate();'; else que='deactivate();';
                
                comboFormatoInforme.disabled=cond;
                comboEntidadeInforme.disabled=cond;
                eval('ncomboFormatoInforme.'+que);
                
                comboCamposDisponibles.disabled=cond;
                comboCamposElexidos.disabled=cond;
                
                BotonAsignar.disabled=cond;
                BotonAsignarTodos.disabled=cond;
                BotonDesAsignar.disabled=cond;
                BotonDesAsignarTodos.disabled=cond;
                BotonArriba.disabled=cond;
                BotonAbaixo.disabled=cond;
                //
                // Propiedades de Ordenacion
                //
                
                
                eval('ncomboCampoOrdenacion_1.'+que);
                eval('ncomboCampoOrdenacion_2.'+que);
                eval('ncomboCampoOrdenacion_3.'+que);
                eval('ncomboCampoOrdenacion_4.'+que);
                eval('ncomboCampoOrdenacion_5.'+que);
                
                eval('ncomboSentidoOrdenacion_1.'+que);
                eval('ncomboSentidoOrdenacion_2.'+que);
                eval('ncomboSentidoOrdenacion_3.'+que);
                eval('ncomboSentidoOrdenacion_4.'+que);
                eval('ncomboSentidoOrdenacion_5.'+que);
                
                
                
                
                
                comboCampoOrdenacion_1.disabled=cond;
                comboSentidoOrdenacion_1.disabled=cond;
                
                comboCampoOrdenacion_2.disabled=cond;
                comboSentidoOrdenacion_2.disabled=cond;
                comboCampoOrdenacion_3.disabled=cond;
                comboSentidoOrdenacion_3.disabled=cond;
                comboCampoOrdenacion_4.disabled=cond;
                comboSentidoOrdenacion_4.disabled=cond;
                comboCampoOrdenacion_5.disabled=cond;
                comboSentidoOrdenacion_5.disabled=cond;
                //
                // FIN DE: Propiedades de Ordenacion
                //
                
                //
                // Propiedades de Filtro
                //
                
                eval('ncomboCampo_1.'+que);
                eval('ncomboCampo_2.'+que);
                eval('ncomboCampo_3.'+que);
                eval('ncomboCampo_4.'+que);
                eval('ncomboCampo_5.'+que);
                eval('ncomboCampo_6.'+que);
                eval('ncomboCampo_7.'+que);
                
                eval('ncomboCondicion_1.'+que);
                eval('ncomboCondicion_2.'+que);
                eval('ncomboCondicion_3.'+que);
                eval('ncomboCondicion_4.'+que);
                eval('ncomboCondicion_5.'+que);
                eval('ncomboCondicion_6.'+que);
                eval('ncomboCondicion_7.'+que);
                
                eval('ncomboOperador_2.'+que);
                eval('ncomboOperador_3.'+que);
                eval('ncomboOperador_4.'+que);
                eval('ncomboOperador_5.'+que);
                eval('ncomboOperador_6.'+que);
                eval('ncomboOperador_7.'+que);
                
                
                comboCampo_1.disabled=cond;
                comboCondicion_1.disabled=cond;
                textValor_1.disabled=cond;
                
                comboOperador_2.disabled=cond;
                comboCampo_2.disabled=cond;
                comboCondicion_2.disabled=cond;
                textValor_2.disabled=cond;
                
                comboOperador_3.disabled=cond;
                comboCampo_3.disabled=cond;
                comboCondicion_3.disabled=cond;
                textValor_3.disabled=cond;
                
                comboOperador_4.disabled=cond;
                comboCampo_4.disabled=cond;
                comboCondicion_4.disabled=cond;
                textValor_4.disabled=cond;
                
                comboOperador_5.disabled=cond;
                comboCampo_5.disabled=cond;
                comboCondicion_5.disabled=cond;
                textValor_5.disabled=cond;
                
                comboOperador_6.disabled=cond;
                comboCampo_6.disabled=cond;
                comboCondicion_6.disabled=cond;
                textValor_6.disabled=cond;
                
                comboOperador_7.disabled=cond;
                comboCampo_7.disabled=cond;
                comboCondicion_7.disabled=cond;
                textValor_7.disabled=cond;
                //
                // FIN DE: Propiedades de Filtro
                //
                
            }
            
            
            
            function PreparaInformeEnviar()
            {
                // Hago todas las options seleccionadas parque las envie en el submit
                //
                for (var i=0;i<comboCamposElexidos.options.length;i++)
                    {
                        comboCamposElexidos.options[i].selected=true;
                    }
                    
                    
                    LimpiaCombo(listaSubentidadeCodigo);
                    
                    if (tabEntidades.lineas.length) {
                        
                        
                        for (var i=0;i<tabEntidades.lineas.length;i++) {
                            codigo=tabEntidades.getLinea(i)['COD_ESTRUCTURA'];
                            miOption=new Option('',codigo);
                            miOption.selected=true;
                            listaSubentidadeCodigo.options[i]=miOption;
                        }
                    }
                    
                    
                    
                }
                
                function PonCampoSN(campo,check)
                {
                    campo.value=(check.checked)?'S':'N';
                }
                
                
                
                function ConstruyeFilaInforme()
                {
                    var temp=new Array();
                    
                    temp[0]=Trim(textNomeInforme.value);
                    
                    //CORNUMOculto.value=selec['CORNUM'];
                    //CORREXOculto.value=TipoCorreo;
                    
                    temp['COD_INFORMEXERADOR']=COD_INFORMEXERADOROculto.value;
                    //temp['CENCOD']=tablaLateral.getLinea(tablaLateral.selectedIndex)['CENCOD'];
                    temp['DESCRIPCION']='';//Trim(textDescripcion.value);
                    //temp['FORMATO']=comboFormatoInforme.options[comboFormatoInforme.selectedIndex].value;
                    //temp['COD_ENTIDADEINFORME']=comboEntidadeInforme.options[comboEntidadeInforme.selectedIndex].value;
                    //temp['COD_FUNCION']='';
                    
                    return temp;
                }
                
                
                
                
                
                
                
                
                function CubreControlesInforme()
                {	
                    var tempAno;
                    
                    //if (tablaLateral.selectedIndex>=0)
                    //{
                    
                    
                    
                    textOperacion.value='C';
                    
                    //COD_INFORMEXERADOROculto.value='21';
                    //alert('CIOculto:'+COD_INFORMEXERADOROculto.value+'.');
                    
                    //InicializaControles();
                    VectorInforme=new Array();
                    //alert('En cci');
                    formulario.action = URLBaseAction+'C';
                    formulario.target = 'oculto';
                    //alert('Antes de enviar');
                    formulario.submit();
                    //}
                    
                }
                
                
                function callbackCubreControlesInforme(vInforme,vSubents)
                {	
                    //
                    //                                                                                                                                                                         
                    // en vInforme viene [CODIGO,CENCOD,NOME,DESCRIPCION,FORMATO,CONSULTASQL,COD_ENTIDADEINFORME,COD_FUNCION,CAMPOSCONDICION,CAMPOSSELECCION,CAMPOSORDE,FORMATOINFORME ]
                    //alert('Recibo en cbCCI:'+vInforme+'.');
                    // en vSubents [ESTI.COD_ESTRUCTURA AS COD_ESTRUCTURA,ESTI.COD_ENTIDADEINFORME AS COD_ENTIDADEINFORME,ESTI.COD_PAI AS COD_PAI,ESTI.CONSULTASQL AS CONSULTASQL,ESTI.POSICION AS POSICION,EI.NOME AS NOME,EI.CLAUSULAFROM AS CLAUSULAFROM,EI.CLAUSULAWHERE AS CLAUSULAWHERE ,EI.NOMEVISTA AS NOMEVISTA,A_DOC.DOC_NOM AS NOME]
                    if ( (vInforme) && (vInforme.length>0) ) 
                        {
                            VectorInforme=unescapeArray(vInforme);
                            VectorInforme=VectorInforme[0];
                            //alert('VectorInforme='+VectorInforme+'.');
                            if ( (vSubents) && (vSubents.length>0) ) VectorSubents=unescapeArray(vSubents); else VectorSubents=[];
                            //alert('VectorSubents='+VectorSubents+'.');
                            CubreControles();
                        }
                        if (Estado=='CONSULTA') EstableceEstadoConsulta();
                        
                    } 
                    
                    
                    
                    
                    
                    function CubreControles()
                    {
                        
                        var vectorOrde=VectorInforme[10];
                        var vectorCondicion=VectorInforme[8];
                        var vectorSeleccion=VectorInforme[9]; //[ COD_INFORMEXERADOR, POSICION, COD_CAMPOINFORME, NOME, CAMPO, TIPO, LONXITUDE ]
                        var vectorFormato=VectorInforme[11];
                        var codcamposel;
                        var nomecamposel;
                        var miOption;
                        
                        
                        //alert('Los vectores son:'+VectorInforme+'.vectocondicion:'+vectorCondicion.length+'.vectorSeleccion:'+vectorSeleccion+'.vectororde:'+vectorOrde+'.vectorFormato:'+vectorFormato+'.');
                        //if (tablaLateral.selectedIndex>=0)
                        //{
                        COD_INFORMEXERADOROculto.value=VectorInforme[0];
                        textNomeInforme.value=VectorInforme[2];
                        //textDescripcion.value=VectorInforme[3];
                        //PosicionarComboPorValue(comboFormatoInforme,VectorInforme[4]);
                        
                        PosicionarComboPorValue(comboEntidadeInforme,VectorInforme[6]);
                        ncomboEntidadeInforme.buscaLinea(VectorInforme[6]);
                        
                        //
                        // Cargar el combo de campos disponibles
                        //
                        CubreListaCamposDisponibles();
                        
                        //
                        // Mover los campos elexidos de campos disponibles a Campos Elexidos
                        LimpiaCombo(comboCamposElexidos);
                        for (var i=0;i<vectorSeleccion.length;i++)
                            {
                                
                                codcamposel=vectorSeleccion[i][2]; //COD_CAMPOINFORME
                                if (codcamposel!='<%=ConstantesXerador.CODIGO_CAMPO_COUNT%>') { //Engadido para non duplicar estatisticas
                                    nomecamposel=vectorSeleccion[i][3]; //NOME
                                    //alert('codcamposel:'+codcamposel+'.nomecamposel:'+nomecamposel+'.');
                                    miOption=new Option(nomecamposel,codcamposel);
                                    comboCamposElexidos.options[comboCamposElexidos.options.length]=miOption;
                                    // Borro codcamposel de campos disponibles
                                    for (var j=0;j<comboCamposDisponibles.options.length;j++) if (comboCamposDisponibles.options[j].value==codcamposel) comboCamposDisponibles.options[j]=null;
                                }
                            }
                            
                            
                            
                            //
                            //
                            // Propiedades de Ordenacion
                            //
                            // [COD_INFORMEXERADOR,POSICION,COD_CAMPOINFORME,NOME,CAMPO, TIPO, LONXITUDE, TIPOORDE ]
                            InicializaControlesOrde();
                            CambiaCamposOrdenacion();
                            
                            //for (var k=0;k<comboCampoOrdenacion_1.options.length;k++) alert('comboorde1.option:'+comboCampoOrdenacion_1.options[k].value+'.');
                            PosicionaCombosOrde(vectorOrde);
                            
                            
                            // FIN DE: Propiedades de Ordenacion
                            //
                            
                            //
                            // Propiedades de Filtro
                            //
                            InicializaControlesFiltro();
                            CambiaCamposFiltro();
                            // [ COD_INFORMEXERADOR, POSICION, CLAUSULA, COD_CAMPOINFORME, NOME, CAMPO, TIPO, LONXITUDE, OPERADOR, VALOR]
                            PosicionaCombosFiltro(vectorCondicion);
                            
                            //
                            // FIN DE: Propiedades de Filtro
                            //
                            
                            //InicializaControlesListado();
                            //InicializaControlesEtiqueta();
                            
                            //if ( (comboFormatoInforme.options[comboFormatoInforme.selectedIndex].value=='<%=ConstantesXerador.CODIGO_FORMATO_LISTADO%>') || 
                            //	(comboFormatoInforme.options[comboFormatoInforme.selectedIndex].value=='<%=ConstantesXerador.CODIGO_FORMATO_ESTATISTICA%>') ) //Tamén estatísticas
                            //	CubreControlesListado(vectorFormato[0]);
                            //else if (comboFormatoInforme.options[comboFormatoInforme.selectedIndex].value=='<%=ConstantesXerador.CODIGO_FORMATO_ETIQUETA%>')
                            //	CubreControlesEtiquetas(vectorFormato[0]);
                            
                            //} else InicializaControles();
                            CubreControlesSubestructuras();
                            
                        }
                        
                        function CubreControlesSubestructuras()
                        {
                            var linea;
                            //// en vSubents [COD_ESTRUCTURA,COD_ENTIDADEINFORME,COD_PAI,POSICION,NOME_ENT,NOME  ";]
                            if ( (VectorSubents) && (VectorSubents.length>0) ) {
                                for (var i=0;i<VectorSubents.length;i++)
                                    {
                                        linea=new Array();
                                        linea[0]=VectorSubents[i][0];
                                        linea[1]=VectorSubents[i][5];
                                        //linea[2]=VectorSubents[i][4];
                                        linea['COD_ESTRUCTURA']=VectorSubents[i][0];
                                        tabEntidades.addLinea(linea);
                                    }
                                }
                                
                            }
                            
                            
                            
                            function PosicionaCombosFiltro(vectorCondicion)
                            {
                                for (var i=0;(i<vectorCondicion.length)&&(i<8);i++)
                                    {
                                        var j=i+1;
                                        if (j>=2) {
                                            PosicionarComboPorValue(eval('comboOperador_'+j),vectorCondicion[i][2]);
                                            eval('ncomboOperador_'+j).buscaLinea(vectorCondicion[i][2]);
                                        }
                                        PosicionarComboPorValue(eval('comboCampo_'+j),vectorCondicion[i][3]);
                                        eval('ncomboCampo_'+j).buscaLinea(vectorCondicion[i][3]);
                                        //alert('Pongo en el campo:'+j+'.:'+vectorCondicion[i][3]+'.');
                                        
                                        PosicionarComboPorValue(eval('comboCondicion_'+j),vectorCondicion[i][8]);
                                        eval('ncomboCondicion_'+j).buscaLinea(vectorCondicion[i][8]);
                                        //alert('Pongo en la cond:'+j+'.:'+vectorCondicion[i][8]+'.');
                                        eval('textValor_'+j).value=vectorCondicion[i][9];
                                    }
                                }
                                
                                
                                
                                
                                function InicializaControlesFiltro()
                                {
                                    //
                                    // Propiedades de Filtro
                                    //
                                    comboCampo_1.selectedIndex=0;
                                    comboCondicion_1.selectedIndex=0;
                                    textValor_1.value='';
                                    
                                    comboOperador_2.selectedIndex=0;
                                    comboCampo_2.selectedIndex=0;
                                    comboCondicion_2.selectedIndex=0;
                                    textValor_2.value='';
                                    
                                    comboOperador_3.selectedIndex=0;
                                    comboCampo_3.selectedIndex=0;
                                    comboCondicion_3.selectedIndex=0;
                                    textValor_3.value='';
                                    
                                    comboOperador_4.selectedIndex=0;
                                    comboCampo_4.selectedIndex=0;
                                    comboCondicion_4.selectedIndex=0;
                                    textValor_4.value='';
                                    
                                    comboOperador_5.selectedIndex=0;
                                    comboCampo_5.selectedIndex=0;
                                    comboCondicion_5.selectedIndex=0;
                                    textValor_5.value='';
                                    
                                    comboOperador_6.selectedIndex=0;
                                    comboCampo_6.selectedIndex=0;
                                    comboCondicion_6.selectedIndex=0;
                                    textValor_6.value='';
                                    
                                    comboOperador_7.selectedIndex=0;
                                    comboCampo_7.selectedIndex=0;
                                    comboCondicion_7.selectedIndex=0;
                                    textValor_7.value='';
                                    //
                                    // FIN DE: Propiedades de Filtro
                                    //
                                    
                                }
                                
                                function InicializaControlesOrde()
                                {
                                    //
                                    // Propiedades de Ordenacion
                                    //
                                    comboCampoOrdenacion_1.selectedIndex=0;
                                    comboSentidoOrdenacion_1.selectedIndex=0;
                                    
                                    comboCampoOrdenacion_2.selectedIndex=0;
                                    comboSentidoOrdenacion_2.selectedIndex=0;
                                    comboCampoOrdenacion_3.selectedIndex=0;
                                    comboSentidoOrdenacion_3.selectedIndex=0;
                                    comboCampoOrdenacion_4.selectedIndex=0;
                                    comboSentidoOrdenacion_4.selectedIndex=0;
                                    comboCampoOrdenacion_5.selectedIndex=0;
                                    comboSentidoOrdenacion_5.selectedIndex=0;
                                    //
                                    // FIN DE: Propiedades de Ordenacion
                                    //
                                }
                                
                                
                                function InicializaControles()
                                {
                                    //CORNUMOculto.value=0;
                                    //COD_INFORMEXERADOROculto.value=selec['COD_INFORMEXERADOR'];
                                    textNomeInforme.value='';
                                    //textDescripcion.value='';
                                    comboFormatoInforme.selectedIndex=0;
                                    
                                    LimpiaCombo(comboCamposElexidos);
                                    COD_INFORMEXERADOROculto.value='0';
                                    comboFormatoInforme.selectedIndex=0;
                                    comboEntidadeInforme.selectedIndex=0;
                                    ncomboEntidadeInforme.selectItem(0);
                                    
                                    InicializaControlesOrde();	
                                    InicializaControlesFiltro();
                                    //InicializaControlesListado();
                                    
                                    //InicializaControlesEtiqueta();
                                    
                                    
                                    //
                                    // Propiedades catalogación
                                    // 
                                    //textDescripcion.value='';
                                    textNomeInforme.value='';
                                    
                                    
                                }
                                
                                
                                function PosicionaCombosOrde(vectorOrde)
                                {
                                    for (var i=0;(i<vectorOrde.length)&&(i<6);i++)
                                        {
                                            var j=i+1;
                                            //alert('En contorde:i:'+i+'vectoOrde:'+vectorOrde[i][2]+'.vectorOrde[i][7]'+vectorOrde[i][7]+'.');
                                            
                                            PosicionarComboPorValue(eval('comboCampoOrdenacion_'+j),vectorOrde[i][2]);
                                            PosicionarComboPorValue(eval('comboSentidoOrdenacion_'+j),vectorOrde[i][7]);
                                            eval('ncomboCampoOrdenacion_'+j).buscaLinea(vectorOrde[i][2]);
                                            eval('ncomboSentidoOrdenacion_'+j).buscaLinea(vectorOrde[i][7]);
                                        }
                                        
                                        
                                    }
                                    
                                    
                                    function HabilitaAceptar(NuevoEstado) 
                                    
                                    {
                                        
                                        //HabilitaBoton(getObjetoPorId('BotonAceptar'),NuevoEstado && (Estado!='CONSULTA') && true);
                                        var valor=NuevoEstado && (Estado!='CONSULTA') && true;
                                        getObjetoPorId('BotonAceptar').disabled=!valor;
                                    }
                                    
                                    
                                    
                                    function HabilitaCancelar(NuevoEstado) 
                                    
                                    {
                                        
                                        //HabilitaBoton(getObjetoPorId('BotonCancelar'),NuevoEstado && (Estado!='CONSULTA') && true);
                                        var valor=NuevoEstado && (Estado!='CONSULTA') && true;
                                        getObjetoPorId('BotonCancelar').disabled=!valor;
                                    }
                                    
                                    
                                    // Esta funcion si el campo elegido no es CHAR elimina el operador LIKE y 
                                    // obliga a meter todo números si es numérico y a respetar el formato de fecha.
                                    //
                                    function  CambiaCondicionYValor(cualCombo)
                                    {
                                        //
                                        var combo=eval('ncomboCampo_'+cualCombo);
                                        var valor;
                                        var fila;
                                        var tipo;
                                        var campoValor=eval('textValor_'+cualCombo);
                                        
                                        var encontrado=false;
                                        if (combo.selectedIndex>0) {
                                            valor=getObjetoPorId('codCampo_'+cualCombo).value;//combo.options[combo.selectedIndex].value;
                                            //
                                            // Busco el puñetero valor para saber si es texto o no.
                                            //
                                            //var vectorCondicion=VectorInforme[8]; // [COD_INFORMEXERADOR,POSICION,CLAUSULA,COD_CAMPOINFORME,NOME,CAMPO,TIPO,LONXITUDE,OPERADOR,VALOR]
                                            //VectorGlobalCamposPorEntidade; // [ CODIGO, NOME, COD_CAMPOINFORME, NOMECAMPO, TIPO, LONXITUDE]
                                            
                                            fila=VectorGlobalCamposPorEntidade;
                                            for (var i=0;( (i<VectorGlobalCamposPorEntidade.length) && !(encontrado) );i++)
                                                {
                                                    fila=VectorGlobalCamposPorEntidade[i];
                                                    encontrado=(fila[2]==valor);
                                                    
                                                }
                                                
                                                
                                                if (encontrado) {
                                                    tipo=fila[4];// TIPO	
                                                    TiposActualesCamposCondicion[cualCombo]=tipo;
                                                    
                                                    CargaCombosOperador((tipo!='A'));
                                                    
                                                }
                                            }
                                        }
                                        
                                        
                                        function campoValor_onKeyPress(cual,event)
                                        {
                                            if (TiposActualesCamposCondicion[cual]=='N') return SoloDecimales(event);
                                        }
                                        
                                        function campoValor_onChange(cual,event)
                                        {
                                            if (TiposActualesCamposCondicion[cual]=='D') validarFecha(getObjetoPorId('textValor_'+cual),0);
                                        }
                                        
                                        
                                        function CargaVectorGlobalEstructuras()
                                        {
                                            // Contiene un array de [COD_ESTRUCTURA,A_DOC.DOC_NOM AS NOME,POSICION FROM ESTRUCTURAINFORME,A_DOC "; ]
                                            <%
            arrayJScript = null;
            try {
                // es.altia.util.Debug.crearDirectorio();	
                //
                //
                es.altia.util.HashtableWithNull tempTabla = new es.altia.util.HashtableWithNull();
                //String pestanaOrixe=request.getParameter(ConstantesXerador.COD_PESTANAORIXE);
                String cualApli = null;

                if (request.getParameter("op").equals("A")) {
                    cualApli = request.getParameter("id");
                } else {
                    cualApli = request.getParameter("codApliInforme");
                }
                System.err.println("Codapliinf=" + request.getParameter("codApliInforme") + "id=" + request.getParameter("id") + ".");
                tempTabla.put("APL_COD", cualApli);//USUARIO.getCOD_PESTANA());
                //String cualEst=request.getParameter("codEst");
                if (request.getParameter("op").equals("M")) {
                    tempTabla.put("COD_ESTRUCTURA", request.getParameter("id"));
                }//USUARIO.getCOD_PESTANA());
                tempTabla.put("PARAMS", params);//USUARIO.getCOD_PESTANA());
                //es.altia.util.Debug.println("EN JSPIII MNUCOD ES:"+pestanaOrixe+".");
                arrayJScript = es.altia.agora.business.geninformes.utils.UtilidadesXerador.ObtenerArrayJSEstructurasInforme(tempTabla);
                System.err.println("Devuelvo:" + arrayJScript + ".");
            } catch (Exception e) {
                System.err.println("Excepcion es:" + e + ".");
            }
%>
    VectorGlobalEstructuras=unescapeArray(<%=arrayJScript%>);
        //alert('VGE es:'+VectorGlobalEstructuras+'.<%=arrayJScript%>');
    }
    
    function EventoCambioTablaEstructura()
    {
        HabilitaBotonMenosSubentidade((Estado!='CONSULTA'));
        //HabilitaBotonCamposJoinSubentidade((tabEntidades.selectedIndex>-1));
        CubreControlesSubEntidad();
    }
    
    function CubreControlesSubEntidad()
    {
        if (tabEntidades.selectedIndex>-1) {
            var cod=tabEntidades.getLinea(tabEntidades.selectedIndex)['COD_ESTRUCTURA'];
            ncomboEstructuraInforme.buscaCodigo(cod);
        } else ncomboEstructuraInforme.selectItem(-1);
    }
    
    function HabilitaBotonMaisSubentidade(est)
    {
        var cond=!(est && true );
        getObjetoPorId('BotonMaisSubentidade').disabled=cond;
    }
    
    function HabilitaBotonMenosSubentidade(est)
    {
        
        var cond=!(est && true && (tabEntidades.selectedIndex>-1));
        getObjetoPorId('BotonMenosSubentidade').disabled=cond;
    }
    
    function HabilitaBotonValidarSubentidade(est)
    {
        var cond=!(est && true );
        getObjetoPorId('BotonValidarSubentidade').disabled=cond;
    }
    
    
    function EditableControlesSubentidade(estado)
    {
        if (estado) ncomboEstructuraInforme.activate();
        else ncomboEstructuraInforme.deactivate();
        
        HabilitaBotonValidarSubentidade(estado);
    }
    
    function EditableBusquedaSubentidade(estado) {
        HabilitaBotonMaisSubentidade(estado);
        HabilitaBotonMenosSubentidade(estado);
        
    }
    
    
    
    function AccionMaisSubentidade()
    {
        tabEntidades.readOnly=true;
        EditableControlesSubentidade(true);
        //ncomboEntidadeInforme.base.focus();
    }
    
    function AccionMenosSubentidade()
    {
        if (tabEntidades.selectedIndex>-1) {
            // Está seguro
            tabEntidades.removeLinea(tabEntidades.selectedIndex);
        }
        
    }
    
    function AccionValidarSubentidade()
    {
        var linea=new Array();
        if (!estaEntidadEnLista()) {
            linea[0]=ncomboEstructuraInforme.cod.value;
            linea[1]=ncomboEstructuraInforme.des.value;
            //	linea[2]=BuscaNomeEntidade(ncomboEstructuraInforme.cod.value);
            
            linea['COD_ESTRUCTURA']=ncomboEstructuraInforme.cod.value;
            tabEntidades.addLinea(linea);
            //alert('Meti en linea:'+linea+'.en cod_ent:'+linea['COD_ENTIDADEINFORME']+'.');
        } else jsp_alerta('A','Subentidad ya añadida');
        tabEntidades.readOnly=false;
        EditableControlesSubentidade(false);
        EditableBusquedaSubentidade(true);
        
    }
    
    
    function estaEntidadEnLista()
    {
        
        var atopado=false;
        var valor=ncomboEstructuraInforme.cod.value;
        for (var i=0;( (i<tabEntidades.lineas.length) && (!atopado) );i++) {
            atopado=(tabEntidades.getLinea(i)['COD_ESTRUCTURA']==valor);
        }
        return atopado;
    }
    
    function EditableControlesBusqueda(estado)
    {
        
        EditableBusquedaSubentidade(estado);
    }
    
    
    InicializaPestana();
    
    </script>
    </html:html>
    
