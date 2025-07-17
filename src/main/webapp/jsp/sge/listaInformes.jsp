<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%--
<%@page import="es.altia.agora.interfaces.user.web.padron.mantenimiento.TitulacionesForm"%>
--%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<%@page import="es.altia.agora.business.geninformes.utils.UtilidadesXerador"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>

<%
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  int idioma=1;
  int apl=4;
  int usuCod=0;
  String[] params=null;
  String soloConsulta = "no";

  if (session.getAttribute("usuario") != null){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    idioma = usuarioVO.getIdioma();
    apl = usuarioVO.getAppCod();
    usuCod=usuarioVO.getIdUsuario();
	soloConsulta = usuarioVO.getSoloConsultarExp();

    params = usuarioVO.getParamsCon();
  }
  System.err.println("Llego1");
  String arrayJScript =null;
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= usuarioVO.getAppCod()%>" />

<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title> <%=descriptor.getDescripcion("gEtiq_MantInformes")%> </title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

    
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script language="JavaScript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script language="JavaScript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
    <script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/utilidadesXerador.js'/>"></script>
</head>
<body class="bandaBody" scroll=no onload="javascript:{pleaseWait('off');   }">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>


    <html:form action="/XeradorInformesAction.do" method="POST" target="oculto" styleId="XeradorInformesForm">
    <html:hidden property="textOperacion" styleId="textOperacion" />
    <html:hidden property="COD_INFORMEXERADOROculto" styleId="COD_INFORMEXERADOROculto" />
    <input  type="hidden"  name="opcion" id="opcion">
    <input  type="hidden"  name="identificador" id="identificador">
    <input  type="hidden"  name="nombrePlantilla" value="">

    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiq_MantInformes")%></div>
    <div class="contenidoPantalla">
        <table width="100%" height="100%" align="center" cellpadding="0px" cellspacing="3px" border="0px" bgcolor="#e6e6e6">
            <tr><!-- En esta fila va el combo de Aplicaciones -->
            <td align="center" class="etiqueta"><html:text style="visibility:hidden;height:0;width:0;" styleId="codAplicacion" property="codAplicacion"/> <% System.err.println("Llego5");%><input class="inputTexto" type="hidden" id="descAplicacion" name="descAplicacion" style="width:200;"><a href="" id="anchorAplicacion" name="anchorAplicacion"></a></td>
            </tr>
            <tr>
                <td id="tabla" width="100%" height="85%" align="center" valign="bottom"></td>
            </tr>
            <tr>
                <td height="15%" align="center" valign="top">
                    <table width="700px" align="center">
                        <tr>
                            <!-- Codigo -->
                            <td width="10%" align="left">
                                <!--<input type="text" class="inputTextoObligatorio" id="obligatorio"  name="txtCodigo" size="11" maxlength="2" onKeyPress="javascript:PasaAMayusculas(event);">-->
                            </td>
                            <!-- Descripcion -->
                            <td width="90%" align="left">
                                <!--<input name="txtDescripcion" id="obligatorio" type="text" class="inputTextoObligatorio" size="120" maxlength="100" onKeyPress="javascript:PasaAMayusculas(event);" onKeyUp="buscar();">-->
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <div id="tabla2" class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value="Visualizar" name="cmdVisualizar" onClick="PulsaBoton(event);">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAlta")%>" name="cmdAlta" onClick="PulsaBoton(event);">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbModificar")%>" name="cmdModificar" onClick="PulsaBoton(event);">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>" name="cmdEliminar" onClick="PulsaBoton(event);">
            <input type= "button" class="botonGeneral" value="Editar Plantilla" name="cmdEditarPlantilla" onClick="PulsaBoton(event);">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSalir")%>" name="cmdSalir"   onClick="PulsaBoton(event);">
        </div>
    </div>
    </html:form>
    <script type="text/javascript">

      var ultimo = false;
        var lista= new Array();

      function Inicio()
      {
        window.focus();
        CargaVectorGlobalAplicacions();
        CargaComboAplicacions();
        document.forms[0].codAplicacion.value = '<%=apl%>';
        eventoCambiaAplicacion();
        EventoCambioTabla();
            }

      function pulsarSalir(){
        <% if(soloConsulta.equals("no")) { %>
          document.forms[0].opcion.value="inicio";
          document.forms[0].target="mainFrame";
          document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
          document.forms[0].submit();
        <% } else { %>
          document.forms[0].opcion.value="inicio";
          document.forms[0].target="mainFrame";
          document.forms[0].action="<c:url value='/sge/ConsultaExpedientes.do'/>";
          document.forms[0].submit();
        <% } %>
      }





    var Estado = 'CONSULTA';



    //
    // Objetos Javascript represenatando a los campos HTML
    //
    var URLBaseAction="<c:url value='/XeradorInformesAction.do?operacion='/>";

    var formulario=document.all.XeradorInformesForm;

    var textOperacion=formulario.textOperacion;
    var COD_INFORMEXERADOROculto = formulario.COD_INFORMEXERADOROculto;
    var codAplicacion = formulario.codAplicacion;

    var ncomboCampoOrdenacion_1 =new Combo('Aplicacion');

    var VectorInformes;

    var VectorGlobalAplicacions;

    var tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
        tab.addColumna('45','center','<%=descriptor.getDescripcion("gEtiq_codigo")%>');
        tab.addColumna('170','left','<%=descriptor.getDescripcion("gEtiqNombre")%>');
        tab.addColumna('430','left','<%=descriptor.getDescripcion("gEtiq_desc")%>');
        tab.addColumna('35','center','<%=descriptor.getDescripcion("gEtiq_PlantEditada")%>');
        tab.displayCabecera=true;
    tab.displayTabla();
    tab.displayDatos=EventoCambioTabla;

      function refresh(){
        tab.displayTabla();
      }



      function PulsaBoton(evento)
      {
            var objeto;
            if (document.all) objeto = evento.srcElement;
            else objeto=evento.target;

            switch (objeto.name)
            {
                case 'cmdAlta':
                    AccionAltaInforme();
                    break;
                case 'cmdModificar':
                    AccionModificarInforme();
                    break;
                case 'cmdEliminar':
                    AccionEliminarInforme();
                    break;
                case 'cmdEditarPlantilla':
                    AccionEditarPlantilla();
                    break;
                case 'cmdVisualizar':
                    AccionVisualizar();
                    break;
            case 'cmdSalir':
                AccionSalir();
                break;

                default:break;
            }
      }


      function AccionSalir()
      {
        pulsarSalir();
      }





    function eventoCambiaAplicacion() {
    CubreTablaInformes();

    }

    function CubreTablaInformes()
    {
        //InicializaControles();
    if (Trim(codAplicacion.value)!='') {
        textOperacion.value='CLI';



        VectorInforme=new Array();

        formulario.action = URLBaseAction+'CLI';
        formulario.target = 'oculto';
        formulario.submit();
        } else {
            tab.clearTabla();
            EventoCambioTabla();
        }


    }

    function callbackCubreTablaInformes(vInformes)
    {
        //
        //
        // en vInforme viene [CODIGO,NOME,DESCRIPCION,PLANT_EDITADA]
        //alert('Recibo en cbCCI:'+vInformes+'.');
        if ( (vInformes) && (vInformes.length>0) )
        {
        VectorInformes=unescapeArray(vInformes);
        //VectorInformes=VectorInforme[0];

        CubreControles();
        } else {
            tab.clearTabla();
            var linea=['','','<%=descriptor.getDescripcion("gEtiq_NoInf")%>',''];
            tab.addLinea(linea);
        }
        EventoCambioTabla();

        //EstableceEstadoConsulta();
    }

    function CubreControles()
    {
    var linea=new Array();
    tab.clearTabla();
    var v;

    for (var i=0;i<VectorInformes.length;i++) {
        v=VectorInformes[i];
        linea=new Array();
        linea[0]=v[0];
        linea[1]=Trim(v[1]);
        linea[2]=Trim(v[2]);
        linea[3]=Trim(v[3]);
        linea['CODIGO']=v[0];
        tab.addLinea(linea);
    }

    }


    function AccionVisualizar()
    {
    var lin;

    if (tab.selectedIndex>-1) {
	    lin=tab.getLinea(tab.selectedIndex);
   		COD_INFORMEXERADOROculto.value=lin['CODIGO'];
        if (lin[3]=='S')
            VisualizaInforme(COD_INFORMEXERADOROculto.value);
        else
        	jsp_alerta('A','<%=descriptor.getDescripcion("msjEditarPlant")%>' + COD_INFORMEXERADOROculto.value+'.');
    } else 
       	jsp_alerta('A','<%=descriptor.getDescripcion("msjElegirInf")%>');
       	
    }


    function AccionModificarInforme()
    {
    var lin;

    if (tab.selectedIndex>-1) {
 	   lin=tab.getLinea(tab.selectedIndex);
    	COD_INFORMEXERADOROculto.value=lin['CODIGO'];
        ModificarInforme(COD_INFORMEXERADOROculto.value);
    } else
       	jsp_alerta('A','<%=descriptor.getDescripcion("msjElegirInf")%>');

    }


    function AccionAltaInforme()
    {
    var lin;

    if (Trim(codAplicacion.value)!='')
        AltaInforme(codAplicacion.value);
	else
		jsp_alerta('A','<%=descriptor.getDescripcion("msjElegirAplic")%>');
    }

    function AccionEliminarInforme()
    {
    var lin;

    if (tab.selectedIndex>-1) {
    	lin=tab.getLinea(tab.selectedIndex);
    	COD_INFORMEXERADOROculto.value=lin['CODIGO'];
        if (jsp_alerta('C','<%=descriptor.getDescripcion("msjEstaSeguro")%>')==1)
    	    EliminarInforme(COD_INFORMEXERADOROculto.value);
    } else
    	jsp_alerta('A','<%=descriptor.getDescripcion("msjElegirInf")%>');
    	
    }


    function AccionEditarPlantilla()
    {
    var lin;

    if (tab.selectedIndex>-1) {
    	lin=tab.getLinea(tab.selectedIndex);
    	COD_INFORMEXERADOROculto.value=lin['CODIGO'];
        EditarPlantillaInforme(COD_INFORMEXERADOROculto.value);
    } else
    	jsp_alerta('A','<%=descriptor.getDescripcion("msjElegirInf")%>');

    }


    function VisualizaInforme(cod)
    {
            textOperacion.value='I';

            COD_INFORMEXERADOROculto.value=cod;

            //
            // divEspera
            //
            //MostrarMensajeEspera(getObjetoPorId('divEspera'));
            //getObjetoPorId('divEspera').style.visibility='visible';

            formulario.action = URLBaseAction+'I';
            formulario.target = 'oculto';
            formulario.submit();
    }

    function callbackVisualizaInforme(urlXML,urlDOC,XML)
    {
    ejecutaDocumento(urlDOC,unescape(XML),false);
    }


    function ModificarInforme(cod){
      lin=tab.getLinea(tab.selectedIndex);
      COD_INFORMEXERADOROculto.value=lin['CODIGO'];
      var source = "<c:url value='/informes/GeneradorInformes.do?opcion=recuperarInforme&operacion='/>"+codAplicacion.value+"&tipoFichero="+COD_INFORMEXERADOROculto.value;
      abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,null,
	'width=750,height=475,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                            recargarLista();
                        }
                    });
    }

    function callbackModificarInforme(v) {
    }

    function AltaInforme(codAplicacion){
      var source = "<c:url value='/informes/GeneradorInformes.do?opcion=inicio&operacion='/>"+codAplicacion;
      abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,null,
	'width=750,height=475,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                            recargarLista();
                        }
                    });
    }

    function recargarLista() {
        CubreTablaInformes();
    }

    function callbackAltaInforme(v) {
    }

    function EliminarInforme(cod) {
            textOperacion.value='B';

            COD_INFORMEXERADOROculto.value=cod;

            formulario.action = URLBaseAction+'B';
            formulario.target = 'oculto';
            formulario.submit();
    }

    function callbackEliminarInforme(cod) {
        recargarLista();
    }

    function EditarPlantillaInforme(cod){
        if(tab.selectedIndex != -1) {
            var lin=tab.getLinea(tab.selectedIndex);
                document.forms[0].target="oculto";
                document.forms[0].action="<c:url value='/jsp/informes/main2.jsp?op=GEP&id='/>"+cod+"&nombrePlantilla="+lin[1];
                document.forms[0].submit();
            }
    }

    function callbackEditarPlantillaInforme(v) {
    }

    function cargaDocumentos(){
        recargarLista();
    }

    function EstableceEstadoConsulta() {
        Estado = 'CONSULTA';
        EditableControles(false);
    }



    function EditableControles(NuevoEstado) {
        var cond=!(NuevoEstado && true);
        var cond2=!(NuevoEstado && true && Estado=='ENGADIR');
        var que;

        if (NuevoEstado) que='activate();'; else que='deactivate();';
        tab.readOnly=false;//cond;

        cmdEditarPlantilla.disabled=cond;
        cmdVisualizar.disabled=cond;
        cmdModificar.disabled=cond;
        cmdAlta.disabled=cond;
        cmdEliminar.disabled=cond;
    }

    function EventoCambioTabla() {
        if (tab.selectedIndex>-1) {
            HabilitaModificar(true);
            HabilitaEditarPlantilla(true);
            HabilitaEliminar(true);
            var lin=tab.getLinea(tab.selectedIndex);
            var cond=(lin[3]=='S');
            HabilitaVisualizar(cond);

        } else {
            HabilitaModificar(false);
            HabilitaEditarPlantilla(false);
            HabilitaEliminar(false);
            HabilitaVisualizar(false);
            HabilitaEngadir((Trim(codAplicacion.value)!=''));
        }
    }

    function HabilitaEliminar(NuevoEstado) {
        HabilitaBoton(getObjetoPorId('cmdEliminar'),(NuevoEstado && true));
    }

    function HabilitaEngadir(NuevoEstado) {
        HabilitaBoton(getObjetoPorId('cmdAlta'),(NuevoEstado &&  true));
    }

    function HabilitaVisualizar(NuevoEstado) {
        HabilitaBoton(getObjetoPorId('cmdVisualizar'),NuevoEstado &&  true);
    }

    function HabilitaModificar(NuevoEstado) {
        HabilitaBoton(getObjetoPorId('cmdModificar'),NuevoEstado &&  true);
    }

    function HabilitaSalir(NuevoEstado) {
        HabilitaBoton(getObjetoPorId('cmdSalir'),NuevoEstado &&  true);
    }

    function HabilitaEditarPlantilla(NuevoEstado) {
        HabilitaBoton(getObjetoPorId('cmdEditarPlantilla'),NuevoEstado  && true);
    }

      function CargaVectorGlobalAplicacions() {
      // Contiene un array de [CENCOD, CENNOM ]
      <%
        arrayJScript =null;
        try
        {
            // es.altia.util.Debug.crearDirectorio();
            //
            //
            es.altia.util.HashtableWithNull tempTabla= new es.altia.util.HashtableWithNull();
            //String pestanaOrixe=request.getParameter(ConstantesXerador.COD_PESTANAORIXE);
            tempTabla.put("AAU_USU",usuCod+"");//USUARIO.getCOD_PESTANA()); AAU_USU = ? AND AID_IDI = ?
            tempTabla.put("AID_IDI",idioma+"");
            tempTabla.put("PARAMS",params);//USUARIO.getCOD_PESTANA());
            tempTabla.put("TRAERGEN","N");
            //es.altia.util.Debug.println("EN JSPIII MNUCOD ES:"+pestanaOrixe+".");
            arrayJScript =es.altia.agora.business.geninformes.utils.UtilidadesXerador.ObtenerArrayJSAplicacionesUsuario(tempTabla);
        }
        catch (Exception e)
        {
            //es.altia.util.Debug.printException(e.getExcepcion());
        }
      %>
      VectorGlobalAplicacions=unescapeArray(<%=arrayJScript%>);
      }

    function CargaComboAplicacions() {
    var cods=new Array();
    var descs=new Array();
    var par;

        for (var i=0;i<VectorGlobalAplicacions.length;i++) {
        par=VectorGlobalAplicacions[i];
        cods[i]=par[0];
        descs[i]=par[1];
        }
        ncomboCampoOrdenacion_1.addItems(cods,descs);

    }

    Inicio();
    </script>
</body>
</html:html>
