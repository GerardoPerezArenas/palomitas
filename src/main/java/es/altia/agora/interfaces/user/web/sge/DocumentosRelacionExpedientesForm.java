package es.altia.agora.interfaces.user.web.sge;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.*;
import org.apache.struts.upload.FormFile;


public class DocumentosRelacionExpedientesForm extends ActionForm{

   protected static Log log =
           LogFactory.getLog(DocumentosRelacionExpedientesForm.class.getName());
   private String codMunicipio;
   private String codProcedimiento;
   private String ejercicio;
   private String numeroRelacion;
   private String numeroExpediente;
   private String codTramite;
   private String ocurrenciaTramite;
   private String codDocumento;
   private String codUsuario;
   private String codPlantilla;
   private String nombreDocumento;
   private String numeroDocumento;
   private String opcion;
   private String datosXML;
   private FormFile ficheroWord;
   private String resultado;
   private String listaCodInteresados;
   private String listaVersInteresados;
   private String opcionGrabar;
   private String tipoPlantilla;
   private HashMap mapa;

    public HashMap getMapa() {
        return mapa;
    }

    public void setMai(HashMap mapa) {
        this.mapa = mapa;
    }
   
   

   public void setCodMunicipio(String municipio) { this.codMunicipio = municipio; }
   public String getCodMunicipio() { return this.codMunicipio; }
   
   public void setCodProcedimiento(String codproc) { this.codProcedimiento = codproc; }
   public String getCodProcedimiento() { return this.codProcedimiento; }
   
   public void setEjercicio(String eje) { this.ejercicio = eje; }
   public String getEjercicio() { return this.ejercicio; }

   public void setNumeroRelacion(String numRel) { this.numeroRelacion = numRel; }
   public String getNumeroRelacion() { return this.numeroRelacion; }

   public void setNumeroExpediente(String numExp) { this.numeroExpediente = numExp; }
   public String getNumeroExpediente() { return this.numeroExpediente; }

   public void setCodTramite(String codtramite) { this.codTramite = codtramite; }
   public String getCodTramite() { return this.codTramite; }
   
   public void setOcurrenciaTramite(String occtramite) { this.ocurrenciaTramite = occtramite; }
   public String getOcurrenciaTramite() { return this.ocurrenciaTramite; }

   public void setCodDocumento(String codigoDocumento) {this.codDocumento = codigoDocumento;}
   public String getCodDocumento() { return(this.codDocumento); }
   
   public void setCodUsuario(String codigoUsuario) {this.codUsuario = codigoUsuario;}
   public String getCodUsuario() { return(this.codUsuario); }
   
   public void setCodPlantilla(String codigoPlantilla) {this.codPlantilla = codigoPlantilla;}
   public String getCodPlantilla() { return(this.codPlantilla); }
   
   public void setNombreDocumento(String nombre) {this.nombreDocumento = nombre;}
   public String getNombreDocumento() { return(this.nombreDocumento); }
   
   public void setNumeroDocumento(String numero) {this.numeroDocumento = numero;}
   public String getNumeroDocumento() { return(this.numeroDocumento); }   
   
   public void setDatosXML(String datos) {this.datosXML = datos;}
   public String getDatosXML() { return(this.datosXML); }

   public FormFile getFicheroWord() { return ficheroWord; }
   public void setFicheroWord(FormFile ficheroWord) { this.ficheroWord = ficheroWord; }

   public void setOpcion(String opcion) {this.opcion = opcion;}
   public String getOpcion() { return(this.opcion); }
   
   public void setResultado(String resultado) {this.resultado = resultado;}
   public String getResultado() { return(this.resultado); }
   
   public void setListaCodInteresados(String listaCodInteresados) {this.listaCodInteresados = listaCodInteresados;}
   public String getListaCodInteresados() { return(this.listaCodInteresados); }
   
   public void setListaVersInteresados(String listaVersInteresados) {this.listaVersInteresados = listaVersInteresados;}
   public String getListaVersInteresados() { return(this.listaVersInteresados); }

   public void setOpcionGrabar(String opcionGrabar) {this.opcionGrabar = opcionGrabar;}
   public String getOpcionGrabar() { return(this.opcionGrabar); }

   public void setTipoPlantilla(String tipoPlantilla) {this.tipoPlantilla = tipoPlantilla;}
   public String getTipoPlantilla() { return(this.tipoPlantilla); }

   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
   	log.debug("validate");
   	ActionErrors errors = new ActionErrors();
   	return errors;
   }

  /* Función que procesa los errores de validación a formato struts */
   private ActionErrors validationException(ValidationException ve,ActionErrors errors){
   	Iterator iter = ve.getMessages().get();
   	while (iter.hasNext()) {
      Message message = (Message)iter.next();
      errors.add(message.getProperty(), new ActionError(message.getMessageKey()));
   	}
   	return errors;
   }
}