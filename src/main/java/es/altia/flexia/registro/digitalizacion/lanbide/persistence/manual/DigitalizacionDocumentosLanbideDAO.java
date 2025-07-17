package es.altia.flexia.registro.digitalizacion.lanbide.persistence.manual;

import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.sge.DocumentoAnotacionRegistroVO;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.DocumentoCatalogacionVO;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.MetadatoCatalogacionVO;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.TipoDocumentalCatalogacionVO;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.GrupoTipDocVO;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.ValidacionRetramitacionCambioProVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class DigitalizacionDocumentosLanbideDAO {

    private static DigitalizacionDocumentosLanbideDAO instance = null;
    private static Logger log = Logger.getLogger(DigitalizacionDocumentosLanbideDAO.class.getName());

    public DigitalizacionDocumentosLanbideDAO() {
    }

    public static DigitalizacionDocumentosLanbideDAO getInstance() {
        // Necesitamos sincronización aquí para serializar (no multithread)
        // las invocaciones a este metodo
        synchronized (DigitalizacionDocumentosLanbideDAO.class) {
            if (instance == null) {
                instance = new DigitalizacionDocumentosLanbideDAO();
            }
        }
        return instance;
    }

    public List<TipoDocumentalCatalogacionVO> getTipDocCatalogacion(Connection con)
            throws SQLException {
        log.info("DigitalizacionDocumentosLanbideDAO : getTipDocCatalogacion()");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        List<TipoDocumentalCatalogacionVO> listadoTipDoc = new ArrayList<TipoDocumentalCatalogacionVO>();

        try {

            //Se cambia "SELECT TIPDOC_ID,TIPDOC_LANBIDE_ES,"
            //por "SELECT TIPDOC_ID,CODTIPDOC,TIPDOC_LANBIDE_ES,"
            //y se añade "tipoVO.setCodigoNuevo(rs.getInt("CODTIPDOC"));"
            //query = "SELECT TIPDOC_ID,CODTIPDOC,TIPDOC_LANBIDE_ES,TIPDOC_LANBIDE_EU FROM MELANBIDE68_TIPDOC_LANBIDE WHERE FECHA_BAJA IS NULL ORDER BY TIPDOC_LANBIDE_ES";
            query = "SELECT TIPDOC_ID,CODTIPDOC,COD_GRUPO_TIPDOC,TIPDOC_LANBIDE_ES,TIPDOC_LANBIDE_EU,REPLACE(DESCTIPDOC_LANBIDE_ES,CHR(10),'|') AS DESCTIPDOC_LANBIDE_ES,REPLACE(DESCTIPDOC_LANBIDE_EU,CHR(10),'|') AS DESCTIPDOC_LANBIDE_EU FROM MELANBIDE68_TIPDOC_LANBIDE WHERE FECHA_BAJA IS NULL ORDER BY TIPDOC_LANBIDE_ES";
            log.debug("Query = " + query);

            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                TipoDocumentalCatalogacionVO tipoVO = new TipoDocumentalCatalogacionVO();
                tipoVO.setIdentificador(rs.getInt("TIPDOC_ID"));
                tipoVO.setCodigoNuevo(rs.getInt("CODTIPDOC"));
                tipoVO.setDescripcion(rs.getString("TIPDOC_LANBIDE_ES"));
                tipoVO.setOtraDesc(rs.getString("TIPDOC_LANBIDE_EU"));
                tipoVO.setDescripcionLargaCAS(rs.getString("DESCTIPDOC_LANBIDE_ES"));
                tipoVO.setDescripcionLargaEUS(rs.getString("DESCTIPDOC_LANBIDE_EU"));
                tipoVO.setCodGrupo(rs.getString("COD_GRUPO_TIPDOC"));
                listadoTipDoc.add(tipoVO);
            }
        } catch (SQLException sqle) {
            log.error("Error al consultar los tipos documentales de Lanbide");
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al cerrar los recursos de la base de datos");
            }
        }

        return listadoTipDoc;
    }
    
    public List<GrupoTipDocVO> getGruposTipDoc(Connection con)
            throws SQLException {
        log.info("DigitalizacionDocumentosLanbideDAO : getGruposTipDoc()");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        List<GrupoTipDocVO> listadoGruposTipDoc = new ArrayList<GrupoTipDocVO>();

        try {
            query = "SELECT DES_VAL_COD,DES_NOM FROM E_DES_VAL WHERE DES_COD='GRTD' AND DES_VAL_ESTADO='A' ORDER BY DES_VAL_COD ";
            log.debug("Query = " + query);

            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                GrupoTipDocVO grupoVO = new GrupoTipDocVO();
                grupoVO.setCodGrupo(rs.getString("DES_VAL_COD"));
                String desc_eu_es = rs.getString("DES_NOM");
                String[] desc_eu_es_sep = desc_eu_es.split("[|]");
                grupoVO.setDescGrupo_es(desc_eu_es_sep[0]);
                grupoVO.setDescGrupo_eu(desc_eu_es_sep[1]);
               
                listadoGruposTipDoc.add(grupoVO);
            }
        } catch (SQLException sqle) {
            log.error("Error al consultar los grupos de tipos documentales de Lanbide");
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al cerrar los recursos de la base de datos");
            }
        }

        return listadoGruposTipDoc;
    }

    public List<MetadatoCatalogacionVO> getMetadatoCatalogByTipDoc(int tipoDoc, Connection con)
            throws SQLException {
        log.info("DigitalizacionDocumentosLanbideDAO : getMetadatoCatalogByTipDoc()");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        List<MetadatoCatalogacionVO> listadoMetad = new ArrayList<MetadatoCatalogacionVO>();

        try {
            query = "SELECT TIPDOC_ID,TIPDOC_ID_METADATO,TIPDOC_OBLIGATORIO FROM MELANBIDE68_DOKUSI_METADATOS WHERE TIPDOC_ID = ? AND FECHA_BAJA IS NULL";
            log.debug("Query = " + query);
            log.debug("Valores pasados a la query = " + tipoDoc);

            ps = con.prepareStatement(query);
            ps.setInt(1, tipoDoc);
            rs = ps.executeQuery();

            while (rs.next()) {
                MetadatoCatalogacionVO metadato = new MetadatoCatalogacionVO();
                metadato.setIdMetadato(rs.getString("TIPDOC_ID_METADATO"));
                metadato.setIdTipoDoc(rs.getInt("TIPDOC_ID"));
                metadato.setObligatorio(rs.getInt("TIPDOC_OBLIGATORIO"));
                listadoMetad.add(metadato);
            }
        } catch (SQLException sqle) {
            log.error("Error al consultar los metadatos de un tipo documental de Lanbide");
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al cerrar los recursos de la base de datos");
            }
        }

        return listadoMetad;
    }

    public int grabarMetadatoDocumento(DocumentoCatalogacionVO documento, Connection con)
            throws SQLException {
        log.info("DigitalizacionDocumentosLanbideDAO : grabarMetadatoDocumento()");
        PreparedStatement ps = null;
        String query;
        int insertedRows = 0;
        int updatedRows = 0;
        Config m_ConfigTechnical = null;
        String gestor = null;

        try {
            query = "UPDATE R_RED SET RED_TIPODOC_ID=?, RED_OBSERV=? WHERE RED_DOC_ID=?";
            log.debug("Query = " + query);
            log.debug("Valores de la query = " + documento.getTipoDocumental().getIdentificador() + " - " + documento.getObservDoc() + " - " + documento.getIdDocumento());

            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, documento.getTipoDocumental().getIdentificador());
            ps.setString(contbd++, documento.getObservDoc());
            ps.setLong(contbd, documento.getIdDocumento());

            updatedRows = ps.executeUpdate();

            if (documento.getMetadato() != null && updatedRows > 0) {
                m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
                gestor = m_ConfigTechnical.getString("CON.gestor");
                query = "";

                if (gestor.equalsIgnoreCase("oracle")) {
                    query = "INSERT INTO R_DOC_METADATOS (ID, RED_DOC_ID, ID_METADATO, VALOR_METADATO) "
                            + "VALUES (SEQ_R_DOC_METADATOS.nextval, ?, ?, ?)";
                } else if (gestor.equalsIgnoreCase("sqlserver")) {
                    query = "INSERT INTO R_DOC_METADATOS (RED_DOC_ID, ID_METADATO, VALOR_METADATO) "
                            + "VALUES (?, ?, ?)";
                }
                log.debug("Query = " + query);
                log.debug("Valores de la query = " + documento.getIdDocumento() + " - " + documento.getMetadatoId() + " - " + documento.getMetadatoValor());

                ps = con.prepareStatement(query);
                contbd = 1;
                ps.setLong(contbd++, documento.getIdDocumento());
                ps.setString(contbd++, documento.getMetadato().getIdMetadato());
                ps.setString(contbd++, documento.getMetadato().getValorMetadato());

                insertedRows = ps.executeUpdate();
            }
        } catch (SQLException sqle) {
            log.error("Error al grabar los metadatos del documento");
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al cerrar los recursos de la base de datos");
            }
        }
        // Si el documento no tiene metadatos: la grabacion es true si el update en R_RED se ha realizado
        // Si el documento tiene metadatos: la grabacion es true si el insert en R_DOC_METADATOS se ha realizado
        return documento.getMetadato() == null ? updatedRows : insertedRows;
    }

    public List<DocumentoCatalogacionVO> recuperarDocCatalogacion(DocumentoCatalogacionVO documento, Connection con)
            throws SQLException {
        log.info("DigitalizacionDocumentosLanbideDAO : recuperarDocCatalogacion()");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        DocumentoCatalogacionVO docCatalogacion = null;
        TipoDocumentalCatalogacionVO tipoDocumental = null;
        MetadatoCatalogacionVO metadato = null;
        List<DocumentoCatalogacionVO> listadoCatalogacion = new ArrayList<DocumentoCatalogacionVO>();
        String metadatoId = null, metadatoValor = null;

        try {
            // TODO: Comprobar si la bbdd es oracle para usar la secuencia

            query = "SELECT R_RED.RED_TIPODOC_ID,tip.TIPDOC_LANBIDE_ES,tip.TIPDOC_LANBIDE_EU,"
                    + "mtd.ID_METADATO,mtd.VALOR_METADATO,mtd.ID,REPLACE(RED_OBSERV,CHR(10),'|') AS RED_OBSERV "
                    + "FROM R_RED LEFT JOIN R_DOC_METADATOS mtd ON (mtd.RED_DOC_ID=R_RED.RED_DOC_ID) "
                    + "LEFT JOIN MELANBIDE68_TIPDOC_LANBIDE tip ON (R_RED.RED_TIPODOC_ID=tip.TIPDOC_ID) "
                    + "WHERE R_RED.RED_DEP=? AND R_RED.RED_UOR=? AND R_RED.RED_EJE=? AND "
                    + "R_RED.RED_NUM=? AND R_RED.RED_TIP=? AND R_RED.RED_NOM_DOC=?";

            log.debug("Query = " + query);
            log.debug("Valores de la query = " + documento.toString(false));

            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, documento.getDepartamento());
            ps.setLong(contbd++, documento.getUnidadOrg());
            ps.setLong(contbd++, documento.getEjercicio());
            ps.setLong(contbd++, documento.getNumeroAnot());
            ps.setString(contbd++, documento.getTipoAnot());
            ps.setString(contbd++, documento.getNomDocumento());

            rs = ps.executeQuery();
            while (rs.next()) {
                tipoDocumental = new TipoDocumentalCatalogacionVO();
                metadato = new MetadatoCatalogacionVO();
                docCatalogacion = new DocumentoCatalogacionVO();

                // Guardamos valores en las propiedades del objeto TipoDocumentalCatalogacionVO
                tipoDocumental.setIdentificador(rs.getInt("RED_TIPODOC_ID"));
                tipoDocumental.setDescripcion(rs.getString("TIPDOC_LANBIDE_ES"));
                tipoDocumental.setOtraDesc(rs.getString("TIPDOC_LANBIDE_EU"));
                // Guardamos valores en las propiedades del objeto MetadatoCatalogacionVO
                metadato.setIdTipoDoc(rs.getInt("RED_TIPODOC_ID"));
                metadatoId = rs.getString("ID_METADATO");
                if (metadatoId != null) {
                    metadato.setIdMetadato(metadatoId);
                } else {
                    metadato.setIdMetadato("");
                }
                metadatoValor = rs.getString("VALOR_METADATO");
                if (metadatoValor != null) {
                    metadato.setValorMetadato(metadatoValor);
                } else {
                    metadato.setValorMetadato("");
                }

                if (metadato.getIdMetadato() != null && !metadato.getIdMetadato().equals("")) {
                    PreparedStatement ps2 = null;
                    ResultSet rs2 = null;

                    String sql = "SELECT TIPDOC_OBLIGATORIO FROM MELANBIDE68_DOKUSI_METADATOS "
                            + " WHERE TIPDOC_ID =" + metadato.getIdTipoDoc()
                            + " AND TIPDOC_ID_METADATO='" + metadato.getIdMetadato() + "'";

                    log.debug("query2: " + sql);

                    ps2 = con.prepareStatement(sql);
                    rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                        metadato.setObligatorio(rs2.getInt("TIPDOC_OBLIGATORIO"));
                    }
                }
                // Duplicamos el objeto documento porque los valores de sus propiedades son comunes en todas las catalogaciones del documento
                docCatalogacion = (DocumentoCatalogacionVO) documento.clone();
                // Guardamos valores en el resto de propiedades del objeto DocumentoCatalogacionVO
                docCatalogacion.setIdentificador(rs.getLong("ID"));
                docCatalogacion.setMetadato(metadato);
                docCatalogacion.setTipoDocumental(tipoDocumental);
                String observDoc = rs.getString("RED_OBSERV");
                if (observDoc == null) {
                    observDoc = "";
                }
                docCatalogacion.setObservDoc(observDoc);

                listadoCatalogacion.add(docCatalogacion);
            }

        } catch (SQLException sqle) {
            log.error("Error al recuperar los metadatos del documento");
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al cerrar los recursos de la base de datos");
            }
        }

        return listadoCatalogacion;
    }

    public boolean borrarDocCatalogacion(DocumentoCatalogacionVO documento, Connection con)
            throws SQLException {
        log.info("DigitalizacionDocumentosLanbideDAO : borrarDocCatalogacion()");
        PreparedStatement ps = null;
        String query;
        int deletedRows = 0;

        try {
            // TODO: Comprobar si la bbdd es oracle para usar la secuencia
            query = "DELETE FROM R_DOC_METADATOS mtd WHERE mtd.RED_DOC_ID=?";
            log.debug("Query = " + query);
            log.debug("Valores de la query = " + documento.getIdDocumento());

            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setLong(contbd, documento.getIdDocumento());
            deletedRows = ps.executeUpdate();

            if (deletedRows > 0) {
                query = "UPDATE R_RED SET RED_TIPODOC_ID=? WHERE RED_DEP=? AND RED_UOR=? AND RED_EJE=? AND RED_NUM=? AND RED_TIP=? AND RED_NOM_DOC=?";

                ps = con.prepareStatement(query);
                ps.setNull(contbd++, java.sql.Types.INTEGER);
                ps.setInt(contbd++, documento.getDepartamento());
                ps.setLong(contbd++, documento.getUnidadOrg());
                ps.setInt(contbd++, documento.getEjercicio());
                ps.setLong(contbd++, documento.getNumeroAnot());
                ps.setString(contbd++, documento.getTipoAnot());
                ps.setString(contbd++, documento.getNomDocumento());

                ps.executeUpdate();
            }
        } catch (SQLException sqle) {
            log.error("Error al borrar los metadatos del documento");
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al cerrar los recursos de la base de datos");
            }
        }

        return deletedRows > 0;
    }

    //se añade MELANBIDE68_TIPDOC_LANBIDE.CODTIPDOC y tipoVO.setCodigoNuevo(rs.getInt("CODTIPDOC"));
    public List<TipoDocumentalCatalogacionVO> getTipDocCatalogacionProcedimiento(String codProcedimiento, Connection con)
            throws SQLException {
        log.info("DigitalizacionDocumentosLanbideDAO : getTipDocCatalogacionProcedimiento()");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        List<TipoDocumentalCatalogacionVO> listadoTipDoc = new ArrayList<TipoDocumentalCatalogacionVO>();

        //se modifica para coger las descripciones de la tabla MELANBIDE68_TIPDOC_LANBIDE (antes se cogía de MELANBIDE68_TIPDOC_PROC y en esta tabla se eliminan esos campos)
        try {
            query = "SELECT MELANBIDE68_TIPDOC_PROC.TIPDOC_ID,MELANBIDE68_TIPDOC_LANBIDE.CODTIPDOC,MELANBIDE68_TIPDOC_LANBIDE.COD_GRUPO_TIPDOC,MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_LANBIDE_ES,MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_LANBIDE_EU FROM MELANBIDE68_TIPDOC_PROC "
                    + " INNER JOIN MELANBIDE68_TIPDOC_LANBIDE"
                    + " ON MELANBIDE68_TIPDOC_PROC.TIPDOC_ID = MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_ID"
                    + " WHERE COD_PROC ='" + codProcedimiento + "'"
                    + " AND MELANBIDE68_TIPDOC_LANBIDE.FECHA_BAJA IS NULL"
                    + " ORDER BY MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_LANBIDE_ES";
            log.debug("Query = " + query);

            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                TipoDocumentalCatalogacionVO tipoVO = new TipoDocumentalCatalogacionVO();
                tipoVO.setIdentificador(rs.getInt("TIPDOC_ID"));
                tipoVO.setDescripcion(rs.getString("TIPDOC_LANBIDE_ES"));
                tipoVO.setOtraDesc(rs.getString("TIPDOC_LANBIDE_EU"));
                tipoVO.setCodigoNuevo(rs.getInt("CODTIPDOC"));
                tipoVO.setCodGrupo(rs.getString("COD_GRUPO_TIPDOC"));
                listadoTipDoc.add(tipoVO);
            }
        } catch (SQLException sqle) {
            log.error("Error al consultar los tipos documentales de Lanbide");
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al cerrar los recursos de la base de datos");
            }
        }

        return listadoTipDoc;
    }
    
    public List<TipoDocumentalCatalogacionVO> getTipDocCatalogacionProcedimiento(String ejercicio, String nRegistro, Connection con)
            throws SQLException {
        log.info("DigitalizacionDocumentosLanbideDAO : getTipDocCatalogacionProcedimiento()");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        List<TipoDocumentalCatalogacionVO> listadoTipDoc = new ArrayList<TipoDocumentalCatalogacionVO>();

        try {
            /*query = "SELECT MELANBIDE68_TIPDOC_PROC.TIPDOC_ID,MELANBIDE68_TIPDOC_LANBIDE.CODTIPDOC,MELANBIDE68_TIPDOC_LANBIDE.COD_GRUPO_TIPDOC,MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_LANBIDE_ES,MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_LANBIDE_EU FROM MELANBIDE68_TIPDOC_PROC "
                    + " INNER JOIN MELANBIDE68_TIPDOC_LANBIDE"
                    + " ON MELANBIDE68_TIPDOC_PROC.TIPDOC_ID = MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_ID"
                    + " WHERE COD_PROC ='" + codProcedimiento + "'"
                    + " AND MELANBIDE68_TIPDOC_LANBIDE.FECHA_BAJA IS NULL"
                    + " ORDER BY MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_LANBIDE_ES";*/
            query = "SELECT MELANBIDE68_TIPDOC_PROC.TIPDOC_ID,\n" +
                    "       MELANBIDE68_TIPDOC_LANBIDE.CODTIPDOC,\n" +
                    "       MELANBIDE68_TIPDOC_LANBIDE.COD_GRUPO_TIPDOC,\n" +
                    "       MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_LANBIDE_ES,\n" +
                    "       MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_LANBIDE_EU,\n" +
                    "       REPLACE(MELANBIDE68_TIPDOC_LANBIDE.DESCTIPDOC_LANBIDE_ES,CHR(10),'|') AS DESCTIPDOC_LANBIDE_ES,\n" +
                    "       REPLACE(MELANBIDE68_TIPDOC_LANBIDE.DESCTIPDOC_LANBIDE_EU,CHR(10),'|') AS DESCTIPDOC_LANBIDE_EU\n" +
                    "FROM MELANBIDE68_TIPDOC_PROC \n" +
                    "INNER JOIN MELANBIDE68_TIPDOC_LANBIDE\n" +
                    "    ON MELANBIDE68_TIPDOC_PROC.TIPDOC_ID = MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_ID\n" +
                    "WHERE COD_PROC = \n" +
                    "    (\n" +
                    "    SELECT PROCEDIMIENTO \n" +
                    "    FROM R_RES \n" +
                    "    WHERE RES_EJE=" + ejercicio + "\n" +
                    "    AND RES_NUM=" + nRegistro + "\n" +
                    "    AND RES_TIP='E' \n" +
                    "    )\n" +
                    "AND MELANBIDE68_TIPDOC_LANBIDE.FECHA_BAJA IS NULL\n" +
                    "ORDER BY MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_LANBIDE_ES";
            log.debug("Query = " + query);

            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                TipoDocumentalCatalogacionVO tipoVO = new TipoDocumentalCatalogacionVO();
                tipoVO.setIdentificador(rs.getInt("TIPDOC_ID"));
                tipoVO.setDescripcion(rs.getString("TIPDOC_LANBIDE_ES"));
                tipoVO.setOtraDesc(rs.getString("TIPDOC_LANBIDE_EU"));
                tipoVO.setCodigoNuevo(rs.getInt("CODTIPDOC"));
                tipoVO.setDescripcionLargaCAS(rs.getString("DESCTIPDOC_LANBIDE_ES"));
                tipoVO.setDescripcionLargaEUS(rs.getString("DESCTIPDOC_LANBIDE_EU"));
                tipoVO.setCodGrupo(rs.getString("COD_GRUPO_TIPDOC"));
                listadoTipDoc.add(tipoVO);
            }
        } catch (SQLException sqle) {
            log.error("Error al consultar los tipos documentales de Lanbide");
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al cerrar los recursos de la base de datos");
            }
        }

        return listadoTipDoc;
    }

    public ArrayList getOidsDocumentosRegistro(int ejercicio, long numeroRegistro, Connection con)
            throws SQLException {
        log.info("getOidsDocumentosRegistro:INI");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        ArrayList listadoIdDocumentos = new ArrayList();

        try {
            query = "SELECT RED_IDDOC_GESTOR FROM R_RED WHERE RED_EJE =" + ejercicio + " AND RED_NUM=" + numeroRegistro;
            log.debug("Query = " + query);

            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                listadoIdDocumentos.add(rs.getString("RED_IDDOC_GESTOR"));
            }
        } catch (SQLException sqle) {
            log.error("Error al consultar los oid documentos registro");
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al cerrar los recursos de la base de datos");
            }
        }

        return listadoIdDocumentos;
    }

    public List<DocumentoAnotacionRegistroVO> getDocumentosRegistro(RegistroValueObject registro, Connection con) {
        log.info("getDocumentosRegistro:INI");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        int contbd;
        List<DocumentoAnotacionRegistroVO> listaDocumentos = null;
        DocumentoAnotacionRegistroVO documentoReg;

        try {
            // Query para obtener los documentos del registro que han sido digitalizados
            query = "SELECT RED_NOM_DOC,RED_IDDOC_GESTOR,RED_DOCDIGIT"
                    + " FROM R_RED"
                    + " WHERE RED_DEP=?"
                    + " AND RED_UOR=?"
                    + " AND RED_EJE =?"
                    + " AND RED_NUM=?"
                    + " AND RED_TIP=?";
            log.info("Query = " + query);
            log.info("Parametros de query = " + registro.getIdentDepart() + "-" + registro.getUnidadOrgan() + "-" + registro.getAnoReg() + "-" + registro.getNumReg() + "-" + registro.getTipoReg());

            ps = con.prepareStatement(query);
            contbd = 1;
            ps.setInt(contbd++, registro.getIdentDepart());
            ps.setInt(contbd++, registro.getUnidadOrgan());
            ps.setInt(contbd++, registro.getAnoReg());
            ps.setLong(contbd++, registro.getNumReg());
            ps.setString(contbd++, registro.getTipoReg());

            rs = ps.executeQuery();

            listaDocumentos = new ArrayList<DocumentoAnotacionRegistroVO>();
            int index = 1;
            while (rs.next()) {
                documentoReg = new DocumentoAnotacionRegistroVO();

                documentoReg.setNombreDocumento(rs.getString("RED_NOM_DOC"));
                boolean compulsado = rs.getBoolean("RED_DOCDIGIT");
                if (compulsado) {
                    documentoReg.setCompulsado(true);
                    String idDocGestor = rs.getString("RED_IDDOC_GESTOR");
                    if (idDocGestor != null) {
                        documentoReg.setCodDepartamento(registro.getIdentDepart());
                        documentoReg.setCodigoUorRegistro(registro.getUnidadOrgan());
                        documentoReg.setEjercicio(registro.getAnoReg());
                        documentoReg.setNumeroAnotacion((int) registro.getNumReg());
                        documentoReg.setTipoEntrada(registro.getTipoReg());
                        documentoReg.setIdDocGestor(idDocGestor);
                        listaDocumentos.add(documentoReg);
                    }
                } else {
                    documentoReg.setCompulsado(false);
                }
                log.info("Documento " + (index++) + " -> Nombre: " + documentoReg.getNombreDocumento()
                        + " - ¿Compulsado?: " + documentoReg.isCompulsado() + " - Id doc gestor: " + documentoReg.getIdDocGestor());
            }

        } catch (SQLException sqle) {
            log.error("Error al consultar los oid documentos registro");
            sqle.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al cerrar los recursos de la base de datos");
            }
        }
        return listaDocumentos;

    }

    public List<DocumentoCatalogacionVO> getDocumentosRegistroCompleto(int departamento, int uor, int anoReg, long numReg, String tipoReg, Connection con) {
        log.info("getDocumentosRegistroCompleto:INI");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        List<DocumentoCatalogacionVO> listaDocumentos = null;

        try {
            listaDocumentos = new ArrayList<DocumentoCatalogacionVO>();

            query = "SELECT RED_NOM_DOC, RED_IDDOC_GESTOR, RED_TIPODOC_ID, RED_MIGRA "
                    + " ,RED_DOC_ID "
                    + " ,RED_DOCDIGIT "
                    + " FROM R_RED "
                    + " WHERE RED_DEP=? AND RED_UOR=? AND RED_EJE =? AND RED_NUM=? AND RED_TIP=?";

            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, departamento);
            ps.setInt(contbd++, uor);
            ps.setInt(contbd++, anoReg);
            ps.setLong(contbd++, numReg);
            ps.setString(contbd++, tipoReg);

            log.debug("Query = " + query);
            log.debug("Parametros de la query = " + departamento + "-" + uor + "-" + anoReg + "-" + numReg + "-" + tipoReg);
            rs = ps.executeQuery();
            int index = 1;
            while (rs.next()) {
                DocumentoCatalogacionVO documentoReg = new DocumentoCatalogacionVO();
                documentoReg.setDepartamento(departamento);
                documentoReg.setUnidadOrg(uor);
                documentoReg.setEjercicio(anoReg);
                documentoReg.setNumeroAnot(numReg);
                documentoReg.setTipoAnot(tipoReg);
                documentoReg.setNomDocumento(rs.getString("RED_NOM_DOC"));
                documentoReg.setEsDocMigrado(rs.getInt("RED_MIGRA"));
                documentoReg.setEsDocCompulsado(rs.getInt("RED_DOCDIGIT"));

                String idDocGestor = rs.getString("RED_IDDOC_GESTOR");
                if (idDocGestor != null) {
                    log.debug("Existe el documento en Dokusi y tiene catalogación");
                    TipoDocumentalCatalogacionVO tipoDocumental = new TipoDocumentalCatalogacionVO();
                    tipoDocumental.setIdentificador(rs.getInt("RED_TIPODOC_ID"));
                    documentoReg.setIdDocumento(rs.getLong("RED_DOC_ID"));
                    documentoReg.setIdDocGestor(idDocGestor);
                    documentoReg.setTipoDocumental(tipoDocumental);
                    listaDocumentos.add(documentoReg);
                    log.debug("Documento " + (index++) + " -> Nombre: " + documentoReg.getNomDocumento() + " - Id doc gestor: " + documentoReg.getIdDocGestor());
                } else {
                    log.debug("No existe el documento en Dokusi");
                }
            }
        } catch (SQLException sqle) {
            log.error("Error al consultar los datos de documentos registro");
            sqle.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al cerrar los recursos de la base de datos");
            }
        }

        return listaDocumentos;
    }

    public String getOidDocumentoCatalogado(DocumentoCatalogacionVO docCatalogado, Connection con)
            throws SQLException {
        log.info("getOidDocumentoCatalogado:INI");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        String idDocumento = null;

        try {
            query = "SELECT RED_IDDOC_GESTOR FROM R_RED WHERE RED_DEP = ? AND RED_UOR = ? AND RED_TIP = ? AND RED_EJE = ? AND RED_NUM = ? "
                    + "AND RED_NOM_DOC = ?";

            log.debug("Query = " + query);
            log.debug("Parametros de la query = " + docCatalogado.getDepartamento() + " - " + docCatalogado.getUnidadOrg()
                    + " - " + docCatalogado.getTipoAnot() + " - " + docCatalogado.getEjercicio() + " - " + docCatalogado.getNumeroAnot()
                    + " - " + docCatalogado.getNomDocumento());

            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, docCatalogado.getDepartamento());
            ps.setLong(contbd++, docCatalogado.getUnidadOrg());
            ps.setString(contbd++, docCatalogado.getTipoAnot());
            ps.setInt(contbd++, docCatalogado.getEjercicio());
            ps.setLong(contbd++, docCatalogado.getNumeroAnot());
            ps.setString(contbd++, docCatalogado.getNomDocumento());
            rs = ps.executeQuery();
            while (rs.next()) {
                idDocumento = rs.getString("RED_IDDOC_GESTOR");
            }
        } catch (SQLException sqle) {
            log.error("Error al consultar el oid documento catalogado");
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al cerrar los recursos de la base de datos: " + ex.getMessage());
            }
        }

        return idDocumento;
    }

    public String getTipoDocumentalDokusi(DocumentoCatalogacionVO docCatalogado, Connection con)
            throws SQLException {
        log.info("getTipoDocumentalDokusi:INI");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        String idDocumento = null;

        try {
            query = "SELECT TIPDOC_DOKUSI FROM MELANBIDE68_DOKUSI_TIPDOC_LANB WHERE TIPDOC_ID = " + docCatalogado.getTipDocId();

            log.debug("Query = " + query);

            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                idDocumento = rs.getString("TIPDOC_DOKUSI");
            }
        } catch (SQLException sqle) {
            log.error("Error al consultar el tipo documental dokusi");
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al cerrar los recursos de la base de datos: " + ex.getMessage());
            }
        }

        return idDocumento;
    }

    public Map getListadoMetadatosDocumentoDokusi(DocumentoCatalogacionVO docCatalogado, Connection con)
            throws SQLException {
        log.info("getListadoMetadatosDocumentoDokusi:INI");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        String idMetadato = null;
        String valorMetadato = null;
        HashMap<String, String> listadoMetadatos = new HashMap<String, String>();

        try {
            query = "SELECT mtddk.TIPDOC_METADATODCTM, mtd.VALOR_METADATO "
                    + "FROM R_DOC_METADATOS mtd JOIN R_RED ON (mtd.RED_DOC_ID = R_RED.RED_DOC_ID) "
                    + "JOIN MELANBIDE68_DOKUSI_METADATOS mtddk ON (R_RED.RED_TIPODOC_ID = mtddk.TIPDOC_ID AND mtd.ID_METADATO = mtddk.TIPDOC_ID_METADATO) "
                    + "WHERE R_RED.RED_DEP = ? AND R_RED.RED_UOR = ? AND R_RED.RED_TIP = ? AND R_RED.RED_EJE = ?"
                    + " AND R_RED.RED_NUM = ? AND R_RED.RED_NOM_DOC = ?";

            log.debug("Query = " + query);
            log.debug("Parámetros de la query = " + docCatalogado.getDepartamento() + " - " + docCatalogado.getUnidadOrg() + " - " + docCatalogado.getTipoAnot()
                    + " - " + docCatalogado.getEjercicio() + " - " + docCatalogado.getNumeroAnot() + " - " + docCatalogado.getNomDocumento());

            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, docCatalogado.getDepartamento());
            ps.setLong(contbd++, docCatalogado.getUnidadOrg());
            ps.setString(contbd++, docCatalogado.getTipoAnot());
            ps.setInt(contbd++, docCatalogado.getEjercicio());
            ps.setLong(contbd++, docCatalogado.getNumeroAnot());
            ps.setString(contbd++, docCatalogado.getNomDocumento());

            rs = ps.executeQuery();
            while (rs.next()) {
                idMetadato = rs.getString("TIPDOC_METADATODCTM");
                valorMetadato = rs.getString("VALOR_METADATO");
                listadoMetadatos.put(idMetadato, valorMetadato);
            }
        } catch (SQLException sqle) {
            log.error("Error al consultar el listado metadatos dokusi");
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al cerrar los recursos de la base de datos: " + ex.getMessage());
            }
        }

        return listadoMetadatos;
    }

    public boolean isDocumentoDigitalizadoCatalogado(Integer codOrganizacion, Integer ejeRegistro, Long num, String oid, String codProcedimiento, Connection con)
            throws SQLException {
        log.info("isDocumentoDigitalizadoCatalogado: INICIO");
        int resultado = 0;
        String query = null;
        boolean isCatalogado = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            query = "SELECT COUNT(*) FROM R_DOC_METADATOS mtd JOIN R_RED ON (R_RED.RED_DOC_ID = mtd.RED_DOC_ID)"
                    + " WHERE R_RED.RED_DEP = 1"
                    + " AND R_RED.RED_UOR = " + codOrganizacion
                    + " AND R_RED.RED_EJE = " + ejeRegistro
                    + " AND R_RED.RED_NUM = " + num
                    + " AND R_RED.RED_TIP = 'E'"
                    + " AND R_RED.RED_IDDOC_GESTOR = '" + oid + "'"
                    + " AND R_RED.RED_DOCDIGIT = 1";

            log.debug("Query = " + query);

            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                resultado = rs.getInt(1);
                if (resultado > 0) {
                    isCatalogado = true;
                }
            }

        } catch (SQLException ex) {
            log.error("Error al consultar si el documento digitalizado contiene metadatos");
            ex.printStackTrace();
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al cerrar los recursos de la base de datos: " + ex.getMessage());
            }
        }
        return isCatalogado;
    }

    /**
     * *
     * Metodo para comprobar si el documento de registro es migrado o no. En
     * principio se ha creado para documentos migrados en RGI
     *
     * @param docCatalogado se evalua solo el OID getIdDocGestor
     * @param con
     * @return 0 Si no es migrado - 1 Si documento es migrado
     * @throws SQLException
     */
    public int isDocumentoMigradoxOID(DocumentoCatalogacionVO docCatalogado, Connection con) throws SQLException {
        log.info("isDocumentoMigradoxOID:INI");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        int retorno = 0;

        try {
            if (docCatalogado != null) {
                query = "SELECT RED_MIGRA FROM R_RED "
                        //+ " WHERE RED_DEP=? AND RED_UOR=? AND RED_EJE =? AND RED_NUM=? AND RED_TIP=? and RED_NOM_DOC=?";
                        + " WHERE RED_IDDOC_GESTOR = ?";

                ps = con.prepareStatement(query);
                ps.setString(1, docCatalogado.getIdDocGestor());
                log.info("isDocumentoMigradoxOID - Query :" + query);
                log.info("isDocumentoMigradoxOID - Params Query :" + docCatalogado.getIdDocGestor());
                //+ docCatalogado.getDepartamento()+","+docCatalogado.getUnidadOrg()+","+docCatalogado.getEjercicio()+","+docCatalogado.getNumeroAnot()+","+docCatalogado.getTipoAnot()+","+docCatalogado.getNomDocumento());
                rs = ps.executeQuery();
                while (rs.next()) {
                    retorno = rs.getInt("RED_MIGRA");
                }
            } else {
                log.error("isDocumentoMigradoxOID - Se ha recibido a null el objeto del documento a consultar. Se devuelve 0 - No migrado ");
            }
        } catch (SQLException sqle) {
            log.error("isDocumentoMigradoxOID SQLException - Error al consultar si es documento migrado o no. " + sqle.getMessage(), sqle);
            throw sqle;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("isDocumentoMigradoxOID - Error al cerrar los recursos de la base de datos: " + ex.getMessage(), ex);
            }
        }
        log.info("isDocumentoMigradoxOID:END " + retorno);
        return retorno;
    }
    
    public boolean borrarDatosCatalogacionRetramitarCambioProc(DocumentoCatalogacionVO documento, Connection con)
            throws SQLException {
        log.info("DigitalizacionDocumentosLanbideDAO : borrarDatosCatalogacionRetramitarCambioProc()");
        PreparedStatement ps = null;
        String query;
        int deletedRows = 0;
        try {
            // Borramos los Metadatos enc aso de existir
            query = "DELETE FROM R_DOC_METADATOS mtd WHERE mtd.RED_DOC_ID=?";
            log.info("Query Metadatos = " + query);
            log.info("params de la query = " + documento.getIdDocumento());

            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setLong(contbd, documento.getIdDocumento());
            deletedRows=ps.executeUpdate();
            log.info("MetadatosBorrados = " + deletedRows );
            contbd = 1;
            query = "UPDATE R_RED SET RED_TIPODOC_ID=null,RED_OBSERV=null WHERE RED_DEP=? AND RED_UOR=? AND RED_EJE=? AND RED_NUM=? AND RED_TIP=? AND RED_NOM_DOC=?";
            log.info("Query R_RED = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(contbd++, documento.getDepartamento());
            ps.setLong(contbd++, documento.getUnidadOrg());
            ps.setInt(contbd++, documento.getEjercicio());
            ps.setLong(contbd++, documento.getNumeroAnot());
            ps.setString(contbd++, documento.getTipoAnot());
            ps.setString(contbd++, documento.getNomDocumento());
            log.info("params de la query = null," + documento.getDepartamento() 
                    +"," + documento.getUnidadOrg()
                    +"," + documento.getEjercicio()
                    +"," + documento.getNumeroAnot()
                    +"," + documento.getTipoAnot()
                    +"," + documento.getNomDocumento()
            );
            deletedRows=ps.executeUpdate();
            log.info("Datos R_RED Actualizados a null = " + deletedRows);
        } catch (SQLException sqle) {
            log.error("Error al borrar los metadatos del documento");
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al cerrar los recursos de la base de datos");
            }
        }
        return deletedRows > 0;
    }
    
    public DocumentoCatalogacionVO getDocumentoRegistroByOID(String oidDocumento, Connection con) {
        log.info("getDocumentoRegistroByOID:INI");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        DocumentoCatalogacionVO respuesta = null;
        try {
            query = "SELECT RED_DEP,RED_UOR,RED_EJE,RED_NUM,RED_TIP "
                    + " ,RED_NOM_DOC, RED_IDDOC_GESTOR, RED_TIPODOC_ID, RED_MIGRA "
                    + " ,RED_DOC_ID "
                    + " ,RED_DOCDIGIT "
                    + " FROM R_RED "
                    + " WHERE RED_IDDOC_GESTOR=?";

            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setString(contbd++, oidDocumento);
            log.info("Query= " + query);
            log.info("Param= " + oidDocumento);
            rs = ps.executeQuery();
            while (rs.next()) {
                respuesta=new DocumentoCatalogacionVO();
                respuesta.setDepartamento(rs.getInt("RED_DEP"));
                respuesta.setUnidadOrg(rs.getInt("RED_UOR"));
                respuesta.setEjercicio(rs.getInt("RED_EJE"));
                respuesta.setNumeroAnot(rs.getInt("RED_NUM"));
                respuesta.setTipoAnot(rs.getString("RED_TIP"));
                respuesta.setNomDocumento(rs.getString("RED_NOM_DOC"));
                respuesta.setEsDocMigrado(rs.getInt("RED_MIGRA"));
                respuesta.setEsDocCompulsado(rs.getInt("RED_DOCDIGIT"));
                respuesta.setIdDocGestor(rs.getString("RED_IDDOC_GESTOR"));
                TipoDocumentalCatalogacionVO tipoDocumental = new TipoDocumentalCatalogacionVO();
                tipoDocumental.setIdentificador(rs.getInt("RED_TIPODOC_ID"));
                respuesta.setIdDocumento(rs.getLong("RED_DOC_ID"));
                respuesta.setTipoDocumental(tipoDocumental);
                log.debug("Documento -> Nombre: " + respuesta.getNomDocumento() + " - Id doc gestor: " + respuesta.getIdDocGestor());
            }
        } catch (SQLException ex) {
            log.error("Error al consultar los datos de documentos registro por OID " + ex.getErrorCode() + " " + ex.getSQLState() + " " +ex.getMessage(),ex);
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {rs.close();}
                if (ps != null) {ps.close();}
            } catch (SQLException ex) {
                log.error("Error al cerrar los recursos de la base de datos"+ ex.getErrorCode() + " " + ex.getSQLState() + " " +ex.getMessage(),ex);
            }
        }
        return respuesta;
    }
    
    public ValidacionRetramitacionCambioProVO getDatosEntradaRegistroValidaRetramitarCambioProc(ValidacionRetramitacionCambioProVO validacionRetramitacionCambPro, Connection con) {
        log.info("getDatosEntradaRegistroValidaRetramitarCambioProc:INI");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        try {
            query = " SELECT RES_DEP,RES_UOR,RES_EJE,RES_NUM,RES_TIP " +
                    " ,PROCEDIMIENTO,COUNT(RED_DOC_ID) AS TOTAL_DOCU_DOKUSI,COUNT(RED_TIPODOC_ID) AS TOTAL_DOCU_CATALOGADOS " +
                    " FROM  " +
                    " R_RES " +
                    " LEFT JOIN R_RED ON RED_DEP=RES_DEP AND RED_UOR=RES_UOR  AND RED_EJE=RES_EJE AND RED_NUM=RES_NUM and res_tip=RED_TIP AND RED_IDDOC_GESTOR IS NOT NULL " +
                    " WHERE RES_DEP=? " +
                    " AND RES_UOR=? " +
                    " AND RES_EJE=? " +
                    " AND RES_NUM=? " +
                    " AND RES_TIP=?  " +
                    " Group By RES_DEP, RES_UOR, RES_EJE, RES_NUM, RES_TIP, " +
                    " PROCEDIMIENTO";

            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, validacionRetramitacionCambPro.getCodDepartamento());
            ps.setInt(contbd++, validacionRetramitacionCambPro.getCodUnidadOrganica());
            ps.setInt(contbd++, validacionRetramitacionCambPro.getEjercicioRegistro());
            ps.setInt(contbd++, validacionRetramitacionCambPro.getNumeroRegistro());
            ps.setString(contbd++, validacionRetramitacionCambPro.getCodTipoRegistro());
            log.info("Query= " + query);
            log.info("Param= " + validacionRetramitacionCambPro.getCodDepartamento()
                    + " " + validacionRetramitacionCambPro.getCodUnidadOrganica()
                    + " " + validacionRetramitacionCambPro.getEjercicioRegistro()
                    + " " + validacionRetramitacionCambPro.getNumeroRegistro()
                    + " " + validacionRetramitacionCambPro.getCodTipoRegistro()
            );
            rs = ps.executeQuery();
            while (rs.next()) {
                validacionRetramitacionCambPro.setCodProcedimientoAnterior(rs.getString("PROCEDIMIENTO"));
                validacionRetramitacionCambPro.setTieneDocumentosEnDokusi(rs.getInt("TOTAL_DOCU_DOKUSI")>0);
                validacionRetramitacionCambPro.setTieneDocumentosCatalogados(rs.getInt("TOTAL_DOCU_CATALOGADOS")>0);
                String codProcedimientoAnterior = (validacionRetramitacionCambPro.getCodProcedimientoAnterior()!=null && !validacionRetramitacionCambPro.getCodProcedimientoAnterior().isEmpty() ? validacionRetramitacionCambPro.getCodProcedimientoAnterior() : "");
                String codProcedimiento = (validacionRetramitacionCambPro.getCodProcedimiento() != null && !validacionRetramitacionCambPro.getCodProcedimiento().isEmpty() ? validacionRetramitacionCambPro.getCodProcedimiento() : "");
                if(!codProcedimientoAnterior.equalsIgnoreCase(codProcedimiento))
                    validacionRetramitacionCambPro.setHayCambioProcedimiento(Boolean.TRUE);
            }
        } catch (SQLException ex) {
            log.error("Error al validar retramitacion por cambio de procedimiento " + ex.getErrorCode() + " " + ex.getSQLState() + " " +ex.getMessage(),ex);
            validacionRetramitacionCambPro.setErrorEnlaPeticion(Boolean.TRUE);
            validacionRetramitacionCambPro.setDetalleErrorEnlaPeticion("Error al validar retramitacion por cambio de procedimiento " + ex.getErrorCode() + " " + ex.getSQLState() + " " +ex.getMessage());
        } finally {
            try {
                if (rs != null) {rs.close();}
                if (ps != null) {ps.close();}
            } catch (SQLException ex) {
                log.error("Error al cerrar los recursos de la base de datos"+ ex.getErrorCode() + " " + ex.getSQLState() + " " +ex.getMessage(),ex);
            }
        }
        return validacionRetramitacionCambPro;
    }
}
