/*______________________________BOF_________________________________*/
package es.altia.util.persistance.facades.defaultimpl.requests;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.persistance.SQLDAO;
import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;
import es.altia.util.persistance.exceptions.IntegrityViolationAttemptedException;
import es.altia.util.commons.DebugOperations;


/**
  * @author
  * @version      $\Revision$ $\Date$
  *
  * Concrete request implementation for PlainCvFacadeDelegate:
  * Remove operation.
  **/
public class DefaultRemoveRequest extends DefaultTxRequest {
	/*_______Attributes_____________________________________________*/
	/*input*/
    private Class voClass = null;
	private PrimaryKey objectToRemove = null;
	/*output*/
	/*exceptions*/
	Exception ex = null;

	/*_______Operations_____________________________________________*/
	public DefaultRemoveRequest(Class voClass, PrimaryKey objectToRemove) {
        this.voClass = voClass;
		this.objectToRemove = objectToRemove;
	}//constructor

    public DefaultRemoveRequest(String dsKey, Class voClass, PrimaryKey objectToRemove) {
        txSetDataSourceKey(dsKey);
        this.voClass = voClass;
        this.objectToRemove = objectToRemove;
    }//constructor

	protected void doExecute()
		throws ModelException, InternalErrorException {
		try {
            if ( (voClass == null) || (objectToRemove == null) )
                throwInternalError("DefaultRemoveRequest.doExecute() Null arguments!");
			final SQLDAO dao = getSQLDAOFactory().getDAOInstance(voClass);
			dao.delete(txGetConnection(), objectToRemove);
		} catch (InstanceNotFoundException e) {
			this.ex = e;
			throw e;
        } catch (IntegrityViolationAttemptedException e) {
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
        final StringBuffer result = new StringBuffer("DefaultRemoveRequest(Class=");
        result.append(DebugOperations.getShortNameForClass(voClass));
        result.append(", PK=");
        result.append(objectToRemove);
        result.append(")");
        return result.toString();
    }

    public String getResultLoggingInfo() {
        if (ex==null)
            return "Removed PK="+objectToRemove;
        else
            return "Exception="+DebugOperations.getShortNameForClass(ex.getClass());
    }

}//class
/*______________________________EOF_________________________________*/

