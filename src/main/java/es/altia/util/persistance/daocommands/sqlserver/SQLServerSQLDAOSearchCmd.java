/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.sqlserver;

import es.altia.util.collections.CollectionsFactory;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.impl.BasePersistanceContext;
import es.altia.util.persistance.daocommands.*;
import es.altia.util.persistance.daocommands.stdsql.SQLDAOCommandsHelper;
import es.altia.util.persistance.daocommands.stdsql.StdSQLDAORetrieveCmd;
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
public class SQLServerSQLDAOSearchCmd implements SQLDAOSearchCommand {
    /*_______Constants______________________________________________*/
    private static final String CLSNAME = "SQLServerSQLDAOSearchCmd";
	public static final String COUNTER = "COUNTER";
	public static final String PERSISTENT_OBJECT = "PERSISTENT_OBJECT";
    public static final int QUERY_TIMEOUT = 60;

    /*_______Attributes_____________________________________________*/
	private SQLDAORetrieveCommandAdapter searchAdapter = null;
	private String sqlQuery = null;
    private static final Log _log =
            LogFactory.getLog(SQLServerSQLDAOSearchCmd.class.getName());
    
    private String sqlSelectClause = null;


    /*_______Operations_____________________________________________*/
	public SQLServerSQLDAOSearchCmd(SQLDAORetrieveCommandAdapter concreteAdapter ) {
		this.searchAdapter = concreteAdapter;
		
		final StringBuffer buff = new StringBuffer("SELECT ");
		buff.append(SQLDAOCommandsHelper.composeCommasList(concreteAdapter.rtGetSQLSelectedAttributeNames()));
        if (concreteAdapter.getVersionNumberAttributeName()!=null)
            buff.append(", ").append(concreteAdapter.getVersionNumberAttributeName());
        sqlSelectClause = buff.toString();
		buff.append(" FROM ");
		buff.append(SQLDAOCommandsHelper.composeCommasList(concreteAdapter.rtGetSQLTableNames()));
		this.sqlQuery = buff.toString();
	}//constructor
	
	
	public List search(Connection connection, SearchCriteria whereClause, OrderCriteria orderClause,
								Integer startIndex, Integer count)
		throws InternalErrorException{
		Statement stmnt = null;
		ResultSet resultSet = null;

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
            String queryString = queryStringBuffer.toString();
			if ( (startIndex!=null) && (count!=null)) {
                final int si = (startIndex!=null)? startIndex.intValue() : 0;
                final int cn = (count!=null)? count.intValue() : 0;
                final StringBuffer buff = new StringBuffer();
                if (si > 0) buff.append(sqlSelectClause).append(" FROM ( ");
                if (cn > 0) buff.append(sqlSelectClause).append(", ROWNUM AS ORACLE_RN FROM ( ");
                buff.append(queryStringBuffer);
                if (cn > 0) buff.append(" ) WHERE (ROWNUM <= ").append(si+cn).append(")");
                if (si > 0) buff.append(" ) WHERE (ORACLE_RN > ").append(si).append(")");
                queryString = buff.toString();
			}//if
			if (_log.isInfoEnabled()) _log.info(CLSNAME+".search():      QUERY=" + queryString);
                        
               if(whereClause.isExpedienteHistorico()) { 
                  // Si el expediente está en el histórico, la tabla a consultar no es E_CRD_FIR sino HIST_E_CRD_FIR
                  queryString = queryString.replaceFirst("E_CRD_FIR","HIST_E_CRD_FIR");                            
               }                                
                        
            final boolean supportsForwardReadResultSets = connection.getMetaData().supportsResultSetConcurrency(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            if (supportsForwardReadResultSets)
                stmnt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            else
                stmnt = connection.createStatement();
            stmnt.setQueryTimeout(QUERY_TIMEOUT);//seconds

			/* Execute query. */
            resultSet = stmnt.executeQuery(queryString);

			/* Get results. */
			final List result;
            if (count==null)
                result = CollectionsFactory.getInstance().newArrayList();
            else
                result = CollectionsFactory.getInstance().newArrayList(count.intValue());
			final Map resultMap = CollectionsFactory.getInstance().newHashMapForSizeLowerThan(2);
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
