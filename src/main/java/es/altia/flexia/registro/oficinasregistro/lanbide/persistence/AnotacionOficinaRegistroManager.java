package es.altia.flexia.registro.oficinasregistro.lanbide.persistence;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.HistoricoMovimientoValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.persistence.HistoricoMovimientoManager;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.manual.TramitacionDAO;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.HistoricoAnotacionHelper;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.registro.oficinasregistro.lanbide.persistence.manual.AnotacionOficinaRegistroDAO;
import es.altia.flexia.registro.oficinasregistro.lanbide.vo.AnotacionOficinaRegistroVO;
import es.altia.flexia.registro.oficinasregistro.lanbide.vo.OficinaRegistroLanbideVO;

import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;




import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author oscar.rodriguez
 */
public class AnotacionOficinaRegistroManager {

    private static AnotacionOficinaRegistroManager instance;
    private Logger log = Logger.getLogger(AnotacionOficinaRegistroManager.class);

    private AnotacionOficinaRegistroManager(){
    }

    public static AnotacionOficinaRegistroManager getInstance(){
        if(instance==null){
            instance=new AnotacionOficinaRegistroManager();
        }

        return instance;
    }

  /**
     * Recupera las oficinas de registro de un usuario
     * @param codUorRegistro: Código de la unidad de registro seleccionada por el usuario antes de entrar en el módulo de Registro de E/S
     * @param usuario: UsuarioValueObject con los datos del usuario
     * @param conexion:  Conexion a la base de datos
     * @return ArrayList<OficinaRegistroLanbideVO>
     */
    public OficinaRegistroLanbideVO getOficinaRegistroUsuario(int codUorRegistro,UsuarioValueObject usuario,String[] params){
       Connection conexion = null;
       OficinaRegistroLanbideVO oficina = null;

       try{
           AdaptadorSQLBD adaptador = new AdaptadorSQLBD(params);
           conexion = adaptador.getConnection();
           // Se recuperan las unidades orgánicas que hacen de oficinas de registro en Lanbide
           /** ORIGINAL
           ArrayList<OficinaRegistroLanbideVO> oficinas = AnotacionOficinaRegistroDAO.getInstance().getUnidadesUsuarioTipoOficinasRegistro(usuario, conexion);           
           if(oficinas.size()==1)
            oficina = oficinas.get(0);
            */
           
           oficina = AnotacionOficinaRegistroDAO.getInstance().getOficinaRegistroUsuario(codUorRegistro, usuario, conexion,params[6]);
           
       }catch(Exception e){
            e.printStackTrace();
       }finally{
           
           try{
               if(conexion!=null) conexion.close();
           }catch(SQLException e){
               e.printStackTrace();
           }
       }

       return oficina;
    }  


 /**
     * Recupera las oficinas de registro existentes     
     * @param conexion:  Conexion a la base de datos
     * @return ArrayList<OficinaRegistroLanbideVO>
     */
    public ArrayList<OficinaRegistroLanbideVO> getOficinasRegistro(String[] params){
       Connection conexion = null;
       ArrayList<OficinaRegistroLanbideVO> oficinas = new ArrayList<OficinaRegistroLanbideVO>();

       try{
           AdaptadorSQLBD adaptador = new AdaptadorSQLBD(params);
           conexion = adaptador.getConnection();           
           oficinas = AnotacionOficinaRegistroDAO.getInstance().getOficinasRegistro(conexion);

       }catch(Exception e){
            e.printStackTrace();
       }finally{

           try{
               if(conexion!=null) conexion.close();
           }catch(SQLException e){
               e.printStackTrace();
           }
       }

       return oficinas;
    }


    /**
     * Recupera las anotaciones de registro de tipo entrada que se encuentran que han sido dadas de alta en un determinado rango de fechas     
     * @param codUnidadRegistro: Código de la unidad de registro
     * @param codOficinaOrigen: Código de la oficina de origen
     * @param codOficinaDestino: Código de la oficina de destino
     * @param fechaDesde: Fecha desde
     * @param fechaHasta: Fecha hasta
     * @param estado: Estado     
     * @param Usuario: Objeto UsuarioValueObject con el contenido de la información del usuario
     * @param pagina: Pagina actual a recuperar
     * @param numRegistrosPagina: Número de registros a mostrar por cada página
     * @param registroInicio: Registro a partir se comienza a recuperar
     * @param conexion:  Conexion a la base de datos
     * @return ArrayList<OficinaRegistroLanbideVO> o vacío si no hay anotaciones o el usuario no tiene los permisos pertinentes
     */
    public ArrayList<AnotacionOficinaRegistroVO> getAnotaciones(String codUnidadRegistro,String codOficinaOrigen,String codOficinaDestino,
            String nombreOficinaDestino, String fechaDesde, String fechaHasta,String estado,UsuarioValueObject usuario,
            int pagina,int numRegistrosPagina,String[] params){
        Connection con = null;
        ArrayList<AnotacionOficinaRegistroVO> anotaciones = new ArrayList<AnotacionOficinaRegistroVO>();
        
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            int codAplicacion= usuario.getAppCod();
            int codIdioma    = usuario.getIdioma();

            // Se recuperan las anotaciones de BBDD
            anotaciones = AnotacionOficinaRegistroDAO.getInstance().getAnotaciones(codUnidadRegistro,codOficinaOrigen, codOficinaDestino,nombreOficinaDestino, 
                    fechaDesde, fechaHasta,estado,usuario, 
                    pagina,numRegistrosPagina,codAplicacion,codIdioma,con,params[6]);
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return anotaciones;
    }


    /**
     * Cuenta el número de anotaciones que cumplen los criterios de búsqueda     
     * @param codUnidadRegistro: Código de la unidad de tipo registro seleccionada al entrar en el módulo de registro de E/S por el usuario
     * @param codOficinaOrigen: Código de la oficina de origen
     * @param codOficinaDestino: Código de la oficina de destino
     * @param fechaDesde: Fecha desde
     * @param fechaHasta: Fecha hasta
     * @param estado: Código del estado
     * @param Usuario: Objeto UsuarioValueObject con el contenido de la información del usuario
     * @param conexion:  Conexion a la base de datos
     * @return ArrayList<OficinaRegistroLanbideVO> o vacío si no hay anotaciones o el usuario no tiene los permisos pertinentes
     */
    public int getTotalAnotaciones(String codUnidadRegistro,String codOficinaOrigen,String codOficinaDestino,String fechaDesde, String fechaHasta,String estado,UsuarioValueObject usuario,String[] params){
        Connection con = null;
        int total = 0;
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            total = AnotacionOficinaRegistroDAO.getInstance().getTotalAnotaciones(codUnidadRegistro,codOficinaOrigen, codOficinaDestino, fechaDesde, fechaHasta,estado, params[6], usuario, con);

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();
                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return total;
    }



    public boolean aceptarAnotaciones(ArrayList<AnotacionOficinaRegistroVO> anotaciones,UsuarioValueObject usuario,String[] params){
        boolean exito = false;
        Connection conexion = null;
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        ArrayList<TramitacionValueObject> nuevo = new ArrayList<TramitacionValueObject>();
        try{
            
            conexion = adapt.getConnection();            
            
            /** Se recuperan las observaciones y origen de cada anotación */
            for(int i=0;i<anotaciones.size();i++){
                AnotacionOficinaRegistroVO anotacion = anotaciones.get(i);
                TramitacionValueObject tramVO = new TramitacionValueObject();
                tramVO.setCodDepartamento(anotacion.getCodDepartamento());
                tramVO.setCodUnidadRegistro(anotacion.getUor());
                tramVO.setTipoRegistro(anotacion.getTipoEntrada());
                tramVO.setEjercicioRegistro(anotacion.getEjercicio());
                tramVO.setNumero(anotacion.getNumero());
                //tramVO.setOrigen(elRegistroESVO.getIdServicioOrigen());                
                tramVO.setOrigen("SGE");
                tramVO.setIdUsuario(usuario.getIdUsuario());
                RegistroValueObject registro = AnotacionOficinaRegistroDAO.getInstance().getAnotacionRegistro(anotacion, conexion);
                
                //tramVO.setObservaciones(StringEscapeUtils.escapeHtml(tramForm.getObservaciones()));
                String obser = registro.getObservaciones();
                String observFichero = obser.replace("\r\n","<br>");
                tramVO.setObservaciones(observFichero);
                nuevo.add(tramVO);

            } // for

            try{
                // Se cierra la conexión a la BBDD
                if(conexion!=null) conexion.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }

            int contador = 0;
            for(int i=0;i<nuevo.size();i++)
            {
                TramitacionValueObject tramVO = nuevo.get(i);
                try
                {
                    // Se cambia el estado de la anotación a INCORRECTO o RECHAZADO
                    //VisorRegistroManager.getInstance().cambiaEstadoAsiento(tramVO, usuario, 2, params);
                    //TramitacionDAO.getInstance().cambiaEstadoAsiento(tramVO, 1, params);
                    TramitacionDAO.getInstance().cambiaEstadoAsiento(tramVO, ConstantesDatos.REG_ANOTACION_ESTADO_ACEPTADA_EN_DESTINO, params);
                    contador++;

                }catch (TechnicalException te) {
                    te.printStackTrace();
                    log.debug("<================= AnotacionOficinaRegistroManager ======================");
                }catch (TramitacionException te) {
                    te.printStackTrace();
                    log.debug("<================= AnotacionOficinaRegistroManager ======================");
                }

            }// for

           if(contador==nuevo.size()) exito = true;

        }catch(BDException e){
            e.printStackTrace();            
        }

        return exito;
    }


      public boolean rechazarAnotaciones(ArrayList<AnotacionOficinaRegistroVO> anotaciones,UsuarioValueObject usuario,String[] params){
        boolean exito = false;
        Connection conexion = null;
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        ArrayList<TramitacionValueObject> nuevo = new ArrayList<TramitacionValueObject>();
        try{

            conexion = adapt.getConnection();

            /** Se recuperan las observaciones y origen de cada anotación */
            for(int i=0;i<anotaciones.size();i++){
                AnotacionOficinaRegistroVO anotacion = anotaciones.get(i);
                TramitacionValueObject tramVO = new TramitacionValueObject();
                tramVO.setCodDepartamento(anotacion.getCodDepartamento());
                tramVO.setCodUnidadRegistro(anotacion.getUor());
                tramVO.setTipoRegistro(anotacion.getTipoEntrada());
                tramVO.setEjercicioRegistro(anotacion.getEjercicio());
                tramVO.setNumero(anotacion.getNumero());
                //tramVO.setOrigen(elRegistroESVO.getIdServicioOrigen());                
                tramVO.setOrigen("SGE");

                RegistroValueObject registro = AnotacionOficinaRegistroDAO.getInstance().getAnotacionRegistro(anotacion, conexion);
                //tramVO.setObservaciones(StringEscapeUtils.escapeHtml(tramForm.getObservaciones()));
                String obser = registro.getObservaciones();
                String observFichero = obser.replace("\r\n","<br>");
                tramVO.setObservaciones(observFichero);
                tramVO.setIdUsuario(usuario.getIdUsuario());
                nuevo.add(tramVO);

            } // for

            try{
                // Se cierra la conexión a la BBDD
                if(conexion!=null) conexion.close();
            }catch(SQLException e){
                e.printStackTrace();
            }

            int contador = 0;
            for(int i=0;i<nuevo.size();i++)
            {
                TramitacionValueObject tramVO = nuevo.get(i);
                try
                {
                    // Se cambia el estado de la anotación a INCORRECTO o RECHAZADO
                    //VisorRegistroManager.getInstance().cambiaEstadoAsiento(tramVO, usuario, 2, params);
                    TramitacionDAO.getInstance().cambiaEstadoAsiento(tramVO, 2, params);
                    contador++;

                }catch (TechnicalException te) {
                    te.printStackTrace();
                    log.debug("<================= AnotacionOficinaRegistroManager ======================");
                }catch (TramitacionException te) {
                    te.printStackTrace();
                    log.debug("<================= AnotacionOficinaRegistroManager ======================");
                }

            }// for

           if(contador==nuevo.size()) exito = true;

        }catch(BDException e){
            e.printStackTrace();
        }

        return exito;
    }


    /**
       * Permite marca un conjunto de anotaciones que se encontraba en estado de Incorrecta/Rechazada como pendiente
       * @param anotaciones: Conjunto de anotaciones
       * @param usuario: Objeto con la información del usuario
       * @param params: Parámetros de conexión a la base de datos
       * @return Un boolean
       */
     public boolean enviarAnotaciones(ArrayList<AnotacionOficinaRegistroVO> anotaciones,UsuarioValueObject usuario,String[] params){
        boolean exito = false;
        Connection conexion = null;
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        
        try{

            conexion = adapt.getConnection();
            adapt.inicioTransaccion(conexion);

            int contador = 0;
            for(int i=0;i<anotaciones.size();i++){
               AnotacionOficinaRegistroVO anot = anotaciones.get(i);
               boolean correcto = AnotacionOficinaRegistroDAO.getInstance().cambiarEstadoPendienteAnotacion(anot, conexion);
               
               if(correcto){
                    RegistroValueObject reg = new RegistroValueObject();
                    reg.setIdentDepart(Integer.parseInt(anot.getCodDepartamento()));
                    reg.setUnidadOrgan(Integer.parseInt(anot.getUor()));
                    reg.setTipoReg(anot.getTipoEntrada());
                    reg.setAnoReg(Integer.parseInt(anot.getEjercicio()));
                    reg.setNumReg(Long.parseLong(anot.getNumero()));

                   // Insertar alta en el historico de movimientos
                   HistoricoMovimientoValueObject hvo = new HistoricoMovimientoValueObject();
                   hvo.setCodigoUsuario(usuario.getIdUsuario());
                   hvo.setTipoEntidad(ConstantesDatos.HIST_ENTIDAD_ANOTACION);
                   hvo.setCodigoEntidad(HistoricoAnotacionHelper.crearClaveHistorico(reg));
                   hvo.setTipoMovimiento(ConstantesDatos.HIST_ANOT_ELIMINAR_RECHAZO);
                   log.debug("CREAR VO HISTORICO EN ALTA");

                   hvo.setDetallesMovimiento(HistoricoAnotacionHelper.crearXMLEliminarRechazo());
                   HistoricoMovimientoManager.getInstance().insertarMovimientoHistorico(hvo, conexion, params);
                   contador++;
               }// if

           }//for

          if(contador==anotaciones.size()){
              exito = true;
              adapt.finTransaccion(conexion);
          }
          
        }catch(BDException e){
            e.printStackTrace();
            try{
                exito = false;
                adapt.rollBack(conexion);
            }catch(Exception f){
                f.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
            try{
                exito = false;
                adapt.rollBack(conexion);
            }catch(Exception f){
                f.printStackTrace();
            }
        }
        finally{
            try{                
                adapt.devolverConexion(conexion);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return exito;
    }



     public ArrayList<String> getTodasUnidades(String tipoEntrada,String fechaInicio,String fechaFin,String codDepartamento,String[] params) throws TechnicalException{
         ArrayList<String> unidades = new ArrayList<String>();
         AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
         Connection con = null;

         try{             
             con = adapt.getConnection();
             unidades = AnotacionOficinaRegistroDAO.getInstance().getTodasUnidades(tipoEntrada,fechaInicio,fechaFin,codDepartamento, con, params);

         }catch(BDException e){
             e.printStackTrace();
         }finally{

             try{
                 adapt.devolverConexion(con);
             }catch(BDException e){
                 e.printStackTrace();
             }
         }

         return unidades;
     }
      public ArrayList<String> getTodasUnidadesPorOficinaPadre(String tipoEntrada,String fechaInicio,String fechaFin,String codDepartamento,String codOficina,String[] params) throws TechnicalException{
         ArrayList<String> unidades = new ArrayList<String>();
         AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
         Connection con = null;

         try{
             con = adapt.getConnection();
             unidades = AnotacionOficinaRegistroDAO.getInstance().getTodasUnidadesPorOficinaPadre(tipoEntrada,fechaInicio,fechaFin,codDepartamento,codOficina, con, params);

         }catch(BDException e){
             e.printStackTrace();
         }finally{

             try{
                 adapt.devolverConexion(con);
             }catch(BDException e){
                 e.printStackTrace();
             }
         }

         return unidades;
     }

      public boolean isOficinaPadreRaiz(String codigoUor,String[] params) throws TechnicalException{
         boolean esRaiz=false;
         AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
         Connection con = null;

         try{
             con = adapt.getConnection();
             esRaiz = AnotacionOficinaRegistroDAO.getInstance().isOficinaPadreRaiz(codigoUor,con, params);

         }catch(BDException e){
             e.printStackTrace();
         }finally{

             try{
                 adapt.devolverConexion(con);
             }catch(BDException e){
                 e.printStackTrace();
             }
         }

         return esRaiz;
     }


     
    public String consulta_fecha(String codigoUnidad, String fecha_desde, String fecha_hasta, boolean orden,
            String ejercicio, String codOrganizacion, String tipoEntrada, String codUnidadRegistro,
            String estado, boolean ofiDestinoLaDelUsuario, boolean ofiOrigenLaDelUsuario,
            String codOficinaOrigen, UsuarioValueObject usuario) {
        
        Config conf = ConfigServiceHelper.getConfig("techserver");

        String sql = "";
        String[] params = usuario.getParamsCon();
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        String nombreLargo = " ";
        nombreLargo = "OP1";
        boolean esRaiz=false;

        try{
             esRaiz=isOficinaPadreRaiz(codigoUnidad,params);

         }catch(Exception e){
             e.printStackTrace();
         }


        String concat[] = {conf.getString("SQL.T_TVI.abreviatura"), "' '"};
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = conf.getString("SQL.T_VIA.nombreVia");
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = "' '";
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        String[] nulo = {oad.convertir(conf.getString("SQL.T_DSU.numeroDesde"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null), "''"};
        concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = "' '";
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        nulo[0] = conf.getString("SQL.T_DSU.letraDesde");
        concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = "' '";
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        nulo[0] = oad.convertir(conf.getString("SQL.T_DSU.numeroHasta"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, null);
        concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = "' '";
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        nulo[0] = conf.getString("SQL.T_DSU.letraHasta");
        concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);

        // CONCATENAR LA POBLACION
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = "' '";
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = conf.getString("SQL.T_MUN.nombre");

        String parteFrom = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,
                concat);
        String parteWhere = conf.getString("SQL.T_DPO.domicilio") + " = " + conf.getString("SQL.R_RES.domicTercero");

        String[] joins = new String[14];
        joins[0] = "T_DPO";
        joins[1] = "INNER";
        joins[2] = "T_DSU";
        joins[3] = conf.getString("SQL.T_DPO.suelo") + " = "
                + conf.getString("SQL.T_DSU.identificador");
        joins[4] = "INNER";
        joins[5] = "T_VIA";
        joins[6] = conf.getString("SQL.T_DSU.pais") + " = "
                + conf.getString("SQL.T_VIA.pais") + " AND " + conf.getString("S"
                + "QL.T_DSU.provincia") + " = " + conf.getString("SQL.T_VIA.prov"
                + "incia") + " AND " + conf.getString("SQL.T_DSU.municipio")
                + " = " + conf.getString("SQL.T_VIA.municipio") + " AND "
                + conf.getString("SQL.T_DSU.vial") + " = " + conf.getString("SQL"
                + ".T_VIA.identificador");
        joins[7] = "INNER";
        joins[8] = "T_TVI";
        joins[9] = conf.getString("SQL.T_VIA.tipo") + " = "
                + conf.getString("SQL.T_TVI.codigo");
        joins[10] = "INNER";
        joins[11] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
        joins[12] = conf.getString("SQL.T_DSU.pais") + " = "
                + conf.getString("SQL.T_MUN.idPais") + " AND "
                + conf.getString("SQL.T_DSU.provincia") + " = "
                + conf.getString("SQL.T_MUN.idProvincia") + " AND "
                + conf.getString("SQL.T_DSU.municipio") + " = "
                + conf.getString("SQL.T_MUN.idMunicipio");
        joins[13] = "false";
        String calle1 = "''";
        try {

            log.debug("======> parteWhere: " + parteWhere);
            calle1 = oad.join(parteFrom, parteWhere, joins);

        } catch (Exception bde) {
            if (log.isDebugEnabled()) {
                log.error("Error en calle1: " + bde.toString());
            }
        }
        concat[0] = conf.getString("SQL.T_TVI.abreviatura");
        concat[1] = "' '";
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = conf.getString("SQL.T_VIA.nombreVia");
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = "' '";
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        nulo[0] = oad.convertir(conf.getString("SQL.T_DNN.numDesde"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
        concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = "' '";
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        nulo[0] = conf.getString("SQL.T_DNN.letraDesde");
        concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = "' '";
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        nulo[0] = oad.convertir(conf.getString("SQL.T_DNN.numHasta"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
        concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = "' '";
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        nulo[0] = conf.getString("SQL.T_DNN.letraHasta");
        concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);

        // CONCATENAR LA POBLACION
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = "' '";
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = conf.getString("SQL.T_MUN.nombre");

        parteFrom = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        parteWhere = conf.getString("SQL.T_DNN.idDomicilio") + " = "
                + conf.getString("SQL.R_RES.domicTercero");
        joins = new String[11];
        joins[0] = "T_DNN";
        joins[1] = "INNER";
        joins[2] = "T_VIA";
        joins[3] = conf.getString("SQL.T_DNN.idPaisDVia") + " = "
                + conf.getString("SQL.T_VIA.pais") + " AND " + conf.getString("S"
                + "QL.T_DNN.idProvinciaDVia") + " = " + conf.getString("SQL.T_VI"
                + "A.provincia") + " AND " + conf.getString("SQL.T_DNN.idMunicip"
                + "ioDVia") + " = " + conf.getString("SQL.T_VIA.municipio") + " "
                + "AND " + conf.getString("SQL.T_DNN.codigoVia") + " = "
                + conf.getString("SQL.T_VIA.identificador");
        joins[4] = "INNER";
        joins[5] = "T_TVI";
        joins[6] = conf.getString("SQL.T_VIA.tipo") + " = "
                + conf.getString("SQL.T_TVI.codigo");
        joins[7] = "INNER";
        joins[8] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
        joins[9] = conf.getString("SQL.T_DNN.idPaisD") + " = "
                + conf.getString("SQL.T_MUN.idPais") + " AND "
                + conf.getString("SQL.T_DNN.idProvinciaD") + " = "
                + conf.getString("SQL.T_MUN.idProvincia") + " AND "
                + conf.getString("SQL.T_DNN.idMunicipioD") + " = "
                + conf.getString("SQL.T_MUN.idMunicipio");
        joins[10] = "false";
        String calle2 = "''";
        try {
            calle2 = oad.join(parteFrom, parteWhere, joins);
        } catch (Exception bde) {
            if (log.isDebugEnabled()) {
                log.error("Error en calle2: " + bde.toString());
            }
        }
        concat[0] = conf.getString("SQL.T_TVI.abreviatura");
        concat[1] = "' '";
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = conf.getString("SQL.T_DNN.domicilio");
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = "' '";
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        nulo[0] = oad.convertir(conf.getString("SQL.T_DNN.numDesde"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
        concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = "' '";
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        nulo[0] = conf.getString("SQL.T_DNN.letraDesde");
        concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = "' '";
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        nulo[0] = oad.convertir(conf.getString("SQL.T_DNN.numHasta"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
        concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = "' '";
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        nulo[0] = conf.getString("SQL.T_DNN.letraHasta");
        concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);

        // CONCATENAR LA POBLACION
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = "' '";
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = conf.getString("SQL.T_MUN.nombre");

        parteFrom = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        parteWhere = conf.getString("SQL.T_DNN.idDomicilio") + " = "
                + conf.getString("SQL.R_RES.domicTercero");
        joins = new String[8];
        joins[0] = "T_DNN";
        joins[1] = "INNER";
        joins[2] = "T_TVI";
        joins[3] = conf.getString("SQL.T_DNN.idTipoVia") + " = "
                + conf.getString("SQL.T_TVI.codigo");
        joins[4] = "INNER";
        joins[5] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
        joins[6] = conf.getString("SQL.T_DNN.idPaisD") + " = "
                + conf.getString("SQL.T_MUN.idPais") + " AND "
                + conf.getString("SQL.T_DNN.idProvinciaD") + " = "
                + conf.getString("SQL.T_MUN.idProvincia") + " AND "
                + conf.getString("SQL.T_DNN.idMunicipioD") + " = "
                + conf.getString("SQL.T_MUN.idMunicipio");
        joins[7] = "false";
        String calle3 = "''";
        try {
            calle3 = oad.join(parteFrom, parteWhere, joins);
        } catch (Exception bde) {
            if (log.isDebugEnabled()) {
                log.error("Error en calle3: " + bde.toString());
            }
        }
        concat[0] = conf.getString("SQL.T_DNN.domicilio");
        concat[1] = "' '";
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        nulo[0] = oad.convertir(conf.getString("SQL.T_DNN.numDesde"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
        concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = "' '";
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        nulo[0] = conf.getString("SQL.T_DNN.letraDesde");
        concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = "' '";
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        nulo[0] = oad.convertir(conf.getString("SQL.T_DNN.numHasta"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
        concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = "' '";
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        nulo[0] = conf.getString("SQL.T_DNN.letraHasta");
        concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);

        // CONCATENAR LA POBLACION
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = "' '";
        concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
        concat[1] = conf.getString("SQL.T_MUN.nombre");

        String calle4 = "SELECT " + oad.funcionCadena(
                AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat) + " AS CALLE4 FROM T_DNN," + GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN WHER"
                + "E " + conf.getString("SQL.T_DNN.idDomicilio") + " = "
                + conf.getString("SQL.R_RES.domicTercero") + " AND "
                + conf.getString("SQL.T_DNN.idPaisD") + " = "
                + conf.getString("SQL.T_MUN.idPais") + " AND "
                + conf.getString("SQL.T_DNN.idProvinciaD") + " = "
                + conf.getString("SQL.T_MUN.idProvincia") + " AND "
                + conf.getString("SQL.T_DNN.idMunicipioD") + " = "
                + conf.getString("SQL.T_MUN.idMunicipio");

        parteFrom =
                oad.convertir(conf.getString("SQL.R_DIL.fecha"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHAANOTACION," + conf.getString("SQL.R_DIL.anotacion") + " AS ANOTACION, " + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHA_ORDE," + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHA," + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "HH24:MI") + " AS HORA," + oad.convertir(conf.getString("SQL.R_RES.fechaDocumento"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHADOCUMENTO," + oad.convertir(conf.getString("SQL.R_RES.fechaDocumento"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "HH24:MI") + " AS HORADOCUMENTO," +" RES_EJE AS EJERCICIO, "+ conf.getString("SQL.R_RES.numeroAnotacion") + " AS NUM," + conf.getString("SQL.R_RES.asunto") + " AS ASUNTO," + conf.getString("SQL.R_RES.tipoReg") + " AS TIPO,";
        parteFrom += "HTE_NOM AS NOMBRE, HTE_AP1 AS APELLIDO1, HTE_AP2 AS APELLIDO2, HTE_DOC AS DOCUMENTO, ";


        parteFrom += conf.getString("SQL.A_UOR.nombre") + " AS DESTINO, ";

        parteFrom += conf.getString("SQL.R_RES.estAnot") + " AS ESTADO"
                + "," + conf.getString("SQL.R_RES.diligencia") + " AS DILIGENCIA"
                + ",(" + calle1 + ") AS CALLE1,(" + calle2 + ") AS CALLE2,("
                + calle3 + ") AS CALLE3,(" + calle4 + ") AS CALLE4," + oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, new String[]{conf.getString("SQL.R_TTR.descripcion"), "'-'"}) + "as TRANSPORTE, "
                + "'" + nombreLargo + "' AS TIPONOMBRE";

        /** ORIGINAL 
        parteWhere = conf.getString("SQL.R_RES.tipoReg") + " = '"
                + tipoEntrada + "' " + " AND " + conf.getString("SQL.R_RES.cod"
                + "Unidad") + " = " + codUnidadRegistro
                + " AND " + conf.getString("SQL.R_RES.ejercicio") + "=" + ejercicio;
                **/ 
       
        parteWhere = conf.getString("SQL.R_RES.tipoReg") + " ='" + tipoEntrada + "' "; 
        

        if ((fecha_desde != null) && (!fecha_desde.equals(""))) {
            parteWhere += " AND "
                    + oad.convertir(oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, null) + " >= "
                    + oad.convertir("'" + fecha_desde + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, null);
        }

        if ((fecha_hasta != null) && (!fecha_hasta.equals(""))) {
            parteWhere += " AND "
                    + oad.convertir(oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, null) + " <= "
                    + oad.convertir("'" + fecha_hasta + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, null);
        }

        joins = new String[14];
        joins[0] = "R_RES";
        joins[1] = "LEFT";
        joins[2] = "R_TTR";
        joins[3] = "res_ttr=ttr_ide";
        joins[4] = "INNER";
        joins[5] = "T_HTE";
        joins[6] = conf.getString("SQL.R_RES.codTercero") + " = "
                + conf.getString("SQL.T_HTE.identificador") + " AND "
                + conf.getString("SQL.R_RES.modifInteresado") + " = "
                + conf.getString("SQL.T_HTE.version");
        joins[7] = "LEFT";


        joins[8] = "A_UOR";
        joins[9] = conf.getString("SQL.R_RES.unidOrigDest") + " = "
                + conf.getString("SQL.A_UOR.codigo");

        joins[10] = "LEFT";
        joins[11] = "R_DIL";
        if (params[0].equals("sqlserver") || params[0].equals("SQLSERVER")) {
            joins[12] = conf.getString("SQL.R_RES.codUnidad") + " = " + conf.getString("SQL.R_DIL.codUnidad")
                    + " AND " + conf.getString("SQL.R_RES.tipoReg") + " = " + conf.getString("SQL.R_DIL.tipo")
                    + " AND " + oad.convertir(oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null) + " = "
                    + oad.convertir(oad.convertir(conf.getString("SQL.R_DIL.fecha"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null);
        } else {
            joins[12] = conf.getString("SQL.R_RES.codUnidad") + " = " + conf.getString("SQL.R_DIL.codUnidad")
                    + " AND " + conf.getString("SQL.R_RES.tipoReg") + " = " + conf.getString("SQL.R_DIL.tipo")
                    + " AND " + oad.convertir(oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null) + " = "
                    + conf.getString("SQL.R_DIL.fecha");
        }
        joins[13] = "false";

        try {//esta es la que me hace falta
            if(esRaiz)
            {
               parteWhere+= " AND (" + conf.getString("SQL.R_RES.unidOrigD"
                    + "est") + " = " + codigoUnidad+")";
            }
            else
            {
            parteWhere += " AND (" + conf.getString("SQL.R_RES.unidOrigD"
                    + "est") + " = " + codigoUnidad + " OR " + conf.getString("SQL.R_RES.unidOrigDest")
                    + " IN (select UOR_COD FROM A_UOR start with UOR_PAD =" + codigoUnidad + " connect by prior UOR_COD = UOR_PAD))";
            }
            sql = oad.join(parteFrom, parteWhere, joins);

            if (estado != null && !"-1".equals(estado)) {
                sql = sql + " AND RES_EST=" + estado;
            }

            /**
            if (ofiDestinoLaDelUsuario || ofiOrigenLaDelUsuario) {
                
                                
                sql = sql + "AND EXISTS(SELECT UOR_COD AS NUM "
                        + "FROM A_UOR, " + GlobalNames.ESQUEMA_GENERICO + "A_ORG," + GlobalNames.ESQUEMA_GENERICO + "A_UOU "
                        + "WHERE UOU_ORG=" + usuario.getOrgCod() + " AND UOU_ENT=" + usuario.getEntCod()                 
                        + "AND UOR_COD=" + codOficinaOrigen + " AND OFICINA_REGISTRO=1 "
                        + "AND UOU_UOR= UOR_COD	AND UOU_ORG	= ORG_COD AND (UOR_TIP IS NULL OR UOR_TIP=0))"; 
            }*/
            
            if(codOficinaOrigen!=null && !"".equals(codOficinaOrigen) && !"-1".equals(codOficinaOrigen)){
                sql+=" AND RES_OFI=" + codOficinaOrigen;
            }

            sql += " order by res_num";

        } catch (Exception bde) {
            if (log.isDebugEnabled()) {
                log.error("Error en el join: " + bde.toString());
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("sql consulta_fecha:" + sql);
        }
        return sql;
    }

    /**** SOPORTE A OFICINAS DE REGISTRO *****/
    
    public ArrayList<String> getListaCodigosUorsDestinoTodasOficinas(String[] params){
        ArrayList<String> lista = new ArrayList<String>();
        
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            lista = AnotacionOficinaRegistroDAO.getInstance().getListaCodigosUorsDestinoTodasOficinas(con,params[6]);
            
        }catch(Exception e){
            log.error(" Error : " + e.getMessage());
        }finally{
            
            try{                
                if(con!=null) con.close();
            }catch(SQLException e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return lista;
    }
    
}