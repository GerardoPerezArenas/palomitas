<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Comparator" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>

<html>
    <head>
        <%
            int idiomaUsuario = 0;
            int codOrganizacion = 0;
            int apl = 5;
            String css = "";
            UsuarioValueObject usuario = new UsuarioValueObject();
            Config m_Config = null;
            String statusBar = "";
            
            try
            {
                if (session != null) 
                {
                    if (usuario != null) 
                    {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                        idiomaUsuario = usuario.getIdioma();
                        codOrganizacion  = usuario.getOrgCod();
                        apl = usuario.getAppCod();
                        css = usuario.getCss();
                    }
                }
                
                m_Config = ConfigServiceHelper.getConfig("common");
                statusBar = m_Config.getString("JSP.StatusBar");
            }
            catch(Exception ex)
            {

            }   
        %>     
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script> 
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide68/JavaScriptUtil.js"></script>
        
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>         
        <script type="text/javascript">
            
            var elementoSeleccionado = false;
            var listaId = new Array();
            var listaCodigo = new Array();
            var listaDesc = new Array();
            var listaDescLarga = new Array();
            var listaCodGrupoTipDocu = new Array();
            
            //Grupos
            var listaCodGrupo = new Array();
            var listaDescGrupo = new Array();
            
            
            var tabTipDocLanbide;
            
            function inicializar() {
                window.focus();
                listaId = self.parent.opener.xanelaAuxiliarArgs[0];
                listaCodigo = self.parent.opener.xanelaAuxiliarArgs[1];
                listaDesc = self.parent.opener.xanelaAuxiliarArgs[2];
                listaDescLarga = self.parent.opener.xanelaAuxiliarArgs[3];
                listaCodGrupoTipDocu = self.parent.opener.xanelaAuxiliarArgs[4];
                
                //Grupos
                listaCodGrupo = self.parent.opener.xanelaAuxiliarArgs[5];
                
                listaDescGrupo = self.parent.opener.xanelaAuxiliarArgs[6];
                
                /*if (listaCodGrupo[0] != 'TDS'){
                    listaCodGrupo.unshift('TDS');
                    if ('idiomaUsuario' == 1)
                        listaDescGrupo.unshift('TODOS');
                    else             
                        listaDescGrupo.unshift('GUZTIAK');
                }*/
                
                comboListaGrupo.addItems(listaCodGrupo, listaDescGrupo);
                
                cargarTipos();
                
            }
            
            function pulsarModificarTipDoc(){
                
                var tipoDocumentalSeleccionado;
                
                if(tabTipDocLanbide.selectedIndex != -1) {
                    tipoDocumentalSeleccionado = tabTipDocLanbide.selectedIndex;
                    var tds = new Array();
                    /*tds[0] = listaId[tipoDocumentalSeleccionado];
                    tds[1] = listaCodigo[tipoDocumentalSeleccionado];
                    tds[2] = listaDesc[tipoDocumentalSeleccionado];*/
                    tds[0] = listaTiposDocumentales[tipoDocumentalSeleccionado][0];
                    tds[1] = listaTiposDocumentales[tipoDocumentalSeleccionado][1];
                    tds[2] = listaTiposDocumentales[tipoDocumentalSeleccionado][2];
                    
                    
                    self.parent.opener.retornoXanelaAuxiliar(tds);
                }else{
                    jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                }    
            }
            
            function pulsarVolver(){
                
                var n = new Array();

                self.parent.opener.retornoXanelaAuxiliar(n);
               
            }
            
            function replaceAll(str, token, newtoken) {
				if(str==undefined) str = "";
				if(token!=newtoken)
					while(str.indexOf(token) > -1) {
						str = str.replace(token, newtoken);
					}
                return str;
            }
            
        </script> 
    </head>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>         
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide68/melanbide68.css"/>
    
    <body class="bandaBody" onload="{inicializar();}">
        <!-- <div style="height:50px; width: 100%;">
            <table width="100%" style="height: 100%;" cellpadding="0px" cellspacing="0px" border="0px">
                <tr>
                    <td class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_TiposDoc")%></td>
                </tr>
            </table>
        </div> -->
                
        <div class="txttitblanco">
            <%=descriptor.getDescripcion("gEtiq_TiposDoc")%>
            <span style="position: absolute; right: 20px; top: 10px; font-size: 30px !important; cursor: pointer;" class="fa fa-question" id="botonAyudaTiposDoc" name="botonAyudaTiposDoc" onclick="verPDFAyudaSelTipDoc()"></span>
        </div>        
        
        <div class="tab-page contenidoPantalla" id="tabPage354" style="height:640px !important; width: 100%;">
            </br>
            <div style="padding: 10px;">
                
                <!-- Grupos / Familias --->
                <div>
                    <label class="etiqueta"><%=descriptor.getDescripcion("gEtiq_tipDocGrupo")%>:</label>
                </div>
                <div style="margin-bottom: 1%;">
                    <span>
                        <input type="text" name="codGrupo" id="codGrupo" class="inputTexto" style="display:none; width:7%;" />
                        <input type="text" class="inputTexto" name="descGrupo" id="descGrupo" style="width:75%;" readonly="true" />
                        <a href="" id="anchorGrupo" name="anchorGrupo" style="text-decoration: none;"> 
                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonGrupo" name="botonGrupo" style="cursor:hand;"></span> 
                        </a>
                    </span>
                </div>
                
                </br>
                <div id="divGeneral" > 
                    <div style="margin-bottom: 10px;">
                        <label class="etiqueta"><%=descriptor.getDescripcion("gEtiq_TiposDoc")%>:</label>
                    </div>
                    <div id="listaTiposDocumentales" align="center"></div>
                </div> 
                <!-- botones debajo de la tabla --->
                <div class="botonera" style="margin-bottom: 1%;">
                    <input type="button" id="btnModificarTipDoc" name="btnModificarTipDoc" class="botonGeneral" value="<%=descriptor.getDescripcion("etiqSeleccionar")%>" onclick="pulsarModificarTipDoc();">
                    <input type="button" id="btnVolver" name="btnVolver" class="botonGeneral" value='<%=descriptor.getDescripcion("gbVolver")%>' onclick="pulsarVolver();">
                </div>   
                
                <div style="display: inline-block;float: left;width: 29%;margin-right: 2%;">
                    <label class="etiqueta" ><%=descriptor.getDescripcion("etiqTipoDocumental")%>:</label>
                    <div>
                        <textarea id="descCorta" name="descCorta" rows="7" cols="36" class="inputTexto" style="text-transform: none;width: 100%;height: auto;float:left;" ></textarea>
                    </div>
                </div>
                <div style="display: inline-block;width: 69%;">
                    <label class="etiqueta" ><%=descriptor.getDescripcion("etDesForm")%>:</label>
                    <div>
                        <textarea id="descLarga" name="descLarga" rows="7" cols="84" class="inputTexto" style="text-transform: none;width: 100%;height: auto;float:left;" ></textarea>
                    </div>
                </div>
                
            </div>
        </div>   
                    
        <script  type="text/javascript">
            var comboListaGrupo = new Combo("Grupo");
            
            var listaTiposDocumentales = new Array();
            var listaTiposDocumentalesTabla = new Array();
            
            $(document).ready(function() {
                
                //para que la búsqueda la haga sin tildes también
                jQuery.fn.DataTable.ext.type.search.string = function ( data ) {
                    return ! data ?
                        '' :
                        typeof data === 'string' ?
                            data
                                .replace( /\n/g, ' ' )
                                .replace( /[áâàä]/g, 'a' )
                                .replace( /[éêèë]/g, 'e' )
                                .replace( /[íîìï]/g, 'i' )
                                .replace( /[óôòö]/g, 'o' )
                                .replace( /[úûùü]/g, 'u' )
                                .replace( /ç/g, 'c' ) :
                            data;
                };
                
                $('#codGrupo').on('change', cargarTipos());
                
                comboListaGrupo.change = cargarTipos;
                
                $('body').on('click','td', function() {
                    if(tabTipDocLanbide.selectedIndex != -1) {
                        tipoDocumentalSeleccionado = tabTipDocLanbide.selectedIndex;
                        /*$('#descCorta').val(listaDesc[tipoDocumentalSeleccionado]);
                        $('#descLarga').val(replaceAll(listaDescLarga[tipoDocumentalSeleccionado], '|', '\n'));*/
            
                        $('#descCorta').val(listaTiposDocumentales[tipoDocumentalSeleccionado][2]);
                        $('#descLarga').val(replaceAll(listaTiposDocumentales[tipoDocumentalSeleccionado][3], '|', '\n'));
                    }else{
                        $('#descCorta').val('');
                        $('#descLarga').val('');
                    }
                });
            });
           
            function cargarTipos(){
                //Tabla TiposDocumentales
                
                listaTiposDocumentales = new Array();
                listaTiposDocumentalesTabla = new Array();
                
                var codGrupo = $('#codGrupo').val();
                
                tabTipDocLanbide = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaTiposDocumentales'));        
                tabTipDocLanbide.addColumna('0','center',"<%=descriptor.getDescripcion("gEtiq_tipDocId")%>", "<%=descriptor.getDescripcion("gEtiq_tipDocId")%>");
                tabTipDocLanbide.addColumna('100','center',"<%=descriptor.getDescripcion("etCodForm")%>", "<%=descriptor.getDescripcion("etCodForm")%>");    
                
                tabTipDocLanbide.addColumna('340','left',"<%=descriptor.getDescripcion("etiqTipoDocumental")%>", 'left',"<%=descriptor.getDescripcion("etiqTipoDocumental")%>");
                tabTipDocLanbide.addColumna('340','left',"<%=descriptor.getDescripcion("etDesForm")%>", "<%=descriptor.getDescripcion("etDesForm")%>");                    
        
                //tabTipDocLanbide.addColumna('0','center','Grupo','Grupo');
        
                tabTipDocLanbide.displayCabecera=true;
                tabTipDocLanbide.height = '300';   
                
                //if (codGrupo != '' && codGrupo != 'TDS'){
                if (codGrupo != ''){
                    var c=0;
                    for (var i=0;i<listaId.length;i++){
                        
                        if (listaCodGrupoTipDocu[i] == codGrupo){
                            listaTiposDocumentales[c] = [listaId[i],listaCodigo[i],listaDesc[i],replaceAll(listaDescLarga[i], '|', '\n')];
                            listaTiposDocumentalesTabla[c] = [listaId[i],listaCodigo[i],listaDesc[i],replaceAll(listaDescLarga[i], '|', '\n')];
                            c++;
                        }
                        
                    }
                }else{
                    for (var i=0;i<listaId.length;i++){
                        listaTiposDocumentales[i] = [listaId[i],listaCodigo[i],listaDesc[i],replaceAll(listaDescLarga[i], '|', '\n')];
                        listaTiposDocumentalesTabla[i] = [listaId[i],listaCodigo[i],listaDesc[i],replaceAll(listaDescLarga[i], '|', '\n')];
                    }
                }
                
                tabTipDocLanbide.lineas=listaTiposDocumentales;
                tabTipDocLanbide.displayTablaConTooltips(listaTiposDocumentalesTabla);
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    try{
                        var div = document.getElementById('listaTiposDocumentales');
                        div.children[0].children[0].children[0].children[1].style.width = '100%';
                        div.children[0].children[1].style.width = '100%';
                    }
                    catch(err){
                    }
                } 
            }
            
            function verPDFAyudaSelTipDoc(){	
				if ('<%=idiomaUsuario%>' == 1 || '<%=idiomaUsuario%>' == 4) {
					var sourc;
					if ('<%=idiomaUsuario%>' == 1) {
						sourc = "<%=request.getContextPath()%>/jsp/sge/ver_pdf.jsp?fichero=/pdf/ayuda/catalogacion/pantallaTiposDocumentales_es.pdf";						
					}else {
						sourc = "<%=request.getContextPath()%>/jsp/sge/ver_pdf.jsp?fichero=/pdf/ayuda/catalogacion/pantallaTiposDocumentales_eu.pdf";
					}
					ventanaInforme = window.open("<%=request.getContextPath()%>/jsp/mainVentana.jsp?source="+sourc,'ventanaInforme','width=800px,height=550px,status='+ '<%=statusBar%>' + ',toolbar=no,resizable=1');
					ventanaInforme.focus();
				} else {
					jsp_alerta('A','<%=descriptor.getDescripcion("msjNoPDF")%>');
				}
            }
            
        </script>
       
    </body>
</html>