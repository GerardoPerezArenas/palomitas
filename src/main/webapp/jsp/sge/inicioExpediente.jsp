<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

<TITLE>Inicio de Expediente</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<%
	UsuarioValueObject usuarioVO = new UsuarioValueObject(); 
  	int idioma=2;
  	int apl=4;

  	if(session.getAttribute("usuario") != null){
    	usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");    
    	idioma = usuarioVO.getIdioma();
    	apl = usuarioVO.getAppCod();
  	}
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
<script type="text/javascript" src="<c:url value='/scripts/listaBusquedaTerceros.js'/>"></script>
<script language="JavaScript1.2">
	var lista = new Array();
	var lista_ter = new Array();
	var ultimo = false;
	
	var codTipoDocumentoOld;        	
	var desTipoDocumentoOld;			
	var documentoOld;			
	var nombreOld;			
	var telefonoOld;			
	var emailOld;
		
	var provinciaOld;			
	var municipioOld;			
	var domicilioOld;			
	var poblacionOld;			
	var codigoPostalOld;
	
	var codProcedimientoOld;
	var descProcedimientoOld;
	
	var terceroOld;			
	var versionOld;
		
	var codProcedimientos;
	var desProcedimientos;
	
	var ini = true;
		
	function cargarInicio(){	   
		cargaValores();
		cargaProcedimientos();			
	}
	
	function cargaProcedimientos(){
		var cont = 0;
		codProcedimientos = new Array();
		desProcedimientos = new Array();
		<logic:iterate id="elemento" name="InicioExpedienteForm" property="procedimientos">		
			codProcedimientos[cont]	= ['<bean:write name="elemento" property="codProcedimiento"/>'];
			desProcedimientos[cont]	= ['<bean:write name="elemento" property="descProcedimiento"/>'];	        	
          	cont = cont + 1;
      	</logic:iterate>  	
	}
	
	function cargaValores(){
		document.forms[0].codProcedimientoExp.value = '<bean:write name="InicioExpedienteForm" property="codProcedimiento" />';
		codProcedimientoOld = '<bean:write name="InicioExpedienteForm" property="codProcedimiento" />';	
		descProcedimientoOld = '<bean:write name="InicioExpedienteForm" property="descProcedimiento" />';				
		
		if('' == descProcedimientoOld){		
			ini = false;			
			document.all.capaBotones.style.visibility = "hidden";
			document.all.codProcedimiento.className = "inputTextoObligatorio";
			document.all.descProcedimiento.className = "inputTextoObligatorio";	
			document.all.capaBotones2.style.visibility = "visible";			
		}else{	
			ini = true;									
			document.all.capaBotones.style.visibility = "visible";
			document.all.codProcedimiento.className = "inputTextoDeshabilitado";
			document.all.descProcedimiento.className = "inputTextoDeshabilitado";
			document.all.capaBotones2.style.visibility = "hidden";
		}
		
		codTipoDocumentoOld = document.forms[0].codTipoDocumento.value;
		desTipoDocumentoOld = document.forms[0].desTipoDocumento.value;
		documentoOld  = document.forms[0].documento.value;
		nombreOld = document.forms[0].nombre.value;
		telefonoOld = document.forms[0].telefono.value;
		emailOld = document.forms[0].email.value;
			
		provinciaOld = document.forms[0].provincia.value;
		municipioOld = document.forms[0].municipio.value;
		domicilioOld = document.forms[0].domicilio.value;
		poblacionOld = document.forms[0].poblacion.value;
		codigoPostalOld = document.forms[0].codigoPostal.value;
		
		terceroOld = document.forms[0].tercero.value;
		versionOld = document.forms[0].version.value;
	}
	
	function pulsarAceptar(){		
		if(!ini){
			if(validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')){
				document.forms[0].iniciado.value = 'false';
				document.forms[0].opcion.value = 'iniciar';
				document.forms[0].target="mainFrame";
				document.forms[0].action="<c:url value='/sge/InicioExpediente.do'/>";
				document.forms[0].submit();
			}
		}else{
			document.forms[0].iniciado.value = 'true';
			document.forms[0].opcion.value = 'iniciar';
			document.forms[0].target="mainFrame";
			document.forms[0].action="<c:url value='/sge/InicioExpediente.do'/>";
			document.forms[0].submit();
		}	
	}
	
	function pulsarCancelar(){					
		document.forms[0].codTipoDocumento.value = codTipoDocumentoOld;        	
		document.forms[0].desTipoDocumento.value = desTipoDocumentoOld;			
		document.forms[0].documento.value = documentoOld;			
		document.forms[0].nombre.value = nombreOld;			
		document.forms[0].telefono.value = telefonoOld;			
		document.forms[0].email.value = emailOld;
		
		document.forms[0].provincia.value = provinciaOld;			
		document.forms[0].municipio.value = municipioOld;			
		document.forms[0].domicilio.value = domicilioOld;			
		document.forms[0].poblacion.value = poblacionOld;			
		document.forms[0].codigoPostal.value = codigoPostalOld;
		
		document.forms[0].tercero.value = terceroOld;			
		document.forms[0].version.value = versionOld;
		
		if(!ini){
			document.forms[0].codProcedimiento.value = codProcedimientoOld;
			document.forms[0].descProcedimiento.value = descProcedimientoOld;		
		}
	}
	
	function pulsarSalir(){
		document.forms[0].opcion.value = 'salir';
		document.forms[0].target="mainFrame";
       	document.forms[0].action="<c:url value='/sge/InicioExpediente.do'/>";
       	document.forms[0].submit();
	}
	
	function recuperaBusquedaTerceros(datos){
		var MAX=0;
	   	if (datos.length>0){
      		cargarListaB(datos,'botonT');	  		
    	}else{
      		jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoRegistros")%>');
      		document.forms[0].codTerc.value = '0';
      		document.forms[0].codDomTerc.value = '0';
      		document.forms[0].numModifTerc.value = '0';			
    	}
    	dTipoDoc = document.forms[0].desTipoDocumento.value;
    	tipoDoc = document.forms[0].codTipoDocumento.value;
  	}
	
function pulsarBuscarTerceros(){
    var argumentos = new Array();
    argumentos =[new Array(),""];
    var source = "<c:url value='/BusquedaTerceros.do?opcion=inicializarBusquedaTerc&ventana=true'/>";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source, argumentos,
                'width=700,height=500,status='+ '<%=statusBar%>',function(datos){
                        if(datos){
                                vectorCampos = ['codTerc','numModifTerc','tercero','version','codTipoDocumento','documento','nombre',
                                          'txtApell1','txtApell2','txtPart','txtPart2','telefono','email','codDomTerc',
                                              'txtIdDomicilio','txtPais','provincia','municipio','domicilio','codigoPostal','poblacion'];
                                rellenarTercero(datos,vectorCampos);			
                        }		
                });
}
	
function abrirTerceros() {
    var tipo = document.forms[0].codTipoDocumento.value;
    var descDoc = "N.I.F.";
    var doc = document.forms[0].documento.value;
    var source;
    
    if((tipo!="")&&(doc!="")){
            source = '<%=request.getContextPath()%>/BusquedaTerceros.do?opcion=inicializar&destino=registroAlta&tipo='+tipo+'B&descDoc='+descDoc+'&docu='+doc;
    }else{
            source = '<%=request.getContextPath()%>/BusquedaTerceros.do?opcion=inicializar&destino=registroAlta&tipo=NoAlta&descDoc=&docu=';
    }
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,'terceros',
            'width=700,height=500,status='+ '<%=statusBar%>',function(ventana){});
}
	
	function mostrarDescripcionTipoDoc(){}
	
	document.onkeydown=checkKeys;
	document.onmouseup = checkKeys; 	
	function checkKeysLocal(evento,tecla) {
        var teclaAuxiliar = "";
        if(window.event){
            evento = window.event;
            teclaAuxiliar = evento.keyCode;
        }else{
            teclaAuxiliar = evento.which;
        }

		if ('Alt+C' == tecla) pulsarCancelar();
		if ('Alt+S' == tecla) pulsarSalir();
		if ('Alt+A' == tecla) pulsarAceptar();
                keyDel(evento);
		if((layerVisible)||(divSegundoPlano)) buscar();
		if (teclaAuxiliar == 9){
			if(layerVisible) ocultarDiv();
			if(divSegundoPlano) divSegundoPlano = false;
			return false;
		}
                if (teclaAuxiliar == 1){
			if(layerVisible) setTimeout('ocultarDivNoFocus()',40);
                        if(divSegundoPlano)	divSegundoPlano = false;
		}
		if (teclaAuxiliar == 40){
			if((layerVisible)||(divSegundoPlano)) upDown(teclaAuxiliar);
			return false;
		}
		if (teclaAuxiliar == 38){
			if((layerVisible)||(divSegundoPlano)) upDown(teclaAuxiliar);
			return false;
		}  
		if (evento.button == 1){
			if(layerVisible) setTimeout('ocultarDiv()',50);
			if(divSegundoPlano) divSegundoPlano = false;      
		}	

	}	
</script>

</head>

<BODY class="bandaBody" onload="javascript:{cargarInicio();}">
							
<html:form action="/sge/InicioExpediente.do" method="post">
<html:hidden property="codTerc" value="0"/>
<html:hidden property="codDomTerc" value="0"/>
<html:hidden property="numModifTerc" value="0"/>
<html:hidden property="txtIdDomicilio" value=""/>
<html:hidden property="txtPart" value=""/>
<html:hidden property="txtApell1" value=""/>
<html:hidden property="txtPart2" value=""/>
<html:hidden property="txtApell2" value=""/>
<html:hidden property="txtPais" value=""/>

<html:hidden property="tercero" />
<html:hidden property="version" />
<html:hidden property="iniciado" value=""/>
<html:hidden property="opcion" value=""/>
<CENTER>
	<TABLE id ="tabla1" class="tablaP" width="730px" cellspacing="0" cellpadding="0">
	<TR>
		<TD>
    		<TABLE width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  			<TR>
      			<TD>&nbsp;</TD>
    		</TR>
    		<TR>
      			<TD align="center" class="titulo">Inicio de Expediente</TD>
    		</TR>
			<TR>
				<TD>
					<TABLE width="100%" cellspacing="7px" cellpadding="1px" style="border-top: #7B9EC0 1px solid; border-bottom: #7B9EC0 1px solid;border-left: #7B9EC0 1px solid;border-right: #7B9EC0 1px solid;">
					<TR>
						<TD>
							<TABLE width="100%" cellspacing="7px" cellpadding="1px" style="border-top: #7B9EC0 1px solid; border-bottom: #7B9EC0 1px solid;border-left: #7B9EC0 1px solid;border-right: #7B9EC0 1px solid;">
							<TR>
								<TD width="17%" class="etiqueta">Registro:</TD>
								<TD width="12%" class="etiqueta">							
									<html:text styleClass="inputTextoDeshabilitado" property="registro" size="10" maxlength="10" readonly="true" tabindex="-1"/>
								</TD>
								<TD width="71%">
									<DIV id="capaBotones" name="capaBotones" style="width:100%; visibility:visible;">
									<TABLE>
									<TR>
										<TD width="12%" class="etiqueta">Expediente:</TD>					
										<TD width="61%" class="etiqueta"> 				  			
											<input type="text" class="inputTextoDeshabilitado" name="codProcedimientoExp" size="5" maxlength="5" readonly="true" tabindex="-1"/>&nbsp;.
											<html:text styleClass="inputTextoDeshabilitado" property="ejercicio" size="4" maxlength="4" readonly="true" tabindex="-1"/>&nbsp;.
											<html:text styleClass="inputTextoDeshabilitado" property="numero" size="6" maxlength="6" readonly="true" tabindex="-1"/>
										</TD>						
									</TR>
									</TABLE>
									</DIV>
								</TD>
							</TR>					
							<TR>
								<TD width="17%" class="etiqueta">Procedimiento:</TD>
								<TD class="etiqueta" colspan="3">
									<TABLE>
									<TR>
										<TD>
											<html:text styleClass="inputTextoDeshabilitado" property="codProcedimiento" styleId="obligatorio" size="5" maxlength="5" readonly="true" tabindex="-1"/>
											<html:text styleClass="inputTextoDeshabilitado" property="descProcedimiento" styleId="obligatorio" style="width:282;height:17" readonly="true"
												onfocus="javascript:{divSegundoPlano=true;inicializarValores('codProcedimiento', 'descProcedimiento',codProcedimientos,desProcedimientos);}"
          										onclick="javascript:{divSegundoPlano=false;inicializarValores('codProcedimiento', 'descProcedimiento',codProcedimientos,desProcedimientos);}"
											/>
										</TD>
										<TD>
											<DIV id="capaBotones2" name="capaBotones2" style="width:100%; visibility:hidden;">
												<A href="javascript:{divSegundoPlano=false;inicializarValores('codProcedimiento', 'descProcedimiento',codProcedimientos,desProcedimientos);}" style="text-decoration:none;" name="anchorPro" id="anclaD" onfocus="javascript:this.focus();"><span class="fa fa-chevron-circle-down" aria-hidden="true"  name="botonPro" style="cursor:hand;"></span></A>
											</DIV>
										</TD>
									</TR>
									</TABLE>	
								</TD>
							</TR>							
							</TABLE>					
						</TD>
					</TR>
					<TR>
        				<TD colspan="4">
							<TABLE align="center" id="tablaSolicitante" width="100%" cellspacing="7px" cellpadding="1px" border="0" style="border-top: #7B9EC0 1px solid; border-bottom: #7B9EC0 1px solid;border-left: #7B9EC0 1px solid;border-right: #7B9EC0 1px solid;">
							<TR>
								<TD align="center" class="etiquetaFondo">&nbsp;Solicitante</TD>
							</TR>
							<TR>
								<TD>
									<TABLE width="100%" cellspacing="0px" cellpadding="0px" border="0">
									<TR>
										<TD>
											<TABLE width="100%" cellspacing="0px" cellpadding="0px" border="0">
											<TR>
												<TD width="15%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqTiDoc")%>:</TD>
												<TD width="27%" class="columnP">
													<html:text property="codTipoDocumento" styleClass="inputTextoDeshabilitado" size="3" readonly="true" tabindex="-1"/>
													<html:text property="desTipoDocumento" styleClass="inputTextoDeshabilitado" style="width:140;height:17" readonly="true"/>									
												</TD>
												<TD width="12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqDocumento")%>:</TD>
												<TD width="24%" class="columnP">
													<html:text styleClass="inputTextoDeshabilitado" size="12" maxlength="9" property="documento" readonly="true" tabindex="-1"/>
												</TD>								
												<TD width="24%" class="columnP">
													<span class="fa fa-search" aria-hidden="true"  name="botonT" alt="Buscar Interesado"
														onclick="javascript:pulsarBuscarTerceros();"></span>
													<span class="fa fa-users" aria-hidden="true"  name="botonTer" alt="Mantenimiento de Terceros"
														onclick="javascript:abrirTerceros();"></span>
												</TD>
											</TR>
											</TABLE>
										</TD>
									</TR>					
									<TR>
										<TD>
											<TABLE width="100%" cellspacing="0px" cellpadding="0px" border="0">
											<TR>
												<TD width="15%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqNombr")%>/<BR><%=descriptor.getDescripcion("gEtiqRazonSoc")%>:&nbsp;</TD>
												<TD width="85%">
													<html:text styleClass="inputTextoDeshabilitado" style="width:487;" maxlength="50" property="nombre" readonly="true" tabindex="-1"/>
												</TD>
											</TR>
											</TABLE>
										</TD>
									</TR>
									</TABLE>
								</TD>
							</TR>
							<TR>
								<TD>
									<TABLE width="100%" cellspacing="0px" cellpadding="0px">
									<TR>
										<TD width="15%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqTelfFax")%>:&nbsp;</TD>
										<TD width="19%" class="etiqueta">
											<html:text property="telefono" styleClass="inputTextoDeshabilitado" style="width:120;" maxlength="20" readonly="true" tabindex="-1"/>
										</TD>
										<TD width="8%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqEmail")%>:&nbsp;</TD>
										<TD width="58%" class="columnP">
											<html:text property="email" styleClass="inputTextoDeshabilitado" style="width:300;" maxlength="30" readonly="true" tabindex="-1"/>
										</TD>
									</TR>
									</TABLE>
								</TD>
							</TR>										
							<TR>
								<TD>
									<TABLE width="100%" style="height:20px" cellspacing="0px" cellpadding="0px">
									<TR>
										<TD class="sub3titulo"><u><%=descriptor.getDescripcion("gEtiqDomicilio")%></u></TD>
									</TR>
									</TABLE>
								</TD>
							</TR>
							<TR>
								<TD>
									<TABLE width="100%" border="0" cellspacing="1" cellpadding="0">
									<TR>
										<TD width="15%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqProvincia")%>:&nbsp;</TD>			  
										<TD width="31%" class="columnP" valign="top">									
											<html:text property="provincia"  styleClass="inputTextoDeshabilitado" styleId="provincia" style="width:196;" readonly="true" tabindex="-1"/>
										</TD>
										<TD width="11%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqMunicipio")%>:&nbsp;</TD>
										<TD width="44%" class="columnP" valign="top">
											<html:text property="municipio"  styleClass="inputTextoDeshabilitado" styleId="municipio" style="width:194;" readonly="true" tabindex="-1"/>
										</TD>
									</TR>
									</TABLE>
								</TD>
							</TR>
							<TR>
								<TD>
									<TABLE width="100%" cellspacing="0px" cellpadding="0px">
									<TR>
										<TD width="15%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqDomicilio")%>:&nbsp;</TD>
										<TD width="85%" class="columnP">
											<html:text  styleClass="inputTextoDeshabilitado" property="domicilio" style="width:487;" maxlength="60" readonly="true" tabindex="-1"/>
										</TD>
									</TR>
									</TABLE>
								</TD>
							</TR>
							<TR>
								<TD>
									<TABLE border="0" width="100%" cellspacing="0px" cellpadding="0px">
									<TR>
										<TD width="15%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqPoblacion")%>:&nbsp;</TD>
										<td width="50%" class="columnP">
											<html:text styleClass="inputTextoDeshabilitado" style="width:335;" maxlength="40" property="poblacion" readonly="true" tabindex="-1"/>
										</TD>
										<TD width="10%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqCodPostal")%>:&nbsp;</td>
										<TD width="25%" class="columnP">
											<html:text styleClass="inputTextoDeshabilitado" style="width:71;" maxlength="5" property="codigoPostal" readonly="true" tabindex="-1"/>
										</TD>
									</TR>
									</TABLE>
								</TD>
							</TR>
							</TABLE>
						</TD>
					</TR>					
					</TABLE>
				</TD>
			</TR>				
			</TABLE>
			<TR>
        		<TD>&nbsp;</TD>
    		</TR>
			<TR>
				<TD>
					<DIV id="capaBotones3" name="capaBotones3" STYLE="position:absolute; width:100%;  height:0px; visibility:visible;">
				  		<TABLE class="tablaBotones" width="100%" align="center" >
					  	<TR>
					  		<TD width="20%" align="center">
								<input type="button" class="boton" value=<%=descriptor.getDescripcion("gbAceptar")%> name="cmdAceptar" onclick="pulsarAceptar();">
							</TD>
							<TD width="20%" align="center">
								<input type="button" class="boton" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdCancelar" onclick="pulsarCancelar();">
							</TD>
							<TD width="20%" align="center">
								<input type="button" class="boton" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir" onclick="pulsarSalir();">
							</TD>							
						</TR>
				  		</TABLE>
				  	</DIV>
				</TD>
			</TR>
		</TD>
	</TR>
	</TABLE>
</CENTER>
</html:form>

<script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>

</BODY>
</html:html>
