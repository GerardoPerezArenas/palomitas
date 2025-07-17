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
<%@page import="es.altia.agora.business.registro.RegistroValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm"%>
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

    <TITLE>::: Interesados Registro :::</TITLE>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />


    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
    <script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
      <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>" media="screen" >
  
   

    <SCRIPT type="text/javascript">

    var numeroExpediente;
    var interesados;
   
    
    function inicializar() {
      window.focus();

      var listaIntSoloReg = new Array();
     
      var j=0;
      
    <%
     Vector tercerosSoloEnRegistro =(Vector)request.getAttribute("tercerosSoloEnRegistro");
     MantAnotacionRegistroForm registroForm = (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm");
     RegistroValueObject elRegistroESVO = registroForm.getRegistro();
     //Vector tercerosSoloEnRegistro= registroForm.getListaInteresadosSoloRegistro();
     
     if(tercerosSoloEnRegistro.size()>0)
     {
        for(int i=0;i<tercerosSoloEnRegistro.size();i++)
       {
            GeneralValueObject interesadogVO = new GeneralValueObject();
            interesadogVO=(GeneralValueObject)tercerosSoloEnRegistro.get(i);

        %>
          
           var dni='<%=(String)interesadogVO.getAtributo("doc")%>';
           var nombre='<%=StringEscapeUtils.escapeJavaScript((String)interesadogVO.getAtributo("titular"))%>';
           var domicilio='<%=(String)interesadogVO.getAtributo("descDomicilio")%>';
           listaIntSoloReg[j]=[dni,nombre,domicilio];
           j++;
    <%
        }
     }else
        {
          %>

           var dni='<%=elRegistroESVO.getDocumentoInteresado()%>';
           var nombre='<%=elRegistroESVO.getNomCompletoInteresado()%>';
           var domicilio='<%=elRegistroESVO.getDomCompletoInteresado()%>';
           listaIntSoloReg[0]=[dni,nombre,domicilio];
            <%



        }

    %>

      
        
        tab.lineas=listaIntSoloReg;
        tab.displayTabla();
      }
    
    function pulsarAceptar() {

        self.parent.opener.retornoXanelaAuxiliar(1);      
        
    }

  
    function pulsarCancelar() {
     
       self.parent.opener.retornoXanelaAuxiliar(0);      
    }

   
    </SCRIPT>
</head>

<BODY class="bandaBody" onload="javascript:{inicializar();}">
    <html:form action="/sge/Tramitacion.do" target="_self">
        
        <html:hidden  property="opcion" value=""/>
        <html:hidden  property="numeroExpediente"/>
        
    <div id="titulo" class="txttitblanco">&nbsp;<%=descriptor.getDescripcion("etiqIntReg2Exp")%></div>
    <div class="contenidoPantalla" valign="top">
         <TABLE cellspacing="10px" cellpadding="0px" border="0px" style="margin-bottom:10px">

           
                          <tr>
                            <td align="left" style="width:100%; padding-left: 15px" class="etiqueta"><%=descriptor.getDescripcion("etiqAñadirIntReg2Exp")%></td>
                         </tr>
                         <tr >

                         </tr>
                         <tr>
                            <TD id="tabla" align="left"></TD>
                        </tr>


                   </table>
                </td>
                </tr>
            </table>
             </div>
            <div class="capaFooter">
            <DIV id="capaBotones1" name="capaBotones1" class="botoneraPrincipal">
                <input type="button" class="botonGeneral"value='Añadir' onclick="pulsarAceptar();"></td>
                <input type="button" class="botonGeneral" value='No añadir' onclick="pulsarCancelar();"></td>
            </DIV>
            </div>
        </html:form>
        <script type="text/javascript">
    var tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'), 720);
    tab.addColumna('90', null, 'Documento');
    tab.addColumna('300', 'left', 'Nombre');
    tab.addColumna('310', 'left', 'Direccion completa');
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
