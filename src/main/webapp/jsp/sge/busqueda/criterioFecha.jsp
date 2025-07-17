<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%
  int idioma=1;
  int apl=4;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
        }
  }
  
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<form name="criterioFecha">
<%
    String operador = (String)request.getAttribute("operador");
    String codigo   = (String)request.getParameter("codigo");
    String codCampoSeleccionado   = (String)request.getAttribute("codCampoSeleccionado");
%>
<table border="0" cellspacing="4" cellpadding="4" align="center">
<tr>
    <td class="etiqueta">
        <c:out value="${requestScope.nombre}"/>:
    </td>
    <td>
        <select name="valorOperadorCriterio" id="valorOperadorCriterio" class="inputTexto" onchange="javascript:comprobarOperadorFecha();">
            <%--
            <option value=""></option>
            --%>
            <option value="0" <% if("0".equals(operador) && (codigo.equalsIgnoreCase(codCampoSeleccionado))){ %> selected <%}%>> = </option>
            <option value="1" <% if("1".equals(operador) && (codigo.equalsIgnoreCase(codCampoSeleccionado))){ %> selected <%}%>> > </option>
            <option value="2" <% if("2".equals(operador) && (codigo.equalsIgnoreCase(codCampoSeleccionado))){ %> selected <%}%>> >= </option>
            <option value="3" <% if("3".equals(operador) && (codigo.equalsIgnoreCase(codCampoSeleccionado))){ %> selected <%}%>> &lt </option>
            <option value="4" <% if("4".equals(operador) && (codigo.equalsIgnoreCase(codCampoSeleccionado))){ %> selected <%}%>> &lt= </option>
            <option value="5" <% if("5".equals(operador) && (codigo.equalsIgnoreCase(codCampoSeleccionado))){ %> selected <%}%>> ENTRE </option>
        
        </select>
    </td>
    <td>
        <input type="text" class="inputTxtFecha" size="12" maxlength="10" name="fechaInicio"
               id="fechaInicio" onkeyup = "javascript: return SoloCaracteresFecha(this);"
                                onblur = "javascript:return comprobarFecha(this);"
                                onfocus = "this.select();"/>

        <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaInicio(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;" >
            <span class="fa fa-calendar" aria-hidden="true"  id="calFechaInicio" name="calFechaInicio" alt='<%=descriptor.getDescripcion("gEtiqFecha")%>' style="cursor:hand; border:0px none"></span>
        </A>
    </td>
    <td id="segundoCriterio" style="visibility:hidden;">
        <input type="text" class="inputTxtFecha" size="12" maxlength="10" name="fechaFin"
               id="fechaFin" onkeyup = "javascript: return SoloCaracteresFecha(this);"
                             onblur = "javascript:return comprobarFecha(this);"
                             onfocus = "this.select();"/>

        <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaFin(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;" >
            <span class="fa fa-calendar" aria-hidden="true"  id="calFechaFin" name="calFechaFin" alt='<%=descriptor.getDescripcion("gEtiqFecha")%>' style="cursor:hand; border:0px none"></span>
        </A>
    </td>
</tr>
</table>   
</form>    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript">
    var separador = "зе";
    var valores = '<c:out value="${requestScope.valores}"/>';
    var datos = new Array();
    datos = valores.split(separador);
    var codCampo             = '<c:out value="${requestScope.codigo}"/>';
    var codCampoSeleccionado = '<c:out value="${requestScope.codCampoSeleccionado}"/>';


    if(datos!=null && datos.length==1 && datos[0]!="" && codCampo!="" && codCampoSeleccionado!="" && codCampo.trim()==codCampoSeleccionado.trim()){
        document.forms[0].fechaInicio.value = datos[0];
    }else
    if(datos!=null && datos.length==2 && datos[0]!="" && datos[1]!="" && codCampo!="" && codCampoSeleccionado!="" && codCampo.trim()==codCampoSeleccionado.trim()){
        document.forms[0].fechaInicio.value = datos[0];
        document.forms[0].fechaFin.value    = datos[1];
        comprobarOperadorFecha();
    }

</script>
