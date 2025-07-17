/*______________________________BOF_________________________________*/
package es.altia.agora.business.sge.plantillafirma.vo;

import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.PersistanceContext;
import es.altia.util.commons.DebugOperations;

/**
  * @version      $\Revision$ $\Date$
  *
  * This is the value object for the PlantillaFirma 
  * in our system.
  *
  **/
public class PlantillaFirmaVO implements PersistentObject {
    /*_______Constants______________________________________________*/
    public static final String REQUIERE_FIRMA_NO = null;
    public static final String REQUIERE_FIRMA_TRAMITADOR = "T";
    public static final String REQUIERE_FIRMA_OTROS = "O";
    public static final String REQUIERE_FIRMA_FLUJO = "L";

	/*_______Attributes_____________________________________________*/
	protected PersistanceContext pPersistanceContext = null;
	protected PlantillaFirmaPK pPrimaryKey;
	protected String pRequiereFirma;
	protected int[] pIdsUsuariosFirmantes;
    protected int idFlujo;

	/*_______Operations_____________________________________________*/
    public PlantillaFirmaVO( PlantillaFirmaPK pk,  String theRequiereFirma,  int[] theIdsUsuariosFirmantes, int theIdFlujo) {
		pPrimaryKey=pk;
		pRequiereFirma = theRequiereFirma;
		pIdsUsuariosFirmantes = theIdsUsuariosFirmantes;
        idFlujo = theIdFlujo;
	}//constructor
        
	public PlantillaFirmaVO( PlantillaFirmaPK pk,  String theRequiereFirma,  int[] theIdsUsuariosFirmantes ) {
		pPrimaryKey=pk;
		pRequiereFirma = theRequiereFirma;
		pIdsUsuariosFirmantes = theIdsUsuariosFirmantes;
	}//constructor

    public PlantillaFirmaVO( PlantillaFirmaPK pk,  String theRequiereFirma ) {
        pPrimaryKey=pk;
        pRequiereFirma = theRequiereFirma;
        pIdsUsuariosFirmantes = new int[0];
    }//constructor

	public PrimaryKey getPrimaryKey() {
		return pPrimaryKey;
	}//getPrimaryKey

	public PersistanceContext getPersistanceContext() {
		return pPersistanceContext;
	}//getPersistanceContext

	public void setPersistanceContext(PersistanceContext ctx) {
		pPersistanceContext = ctx;
	}//setPersistanceContext

                    
	public int getIdMunicipio() {
		return pPrimaryKey.getIdMunicipio();
	}//getIdMunicipio
                            
	public String getIdProcedimiento() {
		return pPrimaryKey.getIdProcedimiento();
	}//getIdProcedimiento
                            
	public int getIdTramite() {
		return pPrimaryKey.getIdTramite();
	}//getIdTramite
                            
	public int getIdPlantilla() {
		return pPrimaryKey.getIdPlantilla();
	}//getIdPlantilla
            
	public String getRequiereFirma() {
		return pRequiereFirma;
	}//getRequiereFirma

	public void setRequiereFirma(String newValue) {
		pRequiereFirma = newValue;
	}//setRequiereFirma

	public int[] getIdsUsuariosFirmantes() {
		return pIdsUsuariosFirmantes;
	}//getIdsUsuariosFirmantes

	public void setIdsUsuariosFirmantes(int[] newValue) {
		pIdsUsuariosFirmantes = newValue;
	}//setIdsUsuariosFirmantes

    public int getIdFlujo() {
        return idFlujo;
    }

    public void setIdFlujo(int idFlujo) {
        this.idFlujo = idFlujo;
    }

	/**
	  * String representation for debugging
	  * @return String
	  **/
	public String toString() {
		final StringBuffer buff = new StringBuffer("[PlantillaFirmaVO: ");
		buff.append("| PK=");
		buff.append(getPrimaryKey());
		buff.append("| RequiereFirma=");
		buff.append(pRequiereFirma);
		buff.append("| IdsUsuariosFirmantes=");
		DebugOperations.appendDebugStringFor(pIdsUsuariosFirmantes,buff);
		buff.append("| PersCtx=");
		buff.append(pPersistanceContext);
        buff.append("| idFlujo=");
		buff.append(idFlujo);
		buff.append("|]");
		return buff.toString();
	}//toString

}//class
/*______________________________EOF_________________________________*/

