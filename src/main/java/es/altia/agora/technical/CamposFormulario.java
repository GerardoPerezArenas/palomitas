package es.altia.agora.technical;

import java.util.HashMap;

/**
 * <p>Titulo: Herramienta de Gestión de Contenidos</p>
 * <p>Descripcion: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: <font color="navy"><b>altia</b><font><font color="gray">consultores</font></p><img src="/logoaltia.gif"></p>
 * @author unascribed
 * @version 1.0
 */

public class CamposFormulario implements java.io.Serializable {

  private HashMap campos;

  public CamposFormulario() {
	this.campos = null;
  }

  public CamposFormulario(HashMap campos) {
    this.campos = campos;
  }

  public HashMap getCampos(){ return this.campos; }

  public String[] getNombresCampo() { return (String [])this.campos.keySet().toArray(); }

  public boolean contieneCampo(String nombreCampo){
    return campos.containsKey(nombreCampo);
  }

  public void set(String nombreCampo, Object valor){
    if(campos.containsKey(nombreCampo)) {
      campos.put(nombreCampo,valor);
    }
  }

  public Object get(String nombreCampo){
    try{
      return campos.get(nombreCampo);
    }catch(Exception e){
      return null;
    }
  }

  public Object get(String nombreCampo, String tipoJava){
    return this.get(nombreCampo,tipoJava,null,null);
  }

  public Object get(String nombreCampo, String tipoJava,String mascara, Long longitud){
    if(tipoJava==null) tipoJava = "";
    if(tipoJava.equals("java.math.BigDecimal")) return getBigDecimal(nombreCampo);
    else if(tipoJava.equals("java.lang.Boolean")) return getBoolean(nombreCampo);
    else if(tipoJava.equals("java.util.Date")) return getDate(nombreCampo,mascara);
    else if(tipoJava.equals("java.lang.Double")) return getDouble(nombreCampo);
    else if(tipoJava.equals("java.lang.Integer")) return getInteger(nombreCampo);
    else if(tipoJava.equals("java.lang.Long")) return getLong(nombreCampo);
    else if(tipoJava.equals("java.lang.Number")) return getNumber(nombreCampo);
    else if(tipoJava.equals("java.lang.String")) return getString(nombreCampo,longitud);
    else if(tipoJava.equals("java.sql.Time")) return getTime(nombreCampo,mascara);
    else if(tipoJava.equals("java.sql.Timestamp")) return getTimestamp(nombreCampo,mascara);
    else if(tipoJava.equals("java.sql.Date")) return getDate(nombreCampo,mascara);
    else throw new IllegalArgumentException("El tipo "+tipoJava+" no está soportado.");
  }

  public java.lang.String getString(String nombreCampo){
    return getString(nombreCampo,null);
  }

  public java.lang.String getString(String nombreCampo,Long longitud){
    Object obj = get(nombreCampo);
    if(obj==null) return null;
    String valor = obj.toString().trim();
    //Si tenemos una longitud máxima y es mayor que cero, comprobamos que la
    //longitud del valor del campo es igual o menor ue la longitud máxima...
    if(longitud!=null && longitud.intValue()>0 && longitud.intValue()<valor.length()){
      return null;
    }
    return valor;
  }

  public java.lang.Number getNumber(String nombreCampo){
    Object obj = get(nombreCampo);
    if(obj==null) return null;
    if(obj instanceof Number){
      return (Number)obj;
    }else{
      return null;
    }
  }

  public java.lang.Integer getInteger(String nombreCampo){
    Object obj = get(nombreCampo);
    if(obj==null) return null;
    if(obj instanceof Number) {
      return new Integer(((Number)obj).intValue());
    } else {
      try{
        return Integer.valueOf(obj.toString());
      }catch(NumberFormatException e){
        return null;
      }
    }
  }

  public java.lang.Long getLong(String nombreCampo){
    Object obj = get(nombreCampo);
    if(obj==null) return null;
    if(obj instanceof Number) {
      return new Long(((Number)obj).longValue());
    } else {
      try{
        return Long.valueOf(obj.toString());
      }catch(NumberFormatException e){
        return null;
      }
    }
  }

  public java.lang.Double getDouble(String nombreCampo){
    Object obj = get(nombreCampo);
    if(obj==null) return null;
    if(obj instanceof Number) {
      return new Double(((Number)obj).doubleValue());
    } else {
      try{
        return Double.valueOf(obj.toString());
      }catch(NumberFormatException e){
        return null;
      }
    }
  }

  public java.math.BigDecimal getBigDecimal(String nombreCampo){
    Object obj = get(nombreCampo);
    if(obj==null) return null;
    if(obj instanceof Number) {
      return new java.math.BigDecimal(((Number)obj).doubleValue());
    } else {
      try{
        return new java.math.BigDecimal(obj.toString());
      }catch(NumberFormatException e){
        return null;
      }
    }
  }

  public java.lang.Boolean getBoolean(String nombreCampo){
    Object obj = get(nombreCampo);
    if(obj==null) return null;
    if(obj instanceof Boolean) {
      return (Boolean)obj;
    } else {
      String s = obj.toString().trim().toLowerCase();
      if(s.equalsIgnoreCase("true") || s.equals("s") || s.equals("1")) {
        return new Boolean(true);
      }else if(s.equalsIgnoreCase("false") || s.equals("n") || s.equals("0")) {
        return new Boolean(false);
      }else {
        return null;
      }
    }
  }

  public java.util.Date getDate(String nombreCampo){
    return getDate(nombreCampo,"dd/MM/yyyy HH:mm");
  }

  public java.util.Date getDate(String nombreCampo, String mascara){
    Object obj = get(nombreCampo);
    if(obj==null) return null;
    if(obj instanceof java.util.Date) {
      return (java.util.Date)obj;
    }else {
      try{
        if(mascara==null || mascara=="") mascara = "dd/MM/yyyy HH:mm";
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(mascara);
        return df.parse(obj.toString());
      }catch(Exception ex){
        return null;
      }
    }
  }

  public java.sql.Time getTime(String nombreCampo){
    return getTime(nombreCampo, "HH:mm");
  }

  public java.sql.Time getTime(String nombreCampo,String mascara){
    Object obj = get(nombreCampo);
    if(obj==null) return null;
    if(obj instanceof java.util.Date) {
      return new java.sql.Time(((java.util.Date)obj).getTime());
    }else {
      try{
        if(mascara==null || mascara=="") mascara = "HH:mm";
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(mascara);
        return new java.sql.Time(df.parse(obj.toString()).getTime());
      }catch(Exception ex){
        return null;
      }
    }
  }

  public java.sql.Timestamp getTimestamp(String nombreCampo){
    return getTimestamp(nombreCampo, "dd/MM/yyyy HH:mm");
  }

  public java.sql.Timestamp getTimestamp(String nombreCampo, String mascara){
    Object obj = get(nombreCampo);
    if(obj==null) return null;
    if(obj instanceof java.util.Date) {
      return new java.sql.Timestamp(((java.util.Date)obj).getTime());
    }else {
      try{
        if(mascara==null || mascara=="") mascara = "dd/MM/yyyy HH:mm";
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(mascara);
        return new java.sql.Timestamp(df.parse(obj.toString()).getTime());
      }catch(Exception ex){
        return null;
      }
    }
  }

  public String toString() {
    String s="";
    java.util.Iterator it = this.campos.keySet().iterator();
    while(it.hasNext()){
      String key = (String)it.next();
      s += "\n ["+key+":"+campos.get(key)+"] ";
    }
    return s;
  }
}