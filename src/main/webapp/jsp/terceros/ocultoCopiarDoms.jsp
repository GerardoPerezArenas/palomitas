<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="c" uri="/WEB-INF/tlds/c.tld" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>:: Oculto Copiar Domicilios en Depuracion ::</title>
    <script type="text/javascript" src="<html:rewrite page='/javascript/general.js'/>"></script>
    <script type="text/javascript">
        function inicializar() {
            var codigoTercero = "<c:out value="${param.codTercero}"/>";
            var tercerosACopiar = "<c:out value="${param.codTerSelec}"/>";
            var codigosACopiar = tercerosACopiar.split(",");
            var doms = "";
            for (var i = 0; i < codigosACopiar.length; i++) {
                var tercero = codigosACopiar[i];
            <logic:iterate id="tercero" name="DepuracionTerceros.terceros" scope="session">
                var codItTercero = "<bean:write name="tercero" property="codTercero"/>";
                if (codItTercero == tercero) {
                <logic:iterate id="elemento" name="tercero" property="domicilios">
                    doms = doms + "<bean:write name="elemento" property="codDomicilio"/>,";
                </logic:iterate>
                }
            </logic:iterate>
            }
            doms = doms.substring(0, doms.length - 1);

            parent.mainFrame.copiarDomicilios(codigoTercero, doms);
        }

    </script>
</head>
<body onload="inicializar();">
<p>&nbsp;</p>
</body>
</html>