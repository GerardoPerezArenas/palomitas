<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.agora.business.gestionInformes.CriteriosValueObject" %>
<%@ page import="es.altia.agora.interfaces.user.web.gestionInformes.FichaInformeForm" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="java.util.Vector" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%--
  Created by IntelliJ IDEA.
  User: daniel.sambad
  Date: 07-feb-2007
  Time: 16:40:51
  To change this template use File | Settings | File Templates.
--%>
<%
    response.setHeader("Cache-control","no-cache");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader ("Expires", 0);

    Log m_Log = LogFactory.getLog(FichaInformeForm.class.getName());
    FichaInformeForm fichaInformeForm = (FichaInformeForm) session.getAttribute("FichaInformeForm");
    String codInforme = fichaInformeForm.getCodPlantilla();
    
    Vector listaCriterios = fichaInformeForm.getListaCriInf();
    JSONArray jsonArrayCod = new JSONArray();
    JSONArray jsonArrayCampo = new JSONArray();
    JSONArray jsonArrayCond = new JSONArray();
    JSONArray jsonArrayValor1 = new JSONArray();
    JSONArray jsonArrayValor2 = new JSONArray();
    JSONArray jsonArrayTitulo = new JSONArray();
    JSONArray jsonArrayOrigen = new JSONArray();
    JSONArray jsonArrayTabla = new JSONArray();
    JSONArray jsonArrayTipo = new JSONArray();
    
    boolean pintarCriterios = false;
    int j = 0;

    while (j < listaCriterios.size() && !pintarCriterios) {
        CriteriosValueObject criterio = (CriteriosValueObject) listaCriterios.get(j);
        pintarCriterios = criterio.getCondicion()==null || criterio.getCondicion().equals("");
        j++;
    }

    for (int i = 0; i < listaCriterios.size(); i++) {
        CriteriosValueObject criterio = (CriteriosValueObject) listaCriterios.get(i);
        jsonArrayCod.put(criterio.getId());
        jsonArrayCampo.put(criterio.getCampo());
        jsonArrayCond.put(criterio.getCondicion());
        jsonArrayValor1.put(criterio.getValor1());
        jsonArrayValor2.put(criterio.getValor2());
        jsonArrayTitulo.put(criterio.getTitulo());
        jsonArrayOrigen.put(criterio.getOrigen());
        jsonArrayTabla.put(criterio.getTabla());
        jsonArrayTipo.put(criterio.getTipo());
    }
    fichaInformeForm.setListaCriteriosCod(jsonArrayCod);
    fichaInformeForm.setListaCriteriosCampo(jsonArrayCampo);
    fichaInformeForm.setListaCriteriosCond(jsonArrayCond);
    fichaInformeForm.setListaCriteriosValor1(jsonArrayValor1);
    fichaInformeForm.setListaCriteriosValor2(jsonArrayValor2);
    fichaInformeForm.setListaCriteriosTitulo(jsonArrayTitulo);
    fichaInformeForm.setListaCriteriosOrigen(jsonArrayOrigen);
    fichaInformeForm.setListaCriteriosTabla(jsonArrayTabla);
    fichaInformeForm.setListaCriteriosTipo(jsonArrayTipo);
    m_Log.debug("LISTA CRITERIOS COD *******: " + fichaInformeForm.getListaCriteriosCod());
    m_Log.debug("LISTA CRITERIOS CAMPO *******: " + fichaInformeForm.getListaCriteriosCampo());
    m_Log.debug("LISTA CRITERIOS COND *******: " + fichaInformeForm.getListaCriteriosCond());
    m_Log.debug("LISTA CRITERIOS VALOR1 *******: " + fichaInformeForm.getListaCriteriosValor1());
    m_Log.debug("LISTA CRITERIOS VALOR2 *******: " + fichaInformeForm.getListaCriteriosValor2());
    m_Log.debug("LISTA CRITERIOS TITULO *******: " + fichaInformeForm.getListaCriteriosTitulo());
    m_Log.debug("LISTA CRITERIOS ORIGEN *******: " + fichaInformeForm.getListaCriteriosOrigen());
    m_Log.debug("LISTA CRITERIOS TABLA *******: " + fichaInformeForm.getListaCriteriosTabla());
    m_Log.debug("LISTA CRITERIOS TIPO *******: " + fichaInformeForm.getListaCriteriosTipo());
%>
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
<jsp:setProperty name="descriptor" property="apl_cod" value="<%= apl %>"/>
<%
    String etiquetaEsNulo   = descriptor.getDescripcion("etiqEsNulo");
    String etiquetaEsNoNulo = descriptor.getDescripcion("etiqEsNoNulo");
%>


<style type="text/css">

    body.nuevo div.tableContainerComboCriterios {	/* IE only hack */
    border:1px solid #888;
    height: 475px;
    overflow-x:hidden;
    overflow-y: auto;
    margin-bottom: 1px;
    margin-top: 1px;
    padding:10px;
    text-align:left;
    height: 680px;
}

    html>body body.nuevo div.tableContainerComboCriterios div.tableContainerBloqueCriterios {
        border:1px solid #888;
        margin-bottom: 2px;
        margin-top: 2px;
        padding:10px;
    }


    html>body body.nuevo div.tableContainerComboCriterios div.tableContainerComboCriteriosGeneracionDefinicion {	/* IE only hack */
    border:0px solid #ccc;
    height: 40px;
    margin-bottom: 2px;
    padding:0px;
}
html>body body.nuevo div.tableContainerComboCampos div.tableContainerComboCamposParametro {	/* IE only hack */
    border:0px solid #ccc;
    margin-top:10px;
    margin-bottom: 0px;
    padding-left: 40px;
}



  .dojoDialog.dialogCriterios {
    background : #ddd;
    border : 1px solid #888;
    padding:2px;
    height:660px;
    width:700px;
    text-align:right;
    top:0px;
    left:0px;
}


</style>
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/dojo/dojo.js"></script>
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript">
    dojo.require("dojo.widget.*");
    dojo.require("dojo.event.*");

    var listaCriteriosCod = new Array();
    var listaCriteriosCampo = new Array();
    var listaCriteriosCond = new Array();
    var listaCriteriosValor1 = new Array();
    var listaCriteriosValor2 = new Array();
    var listaCriteriosTitulo = new Array();
    var listaCriteriosOrigen = new Array();
    var listaCriteriosTabla = new Array();
    var listaCriteriosTipo = new Array();

    var criteriosCod = new Array();
    var criteriosCampo = new Array();
    var criteriosCond = new Array();
    var criteriosValor1 = new Array();
    var criteriosValor2 = new Array();
    var criteriosTitulo = new Array();
    var criteriosOrigen = new Array();
    var criteriosTabla = new Array();
    var criteriosTipo = new Array();
    
    <% for (int i=0;i<jsonArrayCampo.length();i++) { %>
        listaCriteriosCod[<%=i%>] = '<%=jsonArrayCod.get(i)%>';
        listaCriteriosCampo[<%=i%>] = '<%=jsonArrayCampo.get(i)%>';
        listaCriteriosCond[<%=i%>] = '<%=jsonArrayCond.get(i)%>';
        listaCriteriosValor1[<%=i%>] = '<%=jsonArrayValor1.get(i)%>';
        listaCriteriosValor2[<%=i%>] = '<%=jsonArrayValor2.get(i)%>';
        listaCriteriosTitulo[<%=i%>] = '<%=jsonArrayTitulo.get(i)%>';
        listaCriteriosOrigen[<%=i%>] = '<%=jsonArrayOrigen.get(i)%>';
        listaCriteriosTabla[<%=i%>] = '<%=jsonArrayTabla.get(i)%>';
        listaCriteriosTipo[<%=i%>] = '<%=jsonArrayTipo.get(i)%>';
    <% } %>

    var dlgCriterios;

    function init(e) {
        document.forms[0].codPlantilla.value = '<%=codInforme%>';
        dlgCriterios = dojo.widget.byId("DialogCriterios");
        var btnCriterios = document.getElementById("cancelarDialogCriterios");
        dlgCriterios.setCloseControl(btnCriterios);

        for (var k=0; k<listaCriteriosCampo.length; k++) {
            if (listaCriteriosCond[k]=='') {
                eval("document.getElementById('criterio"+listaCriteriosCampo[k]+"').style.visibility='hidden';");
                if (listaCriteriosTipo[k]=='F') {
                            eval("dojo.widget.byId('valor2Criterio"+listaCriteriosCampo[k]+"').hide();");
                            eval("dojo.widget.byId('valor2Criterio"+listaCriteriosCampo[k]+"').inputNode.value = '';");
                } else if (listaCriteriosTipo[k]=='N') {
                    eval("document.getElementById('valor2Criterio"+listaCriteriosCampo[k]+"').style.visibility='hidden';");
                }
            }
        }

        for (var k=0; k<listaCriteriosCampo.length; k++) {
            criteriosCampo[k] = listaCriteriosCampo[k];
            criteriosTitulo[k] = listaCriteriosTitulo[k];
            criteriosOrigen[k] = listaCriteriosOrigen[k];
            criteriosTabla[k] = listaCriteriosTabla[k];
            criteriosTipo[k] = listaCriteriosTipo[k];
            if (listaCriteriosCond[k]!='' || listaCriteriosTipo[k]=='D' || listaCriteriosTipo[k]=='N') {
                criteriosValor2[k] = listaCriteriosValor2[k];
            }
            if (listaCriteriosCond[k]!='') {
                criteriosCond[k] = listaCriteriosCond[k];
                criteriosValor1[k] = listaCriteriosValor1[k];
            } else {
                eval("document.getElementById('criterio"+listaCriteriosCampo[k]+"').style.visibility='visible';");
            }
        }

        dlgCriterios.show();
    }

    dojo.addOnLoad(init);

    function cerrar() {
        self.parent.opener.retornoXanelaAuxiliar();
    }

    function generar() {  
        var retorno = new Array();
        var listaCamposCriterio = '';
        var listaValorOperCriterio = '';
        var listaValor1Criterio = '';
        var listaValor2Criterio = '';
        var listaTiposCriterio = '';
	
        if (validar()) { 
            if (validarFechas() && validaNumericoAnterior()) { 
                for (k=0;k<listaCriteriosCampo.length;k++) {
                    if (listaCriteriosCond[k]=='') {
                        //if (listaCriteriosTipo[k]=='D' || listaCriteriosTipo[k]=='N') { 
                        // NOTA : ya se ponen los valores correctos para las operaciones en criterioTexto.jsp y criterioDesplegable.jsp
                        eval("criteriosCond[k] = document.getElementById('valorOperCriterio"+listaCriteriosCampo[k]+"').value;");
                        //} else {
                        //    eval("criteriosCond[k] = (document.forms[0].valorOperCriterio"+listaCriteriosCampo[k]+".value!=0) ? 6 : 5;");
                        //}
                        if (listaCriteriosTipo[k]=='F') {
                            eval("criteriosValor1[k] = dojo.widget.byId('valor1Criterio"+listaCriteriosCampo[k]+"').getValue();");
                            eval("criteriosValor2[k] = dojo.widget.byId('valor2Criterio"+listaCriteriosCampo[k]+"').getValue();");
                        } else {
                            eval("criteriosValor1[k] = document.getElementById('valor1Criterio"+listaCriteriosCampo[k]+"').value;");
                            if (listaCriteriosTipo[k]!='D') {
                                eval("criteriosValor2[k] = document.getElementById('valor2Criterio"+listaCriteriosCampo[k]+"').value;");
                            }
                        }
                    }
                    listaCamposCriterio += (criteriosCampo[k]!='') ? criteriosCampo[k] + '§¥' : 'null' + '§¥';
                    listaValorOperCriterio += (criteriosCond[k]!='') ? criteriosCond[k] + '§¥' : 'null' + '§¥';
                    listaValor1Criterio += (criteriosValor1[k]!='') ? criteriosValor1[k] + '§¥' : 'null' + '§¥';
                    listaValor2Criterio += (criteriosValor2[k]!='') ? criteriosValor2[k] + '§¥' : 'null' + '§¥';
                    listaTiposCriterio += (criteriosValor2[k]!='') ? criteriosValor2[k] + '§¥' : 'null' + '§¥';
                }
                retorno[0] = listaCamposCriterio;
                retorno[1] = listaValorOperCriterio;
                retorno[2] = listaValor1Criterio;
                retorno[3] = listaValor2Criterio;
                retorno[4] = document.getElementById('tipoFichero').value;
                retorno[5] = document.getElementById('descripcion').value;
                retorno[6] = document.forms[0].dir.value;
                var radioBoton1 = document.getElementsByName('boton1');
                if (radioBoton1[0].checked == true)
   		 { boton1=1;  }else{boton1=2;}

                retorno[7] =boton1;
       
                self.parent.opener.retornoXanelaAuxiliar(retorno);
            }
        } else {
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
        }
    }

    function validarCriterios() { 
        var condicion = false;
        var valida = true;
        var k = 0;
        while (k<listaCriteriosCampo.length && valida) {
            if (listaCriteriosTipo[k]=='F') {
                if (listaCriteriosCond[k]=='') {         
                    eval("condicion = dojo.widget.byId('valor1Criterio"+listaCriteriosCampo[k]+"').getValue()!='';");
                    eval ("var nulos = document.getElementById('valorOperCriterio"+listaCriteriosCampo[k]+"').value=='8';");
                    eval ("nulos = nulos || document.getElementById('valorOperCriterio"+listaCriteriosCampo[k]+"').value=='9';");
                   if (nulos) valida=true;
                    if (condicion) {                    
                        eval("condicion = document.getElementById('valorOperCriterio"+listaCriteriosCampo[k]+"').value=='4';");                        
                        if (condicion) {                            
                            eval("valida = dojo.widget.byId('valor2Criterio"+listaCriteriosCampo[k]+"').getValue()!='';");
                        } else {                            
                            valida = true;
                        }
                    } else {                        
                        valida=nulos;
                    }
                } else {                    
                    valida = true;
                }
            } else {
                if (listaCriteriosCond[k]=='') {
                    eval("valida = document.getElementById('valor1Criterio"+listaCriteriosCampo[k]+"').value != '';");
                    eval("var nulos= document.getElementById('valorOperCriterio"+listaCriteriosCampo[k]+"').value=='8';");
                    eval("nulos = nulos ||  document.getElementById('valorOperCriterio"+listaCriteriosCampo[k]+"').value=='9';");
                        
                        if (valida) {
                            eval("condicion = document.getElementById('valorOperCriterio"+listaCriteriosCampo[k]+"').value=='4';");                            
                            if (condicion) {                                
                            eval("valida = document.getElementById('valor2Criterio"+listaCriteriosCampo[k]+"').value != '';");
                            }
                        }
                    else{
                        if (nulos) valida=true;
                    }
                }
            }
            k++;
        }
        return valida;
    }

    function validarDescripcion() {
        return (document.getElementById('descripcion').value!="");
    }

    function validar() { 
        var validaDescripcion = validarDescripcion();
        var validaCriterios = validarCriterios();        
        return (validaDescripcion && validaCriterios);
    }

    function validarFechas() { 
        var valida = true;
        i = 0;
        while (i<listaCriteriosCampo.length && valida) {
            if (listaCriteriosCond[i]=='' &&
                (listaCriteriosTipo[i]=='F')) {                
                eval("valida = ((document.getElementById('valorOperCriterio"+listaCriteriosCampo[i]+"').value!=4) || " +
                     "(document.getElementById('valorOperCriterio"+listaCriteriosCampo[i]+"').value==4 && " +
                     "validarFechaAnterior(dojo.widget.byId('valor1Criterio"+listaCriteriosCampo[i]+"').getValue()," +
                     "dojo.widget.byId('valor2Criterio"+listaCriteriosCampo[i]+"').getValue())))");

            }
            i++;
        }        
        return valida;
    }

    function validaNumericoAnterior() {
        var valida = true;
        i = 0;
        while (i<listaCriteriosCampo.length && valida) {
            if (listaCriteriosCond[i]=='' && (listaCriteriosTipo[i]=='N')) {
                eval("var opcionEntre = document.getElementById('valorOperCriterio"+listaCriteriosCampo[i]+"').value==4;");
                if (opcionEntre) {
                    eval("var valor1Criterio = new Number(document.getElementById('valor1Criterio"+listaCriteriosCampo[i]+"').value);");
                    eval("var valor2Criterio = new Number(document.getElementById('valor2Criterio"+listaCriteriosCampo[i]+"').value);");
                    if (valor1Criterio>valor2Criterio) {
                        jsp_alerta("A",'<%=descriptor.getDescripcion("msjNumAnterior")%>');
                        valida = false;
                    }
                }
            }
            i++;
        }
        return valida;
    }


    function comprobarOperFecha(campo) {
    comprobarVisibilidadNulos(campo);
        eval("var opcionEntre = document.getElementById('valorOperCriterio"+campo+"').value==4;");
        if (opcionEntre) {
            eval("dojo.widget.byId('valor2Criterio"+campo+"').show();");
        } else {
            eval("dojo.widget.byId('valor2Criterio"+campo+"').hide();");
            eval("dojo.widget.byId('valor2Criterio"+campo+"').inputNode.value = '';");
        }
    }

    function comprobarOperNumerico(campo) {
        comprobarVisibilidadNulos(campo);
        eval("var opcionEntre = document.getElementById('valorOperCriterio"+campo+"').value==4;");
        if (opcionEntre) {
            eval("document.getElementById('valor2Criterio"+campo+"').style.visibility='visible';");
        } else {
            eval("document.getElementById('valor2Criterio"+campo+"').style.visibility='hidden';");
            eval("document.getElementById('valor2Criterio"+campo+"').value = '';");
        }
    }

    function comprobarOperTexto(campo) {
        comprobarVisibilidadNulos(campo);
    }
    
    function comprobarVisibilidadNulos(campo) {
        eval("var nulos= document.getElementById('valorOperCriterio"+campo+"').value=='8';");
        eval("nulos = nulos ||  document.getElementById('valorOperCriterio"+campo+"').value=='9';");
        if (nulos)
            eval("document.getElementById('valor1Criterio"+campo+"').style.visibility='hidden';");
        else
            eval("document.getElementById('valor1Criterio"+campo+"').style.visibility='visible';");
    }

    function comprobarOperInteresado(campo) {
        comprobarVisibilidadNulosInteresado(campo);
    }
    function comprobarVisibilidadNulosInteresado(campo) {
        eval("var nulos= document.getElementById('valorOperCriterio"+campo+"').value=='8';");
        eval("nulos = nulos ||  document.getElementById('valorOperCriterio"+campo+"').value=='9';");
        if (nulos) {
            eval("document.getElementById('valor1Criterio"+campo+"').style.visibility='hidden';");
            eval("document.getElementById('botonBuscar').style.visibility='hidden';");
        }
        else {
            eval("document.getElementById('valor1Criterio"+campo+"').style.visibility='visible';");
            eval("document.getElementById('botonBuscar').style.visibility='visible';");
        }
    }
    function pulsarBuscarInteresado(campo) {
        var source = DOMAIN_NAME + "<c:url value='/BusquedaTerceros.do?opcion=inicializar&ventana=true&destino=seleccionDesdeInformes'/>";
        var argumentos = new Array();
        argumentos['modo'] = 'seleccion';
        var terceros = new Array();
        argumentos['terceros'] = terceros;
        abrirXanelaAuxiliar(DOMAIN_NAME + "<c:url value='/jsp/terceros/mainVentana.jsp?source='/>" + source,argumentos,
	'width=990,height=650',function(datos){
                        if (datos != null) {
                            //aqui va el nombre
                            eval ("document.getElementById('valor1Criterio"+campo+"').value = '" + datos[6] +"';");
                            //aqui va el codigo
                            eval ("document.getElementById('valor2Criterio"+campo+"').value = '" + datos[0] +"';");
                        }
                   });
    }


</script>

<form name="form" id="form" action="<%=request.getContextPath()%>/gestionInformes/SolicitudesInformes.do" target="_self">
    <input type="hidden" name="dir" value="">
    <input type="hidden" name="opcion" value="">
    <input type="hidden" name="codPlantilla" value="">

</form>

<!-- TABLA DE CRITERIOS -->
<div class="dialogCriterios" dojoType="dialog" id="DialogCriterios" bgColor="white" bgOpacity="0.5" toggle="fade" toggleDuration="250"  >
    <form name="criterios" action="">
        <div class="tableContainerComboCriterios" >
        <div class="tableContainerBloqueCriterios" style="height:40px;" >
            <div class="tableContainerComboCriteriosGeneracionDefinicion">
                <span class="etiqBoldMaxi"><%=descriptor.getDescripcion("titTipoFichero")%></span>
            </div>
           <div class="tableContainerComboCriteriosParametro">
            <span class="etiq" style="width:80px"><%=descriptor.getDescripcion("etiqTipo")%></span>
            <select name="tipoFichero" id="tipoFichero" class="inputTexto">
                <option value="0"> PDF  </option>
                <option value="1"> HTML </option>
                <option value="2"> EXCEL</option>
                <option value="3"> CSV  </option>
            </select>
        </div>
        </div>
        
        <div class="tableContainerBloqueCriterios" style="height:40px;">
            <div class="tableContainerComboCriteriosGeneracionDefinicion">
                <span class="etiqBoldMaxi">MODO VISUALIZACIÓN</span>
            </div>
           <div class="tableContainerComboCriteriosParametro">
            <span class="etiq" style="width:35px">Buzón</span> 
            <input type="radio" name="boton1" value="1" checked >  
	    <span class="etiq" style="width:40px;margin-left:40px;">Directo</span>
	    <input type="radio" name="boton1" value="2"> 

        </div>
        </div>
        <div class="tableContainerBloqueCriterios" style="height:40px;">
            <div class="tableContainerComboCriteriosGeneracionDefinicion">
                <span class="etiqBoldMaxi"><%=descriptor.getDescripcion("titDescripcion")%></span>
            </div>
        <div class="tableContainerComboCriteriosParametro">
            <span class="etiq" style="width:80px"><%=descriptor.getDescripcion("etiqDescripcion")%></span>
            <input type="text" name="descripcion" id="descripcion" class="inputTextoObligatorio" style="width:250px" maxlength="50" value="" onblur="return xAMayusculas(this); enter(event);" >
        </div>
        </div>
        <% if (pintarCriterios) { %>
        <div id="bloqueCriterios" class="tableContainerBloqueCriterios" style="height: 400px">
                <div  id="etiqCriterios" class="tableContainerComboCriteriosGeneracionDefinicion">
                    <span class="etiqBold"><%=descriptor.getDescripcion("titCriterios")%></span>
                </div>
            <%  for (int i=0;i<jsonArrayCampo.length();i++) {
                    if (jsonArrayCond.get(i)==null || jsonArrayCond.get(i).equals("")) { %>
                        <jsp:include page="criterioGeneral.jsp" flush="true">
                            <jsp:param name="campo" value="<%=jsonArrayCampo.get(i)%>"/>
                            <jsp:param name="titulo" value="<%=jsonArrayTitulo.get(i)%>"/>
                            <jsp:param name="tabla" value="<%=jsonArrayTabla.get(i)%>"/>
                            <jsp:param name="codigoCampoDesplegable" value="<%=jsonArrayValor2.get(i)%>"/>
                            <jsp:param name="formulario" value='<%="SolicitudesInformesForm"%>'/>
                            <jsp:param name="tipo" value="<%=jsonArrayTipo.get(i)%>"/>
                        </jsp:include>
                    <%}
                }  %>
            </div>
        <% } %>
        <div style="text-align:right">
            <input type= "button" id="aceptarDialogCriterios" class="botonGeneral" value="<%=descriptor.getDescripcion("botonGenerar")%>" name="aceptarDialogCriterios" onclick="generar();">
            <input type= "button" id="cancelarDialogCriterios" class="botonGeneral" value="<%=descriptor.getDescripcion("botonCancelar")%>" name="cancelarDialogCriterios" onclick="cerrar();">
        </div>
    </div>
    </form>
</div>

<!-- TABLA DE CRITERIOS -->

document.getElementById("bloqueCriterios").style.height = "50px";

<script type="text/javascript">
        function enter(evento) {
            alert(evento);
        

        var aux=null;
        if(window.event)
            aux = window.event;
        else
            aux = evento;

        var tecla = 0;
        if(aux.keyCode)
            tecla = aux.keyCode;
        else
            tecla = aux.which;
    keyDel(aux);
    if (tecla == 13)
       //LA FUNCION DE GENERAR
           generar();
    }
    
</script>