/*______________________________BOF_________________________________*/
package es.altia.util.persistance.facades.defaultimpl.requests;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.persistance.SQLDAO;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.exceptions.DuplicateInstanceException;
import es.altia.util.commons.DebugOperations;


/**
  * @author
  * @version      $\Revision$ $\Date$
  *
  **/
public class DefaultCreateRequest extends DefaultTxRequest {
	/*_______Attributes_____________________________________________*/
	/*input*/
    private Class voClass = null;
	private PersistentObject vo = null;
	/*output*/
	private PersistentObject result = null;
	/*exceptions*/
	private DuplicateInstanceException diex = null;
    private Exception ex = null;

	/*_______Operations_____________________________________________*/
	public DefaultCreateRequest(Class voClass, PersistentObject vo) {
        this.voClass = voClass;
		this.vo = vo;
	}//constructor

    public DefaultCreateRequest(String dsKey, Class voClass, PersistentObject vo) {
        txSetDataSourceKey(dsKey);
        this.voClass = voClass;
        this.vo = vo;
    }//constructor

	protected void doExecute()
		throws ModelException, InternalErrorException {
		try {
            if ( (voClass == null) || (vo == null) )
                throwInternalError("DefaultCreateRequest.doExecute() Null arguments!");
			final SQLDAO dao = getSQLDAOFactory().getDAOInstance(voClass);
			result = dao.create(txGetConnection(), this.vo);
		} catch (DuplicateInstanceException e) {
			this.diex=e;
            this.ex=e;
			throw e;
		} catch (InternalErrorException e) {
            this.ex=e;
			throw e;
		} catch (Exception e) {
            this.ex=e;
			throw new InternalErrorException(e);
		}//try-catch
	}//doExecute

	/**
	  * @return the object with given id or exception if no one found
	  **/
	public PersistentObject getResult()
		throws DuplicateInstanceException {
		if (diex!=null) throw diex;
		return result;
	}//getResult

    /*_______Operations inherited from LoggableRequest____________*/
    public String getParamsLoggingInfo() {
        final StringBuffer result = new StringBuffer("DefaultCreateRequest(Class=");
        result.append(DebugOperations.getShortNameForClass(voClass));
        result.append(", VO=");
        result.append(vo);
        result.append(")");
        return result.toString();
    }

    public String getResultLoggingInfo() {
        if (ex==null)
            return "Created VO="+result;
        else
            return "Exception="+DebugOperations.getShortNameForClass(ex.getClass());
    }

}//class
/*______________________________EOF_________________________________*/

