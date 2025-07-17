<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%@ page import="java.util.Vector" %>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager" %>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO" %>
<%@ page import="es.altia.agora.interfaces.user.web.util.ValidarDocumento" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm"%>
<%@ page import="es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm"%>
<%@ page import="es.altia.agora.business.escritorio.RegistroUsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.technical.ConstantesDatos"%>

<html:html>
    <head>
        <TITLE>::: REXISTRO ENTRADA - Alta :::</TITLE>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
        <%
            MantAnotacionRegistroForm mantARForm;
            if (request.getAttribute("MantAnotacionRegistroForm") != null) {
                mantARForm = (MantAnotacionRegistroForm) request.getAttribute("MantAnotacionRegistroForm");
            } else {
                mantARForm = (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm");
            }

            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            RegistroUsuarioValueObject regUsuarioVO = new RegistroUsuarioValueObject();
            int idioma = 2;
            int apl = ConstantesDatos.APP_REGISTRO_ENTRADA_SALIDA;
            int cod_org = 1;
            int cod_dep = 1;
            int cod_ent = 1;
            int cod_unidOrg = 1;
            String css = "";

            String desc_org = "";
            String desc_ent = "";
            String respOpcion = "";
            String funcion = "";
            String dil = "";

            String idSesion = session.getId();

            if ((session.getAttribute("usuario") != null) && (session.getAttribute("registroUsuario") != null)) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                regUsuarioVO = (RegistroUsuarioValueObject) session.getAttribute("registroUsuario");
                idioma = usuarioVO.getIdioma();
                cod_org = usuarioVO.getOrgCod();
                cod_dep = regUsuarioVO.getDepCod();
                cod_ent = usuarioVO.getEntCod();
                cod_unidOrg = regUsuarioVO.getUnidadOrgCod();
                desc_org = usuarioVO.getOrg();
                desc_ent = usuarioVO.getEnt();
                css = usuarioVO.getCss();
            }


            String tipoAnotacion = "S";
            String tA = (String) session.getAttribute("tipEntrada");
            if (tA != null) {
                tipoAnotacion = tA;
            }

            String titPag;
            String tipo;
            String fech;
            String hora;
            String numOrden;
            String destino;
            String origen;


            if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion))) {
                titPag = "tit_BusquedaEntr";
                tipo = "res_tipoEntrada";
                fech = "res_fecE";
                hora = "res_HoraEnt";
                numOrden = "res_numOrdE";
                destino = "res_etiqDestino";
                origen = "res_etiqOrigen";
            } else {
                titPag = "tit_BusquedaSal";
                tipo = "res_tipoSalida";
                fech = "res_fecS";
                hora = "res_HoraSal";
                numOrden = "res_numOrdS";
                destino = "res_etiqOrigen";
                origen = "res_etiqDestino";
            }

            String[] params = usuarioVO.getParamsCon();
            Vector listaUORDTOs = UORsManager.getInstance().getListaUORs(false,params);

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");

            String userAgent = request.getHeader("user-agent");

        %>

        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
                     type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />


    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >
        <script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/uor.js'/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/listas.js'/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/seleccionBusquedaTerceros.js'/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/gestionTerceros.js'/>"></script>

        <script type="text/javascript">
    
  
            var codTipoDocs=new Array();
            var descTipoDocs=new Array();
            var cod_asuntos = new Array();
            var desc_asuntos = new Array();
            var uni_asuntos = new Array();
            var codEstados= new Array();
            var descEstados=new Array();
            var uors = new Array();
            var uorcods = new Array();
            var uorcodsInternos = new Array();

            <% for (int j = 0; j < listaUORDTOs.size(); j++) {
                    UORDTO dto = (UORDTO) listaUORDTOs.get(j);%>
                        uors[<%=j%>] = new Uor<%=dto.toJavascriptArgs()%>;
                        uorcods[<%=j%>] = '<%=dto.getUor_cod_vis()%>';
                        uorcodsInternos[<%=j%>] = '<%=dto.getUor_cod()%>';
            <% }%>


    
    
                function inicializar(){
                    cargaCombos();
                    habilitarImagenCal("calFechaAnotacion",true);
                    habilitarImagenCal("calHasta",true);
                    var argVentana = self.parent.opener.xanelaAuxiliarArgs;  

    
                    if((argVentana[0]!='') && (argVentana[1]!='')){
                        document.forms[0].ano.value = argVentana[0];
                        document.forms[0].numero.value = argVentana[1];   
                    }else{
                        document.forms[0].ano.value = "";
                        document.forms[0].numero.value = "";
                    }
   
   
                    pulsarLimpiar();
                    document.forms[0].tipoReg.value="<%=tipoAnotacion%>";
                    document.forms[0].UOR.value="<%=cod_unidOrg%>";
                    document.forms[0].cod_uniRegDestinoORD.value="";
                    consultando=false;

                }
                function cargaCombos(){
 
                        <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTiposIdInteresado">
                        codTipoDocs['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
                    descTipoDocs['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
                        </logic:iterate>
                        comboTipoDoc.addItems(codTipoDocs,descTipoDocs);
                    cont=0;
                        <logic:present name="MantAnotacionRegistroForm" property="listaAsuntos">
                        <logic:iterate id="asunto" name="MantAnotacionRegistroForm" property="listaAsuntos">
                        uni_asuntos[cont] = '<bean:write name="asunto" property="unidadRegistro"/>';
                    cod_asuntos[cont]  ='<bean:write name="asunto" property="codigo"/>';
                    desc_asuntos[cont] ="<str:escape><bean:write name="asunto" property="descripcion" filter="false"/></str:escape>";
                    cont++;
                        </logic:iterate>
                        </logic:present>

                        comboAsuntos.addItems(cod_asuntos, desc_asuntos);
                    comboAsuntos.selectItem(0);


                    codEstados=[0,1,2,9,-9,3];
                    descEstados=['<%=descriptor.getDescripcion("etiq_pendientes")%>',
                        '<%=descriptor.getDescripcion("etiq_aceptadas")%>',
                        '<%=descriptor.getDescripcion("etiq_rechazadas")%>',
                        '<%=descriptor.getDescripcion("etiq_anuladas")%>',
                        '<%=descriptor.getDescripcion("etiq_noAnuladas")%>',
                        '<%=descriptor.getDescripcion("etiq_asocExp")%>'];
                    comboEstados.addItems(codEstados, descEstados);
                    comboEstados.selectItem(-1);
      
                }

                function pulsarSalir(){
                    self.parent.opener.retornoXanelaAuxiliar();
                }

                function onClickDesc_uniRegDestinoORD() {
                    var condiciones = new Array();
                    condiciones[0]='UOR_OCULTA'+'зе';
                    condiciones[1]='N';

                    muestraListaTabla('UOR_COD_VIS', 'UOR_NOM', 'A_UOR', condiciones, 
                    'cod_uniRegDestinoORD', 'desc_uniRegDestinoORD', 'botonUnidadeRexistroORD', '100');
                }

                function mostrarCalDesde(evento) {
                    if(window.event) evento = window.event;
                    if (document.getElementById("calFechaAnotacion").className.indexOf("fa-calendar") != -1 )
                        showCalendar('forms[0]','fechaAnotacion',null,null,null,'','calFechaAnotacion','',null,null,null,null,null,null,null,null,evento);
                }

                function mostrarCalHasta(evento) {
                    if(window.event) evento = window.event;
                    if (document.getElementById("calHasta").className.indexOf("fa-calendar") != -1 )
                        showCalendar('forms[0]','fechaDocumento',null,null,null,'','calHasta','',null,null,null,null,null,null,null,null,evento);
                }
        
                function comprobarFecha(inputFecha){
                    if(Trim(inputFecha.value)!=''){
                        if(!ValidarFechaConFormato(document.forms[0],inputFecha)){
                            jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
                            inputFecha.focus();
                            return false;
                        }
                    }
                    return true;
                }
         
                function buscarDocTipoDoc(){

                    var tipo = document.forms[0].codTipoDoc.value;
                    if (!validarDocumento()) return;
                    document.forms[0].txtDNI.value=document.forms[0].txtDNI.value.toUpperCase();
                    document.forms[0].opcion.value="buscar";
                    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                    document.forms[0].target="oculto";
                    document.forms[0].submit();

                }


                function pulsarBuscarTerceros(){

                    var argumentos = new Array();

                    var argumentos = new Array();
                    argumentos['modo'] = 'seleccion';
                    argumentos['terceros'] =[ ];

                    var source = '<%=request.getContextPath()%>/BusquedaTerceros.do?opcion=inicializar&ventana=true&preguntaAlta=no';
                    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,argumentos,
                        'width=998,height=700,status='+ '<%=statusBar%>',function(datos){
                            if(datos!=undefined){
                                tercero=datos;
                                document.forms[0].codInter.value=datos[0];
                                document.forms[0].codTipoDoc.value= datos[4];
                                document.forms[0].txtDNI.value= datos[5];
                                document.forms[0].txtInteresado.value=datos[6]; // Razon Social.
                                mostrarDescripcionTipoDoc();
                            }
                        });
                }
 
                function mostrarDescripcionTipoDoc(){
                    if (!(Trim(document.forms[0].codTipoDoc.value) == '')) {
                        cargarDesc('codTipoDoc','descTipoDoc',codTipoDocs,descTipoDocs);
                    } else document.forms[0].descTipoDoc.value="";
                }

               
                function onClickHref_uniRegDestinoORD() {
                    var argumentos = new Array();
                    argumentos[0] = document.forms[0].cod_uor.value;
                    abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + APP_CONTEXT_PATH +
                            "/MantAnotacionRegistro.do?opcion=seleccionUOR&consultando=si",argumentos,
                            'width=670,height=470',function(datos){
                                if(datos != null) {
                                    document.forms[0].cod_uniRegDestinoORD.value = datos[0];
                                    document.forms[0].desc_uniRegDestinoORD.value = datos[1];
                                    document.forms[0].cod_uor.value = datos[2];
                                }    

                                if((document.forms[0].cod_uor.value != '') && (document.forms[0].desc_uniRegDestinoORD.value == '')) {        
                                    document.forms[0].cod_uniRegDestinoORD.value = '';
                                    document.forms[0].cod_uor.value = '';
                                }
                            });
                }


                function pulsarLimpiar(){
   
                    document.forms[0].txtDNI.value = "";
                    document.forms[0].txtInteresado.value="";
                    document.forms[0].cod_uor.value = "";
                    document.forms[0].desc_uniRegDestinoORD.value = "";
                    document.forms[0].observaciones.value = "";
                    document.forms[0].asunto.value = "";
                    document.forms[0].fechaAnotacion.value = "";
                    document.forms[0].fechaDocumento.value = "";
                    document.forms[0].codInter.value="";
                    document.forms[0].codTipoDoc.value = "";
                    document.forms[0].descTipoDoc.value="";
    
                }

                function pulsarConsultar() {  
  
                    var valido=true;
                    if (!comprobarFecha(document.forms[0].fechaAnotacion))
                        valido=false;
                    else if (!comprobarFecha(document.forms[0].fechaDocumento))
                        valido=false;
                    if (valido) {
   
                        document.forms[0].opcion.value="consultarAnotacionRelacion";
                        document.forms[0].target="mainFrame";
                        document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
                        document.forms[0].submit();
                    }

                }
     
     
                function comprobarFecha(inputFecha){
                    if(Trim(inputFecha.value)!=''){
                        if(!ValidarFechaConFormato(document.forms[0],inputFecha)){
                            jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
                            inputFecha.focus();
                            return false;
                        }
                    }
                    return true;
                }


                function recuperaBusquedaTerceros(datos){
                    if (datos.length>0){

                        var nombre=datos[0][4];
                        var ap1=datos[0][5];
                        var ap2=datos[0][6];
                        var razon = formatearNombre(nombre, ap1, ap2);
                        document.forms[0].txtInteresado.value=razon;
                        document.forms[0].codInter.value=datos[0][0];
                    }else{
                        jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoTercero")%>');
                    }
 

                }


                function validarNif(campo) {
                    var documento = campo.value;
                    var LONGITUD = 9;                    
                    var ultCaracter = documento.substring(documento.length - 1, documento.length);
                    if (isNaN(ultCaracter)) while (documento.length < LONGITUD) documento = "0" + documento;
                    else while (documento.length < LONGITUD - 1) documento = "0" + documento;

                    if (documento.length > LONGITUD) {
                        jsp_alerta('A','<%=descriptor.getDescripcion("msjDocIncorrecto")%>');
                        campo.value = '';
                        campo.focus();
                        return false;
                    }

                    if (documento.length == LONGITUD) {
                        var numDocumento = documento.substring(0, 8);
                        var letDocumento = documento.substring(8, 9);
                    } else {
                        var numDocumento = documento;
                        var letDocumento = '';
                    }

                    if (isNaN(numDocumento)) {
                        jsp_alerta('A','<%=descriptor.getDescripcion("msjDocIncorrecto")%>');
                        campo.value = '';
                        campo.focus();
                        return false;
                    }

                    var letraCorrecta = getLetraNif(numDocumento);
                    if (letDocumento == '') {
                        jsp_alerta('A','<%=descriptor.getDescripcion("msjLetraNif")%> ' + letraCorrecta);
                        campo.value = numDocumento;
                        campo.select();
                        return false;
                    }

                    if (letDocumento != letraCorrecta) {
                        var res = jsp_alerta('A','<%=descriptor.getDescripcion("msjLetraNif")%> ' + letraCorrecta + '. <%=descriptor.getDescripcion("msjPregContinuar")%>');
                        if (!(res > 0)) {
                            campo.value = numDocumento + letDocumento;
                            campo.select();
                            return false;
                        }
                    }
                    campo.value = numDocumento + letDocumento;
                    return true;
                }

                function getLetraNif(dni) {
                    var lockup = 'TRWAGMYFPDXBNJZSQVHLCKE';
                    return lockup.charAt(dni % 23);
                }

                function validarDocumento(){
                    var tipo = document.forms[0].codTipoDoc.value;
                    var documento = document.forms[0].txtDNI.value;

                    if (documento == '') return true;

                    if(tipo=="4" || tipo=="5"){
                        // Si se trata de un CIF
                        if(!validarCIF(documento)){
                            jsp_alerta('A','<%=descriptor.getDescripcion("msjDocIncorrecto")%>');
                            return false;
                        }
                        return true;
                    }
                    if(tipo=="1") return validarNif(document.forms[0].txtDNI);

                    // Validamos el pasaporte.
                    if (tipo=="2") return true;

                    // Validamos la tarjeta de residencia
                    if (tipo=="3") {
                        var nieCorrecto = validarNie(document.forms[0].txtDNI);
                        if (!nieCorrecto) jsp_alerta('A','<%=descriptor.getDescripcion("msjDocIncorrecto")%>');
                        return nieCorrecto;
                    }
                }
                
                function cambiaUnidadOrganica(){
                    uor = buscarUorPorCodVisible(uors, document.forms[0].cod_uniRegDestinoORD.value);
                    if(uor!=null){
                        document.forms[0].cod_uniRegDestinoORD.value = uor.uor_cod_vis;
                        document.forms[0].cod_uor.value = uor.uor_cod;
                        document.forms[0].desc_uniRegDestinoORD.value = uor.uor_nom;
                    } else {
                        // reseteamos los campos
                        document.forms[0].cod_uniRegDestinoORD.value = "";
                        document.forms[0].cod_uor.value = "";
                        document.forms[0].des_uniRegDestinoORD.value = "";
                    }
                        
                }
        </script>
    </head>
<body class="bandaBody" onload="pleaseWait('off');inicializar()">
<!-- Mensaje de esperar mientras carga  -->
<jsp:include page="/jsp/hidepage.jsp" flush="true">
    <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
</jsp:include>
<html:form action="/MantAnotacionRegistro.do" target="_self">
    <html:hidden  property="opcion" value=""/>
    <html:hidden  property="tipoConsulta" value="listado"/>
    <html:hidden  property="tipoReg" value=""/>
    <html:hidden  property="UOR" value=""/>
    <html:hidden  property="codInter" value=""/>
    <input type="hidden" name="lineasPagina" value="10">
    <input type="hidden" name="pagina" value="1">
    <input type="hidden" name="tipo_select"/>
    <input type="hidden" name="col_cod"/>
    <input type="hidden" name="col_desc"/>
    <input type="hidden" name="nom_tabla"/>
    <input type="hidden" name="input_cod"/>
    <input type="hidden" name="input_desc"/>
    <input type="hidden" name="column_valor_where"/>

    <% if ("S".equals(tipoAnotacion) || "Relacion_S".equals(tipoAnotacion)) {%>
    <div class="txttitblancoder"><%=descriptor.getDescripcion(titPag)%></div>
    <% } else {%>
    <div class="txttitblanco"><%=descriptor.getDescripcion(titPag)%></div>
    <% }%>
    <div class="encabezadoGris">
        <TABLE id="tablaBuscar" width="100%" cellpadding="0px" cellspacing="0px" border="0">
            <TR>
                <TD style="width: 30%" class="etiqueta">&nbsp;&nbsp;<%=descriptor.getDescripcion(numOrden)%></TD>
                <TD style="width: 70%" class="columnP">
            <html:text  styleClass='inputTexto' size="5" maxlength="4" property="ano"
                        onkeypress = "javascript:return SoloDigitos(event);"
                        onfocus="this.select();" />&nbsp;<span style="font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 11px; font-style:normal; font-weight: normal; font-variant: normal; color: #999999;">/</span>
            <html:text  styleClass='inputTexto' size="9" maxlength="8" property="numero"
                        onkeypress = "javascript:return SoloDigitos(event);"
                        onfocus="this.select();" />
            </TD>
        </TR>
    </TABLE>
</div>
<div class="contenidoPantalla" style="width:100%;">
                        <table style="width:100%;">
                            <TR>
                                <TD> 
                                    <TABLE style="width:100%;">
                                        <TR >

                                            <TD style="width: 17%;" class="etiqueta"> <%=descriptor.getDescripcion(fech)%>:</TD>
                                            <TD style="width: 10%" class="columnP">
                                                <INPUT TYPE="text"  class="inputTxtFecha"
                                                       size="12" maxlength="10" name="fechaAnotacion"
                                                       onkeypress="javascript:return soloCaracteresFecha(event);"
                                                       onblur="javascript:return comprobarFecha(this);"
                                                       onfocus="this.select();">
                                            </TD>
                                            <td class="columnP" style="width: 16%">
                                                <A href="javascript:calClick();return false;"
                                                   onClick="mostrarCalDesde(event);return false;" onblur="ocultarCalendarioOnBlur(); return false;">
                                                    <span class="fa fa-calendar" aria-hidden="true" style="border: 0; height: 17px; width: 25px;" name="calFechaAnotacion" id="calFechaAnotacion" alt="<%=descriptor.getDescripcion("altFecha")%>" title="<%=descriptor.getDescripcion("altFecha")%>" ></span>
                                                </A>
                                            </td>
                                            <TD style="width: 16%" class="etiqueta"><%=descriptor.getDescripcion("res_fecPres")%>: </TD>
                                            <TD style="width: 10%" class="columnP">
                                                <INPUT TYPE="text" class="inputTxtFecha" size="12" maxlength="10"
                                                       name="fechaDocumento"
                                                       onkeypress="javascript:return soloCaracteresFecha(event);"
                                                       onblur="javascript:return comprobarFecha(this);"
                                                       onfocus="this.select();">
                                            </TD>
                                            <td class="columnP">
                                                <A href="javascript:calClick();return false;"
                                                   onClick="mostrarCalHasta(event);return false;" onblur="ocultarCalendarioOnBlur(); return false;">
                                                    <span class="fa fa-calendar" aria-hidden="true" style="border: 0; height: 17px; width: 25px;" name="calHasta"  id="calHasta" alt="<%=descriptor.getDescripcion("altFecha")%>" title="<%=descriptor.getDescripcion("altFecha")%>"
                                                         ></span>
                                                </A>
                                            </td>
                                        </TR>
                                    </TABLE>
                                </TD>
                            </TR>
                            <TR>
                                <TD>
                                    <TABLE width="100%" cellspacing="0px" cellpadding="0px">
                                        <TR>
                                            <TD class="sub3titulo"><%=descriptor.getDescripcion("gEtiqIntDom")%></TD>
                                        </TR>
                                    </TABLE>
                                </TD>
                            </TR>
                            <TR>
                                <TD>
                                    <TABLE width="100%" cellspacing="0px" cellpadding="0px" border="0">
                                        <TR>
                                            <TD style="width: 17%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqIdentificacion")%>:</TD>
                                            <td style="width: 26%" class="columnP">
                                                <input id="codTipoDoc" name="codTipoDoc" type="text" class="inputTexto" size="3"
                                                       onkeypress="javascript:return SoloDigitosConsulta(event);">
                                                <input id="descTipoDoc" name="descTipoDoc" type="text" class="inputTexto" style="width: 155px" size="25" readonly>
                                                <a id="anchorTipoDoc" name="anchorTipoDoc" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoDoc" name="botonTipoDoc" style="cursor:hand;"	alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>" border="0" style="cursor:hand;"></span></a>
                                            </td>
                                            <td style="width: 12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqDocumento")%>:</td>
                                            <td style="width: 32%" class="columnP" align="left">
                                                <input type="text" class="inputTexto" size="20" maxlength="16" name="txtDNI" onKeyPress="javascript:PasaAMayusculas(event);" onchange="javascript:buscarDocTipoDoc();">
                                            </td>
                                            <td>
                                                <span class="fa fa-users" aria-hidden="true"  name="botonT" alt = "<%=descriptor.getDescripcion("altBuscInt")%>" title = "<%=descriptor.getDescripcion("altBuscInt")%>" style="cursor:pointer; border: 0px none" onclick= "pulsarBuscarTerceros();"></span>
                                            </td>

                                        </TR>
                                    </TABLE>
                                </TD>
                            </TR>
                            <tr>
                                <td>
                                    <table width="100%" cellspacing="0px" cellpadding="0px" border="0px">
                                        <tr>
                                            <td style="width: 17%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqNombreRazon")%>:</td>
                                            <td>
                                                <input type="text" id="obligatorio" class="inputTextoObligatorio" size="124" maxlength=80 name="txtInteresado" style="width:700px" onKeyPress="javascript:PasaAMayusculas(event);">
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <TR>
                                <TD>
                                    <TABLE width="100%" cellspacing="0px" cellpadding="0px" style="margin:4px 0px 4px 0px;">
                                        <TR>
                                            <TD class="sub3titulo"><%=descriptor.getDescripcion("gEtiqDatosDocumento")%></TD>
                                        </TR>
                                    </TABLE>
                                </TD>
                            </TR>
                            <TR>
                                <TD>
                                    <TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <TR>
                                            <TD style="width: 8%" class="etiqueta"><%=descriptor.getDescripcion("res_asunto")%>:</TD>
                                            <TD  valign="top">

                                        <html:text styleClass="inputTexto" property="codAsunto" style="width:70px;height:17px" size="4" maxlength="10" onkeypress="javascript:PasaAMayusculas(event);"/>
                                        <html:text styleClass="inputTexto" property="descAsunto"  style="width:750px;height:17px" readonly="true"/>
                                        <A href="" style="text-decoration:none;" id="anchorUnidadRegistro" name="anchorAsunto">
                                            <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonAsunto" name="botonAsunto" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
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
                                <TD ></TD>
                                <TD class="etiqueta"><%=descriptor.getDescripcion("etiq_Extracto")%>:</TD>
                                <TD  class="etiqueta"><%=descriptor.getDescripcion("etiq_observaciones")%>:</TD>
                            </TR>
                            <TR>
                                <TD style="width: 8%"></TD>

                                <TD  class="columnP">
                                        <html:textarea styleId="obligatorio" styleClass="textareaTextoObligatorio" cols="73" rows="4" property="asunto"  maxlength="2" onkeydown="return textCounter(this,4000);" onkeypress="javascript:PasaAMayusculas(event);" value=""></html:textarea>
                                </TD>
                                <TD  class="columnP">
                            <html:textarea styleClass="textareaTexto" property="observaciones" style="width:368px" rows="4"  maxlength="2"
                                           onkeydown="return textCounter(this,4000);" onkeypress="javascript:PasaAMayusculas(event);" value=""></html:textarea>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR> 

        <TR>
            <TD>
                <TABLE width="100%" border="0" bordercolor="purple" cellspacing="0" cellpadding="0">
                    <TR>
                        <%
                            if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion))) {
                        %>
                        <TD style="width: 8%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqUnidDestino")%>:</TD>
                        <% } else {%>
                        <TD style="width: 8%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqUnidOrigen")%>:</TD>
                        <% }%>
                        <TD class="columnP" valign="bottom" >
                    <html:hidden property="cod_uor" value=""/>
                    <html:text styleClass="inputTextoObligatorio" property="cod_uniRegDestinoORD" size="8" onblur="javascript:cambiaUnidadOrganica();"
                               onkeypress="javascript:PasaAMayusculas(event);"/>
                    <html:text styleClass="inputTextoObligatorio"  property="desc_uniRegDestinoORD" 
                               style="width:750;height:17"  onclick="javascript:{onClickDesc_uniRegDestinoORD();}" readonly="true"/>
                    <A href="javascript:{onClickHref_uniRegDestinoORD();}" style="text-decoration:none;" id="anclaD" 
                       name="anchorUnidadeRexistroORD"
                       >
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp" name="botonUnidadeRexistroORD" alt="<%=descriptor.getDescripcion("altDesplegable")%>" alt="<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                    </A>
            </TD>
        </TR>
    </TABLE>
    </TD>
    </TR>   

    <TR>
        <TD>
            <TABLE width="100%" cellspacing="0px" cellpadding="0px" border="0">
                <TR>
                    <TD style="width: 8%; text-align: left" class="etiqueta"><%=descriptor.getDescripcion("etiq_estadoAnot")%>:&nbsp;</TD>
                    <TD valign="top" style="width: 292px;" class="columnP">
                        <input id="codEstado" name="codEstado" type="text" class="inputTextoObligatorio" size="3">
                        <input id="descEstado" name="descEstado" type="text" class="inputTextoObligatorio" style="width:174" readonly>
                        <a id="anchorEstado" name="anchorEstado" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoDoc" name="botonTipoDoc"	style="cursor:pointer;" alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>" border="0" style="cursor:hand;"></span></a>                           
                    </TD>
                    <TD style="width: 19%" class="etiqueta"><%=descriptor.getDescripcion("res_ExpRel")%>:</TD>
                    <TD class="columnP">
                <html:text  styleClass="inputTexto" style="width:180;height:17" maxlength="18" property="txtExp1" onkeypress="javascript:PasaAMayusculas(event);" onchange="javascript:{}" onfocus="javascript:{}"/>
        </TD>               
    </TR>
    </TABLE>
        </TD>               
    </TR>
    </TABLE>
    <div class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbBuscar")%> name="cmdBuscar" onClick="pulsarConsultar();" alt="<%= descriptor.getDescripcion("toolTip_bBuscar")%>" title="<%= descriptor.getDescripcion("toolTip_bBuscar")%>">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%> name="cmdModificar" onClick="pulsarLimpiar();" title='<%=descriptor.getDescripcion("toolTip_bLimpiar")%>' alt='<%=descriptor.getDescripcion("toolTip_bLimpiar")%>'>
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir" onClick="pulsarSalir();" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>'>
    </div>
</div>
</html:form>

<div id="desplegable" style="overflow-y: auto; overflow-x: no; visibility: hidden; BORDER: 0px"></div>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaCombo.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript">

    var comboTipoDoc = new Combo("TipoDoc");
    var comboAsuntos = new Combo("Asunto");
    var comboEstados = new Combo("Estado");

    comboTipoDoc.change = function() {
        document.forms[0].txtDNI.value="";
    }


    //Usado para el calendario
    var coordx=0;
    var coordy=0;


        <%if (userAgent.indexOf("MSIE") == -1) {%> //Que no sea IE
        window.addEventListener('mousemove', function(e) {
            coordx = e.clientX;
            coordy = e.clientY;
        }, true);
        <%}%>


        document.onmouseup = checkKeys;

        function checkKeysLocal(evento,tecla) {
            var teclaAuxiliar = "";
            if(window.event){
                evento = window.event;
                teclaAuxiliar = evento.keyCode;
            }else{
                teclaAuxiliar = evento.which;
            }


            if (teclaAuxiliar == 9){

                if(layerVisible) ocultarDiv();
                if(divSegundoPlano) divSegundoPlano = false;

                comboTipoDoc.ocultar();
                comboAsuntos.ocultar();
                comboEstados.ocultar();
    
                if(IsCalendarVisible) hideCalendar();

                return false;
            }
            if (teclaAuxiliar == 1){

                if(layerVisible) setTimeout('ocultarDivNoFocus()',40);
                if(divSegundoPlano)	divSegundoPlano = false;

                if (comboTipoDoc.base.style.visibility == "visible" && isClickOutCombo(comboTipoDoc,coordx,coordy)) setTimeout('comboTipoDoc.ocultar()',20);
                if (comboAsuntos.base.style.visibility == "visible" && isClickOutCombo(comboAsuntos,coordx,coordy)) setTimeout('comboAsuntos.ocultar()',20);
                if (comboEstados.base.style.visibility == "visible" && isClickOutCombo(comboEstados,coordx,coordy)) setTimeout('comboEstados.ocultar()',20);

                if(IsCalendarVisible) replegarCalendario(coordx,coordy);

            } if (evento.button == 1) {
                if(layerVisible) ocultarDiv();
                if(divSegundoPlano) divSegundoPlano = false;
            }
        }
     
    </script>

</body>
</html:html>
