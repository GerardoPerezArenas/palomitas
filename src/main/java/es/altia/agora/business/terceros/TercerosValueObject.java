 // NOMBRE DEL PAQUETE
package es.altia.agora.business.terceros;

// PAQUETES IMPORTADOS
import es.altia.technical.*;
import es.altia.util.ParserUtils;
import es.altia.util.StringUtils;
import es.altia.util.jdbc.sqlbuilder.RowResult;

import java.io.*;

import java.util.*;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase TercerosValueObject</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class TercerosValueObject implements Serializable, ValueObject {

  private String documento="";
  private String nombre="";
  private String apellido1="";
  private String partApellido1="";
  private String apellido2="";
  private String partApellido2="";
  private String telefono="";
  private String email="";
  private char situacion;
  private String fechaAlta="";
  private String fechaBaja="";
  private Vector domicilios;
  private String domPrincipal="";

  private String idDomicilio="";
  private String identificador="";
  private String moduloAlta="";
  private String normalizado="";
  private String tipoDocumento="";
  private String tipoDocDesc="";
  private String usuarioAlta="";
  private String usuarioBaja="";
  private String version="";
  private String nombreCompleto;
  
  private String origen = "SGE";
  private String codTerceroOrigen="";
  
  private Vector valoresCamposSuplementarios;
  private String codOrganizacion;
  
  private Integer codRol;
  private String descRol;
  private Boolean rolDefecto;
  private String notificacionElectronica;

  // Atributos deprecados
  @Deprecated
  private String descripcionDocumento;
  @Deprecated
  private String codIdioma;
  @Deprecated
  private boolean isValid;
  @Deprecated
  private String nombreAntiguo = "";
  @Deprecated
  private String apellido1Antiguo = "";
  @Deprecated
  private String partApellido1Antiguo = "";
  @Deprecated
  private String apellido2Antiguo = "";
  @Deprecated
  private String partApellido2Antiguo = "";
  @Deprecated
  private String tipoDocumentoAntiguo = "";
  @Deprecated
  private String documentoAntiguo = "";
	
  public TercerosValueObject() {
  }

  public TercerosValueObject(String ident, String ver, String tipo, String tipoDocDesc,
                             String doc, String nom,String ap1, String parAp1, String ap2,
                             String parAp2,String nor, String telf, String email, char sit, String fAlta,
                             String usuAlta, String mod, String fBaja, String usuBaja){
    this.identificador = ident;
    this.version = ver;
    this.tipoDocumento = (tipo==null)?"":tipo;
    this.tipoDocDesc = (tipoDocDesc==null)?"":tipoDocDesc;
    this.documento = (doc==null)?"":doc;
    this.nombre = (nom==null)?"":nom;
    this.apellido1 = (ap1==null?"":ap1);
    this.partApellido1 = (parAp1==null)?"":parAp1;
    this.apellido2 = (ap2==null)?"":ap2;
    this.partApellido2 = (parAp2==null)?"":parAp2;
    this.normalizado = nor;
    this.telefono = (telf==null)?"":telf;
    this.email = (email==null)?"":email;
    this.situacion = sit;
    this.fechaAlta = fAlta;
    this.usuarioAlta = usuAlta;
    this.moduloAlta = mod;
    this.fechaBaja = fBaja;
    this.usuarioBaja = usuBaja;
  }
  
	/**
	* Parsea un {@link RowResult} obtenido a partir de una consulta completa a la tabla T_TER a un 
	* {@link TercerosValueObject}. 
	* Utiliza los nombres TER_XXX, etc. para recuperar los valores, sin el prefijo de la tabla.
	* No se inicializan las listas porque no es un traslado directo de un {@link RowResult}.
	*
	* @param row
	*/
	public TercerosValueObject(RowResult row) {
		this.apellido1 = row.getString("TER_AP1");
		this.apellido2 = row.getString("TER_AP2");
		this.codOrganizacion = row.getString("EXT_MUN");
		this.codRol = row.getInteger("EXT_ROL");
		this.codTerceroOrigen = row.getString("TER_COD");
		this.descRol = row.getString("ROL_DES");
		this.documento = row.getString("TER_DOC");
		this.domPrincipal = row.getString("TER_DOM");
		this.email = row.getString("TER_DCE");
		this.fechaAlta = row.getString("TER_FAL");	
		this.fechaBaja = row.getString("TER_FBJ");
		this.idDomicilio = row.getString("TER_DOM");	// Parece, según los usos, redundante con domPrincipal
		this.identificador = row.getString("TER_COD");	// Parece, según los usos, redundante con codTerceroOrigen
		this.moduloAlta = row.getString("TER_APL");
		this.nombre = row.getString("TER_NOM");
		this.normalizado = row.getString("TER_NML");
		this.notificacionElectronica = row.getString("EXT_NOTIFICACION_ELECTRONICA");
		this.partApellido1 = row.getString("TER_PA1");
		this.partApellido2 = row.getString("TER_PA1");
		this.rolDefecto = row.getBoolean("ROL_PDE");
		this.situacion = ParserUtils.parsear(row.getString("TER_SIT"), 'A');
		this.telefono = row.getString("TER_TLF");
		this.tipoDocDesc = null;							// TODO
		this.tipoDocumento = row.getString("TER_TID");
		this.usuarioAlta = row.getString("TER_UAL");
		this.usuarioBaja = row.getString("TER_UBJ");
		this.valoresCamposSuplementarios = null;			// TODO
		this.version = row.getString("TER_NVE");
		
		// Formar el nombre completo
		this.nombreCompleto = StringUtils.concatenarStrings(nombre, partApellido1, apellido1, partApellido2, apellido2);
		
		// Campos Deprecados
		
		// this.apellido1Antiguo = null;
		// this.apellido2Antiguo = null;
		// this.codIdioma = null;
		// this.documentoAntiguo = null;
		// this.descripcionDocumento = null;
		// this.isValid = false;
		// this.nombreAntiguo = null;
		// this.partApellido1Antiguo = null;
		// this.partApellido2Antiguo = null;
		// this.tipoDocumentoAntiguo = null;
	}
  
  
  public String getIdentificador() {
    return identificador;
  }

  public void setIdentificador(String identificador) {
    if(identificador!=null)
      this.identificador = identificador;
    else
      this.identificador = "";
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    if(version!=null)
      this.version = version;
    else
      this.version = "";
  }

  public String getApellido1() {
    return apellido1;
  }

  public void setApellido1(String apellido1) {
    if(apellido1!=null)
      this.apellido1 = apellido1;
    else this.apellido1 = "";
  }

  public String getApellido2() {
    return apellido2;
  }

  public void setApellido2(String apellido2) {
    if(apellido2!=null)
      this.apellido2 = apellido2;
    else this.apellido2 = "";
  }

  public String getDocumento() {
    return documento;
  }

  public void setDocumento(String documento) {
    if(documento!=null)
      this.documento = documento;
    else
      this.documento = "";
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    if (email!=null)
      this.email = email;
    else
      this.email = "";
  }

  public void setFechaAlta(String fechaAlta) {
    if(fechaAlta!=null)
      this.fechaAlta = fechaAlta;
    else
      this.fechaAlta = "";
  }

  public String getFechaAlta() {
    return fechaAlta;
  }

  public String getFechaBaja() {
    return fechaBaja;
  }

  public void setFechaBaja(String fechaBaja) {
    if(fechaBaja!=null)
      this.fechaBaja = fechaBaja;
    else
      fechaBaja = "";
  }

  public String getModuloAlta() {
    return moduloAlta;
  }

  public void setModuloAlta(String moduloAlta) {
    if(moduloAlta!=null)
      this.moduloAlta = moduloAlta;
    else
      this.moduloAlta = "";
  }

  public void setNombre(String nombre) {
    if(nombre!=null)
      this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public String getNormalizado() {
    return normalizado;
  }

  public void setNormalizado(String normalizado) {
    if(normalizado!=null)
      this.normalizado = normalizado;
  }

  public void setPartApellido1(String pApellido1) {
    if (pApellido1!=null)
      this.partApellido1 = pApellido1;
    else
      this.partApellido1 = "";
  }

  public void setPartApellido2(String pApellido2) {
    if (pApellido2!=null)
      this.partApellido2 = pApellido2;
    else
      this.partApellido2 = "";
  }

  public void setSituacion(char situacion) {
    this.situacion = situacion;
  }

  public void setTelefono(String telf) {
    if (telf!=null)
      this.telefono = telf;
    else
      this.telefono = "";
  }

  public void setTipoDocumento(String tipoDocumento) {
    this.tipoDocumento = tipoDocumento;
  }

  public void setTipoDocDesc(String tipoDocDesc) {
    this.tipoDocDesc = tipoDocDesc;
  }

  public void setUsuarioAlta(String usuarioAlta) {
    this.usuarioAlta = usuarioAlta;
  }

  public void setUsuarioBaja(String usuarioBaja) {
    if (usuarioBaja!=null)
      this.usuarioBaja = usuarioBaja;
    else
      this.usuarioBaja = "";
  }

  public String getPartApellido1() {
    return partApellido1;
  }

  public String getPartApellido2() {
    return partApellido2;
  }

  public char getSituacion() {
    return situacion;
  }

  public String getTelefono() {
    return telefono;
  }

  public String getTipoDocumento() {
    return tipoDocumento;
  }

  public String getTipoDocDesc() {
    return tipoDocDesc;
  }

  public String getUsuarioAlta() {
    return usuarioAlta;
  }

  public String getUsuarioBaja() {
    return usuarioBaja;
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

  public void setDomicilios(Vector domicilios) {
    this.domicilios = domicilios;
  }

  public Vector getDomicilios() {
    return domicilios;
  }

  public String getDomPrincipal() {
      return domPrincipal;
  }

  public void setDomPrincipal(String domPrincipal) {
      this.domPrincipal = domPrincipal;
  }

  public void validate(String idioma) throws ValidationException {
    String sufijo = "";
    Messages errors = new Messages();
    if (!errors.empty()) throw new ValidationException(errors);
    isValid = true;
  }

  public String getApellido1Antiguo() {
    return apellido1Antiguo;
  }

  public void setApellido1Antiguo(String apellido1Antiguo) {
    if(apellido1Antiguo!=null)
      this.apellido1Antiguo = apellido1Antiguo;
    else this.apellido1Antiguo = "";
  }

  public String getApellido2Antiguo() {
    return apellido2Antiguo;
  }

  public void setApellido2Antiguo(String apellido2Antiguo) {
    if(apellido2Antiguo!=null)
      this.apellido2Antiguo = apellido2Antiguo;
    else this.apellido2Antiguo = "";
  }

  @Deprecated
  public String getNombreAntiguo() {
    return nombreAntiguo;
  }

  @Deprecated
  public void setNombreAntiguo(String nombreAntiguo) {
    if(nombreAntiguo!=null)
      this.nombreAntiguo = nombreAntiguo;
    else this.nombreAntiguo = "";
  }

  @Deprecated
  public String getPartApellido1Antiguo() {
    return partApellido1Antiguo;
  }

  @Deprecated
  public void setPartApellido1Antiguo(String partApellido1Antiguo) {
    if(partApellido1Antiguo!=null)
      this.partApellido1Antiguo = partApellido1Antiguo;
    else this.partApellido1Antiguo = "";
  }

  @Deprecated
  public String getPartApellido2Antiguo() {
    return partApellido2Antiguo;
  }

  @Deprecated
  public void setPartApellido2Antiguo(String partApellido2Antiguo) {
    if(partApellido2Antiguo!=null)
      this.partApellido2Antiguo = partApellido2Antiguo;
    else this.partApellido2Antiguo = "";
  }

  @Deprecated
  public String getTipoDocumentoAntiguo() {
	  return tipoDocumentoAntiguo;
	}

  @Deprecated
  public void setTipoDocumentoAntiguo(String tipoDocumentoAntiguo) {
	  if(tipoDocumentoAntiguo!=null)
		this.tipoDocumentoAntiguo = tipoDocumentoAntiguo;
	  else this.tipoDocumentoAntiguo = "";
	}

  @Deprecated
  public String getDocumentoAntiguo() {
	return documentoAntiguo;
  }
	
  @Deprecated
  public void setDocumentoAntiguo(String documentoAntiguo) {
	if(documentoAntiguo!=null)
	  this.documentoAntiguo = documentoAntiguo;
	else this.documentoAntiguo = "";
  }

  /** Devuelve un booleano que representa si el estado de este RegistroEntrada es válido. */
  @Deprecated
  public boolean IsValid() { return isValid; }

  public String getNombreCompleto() {
    return nombreCompleto;
  }

  public void setNombreCompleto(String newNombreCompleto) {
    if(newNombreCompleto!=null)
      nombreCompleto = newNombreCompleto;
    else
      nombreCompleto = "";
  }

    public String getOrigen(){
        return origen;
    }
    public void setOrigen (String valor){
        origen = valor;
    }

    public String getCodTerceroOrigen() {
        return codTerceroOrigen;
    }

    public void setCodTerceroOrigen(String codTerceroOrigen) {
        this.codTerceroOrigen = codTerceroOrigen;
    }

    /**
     * @return the valoresCamposSuplementarios
     */
    public Vector getValoresCamposSuplementarios() {
        return valoresCamposSuplementarios;
    }

    /**
     * @param valoresCamposSuplementarios the valoresCamposSuplementarios to set
     */
    public void setValoresCamposSuplementarios(Vector valoresCamposSuplementarios) {
        this.valoresCamposSuplementarios = valoresCamposSuplementarios;
    }

    public String getCodOrganizacion(){
        return this.codOrganizacion;
    }

    public void setCodOrganizacion(String codOrganizacion){
        this.codOrganizacion = codOrganizacion;
    }

    /**
     * @return the codIdioma
     */
    public String getCodIdioma() {
        return codIdioma;
    }

    /**
     * @param codIdioma the codIdioma to set
     */
    public void setCodIdioma(String codIdioma) {
        this.codIdioma = codIdioma;
    }

    public Integer getCodRol() {
        return codRol;
    }

    public void setCodRol(Integer codRol) {
        this.codRol = codRol;
    }

    public String getDescRol() {
        return descRol;
    }

    public void setDescRol(String descRol) {
        this.descRol = descRol;
    }

	@Deprecated
    public String getDescripcionDocumento() {
        return descripcionDocumento;
    }
	
	@Deprecated
    public void setDescripcionDocumento(String descripcionDocumento) {
        this.descripcionDocumento = descripcionDocumento;
    }

    public Boolean isRolDefecto() {
        return rolDefecto;
    }

    public void setRolDefecto(Boolean rolDefecto) {
        this.rolDefecto = rolDefecto;
    }
    
       public String getNotificacionElectronica() {
        return notificacionElectronica;
    }

    public void setNotificacionElectronica(String notificacionElectronica) {
        this.notificacionElectronica = notificacionElectronica;
    }
}