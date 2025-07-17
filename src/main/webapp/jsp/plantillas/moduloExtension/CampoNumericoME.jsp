<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="java.text.MessageFormat"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<!-- VISTA DE CAMPO NUMERICO   -->
<jsp:useBean id="CAMPO_BEAN" scope="request"  class="es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.EstructuraCampoModuloIntegracionVO"/>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />

<script type="text/javascript" src="<c:url value='/scripts/formateador.js'/>"></script>

<%

  String valor="";
  String texto = "inputTexto";
  String read = "";
  String id="";
  String name = "";
 
  name = CAMPO_BEAN.getCodCampo();
  id=name;
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

   
  function formatNumero(obj){
       
        var valor = obj.value;
        
        if (valor != ""){
        
                var valid = "0123456789,.";
                var allValid = true;
                for (n=0;  n<valor.length; n++){
                        for (m=0;  m<valid.length; m++){
                                if (valor.charAt(n) == valid.charAt(m)){
                                        break;
                                }//for (m=0;  m<valid.length; m++)
                        }//for (n=0;  n<valor.length; n++)
                        if (m == valid.length){
                                allValid = false;
                                break;
                        }//if (m == valid.length)
                }//for (n=0;  n<valor.length; n++)
                
                if (!allValid){
                        return;
                }//if (!allValid)
                // si no hay puntos ni comas
                if ((valor.indexOf(".")==-1) && (valor.indexOf(",")==-1)){
                        //obj.value = processIntPart(valor);
                        var dato = processIntPart(valor);
                        if(dato.indexOf(",")==-1){
                            dato = dato + ",00";
                        }
                        
                        obj.value = dato; 
                                                
                        return;
                }//if ((valor.indexOf(".")==-1) && (valor.indexOf(",")==-1))
                // si hay punto pero no hay coma
                if ((valor.indexOf(".")!=-1) && (valor.indexOf(",")==-1)){
                    
                        
                        pos = valor.lastIndexOf(".");
                        intPart = processIntPart(valor.substring(0,pos));
                        decPart = processDecPart(valor.substring(pos+1,valor.length));
                        obj.value = intPart+ ","+ decPart;
                        return;
                        
                       //return;
                }//if ((valor.indexOf(".")!=-1) && (valor.indexOf(",")==-1))
                // si hay coma pero no hay punto
                if ((valor.indexOf(".")==-1) && (valor.indexOf(",")!=-1)){
                    
                        pos = valor.lastIndexOf(",");
                        intPart = processIntPart(valor.substring(0,pos));
                        decPart = processDecPart(valor.substring(pos+1,valor.length));
                        obj.value = intPart+ ","+ decPart;
                    
                        return;
                }//if ((valor.indexOf(".")==-1) && (valor.indexOf(",")!=-1))
                // si hay punto pero no hay coma
                if ((valor.indexOf(".")!=-1) && (valor.indexOf(",")==-1)){
                    
                        /*
                        pos = valor.lastIndexOf(".");
                        intPart = processIntPart(valor.substring(0,pos));
                        if (valor.substring(pos+1,valor.length) != "00"){
                                decPart = processDecPart(valor.substring(pos+1,valor.length));
                                obj.value =  intPart+ ","+ decPart;
                        }else{
                                obj.value =  intPart;
                        }//if (valor.substring(pos+1,valor.length) != "00")
                        */
                       return;
                }//if ((valor.indexOf(".")!=-1) && (valor.indexOf(",")==-1))
                // si hay puntos y comas
                if ((valor.indexOf(".")!=-1) && (valor.indexOf(",")!=-1)){
                    
                        posComa = valor.lastIndexOf(",");
                        posPunt = valor.lastIndexOf(".");
                        pos = posComa>posPunt? posComa:posPunt;
                        intPart = processIntPart(valor.substring(0,pos));
                        decPart = processDecPart(valor.substring(pos+1,valor.length));
                        obj.value = intPart+ ","+ decPart;
                    
                        return;
                }//if ((valor.indexOf(".")!=-1) && (valor.indexOf(",")!=-1))
        }//if (valor != "")
    }//function formatNumero(obj)
   
    
   //Función que comprueba que solo se introduzca un número con parte decimal de dos digitos y separador ","
    //Pensada para llamar al onKeyPress de un campo de texto pasándole this y event como parámetro 
    function soloDecimal(campo, e){
            key = e.keyCode ? e.keyCode : e.which // backspace
            if(key != 9){
                    var expRegDecimal = /^\d+[.]{0,1}[\d]{0,2}$/;
                    while (campo.value.length > 0 && expRegDecimal.test(campo.value)!=true ){
                            campo.value = campo.value.substring(0,campo.value.length-1);		
                    }//while (campo.value.length > 0 && expRegDecimal.test(campo.value)!=true )
            }//if(key != 9)
    }//soloDecimal
     

function SoloDigitosNumericosMai (objeto){
    if(objeto.value!=null){
      
      if (!/^-?(\d*|\d+\.?\d{0,2})$/.test(objeto.value)) {                      
        objeto.value =" ";
     }
    }
}







</SCRIPT>
  <input type="text" name="<%= name %>" value=""
    maxlength="<%= CAMPO_BEAN.getTamanho() %>" id="<%= id %>" class="<%= texto %>"
    title="<%= CAMPO_BEAN.getRotulo() %>"  <%= read %>  
    size="20"
   
    onblur="formatNumero(this);"
    onkeyup="soloDecimal(this,event);">
  

<script type="text/javascript">

</script>
