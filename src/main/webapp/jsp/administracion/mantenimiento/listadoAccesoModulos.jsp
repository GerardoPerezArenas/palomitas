<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Accesos a módulos de Flexia</title>

        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 5;
            String css = "";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                css = usuarioVO.getCss();

            }%>

        <!-- Estilos -->
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" href="<c:url value='/login/css/color_admin/assets/plugins/bootstrap/css/bootstrap.min.css'/>">
        <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap-override.css">
        <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">

        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/administracion/mantenimiento/listadoAccesoModulos.js"></script>
        <script type="text/javascript">
            var opcionesViewListaAccesoModulos = {
                textos: {buscar: '<%=descriptor.getDescripcion("buscar")%>'
                    , anterior: '<%=descriptor.getDescripcion("anterior")%>'
                    , siguiente: '<%=descriptor.getDescripcion("siguiente")%>'
                    , mosFilasPag: '<%=descriptor.getDescripcion("mosFilasPag")%>'
                    , msgNoResultBusq: '<%=descriptor.getDescripcion("msgNoResultBusq")%>'
                    , mosPagDePags: '<%=descriptor.getDescripcion("mosPagDePags")%>'
                    , noRegDisp: '<%=descriptor.getDescripcion("noRegDisp")%>'
                    , filtrDeTotal: '<%=descriptor.getDescripcion("filtrDeTotal")%>'
                    , primero: '<%=descriptor.getDescripcion("primero")%>'
                    , ultimo: '<%=descriptor.getDescripcion("ultimo")%>'
                    , tblAccesoColMovimiento: '<%= descriptor.getDescripcion("gEtiq_Movimiento")%>'
                    , tblAccesoColOrganizacion: '<%= descriptor.getDescripcion("gEtiq_Organizacion")%>'
                    , tblAccesoColLogin: '<%= descriptor.getDescripcion("gEtiq_Login")%>'
                    , tblAccesoColUsuario: '<%= descriptor.getDescripcion("gEtiq_Usuario")%>'
                    , tblAccesoColFechaHora: '<%= descriptor.getDescripcion("gEtiq_FechaHora")%>'
                    , tblAccesoA: '<%= descriptor.getDescripcion("gEtiq_AccesoA")%>'
                    , errCargarAccesosModulos: '<%=descriptor.getDescripcion("errorCargarAccesosModulos")%>'
                    , msjObligTodos : '<%=descriptor.getDescripcion("msjObligTodos")%>'
                    , msjCriterioFechaInicioFin : '<%=descriptor.getDescripcion("msjCriterioFechaInicioFin")%>'
                },
                listaOrganizaciones : <%=request.getAttribute("ListaOrganizaciones")%>,
                listaAplicaciones : <%=request.getAttribute("ListaAplicaciones")%>
            }
            
        </script>
    </head>

    <body class="bandaBody" onload="javascript:{
                viewListaAccesoModulos = new ViewListaAccesoModulos()
                viewListaAccesoModulos.inicializar(opcionesViewListaAccesoModulos);
                checkKeysLocal = viewListaAccesoModulos.checkKeysLocal;
            }">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        <form name="formulario" method="post">
            <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiq_InfoAccesoModulo")%></div>
            <div class="contenidoPantalla">
                <div class="contenidoBusqueda">
                    <div class="contenidoBusquedaFila row">
                        <div class="camposFormulario col-sm-12">
                            <label class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Organizacion")%></label>
                            <input id="codOrganizacion" name="codOrganizacion" type="text" class="inputTexto" size="3" />
                            <input name="descOrganizacion" type="text" class="inputTexto" size="50" readonly />
                            <a id="anchorOrganizacion" name="anchorOrganizacion" href="" onfocus="javascript:this.focus();">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonOrganizacion" name="botonOrganizacion" style="cursor:hand;"></span>
                            </a>
                        </div>
                        <div class="camposFormulario col-sm-12">
                            <label class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Aplicacion")%></label>
                            <input id="codAplicacion" name="codAplicacion" type="text" class="inputTexto" size="3" />
                            <input name="descAplicacion" type="text" class="inputTexto" size="50" readonly />
                            <a id="anchorAplicacion" name="anchorAplicacion" href="" onfocus="javascript:this.focus();">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:hand;"></span>
                            </a>
                        </div>
                    </div>
                    <div class="contenidoBusquedaFila row">
                        <div class="camposFormulario col-sm-12">
                            <label class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Login")%></label> 
                            <input type="text" name="loginUsuario" id="loginUsuario" maxlength="255" size="50" class="inputTexto" />
                        </div>
                        <div class="camposFormulario col-sm-12">
                            <label class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Usuario")%></label>
                            <input type="text" name="nombreUsuario" id="nombreUsuario" maxlength="40" size="50" class="inputTexto" />
                        </div>
                    </div>
                    <div class="contenidoBusquedaFila row">
                        <div class="camposFormulario comboCriteriosFecha col-sm-12">
                            <label class="etiqueta"><%=descriptor.getDescripcion("gEtiqFecha")%></label> 
                            <select name="valorOperCriterioFecha" id="valorOperCriterioFecha" class="inputTexto" onchange="javascript:viewListaAccesoModulos.cambiarVisibilidadInputFechas();">
                                <option value="-1"></option>
                                <option value="0"> = </option>
                                <option value="1"> > </option>
                                <option value="2"> >= </option>
                                <option value="3"> &lt </option>
                                <option value="4"> &lt= </option>
                                <option value="5"> ENTRE </option>
                            </select>
                            <div id="primerCriterioFecha" class="inputFechaCalendario hideVisibility">
                                <input type="text" class="inputTxtFechaObligatorio" size="12" maxlength="10" name="fechaInicio"
                                       id="fechaInicio" onkeyup = "javascript: return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFecha(this);"
                                       onfocus = "this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="return viewListaAccesoModulos.mostrarCalFechaInicio(event);" onblur="ocultarCalendarioOnBlur(event); return false;">
                                    <span class="fa fa-calendar" aria-hidden="true"  id="calFechaInicio" name="calFechaInicio" alt='<%=descriptor.getDescripcion("gEtiqFecha")%>'></span>
                                </A>
                            </div>
                            <div id="segundoCriterioFecha" class="inputFechaCalendario hideVisibility">
                                <input type="text" class="inputTxtFechaObligatorio" size="12" maxlength="10" name="fechaFin"
                                       id="fechaFin" onkeyup = "javascript: return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFecha(this);"
                                       onfocus = "this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="return viewListaAccesoModulos.mostrarCalFechaFin(event);" onblur="ocultarCalendarioOnBlur(event); return false;">
                                    <span class="fa fa-calendar" aria-hidden="true"  id="calFechaFin" name="calFechaFin" alt='<%=descriptor.getDescripcion("gEtiqFecha")%>'></span>
                                </A>
                            </div>
                        </div>
                    </div>
                    <div class="contenidoBusquedaBotonera row">
                        <div class="botoneraBusqueda col-sm-12">
                            <input name="botonBuscar" type="button"  class="botonGeneral" id="botonBuscar" value="<%=descriptor.getDescripcion("gbBuscar")%>" onClick="viewListaAccesoModulos.pulsarBuscar();" accesskey="B" />
                            <input name="botonLimpiarBuscar" type="button"  class="botonGeneral" id="botonLimpiarBuscar" value="<%=descriptor.getDescripcion("gbLimpiar")%>" onClick="viewListaAccesoModulos.pulsarLimpiarBuscar();" accesskey="L" />
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div id="tabla" class="col-sm-12"></div>
                </div>
                <div class="row">
                    <div class="botoneraPrincipal col-sm-12">
                        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%>
                               name="cmdSalir" onClick="viewListaAccesoModulos.pulsarSalir();" accesskey="S" /> 
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>
