<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.sge.DefinicionProcedimientosValueObject" %>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>

<html:html>
 <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title>::: Catálogo de Procedimientos:::</title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <%
        int idioma=1;
        int apl=4;

        UsuarioValueObject usuario=new UsuarioValueObject();
        Vector catalogo = new Vector();
        String directorio = "";
          if (usuario!=null) {
            usuario = (UsuarioValueObject)session.getAttribute("usuario");
            catalogo = (Vector)session.getAttribute("CatalogoProcedimientos");
            //session.removeAttribute("CatalogoProcedimientos");
            idioma     = usuario.getIdioma();
            apl        = usuario.getAppCod();
            directorio = usuario.getDtr();
          }
        
        DefinicionProcedimientosValueObject pVO=null;
        Config m_Config = ConfigServiceHelper.getConfig("common");
        String statusBar = m_Config.getString("JSP.StatusBar");

    %>
    
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script src="<c:url value='/scripts/xtree.js'/>"></script>
    <script src="<c:url value='/scripts/WebFXImageTreeItem.js'/>"></script>
    <script src="<c:url value='/scripts/WebFX2ImageTreeItem.js'/>"></script>
    <script type="text/javascript">
      var listaProcedimientos= new Array();

      function inicializar(){
        if (tree.childNodes.length <= 0) {
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjProcDisp")%>');
        }
      }

      var tree;
      var areas = new Array(); // Indexadas por codigo
      var procedimientos = new Array(); // Indexados x nodo del arbol
      var tramites = new Array(); // Tramites q se consultan. Indexados x nodo del arbol.

      var icoArea ='fa fa-puzzle-piece';
      var icoClassArea ='fa fa-folder-open';
      var icoProc = 'fa fa-cubes';
      var icoTram = 'fa fa-file-o';
      var icoClassTram = 'fa fa-clipboard';
      var icoE ='fa fa-arrow-right';
      var icoS ='fa fa-arrow-left';
      var icoFin = 'fa fa-times-circle';
      var icoResol = 'fa fa-pencil-square-o';
      var icoPreg = 'fa fa-question';
      var icoListaTram = 'fa fa-archive';
      var icoF = 'fa fa-thumbs-up';
      var icoDF = 'fa fa-thumbs-down';
      var imgBusq = 'fa fa-search-plus';
      var imgPrint = 'fa fa-print';

      var procedimientoBuscado;

      function pulsarCancelar(){
        pulsarCerrar();
      }

      function pulsarCerrar(){
        self.parent.opener.retornoXanelaAuxiliar(retorno);
      }

      function checkKeysLocal(evento,tecla){
          if(window.event) evento = window.event;
          keyDel(evento);
      }

      function desplegarProcedimiento(procedimiento){
        procedimientoBuscado = procedimiento;
        if (procedimientoBuscado!=undefined) {
                document.forms[0].codMunicipio.value = procedimientos[procedimientoBuscado.id][0];
                document.forms[0].txtCodigo.value = procedimientos[procedimientoBuscado.id][1];
                document.forms[0].opcion.value="catalogoProcedimientosTramites";
                document.forms[0].target="oculto";
                document.forms[0].action="<c:url value='/sge/DefinicionProcedimientos.do'/>";
                //alert(document.forms[0].codMunicipio.value+ " .."+ document.forms[0].txtCodigo.value);
                document.forms[0].submit();
        } else procedimientoBuscado = null;
      }

      function verTramite(codMunicipio, codProc, codTram, numTram){
        var source = "<c:url value='/sge/DefinicionTramites.do?opcion=deCatalogo&codMun='/>" + codMunicipio + "&codProc=" + codProc + "&codTram=" + codTram + "&nCS="+ numTram;
        //alert("<c:url value='/jsp/sge/mainVentana.jsp?source='/>"+source);
        abrirXanelaAuxiliar("<c:url value='/jsp/sge/mainVentana.jsp?source='/>"+source,"",
	'width=997,height=570,scrollbars=no,status='+ '<%=statusBar%>',function(resp){});
      }

      function consultarTramite(id){
        var tramite = tramites[id];
        if (tramite != undefined)
          verTramite(tramite[0],tramite[1],tramite[2],tramite[3]);
      }

      function verProcedimiento(codMunicipio, codProc){
        var source = "<c:url value='/sge/DefinicionProcedimientos.do?opcion=deCatalogo&codMun='/>" + codMunicipio + "&codProc=" + codProc;
        abrirXanelaAuxiliar("<c:url value='/jsp/sge/mainVentana.jsp?source='/>"+source,"",
	'width=998,height=950,scrollbars=no,status='+ '<%=statusBar%>',function(resp){});
      }

      function consultarProcedimiento(id){
        var procedimiento = procedimientos[id];
        if (procedimiento != undefined)
          verProcedimiento(procedimiento[0],procedimiento[1]);
      }

      function addTramites(l, padre) {
            if (l.length > 0 ) {
                var j=0;
                for (j=0; j<l.length; j++) {
                  var tlcs = new WebFXTreeItem(l[j][2],'',padre,icoTram, icoTram, 'webfx-tree-item');
                }
            }
      }

      function addCondicionSalidaResolucion(padre, condF, lF, condDF, lDF, tipolF, tipolDF) {

        var etiq = "FAVORABLE";
        if ( (condF == 'TramiteSI') && tipolF !='') { etiq += " (" + tipolF +")"; }
        var hijo = new WebFXTreeItem(etiq,'',padre,icoF,icoF,'webfx-tree-item');

        if (condF == 'TramiteSI') {
          addTramites(lF, hijo);
        } else if (condF == 'FinalizacionSI') {
          var csf = new WebFXTreeItem("FINALIZACION",'',hijo,icoFin,icoFin,'webfx-tree-item');
        }

        etiq = "DESFAVORABLE";
        if ( (condDF == 'TramiteNO') && tipolDF !='') { etiq += " (" + tipolDF +")"; }
        var hijo2 = new WebFXTreeItem(etiq,'',padre,icoDF,icoDF,'webfx-tree-item');
        if (condDF == 'TramiteNO') {
          addTramites(lDF, hijo2);
        } else if (condDF == 'FinalizacionNO') {
          var csf = new WebFXTreeItem("FINALIZACION",'',hijo2,icoFin,icoFin,'webfx-tree-item');
        }
      }


    function recuperaTramitesProcedimientos(listaTramites){

      // Los tramites del primer nodo se pueden consultar.

      var clases = new Array();
      var tramite;
      var clase;
      var i;

      if (procedimientoBuscado != null) {

        procedimientos[procedimientoBuscado.id][4] = true;
        for (i=0; i<listaTramites.length; i++ ) {

          clase = clases[listaTramites[i][5]];
          if (clase == undefined) {
            clase = new WebFXTreeItem(listaTramites[i][6],'',procedimientoBuscado,icoClassTram,icoClassTram,'webfx-tree-item-tramite');
            clases[listaTramites[i][5]]=clase;
          }

          var nmbf =  "consultarTramite";
          tramite = new WebFXImageTreeItem(listaTramites[i][4],null,clase,icoTram,icoTram,'webfx-tree-item-tramite', imgBusq,nmbf);
          tramites[tramite.id]=[listaTramites[i][0],listaTramites[i][1],listaTramites[i][2],listaTramites[i][3]];

          var lce = listaTramites[i][10];

          if (lce.length > 0 ) {
            var ce = new WebFXTreeItem('CONDICIONES DE ENTRADA','',tramite,icoE,icoE,'webfx-tree-item');
            var j=0;
            for (j=0; j<lce.length; j++) {
              if (lce[j][5]=="TRÁMITE")
                  var tce = new WebFXTreeItem(lce[j][3] + " (" + lce[j][4] + ") ",'',ce,icoTram,icoTram,'webfx-tree-item');
              else
                  var tce = new WebFXTreeItem("EXPRESIÓN: " + lce[j][6] + " ",'',ce,icoTram,icoTram,'webfx-tree-item');
            }
          }
          var tcs = listaTramites[i][7];
          var cs;
          var csH;
          var lcsF;
          var tlcs;

          if (tcs != "Vacia" ) {
            var etq = 'CONDICIONES DE SALIDA';
            if ( (tcs == 'Tramite') && (listaTramites[i][14]!='')) { etq += " (" + listaTramites[i][14] +")"; }
            cs = new WebFXTreeItem(etq,'',tramite,icoS,icoS,'webfx-tree-item');
            if (tcs == 'Finalizacion' ){
              csH = new WebFXTreeItem('FINALIZACION','',cs,icoFin,icoFin,'webfx-tree-item');
            } else if (tcs == 'Tramite') {
              lcsF = listaTramites[i][11];
              addTramites(lcsF, cs);
            } else if (tcs == 'Resolucion') {
                csH = new WebFXTreeItem('RESOLUCION','',cs,icoResol,icoResol,'webfx-tree-item');
                addCondicionSalidaResolucion(csH, listaTramites[i][8], listaTramites[i][11], listaTramites[i][9], listaTramites[i][12], listaTramites[i][14], listaTramites[i][15] );
            } else if (tcs == 'Pregunta') {
                csH = new WebFXTreeItem(listaTramites[i][13],'',cs,icoPreg,icoPreg,'webfx-tree-item');
                addCondicionSalidaResolucion(csH, listaTramites[i][8], listaTramites[i][11], listaTramites[i][9], listaTramites[i][12], listaTramites[i][14], listaTramites[i][15] );
              }
          }

        }
        //procedimientoBuscado.expandChildren();
        procedimientoBuscado.expand();
      }
    }

    function imprimirProcedimiento(id) {
      var procedimiento = procedimientos[id];
      var cMunicipio = procedimiento[0];
      var cProcedimiento = procedimiento[1];
      document.forms[0].codMunicipio.value=cMunicipio;
      document.forms[0].txtCodigo.value=cProcedimiento;
      document.forms[0].opcion.value="imprimirProcedimiento";
      document.forms[0].target="oculto";
      document.forms[0].action="<c:url value='/sge/DefinicionProcedimientos.do'/>";
      document.forms[0].submit();
    }

    function abrirInforme(nombre){
      var tipoFichero = '<%= request.getParameter("tipoFichero") %>';
      var directorio  = '<%=directorio%>';
      // PDFS NUEVA SITUACION
      //var source = "<c:url value='/jsp/verPdf.jsp?opcion=null&nombre='/>"+nombre;
      var source = "<c:url value='/jsp/verPdf.jsp?opcion=null&nombre='/>"+nombre + "&dir=" + directorio;      
      ventanaInforme = window.open('<%=request.getContextPath()%>/jsp/mainVentana.jsp?source='+source,'ventana','width=800px,height=550px,status='+ '<%=statusBar%>' + ',toolbar=no');
      ventanaInforme.focus(); 
      // FIN PDFS NUEVA SITUACION

      //ventanaInforme = window.open("/jsp/sge/ver_pdf.jsp?fichero='/pdf/"+nombre+".pdf'", "informe", "width=800px,height=550px,toolbar=no");
    }
    </script>
 </head>

<body class="bandaBody" onload="pleaseWait('off'); inicializar()">
<jsp:include page="/jsp/hidepage.jsp" flush="true">
    <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
</jsp:include>  
<html:form action="/sge/DefinicionProcedimientos.do" method="POST">
<html:hidden  property="opcion" value=""/>
<html:hidden  property="codMunicipio" />
<html:hidden  property="txtCodigo" /> <!-- Codigo del procedimiento -->

<div class="txttitblanco"><%=descriptor.getDescripcion("titCatalogoProc")%></div>
<div class="contenidoPantalla">
    <table style="width:100%">
        <tr>
            <td>
                <div class="webfx-tree-div">
                    <script type="text/javascript">                                  
                        tree = new WebFXTree('ÁREAS');
                        tree.icon=icoArea;
                        tree.openIcon=icoArea;
                        tree.estilo='webfx-tree-item-area';

                        var area;
                        var procedimiento;

                    <%
                        if (catalogo != null) {
                        int lengthTD = catalogo.size();
                        for (int i = 0; i < lengthTD; i++) {
                        pVO = (DefinicionProcedimientosValueObject) catalogo.elementAt(i);
                        String codMun = (String) pVO.getCodMunicipio();
                        String codArea = (String) pVO.getCodArea();
                        String descArea = (String) pVO.getDescArea();
                        String codProcedimiento = (String) pVO.getTxtCodigo();
                        String descProcedimiento = (String) pVO.getTxtDescripcion();
                    %>

                    area = areas['<%=codArea%>'];
                        if (area == undefined) {
                            area = new WebFXTreeItem('<%=descArea%>','',tree,icoClassArea,icoClassArea,'webfx-tree-item-areas');
                                areas['<%=codArea%>']=area;
                                }
                                // Salta con un solo click
                                //procedimiento = new WebFXTreeItem('<%=descProcedimiento%>','javascript:{consultarProcedimiento();}',area,icoProc,icoProc,'webfx-tree-item-area');
                                procedimiento = new WebFX2ImageTreeItem('<%=descProcedimiento%>',null,area,icoProc,icoProc,'webfx-tree-item-areas',imgBusq,"consultarProcedimiento",imgPrint,"imprimirProcedimiento");
                                    procedimiento.folder = true;
                                    procedimiento.open = false;
                                    procedimiento.toggle = function() {       
                                        if (this.childNodes.length <= 0 && !procedimientos[this.id][4]) {
                                            desplegarProcedimiento(this);
                                        } else {
                                        if (this.folder) {    
                                            if (this.open) { this.collapse(); }
                                            else { this.expand(); }
                                        }
                                    }
                                }          

                                procedimientos[procedimiento.id] =  new Array('<%=codMun%>','<%=codProcedimiento%>','<%=descProcedimiento%>','<%=codArea%>', false);

                            <%
                                    }
                                    }
                            %>

                            document.write(tree);
                            tree.expand();
                    </script>
                </div>
            </td>
        </tr>
    </table>
</div>
</html:form>
</body>
</html:html>

