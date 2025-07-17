<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<html>
<head>
    <title>Comparar Datos de Terceros</title>
    <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
    <%
        UsuarioValueObject usuarioVO;
        int idioma=1;
        int apl=1;
        String css="";
        if (session.getAttribute("usuario") != null){
            usuarioVO =	(UsuarioValueObject)session.getAttribute("usuario");
            idioma = usuarioVO.getIdioma();
            apl = usuarioVO.getAppCod();
            css=usuarioVO.getCss();
        }
    %>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>">    
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
                 type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"	property="idi_cod" value= "<%=idioma%>" />
    <jsp:setProperty name="descriptor"	property="apl_cod" value="<%=apl%>"	/>

    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
    <script type="text/javascript">

        var tercerosReps = new Array();

        function comprobarOpciones() {
            <c:choose>
                <c:when test="${not empty requestScope.opcion}">
                    <c:if test="${requestScope.opcion eq 'forzarGrabarTercero'}">
                        recibirCodigoTercero();
                    </c:if>
                    <c:if test="${requestScope.opcion eq 'updateTercAntiguo'}">
                        recibirCodigoTercero();
                    </c:if>
                </c:when>
                <c:otherwise>
                     inicializar();
                </c:otherwise>
            </c:choose>
        }

        function inicializar() {
            var argVentana = self.parent.opener.xanelaAuxiliarArgs;
            document.forms[0].codTipoDoc.value = argVentana[2];
            document.forms[0].descTipoDoc.value = argVentana[3];
            document.forms[0].txtDNI.value = argVentana[4];
            document.forms[0].txtInteresado.value = argVentana[5];
            var apellido1Completo = '';
            if (argVentana[8] != '') apellido1Completo = argVentana[8] + ' ';
            apellido1Completo += argVentana[6];
            document.forms[0].txtApell1.value = apellido1Completo;
            var apellido2Completo = '';
            if (argVentana[9] != '') apellido1Completo = argVentana[9] + ' ';
            apellido2Completo += argVentana[7];
            document.forms[0].txtApell2.value = apellido2Completo;
            document.forms[0].txtTelefono.value = argVentana[10];
            document.forms[0].txtCorreo.value = argVentana[11];

            var indexReps = 0;
            for (var j = 12; j < argVentana.length; j = j + 12) {
                var unTerceroRep = new Array();
                unTerceroRep[0] = argVentana[j];
                unTerceroRep[1] = argVentana[j + 1];
                unTerceroRep[2] = argVentana[j + 2];
                unTerceroRep[3] = argVentana[j + 3];
                unTerceroRep[4] = argVentana[j + 4];
                unTerceroRep[5] = argVentana[j + 5];
                unTerceroRep[6] = argVentana[j + 6];
                unTerceroRep[7] = argVentana[j + 7];
                unTerceroRep[8] = argVentana[j + 8];
                unTerceroRep[9] = argVentana[j + 9];
                unTerceroRep[10] = argVentana[j + 10];
                unTerceroRep[11] = argVentana[j + 11];
                tercerosReps[indexReps++] = unTerceroRep;
            }

            document.forms[0].totalTercRep.value = tercerosReps.length;
            setTerceroMostrado(0);

        }

        function setTerceroMostrado(indice) {
            document.forms[0].cmdAnterior.disabled = indice == 0;
            document.forms[0].cmdSiguiente.disabled = indice == document.forms[0].totalTercRep.value - 1;

            pintarTerceroRep(tercerosReps[indice]);
            document.forms[0].tercRepSelected.value = indice + 1;
        }

        function pintarTerceroRep(terceroRep) {
            document.forms[0].oldIdentificador.value = terceroRep[0];
            document.forms[0].oldNumVersion.value = terceroRep[1];
            document.forms[0].codOldTipoDoc.value = terceroRep[2];
            document.forms[0].descOldTipoDoc.value = terceroRep[3];
            document.forms[0].txtOldDNI.value = terceroRep[4];
            document.forms[0].txtOldInteresado.value = terceroRep[5];
            var apellido1Completo = '';
            if (terceroRep[8] != '') apellido1Completo = terceroRep[8] + ' ';
            apellido1Completo += terceroRep[6];
            document.forms[0].txtOldApell1.value = apellido1Completo;
            var apellido2Completo = '';
            if (terceroRep[9] != '') apellido1Completo = terceroRep[9] + ' ';
            apellido2Completo += terceroRep[7];
            document.forms[0].txtOldApell2.value = apellido2Completo;
            document.forms[0].txtOldTelefono.value = terceroRep[10];
            document.forms[0].txtOldCorreo.value = terceroRep[11];
        }

        function pulsarCrearNuevo() {
            document.forms[0].opcion.value = "forzarGrabarTercero";
            document.forms[0].target = "oculto";
            document.forms[0].action = "<%=request.getContextPath()%>/BusquedaTerceros.do";
            document.forms[0].submit();
        }

        function recibirCodigoTercero() {
            var terceroSelected = new Array();
            terceroSelected[0] = "<c:out value="${requestScope.tercero.identificador}"/>";
            terceroSelected[1] = "<c:out value="${requestScope.tercero.version}"/>";
            terceroSelected[2] = "<c:out value="${requestScope.tercero.tipoDocumento}"/>";
            terceroSelected[3] = "<c:out value="${requestScope.tercero.tipoDocDesc}"/>";
            terceroSelected[4] = "<c:out value="${requestScope.tercero.documento}"/>";
            terceroSelected[5] = "<c:out value="${requestScope.tercero.nombre}"/>";
            terceroSelected[6] = "<c:out value="${requestScope.tercero.apellido1}"/>";
            terceroSelected[7] = "<c:out value="${requestScope.tercero.apellido2}"/>";
            terceroSelected[8] = '';
            terceroSelected[9] = '';
            terceroSelected[10] = "<c:out value="${requestScope.tercero.telefono}"/>";
            terceroSelected[11] = "<c:out value="${requestScope.tercero.email}"/>";
            terceroSelected[12] = 'true';

            self.parent.opener.retornoXanelaAuxiliar(terceroSelected);
        }

        function usarTerceroViejo() {
            var terceroSelected = new Array();
            terceroSelected[0] = document.forms[0].oldIdentificador.value;
            terceroSelected[1] = document.forms[0].oldNumVersion.value;
            terceroSelected[2] = document.forms[0].codOldTipoDoc.value;
            terceroSelected[3] = document.forms[0].descOldTipoDoc.value;
            terceroSelected[4] = document.forms[0].txtOldDNI.value;
            terceroSelected[5] = document.forms[0].txtOldInteresado.value;
            terceroSelected[6] = document.forms[0].txtOldApell1.value;
            terceroSelected[7] = document.forms[0].txtOldApell2.value;
            terceroSelected[8] = '';
            terceroSelected[9] = '';
            terceroSelected[10] = document.forms[0].txtOldTelefono.value;
            terceroSelected[11] = document.forms[0].txtOldCorreo.value;
            terceroSelected[12] = 'false';

            self.parent.opener.retornoXanelaAuxiliar(terceroSelected);
        }

        function pulsarSiguiente() {
            var indice = document.forms[0].tercRepSelected.value - 1;
            setTerceroMostrado(indice + 1);
        }

        function pulsarAnterior() {
            setTerceroMostrado(document.forms[0].tercRepSelected.value - 2);
        }

        function actualizarTerceroAntiguo() {
            document.forms[0].opcion.value = "updateTercAntiguo";
            document.forms[0].target = "oculto";
            document.forms[0].action = "<%=request.getContextPath()%>/BusquedaTerceros.do";
            document.forms[0].submit();
        }

    </script>
    
</head>

<body class="bandaBody" onload="javascript:comprobarOpciones();">
<form>
    <input type="hidden" name="opcion"/>
    <input type="hidden" name="oldIdentificador"/>
    <input type="hidden" name="oldNumVersion"/>

    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("etiqCompararTerceros")%></div>
    <div class="contenidoPantalla">
        <div class="sub3titulo"><%=descriptor.getDescripcion("etiqDataTerceroAntiguo")%></div>
        <table width="100%">
            <TR>
                <TD>
                    <table>
                        <tr>
                            <td style="width: 22%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqTiDoc")%>:</td>
                            <td style="width: 33%" class="columnP">
                                <input id="codOldTipoDoc" name="codOldTipoDoc" type="text" class="inputTexto" size="5" readonly>
                                <input id="descOldTipoDoc" name="descOldTipoDoc" type="text" class="inputTexto" style="width:182px" readonly>
                            </td>
                            <td style="width: 12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqDocumento")%>:</td>
                            <td style="width: 33%" class="columnP">
                                <input type="text" class="inputTexto" size=38 maxlength=16 name="txtOldDNI" readonly>
                            </td>
                        </tr>
                    </table>
                    <table>
                        <tr>
                            <td style="width: 22%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqNombreRazon")%>:</td>
                            <td style="width: 78%">
                                <input type="text" class="inputTexto" style="width:567px" maxlength=80 name="txtOldInteresado" readonly>
                            </td>
                        </tr>
                    </table>
                    <table>
                        <tr>
                            <td style="width: 22%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqApellido1Part")%>:</td>
                            <td style="width: 33%" class="columnP">
                                <input type="text" name="txtOldApell1" class="inputTexto" size=36 maxlength=25 readonly>
                            </td>
                            <td style="width: 12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqApellido2Part")%>:</td>
                            <td style="width: 33%" class="columnP">
                                <input name="txtOldApell2" type="text" class="inputTexto" size=38 maxlength=25 readonly>
                            </td>																		
                        </tr>
                    </table>
                    <table>
                        <tr>
                            <td style="width: 22%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqTelfFax")%>:</td>
                            <td style="width: 33%" class="etiqueta">
                                <input id="oldTelefono" type="text" name="txtOldTelefono" class="inputTexto" size=36 maxlength=40 readonly>
                            </td>
                            <td style="width: 12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqEmail")%>:</td>
                            <td style="width: 33%" class="etiqueta">
                                <input id="oldEmail" type="text" name="txtOldCorreo" class="inputTexto" size=38 maxlength=50 readonly>
                            </td>
                        </tr>
                    </table>
                    <table>
                        <tr align="center">
                            <TD style="width: 45%" align="right">
                                <input type= "button" class="botonGeneral" value="<<" name="cmdAnterior" onClick= "pulsarAnterior();return false;" style="width:25px;height:22px">
                            </TD>
                            <td style="width: 10%" align="center">
                                <input type="text" class="etiqueta" name="tercRepSelected" style="border:0" size="2"/>/<input type="text" class="etiqueta" name="totalTercRep" style="border:0" size="2"/>
                            </td>
                            <TD style="width: 45%" align="left">
                                <input type= "button" class="botonGeneral" value=">>" name="cmdSiguiente" onClick= "pulsarSiguiente();return false;" style="width:25px;height:22px">
                            </TD>

                        </tr>
                    </table>                                                  
                </td>
            </tr>
        </table>
        <div class="sub3titulo"><%=descriptor.getDescripcion("etiqDataTerceroNuevo")%></div>
        <table width="100%">
            <TR>
                <TD>
                    <table width="100%">
                        <tr>
                            <td style="width: 22%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqTiDoc")%>:</td>
                            <td style="width: 33%" class="columnP">
                                <input id="codTipoDoc" name="codTipoDoc" type="text" class="inputTexto" size="5" readonly>
                                <input id="descTipoDoc" name="descTipoDoc" type="text" class="inputTexto" style="width:182px" readonly>
                            </td>
                            <td style="width: 12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqDocumento")%>:</td>
                            <td style="width: 33%" class="columnP">
                                <input type="text" class="inputTexto" size=38 maxlength=16 name="txtDNI" readonly>
                            </td>
                        </tr>
                    </table>
                    <table>   
                        <tr>
                            <td style="width: 22%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqNombreRazon")%>:</td>
                            <td style="width: 78%">
                                <input type="text" class="inputTexto" style="width:567px" maxlength=80 name="txtInteresado" readonly>
                            </td>
                        </tr>
                    </table>
                    <table>
                        <tr>
                            <td style="width: 22%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqApellido1Part")%>:</td>
                            <td style="width: 33%" class="columnP">
                                <input type="text" name="txtApell1" class="inputTexto" size=36 maxlength=25 readonly>
                            </td>
                            <td style="width: 12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqApellido2Part")%>:</td>
                            <td style="width: 33%" class="columnP">
                                <input name="txtApell2" type="text" class="inputTexto" size=38 maxlength=25 readonly>
                            </td>
                        </tr>
                    </table>
                    <table>
                        <tr>
                            <td style="width: 22%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqTelfFax")%>:</td>
                            <td style="width: 33%" class="etiqueta">
                                <input id="telefono" type="text" name="txtTelefono" class="inputTexto" size=36 maxlength=40 readonly>
                            </td>
                            <td style="width: 12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqEmail")%>:</td>
                            <td style="width: 33%" class="etiqueta">
                                <input id="email" type="text" name="txtCorreo" class="inputTexto"	size=38 maxlength=50 readonly>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input type= "button" title='<%=descriptor.getDescripcion("altCrearNuevo")%>' class="botonGeneral" name="cmdSalir" onClick="pulsarCrearNuevo();return false;" value="<%=descriptor.getDescripcion("botCrearNuevo")%>" >
            <input type="button" title='<%=descriptor.getDescripcion("altActualizar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("botActualizar")%>' name="cmdLimpiar" onClick="actualizarTerceroAntiguo();return false;">
            <input type="button" title='<%=descriptor.getDescripcion("altUsarViejo")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("botUsarViejo")%>' name="cmdLimpiar" onClick="usarTerceroViejo();return false;">
        </div>
    </div>
</form>
</body>

</html>
