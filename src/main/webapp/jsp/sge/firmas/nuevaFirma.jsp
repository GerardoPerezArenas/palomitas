<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.UsuariosGruposForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO"%>

<html:html>

    <head>
        <jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

        <TITLE>::: ADMINISTRACION Unidad Orgánica:::</TITLE>


        <%
            int idioma = 1;
            int apl = 1;
            int munic = 0;
            String css = "";
            if (session != null) {
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    apl = usuario.getAppCod();
                    munic = usuario.getOrgCod();
                    css = usuario.getCss();
                }
            }
        %>


        <!-- Estilos -->

        <jsp:useBean id="descriptor" scope="request"
                     class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
                     type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor" property="idi_cod" value="<%= idioma%>" />
        <jsp:setProperty name="descriptor" property="apl_cod" value="<%= apl%>" />


        <!-- Ficheros JavaScript -->

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/uor.js"></script>
        <SCRIPT type="text/javascript">

            // Declaracion de variables.            
            var uor_cods = new Array();
            var uor_descs = new Array();
            var uors = new Array();
            var codCargos = new Array();
            var descCargos = new Array();
            var cargos = new Array();
            var usuarios = new Array();
            var cod_Usuarios = new Array();
            var nomUsuarios = new Array();
            var desdeDefinicionProcedimiento;
            var ordenDefinido = 0;
            
            
            <%
                Vector listaUORDTOs = (Vector) request.getAttribute("listaUORs");
                for (int j = 0; j < listaUORDTOs.size(); j++) {
                    UORDTO dto = (UORDTO) listaUORDTOs.get(j);%>
                        uors[<%=j%>] = new Uor<%=dto.toJavascriptArgs()%>;
            <%}%>
            
            

                /** Funcion encargada de inicializar los datos durante la carga de la pagina **/
                function inicializar() {
                    var argVentana = self.parent.opener.xanelaAuxiliarArgs;
                    document.forms[0].codMunicipio.value = argVentana[0];
                    desdeDefinicionProcedimiento = argVentana[1];
                    if (!desdeDefinicionProcedimiento){
                        ordenDefinido = argVentana[2];
                        document.forms[0].numOrden.value=ordenDefinido;
                        document.forms[0].numOrden.readOnly=true;
                        document.getElementById("definicionFirma").style.display="none";
                        document.forms[0].defineTramitacion.style.display = "none";
                        document.getElementById("definicionFinExpedienteRechazar").style.display = "none";
                        document.forms[0].finExpedienteRechazar.style.display = "none";
						document.getElementById("tramitarSubsanacion").style.display = "none";
                    	document.forms[0].tramSubsanacion.style.display = "none";
                    }
                    var cont = 0;
                
                        <c:forEach var="campo" items="${requestScope.listaUORs}">
                        uor_cods[cont] ='<bean:write name="campo" property="uor_cod_vis" ignore="true"/>';
                    uor_descs[cont] ='<bean:write name="campo" property="uor_nom" ignore="true"/>';               
                    cont++;
                        </c:forEach>                        
                    
                        cont = 0;
                        <c:forEach var="campo" items="${requestScope.listaCargos}">     
                        codCargos[cont] ='<bean:write name="campo" property="uor_cod_vis" ignore="true"/>';
                    descCargos[cont] ='<bean:write name="campo" property="uor_nom" ignore="true"/>';
                    cargos [cont] = [
                        '<bean:write name="campo" property="uor_cod" ignore="true"/>',
                        '<bean:write name="campo" property="uor_cod_vis" ignore="true"/>',
                        '<bean:write name="campo" property="uor_nom" ignore="true"/>',
                        '<bean:write name="campo" property="uor_estado" ignore="true"/>'
                    ];

                    cont++;
                        </c:forEach>
                        
                        cont = 0;
                        <c:forEach var="campo" items="${requestScope.listaUsuarios}">     
                        cod_Usuarios[cont] ='<bean:write name="campo" property="codUsuario" ignore="true"/>';
                    nomUsuarios[cont] ='<bean:write name="campo" property="nombreUsuario" ignore="true"/>';
                    usuarios [cont] = [
                        '<bean:write name="campo" property="codUsuario" ignore="true"/>',
                        '<bean:write name="campo" property="nombreUsuario" ignore="true"/>'
                    ];

                    cont++;
                        </c:forEach>
                    
                    
                        comboUORs.addItems(uor_cods, uor_descs);
                    comboCargos.addItems(codCargos, descCargos);
                    comboUsuarios.addItems(cod_Usuarios,nomUsuarios);
                }
            
                function pulsarSalir() {
                    self.parent.opener.retornoXanelaAuxiliar();
                }
               
                function cargarUsuarios(lUsuarios, cUsuarios, dUsuarios) {
                    usuarios = lUsuarios;
                    cod_Usuarios = cUsuarios;
                    descUsuarios = dUsuarios;
                    comboUsuarios.addItems(cod_Usuarios, descUsuarios);
                }
            
                function onClickHrefUOR() {
                    var datos;
                    var argumentos = new Array();
    
                    argumentos[0] = document.forms[0].codUORs.value;
   
                    var source = APP_CONTEXT_PATH + "/administracion/UsuariosGrupos.do?opcion=modalUOR" +
                        "&codOrganizacion=" + document.forms[0].codMunicipio.value +
                        "&codEntidad=" + document.forms[0].entidad.value;

                    abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + source, argumentos,
                            'width=550,height=460',function(datos){
                                    if(datos != null) {
                                        var indice = buscarIndiceUOR(datos[0]);
                                        comboUORs.selectItem(indice);
                                    }
                                    if((document.forms[0].codUORs.value != '') && (document.forms[0].descUORs.value == '')) {
                                        document.forms[0].codUORs.value = '';
                                    }
                            });                    
                }	


                function buscarIndiceUOR(codUOR) {
                    var indice=-1;
                    for (var i=0; uor_cods.length;i++)  {
                        if (codUOR==uor_cods[i]) {
                            indice=i;
                            break;
                        }
                    }
                    return indice;
                }


                function  pulsarAceptar(){
                    //En el caso de que la firma se configure en el alta, se mandará la uor -888
                    //En el caso de que la UOR sea la del expediente, se mandará la uor -999
                    
                    /*Para poder grabar una firma tiene que darse una de estas circunstancias:
                     *1 = Se definirá en tramitación --
                     *2 = Tiene un usuario configurado
                     *3 = Tiene una UOR configurada
                     *4 = Tiene marcado que firme la UOR del expediente */
                    var retorno = new Array();
                    var codigoUOR = null;
                    var descUOR= '--';
                    var codigoCargo = null;
                    var codigoUsuario = null;
                    var descCargo = '--';
                    var descUsuario = '--';   
					var tramitar = '0';
                                      
                    if (document.forms[0].defineTramitacion.checked==false &&
                        (document.forms[0].UORExpediente.checked==false && comboUORs.selectedIndex<=0) &&
                        (comboUsuarios.selectedIndex<=0)) {
                        jsp_alerta("A",'<%=descriptor.getDescripcion("msjCamposObligatorios")%>');
                    } else {                    
                        if ( document.forms[0].defineTramitacion.checked==true){                        
                            codigoUOR = '-888';
                            descUOR = '<%=descriptor.getDescripcion("gEtiq_defExp")%>';
                        } else {
                            if (document.forms[0].UORExpediente.checked==true) {
                                codigoUOR = '-999';
                                descUOR = '<%=descriptor.getDescripcion("gEtiq_txtUnidExp")%>';
                            } else if (comboUORs.selectedIndex>0){
                                codigoUOR = uors[comboUORs.selectedIndex].uor_cod
                                descUOR = document.forms[0].descUORs.value;
                            } 
                            if (comboCargos.selectedIndex>0) {
                                codigoCargo = cargos[comboCargos.selectedIndex-1][0];
                                descCargo = document.forms[0].descCargos.value;
                            }
                            if (comboUsuarios.selectedIndex>0) {
                                codigoUsuario = document.forms[0].codUsuarios.value;
                                descUsuario = document.forms[0].descUsuarios.value;
                            }
							
							if (document.forms[0].tramSubsanacion.checked==true) {
                            	tramitar = 1;
                        	}

                        }
                        retorno[0] = codigoUOR;
                        retorno[1] = document.forms[0].codUORs.value;
                        retorno[2] = descUOR;
                        retorno[3] = document.forms[0].codCargos.value;
                        retorno[4] = codigoCargo;
                        retorno[5] = descCargo;
                        retorno[6] = document.forms[0].numOrden.value;
                        retorno[7] = descUsuario;
                        retorno[8] = codigoUsuario;
                        retorno[9] = document.forms[0].finExpedienteRechazar.checked;
			retorno[10] = tramitar;
                        
                        self.parent.opener.retornoXanelaAuxiliar(retorno);
                    }
                }
                    
                                   
                

                function onclickUORExpediente() {
                    if (document.forms[0].UORExpediente.checked==true) {
                        comboUORs.selectItem(-1);
                        comboUORs.deactivate();                        
                        document.forms[0].descUORs.style.background="FFFFFF";                        
                        comboCargos.activate();
                        comboUsuarios.selectItem(-1);
                        comboUsuarios.deactivate();
                    } else {
                        comboUORs.selectItem(-1);
                        comboUORs.activate();
                        document.forms[0].descUORs.style.background="#fffea3";
                        comboCargos.selectItem(-1);
                        comboCargos.deactivate();
                        cargarUsuarios([],[],[]);
                    }
                }
                
                function onClickDefinible(){
                    if (document.forms[0].defineTramitacion.checked==true) {
                        document.forms[0].UORExpediente.checked=false;                        
						document.forms[0].tramSubsanacion.checked=false;
                        comboUORs.selectItem(-1);
                        comboUORs.deactivate();                        
                        document.forms[0].descUORs.style.background="FFFFFF";  
                        comboUsuarios.selectItem(-1);
                        comboUsuarios.deactivate();
                        comboCargos.selectItem(-1);
                        comboCargos.deactivate();
                        document.forms[0].UORExpediente.disabled=true;                        
						document.forms[0].tramSubsanacion.disabled=true;
                    } else {
                        document.forms[0].UORExpediente.disabled=false;
						document.forms[0].tramSubsanacion.disabled=false;
                        comboUORs.selectItem(-1);
                        comboUORs.activate();                    
                        cargarUsuarios([],[],[]); 
                    }
                }

                function onchangeCodUOR(){                                 
                    comboCargos.activate();
                    comboUsuarios.selectItem(-1);
                    comboUsuarios.activate();              
                    var codigoUOR = '';
                    if (comboUORs.selectedIndex>0) codigoUOR = uors[comboUORs.selectedIndex].uor_cod;
                    var codigoCargo = '';
                    if (comboCargos.selectedIndex>0) codigoCargo = cargos[comboCargos.selectedIndex-1][0];
                    document.forms[0].opcion.value="verUsuarios";
                    document.forms[0].target="oculto";
                    document.forms[0].action="<%=request.getContextPath()%>/ListadoFirmasDocumentoProcedimiento.do?codigoUOR="
                        +codigoUOR+"&codigoCargo="+codigoCargo;
                    document.forms[0].submit();                    
                }
                
                function onchangeCodCargo(){
                    comboUsuarios.selectItem(-1);                                                 
                    var codigoUOR = '';
                    if (document.forms[0].UORExpediente.checked==false && comboUORs.selectedIndex>0) {
                        codigoUOR = uors[comboUORs.selectedIndex].uor_cod;
                    }
                    var codigoCargo = '';
                    if (comboCargos.selectedIndex>0) codigoCargo = cargos[comboCargos.selectedIndex-1][0];
                    document.forms[0].opcion.value="verUsuarios";
                    document.forms[0].target="oculto";
                    document.forms[0].action="<%=request.getContextPath()%>/ListadoFirmasDocumentoProcedimiento.do?codigoUOR="
                        +codigoUOR+"&codigoCargo="+codigoCargo;
                    document.forms[0].submit();
                }

       

        </SCRIPT>

    </head>

    <body class="bandaBody"
          onload="javascript:{ pleaseWait('off'); 
              inicializar()}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>


<form  method="post">

    <html:hidden property="codMunicipio" value="" />
    <html:hidden property="entidad" value="1" />
    <html:hidden property="opcion" value="" />

    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("tit_MantFirma")%></div>
    <div class="contenidoPantalla">
        <table id="tablaDatosGral" border="0px" cellspacing="3px" cellpadding="2px">
            <tr>
                <td style="width: 20%" class="etiqueta" id="definicionFirma"  ><%=descriptor.getDescripcion("gEtiq_txtFirmaExp")%>:</td>
                <td class="columnP">
                    <input type="checkbox"  name="defineTramitacion" onclick="onClickDefinible()"/>
                </td>
            </tr>
            <tr>
                <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_unidOrg")%>:</td>
                <td class="columnP">
                    <input type="text" class="inputTexto" name="codUORs"
                           size="8" onkeyup="return xAMayusculas(this);"/>
                    <input type="text" style="width:450;height:17"
                           class="inputTexto" name="descUORs" id="descUORs" readonly/>
                    <A href="javascript:{onClickHrefUOR()}" id="anchorUORs" name="anchorUORs"> 
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                             id="botonUORs" name="botonUORs"></span>
                    </A>
                </td>
            </tr>

            <tr>
                <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_unidExp")%>:</td>
                <td class="columnP">
                    <input type="checkbox"  name="UORExpediente" onclick="onclickUORExpediente()"/>
                </td>
            </tr>

            <tr>
                <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Cargo")%>:</td>
                <td class="columnP">
                    <input type="text" class="inputTexto" name="codCargos" size="8"
                           onkeyup="return xAMayusculas(this);"/>
                    <input type="text" style="width:450;height:17" class="inputTexto"
                           name="descCargos" readonly/> 
                    <A  id="anchorCargos" name="anchorCargos"> 
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                             id="botonCargos" name="botonCargos"></span> 
                    </A>
                </td>
            </tr>

            <tr>
                <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Usuario")%>:</td>
                <td class="columnP">
                    <input type="hidden" name="codUsuario" id="codUsuario"/> 
                    <input type="hidden" class="inputTexto" name="codUsuarios" size="8"
                           onkeyup="return xAMayusculas(this);"/>
                    <input type="text" style="width:450;height:17" class="inputTexto"
                           name="descUsuarios" readonly/> 
                    <A id="anchorUsuarios" name="anchorUsuarios"> 
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                             id="botonUsuarios" name="botonUsuarios"></span> 
                    </A>
                </td>
            </tr>
                                                                            <tr>
                <td style="width: 20%" class="etiqueta" id="tramitarSubsanacion" ><%=descriptor.getDescripcion("gEtiq_tramSub")%>:</td>
                <td class="columnP">
                    <input type="checkbox"  name="tramSubsanacion"/>
                </td>
            </tr>

            <tr >
                <td style="width: 20%" class="etiqueta" id="definicionFinExpedienteRechazar"  ><%=descriptor.getDescripcion("gEtiq_txtFinRechazo")%>:</td>
                <td class="columnP">
                    <input type="checkbox"  name="finExpedienteRechazar" />
                </td>
            </tr>

            <tr>
                <td style="width: 20%" class="etiqueta">
                    <%=descriptor.getDescripcion("etiq_orden")%>:</td>
                <td align="left">
                    <input style="width: 50px" type="text" 
                           class="inputTexto" name="numOrden" 
                           size="3" maxlength="2" onkeyup="return SoloDigitos(this);">
                </td>
            </tr>
        </table>
        <div class="botoneraPrincipal">                
            <INPUT type="button" class="botonGeneral"
                    value='<%=descriptor.getDescripcion("gbAceptar")%>'
                    name="cmdAceptar" onClick="pulsarAceptar();" />
            <INPUT type="button" class="botonGeneral"
                    value='<%=descriptor.getDescripcion("gbSalir")%>' name="cmdSalir"
                    onClick="pulsarSalir();" />
        </div>                        
    </div>                        
</form>

        <script type="text/javascript">
            <%String Agent = request.getHeader("user-agent");%>

            var coordx=0;
            var coordy=0;


            <%if (Agent.indexOf("MSIE") == -1) {%> //Que no sea IE
            window.addEventListener('mousemove', function(e) {
                coordx = e.clientX;
                coordy = e.clientY;
            }, true);
            <%}%>


            document.onmouseup = checkKeys;

            function checkKeysLocal(evento,tecla) {
                var teclaAuxiliar = "";
                if(window.event){
                    evento = window.event;
                    teclaAuxiliar = evento.keyCode;
                }else{
                    teclaAuxiliar = evento.which;
                }
                if (teclaAuxiliar == 1){
                    if (comboUORs.base.style.visibility == "visible" && isClickOutCombo(comboUORs,coordx,coordy)) setTimeout('comboUORs.ocultar()',20);
                    if (comboCargos.base.style.visibility == "visible" && isClickOutCombo(comboCargos,coordx,coordy)) setTimeout('comboCargos.ocultar()',20);
                    if (comboUsuarios.base.style.visibility == "visible" && isClickOutCombo(comboUsuarios,coordx,coordy)) setTimeout('comboUsuarios.ocultar()',20);
                }

                if (teclaAuxiliar == 9){
                    if (comboUORs.base.style.visibility == "visible") comboUORs.ocultar();
                    if (comboCargos.base.style.visibility == "visible") comboCargos.ocultar();
                    if (comboUsuarios.base.style.visibility == "visible") comboUsuarios.ocultar();
                }

                keyDel(evento);
            }

            var comboUORs = new Combo("UORs");
            var comboCargos = new Combo("Cargos");
            var comboUsuarios = new Combo("Usuarios");
       
            comboUORs.anchor.onclick = function() {}
            comboCargos.anchor.onclick = function() {}
            
            comboUORs.change = function() {
                onchangeCodUOR();
            }

            comboCargos.change = function() {
                onchangeCodCargo();
            }
            
            comboCargos.deactivate();

        </script>
    </BODY>
</html:html>
