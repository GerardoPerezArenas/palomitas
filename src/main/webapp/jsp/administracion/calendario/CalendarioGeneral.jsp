
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>

<%@ page import="es.altia.agora.interfaces.user.web.util.*"%>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="java.util.Calendar"%>

<%
            int idioma = 1;
            int apl = 1;
            int munic = 0;
            String css="";
            if (session != null) {
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    apl = usuario.getAppCod();
                    munic = usuario.getOrgCod();
                    css=usuario.getCss();
                }
            }

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
			
			Calendar cal= Calendar.getInstance();
			int anioActual = cal.get(Calendar.YEAR);
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod"   value="<%=idioma%>"/>
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>"/>

<html:html>
    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <title><%=descriptor.getDescripcion("TituCalLabGen")%></title>
        
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/date.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >
        <script type="text/javascript">
            
            var festivos    = new Array();
            var festivosAux = new Array();
            var diasOcupados     = new Array();
            var i=0;
            
            // Variable para poner el el combo de los años el año actual y posteriores
            var fecha_aux = fechaHoy();
            var ano_aux = fecha_aux.substring(6,10);
            
            
            
            <logic:iterate id="elemento" name="GestionForm" property="lista_festivos_generales">
            festivos[i] = '<bean:write name="elemento"/>'; i++;
            </logic:iterate> i=0;
            
            <logic:iterate id="elemento" name="GestionForm" property="lista_dias_ocupados">
            diasOcupados[i] = '<bean:write name="elemento"/>'; i++;
            </logic:iterate>
            
            festivosAux = festivos;
            
            var curDate   = new Date();
            var todayDate = new Date();
            var days      = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
            var dow;
            dow = ['L', 'M', 'X', 'J', 'V', 'S', 'D'];
            
            
            var meses = ['<%=descriptor.getDescripcion("TxtEnero")%>', '<%=descriptor.getDescripcion("TxtFebrero")%>', '<%=descriptor.getDescripcion("TxtMarzo")%>',
                '<%=descriptor.getDescripcion("TxtAbril")%>', '<%=descriptor.getDescripcion("TxtMayo")%>', '<%=descriptor.getDescripcion("TxtJunio")%>',
                '<%=descriptor.getDescripcion("TxtJulio")%>', '<%=descriptor.getDescripcion("TxtAgosto")%>', '<%=descriptor.getDescripcion("TxtSeptiembre")%>',
                '<%=descriptor.getDescripcion("TxtOctubre")%>', '<%=descriptor.getDescripcion("TxtNoviembre")%>', '<%=descriptor.getDescripcion("TxtDiciembre")%>' ];
                
                
                function inicio(){
                    
                    var cadenaerrores="";
                    var i=0;
                    
                    <logic:present scope="session" name="GestionForm">
                    <logic:iterate id="mensaje" scope="session" name="GestionForm" property="listaMensajesError">
                    cadenaerrores += '<bean:write name="mensaje"/>' + '¥§¥';
                    </logic:iterate>
                    </logic:present>
                    
                    if(cadenaerrores != "")
                        jsp_alerta('A',cadenaerrores);
                    
                    document.forms[0].cadenaerrores.value=cadenaerrores;
                    
                    var hoy = new Date();
                    
                    mostrarMeses(hoy.getYear(), 'todos');
                    
                    // Hacemos que el año en curso sea el que se muestra en el combo como año por defecto 
                    for (i=0; i<document.forms[0]["ano"].length; i++) {
                        if (document.forms[0]["ano"][i].value == ano_aux) {
                            document.forms[0]["ano"][i].selected = true;
                             cambiarCalendario();
                        }
                    }
                }
                
                
                function cambiarCalendario(){
                    mostrarMeses(document.forms[0].ano.value, 'todos');
                }
                
                
                function mostrarMeses(whatYear, numeroMes){
                    
                    var lastYear  = whatYear;
                    var nextYear  = whatYear;
                    
                    var mes, column, lastMonth, nextMonth, firstDay, startDay;
                    var i,j,k;
                    
                    if(numeroMes=='todos'){
                        j=0;
                        k=12;
                    }
                    else{
                        j=numeroMes;
                        numeroMes++;
                        k=numeroMes;
                    }
                    
                    for(;j<k;j++){
                        curDate.setMonth(j);
                        curDate.setFullYear(whatYear);
                        curDate.setDate(todayDate.getDate());
                        
                        mes='';  column=0;  lastMonth=j-1;  nextMonth=j+1;
                        
                        firstDay = new Date(whatYear,j,1);
                        startDay = firstDay.getDay() - 1;
                        if(startDay == -1)
                            startDay = 6;
                        
                        if (((whatYear % 4 == 0) && (whatYear % 100 != 0)) || (whatYear % 400 == 0))
                            days[1] = 29;
                        else
                            days[1] = 28;
                        
                        mes += '<' + 'table style="width:80;background-color:#004595" class="cal-Table" cellspacing="0" cellpadding="0"' + '>';
                        mes += '<' + 'tr' + '>' + '<' + 'td class="cal-HeadCell" align="center"' + '>' + meses[j] + '<' + '/td' + '>' + '<' + '/tr' + '>';
                        mes += '<' + 'tr' + '>' + '<' + 'td' + '>';
                        mes += '<' + 'table style="width:80" border="0" class="cal-Table" cellspacing="0" cellpadding="0"' + '>';
                        mes += '<' + 'tr' + '>' + '<' + 'td class="cal-HeadCell" align="center" style="width:100%"' + '>' + '<' + '/td' + '>' + '<' + '/tr' + '>';
                        mes += '<' + 'tr' + '>' + '<' + 'td style="width:100%" align="center"' + '>';
                        mes += '<' + 'table style="width:80" cellspacing="1" cellpadding="1" border="0"' + '>' + '<' + 'tr' + '>';
                        
                        for (i=0; i<7; i++)
                            mes += '<' + 'td class="cal-Semana" align="center" valign="middle"' + '>' + dow[i] + '<' + '/td'+ '>';
                        
                        mes += '<' + '/tr' + '>' + '<' + 'tr' + '>';
                        
                        if (lastMonth == -1){
                            lastMonth = 11;
                            lastYear=lastYear-1;
                        }
                        
                        for(i=0; i<startDay; i++, column++){
                            if(column==6)
                                mes += getDayLink((days[lastMonth]-startDay+i+1),true,lastMonth,lastYear,true);
                            else
                                mes += getDayLink((days[lastMonth]-startDay+i+1),true,lastMonth,lastYear,false);
                        }
                        
                        for(i=1; i<=days[j]; i++, column++){
                            if(column==6)
                                mes += getDayLink(i,false,j,whatYear,true);
                            else
                                mes += getDayLink(i,false,j,whatYear,false);
                            
                            if (column == 6){
                                mes += '<' + '/tr' + '>' + '<' + 'tr' + '>';
                                column = -1;
                            }
                        }
                        
                        if(nextMonth==12){
                            nextMonth=0;
                            nextYear=nextYear+1;
                        }
                        
                        if(column > 0){
                            for (i=1; column<7; i++, column++){
                                if(column==6)
                                    mes +=  getDayLink(i,true,nextMonth,nextYear,true);
                                else
                                    mes +=  getDayLink(i,true,nextMonth,nextYear,false);
                            }
                            
                            mes += '<' + '/tr' + '>' + '<' + '/table' + '>' + '<' + '/td' + '>' + '<' + '/tr' + '>' + '<' + '/table' + '>';
                            mes += '<' + '/td' + '>' + '<' + '/tr' + '>' + '<' + '/table' + '>';
                        }
                        else{
                            mes = mes.substr(0,mes.length-4);
                            mes += '<' + '/table' + '>' + '<' + '/td' + '>' + '<' + '/tr' + '>' + '<' + '/table' + '>';
                            mes += '<' + '/td' + '>' + '<' + '/tr' + '>' + '<' + '/table' + '>';
                        }
                        
                        domlay(meses[j],1,0,0,mes);
                    }
                }
                
                
                function getDayLink(linkDay,isGreyDate,linkMonth,linkYear,domingo){
                    var templink;
                    var dia='';
                    var mes='';
                    var fecha='';
                    var i=0,j=0;
                    
                    linkMonth++;
                    
                    if(linkDay < 10)  dia = '0' + linkDay;
                    else              dia = linkDay;
                    
                    if(linkMonth < 10) mes = '0' + linkMonth;
                    else              mes = linkMonth;
                    
                    fecha = linkYear + '-' + mes + '-' + dia;
                    
                    linkMonth--;
                    
                    if(isGreyDate)
                        templink = '<' + 'td align="center" class="cal-GreyDate"' + '>' + linkDay + '<' + '/td' + '>';
                    
                    else{
                        if(domingo)
                            templink = '<' + 'td align="center" class="cal-DayCell"' + '>' + '<' + 'a class="cal-DayFestivo"' + '>' + linkDay + '<' + '/td' + '>';
                        else{
                            for(;i<festivos.length;i++) {
                                if(fecha == festivos[i]){ j++;  break; }
                            }
                            
                            if(j==0)
                                templink = '<' + 'td align="center" class="cal-DayCell"' + '>' + '<' + 'a class="cal-DayLink"    href=javascript:changeDay("' + fecha + '","' + linkMonth + '");' + '>' + linkDay + '<' + '/td' + '>';
                            else
                                templink = '<' + 'td align="center" class="cal-DayCell"' + '>' + '<' + 'a class="cal-DayFestivo" href=javascript:changeDay("' + fecha + '","' + linkMonth + '");' + '>' + linkDay + '<' + '/td' + '>';
                        }
                    }
                    
                    return templink;
                }
                
                
                function changeDay(fecha, numeroMes){
                    var i=0,j=0,k=0;
                    var year = fecha.substring(0,4);
                    
                    for(;i<festivos.length;i++){
                        if(fecha == festivos[i]){
                            festivos[i]='';
                            j++;
                            actualizarFestivos();
                            break;
                        }
                    }
                    
                    if(j==0){
                        for(i=0;i<diasOcupados.length;i++){
                            if(fecha == diasOcupados[i]){
                                k++;
                                break;
                            }
                        }
                        
                        if(k==0) festivos[festivos.length] = fecha;
                        else     alert('<%=descriptor.getDescripcion("MsgDiaOcupado")%>');
                        }
                        
                        mostrarMeses(year, numeroMes);
                    }
                    
                    
                    function actualizarFestivos(){
                        var i,j;
                        
                        festivosAux=new Array();
                        
                        for(i=0,j=0;i<festivos.length;i++){
                            if(festivos[i]!=''){
                                festivosAux[j]= festivos[i];
                                j++;
                            }
                        }
                        
                        festivos=festivosAux;
                        }
                    
                    function grabar(){
                        var listaFestivos='';
                        var fecha='';
                        var i;
                        
                        for(i=0;i<festivos.length;i++){
                            fecha = festivos[i].substring(8) + '/' + festivos[i].substring(5,7) + '/' + festivos[i].substring(0,4);
                            listaFestivos = listaFestivos + fecha + 'zxz';
                        }
                        
                        document.forms[0].festivo.value=listaFestivos;
                        document.forms[0].accion.value = 'CalendarioGeneral.Grabar';
                        document.forms[0].target='oculto';
                        document.forms[0].submit();
                    }
                    
                    function salir(){
                        /*document.forms[0].opcion.value = 'cargar';
                        document.forms[0].target = "mainFrame";
                        document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/UORs.do';
                        document.forms[0].submit();*/
                        document.forms[0].target = "mainFrame";
                        document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
                        document.forms[0].submit();
                    }
                    
    </script>
</head>
<body class="bandaBody" onload="javascript:{inicio();}">
    <html:form action="/EntradaGestion.do">
        <html:hidden  property="opcion" value=""/>

        <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("TituCalLabGen")%></div>
        <div class="contenidoPantalla">
            <table style="width: 100%">
                <TR>	
                    <TD>


                        <!--Viejo-->

                        <tr>
                            <td style="width:1px;"  class="columnP"></td>
                            <td style="height:5px"></td>
                            <td style="width:1px;"  class="columnP"></td>
                        </tr>


                        <tr height="25">
                            <td class="columnP"></td>
                            <td class="columnP">
                                &nbsp;
                            </td>
                            <td class="columnP"></td>
                        </tr>
                        <tr>
                            <td class="columnP"></td>
                            <td class="columnP">
                                <table style="width:100%">
                                    <tr valign="top">
                                        <td style="width:3%">&nbsp;</td>
                                        <td style="width:14%">
                                            <div id="<%=descriptor.getDescripcion("TxtEnero")%>"></div>
                                        </td>
                                        <td style="width:2%">&nbsp;</td>
                                        <td style="width:14%">
                                                <div id="<%=descriptor.getDescripcion("TxtFebrero")%>"></div>
                                        </td>
                                        <td style="width:2%">&nbsp;</td>
                                        <td style="width:14%">
                                            <div id="<%=descriptor.getDescripcion("TxtMarzo")%>"></div>
                                        </td>
                                        <td style="width:2%">&nbsp;</td>
                                        <td style="width:14%">
                                            <div id="<%=descriptor.getDescripcion("TxtAbril")%>"></div>
                                        </td>
                                        <td style="width:2%">&nbsp;</td>
                                        <td style="width:14%">
                                            <div id="<%=descriptor.getDescripcion("TxtMayo")%>"></div>
                                        </td>
                                        <td style="width:2%">&nbsp;</td>
                                        <td style="width:14%">
                                            <div id="<%=descriptor.getDescripcion("TxtJunio")%>"></div>
                                        </td>
                                        <td style="width:3%">&nbsp;</td>
                                    </tr>
                                </table>

                                <table style="width:100%">
                                    <tr height="35">
                                        <td valign="middle" class="columnP">
                                            <hr width="95%" noshade size="1" color="#666666">
                                        </td>
                                    </tr>
                                </table>

                                <table style="width:100%" border="0" cellspacing="0" cellpadding="0">
                                    <tr valign="top">
                                        <td style="width:3%">&nbsp;</td>
                                        <td style="width:14%">
                                            <div id="<%=descriptor.getDescripcion("TxtJulio")%>"></div>
                                        </td>
                                        <td style="width:2%">&nbsp;</td>
                                        <td style="width:14%">
                                            <div id="<%=descriptor.getDescripcion("TxtAgosto")%>"></div>
                                        </td>
                                        <td style="width:2%">&nbsp;</td>
                                        <td style="width:14%">
                                            <div id="<%=descriptor.getDescripcion("TxtSeptiembre")%>"></div>
                                        </td>
                                        <td style="width:2%">&nbsp;</td>
                                        <td style="width:14%">
                                            <div id="<%=descriptor.getDescripcion("TxtOctubre")%>"></div>
                                        </td>
                                        <td style="width:2%">&nbsp;</td>
                                        <td style="width:14%">
                                            <div id="<%=descriptor.getDescripcion("TxtNoviembre")%>"></div>
                                        </td>
                                        <td style="width:2%">&nbsp;</td>
                                        <td style="width:14%">
                                            <div id="<%=descriptor.getDescripcion("TxtDiciembre")%>"></div>
                                        </td>
                                        <td style="width:3%">&nbsp;</td>
                                    </tr>
                                </table>
                            </td>
                            <td class="columnP"></td>
                        </tr>
                        <tr height="20">
                            <td class="columnP"></td>
                            <td class="columnP">
                                &nbsp;
                            </td>
                            <td class="columnP"></td>
                        </tr>
                        <tr>
                            <td class="columnP"></td>
                            <td class="columnP"></td>
                            <td class="columnP"></td>
                        </tr>
                        <tr>
                            <td class="columnP"></td>
                            <td class="columnP">
                                <table style="width:100%" border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td class="columnP">
                                            Año&nbsp;&nbsp;
                                            <select name="ano" id="ano" onchange="cambiarCalendario();">
												<!-- Se modifica la carga del combo de años con valores fijos, por una carga de 10 años menos y 5 más -->
												<% for(int i = anioActual - 10; i < anioActual + 6; i++){ %>
													<option value="<%=i%>"><%=i%>
												<% } %>
                                             </select>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <td class="columnP"></td>
                        </tr>
                        <tr>
                            <td style="width:1"  class="columnP"></td>
                            <td style="height:5px"></td>
                            <td style="width:1"  class="columnP"></td>
                        </tr>
                        <!--Fin de lo viejo-->
                    </TD>
                </TR>
            </TABLE>								
        <div class="botoneraPrincipal"> 
            <input type="button" class="botonGeneral"  value="<%=descriptor.getDescripcion("gbGrabar")%>" onclick="javascript:grabar();">
            <input type="button" class="botonGeneral"  value="<%=descriptor.getDescripcion("gbSalir")%>" onclick="javascript:salir();">
        </div>      
    </div>      

<input type="hidden" name="festivo" value="">
<input type="hidden" name="accion" value="">
<!-- Parametro para la ventana de errores -->
<input type="hidden" name="cadenaerrores" value="">
</html:form>
</body>
</html:html>
    
