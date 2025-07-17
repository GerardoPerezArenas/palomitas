/*______________________________BOF_________________________________*/
package es.altia.agora.business.sge.plantillafirma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.List;
import java.util.Collection;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.SQLDAO;
import es.altia.util.persistance.daocommands.*;
import es.altia.util.persistance.exceptions.DuplicateInstanceException;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;
import es.altia.util.persistance.exceptions.StaleUpdateException;
import es.altia.util.persistance.exceptions.IntegrityViolationAttemptedException;
import es.altia.util.persistance.searchcriterias.SearchCriteria;
import es.altia.util.persistance.searchcriterias.stdsql.EqualSearchCriteria;
import es.altia.util.collections.CollectionsFactory;



import es.altia.agora.business.sge.plantillafirma.vo.*;
import es.altia.flexia.portafirmas.plugin.util.ConstantesPortafirmas;


/**
  * @version      $\Revision$ $\Date$
  * 
  * 
  **/
public class ConcreteSQLPlantillaFirmaDAO implements SQLDAO, 
						CustomPlantillaFirmaDAO,
						SQLDAOPKCommandAdapter,
						SQLDAOCreateCommandAdapter,
						SQLDAORetrieveCommandAdapter,
						SQLDAOUpdateCommandAdapter {
		
	/*____________ Constants ______________*/
	private static final String MAIN_TABLE_NAME		= "E_DOT";
	private static final String[] VO_TABLES			= {"E_DOT"};
	private static final String[] VO_ATTRIBS_PK 		= { "DOT_MUN", "DOT_PRO", "DOT_TRA", "DOT_COD" };
	private static final String[] VO_ATTRIBS_ALL		= { "DOT_MUN", "DOT_PRO", "DOT_TRA", "DOT_COD", "DOT_FRM" };
	private static final String[] VO_ATTRIBS_UPDATE		= { "DOT_FRM" };

    private static final String FIRMANTES_TABLE_NAME	= "E_DOT_FIR";
    private static final String[] FIRMANTES_ATTRIBS_PK	= { "DOT_MUN", "DOT_PRO", "DOT_TRA", "DOT_COD" };
    private static final String[] FLUJO_ATTRIBS_PK	= { "DOT_MUN", "DOT_PRO", "DOT_TRA", "DOT_COD", "USU_COD" };
    private static final String[] FIRMANTES_ATTRIBS_ALL	= { "DOT_MUN", "DOT_PRO", "DOT_TRA", "DOT_COD", "USU_COD" };
    private static final String[] FLUJO_ATTRIBS_ALL	= { "DOT_MUN", "DOT_PRO", "DOT_TRA", "DOT_COD", "USU_COD", "DOT_FLUJO" };
    private static final String FIRMANTES_INT_ATTRIB	= "USU_COD";
    private static final String FLUJO_INT_ATTRIB	= "DOT_FLUJO";
    



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

    private SQLDAOCreateIntArrayPropertyCommand createUsuariosFirmantesCmd = null;
    private SQLDAORetrieveIntArrayPropertyCommand retrieveUsuariosFirmantesCmd = null;
    private SQLDAODeleteByCriteriaCommand deleteUsuariosFirmantesCmd = null;
    private SQLDAOCreateIntArrayPropertyCommand createFlujoFirmantesCmd = null;
    private SQLDAORetrieveIntArrayPropertyCommand retrieveFlujoFirmantesCmd = null;
    private SQLDAODeleteByCriteriaCommand deleteFlujoFirmantesCmd = null;

    private class UsuariosFirmantesAdapter 
            implements SQLDAOPKCommandAdapter, 
            SQLDAOCreateIntArrayPropertyCommandAdapter,
            SQLDAORetrieveIntArrayPropertyCommandAdapter {

        /**
         * SQL main table name for the concrete VO
         */
        public String pkGetSQLMainTableName() {
            return FIRMANTES_TABLE_NAME;
        }

        /**
         * PrimaryKey SQL attribute names (ordered)
         */
        public String[] pkGetSQLPKAttributeNames() {
            return FIRMANTES_ATTRIBS_PK;
        }

        /**
         * Binds the PreparedStatement with PrimaryKey elements
         *
         * @param primaryKey concrete impl you must use for binding
         *                   preparedStatement	the JDBC statement to be bound
         *                   i			actual PreparedStatement positional field counter
         * @return positional counter incremented properly
         */
        public int pkBind(PrimaryKey primaryKey, PreparedStatement preparedStatement, int i) throws SQLException {
            int result = i;
            final PlantillaFirmaPK concretePK = (PlantillaFirmaPK) primaryKey;
		
            preparedStatement.setInt(result++, concretePK.getIdMunicipio());
            result = JdbcOperations.bindStringToStatement(concretePK.getIdProcedimiento(), 5, preparedStatement, result);
            preparedStatement.setInt(result++, concretePK.getIdTramite());
            preparedStatement.setInt(result++, concretePK.getIdPlantilla());
		
            return result;
        }


        /**
         * SQL attribute names for the INSERT command (included PK attributes,ordered)
         */
        public String[] cGetSQLAttributeNames() {
            return FIRMANTES_ATTRIBS_ALL;
        }

        /**
         * SQL table name for the concrete VO
         */
        public String cGetSQLTableName() {
            return FIRMANTES_TABLE_NAME;
        }

        /**
         * Binds the PreparedStatement with PrimaryKey elements
         *
         * @param pk concrete impl you must use for binding
         *           preparedStatement	the JDBC statement to be bound
         *           i			actual PreparedStatement positional field counter
         * @return positional counter incremented properly
         */
        public int cpkBind(PrimaryKey pk, PreparedStatement preparedStatement, int i) throws SQLException {
            int result = i;
            final PlantillaFirmaPK concretePK = (PlantillaFirmaPK) pk;
		
            preparedStatement.setInt(result++, concretePK.getIdMunicipio());
            result = JdbcOperations.bindStringToStatement(concretePK.getIdProcedimiento(), 5, preparedStatement, result);
            preparedStatement.setInt(result++, concretePK.getIdTramite());
            preparedStatement.setInt(result++, concretePK.getIdPlantilla());
		
            return result;
        }


        public String rtGetSQLPropertyAttributeName() {
            return FIRMANTES_INT_ATTRIB;
        }

        public String[] rtGetSQLFilterAttributeNames() {
            return FIRMANTES_ATTRIBS_PK;
        }

        public String[] rtGetSQLAttributeNames() {
            return FIRMANTES_ATTRIBS_ALL;
        }

        public String rtGetSQLTableName() {
            return FIRMANTES_TABLE_NAME;
        }

        public int rtFilterBind(PrimaryKey primaryKey, PreparedStatement preparedStatement, int i) throws SQLException {
            int result = i;
            final PlantillaFirmaPK concretePK = (PlantillaFirmaPK) primaryKey;

            preparedStatement.setInt(result++, concretePK.getIdMunicipio());
            result = JdbcOperations.bindStringToStatement(concretePK.getIdProcedimiento(), 5, preparedStatement, result);
            preparedStatement.setInt(result++, concretePK.getIdTramite());
            preparedStatement.setInt(result++, concretePK.getIdPlantilla());

            return result;
        }
    }//inner class
											  
        private class FlujoFirmantesAdapter 
            implements SQLDAOPKCommandAdapter, 
            SQLDAOCreateIntArrayPropertyCommandAdapter,
            SQLDAORetrieveIntArrayPropertyCommandAdapter {

        /**
         * SQL main table name for the concrete VO
         */
        public String pkGetSQLMainTableName() {
            return FIRMANTES_TABLE_NAME;
        }

        /**
         * PrimaryKey SQL attribute names (ordered)
         */
        public String[] pkGetSQLPKAttributeNames() {
            return FIRMANTES_ATTRIBS_PK;
        }

        /**
         * Binds the PreparedStatement with PrimaryKey elements
         *
         * @param primaryKey concrete impl you must use for binding
         *                   preparedStatement	the JDBC statement to be bound
         *                   i			actual PreparedStatement positional field counter
         * @return positional counter incremented properly
         */
        public int pkBind(PrimaryKey primaryKey, PreparedStatement preparedStatement, int i) throws SQLException {
            int result = i;
            final PlantillaFirmaPK concretePK = (PlantillaFirmaPK) primaryKey;
		
            preparedStatement.setInt(result++, concretePK.getIdMunicipio());
            result = JdbcOperations.bindStringToStatement(concretePK.getIdProcedimiento(), 5, preparedStatement, result);
            preparedStatement.setInt(result++, concretePK.getIdTramite());
            preparedStatement.setInt(result++, concretePK.getIdPlantilla());
            preparedStatement.setInt(result++, -1);
		
            return result;
        }


        /**
         * SQL attribute names for the INSERT command (included PK attributes,ordered)
         */
        public String[] cGetSQLAttributeNames() {
            return FLUJO_ATTRIBS_ALL;
        }

        /**
         * SQL table name for the concrete VO
         */
        public String cGetSQLTableName() {
            return FIRMANTES_TABLE_NAME;
        }

        /**
         * Binds the PreparedStatement with PrimaryKey elements
         *
         * @param pk concrete impl you must use for binding
         *           preparedStatement	the JDBC statement to be bound
         *           i			actual PreparedStatement positional field counter
         * @return positional counter incremented properly
         */
        public int cpkBind(PrimaryKey pk, PreparedStatement preparedStatement, int i) throws SQLException {
            int result = i;
            final PlantillaFirmaPK concretePK = (PlantillaFirmaPK) pk;
		
            preparedStatement.setInt(result++, concretePK.getIdMunicipio());
            result = JdbcOperations.bindStringToStatement(concretePK.getIdProcedimiento(), 5, preparedStatement, result);
            preparedStatement.setInt(result++, concretePK.getIdTramite());
            preparedStatement.setInt(result++, concretePK.getIdPlantilla());
            preparedStatement.setInt(result++, -1);
		
            return result;
        }


        public String rtGetSQLPropertyAttributeName() {
            return FLUJO_INT_ATTRIB;
        }

        public String[] rtGetSQLFilterAttributeNames() {
            return FIRMANTES_ATTRIBS_PK;
        }

        public String[] rtGetSQLAttributeNames() {
            return FLUJO_ATTRIBS_ALL;
        }

        public String rtGetSQLTableName() {
            return FIRMANTES_TABLE_NAME;
        }

        public int rtFilterBind(PrimaryKey primaryKey, PreparedStatement preparedStatement, int i) throws SQLException {
            int result = i;
            final PlantillaFirmaPK concretePK = (PlantillaFirmaPK) primaryKey;

            preparedStatement.setInt(result++, concretePK.getIdMunicipio());
            result = JdbcOperations.bindStringToStatement(concretePK.getIdProcedimiento(), 5, preparedStatement, result);
            preparedStatement.setInt(result++, concretePK.getIdTramite());
            preparedStatement.setInt(result++, concretePK.getIdPlantilla());

            return result;
        }
    }//inner class
    
	/*____________ Operations ______________*/
	/* *********************************************************************** */
	/* **           Modify this methods for your concrete DAO               ** */
	/* *********************************************************************** */
	public ConcreteSQLPlantillaFirmaDAO() {		
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
		retrieveCustomCmds.put(PlantillaFirmaVO.class.getName(), retrieveCmd);//{"PlantillaFirmaVO"->SQLDAORetrieveCmd}
		searchCustomCmds.put(PlantillaFirmaVO.class.getName(), searchCmd);//{"PlantillaFirmaVO"->SQLDAOSearchCmd}
        
        UsuariosFirmantesAdapter usuariosFirmantesAdapter = new UsuariosFirmantesAdapter();
        createUsuariosFirmantesCmd = cmdFactory.newCreateIntArrayPropertyCmd(usuariosFirmantesAdapter);
        retrieveUsuariosFirmantesCmd = cmdFactory.newRetrieveIntArrayPropertyCmd(usuariosFirmantesAdapter);
        deleteUsuariosFirmantesCmd = cmdFactory.newDeleteByCriteriaCmd(usuariosFirmantesAdapter);
        
        FlujoFirmantesAdapter flujoFirmantesAdapter = new FlujoFirmantesAdapter();
        createFlujoFirmantesCmd = cmdFactory.newCreateIntArrayPropertyCmd(flujoFirmantesAdapter);
        retrieveFlujoFirmantesCmd = cmdFactory.newRetrieveIntArrayPropertyCmd(flujoFirmantesAdapter);
        deleteFlujoFirmantesCmd = cmdFactory.newDeleteByCriteriaCmd(flujoFirmantesAdapter);
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
		final PlantillaFirmaPK concretePK = (PlantillaFirmaPK) primaryKey;
		
		preparedStatement.setInt(result++, concretePK.getIdMunicipio());
		result = JdbcOperations.bindStringToStatement(concretePK.getIdProcedimiento(), 5, preparedStatement, result);
		preparedStatement.setInt(result++, concretePK.getIdTramite());
		preparedStatement.setInt(result++, concretePK.getIdPlantilla());
		
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
		final PlantillaFirmaVO concreteVO = (PlantillaFirmaVO) vo;
		
		result = JdbcOperations.bindStringToStatement(concreteVO.getRequiereFirma(), 1, preparedStatement, result);
		
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
//		final PlantillaFirmaVO concreteVO = (PlantillaFirmaVO) givenVO;
//		final PlantillaFirmaPK concretePK = (PlantillaFirmaPK) concreteVO.getPrimaryKey();
//		final long _serial = ( (IfxStatement) preparedStatement).getSerial();
//		return new PlantillaFirmaVO(new PlantillaFirmaPK( _serial ),  concreteVO.getRequiereFirma(),  concreteVO.getIdsUsuariosFirmantes(), );
//		return new PlantillaFirmaVO((PlantillaFirmaPK) generatedKey,  concreteVO.getRequiereFirma(),  concreteVO.getIdsUsuariosFirmantes(), );

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
		final int theIdMunicipio = resultSet.getInt(i++);
		final String theIdProcedimiento = resultSet.getString(i++);
		final int theIdTramite = resultSet.getInt(i++);
		final int theIdPlantilla = resultSet.getInt(i++);
		final String theRequiereFirma = resultSet.getString(i++);
		
		/* Create VO. */
		final PersistentObject result = new PlantillaFirmaVO(new PlantillaFirmaPK( theIdMunicipio,  theIdProcedimiento,  theIdTramite,  theIdPlantilla  ),  theRequiereFirma );
		
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
		final PlantillaFirmaVO concreteVO = (PlantillaFirmaVO) po;
		
		result = JdbcOperations.bindStringToStatement(concreteVO.getRequiereFirma(), 1, preparedStatement, result);
		
		return result;
	}//upBindUpdateFieldsToStatement


	public String getVersionNumberAttributeName() {
	    return null;  //No version number
	    //return SQLDAOCreateCommandAdapter.VERSION_NUMBER_DEFAULT_ATTRIBUTE_NAME;
	}//getVersionNumberAttributeName







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
        PlantillaFirmaVO result = (PlantillaFirmaVO) createCmd.create(connection, newVO);
        if (ConstantesPortafirmas.FIRMA_FLUJO.equals(result.getRequiereFirma())) {
            createFlujoFirmantesCmd.create(connection, newVO.getPrimaryKey(), new int[]{result.getIdFlujo()});
        } else {
        createUsuariosFirmantesCmd.create(connection,newVO.getPrimaryKey(),result.getIdsUsuariosFirmantes());
        }
        
        return result;
	}//create

	public boolean exists(Connection connection, PrimaryKey primaryKey)
		throws InternalErrorException {
		return existsCmd.exists(connection, primaryKey);
	}//exists
	
	public PersistentObject retrieve(Connection connection, PrimaryKey primaryKey)
		throws InstanceNotFoundException, InternalErrorException {
		PlantillaFirmaVO result = (PlantillaFirmaVO) retrieveCmd.retrieve(connection, primaryKey);
        if (ConstantesPortafirmas.FIRMA_FLUJO.equals(result.getRequiereFirma())) {
            int[] idFlujo = retrieveFlujoFirmantesCmd.retrieveIntArray(connection,primaryKey);
            if (idFlujo != null) {
                result.setIdFlujo(idFlujo[0]);
            }
        } else {
        result.setIdsUsuariosFirmantes(retrieveUsuariosFirmantesCmd.retrieveIntArray(connection,primaryKey));
        }
        
        return result;
	}//retrieve
	
	public PersistentObject retrieveCustom(Connection connection, PrimaryKey primaryKey, Class customVO)
		throws InstanceNotFoundException, InternalErrorException {
		final SQLDAORetrieveCommand customCmd = (SQLDAORetrieveCommand) retrieveCustomCmds.get(customVO.getName());
		if (customCmd == null)
			throw new InternalErrorException(new Exception("ConcreteSQLPlantillaFirmaDAO: Invalid CustomVO class"));
		return customCmd.retrieve(connection, primaryKey);
	}//retrieveCustom
	
	public void update(Connection connection, PersistentObject newVO)
		throws InstanceNotFoundException, StaleUpdateException, InternalErrorException {
        PlantillaFirmaVO result = (PlantillaFirmaVO) newVO;
		updateCmd.update(connection, newVO);

        EqualSearchCriteria sc = null;
        sc = cmdFactory.getSearchCriteriaFactory().newEqualSearchCriteria(FIRMANTES_ATTRIBS_PK[0]);
        sc.setTo(new Integer(result.getIdMunicipio()));
        SearchCriteria wholeSC = sc;
        sc = cmdFactory.getSearchCriteriaFactory().newEqualSearchCriteria(FIRMANTES_ATTRIBS_PK[1]);
        sc.setTo(result.getIdProcedimiento());
        wholeSC = wholeSC.and(sc);
        sc = cmdFactory.getSearchCriteriaFactory().newEqualSearchCriteria(FIRMANTES_ATTRIBS_PK[2]);
        sc.setTo(new Integer(result.getIdTramite()));
        wholeSC = wholeSC.and(sc);
        sc = cmdFactory.getSearchCriteriaFactory().newEqualSearchCriteria(FIRMANTES_ATTRIBS_PK[3]);
        sc.setTo(new Integer(result.getIdPlantilla()));
        wholeSC = wholeSC.and(sc);

        if (ConstantesPortafirmas.FIRMA_FLUJO.equals(result.getRequiereFirma())) {
            deleteFlujoFirmantesCmd.delete(connection,wholeSC);
            createFlujoFirmantesCmd.create(connection,newVO.getPrimaryKey(),new int[]{result.getIdFlujo()});
        } else {
        deleteUsuariosFirmantesCmd.delete(connection,wholeSC);
        createUsuariosFirmantesCmd.create(connection,newVO.getPrimaryKey(),result.getIdsUsuariosFirmantes());
        }
        
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
			throw new InternalErrorException(new Exception("ConcreteSQLPlantillaFirmaDAO: Invalid CustomVO class"));
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


