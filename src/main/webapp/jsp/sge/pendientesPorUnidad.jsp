<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<%
  int idioma=1;
  int apl=1;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
        }
  }
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<html:html>
 <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <TITLE>::: EXPEDIENTES  Expedientes pendientes por unidad :::</TITLE>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

        
    
    
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
    <script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>

    <SCRIPT language="JavaScript">
    var cod_unidad = new Array();
    var desc_unidad = new Array();

    function inicializar() {
      activarFormulario();
      <logic:iterate id="elemento" name="PendientesPorUnidadForm" property="listaUnidades">
        cod_unidad['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
        desc_unidad['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
      </logic:iterate>
    }

    function pulsarSalir() {
       window.location = "<c:url value='/jsp/sge/presentacionExped.jsp'/>";
    }

    function desactivarFormulario() {
      deshabilitarDatos(document.forms[0]);
      var vectorBoton = new Array(document.forms[0].botonUnidad);
      deshabilitarImagenBotonGeneral(vectorBoton, true);
      var vectorAnchor = new Array(document.getElementsByClassName("fa-chevron-circle-down"));
      deshabilitarImagen(vectorAnchor,true);    
    }

    function activarFormulario() {
      habilitarDatos(document.forms[0]);
      var vectorBoton = new Array(document.forms[0].botonUnidad);
      habilitarImagenBotonGeneral(vectorBoton, true);
      var vectorAnchor = new Array(document.getElementsByClassName("fa-chevron-circle-down"));
      deshabilitarImagen(vectorAnchor,false);    
    }

    function actualizarDescripcion(campoCodigo, campoDescripcion, listaCodigos, listaDescripciones, bConsulta) {
      cargarDesc(campoCodigo,campoDescripcion, listaCodigos, listaDescripciones);
    }
    </SCRIPT>
 </head>

 <BODY class="bandaBody" onload="javascript:{ pleaseWait('off'); 
        inicializar()}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

    <html:form action="/sge/PendientesPorUnidad.do" target="_self">

    <CENTER>

    <TABLE id ="tabla1" class="tablaP" width="730px" cellspacing="0" cellpadding="0">
    <TR>
      <TD>&nbsp;</TD>
    </TR>
    <tr>
      <TD class="titulo" colspan="4"><%=descriptor.getDescripcion("tit_pendExpUnid")%></TD>
    </tr>

    <TR>
      <TD>

    <TABLE width="100%" cellspacing="7px" cellpadding="1px" style="border-top: #7B9EC0 1px solid; border-bottom: #7B9EC0 1px solid;border-left: #7B9EC0 1px solid;border-right: #7B9EC0 1px solid;">
      <tr>
        <TD width="20%" class="etiqueta"><%=descriptor.getDescripcion("res_etiqUnid")%>:</TD>
        <TD width="80%" valign="top" class="columnP">
          <html:text styleId="obligatorio" styleClass="inputTextoObligatorio"  property="codUnidad" size="5"
                onkeypress="javascript:PasaAMayusculas(event);"
                onfocus="this.select();"
                onchange="actualizarDescripcion('codUnidad','descUnidad',cod_unidad,desc_unidad);"/>
          <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="descUnidad" style="width:255;height:17" readonly="true"
                onclick="javascript:{divSegundoPlano=false;inicializarValores('codUnidad','descUnidad',cod_unidad,desc_unidad);}"
                onfocus="javascript:{divSegundoPlano=true;inicializarValores('codUnidad','descUnidad',cod_unidad,desc_unidad);}"/>
            <A href="javascript:{divSegundoPlano=false;inicializarValores('codUnidad','descUnidad',cod_unidad,desc_unidad);}" name ="anchorD" style="text-decoration:none;" id="anclaDD" >
                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonUnidad" style="cursor:hand;"></span>
            </A>
        </TD>
      </tr>
      <tr>
        <TD width="20%" class="etiqueta"><%=descriptor.getDescripcion("res_etiqReg")%>:</TD>
        <TD width="80%" class="columnP">
          <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="txtRegistros" size="5" maxlength="5"
            onkeypress="javascript:PasaAMayusculas(event);"/>
        </TD>
      </tr>
      <tr>
        <TD width="50%" height="5px" colspan="2"></TD>
      </tr>
      <TR>
        <TD class="etiqueta" colspan="4" style="height:6px;background-color:#F7DAB0;" align="center"> <%=descriptor.getDescripcion("res_etiqListUnid")%> </TD>
      </TR>
      <tr>
        <TD width="100%" class="columnP" colspan="2">
          <TABLE width="100%" cellspacing="7px" cellpadding="1px">
            <TR>
              <TD>
                <table width="100%" rules="cols" bordercolor="#7B9EC0" border="1" cellspacing="0" cellpadding="0" class="fondoCab">
                  <tr height="15">
                    <td width="20%" bgcolor="#7B9EC0" class="txtverdebold" align="center"><%= descriptor.getDescripcion("etiq_numRex")%></td>
                    <td width="20%" bgcolor="#7B9EC0" class="txtverdebold" align="center"><%= descriptor.getDescripcion("etiq_numExp")%></td>
                    <td width="20%" bgcolor="#7B9EC0" class="txtverdebold" align="center"><%= descriptor.getDescripcion("gEtiqProc")%></td>
                    <td width="15%" bgcolor="#7B9EC0" class="txtverdebold" align="center"><%= descriptor.getDescripcion("gEtiq_fecIni")%></td>
                    <td width="25%" bgcolor="#7B9EC0" class="txtverdebold" align="center"><%= descriptor.getDescripcion("etiq_tramEnc")%></td>
                  </tr>
                  <tr>
                    <td colspan="5">
                      <div id="tabla" class="text" style="HEIGHT:200px; WIDTH: 700px; overflow-y: auto; overflow-x: no; visibility: visible; BORDER: 0px">
                      </div>
                    </td>
                  </tr>
                </table>
              </TD>
            </TR>
          </TABLE>
        </TD>
      </tr>

    </table>

      </TD>
    </TR>
    <TR>
      <TD>

    &nbsp;
    &nbsp;

    <TABLE class="tablaBotones" width="100%">
      <TR>
        <TD  width="50%" align="center">
          <input type= "button" class="boton" value=<%=descriptor.getDescripcion("gbConsultar")%> name="cmdConsultar" onclick="pulsarConsultar();" accesskey="C">
        </TD>
        <TD width="50%" align="center">
          <input type= "button" class="boton" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir"  onclick="pulsarSalir();" accesskey="S">
        </TD>
      </TR>
    </TABLE>

      </TD>
    </TR>
    </TABLE>

    </center>
    </html:form>

    <script language="JavaScript1.2">
    tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'),150);

    tab.addColumna('140','center');
    tab.addColumna('140','center');
    tab.addColumna('140','center');
    tab.addColumna('100','center');
    tab.addColumna('175','center');

    function refresca() {
      tab.displayTabla();
    }

    tab.displayDatos = pintaDatos;

    function pintaDatos() {
      var selRow = eval("document.all." + tab.id + "_Row" + tab.selectedIndex);
      selRow.bgColor = TB_Fondo;
    }

    document.onmouseup = checkKeys;

    function checkKeysLocal(evento,tecla) {
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else{
        teclaAuxiliar = evento.which;
    }

      if((layerVisible)||(divSegundoPlano)) buscar();
      keyDel(evento);
      if (teclaAuxiliar == 9){
          if(layerVisible) ocultarDiv();
        if(divSegundoPlano) divSegundoPlano = false;
          return false;
        }
        if(teclaAuxiliar==1){
             if(layerVisible) setTimeout("ocultarDiv()",30);
        if(divSegundoPlano)	divSegundoPlano = false;
        }

        if (teclaAuxiliar == 40){
          if((layerVisible)||(divSegundoPlano)) upDown(teclaAuxiliar);
          return false;
        }
        if (teclaAuxiliar == 38){
          if((layerVisible)||(divSegundoPlano)) upDown(teclaAuxiliar);
          return false;
        }

        if(teclaAuxiliar == 13){
          if((tab.selectedIndex>-1)&&(tab.selectedIndex < lista.length)&&(!ultimo)){
            if(document.forms[0].cmdNuevoTram.disabled == false) {
              callFromTableTo(tab.selectedIndex,tab.id);
            }
          }
        }

      if ((evento.button == 1)||(evento.button == 2)){
          if(layerVisible) setTimeout("ocultarDiv()",30);
        if(divSegundoPlano)	divSegundoPlano = false;
        }
    }
    </script>


    <script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>
 </BODY>

</html:html>
