<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.FichaRelacionExpedientesForm"%>

<%
    int idioma=1;
    int apl=4;
    UsuarioValueObject usuario=new UsuarioValueObject();;
    FichaRelacionExpedientesForm fichaRelExpForm=new FichaRelacionExpedientesForm();

    if (session!=null){
      if (usuario!=null) {
        usuario = (UsuarioValueObject)session.getAttribute("usuario");
        fichaRelExpForm = (FichaRelacionExpedientesForm)session.getAttribute("FichaRelacionExpedientesForm");
        idioma = usuario.getIdioma();
        apl = usuario.getAppCod();
      }
  }
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<html:html>
 <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title>::: Tramites Disponibles:::</title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

    
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/xtree.js'/>"></script>
    <script type="text/javascript">
    <%
        GeneralValueObject relacionVO = (GeneralValueObject)fichaRelExpForm.getFichaRelExpVO();
    %>
        var listaTramitesDisponibles= new Array();
    <%
        Vector tramitesDisponibles = null;
        tramitesDisponibles = (Vector) fichaRelExpForm.getTramitesDisponibles();
    %>

    var listaTramitesOriginal = new Array();

      function inicializar(){
        var argVentana = self.parent.opener.xanelaAuxiliarArgs;
        listaTramitesOriginal = argVentana;
        with (document.forms[0]) {
          codMunicipio.value = '<%=(String)relacionVO.getAtributo("codMunicipio")%>';
          codProcedimiento.value = '<%=(String)relacionVO.getAtributo("codProcedimiento")%>';
          ejercicio.value ='<%=(String)relacionVO.getAtributo("ejercicio")%>';
          numero.value ='<%=(String)relacionVO.getAtributo("numero")%>';
          numeroRelacion.value ='<%=(String)relacionVO.getAtributo("numeroRelacion")%>';
        }
        if (tree.childNodes.length <= 0) {
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjTramDisp")%>');
        }

      }

      var tree;
      var nodosTramites = new Array();

      function pulsarIniciar() {
        if (tree.getSelected()) {
          if (tree.getSelected() != tree) {
            if (tree.getSelected().parentNode != tree) { // No clasificacion
              iniciarTramite(tree.getSelected().id);
            }
          }
        }
      }

      function iniciarTramite(id) {
        document.forms[0].listaCodTramites.value = listaTramitesDisponibles[id][0];
        var existe = "no";
        for(var i=0;i<listaTramitesOriginal.length;i++) {
          if(listaTramitesDisponibles[id][0] == listaTramitesOriginal[i][1]) {
            if(listaTramitesOriginal[i][4] == "") {
              existe = "si";
            }
          }
        }
        if(existe == "no") {
            document.forms[0].codUORTramiteManual.value = listaTramitesDisponibles[id][3];
            if(listaTramitesDisponibles[id][3] == "-99998") {
                var source = "<c:url value='/sge/FichaRelacionExpedientes.do?opcion=listaUnidadesUsuario'/>";
                abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'',
                        'width=800,height=350,status='+ '<%=statusBar%>',function(datosRespuesta){
                                if(datosRespuesta!=undefined) {
                                    document.forms[0].codUnidadTramitadoraUsu.value = datosRespuesta;
                                    document.forms[0].opcion.value="iniciarTramitesManual";
                                    document.forms[0].target="oculto";
                                    document.forms[0].action="<c:url value='/sge/TramitacionRelacionExpedientes.do'/>";
                                    document.forms[0].submit();
                                }
                        });
          } else {
            document.forms[0].opcion.value="iniciarTramitesManual";
            document.forms[0].target="oculto";
            document.forms[0].action="<c:url value='/sge/TramitacionRelacionExpedientes.do'/>";
            document.forms[0].submit();
          }
        } else { 
          jsp_alerta("A",'<%=descriptor.getDescripcion("msjEseTramEstaInic")%>');
        }
      }

      function tramiteIniciado(listaTramites,notifRealizada) {
        if (notifRealizada=="no"){
            msnj = "<%=descriptor.getDescripcion("msjNotifNoRealiz")%>";
            jsp_alerta("A",msnj);
        }
        pulsarCerrar(listaTramites);
      }

    function tramitesPendientes(listaTramitesPendientes) {
        var source = "<c:url value='/jsp/sge/informacionTramites.jsp?opcion=null'/>";
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,listaTramitesPendientes,
	'width=350,height=500,status='+ '<%=statusBar%>',function(datosConsulta){
                            if(datosConsulta!=undefined){
                            }
                    });
    }

      function pulsarCancelar(){
        pulsarCerrar();
      }

      function pulsarCerrar(datos){
        self.parent.opener.retornoXanelaAuxiliar(datos);
      }

      function checkKeysLocal(evento,tecla){
          if(window.event) evento = window.event;
          keyDel(evento);
      }

      function actualizarEstilosUltimaBusqueda(){
      }
      
    </script>
 </head>

 <body class="bandaBody" onload="javascript:{ pleaseWait('off'); inicializar();}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

    <html:form action="/sge/FichaRelacionExpedientes.do" method="POST">

    <html:hidden  property="opcion" value=""/>
    <html:hidden  property="codMunicipio" />
    <html:hidden  property="codProcedimiento" />
    <html:hidden  property="ejercicio" />
    <html:hidden  property="numero" />
    <html:hidden  property="numeroRelacion" />
    <html:hidden  property="listaCodTramites" value=""/>
    <html:hidden  property="codUORTramiteManual" value=""/>


<input type="Hidden" name="codUnidadTramitadoraUsu" value="">

<div class="txttitblanco"><%=descriptor.getDescripcion("titTramitesDisp")%></div>

<div class="contenidoPantalla">

    <table width="100%" cellspacing="0px" cellpadding="0px" border="0px">
        <tr>
           <td style="width: 100%" valign="top">	
                <div style="max-height:300px;overflow:auto;">	
                    <script type="text/javascript">
                                                tree = new WebFXTree('Clasificación de trámites');
                                                var i;
                                                var clases = new Array();
                                                var tramite;

                                    <%
if (tramitesDisponibles != null) {
int lengthTD = tramitesDisponibles.size();
for (int i = 0; i < lengthTD; i++) {
String clase = (String) ((GeneralValueObject) tramitesDisponibles.get(i)).getAtributo("clasificacion");
String tramite = (String) ((GeneralValueObject) tramitesDisponibles.get(i)).getAtributo("tramite");
                                    %>
                                        clasePadre = clases['<%=clase%>'];
                                            if (clasePadre == undefined) {
                                                clasePadre = new WebFXTreeItem('<%=clase%>');
                                                    tree.add(clasePadre);
                                                    clases['<%=clase%>']=clasePadre;
                                                    }
                                                    tramite = new WebFXTreeItem('<%=tramite%>');
                                                        clasePadre.add(tramite);
                                                        listaTramitesDisponibles[tramite.id] =  new Array('<%=(String) ((GeneralValueObject) tramitesDisponibles.get(i)).getAtributo("codTramite")%>',
                                                            '<%=tramite%>','<%=clase%>','<%=(String) ((GeneralValueObject) tramitesDisponibles.get(i)).getAtributo("codUnidadInicio")%>');
                                    <%
}
}
                                    %>
                                        document.write(tree);
                                        tree.expandAll();
                                            </script>
                                        </div>
                                    </td>
                                </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type="button" Class="botonLargo" value='<%=descriptor.getDescripcion("bIniciarTram")%>' name="cmdIniciar" onclick="pulsarIniciar();return false;" accesskey='I'>
        <input type="button" Class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelar"  onclick="pulsarCancelar();return false;" accesskey='C'>
    </div>        
        </div>
</html:form>
</body>
</html:html>
