package es.altia.agora.business.portafirmas.documentofirma.dao;

import es.altia.util.persistance.SQLDAO;
import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.searchcriterias.SearchCriteria;
import es.altia.util.persistance.searchcriterias.stdsql.InSearchCriteria;
import es.altia.util.persistance.searchcriterias.stdsql.EqualSearchCriteria;
import es.altia.util.persistance.exceptions.DuplicateInstanceException;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;
import es.altia.util.persistance.exceptions.StaleUpdateException;
import es.altia.util.persistance.exceptions.IntegrityViolationAttemptedException;
import es.altia.util.persistance.daocommands.*;
import es.altia.util.collections.CollectionsFactory;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoRelacionFirmaVO;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoRelacionPortafirmasVO;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoRelacionFirmaPK;

import java.util.Map;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.MessageFormat;

public class ConcreteSQLDocumentoRelacionFirmaDAO implements SQLDAO, CustomDocumentoRelacionFirmaDAO, SQLDAOPKCommandAdapter,
        SQLDAOCreateCommandAdapter, SQLDAORetrieveCommandAdapter, SQLDAOUpdateCommandAdapter {

	/*____________ Constants ______________*/
	private static final String MAIN_TABLE_NAME		= "G_CRD_FIR";
	private static final String[] VO_TABLES			= {"G_CRD_FIR"};
	private static final String[] VO_ATTRIBS_PK 		= { "CRD_MUN","CRD_PRO","CRD_EJE","CRD_NUM","CRD_TRA","CRD_OCU","CRD_NUD","USU_COD"};
	private static final String[] VO_ATTRIBS_ALL		= { "CRD_MUN","CRD_PRO","CRD_EJE","CRD_NUM","CRD_TRA","CRD_OCU","CRD_NUD","USU_COD", "FIR_EST", "FIR", "FX_FIRMA", "OBSERV", "USU_FIR"};
	private static final String[] VO_ATTRIBS_UPDATE		= { "FIR_EST", "FIR", "FX_FIRMA", "OBSERV", "USU_FIR" };
    private static final String CODIGO_USUARIO = "USU_COD";
    private static final String ESTADO_FIRMA = "FIR_EST";
    private static final String NRO_EXPEDIENTE = "CRD_NUM";
    private static final String FEC_ENVIO = "CRD_FMO";

    private static final String DOCUMENT_CRITERIA = "( (CRD_MUN={0}) AND (CRD_PRO={1}) AND (CRD_EJE={2}) AND (CRD_NUM={3}) AND (CRD_TRA={4}) AND (CRD_OCU={5}) AND (CRD_NUD={6}) )";


    private static final String[] VO_DOCUMENTOPORTAFIRMAS_TABLES
            = { "G_PORTAFIRMAS" };
    private static final String[] VO_DOCUMENTOPORTAFIRMAS_ATTRIBS_ALL
            = { "CRD_MUN","CRD_PRO","CRD_EJE","CRD_NUM","CRD_TRA","CRD_OCU","CRD_NUD","USU_COD", "FIR_EST", "FIR", "CRD_FMO", "FX_FIRMA", "OBSERV", "PML_VALOR", "TML_VALOR", "CRD_DES"};
    private static final String NOMBRE_DOCUMENTO_PORTAFIRMAS = "CRD_DES";


    private static final String SND_TABLE_NAME		= "G_CRD";
    private static final String[] SND_ATTRIBS_PK 		= { "CRD_MUN","CRD_PRO","CRD_EJE","CRD_NUM","CRD_TRA","CRD_OCU","CRD_NUD"};
    private static final String[] SND_ATTRIBS_UPDATE		= { "CRD_FIR_EST" };


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

    private SQLDAOSearchCommand documentoPortafirmasSearchCmd = null;
    private SQLDAOUpdateCommand updateDocumentoTramtacionCmd = null;


    /*____________ Operations ______________*/
	/* *********************************************************************** */
	/* **           Modify this methods for your concrete DAO               ** */
	/* *********************************************************************** */
	public ConcreteSQLDocumentoRelacionFirmaDAO() {
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

        documentoPortafirmasSearchCmd = cmdFactory.newSearchCmd(new DocumentoPortafirmasRetrieveAdapter() );

		retrieveCustomCmds.put(DocumentoRelacionFirmaVO.class.getName(), retrieveCmd);//{"DocumentoFirmaVO"->SQLDAORetrieveCmd}
		searchCustomCmds.put(DocumentoRelacionFirmaVO.class.getName(), searchCmd);//{"DocumentoFirmaVO"->SQLDAOSearchCmd}
        searchCustomCmds.put(DocumentoRelacionPortafirmasVO.class.getName(), documentoPortafirmasSearchCmd);//{"DocumentoPortafirmasVO"->SQLDAOSearchCmd}

        updateDocumentoTramtacionCmd = cmdFactory.newUpdateCmd(new DocumentoTramitacionUpdateAdapter());
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
		final DocumentoRelacionFirmaPK concretePK = (DocumentoRelacionFirmaPK) primaryKey;

		preparedStatement.setInt(result++, concretePK.getIdMunicipio());
		result = JdbcOperations.bindStringToStatement(concretePK.getIdProcedimiento(), 5, preparedStatement, result);
		preparedStatement.setInt(result++, concretePK.getIdEjercicio());
		result = JdbcOperations.bindStringToStatement(concretePK.getIdNumeroExpediente(), 30, preparedStatement, result);
		preparedStatement.setInt(result++, concretePK.getIdTramite());
		preparedStatement.setInt(result++, concretePK.getIdOcurrenciaTramite());
		preparedStatement.setInt(result++, concretePK.getIdNumeroDocumento());
		preparedStatement.setInt(result++, concretePK.getUsuarioFirmante());

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
		final DocumentoRelacionFirmaVO concreteVO = (DocumentoRelacionFirmaVO) vo;

		result = JdbcOperations.bindStringToStatement(concreteVO.getEstadoFirma(), 1, preparedStatement, result);
        result = JdbcOperations.bindBinaryStreamToStatement(concreteVO.getFirma(), preparedStatement, result);
		result = JdbcOperations.bindCalendarAsTimestampToStatement(concreteVO.getFechaFirma(), preparedStatement, result);
		result = JdbcOperations.bindStringToStatement(concreteVO.getObservaciones(), 160, preparedStatement, result);
        result = JdbcOperations.bindIntegerToStatement(concreteVO.getIdUsuarioDelegadoFirmante(),preparedStatement,result);

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
//		final DocumentoFirmaVO concreteVO = (DocumentoFirmaVO) givenVO;
//		final DocumentoFirmaPK concretePK = (DocumentoFirmaPK) concreteVO.getPrimaryKey();
//		final long _serial = ( (IfxStatement) preparedStatement).getSerial();
//		return new DocumentoFirmaVO(new DocumentoFirmaPK( _serial ),  concreteVO.getEstadoFirma(),  concreteVO.getFechaFirma(),  concreteVO.getObservaciones(), );
//		return new DocumentoFirmaVO((DocumentoFirmaPK) generatedKey,  concreteVO.getEstadoFirma(),  concreteVO.getFechaFirma(),  concreteVO.getObservaciones(), );

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
		int i = (Integer)resultMap.get(SQLDAORetrieveCommand.COUNTER);

		/* Get results. */
		final int theIdMunicipio = resultSet.getInt(i++);
		final String theIdProcedimiento = resultSet.getString(i++);
		final int theIdEjercicio = resultSet.getInt(i++);
		final String theIdNumeroExpediente = resultSet.getString(i++);
		final int theIdTramite = resultSet.getInt(i++);
		final int theIdOcurrenciaTramite = resultSet.getInt(i++);
		final int theIdNumeroDocumento = resultSet.getInt(i++);
		final int theUsuarioFirmante = resultSet.getInt(i++);
		final String theEstadoFirma = resultSet.getString(i++);
        final byte[] theFirma = resultSet.getBytes(i++);
		final Calendar theFechaFirma = JdbcOperations.extractCalendarAsTimestampFromResultSet(resultSet, i++);
		final String theObservaciones = resultSet.getString(i++);
        final Integer theUsuarioDelegadoFirmante = JdbcOperations.extractIntegerFromResultSet(resultSet, i++);

		/* Create VO. */
		final PersistentObject result = new DocumentoRelacionFirmaVO(new DocumentoRelacionFirmaPK( theIdMunicipio,  theIdProcedimiento,  theIdEjercicio,  theIdNumeroExpediente,  theIdTramite,  theIdOcurrenciaTramite,  theIdNumeroDocumento,  theUsuarioFirmante  ),  theEstadoFirma,  theFirma, theFechaFirma,  theObservaciones, theUsuarioDelegadoFirmante );

		/* Return the value object. */
		resultMap.put(SQLDAORetrieveCommand.COUNTER, i);
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
		final DocumentoRelacionFirmaVO concreteVO = (DocumentoRelacionFirmaVO) po;

		result = JdbcOperations.bindStringToStatement(concreteVO.getEstadoFirma(), 1, preparedStatement, result);
        result = JdbcOperations.bindBinaryStreamToStatement(concreteVO.getFirma(), preparedStatement, result);
		result = JdbcOperations.bindCalendarAsTimestampToStatement(concreteVO.getFechaFirma(), preparedStatement, result);
		result = JdbcOperations.bindStringToStatement(concreteVO.getObservaciones(), 160, preparedStatement, result);
        result = JdbcOperations.bindIntegerToStatement(concreteVO.getIdUsuarioDelegadoFirmante(),preparedStatement,result);

		return result;
	}//upBindUpdateFieldsToStatement


	public String getVersionNumberAttributeName() {
	    return null;  //No version number
	    //return SQLDAOCreateCommandAdapter.VERSION_NUMBER_DEFAULT_ATTRIBUTE_NAME;
	}//getVersionNumberAttributeName





    private class DocumentoPortafirmasRetrieveAdapter implements SQLDAORetrieveCommandAdapter {

        public String[] rtGetSQLTableNames() {
            return VO_DOCUMENTOPORTAFIRMAS_TABLES;
        }

        public String[] rtGetSQLSelectedAttributeNames() {
            return VO_DOCUMENTOPORTAFIRMAS_ATTRIBS_ALL;
        }

        /* NOT USED IN SEARCH */
        public String[] pkGetSQLPKAttributeNames() {
            return new String[0];
        }

        /* NOT USED IN SEARCH */
        public int pkBind(PrimaryKey primaryKey, PreparedStatement preparedStatement, int i) throws SQLException {
            return i;
        }

        public void rtExtractVOFromResultSet(ResultSet resultSet, Map resultMap) throws SQLException {
            int i = ((Integer)resultMap.get(SQLDAORetrieveCommand.COUNTER)).intValue();

            /* Get results. */
            final int theIdMunicipio = resultSet.getInt(i++);
            final String theIdProcedimiento = resultSet.getString(i++);
            final int theIdEjercicio = resultSet.getInt(i++);
            final String theIdNumeroExpediente = resultSet.getString(i++);
            final int theIdTramite = resultSet.getInt(i++);
            final int theIdOcurrenciaTramite = resultSet.getInt(i++);
            final int theIdNumeroDocumento = resultSet.getInt(i++);
            final int theUsuarioFirmante = resultSet.getInt(i++);
            final String theEstadoFirma = resultSet.getString(i++);
            final byte[] theFirma = resultSet.getBytes(i++);
            final Calendar theFechaEnvioFirma = JdbcOperations.extractCalendarAsTimestampFromResultSet(resultSet, i++);
            final Calendar theFechaFirma = JdbcOperations.extractCalendarAsTimestampFromResultSet(resultSet, i++);
            final String theObservaciones = resultSet.getString(i++);
            final String theNombreProcedimiento = resultSet.getString(i++);
            final String theNombreTramite = resultSet.getString(i++);
            final String theNombreDocumento = resultSet.getString(i++);

            /* Create VO. */
            final PersistentObject result = new DocumentoRelacionPortafirmasVO(
                    new DocumentoRelacionFirmaPK( theIdMunicipio,  theIdProcedimiento,
                            theIdEjercicio,  theIdNumeroExpediente,  theIdTramite,  theIdOcurrenciaTramite,
                            theIdNumeroDocumento,  theUsuarioFirmante  ),
                    theEstadoFirma,  theFirma, theFechaFirma,  theObservaciones,
                    theNombreProcedimiento, theNombreTramite, theNombreDocumento, theFechaEnvioFirma );

            /* Return the value object. */
            resultMap.put(SQLDAORetrieveCommand.COUNTER, i);
            resultMap.put(SQLDAORetrieveCommand.PERSISTENT_OBJECT,result);
        }

        public String getVersionNumberAttributeName() {
            return null;
        }
    }//inner class


    private class DocumentoTramitacionUpdateAdapter implements SQLDAOUpdateCommandAdapter {
        public String[] upGetSQLUpdateAttributeNames() {
            return SND_ATTRIBS_UPDATE;
        }
        public String getVersionNumberAttributeName() {
            return null;
        }
        public String pkGetSQLMainTableName() {
            return SND_TABLE_NAME;
        }
        public String[] pkGetSQLPKAttributeNames() {
            return SND_ATTRIBS_PK;
        }

        public int upBindUpdateFieldsToStatement(PersistentObject po, PreparedStatement preparedStatement, int i) throws SQLException {
            int result = i;
            final DocumentoRelacionFirmaVO concreteVO = (DocumentoRelacionFirmaVO) po;

            result = JdbcOperations.bindStringToStatement(concreteVO.getEstadoFirma(), 1, preparedStatement, result);

            return result;
        }

        public int pkBind(PrimaryKey primaryKey, PreparedStatement preparedStatement, int i) throws SQLException {
            int result = i;
            final DocumentoRelacionFirmaPK concretePK = (DocumentoRelacionFirmaPK) primaryKey;

            preparedStatement.setInt(result++, concretePK.getIdMunicipio());
            result = JdbcOperations.bindStringToStatement(concretePK.getIdProcedimiento(), 5, preparedStatement, result);
            preparedStatement.setInt(result++, concretePK.getIdEjercicio());
            result = JdbcOperations.bindStringToStatement(concretePK.getIdNumeroExpediente(), 30, preparedStatement, result);
            preparedStatement.setInt(result++, concretePK.getIdTramite());
            preparedStatement.setInt(result++, concretePK.getIdOcurrenciaTramite());
            preparedStatement.setInt(result++, concretePK.getIdNumeroDocumento());

            return result;
        }
    }//inner class


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
//		return createCmd.create(connection, newVO);

        /* *************** WORKARROUND ************************** */
        /* No sabemos por qué pero parece que da problemas
        al insertar algo no nulo en esta columna en algún esquema */
        DocumentoRelacionFirmaVO concreteVO = (DocumentoRelacionFirmaVO) newVO;
        Integer usuarioFirmante = concreteVO.getIdUsuarioDelegadoFirmante();
        if (usuarioFirmante==null)
            return createCmd.create(connection, newVO);
        else {
            concreteVO.setIdUsuarioDelegadoFirmante(null);
            concreteVO = (DocumentoRelacionFirmaVO) createCmd.create(connection, concreteVO);
            concreteVO.setIdUsuarioDelegadoFirmante(usuarioFirmante);
            try {
                updateCmd.update(connection, concreteVO);
            } catch (InstanceNotFoundException e) {
                throw new InternalErrorException(e);
            } catch (StaleUpdateException e) {
                throw new InternalErrorException(e);
            }
            return concreteVO;
        }//if
        /* ****************************************************** */
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
			throw new InternalErrorException(new Exception("ConcreteSQLDocumentoFirmaDAO: Invalid CustomVO class"));
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
			throw new InternalErrorException(new Exception("ConcreteSQLDocumentoFirmaDAO: Invalid CustomVO class"));
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

    public OrderCriteria orderByEstadoFirma(boolean asc) {
        OrderCriteria result = cmdFactory.newOrderCriteria(ESTADO_FIRMA,asc);
        return result;
    }//orderByEstadoFirma

    public OrderCriteria orderByNumeroExpediente(boolean asc) {
        OrderCriteria result = cmdFactory.newOrderCriteria(NRO_EXPEDIENTE,asc);
        return result;
    }//orderByNumeroExpediente
       public OrderCriteria orderByFechaEnvio(boolean asc) {
        OrderCriteria result = cmdFactory.newOrderCriteria(FEC_ENVIO,asc);
        return result;
    }//orderByFechaEnvio

    public OrderCriteria orderDocumentoPortafirmasByNombreDocumento(boolean asc) {
        OrderCriteria result = cmdFactory.newOrderCriteria(NOMBRE_DOCUMENTO_PORTAFIRMAS,asc);
        return result;
    }//orderDocumentoPortafirmasByNombreDocumento

    public SearchCriteria searchByDocument(DocumentoRelacionFirmaPK documentoPK) {
        final SQLFormatter fmt = cmdFactory.newSQLFormatter();
        final Object[] args = {fmt.formatValue(new Integer(documentoPK.getIdMunicipio())),
                             fmt.formatValue(documentoPK.getIdProcedimiento()),
                             fmt.formatValue(new Integer(documentoPK.getIdEjercicio())),
                             fmt.formatValue(documentoPK.getIdNumeroExpediente()),
                             fmt.formatValue(new Integer(documentoPK.getIdTramite())),
                             fmt.formatValue(new Integer(documentoPK.getIdOcurrenciaTramite())),
                             fmt.formatValue(new Integer(documentoPK.getIdNumeroDocumento()))
                            };
        return new SearchCriteria(MessageFormat.format(DOCUMENT_CRITERIA,args));
    }//searcByDocument

    public SearchCriteria searchDocumentoPortafirmasByUsuarios(Integer[] idsUsuarios) {
        InSearchCriteria result = cmdFactory.getSearchCriteriaFactory().newInSearchCriteria(CODIGO_USUARIO);
        result.setTo(idsUsuarios);
        return result;
    }//searcDocumentoPortafirmasByIdsUsuarios

    public SearchCriteria searchByEstadoFirma(String estadoFirma) {
        EqualSearchCriteria result = cmdFactory.getSearchCriteriaFactory().newEqualSearchCriteria(ESTADO_FIRMA);
        result.setTo(estadoFirma);
        return result;
    }//searchByEstadoFirma


    public void updateEstadoDocumentoTramitacion(Connection connection, PersistentObject newVO)
        throws InstanceNotFoundException, StaleUpdateException, InternalErrorException {
        updateDocumentoTramtacionCmd.update(connection, newVO);
    }//updateEstadoDocumentoTramitacion


}//class