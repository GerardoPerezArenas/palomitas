<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.formularios.FichaFormularioForm"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.DatosSuplementariosFicheroForm"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.TramitacionForm"%>
<%@ page import="es.altia.agora.technical.ConstantesDatos"%>
<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title>:::Rechazar Asiento:::</title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<%
    int idioma=1;
    int apl=4;
	String codUsu = "";
    UsuarioValueObject usuario=new UsuarioValueObject();
    Log _log = LogFactory.getLog(this.getClass());
    if (session!=null){
      if (usuario!=null) {
        usuario = (UsuarioValueObject)session.getAttribute("usuario");
        idioma = usuario.getIdioma();
        apl = usuario.getAppCod();
	  	int cUsu = usuario.getIdUsuario();
        codUsu = Integer.toString(cUsu);
      }
    }
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String sBytes = ConstantesDatos.DESCRIPCION_BYTES;
    
    TramitacionForm tramForm = (TramitacionForm) session.getAttribute("TramitacionForm");
    boolean obsObligatorias = tramForm.isObsObligatorias();
    
%>
<!-- Estilos -->
<!-- Beans -->
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<style type="text/css">
   TR.rojo TD {background-color:red;color:white;}
   TR.gris TD {background-color:white;color:#a5a5a5;}
</style>

<script type="text/javascript">
     
   <% if (obsObligatorias) { %>
      obsObligatorias = true;
   <% } else { %>
      obsObligatorias = false;
   <% } %>

    /** Carga las observaciones en el formulario obteniendolas de la 
        ventana padre, pasada en self.parent.opener.xanelaAuxiliarArgs  */
    function cargar(){
         observacionesOriginales = self.parent.opener.xanelaAuxiliarArgs.document.forms[0].observaciones.value;
         document.forms[0].observaciones.value=self.parent.opener.xanelaAuxiliarArgs.document.forms[0].observaciones.value;        
    }
        
</script>
</head>
<body class="bandaBody" onload="javascript:cargar();">
<script type="text/javascript">      
        
    function pulsarRechazar()
    {   
         var obs = document.forms[0].observaciones.value;
        if (obsObligatorias && observacionesOriginales == obs) {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjObsRechazar")%>');
            return;
        }
        datos = { obs : obs };
        self.parent.opener.retornoXanelaAuxiliar(datos);

    }
    
   
    function pulsarCancelar(){
        self.parent.opener.retornoXanelaAuxiliar();
    }
    
</script>
<html:form action="/sge/Tramitacion.do" method="POST">
    <input type="hidden" name="opcion" value="aceptarAsiento">
    <input type="hidden" name="codigo" value="">
    <div class="txttitblanco"><%=descriptor.getDescripcion("desRechazar")%></div>
    <div class="contenidoPantalla" valign="top" style="padding-top: 5px; padding-bottom: 10px">
        <table>                                            
            <tr>
                <td class="etiqueta"><%= descriptor.getDescripcion("etiq_observaciones") %></td>
            </tr>    
            <tr>
                <td>
                    <textarea <% if (obsObligatorias) {%> class="textareaTextoObligatorio" <% } else {%> class="textareaTexto" <% }%> name="observaciones" cols="126" rows="10"></textarea>
                </td>
            </tr>                                                                
        </table>                  
        <div id="capaBotones1" name="capaBotones1" class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar" onclick="javascript:pulsarRechazar();">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelar" onclick="pulsarCancelar();">
        </div>
    </div>
</html:form>
</body>
</html>
