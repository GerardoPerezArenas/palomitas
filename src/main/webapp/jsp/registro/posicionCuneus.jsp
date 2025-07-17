<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>
<html>
<head>
  <title>Posición Cuneus</title>
  <meta http-equiv="Content-Type" content="text/html;	charset=iso-8859-1">
  <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css">
  <%
    UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int idioma=1;
    int apl=1;
    if (session.getAttribute("usuario") != null){
		usuarioVO =	(UsuarioValueObject)session.getAttribute("usuario");
		apl =	usuarioVO.getAppCod();
		idioma = usuarioVO.getIdioma();
    }
      Config m_Conf = ConfigServiceHelper.getConfig("common");
      String numeroCopias = m_Conf.getString("Registro.copias");
	%>

  <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"	type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
  <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>"	/>
  <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
  <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />

  <script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
  <script type="text/javascript">

    function pulsarAceptar(){
      var nCopias = document.forms[0].copias.value;
      if (nCopias != "") {
        if (nCopias !="0") {
	  var p = document.forms[0].posicionCuneus[0].value;
	  if (document.forms[0].posicionCuneus[1].checked){
	  	p = document.forms[0].posicionCuneus[1].value;
	  } else if (document.forms[0].posicionCuneus[2].checked){
	  	p = document.forms[0].posicionCuneus[2].value;
	  } if (document.forms[0].posicionCuneus[3].checked){
	  	p = document.forms[0].posicionCuneus[3].value;
	  } if (document.forms[0].posicionCuneus[4].checked){
	  	p = document.forms[0].posicionCuneus[4].value;
	  } if (document.forms[0].posicionCuneus[5].checked){
	  	p = document.forms[0].posicionCuneus[5].value;
	  }
      var i = '<%=idioma%>';
	  var r = new Array();
	  r[0]=i
	  r[1]=p;
          r[2]=nCopias;
	  self.parent.opener.retornoXanelaAuxiliar(r);
        } else {
            jsp_alerta("A", '<%=descriptor.getDescripcion("msjAlertCopias")%>')
        }
      } else {
          jsp_alerta("A", '<%=descriptor.getDescripcion("msjObligTodos")%>');
      }
    }

    function pulsarCancelar(){
		self.parent.opener.retornoXanelaAuxiliar();
    }

  </script>
</head>

<body class="bandaBody">

<form method="post">

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titCuneus")%></div>
<div class="contenidoPantalla">            
    <table width="100%">
        <tr>
            <td height="180px" >
                <table style="width:100%;border-top: #666666 1px solid; border-bottom: #666666 1px solid;border-left: #666666 1px solid;border-right: #666666 1px solid;">
                    <tr>
                        <td>
                            <table width="100%" height="160px" border="0px" cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td class="etiqueta">
                                        <input type="radio" name="posicionCuneus" value="ARRIBAIZQA"  class="textoSuelto" > <%=descriptor.getDescripcion("posCuneusSI")%>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="etiqueta">
                                        <input type="radio" name="posicionCuneus" CHECKED value="ARRIBACENTRO"  class="textoSuelto" > <%=descriptor.getDescripcion("posCuneusSC")%>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="etiqueta">
                                        <input type="radio" name="posicionCuneus" value="ARRIBADCHA" class="textoSuelto"> <%=descriptor.getDescripcion("posCuneusSD")%>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="etiqueta">
                                        <input type="radio" name="posicionCuneus" value="ABAJOIZQA" class="textoSuelto"> <%=descriptor.getDescripcion("posCuneusII")%>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="etiqueta">
                                        <input type="radio" name="posicionCuneus" value="ABAJOCENTRO" class="textoSuelto"> <%=descriptor.getDescripcion("posCuneusIC")%>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="etiqueta">
                                        <input type="radio" name="posicionCuneus" value="ABAJODCHA" class="textoSuelto"> <%=descriptor.getDescripcion("posCuneusID")%>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <TR>
            <TD>
                <TABLE width="50%" cellspacing="0px" cellpadding="0px" border="0px">
                    <TR>
                        <TD width="10%"></TD>
                        <TD width="55%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqNumCopias")%>:</TD>
                        <TD width="35%" class="etiqueta">
                            <input type="text" name="copias" class="inputTextoObligatorio" style="width:50;text-align:center" maxlength="20" tabindex="-1" value="<%=numeroCopias%>" onkeyup = "return SoloDigitosNumericos(this);"/>
                        </TD>
                    </TR>
                </TABLE>
            </td>
        </tr>
    </table>
    <div class="botoneraPrincipal">
        <input name="botonAceptar" type="button" class="botonGeneral" id="botonAceptar" accesskey="A" value="<%=descriptor.getDescripcion("gbAceptar")%>" onclick="pulsarAceptar();">
        <input name="botonCancelar"	type="button" class="botonGeneral" id="botonCancelar" accesskey="S" value="<%=descriptor.getDescripcion("gbCancelar")%>" onclick="pulsarCancelar();">                                                       
    </div>
</div>
</form>

</body>
</html>
