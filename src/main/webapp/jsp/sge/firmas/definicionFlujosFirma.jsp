<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
    <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Definición de flujos de firma</title>

        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 1;
            String css = "";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                css = usuarioVO.getCss();
            }
        %>

        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        
        <!-- Estilos -->
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>?version=<c:out value="${applicationScope.appVersion}"/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<c:url value='/scripts/DataTables/datatables.min.css'/>?version=<c:out value="${applicationScope.appVersion}"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value='/login/css/color_admin/assets/plugins/bootstrap/css/bootstrap.min.css'/>?version=<c:out value="${applicationScope.appVersion}"/>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap-override.css'/>?version=<c:out value="${applicationScope.appVersion}"/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>?version=<c:out value="${applicationScope.appVersion}"/>" media="screen">

        <!-- JS Scripts -->
        <script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/jquery/jquery-1.9.1.min.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/DataTables/datatables.min.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/listas.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/listaComboBox.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/TablaNueva.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/sge/firmas/definicionFlujosFirma.js'/>?version=<c:out value="${applicationScope.appVersion}"/>"></script>

        <script type="text/javascript">
            // Convertir listado de tipos de firma a javascript
            var index = 0;
            var datosTipoFirma = { codigo: [], descripcion: [] };
            <c:forEach items="${requestScope.listaFirmaTipo}" var="firmaTipo"> 
                    datosTipoFirma.codigo[index] = '<c:out value="${firmaTipo.id}" />';
                    datosTipoFirma.descripcion[index] = '<c:out value="${firmaTipo.nombre}" />';
                    index++;
            </c:forEach>
            
            // Convertir listado de flujos de firma a javascript
            index = 0;
            var flujoActivo;
            var datosFlujosFirma = [];
            <c:forEach items="${requestScope.listaFirmaFlujo}" var="firmaFlujo"> 
                    <c:choose>
                        <c:when test="${firmaFlujo.activo == true}"> flujoActivo = 'SI'; </c:when>
                        <c:otherwise> flujoActivo = 'NO'; </c:otherwise>
                    </c:choose>
                    
                    datosFlujosFirma[index] = [
                        '<c:out value="${firmaFlujo.id}" />',
                        '<c:out value="${firmaFlujo.nombre}" />',
                        '<c:out value="${firmaFlujo.tipoFirma}" />',
                        flujoActivo,
                    ];
                    index++;
            </c:forEach>
            
            var opcionesViewDefinicionFlujosFirma = {
                datosTabFlujosFirma: datosFlujosFirma,
                datosComboTipoFirma: datosTipoFirma,
                textos: {
                      buscar: '<%=descriptor.getDescripcion("buscar")%>'
                    , anterior: '<%=descriptor.getDescripcion("anterior")%>'
                    , siguiente: '<%=descriptor.getDescripcion("siguiente")%>'
                    , mosFilasPag: '<%=descriptor.getDescripcion("mosFilasPag")%>'
                    , msgNoResultBusq: '<%=descriptor.getDescripcion("msgNoResultBusq")%>'
                    , mosPagDePags: '<%=descriptor.getDescripcion("mosPagDePags")%>'
                    , noRegDisp: '<%=descriptor.getDescripcion("noRegDisp")%>'
                    , filtrDeTotal: '<%=descriptor.getDescripcion("filtrDeTotal")%>'
                    , primero: '<%=descriptor.getDescripcion("primero")%>'
                    , ultimo: '<%=descriptor.getDescripcion("ultimo")%>'
                    , eqTblFlujosFirmaFlujo: '<%=descriptor.getDescripcion("etiqFlujo")%>'
                    , eqTblFlujosFirmaTipo: '<%=descriptor.getDescripcion("etiqTipo")%>'
                    , eqTblFlujosFirmaActivo: '<%=descriptor.getDescripcion("etiqActivo")%>'
                    , msjNoSelecFila: '<%=descriptor.getDescripcion("msjNoSelecFila")%>'
                    , preguntaEliminarFlujoFirma: '<%=descriptor.getDescripcion("preguntaEliminarFlujoFirma")%>'
                    , msjErrorInternoAplicacion: '<%=descriptor.getDescripcion("msjErrorInterno")%>'
                    , msjDatosObligatorios: '<%=descriptor.getDescripcion("msjObligTodos")%>'
                    , msjExisteNombreFlujoFirma: '<%=descriptor.getDescripcion("msjExisteNombreFlujoFirma")%>'
                }
            }
        </script>
    </head>

    <body class="bandaBody" onload="javascript:{
                viewDefinicionFlujosFirma = new ViewDefinicionFlujosFirma();
                callFromTableTo = viewDefinicionFlujosFirma.callFromTableToFlujosFirma;
                viewDefinicionFlujosFirma.inicializar(opcionesViewDefinicionFlujosFirma);
                checkKeysLocal = viewDefinicionFlujosFirma.checkKeysLocal;
            }">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        <form name="formDefinicionFlujosFirma" method="post">
            <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiqMantFlujoFirmas")%></div>
            <div class="contenidoPantalla">
                <div class="contenidoInformacion">
                    <div class="contenidoInformacionFila row">
                        <div id="tablaFlujosFirma" class="col-sm-12"></div>
                    </div>
                    <div class="contenidoInformacionFila row">
                        <div class="camposFormulario-inputUnico-full col-sm-6">
                            <input id="nombreFlujoFirma" name="nombreFlujoFirma" type="text" class="inputTextoObligatorio" />
                        </div>
                        <div class="camposFormulario-inputUnico col-sm-6">
                            <input id="codTipoFlujoFirma" name="codTipoFlujoFirma" type="hidden" class="inputTextoObligatorio" />
                            <input name="descTipoFlujoFirma" type="text" class="inputTextoObligatorio" readonly />
                            <a id="anchorTipoFlujoFirma" name="anchorTipoFlujoFirma" href="" onfocus="javascript:this.focus();">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoFlujoFirma" name="botonTipoFlujoFirma" style="cursor:hand;"></span>
                            </a>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="botoneraPrincipal col-sm-12">
                        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAlta")%>"
                               id="cmdAltaFlujoFirma" name="cmdAltaFlujoFirma" onClick="viewDefinicionFlujosFirma.pulsarAltaFlujoFirma();" accesskey="A" />
                        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>"
                               id="cmdEliminarFlujoFirma" name="cmdEliminarFlujoFirma" onClick="viewDefinicionFlujosFirma.pulsarEliminarFlujoFirma();" accesskey="E" />
                        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbDefinirFlujo")%>"
                               id="cmdDefinirFlujoFirma" name="cmdDefinirFlujoFirma" onClick="viewDefinicionFlujosFirma.pulsarDefinirFlujoFirma();" accesskey="D" />
                        <input type= "button" class="botonLargo" value="<%=descriptor.getDescripcion("gbActivarDesactivar")%>"
                               id="cmdActivarDesactivarFlujoFirma" name="cmdActivarDesactivarFlujoFirma" onClick="viewDefinicionFlujosFirma.pulsarActivarDesactivarFlujoFirma();" accesskey="C" />
                        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbLimpiar")%>"
                               id="cmdLimpiarFlujoFirma" name="cmdLimpiarFlujoFirma" onClick="viewDefinicionFlujosFirma.pulsarLimpiarFlujoFirma();" accesskey="L" />                        
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>
