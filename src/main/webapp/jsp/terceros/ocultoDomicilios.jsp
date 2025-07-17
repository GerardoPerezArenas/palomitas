<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="org.apache.commons.logging.Log"%>

<html>
<head>
<title>Oculto Busqueda Domicilios</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<script src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script language="javascript">
  var ventana = "<bean:write name="BusquedaTercerosForm" property="ventana"/>";
  var Domicilio = new Array();
  var lista = new Array();	
  
  function cargarBusqueda(){
    var i = 0;
		var j = 0;
		<% 
      Log m_log = LogFactory.getLog(this.getClass().getName());
      BusquedaTercerosForm bForm =(BusquedaTercerosForm)session.getAttribute("BusquedaTercerosForm");
      Vector listaDoms = bForm.getListaDomicilios();
      int lengthDoms = listaDoms.size();
      int i = 0;
	  String domicilios = "";
	  String lista = "";
	  String campo1 = "";
      String campo2 = "";
      String campo3 = "";
      String campo4 = "";
      String campo5 = "";
      String campo6 = "";
	  String valor = "";
	  
	  if (lengthDoms>0) {

		  for(i=0;i<lengthDoms-1;i++){
	  		domicilios += "[" 
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("codVia")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("descVia")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("codPais")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("descPais")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("codProvincia")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("descProvincia")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("codMunicipio")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("descMunicipio")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("codTipoVia")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("descTipoVia")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("numDesde")+"\"," // 10
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("letraDesde")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("numHasta")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("letraHasta")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("bloque")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("portal")+"\"," //15
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("escalera")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("planta")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("puerta")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("km")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("hm")+"\"," //20
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("lugar")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("codPostal")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("codUso")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("descUso")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("idDomicilio")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("idVia")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("codECO")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("codESI")+"\","
					   + "\""+(String)((GeneralValueObject)listaDoms.get(i)).getAtributo("descESI") +"\","
					    + "\""+(String)((GeneralValueObject)listaDoms.get(i)).getAtributo("origen") +"\""
					  + "],";
	  	 campo1="";
		 campo2="";
		 campo3="";
		 campo4="";
		 campo5="";
		 campo6="";
		 valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("numDesde");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo1 = valor;
		 valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("letraDesde");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo1 += "-"+ valor;
		 valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("numHasta");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo1 = valor;
		 valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("letraHasta");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo1 += "-"+ valor;
		 valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("bloque");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo2 = valor;
		 valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("portal");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo2 += "-"+ valor;
		 valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("escalera");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo3 += valor;
		 valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("planta");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo4 = valor;
		 valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("puerta");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo4 += " "+ valor;
		 valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("km");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo5 = valor;
		 valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("hm");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo5 += "-"+ valor;
	    valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("origen");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo6 = valor;
		  lista += "[\""+ campo1 +"\",\""+ campo2 +"\",\""+ campo3 +"\",\""+ campo4 +"\",\""+ campo5 + "\",\""+campo6 + "\"],";
	    }	  // fin for
                   
	  		domicilios += "[" 
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("codVia")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("descVia")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("codPais")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("descPais")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("codProvincia")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("descProvincia")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("codMunicipio")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("descMunicipio")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("codTipoVia")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("descTipoVia")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("numDesde")+"\"," // 10
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("letraDesde")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("numHasta")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("letraHasta")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("bloque")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("portal")+"\"," //15
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("escalera")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("planta")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("puerta")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("km")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("hm")+"\"," //20
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("lugar")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("codPostal")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("codUso")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("descUso")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("idDomicilio")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("idVia")+"\","
					   + "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("codECO")+"\","
					   +  "\""+ (String)((GeneralValueObject)listaDoms.get(i)).getAtributo("codESI")+"\","
					   + "\""+(String)((GeneralValueObject)listaDoms.get(i)).getAtributo("descESI") +"\","
					   + "\""+(String)((GeneralValueObject)listaDoms.get(i)).getAtributo("origen") +"\""
					 +  "]";

	  	 campo1="";
		 campo2="";
		 campo3="";
		 campo4="";
		 campo5="";
		 campo6 ="";
		 valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("numDesde");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo1 = valor;
		 valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("letraDesde");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo1 += "-"+ valor;
		 valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("numHasta");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo1 = valor;
		 valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("letraHasta");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo1 += "-"+ valor;
		 valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("bloque");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo2 = valor;
		 valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("portal");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo2 += "-"+ valor;
		 valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("escalera");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo3 += valor;
		 valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("planta");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo4 = valor;
		 valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("puerta");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo4 += " "+ valor;
		 valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("km");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo5 = valor;
		 valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("hm");
		 if (valor != null)
		 	if (!"".equals(valor))
				campo5 += "-"+ valor;
            valor = (String) ((GeneralValueObject)listaDoms.get(i)).getAtributo("origen");
		  if (valor != null)
			if (!"".equals(valor))
				campo6 =  valor;
		  lista += "[\""+ campo1 +"\",\""+ campo2 +"\",\""+ campo3 +"\",\""+ campo4 +"\",\""+ campo5 + "\",\""+campo6 + "\"]";

	} // Fin if
    %>
	  	Domicilio = [<%= domicilios %>];	
	  	lista = [<%= lista %>];	
		var frame;
    	frame=parent.mainFrame;
   		frame.recuperaBusquedaDomicilio();
    
  }
	
   var Vias = new Array();


  	function cargarListaVias(){
    <% 
		Vector listaVias = bForm.getListaVias();
		int lengthVias = listaVias.size();
	    String idVias="";
      	String codVias="";
      	String descVias="";
		int k = 0;
     	if ( lengthVias>0){  
      		for(k=0;k<lengthVias-1;k++){
        		GeneralValueObject vias = (GeneralValueObject)listaVias.get(k);
        		idVias+="\""+(String)vias.getAtributo("idVia")+"\",";
        		codVias+="\""+(String)vias.getAtributo("codVia")+"\",";
        		descVias+="\""+(String)vias.getAtributo("descVia")+"\",";
      		}		
      		GeneralValueObject vias = (GeneralValueObject)listaVias.get(k);
      		idVias+="\""+(String)vias.getAtributo("idVia")+"\"";
      		codVias+="\""+(String)vias.getAtributo("codVia")+"\"";
      		descVias+="\""+(String)vias.getAtributo("descVia")+"\"";
		} else if(m_log.isDebugEnabled()) m_log.debug("lengthVias igual a cero");
      %>
    	frame.idVias = [<%=idVias%>];
    	frame.codVias = [<%=codVias%>];
    	frame.descVias = [<%=descVias%>];
		frame.auxCombo = 'comboVia';
		frame.cargarComboBox(frame.codVias,frame.descVias);
  }

</script>

<%
  String opcion=request.getParameter("opcion");
  if(m_log.isDebugEnabled()) m_log.debug("<" + opcion + ">");
  if (opcion.equals("buscar")){%>
    <script>cargarBusqueda()</script>
<%}else if (opcion.equals("cargarVias")){%>
    <script>cargarListaVias()</script>
<%}else if (opcion.equals("descripcionVias")){%>
    <script>cargarListaVias()</script>
<%}%>
</head>

<body>

</body>
</html>
