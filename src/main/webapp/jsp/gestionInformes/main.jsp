<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<tiles:insert page="/jsp/gestionInformes/tpls/WindowTemplate.jsp" flush="true">
	<tiles:put name="title" type="string">
        <fmt:message key='GestionInformes.Application.title'/>
    </tiles:put>

	<tiles:put name="head-content" type="string">
	</tiles:put>
	<tiles:put name="content" type="string">
		<tiles:insert page="/jsp/gestionInformes/tpls/AltiaTemplate.jsp" flush="false">
			<tiles:put name="altiaAppFirsttitle" type="string">
                <c:out value="${sessionScope.usuario.ent}" />
            </tiles:put>
			<tiles:put name="altiaAppSecondtitle" type="string">
                <fmt:message key='GestionInformes.Application.title'/>
            </tiles:put>
			<tiles:put name="altiaAppUser" type="string">
                <c:out value="${sessionScope.usuario.nombreUsu}"/>
            </tiles:put>
			<tiles:put name="altiaAppFormTitle" type="string">
                <fmt:message key='GestionInformes.Application.title'/>
            </tiles:put>
            <tiles:put name="altiaAppFormContent" type="string"></tiles:put>
            <tiles:put name="altiaAppFormBotons" type="string"></tiles:put>
	 	</tiles:insert>
	</tiles:put>
	<tiles:put name="finalJavascript" type="string">
		<script type="text/javascript" language="JavaScript">
			pleaseWait1("off",this);
                        function inicializar(){}
                </script>
	</tiles:put>
</tiles:insert>
