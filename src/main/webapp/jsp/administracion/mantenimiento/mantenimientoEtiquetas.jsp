<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@ page contentType="text/html;charset=ISO_8859-1"%>
<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
  <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
  <title> Mantenimiento de Etiquetas </title>
  <!-- Estilos -->
<%
	UsuarioValueObject usuarioVO = new UsuarioValueObject();
  int idioma=1;
  int apl=3;
  if (session.getAttribute("usuario") != null){
		usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    idioma = usuarioVO.getIdioma();
    apl = usuarioVO.getAppCod();
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
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
	<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
	<script type="text/javascript">

		/**************  VARIABLES GLOBALES ***********************/
		<% 
      MantenimientosAdminForm mantForm =(MantenimientosAdminForm)session.getAttribute("MantenimientosAdminForm");
    %>  
		var ventana = '<%=mantForm.getVentana()%>';
		//Vectores para albergar los datos de las Aplicaciones, código y descripcion
		var codigosAplic = new Array();
		var nombresAplic = new Array();
		//Vectores para albergar los datos de las Tablas de la BD, código y descripcion
		var codigosTablas = new Array();
		var comentariosTablas = new Array();
		//Vectores para albergar los datos de los Campos de las Tablas de la BD, código y descripcion
		var codigosCampos = new Array();
		var comentariosCampos = new Array();
		//Vector para albergar la estructura de tablas y atributos inicial (pasar de la jsp a javascript)
		//contiene en cada posicion:
		//  1. El nombre de la tabla.
		//  2. Su comentario.
		//  3. Un vector con la información de los campos asociados a la tabla con la siguiente información:
		//      Para cada posición del vector:
		//      1. El nombre del campo.
		//      2. El comentario del campo
		var listaTablasAtributos = new Array();
		//Vectores para albergar los datos de la lista de Etiquetas, tabla (Dinámicos)
		var listaEtiquetas = new Array();
		var listaTablaEtiquetas = new Array();
		//Variable para direccionamiento al frame
  	var frame;
		//Vectores para el Almacenado Campos, combos y Reset de Rejilla
		var vectorCamposRejillaTodos = ['codEtiq','nomEtiq','descEtiq','codAplic','descAplic','codTabla','descTabla','codCampo','descCampo'];
		var vectorCamposRejilla = ['codEtiq','nomEtiq','descEtiq'];
		var vectorCombosRejilla = ['comboAplicaciones','comboTablas','comboCampos'];		
		//Vector de Valores de Limpieza para pasarlo a la función rellenar, es un vector donde cada posicion es un
		//vector que contiene: En la posicion 0 el valor que se asignará al campo en la función de limpieza, si el
		//campo es un checkbox, además, contendrá las posiciones 2 y 3 que contendrá los valores que se asignarán 
		//cuando no esté chequeado (pos 1) ó cuando lo esté (pos 2)
		var vectorValoresResetRejilla = [[''],[''],[''],[''],[''],[''],[''],[''],['']];															 
		//Vector de Botones para la realización de las OPERACIONES con la Ent Singular
		var vectorBotones = ['botonAlta','botonModificar','botonBorrar','botonLimpiar'];
		//Definimos los combos
		var comboAplicaciones;
		var comboTablas;
		var comboCampos;
			
 

		/**************  FUNCIONES DE CARGA E INICIALIZACION DE DATOS ***********************/
		function recuperaDatosIniciales()
		{
			<%	
				Vector listaEtiquetas = mantForm.getListaEtiquetas();
				Vector listaAplicaciones = mantForm.getListaAplicaciones();
				Vector listaTablasAtributos = mantForm.getListaTablasAtributos();
			%>
				//Pasamos a los vectores JavaScript el vector de Etiquetas de Jsp
				var j=0;
				var m=0;
			<%for(int i=0;i<listaEtiquetas.size();i++)
				{
					GeneralValueObject etiqueta = (GeneralValueObject)listaEtiquetas.elementAt(i);
			%>
				var nombreTabla='<%=(String)etiqueta.getAtributo("codTabla")%>';
				if (nombreTabla!="")
				{
					listaEtiquetas[m] = ['<%=(String)etiqueta.getAtributo("codEtiq")%>',//0
						'<%=(String)etiqueta.getAtributo("nomEtiq")%>',//1
						'<%=(String)etiqueta.getAtributo("descEtiq")%>',//2
						'<%=(String)etiqueta.getAtributo("codAplic")%>',//3
						'<%=(String)etiqueta.getAtributo("descAplic")%>',//4
						'<%=(String)etiqueta.getAtributo("codTabla")%>',//5
						'<%=(String)etiqueta.getAtributo("descTabla")%>',//6
						'<%=(String)etiqueta.getAtributo("codCampo")%>',//7
						'<%=(String)etiqueta.getAtributo("descCampo")%>'];//8
					listaTablaEtiquetas[m] = ['<%=(String)etiqueta.getAtributo("codEtiq")%>',//0
						'<%=(String)etiqueta.getAtributo("nomEtiq")%>',//1
						'<%=(String)etiqueta.getAtributo("descEtiq")%>',//2
						'<%=(String)etiqueta.getAtributo("codAplic")%>',//3
						'<%=(String)etiqueta.getAtributo("codTabla")%>',//5
						'<%=(String)etiqueta.getAtributo("codCampo")%>'];//7
						m++;
					}
					j++;
			<%}%>
	
				//Pasamos a los vectores JavaScript el vector de Aplicaciones de Jsp
				var j=0;
			<%for(int i=0;i<listaAplicaciones.size();i++)
				{
					GeneralValueObject aplicacion = (GeneralValueObject)listaAplicaciones.elementAt(i);
			%>
					codigosAplic[j] = '<%=(String)aplicacion.getAtributo("codigo")%>';
					nombresAplic[j] = '<%=(String)aplicacion.getAtributo("descripcion")%>';
					j++;
			<%}%>
	
				//Pasamos a los vectores JavaScript el vector de Tablas y Atributos de Jsp y al array que contiene los datos
				//de la estructura tablas y atributos de la variable listaTablasAtributos jsp
				var j=0;
				var n=0;
			<%for(int i=0;i<listaTablasAtributos.size();i++)
				{
					//Cogemos el elemento i del vector que contendrá la información de la tabla tal como
					//nombre y comentario y tendrá también el vector de campos de dicha tabla
					GeneralValueObject tabla = (GeneralValueObject)listaTablasAtributos.elementAt(i);
					//Referenciamos al vector de campos de la tabla anterior
					Vector vectorCampos = (Vector)tabla.getAtributo("vectorCampos");
			%>
					//Almacenamos en los vectores de las tablas javascript los datos de la tabla
					var codTabla='<%=(String)tabla.getAtributo("nombreTabla")%>';
					var comentarioTabla='<%=(String)tabla.getAtributo("comentarioTabla")%>';
					if (codTabla!="")
					{
						codigosTablas[n] = codTabla;
						comentariosTablas[n] = comentarioTabla;
						//Ahora recorremos el vector de campos para obtener los datos e insertarlos en los vectores de campos
						//para javascript
						var k=0;
						var vectorCampos = new Array();
						<%for(int l=0;l<vectorCampos.size();l++)
							{
								//Comemos el elemento l del vector de campos que es un GeneralValueObject con los datos
								//nombre y comentario del campo
								GeneralValueObject campo = (GeneralValueObject)vectorCampos.elementAt(l);
							%>
								//Añadimos los datos a los vectores que albergarán los códigos y comentarios para el combo
								var codigoCampo='<%=(String)campo.getAtributo("nombreCampo")%>';
								var comentarioCampo='<%=(String)campo.getAtributo("comentarioCampo")%>';
								//Vamos añadiendo los campos al vector javascript
								vectorCampos[k] = [codigoCampo,comentarioCampo];
								k++;
						<%}%>					
						//En este momento tenemos el vector javascript de campos completo, ahora podemos añadir el vector
						//listaTablasAtributos los datos
						listaTablasAtributos[n] = [codigosTablas[j],comentariosTablas[j],vectorCampos];
						n++;
					}//del if
					j++;
			<%}%>
   	}//de la funcion


		function redireccionaFrame()
		{
		  //var ventana = '<%=mantForm.getVentana()%>';
		  frame=(ventana=="true")?top1.mainFrame1:top.mainFrame;	
		}//de la funcion
		

		function inicializar()
		{
			window.focus();
			recuperaDatosIniciales();
			redireccionaFrame();
			cargaCombos();
			//Inicializo el Formulario
			inicializarFormulario();
			//Cargamos la lista de Etiquetas en la Tabla
			cargarListaEtiquetas(listaTablaEtiquetas);			
      if(ventana=="false"){
				pleaseWait1('off',frame);
			}else{
				pleaseWait1('off',frame);
        var parametros = self.parent.opener.xanelaAuxiliarArgs;
        pulsarBuscar(); 
      } 
		}//de la funcion
		
		
		function inicializarFormulario()
		{
			limpiarFormulario('todo');
			//Habilitamos la rejilla
      habilitarGeneralInputs(vectorCamposRejilla,true);
			//Habilitamos los Combos de la Rejilla
			habilitarGeneralCombos(vectorCombosRejilla,true);			
			//Habilitamos los Botones
			habilitarGeneralInputs(vectorBotones,true);
		}//de la funcion

		function cargaCombos()
		{
			comboAplicaciones.addItems(codigosAplic,nombresAplic);
			comboTablas.addItems(codigosTablas,comentariosTablas);
			comboCampos.addItems(codigosCampos,comentariosCampos);
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
					if (tipoCampo.toLowerCase() == 'text')
					{
						campo.value=valorCampo;
					}
					else if (tipoCampo.toLowerCase() == 'checkbox') //Es un checkbox
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
			if (opcion_limpieza=='todo') //limpiamos todo el formulario
			{
				rellenar(vectorCamposRejillaTodos,vectorValoresResetRejilla);
				tablaEtiquetas.lineas = new Array();
				refresca(tablaEtiquetas);
			}
			else if (opcion_limpieza=='tabla') //limpiamos sólo la tabla
			{
				tablaEtiquetas.lineas = new Array();
				refresca(tablaEtiquetas);
			}
			else if (opcion_limpieza=='campos_rejilla') //limpiamos solo los campos de la rejilla
			{
				rellenar(vectorCamposRejillaTodos,vectorValoresResetRejilla);
			}//del if
			else if (opcion_limpieza=='rejilla/tabla') //limpiamos los campos de la rejilla y la tabla
			{
				tablaEtiquetas.lineas = new Array();
				refresca(tablaEtiquetas);
				rellenar(vectorCamposRejillaTodos,vectorValoresResetRejilla);
			}
		}//de la funcion
						
		
		/**************  FUNCIONES DE VALIDACION DE CAMPOS ***********************/
		function validarCamposRejilla()
		{
			//Insertamos en un array los valores de los campos obligatorios, pero sólo los de los campos de texto
			//puesto que los checkbox siempre tendrán un valor, el valor que se le ha puesto en el VALUE si está
			//activado ó null si no lo está
			var valoresCamposRejillaObligatorios = [document.forms[0].codEtiq.value,
				document.forms[0].descEtiq.value,document.forms[0].codAplic.value,document.forms[0].descAplic.value,
				document.forms[0].codTabla.value,document.forms[0].codCampo.value];
			var validacionCorrecta=true;
			for(var i=0;i<valoresCamposRejillaObligatorios.length;i++)
			{
				if (valoresCamposRejillaObligatorios[i]=='')
					return false;
			}//del for
			return true;
		}//de la funcion

    function noEsta(indice)
		{
      var cod = document.forms[0].codEtiq.value;
      for(i=0;(i<listaEtiquetas.length);i++){
        if(i!=indice){
          if((listaEtiquetas[i][0]) == cod)
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
    
		/**************  FUNCIONES PARA UTILIZACIÓN EN LOS COMBOS DEPENDIENTES UNOS DE OTROS ***********************/

		//Función util para los combos desplegables cuando 2 combos son dependientes, es decir, cuando se
		//carga el contenido del 2º combo en función del valor del 1º.
		//Para que no saque el mensaje de código inexistente cuando cambie el valor del 1º y busque si el 2º depende
		//del primero se pasas los nombres de los campos tanto del código como de la descripción del primer y segundo combo.
		function onblurCombo(codCampoOrigen, desCampoOrigen,codCampoDestino,desCampoDestino)
		{	
			if(camposDistintosDiv(codCampoOrigen, desCampoOrigen))
			{
				var vectorCampos = [codCampoDestino,desCampoDestino];
				var vectorDatos  = [[''],['']];			
				rellenar(vectorCampos,vectorDatos);
			}//del if
		}//de la funcion


		/**************  FUNCIONES DE CARGA DE DATOS DINAMICA ***********************/
		function cargarListaEtiquetas(lista)
		{
      tablaEtiquetas.lineas = lista;
      refresca(tablaEtiquetas);
			//Oculto la imagen de carga de datos de la pantalla
			pleaseWait1('off',frame);
			
    }//de la funcion

		//Carga el Combo de Campos dependiendo del valor del combo de Tablas
		function cargarComboCampos()
		{
				//Cargamos los vectores de Campos, codigosCampos y comentariosCampos a partir de los datos
				//que hay en la estructura de tablas y sus atributos (listaTablasAtributos) en javascript
				var tablaABuscar=document.forms[0].codTabla.value;
				//Cogemos la tabla que vamos a buscar para insertar en el combo de campos los Campos asociados
				//a dicha tabla
				if (tablaABuscar=="")
					tablaABuscar=codigosTablas[0];
				var j=0;
				var tablaEncontrada=false;
				codigosCampos=new Array();
				comentariosCampos=new Array();
				while(tablaEncontrada==false && j<listaTablasAtributos.length)
				{
					var nombreTabla=listaTablasAtributos[j][0];
					if (tablaABuscar==nombreTabla)
					{
						tablaEncontrada=true;
						var vectorCampos=listaTablasAtributos[j][2];
						for(var k=0;k<vectorCampos.length;k++)
						{
							codigosCampos[k]=vectorCampos[k][0];//cogemos el código y lo insertamos en el vector de códigos
							comentariosCampos[k]=vectorCampos[k][1];//cogemos el cómentario y lo insertamos en el vector de cómentarios
						}//del for
					}//del if
					j++;
				}//del while
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
          document.forms[0].action="<%=request.getContextPath()%>/administracion/mantenimiento/Etiquetas.do";
          document.forms[0].submit();
          limpiarFormulario('rejilla/tabla');
        }
				else
          jsp_alerta("A",'<%=descriptor.getDescripcion("msjCodExiste")%>'); 
			}
			else
				jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
		}//de la funcion


    function pulsarModificar()
		{
			if(filaSeleccionada(tablaEtiquetas)) //Si  ha seleccionado alguna fila
			{
				if(validarCamposRejilla()) //Si los campos de la rejilla son correctos (los obligatorios)
				{
	        if(noEsta(tablaEtiquetas.selectedIndex)) //Si el código está en la lista
					{
	          document.forms[0].opcion.value="modificar";
	          document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
	          document.forms[0].action="<%=request.getContextPath()%>/administracion/mantenimiento/Etiquetas.do";
	          document.forms[0].submit();
	          limpiarFormulario('rejilla/tabla');
	        }
					else
	          jsp_alerta("A",'<%=descriptor.getDescripcion("msjCodExiste")%>'); 
				}
				else
					jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
      }
			else
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoSelecFila")%>');					
		}//de la funcion


    function pulsarBorrar()
		{
			if(filaSeleccionada(tablaEtiquetas)) //Si  ha seleccionado alguna fila
			{
				if(validarCamposRejilla()) //Si los campos de la rejilla son correctos (los obligatorios)
				{
	        if(noEsta(tablaEtiquetas.selectedIndex)) //Si el código está en la lista
					{
						document.forms[0].opcion.value="eliminar";
		        document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
		        document.forms[0].action="<%=request.getContextPath()%>/administracion/mantenimiento/Etiquetas.do";
		        document.forms[0].submit();
		        limpiarFormulario('rejilla/tabla');
	        }
					else
	          jsp_alerta("A",'<%=descriptor.getDescripcion("msjCodExiste")%>'); 
				}
				else
					jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
      }
			else
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoSelecFila")%>');					
		}//de la funcion

		
		function pulsarLimpiar()
		{
			limpiarFormulario('campos_rejilla');
		}//de la funcion
		
		function pulsarSalir()
		{
			if(ventana=="false"){
				window.location = "<%=request.getContextPath()%>/administracion/mantenimiento/Etiquetas.do?opcion=salir"
			}else{
				var datosRetorno;
				if(indice>-1)
					datosRetorno = listaEtiquetas[indice];
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
<input type="hidden" name="codEtiqAntiguo" value="">
<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_MantEtiquetas")%></div>
<div class="contenidoPantalla">
    <TABLE>
        <tr>
            <td colspan="6" id="tablaEtiquetas"></td> 
        </tr>
        <tr>
            <td class="etiqueta" style="width:3%"> 
                <input name="codEtiq" type="text" class="inputTextoObligatorio" style="width:100%" maxlength=3
                        onKeyPress = "javascript:return SoloDigitos(event);">	
            </td>
            <td class="etiqueta" style="width:15%">
                <input name="nomEtiq" type="text" class="inputTexto" style="width:100%" maxlength=200
                        onKeyPress = "javascript:PasaAMayusculas(event);">
            </td>
            <td class="etiqueta" style="width:10%"> 
                <input name="descEtiq" type="text" class="inputTextoObligatorio" style="width:100%" maxlength=50
                        onKeyPress = "javascript:PasaAMayusculas(event);">
            </td>
            <td class="etiqueta" style="width:20%">
                <input class="inputTextoObligatorio" type="text" name="codAplic" id="codAplic" style="width:10%" maxlength="3">
                <input class="inputTextoObligatorio" type="text" name="descAplic" id="descAplic" style="width:70%" readonly>
                <a name="anchorAplic" href="" id="anchorAplic"><span class="fa fa-chevron-circle-down" aria-hidden="true" name="botonAplic" id="botonAplic" style="cursor:hand;"></span></a>
            </td>
            <td class="etiqueta" style="width:20%"> 
                <input class="inputTextoObligatorio" type="text" name="codTabla" id="codTabla" style="width:10%" maxlength="50">
                <input class="inputTextoObligatorio" type="text" name="descTabla" id="descTabla" style="width:70%" readonly>
                <a name="anchorTabla" href="" id="anchorTabla"><span class="fa fa-chevron-circle-down" aria-hidden="true" name="botonTabla" id="botonTabla" style="cursor:hand;"></span></a>
            </td>
            <td class="etiqueta" style="width:20%">
                <input class="inputTextoObligatorio" type="text" name="codCampo" id="codCampo" style="width:10%" maxlength="50">
                <input class="inputTextoObligatorio" type="text" name="descCampo" id="descCampo" style="width:70%" readonly>
                <a name="anchorCampo" href="" id="anchorCampo"><span class="fa fa-chevron-circle-down" aria-hidden="true" name="botonCampo" id="botonCampo" style="cursor:hand;"></span></a>
            </td>
        </tr>
    </table>
    <div id="tablaBotones" class="botoneraPrincipal">
        <input type="button" class="boton"  name="botonAlta"	onClick="pulsarAlta();" accesskey="A" value='<%=descriptor.getDescripcion("gbAlta")%>'>
        <input type="button" class="boton"  name="botonModificar"	onClick="pulsarModificar();" accesskey="M" value='<%=descriptor.getDescripcion("gbModificar")%>'>
        <input type="button" class="boton" name="botonBorrar"	onClick="pulsarBorrar();" accesskey="B" value='<%=descriptor.getDescripcion("gbEliminar")%>'> 
        <input type="button" class="boton"  name="botonLimpiar"	onClick="pulsarLimpiar();" accesskey="L" value='<%=descriptor.getDescripcion("gbLimpiar")%>'>
        <input type="button" class="boton" name="botonSalir" onClick="pulsarSalir();" accesskey="S" value='<%=descriptor.getDescripcion("gbSalir")%>'> 
    </div>				
</div>				
</form>
<script type="text/javascript">
  var indice;
  var tablaEtiquetas = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaEtiquetas'));
  tablaEtiquetas.addColumna('25',null,'<%=descriptor.getDescripcion("gEtiq_Codigo")%>');//codEtiq
  tablaEtiquetas.addColumna('132',null,'<%=descriptor.getDescripcion("gEtiq_Nombre")%>');//nomEtiq
  tablaEtiquetas.addColumna('130',null,'<%=descriptor.getDescripcion("gEtiq_Descrip")%>');//descEtiq
  tablaEtiquetas.addColumna('140',null,'<%=descriptor.getDescripcion("gEtiq_Aplicacion")%>');//codAplic
  tablaEtiquetas.addColumna('145',null,'<%=descriptor.getDescripcion("gEtiq_Tabla")%>');//codTabla
  tablaEtiquetas.addColumna('135',null,'<%=descriptor.getDescripcion("gEtiq_Campo")%>');//codCampo 
  tablaEtiquetas.displayCabecera = true;
  
function refresca(tabla) {
    tabla.displayTabla();
}//de la funcion

function rellenarDatos(tableName,rowID){
    if(tablaEtiquetas==tableName) {
        if(filaSeleccionada(tablaEtiquetas)) //Si  ha seleccionado alguna fila{
            var i=rowID;
            indice = rowID;	    			
            if(i>=0){
                var vectorDatosRejilla = [[listaEtiquetas[i][0]],[listaEtiquetas[i][1]],
                                                        [listaEtiquetas[i][2]],[listaEtiquetas[i][3]],[listaEtiquetas[i][4]],
                                                        [listaEtiquetas[i][5]],[listaEtiquetas[i][6]],[listaEtiquetas[i][7]],
                                                        [listaEtiquetas[i][8]]];
                                                        //Nos quedamos con el código antiguo por si se desea modificar
                                                        document.forms[0].codEtiqAntiguo.value=listaEtiquetas[i][0];
                rellenar(vectorCamposRejillaTodos,vectorDatosRejilla);
            }
        }  
    }
} //de la funcion
 
// FUNCION DE CONTROL DE TECLAS
document.onmouseup = checkKeys; 

function checkKeysLocal(evento,tecla){
          var teclaAuxiliar="";
          if(window.event){
            evento = window.event;
            teclaAuxiliar =evento.keyCode;
          }else
                teclaAuxiliar =evento.which;

            //** Esta funcion se debe implementar en cada JSP para particularizar  **//
            //** las acciones a realizar de las distintas combinaciones de teclas  **//
                    keyDel(evento);
                    if(teclaAuxiliar == 40)
                    {
                            upDownTable(tablaEtiquetas,listaTablaEtiquetas,teclaAuxiliar);
                    }

                    if(teclaAuxiliar == 38)
                    {
                            upDownTable(tablaEtiquetas,listaTablaEtiquetas,teclaAuxiliar);
                    }  
}//de la funcion
	
//Declaramos los combos
comboAplicaciones = new Combo("Aplic"); 	
comboTablas = new Combo("Tabla");	
comboCampos = new Combo("Campo");	
	
//Redefinimos la función Change para el combo de Tablas, este deberá recargar el 
//combo de Campos y rellenar su códdigo y descripción con el primer valor.
comboTablas.change=
        function()
        {
                //llamamos a la función que nos recarga los codigos y descripciones de los campos
                cargarComboCampos();	
                //Asignamos de nuevo los vectores al combo de campos
                comboCampos.addItems(codigosCampos,comentariosCampos);
                //Rellenamos los campos del combo del que depende (campos)
        rellenar(['codCampo','descCampo'],[[codigosCampos[0]],[comentariosCampos[0]]]);		
        }//de la funcion	
</script>
</body>
</html>