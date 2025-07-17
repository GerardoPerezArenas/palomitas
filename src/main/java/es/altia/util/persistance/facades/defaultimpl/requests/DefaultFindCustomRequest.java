/*______________________________BOF_________________________________*/
package es.altia.util.persistance.facades.defaultimpl.requests;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.persistance.SQLDAO;
import es.altia.util.persistance.daocommands.OrderCriteria;
import es.altia.util.persistance.searchcriterias.SearchCriteria;

import java.util.List;



/**
  * @author
  * @version      $\Revision$ $\Date$
  *
  **/
public class DefaultFindCustomRequest extends DefaultNonTxRORequest {
	/*_______Attributes_____________________________________________*/
	/*input*/
    private Class mainClass = null;
    private Class customClass = null;
	private SearchCriteria selCriteria = null;
	private OrderCriteria orderCriteria = null;
	private int startIndex = -1;
	private int count = -1;
	/*output*/
	private List result = null;
	/*exceptions*/

	/*_______Operations_____________________________________________*/
	public DefaultFindCustomRequest(Class mainVOClass, Class customVOClass, SearchCriteria selCriteria,
					OrderCriteria orderCriteria, 
					int startIndex, int count) {
        this.mainClass = mainVOClass;
        this.customClass = customVOClass;
		this.selCriteria = selCriteria;
		this.orderCriteria = orderCriteria;
		this.startIndex = startIndex;
		this.count = count;
	}//constructor

    public DefaultFindCustomRequest(String dsKey, Class mainVOClass, Class customVOClass,
                              SearchCriteria selCriteria, OrderCriteria orderCriteria,
                              int startIndex, int count) {
        txSetDataSourceKey(dsKey);
        this.mainClass = mainVOClass;
        this.customClass = customVOClass;
        this.selCriteria = selCriteria;
        this.orderCriteria = orderCriteria;
        this.startIndex = startIndex;
        this.count = count;
    }//constructor

	protected void doExecute()
		throws ModelException, InternalErrorException {
		try {
            if ( (mainClass == null) || (customClass == null) )
                throwInternalError("DefaultFindCustomRequest.doExecute() Null arguments!");
			final SQLDAO dao = getSQLDAOFactory().getDAOInstance(mainClass);
			result = dao.searchCustomVOByCriteria(txGetConnection(),
						selCriteria, orderCriteria,	
						( (startIndex>=0)?(new Integer(startIndex)):(null) ),
						( (count>=0)?(new Integer(count)):(null) ), customClass);
		} catch (InternalErrorException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalErrorException(e);
		}//try-catch
	}//doExecute

	/**
	  * @return List of found objects. Empty list if none found.
	  **/
	public List getResult() {
		return result;
	}//getResult

}//class
/*______________________________EOF_________________________________*/

