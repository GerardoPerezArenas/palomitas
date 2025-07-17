<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@ page import="es.altia.agora.interfaces.user.web.sge.DefinicionProcedimientosForm"%>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title> Oculto Definicion Procedimientos </title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <script>
    var lista = new Array();
    var listaOriginal = new Array();
    var listaDoc = new Array();
    var listaDocOriginal = new Array();
    var listaUnidadesInicio = new Array();
    var listaUnidadesInicioOriginal = new Array();
    var listaCampos = new Array();
    var listaCamposOriginal = new Array();
    var listaAgrupacionesCampos = new Array();
    var listaAgrupacionesCamposOriginal = new Array();
    var listaEnlaces = new Array();
    var listaEnlacesOriginal = new Array();
    var listaRoles = new Array();
    var listaRolesOriginal = new Array();
    var radioButon = "";
    var cont = 0;
    var cont1 = 0;
    var cont2 = 0;
    var cont3 = 0;
    var cont4 = 0;
    var cont5 = 0;
    var cont6 = 0;
    var cont7 = 0;
    var cont8 = 0;
    
      function redirecciona(){ 
                    
        var datos  = new Array();
        datos[1] = '<bean:write name="DefinicionProcedimientosForm" property="codMunicipio"/>';
        datos[2] = '<bean:write name="DefinicionProcedimientosForm" property="txtCodigo"/>';
        datos[3] = '<bean:write name="DefinicionProcedimientosForm" property="txtDescripcion"/>';
        datos[4] = '<bean:write name="DefinicionProcedimientosForm" property="fechaLimiteDesde"/>';
        datos[5] = '<bean:write name="DefinicionProcedimientosForm" property="fechaLimiteHasta"/>';
        datos[6] = '<bean:write name="DefinicionProcedimientosForm" property="codArea"/>';
        datos[7] = '<bean:write name="DefinicionProcedimientosForm" property="descArea"/>';
        datos[8] = '<bean:write name="DefinicionProcedimientosForm" property="codTipoProcedimiento"/>';
        datos[9] = '<bean:write name="DefinicionProcedimientosForm" property="descTipoProcedimiento"/>';
        datos[10] = '<bean:write name="DefinicionProcedimientosForm" property="codEstado"/>';
        datos[11] = '<bean:write name="DefinicionProcedimientosForm" property="codTipoInicio"/>';
        datos[12] = '<bean:write name="DefinicionProcedimientosForm" property="plazo"/>';
        datos[13] = '<bean:write name="DefinicionProcedimientosForm" property="tipoPlazo"/>';
        datos[14] = '<bean:write name="DefinicionProcedimientosForm" property="tipoSilencio"/>';
        datos[23] = '<bean:write name="DefinicionProcedimientosForm" property="porcentaje"/>';       
        datos[24] = '<bean:write name="DefinicionProcedimientosForm" property="restringido"/>';
        datos[25] = '<bean:write name="DefinicionProcedimientosForm" property="interesadoOblig"/>';  
        //Mai ahora necesitamos recuperar este valor del Form
        datos[26]='<bean:write name="DefinicionProcedimientosForm" property="soloWS"/>';
        //Mai
		datos[27] = '<bean:write name="DefinicionProcedimientosForm" property="claseBuzonEntradaHistorico"/>';                      
        <logic:iterate id="elemento" name="DefinicionProcedimientosForm" property="tablaUnidadInicio">
          listaUnidadesInicio[cont4] = ['<bean:write name="elemento" property="codVisibleUnidadInicio" />',
                                        '<bean:write name="elemento" property="descUnidadInicio"/>'];
          listaUnidadesInicioOriginal[cont4] = ['<bean:write name="elemento" property="codUnidadInicio" />',
                                                '<bean:write name="elemento" property="descUnidadInicio"/>'];
          cont4++;
        </logic:iterate>
        if(listaUnidadesInicio.length >0) {
          datos[15] = listaUnidadesInicio[0][0];
          datos[16] = listaUnidadesInicio[0][1];
        } else {
          datos[15] = "";
          datos[16] = "";
        }
        datos[17] = '<bean:write name="DefinicionProcedimientosForm" property="disponible"/>';
        datos[18] = '<bean:write name="DefinicionProcedimientosForm" property="tramitacionInternet"/>';
        datos[19] = '<bean:write name="DefinicionProcedimientosForm" property="localizacion"/>';
        datos[20] = '<bean:write name="DefinicionProcedimientosForm" property="tramiteInicio"/>';
        datos[21] = '<bean:write name="DefinicionProcedimientosForm" property="cqUnidadInicio"/>';
        datos[22] = '<bean:write name="DefinicionProcedimientosForm" property="descripcionBreve"/>';
        // recuperamos del form el valor del campo biblioteca/libreria de flujo
        datos[28] = '<bean:write name="DefinicionProcedimientosForm" property="biblioteca"/>';
        // #303601: recuperamos del form el valor del campo numeracionExpedientesAnoAsiento
        datos[29] = '<bean:write name="DefinicionProcedimientosForm" property="numeracionExpedientesAnoAsiento"/>';


        <logic:iterate id="elemento" name="DefinicionProcedimientosForm" property="tramites">
        lista[cont3] = [datos[1],
                      datos[2],
                      '<bean:write name="elemento" property="codigoTramite"/>',
                      '<bean:write name="elemento" property="numeroTramite"/>',
                      '<bean:write name="elemento" property="nombreTramite"/>',
                      '<bean:write name="elemento" property="codClasificacionTramite"/>',
                      '<bean:write name="elemento" property="descClasificacionTramite"/>',
                      ];
        cont3++;
        </logic:iterate>

        <logic:iterate id="elemento" name="DefinicionProcedimientosForm" property="listasDoc">
        var tieneFirmas = '<bean:write name="elemento" property="requiereFirma" />';
                    var imagenFirma = "";
                    if (tieneFirmas == "1") {
                        imagenFirma = '<span class="fa fa-check 2x"></span>';
                    } else {
                        imagenFirma = '<span class="fa fa-close 2x"></span>';
                    }
                    listaDoc[cont1] = ['<bean:write name="elemento" property="nombreDocumento"/>',
                        '<bean:write name="elemento" property="condicion" />',
                        imagenFirma];
          cont1++;
        </logic:iterate>
        <logic:iterate id="elemento" name="DefinicionProcedimientosForm" property="listasDoc">
          listaDocOriginal[cont2] = ['<bean:write name="elemento" property="codigoDocumento" />',
                            '<bean:write name="elemento" property="nombreDocumento"/>',
                            '<bean:write name="elemento" property="condicion" />',
                        '<bean:write name="elemento" property="requiereFirma" />'];
          cont2++;
        </logic:iterate>
        var campoAct = "";
        var campoImg = "";
        <logic:iterate id="elemento" name="DefinicionProcedimientosForm" property="listaCampos">
            campoAct = '<bean:write name="elemento" property="activo" />';
            if (campoAct == "SI") {
                campoImg = '<span class="fa fa-check 2x"></span>';
            } else {
                campoImg = '<span class="fa fa-close 2x"></span>';
            }
          listaCampos[cont5] = ['<bean:write name="elemento" property="codCampo" />',
                              '<bean:write name="elemento" property="descCampo" />',campoImg];
          listaCamposOriginal[cont5] =['<bean:write name="elemento" property="codCampo" />','<bean:write name="elemento" property="descCampo" />',
                                     '<bean:write name="elemento" property="codPlantilla" />','<bean:write name="elemento" property="codTipoDato" />',
                                     '<bean:write name="elemento" property="tamano" />','<bean:write name="elemento" property="descMascara" />',
                                     '<bean:write name="elemento" property="obligat" />','<bean:write name="elemento" property="orden" />',
                                     '<bean:write name="elemento" property="descPlantilla" />','<bean:write name="elemento" property="descTipoDato" />',
                                     '<bean:write name="elemento" property="rotulo" />','<bean:write name="elemento" property="activo" />',
                                     '<bean:write name="elemento" property="oculto" />','<bean:write name="elemento" property="bloqueado" />',
                                     '<bean:write name="elemento" property="plazoFecha" />','<bean:write name="elemento" property="checkPlazoFecha" />',
                                     '<bean:write name="elemento" property="validacion" />','<bean:write name="elemento" property="operacion" />',
                                     '<bean:write name="elemento" property="codAgrupacion" />','<bean:write name="elemento" property="posX" />',
                                     '<bean:write name="elemento" property="posY" />'];
          cont5++;
        </logic:iterate>
        
        var campoAgrupacionActiva = "";
        var campoAgrupacionImg = "";
        <logic:iterate id="agrupacion" name="DefinicionProcedimientosForm" property="listaAgrupaciones">
            campoAgrupacionActiva = '<bean:write name="agrupacion" property="agrupacionActiva" />';
            if (campoAgrupacionActiva == "SI") {
                campoAgrupacionImg = '<span class="fa fa-check 2x"></span>';
            }else{
                campoAgrupacionImg = '<span class="fa fa-close 2x"></span>';
            }//if (campoAct == "SI") 
            
            listaAgrupacionesCampos[cont8] = ['<bean:write name="agrupacion" property="codAgrupacion" />', 
                '<bean:write name="agrupacion" property="descAgrupacion" />', '<bean:write name="agrupacion" property="ordenAgrupacion" />',
                campoAgrupacionImg];
            
            listaAgrupacionesCamposOriginal[cont8] = ['<bean:write name="agrupacion" property="codAgrupacion" />', 
                '<bean:write name="agrupacion" property="descAgrupacion" />', '<bean:write name="agrupacion" property="ordenAgrupacion" />',
                '<bean:write name="agrupacion" property="agrupacionActiva" />'];
            
            cont8++;
        </logic:iterate>
        
        <logic:iterate id="elemento" name="DefinicionProcedimientosForm" property="listaEnlaces">
          listaEnlaces[cont6] = ['<bean:write name="elemento" property="descEnlace" />',
                               '<bean:write name="elemento" property="urlEnlace" />',
                               '<bean:write name="elemento" property="estadoEnlace" />'];
          listaEnlacesOriginal[cont6] =['<bean:write name="elemento" property="codEnlace" />',
                                      '<bean:write name="elemento" property="descEnlace" />',
                                      '<bean:write name="elemento" property="urlEnlace" />',
                                      '<bean:write name="elemento" property="estadoEnlace" />'];
          cont6++;
        </logic:iterate>
        <logic:iterate id="elemento" name="DefinicionProcedimientosForm" property="listaRoles">
          var pD = '<bean:write name="elemento" property="rolPorDefecto" />';
          var pCw = '<bean:write name="elemento" property="consultaWebRol" />';
          var marca="";
          var marcaCw = "";
          if(pD == 1) {
            marca = "X";
          }          
          if(pCw == 1) {
            marcaCw = "X";
          }                    
          listaRoles[cont7] = ['<bean:write name="elemento" property="codRol" />',
                               '<bean:write name="elemento" property="descRol" />',
                               marca,marcaCw];
          listaRolesOriginal[cont7] =['<bean:write name="elemento" property="codRol" />',
                                    '<bean:write name="elemento" property="descRol" />',
                                    '<bean:write name="elemento" property="rolPorDefecto" />',
                                    '<bean:write name="elemento" property="consultaWebRol" />'];
          cont7++;
        </logic:iterate>



       // Lista de campos de la vista de pendientes disponibles
       var listaCamposDisponibles   = new Array();
       var listaCamposSeleccionados = new Array();
       var contador = 0;

        
       <logic:iterate name="DefinicionProcedimientosForm" id="seleccionado" property="camposPendientesSeleccionados">
            listaCamposSeleccionados[contador] = ['<bean:write name="seleccionado" property="codigo" ignore="true"/>',
                                                '<bean:write name="seleccionado" property="nombreCampo" ignore="true"/>',
                                                '<bean:write name="seleccionado" property="tamanho" ignore="true"/>',
                                                '<bean:write name="seleccionado" property="activo" ignore="true"/>',
                                                '<bean:write name="seleccionado" property="orden" ignore="true"/>',
                                                '<bean:write name="seleccionado" property="descripcionCampoSuplementario" ignore="true"/>',
                                                '<bean:write name="seleccionado" property="campoSuplementario" ignore="true"/>'];
             contador++;
        </logic:iterate>

        /**** PLUGIN DE FINALIZACIÓN NO CONVENCINAL *****/
        var codServicioFinalizacion = '<bean:write name="DefinicionProcedimientosForm" property="codServicioFinalizacion" ignore="true"/>';
        var implClassServicioFinalizacion = '<bean:write name="DefinicionProcedimientosForm" property="implClassServicioFinalizacion" ignore="true"/>';
        /**** PLUGIN DE FINALIZACIÓN NO CONVENCIONAL *****/

        var lUI = crearListas();
        var listaCodUnidadesInicio = lUI[0];
        var listaDescUnidadesInicio = lUI[1];
        var listaCodVisibleUnidadesInicio = lUI[2]; 
        parent.mainFrame.recuperaDatos(datos,lista,listaDoc,listaDocOriginal,
        listaCodUnidadesInicio,listaDescUnidadesInicio,listaCampos,listaCamposOriginal,listaEnlaces,
        listaEnlacesOriginal,listaRoles,listaRolesOriginal,listaUnidadesInicio,listaCodVisibleUnidadesInicio,listaCamposSeleccionados,
        codServicioFinalizacion,implClassServicioFinalizacion,listaAgrupacionesCampos, listaAgrupacionesCamposOriginal);

      }



    function crearListas() { 
      var listaCodUnidadesInicio = "";
      var listaCodVisibleUnidadesInicio = "";
      var listaDescUnidadesInicio = "";
      var listas = new Array();
      for (i=0; i < listaUnidadesInicio.length; i++) {
        listaCodUnidadesInicio += listaUnidadesInicioOriginal[i][0]+'§¥';
        listaCodVisibleUnidadesInicio += listaUnidadesInicio[i][0]+'§¥';
        listaDescUnidadesInicio += listaUnidadesInicioOriginal[i][1]+'§¥';
      }
      listas = [listaCodUnidadesInicio,listaDescUnidadesInicio,listaCodVisibleUnidadesInicio];
      return listas;
    }
    </script>
</head>
<body onLoad="redirecciona();">
</body>
</html>
