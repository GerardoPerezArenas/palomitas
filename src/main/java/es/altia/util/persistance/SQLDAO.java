/*______________________________BOF_________________________________*/
package es.altia.util.persistance;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.persistance.daocommands.OrderCriteria;
import es.altia.util.persistance.exceptions.DuplicateInstanceException;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;
import es.altia.util.persistance.exceptions.StaleUpdateException;
import es.altia.util.persistance.exceptions.IntegrityViolationAttemptedException;
import es.altia.util.persistance.searchcriterias.SearchCriteria;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;


/**
  * @version      $\Date$ $\Revision$
  *
  * The interface must at least implement all DAO's.
  *
  **/
public interface SQLDAO {

    /**
     * Init the DAO for the concrete DB
     * @param dbKey the key
     * @see es.altia.util.persistance.daocommands.SQLDAOCommandFactory for available keys
     */
    void initForDb(String dbKey) throws InternalErrorException;


	/**
	  * Inserts in the DB a new object using a PersistentObject
	  * @return The VO just created
	  **/
	public PersistentObject create(Connection connection, PersistentObject newVO)
		throws DuplicateInstanceException, InternalErrorException;

	/**
	  * Checks if there's an object in DB identified by provided 'primaryKey'
	  **/
	public boolean exists(Connection connection, PrimaryKey primaryKey)
		throws InternalErrorException;

	/**
	  * Retrieves a concrete PersistentObject from DB (the one identified by 'primaryKey')
	  **/
	public PersistentObject retrieve(Connection connection, PrimaryKey primaryKey)
		throws InstanceNotFoundException, InternalErrorException;

	/**
	  * Retrieves a concrete Custom PersistentObject from DB (the one identified by 'primaryKey')
	  * You must show custom VO class
	  **/
	public PersistentObject retrieveCustom(Connection connection, PrimaryKey primaryKey, Class customVO)
		throws InstanceNotFoundException, InternalErrorException;

	/**
	  * Updates the provided PersistentObject (identified by it's primaryKey) in DB
	  **/
	public void update(Connection connection, PersistentObject newVO)
		throws InstanceNotFoundException, StaleUpdateException, InternalErrorException;

	/**
	  * Deletes in DB the PersistentObject identified by the 'primaryKey'
	  **/
	public void delete(Connection connection, PrimaryKey primaryKey)
		throws InstanceNotFoundException, InternalErrorException, IntegrityViolationAttemptedException;


	/**
	  * Counts all PersistentObject's in DB
	  **/
	public long countAll(Connection connection)
		throws InternalErrorException;

	/**
	  * Counts all PersistentObject's in DB satisfying the criteria
	  **/
	public long countByCriteria(Connection connection, SearchCriteria criteria)
		throws InternalErrorException;


	/**
	  * Retrieves all PersistentObject's from DB
	  **/
	public Collection retrieveAll(Connection connection)
		throws InternalErrorException;

	/**
	  * Retrieves all PersistentObject's from DB ('count' objects starting in 'startIndex')
	  **/
	public Collection retrieveAll(Connection connection, int startIndex, int count)
		throws InternalErrorException;

	/**
	  * Retrieves all PersistentObject's from DB ('count' objects starting in 'startIndex')
	  * satisfying given criterias for selection and ordering
	  **/
	public List searchByCriteria(Connection connection, SearchCriteria whereClause, OrderCriteria orderClause,
								Integer startIndex, Integer count)
		throws InternalErrorException;
		

	/**
	  * Retrieves all PersistentObject's from DB ('count' objects starting in 'startIndex')
	  * satisfying given criterias for selection and ordering
	  * It returns a list of custom VOs
	  **/
	public List searchCustomVOByCriteria(Connection connection, SearchCriteria whereClause, 
						OrderCriteria orderClause, Integer startIndex, Integer count,
						Class customVO)
		throws InternalErrorException;

	/**
	  * Retrieves all PersistentObject's from DB satisfying given criterias for selection and ordering
	  **/
	public List searchByCriteria(Connection connection, SearchCriteria whereClause, OrderCriteria orderClause)
		throws InternalErrorException;
		

	/**
	  * Deletes in DB the PersistentObject satisfying given criterias
	  **/
	public int delete(Connection connection, SearchCriteria whereClause)
		throws InternalErrorException;

}//class
/*______________________________EOF_________________________________*/
