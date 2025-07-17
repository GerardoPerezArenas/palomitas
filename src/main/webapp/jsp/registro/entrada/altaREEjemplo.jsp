
<HTML>

<head>

<TITLE>::: REXISTRO ENTRADA - Alta :::</TITLE>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/tab.webfx.css" />

<!-- Ficheros JavaScript -->

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<link rel="stylesheet" MEDIA="screen" TYPE="text/css" href="<%=request.getContextPath()%>/css/estilo.css">
<script src="<%=request.getContextPath()%>/scripts/general.js"></script>

<script src="<%=request.getContextPath()%>/scripts/calendario.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>



<SCRIPT language="JavaScript">

function muestracapa(id) {

    var opcion = document.formulario.cbTipoEntrada.selectedIndex;
            
    if (opcion == 0) {
    
	    document.formulario.cbTramitadoraDest.disabled=true;
	    document.formulario.cbUnidadeRexistro.disabled=false;
    } else {
	    document.formulario.cbTramitadoraDest.disabled=false;
	    document.formulario.cbUnidadeRexistro.disabled=true;
    }
  }



// ------ Calendario

function mostrarCalDesde() {

  if (document.getElementById("calDesde").className.indexOf("fa-calendar") != -1 ) 

    showCalendar('formulario','txtDiaDoc','txtDiaDoc','txtMesDoc','txtAnoDoc','','calDesde','');
}


// ------ Funciones de página

function pulsarBuzon() {

    window.location = "consultaBuzonRE.jsp";

}

function pulsarCancelar() {

    window.location = "<%=request.getContextPath()%>/jsp/registro/presentacionRexES.jsp";
}

function pulsarRegistrar() {
  
   if (formulario.txtDiaDoc.value == "") {
   
       jsp_alerta("A","Data do documento: Introducir día"); 
       
       formulario.txtDiaDoc.focus();
       
       return;
   }

   if (formulario.txtMesDoc.value == "") {
   
       jsp_alerta("A","Data do documento: Introducir mes"); 
       
       formulario.txtMesDoc.focus();
       
       return;
   }
   
   if (formulario.txtAnoDoc.value == "") {
   
       jsp_alerta("A","Data do documento: Introducir año"); 
       
       formulario.txtAnoDoc.focus();
       
       return;
   }

   if (formulario.txtHoraDoc.value == "") {
   
       jsp_alerta("A","Data do documento: Introducir hora"); 
       
       formulario.txtHoraDoc.focus();
       
       return;
   }

   if (formulario.txtMinDoc.value == "") {
   
       jsp_alerta("A","Data do documento: Introducir minutos"); 
       
       formulario.txtMinDoc.focus();
       
       return;
   }

   if (formulario.txtHoraDoc.value == "") {
   
       jsp_alerta("A","Introducir Asunto"); 
       
       formulario.txtAsunto.focus();
       
       return;
   }
   
    jsp_alerta("A","Nº Entrada 2002/23 <br> Nª Expediente UB001 2002/2");

    window.location = "altaRE.jsp";
}

function buscarDocTipoDoc() {

	open('<%=request.getContextPath()%>/jsp/registro/busqTipoDoc.html', 'Sizewindow', 'width=375,height=200,scrollbars=no,toolbar=no')
} 
	
function buscarDocRazonSocial() {

	open('<%=request.getContextPath()%>/jsp/registro/busqRazonSocial.html', 'Sizewindow', 'width=375,height=200,scrollbars=no,toolbar=no')
} 

</SCRIPT>

</head>


<BODY onload="javascript:{  
			}">


<FORM name = "formulario" METHOD=POST target="_self">


<CENTER>

<TABLE id ="tabla1" class="tablaP" width="650px" cellspacing="5px" cellpadding="5px">
<TR>
  <TD class="titulo">Consulta de Entradas</TD>
</TR>
<TR>
  <TD class="columnP">
	<BR> <!-- Línea en blanco -->  
  </TD>
</TR>
</TABLE>

<!-- ------------------------------------------------------------------ -->
<!-- 				PESTANAS 				-->
<!-- ------------------------------------------------------------------ -->

<center>

<div class="tab-pane" id="tab-pane-1">


   <!-- CAPA 1: DATOS GENERALES 
        ------------------------------ -->

   <div class="tab-page">

      <h2 class="tab">Datos Xerais</h2>

      <TABLE id ="tablaDatosGral" width="600px" cellspacing="7px" cellpadding="1px" border="0">

      <TR>
	  <TD>

	      <TABLE width="100%" cellspacing="0" cellpadding="0" border="0">

	      <TR>
		  <TD width="25%" class="etiqueta">Data do documento:
    		  	<span class="etiqueta">*</span>
		  </TD>

		  <TD width="30%" class="columnP">

			 <INPUT TYPE="text" class="inputTxtFecha" size=1 maxlength=2 NAME="txtDiaDoc" value="01"
			 onKeyPress = "javascript:return SoloDigitos(event);" 
			 onKeyUp    = "MarcarObligatorios(this);" onfocus="this.select();" 
			 onfocus    = "this.select();"
			 onblur     = "MarcarObligatorios(this);" 
			 onchange   = "this.value=formateaIntToString(document.formulario.txtDiaDoc.value,2);
				       return esFecha(document.formulario.txtDiaDoc,
						      document.formulario.txtMesDoc,
						      document.formulario.txtAnoDoc);"> /


			  <INPUT TYPE="text" class="inputTxtFecha" size=1 maxlength=2 NAME="txtMesDoc" value="10"
			  onKeyPress="javascript:return SoloDigitos(event);" 
			  onKeyUp="MarcarObligatorios(this);" 
			  onfocus="this.select();" 
			  onblur="MarcarObligatorios(this);" 
			  onchange="this.value= formateaIntToString(document.formulario.txtMesDoc.value,2);
				    return esFecha(document.formulario.txtDiaDoc,
						   document.formulario.txtMesDoc,
						   document.formulario.txtAnoDoc);"> /

			  <INPUT TYPE="text" class='inputTxtFecha' size=3 maxlength=4 NAME="txtAnoDoc" value="2002"
			  onKeyPress="javascript:return SoloDigitos(event);" 
			  onKeyUp="MarcarObligatorios(this);" 
			  onfocus="this.select();" 
			  onblur="MarcarObligatorios(this);" 
			  onchange="this.value=formateaAno(document.formulario.txtAnoDoc.value);
				    return esFecha(document.formulario.txtDiaDoc,
						  document.formulario.txtMesDoc,
						  document.formulario.txtAnoDoc);">

			  &nbsp;   
			 <A href="javascript:calClick();return false;" onClick="mostrarCalDesde();return false;">
			 <span class="fa fa-calendar" aria-hidden="true" name="calDesde" alt="Data" ></span>
			 </A>
		  </TD>
		 
		  <TD width="25%" class="etiqueta">Hora do documento:
    		 	 <span class="etiqueta">*</span>
		  </TD>

		  <TD width="20%" class="columnP">
			 <INPUT TYPE="text" class="inputTxtFecha" SIZE=1 MAXLENGTH=2 NAME="txtHoraDoc" value="11"
    	     		 	 onKeyPress="javascript:return SoloDigitos(event);" 
			 	 onfocus="this.select();" > :
    	     		 <INPUT TYPE="text" class="inputTxtFecha" SIZE=1 MAXLENGTH=2 NAME="txtMinDoc" value="15"
    	     		   	 onKeyPress="javascript:return SoloDigitos(event);" 
			 	 onfocus="this.select();" > 	      	      		  </TD>
	      </TR>
	      </TABLE>

	  </TD>
      </TR>

      <TR>
          <TD>

	      <TABLE width="100%" cellspacing="0px" cellpadding="0px" border="0">
	      <TR>
		  <TD width="25%" class="etiqueta"> Data de entrada:</TD>
		  <TD width="75%" class="columnP">

			<INPUT TYPE="text" class="inputTxtFecha" size=1 maxlength=2 NAME="txtDiaE" value="02" disabled 
			  onKeyPress = "javascript:return SoloDigitos(event);" 
			  onKeyUp    = "MarcarObligatorios(this);" onfocus="this.select();" 
			  onfocus    = "this.select();"
			  onblur     = "MarcarObligatorios(this);" 
			  onchange   = "this.value=formateaIntToString(document.formulario.txtDiaCierre.value,2);
				return esFecha(document.formulario.txtDiaE,
				document.formulario.txtMesE,
				document.formulario.txtAnoE);"> /


			<INPUT TYPE="text" class="inputTxtFecha" size=1 maxlength=2 NAME="txtMesE" value="10" disabled
			  onKeyPress="javascript:return SoloDigitos(event);" 
			  onKeyUp="MarcarObligatorios(this);" 
			  onfocus="this.select();" 
			  onblur="MarcarObligatorios(this);" 
			  onchange="this.value= formateaIntToString(document.formulario.txtMesE.value,2);
				return esFecha(document.formulario.txtDiaE,
				document.formulario.txtMesE,
				document.formulario.txtAnoE);"> /

			<INPUT TYPE="text" class="inputTxtFecha" size=3 maxlength=4 NAME="txtAnoE" value="2002" disabled
			  onKeyPress="javascript:return SoloDigitos(event);" 
			  onKeyUp="MarcarObligatorios(this);" 
			  onfocus="this.select();" 
			  onblur="MarcarObligatorios(this);" 
			  onchange="this.value=formateaAno(document.formulario.txtAnoE.value);
				return esFecha(document.formulario.txtDiaE,
 				document.formulario.txtMesE,
				document.formulario.txtAnoE);">
		  </TD>
	      </TR>
	      </TABLE>

          </TD>
      </TR>

      <TR>
          <TD>

	      <TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	      <TR>

		  <TD width="25%" class="etiqueta">Asunto:
			<span class="etiqueta">*</span>
		  </TD>

		  <TD width="75%" >
			<textarea class="textareaTexto" cols="68" rows="8" name="txtAsunto" WRAP="virtual">Solicitud de licencia de obras mayores.</textarea>
		  </TD>

	      </TR>
	      </TABLE>

          </TD>
      </TR>
      
      <TR>
          <TD>

	      <TABLE width="100%" cellspacing="0px" cellpadding="0px">
	      <TR>
		  <TD width="25%" class="etiqueta">Tipo documento:
			  <span class="etiqueta">*</span>
		  </TD>

		  <TD width="75%" valign="top">

			<SELECT NAME="cbTipoDocumento" class="inputCombo">
				<option></option>
				<OPTION VALUE="AC">AC Acta</OPTION>
				<OPTION VALUE="CA">CA Carta</OPTION>
				<OPTION VALUE="CE">CE Certificado</OPTION>
				<OPTION VALUE="DE">DE Denuncia</OPTION>
				<OPTION VALUE="DX">DX Declaración Xunta</OPTION>
				<OPTION VALUE="FA">FA Factura</OPTION>
				<OPTION VALUE="IN">IN Informe ou Dictame</OPTION>
				<OPTION VALUE="IS" selected>IS Instancia ou Solicitude</OPTION>
				<OPTION VALUE="NI">NI Nota Interior</OPTION>
				<OPTION VALUE="NO">NO Notificación</OPTION>
				<OPTION VALUE="OF">OF Proposta ou Oferta Económica</OPTION>
				<OPTION VALUE="OI">OI Oficio</OPTION>
				<OPTION VALUE="OR">OR Orzamento</OPTION>
				<OPTION VALUE="QU">QU Queixa</OPTION>
				<OPTION VALUE="RC">RC Recurso</OPTION>
				<OPTION VALUE="RE">RE Reclamación</OPTION>
			</SELECT>
		  </TD>

	      </TR>
	      </TABLE>

          </TD>
      </TR>


      <TR>
          <TD>

	      <TABLE width="100%" border="0" bordercolor="purple" cellspacing="0" cellpadding="0">
	      <TR>

		  <TD width="25%" class="etiqueta">Tipo Entrada:
		  <span class="etiqueta">*</span>
		  </TD>

		  <TD width="75%" valign="top">

			<SELECT NAME="cbTipoEntrada" class="inputCombo" onChange="muestracapa('onChange')">
				<OPTION> </OPTION>
				<OPTION VALUE="capa1" onClick="muestracapa('capa1')" selected>Entrada Propia </OPTION>
				<OPTION VALUE="capa2" onClick="muestracapa('capa1')">Destino Outro Rexistro </OPTION>
			</SELECT> 

		  </TD>
	      </TR>
	      </TABLE>

          </TD>
      </TR>

      <TR>
          <TD>

	      <TABLE width="100%" cellspacing="0px" cellpadding="0px">
	      <TR>
		  <TD width="25%" class="etiqueta">Tipo remitente:
			<span class="etiqueta">*</span>
		  </TD>

		  <TD width="75%" valign="top">

			<SELECT NAME="cbTipoRemitente" class="inputCombo">
				<option></option>
				<OPTION VALUE="AA">AA Administración Autonómica </OPTION>
				<OPTION VALUE="AC">AC Administración Central </OPTION>
				<OPTION VALUE="AL">AL Administración Local </OPTION>
				<OPTION VALUE="AS">AS Asociacións </OPTION>
				<OPTION VALUE="CP">CP Colexios Profesionais </OPTION>
				<OPTION VALUE="EF">EF Entidades Financieras </OPTION>
				<OPTION VALUE="EP">EP Empresas Públicas </OPTION>
				<OPTION VALUE="OA">OA Organismos Autónomos </OPTION>
				<OPTION VALUE="OT">OT Outros </OPTION>
				<OPTION VALUE="PA" selected>PA Particular </OPTION>
				<OPTION VALUE="XU">XU Xulgados </OPTION>
			</SELECT> 

		  </TD>

	      </TR>
	      </TABLE>

          </TD>
      </TR>

      <TR>
          <TD>

	      <TABLE width="100%" border="0" cellspacing="0px" cellpadding="0px">
	      <TR>
		  <TD width="25%" class="etiqueta">Nº Transporte:</TD>
		  <TD width="30%">
			<INPUT TYPE="text" class="inputTexto" SIZE=4 MAXLENGTH=4 NAME="txtNumTransp" value="125/3">
		  </TD>
		  <TD width="20%" class="etiqueta"> Tipo transporte: </TD>
		  <TD width="25%">
			<SELECT NAME="cbTipoRemitente" class="inputCombo">
				<option></option>
				<OPTION VALUE="1" selected>Correo Postal </OPTION>
				<OPTION VALUE="2">Mensaxería </OPTION>
			</SELECT> 

		  </TD>
	      </TR>
	      </TABLE>

          </TD>
      </TR>

      <TR>
          <TD>

	      <TABLE width="100%" border="0" cellspacing="0px" cellpadding="0px">
	      <TR>
		  <TD width="25%" class="etiqueta"> Actuación: </TD>
		  <TD width="75%">
			<SELECT NAME="cbTipoRemitente" class="inputCombo">
				<option></option>
				<OPTION VALUE="1">Contratación Operarios 2002 </OPTION>
				<OPTION VALUE="2">Solicitud Curso Perfeccionamento Galego </OPTION>
			</SELECT> 

		  </TD>
	      </TR>
	      </TABLE>

          </TD>
      </TR>

      </TABLE>
      
   </div>
   
   
   
   <!-- CAPA 2: DESTINO 
   	------------------------------ -->

   <div class="tab-page">
     
      <h2 class="tab">Destino</h2>

      <TABLE id="tablaDestino" width="600px" cellspacing="7px" cellpadding="1px" border="0" >
      
      <TR>
        <TD>

	      <TABLE width="100%" cellspacing="0px" cellpadding="0px">
	      <TR>
		  <TD width="30%" class="etiqueta">Departamento:
		  	<span class="etiqueta">*</span>
		  </TD>
		  
		  <TD width="70%" class="columnP" valign="top">
			<SELECT NAME="cbDepartamento" class="inputCombo">
				<option></option>
				<OPTION VALUE="01" selected>Urbanismo</OPTION>
				<OPTION VALUE="02">Seguridad Ciudadana</OPTION>
				<OPTION VALUE="03">Bienestar y Cultura</OPTION>
			</SELECT> 
		  </TD>
	      </TR>
	      </TABLE>
	  
	</TD>
      </TR>

      <TR>
	<TD>
	
	      <TABLE width="100%" cellspacing="0px" cellpadding="0px">
	      <TR>
		  <TD width="30%" class="etiqueta">Procedemento:</TD>
		  <TD width="70%" class="columnP" valign="top">	    
			<SELECT NAME="cbProcedemento" class="inputCombo">
				<option></option>
				<OPTION VALUE="01" selected>Licencia de Obras Mayores</OPTION>
				<OPTION VALUE="02">Licencia de Apertura por Actividades Molestas</OPTION>
			</SELECT> 
		  </TD>
	      </TR>
	      </TABLE>
	  
	</TD>
      </TR>

      <TR>
	<TD>
	
	      <TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	      <TR>
		  <TD width="30%" class="etiqueta">Unidade Tramitadora:</TD>
		  <TD width="70%" class="columnP" valign="top">
			<SELECT NAME="cbTramitadoraDest" class="inputCombo">
				<option></option>
				<OPTION VALUE="01" selected>Planeamiento Urbanístico</OPTION>
				<OPTION VALUE="02">Gestión Urbanística</OPTION>
				<OPTION VALUE="03">Licencias Urbanísticas y de Apertura</OPTION>
			</SELECT> 		
		  </TD>
	      </TR>
	      </TABLE>	  

	</TD>
      </TR>	
      
      <TR>
	<TD>
	
	      <TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	      <TR>
		  <TD width="30%" class="etiqueta">Unidade de Rexistro:</TD>
		  <TD width="70%" class="columnP" valign="top">
			<SELECT NAME="cbUnidadeRexistro" class="inputCombo">
				<option></option>
				<OPTION VALUE="01" selected>Rexistro de Urbanismo</OPTION>
				<OPTION VALUE="02">Rexistro de Seguridade Ciudadana</OPTION>
				<OPTION VALUE="03">Rexistro de Bienestar </OPTION>
			</SELECT> 		
		  </TD>
	      </TR>
	      </TABLE>
	  
	</TD>
      </TR>
	
      <TR>
	<TD>
	      <TABLE width="100%" border="0" cellspacing="0px" cellpadding="0px">
	      <TR>
		  <TD width="30%" class="etiqueta">Expediente Relacionado:</TD>
		  <TD width="70%" class="columnP">
			<INPUT TYPE="text" class="inputTexto" SIZE=3 MAXLENGTH=4  NAME="txtExp1" value="2002"> /
			<INPUT TYPE="text" class="inputTexto" SIZE=5 NAME="txtExp2" value="78">
		  </TD>
	      </TR>
	      </TABLE>
	
	</TD>
      </TR>

      </TABLE>

   </div>


   <!-- CAPA 3: INTERESADO
   	------------------------------ -->

   <div class="tab-page">
      
      <h2 class="tab"> Interesado </h2>
      
      <TABLE  id="tablaInteresado" width="600px" cellspacing="7px" cellpadding="1px" border="0">
      <TR>
          <TD>

	      <TABLE width="100%" cellspacing="0px" cellpadding="0px" border="0">
	      <TR>
		  <TD width="30%" class="etiqueta">Tipo Documento:</TD>
		  <TD width="18%" class="columnP">
			<SELECT NAME="cbTipoDoc" class="inputCombo">
				<option></option>
				<OPTION VALUE="01" selected>D.N.I.</OPTION>
				<OPTION VALUE="02">C.I.F.</OPTION>
			</SELECT> 
		  </TD>
		  <TD width="20%" class="etiqueta">Documento:</TD>
		  <TD width="28%" class="columnP">
			<INPUT TYPE="text" class="inputTexto" SIZE=12 MAXLENGTH=12 NAME="txtDNI" value="123456789">
		  </TD>
		  <TD width="4%" class="columnP" align="right">
			<a href="#" onclick="buscarDocTipoDoc();">
			<span class="fa fa-search" aria-hidden="true"  onClick="" alt="Buscar"></span>
			</a>
		  </TD>
	      </TR>			
	      </TABLE>
			
          </TD>
      </TR>
		
      <TR>
          <TD>

	      <TABLE width="100%" cellspacing="0px" cellpadding="0px">
	      <TR>
		  <TD width="30%" class="etiqueta">Nombre/Razón Social:</TD>
		  <TD width="66%">
			<INPUT TYPE="text" class="inputTexto" SIZE=60 MAXLENGTH=11  NAME="txtInteresado" value="José Manuel">
		  </TD>
		  <TD width="4%" class="columnP" align="right">
			<a href="#" onclick="buscarDocRazonSocial();">
			<span class="fa fa-search" aria-hidden="true"  onClick="" alt="Buscar"></span>
			</a>
		  </TD>			
	      </TR>
	      </TABLE>
			
          </TD>
      </TR>


      <TR>
          <TD>

	      <TABLE width="100%" cellspacing="0px" cellpadding="0px">
	      <TR>
		  <TD width="30%" class="etiqueta">Apellidos</TD>
		  <TD width="18%" class="etiqueta">Partículas</TD>
		  <TD width="20%" class="etiqueta">Teléfono, Fax:</TD>
		  <TD width="32%" class="etiqueta">
			<INPUT TYPE="text" name="txtTelefono" class="inputTexto" SIZE=30 MAXLENGTH=20>
		  </TD>
	      </TR>
	      </TABLE>
			
          </TD>
      </TR>

      <TR>
          <TD>
	      <TABLE border="0" width="100%" cellspacing="0px" cellpadding="0px">
	      <TR>
		  <TD width="30%" class="columnP">
			<INPUT TYPE="text" name="txtApell1" class="inputTexto" SIZE=20 MAXLENGTH=25 value="Río">
		  </TD>
		  <TD width="18%" class="columnP">
			<INPUT TYPE="text" name="txtPart" class="inputTexto" SIZE=5 MAXLENGTH=5 value="del">
		  </TD>
		  <TD width="20%" class="etiqueta">e-mail:</TD>
		  <TD width="32%" class="etiqueta">
			<INPUT TYPE="text" name="txtCorreo" class="inputTexto" SIZE=30 MAXLENGTH=30 value="jmanuel@aaa.es">
		  </TD>			
	      </TR>
	      </TABLE>
			
          </TD>
      </TR>

      <TR>
          <TD>
          
	      <TABLE width="100%" cellspacing="0px" cellpadding="0px">
	      <TR>
		  <TD width="30%" class="etiqueta">
			<INPUT TYPE="text" name="txtApell2" class="inputTexto" SIZE=20 MAXLENGTH=20 value="López">
		  </TD>
		  <TD width="18%" class="etiqueta">
			<INPUT TYPE="text" name="txtPart2" class="inputTexto" SIZE=5 MAXLENGTH=5>
		  </TD>
		  <TD width="20%" class="etiqueta">Notas:</TD>
		  <TD width="32%" class="etiqueta">
			<INPUT TYPE="text" name="txtCorreo" class="inputTexto" SIZE=30 MAXLENGTH=30>
		  </TD>			
	      </TR>
	      </TABLE>
			
          </TD>
      </TR>

				
      <TR>
          <TD>
	      
	      <TABLE width="100%" cellspacing="0px" cellpadding="0px">
	      <TR>
		  <TD class="sub3titulo"><u>Domicilio</u></TD>
	      </TR>
	      </TABLE>
	      
          </TD>
      </TR>

      <TR>
          <TD>
          
	      <TABLE width="100%" border="0" cellspacing="1" cellpadding="0">
	      <TR>
		  <TD width="33%" class="etiqueta">País</TD>
		  <TD width="33%" class="etiqueta">Provincia </TD>
		  <TD width="34%" class="etiqueta">Municipio</TD>
	      </TR>
	      <TR>
		  <TD width="33%" class="columnP" valign="top">
			<SELECT NAME="cbPais" class="inputCombo">
				<OPTION VALUE="01" selected>España</OPTION>
				<OPTION VALUE="02">Portugal</OPTION>
			</SELECT> 
		  </TD>
		  <TD width="33%" class="columnP" valign="top">
			<SELECT NAME="cbProvincia" class="inputCombo">
				<OPTION VALUE="01" selected>A Coruña</OPTION>
				<OPTION VALUE="02">Lugo</OPTION>
				<OPTION VALUE="03">Ourense</OPTION>
				<OPTION VALUE="04">Pontevedra</OPTION>
			</SELECT> 
		  </TD>
		  <TD width="34%" class="columnP" valign="top">
			<SELECT NAME="cbMunicipio" class="inputCombo">
				<OPTION VALUE="01" selected>A Coruña</OPTION>
				<OPTION VALUE="02">Lousame</OPTION>
				<OPTION VALUE="03">Porto do Son </OPTION>
				<OPTION VALUE="04">Rianxo</OPTION>
			</SELECT> 
		  </TD>
	      </TR>
	      </TABLE>

          </TD>
      </TR>

      <TR>
          <TD>

	      <TABLE border="0" width="100%" cellspacing="1px" cellpadding="0px">
	      <TR>		      
      		  <TD width="33%" class="etiqueta">Código Vía</TD>
      		  <TD width="33%" class="etiqueta">Tipo Vía</TD>
      		  <TD width="34%" class="etiqueta">Uso/Relación</TD>
      	      </TR>
	      <TR>
		  <TD width="33%" class="columnP" valign="top	">
		  	<INPUT TYPE="text" class="inputTexto" SIZE=5 MAXLENGTH=5  NAME="txtCodVia">
		  </TD>
		  <TD width="33%" class="columnP">
			<SELECT NAME="cbVia" class="inputCombo">
				<option></option>
				<OPTION VALUE="01" selected>AV Avenida</OPTION>
				<OPTION VALUE="02">CL Calle</OPTION>
				<OPTION VALUE="03">PZ Plaza</OPTION>
			</SELECT> 
		  </td>
		  <td width="34%" class="etiqueta">
			<SELECT NAME="cbVia" class="inputCombo">
				<option></option>
				<OPTION VALUE="01" selected>Vivienda Principal</OPTION>
				<OPTION VALUE="02">Vivienda Secundaria</OPTION>
				<OPTION VALUE="03">Lugar de Trabajo</OPTION>			
		  </td>
	      </TR>
	      </TABLE>
          </TD>
      </TR>

      <tr>
          <td>
	      
	      <TABLE border="0" width="100%" cellspacing="0px" cellpadding="0px">
	      <tr>
		  <TD width="20%" class="etiqueta">Nome Vía:</TD>
		  <td width="46%" class="columnP">
			<INPUT TYPE="text" class="inputTexto" SIZE=40 MAXLENGTH=40 NAME="txtNomeVia" value="Real">
		  </td>
		  <td width="17%" class="etiqueta">Cód.Postal:</td>
		  <td width="17%" class="columnP">
		  	<INPUT TYPE="text" class="inputTexto" SIZE=5 MAXLENGTH=5 value="15001">
		  </td>
	      </tr>
	      </table>
	      
          </TD>
      </TR>

      <TR>
          <TD>
      
	      <TABLE width="100%" cellspacing="0px" cellpadding="0px">
	      <TR>
		  <TD width="20%" class="etiqueta">Lugar, Barrio... :</TD>
		  <TD width="80%" class="columnP">
			<INPUT TYPE="text" class="inputTexto" SIZE=40 MAXLENGTH=40>
		  </TD>
	      </TR>
	      </TABLE>
			
          </TD>
      </TR>

      <TR>
          <TD>
          
	      <TABLE width="100%" cellspacing="1px" cellpadding="0px">
	      <TR>
		  <TD width="20%" class="etiqueta">Num.-Letra(D)</TD>
		  <TD width="20%" class="etiqueta">Num.-Letra(H)</TD>
		  <TD width="10%" class="etiqueta">Bloque</TD>
		  <td width="10%" class="etiqueta">Portal</td>
		  <td width="10%" class="etiqueta">Esc.</td>
		  <td width="10%" class="etiqueta">Plta.</td>
		  <td width="10%" class="etiqueta">Km.</td>
		  <td width="10%" class="etiqueta">Hm.</td>

	      </TR>			
	      <TR>
		  <TD width="20%" class="columnP">	
			<INPUT TYPE="text" class="inputTexto" SIZE=2 MAXLENGTH=3> -		
			<INPUT TYPE="text" class="inputTexto" SIZE=2 MAXLENGTH=3>
			
		  </TD>
		  <TD width="20%" class="columnP">
			<INPUT TYPE="text" class="inputTexto" SIZE=2 MAXLENGTH=3> -
			<INPUT TYPE="text" class="inputTexto" SIZE=2 MAXLENGTH=3>
		  </TD>
		  <TD width="10%" class="columnP">
		  	<INPUT TYPE="text" class="inputTexto" name="txtBloque" SIZE=2 MAXLENGTH=3>
		  </TD>			
		  <TD width="10%" class="columnP">
		  	<INPUT TYPE="text" class="inputTexto" name="txtPortal" SIZE=2 MAXLENGTH=3 value="10">
		  </TD>
		  <TD width="10%" class="columnP">
		  	<INPUT TYPE="text" class="inputTexto" name="txtEsc" SIZE=2 MAXLENGTH=3>
		  </TD>
		  <TD width="10%" class="columnP">
		  	<INPUT TYPE="text" class="inputTexto" name="txtPlta" SIZE=2 MAXLENGTH=3 value="7">
		  </TD>
		  <TD width="10%" class="columnP">
		  	<INPUT TYPE="text" class="inputTexto" name="txtKm" SIZE=2 MAXLENGTH=3>
		  </TD>
		  <TD width="10%" class="columnP">
		  	<INPUT TYPE="text" class="inputTexto" name="txtHm" SIZE=2 MAXLENGTH=3>
		  </TD>
	      </TR>
	      </TABLE>
		
          </TD>		
      </TR>
      </table>

   </div>


   <!-- CAPA 4: DEPARTAMENTO ORIXE
   	------------------------------ -->

   <div class="tab-page">
      
      <h2 class="tab"> Departamento Orixe </h2>

      <TABLE ID="tablaDptoOrixe" width="600px" cellspacing="7px" cellpadding="1px" border="0">

      <TR>
          <TD>
          
	      <TABLE width="100%" cellspacing="0px" cellpadding="0px">
	      <TR>
		  <TD width="30%" class="etiqueta">Departamento:</TD>
		  <TD width="70%" class="columnP" valign="top">
			<SELECT NAME="cbDepartamento" class="inputCombo" disabled>
				<option></option>
				<OPTION VALUE="01">Urbanismo</OPTION>
				<OPTION VALUE="02">Seguridad Ciudadana</OPTION>
				<OPTION VALUE="03">Bienestar y Cultura</OPTION>
			</SELECT> 
		  </TD>
	      </TR>
	      </TABLE>
	      
          </TD>
      </TR>

      <TR>
          <TD>

	      <TABLE width="100%" cellspacing="0px" cellpadding="0px">
	      <TR>
		  <TD width="30%" class="etiqueta">Unidade de Rexistro:</TD>
		  <TD width="70%" class="columnP" valign="top">
			<SELECT NAME="cbTramitadora" class="inputCombo" disabled>
				<option></option>
				<OPTION VALUE="01">Planeamiento Urbanístico</OPTION>
				<OPTION VALUE="02">Gestión Urbanística</OPTION>
				<OPTION VALUE="03">Licencias Urbanísticas y de Apertura</OPTION>
			</SELECT>  
		  </TD>
	      </TR>
	      </TABLE>
	      
	  </TD>
      </TR>

      <TR>
          <TD>

	      <TABLE width="100%" border="0" cellspacing="0px" cellpadding="0px">
	      <TR>
		  <TD width="30%" class="etiqueta">Entrada Relacionada:</TD>
		  <TD width="70%" class="columnP">
			<INPUT TYPE="text" class="inputTexto" SIZE=3 MAXLENGTH=3 NAME="txtExp1" disabled value="2002"> / 
			<INPUT TYPE="text" class="inputTexto" SIZE=5 MAXLENGTH=4 NAME="txtExp2" disabled value="44">
		  </TD>
	      </TR>
	      </TABLE>
		
          </TD>
      </TR>
      
      </TABLE>
      
   </div>   

</div>


</center>

<script type="type/javascript"> setupAllTabs(); </script>

<TABLE width="600px">
<TR>
  <TD class="columnP">
	<BR> <!-- Línea en blanco -->  
  </TD>
</TR>

<TR>
    <TD width="100%" align="center" >
    	<TABLE class="tablaBotones" width="45%">
    	<TR>
  		<TD width="33%" align="center">
  		    <input type= "button" class="boton" value="Buzón" name="cmdBuzon" 
              		onClick = "pulsarBuzon();return false;">
  		</TD>

  		<TD width="33%" align="center">
  		    <input type= "button" class="boton" value="Rexistrar" name="cmdRegistrar" 
  		            onClick = "pulsarRegistrar();return false;">
  		</TD>
              
  		<TD width="33%" align="center">
  		    <input type="button" class="boton" value="Cancelar" name="cmdCancelar" 
             		onClick="pulsarCancelar();return false;">
             	</TD>
        </TR>
        </TABLE>
    </TD>
</TR>

<tr>
  <td width="100%" align="left">
    <span class="etiqueta">(*) </span>
    <span class="columnP"> Obrigatorio</span>
  </td>
</tr>
</table>

</CENTER>

</FORM>

<SCRIPT> habilitarImagenCal("calDesde",true); </SCRIPT>

 



</BODY>

</HTML>
