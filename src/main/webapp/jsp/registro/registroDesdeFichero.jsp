<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.interfaces.user.web.registro.RegistroDesdeFicheroForm" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO"%>

<html>
    <head>
        <title>REGISTRO DESDE FICHERO</title>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
        <meta http-equiv="" content="text/html; charset=iso-8859-1">
        <!-- Estilos -->
       
        
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 2;
            int orgCod = 0;
            int entCod = 0;
            String css = "";
            RegistroDesdeFicheroForm rForm = new RegistroDesdeFicheroForm();
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                rForm = (RegistroDesdeFicheroForm) session.getAttribute("RegistroDesdeFicheroForm");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                orgCod = usuarioVO.getOrgCod();
                entCod = usuarioVO.getEntCod();
                css = usuarioVO.getCss();
            }
            String tipoRegistro = rForm.getTipoRegistro();
            String etiqTitulo = "etiq_DatosSalida";
            if ("E".equals(tipoRegistro)) {
                etiqTitulo = "etiq_DatosEntrada";
            }
            
            String directivaUsuario=rForm.getDirectivaUsuario();
            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
        %>
        
        
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        
        <!-- Ficheros JavaScript -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/uor.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >
        
        
        <script type="text/javascript">
            
            
            var uors = new Array();
            var uorcods = new Array();
            var nombreFicheroFisico = '';
            
            var EsquemaGenerico = "<%=es.altia.agora.business.util.GlobalNames.ESQUEMA_GENERICO%>"
            
            function inicializar(){
                // === UORs
    <%Vector listaUORDTOs = rForm.getListaUORs();
            for (int j = 0; j < listaUORDTOs.size(); j++) {
                UORDTO dto = (UORDTO) listaUORDTOs.get(j);%>
                    // array con los objetos tipo uor mapeados por el array de arriba
                    uors[<%=j%>] = new Uor<%=dto.toJavascriptArgs()%>;
                        // array con los códigos visibles
                        uorcods[<%=j%>] = '<%=dto.getUor_cod_vis()%>';
                            <%}%>
                            // === fin seccion UORs
                            pleaseWait1('off',top.mainFrame);
                            
                        }    		
                        
                        function pulsarAceptar(){
                            if(validarFormulario()){        
                                pleaseWait1('on',top.mainFrame);
                                document.forms[0].opcion.value = "cargar";
                                document.forms[0].target = "oculto";
                                document.forms[0].action = "<%=request.getContextPath()%>/registro/RegistroDesdeFichero.do";
                                document.forms[0].submit();
                                
                            }
                        }
                        
                        function pulsarSalir(){
                            window.location = "<%=request.getContextPath()%>/MantAnotacionRegistro.do?opcion=<%=tipoRegistro%>";
                        }
                        
                        function resultadoInsertarDesdeFichero(resultado,registrosNoIns) {
                            pleaseWait1('off',top.mainFrame);
                            var mnsx =	'<%=descriptor.getDescripcion("etiqFichSinErr")%>'
                            if (resultado=='numerosReservadosDistintoLineasFichero') {
                                mnsx =	'<%=descriptor.getDescripcion("etiqNoResLin")%>'
                                jsp_alerta("A", mnsx);    	
                            } else if (resultado=='numerosNoReservados') {
                            mnsx =	'<%=descriptor.getDescripcion("etiqNoRes")%>'
                            var tam = registrosNoIns.length
                            if (registrosNoIns.length>10) tam=10;			
                            for (var i=0; i<tam; i++){
                                mnsx += " "+ registrosNoIns[i][0]
                            }
                            if (registrosNoIns.length>10) mnsx +="...";
                            else mnsx +=".";			
                            jsp_alerta("A", mnsx);    		
                        } else if (resultado=='ficheroProcesado') {    				
                        if (registrosNoIns.length > 0) {
                            var source = "<%=request.getContextPath()%>/jsp/registro/listaRegistroDesdeFichero.jsp";
                            abrirXanelaAuxiliar(source,registrosNoIns,
                                    'width=480,height=340,scrollbars=no,status='+ '<%=statusBar%>',function(resp){});
                            } else jsp_alerta("A", mnsx);    	
                        }
                    }
                    
                    function onchangeCodUOR() {                   
                        //if (!contieneOperadoresConsulta(document.forms[0].codUnidadesOrganicas,operadorConsulta)) {
                        var uor = buscarUorPorCodVisibleEstado(uors, document.forms[0].cod_visible_UOR.value, 'A');                      
                        if(uor != null) {
                            document.forms[0].codUOR.value = uor.uor_cod;
                            document.forms[0].descUOR.value = uor.uor_nom;
                        }
                        else { // ha dado null para alta, buscamos de baja
                            uor = buscarUorPorCodVisibleEstado(uors, document.forms[0].cod_visible_UOR.value, 'B');
                            if(uor != null) {
                                document.forms[0].codUOR.value = uor.uor_cod;
                                document.forms[0].descUOR.value = uor.uor_nom;
                            }
                        }
                        if(uor == null) {
                            //alert('no existe');
                            document.forms[0].codUOR.value = '';
                            document.forms[0].descUOR.value = '';
                            document.forms[0].cod_visible_UOR.value = '';
                        }
                        //}
                    }
                    
                    function onClickDescUOR(){
                        divSegundoPlano=false;
                        mostrarListaUOR();
                    }
                    
                    function mostrarListaUOR(){
                        var condiciones = new Array();
                        var condicionCompleja="";
                        condiciones[0]='UOR_NO_VIS'+'§¥';
                        condiciones[1]='0';
                        condiciones[2]='UOR_OCULTA'+'§¥';
                         condiciones[3]='N';

                        var codUsu='<%=usuarioVO.getIdUsuario()%>';
                        var codOrg='<%=usuarioVO.getOrgCod()%>';
                        
                         <% if (("REGISTRO_S_SOLO_UORS_USUARIO".equals(directivaUsuario))&&("S".equals(tipoRegistro))){%>
                         condicionCompleja= 'uor_cod in (select uou_uor from '+EsquemaGenerico+'A_UOU a_uou where uou_usu='+codUsu+' and uou_org='+codOrg+')';   
                         <%}%>
                        
                        muestraListaTabla('UOR_COD_VIS','UOR_NOM','A_UOR',condiciones,'cod_visible_UOR','descUOR', 'botonUOR','100',null,null,condicionCompleja);
                    }
                    
                    function onClickHrefUOR() {
                        var argumentos = new Array();
                        argumentos[0] = document.forms[0].cod_visible_UOR.value;
                           
                         
                        var source = APP_CONTEXT_PATH + "/administracion/UsuariosGrupos.do?opcion=modalUOR" +
                        "&codOrganizacion=" + <%=orgCod%> + "&codEntidad=" + <%=entCod%>+ "&tipoESRegistro=" + '<%=tipoRegistro%>';
                        
                        abrirXanelaAuxiliar(source,argumentos,
                                'width=550,height=410',function(datos){
                                    if(datos != null) {
                                        document.forms[0].codUOR.value = datos[2];
                                        document.forms[0].descUOR.value = datos[1];
                                        document.forms[0].cod_visible_UOR.value = datos[0];
                                    }
                                    if((document.forms[0].cod_visible_UOR.value != '') && (document.forms[0].descUOR.value == '')) {
                                        document.forms[0].codUOR.value = '';
                                        document.forms[0].cod_visible_UOR.value = '';
                                    }
                                });
                    }
                    
        </script>
        
    </head>
    
    <body class="bandaBody" onload="javascript:{inicializar();}">
        
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
<form action="" method="post" name="formulario" id="formulario" enctype="multipart/form-data">
    <input type="hidden" name="tipo_select" value=""/>
    <input type="hidden" name="col_cod" value=""/>
    <input type="hidden" name="col_desc" value=""/>
    <input type="hidden" name="nom_tabla" value=""/>
    <input type="hidden" name="input_cod" value=""/>
    <input type="hidden" name="input_desc" value=""/>
    <input type="hidden" name="column_valor_where" value=""/>
    <input type="hidden" name="whereComplejo" value=""/>
    <input type="hidden" name="opcion" value="">
    <input type="hidden" name="nombreFicheroFisico" value="">
            
<%if ("E".equals(tipoRegistro)) {%>
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("etiqRegFich")%> <%=descriptor.getDescripcion(etiqTitulo)%></div>
<%} else {%>
    <div id="titulo" class="txttitblancoder"><%=descriptor.getDescripcion("etiqRegFich")%> <%=descriptor.getDescripcion(etiqTitulo)%></div>
<%}%>
<div class="contenidoPantalla">
    <table style="width:100%">
        <TR> 
            <td style="width:100%" colspan="7" class="sub3titulo"><%=descriptor.getDescripcion("rel_titulo")%> </td>
        </TR>

        <tr>
            <td style="width:13%;" class="etiqueta"><%=descriptor.getDescripcion("etiqEjercicio")%>:</td>
            <td style="width:17%;">
                <INPUT TYPE="text" class='inputTextoObligatorio' SIZE=6 maxlength=4 NAME="ejReservaD" 
                       onfocus="this.select();"> 
            </td>
            <td style="width:16%;" align="right" class="etiqueta"><%=descriptor.getDescripcion("etiqNumeros")%>:</td>
            <td style="width:7%;" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_desde")%> </td>							
            <td style="width:15%;">
                <INPUT TYPE="text" class='inputTextoObligatorio' SIZE=10 maxlength=10 NAME="numReservaD"
                       onfocus="this.select();" >					          			
            </td>
            <td style="width:7%;" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_hasta")%> </td>							
            <td style="width:27%;">
                <INPUT TYPE="text" class='inputTextoObligatorio' SIZE=10 maxlength=10 NAME="numReservaH"
                       onfocus="this.select();" >					          			
            </td>					          			
        </tr>

        <TR>
            <td style="width:13%;" class="etiqueta"><%=descriptor.getDescripcion("res_asunto")%>:</td>
            <td style="width:87%;" colspan="6">
                <INPUT TYPE="text" class='inputTextoObligatorio' SIZE=85 maxlength=120 NAME="asunto"
                       onfocus="this.select();" onkeyup="return xAMayusculas(this);" style="width:590px">
            </td>
        </tr>

        <tr>

            <td align="left" class="etiqueta">
                <%= descriptor.getDescripcion("etiqUOR")%>:
            </td>
            <td align="left" colspan="6">
                <input type="hidden" name="codUOR" id="codUOR"/>
                <input type=text class="inputTextoObligatorio" name="cod_visible_UOR" size="6"
                       onkeyup="return xAMayusculas(this);"
                       onfocus="javascript:this.select();"
                       onblur="javascript:{onchangeCodUOR();}"/>
                <input type=text class="inputTextoObligatorio" name="descUOR" size=72 readonly="true"
                       onclick="javascript:{onClickDescUOR();}" style="width:525px"/>  
                <A href="javascript:{onClickHrefUOR();}" style="text-decoration:none;" id="anclaD2" name="anchorUOR"
                   >
                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp" name="botonUOR" style="cursor:pointer;"></span>
                </A>
            </td>

        </tr>				                      
        <tr>

            <td width="13%" class="etiqueta"><%=descriptor.getDescripcion("etiqFichero")%>:</td>
            <td width="87%" colspan="6">
                <input type="file" class="inputTextoObligatorio" name="ficheroUp" size="72" style="width:590px">
            </td>
        </tr>

    </table>
    <div class="botoneraPrincipal">
        <input class="botonGeneral" name="botonAceptar" type="button" id="botonAceptar" value='<%=descriptor.getDescripcion("gbAceptar")%>' accesskey="A" onClick="pulsarAceptar();" alt="<%=descriptor.getDescripcion("toolTip_bAceptar")%>" title="<%=descriptor.getDescripcion("toolTip_bAceptar")%>">
        <input class="botonGeneral" name="botonCancelar" type="button" id="botonCancelar" value='<%=descriptor.getDescripcion("gbCancelar")%>' accesskey="C" onClick="pulsarSalir();" alt="<%=descriptor.getDescripcion("toolTip_bVolver")%>" title="<%=descriptor.getDescripcion("toolTip_bVolver")%>">
    </div>      
</div>      

</form>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaCombo.js"></script>

        <script type="text/javascript">
            function rellenarDatos(tableName,rowID){
                ocultarDiv();
            }

            document.onmouseup = checkKeys;

      function checkKeysLocal(evento,tecla) {
        var teclaAuxiliar;
        if(window.event){
            evento = window.event;
            teclaAuxiliar = evento.keyCode;
        }else
            teclaAuxiliar = evento.which;

        if (tecla == "Alt+I") {
            pulsarImprimir();
        } else if (tecla == "Alt+S") {
            pulsarSalir();
        }

        if ((layerVisible) || (divSegundoPlano)) buscar(evento);
        keyDel(evento);

        if (teclaAuxiliar == 9) {
            if (layerVisible) ocultarDiv();
            if (divSegundoPlano) divSegundoPlano = false;
           
            return false;
        }

        if (teclaAuxiliar == 40) {
            if ((layerVisible) || (divSegundoPlano)) upDown(teclaAuxiliar);
            return false;
        }

        if (teclaAuxiliar == 38) {
            if ((layerVisible) || (divSegundoPlano)) upDown(teclaAuxiliar);
            return false;
        }
        
         if (teclaAuxiliar == 1) {
            if (layerVisible) setTimeout('ocultarDiv()', 50);
            if (divSegundoPlano) divSegundoPlano = false;
            
        }
        if (evento.button == 1) {
            if (layerVisible) setTimeout('ocultarDiv()', 50);
            if (divSegundoPlano) divSegundoPlano = false;
        }
    }        

        </script>
    </body>
</html>
