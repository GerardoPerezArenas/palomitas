<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        
        <title>Mantenimiento de roles de asuntos</title>
        
        <!-- Estilos -->
        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css" media="screen" >
        
        <%
            int idioma = 1;
            int apl = 1;
            int munic = 0;
            if (session != null) {
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    apl = usuario.getAppCod();
                    munic = usuario.getOrgCod();
                }
            }
        %>
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
        
        <script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
        <script type="text/javascript">
            
            function inicializar() {
                var argVentana = self.parent.opener.xanelaAuxiliarArgs;
                document.forms[0].nombreRol.focus();
            }
            
            function pulsarSalir() {
                self.parent.opener.retornoXanelaAuxiliar('');
            }
            
            function pulsarAceptar () {
                var nombreRol = document.forms[0].nombreRol.value;
                if(nombreRol != '') {    
                    self.parent.opener.retornoXanelaAuxiliar(nombreRol);
                } else {
                jsp_alerta('A','<%=descriptor.getDescripcion("msjObligTodos")%>');
                }        
            }
            
        </script>
        
    </head>
    
<body class="bandaBody" onload="javascript:{ 
    inicializar()}">
<form>
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gbAlta")%></div>
    <div class="contenidoPantalla">
        <table style="width:100%">
            <TR>
                <TD>
                    <TABLE id ="tablaDatosGral"  border="0px" width="885px" height="150px" cellspacing="0px" cellpadding="0px" border="0px">
                        <tr>
                            <td width="10%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Nombre")%>:</td>
                            <td width="90%" class="columnP">
                                <input type="text" class="inputTexto" name="nombreRol" size="100" maxlength="255"
                                       onblur="javascript:xAMayusculas(this);">
                            </td>
                        </tr>
                    </table>
                </TD>
            </TR>
        </TABLE>
   
    <div class="botoneraPrincipal">
        <INPUT type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbAceptar")%>' name="cmdAceptar"  onClick="pulsarAceptar();">                        
        <INPUT type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbSalir")%>' name="cmdSalir"  onClick="pulsarSalir();">                             
    </div>         
</div>         
</form>
<script language="JavaScript1.2">
            
            document.onmouseup = checkKeys;
            
            function checkKeysLocal(evento,tecla) {
                var teclaAuxiliar;
                if(window.event){
                    evento = window.event;
                    teclaAuxiliar = evento.keyCode;
                }else
                    teclaAuxiliar = evento.which;

                // Controlamos la pulsacion de 'Enter'
                //if (event.keyCode == 13) pulsarAceptar();
                if (teclaAuxiliar == 13) pulsarAceptar();
                keyDel(evento);
            }
            
            
        </script>
        
    </body>
</html>
