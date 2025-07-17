package es.altia.agora.business.sge;

import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import java.io.Serializable;

public class ConfInformeValueObject implements Serializable, ValueObject {

  private boolean isValid;



  public ConfInformeValueObject(){
      super();
  }

  public ConfInformeValueObject(String Combo, String Area, String Unidad, String TipoProcedimiento, String Procedimiento, String ClasTramite, String Tramite, String AreaD, String UnidadD, String TipoProcedimientoD, String ProcedimientoD, String ClasTramiteD, String TramiteD){
    this.Combo              = Combo;

    this.Area               = Area;
    this.Unidad             = Unidad;
    this.TipoProcedimiento  = TipoProcedimiento;
    this.Procedimiento      = Procedimiento;
    this.ClasTramite        = ClasTramite;
    this.Tramite            = Tramite;
    this.AreaD              = AreaD;
    this.UnidadD            = UnidadD;
    this.TipoProcedimientoD = TipoProcedimientoD;
    this.ProcedimientoD     = ProcedimientoD;
    this.ClasTramiteD       = ClasTramiteD;
    this.TramiteD           = TramiteD;
  }

  public String getCombo(){
    return Combo;
  }

  public void setCombo(String pStrCombo){
    this.Combo = pStrCombo;
  }


  public String getArea(){
    return Area;
  }

  public void setArea(String pStrArea){
    this.Area = pStrArea;
  }

  public String getUnidad(){
    return Unidad;
  }

  public void setUnidad(String pStrUnidad){
    this.Unidad = pStrUnidad;
  }

  public String getTipoProcedimiento(){
    return TipoProcedimiento;
  }

  public void setTipoProcedimiento(String pStrTipoProcedimiento){
    this.TipoProcedimiento = pStrTipoProcedimiento;
  }

  public String getProcedimiento(){
    return Procedimiento;
  }

  public void setProcedimiento(String pStrProcedimiento){
    this.Procedimiento = pStrProcedimiento;
  }

  public String getClasTramite(){
    return ClasTramite;
  }

  public void setClasTramite(String pStrClasTramite){
    this.ClasTramite = pStrClasTramite;
  }

  public String getTramite(){
    return Tramite;
  }

  public void setTramite(String pStrTramite){
    this.Tramite = pStrTramite;
  }


  public String getAreaD(){
    return AreaD;
  }

  public void setAreaD(String pStrAreaD){
    this.AreaD = pStrAreaD;
  }

  public String getUnidadD(){
    return UnidadD;
  }

  public void setUnidadD(String pStrUnidadD){
    this.UnidadD = pStrUnidadD;
  }

  public String getTipoProcedimientoD(){
    return TipoProcedimientoD;
  }

  public void setTipoProcedimientoD(String pStrTipoProcedimientoD){
    this.TipoProcedimientoD = pStrTipoProcedimientoD;
  }

  public String getProcedimientoD(){
    return ProcedimientoD;
  }

  public void setProcedimientoD(String pStrProcedimientoD){
    this.ProcedimientoD = pStrProcedimientoD;
  }

  public String getClasTramiteD(){
    return ClasTramiteD;
  }

  public void setClasTramiteD(String pStrClasTramiteD){
    this.ClasTramiteD = pStrClasTramiteD;
  }

  public String getTramiteD(){
    return TramiteD;
  }

  public void setTramiteD(String pStrTramiteD){
    this.TramiteD = pStrTramiteD;
  }

  public void copy(ConfInformeValueObject other) {
    this.Combo              = other.Combo;

    this.Area               = other.Area;
    this.Unidad             = other.Unidad;
    this.TipoProcedimiento  = other.TipoProcedimiento;
    this.Procedimiento      = other.Procedimiento;
    this.ClasTramite        = other.ClasTramite;
    this.Tramite            = other.Tramite;
    this.AreaD              = other.AreaD;
    this.UnidadD            = other.UnidadD;
    this.TipoProcedimientoD = other.TipoProcedimientoD;
    this.ProcedimientoD     = other.ProcedimientoD;
    this.ClasTramiteD       = other.ClasTramiteD;
    this.TramiteD           = other.TramiteD;    
  }

  public void validate(String idioma) throws ValidationException {

    String   sufijo   = "";
    boolean  correcto = true;
    Messages errors   = new Messages();

    if ("euskera".equals(idioma)) sufijo="_eu";

    if (!errors.empty()) throw new ValidationException(errors);
    isValid = true;
  }

  public boolean IsValid() { 
    return isValid; 
  }
  private String Combo;

  private String Area;
  private String Unidad;
  private String TipoProcedimiento; 
  private String Procedimiento;
  private String ClasTramite;
  private String Tramite; 
  private String AreaD;
  private String UnidadD;
  private String TipoProcedimientoD; 
  private String ProcedimientoD;
  private String ClasTramiteD;
  private String TramiteD;
 

}