<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="java.util.StringTokenizer"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<html>
    <head>
        <TITLE>::: LISTA VIAS BUSCADAS:::</TITLE>
        <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />


        <%
            int idioma = 1;
            int apl = 1;
            UsuarioValueObject usuario = new UsuarioValueObject();
            if (session != null) {
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    apl = usuario.getAppCod();
                }
            }
            Config m_Conf = ConfigServiceHelper.getConfig("common");
            boolean bAltaViaDirecta = false;
            String JSP_altaViaDirecta = m_Conf.getString("JSP.altaViaDirecta");
            StringTokenizer st = new StringTokenizer(JSP_altaViaDirecta, ",");
            while (st.hasMoreTokens() && usuario != null) {
                if (st.nextToken().trim().equals(String.valueOf(usuario.getOrgCod()))) {
                    bAltaViaDirecta = true;
                }
            }
            int iAnchoESI = 145;
            int iAnchoTabla = 920;
            int iAnchoNumeracion = 68;
            String JSP_entidadSingular = m_Conf.getString("JSP.EntidadSingular");
            if ("no".equals(JSP_entidadSingular)) {
                iAnchoESI = 0;
                iAnchoTabla = 768;
            }
            String JSP_numeracion = m_Conf.getString("JSP.Numeracion");
            if ("no".equals(JSP_numeracion)) {
                iAnchoTabla -= 73;
                iAnchoNumeracion = 0;
            }
            int iAnchoProvincia = 114;
            int iAnchoMunicipio = 114;

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

        <%
            String ver = request.getParameter("opcion");
            String[] aux = ver.split(",");
            String verProvincia = "";
            String verMunicipio = "";
            if (aux.length == 2) {
                verProvincia = aux[0];
                verMunicipio = aux[1];
            }
            if (verProvincia.equals("1")) {
                iAnchoTabla -= 120;
                iAnchoProvincia = 0;
            }
            if (verMunicipio.equals("1")) {
                iAnchoTabla -= 120;
                iAnchoMunicipio = 0;
            }
        %>

        <SCRIPT type="text/javascript">

            var listaViasOriginal = new Array();
            var listaVias = new Array();

            function inicializar() {
                window.focus();
                var i = 0;
                <c:forEach items="${sessionScope.MantenimientosTercerosForm.listaViasBuscadas}" var="via" >
                    var codigosECOESI;
                    var numeracion;
                    <c:choose>
                        <c:when test="${empty via.infoTramero}">
                            codigosECOESI = new Array();
                            numeracion = "";
                        </c:when>
                        <c:otherwise>
                            codigosECOESI = ["<c:out value="${via.infoTramero.codigoECO}"/>",
                                "<c:out value="${via.infoTramero.descECO}"/>",
                                "<c:out value="${via.infoTramero.codigoESI}"/>",
                                "<c:out value="${via.infoTramero.descESI}"/>",
                                "<c:out value="${via.infoTramero.codigoNUC}"/>",
                                "<c:out value="${via.infoTramero.descNUC}"/>"];
                             <c:if test="${via.infoTramero.primerNumero != 0}">
                                numeracion += "<c:out value="${via.infoTramero.primerNumero}"/> ";
                            </c:if>
                            <c:if test="${not empty via.infoTramero.primeraLetra}">
                                numeracion += "<c:out value="${via.infoTramero.primeraLetra}"/> ";
                            </c:if>
                            <c:if test="${via.infoTramero.ultimoNumero != 0}">
                                numeracion += "<c:out value="${via.infoTramero.ultimoNumero}"/> ";
                            </c:if>
                            <c:if test="${not empty via.infoTramero.ultimaLetra}">
                                numeracion += "<c:out value="${via.infoTramero.ultimaLetra}"/> ";
                            </c:if>

                        </c:otherwise>

                    </c:choose>

                    var descripcionESI = "";
                    if (codigosECOESI != "") descripcionESI = codigosECOESI[3];

                    listaViasOriginal[i] = ["<c:out value="${via.codigoProvincia}"/>", "<c:out value="${via.descProvincia}"/>",
                        "<c:out value="${via.codigoMunicipio}"/>", "<c:out value="${via.descMunicipio}"/>",
                        "<c:out value="${via.codigoVia}"/>", "<c:out value="${via.codigoVia}"/>",
                        "<c:out value="${via.nombreVia}"/>", codigosECOESI, "<c:out value="${via.descTipoVia}"/>",
                        "<c:out value="${via.codigoTipoVia}"/>"];
                        listaVias[i] = ["<c:out value="${via.codigoProvincia}"/>", "<c:out value="${via.descProvincia}"/>",
                            "<c:out value="${via.codigoMunicipio}"/>", "<c:out value="${via.descMunicipio}"/>",
                            "<c:out value="${via.codigoVia}"/>", "<c:out value="${via.descTipoVia}"/>",
                            "<c:out value="${via.nombreVia}"/>", descripcionESI, numeracion];
                            i++;
                </c:forEach>


                tab.lineas=listaVias;
                refresca();


             }

             function pulsarAceptar(i) {
                            i = tab.selectedIndex;
                            if (i >= 0){
                                seleccionarVia(i);
                            }
                        }
                        function pulsarCancelar() {
                            self.parent.opener.retornoXanelaAuxiliar();
                        }
                        function callFromTableTo(rowID, tableName) {
                            if(tab.id == tableName){
                                seleccionarVia(rowID);
                            }
                        }
                        function iniciarAltaViaDirecta() {
                            var retorno = new Array();
                            retorno[0] = "altaViaDirecta";
                            self.parent.opener.retornoXanelaAuxiliar(retorno);
                        }
                        function seleccionarVia(i) {
                            var retorno = new Array();
                            retorno[0] = listaViasOriginal[i][4]; // codigo de la via (oculto en pantalla)
                            retorno[1] = listaViasOriginal[i][5]; // codigo de la via (visible en la pantalla) -->
                            retorno[2] = listaViasOriginal[i][6]; // descripcion de la via -->
                            retorno[3] = listaViasOriginal[i][8]; // descripcion del tipo de via -->
                            var masDatos = new Array();
                            masDatos = listaViasOriginal[i][7];
                            retorno[4] = masDatos[0]; // codigo de entidad colectiva -->
                            retorno[5] = masDatos[2]; // codigo de entidad singular -->
                            retorno[6] = listaViasOriginal[i][9]; // codigo del tipo de via -->
                            retorno[7] = listaViasOriginal[i][0]; // codigo de la provincia (oculto en pantalla) -->
                            retorno[8] = listaViasOriginal[i][1]; // descripcion de la provincia (oculto en pantalla) -->
                            retorno[9] = listaViasOriginal[i][2]; // codigo del municipio (oculto en pantalla) -->
                            retorno[10] = listaViasOriginal[i][3]; // descripcion del municipio (oculto en pantalla) -->

                            self.parent.opener.retornoXanelaAuxiliar(retorno);
                        }
</SCRIPT>
    </head>
    <BODY class="bandaBody" onload="{inicializar();}">
        <form target="_self">
            <input type="Hidden" name="opcion" value="">
            <input type="Hidden" name="numeroCasa" value="">
    <div class="txttitblanco"><%=descriptor.getDescripcion("tit_listVias")%></div>
    <div class="contenidoPantalla">
        <div id="tabla"></div>
        <div class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar" onclick="pulsarAceptar();" accesskey="A">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelar" onclick="pulsarCancelar();" accesskey="C">
        </div>
    </div>
</form>
<script type="text/javascript">
            var tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla', <%=iAnchoTabla - 2%>));

            tab.addColumna('0', 'center', ''); //cod Provincia
            tab.addColumna('<%=iAnchoProvincia%>','center','<%= escape(descriptor.getDescripcion("gEtiq_Provincia"))%>');
            tab.addColumna('0', 'center', ''); //cod Municipio
            tab.addColumna('<%=iAnchoMunicipio%>','center','<%= escape(descriptor.getDescripcion("gEtiq_Municipio"))%>');
            tab.addColumna('0','center','<%= escape(descriptor.getDescripcion("gEtiq_codigo"))%>');
            tab.addColumna('145','center','<%= escape(descriptor.getDescripcion("etiqTipVia"))%>');
            tab.addColumna('295','center','<%= escape(descriptor.getDescripcion("gEtiq_Nombre"))%>');
            tab.addColumna('<%=iAnchoESI%>','center','<%= escape(descriptor.getDescripcion("gEtiq_ESI"))%>');
            tab.addColumna('<%=iAnchoNumeracion%>','center','<%= escape(descriptor.getDescripcion("gEtiq_TipNum"))%>');
            tab.displayCabecera=true;
            tab.displayTabla();

            function refresca() {
                tab.displayTabla();
            }

                                                function checkKeysLocal(evento,tecla) {
                                                      var teclaAuxiliar;
                                                    if(window.event){
                                                        evento = window.event;
                                                        teclaAuxiliar = evento.keyCode;
                                                    }else
                                                        teclaAuxiliar = evento.which;

                                                    keyDel(evento);
                                                    if ( (teclaAuxiliar == 40) || (teclaAuxiliar == 38)){
                                                        upDownTable(tab,listaVias,teclaAuxiliar);
                                                    }
                                                }

        </script>


    </body>

</html>
