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

<%@page import="java.util.Vector" %>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title> Oculto cargar pagina </title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <%
      UsuarioValueObject usuarioVO = new UsuarioValueObject();
      RegistroUsuarioValueObject regUsuarioVO = new RegistroUsuarioValueObject();
      int idioma=1;
      int apl=1;
      if ((session.getAttribute("usuario") != null) && (session.getAttribute("registroUsuario") != null)){
        usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        regUsuarioVO = (RegistroUsuarioValueObject)session.getAttribute("registroUsuario");
        idioma = usuarioVO.getIdioma();
        apl = usuarioVO.getAppCod();
      }
    %>

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

    <script>
    function redirecciona() {
    parent.mainFrame.listaOriginal = new Array();
    parent.mainFrame.lista= new Array();
    parent.mainFrame.listaSel = new Array();

    var asunto;
    var estado;

    <% /* Recuperar el vector de anotaciones de la sesion. */

       DefinicionProcedimientosForm f= (DefinicionProcedimientosForm)  session.getAttribute("DefinicionProcedimientosForm");
       DefinicionProcedimientosValueObject arVO = f.getDefinicionProcedimientos();
       int numPagina = Integer.parseInt(arVO.getPaginaListado());
       int numLineasPagina = Integer.parseInt(arVO.getNumLineasPaginaListado());
       int ini= (numPagina -1)*numLineasPagina;
       int fin= ini+numLineasPagina;
    %>
    <%
       Vector relacionProcedimientos = (Vector) session.getAttribute("RelacionProcedimientos");
       int numRelacionProcedimientos = relacionProcedimientos.size();

        if ( relacionProcedimientos != null ) {
         if (numRelacionProcedimientos < fin) fin=numRelacionProcedimientos;
         int j=0;
         for (int i=ini; i< fin; i++ ) {
          DefinicionProcedimientosValueObject proc = (DefinicionProcedimientosValueObject) relacionProcedimientos.elementAt(i);
          String cM = proc.getCodMunicipio();
          String dM = proc.getDescMunicipio();
          String cT = proc.getTxtCodigo();
          String dT = proc.getTxtDescripcion();
          String fL = proc.getFueraLimite();
    %>
       parent.mainFrame.listaOriginal[<%= j %>]  = ['<%= dM %>','<%= cT %>','<%= dT %>','<%= cM %>','<%= fL %>'];
       parent.mainFrame.lista[<%= j++ %>]  = ['<%= cT %>','<%= dT %>'];

    <% 	   }
            }

    %>
      var numPagina = '<%= numPagina%>';
      parent.mainFrame.listaSel =parent.mainFrame.lista;
      parent.mainFrame.inicializaLista(numPagina);
    }
    </script>
</head>
<body onLoad="redirecciona();">
    <p>&nbsp;<p><center>
</body>
</html>
