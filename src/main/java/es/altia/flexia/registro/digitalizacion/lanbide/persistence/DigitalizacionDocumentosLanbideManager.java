package es.altia.flexia.registro.digitalizacion.lanbide.persistence;

import es.altia.agora.business.escritorio.persistence.manual.UsuarioDAO;
import es.altia.agora.business.registro.HistoricoMovimientoValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.registro.persistence.HistoricoMovimientoManager;
import es.altia.agora.business.registro.persistence.manual.AnotacionRegistroDAO;
import es.altia.agora.business.sge.DocumentoAnotacionRegistroVO;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoGestor;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.HistoricoAnotacionHelper;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide_dokusi.exception.ErrorLan6ExcepcionBean;
import es.altia.flexia.integracion.moduloexterno.melanbide_dokusi.exception.GestionarErroresDokusi;
import es.altia.flexia.registro.digitalizacion.lanbide.persistence.manual.DigitalizacionDocumentosLanbideDAO;
import es.altia.flexia.registro.digitalizacion.lanbide.util.GestionAdaptadoresLan6Config;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.*;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.beans.Lan6Documento;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.servicios.Lan6DokusiServicios;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6ErrorBean;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6UtilExcepcion;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DigitalizacionDocumentosLanbideManager {
    private static DigitalizacionDocumentosLanbideManager instance = null;
    private static Logger log = Logger.getLogger(DigitalizacionDocumentosLanbideManager.class.getName());

    private static final GestionAdaptadoresLan6Config gestionAdaptadoresLan6Config = new GestionAdaptadoresLan6Config();

    public DigitalizacionDocumentosLanbideManager() {
    }
    
    public static DigitalizacionDocumentosLanbideManager getInstance() {
        // Necesitamos sincronización aquí para serializar (no multithread)
        // las invocaciones a este metodo
        synchronized(DigitalizacionDocumentosLanbideManager.class) {
            if (instance == null) {
              instance = new DigitalizacionDocumentosLanbideManager();
            }      
        }
        return instance;
    }
    
    public String obtenerUsuario(int codUsu, String[] params) throws TechnicalException{
        UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
        String auditUser = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            String[] nombreNifUsuario = usuarioDAO.getNombreNifUsuario(codUsu, params);
            String loginUsuario = usuarioDAO.getLoginUsuario(codUsu, con);
            
            auditUser = nombreNifUsuario[1] + "/" + nombreNifUsuario[0] + "/" + loginUsuario;
        } catch (BDException bde) {
            log.error("Error al obtener una conexión a la BBDD",bde);
            throw new TechnicalException("Error obtenerUsuario(): " + bde.getMessage());
        } catch (TechnicalException te) {
            log.error("Error al obtener datos de usuario",te);
            throw new TechnicalException("Error obtenerUsuario(): " + te.getMessage());
        } finally {
            try {
                if(con!=null) con.close();
            } catch (SQLException ex){
                log.error("Error al cerrar la conexión a la BBDD",ex);
            }
        }
        
        return auditUser;
    }
	public boolean insertarTodosLosDocumentoRegistro(List<DocumentoGestor> listaDocumentos, String[] params) throws TechnicalException {
		AdaptadorSQLBD adapt = null;
		Connection con = null;
		boolean exitoEnLaInsercion;
		try {
			adapt = new AdaptadorSQLBD(params);
			con = adapt.getConnection();

			adapt.inicioTransaccion(con);
			ArrayList<Documento> documento = new ArrayList<Documento>();

			for (int i = 0; i < listaDocumentos.size(); i++) {
				documento.add(listaDocumentos.get(i));
				AnotacionRegistroDAO.getInstance().insertarDocsRegistroSinBinario(con, documento);
				documento.clear();
			}
			exitoEnLaInsercion=true;
			adapt.finTransaccion(con);
		} catch (BDException bde) {
			log.error("Error al obtener una conexión a la BBDD", bde);
			exitoEnLaInsercion = false;
			try {
				adapt.rollBack(con);
			} catch (BDException ex) {
				log.error("Ha ocurrido un error haciendo Rollback de la transacción", ex);
				exitoEnLaInsercion = false;
			}
			throw new TechnicalException("Error al obtener una conexión a la BBDD");
		} catch (AnotacionRegistroException are) {
			log.error("Error al insertar el documento en R_RED", are);
			try {
				adapt.rollBack(con);
			} catch (BDException ex) {
				log.error("Ha ocurrido un error haciendo Rollback de la transacción", ex);
				exitoEnLaInsercion = false;
			}
			throw new TechnicalException("Error al insertar el documento en R_RED");
		} catch (Exception ex) {
			log.error("Error al insertar el documento en MeLanbide", ex);
			try {
				adapt.rollBack(con);
			} catch (BDException bdex) {
				log.error("Ha ocurrido un error haciendo Rollback de la transacción", bdex);
				exitoEnLaInsercion = false;
			}
			throw new TechnicalException("Error al insertar el documento en MeLanbide");
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (adapt != null) {
					adapt = null;
				}
			} catch (SQLException ex) {
				log.error("Ha ocurrido un error al cerrar la conexión a la BBDD", ex);
			}
			
		}
		return exitoEnLaInsercion;
	}

	public void insertarHistoricoDocumentoRegistro(List<DocumentoGestor> listaDocumentos, String[] params, int idUsuario) throws TechnicalException {
		HistoricoMovimientoValueObject hvo = null;
		AdaptadorSQLBD adapt = null;
		Connection con = null;
		try {
			log.debug("AÑADIENDO DOCUMENTO A HISTÓRICO");
			adapt = new AdaptadorSQLBD(params);
			con = adapt.getConnection();
			for (int i = 0; i < listaDocumentos.size(); i++) {
				hvo = new HistoricoMovimientoValueObject();
				hvo.setCodigoUsuario(idUsuario);
				hvo.setTipoEntidad(ConstantesDatos.HIST_REGISTRO_DOCUMENTO);
				String claveAnotacion = (listaDocumentos.get(i).getCodigoDepartamento()) + "/"
						+ (listaDocumentos.get(i).getCodigoUnidadOrganica()) + "/"
						+ (listaDocumentos.get(i).getTipoRegistro()) + "/"
						+ (listaDocumentos.get(i).getEjercicioAnotacion()) + "/"
						+ (listaDocumentos.get(i).getNumeroRegistro());
				hvo.setCodigoEntidad(claveAnotacion);
				hvo.setTipoMovimiento(ConstantesDatos.HIST_DOC_DIGIT);
				hvo.setDetallesMovimiento(
						HistoricoAnotacionHelper.crearXMLDocumento(listaDocumentos));
			}
			HistoricoMovimientoManager.getInstance().insertarMovimientoHistorico(hvo, con, params);
			log.debug("SE AÑADIÓ DOCUMENTO A HISTÓRICO");
			
		} catch (BDException ex) {
			log.error("Error al obtener una conexión a la BBDD",ex);
		} finally {
			try {
                if(con!=null) con.close();
                if(adapt!=null) adapt = null;
            } catch (SQLException ex) {
                log.error("Ha ocurrido un error al cerrar la conexión a la BBDD",ex);
            }
		}
	}
    public boolean insertarDocumentoRegistro(DocumentoGestor docGestor, String[] params) throws TechnicalException{
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        boolean exitoEnLaInsercion=true;
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            adapt.inicioTransaccion(con);

            ArrayList<Documento> documento = new ArrayList<Documento>();
            documento.add(docGestor);
            AnotacionRegistroDAO.getInstance().insertarDocsRegistroSinBinario(con, documento);
			
            adapt.finTransaccion(con);
        } catch (BDException bde){
            log.error("Error al obtener una conexión a la BBDD",bde);
            try {
                adapt.rollBack(con);
            } catch (BDException ex) {
                log.error("Ha ocurrido un error haciendo Rollback de la transacción",ex);
            }
            throw new TechnicalException("Error al obtener una conexión a la BBDD");
        } catch (AnotacionRegistroException are) {
            log.error("Error al insertar el documento en R_RED",are);
            try {
                adapt.rollBack(con);
            } catch (BDException ex) {
                log.error("Ha ocurrido un error haciendo Rollback de la transacción",ex);
            }
            throw new TechnicalException("Error al insertar el documento en R_RED");
        } catch (Exception ex) {
            log.error("Error al insertar el documento en MeLanbide",ex);
            try {
                adapt.rollBack(con);
            } catch (BDException bdex) {
                log.error("Ha ocurrido un error haciendo Rollback de la transacción",bdex);
            }
            throw new TechnicalException("Error al insertar el documento en MeLanbide");
        } finally {
            try {
                if(con!=null) con.close();
                if(adapt!=null) adapt = null;
            } catch (SQLException ex) {
                log.error("Ha ocurrido un error al cerrar la conexión a la BBDD",ex);
            }
        }
		return exitoEnLaInsercion;
    }
    
    public List<TipoDocumentalCatalogacionVO> getTipDocCatalogacion(String[] params) throws SQLException, BDException{
        log.debug("DigitalizacionDocumentosLanbideManager : getTipDocCatalogacion()");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        List<TipoDocumentalCatalogacionVO> listadoTipDoc = null;
        DigitalizacionDocumentosLanbideDAO digitalizacionDAO = DigitalizacionDocumentosLanbideDAO.getInstance();
        
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            listadoTipDoc = digitalizacionDAO.getTipDocCatalogacion(con);
        } catch (BDException bde) {
            log.error("Error al obtener una conexión a la base de datos",bde);
            bde.printStackTrace();
            throw bde;
        } catch (SQLException sqle) {
            log.error("Error: " + sqle.getMessage(),sqle);
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if(con!=null) con.close();
            } catch (SQLException ex){
                log.error("Error al cerrar la conexión con la base de datos",ex);
            }
        }
        
        return listadoTipDoc;
    }
    
    public List<GrupoTipDocVO> getGruposTipDoc(String[] params) throws SQLException, BDException{
        log.debug("DigitalizacionDocumentosLanbideManager : getGruposTipDoc()");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        List<GrupoTipDocVO> listadoGruposTipDoc = null;
        DigitalizacionDocumentosLanbideDAO digitalizacionDAO = DigitalizacionDocumentosLanbideDAO.getInstance();
        
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            listadoGruposTipDoc = digitalizacionDAO.getGruposTipDoc(con);
        } catch (BDException bde) {
            log.error("Error al obtener una conexión a la base de datos",bde);
            bde.printStackTrace();
            throw bde;
        } catch (SQLException sqle) {
            log.error("Error: " + sqle.getMessage(),sqle);
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if(con!=null) con.close();
            } catch (SQLException ex){
                log.error("Error al cerrar la conexión con la base de datos",ex);
            }
        }
        
        return listadoGruposTipDoc;
    }
    
    public List<MetadatoCatalogacionVO> getMetadatosCatalogByTipDoc(int tipDoc, String[] params) throws SQLException, BDException{
        log.debug("DigitalizacionDocumentosLanbideManager : getMetadatoCatalogByTipDoc()");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        List<MetadatoCatalogacionVO> listadoMetad = null;
        DigitalizacionDocumentosLanbideDAO digitalizacionDAO = DigitalizacionDocumentosLanbideDAO.getInstance();
        
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            listadoMetad = digitalizacionDAO.getMetadatoCatalogByTipDoc(tipDoc, con);
        } catch (BDException bde) {
            log.error("Error al obtener una conexión a la base de datos",bde);
            bde.printStackTrace();
            throw bde;
        } catch (SQLException sqle) {
            log.error("Error: " + sqle.getMessage(),sqle);
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if(con!=null) con.close();
            } catch (SQLException ex){
                log.error("Error al cerrar la conexión con la base de datos",ex);
            }
        }
        
        return listadoMetad;
    }
    
    public boolean grabarCatalogacionDocumento(DocumentoCatalogacionVO docCatalogar, List<String> listaMetadatos,String tipoDocumental, String[] params) throws TechnicalException, BDException, SQLException, Lan6Excepcion{
        log.info("DigitalizacionDocumentosLanbideManager : grabarCatalogacionDocumento()");
        DigitalizacionDocumentosLanbideDAO digitalizacionDAO = DigitalizacionDocumentosLanbideDAO.getInstance();
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        int inserciones = 0;
        boolean exito = false;
        
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            
            // Borramos la catalogación anterior del documento si tiene
            List<DocumentoCatalogacionVO> metadatosDoc = digitalizacionDAO.recuperarDocCatalogacion(docCatalogar, con);
            if(metadatosDoc.size() > 0){
                digitalizacionDAO.borrarDocCatalogacion(docCatalogar, con);
            }
            if(listaMetadatos.size()!=0){
                for(String metadato : listaMetadatos){
                    String[] partesMetadato = metadato.split(";");
                    // Seteamos los valores de catalogacion para cada metadato
                    TipoDocumentalCatalogacionVO tipo = new TipoDocumentalCatalogacionVO();
                    MetadatoCatalogacionVO mdVO = new MetadatoCatalogacionVO();
                
                    tipo.setIdentificador(Integer.parseInt(partesMetadato[0].trim()));
                    mdVO.setIdMetadato(partesMetadato[1].trim());
                    mdVO.setValorMetadato(partesMetadato[2].trim());
                    docCatalogar.setTipoDocumental(tipo);
                    docCatalogar.setMetadato(mdVO);
                
                    inserciones += digitalizacionDAO.grabarMetadatoDocumento(docCatalogar, con);
                }
                if(inserciones == listaMetadatos.size()){
                    exito = true;
                } else throw new TechnicalException("Ha ocurrido un error en la operación");
           
            } else if (tipoDocumental!=null && !tipoDocumental.equals("")){
                TipoDocumentalCatalogacionVO tipo = new TipoDocumentalCatalogacionVO();
               // MetadatoCatalogacionVO mdVO = new MetadatoCatalogacionVO();
                
                tipo.setIdentificador(Integer.parseInt(tipoDocumental));
                docCatalogar.setTipoDocumental(tipo);
                
                inserciones = digitalizacionDAO.grabarMetadatoDocumento(docCatalogar, con);
                if(inserciones == 1){
                    // Transaccion correcta
                   
                    exito = true;
                } else {
                    throw new TechnicalException("Ha ocurrido un error en la operación");
                }
                
            }
            if(exito){
                    retramitarDocumentoCatalogado(docCatalogar, con);
                    // Transaccion correcta
                    adapt.finTransaccion(con);
            }
        } catch (BDException bde){
            log.error("Error al obtener una conexión a la BBDD",bde);
            try {
                adapt.rollBack(con);
            } catch (BDException ex) {
                log.error("Ha ocurrido un error haciendo Rollback de la transacción",ex);
            }
            throw new BDException("Error al obtener una conexión a la BBDD");
        } catch (SQLException te) {
            log.error("Error al obtener una conexión a la BBDD",te);
            try {
                adapt.rollBack(con);
            } catch (BDException ex) {
                log.error("Ha ocurrido un error haciendo Rollback de la transacción",ex);
            }
            throw new SQLException("Error: " + te.getMessage());
        } catch(Lan6Excepcion le){
            log.error("Error: "+ le.getMessage(),le);
            try {
                log.debug("rollback");
                adapt.rollBack(con);
            } catch (BDException ex) {
                log.error("Ha ocurrido un error haciendo Rollback de la transacción",ex);
            }finally{
                throw new Lan6Excepcion("Error","Error: " + le.getMessage());
            }
        } catch (Exception ex) {
            log.error("Error al grabar la catalogación del documento",ex);
            try {
                adapt.rollBack(con);
            } catch (BDException bdex) {
                log.error("Ha ocurrido un error haciendo Rollback de la transacción",ex);
            }
            throw new TechnicalException("Error al grabar la catalogación del documento");
        } finally {
            try {
                 adapt.finTransaccion(con);
                if(con!=null) con.close();
                if(adapt!=null) adapt = null;
            } catch (SQLException ex) {
                log.error("Ha ocurrido un error al cerrar la conexión a la BBDD",ex);
            }
        }
        return exito;
    }
    
    public List<DocumentoCatalogacionVO> recuperarDatosCatalogacionDoc(DocumentoCatalogacionVO docCatalogar, String[] params) throws TechnicalException, BDException, SQLException{
        log.info("DigitalizacionDocumentosLanbideManager : comprobarDocCatalogado()");
        DigitalizacionDocumentosLanbideDAO digitalizacionDAO = DigitalizacionDocumentosLanbideDAO.getInstance();
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        List<DocumentoCatalogacionVO> listado = null;
        
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
           listado = digitalizacionDAO.recuperarDocCatalogacion(docCatalogar, con);
           
        } catch (BDException bde){
            log.error("Error al obtener una conexión a la BBDD",bde);
            throw new BDException("Error al obtener una conexión a la BBDD");
        } catch (SQLException te) {
            log.error("Error al obtener una conexión a la BBDD",te);
            throw new SQLException("Error: " + te.getMessage());
        } catch (Exception ex) {
            log.error("Error al recuperar la catalogación del documento",ex);
            throw new TechnicalException("Error al comprobar la catalogación del documento");
        } finally {
            try {
                if(con!=null) con.close();
                if(adapt!=null) adapt = null;
            } catch (SQLException ex) {
                log.error("Ha ocurrido un error al cerrar la conexión a la BBDD",ex);
            }
        }
        return listado;
    }
    
    
    public List<TipoDocumentalCatalogacionVO> getTipDocCatalogacionProcedimiento(String codProcedimiento, String[] params) throws SQLException, BDException{
        log.debug("DigitalizacionDocumentosLanbideManager : getTipDocCatalogacionProcedimiento()");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        List<TipoDocumentalCatalogacionVO> listadoTipDoc = null;
        DigitalizacionDocumentosLanbideDAO digitalizacionDAO = DigitalizacionDocumentosLanbideDAO.getInstance();
        
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            listadoTipDoc = digitalizacionDAO.getTipDocCatalogacionProcedimiento(codProcedimiento, con);
        } catch (BDException bde) {
            log.error("Error al obtener una conexión a la base de datos",bde);
            bde.printStackTrace();
            throw bde;
        } catch (SQLException sqle) {
            log.error("Error: " + sqle.getMessage(),sqle);
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if(con!=null) con.close();
            } catch (SQLException ex){
                log.error("Error al cerrar la conexión con la base de datos",ex);
            }
        }
        
        return listadoTipDoc;
    }
    
    public List<TipoDocumentalCatalogacionVO> getTipDocCatalogacionProcedimiento(String ejercicio, String nRegistro, String[] params) throws SQLException, BDException{
        log.debug("DigitalizacionDocumentosLanbideManager : getTipDocCatalogacionProcedimiento()");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        List<TipoDocumentalCatalogacionVO> listadoTipDoc = null;
        DigitalizacionDocumentosLanbideDAO digitalizacionDAO = DigitalizacionDocumentosLanbideDAO.getInstance();
        
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            listadoTipDoc = digitalizacionDAO.getTipDocCatalogacionProcedimiento(ejercicio, nRegistro, con);
        } catch (BDException bde) {
            log.error("Error al obtener una conexión a la base de datos",bde);
            bde.printStackTrace();
            throw bde;
        } catch (SQLException sqle) {
            log.error("Error: " + sqle.getMessage(),sqle);
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if(con!=null) con.close();
            } catch (SQLException ex){
                log.error("Error al cerrar la conexión con la base de datos",ex);
            }
        }
        
        return listadoTipDoc;
    }
    
    public ArrayList getOidsDocumentosRegistro(int ejercicio, long numeroRegistro, String[] params){
        log.debug("DigitalizacionDocumentosLanbideManager.getOidsDocumentosRegistro(): INI");
        ArrayList listadoIdDocumentos = new ArrayList();
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        DigitalizacionDocumentosLanbideDAO digitalizacionDAO = DigitalizacionDocumentosLanbideDAO.getInstance();
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            listadoIdDocumentos = digitalizacionDAO.getOidsDocumentosRegistro(ejercicio, numeroRegistro, con);
         }catch(BDException bde){
            log.error("Error al obtener una conexión a la BBDD: "+bde.getMensaje(),bde); 
        }catch(Exception ex){
            log.error("Ha ocurrido un error al recuperar los Oids de documentos "+ ex.getMessage(),ex);
        }finally{
             try {
                if(con!=null) con.close();
                if(adapt!=null) adapt = null;
            } catch (SQLException ex) {
                log.error("Ha ocurrido un error al cerrar la conexión a la BBDD",ex);
            }
        }
        return listadoIdDocumentos;
    }    
    
    public List<DocumentoAnotacionRegistroVO> getDocumentosRegistro(RegistroValueObject registro, String[] params){
        log.info("DigitalizacionDocumentosLanbideManager.getDocumentosRegistro(): INI");
        List<DocumentoAnotacionRegistroVO> listaDocumentos = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        DigitalizacionDocumentosLanbideDAO digitalizacionDAO = DigitalizacionDocumentosLanbideDAO.getInstance();
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
                        
            listaDocumentos = digitalizacionDAO.getDocumentosRegistro(registro, con);
         }catch(BDException bde){
            log.error("Error al obtener una conexión a la BBDD: "+bde.getMensaje(),bde); 
        }catch(Exception ex){
            log.error("Ha ocurrido un error al recuperar los Oids de documentos "+ ex.getMessage(),ex);
        }finally{
             try {
                if(con!=null) con.close();
                if(adapt!=null) adapt = null;
            } catch (SQLException ex) {
                log.error("Ha ocurrido un error al cerrar la conexión a la BBDD",ex);
            }
        }
        return listaDocumentos;
    }    
    
    public List<DocumentoCatalogacionVO> getDocumentosRegistroCompleto(int departamento, int uor, int ejercicio, long numero, String tipo, String[] params){
        log.debug("DigitalizacionDocumentosLanbideManager.getDocumentosRegistroCompleto(): INI");
        List<DocumentoCatalogacionVO> listaDocumentos = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        DigitalizacionDocumentosLanbideDAO digitalizacionDAO = DigitalizacionDocumentosLanbideDAO.getInstance();
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            // Recuperamos los documentos del registro
            listaDocumentos = digitalizacionDAO.getDocumentosRegistroCompleto(departamento, uor, ejercicio, numero, tipo, con);
         }catch(BDException bde){
            log.error("Error al obtener una conexión a la BBDD: "+bde.getMensaje(),bde); 
        }catch(Exception ex){
            log.error("Ha ocurrido un error al recuperar los Oids de documentos "+ ex.getMessage(),ex);
        }finally{
             try {
                if(con!=null) con.close();
                if(adapt!=null) adapt = null;
            } catch (SQLException ex) {
                log.error("Ha ocurrido un error al cerrar la conexión a la BBDD",ex);
            }
        }
        return listaDocumentos;
    }    
    
    public String getOidDocumentoCatalogado(DocumentoCatalogacionVO docCatalogado,  Connection con){
        log.debug("DigitalizacionDocumentosLanbideManager.getOidDocumentoCatalogado(): INI");
        String identificadorDocumento = null;  
        try{
          
            DigitalizacionDocumentosLanbideDAO digitalizacionDAO = DigitalizacionDocumentosLanbideDAO.getInstance();
            identificadorDocumento = digitalizacionDAO.getOidDocumentoCatalogado(docCatalogado, con);
         
        }catch(Exception ex){
            log.error("Ha ocurrido un error al recuperar el oid del documento "+ ex.getMessage(),ex);
        }
        return identificadorDocumento;
        
    }
    
     public String getTipoDocumentalDokusi(DocumentoCatalogacionVO docCatalogado, Connection con){
        log.debug("DigitalizacionDocumentosLanbideManager.getTipoDocumentalDokusi(): INI");
        String tipoDocumental = null;
       
        DigitalizacionDocumentosLanbideDAO digitalizacionDAO = DigitalizacionDocumentosLanbideDAO.getInstance();
        try{
           
            
            tipoDocumental = digitalizacionDAO.getTipoDocumentalDokusi(docCatalogado, con);
         
        }catch(Exception ex){
            log.error("Ha ocurrido un error al recuperar el tipo documental "+ ex.getMessage(),ex);
        }
        return tipoDocumental;
        
    }    
     
     public String getTipoDocumentalDokusi(DocumentoCatalogacionVO docCatalogado, String[] params){
        log.debug("DigitalizacionDocumentosLanbideManager.getTipoDocumentalDokusi(): INI");
        String tipoDocumental = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
       
        DigitalizacionDocumentosLanbideDAO digitalizacionDAO = DigitalizacionDocumentosLanbideDAO.getInstance();
        try{
           adapt = new AdaptadorSQLBD(params);
           con = adapt.getConnection();
            
            tipoDocumental = digitalizacionDAO.getTipoDocumentalDokusi(docCatalogado, con);
         
        }catch(Exception ex){
            log.error("Ha ocurrido un error al recuperar el tipo documental "+ ex.getMessage(),ex);
        }finally{
            try {
                if(con!=null) con.close();
                if(adapt!=null) adapt = null;
            } catch (SQLException ex) {
                log.error("Ha ocurrido un error al cerrar la conexión a la BBDD",ex);
            }
        }
        return tipoDocumental;
        
    }    
     
    public Map getListadoMetadatosDocumentoDokusi(DocumentoCatalogacionVO docCatalogado,  Connection con){
        log.debug("DigitalizacionDocumentosLanbideManager.getListadoMetadatosDocumentosDokusi(): INI");
        HashMap<String, String> listadoMetadatos = new HashMap<String, String>();
        
        DigitalizacionDocumentosLanbideDAO digitalizacionDAO = DigitalizacionDocumentosLanbideDAO.getInstance();
        try{
            
            
            listadoMetadatos = (HashMap) digitalizacionDAO.getListadoMetadatosDocumentoDokusi(docCatalogado, con);
         
        }catch(Exception ex){
            log.error("Ha ocurrido un error al recuperar la lista de metadatos "+ ex.getMessage(),ex);
        }
        return listadoMetadatos;
        
    }

    // cambia un documento de contexto de Registro a contexto de Tramitación
    public void tramitarDocumento(Integer codOrganizacion, Integer ejeRegistro, Long num, String oid, String codProcedimiento, boolean esDocCompulsado, String BDconnectionParams[]) throws Lan6UtilExcepcion, Lan6Excepcion {
        log.info("DigitalizacionDocumentosLanbideManager.tramitarDocumentacion(): INICIO");

        //en los atributos adicionales el procedimiento será el resultante de la conversión FLEXIA - DOKUSI
        String procedimientoDokusi = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea(codProcedimiento); //melanbideDokusiPlateaProService.getCodigoProcedimientoPlatea(codProcedimiento,BDconnectionParams); // convierteProcedimiento(codProcedimiento);
        // Para tramitar un documento el servicio se crea...
        //String idProcedimiento = esDocCompulsado ? "LANDIS" : codProcedimiento;
        /*if(esDocCompulsado){
            // ... partir de PROCEDIMIENTO_ID_LANDIS si el documento esta compulsado
            idProcedimiento = melanbideDokusiPlateaProService.getCodigoProcedimientoPlatea("LANDIS",BDconnectionParams);//gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("PROCEDIMIENTO_ID_"+"LANDIS");
        } else {
            // ...a partir de PROCEDIMIENTO_ID_<procedimiento_del_registro> si el documento es no compulsado
            idProcedimiento = melanbideDokusiPlateaProService.getCodigoProcedimientoPlatea(codProcedimiento,BDconnectionParams);//gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("PROCEDIMIENTO_ID_"+procedimientoDokusi);
        }
        */
        log.info("Es doc compulsado? " + esDocCompulsado + " - idProcedimiento servicio PLATEA: " + procedimientoDokusi);
        if (!procedimientoDokusi.equals("")) {
            try {
                log.info("tramitarDocumento => Instanciamos Lan6DokusiServicios con: " + (esDocCompulsado ? "LANDIS" : codProcedimiento));
                Lan6DokusiServicios servicios = new Lan6DokusiServicios(esDocCompulsado ? "LANDIS" : codProcedimiento);

                Lan6Documento lan6Documento = new Lan6Documento();

                lan6Documento.setIdDocumento(oid);

                HashMap<String, String> atributosAdicionales = new HashMap<String, String>();

                //Serie Documental
                String serieDocumental = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("MD_SERIE_" + procedimientoDokusi);
                log.info("serieDocumental " + serieDocumental);
                atributosAdicionales.put(Lan6Constantes.METADATO_SERIE, serieDocumental);

                //Archivo Digital
                String idProcArchivoDigital = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ID_PROC_ARCHIVO_DIGITAL_" + procedimientoDokusi);
                atributosAdicionales.put(Lan6Constantes.METADATO_PROCEDIMIENTO, idProcArchivoDigital);
                log.info("idProcArchivoDigital " + idProcArchivoDigital);

                // metadatos ACL
                String nuevaACL = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ACL_" + procedimientoDokusi);
                if (nuevaACL != null && !nuevaACL.equals("")) {
                    atributosAdicionales.put(Lan6Constantes.METADATO_ACL, nuevaACL);
                }
                lan6Documento.setAtributosAdicionales(atributosAdicionales);
                log.debug("nuevaACL : " + nuevaACL);
                log.debug("Objeto Lan6Documento que vamos a tramitar: " + lan6Documento);

                log.info("Llamada a tramitarDocumento de Adaptadores. Numero de registro = " + ejeRegistro + "/" + num
                        + "; Oid de documento = " + oid);
                servicios.tramitarDocumento(lan6Documento);
                log.info("Finaliza la llamada a tramitarDocumento de Adaptadores sin provocar ninguna Exception");
            } catch (Lan6Excepcion le) {
                log.error("Error Lan6Excepcion en la llamada a tramitarDocumento: " + le.getMensajeExcepcion(), le);
                try {
                    // Vamos Registrar el error usando el jar de Plugin de Documentos Dokusi
                    log.error("Vamos a registrar el error en BD - al tramitarDocumento Numero de registro = " + ejeRegistro + "/" + num
                            + "; Oid de documento = " + oid);
                    String causaExcepcion = (le.getCause() != null ? (le.getCause().getMessage() != null ? le.getCause().getMessage() + " - " + le.getCause().toString() : le.getCause().toString()) : "");
                    String mensajeExcepcion = (le.getMessages().size() > 0 ? le.getMessages().get(0).toString() : "Excepcion capturada sin Mensaje de error");
                    String trazaError = le.getTrazaExcepcion(); //ExceptionUtils.getStackTrace(ex);

                    Lan6ErrorBean errorBean = new Lan6ErrorBean(causaExcepcion, mensajeExcepcion,
                            trazaError, "FLEXIA_REGISTRO",
                            "COMPUL_TRAMITAR_DOCUMENTO", "Error al llamar a metodo tramitarDocumento() de los servicios de adaptadores de platea.", 2); // Tipollogia 2 Fucional
                    ErrorLan6ExcepcionBean errorLan6Bean = new ErrorLan6ExcepcionBean(errorBean, le);
                    // Creamos el objeto docGestor para pasar los datos del procediieto y el ID del registro tratado.
                    DocumentoGestor docGestor = new DocumentoGestor();
                    docGestor.setCodProcedimiento(codProcedimiento);
                    docGestor.setEjercicioAnotacion(ejeRegistro);
                    docGestor.setNumeroRegistro(num);
                    docGestor.setIdDocGestor(oid);
                    int idError = GestionarErroresDokusi.grabarError(errorLan6Bean, docGestor, oid, "", this.getClass().getName() + ".tramitarDocumento()");
                    log.error("Error Registrado en BD correctamente: " + idError);
                } catch (Exception ex1) {
                    log.error("LLamada servicios tramitarDocumento - Error al registrar un error en BBDD.: " + ex1.getMessage(), ex1);
                }
                throw new Lan6Excepcion("Error durante el cambio de contexto de documentos", "error");
            } catch (Exception ex) {
                log.error("error Exception al tramitarDocumento : ", ex);
                try {
                    // Vamos Registrar el error usando el jar de Plugin de Documentos Dokusi
                    log.error("Vamos a registrar el error en BD - al tramitarDocumento Numero de registro = " + ejeRegistro + "/" + num
                            + "; Oid de documento = " + oid);
                    String causaExcepcion = (ex.getCause() != null ? (ex.getCause().getMessage() != null ? ex.getCause().getMessage() + " - " + ex.getCause().toString() : ex.getCause().toString()) : "");
                    String mensajeExcepcion = (ex.getMessage() != null ? ex.getMessage() : "Excepcion capturada sin Mensaje de error");
                    String trazaError = ExceptionUtils.getStackTrace(ex);

                    Lan6ErrorBean errorBean = new Lan6ErrorBean(causaExcepcion, mensajeExcepcion,
                            trazaError, "FLEXIA_REGISTRO",
                            "COMPUL_TRAMITAR_DOCUMENTO", "Error al llamar a metodo tramitarDocumento() de los servicios de adaptadores de platea.", 2); // Tipollogia 2 Fucional
                    ErrorLan6ExcepcionBean errorLan6Bean = new ErrorLan6ExcepcionBean(errorBean, ex);
                    // Creamos el objeto docGestor para pasar los datos del procediieto y el ID del registro tratado.
                    DocumentoGestor docGestor = new DocumentoGestor();
                    docGestor.setCodProcedimiento(codProcedimiento);
                    docGestor.setEjercicioAnotacion(ejeRegistro);
                    docGestor.setNumeroRegistro(num);
                    docGestor.setIdDocGestor(oid);
                    int idError = GestionarErroresDokusi.grabarError(errorLan6Bean, docGestor, oid, "", this.getClass().getName() + ".tramitarDocumento()");
                    log.error("Error Registrado en BD correctamente: " + idError);
                } catch (Exception ex1) {
                    log.error("LLamada servicios tramitarDocumento - Error al registrar un error en BBDD.: " + ex1.getMessage(), ex1);
                }
                throw new Lan6Excepcion("Error durante el cambio de contexto de documentos", "error");
            }
        }
        log.info("DigitalizacionDocumentosLanbideManager.tramitarDocumentacion (): FIN");

    }

    // actualizamos la información de tipoDocumental, metadatos del documento almacenado
    public void retramitarDocumentoCatalogado(DocumentoCatalogacionVO docCatalogado, Connection con) throws Lan6Excepcion, TechnicalException {
        log.debug("DigitalizacionDocumentosLanbideManager.retramitarDocumentoCatalogado(): INICIO");
        try {
            //se recupera el oid del documento catalogado
            String idDocumento = getOidDocumentoCatalogado(docCatalogado, con);
            docCatalogado.setIdDocGestor(idDocumento);
            boolean esDocumentoMigrado = isDocumentoMigradoxOID(docCatalogado, con);
            if (idDocumento != null && !idDocumento.equals("")) {
                RegistroValueObject regVO = new RegistroValueObject();
                regVO.setAnoReg(docCatalogado.getEjercicio());
                regVO.setNumReg(docCatalogado.getNumeroAnot());
                regVO.setIdentDepart(docCatalogado.getDepartamento());
                regVO.setTipoReg(docCatalogado.getTipoAnot());
                regVO.setUnidadOrgan((int) docCatalogado.getUnidadOrg());
                //se recupera el procedimiento de la anotación
                String codProcedimiento = AnotacionRegistroDAO.getInstance().getCodProcedimientoRegistro(regVO, con);

                //se realiza la conversión de procedimiento Flexia a Dokusi
                String procedimientoDokusi = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea(codProcedimiento); //convierteProcedimiento(codProcedimiento);
                if (procedimientoDokusi != null && !procedimientoDokusi.equalsIgnoreCase("")) {
                    //obtenemos el tipo documental de Dokusi
                    String tipoDocuDokusi = getTipoDocumentalDokusi(docCatalogado, con);

                    //se obtiene el listado de metadatos del documento catalogado
                    HashMap<String, String> listadoMetadatos = (HashMap) getListadoMetadatosDocumentoDokusi(docCatalogado, con);

                    // se realizar la llamada al metodo retramitar de Dokusi para incorporar los metadatos
                    //String idProcedimiento = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties( "PROCEDIMIENTO_ID_"+procedimientoDokusi);
                    //Lan6DokusiServicios servicios = new Lan6DokusiServicios(idProcedimiento);

                    //String idProcedimientoOwnerLandis = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties(ConstantesDigitalizacion.ID_PROC_LANDIS);
                    Lan6DokusiServicios servicios = new Lan6DokusiServicios("LANDIS"); // Se instancia con owner LANDIS que tiene permisos sobre todos procedimientos para poder retramitar.

                    Lan6Documento lan6Documento = new Lan6Documento();

                    lan6Documento.setIdDocumento(idDocumento);
                    lan6Documento.setTipoDocumental(tipoDocuDokusi);
                    // metadatos ACL
                    String nuevaACL = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ACL_" + procedimientoDokusi);
                    if (nuevaACL != null && !nuevaACL.equals("")) {
                        listadoMetadatos.put(Lan6Constantes.METADATO_ACL, nuevaACL);
                    }
                    // metadatos documento
                    if (listadoMetadatos.size() > 0) {
                        lan6Documento.setAtributosAdicionales(listadoMetadatos);
                    }

                    log.info("Llamada a retramitarDocumento de Adaptadores. Numero de registro = " + docCatalogado.getEjercicio() + "/"
                            + docCatalogado.getNumeroAnot() + "; Oid de documento = " + idDocumento);
                    if (esDocumentoMigrado)
                        log.info(" NO Se ejecuta la llamada a retramitarDocumento - Es un documento Migrado.");
                    else
                        servicios.retramitarDocumento(lan6Documento);
                    log.info("Finaliza la llamada a retramitarDocumento de Adaptadores sin provocar ninguna Exception");
                }

            }
        } catch (Lan6Excepcion ex) {
            ArrayList<String> codes = ex.getCodes();
            ArrayList<String> messages = ex.getMessages();
            log.error("Error en la llamada a retramitarDocumento: " + ex.getMensajeExcepcion(), ex);
            ex.printStackTrace();
            throw new Lan6Excepcion("Error", "Error al enviar la catalogación del documento");
        } catch (Exception e) {
            log.error(e.getStackTrace());
            throw new TechnicalException("Error técnico ");
        }

        log.debug("DigitalizacionDocumentosLanbideManager.retramitarDocumentoCatalogado() : FIN");
    }
    
    
    public void retramitarDocumentoInicioExpediente(GeneralValueObject gVO, String params[]) throws Lan6Excepcion, TechnicalException {
        log.debug("DigitalizacionDocumentosLanbideManager.retramitarDocumentoInicioExpediente(): INICIO");

        List<DocumentoCatalogacionVO> listadoDocumentos = new ArrayList<DocumentoCatalogacionVO>();

        try {
            // #320123: Se resupera del VO el atributo que indica si se trata de un registro telematico para en tal caso no retramitar el documento
            Boolean esRegTelematico = (Boolean) gVO.getAtributo("esRegTelematico");

            if (!esRegTelematico) {
                // se recuperan los oids y tipos documentales de los documentos del registro que inicia el expediente
                Integer departamento = Integer.parseInt(gVO.getAtributo("codDepartamento").toString());
                Integer uor = Integer.parseInt(gVO.getAtributo("codUnidadRegistro").toString());
                Integer ejercicioAsiento = Integer.parseInt(gVO.getAtributo("ejercicioAsiento").toString());
                Long numeroAsiento = Long.parseLong(gVO.getAtributo("numeroAsiento").toString());
                String tipo = gVO.getAtributo("tipoAsiento").toString();
                log.debug("Departamento del asiento para el que recupera los documentos: " + departamento);
                log.debug("Uor del asiento para el que recupera los documentos: " + uor);
                log.debug("Ejercio del asiento para el que recupera los documentos: " + ejercicioAsiento);
                log.debug("Numero del asiento para el que recupera los documentos: " + numeroAsiento);
                log.debug("Tipo del asiento para el que recupera los documentos: " + tipo);
                listadoDocumentos = getDocumentosRegistroCompleto(departamento, uor, ejercicioAsiento, numeroAsiento, tipo, params);
                if (listadoDocumentos.size() > 0) {
                    //se realiza la conversión de procedimiento Flexia a Dokusi
                    //String procedimientoDokusi = melanbideDokusiPlateaProService.getCodigoProcedimientoPlatea(gVO.getAtributo("procedimientoAsiento").toString(),params); //convierteProcedimiento(gVO.getAtributo("procedimientoAsiento").toString());

                    //if (!procedimientoDokusi.equals("")) {

                        //String idProcedimiento = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties( "PROCEDIMIENTO_ID_" + procedimientoDokusi);
                        log.debug("Id de procedimiento para el que creamos el servicio: " + gVO.getAtributo("procedimientoAsiento"));
                        Lan6DokusiServicios servicios = new Lan6DokusiServicios((String) gVO.getAtributo("procedimientoAsiento"));
                        for (DocumentoCatalogacionVO documento : listadoDocumentos) {
                            String idDocumento = documento.getIdDocGestor();
                            //obtenemos el tipo documental de Dokusi
                            String tipoDocuDokusi = getTipoDocumentalDokusi(documento, params);

                            Lan6Documento lan6Documento = new Lan6Documento();
                            log.debug("Id del documento que vamos a retramitar: " + idDocumento);
                            lan6Documento.setIdDocumento(idDocumento);

                            // se indica el tipo documental para que no se pierda al no indicar nada
                            lan6Documento.setTipoDocumental(tipoDocuDokusi);
                            log.debug("Tipo documental del documento, se vuelve a indicar al retramitar: " + gVO.getAtributo("numero").toString());

                            //se añade número expedediente iniciado 
                            log.debug("Número de expediente que indicamos al retramitar: " + gVO.getAtributo("numero").toString());
                            lan6Documento.setIdExpediente(gVO.getAtributo("numero").toString());
                            
                            log.info("Llamada a retramitarDocumento de Adaptadores. Numero de registro = " + ejercicioAsiento + "/" 
                                    + numeroAsiento + "; Oid de documento = " + idDocumento);
                            boolean esDocumentoMigrado = false; 
                            if(1==documento.getEsDocMigrado()) esDocumentoMigrado=true;
                            if(esDocumentoMigrado)
                                log.info(" NO Se ejecuta la llamada a retramitarDocumento - Es un documento Migrado.");
                            else
                                servicios.retramitarDocumento(lan6Documento);
                            log.info("Finaliza la llamada a retramitarDocumento de Adaptadores sin provocar ninguna Exception");
                        }
                    //}

                }
            }

        } catch (Lan6Excepcion ex) {
            log.error("Error en la llamada a retramitarDocumento: " + ex.getMensajeExcepcion(),ex);
            throw new TechnicalException("Error en la retramitación del documento");
        } catch (Exception e) {
            log.error(e.getStackTrace());
            throw new TechnicalException("Error técnico en la retramitación del documento ");
        }
        log.debug("DigitalizacionDocumentosLanbideManager.retramitarDocumentoInicioExpediente(): FIN");
    }
    
    public void retramitarDocumentoAdjuntarExpediente(RegistroValueObject registro, String numeroExpediente, String params[]) throws Lan6Excepcion, TechnicalException{
        log.debug("DigitalizacionDocumentosLanbideManager.retramitarDocumentoAdjuntarExpediente(): INICI");
        
        int ejercicioAsiento = registro.getAnoReg();
        long numeroAsiento = registro.getNumReg();
        String procedimientoAsiento = registro.getCodProcedimiento();
        
       try{
          GeneralValueObject gVO = new GeneralValueObject();
          gVO.setAtributo("ejercicioAsiento", ejercicioAsiento);
          gVO.setAtributo("numeroAsiento", numeroAsiento);
          gVO.setAtributo("procedimientoAsiento", procedimientoAsiento);
          gVO.setAtributo("numero", numeroExpediente);
          gVO.setAtributo("codDepartamento",registro.getIdentDepart());
          gVO.setAtributo("codUnidadRegistro",registro.getUnidadOrgan());
          gVO.setAtributo("tipoAsiento",registro.getTipoReg());
          
          // #320123: Se necesita enviar al VO un atributo que indique si se trata de un registro telematico para en tal caso no retramitar el documento
          // usando el servicio de Adaptadores (DigitalizacionDocumentosLanbideManager.retramitarDocumentoInicioExpediente())
          gVO.setAtributo("esRegTelematico", registro.isRegistroTelematico());
          
          retramitarDocumentoInicioExpediente(gVO, params);
           
       }catch(Lan6Excepcion ex){
              log.error("Error en la retramitación del documento: "+ex.getMessage(),ex);
              throw new  TechnicalException("Error en la retramitación del documento");
       }catch(Exception e){
              log.error(e.getStackTrace());
              throw new TechnicalException("Error técnico en la retramitación del documento ");
       }  
       log.debug("DigitalizacionDocumentosLanbideManager.retramitarDocumentoAdjuntarExpediente(): FIN");  
    }
    
    public void retramitarDocumentoCambioProcedimiento(Integer codOrganizacion, Integer ejeRegistro, Long num, String oid, String codProcedimientoNuevo, String[] params, String procOwnerOrAutorizaCambiar) throws Lan6Excepcion{
        log.info("DigitalizacionDocumentosLanbideManager.reTramitarDocumentoCambioProcedimiento(): INICIO");
        //boolean isDocDigitalizadoCatalogado = false; 
       String textoErrorAux="";  
       int idError = 0;
        //en los atributos adicionales el procedimiento será el resultante de la conversión FLEXIA - DOKUSI
        String procedimientoDokusi =  gestionAdaptadoresLan6Config.getCodProcedimientoPlatea(codProcedimientoNuevo);//convierteProcedimiento(codProcedimientoNuevo);
        //String procedimientoDokusiOwner = melanbideDokusiPlateaProService.getCodigoProcedimientoPlatea(procOwnerOrAutorizaCambiar,params);//convierteProcedimiento(procOwnerOrAutorizaCambiar);
        log.info("Owner del documento para instanciar el servicio : " + procOwnerOrAutorizaCambiar);
        if(procedimientoDokusi != null && !procedimientoDokusi.isEmpty()){
            try{
            // Instaciamos con el Owner para que tenga  los permisos y pueda modificar
            Lan6DokusiServicios servicios = new Lan6DokusiServicios(procOwnerOrAutorizaCambiar);
        
            Lan6Documento lan6Documento = new Lan6Documento();
        
            lan6Documento.setIdDocumento(oid);
        
            HashMap<String, String> atributosAdicionales = new HashMap<String, String>();
        
            //Serie Documental
            String serieDocumental = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("MD_SERIE_"+procedimientoDokusi);
            log.info("serieDocumental "+serieDocumental);
            atributosAdicionales.put(Lan6Constantes.METADATO_SERIE, serieDocumental);
        
            //Archivo Digital
            String idProcArchivoDigital = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ID_PROC_ARCHIVO_DIGITAL_"+procedimientoDokusi);
            atributosAdicionales.put(Lan6Constantes.METADATO_PROCEDIMIENTO, idProcArchivoDigital);
            log.info("idProcArchivoDigital "+idProcArchivoDigital);
            
            //Metadatos ACL
            //se comprueba que el documento ha sido catalogado para modificar los  metadatos ACL en función de datos de entrada. Procedimiento no se usa para esta comprobacion
            // Se debe actualizar la ACL que corresponda al nuevo procedimiento
            //isDocDigitalizadoCatalogado = isDocDigitalizadoCatalogado(codOrganizacion, ejeRegistro, num, oid, codProcedimientoNuevo, params);
            //if(isDocDigitalizadoCatalogado){
             String nuevaACL = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ACL_"+procedimientoDokusi);
                     atributosAdicionales.put(Lan6Constantes.METADATO_ACL, nuevaACL);
            log.info("nuevaACL "+nuevaACL);
            //}
            
            lan6Documento.setAtributosAdicionales(atributosAdicionales);
            lan6Documento.setTipoDocumental(Lan6Constantes.TIPO_DOCUMENTAL_ARCHIVO);
            log.info("Llamada a retramitarDocumento de Adaptadores. Numero de registro = " + ejeRegistro + "/" 
                                    + num + "; Oid de documento = " + oid);
            // Creamos el objeto para pasar el OID
            DocumentoCatalogacionVO documentoCatalogacionVO = new DocumentoCatalogacionVO();
            documentoCatalogacionVO.setIdDocGestor(oid);
            //boolean esDocumentoMigrado = isDocumentoMigradoxOID(documentoCatalogacionVO, params);
            //if (esDocumentoMigrado)
            //    log.info(" NO Se ejecuta la llamada a retramitarDocumento - Es un documento Migrado.");
            //else
                servicios.retramitarDocumento(lan6Documento);
            log.info("Finaliza la llamada a retramitarDocumento por cambio de procedimiento sin provocar ninguna Exception");
             } catch (Lan6Excepcion le) {
                log.error("Error Lan6Excepcion en la llamada a retramitarDocumento: " + le.getMensajeExcepcion(), le);
                try {
                    // Vamos Registrar el error usando el jar de Plugin de Documentos Dokusi
                    log.error("Vamos a registrar el error en BD - al retramitarDocumento Numero de registro = " + ejeRegistro + "/" + num
                            + "; Oid de documento = " + oid);
                    String causaExcepcion = (le.getCause() != null ? (le.getCause().getMessage() != null ? le.getCause().getMessage() + " - " + le.getCause().toString() : le.getCause().toString()) : "");
                    String mensajeExcepcion = (le.getMessages().size() > 0 ? le.getMessages().get(0).toString() : "Excepcion capturada sin Mensaje de error");
                    String trazaError = le.getTrazaExcepcion(); 

                    Lan6ErrorBean errorBean = new Lan6ErrorBean(causaExcepcion, mensajeExcepcion,
                            trazaError, "FLEXIA_REGISTRO",
                            "REGI_RETRAM_DOC_CAMB_PROC", "Error al llamar a metodo retramitarDocumento(Cambio Procedimiento) de los servicios de adaptadores de platea.", 2); // Tipollogia 2 Fucional
                    ErrorLan6ExcepcionBean errorLan6Bean = new ErrorLan6ExcepcionBean(errorBean, le);
                    // Creamos el objeto docGestor para pasar los datos del procediieto y el ID del registro tratado.
                    DocumentoGestor docGestor = new DocumentoGestor();
                    docGestor.setCodProcedimiento(codProcedimientoNuevo);
                    docGestor.setEjercicioAnotacion(ejeRegistro);
                    docGestor.setNumeroRegistro(num);
                    docGestor.setIdDocGestor(oid);
                    idError = GestionarErroresDokusi.grabarError(errorLan6Bean, docGestor, oid, "", this.getClass().getName() + ".retramitarDocumentoCambioProcedimiento()");
                    log.error("Error Registrado en BD correctamente: " + idError);
                } catch (Exception ex1) {
                    textoErrorAux=ex1.getMessage();
                    log.error("LLamada servicios retramitarDocumento - Error al registrar un error en BBDD.: " + textoErrorAux, ex1);
                }
                throw new Lan6Excepcion(String.valueOf(idError),"Error durante la retramitacion de documentos por cambio de procedimiento. " + textoErrorAux);
            } catch (Exception ex) {
                textoErrorAux=ex.getMessage();
                log.error("error Exception al retramitarDocumento por cambio de procedimiento : " + textoErrorAux, ex);
                try {
                    // Vamos Registrar el error usando el jar de Plugin de Documentos Dokusi
                    log.error("Vamos a registrar el error en BD - al retramitarDocumento por cambio de procedimiento, Numero de registro = " + ejeRegistro + "/" + num
                            + "; Oid de documento = " + oid);
                    String causaExcepcion = (ex.getCause() != null ? (ex.getCause().getMessage() != null ? ex.getCause().getMessage() + " - " + ex.getCause().toString() : ex.getCause().toString()) : "");
                    String mensajeExcepcion = (ex.getMessage() != null ? ex.getMessage() : "Excepcion capturada sin Mensaje de error");
                    String trazaError = ExceptionUtils.getStackTrace(ex);

                    Lan6ErrorBean errorBean = new Lan6ErrorBean(causaExcepcion, mensajeExcepcion,
                            trazaError, "FLEXIA_REGISTRO",
                            "REGI_RETRAM_DOC_CAMB_PROC", "Error Generico al llamar a metodo retramitarDocumento(Cambio Procedimiento) de los servicios de adaptadores de platea.", 2); // Tipollogia 2 Fucional
                    ErrorLan6ExcepcionBean errorLan6Bean = new ErrorLan6ExcepcionBean(errorBean, ex);
                    // Creamos el objeto docGestor para pasar los datos del procediieto y el ID del registro tratado.
                    DocumentoGestor docGestor = new DocumentoGestor();
                    docGestor.setCodProcedimiento(codProcedimientoNuevo);
                    docGestor.setEjercicioAnotacion(ejeRegistro);
                    docGestor.setNumeroRegistro(num);
                    docGestor.setIdDocGestor(oid);
                    idError = GestionarErroresDokusi.grabarError(errorLan6Bean, docGestor, oid, "", this.getClass().getName() + ".retramitarDocumentoCambioProcedimiento()");
                    log.error("Error Registrado en BD correctamente: " + idError);
                } catch (Exception ex1) {
                    textoErrorAux=ex1.getMessage();
                    log.error("LLamada servicios retramitarDocumento por cambio de procedimiento - Error al registrar un error en BBDD. : " + textoErrorAux , ex1);
                }
                throw new Lan6Excepcion(String.valueOf(idError),"Error durante el cambio de contexto de documentos. " + textoErrorAux);
            }
        }else{
            log.error("No se ha podido mapear el Codigo de procedimiento.");
            throw new Lan6Excepcion("-1","No se ha podido mapear el Codigo de procedimiento. " + textoErrorAux);
        }
        log.info("DigitalizacionDocumentosLanbideManager.retramitarDocumentoCambioProcedimiento(): FIN");
    }
    
    private boolean isDocDigitalizadoCatalogado(Integer codOrganizacion, Integer ejeRegistro, Long num, String oid, String codProcedimiento, String[] params){
        log.debug("DigitalizacionDocumentosLanbideManager.isDocDigitalizadoCatalogado(): INI");
        boolean isCatalogado = false;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        DigitalizacionDocumentosLanbideDAO digitalizacionDAO = DigitalizacionDocumentosLanbideDAO.getInstance();
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            isCatalogado = digitalizacionDAO.isDocumentoDigitalizadoCatalogado(codOrganizacion, ejeRegistro, num, oid, codProcedimiento, con);
         }catch(BDException bde){
            log.error("Error al obtener una conexión a la BBDD: "+bde.getMensaje(),bde);   
        }catch(Exception ex){
            log.error("Ha ocurrido un error al comprobar si el documento se encuentra catalogado "+ ex.getMessage(),ex);
        }finally{
             try {
                if(con!=null) con.close();
                if(adapt!=null) adapt = null;
            } catch (SQLException ex) {
                log.error("Ha ocurrido un error al cerrar la conexión a la BBDD",ex);
            }
        }
        return isCatalogado;
    }
    
    /***
     * Sobrecarga del metodo isDocumentoMigradoxOID se recibe parametros para crear el adaptador y la conexion.
     * Comprueba si el documento es migrado desde RGI o No.
     * @param documentoCatalogado
     * @param params
     * @return 1 si es documento migrado y 0 en caso de no serlo
     * @throws SQLException
     * @throws Exception 
     */
    private boolean isDocumentoMigradoxOID(DocumentoCatalogacionVO documentoCatalogado, String[] params) throws SQLException, Exception{
        log.info("isDocumentoMigradoxOID recibiendo String[] params : INI");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        boolean retorno=false;
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            retorno = isDocumentoMigradoxOID(documentoCatalogado,con);
        }catch(BDException bde){
            log.error("Error al obtener una conexión a la BBDD: "+bde.getMensaje(),bde);   
        }catch(Exception ex){
            log.error("Ha ocurrido un error al comprobar si el documento se encuentra catalogado "+ ex.getMessage(),ex);
        }finally{
             try {
                if(con!=null) con.close();
                if(adapt!=null) adapt = null;
            } catch (SQLException ex) {
                log.error("Ha ocurrido un error al cerrar la conexión a la BBDD",ex);
            }
        }
        return retorno;
    }
    
    /***
     * Comprueba si es un documento migrado o no
     * @param documentoCatalogado
     * @param con
     * @return 1 si es documento migrado desde RGI o no en caso de no serlo
     * @throws SQLException
     * @throws Exception 
     */
    private boolean isDocumentoMigradoxOID(DocumentoCatalogacionVO documentoCatalogado, Connection con) throws SQLException, Exception{
        log.info("DigitalizacionDocumentosLanbideManager.isDocumentoMigradoxOID(): INI");
        boolean isCatalogado = false;
        DigitalizacionDocumentosLanbideDAO digitalizacionDAO = DigitalizacionDocumentosLanbideDAO.getInstance();
        try{
            int isCatalogadoInt = digitalizacionDAO.isDocumentoMigradoxOID(documentoCatalogado, con);
            if(1==isCatalogadoInt)
                isCatalogado=true;
         }catch(SQLException bde){
            log.error("isDocumentoMigradoxOID Manager - SQLException al al comprobar si el documento es migrado: "+bde.getMessage(),bde);   
            //throw bde;
        }catch(Exception ex){
            log.error("isDocumentoMigradoxOID Manager - Exception al comprobar si el documento es migrado "+ ex.getMessage(),ex);
            throw  ex;
        }
        return isCatalogado;
    }
    
    public boolean borrarDatosCatalogacionRetramitarCambioProc(DocumentoCatalogacionVO docCatalogar, String[] params) throws TechnicalException, BDException, SQLException{
        log.info("DigitalizacionDocumentosLanbideManager : borrarDatosCatalogacionRetramitarCambioProc()");
        DigitalizacionDocumentosLanbideDAO digitalizacionDAO = DigitalizacionDocumentosLanbideDAO.getInstance();
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        boolean exito = false;
        
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);            
            digitalizacionDAO.borrarDatosCatalogacionRetramitarCambioProc(docCatalogar, con);
            adapt.finTransaccion(con);
            exito=true;
        } catch (BDException bde){
            log.error("Error al obtener una conexión a la BBDD",bde);
            try {
                adapt.rollBack(con);
            } catch (BDException ex) {
                log.error("Ha ocurrido un error haciendo Rollback de la transacción",ex);
            }
            throw new BDException("Error al obtener una conexión a la BBDD");
        } catch (SQLException te) {
            log.error("Error al obtener una conexión a la BBDD",te);
            try {
                adapt.rollBack(con);
            } catch (BDException ex) {
                log.error("Ha ocurrido un error haciendo Rollback de la transacción",ex);
            }
            throw new SQLException("Error: " + te.getMessage());
        }catch (Exception ex) {
            log.error("Error al borrarDocCatalogacion del documento",ex);
            try {
                adapt.rollBack(con);
            } catch (BDException bdex) {
                log.error("Ha ocurrido un error haciendo Rollback de la transacción",ex);
            }
            throw new TechnicalException("Error al grabar la catalogación del documento");
        } finally {
            try {
                if(con!=null) con.close();
            } catch (SQLException ex) {
                log.error("Ha ocurrido un error al cerrar la conexión a la BBDD",ex);
            }
        }
        return exito;
    }
    
    public void registrarDocumentoDokusiCompulsado(Integer codOrganizacion, Integer ejeRegistro, Long num, String oid,  String registarDokusiDniUsuario, String registarDokusiNombreApellidosUsuario,String registarDokusiLoginUsuario,String[] params) throws Lan6Excepcion {
        log.info(" registrarDocumentoDokusiCompulsado (): INICIO");
        //boolean isDocDigitalizadoCatalogado = false; 
        String textoErrorAux = "";
        int idError = 0;
        if (oid !=null && !oid.isEmpty()) {
            try {
                    String cadenaUsuarioregistro = (registarDokusiDniUsuario!=null ? registarDokusiDniUsuario : "")
                            +"#"+(registarDokusiNombreApellidosUsuario!=null ? registarDokusiNombreApellidosUsuario : "")
                            +"#"+(registarDokusiLoginUsuario!=null ? registarDokusiLoginUsuario : "");
                    // Registrar de Dokusi
                    String idProcedimiento = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties(
                            "PROCEDIMIENTO_ID_" + "LANRE");
                    Lan6DokusiServicios servicios = new Lan6DokusiServicios(idProcedimiento);
                    log.info("Procedemos a realizar el registro para : " + oid + " " + cadenaUsuarioregistro);
                    log.info("respuesta: " 
                            + servicios.registraDocumento(oid, cadenaUsuarioregistro)
                    );
            } catch (Lan6Excepcion le) {
                log.error("Error Lan6Excepcion en la llamada a registraDocumento: " + le.getMensajeExcepcion(), le);
                try {
                    // Vamos Registrar el error usando el jar de Plugin de Documentos Dokusi
                    log.error("Vamos a registrar el error en BD - al registraDocumento Numero de registro = " + ejeRegistro + "/" + num
                            + "; Oid de documento = " + oid);
                    String causaExcepcion = (le.getCause() != null ? (le.getCause().getMessage() != null ? le.getCause().getMessage() + " - " + le.getCause().toString() : le.getCause().toString()) : "");
                    String mensajeExcepcion = (le.getMessages().size() > 0 ? le.getMessages().get(0).toString() : "Excepcion capturada sin Mensaje de error");
                    String trazaError = le.getTrazaExcepcion();

                    Lan6ErrorBean errorBean = new Lan6ErrorBean(causaExcepcion, mensajeExcepcion,
                            trazaError, "FLEXIA_REGISTRO",
                            "REGI_REGIST_DOC_COMP_DOK", "Error al llamar a metodo registraDocumento de los servicios de adaptadores de platea.", 2); // Tipollogia 2 Fucional
                    ErrorLan6ExcepcionBean errorLan6Bean = new ErrorLan6ExcepcionBean(errorBean, le);
                    // Creamos el objeto docGestor para pasar los datos del procediieto y el ID del registro tratado.
                    DocumentoGestor docGestor = new DocumentoGestor();
                    docGestor.setCodProcedimiento("LANRE");
                    docGestor.setEjercicioAnotacion(ejeRegistro);
                    docGestor.setNumeroRegistro(num);
                    docGestor.setIdDocGestor(oid);
                    idError = GestionarErroresDokusi.grabarError(errorLan6Bean, docGestor, oid, "", this.getClass().getName() + ".registrarDocumentoDokusiCompulsado()");
                    log.error("Error Registrado en BD correctamente: " + idError);
                } catch (Exception ex1) {
                    textoErrorAux = ex1.getMessage();
                    log.error("LLamada servicios registraDocumento - Error al registrar un error en BBDD.: " + textoErrorAux, ex1);
                }
                throw new Lan6Excepcion(String.valueOf(idError), "Error durante la registro de documentos compulsados en Dokusi. " + textoErrorAux);
            } catch (Exception ex) {
                textoErrorAux = ex.getMessage();
                log.error("error Exception al registraDocumento : " + textoErrorAux, ex);
                try {
                    log.error("Vamos a registrar el error en BD - al registraDocumento, Numero de registro = " + ejeRegistro + "/" + num
                            + "; Oid de documento = " + oid);
                    String causaExcepcion = (ex.getCause() != null ? (ex.getCause().getMessage() != null ? ex.getCause().getMessage() + " - " + ex.getCause().toString() : ex.getCause().toString()) : "");
                    String mensajeExcepcion = (ex.getMessage() != null ? ex.getMessage() : "Excepcion capturada sin Mensaje de error");
                    String trazaError = ExceptionUtils.getStackTrace(ex);

                    Lan6ErrorBean errorBean = new Lan6ErrorBean(causaExcepcion, mensajeExcepcion,
                            trazaError, "FLEXIA_REGISTRO",
                            "REGI_REGIST_DOC_COMP_DOK", "Error Generico al llamar a metodo registraDocumento, de los servicios de adaptadores de platea.", 2); // Tipollogia 2 Fucional
                    ErrorLan6ExcepcionBean errorLan6Bean = new ErrorLan6ExcepcionBean(errorBean, ex);
                    // Creamos el objeto docGestor para pasar los datos del procediieto y el ID del registro tratado.
                    DocumentoGestor docGestor = new DocumentoGestor();
                    docGestor.setCodProcedimiento("LANRE");
                    docGestor.setEjercicioAnotacion(ejeRegistro);
                    docGestor.setNumeroRegistro(num);
                    docGestor.setIdDocGestor(oid);
                    idError = GestionarErroresDokusi.grabarError(errorLan6Bean, docGestor, oid, "", this.getClass().getName() + ".retramitarDocumentoCambioProcedimiento()");
                    log.error("Error Registrado en BD correctamente: " + idError);
                } catch (Exception ex1) {
                    textoErrorAux = ex1.getMessage();
                    log.error("LLamada servicios registraDocumento - Error al registrar un error en BBDD. : " + textoErrorAux, ex1);
                }
                throw new Lan6Excepcion(String.valueOf(idError), "Error durante el cambio de contexto de documentos. " + textoErrorAux);
            }
        } else {
            log.error("No se ha recibido Oid del Documento a registrar.");
            throw new Lan6Excepcion("-1", "No se ha recibido Oid del Documento a registrar " + textoErrorAux);
        }
        log.info("registrarDocumentoDokusiCompulsado -  FIN");
    }
    
    public DocumentoCatalogacionVO getDocumentoRegistroByOID(String oidDocumento, String[] params){
        log.debug("getDocumentoRegistroByOID  -  INI");
        DocumentoCatalogacionVO respuesta = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        DigitalizacionDocumentosLanbideDAO digitalizacionDAO = DigitalizacionDocumentosLanbideDAO.getInstance();
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            respuesta = digitalizacionDAO.getDocumentoRegistroByOID(oidDocumento, con);
         }catch(BDException bde){
            log.error("Error al obtener una conexión a la BBDD: " +bde.getErrorCode()+" "+bde.getDescripcion() +" "+  bde.getMensaje() + " "+ bde.getMessage(),bde); 
        }catch(Exception ex){
            log.error("Ha ocurrido un error al recuperar los Datos de un Documento - Por OID "+ ex.getMessage(),ex);
        }finally{
             try {
                if(con!=null) con.close();
                if(adapt!=null) adapt = null;
            } catch (SQLException ex) {
                log.error("Ha ocurrido un error al cerrar la conexión a la BBDD : " + ex.getErrorCode() + " " + ex.getSQLState() + " " +ex.getMessage(),ex);
            }
        }
        return respuesta;
    }
    
    public ValidacionRetramitacionCambioProVO getDatosEntradaRegistroValidaRetramitarCambioProc(ValidacionRetramitacionCambioProVO validacionRetramitacionCambPro, String[] params){
        log.debug("getDatosEntradaRegistroValidaRetramitarCambioProc  -  INI");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        DigitalizacionDocumentosLanbideDAO digitalizacionDAO = DigitalizacionDocumentosLanbideDAO.getInstance();
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            validacionRetramitacionCambPro = digitalizacionDAO.getDatosEntradaRegistroValidaRetramitarCambioProc(validacionRetramitacionCambPro, con);
         }catch(BDException bde){
            log.error("Error al obtener una conexión a la BBDD: " +bde.getErrorCode()+" "+bde.getDescripcion() +" "+  bde.getMensaje() + " "+ bde.getMessage(),bde); 
        }catch(Exception ex){
            log.error("Ha ocurrido un error al recuperar los Datos de un Documento - Por OID "+ ex.getMessage(),ex);
        }finally{
             try {
                if(con!=null) con.close();
                if(adapt!=null) adapt = null;
            } catch (SQLException ex) {
                log.error("Ha ocurrido un error al cerrar la conexión a la BBDD : " + ex.getErrorCode() + " " + ex.getSQLState() + " " +ex.getMessage(),ex);
            }
        }
        return validacionRetramitacionCambPro;
    }
}
