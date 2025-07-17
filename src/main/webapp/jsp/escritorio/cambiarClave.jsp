<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.Vector" %>
<%@page import="java.util.ResourceBundle" %>

    <%
      UsuarioValueObject usuarioVO = new UsuarioValueObject();
      int idioma=1;
      // Forzamos a que la aplicacion sea siempre la 1
      int apl = 1;
      if (session.getAttribute("usuario") != null){
        usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        idioma = usuarioVO.getIdioma();
      }


      int longitud = 0;
      try{
            ResourceBundle configuracion = ResourceBundle.getBundle("common");
            String valorLongitud  = configuracion.getString("password_longitud");

            if(valorLongitud!=null && !"".equals(valorLongitud)){
                longitud = Integer.parseInt(valorLongitud);                
            }
      }catch(Exception e){
        longitud = 0;
      }
    %>
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<html:html locale="true">
 <fmt:setLocale value='${sessionScope["org.apache.struts.action.LOCALE"]}' scope="session"/>
 <head>
     <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
     <jsp:include page="/jsp/escritorio/tpls/app-constants.jsp" />
    <title>Cambiar clave</title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>" media="screen">

    <script type="text/javascript">

    var longitud_configuracion = "<%=longitud%>";
    var msgFormatoLongitudPassword1   = '<fmt:message key="formatoPassword.usuario.longitud.flexia"/>';
    var msgFormatoLongitudPassword2   = '<fmt:message key="formatoPassword.usuario.longitud.caracteres"/>';
    var msgFormatoPassword            = '<fmt:message key="formatoPassword.usuario.flexia"/>';
    
    /*
    alert("longitud_configuracion " + longitud_configuracion);
    alert("msgFormatoLongitudPassword1: " + msgFormatoLongitudPassword1);
    alert("msgFormatoLongitudPassword2: " + msgFormatoLongitudPassword2);
    alert("msgFormatoPassword " + msgFormatoPassword);
    */
    var datosAEnviar = new Array();
    var login="";
    
    function validarPassword(password){         
	var space  = " ";
        var cumpleLongitud = true;
        var contador = 0;
        var longitud = 0;
                
        if(longitud_configuracion!=null && longitud_configuracion!=""){            
            try{
                longitud = parseInt(longitud_configuracion);                                                              
                if(password.length<longitud){
                    contador++;
                    cumpleLongitud = false;
                }
            }catch(err){                
            }            
        }
        
	
	if (password.indexOf(space) > -1) {
            contador++;
	}     	
	
	if (!(password.match(/\d/))) {
             contador++;
	}		
	
	if (!(password.match(/[a-zA-Z]/))) {	   
           contador++;
	}	
	
	if (contador>0){
            if(longitud>0)
                jsp_alerta('A',msgFormatoLongitudPassword1 + " " + longitud + " " + msgFormatoLongitudPassword2);
            else{                 
               jsp_alerta('A',msgFormatoPassword);		                            
            }
            
             return false;
	}		 
        return true;        
    }
    
    
    function pulsarAceptar() {
      if(comprobarObligatorios()) {
          if(document.forms[0].claveNueva.value == document.forms[0].claveRepetida.value) {
              
              if(validarPassword(document.forms[0].claveNueva.value)){
                document.forms[0].option.value = "cambiarClave";
                document.forms[0].target="oculto";
                document.forms[0].action="<c:url value='/UserPref.do'/>";
                document.forms[0].submit();
              }
          } else {
            document.forms[0].claveNueva.value = "";
            document.forms[0].claveRepetida.value = "";
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjClaveIgual")%>');
          }
      } else {
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
      }
    }

    function comprobarObligatorios() {
      if(document.forms[0].claveAntigua.value!="" &&
         document.forms[0].claveNueva.value!="" &&
         document.forms[0].claveRepetida.value!="")
      {return true;}
      else
      {return false;}
    }

    function pulsarSalir(){
      self.parent.opener.retornoXanelaAuxiliar();
    }

    function noClave() {
      jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoClave")%>');
    }

    function claveModificada() {
      jsp_alerta("A",'<%=descriptor.getDescripcion("msjClaveMod")%>');
      self.parent.opener.retornoXanelaAuxiliar();
    }
    
    
    function esClaveRepetida() {
      jsp_alerta("A",'<%=descriptor.getDescripcion("msjClaveRepetida")%>');
    }
    
     function inicializar(){
            datosAEnviar = self.parent.opener.xanelaAuxiliarArgs;
            login=datosAEnviar[0];
           document.getElementById('login').innerHTML=login; 
            }
    </script>
 </head>

 <body class="bandaBody" onload="inicializar();">
    <form  name="cambiaClave">

    <input type="hidden"  name="option" />
    <input type="hidden"  name="value" />

    <div id="titulo" class="txttitblanco">
        <%=descriptor.getDescripcion("etiq_CambClave")%>
    </div>
    <div class="contenidoPantalla">
        <table id ="tablaDatosGral"  border="0px" width="340px" cellspacing="3px" cellpadding="2px" border="0px">
            <tr>
            <td width="40%" class="etiqueta"><%=descriptor.getDescripcion("etiq_login")%>:</td>
            <td width="60%" class="columnP">
                <div id="login"></div>
            </td>
          </tr>
          <tr>
            <td width="40%" class="etiqueta"><%=descriptor.getDescripcion("etiq_claveAnt")%>:</td>
            <td width="60%" class="columnP">
              <input type="password" name="claveAntigua" class="inputTextoObligatorio" size="20" maxlength="15" >
            </td>
          </tr>
          <tr>
            <td width="40%" class="etiqueta"><%=descriptor.getDescripcion("etiq_claveNuev")%>:</td>
            <td width="60%" class="columnP">
              <input type="password" name="claveNueva" class="inputTextoObligatorio" size="20" maxlength="15" >
            </td>
          </tr>
          <tr>
            <td width="40%" class="etiqueta"><%=descriptor.getDescripcion("etiq_claveRep")%>:</td>
            <td width="60%" class="columnP">
              <input type="password" name="claveRepetida" class="inputTextoObligatorio" size="20" maxlength="15" >
            </td>
          </tr>
        </table>
        <div class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbAceptar")%>' name="botonAceptar" onClick="pulsarAceptar();">
            <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbSalir")%>' name="botonSalir" onClick="pulsarSalir();">
        </div>
    </div>
</form>
 </body>
</html:html>
