<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="java.text.MessageFormat"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>

<html:html>

<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

<TITLE>::: EXPEDIENTES  Configuración Servicio Web:::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />

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
  Config m_Config = ConfigServiceHelper.getConfig("common");
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
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/dojo/dojo.js"></script>

    <script type="text/javascript">
    	
        dojo.require("dojo.widget.FilteringTable");
		var listaParametrosSalida = new Array();
		var listaParametrosEntrada = new Array();
		function inicializar() {
			
			var valor = "";
	        
	        var i = 0;
	        
	        var j = 0;
			
			<logic:iterate id="param" property="listaParamsEntrada" name="ConfiguracionSWTramiteForm">
				<logic:equal name="param" property="tipoValorPaso" value="0">
					valor = '<bean:write name="param" property="valorConstante"/>';
				</logic:equal>
				<logic:equal name="param" property="tipoValorPaso" value="1">
					valor = '<bean:write name="param" property="codCampoExp"/>';
				</logic:equal>                                       
	                    
	            listaParametrosEntrada[i++] = ['<bean:write name="param" property="nombreDefinicion"/>',
	                    '<bean:write name="param" property="tituloParam"/>', valor,
	                    '<bean:write name="param" property="obligatorio"/>',
	                    '<bean:write name="param" property="tipoValorPaso"/>'];                    
	        </logic:iterate>
	
	        
	        <logic:iterate id="param" property="listaParamsSalida" name="ConfiguracionSWTramiteForm">
	        	
	        	
	            listaParametrosSalida[j++] = [
	                    '<bean:write name="param" property="nombreDefinicion"/>',                    
	                    '<bean:write name="param" property="codCampoExp"/>',
	                    '<bean:write name="param" property="tituloParam"/>'];
	        </logic:iterate>
	        	      	
		}
        function pulsarAceptar() {
			var i = 0;
			var valYTiposE = new Array();
			var valoresS= new Array();
			while (i<listaParametrosEntrada.length) {
				valYTiposE[i] = [listaParametrosEntrada[i][2],listaParametrosEntrada[i][4]];
				i++;
			}
			i=0;
			while (i<listaParametrosSalida.length) {
				valoresS[i] = listaParametrosSalida[i][1];
				i++;
			}
			

												  
												  	 											  
			document.forms[0].opcion.value = "guardarCambios";
			var source = "<c:url value='/sge/ConfiguracionSWTramite.do'/>";
			source += "?valYTiposE=" + valYTiposE;
			
			source += "&valoresS=" + valoresS;
            document.forms[0].action = source;
            document.forms[0].submit();
            
	       	
        }
        function listaToString(lista,inout) {
        	var s="";
        	var primero = 1;
        	var i=0;
        	for (i in lista) {
        		if (primero == 1) {
        			if (inout=='0') s+= lista[i];
        			else s+= listaToStringIn(lista[i]);
        			primero = 0;

        		}
        		else {
        			if (inout=='0') s+= ',' + lista[i];
        			else s+= ',' +listaToStringIn(lista[i]);

        		}
        		
        	}
        	return s;
        }
        
        function vacia2esp(str) {
        	if (str=="") {
        		return " ";
        	}
        	else {
        		return str;
        	}
        }
        

        
		function listaToStringIn(lista) {
        	return(lista[0] + ',' + lista[1]);
        }

        function configurarParametro(tabla, esEntrada) {
            var m = dojo.widget.byId(tabla);
            if (m) {
                var s = getValue(m);
                if (s != -1) {
                	var valor;
                	var tipo;
                	if (esEntrada == 'true') {
                		valor = listaParametrosEntrada[s][2];
                		tipo = listaParametrosEntrada[s][3];
                	}
                	else {
                		valor = listaParametrosSalida[s][1];
                	}
                    var source = "<c:url value='/sge/ConfiguracionSWTramite.do'/>";
                    source += "?opcion=modificarParametro";
                    source += "&selectedIndex=" + s;
                    source += "&esEntrada=" + esEntrada;
                    source += "&tipo=" + tipo;
                    source += "&valor=" + valor;
                    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,null,
                        'width=650,height=490,status=no',function(datos){
                            if (datos != undefined) {
                                  var indice = datos[2];
                                  if (esEntrada == 'true') {
                                      listaParametrosEntrada[indice] = [
                                              listaParametrosEntrada[indice][0],
                                              listaParametrosEntrada[indice][1],
                                              datos[1],
                                              listaParametrosEntrada[indice][3],
                                              datos[3]];
                                  } else {
                                      listaParametrosSalida[indice] = [
                                              listaParametrosSalida[indice][0],
                                              datos[1],
                                              listaParametrosSalida[indice][2]];
                                  }
                                  recargaDatos(tabla, esEntrada);
                              }
                          });
                } else {
                    jsp_alerta('A', "NO HA SELECCIONADO NINGUNA FILA");
                }
            }
        }

        function getValue(w) {
            var data = w.store.get();
            var codigoSW = -1;
            for (var i = 0; i < data.length; i++) {
                if (data[i].isSelected) {
                    codigoSW = i;
                }
            }
            return codigoSW;
        }

        function recargaDatos(tabla, esEntrada) {
            clearData(tabla);
            var theJSONData = [];
            if (esEntrada == 'true') {
              	var pa; 
                for (var j = 0; j < listaParametrosEntrada.length; j++) {
                	pa = listaParametrosEntrada[j][1];
                	if (listaParametrosEntrada[j][3]=='1') {
                		pa = '(*) ' + pa;
                	}
                    var o = {
                        Id:listaParametrosEntrada[j][0],
                        Parametro:pa,
                        Valor:listaParametrosEntrada[j][2]
                    };
                  
                    theJSONData.push(o);
                }
            } else {
                for (var j = 0; j < listaParametrosSalida.length; j++) {
                    var o = {
                        Id:listaParametrosSalida[j][0],
                        Parametro:listaParametrosSalida[j][2],
                        Valor:listaParametrosSalida[j][1]
                    };
                    theJSONData.push(o);
                }
            }
            var w = dojo.widget.byId(tabla);
            w.store.setData(theJSONData);
        }

        function clearData(key) {
            dojo.widget.byId(key).store.clearData();
        }
		function comprobarErrores() {
			var op = '<c:out value = "${requestScope.opcion}"/>';
			
			if (op=="inicioConfiguracion") {

				inicializar();
            
       	  	} 			
			else {
				if (op=="guardarCambios") {
				
					<c:if test="${empty requestScope.errorOb}">             
            			self.parent.opener.retornoXanelaAuxiliar("recarga");
            		</c:if>
            		<c:if test="${! empty requestScope.errorOb}">             
						jsp_alerta('A','<c:out value = "${requestScope.errorOb}"/>'); 
						inicializar();
<!--						var source = '<c:url value='/sge/ConfiguracionSWTramite.do'/>';-->
<!--						source += '?opcion=inicioConfiguracion';						-->
<!--			            document.forms[0].action = source;-->
<!--			            document.forms[0].submit();-->
						
<!--						recargaDatos('parsedFromHtmlen', 'true');-->
<!--						recargaDatos('parsedFromHtmlsa' , 'false');-->							
            		</c:if>
            
       	  		}
       	  		
       	  	}

       	  	
		}
		
    </script>


</head>

<BODY class="bandaBody" onload="javascript:{ pleaseWait('off'); 
        comprobarErrores();}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<html:form  action= "sge/ConfiguracionSWTramite.do" target="_self">

<html:hidden  property="opcion" value=""/>

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("tit_confSW")%></div>
<div class="contenidoPantalla">
    <table style="width:100%">
        <tr>
            <td id="datos">
                <fieldset style="height:100px;">
                    <legend class="etiqueta"><%=descriptor.getDescripcion("cswEtiqOperacion")%></legend>
                    <br>

                    <div width="100%" heigth=50% align="center" valign="middle">
                      <html:text property="tituloOperacion" styleClass="inputTextoObligatorio"
                               readonly="true" size="80" name="ConfiguracionSWTramiteForm"/>
                    </div>
                    <table width="35%" heigth=50% cellpadding="0px" cellspacing="7px" border="0px" align="left">
                      <tr>
                        <td align="left">
                            <span class="etiqueta" style="width:20px"><%=descriptor.getDescripcion("cswEtiqTipo")%></span>
                        </td>
                        <td align="left">
                            <html:select style="width:90px" property="isObligatoria"
                                         name="ConfiguracionSWTramiteForm">
                                <html:option value="true"><%=descriptor.getDescripcion("cswEtiqOb")%></html:option>
                                <html:option value="false"><%=descriptor.getDescripcion("cswEtiqOp")%></html:option>
                            </html:select>
                        </td>
                      </tr>
                    </table>
                  </fieldset>
              	</td>
              </tr>
              <tr>
                <td>
                  <!--INICIO DIV PESTAÑAS            -->
                    <div id="mainTabContainer" dojoType="TabContainer" style="width: 100%; height:300px" selectedTab="tab1">
                        <div class="contenidoTab" id="tab1" dojoType="ContentPane" label="<%=descriptor.getDescripcion("iswEtiqEntradaT")%>">
                            <div class="contenidoTabcampo" style="width:100%; height:100%; overflow-y:auto;">            
                                <table width="98%" height="99%" cellpadding="0px" cellspacing="1px" border="0px" bgcolor="#ffffff">
                                    <tr>
                                        <td id="tabla" width="100%" align="center" valign="middle" bgcolor="#e6e6e6" style="padding: 5px;">
                                                <table class="xTabla" dojoType="filteringTable" id="parsedFromHtmlen" multiple="false" alternateRows="true" maxSortable="2"
                                                        ondblclick="configurarParametro('parsedFromHtmlen', 'true');">
                                                    <thead>
                                                        <tr>
                                                          <th field="Parametro" dataType="String" width="50%" align="center"><%=descriptor.getDescripcion("cswEtiqParam")%></th>
                                                          <th field="Valor" dataType="String" width="50%" align="center"><%=descriptor.getDescripcion("cswEtiqValor")%></th>				                        
                                                        </tr>
                                                          </thead>
                                                          <tbody align="left">
                        		    <logic:iterate id="param" name="ConfiguracionSWTramiteForm" property="listaParamsEntrada">
                            		  <tr value="<bean:write name='param' property='nombreDefinicion'/>">
                                		<td>
                                		  <logic:equal name="param" property="obligatorio" value="0">
                							<bean:write name="param" property="tituloParam"/>
            							  </logic:equal>
            							  <logic:equal name="param" property="obligatorio" value="1">
                							(*) <bean:write name="param" property="tituloParam"/>
 								          </logic:equal>
                                		</td>
                                		<td>
                                    	  <logic:equal name="param" property="tipoValorPaso" value="0">
                                                            <bean:write name="param" property="valorConstante"/>
                                    	  </logic:equal>
                                    	  <logic:equal name="param" property="tipoValorPaso" value="1">
                                                            <bean:write name="param" property="codCampoExp"/>
                                    	  </logic:equal>
                                    	  <logic:equal name="param" property="tipoValorPaso" value="2">
                                                            <bean:write name="param" property="codCampoExp"/>
                                    	  </logic:equal>
                                		</td>
                            		  </tr>
                                                    </logic:iterate>
                                                </tbody>
                                              </table>
                                              </td>
                                            </tr>
                                        </table>
                                    </div>
                                  </div>
                                    <div class="contenidoTab" id="tab2" dojoType="ContentPane" label="<%=descriptor.getDescripcion("iswEtiqSalidaT")%>">
                                      <div class="contenidoTabcampo" style="width:100%; height:100%;overflow-y:auto;">
                                        <table width="98%" height="99%" cellpadding="0px" cellspacing="1px" border="0px" bgcolor="#ffffff">
                                            <tr>
                                                <td id="tabla" width="100%" align="center" valign="middle" bgcolor="#e6e6e6" style="padding: 5px;">
                                                    <table class="xTabla" dojoType="filteringTable" id="parsedFromHtmlsa" multiple="false" alternateRows="true" maxSortable="2"
                                                        ondblclick="configurarParametro('parsedFromHtmlsa', 'false');">
                                                        <thead>
                                                        <tr>
                                                            <th field="Parametro" dataType="String" width="50%" align="center"><%=descriptor.getDescripcion("cswEtiqParam")%></th>
                                                            <th field="Valor" dataType="String" width="50%" align="center"><%=descriptor.getDescripcion("cswEtiqValor")%></th>				                        
                                                          </tr>
                                                            </thead>
                                                            <tbody>
                        		    <logic:iterate id="param" name="ConfiguracionSWTramiteForm" property="listaParamsSalida">
                                                            <tr value="<bean:write name="param" property="nombreDefinicion"/>">
                                                              <td> <bean:write name="param" property="tituloParam"/></td>
                                                              <td>
                                                                <logic:equal name="param" property="tipoValorPaso" value="0">
                                                                  <bean:write name="param" property="valorConstante"/>
                                                                </logic:equal>
                                                                <logic:equal name="param" property="tipoValorPaso" value="1">
                                                                  <bean:write name="param" property="codCampoExp"/>
                                                                </logic:equal>
                                                              </td>
                                                            </tr>
                                                              </logic:iterate>
                                                            </tbody>
                                                          </table>
                                                          </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </div>      		      		      		      		      		      		
                                        </div>
                </td>
              </tr>
    </table>
<!--FIN DIV PESTAÑAS      		-->

    <div id="capaBotones1" name="capaBotones1" class="botoneraPrincipal">
        <INPUT type= "button" class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar"  onclick="pulsarAceptar();">
        <INPUT type= "button" class="botonGeneral" accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>" name="cmdSalir"  onclick="self.parent.opener.retornoXanelaAuxiliar();">
    </div>
</div>
</html:form>
<script>
    $(document).ready(function() {
        $(".xTabla").DataTable( {
            "lengthMenu": [ 10, 25, 50, 100 ],
            "aaSorting": [],
            "language": {
                "search": "<%=descriptor.getDescripcion("buscar")%>",
                "previous": "<%=descriptor.getDescripcion("anterior")%>",
                "next": "<%=descriptor.getDescripcion("siguiente")%>",
                "lengthMenu": "<%=descriptor.getDescripcion("mosFilasPag")%>",
                "zeroRecords": "<%=descriptor.getDescripcion("msgNoResultBusq")%>",
                "info": "<%=descriptor.getDescripcion("mosPagDePags")%>",
                "infoEmpty": "<%=descriptor.getDescripcion("noRegDisp")%>",
                "infoFiltered": "<%=descriptor.getDescripcion("filtrDeTotal")%>",
                "paginate": {
                    "first":      "<%=descriptor.getDescripcion("primero")%>",
                    "last":       "<%=descriptor.getDescripcion("ultimo")%>",
                    "next":       "<%=descriptor.getDescripcion("siguiente")%>",
                    "previous":   "<%=descriptor.getDescripcion("anterior")%>",
                },
            }
        } );
    });
</script>

</BODY>
</html:html>
