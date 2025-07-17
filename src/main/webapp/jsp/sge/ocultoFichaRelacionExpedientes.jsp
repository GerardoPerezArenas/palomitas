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
<%@page import="java.util.ArrayList"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionExpedientesForm" %>
<%@page import="es.altia.agora.business.sge.TramitacionExpedientesValueObject" %>
<%@ page import="es.altia.agora.interfaces.user.web.sge.FichaRelacionExpedientesForm"%>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title> Oculto Ficha Expediente </title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

    <%int idioma=1;
    int apl=4;
    UsuarioValueObject usuarioVO = new UsuarioValueObject();
    FichaRelacionExpedientesForm relExpForm=new FichaRelacionExpedientesForm();

    if (session.getAttribute("usuario") != null){
        usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        relExpForm = (FichaRelacionExpedientesForm)session.getAttribute("FichaRelacionExpedientesForm");
        idioma = usuarioVO.getIdioma();
        apl = usuarioVO.getAppCod();
    }

    String mensajeTramiteFinalizadoConTramitesAbiertos ="";
    String mensajeTramiteOrigenConTramitesAbiertos ="";
 %>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
             type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />


<%
        if(relExpForm.getRespOpcion()=="tramFinalizadoConTramitesPosteriores")
         {
            ArrayList<TramitacionExpedientesValueObject> tramitesDestino = relExpForm.getTramitesDestino();
            mensajeTramiteFinalizadoConTramitesAbiertos = descriptor.getDescripcion("etiq_notRetrocesoTramAbiertos") + "<br/>";
            for(int i=0;tramitesDestino!=null && i<tramitesDestino.size();i++){
                TramitacionExpedientesValueObject destino = tramitesDestino.get(i);
                String unidad = destino.getUnidadTramitadora();
                String descripcion = destino.getDescripcionTramiteFlujoSalida();
                String msgTramite = "<li>" + descriptor.getDescripcion("etiqTramite") + " " + descripcion + " " + descriptor.getDescripcion("etiq_conUnidad") + " " + unidad + "</li><br/>";
                mensajeTramiteFinalizadoConTramitesAbiertos += msgTramite;

            }// for
        }// if

        if(relExpForm.getRespOpcion()=="tramiteOrigenConTramitesAbiertos")
        {
            ArrayList<TramitacionExpedientesValueObject> tramitesDestino = relExpForm.getTramitesDestino();
            if(tramitesDestino!=null && tramitesDestino.size()>0){
                mensajeTramiteOrigenConTramitesAbiertos = descriptor.getDescripcion("etiq_notRetrocesoTramOrigen1") + " " + tramitesDestino.get(0).getTramite() + "  " + descriptor.getDescripcion("etiq_notRetrocesoTramOrigen2") + "<br/>";
                for(int i=0;tramitesDestino!=null && i<tramitesDestino.size();i++){
                    TramitacionExpedientesValueObject destino = tramitesDestino.get(i);
                    String unidad = destino.getUnidadTramitadora();
                    String descripcion = destino.getDescripcionTramiteFlujoSalida();
                    String msgTramite = "<li>" + descriptor.getDescripcion("etiqTramite") + " " + descripcion + " " + descriptor.getDescripcion("etiq_conUnidad") + " " + unidad + "</li><br/>";
                    mensajeTramiteOrigenConTramitesAbiertos += msgTramite;

                }// for
            }//if
        }// if
%>

    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script>

      var listaTramites = new Array();
      var listaTramitesOriginal = new Array();
      var listaOriginal = new Array();
      var lista = new Array();
      var frame = parent.mainFrame;

      function redirecciona(){
        var respOpcion ='<bean:write name="FichaRelacionExpedientesForm" property="respOpcion"/>';


        if ("tramFinalizadoConTramitesPosteriores"==respOpcion){
            var msnj = "<%=mensajeTramiteFinalizadoConTramitesAbiertos%>";
            jsp_alerta("A", msnj);
        }else       
        if ( ("noGrabado"==respOpcion) || ("grabado"==respOpcion) ){
        <%
          Vector listaDocumentos= new Vector();
          listaDocumentos = (Vector) relExpForm.getDocumentos();
          String checkEntreg1 = "<input type='checkbox' class='check' name='documentoEntregado";
          String checkEntreg2 = "' value='SI'";
          String checkEntreg3 =" CHECKED ";
          String checkEntreg4 = ">";

          String entreg;

          if ( listaDocumentos != null ) {
            for (int i=0; i< listaDocumentos.size(); i++ ) {
              GeneralValueObject gVO = (GeneralValueObject) listaDocumentos.elementAt(i);
              String cD = (String) gVO.getAtributo("codigo");
              String nD = (String) gVO.getAtributo("nombre");
              String condD = (String) gVO.getAtributo("condicion");
              String entregado =(String) gVO.getAtributo("ENTREGADO");

              entreg = checkEntreg1+i+checkEntreg2;
              if ("SI".equals(entregado)) entreg += checkEntreg3;
              entreg += checkEntreg4;%>
              listaOriginal[<%= i %>]  = ['<%= cD %>','<%= nD %>','<%= condD %>'];
              lista[<%= i %>]  = ["<%=entreg%>",'<%= nD %>','<%= condD %>'];
              <%}
          }%>
          parent.mainFrame.grabacionRelacionExpedientes(respOpcion,listaOriginal,lista);
        } else if ("noFinalizado"==respOpcion) {
              msnj = "<%=descriptor.getDescripcion("msjTramNoRetrocedido")%>";
              jsp_alerta("A",msnj);
        } else {
             if("tramiteOrigenConTramitesAbiertos"  ==respOpcion){
                var msnj = "<%=mensajeTramiteOrigenConTramitesAbiertos%>";
                jsp_alerta("A", msnj);
           }
          <%Vector tramites = new Vector();
          tramites = (Vector) relExpForm.getTramites();
          if (tramites != null) {
            int lengthTramites = tramites.size();%>
            var j=0;
            <%for(int i=0;i<lengthTramites;i++){%>
                  listaTramites[j] = ['<%=((GeneralValueObject)tramites.get(i)).getAtributo("tramite")%>',
                        '<%=((GeneralValueObject)tramites.get(i)).getAtributo("fehcaInicio")%>',
                        '<%=((GeneralValueObject)tramites.get(i)).getAtributo("fechaFin")%>',
                        '<%=((GeneralValueObject)tramites.get(i)).getAtributo("unidad")%>',
                        '<%=((GeneralValueObject)tramites.get(i)).getAtributo("clasificacion")%>',
                        ''];
                  <%if (!((GeneralValueObject)tramites.get(i)).getAtributo("usuarioBloq").equals("")) {
                        if (((GeneralValueObject)tramites.get(i)).getAtributo("codUsuarioBloq").toString().equals(String.valueOf(usuarioVO.getIdUsuario()))) {%>
                            listaTramites[j][5]='<span class="fa fa-lock" alt="<%=((GeneralValueObject)tramites.get(i)).getAtributo("usuarioBloq")%>"></span>';
                    <%} else {%>
                            listaTramites[j][5]='<span class="fa fa-unlock" alt="<%=((GeneralValueObject)tramites.get(i)).getAtributo("usuarioBloq")%>"></span>':
                        <%}
                  }%>
                  listaTramitesOriginal[j] = ['<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("ocurrenciaTramite")%>',
                        '<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("codTramite")%>',
                        '<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("tramite")%>',
                        '<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("fehcaInicio")%>',
                        '<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("fechaFin")%>',
                        '<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("unidad")%>',
                        '<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("usuario")%>',
                        '<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("clasificacion")%>',
                        '<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("consultar")%>',
                        '<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("fueraDePlazo")%>',
                        '<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("codUniTramTramite")%>',
                        '<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("tramiteInicio")%>',
                        '<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("codUsuario")%>',
                        '<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("codUsuarioFinalizacion")%>',
                        '<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("usuarioBloq")%>',
                        '<%=(String)((GeneralValueObject)tramites.get(i)).getAtributo("codUsuarioBloq")%>'];
                  j++;
            <%}
          }%>
          frame.listaTramites = listaTramites;
          frame.listaTramitesOriginal = listaTramitesOriginal;
          frame.tabTramites.lineas = listaTramites;
          frame.tabTramites.displayTabla();
        }
      }
    </script>
</head>
<body onLoad="pleaseWait('off');redirecciona();">
    <form>
    <input type="hidden" name="opcion" value="">
    </form>
    <p>&nbsp;<p>
</body>
</html>
