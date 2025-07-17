<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.Vector"%>

<html>
    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Lista de Procesos </title>
  
        
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 5;
            String css="";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                css=usuarioVO.getCss();
            }
        %>
        
              <!-- Estilos -->

        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        
        <!-- Ficheros JavaScript -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        
        <script type="text/javascript">
            
            
            // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
            function inicializar(){
                window.focus();
            }
            
            
            // FUNCIONES DE PULSACION DE BOTONES
            
            function pulsarCerrar(retorno){
                self.parent.opener.retornoXanelaAuxiliar(retorno);
            }
            

          



            function pulsarAceptar(){
                if(validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')){
                    var datos = new Array();
                    datos[0] = document.forms[0].descMenu.value;
                    pulsarCerrar(datos);	  
                }  
            }
            
    </script>
</head>

<body class="bandaBody" onload="javascript:{pleaseWait('off');inicializar();}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
<form  method="post">
    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiq_Menu")%></div>
    <div class="contenidoPantalla">
        <table style="width: 100%">
            <tr style="vertical-align: top"> 
                <td class="etiqueta" style="width: 15%"><%=descriptor.getDescripcion("gEtiq_nomMenu")%>:</td>
                <td class="etiqueta" style="width: 85%"> 
                    <input type="text" name="descMenu" id="descMenu" size="100" maxsize="60" class="inputTextoObligatorio" value="">
                </td>
            </tr>                                                    
        </TABLE>
        <div class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAceptar")%> 
                                       name="botonAceptar" onClick="pulsarAceptar();" accesskey="A"> 
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> 
                                       name="botonCancelar" onClick="pulsarCerrar();" accesskey="M"> 
        </div>                               
    </div>                               
</form>
<script type="text/javascript">  

    function checkKeysLocal(tecla){		
        keyDel();		
    }

</script>
</body>
</html>

