<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <jsp:include page="/jsp/informes/tpls/app-constants.jsp"/>
</head>
<body>
    <script type='text/javascript'>
    //alert('En oculto');
    if ( (top.mainFrame.XeradorInformesForm.textOperacion.value == 'M') )
    {
            top.mainFrame.callbackAceptarModificar();
    }
    else if (top.mainFrame.XeradorInformesForm.textOperacion.value == 'C')
    {
        //alert('Estoy en oculto con op C');
        <%
        String vector=null;
         if  ( (request.getAttribute("vectorInforme")!=null) && (!request.getAttribute("vectorInforme").toString().trim().equals("")) )
            {
            vector=request.getAttribute("vectorInforme").toString();

            } else vector="[]";
        %>
        var vector=<%=vector%>;
        //alert('Estoy en oculto con op C.vector es:'+vector+'.');
        top.mainFrame.callbackCubreControlesInforme(vector);
    }
    else if (top.mainFrame.XeradorInformesForm.textOperacion.value == 'CLI')
    {
        //alert('Estoy en oculto con op CLI');
        <%

         if  ( (request.getAttribute("vectorInformes")!=null) && (!request.getAttribute("vectorInformes").toString().trim().equals("")) )
            {
            vector=request.getAttribute("vectorInformes").toString();

            } else vector="[]";
        %>
        var vector=<%=vector%>;
        //alert('Estoy en oculto con op CLI.vector es:'+vector+'.');
        top.mainFrame.callbackCubreTablaInformes(vector);
    }
    else if (top.mainFrame.XeradorInformesForm.textOperacion.value == 'GEP')
    {
        //alert('Estoy en oculto con op GEP');
        <%

         if  ( (request.getAttribute("COD_INFORMEXERADOR")!=null) && (!request.getAttribute("COD_INFORMEXERADOR").toString().trim().equals("")) )
            {
            vector=request.getAttribute("COD_INFORMEXERADOR").toString();

            } else vector="Ok";
        %>
        var vector='<%=vector%>';
        //alert('Estoy en oculto con op CLI.vector es:'+vector+'.');
        top.mainFrame.callbackpulsarGuardar(vector);
    }
    else if (top.mainFrame.XeradorInformesForm.textOperacion.value == 'A')
    {
    //	alert('Estoy en oculto con op A');
        <%
        if  ( (request.getAttribute("COD_INFORMEXERADOR")!=null) && (!request.getAttribute("COD_INFORMEXERADOR").toString().trim().equals("")) )
                {
                vector=request.getAttribute("COD_INFORMEXERADOR").toString();

                } else vector="[]";
            %>

        var codigo=<%=vector%>;
        //alert('Estoy en oculto con op A.vector es:'+vector+'.');
        top.mainFrame.callbackAceptarEngadir(codigo);
    }
    else if (top.mainFrame.XeradorInformesForm.textOperacion.value == 'I')
    {
    //	alert('Estoy en oculto con op A');
        <%
        if  ( (request.getAttribute("URL_XML")!=null) && (!request.getAttribute("URL_XML").toString().trim().equals("")) )
                {
                vector=request.getAttribute("URL_XML").toString();

                } else vector="Ok";
            %>

        var urlXML='<%=vector%>';
    <%
        if  ( (request.getAttribute("URL_FDOT")!=null) && (!request.getAttribute("URL_FDOT").toString().trim().equals("")) )
                {
                vector=request.getAttribute("URL_FDOT").toString();

                } else vector="Ok";
            %>

        var urlDOC='<%=vector%>';
        <%
            if  ( (request.getAttribute("XML")!=null) && (!request.getAttribute("XML").toString().trim().equals("")) )
                    {
                    vector=request.getAttribute("XML").toString();

                    } else vector="Ok";
                %>

        var XML='<%=vector%>';
        //alert('Estoy en oculto con op I.urlXML es:'+urlXML+'.urlDOC:'+urlDOC+'.');
        top.mainFrame.callbackVisualizaInforme(urlXML,urlDOC,XML);
    }
    else if (top.mainFrame.XeradorInformesForm.textOperacion.value == 'B')
    {
        //alert('Estoy en oculto con op A');

        //alert('Estoy en oculto con op A.vector es:'+vector+'.');
        top.mainFrame.callbackEliminarInforme() ;

    }else if (top.mainFrame.XeradorInformesForm.textOperacion.value == 'BV')
    {
        //alert('Estoy en oculto con op BV');

        //alert('Estoy en oculto con op BV.vector es:'+vector+'.');
        //var temp=<%=request.getAttribute("VALORES")%>;
        top.mainFrame.callbackBuscarValores(temp) ;
    }

    //top.oculto.location="about:blank";
    </script>
</body>
</html>
