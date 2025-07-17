<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.sge.DefinicionProcedimientosValueObject" %>
<%@page import="es.altia.agora.business.sge.DefinicionTramitesValueObject" %>
<%@page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title> Oculto Definicion Procedimientos </title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <%
            UsuarioValueObject usuarioVO = null;
            int idioma=1;
            int apl=4;

            if (session.getAttribute("usuario") != null){
                    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
                    idioma = usuarioVO.getIdioma();
                    apl = usuarioVO.getAppCod();
            }
            DefinicionProcedimientosValueObject pVO = (DefinicionProcedimientosValueObject) session.getAttribute("CatalogoValueObject");
            DefinicionTramitesValueObject tVO = null;
            Vector tramites = pVO.getTramites();
    %>

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

    <script>
      function redirecciona(){
        var listaTramites  = new Array();
        var i=0;
        <%
            for (int i=0; i<tramites.size(); i++) {
              tVO = (DefinicionTramitesValueObject) tramites.elementAt(i);
              if ( (tVO.getCodMunicipio() != null) && ( tVO.getTxtCodigo() !=null) && (tVO.getCodigoTramite() !=  null)
                && (tVO.getNumeroTramite() != null) && (tVO.getNombreTramite() != null) && (tVO.getCodClasifTramite() != null)
                && (tVO.getDescClasifTramite() != null) ) {
        %>
          var condEntrada = new Array();
          var condSalidaF = new Array();
          var condSalidaDF = new Array();

          var tipoCondSalida = '<%= tVO.getTipoCondicion() %>';
          var tipoListaF = '<%= tVO.getObligatorio() %>';
          var tipoListaDF = '<%= tVO.getObligatorioDesf()%>';

          if (tipoCondSalida=='') tipoCondSalida='Vacia';
          else if (tipoCondSalida=='Tramite') {
            <%
                Vector lt = (Vector) tVO.getListaTramitesFavorable();
                if (lt.size() > 0 ) {
                DefinicionTramitesValueObject tVO2 = (DefinicionTramitesValueObject) lt.elementAt(0);
            %>
                condSalidaF = [ ['<%= tVO.getCodMunicipio() %>'
                              , '<%= tVO2.getCodTramiteFlujoSalida() %>'
                              , '<%= tVO2.getNombreTramiteFlujoSalida() %>']
            <%
                for (int j=1; j < lt.size(); j++) {
                  tVO2 = (DefinicionTramitesValueObject) lt.elementAt(j);
            %>
                             , ['<%= tVO.getCodMunicipio() %>'
                              , '<%= tVO2.getCodTramiteFlujoSalida() %>'
                              , '<%= tVO2.getNombreTramiteFlujoSalida() %>']
            <%  } %>
                            ];
            <% } %>

          }else  if (tipoCondSalida=='Pregunta' || tipoCondSalida=='Resolucion') {

            var tipoFavorableSI = '<%= tVO.getTipoFavorableSI() %>';
            var tipoFavorableNO = '<%= tVO.getTipoFavorableNO() %>';

            if (tipoFavorableSI=='TramiteSI') {
              <%
                Vector ltf = (Vector) tVO.getListaTramitesFavorable();
                if (ltf.size() > 0 ) {
                  DefinicionTramitesValueObject tVO2 = (DefinicionTramitesValueObject) ltf.elementAt(0);
              %>
                  condSalidaF= [ [ '<%= tVO.getCodMunicipio() %>'
                                 , '<%= tVO2.getCodTramiteFlujoSalida() %>'
                                 , '<%= tVO2.getNombreTramiteFlujoSalida() %>']
              <%
                  for (int j=1; j < ltf.size(); j++) {
                      tVO2 = (DefinicionTramitesValueObject) ltf.elementAt(j);
              %>
                            , [ '<%= tVO.getCodMunicipio() %>'
                            , '<%= tVO2.getCodTramiteFlujoSalida() %>'
                            , '<%= tVO2.getNombreTramiteFlujoSalida() %>']
              <% } %>
                            ];
              <% } %>
            }

            if (tipoFavorableNO=='TramiteNO') {
                <%
                  Vector ltdf = (Vector) tVO.getListaTramitesDesfavorable();
                  if (ltdf.size() > 0 ) {
                    DefinicionTramitesValueObject tVO2 = (DefinicionTramitesValueObject) ltdf.elementAt(0);
                %>
                    condSalidaDF=[ [ '<%= tVO.getCodMunicipio() %>'
                            , '<%= tVO2.getCodTramiteFlujoSalida() %>'
                            , '<%= tVO2.getNombreTramiteFlujoSalida() %>']
                <%
                    for (int j=1; j < ltdf.size(); j++) {
                      tVO2 = (DefinicionTramitesValueObject) ltdf.elementAt(j);
                %>
                            , [ '<%= tVO.getCodMunicipio() %>'
                            , '<%= tVO2.getCodTramiteFlujoSalida() %>'
                            , '<%= tVO2.getNombreTramiteFlujoSalida() %>']
                <% } %>
                      ];
                <% } %>

            }

          }

          condEntrada = new Array();
          <%
              Vector lte = (Vector) tVO.getListasCondEntrada();
              if (lte.size() > 0 ) {
                DefinicionTramitesValueObject tVO2 = (DefinicionTramitesValueObject) lte.elementAt(0);
          %>
                condEntrada=[ [ '<%= tVO.getCodMunicipio() %>'
                            ,'<%= tVO2.getIdTramiteCondEntrada() %>'
                            ,'<%= tVO2.getCodTramiteCondEntrada() %>'
                            ,'<%= tVO2.getDescTramiteCondEntrada() %>'
                            ,'<%= tVO2.getEstadoTramiteCondEntrada() %>'
                            ,'<%= tVO2.getTipoCondEntrada() %>'
                            ,"<%= tVO2.getExpresionCondEntrada()%>"]

          <%
                for (int j=1; j < lte.size(); j++) {
                  tVO2 = (DefinicionTramitesValueObject) lte.elementAt(j);
          %>
                          , [ '<%= tVO.getCodMunicipio() %>'
                            ,'<%= tVO2.getIdTramiteCondEntrada() %>'
                            ,'<%= tVO2.getCodTramiteCondEntrada() %>'
                            ,'<%= tVO2.getDescTramiteCondEntrada() %>'
                            ,'<%= tVO2.getEstadoTramiteCondEntrada() %>'
                            ,'<%= tVO2.getTipoCondEntrada() %>'
                            ,"<%= tVO2.getExpresionCondEntrada()%>"]
          <% }  %>
                            ];
          <% } %>

          listaTramites[i] = ['<%= tVO.getCodMunicipio() %>'
                            ,'<%= tVO.getTxtCodigo() %>'
                            ,'<%= tVO.getCodigoTramite() %>'
                            ,'<%= tVO.getNumeroTramite() %>'
                            ,'<%= tVO.getNombreTramite() %>'
                            ,'<%= tVO.getCodClasifTramite() %>'
                            ,'<%= tVO.getDescClasifTramite() %>'
                            ,tipoCondSalida
                            ,tipoFavorableSI
                            ,tipoFavorableNO
                            , condEntrada
                            , condSalidaF
                            , condSalidaDF
                            , '<%= tVO.getTexto() %>'
                            , (tipoListaF=='0')?'OPCIONAL':(tipoListaF=='1')?'OBLIGATORIO':(tipoListaF=='2')?'EXCLUYENTE':''
                            , (tipoListaDF=='0')?'OPCIONAL':(tipoListaDF=='1')?'OBLIGATORIO':(tipoListaDF=='2')?'EXCLUYENTE':''];

          //listaTramites[i][listaTramites[i].length]= condEntrada;
          i=i+1;
        <%  }
            } %>
        parent.mainFrame.recuperaTramitesProcedimientos(listaTramites);
      }
    </script>
</head>
<body onLoad="redirecciona();"></body>
</html>
