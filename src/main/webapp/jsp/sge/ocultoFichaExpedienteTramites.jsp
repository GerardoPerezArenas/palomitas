<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.Vector"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionExpedientesForm" %>
<%@page import="es.altia.agora.business.sge.TramitacionExpedientesValueObject" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.technical.EstructuraCampo"%>
<%@ page import="java.util.Collection"%>
<%@ page import="es.altia.agora.technical.CamposFormulario"%>





<%Log mLog = LogFactory.getLog(this.getClass().getName());
  String fechaFinExpediente = "";
  int idioma=1;
  int apl=1;
  String[] params=null;
  TramitacionExpedientesForm tramExpForm=new TramitacionExpedientesForm();

    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
      tramExpForm = (TramitacionExpedientesForm)session.getAttribute("TramitacionExpedientesForm");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
          params = usuario.getParamsCon();
        }
   
  }
        
  fechaFinExpediente = tramExpForm.getFechaFin();
  String codTramite=tramExpForm.getCodTramite();
  String titular=tramExpForm.getTitular(); 
  String accion=tramExpForm.getAccion(); 
  String fecha=tramExpForm.getFechaFin();
  String bloquear=tramExpForm.getBloqueo();
   mLog.debug("codTramite =" + codTramite+ "-----"); 
   mLog.debug("titular =" + titular+ "-----"); 
   mLog.debug("accion =" + accion+ "-----"); 
   mLog.debug("fechaFinExpediente =" + fechaFinExpediente+ "-----"); 
   mLog.debug("fecha =" + fecha+ "-----"); 
   mLog.debug("bloquear =" + bloquear+ "-----"); 


    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String campos="";
    int entro=0;

	Vector estructuraDatosSuplementariosAux = null;
	Vector valoresDatosSuplementariosAux = null;
    estructuraDatosSuplementariosAux = (Vector) tramExpForm.getEstructuraDatosSuplementarios();
      valoresDatosSuplementariosAux = (Vector) tramExpForm.getValoresDatosSuplementarios();
        if (estructuraDatosSuplementariosAux == null) {entro=1;}     

      // Contador de campos suplementarios obligatorios
      int contadorNumObligatorios = 0;
      // Contador de valores de campos suplementarios obligatorios
      int contadorNumValoresObligatorios = 0;

      if (estructuraDatosSuplementariosAux != null) {
         int lengthEstructuraDatosSuplementariosAux = estructuraDatosSuplementariosAux.size();
        int lengthValoresDatosSuplementariosAux = valoresDatosSuplementariosAux.size();
	 if (lengthEstructuraDatosSuplementariosAux  == 0) {entro=1;}
        if (lengthEstructuraDatosSuplementariosAux>0 ) {
           
            for (int i=0;i<lengthEstructuraDatosSuplementariosAux;i++)
            {
                // Se cuenta el número de campos obligatorios y el número de campos obligatorios con valor.
                // 1.- Si el nº de obligatorios = campos con valor => Se puede finalizar el trámite
                // 2.- Si el nº de obligatorios != nº campos con valor => No se puede finalizar
                EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementariosAux.elementAt(i);
                CamposFormulario cF = (CamposFormulario) valoresDatosSuplementariosAux.elementAt(i);
                String nombre = eC.getCodCampo();
                String valor = cF.getString(nombre);
                String obligatorio = eC.getObligatorio();
                 String activo=eC.getActivo();
                   mLog.info("campo activo: "+activo);
                    if (!"NO".equals(activo)){

                        if(obligatorio!=null && "1".equals(obligatorio)){
                            // Se cuenta el número de campos obligatorios
                            contadorNumObligatorios++;
                        }

                        if(obligatorio!=null && "1".equals(obligatorio) && valor!=null && !"".equals(valor)){
                            // Se cuenta el número de campos obligatorios con valor !=null
                            contadorNumValoresObligatorios++;
                        }
                    }
            }// for
         }
      }

      // Sólo si el contador de campos obligatorios se corresponde con el
      // contador de valores de campos suplementarios obligatorios => Se han cumplimentado todos los campos suplementarios
      // de tipo obligatorio del trámite => Se puede finalizar el trámite.
      if(contadorNumObligatorios==contadorNumValoresObligatorios)
          entro = 1;
     

    int validarFirma=1;
    Vector documentos=null;
 documentos=tramExpForm.getListaDocumentos();
          //Vector documentos=tramExpVO.getListaDocumentos();
          if(documentos!=null){
           for(int z=0;z<documentos.size();z++){
               String codDocumento=((TramitacionExpedientesValueObject)documentos.elementAt(z)).getCodDocumento();
               String firma=((TramitacionExpedientesValueObject)documentos.elementAt(z)).getEstadoFirma();
               mLog.debug("......documentos... "+codDocumento);
               mLog.debug("......firma... "+firma);
              if(firma==null){
              	   validarFirma=1;
              }else if(firma.equals("T")||firma.equals("O")||firma.equals("E")||firma.equals("L")||firma.equals("U")){
                validarFirma=0;
               }
            }
	}
 %>
<script>
var frame;
      if(parent.mainFrame){
        frame = parent.mainFrame;
      } else {
        frame = parent;
      }

</script>
 <% if(entro==0){ %> 
<script>
frame.procesoFinalizar("<%=entro%>","<%=validarFirma%>","","","","","","");

</script>
<%}else if(validarFirma==0){%>
<script>
frame.procesoFinalizar("<%=entro%>","<%=validarFirma%>","","","","","","","","");



</script>
<%}else{%>

<script>
frame.procesoFinalizar("<%=entro%>","<%=validarFirma%>","<%=tramExpForm.getAccion()%>","<%=tramExpForm.getObligat()%>","<%=tramExpForm.getObligatorioDesf()%>"
,"<%=tramExpForm.getAccionAfirmativa()%>","<%=tramExpForm.getAccionNegativa()%>","<%=tramExpForm.getPregunta()%>","<%=tramExpForm.getFechaFin()%>","<%=tramExpForm.getBloqueo()%>");
</script>
<%}%>