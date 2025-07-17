/*______________________________BOF_________________________________*/
package es.altia.agora.business.portafirmas.usuariodelegado.dao;

import es.altia.agora.business.portafirmas.usuariodelegado.vo.UsuarioDelegadoPK;
import es.altia.agora.business.portafirmas.usuariodelegado.vo.UsuarioDelegadoVO;
import es.altia.agora.business.util.GlobalNames;
import es.altia.util.collections.CollectionsFactory;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.SQLDAO;
import es.altia.util.persistance.daocommands.*;
import es.altia.util.persistance.exceptions.DuplicateInstanceException;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;
import es.altia.util.persistance.exceptions.IntegrityViolationAttemptedException;
import es.altia.util.persistance.exceptions.StaleUpdateException;
import es.altia.util.persistance.searchcriterias.SearchCriteria;
import es.altia.util.persistance.searchcriterias.stdsql.EqualSearchCriteria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


/**
  * @version      $\Revision$ $\Date$
  * 
  * 
  **/
public class ConcreteSQLUsuarioDelegadoDAO implements SQLDAO, 
						CustomUsuarioDelegadoDAO,
						SQLDAOPKCommandAdapter,
						SQLDAOCreateCommandAdapter,
                                                SQLDAORetrieveCommandAdapter,
						SQLDAOUpdateCommandAdapter {
		
	/*____________ Constants ______________*/
    private static final Integer INTEGER_ONE = new Integer(1);
    private static final Integer INTEGER_ZERO = new Integer(0);

	private static final String MAIN_TABLE_NAME		= GlobalNames.ESQUEMA_GENERICO + "A_USU A_USU";
	private static final String[] VO_TABLES			= {GlobalNames.ESQUEMA_GENERICO +"A_USU"};
	private static final String[] VO_ATTRIBS_PK 		= { "USU_COD" };
        private static final String[] VO_ATTRIBS_ALL		= { "USU_COD", "USU_LOG", "USU_NIF", "USU_NOM", "USU_BUZFIR" };
	private static final String[] VO_ATTRIBS_UPDATE		= { "USU_LOG", "USU_NIF", "USU_NOM" };
    private static final String NIF = "USU_NIF";
    private static final String FIRMANTE = "USU_FIRMANTE";
    private static final String NOMBRE_USU = "USU_NOM";
    private static final String BUZONFIRMA = "USU_BUZFIR";




    /*____________ Attributes ______________*/
	private SQLDAOCommandFactory cmdFactory = null;
	private SQLDAOCreateCommand createCmd = null;
	private SQLDAOExistsCommand existsCmd = null;
	private SQLDAORetrieveCommand retrieveCmd = null;
	private SQLDAOUpdateCommand updateCmd = null;
	private SQLDAODeleteCommand deleteCmd = null;
	private SQLDAOSearchCommand searchCmd = null;
	private SQLDAOCountCommand countCmd = null;
	private SQLDAODeleteByCriteriaCommand deleteByCriteriaCmd = null;
	private Map retrieveCustomCmds = CollectionsFactory.getInstance().newHashMap(2);
	private Map searchCustomCmds = CollectionsFactory.getInstance().newHashMap(2);


    /*____________ Operations ______________*/
	/* *********************************************************************** */
	/* **           Modify this methods for your concrete DAO               ** */
	/* *********************************************************************** */
	public ConcreteSQLUsuarioDelegadoDAO() {		
	}//constructor

	public void initForDb(String dbKey) throws InternalErrorException {
		/* instanciate adapters and set-up them */
		cmdFactory = SQLDAOCommandFactory.getInstance(dbKey);
		createCmd =  cmdFactory.newCreateCmd(this);
		existsCmd = cmdFactory.newExistsCmd(this);
		retrieveCmd = cmdFactory.newRetrieveCmd(this);
		updateCmd = cmdFactory.newUpdateCmd(this);
		deleteCmd = cmdFactory.newDeleteCmd(this);
		searchCmd = cmdFactory.newSearchCmd(this);
		countCmd = cmdFactory.newCountCmd(this);
		deleteByCriteriaCmd = cmdFactory.newDeleteByCriteriaCmd(this);
		retrieveCustomCmds.put(UsuarioDelegadoVO.class.getName(), retrieveCmd);//{"UsuarioDelegadoVO"->SQLDAORetrieveCmd}
		searchCustomCmds.put(UsuarioDelegadoVO.class.getName(), searchCmd);//{"UsuarioDelegadoVO"->SQLDAOSearchCmd}
	}//initForDb



	/**
	  * Binds the PreparedStatement with PrimaryKey elements
	  *
	  * @param 	primaryKey		concrete impl you must use for binding
	  *		preparedStatement	the JDBC statement to be bound
	  *		i			actual PreparedStatement positional field counter
	  * @return positional counter incremented properly
	  **/
	public int pkBind(PrimaryKey primaryKey, PreparedStatement preparedStatement, int i) 
		throws SQLException {
		int result = i;
		final UsuarioDelegadoPK concretePK = (UsuarioDelegadoPK) primaryKey;
		
		preparedStatement.setInt(result++, concretePK.getId());
		
		return result;
	}//pkBind

	/**
	  * Binds the PreparedStatement with all PersistentObject properties but PrimaryKey ones 
	  *
	  * @param 	vo			concrete impl you must use for binding
	  *		preparedStatement	the JDBC statement to be bound
	  *		i			actual PreparedStatement positional field counter
	  * @return positional counter incremented properly
	  **/
	public int crBindAllNonPK(PersistentObject vo, PreparedStatement preparedStatement, int i) 
		throws SQLException {
		int result = i;
		final UsuarioDelegadoVO concreteVO = (UsuarioDelegadoVO) vo;
		
		result = JdbcOperations.bindStringToStatement(concreteVO.getLogin(), 10, preparedStatement, result);
		result = JdbcOperations.bindStringToStatement(concreteVO.getNif(), 10, preparedStatement, result);
		result = JdbcOperations.bindStringToStatement(concreteVO.getNombre(), 40, preparedStatement, result);
		
		return result;
	}//crBindAllNonPK

	/**
	  * Shall I generate a PrimaryKey for this object?
	  **/
	public boolean crMustGeneratePK(PrimaryKey primaryKey) {
		return (false);
	}//crMustGeneratePK

	/**
	  * Generate a PrimaryKey for this PersistentObject
	  **/
	public PrimaryKey crPreGenerateKey(Connection connection, PersistentObject vo) 
		throws SQLException {
		return vo.getPrimaryKey();
	}//crPreGenerateKey

	/**
	  * Compose the complete PersistentObject with a given one and generated PrimaryKey
	  * if it was generated
	  *
	  * @param 	connection		the JDBC connection (maybe you need it to get generated key)
	  *		preparedStatement	the JDBC statement (maybe you need it to get generated key)
	  *		givenVO			the VO with its properties
	  *		generatedKey		the PRE-generated key (may be null if it is POST-generated)
	  * @return the complete PersistenObject (with generated PrimaryKey if it was generated)
	  **/
	public PersistentObject crReturnNewVO(Connection connection, PreparedStatement preparedStatement, PersistentObject givenVO, PrimaryKey generatedKey)
		throws SQLException {
//		final UsuarioDelegadoVO concreteVO = (UsuarioDelegadoVO) givenVO;
//		final UsuarioDelegadoPK concretePK = (UsuarioDelegadoPK) concreteVO.getPrimaryKey();
//		final long _serial = ( (IfxStatement) preparedStatement).getSerial();
//		return new UsuarioDelegadoVO(new UsuarioDelegadoPK( _serial ),  concreteVO.getLogin(),  concreteVO.getNif(),  concreteVO.getNombre(), );
//		return new UsuarioDelegadoVO((UsuarioDelegadoPK) generatedKey,  concreteVO.getLogin(),  concreteVO.getNif(),  concreteVO.getNombre(), );

		return givenVO;
	}//crReturnNewVO

	/**
	  * uses two entries in resultMap for returning results: <ol>
	  * <li> COUNTER : Integer (ResultSet offset)</li>
	  * <li> PERSISTENT_OBJECT : PersistentObject (The result object) </li>
	  * </ol>
	  **/
	public void rtExtractVOFromResultSet(ResultSet resultSet, Map resultMap) 
		throws SQLException {
		int i = ((Integer)resultMap.get(SQLDAORetrieveCommand.COUNTER)).intValue();
		
		/* Get results. */
		final int theId = resultSet.getInt(i++);
		final String theLogin = resultSet.getString(i++);
		final String theNif = resultSet.getString(i++);
		final String theNombre = resultSet.getString(i++);
                String theBuzFir = resultSet.getString(i++);
             
		/* Create VO. */
		final PersistentObject result = new UsuarioDelegadoVO(new UsuarioDelegadoPK( theId  ),  theLogin,  theNif,  theNombre, theBuzFir );
		
		/* Return the value object. */
		resultMap.put(SQLDAORetrieveCommand.COUNTER,new Integer(i));
		resultMap.put(SQLDAORetrieveCommand.PERSISTENT_OBJECT,result);
	}//rtExtractVOFromResultSet

	/**
	  * Binds the PreparedStatement with all PersistentObject properties to be updated
	  *
	  * @param 	po			concrete impl you must use for binding
	  *		preparedStatement	the JDBC statement to be bound
	  *		i			actual PreparedStatement positional field counter
	  * @return positional counter incremented properly
	  **/
	public int upBindUpdateFieldsToStatement(PersistentObject po, PreparedStatement preparedStatement, int i) 
		throws SQLException {
		int result = i;
		final UsuarioDelegadoVO concreteVO = (UsuarioDelegadoVO) po;
		
		result = JdbcOperations.bindStringToStatement(concreteVO.getLogin(), 10, preparedStatement, result);
		result = JdbcOperations.bindStringToStatement(concreteVO.getNif(), 10, preparedStatement, result);
		result = JdbcOperations.bindStringToStatement(concreteVO.getNombre(), 40, preparedStatement, result);
		
		return result;
	}//upBindUpdateFieldsToStatement


	public String getVersionNumberAttributeName() {
	    return null;  //No version number
	    //return SQLDAOCreateCommandAdapter.VERSION_NUMBER_DEFAULT_ATTRIBUTE_NAME;
	}//getVersionNumberAttributeName


    public SearchCriteria searchBySoloFirmantes(boolean firmantes, boolean obligatorioNif, boolean obligBuzonFirma) {
        StringBuilder criterio = new StringBuilder();
        
        criterio.append(FIRMANTE).append(" = ").append((firmantes)?(INTEGER_ONE):(INTEGER_ZERO));
        
        if (obligatorioNif) {
            criterio.append(" AND ").append(NIF).append(" IS NOT NULL ");
        }
        
        if (obligBuzonFirma) {
            criterio.append(" AND ").append(BUZONFIRMA).append(" IS NOT NULL ");
        }
        
        return new SearchCriteria(criterio.toString());
    }

    public OrderCriteria orderByNombre(boolean ascendente) {
        return new OrderCriteria(NOMBRE_USU, ascendente);
    }


    /* *********************************************************************** */
	/* **           Do not touch bellow if not necessary                    ** */
	/* *********************************************************************** */
	public String pkGetSQLMainTableName() {
		return MAIN_TABLE_NAME;
	}//pkGetSQLMainTableName

	public String[] pkGetSQLPKAttributeNames() {
		return VO_ATTRIBS_PK;
	}//pkGetSQLPKAttributeNames

        
	public String[] crGetSQLAttributeNames() {
            return VO_ATTRIBS_ALL;
	}//crGetSQLAttributeNames

	public SQLDAOExistsCommand crGetExistsCmd() {
		return existsCmd;
	}//crGetExistsCmd

	public String[] rtGetSQLSelectedAttributeNames() {
                return VO_ATTRIBS_ALL;
	}//rtGetSQLSelectedAttributeNames

	public String[] rtGetSQLTableNames() {
		return VO_TABLES;
	}//rtGetSQLTableNames

	public String[] upGetSQLUpdateAttributeNames() {
		return VO_ATTRIBS_UPDATE;
	}//upGetSQLUpdateAttributeNames

	public PersistentObject create(Connection connection, PersistentObject newVO)
		throws DuplicateInstanceException, InternalErrorException {
		return createCmd.create(connection, newVO);
	}//create

	public boolean exists(Connection connection, PrimaryKey primaryKey)
		throws InternalErrorException {
		return existsCmd.exists(connection, primaryKey);
	}//exists
	
	public PersistentObject retrieve(Connection connection, PrimaryKey primaryKey)
		throws InstanceNotFoundException, InternalErrorException {
		return retrieveCmd.retrieve(connection, primaryKey);
	}//retrieve
	
	public PersistentObject retrieveCustom(Connection connection, PrimaryKey primaryKey, Class customVO)
		throws InstanceNotFoundException, InternalErrorException {
		final SQLDAORetrieveCommand customCmd = (SQLDAORetrieveCommand) retrieveCustomCmds.get(customVO.getName());
		if (customCmd == null)
			throw new InternalErrorException(new Exception("ConcreteSQLUsuarioDelegadoDAO: Invalid CustomVO class"));
		return customCmd.retrieve(connection, primaryKey);
	}//retrieveCustom
	
	public void update(Connection connection, PersistentObject newVO)
		throws InstanceNotFoundException, StaleUpdateException, InternalErrorException {
		updateCmd.update(connection, newVO);
	}//update
	
	public void delete(Connection connection, PrimaryKey primaryKey)
		throws InstanceNotFoundException, InternalErrorException, IntegrityViolationAttemptedException {
		deleteCmd.delete(connection, primaryKey);
	}//delete


	public Collection retrieveAll(Connection connection)
		throws InternalErrorException {
			return searchByCriteria(connection, null, null,	null, null );
	}//retrieveAll

	public Collection retrieveAll(Connection connection, int startIndex, int count)
		throws InternalErrorException {
		if ( (startIndex>=0) && (count > 0 ) ) 
			return searchByCriteria(connection, null, null,	new Integer(startIndex), new Integer(count) );
		else
			return CollectionsFactory.getInstance().newArrayList(0);
	}//retrieveAll

	public List searchByCriteria(Connection connection, SearchCriteria whereClause, OrderCriteria orderClause)
		throws InternalErrorException{
			return searchByCriteria(connection, whereClause, orderClause, null, null);
	}//searchByCriteria

	public List searchByCriteria(Connection connection, SearchCriteria whereClause, OrderCriteria orderClause,
								Integer startIndex, Integer count)
		throws InternalErrorException {
		return searchCmd.search(connection, whereClause, orderClause, startIndex, count);
	}//searchByCriteria

	public List searchCustomVOByCriteria(Connection connection, SearchCriteria whereClause, 
						OrderCriteria orderClause, Integer startIndex, Integer count,
						Class customVO)
		throws InternalErrorException {
		final SQLDAOSearchCommand customCmd = (SQLDAOSearchCommand) searchCustomCmds.get(customVO.getName());
		if (customCmd == null)
			throw new InternalErrorException(new Exception("ConcreteSQLUsuarioDelegadoDAO: Invalid CustomVO class"));
		return customCmd.search(connection, whereClause, orderClause, startIndex, count);
	}//searchByCriteria

		
	public long countAll(Connection connection)
		throws InternalErrorException {
		return countByCriteria(connection, null);	
	}//countAll

	public long countByCriteria(Connection connection, SearchCriteria criteria)
		throws InternalErrorException {
		return countCmd.count(connection,criteria);
	}//countByCriteria

	public int delete(Connection connection, SearchCriteria whereClause)
		throws InternalErrorException{
		return deleteByCriteriaCmd.delete(connection,whereClause);
	}//delete

}//class
/*______________________________EOF_________________________________*/


