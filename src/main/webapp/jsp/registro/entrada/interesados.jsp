<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.InteresadosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%
    UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int idioma = 1;
    int apl = 4;
    String css="";
    if (session.getAttribute("usuario") != null) {
        usuarioVO = (UsuarioValueObject) session
                .getAttribute("usuario");
        apl = usuarioVO.getAppCod();
        idioma = usuarioVO.getIdioma();
        css=usuarioVO.getCss();
    }
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
%>
<jsp:useBean id="descriptor" scope="request"
	class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
	type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor" property="idi_cod"
	value="<%=idioma%>" />
<jsp:setProperty name="descriptor" property="apl_cod" value="<%=apl%>" />
<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title>Interesados</title>
<jsp:include page="/jsp/plantillas/Metas.jsp"/>

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript"
	src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
  <LINK	REL="stylesheet" MEDIA="screen" type="text/css"	href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript"
	src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/scripts/listaBusquedaTerceros.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript">
    
        
    
    var vectorCamposRejilla = ['tercero','versionTercero','nombre','codRol','descRol','domicilio','descDomicilio','porDefecto','tlf','email','pais','provincia','municipio','cp','coddni','dni'];
        //var vectorCamposRejilla = ['nombre','codRol','descRol','codMunicipio','codProcedimiento','ejercicio','numero','tercero','versionTercero','domicilio','descDomicilio'];
        var listaTabla = new Array();
        var listaInteresados = new Array();
        var lista = new Array();
        var expediente = new Array();
        var datosRejilla = new Array();
        var codRols = new Array();
        var descRols = new Array();
        var pDef = new Array();
       var listaTabla = new Array();
   


       
        // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function inicializar(){

            expediente = self.parent.opener.xanelaAuxiliarArgs;
            listaInteresados= new Array();
           
            // Comprobamos si estamos en modificacion de un asiento para
            // bloquear los botones de eliminar, modificar y alta.
         
            modificando=expediente[6];
            if (modificando=='S') {
                document.getElementById('capaBotonesConsulta').style.display='none';
                document.getElementById('capaBotonesModificando').style.display=''; 
                document.getElementById('capaModificar').style.visibility='visible'; 
            } else {
                document.getElementById('capaBotonesConsulta').style.display='';
                document.getElementById('capaBotonesModificando').style.display='none';
                document.getElementById('capaModificar').style.visibility='hidden';  
            }
            
       <%  Vector listaRoles = new Vector();
            InteresadosForm intForm =(InteresadosForm)session.getAttribute("InteresadosForm");
            listaRoles = (Vector) intForm.getListaRoles();
            int lengthRoles = listaRoles.size();%>
        var m=0;
        rolPorDefecto=0;
        <%  for(int t=0;t<lengthRoles;t++){%>
                codRols[m] = [	'<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("codRol")%>'];
                descRols[m] = ['<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("descRol")%>'];
                pDef[m] = ['<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("porDefecto")%>'];
                if (pDef[m]=='SI') rolPorDefecto=codRols[m];
                m++;
        <%  }%>
                
        if(expediente[5]!=undefined){
         // En registro, los interesados estan en un vector javascript
	        listaInteresados = expediente[5];
        }else{
         // En consulta de asiento en expedientes, se recuperan de BD
         <% 
           Vector listaInteresados = new Vector();
           InteresadosForm intForm1 =(InteresadosForm)session.getAttribute("InteresadosForm");
           listaInteresados = (Vector)intForm1.getListaInteresados();
           int lengthInteresados = listaInteresados.size();
           int i = 0;
         %>
         var j=0;
        <% for(i=0;i<lengthInteresados;i++) { %>
          listaInteresados[j] = ['<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("codigoTercero")%>',
                        '<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("versionTercero")%>',
                        '<%=StringEscapeUtils.escapeJavaScript((String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("titular"))%>',
                        '<%=StringEscapeUtils.escapeJavaScript((String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("descRol"))%>',
                        '<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("domicilio")%>',
                        '<%=StringEscapeUtils.escapeJavaScript((String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("descDomicilio"))%>',
                        '<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("porDefecto")%>',
                        '<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("rol")%>',];
          j++;
        <% } %>
        }
        
         for (var j=0; j<listaInteresados.length; j++) {
	                    listaTabla[j] = [listaInteresados[j][2],listaInteresados[j][5],listaInteresados[j][3]];
	            }
        tablaInteresados.lineas = listaTabla;
        tablaInteresados.displayTabla();
        comboRol.addItems(codRols,descRols);
        //saco el rol por defecto para mostrarlo siempre que entre
            cod="";
            desc="";
            for(i=0;i<codRols.length;i++){
            	if(pDef[i]=="SI"){
            		cod=codRols[i];
            		desc=descRols[i];
            	}
            }
            document.forms[0].descRol.value=desc;
            document.forms[0].codRol.value=cod;
        
     }
        // FUNCIONES DE LIMPIEZA DE CAMPOS
        function limpiarFormulario(){
            tablaInteresados.lineas = new Array();
            tablaInteresados.displayTabla();
            limpiar(vectorCamposRejilla);
        }

        function limpiarCamposRejilla(){
            limpiar(vectorCamposRejilla);
        }

        // FUNCIONES DE PULSACION DE BOTONES
	//expediente viene de altaRE

        function grabarInteresados() {
        
          pulsarSalir2();
        }

        function pulsarSalir() {

            var listas = crearListas();
            var listaCodTercero = listas[0];

            var listaVersionTercero = listas[1];
            var listaCodDomicilios = listas[2];
            var listaRol = listas[3];
            retorno = [listaCodTercero,listaVersionTercero,listaCodDomicilios,listaRol,listaInteresados];
            if (modificando == 'S') {                
                self.parent.opener.retornoXanelaAuxiliar(retorno);
            }else
                self.parent.opener.retornoXanelaAuxiliar();
        }
        
function pulsarEliminar() {
    if(tablaInteresados.selectedIndex != -1) {
        if(jsp_alerta('', '<%=descriptor.getDescripcion("msjBorrarInt")%>')) {
            var list = new Array();
            var listOriginal = new Array();
            tamIndex=tablaInteresados.selectedIndex;
            tamLength=tablaInteresados.lineas.length;

            for (i=0; i<tamLength-1; i++){
                if(i<tamIndex){
                    list[i] = listaTabla[i];
                    listOriginal[i] = listaInteresados[i];
                }else{
                    list[i]=listaTabla[i+1];
                    listOriginal[i]=listaInteresados[i+1];
                }
            }
            listaTabla=list;
            listaInteresados = listOriginal;

            tablaInteresados.lineas=list;
            tablaInteresados.displayTabla();
            limpiar(vectorCamposRejilla);

        } else {
            tablaInteresados.selectLinea(tablaInteresados.selectedIndex);
            tablaInteresados.selectedIndex = -1;
            limpiar(vectorCamposRejilla);

        }
    } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }
    
   function pulsarModificar() {
        var domic = document.forms[0].domicilio.value;
        var yaExiste = 0;
        if(tablaInteresados.selectedIndex != -1) {
          if (validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
            // Miramos que el tercero no exista ya en la tabla
            for(var l=0; l < listaTabla.length; l++){
              if(l != tablaInteresados.selectedIndex) {
                if ((listaTabla[l][1]) == domic) yaExiste = 1;
              }
            }

            if(yaExiste == 0) {         
                 listaTabla[tablaInteresados.selectedIndex]=
                    [document.forms[0].nombre.value,document.forms[0].descDomicilio.value,document.forms[0].descRol.value];
                 var pD = "";
                 for(var u=0;u<codRols.length;u++) {
                     if(document.forms[0].codRol.value == codRols[u][0]) pD = pDef[u][0];
                 }
                 listaInteresados[tablaInteresados.selectedIndex] = 
                    [document.forms[0].tercero.value,document.forms[0].versionTercero.value,
                     document.forms[0].nombre.value,document.forms[0].descRol.value,
                     document.forms[0].domicilio.value,document.forms[0].descDomicilio.value,
                     pD,document.forms[0].codRol.value, document.forms[0].tlf.value,
                     document.forms[0].email.value,document.forms[0].pais.value, 
                     document.forms[0].provincia.value,document.forms[0].municipio.value,
                     document.forms[0].cp.value,document.forms[0].coddni.value,document.forms[0].dni.value];

                 tablaInteresados.lineas=listaTabla;
                 tablaInteresados.displayTabla();
                 limpiar(vectorCamposRejilla);
            }else{
               jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
            }
          }
        }else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
   }

        function pulsarAlta() {
            if(validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
                if(noEsta()){
                    listaTabla[listaTabla.length]=
                        [document.forms[0].nombre.value,document.forms[0].descDomicilio.value,document.forms[0].descRol.value];                   

                    var pD = "";
                    for(var u=0; u<codRols.length; u++) {
                        if(document.forms[0].codRol.value == codRols[u][0]) pD = pDef[u][0];
                    }    

                    listaInteresados[listaInteresados.length] = 
                        [document.forms[0].tercero.value,document.forms[0].versionTercero.value,
                         document.forms[0].nombre.value,document.forms[0].descRol.value,
                         document.forms[0].domicilio.value,document.forms[0].descDomicilio.value,
                         pD,document.forms[0].codRol.value, document.forms[0].tlf.value,document.forms[0].email.value,
                         document.forms[0].pais.value, document.forms[0].provincia.value,document.forms[0].municipio.value,
                         document.forms[0].cp.value,document.forms[0].coddni.value,document.forms[0].dni.value];

                    tablaInteresados.lineas=listaTabla;
                    tablaInteresados.displayTabla();
                    limpiar(vectorCamposRejilla);
                }else{
                    jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
                }
            } 
        }
        
        function crearListas() {
            var listaCodTercero = "";
            var listaVersionTercero = "";
            var listaCodDomicilios= "";
            var listaRol = "";
            var listaPDefecto = "";
            var listaTlf="";
            var listaEmail="";
            var listaPais="";
            var listaProvincia="";
            var listaMunicipio="";
            for (i=0; i < listaTabla.length; i++) {
                listaCodTercero += listaInteresados[i][0]+'зе';
                listaVersionTercero += listaInteresados[i][1]+'зе';
                listaRol += listaInteresados[i][7]+ 'зе';
                listaCodDomicilios += listaInteresados[i][4]+'зе';
                listaPDefecto += listaInteresados[i][6]+'зе';
                listaTlf += listaInteresados[i][8]+'зе';
                listaEmail += listaInteresados[i][9]+'зе';
                listaPais += listaInteresados[i][10]+'зе';
                listaProvincia += listaInteresados[i][11]+'зе';
                listaMunicipio += listaInteresados[i][12]+'зе';
            }
            var listas = new Array();
            listas = [listaCodTercero,listaVersionTercero,listaCodDomicilios,listaRol,listaPDefecto,listaTlf,listaEmail,listaPais,listaProvincia,listaMunicipio,listaInteresados];
            return listas;
        }

        // FUNCIONES DE VALIDACION Y COMPROBACION DEL FORMULARIO
        function noEsta(){
            var cod = document.forms[0].tercero.value;
            var ver = document.forms[0].versionTercero.value;
            for(i=0;(i<listaInteresados.length);i++){
                    if((listaInteresados[i][0] == cod) && (listaInteresados[i][1] == ver))
                        return false;
            }
            return true;
        }

        function filaSeleccionada(tabla){
            var i = tabla.selectedIndex;
            if((i>=0)&&(!tabla.ultimoTable))
                return true;
            return false;
        }

        function validarCamposRejilla(){
            if((document.forms[0].nombre.value!="")&&(document.forms[0].descRol.value!=""))
                return true;
            return false;
        }

        function pulsarBuscarTerc() {
            var argumentos = new Array();
            argumentos =[new Array(),""];
            var source = "<c:url value='/BusquedaTerceros.do?opcion=inicializarBusquedaTerc&ventana=true&preguntaAlta=si'/>";
     
            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,argumentos,
	'width=998,height=700,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                            document.forms[0].tercero.value = datos[2];
                            document.forms[0].versionTercero.value = datos[3];
                            document.forms[0].nombre.value = datos[6];
                            document.forms[0].domicilio.value = datos[14];
                            document.forms[0].tlf.value = datos[11];
                            document.forms[0].email.value = datos[12];
                            document.forms[0].pais.value = datos[15];
                            document.forms[0].provincia.value = datos[16];
                            document.forms[0].municipio.value = datos[17];
                            document.forms[0].descDomicilio.value = datos[18];
                            document.forms[0].cp.value = datos[19];
                            document.forms[0].coddni.value = datos[4];
                            document.forms[0].dni.value = datos[5];
                        }
                  });
        }

        function abrirTerceros() {
            var codTercero = document.forms[0].tercero.value;
             var source;
            if ((codTercero!=null) && (codTercero!='')) {
              source = "<c:url value='/BusquedaTerceros.do'/>" + '?opcion=inicializar&destino=registroAlta&tipo=B&codTerc='+codTercero;
            }else {
                source = "<c:url value='/BusquedaTerceros.do?opcion=inicializar&destino=registroAlta&tipo=NoAlta&descDoc=&docu='/>";
            }
            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,'terceros',
	'width=998,height=530,status='+ '<%=statusBar%>',function(datos){
                        // Comprobacion de que no se ha dejado un tercero sin domicilio
                        if (datos!=null && datos[0]!=null && datos[0][0] == "terceroSinDomicilio") 
                               terceroSinDomicilio(datos[0][1],"<%=descriptor.getDescripcion("msjNoGrabDomTercExt")%>",
                                        '<%=request.getContextPath()%>',function (datos2){
                                                // Otra comprobacion, si se ha eliminado un tercero, no intentar cargar los datos devueltos                  
                                                if(datos2!=undefined){
                                                    if (datos2[0][0] != "eliminarTercero") {                              
                                                            document.forms[0].tercero.value = datos2[2];
                                                            document.forms[0].versionTercero.value = datos2[3];
                                                            document.forms[0].nombre.value = datos2[6];
                                                            document.forms[0].domicilio.value = datos2[14];
                                                            document.forms[0].tlf.value = datos2[11];
                                                            document.forms[0].email.value = datos2[12];
                                                            document.forms[0].pais.value = datos2[15];
                                                            document.forms[0].provincia.value = datos2[16];
                                                            document.forms[0].municipio.value = datos2[17];
                                                            document.forms[0].descDomicilio.value = datos2[18];
                                                            document.forms[0].cp.value = datos2[19];
                                                            document.forms[0].coddni.value = datos2[4];
                                                            document.forms[0].dni.value = datos2[5];
                                                    }
                                                }
                                                 var pD = "";
                                                  for(var u=0;u<codRols.length;u++) {
                                                      if(document.forms[0].codRol.value == codRols[u][0]) pD = pDef[u][0];
                                                  }
                                                 listaInteresados[tablaInteresados.selectedIndex] = [document.forms[0].tercero.value,document.forms[0].versionTercero.value,
                                                              document.forms[0].nombre.value,document.forms[0].descRol.value,
                                                              document.forms[0].domicilio.value,document.forms[0].descDomicilio.value,
                                                              pD,document.forms[0].codRol.value,document.forms[0].tlf.value,document.forms[0].email.value,
                                                              document.forms[0].pais.value, document.forms[0].provincia.value,document.forms[0].municipio.value,
                                                              document.forms[0].cp.value,document.forms[0].coddni.value,document.forms[0].dni.value];
                                        });
                        else{
                          // Otra comprobacion, si se ha eliminado un tercero, no intentar cargar los datos devueltos                  
                          if(datos!=undefined){
                              if (datos[0][0] != "eliminarTercero") {                              
                                      document.forms[0].tercero.value = datos[2];
                                      document.forms[0].versionTercero.value = datos[3];
                                      document.forms[0].nombre.value = datos[6];
                                      document.forms[0].domicilio.value = datos[14];
                                      document.forms[0].tlf.value = datos[11];
                                      document.forms[0].email.value = datos[12];
                                      document.forms[0].pais.value = datos[15];
                                      document.forms[0].provincia.value = datos[16];
                                      document.forms[0].municipio.value = datos[17];
                                      document.forms[0].descDomicilio.value = datos[18];
                                      document.forms[0].cp.value = datos[19];
                                      document.forms[0].coddni.value = datos[4];
                                      document.forms[0].dni.value = datos[5];
                              }
                          }
                           var pD = "";
                            for(var u=0;u<codRols.length;u++) {
                                if(document.forms[0].codRol.value == codRols[u][0]) pD = pDef[u][0];
                            }
                           listaInteresados[tablaInteresados.selectedIndex] = [document.forms[0].tercero.value,document.forms[0].versionTercero.value,
                                        document.forms[0].nombre.value,document.forms[0].descRol.value,
                                        document.forms[0].domicilio.value,document.forms[0].descDomicilio.value,
                                        pD,document.forms[0].codRol.value,document.forms[0].tlf.value,document.forms[0].email.value,
                                        document.forms[0].pais.value, document.forms[0].provincia.value,document.forms[0].municipio.value,
                                        document.forms[0].cp.value,document.forms[0].coddni.value,document.forms[0].dni.value];
                        }
                    });          
        }

        function nombreCompleto(nom, pa1, ap1, pa2, ap2){
            var titular = "";
            if(""!=pa1) titular += pa1 + " ";
            else if(""!=ap1) titular += ap1 + " ";
            else if(""!=pa2) titular += pa2 + " ";
            else if(""!=ap2) titular += ap2;
            else if(""!=nom) titular += ", " +nom;
            return titular;
        }

        function pulsarVer() {
          if(tablaInteresados.selectedIndex != -1) {
              var i = tablaInteresados.selectedIndex;
              var codTerc = listaInteresados[i][0];
              var versTerc = listaInteresados[i][1];
              var codDom = listaInteresados[i][4];
              var source = "<c:url value='/BusquedaTerceros.do?opcion=verTercero&nCS='/>"+codTerc+"&codMun="+versTerc+"&codProc="+codDom;
              abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,null,
	'width=785,height=400,status='+ '<%=statusBar%>',function(datos){});
          } else {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
          }
        }
    </script>
</head>

<body class="bandaBody" onload="inicializar();">
<form name="formulario" method="post" action=""><input
	type="hidden" name="opcion" id="opcion"> <input type="hidden"
	name="codMunicipio" id="codMunicipio"> <input type="hidden"
	name="codProcedimiento" id="codProcedimiento"> <input
	type="hidden" name="ejercicio" id="ejercicio"> <input
	type="hidden" name="numero" id="numero"> <input type="hidden"
	name="tercero" id="tercero"> <input type="hidden"
	name="versionTercero" id="versionTercero"> <input type="hidden"
	name="domicilio" id="domicilio"> <input type="hidden"
	name="codOur" id="codOur"> <input type="hidden" name="codTip"
	id="codTip"> <input type="hidden" name="codDep" id="codDep">
<input type="hidden" name="listaCodTercero" value=""> <input
	type="hidden" name="listaVersionTercero" value=""> <input
	type="hidden" name="listaCodDomicilio" value=""> <input
	type="hidden" name="listaRol" value=""> <input type="hidden"
	name="tlf" id="tlf"> <input type="hidden" name="email"
	id="email"> <input type="hidden" name="pais" id="pais">
<input type="hidden" name="provincia" id="provincia"> <input
	type="hidden" name="municipio" id="municipio"> <input
	type="hidden" name="cp" id="cp"> <input type="hidden"
	name="coddni" id="coddni"> <input type="hidden" name="dni"
	id="dni"> <input type="hidden" name="porDefecto"
	id="porDefecto">

<div id="titulo" class="txttitblanco">Lista de Interesados <!-- <%= descriptor.getDescripcion("tit_modOperaciones")%> --></div>
<div class="contenidoPantalla" valign="top">
<table>
    <tr>
            <td align="center" id="tabla"></td>
    </tr>
    <tr>
        <td>
            <div id="capaModificar">  
                    <table width="100%" cellpadding="1px">
                            <tr>
                                    <td width="315px" align="center"><input type="text"
                                            class="inputTextoObligatorio" id="nombre" name="nombre"
                                            style="width: 265px" readonly><span class="fa fa-search" 
                                            name="botonT" alt = "<%=descriptor.getDescripcion("altBuscInt")%>" title = "<%=descriptor.getDescripcion("altBuscInt")%>" 
                                            style="cursor: pointer;"
                                            onclick="javascript:pulsarBuscarTerc();"></span><span class="fa fa-user" 
                                            name="botonTer" alt="<%=descriptor.getDescripcion("altMantTerc")%>" 
                                            title="<%=descriptor.getDescripcion("altMantTerc")%>" style="cursor: pointer;"
                                            onclick="javascript:abrirTerceros();"></span></td>
                                    <td width="290px" align="center"><input type="text"
                                            class="inputTextoObligatorio" id="descDomicilio"
                                            name="descDomicilio" style="width: 255px" readonly></td>
                                    <td width="150px" align="center"><input type="text"
                                            class="inputTextoObligatorio" id="codRol" name="codRol"
                                            style="width: 15px" maxlength="2"
                                            onKeyPress="javascript:return SoloDigitos(event);"> <input
                                            type="text" class="inputTextoObligatorio" id="descRol"
                                            name="descRol" style="width: 110px" readOnly="true">&nbsp;<a
                                            href="" id="anchorRol" name="anchorRol"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonRol" name="botonRol" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor: pointer;"></span></a></td>
                            </tr>
                    </table>
                 </div>
                </td>
        </tr>
        <tr>
            <td height="20px"></td>
        </tr>
    </table>
<!-- Botones CONSULTANDO:
         1. Ver.         
         6. Salir.
    -->
    <div id="capaBotonesConsulta" name="capaBotonesConsulta" style="display:none" class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbVer")%>' name="cmdVer" onClick="pulsarVer();" accesskey="V">
        <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbSalir")%>' name="cmdSalir" onClick="pulsarSalir();" accesskey="S">
    </div>

<!-- Botones MODIFICANDO:
         1. Ver.
         2. Alta.
         3. Modificar.
         4. Eliminar.
         5. Limpiar.
         6. Salir.
    -->
  <div id="capaBotonesModificando" name="capaBotonesModificando" style="display:none" class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbVer")%>' name="cmdVer" onClick="pulsarVer();" accesskey="V">
        <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbAlta")%>' name="cmdAlta" onClick="pulsarAlta();" accesskey="A">
        <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbModificar")%>' name="cmdModificar" onClick="pulsarModificar();" accesskey="M">
        <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbEliminar")%>' name="cmdEliminar" onClick="pulsarEliminar();" accesskey="E">
        <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbLimpiar")%>' name="cmdLimpiar" onClick="limpiar(vectorCamposRejilla);" accesskey="L">
        <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbSalir")%>' name="cmdSalir" onClick="pulsarSalir();" accesskey="S">
    </div>
</div>
</form>

<script language="JavaScript1.2" type="text/javascript">
    var comboRol = new Combo("Rol");
    var tablaInteresados = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

    tablaInteresados.addColumna('320','center','<%=descriptor.getDescripcion("gEtiqNombre")%>');
    tablaInteresados.addColumna('320','center','Domicilio');
    tablaInteresados.addColumna('131','center','Rol');
    tablaInteresados.displayCabecera=true;

    function rellenarDatos(tableName,rowID){
            var i = rowID;
            if((i>=0)&&!tableName.ultimoTable){

                      datosRejilla = [listaInteresados[i][0],listaInteresados[i][1],listaInteresados[i][2],listaInteresados[i][7],listaInteresados[i][3],listaInteresados[i][4],listaInteresados[i][5],listaInteresados[i][6],listaInteresados[i][8],listaInteresados[i][9],listaInteresados[i][10],listaInteresados[i][11],listaInteresados[i][12],listaInteresados[i][13],listaInteresados[i][14],listaInteresados[i][15]];
                      rellenar(datosRejilla,vectorCamposRejilla);

            }else limpiar(vectorCamposRejilla);
    }

    document.onmouseup = checkKeys;
    function checkKeysLocal(evento,tecla){
       var teclaAuxiliar = "";
        if(window.event){
            evento = window.event;
            teclaAuxiliar = evento.keyCode;
        }else
            teclaAuxiliar = evento.which;

		keyDel(evento);
		if (teclaAuxiliar == 38 || teclaAuxiliar == 40) upDownTable(tablaInteresados,listaInteresados,teclaAuxiliar);
    }
</script>
</body>
</html>

