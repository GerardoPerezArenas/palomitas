<!-- JSP de mantenimiento de justificantes de registro -->
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject" %>
<%@page import="java.util.ArrayList" %>
 <%
    int idioma = 1;
    int apl = 1;
    String css = "";
    if (session != null && session.getAttribute("usuario") != null) {
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        if (usuario != null) {
            idioma = usuario.getIdioma();
            apl = usuario.getAppCod();
            css = usuario.getCss();
        }
    }


     String error = (String)request.getAttribute("ERROR_MANTENIMIENTO_JUSTIFICANTE");
     Boolean modeloPeticionRespuesta = (Boolean)request.getAttribute("modeloPeticionRespuesta");
%>
<html:html>
    <head>
        <title>Mantenimiento Justificantes de registro</title>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
                 type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
    <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

    <!-- *********************	FICHEROS JAVASCRIPT **************************    -->

   <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >

    <script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >
    <script type="text/javascript" src="<html:rewrite page='/scripts/TablaNueva.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>


    <script type="text/javascript">
        var error = "<%=error%>";
        var justificantes = new Array();
        var modelos = new Array();
        var codsTipoJustif = new Array();
        var descsTipoJustif = new Array();
        var comboTipos;
        var tablaSeleccionada="";
        var filaSeleccionada=-1;
        var tipoJustif=-1;

        function inicializar(){
            pleaseWait('off');
            
            var j=0;
            <%
                  if (modeloPeticionRespuesta) {	
                    if(request.getAttribute("tiposJustif")!=null){	
                        ArrayList<GeneralValueObject> listaTipos = (ArrayList<GeneralValueObject>)request.getAttribute("tiposJustif");	
                        //añadimos codigos y descripciones de tipos de justificante a dos arrays para	
                        //crear un desplegable con estos datos	
                        if(listaTipos.size()>0){	
                            for(int i = 0; i< listaTipos.size(); i++){	
                                GeneralValueObject tipo = listaTipos.get(i);
            %>
                            codsTipoJustif[j] = ['<%=tipo.getAtributo("codigo")%>'];	
                             descsTipoJustif[j++] = ['<%=tipo.getAtributo("descripcion")%>'];
              
            <%
                        }
                    }
                }
            %>
                comboTipos = new Combo("TipoJustif");
                comboTipos.addItems(codsTipoJustif,descsTipoJustif);
                  <% } else { %>	
                // Es de tipo justificante siempre	
                document.forms[0].codTipoJustif.value = 0;	
            <% } %>
        }
        
        function pulsarAlta(){
            var continuar = false;
            var descripcion = document.forms[0].descripcionJustificante.value;
            var fichero             = document.forms[0].fichero.value;
            var tipo = document.forms[0].codTipoJustif.value;
            
            if(descripcion!="" && fichero!=""){            
                if(tipo!=""){
                    document.forms[0].action="<%=request.getContextPath()%>/flexia/MantJustificantesRegistro.do?opcion=alta";                
                    document.forms[0].submit();
                } else
                    jsp_alerta("A",'Es obligatorio indicar el Tipo de Justificante')
            }else
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorJustifObligatorio")%>')

        }


        function isJustificanteActivo(nombre){
            var exito = false;
            if(nombre!=null && nombre!=""){
                if(tipoJustif==0 && justificantes!=null){
                    for(i=0;justificantes!=null && i<justificantes.length;i++){
                        if(justificantes[i][0]==nombre && (justificantes[i][2]=="SI" || justificantes[i][2]=="si"))
                            exito = true;
                    }
                } else if(tipoJustif==1 && modelos!=null){
                    for(i=0;modelos!=null && i<modelos.length;i++){
                        if(modelos[i][0]==nombre && (modelos[i][2]=="SI" || modelos[i][2]=="si"))
                            exito = true;
                    }
                }
            }
            return exito;
        }


        function pulsarEliminar(){
            if(filaSeleccionada!=-1){

                if(jsp_alerta("C","<%=descriptor.getDescripcion("msgConfirmEliminarJustif")%>",tablaSeleccionada+" - Fila "+(filaSeleccionada+1))){
                   try{
                       var justificanteDelete;
                       if(tipoJustif==0)
                            justificanteDelete = justificantes[filaSeleccionada][0];
                       else if(tipoJustif==1)
                            justificanteDelete = modelos[filaSeleccionada][0];

                        if(justificanteDelete!=null){

                            var continuar = false;
                            if(isJustificanteActivo(justificanteDelete)){
                                if(jsp_alerta("A","<%=descriptor.getDescripcion("msgErrEliminarJusActivo")%>"))
                                    continuar = true;
                            }else
                                continuar = true;

                            if(continuar){
                                document.forms[0].action="<%=request.getContextPath()%>/flexia/MantJustificantesRegistro.do?opcion=eliminar&justificante=" + justificanteDelete + "&tipoJustif=" + tipoJustif;
                                document.forms[0].submit();
                            }
                        }
                    }catch(err){
                        alert("Error: " + err.descripcion);
                    }
                }
            }else
                jsp_alerta("A","<%=descriptor.getDescripcion("msgErrorNoSelecJust")%>");
        }


        function pulsarActivar(){
            var indice = filaSeleccionada;
            if(indice!=-1){

                try{
                    var justificanteDelete;
                    if(tipoJustif==0)
                        justificanteDelete = justificantes[filaSeleccionada][0];
                    else if(tipoJustif==1)
                        justificanteDelete = modelos[filaSeleccionada][0];
                    
                    if(justificanteDelete!=null){
                        document.forms[0].action="<%=request.getContextPath()%>/flexia/MantJustificantesRegistro.do?opcion=activar&justificante=" + justificanteDelete + "&tipoJustif=" + tipoJustif;
                        document.forms[0].submit();
                    }
                }catch(err){
                    alert("Error: " + err.descripcion);
                }
            }else
                jsp_alerta("A","<%=descriptor.getDescripcion("msgErrorNoSelecJust")%>");
        }


        function pulsarDesactivar(){
            var indice = filaSeleccionada;
            if(indice!=-1){

                try{
                    var justificanteDelete;
                    if(tipoJustif==0)
                        justificanteDelete = justificantes[filaSeleccionada][0];
                    else if(tipoJustif==1)
                        justificanteDelete = modelos[filaSeleccionada][0];
                    
                    if(justificanteDelete!=null){
                        document.forms[0].action="<%=request.getContextPath()%>/flexia/MantJustificantesRegistro.do?opcion=desactivar&justificante=" + justificanteDelete + "&tipoJustif=" + tipoJustif;
                        document.forms[0].submit();
                    }
                }catch(err){
                    alert("Error: " + err.descripcion);
                }
            }else
                jsp_alerta("A","<%=descriptor.getDescripcion("msgErrorNoSelecJust")%>");
        }



        function mostrarError(codigo){
            pleaseWait('off');
            if(codigo=="1")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorExtensionJustif")%>');
            else
            if(codigo=="2")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorConnectionJustif")%>');
            else
            if(codigo=="3")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorAltaJustif")%>');
            else
            if(codigo=="4")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorCopiarJustif")%>');
            else
            if(codigo=="5")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorExisteJustif")%>');
            else
            if(codigo=="6")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorNoExisteDir")%>');
            else
            if(codigo=="7")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgJustBorrarDesconocido")%>');
            else
            if(codigo=="8")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgBorrarJustDirDesconocido")%>');
            else            
            if(codigo=="9")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgBorrarJustNoExisteDisco")%>');
            else
            if(codigo=="10") 
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgJustEliminarCorrecto")%>');
            else
            if(codigo=="11")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorEliminarJustBD")%>');
            else
            if(codigo=="12")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorElimJustServer")%>');
            else
            if(codigo=="13")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorConnectionJustif")%>');
            else
            if(codigo=="14")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorConnectionJustif")%>');
            else
            if(codigo=="15")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorAltaJustDesconocido")%>');
            else
            if(codigo=="16")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorTamMaxJustConfig")%>');
            else
            if(codigo=="17")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorTamMaxJustExcedido")%>' + " " + '<%=(String)request.getAttribute("TAM_MAX_JUSTIFICANTE")%> bytes');
            else
            if(codigo=="18")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorPropDirJustifi")%>');
            else
            if(codigo=="19")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgJustAltaCorrecta")%>');
            else
            if(codigo=="20")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgJustActivarDescon")%>');
            else
            if(codigo=="21") /*** desde aqui **/
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgJustActivarCorrecto")%>');
            else
            if(codigo=="22")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgJustActivarIncorrecto")%>');
            else
            if(codigo=="23")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorConnectionJustif")%>');
            else
            if(codigo=="24")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorTecnicoActivacion")%>');
            else
            if(codigo=="25")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorConnectionJustif")%>');
            else
            if(codigo=="26")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorTecnicoGetJustif")%>');
            else
            if(codigo=="27")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorExisteJustifAct")%>');
            else
            if(codigo=="28") /** probar a partir de aqui **/
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorJustDesactDesc")%>');
            else
            if(codigo=="29") 
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgJustDesactCorrecto")%>');
            else
            if(codigo=="30")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgJustDesactIncorrecto")%>');
            else
            if(codigo=="31")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorConnectionJustif")%>');
            else
            if(codigo=="32")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorTecDesactJustif")%>');
            else
            if(codigo=="33")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgFormatoZipJustif")%>');
            else
            if(codigo=="34")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrFilExiDestino1") + "</br>" + (String)request.getAttribute("FICHEROS_EXISTENTES")
                    + " " + descriptor.getDescripcion("msgErrFilExiDestino2") %>');
            else
            if(codigo=="35")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrValidarDestino")%>');
            else
            if(codigo=="36")
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrDescomprimirZip")%>');


            limpiar();
        }

        function limpiar(){
            document.forms[0].descripcionJustificante.value = "";
            
            //document.forms[0].fichero.value ="";
            // Para eliminar el contenido de input file no se puede hacer value="" porque por seguridad el control es de solo lectura y no podemos
            // modificar su contenido. ->
            var fileupload = $('#fichero');
             fileupload.replaceWith(fileupload.val('').clone(true));	
            <% if (modeloPeticionRespuesta) { %>	
                comboTipos.buscaCodigo("");	
            <% } %>
            
            
        }
        
        function rellenarDatos(tableName,rowID){
            if(tableName==tabla){
                filaSeleccionada=tabla.selectedIndex;
                if(filaSeleccionada!=-1){
                    tablaSeleccionada="Listado de Justificantes";
                    tipoJustif=0;
                }
            } else if(tableName==tablaModelos){
                filaSeleccionada=tablaModelos.selectedIndex;
                if(filaSeleccionada!=-1){
                    tablaSeleccionada="Listado de Peticiones";
                    tipoJustif=1;
                }
            }
            if(filaSeleccionada==-1) tablaSeleccionada="";
        } 
    </script>

    </head>

    <body class="bandaBody" onload="inicializar();" >

        <!-- Mensaje de esperar mientras carga  -->
         <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        <!-- Fin mensaje de esperar mientras carga  -->
<form action='<%=request.getContextPath()%>/flexia/MantJustificantesRegistro.do' method="POST" enctype="multipart/form-data">
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titMantJustifPerso")%></div>
    <div class="contenidoPantalla">
    <table>
        <tr>
            <td valign="top">
                <table width="100%" rules="cols"  border="0" cellspacing="0" cellpadding="0" class="fondoCab" valign="top">
                    <tr>
                        <td colspan="3" class="sub3titulo"><%=descriptor.getDescripcion("etiq_TiposJustifJustificante")%></td>	
                    </tr>	
                    <tr>
                        <td colspan="3" name="tablaJustificantes" id="tablaJustificantes">
                        </td>
                    </tr>
                     </table>	
                <% if (modeloPeticionRespuesta) { %>	
                <table width="100%" rules="cols"  border="0" cellspacing="0" cellpadding="0" class="fondoCab" valign="top" style="margin-top: 3%">	
                    <tr>	
                        <td colspan="3" class="sub3titulo"><%=descriptor.getDescripcion("etiq_TiposJustifModelo")%></td>	
                    </tr>	
                    <tr>	
                        <td colspan="3" name="tablaModelos" id="tablaModelos"></td>	
                    </tr>	
                </table>	
                <% } %>
            </td>
        </tr>
        <tr>
            <td>
                  <table border="0" style="width: 90%; padding-top: 3%">
                <tr>
       <td class="etiqueta" style="width: 45%"><%=descriptor.getDescripcion("etiqDescripcionJustificante")%></td>	
                   <td class="etiqueta" style="width: 30%"><%=descriptor.getDescripcion("etiqSeleccionarArchivo")%></td>	
                   <% if (modeloPeticionRespuesta) { %>	
                   <td class="etiqueta" style="width: 25%"><%=descriptor.getDescripcion("etiq_TiposJustif")%></td>	
                   <% } %>
                </tr>
                <tr>
                   <td style="width: 45%"><input type="text" name="descripcionJustificante" id="descripcionJustificante" class="inputTextoObligatorio" size="80" maxlength="4000" onkeyup="return xAMayusculas(this);"></td>	
                    <td style="width: 30%"><input type="file" name="fichero" id="fichero" class="inputTextoObligatorio" size="44" style="width:100%"></td>	
                    <td class="etiqueta" style="width: 25%">	
                        <% if (modeloPeticionRespuesta) { %>	
                        <input class="inputTextoObligatorio" type="text" name="codTipoJustif" id="codTipoJustif" size="4" onfocus="this.select();" >	
                        <input class="inputTexto" type="text" name="descTipoJustif" id="descTipoJustif" readonly >	
                        <a name="anchorTipoJustif" id="anchorTipoJustif" href = "" style="text-decoration:none;" onfocus="javascript:this.focus();">	
                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoJustif"	
                                                name="botonTipoJustif" style="cursor:hand;"></span>	
                        </a>	
                        <% } else { %>	
                        <input type="hidden" name="codTipoJustif" id="codTipoJustif">	
                        <% } %>	
                    </td>
                </tr>
                </table>
            </td>
        </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAlta")%>" name="cmdAlta" onClick="pulsarAlta();" alt="<%=descriptor.getDescripcion("gbAlta")%>" title="<%=descriptor.getDescripcion("gbAlta")%>"/>
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> name="cmdEliminar" onClick="pulsarEliminar();" title='<%=descriptor.getDescripcion("gbEliminar")%>' alt='<%=descriptor.getDescripcion("gbEliminar")%>'>
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%> name="cmdLimpiar" onClick="limpiar();" title='<%=descriptor.getDescripcion("gbLimpiar")%>' alt='<%=descriptor.getDescripcion("gbLimpiar")%>'>
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbActivar")%> name="cmdActivar" onClick="pulsarActivar();" title='<%=descriptor.getDescripcion("gbActivar")%>' alt='<%=descriptor.getDescripcion("gbActivar")%>'>
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbDesactivar")%> name="cmdDesactivar" onClick="pulsarDesactivar();" title='<%=descriptor.getDescripcion("gbDesactivar")%>' alt='<%=descriptor.getDescripcion("gbDesactivar")%>'>
    </div>
</div>
<script type="text/javascript">    
    var tabla = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaJustificantes"));
    tabla.addColumna('280','center',"<%=descriptor.getDescripcion("titNombreJustificante")%>");
    tabla.addColumna('515','center',"<%=descriptor.getDescripcion("titDescripcionJustificante")%>");
    tabla.addColumna('100','center',"<%=descriptor.getDescripcion("titActivoJustificante")%>");
    tabla.displayCabecera=true;
    tabla.displayTabla();
    


    var contador = 0;
    <logic:iterate name="JustificanteRegistroPersonalizadoForm" property="justificantes" id="justificante">
        justificantes[contador] = ['<bean:write name="justificante" property="nombreJustificante" ignore="true"/>',
                                   '<bean:write name="justificante" property="descripcionJustificante" ignore="true"/>',
                                   '<bean:write name="justificante" property="descripcionDefecto" ignore="true"/>'];

        contador++;
    </logic:iterate>

    tabla.lineas = justificantes;
    tabla.displayTabla();
    

  <% if (modeloPeticionRespuesta) { %>	
        // Modelo peticion respuesta	
        var tablaModelos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaModelos"));	
        tablaModelos.addColumna('280','center',"<%=descriptor.getDescripcion("titNombreJustificante")%>");	
        tablaModelos.addColumna('515','center',"<%=descriptor.getDescripcion("titDescripcionJustificante")%>");	
        tablaModelos.addColumna('100','center',"<%=descriptor.getDescripcion("titActivoJustificante")%>");	
        tablaModelos.displayCabecera=true;	
        tablaModelos.displayTabla();
        
           contador = 0;	
        <logic:iterate name="JustificanteRegistroPersonalizadoForm" property="modelos" id="justificante">	
            modelos[contador] = ['<bean:write name="justificante" property="nombreJustificante" ignore="true"/>',	
                                       '<bean:write name="justificante" property="descripcionJustificante" ignore="true"/>',	
                                       '<bean:write name="justificante" property="descripcionDefecto" ignore="true"/>'];

            contador++;	
        </logic:iterate>		
        tablaModelos.lineas = modelos;	
        tablaModelos.displayTabla();	
    <% } %>

    if(error!=null && error!="" && error!="null"){
        mostrarError(error);
    }
   
</script>   
</form>
    
</body>
</html:html>
