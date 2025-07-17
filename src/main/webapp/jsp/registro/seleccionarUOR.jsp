<%@page contentType="text/html; charset=iso-8859-1" language="java"  %>
<%@page import="java.util.*" %>
<%@page import="es.altia.agora.business.escritorio.UserPreferences" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioEscritorioValueObject" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO" %>
<html>

<head>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<title>Selección de Organizacións e Entidades</title>

<%
	int idioma = 1;
	int apl =1;
	UsuarioEscritorioValueObject usuarioEscritorioVO = null;
	UserPreferences userPreferences = null;
	UsuarioValueObject usuarioVO = new UsuarioValueObject();
	String entidad="";
	
	
	if (session!=null){
		if (session.getAttribute("usuario") != null){
			usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");		
			entidad = usuarioVO.getEnt();
			apl = usuarioVO.getAppCod();
		}
	
		usuarioEscritorioVO = (UsuarioEscritorioValueObject)session.getAttribute("usuarioEscritorio");
		idioma =  usuarioEscritorioVO.getIdiomaEsc(); 
		userPreferences = (UserPreferences) usuarioEscritorioVO.getPreferences();  
	}			        
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" /> 
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<SCRIPT type="text/javascript">

var opcionAnterior = self.parent.opener.xanelaAuxiliarArgs['opcionAnterior'];

function pulsarSeleccionar(opcion, unidadOrgCod, unidadOrg){           
        retorno = [opcion, unidadOrgCod, unidadOrg];
        self.parent.opener.retornoXanelaAuxiliar(retorno);

}

</script>

</head>

<body class="bandaBody">

<form name="formSelReg" id="formSelReg" target="_self">

<div class="txttitblanco">
    <%=descriptor.getDescripcion("titSelReg")%>
</div>
<div class ="contenidoPantalla">
    <table id="tabla1" style="width:100%;height:100%;text-align:center" cellpadding="0px" cellspacing="1px" border="0px" bgcolor="#ffffff">
        <tr>
            <td cellpadding="0px" cellspacing="0px" >          											
                <table border="0" width="100%" height="15px" cellpadding="0px" cellspacing="0px" >          												
                    <tr>
                        <td width="288px" bgcolor="#B0BDCD" align="center" height="15">
                                <font face="Arial" size="2" ><%=descriptor.getDescripcion("etiqUOR")%></font>																	
                        </td>
                    </tr>          			
                </table>
            </td>
        </tr>
        <%
        String colorFondo1 ="#FFFFFF";
        String colorFondo2 ="#e6e6e6";
        Vector aplic = (Vector)session.getAttribute("vecUOR");
        for(int i=0;i<aplic.size();i++) { 
            UORDTO uorTO = (UORDTO)aplic.get(i);
            String unidadOrgCod  = uorTO.getUor_cod();
            String unidadOrg = uorTO.getUor_nom();
        %>

        <tr>
            <td cellpadding="0px" cellspacing="0px" >          											
                <table border="0" width="100%" height="50" cellpadding="0px" cellspacing="0px" >
                    <tr>					  																																														  																									
                        <td width="288px" align="center" height="15" bgColor="<%= (i % 2==0)?colorFondo2:colorFondo1%>"
                             onclick ='pulsarSeleccionar(opcionAnterior,"<%= unidadOrgCod %>","<%=unidadOrg %>");'>
                            <font face="Arial" size="1">
                                <a href="#" class="direccion"><%= unidadOrg %></a>&nbsp;
                            </font>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
                <% 
                        }
                        session.removeAttribute("org_ent");
                %>												
    </table>
</div>		
<!--Datos-->
 </form>
</BODY>
</html>
