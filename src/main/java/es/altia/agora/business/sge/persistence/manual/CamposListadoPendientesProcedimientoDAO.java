package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.administracion.mantenimiento.CampoDesplegableVO;
import es.altia.agora.business.administracion.mantenimiento.CamposListadoParametrizablesProcedimientoVO;
import es.altia.agora.business.administracion.mantenimiento.CriterioBusquedaPendientesVO;
import es.altia.agora.business.administracion.mantenimiento.TipoDocumentoVO;
import es.altia.agora.business.sge.CampoListadoPendientesProcedimientoVO;
import es.altia.agora.business.sge.CampoSuplementarioVO;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.util.commons.DateOperations;
import es.altia.util.commons.NumericOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import org.apache.log4j.Logger;
import java.util.Hashtable;


public class CamposListadoPendientesProcedimientoDAO {

    private static CamposListadoPendientesProcedimientoDAO instance=null;
    private Logger log = Logger.getLogger(CamposListadoPendientesProcedimientoDAO.class);

    private CamposListadoPendientesProcedimientoDAO(){
    }

    public static CamposListadoPendientesProcedimientoDAO getInstance(){
        if(instance==null)
            instance = new CamposListadoPendientesProcedimientoDAO();

        return instance;
    }


    /**
     * Comprueba si un procedimiento tiene asignada una vista de expedientes pendientes propia
     * @param codProcedimiento: Código del procedimiento
     * @param codMunicipio: Código del municipio
     * @param con: Conexión a la BBDD
     * @return Un boolean
     */
    public boolean tieneProcedimientoVistaExpedientesPendientes(String codProcedimiento,int codMunicipio,Connection con){
        boolean exito = false;
        Statement st = null;
        ResultSet rs = null;

        try{
           String sql = "SELECT COUNT(*) AS NUM FROM CAMPOS_LIST_PENDIENTES_PROC WHERE CODIGO_PROCEDIMIENTO='" + codProcedimiento + "' AND "
                      + " CODIGO_MUNICIPIO=" + codMunicipio;
           log.debug("tieneProcedimientoVistaPendientes " + sql);
           st = con.createStatement();
           rs = st.executeQuery(sql);
           rs.next();
           int num = rs.getInt("NUM");
           log.debug("numero :: " + num);
           if(num>=1) exito = true;

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }// finally

        return exito;
    }



  /**
     * Comprueba si un campo que pertenece a listado de expedientes pendientes ya se encuentra entre los campos seleccionados de la vista
     * de expedientes pendientes de un procedimiento
     * @param codProcedimiento: Código del procedimiento
     * @param codMunicipio: Código del municipio
     * @param: codCampo: Código del campo
     * @param: nombreCampo: Nombre del campo
     * @param con: Conexión a la BBDD
     * @return Un boolean  
    private boolean estaCampoSeleccionado(String codProcedimiento,String codMunicipio,String codCampo,String nombreCampo,Connection con){
        boolean exito = false;
        Statement st = null;
        ResultSet rs = null;

        try{
           String sql = "SELECT COUNT(*) AS NUM FROM CAMPOS_LIST_PENDIENTES_PROC WHERE CODIGO_PROCEDIMIENTO='" + codProcedimiento + "' AND "
                      + " CODIGO_MUNICIPIO=" + codMunicipio + " AND CODIGO='" + codCampo + "' AND NOMBRE_CAMPO='" + nombreCampo + "'";
           log.debug("estaCampoFijoSeleccionado " + sql);

           st = con.createStatement();
           rs = st.executeQuery(sql);
           rs.next();
           int num = rs.getInt("NUM");
           log.debug("numero :: " + num);
           if(num>=1) exito = true;

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }// finally

        return exito;
    }*/




  /**
     * Recupera el contenido de un determinado campo seleccionado perteneciente al listado de exp. pendientes de un procedimiento
     * @param codProcedimiento: Código del procedimiento
     * @param codMunicipio: Código del municipio
     * @param: codCampo: Código del campo
     * @param: nombreCampo: Nombre del campo
     * @param con: Conexión a la BBDD
     * @return CampoListadoPendientesProcedimientoVO  */
    public CampoListadoPendientesProcedimientoVO getCampoSeleccionado(String codProcedimiento,String codMunicipio,String codCampo,String nombreCampo,Connection con){
        CampoListadoPendientesProcedimientoVO campo =null;
        Statement st = null;
        ResultSet rs = null;

        try{
           String sql = "SELECT CODIGO,NOMBRE_CAMPO,TAMANHO,ORDEN,ACTIVO FROM CAMPOS_LIST_PENDIENTES_PROC WHERE CODIGO_PROCEDIMIENTO='" + codProcedimiento + "' AND "
                      + " CODIGO_MUNICIPIO=" + codMunicipio + " AND CODIGO='" + codCampo + "' AND NOMBRE_CAMPO='" + nombreCampo + "'";
           log.debug("getCampoSeleccionado " + sql);

           st = con.createStatement();
           rs = st.executeQuery(sql);
           while(rs.next()){
               campo = new CampoListadoPendientesProcedimientoVO();
               campo.setCodigo(rs.getString("CODIGO"));
               campo.setNombreCampo(rs.getString("NOMBRE_CAMPO"));
               campo.setTamanho(rs.getString("TAMANHO"));
               campo.setActivo(rs.getString("ACTIVO"));
               campo.setOrden(rs.getString("ORDEN"));
           }           

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }// finally

        return campo;
    }


  /**
     * Recupera los campos de la vista de expedientes pendientes que todavía están disponibles
     * @param codProcedimiento: Código del procedimiento
     * @param codMunicipio: Código del municipio
     * @param con: Conexión a la BBDD
     * @return ArrayList<CampoListadoPendientesProcedimientoVO>
     */
    public ArrayList<CampoListadoPendientesProcedimientoVO> getCamposDisponibles(String codProcedimiento,String codMunicipio,Connection con){
        ArrayList<CampoListadoPendientesProcedimientoVO> disponibles = new ArrayList<CampoListadoPendientesProcedimientoVO>();
        Statement st = null;
        ResultSet rs = null;
        
        log.debug("getCamposDisponibles codProcedimiento: " + codProcedimiento + ", codMunicipio: " + codMunicipio);
        try{

            if(codProcedimiento!=null && codProcedimiento.length()>0 && codMunicipio!=null && codMunicipio.length()>0)
            {
                /** SE RECUPERAN LOS CAMPOS FIJOS QUE DEL LISTADO DE EXPEDIENTES PENDIENTES **/
                String sql = "SELECT CAMPLIST_COD,CAMPLIST_NOM " +
                             "FROM A_CAMPLIST WHERE CAMPLIST_CODLIST=2";

                log.debug("getCamposDisponibles " + sql);
                st = con.createStatement();
                rs = st.executeQuery(sql);

                while(rs.next()){
                    CampoListadoPendientesProcedimientoVO fijo = new CampoListadoPendientesProcedimientoVO();
                    String codigo = rs.getString("CAMPLIST_COD");
                    String nombre = rs.getString("CAMPLIST_NOM");

                    CampoListadoPendientesProcedimientoVO aux = this.getCampoSeleccionado(codProcedimiento, codMunicipio, codigo, nombre, con);
                    fijo.setCodigo(codigo);
                    fijo.setNombreCampo(nombre);
                    fijo.setCampoSuplementario(false);
                    fijo.setDescripcionCampoSuplementario("");
                    fijo.setCodMunicipio(codMunicipio);
                    fijo.setCodProcedimiento(codProcedimiento);

                    if(aux!=null){
                        fijo.setTamanho(aux.getTamanho());
                        fijo.setOrden(aux.getOrden());
                        if(aux.getActivo()!=null && "0".equals(aux.getActivo()))
                            fijo.setActivo("NO");
                        else
                            fijo.setActivo("SI");
                    }
                    disponibles.add(fijo);

                }// while

                log.debug("Número de campos fijos recuperados: " + disponibles.size());
                st.close();
                rs.close();

                /** SE RECUPERAN LOS CAMPOS SUPLEMENTARIOS NUMÉRICOS, FECHA, TEXTO CORTO Y DESPLEGABLE QUE NO ESTÉN YA ENTRE LOS CAMPOS SELECCIONADOS **/
                sql = "SELECT PCA_MUN,PCA_COD,PCA_ROT,PCA_DES,PCA_DESPLEGABLE,PCA_TAM,PCA_NOR " +
                      "FROM E_PCA,E_PLT WHERE E_PCA.PCA_PLT = E_PLT.PLT_COD AND PCA_PRO='" + codProcedimiento + "' AND PCA_MUN="
                      + codMunicipio + " AND PCA_TDA IN (1,2,3,4,6) AND PCA_ACTIVO='SI'";

                log.debug("getCamposDisponibles " + sql);

               st = con.createStatement();
               rs = st.executeQuery(sql);
               while(rs.next()){
                   String codigo      = rs.getString("PCA_COD");
                   String nombre      = rs.getString("PCA_ROT");

                   log.debug("codigo: " + codigo + " nombre: " + nombre);

                   CampoListadoPendientesProcedimientoVO aux = this.getCampoSeleccionado(codProcedimiento, codMunicipio, codigo, nombre, con);
                   CampoListadoPendientesProcedimientoVO campo = new CampoListadoPendientesProcedimientoVO();
                   campo.setCodigo(codigo);
                   campo.setCodMunicipio(codMunicipio);
                   campo.setCodProcedimiento(codProcedimiento);
                   campo.setDescripcionCampoSuplementario(rs.getString("PCA_DES"));
                   campo.setCampoSuplementario(true);
                   campo.setNombreCampo(nombre);

                   if(aux!=null){
                       if(aux.getActivo()!=null && "SI".equals(aux.getActivo())){
                           campo.setActivo("SI");
                       }else
                        campo.setActivo("NO");

                       campo.setTamanho(aux.getTamanho());
                       campo.setOrden(aux.getOrden());
                   }

                   disponibles.add(campo);

               }// while
            }//if
           
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }// finally

        return disponibles;
        
    }// getCamposDisponibles


  /**
     * Recupera los campos que forman parte de la vista de expedientes pendientes del procedimiento
     * @param codProcedimiento: Código del procedimiento
     * @param codMunicipio: Código del municipio
     * @param con: Conexión a la BBDD
     * @return ArrayList<CampoListadoPendientesProcedimientoVO>
     */
    public ArrayList<CampoListadoPendientesProcedimientoVO> getCamposSeleccionados(String codProcedimiento,String codMunicipio,Connection con){
        ArrayList<CampoListadoPendientesProcedimientoVO> seleccionados = new ArrayList<CampoListadoPendientesProcedimientoVO>();
        Statement st = null;
        ResultSet rs = null;

        log.debug("getCamposSeleccionados codProcedimiento: " + codProcedimiento + ", codMunicipio: " + codMunicipio);
        try{

            /** SE RECUPERAN LOS CAMPOS QUE FORMAN PARTE DE LA VISTA DE EXPEDIENTES PENDIENTES DEL PROCEDIMIENTO **/
            String sql = "SELECT CODIGO,NOMBRE_CAMPO,TAMANHO,ACTIVO,ORDEN,CAMPO_SUP " +
                         "FROM CAMPOS_LIST_PENDIENTES_PROC " +
                         "WHERE CODIGO_PROCEDIMIENTO='" + codProcedimiento + "' AND CODIGO_MUNICIPIO=" + codMunicipio +
                         " ORDER BY ORDEN ASC";
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){
                CampoListadoPendientesProcedimientoVO campo = new CampoListadoPendientesProcedimientoVO();
                campo.setCodigo(rs.getString("CODIGO"));                
                campo.setNombreCampo(rs.getString("NOMBRE_CAMPO"));
                campo.setTamanho(rs.getString("TAMANHO"));
                campo.setActivo(rs.getString("ACTIVO"));
                campo.setOrden(rs.getString("ORDEN"));
                campo.setCodMunicipio(codMunicipio);
                campo.setCodProcedimiento(codProcedimiento);
                int campoSup = rs.getInt("CAMPO_SUP");
                campo.setCampoSuplementario(false);
                if(campoSup==1) campo.setCampoSuplementario(true);                
                seleccionados.add(campo);
            }


        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }// finally

        return seleccionados;

    }// getCamposSeleccionados



    /**
     * Almacena los campos de la vista de exp. pendientes de un procedimiento en base de datos
     * @param campos: Colección con los campos a insertar
     * @param codProcedimiento: Código del procedimiento
     * @param codMunicipio: Código del municipio
     * @param con: Conexión a la base de datos
     * @return true si todo bien y false en caso contrario
     */
    public boolean guardarCampos(ArrayList<CampoListadoPendientesProcedimientoVO> campos,String codProcedimiento,int codMunicipio,Connection con){
        boolean exito = false;
        PreparedStatement ps  = null;
        ResultSet rs  = null;

        try{

            int contador = 0;
                        
            log.debug("Código del procedimiento: " + codProcedimiento + ", codMunicipio: " + codMunicipio);

            // Se eliminan todos los campos de la vista de pendientes del procedimiento para volver a insertarlos a continuación
            String sql = "DELETE FROM CAMPOS_LIST_PENDIENTES_PROC WHERE CODIGO_PROCEDIMIENTO=? AND CODIGO_MUNICIPIO=?";
            log.debug("guardarCampos: " + sql);
            int j= 1;
            ps = con.prepareStatement(sql);
            ps.setString(j++, codProcedimiento);
            ps.setInt(j++,codMunicipio);

            int rowsDeleted = ps.executeUpdate();
            log.debug("guardarCampos número de registros eliminados: " + rowsDeleted);
            ps.close();

            //if(rowsDeleted>0){
                
                for(int i=0;i<campos.size();i++){
                    CampoListadoPendientesProcedimientoVO campo = campos.get(i);
                    // Se inserta cada campo de la vista de pendientes
                    sql = "INSERT INTO CAMPOS_LIST_PENDIENTES_PROC(CODIGO,CODIGO_PROCEDIMIENTO,CODIGO_MUNICIPIO,NOMBRE_CAMPO,TAMANHO,ACTIVO,ORDEN,CAMPO_SUP) " +
                                 "VALUES(?,?,?,?,?,?,?,?)";

                    log.debug("guardarCampos: " + sql);
                    j = 1;
                    ps = con.prepareStatement(sql);
                    ps.setString(j++,campo.getCodigo());
                    ps.setString(j++,codProcedimiento);
                    ps.setInt(j++,codMunicipio);
                    ps.setString(j++,campo.getNombreCampo());
                    ps.setInt(j++,Integer.parseInt(campo.getTamanho()));
                    ps.setInt(j++,1); // el campo está activo
                    ps.setInt(j++,Integer.parseInt(campo.getOrden()));
                    if(campo.isCampoSuplementario())
                        ps.setInt(j++,1);
                    else
                        ps.setInt(j++,0);

                    int rowsInserted = ps.executeUpdate();
                    if(rowsInserted>=1) contador++;
                    log.debug("guardarCampos filas insertada: " + rowsInserted);
                }//for
            //}

            log.debug("Número de campos insertados " + contador);
            if(contador==campos.size()){
                exito = true;              
            }

        }catch(SQLException e){
            e.printStackTrace();            
        }finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }// finally

        return exito;
    }


  /**
     * Comprueba si un campo suplementario de un procedimiento es de tipo texto
     * @param codCampo: Código del camop
     * @param codMunicipioo: Código del municipio
     * @param codProcedimiento: Código del procedimiento
     * @param conexion: Conexión a la base de datos
     * @return Devuelve -1 => si no es de tipo texto
     *                   0 -> si es de tipo texto corto
     *                   1 -> si es de tipo texto largo
     */
    private int esCampoSuplementarioTexto(String codCampo,int codMunicipio,String codProcedimiento,Connection conexion){
        int salida = -1;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";

        try{

          // Creamos la select con los parametros adecuados.
          sql = "SELECT PCA_TDA FROM E_PCA " +
                "WHERE PCA_PRO='" + codProcedimiento + "' AND PCA_MUN=" + codMunicipio + " AND PCA_COD='" + codCampo + "'";

          if(log.isDebugEnabled()) log.debug(sql);
          stmt = conexion.createStatement();
          rs = stmt.executeQuery(sql);
          int num = 0;
          while(rs.next()){
            num = rs.getInt("PCA_TDA");
            if(num==ConstantesDatos.TIPO_DATO_TEXTO_CORTO_CAMPO_SUPLEMENTARIO){
                salida = 0;
            }else
            if(num==ConstantesDatos.TIPO_DATO_TEXTO_LARGO_CAMPO_SUPLEMENTARIO){
                salida = 1;
            }else
                salida = -1;
          }//while
          
        }catch (Exception e){
            e.printStackTrace();
            log.debug(e.getMessage());
        }finally{
            try{
                if(stmt!=null) stmt.close();
                if(rs!=null) rs.close();
                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return salida;
    }


    /**
     * Recupera los campos del listado de expedientes pendientes de un determinado procedimiento
     * @param codProcedimiento: Código del procedimiento
     * @param codMunicipio: Código del municipio
     * @param codIdioma: Código del idioma
     * @param conexion: Conexión a la BBDD
     * @return Vector<CamposListadosParametrizablesVO>     
     */
    public Vector<CamposListadoParametrizablesProcedimientoVO> getCamposListado(String codProcedimiento,int codMunicipio,int codIdioma,Connection conexion){

        Vector<CamposListadoParametrizablesProcedimientoVO> resultado = new Vector<CamposListadoParametrizablesProcedimientoVO>();
         PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        
        try{

          // Creamos la select con los parametros adecuados.
            sql = "SELECT CODIGO,NOMBRE_CAMPO,TAMANHO,ACTIVO,ORDEN,CAMPO_SUP,PCA_TDA  FROM CAMPOS_LIST_PENDIENTES_PROC "
                    + "left join e_pca on (PCA_PRO=CODIGO_PROCEDIMIENTO and PCA_MUN=CODIGO_MUNICIPIO and PCA_COD=CODIGO) "
                    + " WHERE CODIGO_PROCEDIMIENTO=? AND CODIGO_MUNICIPIO=? "
                    + "ORDER BY ORDEN ASC";

          if(log.isDebugEnabled()) log.debug(sql);
          
            stmt = conexion.prepareStatement(sql);
            int i=1;            
            stmt.setString(i++,codProcedimiento);           
            stmt.setInt(i++,codMunicipio);
            rs = stmt.executeQuery();
          
          while(rs.next()){
            CamposListadoParametrizablesProcedimientoVO gVOCampos = new CamposListadoParametrizablesProcedimientoVO();
            String codigo = rs.getString("CODIGO");
            String nombre = rs.getString("NOMBRE_CAMPO");
            
            gVOCampos.setCodCampo(codigo);
            gVOCampos.setNomCampo(nombre);
            int act= rs.getInt("ACTIVO");
            String activo="";
            if(act==1){
                activo="SI";
            }else{
                activo="NO";
            }
            
            gVOCampos.setActCampo(activo);
            gVOCampos.setTamanoCampo(rs.getInt("TAMANHO"));
            int campoSup = rs.getInt("CAMPO_SUP");
            if(campoSup==0)
                gVOCampos.setCampoSuplementario(false);
            else
                gVOCampos.setCampoSuplementario(true);

            int tipoTexto = -1;
            if(campoSup==0){
                gVOCampos.setOrdenCampo(Integer.toString(this.getOrdenCampoFijo(Integer.parseInt(codigo),ConstantesDatos.CODIGO_TIPO_LISTADO_EXPEDIENTES_PENDIENTES,nombre,conexion)));
                String etiqueta = this.getEtiquetaCampoFijo(Integer.parseInt(codigo),ConstantesDatos.CODIGO_TIPO_LISTADO_EXPEDIENTES_PENDIENTES,codIdioma,conexion);
                log.debug(" **** La etiqueta del campo recuperado es " + etiqueta);
                gVOCampos.setEtiquetaIdioma(etiqueta);
            }else{
                // Si el campo del listado es un campo suplementario                
                int num=0;
                num = rs.getInt("PCA_TDA");
                if (num == ConstantesDatos.TIPO_DATO_TEXTO_CORTO_CAMPO_SUPLEMENTARIO) {
                    tipoTexto = 0;
                } else if (num == ConstantesDatos.TIPO_DATO_TEXTO_LARGO_CAMPO_SUPLEMENTARIO) {
                    tipoTexto = 1;
                } else {
                    tipoTexto = -1;
                }
                
                if(tipoTexto==1 || tipoTexto==0){
                    // el campo es de tipo texto cor
                    if(tipoTexto==0){ // texto corto => Admite orden
                        gVOCampos.setOrdenCampo(codigo);
                    }
                    else
                    if(tipoTexto==1){ // texto largo => No admite orden
                        gVOCampos.setOrdenCampo("0");
                    }
                    gVOCampos.setCampoTexto(true);
                }
                else{
                   gVOCampos.setOrdenCampo(codigo);
                   gVOCampos.setCampoTexto(false);
                }                

                gVOCampos.setEtiquetaIdioma(nombre);
                
            }// else

          
            resultado.add(gVOCampos);
          }
          
        }catch (Exception e){
            e.printStackTrace();
            log.debug(e.getMessage());
        }finally{
            try{
                if(stmt!=null) stmt.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return resultado;
    }



    /**
     * Recupera la etiqueta de idioma de un campo fijo
     * @param codigo: Código del campo
     * @param tipoListado: Tipo de listado
     * @param codIdioma: Código del idioma
     * @param conexion: Conexión a la BBDD
     * @return String o null si no se ha podido recuperar
     */
    public String getEtiquetaCampoFijo(int codigo,int tipoListado,int codIdioma,Connection conexion){

        String texto = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";

        try{

          // Creamos la select con los parametros adecuados.
          sql = "SELECT ETIQUETA FROM ETIQUETAS_LISTADO_PENDIENTES " +
                "WHERE CODIGO=" + codigo + " AND TIPO_LISTADO=" + tipoListado; 
          if(log.isDebugEnabled()) log.debug(sql);

          stmt = conexion.createStatement();
          rs = stmt.executeQuery(sql);
          String etiqueta = null;
          while(rs.next()){
            etiqueta = rs.getString("ETIQUETA");
          }

          log.debug("Campo de código " + codigo + " tiene como etiqueta: " + etiqueta);
          if(etiqueta!=null && etiqueta.length()>0 && !"".equals(etiqueta)){
              TraductorAplicacionBean traductor = new TraductorAplicacionBean();
              traductor.setApl_cod(ConstantesDatos.APP_GESTION_EXPEDIENTES);
              traductor.setIdi_cod(codIdioma);
              // Se recupera el texto traducido correspondiente a la etiqueta
              texto = traductor.getDescripcion(etiqueta);
              log.debug("Campo de código " + codigo + " tiene como etiqueta: " + etiqueta);
          }

        }catch (Exception e){
            e.printStackTrace();
            log.debug(e.getMessage());
        }finally{
            try{
                if(stmt!=null) stmt.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return texto;
    }



  /**
     * Recupera el orden que tiene un campo fijo del listado de expedientes
     * @param codigo: Código del campo
     * @param tipoListado: Tipo de listado. Para el listado de expedientes pendientes debe tomar el valor 2
     * @param nombre: Nombre del campo
     * @param conexion: Conexión a la BBDD
     * @return El orden que le corresponde al campo en la consulta SQL o cero sino tiene orden. Esto indica que los resultados no se pueden ordenar
     */
    public int getOrdenCampoFijo(int codigo,int tipoListado,String nombre,Connection conexion){
        int orden =0;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try{

          // Creamos la select con los parametros adecuados.
          sql = "SELECT CAMPLIST_ORDEN FROM A_CAMPLIST " +
                "WHERE CAMPLIST_COD=" + codigo + " AND CAMPLIST_NOM='" + nombre + "' AND CAMPLIST_CODLIST=" + tipoListado;
          if(log.isDebugEnabled()) log.debug(sql);

          stmt = conexion.createStatement();
          rs = stmt.executeQuery(sql);          
          while(rs.next()){
            orden = rs.getInt("CAMPLIST_ORDEN");
          }

        }catch (Exception e){
            e.printStackTrace();
            log.debug(e.getMessage());
        }finally{
            try{
                if(stmt!=null) stmt.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return orden;
    }



  /**
     * Recupera los campos suplementarios que forman parte del listado de expedientes pendientes de un determinado procedimiento
     * @param codProcedimiento: Código del procedimiento
     * @param codMunicipio: Código del municipio
     * @param codIdioma: Código del idioma
     * @param conexion: Conexión a la BBDD
     * @return Vector<CampoSuplementarioVO>
     */
    public Vector<CampoSuplementarioVO> getCamposSuplementariosListado(String codProcedimiento,int codMunicipio,Connection conexion){

        Vector<CampoSuplementarioVO> resultado = new Vector<CampoSuplementarioVO>();
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";

        try{

          // Creamos la select con los parametros adecuados.
          sql = "SELECT CODIGO,NOMBRE_CAMPO,TAMANHO,ACTIVO,ORDEN,CAMPO_SUP,PCA_TDA " +
                "FROM CAMPOS_LIST_PENDIENTES_PROC,E_PCA WHERE " +
                "CODIGO_PROCEDIMIENTO='"+ codProcedimiento + "' AND CODIGO_MUNICIPIO=" + codMunicipio + " AND CAMPO_SUP=1 " +
                "AND CODIGO_PROCEDIMIENTO = PCA_PRO AND CODIGO_MUNICIPIO=PCA_MUN AND CODIGO=PCA_COD " +
                "ORDER BY CAMPOS_LIST_PENDIENTES_PROC.ORDEN ASC";

          if(log.isDebugEnabled()) log.debug(sql);
          stmt = conexion.createStatement();
          rs = stmt.executeQuery(sql);
          while(rs.next()){
            CampoSuplementarioVO campo = new CampoSuplementarioVO();            
            String codigo   = rs.getString("CODIGO");
            String nombre   = rs.getString("NOMBRE_CAMPO");
            String tipoDato = rs.getString("PCA_TDA");

            log.debug("codigo: " + codigo + " ,nombre: " + nombre + ", tipoDato: " + tipoDato);
            campo.setCodigo(codigo);
            campo.setRotulo(nombre);
            campo.setCodigoTipoDato(tipoDato);
            
            resultado.add(campo);                       
          }

        }catch (Exception e){
            e.printStackTrace();
            log.debug(e.getMessage());
        }finally{
            try{
                if(stmt!=null) stmt.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return resultado;
    }


  /**
     * Recupera los campos suplementarios que forman parte del listado de expedientes pendientes de un determinado procedimiento
     * @param numExpediente: Número del expediente
     * @param ejercicio: ejercicio
     * @param codMunicipio: Código del municipio
     * @param codigoCampo: Código del campo
     * @param tipoCampo: Tipo del campo
     * @param conexion: Conexión a la BBDD
     * @return Valor del campo en formato String
     */
    public String getValorCampoSuplementario(String numExpediente,String ejercicio,String codMunicipio,String codigoCampo,int tipoCampo,Connection conexion){
        String valor = "";
        Statement st = null;
        ResultSet rs = null;
        String sql = "";

        try{

            switch (tipoCampo){
               case 1:
                    // Campo numérico
                    sql ="SELECT TNU_VALOR AS VALOR FROM E_TNU WHERE TNU_MUN=" + codMunicipio + " AND TNU_EJE=" + ejercicio +
                         " AND TNU_NUM='" + numExpediente + "' AND TNU_COD='" + codigoCampo + "'";
                    break;
               case 2:
                    // Texto corto
                    sql ="SELECT TXT_VALOR AS VALOR FROM E_TXT WHERE TXT_MUN=" + codMunicipio + " AND TXT_EJE=" + ejercicio + " AND TXT_NUM='" + numExpediente + "' AND TXT_COD='" + codigoCampo + "'";
                    break;
               case 3:
                    // Fecha
                    sql ="SELECT TFE_VALOR AS VALOR FROM E_TFE WHERE TFE_MUN=" + codMunicipio + " AND TFE_EJE=" + ejercicio + " AND TFE_NUM='"
                        + numExpediente  + "' AND TFE_COD='" + codigoCampo + "'";
                    break;
               case 4:
                    // Texto largo
                    sql ="SELECT TTL_VALOR AS VALOR FROM E_TTL WHERE TTL_MUN=" + codMunicipio + " AND TTL_EJE=" + ejercicio
                       + " AND TTL_NUM='" + numExpediente + "' AND TTL_COD='" + codigoCampo + "'";
                    break;
               case 6:
                    // Campo desplegable
                    sql = "SELECT DES_NOM AS VALOR FROM E_TDE, E_PCA,E_DES_VAL " +
                           "WHERE TDE_MUN=" + codMunicipio + " AND TDE_EJE=" + ejercicio +
                           "AND TDE_NUM='" + numExpediente + "' AND TDE_COD='" + codigoCampo + "' AND PCA_MUN = TDE_MUN " +
                           "AND PCA_COD= TDE_COD AND PCA_DESPLEGABLE = E_DES_VAL.DES_COD " +
                           "AND TDE_VALOR = E_DES_VAL.DES_VAL_COD";
                    break;
               case 8:
                    // Campo numérico calculado
                    sql ="SELECT TNUC_VALOR AS VALOR FROM E_TNUC WHERE TNUC_MUN=" + codMunicipio + " AND TNUC_EJE=" + ejercicio +
                         " AND TNUC_NUM='" + numExpediente + "' AND TNUC_COD='" + codigoCampo + "'";
                    break;
               case 9:
                    // Fecha calculada
                    sql ="SELECT TFEC_VALOR AS VALOR FROM E_TFEC WHERE TFEC_MUN=" + codMunicipio + " AND TFEC_EJE=" + ejercicio + " AND TFEC_NUM='"
                        + numExpediente  + "' AND TFEC_COD='" + codigoCampo + "'";
                    break;
               case 10:
                    // despelgable externo
                    sql = "SELECT TDEX_VALOR AS VALOR FROM E_TDEX " +
                           "WHERE TDEX_MUN=" + codMunicipio + " AND TDEX_EJE=" + ejercicio +
                           "AND TDEX_NUM='" + numExpediente + "' AND TDEX_COD='" + codigoCampo+"'";
                    break;    
                    
           }// switch
          
          if(log.isDebugEnabled()) log.debug(sql);          
          st = conexion.createStatement();          
          rs = st.executeQuery(sql);

          while(rs.next()){
              if(tipoCampo==1 || tipoCampo==2 || tipoCampo==10){ // Númerico, texto corto, desplegable
                  valor = AdaptadorSQLBD.js_escape(rs.getString("VALOR"));
              }
              else
              if(tipoCampo==4){ // Texto largo
                java.io.Reader cr = rs.getCharacterStream("VALOR");
                if(cr==null){
                    valor = "";
                }
                else{
                    java.io.CharArrayWriter ot = new java.io.CharArrayWriter();
                    int c;
                    while ((c = cr.read())!= -1)
                    {
                        ot.write(c);
                    }
                    ot.flush();
                    // Se escapa el contenido del texto largo
                    valor = AdaptadorSQLBD.js_escape(ot.toString());
                    ot.close();
                    cr.close();
                    
                }// else                
              }
              else
              if(tipoCampo==3){
                  // Campo suplementario de tipo fecha
                  java.sql.Timestamp fecha = rs.getTimestamp("VALOR");
                  Calendar cFecha = DateOperations.toCalendar(fecha);
                  valor = DateOperations.toString(cFecha, "dd/MM/yyyy");
              }else
              if(tipoCampo==6){
                  //valor = AdaptadorSQLBD.js_escape(rs.getString("VALOR"));
                  valor = rs.getString("VALOR");
              }

          }// while

          log.debug("resultado de la consulta: " + valor);
          if(st!=null) st.close();
          if(rs!=null) rs.close();
          
        }catch (Exception e){
            e.printStackTrace();
            log.debug(e.getMessage());
        }finally{
            
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return valor;
    }
    
    
    
    
    
    
    
    
    public Hashtable<String,String> getValorCampoSuplementario2(String numExpediente,String ejercicio,String codMunicipio,Vector<CampoSuplementarioVO> camposSuplementarios,AdaptadorSQLBD adapt) throws BDException{
       
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "";
        String sqlAux = "";
        Vector codigosCampos = new Vector();
        int numeroConsultas=0;
         Connection conexion = null;
        conexion = adapt.getConnection();
        
        Hashtable<String,String> campos = new Hashtable<String,String>();
         Hashtable<String,String> camposReturn = new Hashtable<String,String>();
        
        try{
        
        for (int z = 0; camposSuplementarios != null && z < camposSuplementarios.size(); z++) {

            String codigoCampo = camposSuplementarios.get(z).getCodigo();
            String tipoDato = camposSuplementarios.get(z).getCodigoTipoDato();
            
            int tipoCampo=Integer.parseInt(tipoDato);
           
            
            switch (tipoCampo){
               case 1:
                    // Campo numérico
                    sqlAux ="SELECT 1 as tipo,TNU_COD AS codigo,"+adapt.convertir("TNU_VALOR", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null)+" AS VALOR FROM E_TNU WHERE TNU_MUN=? AND TNU_EJE=? " +
                         " AND TNU_NUM=? AND TNU_COD=? ";
                    codigosCampos.add(codigoCampo);
                    numeroConsultas++;
                    break;
               case 2:
                    // Texto corto
                    sqlAux ="SELECT 2 as tipo,TXT_COD AS codigo,"+adapt.convertir("TXT_VALOR", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null)+" AS VALOR FROM E_TXT WHERE TXT_MUN=? AND TXT_EJE=? "  + 
                            " AND TXT_NUM=? AND TXT_COD=? ";
                    codigosCampos.add(codigoCampo);
                    numeroConsultas++;
                    break;
               case 3:
                    // Fecha
                    sqlAux ="SELECT 3 as tipo,TFE_COD AS codigo,"+adapt.convertir("TFE_VALOR", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null)+" AS VALOR FROM E_TFE WHERE TFE_MUN=? AND TFE_EJE=? AND TFE_NUM=? "+
                      " AND TFE_COD=? ";
                    codigosCampos.add(codigoCampo);
                    numeroConsultas++;
                    break;
               case 4:
                    // Texto largo
                    sqlAux ="SELECT 4 as tipo,TTL_COD AS codigo,"+adapt.convertir("TTL_VALOR", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null)+" AS VALOR FROM E_TTL WHERE TTL_MUN=? AND TTL_EJE=? "
                       + " AND TTL_NUM=? AND TTL_COD=? ";
                    codigosCampos.add(codigoCampo);
                    numeroConsultas++;
                    break;
               case 6:
                    // Campo desplegable
                    sqlAux = "SELECT  6 as tipo, TDE_COD AS codigo,"+adapt.convertir("DES_NOM", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null)+" AS VALOR FROM E_TDE, E_PCA,E_DES_VAL " +
                           "WHERE TDE_MUN=? AND TDE_EJE=? "+
                           "AND TDE_NUM=? AND TDE_COD=? AND PCA_MUN = TDE_MUN " +
                           "AND PCA_COD= TDE_COD AND PCA_DESPLEGABLE = E_DES_VAL.DES_COD " +
                           "AND TDE_VALOR = E_DES_VAL.DES_VAL_COD";
                    codigosCampos.add(codigoCampo);
                    numeroConsultas++;
                    break;
               case 8:
                    // Campo numérico calculado
                    sqlAux ="SELECT  8 as tipo, TNUC_COD AS codigo,"+adapt.convertir("TNUC_VALOR", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null)+" AS VALOR FROM E_TNUC WHERE TNUC_MUN=? AND TNUC_EJE=? "+
                         " AND TNUC_NUM=? AND TNUC_COD=? ";
                    codigosCampos.add(codigoCampo);
                    numeroConsultas++;
                    break;
               case 9:
                    // Fecha calculada
                    sqlAux ="SELECT  9 as tipo, TFEC_COD AS codigo,"+adapt.convertir("TFEC_VALOR", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null)+" AS VALOR FROM E_TFEC WHERE TFEC_MUN=? AND TFEC_EJE=? AND TFEC_NUM="
                        + "? AND TFEC_COD=? ";
                     codigosCampos.add(codigoCampo);
                     numeroConsultas++;
                    break;
               case 10:
                    // despelgable externo
                    sqlAux = "SELECT  10 as tipo, TDEX_COD AS codigo,"+adapt.convertir("TDEX_VALOR", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null)+" AS VALOR FROM E_TDEX " +
                           "WHERE TDEX_MUN=? AND TDEX_EJE=? " +
                           "AND TDEX_NUM=? AND TDEX_COD=? ";
                    codigosCampos.add(codigoCampo);
                    numeroConsultas++;
                    break;    
                    
           }// switch
            

        if (z==0) sql=sqlAux;
        else sql=sql+" UNION ALL "+sqlAux;
            
        }
        
             

        
          
          if(log.isDebugEnabled()) log.debug(sql);          
           st = conexion.prepareStatement(sql);
            int i=1;  
            
            for(int k=0;k< numeroConsultas;k++){
            
            st.setInt(i++,Integer.parseInt(codMunicipio));
            st.setInt(i++,Integer.parseInt(ejercicio));
            st.setString(i++,numExpediente);
            st.setString(i++,(String)codigosCampos.get(k)); 
            
             log.debug("PARÁMETROS de la consulta: " + codMunicipio+ ","+ejercicio+", "+numExpediente+","+(String)codigosCampos.get(k));
            }
            
            rs = st.executeQuery();         
          
            int j=0;
            while (rs.next()) {
                
                //valor = AdaptadorSQLBD.js_escape(rs.getString("VALOR"));
                int tipoCampo = rs.getInt("tipo");
                String codigo = rs.getString("codigo");
                String valor = "";
                if (tipoCampo == 1 || tipoCampo == 2 || tipoCampo == 10) { // Númerico, texto corto, desplegable
                    valor = AdaptadorSQLBD.js_escape(rs.getString("VALOR"));
                } else if (tipoCampo == 4) { // Texto largo
                    java.io.Reader cr = rs.getCharacterStream("VALOR");
                    if (cr == null) {
                        valor = "";
                    } else {
                        java.io.CharArrayWriter ot = new java.io.CharArrayWriter();
                        int c;
                        while ((c = cr.read()) != -1) {
                            ot.write(c);
                        }
                        ot.flush();
                        // Se escapa el contenido del texto largo
                        valor = AdaptadorSQLBD.js_escape(ot.toString());
                        ot.close();
                        cr.close();

                    }// else                
                } else {
                    //valor = AdaptadorSQLBD.js_escape(rs.getString("VALOR"));
                    valor = rs.getString("VALOR");
                }
                if ((valor != null) && (!"NULL".equals(valor)) && (!"null".equals(valor))) {
                    campos.put(codigo, valor);
                  
                    log.debug("resultado de la consulta> " +codigo+": "+ valor);
                }  else  campos.put(codigo, " ");
                j++;
                
                

            }// while
            
            
            
            for (int z = 0; camposSuplementarios != null && z < camposSuplementarios.size(); z++) {

            String codigoCampo = camposSuplementarios.get(z).getCodigo();
                       
            String valor =campos.get(codigoCampo);
            
            if(valor!=null) camposReturn.put(codigoCampo, valor);
            else camposReturn.put(codigoCampo, "");
            
             }

         
          if(st!=null) st.close();
          if(rs!=null) rs.close();
          
        }catch (Exception e){ 
            e.printStackTrace();
            log.debug(e.getMessage());
        }finally{
            
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                adapt.devolverConexion(conexion);

            }catch(SQLException e){
                e.printStackTrace();
            }
            catch(BDException e){
                e.printStackTrace();
            }
        }
        
       
        return camposReturn;
    }


  /**
     * Recupera el tipo del campo suplementario
     * @param codCampo: Código del campo suplementario
     * @param codProcedimiento: Código del procedimiento
     * @param codMunicipio: Código del municipio
     * @param conexion: Conexión a la BBDD
     * @return Nombre de la tabla que contiene el valor del campo
     */
    public int getTipoCampoSuplementario(String codCampo,String codProcedimiento,int codMunicipio,Connection conexion){
        int valor=-1;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";

        try{
           
          sql = "SELECT PCA_TDA FROM E_PCA WHERE PCA_PRO='" + codProcedimiento.trim() + "' AND PCA_COD='" + codCampo.trim() + "' AND PCA_MUN=" + codMunicipio;          
          log.debug(sql);
          
          String tda = null;
          st = conexion.createStatement();
          rs = st.executeQuery(sql);          
          while(rs.next()){
            tda = rs.getString("PCA_TDA");
          }
         
          if(NumericOperations.isInteger(tda)){
              valor = Integer.parseInt(tda);
          }
          log.debug("Tipo campo suplementario " + tda);
          if(st!=null) st.close();
          if(rs!=null) rs.close();
          
        }catch (Exception e){
            e.printStackTrace();
            log.debug(e.getMessage());
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return valor;
    }



    /**
     * Recupera los campos suplementarios a nivel de procedimiento de los tipos numéricos, texto corto, desplegable y fecha. Los campos suplementarios
     * tienen que estar activos
     * @param codProcedimiento: Código del procedimiento
     * @param codMunicipio: Código del municipio
     * @param criterios: Lista en la que almacenar los campos. Previamente ya deberían contener
     * @param con: Conexión a la base de datos
     * @return ArrayList<CriterioBusquedaPendientesVO>
     */
    public ArrayList<CriterioBusquedaPendientesVO> getCamposSuplemProcCriterioBusqueda(String codProcedimiento,String codMunicipio,ArrayList<CriterioBusquedaPendientesVO> criterios,Connection con){
        String sql;
        Statement st = null;
        ResultSet rs = null;

        try{

            /** SE RECUPERAN LOS CAMPOS SUPLEMENTARIOS NUMÉRICOS, FECHA, TEXTO CORTO Y DESPLEGABLE QUE NO ESTÉN YA ENTRE LOS CAMPOS SELECCIONADOS **/
           sql = "SELECT PCA_MUN,PCA_COD,PCA_ROT,PCA_DES,PCA_DESPLEGABLE,PCA_TAM,PCA_NOR,PCA_TDA " +
                 "FROM E_PCA,E_PLT WHERE E_PCA.PCA_PLT = E_PLT.PLT_COD AND PCA_PRO='" + codProcedimiento + "' AND PCA_MUN="
                 + codMunicipio + " AND PCA_TDA IN (1,2,3,6) AND PCA_ACTIVO='SI'";

           log.debug("getCamposSuplemProcCriterioBusqueda " + sql);

           st = con.createStatement();
           rs = st.executeQuery(sql);
           while(rs.next()){
               String codigo      = rs.getString("PCA_COD");
               String nombre      = rs.getString("PCA_ROT");
               String tipoDato    = rs.getString("PCA_TDA");
               String desplegable = rs.getString("PCA_DESPLEGABLE");
               log.debug("codigo: " + codigo + " nombre: " + nombre + ", desplegable: " + desplegable);
               CriterioBusquedaPendientesVO campo = new CriterioBusquedaPendientesVO();               
               campo.setCodigo(codigo);
               campo.setCampoSuplementario(true);
               campo.setNombre(nombre);
               campo.setTipoCampoSuplementario(tipoDato);               
               campo.setCodigoDesplegable("");
               if(desplegable!=null && !"".equals(desplegable)){                    
                    campo.setCodigoDesplegable(desplegable);
               }

               criterios.add(campo);
           }

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return criterios;
    }



   /**
     * Recupera los valores de un determinado desplegable
     * @param codigoCampo: Código del campo
     * @return ArrayList<CriterioBusCampoDesplegableVOquedaPendientesVO>
     */
    public ArrayList<CampoDesplegableVO> getValoresDesplegable(String codigoCampo,Connection con){
        String sql;
        Statement st = null;
        ResultSet rs = null;
        ArrayList<CampoDesplegableVO> campos = new ArrayList<CampoDesplegableVO>();

        try{

            /** SE RECUPERAN LOS CAMPOS SUPLEMENTARIOS NUMÉRICOS, FECHA, TEXTO CORTO Y DESPLEGABLE QUE NO ESTÉN YA ENTRE LOS CAMPOS SELECCIONADOS **/
           sql = "SELECT DES_VAL_COD,DES_NOM FROM E_DES_VAL WHERE DES_COD='" + codigoCampo + "'";
           log.debug("getValoresDesplegable " + sql);

           st = con.createStatement();
           rs = st.executeQuery(sql);
           while(rs.next()){
               String codigoValor = rs.getString("DES_VAL_COD");
               String nombre      = rs.getString("DES_NOM");
               
               CampoDesplegableVO campo = new CampoDesplegableVO();
               campo.setCodigoCampo(codigoCampo);
               campo.setCodigoValor(codigoValor);
               campo.setDescripcion(nombre);
               campos.add(campo);
           }

           st.close();
           rs.close();

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return campos;
    }



    public ArrayList<TipoDocumentoVO> getTiposDocumento(Connection con){
        ArrayList<TipoDocumentoVO> tipos = new ArrayList<TipoDocumentoVO>();
        Statement st = null;
        ResultSet rs = null;

        try{
            String sql = "SELECT TID_COD,TID_DES FROM T_TID ORDER BY TID_COD ASC";
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                TipoDocumentoVO tipo = new TipoDocumentoVO();
                tipo.setCodigo(rs.getString("TID_COD"));
                tipo.setDescripcion(rs.getString("TID_DES"));
                tipos.add(tipo);
            }

            st.close();
            rs.close();

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return tipos;
    }

}