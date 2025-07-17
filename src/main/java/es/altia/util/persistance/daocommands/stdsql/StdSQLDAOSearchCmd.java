/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.stdsql;

import es.altia.util.collections.CollectionsFactory;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.impl.BasePersistanceContext;
import es.altia.util.persistance.daocommands.*;
import es.altia.util.persistance.searchcriterias.SearchCriteria;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
  * @author
  * @version      $\Date$ $\Revision$
  **/
public class StdSQLDAOSearchCmd implements SQLDAOSearchCommand {
    /*_______Constants______________________________________________*/
    private static final String CLSNAME = "StdSQLDAOSearchCmd";
	public static final String COUNTER = "COUNTER";
	public static final String PERSISTENT_OBJECT = "PERSISTENT_OBJECT";
    public static final int QUERY_TIMEOUT = 60;

    /*_______Attributes_____________________________________________*/
    private SQLDAOCommandFactory cmdFactory = null;
	private SQLDAORetrieveCommandAdapter searchAdapter = null;
	private String sqlQuery = null;
    private static final Log _log =
            LogFactory.getLog(StdSQLDAOSearchCmd.class.getName());


    /*_______Operations_____________________________________________*/
	public StdSQLDAOSearchCmd(SQLDAOCommandFactory cmdFactory, SQLDAORetrieveCommandAdapter concreteAdapter ) {
        this.cmdFactory = cmdFactory;
		this.searchAdapter = concreteAdapter;
		
		final StringBuffer buff = new StringBuffer("SELECT ");
		buff.append(SQLDAOCommandsHelper.composeCommasList(concreteAdapter.rtGetSQLSelectedAttributeNames()));
        if (concreteAdapter.getVersionNumberAttributeName()!=null)
            buff.append(", ").append(concreteAdapter.getVersionNumberAttributeName());
		buff.append(" FROM ");
		buff.append(SQLDAOCommandsHelper.composeCommasList(concreteAdapter.rtGetSQLTableNames()));
		this.sqlQuery = buff.toString();
	}//constructor
	
	
	public List search(Connection connection, SearchCriteria whereClause, OrderCriteria orderClause,
								Integer startIndex, Integer count)
		throws InternalErrorException{
		Statement stmnt = null;
		ResultSet resultSet = null;
		int rowCount;
		final LimitCriteria limitCriteria;
		boolean limitNotSupported=false;

		try {
			if (_log.isInfoEnabled()) _log.info(CLSNAME+".search() BEGIN");

			/* Create "stmnt". */
			final StringBuffer queryStringBuffer = new StringBuffer(sqlQuery);
			if (whereClause!=null) {
				queryStringBuffer.append(" WHERE ( ");
				queryStringBuffer.append(whereClause.getSQLStringToExecuteDirectly());
				queryStringBuffer.append(" ) ");
			}//if
			if (orderClause!=null) {
				queryStringBuffer.append(" ORDER BY ");
				queryStringBuffer.append(orderClause.toSQLString());
			}//if
			if ( (startIndex!=null) && (count!=null)) {
				try {
					limitCriteria = cmdFactory.newLimitCriteria();
					limitCriteria.setStartIndex(startIndex.intValue());
					limitCriteria.setCount(count.intValue());
					queryStringBuffer.append( limitCriteria.getSQLStringToExecuteDirectly() );
				} catch (Exception e) {
					limitNotSupported=true;
				}//try-catch
			}//if
			final String queryString = queryStringBuffer.toString();
			//if (_log.isDebugEnabled()) _log.debug("StdSQLDAOSearchCmd:       QUERY=" + queryString);
            final boolean supportsForwardReadResultSets = connection.getMetaData().supportsResultSetConcurrency(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            if (supportsForwardReadResultSets)
                stmnt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            else
                stmnt = connection.createStatement();
            stmnt.setQueryTimeout(QUERY_TIMEOUT);//seconds

			/* limit resultset if no limitcriteria supported */
            if ( (startIndex!=null) && (count!=null) && (limitNotSupported) ) {
                stmnt.setMaxRows(startIndex.intValue()+count.intValue());
            }//if

			/* Execute query. */
            resultSet = stmnt.executeQuery(queryString);

			/* Get results. */
			final List result;
            if (count==null)
                result = CollectionsFactory.getInstance().newArrayList();
            else
                result = CollectionsFactory.getInstance().newArrayList(count.intValue());
			final Map resultMap = CollectionsFactory.getInstance().newHashMapForSizeLowerThan(2);
			if ( (startIndex!=null) && (count!=null) && (limitNotSupported)) {
				/* Advance up to startIndex */
				rowCount = -1;
				boolean thereAreMore;
				do {
					thereAreMore = resultSet.next();
					rowCount++;
				} while ( (rowCount < startIndex.intValue()) && thereAreMore );
				
				/* Get 'count' rows */
				rowCount = 0;
				while ( (rowCount < count.intValue()) && thereAreMore ) {
                    resultMap.put(StdSQLDAORetrieveCmd.COUNTER,new Integer(1));
                    searchAdapter.rtExtractVOFromResultSet(resultSet, resultMap);
                    PersistentObject obj = (PersistentObject) resultMap.get(StdSQLDAORetrieveCmd.PERSISTENT_OBJECT);
                    int i = ((Integer) resultMap.get(COUNTER)).intValue();
                    if (searchAdapter.getVersionNumberAttributeName()!=null) {
                       final long pVersionNumber = resultSet.getLong(i);
                        obj.setPersistanceContext(new BasePersistanceContext(pVersionNumber,false,false));
                    }//if
                    result.add(obj);
					rowCount++;
					thereAreMore = resultSet.next();
				}//while				
			} else  {
				/* Get all rows in ResultSet */
				while (resultSet.next()) {
					resultMap.put(StdSQLDAORetrieveCmd.COUNTER,new Integer(1));
					searchAdapter.rtExtractVOFromResultSet(resultSet, resultMap);
                    PersistentObject obj = (PersistentObject) resultMap.get(StdSQLDAORetrieveCmd.PERSISTENT_OBJECT);
                    int i = ((Integer) resultMap.get(COUNTER)).intValue();
                    if (searchAdapter.getVersionNumberAttributeName()!=null) {
                       final long pVersionNumber = resultSet.getLong(i);
                        obj.setPersistanceContext(new BasePersistanceContext(pVersionNumber,false,false));
                    }//if
                    result.add(obj);
				}//while
			}//if
			/* Return the value object. */
            if (_log.isInfoEnabled()) _log.info(CLSNAME+".search() END returning "+result.size()+" objects.");
			return result;
		} catch (SQLException e) {
			throw new InternalErrorException(e);
		} finally {
			JdbcOperations.closeResultSet(resultSet);
			JdbcOperations.closeStatement(stmnt);
		}//try-catch
	}//search
}//class
/*______________________________EOF_________________________________*/
