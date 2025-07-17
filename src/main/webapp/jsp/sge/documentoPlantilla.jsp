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
<title>::: Adjuntar Fichero :::</title>
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

  String aplicacion=request.getParameter("codAplicacion");
  String proc = request.getParameter("codProcedimiento");
  String tramite = request.getParameter("codTramite");

    Config m_Config = ConfigServiceHelper.getConfig("common");
    String sBytes  = ConstantesDatos.DESCRIPCION_MEGABYTES;
     // Se obtienen las extensiones de los ficheros permitidos
    Config m_ConfigTechnical = ConfigServiceHelper.getConfig("Expediente");
    String extensiones = m_ConfigTechnical.getString("extesion_plantilla_adjuntar");
    String[] lExt = extensiones.split(COMMA);
%>
<!-- Beans -->
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<style type="text/css">
   TR.rojo TD {background-color:red;color:white;}
   TR.gris TD {background-color:white;color:#a5a5a5;}
</style>
</head>
<body class="bandaBody" onload="inicializar();">
<script type="text/javascript">

var estaModificando=<str:escape><bean:write name="EditorDocumentosAplicacionForm" property="modificando" filter="false"/></str:escape>;
var aplicacion=<%=aplicacion%>;

    var extensiones = new Array();
    <%
        if(lExt!=null && lExt.length>0){
            // Se guarda en un Array de javascript las extensiones de los ficheros permitidas
            for(int i=0;i<lExt.length;i++){
                out.print("extensiones[" + i + "]='" + lExt[i] + "';");
            }
        }
    %>

function inicializar(){
   
    if(aplicacion==1)
    {
        document.getElementById("filaRelacion").style.display = "none";
        document.getElementById("filaInteresado").style.display = "none";
    }
    pleaseWait("off");
}


/** Genera el título de un documento en base a la fecha y hora actual */
    function generarTitulo(){
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
        document.forms[0].nombreDocumento.value = nombre;
    }

     /** Deshabilita el botón de capturar */
    function deshabilitarCaptura(){
        var fichero = document.forms[0].ficheroWord.value;
        if(fichero!=null || fichero.length>=1){
            // Se comprueba que su extensión sea una de las válidas
            var num = fichero.lastIndexOf(".");
            var ext = fichero.substring(num + 1,fichero.length);
            if(comprobarExtension(ext)){
                // Se deshabilita el botón de capturar, se genera el título
                // si no existe ya, y se habilita el boton aceptar.
                document.forms[0].cmdAceptar.disabled = false;
                if (document.forms[0].nombreDocumento.value == '') generarTitulo();
            }
            else{
                // Se deshabilita el botón de aceptar
                document.forms[0].cmdAceptar.disabled = true;
                jsp_alerta("A",'<%=descriptor.getDescripcion("etiqExtensionInvalid")%> <%= extensiones %>');
           }
        }
    }



 /** Comprueba si una extensión de un fichero se encuentra entre
        una de las aceptadas */
    function comprobarExtension(extension){
        var j = 0;
        if(extensiones!=null){
            for(j=0;j<extensiones.length;j++){
                if(extensiones[j]==extension.toLowerCase())
                    return true;
            }
        }
        return false;
    }


    function resetearFormulario() {
        var trEscaneo     = document.getElementById("trEscaneo");
        var trTxtFichero  = document.getElementById("trTxtFichero");
        var trEtiqFichero = document.getElementById("trEtiqFichero");

        document.forms[0].cmdAceptar.disabled = false;
        document.forms[0].docEscaneado.value = "";
        trEtiqFichero.style.display  = 'inline';
        trTxtFichero.style.display   = 'inline';
        trEscaneo.style.display      = 'none';
    }


  /** Se validan los campos necesarios para dar de alta un documento */
  function validarCampos(){
        var formFichero = document.forms[0].ficheroWord.value;
        var docEscaneado = document.forms[0].docEscaneado.value;
        var titulo       = document.forms[0].nombreDocumento.value;
        // Titulo obligatorio. O se sube doc por POST o bien doc escaneado

        if(titulo!=null && titulo.length>=1){
            return true;
        } else
        // Se permite grabar solo el titulo (sin fichero)
            if(titulo!=null && /* formFichero!=null && */ titulo.length>=1 ){
                return true;
            }     

        return false;
    }

    function pulsarAceptar() { 
      barraProgresoDocumento('on');
      if (validarCampos()){
       
      if(document.getElementById("relacionS").checked){
        document.getElementById("relacion").value='S';
        document.getElementById("interesado").value='N';
      }else if(document.getElementById("interesadoS").checked){
         document.getElementById("relacion").value='N';
            document.getElementById("interesado").value='S';
    }else{
       document.getElementById("relacion").value='N';
       document.getElementById("interesado").value='N';
    }

          document.forms[0].opcion.value = 'documentoAlta';
          document.forms[0].target="oculto";
          document.forms[0].action = "<html:rewrite page='/editor/DocumentosAplicacion.do'/>";
          document.forms[0].submit();
         
      }
      else{
        barraProgresoDocumento('off');
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
      }
    }


    function pulsarCancelar(){
        self.parent.opener.retornoXanelaAuxiliar();
    }
    
    
    /** Muestra/Oculta la barra de progreso de documento en función del parámetro valor que
        puede tomar el valor on u off*/
    function barraProgresoDocumento(valor) {
        if(valor=='on'){
            document.getElementById('hidepage').style.visibility = 'inherit';
        }
        else
            if(valor=='off'){
                
            }
    }


   function mostrarCalFechaLimiteDesde() {
       
    }
    
  function resetearForm(){
    if (!estaModificando) {
    document.getElementById("relacionN").checked=true;
    document.getElementById("interesadoN").checked=true;
        document.forms[0].nombreDocumento.value="";
    }
    document.getElementById("ficheroWord").value="";
    document.getElementById("filaInteresado").style.display="";
 }
 
 function onClickParaRelacion(){
    if (document.getElementById("relacionS").checked){
        document.getElementById("interesadoN").checked = true;
        document.getElementById("filaInteresado").style.display = "none";
    } else
        document.getElementById("filaInteresado").style.display = "";
}


</script>
<html:form action="/editor/DocumentosAplicacion.do" target="_self" enctype="multipart/form-data" >

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

    <input type="hidden" name="opcion" value="">
    <input type="hidden" name="codigo" value="">
     <input type="hidden" name="codigo" value="">
     <input type="hidden" name="relacion"  id="relacion" value="">
      <input type="hidden" name="interesado"  id="interesado" value="">
    
     <html:hidden property="codAplicacion"/>
    <html:hidden property="codProcedimiento"/>
    <html:hidden property="codTramite"/>
    <html:hidden property="codDocumento"/>
    <html:hidden property="docActivo"/>
    <html:hidden property="editorTexto" value="ODT"/>

    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_Documento")%></div>
    <div class="contenidoPantalla">
        <table class="contenidoPestanha">
            <tr id="filaRelacion">                                                       
                <td style="width: 50%" class="etiqueta">
                    <%=descriptor.getDescripcion("msjGenParaRelac")%>
                </td>
                <td style="width: 25%" class="etiqueta">
                    <input type="radio" id="relacionS" name="paraRelacion" styleClass="textoSuelto" value="S" onclick="onClickParaRelacion();"/> <%=descriptor.getDescripcion("etiq_si")%>&nbsp;
                </td>
                <td style="width: 25%" class="etiqueta">
                    <input type="radio" id="relacionN" name="paraRelacion" styleClass="textoSuelto" value="N" onclick="onClickParaRelacion();" checked/> <%=descriptor.getDescripcion("etiq_no")%>&nbsp;
                </td>
            </tr>
            <tr id="filaInteresado">                                                       
                <td style="width: 50%" class="etiqueta">
                    <%=descriptor.getDescripcion("msjGenPorInt")%>
                </td>
                <td style="width: 25%" class="etiqueta">
                    <input type="radio" id="interesadoS" name="porInteresado" styleClass="textoSuelto" value="S"/> <%=descriptor.getDescripcion("etiq_si")%>&nbsp;
                </td>
                <td style="width: 25%" class="etiqueta">
                    <input type="radio" id="interesadoN" name="porInteresado" styleClass="textoSuelto" value="N" checked/> <%=descriptor.getDescripcion("etiq_no")%>&nbsp;
                </td>
           </tr>
            <tr>
               <td class="columnP"></td>
           </tr>
            <tr>
                <td colspan="2" class="etiqueta"><%= descriptor.getDescripcion("etiqTitDoc")%></td>
            </tr>

            <tr>
                <td colspan="2">
                    <input type="text" size="65" name="nombreDocumento" style="width: 356px" id="nombrePlantilla" class="inputTexto" onkeypress="javascript:PasaAMayusculas(event);"/>
                </td>
            </tr>

            <tr id="trEtiqFichero">
                <td  colspan="2" class="etiqueta"><%= descriptor.getDescripcion("etiqRutaFichero") %></td>
            </tr>
         

            <tr id="trTxtFichero" style="display:inline;">
                <td colspan="2">
                    <input type="file" name="ficheroWord" id="ficheroWord" class="inputTexto" onchange="javascript:deshabilitarCaptura();"
                           title="Fichero" size="57">
                </td>
            </tr>
               <tr>
               <td class="columnP">*extensiones permitidas: <%=extensiones%> </td>
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


                    </tr>
                </table>
            </td>
        </table>
        <input type="hidden" name="docEscaneado" id="docEscaneado" value="" onchange="javascript:generarTitulo();">
        <div class="botoneraPrincipal">
            <input type="button" value="<%= descriptor.getDescripcion("etiq_limpiar") %>" class="botonGeneral" name="Vaciar" onclick="resetearForm();"/>
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar" onclick="pulsarAceptar();">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelar" onclick="pulsarCancelar();">
        </div>
    </div>
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

  

    <c:if test="${requestScope.DOCUMENT_TITLE_REPEATED eq 'si'}">
        barraProgresoDocumento('off');
        jsp_alerta("A","<%=descriptor.getDescripcion("etiqTituloRepetido") %>");
    </c:if>

 //deshabilitamos los elementos del formulario si se esta moficando la plantilla	
     if (estaModificando){	
     	document.forms[0].nombreDocumento.value =
      "<str:escape><bean:write name="EditorDocumentosAplicacionForm" property="nombreDocumento" filter="false"/></str:escape>";
         document.getElementById("nombrePlantilla").disabled=true;	
         document.getElementById("interesadoS").disabled=true;	
         document.getElementById("interesadoN").disabled=true;	
         document.getElementById("relacionS").disabled=true;	
         document.getElementById("relacionN").disabled=true;	
         var  interesado='<str:escape><bean:write name="EditorDocumentosAplicacionForm" property="interesado" filter="false"/></str:escape>';	
         if(interesado=="N"){	
             document.getElementById("interesadoN").checked=true;	
             document.getElementById("interesadoS").checked=false;	
         }else{
	
             document.getElementById("interesadoS").checked=true;	
             document.getElementById("interesadoN").checked=false;	
         }	
          var  relacion='<str:escape><bean:write name="EditorDocumentosAplicacionForm" property="relacion" filter="false"/></str:escape>';	
         if(interesado=="N"){	
             document.getElementById("relacionN").checked=true;	
             document.getElementById("relacionS").checked=false;	
             	
         }else{	
             document.getElementById("relacionS").checked=true;	
             document.getElementById("relacionN").checked=false;	
         }	
     }

</script>


</body>
</html:html>
