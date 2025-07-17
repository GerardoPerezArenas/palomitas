/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.stdsql;

import es.altia.util.collections.CollectionsFactory;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.daocommands.SQLDAORetrieveCommand;
import es.altia.util.persistance.daocommands.SQLDAORetrieveCommandAdapter;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;
import es.altia.util.persistance.impl.BasePersistanceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;



/**
  * @author
  * @version      $\Date$ $\Revision$
  *
  **/
public class StdSQLDAORetrieveCmd implements SQLDAORetrieveCommand {
    /*_______Atributes______________________________________________*/
    private static final String CLSNAME = "StdSQLDAORetrieveCmd";
    private static final Log _log =
            LogFactory.getLog(StdSQLDAORetrieveCmd.class.getName());
    protected SQLDAORetrieveCommandAdapter retrieveAdapter = null;
    protected String sqlQuery = null;

    /*_______Operations_____________________________________________*/
	public StdSQLDAORetrieveCmd( SQLDAORetrieveCommandAdapter concreteAdapter ) {
		this.retrieveAdapter = concreteAdapter;
		
		final StringBuffer buff = new StringBuffer("SELECT ");
		buff.append(SQLDAOCommandsHelper.composeCommasList(concreteAdapter.rtGetSQLSelectedAttributeNames()));
        if (retrieveAdapter.getVersionNumberAttributeName()!=null)
            buff.append(", ").append(concreteAdapter.getVersionNumberAttributeName());
        buff.append(" FROM ");
		buff.append(SQLDAOCommandsHelper.composeCommasList(concreteAdapter.rtGetSQLTableNames()));
		buff.append(" WHERE ( ");
		buff.append(SQLDAOCommandsHelper.composeWhereCriteriaList(concreteAdapter.pkGetSQLPKAttributeNames()));
		buff.append(" )");
		this.sqlQuery = buff.toString();
	}//constructor
	
	
	public PersistentObject retrieve(Connection connection, PrimaryKey primaryKey)
		throws InstanceNotFoundException, InternalErrorException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int i;

		try {
			if (_log.isInfoEnabled()) _log.info(CLSNAME+".retrieve("+primaryKey+") BEGIN");

			/* Create "preparedStatement". */
			if (_log.isDebugEnabled()) _log.debug(CLSNAME+".retrieve(): QUERY= " + sqlQuery);
			preparedStatement = connection.prepareStatement( sqlQuery );

			/* Fill "preparedStatement". */
			retrieveAdapter.pkBind(primaryKey, preparedStatement, 1);

			/* Execute query. */
			resultSet = preparedStatement.executeQuery();
            //if (_log.isDebugEnabled()) _log.debug("StdSQLDAORetrieveCmd: resultSet= " + resultSet);

			if (!resultSet.next()) {
				if (_log.isErrorEnabled()) _log.error(CLSNAME+".retrieve(): InstanceNotFoundException retrieving object with pk="+primaryKey);
				throw new InstanceNotFoundException(PersistentObject.class.getName(),primaryKey.toString());
			}//if

			/* Return the value object. */
			i = 1;
			final Map resultMap = CollectionsFactory.getInstance().newHashMapForSizeLowerThan(2);
			resultMap.put(COUNTER,new Integer(i));
			retrieveAdapter.rtExtractVOFromResultSet(resultSet, resultMap);
			final PersistentObject result = (PersistentObject) resultMap.get(PERSISTENT_OBJECT);
            i = ((Integer) resultMap.get(COUNTER)).intValue();
            if (retrieveAdapter.getVersionNumberAttributeName()!=null) {
               final long pVersionNumber = resultSet.getLong(i);
                result.setPersistanceContext(new BasePersistanceContext(pVersionNumber,false,false));
            }//if
            if (_log.isDebugEnabled()) _log.debug(CLSNAME+".retrieve(): result= " + result);
            if (_log.isInfoEnabled()) _log.info(CLSNAME+".retrieve() END");
			return result;
		} catch (SQLException e) {
			throw new InternalErrorException(e);
		} finally {
			JdbcOperations.closeResultSet(resultSet);
			JdbcOperations.closeStatement(preparedStatement);
		}//try-catch
	}//retrieve
}//class
/*______________________________EOF_________________________________*/
