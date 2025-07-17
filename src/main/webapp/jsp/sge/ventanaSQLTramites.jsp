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
    DefinicionTramitesForm dtForm = null;
    DefinicionProcedimientosForm dpForm = null;
    Log _log = LogFactory.getLog(this.getClass());
    if (session!=null){
        dtForm = (DefinicionTramitesForm)session.getAttribute("DefinicionTramitesForm");
        dpForm = (DefinicionProcedimientosForm)session.getAttribute("DefinicionProcedimientosForm");
        usuario = (UsuarioValueObject)session.getAttribute("usuario");
        idioma = usuario.getIdioma();
        apl = usuario.getAppCod();
	  	int cUsu = usuario.getIdUsuario();
        codUsu = Integer.toString(cUsu);
      }
    Config m_Config = ConfigServiceHelper.getConfig("common");
    Vector listaCamposTramite = new Vector();
    Vector listaCamposProcedimiento = new Vector();
    if (dtForm!=null) {listaCamposTramite = dtForm.getListaCamposTramitesCondEntrada();}
    if (dpForm!=null) {listaCamposProcedimiento = dpForm.getListaCampos();}
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

    function comprobarFecha(inputFecha) {
      var formato = 'dd/mm/yyyy';
      if (Trim(inputFecha.value)!='') {
          var D = DataValida(inputFecha.value);
          inputFecha.value = ( D[0] ? D[1].ISOlocaldateStr() : inputFecha.value );
          if (!D[0]){
            jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
            inputFecha.focus();
            return false;
          } else {
            inputFecha.value = D[1];
            return true;
          }
      }
      return true;
    }

    function cargarCombos() {
        comboCampos.addItems(tiposCampos,codCampos);
    }
    function desactivarCaja1(){
        var caja1 = new Array(document.forms.f.caja1);
        deshabilitarGeneral(caja1);
    }
    function activarCaja1(){
        var caja1 = new Array(document.forms.f.caja1);
        habilitarGeneral(caja1);
    }
    function desactivarCaja2(){
        var caja2 = new Array(document.forms.f.caja2);
        deshabilitarGeneral(caja2);
    }
    function activarCaja2(){
        var caja2 = new Array(document.forms.f.caja2);
        habilitarGeneral(caja2);
    }

    var codCampos = new Array();
    var tiposCampos = new Array();
    var j=0;
    <%for (int i=0;i<listaCamposTramite.size();i++){
        DefinicionCampoValueObject campos = (DefinicionCampoValueObject)listaCamposTramite.get(i);
        if (campos.getActivo().equals("SI")) {
            if (!campos.getCodTipoDato().equals("4") && !campos.getCodTipoDato().equals("5")) {%>
                tiposCampos[j] = '<%=(String) campos.getCodTipoDato()%>';
                codCampos[j++] = '<%=(String) campos.getCodCampo()%>';
            <%}
        }%>
    <%}%>
    <%for (int i=0;i<listaCamposProcedimiento.size();i++){
        DefinicionCampoValueObject campos = (DefinicionCampoValueObject)listaCamposProcedimiento.get(i);
        if (campos.getActivo().equals("SI")) {
            if (!campos.getCodTipoDato().equals("4") && !campos.getCodTipoDato().equals("5")) {%>
                tiposCampos[j] = '<%=(String) campos.getCodTipoDato()%>';
                codCampos[j++] = '<%=(String) campos.getCodCampo()%>';
            <%}
        }%>
    <%}%>
    function validarCampos(){
        var formFichero = document.forms[0].fichero.value;
        if(formFichero!="") return true;
      return false;
    }

    function pulsarAceptar() {
        var saida = document.f.sql.value;
        saida = saida.replace(/\s*[\r\n][\r\n \t]*/g, " ");         
        if (saida!="") {
            self.parent.opener.retornoXanelaAuxiliar(saida);
        } else {
          jsp_alerta('A','<%=descriptor.getDescripcion("msjExpresionVacia")%>');
        }
    }

    function pulsarCancelar(){
            self.parent.opener.retornoXanelaAuxiliar("");
    }

    function pulsarEliminar(){
            self.parent.opener.retornoXanelaAuxiliar("-1");
    }

    function pulsarLimpiar() {
        comboCampos.selectItem(-1);
        comboOperadores.selectItem(-1);
        document.f.caja1.value="";
        document.f.caja2.value="";
    }

    function pulsarLimpiarTodo() {
        document.f.sql.value="";
    }

    function pulsarAnadir(){
        var caja1 = document.f.caja1.value;
        var caja2 = document.f.caja2.value;
        var aux = document.f.sql.value;
        var tipoCampo = document.f.codCampos.value;
        var campo = document.f.descCampos.value;
        var operador = document.f.descOperadores.value;
        var ok=true;
        if (campo!="" && operador!="") {
            switch(tipoCampo) {
                case "1":if ((caja1=="") && ((operador != "IS NULL") && (operador != "IS NOT NULL"))) ok = false;
                        break;
                case "2":caja1="'"+caja1+"'";
                        if(document.f.descOperadores.value == "BETWEEN") caja2="'"+caja2+"'";
                        break;
                case "3":if ((caja1=="") && ((operador != "IS NULL") && (operador != "IS NOT NULL"))) ok = false;
                    //caja1="TO_DATE('"+caja1+"','DD/MM/YYYY')";
                    caja1="'"+caja1+"'";
                    if(document.f.descOperadores.value == "BETWEEN") {
                        if (caja2=="") ok = false;
                        //caja2="TO_DATE('"+caja2+"','DD/MM/YYYY')";;
                        caja2="'"+caja2+"'";
                    }
                    break;
                default: break
            };

            if(document.f.descOperadores.value == "BETWEEN") {
                aux = aux + " (" + document.f.descCampos.value + " " + document.f.descOperadores.value
                          + " "+ caja1 + " , " + caja2 + ")";
            } else if(document.f.descOperadores.value == "LIKE") {
                aux = aux + " (" + document.f.descCampos.value + " "
                          + document.f.descOperadores.value + " "
                          + caja1.replace("*","%") + ")";
            } else if((document.f.descOperadores.value == "IS NULL") || (document.f.descOperadores.value == "IS NOT NULL")) {
                aux = aux + " (" + document.f.descCampos.value + " "
                          + document.f.descOperadores.value + ")";
            } else {
                aux = aux + " (" + document.f.descCampos.value + " "
                          + document.f.descOperadores.value + " "
                          + caja1 + ")";
                }
            if (ok==true) {
                document.f.sql.value = aux;
                pulsarLimpiar();
            } else {
                jsp_alerta("A",'<%=descriptor.getDescripcion("msjCondicionOblig")%>');
            }
        } else {
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjCondicionOblig")%>');
        }
    }

    function pulsarAND(){
        var aux = document.f.sql.value;
        document.f.sql.value = aux + " AND";
    }

    function pulsarOR(){
        var aux = document.f.sql.value;
        document.f.sql.value = aux + " OR";
    }

    function pulsarp1(){
        var aux = document.f.sql.value;
        document.f.sql.value = aux + " (";
    }

    function pulsarp2(){
        var aux = document.f.sql.value;
        document.f.sql.value = aux + " )";
    }
</script>
<%String sql=request.getParameter("sql");
if (sql==null) sql="";%>
<form action="" name="f" target="_self">
    <input type="hidden" name="opcion" value=""/>
    <div class="txttitblanco"><%=descriptor.getDescripcion("etiqExpresion")%></div>
    <div class="contenidoPantalla">
        <table style="width:100%" >
            <tr>
                <td>
                    <input type="hidden" name="codCampos"/>
                    <input type="text" class="inputTextoObligatorio" name="descCampos"  size="15" readonly="true">
                    <A href="" id="anchorCampos"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampos" name="botonCampos" style="cursor:hand; border: 0px none"></span></A>
                </td>
                <td>
                    <input type="text" class="inputTextoObligatorio" name="descOperadores"  size="15" readonly="true">
                    <A href="" id="anchorOperadores"><span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonOperadores" name="botonOperadores" style="cursor:hand; border: 0px none"></span></A>
                </td>
                <td>
                    <input type="text" class="inputTextoObligatorio" name="caja1" size="50" onkeyup="javascript:xValidarCaracteres(this);" style="text-transform: none">
                </td>
                <td>
                    <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbLimpiar")%>" name="cmdLimpiar2" onclick="pulsarLimpiar();">                                                                                        
                </td>
            </tr>
            <tr>
                <td colspan=2>
                    <input type="button" class="botonGeneral" value="AND" name="cmdAND" onclick="pulsarAND();" style="width:49px">
                    <input type="button" class="botonGeneral" value="OR" name="cmdOR" onclick="pulsarOR();" style="width:49px">
                    <input type="button" class="botonGeneral" value="(" name="cmdp1" onclick="pulsarp1();" style="width:49px">
                    <input type="button" class="botonGeneral" value=")" name="cmdp2" onclick="pulsarp2();" style="width:49px">
                </td>
                <td>
                    <input type="text" class="inputTextoObligatorio" name="caja2" size="50" onkeyup="return xValidarCaracteres(this);" style="text-transform: none">
                </td>
                <td>
                    <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAnadir")%>" name="cmdAnadir" onclick="pulsarAnadir();">
                </td>
            </tr>
        </table>
        <div style="width: 100%">
            <textarea name="sql" class="textareaTexto" cols="150" rows="14" onkeyup="return xValidarCaracteres(this);" style="text-transform: none"></textarea>
        </div>
        <div class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbGrabar")%>" name="cmdAceptar" onclick="pulsarAceptar();">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelar" onclick="pulsarCancelar();">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbLimpiar")%>" name="cmdLimpiar" onclick="pulsarLimpiarTodo();">
        </div>
    </div>
</form>
<script type="text/javascript">
    var codOperadores = new Array(0,1,2,3,4,5,6,7,8);
    var operadores = new Array('=','<>','>','<','>=','<=','BETWEEN','IS NULL','IS NOT NULL');
    var codOperadoresCampoTexto = new Array(0,1,2,3,4);
    var operadoresCampoTexto = new Array('=','<>','LIKE','IS NULL','IS NOT NULL');
    var aux="<%=sql%>";
    aux=aux.replace(/_ascii_37_/g,"%");
    document.f.sql.value=aux;
    var comboCampos = new Combo("Campos");
    var comboOperadores = new Combo("Operadores");
    cargarCombos();
    desactivarCaja2();

    comboCampos.change = function() {
        if(document.forms.f.codCampos.value == "2") comboOperadores.addItems(codOperadoresCampoTexto,operadoresCampoTexto);
        else comboOperadores.addItems(codOperadores,operadores);
        comboOperadores.selectItem(-1);
    }
    comboOperadores.change = function() {
        if(document.forms.f.descOperadores.value == "BETWEEN") {
            activarCaja1();
            activarCaja2();
        } else if(document.forms.f.descOperadores.value == "IS NULL") {
            desactivarCaja1();
            desactivarCaja2();
            document.forms.f.caja1.value="";
            document.forms.f.caja2.value="";
        } else if(document.forms.f.descOperadores.value == "IS NOT NULL") {
            desactivarCaja1();
            desactivarCaja2();
            document.forms.f.caja1.value="";
            document.forms.f.caja2.value="";
        } else {
            activarCaja1();
            desactivarCaja2();
            document.forms.f.caja2.value="";
        }
    }

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
            if (comboCampos.base.style.visibility == "visible" && isClickOutCombo(comboCampos,coordx,coordy)) setTimeout('comboCampos.ocultar()',20);
            if (comboOperadores.base.style.visibility == "visible" && isClickOutCombo(comboOperadores,coordx,coordy)) setTimeout('comboOperadores.ocultar()',20);
        }
        if (teclaAuxiliar == 9){
            comboCampos.ocultar();
            comboOperadores.ocultar();
        }

}
</script>
  </body>
</html>
