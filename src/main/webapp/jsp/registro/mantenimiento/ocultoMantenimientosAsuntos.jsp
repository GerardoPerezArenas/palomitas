<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<html>
<head>
<title> Oculto mantenimiento asuntos </title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<%
            int idioma = 1;
            int apl = 1;
            String css = "";
            if (session != null && session.getAttribute("usuario") != null) {
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    apl = usuario.getAppCod();
                    css = usuario.getCss();
                }
            }
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  
                     type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        
<script type="text/javascript">

function redirecciona() {
    var opcion = '<bean:write name="MantAsuntosForm" property="opcion"/>';
    if (opcion=="listaProcedimientos"){
	    mun_procs = new Array();
	    cod_procs =  new Array();
	    desc_procs = new Array();  
	    var i = 0;
	    <logic:present name="MantAsuntosForm" property="procedimientos">
	      <logic:iterate id="procedimiento" name="MantAsuntosForm" property="procedimientos">
	         mun_procs[i]='<bean:write name="procedimiento" property="codMunicipio"/>';
	         cod_procs[i]='<bean:write name="procedimiento" property="txtCodigo"/>';
	         desc_procs[i]="<str:escape><bean:write name="procedimiento" property="txtDescripcion" filter="false"/></str:escape>";
	         i++;
	      </logic:iterate>
	    </logic:present>
        parent.mainFrame.recuperaListaProcedimientos(mun_procs, cod_procs, desc_procs);
        
    } else if (opcion=="eliminar"){
        var lista_claves = new Array();  
        var lista = new Array();
        var i = 0;
       <logic:present name="MantAsuntosForm" property="asuntos">
          <logic:iterate id="asunto" name="MantAsuntosForm" property="asuntos">
           lista_claves[i] = ['<bean:write name="asunto" property="codigo" />',
                              '<bean:write name="asunto" property="unidadRegistro" />',
                              '<bean:write name="asunto" property="tipoRegistro"/>'];
           
           var descripcionUor = "<str:escape><bean:write name="asunto" property="descUor" filter="false"/></str:escape>";
           if (descripcionUor == null || descripcionUor == 'null' || descripcionUor == '') 
               descripcionUor = "<str:escape><%=descriptor.getDescripcion("etiq_TodosRegistros")%></str:escape>";
                              
           lista[i] = ['<bean:write name="asunto" property="codigo" />', 
                       "<str:escape><bean:write name="asunto" property="descripcion" filter="false"/></str:escape>",
                       descripcionUor,
                       '<bean:write name="asunto" property="tipoRegistro"/>',	
                     '<bean:write name="asunto" property="procedimiento"/>',	
                     '<bean:write name="asunto" property="desProcedimiento"/>',	
                     '<bean:write name="asunto" property="asuntoBaja"/>'];
           i++;
          </logic:iterate>
        </logic:present>
        parent.mainFrame.recuperaListaAsuntos(lista, lista_claves);
        
    } else if (opcion=="cargarProcedimiento"){
        var listaDocs = new Array();
        var i=0;
        <logic:iterate id="doc" name="MantAsuntosForm" property="listaDocs">
          listaDocs[i] = ["<str:escape><bean:write name="doc" property="descripcion" filter="false"/></str:escape>"];         
          i++;
        </logic:iterate>
        
        parent.mainFrame.recuperaListaDocumentos(listaDocs);
    }
}

</script>
</head>
<body onLoad="redirecciona();">
<p>&nbsp;</p><center/>
</body>
</html>
