<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="java.util.Vector"%>

<%@ page contentType="text/html;charset=ISO_8859-1"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title> Parámetros de Terceros </title>
        <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            ParametrosTerceroValueObject ptVO = null;
            MantenimientosTercerosForm mantForm = (MantenimientosTercerosForm) session.getAttribute("MantenimientosTercerosForm");
            int idioma = 1;
            int apl = 3;
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
                apl = usuarioVO.getAppCod();
                ptVO = mantForm.getParametrosTerceros();
            }
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        
        <!-- Ficheros JavaScript -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <link rel="stylesheet" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>" type="text/css">
        <script type="text/javascript">
            // VARIABLES GLOBALES
            var ventana = "<%=mantForm.getVentana()%>";
            //Vectores para albergar los datos de Paises (estáticos) 
            var codPaises = new Array();
            var descPaises = new Array();
            //Vectores para albergar los datos de Provincias (estáticos) 
            var codProvincias = new Array();
            var descProvincias = new Array();
            //Vectores para albergar los datos de Municipios (Dinámicos en funcion de la provincia)
            var codMunicipios = new Array();
            var descMunicipios = new Array();
            //Vectores para albergar los datos de los Tipos de Documentos (Dinámicos en funcion del municipio)
            var codTiposDocs = new Array();
            var descTiposDocs = new Array();
            //Vectores para albergar los datos de los Tipos de Ocupaciones (Dinámicos en funcion del municipio)
            var codTiposOcupaciones = new Array();
            var descTiposOcupaciones = new Array();
            //Variable para direccionamiento al frame
            var frame;
            //Vector 	que albergará los nombres de los campos del formulario y sus valores
            var vectorNombresCampos = new Array();
            var vectorValoresCampos = new Array();
            
            
            // FUNCIONES DE CARGA E INICIALIZACION DE DATOS  
            function recuperaDatosIniciales(){
    <%
            Vector listaPaises = mantForm.getListaPaises();
            Vector listaProvincias = mantForm.getListaProvincias();
            Vector listaMunicipios = mantForm.getListaMunicipios();
            Vector listaTiposDocs = mantForm.getListaTipoDocs();
            Vector listaTiposOcupaciones = mantForm.getListaUsoViviendas();
            int i = 0;
    %>
        var j=0;
        <%for (i = 0; i < listaPaises.size(); i++) {%>
        codPaises[j] = "<%=(String) ((GeneralValueObject) listaPaises.get(i)).getAtributo("codigo")%>";
        descPaises[j] = "<%=(String) ((GeneralValueObject) listaPaises.get(i)).getAtributo("descripcion")%>";
        j++;
        <%}%>
        var j=0;
        <%for (i = 0; i < listaProvincias.size(); i++) {%>
        codProvincias[j] = "<%=(String) ((GeneralValueObject) listaProvincias.get(i)).getAtributo("codigo")%>";
        descProvincias[j] = "<%=(String) ((GeneralValueObject) listaProvincias.get(i)).getAtributo("descripcion")%>";
        j++;
        <%}%>
        j=0;
        <%for (i = 0; i < listaMunicipios.size(); i++) {%>
        codMunicipios[j] = "<%=(String) ((GeneralValueObject) listaMunicipios.get(i)).getAtributo("codMunicipio")%>";
        descMunicipios[j] = "<%=(String) ((GeneralValueObject) listaMunicipios.get(i)).getAtributo("nombreOficial")%>";
        j++;
        <%}%>
        j=0;
        <%for (i = 0; i < listaTiposDocs.size(); i++) {%>
        codTiposDocs[j] = "<%=(String) ((GeneralValueObject) listaTiposDocs.get(i)).getAtributo("codTipoDoc")%>";
        descTiposDocs[j] = "<%=(String) ((GeneralValueObject) listaTiposDocs.get(i)).getAtributo("descTipoDoc")%>";
        j++;
        <%}%>
        j=0;
        <%for (i = 0; i < listaTiposOcupaciones.size(); i++) {%>
        codTiposOcupaciones[j] = 
        "<%=(String) ((GeneralValueObject) listaTiposOcupaciones.get(i)).getAtributo("codUsoVivienda")%>";
        descTiposOcupaciones[j] = 
        "<%=(String) ((GeneralValueObject) listaTiposOcupaciones.get(i)).getAtributo("descUsoVivienda")%>";
        j++;
        <%}%>
    }//de la funcion
    
    
    function redireccionaFrame(){
        //var ventana = "<%=mantForm.getVentana()%>";
        frame=(ventana=="true")?top1.mainFrame1:top.mainFrame;	
    }//de la funcion
    
    
    function inicializar(){
        recuperaDatosIniciales();
        redireccionaFrame();
        vectorNombresCampos = ["codPais","descPais","codProvincia","descProvincia","codMunicipio","descMunicipio",
            "codTipoDoc","descTipoDoc","codTipoOcupacion","descTipoOcupacion","codTipoOcupacionPr",
            "descTipoOcupacionPr","tratamientoLugar"];
        cargarDatosFormulario();
        if(ventana=="false"){
            pleaseWait1("off",frame);
            
                
            
        }else{
        pleaseWait1("off",frame);
        var parametros = self.parent.opener.xanelaAuxiliarArgs;
        rellenarCamposBusqueda(parametros);
        pulsarBuscar(); 
    } 
}//de la funcion


function cargarDatosFormulario(){
    vectorValoresCampos = ["<%=ptVO.getPais()%>","<%=ptVO.getNomPais()%>","<%=ptVO.getProvincia()%>",
        "<%=ptVO.getNomProvincia()%>","<%=ptVO.getMunicipio()%>","<%=ptVO.getNomMunicipio()%>",
        "<%=ptVO.getTipoDocumento()%>","<%=ptVO.getNomTipoDocumento()%>",
        "<%=ptVO.getTipoOcupacion()%>","<%=ptVO.getNomTipoOcupacion()%>",
        "<%=ptVO.getTipoOcupacionPrincipal()%>","<%=ptVO.getNomTipoOcupacionPrincipal()%>",
        "<%=ptVO.getLugar()%>"]; 
        rellenar(vectorValoresCampos,vectorNombresCampos);
        var identificadorMultiple="<%=ptVO.getIdentificadorMultiple()%>";
        document.forms[0].identificMultiples.checked=(identificadorMultiple=="0")?false:true;						
        comboPais.addItems(codPaises,descPaises);
        comboProvincia.addItems(codProvincias,descProvincias);
        comboMunicipio.addItems(codMunicipios,descMunicipios);
        comboTipoDoc.addItems(codTiposDocs,descTiposDocs);
        comboTipoOcupacion.addItems(codTiposOcupaciones,descTiposOcupaciones);
        comboTipoOcupacionPr.addItems(codTiposOcupaciones,descTiposOcupaciones);
    }//de la funcion
    
    // FUNCIONES DE VALIDACION DE CAMPOS
    function validarCamposFormulario(){
        var codTipoOcupacionPr = document.forms[0].codTipoOcupacionPr.value;
        if((codTipoOcupacionPr!=""))
            return true;
        return false;
    }//de la funcion
    
    
    // FUNCIONES DE CARGA DE DATOS DINAMICA
    function cargarListaProvincias(){	
        document.forms[0].opcion.value="cargarProvincias";
        document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
        document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Parametros.do";
        document.forms[0].submit();
    }//de la funcion
    
    function cargarListaMunicipios(){	
        document.forms[0].opcion.value="cargarMunicipios";
        document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
        document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Parametros.do";
        document.forms[0].submit();
    }//de la funcion
    
    
    // FUNCIONES DE PULSACION DE BOTONES		
    function pulsarGrabar(){
        if(validarCamposFormulario()){ //Si los campos del formulario son correctos (los obligatorios)
            document.forms[0].opcion.value="modificar";
            document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
            document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Parametros.do";
            document.forms[0].submit();
        }else
        jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
        }//de la funcion
        
        function pulsarSalir(){
            if(ventana=="false"){
                document.forms[0].opcion.value="inicializarTerc";
                document.forms[0].target="mainFrame";
                document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                document.forms[0].submit();
            }else{
            var datosRetorno;
            self.parent.opener.retornoXanelaAuxiliar(datosRetorno);
        }
    }//de la funcion
    
        </script>
    </head>
    
    <body class="bandaBody" onLoad="inicializar();">
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
<form action="" method="get" name="formulario" target="_self">
    <input type="hidden" name="opcion">
    <input type="hidden" name="ventana" value="<%=mantForm.getVentana()%>">

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_MantParam")%></div>
<div class="contenidoPantalla">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td>
                <table border="0" width="100%" cellspacing="0px" cellpadding="0px">
                    <tr>
                        <td>
                            <table border="0" width="100%" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td style="width: 35%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Pais")%></td>
                                    <td style="width: 38%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Provincia")%></td>
                                    <td class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Municipio")%></td>	
                                </tr>
                            </table>
                        </td>											  
                    </tr>
                    <tr>
                        <td>
                            <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
                                <tr>
                                    <td style="width: 35%" align="left" class="etiqueta">
                                        <input class="inputTexto" type="text" id="codPais" name="codPais" 
                                               size="3">
                                        <input class="inputTexto" type="text" id="descPais" 
                                               name="descPais" style="width:150" readonly>
                                        <a id="anchorPais" name="anchorPais" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonPais"
                                            name="botonPais" style="cursor:hand;"></span></a>
                                    </td>
                                    <td style="width: 38%" class="etiqueta">
                                        <input class="inputTexto" type="text" id="codProvincia" name="codProvincia" 
                                               size="3">
                                        <input class="inputTexto" type="text" id="descProvincia" 
                                               name="descProvincia" style="width:150" readonly>
                                        <a id="anchorProvincia" name="anchorProvincia" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonProvincia"
                                            name="botonProvincia" style="cursor:hand;"></span></a>
                                    </td>
                                    <td class="etiqueta">
                                        <input class="inputTexto" type="text" id="codMunicipio" name="codMunicipio" 
                                               size="3">
                                        <input id="descMunicipio" name="descMunicipio" type="text"   
                                               class="inputTexto" style="width:150" readonly>
                                        <a id="anchorMunicipio" name="anchorMunicipio" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonMunicipio" name="botonMunicipio"
                                            style="cursor:hand;"></span></a>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td align="center">
                            <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center">
                                <tr>
                                    <td style="width: 35%" class="etiqueta">
                                        <%=descriptor.getDescripcion("gbEtiq_TipIdentific")%>
                                    </td>
                                    <td style="width: 38%" class="etiqueta"><%=descriptor.getDescripcion("gbEtiq_Ocup")%></td>
                                    <td style="width: 27%" class="etiqueta"><%=descriptor.getDescripcion("gbEtiq_OcupDomPri")%></td>	
                                </tr>
                            </table>
                        </td>											  
                    </tr>
                    <tr>
                        <td>
                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td style="width: 35%" align="left" class="etiqueta">
                                        <input id="codTipoDoc" name="codTipoDoc" type="text" class="inputTexto" size="3">
                                        <input id="descTipoDoc" name="descTipoDoc" type="text" class="inputTexto" 
                                               style="width:150" readonly>
                                        <a id="anchorTipoDoc" name="anchorTipoDoc" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoDoc" name="botonTipoDoc" style="cursor:hand;" alt=""></span></a> 
                                    </td>
                                    <td style="width: 38%" class="etiqueta">
                                        <input id="codTipoOcupacion" name="codTipoOcupacion" type="text" class="inputTexto" 
                                               size="3">
                                        <input id="descTipoOcupacion" name="descTipoOcupacion" type="text" class="inputTexto" 
                                               style="width:150" readonly>
                                        <a id="anchorTipoOcupacion" name="anchorTipoOcupacion" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoOcupacion"
                                                name="botonTipoOcupacion" 
                                            style="cursor:hand;" alt=""></span></a>
                                    </td>
                                    <td class="etiqueta">
                                        <input id="codTipoOcupacionPr" name="codTipoOcupacionPr" type="text" class="inputTexto" 
                                               size="3">
                                        <input id="descTipoOcupacionPr" name="descTipoOcupacionPr" type="text" 
                                               class="inputTexto" style="width:150" readonly>
                                        <a id="anchorTipoOcupacionPr" name="anchorTipoOcupacionPr" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoOcupacionPr"
                                                name="botonTipoOcupacionPr" 
                                            style="cursor:hand;" alt=""></span></a>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td> 
                <table width="100%" cellpadding="0" cellspacing="0">
                    <tr>
                        <td style="width: 4%"> 
                            <input name="identificMultiples" type="checkbox" value="1">	
                        </td>
                        <td class="etiqueta"><%=descriptor.getDescripcion("gEtiq_IdentMultiples")%></td>
                    </tr>
                </table>   
            </td>
        </tr>
        <tr>
            <td> 
                <table width="100%" cellpadding="0" cellspacing="0">
                    <tr>
                        <td style="width: 4%"> 
                            <input name="tratamientoLugar" type="text" class="inputTexto" size=2 
                                   onkeyup = "return xAMayusculas(this);">
                        </td>
                        <td class="etiqueta"><%=descriptor.getDescripcion("gEtiq_TratLugar")%></td>
                    </tr>
                </table>   
            </td>
        </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type= "button"  class="botonGeneral"  name="botonGrabar" onClick="pulsarGrabar();" accesskey="G" value="<%=descriptor.getDescripcion("gbGrabar")%>">
    </div>
</div>
</form>
<script type="text/javascript">
            
            // FUNCION DE CONTROL DE TECLAS

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

            function checkKeysLocal(evento,tecla) {
                var teclaAuxiliar = "";
                if(window.event){
                    evento = window.event;
                    teclaAuxiliar = evento.keyCode;
                }else{
                    teclaAuxiliar = evento.which;
                }

                if (teclaAuxiliar == 1){
                    if (comboPais.base.style.visibility == "visible" && isClickOutCombo(comboPais,coordx,coordy)) setTimeout('comboPais.ocultar()',20);
                    if (comboProvincia.base.style.visibility == "visible" && isClickOutCombo(comboProvincia,coordx,coordy)) setTimeout('comboProvincia.ocultar()',20);
                    if (comboMunicipio.base.style.visibility == "visible" && isClickOutCombo(comboMunicipio,coordx,coordy)) setTimeout('comboMunicipio.ocultar()',20);
                    if (comboTipoDoc.base.style.visibility == "visible" && isClickOutCombo(comboTipoDoc,coordx,coordy)) setTimeout('comboTipoDoc.ocultar()',20);
                    if (comboTipoOcupacion.base.style.visibility == "visible" && isClickOutCombo(comboTipoOcupacion,coordx,coordy)) setTimeout('comboTipoOcupacion.ocultar()',20);
                    if (comboTipoOcupacionPr.base.style.visibility == "visible" && isClickOutCombo(comboTipoOcupacionPr,coordx,coordy)) setTimeout('comboTipoOcupacionPr.ocultar()',20);
                }
                if (teclaAuxiliar == 9){
                    comboPais.ocultar();
                    comboProvincia.ocultar();
                    comboMunicipio.ocultar();
                    comboTipoDoc.ocultar();
                    comboTipoOcupacion.ocultar();
                    comboTipoOcupacionPr.ocultar();
                }
                keyDel(evento);
            }
            
            // COMBOS
            var comboPais = new Combo("Pais");
            var comboProvincia = new Combo("Provincia");
            var comboMunicipio = new Combo("Municipio");
            var comboTipoDoc = new Combo("TipoDoc");
            var comboTipoOcupacion = new Combo("TipoOcupacion");
            var comboTipoOcupacionPr = new Combo("TipoOcupacionPr");            
            var auxCombo = "comboProvincia";
            
            comboPais.change = 
            function() { 
                auxCombo="comboProvincia"; 
                limpiar(["codProvincia","descProvincia","codMunicipio","descMunicipio"]);
                if(comboPais.des.value.length!=0){
                    cargarListaProvincias();
                }else{
                comboProvincia.addItems([],[]);
                comboMunicipio.addItems([],[]);
            }		
        } 
        
        comboProvincia.change = 
        function() { 
            auxCombo="comboMunicipio"; 
            limpiar(["codMunicipio","descMunicipio"]);
            if(comboProvincia.des.value.length!=0){
                cargarListaMunicipios();
            }else{
            comboMunicipio.addItems([],[]);
        }		
    } 
    
    function cargarComboBox(cod, des){
        eval(auxCombo+".addItems(cod,des)");
    }
    
        </script>
    </body>
</html>
