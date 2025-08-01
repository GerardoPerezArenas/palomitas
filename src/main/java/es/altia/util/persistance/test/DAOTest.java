/*______________________________BOF_________________________________*/
package es.altia.util.persistance.test;

import junit.framework.*;

import java.util.Iterator;
import java.util.Collection;
import java.util.Calendar;
import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.util.exceptions.*;
import es.altia.util.persistance.exceptions.*;
import es.altia.util.persistance.SQLDAO;
import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.SQLDAOFactory;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.jdbc.DataSourceLocator;
import es.altia.util.jdbc.SimpleDataSource;
import es.altia.util.persistance.*;
import es.altia.util.jdbc.*;
import es.altia.util.persistance.searchcriterias.*;





/**
  * @author
  *
  * This is a UNIT TEST CLASS. It uses Junit Framework.
  *
  **/
public abstract class DAOTest extends TestCase {

	/*_______Attributes_____________________________________________*/
    private static Log _log =
            LogFactory.getLog(DAOTest.class.getName());
    /*_______Operations_____________________________________________*/

	/**
	  *		Test for "create" and "exists" DAO's methods.
	  **/
	public PersistentObject dao_create_Test(PersistentObject vo) {

		_log.debug("####### dao_create_Test BEGINS HERE #########");
		Connection connection=getConnection();
		SQLDAO dao=getDAO();
		PersistentObject result = null;

		try {
			if (dao.exists(connection,vo.getPrimaryKey())) {
				/*Tiene que petar la inserción*/
				try {
                    try {
                        JdbcOperations.closeConnection(connection);
                    } catch (InternalErrorException ie) {
                        _log.error("dao_create_Test(): InternalErrorException closing connection!");
                        ie.printStackTrace();
                        assertTrue(false);
                    }//try-catch
                    connection=getConnection();
					result = dao.create(connection, vo);
					assertTrue(false);
				} catch(DuplicateInstanceException e) {
					_log.debug("dao_create_Test(): Duplicate instance. OK!");
				}//try-catch
			} else {
				/*No debe que petar la inserción*/
				try {
                    try {
                        JdbcOperations.closeConnection(connection);
                    } catch (InternalErrorException ie) {
                        _log.error("dao_create_Test(): InternalErrorException closing connection!");
                        ie.printStackTrace();
                        assertTrue(false);
                    }//try-catch
                    connection=getConnection();
					result = dao.create(connection, vo);
					if (_log.isDebugEnabled()) _log.debug("dao_create_Test(): Inserted with key "+result.getPrimaryKey()+". OK!");
				} catch(DuplicateInstanceException e) {
					assertTrue(false);
				}//try-catch
			}//if
		} catch(InternalErrorException e){
			_log.error("dao_create_Test(): InternalErrorException!");
			e.printStackTrace();
			assertTrue(false);
		} finally {
			try {
				JdbcOperations.closeConnection(connection);
			} catch (InternalErrorException ie) {
				_log.error("dao_create_Test(): InternalErrorException closing connection!");
				ie.printStackTrace();
				assertTrue(false);
			}//try-catch
		}//try-catch
		_log.debug("####### dao_create_Test ENDS HERE   #########");
		return result;
	}//dao_create_Test


	/**
	  *	Test for "find" DAO's method.
	  **/
	public PersistentObject dao_find_Test(PrimaryKey pk, boolean mustBeFound) {
		if (_log.isDebugEnabled()) _log.debug("####### dao_find_Test("+pk.toString()+") BEGINS HERE #########");
		Connection connection=getConnection();
		SQLDAO dao=getDAO();

		PersistentObject vo=null;
		try {
			vo = dao.retrieve(connection, pk);
			assertTrue(vo!=null);
            if (_log.isDebugEnabled()){
                _log.debug("dao_find_Test(): Instance found");
			    _log.debug("dao_find_Test(): "+vo.toString());
            }
			assertTrue(mustBeFound);
		} catch(InstanceNotFoundException e){
			_log.debug("dao_find_Test(): Instance not found");
			assertTrue(!mustBeFound);
		} catch(InternalErrorException e){
			_log.error("dao_find_Test(): InternalErrorExcepton!");
			e.printStackTrace();
			assertTrue(false);
		} finally {
			try {
				JdbcOperations.closeConnection(connection);
			} catch (InternalErrorException ie) {
				_log.debug("dao_find_Test(): InternalErrorExcepton closing connection!");
				ie.printStackTrace();
				assertTrue(false);
			}//try-catch
		}//try-catch
		_log.debug("####### dao_find_Test ENDS HERE   #########");
		return vo;
	}//dao_find_Test


	/**
	  *		Test for "update" DAO's method.
	  **/
	public void dao_update_Test(PersistentObject vo, boolean mustBeFound, boolean mustStale) {
		_log.debug("####### dao_update_Test BEGINS HERE #########");
		Connection connection=getConnection();
		SQLDAO dao=getDAO();

		try {
			dao.update(connection, vo);
			_log.debug("dao_update_Test(): Updated!");
			assertTrue(mustBeFound);
		} catch(InstanceNotFoundException e){
			_log.debug("dao_update_Test(): Instance not found!");
			assertTrue(!mustBeFound);
		} catch(StaleUpdateException e){
			_log.debug("dao_update_Test(): Stale update!");
			assertTrue(mustStale);
		} catch(InternalErrorException e){
			_log.error("dao_update_Test(): InternalErrorException!");
			e.printStackTrace();
			assertTrue(false);
		} finally {
			try {
				JdbcOperations.closeConnection(connection);
			} catch (InternalErrorException ie) {
				_log.error("dao_update_Test(): InternalErrorException closing connection!");
				ie.printStackTrace();
				assertTrue(false);
			}//try-catch
		}//try-catch
		_log.debug("####### dao_update_Test ENDS HERE   #########");
	}//dao_update_Test


	/**
	  *		Test for "delete" DAO's method.
	  **/
	public void dao_delete_Test(PrimaryKey pk, boolean mustBeFound) {
		if (_log.isDebugEnabled()) _log.debug("####### dao_delete_Test("+pk.toString()+") BEGINS HERE #########");
		Connection connection=getConnection();
		SQLDAO dao=getDAO();

		try {
			dao.delete(connection,pk);
			_log.debug("dao_delete_Test(): Removed!");
			assertTrue(mustBeFound);
		} catch(InstanceNotFoundException e){
			_log.debug("dao_delete_Test(): Instance not found");
			assertTrue(!mustBeFound);
		} catch(InternalErrorException e){
			_log.error("dao_delete_Test(): InternalErrorException!");
			e.printStackTrace();
			assertTrue(false);
		} catch (IntegrityViolationAttemptedException e) {
            _log.error("dao_delete_Test(): Integrity Violation Attempted!");
            e.printStackTrace();
            assertTrue(false);
        } finally {
			try {
				JdbcOperations.closeConnection(connection);
			} catch (InternalErrorException ie) {
				_log.error("dao_delete_Test(): InternalErrorException closing connection!");
				ie.printStackTrace();
				assertTrue(false);
			}//try-catch
		}//try-catch
		_log.debug("####### dao_delete_Test ENDS HERE   #########");
	}//dao_delete_Test



	/**
	  *		Test for "countAll" DAO's method.
	  **/
	public long dao_countAll_Test() {
		_log.debug("####### dao_countAll_Test() BEGINS HERE #########");
		Connection connection=getConnection();
		SQLDAO dao=getDAO();
		long result = -1;
		Collection allVOs = null;

		try {
			result = dao.countAll(connection);
			allVOs = dao.retrieveAll(connection);
			assertTrue( (result == allVOs.size()) );
			if (_log.isDebugEnabled()) _log.debug("dao_countAll_Test(): There are "+result+" instances in DB.");
		} catch(InternalErrorException e){
			_log.error("dao_countAll_Test(): InternalErrorException!");
			e.printStackTrace();
			assertTrue(false);
		} finally {
			try {
				JdbcOperations.closeConnection(connection);
			} catch (InternalErrorException ie) {
				_log.error("dao_countAll_Test(): InternalErrorException closing connection!");
				ie.printStackTrace();
				assertTrue(false);
			}//try-catch
		}//try-catch
		_log.debug("####### dao_countAll_Test ENDS HERE   #########");
		return result;
	}//dao_countAll_Test




	public DAOTest(String name) {
		super(name);
	}//Constructor for the test


	/**
	  * Private method for getting a connection
	  **/
	protected Connection getConnection() {
		DataSource dataSource=null;
		Connection connection=null;

		/*Get datasource*/
		try {
			dataSource = DataSourceLocator.getDataSource(getDSKey());
			assertTrue(dataSource!=null);
			connection = dataSource.getConnection();
			assertTrue(connection!=null);
			//connection.setAutoCommit(true);
			//connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			//connection.setReadOnly(false);
		} catch(Exception e){
			_log.fatal("Error creating dataSource");
			e.printStackTrace();
			assertTrue(false);
		}//try-catch
		return connection;
	}//getConnection

	/**
	  * Private method for getting the DAO instance
	  **/
    	protected SQLDAO getDAO(){
		SQLDAO dao=null;

		/*Get SQLDAO*/
		try {
			dao=SQLDAOFactory.getDAO(getVOClass());
		} catch(Exception e) {
			_log.fatal("Error using DAO Factory");
			e.printStackTrace();
			assertTrue(false);
		}//try-catch
		assertTrue(dao!=null);
		return dao;
	}//getDAO

	protected void setUp() {
		DataSourceLocator.addDataSource(getDSKey(),new SimpleDataSource());
		doSetUp();
	}//setUp

	protected abstract void doSetUp();

    	protected abstract Class getVOClass();

    	protected abstract String getDSKey();

}//End of the concrete test class
/*______________________________EOF_________________________________*/
