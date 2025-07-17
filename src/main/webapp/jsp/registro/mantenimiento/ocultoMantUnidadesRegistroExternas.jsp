<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.util.ElementoListaValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.registro.mantenimiento.MantRegistroExternoForm"%>
<%@ page import="java.util.Vector"%>

<html>
<head>
<title> Oculto para el mantenimiento de Organizaciones Externas </title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<script>
var codOrganizaciones = new Array();
var desOrganizaciones = new Array();
var listaOriginal = new Array();
var lista = new Array();

<% 
	MantRegistroExternoForm bForm = (MantRegistroExternoForm)session.getAttribute("MantRegistroExternoForm");
	String resultado =  bForm.getResultado();
	if (resultado == null) resultado="";
	Vector listaOrganizaciones = bForm.getListaOrganizacionesExternas();
	if (listaOrganizaciones == null) listaOrganizaciones = new Vector();
	int lengthOrganizaciones = listaOrganizaciones.size();	

    String cods="";
    String descs="";
	
	if (lengthOrganizaciones > 0 ) {
		ElementoListaValueObject organizaciones;
		for(int i=0;i<lengthOrganizaciones-1;i++){
			organizaciones = (ElementoListaValueObject)listaOrganizaciones.get(i);
			cods+="\""+organizaciones.getCodigo()+"\",";
        	descs+="\""+organizaciones.getDescripcion()+"\",";
		}
		organizaciones = (ElementoListaValueObject)listaOrganizaciones.get(lengthOrganizaciones-1);
		cods+="\""+organizaciones.getCodigo()+"\"";
        descs+="\""+organizaciones.getDescripcion()+"\"";
    } 
	
%>
	codOrganizaciones = [<%=cods%>];;
	descOrganizaciones = [<%=descs%>];
<%
	

	Vector listaUnidadesReg = bForm.getListaUnidadesRegistroExternas();
	if (listaUnidadesReg == null) listaUnidadesReg = new Vector();
	int lengthUnidadesReg = listaUnidadesReg.size();	
	String listaOriginal = "";
	String lista= "";
	if (lengthUnidadesReg > 0 ) {
		GeneralValueObject unidadRegistro = null;
		for(int i=0;i<lengthUnidadesReg-1;i++){
			unidadRegistro= (GeneralValueObject) listaUnidadesReg.get(i);
			lista+="[\""+(String) unidadRegistro.getAtributo("codigo") +"\",\""+(String) unidadRegistro.getAtributo("descripcion")+"\"],";
			listaOriginal+="[\""+(String) unidadRegistro.getAtributo("codigo") 
							+"\",\""+(String) unidadRegistro.getAtributo("descripcion")
							+"\",\""+ (String) unidadRegistro.getAtributo("organizacion") +"\"],";
		}
		
		unidadRegistro= (GeneralValueObject)listaUnidadesReg.get(lengthUnidadesReg-1);	
		lista+="[\""+(String) unidadRegistro.getAtributo("codigo") +"\",\""+(String) unidadRegistro.getAtributo("descripcion")+"\"]";
		listaOriginal+="[\""+(String) unidadRegistro.getAtributo("codigo") 
							+"\",\""+(String) unidadRegistro.getAtributo("descripcion")
							+"\",\""+ (String) unidadRegistro.getAtributo("organizacion") +"\"]";
	}	
%>
	listaOriginal = [<%=listaOriginal%>];
	lista = [<%=lista%>];

function redirecciona()
{
	if ('<%=resultado%>' == '') 
  		parent.mainFrame.recuperaDatos(listaOriginal,lista);
	else parent.mainFrame.recuperaDatosCompleto('<%=resultado%>',listaOriginal,lista,codOrganizaciones,descOrganizaciones);

}
</script>
</head>
<body onLoad="redirecciona();">
</body>

</html>



