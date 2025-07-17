<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="es.altia.agora.business.geninformes.utils.ConstantesXerador"%>

<%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 3;
            String[] params = null;
            String css = "";

            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
                apl = usuarioVO.getAppCod();
                params = usuarioVO.getParamsCon();
                css = usuarioVO.getCss();
            }
            String arrayJScript = null;
            String estado = ((request.getParameter("op") != null) && (request.getParameter("op").equals("A"))) ? "ENGADIR" : ((request.getParameter("op").equals("M")) ? "MODIFICAR" : "CONSULTA");
            String codSubEntidad = request.getParameter("id");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<html>
<head>
<title>Campos de join</title>
<jsp:include page="/jsp/informes/tpls/app-constants.jsp" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/utilidadesXerador.js'/>"></script>

<script type="text/javascript">
            var tp1;
            var tp1_p1;
            var tp1_p2;
            var tp1_p3;
            var tp1_p4;
            var tp1_p5;
            var tp1_p6;
        </script>
    </head>
    
    
    
    
    <body  class="bandaBody" onLoad="getObjetoPorId('hidepage').style.visibility='hidden';"> <!-- onLoad="inicializar();" --><!----> <!-- pleaseWait('off');-->



        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        <table  width="795px" height="220px" cellpadding="0px" cellspacing="1px" border="0px" bgcolor="#ffffff">
            <tr> 
                <td id="titulo" class="txttitblanco">Campos de join </td>
            </tr>
            <tr>
                <td class="contenidoPantalla">
                    
                    <form method="POST" name="formCamposJoin" id="formCamposJoin">
                    
                    
                    <table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td class="etiqueta"><%= descriptor.getDescripcion("gEtiq_CJENTPai")%></td>
                            <td>&nbsp;&nbsp;<input type="text" class="inputTexto" disabled name="textNomeEntidadePai" id="textNomeEntidadePai" size="30"></td>
                        </tr>
                        <tr><td class="etiqueta"><%= descriptor.getDescripcion("gEtiq_CJSUBENT")%></td>
                            <td>&nbsp;&nbsp;<input type="text" class="inputTexto" disabled name="textNomeSubentidade" id="textNomeSubentidade" size="30"></td>
                        </tr>
                        
                    </table>
                </td>
            </tr>
            <tr><td>
            <table border=0>
            <tr>
                <td colspan="3">
                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
                        <tr>
                        <td rowspan="2" id="tablaCJ" width="80%" height="85%" align="center" valign="bottom"></td>
                        <td valign="bottom" >&nbsp;&nbsp;<input type="button" class="botonGeneral" name="BotonMais" id="BotonMais" value="+" style="width:25;" onclick="javascript:PulsaBoton(event);"></td>
                        <tr><td valign="middle">&nbsp;&nbsp;<input type="button" class="botonGeneral" name="BotonMenos" id="BotonMenos" value="-" style="width:25;" onclick="javascript:PulsaBoton(event);"></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <!-- Aquí el combo de entidades -->
                <td style="text-align:right;">
                    
                    <input type="text" style="visibility:hidden;height:0;width:0;" id="codCampoEnt" name="codCampoEnt"> 
                    <input class="inputTexto" type="text" id="descCampoEnt" name="descCampoEnt" size=20>
                    <a href="" id="anchorCampoEnt" name="anchorCampoEnt"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampoEnt" name="botonCampoEnt" style="cursor:hand;"></span></a>
                  </td>
                  <td style="text-align:center;">
                    <input type="text" style="visibility:hidden;height:0;width:0;" id="codCampoSubent" name="codCampoSubent"> 
                    <input class="inputTexto" type="text" id="descCampoSubent" name="descCampoSubent" size=20>
                    <a href="" id="anchorCampoSubent" name="anchorCampoSubent">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampoSubent" name="botonCampoSubent" style="cursor:hand;"></span></a>
                  </td>
                  <td style="text-align:right;widht:50%">
                    <input type="text" style="visibility:hidden;height:0;width:0;" id="codOuterJoin" name="codOuterJoin"> 
                    <input class="inputTexto" type="text" id="descOuterJoin" name="descOuterJoin" size=15 >
                    <a href="" id="anchorOuterJoin" name="anchorOuterJoin">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonOuterJoin" name="botonOuterJoin" style="cursor:hand;"></span></a>
                    <input type="button" class="botonGeneral" name="BotonValidar" id="BotonValidar" value="Validar" onclick="javascript:PulsaBoton(event);">
                </td>
            </tr>
                        
                    </table>
               </td></tr>     
        </TABLE>
      

        <table style="width:798px;valign:top;align:right;">
            <tr> 
                <td align="right"> 
                    <input class="botonGeneral" name="BotonAceptar" type="button" value="Aceptar" accesskey="A" 
                           onClick="PulsaBoton(event)"> 
                    <input name="BotonCancelar" type="button" class="botonGeneral" id="BotonCancelar" accesskey="C" 
                       onClick="PulsaBoton(event)" value="Cancelar"> </td>
            </tr>
        </table>
        
       </form>   
        
    </body>
    
    <script type='text/javascript'>
        var mensaxeIncorrectoInforme='Por favor, introduza alomenos o nome do informe e un campo para o informe.';
        
        //
        // Desde principio
        //
        var URLBaseAction="<c:url value='/XeradorInformesAction.do?operacion='/>";
        
        //
        // Código nuevo a partir de aquí.
        //
        var Estado = '<%=estado%>';
        
        //
        // Objetos Javascript represenatando a los campos HTML
        //
        var formulario=document.all.formCamposJoin;
        var textOperacion=formulario.textOperacion;
        
        var ncomboCampoEnt =new Combo('CampoEnt');
        var ncomboCampoSubent =new Combo('CampoSubent');
        var ncomboOuterJoin =new Combo('OuterJoin');
        
        var argVentana;
        var camposEntidadePai;
        var CJActual;
        var camposSubentidade;
        
        var textNomeEntidadePai=formulario.textNomeEntidadePai;
        var textNomeSubentidade=formulario.textNomeSubentidade;
        
        var estaAnadiendo=false;
        
        var tabCJ = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaCJ'));
        
        tabCJ.addColumna('250','center','<%= descriptor.getDescripcion("gEtiq_CJCENT")%>');
            tabCJ.addColumna('250','center','<%= descriptor.getDescripcion("gEtiq_CJCSubENT")%>');
                tabCJ.addColumna('230','center','Outer Join');
                tabCJ.displayCabecera=true;  
                tabCJ.displayTabla();
                tabCJ.displayDatos=EventoCambioTabla;
                
                function InicializaPestana()
                {
                    
                    argVentana = self.parent.opener.xanelaAuxiliarArgs;
                    
                    camposEntidadePai=argVentana[3];
                    CJActual=argVentana[2];
                    textNomeEntidadePai.value=argVentana[0];
                    textNomeSubentidade.value=argVentana[1];
                    
                    CargaVectorGlobalCamposPorEntidade();
                    
                    CargaComboCamposEntidade();
                    CargaComboCamposSubEntidade();
                    CargaComboTiposJoin();
                    CubreTablaCamposJoin();
                    
                    if ( (Estado=='CONSULTA')  )
                        {
                            EstableceEstadoConsulta();
                        } else if ((Estado=='MODIFICAR')) {
                        EstableceEstadoModificar();
                    } else {
                    EstableceEstadoEngadir();
                }
                
            }
            
            function CargaComboCamposEntidade()
            {
                var cods=new Array();
                var descs=new Array();
                // camposEntidadePAI: NOME,CAMPO, TIPO,LONXITUDE, NOMEAS
                if ( (camposEntidadePai) && (camposEntidadePai.length>0) ) {
                    for (var i=0;i<camposEntidadePai.length;i++) {
                        cods[i]=camposEntidadePai[i][4];
                        descs[i]=camposEntidadePai[i][0];
                    }
                    ncomboCampoEnt.addItems(cods,descs);
                }
            }
            
            function CargaComboCamposSubEntidade()
            {
                var cods=new Array();
                var descs=new Array();
                // camposSubentidade : COD_CAMPOINFORME, NOME, CAMPO, TIPO,LONXITUDE, NOMEAS 
                if ( (camposSubentidade) && (camposSubentidade.length>0) ) {
                    for (var i=0;i<camposSubentidade.length;i++) {
                        cods[i]=camposSubentidade[i][5];
                        descs[i]=camposSubentidade[i][1];
                    }
                    ncomboCampoSubent.addItems(cods,descs);
                }
            }
            
            function CargaComboTiposJoin()
            {
                ncomboOuterJoin.addItems(['L','R'],['Left Outer Join','Right Outer Join']);
            }
            
            
            function CubreTablaCamposJoin()
            {
                // CJActual [ [CAMPO_ENT,CAMPO_SUBENT,OUTER_JOIN]* ]
                var linea=new Array();
                
                if ( (CJActual) && (CJActual.length>0) ) {
                    for (var i=0;i<CJActual.length;i++) {
                        linea=new Array();
                        linea[0]=CJActual[i][0];
                        linea[1]=CJActual[i][1];
                        linea[2]=CJActual[i][2];
                        tabCJ.addLinea(linea);
                    }
                }
            }
            
            
            function devuelveArrayCJ()
            {
                var salida=new Array();
                
                for (var i=0;i<tabCJ.lineas.length;i++) {
                    salida[i]=tabCJ.getLinea(i);
                }
                return salida;
            }
            
            function PulsaBoton(evento)
            {
                var objeto;
                if (document.all) objeto = evento.srcElement;
                else objeto=evento.target;
                
                switch (objeto.name)
                {
                    
                    case 'BotonAceptar':
                    AccionAceptar();
                    break;
                    case 'BotonCancelar':
                    AccionCancelar();
                    break;
                    case 'BotonMais':
                    AccionMais();
                    break;
                    case 'BotonMenos':
                    AccionMenos();
                    break;
                    case 'BotonValidar':
                    AccionValidar();
                    break;
                    
                    default:break;
                }
            }
            
            
            function AccionMais()
            {
                tabCJ.readOnly=true;
                EditableControles(true);
                ncomboCampoEnt.selectItem(-1);
                ncomboCampoSubent.selectItem(-1);
                ncomboOuterJoin.selectItem(-1);
                estaAnadiendo=true;
            }
            
            function AccionMenos()
            {
                if (tabCJ.selectedIndex>-1) {
                    tabCJ.removeLinea(tabCJ.selectedIndex);
                }
            }
            
            function contains(tabCJ,linea) {
                var contiene = false;
                var i=0;
                var longitud = tabCJ.lineas.length;
                while ((i<longitud) && (!contiene)) {
                    if (tabCJ.lineas[i][0]==linea[0] && tabCJ.lineas[i][1]==linea[1]) {
                        contiene=true;
                    }
                    i++;
                }
                return contiene;
            }
            
            function AccionValidar()
            {
                
                var correcto=(ncomboCampoEnt.selectedIndex>-1) && (ncomboCampoSubent.selectedIndex>-1);
                if (correcto) {
                    var linea=new Array();
                    linea[0]=ncomboCampoEnt.cod.value;
                    linea[1]=ncomboCampoSubent.cod.value;
                    linea[2]=ncomboOuterJoin.cod.value;
                    
                    if (estaAnadiendo) {
                        if (!(contains(tabCJ,linea))) {
                            tabCJ.addLinea(linea);
                        } else {
                        jsp_alerta('A','Campos de entidad y subentidad ya añadidos.');
                    }
                } else {
                tabCJ.setLinea(linea);
            }
        } else jsp_alerta('A','Campo de entidad y de subentidad obligatorios.');
        
        tabCJ.readOnly=false;
        ncomboCampoEnt.selectItem(-1);
        ncomboCampoSubent.selectItem(-1);
        ncomboOuterJoin.selectItem(-1);
        EditableControles(false);
        estaAnadiendo=false;
    }
    
    function AccionAceptar()
    {
        self.parent.opener.retornoXanelaAuxiliar(devuelveArrayCJ());
    }
    
    function AccionCancelar() 
    {
        self.parent.opener.retornoXanelaAuxiliar();    
    }
    
    function AceptarConsulta()
    {
        self.parent.opener.retornoXanelaAuxiliar();
    }
    
    function EstableceEstadoConsulta()
    {
        Estado = 'CONSULTA';
        
        HabilitaMais(true);
        HabilitaMenos(true);
        HabilitaValidar(true);
        
        HabilitaAceptar(true);			
        HabilitaCancelar(true);
        
        EditableControles(false);
        
    }
    
    function EstableceEstadoEngadir()
    {
        Estado = 'ENGADIR';
        
        //InicializaControles();
        HabilitaMais(true);
        HabilitaMenos(true);
        HabilitaValidar(true);
        
        HabilitaAceptar(true);			
        HabilitaCancelar(true);	
        EditableControles(false);
        //ncomboCampoEnt.base.focus();
    }
    
    function EstableceEstadoModificar()
    {
        Estado = 'MODIFICAR';
        HabilitaAceptar(true);			
        HabilitaCancelar(true);	
        EditableControles(false);
    }
    
    function CargaVectorGlobalCamposPorEntidade()
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
                String cualApli = null;

                if (request.getParameter("op").equals("A")) {
                    cualApli = request.getParameter("id");
                } else {
                    cualApli = request.getParameter("codApliInforme");
                }

                tempTabla.put("PARAMS", params);//USUARIO.getCOD_PESTANA());
                tempTabla.put("COD_ENTIDADEINFORME", codSubEntidad);
                //es.altia.util.Debug.println("EN JSPIII MNUCOD ES:"+pestanaOrixe+".");
                arrayJScript = es.altia.agora.business.geninformes.utils.UtilidadesXerador.ObtenerArrayJSCamposInformeEntidad(tempTabla);
            } catch (Exception e) {
            //es.altia.util.Debug.printException(e.getExcepcion());
            }
%>
    camposSubentidade=unescapeArray(<%=arrayJScript%>);
        //alert('camposSubentidade es:'+camposSubentidade+'.');
    }
    
    function HabilitaMais(est)
    {
        var cond=!(est && true );
        getObjetoPorId('BotonMais').disabled=cond;
    }
    
    function HabilitaAceptar(est)
    {
        var cond=!(est && true );
        getObjetoPorId('BotonAceptar').disabled=cond;
    }
    
    function HabilitaCancelar(est)
    {
        var cond=!(est && true );
        getObjetoPorId('BotonCancelar').disabled=cond;
    }
    
    
    function HabilitaMenos(est)
    {
        var cond=!(est && true && (tabCJ.selectedIndex>-1) && (Estado!='CONSULTA'));
        getObjetoPorId('BotonMenos').disabled=cond;
    }
    
    function HabilitaValidar(est)
    {
        var cond=!(est && true  );
        getObjetoPorId('BotonValidar').disabled=cond;
    }
    
    function EditableControles(NuevoEstado)
    {
        var cond=!(NuevoEstado && true);
        var cond2=!(NuevoEstado && true && Estado=='ENGADIR');
        var que;
        
        if (NuevoEstado) que='activate();'; else que='deactivate();';
        eval('ncomboCampoEnt.'+que);
        eval('ncomboCampoSubent.'+que);
        eval('ncomboOuterJoin.'+que);
    }
    
    function EventoCambioTabla()
    {
        if (tabCJ.selectedIndex>-1) {
            HabilitaMenos(true);
        }
        CubreControles();
    }
    
    function CubreControles()
    {
        if (tabCJ.selectedIndex>-1) {
            var linea=tabCJ.getLinea(tabCJ.selectedIndex);
            ncomboCampoEnt.buscaCodigo(linea[0]);
            ncomboCampoSubent.buscaCodigo(linea[1]);
            ncomboOuterJoin.buscaCodigo(linea[2]);
        }
    }
    
    InicializaPestana();
    </script>
</html>



