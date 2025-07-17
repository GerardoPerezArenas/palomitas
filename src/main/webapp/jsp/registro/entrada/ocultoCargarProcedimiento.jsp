<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>

<%@page import="java.util.Vector" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm" %>

<html>
<head>
    <title>Cargar procedimiento</title>
    <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
    <script>            
        var frame;
        if (parent.mainFrame){
            frame = parent.mainFrame;
        } else {
            frame = parent;
        }
    
        function redirecciona() {
            var opcion = '<bean:write name="MantAnotacionRegistroForm" property="respOpcionForm"/>';
            var modoAlta=frame.document.forms[0].modoAlta.value;
            
            if (opcion == 'cargarProcedimiento') {
                
                // Variable con el codigo del rol por defecto
                var rolPorDefecto;
                var descRolPorDefecto;
                rolPorDefecto = '<bean:write name="MantAnotacionRegistroForm" property="codRolDefecto"/>';
                descRolPorDefecto = 
                    "<str:escape><bean:write name="MantAnotacionRegistroForm" property="descRolDefecto" filter="false"/></str:escape>";                        

                // Mapeo de roles anteriores a los roles nuevos
                var codRolesAnteriores = new Array();
                var codRolesNuevos = new Array();
                var descRolesNuevos = new Array();
                cont = 0;
                <logic:present name="MantAnotacionRegistroForm" property="codRolesAnteriores">
                <logic:iterate id="codRol" name="MantAnotacionRegistroForm" property="codRolesAnteriores">
                    codRolesAnteriores[cont] = <bean:write name="codRol"/>;     
                    cont++;
                </logic:iterate>
                </logic:present>  

                cont = 0;
                <logic:present name="MantAnotacionRegistroForm" property="codRolesNuevos">
                <logic:iterate id="codRol" name="MantAnotacionRegistroForm" property="codRolesNuevos">
                    codRolesNuevos[cont] = <bean:write name="codRol"/>;     
                    cont++;
                </logic:iterate>
                </logic:present>  

                cont = 0;
                <logic:present name="MantAnotacionRegistroForm" property="descRolesNuevos">
                <logic:iterate id="descRol" name="MantAnotacionRegistroForm" property="descRolesNuevos">
                    descRolesNuevos[cont] = '<str:escape><bean:write name="descRol" filter="false"/></str:escape>';
                    cont++;
                </logic:iterate>
                </logic:present>

                // Los documentos nuevos ya se asignan al form en MantAnotacionRegistroAction y se 
                // recarga la tabla en altaRE.jsp en 'cargarDatosProcedimiento'
                var listaDocs = new Array();
                cont=0;
                <logic:present name="MantAnotacionRegistroForm" property="listaDocsAsignados">
                <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaDocsAsignados">

                var str='';
                if('<bean:write name="elemento" property="entregado"/>'=='S') str='SI';
                else str='NO';
                
                var esAccesoExterno = '<%=request.getAttribute("esAccesoExterno") %>';
                if(frame.mostrarDigitalizar || esAccesoExterno){
                    var strCompulsado='';
                    if('<bean:write name="elemento" property="compulsado"/>'=='S'  || '<bean:write name="elemento" property="compulsado"/>'=='SI') 
                        strCompulsado='SI';
                    else strCompulsado='NO';
                
                    listaDocs[cont]= [ str,'<str:escape><bean:write name="elemento" property="nombreDoc" filter="false"/></str:escape>',
                                           '<bean:write name="elemento" property="tipoDoc"/>', '<bean:write name="elemento" property="fechaDoc"/>',
                                           strCompulsado,'<bean:write name="elemento" property="doc"/>',
                                        '<bean:write name="elemento" property="descripcionTipoDocumental"/>'];
                }else{
                    listaDocs[cont]= [ str,'<str:escape><bean:write name="elemento" property="nombreDoc" filter="false"/></str:escape>',
                                           '<bean:write name="elemento" property="tipoDoc"/>', '<bean:write name="elemento" property="fechaDoc"/>',
                                           '<bean:write name="elemento" property="cotejado"/>','<bean:write name="elemento" property="doc"/>'];
                }
                    cont= cont+1;
                </logic:iterate>
                </logic:present>

                // Lista de roles nuevos para recargar el combo
                var cod_roles = new Array();
                var desc_roles = new Array();
                var defecto_roles = new Array();
                var m=0;
            <%  MantAnotacionRegistroForm mantForm = (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm");
                Vector listaRoles = mantForm.getListaRoles();
                for(int t=0;t<listaRoles.size();t++) {
            %>
                    cod_roles[m] =     '<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("codRol")%>';
                    desc_roles[m] =    '<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("descRol")%>';
                    defecto_roles[m] = '<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("porDefecto")%>';
                    m++;
            <% } %>

                frame.cargarDatosProcedimiento(rolPorDefecto, descRolPorDefecto,
                      codRolesAnteriores, codRolesNuevos, descRolesNuevos, listaDocs, cod_roles, desc_roles, defecto_roles);
            } else { // Aun estamos en confirmacion.
                frame.confirmarCargarProcedimiento(opcion);
            }
        }
    </script>

</head>
<body onLoad="redirecciona()">

<p>&nbsp;<p>

</body>
</html>
