<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="java.util.ArrayList" %>
<%@page import="es.altia.agora.business.administracion.mantenimiento.CampoDesplegableVO" %>
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

  ArrayList<CampoDesplegableVO> campos = (ArrayList<CampoDesplegableVO>)request.getAttribute("valores_desplegable");
  String operador = (String)request.getAttribute("operador");
  String codAux   = (String)request.getParameter("codigo");
  String codCampoSeleccionado   = (String)request.getAttribute("codCampoSeleccionado");
  String dValor     = (String)request.getAttribute("valores");

  String[] valores = dValor.split("зе");  
%>
<table border="0" align="center" cellspacing="4" cellpadding="4">
<tr>
    <td class="etiqueta">
        <c:out value="${requestScope.nombre}"/>:
    </td>
    <td>
        <select name="valorOperadorCriterio" id="valorOperadorCriterio" class="inputTexto">
            <%--
                <option value=""></option>
              --%>
                <option value="0" <% if("0".equals(operador) && (codAux.equalsIgnoreCase(codCampoSeleccionado))){ %> selected <%}%>> = </option>
                <option value="6" <% if("6".equals(operador) && (codAux.equalsIgnoreCase(codCampoSeleccionado))){ %> selected <%}%>> &lt> </option>              
         </select>
    </td>
    <td>
        <select name="valorBusqueda1" id="valorBusqueda1" class="inputTexto">
            <option value=""></option>
            <%
                for(int i=0;i<campos.size();i++)
                {
                    String codigo = campos.get(i).getCodigoValor();
                    String valor =  campos.get(i).getDescripcion();
            %>
                    <option value="<%=codigo%>" <% if(codigo.equalsIgnoreCase(valores[0])){ %> selected <% } %>><%=valor%></option>
            <%
                }// for
            %>
        </select>
    </td>
</tr>    
</table>