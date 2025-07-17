<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="java.text.MessageFormat"%>

<html:html>

    <head>
        <jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

        <TITLE>::: EXPEDIENTES  DefiniciÛn de Agrupaciones de campos:::</TITLE>
        <jsp:include page="/jsp/plantillas/Metas.jsp" />

        <!-- Estilos -->

        <%
        int idioma=1;
        int apl=1;
        int munic = 0;
            if (session!=null){
            UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
                if (usuario!=null) {
                idioma = usuario.getIdioma();
                apl = usuario.getAppCod();
                munic = usuario.getOrgCod();
                }
        }
        String municipio = Integer.toString(munic);
        String aplicacion = Integer.toString(apl);
        String modoInicio = "";
        if (session.getAttribute("modoInicio") != null) {
            modoInicio = (String) session.getAttribute("modoInicio");
            session.removeAttribute("modoInicio");
        }
        String lectura = "";
        if (session.getAttribute("lectura") != null) {
            lectura = (String) session.getAttribute("lectura");
            session.removeAttribute("lectura");
        }
        %>

        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>

        <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">

        <script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>

        <SCRIPT type="text/javascript">
            function inicializar() { 
                var cargaCampo="<%=request.getParameter("cargaCampo")%>";
                document.forms[0].desdeProcedimiento.value="<%=request.getParameter("desdeProcedimiento")%>";
                
                if (cargaCampo=="si") {
                    var retorno = new Array();
                    if (document.forms[0].desdeProcedimiento.value=="no") {
                        retorno = [document.forms[0].codAgrupacion.value, document.forms[0].descAgrupacion.value, document.forms[0].ordenAgrupacion.value];
                    }else{
                        retorno = [document.forms[0].codAgrupacion.value, document.forms[0].descAgrupacion.value, document.forms[0].ordenAgrupacion.value];
                    }//if (document.forms[0].desdeProcedimiento.value=="no")
                    self.parent.opener.retornoXanelaAuxiliar(retorno);
                }else{
                    mostrarCapasBotones('capaBotones1');
                    <%if ("modificarDesdeProcedimiento".equals(modoInicio) || "modificarDesdeTramite".equals(modoInicio)) {%>
                        document.forms[0].codAgrupacion.disabled = true;
                    <%}else{%>
                        document.forms[0].codAgrupacion.value = '';
                        document.forms[0].descAgrupacion.value = '';
                        document.forms[0].ordenAgrupacion.value = '';
                    <%}%>
                    <% if("si".equals(lectura)) { %>
                        mostrarCapasBotones('capaBotones2');
                        desactivarFormulario();
                        var botonSalir = [document.forms[0].cmdSalir2];
                        habilitarGeneral(botonSalir);
                    <%}%>
                }//if (cargaCampo=="si") 
            }//inicializar
            
            function SoloCaracterValidosEspacio(objeto) {      
                var valores='1234567890ABCDEFGHIJKLMN—OPQRSTUVWYXZ !$()=¡…Õ”⁄·ÈÌÛ˙¸«Á|+-*?ø°';
                var numeros='1234567890';
                xAMayusculas(objeto);
                if (objeto){
                    var original = objeto.value;        
                    var salida = "";
                    for(i=0;i<original.length;i++){
                        if(valores.indexOf(original.charAt(i).toUpperCase())!=-1 || numeros.indexOf(original.charAt(i).toUpperCase())!=-1){
                            salida = salida + original.charAt(i);
                        }//if(valores.indexOf(original.charAt(i).toUpperCase())!=-1 || numeros.indexOf(original.charAt(i).toUpperCase())!=-1)
                    }//for(i=0;i<original.length;i++)
                    objeto.value=salida.toUpperCase();
                }//if (objeto)   
            }//SoloCaracterValidosEspacio
            
            function SoloCaracterValidos(objeto) {
                var valores='1234567890ABCDEFGHIJKLMN—OPQRSTUVWYXZ';
                var numeros='1234567890';
                xAMayusculas(objeto);
                if (objeto){
                    var original = objeto.value;
                    var salida = "";
                    for(i=0;i<original.length;i++){
                        if(valores.indexOf(original.charAt(i).toUpperCase())!=-1 || numeros.indexOf(original.charAt(i).toUpperCase())!=-1){
                            var valido = true;
                            if(numeros.indexOf(original.charAt(i).toUpperCase())!=-1 && original.length==1) valido = false;
                            if(valido) salida = salida + original.charAt(i);
                        }//if(valores.indexOf(original.charAt(i).toUpperCase())!=-1 || numeros.indexOf(original.charAt(i).toUpperCase())!=-1)
                    }//for(i=0;i<original.length;i++)
                    objeto.value=salida.toUpperCase();
                }//if (objeto)
            }//SoloCaracterValidos
            
            function validarOrdenAgrupacion(){
                var retorno = true;
                var orden = parseInt(document.forms[0].ordenAgrupacion.value);
                if(orden < 1){
                    retorno = false;
                }//if(orden < 1)
                return retorno
            }//validarOrdenAgrupacion
            
            function pulsarSalir() {
                self.parent.opener.retornoXanelaAuxiliar();
            }//pulsarSalir
            
            function pulsarAceptar () { 
                if( validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
                    if(validarOrdenAgrupacion()){
                        <% if("altaDesdeTramite".equals(modoInicio) || "modificarDesdeTramite".equals(modoInicio)) { %>
                            document.forms[0].desdeProcedimiento.value="no";
                        <% } %>
                        document.forms[0].target="oculto";
                        document.forms[0].opcion.value="validarAgrupacion";
                        document.forms[0].action="<c:url value='/sge/DefinicionAgrupacionCampo.do'/>";
                        document.forms[0].submit();
                    }else{
                        jsp_alerta("A",'<%=descriptor.getDescripcion("getiq_primagrup")%>');
                    }
                }//if
            }//pulsarAceptar

        </SCRIPT>
    </head>
    <BODY class="bandaBody" onload="javascript:{ pleaseWait('off'); 
            inicializar()}">
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
<html:form action="/sge/DefinicionAgrupacionCampo" target="_self">
    <html:hidden  property="opcion" value=""/>
    <input type="hidden" name="desdeProcedimiento">

<div class="txttitblanco"><%=descriptor.getDescripcion("tit_defCampo")%></div>
<div class="contenidoPantalla" valign="top">
    <table id ="tablaDatosGral">
        <tr>
            <td style="width:15%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_codigo")%>:</td>
            <td class="columnP">
                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="codAgrupacion" size="6" maxlength="5"
                    onkeyup="return SoloCaracterValidos(this);"/>
            </td>
        </tr>
        <tr>
            <td class="etiqueta"><%=descriptor.getDescripcion("etiq_rotulo")%>:</td>
            <td class="columnP">
                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="descAgrupacion" size="108" maxlength="50"
                    onkeyup="return SoloCaracterValidosEspacio(this);"/>
            </td>
        </tr>
        <tr>
            <td class="etiqueta"><%= descriptor.getDescripcion("getiq_orden")%>:</td>
            <td class="columnP">
                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="ordenAgrupacion" size="6" maxlength="4"
                    onkeyup="return SoloDigitos(this);"/>
            </td>
        </tr>  
    </table>
    <DIV id="capaBotones1" name="capaBotones1" STYLE="display:none;" class="botoneraPrincipal">
        <INPUT type= "button" class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar"  onclick="pulsarAceptar();">
        <INPUT type= "button" class="botonGeneral" accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>" name="cmdSalir"  onclick="pulsarSalir();">
    </DIV>
    <DIV id="capaBotones2" name="capaBotones2" STYLE="display:none;" class="botoneraPrincipal">
        <INPUT type= "button" class="botonGeneral" accesskey="S" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir2"  onclick="pulsarSalir();">
    </DIV>
</div>
</html:form>capaBotones
    <script language="JavaScript1.2">
            function mostrarCapasBotones(nombreCapa) {
                document.getElementById('capaBotones1').style.display='none';
                document.getElementById('capaBotones2').style.display='none';
                document.getElementById(nombreCapa).style.display='';
            }//mostrarCapasBotones
            
        </script>
    </BODY>
</html:html>
