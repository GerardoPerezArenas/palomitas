<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<div class="txttitblanco"><tiles:insert attribute="altia-app-form-title"/></div>
<div class="contenidoPantalla">
    <!-- ****************************************************************** -->
    <!-- ******************* CONTENIDO DEL FORMULARIO ********************* -->
    <!-- ****************************************************************** -->
            <tiles:insert attribute="altia-app-form-content"/>
    <!-- ****************************************************************** -->
    <!-- ******************* FIN CONTENIDO DEL FORMULARIO ***************** -->
    <!-- ****************************************************************** -->
        <tiles:insert attribute="altia-app-form-footer"/>
    <!-- ****************************************************************** -->
    <!-- ******************* FIN PIE DEL FORMULARIO *********************** -->
    <!-- ****************************************************************** -->
</div>