// NOMBRE DEL PAQUETE
package es.altia.agora.business.terceros;

// PAQUETES IMPORTADOS
import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import java.io.Serializable;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase DomicilioSimpleValueObject</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class DomicilioSimpleValueObject implements Serializable, ValueObject {

  private boolean isValid;
  private String idDomicilio="";
  private String idDomicilioAntiguo="";
  private String idPais="";
  private String idProvincia="";
  private String idMunicipio="";
  private String pais="";
  private String provincia="";
  private String municipio="";
  private String domicilio="";
  private String codigoPostal="";
  private String numDesde="";
  private String letraDesde="";
  private String numHasta="";
  private String letraHasta="";
  private String bloque="";
  private String portal="";
  private String escalera="";
  private String planta="";
  private String puerta="";
  private String barriada="";
  private String idPaisVia="";
  private String idProvinciaVia="";
  private String idMunicipioVia="";
  private String idVia="";
  private String idTipoVia="";
  private String tipoVia="";
  private String codigoVia="";
  private String revNormalizar="";
  private String normalizado="";
  @Deprecated
  private String enPadron="";
  private String codTipoUso="";
  private String descTipoUso="";
  private String situacion="";
  private String descVia="";
  /* anadir ECOESI */
  private String codECO="";
  private String codESI="";
  private String descESI="";
  private String descECO="";
  private String modificado="";
  private String origen = "SGE";
  /* Fin anadir ECOESI */
  private String domActual="";
  private String esDomPrincipal="false";
  private boolean cambiarCodVia = false;
  private boolean cambiarDomicilioVia = true;


  public DomicilioSimpleValueObject() {
  }

  public DomicilioSimpleValueObject(String idDom,String idPais,String idProv, String idMuni,
                                    String pais, String prov, String mun,
                                    String dom, String cod) {
    this.idDomicilio = idDom;
    this.idPais = idPais;
    this.idProvincia = idProv;
    this.idMunicipio = idMuni;
    this.pais = pais;
    this.provincia = prov;
    this.municipio = mun;
    this.domicilio = (dom==null)?"":dom;
    this.codigoPostal = (cod==null)?"":cod;
  }

  public void validate(String idioma) throws ValidationException {
    boolean correcto = true;
    Messages errors = new Messages();
    if (!errors.empty()) throw new ValidationException(errors);
      isValid = true;
  }

  /** Devuelve un booleano que representa si el estado de este RegistroEntrada es válido. */
  public boolean IsValid() { return isValid; }

  public void setPais(String pais) {
    if(pais!=null)
      this.pais = pais;
    else
      this.pais = "";
  }

  public String getPais() {
    return pais;
  }

  public void setProvincia(String provincia) {
    if(provincia!=null)
      this.provincia = provincia;
    else
      this.provincia = "";
  }

  public String getProvincia() {
    return provincia;
  }

  public void setMunicipio(String municipio) {
    if(municipio!=null)
      this.municipio = municipio;
    else
      this.municipio = "";
  }

  public String getMunicipio() {
    return municipio;
  }

  public void setDomicilio(String domicilio) {
    this.domicilio = domicilio;
  }

  public String getDomicilio() {
    return domicilio;
  }

  public void setCodigoPostal(String newCodigoPostal) {
    if(newCodigoPostal!=null)
      this.codigoPostal = newCodigoPostal;
    else
      this.codigoPostal = "";
  }

  public String getCodigoPostal() {
    return codigoPostal;
  }

  public String getIdDomicilio() {
    return idDomicilio;
  }

  public void setIdDomicilio(String idDomicilio) {
    if(idDomicilio!=null)
      this.idDomicilio = idDomicilio;
    else
      this.idDomicilio = "";
  }
  
  public String getIdDomicilioAntiguo() {
    return idDomicilioAntiguo;
  }

  public void setIdDomicilioAntiguo(String idDomicilioAntiguo) {
    if(idDomicilioAntiguo!=null)
      this.idDomicilioAntiguo = idDomicilioAntiguo;
    else
      this.idDomicilioAntiguo = "";
  }

  public void setIdPais(String idPais) {
    if(idPais!=null)
      this.idPais = idPais;
    else
      this.idPais = "";
  }

  public String getIdPais() {
    return idPais;
  }

  public void setIdProvincia(String idProvincia) {
    if(idProvincia!=null)
      this.idProvincia = idProvincia;
    else
      this.idProvincia = "";
  }

  public String getIdProvincia() {
    return idProvincia;
  }

  public void setIdMunicipio(String idMunicipio) {
    if(idMunicipio!=null)
      this.idMunicipio = idMunicipio;
    else
      this.idMunicipio = "";
  }

  public String getIdMunicipio() {
    return idMunicipio;
  }

  public void setNumDesde(String numDesde) {
    if(numDesde!=null)
      this.numDesde = numDesde;
    else
      this.numDesde = "";
  }

  public String getNumDesde() {
    return numDesde;
  }

  public void setLetraDesde(String letraDesde) {
    if(letraDesde!=null)
      this.letraDesde = letraDesde;
    else
      this.letraDesde = "";
  }

  public String getLetraDesde() {
    return letraDesde;
  }

  public void setNumHasta(String numHasta) {
    if(numHasta!=null)
      this.numHasta = numHasta;
    else
      this.numHasta = "";
  }

  public String getNumHasta() {
    return numHasta;
  }

  public void setLetraHasta(String letraHasta) {
    if(letraHasta!=null)
      this.letraHasta = letraHasta;
    else
      this.letraHasta = "";
  }

  public String getLetraHasta() {
    return letraHasta;
  }

  public void setBloque(String bloque) {
    if(bloque!=null)
      this.bloque = bloque;
    else
      this.bloque = "";
  }

  public String getBloque() {
    return bloque;
  }

  public void setPortal(String portal) {
    if(portal!=null)
      this.portal = portal;
    else
      this.portal = "";
  }

  public String getPortal() {
    return portal;
  }

  public void setEscalera(String escalera) {
    if(escalera!=null)
      this.escalera = escalera;
    else
      this.escalera = "";
  }

  public String getEscalera() {
    return escalera;
  }

  public void setPlanta(String planta) {
    if(planta!=null)
      this.planta = planta;
    else
      this.planta = "";
  }

  public String getPlanta() {
    return planta;
  }

  public void setPuerta(String puerta) {
    if(puerta!=null)
      this.puerta = puerta;
    else
      this.puerta = "";
  }

  public String getPuerta() {
    return puerta;
  }

  public void setBarriada(String barriada) {
    if(barriada!=null)
      this.barriada = barriada;
    else
      this.barriada = "";
  }

  public String getBarriada() {
    return barriada;
  }

  public void setIdPaisVia(String idPaisVia) {
    if(idPaisVia!=null)
      this.idPaisVia = idPaisVia;
    else
      this.idPaisVia = "";
  }

  public String getIdPaisVia() {
    return idPaisVia;
  }

  public void setIdProvinciaVia(String idProvinciaVia) {
    if(idProvinciaVia!=null)
      this.idProvinciaVia = idProvinciaVia;
    else
      this.idProvinciaVia = "";
  }

  public String getIdProvinciaVia() {
    return idProvinciaVia;
  }

  public void setIdMunicipioVia(String idMunicipioVia) {
    if(idMunicipioVia!=null)
      this.idMunicipioVia = idMunicipioVia;
    else
      this.idMunicipioVia = "";
  }

  public String getIdMunicipioVia() {
    return idMunicipioVia;
  }

  public void setIdVia(String idVia) {
    if(idVia!=null)
      this.idVia = idVia;
    else
      this.idVia = "";
  }

  public String getIdVia() {
    return idVia;
  }
  
  public void setIdTipoVia(String idTipoVia) {
    if(idTipoVia!=null)
      this.idTipoVia = idTipoVia;
    else
      this.idTipoVia = "";
  }

  public String getIdTipoVia() {
    return idTipoVia;
  }

  public void setTipoVia(String tipoVia) {
    if(tipoVia!=null)
      this.tipoVia = tipoVia;
    else
      this.tipoVia = "";
  }

  public String getTipoVia() {
    return tipoVia;
  }

  public void setCodigoVia(String codigoVia) {
    if(codigoVia!=null)
      this.codigoVia = codigoVia;
    else
      this.codigoVia = "";
  }

  public String getCodigoVia() {
    return codigoVia;
  }

  public void setDescVia(String descVia) {
	  if(descVia!=null)
		this.descVia = descVia;
	  else
		this.descVia = "";
	}

	public String getDescVia() {
	  return descVia;
	}

/* anadir ECOESI */
	public void setCodECO(String codigo) {
	  this.codECO= codigo;	
  	}
  	public String getCodECO() {
		return this.codECO;
  	}
	public void setCodESI(String codigo) {
	  this.codESI= codigo;	
	}
	public String getCodESI() {
		return this.codESI;
	}
	public void setDescESI(String descripcion) {
		this.descESI= descripcion;	
	}
	public String getDescESI() {
		return this.descESI;
	}
        public void setDescECO(String descripcion) {
            this.descECO= descripcion;
        }
        public String getDescECO() {
            return this.descECO;
        }
	public void setModificado(String valor) {
		this.modificado= valor;	
	}
	public String getModificado() {
		return this.modificado;
	}
/* fin anadir ECOESI */
  public void setRevNormalizar(String normalizar) {
    if(normalizar!=null)
      this.revNormalizar = normalizar;
    else
      this.revNormalizar = "";
  }

  public String getRevNormalizar() {
    return revNormalizar;
  }

  public void setNormalizado(String normalizado) {
    if(normalizado!=null)
      this.normalizado = normalizado;
    else
      this.normalizado = "";
  }

  public String getNormalizado() {
    return normalizado;
  }

  @Deprecated
  public void setEnPadron(String enPadron) {
    if(enPadron!=null)
      this.enPadron = enPadron;
    else
      this.enPadron = "";
  }

  @Deprecated
  public String getEnPadron() {
    return enPadron;
  }

  public void setCodTipoUso(String uso) {
    if(uso!=null)
      this.codTipoUso = uso;
    else
      this.codTipoUso = "";
  }

  public String getCodTipoUso() {
    return codTipoUso;
  }

  public void setDescTipoUso(String uso) {
    if(uso!=null)
      this.descTipoUso = uso;
    else
      this.descTipoUso = "";
  }

  public String getDescTipoUso() {
    return descTipoUso;
  }

  public void setSituacion(String sit) {
    if(sit!=null)
      this.situacion = sit;
    else
      this.situacion = "A";
  }

  public String getSituacion() {
    return situacion;
  }

 public String getOrigen(){
    return origen;
}

public void setOrigen(String valor){
    origen = valor;
}

public void setDomActual(String domActual){
   this.domActual = domActual;
}

public String getDomActual(){
   return domActual;
}

public String getEsDomPrincipal() {
    return esDomPrincipal;
}

public void setEsDomPrincipal(String esDomPrincipal) {
    this.esDomPrincipal = esDomPrincipal;
}

    /**
     * @return the cambiarCodVia
     */
    public boolean isCambiarCodVia() {
        return cambiarCodVia;
    }

    /**
     * @param cambiarCodVia the cambiarCodVia to set
     */
    public void setCambiarCodVia(boolean cambiarCodVia) {
        this.cambiarCodVia = cambiarCodVia;
    }

    /**
     * @return the cambiarDomicilioVia
     */
    public boolean isCambiarDomicilioVia() {
        return cambiarDomicilioVia;
    }

    /**
     * @param cambiarDomicilioVia the cambiarDomicilioVia to set
     */
    public void setCambiarDomicilioVia(boolean cambiarDomicilioVia) {
        this.cambiarDomicilioVia = cambiarDomicilioVia;
    }

 @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.isValid ? 1 : 0);
        hash = 79 * hash + (this.idDomicilio != null ? this.idDomicilio.hashCode() : 0);
        hash = 79 * hash + (this.idDomicilioAntiguo != null ? this.idDomicilioAntiguo.hashCode() : 0);
        hash = 79 * hash + (this.idPais != null ? this.idPais.hashCode() : 0);
        hash = 79 * hash + (this.idProvincia != null ? this.idProvincia.hashCode() : 0);
        hash = 79 * hash + (this.idMunicipio != null ? this.idMunicipio.hashCode() : 0);
        hash = 79 * hash + (this.pais != null ? this.pais.hashCode() : 0);
        hash = 79 * hash + (this.provincia != null ? this.provincia.hashCode() : 0);
        hash = 79 * hash + (this.municipio != null ? this.municipio.hashCode() : 0);
        hash = 79 * hash + (this.domicilio != null ? this.domicilio.hashCode() : 0);
        hash = 79 * hash + (this.codigoPostal != null ? this.codigoPostal.hashCode() : 0);
        hash = 79 * hash + (this.numDesde != null ? this.numDesde.hashCode() : 0);
        hash = 79 * hash + (this.letraDesde != null ? this.letraDesde.hashCode() : 0);
        hash = 79 * hash + (this.numHasta != null ? this.numHasta.hashCode() : 0);
        hash = 79 * hash + (this.letraHasta != null ? this.letraHasta.hashCode() : 0);
        hash = 79 * hash + (this.bloque != null ? this.bloque.hashCode() : 0);
        hash = 79 * hash + (this.portal != null ? this.portal.hashCode() : 0);
        hash = 79 * hash + (this.escalera != null ? this.escalera.hashCode() : 0);
        hash = 79 * hash + (this.planta != null ? this.planta.hashCode() : 0);
        hash = 79 * hash + (this.puerta != null ? this.puerta.hashCode() : 0);
        hash = 79 * hash + (this.barriada != null ? this.barriada.hashCode() : 0);
        hash = 79 * hash + (this.idPaisVia != null ? this.idPaisVia.hashCode() : 0);
        hash = 79 * hash + (this.idProvinciaVia != null ? this.idProvinciaVia.hashCode() : 0);
        hash = 79 * hash + (this.idMunicipioVia != null ? this.idMunicipioVia.hashCode() : 0);
        hash = 79 * hash + (this.idVia != null ? this.idVia.hashCode() : 0);
        hash = 79 * hash + (this.idTipoVia != null ? this.idTipoVia.hashCode() : 0);
        hash = 79 * hash + (this.tipoVia != null ? this.tipoVia.hashCode() : 0);
        hash = 79 * hash + (this.codigoVia != null ? this.codigoVia.hashCode() : 0);
        hash = 79 * hash + (this.revNormalizar != null ? this.revNormalizar.hashCode() : 0);
        hash = 79 * hash + (this.normalizado != null ? this.normalizado.hashCode() : 0);
        hash = 79 * hash + (this.enPadron != null ? this.enPadron.hashCode() : 0);
        hash = 79 * hash + (this.codTipoUso != null ? this.codTipoUso.hashCode() : 0);
        hash = 79 * hash + (this.descTipoUso != null ? this.descTipoUso.hashCode() : 0);
        hash = 79 * hash + (this.situacion != null ? this.situacion.hashCode() : 0);
        hash = 79 * hash + (this.descVia != null ? this.descVia.hashCode() : 0);
        hash = 79 * hash + (this.codECO != null ? this.codECO.hashCode() : 0);
        hash = 79 * hash + (this.codESI != null ? this.codESI.hashCode() : 0);
        hash = 79 * hash + (this.descESI != null ? this.descESI.hashCode() : 0);
        hash = 79 * hash + (this.descECO != null ? this.descECO.hashCode() : 0);
        hash = 79 * hash + (this.modificado != null ? this.modificado.hashCode() : 0);
        hash = 79 * hash + (this.origen != null ? this.origen.hashCode() : 0);
        hash = 79 * hash + (this.domActual != null ? this.domActual.hashCode() : 0);
        hash = 79 * hash + (this.esDomPrincipal != null ? this.esDomPrincipal.hashCode() : 0);
        hash = 79 * hash + (this.cambiarCodVia ? 1 : 0);
        hash = 79 * hash + (this.cambiarDomicilioVia ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DomicilioSimpleValueObject other = (DomicilioSimpleValueObject) obj;
        if (this.isValid != other.isValid) {
            return false;
        }
        if ((this.idDomicilio == null) ? (other.idDomicilio != null) : !this.idDomicilio.equals(other.idDomicilio)) {
            return false;
        }
        if ((this.idDomicilioAntiguo == null) ? (other.idDomicilioAntiguo != null) : !this.idDomicilioAntiguo.equals(other.idDomicilioAntiguo)) {
            return false;
        }
        if ((this.idPais == null) ? (other.idPais != null) : !this.idPais.equals(other.idPais)) {
            return false;
        }
        if ((this.idProvincia == null) ? (other.idProvincia != null) : !this.idProvincia.equals(other.idProvincia)) {
            return false;
        }
        if ((this.idMunicipio == null) ? (other.idMunicipio != null) : !this.idMunicipio.equals(other.idMunicipio)) {
            return false;
        }
        if ((this.pais == null) ? (other.pais != null) : !this.pais.equals(other.pais)) {
            return false;
        }
        if ((this.provincia == null) ? (other.provincia != null) : !this.provincia.equals(other.provincia)) {
            return false;
        }
        if ((this.municipio == null) ? (other.municipio != null) : !this.municipio.equals(other.municipio)) {
            return false;
        }
        if ((this.domicilio == null) ? (other.domicilio != null) : !this.domicilio.equals(other.domicilio)) {
            return false;
        }
        if ((this.codigoPostal == null) ? (other.codigoPostal != null) : !this.codigoPostal.equals(other.codigoPostal)) {
            return false;
        }
        if ((this.numDesde == null) ? (other.numDesde != null) : !this.numDesde.equals(other.numDesde)) {
            return false;
        }
        if ((this.letraDesde == null) ? (other.letraDesde != null) : !this.letraDesde.equals(other.letraDesde)) {
            return false;
        }
        if ((this.numHasta == null) ? (other.numHasta != null) : !this.numHasta.equals(other.numHasta)) {
            return false;
        }
        if ((this.letraHasta == null) ? (other.letraHasta != null) : !this.letraHasta.equals(other.letraHasta)) {
            return false;
        }
        if ((this.bloque == null) ? (other.bloque != null) : !this.bloque.equals(other.bloque)) {
            return false;
        }
        if ((this.portal == null) ? (other.portal != null) : !this.portal.equals(other.portal)) {
            return false;
        }
        if ((this.escalera == null) ? (other.escalera != null) : !this.escalera.equals(other.escalera)) {
            return false;
        }
        if ((this.planta == null) ? (other.planta != null) : !this.planta.equals(other.planta)) {
            return false;
        }
        if ((this.puerta == null) ? (other.puerta != null) : !this.puerta.equals(other.puerta)) {
            return false;
        }
        if ((this.barriada == null) ? (other.barriada != null) : !this.barriada.equals(other.barriada)) {
            return false;
        }
        if ((this.idPaisVia == null) ? (other.idPaisVia != null) : !this.idPaisVia.equals(other.idPaisVia)) {
            return false;
        }
        if ((this.idProvinciaVia == null) ? (other.idProvinciaVia != null) : !this.idProvinciaVia.equals(other.idProvinciaVia)) {
            return false;
        }
        if ((this.idMunicipioVia == null) ? (other.idMunicipioVia != null) : !this.idMunicipioVia.equals(other.idMunicipioVia)) {
            return false;
        }
        if ((this.idVia == null) ? (other.idVia != null) : !this.idVia.equals(other.idVia)) {
            return false;
        }
        if ((this.idTipoVia == null) ? (other.idTipoVia != null) : !this.idTipoVia.equals(other.idTipoVia)) {
            return false;
        }
        if ((this.tipoVia == null) ? (other.tipoVia != null) : !this.tipoVia.equals(other.tipoVia)) {
            return false;
        }
        if ((this.codigoVia == null) ? (other.codigoVia != null) : !this.codigoVia.equals(other.codigoVia)) {
            return false;
        }
        if ((this.revNormalizar == null) ? (other.revNormalizar != null) : !this.revNormalizar.equals(other.revNormalizar)) {
            return false;
        }
        if ((this.normalizado == null) ? (other.normalizado != null) : !this.normalizado.equals(other.normalizado)) {
            return false;
        }
        if ((this.enPadron == null) ? (other.enPadron != null) : !this.enPadron.equals(other.enPadron)) {
            return false;
        }
        if ((this.codTipoUso == null) ? (other.codTipoUso != null) : !this.codTipoUso.equals(other.codTipoUso)) {
            return false;
        }
        if ((this.descTipoUso == null) ? (other.descTipoUso != null) : !this.descTipoUso.equals(other.descTipoUso)) {
            return false;
        }
        if ((this.situacion == null) ? (other.situacion != null) : !this.situacion.equals(other.situacion)) {
            return false;
        }
        if ((this.descVia == null) ? (other.descVia != null) : !this.descVia.equals(other.descVia)) {
            return false;
        }
        if ((this.codECO == null) ? (other.codECO != null) : !this.codECO.equals(other.codECO)) {
            return false;
        }
        if ((this.codESI == null) ? (other.codESI != null) : !this.codESI.equals(other.codESI)) {
            return false;
        }
        if ((this.descESI == null) ? (other.descESI != null) : !this.descESI.equals(other.descESI)) {
            return false;
        }
        if ((this.descECO == null) ? (other.descECO != null) : !this.descECO.equals(other.descECO)) {
            return false;
        }
        if ((this.modificado == null) ? (other.modificado != null) : !this.modificado.equals(other.modificado)) {
            return false;
        }
        if ((this.origen == null) ? (other.origen != null) : !this.origen.equals(other.origen)) {
            return false;
        }
        if ((this.domActual == null) ? (other.domActual != null) : !this.domActual.equals(other.domActual)) {
            return false;
        }
        if ((this.esDomPrincipal == null) ? (other.esDomPrincipal != null) : !this.esDomPrincipal.equals(other.esDomPrincipal)) {
            return false;
        }
        if (this.cambiarCodVia != other.cambiarCodVia) {
            return false;
        }
        if (this.cambiarDomicilioVia != other.cambiarDomicilioVia) {
            return false;
        }
        return true;
    }

}