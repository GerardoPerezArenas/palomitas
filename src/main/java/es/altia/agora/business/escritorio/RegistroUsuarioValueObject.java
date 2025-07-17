package es.altia.agora.business.escritorio;

import java.io.Serializable;

import es.altia.technical.ValidationException;
import es.altia.technical.ValueObject;

public class RegistroUsuarioValueObject implements Serializable, ValueObject {
	
    private int depCod;	
    private int unidadOrgCod;
    private String unidadOrg;
    private int codOficinaRegistro;
    private boolean unidadOrganicaDigit;

    public RegistroUsuarioValueObject() {
        super();
    }	
    
    public RegistroUsuarioValueObject(int depCod, int unidadOrgCod, String unidadOrg) {
        this.depCod = depCod;
        this.unidadOrgCod = unidadOrgCod;
        this.unidadOrg = unidadOrg;
        this.codOficinaRegistro = codOficinaRegistro;
    }

    public RegistroUsuarioValueObject(int depCod, int unidadOrgCod, String unidadOrg, int codOficinaRegistro) {
        this.depCod = depCod;
        this.unidadOrgCod = unidadOrgCod;
        this.unidadOrg = unidadOrg;
        this.codOficinaRegistro = codOficinaRegistro;
    }
   
  public RegistroUsuarioValueObject(int depCod, int unidadOrgCod, String unidadOrg, int codOficinaRegistro, boolean unidadOrganicaDigit){
        this.depCod = depCod;
        this.unidadOrgCod = unidadOrgCod;
        this.unidadOrg = unidadOrg;
        this.codOficinaRegistro = codOficinaRegistro;
        this.unidadOrganicaDigit = unidadOrganicaDigit;
    }
    public int getDepCod() { 
        return depCod; 
    }
    public void setDepCod(int depCod) { 
        this.depCod = depCod; 
    }
	
    public int getUnidadOrgCod() { 
        return unidadOrgCod; 
    }
    public void setUnidadOrgCod(int unidadOrgCod) { 
        this.unidadOrgCod = unidadOrgCod; 
    }

    public String getUnidadOrg() { 
        return unidadOrg; 
    }
    public void setUnidadOrg(String unidadOrg) { 
        this.unidadOrg = unidadOrg; 
    }

    public void copy(RegistroUsuarioValueObject other) {
            this.depCod = other.depCod;	
            this.unidadOrgCod = other.unidadOrgCod;
            this.unidadOrg = other.unidadOrg;
            this.codOficinaRegistro = other.codOficinaRegistro;
            this.unidadOrganicaDigit = other.unidadOrganicaDigit;
    }

    public void validate(String idioma) throws ValidationException {
    }
        
    public int getCodOficinaRegistro() {
        return codOficinaRegistro;
    }//getCodOficinaRegistro
    public void setCodOficinaRegistro(int codOficinaRegistro) {
        this.codOficinaRegistro = codOficinaRegistro;
    }//setCodOficinaRegistro

    public boolean isUnidadOrganicaDigit() {
        return unidadOrganicaDigit;
    }

    public void setUnidadOrganicaDigit(boolean unidadOrganicaDigit) {
        this.unidadOrganicaDigit = unidadOrganicaDigit;
    }
    
    
}//class
