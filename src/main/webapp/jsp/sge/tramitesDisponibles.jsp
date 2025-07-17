<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm"%>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<%
    int idioma=1;
    int apl=4;
    UsuarioValueObject usuario=new UsuarioValueObject();
    FichaExpedienteForm expForm=new FichaExpedienteForm();

    if (session!=null){
      if (usuario!=null) {
        usuario = (UsuarioValueObject)session.getAttribute("usuario");
        expForm = (FichaExpedienteForm)session.getAttribute("FichaExpedienteForm");
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
    <script type="text/javascript" src="<c:url value='/scripts/json2.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script src="<c:url value='/scripts/listaComboBox.js'/>"></script>
	<script src="<c:url value='/scripts/xtree.js'/>"></script>
    <script type="text/javascript">
    <%
        GeneralValueObject expedienteVO = (GeneralValueObject)expForm.getExpedienteVO();
    %>
        var listaTramitesDisponibles= new Array();
    <%
        Vector tramitesDisponibles = null;
        tramitesDisponibles = (Vector) expForm.getTramitesDisponibles();
    %>

    
    var listaTramitesOriginal = new Array();

      function inicializar(){
        var argVentana = self.parent.opener.xanelaAuxiliarArgs;
        listaTramitesOriginal = argVentana;

        with (document.forms[0]) {
          codMunicipio.value = '<%=(String)expedienteVO.getAtributo("codMunicipio")%>';
          codProcedimiento.value = '<%=(String)expedienteVO.getAtributo("codProcedimiento")%>';
          ejercicio.value ='<%=(String)expedienteVO.getAtributo("ejercicio")%>';
          numero.value ='<%=(String)expedienteVO.getAtributo("numero")%>';
        }
        if (tree.childNodes.length <= 0) {
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjTramDisp")%>');
        }

      }

      var tree;
      var nodosTramites = new Array();

      function pulsarIniciar() {
        document.forms[0].cmdIniciar.disabled = true ;       
        if (tree.getSelected()) {
          if (tree.getSelected() != tree) {
            if (tree.getSelected().parentNode != tree) { // No clasificacion
              previoIniciarTramite(tree.getSelected().id);
            }
          }
        }
      }

      function previoIniciarTramite(id) { 
        document.forms[0].listaCodTramites.value = listaTramitesDisponibles[id][0];
        var existe = "no";
        if(listaTramitesOriginal != null){
            for(var i=0;i<listaTramitesOriginal.length;i++) {
              if(listaTramitesDisponibles[id][0] == listaTramitesOriginal[i][1]) {
                if(listaTramitesOriginal[i][4] == "") {
                  existe = "si";
                }
              }
            }
        }

        
        if(existe == "no") {
          comprobarPermisoUsuario(id);
        } else {
          jsp_alerta("A",'<%=descriptor.getDescripcion("msjEseTramEstaInic")%>');
          document.forms[0].cmdIniciar.disabled = false ;
        }
      }
      
      function iniciarTramite(id){
        if(listaTramitesDisponibles[id][3] == "-99998") {
            var source = "<c:url value='/sge/FichaExpediente.do?opcion=listaUnidadesUsuario'/>";
            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'',
                'width=450,height=520,scrollbars=no,status='+ '<%=statusBar%>',function(datosRespuesta){
                if(datosRespuesta!=undefined) {
                    document.forms[0].codUnidadTramitadoraUsu.value = datosRespuesta;
                    document.forms[0].codUnidadTramitadoraManual.value = datosRespuesta;
                    document.forms[0].opcion.value="iniciarTramitesManual";
                    document.forms[0].target="oculto";
                    document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
                    document.forms[0].submit();
                }
            });
          } else {
                document.forms[0].codUnidadTramitadoraManual.value = listaTramitesDisponibles[id][3];
                document.forms[0].opcion.value="iniciarTramitesManual";
                document.forms[0].target="oculto";
                document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
                document.forms[0].submit();
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
                            document.forms[0].cmdIniciar.disabled = false ;
                    });
    }

      function pulsarCancelar(){
        pulsarCerrar();
      }

      function pulsarCerrar(datos){
        self.parent.opener.retornoXanelaAuxiliar(datos);
      }// pulsarCerrar

      function checkKeysLocal(evento, tecla){
          var aux=null;
        if(window.event)
            aux = window.event;
        else
            aux = evento;

        var tecla = 0;
        if(aux.keyCode)
            tecla = aux.keyCode;
        else
            tecla = aux.which;

      
        keyDel(aux);
      }

      function actualizarEstilosUltimaBusqueda(){      
      }
      
      function comprobarPermisoUsuario(id){
        var datos = {
            'codOrganizacion': '<%=(String)expedienteVO.getAtributo("codMunicipio")%>',
            'codProcedimiento': '<%=(String)expedienteVO.getAtributo("codProcedimiento")%>',
            'ejercicio': '<%=(String)expedienteVO.getAtributo("ejercicio")%>',
            'numExpediente': '<%=(String)expedienteVO.getAtributo("numero")%>',
            'codTramite': listaTramitesDisponibles[id][0],
            'posicion': id,
            'opcion': 'comprobarPermisoUsuario'  
        };
        pleaseWait('on');
        try{
            $.ajax({
                url: '<c:url value='/sge/TramitacionExpedientes.do'/>',
                type: 'POST',
                async: true,
                data: datos,
                success: procesarRespuestaComprobarCargoTramite,
                error: muestraErrorRespuestaComprobarCargoTramite
            });           
       }catch(Err){
            muestraErrorRespuestaComprobarCargoTramite();
       }
       
       function procesarRespuestaComprobarCargoTramite(ajaxResult){
           var datos = JSON.parse(ajaxResult);
           if(datos.status == 0){
               console.log(datos.descStatus);
               var resultado = datos.resultado.tabla;
               iniciarTramite(resultado.posicion);
           } else {
               pleaseWait('off');
               jsp_alerta("A",datos.descStatus);
               document.forms[0].cmdIniciar.disabled = false;
           }
       }
       
       function muestraErrorRespuestaComprobarCargoTramite(){
           pleaseWait('off');
            jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
       }
    }
    </script>
 </head>

 <body class="bandaBody" onload="javascript:{ pleaseWait('off'); inicializar();}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

    <html:form action="/sge/FichaExpediente.do" method="POST">

    <html:hidden  property="opcion" value=""/>
    <html:hidden  property="codMunicipio" />
    <html:hidden  property="codProcedimiento" />
    <html:hidden  property="ejercicio" />
    <html:hidden  property="numero" />
    <html:hidden  property="listaCodTramites" value=""/>

    <input type="hidden" name="codUnidadTramitadoraUsu" value="">
    <input type="hidden" name="codUnidadTramitadoraManual" value="">

    <div class="txttitblanco"><%=descriptor.getDescripcion("titTramitesDisp")%></div>
    <div class="contenidoPantalla">
        <table width="100%">
            <tr>
                <td style="width: 100%" valign="top">
                     <div style="max-height:300px;overflow:auto;" class="webfx-tree-div">
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
