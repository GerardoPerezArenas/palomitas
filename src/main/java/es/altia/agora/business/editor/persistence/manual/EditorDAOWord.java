package es.altia.agora.business.editor.persistence.manual;

import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.util.Registro;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.AdaptadorSQL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.Vector;
import java.util.ArrayList;

public class EditorDAOWord
{
    /*
     * Mi propia instancia. Usada en el metodo getInstance
     */

    private static EditorDAOWord instance = null;
    protected static Config mTech; //Para el fichero de configuracion tecnico
    protected static Log m_Log =
            LogFactory.getLog(EditorDAOWord.class.getName());


    /**
     * Factory method para el<code>Singelton</code>.
     * @return La unica instancia de EditorDAOWord.
     */
    public static EditorDAOWord getInstance()
    {
        //si no hay ninguna instancia de esta clase tenemos que crear una
        if (instance == null)
        {
            // Necesitamos sincronizacion para serializar (no multithread)
            // Las invocaciones de este metodo
            synchronized(EditorDAOWord.class)
            {
                if (instance == null)
                {
                    instance = new EditorDAOWord();
                }
            }
        }
        return instance;
    }

    /**
     * Construye un nuevo EditorDAOWord. Es protected, por lo que la unica manera de instanciar esta clase
     * es usando el factory method <code>getInstance</code>
     */
    protected EditorDAOWord()
    {
        super();
        mTech = ConfigServiceHelper.getConfig("techserver");
    }


    public Vector obtenerDatosEtiquetas(String[] params,Registro parametros) throws Exception,SQLException
    {
        Connection con=null;
        Registro datos = new Registro();
        Vector vectorEtiquetas=new Vector();
        try
        {
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            StringBuffer stb = new StringBuffer();
            stb.append("SELECT ");
            stb.append(mTech.getString("SQL.A_ETI.descripcion")+","+
                    mTech.getString("SQL.A_ETI.codBD")+","+
                    mTech.getString("SQL.A_ETI.nombre")+","+
                    mTech.getString("SQL.A_ETI.codigoAplicacion")+
                    " FROM A_ETI WHERE "+
                    mTech.getString("SQL.A_ETI.codigoAplicacion")+"="+
                    parametros.getString("plt_apl"));
            String[] orden = {mTech.getString("SQL.A_ETI.nombre"),"3"};
            stb.append(oad.orderUnion(orden));
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(stb.toString());

            while (rs.next())
            {
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("ETI_DES",rs.getString(mTech.getString("SQL.A_ETI.descripcion")));
                gVO.setAtributo("ETI_CBD",rs.getString(mTech.getString("SQL.A_ETI.codBD")));
                gVO.setAtributo("ETI_NOM",rs.getString(mTech.getString("SQL.A_ETI.nombre")));
                gVO.setAtributo("ETI_CODAPLI",rs.getString(mTech.getString("SQL.A_ETI.codigoAplicacion")));
                vectorEtiquetas.add(gVO);
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            if (con != null)
                try
                {
                    con.close();
                }
                catch(SQLException ex)
                {
                    throw ex;
                }
        }
        return vectorEtiquetas;
    }

    public void modificarPlantilla(String[] params,Registro parametros) throws Exception,SQLException
    {
        Connection con=null;

        try
        {
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            con.setAutoCommit(true);

            byte[] plt_doc = (byte[])parametros.get("plt_doc");

            StringBuffer stb = new StringBuffer();

            stb.append("update A_PLT set PLT_DES=?,PLT_APL=?,PLT_DOC=? where PLT_cod='")
                    .append(parametros.getString("plt_cod"))
                    .append("'");

            PreparedStatement ps = con.prepareStatement(stb.toString());

            ps.setString(1,parametros.getString("plt_des"));

            ps.setInt(2,Integer.parseInt(parametros.getString("plt_apl")));

            if(plt_doc==null)
            {
                ps.setNull(3,java.sql.Types.BLOB);
                ps.executeUpdate();
            }
            else
            {
                java.io.InputStream st = new java.io.ByteArrayInputStream(plt_doc);
                ps.setBinaryStream(3,st,plt_doc.length);

                ps.executeUpdate();
                st.close();
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            if (con != null)
                try
                {
                    con.setAutoCommit(true);
                    con.close();
                }
                catch(SQLException ex)
                {
                    throw ex;
                }
        }
    }

    public void modificarPlantillaCRD(String[] params,Registro parametros) throws Exception,SQLException
    {
        Connection con=null;

        try
        {
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            con.setAutoCommit(true);

            if(m_Log.isDebugEnabled()) m_Log.debug("el fichero en el DAO es : " + parametros.get("crd_fil"));

            byte[] crd_fil = (byte[])parametros.get("crd_fil");

            if(m_Log.isDebugEnabled()){
                m_Log.debug("el fichero en el DAO es : " + crd_fil);
                m_Log.debug("el tamaño del fichero en el DAO es : " + crd_fil.length);
            }

            StringBuffer stb = new StringBuffer();
            stb.append("UPDATE E_CRD SET ")
                    .append(mTech.getString("SQL.E_CRD.fechaModificacion"))
                    .append("=")
                    .append(oad.convertir(oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE,null), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+",")
                    .append(mTech.getString("SQL.E_CRD.codUsuarioModif"))
                    .append("=")
                    .append(parametros.getString("codUsuario"))
                    .append(",")
                    .append(mTech.getString("SQL.E_CRD.descripcion"))
                    .append("='")
                    .append(parametros.getString("nombreDocumento"));
            if(crd_fil.length != 0) {
                stb.append("',")
                        .append(mTech.getString("SQL.E_CRD.fichero"))
                        .append("=? WHERE ");
            } else {
                stb.append("' WHERE ");
            }
            stb.append(mTech.getString("SQL.E_CRD.codMunicipio"))
                    .append("=")
                    .append(parametros.getString("codMunicipio"))
                    .append(" AND " )
                    .append(mTech.getString("SQL.E_CRD.codProcedimiento"))
                    .append("='")
                    .append(parametros.getString("codProcedimiento"))
                    .append("' AND ")
                    .append(mTech.getString("SQL.E_CRD.ejercicio"))
                    .append("=")
                    .append(parametros.getString("ejercicio"))
                    .append(" AND ")
                    .append(mTech.getString("SQL.E_CRD.numeroExpediente"))
                    .append("='")
                    .append(parametros.getString("numeroExpediente"))
                    .append("' AND ")
                    .append(mTech.getString("SQL.E_CRD.codTramite"))
                    .append("=")
                    .append(parametros.getString("codTramite"))
                    .append(" AND ")
                    .append(mTech.getString("SQL.E_CRD.ocurrencia"))
                    .append("=")
                    .append(parametros.getString("ocurrenciaTramite"))
                    .append(" AND ")
                    .append(mTech.getString("SQL.E_CRD.numeroDocumento"))
                    .append("=")
                    .append(parametros.getString("codDocumento"));

            PreparedStatement ps = con.prepareStatement(stb.toString());
            if(m_Log.isDebugEnabled()) m_Log.debug(stb.toString());

            java.io.InputStream st = null;

            if(crd_fil.length != 0) {
                st = new java.io.ByteArrayInputStream(crd_fil);
                ps.setBinaryStream(1,st,crd_fil.length);
            }

            ps.executeUpdate();
            if(crd_fil.length != 0) {
                st.close();
            }

        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            if (con != null)
                try
                {
                    con.setAutoCommit(true);
                    con.close();
                }
                catch(SQLException ex)
                {
                    throw ex;
                }
        }
    }





    public void grabarPlantilla(String[] params,Registro parametros) throws Exception,SQLException
    {
        Connection con=null;

        try
        {
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            con.setAutoCommit(true);

            byte[] plt_doc = (byte[])parametros.get("plt_doc");

            StringBuffer stb = new StringBuffer();

            // Obtenemos el número de tupla a insertar
            stb.append("select "+oad.funcionMatematica(oad.FUNCIONMATEMATICA_MAX, new String[]{"PLT_COD"})+" FROM A_PLT");
            PreparedStatement ps = con.prepareStatement(stb.toString());

            ResultSet rs = ps.executeQuery(stb.toString());

            int plt_cod = 1;
            if(rs.next()) //solo va a haber uno
                plt_cod=rs.getInt(1)+1;

            stb = new StringBuffer();
            stb.append("insert into A_PLT (PLT_COD,PLT_DES,PLT_APL,PLT_TXT,PLT_CLS,PLT_DOC) VALUES (?,?,?,null,?,?)");

            ps = con.prepareStatement(stb.toString());

            ps.setInt(1,plt_cod);
            ps.setString(2,parametros.getString("plt_des"));

            ps.setInt(3,Integer.parseInt(parametros.getString("plt_apl")));

            if(!parametros.getString("plt_apl").equals("4"))
                ps.setNull(4,java.sql.Types.VARCHAR);
            else
                ps.setString(4,parametros.getString("codClasif"));


            if(plt_doc==null)
            {
                ps.setNull(5,java.sql.Types.BLOB);
                ps.executeUpdate();
            }
            else
            {
                // El tipo de datos es un long raw
                java.io.InputStream st = new java.io.ByteArrayInputStream(plt_doc);
                ps.setBinaryStream(5,st,plt_doc.length);
                ps.executeUpdate();
                st.close();
            }

        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            if (con != null)
                try
                {
                    con.setAutoCommit(true);
                    con.close();
                }
                catch(SQLException ex)
                {
                    throw ex;
                }
        }
    }

    public void grabarCRD(String[] params,Registro parametros) throws Exception,SQLException
    {
        Connection con=null;

        try
        {
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            con.setAutoCommit(true);

            byte[] crd_fil = (byte[])parametros.get("crd_fil");

            StringBuffer stb = new StringBuffer();
            // Obtenemos el número de tupla a insertar
            stb.append("select "+oad.funcionMatematica(oad.FUNCIONMATEMATICA_MAX, new String[]{"CRD_NUD"})+" FROM E_CRD");

            PreparedStatement ps = con.prepareStatement(stb.toString());

            ResultSet rs = ps.executeQuery(stb.toString());

            int crd_nud = 1;
            if(rs.next()) //solo va a haber uno
                crd_nud=rs.getInt(1)+1;

            stb = new StringBuffer();

            stb.append("INSERT INTO E_CRD (")
                    .append(mTech.getString("SQL.E_CRD.codMunicipio"))
                    .append(",")
                    .append(mTech.getString("SQL.E_CRD.codProcedimiento"))
                    .append(",")
                    .append(mTech.getString("SQL.E_CRD.ejercicio"))
                    .append(",")
                    .append(mTech.getString("SQL.E_CRD.numeroExpediente"))
                    .append(",")
                    .append(mTech.getString("SQL.E_CRD.codTramite"))
                    .append(",")
                    .append(mTech.getString("SQL.E_CRD.ocurrencia"))
                    .append(",")
                    .append(mTech.getString("SQL.E_CRD.numeroDocumento"))
                    .append(",")
                    .append(mTech.getString("SQL.E_CRD.fechaAlta"))
                    .append(",")
                    .append(mTech.getString("SQL.E_CRD.fechaModificacion"))
                    .append(",")
                    .append(mTech.getString("SQL.E_CRD.codUsuarioCreac"))
                    .append(",")
                    .append(mTech.getString("SQL.E_CRD.codUsuarioModif"))
                    .append(",")
                    .append(mTech.getString("SQL.E_CRD.fichero"))
                    .append(",")
                    .append(mTech.getString("SQL.E_CRD.descripcion"))
                    .append(",")
                    .append(mTech.getString("SQL.E_CRD.codDocumento"))
                    .append(") VALUES (")
                    .append(parametros.getString("codMunicipio"))
                    .append(",'")
                    .append(parametros.getString("codProcedimiento"))
                    .append("',")
                    .append(parametros.getString("ejercicio"))
                    .append(",'")
                    .append(parametros.getString("numeroExpediente"))
                    .append("',")
                    .append(parametros.getString("codTramite"))
                    .append(",")
                    .append(parametros.getString("ocurrenciaTramite"))
                    .append(",")
                    .append(crd_nud)
                    .append(",")
                    .append(oad.convertir(oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE,null),AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY"))
                    .append(",")
                    .append("null")
                    .append(",")
                    .append(parametros.getString("codUsuario"))
                    .append(",")
                    .append("null")
                    .append(",")
                    .append("?")
                    .append(",'")
                    .append(parametros.getString("nombreDocumento"))
                    .append("',")
                    .append(parametros.getString("codDocumento"))
                    .append(")");

            ps = con.prepareStatement(stb.toString());
            if(m_Log.isDebugEnabled()) m_Log.debug(stb.toString());

            java.io.InputStream file = new java.io.ByteArrayInputStream(crd_fil);
            ps.setBinaryStream(1,file,crd_fil.length);
            ps.executeUpdate();
            file.close();

        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            if (con != null)
                try
                {
                    con.setAutoCommit(true);
                    con.close();
                }
                catch(SQLException ex)
                {
                    throw ex;
                }
        }
    }


    public Registro obtenerDatosPlantilla(String[] params,Registro parametros) throws Exception
    {
        Connection con=null;
        Registro datos = new Registro();

        try
        {
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            // Obtenemos los datos del registro indicado por su identificador
            Registro datosRegistro = obtenerValoresDB(con,parametros,oad);

            // Obtenemos los pares DES --> CDB
            Vector vectorEtiquetas = obtenerDatosEtiquetas(params,parametros);
            //Ahora transformamos lo que hay en el vectorEtiquetas a
            //paresDES_CBD para mantener la consistencia en el resto del
            //código
            Registro paresDES_CBD = new Registro();
            for(int i=0;i<vectorEtiquetas.size();i++)
            {
                GeneralValueObject gVO=(GeneralValueObject)vectorEtiquetas.elementAt(i);
                paresDES_CBD.setString((String)gVO.getAtributo("ETI_DES"),(String)gVO.getAtributo("ETI_CBD"));
            }

            Registro plantillaSinDatos = ObtenerPlantillaSinDatos(con,parametros);

            datos.put("datosRegistro",datosRegistro);
            datos.put("vectorEtiquetas",vectorEtiquetas);
            datos.put("paresDES_CBD",paresDES_CBD);
            datos.put("plantillaSinDatos",plantillaSinDatos);

            // Sustituimos las etiquetas por los valores correspondientes
            // Esta sustitución se tiene que hacer en la jsp con vba
            //datos = construirPlantillaConDatos(con,datosRegistro,paresDES_CBD,parametros);
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            if (con != null)
                try
                {
                    con.close();
                }
                catch(SQLException ex)
                {
                    throw ex;
                }
        }
        return datos;
    }

    public Registro obtenerPlantilla(String[] params,Registro parametros) throws Exception
    {
        Connection con=null;
        Registro datos = new Registro();
        byte [] plt_doc = null;

        try
        {
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            StringBuffer stb = new StringBuffer();
            stb.append("SELECT ");
            stb.append(mTech.getString("SQL.A_PLT.doc")+",");
            stb.append(mTech.getString("SQL.A_PLT.codigo")+",");
            stb.append(mTech.getString("SQL.A_PLT.descripcion")+",");
            stb.append(mTech.getString("SQL.A_PLT.codigoApli")+" FROM A_PLT ");
            stb.append("WHERE "+mTech.getString("SQL.A_PLT.codigo")+"='");
            stb.append(parametros.getString("plt_cod")+"'");

            if(m_Log.isDebugEnabled()) m_Log.debug(stb.toString());

            PreparedStatement ps = con.prepareStatement(stb.toString());
            ResultSet rs = ps.executeQuery(stb.toString());

            if (rs.next()) // Sólo va a devolver una tupla
                plt_doc = (byte[])rs.getObject(mTech.getString("SQL.A_PLT.doc"));

            datos.put("plt_doc",plt_doc);
            datos.setString("plt_cod",rs.getString(mTech.getString("SQL.A_PLT.codigo")));
            datos.setString("plt_des",rs.getString(mTech.getString("SQL.A_PLT.descripcion")));
            datos.setString("plt_apl",rs.getString(mTech.getString("SQL.A_PLT.codigoApli")));
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            if (con != null)
                try
                {
                    con.close();
                }
                catch(SQLException ex)
                {
                    throw ex;
                }
        }
        return datos;
    }

    public Registro obtenerPlantillaCRD(String[] params,Registro parametros) throws Exception
    {
        Connection con=null;
        Registro datos = new Registro();
        byte [] crd_fil = null;

        try
        {
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            StringBuffer stb = new StringBuffer();
            stb.append("SELECT ");
            stb.append(mTech.getString("SQL.E_CRD.fichero")+",");
            stb.append(mTech.getString("SQL.E_CRD.descripcion"));
            stb.append(" FROM E_CRD WHERE ");
            stb.append(mTech.getString("SQL.E_CRD.codMunicipio"))
                    .append("=")
                    .append(parametros.getString("codMunicipio"))
                    .append(" AND " )
                    .append(mTech.getString("SQL.E_CRD.codProcedimiento"))
                    .append("='")
                    .append(parametros.getString("codProcedimiento"))
                    .append("' AND ")
                    .append(mTech.getString("SQL.E_CRD.ejercicio"))
                    .append("=")
                    .append(parametros.getString("ejercicio"))
                    .append(" AND ")
                    .append(mTech.getString("SQL.E_CRD.numeroExpediente"))
                    .append("='")
                    .append(parametros.getString("numeroExpediente"))
                    .append("' AND ")
                    .append(mTech.getString("SQL.E_CRD.codTramite"))
                    .append("=")
                    .append(parametros.getString("codTramite"))
                    .append(" AND ")
                    .append(mTech.getString("SQL.E_CRD.ocurrencia"))
                    .append("=")
                    .append(parametros.getString("ocurrenciaTramite"))
                    .append(" AND ")
                    .append(mTech.getString("SQL.E_CRD.numeroDocumento"))
                    .append("=")
                    .append(parametros.getString("codDocumento"));

            if(m_Log.isDebugEnabled()) m_Log.debug(stb.toString());

            PreparedStatement ps = con.prepareStatement(stb.toString());
            ResultSet rs = ps.executeQuery(stb.toString());

            if (rs.next()) // Sólo va a devolver una tupla
                crd_fil = (byte[])rs.getObject(mTech.getString("SQL.E_CRD.fichero"));

            datos.put("crd_fil",crd_fil);
            datos.setString("plt_des",rs.getString(mTech.getString("SQL.E_CRD.descripcion")));
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            if (con != null)
                try
                {
                    con.close();
                }
                catch(SQLException ex)
                {
                    throw ex;
                }
        }
        return datos;
    }



    private Registro obtenerValoresDB(Connection con,Registro parametros,AdaptadorSQLBD oad) throws Exception
    {
        try
        {
            StringBuffer stb = new StringBuffer();

            if(parametros.getString("plt_apl").equals("1")) // Registro Entrada / Salida
            {
                StringBuffer parteFrom = new StringBuffer();
                parteFrom.append(oad.convertir(mTech.getString("SQL.R_RES.fechaAnotacion"),AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY HH24:MI:SS"))
                        .append(" AS ")
                        .append(mTech.getString("SQL.R_RES.fechaAnotacion"))
                        .append(",")
                        .append(mTech.getString("SQL.R_RES.tipoDoc"))
                        .append(",")
                        .append(mTech.getString("SQL.R_RES.asunto"))
                        .append(",")
                        .append(mTech.getString("SQL.R_RES.tipoAnotacionDestino"))
                        .append(",")
                        .append(oad.convertir(mTech.getString("SQL.R_RES.fechaDocumento"),AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY HH24:MI:SS"))
                        .append(" AS ")
                        .append(mTech.getString("SQL.R_RES.fechaDocumento"))
                        .append(",")
                        .append(mTech.getString("SQL.R_RES.tipoRemitente"))
                        .append(",")
                        .append(mTech.getString("SQL.R_RES.codTercero"))
                        .append(",")
                        .append(mTech.getString("SQL.R_RES.domicTercero"))
                        .append(",")
                        .append(mTech.getString("SQL.R_RES.modifInteresado"))
                        .append(",")
                        .append(mTech.getString("SQL.R_RES.numTransporte"))
                        .append(",")
                        .append(mTech.getString("SQL.R_RES.tipoTransporte"))
                        .append(",")
                        .append(mTech.getString("SQL.R_RES.entidadDestino"))
                        .append(",")
                                // tiruritata
                                /*.append(mTech.getString("SQL.R_RES.dptoDestino"))
                                .append(",")*/
                        .append(mTech.getString("SQL.R_RES.unidRegDestino"))
                        .append(",")
                        .append(mTech.getString("SQL.R_RES.orgDestAnot"))
                        .append(",")
                                // tiruritata
                                /*.append(mTech.getString("SQL.R_RES.departOrigDest"))
                                .append(",")*/
                        .append(mTech.getString("SQL.R_RES.unidOrigDest"))
                        .append(",")
                        .append(mTech.getString("SQL.R_RES.departOrigAnot"))
                        .append(",")
                        .append(mTech.getString("SQL.R_RES.orgOrigAnot"))
                        .append(",")
                        .append(mTech.getString("SQL.R_RES.unidOrigAnot"))
                        .append(",")
                        .append(mTech.getString("SQL.R_RES.entOrigAnot"))
                        .append(",")
                        .append(mTech.getString("SQL.R_RES.tipoOrigDest"))
                        .append(",")
                        .append(mTech.getString("SQL.R_RES.numOrig"))
                        .append(",")
                        .append(mTech.getString("SQL.R_RES.actuacion"))
                        .append(",")
                        .append(mTech.getString("SQL.R_RES.estAnot"))
                        .append(",")
                        .append(mTech.getString("SQL.R_RES.diligencia"))
                        .append(",R_TDO.")
                        .append(mTech.getString("SQL.R_TDO.codigo"))
                        .append(",R_TPE.")
                        .append(mTech.getString("SQL.R_TPE.codigo"))
                        .append(",R_TDO.")
                        .append(mTech.getString("SQL.R_TDO.descripcion"))
                        .append(",R_TPE.")
                        .append(mTech.getString("SQL.R_TPE.descripcion"))
                        .append(",R_TTR.")
                        .append(mTech.getString("SQL.R_TTR.descripcion"))
                        .append(",R_TTR.")
                        .append(mTech.getString("SQL.R_TTR.codigo"))
                        .append(" AS COD_TRANSP,R_ACT.")
                        .append(mTech.getString("SQL.R_ACT.descripcion"))
                        .append(",R_ACT.")
                        .append(mTech.getString("SQL.R_ACT.codigo"))
                        .append(",A_UOR.")
                        .append(mTech.getString("SQL.A_UOR.nombre"))
                        .append(",A_DEP.")
                        .append(mTech.getString("SQL.A_DEP.nombre"))
                        .append(",A_ENT.")
                        .append(mTech.getString("SQL.A_ENT.nombre"))
                        .append(",A_ORG.")
                        .append(mTech.getString("SQL.A_ORG.descripcion"))
                        .append(",A.")
                        .append(mTech.getString("SQL.A_UOR.nombre"))
                        .append(" AS UOR_ORIGEN,D.")
                        .append(mTech.getString("SQL.A_DEP.nombre"))
                        .append(" AS DEP_ORIGEN,E.")
                        .append(mTech.getString("SQL.A_ENT.nombre"))
                        .append(" AS ENT_ORIGEN ,O.")
                        .append(mTech.getString("SQL.A_ORG.descripcion"))
                        .append(" AS ORG_ORIGEN, A1.")
                        .append(mTech.getString("SQL.A_UOR.nombre"))
                        .append(" AS UOR_DESTINO, D1.")
                        .append(mTech.getString("SQL.A_DEP.nombre"))
                        .append(" AS DEP_DESTINO");

                StringBuffer parteWhere = new StringBuffer();
                parteWhere.append(mTech.getString("SQL.R_RES.codDpto"))
                        .append("=")
                        .append(parametros.getString("identDepart"))
                        .append(" AND ")
                        .append(mTech.getString("SQL.R_RES.codUnidad"))
                        .append("=")
                        .append(parametros.getString("unidadOrgan"))
                        .append(" AND ")
                        .append(mTech.getString("SQL.R_RES.tipoReg"))
                        .append("='")
                        .append(parametros.getString("tipoReg"))
                        .append("' AND ")
                        .append(mTech.getString("SQL.R_RES.ejercicio"))
                        .append("=")
                        .append(parametros.getString("anoReg"))
                        .append(" AND ")
                        .append(mTech.getString("SQL.R_RES.numeroAnotacion"))
                        .append("=")
                        .append(parametros.getString("numReg"));

                ArrayList join = new ArrayList();
                join.add("A_DEP, A_DEP D1, R_RES");
                join.add("INNER");
                join.add("R_TDO");
                join.add("R_RES.RES_TDO=R_TDO.TDO_IDE");
                join.add("INNER");
                join.add("R_TPE");
                join.add("R_RES.RES_TPE=R_TPE.TPE_IDE");
                join.add("LEFT");
                join.add("R_TTR");
                join.add("R_RES.RES_TTR=R_TTR.TTR_IDE");
                join.add("LEFT");
                join.add("R_ACT");
                join.add("R_RES.RES_ACT=R_ACT.ACT_IDE");
                join.add("LEFT");
                join.add("A_UOR");
                join.add("R_RES.RES_UCD=A_UOR.UOR_COD");
                join.add("LEFT");
                join.add(GlobalNames.ESQUEMA_GENERICO + "A_ENT A_ENT");
                join.add("R_RES.RES_OCD=A_ENT.ENT_ORG AND R_RES.RES_ECD=A_ENT.ENT_COD");
                join.add("LEFT");
                join.add(GlobalNames.ESQUEMA_GENERICO + "A_ORG A_ORG");
                join.add("R_RES.RES_OCD=A_ORG.ORG_COD");
                join.add("LEFT");
                join.add("A_UOR A");
                join.add("R_RES.RES_UCO=A.UOR_COD");
                join.add("LEFT");
                join.add("A_DEP D");
                join.add("R_RES.RES_DCO=D.DEP_COD");
                join.add("LEFT");
                join.add(GlobalNames.ESQUEMA_GENERICO + "A_ENT E");
                join.add("R_RES.RES_OCO=E.ENT_ORG AND R_RES.RES_ECO=E.ENT_COD");
                join.add("LEFT");
                join.add(GlobalNames.ESQUEMA_GENERICO + "A_ORG O");
                join.add("R_RES.RES_OCO=O.ORG_COD");
                join.add("LEFT");
                join.add("A_UOR A1");
                join.add("R_RES.RES_UOD=A1.UOR_COD");
                join.add("false");

                stb.append(oad.join(parteFrom.toString(), parteWhere.toString(), (String[]) join.toArray(new String[]{})));
            }
            else if(parametros.getString("plt_apl").equals("2")) // Padrón
            {
                // Poner la query correspondientes
            }
            else if(parametros.getString("plt_apl").equals("3")) // TercerosImpl y Territorio
            {
                // Poner la query correspondientes
            }
            else if(parametros.getString("plt_apl").equals("4")) // Gestión de Expedientes
            {

                StringBuffer parteFrom = new StringBuffer();
                parteFrom.append(mTech.getString("SQL.E_EXT.codTercero"))
                        .append(",")
                        .append(
                                oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,new String[]{mTech.getString("SQL.T_TER.partApellido1"),"' '",
                                        mTech.getString("SQL.T_TER.apellido1"),"' '",
                                        mTech.getString("SQL.T_TER.partApellido2"),"' '",
                                        mTech.getString("SQL.T_TER.apellido2"),"', '",
                                        mTech.getString("SQL.T_TER.nombre")}))
                        .append(" AS TER_NOM")
                        .append(",uor2.")
                        .append(mTech.getString("SQL.A_UOR.nombre"))
                        .append(" AS EXP_UTML,")
                        .append(mTech.getString("SQL.E_EXP.ano"))
                        .append(",")
                        .append(mTech.getString("SQL.E_EXP.numero"))
                        .append(",")
                        .append(mTech.getString("SQL.E_CRO.codUnidadTramitadora"))
                        .append(",")
                        .append(oad.convertir(mTech.getString("SQL.E_EXP.fechaInicio"),AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY"))
                        .append(" AS ")
                        .append(mTech.getString("SQL.E_EXP.fechaInicio"))
                        .append(",")
                        .append(mTech.getString("SQL.E_EXP.codMunicipio"))
                        .append(",")
                        .append(mTech.getString("SQL.E_EXP.codProcedimiento"))
                        .append(",")
                        .append(mTech.getString("SQL.E_CRO.codTramite"))
                        .append(",")
                        .append(oad.convertir(mTech.getString("SQL.E_EXP.fechaFin"),AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY"))
                        .append(" AS ")
                        .append(mTech.getString("SQL.E_EXP.fechaFin"))
                        .append(",")
                        .append(mTech.getString("SQL.E_EXP.estado"))
                        .append(",uor1.")
                        .append(mTech.getString("SQL.A_UOR.nombre"))
                        .append(" AS CRO_UTML,")
                        .append(mTech.getString("SQL.A_USU.nombre"));

                StringBuffer parteWhere = new StringBuffer();
                parteWhere.append(mTech.getString("SQL.E_EXP.codMunicipio"))
                        .append("=")
                        .append(parametros.getString("codMunicipio"))
                        .append(" AND ")
                        .append(mTech.getString("SQL.E_EXP.ano"))
                        .append("=")
                        .append(parametros.getString("ejercicio"))
                        .append(" AND ")
                        .append(mTech.getString("SQL.E_EXP.numero"))
                        .append("='")
                        .append(parametros.getString("numeroExpediente"))
                        .append("' AND ")
                        .append(mTech.getString("SQL.E_CRO.codProcedimiento"))
                        .append("='")
                        .append(parametros.getString("codProcedimiento"))
                        .append("' AND ")
                        .append(mTech.getString("SQL.E_CRO.codTramite"))
                        .append("=")
                        .append(parametros.getString("codTramite"))
                        .append(" AND ")
                        .append(mTech.getString("SQL.E_CRO.ocurrencia"))
                        .append("=")
                        .append(parametros.getString("ocurrenciaTramite"));

                ArrayList join = new ArrayList();
                join.add("E_EXP");
                join.add("INNER");
                join.add("E_CRO");
                join.add("E_EXP.EXP_MUN=E_CRO.CRO_MUN AND E_EXP.EXP_EJE=E_CRO.CRO_EJE AND E_EXP.EXP_NUM=E_CRO.CRO_NUM");
                join.add("LEFT");
                join.add("E_EXT");
                join.add("E_EXP.EXP_MUN=E_EXT.EXT_MUN AND E_EXP.EXP_EJE=E_EXT.EXT_EJE AND E_EXP.EXP_NUM=E_EXT.EXT_NUM");
                join.add("LEFT");
                join.add("T_TER");
                join.add("E_EXT.EXT_TER=T_TER.TER_COD AND E_EXT.EXT_NVR=T_TER.TER_NVE");
                join.add("INNER");
                join.add("A_UOR uor1");
                join.add("E_CRO.CRO_UTR=uor1.UOR_COD");
                join.add("INNER");
                join.add("A_UOR uor2");
                join.add("E_EXP.EXP_UOR=uor2.UOR_COD");
                join.add("INNER");
                join.add(GlobalNames.ESQUEMA_GENERICO + "A_USU A_USU");
                join.add("E_CRO.CRO_USU=A_USU.USU_COD");
                join.add("false");

                stb.append(oad.join(parteFrom.toString(), parteWhere.toString(), (String[]) join.toArray(new String[]{})));
            }

            if(m_Log.isDebugEnabled()) m_Log.debug(stb.toString());
            PreparedStatement ps = con.prepareStatement(stb.toString());

            ResultSet rs = ps.executeQuery(stb.toString());

            Registro r = new Registro();

            ResultSetMetaData md = rs.getMetaData();

            if (rs.next()) // Sólo va a devolver una tupla
            {
                // Ejemplo de como quedaría  --> r.setString("HTE_NOM",rs.getString(1));
                // Ojo md.getColumName(1) siempre devuelve los campos en mayusculas
                for(int i=1;i<=md.getColumnCount();i++) {
                    String aux = rs.getString(i);

                    if(md.getColumnName(i).equals(mTech.getString("SQL.R_RES.asunto")) || md.getColumnName(i).equals(mTech.getString("SQL.R_RES.diligencia"))) {
                        if(aux != null)
                            r.setString(md.getColumnName(i),AdaptadorSQLBD.js_escape(aux));
                        else
                            r.setString(md.getColumnName(i),"");
                    } else {
                        r.setString(md.getColumnName(i),aux);
                    }
                }
            }

            return r;
        }
        catch(Exception e)
        {
            throw e;
        }
    }


    private void replace(StringBuffer orig, String o, String n,boolean all)
    {
        if (orig == null || o == null || o.length() == 0 || n == null)
            throw new IllegalArgumentException("Null or zero-lengthString");
        int i = 0;
        while (i + o.length() <= orig.length())
        {
            if (orig.substring(i, i + o.length()).equals(o))
            {
                orig.replace(i, i + o.length(), n);
                if (!all)
                    break;
                else
                    i += n.length();
            }
            else
                i++;
        }
    }


    private Registro ObtenerPlantillaSinDatos(Connection con,Registro parametros) throws Exception
    {
        try
        {
            byte[] plt_doc = null;
            StringBuffer stb = new StringBuffer();

            // Obtenemos los datos de la plantilla
            stb.append("SELECT ");
            stb.append(mTech.getString("SQL.A_PLT.doc")+",");
            stb.append(mTech.getString("SQL.A_PLT.codigo")+",");
            stb.append(mTech.getString("SQL.A_PLT.descripcion")+",");
            stb.append(mTech.getString("SQL.A_PLT.codigoApli")+" FROM A_PLT ");
            stb.append("WHERE "+mTech.getString("SQL.A_PLT.codigo")+"='");
            stb.append(parametros.getString("plt_cod")+"'");

            PreparedStatement ps = con.prepareStatement(stb.toString());
            ResultSet rs = ps.executeQuery(stb.toString());
            Registro r = new Registro();

            if (rs.next()) // Sólo va a devolver una tupla
                plt_doc = (byte[])rs.getObject(mTech.getString("SQL.A_PLT.doc"));

            r.put("plt_doc",plt_doc);
            r.setString("plt_cod",rs.getString(mTech.getString("SQL.A_PLT.codigo")));
            r.setString("plt_des",rs.getString(mTech.getString("SQL.A_PLT.descripcion")));
            r.setString("plt_apl",rs.getString(mTech.getString("SQL.A_PLT.codigoApli")));

            return r;
        }
        catch(Exception e)
        {
            throw e;
        }
    }



}
