/*______________________________BOF_________________________________*/
package es.altia.util.persistance.facades.defaultimpl.requests;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.persistance.SQLDAO;
import es.altia.util.persistance.searchcriterias.SearchCriteria;

/**
  * @author
  * @version      $\Revision$ $\Date$
  *
  * Default Count operation for a PersistentObject.
  **/
public class DefaultCountRequest extends DefaultNonTxRORequest {
	/*_______Attributes_____________________________________________*/
	/*input*/
    private Class voClass = null;
	private SearchCriteria selCriteria = null;
	/*output*/
	private long result = 0;
    /*exceptions*/

	/*_______Operations_____________________________________________*/
	public DefaultCountRequest(Class voClass, SearchCriteria selCriteria) {
        this.voClass = voClass;
		this.selCriteria = selCriteria;
	}//constructor

    public DefaultCountRequest(String dsKey, Class voClass, SearchCriteria selCriteria) {
        txSetDataSourceKey(dsKey);
        this.voClass = voClass;
        this.selCriteria = selCriteria;
    }//constructor

	protected void doExecute()
		throws ModelException, InternalErrorException {
		try {
            if ( (voClass == null) )
                throwInternalError("DefaultCountRequest.doExecute() Null Arguments!");
			final SQLDAO dao = getSQLDAOFactory().getDAOInstance(voClass);
			result = dao.countByCriteria(txGetConnection(), selCriteria);
		} catch (InternalErrorException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalErrorException(e);
		}//try-catch
	}//doExecute

	/**
	  * @return the count of objects satisfying the criteria
	  **/
	public long getResult() {
		return result;
	}//getResult

}//class
/*______________________________EOF_________________________________*/

