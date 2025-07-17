<%@ page import="es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.ArrayList"%>

<%@ page import="es.altia.agora.business.terceros.TercerosValueObject"%>
<%@ page import="es.altia.agora.business.terceros.DomicilioSimpleValueObject"%>
<%@ page import = "es.altia.catalogoformularios.util.DateOperations"%>
<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm" %>
<%@ page import = "es.altia.catalogoformularios.model.solicitudes.vo.FormularioTramitadoVO"%>
<%@ page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>


<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>

 <% 
    BusquedaTercerosForm busquedaTercerosForm = (BusquedaTercerosForm) session.getAttribute("BusquedaTercerosForm");
    MantAnotacionRegistroForm mantForm = (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm");
 %>


<html>
<head>
<title> Buscar Alta Entrada </title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />

<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>"></script>

<script>
  var Terceros = new Array();
  var Domicilio = new Array();
  var listaTemas = new Array();
  var listaDocs = new Array();
   var listaAnteriores=new Array();
  var listaFormularios = new Array();
  var listaFormulariosOriginal = new Array();
  var listaInteresados = new Array();
  var listaRelaciones = new Array();
  var existenAnexosForms='no';
  var digitProcedimientos = new Array();
  var frame;
  if(parent.mainFrame){
        frame = parent.mainFrame;
  } else {
        frame = parent;
  }

  function redirecciona(){ 
    var codProcedimientos = new Array();
    var descProcedimientos = new Array();
    var munProcedimientos = new Array();
    var digitProcedimientos = new Array(); 
    var cod_tiposDocumentos = new Array();
    var desc_tiposDocumentos = new Array();
    var act_tiposDocumentos = new Array();
    var cod_tiposDocumentosAlta = new Array();
    var desc_tiposDocumentosAlta = new Array();	
    var act_tiposDocumentosAlta = new Array();
    var cod_tiposRemitentes = new Array();
    var desc_tiposRemitentes = new Array();
    var act_tiposRemitentes = new Array();
    var cod_tiposTransportes = new Array();
    var desc_tiposTransportes = new Array();
    var act_tiposTransportes = new Array();
    var cod_actuaciones= new Array();
    var desc_actuaciones= new Array();
    var cod_temas = new Array();
    var desc_temas = new Array();
	var cod_tiposIdInteresado= new Array();
    var desc_tiposIdInteresado= new Array();
    var cod_dpto = new Array();
    var desc_dpto = new Array();
    var cod_roles = new Array();
    var desc_roles = new Array();
    var defecto_roles = new Array();

    var cod_formularios = new Array();
    var desc_formularios = new Array();
    var fechaMod;

    var datos  = new Array();
    datos[1] = '<bean:write name="MantAnotacionRegistroForm" property="diaAnotacion"/>';
    datos[2] = '<bean:write name="MantAnotacionRegistroForm" property="mesAnotacion"/>';
    datos[3] = '<bean:write name="MantAnotacionRegistroForm" property="anoAnotacion"/>';
    datos[4] = '<bean:write name="MantAnotacionRegistroForm" property="dia_doc"/>';
    datos[5] = '<bean:write name="MantAnotacionRegistroForm" property="mes_doc"/>';
    datos[6] = '<bean:write name="MantAnotacionRegistroForm" property="ano_doc"/>';
    datos[7] = '<bean:write name="MantAnotacionRegistroForm" property="txtHoraDoc"/>';
    datos[8] = '<bean:write name="MantAnotacionRegistroForm" property="txtMinDoc"/>';
    datos[9] = unescape('<bean:write name="MantAnotacionRegistroForm" property="asunto"/>');  
    datos[10] = '<bean:write name="MantAnotacionRegistroForm" property="cod_tipoTransporte"/>';
    datos[11] = '<bean:write name="MantAnotacionRegistroForm" property="txtCodigoDocumento"/>';
    datos[12] = '<bean:write name="MantAnotacionRegistroForm" property="cbTipoEntrada"/>';
    datos[13] = '<bean:write name="MantAnotacionRegistroForm" property="cod_tipoRemitente"/>';
    datos[14] = '<bean:write name="MantAnotacionRegistroForm" property="txtNumTransp"/>';
    datos[15] = '<bean:write name="MantAnotacionRegistroForm" property="cod_dptoDestino"/>';
    datos[16] = '<bean:write name="MantAnotacionRegistroForm" property="cod_uniRegDestino"/>';
    datos[17] = '<bean:write name="MantAnotacionRegistroForm" property="cod_departamentoOrixe"/>';
    datos[18] = '<bean:write name="MantAnotacionRegistroForm" property="cod_unidadeRexistroOrixe"/>';
    datos[19] = '<bean:write name="MantAnotacionRegistroForm" property="cod_orgDestino"/>';
    datos[20] = '<bean:write name="MantAnotacionRegistroForm" property="cod_entDestino"/>';
    datos[21] = '<bean:write name="MantAnotacionRegistroForm" property="cod_orgOrigen"/>';
    datos[22] = '<bean:write name="MantAnotacionRegistroForm" property="cod_entidadOrigen"/>';
    datos[23] = '<bean:write name="MantAnotacionRegistroForm" property="txtNomeDocumento"/>';
    datos[24] = '<bean:write name="MantAnotacionRegistroForm" property="txtNomeTipoRemitente"/>';
    datos[25] = '<bean:write name="MantAnotacionRegistroForm" property="desc_tipoTransporte"/>';
    datos[26] = '<bean:write name="MantAnotacionRegistroForm" property="cod_actuacion"/>';
    datos[27] = '<bean:write name="MantAnotacionRegistroForm" property="txtNomeActuacion"/>';
    datos[28] = '<bean:write name="MantAnotacionRegistroForm" property="desc_orgDestino"/>';
    datos[29] = '<bean:write name="MantAnotacionRegistroForm" property="desc_entDestino"/>';
    datos[30] = '<bean:write name="MantAnotacionRegistroForm" property="desc_dptoDestino"/>';
    datos[31] = '<bean:write name="MantAnotacionRegistroForm" property="desc_uniRegDestino"/>';
    datos[32] = '<bean:write name="MantAnotacionRegistroForm" property="desc_orgOrigen"/>';
    datos[33] = '<bean:write name="MantAnotacionRegistroForm" property="desc_entidadOrigen"/>';
    datos[34] = '<bean:write name="MantAnotacionRegistroForm" property="desc_departamentoOrixe"/>';
    datos[35] = '<bean:write name="MantAnotacionRegistroForm" property="desc_unidadeRexistroOrixe"/>';
    datos[36] = '<bean:write name="MantAnotacionRegistroForm" property="tipoRegistroOrigen"/>';
    datos[37] = '<bean:write name="MantAnotacionRegistroForm" property="txtExp2Orixe"/>';
    datos[38] = '<bean:write name="MantAnotacionRegistroForm" property="txtExp1Orixe"/>';
    datos[39] = '<bean:write name="MantAnotacionRegistroForm" property="codTerc"/>';
    datos[40] = '<bean:write name="MantAnotacionRegistroForm" property="codDomTerc"/>';
    datos[41] = '<bean:write name="MantAnotacionRegistroForm" property="numModifTerc"/>';
    datos[42] = '<bean:write name="MantAnotacionRegistroForm" property="cod_uniRegDestinoORD"/>';
    datos[43] = '<bean:write name="MantAnotacionRegistroForm" property="cod_uor"/>';      
    datos[44] = '<bean:write name="MantAnotacionRegistroForm" property="ano"/>';
    datos[45] = '<bean:write name="MantAnotacionRegistroForm" property="numero"/>';      
    datos[46] = '<bean:write name="MantAnotacionRegistroForm" property="txtHoraEnt"/>';
    datos[47] = '<bean:write name="MantAnotacionRegistroForm" property="txtMinEnt"/>';
    datos[48] = '<bean:write name="MantAnotacionRegistroForm" property="abiertCerrado"/>';
    datos[49] = '<bean:write name="MantAnotacionRegistroForm" property="desc_dptoDestinoORD"/>';
    datos[50] = "<str:escape><bean:write name="MantAnotacionRegistroForm" property="desc_uniRegDestinoORD" filter="false"/></str:escape>";
    datos[51] = '<bean:write name="MantAnotacionRegistroForm" property="hayTexto"/>';
    datos[52] = unescape('<bean:write name="MantAnotacionRegistroForm" property="textoDiligencia"/>');
    datos[53] = '<bean:write name="MantAnotacionRegistroForm" property="estadoAnotacion"/>';
    datos[54] = unescape('<bean:write name="MantAnotacionRegistroForm" property="txtDiligenciasAnulacion"/>');
    datos[55] = '<bean:write name="MantAnotacionRegistroForm" property="ejercicioAnotacionContestada"/>';
    datos[56] = '<bean:write name="MantAnotacionRegistroForm" property="numeroAnotacionContestada"/>';
    datos[57] = '<bean:write name="MantAnotacionRegistroForm" property="txtExp1"/>';
    datos[58] = '<bean:write name="MantAnotacionRegistroForm" property="cod_procedimiento"/>';
    datos[59] = "<str:escape><bean:write name="MantAnotacionRegistroForm" property="desc_procedimiento"  filter="false"/></str:escape>";    
    datos[60] = '<bean:write name="MantAnotacionRegistroForm" property="observaciones" />';
    datos[61] = '<bean:write name="MantAnotacionRegistroForm" property="autoridad" />'
    datos[62] = '<bean:write name="MantAnotacionRegistroForm" property="codRolDefecto" />';
    datos[63] = "<str:escape><bean:write name="MantAnotacionRegistroForm" property="descRolDefecto" filter="false"/></str:escape>";
    datos[64] = '<bean:write name="MantAnotacionRegistroForm" property="mun_procedimiento"/>';
    datos[65] = '<bean:write name="MantAnotacionRegistroForm" property="codAsunto"/>';
    if (datos[65] == null || datos[65] == 'null') datos[65] = '';
    datos[66] = '<bean:write name="MantAnotacionRegistroForm" property="codProcedimientoRoles"/>';
    datos[67] = '<bean:write name="MantAnotacionRegistroForm" property="fechaDoc"/>';

    /** prueba **/
    datos[68] = '<bean:write name="MantAnotacionRegistroForm" property="asuntoAnotacionBaja"/>';
    datos[69] = '<bean:write name="MantAnotacionRegistroForm" property="fechaBajaAsuntoCodificadoRegistro"/>';
    /** prueba **/

 datos[70] = '<bean:write name="MantAnotacionRegistroForm" property="txtSegEnt"/>';
 
 datos[71]='<bean:write name="MantAnotacionRegistroForm" property="codigoSga"/>';	
 datos[72]='<bean:write name="MantAnotacionRegistroForm" property="expedienteSga"/>';
    
    // #239565: recuperamos el valor de la propiedad y se lo asignamos al input hidden
    var mostrarGenerarModelo     = '<bean:write name="MantAnotacionRegistroForm" property="mostrarGenerarModelo"/>';
    frame.document.forms[0].mostrarGenerarModelo.value = mostrarGenerarModelo;
    frame.mostrarGenerarModelo(frame.tipoActual);

    
    // #234108: recuperamos el valor de la propiedad y se lo asignamos al input hidden
    var tipoDocInteresadoOblig     = '<bean:write name="MantAnotacionRegistroForm" property="tipoDocInteresadoOblig"/>';
    frame.document.forms[0].asuntoConTipoDocOblig.value = tipoDocInteresadoOblig;
        
   var bloquearDestino=<bean:write name="MantAnotacionRegistroForm" property="bloquearDestino"/>;
   var bloquearProcedimiento=<bean:write name="MantAnotacionRegistroForm" property="bloquearProcedimiento"/>
   var asuntoCodificadoObligatorio = <bean:write name="MantAnotacionRegistroForm" property="obligatorioAsuntoCodificado"/>;
    
    
    var aux = '<bean:write name="MantAnotacionRegistroForm" property="procesarBuzon"/>';
    if(aux)
      frame.document.forms[0].pendienteBuzon.value="si";
    else
        frame.document.forms[0].pendienteBuzon.value="no";
    var fecha = '<bean:write name="MantAnotacionRegistroForm" property="fechaHoraHoy"/>';
    var fechaServidor = fecha.substring(0,10);
    var horaServidor = fecha.substring(11,16);
    
    var cont=0;
    <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTemasAsignados">
      listaTemas[cont]= [ '<bean:write name="elemento" property="codigoTema"/>', '<bean:write name="elemento" property="descTema"/>'];
      cont= cont +1;
    </logic:iterate>
    
    cont=0;
    <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaDocsAsignados">
    
    var str='';
    if('<bean:write name="elemento" property="entregado"/>'=='S') str='SI';
    else if('<bean:write name="elemento" property="entregado"/>'=='N') str='NO';
    else str='';
    if(frame.mostrarDigitalizar){
        listaDocs[cont]= [str, '<bean:write name="elemento" property="nombreDoc"/>', '<bean:write name="elemento" property="tipoDoc"/>','<bean:write name="elemento" property="fechaDoc"/>', '<bean:write name="elemento" property="compulsado"/>','<bean:write name="elemento" property="doc"/>','<bean:write name="elemento" property="descripcionTipoDocumental"/>','<bean:write name="elemento" property="idDocumento"/>'];
    } else {
        listaDocs[cont]= [str, '<bean:write name="elemento" property="nombreDoc"/>', '<bean:write name="elemento" property="tipoDoc"/>','<bean:write name="elemento" property="fechaDoc"/>', '<bean:write name="elemento" property="cotejado"/>','<bean:write name="elemento" property="doc"/>','<bean:write name="elemento" property="idDocumento"/>'];
    }
        cont= cont +1;
    </logic:iterate>
        
         cont=0;	
    <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaDocsAnteriores">	
       listaAnteriores[cont]=['<bean:write name="elemento" property="tipoDocAnterior"/>',	
           '<bean:write name="elemento" property="nombreDocAnterior"/>',	
           '<bean:write name="elemento" property="organoDocAnterior"/>',	
           '<bean:write name="elemento" property="fechaDocAnterior"/>'];	
       cont=cont+1;	
    </logic:iterate>
    
    // Relaciones entre asientos
    cont=0;
    <logic:iterate id="relacion" name="MantAnotacionRegistroForm" property="relaciones">
        listaRelaciones[cont] = ['<bean:write name="relacion" property="tipo"/>', '<bean:write name="relacion" property="ejercicio"/>', '<bean:write name="relacion" property="numero"/>'];
        cont = cont + 1;
    </logic:iterate>
            
    // Listado de asuntos
    cont = 0;
    var cod_asuntos = new Array();
    var desc_asuntos = new Array();
    var uni_asuntos = new Array();
    <logic:present name="MantAnotacionRegistroForm" property="listaAsuntos">
        <logic:iterate id="asunto" name="MantAnotacionRegistroForm" property="listaAsuntos">
            uni_asuntos[cont] = '<bean:write name="asunto" property="unidadRegistro"/>';
            cod_asuntos[cont]  ='<bean:write name="asunto" property="codigo"/>';
            desc_asuntos[cont] ="<str:escape><bean:write name="asunto" property="descripcion" filter="false"/></str:escape>";
            cont++;
        </logic:iterate>
    </logic:present>

    // Lista de roles
        var m=0;
    <%  Vector listaRoles = mantForm.getListaRoles();
        for(int t=0;t<listaRoles.size();t++) {
    %>
            cod_roles[m] =     '<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("codRol")%>';
            desc_roles[m] =    '<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("descRol")%>';
            defecto_roles[m] = '<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("porDefecto")%>';
            m++;
    <% } %>


  <%--//cont = 0;
  //<logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaFormulariosAsociados">       
     // fechaMod = '<bean:write name="elemento" property="fecMod"/>';
     // String fecha = DateOperations.toString(( '<bean:write name="elemento" property="fecMod"/>' ),"dd-MM-yyyy"); 
  	 //	listaFormularios[cont]= [ '<bean:write name="elemento" property="codigo"/>', '<bean:write name="elemento" property="descripcion"/>', '<bean:write name="elemento" property="fecMod"/>', '<bean:write name="elemento" property="usuario"/>'];
     // cont= cont +1;       
  //</logic:iterate>--%>
      
    var i = 0;
    
<% // Se recogen los valores necesarios de los formularios asociados a una entrada de registro 
	  Collection coleccion = mantForm.getListaFormulariosAsociados();
	  Iterator iter = coleccion.iterator();
          Vector anexos = (Vector) mantForm.getListaFormulariosAnexos();
        
         	 
	  int iteracion = 0;
	  if (coleccion!=null){
		  while(iter.hasNext()){
                                              

			FormularioTramitadoVO formTramVO = (FormularioTramitadoVO) iter.next();
                        GeneralValueObject gVO=(GeneralValueObject) anexos.elementAt(iteracion);
			String fechaMod = DateOperations.toString(formTramVO.getFecMod(),"dd-MM-yyyy"); %>

                        var imagen;

                        if (('<%=gVO.getAtributo("codigoInforme")%>')==('<%=formTramVO.getCodigo()%>'))
                        {
                            if(('<%=gVO.getAtributo("tieneAnexo")%>')=='si')
                            {
                                existenAnexosForms='si';
                                imagen='<span class="fa fa-paperclip"></span>';
                            }
                            else {
                                imagen='';
                            }                           
                        }else
                            {
                                 
                                imagen='';
                            }



		    listaFormularios[i] = ['<%=formTramVO.getCodigo()%>', '<%=formTramVO.getDescripcion()%>',
		                           '<%=fechaMod%>','<%=formTramVO.getUsuario()%>',imagen];
		    listaFormulariosOriginal[i] = ['<%=formTramVO.getTipo()%>', '<%=formTramVO.getCodigo()%>',
		                           		   '<%=formTramVO.getDescripcion()%>', '<%=fechaMod%>', '<%=formTramVO.getUsuario()%>', 
		                           		   '<%=formTramVO.getEstado()%>'];                         
<%          String fechaMod1 = DateOperations.toString(formTramVO.getFecMod(),"dd-MM-yyyy");	
            
            iteracion=iteracion+1;

%>
		  	
            i++;	 
<%		  }
	  }	  
%>    

    var i = 0;
    var j = 0;
<%
      Vector terceros = busquedaTercerosForm.getListaTerceros();
      for (int l=0;l<terceros.size();l++) {
          TercerosValueObject tercero = (TercerosValueObject) terceros.elementAt(l);
          Vector domicilios = tercero.getDomicilios();
          if (domicilios!=null) {
          for (int k=0;k<domicilios.size();k++) {
            DomicilioSimpleValueObject domicilio = (DomicilioSimpleValueObject) domicilios.elementAt(k); 
            String provincia = domicilio.getProvincia();
            if (provincia==null) provincia="";
            String municipio = domicilio.getMunicipio();
            if (municipio==null) municipio="";%>
            Domicilio[j] = ['<%=domicilio.getPais()%>',
                            '<%=provincia%>',
                            '<%=StringEscapeUtils.escapeJavaScript(municipio)%>',
                            '<%=StringEscapeUtils.escapeJavaScript(domicilio.getDomicilio())%>',
                            '<%=domicilio.getCodigoPostal()%>',
                            '<%=domicilio.getIdDomicilio()%>',
                            '<%=domicilio.getIdPais()%>',
                            '<%=domicilio.getIdProvincia()%>',
                            '<%=domicilio.getIdMunicipio()%>',
                            '<%=domicilio.getNumDesde()%>',
                            '<%=StringEscapeUtils.escapeJavaScript(domicilio.getLetraDesde())%>',
                            '<%=domicilio.getNumHasta()%>',
                            '<%=StringEscapeUtils.escapeJavaScript(domicilio.getLetraHasta())%>',
                            '<%=StringEscapeUtils.escapeJavaScript(domicilio.getBloque())%>',
                            '<%=StringEscapeUtils.escapeJavaScript(domicilio.getPortal())%>',
                            '<%=StringEscapeUtils.escapeJavaScript(domicilio.getEscalera())%>',
                            '<%=StringEscapeUtils.escapeJavaScript(domicilio.getPlanta())%>',
                            '<%=StringEscapeUtils.escapeJavaScript(domicilio.getPuerta())%>',
                            '<%=StringEscapeUtils.escapeJavaScript(domicilio.getBarriada())%>',
                            '<%=domicilio.getIdTipoVia()%>',
                            '<%=domicilio.getTipoVia()%>',
                            '<%=domicilio.getCodTipoUso()%>',
                            '<%=StringEscapeUtils.escapeJavaScript(domicilio.getDescTipoUso())%>',
                            '<%=domicilio.getCodigoVia()%>',
                            '<%=domicilio.getNormalizado()%>',
                            '<%=domicilio.getIdVia()%>',
                            '<%=domicilio.getCodECO()%>',
                            '<%=domicilio.getCodESI()%>',
                            '<%=StringEscapeUtils.escapeJavaScript(domicilio.getDescESI())%>',
                            '<%=StringEscapeUtils.escapeJavaScript(domicilio.getDescVia())%>',
                            '<%=domicilio.getOrigen()%>'];
            j++;

        <% }
        } %>
        Terceros[i] = ['<%=tercero.getIdentificador()%>',
                       '<%=tercero.getVersion()%>',
                       '<%=tercero.getTipoDocumento()%>',
                       '<%=tercero.getDocumento()%>',
                       '<%=StringEscapeUtils.escapeJavaScript(tercero.getNombre())%>',
                       '<%=StringEscapeUtils.escapeJavaScript(tercero.getApellido1())%>',
                       '<%=StringEscapeUtils.escapeJavaScript(tercero.getApellido2())%>',
                       '<%=StringEscapeUtils.escapeJavaScript(tercero.getPartApellido1())%>',
                       '<%=StringEscapeUtils.escapeJavaScript(tercero.getPartApellido1())%>',
                       '<%=StringEscapeUtils.escapeJavaScript(tercero.getTelefono())%>',
                       '<%=StringEscapeUtils.escapeJavaScript(tercero.getEmail())%>',
                       '<%=tercero.getNormalizado()%>',
                       '<%=tercero.getSituacion()%>',
                       '<%=tercero.getFechaAlta()%>',
                       '<%=tercero.getUsuarioAlta()%>',
                       '<%=tercero.getModuloAlta()%>',
                       '<%=tercero.getFechaBaja()%>',
                       '<%=tercero.getUsuarioBaja()%>',
                       Domicilio];
        i++;
  <% } %>
	<%
		Vector listaInteresados = new Vector();
		listaInteresados = (Vector)busquedaTercerosForm.getListaInteresados();
		int lengthInteresados = listaInteresados.size();
		int i = 0;
		
		%>
		var j=0;
		<%
		for(i=0;i<lengthInteresados;i++){
		%>
			listaInteresados[j] = ['<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("codigoTercero")%>',
			'<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("versionTercero")%>',
			'<%=StringEscapeUtils.escapeJavaScript((String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("titular"))%>',
			'<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("descRol")%>',
			'<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("domicilio")%>',
			'<%=StringEscapeUtils.escapeJavaScript((String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("descDomicilio"))%>',
			'<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("porDefecto")%>',
			'<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("rol")%>',
			'<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("telefono")%>',
			'<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("email")%>',
			'<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("pais")%>',
			'<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("provincia")%>',
			'<%=StringEscapeUtils.escapeJavaScript((String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("municipio"))%>',
			'<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("cp")%>',
			'<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("tip")%>',
			'<%=(String)((GeneralValueObject)listaInteresados.get(i)).getAtributo("doc")%>',
			];
			
			j++;
		<%
		}
		%>
	
	
		

    var resp = '<bean:write name="MantAnotacionRegistroForm" property="respOpcion"/>';
    if( (resp == 'modificar') || (resp == 'iniciarDuplicar' ) || (resp == 'relacionar')){
      
      <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaProcedimientos"> 
         codProcedimientos[i]='<bean:write name="elemento" property="txtCodigo"/>';
         descProcedimientos[i]='<bean:write name="elemento" property="txtDescripcion"/>';
         munProcedimientos[i]='<bean:write name="elemento" property="codMunicipio"/>';
         digitProcedimientos[i]= '<bean:write name="elemento" property="procedimientoDigit"/>';
         i++;
      </logic:iterate>
      frame.recuperaListaProcedimientos(codProcedimientos,descProcedimientos,munProcedimientos, digitProcedimientos);
      
      <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTiposDocumentos">
        cod_tiposDocumentos['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
        desc_tiposDocumentos['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
        act_tiposDocumentos['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="activo"/>';
      </logic:iterate>
          
      <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTiposDocumentosAlta">	
          cod_tiposDocumentosAlta['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
          desc_tiposDocumentosAlta['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
          act_tiposDocumentosAlta['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="activo"/>';
      </logic:iterate>

      <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTiposRemitentes">
        cod_tiposRemitentes['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
        desc_tiposRemitentes['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
        act_tiposRemitentes['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="activo"/>';
      </logic:iterate>

      <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTiposTransportes">
        cod_tiposTransportes['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
        desc_tiposTransportes['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
        act_tiposTransportes['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="activo"/>';
      </logic:iterate>

      <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaActuaciones">
        cod_actuaciones['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
        desc_actuaciones['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
      </logic:iterate>

      <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTemas">
        cod_temas['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
        desc_temas['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
      </logic:iterate>
	 
     <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTiposIdInteresado">
     cod_tiposIdInteresado['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
     desc_tiposIdInteresado['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
     </logic:iterate>

     <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaDepartamentos">
     cod_dpto['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
     desc_dpto['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
     </logic:iterate>

  }else{
      cod_tiposDocumentos = 0;
      desc_tiposDocumentos = 0;
      cod_tiposDocumentosAlta = 0;
      desc_tiposDocumentosAlta = 0;
      cod_tiposRemitentes = 0;
      desc_tiposRemitentes= 0;
      cod_tiposTransportes = 0;
      desc_tiposTransportes = 0;
      cod_actuaciones= 0;
      desc_actuaciones= 0;
      cod_temas = 0;
      desc_temas = 0;
	  cod_tiposIdInteresado= 0;
      desc_tiposIdInteresado= 0;
	  cod_dpto = 0;
      desc_dpto = 0;
    }
    
    
    //Se descartan las relaciones entre asientos si es duplicar.
    if(resp == 'iniciarDuplicar') listaRelaciones = new Array();
    if(resp == 'iniciarDuplicar' || resp == 'relacionar'){
		// el tipoDocumental no se debe duplicar
        if(frame.mostrarDigitalizar){
            for(i=0; i<listaDocs.length; i++){
                listaDocs[i][6]='';
        	}
    	}
      var bloqueoFechaHoraPresentacion = '<bean:write name="MantAnotacionRegistroForm" property="bloquearFechaHoraPresentacion"/>';
      if(bloqueoFechaHoraPresentacion!=null && bloqueoFechaHoraPresentacion=="SI")
        frame.BLOQUEO_FECHA_HORA_ANOTACION = true;

      frame.iniciarDuplicar(datos,Terceros,listaTemas,cod_tiposDocumentos,
        desc_tiposDocumentos, act_tiposDocumentos,cod_tiposDocumentosAlta, desc_tiposDocumentosAlta,act_tiposDocumentosAlta,
        cod_actuaciones, desc_actuaciones, cod_tiposTransportes, desc_tiposTransportes, act_tiposTransportes,
        cod_tiposRemitentes, desc_tiposRemitentes, act_tiposRemitentes, cod_temas, desc_temas,cod_tiposIdInteresado, desc_tiposIdInteresado,
             cod_dpto, desc_dpto, fecha, listaDocs,listaAnteriores, listaFormularios, listaFormulariosOriginal,listaInteresados, listaRelaciones,
uni_asuntos, cod_asuntos, desc_asuntos, cod_roles, desc_roles, defecto_roles, fechaServidor, horaServidor, asuntoCodificadoObligatorio,
              bloquearDestino,bloquearProcedimiento,<bean:write name="MantAnotacionRegistroForm" property="procedimientoDigitalizacion"/>);
    }else{ 
       var contNumExpedientes = 0;
       var numsExpedientesRelacionados = new Array();
       var contIdNumsExpedientesRelacionados = new Array();
       <logic:iterate id="numExpediente" name="MantAnotacionRegistroForm" property="listaNumExpedientesRelacionados">
           numsExpedientesRelacionados[contNumExpedientes] = ['<bean:write name="numExpediente"/>'];
           contIdNumsExpedientesRelacionados[contNumExpedientes] = contNumExpedientes;
           contNumExpedientes = contNumExpedientes + 1;
       </logic:iterate>
    
      // Se comprueba si hay que bloquear la fecha y hora de presentación
      var bloqueoFechaHoraPresentacion = '<bean:write name="MantAnotacionRegistroForm" property="bloquearFechaHoraPresentacion"/>';
      if(bloqueoFechaHoraPresentacion!=null && bloqueoFechaHoraPresentacion=="SI")
        frame.BLOQUEO_FECHA_HORA_ANOTACION = true;
    
        <logic:present name="pulsarModificar">	
            <logic:notPresent name="esFechaBajaPosterior">	
                frame.estadolabelTipoDoc = 0;	
            </logic:notPresent>	
            <logic:present name="esFechaBajaPosterior">	
                var ultimaPos = cod_tiposDocumentosAlta.length;	
                cod_tiposDocumentosAlta[ultimaPos] = '<bean:write name="tipoDocumentoPreSel" property="codigo"/>';	
                desc_tiposDocumentosAlta[ultimaPos] = '<bean:write name="tipoDocumentoPreSel" property="txtNomeDescripcion"/>';	
                act_tiposDocumentosAlta[ultimaPos] = '<bean:write name="tipoDocumentoPreSel" property="activo"/>';	
                frame.inicializarValores('txtCodigoDocumento','txtNomeDocumento',cod_tiposDocumentosAlta,desc_tiposDocumentosAlta);	
                frame.ocultarDiv();	
            </logic:present>
        </logic:present>

        // Comprobamos si el registro es telematico, no tiene expedientes asociados y es modificable
        var regTelemModificable = false;
        <logic:present name="MantAnotacionRegistroForm" property="registroTelematicoModicable">
            regTelemModificable = <bean:write name="MantAnotacionRegistroForm" property="registroTelematicoModicable"/>
        </logic:present>
    
      frame.recuperaDatos(datos,Terceros,listaTemas,cod_tiposDocumentos,
        desc_tiposDocumentos, act_tiposDocumentos,cod_tiposDocumentosAlta, desc_tiposDocumentosAlta, act_tiposDocumentosAlta,	
        cod_actuaciones, desc_actuaciones, cod_tiposTransportes, desc_tiposTransportes, act_tiposTransportes,
        cod_tiposRemitentes, desc_tiposRemitentes, act_tiposRemitentes, cod_temas, desc_temas,cod_tiposIdInteresado, desc_tiposIdInteresado, 
        cod_dpto, desc_dpto, fecha, listaDocs,listaAnteriores,listaFormularios, listaFormulariosOriginal,listaInteresados, listaRelaciones,
        uni_asuntos, cod_asuntos, desc_asuntos, cod_roles, desc_roles, defecto_roles,existenAnexosForms,false, bloquearDestino,numsExpedientesRelacionados,
        contIdNumsExpedientesRelacionados,<bean:write name="MantAnotacionRegistroForm" property="registroTelematico"/>,regTelemModificable,<bean:write name="MantAnotacionRegistroForm" property="procedimientoDigitalizacion"/>,
        <bean:write name="MantAnotacionRegistroForm" property="finDigitalizacion"/>,bloquearProcedimiento);
      
    }
    // Cuando se digitaliza, y se vuelve a la pagina inicial se recargan datos desde BD
    // Sino se han guardado, se borra la seleccion en el formulario por parte del usuario, si existe, carga el valor anterior en caso de que el usuario lo haya cambiado
    // Usamos esta variable para saber que estamos modificando y volvemos de la pantalla de digitalizacion (
    var conservarDatosDestinoSIRFormulario =  false;
    <c:if test="${not empty requestScope.irAPestana}">
        frame.irAPestana('<c:out value="${requestScope.irAPestana}"/>');
        frame.ocultaDivCombosDesplegables();
        conservarDatosDestinoSIRFormulario =  true;
    </c:if>    
    
    //if("S"=='<bean:write name="MantAnotacionRegistroForm" property="tipoReg"/>'){
        frame.inicializarPaginaPrincipalAltaRE(conservarDatosDestinoSIRFormulario);
    //}

    
  }

</script>
</head>
<body onLoad="redirecciona();">
</body>
</html>
