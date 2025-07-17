<html>
<head>
<title>Búsqueda</title>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "2" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="1" />

<% String campoCod  = request.getParameter("campoCod");
   String campoDesc = request.getParameter("campoDesc"); %>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/estilo.css" type="text/css">
<script type="text/javascript" src="../scripts/domlay.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<script type="text/javascript" src="../scripts/seleccion.js"></script>
<script type="text/javascript">
var APP_CONTEXT_PATH="<%=request.getContextPath()%>";
for (var i=0; i < codigo.length; i++){
   listaOriginal[i] = [codigo[i],descripcion[i]];
   lista[i]  = [codigo[i],'<a href=javascript:enviaSeleccion("'+codigo[i]+'");>'+descripcion[i]+'</a>'];
}
function cerrar(){
  if(self.parent.opener.xanelaAuxiliar && !self.parent.opener.xanelaAuxiliar.closed){
     if (respuesta != null){
        self.parent.opener.rellenarCampos(codSel,descSel,'<%=campoCod%>','<%=campoDesc%>');
     }
     self.parent.opener.retornoXanelaAuxiliar(respuesta);
  }else{
     opener.rellenarCampos(respuesta[0],respuesta[1],'<%=campoCod%>','<%=campoDesc%>');
     self.close();
  }
}
</script>
</head>
<body class="bandaBody" onLoad="cargarInicio();" onunload="cerrar();">
<center>
<form>
<table border="0" cellpadding="0" cellspacing="0" align="center" width="335">
   <tr>			
      <td valign="top" align="center" width="100%"> 
         <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
            <tr>
               <td width="100%" align="left">
                  <table border="0" cellspacing="0" cellpadding="1" bordercolor="#FFFFFF" width="100%">
                     <tr> 
                        <td align="center" class="fondo"><input type="text" name="codigo"      size="8"  maxlength="12" class="inputTexto" style="width:85"  onKeyUp="buscar();"></td>
                        <td align="center" class="fondo"><input type="text" name="descripcion" size="15" maxlength="50" class="inputTexto" style="width:250" onKeyUp="buscar();"></td>
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
                        <td align="center" class="cabecera"><div style="overflow:hidden;width:85;">Código</div></td>
                        <td align="center" class="cabecera"><div style="overflow:hidden;width:250;">Descripción</div></td>
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
         <input type="button" value="Aceptar" onclick="javascript:enviaSeleccion('BOTON');" class="boton">
         <input type="button"  value="Limpar" onclick="javascript:limpiaDatos();" class="boton">				
         <hr width="100%" noshade size="1" color="#FFDD82">
      </td>			
   </tr>   
</table>
<div id="enlace">
</div>
</form>
</center>
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
</html>
