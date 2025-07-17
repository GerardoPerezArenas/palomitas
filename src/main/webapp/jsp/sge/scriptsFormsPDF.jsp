<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<%
    int idioma = 1;
    int apl = 4;
   
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

<style type="text/css">
   TR.rojo TD {background-color:red;color:white;}
   TR.gris TD {background-color:white;color:#a5a5a5;}
</style>

<SCRIPT type="text/javascript">
var listaFormsPDF = new Array();
var listaFormsPDFOriginal = new Array();
var puedeAltaAdjunto;

var tabFormsPDF = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>',
        '<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>',
        '<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', 
        '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>',
        '<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',
        document.getElementById('tablaFormsPDF'));
tabFormsPDF.addColumna('75','left',"<%= descriptor.getDescripcion("etCodForm")%>");
tabFormsPDF.addColumna('550','left',"<%= descriptor.getDescripcion("etDesForm")%>");
tabFormsPDF.addColumna('80','left',"<%= descriptor.getDescripcion("etFechaForm")%>");
tabFormsPDF.addColumna('90','left',"<%= descriptor.getDescripcion("etUsuarioForm")%>");
tabFormsPDF.addColumna('60','left',"<%= descriptor.getDescripcion("etEstadoForm")%>");
tabFormsPDF.displayCabecera=true;
tabFormsPDF.colorLinea=function(rowID) {
    if(listaFormsPDFOriginal[rowID][0]!="0")
        return 'gris';
}

function calcularPuedeAltaAdjunto(){
    puedeAltaAdjunto = true;
    for (var i=0; i<listaFormsPDFOriginal.length; i++){
        puedeAltaAdjunto = puedeAltaAdjunto && (listaFormsPDFOriginal[i][5]>0);
    }
}

//Inicializar la lista de Formularios PDF
function inicializarFormsPDF(){
    var cont = 0;
    var estados = ['<%= descriptor.getDescripcion("etEstado0Form")%>','<%= descriptor.getDescripcion("etEstado1Form")%>','<%= descriptor.getDescripcion("etEstado2Form")%>','<%= descriptor.getDescripcion("etEstado3Form")%>'];
    <logic:iterate id="elem" name="TramitacionExpedientesForm" property="listaFormsPDF">
    listaFormsPDF[cont] = ['<bean:write name="elem" property="codigo" />',
        '<bean:write name="elem" property="descripcion" />',
        '<fmt:formatDate value="${elem.fecMod.time}" type="both" pattern="dd/MM/yyyy HH:mm"/>',
        '<bean:write name="elem" property="usuario"/>',
        estados[<bean:write name="elem" property="estado"/>]
        ];
    listaFormsPDFOriginal[cont] = ['<bean:write name="elem" property="tipo" />',
        '<bean:write name="elem" property="codigo" />',
        '<bean:write name="elem" property="descripcion" />',
        '<fmt:formatDate value="${elem.fecMod.time}" type="both" pattern="dd/MM/yyyy HH:mm"/>',
        '<bean:write name="elem" property="usuario"/>',
        '<bean:write name="elem" property="estado"/>'
        ];
    cont++;
    </logic:iterate>

    tabFormsPDF.lineas=listaFormsPDF;
    calcularPuedeAltaAdjunto();
    tabFormsPDF.displayTabla();
}

function pulsarAltaFormPDF(){
    if (!puedeAltaAdjunto){
        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoAltaF")%>");
    }else{
        var i = tabFormsPDF.selectedIndex;
        var tipo = "";
        var padre = "";
        if (i>=0){
            tipo = listaFormsPDFOriginal[i][0];
            if (tipo==0)
                padre = listaFormsPDFOriginal[i][1];
        }
        var source = "<c:url value='/sge/TramitacionExpedientes.do?opcion=listaAjuntosPDFTramite'/>";
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana',
    'width=500,height=500',function(datosConsulta){
                    if(datosConsulta!=undefined){
                        window.open("<html:rewrite page='/AbrirPDFFormulario'/>?codigo=" + datosConsulta +
                                "&opcion=nuevoadj&tfCod="+padre+"&ocu="+document.forms[0].ocurrenciaTramite.value+
                                "&proc="+document.forms[0].codProcedimiento.value+
                                "&mun="+document.forms[0].codMunicipio.value+
                                "&ejer="+document.forms[0].ejercicio.value+
                                "&tram="+document.forms[0].codTramite.value+
                                "&num="+document.forms[0].numero.value,
                                "ventanaForm");
                    }
            });
    }
}

function pulsarVerFormPDF(){
    var i = tabFormsPDF.selectedIndex;
    if(i == -1) {
        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
    }else{ 
        var estado = listaFormsPDFOriginal[i][5];
        if(estado == 0){
            jsp_alerta("A","No puede ver un formulario pendiente"); 
        }else{
            window.open("<html:rewrite page='/AbrirPDFFormulario'/>?codigo=" + listaFormsPDFOriginal[i][1] + "&opcion=imprimir","ventanaForm");
        }
    }
}

function pulsarModificarFormPDF(){
    var i = tabFormsPDF.selectedIndex;
    if(i == -1) {
        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
    }else{
        var estado = listaFormsPDFOriginal[i][5];
        if ((estado != 0) && (estado != 3)){ //formulario ni pendiente ni erroneo
            jsp_alerta("A","<%=descriptor.getDescripcion("msjNoModFEnviado")%>");
        }else{
            window.open("<html:rewrite page='/AbrirPDFFormulario'/>?codigo=" + listaFormsPDFOriginal[i][1] + "&opcion=editaradj","ventanaForm");
        }            
    }
}

function pulsarEliminarFormPDF(){
    var i = tabFormsPDF.selectedIndex;
    if(i == -1) {
        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
    }else{
        var estado = listaFormsPDFOriginal[i][5];
        if(estado != 0){
            jsp_alerta("A","<%=descriptor.getDescripcion("msjNoBorrarFEnviado")%>");
        }else{
            if(jsp_alerta("C","<%=descriptor.getDescripcion("msjBorrarForm")%>")) {
              document.forms[0].codFormPDF.value = listaFormsPDFOriginal[i][1];
              document.forms[0].opcion.value="eliminarFormPDF";
              document.forms[0].target="oculto";
              document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
              document.forms[0].submit();
            }
        }
    }
}

function actualizaTablaFormsPDF(listaFormularios, listaFormulariosOriginal){
    listaFormsPDF = new Array();
    listaFormsPDFOriginal = new Array();

    for (var i=0; i<listaFormularios.length; i++){
      listaFormsPDF[i] = [listaFormularios[i][0], 
                            listaFormularios[i][1],
                            listaFormularios[i][2],
                            listaFormularios[i][3],
                            listaFormularios[i][4]];
    }

    for (var j=0; j<listaFormulariosOriginal.length; j++){
      listaFormsPDFOriginal[j] = [listaFormulariosOriginal[j][0], 
                            listaFormulariosOriginal[j][1],
                            listaFormulariosOriginal[j][2],
                            listaFormulariosOriginal[j][3],
                            listaFormulariosOriginal[j][4],
                            listaFormulariosOriginal[j][5]];
    }

    tabFormsPDF.lineas=listaFormsPDF;
    tabFormsPDF.displayTabla();
}

function tratarBotonesFormsPDF(){
    //siempre se puede ver el formulario pdf
    habilitarGeneral([document.forms[0].cmdVerForm]);
}

function actualizaCampoSup(nombre, valor, tipo, nombreCombo){
    var elemento = document.getElementsByName(nombre)[0];
    elemento.value=valor;

    if (tipo=="6")
        eval("combo"+nombreCombo+".buscaLinea('" + valor + "')");

    //eval("comboT_171_1_ACEPTA.buscaLinea('S')");
}

function pulsarVerAnexoPDF(){
  var i = tabFormsPDF.selectedIndex;
  if(i == -1) {
    jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
  }else{
     var source = "<%=request.getContextPath()%>/sge/ListaAnexos.do?formPDF=" + listaFormsPDFOriginal[i][1] + 
                  "&estado=" + listaFormsPDFOriginal[i][5];
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana',
    'width=700,height=500',function(mensaje){
                    if (mensaje!=null){
                        jsp_alerta("A",mensaje);
                    }
                });
  }
}
</script>