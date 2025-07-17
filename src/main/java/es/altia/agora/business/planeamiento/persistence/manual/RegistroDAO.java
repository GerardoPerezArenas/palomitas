package es.altia.agora.business.planeamiento.persistence.manual;

import java.util.Vector;
import java.util.Calendar;
import java.sql.*;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.BDException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.agora.business.planeamiento.RegistroValueObject;

public class RegistroDAO {

    //Para el fichero de configuracion tecnico.
    private static Config m_ConfigTechnical;
    //Para informacion de logs.
    private static Log m_Log = LogFactory.getLog(RegistroDAO.class.getName());


    private static RegistroDAO instance = null;
    //Nombre de Tabla y nombres de campos
    private static String nombreTabla = null;
    private static String tipoRegistro = null;
    private static String codigoSubseccion = null;
    private static String codigoTipo = null;
    private static String anho = null;
    private static String numero = null;
    private static String numeroRegistro = null;
    private static String fechaAlta = null;
    private static String fechaAprobacion = null;
    private static String fechaVigencia = null;
    private static String fechaBaja = null;
    private static String codigoProcedimiento = null;
    private static String numeroProcedimiento = null;
    private static String codigoAmbito = null;
    private static String parcela = null;
    private static String promotor = null;
    private static String codigoOrganoAprobacion = null;
    private static String objetoConvenio = null;
    private static String denominacionBien = null;
    private static String codigoDomicilio = null;
    private static String codigoCatalogacion = null;
    private static String codigoGradoProteccion = null;
    private static String codigoRelacionBien = null;
    private static String fechaPublicacion = null;
    private static String numeroPublicacion = null;
    private static String observaciones = null;
    private static String archivo = null;

    protected RegistroDAO() {
        //Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados

        nombreTabla = m_ConfigTechnical.getString("SQL.U_REG.Tabla");
        tipoRegistro = m_ConfigTechnical.getString("SQL.U_REG.TipoRegistro");
        codigoSubseccion = m_ConfigTechnical.getString("SQL.U_REG.CodigoSubseccion");
        codigoTipo = m_ConfigTechnical.getString("SQL.U_REG.CodigoTipo");
        anho = m_ConfigTechnical.getString("SQL.U_REG.Anho");
        numero = m_ConfigTechnical.getString("SQL.U_REG.Numero");
        numeroRegistro = m_ConfigTechnical.getString("SQL.U_REG.NumeroRegistro");
        fechaAlta = m_ConfigTechnical.getString("SQL.U_REG.FechaAlta");
        fechaAprobacion = m_ConfigTechnical.getString("SQL.U_REG.FechaAprobacion");
        fechaVigencia = m_ConfigTechnical.getString("SQL.U_REG.FechaVigencia");
        fechaBaja = m_ConfigTechnical.getString("SQL.U_REG.FechaBaja");
        codigoProcedimiento = m_ConfigTechnical.getString("SQL.U_REG.CodigoProcedimiento");
        numeroProcedimiento = m_ConfigTechnical.getString("SQL.U_REG.NumeroProcedimiento");
        codigoAmbito = m_ConfigTechnical.getString("SQL.U_REG.CodigoAmbito");
        parcela = m_ConfigTechnical.getString("SQL.U_REG.Parcela");
        promotor = m_ConfigTechnical.getString("SQL.U_REG.Promotor");
        codigoOrganoAprobacion = m_ConfigTechnical.getString("SQL.U_REG.CodigoOrganoAprobacion");
        objetoConvenio = m_ConfigTechnical.getString("SQL.U_REG.ObjetoConvenio");
        denominacionBien = m_ConfigTechnical.getString("SQL.U_REG.DenominacionBien");
        codigoDomicilio = m_ConfigTechnical.getString("SQL.U_REG.CodigoDomicilio");
        codigoCatalogacion = m_ConfigTechnical.getString("SQL.U_REG.CodigoCatalogacion");
        codigoGradoProteccion = m_ConfigTechnical.getString("SQL.U_REG.CodigoGradoProteccion");
        codigoRelacionBien = m_ConfigTechnical.getString("SQL.U_REG.CodigoRelacionBien");
        fechaPublicacion = m_ConfigTechnical.getString("SQL.U_REG.FechaPublicacion");
        numeroPublicacion = m_ConfigTechnical.getString("SQL.U_REG.NumeroPublicacion");
        observaciones = m_ConfigTechnical.getString("SQL.U_REG.Observaciones");
        archivo = m_ConfigTechnical.getString("SQL.U_REG.Archivo");
    }

    public static RegistroDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized(RegistroDAO.class){
                if (instance == null)
                    instance = new RegistroDAO();
            }
        }
        return instance;
    }

    public void create(RegistroValueObject registroVO, Connection conexion)
            throws BDException {

        ResultSet rs = null;
        String sql = "";

        try{

            m_Log.info("RegistroDAO.create");

            sql = "INSERT INTO " + nombreTabla + "(" + tipoRegistro + "," + codigoSubseccion + "," + codigoTipo + "," +
                    anho + "," + numero + "," + numeroRegistro + "," + fechaAlta + "," + fechaAprobacion + "," +
                    fechaVigencia + "," + fechaBaja + "," + codigoProcedimiento + "," + numeroProcedimiento + "," +
                    codigoAmbito + "," + parcela + "," + promotor + "," + codigoOrganoAprobacion + "," + objetoConvenio +
                    "," + denominacionBien + "," + codigoDomicilio + "," + codigoCatalogacion + "," +
                    codigoGradoProteccion + "," + codigoRelacionBien + "," + fechaPublicacion + "," +
                    numeroPublicacion + "," + observaciones + "," + archivo +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, registroVO.getTipoRegistro().toString());
            ps.setString(i++, registroVO.getCodigoSubseccion());
            ps.setString(i++, registroVO.getCodigoTipo());
            ps.setString(i++, registroVO.getAnho());
            ps.setInt(i++, registroVO.getNumero().intValue());
            ps.setString(i++, registroVO.getNumeroRegistro());
            Date fecha = null;
            if (registroVO.getFechaAlta()!=null) {
                fecha = new Date(registroVO.getFechaAlta().getTimeInMillis());
            }
            ps.setDate(i++, fecha);
            fecha = null;
            if (registroVO.getFechaAprobacion()!=null) {
                fecha = new Date(registroVO.getFechaAprobacion().getTimeInMillis());
            }
            ps.setDate(i++, fecha);
            fecha = null;
            if (registroVO.getFechaVigencia()!=null) {
                fecha = new Date(registroVO.getFechaVigencia().getTimeInMillis());
            }
            ps.setDate(i++, fecha);
            fecha = null;
            if (registroVO.getFechaBaja()!=null) {
                fecha = new Date(registroVO.getFechaBaja().getTimeInMillis());
            }
            ps.setDate(i++, fecha);
            ps.setString(i++, registroVO.getCodigoProcedimiento());
            Integer numeroProcedimiento = registroVO.getNumeroProcedimiento();
            if (numeroProcedimiento == null) {
                numeroProcedimiento = new Integer(-1);
            }
            ps.setInt(i++, numeroProcedimiento.intValue());
            ps.setString(i++, registroVO.getCodigoAmbito());
            ps.setString(i++, registroVO.getParcela());
            String promotorAux = null;
            if (registroVO.getPromotor()!=null) {
                promotorAux = registroVO.getPromotor().toString();
            }
            ps.setString(i++, promotorAux);
            ps.setString(i++, registroVO.getCodigoOrganoAprobacion());
            ps.setString(i++, registroVO.getObjetoConvenio());
            ps.setString(i++, registroVO.getDenominacionBien());
            Integer domicilio = registroVO.getCodigoDomicilio();
            if (domicilio==null) {
                domicilio = new Integer(-1);
            }
            ps.setInt(i++, domicilio.intValue());
            ps.setString(i++, registroVO.getCodigoCatalogacion());
            ps.setString(i++, registroVO.getCodigoGradoProteccion());
            ps.setString(i++, registroVO.getCodigoRelacionBien());
            fecha = null;
            if (registroVO.getFechaPublicacion()!=null) {
                fecha = new Date(registroVO.getFechaPublicacion().getTimeInMillis());
            }
            ps.setDate(i++, fecha);
            ps.setString(i++, registroVO.getNumeroPublicacion());
            ps.setString(i++, registroVO.getObservaciones());
            ps.setString(i++, registroVO.getArchivo());

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BDException(ex.getMessage());
        }
    }

    public void delete(RegistroValueObject registroVO, Connection conexion)
            throws BDException {

        String sql = "";

        try{
            m_Log.info("RegistroDAO.delete");

            sql = "DELETE FROM " + nombreTabla + " WHERE " + tipoRegistro + "=? AND " + codigoSubseccion + "=? AND " +
                    codigoTipo + "=? AND " + anho + "=? AND " + numero + "=?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, registroVO.getTipoRegistro().toString());
            ps.setString(i++, registroVO.getCodigoSubseccion());
            ps.setString(i++, registroVO.getCodigoTipo());
            ps.setString(i++, registroVO.getAnho());
            ps.setInt(i++, registroVO.getNumero().intValue());

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BDException(ex.getMessage());
        }
    }

    public void modify(RegistroValueObject registroVO, Connection conexion)
            throws BDException {

        String sql = "";

        try{

            m_Log.info("RegistroDAO.modify");
            sql = "UPDATE " + nombreTabla + " SET " + numeroRegistro + "=?, " + fechaAlta + "=?, " + fechaAprobacion +
                    "=?, " + fechaVigencia + "=?, " + fechaBaja + "=?, " + codigoProcedimiento + "=?, " +
                    numeroProcedimiento + "=?, " + codigoAmbito + "=?, " + parcela + "=?, " + promotor + "=?, " +
                    codigoOrganoAprobacion + "=?, " + objetoConvenio + "=?, " +
                    denominacionBien + "=?, " + codigoDomicilio + "=?, " + codigoCatalogacion + "=?, " +
                    codigoGradoProteccion + "=?, " + codigoRelacionBien + "=?, " + fechaPublicacion + "=?, " +
                    numeroPublicacion + "=?, " + observaciones + "=?, " + archivo + "=? WHERE " + tipoRegistro +
                    "=? AND " + codigoSubseccion + "=? AND " + codigoTipo + "=? AND " + anho + "=? AND " + numero + "=?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, registroVO.getNumeroRegistro());
            Date fecha = null;
            if (registroVO.getFechaAlta()!=null) {
                fecha = new Date(registroVO.getFechaAlta().getTimeInMillis());
            }
            ps.setDate(i++, fecha);
            fecha = null;
            if (registroVO.getFechaAprobacion()!=null) {
                fecha = new Date(registroVO.getFechaAprobacion().getTimeInMillis());
            }
            ps.setDate(i++, fecha);
            fecha = null;
            if (registroVO.getFechaVigencia()!=null) {
                fecha = new Date(registroVO.getFechaVigencia().getTimeInMillis());
            }
            ps.setDate(i++, fecha);
            fecha = null;
            if (registroVO.getFechaBaja()!=null) {
                fecha = new Date(registroVO.getFechaBaja().getTimeInMillis());
            }
            ps.setDate(i++, fecha);
            ps.setString(i++, registroVO.getCodigoProcedimiento());
            Integer numeroProcedimiento = registroVO.getNumeroProcedimiento();
            if (numeroProcedimiento==null) {
                numeroProcedimiento = new Integer(-1);
            }
            ps.setInt(i++, numeroProcedimiento.intValue());
            ps.setString(i++, registroVO.getCodigoAmbito());
            ps.setString(i++, registroVO.getParcela());
            String promotorAux = null;
            if (registroVO.getPromotor()!=null) {
                promotorAux = registroVO.getPromotor().toString();
            }
            ps.setString(i++, promotorAux);
            ps.setString(i++, registroVO.getCodigoOrganoAprobacion());
            ps.setString(i++, registroVO.getObjetoConvenio());
            ps.setString(i++, registroVO.getDenominacionBien());
            Integer domicilio = registroVO.getCodigoDomicilio();
            if (domicilio==null) {
                domicilio = new Integer(-1);
            }
            ps.setInt(i++, domicilio.intValue());
            ps.setString(i++, registroVO.getCodigoCatalogacion());
            ps.setString(i++, registroVO.getCodigoGradoProteccion());
            ps.setString(i++, registroVO.getCodigoRelacionBien());
            fecha = null;
            if (registroVO.getFechaPublicacion()!=null) {
                fecha = new Date(registroVO.getFechaPublicacion().getTimeInMillis());
            }
            ps.setDate(i++, fecha);
            ps.setString(i++, registroVO.getNumeroPublicacion());
            ps.setString(i++, registroVO.getObservaciones());
            ps.setString(i++, registroVO.getArchivo());
            ps.setString(i++, registroVO.getTipoRegistro().toString());
            ps.setString(i++, registroVO.getCodigoSubseccion());
            ps.setString(i++, registroVO.getCodigoTipo());
            ps.setString(i++, registroVO.getAnho());
            ps.setInt(i++, registroVO.getNumero().intValue());

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BDException(ex.getMessage());
        }
    }

    public Vector findAll(String[] params) throws TechnicalException {

        Vector registros = new Vector();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            sql = "SELECT " + tipoRegistro + ", " + codigoSubseccion + ", " + codigoTipo + ", " + anho + ", " + numero
                    + "," + numeroRegistro + "," + fechaAlta + "," + fechaAprobacion + "," +
                    fechaVigencia + "," + fechaBaja + "," + codigoProcedimiento + "," + numeroProcedimiento + "," +
                    codigoAmbito + "," + parcela + "," + promotor + "," + codigoOrganoAprobacion + "," +
                    objetoConvenio + "," + denominacionBien + "," + codigoDomicilio + "," + codigoCatalogacion + "," +
                    codigoGradoProteccion + "," + codigoRelacionBien + "," + fechaPublicacion + "," +
                    numeroPublicacion + "," + observaciones + "," + archivo + " FROM " + nombreTabla;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            int i = 1;
            while(rs.next()){
                i = 1;
                Character tipoReg = new Character(rs.getString(i++).charAt(0));
                String codSub = rs.getString(i++);
                String codTipo = rs.getString(i++);
                String año = rs.getString(i++);
                Integer num = new Integer(rs.getInt(i++));
                String numReg = rs.getString(i++);
                Calendar fecAlta = Calendar.getInstance();
                fecAlta.setTime(rs.getDate(i++));
                Calendar fecAprob = Calendar.getInstance();
                Date aux = rs.getDate(i++);
                if (aux==null) {
                    fecAprob = null;
                } else {
                    fecAprob.setTime(aux);
                }
                Calendar fecVig = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecVig = null;
                } else {
                    fecVig.setTime(aux);
                }
                Calendar fecBaja = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecBaja = null;
                } else {
                    fecBaja.setTime(aux);
                }
                String codProc = rs.getString(i++);
                Integer numProc = new Integer(rs.getInt(i++));
                String codAmb = rs.getString(i++);
                String parc = rs.getString(i++);
                Character promot = null;
                String auxChar = rs.getString(i++);
                if (auxChar != null) {
                    promot = new Character(auxChar.charAt(0));
                }
                String codOrg = rs.getString(i++);
                String objConv = rs.getString(i++);
                String denomBien = rs.getString(i++);
                Integer codDom = new Integer(rs.getInt(i++));
                String codCatal = rs.getString(i++);
                String codGrado = rs.getString(i++);
                String codRelBien = rs.getString(i++);
                Calendar fecPubli = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecPubli = null;
                } else {
                    fecPubli.setTime(aux);
                }
                String numPubli = rs.getString(i++);
                String obs = rs.getString(i++);
                String file = rs.getString(i++);

                registros.add(new RegistroValueObject(tipoReg, codSub, codTipo, num, año, numReg, fecAlta, fecAprob,
                        fecVig, fecBaja, codProc, numProc, codAmb, parc, promot, codOrg, objConv, denomBien, codDom,
                        codCatal, codGrado, codRelBien, fecPubli, numPubli, obs, file));
            }

            rs.close();
            st.close();
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            //Aquí se pueden lanzar TechnicalException que no se capturan.
            if (con!=null) {
                try{
                    con.close();
                } catch(SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }
        return registros;
    }

    public Vector findByProcedimiento(String codigoProc, String[] params) throws TechnicalException {

        Vector registros = new Vector();
        Connection con = null;
        ResultSet rs = null;
        String sql = "";

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            sql = "SELECT " + tipoRegistro + ", " + codigoSubseccion + ", " + codigoTipo + ", " + anho + ", " + numero
                    + "," + numeroRegistro + "," + fechaAlta + "," + fechaAprobacion + "," +
                    fechaVigencia + "," + fechaBaja + "," + codigoProcedimiento + "," + numeroProcedimiento + "," +
                    codigoAmbito + "," + parcela + "," + promotor + "," + codigoOrganoAprobacion + "," + objetoConvenio + "," +
                    denominacionBien + "," + codigoDomicilio + "," + codigoCatalogacion + "," + codigoGradoProteccion +
                    "," + codigoRelacionBien + "," + fechaPublicacion + "," + numeroPublicacion + "," + observaciones +
                    "," + archivo + " FROM " + nombreTabla + " WHERE " + codigoProcedimiento + "=?";

            PreparedStatement ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, codigoProc);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                i = 1;
                Character tipoReg = new Character(rs.getString(i++).charAt(0));
                String codSub = rs.getString(i++);
                String codTipo = rs.getString(i++);
                String año = rs.getString(i++);
                Integer num = new Integer(rs.getInt(i++));
                String numReg = rs.getString(i++);
                Calendar fecAlta = Calendar.getInstance();
                fecAlta.setTime(rs.getDate(i++));
                Calendar fecAprob = Calendar.getInstance();
                Date aux = rs.getDate(i++);
                if (aux==null) {
                    fecAprob = null;
                } else {
                    fecAprob.setTime(aux);
                }
                Calendar fecVig = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecVig = null;
                } else {
                    fecVig.setTime(aux);
                }
                Calendar fecBaja = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecBaja = null;
                } else {
                    fecBaja.setTime(aux);
                }
                String codProc = rs.getString(i++);
                Integer numProc = new Integer(rs.getInt(i++));
                String codAmb = rs.getString(i++);
                String parc = rs.getString(i++);
                Character promot = null;
                String auxChar = rs.getString(i++);
                if (auxChar != null) {
                    promot = new Character(auxChar.charAt(0));
                }
                String codOrg = rs.getString(i++);
                String objConv = rs.getString(i++);
                String denomBien = rs.getString(i++);
                Integer codDom = new Integer(rs.getInt(i++));
                String codCatal = rs.getString(i++);
                String codGrado = rs.getString(i++);
                String codRelBien = rs.getString(i++);
                Calendar fecPubli = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecPubli = null;
                } else {
                    fecPubli.setTime(aux);
                }
                String numPubli = rs.getString(i++);
                String obs = rs.getString(i++);
                String file = rs.getString(i++);

                registros.add(new RegistroValueObject(tipoReg, codSub, codTipo, num, año, numReg, fecAlta, fecAprob,
                        fecVig, fecBaja, codProc, numProc, codAmb, parc, promot, codOrg, objConv, denomBien, codDom,
                        codCatal, codGrado, codRelBien, fecPubli, numPubli, obs, file));
            }

            rs.close();
            ps.close();
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            //Aquí se pueden lanzar TechnicalException que no se capturan.
            if (con!=null) {
                try{
                    con.close();
                } catch(SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }
        return registros;
    }

    public Vector findByAmbito(String codigoAmb, String[] params) throws TechnicalException {

        Vector registros = new Vector();
        Connection con = null;
        ResultSet rs = null;
        String sql = "";

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            sql = "SELECT " + tipoRegistro + ", " + codigoSubseccion + ", " + codigoTipo + ", " + anho + ", " + numero
                    + "," + numeroRegistro + "," + fechaAlta + "," + fechaAprobacion + "," +
                    fechaVigencia + "," + fechaBaja + "," + codigoProcedimiento  + "," + numeroProcedimiento + "," +
                    codigoAmbito + "," + parcela + "," + promotor + "," + codigoOrganoAprobacion + "," +
                    objetoConvenio + "," + denominacionBien + "," + codigoDomicilio + "," + codigoCatalogacion + "," +
                    codigoGradoProteccion + "," + codigoRelacionBien + "," + fechaPublicacion + "," +
                    numeroPublicacion + "," + observaciones + "," + archivo + " FROM " + nombreTabla + " WHERE " +
                    codigoAmbito + "=?";

            PreparedStatement ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, codigoAmb);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                i = 1;
                Character tipoReg = new Character(rs.getString(i++).charAt(0));
                String codSub = rs.getString(i++);
                String codTipo = rs.getString(i++);
                String año = rs.getString(i++);
                Integer num = new Integer(rs.getInt(i++));
                String numReg = rs.getString(i++);
                Calendar fecAlta = Calendar.getInstance();
                fecAlta.setTime(rs.getDate(i++));
                Calendar fecAprob = Calendar.getInstance();
                Date aux = rs.getDate(i++);
                if (aux==null) {
                    fecAprob = null;
                } else {
                    fecAprob.setTime(aux);
                }
                Calendar fecVig = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecVig = null;
                } else {
                    fecVig.setTime(aux);
                }
                Calendar fecBaja = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecBaja = null;
                } else {
                    fecBaja.setTime(aux);
                }
                String codProc = rs.getString(i++);
                Integer numProc = new Integer(rs.getInt(i++));
                String codAmb = rs.getString(i++);
                String parc = rs.getString(i++);
                Character promot = null;
                String auxChar = rs.getString(i++);
                if (auxChar != null) {
                    promot = new Character(auxChar.charAt(0));
                }
                String codOrg = rs.getString(i++);
                String objConv = rs.getString(i++);
                String denomBien = rs.getString(i++);
                Integer codDom = new Integer(rs.getInt(i++));
                String codCatal = rs.getString(i++);
                String codGrado = rs.getString(i++);
                String codRelBien = rs.getString(i++);
                Calendar fecPubli = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecPubli = null;
                } else {
                    fecPubli.setTime(aux);
                }
                String numPubli = rs.getString(i++);
                String obs = rs.getString(i++);
                String file = rs.getString(i++);

                registros.add(new RegistroValueObject(tipoReg, codSub, codTipo, num, año, numReg, fecAlta, fecAprob,
                        fecVig, fecBaja, codProc, numProc, codAmb, parc, promot, codOrg, objConv, denomBien, codDom,
                        codCatal, codGrado, codRelBien, fecPubli, numPubli, obs, file));
            }

            rs.close();
            ps.close();
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            //Aquí se pueden lanzar TechnicalException que no se capturan.
            if (con!=null) {
                try{
                    con.close();
                } catch(SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }
        return registros;
    }

    public Vector findByOrganoAprobacion(String codigoOrganoAp, String[] params) throws TechnicalException {

        Vector registros = new Vector();
        Connection con = null;
        ResultSet rs = null;
        String sql = "";

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            sql = "SELECT " + tipoRegistro + ", " + codigoSubseccion + ", " + codigoTipo + ", " + anho + ", " + numero
                    + "," + numeroRegistro + "," + fechaAlta + "," + fechaAprobacion + "," +
                    fechaVigencia + "," + fechaBaja + "," + codigoProcedimiento + "," + numeroProcedimiento + "," +
                    codigoAmbito + "," + parcela + "," + promotor + "," + codigoOrganoAprobacion + "," + objetoConvenio + "," +
                    denominacionBien + "," + codigoDomicilio + "," + codigoCatalogacion + "," + codigoGradoProteccion +
                    "," + codigoRelacionBien + "," + fechaPublicacion + "," + numeroPublicacion + "," + observaciones +
                    "," + archivo + " FROM " + nombreTabla + " WHERE " + codigoOrganoAprobacion + "=?";

            PreparedStatement ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, codigoOrganoAp);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                i = 1;
                Character tipoReg = new Character(rs.getString(i++).charAt(0));
                String codSub = rs.getString(i++);
                String codTipo = rs.getString(i++);
                String año = rs.getString(i++);
                Integer num = new Integer(rs.getInt(i++));
                String numReg = rs.getString(i++);
                Calendar fecAlta = Calendar.getInstance();
                fecAlta.setTime(rs.getDate(i++));
                Calendar fecAprob = Calendar.getInstance();
                Date aux = rs.getDate(i++);
                if (aux==null) {
                    fecAprob = null;
                } else {
                    fecAprob.setTime(aux);
                }
                Calendar fecVig = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecVig = null;
                } else {
                    fecVig.setTime(aux);
                }
                Calendar fecBaja = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecBaja = null;
                } else {
                    fecBaja.setTime(aux);
                }
                String codProc = rs.getString(i++);
                Integer numProc = new Integer(rs.getInt(i++));
                String codAmb = rs.getString(i++);
                String parc = rs.getString(i++);
                Character promot = null;
                String auxChar = rs.getString(i++);
                if (auxChar != null) {
                    promot = new Character(auxChar.charAt(0));
                }
                String codOrg = rs.getString(i++);
                String objConv = rs.getString(i++);
                String denomBien = rs.getString(i++);
                Integer codDom = new Integer(rs.getInt(i++));
                String codCatal = rs.getString(i++);
                String codGrado = rs.getString(i++);
                String codRelBien = rs.getString(i++);
                Calendar fecPubli = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecPubli = null;
                } else {
                    fecPubli.setTime(aux);
                }
                String numPubli = rs.getString(i++);
                String obs = rs.getString(i++);
                String file = rs.getString(i++);

                registros.add(new RegistroValueObject(tipoReg, codSub, codTipo, num, año, numReg, fecAlta, fecAprob,
                        fecVig, fecBaja, codProc, numProc, codAmb, parc, promot, codOrg, objConv, denomBien, codDom, codCatal,
                        codGrado, codRelBien, fecPubli, numPubli, obs, file));
            }

            rs.close();
            ps.close();
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            //Aquí se pueden lanzar TechnicalException que no se capturan.
            if (con!=null) {
                try{
                    con.close();
                } catch(SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }
        return registros;
    }

    public Vector findByCatalogacion(String codigoCat, String[] params) throws TechnicalException {

        Vector registros = new Vector();
        Connection con = null;
        ResultSet rs = null;
        String sql = "";

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            sql = "SELECT " + tipoRegistro + ", " + codigoSubseccion + ", " + codigoTipo + ", " + anho + ", " + numero
                    + "," + numeroRegistro + "," + fechaAlta + "," + fechaAprobacion + "," +
                    fechaVigencia + "," + fechaBaja + "," + codigoProcedimiento+ "," + numeroProcedimiento + "," +
                    codigoAmbito +"," + parcela + "," + promotor + "," + codigoOrganoAprobacion + "," + objetoConvenio +
                    "," + denominacionBien + "," + codigoDomicilio + "," + codigoCatalogacion + "," +
                    codigoGradoProteccion + "," + codigoRelacionBien + "," + fechaPublicacion + "," +
                    numeroPublicacion + "," + observaciones + "," + archivo + " FROM " + nombreTabla + " WHERE " +
                    codigoCatalogacion + "=?";

            PreparedStatement ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, codigoCat);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                i = 1;
                Character tipoReg = new Character(rs.getString(i++).charAt(0));
                String codSub = rs.getString(i++);
                String codTipo = rs.getString(i++);
                String año = rs.getString(i++);
                Integer num = new Integer(rs.getInt(i++));
                String numReg = rs.getString(i++);
                Calendar fecAlta = Calendar.getInstance();
                fecAlta.setTime(rs.getDate(i++));
                Calendar fecAprob = Calendar.getInstance();
                Date aux = rs.getDate(i++);
                if (aux==null) {
                    fecAprob = null;
                } else {
                    fecAprob.setTime(aux);
                }
                Calendar fecVig = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecVig = null;
                } else {
                    fecVig.setTime(aux);
                }
                Calendar fecBaja = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecBaja = null;
                } else {
                    fecBaja.setTime(aux);
                }
                String codProc = rs.getString(i++);
                Integer numProc = new Integer(rs.getInt(i++));
                String codAmb = rs.getString(i++);
                String parc = rs.getString(i++);
                Character promot = null;
                String auxChar = rs.getString(i++);
                if (auxChar != null) {
                    promot = new Character(auxChar.charAt(0));
                }
                String codOrg = rs.getString(i++);
                String objConv = rs.getString(i++);
                String denomBien = rs.getString(i++);
                Integer codDom = new Integer(rs.getInt(i++));
                String codCatal = rs.getString(i++);
                String codGrado = rs.getString(i++);
                String codRelBien = rs.getString(i++);
                Calendar fecPubli = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecPubli = null;
                } else {
                    fecPubli.setTime(aux);
                }
                String numPubli = rs.getString(i++);
                String obs = rs.getString(i++);
                String file = rs.getString(i++);

                registros.add(new RegistroValueObject(tipoReg, codSub, codTipo, num, año, numReg, fecAlta, fecAprob,
                        fecVig, fecBaja, codProc, numProc, codAmb, parc, promot, codOrg, objConv, denomBien, codDom,
                        codCatal, codGrado, codRelBien, fecPubli, numPubli, obs, file));
            }

            rs.close();
            ps.close();
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            //Aquí se pueden lanzar TechnicalException que no se capturan.
            if (con!=null) {
                try{
                    con.close();
                } catch(SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }
        return registros;
    }

    public Vector findByGradoProteccion(String codigoGrado, String[] params) throws TechnicalException {

        Vector registros = new Vector();
        Connection con = null;
        ResultSet rs = null;
        String sql = "";

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            sql = "SELECT " + tipoRegistro + ", " + codigoSubseccion + ", " + codigoTipo + ", " + anho + ", " + numero
                    + "," + numeroRegistro + "," + fechaAlta + "," + fechaAprobacion + "," +
                    fechaVigencia + "," + fechaBaja + "," + codigoProcedimiento + "," + numeroProcedimiento + "," +
                    codigoAmbito + "," + parcela + "," + promotor + "," + codigoOrganoAprobacion + "," +
                    objetoConvenio + "," + denominacionBien + "," + codigoDomicilio + "," + codigoCatalogacion + "," +
                    codigoGradoProteccion + "," + codigoRelacionBien + "," + fechaPublicacion + "," +
                    numeroPublicacion + "," + observaciones + "," + archivo + " FROM " + nombreTabla + " WHERE " +
                    codigoGradoProteccion + "=?";

            PreparedStatement ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, codigoGrado);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                i = 1;
                Character tipoReg = new Character(rs.getString(i++).charAt(0));
                String codSub = rs.getString(i++);
                String codTipo = rs.getString(i++);
                String año = rs.getString(i++);
                Integer num = new Integer(rs.getInt(i++));
                String numReg = rs.getString(i++);
                Calendar fecAlta = Calendar.getInstance();
                fecAlta.setTime(rs.getDate(i++));
                Calendar fecAprob = Calendar.getInstance();
                Date aux = rs.getDate(i++);
                if (aux==null) {
                    fecAprob = null;
                } else {
                    fecAprob.setTime(aux);
                }
                Calendar fecVig = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecVig = null;
                } else {
                    fecVig.setTime(aux);
                }
                Calendar fecBaja = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecBaja = null;
                } else {
                    fecBaja.setTime(aux);
                }
                String codProc = rs.getString(i++);
                Integer numProc = new Integer(rs.getInt(i++));
                String codAmb = rs.getString(i++);
                String parc = rs.getString(i++);
                Character promot = null;
                String auxChar = rs.getString(i++);
                if (auxChar != null) {
                    promot = new Character(auxChar.charAt(0));
                }
                String codOrg = rs.getString(i++);
                String objConv = rs.getString(i++);
                String denomBien = rs.getString(i++);
                Integer codDom = new Integer(rs.getInt(i++));
                String codCatal = rs.getString(i++);
                String codGrado = rs.getString(i++);
                String codRelBien = rs.getString(i++);
                Calendar fecPubli = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecPubli = null;
                } else {
                    fecPubli.setTime(aux);
                }
                String numPubli = rs.getString(i++);
                String obs = rs.getString(i++);
                String file = rs.getString(i++);

                registros.add(new RegistroValueObject(tipoReg, codSub, codTipo, num, año, numReg, fecAlta, fecAprob,
                        fecVig, fecBaja, codProc, numProc, codAmb, parc, promot, codOrg, objConv, denomBien, codDom,
                        codCatal, codGrado, codRelBien, fecPubli, numPubli, obs, file));
            }

            rs.close();
            ps.close();
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            //Aquí se pueden lanzar TechnicalException que no se capturan.
            if (con!=null) {
                try{
                    con.close();
                } catch(SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }
        return registros;
    }

    public Vector findByRelacionBien(String codigoRelacion, String[] params) throws TechnicalException {

        Vector registros = new Vector();
        Connection con = null;
        ResultSet rs = null;
        String sql = "";

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            sql = "SELECT " + tipoRegistro + ", " + codigoSubseccion + ", " + codigoTipo + ", " + anho + ", " + numero
                    + "," + numeroRegistro + "," + fechaAlta + "," + fechaAprobacion + "," +
                    fechaVigencia + "," + fechaBaja + "," + codigoProcedimiento + "," + numeroProcedimiento + "," +
                    codigoAmbito + "," + parcela + "," + promotor + "," + codigoOrganoAprobacion + "," +
                    objetoConvenio + "," + denominacionBien + "," + codigoDomicilio + "," + codigoCatalogacion + "," +
                    codigoGradoProteccion + "," + codigoRelacionBien + "," + fechaPublicacion + "," +
                    numeroPublicacion + "," + observaciones + "," + archivo + " FROM " + nombreTabla + " WHERE " +
                    codigoRelacionBien + "=?";

            PreparedStatement ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, codigoRelacion);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                i = 1;
                Character tipoReg = new Character(rs.getString(i++).charAt(0));
                String codSub = rs.getString(i++);
                String codTipo = rs.getString(i++);
                String año = rs.getString(i++);
                Integer num = new Integer(rs.getInt(i++));
                String numReg = rs.getString(i++);
                Calendar fecAlta = Calendar.getInstance();
                fecAlta.setTime(rs.getDate(i++));
                Calendar fecAprob = Calendar.getInstance();
                Date aux = rs.getDate(i++);
                if (aux==null) {
                    fecAprob = null;
                } else {
                    fecAprob.setTime(aux);
                }
                Calendar fecVig = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecVig = null;
                } else {
                    fecVig.setTime(aux);
                }
                Calendar fecBaja = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecBaja = null;
                } else {
                    fecBaja.setTime(aux);
                }
                String codProc = rs.getString(i++);
                Integer numProc = new Integer(rs.getInt(i++));
                String codAmb = rs.getString(i++);
                String parc = rs.getString(i++);
                Character promot = null;
                String auxChar = rs.getString(i++);
                if (auxChar != null) {
                    promot = new Character(auxChar.charAt(0));
                }
                String codOrg = rs.getString(i++);
                String objConv = rs.getString(i++);
                String denomBien = rs.getString(i++);
                Integer codDom = new Integer(rs.getInt(i++));
                String codCatal = rs.getString(i++);
                String codGrado = rs.getString(i++);
                String codRelBien = rs.getString(i++);
                Calendar fecPubli = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecPubli = null;
                } else {
                    fecPubli.setTime(aux);
                }
                String numPubli = rs.getString(i++);
                String obs = rs.getString(i++);
                String file = rs.getString(i++);

                registros.add(new RegistroValueObject(tipoReg, codSub, codTipo, num, año, numReg, fecAlta, fecAprob,
                        fecVig, fecBaja, codProc, numProc, codAmb, parc, promot, codOrg, objConv, denomBien, codDom,
                        codCatal, codGrado, codRelBien, fecPubli, numPubli, obs, file));
            }

            rs.close();
            ps.close();
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            //Aquí se pueden lanzar TechnicalException que no se capturan.
            if (con!=null) {
                try{
                    con.close();
                } catch(SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }
        return registros;
    }

    public Vector findBySubseccion(Character tipoReg, String codigoSubsec, String[] params) throws TechnicalException {

        Vector registros = new Vector();
        Connection con = null;
        ResultSet rs = null;
        String sql = "";

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            sql = "SELECT " + tipoRegistro + ", " + codigoSubseccion + ", " + codigoTipo + ", " + anho + ", " + numero
                    + "," + numeroRegistro + "," + fechaAlta + "," + fechaAprobacion + "," +
                    fechaVigencia + "," + fechaBaja + "," + codigoProcedimiento + "," + numeroProcedimiento + "," +
                    codigoAmbito + "," + parcela + "," + promotor + "," + codigoOrganoAprobacion + "," + objetoConvenio + "," +
                    denominacionBien + "," + codigoDomicilio + "," + codigoCatalogacion + "," + codigoGradoProteccion +
                    "," + codigoRelacionBien + "," + fechaPublicacion + "," + numeroPublicacion + "," + observaciones +
                    "," + archivo + " FROM " + nombreTabla + " WHERE " + tipoRegistro + "=? AND " + codigoSubseccion + "=?";

            PreparedStatement ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, tipoReg.toString());
            ps.setString(i++, codigoSubsec);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                i = 1;
                Character tipoR = new Character(rs.getString(i++).charAt(0));
                String codSub = rs.getString(i++);
                String codTipo = rs.getString(i++);
                String año = rs.getString(i++);
                Integer num = new Integer(rs.getInt(i++));
                String numReg = rs.getString(i++);
                Calendar fecAlta = Calendar.getInstance();
                fecAlta.setTime(rs.getDate(i++));
                Calendar fecAprob = Calendar.getInstance();
                Date aux = rs.getDate(i++);
                if (aux==null) {
                    fecAprob = null;
                } else {
                    fecAprob.setTime(aux);
                }
                Calendar fecVig = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecVig = null;
                } else {
                    fecVig.setTime(aux);
                }
                Calendar fecBaja = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecBaja = null;
                } else {
                    fecBaja.setTime(aux);
                }
                String codProc = rs.getString(i++);
                Integer numProc = new Integer(rs.getInt(i++));
                String codAmb = rs.getString(i++);
                String parc = rs.getString(i++);
                Character promot = null;
                String auxChar = rs.getString(i++);
                if (auxChar != null) {
                    promot = new Character(auxChar.charAt(0));
                }
                String codOrg = rs.getString(i++);
                String objConv = rs.getString(i++);
                String denomBien = rs.getString(i++);
                Integer codDom = new Integer(rs.getInt(i++));
                String codCatal = rs.getString(i++);
                String codGrado = rs.getString(i++);
                String codRelBien = rs.getString(i++);
                Calendar fecPubli = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecPubli = null;
                } else {
                    fecPubli.setTime(aux);
                }
                String numPubli = rs.getString(i++);
                String obs = rs.getString(i++);
                String file = rs.getString(i++);

                registros.add(new RegistroValueObject(tipoR, codSub, codTipo, num, año, numReg, fecAlta, fecAprob,
                        fecVig, fecBaja, codProc, numProc, codAmb, parc, promot, codOrg, objConv, denomBien, codDom,
                        codCatal, codGrado, codRelBien, fecPubli, numPubli, obs, file));
            }

            rs.close();
            ps.close();
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            //Aquí se pueden lanzar TechnicalException que no se capturan.
            if (con!=null) {
                try{
                    con.close();
                } catch(SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }
        return registros;
    }

    public Vector findByTipo(Character tipoReg, String codigoSubsec, String codigTipo, String[] params)
            throws TechnicalException {

        Vector registros = new Vector();
        Connection con = null;
        ResultSet rs = null;
        String sql = "";

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            sql = "SELECT " + tipoRegistro + ", " + codigoSubseccion + ", " + codigoTipo + ", " + anho + ", " + numero
                    + "," + numeroRegistro + "," + fechaAlta + "," + fechaAprobacion + "," +
                    fechaVigencia + "," + fechaBaja + "," + codigoProcedimiento + "," + numeroProcedimiento + "," +
                    codigoAmbito + "," + parcela + "," + promotor + "," + codigoOrganoAprobacion + "," + objetoConvenio + "," +
                    denominacionBien + "," + codigoDomicilio + "," + codigoCatalogacion + "," + codigoGradoProteccion +
                    "," + codigoRelacionBien + "," + fechaPublicacion + "," + numeroPublicacion + "," + observaciones +
                    "," + archivo + " FROM " + nombreTabla + " WHERE " + tipoRegistro + "=? AND " + codigoSubseccion +
                    "=? AND " + codigoTipo + "=?";

            PreparedStatement ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, tipoReg.toString());
            ps.setString(i++, codigoSubsec);
            ps.setString(i++, codigTipo);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                i = 1;
                Character tipoR = new Character(rs.getString(i++).charAt(0));
                String codSub = rs.getString(i++);
                String codTipo = rs.getString(i++);
                String año = rs.getString(i++);
                Integer num = new Integer(rs.getInt(i++));
                String numReg = rs.getString(i++);
                Calendar fecAlta = Calendar.getInstance();
                fecAlta.setTime(rs.getDate(i++));
                Calendar fecAprob = Calendar.getInstance();
                Date aux = rs.getDate(i++);
                if (aux==null) {
                    fecAprob = null;
                } else {
                    fecAprob.setTime(aux);
                }
                Calendar fecVig = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecVig = null;
                } else {
                    fecVig.setTime(aux);
                }
                Calendar fecBaja = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecBaja = null;
                } else {
                    fecBaja.setTime(aux);
                }
                String codProc = rs.getString(i++);
                Integer numProc = new Integer(rs.getInt(i++));
                String codAmb = rs.getString(i++);
                String parc = rs.getString(i++);
                Character promot = null;
                String auxChar = rs.getString(i++);
                if (auxChar != null) {
                    promot = new Character(auxChar.charAt(0));
                }
                String codOrg = rs.getString(i++);
                String objConv = rs.getString(i++);
                String denomBien = rs.getString(i++);
                Integer codDom = new Integer(rs.getInt(i++));
                String codCatal = rs.getString(i++);
                String codGrado = rs.getString(i++);
                String codRelBien = rs.getString(i++);
                Calendar fecPubli = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecPubli = null;
                } else {
                    fecPubli.setTime(aux);
                }
                String numPubli = rs.getString(i++);
                String obs = rs.getString(i++);
                String file = rs.getString(i++);

                registros.add(new RegistroValueObject(tipoR, codSub, codTipo, num, año, numReg, fecAlta, fecAprob,
                        fecVig, fecBaja, codProc, numProc, codAmb, parc, promot, codOrg, objConv, denomBien, codDom,
                        codCatal, codGrado, codRelBien, fecPubli, numPubli, obs, file));
            }

            rs.close();
            ps.close();
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            //Aquí se pueden lanzar TechnicalException que no se capturan.
            if (con!=null) {
                try{
                    con.close();
                } catch(SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }
        return registros;
    }

    public RegistroValueObject findByPrimaryKey(RegistroValueObject registroVO, String[] params) throws TechnicalException {

        RegistroValueObject registro = null;
        Connection con = null;
        ResultSet rs = null;
        String sql = "";

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            sql = "SELECT " + tipoRegistro + ", " + codigoSubseccion + ", " + codigoTipo + ", " + anho + ", " + numero
                    + "," + numeroRegistro + "," + fechaAlta + "," + fechaAprobacion + "," +
                    fechaVigencia + "," + fechaBaja + "," + codigoProcedimiento + "," + numeroProcedimiento + "," +
                    codigoAmbito + "," + parcela + "," + promotor + "," + codigoOrganoAprobacion + "," + objetoConvenio + "," +
                    denominacionBien + "," + codigoDomicilio + "," + codigoCatalogacion + "," + codigoGradoProteccion +
                    "," + codigoRelacionBien + "," + fechaPublicacion + "," + numeroPublicacion + "," + observaciones +
                    "," + archivo + " FROM " + nombreTabla + " WHERE " + tipoRegistro + "=? AND " + codigoSubseccion +
                    "=? AND " + anho + "=? AND " + numero + "=?";

            PreparedStatement ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, registroVO.getTipoRegistro().toString());
            ps.setString(i++, registroVO.getCodigoSubseccion());
            ps.setString(i++, registroVO.getAnho());
            ps.setInt(i++, registroVO.getNumero().intValue());
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = ps.executeQuery();
            if(rs.next()){
                i = 1;
                Character tipoReg = new Character(rs.getString(i++).charAt(0));
                String codSub = rs.getString(i++);
                String codTipo = rs.getString(i++);
                String año = rs.getString(i++);
                Integer num = new Integer(rs.getInt(i++));
                String numReg = rs.getString(i++);
                Calendar fecAlta = Calendar.getInstance();
                fecAlta.setTime(rs.getDate(i++));
                Calendar fecAprob = Calendar.getInstance();
                Date aux = rs.getDate(i++);
                if (aux==null) {
                    fecAprob = null;
                } else {
                    fecAprob.setTime(aux);
                }
                Calendar fecVig = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecVig = null;
                } else {
                    fecVig.setTime(aux);
                }
                Calendar fecBaja = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecBaja = null;
                } else {
                    fecBaja.setTime(aux);
                }
                String codProc = rs.getString(i++);
                Integer numProc = new Integer(rs.getInt(i++));
                String codAmb = rs.getString(i++);
                String parc = rs.getString(i++);
                Character promot = null;
                String auxChar = rs.getString(i++);
                if (auxChar != null) {
                    promot = new Character(auxChar.charAt(0));
                }
                String codOrg = rs.getString(i++);
                String objConv = rs.getString(i++);
                String denomBien = rs.getString(i++);
                Integer codDom = new Integer(rs.getInt(i++));
                String codCatal = rs.getString(i++);
                String codGrado = rs.getString(i++);
                String codRelBien = rs.getString(i++);
                Calendar fecPubli = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecPubli = null;
                } else {
                    fecPubli.setTime(aux);
                }
                String numPubli = rs.getString(i++);
                String obs = rs.getString(i++);
                String file = rs.getString(i++);

                registro = new RegistroValueObject(tipoReg, codSub, codTipo, num, año, numReg, fecAlta, fecAprob,
                        fecVig, fecBaja, codProc, numProc, codAmb, parc, promot, codOrg, objConv, denomBien, codDom,
                        codCatal, codGrado, codRelBien, fecPubli, numPubli, obs, file);
            }

            rs.close();
            ps.close();
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            //Aquí se pueden lanzar TechnicalException que no se capturan.
            if (con!=null) {
                try{
                    con.close();
                } catch(SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }
        return registro;
    }

    public Vector findByAll(RegistroValueObject registroVO, String[] params)
            throws TechnicalException {

        Vector registros = new Vector();
        Connection con = null;
        ResultSet rs = null;
        String sql = "";

        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            sql = "SELECT " + tipoRegistro + ", " + codigoSubseccion + ", " + codigoTipo + ", " + anho + ", " + numero
                    + "," + numeroRegistro + "," + fechaAlta + "," + fechaAprobacion + "," +
                    fechaVigencia + "," + fechaBaja + "," + codigoProcedimiento + "," + numeroProcedimiento + "," +
                    codigoAmbito + "," + parcela + "," + promotor + "," + codigoOrganoAprobacion + "," + objetoConvenio + "," +
                    denominacionBien + "," + codigoDomicilio + "," + codigoCatalogacion + "," + codigoGradoProteccion +
                    "," + codigoRelacionBien + "," + fechaPublicacion + "," + numeroPublicacion + "," + observaciones +
                    "," + archivo + " FROM " + nombreTabla + " WHERE " + tipoRegistro + "=?";

            /*
            * Preparacion de la consulta SQL
            */
            Vector strings = new Vector();
            Vector ints = new Vector();
            Vector fechas = new Vector();
            Calendar auxFecha = null;
            Integer auxInt = null;
            String auxString = registroVO.getCodigoSubseccion();
            if (auxString!=null && !auxString.equals("")) {
                sql = sql + " AND " + codigoSubseccion + " LIKE ?";
                strings.add(auxString.replaceAll("\\*", "%"));
            }
            auxString = registroVO.getCodigoTipo();
            if (auxString!=null && !auxString.equals("")) {
                sql = sql + " AND " + codigoTipo + " LIKE ?";
                strings.add(auxString.replaceAll("\\*", "%"));
            }
            auxString = registroVO.getAnho();
            if (auxString!=null && !auxString.equals("")) {
                sql = sql + " AND " + anho + " LIKE ?";
                strings.add(auxString.replaceAll("\\*", "%"));
            }
            auxString = registroVO.getNumeroRegistro();
            if (auxString!=null && !auxString.equals("")) {
                sql = sql + " AND " + numeroRegistro + " LIKE ?";
                strings.add(auxString.replaceAll("\\*", "%"));
            }
            auxString = registroVO.getCodigoProcedimiento();
            if (auxString!=null && !auxString.equals("")) {
                sql = sql + " AND " + codigoProcedimiento + " LIKE ?";
                strings.add(auxString.replaceAll("\\*", "%"));
            }
            auxString = registroVO.getCodigoAmbito();
            if (auxString!=null && !auxString.equals("")) {
                sql = sql + " AND " + codigoAmbito + " LIKE ?";
                strings.add(auxString.replaceAll("\\*", "%"));
            }
            auxString = registroVO.getParcela();
            if (auxString!=null && !auxString.equals("")) {
                sql = sql + " AND " + parcela + " LIKE ?";
                strings.add(auxString.replaceAll("\\*", "%"));
            }
            if (registroVO.getPromotor()!=null) {
                auxString = registroVO.getPromotor().toString();
                if (auxString!=null && !auxString.equals("")) {
                    sql = sql + " AND " + promotor + " LIKE ?";
                    strings.add(auxString.replaceAll("\\*", "%"));
                }
            }
            auxString = registroVO.getCodigoOrganoAprobacion();
            if (auxString!=null && !auxString.equals("")) {
                sql = sql + " AND " + codigoOrganoAprobacion + " LIKE ?";
                strings.add(auxString.replaceAll("\\*", "%"));
            }
            auxString = registroVO.getObjetoConvenio();
            if (auxString!=null && !auxString.equals("")) {
                sql = sql + " AND " + objetoConvenio + " LIKE ?";
                strings.add(auxString.replaceAll("\\*", "%"));
            }
            auxString = registroVO.getDenominacionBien();
            if (auxString!=null && !auxString.equals("")) {
                sql = sql + " AND " + denominacionBien + " LIKE ?";
                strings.add(auxString.replaceAll("\\*", "%"));
            }
            auxString = registroVO.getCodigoCatalogacion();
            if (auxString!=null && !auxString.equals("")) {
                sql = sql + " AND " + codigoCatalogacion + " LIKE ?";
                strings.add(auxString.replaceAll("\\*", "%"));
            }
            auxString = registroVO.getCodigoGradoProteccion();
            if (auxString!=null && !auxString.equals("")) {
                sql = sql + " AND " + codigoGradoProteccion + " LIKE ?";
                strings.add(auxString.replaceAll("\\*", "%"));
            }
            auxString = registroVO.getCodigoRelacionBien();
            if (auxString!=null && !auxString.equals("")) {
                sql = sql + " AND " + codigoRelacionBien + " LIKE ?";
                strings.add(auxString.replaceAll("\\*", "%"));
            }
            auxString = registroVO.getNumeroPublicacion();
            if (auxString!=null && !auxString.equals("")) {
                sql = sql + " AND " + numeroPublicacion + " LIKE ?";
                strings.add(auxString.replaceAll("\\*", "%"));
            }
            auxString = registroVO.getObservaciones();
            if (auxString!=null && !auxString.equals("")) {
                sql = sql + " AND " + observaciones + " LIKE ?";
                strings.add(auxString.replaceAll("\\*", "%"));
            }
            auxString = registroVO.getArchivo();
            if (auxString!=null && !auxString.equals("")) {
                sql = sql + " AND " + archivo + " LIKE ?";
                strings.add(auxString.replaceAll("\\*", "%"));
            }

            auxInt = registroVO.getNumero();
            if (auxInt!=null) {
                sql = sql + " AND " + numero + "=?";
                ints.add(auxInt);
            }
            auxInt = registroVO.getCodigoDomicilio();
            if (auxInt!=null) {
                sql = sql + " AND " + codigoDomicilio + "=?";
                ints.add(auxInt);
            }
            auxInt = registroVO.getNumeroProcedimiento();
            if (auxInt!=null) {
                sql = sql + " AND " + numeroProcedimiento + "=?";
                ints.add(auxInt);
            }

            auxFecha = registroVO.getFechaAlta();
            if (auxFecha!=null) {
                sql = sql + " AND " + fechaAlta + " LIKE ?";
                fechas.add(auxFecha);
            }
            auxFecha = registroVO.getFechaAprobacion();
            if (auxFecha!=null) {
                sql = sql + " AND " + fechaAprobacion + " LIKE ?";
                fechas.add(auxFecha);
            }
            auxFecha = registroVO.getFechaVigencia();
            if (auxFecha!=null) {
                sql = sql + " AND " + fechaVigencia + " LIKE ?";
                fechas.add(auxFecha);
            }
            auxFecha = registroVO.getFechaBaja();
            if (auxFecha!=null) {
                sql = sql + " AND " + fechaBaja + " LIKE ?";
                fechas.add(auxFecha);
            }
            auxFecha = registroVO.getFechaPublicacion();
            if (auxFecha!=null) {
                sql = sql + " AND " + fechaPublicacion + " LIKE ?";
                fechas.add(auxFecha);
            }
            /******************************************************************************************************/

            PreparedStatement ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, registroVO.getTipoRegistro().toString());
            for (int k=0;k<strings.size();k++) {
                ps.setString(i++, (String) strings.get(k));
            }
            for (int k=0;k<ints.size();k++) {
                ps.setInt(i++, ((Integer) ints.get(k)).intValue());
            }
            for (int k=0;k<fechas.size();k++) {
                ps.setDate(i++, new Date(((Calendar) fechas.get(k)).getTimeInMillis()));
            }

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                i = 1;
                Character tipoR = new Character(rs.getString(i++).charAt(0));
                String codSub = rs.getString(i++);
                String codTipo = rs.getString(i++);
                String año = rs.getString(i++);
                Integer num = new Integer(rs.getInt(i++));
                String numReg = rs.getString(i++);
                Calendar fecAlta = Calendar.getInstance();
                fecAlta.setTime(rs.getDate(i++));
                Calendar fecAprob = Calendar.getInstance();
                Date aux = rs.getDate(i++);
                if (aux==null) {
                    fecAprob = null;
                } else {
                    fecAprob.setTime(aux);
                }
                Calendar fecVig = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecVig = null;
                } else {
                    fecVig.setTime(aux);
                }
                Calendar fecBaja = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecBaja = null;
                } else {
                    fecBaja.setTime(aux);
                }
                String codProc = rs.getString(i++);
                Integer numProc = new Integer(rs.getInt(i++));
                String codAmb = rs.getString(i++);
                String parc = rs.getString(i++);
                Character promot = null;
                String auxChar = rs.getString(i++);
                if (auxChar != null) {
                    promot = new Character(auxChar.charAt(0));
                }
                String codOrg = rs.getString(i++);
                String objConv = rs.getString(i++);
                String denomBien = rs.getString(i++);
                Integer codDom = new Integer(rs.getInt(i++));
                String codCatal = rs.getString(i++);
                String codGrado = rs.getString(i++);
                String codRelBien = rs.getString(i++);
                Calendar fecPubli = Calendar.getInstance();
                aux = rs.getDate(i++);
                if (aux==null) {
                    fecPubli = null;
                } else {
                    fecPubli.setTime(aux);
                }
                String numPubli = rs.getString(i++);
                String obs = rs.getString(i++);
                String file = rs.getString(i++);

                registros.add(new RegistroValueObject(tipoR, codSub, codTipo, num, año, numReg, fecAlta, fecAprob,
                        fecVig, fecBaja, codProc, numProc, codAmb, parc, promot, codOrg, objConv, denomBien, codDom,
                        codCatal, codGrado, codRelBien, fecPubli, numPubli, obs, file));
            }

            rs.close();
            ps.close();
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            //Aquí se pueden lanzar TechnicalException que no se capturan.
            if (con!=null) {
                try{
                    con.close();
                } catch(SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }
        return registros;
    }
}