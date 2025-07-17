<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.escritorio.RegistroUsuarioValueObject" %>
<%@page import="es.altia.agora.business.sge.DefinicionProcedimientosValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.DefinicionProcedimientosForm" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%@page import="java.util.Vector" %>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title> Oculto cargar pagina </title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <%UsuarioValueObject usuarioVO = new UsuarioValueObject();
      RegistroUsuarioValueObject regUsuarioVO = new RegistroUsuarioValueObject();
      int idioma=1;
      int apl=1;
      if ((session.getAttribute("usuario") != null) && (session.getAttribute("registroUsuario") != null)){
        usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        regUsuarioVO = (RegistroUsuarioValueObject)session.getAttribute("registroUsuario");
        idioma = usuarioVO.getIdioma();
        apl = usuarioVO.getAppCod();
      }

      Log log = LogFactory.getLog(this.getClass());
    %>

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

    <script>
    function redirecciona() {
    <% /* Recuperar el vector de anotaciones de la sesion. */
       DefinicionProcedimientosForm f= (DefinicionProcedimientosForm)  session.getAttribute("DefinicionProcedimientosForm");
       DefinicionProcedimientosValueObject arVO = f.getDefinicionProcedimientos();

       Vector relacionProcedimientos = (Vector) session.getAttribute("RelacionProcedimientos");
       int numRelacionProcedimientos = relacionProcedimientos.size();
       int posProcedimiento = Integer.parseInt(arVO.getPosicionProcedimiento());
       String cMunic= arVO.getCodMunicipio();
       String cProc= arVO.getTxtCodigo();
      
       int pos=0;
       if (posProcedimiento == -1) {
        if ( (cMunic !=null) && (cProc !=null) ){
          DefinicionProcedimientosValueObject dFVO = new DefinicionProcedimientosValueObject();
          boolean encontrada=false;
          while (!encontrada && (pos<relacionProcedimientos.size()))
          {
            dFVO = (DefinicionProcedimientosValueObject) relacionProcedimientos.elementAt(pos);
            String m = dFVO.getCodMunicipio();
            String p = dFVO.getTxtCodigo();            
            if ( m.equals(cMunic) && p.equals(cProc) ) {
              encontrada=true;
            }
            else pos++;
          }       
          if (!encontrada) pos=0;
        }
       } else {
         pos = posProcedimiento-1;
       }
              
       DefinicionProcedimientosValueObject cp_procedimiento = (DefinicionProcedimientosValueObject) relacionProcedimientos.elementAt(pos);
    %>
       if (parent.mainFrame.numProcedimientos != 1) {
        parent.mainFrame.numProcedimientos = <%=numRelacionProcedimientos%>;
       } else {
        parent.mainFrame.numProcedimientos = -1;
       }
       parent.mainFrame.codMunic = <%=cp_procedimiento.getCodMunicipio()%>;
       parent.mainFrame.codProc = "<%=cp_procedimiento.getTxtCodigo()%>";       
       parent.mainFrame.actualizaAnotacionProcedimientoRealImportacionNavegacion(<%=pos+1%>);
    }
    </script>
</head>
<body onLoad="redirecciona();">
    <p>&nbsp;<p><center>
</body>
</html>