<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.util.struts.StrutsFileValidation" %>
<%@ page import="es.altia.agora.technical.ConstantesDatos" %>
<%@page import="java.lang.Integer" %>
<html>
<head>
<title>Scanner</title>
<%
    int idioma=1;
    int apl=4;

    String codUsu = "";
    UsuarioValueObject usuario=new UsuarioValueObject();
    if (session!=null){
      if (usuario!=null) {
        usuario = (UsuarioValueObject)session.getAttribute("usuario");
        idioma = usuario.getIdioma();
        apl = usuario.getAppCod();
	  	int cUsu = usuario.getIdUsuario();
        codUsu = Integer.toString(cUsu);
      }
    }

    //String app = (String)request.getParameter("app");
    Config co = ConfigServiceHelper.getConfig(request.getParameter("app"));
    String fileSizeMax = co.getString("filesize.upload.correct");
    int num = Math.round(Integer.parseInt(fileSizeMax)/ConstantesDatos.DIVISOR_BYTES);
%>
<!-- Beans -->
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script language="javascript">
<!--
     /** Recupera la imagen capturada del scanner */
    function getImageScanner()
    {
        var element = document.getElementById('imgApplet');
        // Se llama al método del applet
       var imagen= element.getImagen();
        if(imagen==null){
               timerID = setTimeout("getImageScanner()",500);
        }
        else{
            var trEscaneo    = window.opener.document.getElementById("trEscaneo");
            var trTxtFichero    = window.opener.document.getElementById("trTxtFichero");
            var trEtiqFichero = window.opener.document.getElementById("trEtiqFichero");

            document.getElementById('imagen').innerHtml = imagen;
            window.opener.document.forms[0].docEscaneado.value = imagen;
            window.opener.document.forms[0].cmdAceptar.disabled = false;
            trEtiqFichero.style.display  = 'none';
            trTxtFichero.style.display   = 'none';
            trEscaneo.style.display      = 'inline';
            if(window.opener.document.forms[0].tituloFichero!=null)
                generarTitulo();

            window.close();
        }
    }

     /** Genera el título de un documento en base a la fecha y hora actual y lo añade en la ventana
        padre */
    function generarTitulo()
    {
        var cab     = "DOCUMENTO";
        var guion   = "_";
        var fecha = new Date();
        var day = fecha.getDate() + "";
        var month = (fecha.getMonth() +  1) + "";
        var year  = fecha.getFullYear() + "";
        var hour = fecha.getHours() + "";
        var minutes = fecha.getMinutes() + "";
        var seconds = fecha.getSeconds() + "";

        if(day.length==1)
            day = "0" + day;
        if(month.length==1)
            month = "0" + month;
        if(hour.length==1)
            hour = "0" + hour;
        if(minutes.length==1)
            minutes = "0" + minutes;
        if(seconds.length==1)
            seconds = "0" + seconds;

        var nombre = cab + guion + day + month + year + guion + hour + minutes + seconds;
        window.opener.document.forms[0].tituloFichero.value = nombre;
    }

-->
</script>
</head>
<body onLoad="javascript:getImageScanner();">
	<applet id="imgApplet" codebase="." code="uk.co.mmscomputing.application.imageviewer.MainApp.class"
	archive="imageviewer.jar,sane.jar,twain.jar"
	width=100%
	height=100%>
             <param name="nodisponible" value="<%= descriptor.getDescripcion("etiqNoImagen") %>">
	     <param name="seleccionar" value="<%= descriptor.getDescripcion("etiqSeleccionar") %>">
	     <param name="enviar" value="<%= descriptor.getDescripcion("etiqEnviar") %>">
	     <param name="escanear" value="<%= descriptor.getDescripcion("etiqEscanear") %>">
	     <param name="titulo" value="<%= descriptor.getDescripcion("gEtiq_Documento") %>">
	     <param name="txtImagen" value="<%= descriptor.getDescripcion("gEtiq_Documento") %>">

             <!--
             <param name="maxImagen" value="<= StrutsFileValidation.TAM_MAX_FILE >">
             -->
             <param name="maxImagen" value="<%= fileSizeMax %>">
             <param name="txtTamExceed" value="<%=descriptor.getDescripcion("etiqTamMaxExceed")%> <%= num %> <%= ConstantesDatos.DESCRIPCION_MEGABYTES %>">
	</applet>
	<input type="hidden" id="imagen" name="imagen" value=""/>
</body>
</html>