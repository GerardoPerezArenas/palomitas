<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>

<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm" %>
<%@ page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@ page import="java.util.Vector"%>
<%@page import="es.altia.common.service.config.*"%>

<html>
<head>
    <title>Cargar asunto</title>
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
            if (opcion == 'cargarAsunto') {
                // Variable con el codigo del rol por defecto
                var rolPorDefecto;
                var descRolPorDefecto;
                var mun_procedimiento = '<bean:write name="MantAnotacionRegistroForm" property="mun_procedimiento"/>';
                
                rolPorDefecto = '<bean:write name="MantAnotacionRegistroForm" property="codRolDefecto"/>';
                descRolPorDefecto = 
                    "<str:escape><bean:write name="MantAnotacionRegistroForm" property="descRolDefecto" filter="false"/></str:escape>";

                <bean:define id="asunto" name="MantAnotacionRegistroForm" property="asuntoVO"/>             
                frame.document.forms[0].asunto.value = 
                  "<str:escape><bean:write name="asunto" property="extracto" filter="false"/></str:escape>";
                var unidadTramitadora = '<bean:write name="asunto" property="unidadTram"/>';
                var procedimiento     = '<bean:write name="asunto" property="procedimiento"/>';
                var descProcedimiento     = '<bean:write name="asunto" property="desProcedimiento"/>';
                var digitProcedimiento     = '<bean:write name="asunto" property="digitProcedimiento"/>';
                var bloquearDestino='<bean:write name="asunto" property="bloquearDestino"/>';
                var bloquearProcedimiento='<bean:write name="asunto" property="bloquearProcedimiento"/>';
                                
                // #234108: recuperamos el valor de la propiedad y se lo asignamos al input hidden
                var tipoDocObligatorio     = '<bean:write name="asunto" property="tipoDocObligatorio"/>';
                frame.document.forms[0].asuntoConTipoDocOblig.value = tipoDocObligatorio;

                // Los documentos nuevos ya se asignan al form en MantAnotacionRegistroAction y se 
                // recarga la tabla en altaRE.jsp en 'cargarDatosAsunto'
                var listaDocs = new Array();
                cont=0;
                <logic:present name="MantAnotacionRegistroForm" property="listaDocsAsignados">
                <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaDocsAsignados">
               
                var str='';
                if('<bean:write name="elemento" property="entregado"/>'=='S') str='SI';
                else str='NO';
                
                var esAccesoExterno = '<%=request.getAttribute("esAccesoExterno") %>';
                if(frame.mostrarDigitalizar || esAccesoExterno=="si"){
                    var strCompulsado='';
              
                    if('<bean:write name="elemento" property="compulsado"/>'=='S' || '<bean:write name="elemento" property="compulsado"/>'=='SI') 
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


                // Lista de unidades organicas a notificar por correo
                var listaUorsCorreo = new Array();
                var notificar = <bean:write name="MantAnotacionRegistroForm" property="enviarCorreo"/>;
                if (notificar) {
                    cont=0;
                    <logic:present name="MantAnotacionRegistroForm" property="listaUorsCorreo">
                    <logic:iterate id="uor" name="MantAnotacionRegistroForm" property="listaUorsCorreo">
                       listaUorsCorreo[cont] = <bean:write name="uor"/>;     
                       cont++;
                    </logic:iterate>
                    </logic:present> 
                }

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

                // Se pasan los datos a altaRE.jsp
                frame.cargarDatosAsunto(unidadTramitadora, procedimiento, descProcedimiento, digitProcedimiento, mun_procedimiento,
                       rolPorDefecto, descRolPorDefecto,listaDocs, notificar, listaUorsCorreo,
                       codRolesAnteriores, codRolesNuevos, descRolesNuevos, cod_roles, desc_roles, defecto_roles,bloquearDestino,bloquearProcedimiento);
            } else { // Aun estamos en confirmacion.
                frame.confirmarCargarAsunto(opcion);                
            }
        }
    </script>

</head>
<body onLoad="redirecciona()">

<p>&nbsp;<p>

</body>
</html>
