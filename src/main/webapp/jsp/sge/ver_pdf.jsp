<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="org.apache.commons.logging.Log"%>
<html><head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title>Ver fichero PDF</title>
 </head>
 <body onload="top.focus();">
<%  Log m_log = LogFactory.getLog(this.getClass().getName());
	String nmbFichero;
	String nombre = request.getParameter("fichero");
	if (nombre != null){
        nmbFichero = request.getContextPath() + nombre;
        if(m_log.isDebugEnabled()) m_log.debug("El fichero:" + nmbFichero);%>
	<OBJECT CLASSID="clsid:CA8A9780-280D-11CF-A24D-444553540000" WIDTH='100%' HEIGHT='100%' ID=Pdf1>
	    <PARAM NAME="SRC" VALUE="<%=nmbFichero%>" >
	    <embed src="<%=nmbFichero%>" width='100%' height='100%' style="z-index:-100;">
	</OBJECT>
<%  } else {
        if(m_log.isDebugEnabled()) m_log.debug("El fichero es null"); %>
<%  } %>
</body>
</html>
