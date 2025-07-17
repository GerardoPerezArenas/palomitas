<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="es.altia.agora.interfaces.user.web.administracion.MyHttpSessionListener"%>
<%@page import="es.altia.agora.interfaces.user.web.administracion.SessionInfo"%>
<%@page import="es.altia.agora.interfaces.user.web.administracion.InformacionSistemaForm"%>

<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<html:form action="/administracion/InformacionSistema.do" method="post">
    <head>
        <jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <%@ include file="/jsp/plantillas/Metas.jsp" %>
        <title>Info Sistema</title>

        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 5;
            String css = "";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                css = usuarioVO.getCss();
            }

             %>

        <!-- Estilos -->

        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
    
        <style type="text/css">
table.propsGenerales {

	border-collapse: collapse;
        font-family:Verdana, Helvetica, sans-serif;
        font-size: 11px;
        text-align: left;

}

table.propsGenerales td {
	background: none repeat scroll 0 0 #E8EDFF ;
        border-bottom: 1px solid #FFFFFF;
        border-top: 1px solid transparent;
        border-left: 1px solid transparent;
        color: #666699;
        padding: 5px;
        width: 530px;

}
table.propsGenerales td.izq {
        width: 400px;

}
table.propsGenerales td.mini {
        width: 100px;

}
table.propsGenerales td.maxi {
        width: 800px;

}
</style>

        <script type="text/javascript">

            // FUNCIONES DE CARGA E INICIALIZACION DE DATOS

           var listaPropiedadesSistema=new Array();
           var listaSesiones=new Array();
           
           var codOrganizaciones = new Array();
           var descOrganizaciones = new Array();
           
           


            function inicializar(){
                window.focus();

             
                rellenarPropiedadesSistema();
                recuperaDatosIniciales();
                

            }
            
    
                function recuperaDatosIniciales(){
                <%
                    InformacionSistemaForm bForm =
                    (InformacionSistemaForm) session.getAttribute("InformacionSistemaForm");
                    Vector listaOrganizaciones = bForm.getListaOrganizaciones();
                    
                    boolean activarSql=bForm.getActivarConsultaADMG();
                    
                    int lengthOrganizaciones = listaOrganizaciones.size();
                    int i = 0;
                %>
                    var j=0;
                <%
                    for (i = 0; i < lengthOrganizaciones; i++) {
                        GeneralValueObject organizaciones = (GeneralValueObject) listaOrganizaciones.get(i);%>
                        codOrganizaciones[j] = ['<%=(String) organizaciones.getAtributo("codigo")%>'];
                        descOrganizaciones[j] = ['<%=(String) organizaciones.getAtributo("descripcion")%>'];
                        j++;
                <%}%>
                    
                    comboOrganizaciones.addItems(codOrganizaciones,descOrganizaciones);
                    comboOrganizacionesExp.addItems(codOrganizaciones,descOrganizaciones);
                    
                    if('<%=activarSql%>'=='true')
                    {                      
                        var existingDiv = document.getElementById('tabPage4');
                      
                        existingDiv.style.visibility='visible';
                       
                    } else {
                        var existingDiv = document.getElementById('tabPage4');
                     
                        existingDiv.style.visibility='hidden';
                       
                    }
                    
                    
                }

            function rellenarPropiedadesSistema()
            {

               var i=0;
              <%
                Properties propiedadesSistema=System.getProperties();

                Enumeration e = propiedadesSistema.keys();
                Object obj;

                while (e.hasMoreElements()) {
                    obj = e.nextElement();


                    %>
                    listaPropiedadesSistema[i]=["<%=StringEscapeUtils.escapeJava(obj.toString())%>","<%=StringEscapeUtils.escapeJava((propiedadesSistema.get(obj)).toString())%>"];
                    i++;
                    <%
                }
                %>

                tabPropiedadesSistema.lineas=listaPropiedadesSistema;
                tabPropiedadesSistema.displayTabla();


                
                rellenarSesiones();

            }

            function rellenarSesiones()
            {                
                var j=0
                     
                listaSesiones=new Array();
                <%
                
                HashMap mapa=MyHttpSessionListener.getInfoSessions();
                Collection coleccionSesiones=mapa.values();
                
                Iterator iter = coleccionSesiones.iterator();

                  while (iter.hasNext()){

                      SessionInfo infoSesion=(SessionInfo) iter.next();
                       %>
                        listaSesiones[j]=["<%=infoSesion.getId()%>","<%=infoSesion.getCreationTimeAsString()%>","<%=infoSesion.getLastAccesedTimeAsString()%>","<%=(infoSesion.getMaxInactiveInterval())/60%> min","<%=(infoSesion.getIdleTime())/1000%> sec.","<%=(infoSesion.getTTL())/1000%> sec."];
                        j++;
                        <%
                    }
                %>
                tabSesiones.lineas= listaSesiones;
                tabSesiones.displayTabla();
            }


             function descargar(opcion)
            {
                document.forms[0].opcion.value="descargar";
                document.forms[0].target="oculto";
                document.forms[0].ficheroDescarga.value=opcion;
                document.forms[0].action="<%=request.getContextPath()%>/administracion/InformacionSistema.do";
                document.forms[0].submit();
            }
            
            
             function descargarOtroProperties()
            {
                document.forms[0].opcion.value="descargar";
                document.forms[0].target="oculto";
                document.forms[0].ficheroDescarga.value=document.forms[0].otroFichero.value;
                document.forms[0].action="<%=request.getContextPath()%>/administracion/InformacionSistema.do";
                document.forms[0].submit();
            }
            
            function descargarOtro()
            {
                document.forms[0].opcion.value="otroFicheroRuta";
                document.forms[0].target="oculto";
                document.forms[0].ficheroDescarga.value=document.forms[0].otroFicheroRuta.value;
                document.forms[0].action="<%=request.getContextPath()%>/administracion/InformacionSistema.do";
                document.forms[0].submit();
            }
            function pulsarInvalidar()
            {

                if(tabSesiones.selectedIndex != -1) {
                    var idSesion = listaSesiones[tabSesiones.selectedIndex][0];

                document.forms[0].opcion.value="invalidar";
                document.forms[0].target="oculto";
                document.forms[0].idSesion.value=idSesion;
                document.forms[0].action="<%=request.getContextPath()%>/administracion/InformacionSistema.do";
                document.forms[0].submit();
                }
                else{
                     jsp_alerta('A', '<%=descriptor.getDescripcion("msg_NoSelSesion")%>');
                }
            }

            

            function pulsarRecolectarMemoria()
            {

                document.forms[0].opcion.value="recolectarMemoria";
                document.forms[0].target="oculto";                
                document.forms[0].action="<%=request.getContextPath()%>/administracion/InformacionSistema.do";
                document.forms[0].submit();
                
            }

            function actualizarMemoria(mem)
            {            
                var existingDiv = document.getElementById('memoria');
                existingDiv.innerHTML = mem+' <input type= "button" class="botonGeneral" value="!Recolector"  name="cmdRecolector" onClick="pulsarRecolectarMemoria();" accesskey="S">';
                
            }

            function modificarNivelLog()
            {
                var level = document.forms[0].descLevelLog.value;
                if((level!=null)&&(level!=""))
                {
                    document.forms[0].opcion.value="nivelLog";
                    document.forms[0].target="oculto";
                    document.forms[0].action="<%=request.getContextPath()%>/administracion/InformacionSistema.do";
                    document.forms[0].submit();
                }
                
            }
            function actualizarNivelLog(level)
            {
                var existingDiv = document.getElementById('nivelLog');
                
                existingDiv.innerHTML = level;
                comboLogLevel.selectItem(-1);

            }
            
            function actualizarResultados(resultado)
            {
                var res="";
                res=tratarResultado(resultado);
                document.getElementById('resultados').value=res;
                
            }
            
            function actualizarResultadosExp(resultadoExp){
                var resExp="";
                resExp=tratarResultado(resultadoExp);                  
                document.getElementById('resultadosExp').value=resExp;
            }
            
            function tratarResultado (resultado)
            {
                var retorno="";
                retorno = resultado.replace(/§¥/g,"\n");
              
                return retorno;
            }

            function pulsarEnviarEmailPrueba()
            {
               if(document.forms[0].emailPrueba.value!= '') {

               document.forms[0].opcion.value="enviarMail";
               document.forms[0].target="oculto";
               document.forms[0].action="<%=request.getContextPath()%>/administracion/InformacionSistema.do";
               document.forms[0].submit();
               }
               else
                   {
                       jsp_alerta('A', '<%=descriptor.getDescripcion("msjObligEmail")%>');
                   }
            }

    function pulsarSalir(){

        document.forms[0].target = "mainFrame";
        document.forms[0].action = '/flexia-head/jsp/administracion/presentacionADM.jsp';
        document.forms[0].submit();
    }
    
    
    
    function ejecutarConsulta()
    {
       if(document.forms[0].codOrganizacion.value!= '')
       {
       if(document.forms[0].sql.value!= '') {

 
               document.forms[0].opcion.value="ejecutarConsulta";               
               document.forms[0].target="oculto";
               document.forms[0].action="<%=request.getContextPath()%>/administracion/InformacionSistema.do";
               document.forms[0].submit();
               }
               else
                   {
                       jsp_alerta('A', 'Consulta vacía');
                   } 
                   
         }else
                   {
                       jsp_alerta('A', 'Falta la organización');
                   } 
    }


    function ConsultaListaTramOrig(){
        if(document.forms[0].codOrganizacionExp.value == ''){
              jsp_alerta('A', 'La organización no puede estar vacía');
        }else if(document.forms[0].numExpediente.value == ''){
              jsp_alerta('A', 'El número de expediente no puede estar vacío');
        }else{
                   document.forms[0].opcion.value="ConsultaListTramOrig";               
                   document.forms[0].target="oculto";
                   document.forms[0].action="<%=request.getContextPath()%>/administracion/InformacionSistema.do";
                   document.forms[0].submit()
        }                      
    }
    
    
    function ConsultaCodTramite(){
          if(document.forms[0].codOrganizacionExp.value == ''){
              jsp_alerta('A', 'La organización no puede estar vacía');
          }else if(document.forms[0].procedimiento.value == ''){
              jsp_alerta('A', 'El procedimiento no puede estar vacío');
          }else{
                   document.forms[0].opcion.value="ConsultaCodTramite";               
                   document.forms[0].target="oculto";
                   document.forms[0].action="<%=request.getContextPath()%>/administracion/InformacionSistema.do";
                   document.forms[0].submit()
          }
    }


    function ConsultaFlujoExp(){
          if(document.forms[0].codOrganizacionExp.value == ''){
              jsp_alerta('A', 'La organización no puede estar vacía');
          }else if(document.forms[0].numExpediente.value == ''){
              jsp_alerta('A', 'El número expediente no puede estar vacío');
          }else if(document.forms[0].procedimiento.value == ''){
              jsp_alerta('A', 'El procedimiento no puede estar vacío');
          }else{
              document.forms[0].opcion.value="ConsultaFlujoExp";               
              document.forms[0].target="oculto";
              document.forms[0].action="<%=request.getContextPath()%>/administracion/InformacionSistema.do";
              document.forms[0].submit();
          }    
    }   
    
    
    function Limpiar(){
        document.forms[0].codOrganizacionExp.value= '';
        document.forms[0].descOrganizacionExp.value= '';
        document.forms[0].procedimiento.value= '';
        document.getElementById('resultadosExp').value='';
        document.getElementById('numExpediente').value='';
    }
    
        </script>
    </head>

    <body class="bandaBody" onload="javascript:{pleaseWait('off');
        inicializar();}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

        <form name="formulario" method="post">
            <input  type="hidden"  name="opcion" id="opcion">
            <input  type="hidden"  name="identificador" id="identificador">
            <input  type="hidden"  name="codCampo" value="NOM">
            <input  type="hidden"  name="codIdioma" value="1">
            <input  type="hidden"  name="descIdioma" value="">
            <input  type="hidden"  name="ficheroDescarga" value="">
            <input  type="hidden"  name="idSesion" value="">
            <input  type="hidden"  name="codLevelLog" value="">

            <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiq_PropSistema")%></div>
            <div class="contenidoPantalla">
                <div class="tab-pane" id="tab-pane-1" >

                    <script type="text/javascript">
                    tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
                    </script>
                        <div class="tab-page" id="tabPage1">

                            <h2 class="tab"><%= descriptor.getDescripcion("gEtiq_InfoGeneral")%></h2>

                            <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>

                            <TABLE id ="tablaDatosGral" class="contenidoPestanha" border="0px" cellspacing="0px" cellpadding="0px">


                                <tr>
                                    <td height="20px">
                                    </td>
                                </tr>


                                <tr>
                                    <td>
                                    <table class="propsGenerales">

                                        <tr><td class="izq" align="right"><%= descriptor.getDescripcion("gEtiq_Nodo")%></td><td><c:out value="${requestScope.InformacionSistemaForm.serverNodeLocalName}"/> (<c:out value="${requestScope.InformacionSistemaForm.serverNodeLocalIP}"/>)</td></tr>
                                        <tr><td class="izq" align="right"><%= descriptor.getDescripcion("gEtiq_SistemaOperativo")%></td><td><c:out value="${requestScope.InformacionSistemaForm.osName}"/> (<c:out value="${requestScope.InformacionSistemaForm.osVersion}"/>)</td></tr>
                                        <tr><td class="izq" align="right"><%= descriptor.getDescripcion("gEtiq_JVM")%></td><td><c:out value="${requestScope.InformacionSistemaForm.VMVendor}"/> - <c:out value="${requestScope.InformacionSistemaForm.VMVersion}"/> - <c:out value="${requestScope.InformacionSistemaForm.VMModeInfo}"/></td></tr>
                                        <tr><td class="izq" align="right"><%= descriptor.getDescripcion("gEtiq_MemJVM")%></td><td id="memoria"><c:out value="${requestScope.InformacionSistemaForm.JVMUsedMemory}"/> MB./ <c:out value="${requestScope.InformacionSistemaForm.JVMTotalMemory}"/> MB. <input type= "button" class="botonGeneral" value="<%= descriptor.getDescripcion("gEtiq_recolector")%>"  name="cmdRecolector" onClick="pulsarRecolectarMemoria();" accesskey="S"></td></tr>
                                        <tr><td class="izq" align="right"><%= descriptor.getDescripcion("gEtiq_ServAplicaciones")%></td><td><c:out value="${requestScope.InformacionSistemaForm.serverInfo}"/></td></tr>
                                        <tr><td class="izq" align="right"><%= descriptor.getDescripcion("gEtiq_EnvioEmail")%></td><td><input type="text" class="inputTextoSinMayusculas" id="emailPrueba" name="emailPrueba" style="width:230px" >&nbsp;<input type= "button" class="botonGeneral" value="<%= descriptor.getDescripcion("etiqEnviar")%>"  name="cmdRecolector" onClick="pulsarEnviarEmailPrueba();" accesskey="S"></td></tr>
                                        <tr><td class="izq" align="right"><%= descriptor.getDescripcion("gEtiq_NivelTraza")%></td><td id="nivelLog"><c:out value="${requestScope.InformacionSistemaForm.nivelLog}"/></td></tr>
                                        <tr><td class="izq" align="right"><%= descriptor.getDescripcion("gEtiq_ModifNivelTraza")%></td><td><input type="text" class="inputTexto" id="descLevelLog" name="descLevelLog" style="width:130px" readOnly="true">
                                            <a href="" id="anchorLevelLog" name="anchorLevelLog">
                                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonLevelLog" name="botonLevelLog" style="cursor:pointer; border: 0px none"></span>
                                            </a>
                                        </td></tr>
                                        <tr><td class="izq" align="right"><%= descriptor.getDescripcion("gEtiq_SalidaLog")%></td><td><c:out value="${requestScope.InformacionSistemaForm.salidaLog}"/></td></tr>
                                        <tr><td class="izq" align="right"><%= descriptor.getDescripcion("gEtiq_FichLog")%></td><td><a href="#" onclick="descargar('ficheroLog');"><c:out value="${requestScope.InformacionSistemaForm.rutaFicheroLog}"/></a></td></tr>



                                    </table>
                                    </td>
                                </tr>
                                <tr>
                                <tr>
                                    <td height="20px">
                                    </td>
                                </tr>
                                <TD class=sub3titulo>&nbsp;<%= descriptor.getDescripcion("gEtiq_InfoSesiones")%></TD>
                                </tr>
                                <tr>
                                    <td height="5px">
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                         <table class="propsGenerales">

                                             <tr><td class="mini"><%= descriptor.getDescripcion("gEtiq_SesionActual")%>:</td><td class="maxi"><%=session.getId()%></td>
                                         </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td height="5px">
                                    </td>
                                </tr>
                                <tr>

                                    <td id="tablaSesiones">
                                    </td>
                                </tr>
                                <tr>
                                    <td height="3px">
                                    </td>
                                </tr>
                                <tr>
                                    <td align="right">
                                        <input type= "button" class="botonGeneral" value=<%= descriptor.getDescripcion("gEtiq_Invalidar")%>
                                               name="cmdInvalidar" onClick="pulsarInvalidar();" accesskey="S">
                                    </td>

                                </tr>

                                <tr>

                            </tr>

                            </TABLE>
                        </div>
                        <div class="tab-page" id="tabPage2">

                            <h2 class="tab"><%= descriptor.getDescripcion("gEtiq_InfoSistema")%></h2>

                            <script type="text/javascript">tp1_p2 = tp1.addTabPage( document.getElementById( "tabPage2" ) );</script>

                            <TABLE id ="tablaDatosGral" class="contenidoPestanha" border="0px" cellspacing="0px" cellpadding="0px">
                                 <tr>
                                <td style="width: 100%; padding-bottom: 5px" class="columnP">
                                    <table width="100%" rules="cols"border="0" cellspacing="0" cellpadding="0">
                                        <tr>
                                            <td height="20px">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td id="tablaPropiedadesSistema">
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            </TABLE>
                        </div>
                        <div class="tab-page" id="tabPage3">

                            <h2 class="tab" id="pestana3"><%= descriptor.getDescripcion("gEtiq_InfoApl")%></h2>

                            <script type="text/javascript">tp1_p3 = tp1.addTabPage( document.getElementById( "tabPage3" ) );</script>

                            <TABLE id ="tablaDatosGral" class="contenidoPestanha" border="0px" cellspacing="0px" cellpadding="0px">
                                <tr>
                                    <td height="20px">
                                    </td>
                                </tr>


                                <tr>
                                    <td>
                                    <table class="propsGenerales">

                                        <tr><td class="izq" align="right"><%= descriptor.getDescripcion("gEtiq_WebAppBase")%></td><td><c:out value="${requestScope.InformacionSistemaForm.webAppBaseDir}"/></td></tr>
                                        <tr><td class="izq" align="right"><%= descriptor.getDescripcion("gEtiq_HostVirtual")%></td><td><c:out value="${requestScope.InformacionSistemaForm.hostVirtual}"/> </td></tr>
                                        <tr><td class="izq" align="right">jndi</td><td><c:out value="${requestScope.InformacionSistemaForm.jndi}"/></td></tr>
                                        <tr><td class="izq" align="right"><%= descriptor.getDescripcion("gEtiq_GestorBD")%></td><td><c:out value="${requestScope.InformacionSistemaForm.gestor}"/></td></tr>
                                        <tr><td class="izq" align="right"><%= descriptor.getDescripcion("gEtiq_VersionFlexia")%></td><td><c:out value="${requestScope.InformacionSistemaForm.version}"/></td></tr>
                                        <tr><td class="izq" align="right"><%= descriptor.getDescripcion("gEtiq_Fichero")%> common.properties</td><td><a href="#" onclick="descargar('common');"><%= descriptor.getDescripcion("gEtiq_Descargar")%></a></td></tr>
                                        <tr><td class="izq" align="right"><%= descriptor.getDescripcion("gEtiq_Fichero")%> log4j.properties</td><td><a href="#" onclick="descargar('log4j');"><%= descriptor.getDescripcion("gEtiq_Descargar")%></a></td></tr>
                                        <tr><td class="izq" align="right"><%= descriptor.getDescripcion("gEtiq_Fichero")%> techserver.properties</td><td><a href="#" onclick="descargar('techserver');"><%= descriptor.getDescripcion("gEtiq_Descargar")%></a></td></tr>
                                        <tr><td class="izq" align="right"><%= descriptor.getDescripcion("gEtiq_Fichero")%> Registro.properties</td><td><a href="#" onclick="descargar('Registro');"><%= descriptor.getDescripcion("gEtiq_Descargar")%></a></td></tr>
                                        <tr><td class="izq" align="right"><%= descriptor.getDescripcion("gEtiq_Fichero")%> web.xml</td><td><a href="#" onclick="descargar('web');"><%= descriptor.getDescripcion("gEtiq_Descargar")%></a></td></tr>
                                        <tr><td class="izq" align="right"><%= descriptor.getDescripcion("gEtiq_Fichero")%> context.xml</td><td><a href="#" onclick="descargar('context');"><%= descriptor.getDescripcion("gEtiq_Descargar")%></a></td></tr>
                                        <tr><td class="izq" align="right"><%= descriptor.getDescripcion("gEtiq_Fichero")%> <input type="text" class="inputTextoSinMayusculas" id="otroFichero" name="otroFichero" style="width:130px" >.properties</td><td><a href="#" onclick="descargarOtroProperties();"><%= descriptor.getDescripcion("gEtiq_Descargar")%></a></td></tr>
                                        <tr><td class="izq" align="right"><%= descriptor.getDescripcion("gEtiq_Fichero")%> <input type="text" class="inputTextoSinMayusculas" id="otroFicheroRuta" name="otroFicheroRuta" style="width:130px" > (ruta desde la base del proyecto)</td><td><a href="#" onclick="descargarOtro();"><%= descriptor.getDescripcion("gEtiq_Descargar")%></a></td></tr>


                                    </table>
                                    </td>
                                </tr>
                            </TABLE>
                        </div>
                                        <!-- En caso de querer activar la consulta sql quitamos la visibiildad oculta-->
                        <div class="tab-page"  id="tabPage4" style="visibility: hidden;" >

                            <h2 class="tab" id="pestana4"> SQL </h2>

                            <script type="text/javascript">tp1_p4 = tp1.addTabPage( document.getElementById( "tabPage4" ) );</script>

                            <TABLE id ="tablaDatosGral" class="contenidoPestanha" border="0px" cellspacing="0px" cellpadding="0px">
                                <tr>
                                    <td height="20px">
                                    </td>
                                </tr>

                                <tr>
                                    <td class="etiqueta" style="width: 20%"><%=descriptor.getDescripcion("gEtiq_Organizacion")%></td>
                                    <td><input type="text" class="inputTextoObligatorio" id="codOrganizacion" name="codOrganizacion" style="width:30px" >
                                    <input type="text" class="inputTextoObligatorio" id="descOrganizacion" name="descOrganizacion" style="width:280px" readOnly="true">
                                        <a href="" id="anchorOrganizacion" name="anchorOrganizacion">
                                            <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonOrganizacion" name="botonOrganizacion" style="cursor:pointer; border: 0px none"></span>
                                        </a>
                                    </td>
                                </tr>
                                <tr>
                                    <td height="20px">
                                    </td>
                                </tr>

                               </tr>
                                <tr>
                                    <td class=sub3titulo colspan="3"  style="width: 20%">Consulta</td>
                                </tr>
                                <tr>

                                    <td colspan="3">
                                    <html:textarea  property="sql" style="width:920px" rows="13"  maxlength="2"
                                       onkeydown="return textCounter(this,40000);"   value=""></html:textarea>

                                    </td>
                                </tr>
                                <tr>
                                    <td class=sub3titulo colspan="3" style="width: 20%">Resultados</td>
                                </tr>

                                <tr>
                                    <td colspan="3"> 
                                    <textarea  property="resultados"   id="resultados" style="width:920px;overflow-y:scroll" rows="17" cols="20" maxlength="2" wrap="off"
                                       value="resultados"></textarea>

                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="3">
                                    <input type= "button" class="botonGeneral" value="Exec"  name="cmdEjecutar" onClick="ejecutarConsulta();" accesskey="S">
                                    </td>
                                </tr>
                            </TABLE>
                        </div> 

                        <div class="tab-page" id="tabPage5">

                            <h2 class="tab" id="pestana5"> Info. Expediente </h2>

                            <script type="text/javascript">tp1_p5 = tp1.addTabPage( document.getElementById( "tabPage5" ) );</script>

                            <TABLE id ="tablaDatosGral" class="contenidoPestanha" border="0px" cellspacing="0px" cellpadding="0px">
                                <tr>
                                    <td height="20px">
                                    </td>
                                </tr>

                                <tr>
                                    <td class="etiqueta" style="width: 20%"><%=descriptor.getDescripcion("gEtiq_Organizacion")%></td>
                                    <td><input type="text" class="inputTextoObligatorio" id="codOrganizacionExp" name="codOrganizacionExp" style="width:30px" >
                                    <input type="text" class="inputTextoObligatorio" id="descOrganizacionExp" name="descOrganizacionExp" style="width:280px" readOnly="true" >
                                        <a href="" id="anchorOrganizacionExp" name="anchorOrganizacionExp">
                                            <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonOrganizacionExp" name="botonOrganizacionExp" style="cursor:pointer; border: 0px none"></span>
                                        </a>
                                    </td>
                                </tr>
                                <tr>
                                    <td height="10px">
                                    </td>
                                </tr>

                                <tr>
                                    <td class="etiqueta" style="width: 20%">Número expediente: </td>                                                                                                                
                                    <td> <input  type="text" property="numExpediente" class="inputTexto" id="numExpediente" name="numExpediente" style="width:280px" value=""> </td>
                                </tr>

                                <tr>
                                    <td height="10px">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="etiqueta" style="width: 20%">Procedimiento: </td>                                                                                                                
                                    <td> <input  type="text" property="procedimiento" class="inputTexto" id="procedimiento" name="procedimiento" style="width:280px" value=""> </td>
                                </tr>                                                                
                                <tr>
                                    <td height="30px">
                                    </td>
                                </tr>                                                                
                                <tr colspan="3">
                                    <td class=sub3titulo colspan="3" style="width: 20%">Resultado</td>
                                </tr>

                                <tr>
                                    <td colspan="3"> 
                                    <textarea  property="resultadosExp"  name="resultadoExp" id="resultadosExp" style="width:100%;overflow-y:scroll" rows="10" cols="20" maxlength="2" wrap="off"
                                       value="resultados"></textarea>                                                  
                                    </td>  
                                </tr>

                                <tr>
                                    <td height="30px">
                                    </td>
                                </tr>

                                <tr>
                                    <td>
                                    <table style="border: 0;" align="left">
                                        <tr>
                                            <td width="10px"></td>
                                            <td> <input type= "button" class="botonGeneral" value="List Tram Orig"  name="cmdList" onclick="ConsultaListaTramOrig()"> </td>
                                            <td width="10px"></td>
                                            <td> <input type="button" class="botonGeneral" value="Código trámite" name="cmdCodTramite" onclick="ConsultaCodTramite()"></td>
                                            <td width="10px"></td>
                                            <td> <input type="button" class="botonGeneral" value="Flujo expediente" name="cmdFlujoExp" onclick="ConsultaFlujoExp()"></td>
                                            <td width="10px"></td>
                                            <td> <input type="button" class="botonGeneral" value="Limpiar" name="cmdLimpiar" onclick="Limpiar()"></td>
                                        </tr>
                                    </table>                                                                
                                    </td>              
                                </tr>              
                            </TABLE>
                </div>
                </div>
            <div class="botoneraPrincipal"> 
           </div>
        </div>
    </form>
    <script type="text/javascript">
       
        var tabPropiedadesSistema = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaPropiedadesSistema'));

        tabPropiedadesSistema.addColumna('400','left','<%=descriptor.getDescripcion("gEtiq_Nombre")%>');
        tabPropiedadesSistema.addColumna('530','left','<%=descriptor.getDescripcion("gEtiq_Propiedad")%>');

         var tabSesiones = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaSesiones'));

        tabSesiones.addColumna('300','center','Id');
        tabSesiones.addColumna('170','center','<%=descriptor.getDescripcion("gEtiq_Creacion")%>');
        tabSesiones.addColumna('160','center','<%=descriptor.getDescripcion("gEtiq_UltAcceso")%>');
        tabSesiones.addColumna('90','center','Max TTL');
        tabSesiones.addColumna('90','center','<%=descriptor.getDescripcion("gEtiq_TiempoInact")%>');
        tabSesiones.addColumna('90','center','TTL');
       

        tabSesiones.displayCabecera=true;        

        var comboLogLevel = new Combo("LevelLog");
        comboLogLevel.addItems([1,2,3,4,5,6,7],["All","Debug","Error","Fatal","Info","Off","Warn"]);
        comboLogLevel.change = modificarNivelLog;
        
        var comboOrganizaciones = new Combo("Organizacion");
        var comboOrganizacionesExp = new Combo("OrganizacionExp");
       
        
       
       
       
        

        //Eventos raton, teclado

       <%String Agent = request.getHeader("user-agent");%>

        var coordx=0;
        var coordy=0;


        <%if(Agent.indexOf("MSIE")==-1) {%> //Que no sea IE
            window.addEventListener('mousemove', function(e) {
                coordx = e.clientX;
                coordy = e.clientY;
            }, true);
        <%}%>

            document.onmouseup = checkKeys; 
                        
            function checkKeysLocal(evento,tecla){
                var teclaAuxiliar="";
                if(window.event){
                    evento = window.event;
                    teclaAuxiliar =evento.keyCode;
                }else
                    teclaAuxiliar =evento.which;


                if(teclaAuxiliar == 1){
                    if (comboLogLevel.base.style.visibility == "visible" && isClickOutCombo(comboLogLevel,coordx,coordy)) setTimeout('comboLogLevel.ocultar()',20);
                    if (comboOrganizaciones.base.style.visibility == "visible" && isClickOutCombo(comboOrganizaciones,coordx,coordy)) setTimeout('comboOrganizaciones.ocultar()',20);
                }
                if(teclaAuxiliar == 9){
                    comboLogLevel.ocultar();
                    comboOrganizaciones.ocultar();
                }
                keyDel(evento);
            }  

        </script>

    </body>
</html:form>
