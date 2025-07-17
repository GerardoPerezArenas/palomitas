<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
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
<%@ page import="es.altia.agora.technical.ConstantesDatos"%>
<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title>::: Adjuntar Fichero:::</title>

<jsp:include page="/jsp/plantillas/Metas.jsp" />
<meta http-equiv="X-UA-Compatible" content="IE=edge"> 
<%
    String COMMA = ",";
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
        
    String sBytes = ConstantesDatos.DESCRIPCION_MEGABYTES;    
    // Se obtienen las extensiones de los ficheros permitidos
    Config m_ConfigTechnical = ConfigServiceHelper.getConfig("Registro");
    String extensiones = m_ConfigTechnical.getString("extension.upload.correct");
    String[] lExt = extensiones.split(COMMA);    
    
%>
<!-- Estilos -->




<!-- Beans -->
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
<!--
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
-->
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
</head>
<body class="bandaBody" >
<script type="text/javascript">      
    var extensiones = new Array();       
    <%
        if(lExt!=null && lExt.length>0)
        {
            // Se guarda en un Array de javascript las extensiones de los ficheros permitidas
            for(int i=0;i<lExt.length;i++){
                out.print("extensiones[" + i + "]='" + lExt[i] + "';");                
            }
        }
    %>       
    
    /** Comprueba si una extensión de un fichero se encuentra entre 
        una de las aceptadas */  
    function comprobarExtension(extension)
    {
        var j = 0;        
        if(extensiones!=null){            
            for(j=0;j<extensiones.length;j++){                
                if(extensiones[j]==extension.toLowerCase())
                    return true;                            
            }
        }        
        return false;        
    }
    
    function pulsarAceptar() { 
   
      
                document.forms[0].opcion.value="insertarProcedimiento";
                document.forms[0].action = "<html:rewrite page='/xpdl/DocumentoXPDL.do'/>";
                document.forms[0].submit();
             
    }
    
    function pulsarCancelar(){
        this.close();
    }
    
    function onChangeFichero() {
        var aceptar = document.getElementById("botonAceptar");
        var fichero = document.getElementById("fichero");
        
        // formato valido
        if (fichero.value.indexOf(".xml") >= 0) {
            aceptar.disabled = false;
            $("#errorFormato").hide();
        } else {
            aceptar.disabled = true;
            $("#errorFormato").show();
        }
    }
    
</script>
<html:form action="/xpdl/DocumentoXPDL.do" target="_self" enctype="multipart/form-data">
<input type="hidden" name="opcion" value="">    
<div class="txttitblanco"><%=descriptor.getDescripcion("xpdlDocumento")%></div>
<div class="contenidoPantalla">
    <table width="100%">
        <tr id="trEtiqFichero">
            <td class="etiqueta"><%= descriptor.getDescripcion("xpdlRutaFichero") %></td>
        </tr>                    
        <tr id="trTxtFichero">
            <td>
                <input type="file" name="fichero" id="fichero" class="inputTexto" title="Fichero"
                       size="80" onchange="onChangeFichero();">                                
            </td>
        </tr>      
        <tr>
            <td>
                <div id="errorFormato" class="etiquetaError" hidden="true">
                    El formato del fichero debe ser XML.
                </div>
            </td>
        </tr>                    
    </table>
    <div class="botoneraPrincipal">
        <input type="button" class="botonGeneral" id="botonAceptar" value="<%=descriptor.getDescripcion("gbAceptar")%>" 
               name="cmdAceptar" onclick="pulsarAceptar();" disabled="true">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" 
               name="cmdCancelar" onclick="pulsarCancelar();">
    </div>
</div>
</html:form>
</body>
</html:html>
