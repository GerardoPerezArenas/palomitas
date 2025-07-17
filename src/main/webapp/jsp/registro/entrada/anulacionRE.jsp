<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<%
            int idioma = 1;
            int apl = 1;
            String css = "";
            if (session != null) {
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    apl = usuario.getAppCod();
                    css = usuario.getCss();
                }
            }
            String tipoAnotacion = "S";
            if (session.getAttribute("tipoAnotacion") != null) {
                tipoAnotacion = (String) session.getAttribute("tipoAnotacion");
            }
            String titPag;
            String etiqAnot;

            if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion))) {
                titPag = "tit_DiligAnulE";
                etiqAnot = "etiq_DatosEntrada";
            } else {
                titPag = "tit_DiligAnulS";
                etiqAnot = "etiq_DatosSalida";
            }
%>

<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<HTML>

<head>

<TITLE>::: REXISTRO ENTRADA - Anulación Entrada :::</TITLE>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/> 
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />


<!-- Ficheros JavaScript -->
<script src="<%=request.getContextPath()%>/scripts/general.js"></script>

<SCRIPT type="text/javascript">
    
    // --- Funciones de página.
    
    function inicializar() {
        
        //document.all.tabla1.style.marginTop = calcularPosTabla(document.all.tabla1);	
    }
    
    
    function pulsarCancelar() {
        
        modificando('N');
        document.forms[0].numero.value = document.forms[0].numeroAnotacion.value;
        document.forms[0].ano.value=document.forms[0].ejercicioAnotacion.value;
        document.forms[0].opcion.value="cancelar_anular";
        document.forms[0].target="mainFrame";
        document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
        document.forms[0].submit();        
    }
    
    
    function pulsarAnular(){
        if (validarObligatoriosAqui()) {
            modificando('N');
            //document.forms[0].txtDiligenciasAnulacion.value = escape(document.forms[0].txtDiligenciasAnulacionSinEscape.value);
            document.forms[0].opcion.value="anular";	    
            document.forms[0].target="oculto";
            document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
            document.forms[0].submit();
        }
    }
    
    function validarObligatoriosAqui() {
        //document.forms[0].txtDiligenciasAnulacion.value = escape(document.forms[0].txtDiligenciasAnulacionSinEscape.value);
        // Longitud del textArea
        if (document.forms[0].txtDiligenciasAnulacion.value.length <= 0)
            {
                jsp_alerta('A','<%=descriptor.getDescripcion("msjAbrirCond10")%>');
                    document.forms[0].txtDiligenciasAnulacion.focus();
                    return false;
                }
                // Longitud del textArea
                if (document.forms[0].txtDiligenciasAnulacion.value.length > 4000)
                    {
                        jsp_alerta('A','<%=descriptor.getDescripcion("msjDiligAnul")%>');
                            document.forms[0].txtDiligenciasAnulacion.focus();
                            return false;
                        }
                        return true;
                        
                    }
                    
                    function idAnotacion(){
                        document.writeln(' '+ document.forms[0].ejercicioAnotacion.value 
                            + '/' + document.forms[0].numeroAnotacion.value) ;
                    }
                    
                    function anotacionAnulada(mnsj) {
                        
                        jsp_alerta("A", mnsj);
                        
                        document.forms[0].numero.value = document.forms[0].numeroAnotacion.value;
                        document.forms[0].ano.value = document.forms[0].ejercicioAnotacion.value ;
                        document.forms[0].opcion.value="recargar_consulta";
                        document.forms[0].target="mainFrame";
                        document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
                        document.forms[0].submit();        
                        
                    }
                    
                    function checkKeysLocal(evento,tecla){
                        if ('Alt+A' == tecla)
                            pulsarAnular();
                        else if ('Alt+C' == tecla)
                            pulsarCancelar();
                        
                    }
                    </SCRIPT>




<body class="bandaBody" onload="javascript:{};">
    
    <html:form action="/MantAnotacionRegistro.do">         
        <html:hidden property="opcion" value=""/>
        <html:hidden property="ejercicioAnotacion" />
        <html:hidden property="numeroAnotacion" />
        <html:hidden property="numero" value=''/>
        <html:hidden property="ano" value=''/>
        
        <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion(titPag)%></div>
        <div class="contenidoPantalla">
        <table  width="100%" cellpadding="0px" cellspacing="0px">
        <tr>
        </tr>
        <tr>
            <td>
                <table align="center"s>
                    <tr>
                        <TD class="sub3titulo"><%=descriptor.getDescripcion(etiqAnot)%>  <script> idAnotacion(); </script></TD>  
                    </tr>
                    <TR>	
                        <TD style="width: 100%" class="etiqueta"><%=descriptor.getDescripcion("etiq_DiligAnul")%></TD>
                    </TR>

                    <TR>	
                        <TD class="columnP" style="width: 100%">
                            <html:textarea property="txtDiligenciasAnulacion" styleClass='inputTextoObligatorio' cols="125" rows="16" value="" onblur="return xAMayusculas(this);"/>
                        </TD>
                    </TR>                                                
                    <TR>
                        <TD class="columnP" align="center" >&nbsp;</TD>
                    </TR>
                </table>
            </td>
        </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAnular")%> 
                               name="cmdAnular" onClick="pulsarAnular();">
        <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> 
                               name="cmdCancelar"  onClick="pulsarCancelar();">
    </div>                  
</div>                  
</html:form>
<SCRIPT> 
    inicializar();	
</SCRIPT>
</BODY>
</HTML>
