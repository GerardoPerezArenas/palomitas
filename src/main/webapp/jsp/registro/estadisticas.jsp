<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.registro.InformesRegistroForm"%>
<%@page import="java.util.Vector" %>
<html>
<head>
<title> REGISTRO. INFORME ESTADISTICAS </title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<meta http-equiv="" content="text/html; charset=iso-8859-1">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<link href="<%=request.getContextPath()%>/css/estilos_informe.css" rel="stylesheet" type="text/css">
<%
	UsuarioValueObject usuarioVO = new UsuarioValueObject();
	int idioma=1;
	int apl = 2;
	String entidad="";

	if (session.getAttribute("usuario") != null){
		usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
		idioma = usuarioVO.getIdioma();
		apl = usuarioVO.getAppCod();
		entidad = usuarioVO.getEnt();
	}

  InformesRegistroForm irForm =(InformesRegistroForm)session.getAttribute("InformesRegistroForm");
  Vector datos = irForm.getEstadisticas();
  if (datos == null) datos = new Vector();
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<%
  String codMes[] = {"01","02","03","04","05","06","07","08","09","10","11","12"};
  String descMes[] = {descriptor.getDescripcion("TxtEnero"),descriptor.getDescripcion("TxtFebrero"),descriptor.getDescripcion("TxtMarzo"),
	descriptor.getDescripcion("TxtAbril"),descriptor.getDescripcion("TxtMayo"),descriptor.getDescripcion("TxtJunio"),
	descriptor.getDescripcion("TxtJulio"),descriptor.getDescripcion("TxtAgosto"),descriptor.getDescripcion("TxtSeptiembre"),
	descriptor.getDescripcion("TxtOctubre"),descriptor.getDescripcion("TxtNoviembre"),descriptor.getDescripcion("TxtDiciembre")};
%>



<!-- Ficheros JavaScript -->
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link href="<%=request.getContextPath()%>/css/estilo.css" rel="stylesheet" type="text/css">

<script type="text/javascript">
</script>

</head>

<body class="bandaBody">

<table width="800px" border=0 cellspacing=0 cellpading=0 >			
  <tr>
    <td width="100%" valign="top">

	    <!-- Cabecera -->    
		<table width="100%" height="52px" cellpadding="0px" cellspacing="0px">
   		<tr>
			<td width="799px" bgcolor="#e6e6e6" height="52px">
				<table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
            		<tr>
						<td>
							<table width="100%" height="100%" cellpadding="0px" cellspacing="0px" bgcolor="#e6e6e6">
								<tr>
									<td width="80px" align="center" valign="middle"><img src="<%= request.getContextPath() + usuarioVO.getOrgIco()%>"  height="45px" border="0px"></td>
									<td width="2px" align="center" valign="middle"><img src="<%=request.getContextPath()%>/images/separador.gif" border="0px"></td>
								<td>
									<table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
										<tr>
											<td valign="middle">&nbsp;<span class="txttitsmallcabecera"><%= entidad %></span></td>
										</tr>
										<tr>
											<td height="1px"></td>
										</tr>
									</table>
								</td>
								<td width="55px">
									<table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
										<tr>
											<td valign="middle"></td>
										</tr>
									</table>								
								</td>
								<td width="2px" align="center" valign="middle"><img src="<%=request.getContextPath()%>/images/separador_discontinuo.gif" border="0px"></td>
								<td width="120px">
									<table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
										<tr>
											<td height="17px" align="right" valing="middle"><span class="txtnegrobold">&nbsp;<%=descriptor.getDescripcion("gEtiqFecha")%>&nbsp;</span><span class="txtsmallnegro"><script>document.write(fechaHoy());</script></span></td>
										</tr>
									</table>
								</td>
								<td width="22px" align="center" valign="top"></td>
							</tr>
						</table>
					</td>
				</tr>
				</table>
			</td>
						
			<!-- Sombra lateral. -->
			<td width="1px" height="52px">
				<table width="100%" height="52px" cellpadding="0px" cellspacing="0px">
					<tr>
						<td height="1px" bgcolor="#B0BDCD"></td>
            		</tr>
            		<tr>
						<td height="51px" bgcolor="#393939"></td>
            		</tr>            
         		</table>
      		</td>
			<!-- Fin sombra lateral. -->
		</tr>
	
		<!-- Sombra inferior. -->
		<tr>
			<td colspan="2" height="1px">
				<table cellpadding="0px" cellspacing="0px">
					<tr>
						<td width="1px" height="1px" bgcolor="#B0BDCD"></td>
						<td width="799px" height="1px" bgcolor="#393939"></td>
					</tr>
				</table>
			</td>
		</tr>
		<!-- Fin sombra inferior. -->
		</table>
		<!-- Fin cabecera. -->
   
   	</td>
 </tr>
  <tr>
    <td width="100%" height="300px" valign="top">
    
    <!-- Datos. -->
            <table width="799px" height="530px" cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td width="799px" height="530px" align="left" >
                                    <table width="100%" height="100%" cellpadding="0px" cellspacing="0px" bgcolor="#666666">
                                    <tr>
                                            <td>
                                            <table id="tabla1" width="100%" height="100%" cellpadding="0px" cellspacing="1px" border="0px" bgcolor="#ffffff">
                                                    <tr>
                                                            <td class="txttitblanco"><%=descriptor.getDescripcion("titEstReg")%></td>
                                                    </tr>
                                                    <tr><td width="100%" height="1px" bgcolor="#666666"></td></tr>
                                                    <tr>
                                                            <td width="100%" bgcolor="#e6e6e6" align="center" valign="top">
                                                                    <!-- Separador. -->
                                                                <table height="3px" cellpadding="0px" cellspacing="0px">
                                                                        <tr><td></td></tr>
                                                                    </table>
                                                                    <!-- Fin separador. -->
                                                                    </td>
                                                            </tr>
                                                            <tr>
                                                                            <td bgcolor="#FFFFFF" width="100%" height="495px" style="border-top: #666666 1px solid; border-bottom: #666666 1px solid;border-left: #666666 1px solid;border-right: #666666 1px solid;" valign="top">  
                                                                            <table width="789px" cellpadding="0" cellspacing="0" border="0" align="center" bgcolor="#FFFFFF">
                                                                                    <tr>
                                                                                            <td height="3px" cellpadding="0px" cellspacing="0px"></td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                            <td cellpadding="0px" cellspacing="0px" >
                                                                                                    <table border="0" width="100%" height="15" cellpadding="0px" cellspacing="0px" >          												
                                                                                                            <tr>
                                                                                                                            <td width="31px%" height="15"></td>
                                                                                                                            <td width="2px" height="15"></td>																
                                                                                                                    <td width="376px" bgcolor="#B0BDCD" align="center" height="15">
                                                                                                                                    <font face="Arial" size="2"><%=descriptor.getDescripcion("tit_AnotE")%></font>																	
                                                                                                                            </td>
                                                                                                                            <td width="2px" height="15"></td>																
                                                                                                                    <td width="376px" bgcolor="#B0BDCD" align="center" height="15">
                                                                                                                                    <font face="Arial" size="2"><%=descriptor.getDescripcion("tit_AnotS")%></font>
                                                                                                                            </td>																
                                                                                                                            <td width="2px" height="15"></td>																
                                                                                                                    </tr>          												
                                                                                                                    <tr>
                                                                                                                            <td width="31px"  bgcolor="#B0BDCD"  align="center" >
                                                                                                                                    <font face="Arial" size="2"><%=descriptor.getDescripcion("etiqAno")%></font>
                                                                                                                            </td>
                                                                                                                            <td width="2px" height="15"></td>																	  											
                                                                                                                            <td width="376px" align="center" height="15">
                                                                                                                                <table border="0" width="100%" height="15"  bgcolor="#e6e6e6" >
                                                                                                                                    <tr>																
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtEnero").substring(0,3)%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <td width="28px"  align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtFebrero").substring(0,3)%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtMarzo").substring(0,3)%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtAbril").substring(0,3)%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtMayo").substring(0,3)%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtJunio").substring(0,3)%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtJulio").substring(0,3)%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtAgosto").substring(0,3)%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtSeptiembre").substring(0,3)%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtOctubre").substring(0,3)%></font>
                                                                                                                                                    </td>			
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtNoviembre").substring(0,3)%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtDiciembre").substring(0,3)%></font>
                                                                                                                                                    </td>	
                                                                                                                                                    <td width="40px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1">Total</font>
                                                                                                                                                    </td>																																	
                                                                                                                                            </tr>
                                                                                                                                    </table>
                                                                                                                            </td>
                                                                                                                            <td width="2px" height="15"></td>																
                                                                                                                            <td width="376px" align="center" height="15">
                                                                                                                                <table border="0" width="100%" height="15"  bgcolor="#e6e6e6" >
                                                                                                                                    <tr>		
                                                                                                                                                    <td width="2px" ></td>																																														
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtEnero").substring(0,3)%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtFebrero").substring(0,3)%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtMarzo").substring(0,3)%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtAbril").substring(0,3)%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtMayo").substring(0,3)%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtJunio").substring(0,3)%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtJulio").substring(0,3)%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtAgosto").substring(0,3)%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtSeptiembre").substring(0,3)%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtOctubre").substring(0,3)%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtNoviembre").substring(0,3)%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=descriptor.getDescripcion("TxtDiciembre").substring(0,3)%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <td width="40px" align="center" >
                                                                                                                                                            <font face="Arial" size="1">Total</font>
                                                                                                                                                    </td>																																		
                                                                                                                                            </tr>
                                                                                                                                    </table>
                                                                                                                            </td>
                                                                                                                            <td width="4px" ></td>																																
                                                                                                                    </tr>
                                                                                                                    <!-- Estadisticas -->					  										
                                                                                                                    <%				
                                                                                                                              int filas=0;
                                                                                                                              String colorFondo1 ="#FFFFFF";
                                                                                                                              String colorFondo2 ="#e6e6e6";

                                                                                                                              for (int i=0; i<datos.size(); i++ ){
                                                                                                                                    filas++;
                                                                                                                                    GeneralValueObject gVO = (GeneralValueObject) datos.elementAt(i);
                                                                                                                                    String ano = (String) gVO.getAtributo("ano");
                                                                                                                    %>
                                                                                                                    <tr>
                                                                                                                            <td width="55px"  bgcolor="#B0BDCD"  align="center" >
                                                                                                                                    <font face="Arial" size="2"><%= ano%></font>
                                                                                                                            </td>
                                                                                                                            <td width="2px" height="15"></td>																	  											
                                                                                                                            <td width="376px" align="center" height="15">
                                                                                                                                    <table border="0" width="100%" height="15" bgColor="<%= (filas % 2==0)?colorFondo2:colorFondo1%>">
                                                                                                                                    <tr>																
                                                                                                                                                    <% 
                                                                                                                                                            int total = 0;
                                                                                                                                                            GeneralValueObject eVO = (GeneralValueObject) gVO.getAtributo("E");

                                                                                                                                                            for (int j=0; j<codMes.length; j++) {
                                                                                                                                                                    String cuentaMes = null;
                                                                                                                                                                    if (eVO != null) cuentaMes=  (String) eVO.getAtributo(codMes[j]);  		
                                                                                                                                                                    if (cuentaMes == null) cuentaMes="0";
                                                                                                                                                                    total += Integer.parseInt(cuentaMes);  			
                                                                                                                                                    %>
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=cuentaMes%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <%
                                                                                                                                                            }
                                                                                                                                                    %>
                                                                                                                                                    <td width="40px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=total%></font>
                                                                                                                                                    </td>
                                                                                                                                            </tr>
                                                                                                                                    </table>
                                                                                                                            </td>
                                                                                                                            <td width="2px" height="15" ></td>																
                                                                                                                            <td width="376px" align="center" height="15">
                                                                                                                                <table border="0" width="100%" height="15"  bgcolor="<%= (filas % 2==0)?colorFondo2:colorFondo1%>">
                                                                                                                                    <tr>	
                                                                                                                                                    <% 
                                                                                                                                                            total = 0;
                                                                                                                                                            eVO = (GeneralValueObject) gVO.getAtributo("S");

                                                                                                                                                            for (int j=0; j<codMes.length; j++) {  		
                                                                                                                                                                    String cuentaMes = null;
                                                                                                                                                                    if (eVO!=null) cuentaMes= (String) eVO.getAtributo(codMes[j]);  		
                                                                                                                                                                    if (cuentaMes == null) cuentaMes="0";
                                                                                                                                                                    total += Integer.parseInt(cuentaMes);  			
                                                                                                                                                    %>
                                                                                                                                                    <td width="28px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=cuentaMes%></font>
                                                                                                                                                    </td>
                                                                                                                                                    <%
                                                                                                                                                            }
                                                                                                                                                    %>
                                                                                                                                                    <td width="40px"   align="center" >
                                                                                                                                                            <font face="Arial" size="1"><%=total%></font>
                                                                                                                                                    </td>
                                                                                                                                            </tr>
                                                                                                                                    </table>
                                                                                                                            </td>
                                                                                                                            <td width="4px" ></td>																																
                                                                                                                    </tr>
                                                                                                                    <% } %>																																		
                                                                                                                    <!-- Fin estadisticas -->
                                                                                                    </table>
                                                                                            </td>
                                                                                    </tr>          										
                                                                            </table>
                                                                    </td>
                                                    </tr>              						
                                                    <table>		
                                            </td>
                                    </tr>
                            </table>
                    </td>
                    </tr>
    	</table>
    	<!--Datos-->
   	</td>
 </tr>
</td>
</tr>
</table>

	

</body>
</html>
