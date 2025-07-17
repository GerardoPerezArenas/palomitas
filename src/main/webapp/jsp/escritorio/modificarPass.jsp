<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@ page import="es.altia.common.service.config.Config" %>
<%@ page import="java.util.StringTokenizer" %>
<!--[if IE 8]> <html lang="es" class="ie8"> <![endif]-->
<!--[if !IE]><!-->
<html lang="es">
    <!--<![endif]-->
 <fmt:setLocale value='${initParam["sLocaleInicial"]}' scope="session"/>
 <head>
     <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
     <jsp:include page="/jsp/escritorio/tpls/app-constants.jsp" />

<style type="text/css">

    div.login div.loginVersion {  
       width:100px;
       margin-left: 330px;
        margin-top: -10px;
   }

   html>body div.login div.loginVersion {    
       width:100px;
       margin-left: 330px;
       margin-top: 30px;
   }

</style>

<%
    ClassLoader classLoader =  this.getClass().getClassLoader();
    java.io.InputStream inputStream = classLoader.getResourceAsStream("version.properties");
    java.util.Properties properties = new java.util.Properties();
    properties.load(inputStream);
    inputStream.close();                     
    java.util.HashMap parameters = new java.util.HashMap(properties);
    String version = (String)parameters.get("version.aplicacion");

    java.io.InputStream inputStreamLogin = classLoader.getResourceAsStream("authentication.properties");
    java.util.Properties propertiesLogin = new java.util.Properties();
    propertiesLogin.load(inputStreamLogin);
    inputStreamLogin.close();
    java.util.HashMap parametersLogin = new java.util.HashMap(propertiesLogin);
    String login = (String)parametersLogin.get("Auth/accessMode");
    StringTokenizer tokens=new StringTokenizer(login,";");
        String loginSIGP = "hidden";

        while (tokens.hasMoreElements()) {
            if ("pass".equals(tokens.nextToken())) loginSIGP="visible";
        }

    String renovacion = (String)request.getAttribute("RENOVACION_PASSWORD_PROXIMO_ACCESO");
    request.setAttribute("RENOVACION_PASSWORD_PROXIMO_ACCESO",renovacion);


    String LONGITUD_MINIMA = (String)request.getAttribute("LONGITUD_MINIMA_PASSWORD");
    request.setAttribute("LONGITUD_MINIMA_PASSWORD",LONGITUD_MINIMA);

%>
    <title><fmt:message key="cambiaPass.title"/></title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-1.9.1.min.js'/>"></script>
<script type='text/javascript'>
    
    var renovacion = "<%=renovacion%>"; 
    var contrasenaNoCoinciden         = '<fmt:message key="Escritorio.login.ContraseñaNoCoincide"/>';
    var msgFormatoLongitudPassword1   = '<fmt:message key="formatoPassword.usuario.longitud.flexia"/>';
    var msgFormatoLongitudPassword2   = '<fmt:message key="formatoPassword.usuario.longitud.caracteres"/>';
    var msgFormatoPassword            = '<fmt:message key="formatoPassword.usuario.flexia"/>';
    
    var inserteCampos   = '<fmt:message key="Escritorio.login.InserteCampos"/>';
    var claveModificada = '<fmt:message key="Escritorio.login.claveModificada"/>';
    var longitudMinima = "<%=LONGITUD_MINIMA%>";    
   
    
    function Cancelar() {top.close();}


    function validarPassword(password){         
	var space  = " ";
        var cumpleLongitud = true;
        var contador = 0;
        var longitud = 0;
        
        if(longitudMinima!=null && longitudMinima!="" && longitudMinima!="NO" ){
            
            try{
                longitud = parseInt(longitudMinima);                
              
                                
                if(password.length<longitud){
                    contador++;
                    cumpleLongitud = false;
                }
            }catch(err){                
            }            
        }
         
	//It must not contain a space
	if (password.indexOf(space) > -1) {
            contador++;
	}     
	 
	//It must contain at least one number character
	if (!(password.match(/\d/))) {
             contador++;
	}
		
	//It must start with at least one letter     
	if (!(password.match(/[a-zA-Z]/))) {
	   //errorMsg += "La contraseña debe contener letras<\br>";
           contador++;
	}
	
	//If there is aproblem with the form then display an error
	if (contador>0){
            if(longitud>0)
                jsp_alerta('A',escape(msgFormatoLongitudPassword1) + " " + longitud + " " + escape(msgFormatoLongitudPassword2));
            else{                 
               jsp_alerta('A',escape(msgFormatoPassword));		                            
            }
             return false;
	}		 
        return true;
        
    }



    function Aceptar(event){
        event.preventDefault();
       
       if(comprobarObligatorios()){
           if(document.forms[0].nuevaPassword.value != document.forms[0].password.value){
                jsp_alerta("A", contrasenaNoCoinciden);
                return false;
            }else{                
                // Las passwords coinciden, se comprueba si tiene la longitud y contienen números y letras
                if(validarPassword(document.getElementById("nuevaPassword").value)){
                    document.forms[0].submit();
                }else{
                    return false;
                } 
            }
         }else {
            jsp_alerta("A",inserteCampos);
            return false;
         }
       }

    function comprobarObligatorios() {     
        if(document.forms[0].password.value != "" &&
            document.forms[0].nuevaPassword.value != "" ){
            return true;
        }else{
            return false;
        }
    }
     
        function inicializar() {
            //document.all.tlogon.style.marginTop = calcularPosTabla(document.all.tlogon);
          
            $('input[name=nuevaPassword]').attr('placeholder',"<fmt:message key='Escritorio.login.NuevaClave'/>");
            $('input[name=nuevaPassword]').attr('accesskey','u');
            $('input[name=nuevaPassword]').attr('required','required');
                    
            $('input[name=password]').attr('placeholder',"<fmt:message key='Escritorio.login.RepetirClave'/>");
            $('input[name=password]').attr('required','required');
        }
        
        
        $(function(){    
                    if($.browser.msie && $.browser.version <= 9){
                         $('#login').removeClass('text-black').addClass('text-gray');
                         $("[placeholder]").focus(function(){
                             $('#login').removeClass( "text-gray" ).addClass( "text-black" );
                            if($(this).val()==$(this).attr("placeholder")){
                                $(this).val("");
                                }
                             })
                           .blur(function(){
                                if($(this).val()==""){
                                    $(this).val($(this).attr("placeholder"));
                                    $('#login').removeClass('text-black').addClass('text-gray');
                                }
                            }).blur();
                        
                        $("#password").each(function() {
                            var ph_id, phvalor;
                            if (phvalor = $(this).attr('placeholder')) {
                                ph_id='#'+$(this).attr('id');
                                // se cambia el tipo del input para que se muestre el texto del "placeholder"
                                $(ph_id).removeClass( "text-black" ).addClass( "text-gray" );
                                $(ph_id).prop("type", "text");
                              
                                $(this).val(phvalor);
                                $(this).focus(function() {
                                // se vuelve a cambiar el tipo para que no se muestre la contraseña
                                $(ph_id).removeClass( "text-gray" ).addClass( "text-black" );
                                $(ph_id).prop("type", "password");
                                
                                if($('#login').val()==$('#login').attr("placeholder")){
                                    $('#login').removeClass("text-black").addClass("text-gray");
                                }
                                if (phvalor ==$(this).val()) {
                                    return $(this).val('');
                                }
                                });
                                $(this).blur(function() {
                                    if ($(this).val()==='' || $(this).val()===phvalor) {
                                        $(ph_id).removeClass( "text-black" ).addClass( "text-gray" );
                                        $(ph_id).prop("type", "text");
                                        $(this).val(phvalor);
                                        
                                        if($('#login').val()!=$('#login').attr("placeholder")){
                                            $('#login').removeClass("text-gray").addClass("text-black");
                                        }
                                    }
                                });
                            }
                        });
                        
                           $("#nuevaPassword").each(function() {
                            var ph_id, phvalor;
                            if (phvalor = $(this).attr('placeholder')) {
                                ph_id='#'+$(this).attr('id');
                                // se cambia el tipo del input para que se muestre el texto del "placeholder"
                                $(ph_id).removeClass( "text-black" ).addClass( "text-gray" );
                                $(ph_id).prop("type", "text");
                              
                                $(this).val(phvalor);
                                $(this).focus(function() {
                                // se vuelve a cambiar el tipo para que no se muestre la contraseña
                                $(ph_id).removeClass( "text-gray" ).addClass( "text-black" );
                                $(ph_id).prop("type", "password");
                                
                                if($('#login').val()==$('#login').attr("placeholder")){
                                    $('#login').removeClass("text-black").addClass("text-gray");
                                }
                                if (phvalor ==$(this).val()) {
                                    return $(this).val('');
                                }
                                });
                                $(this).blur(function() {
                                    if ($(this).val()==='' || $(this).val()===phvalor) {
                                        $(ph_id).removeClass( "text-black" ).addClass( "text-gray" );
                                        $(ph_id).prop("type", "text");
                                        $(this).val(phvalor);
                                        
                                        if($('#login').val()!=$('#login').attr("placeholder")){
                                            $('#login').removeClass("text-gray").addClass("text-black");
                                        }
                                    }
                                });
                            }
                        });
                        ///no enviar el formulario con los campos del placeholder
                        $('form').submit(function() {
                            $("input[type=text]").each(function() {
                                if ($(this).val() === $(this).attr('placeholder')) {
                                    $(this).val('');
                                }
                            });
                            $("input[type=password]").each(function() {
                                if ($(this).val() === $(this).attr('placeholder')) {
                                    $(this).val('');
                                }
                            });
                        });
                    }
                }); 
        
        function escritorio(){
            <c:if test="${param.escritorio eq 'yes'}" >
                window.open("<html:rewrite page="/jsp/escritorio/close.html"/>", '_self');
                window.open('<%=request.getContextPath()%>/jsp/escritorio/escritorio.jsp?escritorio=yes', '',
                        'toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=no,width=' +
                        window.outerWidth + ',height=' + window.outerHeight + ',resizable=yes,fullscreen=yes,top=' + 
                        window.screenY + ',left=' + window.window.screenX);
            </c:if>
            <c:if test="${param.escritorio eq 'no'}" >
                 window.open("<html:rewrite page="/jsp/escritorio/close.html"/>", '_self');
                 window.open('<%=request.getContextPath()%>/jsp/escritorio/modificarPass.jsp', '',
                        'toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=no,width=' +
                        window.outerWidth + ',height=' + window.outerHeight + ',resizable=yes,fullscreen=yes,top=' + 
                        window.screenY + ',left=' + window.window.screenX');
            </c:if>
        }
       
    </script>

        <!-- ================== BEGIN BASE CSS STYLE ================== -->
        <link rel="stylesheet" href="<c:url value='/login/css/reseter.css'/>">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700" />
        <link rel="stylesheet" href="<c:url value='/login/css/color_admin/assets/plugins/jquery-ui/themes/base/minified/jquery-ui.min.css'/>">
        <link rel="stylesheet" href="<c:url value='/login/css/color_admin/assets/plugins/bootstrap/css/bootstrap.min.css'/>">
        <link rel="stylesheet" href="<c:url value='/login/css/color_admin/my/css/bootstrap.css'/>">
        <link rel="stylesheet" href="<c:url value='/login/css/color_admin/assets/plugins/font-awesome/css/font-awesome.min.css'/>">
        <link rel="stylesheet" href="<c:url value='/login/css/color_admin/my/css/font-awesome.css'/>">
        <link rel="stylesheet" href="<c:url value='/login/css/color_admin/assets/css/animate.min.css'/>">
        <link rel="stylesheet" href="<c:url value='/login/css/color_admin/assets/css/style.min.css'/>">
        <link rel="stylesheet" href="<c:url value='/login/css/color_admin/my/css/style.css'/>">
        <link rel="stylesheet" href="<c:url value='/login/css/color_admin/assets/css/style-responsive.min.css'/>">
        <link rel="stylesheet" href="<c:url value='/login/css/color_admin/assets/css/theme/orange.css" id="theme'/>">
        <link rel="stylesheet" href="<c:url value='/login/css/styles.css'/>">
        <!-- ================== END BASE CSS STYLE ================== -->

        <!-- ================== BEGIN PAGE LEVEL CSS ================== -->
        <!-- ================== END PAGE LEVEL CSS ================== -->

        <!-- ================== BEGIN BASE JS ================== -->
        <script type="text/javascript" src="<c:url value='/login/css/color_admin/assets/plugins/pace/pace.min.js'/>"></script>
        <!-- ================== END BASE JS ================== -->
    </head>
    <body class="pace-top"  onLoad="escritorio();">
        <div id="page-loader" class="fade in"><span class="spinner"></span></div>

        <div class="login-cover hide">
            <div class="login-cover-image">
                <%--<img alt="" src="<c:url value='/login/img/fondo.jpg'/>" data-id="login-cover-image" />--%>
            </div>
        </div>

        <div id="page-container" class="fade">
            <div class="height-150 p-t-20"></div>
            
            <div class="login login-v2 m-b-0 m-t-0 overflow-hidden border-blue-primary" data-pageload-addclass="animated fadeIn" style="visibility:<%=loginSIGP%>">
                <div class="bg-white text-center m-0 p-20">
                    <p class="text-center m-0"><img src="<c:url value='/images/logo-login.png'/>" alt="Logo admin" /></p>
                </div>

                <div class="login-content bg-blue-primary">
                    <html:form  action="/ModificarContrasenhaCaducada.do" target="_self">
                         <html:hidden property="login"/>
                         <input type="hidden" name="renovacion" id="renovacion" value="<%=renovacion%>"/>
                        <div class="form-group has-feedback">
                            <span class="fa fa-lock form-control-feedback"></span>
                          
                            <html:password  property="nuevaPassword" styleId="nuevaPassword" styleClass="form-control bg-white text-black" tabindex="2" redisplay="<%=false%>" />
                        </div>
                        <div class="form-group has-feedback">
                            <span class="fa fa-lock form-control-feedback"></span>
                       
                            <html:password  property="password" styleId="password" styleClass="form-control bg-white text-black" tabindex="2" redisplay="<%=false%>" />
                        </div>
                        <div class="form-group m-0">
                            <button id="entrar" class="btn btn-block btn-lg bg-primary" onclick="javascript:Aceptar(event);" name="login_entrar" accesskey="e" tabindex="3">
                                <fmt:message key="logon.entrar"/>
                            </button>
                        </div>                 
                    </html:form>
                    <div style="font-size:14px">
                        <fmt:message key="Escritorio.login.ContraseñaCaducada"/>
                    </div>
                           <div style="font-size:14px">
                         <fmt:message key="Escritorio.login.IntroduzcaNuevaPass"/>
                    </div>
            <div style="width:100%;text-align:center; padding: 15px">
                <logic:messagesPresent>
                     <html:messages id="msg">
                         <p class="f-s-1-7-r">
                             <c:out value="${msg}"/>
                         </p>
                     </html:messages>
                </logic:messagesPresent>
                <c:if test="${requestScope.error eq 'sesionCaduca'}" >
                        <p class="f-s-1-7-r color-secondary">
                            <fmt:message key="Escritorio.login.CaducoSesion"/>
                        </p>
                </c:if>
             </div>
         </div>
        </div> <!-- /page-container -->

        <!-- ================== BEGIN BASE JS ================== -->
        <script type="text/javascript" src="<c:url value='/login/css/color_admin/assets/plugins/jquery/jquery-1.9.1.min.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/login/css/color_admin/assets/plugins/jquery/jquery-migrate-1.1.0.min.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/login/css/color_admin/assets/plugins/jquery-ui/ui/minified/jquery-ui.min.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/login/css/color_admin/assets/plugins/bootstrap/js/bootstrap.min.js'/>"></script>
        <!--[if lt IE 9]>
                <script type="text/javascript" src="<c:url value='/login/css/color_admin/assets/crossbrowserjs/html5shiv.js'/>"></script>
                <script type="text/javascript" src="<c:url value='/login/css/color_admin/assets/crossbrowserjs/respond.min.js'/>"></script>
                <script type="text/javascript" src="<c:url value='/login/css/color_admin/assets/crossbrowserjs/excanvas.min.js'/>"></script>
        <![endif]-->
        <script type="text/javascript" src="<c:url value='/login/css/color_admin/assets/plugins/slimscroll/jquery.slimscroll.min.js'/>"></script>
        <!-- ================== END BASE JS ================== -->
   
        <!-- ================== BEGIN PAGE LEVEL JS ================== -->
        <!-- ================== END PAGE LEVEL JS ================== -->

        <script type="text/javascript" src="<c:url value='/login/css/color_admin/my/js/apps/apps.cnf.js'/>"></script>
   
    <script type="text/javascript">
        inicializar();
    </script>
 </body>
</html>