<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="es.altia.agora.business.geninformes.utils.UtilidadesXerador"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<%
            Log m_log = LogFactory.getLog(this.getClass().getName());
            es.altia.common.service.config.Config m_ConfigTechnical = es.altia.common.service.config.ConfigServiceHelper.getConfig("techserver");
            String url = request.getContextPath() + m_ConfigTechnical.getString("URL.caducaSesion");
            try {
                session = request.getSession(true);
                if (m_log.isDebugEnabled()) {
                    m_log.debug("checkSession: " + session.getId());
                }
                if ((session.getAttribute("usuarioEscritorio") == null)) {
                    if (m_log.isDebugEnabled()) {
                        m_log.debug("checkSession: session.isNew()");
                    }
                    response.sendRedirect(response.encodeURL(url));

                }

            } catch (Exception e) {
                    m_log.error("Error en checkSession");
            }

            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 3;
            int usuCod = 0;
            String css="";
            String[] params = null;

            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
                apl = usuarioVO.getAppCod();
                usuCod = usuarioVO.getIdUsuario();
                css=usuarioVO.getCss();

                params = usuarioVO.getParamsCon();
            }
            String arrayJScript = null;
            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= usuarioVO.getAppCod()%>" />

<html:html>
    <head>
        <title> <%=descriptor.getDescripcion("gEtiq_MantEstructura")%> </title>
        
        <jsp:include page="/jsp/informes/tpls/app-constants.jsp" />
        

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript" SRC="<c:url value='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
          <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/utilidadesXerador.js'/>"></script>
    </head>
    <body class="bandaBody" scroll=no onload="javascript:{pleaseWait('off');}">
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        
<html:form action="/informes/mantenimiento/MantenimientosInformesAction.do" method="POST" target="oculto" styleId="MantenimientosInformesForm">
    <html:hidden property="textOperacion" styleId="textOperacion" />
    <html:hidden property="COD_INFORMEXERADOROculto" styleId="COD_INFORMEXERADOROculto" />
    <html:hidden property="COD_ENTIDADEINFORMEOculto" styleId="COD_ENTIDADEINFORMEOculto" />
    <input  type="hidden"  name="opcion" id="opcion">
    <input  type="hidden"  name="identificador" id="identificador">
    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiq_MantEstructura")%></div>
    <div class="contenidoPantalla">
        <table style="width:100%">
            <tr><!-- En esta fila va el combo de Aplicaciones -->
                <td class="etiqueta">
                    Aplicaci&oacute;n:&nbsp;&nbsp;&nbsp;&nbsp;
                    <html:text style="visibility:hidden;height:0;width:0;" styleId="codAplicacion" property="codAplicacion"/> 
                    <input class="inputTexto" type="text" id="descAplicacion" name="descAplicacion" style="width:300;">
                    <a href="" id="anchorAplicacion" name="anchorAplicacion">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonAplicacion" name="botonAplicacion" style="cursor:hand;"></span>
                    </a>
                </td>
            </tr>

            <tr style="padding-top: 10px">
                <td id="tabla"></td>
            </tr>                                                    
        </table>
        <div class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAlta")%>" name="cmdAlta" id="cmdAlta" onClick="PulsaBoton(event);">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbModificar")%>" name="cmdModificar" id="cmdModificar" onClick="PulsaBoton(event);">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>" name="cmdEliminar" id="cmdEliminar" onClick="PulsaBoton(event);">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSalir")%>" name="cmdSalir" id="cmdSalir"  onClick="PulsaBoton(event);">
        </div>            
    </div>            
</html:form>
<script type="text/javascript">
            var ultimo = false;
            var lista= new Array();
            var VectorGlobalEntidades=new Array();
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
                
                if (ncomboAplicacion.base.style.visibility == "visible") setTimeout('ncomboAplicacion.ocultar()',20);
            }
            if (teclaAuxiliar == 9){
                ncomboAplicacion.ocultar();
            }
            }


            function Inicio()
            {
                window.focus();
                CargaVectorGlobalAplicacions();
                CargaComboAplicacions();
                ncomboAplicacion.change=eventoCambiaAplicacion;
                
                CubreTablaInformes();
                EventoCambioTabla();
            }
            
            function eventoCambiaAplicacion() {
                CubreTablaInformes();
                
            }
            
            
            function pulsarSalir(){
                document.forms[0].target = "mainFrame";
                document.forms[0].action = "<c:url value='/jsp/administracion/presentacionADM.jsp'/>";
                document.forms[0].submit();
            }
            
            var Estado = 'CONSULTA';
            
            //
            // Objetos Javascript represenatando a los campos HTML
            //
            var URLBaseAction='<c:url value='/informes/mantenimiento/MantenimientosInformesAction.do?operacion='/>';
            
            var formulario=document.all.MantenimientosInformesForm;
            
            var textOperacion=formulario.textOperacion;
            var COD_INFORMEXERADOROculto = formulario.COD_INFORMEXERADOROculto;
            var codAplicacion = formulario.codAplicacion;
            
            var COD_ENTIDADEINFORMEOculto=formulario.COD_ENTIDADEINFORMEOculto;
            
            //var ncomboCampoOrdenacion_1 =new Combo('Aplicacion');
            
            var VectorInformes;
            
            var VectorGlobalAplicacions;
            var ncomboAplicacion=new Combo('Aplicacion');
            
            
            var tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
            
            tab.addColumna('120','center','<%=descriptor.getDescripcion("gEtiq_CodEntidad")%>');
            tab.addColumna('450','left','<%=descriptor.getDescripcion("gEtiq_Nombre")%>');
            tab.addColumna('370','left','<%=descriptor.getDescripcion("gEtiq_NombreEntidad")%>');
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
                                AccionAltaEntidad();
                                break;
                                case 'cmdModificar':
                                AccionModificarEntidad();
                                break;
                                case 'cmdEliminar':
                                AccionEliminarEntidad();
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
                        
                        function CubreTablaInformes()
                        {
                            textOperacion.value='CLEST';
                            
                            
                            if (codAplicacion.value!='') {
                                VectorInforme=new Array();
                                
                                formulario.action = URLBaseAction+'CLEST';
                                formulario.target = 'oculto';
                                
                                formulario.submit();
                            }
                            
                        }
                        
                        function callbackCubreTablaInformes(vInformes)
                        {
                            //
                            //
                            // en vInforme viene [CODIGO,NOME,DESCRIPCION,PLANT_EDITADA]
                            
                            
                            if ( (vInformes) && (vInformes.length>0) )
                                {
                                    VectorInformes=unescapeArray(vInformes);
                                    //VectorInformes=VectorInforme[0];
                                    
                                    CubreControles();
                                } else {
                                tab.clearTabla();
                                var linea=['-','<%=descriptor.getDescripcion("gEtiq_NoEstruc")%>','-'];
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
                                // ESTRUCTURAINFORME.COD_ESTRUCTURA AS COD_ESTRUCTURA,A_DOC.DOC_APL AS APL_DOC,A_DOC.DOC_NOM AS NOME,ESTRUCTURAINFORME.COD_ENTIDADEINFORME AS COD_ENTIDADEINFORME,EI.NOME AS NOME_ENT
                                for (var i=0;i<VectorInformes.length;i++) {
                                    v=VectorInformes[i];
                                    linea=new Array();
                                    linea[0]=v[0];
                                    linea[1]=Trim(v[2]);
                                    linea[2]=Trim(v[4]);
                                    //linea[2]=Trim(v[2]);
                                    //linea[3]=Trim(v[3]);
                                    linea['CODIGO']=v[0];
                                    linea['COD_ENTIDADEINFORME']=v[4];
                                    tab.addLinea(linea);
                                }
                                
                            }
                            
                            
                            //function AccionVisualizar()
                            //{
                            //var lin;
                            //
                            //if (tab.selectedIndex>-1) {
                            //lin=tab.getLinea(tab.selectedIndex);
                            //COD_INFORMEXERADOROculto.value=lin['CODIGO'];
                            //	if (lin[3]=='S') {
                            //		VisualizaInforme(COD_INFORMEXERADOROculto.value);
                            //	} else jsp_alerta('A','Debe editar primero la plantilla para el informe con código:'+COD_INFORMEXERADOROculto.value+'.');
                            //
                            //} else jsp_alerta('A','Debe elegir un informe.');
                            //}
                            
                            
                            function AccionModificarEntidad()
                            {
                                var lin;
                                
                                if (tab.selectedIndex>-1) {
                                    lin=tab.getLinea(tab.selectedIndex);
                                    COD_ENTIDADEINFORMEOculto.value=lin['CODIGO'];
                                    ModificarEntidad(COD_ENTIDADEINFORMEOculto.value);
                                } else 
                                jsp_alerta('A','<%=descriptor.getDescripcion("msjElegirEst")%>');
                                }
                                
                                function AccionAltaEntidad()
                                {
                                    var lin;
                                    
                                    if (Trim(codAplicacion.value)!='') {
                                        
                                        AltaEntidad(codAplicacion.value);
                                    } else
                                    jsp_alerta('A','<%=descriptor.getDescripcion("msjElegirAplic")%>');
                                    }
                                    
                                    function AccionEliminarEntidad()
                                    {
                                        var lin;
                                        
                                        if (tab.selectedIndex>-1) {
                                            lin=tab.getLinea(tab.selectedIndex);
                                            COD_ENTIDADEINFORMEOculto.value=lin['CODIGO'];
                                            if (jsp_alerta('C','¿Está seguro?')==1)
                                                EliminarEntidad(COD_ENTIDADEINFORMEOculto.value);
                                        } else
                                        jsp_alerta('A','<%=descriptor.getDescripcion("msjElegirEnt")%>');
                                        }
                                        
                                        
                                        
                                        function ModificarEntidad(cod)
                                        {
                                            var datos = window.open('<%=request.getContextPath()%>/jsp/informes/mantenimiento/main2.jsp?op=M&id='+cod+"&codApliInforme="+codAplicacion.value+"&kk="+(new Date()),'ventana',"width=998px,height=580px,status=<%=statusBar%>, scroll=no");
                                                if(datos!=undefined){
                                                    recargarLista();
                                                }
                                            }
                                            
                                            
                                            function callbackModificarInforme(v)
                                            {
                                                
                                            }
                                            
                                            function AltaEntidad(codAp)
                                            {
                                                var datos = window.open('<%=request.getContextPath()%>/jsp/informes/mantenimiento/main2.jsp?op=A&id='+codAp+"&kk="+(new Date()),null,'width=998px,height=580px,status='+ '<%=statusBar%>' + ',scrollbar=no');
                                                    if(datos!=undefined){
                                                        recargarLista();
                                                    }
                                                }
                                                
                                                function recargarLista()
                                                {
                                                    CubreTablaInformes();
                                                }
                                                
                                                
                                                function callbackAltaInforme(v)
                                                {
                                                }
                                                
                                                function EliminarEntidad(cod)
                                                {
                                                    textOperacion.value='B';
                                                    
                                                    COD_INFORMEXERADOROculto.value=cod;
                                                    
                                                    //
                                                    // divEspera
                                                    //
                                                    //MostrarMensajeEspera(getObjetoPorId('divEspera'));
                                                    //getObjetoPorId('divEspera').style.visibility='visible';
                                                    
                                                    formulario.action = URLBaseAction+'B';
                                                    formulario.target = 'oculto';
                                                    formulario.submit();
                                                }
                                                
                                                function callbackEliminarEntidad(cod)
                                                {
                                                    recargarLista();
                                                    //tab.removeLinea(tab.selectedIndex);
                                                    //if (tab.lineas.length>0) tab.selectLinea(0);
                                                }
                                                
                                                
                                                
                                                function EditarPlantillaInforme(cod)
                                                {
                                                    var datos = window.open('<%=request.getContextPath()%>/jsp/informes/main2.jsp?op=GEP&id='+cod,null,'width=455px,height=160px,status='+ '<%=statusBar%>' + ',scrollbar=no');
                                                        if(datos!=undefined){
                                                            recargarLista();
                                                        }
                                                    }
                                                    
                                                    function callbackEditarPlantillaInforme(v)
                                                    {
                                                        
                                                    }
                                                    
                                                    
                                                    
                                                    function EstableceEstadoConsulta()
                                                    {
                                                        Estado = 'CONSULTA';
                                                        EditableControles(false);
                                                    }
                                                    
                                                    
                                                    
                                                    function EditableControles(NuevoEstado)
                                                    {
                                                        var cond=!(NuevoEstado && true);
                                                        var cond2=!(NuevoEstado && true && Estado=='ENGADIR');
                                                        var que;
                                                        
                                                        if (NuevoEstado) que='activate();'; else que='deactivate();';
                                                        
                                                        tab.readOnly=false;//cond;
                                                        
                                                        
                                                        //	cmdSalir.disabled=cond;
                                                        cmdEditarPlantilla.disabled=cond;
                                                        cmdVisualizar.disabled=cond;
                                                        cmdModificar.disabled=cond;
                                                        cmdAlta.disabled=cond;
                                                        cmdEliminar.disabled=cond;
                                                        
                                                    }
                                                    
                                                    function EventoCambioTabla()
                                                    {
                                                        if (tab.selectedIndex>-1)
                                                            {
                                                                var lin=tab.getLinea(tab.selectedIndex);
                                                                if (lin[0]!="-") {
                                                                    HabilitaModificar(true);
                                                                    //HabilitaEditarPlantilla(true);
                                                                    HabilitaEliminar(true);
                                                                    //var lin=tab.getLinea(tab.selectedIndex);
                                                                    //var cond=(lin[3]=='S');
                                                                    //HabilitaVisualizar(cond);
                                                                } else {
                                                                HabilitaModificar(false);
                                                                //HabilitaEditarPlantilla(false);
                                                                HabilitaEliminar(false);
                                                                //HabilitaVisualizar(false);
                                                                HabilitaEngadir(true);
                                                            }
                                                        } else {
                                                        HabilitaModificar(false);
                                                        //HabilitaEditarPlantilla(false);
                                                        HabilitaEliminar(false);
                                                        //HabilitaVisualizar(false);
                                                        HabilitaEngadir(true);
                                                    }
                                                    
                                                }
                                                
                                                function HabilitaEliminar(NuevoEstado)
                                                {
                                                    HabilitaBoton(getObjetoPorId('cmdEliminar'),(NuevoEstado && true));
                                                }
                                                
                                                function HabilitaEngadir(NuevoEstado)
                                                {
                                                    HabilitaBoton(getObjetoPorId('cmdAlta'),(NuevoEstado &&  true));
                                                }
                                                
                                                function HabilitaVisualizar(NuevoEstado)
                                                {
                                                    HabilitaBoton(getObjetoPorId('cmdVisualizar'),NuevoEstado &&  true);
                                                }
                                                
                                                function HabilitaModificar(NuevoEstado)
                                                {
                                                    HabilitaBoton(getObjetoPorId('cmdModificar'),NuevoEstado &&  true);
                                                }
                                                
                                                function HabilitaSalir(NuevoEstado)
                                                {
                                                    HabilitaBoton(getObjetoPorId('cmdSalir'),NuevoEstado &&  true);
                                                }
                                                
                                                function HabilitaEditarPlantilla(NuevoEstado)
                                                {
                                                    HabilitaBoton(getObjetoPorId('cmdEditarPlantilla'),NuevoEstado  && true);
                                                }
                                                
                                                function CargaVectorGlobalAplicacions()
                                                {
                                                    // Contiene un array de [CENCOD, CENNOM ]
  <%
            arrayJScript = null;
            try {
                // es.altia.util.Debug.crearDirectorio();
                //
                //
                es.altia.util.HashtableWithNull tempTabla = new es.altia.util.HashtableWithNull();
                //String pestanaOrixe=request.getParameter(ConstantesXerador.COD_PESTANAORIXE);
                tempTabla.put("AAU_USU", usuCod + "");//USUARIO.getCOD_PESTANA()); AAU_USU = ? AND AID_IDI = ?
                tempTabla.put("AID_IDI", idioma + "");
                tempTabla.put("PARAMS", params);//USUARIO.getCOD_PESTANA());
                tempTabla.put("TRAERGEN", "N");
                //es.altia.util.Debug.println("EN JSPIII MNUCOD ES:"+pestanaOrixe+".");
                arrayJScript = es.altia.agora.business.geninformes.utils.UtilidadesXerador.ObtenerArrayJSAplicacionesUsuario(tempTabla);
            } catch (Exception e) {
            //es.altia.util.Debug.printException(e.getExcepcion());
            }
  %>
      VectorGlobalAplicacions=unescapeArray(<%=arrayJScript%>);
      }
      
      
      function CargaVectorGlobalEntidades()
      {
          // Contiene un array de [CENCOD, CENNOM ]
  <%
            arrayJScript = null;
            try {
                // es.altia.util.Debug.crearDirectorio();
                //
                //
                es.altia.util.HashtableWithNull tempTabla = new es.altia.util.HashtableWithNull();
                //String pestanaOrixe=request.getParameter(ConstantesXerador.COD_PESTANAORIXE);
                tempTabla.put("AAU_USU", usuCod + "");//USUARIO.getCOD_PESTANA()); AAU_USU = ? AND AID_IDI = ?
                tempTabla.put("AID_IDI", idioma + "");
                tempTabla.put("PARAMS", params);//USUARIO.getCOD_PESTANA());
                tempTabla.put("TRAERGEN", "N");
                //es.altia.util.Debug.println("EN JSPIII MNUCOD ES:"+pestanaOrixe+".");
                arrayJScript = es.altia.agora.business.geninformes.utils.UtilidadesXerador.ObtenerArrayJSEntidadesInforme(tempTabla);
            } catch (Exception e) {
            //es.altia.util.Debug.printException(e.getExcepcion());
            }
  %>
      VectorGlobalEntidades=unescapeArray(<%=arrayJScript%>);
      }
      
      function CargaComboAplicacions() {
          var cods=new Array();
          var descs=new Array();
          var par;
          
          for (var i=0;i<VectorGlobalAplicacions.length;i++) {
              par=VectorGlobalAplicacions[i];
              cods[i]=par[0];
              descs[i]=capitalize(par[1]);
          }
          ncomboAplicacion.addItems(cods,descs);
          
      }
      
      Inicio();
        </script>
    </body>
    </html:html>
    
