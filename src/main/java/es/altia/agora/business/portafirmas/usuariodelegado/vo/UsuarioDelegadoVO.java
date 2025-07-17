/*______________________________BOF_________________________________*/
package es.altia.agora.business.portafirmas.usuariodelegado.vo;

import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.PersistanceContext;

/**
  * @version      $\Revision$ $\Date$
  *
  * This is the value object for the UsuarioDelegado 
  * in our system.
  *
  **/
public class UsuarioDelegadoVO implements PersistentObject {
	/*_______Attributes_____________________________________________*/
	protected PersistanceContext pPersistanceContext = null;
	protected UsuarioDelegadoPK pPrimaryKey;
	protected String pLogin;
	protected String pNif;
	protected String pNombre;
        protected String pBuzFir;

	/*_______Operations_____________________________________________*/
	public UsuarioDelegadoVO( UsuarioDelegadoPK pk,  String theLogin,  String theNif,  String theNombre, String theBuzFir ) {
		pPrimaryKey=pk;
		pLogin = theLogin;
		pNif = theNif;
		pNombre = theNombre;
                pBuzFir = theBuzFir;
	}//constructor

    public UsuarioDelegadoVO( int id,  String theLogin,  String theNif,  String theNombre, String theBuzFir  ) {
        this( new UsuarioDelegadoPK(id), theLogin, theNif, theNombre, theBuzFir );
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

                    
	public int getId() {
		return pPrimaryKey.getId();
	}//getId
            
	public String getLogin() {
		return pLogin;
	}//getLogin

	public void setLogin(String newValue) {
		pLogin = newValue;
	}//setLogin

	public String getNif() {
		return pNif;
	}//getNif

	public void setNif(String newValue) {
		pNif = newValue;
	}//setNif

	public String getNombre() {
		return pNombre;
	}//getNombre

	public void setNombre(String newValue) {
		pNombre = newValue;
	}//setNombre
        
        public String getBuzFir() {
		return pBuzFir;
	}//getBuzFir

	public void setBuzFir(String newValue) {
		pBuzFir = newValue;
	}//setBuzFir

	/**
	  * String representation for debugging
	  * @return String
	  **/
	public String toString() {
		final StringBuffer buff = new StringBuffer("[UsuarioDelegadoVO: ");
		buff.append("| PK=");
		buff.append(getPrimaryKey());
		buff.append("| Login=");
		buff.append(pLogin);
		buff.append("| Nif=");
		buff.append(pNif);
		buff.append("| Nombre=");
		buff.append(pNombre);
		buff.append("| PersCtx=");
		buff.append(pPersistanceContext);
                buff.append("| BuzFir=");
		buff.append(pBuzFir);
		buff.append("|]");
		return buff.toString();
	}//toString

}//class
/*______________________________EOF_________________________________*/

