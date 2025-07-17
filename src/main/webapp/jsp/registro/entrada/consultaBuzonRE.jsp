
<HTML>

<head>

<TITLE>::: BUZÓN - Rexistro Entrada ::: </TITLE>

<!-- Estilos -->

<LINK REL="stylesheet" MEDIA="screen" TYPE="text/css" href="<%=request.getContextPath()%>/css/estilo.css">
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<script src="<%=request.getContextPath()%>/scripts/general.js"></script>


<SCRIPT language="JavaScript">


  function pulsarCancelar() {

    window.location = "altaRE.jsp";
  }

  function pulsarRegistrar() {
  
    window.location = "altaREEjemplo.jsp";
  }

</SCRIPT>


</head>




<body onload="javascript:{ }">




<CENTER>
<form name="formulario" METHOD=POST target="_self">


<TABLE id ="tabla1" class="tablaP" width="650px" cellspacing="5px" cellpadding="5px">

<TR>
  <TD colspan="6" class="columnP"> <BR> </TD>
</TR>

<TR>
  <TD colspan="6" class="titulo"> BUZÓN </TD>
</TR>

<TR>
  <td class="etiqueta"> </td>
  <TD class="etiqueta"> <u>Depto. Origen </u></TD>
  <TD class="etiqueta"> <u>Unidad Reg. Origen </u></TD>
  <TD class="etiqueta"> <u>Año </u></TD>
  <TD class="etiqueta" align="center"> <u>Nº </u></TD>
  <TD class="etiqueta"> <u>Tipo </u></TD>
</TR>

<TR>
  <td class="textoSuelto"> <input type="radio" name="rb"></td>
  <TD class="textoSuelto"> Urbanismo </TD>
  <TD class="textoSuelto"> Gestión Urbanística </TD>
  <TD class="textoSuelto"> 2002 </TD>
  <TD class="textoSuelto" align="center"> 2002/1234 </TD>
  <TD class="textoSuelto"> Entrada </TD>
</TR>

<TR>
  <td class="textoSuelto"> <input type="radio" name="rb"></td>
  <TD class="textoSuelto"> Urbanismo </TD>
  <TD class="textoSuelto"> Licencia Urbanísticas y de Apertura </TD>
  <TD class="textoSuelto"> 2002 </TD>
  <TD class="textoSuelto" align="center"> 2002/1478 </TD>
  <TD class="textoSuelto"> Entrada </TD>
</TR>

<TR class="txtverde">
  <td class="textoSuelto"> <input type="radio" name="rb"></td>
  <TD class="textoSuelto"> Urbanismo </TD>
  <TD class="textoSuelto"> Planeamiento Urbanístico </TD>
  <TD class="textoSuelto"> 2002 </TD>
  <TD class="textoSuelto" align="center"> 2002/4448 </TD>
  <TD class="textoSuelto"> Saída </TD>
</TR>

<TR>
  <TD class="columnP" colspan="6" align="center">	
  
      	<TABLE class="tablaBotones" width="60%">
        <TR>  		
            <TD width="50%" align="center">    	
    		<input type="button" class="boton" value="Seleccionar" name="cmdRegistrar" onclick = "pulsarRegistrar();">
    	    </TD>
    	    <TD width="50%" align="center">    	
    		<input type="button" class="boton" value="Cancelar"  name="cmdSalir" onclick="pulsarCancelar();">	
    	    </TD>
    	</TR>
   	</TABLE>
  </TD>
</TR>

</TABLE>

</FORM>
</CENTER>

</BODY>

</HTML>
