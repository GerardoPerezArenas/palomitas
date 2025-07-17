<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.formularios.FichaFormularioForm"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.DatosSuplementariosFicheroForm"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.DefinicionTramitesForm"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.ArrayList"%>
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

    if (session!=null){        
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


<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
 <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/gestionTerceros.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/JavaScriptUtil.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/fichaExpediente.js'/>"></script> 
    
<style type="text/css">
   TR.rojo TD {background-color:red;color:white;}
   TR.gris TD {background-color:white;color:#a5a5a5;}
</style>
</head>
<body class="bandaBody" onload="llenarTabla()">    
<%
    ArrayList listaExpedientes = (ArrayList) request.getAttribute("Expedientes");
    String cod_dep = request.getAttribute("cod_departamento").toString();
    String cod_uni = request.getAttribute("cod_unidad_reg").toString();
    String ejerc = request.getAttribute("ejercicio").toString();
    String tipo_reg = request.getAttribute("tipo_reg").toString();
    String bloquearPantalla = request.getAttribute("bloquearPantalla").toString();       
%>


<script type="text/javascript">    
    
    function pulsarCancelar(){
        self.parent.opener.retornoXanelaAuxiliar("@Cancelar");
    }//pulsarCancelar
     
    function pulsarAceptar() {          
        var salida = generaSalida();
        if (salida == "" || ExpedientesPermitidos(salida) == "OK"){
           self.parent.opener.retornoXanelaAuxiliar(salida);
        }//if (salida == "" || ExpedientesPermitidos(salida) == "OK")
    }//pulsarAceptar
    
    function getXMLHttpRequest(){
        var aVersions = [ "MSXML2.XMLHttp.5.0",
            "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
            "MSXML2.XMLHttp","Microsoft.XMLHttp"];

        if (window.XMLHttpRequest){
            // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
            return new XMLHttpRequest();
        }else if (window.ActiveXObject){
            // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
            for (var i = 0; i < aVersions.length; i++) {
                try {
                    var oXmlHttp = new ActiveXObject(aVersions[i]);
                    return oXmlHttp;
                }catch (error) {
                    //no necesitamos hacer nada especial
                }//try-catch
            }//for (var i = 0; i < aVersions.length; i++) 
        }//if
    }//getXMLHttpRequest
    
    function generaSalida(){    
        var salida = "";
        var nombre = "";
        <% for(int i=0;i<listaExpedientes.size();i++){ %>
            nombre = "document.forms[0].chk"+<%=i%>+".checked"                  
            if (eval(nombre) == true){
                if (salida == "")
                    salida += "'"+ '<%=listaExpedientes.get(i)%>' + "'";
                else
                    salida += ",'" + '<%=listaExpedientes.get(i)%>' + "'";
            }//if (eval(nombre) == true)
        <%}%>
        return salida;
    }//generaSalida

    function ExpedientesPermitidos(listado){
        var ajax = getXMLHttpRequest();
        var salida = "";    
        var mensaje = "";
    
        if(ajax!=null){          
            var tipo_reg = '<%=tipo_reg%>';
            var ejercicio = '<%=ejerc%>'
            var url = "<%=request.getContextPath() %>" + "/sge/Tramitacion.do";       
            var parametros = "&opcion=CompruebaPermisoHist&cod_departamento="+<%=cod_dep%>+"&cod_unidad_reg="+<%=cod_uni%>+
                "&ejercicio="+escape(ejercicio)+"&tipo_reg="+escape(tipo_reg)+"&listaExp=" + escape(listado);
            
            ajax.open("POST",url,false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
            ajax.send(parametros);
            
            try{
                if (ajax.readyState==4 && ajax.status==200){
                    var texto = ajax.responseText;                       
                    if (texto.trim() == ""){
                        salida = "OK";
                    }else{
                        var aux = texto.split("||")
                        mensaje = '<%=descriptor.getDescripcion("msjPermisoHist")%>'
                        for (var i=0; i < aux.length; i++) {
                            mensaje = mensaje + "<br>"+ aux[i];
                        }//for (var i=0; i < aux.length; i++)                    
                        jsp_alerta("A",mensaje); 
                    }//if (texto.trim() == "")
                }//if (ajax.readyState==4 && ajax.status==200)
            }catch(Err){
                alert("Error.descripcion: " + Err.description);
            }//try-catch
        }//if(ajax!=null)
        return salida;
    }//ExpedientesPermitidos
    
    function llenarTabla(){
        var datosProcesos = new Array();
        var j = 0;  
        <% for(int i=0;i<listaExpedientes.size();i++){ %>        
            <% if("true".equalsIgnoreCase(bloquearPantalla)){ %>
                datosProcesos[j] = ["<input type='checkbox' class='check' checked='0' disabled='disabled' name='chk"+<%=i%>+"'>",'<%=listaExpedientes.get(i)%>'];
            <% }else{ %>
                datosProcesos[j] = ["<input type='checkbox' class='check' checked='0' name='chk"+<%=i%>+"'>",'<%=listaExpedientes.get(i)%>'];
            <% } %>
            j++;    
        <%}%>  
  
        tabla_exp.lineas = datosProcesos;   
        tabla_exp.displayTabla();
    }//llenarTabla
    
</script>

<form action="" name="f" target="_self">
    <input type="hidden" name="opcion" value=""/>
    <input type="hidden" name="idioma" id="idioma" value="<%=idioma%>"
    <div class="txttitblanco">Desasociar Expedientes</div>
    <div class="contenidoPantalla">
        <table class="contenidoPestanha" border=0 cellspacing="0px" cellpadding="0px">                                                                                        
           <div id="tablaEx" name="tablaEx" style="background-color:white;" height="220" 
                width="335" align="center"> </div>                                                                                      
        </table>
         </div>
            <div class="capaFooter">
        <div class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbGrabar")%>" name="cmdAceptar" onclick="pulsarAceptar();">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelar" onclick="pulsarCancelar();">
        </div>
    </div>
</form>
<script type="text/javascript">           
  var tabla_exp = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaEx'));
  
  tabla_exp.addColumna('25',null,"");
  tabla_exp.addColumna('525',null,"Expedientes Asociados");
  tabla_exp.displayCabecera = true;     
  tabla_exp.displayTabla();
</script>
</body>
</html>
