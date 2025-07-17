<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm"%>
<%@page import="es.altia.flexia.terceros.integracion.externa.factoria.AltaTerceroExternoFactoria"%>
<%@page import="es.altia.flexia.terceros.integracion.externa.vo.ErrorSistemaTerceroExternoVO"%>
<%@page import="java.util.Hashtable" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.util.Set" %>
<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<%
    int idioma = 1;
    int apl = 3;

    if (session != null) {
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        if (usuario != null) {
            idioma = usuario.getIdioma();
        }
    }

    BusquedaTercerosForm  formulario = (BusquedaTercerosForm)session.getAttribute("BusquedaTercerosForm");
    Hashtable<Integer, ArrayList<ErrorSistemaTerceroExternoVO>> errores = formulario.getErroresSistemaExterno();
    Set<Integer> claves = errores.keySet();
    Iterator<Integer> iterator = claves.iterator();
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
                 type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>"/>
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>"/>

<TITLE>
     <%=descriptor.getDescripcion("etiqInfoAltaTerExterno")%>
</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>

<script type="text/javascript">
    function cerrarVentana(){
        window.close();
    }
    
</script>
</head>
<BODY>
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("etiqInfoAltaTerExterno")%></div>
    <div class="contenidoPantalla">
        <table width="100%">
            <tr>
                <td>
                    <div style="height:410px;width:97%;overflow:auto;">
                        <table border="0" >
                        <%
                            while (iterator.hasNext()){
                                int clave = iterator.next();
                                String subtitulo = "";                                                                
                                switch(clave){    
                                    case 1: subtitulo = descriptor.getDescripcion("titErrorInstanceTerExterno");
                                            break;

                                    case 2: subtitulo = descriptor.getDescripcion("titErrorEjecTerExterno");
                                            break;

                                    case 3: subtitulo = descriptor.getDescripcion("titErrorCamposObligaTerExterno");
                                            break;

                                    case 4: subtitulo = descriptor.getDescripcion("titRestriccAltaTerExterno");
                                            break;

                                    case 5: subtitulo = descriptor.getDescripcion("titRestriccModTerExterno");
                                            break;

                                    case 6: subtitulo = descriptor.getDescripcion("titErroresAltaTerExterno");
                                            break;
                                }

                                ArrayList<ErrorSistemaTerceroExternoVO> LISTA =  errores.get(clave);
                                if(!"".equals(subtitulo) && LISTA.size()>=1){
                        %>
                                <tr>
                                    <td colspan="2"><strong><%=subtitulo%></strong></td>
                                </tr>

                        <%

                                for(int i=0;LISTA!=null && i<LISTA.size();i++)
                                {
                                    ErrorSistemaTerceroExternoVO oError = LISTA.get(i);
                                    ArrayList<String> etiquetas = oError.getListaErrores();

                                        for(String dato: etiquetas){                                                            
                        %>                                                            
                                        <tr valign="top" height="15">
                                            <td style="width:45px;" valign="top">&nbsp</td>
                                            <td class="etiqueta" valign="top" >
                                                <li>
                                                    <%=dato %>
                                                </li>
                                            </td>
                                        </tr>

                        <%
                                       }// for


                        %>

                        <%

                                 }// for
                               }//if
                            }// while   
                        %>
                        </table>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAceptar")%>" onclick="javascript:cerrarVentana();"/>
                </td>
            </tr>
        </table>
    </div>
</BODY>
</html:html>