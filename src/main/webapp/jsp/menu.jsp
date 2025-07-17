<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %> 
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.ArrayList"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.agora.business.util.LabelValueTO"%>

<%
  String funcionAxuda = "";
  String aplicacion = "";
  String usu = "";
  String entidad = "";
  int idioma = 1;
  String appIco = " fa-pencil";
  String tituloUor = "";
  String tituloUorReducido = "";
  Config m_Config = ConfigServiceHelper.getConfig("common");
  String statusBar = m_Config.getString("JSP.StatusBar");

  UsuarioValueObject usuarioVO = null;
  if(session != null){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
  }

  if (usuarioVO != null){
    aplicacion = usuarioVO.getApp().toUpperCase();
      if(!usuarioVO.isCambioIdioma()){	
        //En Lanbide: aplicacion = new String(aplicacion.getBytes(), "ISO-8859-1");
        //Pero en local...	
        aplicacion = new String(aplicacion.getBytes("ISO-8859-1"), "UTF-8");	
    } 	
    usuarioVO.setCambioIdioma(false);
    usu = usuarioVO.getNombreUsu();
    entidad = usuarioVO.getEnt();
    entidad=new String(entidad.getBytes("ISO-8859-1"),"UTF-8");
    idioma = usuarioVO.getIdioma();
    appIco = usuarioVO.getAppIco()==""?"fa-play":usuarioVO.getAppIco();
    if (usuarioVO.getAppCod()==4)
        funcionAxuda = "function axuda() {var manual = '"+ 
            (usuarioVO.isMultipleIdioma() && idioma == 2?(request.getContextPath() + "/jsp/sge/ver_pdf.jsp?fichero=/pdf/Manual_GestionExptes.pdf"):
            (request.getContextPath() + "/jsp/sge/ver_pdf.jsp?fichero=/pdf/Manual_XestionExptes.pdf")) + 
            "'; ventanaInforme = window.open(manual, 'informe', 'width=800px,height=550px,status="+ statusBar + ",toolbar=no'); " +
             "ventanaInforme.focus(); }";
    else if (usuarioVO.getAppCod()==1) {
        funcionAxuda = "function axuda() {var manual = '"+ 
            (usuarioVO.isMultipleIdioma() && idioma == 2?(request.getContextPath() + "/jsp/sge/ver_pdf.jsp?fichero=/pdf/ManualRexistro.pdf"):
            (request.getContextPath() + "/jsp/sge/ver_pdf.jsp?fichero=/pdf/ManualRegistro.pdf")) + 
            "'; ventanaInforme = window.open(manual, 'informe', 'width=800px,height=550px,status="+ statusBar + ",toolbar=no'); " +
             "ventanaInforme.focus(); }";
            tituloUor = usuarioVO.getUnidadOrg();
            tituloUor=new String(tituloUor.getBytes("ISO-8859-1"),"UTF-8");
            if (tituloUor.length() > 40)    
                tituloUorReducido = tituloUor.substring(0,40) + "..";
            else
                tituloUorReducido = tituloUor;
    } else
        funcionAxuda = "function axuda() {" +
               (idioma == 1?"jsp_alerta('A','En este momento no se encuentra ayuda disponible');":"jsp_alerta('A','Nestes intres non se atopa axuda dispoñíbel');") +
           "}";
   }
  
  ArrayList idiomas = (ArrayList)session.getAttribute("listaIdiomas");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%= usuarioVO.getIdioma()%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= usuarioVO.getAppCod()%>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/login/css/color_admin/assets/plugins/bootstrap/css/bootstrap.min.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/login/css/color_admin/assets/css/style.min.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/login/css/color_admin/my/css/style.css'/>">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-1.9.1.min.js'/>"></script>
 <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-15">
<script type='text/javascript'>
    var mensajeGuardarCambios='<%=descriptor.getDescripcion("msjExistenDatosGuardar")%>';
    function CambioIdioma(valor){
        try{
            var existe = top.mainFrame.document.forms[0].existenCambiosSinGrabar.value;
            if(irActionConCambios(existe,mensajeGuardarCambios)) {
                var resultado = jsp_alerta("",'<%= descriptor.getDescripcion("g_confirmIdioma")%>');
                if (resultado == 1){
                    //parent.existenCambiosSinGrabar.value=0;
                    top.mainFrame.document.forms[0].existenCambiosSinGrabar.value = 0;
                    document.forms[0].target = "_top";
                    document.forms[0].idioma.value = valor;
                    document.forms[0].action = "<c:url value='/CambioIdioma.do'/>";
                    document.forms[0].submit();
                }
            }
        }catch(Err){                    
            var resultado = jsp_alerta("",'<%= descriptor.getDescripcion("g_confirmIdioma")%>');
            if (resultado == 1){                      
                document.forms[0].target = "_top";
                document.forms[0].idioma.value = valor;
                document.forms[0].action = "<c:url value='/CambioIdioma.do'/>";
                document.forms[0].submit();
            }
        }
    }// CambioIdioma

    function salir(){
        try{                    
            var existe = top.mainFrame.document.forms[0].existenCambiosSinGrabar.value                    
            if(irActionConCambios(existe,mensajeGuardarCambios)) {
            var resultado = jsp_alerta("",'<%= descriptor.getDescripcion("g_confirmSalirApp")%>');
            if (resultado == 1){
                    top.mainFrame.document.forms[0].existenCambiosSinGrabar.value=0;
                document.forms[0].target = '_top';
                document.forms[0].action = "<c:url value='/SalirApp.do?app='/><%= usuarioVO.getAppCod()%>";
                document.forms[0].submit();
                }//if
            }//if
        }catch(Err){                    
            /***  SI OCURRE UN ERROR AL OBTENER EL VALOR DEL CAMPO existenCambiosSinGrabar ***/
            var resultado = jsp_alerta("",'<%= descriptor.getDescripcion("g_confirmSalirApp")%>');

            if (resultado == 1){                         
                document.forms[0].target = '_top';
                document.forms[0].action = "<c:url value='/SalirApp.do?app='/><%= usuarioVO.getAppCod()%>";                        
                document.forms[0].submit();
            }//if                    
        }//catch
    }// salir

        // Permite cambiar el idioma en función del seleccionado en la lista desplegable
        function CambioIdiomaCombo(){

            var resultado = jsp_alerta("",'<%= descriptor.getDescripcion("g_confirmIdioma")%>');
            if(resultado==1) {
                document.forms[0].target = "_top";
                document.forms[0].idioma.value = document.forms[0].selectLanguage.value;
                document.forms[0].action = "<html:rewrite page='/CambioIdioma.do'/>";
                document.forms[0].submit();
            }
        }

    function volverEscritorio() {
        document.forms[0].target = '_top';
        document.forms[0].action = "<c:url value='/SalirApp.do?app='/><%= usuarioVO.getAppCod()%>";
        document.forms[0].submit();
    }

    <%=funcionAxuda%>
</script>
    <html:form action="/MantAnotacionRegistro.do" target="_self" style="display:none">
        <input type="hidden" name="idioma">
    </html:form>
    <div id="menuLateral">
        <!-- Cabecera. -->
        <div id="novoCabeceiro">
            <div id="logo">
                <img alt="Logo" src="<%= request.getContextPath() + usuarioVO.getOrgIco()%>">
            </div>
            <div id="idiomas">
                <div>
                    <%
                        if(idiomas!=null && idiomas.size()>1) {
                    %>
                    <logic:notEmpty name="listaIdiomas" scope="session">
                        <select name="selectLanguage" class="txtnegroboldIdiomas">
                            <logic:iterate name="listaIdiomas" id="elemento">
                                <bean:define id="idIdioma" name="elemento" property="value" type="java.lang.String" />
                                <option value="<bean:write name='elemento' property='value' ignore='true'/>">
                                <bean:write name="elemento" property="label" ignore="true"/>
                                </option>
                            </logic:iterate>
                        </select>
                        <a href="javascript:CambioIdiomaCombo();">
                            <span class="txtnegroboldenlace"> > </span>
                        </a>
                    </logic:notEmpty>
                    <% }
                        
                        if(idiomas!=null && idiomas.size()==1){
                            String etiqueta = ((LabelValueTO)idiomas.get(0)).getLabel();
                            String valor    = ((LabelValueTO)idiomas.get(0)).getValue();
                    %>
                    <a href='javascript: CambioIdioma("<%= valor %>");'>
                        <span class="txtnegrobold" title="Cambio de idioma">
                            <%= etiqueta %>
                        </span>
                    </a>
                    <% } // if %>
                </div>
                <div>
                    <a href='javascript: axuda();'><span class="txtnegrobold" title="Ayuda">
                            <%= descriptor.getDescripcion("etiqAyuda")%>
                        </span>
                    </a>
                </div>
            </div>
            <div id="usuarios">
                <span class="menuInfoSesion">
                    <span class="txtnegrobold">&nbsp;<%= descriptor.getDescripcion("gEtiqFecha")%>:&nbsp;</span>
                    <span class="txtsmallnegro"><script>document.write(fechaHoy());</script></span>
                </span>
                <span class="menuInfoSesion">
                    <span class="txtnegrobold">&nbsp;<%= descriptor.getDescripcion("etiq_usur")%>:&nbsp;</span>
                    <span class="txtsmallnegro"><%=usu%></span>
                </span>
            </div>
            <div id="migas">
				<div id="logoFlexiaMenu">
					<img alt="Logo Flexia" src="<%= request.getContextPath()%>/images/logo_flexia_menu.png"/>                                   
				</div>
                <div style="padding-top: 3px;">
                    <span class="txttitsmallcabecera"><%=entidad%></span><br/>
                    <span class="txttitcabecera">
                        <span class="p-3 border-primary img-circle display-inline-block">
                            <i class="fa <%=appIco%> color-primary bg-primary text-white p-5 img-circle"></i>
                        </span>
                        <%=aplicacion%>
                    </span>
                     </div>
                    <div style="padding-top: 2px;">
                   <%if (tituloUorReducido!=null && !tituloUorReducido.trim().equals("")) {%>
                    <span class="txttitsmallercabecera" title="<%=tituloUorReducido%>">
                            <%=tituloUor%>
                    </span>
                    <%}%>
                </div>
             </div>
            <div id="salir">
                <a id="imAmosar" href="javascript:amosar();" style ="display:none">
                    <span class="fa fa-angle-double-right fa-lg" aria-hidden="true" title="<%= descriptor.getDescripcion("mostrarMenu")%>"></span>
                </a>
                <a id="icoSalir" href="javascript:salir();">
                    <span class="fa fa-sign-out fa-3x" aria-hidden="true" title="<%= descriptor.getDescripcion("ico_SalirApp")%>"></span>
                </a>                                        
                <a id="imAgachar" href="javascript:agachar();">
                    <span class="fa fa-angle-double-left fa-lg" aria-hidden="true" title="<%= descriptor.getDescripcion("ocultarMenu")%>"></span>
                </a>                                        
            </div>
        </div>
        <!-- Fin cabecera. -->
        <div id="contedorMenu" class="contedorMenu">
                <div class="novoMenu">
                    <c:out value="${sessionScope.menuAppUsu}" escapeXml="false"/>
            </div>
        </div>
    </div>
    <script type="text/javascript" charset="iso-8859-1">
        $(".novoMenu div div").css("display","none");
        $(".novoMenu div span").bind({
            click: function(){
                if ($(this).parent().children("div").css("display") == "none")
                    $(this).parent().children("div").slideDown();
                else
                    $(this).parent().find("div").slideUp();
            }
        });
        
        function agachar() {
            $("#novoMenu div div").slideUp();
            $(parent.document.getElementsByName("menu")[0]).animate({width: '25px'},600);
            $(parent.document.getElementsByName("mainFrame")[0]).animate({ padding: '0 0 0 25px' }, 600, 'linear',function(){
                setTimeout(function(){
                    $("#novoCabeceiro div:not(#salir)").css("visibility","hidden");
                    $("#novoCabeceiro #salir").css("padding","0");
                    $("#contedorMenu").css("display","none");
                    $("#imAmosar").css("display","");
                }, 600);
            });
            $("#imAgachar, #icoSalir").css("display","none");
            
        }
        function amosar() {
                $("#imAmosar").css("display","none");
                $("#novoCabeceiro div:not(#salir)").css("visibility","");
                $("#novoCabeceiro").removeAttr('style');
                $("#novoCabeceiro #salir").removeAttr('style');
                $("#contedorMenu").css("display","");
                $(parent.document.getElementsByName("mainFrame")[0]).animate({ padding: '0 0 0 245px' });
                $(parent.document.getElementsByName("menu")[0]).animate({ width: '245px' });
                setTimeout(function(){
                    $(parent.document.getElementsByName("mainFrame")[0]).removeAttr('style');
                    $(parent.document.getElementsByName("menu")[0]).removeAttr('style');
                    $("#imAgachar, #icoSalir").css("display","");
                },600);
        }
    </script>
