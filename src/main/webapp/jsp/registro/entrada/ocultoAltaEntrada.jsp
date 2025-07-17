<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>

<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm" %>
<%@ page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@ page import="java.util.Vector"%>

<html>
<head>
    <title>Alta Entrada</title>
    <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
    <script>

        function redirecciona()
        {
            var respIniciarAlta = '<bean:write name="MantAnotacionRegistroForm" property="respOpcion"/>';
                        
            if (respIniciarAlta == 'registro_cerrado')
            {
                parent.mainFrame.registroCerrado();

            }else if (respIniciarAlta == 'registrar_alta_sin_confirmar') {
                parent.mainFrame.confirmarAlta();
            }else if (respIniciarAlta == 'fecha_posterior_a_actual') {
                parent.mainFrame.alertarFechaPosterior();
            }else if (respIniciarAlta == 'registro_cerrado_duplicar') {
                parent.mainFrame.duplicarDenegado();
            } else if (respIniciarAlta == 'registro_cerrado_contestar') {
                parent.mainFrame.contestarDenegado();
            } else if (respIniciarAlta == 'no_existe_expediente') {
                parent.mainFrame.noExisteExpediente();
            } else if (respIniciarAlta == 'proc_mal_relacionado') {
                parent.mainFrame.procMalRelacionado();
            } else if (respIniciarAlta == 'alta_entrada_registrada') {
           
                var ejer = '<bean:write name="MantAnotacionRegistroForm" property="ejercicioAnotacion"/>';
                var numAnot = '<bean:write name="MantAnotacionRegistroForm" property="numeroAnotacion"/>';
                parent.mainFrame.altaEntradaRegistrada(ejer, numAnot);

            } else if (respIniciarAlta == 'alta_tramitar_registrada') {
               
                dia = '<bean:write name="MantAnotacionRegistroForm" property="diaAnotacion"/>' + "/" +
                      '<bean:write name="MantAnotacionRegistroForm" property="mesAnotacion"/>' + "/" +
                      '<bean:write name="MantAnotacionRegistroForm" property="anoAnotacion"/>';
                hora = '<bean:write name="MantAnotacionRegistroForm" property="txtHoraEnt"/>' + ":" +
                       '<bean:write name="MantAnotacionRegistroForm" property="txtMinEnt"/>';
                ejerNum = '<bean:write name="MantAnotacionRegistroForm" property="ejercicioAnotacion"/>' + "/" +
                          '<bean:write name="MantAnotacionRegistroForm" property="numeroAnotacion"/>';

                parent.mainFrame.altaTramitarRegistrada(dia, hora, ejerNum);

            } else { //Suponemos respIniciarAlta == 'init_alta_entrada';



                var cod_tiposDocumentos = new Array();
                var desc_tiposDocumentos = new Array();
                var act_tiposDocumentos= new Array();
            <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTiposDocumentos">
                cod_tiposDocumentos['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
                desc_tiposDocumentos['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
                act_tiposDocumentos['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="activo"/>';
            </logic:iterate>
                
            var cod_tiposDocumentosAlta = new Array();
            var desc_tiposDocumentosAlta = new Array();
            var act_tiposDocumentosAlta= new Array();
            <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTiposDocumentosAlta">
                cod_tiposDocumentosAlta['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
                desc_tiposDocumentosAlta['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
                act_tiposDocumentosAlta['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="activo"/>';
            </logic:iterate>

                var cod_tiposRemitentes = new Array();
                var desc_tiposRemitentes= new Array();
                 var act_tiposRemitentes= new Array();
            <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTiposRemitentes">
                cod_tiposRemitentes['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
                desc_tiposRemitentes['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
             
            </logic:iterate>

                var cod_tiposTransportes = new Array();
                var desc_tiposTransportes = new Array();
                var act_tiposTransportes = new Array();
            <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTiposTransportes">
                cod_tiposTransportes['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
                desc_tiposTransportes['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
               
            </logic:iterate>

                var cod_temas = new Array();
                var desc_temas = new Array();
            <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTemas">
                cod_temas['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
                desc_temas['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
            </logic:iterate>

                var cod_tiposIdInteresado= new Array();
                var desc_tiposIdInteresado= new Array();
            <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTiposIdInteresado">
                cod_tiposIdInteresado['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
                desc_tiposIdInteresado['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
            </logic:iterate>

                var cod_dpto = new Array();
                var desc_dpto = new Array();
            <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaDepartamentos">
                cod_dpto['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
                desc_dpto['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
            </logic:iterate>
            
             var uni_asuntos = new Array();
             var cod_asuntos = new Array();
             var desc_asuntos = new Array();
             var cont = 0;
            <logic:present name="MantAnotacionRegistroForm" property="listaAsuntos">     
              <logic:iterate id="asunto" name="MantAnotacionRegistroForm" property="listaAsuntos">
                 uni_asuntos[cont] = '<bean:write name="asunto" property="unidadRegistro"/>';
                 cod_asuntos[cont]  ='<bean:write name="asunto" property="codigo"/>';
                 desc_asuntos[cont] ="<str:escape><bean:write name="asunto" property="descripcion" filter="false"/></str:escape>";
                 cont++;
              </logic:iterate>
            </logic:present>

            // Lista de roles
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
                // Se comprueba si hay que bloquear la fecha y hora de presentación
                var bloqueoFechaHoraPresentacion = '<bean:write name="MantAnotacionRegistroForm" property="bloquearFechaHoraPresentacion"/>';
                if(bloqueoFechaHoraPresentacion!=null && bloqueoFechaHoraPresentacion=="SI")
                    parent.mainFrame.BLOQUEO_FECHA_HORA_ANOTACION = true;
                
                var fechaHoraServidor = '<bean:write name="MantAnotacionRegistroForm" property="fechaHoraHoy"/>';
                var fechaServidor = fechaHoraServidor.substring(0,10);
                var horaServidor = fechaHoraServidor.substring(11,16);
                parent.mainFrame.iniciarAlta(cod_tiposDocumentos, desc_tiposDocumentos, act_tiposDocumentos,
                                                       cod_tiposDocumentosAlta, desc_tiposDocumentosAlta,	
                                                       act_tiposDocumentosAlta,
                                                       cod_tiposTransportes, desc_tiposTransportes, 
                                                       cod_tiposRemitentes, desc_tiposRemitentes, 
                                                       cod_temas, desc_temas,
                                                       cod_tiposIdInteresado, desc_tiposIdInteresado, 
                                                       cod_dpto, desc_dpto, 
                                                       uni_asuntos, cod_asuntos, desc_asuntos,
                                                       cod_roles, desc_roles, defecto_roles,
                                                       fechaServidor,horaServidor, fechaHoraServidor);
            }
            // Al dar de alta una Entrada/Salida en SIR ya se ha creado el registro en Regexlan.
            // Si falla o va OK, hay que mostrar al usuario y hacer que cargue los datos del registro que se acaba de crear
            // Sin afectar el flujo Original
            var tipoRegistroES = '<bean:write name="MantAnotacionRegistroForm" property="tipoReg"/>';
            var requiereGestionSIR = '<bean:write name="MantAnotacionRegistroForm" property="requiereGestionSIR"/>';
            if(requiereGestionSIR=="true"){
                var mensajeSIR = '<bean:write name="MantAnotacionRegistroForm" property="descEstadoRespGestionSIR"/>';
                parent.mainFrame.mostrarMensajeAltaSIR(mensajeSIR);
            }
        }
    </script>

</head>
<body onLoad="redirecciona()">

<p>&nbsp;<p>

</body>
</html>
