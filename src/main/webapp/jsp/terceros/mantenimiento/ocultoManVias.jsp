<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<html>
<head>
<title>Oculto Padron</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<%int idioma=1;
int apl=1;
int munic = 0;
if (session!=null){
  UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    if (usuario!=null) {
      idioma = usuario.getIdioma();
      apl = usuario.getAppCod();
      munic = usuario.getOrgCod();
    }
}%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript">
<%MantenimientosTercerosForm mantForm = (MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");%>
var opcion = '<%=mantForm.getOpcion()%>';
var operacion = '<%=mantForm.getOperacion()%>';
var frame;
frame=parent.mainFrame;
function cargarListaMunicipios(){
<%Vector listaMunicipios = mantForm.getListaMunicipios();
  int lengthMuns = listaMunicipios.size();
  int i = 0;
  String codMunicipios="";
  String descMunicipios="";
  for(i=0;i<lengthMuns-1;i++){
    GeneralValueObject municipios = (GeneralValueObject)listaMunicipios.get(i);
    codMunicipios+="\""+(String)municipios.getAtributo("codMunicipio")+"\",";
    descMunicipios+="\""+(String)municipios.getAtributo("nombreOficial")+"\",";
  }
  GeneralValueObject municipios = (GeneralValueObject)listaMunicipios.get(i);
  codMunicipios+="\""+(String)municipios.getAtributo("codMunicipio")+"\"";
  descMunicipios+="\""+(String)municipios.getAtributo("nombreOficial")+"\"";%>
  frame.codMunicipios = [<%=codMunicipios%>];
  frame.descMunicipios = [<%=descMunicipios%>];
  frame.cargarComboBox(frame.codMunicipios,frame.descMunicipios);
}
function cargarListaVias(){

<%
  Vector listaVias = mantForm.getListaVias();
        int numPaginaE = mantForm.getPaginaListadoE();
        int numLineasPaginaE = mantForm.getNumLineasPaginaListadoE();
        int iniE= (numPaginaE -1)*numLineasPaginaE;
        int finE= iniE+numLineasPaginaE;

  int lengthVias = listaVias.size();
        if (lengthVias < finE) finE = lengthVias;
  String viasOriginal="";
  if(lengthVias>0){
          for(i=iniE; i<finE-1; i++){
      GeneralValueObject vias = (GeneralValueObject)listaVias.get(i);
            viasOriginal+="[\""+vias.getAtributo("idVia")+"\","+
              "\""+vias.getAtributo("codVia")+"\","+
              "\""+vias.getAtributo("descVia")+"\","+
              "\""+vias.getAtributo("nombreCorto")+"\","+
              "\""+vias.getAtributo("codTipoVia")+"\","+
              "\""+vias.getAtributo("descTipoVia")+"\"],";
    }
    GeneralValueObject vias = (GeneralValueObject)listaVias.get(i);
          viasOriginal+="[\""+vias.getAtributo("idVia")+"\","+
            "\""+vias.getAtributo("codVia")+"\","+
            "\""+vias.getAtributo("descVia")+"\","+
            "\""+vias.getAtributo("nombreCorto")+"\","+
            "\""+vias.getAtributo("codTipoVia")+"\","+
            "\""+vias.getAtributo("descTipoVia")+"\"]";
        }
        %>
  frame.listaViasOriginal = [<%=viasOriginal%>];
            frame.numViasBuscadas = <%=lengthVias%>;
            var numPaginaE = "<%= numPaginaE%>";
            frame.actualizarListaVias(numPaginaE);
}
if(operacion=="CORRECTO"){
    jsp_alerta("A","<%=descriptor.getDescripcion("msjModRealizada")%>");
}else if(operacion=="ERROR"){
    jsp_alerta("A","<%=descriptor.getDescripcion("msjModNonRealizada")%>");
}
if(opcion=="cargarMunicipios"){
    cargarListaMunicipios();
}else{
    cargarListaVias();
}
</script></head><body></body></html>
