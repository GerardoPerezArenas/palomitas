<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.formularios.FichaFormularioForm"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.DatosSuplementariosFicheroForm"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.DefinicionTramitesForm"%>
<%@ page import="java.util.Vector"%>
<%@ page import="es.altia.agora.business.sge.DefinicionCampoValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.DefinicionProcedimientosForm"%>
<%@ page import="es.altia.agora.business.sge.DefinicionProcedimientosValueObject"%>
<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title>::: Adjuntar Expresión:::</title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<%
    int idioma=1;
    int apl=4;
	String codUsu = "";
    UsuarioValueObject usuario=new UsuarioValueObject();  
    Log _log = LogFactory.getLog(this.getClass());
    Vector ListaCampos = new Vector();        

    if (session!=null){
        ListaCampos = (Vector)request.getAttribute("listadoCampos");        
        usuario = (UsuarioValueObject)session.getAttribute("usuario");
        idioma = usuario.getIdioma();
        apl = usuario.getAppCod();
	  	int cUsu = usuario.getIdUsuario();
        codUsu = Integer.toString(cUsu);
      }
    Config m_Config = ConfigServiceHelper.getConfig("common");

%>
<!-- Estilos -->
<!-- Beans -->
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<style type="text/css">
   TR.rojo TD {background-color:red;color:white;}
   TR.gris TD {background-color:white;color:#a5a5a5;}
</style>
</head>
<body class="bandaBody">
<script type="text/javascript">
    var codCampos = new Array();
    var tiposCampos = new Array();
    var j=0;
        
   <%for (int i=0;i<ListaCampos.size();i++){
        DefinicionCampoValueObject campos = (DefinicionCampoValueObject)ListaCampos.get(i);%>
        tiposCampos[j] = '<%=(String) campos.getCodTipoDato()%>';
        <% if (campos.getCodTramite() == null || "-".equals(campos.getCodTramite())) {%>
            codCampos[j++] =  '<%=(String) campos.getCodCampo()%>';
        <%}else{%>
            codCampos[j++] =  '<%=(String) campos.getCodCampo()%>' +"_T"+ '<%=(String) 	campos.getCodTramite() %>';        
    <%}}%>
        
    function pulsarCancelar(){
        self.parent.opener.retornoXanelaAuxiliar(null);
    }
    function pulsarLimpiar() {
        comboCampos.selectItem(-1);        
        comboOperadores.selectItem(-1);        
        document.f.caja1.value="";
        document.f.caja2.value="";
        document.f.caja3.value="";
        
    }    
    function cargarCombos() {
        comboCampos.addItems(tiposCampos,codCampos);        
        comboOperadores.addItems(codOperadores,operadores);        
    }          
    
     function pulsarEliminar(){
        self.parent.opener.retornoXanelaAuxiliar("-1");
    }

    function pulsarLimpiarTodo() {
        document.f.sql.value="";
        pulsarLimpiar();
    }

    function pulsarAnadir(){        
        var cadena = "";
       document.f.sql.value = "" 
        
       cadena =  " " + document.f.descCampos.value + " " + document.f.descOperadores.value +"("
       
       if (document.f.caja1.value != "")
       {
           cadena = cadena + document.f.caja1.value + " DIAS ";
       }
       else
       {
           cadena = cadena + "0" + " DIAS " ;
       }    
       if (document.f.caja2.value != "")
       {
           cadena = cadena + document.f.caja2.value + " MESES ";
       }
       else
       {
           cadena = cadena + "0" + " MESES " ;
       }    
       
       if (document.f.caja3.value != "")
       {
           cadena = cadena + document.f.caja3.value + " ANOS ";
       }
       else
       {
           cadena = cadena + "0" + " ANOS " ;
       }      
       
       cadena.replace(/\s*[\r\n][\r\n \t]*/g, " ");     
       document.f.sql.value = cadena + ")"
       pulsarLimpiar();
     }        
     
     function pulsarAceptar() {                 
        var cadena = "";
        cadena = document.f.sql.value;
        self.parent.opener.retornoXanelaAuxiliar(cadena);
     }     
    
</script>


<%String sql=request.getParameter("sql");
    if (sql==null) sql="";
    sql = sql.replace(";SUMA;","+");
%>
<form action="" name="f" target="_self">
    <input type="hidden" name="opcion" value=""/>

    <div class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_ExpCal")%></div>
    <div class="contenidoPantalla">
        <table style="width:100%">
            <tr>
                <td style="width: 15%">
                    <input type="hidden" name="codCampos"/>
                    <input type="text" class="inputTexto" name="descCampos"  size="15" readonly="true">
                    <A href="" id="anchorCampos"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampos" name="botonCampos" style="cursor:hand; border: 0px none"></span></A>
                </td>
                <td style="width: 15%">
                    <input type="text" class="inputTexto" name="descOperadores"  size="15" readonly="true">
                    <A href="" id="anchorOperadores"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonOperadores" name="botonOperadores" style="cursor:hand; border: 0px none"></span></A>
                </td>
                    <td class="etiqueta" style="width: 10%"><%=descriptor.getDescripcion("gEtiq_dias")%>:</td>
                </td>                                                            
                <td>
                    <input type="text" class="inputTexto" name="caja1" size="10" onkeyup="javascript:return SoloDigitos(this);" style="text-transform: none">
                </td>
                <td align ="right">
                    <!-- BOTÓN LIMPIAR -->
                    <table cellpadding="0px" cellspacing="0px">
                        <TR>
                            <TD>
                                <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbLimpiar")%>" name="cmdLimpiar2" onclick="pulsarLimpiar();">                                                                                        
                            </TD>                                                                                                                        
                        </TR>                                                        
                    </table>
                    <!-- BOTÓN LIMPIAR --> 
                </td>
            </tr>
            <tr>
                <td></td>
                <td></td> 
                </td>
                    <td class="etiqueta" ><%=descriptor.getDescripcion("gEtiq_meses")%>:</td>
                </td>                                                            
                <td>
                    <input type="text" class="inputTexto" name="caja2" size="10" onkeyup="javascript:return SoloDigitos(this);" style="text-transform: none">
                </td>
                <td>
                    <!-- BOTÓN AÑADIR -->
                    <table cellpadding="0px" cellspacing="px" border="0px" align=right>
                        <TR>
                            <TD>
                                <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAnadir")%>" name="cmdAnadir" onclick="pulsarAnadir();">
                            </TD>
                        </TR>
                    </table>
                    <!-- BOTÓN AÑADIR -->
                </td>
            </tr>
            <tr>
                <td></td>
                <td></td> 
                </td>
                    <td class="etiqueta"><%=descriptor.getDescripcion("gEtiq_anos")%>:</td>
                </td>                                                            
                <td>
                    <input type="text" class="inputTexto" name="caja3" size="10" onkeyup="javascript:return SoloDigitos(this);" style="text-transform: none">
                </td>
                <td></td>
            </tr>
        </table>
        <div style="width: 100%">
            <textarea name="sql" class="textareaTexto" readonly="true" cols="157" rows="1" onkeyup="return xValidarCaracteres(this);" style="text-transform: none"></textarea>
        </div>
        <div STYLE="height:0px;" class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbGrabar")%>" name="cmdAceptar" onclick="pulsarAceptar();">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelar" onclick="pulsarCancelar();">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbLimpiar")%>" name="cmdLimpiar" onclick="pulsarLimpiarTodo();">
        </div>
    </div>
</form>
<script type="text/javascript">        
    var codOperadores = new Array(0,1);    
    var operadores = new Array('+','-');                
    var comboOperadores = new Combo("Operadores");    
    var comboCampos = new Combo("Campos");          
    document.f.sql.value = "<%=sql%>";    
    cargarCombos();     
      
</script>
</body>
</html>
