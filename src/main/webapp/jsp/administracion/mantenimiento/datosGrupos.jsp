<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.UsuariosGruposForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<html:html>

    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        
        <TITLE>::: ADMINISTRACION  Datos de Grupos:::</TITLE>
        
      
        <%
            int idioma = 1;
            int apl = 1;
            int munic = 0;
            String css="";
            if (session != null) {
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    apl = usuario.getAppCod();
                    munic = usuario.getOrgCod();
                    css=usuario.getCss();
                }
            }
        %>
        
        
          <!-- Estilos -->

        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
     
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <SCRIPT type="text/javascript" >
            var listaGrupos = new Array();
            var listaGruposOriginal = new Array();
            var cont = 0;
            var listaGruposTodos = new Array();
            var nombreOriginal = "";
            <% Vector relacionGrupos = (Vector) session.getAttribute("RelacionGrupos");
            if (relacionGrupos != null) {
                int j = 0;
                for (int i = 0; i < relacionGrupos.size(); i++) {
                    GeneralValueObject g = (GeneralValueObject) relacionGrupos.elementAt(i);
                    String nG = (String) g.getAtributo("nombreGrupo");
%>
    listaGruposTodos[<%= j++ %>]  = ['<%= nG %>'];
        <% 	   }
            }
%> 
    
    
    <% String modo = "";
            modo = (String) session.getAttribute("modo");
            session.removeAttribute("modo");
            Log m_log = LogFactory.getLog(this.getClass().getName());
            if (m_log.isDebugEnabled()) {
                m_log.debug("El modo es: " + modo);
            }
%>
    function inicializar() {
        var argVentana = self.parent.opener.xanelaAuxiliarArgs;
        if(argVentana != null ) {
            nombreOriginal = argVentana[0];
        }
        <logic:iterate id="elemento" name="UsuariosGruposForm" property="listaUsuariosGrupos">
        listaGrupos[cont] = ['<bean:write name="elemento" property="codUsuario" />',
            '<bean:write name="elemento" property="nombreUsuario" />',
            '<bean:write name="elemento" property="codAplicacion" />',
            '<bean:write name="elemento" property="nombreAplicacion" />',
            '<bean:write name="elemento" property="codEntidad" />',
            '<bean:write name="elemento" property="nombreEntidad" />'];
            cont++;
            </logic:iterate>
            domlay('tblUsuarios',1,0,0,cargaTabla());
        }
        
        function pulsarSalir() {
            self.parent.opener.retornoXanelaAuxiliar();
        }
        
        function TablaUsuarios(lineas){
            this.lineas = lineas;
            this.usuarios=new Array();
            this.codUsuarios = new Array();	
            
            this.cargaLineas=function(){
                for(var i=0;i<lineas.length;i++){
                    var lin = lineas[i];
                    var usuario = this.codUsuarios[lin[0]];
                    if(!usuario){
                        usuario = new Usuario(lin[1]);
                        this.codUsuarios[lin[0]] = usuario;
                        this.usuarios[this.usuarios.length] = usuario;
                        
                    }
                    var aplicacion = usuario.codAplicaciones[lin[2]];
                    if(!aplicacion){
                        aplicacion = new Aplicacion(lin[3]);
                        usuario.codAplicaciones[lin[2]] = aplicacion;
                        usuario.aplicaciones[usuario.aplicaciones.length] = aplicacion;
                    }
                    aplicacion.codEntidades[lin[4]] = lin[5];
                    aplicacion.entidades[aplicacion.entidades.length] = lin[5];
                }	
            }
            this.calculaTabla=function(){
                var str='';
                for(var i=0;i<this.usuarios.length;i++){
                    str += this.usuarios[i].pintaUsuario();
                }
                return str;
            }
            this.cargaLineas();
        }
        
        function Usuario(nom){
            this.nombre=nom;
            this.codAplicaciones=new Array();
            this.aplicaciones=new Array();
            this.getRowspan=function(){
                var row = 1;
                for(var i=0;i<this.aplicaciones.length;i++){
                    row = row+this.aplicaciones[i].getRowspan();
                }
                return row;
            }
            this.pintaUsuario=function(){
                var str =  '<tr><td rowspan="'+this.getRowspan()+'">'+this.nombre+'</td>';
                str += '<tr>'+this.aplicaciones[0].pintaAplicacion()+'</tr>';
                for(var i=1;i<this.aplicaciones.length;i++){
                    str+='<tr>'+this.aplicaciones[i].pintaAplicacion()+'</tr>';
                }
                return str;
            }
        }
        function Aplicacion(nom){
            this.nombre=nom;
            this.codEntidades=new Array();
            this.entidades=new Array();
            this.getRowspan=function(){
                return this.entidades.length;
            }
            this.pintaAplicacion=function(){
                var str = '<td rowspan="'+this.getRowspan()+'">'+this.nombre+'</td>';
                str+= '<td>'+this.entidades[0]+'</td>';
                for(var i=1;i<this.entidades.length;i++){
                    str+='</tr><tr><td>'+this.entidades[i]+'</td>';
                }
                return str;
            }
        }
        
        function cargaTabla() {
            var htmlString = "";
            var longLista = listaGrupos.length;
            htmlString += '<table width="100%" class="xTabla" cellpadding="3" cellspacing="0" align="center" >';
            htmlString += '<tr><th width="34%" align="center" class="xCabecera"><%=descriptor.getDescripcion("columUsuarios")%></th><th width="33%" align="center" class="xCabecera"><%=descriptor.getDescripcion("columAplicaciones")%></th><th width="33%" align="center" class="xCabecera"><%=descriptor.getDescripcion("columEntidades")%></th>';
            htmlString += new TablaUsuarios(listaGrupos).calculaTabla();
            htmlString += '</table>';
            return htmlString;
        }
        
        
        function pulsarAceptar () {
            var desc = document.forms[0].nombreGrupo.value;
            var yaExiste = 0;
            for(l=0; l < listaGruposTodos.length; l++){
                if(nombreOriginal != "") {
                    if ((listaGruposTodos[l][0]) == desc && nombreOriginal != desc){
                        yaExiste = 1;
                    }
                } else {
                if ((listaGruposTodos[l][0]) == desc){
                    yaExiste = 1;
                }
            }
        }
        if( validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
            <% if ("alta".equals(modo)) {%>
            if(yaExiste == 0 ) {
                document.forms[0].opcion.value="insertarGrupo";
                document.forms[0].target="oculto";
                document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
                document.forms[0].submit();
            } else {
            jsp_alerta('A','<%=descriptor.getDescripcion("msjGrupoExist")%>');
            }
            <%} else {%>
            if(yaExiste == 0 ) {
                document.forms[0].opcion.value="modificarGrupo";
                document.forms[0].target="oculto";
                document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
                document.forms[0].submit();
            } else {
            jsp_alerta('A','<%=descriptor.getDescripcion("msjGrupoExist")%>');
            }
            <% }%>
        }
    }
    
    function devolver () {
        var retorno = new Array();
        self.parent.opener.retornoXanelaAuxiliar(retorno);
    }
    
    
        </SCRIPT>
        
    </head>
    
    <body class="bandaBody" onload="javascript:{ pleaseWait('off'); 
        inicializar()}">
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
<html:form action="/administracion/UsuariosGrupos.do" target="_self">

    <html:hidden  property="opcion" value=""/>
    <html:hidden  property="codGrupo"/>

    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("tit_datGrp")%></div>
    <div class="contenidoPantalla">
        <table id ="tablaDatosGral" >
            <tr>
                <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Nombre")%>:</td>
                <td class="columnP">
                    <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="nombreGrupo" size="40" maxlength="40"
                               onblur="return xAMayusculas(this);"/>
                </td>
            </tr>
            <tr>
                <td colspan="2" class="subtituloFondoGris"><%=descriptor.getDescripcion("etiq_usuAsoc")%></td>
            </tr>
            <tr>
                <td colspan="2" valign="top">
                    <div id="tblUsuarios" name="tblUsuarios" style="width: 100%; overflow: auto; visibility: visible; -ms-overflow-x: auto; -ms-overflow-y: auto;"></div>
                </td>
            </tr>
        </table>
        <div class="botoneraPrincipal"> 
            <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAceptar")%> name="cmdAceptar"  onClick="pulsarAceptar();">
            <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir"  onClick="pulsarSalir();">
        </div>                        
    </div>                        
</html:form>
        
<script type="text/javascript">
            
            document.onmouseup = checkKeys;
            
            function checkKeysLocal(evento,tecla) {
                if(window.event) evento = window.event;
                keyDel(evento);
            }
            window.focus();
            
        </script>
        
    </BODY>
    
    </html:html>
    
