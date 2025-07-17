<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Vector"%>
<%@ page import="es.altia.catalogoformularios.util.DateOperations"%>
<%@ page import="es.altia.catalogoformularios.model.solicitudes.vo.FormularioTramitadoVO"%>
<%@ page import="es.altia.agora.technical.EstructuraCampo"%>
<%@ page import="es.altia.agora.technical.CamposFormulario"%>
<%
  int idioma=1;
  int apl=1;
  if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
      if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
      }
  }
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<html>
<head>
<title>Oculto Formularios PDF de Tramite</title>
<script>
function redirecciona(){
 <%   ArrayList formularios = (ArrayList) request.getAttribute("formsPDF");
      if (formularios != null) {
 %>
var listaFormularios = new Array();
var listaFormulariosOriginal = new Array();
var estados = ['<%= descriptor.getDescripcion("etEstado0Form")%>','<%= descriptor.getDescripcion("etEstado1Form")%>','<%= descriptor.getDescripcion("etEstado2Form")%>','<%= descriptor.getDescripcion("etEstado3Form")%>'];
<%      for (int i=0; i< formularios.size(); i++ ) {
          FormularioTramitadoVO vo = (FormularioTramitadoVO) formularios.get(i);
          String fecha = DateOperations.toString(vo.getFecMod(), "dd-MM-yyyy HH:mm");
  %>
  listaFormularios[<%= i %>]  = ["<%=vo.getCodigo()%>","<%=vo.getDescripcion()%>","<%=fecha%>","<%=vo.getUsuario()%>",estados[<%=vo.getEstado()%>]];
  listaFormulariosOriginal[<%= i %>]  = ["<%=vo.getTipo()%>","<%=vo.getCodigo()%>","<%=vo.getDescripcion()%>","<%=fecha%>","<%=vo.getUsuario()%>","<%=vo.getEstado()%>"];
  <%    } %>
window.opener.top.mainFrame.actualizaTablaFormsPDF(listaFormularios, listaFormulariosOriginal);

<%  }  %>
<%
  //si hay datos suplementarios
  Vector estructura = (Vector) request.getAttribute("estrDtsSupForm");
  if (estructura!=null){
  //por cada dato suplementario
  Vector valores =  (Vector) request.getAttribute("valorDtsSupForm");
  for (int j=0; j<estructura.size(); j++){
    EstructuraCampo ec = (EstructuraCampo) estructura.get(j);
      if ("SI".equals(ec.getActivo())){
    CamposFormulario cf = (CamposFormulario) valores.get(j);
    String valor = cf.getString(ec.getCodCampo());
    if(valor==null) valor="";
    String name = ec.getCodCampo();
    String prefijoCampo = "T_";
    String combos ="";
    if (ec.getCodTipoDato().equals("6")) prefijoCampo = "codT_";
    if (ec.getCodTramite() != null) {
      if (ec.getOcurrencia() != null){
          name = prefijoCampo + ec.getCodTramite() + "_" + ec.getOcurrencia() + "_" + ec.getCodCampo();
          combos = "T_" + ec.getCodTramite() + "_" + ec.getOcurrencia() + "_" + ec.getCodCampo();
      }
      else{
          name = prefijoCampo + ec.getCodTramite() + "_" + ec.getCodCampo();
          combos = "T_" + ec.getCodTramite() + "_" + ec.getCodCampo();
          }
    }
%>
window.opener.top.mainFrame.actualizaCampoSup("<%=name%>", "<%=valor%>","<%=ec.getCodTipoDato()%>","<%=combos%>");
<%       }//if activo
       }//if hay datos suplementarios
    }//for%>
window.opener.focus();
window.self.close();

}
</script>
</head>
<body onload="redirecciona()">

</body>
</html>
