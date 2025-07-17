<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.agora.technical.Fecha" %>
<%@ page import="java.util.Date" %>
<%@ page import="es.altia.common.service.config.Config" %>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@ page import="java.util.Vector" %>

<HTML>
    <head>
        <%            
            String tipo = "E"; // Sólo se trabaja con anotaciones de entrada
            char t = tipo.charAt(0);
            int idioma = 1;
            String[] params = null;
            UsuarioValueObject usuario = null;
            String css = "";
            int codOrganizacion = 0;
            if (session != null) {
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    css = usuario.getCss();
                    params = usuario.getParamsCon();
                    codOrganizacion = usuario.getOrgCod();
                }
            }
            
            // Fecha actual del servidor.
            Fecha f = new Fecha();
            Date dateServidor = new Date();
            String fechaServidor = f.obtenerString(dateServidor);
            String idSesion = session.getId();

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
            // Carga de la lista de idiomas
            
            String userAgent = request.getHeader("user-agent");

            /** Como código de aplicación se utiliza el 1 que es el de registro para las etiquetas, puesto que son las mismas y así no hay que dar de alta unas nuevas para este módulo **/
        %>

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript">
         
          var codigoError  = '<%=(String) session.getAttribute("CODIGO_ERROR_LISTADO_LANBIDE") %>';
       
    </script>

        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
                     type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
        <jsp:setProperty name="descriptor" property="idi_cod" value="<%=idioma%>"/>
        <jsp:setProperty name="descriptor" property="apl_cod" value="1"/>
        <TITLE>::: REGISTRO DE ENTRADA - Consulta del libro de registro :::</TITLE>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%><%=css%>" type="text/css">
        <!-- Ficheros javascript -->
        <script src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script src="<%=request.getContextPath()%>/scripts/calendario.js"></script>

        
        <script type="text/javascript">
            function pulsarSalir() {
                var resultado = jsp_alerta("C",'<%= descriptor.getDescripcion("g_confirmSalirApp")%>');
                if (resultado == 1){
                    document.forms[0].target = '_top';
                    document.forms[0].action = '<%=request.getContextPath()%>/SalirApp.do?app=<%= usuario.getAppCod()%>';
                    document.forms[0].submit();
                }
            }      
            
            
            function redirigir(codUor,codOrganizacion){
                if(codUor!="" && codOrganizacion!=""){                    
                    document.forms[0].target = "mainFrame";                    
                    document.forms[0].action = "<%=request.getContextPath()%>/ListadoEntradasOficinaRegistro.do?opcion=seleccionUor&codUor=" + codUor + "&codOrganizacion=" + codOrganizacion;
                    document.forms[0].submit();
                }
            }
            
        </script>    
        
    </head>

    <body class="bandaBody" onload="javascript:{ pleaseWait('off'); 
       }">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>


        <FORM name="formulario" METHOD=POST target="_self">
            
            <input type="hidden" name="tipoListado" value="<%=(String)session.getAttribute("TIPO_LISTADO")%>"/>
            
        <%if ("salida".equals((String)session.getAttribute("TIPO_LISTADO"))) {%>
            <div id="titulo" class="txttitblancoder"><%=descriptor.getDescripcion("titListadoOfiRegistroSalida")%></div>
        <%} else {%>
            <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titListadoOfiRegistro")%></div>
        <%}%>
            <div class="contenidoPantalla">
                <TABLE  cellspacing="0px"class="cuadroFondoBlanco">
                    <tr>
                        <td style="width:100%;height:23px" colspan="7" class="sub3titulo">                                                            
                            <%=descriptor.getDescripcion("etiqSeleccionUnidadRegistro")%>
                        </td>
                    </tr>
                    <TR>
                        <TD>
                            <TABLE id="tabla1" width="100%" cellpadding="2" cellspacing="2" align="center" border="0">
                                <TR>
                                    <TD>
                                        <TABLE width="100%" cellpadding="1" cellspacing="2" border="0">


                                            <c:forEach var="unidad" items="${sessionScope.unidades_tipo_registro}">                                                                                
                                            <TR>
                                                <td style="width: 50%" class="etiqueta">

                                                    <a href="#" onclick='redirigir("<c:out value="${unidad.uor_cod}"/>","<%=codOrganizacion%>")'>
                                                        <c:out value="${unidad.uor_nom}"/>
                                                    </a>
                                                </td>

                                            </TR>
                                            </c:forEach>

                                        </TABLE>
                                    </TD>

                                </TR>
                            </TABLE>
                        </TD>
                    </tr>
                </TABLE>
            <div class="botoneraPrincipal">
                 <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir"  onClick="pulsarSalir();"
                 alt="<%=descriptor.getDescripcion("toolTip_bVolver")%>" title="<%=descriptor.getDescripcion("toolTip_bVolver")%>">
            </div>
        </div>
   </FORM>
    </BODY>
</HTML>
