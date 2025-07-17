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
     <meta http-equiv="X-UA-Compatible" content="IE=10"/>
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
    %>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
    
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" 
                 type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
         
    <!-- ********************* JAVASCRIPT **************************    -->

    <script type="text/javascript">
        
      var pop_up; 
      var numExpediente = "<%=(String)request.getAttribute("numExpediente")%>";
            
      function pulsarCancelar(){            
        top.close();
      }
 
      // Carga de un campo suplemetario de tipo fichero del expediente
      function onClickDocumentoExpediente(codigo) {
          window.open("<%=request.getContextPath()%>/portafirmas/documentoportafirmas/VerDocumentacionExpedientePortafirmas.do?opcion=verFicheroCampoSuplementarioExpediente&codigo=" + codigo + "&numExpediente=" + numExpediente, "_blank",
                    "left=10, top=10, width=650, height=500, scrollbars=no, menubar=no, location=no, resizable=yes");
      }
      
      
      function onClickDocumentoExternoExpediente(codigo,extension,nombreDocumento) {    
          window.open("<%=request.getContextPath()%>/portafirmas/documentoportafirmas/VerDocumentacionExpedientePortafirmas.do?opcion=verDocumentoExternoExpediente&codigo=" + codigo + "&numExpediente=" + numExpediente + "&extension=" + escape(extension) + "&nombreDocumento=" + escape(nombreDocumento), "_blank",
                    "left=10, top=10, width=650, height=500, scrollbars=no, menubar=no, location=no, resizable=yes");
      }


      // Carga de un documento de registro o tramites
      function onClickDocumentoTramitacion(codigo,nombreDocumento,extension) {            
          var indiceDoc = codigo.indexOf("DOC-");
          var indiceAnexo = codigo.indexOf("ANEXO-");
          
          if(indiceDoc==0 && indiceAnexo==-1){          
                window.open("<%=request.getContextPath()%>/portafirmas/documentoportafirmas/VerDocumentacionExpedientePortafirmas.do?opcion=verDocumentoTramitacion&codigo=" + codigo + "&numExpediente=" + numExpediente + "&nombreDocumento=" + escape(nombreDocumento) + "&extension=" + escape(extension), "_blank",
                    "left=10, top=10, width=650, height=500, scrollbars=no, menubar=no, location=no, resizable=yes");
          }else
          if(indiceDoc==-1 && indiceAnexo==0){          
                window.open("<%=request.getContextPath()%>/portafirmas/documentoportafirmas/VerDocumentacionExpedientePortafirmas.do?opcion=verCampoFicheroTramite&codigo=" + codigo + "&numExpediente=" + numExpediente, "_blank",
                    "left=10, top=10, width=650, height=500, scrollbars=no, menubar=no, location=no, resizable=yes");
          }
      }
      
      
      function verDocumentoExpediente(codigo){       
            window.open("<%=request.getContextPath()%>/portafirmas/documentoportafirmas/VerDocumentacionExpedientePortafirmas.do?opcion=verDocumentoInicio&codigo=" + codigo + "&numExpediente=" + numExpediente, "_blank",
                    "left=10, top=10, width=650, height=500, scrollbars=no, menubar=no, location=no, resizable=yes");
        
      }
      
      
          
      function onClickDocumentoRegistro(codigo){  
            window.open("<%=request.getContextPath()%>/portafirmas/documentoportafirmas/VerDocumentacionExpedientePortafirmas.do?opcion=verDocumentoAnotacionRegistro&codigo=" + codigo + "&numExpediente=" + numExpediente, "_blank",
                    "left=10, top=10, width=650, height=500, scrollbars=no, menubar=no, location=no, resizable=yes");
        
      }
    
    </script>
    
</head>
    
<BODY class="bandaBody">
    <div id="titulo" class="txttitblanco">&nbsp;<%=descriptor.getDescripcion("etiqDocsExp")%> <%=(String)request.getAttribute("numExpediente")%></div>
    <div class="contenidoPantalla" id="tabla">
    <table style="width:100%" cellspacing="0px" cellpadding="0px" class="cuadroFondoBlanco" valign="top">
        <tr>
            <td valign="top">
                <table width="100%">

                    <!-- Documentos de inicio de expediente -->
                    <tr>
                        <td style="width: 100%; height: 20px" class="sub3titulo"><%=descriptor.getDescripcion("etiqDocsIniExp")%></td>
                    </tr>
                    <logic:iterate name="documentosExpediente" id="fichero">
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
                    <logic:iterate name="ficherosExpediente" id="fichero">
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
                    <logic:iterate name="otrosDocumentosExpediente" id="fichero"> 


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
                    <logic:iterate name="ficherosRegistroEntrada" id="fichero">
                        <tr>
                            <td class="etiqueta" style="width: 100%; height: 10px">
                                &nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="fichero" property="nombreAsiento"/>&nbsp;-&nbsp;
                                <a href="#" class="fichero" onclick="onClickDocumentoRegistro('<bean:write name="fichero" property="codigo"/>');">
                                   <bean:write name="fichero" property="nombre"/>
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
                                <a href="#" class="fichero" onclick="onClickDocumento('<bean:write name="fichero" property="codigo"/>');">
                                   <bean:write name="fichero" property="nombre"/>
                                </a>
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
                            <a href="#" class="fichero" onclick="onClickDocumentoTramitacion('<bean:write name="fichero" property="codigo"/>','<bean:write name="fichero" property="nombre"/>');">
                               <bean:write name="fichero" property="nombre"/>
                            </a> 
                        </tr>
                    </logic:iterate>

                </table>
            </td>
        </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbCerrar")%>' name="cmdCancelar"  onclick="pulsarCancelar();" accesskey='C'/>
    </div>             
</div>             
</BODY>    
</html:html>