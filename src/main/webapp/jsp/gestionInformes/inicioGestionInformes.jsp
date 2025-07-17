<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
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
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/dojo/dojo.js"></script>
<script type="text/javascript">
    dojo.require("dojo.widget.SortableTable");

    var listaAmbitos = new Array();
    var cont=0;
    <logic:iterate id="elemento" name="FichaInformeForm" property="listaAmbitos">
      listaAmbitos[cont] = ['<bean:write name="elemento" property="codigo" />', '<bean:write name="elemento" property="descripcion" />'];
      cont++;
   </logic:iterate>

    var listaProcedimientos = new Array();
    var cont=0;
    <logic:iterate id="elemento" name="FichaInformeForm" property="listaProcedimientos">
      listaProcedimientos[cont] = ['<bean:write name="elemento" property="codigo" />', '<bean:write name="elemento" property="descripcion" />'];
      cont++;
   </logic:iterate>
      
    var listaInformes = new Array();
    var cont=0;
    <logic:iterate id="elemento" name="GestionInformesForm" property="listaInformes">
      listaInformes[cont] = ['<bean:write name="elemento" property="titulo" />'];
      cont++;
   </logic:iterate>

    var dlg, dlg3;
    function init(e) {
        dlg = dojo.widget.byId("DialogContent");
        var btn = document.getElementById("cancelarDialog");
        dlg.setCloseControl(btn);
        dlg3 = dojo.widget.byId("DialogContent3");
        var btn3 = document.getElementById("cancelarDialog3");
        dlg3.setCloseControl(btn3);
    }
    dojo.addOnLoad(init);

</script>

<script type="text/javascript">
    function actualizarProcedimiento(){
        var w=dojo.widget.byId("parsedFromHtml2");
        if(w){
            var s=w.getValue();
            if(s.length>0) document.forms[0].codProcedimiento.value=s;
            for (i=0;i<listaProcedimientos.length;i++) {
                if (s==listaProcedimientos[i][0]) {
                    document.forms[0].descProcedimiento.value=listaProcedimientos[i][1];
                    break;
                }
            }
            document.getElementById("cancelarDialog").click();
        }
    }

    function cambiarProcedimiento(){
        var w=dojo.widget.byId("parsedFromHtml2");
        var s = document.forms[0].codProcedimiento.value;
        var encontrado=false;
        for (i=0;i<listaProcedimientos.length;i++) {
            if (s==listaProcedimientos[i][0]) {
                document.forms[0].descProcedimiento.value=listaProcedimientos[i][1];
                encontrado = true;
            }
        }
        if (!encontrado) {
            document.forms[0].codProcedimiento.value="";
            document.forms[0].descProcedimiento.value="";
        }
    }

    function actualizarAmbito(){
        var comboProced = [document.forms[0].codProcedimiento,document.forms[0].descProcedimiento,
                        document.getElementById("botonProcedimientoBusqueda")];
        var w=dojo.widget.byId("parsedFromHtml3");
        if(w){
            var s=w.getValue();
            if(s.length>0) document.forms[0].codAmbito.value=s;
            for (i=0;i<listaAmbitos.length;i++) {
                if (s==listaAmbitos[i][0]) {
                    document.forms[0].descAmbito.value=listaAmbitos[i][1];
                    if (s=="1") { // CASO ÄMBITO EXPEDIENTE
                        document.getElementById("botonProcedimientoBusqueda").style.color="#0B3090 !important";
                        habilitarGeneral(comboProced);
                    } else {
                        document.getElementById("botonProcedimientoBusqueda").style.color="#f6f6f6 !important";
                        deshabilitarGeneral(comboProced);
                        document.forms[0].codProcedimiento.value="";
                        document.forms[0].descProcedimiento.value="";
                    }
                    break;
                }
            }
            document.getElementById("cancelarDialog3").click();
        }
    }

    function cambiarAmbito(){
        var w=dojo.widget.byId("parsedFromHtml3");
        var s = document.forms[0].codAmbito.value;
        var encontrado=false;
        var comboProced = [document.forms[0].codProcedimiento,document.forms[0].descProcedimiento,
                        document.getElementById("botonProcedimientoBusqueda")];
        for (i=0;i<listaAmbitos.length;i++) {
            if (s==listaAmbitos[i][0]) {
                document.forms[0].descAmbito.value=listaAmbitos[i][1];
                encontrado = true;
                if (s=="1") { // CASO ÄMBITO EXPEDIENTE
                    document.getElementById("botonProcedimientoBusqueda").style.color="#0B3090 !important";
                    habilitarGeneral(comboProced);
                } else {
                    document.getElementById("botonProcedimientoBusqueda").style.color="#f6f6f6 !important";
                    deshabilitarGeneral(comboProced);
                    document.forms[0].codProcedimiento.value="";
                    document.forms[0].descProcedimiento.value="";
                }
            }
        }
        if (!encontrado) {
            document.forms[0].codAmbito.value="";
            document.forms[0].descAmbito.value="";
        }
    }

    function validarCampos(){
        var nombre = document.forms[0].nombre.value;
        var codAmbito = document.forms[0].codAmbito.value;
        //var proc = document.forms[0].codProcedimiento.value;
        //if ((nombre!="")&&(proc!=""))
        if ((nombre!="")&&(codAmbito!=""))
            return true;
        return false;
    }

    function pulsarAlta() {
        var nombre = Trim(document.forms[0].nombre.value);
        var yaExiste = 0;
        var i;
        for (i=0; i<listaInformes.length; i++){
            if (nombre == listaInformes[i]){
            	yaExiste = 1;
            	i = listaInformes.length;
            }
        }
        if (validarCampos()){
            if (!yaExiste){
            	document.forms[0].opcion.value = "altaInforme";
            	document.forms[0].action = DOMAIN_NAME+"<c:url value='/gestionInformes/FichaInforme.do'/>";
            	document.forms[0].submit();
            } else {
             	jsp_alerta('A','<%=descriptor.getDescripcion("msjNombExiste")%>');
             	document.forms[0].nombre.value = '';
            }
        } else  jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');            
    }

    function ventanaProcBusqueda() {
        if (!document.forms[0].codProcedimiento.disabled)
            dlg.show();
    }
</script>
    <html:form action="/gestionInformes/FichaInforme.do" target="_self">
        <html:hidden property="opcion" />
        <!-- TABLA DE AMBITOS -->
        <div class="dialogCombo" dojoType="dialog" id="DialogContent3" bgColor="white" bgOpacity="0.5" toggle="fade" toggleDuration="250">
            <table class="xTabla" dojoType="SortableTable" widgetId="parsedFromHtml3" headClass="fixedHeader" tbodyClass="scrollContent"
                   enableMultipleSelect="false" enableAlternateRows="true" rowAlternateClass="alternateRow" 
                   onSelect=actualizarAmbito();>
                 <thead>
                     <tr>
                        <th field="Id" dataType="String" width="15%"><%=descriptor.getDescripcion("gbInformeCodigo")%></th>
                        <th field="Name" dataType="String" width="85%"><%=descriptor.getDescripcion("gbInformeAmbito")%></th>
                     </tr>
                 </thead>
                 <logic:iterate id="elemento" name="GestionInformesForm" property="listaAmbitos">
                     <tr>
                        <td><bean:write name="elemento" property="codigo"/></td>
                        <td><bean:write name="elemento" property="descripcion"/></td>
                     </tr>
                </logic:iterate>
            </table>
            <div>
                <input type= "button" id="cancelarDialog3" class="botonAplicacion" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cancelarDialog3">
            </div>
        </div>
        <!-- TABLA DE AMBITOS -->
        <!-- TABLA DE PROCEDIMIENTOS -->
        <div class="dialogCombo" dojoType="dialog" id="DialogContent" bgColor="white" bgOpacity="0.5" toggle="fade" toggleDuration="250">
            <table class="xTabla" dojoType="SortableTable" widgetId="parsedFromHtml2" headClass="fixedHeader" tbodyClass="scrollContent"
                   enableMultipleSelect="false" enableAlternateRows="true" rowAlternateClass="alternateRow" onSelect=actualizarProcedimiento();>
                 <thead>
                     <tr>
                        <th field="Id" dataType="String"><%=descriptor.getDescripcion("gbInformeCodigo")%></th>
                        <th field="Name" dataType="String"><%=descriptor.getDescripcion("gbInformeProc")%></th>
                     </tr>
                 </thead>
                 <logic:iterate id="elemento" name="GestionInformesForm" property="listaProcedimientos">
                     <tr>
                        <td><bean:write name="elemento" property="codigo"/></td>
                        <td><bean:write name="elemento" property="descripcion"/></td>
                     </tr>
                </logic:iterate>
            </table>
            <div>
                <input type= "button" id="cancelarDialog" class="botonAplicacion" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cancelarDialog">
            </div>
        </div>
        <!-- TABLA DE PROCEDIMIENTOS -->

           <div class="nuevoInforme">
               <div>
                    <span class="etiq"><%=descriptor.getDescripcion("gbInformeNombre")%></span>
                    <html:text property="nombre" styleId="nombre" styleClass="inputTextoObligatorio" size="56" value="" 
                               onblur="return xAMayusculas(this);" />
               </div>
               <div>
                    <span class="etiq"><%=descriptor.getDescripcion("gbInformeAmbito")%></span>
                    <html:text styleId="codAmbito" property="codAmbito" styleClass="inputTextoObligatorio" size="6"
                           onkeypress="return xAMayusculas(this);" onchange="cambiarAmbito();" value="" />
                    <html:text styleId="descAmbito" property="descAmbito" styleClass="inputTextoObligatorio" size="45" readonly="true" value="" />
                    <a href="javascript:dlg3.show()">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAmbitoBusqueda"
                             name="botonAmbitoBusqueda" style="cursor:hand;"></span>
                    </a>
               </div>
               <div>
                    <span class="etiq"><%=descriptor.getDescripcion("gbInformeProc")%></span>
                    <html:text styleId="codProcedimiento" property="codProcedimiento" styleClass="inputTexto" size="6"
                           onkeypress="return xAMayusculas(this);" onchange="cambiarProcedimiento();" value="" />
                    <html:text styleId="descProcedimiento" property="descProcedimiento" styleClass="inputTexto" size="45" readonly="true" value="" />
                    <a href="javascript:ventanaProcBusqueda()">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonProcedimientoBusqueda"
                             name="botonProcedimientoBusqueda" style="cursor:hand;"></span>
                    </a>
               </div>
           </div>
    </html:form>
<script type="text/javascript">
    var comboProced = [document.forms[0].codProcedimiento,document.forms[0].descProcedimiento,
                    document.getElementById("botonProcedimientoBusqueda")];
    document.getElementById("botonProcedimientoBusqueda").style.color="#f6f6f6 !important";
    deshabilitarGeneral(comboProced);
    
    $(document).ready(function() {
        $(".xTabla").not(".dialogCombo .xTabla").DataTable( {
            "sort" : false,
            "paginate" : false,
            "autoWidth": false,
            "language": {
                "search": "<%=descriptor.getDescripcion("buscar")%>",
                "zeroRecords": "<%=descriptor.getDescripcion("msgNoResultBusq")%>",
                "info": "<%=descriptor.getDescripcion("mosPagDePags")%>",
                "infoEmpty": "<%=descriptor.getDescripcion("noRegDisp")%>",
                "infoFiltered": "<%=descriptor.getDescripcion("filtrDeTotal")%>",
            }
        } );
    });
</script>
