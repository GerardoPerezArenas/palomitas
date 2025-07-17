<!-- JSP de visualizacion de documentos -->
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>

<%@page import="java.util.Vector" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html:html>
<head>
    <TITLE>Documentación asociada al expediente</TITLE>
    <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />

    <%
        int idioma = 1;
        int apl = 1;
        if (session != null && session.getAttribute("usuario") != null) {
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            if (usuario != null) {
                idioma = usuario.getIdioma();
                apl = usuario.getAppCod();                
            }
        }
        boolean mostrarDigitalizar = false;
        String servicioDigitalizacionActivo = (String) session.getAttribute("servicioDigitalizacionActivo");
        if(servicioDigitalizacionActivo != null && servicioDigitalizacionActivo.equalsIgnoreCase("si")){
            mostrarDigitalizar = true;
        }
        String numExp = (String)request.getAttribute("numeroExpediente");
        Boolean expedienteHistorico =(Boolean) request.getAttribute("expedienteHistorico"); 
    %>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
    
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" 
                 type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
         
    <!-- ********************* JAVASCRIPT **************************    -->
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript">var APP_CONTEXT_PATH="<%=request.getContextPath()%>";</script>
    <script type="text/javascript">
        
      var pop_up;       
      function pulsarCancelar(){            
        self.parent.opener.retornoXanelaAuxiliar();
      }
 
      // Carga de un documento del expediente
      function onClickDocumentoExpediente(codigo) { 
        var expHistorico = '<bean:write name="FichaExpedienteForm" property="expHistorico"/>';

        window.open("<%=request.getContextPath()%>/VerDocumentoDatosSuplementarios;jsessionid=" + '<%=session.getId()%>' +
                    "?codigo=" + codigo + "&opcion=0" + "&expHistorico="+expHistorico+"&embedded=false", "_blank",
                    "left=10, top=10, width=1, height=1, scrollbars=no, menubar=no, location=no, resizable=yes");

      }
      
      
      function onClickDocumentoRegistro(codigo){          
           var expHistorico = '<bean:write name="FichaExpedienteForm" property="expHistorico"/>';
            
           var destino = "<%=request.getContextPath()%>/VerDocumentoDatosSuplementarios;jsessionid=" + '<%=session.getId()%>' +
                        "?codigo=" + codigo + "&opcion=8" + "&expHistorico="+expHistorico+"&embedded=true";
                    
            window.open(destino, "_blank","left=10, top=10, width=850, height=800, scrollbars=no, menubar=no, location=no, resizable=yes");
          
      }
      
      

      // Carga de un documento de registro o tramites
      function onClickDocumento(codigo) { 
          var destino = "";
          var datos = new Array();
          var expHistorico = '<bean:write name="FichaExpedienteForm" property="expHistorico"/>';
            
          datos = codigo.split("-");          
          if(datos!=null && datos.length==6){
                if(datos[0]=="ANEXO"){
                    destino = "<%=request.getContextPath()%>/VerDocumentoDatosSuplementarios;jsessionid=" + '<%=session.getId()%>' +
                        "?codigo=" + codigo + "&opcion=5" + "&expHistorico="+expHistorico+"&embedded=false";

                }else
                if(datos[0]=="DOC"){     
                    destino = "<%=request.getContextPath()%>/VerDocumentoDatosSuplementarios;jsessionid=" + '<%=session.getId()%>' +
                        "?codigo=" + codigo + "&opcion=7" + "&expHistorico="+expHistorico+"&embedded=false";

                }              
          }
                    
          window.open(destino, "_blank",
                    "left=10, top=10, width=1, height=1, scrollbars=no, menubar=no, location=no, resizable=yes");
          
      }
      
      
      function verDocumentoExpediente(codigo){       
      
           var datos = codigo.split("-");
           if(datos!=null && datos.length==4){
               var codigoDocumento = datos[0];
               var codigoMunicipio = datos[1];
               var ejercicio       = datos[2];
               var numExpediente   = datos[3];
               var codProcedimiento = "";
               
               var datosExp = numExpediente.split("/");
               if(datosExp!=null && datosExp.length==3){
                   codProcedimiento = datosExp[1];
               }
               
               var parametros = "?codigoDocumento=" + codigoDocumento + "&codMunicipio=" + codigoMunicipio +
				 "&ejercicio=" + ejercicio + "&numExpediente=" + numExpediente + "&codProcedimiento=" + codProcedimiento;
                         
               window.open("<%=request.getContextPath()%>/VerDocumentoPresentado" + parametros, "ventana1",
						"left=10, top=10, width=650, height=500, scrollbars=no, menubar=no, location=no, resizable=yes");                              
            }
      }
      
      
      function onClickDocumentoExternoExpediente(codigo,extension,nombreDocumento) {             
          
          var numExpediente = '<bean:write name="FichaExpedienteForm" property="numExpediente"/>';
          window.open("<%=request.getContextPath()%>/portafirmas/documentoportafirmas/VerDocumentacionExpedientePortafirmas.do?opcion=verDocumentoExternoExpediente&codigo=" + codigo + "&numExpediente=" + numExpediente + "&extension=" + escape(extension) + "&nombreDocumento=" + escape(nombreDocumento), "_blank",
                    "left=10, top=10, width=1, height=1, scrollbars=no, menubar=no, location=no, resizable=yes");
      }
      
      function pulsarCatalogar(uor,departamento, numero, ejercicio){
    
        var ejercicio = ejercicio;
        var numero= numero;
        var uor = uor;  
        var codDepartamento = departamento;
        var numeroExpediente ='<%=numExp%>';
        var expedienteHistorico = '<%=expedienteHistorico%>';
     
        var parametros  = "&numero=" + numero
                       +"&ejercicio="+ejercicio
                       +"&uorRegistro="+uor
                       +"&codDepartamento="+codDepartamento
                       +"&numeroExpediente="+numeroExpediente
                       +"&expedienteHistorico="+expedienteHistorico;
     
        var source = getUrlDigitalizacion() + "?opcion=cargarPantallaCatalogar" + parametros;
        var argumentos = {"hayDatos" : 'NO'};
    
        abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + source,argumentos,    
                'width=1350,height=850',function(){
                    actualizarDocumentacionAportada();
                    
                  
                });
        
     
}
      
 function getUrlDigitalizacion(){
    return APP_CONTEXT_PATH + "/registro/digitalizacionDocumentosLanbide.do";
}   

    //actualiza apartado Documentación aportada desde registro al volver de la ventana de Catalogación
    function actualizarDocumentacionAportada(){
           // pleaseWait('on');
            var datos={};
            datos.opcion='actualizarDocumentacionAsociada';
            datos.numrtoExpediente='<%=numExp%>';
            datos.expedienteHistorico = '<%=expedienteHistorico%>';
            try{
                $.ajax({
                    url:  '<c:url value="/sge/FichaExpediente.do"/>',
                    type: 'POST',
                    async: true,
                    data: datos,
                    success: procesarRespuestaActualizarDocumentacion,
                    error: mostrarErrorActualizarDocumentacion
                });           
            }catch(Err){
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
            }
    }
    
    function procesarRespuestaActualizarDocumentacion(ajaxResult){
        if(ajaxResult){
            var resp = JSON.parse(ajaxResult);
            for(var i=0; i<resp.length; i++){
                if(resp[i].tipoDocumental!=undefined){
                    document.getElementById(resp[i].nombre.toString()).innerHTML = " - "+ resp[i].tipoDocumental;
                }
            }
        }
    }
    
    function mostrarErrorActualizarDocumentacion(){
        jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
    }
    </script>
    
</head>
    
<BODY class="bandaBody" onload="javascript:{ }">

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("etiqDocsExp")%></div>
<div class="contenidoPantalla" id="tabla">
        <table style="width:100%" cellspacing="0px" cellpadding="0px" class="cuadroFondoBlanco" valign="top">
            <tr>
                <td valign="top">
                    <table width="100%">

                        <!-- Documentos de inicio de expediente -->
                        <tr>
                            <td style="width: 100%; height: 20px" class="sub3titulo"><%=descriptor.getDescripcion("etiqDocsIniExp")%></td>
                        </tr>

                        <logic:iterate id="fichero" name="documentos">
                            <tr>
                                <td style="width: 100%; height: 10px">&nbsp;&nbsp;&nbsp;&nbsp;
                                    <a href="#" class="fichero" onclick="verDocumentoExpediente('<bean:write name="fichero" property="codigo"/>');">
                                       <bean:write name="fichero" property="nombre"/>
                                    </a>
                                </td>
                            </tr>
                        </logic:iterate>


                        <!-- DOCUMENTOS DEL EXPEDIENTE -->
                        <tr>
                            <td style="width: 100%; height: 20px" class="sub3titulo"><%=descriptor.getDescripcion("etiqDocsGenExp")%></td>
                        </tr>
                        <!-- (Iterar sobre docs) -->


                        <logic:iterate id="fichero" name="ficherosExpediente">
                            <tr>
                                <td style="width: 100%; height: 10px">&nbsp;&nbsp;&nbsp;&nbsp;
                                    <a href="#" class="fichero" onclick="onClickDocumentoExpediente('<bean:write name="fichero" property="codigo"/>');">
                                       <bean:write name="fichero" property="nombre"/>
                                    </a>
                                </td>
                            </tr>
                        </logic:iterate>


                        <!-- DOCUMENTOS EXTERNOS DEL EXPEDIENTE -->
                        <tr>
                            <td style="width: 100%; height: 20px" class="sub3titulo"><%=descriptor.getDescripcion("etiqDocExternos")%></td>
                        </tr>
                        <!-- (Iterar sobre docs) -->

                        <logic:iterate id="fichero" name="documentosExternosExpediente">                                                            
                            <tr>
                                <td style="width: 100%; height: 10px">&nbsp;&nbsp;&nbsp;&nbsp;

                                    <a href="#" class="fichero" onclick="onClickDocumentoExternoExpediente('<bean:write name="fichero" property="codigoDocumento"/>','<bean:write name="fichero" property="extension"/>','<bean:write name="fichero" property="nombreDocumento"/>');">
                                        <bean:write name="fichero" property="nombreDocumento"/>
                                    </a>

                                </td>
                            </tr>
                        </logic:iterate>
                        <!-- DOCUMENTOS DEL REGISTRO DE ENTRADA -->
                        <tr>
                            <td style="width: 100%; height: 20px;" class="sub3titulo"><%=descriptor.getDescripcion("etiqDocsDesdeReg")%></td>
                        </tr>                                                         
                        <!-- (Iterar sobre docs) -->
                        <logic:iterate id="fichero" name="ficherosRegistroEntrada">
                            <logic:equal name="fichero" property="nuevoRegistro" value="true">
                            <tr>
                                    <td class="etiqueta" style="width: 100%; height: 20px; padding-top:15px;">
                                        &nbsp;&nbsp;&nbsp;&nbsp; <bean:write name="fichero" property="nombreAsiento"/> &nbsp;-&nbsp;Fecha presentación: <bean:write name="fichero" property="fechaAsiento"/>
                                        &nbsp;&nbsp;&nbsp;&nbsp; <input type="button" class="botonGeneral" style="width:80px; padding-bottom: 0px; padding-top:0px" value="<%=descriptor.getDescripcion("btnCatalogar")%>" name="cmdCatalogar" 
                                                                        onclick="pulsarCatalogar(<bean:write name="fichero" property="uor"/>,<bean:write name="fichero" property="dep"/>,<bean:write name="fichero" property="numero"/>,<bean:write name="fichero" property="ejercicio"/>)">
                                    </td>
                                </tr>
                            </logic:equal>
                            <tr>
                                <td class="etiqueta" style="width: 100%; height: 10px">
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <a href="#" class="fichero" onclick="onClickDocumentoRegistro('<bean:write name="fichero" property="codigo"/>');">
                                       <bean:write name="fichero" property="nombre"/>
                                       <%if(mostrarDigitalizar){%>
                                            <logic:notEqual name="fichero" property="tipoDocumental" value="">
                                                <span id="<bean:write name="fichero" property="nombre"/>">&nbsp;-&nbsp;<bean:write name="fichero" property="tipoDocumental"/></span>
                                            </logic:notEqual>
                                        <%}%>
                                      
                                    </a>
                                       
                                </td>
                            </tr>
                        </logic:iterate>
                        <!-- DOCUMENTOS DEL REGISTRO DE SALIDA -->
                        <tr>
                            <td style="width: 100%; height: 20px" class="sub3titulo"><%=descriptor.getDescripcion("etiqDocsAReg")%></td>
                        </tr>
                        <!-- (Iterar sobre docs) -->                                                       
                        <logic:iterate id="fichero" name="ficherosRegistroSalida">    
                            <tr>
                                <td class="etiqueta" style="width: 100%; height: 10px">
                                    &nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="fichero" property="nombreAsiento"/>&nbsp;-&nbsp;
                                    <a href="#" class="fichero" onclick="onClickDocumentoRegistro('<bean:write name="fichero" property="codigo"/>');">
                                       <bean:write name="fichero" property="nombre"/>
                                    </a>
                                </td>
                            </tr>
                        </logic:iterate>
                        <!-- DOCUMENTOS APORTADOS ANTERIORMENTE POR EL CIUDADANO  REGISTRO DE ENTRADA-->
                        <tr>
                            <td style="width:100%; height: 20px" class="sub3titulo"><%=descriptor.getDescripcion("etiqDocAportadosEntrada")%></td>
                        </tr>
                        <!-- (Iterar sobre docs aportados) -->
                        <logic:iterate id="fichero" name="ficherosAportadosAnteriorEntrada">
                            <tr>
                                <td class="etiqueta" style="width: 100%; height: 10px">
                                    &nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="fichero" property="ejercicio"/>/<bean:write name ="fichero" property="numero"/>&nbsp;&nbsp;<bean:write name="fichero" property="nombre"/>

                                </td>
                            </tr>
                        </logic:iterate>    
                        <!-- DOCUMENTOS APORTADOS ANTERIORMENTE POR EL CIUDADANO  REGISTRO DE SALIDA-->
                        <tr>
                            <td style="width:100%; height: 20px" class="sub3titulo"><%=descriptor.getDescripcion("etiqDocAportadosSalida")%></td>
                        </tr>
                        <!-- (Iterar sobre docs aportados) -->
                        <logic:iterate id="fichero" name="ficherosAportadosAnteriorSalida">
                            <tr>
                                <td class="etiqueta" style="width: 100%; height: 10px">
                                    &nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="fichero" property="ejercicio"/>/<bean:write name ="fichero" property="numero"/>&nbsp;&nbsp;<bean:write name="fichero" property="nombre"/>     
                                </td>
                            </tr>
                        </logic:iterate>
                        <!-- DOCUMENTOS DE LOS TRAMITES -->
                        <tr>
                            <td style="width: 100%; height: 20px" class="sub3titulo"><%=descriptor.getDescripcion("etiqDocsTramites")%></td>
                        </tr>                                                  
                        <!-- (Iterar sobre docs) --> 

                        <logic:iterate id="fichero" name="ficherosTramites">
                            <logic:equal name="fichero" property="nuevoTramite" value="true">
                                <tr>
                                    <td class="etiqueta" style="width: 100%; height: 10px">&nbsp;&nbsp;&nbsp;&nbsp;<%=descriptor.getDescripcion("etiqTramiteMayus")%>: <bean:write name="fichero" property="nombreTramite"/></td>
                                </tr>
                            </logic:equal>
                            <tr>
                                <td class="textoSuelto" style="width: 100%; height: 10px">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <a href="#" class="fichero" onclick="onClickDocumento('<bean:write name="fichero" property="codigo"/>');">
                                   <bean:write name="fichero" property="nombre"/>
                                </a> 
                            </tr>
                        </logic:iterate>
                    </table>
                </td>
            </tr>
        </table> 
    <div class="botoneraPrincipal">
        <input type="button" Class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdCancelar"  onclick="pulsarCancelar();" accesskey='C'>
    </div>
</div>
    <!-- Separador. -->
    <table height="2px" cellpadding="0px" cellspacing="0px"><tr><td></td></tr></table>
    <!-- Fin separador. -->
</BODY>    
</html:html>