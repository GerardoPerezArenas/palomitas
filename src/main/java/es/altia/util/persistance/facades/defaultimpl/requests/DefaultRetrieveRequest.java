/*______________________________BOF_________________________________*/
package es.altia.util.persistance.facades.defaultimpl.requests;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.SQLDAO;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;


/**
  * @author
  * @version      $\Revision$ $\Date$
  *
  * Default retrieve operation for a single PersistentObject.
  **/
public class DefaultRetrieveRequest extends DefaultNonTxRORequest {
	/*_______Attributes_____________________________________________*/
	/*input*/
    private Class voClass = null;
	private PrimaryKey pk = null;
	/*output*/
	private PersistentObject result = null;
	/*exceptions*/
	private InstanceNotFoundException infex = null;

	/*_______Operations_____________________________________________*/
	public DefaultRetrieveRequest(Class voClass, PrimaryKey pk) {
        this.voClass = voClass;
		this.pk = pk;
	}//constructor

    public DefaultRetrieveRequest(String dsKey, Class voClass, PrimaryKey pk) {
        txSetDataSourceKey(dsKey);
        this.voClass = voClass;
        this.pk = pk;
    }//constructor

	protected void doExecute()
		throws ModelException, InternalErrorException {
		try {
            if ( (voClass == null) || (pk == null) )
                throwInternalError("DefaultRetrieveRequest.doExecute() Null arguments!");
			final SQLDAO dao = getSQLDAOFactory().getDAOInstance(voClass);
			result = dao.retrieve(txGetConnection(), this.pk);
		} catch (InstanceNotFoundException e) {
			this.infex=e;
			throw e;
		} catch (InternalErrorException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalErrorException(e);
		}//try-catch
	}//doExecute

	/**
	  * @return the object with given id or exception if no one found
	  **/
	public PersistentObject getResult()
		throws InstanceNotFoundException {
		if (infex!=null) throw infex;
		return result;
	}//getResult

}//class
/*______________________________EOF_________________________________*/

