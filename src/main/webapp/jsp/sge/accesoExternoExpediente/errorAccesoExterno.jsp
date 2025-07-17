<jsp:include page="/jsp/errorServletPage.jsp" flush="true">
    <jsp:param name='msg' value='<%= request.getAttribute("errorTramitacionExterna") %>'/>
</jsp:include>
