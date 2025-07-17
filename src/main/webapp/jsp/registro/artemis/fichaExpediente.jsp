<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="java.util.ArrayList"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page language="java" contentType="text/html" pageEncoding="ISO-8859-15"%>

<%
    int apl = 4;
    int idioma=1;
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    if(usuario!=null){
         idioma = usuario.getIdioma();
    }
%>
<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title>::: Ficha Expediente:::</title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

    <!-- Estilos -->
     <style type="text/css">
              .dynamic-tab-pane-control .tab-page {
                  height:	200px;
              }

              .dynamic-tab-pane-control .tab-page .dynamic-tab-pane-control .tab-page {
                  height:	100px;
              }
     </style>

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
    <script type="text/javascript">
        var CONSTANTE_DE = " de ";
        var terceros = new Array();
        var total = 0;
        var posInteresadoActual = 0;

        function inicializar(){
            var cont = 0;
            <logic:iterate id="interesado" name="FichaExpedienteArtemisForm" property="empresasAdjudicatarias" scope="request">
                    terceros[cont]= [ '<bean:write name="interesado" property="nif"/>', '<bean:write name="interesado" property="nombreEmpresa"/>'];
                    cont= cont +1;
            </logic:iterate>

            inicializarInteresados();
        }

        function inicializarInteresados(){
            total = terceros.length;
            // Se actualiza la leyenda correspondiente
            var posicionLeyenda =0;
            if(total>0) posicionLeyenda = posInteresadoActual + 1;
            document.getElementById("msgInteresados").innerHTML = posicionLeyenda + CONSTANTE_DE + total;
            if(total>0){
                document.forms[0].documento.value = terceros[posInteresadoActual][0];
                document.forms[0].interesado.value = terceros[posInteresadoActual][1];
            }

            actualizarImagenAvanzarInteresados();
        }


        function interesadoAnterior(){
            if(total>0){                
                if(posInteresadoActual>0){
                    posInteresadoActual--;                    
                }
                
                var posicionLeyenda = 1;
                if(posInteresadoActual>0){
                    posicionLeyenda = posInteresadoActual + 1;
                }
                document.getElementById("msgInteresados").innerHTML = posicionLeyenda + CONSTANTE_DE + total;
                document.forms[0].documento.value = terceros[posInteresadoActual][0];
                document.forms[0].interesado.value = terceros[posInteresadoActual][1];
            }//if

            actualizarImagenAvanzarInteresados();
        }


        function interesadoSiguiente(){
            if(total>0){
                if((posInteresadoActual+1)<terceros.length){
                    posInteresadoActual++;
                }
                var posicionLeyenda = posInteresadoActual + 1;
                document.getElementById("msgInteresados").innerHTML = posicionLeyenda + CONSTANTE_DE + total;
                document.forms[0].documento.value = terceros[posInteresadoActual][0];
                document.forms[0].interesado.value = terceros[posInteresadoActual][1];
            }

            actualizarImagenAvanzarInteresados();
        }


        function actualizarImagenAvanzarInteresados(){
            if((posInteresadoActual+1)<total)
                document.getElementById("imgNext").style.color="#0B3090 !important";
            else
                document.getElementById("imgNext").style.color="#f6f6f6 !important";

            if((posInteresadoActual)>0)
                document.getElementById("imgAnt").style.color="#0B3090 !important";
            else
                document.getElementById("imgAnt").style.color="#f6f6f6 !important";
        }
        


        function pulsarVolver(){
            window.close();
        }

    </script>
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>


</head>
<body class="bandaBody" onload="pleaseWait('off');inicializar();">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

   <html:form action="/artemis/CargarFichaExpedienteArtemis.do">
       <div style="width:100%" class="txttitblanco"><%=descriptor.getDescripcion("tit_fichExp")%></div>
        <div class="encabezadoGris">
            <table width="100%" cellpadding="0px" cellspacing="0px" border="0">
                <tr>
                    <td class="etiqueta" style="width:10%" >
                        &nbsp;<%=descriptor.getDescripcion("etiq_numExp")%>:</td>
                    <td style="width:15%">
                        <html:text styleId="numExpediente" property="numExpediente" styleClass="inputTexto" size="20" readonly="true" onmouseover="titulo(this);"/>
                    </td>
                    <td>
                        <html:text styleId="procedimiento" property="nombreExpediente" styleClass="inputTexto" size="118" readonly="true" onmouseover="titulo(this);"/>
                    </td>                                                 
                </tr>
            </table>
        </div>
        <div class="contenidoPantalla" valign="top">
<div class="tab-pane" id="tab-pane-1">
    <script type="text/javascript">
        tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
    </script>
    <!-- CAPA 1: DATOS GENERALES ------------------------------ -->
    <div class="tab-page" id="tabPage1" style="height:60px" >
        <h2 class="tab" id="pestana1"><%=descriptor.getDescripcion("etiqDatos")%></h2>
        <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>
        <TABLE id ="tablaDatosGral" class="contenidoPestanha" border="0" cellspacing="2px" cellpadding="2px">
            <tr height="5px">
                <td class="columnP" colspan="7" style="width: 88%">
                    <table border="0" width="100%" align="left" cellpadding="0" cellspacing="0">
                        <tr>                                                                    
                            <td class="etiqueta" style="width: 9%"><%=descriptor.getDescripcion("gEtiqDocumento")%>:</td>
                            <td class="columnP" align="left" style="width: 15%" colspan="2">
                                <input type="text" class="inputTexto" size="20" maxlength="16" name="documento" class="inputTexto" readonly/>
                            </td>
                            <td width="8%">
                                &nbsp;
                            </td>
                            <td width="2%">
                                <a href="#" onclick="javascript:interesadoAnterior();">
                                    <span class="fa fa-arrow-circle-o-left" aria-hidden="true" name="imgAnt" id="imgAnt" style="color:#f6f6f6 !important" title="<%=descriptor.getDescripcion("etiqInterAnt")%>"></span>
                                </a>
                            </td>
                            <td width="10%" align="center">
                                <div id="msgInteresados" class="etiqueta">0 de 0</div>
                            </td>
                            <td>
                                <a href="#" onclick="javascript:interesadoSiguiente();">
                                    <span class="fa fa-arrow-circle-o-right" aria-hidden="true" name="imgNext" id="imgNext" style="color:#f6f6f6 !important" title="<%=descriptor.getDescripcion("etiqInterSig")%>"></span>
                                </a>
                            </td>
                            <td colspan="1">
                                    &nbsp;
                            </td>                                                                                                                                                
                        </tr>
                    </table>
                </td>
            </tr>
            <tr height="5px">
                <td class="etiqueta" colspan="4" style="width: 50%"><%=descriptor.getDescripcion("etiq_IntPrin")%>:</td>
                <td class="etiqueta" colspan="3">&nbsp;</td>
            </tr>
            <tr height="5px">
                <td class="columnP" colspan="4">
                    <input type="text" name="interesado" size="90"  class="inputTexto" readonly/>
                </td>
                <td class="columnP" colspan="3">
                     &nbsp;
                </td>
            </tr>
            <tr height="5px">
                <td class="etiqueta" colspan="4" style="width: 50%"><%=descriptor.getDescripcion("gEtiq_Asunto")%>:</td>
                <td class="etiqueta" colspan="3">&nbsp;</td>
            </tr>
            <tr height="5px">
                <td class="columnP" colspan="4">
                    <html:textarea styleClass="textareaTexto" cols="75" rows="6" property="objeto" readonly="true"></html:textarea>
                </td>
                <td class="columnP" colspan="3">
                    &nbsp;
                </td>
            </tr>                                                        
            <tr height="5px">
                <td colspan="7">
                    <table border="0" width="100%">
                    <tr>
                        <td class="etiqueta" style="width:15%"><%=descriptor.getDescripcion("gEtiq_fecIni")%>:</td>
                        <td class="columnP" style="width:12%"><html:text styleClass="inputTxtFecha" size="10" maxlength="9" property="fechaInicio" readonly="true"/></td>
                        <td class="etiqueta" style="width:13%"><%=descriptor.getDescripcion("gEtiq_fecFin")%>:</td>
                        <td class="columnP" style="width:15%"><html:text styleClass="inputTxtFecha" size="10" maxlength="9" property="fechaFin" readonly="true"/></td>
                        <td style="width:45%">&nbsp;</td>
                    </tr>
                    </table>
                </td>                                                            
            </tr>

            <tr>
                <td colspan="7" class="sub3titulo">Otros datos</td>
            </tr>

            <tr style="padding-bottom: 5px">
                <td class="etiqueta" style="width:18%"><%=descriptor.getDescripcion("gbInformeEstado")%>:</td>
                <td class="columnP">
                    <html:text styleClass="inputTexto" size="50" readonly="true" property="estadoExpediente"/>
                </td>
                <td class="etiqueta" style="width:18%"><%=descriptor.getDescripcion("etiqAreaAdquisiciones")%>:</td>
                <td class="columnP" colspan="4">
                    <html:text styleClass="inputTexto" size="30" readonly="true" property="areaAdquisicionesResponsable"/>
                </td>
            </tr>


            <!-- DEPARTAMIENTO RESPONSABLE Y NATURALEZA DEL CONTRATO -->
            <tr style="padding-bottom: 5px">
                <td class="etiqueta"><%=descriptor.getDescripcion("etiqDptoResponsable")%>:</td>
                <td class="columnP">
                    <html:text styleClass="inputTexto" size="50" readonly="true" property="departamentoResponsable"/>
                </td>
                <td class="etiqueta"><%=descriptor.getDescripcion("etiqNaturalezaContrato")%></td>
                <td class="columnP" colspan="4">
                    <html:text styleClass="inputTexto" size="50" readonly="true" property="naturalezaContrato"/>
                </td>
            </tr>


            <!-- ALCANCE MÁXIMO Y NÚMERO DE LOTES -->
            <tr style="padding-bottom: 5px">
                <td class="etiqueta"><%=descriptor.getDescripcion("etiqAlcanceMaximo")%>:</td>
                <td class="columnP">
                    <html:text styleClass="inputTexto" size="20" readonly="true" property="alcanceMaximo"/>
                </td>
                <td class="etiqueta"><%=descriptor.getDescripcion("etiqNumeroLotes")%>:</td>
                <td class="columnP" colspan="4">
                    <html:text styleClass="inputTexto" size="20" readonly="true" property="alcanceMaximo"/>
                </td>
            </tr>


            <!-- IMPORTE MAXIMO SIN IVA Y CON IVA  -->
            <tr style="padding-bottom: 5px">
                <td class="etiqueta"><%=descriptor.getDescripcion("etiqImpOfertaSinIva")%>:</td>
                <td class="columnP">
                    <html:text styleClass="inputTexto" size="20" readonly="true" property="importeModeloOfertaSinIVA"/>
                </td>
                <td class="etiqueta"><%=descriptor.getDescripcion("etiqImpOfertaConIva")%>:</td>
                <td class="columnP" colspan="4">
                    <html:text styleClass="inputTexto" size="20" readonly="true" property="importeModeloOfertaConIVA"/>
                </td>
            </tr>

             <!-- PLAZO RECEPCIÓN OFERTAS Y DURACIÓN CONTRATO-->
            <tr style="padding-bottom: 5px">
                <td class="etiqueta"><%=descriptor.getDescripcion("etiqPlazoRecepcionOfertas")%>:</td>
                <td class="columnP">
                    <html:text styleClass="inputTexto" size="20" readonly="true" property="plazoRecepcionOfertas"/>
                </td>
                <td class="etiqueta"><%=descriptor.getDescripcion("etiqDuracionContrato")%>:</td>
                <td class="columnP" colspan="4">
                    <html:text styleClass="inputTexto" size="20" readonly="true" property="duracionContrato"/>
                </td>
            </tr>


            <!-- CÓDIGOS CPV  -->
            <tr style="padding-bottom: 5px">
                <td class="etiqueta"><%=descriptor.getDescripcion("etiqCodigosCPV")%>:</td>
                <td class="columnP" colspan="6">
                    <bean:write name="FichaExpedienteArtemisForm" property="codigosCPV" ignore="true"/>
                </td>
            </tr>

            <TR>
                <TD style="width: 100%" colspan="7" style="text-align: center">
                  &nbsp;
                </TD>
            </TR>

        </table>
    </div>
    <div class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbVolver")%>" name="cmdVolver" onclick="pulsarVolver();">
    </div>
</div>
      
<script language="text/javascript">
   
        document.onmouseup = checkKeys;
        function checkKeysLocal(evento,tecla){
           var teclaAuxiliar = "";
            if(window.event){
                evento = window.event;
                teclaAuxiliar = evento.keyCode;
            }else
                teclaAuxiliar = evento.which;
            }

            if( (teclaAuxiliar == 40)|| (teclaAuxiliar== 38) ){
            if(tabAsientos==tableObject) {
                upDownTable(tabAsientos,listaAsientos,teclaAuxiliar);
            } else if(tabTramites==tableObject){
                upDownTable(tabTramites,listaTramites,teclaAuxiliar);
            } else if(tabDocumentos==tableObject){
                upDownTable(tabDocumentos,listaDocumentos,teclaAuxiliar);
            }
            }
            if(teclaAuxiliar == 13){
            if((tabAsientos.selectedIndex>-1)&&(tabAsientos.selectedIndex < listaAsientos.length)){
            callFromTableTo(tabAsientos.selectedIndex,tabAsientos.id);
            }
            if((tabTramites.selectedIndex>-1)&&(tabTramites.selectedIndex < listaTramites.length)){
            callFromTableTo(tabTramites.selectedIndex,tabTramites.id);
            }
            }
            keyDel(evento);
        }

    </script>    

</html:form>
</body>
</html:html>
