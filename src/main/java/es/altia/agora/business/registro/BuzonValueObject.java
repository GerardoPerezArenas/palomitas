// NOMBRE DEL PAQUETE
package es.altia.agora.business.registro;

// PAQUETES IMPORTADOS
import java.io.Serializable;
import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase RelUnOrgValueObject</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.1
 */

public class BuzonValueObject implements Serializable, ValueObject {

   private int eje;
   private int num;
   private String fec;
   private String tip;
   private String asu;
   private int orgcod;
   private String orgdes;
   private int entcod;
   private String entnom;
   private int depcod;
   private String depnom;
   private int uorcod;
   private String uornom;

   // Variable booleana que indica si el estado de la instancia es válido o no
   private boolean isValid;

   public BuzonValueObject(){
	super();
   }

   public BuzonValueObject(int eje, int num, String fec,
				   String tip, String asu, int orgcod, String orgdes,
				   int entcod, String entnom, int depcod, String depnom,
				   int uorcod, String uornom){
	this.eje = eje;
	this.num = num;
	this.fec = fec;
	this.tip = tip;
	this.asu = asu;
	this.orgcod = orgcod;
	this.orgdes = orgdes;
	this.entcod = entcod;
	this.entnom = entnom;
	this.depcod = depcod;
	this.depnom = depnom;
	this.uorcod = uorcod;
	this.uornom = uornom;
   }

   public int getEje(){
	return eje;
   }

   public void setEje(int eje){
	this.eje = eje;
   }

   public long getNum(){
	return num;
   }

   public void setNum(int num){
	this.num = num;
   }

   public String getFec(){
	return fec;
   }

   public void setFec(String fec){
	this.fec = fec;
   }

   public String getAsu(){
	return asu;
   }

   public void setAsu(String asu){
	this.asu = asu;
   }

   public int getOrgcod(){
	return orgcod;
   }

   public void setOrgcod(int orgcod){
	this.orgcod = orgcod;
   }

   public String getOrgdes(){
	return orgdes;
   }

   public void setOrgdes(String orgdes){
	this.orgdes = orgdes;
   }

   public int getEntcod(){
	return entcod;
   }

   public void setEntcod(int entcod){
	this.entcod = entcod;
   }

   public String getEntnom(){
	return entnom;
   }

   public void setEntnom(String entnom){
	this.entnom = entnom;
   }


   public int getDepcod(){
	return depcod;
   }

   public void setDepcod(int depcod){
	this.depcod = depcod;
   }

   public String getDepnom(){
	return depnom;
   }

   public void setDepnom(String depnom){
	this.depnom = depnom;
   }

   public int getUorcod(){
	return uorcod;
   }

   public void setUorcod(int uorcod){
	this.uorcod = uorcod;
   }

   public String getUornom(){
	return uornom;
   }

   public void setUornom(String uornom){
	this.uornom = uornom;
   }

   public String getTip(){
	return tip;
   }

   public void setTip(String tip){
	this.tip = tip;
   }

   public void validate(String idioma) throws ValidationException {
	String sufijo = "";
	if ("euskera".equals(idioma)) sufijo="_eu";
	boolean correcto = true;
	Messages errors = new Messages();
	if (!errors.empty()) throw new ValidationException(errors);
	isValid = true;
   }

   /** Devuelve un booleano que representa si el estado de este Buzon es válido. */
   public boolean IsValid(){
	return isValid;
   }
}