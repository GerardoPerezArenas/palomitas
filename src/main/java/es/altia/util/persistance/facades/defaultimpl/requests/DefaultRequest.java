/*______________________________BOF_________________________________*/
package es.altia.util.persistance.facades.defaultimpl.requests;

import es.altia.util.exceptions.ModelException;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.persistance.SQLDAOFactory;
import es.altia.util.persistance.GlobalNames;
import es.altia.util.jdbc.DataSourceLocator;
import es.altia.util.configuration.ConfigurationParametersManager;
import es.altia.util.configuration.MissingConfigurationParameterException;

import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public abstract class DefaultRequest {
    /*_______Constants______________________________________________*/
    private static final String DEFAULT_DSKEY_PARAMETER = "CON.jndi";

    /*_______Attributes_____________________________________________*/
    protected static final Log _log =
            LogFactory.getLog(DefaultRequest.class.getName());
    private Connection conn = null;
    private String dsKey = null;

    /*_______Operations_____________________________________________*/
    public String txGetDataSourceKey() {
        if (dsKey!=null) {
            return dsKey;
        } else {
            try {
                dsKey = ConfigurationParametersManager.getParameter(DEFAULT_DSKEY_PARAMETER);
            } catch (MissingConfigurationParameterException e) {
                dsKey = GlobalNames.DEFAULT_DATASOURCE;
            }//try-catch
            return dsKey;
        }//if
    }//txGetDataSourceKey

    protected void txSetDataSourceKey(String dsKey) {
        this.dsKey = dsKey;
    }//txSetDataSourceKey

    public void txSetConnection(Connection connection) {
        conn = connection;
    }//txSetConnection

    public Connection txGetConnection() {
        return conn;
    }//txGetConnection

    public boolean txIsTopLevel() {
        return true;
    }//txIsTopLevel

    public void execute()
        throws ModelException, InternalErrorException {
        if (conn!=null) {
            doExecute();
        } else {
            throw new InternalErrorException(new Exception("Null Connection"));
        }//if
    }//execute

    protected abstract void doExecute()
        throws ModelException, InternalErrorException;

    protected final SQLDAOFactory getSQLDAOFactory()
            throws InternalErrorException {
        if (txGetDataSourceKey()==null)
            return null;
        else
            return SQLDAOFactory.getInstance(DataSourceLocator.getDBKeyForDataSource(txGetDataSourceKey()));
    }//getSQLDAOFactory

    protected void throwInternalError(String message)
            throws InternalErrorException {
        _log.error(message);
        throw new InternalErrorException(new Exception(message));
    }//throwInternalError

}//class
/*______________________________EOF_________________________________*/
