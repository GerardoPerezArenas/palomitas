<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<%
	int idioma = 1;
	int apl = 1;  
	if (session != null) {
		UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
		if (usuario != null) {
			idioma = usuario.getIdioma();
			apl = usuario.getAppCod();
		}
	}
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl%>" />

<html:html>
	<head>
		<jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
		<TITLE>::: REGISTRO E/S  Importar Entradas:::</TITLE>
			<jsp:include page="/jsp/plantillas/Metas.jsp" />

		<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
		<script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-1.9.1.min.js'/>"></script> 
		<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
                <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
                
                    	
	</head>
	<body class="bandaBody" onload="javascript:{inicializar(); }">
		
		<jsp:include page="/jsp/hidepage.jsp" flush="true">
			<jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
		</jsp:include>
		
		<form id="formImportar">
                    
			<div class="txttitblanco">Importar anotaciones registro</div>
			<div class="contenidoPantalla">
                            
                                <div class="etiqueta"> <%=descriptor.getDescripcion("gEtiqProc")%> </div>
                                
                                <div>
                                   <input type=text style="width:8%;" id="codProcedimientos" class="inputTextoObligatorio"
                                            name="codProcedimientos" size="8" maxlength="5"/>
                                     <input type="text" style="width:65%;" id="descProcedimientos"
                                            class="inputTextoObligatorio" name="descProcedimientos"
                                            readonly/> 
                                     <A href="" id="anchorProcedimientos" name="anchorProcedimientos"> 
                                         <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                                              id="botonProcedimientos" name="botonProcedimientos"
                                              style="cursor:hand;"></span> 
                                     </A> 
                                </div>
				
				<div class="etiqueta" id="mensajesResultadoImportacion"></div>
				
				<div class="botoneraPrincipal">
					<input id="botonImportar" type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbImportar")%> 
						name="botonMigrar" onClick="pulsarImportar();"> 
				</div>
			</div>
		</form>
						
		<script type="text/javascript">
			function inicializar() {

                            pleaseWait('on');
                            try {
                                    $.ajax({
                                            url: '<c:url value="/registro/migracionSolicitudesAyudaLanbide.do"/>',
                                            type: 'POST',
                                            async: true,
                                            data: {'opcion':'cargarProcdimientos'},
                                            success:	cargarComboProcedimientos,
                                            error:	procesarErrorImportar
                                    });
                            } catch (Err) {
                                    //pleaseWait('off');
                                    jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
                            }

                            pleaseWait('off');                         
			}
                        
                        function cargarComboProcedimientos(ajaxResult){
                
                            pleaseWait('off');
                            if (ajaxResult) {
                                var resultado = JSON.parse(ajaxResult);
                                if((resultado.tabla)!=undefined && (resultado.tabla).error!=undefined && (resultado.tabla).error.length>0)
                                    jsp_alerta("A",(resultado.tabla).error)
                                else if(resultado.length>0){
                                    var cod_Proc = new Array();
                                    var desc_Proc = new Array();
                                    for(var iter=0; iter<resultado.length; iter++){
                                        var item = resultado[iter].tabla;
                                        var codigo = item.codigo;
                                        var descripcion = item.descripcion;
                                        cod_Proc[iter] = codigo;
                                        desc_Proc[iter] = descripcion;
                                    } 
                                    comboTipoProcedimiento.addItems(cod_Proc, desc_Proc);
                                }  
                           } else {
                                jsp_alerta("A", "<%=descriptor.getDescripcion("msgErrGenServ")%>");
                            }
                        }
                        
                        var comboTipoProcedimiento = new Combo("Procedimientos");
                        
                        comboTipoProcedimiento.change = function() {
                            if(comboTipoProcedimiento.selectedIndex!==-1){
                                codProcedimientos = $('input[name=codProcedimientos]').val();
                            } else {
                                codProcedimientos = "";
                            }
                            
                        }
			
			function pulsarImportar() {
                            //pleaseWait('on');
                                
                            if(comboTipoProcedimiento.selectedIndex!==-1){
                                
                                codProcedimientos = $('input[name=codProcedimientos]').val();

                                try {
                                    $.ajax({
                                            url		:	'<c:url value="/registro/migracionSolicitudesAyudaLanbide.do"/>',
                                            type	:	'POST',
                                            async	:	true,
                                            data	:	{'codProc':codProcedimientos, 'opcion': 'importarSolicitudes'},
                                            succes	:	procesarRespuestaImportar,
                                            error	:	procesarErrorImportar
                                    });
                                } catch (Err) {
                                        //pleaseWait('off');
                                        jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
                                }
                            } else {
                                jsp_alerta("A", "<%=descriptor.getDescripcion("codDescProcVacio")%>");
                            }
				
			}
			
			function procesarRespuestaImportar(ajaxResult){
                            //pleaseWait('off');
                            alert("success");
			}
			
			function procesarErrorImportar(){
                            //pleaseWait('off');
                            jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
			}
		</script>
				
	</body>
</html:html>