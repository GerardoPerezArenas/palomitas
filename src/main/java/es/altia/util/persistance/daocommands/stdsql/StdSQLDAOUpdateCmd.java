/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.stdsql;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.daocommands.SQLDAOUpdateCommand;
import es.altia.util.persistance.daocommands.SQLDAOUpdateCommandAdapter;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;
import es.altia.util.persistance.exceptions.StaleUpdateException;
import es.altia.util.persistance.impl.BasePersistanceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
  * @author
  * @version      $\Date$ $\Revision$
  **/
public class StdSQLDAOUpdateCmd  implements SQLDAOUpdateCommand {
    /*_______Atributes______________________________________________*/
    private static final Log _log =
            LogFactory.getLog(StdSQLDAOUpdateCmd.class.getName());
    
    protected SQLDAOUpdateCommandAdapter updateAdapter;
    protected String sqlQuery = null;
	private String sqlVersionNumberQuery = null;

    /*_______Operations_____________________________________________*/
	public StdSQLDAOUpdateCmd( SQLDAOUpdateCommandAdapter concreteAdapter ) {
		this.updateAdapter = concreteAdapter;
		
		final String[] pkAttribs = concreteAdapter.pkGetSQLPKAttributeNames();
		final String pkWhere = " WHERE ( " + SQLDAOCommandsHelper.composeWhereCriteriaList(pkAttribs) + " ) ";
		
		/*Update query*/
		StringBuffer buff = new StringBuffer("UPDATE ");
		buff.append(concreteAdapter.pkGetSQLMainTableName().split(" ")[0]);
		buff.append(" SET ");
		final String[] updateAttribs = concreteAdapter.upGetSQLUpdateAttributeNames();
		for (int i=0;i<updateAttribs.length;i++) {
			buff.append(updateAttribs[i]);
            if (i < updateAttribs.length-1) buff.append("=?, ");
            else if (i == updateAttribs.length-1) buff.append("=? ");
		}//for
        if (concreteAdapter.getVersionNumberAttributeName()!=null)
		    buff.append(", pVersion = pVersion+1 ");
		buff.append(pkWhere);
		this.sqlQuery = buff.toString();

        if (concreteAdapter.getVersionNumberAttributeName()!=null) {
            /*Read version number query*/
            buff = new StringBuffer("SELECT pVersion FROM ");
            buff.append(concreteAdapter.pkGetSQLMainTableName());
            buff.append(pkWhere);
            this.sqlVersionNumberQuery = buff.toString();
        }//if
	}//constructor
	
	private long readVersionNumber(PreparedStatement preparedStatement, PrimaryKey primaryKey)
		throws InstanceNotFoundException, InternalErrorException {
		ResultSet resultSet = null;
		try {
			/* Fill "preparedStatement". */
			updateAdapter.pkBind(primaryKey, preparedStatement, 1);

			/* Execute query. */
			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				//if (_log.isInfoEnabled()) _log.info("StdSQLDAOUpdateCmd: InstanceNotFoundException reading version number");
				_log.error("StdSQLDAOUpdateCmd: InstanceNotFoundException reading version number");
				throw new InstanceNotFoundException(PersistentObject.class.getName(), primaryKey.toString());
			}//if
			final long result = resultSet.getLong(1);
			if (_log.isInfoEnabled()) _log.info("StdSQLDAOUpdateCmd: version="+result);
			return result;
		} catch (SQLException e) {
			throw new InternalErrorException(e);
		} finally {
			JdbcOperations.closeResultSet(resultSet);
		}//try-catch
	}//readVersionNumber

	private BasePersistanceContext getPersistanceContext(PersistentObject vo) {
		return (BasePersistanceContext) vo.getPersistanceContext();
	}//getPersistanceContext
		
	
	public void update(Connection connection, PersistentObject theVO)
		throws InstanceNotFoundException, StaleUpdateException, InternalErrorException {
		PreparedStatement preparedStatement = null;
		PreparedStatement versionNumberPreparedStatement = null;

		try {
			if (_log.isInfoEnabled()) _log.info("StdSQLDAOUpdateCmd: BEGIN update("+theVO.getPrimaryKey()+")");
			/* Get Persistance Context */
			BasePersistanceContext ctx = getPersistanceContext(theVO);
			if (ctx==null) ctx = new BasePersistanceContext(0,false,true);
			
            long databaseVersionNumber;
            if (updateAdapter.getVersionNumberAttributeName()!=null) {
                /* Create "preparedStatement" for reading version number. */
                versionNumberPreparedStatement = connection.prepareStatement( sqlVersionNumberQuery );

                /* Read database version number */
			    databaseVersionNumber = readVersionNumber(versionNumberPreparedStatement, theVO.getPrimaryKey());

			    JdbcOperations.closeStatement(versionNumberPreparedStatement);

                /* Check Version number */
                if (databaseVersionNumber != ctx.getVersionNumber()) {
                    //if (_log.isInfoEnabled()) _log.info("StdSQLDAOUpdateCmd: StaleUpdateException updating PersistentObject");
                    _log.error("StdSQLDAOUpdateCmd: StaleUpdateException updating PersistentObject");
                    throw new StaleUpdateException(PersistentObject.class.getName(), theVO.getPrimaryKey().toString());
                }//if
            }//if



            /* !!!! OJO !!!! */
//			if (ctx.isDirty()) {

            //if (_log.isDebugEnabled()) _log.debug("StdSQLDAOUpdateCmd:     QUERY = "+ sqlQuery);

            /* Create "preparedStatement". */
            preparedStatement = connection.prepareStatement( sqlQuery );
            /* Fill "preparedStatement". */
            int i = 1;
            i = updateAdapter.upBindUpdateFieldsToStatement(theVO, preparedStatement, i);
            updateAdapter.pkBind(theVO.getPrimaryKey(), preparedStatement, i);

            /* Execute query. */
            final int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                //if (_log.isInfoEnabled()) _log.info("StdSQLDAOUpdateCmd: InstanceNotFoundException updating PersistentObject");
                _log.error("StdSQLDAOUpdateCmd: InstanceNotFoundException updating PersistentObject");
                throw new InstanceNotFoundException(PersistentObject.class.getName(), theVO.getPrimaryKey().toString());
            }//if

            if (updatedRows > 1) {
                //if (_log.isInfoEnabled()) _log.info("StdSQLDAOUpdateCmd: DuplicateInstanceException updating PersistentObject");
                _log.error("StdSQLDAOUpdateCmd: DuplicateInstanceException updating PersistentObject");
                throw new SQLException("Duplicate row for pk = '" + theVO.getPrimaryKey().toString() + "' in table '"+updateAdapter.pkGetSQLMainTableName()+"'");
            }//if
JdbcOperations.closeStatement(preparedStatement);
            if (updateAdapter.getVersionNumberAttributeName()!=null) {
versionNumberPreparedStatement = connection.prepareStatement( sqlVersionNumberQuery );
                /* Update VO persistance context */
                databaseVersionNumber = readVersionNumber(versionNumberPreparedStatement, theVO.getPrimaryKey());
                ctx.setVersionNumber(databaseVersionNumber);
                ctx.setDirty(false);
                ctx.setNew(false);
            }//if

            if (_log.isInfoEnabled()) _log.info("StdSQLDAOUpdateCmd: END update()");
		} catch (SQLException e) {
			throw new InternalErrorException(e);
		} finally {
            if (updateAdapter.getVersionNumberAttributeName()!=null)
			    JdbcOperations.closeStatement(versionNumberPreparedStatement);
		//	JdbcOperations.closeStatement(preparedStatement);
		}//try-catch
	}//update

}//class
/*______________________________EOF_________________________________*/
