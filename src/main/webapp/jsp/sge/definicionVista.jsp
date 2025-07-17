<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="java.util.Vector"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.DefinicionCampoVistaForm"%>
<%@page import="es.altia.agora.business.sge.DefinicionAgrupacionCamposValueObject"%>
<%@page import="es.altia.agora.technical.EstructuraCampo"%>
<%@page import="es.altia.agora.business.sge.DefinicionCampoValueObject"%>
<%@page import="java.text.MessageFormat"%>

<html:html>
    <head>
        
        <TITLE>::: EXPEDIENTES  Definición de vistas:::</TITLE>
        <jsp:include page="/jsp/plantillas/Metas.jsp" />
        <!-- Estilos -->
        <jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
        
                
        <link type="text/css" href="<%=request.getContextPath()%>/css/ui-lightness/jquery-ui-1.10.2.custom.css" rel="stylesheet"/>
        <link type="text/css" href="<%=request.getContextPath()%>/css/ui-lightness/jquery-ui-1.10.2.custom.min.css" rel="stylesheet"/>
        
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">

        <script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-1.9.1.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-ui-1.10.2.custom.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-ui-1.10.2.custom.min.js'/>"></script>
  

        <%
        int idioma=1;
        int apl=1;
        int munic = 0;
            if (session!=null){
                UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
                if (usuario!=null) {
                    idioma = usuario.getIdioma();
                    apl = usuario.getAppCod();
                    munic = usuario.getOrgCod();
                }
            }//if (session!=null)
        
        DefinicionCampoVistaForm vistaForm = new DefinicionCampoVistaForm();
        vistaForm = (DefinicionCampoVistaForm) session.getAttribute("DefinicionCampoVistaForm");
        
        String municipio = Integer.toString(munic);
        String aplicacion = Integer.toString(apl);
        String modoInicio = "";
        if (session.getAttribute("modoInicio") != null) {
            modoInicio = (String) session.getAttribute("modoInicio");
            session.removeAttribute("modoInicio");
        }
        String lectura = "";
        if (session.getAttribute("lectura") != null) {
            lectura = (String) session.getAttribute("lectura");
            session.removeAttribute("lectura");
        }
        %>

        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

        <style type="text/css">
            
            .componenteCampo{
                border-top: 1px solid #1766A7;
                border-bottom: 1px solid #1766A7; 
                border-right: 1px solid #1766A7; 
                border-left: 1px solid #1766A7;
                
                padding-top: 2px; 
                 padding-bottom: 10px; 
                
                font-family: Arial, Verdana, Helvetica, sans-serif;
                font-style:normal;
                font-variant: normal ;
                font-size: 12px;
                text-align:center;
                color: #000000;
            
                display:inline-block;
                background-color:#FDFAFA;
                opacity:0.8;
                filter:alpha(opacity=80);               
                width: 33%;

   
	}
        
        .selected {
	background-color: #ECB;
	border-color: #B98;
	}
        
        #selectable .ui-selecting { background: #FECA40; }
        #selectable .ui-selected { background: #F39814; color: white; }
        
        .overlap   { pointer-events: none; border: 1px solid black; margin: -1px; }
                                           
        </style>
        
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>

        <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
    
        <script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>

        <script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
        
        <SCRIPT type="text/javascript">
            var listaAgrupaciones = new Array();
            var listaCampos = new Array();
            var listaCoordenadasCampos = new Array();
            
            var cont1 = 0;
            var cont2 = 0;
            
            <logic:iterate id="agrupacion" name="DefinicionCampoVistaForm" property="agrupacionesCampos">
                var agrupacion = new Array();
                    agrupacion[0] = '<bean:write name="agrupacion" property="codAgrupacion" />';
                    agrupacion[1] = '<bean:write name="agrupacion" property="descAgrupacion" />';
                    agrupacion[2] = '<bean:write name="agrupacion" property="ordenAgrupacion" />';
                listaAgrupaciones[cont1] = agrupacion;
                cont1++;
            </logic:iterate>
            
            <logic:iterate id="campo" name="DefinicionCampoVistaForm" property="listaCampos">
                var campo = new Array();
                    campo[0] = '<bean:write name="campo" property="codCampo"/>';
                    campo[1] = '<bean:write name="campo" property="descCampo"/>';
                    campo[2] = '<bean:write name="campo" property="codTipoDato"/>';
                    campo[3] = '<bean:write name="campo" property="codAgrupacion"/>';
                    campo[4] = '<bean:write name="campo" property="posX"/>';
                    campo[5] = '<bean:write name="campo" property="posY"/>';
                    campo[6] = '<bean:write name="campo" property="activo"/>';
                    campo[7] = '<bean:write name="campo" property="oculto"/>';
                    campo[8] = '<bean:write name="campo" property="tamano"/>';
                listaCampos[cont2] = campo;
                cont2++;
            </logic:iterate>
            
            function inicializar() { 
                var error = <%=vistaForm.getErrorCargando()%>;
                if(error == false || error == "false"){
                    var cargaCampo="<%=request.getParameter("cargaCampo")%>";
                    document.forms[0].desdeProcedimiento.value="<%=request.getParameter("desdeProcedimiento")%>";
                    if (cargaCampo=="si") {
                        
                    }else{
                        mostrarCapasBotones('capaBotones1');

                        <% if("si".equals(lectura)) { %>
                            mostrarCapasBotones('capaBotones2');
                            desactivarFormulario();
                            var botonSalir = [document.forms[0].cmdSalir2];
                            habilitarGeneral(botonSalir);
                        <%}else{%>
                            for (i=0; i < listaCampos.length; i++) {
                                var campo = new Array();
                                    campo = listaCampos[i];
                                var nombreCampo = "#campo_" + campo[0];
                                if( campo[7]!='SI')
                                generarCampoDraggable(nombreCampo,campo[3]);                              
                            }//for (i=0; i < listaCampos.length; i++) 
                        <%}%>
                        colocarCampos();
                    }//if (cargaCampo=="si") 
                }else{
                    jsp_alerta("A",'<%=descriptor.getDescripcion("getiq_sinAgrup")%>');
                    self.parent.opener.retornoXanelaAuxiliar();
                }//if(error == false)
            }//inicializar
            
            function colocarCampos(){
                //Colocamos los campos en funcion de sus coordenadas
                 for (i=0; i < listaCampos.length; i++) {
                    var campo = new Array();
                        campo = listaCampos[i];
                    var nomCampo = "campo_" + campo[0];
                    var nomAgrupacion = "#capaDatosSuplementarios_" + campo[3];
                    var posX = campo[4];
                    var posY = campo[5];
                    var campoActivo = campo[6];
                    var campoOculto = campo[7];
                    var tamanoCampo = campo[8];
                    if(posX != undefined && posX != null && posX != "" && posX !=  " "){
                        if(posY != undefined && posY != null && posY != "" && posY !=  " "){
                            if(posX != 0 || posY != 0){
                                if(document.getElementById(nomCampo)){
                                    var posParent = $('#'+nomCampo).offsetParent();
                                    posX = parseInt(posX) + parseInt(posParent.offset().left);
                                    posY = parseInt(posY) + parseInt(posParent.offset().top);
                                    //alert("nomCampo =" + nomCampo + " x = " + posX + " y = " + posY);
                                    $('#'+nomCampo).offset({ top: posY, left: posX, of: nomAgrupacion});
                                    $('#'+nomCampo).html(generarCampoVista(campo[1],campo[2],campo[8]));
                                    //alert("nomCampo = " + nomCampo + " posX = " + posX + " posY = " + posY);
                                    //$('#'+ nomCampo).css({top: posY, left: posX});
                                }//if(document.getElementById(nomCampo))
                            }else{
                                $('#'+nomCampo).html(generarCampoVista(campo[1],campo[2],campo[8]));
                            }//if(posX != 0 && posY != 0)
                        }//if(posY != undefined && posY != null && posY != "" && posY !=  " ")
                    }//if(posX != undefined && posX != null && posX != "" && posX !=  " ")
                 }//for (i=0; i < listaCampos.length; i++)
            }//colocarCampos()
            
            function ocultarCampo(nomCampo){
                document.getElementById(nomCampo).display = 'none';
            }//ocultarCampo
            
            function generarCampoVista(nomCampo, tipoCampo,tamanoCampo){  
                var componente = ""; 
                if (tipoCampo == 1 || tipoCampo == 8){
                    //Numerico / Numerico calculado
                    componente = '<table width="100%" border="0px"  cellpadding="1px"><tr><td class="etiqueta" width="120px" align="left">' 
                        + nomCampo + ':</td><td class="columnP" align="left">'
                        +'<input type="text" name="' + nomCampo + '" ' + 'id="' + nomCampo + '" class="inputTexto" disabled=true size="20"/>'
                        +'</td></tr></table>';
                }else if (tipoCampo == 2){
                    //Texto
                    
                    componente = '<table width="100%" border="0px"  cellpadding="1px"><tr><td class="etiqueta" width=10%"  align="left">' 
                        + nomCampo + ':</td><td class="columnP" align="left">'
                        +'<input type="text" name="' + nomCampo + '" ' + 'id="' + nomCampo + '" class="inputTexto" disabled=true style="width:90%;"/>'
                        +'</td></tr></table>';
                }else if (tipoCampo == 3 || tipoCampo == 9){
                    //Fecha / Fecha calculada
                    componente = '<table width="100%" border="0px" cellpadding="1px"><tr><td class="etiqueta" width="120px" align="left">' 
                        + nomCampo + ':</td><td class="columnP" align="left">'
                        + '<input type="text" name="' + nomCampo + '" ' + 'id="' + nomCampo + '" class="inputTexto" disabled=true size="10"/>'
                        + '<span class="fa fa-calendar" aria-hidden="true" ></span>'
                        + '</td></tr></table>';
                }else if (tipoCampo == 4){
                    //Texto largo
                    componente = '<table width="100%" border="0px" cellpadding="1px"><tr><td class="etiqueta" width="120px" align="left">' 
                        + nomCampo + ':</td><td class="columnP" align="left">'
                        + '<textarea class="textareaTexto" style="width:100%;height:130px !important" name="' + nomCampo + '" id="' + nomCampo +'"></textarea>'
                        + '</td></tr></table>';
                }else if (tipoCampo == 5){
                    //Fichero
                    componente = '<table width="100%" border="0px" cellpadding="1px"><tr><td class="etiqueta" width="120px" align="left">' 
                        + nomCampo + ':</td><td class="columnP" align="left">'
                        + '<input style="height: 17px" type="text" name="' + nomCampo + '" id="' + nomCampo + '" class="inputTexto" size=75 readonly="true" ></td>'
                        + '<td><input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbVisualizar")%>"></td>'
                        + '<td><input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>"></td>'
                        + '</tr></table>';
                }else if (tipoCampo == 6 || tipoCampo == 10){
                    //Desplegable / Desplegable externo
                    componente = '<table width="100%" border="0px" c cellpadding="1px" ><tr><td class="etiqueta" width="120px" align="left">' 
                        + nomCampo + ':</td><td class="columnP" align="left">'
                        + '<input type="text" class="inputTexto" disabled style="width: 10%" readonly="true" >'
                        + '<input type="text" disabled class="inputTexto" style="width: 80%" readonly="true">'
                        + '&nbsp;'
                        + '<span class="fa fa-chevron-circle-down" aria-hidden="true"  style="cursor:hand;"></span>'
                        + '</td></tr></table>';
                }
                return componente;
            }//generarCampoVista
            
            function generarCampoDraggable (idCampo,idAgrupacion){            
              
               $("#capaDatosSuplementarios" + idAgrupacion).droppable({
               tolerance: 'fit',
               accept: $(idCampo).parent().children(),
               scroll: true
              
               });
               
                 $("#capaDatosSuplementarios" + idAgrupacion).dblclick(function(e) {
                 
                  if($(idCampo).hasClass("selected")){
                  
                       $(idCampo).offset({ top: e.pageY, left:e.pageX, of: "capaDatosSuplementarios" + idAgrupacion});
                       $(idCampo).removeClass("selected");
                       
                  }
                  
                 });
                
                 
                  $("#capaDatosSuplementarios" + idAgrupacion). keydown(function(event) {
                 
                  if($(idCampo).hasClass("selected")){
                                 
                    var posicion = $(idCampo).position();
                  
                   if(event.which=='38')//arriba
                     $(idCampo).animate({marginTop: '-=1px'});                        
                   if(event.which=='40') //abajo
                     $(idCampo).animate({marginTop: '+=1px'});  
                   if(event.which=='37') //izquierda
                     $(idCampo).animate({marginLeft: '-=1px'});  
                   if(event.which=='39') //derecha
                     $(idCampo).animate({marginLeft: '+=1px'});  
                   
                    
                       //$(idCampo).removeClass("selected");
                       
                  }
                  
                 });
               
             
             $("#capaDatosSuplementarios" + idAgrupacion).selectable();
      
      
     

		// since the draggable will kill the selectable click event, chain in our own
		

                $(idCampo).draggable({
                    revert: 'invalid',
                    cursor: "move",                  
                    scroll: true,                   
                    stop: function(){
                        $(this).draggable('option','revert','invalid'); 
                        
                    }
                   

                });
            
                $(idCampo).droppable({
                    greedy: true,
                    tolerance: 'intersect',
                    drop: function(event,ui){
                        ui.draggable.draggable('option','revert',true);                       
                    }


                });
                
          
             $(idCampo).click(function(event) {
                if($(this).hasClass("selected")){
                    $(this).removeClass("selected");
                }else{
                $(this).siblings().removeClass("selected");
                $(this).addClass("selected");
                }
                
                
                
            });
            
            
            
            }//generarCampoDraggable
            
    
           
     
            function pulsarAceptar(){
                for (i=0; i < listaCampos.length; i++) {
                    var campo = new Array();
                        campo = listaCampos[i];
                    var nomCampo = "#campo_" + campo[0];
                    //var pos = $(nomCampo).position();
                    var pos = $(nomCampo).offset();
     
                   
                    var posParent = $(nomCampo).offsetParent();
                    if(pos != null && pos != "" && pos != " " && pos != "undefined"){
                        var x = pos.left;  // x coordinate
                        var y = pos.top;  // y coordinate                      
                        //alert("offset parent top = " + posParent.offset().top + " left = " + posParent.offset().left);
                        var campoCoordenada = new Array();
                            campoCoordenada[0] = campo[0];
                            campoCoordenada[1] = x - posParent.offset().left;
                            campoCoordenada[2] = y - posParent.offset().top;
                           
                        listaCoordenadasCampos[i] = campoCoordenada;
                        $(nomCampo).addClass("ui-corner-all");
                    }//if(pos != null && pos != "" && pos != " " && pos != "undefined")
                }//for (i=0; i < listaCampos.length; i++) 
                self.parent.opener.retornoXanelaAuxiliar(listaCoordenadasCampos);
            }//pulsarAceptar()
            
            
            function pulsarValoresDefecto(){
              if(jsp_alerta("C",'<%=descriptor.getDescripcion("msj_ordenacionCamposDef")%>')==1){
                for (i=0; i < listaCampos.length; i++) {
                    var campo = new Array();
                        campo = listaCampos[i];
                    var nomCampo = "#campo_" + campo[0];
                    //var pos = $(nomCampo).position();
                    var pos = $(nomCampo).offset();
                    var posParent = $(nomCampo).offsetParent();
                    if(pos != null && pos != "" && pos != " " && pos != "undefined"){
                        var x = pos.left;  // x coordinate
                        var y = pos.top;  // y coordinate
                        //alert("nomCampo =" + nomCampo + " x = " + x + " y = " + y);
                        //alert("offset parent top = " + posParent.offset().top + " left = " + posParent.offset().left);
                        var campoCoordenada = new Array();
                            campoCoordenada[0] = campo[0];
                            campoCoordenada[1] = null;
                            campoCoordenada[2] = null;

                        listaCoordenadasCampos[i] = campoCoordenada;
                        $(nomCampo).addClass("ui-corner-all");
                    }//if(pos != null && pos != "" && pos != " " && pos != "undefined")
                }//for (i=0; i < listaCampos.length; i++) 
                self.parent.opener.retornoXanelaAuxiliar(listaCoordenadasCampos);
              }
            }//pulsarAceptar()
            
            function pulsarSalir() {
                self.parent.opener.retornoXanelaAuxiliar();
            }//pulsarSalir
        </SCRIPT>
    </head>
    <BODY class="bandaBody" onload="javascript:{ pleaseWait('off'); 
            inicializar()}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
<html:form action="/sge/DefinicionVista" target="_self">
    <html:hidden  property="opcion" value=""/>
    <input type="hidden" name="desdeProcedimiento">
    <div class="txttitblanco"><%=descriptor.getDescripcion("getiq_defVista")%></div>
    <div class="contenidoPantalla">
        <div id="capaDatosSuplementarios" name="capaDatosSuplementarios" class="capaDatosSuplementarios" style="height:600px; overflow:auto;">
            <table id ="tablaDatosSuplementariosAgrupaciones" 
                    class="contenidoPestanha" border=0 cellspacing="0px" cellpadding="0px">
            <% 
                if(!vistaForm.getErrorCargando()){
                    Vector<DefinicionAgrupacionCamposValueObject> estructuraAgrupaciones = 
                            vistaForm.getAgrupacionesCampos();

                    Vector<DefinicionCampoValueObject> estructuraCampos = 
                            vistaForm.getListaCampos();

                    if (estructuraAgrupaciones != null) {
                        int lengthEstructuraAgrupaciones = estructuraAgrupaciones.size();
                        int lengthEstructuraCampos = estructuraCampos.size();

                        for (int i = 0; i < lengthEstructuraAgrupaciones; i++) {
                            DefinicionAgrupacionCamposValueObject agrupacion = estructuraAgrupaciones.get(i);
            %>
            <div style="overflow:auto;">
                            <tr>
                                <td class="sub3titulo"><%=agrupacion.getDescAgrupacion()%></td>
                            </tr>
                            <tr style="padding-bottom:5px">
                                <td style="width: 100%" valign="top">
                                    <DIV id="capaDatosSuplementarios<%=agrupacion.getCodAgrupacion()%>" 
                                        name="capaDatosSuplementarios<%=agrupacion.getCodAgrupacion()%>"
                                        class="capaDatosSuplementarios<%=agrupacion.getCodAgrupacion()%>"
                                        style="
                                        position: relative;                                                                               
                                        padding-bottom: 80px;                                                                             
                                        padding-top: 10px;
                                        padding-left: 10px;
                                        padding-right: 10px;
                                        overflow-y: true;                                                                           
                                        ">

                                        <div style="border: 1px dashed #1766A7;
                                              padding-left: 10px;
                                              padding-right: 10px;
                                              padding-bottom: 10px;" >

                                        <%
                                            for (int k = 0; k < lengthEstructuraCampos; k++) {
                                                DefinicionCampoValueObject campo = estructuraCampos.get(k);
                                                if(campo.getCodAgrupacion().equalsIgnoreCase(agrupacion.getCodAgrupacion())
                                                        && "SI".equalsIgnoreCase(campo.getActivo())
                                                        && !"SI".equalsIgnoreCase(campo.getOculto())
                                                        ){
                                        %>

                                                    <div id="campo_<%=campo.getCodCampo()%>" 
                                                        class="componenteCampo"
                                                        style="
                                                        height: <%=campo.getAlto() %>;
                                                        width: <%=campo.getTamanho() + '%'%>">
                                                        <%=campo.getDescCampo()%>
                                                    </div>                                                  



                                        <%
                                                }//if(campo.getCodAgrupacion().equalsIgnoreCase(agrupacion.getCodAgrupacion()))
                                            }//for (int k = 0; k < lengthEstructuraCampos; k++)
                                        %>

                                        </div>

                                        </DIV>

            </div>

                                </td>
                            </tr>    
            <%  
                        }//for (int i = 0; i < lengthEstructuraAgrupaciones; i++) 
                    }//if (estructuraAgrupaciones != null)
                }//if(vistaForm.getErrorCargando())
            %>
            </table>
        </div>  
        <DIV id="capaBotones1" name="capaBotones1" STYLE="display:none" class="botoneraPrincipal">
            <INPUT type= "button" class="botonGeneral" accesskey="D" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar"  onclick="pulsarAceptar();">
            <INPUT type= "button" class="botonGeneral" accesskey="A" value="Defecto" name="cmdAceptar"  onclick="pulsarValoresDefecto();">
            <INPUT type= "button" class="botonGeneral" accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>" name="cmdSalir"  onclick="pulsarSalir();">
        </DIV>
        <DIV id="capaBotones2" name="capaBotones2" STYLE="display:none" class="botoneraPrincipal">
            <INPUT type= "button" class="botonGeneral" accesskey="S" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir2"  onclick="pulsarSalir();">
        </DIV>
    </div>
</html:form>
<script language="JavaScript1.2">
            function mostrarCapasBotones(nombreCapa) {
                document.getElementById('capaBotones1').style.display='none';
                document.getElementById('capaBotones2').style.display='none';
                document.getElementById(nombreCapa).style.display='';
            }//mostrarCapasBotones
        </script>
    </BODY>
</html:html>
