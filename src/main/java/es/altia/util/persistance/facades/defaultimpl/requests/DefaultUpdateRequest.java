/*______________________________BOF_________________________________*/
package es.altia.util.persistance.facades.defaultimpl.requests;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.persistance.SQLDAO;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;
import es.altia.util.persistance.exceptions.StaleUpdateException;
import es.altia.util.commons.DebugOperations;



/**
  * @author
  * @version      $\Revision$ $\Date$
  *
  * Default Update operation for a single PersistenObject.
  **/
public class DefaultUpdateRequest extends DefaultTxRequest {
	/*_______Attributes_____________________________________________*/
	/*input*/
    private Class pVOClass = null;
	private PersistentObject vo=null;
	/*output*/
	/*exceptions*/
    Exception ex = null;

	/*_______Operations_____________________________________________*/
	public DefaultUpdateRequest(Class voClass, PersistentObject vo) {
        this.pVOClass = voClass;
		this.vo = vo;
	}//constructor

    public DefaultUpdateRequest(String dsKey, Class voClass, PersistentObject vo) {
        txSetDataSourceKey(dsKey);
        this.pVOClass = voClass;
        this.vo = vo;
    }//constructor

	protected void doExecute()
		throws ModelException, InternalErrorException {
		try {
            if ( (pVOClass == null) || (vo == null) )
                throwInternalError("DefaultUpdateRequest.doExecute() Null arguments!");
			final SQLDAO dao = getSQLDAOFactory().getDAOInstance(pVOClass);
			dao.update(txGetConnection(), vo);
		} catch (InstanceNotFoundException e) {
            this.ex = e;
			throw e;
        } catch (StaleUpdateException e) {
            this.ex = e;
            throw e;
		} catch (InternalErrorException e) {
            this.ex = e;
			throw e;
        } catch (Exception e) {
            this.ex = e;
            throw new InternalErrorException(e);
		}//try-catch
	}//doExecute

	public void getResult() {
	}//getResult

    /*_______Operations inherited from LoggableRequest____________*/
    public String getParamsLoggingInfo() {
        final StringBuffer result = new StringBuffer("DefaultUpdateRequest(Class=");
        result.append(DebugOperations.getShortNameForClass(pVOClass));
        result.append(", VO=");
        result.append(vo);
        result.append(")");
        return result.toString();
    }

    public String getResultLoggingInfo() {
        if (ex==null)
            return "Updated VO="+vo;
        else
            return "Exception="+DebugOperations.getShortNameForClass(ex.getClass());
    }

}//class
/*______________________________EOF_________________________________*/

