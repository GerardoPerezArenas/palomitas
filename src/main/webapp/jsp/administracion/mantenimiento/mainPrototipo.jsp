<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
    <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
    <title>Mantenimiento de Procesos </title>
    <!-- Estilos -->

    <%
        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        int idioma = 1;
        int apl = 5;
        if (session.getAttribute("usuario") != null) {
            usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
            apl = usuarioVO.getAppCod();
            idioma = usuarioVO.getIdioma();
        }%> 
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <TITLE>::: REXISTRO ENTRADA - Alta :::</TITLE>
<script>
    <%
     MantenimientosAdminForm bForm = (MantenimientosAdminForm) session.getAttribute("MantenimientosAdminForm");
     Vector listaOrganizaciones = bForm.getListaOrganizaciones();
     Vector listaCss = bForm.getListaCss();
     String ruta="/css/estilo.css";
     int lengthCss = listaCss.size();
     int i = 0;
      %>
          //sacamos listado de estilos
          var z=0;
     <%for (i = 0; i < lengthCss; i++) {
         GeneralValueObject css = (GeneralValueObject) listaCss.get(i);
         ruta=(String) css.getAtributo("ruta");
     }
     %>
</script>
    <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=ruta%>">
    <style type="text/css">
        /* Override styles from webfxlayout */
        /* Para colocar las pestañas correctamente */

        .dynamic-tab-pane-control .tab-page {
            height:		250px;
        }

        .dynamic-tab-pane-control .tab-page .dynamic-tab-pane-control .tab-page {
            height:		150px;
        }

    </style>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaBusquedaTerceros.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
    <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css">
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/altaRE.js"></script>

    <script type="text/javascript">
    function  pulsarSalir(){
       this.close();
   }
</script>
</head>
<BODY class="bandaBody">
    <form method="post" target="_self">
        <div class="txttitblanco">Entradas</td>
        <div class="encabezadoGris">
            <TABLE id="tablaBuscar" width="100%" cellpadding="0px" cellspacing="0px" border="0">
                <TR>
                    <TD width="28%" class="etiqueta">Número de Orden de Entrada:</TD>
                    <TD width="18%" class="columnP">
                        <input type="text" name="ano" maxlength="4" size="5" value="" onkeypress="javascript:return SoloDigitos(event);" onfocus="this.select();" class="inputTexto">&nbsp;<span style="font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 11px; font-style:normal; font-weight: normal; font-variant: normal;">/</span>
                        <input type="text" name="numero" maxlength="8" size="9" value="" onkeypress="javascript:return SoloDigitos(event);" onfocus="this.select();" class="inputTexto">
                    </TD>
                    <td width = "50%">
                    </td>
                </TR>
            </TABLE>
        </div>
        <div class="contenidoPantalla">									
            <div class="tab-pane" id="tab-pane-1" >
                <script type="text/javascript">
                    tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
                </script>
                <!-- CAPA 1: DATOS GENERALES------------------------------ -->
                <div class="tab-page" id="tabPage1" style="height:100%">
                    <h2 class="tab">Datos generales</h2>
                    <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>

                    <TABLE id ="tablaDatosGral" class="contenidoPestanha">
                        <TR>
                            <TD>
                                <TABLE width="100%" cellspacing="0px" cellpadding="0px" border="0px">
                                    <TR>
                                        <TD width="16%" class="etiqueta"> Fecha Entrada:</TD>
                                        <TD width="26%" class="columnP">
                                            <input type="text" name="fechaAnotacion" size="18" value="" onblur="javascript:return comprobarFecha(this);" onfocus="javascript: this.select();" class="inputTxtFechaObligatorio" id="obligatorio">
                                            &nbsp;
                                            <A href="javascript:calClick();return false;" onClick="" style="text-decoration:none;">
                                                <span class="fa fa-calendar" aria-hidden="true" name="calFechaAnotacion" alt="Data" ></span>
                                            </A>
                                        </TD>
                                        <TD width="17%" class="etiqueta">Hora de Entrada:</TD>
                                        <TD class="columnP">
                                            <input type="text" name="horaMinAnotacion" maxlength="5" size="6" value="" onkeypress="javascript:return soloCaracteresHora(event);" onblur="javascript:return comprobarHora(this, mensajeHora);" onfocus="javascript: this.select();" class="inputTxtFecha">
                                        </TD>	  															
                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>
                        <TR>
                            <TD>
                                <TABLE width="100%" cellspacing="0" cellpadding="0" border="0">
                                    <TR>
                                        <TD width="16%" class="etiqueta">Fecha Presentación: </TD>
                                        <TD width="26%" class="columnP">
                                            <input type="text" name="fechaDocumento" size="18" value="" onblur="javascript:return comprobarFecha(this);" onfocus="this.select();" class="inputTxtFechaObligatorio" id="obligatorio">
                                            &nbsp;
                                            <A href="javascript:calClick();return false;" onClick="" style="text-decoration:none;">
                                                <span class="fa fa-calendar" aria-hidden="true" name="calDesde" alt="Data" ></span>
                                            </A>
                                        </TD>
                                        <TD width="17%" class="etiqueta">Hora de Presentación:</TD>
                                        <TD class="columnP">
                                            <input type="text" name="horaMinDocumento" maxlength="5" size="6" value="" onkeypress="javascript:return soloCaracteresHora(event);" onblur="javascript:return comprobarHora(this, mensajeHora);" onfocus="javascript: this.select();" class="inputTxtFecha">
                                        </TD>
                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>      												
                        <!-- Unidad Organica -->
                        <TR>
                            <TD>
                                <TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <TR>
                                        <TD width="16%" class="etiqueta">Unidad Tramitadora:</TD>
                                        <TD width="84%" class="columnP" valign="top">
                                            <input type="hidden" name="cod_uniRegDestinoORD" value="">
                                            <input type="text" name="cod_uor" size="8" value="" class="inputTextoObligatorio">
                                            <input type="text" name="desc_uniRegDestinoORD" value="" readonly="readonly" style="width:320;height:17" class="inputTextoObligatorio">
                                            <A href="" style="text-decoration:none;" id="anclaD" name="anchorUnidadeRexistroORD" onclick="">
                                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp" name="botonUnidadeRexistroORD" style="cursor:hand;"></span>
                                            </A>
                                        </TD>
                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>
                        <TR>
                            <TD>
                                <TABLE width="100%" cellspacing="0px" cellpadding="0px" style="margin-top:1px">
                                    <TR>
                                        <TD class="sub3titulo">&nbsp;Interesado y Domicilio</TD>
                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>
                        <TR>
                            <TD>
                                <TABLE width="100%" cellspacing="0px" cellpadding="0px" border="0">
                                    <TR>
                                        <TD width="16%" class="etiqueta">Tipo Documento:</TD>
                                        <TD width="26%" class="columnP">
                                            <input type="text" name="cbTipoDoc" size="3" value="" onchange="" onblur="" onfocus="javascript:this.select();  analizarDocumento();" class="inputTextoObligatorio" id="obligatorio">
                                            <input type="text" name="descTipoDoc" value="N.I.F." onclick="" onblur="javascript:{onblurTipoDocumento('cbTipoDoc','descTipoDoc');}" onfocus="javascript:{onFocusDescTipoDoc();}" readonly="readonly" style="width:140;height:17" class="inputTextoObligatorio" id="obligatorio">
                                            <A href="" style="text-decoration:none;" id="anclaD" name="anchorTipoDoc" onclick="">
                                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonTipoDoc" style="cursor:hand;"></span>
                                            </A>
                                        </TD>
                                        <TD width="10%" class="etiqueta">Documento:</TD>
                                        <TD width="12%" class="columnP">
                                            <input type="text" name="txtDNI" maxlength="16" size="12" value="" onkeypress="javascript:PasaAMayusculas(event);" onchange="javascript:buscarDocTipoDoc();" class="inputTextoObligatorio" id="obligatorio">
                                        </TD>
                                        <TD class="columnP">
                                            <span class="fa fa-search" aria-hidden="true"   name="botonT" alt="Buscar Interesado" onclick="javascript:pulsarBuscarTerceros();"></span>
                                            <span class="fa fa-users" aria-hidden="true" name="botonTer" alt="Mantenimiento de Terceros" onclick=""></span>
                                        </TD>
                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>
                        <TR>
                            <TD>
                                <TABLE width="100%" cellspacing="0px" cellpadding="0px" border="0">
                                    <TR>
                                        <TD width="16%" class="etiqueta">Nombre/<BR>Razón social:</TD>
                                        <TD width="84%">
                                            <input type="text" name="txtInteresado" maxlength="50" tabindex="-1" value="" onkeypress="javascript:PasaAMayusculas(event);" style="width:735;" class="inputTexto">
                                        </TD>
                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>
                        <TR>
                            <TD>
                                <TABLE width="100%" cellspacing="0px" cellpadding="0px">
                                    <TR>
                                        <TD width="16%" class="etiqueta">Tlfno/Fax:</TD>
                                        <TD width="26%" class="etiqueta">
                                            <input type="text" name="txtTelefono" maxlength="20" tabindex="-1" value="" readonly="readonly" style="width:120;" class="inputTexto">
                                        </TD>
                                        <TD width="10%" class="etiqueta">e-mail:</TD>
                                        <TD class="columnP">
                                            <input type="text" name="txtCorreo" maxlength="30" tabindex="-1" value="" readonly="readonly" style="width:299;" class="inputTexto">
                                        </TD>
                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>
                        <TR>
                            <TD>
                                <TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <TR>
                                        <TD width="16%" class="etiqueta">Provincia:</TD>
                                        <TD width="26%" class="columnP" valign="top">                    														
                                            <input type="text" name="txtProv" tabindex="-1" value="" readonly="readonly" style="width:208;" class="inputTexto" id="txtProv"> 
                                        </TD>
                                        <TD width="10%" class="etiqueta">Municipio:</TD>
                                        <TD class="columnP" valign="top">
                                            <input type="text" name="txtMuni" tabindex="-1" value="" readonly="readonly" style="width:210;" class="inputTexto" id="txtMuni">
                                        </TD>
                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>
                        <TR>
                            <TD>
                                <TABLE width="100%" cellspacing="0px" cellpadding="0px">
                                    <TR>
                                        <TD width="16%" class="etiqueta">Domicilio:</TD>
                                        <TD width="55%" class="columnP">
                                            <input type="text" name="txtDomicilio" maxlength="60" tabindex="-1" value="" readonly="readonly" style="width:476;" class="inputTexto">
                                        </TD>
                                        <td width="9%" class="etiqueta">C. Postal:</td>
                                        <td class="columnP">
                                            <input type="text" name="txtCP" maxlength="5" tabindex="-1" value="" readonly="readonly" style="width:56;" class="inputTexto">
                                        </td>
                                    </tr>	
                                </TABLE>
                            </TD>
                        </TR>
                        <TR>
                            <TD>
                                <TABLE width="100%" cellspacing="0px" cellpadding="0px" style="margin-top:3px">
                                    <TR>
                                        <TD class="sub3titulo">&nbsp;Destino/Origen</TD>
                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>
                        <TR>
                            <TD>
                                <TABLE width="100%" cellspacing="0px" cellpadding="0px">
                                    <TR>
                                        <TD width="16%" class="etiqueta">Tipo entrada:</TD>
                                        <TD valign="top" class="columnP">
                                            <input type="text" name="cbTipoEntrada" maxlength="1" size="8" value="0" onkeypress="javascript:return SoloDigitos(event);" onchange="javascript:{divSegundoPlano=true;inicializarValores('cbTipoEntrada','txtNomeTipoEntrada',cod_tipoEntrada,desc_tipoEntrada);}" onblur="javascript:{mostrarDestino();}" onfocus="javascript:this.select(); mostrarDestino();" class="inputTextoObligatorio" id="obligatorio">
                                            <input type="text" name="txtNomeTipoEntrada" value="" onclick="" onchange="" onblur="javascript:mostrarDestino();" onfocus="javascript:{divSegundoPlano=true;inicializarValores('cbTipoEntrada','txtNomeTipoEntrada',cod_tipoEntrada,desc_tipoEntrada);}" readonly="readonly" style="width:320;height:17" class="inputTextoObligatorio" id="obligatorio">
                                            <A href="javascript:{divSegundoPlano=false;inicializarValores('cbTipoEntrada','txtNomeTipoEntrada',cod_tipoEntrada,desc_tipoEntrada);}" style="text-decoration:none;" id="anclaD" name="anchorTipoEntrada" onclick="">
                                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonTipoEntrada" style="cursor:hand;"></span>
                                            </A>
                                        </TD>
                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>
                        <TR>
                            <TD>
                                <TABLE width="100%" cellspacing="0px" cellpadding="0px">
                                    <TR>
                                        <TD width="16%" class="etiqueta">Tipo documento:</TD>
                                        <TD valign="top" class="columnP">
                                            <input type="text" name="txtCodigoDocumento" size="8" value="" onkeypress="javascript:PasaAMayusculas(event);" onchange="javascript:onchangeCampoCodigo('txtCodigoDocumento','txtNomeDocumento',cod_tiposDocumentos,desc_tiposDocumentos,operadorConsulta);" onfocus="javascript:this.select();" class="inputTextoObligatorio" id="obligatorio">
                                            <input type="text" name="txtNomeDocumento" value="" onclick="" onfocus="javascript:onFocusCampoDesc('txtCodigoDocumento','txtNomeDocumento',cod_tiposDocumentos,desc_tiposDocumentos,operadorConsulta);" readonly="readonly" style="width:320;height:17" class="inputTextoObligatorio" id="obligatorio">
                                            <A href="javascript:{onClickCampoDesc('txtCodigoDocumento','txtNomeDocumento',cod_tiposDocumentos,desc_tiposDocumentos);}" style="text-decoration:none;" name="anchorTipoDocumento" id="anclaD" onclick="">
                                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonTipoDocumento" style="cursor:hand;"></span>
                                            </A>
                                        </TD>
                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>
                        <TR>
                            <TD>
                                <TABLE width="100%" cellspacing="0px" cellpadding="0px" style="margin-top:3px">
                                    <TR>
                                        <TD class="sub3titulo">&nbsp;Documento</TD>
                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>
                        <TR>
                            <TD>
                                <TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <TR>
                                        <TD width="50%" class="etiqueta">Asunto:</TD>
                                        <TD width="50%" class="etiqueta">Observaciones:</TD>

                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>
                        <TR>
                            <TD>
                                <TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <TR>
                                        <TD width="49.5%" class="columnP">
                                            <textarea name="asunto" cols="68" rows="4" style="width:450px" 
                                                      onkeydown="return textCounter(this,4000);" onkeypress="javascript:PasaAMayusculas(event);" class="textareaTextoObligatorio" id="obligatorio"></textarea>
                                        </TD>
                                        <TD width="49.5%" class="columnP">
                                            <textarea name="observaciones" cols="68" rows="4" style="width:450px" 
                                                      onkeydown="return textCounter(this,4000);" onkeypress="javascript:PasaAMayusculas(event);" class="textareaTexto"></textarea>
                                        </TD>

                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>
                        <TR>
                            <TD>
                                <TABLE width="100%" border="0" cellspacing="0px" cellpadding="0px">
                                    <TR>
                                        <TD width="16%" class="etiqueta">Estado:</TD>
                                        <TD valign="top" class="columnP">
                                            <input type="text" name="cod_estadoAnotacion" maxlength="1" size="8" value="" onkeypress="javascript:return SoloDigitos(event);" onchange="javascript:{divSegundoPlano=true;inicializarValores('cod_estadoAnotacion','desc_estadoAnotacion',cod_estadosAnotaciones,desc_estadosAnotaciones);}" onfocus="javascript:this.select();" style="align:right" class="inputTexto" id="noObligatorio">
                                            <input type="text" name="desc_estadoAnotacion" value="" onclick="" onfocus="javascript:{divSegundoPlano=true;inicializarValores('cod_estadoAnotacion','desc_estadoAnotacion',cod_estadosAnotaciones,desc_estadosAnotaciones);}" readonly="readonly" style="width:320;height:17" class="inputTexto" id="noObligatorio">
                                            <A href="javascript:{divSegundoPlano=false;inicializarValores('cod_estadoAnotacion','desc_estadoAnotacion',cod_estadosAnotaciones,desc_estadosAnotaciones);}" style="text-decoration:none;" id="anclaD" name="anchorEstadoAnotacion" onclick="">
                                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonEstadoAnotacion" style="cursor:hand;"></span>
                                            </A>
                                        </TD>
                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>
                        <TR>
                            <TD>
                                <TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <TR>
                                        <TD width="16%" class="etiqueta">Autoridad a la que se dirige:</TD>
                                        <TD class="columnP">
                                            <input style="width: 735px;" type="text" property="autoridad" size="64" maxlength="64" value="" class="inputTexto"/>                      
                                        </TD>
                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>
                    </TABLE>
                </div>
                <!-- CAPA 3: DESTINO  y ORIGEN -->
            </div>
            <DIV id="capaBotones2" name="capaBotones2" class="botoneraPrincipal">
                <input type="button" title='Volver a la pantalla anterior' class="botonGeneral" value='Salir'
                       name="cmdCancelarAlta" onClick="pulsarSalir();"/>
            </DIV>
        </div>
    </form>
        
    
    <div id="desplegable" style="overflow-y: auto; overflow-x: no; visibility: hidden; BORDER: 0px"></div>

    <script type="text/javascript" src="scripts/listaCombo.js"></script>
    </BODY>
    
</html>
