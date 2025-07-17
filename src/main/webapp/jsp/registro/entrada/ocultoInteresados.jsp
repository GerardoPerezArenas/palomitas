<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map,
                                                                            java.util.Vector" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.InteresadosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>

<%
UsuarioValueObject usuarioVO = new UsuarioValueObject();
int idioma=1;
int apl=1;

if (session.getAttribute("usuario") != null){
  usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    idioma = usuarioVO.getIdioma();
    apl = usuarioVO.getAppCod();
}
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<html>
 <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title> Oculto Consulta Expedientes pendientes por procedimiento</title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>

    <script>
      var listaTabla = new Array();
      var listaInteresados = new Array();

    //  var frame = parent.mainFrame;
        <%
        Vector listaInteresados = new Vector();
        InteresadosForm intForm =(InteresadosForm)session.getAttribute("InteresadosForm");
        listaInteresados = (Vector)intForm.getListaInteresados();
        int lengthInteresados = listaInteresados.size();
        int i = 0;
        %>
        var j=0;
        <%
        for(i=0;i<lengthInteresados;i++){
        %>
          var rol = "Secundario";
          if('<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("rol")%>'=='1')
            rol = "Principal";
          else if('<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("rol")%>'=='2')
            rol = "Representante";

          listaTabla[j] = [	'<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("titular")%>',
                    '<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("descDomicilio")%>',rol];
          listaInteresados[j] = ['<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("codigoTercero")%>',
                        '<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("versionTercero")%>',
                        '<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("titular")%>',
                        '<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("rol")%>',
                        '<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("domicilio")%>',
                        '<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("descDomicilio")%>',];
          j++;
        <%
        }
        %>
      var frame = parent.mainFrame;
      frame.grabarInteresados();
    </script>
 </head>
 <body>
 </body>
</html>
