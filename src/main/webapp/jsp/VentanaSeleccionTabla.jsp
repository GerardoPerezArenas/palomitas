<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<html:html>
<head>
<title>Búsqueda</title>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "2" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="1" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css" media="screen" >
<script type="text/javascript" src="../scripts/domlay.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<script type="text/javascript" src="../scripts/seleccion.js"></script>
<script type="text/javascript">
var APP_CONTEXT_PATH="<%=request.getContextPath()%>";
var i = 0;
<logic:iterate id="elemento" name="SelectForm" property="lista_resultado">
   listaOriginal[i] = ['<bean:write name="elemento" property="codigo"/>','<bean:write name="elemento" property="desc_c"/>'];
   var aux = '<bean:write name="elemento" property="codigo"/>';
   lista[i] = ['<bean:write name="elemento" property="codigo"/>','<a href=javascript:enviaSeleccion("'+aux+'");>'+'<bean:write name="elemento" property="desc_c"/>'+'</a>'];
   i++;
</logic:iterate>
function cerrar(){
  if(self.parent.opener.xanelaAuxiliar && !self.parent.opener.xanelaAuxiliar.closed){
     if (respuesta != null){
        eval('opener.document.forms[0].'+'<bean:write name="SelectForm" property="input_cod"/>'+'.value  = codSel');
        eval('opener.document.forms[0].'+'<bean:write name="SelectForm" property="input_desc"/>'+'.value = descSel');
     }
     self.parent.opener.retornoXanelaAuxiliar(respuesta);
  }else{
     eval('opener.document.forms[0].'+'<bean:write name="SelectForm" property="input_cod"/>'+'.value  = respuesta[0]');
     eval('opener.document.forms[0].'+'<bean:write name="SelectForm" property="input_desc"/>'+'.value = respuesta[1]');
    self.close();
  }
}
</script>
</head>
<body class="bandaBody" onLoad="cargarInicio();" onunload="cerrar();">
<html:form method="post" action="/CargarSelect.do">
<center>
<table border="0" cellpadding="0" cellspacing="0" align="center" width="210">
   <tr>			
      <td valign="top" align="center" width="100%"> 
         <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
            <tr>
               <td width="100%" align="center">
                  <table border="0" cellspacing="0" cellpadding="1" bordercolor="#FFFFFF">
                     <tr> 
                        <td align="center" class="fondo"><input type="text" name="codigo"      size="8"  maxlength="8" style="width:60"   onKeyUp="buscar();"></td>
                        <td align="center" class="fondo"><input type="text" name="descripcion" size="15" maxlength="50" style="width:150" onKeyUp="buscar();"></td>
                     </tr>
                     <tr>
                        <td colspan="2">
                           <hr width="100%" noshade size="1" color="#FFDD82">
                        </td>
                     </tr>
                  </table>
               </td>
            </tr>         
            <tr>
               <td align="left">
                  <table border="1" cellspacing="0" cellpadding="3" class="fondoCab" bordercolor="#FFFFFF">
                     <tr> 
                        <td align="center" class="cabecera"><div style="overflow:hidden;width:85;"><%=descriptor.getDescripcion("gEtiq_codigo")%></div></td>
                        <td align="center" class="cabecera"><div style="overflow:hidden;width:250;"><%=descriptor.getDescripcion("gEtiq_desc")%></div></td>
                     </tr>
                  </table>
               </td>
            </tr>
            <tr>
               <td class="fondo" height="220" valign="top" width="100%" align="left">
                  <div id="grid" valign="top" style="height:220" class="grid" width="335"  align="center">
                  <table border="0" cellspacing="0" cellpadding="0" width="335" align="center">
                     <tr> 
                        <td align="center">
                           <table border="0" cellpadding="0" cellspacing="0" width="335" align="center">
                              <tr>
                                 <td valign="top" align="center">
                                    <div id="tabla" style="background-color:white;" height="220" width="335" align="center"> 
                                    </div>
                                 </td>
                              </tr>
                           </table>
                        </td>
                     </tr>
                  </table>
                  </div>
               </td>            
            </tr>
         </table>								
      </td>
   </tr>
   <tr>
      <td align="center">
         <hr width="100%" noshade size="1" color="#FFDD82">
         <input type="button" value='<%=descriptor.getDescripcion("gbAceptar")%>'  onclick="javascript:enviaSeleccion('BOTON');" class="boton">
         <input type="button"  value='<%=descriptor.getDescripcion("gbLimpiar")%>' onclick="javascript:limpiaDatos();" class="boton">
         <hr width="100%" noshade size="1" color="#FFDD82">
      </td>			
   </tr>   
</table>
<div id="enlace">
</div>
</center>
</html:form>
<script type="text/javascript">
  var tab;
  if(document.all) tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.all.tabla,335);
  else tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'),335);
  tab.addColumna('85');
  tab.addColumna('250');
  function refresh(){
    tab.displayTabla();
  }
</script>
</body>
</html:html>
