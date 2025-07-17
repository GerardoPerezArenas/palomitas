/*______________________________BOF_________________________________*/
package es.altia.util.persistance.facades.defaultimpl.requests;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.SQLDAO;
import es.altia.util.commons.DebugOperations;

import java.sql.Connection;


/**
  * @author
  * @version      $\Revision$ $\Date$
  *
  **/
public class DefaultCreateOrUpdateRequest extends DefaultTxRequest {
	/*_______Attributes_____________________________________________*/
	/*input*/
    private Class voClass = null;
	private PersistentObject vo = null;
	/*output*/
	private PersistentObject result = null;
	/*exceptions*/
    private Exception ex = null;

    private boolean didExist = false;

	/*_______Operations_____________________________________________*/
	public DefaultCreateOrUpdateRequest(Class voClass, PersistentObject vo) {
        this.voClass = voClass;
		this.vo = vo;
	}//constructor

    public DefaultCreateOrUpdateRequest(String dsKey, Class voClass, PersistentObject vo) {
        txSetDataSourceKey(dsKey);
        this.voClass = voClass;
        this.vo = vo;
    }//constructor

	protected void doExecute()
		throws ModelException, InternalErrorException {
		try {
            if ( (voClass == null) || (vo == null) )
                throwInternalError("DefaultCreateOrUpdateRequest.doExecute() Null arguments!");
			final SQLDAO dao = getSQLDAOFactory().getDAOInstance(voClass);
            final Connection conn = txGetConnection();
            if (dao.exists(conn,vo.getPrimaryKey())) {
                didExist = true;
                dao.update(conn,vo);
                result = vo;
            } else
			    result = dao.create(conn, vo);
		} catch (InternalErrorException e) {
            this.ex = e;
			throw e;
		} catch (Exception e) {
            this.ex = e;
			throw new InternalErrorException(e);
		}//try-catch
	}//doExecute

	/**
	  * @return the object with given id or exception if no one found
	  **/
	public PersistentObject getResult() {
		return result;
	}//getResult

    /*_______Operations inherited from LoggableRequest____________*/
    public String getParamsLoggingInfo() {
        final StringBuffer result = new StringBuffer("DefaultCreateOrUpdateRequest(Class=");
        result.append(DebugOperations.getShortNameForClass(voClass));
        result.append(", VO=");
        result.append(vo);
        result.append(")");
        return result.toString();
    }

    public String getResultLoggingInfo() {
        if (ex==null)
            return ((didExist)?("Updated VO="):("Created VO="))+result;
        else
            return "Exception="+DebugOperations.getShortNameForClass(ex.getClass());
    }

}//class
/*______________________________EOF_________________________________*/

