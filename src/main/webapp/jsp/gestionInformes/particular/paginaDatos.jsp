<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="java.util.ArrayList" %>
<%@page import="es.altia.agora.technical.Fecha" %>
<%@page import="java.util.Date" %>
<%@page import="es.altia.agora.business.gestionInformes.particular.CampoEntradaParticular" %>
<%@page import="es.altia.agora.business.gestionInformes.particular.CodigoEtiqueta" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%
    response.setHeader("Cache-control","no-cache");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader ("Expires", 0);

    int idioma = 1;
    int apl = 1;
    if (session != null) {
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        if (usuario != null) {
            idioma = usuario.getIdioma();
            apl = usuario.getAppCod();
        }
    }    
%>

<!-- Estilos -->
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<LINK rel="stylesheet" href="<%=request.getContextPath()%>/css/estilo.css" type="text/css">
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/dojo/dojo.js"></script>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />


<script type="text/javascript">
    dojo.require("dojo.widget.*");
    dojo.require("dojo.io.*");
    dojo.require("dojo.event.*");
    dojo.require("dojo.html.*");
    dojo.require("dojo.dnd.*");
    

var listaArray = new Array();





function generarInforme() {

	if(validarCampos() && validarFormulario()){

        document.forms[1].action = DOMAIN_NAME+"<c:url value='/gestionInformes/GenerarInformeParticular.do'/>";
        document.forms[1].submit();
	}
	else 
		{
		jsp_alerta("A",'<%=descriptor.getDescripcion("seleccionLista")%>');
		}
}


function botonesRadio(grupoRadio){
	eval ('radios = document.forms[1].'+grupoRadio);
    var i;
    for (i=0;i<radios.length;i++){
       if (radios[i].checked)
          return radios[i].value;
    }    
} 

    
function mostrarCalendario(campoTexto,imagen,evento) {
                  if(window.event) evento = window.event;
 
	showCalendar('forms[1]',campoTexto, null, null, null, '', imagen,'',null,null,null,null,null,null,null,'',evento);
}

/*Redefinimos aqui el showCalendar debido a que la posición relativa del contenedor principal no está en esta jsp*/
function showCalendar(frmName, dteBox, dteDia, dteMes, dteAno, dteBox2, btnImg, list_elements, hideDrops, MnDt, MnMo, MnYr, MxDt, MxMo, MxYr,runFuncs,evento) {
    list = list_elements;
    hideDropDowns = hideDrops;
    FuncsToRun = runFuncs;
    calfrmName = frmName;

    if (IsCalendarVisible) {
        hideCalendar();
    }
    else {
        if (hideDropDowns) {toggleDropDowns('hidden');}
        if ((MnDt!=null) && (MnMo!=null) && (MnYr!=null) && (MxDt!=null) && (MxMo!=null) && (MxYr!=null)) {
            IsUsingMinMax = true;
            minDate.setDate(MnDt);
            minDate.setMonth(MnMo-1);
            minDate.setFullYear(MnYr);
            maxDate.setDate(MxDt);
            maxDate.setMonth(MxMo-1);
            maxDate.setFullYear(MxYr);
        } else {
            IsUsingMinMax = false;
        }        		
		
        curImg = btnImg;
        curDateBox = dteBox;
        curDateBox2 = dteBox2;
        curDateDiaBox=dteDia;
        curDateMesBox=dteMes;
        curDateAnoBox=dteAno;

        anhoReplegado=true;
        var dimContPantalla = document.getElementsByClassName("contenidoPantalla")[0].getBoundingClientRect();
       var scrollContPantalla= document.getElementsByClassName("contenidoPantalla")[0].scrollTop;
      
        ppcY = posicionY(evento)+10 - dimContPantalla.top+scrollContPantalla;
        ppcX = posicionX(evento)-250;

      
     
      
        
        if(ppcX+170>dimContPantalla.width) 
            ppcX = ppcX - 165;
        if(ppcY+170>(dimContPantalla.height+scrollContPantalla) )
            ppcY = ppcY - 133;
        


        domlay('popupcalendar',1,ppcX,ppcY,Calendar(todayDate.getMonth(),todayDate.getFullYear()));       

        IsCalendarVisible = true;
    }
}


function validarCampos(){

<%

ArrayList listaCampos = (ArrayList) request.getAttribute ("listaCampos");
CampoEntradaParticular cep;
String tip="";
String identificador="";
String identificador2="";
String cal1="";
String cal2="";

	for(int i=0; i<listaCampos.size();i++){
		cep = (CampoEntradaParticular) (listaCampos.get(i)); 
		tip = (String) cep.getTipoCampo(); 
		identificador = (String) cep.getIdCampo();
		
		if(tip.equals("LIST")){%>		
                       
                  
			eval('d2 = document.getElementById("'+ '<%=identificador%>' +'2");');
			eval('new dojo.dnd.HtmlDropTarget(d2, ["li'+ '<%=identificador%>' +'1"]);')
			lis2 = d2.getElementsByTagName("div");
			var seleccion="";
			if(lis2.length>0){			
				for(var x=0; x<lis2.length; x++){
					var item = lis2[x].id;
					seleccion += listaArray[item][1] + '§¥';
				}				
				eval('document.forms[1].lista'+ '<%=identificador%>' +'.value=seleccion;');
			}
			else{
				return false;
			}
		<%}

		else if (tip.equals("CALENDAR")){%>
			validarFecha(document.getElementById('<%=identificador%>'),0);
		<%}
		
		else if (tip.equals("RANGO_CALENDAR")){
			cal1=identificador+"Desde";
			cal2=identificador+"Hasta";%>

                        var datos1 = new Array();
                        datos1 = document.getElementsByName('<%=cal1%>');

                        var datos2 = new Array();
                        datos2 = document.getElementsByName('<%=cal2%>');

                        validarFecha(datos1[0],0);
			validarFecha(datos2[0],0);
			validarFechaAnterior(datos1[0].value,datos2[0].value);

                        /*
			validarFecha(document.getElementById('<%=cal1%>'),0);
			validarFecha(document.getElementById('<%=cal2%>'),0);
			validarFechaAnterior(document.getElementById('<%=cal1%>').value,document.getElementById('<%=cal2%>').value);
                       */

		<%}
		else if (tip.equals("RADIO")){
			identificador2=identificador + "Radio";%>

                        var datos1 = new Array();
                        datos1 = document.getElementsByName('<%=identificador%>');

                        datos1[0].value=botonesRadio('<%=identificador2%>');

			//document.getElementById('<%=identificador%>').value=botonesRadio('<%=identificador2%>');
		<%}

	}
                
            
                %>

return true;
}


function inicializar(){
cont = 0; 

<%
ArrayList lCampos = (ArrayList) request.getAttribute ("listaCampos");
String tipo="";
String cod="";
String etiqueta="";
ArrayList etiqs;
CampoEntradaParticular campo;
CodigoEtiqueta codEtiq;
String id="";

	for(int i=0; i<lCampos.size();i++){
		campo = (CampoEntradaParticular) (lCampos.get(i)); 
		tipo = (String) campo.getTipoCampo(); 
		etiqs = (ArrayList) campo.getValoresCampo();
		id = (String) campo.getIdCampo();
		
		if(tipo.equals("LIST")){
		
			%>
			eval('var '+ '<%=id%>' +' = document.getElementById("'+ '<%=id%>' +'");');
			eval('var '+ '<%=id%>' +'2 = document.getElementById("'+ '<%=id%>' +'2");');
            <%
		
			for(int j=0;j<etiqs.size();j++){
				codEtiq = (CodigoEtiqueta) etiqs.get(j);
				etiqueta= (String) codEtiq.getEtiqueta();
				cod = (String) codEtiq.getCodigo();%>
				elemento = document.createElement("div");
				elemento.id = cont;
				elemento.appendChild(document.createTextNode('<%=etiqueta%>'));
            	<%String instruccionJs=campo.getIdCampo()+".appendChild(elemento);";%>
            	eval('<%=instruccionJs%>');
				detallesCampo = new Array(4);
				detallesCampo[0] = '<%=etiqueta%>'; // CODIGO
				detallesCampo[1] = '<%=cod%>'; // NOMBRE
				detallesCampo[2] = '0'; // POSX
				detallesCampo[3] = '0'; // POSY
				listaArray.push(detallesCampo);
				cont++;
			<%}%>//Fin for j
			
		eval('<%=id%>'+'1 = document.getElementById("' + '<%=id%>' + '");');
		eval('new dojo.dnd.HtmlDropTarget('+'<%=id%>'+'1, ["li'+'<%=id%>'+'1"]);');
        eval('lis = '+'<%=id%>'+'1.getElementsByTagName("div");');
        
        for(var x=0; x<lis.length; x++){
        eval('new dojo.dnd.HtmlDragSource(lis[x], "li'+ '<%=id%>' +'1");');
         //deslizamiento
        }
        
        eval('<%=id%>'+'2 = document.getElementById("' + '<%=id%>' + '2");');
        eval('new dojo.dnd.HtmlDropTarget('+'<%=id%>'+'2, ["li'+'<%=id%>'+'1"]);');
        eval('lis = '+'<%=id%>'+'2.getElementsByTagName("div");');

        for(var y=0; y<lis.length; y++){
        eval('new dojo.dnd.HtmlDragSource(lis[y], "li'+'<%=id%>'+'1");');
        }
		<%}//Fin if list	
		if(tipo.equals("CALENDAR")){
			codEtiq = (CodigoEtiqueta) etiqs.get(0);
			etiqueta= (String) codEtiq.getEtiqueta();%>
			habilitarImagenCal('<%=etiqueta%>', true);
		<%}
			if(tipo.equals("RANGO_CALENDAR")){
			codEtiq = (CodigoEtiqueta) etiqs.get(0);
			etiqueta= (String) codEtiq.getEtiqueta();
			String etiqueta1=etiqueta+"Desde";
			String etiqueta2=etiqueta+"Hasta";%>
			habilitarImagenCal('<%=etiqueta1%>', true);
			habilitarImagenCal('<%=etiqueta2%>', true);
		<%}%>
			<%if(tipo.equals("RADIO")){%>
			eval('document.forms[1].'+'<%=id%>'+'Radio[0].checked=true;');
		<%}%>	
	<%}%> //fin for i
}//Fin inicializar

  
function comprobarFecha(inputFecha) {
	if (Trim(inputFecha.value) != '') {
		if (!ValidarFechaConFormato(document.forms[1], inputFecha)) {
			inputFecha.focus();
			return false;
		}
	}
	return true;
}
    
    
</script>
<form name="form" action="/gestionInformes/GenerarInformeParticular.do" target="_self">
<head><jsp:include page="/jsp/gestionInformes/tpls/app-constants.jsp" />

<style type="text/css">

    html>body.nuevo div.cuerpoPagina div.cuerpoAplicacion div.filtroGestionInformes {
    width:95%;
    height:75%;
    border: 2px solid #ccc;
    margin-bottom: 5px;
    margin-left: 25px;
}

 html>body.nuevo div.cuerpoPagina div.cuerpoAplicacion div.filtroGestionInformes div.filtroGestionInformesCampos {
    width:100%;
    padding:0.5em;
    float:left;
    text-align:left;
    width: 83%;
    margin-left: 70px;
    line-height: 30px;
}


div.contenidoTab div.camposSeleccionables
{
    color: #000000;
    float:left;
    width:"50%";
    border:0px solid red;
    height:120px;
    overflow-x:hidden;
    overflow-y: hidden;
    text-align:left;
    padding-top:10px;
}

div.contenidoTab div.camposSeleccionables ul.listaCampos
{
    border:2px solid #999999;
    height:100px;
    width:300px;
    background:#FFFFFF;
    overflow-x:auto;
    overflow-y: auto;
    text-align:left;
    cursor:pointer;
}


</style>

    <div class="filtroGestionInformes">
        <div class="filtroGestionInformesCampos">
            <c:forEach items="${requestScope.listaCampos}" var="campo">
                <div>
                    <span class="etiqBoldMaxi"><c:out value="${campo.tituloCampo}"/></span>
                    
                    <c:if test="${campo.tipoCampo eq 'SELECT'}">
                        <span>
		                    <select class="inputTexto" id="<c:out value="${campo.idCampo}"/>" name="<c:out value="${campo.idCampo}"/>">
                                <c:forEach items="${campo.valoresCampo}" var="valor">
                                    <option value="<c:out value="${valor.codigo}"/>"><c:out value="${valor.etiqueta}"/></option>
                                </c:forEach>
                            </select>
	                    </span>
                    </c:if>
                    
                    <c:if test="${campo.tipoCampo eq 'RADIO'}">
                        <span>           
                        <input type="hidden" style="" name="<c:out value="${campo.idCampo}"/>"/>
                            <c:forEach items="${campo.valoresCampo}" var="valor">
                               <input type="radio" id="<c:out value="${campo.idCampo}${'Radio'}"/>" name="<c:out value="${campo.idCampo}${'Radio'}"/>" class="textoSuelto"
                               value="<c:out value="${valor.codigo}"/>"><c:out value="${valor.etiqueta}"/>
                            </c:forEach>                            
	                    </span>
                    </c:if>
              
					<c:if test="${campo.tipoCampo eq 'CALENDAR'}">
						<span>
							<c:forEach items="${campo.valoresCampo}" var="valor">
								<INPUT TYPE="text" id="obligatorio" class="inputTxtFechaObligatorio" size="12" maxlength="10"
								name="<c:out value="${valor.codigo}"/>"
								onkeypress="javascript:return soloCaracteresFecha(event);"
								onblur="javascript:return comprobarFecha(this);"
								onfocus="this.select();">
					<A href="javascript:calClick(event);return false;"
								onClick="mostrarCalendario('<c:out value="${valor.codigo}"/>','<c:out value="${valor.etiqueta}"/>');
								return false;" >
                                                                <span class="fa fa-calendar" aria-hidden="true" alt="Calendario"   id="<c:out value="${valor.etiqueta}"/></span>" name="<c:out value="${valor.etiqueta}"/>" width="25">
								</A>
							</c:forEach>  
						</span>
					</c:if>
                    
                  
					<c:if test="${campo.tipoCampo eq 'RANGO_CALENDAR'}">
						<span>
							<c:forEach items="${campo.valoresCampo}" var="valor">
								<INPUT TYPE="text" id="obligatorio" class="inputTxtFechaObligatorio" size="12" maxlength="10"
								name="<c:out value="${valor.codigo}${'Desde'}"/>"
								onkeypress="javascript:return soloCaracteresFecha(event);"
								onblur="javascript:return comprobarFecha(this);"
								onfocus="this.select();">
								<A href="javascript:calClick(event);return false;"
								onClick="mostrarCalendario('<c:out value="${valor.codigo}${'Desde'}"/>','<c:out value="${valor.etiqueta}${'Desde'}"/>','event');
								return false;" >
								<span class="fa fa-calendar" aria-hidden="true" id="<c:out value="${valor.etiqueta}${'Desde'}"/>" name="<c:out value="${valor.etiqueta}${'Desde'}"/>" alt="Calendario"
								></span>
								</A>
							</span>
							&nbsp&nbsp&nbsp
							<span>
							
								<INPUT TYPE="text" id="obligatorio" class="inputTxtFechaObligatorio" size="12" maxlength="10"
								name="<c:out value="${valor.codigo}${'Hasta'}"/>"
								onkeypress="javascript:return soloCaracteresFecha(event);"
								onblur="javascript:return comprobarFecha(this);"
								onfocus="this.select();">
								<A href="javascript:calClick(event);return false;"
								onClick="mostrarCalendario('<c:out value="${valor.codigo}${'Hasta'}"/>','<c:out value="${valor.etiqueta}${'Hasta'}"/>','event');
								return false;" >
								<span class="fa fa-calendar" aria-hidden="true" id="<c:out value="${valor.etiqueta}${'Hasta'}"/>" name="<c:out value="${valor.etiqueta}${'Hasta'}"/>" alt="Calendario"
								></span>
								</A>
							</c:forEach>  
						
						</span>
					</c:if>
                    <c:if test="${campo.tipoCampo eq 'LIST'}">
                        <span>
            				<div  class="contenidoTab" id="tablasDojo" dojoType="ContentPane" 
            				label="<%=descriptor.getDescripcion("gbInformePermisos")%>">
							<input type="hidden" id="<c:out value="${'lista'}${campo.idCampo}"/>" name="<c:out value="${'lista'}${campo.idCampo}"/>">
                			<div class="camposSeleccionables">
                    		<ul id="<c:out value="${campo.idCampo}"/>" class="listaCampos"></ul>
                			</div>
                			<div class="camposSeleccionables">
                    		<ul id="<c:out value="${campo.idCampo}${2}"/>" class="listaCampos"></ul>
                			</div>
            				</div>
	                    </span>
                    </c:if>
                    <p></p>
                </div>
            </c:forEach>
        </div>
    </div>

</form>



<script type="text/javascript">
    inicializar();
</script>
