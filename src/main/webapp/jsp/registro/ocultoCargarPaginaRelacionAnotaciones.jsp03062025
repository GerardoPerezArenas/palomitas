<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.registro.RegistroValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@page import="es.altia.agora.interfaces.user.web.util.FormateadorTercero" %>
<%@page import="com.google.gson.GsonBuilder" %>

<%@page import="java.util.Vector" %>

<html>
<head>
<title> Oculto cargar pagina </title>
<%
  UsuarioValueObject usuarioVO;
  int idioma=1;
  int apl=1;
  if ((session.getAttribute("usuario") != null) && (session.getAttribute("registroUsuario") != null)){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    idioma = usuarioVO.getIdioma();
    apl = usuarioVO.getAppCod();
  }
%>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<script>

function redirecciona()
{

parent.mainFrame.listaOriginal = new Array();
parent.mainFrame.lista= new Array();
parent.mainFrame.listaSel = new Array();
parent.mainFrame.listaAnotacionesCompleta = new Array();
var DOT_COMMA = ";";

var asunto;
var estado;
var ejer;	
var numAnot;
var tipoAnot;
var finDigitalizacion;
var estadoExpediente;
var digitalizacionActiva="<%= (String)session.getAttribute("DigitalizacionActiva") %>";


<% /* Recuperar el vector de anotaciones de la sesion. */

   MantAnotacionRegistroForm f= (MantAnotacionRegistroForm)  session.getAttribute("MantAnotacionRegistroForm");
   RegistroValueObject arVO = f.getRegistro();   
   int numPagina = Integer.parseInt(arVO.getPaginaListado());

   String nombreCompleto;
   String apellido1;
   String apellido2;
   String multiinteresado;
   String entrada="";
   String procedimiento="";
   String expediente="";
   String entrSal="";
   String estado2="";
   String finDigitalizacion="";
   String estadoExpediente="";
   Vector relacionAnotaciones = (Vector) session.getAttribute("RelacionAnotaciones");
   String relaciones = (String)session.getAttribute("relaciones");
   int numAnotaciones = ((Integer)session.getAttribute("NumRelacionAnotaciones")).intValue();
   
    if (relacionAnotaciones != null) {
        int j = 0;
        int z=0;
        for(int i=0;i<relacionAnotaciones.size();i++){
            Vector cp_anotacion = (Vector)relacionAnotaciones.get(i);
            nombreCompleto = (String) cp_anotacion.elementAt(8);
            apellido1 = (String) cp_anotacion.elementAt(9);
            apellido2 = (String) cp_anotacion.elementAt(10);
            multiinteresado=(String) cp_anotacion.elementAt(15);
            //System.out.println("\n AAAAAAAAAAA "+ cp_anotacion.elementAt(12));
            
            //para dar formato al interesado
            boolean masTerceros = false;
            if (!multiinteresado.equals("1")) masTerceros = true;
            String nombreFormateado = FormateadorTercero.getDescTercero(nombreCompleto, apellido1, apellido2, masTerceros);

%>

  parent.mainFrame.listaAnotacionesCompleta[<%= j %>] = <%= new GsonBuilder().serializeNulls().create().toJson(cp_anotacion) %>;
   asunto = unescape("<%= cp_anotacion.elementAt(7) %>");
   ejer = "<%= cp_anotacion.elementAt(3) %>";	
   numAnot = "<%= cp_anotacion.elementAt(4) %>";
   tipoAnot = "<%= cp_anotacion.elementAt(2) %>";
   finDigitalizacion = "<%= cp_anotacion.elementAt(22) %>";
   estadoExpediente= "<%= cp_anotacion.elementAt(23) %>";
   parent.mainFrame.listaOriginal[<%= j %>]  = [ejer,
       numAnot,"<%= cp_anotacion.elementAt(5) %>",asunto,
       "<%=StringEscapeUtils.escapeJavaScript(nombreFormateado)%>",
       "<%= cp_anotacion.elementAt(10) %>","<%= cp_anotacion.elementAt(11) %>"];
   if (asunto.length > 75 ) asunto = asunto.substring(0,27)+"</BR>"+asunto.substring(27,75)+"...";

      //tipo de entrada segun el codigo que recibo de la base de datos:
   if ("<%= cp_anotacion.elementAt(20) %>" == "0") {
    entrada='<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("entradaOrd"))%>';
   }else if("<%= cp_anotacion.elementAt(20) %>" == "1"){
         entrada=   '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("DestOtroReg"))%>';
   }else if("<%= cp_anotacion.elementAt(20) %>" == "2"){
         entrada='<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("procOtroReg"))%>';
   }
  
//variable estado2 que es la que aparece en la columna de la tabla estado y necesita el estado pendiente
    if(finDigitalizacion=="0" && digitalizacionActiva=="si"){
		finDigitalizacion= '<b> Pend. fin digit. </b>';
	}else 
		finDigitalizacion="";
   if ("<%= cp_anotacion.elementAt(13) %>" == "9"){
   	estado='<FONT style="color:red"> <%=descriptor.getDescripcion("etiq_anulada")%></FONT><BR>';
        estado2='Anulada'+finDigitalizacion;
   }else if ("<%= cp_anotacion.elementAt(13) %>" == "1"){
   	estado='<FONT style="color:green"> <%=descriptor.getDescripcion("etiq_aceptada")%></FONT><BR>';
        estado2='Aceptada'+finDigitalizacion;
   }else if ("<%= cp_anotacion.elementAt(13) %>" == "2"){
   	estado='<FONT style="color:red"> <%=descriptor.getDescripcion("etiq_rechazada")%></FONT><BR>';
        estado2='Rechazada'+finDigitalizacion;        
     }else if ("<%= cp_anotacion.elementAt(13) %>" == "3"){
   	estado='<FONT style="color:green"> <%=descriptor.getDescripcion("etiq_asocExpSimple")%></FONT><BR>';        
        estado2='Asociada a expediente'+finDigitalizacion;            
     }else if ("<%= cp_anotacion.elementAt(13) %>" == "99"){
   	estado='<FONT style="color:green"> <%=descriptor.getDescripcion("etiqAceptadaDest")%></FONT><BR>';        
        estado2='Aceptada en destino'+finDigitalizacion;      
  } else if ("<%= cp_anotacion.elementAt(13) %>" == "0"){
   /* Fin enlace con SGE. */
     estado2='Pendiente'+finDigitalizacion;
     estado='';
  }
   
   
   // Se construye la listaObs para resaltar los campos con observaciones
   var observacion = '<%=StringEscapeUtils.escapeJavaScript((String) cp_anotacion.elementAt(16))%>';
   if (observacion!='null' && observacion.length>0) { 
       parent.mainFrame.listaObs[<%=j%>] = true;
    } else { 
       parent.mainFrame.listaObs[<%=j%>] = false; 
    }
	
	if(estadoExpediente=="0"){
		estadoExpediente='<br><font color="blue"><b> Exp. Pendiente  </b><font>';
	}
	if(estadoExpediente=="1"){
		estadoExpediente='<br><font color="brown"><b> Exp. Anulado  </b><font>';
	}
	if(estadoExpediente=="9"){
		estadoExpediente='<br><font color="green"><b> Exp. Finalizado  </b><font>';
	}
        if(estadoExpediente=="null"){
            estadoExpediente='';
        }
   // si es nulo aparece como espacio en blanco    
   tipoDoc = "<%= cp_anotacion.elementAt(1) %>";
   if (tipoDoc == 'null' || tipoDoc == null) tipoDoc = '';
   
    procedimiento = "<%= cp_anotacion.elementAt(17) %>";
   if (procedimiento == 'null' || procedimiento == null) {
       procedimiento = '';
   }
      expediente = "<%= cp_anotacion.elementAt(18)%>";
   if (expediente == 'null' || expediente == null)
	   expediente="";
   else
	   expediente= "<%= cp_anotacion.elementAt(18)%>" + estadoExpediente;
   //para que salga la palabra entera no solo E o S
   if (tipoAnot == 'E'){
       entrSal = 'Entrada';
   }else{
       entrSal = 'Salida'
   } ;
   if("<%= relaciones%>"=='relaciones'){
       //lista para ver el listado de buqeuda de anotaciones en ralaciones       
       parent.mainFrame.lista[<%= z++ %>]  = [estado+ejer+"/"+numAnot,"<%= cp_anotacion.elementAt(5) %>",
           "<%=StringEscapeUtils.escapeJavaScript(nombreFormateado)%>"+" <BR>"+ asunto,
           "<%= cp_anotacion.elementAt(14) %>","<%= cp_anotacion.elementAt(12) %>"];
   }else{
        //lista si el unlistado paginable
        var valorCheck = ejer + DOT_COMMA + numAnot + DOT_COMMA + tipoAnot+ DOT_COMMA + finDigitalizacion;
        var check = '<input type="checkbox" class="checkLinea" onclick="pulsarCheck()" value="'+valorCheck+'" />';	
        parent.mainFrame.lista[<%= j++ %>]  = [check,estado2,procedimiento,expediente,
            "<%= cp_anotacion.elementAt(19) %>",estado+ejer+"/"+numAnot,entrSal,"<%= cp_anotacion.elementAt(5) %>",
            "<%= cp_anotacion.elementAt(6) %>","<%=StringEscapeUtils.escapeJavaScript(nombreFormateado)%>",asunto,
            "<%= cp_anotacion.elementAt(12) %>",entrada];        
   }
   
   
<% 	   }// for
        }

%>
        
        
        var numPagina = "<%= numPagina%>";
	parent.mainFrame.listaSel =parent.mainFrame.lista;
        parent.mainFrame.numRelacionAnotaciones = <%=numAnotaciones%>;
	parent.mainFrame.inicializaLista(numPagina);
}
</script>

</head>
<body onLoad="pleaseWait('off');redirecciona();">

<p>&nbsp;<p>

</body>
</html>
