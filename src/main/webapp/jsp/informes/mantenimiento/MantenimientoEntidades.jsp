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
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<style type="text/css">

   html>body .cuadroFondoBlanco {
    width: 100%;
    background-color: #fffaff;
    border: 1px solid #fffaff;
    padding: 5px;
}


</style>

<%
            Log m_log = LogFactory.getLog(this.getClass().getName());
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 3;
            int usuCod = 0;
            String css = "";
            String[] params = null;

            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
                apl = usuarioVO.getAppCod();
                usuCod = usuarioVO.getIdUsuario();
                params = usuarioVO.getParamsCon();
                css = usuarioVO.getCss();
            }


            es.altia.common.service.config.Config m_ConfigTechnical = es.altia.common.service.config.ConfigServiceHelper.getConfig("techserver");
            String url = request.getContextPath() + m_ConfigTechnical.getString("URL.caducaSesion");
            try {
                session = request.getSession(true);
                if (m_log.isInfoEnabled()) {
                    m_log.info("checkSession: " + session.getId());
                }
                if ((session.getAttribute("usuarioEscritorio") == null)) {
                    if (m_log.isInfoEnabled()) {
                        m_log.info("checkSession: session.isNew()");
                    }
                    response.sendRedirect(response.encodeURL(url));

                }

            } catch (Exception e) {
                if (m_log.isInfoEnabled()) {
                    m_log.info("Error en checkSession");
                }
            }
            String arrayJScript = null;
            String estado = ((request.getParameter("op") != null) && (request.getParameter("op").equals("AE"))) ? "ENGADIR" : ((request.getParameter("op").equals("ME")) ? "MODIFICAR" : "CONSULTA");
            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
%>


<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<html:html>
<head>
<title>Xerador de Informes</title>
<jsp:include page="/jsp/informes/tpls/app-constants.jsp" />
<style>
    select.selectOculto{
        width: 1px;
        height: 1px;
        visibility:hidden;
    }
</style>
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
    
    <body  class="bandaBody" onLoad="pleaseWait('off');" style="margin:0;" scroll="no"> <!-- onLoad="inicializar();" --><!---->
  
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiq_MantEntidades")%></div>
        <div class="contenidoPantalla">

    <html:form action="/informes/mantenimiento/MantenimientosInformesAction.do" method="POST" target="oculto" styleId="MantenimientosInformesForm">
         <html:hidden property="textOperacion" styleId="textOperacion" />
         <html:hidden property="COD_INFORMEXERADOROculto" styleId="COD_INFORMEXERADOROculto" />
         <html:hidden property="COD_ENTIDADEINFORMEOculto" styleId="COD_ENTIDADEINFORMEOculto" />
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

    <html:select property="listaCampoInformeCodigo" styleId="listaCampoInformeCodigo" styleClass="selectOculto" multiple="true" style="width:0;height:0;">
    </html:select>

    <html:select property="listaCampoInformeNome" styleId="listaCampoInformeNome" styleClass="selectOculto" multiple="true" style="width:0;height:0;">
    </html:select>
    <html:select property="listaCampoInformeCampo" styleId="listaCampoInformeCampo" styleClass="selectOculto" multiple="true" style="width:0;height:0;">
    </html:select>
    <html:select property="listaCampoInformeTipo" styleId="listaCampoInformeTipo" styleClass="selectOculto" multiple="true" style="width:0;height:0;">
    </html:select>
    <html:select property="listaCampoInformeLonxitude" styleId="listaCampoInformeLonxitude" styleClass="selectOculto" multiple="true" style="width:0;height:0;">
    </html:select>
    <html:select property="listaCampoInformeNomeas" styleId="listaCampoInformeNomeas" styleClass="selectOculto" multiple="true" style="width:0;height:0;">
    </html:select>


    <html:select property="listaAplAplcod" styleId="listaAplAplcod" styleClass="selectOculto" multiple="true" style="width:0;height:0;">
    </html:select>


    <html:select property="listaSubentidadeCodigo" styleId="listaSubentidadeCodigo" styleClass="selectOculto" multiple="true" style="width:0;height:0;">
    </html:select>
    <html:select property="listaSubentidadeCampoJoinCampoEnt" styleId="listaSubentidadeCampoJoinCampoEnt" styleClass="selectOculto" multiple="true" style="width:0;height:0;">
    </html:select>
    <html:select property="listaSubentidadeCampoJoinCampoSubent" styleId="listaSubentidadeCampoJoinCampoSubent" styleClass="selectOculto" multiple="true" style="width:0;height:0;">
    </html:select>
    <html:select property="listaSubentidadeCampoJoinOuterJoin" styleId="listaSubentidadeCampoJoinOuterJoin" styleClass="selectOculto" multiple="true" style="width:0;height:0;">
    </html:select>

                        <TABLE  width="100%">
                            <tr>
                                <td >
                                    <table border="0" cellpadding="1" cellspacing="0">
                                        <tr>
                                            <td class="etiqueta"><%= descriptor.getDescripcion("gEtiq_NomEnt")%></td>
                                            <td>&nbsp;<html:text styleClass="inputTexto" property="nomeEntidade" styleId="nomeEntidade" size="30" onblur="return xAMayusculas(this);"/></td>
                                        </tr>
                                        <tr>
                                            <td class="etiqueta"><%= descriptor.getDescripcion("gEtiq_VisEnt")%></td>
                                            <td>&nbsp;<html:text styleClass="inputTexto" property="nomeVista" styleId="nomeVista" size="25" onblur="return xAMayusculas(this);"/></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            
                            <tr>
                                <td style="height:1%">&nbsp;</td>
                            </tr>
                            
                            <tr>
                                <td>
                                    <div class="tab-pane" id="tab-pane-1">
                                        
                                        <script type="text/javascript">
                                            tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
                                        </script>
                                        
                                        <div class="tab-page" id="tabPage1" style="height:390px;">
                                            
                                            <h2 class="tab" style="margin-left:10px"><!--Datos informe  -->Campos<!-- %=descriptor.getDescripcion("tit_BuzEnt")%>--></h2>
                                            
                                            <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>
                                            
                                            <!--  <div id="divPant1" style="position:absolute;offsetLeft:'0px';offsetTop:'0px';width:'100%';height:'100%';visibility:hidden;"> -->
                                            <TABLE id ="tablaDatosGral" class="contenidoPestanha"  border="0px" style="width:850px;">
                                                <tr>
                                                    <td style="width: 30%;height:20px;" class="sub3titulo">Campos<!--<%=descriptor.getDescripcion("gEtiq_CampInforme")%>--></td>
                                                </tr>
                                                
                                                <tr>
                                                    <td style="width:100%;" valign="top">
                                                        <table cellspacing="0px" class="cuadroFondoBlanco"  style="padding-top: 5px; padding-bottom: 5px; " cellpadding="5px" border="0">
                                                            <tr>
                                                                <td colspan="5">
                                                                    <table cellpadding="2">
                                                                        <tr><td rowspan="2" id="tablaC" valign="bottom"></td>
                                                                            <td valign="bottom" ><input type="button" class="botonGeneral" name="BotonMaisCampo" id="BotonMaisCampo" value="+" onclick="javascript:PulsaBoton(event);" style="width:25;"></td>
                                                                        </tr>
                                                                        <tr><td valign="top"><input type="button" class="botonGeneral" name="BotonMenosCampo" id="BotonMenosCampo" value="-" onclick="javascript:PulsaBoton(event);" style="width:25;"></td>
                                                                        </tr>
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                            <tr >
                                                                <td style="width:20%" align="center">
                                                                    <input type="text" name="textNomeCampo" id="textNomeCampo" size="30" maxlength="30" style="width:200px" class="inputTextoObligatorio" onkeyup="return xAMayusculas(this);">
                                                                </td>
                                                                <td style="width:20%" align="center">
                                                                    <input type="text" name="textCampoCampo" id="textCampoCampo" size="30" maxlength="30" style="width:200px" class="inputTextoObligatorio" onkeyup="return xAMayusculas(this);">
                                                                </td>
                                                                <td style="width:12%" align="center">
                                                                    <input type="text" style="visibility:hidden;height:0;width:0;" id="codTipoCampo" name="codTipoCampo"/> 
                                                                    <input class="inputTextoObligatorio" type="text" id="descTipoCampo" name="descTipoCampo" size="2" style="width:80px">
                                                                    <a href="" id="anchorTipoCampo" name="anchorTipoCampo"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonTipoCampo" name="botonTipoCampo" style="cursor:hand;"></span></a>
                                                                </td>
                                                                <td style="width:3%">
                                                                    <input type="text" name="textLonxitudeCampo" id="textLonxitudeCampo" size="2" maxlength="3" class="inputTextoObligatorio" onkeyup="return SoloDigitosNumericos(this);">
                                                                </td>
                                                                <td style="text-align:left;width:27%">
                                                                    <input type="text" name="textNomeAsCampo" id="textNomeAsCampo" size="20" maxlength="30" style="width:130px" class="inputTextoObligatorio" onkeyup="return xAMayusculas(this);">
                                                                    <input type="button" class="botonGeneral" name="BotonValidarCampo" id="BotonValidarCampo" value="Validar" onclick="javascript:PulsaBoton(event);">
                                                                </td>
                                                                
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                
                                            </table>
                                        </div>
                                        <!-- Fin de DIV de Catalogación  -->
                                        <div class="tab-page" id="tabPage2"  style="height:390px;">
                                            
                                            <h2 class="tab"><%= descriptor.getDescripcion("gEtiq_Aplics")%></h2>
                                            
                                            <script type="text/javascript">tp1_p2 = tp1.addTabPage( document.getElementById( "tabPage2" ) );</script>
                                            
                                            <!-- <div id="divPant2" style="position:absolute;offsetLeft:'0px';offsetTop:'0px';width='100%';height='100%';visibility:hidden;" > -->
                                          <TABLE id ="tablaDatosGral" class="contenidoPestanha"  border="0px">
                                                <!-- -->

                                                <tr>
                                                   <td style="width: 30%;height:20px;" class="sub3titulo"><%=descriptor.getDescripcion("gEtiq_Aplics")%></td>
                                                </tr>
                                                
                                                <tr>
                                                    <td style="width:100%;" valign="top">
                                                       <table cellspacing="0px" class="cuadroFondoBlanco"  style="padding-top: 5px; padding-bottom: 5px;" cellpadding="5px" border="1">
                                                            
                                                            <tr> <!-- Aquí la tabla con la lista de campos de la entidad -->
                                                                <td width="90%" >
                                                                    <table border="0" cellpadding="2" cellspacing="0" width="80%">
                                                                        <tr><td rowspan="2" id="tablaA" width="100%" height="85%" valign="bottom"></td><td valign="bottom"><input type="button" class="botonGeneral" name="BotonMaisAplicacion" id="BotonMaisAplicacion" value="+" onclick="javascript:PulsaBoton(event);" style="width:25;"></td>
                                                                        </tr>
                                                                        <tr><td valign="top"><input type="button" class="botonGeneral" name="BotonMenosAplicacion" id="BotonMenosAplicacion" value="-" onclick="javascript:PulsaBoton(event);" style="width:25;"></td>
                                                                        </tr>
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td class="etiqueta"><%= descriptor.getDescripcion("gEtiq_Aplicacion")%>&nbsp;&nbsp;&nbsp;&nbsp;<html:text style="visibility:hidden;height:0;width:0;" styleId="codAplicacion" property="codAplicacion"/> <input class="inputTexto" type="text" id="descAplicacion" name="descAplicacion" style="width:200;"><a href="" id="anchorAplicacion" name="anchorAplicacion"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonAplicacion" name="botonAplicacion" style="cursor:hand;"></span></a>&nbsp;<input type="button" class="botonGeneral" name="BotonValidarAplicacion" id="BotonValidarAplicacion" value="Validar" onclick="javascript:PulsaBoton(event);"></td>
                                                            </tr>
                                                       </table>
                                                    </td>
                                                </tr>
                                                
                                            </table>
                                        </div>
                                        <!-- Fin de DIV de campos -->
                                        <div class="tab-page" id="tabPage3"  style="height:390px;">
                                            
                                            <h2 class="tab">Subentidades</h2>
                                            
                                            <script type="text/javascript">tp1_p3 = tp1.addTabPage( document.getElementById( "tabPage3" ) );</script>
                                            
                                            
                                            <!--  <div id="divPant3" style="position:absolute;offsetLeft:'0px';offsetTop:'0px';width='100%';height='100%';visibility:hidden;"> -->
                                            <table width="100%" border="0" cellspacing="0" cellpadding="1" class="panel">
                                                <!-- -->

                                                <tr>
                                                   <td style="width: 30%;height:20px;" class="sub3titulo">Subentidades <!--<%=descriptor.getDescripcion("gEtiq_OrdInforme")%>--></td>
                                                </tr>
                                                <tr>
                                                <td style="width:100%;" valign="top">
                                                       <table cellspacing="0px" class="cuadroFondoBlanco"  style="padding-top: 5px; padding-bottom: 5px;" cellpadding="5px" border="1">
                                                            


                                                            <tr><td width="100%" ><table width="50%" cellspacing="0" cellpadding="0" border="0">
                                                                        <tr><!-- Aquí la tabla con la lista de subentidades -->
                                                                        <tr> 
                                                                            <td>
                                                                                <table border="0" cellpadding="2" cellspacing="0">
                                                                                    <tr><td rowspan="2" id="tablaE" width="100%" height="85%" valign="bottom"></td><td valign="bottom">&nbsp;<input type="button" class="botonGeneral" name="BotonMaisSubentidade" id="BotonMaisSubentidade" value="+" onclick="javascript:PulsaBoton(event);" style="width:25;"></td>
                                                                                    </tr>
                                                                                    <tr><td valign="top">&nbsp;<input type="button" class="botonGeneral" name="BotonMenosSubentidade" id="BotonMenosSubentidade" value="-" onclick="javascript:PulsaBoton(event);" style="width:25;"></td>
                                                                                    </tr>
                                                                                </table>
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <!-- Aquí el combo de entidades -->
                                                                            <td width="100%"> <html:text style="width:30;" disabled="<%=true%>" styleId="codEntidadeInforme" property="codEntidadeInforme" styleClass="inputTexto"/> <input class="inputTexto" type="text" id="descEntidadeInforme" name="descEntidadeInforme" style="width:200;">
                                                                            <a href="" id="anchorEntidadeInforme"  name="anchorEntidadeInforme"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonEntidadeInforme" name="botonEntidadeInforme" style="cursor:hand;"></span></a>&nbsp;&nbsp;<input type="button" class="botonGeneral" name="BotonValidarSubentidade" id="BotonValidarSubentidade" value="Validar" onclick="javascript:PulsaBoton(event);" >&nbsp;<input type="button" class="botonLargo" name="BotonCamposJoinSubentidade" id="BotonCamposJoinSubentidade" value="Campos de join" onclick="javascript:PulsaBoton(event);">&nbsp; </td>
                                                                        </tr>
                                                                        
                                                        </table></td></tr></table>
                                                    </td>
                                                </tr>
                                                
                                            </table>
                                        </div>
                                        <!-- FIn de DIV Ordenacion -->

                                    </div> <!-- Fin del div de fuera tab-pane -->
                                </td>
                            </tr>
                        </TABLE>
                    </html:form>
        <div class="botoneraPrincipal">
                    <input class="botonGeneral" name="BotonAceptar"  id="BotonAceptar" type="button" value="Aceptar" accesskey="A"
                           onClick="PulsaBoton(event)">
                    <input name="BotonCancelar" type="button" class="botonGeneral" id="BotonCancelar" accesskey="C"
                       onClick="PulsaBoton(event)" value="Cancelar"> </td>
        </div>
    </div>
</body>
    
<script type='text/javascript'>
        tp1.setSelectedIndex(0);
        var tabEntidades;
        var tabCampos;
        var tabAplicaciones;
        
        // Tabla de entidades
         tabEntidades = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaE',800));

        tabCampos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaC',800));

        tabAplicaciones = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaA',800));
        
        tabEntidades.addColumna('100','center','<%=descriptor.getDescripcion("gEtiq_CodEntidad")%>');
            tabEntidades.addColumna('700','left','<%=descriptor.getDescripcion("gEtiq_NombreEntidad")%>');
                
                
                
                
                tabEntidades.displayCabecera=true;  
                tabEntidades.displayTabla();
                tabEntidades.displayDatos=EventoCambioTablaEntidades;
                
                // Tabla de campos
                
                
                tabCampos.addColumna('240','center','<%=descriptor.getDescripcion("gEtiq_Nombre")%>');
                    tabCampos.addColumna('240','left','Campo');
                    tabCampos.addColumna('30','left','Tipo');
                    tabCampos.addColumna('50','left','<%=descriptor.getDescripcion("gEtiq_Long")%>');
                        tabCampos.addColumna('240','left','<%=descriptor.getDescripcion("gEtiq_Nomeas")%>');
                            
                            
                                                
                            tabCampos.displayCabecera=true;  
                            tabCampos.displayTabla();
                            tabCampos.displayDatos=EventoCambioTablaCampos;
                            
                            // Tabla de aplicaciones
                            
                            
                                tabAplicaciones.addColumna('100','center','<%=descriptor.getDescripcion("gEtiq_Codigo")%>');
                                tabAplicaciones.addColumna('700','left','<%=descriptor.getDescripcion("gEtiq_Nombre")%>');
                                    
                                    
                                    tabAplicaciones.displayCabecera=true;  
                                    tabAplicaciones.displayTabla();
                                    tabAplicaciones.displayDatos=EventoCambioTablaAplicaciones;
                                    
                                    var ncomboEntidadeInforme =new Combo('EntidadeInforme');
                                    var mensaxeIncorrectoInforme='Por favor, introduza alomenos o nome do informe e un campo para o informe.';
                                    var mensaxeIncorrectoCamposJoin='Debe introducir al menos un campo de join para cada subentidad.';
                                    
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
                                                
                                                
                                                
                                                var VectorGlobalCamposPorEntidade=new Array();
                                                var VectorGlobalAplicacions;
                                                
                                                
                                                
                                                var Estado = '<%=estado%>';
                                                
                                                var TiposActualesCamposCondicion=new Array();
                                                
                                                //
                                                // Objetos Javascript represenatando a los campos HTML
                                                //
                                                var formulario=getObjetoPorId('MantenimientosInformesForm');
                                                
                                                var textOperacion=formulario.textOperacion;
                                                
                                                
                                                var comboEntidadeInforme = formulario.comboEntidadeInforme;
                                                var comboFormatoInforme = formulario.comboFormatoInforme;
                                                
                                                
                                                var comboCamposElexidos = formulario.comboCamposElexidos;
                                                var COD_INFORMEXERADOROculto = formulario.COD_INFORMEXERADOROculto;
                                                
                                                
                                                var codAplicacion= formulario.codAplicacion;
                                                
                                                
                                                //
                                                //
                                                //
                                                
                                                
                                                var textNomeCampo=formulario.textNomeCampo;
                                                var textCampoCampo=formulario.textCampoCampo;
                                                
                                                var textLonxitudeCampo=formulario.textLonxitudeCampo;
                                                var textNomeAsCampo=formulario.textNomeAsCampo;
                                                var listaAplAplcod=formulario.listaAplAplcod;
                                                
                                                var listaCampoInformeCodigo=formulario.listaCampoInformeCodigo;
                                                
                                                var listaCampoInformeNome=formulario.listaCampoInformeNome;
                                                
                                                var listaCampoInformeCampo=formulario.listaCampoInformeCampo;
                                                
                                                var listaCampoInformeTipo=formulario.listaCampoInformeTipo;
                                                
                                                var listaCampoInformeLonxitude=formulario.listaCampoInformeLonxitude;
                                                
                                                var listaCampoInformeNomeas=formulario.listaCampoInformeNomeas;
                                                
                                                var listaSubentidadeCodigo=formulario.listaSubentidadeCodigo;
                                                
                                                var listaSubentidadeCampoJoinCampoEnt=formulario.listaSubentidadeCampoJoinCampoEnt;
                                                
                                                var listaSubentidadeCampoJoinCampoSubent=formulario.listaSubentidadeCampoJoinCampoSubent;
                                                
                                                var listaSubentidadeCampoJoinOuterJoin=formulario.listaSubentidadeCampoJoinOuterJoin;
                                                
                                                
                                                
                                                var codCampoInformeOculto=formulario.codCampoInformeOculto;
                                                
                                                
                                                
                                                var ncomboAplicacion=new Combo('Aplicacion');
                                                var ncomboTipoCampo=new Combo('TipoCampo');
                                                var nomeEntidade=formulario.nomeEntidade;
                                                var nomeVista=formulario.nomeVista;
                                                var VectorEntidade=new Array();
                                                var VectorCampos=new Array();
                                                var VectorAplicaciones=new Array();
                                                var VectorSubEntidades=new Array();
                                                
                                                
                                                var BotonMaisSubentidade=formulario.BotonMaisSubentidade;
                                                var BotonMenosSubentidade=formulario.BotonMenosSubentidade;
                                                var BotonValidarSubentidade=formulario.BotonValidarSubentidade;
                                                var BotonCamposJoinSubentidade=formulario.BotonCamposJoinSubentidade;
                                                var BotonMaisCampo=formulario.BotonMaisCampo;
                                                var BotonMenosCampo=formulario.BotonMenosCampo;
                                                var BotonValidarCampo=formulario.BotonValidarCampo;
                                                var BotonMaisAplicacion=formulario.BotonMaisAplicacion;
                                                var BotonMenosAplicacion=formulario.BotonMenosAplicacion;
                                                var BotonValidarAplicacion=formulario.BotonValidarAplicacion;
                                                
                                                var COD_ENTIDADEINFORMEOculto=formulario.COD_ENTIDADEINFORMEOculto;
                                                
                                                function InicializaPestana()
                                                {   
                                                    CargaVectorGlobalCamposPorEntidade();
                                                    CargaVectorGlobalAplicacions();
                                                    
                                                    CargaComboEntidades();
                                                    
                                                    CargaComboAplicacions();
                                                    
                                                    CargaComboTipoCampo();
                                                    
                                                    if ( (Estado=='CONSULTA') || (Estado=='MODIFICAR') ) 
                                                        {
                                                            COD_INFORMEXERADOROculto.value='<%=request.getParameter("id")%>';
                                                            COD_ENTIDADEINFORMEOculto.value='<%=request.getParameter("id")%>';
                                                  
                                                            CubreControlesEntidad();
                                                            if (Estado=='CONSULTA') EstableceEstadoConsulta()
                                                                else EstableceEstadoModificar();
                                                        } else {
                                                        codAplicacion.value='<%=request.getParameter("id")%>';
                                                        COD_ENTIDADEINFORMEOculto.value='<%=request.getParameter("id")%>';
                                                 
                                                        EstableceEstadoEngadir();
                                                    }
                                                    
                                                    
                                                    
                                                    
                                                }
                                                
                                                function CargaComboTipoCampo()
                                                {
                                                    ncomboTipoCampo.addItems(['A','N','D'],['Alfanumérico','Numérico','Fecha']);
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
                                                        case 'BotonMaisSubentidade':
                                                        AccionMaisSubentidade();
                                                        break;
                                                        case 'BotonMenosSubentidade':
                                                        AccionMenosSubentidade();
                                                        break;
                                                        case 'BotonValidarSubentidade':
                                                        AccionValidarSubentidade();
                                                        break;
                                                        case 'BotonCamposJoinSubentidade':
                                                        AccionCamposJoinSubentidade();
                                                        break;
                                                        case 'BotonMaisCampo':
                                                        AccionMaisCampo();
                                                        break;
                                                        case 'BotonMenosCampo':
                                                        AccionMenosCampo();
                                                        break;
                                                        case 'BotonValidarCampo':
                                                        AccionValidarCampo();
                                                        break;
                                                        case 'BotonMaisAplicacion':
                                                        AccionMaisAplicacion();
                                                        break;
                                                        case 'BotonMenosAplicacion':
                                                        AccionMenosAplicacion();
                                                        break;
                                                        case 'BotonValidarAplicacion':
                                                        AccionValidarAplicacion();
                                                        break;
                                                        
                                                        default:break;
                                                    }
                                                }
                                                
                                                function AccionAltaEntidad()
                                                {
                                                    var lin;
                                                    //if (Trim(codAplicacion.value)!='') {
                                                    
                                                    AltaEntidad(codAplicacion.value);
                                                    //} else jsp_alerta('A','Debe elegir una aplicación.');
                                                    
                                                }
                                                
                                                
                                                function AltaEntidad(codAplicacion){
                                                    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/informes/mantenimiento/main2.jsp?op=CJ&id='+codAplicacion,null,
                                                        'width=795,height=580,scrollbars=no,status='+ '<%=statusBar%>',function(datos){
                                                                if(datos!=undefined){
                                                                    //recargarLista();
                                                                    meteCamposJoinEnEntidade();
                                                                }
                                                        });        
                                                    }
                                                    
                                                    
                                                    
                                                    
                                                    
                                                    
                                                    
                                                    function CargaComboEntidades()
                                                    {
                                                        //VectorGlobalCamposPorEntidade[] [CODIGO,NOME,COD_CAMPOINFORME, NOMECAMPO,TIPO,LONXITUDE]
                                                        var fila;
                                                        var miOption;
                                                        var anterior='';
                                                        var cods=new Array();
                                                        var descs=new Array();
                                                        
                                                        //
                                                        // Arrays para comboNuevo
                                                        //
                                                        //	var codsEntidade=new Array();
                                                        //	var descsEntidade=new Array();
                                                        
                                                        //LimpiaCombo(comboEntidadeInforme);
                                                        var cont=0;
                                                        for (var i=0;i<VectorGlobalCamposPorEntidade.length;i++)
                                                            {
                                                                fila=VectorGlobalCamposPorEntidade[i];
                                                                if (fila[0]!=anterior) {
                                                                    cods[cont]=fila[0];
                                                                    descs[cont]=fila[1];
                                                                    cont++;
                                                                    //comboEntidadeInforme.options[comboEntidadeInforme.options.length]=miOption;
                                                                    //
                                                                    // Aquí preparo los arrays para el combo Nuevo
                                                                    //
                                                                    //codsEntidade[codsEntidade.length]=fila[0];
                                                                    //descsEntidade[descsEntidade.length]=fila[1];
                                                                    anterior=fila[0];
                                                                }
                                                            }
                                                            //alert('ANtes ciods:'+cods+'.descs:'+descs+'.');
                                                            ncomboEntidadeInforme.addItems(cods,descs);
                                                            
                                                            if (ncomboEntidadeInforme.codigos.length>0) {
                                                                ncomboEntidadeInforme.selectedIndex=-1;
                                                                //EventoComboEntidades();
                                                            }
                                                            
                                                            //cargaObjetoComboDesdeCombo(ncomboEntidadeInforme,comboEntidadeInforme);
                                                        }
                                                        
                                                        
                                                        
                                                        
                                                        
                                                        
                                                        
                                                        function AccionEliminar()
                                                        {   
                                                            if (jsp_alerta('C',estaSeguroBen())==1)//(window.confirm(estaSeguroBen()))
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
                                                                self.parent.opener.retornoXanelaAuxiliar();
                                                            }
                                                            
                                                            function AceptarConsulta()
                                                            {
                                                                self.parent.opener.retornoXanelaAuxiliar();
                                                            }
                                                            
                                                            
                                                            function AceptarEngadir()
                                                            {
                                                                if (estaCorrectoEntidade()) {
                                                                    if (estaCorrectoCamposJoin()) {
                                                                        if (jsp_alerta('C',estaSeguroBen())==1) {
                                                                            mostrarDiv(1);
                                                                            textOperacion.value='AE';
                                                                            PreparaInformeEnviar();
                                                                            formulario.action = URLBaseAction+'AE';
                                                                            formulario.target = 'oculto';
                                                                            formulario.submit();
                                                                        }
                                                                    } else {
                                                                    jsp_alerta('A',mensaxeIncorrectoCamposJoin);
                                                                }
                                                            } else jsp_alerta('A',mensaxeIncorrectoInforme);
                                                        }
                                                        
                                                        
                                                        
                                                        function callbackAceptarEngadir(num)
                                                        {
                                                            self.parent.opener.retornoXanelaAuxiliar(num);
                                                        }
                                                        
                                                        
                                                        function AceptarModificar()
                                                        {
                                                            if (estaCorrectoEntidade()) {
                                                                if (estaCorrectoCamposJoin()) {
                                                                    if (jsp_alerta('C',estaSeguroBen())==1) {
                                                                        mostrarDiv(1);
                                                                        textOperacion.value='ME';
                                                                        textOperacion.value='ME';
                                                                        PreparaInformeEnviar();
                                                                        formulario.action = URLBaseAction+'ME';
                                                                        formulario.target = 'oculto';
                                                                        formulario.submit();
                                                                    }
                                                                } else {
                                                                jsp_alerta('A',mensaxeIncorrectoCamposJoin);
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
                                                        //EditableControlesBusqueda(true);
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
                                                        
                                                        
                                                        EditableControles(false);
                                                        
                                                        //mostrarDiv(1);
                                                        //CubreListaCamposDisponibles();
                                                        
                                                        //EventoCancelarTipoSaida();
                                                        //textNomeInforme.focus();
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
                                                        EditableControles(false);
                                                        
                                                        //EventoCancelarTipoSaida();
                                                        //textNomeInforme.focus();
                                                    }
                                                    
                                                    function estaCorrectoCamposJoin() {
                                                        var esCorrecto=true;
                                                        if (tabEntidades.lineas.length) {
                                                            for (var i=0;i<tabEntidades.lineas.length;i++) {
                                                                arrayCJ=tabEntidades.getLinea(i)['CAMPOSJOIN'];
                                                                if (!((arrayCJ) && (arrayCJ.length>0))) {
                                                                    esCorrecto=false;
                                                                }
                                                            }
                                                        }
                                                        return esCorrecto;
                                                    }
                                                    
                                                    function estaCorrectoEntidade()
                                                    //
                                                    // Comproba que alomenos fosen introducidos os datos mínimos para construir un informe
                                                    //
                                                    {
                                                        var salida=false;
                                                        salida=(tabAplicaciones.lineas.length>0) && (tabCampos.lineas.length>0) && (Trim(nomeEntidade.value)!='') && (Trim(nomeVista.value)!='');
                                                        
                                                        return salida;
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

                if (request.getParameter("op").equals("AE")) {
                    cualApli = request.getParameter("id");
                } else {
                    cualApli = request.getParameter("codApliInforme");
                }
                tempTabla.put("APL_COD", cualApli);//USUARIO.getCOD_PESTANA());
                tempTabla.put("PARAMS", params);//USUARIO.getCOD_PESTANA());
                if (request.getParameter("op").equals("ME")) {
                    tempTabla.put("COD_ENTIDADEINFORME", request.getParameter("id"));
                }
                //es.altia.util.Debug.println("EN JSPIII MNUCOD ES:"+pestanaOrixe+".");
                arrayJScript = es.altia.agora.business.geninformes.utils.UtilidadesXerador.ObtenerArrayJSEntidadesInforme(tempTabla);
            } catch (Exception e) {
            //es.altia.util.Debug.printException(e.getExcepcion());
            }
%>
    VectorGlobalCamposPorEntidade=unescapeArray(<%=arrayJScript%>);
 
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    function EditableControles(NuevoEstado)
    {
        var cond=!(NuevoEstado && true);
        var cond2=!(NuevoEstado && true && Estado=='ENGADIR');
        var que;
        
        /*nomeEntidade.disabled=cond;
        nomeVista.disabled=cond;*/
        EditableControlesSubentidade(NuevoEstado);
        EditableControlesCampo(NuevoEstado);
        EditableControlesAplicacion(NuevoEstado);
        
        
        
        
    }
    
    
    
    function PreparaInformeEnviar()
    {
        var miOption;
        
        codAplicacion.disabled=false;
        nomeEntidade.disabled=false;
        nomeVista.disabled=false;
        
        //
        // Cargo las listas
        //
        
        
        // Lista de entidades
        
        LimpiaCombo(listaSubentidadeCodigo);
        
        LimpiaCombo(listaSubentidadeCampoJoinCampoEnt);
        
        LimpiaCombo(listaSubentidadeCampoJoinCampoSubent);
        
        LimpiaCombo(listaSubentidadeCampoJoinOuterJoin);
        
        var arrayCJ;
        var lineaCJ;
        var indice=0;
        var base=0;
        var codigo;
        
        if (tabEntidades.lineas.length) {
            
            
            for (var i=0;i<tabEntidades.lineas.length;i++) {
                codigo=tabEntidades.getLinea(i)[0];
                
                arrayCJ=tabEntidades.getLinea(i)['CAMPOSJOIN'];
                if ( (arrayCJ) && (arrayCJ.length>0) ) {
                    for (var j=0;j<arrayCJ.length;j++) {
                        lineaCJ=arrayCJ[j];
                        indice=(base)+j;
                        miOption=new Option('',codigo);
                        miOption.selected=true;
                        listaSubentidadeCodigo.options[indice]=miOption;
                        
                        miOption=new Option('',lineaCJ[0]);
                        miOption.selected=true;
                        listaSubentidadeCampoJoinCampoEnt.options[indice]=miOption;
                        
                        
                        miOption=new Option('',lineaCJ[1]);
                        miOption.selected=true;
                        listaSubentidadeCampoJoinCampoSubent.options[indice]=miOption;
                        
                        
                        miOption=new Option('',lineaCJ[2]);
                        miOption.selected=true;
                        listaSubentidadeCampoJoinOuterJoin.options[indice]=miOption;
                        //alert('Meto en subents:'+codigo+'.'+lineaCJ[0]+'.'+lineaCJ[1]+'.'+lineaCJ[2]+'.');
                        //etc...
                        
                        
                    }
                    base+=arrayCJ.length;
                    
                }
            }
            
        } // if tabEntidades
        
        
        
        
        
        // Lista de Campos
        
        LimpiaCombo(listaCampoInformeCodigo);
        
        LimpiaCombo(listaCampoInformeNome);
        
        LimpiaCombo(listaCampoInformeCampo);
        
        LimpiaCombo(listaCampoInformeTipo);
        
        LimpiaCombo(listaCampoInformeLonxitude);
        
        LimpiaCombo(listaCampoInformeNomeas);
        
        
        if (tabCampos.lineas.length>0) {
            for (var i=0;i<tabCampos.lineas.length;i++) {
                miOption=new Option('',tabCampos.getLinea(i)['COD_CAMPOINFORME']);
                miOption.selected=true;
                listaCampoInformeCodigo.options[i]=miOption;
                
                miOption=new Option('',tabCampos.getLinea(i)[0]);
                miOption.selected=true;
                listaCampoInformeNome.options[i]=miOption;
                
                
                miOption=new Option('',tabCampos.getLinea(i)[1]);
                miOption.selected=true;
                listaCampoInformeCampo.options[i]=miOption;
                
                miOption=new Option('',tabCampos.getLinea(i)[2]);
                miOption.selected=true;
                listaCampoInformeTipo.options[i]=miOption;
                
                miOption=new Option('',tabCampos.getLinea(i)[3]);
                miOption.selected=true;
                listaCampoInformeLonxitude.options[i]=miOption;
                
                miOption=new Option('',tabCampos.getLinea(i)[4]);
                miOption.selected=true;
                listaCampoInformeNomeas.options[i]=miOption;
                
                
                
                
            }
            
            
        }
        
        // lista de aplicaciones
        LimpiaCombo(listaAplAplcod);
        if (tabAplicaciones.lineas.length>0) {
            for (var i=0;i<tabAplicaciones.lineas.length;i++) {
                miOption=new Option('',tabAplicaciones.getLinea(i)['APL_COD']);
                miOption.selected=true;
                listaAplAplcod.options[i]=miOption;
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
        temp['DESCRIPCION']=Trim(textDescripcion.value);
        //temp['FORMATO']=comboFormatoInforme.options[comboFormatoInforme.selectedIndex].value;
        //temp['COD_ENTIDADEINFORME']=comboEntidadeInforme.options[comboEntidadeInforme.selectedIndex].value;
        //temp['COD_FUNCION']='';
        
        return temp;
    }
    
    
    
    
    
    
    
    
    function CubreControlesEntidad()
    {	
        var tempAno;
        
        //if (tablaLateral.selectedIndex>=0)
        //{
        
        
        
        textOperacion.value='CE';
        //COD_INFORMEXERADOROculto.value='21';
        //alert('CIOculto:'+COD_INFORMEXERADOROculto.value+'.');
        
        //InicializaControles();
        VectorEntidade=new Array();
        VectorCampos=new Array();
        VectorAplicaciones=new Array();
        VectorSubEntidades=new Array();
        
        //alert('En cci');
        formulario.action = URLBaseAction+'CE';
        formulario.target = 'oculto';
        formulario.submit();
        //}
        
    }
    
    
    function callbackCubreControlesEntidad(vEntidad,vCampos,vAplicaciones,vSubEntidades)
    {	
        //
        //                                                                                                                                                                         
        // en vInforme viene [CODIGO,CENCOD,NOME,DESCRIPCION,FORMATO,CONSULTASQL,COD_ENTIDADEINFORME,COD_FUNCION,CAMPOSCONDICION,CAMPOSSELECCION,CAMPOSORDE,FORMATOINFORME ]
        //alert('Recibo en cbCCE:'+vEntidad+'..'+vCampos+'..'+vAplicaciones+'..'+vSubEntidades+'.');
        
        if ( (vEntidad) && (vEntidad.length>0) ) 
            {
                VectorEntidade=unescapeArray(vEntidad);
                VectorEntidade=VectorEntidade[0];
                VectorCampos=unescapeArray(vCampos);
                VectorAplicaciones=unescapeArray(vAplicaciones);
                VectorSubEntidades=unescapeArray(vSubEntidades);
                
                CubreControles();
            }

            if (Estado=='CONSULTA') EstableceEstadoConsulta();
            
        } 
        
        
        
        
        
        function CubreControles()
        {
            //alert('Vectores en CC:'+VectorEntidade+'..'+VectorCampos+'..'+VectorAplicaciones+'..'+VectorSubEntidades+'.');
            COD_ENTIDADEINFORMEOculto.value=VectorEntidade[0];
            nomeEntidade.value=VectorEntidade[1];
            nomeVista.value=VectorEntidade[2];
            
            CubreTablaCampos();
            CubreTablaAplicaciones();
            CubreTablaSubEntidades();
            
        }
        
        
        function CubreTablaCampos()
        {
            
            //CAMPOS [ CODIGO,NOME,CAMPO,TIPO,LONXITUDE,NOMEAS]
            var linea;
            for (var i=0;i<VectorCampos.length;i++) {
                linea=new Array();
                //linea[0]=VectorCampos[i][0];
                linea[0]=VectorCampos[i][1];
                linea[1]=VectorCampos[i][2];
                linea[2]=VectorCampos[i][3];
                linea[3]=VectorCampos[i][4];
                linea[4]=VectorCampos[i][5];
                linea['COD_CAMPOINFORME']=VectorCampos[i][0];
                tabCampos.addLinea(linea);
            }
        }
        
        function CubreTablaAplicaciones()
        {
           
            //APLICACIONES	[ COD_ENTIDADEINFORME,APL_COD,APL_NOM]
            for (var i=0;i<VectorAplicaciones.length;i++) {
                linea=new Array();
                linea[0]=VectorAplicaciones[i][1];
                linea[1]=VectorAplicaciones[i][2];
                linea['APL_COD']=VectorAplicaciones[i][1];;
                tabAplicaciones.addLinea(linea);
            }
            
        }
        
        function CubreTablaSubEntidades()
        {
           
            // SUBENTIDADES	[ESE.COD_ENTSUBENT, ENT, SUBENT, NOME, CAMPO_ENT, CAMPO_SUBENT, OUTER_JOIN ]
            if ( (VectorSubEntidades) && (VectorSubEntidades.length>0) ) {
                
                var antiguoSubent=VectorSubEntidades[0][2];
                var antiguoNome=VectorSubEntidades[0][3];
                
                var cambio=false;
                var arrayCJ=new Array();
                var cont=0;
                //arrayCJ [ [CAMPO_ENT,CAMPO_SUBENT,OUTER_JOIN]* ]
                var i;
                //alert('VSUbes:'+VectorSubEntidades+'.');
                for (i=0;i<VectorSubEntidades.length;i++) {
                    //alert('Entro en el for con i:'+i+'.'+VectorSubEntidades[i]+'.');
                    cambio=(antiguoSubent!=VectorSubEntidades[i][2]);
                    if (cambio) {
                        linea=new Array();
                        linea[0]=antiguoSubent;//VectorSubEntidades[i][2];
                        linea[1]=antiguoNome;//VectorSubEntidades[i][3];
                        linea['CAMPOSJOIN']=arrayCJ;
                        linea['COD_ENTIDADEINFORME']=VectorSubEntidades[i][2];
                        
                        arrayCJ=new Array();
                        cont=0;
                        tabEntidades.addLinea(linea);
                        antiguoSubent=VectorSubEntidades[i][2];
                        antiguoNome=VectorSubEntidades[i][3];
                        //alert('en cambio: meto'+linea+'.');
                    }
                    
                    if ( (VectorSubEntidades[i][4]) && (VectorSubEntidades[i][4].length>0) ) {
                        arrayCJ[cont]=new Array();
                        arrayCJ[cont][0]=VectorSubEntidades[i][4];
                        arrayCJ[cont][1]=VectorSubEntidades[i][5];
                        arrayCJ[cont][2]=VectorSubEntidades[i][6];
                        cont++;
                    }
                } // Fin del for
                
                if (VectorSubEntidades.length>0) { // último grupo
                    linea=new Array();
                    linea[0]=VectorSubEntidades[i-1][2];
                    linea[1]=VectorSubEntidades[i-1][3];
                    linea['COD_ENTIDADEINFORME']=VectorSubEntidades[i-1][2];
                    linea['CAMPOSJOIN']=arrayCJ;
                    arrayCJ=new Array();
                    cont=0;
                    tabEntidades.addLinea(linea);
                }
                
                
            } // Fin if externo
            EventoCambioTablaEntidades();
        }
        
        
        
        
        
        function InicializaControles()
        {
            nomeEntidade.value='';
            nomeVista.value='';
            InicializaControlesCampo();
            ncomboEntidadeInforme.selectItem(-1);	
            ncomboAplicacion.selectItem(-1);
        }
        
        
        
        function HabilitaAceptar(NuevoEstado) 
        {
            //HabilitaBoton(getObjetoPorId('BotonAceptar'),NuevoEstado && (Estado!='CONSULTA') && true);
            var valor=NuevoEstado && (Estado!='CONSULTA') && true;
            getObjetoPorId('BotonAceptar').disabled=!valor;
        }
        
        
        
        function HabilitaCancelar(NuevoEstado) 
        {
            var valor=NuevoEstado && (Estado!='CONSULTA') && true;
            getObjetoPorId('BotonCancelar').disabled=!valor;
        }
        
        
        
        
        function CargaVectorGlobalAplicacions()
        {
            
  <%
            arrayJScript = null;
            try {
                // es.altia.util.Debug.crearDirectorio();	
                //
                //
                es.altia.util.HashtableWithNull tempTabla = new es.altia.util.HashtableWithNull();
                //String pestanaOrixe=request.getParameter(ConstantesXerador.COD_PESTANAORIXE);
                tempTabla.put("AAU_USU", usuCod + "");//USUARIO.getCOD_PESTANA()); AAU_USU = ? AND AID_IDI = ?
                tempTabla.put("AID_IDI", idioma + "");
                tempTabla.put("PARAMS", params);//USUARIO.getCOD_PESTANA());
                tempTabla.put("TRAERGEN", "N");
                //es.altia.util.Debug.println("EN JSPIII MNUCOD ES:"+pestanaOrixe+".");
                arrayJScript = es.altia.agora.business.geninformes.utils.UtilidadesXerador.ObtenerArrayJSAplicacionesUsuario(tempTabla);
            } catch (Exception e) {
            //es.altia.util.Debug.printException(e.getExcepcion());
            }
  %>
      VectorGlobalAplicacions=unescapeArray(<%=arrayJScript%>);
      }
      
      
      
      function CargaComboAplicacions() {
          
          var cods=new Array();
          var descs=new Array();
          var par;
          
          for (var i=0;i<VectorGlobalAplicacions.length;i++) {
              par=VectorGlobalAplicacions[i];
              cods[i]=par[0];
              descs[i]=capitalize(par[1]);
          }
          ncomboAplicacion.addItems(cods,descs);
          
      }
      
      
      
      function EditableControlesSubentidade(estado)
      {
          if (estado) ncomboEntidadeInforme.activate();
          else ncomboEntidadeInforme.deactivate();
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
          EditableBusquedaSubentidade(false);
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
          var valor=ncomboEntidadeInforme.cod.value;
          if (Trim(valor)!='') {
              if (!estaEntidadEnLista()) {
                  linea[0]=ncomboEntidadeInforme.cod.value;
                  linea[1]=ncomboEntidadeInforme.des.value;
                  linea['COD_ENTIDADEINFORME']=ncomboEntidadeInforme.cod.value;
                  tabEntidades.addLinea(linea);
                  //alert('Meti en linea:'+linea+'.en cod_ent:'+linea['COD_ENTIDADEINFORME']+'.');
              } else jsp_alerta('A','<%=descriptor.getDescripcion("msjSubentAñadida")%>');
              } else jsp_alerta('A','<%=descriptor.getDescripcion("msjIntroSubent")%>');
                  tabEntidades.readOnly=false;
                  EditableControlesSubentidade(false);
                  EditableBusquedaSubentidade(true);
              }
              
              function estaEntidadEnLista()
              {
                  var atopado=false;
                  var valor=ncomboEntidadeInforme.cod.value;
                  
                  for (var i=0;( (i<tabEntidades.lineas.length) && (!atopado) );i++) {
                      atopado=(tabEntidades.getLinea(i)['COD_ENTIDADEINFORME']==valor);
                      
                  }
                  return atopado;
              }
              
              
              function AccionCamposJoinSubentidade()
              {
                  var camposEntidadePai=obtenerCamposActualesEntidade();
                  //alert('camposEntidadePai:'+camposEntidadePai+'.');
                  var datos;
                  var codSubentidad;
                  
                  var linea;
                  
                  if (tabCampos.lineas.length>0) {
                      if (tabEntidades.selectedIndex>-1) {
                          linea=tabEntidades.getLinea(tabEntidades.selectedIndex);
                          codSubentidad=linea[0];
                          //alert('codSub:'+codSubentidad);
                          var nomeSubentidade=linea[1];
                          var CJActual=tabEntidades.getLinea(tabEntidades.selectedIndex)['CAMPOSJOIN'];
                          var op=(Estado=='ENGADIR')?'A':((Estado=='MODIFICAR')?'M':'C');
                          datos=[nomeEntidade.value,nomeSubentidade,CJActual,camposEntidadePai];
                          var nada=new Date();
                          nada=nada.getTime();
                          
                          abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/informes/mantenimiento/CamposJoin.jsp?op='+op+"&id="+codSubentidad+"&rnd="+nada,datos,
                                'width=795,height=400,scrollbars=no,status='+ '<%=statusBar%>',function(datos2){
                                    if(datos2!=undefined){
                                        meteCamposJoinEnEntidade(datos2);
                                    }
                                });
                        }
                          
                      } else
                        jsp_alerta('A','<%=descriptor.getDescripcion("msjSinCamposDef1")%> ' + nomeEntidade.value + ' <%=descriptor.getDescripcion("msjSinCamposDef2")%>');
                      }
                      
                      function meteCamposJoinEnEntidade(arrayCJ)
                      {
                          // arrayCJ [ [CAMPO_ENT,CAMPO_SUBENT,OUTER_JOIN]* ]
                          var linea=tabEntidades.getLinea(tabEntidades.selectedIndex);
                          linea['CAMPOSJOIN']=arrayCJ;
                      }
                      
                      
                      function EditableControlesCampo(estado)
                      {
                          var cond=!(estado && true);
                          textNomeCampo.disabled=cond;
                          textCampoCampo.disabled=cond;
                          if (estado) ncomboTipoCampo.activate(); else ncomboTipoCampo.deactivate();
                          textLonxitudeCampo.disabled=cond;
                          textNomeAsCampo.disabled=cond;
                          HabilitaBotonValidarCampo(estado);	
                      }
                      
                      function EditableBusquedaCampo(estado)
                      {
                          HabilitaBotonMaisCampo(estado);
                          HabilitaBotonMenosCampo(estado);
                          
                      }
                      
                      function AccionMaisCampo()
                      {
                          tabCampos.readOnly=true;
                          EditableControlesCampo(true);
                          EditableBusquedaCampo(false);
                          // Poner foco en nomeCampo
                          textNomeCampo.value='';
                          textCampoCampo.value='';
                          ncomboTipoCampo.selectItem(-1);
                          textLonxitudeCampo.value='';
                          textNomeAsCampo.value='';
                          
                          textNomeCampo.focus();
                          
                          
                      }
                      
                      function AccionMenosCampo()
                      {
                          // arrayCJ [ [CAMPO_ENT,CAMPO_SUBENT,OUTER_JOIN]* ]
                          var arrayCJ;
                          var nuevoArray=new Array();
                          var cont=0;
                          
                          if (tabCampos.selectedIndex>-1) {
                              // Está seguro
                              
                              //
                              // Borro campos de join relacionados con este
                              //
                              var campo=tabCampos.getLinea(tabCampos.selectedIndex)[2];
                              for (var i=0;i<tabEntidades.lineas.length;i++) {
                                  arrayCJ=tabEntidades.getLinea(i)['CAMPOSJOIN'];
                                  cont=0;
                                  nuevoArray=new Array();
                                  if (arrayCJ!=null && arrayCJ!=undefined) {
                                      for (var j=0;j<arrayCJ.length;j++) {
                                          if (campo!=arrayCJ[j][0]) {
                                              nuevoArray[cont]=arrayCJ[j];
                                              cont++;
                                          } // if
                                      } //for (var j
                                      tabEntidades.getLinea(i)['CAMPOSJOIN']=nuevoArray;
                                  }
                              }
                              tabCampos.removeLinea(tabCampos.selectedIndex);
                          }
                      }
                      
                      
                      function AccionValidarCampo()
                      {
                          var correcto=(textNomeCampo.value!='') && (textCampoCampo.value!='') &&  ( (ncomboTipoCampo.cod.value=='D') || (ncomboTipoCampo.cod.value=='A') || (ncomboTipoCampo.cod.value=='N') ) && (textLonxitudeCampo.value!='') && (textNomeAsCampo.value!='');
                          var linea=new Array();
                          
                          if ((correcto) && (!estaCampoEnLista())) {
                              
                              linea[0]=textNomeCampo.value;
                              linea[1]=textCampoCampo.value;
                              linea[2]=ncomboTipoCampo.cod.value;
                              linea[3]=textLonxitudeCampo.value;
                              linea[4]=textNomeAsCampo.value;
                              linea['COD_CAMPOINFORME']=' ';
                              tabCampos.addLinea(linea);
                          } else jsp_alerta('A','Campo repetido.');
                          tabCampos.readOnly=false;
                          EditableControlesCampo(false);
                          EditableBusquedaCampo(true);
                      }
                      
                      function obtenerCamposActualesEntidade()
                      {
                          var salida=tabCampos.lineas;
                          //for (var i=0;i<tabCampos.lineas.length;i++) {
                          //	
                          //}
                          return salida;
                      }
                      
                      function estaCampoEnLista()
                      {
                          var atopado=false;
                          var valorNome=textNomeCampo.value;
                          var valorNomeAs=textNomeAsCampo.value;
                          
                          for (var i=0;( (i<tabCampos.lineas.length) && (!atopado) );i++) {
                              atopado=( (tabCampos.getLinea(i)[1]==valorNome) || (tabCampos.getLinea(i)[5]==valorNomeAs) );
                          }
                          return atopado;
                          
                          
                      }
                      
                      function EditableControlesBusqueda(estado)
                      {
                          EditableBusquedaAplicacion(estado);
                          EditableBusquedaCampo(estado);
                          EditableBusquedaSubentidade(estado);
                      }
                      
                      function EditableControlesAplicacion(estado)
                      {
                          if (estado) ncomboAplicacion.activate();
                          else ncomboAplicacion.deactivate();
                          HabilitaBotonValidarAplicacion(estado);
                      }
                      
                      function EditableBusquedaAplicacion(estado)
                      {
                          HabilitaBotonMaisAplicacion(estado);
                          HabilitaBotonMenosAplicacion(estado);
                          
                      }
                      
                      
                      function AccionMaisAplicacion()
                      {
                          tabAplicaciones.readOnly=true;
                          EditableControlesAplicacion(true);
                          EditableBusquedaAplicacion(false);
                          // poner foco en comboAplicacion
                          //ncomboAplicacion.base.focus();
                      }
                      
                      
                      function AccionMenosAplicacion()
                      {
                          if (tabAplicaciones.selectedIndex>-1) {
                              // Está seguro
                              tabAplicaciones.removeLinea(tabAplicaciones.selectedIndex);
                          }
                          
                      }
                      
                      function AccionValidarAplicacion()
                      {
                          var linea=new Array();
                          if (Trim(ncomboAplicacion.cod.value)!='') {
                              if (!estaAplicacionEnLista()) {
                                  linea[0]=ncomboAplicacion.cod.value;
                                  linea[1]=ncomboAplicacion.des.value;
                                  linea['APL_COD']=ncomboAplicacion.cod.value;
                                  tabAplicaciones.addLinea(linea);
                              } else jsp_alerta('A','Aplicación ya añadida');
                          }
                          tabAplicaciones.readOnly=false;
                          EditableControlesAplicacion(false);
                          EditableBusquedaAplicacion(true);
                      }
                      
                      function estaAplicacionEnLista()
                      {
                          var atopado=false;
                          var valor=ncomboAplicacion.cod.value;
                          for (var i=0;( (i<tabAplicaciones.lineas.length) && (!atopado) );i++) {
                              atopado=(tabAplicaciones.getLinea(i)['APL_COD']==valor);
                          }
                          return atopado;
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
                          getObjetoPorId('BotonValidarSubentidade').className=cond?"botonGeneralDeshabilitado":"botonGeneral";
                      }
                      
                      function HabilitaBotonCamposJoinSubentidade(est)
                      {
                          var cond=!(est && true );
                          getObjetoPorId('BotonCamposJoinSubentidade').disabled=cond;
                          getObjetoPorId('BotonCamposJoinSubentidade').className=cond?"botonLargoDeshabilitado":"botonLargo";
                      }
                      
                      function HabilitaBotonMaisCampo(est)
                      {
                          var cond=!(est && true );
                          getObjetoPorId('BotonMaisCampo').disabled=cond;
                      }
                      
                      function HabilitaBotonMenosCampo(est)
                      {
                          var cond=!(est && true && (tabCampos.selectedIndex>-1));
                          getObjetoPorId('BotonMenosCampo').disabled=cond;
                      }
                      function HabilitaBotonValidarCampo(est)
                      {
                          var cond=!(est && true );
                          getObjetoPorId('BotonValidarCampo').disabled=cond;
                          getObjetoPorId('BotonValidarCampo').className=cond?"botonGeneralDeshabilitado":"botonGeneral";
                      }
                      
                      function HabilitaBotonMaisAplicacion(est)
                      {
                          var cond=!(est && true );
                          getObjetoPorId('BotonMaisAplicacion').disabled=cond;
                      }
                      
                      function HabilitaBotonMenosAplicacion(est)
                      {
                          var cond=!(est && true && (tabAplicaciones.selectedIndex>-1));
                          //alert('En hbma me pasan:'+est+'.pongo cond='+cond+'.');
                          getObjetoPorId('BotonMenosAplicacion').disabled=cond;
                      }
                      
                      function HabilitaBotonValidarAplicacion(est)
                      {
                          var cond=!(est && true );
                          getObjetoPorId('BotonValidarAplicacion').disabled=cond;
                          getObjetoPorId('BotonValidarAplicacion').className=cond?"botonGeneralDeshabilitado":"botonGeneral";
                          
                      }
                      
                      function EventoCambioTablaAplicaciones()
                      {
                          HabilitaBotonMenosAplicacion((Estado!='CONSULTA'));
                          
                          CubreControlesAplicacion();
                      }
                      
                      function EventoCambioTablaCampos()
                      {
                          HabilitaBotonMenosCampo((Estado!='CONSULTA'));
                          CubreControlesCampo();
                      }
                      
                      function EventoCambioTablaEntidades()
                      {
                          HabilitaBotonMenosSubentidade((Estado!='CONSULTA'));
                          HabilitaBotonCamposJoinSubentidade((tabEntidades.selectedIndex>-1));
                          CubreControlesSubEntidad();
                      }
                      
                      function CubreControlesAplicacion()
                      {
                          if (tabAplicaciones.selectedIndex>-1) {
                              var cod=tabAplicaciones.getLinea(tabAplicaciones.selectedIndex)['APL_COD'];
                              ncomboAplicacion.buscaCodigo(cod);
                          } else ncomboAplicacion.selectItem(-1);
                          
                      }
                      
                      
                      function CubreControlesCampo()
                      {
                          if (tabCampos.selectedIndex>-1) {
                              var linea=tabCampos.getLinea(tabCampos.selectedIndex);
                              var cod=linea['COD_CAMPOINFORME'];
                              textNomeCampo.value=linea[0];
                              textCampoCampo.value=linea[1];
                              ncomboTipoCampo.buscaCodigo(linea[2]);
                              textLonxitudeCampo.value=linea[3];
                              textNomeAsCampo.value=linea[4];
                              
                          } else InicializaControlesCampo();
                          
                      }
                      
                      function InicializaControlesCampo()
                      {	
                          textNomeCampo.value='';
                          textCampoCampo.value='';
                          //textTipoCampo.value='';
                          textLonxitudeCampo.value='';
                          textNomeAsCampo.value='';
                          ncomboTipoCampo.selectItem(-1);
                      }
                      
                      
                      function CubreControlesSubEntidad()
                      {
                          if (tabEntidades.selectedIndex>-1) {
                              var cod=tabEntidades.getLinea(tabEntidades.selectedIndex)['COD_ENTIDADEINFORME'];
                              ncomboEntidadeInforme.buscaCodigo(cod);
                          } else ncomboEntidadeInforme.selectItem(-1);
                      }
                      
                      
                      InicializaPestana();
                      
    </script>
    </html:html>
    
