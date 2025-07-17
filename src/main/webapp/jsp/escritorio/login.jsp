<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
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
        <meta charset="iso-8859-1" />
        <jsp:include page="/jsp/escritorio/tpls/app-constants.jsp" />
        
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

        %>
        
        <title><fmt:message key="logon.title"/></title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
        <jsp:include page="/jsp/plantillas/Metas.jsp" />
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-1.9.1.min.js'/>"></script>

        <script type='text/javascript'>
            function Cancelar() {window.close();}
            function inicializar(){
                if ('<%=loginSIGP%>'=="visible") {
                    document.forms[0].login.focus();
                    $('input[name=login]').attr('placeholder',"<fmt:message key='Escritorio.login.Login'/>");
                    $('input[name=login]').attr('accesskey','u');
                    $('input[name=login]').attr('required','required');
                    
                    $('input[name=password]').attr('placeholder',"<fmt:message key='Escritorio.login.Clave'/>");
                    $('input[name=password]').attr('required','required');
                  
                   // para IExplorer 9 
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
                        
                        $("input[type=password]").each(function() {
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
           }
        }
            
            
            
            function escritorio(){
                <c:if test="${param.escritorio eq 'yes'}" >
                   /* window.open("<html:rewrite page="/jsp/escritorio/close.html"/>", '_self');
                    window.open('<%=request.getContextPath()%>/jsp/escritorio/escritorio.jsp?escritorio=yes', '',
                            'toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=no,width=' +
                            window.outerWidth + ',height=' + window.outerHeight + ',resizable=yes,fullscreen=yes,top=' + 
                            window.screenY + ',left=' + window.window.screenX);*/
                             window.location='<%=request.getContextPath()%>/jsp/escritorio/escritorio.jsp;jsessionid=<%=session.getId()%>?escritorio=yes';
                  
                </c:if>
                <c:if test="${param.escritorio eq 'no'}" >
                     window.open("<html:rewrite page="/jsp/escritorio/close.html"/>", '_self');
                     window.open('<%=request.getContextPath()%>/jsp/escritorio/login.jsp', '',
                             'toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=no,width=' +
                            window.outerWidth + ',height=' + window.outerHeight + ',resizable=yes,fullscreen=yes,top=' + 
                            window.screenY + ',left=' + window.window.screenX);
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
        <!-- ================== END BASE JS ================== -->
    </head>

    <body class="pace-top" onLoad="escritorio();">        
        <div id="page-loader" class="fade in"><span class="spinner"></span></div>

        <div class="login-cover hide">
            <div class="login-cover-image">
                <img alt="" src="<c:url value='/login/img/fondo.jpg'/>" data-id="login-cover-image" />
            </div>
        </div>

        <div id="page-container" class="fade">
            <div class="height-150 p-t-20">						
					<img  id="logoFlexiaLogin" alt="Logo Flexia" src="<c:url value='/images/logo_tr.png'/>"/>				
			</div>            
            <div class="login login-v2 m-b-0 m-t-0 overflow-hidden border-blue-primary" data-pageload-addclass="animated fadeIn" style="visibility:<%=loginSIGP%>">
                <div class="bg-white text-center m-0 p-20">
                    <p class="text-center m-0"><img src="<c:url value='/images/logo-login.png'/>" alt="Logo admin" /></p>
                </div>

                <div class="login-content bg-blue-primary">
                    <html:form action="/logon.do">
                        <div class="form-group has-feedback">
                            <span class="fa fa-user form-control-feedback"></span>
                            <html:text property="login" styleId="login" styleClass="form-control bg-white text-black" tabindex="1" size="30"  />
                        </div>
                        <div class="form-group has-feedback">
                            <span class="fa fa-lock form-control-feedback"></span>
                            <html:password property="password" styleId="password" styleClass="form-control bg-white text-black" tabindex="2" redisplay="<%=false%>" />
                        </div>

                        <div class="form-group m-0">
                            <button id="entrar" class="btn btn-block btn-lg bg-primary" type="submit" name="login_entrar" accesskey="e" tabindex="3"><fmt:message key="logon.entrar"/></button>
                        </div>
                    </html:form>  
                         </div>  
                        <div align="right" class="bg-blue-primary" style="padding-right: 5px;">

                         V. <%=version%>
                         </div>
                </div> 
            </div> 
            <div style="width:100%;text-align:center; padding: 15px">
                <logic:messagesPresent>
                     <html:messages id="msg">
                         <p class="f-s-1-7-r color-secondary"><c:out value="${msg}"/></p>
                     </html:messages>
                </logic:messagesPresent>
                <c:if test="${requestScope.error eq 'sesionCaduca'}" >
                        <p class="f-s-1-7-r color-secondary"><fmt:message key="Escritorio.login.CaducoSesion"/></p>
                </c:if>
             </div>
        </div> <!-- /page-container -->
        <footer>
            
        </footer>
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