<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.sge.DefinicionProcedimientosValueObject"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.ArrayList"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<html>
    <head>
        <jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
        <title>::: EXPEDIENTES - LISTADO LIBRERIAS DE FLUJO :::</title>
        <%
            Log log = LogFactory.getLog(this.getClass().getName());
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
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
        <script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
        <script type="text/javascript">
            <%
            ArrayList librerias = (ArrayList)request.getAttribute("RelacionBibliotecas");
            int numRelacionProcedimientos = 0;
            if ( librerias != null ) numRelacionProcedimientos = librerias.size();
            %>
            
            function pulsarSalirBiblioteca(){
                var codProcedimiento = $('input[name="codProcDest"]').val();
                //var url = "<c:url value='/sge/DefinicionProcedimientos.do'/>?opcion=recargaConsulta&importar=no&codProcedimiento=" +codProcedimiento + "&codMunicipio=0&txtCodigo=" + codProcedimiento;
                var url = "<c:url value='/sge/DefinicionProcedimientos.do'/>?opcion=inicio";
                window.location.href = url;
            }
            
            function pulsarImportar(){
                if(tab.selectedIndex != -1){
                    var codProcDest = $('input[name="codProcDest"]').val();
                    var codBiblioteca = listado[tab.selectedIndex][0];
                    var url = "<c:url value='/procedimiento/bibliotecaFlujoTramitacion.do'/>?opcion=importarFlujoBiblioteca&codProcDest="+codProcDest+"&codBiblioteca="+codBiblioteca;
                    
                    window.location.href = url;
                } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
            
            <%--
            function pulsarVerBiblioteca(){
                if(tab.selectedIndex != -1){
                    var filaSel = tab.selectedIndex;
                    
                    mostrarDefinicion(filaSel);
                } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
            --%>
        </script>
    </head>
    <body class="bandaBody" onload="javascript:{
              
            }">
        <input type="hidden" name="codProcDest" value="<%=request.getAttribute("codProcDestino")%>" />
        <input type="hidden" name="codBiblioteca" value="" />
        <div class="txttitblanco"><%=descriptor.getDescripcion("gEtiqImpFlujo")%></div>
        <div class="contenidoPantalla" valign="top" style="padding-top: 5px; padding-bottom: 10px">
            <TABLE width="100%">
                <TR>
                    <TD id="tablaLibrerias"></TD>
                </TR>
                <TR>
                    <TD align="center" valign="top"><div class="textoSuelto" id="paginacion" STYLE="position:relative; width:100%;"></div></TD>
                </TR>
            </TABLE>
            <DIV id="capaBotonesConsulta" name="capaBotonesConsulta" class="botoneraPrincipal">
                <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("btnBiblImportar")%>" name="cmdImportarBiblioteca" onclick="pulsarImportar();return false;">
                <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdSalirBiblioteca" onclick="pulsarSalirBiblioteca();return false;">
            </DIV>    
        </div>    
        <script type="text/javascript">
            var tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaLibrerias'));
            tab.addColumna('200','center',"<%=descriptor.getDescripcion("etiqCodProc")%>");
            tab.addColumna('700','left',"<%=descriptor.getDescripcion("etiqDescProc")%>");
            tab.displayCabecera=true;
            
            var listado = new Array();
            var contador = 0;
             <logic:iterate id="procedimiento" name="RelacionBibliotecas">
                listado[contador] = ['<bean:write name="procedimiento" property="txtCodigo"/>','<bean:write name="procedimiento" property="txtDescripcion"/>']
                contador++;
            </logic:iterate> 
    
            tab.lineas = listado;
            tab.displayTabla();
            domlay('paginacion',1,0,0,listado.length+' resultados encontrados');
    
            
            function refresh(){
              tab.displayTabla();
            }
            
            <%--
            function callFromTableTo(rowID,tableName){
              if(tab.id == tableName){
                fila=parseInt(rowID);
                $('input[name=codBiblioteca]').val(listado[fila][0]);
                //$('input[name=descBiblioteca]').val(listado[fila][1]);
                mostrarDefinicion(fila);
              }
            }
            
            function mostrarDefinicion(indice) {
                alert("mostrarDefinicion");
                return false;
                var codProcDest = $('input[name="codProcDest"]').val();
                var codProcedimiento = $("input[name=codBiblioteca]").val();
                alert('procDest: '+codProcDest+' - procBib: '+codProcedimiento);
                var url = "<c:url value='/sge/DefinicionProcedimientos.do'/>?opcion=recargaConsulta&importar=no&codProcedimiento=" +codProcedimiento + "&codMunicipio=0&txtCodigo=" + codProcedimiento + "&biblioteca=SI";
                alert(url);
                window.location.href = url;
             }
            --%>
        </script>
    </body>
</html>
