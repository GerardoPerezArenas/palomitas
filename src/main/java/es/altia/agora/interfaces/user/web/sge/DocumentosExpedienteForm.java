package es.altia.agora.interfaces.user.web.sge;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.*;
import org.apache.struts.upload.FormFile;


public class DocumentosExpedienteForm extends ActionForm{

   protected static Log log =
           LogFactory.getLog(DocumentosExpedienteForm.class.getName());
   private String codMunicipio;
   private String codProcedimiento;
   private String ejercicio;
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
   private String fechaInforme;
   private String fechaDocumento;
   private Map campos=new HashMap();
   

   public void setCodMunicipio(String municipio) { this.codMunicipio = municipio; }
   public String getCodMunicipio() { return this.codMunicipio; }
   
   public void setCodProcedimiento(String codproc) { this.codProcedimiento = codproc; }
   public String getCodProcedimiento() { return this.codProcedimiento; }
   
   public void setEjercicio(String eje) { this.ejercicio = eje; }
   public String getEjercicio() { return this.ejercicio; }

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

    public Map getCampos() {
        return campos;
    }

    public void setCampos(Map campos) {
        this.campos = campos;
    }

   
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

    /**
     * @return the fechaInforme
     */
    public String getFechaInforme() {
        return fechaInforme;
    }

    /**
     * @param fechaInforme the fechaInforme to set
     */
    public void setFechaInforme(String fechaInforme) {
        this.fechaInforme = fechaInforme;
    }
    
    public String getFechaDocumento() {
        return fechaDocumento;
    }

    public void setFechaDocumento(String fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }
    
    
 
   
    
     public String toString() {
        String sc = "\n"; // salto de carro

        StringBuffer resultado = new StringBuffer("");

        resultado.append("codMunicipio: " + getCodMunicipio() + sc);
        resultado.append("codProcedimiento: " + getCodProcedimiento() + sc);
        resultado.append("Ejercicio: " + getEjercicio() + sc);
        resultado.append("NumeroExpediente: " + getNumeroExpediente() + sc);
        resultado.append("CodTramite: " + getCodTramite() + sc);
        resultado.append("ocurrenciaTramite: " + getOcurrenciaTramite() + sc);
        resultado.append("CodDocumento: " + getCodDocumento() + sc);
        resultado.append("CodUduario: " + getCodUsuario() + sc);
        resultado.append("CodPlantilla: " + getCodPlantilla() + sc);
        resultado.append("NombreDocumento: " + getNombreDocumento() + sc);
        resultado.append("NumeroDocumento: " + getNumeroDocumento() + sc);
        resultado.append("Opcion: " + getOpcion() + sc);
        resultado.append("FechaInforme: " + getFechaInforme() + sc);
        


        return resultado.toString();
    }

    
}
