<%@page import="es.altia.flexia.viafirma.ConfigureUtil"%>
<%@page import="org.viafirma.cliente.ViafirmaClient"%>
<%@page import="org.viafirma.cliente.ViafirmaClientFactory"%>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<% String titulo = (String)request.getParameter("titulo");%>
<html>
    <head>
        <title>
            <% if("firmarTexto".equals(titulo)){%>
            
            <%}else if("firmarFichero".equals(titulo)){%>
            
            <%}else if("autenticar".equals(titulo)){%>
            
            <%}else if("cofirmaFichero".equals(titulo)){%>
                    
            <%}else if("cofirmaTexto".equals(titulo)){%>
                    
            <%}%>	
        </title>
        <script type="text/javascript">
            var cierreManual=true;
            <%String op = request.getParameter("op")!=null?(String)request.getParameter("op"):"";%>
            <%if(!"validar".equals(op)){%>
                var arguments = self.parent.opener.xanelaAuxiliarArgs;
                var op="";
                var texto="";
                var hashFichero="";
                var firmaAntigua ="";
                var time = new Date().getTime();
			
                    op = arguments.op;
                    texto = arguments.texto;
                    hashFichero = arguments.hashFichero;
                    firmaAntigua = arguments.firmaAntigua;
				
                function firmasCallBack(numFirmas, firmas){
                    cierreManual=false;
                    var valorRetorno = new Object();
                    valorRetorno.error="";
                    valorRetorno.firmas=firmas;
                    self.parent.opener.retornoXanelaAuxiliar(valorRetorno);
                }//firmasCallback
				
                function firmaCancelada(mensaje){
                    var valorRetorno = new Object();
                    valorRetorno.error=mensaje;
                    valorRetorno.firmas="";
                    self.parent.opener.retornoXanelaAuxiliar(valorRetorno);
                }//firmaCancelada
                
            <%}%>
			
            function cerrar(){
                <%if(!"validar".equals(op)){%>
                    if(cierreManual){
                        var valorRetorno = new Object();
                        valorRetorno.error="Firma Cancelada por el usuario";
                        valorRetorno.firmas="";
                        self.parent.opener.retornoXanelaAuxiliar(valorRetorno);
                    }//if(cierreManual)
                <%}%>				
            }//cerrar
        </script>
    </head>
    
    <body onUnLoad="cerrar()">
        <div id="divIFrame">
            <%
                if("validar".equals(op)){
                    if (!ViafirmaClientFactory.isInit()){
                        ConfigureUtil.init();
                        // Iniciamos el cliente de Viafirma
                    }//if (!ViafirmaClientFactory.isInit())
                    String usuarioLogin = (String)request.getParameter("usuarioLogin");
                    request.setAttribute("usuarioLogin",usuarioLogin);	
                    ViafirmaClient viafirmaClient = ViafirmaClientFactory.getInstance();
                    // Iniciamos la autenticación indicando la uri de retorno.
                    viafirmaClient.solicitarAutenticacion(request, response,"/viafirmaClientIntranetServlet?op="+op+"&usuarioLogin=" + usuarioLogin);
                }//if("validar".equals(op))
            %>
        </div>
    </body>
    <%if(!"validar".equals(op)){%>
        <script>
            var div = document.getElementById("divIFrame");
            div.innerHTML = "<iframe id='ifFirma' src='iFrameViaFirma.jsp?op="+op+"&texto="+texto+"&hashFichero="+hashFichero+"&firmaAntigua="+firmaAntigua+"&time="+time+"' width='100%' height='100%''>En caso de error del iframe</iframe>";
        </script>
    <%}%>
</html>