package es.altia.agora.business.registro.mantenimiento.persistence.manual;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.DescripcionRegistroValueObject;
import es.altia.agora.business.registro.HistoricoMovimientoValueObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.mantenimiento.MantAsuntosValueObject;
import es.altia.agora.business.registro.persistence.AnotacionRegistroManager;
import es.altia.agora.business.registro.persistence.HistoricoMovimientoManager;
import es.altia.agora.business.registro.persistence.manual.AnotacionRegistroDAO;
import es.altia.agora.business.sge.persistence.manual.DefinicionProcedimientosDAO;
import es.altia.agora.business.sge.plugin.documentos.dao.RepositorioPluginAlmacenamientoDocumentosDAO;
import es.altia.agora.business.util.ElementoListaValueObject;
import es.altia.agora.business.util.HistoricoAnotacionHelper;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.agora.business.util.GlobalNames;
import es.altia.util.commons.DateOperations;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

public class MantAsuntosDAO {

    //Para el fichero de configuracion tecnico.
    protected static Config m_ConfigTechnical;
    //Para informacion de logs.
    protected static Log m_Log =
            LogFactory.getLog(MantRolesDAO.class.getName());
    //La instancia de esta clase
    private static MantAsuntosDAO instance = null;

    protected static String  asunto_cod;
    protected static String  asunto_unireg;
    protected static String  asunto_tiporeg;
    protected static String  asunto_desc;
    protected static String  asunto_extracto;
    protected static String  asunto_unitram;
    protected static String  asunto_proc;
    protected static String  asunto_procmun;
    protected static String  asunto_uorscorreo;
    protected static String  doc_asunto;
    protected static String  doc_unireg;
    protected static String  doc_tiporeg;
    protected static String  doc_titulo;

    protected MantAsuntosDAO() {
        super();
        //Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");

        asunto_cod        = m_ConfigTechnical.getString("SQL.R_TIPOASUNTO.codigo");
        asunto_unireg     = m_ConfigTechnical.getString("SQL.R_TIPOASUNTO.unidadReg");
        asunto_tiporeg    = m_ConfigTechnical.getString("SQL.R_TIPOASUNTO.tipoReg");
        asunto_desc       = m_ConfigTechnical.getString("SQL.R_TIPOASUNTO.descripcion");
        asunto_extracto   = m_ConfigTechnical.getString("SQL.R_TIPOASUNTO.extracto");
        asunto_unitram    = m_ConfigTechnical.getString("SQL.R_TIPOASUNTO.unidadTram");
        asunto_proc       = m_ConfigTechnical.getString("SQL.R_TIPOASUNTO.procedimiento");
        asunto_procmun    = m_ConfigTechnical.getString("SQL.R_TIPOASUNTO.procMun");
        asunto_uorscorreo = m_ConfigTechnical.getString("SQL.R_TIPOASUNTO.uorsCorreo");
        doc_asunto        = m_ConfigTechnical.getString("SQL.R_DOCSASUNTO.codigo");
        doc_unireg        = m_ConfigTechnical.getString("SQL.R_DOCSASUNTO.unidadReg");
        doc_tiporeg       = m_ConfigTechnical.getString("SQL.R_DOCSASUNTO.tipoReg");
        doc_titulo        = m_ConfigTechnical.getString("SQL.R_DOCSASUNTO.titulo");
    }

    public static MantAsuntosDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized(MantAsuntosDAO.class){
                if (instance == null)
                    instance = new MantAsuntosDAO();
            }
        }
        return instance;
    }

    /**
     * Carga los tipos de asunto asociados a la unidad org�nica cuyo c�digo se
     * pasa, si es -1 se cargan todos.
     *
     * @return Un vector de MantAsuntosValueObject que contendr�n la informaci�n
     * de la clave primaria (codigo, unidadRegistro, tipoEntrada) y descripci�n del asunto.
     */
    public Vector<MantAsuntosValueObject> cargarAsuntos(int codigoUnidadOrg, String[] params) {

        Vector<MantAsuntosValueObject> asuntos = new Vector<MantAsuntosValueObject>();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        if(m_Log.isDebugEnabled()) m_Log.debug("MantAsuntosDAO->cargarAsuntos");
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            // Creamos la select, con codigoUnidadorg == -1 se cargan todos
            String sql;

          /*
          sql = "SELECT  " + asunto_cod + ", " + asunto_unireg + ", " +
                             asunto_tiporeg + ", " + asunto_desc + ", UOR_NOM" +
               " FROM R_TIPOASUNTO" +
               " LEFT JOIN A_UOR ON (" + asunto_unireg + " = UOR_COD) " +
               " WHERE ELIMINADO=0 ";
           */
            sql = "SELECT  " + asunto_cod + ", " + asunto_unireg + ", " +
                    asunto_tiporeg + ", " + asunto_desc + ", CODIGO_CLASIFICACION,  UOR_NOM,ELIMINADO,FECHA_ELIMINACION,USUARIO_BAJA, PROCEDIMIENTO, PRO_DES  " +
                    " FROM R_TIPOASUNTO" +
                    " LEFT JOIN E_PRO ON (PRO_COD = PROCEDIMIENTO)" +
                    " LEFT JOIN A_UOR ON (" + asunto_unireg + " = UOR_COD) ";
            //" WHERE ELIMINADO=0 ";

            if (codigoUnidadOrg != -1) {
                // Recuperamos para la Unidad de Registro actual y las que son para todos los registros (-1)
                sql += "WHERE ";
                sql += " (" + asunto_unireg + " = " + codigoUnidadOrg +
                        " OR " + asunto_unireg + " = -1)";
            }
            sql += " ORDER BY UOR_NOM, " + asunto_cod;


            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                MantAsuntosValueObject mantAsuntosVO = new MantAsuntosValueObject();
                mantAsuntosVO.setCodigo(rs.getString(asunto_cod));
                mantAsuntosVO.setUnidadRegistro(rs.getString(asunto_unireg));
                mantAsuntosVO.setTipoRegistro(rs.getString(asunto_tiporeg));
                mantAsuntosVO.setDescripcion(rs.getString(asunto_desc));
                mantAsuntosVO.setCodigoClasificacion(rs.getInt("CODIGO_CLASIFICACION"));
                mantAsuntosVO.setDescUor(rs.getString("UOR_NOM"));
                mantAsuntosVO.setProcedimiento(rs.getString("PROCEDIMIENTO"));
                mantAsuntosVO.setDesProcedimiento(rs.getString("PRO_DES"));
                int eliminado = rs.getInt("ELIMINADO");
                if(eliminado==1) mantAsuntosVO.setAsuntoBaja("BAJA");
                else mantAsuntosVO.setAsuntoBaja("ALTA");
                asuntos.addElement(mantAsuntosVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally {
            try{
                abd.devolverConexion(conexion);
            }catch (Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("MantAsuntosDAO->cargarAsuntos. END. Los asuntos recuperados son: "+asuntos.toString());
        return asuntos;
    }

    /**
     * Carga los asuntos de la unidad y tipo de registro del RegistroValueObject pasado
     * y tambien los que son para todas las unidades y/o para ambos registros.
     * @param regVO: Contiene informaci�n necesaria para recuperar loa asuntos
     * @param recuperarTodosAsuntos: Si est� a true inidica que se recuperan todos los asuntos de registro, incluso los que han sido
     * dados de baja. Si est� a false, se recuperan s�lo los que no han sido eliminados.
     * @param params: Par�metros de conexi�n a la BBDD
     * @return Un vector de MantAsuntosValueObject que contendr�n la informaci�n
     * de la clave primaria (codigo, unidadRegistro, tipoEntrada) y descripci�n del asunto.
     */
    public Vector<MantAsuntosValueObject> buscarAsuntos(RegistroValueObject regVO, boolean recuperarTodosAsuntos, String[] params) {

        Vector<MantAsuntosValueObject> asuntos = new Vector<MantAsuntosValueObject>();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        if(m_Log.isDebugEnabled()) m_Log.debug("MantAsuntosDAO->buscarAsuntos");
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            String sql;
            String codigoUnidadOrg = Integer.toString(regVO.getUnidadOrgan());
            String tipoRegistro = regVO.getTipoReg();

            /** ORIGINAL
             sql = "SELECT  " + asunto_cod + ", " + asunto_unireg + ", " +
             asunto_tiporeg + ", " + asunto_desc +
             " FROM R_TIPOASUNTO" +
             " WHERE (" + asunto_unireg + " = " + codigoUnidadOrg +
             " OR " + asunto_unireg + "= -1)" + // Para todos los registros
             " AND (" + asunto_tiporeg + " = '" + tipoRegistro + "'" +
             " OR " + asunto_tiporeg + " = 'A') ORDER BY " + asunto_desc + " ASC ";  // Para ambos tipos
             */
            sql = "SELECT  " + asunto_cod + ", " + asunto_unireg + ", " +
                    asunto_tiporeg + ", " + asunto_desc + ", ELIMINADO " +
                    " FROM R_TIPOASUNTO" +
                    " WHERE (" + asunto_unireg + " = " + codigoUnidadOrg +
                    " OR " + asunto_unireg + "= -1)" + // Para todos los registros
                    " AND (" + asunto_tiporeg + " = '" + tipoRegistro + "'" +
                    " OR " + asunto_tiporeg + " = 'A')";

            if(!recuperarTodosAsuntos){
                // S�lo se recupera los asuntos que no han sido eliminados
                sql = sql + " AND ELIMINADO=0 ";
            }

            String orderBy = " ORDER BY " + asunto_desc + " ASC ";  // Para ambos tipos

            sql=sql+orderBy;

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                MantAsuntosValueObject mantAsuntosVO = new MantAsuntosValueObject();
                mantAsuntosVO.setCodigo(rs.getString(asunto_cod));
                mantAsuntosVO.setUnidadRegistro(rs.getString(asunto_unireg));
                mantAsuntosVO.setTipoRegistro(rs.getString(asunto_tiporeg));
                mantAsuntosVO.setDescripcion(rs.getString(asunto_desc));
                mantAsuntosVO.setAsuntoBaja("N");
                int eliminado = rs.getInt("ELIMINADO");
                if(eliminado==1) mantAsuntosVO.setAsuntoBaja("S");
                asuntos.addElement(mantAsuntosVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally {
            try{
                abd.devolverConexion(conexion);
            }catch (Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
        }
        return asuntos;
    }

    /**
     * Carga todos los campos y listas de documentos del asunto que se pasa como
     * argumento (usando los atributos codigo y unidadRegistro como clave
     * primaria del VO).
     *
     * @return Un MantAsuntosValueObject que contendr� toda la informaci�n del asunto.
     */
    public MantAsuntosValueObject cargarAsunto(MantAsuntosValueObject asunto, String[] params) {

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        if(m_Log.isDebugEnabled()) m_Log.debug("MantAsuntosDAO->cargarAsunto");
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();

            // Creamos la select para el asunto
            String sql;
            sql = "SELECT DESCRIPCION, EXTRACTO, UNIDADTRAM, PROCEDIMIENTO, PROCMUN, " +
                    "NOTIFICAR, UORSCORREO, TIPOREGISTRO, CODIGO_CLASIFICACION,DOCINT_OBLIGATORIO, BLOQUEAR_DESTINO, " +
                    "BLOQUEAR_PROCEDIMIENTO, BLOQUEO_PAC " +
                    "FROM R_TIPOASUNTO " +
                    "WHERE CODIGO = ? " +
                    "AND UNIDADREGISTRO = " + asunto.getUnidadRegistro();
            // No se permite repeticion de codigos en cada unidad de registro
            // por tanto no es necesario el tipo.
            // + " " + "AND TIPOREGISTRO = '" + asunto.getTipoRegistro() + "'";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            PreparedStatement ps = conexion.prepareStatement(sql);
            int j=1;
            ps.setString(j++, asunto.getCodigo());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                asunto.setDescripcion(rs.getString("DESCRIPCION"));
                asunto.setExtracto(rs.getString("EXTRACTO"));
                asunto.setUnidadTram(rs.getString("UNIDADTRAM"));
                asunto.setProcedimiento(rs.getString("PROCEDIMIENTO"));
                asunto.setMunProc(rs.getString("PROCMUN"));
                asunto.setEnviarCorreo(rs.getBoolean("NOTIFICAR"));
                String uorsCorreo = rs.getString("UORSCORREO");
                if (uorsCorreo == null || uorsCorreo.equals("")) asunto.setTxtListaUorsCorreo("");
                else asunto.setTxtListaUorsCorreo(uorsCorreo);
                asunto.setTipoRegistro(rs.getString("TIPOREGISTRO"));
                asunto.setCodigoClasificacion(rs.getInt("CODIGO_CLASIFICACION"));
                // #234108
                asunto.setTipoDocObligatorio(rs.getBoolean("DOCINT_OBLIGATORIO"));
                asunto.setBloquearDestino(rs.getBoolean("BLOQUEAR_DESTINO"));
                asunto.setBloquearProcedimiento(rs.getBoolean("BLOQUEAR_PROCEDIMIENTO"));
                asunto.setBloqueoPAC(rs.getBoolean("BLOQUEO_PAC"));
            } else {
                return new MantAsuntosValueObject();
            }
            rs.close();
            ps.close();

            //Si se trata de un asunto que bloquea el procedimiento se devolvera tambien la descripcion y la condicion de digitalizacion de este
            if(asunto.getProcedimiento() != null && !asunto.getProcedimiento().equals("") && asunto.isBloquearProcedimiento()){
                asunto.setDesProcedimiento(DefinicionProcedimientosDAO.getInstance().getDescripcionMultiIdiProcedimiento(asunto.getProcedimiento(), conexion));
                asunto.setDigitProcedimiento(new RepositorioPluginAlmacenamientoDocumentosDAO().getDigitalizacionProcedimiento(asunto.getProcedimiento(), conexion));
            }

            // Para los documentos
            sql = "SELECT " + doc_titulo +
                    " FROM R_DOCSASUNTO" +
                    " WHERE " + doc_asunto + " = ? " +
                    " AND " + doc_unireg + " = " + asunto.getUnidadRegistro() +
                    " AND " + doc_tiporeg + " = '" + asunto.getTipoRegistro() + "'";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            ps = conexion.prepareStatement(sql);
            j=1;
            ps.setString(j++, asunto.getCodigo());
            rs = ps.executeQuery();
            Vector<ElementoListaValueObject> docs = new Vector<ElementoListaValueObject>();
            while (rs.next()) {
                ElementoListaValueObject doc = new ElementoListaValueObject();
                doc.setDescripcion(rs.getString(doc_titulo));
                docs.add(doc);
            }
            asunto.setListaDocs(docs);
            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            try{
                abd.devolverConexion(conexion);
            }catch (Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
        }
        return asunto;
    }

    /**
     * Graba un nuevo asunto en la BD.
     */
    public void grabarAlta(MantAsuntosValueObject asunto, String[] params) {
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        if(m_Log.isDebugEnabled()) m_Log.debug("MantAsuntosDAO->grabarAlta. codigoClasificacion: "+asunto.getCodigoClasificacion());
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);

            //Tenemos que hacer la siguiente comprobacion del codigoClasificacion
            if(asunto.getCodigoClasificacion()==null){
                asunto.setCodigoClasificacion(-1);
            }

            // Creamos el insert para el asunto.
            String sql;
            sql = "INSERT INTO R_TIPOASUNTO (CODIGO,UNIDADREGISTRO,TIPOREGISTRO, " +
                    "DESCRIPCION,CODIGO_CLASIFICACION, EXTRACTO, UNIDADTRAM, PROCEDIMIENTO, PROCMUN, NOTIFICAR, UORSCORREO," +
                    " DOCINT_OBLIGATORIO, BLOQUEAR_DESTINO, BLOQUEAR_PROCEDIMIENTO, BLOQUEO_PAC) " +
                    "VALUES (?, " + asunto.getUnidadRegistro() + ", '" + asunto.getTipoRegistro() + "', " +
                    "?, "+ asunto.getCodigoClasificacion()+", ?, " + asunto.getUnidadTram() + ", ?, " + asunto.getMunProc() + ", ?, ?, ?, ?, ?, ?)";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            PreparedStatement ps = conexion.prepareStatement(sql);
            int j=1;
            ps.setString(j++, asunto.getCodigo());
            ps.setString(j++, asunto.getDescripcion());
            ps.setString(j++, asunto.getExtracto());
            ps.setString(j++, asunto.getProcedimiento());
            ps.setBoolean(j++, asunto.isEnviarCorreo());
            // Lista de uors a notificar
            if (asunto.getTxtListaUorsCorreo() != null && !"".equals(asunto.getTxtListaUorsCorreo()))
                ps.setString(j++, asunto.getTxtListaUorsCorreo());
            else
                ps.setString(j++, "");
            // #234108
            ps.setBoolean(j++, asunto.isTipoDocObligatorio());
            ps.setBoolean(j++, asunto.isBloquearDestino());
            ps.setBoolean(j++, asunto.isBloquearProcedimiento());

            ps.setBoolean(j++, asunto.isBloqueoPAC());

            ps.executeUpdate();
            ps.close();

            // Insertamos los documentos.
            if (asunto.getProcedimiento()==null || asunto.getProcedimiento().equals(""))
                grabarDocumentos(asunto, conexion);

            abd.finTransaccion(conexion);

        }  catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            try{
                abd.rollBack(conexion);
            }catch (Exception x){
                x.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(x.getMessage());
            }
        } finally {
            try{
                abd.devolverConexion(conexion);
            }catch (Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
        }
    }

    /**
     * Modifica un asunto en la BD.
     */
    public void grabarModificacion(MantAsuntosValueObject asunto, MantAsuntosValueObject anterior, String[] params) {
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        if(m_Log.isDebugEnabled()) m_Log.debug("MantAsuntosDAO->grabarModificacion");
        if(m_Log.isDebugEnabled()) m_Log.debug("MantAsuntosDAO->grabarModificacion.Codigo Clasificacion"+asunto.getCodigoClasificacion());
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);

            //Tenemos que hacer la siguiente comprobacion del codigoClasificacion
            if(asunto.getCodigoClasificacion()==null){
                asunto.setCodigoClasificacion(-1);
            }

            // En primer lugar se borran los documentos referenciados por
            // la anterior clave primaria.
            borrarDocumentos(anterior, conexion);

            // Creamos el update para el asunto. Debemos tener cuidado de usar
            // en el WHERE los valores de la antigua clave primaria.
            String sql;
            sql = "UPDATE R_TIPOASUNTO SET " +
                    asunto_cod + "=?, " +
                    asunto_unireg + "=" + asunto.getUnidadRegistro() + ", " +
                    asunto_tiporeg + "='" + asunto.getTipoRegistro() + "', " +
                    asunto_desc + "=?, " +
                    asunto_extracto + "=?, " +
                    asunto_unitram + "=" + asunto.getUnidadTram() + ", " +
                    asunto_proc + "=?, " +
                    asunto_procmun + "=" + asunto.getMunProc() + ", " +
                    asunto_uorscorreo + "=?" + "," +
                    " NOTIFICAR = " + (asunto.isEnviarCorreo() ? "1" : "0") + "," +
                    " CODIGO_CLASIFICACION= " + asunto.getCodigoClasificacion() + "," +
                    " DOCINT_OBLIGATORIO=? , "+
                    " BLOQUEAR_DESTINO=? ,"+
                    " BLOQUEAR_PROCEDIMIENTO=? ,"+
                    " BLOQUEO_PAC=? " +
                    " WHERE " + asunto_cod + " = ? " +
                    " AND " + asunto_unireg + " = " + anterior.getUnidadRegistro() +
                    " AND " + asunto_tiporeg + " = '" + anterior.getTipoRegistro() + "'";


            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            PreparedStatement ps = conexion.prepareStatement(sql);
            int j=1;
            ps.setString(j++, asunto.getCodigo());
            ps.setString(j++, asunto.getDescripcion());
            ps.setString(j++, asunto.getExtracto());
            ps.setString(j++, asunto.getProcedimiento());
            // Lista de uors a notificar
            if (asunto.isEnviarCorreo())
                ps.setString(j++, asunto.getTxtListaUorsCorreo());
            else
                ps.setString(j++, "");
            // #234108
            ps.setBoolean(j++, asunto.isTipoDocObligatorio());
            ps.setBoolean(j++, asunto.isBloquearDestino());
            ps.setBoolean(j++, asunto.isBloquearProcedimiento());
            ps.setBoolean(j++, asunto.isBloqueoPAC());
            ps.setString(j++, anterior.getCodigo());

            ps.executeUpdate();
            ps.close();

            // Por ultimo se crean los nuevos documentos y cerramos transaccion
            if (asunto.getProcedimiento()==null || asunto.getProcedimiento().equals(""))
                grabarDocumentos(asunto, conexion);
            abd.finTransaccion(conexion);

        }  catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            try{
                abd.rollBack(conexion);
            }catch (Exception x){
                x.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(x.getMessage());
            }
        } finally {
            try{
                abd.devolverConexion(conexion);
            }catch (Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
        }
    }

    /**
     * Borra los documentos del asunto en la BD.
     */
    private void borrarDocumentos(MantAsuntosValueObject asunto, Connection conexion)
            throws SQLException {

        if(m_Log.isDebugEnabled()) m_Log.debug("MantAsuntosDAO->borrarDocumentos");

        // Borramos documentos existentes.
        String sql;
        sql = "DELETE FROM R_DOCSASUNTO" +
                " WHERE " + doc_asunto + " = ? " +
                " AND " + doc_unireg + " = " + asunto.getUnidadRegistro() +
                " AND " + doc_tiporeg + " = '" + asunto.getTipoRegistro() + "'";

        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        PreparedStatement ps = conexion.prepareStatement(sql);
        int j=1;
        ps.setString(j++, asunto.getCodigo());
        ps.executeUpdate();
        ps.close();
    }

    /**
     * Inserta los documentos del asunto en la BD.
     */
    private void grabarDocumentos(MantAsuntosValueObject asunto, Connection conexion)
            throws SQLException {

        if(m_Log.isDebugEnabled()) m_Log.debug("MantAsuntosDAO->grabarDocumentos");

        // Insertamos documentos
        String sql;
        PreparedStatement ps = null;
        int j = 1;
        Vector<ElementoListaValueObject> docs = asunto.getListaDocs();
        for (ElementoListaValueObject doc : docs) {
            sql = "INSERT INTO R_DOCSASUNTO (" +
                    doc_titulo + "," + doc_asunto + "," + doc_unireg + "," + doc_tiporeg + ") " +
                    "VALUES (?, ?, " + asunto.getUnidadRegistro() + ", '" + asunto.getTipoRegistro() + "')";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            j=1;
            ps.setString(j++, doc.getDescripcion());
            ps.setString(j++, asunto.getCodigo());
            ps.executeUpdate();
            ps.close();
        }
    }


    // Obtiene toda la informacion de los registros cuyo asunto se desea eliminar
    private Vector<RegistroValueObject> obtenerInfoRegistrosPorAsunto(MantAsuntosValueObject asunto, String[] params) {
        Vector<RegistroValueObject> registrosCompletosAsunto = new Vector<RegistroValueObject>();

        try {
            // Obtenemos los registros cuyo asunto sea el que se desea eliminar
            Vector<RegistroValueObject> registrosAsunto = AnotacionRegistroManager.getInstance().getListaRegistrosPorAsunto(asunto, params);

            // Obtenemos toda la informacion de todos los registros
            for (Iterator<RegistroValueObject> it = registrosAsunto.iterator(); it.hasNext();) {
                RegistroValueObject regAsunto = it.next();
                RegistroValueObject registroVOAntiguo = new RegistroValueObject();
                registroVOAntiguo = AnotacionRegistroManager.getInstance().getByPrimaryKey(regAsunto, params);
                registrosCompletosAsunto.addElement(registroVOAntiguo);
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error(e.getMessage());
            }
        }
        return registrosCompletosAsunto;
    }


    /**
     * Se recupera una colecci�n con los asuntos codificados sobre los que el usuario tiene permiso sobre la unidad de registro
     * a los que dicho asunto pertenece
     * @param usuario: Objeto del tipo UsuarioValueObject
     * @param tipoEntrada: Tipo de entrada (E = Entrada, S = Salida)
     * @param con: Conexi�n a la BBDD
     * @return Colecci�n de objetos MantAsuntosValueObject con la informaci�n del asunto
     */
    public Vector<MantAsuntosValueObject> getAsuntosCodificadosPermisoUsuario(UsuarioValueObject usuario,String tipoEntrada,Connection con) {
        Vector<MantAsuntosValueObject> asuntos = new Vector<MantAsuntosValueObject>();
        ResultSet rs = null;
        Statement st = null;

        try {

            // Se recuperan los asuntos que tiene como unidad de registro cualquiera o aquellos que tienen como unidad de registro alguna sobre la
            // que tiene permiso el usuario
            String sql = "SELECT CODIGO,DESCRIPCION,TIPOREGISTRO,UNIDADREGISTRO FROM R_TIPOASUNTO " +
                    "WHERE (TIPOREGISTRO='" + tipoEntrada + "' OR TIPOREGISTRO='A') AND " +
                    "(UNIDADREGISTRO=-1 OR UNIDADREGISTRO IN " +
                    "(SELECT UOU_UOR FROM " +
                    GlobalNames.ESQUEMA_GENERICO +  "A_UOU  " +
                    "WHERE UOU_USU =" + usuario.getIdUsuario()  + " AND UOU_ORG =" + usuario.getOrgCod() + " AND UOU_ENT =" + usuario.getEntCod() + ")) ";

            m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                MantAsuntosValueObject asunto = new MantAsuntosValueObject();
                asunto.setCodigo(rs.getString("CODIGO"));
                asunto.setDescripcion(rs.getString("DESCRIPCION"));
                asunto.setUnidadRegistro(rs.getString("UNIDADREGISTRO"));
                asunto.setTipoRegistro(rs.getString("TIPOREGISTRO"));
                asuntos.add(asunto);
            }

        }  catch (SQLException e) {
            m_Log.error("Error durante el acceso a la BBDD: " + e.getMessage());
        } finally {
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch (Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
        }

        return asuntos;
    }




    /**
     * Marca un asunto codificado de registro como de baja en lugar de eliminarlo f�sicamente de la base de datos
     * @param asunto: Objeto de tipo MantAsuntosValueObject con los datos del asunto
     * @param idUsuario: Id del usuario
     * @param params: Par�metros de conexi�n a la BBDD
     */
    public void cambiarEstadoAsunto(MantAsuntosValueObject asunto, int idUsuario, int estado, String[] params) {
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        if(m_Log.isDebugEnabled()) m_Log.debug("MantAsuntosDAO->eliminarAsunto");

        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);

            // ORIGINAL
            // Borramos documentos existentes primero
            //borrarDocumentos(asunto, conexion);

            // Borramos el asunto
            String sql;
            PreparedStatement ps = null;
            int j=1;
            sql = "UPDATE R_TIPOASUNTO SET ELIMINADO=?, FECHA_ELIMINACION=?, USUARIO_BAJA=? " +
                    " WHERE " + asunto_cod + " = ? " +
                    " AND " + asunto_unireg + " = " + asunto.getUnidadRegistro() +
                    " AND " + asunto_tiporeg + " = '" + asunto.getTipoRegistro() + "'";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            j=1;

            ps.setInt(j++,estado);
            ps.setTimestamp(j++, DateOperations.toTimestamp(Calendar.getInstance()));
            ps.setInt(j++, idUsuario);
            ps.setString(j++, asunto.getCodigo());
            ps.executeUpdate();
            ps.close();

            abd.finTransaccion(conexion);

        }  catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            try{
                abd.rollBack(conexion);
            }catch (Exception x){
                x.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(x.getMessage());
            }
        } finally {
            try{
                abd.devolverConexion(conexion);
            }catch (Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
        }
    }



    /**
     * Comprueba si un determinado asunto codificado est� dado de baja. Si est� dado de baja devuelve la fecha
     * de baja del mismo en formato dd/MM/yyyy
     * @param codAsunto: C�digo del asunto
     * @param unidadRegistro: Unidad de registro
     * @param tipoRegistro: Tipo de registro
     * @param con: Conexi�n a la base de datos
     * @return String con la fecha. Sino existe el asunto devuelve un null.
     */
    public String estaDeBajaAsuntoCodificado(String codAsunto,String unidadRegistro,String tipoRegistro,Connection con){
        String fechaEliminacion  = null;
        Statement st = null;
        ResultSet rs = null;

        try{
            String sql = "SELECT FECHA_ELIMINACION FROM R_TIPOASUNTO " +
                    "WHERE CODIGO='" + codAsunto + "' AND (UNIDADREGISTRO=-1 OR UNIDADREGISTRO=" + unidadRegistro + ") " +
                    "AND (TIPOREGISTRO='A' OR TIPOREGISTRO='" + tipoRegistro + "') AND ELIMINADO=1";
            m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){
                java.sql.Timestamp tFechaElim = rs.getTimestamp("FECHA_ELIMINACION");
                if(tFechaElim!=null){
                    Calendar cFechaElim = DateOperations.toCalendar(tFechaElim);
                    SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
                    fechaEliminacion    = sf.format(cFechaElim.getTime());
                }
            }// while

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                m_Log.error("Error en la cla�sula finally al cerrar los recursos asociados a la conexi�n de BD: " + e.getMessage());
            }
        }
        return fechaEliminacion;
    }


    /**
     * Carga los asuntos de la unidad y tipo de registro del RegistroValueObject pasado
     * y tambien los que son para todas las unidades y/o para ambos registros.
     * @param regVO: Contiene informaci�n necesaria para recuperar loa asuntos
     * @param recuperarTodosAsuntos: Si est� a true inidica que se recuperan todos los asuntos de registro, incluso los que han sido
     * dados de baja. Si est� a false, se recuperan s�lo los que no han sido eliminados.
     * @param params: Par�metros de conexi�n a la BBDD
     * @return Un vector de MantAsuntosValueObject que contendr�n la informaci�n
     * de la clave primaria (codigo, unidadRegistro, tipoEntrada) y descripci�n del asunto.
     */
    public Vector<MantAsuntosValueObject> buscarAsuntosClasificacion(int codigoClasificacion, RegistroValueObject regVO, boolean recuperarTodosAsuntos, String[] params) {

        Vector<MantAsuntosValueObject> asuntos = new Vector<MantAsuntosValueObject>();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        if(m_Log.isDebugEnabled()) m_Log.debug("MantAsuntosDAO->buscarAsuntosClasificacion. El codigo clasificacion es:"+codigoClasificacion);
        if(m_Log.isDebugEnabled()) m_Log.debug("MantAsuntosDAO->buscarAsuntosClasificacion. El recuperarTodosAsuntos :"+ recuperarTodosAsuntos);
        if(m_Log.isDebugEnabled()) m_Log.debug("MantAsuntosDAO->buscarAsuntosClasificacion. El registroValueObject es :"+ regVO);
        if(m_Log.isDebugEnabled()) m_Log.debug("MantAsuntosDAO->buscarAsuntosClasificacion. El codigo de unidad organica es: "+regVO.getUnidadOrgan());
        if(m_Log.isDebugEnabled()) m_Log.debug("MantAsuntosDAO->buscarAsuntosClasificacion. El tipo registro  es: "+regVO.getTipoReg());
        String tipoRegistro = regVO.getTipoReg();
        String codigoUnidadOrg = Integer.toString(regVO.getUnidadOrgan());
        try {

            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            String sql;


            sql = "SELECT  " + asunto_cod + ", " + asunto_unireg + ", " +
                    asunto_tiporeg + ", " + asunto_desc + ", ELIMINADO " +
                    " FROM R_TIPOASUNTO" +
                    " WHERE (" + asunto_unireg + " = " + codigoUnidadOrg +
                    " OR " + asunto_unireg + "= -1)" + // Para todos los registros
                    " AND (" + asunto_tiporeg + " = '" + tipoRegistro + "'" +
                    " OR " + asunto_tiporeg + " = 'A')" +
                    " AND CODIGO_CLASIFICACION = " +codigoClasificacion;

            if(!recuperarTodosAsuntos){
                // S�lo se recupera los asuntos que no han sido eliminados
                sql = sql + " AND ELIMINADO=0 ";
            }

            String orderBy = " ORDER BY " + asunto_desc + " ASC ";  // Para ambos tipos

            sql=sql+orderBy;

            if(m_Log.isDebugEnabled()) m_Log.debug("buscarAsuntosClasificacion:"+sql);

            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                MantAsuntosValueObject mantAsuntosVO = new MantAsuntosValueObject();
                mantAsuntosVO.setCodigo(rs.getString(asunto_cod));
                mantAsuntosVO.setUnidadRegistro(rs.getString(asunto_unireg));
                mantAsuntosVO.setTipoRegistro(rs.getString(asunto_tiporeg));
                mantAsuntosVO.setDescripcion(rs.getString(asunto_desc));
                mantAsuntosVO.setAsuntoBaja("N");
                int eliminado = rs.getInt("ELIMINADO");
                if(eliminado==1) mantAsuntosVO.setAsuntoBaja("S");
                asuntos.addElement(mantAsuntosVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally {
            try{
                abd.devolverConexion(conexion);
            }catch (Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
        }
        return asuntos;
    }




}