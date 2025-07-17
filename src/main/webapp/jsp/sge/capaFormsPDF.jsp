<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<%
    int idioma = 1;
    int apl = 4;
   
    if (session != null) {
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        if (usuario != null) {
            idioma = usuario.getIdioma();
            apl = usuario.getAppCod();
        }
    }
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<!-- CAPA 6: PESTAÑA DE FORMULARIOS PDF -->
<div class="tab-page" id="tabPage6">
<h2 class="tab"><%=descriptor.getDescripcion("etFichaFormularios")%></h2>
<script type="text/javascript">tp1_p6 = tp1.addTabPage( document.getElementById( "tabPage6" ) );</script>
<TABLE  width="100%">
 <TR>
   <TD id="tablaFormsPDF"></TD>
 </TR>
 <TR>
   <TD width="100%" align="center">
      <input type="button" name="cmdAltaForm" onclick="pulsarAltaFormPDF();" class="botonGeneral" accesskey="A" value=<%=descriptor.getDescripcion("gbAlta")%>>
      <input type="button" class="botonGeneral" accesskey="V" value="Ver" name="cmdVerForm" onclick="pulsarVerFormPDF();">
      <input type="button" class="botonGeneral" accesskey="M" name="cmdModificarForm" onclick="pulsarModificarFormPDF();" value=<%=descriptor.getDescripcion("gbModificar")%>>
      <input type="button" class="botonGeneral" accesskey="X" name="cmdVerAnexo" onclick="pulsarVerAnexoPDF();" value="<%=descriptor.getDescripcion("gbVerAnexo")%>">
      <input type="button" class="botonGeneral" accesskey="E" name="cmdEliminarForm" onclick="pulsarEliminarFormPDF();" value=<%=descriptor.getDescripcion("gbEliminar")%>>
   </TD>
 </TR>
</TABLE>
</div>
