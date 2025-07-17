<jsp:include page="/jsp/errorServletPage.jsp" flush="true">
    <jsp:param name='msg' value='<%= request.getAttribute("errorAccesoExterno") %>'/>
</jsp:include>
