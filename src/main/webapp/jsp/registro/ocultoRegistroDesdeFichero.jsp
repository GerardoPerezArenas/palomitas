
<%@page import="es.altia.agora.interfaces.user.web.registro.RegistroDesdeFicheroForm" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>

<head>
<title> OCULTO REGISTRO DESDE FICHERO</title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<script>
<%
	UsuarioValueObject usuarioVO = new UsuarioValueObject();    
	int idioma=1;
  	int apl=1;
	RegistroDesdeFicheroForm rForm = new RegistroDesdeFicheroForm();
	if (session.getAttribute("usuario") != null){
    	usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
		rForm = (RegistroDesdeFicheroForm)session.getAttribute("RegistroDesdeFicheroForm");
    	idioma = usuarioVO.getIdioma();
    	apl = usuarioVO.getAppCod();
	}
	String opcion = rForm.getOpcion();
	String respOpcion = rForm.getRespOpcion();
%>

function redirecciona() {

	var registrosNoInsertados  = new Array();

  	var resp = '<%= opcion %>';
  	<%	
		Vector registrosNoIns = rForm.getRegistros();
		int longRegistrosNoIns = 0;
		String valores ="[";
		if (registrosNoIns != null) longRegistrosNoIns = registrosNoIns.size();		
		if (longRegistrosNoIns > 0 ){
				GeneralValueObject dVO = (GeneralValueObject) registrosNoIns.elementAt(0);
				valores += "[\""+ (String) dVO.getAtributo("ejercicio")+"/"+ (String) dVO.getAtributo("numero") 
					+"\",\""+ (String) dVO.getAtributo("expediente") 
					+"\",\""+ (String) dVO.getAtributo("nombre")+" "+(String) dVO.getAtributo("apellidos") + "\"]";		
				for(int i=1;i<longRegistrosNoIns;i++){
					dVO = (GeneralValueObject) registrosNoIns.elementAt(i);
					valores += ",[\""+ (String) dVO.getAtributo("ejercicio")+"/"+ (String) dVO.getAtributo("numero") 
						+"\",\""+ (String) dVO.getAtributo("expediente") 
						+"\",\""+ (String) dVO.getAtributo("nombre")+" "+(String) dVO.getAtributo("apellidos") + "\"]";		
				}
  		}
  		valores += "]";
  	%>
  	registrosNoInsertados = <%= valores %>;  	
  	parent.mainFrame.resultadoInsertarDesdeFichero('<%=respOpcion%>',registrosNoInsertados);

}
</script>

</head>
<body onLoad="redirecciona();">
<p>&nbsp;<p><center>
</body>
</html>
