<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<%
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  int idioma=1;
  int apl = 4;
  String[] param = new String[7];
  if (session.getAttribute("usuario") != null){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    idioma = usuarioVO.getIdioma();
    apl = usuarioVO.getAppCod();
  }
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<html:html>
 <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title>ORDENAR CAMPOS </title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">

<script type="text/javascript">
    var listaCamposOriginal = new Array();
    listaCamposOriginal = self.parent.opener.xanelaAuxiliarArgs;
    var posicionOrde = <%=request.getParameter("posicionOrde")%>;
    function desActivarBotones(desactivar) {
      document.forms[0].botonCancelar.disabled=desactivar;
      document.forms[0].botonAceptar.disabled=desactivar;
    }

    function inicializar() {
      desActivarBotones(false);
    }

    function pulsarAceptar(){
      var listaOrdenados = new Array();;
      var encontrado = false;
      var filas = document.getElementById('tblCampos').rows;
      for(i=0;i<filas.length;i++){
        var indice = 0;
        if(filas[i].id !=0) {
         indice = ((filas[i].id)*1)-1;
        }
        var valor = ((filas[i].rowIndex)*1)+1;
        listaCamposOriginal[indice][posicionOrde] = valor;
      }
      for(i=0;i<listaCamposOriginal.length;i++) {
        j=0
        while ((!encontrado) && (j<listaCamposOriginal.length)) {
            if (i == listaCamposOriginal[j][posicionOrde] - 1) {
                listaOrdenados[i] = listaCamposOriginal[j];
                encontrado = true;
            } else {
                j++;
            }
        }
        encontrado = false;
      }
      self.parent.opener.retornoXanelaAuxiliar(listaOrdenados);
    }

    function pulsarCancelar(){
      self.parent.opener.retornoXanelaAuxiliar();
    }

    var campoActivo=null;

    document.onkeydown =
      function (evt) {
        var c = document.layers ? evt.which
                : document.all ? event.keyCode
                : evt.keyCode;
      if(c==38) subeCampo();
      else if(c==40) bajaCampo();
        return true;
      };

    function selectCampo(){
     var campoID = this.id;
     var activo = campoActivo;
     var filas = document.getElementById('tblCampos').rows;
     for (var i=0;i<filas.length;i++){
         filas[i].className = "";
     }
     this.className = "selected";
      if(campoID!=null && campoID!=''){
        var campo = document.getElementById('div-'+campoID);
        if(campo) {
          if(campoID==activo){
            campo.className="ordenCampo";
            campoActivo = null;
          }else{
            campoActivo = campoID;
          }
        }
      }
      if(activo!=null && activo!=campoActivo){
        document.getElementById('div-'+activo).className="ordenCampo";
      }

    }

    function subeCampo(){
      if(campoActivo==null || campoActivo=='') return false;
      var filas = document.getElementById('tblCampos').rows;
      var fila = document.getElementById(campoActivo);      
      var pos = fila.rowIndex;      
      var filaNext = filas[pos-1];      
      if(filaNext!=null){
        this.formDirty=true;
        swapNodes(fila,filaNext);        
      }
    }


    function swapNodes(item1,item2)
    {
        var itemtmp = item1.cloneNode(1);
        var parent = item1.parentNode;
        item2 = parent.replaceChild(itemtmp,item2);
        parent.replaceChild(item2,item1);
        parent.replaceChild(item1,itemtmp);
        itemtmp = null;
    }


    function bajaCampo(){
      if(campoActivo==null || campoActivo=='') return false;
      var filas = document.getElementById('tblCampos').rows;
      var fila = document.getElementById(campoActivo);
      var pos = fila.rowIndex;
      var filaNext = filas[pos+1];
      if(filaNext!=null){
        this.formDirty=true;
        //fila.swapNode(filaNext);
        swapNodes(fila,filaNext);
      }
    }
    </script>
 </head>

 <body onload="javascript:{inicializar();}" >
    <html:form method="post" action="/sge/DefinicionProcedimientos.do" target="_self">
    <input type="hidden" name="opcion" value="">

    <div id="titulo" class="txttitblanco"> <%=descriptor.getDescripcion("titOrdenarCamp")%></div>
    <div class="contenidoPantalla">
        <table width="100%">
            <tr>
              <td width="90%">
                <table style="width:100%" class="xTabla compact dataTable no-footer">
                    <thead>
                        <tr>
                            <th style="width:10%"><%= descriptor.getDescripcion("etiq_orden")%></th>
                            <th style="width:20%"><%= descriptor.getDescripcion("gEtiq_codigo")%></th>
                            <th style="width:70%"><%= descriptor.getDescripcion("gEtiq_desc")%></th>
                        </tr>
                  </thead>
                </table>
              </td>
              <td width="10%">
              </td>
        </tr>
        <tr>
            <td width="90%">
                <table id="tblCampos" style="width:100%" class="xTabla compact dataTable no-footer">
                </table>
                <script type="text/javascript">
                        var tabla=document.getElementById("tblCampos");
                        for(var i=0;i<listaCamposOriginal.length;i++) {
                          var filap=tabla.insertRow(tabla.rows.length);
                          filap.id=listaCamposOriginal[i][posicionOrde];
                          filap.onclick=selectCampo;
                          var celdap=filap.insertCell(0);
                          var dh = '<table width="100%"><tr>';
                            dh += '<td width="10%" class="etiqueta">'+listaCamposOriginal[i][posicionOrde]
                            +'</td><td width="20%" class="etiqueta">';
                          dh += listaCamposOriginal[i][0]
                          + '</td><td width="70%" class="etiqueta" >'+listaCamposOriginal[i][1]
                          + '</td></tr></table>';
                          celdap.innerHTML='<div class="ordenCampo" id="div-'+listaCamposOriginal[i][posicionOrde]+'" >'+ dh +'</div>';
                        }
                </script>
            </td>
            <td width="10%">
                <table border="0" cellpadding="10" cellspacing="0">
                    <tr>
                      <td>
                          <span name="asc" onclick="subeCampo();return false;" class="fa fa-arrow-up"></span>
                  </td>
                </tr>
                <tr>
                  <td>
                        <span name="desc" onclick="bajaCampo();return false;" class="fa fa-arrow-down"></span>
                  </td>
                </tr>
              </table>
          </td>
        </tr>
      </table>
        <div id="tablaBotones" class="botoneraPrincipal">
            <input class="botonGeneral" name="botonAceptar" accesskey="A" type="button" value='<%=descriptor.getDescripcion("gbAceptar")%>' onclick="pulsarAceptar();">
            <input class="botonGeneral" name="botonCancelar" accesskey="C" type="button" value='<%=descriptor.getDescripcion("gbCancelar")%>' onclick="pulsarCancelar();">
        </div>
    </div>
</html:form>
 </body>
</html:html>
