package es.altia.agora.business.sge.persistence;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.AreasDAO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.sge.DefinicionProcedimientosValueObject;
import es.altia.agora.business.sge.DefinicionTramitesValueObject;
import es.altia.agora.business.sge.ErrorImportacionXPDL;
import es.altia.agora.business.sge.ExistenciaUorImportacionVO;
import es.altia.agora.business.sge.RolProcedimientoVO;
import es.altia.agora.business.sge.firma.dao.FirmaFlujoDAO;
import es.altia.agora.business.sge.firma.exception.FlujoFirmaException;
import es.altia.agora.business.sge.firma.vo.FirmaCircuitoVO;
import es.altia.agora.business.sge.firma.vo.FirmaFlujoVO;
import es.altia.agora.business.sge.persistence.manual.ImportacionProcedimientoDAO;
import es.altia.agora.business.sge.persistence.manual.UnidadesTramitacionDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager utilizado para realizar la importación en base de datos de
 * procedimientos entre entornos
 *
 * @author oscar.rodriguez
 */
public class ImportacionProcedimientoManager {

    private static ImportacionProcedimientoManager instance = null;
    private Logger log = Logger.getLogger(ImportacionProcedimientoManager.class);

    // Mensajes de error
    private static final String ERROR_OBTENER_LISTADO_FLUJOS_FIRMA_BBDD
            = "Ha ocurrido un error de bbdd al intentar obtener el listado de flujos de firma";

    private ImportacionProcedimientoManager() {
    }

    public static ImportacionProcedimientoManager getInstance() {
        if (instance == null) {
            instance = new ImportacionProcedimientoManager();
        }

        return instance;
    }


    public ArrayList<ExistenciaUorImportacionVO> getExistenUors(Vector codigosUnidadesInicio, Hashtable<String, String> definicion, String[] params) {
        ArrayList<ExistenciaUorImportacionVO> uors = new ArrayList<ExistenciaUorImportacionVO>();
        Connection con = null;

        try {
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            uors = ImportacionProcedimientoDAO.getInstance().existenUors(codigosUnidadesInicio, definicion, con);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return uors;
    }
    
    
    public String getExisteUor(String unidadeCodigoVisible, String[] params) {
        String codigoUnidadInicio = null;
        Connection con = null;

        try {
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            codigoUnidadInicio = ImportacionProcedimientoDAO.getInstance().recuperaCodigoUnidadInicio(unidadeCodigoVisible, con);

        } catch (Exception e) {
            log.error("Se ha producido un error estableciendo la conexion con la base de datos "+ e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando la conexion con la base de datos "+ e.getMessage());
            }
        }

        return codigoUnidadInicio;
    }
    

    /**
     * Comprueba si existe el procedimiento en una determinada organización
     *
     * @param codProcedimiento: Código del procedimiento
     * @param params: Parámetros de conexión a la base de datos
     * @return boolean
     */
    public boolean existeProcedimiento(String codProcedimiento, String[] params) {
        boolean exito = false;
        Connection con = null;

        try {
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            exito = ImportacionProcedimientoDAO.getInstance().existeProcedimiento(codProcedimiento, con);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Se cierra la conexión a la base de datos
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return exito;
    }

    public boolean importarProcedimiento(DefinicionProcedimientosValueObject defProcVO, String[] params) {
        boolean exito = false;
        Connection con = null;
        AdaptadorSQLBD adapt = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            ImportacionProcedimientoDAO dao = ImportacionProcedimientoDAO.getInstance();

            boolean existeProcedimiento = dao.existeProcedimiento(defProcVO.getTxtCodigo(), con);
            if (con != null) {
                con.close(); // Se cierra la conexión a la base de datos
            }
            log.debug("********** existe procedimiento: " + existeProcedimiento);
            if (!existeProcedimiento) {
                this.importarProcedimientoNuevo(defProcVO, params);
            } else {
                // Si existe el procedimiento => Hay que hacer unos chequeos previos
                //this.importarProcedimientoExistente(defProcVO,params);
            }

        } catch (BDException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Se cierra la conexión a la base de datos
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return exito;

    }

    /**
     * Da de alta un procedimiento en la base de datos junto con sus documentos,
     * enlaces y campos suplementarios.
     *
     * @param defProcVO: Objeto con la ifnormación del proceidmiento
     * @param params: Parámetros de conexión a la base de datos
     * @return
     */
    public boolean importarProcedimientoNuevo(DefinicionProcedimientosValueObject defProcVO, String[] params) {
        
        log.debug("ImportacionProcedimientoManager -> importarProcedimientoNuevo");
        boolean exito = false;
        Connection con = null;
        AdaptadorSQLBD adapt = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            ImportacionProcedimientoDAO dao = ImportacionProcedimientoDAO.getInstance();
            // SE INICIA UNA TRANSACCIÓN
            adapt.inicioTransaccion(con);
            int num = 0;

            try {
                log.debug("********************* importarProcedimiento ***********************");
                boolean procedimientoInsertado = dao.insertarProcedimiento(defProcVO, con, params);
                log.debug("********************* procedimiento importado : " + procedimientoInsertado);


                if (procedimientoInsertado) {
                    // Se ha insertado el procedimiento
                    Vector<DefinicionTramitesValueObject> tramites = (Vector<DefinicionTramitesValueObject>) defProcVO.getTramites();
                    HashMap<String,String> relacionCodigosTramites = new HashMap<String,String>();
                    //Hasht<String,String> relacionCodigosTramites=new Hashtable();
                    int contadorTramites = 0;
                    for (int i = 0; i < tramites.size(); i++) {
                        DefinicionTramitesValueObject tramite = tramites.get(i);

                        log.debug(" =============> Se intenta insertar el trámite " + tramite.getCodigoTramite());
                        String codTramiteOrigen=tramite.getCodigoTramite();
                        // Se inserta el trámite
                        int numTramiteInsertado = dao.insertarTramite(tramite, con, params);
                        if (numTramiteInsertado == -1) {
                            log.debug("==============> Número de tramites insertado: -1");
                            break;
                        } else {
                            log.debug(" El trámite  " + tramite.getCodigoTramite() + " se ha insertado correctamente");
                            contadorTramites++;

                            String codTramiteNuevo=tramite.getCodigoTramite();
                            log.debug("==============> Número de tramites insertado: 1");
                            relacionCodigosTramites.put(codTramiteOrigen, codTramiteNuevo);

                        // Se añaden las unidades tramitadoras si las hay

                        }                        

                    }//for

                    dao.actualizarCodigosTramite(relacionCodigosTramites, defProcVO.getCodMunicipio(), defProcVO.getTxtCodigo(), con);
                    dao.actualizarClasificacionDefectoTramite(defProcVO.getCodMunicipio(), defProcVO.getTxtCodigo(), con);
                    // Insertar flujos y circuitos de firmas y actualizar los codigos de flujo de los documentos insertados
                    Map<Integer, Integer> mapeoIdsFlujos = dao.insertarFlujosYCircuitosFirma(defProcVO.getListaFlujosFirma(), defProcVO.getTxtCodigo(), con);
                    dao.actualizarFlujosFirma(defProcVO.getTxtCodigo(), mapeoIdsFlujos, con);

                    if (contadorTramites == tramites.size()) {
                        // SE CONFIRMA LA TRANSACCIÓN
                        adapt.finTransaccion(con);
                        exito = true;
                    } else {
                        adapt.rollBack(con);
                    }
                    dao.actualizarClasificacionDefectoTramite(defProcVO.getCodMunicipio(),defProcVO.getTxtCodigo(), con);
                }

                //} catch (TechnicalException e) {
            } catch (Exception e) {
                e.printStackTrace();
                // Error durante la inserción del procedimiento                
                throw e;
            }

        } catch (BDException e) {
            e.printStackTrace();
            // Se deshace la transacción
            try {
                adapt.rollBack(con);
            } catch (BDException f) {
                f.printStackTrace();
            }
        } catch (Exception e) {
            // Se deshace la transacción
            try {
                adapt.rollBack(con);
            } catch (BDException f) {
                f.printStackTrace();
            }
        } finally {
            try {
                // Se cierra la conexión a la base de datos
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return exito;
    }




    
    /**
     * Comprueba si hay roles en el procedimiento que no existen en la nueva
     * definición, porque en ese caso, se indica al usuario el error y debe
     * indicar si acepta o no eliminar los roles y dar de alta los nuevos
     *
     * @param codProcedimiento
     * @param listaCodigoRoles
     * @param listaDescripcionRoles
     * @param params
     * @return
     */
    public ArrayList<RolProcedimientoVO> verificarExistenciaRoles(String codProcedimiento, Vector listaCodigoRoles, Vector listaDescripcionRoles, String[] params) {

        Connection con = null;
        ArrayList<RolProcedimientoVO> salida = null;
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);

        try {
            con = adapt.getConnection();
            salida = ImportacionProcedimientoDAO.getInstance().verificarExistenciaRoles(codProcedimiento, listaCodigoRoles, listaDescripcionRoles, con);
            // Para los roles que no existen en la nueva definición, se comprueba si hay interesados de expediente que lo tengan asociado
            ArrayList<RolProcedimientoVO> aux = new ArrayList<RolProcedimientoVO>();
            for (int i = 0; i < salida.size(); i++) {
                RolProcedimientoVO rol = salida.get(i);
                log.debug(" Se comprueba para el rol de código:  " + rol.getCodigo() + " si tiene interesados asociados");


            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Se cierra la conexión a la base de datos
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return salida;
    }


    /**
     * Verifica cuales han sido los fallos que se han producido durante una importación de un procedimiento, pero que no han impedido que esta se llevase a cabo
     * @param defProcVO: DefinicionProcedimientosValueObject con la información del procedimiento y de sus trámites
    * @param codIdioma: Código del idioma del usuario
    * @param codAplicacion: Código de la aplicación
    * @param con: Conexión a la base de datos
    * @return ArrayList<ErrorImportacionXPDL> con el listado de errores.
    * Los códigos de error son:  1 = No existe el cargo
    *                                        2 = No existe un campo desplegable para un campo suplementario de procedimiento, pero aún así se da de alta el campo
    *                                        3 = No existe un campo desplegable para un campo suplementario de trámite, pero aún así se da de alta el campo
     */
    public ArrayList<ErrorImportacionXPDL> verificarErroresImportacionProcedimientoNuevo(DefinicionProcedimientosValueObject defProcVO,int codIdioma,int codAplicacion,String[] params){
        ArrayList<ErrorImportacionXPDL> errores = new ArrayList<ErrorImportacionXPDL>();

        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        Connection con = null;
        try{
            con       = adapt.getConnection();
            errores = ImportacionProcedimientoDAO.getInstance().verificarErroresImportacionProcedimientoNuevo(defProcVO, codIdioma, codAplicacion, con);


        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                // Se cierra la conexión a la base de datos
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return errores;
        
    }// verificarErroresImportacionProcedimientoNuevo



   /**
     * Verifica cuales han sido los fallos que se han producido durante una importación de un procedimiento ya existe, pero que no han impedido que esta se llevase a cabo
     * @param defProcVO: DefinicionProcedimientosValueObject con la información del procedimiento y de sus trámites
    * @param codIdioma: Código del idioma del usuario
    * @param codAplicacion: Código de la aplicación
    * @param con: Conexión a la base de datos
    * @return ArrayList<ErrorImportacionXPDL> con el listado de errores.
    * Los códigos de error son:  1 = No existe el cargo
    *                                        2 = No existe un campo desplegable para un campo suplementario de procedimiento, pero aún así se da de alta el campo
    *                                        3 = No existe un campo desplegable para un campo suplementario de trámite, pero aún así se da de alta el campo
    */
    public ArrayList<ErrorImportacionXPDL> verificarErroresImportacionProcedimientoExistente(DefinicionProcedimientosValueObject defProcVO,int codIdioma,int codAplicacion,String[] params){

        ArrayList<ErrorImportacionXPDL> errores = new ArrayList<ErrorImportacionXPDL>();

        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        Connection con = null;
        try{
            con       = adapt.getConnection();
            errores = ImportacionProcedimientoDAO.getInstance().verificarErroresImportacionProcedimientoExistente(defProcVO, codIdioma, codAplicacion, con);

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                // Se cierra la conexión a la base de datos
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return errores;
    }

  /**
     * Actualiza un procedimiento ya existente
     *
     * @param defProcVO: Objeto con la información del procedimiento     
     * @param params: Parámetros de conexión a la base de datos
     * @return
     */
    public boolean actualizarProcedimiento(DefinicionProcedimientosValueObject defProcVO,int codMunicipio,String[] params) {
        boolean exito = false;
        Connection con = null;
        AdaptadorSQLBD adapt = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            ImportacionProcedimientoDAO dao = ImportacionProcedimientoDAO.getInstance();
            // SE INICIA UNA TRANSACCIÓN
            adapt.inicioTransaccion(con);
            int num = 0;

            try {
                log.debug("********************* importarProcedimiento ***********************");
                boolean procedimientoActualizado = dao.modificarProcedimiento(defProcVO, adapt, con);
                log.debug("********************* procedimiento actualizado : " + procedimientoActualizado);

                if (procedimientoActualizado) {

                     log.debug("ImportacionProcedimientoManager.modificarProcedimiento realizada " + num);
                     
                     
                    // si el error del codigo de area es igual a 4 no existe el código ni la descripción del area insertada por lo que es necesario crearla
                    if (defProcVO.getErrorArea().equals(4)) {

                        GeneralValueObject area = new GeneralValueObject();
                        area.setAtributo("codigo", defProcVO.getCodArea());
                        area.setAtributo("codCampo", "NOM");
                        area.setAtributo("descripcion", defProcVO.getDescArea());
                        AreasDAO.getInstance().altaArea(area, params);
                    }
                     
                    Vector<DefinicionTramitesValueObject> tramites = defProcVO.getTramites();
                    HashMap<String, String> hashTramites = ImportacionProcedimientoDAO.getInstance().actualizarTramiteConCodigoTramite(tramites, defProcVO.getTxtCodigo(), con);

                    /**
                     * SE BUSCAN LOS TRÁMITES YA EXISTENTES EN EL PROCEDIMIENTO
                     * Y QUE NO SE ENCUENTRAN EN LA NUEVA DEFINICIÓN *
                     */
                    boolean tramitesViejosBorrados = ImportacionProcedimientoDAO.getInstance().eliminarTramitesExistentesNoEstanEnNuevaDefinicion(defProcVO, adapt, con);
                    if (tramitesViejosBorrados) {
                        int contadorTramites = 0;
                        for(int i=0;i<tramites.size();i++){
                            DefinicionTramitesValueObject tramite = (DefinicionTramitesValueObject)tramites.get(i);

                            if(ImportacionProcedimientoDAO.getInstance().existeDefinicionTramite(tramite,con)){
                                int numTramiteInsertado = ImportacionProcedimientoDAO.getInstance().modificarTramite(tramite, adapt, con);
                                log.debug("ImportacionProcedimientoManager.modificarProcedimiento modificacion tramite " + numTramiteInsertado);

                               if (numTramiteInsertado != 1) {
                                    break;
                                } else {
									
                                    log.debug(" El trámite  " + tramite.getCodigoTramite() + " se ha insertado correctamente");
                                    contadorTramites++;

                                    /**
                                     * SE ACTUALIZAN LAS UNIDADES TRAMITADORAS
                                     * SI PROCEDE *
                                     */
                                    int codTram = Integer.parseInt(tramite.getCodigoTramite());
                                    // Borramos las unidades tramitadoras
                                    UnidadesTramitacionDAO.getInstance().deleteUTRByTramite(codMunicipio, defProcVO.getTxtCodigo(), codTram,con);
                                    // Se insertan las nuevas unidades tramitadoras
                                    UnidadesTramitacionDAO.getInstance().insertUTR(codMunicipio, defProcVO.getTxtCodigo() , codTram, (Vector<UORDTO>)tramite.getListaUnidadesTramitadoras(),con);
                                }
                                log.debug("Número de tramites insertado: " + numTramiteInsertado);
                                
                            }//if
                            else{
                                log.debug("El trámite no existe, por tanto se da de alta");
                                //Se tiene que dar de alta el trámite
                                int numTramiteInsertado = ImportacionProcedimientoDAO.getInstance().insertarTramite(tramite, con, params);
                                log.debug("Se ha creado un nuevo trámite para el procedimiento " + tramite.getTxtCodigo());
                                contadorTramites++;
                            }
                            

                         }// for
                        // Actualizar las referencias a los trámites que se han actualizado
                        if (!hashTramites.isEmpty()) {
                            ImportacionProcedimientoDAO.getInstance().actualizarCodigosTramite(
                                    hashTramites, defProcVO.getCodMunicipio(), defProcVO.getTxtCodigo(), con);
                        }

                        // Insertar flujos y circuitos de firmas y actualizar los codigos de flujo de los documentos insertados
                        Map<Integer, Integer> mapeoIdsFlujos = dao.insertarFlujosYCircuitosFirma(defProcVO.getListaFlujosFirma(), defProcVO.getTxtCodigo(), con);
                        dao.actualizarFlujosFirma(defProcVO.getTxtCodigo(), mapeoIdsFlujos, con);

                        if (contadorTramites == tramites.size()) {
                            exito = true;
                            adapt.finTransaccion(con);
                        
                        } else {
                            adapt.rollBack(con);
                        }
                         dao.actualizarClasificacionDefectoTramite(defProcVO.getCodMunicipio(),defProcVO.getTxtCodigo(), con);
                    }else{
                        log.debug("Error durante la eliminación de los trámites que no se encuentran en la nueva definición");
                        adapt.rollBack(con);
                    }
            }//if

            } catch (TechnicalException e) {
                e.printStackTrace();
                log.error("Error, procedemos a realizar rolback  " + e.getMessage() + " transaccion : " + con.getAutoCommit());
                // Se deshace la transacción
                try {
                    adapt.rollBack(con);
                } catch (BDException f) {
                    f.printStackTrace();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Error, procedemos a realizar rolback  " + e.getMessage());
            // Se deshace la transacción
            try {
                adapt.rollBack(con);
            } catch (BDException f) {
                f.printStackTrace();
            }

        } catch (BDException e) {
            // Se deshace la transacción
            try {
                adapt.rollBack(con);
            } catch (BDException f) {
                f.printStackTrace();
            }
        } catch (Exception e) {
            // Se deshace la transacción
            try {
                adapt.rollBack(con);
            } catch (BDException f) {
                f.printStackTrace();
            }
        } finally {
            try {
                // Se cierra la conexión a la base de datos
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return exito;
    }



  /**
     * Comprueba si hay expedientes de un determinado procedimiento,pendientes de tramitar en alguno de los trámites ya existentes que no se encuentran en la nueva definición
     * @param defVO: DefinicionProcedimientosValueObject con la nueva definición del procedimiento y de sus trámites
     * @param codIdioma: Código del idioma del usuario
     * @param codAplicacion: Código de la aplicación
     * @param params: Parámetros de conexión a la base de datos
     * @return ArrayList<ErrorImportacionXPDL>
     */
    public ArrayList<ErrorImportacionXPDL> tieneExpedientesPendientesTramitar(DefinicionProcedimientosValueObject defVO,int codIdioma,int codAplicacion, String[] params) {
    
        Connection con = null;
        ArrayList<ErrorImportacionXPDL> errores = new ArrayList<ErrorImportacionXPDL>();
        try {
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            errores = ImportacionProcedimientoDAO.getInstance().tieneExpedientesPendientesTramitar(defVO,codIdioma,codAplicacion,con);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Se cierra la conexión a la base de datos
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return errores;
    }

 
    /**
     * Recupera las unidades de inicio de un procedimiento que este tenga asignadas y que no se encuentren ya en una determinada lista de unidades
     * @param listaUnidades: Vector de String que contiene los códigos visibles de las unidades orgánicas
     * @param codProcedimiento: Código del procedimiento
     * @param con: Conexión a la base de datos
     * @return ArrayList<ExistenciaUorImportacionVO>
     */
    public ArrayList<ExistenciaUorImportacionVO> getUnidadesInicioProcedimiento(Vector<String> listaUnidades,String codProcedimiento,String[] params){
        ArrayList<ExistenciaUorImportacionVO> salida = new ArrayList<ExistenciaUorImportacionVO>();
        Connection con = null;
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            salida = ImportacionProcedimientoDAO.getInstance().getUnidadesInicioProcedimiento(listaUnidades, codProcedimiento, con);

        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }// finally
        
        return salida;
    }

      /**
     * Recupera las unidades tramitadoras de un trámite y que no se encuentren ya en una determinada lista de unidades
     * @param dtVO: Objeto DefinicionTramitesValueObject con la definición del trámite
     * @param uors: Vector<UORDTO> con la colección de unidades tramitadoras del trámite que se va a importar
     * @param codProcedimiento: Código del procedimiento
     * @param con: Conexión a la base de datos
     * @return ArrayList<ExistenciaUorImportacionVO>
     */
    public ArrayList<ExistenciaUorImportacionVO> getUnidadesTramitadorasTramite(DefinicionTramitesValueObject dtVO,Vector<UORDTO> listaUnidades,String codProcedimiento,String[] params){
        ArrayList<ExistenciaUorImportacionVO> salida = new ArrayList<ExistenciaUorImportacionVO>();
        Connection con = null;
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            salida = ImportacionProcedimientoDAO.getInstance().getUnidadesTramitadorasTramite(dtVO,listaUnidades, codProcedimiento, con);

        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }//finally
        return salida;
    }


     /**
     * Recupera la unidad de inicio manual que tiene un trámite actualmentes unidades tramitadoras de un trámite y que no se encuentren ya en una determinada lista de unidades
     * @param dtVO: Objeto DefinicionTramitesValueObject con la definición del trámite
     * @param codProcedimiento: Código del procedimiento
     * @param params: Parámetros de conexión a la base de datos
     * @return ArrayList<ExistenciaUorImportacionVO>
     */
    public ArrayList<ExistenciaUorImportacionVO> getUnidadInicioManualTramite(DefinicionTramitesValueObject dtVO,String codProcedimiento,String[] params){
        ArrayList<ExistenciaUorImportacionVO> salida = new ArrayList<ExistenciaUorImportacionVO>();
        Connection con = null;
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            salida = ImportacionProcedimientoDAO.getInstance().getUnidadInicioManualTramite(dtVO, codProcedimiento, con);

        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }//finally
        return salida;
    }

    /**
     * Obtiene el listado de flujos de firma y firmantes del circuito de cada
     * uno por procedimiento
     *
     * @param codProcedimiento
     * @param params
     * @return
     * @throws FlujoFirmaException
     */
    public List<FirmaFlujoVO> getListaFlujosYCircuitosFirmasProc(String codProcedimiento, String[] params)
            throws FlujoFirmaException {
        log.debug("ImportacionProcedimientoManager.getListaFlujosYCircuitosFirmasProc()::BEGIN");
        FirmaFlujoDAO firmasFlujoDAO = FirmaFlujoDAO.getInstance();
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        List<FirmaFlujoVO> listaFlujos = null;
        List<FirmaCircuitoVO> listaCircuito = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            listaFlujos = firmasFlujoDAO.getListaFlujosFirmaByCodProc(codProcedimiento, con);
            for (FirmaFlujoVO flujo : listaFlujos) {
                Integer flujoId = flujo.getId();
                listaCircuito = firmasFlujoDAO.getListaCircuitoFirmasByIdFlujo(flujoId, con);
                flujo.setListaFirmasCircuito(listaCircuito);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FlujoFirmaException(ERROR_OBTENER_LISTADO_FLUJOS_FIRMA_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        log.debug("ImportacionProcedimientoManager.getListaFlujosYCircuitosFirmasProc()::END");
        return listaFlujos;
    }
}