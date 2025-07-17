/*______________________________BOF_________________________________*/
package es.altia.agora.business.portafirmas.delegacionfirma.vo;

import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.PersistanceContext;

import java.util.Calendar;

/**
  * @version      $\Revision$ $\Date$
  *
  * This is the value object for the DelegacionFirma 
  * in our system.
  *
  **/
public class DelegacionFirmaVO implements PersistentObject {
	/*_______Attributes_____________________________________________*/
	protected PersistanceContext pPersistanceContext = null;
	protected DelegacionFirmaPK pPrimaryKey;
	protected int pUsuarioDelegado;
	protected Calendar pFechaDesde;
	protected Calendar pFechaHasta;

	/*_______Operations_____________________________________________*/
	public DelegacionFirmaVO( DelegacionFirmaPK pk,  int theUsuarioDelegado,  Calendar theFechaDesde,  Calendar theFechaHasta  ) {
		pPrimaryKey=pk;
		pUsuarioDelegado = theUsuarioDelegado;
		pFechaDesde = theFechaDesde;
		pFechaHasta = theFechaHasta;
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

                    
	public int getCodigoUsuario() {
		return pPrimaryKey.getCodigoUsuario();
	}//getCodigoUsuario
            
	public int getUsuarioDelegado() {
		return pUsuarioDelegado;
	}//getUsuarioDelegado

	public void setUsuarioDelegado(int newValue) {
		pUsuarioDelegado = newValue;
	}//setUsuarioDelegado

	public Calendar getFechaDesde() {
		return pFechaDesde;
	}//getFechaDesde

	public void setFechaDesde(Calendar newValue) {
		pFechaDesde = newValue;
	}//setFechaDesde

	public Calendar getFechaHasta() {
		return pFechaHasta;
	}//getFechaHasta

	public void setFechaHasta(Calendar newValue) {
		pFechaHasta = newValue;
	}//setFechaHasta



	/**
	  * String representation for debugging
	  * @return String
	  **/
	public String toString() {
		final StringBuffer buff = new StringBuffer("[DelegacionFirmaVO: ");
		buff.append("| PK=");
		buff.append(getPrimaryKey());
		buff.append("| UsuarioDelegado=");
		buff.append(pUsuarioDelegado);
		buff.append("| FechaDesde=");
		buff.append(pFechaDesde);
		buff.append("| FechaHasta=");
		buff.append(pFechaHasta);
		buff.append("| PersCtx=");
		buff.append(pPersistanceContext);
		buff.append("|]");
		return buff.toString();
	}//toString

}//class
/*______________________________EOF_________________________________*/

