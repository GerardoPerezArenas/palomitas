    <%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="java.util.Vector"%>

<%@ page contentType="text/html;charset=ISO_8859-1"%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
  <title> Mantenimiento de Organismos Externos </title>
  <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
	
<%
        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        ParametrosTerceroValueObject ptVO = null;
        int idioma=1;
        int apl=3;
        String css="";
        if (session.getAttribute("usuario") != null){
            usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        idioma = usuarioVO.getIdioma();
        apl = usuarioVO.getAppCod();
        css=usuarioVO.getCss();
        }
%>


	<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
	<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
	<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

  <!-- Ficheros JavaScript -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
	<script type="text/javascript">

		/**************  VARIABLES GLOBALES ***********************/
		<% 
      MantenimientosTercerosForm mantForm =(MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");
    %>  
		var ventana = "<%=mantForm.getVentana()%>";
		//Vectores para albergar los datos de la lista de Organismos Externos, tabla (Dinámicos)
		var listaOrganismosExternos = new Array();
		//Variable para direccionamiento al frame
  	var frame;
		//Vectores para el Almacenado de Objetos y datos
		var vectorCamposRejilla = ["codigo","descripcion"];
		//Vector de Valores de Limpieza para pasarlo a la función rellenar, es un vector donde cada posicion es un
		//vector que contiene: En la posicion 0 el valor que se asignará al campo en la función de limpieza, si el
		//campo es un checkbox, además, contendrá las posiciones 2 y 3 que contendrá los valores que se asignarán 
		//cuando no esté chequeado (pos 1) ó cuando lo esté (pos 2)
		var vectorValoresResetRejilla = [[""],[""]];															 
		var vectorBotones = new Array();
    var vectorCamposRejilla1 = new Array();
		var vectorAnchorsRejilla = new Array();
    var vectorBotonesRejilla = new Array();
			
		/**************  FUNCIONES DE CARGA E INICIALIZACION DE DATOS ***********************/
		function recuperaDatosIniciales()
		{
    <%	
			Vector listaOrganismosExternos = mantForm.getListaOrganismosExternos();
      int i = 0;
    %>
      var j=0;
    <%for(i=0;i<listaOrganismosExternos.size();i++)
		  {
        GeneralValueObject organismoExterno = (GeneralValueObject)listaOrganismosExternos.elementAt(i);				
			%>
				listaOrganismosExternos[j] = ["<%=(String)organismoExterno.getAtributo("codigo")%>",
					 "<%=(String)organismoExterno.getAtributo("descripcion")%>"];
				j++;
    <%}%>
   	}//de la funcion


		function redireccionaFrame()
		{
		  frame=(ventana=="true")?top1.mainFrame1:top.mainFrame;	
		}//de la funcion
		

		function inicializar()
		{
			window.focus();
			recuperaDatosIniciales();
			redireccionaFrame();
			//Vectores para los campos, imágenes y enlaces de la REJILLA
			vectorCamposRejilla1 = [document.forms[0].codigo,document.forms[0].descripcion];
			vectorAnchorsRejilla = [];
			vectorBotonesRejilla = [];
			//Vector de Botones para la realización de las OPERACIONES con la Ent Singular
			vectorBotones = [document.forms[0].botonAlta,document.forms[0].botonModificar,
				document.forms[0].botonBorrar,document.forms[0].botonLimpiar];
			//Inicializo el Formulario
			inicializarFormulario();
			//Cargamos la lista de Documentos en la Tabla
			cargarListaOrganismosExternos(listaOrganismosExternos);			
      if(ventana=="false"){
				pleaseWait1("off",frame);
				
					 
  				
			}else{
				pleaseWait1("off",frame);
        var parametros = self.parent.opener.xanelaAuxiliarArgs;
        pulsarBuscar(); 
      } 
		}//de la funcion
		
		
		function inicializarFormulario()
		{
			limpiarFormulario("todo");
		}//de la funcion


		/**************  FUNCIONES PARA EL RELLENADO DE CAMPOS ***********************/
		function rellenar(vectorCampos,vectorDatos)
		{
			var longDatos = vectorDatos.length;
			var longCampos = vectorCampos.length;
			
			if(longDatos==longCampos)
			{
				for(i=0;i<longCampos;i++)
				{
					var campo =	eval("document.forms[0]."+vectorCampos[i]);
					var tipoCampo = campo.type;
					var vectorValorCampo = vectorDatos[i];//vector de valores es un vector que puede tener longitud 1 ó 3
					var valorCampo=vectorValorCampo[0];
					//Asignamos el valor al campo, que es la posicion 0 del vector vectorValorCampo, sólo si es campo de texto,
					//si es checkbox no puesto que el valor ya lo tiene asignado estáticamente en la definición del campo	
					if (tipoCampo.toLowerCase() == "text")
					{
						campo.value=valorCampo;
					}
					else if (tipoCampo.toLowerCase() == "checkbox") //Es un checkbox
					{
						if (vectorValorCampo.length==3)
						{
							var checkMIN=vectorValorCampo[1];
							var checkMAX=vectorValorCampo[2];
							if (valorCampo==checkMIN)
								campo.checked=false;
							else if (valorCampo==checkMAX)
								campo.checked=true;
							else
							{
								alert("Hay un campo de texto cuyo valor no se corresponde con los valores asociados al checked y no checked");													
								campo.checked=false;
							}//del if
						}
						else
							alert("Hay un campo checkbox y la longitud de su vector tipo es distinto de 3");						
					}//del if						
				}//del for
			}
			else
				alert("La longitud del vector de nombres de Campos no se corresponde con la del de Datos");
		}//de la funcion


		/**************  FUNCIONES DE LIMPIEZA, HABILITACION Y DESHABILITACION DE CAMPOS ***********************/
		function limpiarFormulario(opcion_limpieza)
		{
			if (opcion_limpieza=="todo") //limpiamos todo el formulario
			{
				rellenar(vectorCamposRejilla,vectorValoresResetRejilla);
				tablaOrganismosExternos.lineas = new Array();
				refresca(tablaOrganismosExternos);
			}
			else if (opcion_limpieza=="tabla") //limpiamos sólo la tabla
			{
				tablaOrganismosExternos.lineas = new Array();
				refresca(tablaOrganismosExternos);
			}
			else if (opcion_limpieza=="campos_rejilla") //limpiamos solo los campos de la rejilla
			{
				rellenar(vectorCamposRejilla,vectorValoresResetRejilla);
			}//del if
			else if (opcion_limpieza=="rejilla/tabla") //limpiamos los campos de la rejilla y la tabla
			{
				tablaOrganismosExternos.lineas = new Array();
				refresca(tablaOrganismosExternos);
				rellenar(vectorCamposRejilla,vectorValoresResetRejilla);
			}
		}//de la funcion
						
		
		/**************  FUNCIONES DE VALIDACION DE CAMPOS ***********************/
		function validarCamposRejilla()
		{
			//Insertamos en un array los valores de los campos obligatorios, pero sólo los de los campos de texto
			//puesto que los checkbox siempre tendrán un valor, el valor que se le ha puesto en el VALUE si está
			//activado ó null si no lo está
			var valoresCamposRejillaObligatorios = [document.forms[0].codigo.value];
			var validacionCorrecta=true;
			for(var i=0;i<valoresCamposRejillaObligatorios.length;i++)
			{
				if (valoresCamposRejillaObligatorios[i]=="")
					return false;
			}//del for
			return true;
		}//de la funcion

    function noEsta(indice)
		{
      var cod = document.forms[0].codigo.value;
      for(i=0;(i<listaOrganismosExternos.length);i++){
        if(i!=indice){
          if((listaOrganismosExternos[i][0]) == cod)
            return false;
        }
      }
      return true;
    }//de la funcion

    function filaSeleccionada(tabla)
		{
      var i = tabla.selectedIndex;
      if((i>=0)&&(!tabla.ultimoTable))
          return true;
      return false;
    }//de la funcion

    
		/**************  FUNCIONES DE CARGA DE DATOS DINAMICA ***********************/
		function cargarListaOrganismosExternos(lista)
		{
      tablaOrganismosExternos.lineas = lista;
      refresca(tablaOrganismosExternos);
			//Oculto la imagen de carga de datos de la pantalla
			pleaseWait1("off",frame);
			
    }//de la funcion

		
		/**************  FUNCIONES DE PULSACION DE BOTONES ***********************/
		function pulsarAlta()
		{
			if(validarCamposRejilla()) //Si los campos de la rejilla son correctos (los obligatorios)
			{
        if(noEsta()) //Si el código está en la lista
				{
          document.forms[0].opcion.value="alta";
          document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
          document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/OrganismosExternos.do";
          document.forms[0].submit();
          limpiarFormulario("rejilla/tabla");
        }
				else
          jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>"); 
			}
			else
				jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
		}//de la funcion


    function pulsarModificar()
		{
			if(filaSeleccionada(tablaOrganismosExternos)) //Si  ha seleccionado alguna fila
			{
				if(validarCamposRejilla()) //Si los campos de la rejilla son correctos (los obligatorios)
				{
	        if(noEsta(tablaOrganismosExternos.selectedIndex)) //Si el código está en la lista
					{
	          document.forms[0].opcion.value="modificar";
	          document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
	          document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/OrganismosExternos.do";
	          document.forms[0].submit();
	          limpiarFormulario("rejilla/tabla");
	        }
					else
	          jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>"); 
				}
				else
					jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
      }
			else
        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");					
		}//de la funcion


    function pulsarBorrar()
		{
			if(filaSeleccionada(tablaOrganismosExternos)) //Si  ha seleccionado alguna fila
			{
				if(validarCamposRejilla()) //Si los campos de la rejilla son correctos (los obligatorios)
				{
	        if(noEsta(tablaOrganismosExternos.selectedIndex)) //Si el código está en la lista
					{
						document.forms[0].opcion.value="eliminar";
		        document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
	          document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/OrganismosExternos.do";
		        document.forms[0].submit();
		        limpiarFormulario("rejilla/tabla");
	        }
					else
	          jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>"); 
				}
				else
					jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
      }
			else
        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");					
		}//de la funcion

		
		function pulsarLimpiar()
		{
			limpiarFormulario("campos_rejilla");
		}//de la funcion
		
		function pulsarSalir()
		{
			if(ventana=="false"){
				document.forms[0].opcion.value="inicializarTerc";
		        document.forms[0].target="mainFrame";
		        document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
		        document.forms[0].submit();
			}else{
				var datosRetorno;
				if(indice>-1)
                                                                            datosRetorno = listaOrganismosExternos[indice];
				self.parent.opener.retornoXanelaAuxiliar(datosRetorno);
			}
		}//de la funcion

		
  </script>
</head>

<body class="bandaBody" onLoad="inicializar();">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
	
<form action="" method="get" name="formulario" target="_self">
<input type="hidden" name="opcion">
<input type="hidden" name="target1"  value="">
<input type="hidden" name="ventana" value="<%=mantForm.getVentana()%>">
<input type="hidden" name="codigoAntiguo" value="">

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_ManOrgmsExt")%></div>
<div class="contenidoPantalla">
    <table width="100%">
        <tr>
            <td> 
                <div id="tablaOrganismosExternos" ></div>
            </td> 
        </tr> 
        <tr>
            <td> 
                <input name="codigo" type="text" class="inputTextoObligatorio" size=5 maxlength=2 
                style="width: 13%" onKeyPress = "javascript:PasaAMayusculas(event);">	
                <input name="descripcion" type="text" class="inputTexto" size=100 maxlength=60
                style="width: 86%" onKeyPress = "javascript:PasaAMayusculas(event);">
            </td>
        </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%>
               name="cmdAlta" onClick="pulsarAlta();" accesskey="A"> 
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%>
               name="cmdModificar" onClick="pulsarModificar();" accesskey="M">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%>
               name="cmdEliminar" onClick="pulsarEliminar();" accesskey="E">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%>
               name="cmdLimpiar" onClick="limpiar();" accesskey="L">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%>
               name="cmdSalir" onClick="pulsarSalir();" accesskey="S"> 
    </div>            				            
</div>            				            
</form>
     

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaCombo.js"></script>
   <script type="text/javascript">
  var indice;
  var tablaOrganismosExternos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaOrganismosExternos"));
  tablaOrganismosExternos.addColumna("120","center",'<%= descriptor.getDescripcion("gEtiq_Codigo")%>');
  tablaOrganismosExternos.addColumna("780","left",'<%= descriptor.getDescripcion("gEtiq_Descrip")%>');
  tablaOrganismosExternos.displayCabecera=true;
  tablaOrganismosExternos.displayTabla();
  
  
  function refresca(tabla)
  {
    tabla.displayTabla();
  }//de la funcion
	

	function rellenarDatos(tableName,rowID)
	{
    if(tablaOrganismosExternos==tableName)
		{
			if(filaSeleccionada(tablaOrganismosExternos)) //Si  ha seleccionado alguna fila
			{
	      var i=rowID;
	      indice = rowID;	    			
	      if(i>=0)
				{
					var vectorDatosRejilla = [[listaOrganismosExternos[i][0]],[listaOrganismosExternos[i][1]]];
					//Nos quedamos con el código antiguo por si se desea modificar
					document.forms[0].codigoAntiguo.value=listaOrganismosExternos[i][0];
	        rellenar(vectorCamposRejilla,vectorDatosRejilla);
				}
			}  
    }
	} //de la funcion

	// FUNCION DE CONTROL DE TECLAS
	document.onmouseup = checkKeys; 

	function checkKeysLocal(evento,tecla)
	{
         var teclaAuxiliar = "";
        if(window.event){
            evento         = window.event;
            teclaAuxiliar =  evento.keyCode;
        }else
            teclaAuxiliar =  evento.which;

		//** Esta funcion se debe implementar en cada JSP para particularizar  **//
		//** las acciones a realizar de las distintas combinaciones de teclas  **//
		if((layerVisible)||(divSegundoPlano)) buscar();
			keyDel(evento);
			if(teclaAuxiliar == 9){
				if(layerVisible) ocultarDiv();
				if(divSegundoPlano) divSegundoPlano = false;
			}
			if(teclaAuxiliar == 40){
				if((layerVisible)||(divSegundoPlano)) upDown(teclaAuxiliar);
				else upDownTable(tablaOrganismosExternos,listaOrganismosExternos,teclaAuxiliar);
			}
			if(teclaAuxiliar == 38){
				if((layerVisible)||(divSegundoPlano)) upDown(teclaAuxiliar);
				else upDownTable(tablaOrganismosExternos,listaOrganismosExternos,teclaAuxiliar);
			}  
		if (event.button == 1){  
				if(layerVisible) setTimeout("ocultarDiv()",50);
			if(capaVisible) ocultarLista();
					if(divSegundoPlano) divSegundoPlano = false;      
			}	
	}//de la funcion
	
	
</script>
</body>
</html>
