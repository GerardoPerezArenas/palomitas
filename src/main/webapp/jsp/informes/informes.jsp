<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.interfaces.user.web.informes.InformesForm"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.*"%>
<html><head>
<title> INFORME </title>
<jsp:include page="/jsp/informes/tpls/app-constants.jsp" />
<meta http-equiv="" content="text/html; charset=iso-8859-1">
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/estilos_informe.css'/>">
<% UsuarioValueObject usuarioVO = null;
   int idioma = 1;
   Vector estadisticas = new Vector();
   String tipoSalida = "";
   String usu = "";
   String entidad = "";
   boolean esHtml = true;
   if (session!=null){
   	 usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
   	 idioma =  usuarioVO.getIdioma();
	 InformesForm iForm =(InformesForm)session.getAttribute("InformesForm");
	 estadisticas = iForm.getEstadisticas();
     tipoSalida = iForm.getTipoSalida(); 
	 usu = usuarioVO.getNombreUsu();
	 entidad = usuarioVO.getEnt();
     if ( (tipoSalida!=null) && (tipoSalida.equals("XLS")) ) response.setContentType("application/vnd.ms-excel"); 
	 esHtml = ( (tipoSalida!=null) && (!tipoSalida.equals("XLS")) && (!tipoSalida.equals("PDF")) );
  } %>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%= usuarioVO.getIdioma()%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= usuarioVO.getAppCod()%>" />
</head>
<body onload="javascript:{top.focus();}">
<table width="100%" border=0 cellspacing=0 cellpading=0 >
  <tr>
    <td width="100%" height="100%" valign="top">
      <DIV id="divTabla" name="divTabla" style="width:100%;height:600px;overflow:auto;visibility:visible">
	<table width="100%" border="0px" rules=all cellspacing=0 cellpading=0 align="center">			
	<%GeneralValueObject titulosVO = (GeneralValueObject)estadisticas.remove(0);
		Vector titulos = (Vector)titulosVO.getAtributo("titulos");		
		int cols=titulos.size();
		boolean mostrarPendientes = false;
		String verPend = (String)titulosVO.getAtributo("VerPend");		
		mostrarPendientes = ( (verPend!=null) && (verPend.equalsIgnoreCase("true")) );
		boolean mostrarExpedientes = false;
		String verVol = (String)titulosVO.getAtributo("VerVol");		
		mostrarExpedientes = ( (verVol!=null) && (verVol.equalsIgnoreCase("true")) );
		boolean mostrarHistoricos = false;
		String verFin = (String)titulosVO.getAtributo("VerFin");		
		mostrarHistoricos = ( (verFin!=null) && (verFin.equalsIgnoreCase("true")) );
		String todos = (String) titulosVO.getAtributo("todos");%>
	<%if (esHtml){%>
	<tr>
		<%int totalCols = cols + 6;
		  if (mostrarHistoricos) totalCols += 3;%>
		<td style="height:50px" colspan="<%=totalCols-4%>" class="cabeceroEntorno">
			<img align="middle" src="<%= request.getContextPath() + usuarioVO.getOrgIco()%>" height="52">
				<%=entidad%>
		</td>		
		<td style="height:50px" colspan=4 align="left" class="cabeceroFecha">
		  &nbsp;&nbsp;<script>document.write(fechaHoy());</script>
		</td>	
	</tr>
	<tr><td height="40px" valign="top" colspan=<%=totalCols%>><hr size=2 color="#666699" style="solid"></td></tr>
	<%}%>
	<tr>
		<td height="15px" colspan=<%=cols%>>&nbsp;</td>
		<%if (mostrarPendientes) {%>
		<td height="15px" class="encabezado" style="text-align: center; border-left: 1 white solid" colspan=3><%=descriptor.getDescripcion("etiqTarPend")%> </td>		
		<%}
		if (mostrarHistoricos){
			int tiempo = ((Integer)titulosVO.getAtributo("tiempo")).intValue();
			String texto = "";
			if (tiempo==1) texto = " (semana)";
			if (tiempo==2) texto = " (mes)";
			if (tiempo==3) texto = " (trimestre)";
		%>		
		<td height="15px" class="encabezado" style="text-align: center; border-left: 1 white solid" colspan=3><%=descriptor.getDescripcion("etiqTarRealiz")%><%=texto%></td>		
		<%}
		if (mostrarExpedientes){%>
		<td height="15px" class="encabezado" style="text-align:center; border-left: 1 white solid" colspan=3><%=descriptor.getDescripcion("inf_Expedientes")%></td>		
		<%}%>
	</tr>
	<tr>
	<%--Lee los titulos de las columnas del informe--%>
	<%if (!(estadisticas.isEmpty())){
		String estiloTit = "";
		//GeneralValueObject titulosVO = (GeneralValueObject)estadisticas.remove(0);
		//Vector titulos = (Vector)titulosVO.getAtributo("titulos");		
		for (int i=0; i<titulos.size(); i++){	
			if (i==0) estiloTit = "titulo1";
			else if (i<titulos.size()-1){
				if ((i%2)>0) estiloTit = "tituloImpar";
				else estiloTit = "tituloPar";
			}
			else if (i==titulos.size()-1) estiloTit = "tituloU";
			String titulo = (String)titulos.elementAt(i);		
		%>
				
		<td height="15px" noWrap class="<%=estiloTit%>">
		
		<%if (titulo.equals("inf_TRA")){%><%=descriptor.getDescripcion("inf_TRA")%>&nbsp;&nbsp; <%
		}else if (titulo.equals("inf_TPR")){%><%=descriptor.getDescripcion("inf_TPR")%>&nbsp;&nbsp; <%
		}else if (titulo.equals("inf_ARE")){%><%=descriptor.getDescripcion("inf_ARE")%>&nbsp;&nbsp; <%
		}else if (titulo.equals("inf_UTR")){%><%=descriptor.getDescripcion("inf_UTR")%>&nbsp;&nbsp; <%
		}else if (titulo.equals("inf_PRO")){%><%=descriptor.getDescripcion("inf_PRO")%>&nbsp;&nbsp; <%
		}else if (titulo.equals("inf_CLS")){%><%=descriptor.getDescripcion("inf_CLS")%>&nbsp;&nbsp; <%
		}%>
						
		</td>
				
		<%} if (titulos.size()==0){ %>
		<td height="15px">&nbsp;</td>
		<%}%>
		
		<%if (mostrarPendientes) {%>
		<td height="15px" class="encabezado" style="text-align:right; border-left:white 1 solid; border-top:1 solid white">&nbsp;<%=descriptor.getDescripcion("inf_Tareas")%>&nbsp;</td>
		<td height="15px" class="encabezado" style="text-align:right; border-left:white 1 solid;border-top:1 solid white">&nbsp;<%=descriptor.getDescripcion("inf_Exped")%>&nbsp;</td>
		<td height="15px" class="encabezado" style="text-align:right;  border-left:white 1 solid;border-top:1 solid white">&nbsp;<%=descriptor.getDescripcion("inf_Tiempos")%>&nbsp;</td>
		<%}%>
		
		<%if (mostrarHistoricos) {%>
			<td height="15px" class="encabezado" style="text-align:right; border-left:white 1 solid; border-top:1 solid white">&nbsp;<%=descriptor.getDescripcion("inf_Tareas")%>&nbsp;</td>
			<td height="15px" class="encabezado" style="text-align:right; border-left:white 1 solid; border-top:1 solid white">&nbsp;<%=descriptor.getDescripcion("inf_Exped")%>&nbsp;</td>
			<td height="15px" class="encabezado" style="text-align:right; border-left:white 1 solid;border-top:1 solid white" noWrap>&nbsp;<%=descriptor.getDescripcion("inf_Tiempos")%>&nbsp;</td>
		<%}%>
		
		<% if (mostrarExpedientes) {%>
		<td height="15px" class="encabezado" style="text-align:right; border-left:white 1 solid; border-top:1 solid white">&nbsp;<%=descriptor.getDescripcion("inf_Fin")%>&nbsp;</td>
		<td height="15px" class="encabezado" style="text-align:right; border-left:white 1 solid;border-top:1 solid white" noWrap>&nbsp;<%=descriptor.getDescripcion("inf_EnTramite")%>&nbsp;</td>
		<td height="15px" class="encabezado" style="text-align:right; border-left:white 1 solid;border-top:1 solid white">&nbsp;<%=descriptor.getDescripcion("inf_Totales")%>&nbsp;</td>
		<%}%>
		
		
	<%}%>
	
	</tr>
	
	<%--Lee los resultados--%>
	<%		
	while(!estadisticas.isEmpty()){%>
			<tr>
			<%--Saca cada linea de resultados--%>
			<%
			GeneralValueObject gVO = (GeneralValueObject)estadisticas.remove(0);
			String estiloA = "";
			String estiloD = "";
			int cuenta;
								
			//Lee los elementos de agrupacion
			Vector agrupaciones = (Vector)gVO.getAtributo("agrupaciones");						
			estiloA = "";
			estiloD = "";
			
			//Determina el estilo
			cuenta = agrupaciones.size()-1;
			while (cuenta >= 0) {
				String agrupacion = (String)agrupaciones.elementAt(cuenta);
				if (agrupacion != null && agrupacion.equals("")) break;
				
				if ((cuenta == 0) && agrupacion !=null && ( (agrupacion.startsWith("Tod"))||(agrupacion.startsWith("inf_Resumen")) ) ){
					estiloA="agrupaA";
					estiloD="agrupaD";
				} else if ((cuenta ==0) && agrupacion!=null && (!agrupacion.startsWith("Tod"))) {
					estiloA="agrupacion1";
					estiloD="detalle1";
				} else if ((cuenta > 0) && agrupacion !=null && (cuenta < (agrupaciones.size() - 1))) {
					if ((cuenta % 2) > 0) {
						estiloA="agrupacionImpar";
						estiloD="detalleImpar";
					} else {
						estiloA="agrupacionPar";
						estiloD="detallePar";
					}
				} else {
					estiloA="agrupacionU";
					estiloD="detalleU";
				}
				
				cuenta--;
			}
			
												
			for (int i=0; i<agrupaciones.size() ; i++){
				String agrupacion = (String)agrupaciones.elementAt(i);		
				if (agrupacion != null && !agrupacion.equals("")) {
					if (agrupacion.equals("inf_Resumen")){%>
						<td height="15px" class="<%=estiloA%>" colspan="<%=agrupaciones.size() - i%>"><%=descriptor.getDescripcion("inf_Resumen")%></td>
					<%} else if (agrupacion != null && agrupacion.equals("999-inf_PendAsign")){%>
						<td height="15px" class="<%=estiloA%>" colspan="<%=agrupaciones.size() - i%>"><%=descriptor.getDescripcion("inf_PendAsign")%></td>
					<%} else {%> <td height="15px" class="<%=estiloA%>" colspan="<%=agrupaciones.size() - i%>"><%=agrupacion%><%}
																			 
					break;
				} else {
					%><td height="15px"><%=agrupacion%></td>
             <%				
				}
			} 
			
			if (agrupaciones.size()==0){
				estiloD="agrupaD";%>
				<td height="15px" class="agrupaA">Resumen total</td>
			<%}	
			

			
			Hashtable parametros = (Hashtable)gVO.getAtributo("parametros");
			Enumeration enum1 = parametros.keys();
			String paramEnlace = "";
			while(enum1.hasMoreElements()){
				String nombre = (String)enum1.nextElement();
				String valor = (String)parametros.get(nombre);
				paramEnlace += "&"+nombre+"="+valor;
			}
			paramEnlace += "&todos="+todos;

			String valor = null;					
			//Lee los resultados 																									
			if (mostrarPendientes) {
			
			valor = (String)gVO.getAtributo("tareasPendientes");%>						
			<td height="15px" class=<%=estiloD%>borde>&nbsp;<%=valor%>&nbsp;</td>
			
			<% valor = (String)gVO.getAtributo("expedientesPendientes");%>
			<%if (esHtml){%><td height="15px" class=<%=estiloD%>>&nbsp;<a class="<%=estiloD%>" href="<%=request.getContextPath()%>/sge/ConsultaExpedientes.do?opcion=consultarDesdeGestion&estado=abierto&tipo=expedientesPendientes<%=paramEnlace%>"><%=valor%></a>&nbsp;</td>
			<%} else {%><td height="15px" class=<%=estiloD%>borde>&nbsp;<%=valor%>&nbsp;</td>
			
			<%} valor = (String)gVO.getAtributo("tiemposPendientes");%>
			<td height="15px" class=<%=estiloD%>>&nbsp;<%=valor%>&nbsp;</td>
			<% }
			
			if (mostrarHistoricos) {
				valor = (String)gVO.getAtributo("tareasHistoricas");%>
				<td height="15px" class=<%=estiloD%>borde>&nbsp;<%=valor%>&nbsp;</td>
				
				<% valor = (String)gVO.getAtributo("expedientesHistoricos");%>
				<%if (esHtml){%><td height="15px" class=<%=estiloD%>>&nbsp;<a class="<%=estiloD%>" href="<%=request.getContextPath()%>/sge/ConsultaExpedientes.do?opcion=consultarDesdeGestion&estado=cerrado&tipo=expedientesHistoricos<%=paramEnlace%>"><%=valor%></a>&nbsp;</td>
				<%} else {%><td height="15px" class=<%=estiloD%>borde>&nbsp;<%=valor%>&nbsp;</td>
				
				<%}valor = (String)gVO.getAtributo("tiemposHistoricos");%>			

				<td height="15px" class=<%=estiloD%>>&nbsp;<%=valor%>&nbsp;</td><%}
			
				
			if (mostrarExpedientes) {
			valor = (String)gVO.getAtributo("expedientesCerrados");%>
			<%if (esHtml){%><td height="15px" class=<%=estiloD%>borde>&nbsp;<a class="<%=estiloD%>" href="<%=request.getContextPath()%>/sge/ConsultaExpedientes.do?opcion=consultarDesdeGestion&estado=cerrado&tipo=expedientesCerrados<%=paramEnlace%>"><%=valor%></a>&nbsp;</td>
			<%} else {%><td height="15px" class=<%=estiloD%>borde>&nbsp;<%=valor%>&nbsp;</td>
			
			<%}valor = (String)gVO.getAtributo("expedientesEnTramitacion");%>
			<%if (esHtml){%><td height="15px" class=<%=estiloD%>>&nbsp;<a class="<%=estiloD%>" href="<%=request.getContextPath()%>/sge/ConsultaExpedientes.do?opcion=consultarDesdeGestion&estado=abierto&tipo=expedientesPendientesVolumen<%=paramEnlace%>"><%=valor%></a>&nbsp;</td>
			<%} else {%><td height="15px" class=<%=estiloD%>borde>&nbsp;<%=valor%>&nbsp;</td>
												
			<%}valor = (String)gVO.getAtributo("expedientesTotales");%>
			<%if (esHtml){%><td height="15px" class=<%=estiloD%>>&nbsp;<a class="<%=estiloD%>" href="<%=request.getContextPath()%>/sge/ConsultaExpedientes.do?opcion=consultarDesdeGestion&estado=sinEstado&tipo=expedientesTotales<%=paramEnlace%>"><%=valor%></a>&nbsp;</td></tr>
			<%} else {%><td height="15px" class=<%=estiloD%>borde>&nbsp;<%=valor%>&nbsp;</td><%} 
			}%>
			
	<%}%>
	
  	</table>
</div>
</td>
</tr>
</table>

	

</body>
</html>
