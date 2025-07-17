<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@ page import="es.altia.common.service.config.Config" %>
<html:html locale="true">
    <fmt:setLocale value='${initParam["sLocaleInicial"]}' scope="session"/>
    <head><jsp:include page="/jsp/escritorio/tpls/app-constants.jsp" />
        
        <%
            Config m_Config = ConfigServiceHelper.getConfig("authentication");
            String modoAcceso = m_Config.getString("Auth/accessMode");
            String urlRedireccion = "";
            if(modoAcceso.equals("cas")) {
                urlRedireccion = m_Config.getString("Auth/"+modoAcceso+"/urlRedireccion");
            }

            ClassLoader classLoader = this.getClass().getClassLoader();
            java.io.InputStream inputStream = classLoader.getResourceAsStream("version.properties");
            java.util.Properties properties = new java.util.Properties();
            properties.load(inputStream);
            inputStream.close();
            java.util.HashMap parameters = new java.util.HashMap(properties);
            String version = (String) parameters.get("version.aplicacion");
            
            String botonSalir=(String) request.getAttribute("botonSalir");
            String paginaParaSalir=(String) request.getAttribute("paginaParaSalir");
            

        %>
        
    <title><fmt:message key="logon.title"/></title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type='text/javascript'>

          
            function inicializar() {
                var opcion ='<c:out value="${param.opcion}"/>';
                if(opcion=="cerrarSesion"){
                    console.log("opcion: "+opcion);
                    window.top.location = "<%= urlRedireccion%>";
                }
                document.all.tlogon.style.marginTop = calcularPosTabla(document.all.tlogon);
           }

            function escritorio(){
                pleaseWait('off');
                <c:if test="${param.escritorio eq 'yes'}" >
                /*window.open("<html:rewrite page="/jsp/escritorio/close.html"/>", '_self');
                window.open('<%=request.getContextPath()%>/jsp/escritorio/escritorio.jsp?escritorio=yes', '',
                    'toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=no,width=' +
                    window.outerWidth + ',height=' + window.outerHeight + ',resizable=yes,fullscreen=yes,top=' + 
                    window.screenY + ',left=' + window.window.screenX);*/
                 window.location='<%=request.getContextPath()%>/jsp/escritorio/escritorio.jsp?escritorio=yes';
                </c:if>
                <c:if test="${param.escritorio eq 'no'}" >
                window.open("<html:rewrite page="/jsp/escritorio/close.html"/>", '_self');
                window.open('<%=request.getContextPath()%>/jsp/escritorio/login.jsp', '',
                    'toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=no,width=' +
                    window.outerWidth + ',height=' + window.outerHeight + ',resizable=yes,fullscreen=yes,top=' + 
                    window.screenY + ',left=' + window.window.screenX);
                </c:if>
            }
            
            function pleaseWait(valor) {
                if(valor=='on') document.all.hidepage.style.visibility = 'visible';
                else if(valor=='off') document.all.hidepage.style.visibility = 'hidden';  
            }

        </script>
    </head>
    <BODY class="loginBody"  onLoad="escritorio();">
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
        	<!--jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/-->
            <jsp:param name='cargaDatos' value='Cargando datos.....'/>
        </jsp:include>
            <center>
            <form action="<c:url value='/welcome.do'/>">
                <table width="350px" height="240px" border="0" id="tlogon">
                    <tr>
                        <td>
                            <!-- Cabecera. -->
                            <table id="tablalogo" name="tblLogo" cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td width="299px" height="57px">
                                        <img src="<c:url value='/images/login.jpg'/>" border="0px">
                                    </td>
                                </tr>
                            </table>
                            <!-- Fin cabecera. -->
                    <!-- Datos de usuario y clave. -->
                            <table cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td style="width: 299px; height: 168px">
                                        <logic:messagesPresent>
                                            <html:messages id="msg">
                                                <c:out value="${msg}"/>
                                            </html:messages>
                                        </logic:messagesPresent>
                                    </td>
                                </tr>
                            </table>
                            <!-- Fin datos de usuario y clave. -->
                            <!-- Botones. -->
                            <div STYLE=" height:0px;" class="botoneraPrincipal">
                               <%     if(botonSalir!=null){

                                %>  
                                <html:button property="submit" value="Salir" styleClass="botonGeneral" onclick="pulsarSalir()"/>
                             <%     

                             }
                             %>  
                            </div>
                            <!-- Fin botones. -->                            
                        </td>
                    </tr>
                </table>
            </form>
            <script type="text/javascript">
                inicializar();
                
                function pulsarSalir(){
                 
                   var ajax = getXMLHttpRequest();
                   var CONTEXT_PATH = '<%=request.getContextPath()%>';
                   var parametros="";
                   
                   
                    if(ajax!=null){
                        var url =  CONTEXT_PATH + "/logout.do";
                        
                        ajax.open("POST",url,false);
                        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
                        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                        ajax.send(parametros);
                        try{
                        //Todo ben ok
                         if (ajax.readyState==4 && ajax.status==200){
                             window.location = "<%=paginaParaSalir%>"; 
                          }else{
                              window.close(); 
                           }

                        }catch(Err){
                            window.close(); 
                        }//           
                    } //ajax !=null     
                   
                    
                }
                
                
                /** 
    Devuelve un objeto XMLHttpRequest
   */
   function getXMLHttpRequest(){
          var aVersions = [ "MSXML2.XMLHttp.5.0",
                "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
                "MSXML2.XMLHttp","Microsoft.XMLHttp"
        ];

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
                        }
            }
        }
    }
                
            </script>
        </center>
    </body>
</html:html>
