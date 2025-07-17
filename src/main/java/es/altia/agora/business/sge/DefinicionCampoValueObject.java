package es.altia.agora.business.sge;

import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import java.io.Serializable;
import java.util.Vector;

public class DefinicionCampoValueObject implements Serializable, ValueObject {

    /** Construye un nuevo DefinicionCampo por defecto. */
    public DefinicionCampoValueObject() {
        super();
    }

public String getCodCampo() {
  return codCampo;
}
public void setCodCampo(String codCampo) {
  this.codCampo = codCampo;
}
public String getCodPlantilla() {
  return codPlantilla;
}
public void setCodPlantilla(String codPlantilla) {
  this.codPlantilla = codPlantilla;
}
public String getCodTipoDato() {
  return codTipoDato;
}
public void setCodTipoDato(String codTipoDato) {
  this.codTipoDato = codTipoDato;
}
public String getDescCampo() {
  return descCampo;
}
public void setDescCampo(String descCampo) {
  this.descCampo = descCampo;
}
public String getDescPlantilla() {
  return descPlantilla;
}
public void setDescPlantilla(String descPlantilla) {
  this.descPlantilla = descPlantilla;
}
public String getDescTipoDato() {
  return descTipoDato;
}
public void setDescTipoDato(String descTipoDato) {
  this.descTipoDato = descTipoDato;
}
public String getDescMascara() {
  return descMascara;
}
public void setDescMascara(String descMascara) {
  this.descMascara = descMascara;
}
public String getObligat() {
  return obligat;
}
public void setObligat(String obligat) {
  this.obligat = obligat;
}
public String getOrden() {
  return orden;
}
public void setOrden(String orden) {
  this.orden = orden;
}
public String getTamano() {
  return tamano;
}
public void setTamano(String tamano) {
  this.tamano = tamano;
}
public String getVisible() {
  return visible;
}
public void setVisible(String visible) {
  this.visible = visible;
}
public String getActivo() {
  return activo;
}
public void setActivo(String activo) {
  this.activo = activo;
}
public String getRotulo() {
  return rotulo;
}
public void setRotulo(String rotulo) {
  this.rotulo = rotulo;
}

public String getOculto() {
    return oculto;
}

public void setOculto(String oculto) {
    this.oculto = oculto;
}

public String getBloqueado() {
    return bloqueado;
}

public void setBloqueado(String bloqueado) {
    this.bloqueado = bloqueado;
}

public String getPlazoFecha() {
    return plazoFecha;
}

public void setPlazoFecha(String plazoFecha) {
    this.plazoFecha = plazoFecha;
}

public String getCheckPlazoFecha() {
    return checkPlazoFecha;
}

public void setCheckPlazoFecha(String checkPlazoFecha) {
    this.checkPlazoFecha = checkPlazoFecha;
}

public String getAlto() {
    return alto;
}
public void setAlto(String alto) {
    this.alto = alto;
}

public String getCodAgrupacion() {
    return codAgrupacion;
}
public void setCodAgrupacion(String codAgrupacion) {
    this.codAgrupacion = codAgrupacion;
}

public String getDescAgrupacion() {
    return descAgrupacion;
}
public void setDescAgrupacion(String descAgrupacion) {
    this.descAgrupacion = descAgrupacion;
}

public boolean isIsValid() {
    return isValid;
}
public void setIsValid(boolean isValid) {
    this.isValid = isValid;
}

public String getPosX() {
    return posX;
}
public void setPosX(String posX) {
    this.posX = posX;
}

public String getPosY() {
    return posY;
}
public void setPosY(String posY) {
    this.posY = posY;
}

public String getTamanho() {
    return tamanho;
}
public void setTamanho(String tamanho) {
    this.tamanho = tamanho;
}




public Vector getListaPlantillas() {
  return listaPlantillas;
}
public void setListaPlantillas(Vector listaPlantillas) {
  this.listaPlantillas = listaPlantillas;
}
public Vector getListaTipoDato() {
  return listaTipoDato;
}
public void setListaTipoDato(Vector listaTipoDato) {
  this.listaTipoDato = listaTipoDato;
}
public Vector getListaRelacionTipoDatoPlantillas() {
  return listaRelacionTipoDatoPlantillas;
}
public void setListaRelacionTipoDatoPlantillas(Vector listaRelacionTipoDatoPlantillas) {
  this.listaRelacionTipoDatoPlantillas = listaRelacionTipoDatoPlantillas;
}
public Vector getListaMascaras() {
  return listaMascaras;
}
public void setListaMascaras(Vector listaMascaras) {
  this.listaMascaras = listaMascaras;
}
public Vector getListaRelacionTipoDatoMascaras() {
  return listaRelacionTipoDatoMascaras;
}
public void setListaRelacionTipoDatoMascaras(Vector listaRelacionTipoDatoMascaras) {
  this.listaRelacionTipoDatoMascaras = listaRelacionTipoDatoMascaras;
}

public Vector getListaCamposDesplegables() {
    return listaCamposDesplegables;
}

public void setListaCamposDesplegables(Vector listaCamposDesplegables) {
    this.listaCamposDesplegables = listaCamposDesplegables;
}

public Vector getListaCamposDesplegablesExterno() {
    return listaCamposDesplegablesExterno;
}

public void setListaCamposDesplegablesExterno(Vector listaCamposDesplegablesExterno) {
    this.listaCamposDesplegablesExterno = listaCamposDesplegablesExterno;
}

public Vector getListaAgrupaciones() {
    return listaAgrupaciones;
}
public void setListaAgrupaciones(Vector listaAgrupaciones) {
    this.listaAgrupaciones = listaAgrupaciones;
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

    private String codCampo;
    private String descCampo;
    private String codPlantilla;
    private String descPlantilla;
    private String codTipoDato;
    private String descTipoDato;
    private String tamano;
    private String descMascara;
    private String obligat;
    private String orden;
    private String visible;
    private String activo;
    private String rotulo;
    private String oculto;
    private String bloqueado;
    private String Validacion;    
    private String plazoFecha;
    private String checkPlazoFecha;	 
    private String CodTramite;
    private String Operacion;    
    private String codAgrupacion;
    private String descAgrupacion;
    private String tamanho;
    private String posX;
    private String posY;
    private String alto;

    private Vector listaCamposDesplegables;
    private Vector listaCamposDesplegablesExterno;
    private Vector listaPlantillas;
    private Vector listaTipoDato;
    private Vector listaRelacionTipoDatoPlantillas;
    private Vector listaMascaras;
    private Vector listaRelacionTipoDatoMascaras;
    private Vector listaCampos;
    private Vector listaAgrupaciones;
    
    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;

    /**
     * @return the Validacion
     */
    public String getValidacion() {
        return Validacion;
    }

    /**
     * @param Validacion the Validacion to set
     */
    public void setValidacion(String Validacion) {
        this.Validacion = Validacion;
    }

    /**
     * @return the CodTramite
     */
    public String getCodTramite() {
        return CodTramite;
    }

    /**
     * @param CodTramite the CodTramite to set
     */
    public void setCodTramite(String CodTramite) {
        this.CodTramite = CodTramite;
    }

    /**
     * @return the Operacion
     */
    public String getOperacion() {
        return Operacion;
    }

    /**
     * @param Operacion the Operacion to set
     */
    public void setOperacion(String Operacion) {
        this.Operacion = Operacion;
    }

    /**
     * @return the listaCampos
     */
    public Vector getListaCampos() {
        return listaCampos;
    }

    /**
     * @param listaCampos the listaCampos to set
     */
    public void setListaCampos(Vector listaCampos) {
        this.listaCampos = listaCampos;
    }
}
