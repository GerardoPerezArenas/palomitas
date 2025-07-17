<!-- jsp para manejar el buzón de registro -->
<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="java.util.Vector" %>
<%@page import="es.altia.agora.interfaces.user.web.registro.BuzonForm" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<head>

	<title>BUZÓN DE REGISTRO</title>
    <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<%
	UsuarioValueObject usuarioVO = new UsuarioValueObject();
	int idioma=1;
	int apl=1;
	int cod_org=1;
	int cod_dep=1;
	int cod_ent= 1;
	int cod_unidOrg=1;
	String desc_org="";
	String desc_ent="";

	if (session.getAttribute("usuario") != null){
		usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
		idioma = usuarioVO.getIdioma();
		apl = usuarioVO.getAppCod();
		cod_org= usuarioVO.getOrgCod();
		cod_dep= usuarioVO.getDepCod();
		cod_ent = usuarioVO.getEntCod();
		cod_unidOrg = usuarioVO.getUnidadOrgCod();
		desc_org = usuarioVO.getOrg();
		desc_ent = usuarioVO.getEnt();
	}
%>

	<!-- Ficheros JavaScript -->
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
	
	<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
	<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
	<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
	<jsp:useBean id="usuario" scope="session" class="es.altia.agora.business.escritorio.UsuarioValueObject" />
<%
	String e1=descriptor.getDescripcion("gEtiqDocumento");
	e1=e1.trim();
	String e2=descriptor.getDescripcion("gEtiqFecha");
	e2=e2.trim();
	String e3=descriptor.getDescripcion("gEtiqTipo");
	e3=e3.trim();
	String e4=descriptor.getDescripcion("res_asunto");
	e4=e4.trim();
	String e5=descriptor.getDescripcion("res_etiqOrigen");
	e5=e5.trim();
%>

	<meta http-equiv="" content="text/html; charset=iso-8859-1">

	<!-- Estilos -->
	<link href="<%=request.getContextPath()%>/css/estilo.css" rel="stylesheet" type="text/css">
	
</head>

	<body scroll=no onload="javascript:{cargaTabla();}">
	

	<table width="720" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
    <td align="center" class="titulo"><%=descriptor.getDescripcion("gbBuzon")%></td>
  </tr>
		<tr>
			<td>
				<table width="720" rules="cols" bordercolor="#7B9EC0" border="1" cellspacing="0" cellpadding="0" class="fondoCab">
				<tr height="15">
					<td width="16%" bgcolor="#7B9EC0" class="txtverdebold" ><%=e1%></td>
						<td width="10%" bgcolor="#7B9EC0" class="txtverdebold" ><%=e2%></td>
						<td width="6%" bgcolor="#7B9EC0" class="txtverdebold" ><%=e3%></td>
					<td width="252" bgcolor="#7B9EC0" class="txtverdebold" ><%=e4%></td>
					<td width="252" bgcolor="#7B9EC0" class="txtverdebold" ><%=e5%></td>
				</tr>
                                                                        <tr>
                                                                            <td colspan="5" id="tabla"></td>
				</tr>
				</table>
			</td>
		</tr>
	</table>
	<form action="/Buzon.do">
  		<div align="center">
    	<input type="button" onClick="enviar()" class="boton" value=<%=descriptor.getDescripcion("gEtiqAnotacion")%> align="middle">
  		</div>
		<input type="hidden" name="acceso" value="procesar" />
  		<input type="hidden" name="eje"	/>
		<input type="hidden" name="num"	/>
		<input type="hidden" name="tip"	/>
		<input type="hidden" name="orgcod" />
		<input type="hidden" name="orgdes" />
		<input type="hidden" name="entcod" />
		<input type="hidden" name="entnom" />
		<input type="hidden" name="depcod" />
		<input type="hidden" name="depnom" />
		<input type="hidden" name="uorcod" />
		<input type="hidden" name="uornom" />
	</form>
	<script language="JavaScript1.2" class="txtverdeboldgrangran">
		var tab;
		var lista = new Array();
		var oculta = new Array();
		var cont = 0;
		var eje;
		var num;
		var fec;
		var tip;
		var asu;
		var orgcod;
		var orgdes;
		var entcod;
		var entnom;
		var depcod;
		var depnom;
		var uorcod;
		var uornom;
		var orgusu = '<bean:write name="usuario" property="orgCod" />';
		var entusu = '<bean:write name="usuario" property="entCod" />';
		var depusu = '<bean:write name="usuario" property="depCod" />';		
		var uorusu = '<bean:write name="usuario" property="unidadOrgCod" />';
		
                                    tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'),150);
		tab.addColumna('108','center');
		tab.addColumna('72','center');
		tab.addColumna('36','center');
		tab.addColumna('252','center');
		tab.addColumna('252','center');
		
		function cargaTabla(){
			<logic:iterate id="b" name="BuzonForm" property="buzon">
				eje = '<bean:write name="b" property="eje"/>';
				num = '<bean:write name="b" property="num"/>';
				fec = '<bean:write name="b" property="fec"/>';
				tip = '<bean:write name="b" property="tip"/>';
				asu = '<bean:write name="b" property="asu"/>';
				orgcod = '<bean:write name="b" property="orgcod"/>';
				orgdes = '<bean:write name="b" property="orgdes"/>';
				entcod = '<bean:write name="b" property="entcod"/>';
				entnom = '<bean:write name="b" property="entnom"/>';
				depcod = '<bean:write name="b" property="depcod"/>';
				depnom = '<bean:write name="b" property="depnom"/>';
				uorcod = '<bean:write name="b" property="uorcod"/>';
				uornom = '<bean:write name="b" property="uornom"/>';
				oculta[cont] = [eje,num,tip,orgcod,orgdes,entcod,entnom,depcod,depnom,uorcod,uornom];
				if (orgcod != orgusu)
					lista[cont] = [eje+'/'+num,fec,tip,asu,orgdes+'/'+uornom];
				else
					lista[cont] = [eje+'/'+num,fec,tip,asu,uornom];
				cont = cont + 1;
			</logic:iterate>
		tab.lineas = lista;
		refresh();
		}

		function refresh(){
			tab.displayTabla();
		}
		
		function enviar(){
			var i = tab.selectedIndex;
			if(i < 0){ 
			  	jsp_alerta("A","Atencion: No ha seleccionado ninguna fila.");
      			return;
   			}
			else{
				if((oculta[i][3] == orgusu) && (oculta[i][5] == entusu)){
					document.forms[0].eje.value = oculta[i][0];
					document.forms[0].num.value = oculta[i][1];
					document.forms[0].tip.value = oculta[i][2];
					document.forms[0].orgcod.value = oculta[i][3];
					document.forms[0].orgdes.value = oculta[i][4];
					document.forms[0].entcod.value = oculta[i][5];
					document.forms[0].entnom.value = oculta[i][6];
					document.forms[0].depcod.value = oculta[i][7];
					document.forms[0].depnom.value = oculta[i][8];
					document.forms[0].uorcod.value = oculta[i][9];
					document.forms[0].uornom.value = oculta[i][10];
				}
			}
			document.forms[0].target = "oculto";
			document.forms[0].submit();
			window.close();
		}
		
	</script>
</body>
</html>
