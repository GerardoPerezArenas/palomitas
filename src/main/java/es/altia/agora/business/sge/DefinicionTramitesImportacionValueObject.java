/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.sge;

import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import es.altia.technical.ValueObject;
import java.io.Serializable;

/**
 *
 * @author MilaNP
 */
public class DefinicionTramitesImportacionValueObject implements Serializable, ValueObject {
    private String codProcedimientoBiblioteca;
    private int codTramiteBiblioteca;
    private String codProcedimientoDest;
    private int codTramiteDest;
    private int codMunicipio;
    private DefinicionTramitesValueObject defTramVO;
    private boolean isValid;

    public DefinicionTramitesImportacionValueObject() {
        super();
    }

    /**
     * Valida el estado de esta RegistroSaida
     * Puede ser invocado desde la capa cliente o desde la capa de negocio
     * @exception ValidationException si el estado no es válido
     */
    public void validate(String idioma) throws ValidationException {
        String sufijo = "";
        if ("euskera".equals(idioma)) sufijo="_eu";
        boolean correcto = true;
        Messages errors = new Messages();

        if (!errors.empty())
            throw new ValidationException(errors);
        isValid = true;
    }

    /** Devuelve un booleano que representa si el estado de este RegistroSaida es válido. */
    public boolean IsValid() { return isValid; }

    /**
     * @return the codProcesoBiblioteca
     */
    public String getCodProcedimientoBiblioteca() {
        return this.codProcedimientoBiblioteca;
    }

    /**
     * @param codProcesoBiblioteca the codProcesoBiblioteca to set
     */
    public void setCodProcedimientoBiblioteca(String codProcBiblioteca) {
        this.codProcedimientoBiblioteca = codProcBiblioteca;
    }

    /**
     * @return the codMunicipio
     */
    public int getCodMunicipio() {
        return codMunicipio;
    }

    /**
     * @param codMunicipio the codMunicipio to set
     */
    public void setCodMunicipio(int codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    /**
     * @return the dTVO
     */
    public DefinicionTramitesValueObject getDefTramVO() {
        return defTramVO;
    }

    /**
     * @param dTVO the dTVO to set
     */
    public void setDefTramVO(DefinicionTramitesValueObject dTVO) {
        this.defTramVO = dTVO;
    }

    /**
     * @return the codTramiteBiblioteca
     */
    public int getCodTramiteBiblioteca() {
        return codTramiteBiblioteca;
    }

    /**
     * @param codTramiteBiblioteca the codTramiteBiblioteca to set
     */
    public void setCodTramiteBiblioteca(int codTramiteBiblioteca) {
        this.codTramiteBiblioteca = codTramiteBiblioteca;
    }

    /**
     * @return the codProcesoDest
     */
    public String getCodProcedimientoDest() {
        return this.codProcedimientoDest;
    }

    /**
     * @param codProcesoDest the codProcesoDest to set
     */
    public void setCodProcedimientoDest(String codProcDest) {
        this.codProcedimientoDest = codProcDest;
    }

    /**
     * @return the codTramiteDest
     */
    public int getCodTramiteDest() {
        return codTramiteDest;
    }

    /**
     * @param codTramiteDest the codTramiteDest to set
     */
    public void setCodTramiteDest(int codTramiteDest) {
        this.codTramiteDest = codTramiteDest;
    }
    
}
