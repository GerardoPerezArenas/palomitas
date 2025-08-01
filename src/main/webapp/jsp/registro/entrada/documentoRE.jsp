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
<%
    String COMMA = ",";
    int idioma=1;
    int apl=4;
    String codUsu = "";
    String css = "";
    UsuarioValueObject usuario=new UsuarioValueObject();
    Log _log = LogFactory.getLog(this.getClass());
    if (session!=null){
      if (usuario!=null) {
        usuario = (UsuarioValueObject)session.getAttribute("usuario");
        idioma = usuario.getIdioma();
        apl = usuario.getAppCod();
	  	int cUsu = usuario.getIdUsuario();
        codUsu = Integer.toString(cUsu);
        css = usuario.getCss();
      }
    }
        
    String sBytes = ConstantesDatos.DESCRIPCION_MEGABYTES;    
    // Se obtienen las extensiones de los ficheros permitidos
    Config m_ConfigTechnical = ConfigServiceHelper.getConfig("Registro");
    String extensiones = m_ConfigTechnical.getString("extension.upload.correct");
    String[] lExt = extensiones.split(COMMA);    

    String userAgent = request.getHeader("user-agent");
%>
<!-- Estilos -->

<!-- Beans -->
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
<!--
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
-->
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/date.js'/>"></script>
<style type="text/css">
   TR.rojo TD {background-color:red;color:white;}
   TR.gris TD {background-color:white;color:#a5a5a5;}
</style>
</head>
<body class="bandaBody" onload="javascript:barraProgresoDocumento('off');">
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
    
    /** Muestra la ventana que permite recuperar una imagen del scanner */
    function abrirVentanaScanner(){           
        document.forms[0].capturar.disabled = false;
        window.open("<%= request.getContextPath() %>/jsp/registro/entrada/scanner.jsp?app=Registro","scan","status=no,toolbar=no, location=no");       
    }      
      
    /** Genera el t�tulo de un documento en base a la fecha y hora actual */    
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
 
    /** Deshabilita el bot�n de capturar */
    function deshabilitarCaptura()
    {
        var fichero = document.forms[0].fichero.value;        
        if(fichero!=null || fichero.length>=1){            
            // Se comprueba que su extensi�n sea una de las v�lidas
            var num = fichero.lastIndexOf(".");            
            var ext = fichero.substring(num + 1,fichero.length);                                    
            if(comprobarExtension(ext)){               
                // Se deshabilita el bot�n de capturar, se genera el t�tulo
                // si no existe ya, y se habilita el boton aceptar.
                document.forms[0].cmdAceptar.disabled = false;
                document.forms[0].capturar.disabled = true;
                if (document.forms[0].tituloFichero.value == '') generarTitulo();
            }
            else{   
                // Se deshabilita el bot�n de aceptar
                document.forms[0].cmdAceptar.disabled = true;
                jsp_alerta("A",'<%=descriptor.getDescripcion("etiqExtensionInvalid")%> <%= extensiones %>');               
           }
        }   
    }         
    
        
    /** Comprueba si una extensi�n de un fichero se encuentra entre 
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
        document.forms[0].reset();
        document.getElementById("obligatorio").value = parsearFecha(new Date());
        document.forms[0].cmdAceptar.disabled = false;
        document.forms[0].capturar.disabled = false;                     
    }
 
    /** Se validan los campos necesarios para dar de alta un documento */
    function validarCampos(){
        var formFichero = document.forms[0].fichero.value;
        var docEscaneado = document.forms[0].docEscaneado.value;
        var titulo       = document.forms[0].tituloFichero.value; 
        // Titulo obligatorio. O se sube doc por POST o bien doc escaneado            
        
        if(titulo!=null && docEscaneado!=null && titulo.length>=1 && docEscaneado.length>=1){             
            return true;
         }
        else 
        // Se permite grabar solo el titulo (sin fichero)
        if(titulo!=null && /* formFichero!=null && */ titulo.length>=1 && docEscaneado.length==0){            
            return true;
        }     
        
      return false;
    }

    function pulsarAceptar() { 
      barraProgresoDocumento('on');
      if (validarCampos()){
          document.forms[0].opcion.value = 'documentoAlta';
          document.forms[0].action = "<html:rewrite page='/registro/DocumentoRegistro.do'/>";
          document.forms[0].submit();
      }
      else{
        barraProgresoDocumento('off');              
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
      }            
    }

    function pulsarCancelar(){
        top.close();
    }
    
     /** Muestra/Oculta la barra de progreso de documento en funci�n del par�metro valor que 
        puede tomar el valor on u off*/
    function barraProgresoDocumento(valor) {        
      if(valor=='on'){   
        
         document.getElementById('hidepage').style.visibility = 'inherit';          
      } 
      else 
      if(valor=='off'){   
          habilitarImagenCal("calFechaLimiteDesde",true);
         document.getElementById('hidepage').style.visibility = 'hidden';                   
      }
  }
  

  
  function mostrarCalFechaLimiteDesde(evento) {
      if(window.event) evento = window.event;
      
    if (document.getElementById("calFechaLimiteDesde").className.indexOf("fa-calendar") != -1 ){
        
      showCalendar('forms[0]','fechaDocumento',null,null,null,'','calFechaLimiteDesde','',null,null,null,null,null,null,null,null,evento);}
  }
        
</script>
<html:form action="/registro/DocumentoRegistro.do" target="_self" enctype="multipart/form-data">
    <input type="hidden" name="opcion" value="">
    <input type="hidden" name="codigo" value="">
    
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_Documento")%></div>
    <div class="contenidoPantalla">
    <table class="contenidoPestanha">
        <tr>
            <td colspan="2" class="etiqueta"> <%=descriptor.getDescripcion("res_fecDoc")%>:</td>
        </tr>
        <tr>
            <td colspan="2">
                <html:text styleId="obligatorio"  styleClass="inputTxtFechaObligatorio" size="12"  property="fechaDocumento"
                           onkeyup = "javascript: return SoloCaracteresFecha(this);"
                           onfocus = "javascript: this.select();" />                                                        
                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaLimiteDesde(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;" >
                    <span class="fa fa-calendar" aria-hidden="true" style="border: 0px none; height: 17; width: 25" id="calFechaLimiteDesde" name="calFechaLimiteDesde" alt="Fecha" ></span>
                </A>
            </td>
        </tr>              
        <tr>
            <td colspan="2" class="etiqueta"><%= descriptor.getDescripcion("etiqTitDoc")%></td>
        </tr>    
        <tr>
            <td colspan="2">
                <input type="text" name="tituloFichero" style="width: 356px" id="tituloFichero" class="inputTexto" onkeyup="return xAMayusculas(this);"/>
            </td>
        </tr>           
        <tr id="trEtiqFichero">
            <td  colspan="2" class="etiqueta"><%= descriptor.getDescripcion("etiqRutaFichero") %></td>
        </tr>                    
        <tr id="trTxtFichero" style="display:inline;">
            <td colspan="2">                            
                <input type="file" name="fichero" id="fichero" class="inputTexto" onchange="javascript:deshabilitarCaptura();"
                       title="Fichero" size="57">                                
            </td>                                                
        </tr>                          
        <tr height="15" id="trEscaneo" style="display:none;">
            <td class="etiqueta" colspan="2">                            
                <%= descriptor.getDescripcion("etiqRecuperadoScan") %>
            </td>
        </tr>
        <c:if test="${sessionScope.modificando == true}">
            <tr>
                <td style="height: 5px" cellpadding="0px" cellspacing="0px" colspan="2"></td>
            </tr>
        </c:if>
        <td align="right">
            <table border="0" width="440px">
                <tr>
                    <td style="width: 351px" class="textoSuelto">
                        <c:if test="${sessionScope.modificando == true}">
                            <%= descriptor.getDescripcion("advModifDoc") %>
                        </c:if>
                    </td>

                    <td align="left">
                        <input type="button" name="capturar" value="Capturar" class="botonCapturar" onclick="javascript:abrirVentanaScanner();"/>

                    </td>       
                </tr>    
            </table>
        </td>
    </table>
    <input type="hidden" name="docEscaneado" id="docEscaneado" value="" onchange="javascript:generarTitulo();">                            
    <div class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value="<%= descriptor.getDescripcion("etiq_limpiar") %>" name="Vaciar" onclick="javascript:resetearFormulario();"/>           												
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar" onclick="pulsarAceptar();">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelar" onclick="pulsarCancelar();">
    </div>
</div>
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

</html:form>
<script language="javascript">
    <c:if test="${requestScope.EXTENSION_FILE_INCORRECT eq 'si' and requestScope.TAM_MAX_FILE_EXCEED eq 'si'}">
        barraProgresoDocumento('off');      
        jsp_alerta("A",'<%=descriptor.getDescripcion("etiqTamMaxExceed")%> <c:out value='${requestScope.TAM_MAX_FILE_BYTE}'/> <%= sBytes %> <br> <%=descriptor.getDescripcion("etiqExtensionInvalid")%> <c:out value='${requestScope.EXTENSION_PERMITED}'/>');
    </c:if>
    
    <c:if test="${requestScope.EXTENSION_FILE_INCORRECT eq 'si' and requestScope.TAM_MAX_FILE_EXCEED ne 'si'}">
        barraProgresoDocumento('off');      
        jsp_alerta("A",'<%=descriptor.getDescripcion("etiqExtensionInvalid")%> <c:out value='${requestScope.EXTENSION_PERMITED}'/>');
    </c:if>
    
     <c:if test="${requestScope.EXTENSION_FILE_INCORRECT ne 'si' and requestScope.TAM_MAX_FILE_EXCEED eq 'si'}">
        barraProgresoDocumento('off');      
        jsp_alerta("A",'<%=descriptor.getDescripcion("etiqTamMaxExceed")%> <c:out value='${requestScope.TAM_MAX_FILE_BYTE}'/> <%= sBytes %>');
    </c:if>

    <c:if test="${requestScope.ERROR_FILESIZE_UPLOAD eq 'si'}">
        barraProgresoDocumento('off');      
        jsp_alerta("A",'<%=descriptor.getDescripcion("etiqTamMaxExceed") %> <c:out value="${requestScope.TAM_MAX_FILE_BYTE}"/> <%= sBytes %>');
    </c:if>
    
    <c:if test="${requestScope.EXCEED_LIMITE_GLOBAL_DOCUMENTOS_REGISTRO eq 'si'}">
        barraProgresoDocumento('off');      
        jsp_alerta("A",'<%= descriptor.getDescripcion("tamGlobalExceed") %> <c:out value='${requestScope.LIMITE_GLOBAL_DOCUMENTOS_REGISTRO}'/> <%= sBytes %>');
    </c:if>
    
    <c:if test="${requestScope.DOCUMENT_TITLE_REPEATED eq 'si'}">
        barraProgresoDocumento('off');      
        jsp_alerta("A","<%=descriptor.getDescripcion("etiqTituloRepetido") %>");
    </c:if>


    <c:if test="${requestScope.ERROR_COPIAR_DOCUMENTO_REGISTRO_RUTA_DISCO eq 'SI'}">
        barraProgresoDocumento('off');      
        jsp_alerta("A","<%=descriptor.getDescripcion("errCopiarArchivoRutaTemporal")%>");
    </c:if>
        
    <c:if test="${requestScope.ERROR_OBTENER_RUTA_ALMACEN_TEMPORAL_DISCO eq 'SI'}">
        barraProgresoDocumento('off');      
        jsp_alerta("A","<%=descriptor.getDescripcion("errObtenerRutaTemporal")%>");
    </c:if>    


  
   document.forms[0].tituloFichero.value = 
      "<str:escape><bean:write name="DocumentoRegistroForm" property="tituloFichero" filter="false"/></str:escape>";


    //Usado para el calendario
var coordx=0;
var coordy=0;


<%if(userAgent.indexOf("MSIE")==-1) {%> //Que no sea IE
    window.addEventListener('mousemove', function(e) {
        coordx = e.clientX;
        coordy = e.clientY;
    }, true);
<%}%>






document.onmouseup = checkKeys;

function checkKeysLocal(evento,tecla) {
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else{
        teclaAuxiliar = evento.which;
    }


	if (teclaAuxiliar == 1){
            if(IsCalendarVisible) replegarCalendario(coordx,coordy);
        }
        if (teclaAuxiliar == 9){
            if(IsCalendarVisible) hideCalendar();
        }
}
</script>

</body>
</html:html>
