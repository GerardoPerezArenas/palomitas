<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.agora.business.sge.InteresadoExpedienteVO"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>

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
        Config m_Config = ConfigServiceHelper.getConfig("common");
        String statusBar = m_Config.getString("JSP.StatusBar");
    %>
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<html:html>

<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

    <TITLE>::: Número de Expediente :::</TITLE>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />


    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>" media="screen" >
    <script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
    
    <SCRIPT type="text/javascript">

    var numeroExpediente;
    var interesados;
   
    
    function inicializar() {
      window.focus();

      var lista = new Array();
     

      document.forms[0].numeroExpediente.value = '<c:out value="${requestScope.numeroExpediente}"/>';
      numeroExpediente=document.forms[0].numeroExpediente.value;

      
      existenInteresadosEnReg= '<c:out value="${requestScope.existenInteresadosEnReg}"/>';

      var j=0;
      <%
       Vector interesado =(Vector)request.getAttribute("interesados");
       
       for(int i=0;i<interesado.size();i++)
       {
           InteresadoExpedienteVO interesadoVO = new InteresadoExpedienteVO();
           interesadoVO=(InteresadoExpedienteVO)interesado.get(i);
        %>
          
           var dni="<%=interesadoVO.getTxtDoc()%>";
           var nombre="<%=StringEscapeUtils.escapeJavaScript(interesadoVO.getNombreCompleto())%>";
           var domicilio="<%=interesadoVO.getDomicilio()%>-<%=interesadoVO.getMunicipio()%>-<%=interesadoVO.getProvincia()%>";
           lista[j]=[dni,nombre,domicilio];
           var codigoTerc='<%=interesadoVO.getCodTercero()%>';
           var codigoDomTerc='<%=interesadoVO.getCodDomicilio()%>';
           j=j+1;
      <% }%>

    
        
        tab.lineas=lista;
        tab.displayTabla();
      }
    
    function pulsarAceptar() {

      var retorno = new Array();
      retorno[0]=1;
      retorno[1]=existenInteresadosEnReg;
      self.parent.opener.retornoXanelaAuxiliar(retorno);
    }

  
    function pulsarCancelar() {
      var retorno = new Array();

      retorno[0]=0;
      self.parent.opener.retornoXanelaAuxiliar(retorno);
    }

   
    </SCRIPT>
</head>

<BODY class="bandaBody" onload="javascript:{inicializar();}">
    <html:form action="/sge/Tramitacion.do" target="_self">
        
        <html:hidden  property="opcion" value=""/>
        <html:hidden  property="numeroExpediente"/>
        
    <div id="titulo" class="txttitblanco">&nbsp;<%=descriptor.getDescripcion("titAdjuntarExp")%></div>
    <div class="contenidoPantalla" valign="top">
     <TABLE cellspacing="10px" cellpadding="0px" border="0px" style="margin-bottom:10px">

                          <tr>
                            <td  class="etiqueta"><%=descriptor.getDescripcion("desAdjuntar")%>&nbsp;<c:out value="${requestScope.numeroExpediente}"/>?</td>
                         </tr>
                         <tr >
                         </tr>
                        <tr>
                            <td  class="etiqueta"><%=descriptor.getDescripcion("txtInteresadosPrincip")%>:</td>
                         </tr>
                         <tr>
                            <TD id="tabla" align="left"></TD>
                        </tr>
                    </table>
                </td>
            </table>
            </div>
            <div class="capaFooter">
       
            <DIV id="capaBotones1" name="capaBotones1" class="botoneraPrincipal">
                <input type="button" class="botonGeneral"value=<%=descriptor.getDescripcion("gbAceptar")%> onclick="pulsarAceptar();"></td>
                <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbcaNcelar")%> onclick="pulsarCancelar();"></td>
            </DIV>
            </div>
        </html:form>
        
        <script type="text/javascript">



    var tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'), 745);
    tab.addColumna('110', null, 'Documento');
    tab.addColumna('300', 'left', 'Nombre');
    tab.addColumna('335', 'left', 'Direccion completa');
    tab.displayCabecera=true;
    tab.displayTabla();

    


            document.onmousedown = checkKeys;

    function checkKeysLocal(evento,tecla) {
        if(window.event) evento = window.event;
        keyDel(evento);

    }
        </script>
        
    
        <script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>
    </BODY>
</html:html>
