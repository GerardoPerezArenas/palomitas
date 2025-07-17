<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title>Buzón de Entrada</title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

    <%
        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        int idioma=2;
        int apl=4;

        if(session.getAttribute("usuario") != null){
            usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
            idioma = usuarioVO.getIdioma();
            apl = usuarioVO.getAppCod();
        }
    %>

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
        type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor" property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor" property="apl_cod" value="<%= apl %>" />

    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
    <script language="JavaScript1.2">

        var cont = 0;
        var lista = new Array();
        var lista_exp = new Array();
        var lista_res = new Array();
        var lista_ter = new Array();

        <logic:iterate id="elemento" name="BuzonEntradaSGEForm" property="iniciados">
            var expediente = '<bean:write name="elemento" property="codProcedimiento"/>'+'.'+'<bean:write name="elemento" property="ejercicio"/>'+'.'+'<bean:write name="elemento" property="numero"/>';
            if('..' == expediente) expediente = '';
            lista[cont] = [	'<bean:write name="elemento" property="registro" />', '<bean:write name="elemento" property="solicitante"/>',
                            expediente, '<bean:write name="elemento" property="nomProcedimiento" />'];
            lista_exp[cont] = [	'<bean:write name="elemento" property="codProcedimiento"/>', '<bean:write name="elemento" property="ejercicio"/>',
                                '<bean:write name="elemento" property="numero"/>', '<bean:write name="elemento" property="codMunicipio"/>',
                                '<bean:write name="elemento" property="fechaInicio"/>'];
            lista_res[cont] = [	'<bean:write name="elemento" property="departamentoRes"/>', '<bean:write name="elemento" property="unidadOrgRes"/>',
                                '<bean:write name="elemento" property="ejercicioRes"/>', '<bean:write name="elemento" property="numeroRes"/>',
                                '<bean:write name="elemento" property="tipoRes"/>'];
            lista_ter[cont] = ['<bean:write name="elemento" property="tercero" />', '<bean:write name="elemento" property="version"/>', '<bean:write name="elemento" property="idDomicilio"/>'];
            cont = cont + 1;
        </logic:iterate>

        var chequeadosIniciados = new Array();
        var chequeadosNoIniciados_res = new Array();

        function pulsarAceptar(){
            seleccionaChequeados();
            if(chequeadosIniciados.length == 0){
                var listaDepartamentoRes = "";
                var listaUnidadOrgRes = "";
                var listaEjercicioRes = "";
                var listaNumeroRes = "";
                var listaTipoRes = "";
                if(chequeadosNoIniciados_res.length != 0){
                    for(i=0; i<chequeadosNoIniciados_res.length; i++){
                        listaDepartamentoRes = listaDepartamentoRes + chequeadosNoIniciados_res[i][0]+'§¥';
                        listaUnidadOrgRes = listaUnidadOrgRes + chequeadosNoIniciados_res[i][1]+'§¥';
                        listaEjercicioRes = listaEjercicioRes + chequeadosNoIniciados_res[i][2]+'§¥';
                        listaNumeroRes = listaNumeroRes + chequeadosNoIniciados_res[i][3]+'§¥';
                        listaTipoRes = listaTipoRes + chequeadosNoIniciados_res[i][4]+'§¥';
                    }
                }
                document.forms[0].departamentoRes.value = listaDepartamentoRes;
                document.forms[0].unidadOrgRes.value = listaUnidadOrgRes;
                document.forms[0].ejercicioRes.value = listaEjercicioRes;
                document.forms[0].numeroRes.value = listaNumeroRes;
                document.forms[0].tipoRes.value = listaTipoRes;

                document.forms[0].opcion.value = "aceptar";
                document.forms[0].target="oculto";
                document.forms[0].action="<c:url value='/sge/BuzonEntrada.do'/>";
                document.forms[0].submit();
            }else{
                jsp_alerta('A','<%=descriptor.getDescripcion("msjAceptNoIni")%>');
            }
        }

        function pulsarRechazar(){
            seleccionaChequeados();
            if(chequeadosIniciados.length == 0){
                var listaDepartamentoRes = "";
                var listaUnidadOrgRes = "";
                var listaEjercicioRes = "";
                var listaNumeroRes = "";
                var listaTipoRes = "";
                if(chequeadosNoIniciados_res.length != 0){
                    for(i=0; i<chequeadosNoIniciados_res.length; i++){
                        listaDepartamentoRes = listaDepartamentoRes + chequeadosNoIniciados_res[i][0]+'§¥';
                        listaUnidadOrgRes = listaUnidadOrgRes + chequeadosNoIniciados_res[i][1]+'§¥';
                        listaEjercicioRes = listaEjercicioRes + chequeadosNoIniciados_res[i][2]+'§¥';
                        listaNumeroRes = listaNumeroRes + chequeadosNoIniciados_res[i][3]+'§¥';
                        listaTipoRes = listaTipoRes + chequeadosNoIniciados_res[i][4]+'§¥';
                    }
                }
                document.forms[0].departamentoRes.value = listaDepartamentoRes;
                document.forms[0].unidadOrgRes.value = listaUnidadOrgRes;
                document.forms[0].ejercicioRes.value = listaEjercicioRes;
                document.forms[0].numeroRes.value = listaNumeroRes;
                document.forms[0].tipoRes.value = listaTipoRes;

                document.forms[0].opcion.value = "rechazar";
                document.forms[0].target="oculto";
                document.forms[0].action="<c:url value='/sge/BuzonEntrada.do'/>";
                document.forms[0].submit();
            }else{
                jsp_alerta('A','<%=descriptor.getDescripcion("msjRechNoIni")%>');
            }
        }

        function pulsarSalir(){
            document.forms[0].opcion.value = 'salir';
            document.forms[0].target="mainFrame";
            document.forms[0].action="<c:url value='/sge/BuzonEntrada.do'/>";
            document.forms[0].submit();
        }

        function pulsarIniciar(){
            if(numChequeados() == 1) callFromTableTo(ultimoIndice());
            else jsp_alerta('A','Solo se puede inciar un Registro');
        }

        function numChequeados(){
            var chequeados = 0;
            for(i=0;i<lista.length;i++){
                if (tab.multiple){
                    if(tab.selectedIndex[i]==true) chequeados++;
                }
            }
            return chequeados;
        }

        function ultimoIndice(){
            var indice = -1;
            for(i=0;i<lista.length;i++){
                if (tab.multiple){
                    if(tab.selectedIndex[i]==true) indice = i;
                }
            }
            return indice;
        }

        function seleccionaChequeados(){
            chequeadosIniciados = new Array();
            chequeadosNoIniciados_res = new Array();
            var j_NoIni = 0;
            var j_Ini = 0;
            for(i=0;i<lista.length;i++){
                if (tab.multiple){
                    if(tab.selectedIndex[i]==true){
                        var auxLista = lista[i][2] + lista[i][3];
                        if(auxLista.length == 0){
                            chequeadosNoIniciados_res[j_NoIni] = lista_res[i];
                            j_NoIni++;
                        }else{
                            chequeadosIniciados[j_Ini] = lista[i];
                            j_Ini++;
                        }
                    }
                }
            }
        }

    </script>
 </head>

<body class="bandaBody" onload="javascript:{cargaTabla(lista, lista_exp, lista_res, lista_ter);}">
<form method="post">
<input type="hidden" name="registro" id="registro"/>
<input type="hidden" name="ejercicio" id="ejercicio"/>
<input type="hidden" name="numero" id="numero"/>
<input type="hidden" name="nomProcedimiento" id="nomProcedimiento"/>
<input type="hidden" name="domicilio" id="domicilio"/>
<input type="hidden" name="fechaInicio" id="fechaInicio"/>
<input type="hidden" name="opcion" id="opcion"/>

<input type="hidden" name="codMunicipio" id="codMunicipio"/>
<input type="hidden" name="codProcedimiento" id="codProcedimiento"/>
<input type="hidden" name="tercero" id="tercero"/>
<input type="hidden" name="version" id="version"/>

<input type="hidden" name="departamentoRes" id="departamentoRes"/>
<input type="hidden" name="unidadOrgRes" id="unidadOrgRes"/>
<input type="hidden" name="ejercicioRes" id="ejercicioRes"/>
<input type="hidden" name="numeroRes" id="numeroRes"/>
<input type="hidden" name="tipoRes" id="tipoRes"/>
<center>
	<table id ="tabla1" class="tablaP" width="730px" cellspacing="0" cellpadding="0">
	<tr>
		<td>
    		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  			<tr>
      			<td>&nbsp;</td>
    		</tr>
    		<tr>
      			<td align="center" class="titulo">Buz&oacute;n de Entrada</td>
    		</tr>
    		<tr>
				<td>
					<table width="100%" cellspacing="7px" cellpadding="1px" style="border-top: #7B9EC0 1px solid; border-bottom: #7B9EC0 1px solid;border-left: #7B9EC0 1px solid;border-right: #7B9EC0 1px solid;">
    				<tr>
        				<td>
							<table width="100%" rules="cols" bordercolor="#7B9EC0" border="1" cellspacing="0" cellpadding="2" class="fondoCab">
							<tr>
								<td width="20%" bgcolor="#7B9EC0" class="txtverdebold"><div align="center">Nº Rex. Entrada</div></td>
								<td width="25%" bgcolor="#7B9EC0" class="txtverdebold"><div align="center">Solicitante</div></td>
								<td width="20%" bgcolor="#7B9EC0" class="txtverdebold"><div align="center">Nº Expediente</div></td>
								<td width="35%" bgcolor="#7B9EC0" class="txtverdebold"><div align="center">Procedimiento</div></td>								
							</tr>
							<tr>
								<td colspan="4">
									<div id="tabla" style="background-color:white; HEIGHT:250; WIDTH: 705; overflow-y: auto; overflow-x: hidden; visibility: visible; BORDER: 0px"></div>
								</td>
							</tr>
							</table>
    					</td>
  					</tr>
				</table>
			</td>
		</tr>
		</table>
	</tr>
	<tr>
    	<td width="100%" class="columnP" style="height:15"></td>
	</tr>
	<tr>
		<td width="100%">
			<table width="100%">
			<tr>
				<td width="40%" align="center" valign="middle">
					<div id="capaBotones" name="capaBotones" style="width:100%; visibility:hidden;">
						<table class="tablaBotones" width="100%">
						<tr>
							<td width="50%" align="center">
								<input type="button"  class="boton" value=<%=descriptor.getDescripcion("gbAceptar")%> name="cmdAceptar" onclick="pulsarAceptar();"/>
							</td>
							<td width="50%" align="center">
								<input type="button"  class="boton" value=<%=descriptor.getDescripcion("gbRechazar")%> name="cmdRechazar" onclick="pulsarRechazar();"/>
							</td>							
						</tr>
						</table>
					</div>
				</td>
				<td width="40%" align="center" valign="middle">
					<table class="tablaBotones2" width="100%">
					<tr>
						<td width="50%" align="center">
							<input type="button"  class="boton" value=<%=descriptor.getDescripcion("gbIniciar")%> name="cmdIniciar" onclick="pulsarIniciar();"/>					     		
						</td>
						<td width="50%" align="center">
							<input type="button"  class="boton" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir" onclick="pulsarSalir();"/>					     		
						</td>							
					</tr>
					</table>
				</td>
			</tr>
			</table>
		</td>
	</tr>
	</table>
</center>

</form>

<script language="JavaScript1.2">
	var tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));    

	tab.addColumna('112');
	tab.addColumna('193');
	tab.addColumna('122');
	tab.addColumna('247');	
	tab.multiple = true;		
  
  	function cargaTabla(list, list_exp, list_res, list_ter){
    	lista = list;
		lista_exp = list_exp;
		lista_res = list_res;
		lista_ter = list_ter;
			
		tab.lineas=list;
      	refresh();		
  	}
  
	function refresh(){   
		tab.displayTabla();
	}
	
	function callFromTableTo(rowID,tableName){
		document.forms[0].registro.value = lista[rowID][0];
    	document.forms[0].codProcedimiento.value = lista_exp[rowID][0];
    	document.forms[0].ejercicio.value = lista_exp[rowID][1];
    	document.forms[0].numero.value = lista_exp[rowID][2];
		document.forms[0].domicilio.value = lista_ter[rowID][2];
		document.forms[0].fechaInicio.value = lista_exp[rowID][4];		
		
		document.forms[0].codMunicipio.value = lista_exp[rowID][3];
		document.forms[0].nomProcedimiento.value = lista[rowID][3];
		document.forms[0].tercero.value = lista_ter[rowID][0];
		document.forms[0].version.value = lista_ter[rowID][1];
		
		document.forms[0].departamentoRes.value = lista_res[rowID][0];
		document.forms[0].unidadOrgRes.value = lista_res[rowID][1];
		document.forms[0].ejercicioRes.value = lista_res[rowID][2];
		document.forms[0].numeroRes.value = lista_res[rowID][3];
		document.forms[0].tipoRes.value = lista_res[rowID][4];
		
		document.forms[0].opcion.value = 'cargar';
		document.forms[0].target="mainFrame";
       	document.forms[0].action="<c:url value='/sge/InicioExpediente.do'/>";
       	document.forms[0].submit();
	}		
	
	document.onkeydown=checkKeys;
	function checkKeysLocal(evento,tecla){
     var teclaAuxiliar = "";
        if(window.event){
            evento = window.event;
            teclaAuxiliar = evento.keyCode;
        }else
            teclaAuxiliar = evento.which;

		keyDel(evento);
    	if ('Alt+C' == tecla) pulsarCancelar();
		if ('Alt+R' == tecla) pulsarRechazar();
		if ('Alt+A' == tecla) pulsarAceptar();
   		if ((teclaAuxiliar == 38)||(teclaAuxiliar == 40)) upDownTable(tab,lista,teclaAuxiliar);
		if ((teclaAuxiliar == 13)||(teclaAuxiliar == 32)) pushEnterTable(tab,lista);
	}
	
	function rellenarDatosMultiple(tableName,rowID){		
		seleccionaChequeados();	
		if(chequeadosNoIniciados_res.length != 0){
			document.all.capaBotones.style.visibility = "visible";			
		}else{
			document.all.capaBotones.style.visibility = "hidden";
		}
	}	
		
</script>

</body>
</html:html>
