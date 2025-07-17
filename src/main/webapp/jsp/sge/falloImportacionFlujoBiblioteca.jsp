<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.technical.EstructuraCampoAgrupado"%>
<%@page import="es.altia.agora.business.sge.DefinicionAgrupacionCamposValueObject"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.ArrayList"%>
<%@page import="org.apache.log4j.Logger"%>

<html>
    <head>
        <jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
        <title>::: EXPEDIENTES - PÁGINA DE ERRORES DE IMPORTACIÓN DE FLUJO :::</title>
        <%
            Logger log = Logger.getLogger(this.getClass().getName());
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma=0;
            int apl=0;
            String funcion = "";

            if ((session.getAttribute("usuario") != null)){
              usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
              idioma = usuarioVO.getIdioma();
              apl = usuarioVO.getAppCod();
            }
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
        
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
        <script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-1.9.1.min.js'/>"></script>   
        <script type="text/javascript">
            <%
            int codError = (Integer)request.getAttribute("importacionCodError");
            String descError = (String)request.getAttribute("importacionDescError");
            String codProc = (String)request.getAttribute("codProcedimiento");
            %>
            
        </script>
    </head>
<body class="bandaBody">
    <div class="txttitblanco"><%=descriptor.getDescripcion("gEtiqImpFlujo")%></div>
    <div class="contenidoPantalla">
        <TABLE width="100%">
            <TR>
                <div class="textoSuelto">
                    <p><%= descriptor.getDescripcion("msgGenFalloImpFlujo")%></p>
                    <p><%= descriptor.getDescripcion(descError)%></p>
                </div>
            </TR>
            <logic:notEmpty name="agrupacionesEnConflicto">
                <TR>
                    <TD id="tablaAgrConflicto"></TD>
                </TR>
            </logic:notEmpty>
            <logic:notEmpty name="camposEnConflicto">
                <TR>
                    <TD id="tablaCamposConflicto"></TD>
                </TR>
            </logic:notEmpty>
            <TR>
                <TD align="center" valign="top"><div class="textoSuelto" id="paginacion" STYLE="position:relative; width:100%;"></div></TD>
            </TR>
        </TABLE>
        <DIV id="capaBotonesConsulta" name="capaBotonesConsulta" class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdVolver" onclick="pulsarVolver();return false;">
        </DIV>    
    </DIV>    
    <script type="text/javascript">
            var tabla;
            
            //tabla agrupaciones
            <logic:notEmpty name="agrupacionesEnConflicto">
                var tabAgr = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaAgrConflicto'));
                tabAgr.addColumna('200','center',"<%= descriptor.getDescripcion("gEtiqCodAgr")%>");
                tabAgr.addColumna('500','left',"<%= descriptor.getDescripcion("gEtiqDescAgr")%>");
                tabAgr.readOnly = true;
                tabAgr.displayCabecera=true;


                var listaAgrupaciones = new Array();
                var contador = 0;
                <logic:iterate id="agrupacion" name="agrupacionesEnConflicto">
                    listaAgrupaciones[contador] = [
                        '<bean:write name="agrupacion" property="codAgrupacion"/>',
                        '<bean:write name="agrupacion" property="descAgrupacion"/>'
                    ]
                    contador++;
                </logic:iterate>
    
                tabAgr.lineas = listaAgrupaciones;
                tabAgr.displayTabla();
                
            </logic:notEmpty>
    
            //tabla campos suplementarios
            <logic:notEmpty name="camposEnConflicto">
                var tabCampos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaCamposConflicto'));
                tabCampos.addColumna('200','center',"<%= descriptor.getDescripcion("gEtiqCodCampo")%>");
                tabCampos.addColumna('200','left',"<%= descriptor.getDescripcion("gEtiqRotuloCampo")%>");
                tabCampos.addColumna('500','left',"<%= descriptor.getDescripcion("gEtiqDescCampo")%>");
                tabCampos.readOnly = true;
                tabCampos.displayCabecera=true;


                var listaCamposSupl = new Array();
                var contador2 = 0;
                <logic:iterate id="campoSupl" name="camposEnConflicto">
                    listaCamposSupl[contador2] = [
                        '<bean:write name="campoSupl" property="codCampo"/>',
                        '<bean:write name="campoSupl" property="rotulo"/>',
                        '<bean:write name="campoSupl" property="descCampo"/>'
                    ]
                    contador2++;
                </logic:iterate>
    
                tabCampos.lineas = listaCamposSupl;
                tabCampos.displayTabla();
                
            </logic:notEmpty>
            
            
            function pulsarVolver() {                                 
                var url = "<c:url value='/sge/DefinicionProcedimientos.do'/>?opcion=inicio";
                window.location.href = url;
            }
        </script>
    </body>
</html>
