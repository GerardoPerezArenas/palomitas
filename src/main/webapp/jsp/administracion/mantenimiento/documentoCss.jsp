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
<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title>::: Adjuntar Fichero:::</title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<%
    int idioma=1;
    int apl=5;
	String codUsu = "";
    UsuarioValueObject usuario=new UsuarioValueObject();
    Log _log = LogFactory.getLog(this.getClass());
    String css = "";
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
    Config m_Config = ConfigServiceHelper.getConfig("common");
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
<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<style type="text/css">
   TR.rojo TD {background-color:red;color:white;}
   TR.gris TD {background-color:white;color:#a5a5a5;}
</style>
</head>
<script type="text/javascript">
    var listaCss = new Array();
    var correcto=0;
    
    function validarCampos(){
         listaCss = self.parent.opener.xanelaAuxiliarArgs;
        var formFichero = document.forms[0].fichero.value;

         var temp = new Array();
         temp=formFichero.split("\\");
         //obtengo la posicion del nombre.css para comprobar si ya exite
         posicion=temp.length;
         //alert("pos"+posicion);
         //alert("temp  "+temp[posicion-1]);
         css="/css/"+temp[posicion-1];
         for(i=0;i<listaCss.length;i++){
          if(listaCss[i][1]==css){
               correcto=0;
          }else{
            correcto=1;
          } 
         }
           if((formFichero!="")&&(correcto==1)) return true;
              return false;
     
    }
    
 

    function pulsarAceptar() {
      if (validarCampos()){
        document.forms[0].opcion.value = 'documentoAlta';
        document.forms[0].target = "oculto";
        document.forms[0].action = "<html:rewrite page='/administracion/mantenimiento/DocumentoCss.do'/>";
        document.forms[0].submit();
        self.parent.opener.retornoXanelaAuxiliar();
      }else{
         retorno = ["iguales"];
         self.parent.opener.retornoXanelaAuxiliar(retorno);
       
      }
    }

    function pulsarCancelar(){
        self.parent.opener.retornoXanelaAuxiliar();
    }
    
</script>

<body class="bandaBody" >
    <html:form action="/registro/DocumentoRegistro.do" method="POST" target="_self" enctype="multipart/form-data">
        <input type="hidden" name="opcion" value="">
        <input type="hidden" name="codigo" value="">
        <div class="txttitblanco"><%= descriptor.getDescripcion("gTit_DocumentoCss")%></div>
        <div class="contenidoPantalla">
            <table width="100%">
                <tr>
                  <td class="etiqueta">
                      <%= descriptor.getDescripcion("gEtiq_Descrip")%>
                  </td>
              </tr>
              <tr>
                  <td>
                      <input type="text" name="descripcion" class="inputTexto"  title="descripcion"  style="width:90%" maxlength="80">
                  </td>
              </tr>
               <tr>
                  <td class="etiqueta">
                       <%= descriptor.getDescripcion("etiqFichero")%>
                  </td>
              </tr>
              <tr>
                  <td>
                      <input type="file" name="fichero" id="fichero" class="inputTexto"  title="Fichero" style="width:90%">
                  </td>
              </tr>
            </table>
            <div class="botoneraPrincipal"> 
                <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar" onclick="pulsarAceptar();">
                 <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelar" onclick="pulsarCancelar();">
            </div>    
        </div>    
    </html:form>
</body>
</html:html>
