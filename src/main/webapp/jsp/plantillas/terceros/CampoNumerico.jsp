<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="java.text.MessageFormat"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<!-- VISTA DE CAMPO NUMERICO   -->
<jsp:useBean id="CAMPO_BEAN" scope="request" class="es.altia.agora.technical.EstructuraCampo"/>
<jsp:useBean id="beanVO" scope="request" class="es.altia.agora.technical.CamposFormulario"/>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />

<script type="text/javascript" src="<c:url value='/scripts/formateador.js'/>"></script>
<script>
    var consultando = false;
</script>
<%

  String valor = beanVO.getString(CAMPO_BEAN.getCodCampo());
  if(valor==null) valor="";
  String obligatorio = CAMPO_BEAN.getObligatorio();
  String texto = "";
  String ob = "";
  if("1".equals(obligatorio)) {
    texto = "inputTextoObligatorio";
    ob = "obligatorio";
  } else {
    texto = "inputTexto";
  }
  String read = "";
  String id="";
  String name = "";
  if("true".equals(CAMPO_BEAN.getSoloLectura())) {
    read = "readonly";
  }
  if("SI".equals(CAMPO_BEAN.getBloqueado())) {
      read = "readonly";
    }
  name = CAMPO_BEAN.getCodCampo();
    if (CAMPO_BEAN.getCodTramite() != null) {
        if (CAMPO_BEAN.getOcurrencia() != null)
            name = "T_" + CAMPO_BEAN.getCodTramite() + "_" + CAMPO_BEAN.getOcurrencia() + "_" + CAMPO_BEAN.getCodCampo();
        else
            name = "T_" + CAMPO_BEAN.getCodTramite() + "_" + CAMPO_BEAN.getCodCampo();
    }
    //name = "T" + CAMPO_BEAN.getCodTramite() + CAMPO_BEAN.getCodCampo();
  if(ob.equals("obligatorio")) {
    id = ob;
  } else {
    id = name;
  }
    Log m_log = LogFactory.getLog(this.getClass().getName());
    if(m_log.isDebugEnabled()) m_log.debug("\n\t\t\t VALOR DEL CAMPO = " + valor);
    String consulta = request.getParameter("consulta");
    if(m_log.isDebugEnabled()) m_log.debug("\n\t\t\t POR CONSULTA = " + consulta);
    if ((consulta != null) && (!"".equalsIgnoreCase(consulta))) {
        if ((CAMPO_BEAN.getTamano() != null) && (!"".equalsIgnoreCase(CAMPO_BEAN.getTamano()))) {
          int tamanoConsulta = 2*Integer.parseInt(CAMPO_BEAN.getTamano())+1;
          CAMPO_BEAN.setTamano(tamanoConsulta + ""); // Para que tenga el tamaño justo para poder consultar
        }
%>
      <script> consultando = true;</script>
<%  }

    Config m_Conf = ConfigServiceHelper.getConfig("common");
    String campo="TamMaximo.InputCampoNumerico";
    Integer tamanoMaximo = new Integer(m_Conf.getString(campo));
    int numNuevesPartEntera = tamanoMaximo.intValue()-3;
    String numMaximo = "";
    for (int i=1;i<=numNuevesPartEntera;i++) {
        numMaximo = numMaximo.concat("9");
    }
    numMaximo = numMaximo.concat("'99");
    String mensaje = MessageFormat.format(descriptor.getDescripcion("msjNumMaximoCampoNum"), new Object[] { numMaximo });
%>

<SCRIPT type="text/javascript">

    function SoloDigitosNumericos(e) {


      var tecla, caracter;
      var numeros= "0123456789";

      tecla = (document.all)?e.keyCode:e.which;

      if (tecla == null) return true;
      if ((tecla == 0)|| (tecla == 8)) return true;


      caracter = String.fromCharCode(tecla);

      if (numeros.indexOf(caracter) != -1) {
          return true;
      } else {
      return false;
      }
  }

  function SoloDigitosConsulta(e) {


      var tecla, caracter;
      var numeros= "0123456789&|():><!=";

      tecla = (document.all)?e.keyCode:e.which;

      if (tecla == null) return true;
      if ((tecla == 0)|| (tecla == 8)) return true;

      caracter = String.fromCharCode(tecla);

      if (numeros.indexOf(caracter) != -1) {
          return true;
      } else {
      return false;
      }
  }

function SoloDigitosConsulta(objeto) {      
      xAMayusculas(objeto);
   var tecla, caracter;
      var numeros= "0123456789,&|():><!=";

      if (objeto){
        var original = objeto.value;
        var salida = "";
        for(i=0;i<original.length;i++){
            if(numeros.indexOf(original.charAt(i).toUpperCase())!=-1){
                salida = salida + original.charAt(i);
            }
   }
        objeto.value=salida.toUpperCase();
     }//if
  }


  function SoloDigitosNumericos (objeto){
       xAMayusculas(objeto);
      var numeros= "0123456789,";
       if (objeto){
        var original = objeto.value;
        var salida = "";
        for(i=0;original!=undefined && i<original.length;i++){
            if(numeros.indexOf(original.charAt(i).toUpperCase())!=-1 || numeros.indexOf(original.charAt(i).toUpperCase())!=-1){
                salida = salida + original.charAt(i);
            }
        }
        objeto.value=salida.toUpperCase();
     }//if
}

function validarLongitudNumero(campo) {
    var valor = campo.value;
    longitudvalida = longitudValidaValor(valor);
}

function longitudValidaValor(valor) {
    var devolver = true;
    if (valor != '')
    {
      // si no hay comas
      if (valor.indexOf(",")==-1 && valor.indexOf(":")==-1 && valor.indexOf(">")==-1 && valor.indexOf("<")==-1) {
        // quitar los ceros a la izquierda
        valor = trimLeftZeroes(valor);
        devolver=(valor.length<=<%=numNuevesPartEntera%>);
      }
      // si hay comas
      else  if (valor.indexOf(",")!=-1){
        pos = valor.lastIndexOf(",");
        intPart = valor.substring(0,pos);
        if (intPart==""){
            intPart = "0";
        }
        // quitar los puntos y comas
        intPart = trimPointers(intPart);
        // quitar los ceros a la izquierda
        intPart = trimLeftZeroes(intPart);
        devolver=(intPart.length<=<%=numNuevesPartEntera%>);
      } else if (valor.indexOf(":")!=-1){
          var los2numeros = valor.split(':');
          devolver = longitudValidaValor(los2numeros[0]) && longitudValidaValor(los2numeros[1]);
      } else if (valor.indexOf(">")==-1 || valor.indexOf("<")==-1) {
          devolver = devolver=((valor.length+1)<=<%=numNuevesPartEntera%>);
      }
      if (!devolver) {
        jsp_alerta("A","<%=mensaje%>");
      }
      return devolver;
    }
}

function cambiosCampoSupl(){
if (consultando!=true) modificaVariableCambiosCamposSupl();
}

</SCRIPT>
  <input type="text" name="<%= name %>" value=""
    maxlength="<%= CAMPO_BEAN.getTamano() %>" id="<%= id %>" class="<%= texto %>"
    title="<%= CAMPO_BEAN.getDescCampo() %>"  <%= read %>  onchange="cambiosCampoSupl()"
    size="20" onfocus="javascript:desFormatValorDecimal(this,<%= CAMPO_BEAN.getSoloLectura() %>)"
    onkeypress="javascript:if (consultando) return SoloDigitosConsulta(event); else return SoloDigitosNumericos(event);"
    onBlur="javascript:validarLongitudNumero(this);if (!longitudValida) {document.getElementById('<%=name%>').value = '';focus(this);}">
<!-- FIN VISTA DE CAMPO NUMERICO   -->
<script type="text/javascript">
var longitudValida=true;
var valorCampo = formatValorDecimal('<%=valor%>');
document.getElementsByName("<%=name%>")[0].value = valorCampo;
</script>
