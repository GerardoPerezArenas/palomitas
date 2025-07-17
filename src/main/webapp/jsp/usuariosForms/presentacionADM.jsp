<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.util.CargaMenu" %>

<%
	int aplicacionCod = 0;
   	String aplicacion = "";
   	String usu = "";
   	String entidad = "";
	int idioma = 0;
	int codAplicacion = 0;
	UsuarioValueObject usuarioVO = null;

   	if(session != null)
    	usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");

	if (usuarioVO != null){
       	aplicacion = usuarioVO.getApp().toUpperCase();
		codAplicacion = usuarioVO.getAppCod();
       	usu = usuarioVO.getNombreUsu();
       	entidad = usuarioVO.getEnt();
		idioma = usuarioVO.getIdioma();
	}

%>

<html>

<head>
<jsp:include page="/jsp/usuariosForms/tpls/app-constants.jsp" />
<title>GESTIÓN AYUNTAMIENTOS - Páxina de inicio</title>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%= usuarioVO.getIdioma()%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= usuarioVO.getAppCod()%>" />
<LINK REL="stylesheet" MEDIA="screen" TYPE="text/css" HREF="<%=request.getContextPath()%>/css/estilo.css">

</head>

<!-- onLoad para que los submenus se vean en este frame -->

<body onload="javascript:{ }">


    <table  width="100%" height="100%" cellpadding="0px" cellspacing="0px">
        <tr>
            <td width="791px" height="415px">
                <table width="100%" height="100%" cellpadding="1px" cellspacing="0px" border="0px" bgcolor="#666666">
                    <tr>
                        <td>
                            <table width="100%" height="100%" cellpadding="0px" cellspacing="1px" border="0px" bgcolor="#ffffff">
                                <tr>
                                    <td id="titulo" class="txttitblanco">
                                      &nbsp;&nbsp;&nbsp;<%= descriptor.getDescripcion("gEtiqTitulo")%>
                                    </td>
                                </tr>

                                <tr>
                                    <td width="100%" height="1px" bgcolor="#666666"></td>
                                </tr>
                                <tr>
                                    <td>
                                        <table width="100%" height="100%" border="0" align="center" cellpadding="0px" cellspacing="3px" border="0px" bgcolor="#e6e6e6">
                                            <tr>
                                               <td id="tabla" width="100%" height="50%" align="center" valign="bottom">
                                                    <img height="100px" src='<%=request.getContextPath() + usuarioVO.getOrgIco().substring(0,usuarioVO.getOrgIco().length()-4)%>_grande.gif' border="0">
                                               </td>
                                           </tr>
                                           <tr>
                                             <td width="100%" height="50%" align="center" valign="top">
                                                 <% if(codAplicacion == 9) { %>
                                                   <span class="txttitcabecera"><%=entidad%></span>
                                                 <% } %>
                                              </td>
                                           </tr>
                                       </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>

		    <!-- Sombra lateral. -->
		    <td height="100%" width="1">
			    <table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
				        <tr><td bgcolor="#b0bdcd" height="1"></td></tr>
            			<tr><td bgcolor="#393939" height="100%"></td></tr>
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
                        <td width="791px" height="1px" bgcolor="#393939"></td>
                    </tr>
                </table>
            </td>
        </tr>
       <!-- Fin sombra inferior. -->
    </table>
</body>

</html>