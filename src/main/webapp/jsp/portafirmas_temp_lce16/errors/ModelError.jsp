<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<tiles:insert page="/jsp/portafirmas/tpls/WindowTemplate.jsp" flush="true">
	<tiles:put name="title" type="string">
        <fmt:message key='Portafirmas.ModelError.title'/>
    </tiles:put>

	<tiles:put name="head-content" type="string">
    		
            <script type="text/javascript" language="JavaScript">

            </script>
	</tiles:put>
	<tiles:put name="content" type="string">
		<tiles:insert page="/jsp/portafirmas/tpls/AltiaTemplate.jsp" flush="false">
			<tiles:put name="altia-app-firsttitle" type="string">
                <c:out value="${sessionScope.usuario.ent}" default="" />
            </tiles:put>
			<tiles:put name="altia-app-secondtitle" type="string">
                <fmt:message key='Portafirmas.ModelError.title'/>
            </tiles:put>
			<tiles:put name="altia-app-user" type="string">
                <c:out value="${sessionScope.usuario.nombreUsu}" default="" />
            </tiles:put>
			<tiles:put name="altia-app-form-title" type="string">
                <fmt:message key='Portafirmas.ModelError.title'/>
            </tiles:put>
			<tiles:put name="altia-app-form-content" type="string">
                <br/><br/><br/><br/><br/>
				<span align="center" style="font-size:11px;font-weight: bold;"><fmt:message key='ErrorMessages.retry'/></span>
                <br/><br/>
			</tiles:put>
	 	</tiles:insert>
	</tiles:put>
	<tiles:put name="finalJavascript" type="string">
		<script type="text/javascript" language="JavaScript">
			pleaseWait1("off",this);
		</script>
	</tiles:put>
</tiles:insert>
