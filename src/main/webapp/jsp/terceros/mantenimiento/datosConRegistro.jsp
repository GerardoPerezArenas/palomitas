<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.FusionDivisionForm" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>	
<html>
<head>
  <title>Datos modificacion con registro</title>	
  <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
  <meta http-equiv="Content-Type" content="text/html;	charset=iso-8859-1">
  
  
  <%
    UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int idioma=1;	
    int apl=3;
    if (session.getAttribute("usuario") != null){
	usuarioVO =	(UsuarioValueObject)session.getAttribute("usuario");
	apl =	usuarioVO.getAppCod();
	idioma = usuarioVO.getIdioma();
    }	
	%>
	
  <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"	type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
  <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>"	/>
  <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
  
  <!-- Ficheros JavaScript -->

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
  <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css">
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
 
	<script type="text/javascript">
	
    /**************  FUNCIONES PARA	LA CARGA DE	LOS CALENDARIOS ***********************/
    function mostrarCalendario(img,campoFecha,evento){
        if(window.event) evento = window.event;
	var indice = document.getElementById(img).src.indexOf('fa-calendar');
	if (indice!=-1)
	  showCalendar('forms[0]',campoFecha,null,null,null,'',img,'',null,null,null,	
            null,null,null,null,'',evento);
    }//de la funcion

    //Funcion para la verificación de un campo fecha
    function comprobarFecha(inputFecha){
	if(Trim(inputFecha.value)!=''){
	  if(!ValidarFechaConFormato(document.forms[0],inputFecha)){
	    jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
	    inputFecha.focus();	
	    return false;	
	  } 
	} 
	return true;
    }//de la funcion

    function pulsarAceptar(){
        if(validarFormulario()){
                        if ( document.forms[0].fechaOperacion.value!= ""){
                            var valores = new Array();	 
                            valores[0]=document.forms[0].fechaOperacion.value;
                            if (document.forms[0].generarOperaciones.checked)
                                    valores[1]="SI";
                            else valores[1]="NO";
                            self.parent.opener.retornoXanelaAuxiliar(valores);
                        }else
                            self.parent.opener.retornoXanelaAuxiliar();
        }
    }	

    
    function pulsarCancelar(){
        self.parent.opener.retornoXanelaAuxiliar();
    }	
	</script> 

</head>
<body class="bandaBody">
<form action="" method="post" name="formulario" id="formulario">
    <input type="hidden" name="opcion" value="">
    <input type="hidden" name="codProceso" value="">
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("Etiq_FechaOperacione")%></div>
    <div class="contenidoPantalla">
        <table>	
            <tr>
                <td>
                    <table width="100%" border="0px" cellpadding="0px" cellspacing="0px">	
                        <tr>
                            <td height="100px" width="40%" class="etiqueta"><%=descriptor.getDescripcion("Etiq_FechaOperacione")%>:</td>
                            <td height="100px">
                                <input class="inputTxtFechaObligatorio" type="text" size="11" maxlength="10" name="fechaOperacion" 
                                 onkeyup = "return SoloCaracteresFecha(this);" onblur = "javascript:return comprobarFecha(this);" onfocus = "this.select();">
                                 <a name="anchorDesde"	id="anchorDesde" href="javascript:calClick(event);return false;"
                                    onClick="mostrarCalendario('Desde','fechaOperacion',event);return false;" style="text-decoration:none;">
                                    <span class="fa fa-calendar" aria-hidden="true" name="Desde" id="Desde"></span>
                                </a>
                            </td>
                        </tr>
                        <tr>
                            <td class="etiqueta" colspan="2">
                                <input name="generarOperaciones" type="checkbox" value="SI" checked><%=descriptor.getDescripcion("etiqGenOper")%>											
                            </td>																	
                        </tr>
                    </table>
                </td>	
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input name="botonAceptar" type="button" class="botonGeneral" id="botonAceptar" accesskey="A" value="<%=descriptor.getDescripcion("gbAceptar")%>" onclick="pulsarAceptar();">
            <input name="botonCancelar" type="button" class="botonGeneral" id="botonCancelar" accesskey="S" value="<%=descriptor.getDescripcion("gbCancelar")%>" onclick="pulsarCancelar();">
        </div>
    </div>
</form>

</body>
</html>
