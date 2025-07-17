<%--
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<html>
	<head>
        <title>Prueba</title>
	</head>
    <body>
 --%>


<!-- se mostrará por debajo del div para tapar los combos -->
<div id="divframeSending" style="position: absolute; top:80px; z-index:9995; display: none;" align="center">
    <table width="100%"> <tr> <td width="100%" align="center">
    <iframe id="iFrameHidePageSending" src="" width="350px" height="120px"
        style="display: none; z-index: 9995;">
    </iframe>
    </td> </tr> </table>
</div>
<!-- Div para la barra de progreso -->
<div id="hidepageSending" style="position: absolute; top:80px; z-index:9995; display: none;">
<table width="100%" border="0px" cellpadding="0px" cellspacing="0px">
   <tr>
      <td align="center" valign="middle">
		<table width="350px" height="120px" cellpadding="0px" cellspacing="0px">
			<tr>
				<td width="349px" bgcolor="#e6e6e6" height="120px">
					<table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
						<tr>
							<td>
								<table width="100%" height="100%" cellpadding="0px" cellspacing="0px" bgcolor="#e6e6e6">
									<tr>
										<td colspan="3" height="70%" align="center" valign="middle"><span style="font-family: Arial, Verdana, Helvetica, sans-serif; font-style:normal; font-variant: normal; font-size: 12px; font-weight: bold; color: #000000;"><fmt:message key="CommonLabels.sending"/></span></td>
									</tr>
									<tr>
										<td width="5%" height="20%"></td>
										<td width="90%" height="20%" style="background-image:url('<%=request.getContextPath()%>/images/escritorio/progressmeter-busy.gif');" align="center" valign="middle"><img src="<c:url value='/images/escritorio/spacer.gif'/>" height="1"></td>
										<td width="5%" height="20%"></td>
									</tr>
									<tr>
										<td colspan="3" height="10%"></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
								
				<td width="1px" height="120px">
					<table width="100%" height="120px" cellpadding="0px" cellspacing="0px">
						<tr>
							<td height="1px" bgcolor="#B0BDCD"></td>
						</tr>
						<tr>
							<td height="119px" bgcolor="#393939"></td>
						</tr>
					</table>
				</td>
			</tr>
			
		
			  <tr>
				<td colspan="2" height="1px">
					<table cellpadding="0px" cellspacing="0px">
						<tr>
							<td width="1px" height="1px" bgcolor="#B0BDCD"></td>
							<td width="349px" height="1px" bgcolor="#393939"></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
      </td>
   </tr>
</table>
</div>
<!-- Fin div para la barra de progreso -->

<%--
    </body>
</html>
 --%>
