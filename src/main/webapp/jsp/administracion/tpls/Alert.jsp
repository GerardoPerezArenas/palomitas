<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<html>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<jsp:include page="/jsp/portafirmas/tpls/app-constants.jsp" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<link rel="stylesheet" href="<c:url value='/css/estilo.css'/>" type="text/css">
<script language="JavaScript" SRC="<c:url value='/scripts/general.js'/>"></script>
<script>
    function aceptarConfirm() {	
        var saida = "1";
        window.returnValue = saida;
        window.close();
     }

     function mostrarTexto(){
        var dialogArg = top.dialogArguments;
        var texto = dialogArg[0];

        document.getElementById("alert_stl").innerHTML = texto;
     }
</script>
<title><c:out value="${param.TituloMSG}"/></title>
</head>
<body class="alertaShowModal" onload="mostrarTexto();">
<center>
        <form action="" method="post" target="_self">
                <table bgcolor="#FFFFFF" width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td bgcolor="#FFFFFF" align="center">
                            <c:choose>
                                <c:when test="${param.TipoMSG eq 'A'}">
                                    <span class="fa fa-exclamation-triangle"></span>
                                </c:when>
                                <c:otherwise>
                                    <span class="fa fa-question"></span>
                                </c:otherwise>
                            </c:choose>
                      </td>
                      <td bgcolor="#FFFFFF" class="txtverdebold" align="left"  id="alert_stl"></td>
                    </tr>

                    <tr>
                      <td bgcolor="#FFFFFF" width="100%" colspan="2" align="center" style="padding-top: 20px">
                            <c:choose>
                                <c:when test="${param.TipoMSG eq 'A'}">
                                   <input type="button" class="botonGeneral" value="Aceptar" onClick="window.close();">
                                </c:when>
                                <c:otherwise>
                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr>
                                            <td bgcolor="#FFFFFF" width="50%" align="center">
                                               <input type="button" class="botonGeneral" value="Confirmar" onClick="aceptarConfirm();">
                                            </td>
                                            <td bgcolor="#FFFFFF" width="50%" align="center">
                                               <input type="button" class="botonGeneral" value="Cancelar" onClick="window.close();">
                                            </td>
                                        </tr>
                                    </table>
                                </c:otherwise>
                            </c:choose>
                      </td>
                    </tr>
                    <tr height="35px">
                      <td bgcolor="#FFFFFF" width="100%" colspan="2" align="center"></td>
                    </tr>
				</table>
			</form>
		</center>
	</body>

</html>
