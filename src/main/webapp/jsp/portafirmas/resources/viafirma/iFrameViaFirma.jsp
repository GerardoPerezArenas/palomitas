<%@page import="java.util.Iterator"%>
<%@page import="java.util.Collection"%>
<%@page import="es.altia.flexia.viafirma.ConfigureUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.viafirma.cliente.firma.TypeFormatSign"%>
<%@page import="org.viafirma.cliente.firma.TypeFile"%>
<%@page import="org.viafirma.cliente.ViafirmaClient"%>
<%@page import="org.viafirma.cliente.ViafirmaClientFactory"%>
<%@page import="org.viafirma.cliente.vo.FirmaInfoViafirma"%>
<%@page import="org.viafirma.cliente.exception.CodigoError"%>
<%@page import="org.bouncycastle.util.encoders.Base64"%>

<html>
    <head>

        <%
            String op = (String)request.getParameter("op");

            FirmaInfoViafirma firma = new FirmaInfoViafirma();
            if(request.getAttribute("firma") != null){
                    firma = (FirmaInfoViafirma) request.getAttribute("firma");
            }
            
            String idioma = "Espanol";
            if (session.getAttribute("idioma") == null){
                    session.setAttribute("idioma",idioma);
            }else{
                    idioma = (String) session.getAttribute("idioma");
            }
            
            String error = null;
            String cancel= null;
            if (request.getAttribute("codError") != null){
                    error =String.valueOf(((CodigoError) request.getAttribute("codError")).getCodigo());
            }
            if (request.getAttribute("cancel") != null){
                    cancel = (String) request.getAttribute("cancel");
            }

        %>
        
        <script LANGUAGE="JavaScript">
            <%String firmasString = firma.getSignId();%>
		function btnCerrar_Click(){
                    <%if(firmasString!=null){
                        String firmas[] = firmasString.split(";");
                    %>	
                    var firmas = new Array(<%=firmas.length%>);
                    <% int contador=0;
                        while(contador<firmas.length){%>
                            firmas[<%=contador%>]="<%=firmas[contador]%>";
                            <%contador++;%>
                        <%}%>
                        parent.firmasCallBack(<%=contador%>,firmas);
                        window.close();
                    <%}%>	
                    <%if(cancel!=null){%>
                        parent.firmaCancelada("<%=cancel%>");
                        window.close();
                    <%}%>	
		}//btnCerrar_Click
        </script>
        
	<title>::::: Flexia :::::</title>
        <link href="/viafirmaStyle.css" rel=stylesheet type="text/css">

    </head>
    <body>
        <%
            if (!ViafirmaClientFactory.isInit()){
                // Iniciamos el cliente de Viafirma
		ConfigureUtil.init();
            }//if (!ViafirmaClientFactory.isInit())
            ViafirmaClient viafirmaClient = ViafirmaClientFactory.getInstance();

            String idTemporal="";
            List<String> listaFirmas = new ArrayList<String>();
            //System.out.println("OP " + op);
            if(op!=null && "firmarTexto".equals(op)){
                //Datos documento a firmar
		String texto = (String)request.getParameter("texto");
		
		if (texto!=null){
                    request.setAttribute("texto",texto );
                    idTemporal = viafirmaClient.prepareFirmaWithTypeFileAndFormatSign("texto.pdf",TypeFile.PDF,TypeFormatSign.CMS,texto.getBytes());
                    //Solicitamos la firma
                    viafirmaClient.solicitarFirma(idTemporal,request, response,  "/viafirmaClientServlet?op=" + op);
                }//if (texto!=null)
            }//if(op!=null && "firmarTexto".equals(op))
            
            if(op!=null && "firmarHashFichero".equals(op)){
                //Datos documento a firmar
                String hashFichero = (String)request.getParameter("hashFichero");
                if (hashFichero!=null && !hashFichero.equals("")){
                    request.setAttribute("hashFichero",hashFichero);
                    //byte[] bytes = Base64.decode(hashFichero);
                    byte[] bytes = hashFichero.getBytes();
                    idTemporal = viafirmaClient.prepareFirmaWithTypeFileAndFormatSign("archivo",TypeFile.bin,TypeFormatSign.CMS,bytes);
                    //Solicitamos la firma
                    viafirmaClient.solicitarFirma(idTemporal,request, response,"/viafirmaClientServlet?op=" + op);
		}//if (hashFichero!=null && !hashFichero.equals(""))
            }else
                if(op!=null && "cofirmarFichero".equals(op)){
                    //Datos documento a firmar
                    String firmaAntigua = (String) request.getParameter("firmaAntigua");
                    if (firmaAntigua!=null && !firmaAntigua.equals("")){
                        //Solicitamos la firma
                        viafirmaClient.solicitarFirma(firmaAntigua,request, response,"/viafirmaClientServlet?op=" + op);
                    }//if (firmaAntigua!=null && !firmaAntigua.equals(""))
		}else if(op!=null && "cofirmarTexto".equals(op)){
                    //Texto a firmar
                    String firmaAntigua = (String) request.getParameter("firmaAntigua");			
                    if (firmaAntigua!=null && !firmaAntigua.equals("")){		
                        //Solicitamos la firma
                        viafirmaClient.solicitarFirma(firmaAntigua,request, response,"/viafirmaClientServlet?op=" + op);
                    }//if (firmaAntigua!=null && !firmaAntigua.equals(""))
		}//if
        %>
        
        <div id="contenido">
            <div id="cuerpo">
                <div class="texto autenticacion">
                    <%
                        if (request.getAttribute("codError") != null){%>  
                            <p>Se ha producido el error : <%=error%> </p>
                            <input type="button" class="boton" value="Cerrar" onclick="btnCerrar_Click();">
                    <%  
                        }
                        if (firma.getSignId() != null && !firma.getSignId().equals("") || cancel!=null){
                    %>
                        <script>
                            btnCerrar_Click();
                        </script>
                    <%}%>
                </div>
            </div>
        </div>
    </body>
</html>
