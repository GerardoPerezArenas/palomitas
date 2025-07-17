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
<%@ page import="es.altia.agora.technical.ConstantesDatos"%>
<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title>::: Adjuntar Fichero:::</title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
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
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String sBytes  = ConstantesDatos.DESCRIPCION_MEGABYTES;      
     // Se obtienen las extensiones de los ficheros permitidos
    Config m_ConfigTechnical = ConfigServiceHelper.getConfig("Expediente");
    String extensiones = m_ConfigTechnical.getString("extension.upload.correct");
    String[] lExt = extensiones.split(COMMA);


    String codTramite = (String)request.getAttribute("codTramite");
    String ocurrenciaTramite = (String)request.getAttribute("ocurrenciaTramite");
    
%>
<!-- Estilos -->

<!-- Beans -->
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
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
</head>
<body class="bandaBody" onload="javascript:barraProgresoDocumento('off');">    
<script type="text/javascript">
    
    var codTramite = '<%=codTramite%>';
    var ocurrenciaTramite = '<%=ocurrenciaTramite%>';
    
    
    var extensiones = new Array();       
    <%
        if(lExt!=null && lExt.length>0){
            // Se guarda en un Array de javascript las extensiones de los ficheros permitidas
            for(int i=0;i<lExt.length;i++){
                out.print("extensiones[" + i + "]='" + lExt[i] + "';");                
            }
        }
    %>       
    
    /** Muestra la ventana que permite recuperar una imagen del scanner */
    function abrirVentanaScanner(){   
        //documento.forms[0].fichero.disabled = true;
        window.open("<%= request.getContextPath() %>/jsp/registro/entrada/scanner.jsp?app=Expediente","scan","status=no,toolbar=no, location=no");       
    }      
  
    /** Deshabilita el botón de capturar */
    function deshabilitarCaptura()
    {
        var fichero = document.forms[0].fichero.value;        
        if(fichero!=null || fichero.length>=1){
            document.forms[0].capturar.disabled = true;
            generarTitulo();
        }          
    }         


  /** Genera el título de un documento en base a la fecha y hora actual */    
    function generarTitulo()
    {
        var cab     = "DOCUMENTO";
        var guion   = "_";
        var fecha = new Date();        
        var day = fecha.getDate() + "";
        var month = (fecha.getMonth() +  1) + "";
        var year  = fecha.getFullYear() + "";
        var hour = fecha.getHours() + "";
        var minutes = fecha.getMinutes() + "";
        var seconds = fecha.getSeconds() + "";
                
        if(day.length==1)
            day = "0" + day;
        if(month.length==1)
            month = "0" + month;        
        if(hour.length==1)
            hour = "0" + hour;
        if(minutes.length==1)
            minutes = "0" + minutes;
        if(seconds.length==1)
            seconds = "0" + seconds;
        
        var nombre = cab + guion + day + month + year + guion + hour + minutes + seconds;
        document.forms[0].tituloFichero.value = nombre;
    }
    
    
    /** Muestra/Oculta la barra de progreso de documento en función del parámetro valor que 
        puede tomar el valor on u off*/
    function barraProgresoDocumento(valor) {        
      if(valor=='on'){            
         document.getElementById('hidepage').style.display = '';          
      } 
      else 
      if(valor=='off'){           
         document.getElementById('hidepage').style.display = 'none';                   
      }
    }
    
    
    /** Deshabilita el botón de capturar */
    function deshabilitarCaptura()
    {
        var fichero = document.forms[0].fichero.value;        
        if(fichero!=null || fichero.length>=1){            
            // Se comprueba que su extensión sea una de las válidas
            var num = fichero.lastIndexOf(".");            
            var ext = fichero.substring(num + 1,fichero.length);                                    
            if(comprobarExtension(ext)){               
                // Se deshabilita el botón de capturar y se genera el título
                document.forms[0].capturar.disabled = true;   
                if (document.forms[0].tituloFichero.value == '') generarTitulo();
            }
            else{   
                // Se deshabilita el botón de aceptar
                document.forms[0].cmdAceptar.disabled = true;
                document.forms[0].capturar.disabled = false;
                // ============================================== Tarea #13291. añadimos el .replace(...)
                jsp_alerta("A",'<%=descriptor.getDescripcion("etiqExtensionInvalid")%> <%= extensiones.replace("," , ", ") %>');
           }
        }   
    }         
            
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
        
    function resetearFormulario()
    {         
        document.forms[0].cmdAceptar.disabled = false;
        document.forms[0].capturar.disabled = false;
        document.forms[0].docEscaneado.value = "";    
        
        document.forms[0].tituloFichero.value='';
        document.forms[0].fichero.value='';
    }
    
     /** Se validan los campos necesarios para dar de alta un documento  */
    function validarCampos(){
        var formFichero = document.forms[0].fichero.value;
        var docEscaneado = document.forms[0].docEscaneado.value;             
        var tituloFichero = document.forms[0].tituloFichero.value;
            
        if((tituloFichero!=null && tituloFichero.length>=1) && ((docEscaneado!=null && docEscaneado.length>=1) || (formFichero!=null && formFichero.length>=1))){          
            return true;
        } else {
            // Se permite grabar solo el titulo (sin fichero)
            if(tituloFichero!=null && /* formFichero!=null && */ tituloFichero.length>=1 && docEscaneado.length==0){ 
                return true;
            }
        }
        return false;
    }     

    function pulsarAceptar() {
      barraProgresoDocumento('on');
      if (validarCampos()){
          document.forms[0].target = 'oculto'; 
          document.forms[0].opcion.value = 'cargar';          
          document.forms[0].path.value = document.forms[0].fichero.value.replace(/\\/g, "/");
          document.forms[0].action = "<html:rewrite page='/sge/DatosSuplementariosFichero.do'/>?codTramite=" + codTramite + "&ocurrenciaTramite=" + ocurrenciaTramite;          
          barraProgresoDocumento('off');
          document.forms[0].submit();
      }
      else{          
          jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
          barraProgresoDocumento('off');
      }
    }

    function pulsarCancelar(){
        top.close();
    }
</script>
<%
    String codigo = null;
    if(request.getParameter("codigo")!=null && request.getParameter("codigo").length()>0)
        codigo = request.getParameter("codigo");
    else
        codigo = (String)request.getAttribute("CODIGO_DOC_REGISTRO_ACTION");
  
    String destino=request.getParameter("destino");  
%>
<html:form action="/sge/DatosSuplementariosFichero.do" target="_self" enctype="multipart/form-data">
    
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<input type="hidden" name="opcion" value="">
<input type="hidden" name="destino" value="<%=destino%>">
<input type="hidden" name="path" value="">
<input type="hidden" name="codigo" value="<%=codigo%>">

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("etiqFichero")%></div>
<div class="contenidoPantalla">
    <table>
        <tr>
            <td class="etiqueta"><%= descriptor.getDescripcion("etiqTitDoc")%></td>
        </tr>    
        <tr>
            <td>
                <input type="text" name="tituloFichero" style="width:421px" id="" class="inputTexto"/>
            </td>
        </tr>
        <tr id="trEtiqFichero">
            <td class="etiqueta"><%= descriptor.getDescripcion("etiqRutaFichero") %></td>
        </tr>   
        <tr id="trTxtFichero">
            <td>
                <input type="file" name="fichero" id="fichero" class="inputTexto" title="Fichero" style="width:421px" onchange="javascript:deshabilitarCaptura();">
            </td>
        </tr>
        <tr height="15" id="trEscaneo" style="display:none;">
            <td class="etiqueta">                            
                <%= descriptor.getDescripcion("etiqRecuperadoScan") %>
            </td>
        </tr>        
        <tr>
            <td align="right">
                <table border="0" width="100%">
                    <tr>
                        <td style="width: 80%">
                            &nbsp;
                        </td>
                        <td>
                            <input type="button" name="capturar" value="Capturar" class="botonCapturar" onclick="javascript:abrirVentanaScanner();"/>                                
                        </td>   
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <input type="hidden" name="docEscaneado" id="docEscaneado" value="" onchange="javascript:generarTitulo()">
    <div class="botoneraPrincipal">
        <input type="button" value="<%= descriptor.getDescripcion("etiq_limpiar") %>" class="botonGeneral" name="Vaciar" onclick="resetearFormulario();"/>   
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar" onclick="pulsarAceptar();">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelar" onclick="pulsarCancelar();">
    </div>         
</div>         
<script type="text/javascript">
      <c:if test="${requestScope.EXTENSION_FILE_INCORRECT eq 'si' and requestScope.TAM_MAX_FILE_EXCEED eq 'si'}">
        barraProgresoDocumento('off');    
        jsp_alerta("A",'<%=descriptor.getDescripcion("etiqTamMaxExceed")%> <c:out value='${requestScope.TAM_MAX_FILE_BYTE}'/> <%= sBytes %> <br> <%=descriptor.getDescripcion("etiqExtensionInvalid")%> <c:out value='${requestScope.EXTENSION_PERMITED}'/>');
      </c:if>
    
     <c:if test="${requestScope.EXTENSION_FILE_INCORRECT eq 'si' and requestScope.TAM_MAX_FILE_EXCEED ne 'si'}">
        jsp_alerta("A",'<%=descriptor.getDescripcion("etiqExtensionInvalid")%> <c:out value='${requestScope.EXTENSION_PERMITED}'/>');
        barraProgresoDocumento('off');        
     </c:if>
    
     <c:if test="${requestScope.EXTENSION_FILE_INCORRECT ne 'si' and requestScope.TAM_MAX_FILE_EXCEED eq 'si'}">
        barraProgresoDocumento('off');
        jsp_alerta("A",'<%=descriptor.getDescripcion("etiqTamMaxExceed")%> <c:out value='${requestScope.TAM_MAX_FILE_BYTE}'/> <%= sBytes %>');
     </c:if>
    
    <c:if test="${requestScope.ERROR_FILESIZE_UPLOAD eq 'si'}">
        barraProgresoDocumento('off');
        jsp_alerta("A",'<%=descriptor.getDescripcion("etiqTamMaxExceed") %> <c:out value="${requestScope.TAM_MAX_FILE_BYTE}"/> <%= sBytes %>');
    </c:if>  
    
    <c:if test="${requestScope.EXCEED_LIMITE_GLOBAL_DOCUMENTOS_EXPEDIENTES eq 'si'}">
        barraProgresoDocumento('off');      
        jsp_alerta("A",'<%= descriptor.getDescripcion("tamGlobalExceed") %> <c:out value='${requestScope.LIMITE_GLOBAL_DOCUMENTOS_EXPEDIENTES}'/> <%= sBytes %>');
    </c:if>
        
    <c:if test="${requestScope.ERROR_COPIAR_DOCUMENTO_REGISTRO_RUTA_DISCO eq 'SI'}">
        barraProgresoDocumento('off');      
        jsp_alerta("A","<%=descriptor.getDescripcion("errCopiarArchivoRutaTemporal")%>");
    </c:if>
        
    <c:if test="${requestScope.ERROR_OBTENER_RUTA_ALMACEN_TEMPORAL_DISCO eq 'SI'}">
        barraProgresoDocumento('off');  
        jsp_alerta("A","<%=descriptor.getDescripcion("errObtenerRutaTemporal")%>");
    </c:if>    
    
</script>

</html:form>
</body>
</html:html>
