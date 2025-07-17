<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%
	UsuarioValueObject usuarioVO = new UsuarioValueObject();
	int idioma=1;

	if (session.getAttribute("usuario") != null){
		usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
		idioma = usuarioVO.getIdioma();
	}

%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="1" />


<html>
<head>
<title> Apertura Rexistro de Saída</title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<script src="<%=request.getContextPath()%>/scripts/general.js"></script>

<script>

function redirecciona(){

	var opcion = '<bean:write name="AperturaCierreRegistroForm" property="opcion"/>';
	var mnsx, mnsx1, mensaje, dia, mes, ano, respuesta, source, datos;

	if ((opcion== 'error_abrir_entrada') || (opcion=='error_abrir_salida') )
	{
		dia = <bean:write name="AperturaCierreRegistroForm" property="txtDiaAbrir"/>;
		mes = <bean:write name="AperturaCierreRegistroForm" property="txtMesAbrir"/>;
		ano = <bean:write name="AperturaCierreRegistroForm" property="txtAnoAbrir"/>;
	}
	else {
		dia = <bean:write name="AperturaCierreRegistroForm" property="txtDiaCerrar"/>;
		mes = <bean:write name="AperturaCierreRegistroForm" property="txtMesCerrar"/>;
		ano = <bean:write name="AperturaCierreRegistroForm" property="txtAnoCerrar"/>;
	}

	if ( opcion == 'cerrar_entrada_denegado') {
            
		mnsx='<%=descriptor.getDescripcion("msj_cerrarfdRE")%>';
                mnsx1='<%=descriptor.getDescripcion("msj_cerrarfd")%>';
                titulo='<%=descriptor.getDescripcion("titNoCerrarReg")%>';
                mensaje = mnsx+ ' '+ dia +'/'+ mes +'/'+ano + ' ' + mnsx1;
                abrirXanelaAuxiliar(APP_CONTEXT_PATH+'/jsp/registro/alertaCierreRegistro.jsp?TituloMSG=' + titulo +  '&DescMSG=' + mensaje, null,
                        'width=350,height=160,scrollbars=no,status=no',function(respuesta){
                            if (respuesta == 'verReservas') {
                                document.forms[0].opcion.value="cargarRERE";
                                document.forms[0].target="mainFrame";
                                document.forms[0].action="<%=request.getContextPath()%>/RelacionReserva.do";
                                document.forms[0].submit();
                            } else if (respuesta == 'anularReservas') {
                                source = "<html:rewrite page='/jsp/registro/anulacionReserva.jsp?dummy='/>";
                                abrirXanelaAuxiliar("<html:rewrite page='/jsp/registro/mainVentana.jsp?source='/>" + source, '<%=descriptor.getDescripcion("etiqMultReserva")%>',
                                    'width=640,height=400,status=no',function(datos){
                                        if (datos!=undefined && datos['aceptar']) {
                                           document.forms[0].diligencia.value = datos['diligencia'];
                                           document.forms[0].opcion.value = "anular_entrada";
                                           document.forms[0].target = "oculto";
                                           document.forms[0].action = "<%=request.getContextPath()%>/AperturaCierreRegistro.do";
                                           document.forms[0].submit();
                                       }
                                   });
                            } else {
                               document.forms[0].opcion.value = "salirE";
                               document.forms[0].target="mainFrame";
                               document.forms[0].action = "<%=request.getContextPath()%>/ReservaOrden.do";
                               document.forms[0].submit();
                            }
                        });
	} else if (opcion == 'cerrar_salida_denegado') {
        
		mnsx='<%=descriptor.getDescripcion("msj_cerrarfdRS")%>';
                mnsx1='<%=descriptor.getDescripcion("msj_cerrarfd")%>';
                titulo='<%=descriptor.getDescripcion("titNoCerrarReg")%>';
                mensaje = mnsx+ ' '+ dia +'/'+ mes +'/'+ano + ' ' + mnsx1;
                abrirXanelaAuxiliar(APP_CONTEXT_PATH+'/jsp/registro/alertaCierreRegistro.jsp?TituloMSG=' + titulo +  '&DescMSG=' + mensaje, null,
                        'width=350,height=160,scrollbars=no,status=no',function(respuesta){
                                if (respuesta == 'verReservas') {
                                   document.forms[0].opcion.value="cargarRERS";
                                   document.forms[0].target="mainFrame";
                                   document.forms[0].action="<%=request.getContextPath()%>/RelacionReserva.do";
                                   document.forms[0].submit();
                               } else if (respuesta == 'anularReservas') {
                                    source = "<html:rewrite page='/jsp/registro/anulacionReserva.jsp?dummy='/>";
                                    abrirXanelaAuxiliar("<html:rewrite page='/jsp/registro/mainVentana.jsp?source='/>" + source, '<%=descriptor.getDescripcion("etiqMultReserva")%>',
                                            'width=640,height=400,status=no',function(datos){
                                                if (datos!=undefined && datos['aceptar']) {
                                                    document.forms[0].diligencia.value = datos['diligencia'];
                                                    document.forms[0].opcion.value = "anular_salida";
                                                    document.forms[0].target = "oculto";
                                                    document.forms[0].action = "<%=request.getContextPath()%>/AperturaCierreRegistro.do";
                                                    document.forms[0].submit();
                                                }
                                            });
                               } else {
                                   document.forms[0].opcion.value = "salirS";
                                   document.forms[0].target="mainFrame";
                                   document.forms[0].action = "<%=request.getContextPath()%>/ReservaOrden.do";
                                   document.forms[0].submit();
                               }
                        });
                }else if (opcion == 'error_abrir_entrada') {
		mnsx='<%=descriptor.getDescripcion("msj_abrirfdR")%>';
                jsp_alerta("A", mnsx+ ' '+ dia +'/'+ mes +'/'+ano);
	} else {
		mnsx='<%=descriptor.getDescripcion("msj_cerrarfdR")%>';
                jsp_alerta("A", mnsx+ ' '+ dia +'/'+ mes +'/'+ano);
	}

}

</script>

</head>
<body onLoad="redirecciona();">

<form>
        <input type="hidden" name="opcion" value="">
        <input type="hidden" name="diligencia" value="">
</form>

</body>
</html>
