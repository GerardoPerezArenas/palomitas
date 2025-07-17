<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="java.util.Vector"%>

<%@ page contentType="text/html;charset=ISO_8859-1"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title> Mantenimiento de Municipios </title>
        <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
  
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            ParametrosTerceroValueObject ptVO = null;
            int idioma = 1;
            int apl = 3;
            String css="";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
                apl = usuarioVO.getAppCod();
                css=usuarioVO.getCss();
                ptVO = (ParametrosTerceroValueObject) session.getAttribute("parametrosTercero");
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
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
              <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript">
            // VARIABLES GLOBALES
                 <%
            MantenimientosTercerosForm mantForm = (MantenimientosTercerosForm) session.getAttribute("MantenimientosTercerosForm");
    %>  
        var ventana = "<%=mantForm.getVentana()%>";
        var codProvincias = new Array();
        var descProvincias = new Array();
        var listaMunicipiosOriginal = new Array();
        var listaMunicipios = new Array();
        var frame;
        
        // FUNCIONES DE CARGA E INICIALIZACION DE DATOS  
        function recuperaDatosIniciales()
        {
    <%
            Vector listaProvincias = mantForm.getListaProvincias();
            int lengthProvs = listaProvincias.size();
            int i = 0;
            String codProvincias = "";
            String descProvincias = "";
            for (i = 0; i < lengthProvs - 1; i++) {
                GeneralValueObject provincias = (GeneralValueObject) listaProvincias.get(i);
                codProvincias += "\"" + (String) provincias.getAtributo("codigo") + "\",";
                descProvincias += "\"" + (String) provincias.getAtributo("descripcion") + "\",";
            }
            GeneralValueObject provincias = (GeneralValueObject) listaProvincias.get(i);
            codProvincias += "\"" + (String) provincias.getAtributo("codigo") + "\"";
            descProvincias += "\"" + (String) provincias.getAtributo("descripcion") + "\"";
     %>
         codProvincias = [<%=codProvincias%>];
             descProvincias = [<%=descProvincias%>];
             }//de la funcion
             
             
             function redireccionaFrame()
             {
                 frame=(ventana=="true")?top1.mainFrame1:top.mainFrame;	
             }//de la funcion
             
             
             function inicializar()
             {
                 recuperaDatosIniciales();
                 redireccionaFrame();
                 valoresPorDefecto();
                 vectorBotones = [document.forms[0].botonAlta,document.forms[0].botonModificar,
                     document.forms[0].botonBorrar,document.forms[0].botonLimpiar];
                 vectorCamposBusqueda1 = [document.forms[0].codProvincia,document.forms[0].descProvincia];
                 vectorCamposRejilla1 = [document.forms[0].codMunicipio,document.forms[0].nombreOficial,
                     document.forms[0].nombreLargo,document.forms[0].partidoJudicial,document.forms[0].comarca,
                     document.forms[0].digitoControl,document.forms[0].superficie,document.forms[0].altitud,
                     document.forms[0].kmtsACapital,document.forms[0].latitudNorte,document.forms[0].latitudSur,
                     document.forms[0].longitudEste,document.forms[0].longitudOeste,document.forms[0].situacion];
                 vectorBotonesBusqueda = [document.getElementsByName('botonProvincia')[0]];
                 pulsarCancelarBuscar();
                 if(ventana=="false")
                     {
                         pleaseWait1("off",frame);
                         
                             
                         
                     }
                     else
                         {
                             pleaseWait1("off",frame);
                             var parametros = self.parent.opener.xanelaAuxiliarArgs;
                             rellenarCamposBusqueda(parametros);
                             pulsarBuscar();
                         }
                     }//de la funcion
                     
                     // FUNCIONES DE LIMPIEZA, HABILITACION Y DESHABILITACION DE CAMPOS
                     var vectorCamposBusqueda = ["codProvincia","descProvincia"];
                     var vectorCamposRejilla = ["codMunicipio","nombreOficial","nombreLargo","partidoJudicial","comarca","digitoControl",
                         "superficie","altitud","kmtsACapital","latitudNorte","latitudSur","longitudEste","longitudOeste",
                         "situacion"];
                     var vectorBotones = new Array();
                     var vectorCamposBusqueda1 = new Array();
                     var vectorCamposRejilla1 = new Array();
                     var vectorBotonesBusqueda = new Array();
                     
                     function limpiarFormulario()
                     {
                         limpiarCamposBusqueda();
                         limpiarCamposRejilla();
                         tablaMunicipios.lineas = new Array();
                         refresca(tablaMunicipios);
                     }
                     
                     function limpiarCamposBusqueda()
                     {
                         limpiar(vectorCamposBusqueda);
                     }
                     
                     function limpiarCamposRejilla()
                     {
                         limpiar(vectorCamposRejilla);
                         var vector = [document.forms[0].codMunicipio];
                         habilitarGeneral(vector);
                     }
                     
                     
                     function cargarListaMunicipios(lista)
                     {
                         listaMunicipios = lista;
                         tablaMunicipios.lineas = lista;
                         refresca(tablaMunicipios);
                         //Oculto la imagen de carga de datos de la pantalla
                         pleaseWait1("off",frame);
                     }//de la funcion
                     
                     function valoresPorDefecto()
                     {
                         document.forms[0].codPais.value ="<%=ptVO.getPais()%>";
                         document.forms[0].codProvincia.value = "<%=ptVO.getProvincia()%>";
                         document.forms[0].descProvincia.value = "<%=ptVO.getNomProvincia()%>";
                     }//de la funcion
                     
                     function rellenarCamposBusqueda(datos)
                     {
                         rellenar(datos,vectorCamposBusqueda);
                     }//de la funcion
                     
                     
                     // FUNCIONES DE VALIDACION DE CAMPOS
                     function validarCamposBusqueda()
                     {
                         var pais = document.forms[0].codPais.value;
                         var provincia = document.forms[0].codProvincia.value;
                         if((pais!="")&&(provincia!=""))
                             return true;
                         return false;
                     }//de la funcion
                     
                     function validarCamposRejilla()
                     {
                         var codMunicipio = document.forms[0].codMunicipio.value;
                         if(codMunicipio!="")
                             return true;
                         return false;
                     }//de la funcion
                     
                     function noEsta(indice)
                     {
                         var cod = document.forms[0].codMunicipio.value;
                         for(i=0;(i<listaMunicipios.length);i++){
                             if(i!=indice){
                                 if((listaMunicipios[i][0]) == cod)
                                     return false;
                             }
                         }
                         return true;
                     }//de la funcion
                     
                     function filaSeleccionada(tabla)
                     {
                         var i = tabla.selectedIndex;
                         if((i>=0)&&(!tabla.ultimoTable))
                             return true;
                         return false;
                     }//de la funcion
                     
                     
                     // FUNCIONES DE PULSACION DE BOTONES
                     function pulsarBuscar()
                     {
                         //Muestro la imagen de carga de datos de la pantalla
                         pleaseWait1("on",frame);
                         //var botonBuscar = [document.forms[0].botonBuscar];
                         document.forms[0].botonBuscar.disabled = true;
                         if(validarCamposBusqueda())
                             {
                                 document.forms[0].opcion.value="cargarMunicipios";
                                 document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                                 document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Municipios.do";
                                 document.forms[0].submit();
                                 //deshabilitarGeneral(botonBuscar);
                                 deshabilitarGeneral(vectorCamposBusqueda1);
                                 document.forms[0].botonAlta.disabled = false;
                                 document.forms[0].botonModificar.disabled = false;
                                 document.forms[0].botonBorrar.disabled = false;
                                 document.forms[0].botonLimpiar.disabled = false;
                                 habilitarGeneral(vectorBotones);
                                 habilitarGeneral(vectorCamposRejilla1);
                                 deshabilitarImagenBotonGeneral(vectorBotonesBusqueda,true);
                             }
                             else
                                 jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                                 }//de la funcion
                                 
                                 function pulsarAlta()
                                 {
                                     if(validarCamposRejilla())
                                         {
                                             if(noEsta())
                                                 {
                                                     document.forms[0].situacion.value="";
                                                     habilitarGeneral(vectorCamposBusqueda1);
                                                     document.forms[0].opcion.value="alta";
                                                     document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                                                     document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Municipios.do";
                                                     document.forms[0].submit();
                                                     deshabilitarGeneral(vectorCamposBusqueda1);
                                                     limpiarCamposRejilla();
                                                 }
                                                 else
                                                     jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>"); 
                                                     }
                                                     else
                                                         jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                                                         }//de la funcion
                                                         
    function pulsarModificar()
    {
        if(filaSeleccionada(tablaMunicipios)) //Si  ha seleccionado alguna fila
            {
                if(validarCamposRejilla()) //Si los campos de la rejilla son correctos (los obligatorios)
                    {
                        if(noEsta(tablaMunicipios.selectedIndex)) //Si el código está en la lista
                            {
                                var vector = [document.forms[0].codMunicipio];
                                habilitarGeneral(vector);
                                document.forms[0].situacion.value="";
                                habilitarGeneral(vectorCamposBusqueda1);
                                document.forms[0].opcion.value="modificar";
                                document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                                document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Municipios.do";
                                document.forms[0].submit();
                                deshabilitarGeneral(vectorCamposBusqueda1);
                                habilitarGeneral(vector);
                                limpiarCamposRejilla();
                            }
                            else
                                jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>"); 
                                }
                                else
                                    jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                                    }
                                    else
                                        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");					
    }//de la funcion
                                                                                             
                                                                                             
    function pulsarBorrar()
    {
        if(filaSeleccionada(tablaMunicipios)) //Si  ha seleccionado alguna fila
            {
                if(validarCamposBusqueda()) //Si los campos de la rejilla son correctos (los obligatorios)
                    {
                        if(noEsta(tablaMunicipios.selectedIndex)) //Si el código está en la lista
                            {
                                if(jsp_alerta("C",'<%=descriptor.getDescripcion("desElimMun")%>') ==1) {
                                    var vector = [document.forms[0].codMunicipio];
                                    habilitarGeneral(vector);
                                    habilitarGeneral(vectorCamposBusqueda1);
                                    document.forms[0].opcion.value="eliminar";
                                    document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                                    document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Municipios.do";
                                    document.forms[0].submit();
                                    deshabilitarGeneral(vectorCamposBusqueda1);
                                    deshabilitarGeneral(vector);
                                    limpiarCamposRejilla();
                                    if(tablaMunicipios.selectedIndex != -1 ) {
                                        tablaMunicipios.selectLinea(tablaMunicipios.selectedIndex);
                                        tablaMunicipios.selectedIndex = -1;
                                    }
                                }
                            }
                            else
                                jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>"); 
                                }
                                else
                                    jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                                    }
                                    else
                                        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");					
        }//de la funcion
                                                                                                                                 
        function noEliminarMunic() {
            jsp_alerta("A","<%=descriptor.getDescripcion("msjNoElimMunic")%>");
            }
                                                                                                                                     
        function pulsarCancelarBuscar()
        {
            limpiarFormulario();
            //var botonBuscar = [document.forms[0].botonBuscar];
            //habilitarGeneral(botonBuscar);
            document.forms[0].botonBuscar.disabled = false;
            habilitarGeneral(vectorCamposBusqueda1);
            deshabilitarImagenBotonGeneral(vectorBotonesBusqueda,false);
            deshabilitarGeneral(vectorCamposRejilla1);

            document.forms[0].botonAlta.disabled = true;
            document.forms[0].botonModificar.disabled = true;
            document.forms[0].botonBorrar.disabled = true;
            document.forms[0].botonLimpiar.disabled = true;

            deshabilitarGeneral(vectorBotones);
            valoresPorDefecto();
        }//de la funcion

        function pulsarLimpiar()
        {
            limpiarCamposRejilla();
            if(tablaMunicipios.selectedIndex != -1 ) {
                tablaMunicipios.selectLinea(tablaMunicipios.selectedIndex);
                tablaMunicipios.selectedIndex = -1;
            }
        }//de la funcion
                                                                                                                                     
        function pulsarSalir()
        {
            if(ventana=="false")
                {
                    document.forms[0].target = "mainFrame";
                    document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
                    document.forms[0].submit();
                }
                else
                    {
                        var datosRetorno;
                        if(indice>-1)
                            datosRetorno = listaMunicipiosOriginal[indice];
                        self.parent.opener.retornoXanelaAuxiliar(datosRetorno);
                    }
                }//de la funcion
                                                                                                                                             
                                                                                                                                             
        </script>
    </head>
    
    <body class="bandaBody"  onLoad="inicializar();">
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
<form action="" method="get" name="formulario" target="_self">
    <input type="hidden" name="opcion">
    <input type="hidden" name="tipo_select"   value="">
    <input type="hidden" name="col_cod"   value="">
    <input type="hidden" name="col_desc"   value="">
    <input type="hidden" name="nom_tabla"   value="">
    <input type="hidden" name="input_cod"   value="">
    <input type="hidden" name="input_desc"   value="">
    <input type="hidden" name="target1"  value="">
    <input type="hidden" name="column_valor_where" value="">
    <input type="hidden" name="codPais" size="3" value="<%=ptVO.getPais()%>">
    <input type="hidden" name="codMunicipioAntiguo" value="">
    <input type="hidden" name="ventana" value="<%=mantForm.getVentana()%>">
    <input type="hidden" name="situacion" value="">
    <input type="hidden" name="partidoJudicial" value="">

    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_MantMunicipios")%></div>
    <div class="contenidoPantalla">
        <table>
            <tr>
                <td class="etiqueta" style="width: 40%">
                    <%=descriptor.getDescripcion("gEtiq_Provincia")%>
                    <input class="inputTextoObligatorio" type="text" name="codProvincia" style="width:8%"
                           onfocus="this.select();" 
                           onchange = "javascript:{actualizarValDiv('codProvincia','descProvincia');divSegundoPlano=true;inicializarValores('codProvincia','descProvincia',codProvincias,descProvincias);}">
                    <input class="inputTextoObligatorio" type="text" name="descProvincia"  
                           style="width:50%" readonly
                           onfocus = "javascript:{divSegundoPlano=true;inicializarValores('codProvincia','descProvincia',codProvincias,descProvincias);}"
                           onclick = "javascript:{divSegundoPlano=false;inicializarValores('codProvincia','descProvincia',codProvincias,descProvincias);}">
                    <a name="anchorProvincia"     
                       href = "javascript:{divSegundoPlano=false;inicializarValores('codProvincia','descProvincia',codProvincias,descProvincias);}"
                       style="text-decoration:none;" onfocus="javascript:this.focus();">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" name="botonProvincia"
                             style="cursor:hand;"></span>
                    </a>
                </td>
                <td style="text-align:right;width: 60%">
                    <input name="botonBuscar" type="button"  class="botonGeneral" id="botonBuscar" 
                           value="<%=descriptor.getDescripcion("gbBuscar")%>"
                           onClick="pulsarBuscar();" accesskey="B">
                    <input name="botonCancelar" type="button" class="botonGeneral" id="botonCancelar"
                           value="<%=descriptor.getDescripcion("gbCancelar")%>"
                           onClick="pulsarCancelarBuscar();" accesskey="C">
                </td>
            </tr>
            <tr> 
                <td colspan="2" id="tablaMunicipios"> <!-- colspan="14"> -->
                </td>
            </tr>
            <tr> 
                <td colspan="2"> 
                    <input name="codMunicipio" type="text" class="inputTextoObligatorio" id="numero" 
                           maxlength=4  style="width:7%" onkeyup="return SoloDigitosNumericos(this);">
                    <input name="nombreOficial" type="text" class="inputTexto" size=15   
                           maxlength=30   style="width:12.5%"
                           onblur="return xAMayusculas(this);">
                    <input name="nombreLargo" type="text" class="inputTexto" maxlength=50
                           style="width:13%" onblur="return xAMayusculas(this);">
                    <input name="comarca" type="text" class="inputTexto" maxlength=5
                            style="width:10%" onkeyup="return SoloDigitosNumericos(this);">
                    <input name="digitoControl" type="text" class="inputTexto" maxlength=1
                            style="width:4%" onkeyup="return xAMayusculas(this);">
                    <input name="superficie" type="text" class="inputTexto" maxlength="10"
                           style="width:10%" onblur = "javascript:if (validarNumeroReal(this)) validarPartesEnteraDecimal(this,7,2);" style="width:75;">
                    <input name="altitud" type="text" class="inputTexto" maxlength=5
                           style="width:6.5%" onkeyup="return SoloDigitosNumericos(this);">
                    <input name="kmtsACapital" type="text" class="inputTexto" maxlength=4
                           style="width:6%" onkeyup="return SoloDigitosNumericos(this);">
                    <input name="latitudNorte" type="text" class="inputTexto" maxlength=6
                           style="width:6%" onblur =  "javascript:if (validarNumeroReal(this)) validarPartesEnteraDecimal(this,3,2);">
                    <input name="latitudSur" type="text" class="inputTexto" maxlength=6
                           style="width:6%" onblur = "javascript:if (validarNumeroReal(this)) validarPartesEnteraDecimal(this,3,2);">
                    <input name="longitudEste" type="text" class="inputTexto" maxlength=6
                           style="width:6%" onblur = "javascript:if (validarNumeroReal(this)) validarPartesEnteraDecimal(this,3,2);" style="width:55;">
                    <input name="longitudOeste" type="text" class="inputTexto" maxlength=6
                           style="width:6%" onblur = "javascript:if (validarNumeroReal(this)) validarPartesEnteraDecimal(this,3,2);">
                </td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" name="botonAlta"	onClick="pulsarAlta();" 
               accesskey="A" value="<%=descriptor.getDescripcion("gbAlta")%>"> 
            <input type= "button" class="botonGeneral" name="botonModificar"	onClick="pulsarModificar();" 
               accesskey="M" value="<%=descriptor.getDescripcion("gbModificar")%>">
            <input type= "button" class="botonGeneral" name="botonBorrar"	onClick="pulsarBorrar();" 
               accesskey="B" value="<%=descriptor.getDescripcion("gbEliminar")%>"> 
            <input type= "button" class="botonGeneral" name="botonLimpiar"	onClick="pulsarLimpiar();" 
               accesskey="L" value="<%=descriptor.getDescripcion("gbLimpiar")%>"> 
            <input type= "button" class="botonGeneral" name="botonSalir" onClick="pulsarSalir();" 
               accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>">
        </div>
    </div>
 </form>
 
 <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaCombo.js"></script>
 <script type="text/javascript">
            var indice;
            var tablaMunicipios = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaMunicipios"));
            
            tablaMunicipios.addColumna("60",'center','<%=descriptor.getDescripcion("gEtiq_Codigo")%>');
            tablaMunicipios.addColumna("120",'center','<%=descriptor.getDescripcion("gEtiq_NomOficial")%>');
            tablaMunicipios.addColumna("120",'center','<%=descriptor.getDescripcion("gEtiq_NomLargo")%>');
            tablaMunicipios.addColumna("90",'center','<%=descriptor.getDescripcion("gEtiq_Comarca")%>');
            tablaMunicipios.addColumna("40",'center','<%=descriptor.getDescripcion("gEtiq_DigitControl")%>');
            tablaMunicipios.addColumna("90",'center','<%=descriptor.getDescripcion("gEtiq_Superficie")%>');
            tablaMunicipios.addColumna("60",'center','<%=descriptor.getDescripcion("gEtiq_Altitud")%>');
            tablaMunicipios.addColumna("60",'center','<%=descriptor.getDescripcion("gEtiq_KmtsCapital")%>');
            tablaMunicipios.addColumna("60",'center','<%=descriptor.getDescripcion("gEtiq_LatitNorte")%>');
            tablaMunicipios.addColumna("60",'center','<%=descriptor.getDescripcion("gEtiq_LatitSur")%>');
            tablaMunicipios.addColumna("60",'center','<%=descriptor.getDescripcion("gEtiq_LongEste")%>');
            tablaMunicipios.addColumna("60",'center','<%=descriptor.getDescripcion("gEtiq_LongOeste")%>');

            tablaMunicipios.displayCabecera=true;
            
function refresca(tabla){
    tabla.displayTabla();
}

    function rellenarDatos(tableName,rowID){
        if(tablaMunicipios==tableName){
            if(filaSeleccionada(tablaMunicipios)) {//Si  ha seleccionado alguna fila
                    var i=rowID;
                    indice = rowID;
                    limpiarCamposRejilla();
                    if(i>=0){
                        var vector = [document.forms[0].codMunicipio];
                        deshabilitarGeneral(vector);
                        var vectorDatosRejilla = [listaMunicipiosOriginal[i][2],listaMunicipiosOriginal[i][5],
                            listaMunicipiosOriginal[i][6],listaMunicipiosOriginal[i][3],listaMunicipiosOriginal[i][4],
                            listaMunicipiosOriginal[i][7],listaMunicipiosOriginal[i][8],listaMunicipiosOriginal[i][9],
                            listaMunicipiosOriginal[i][10],listaMunicipiosOriginal[i][11],listaMunicipiosOriginal[i][12],
                            listaMunicipiosOriginal[i][13],listaMunicipiosOriginal[i][14],listaMunicipiosOriginal[i][15]];
                        rellenar(vectorDatosRejilla,vectorCamposRejilla);
                        document.forms[0].codMunicipioAntiguo.value = listaMunicipiosOriginal[i][2];
                    } 
                }
            }
        } //de la funcion

            // FUNCION DE CONTROL DE TECLAS
            document.onmouseup = checkKeys; 

            function checkKeysLocal(evento, tecla){
                //** Esta funcion se debe implementar en cada JSP para particularizar  **//
                //** las acciones a realizar de las distintas combinaciones de teclas  **//

                var teclaAuxiliar = "";
                if(window.event) {
                    evento = window.event;
                    teclaAuxiliar = evento.keyCode;
                }else
                    teclaAuxiliar = evento.which;

                if((layerVisible)||(divSegundoPlano)) buscar(evento);
                 var aux=null;

                keyDel(evento);
                if(teclaAuxiliar == 9){
                    if(layerVisible) ocultarDiv();
                    if(divSegundoPlano) divSegundoPlano = false;
                }
                if(teclaAuxiliar == 1){
                    if(layerVisible) setTimeout("ocultarDiv()",50);
                    if(capaVisible) ocultarLista();
                    if(divSegundoPlano) divSegundoPlano = false;
                }
                if(teclaAuxiliar == 40){
                    if((layerVisible)||(divSegundoPlano)) upDown(teclaAuxiliar);
                    upDownTable(tablaMunicipios,listaMunicipios,teclaAuxiliar);
                }
                if(teclaAuxiliar == 38){
                    if((layerVisible)||(divSegundoPlano)) upDown(teclaAuxiliar);
                    upDownTable(tablaMunicipios,listaMunicipios,teclaAuxiliar);
                }  
                if (evento.button == 1){
                    if(layerVisible) setTimeout("ocultarDiv()",50);
                    if(capaVisible) ocultarLista();
                    if(divSegundoPlano) divSegundoPlano = false;      
                }	
            }
                                                                        
                                                                        
        </script>
    </body>
</html>
