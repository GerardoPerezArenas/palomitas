package es.altia.flexia.registro.oficinasregistro.lanbide.persistence.manual;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORsDAO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.persistence.manual.UsuarioDAO;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.registro.oficinasregistro.lanbide.vo.AnotacionOficinaRegistroVO;
import es.altia.flexia.registro.oficinasregistro.lanbide.vo.OficinaRegistroLanbideVO;
import es.altia.flexia.registro.oficinasregistro.lanbide.vo.UnidadRegistroLanbideVO;
import es.altia.util.cache.CacheDatosFactoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.*;


public class AnotacionOficinaRegistroDAO {

    private static AnotacionOficinaRegistroDAO instance = null;
    private final String COMMA = ",";
    private Logger log = Logger.getLogger(AnotacionOficinaRegistroDAO.class);

    private AnotacionOficinaRegistroDAO() {
    }

    public static AnotacionOficinaRegistroDAO getInstance() {
        if (instance == null) {
            instance = new AnotacionOficinaRegistroDAO();
        }

        return instance;
    }

   

   

    /**
     * Recupera aquellas unidades organizativas cuyo código visible comienza por OF     
     * @param conexion:  Conexion a la base de datos
     * @return ArrayList<OficinaRegistroLanbideVO>
     */
    public ArrayList<OficinaRegistroLanbideVO> getOficinasRegistro(Connection conexion) {
        ResultSet rs = null;
        Statement st = null;
        ArrayList<OficinaRegistroLanbideVO> oficinas = new ArrayList<OficinaRegistroLanbideVO>();

        try {
            
            /** ORIGINAL
            String sql = "SELECT UOR_COD,UOR_NOM,UOR_TIP,UOR_COD_VIS  "
                    + "FROM A_UOR "
                    + "WHERE UOR_COD_VIS LIKE '" + ConstantesDatos.PREFIJO_OFICINA_REGISTRO_LANBIDE + "%' "
                    + "AND (UOR_TIP IS NULL OR UOR_TIP=0) "
                    + "ORDER BY UOR_NOM ASC";
                    */
            
            String sql = "SELECT UOR_COD,UOR_NOM,UOR_TIP,UOR_COD_VIS  "
                       + "FROM A_UOR "
                       + "WHERE OFICINA_REGISTRO=1 "
                       + "AND (UOR_TIP IS NULL OR UOR_TIP=0) "
                       + "AND UOR_FECHA_BAJA IS NULL AND UOR_ESTADO='A' " 
                       + "ORDER BY UOR_NOM ASC";
            
            log.debug(sql);
            st = conexion.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                String codUor = rs.getString("UOR_COD");
                String codUorVis = rs.getString("UOR_COD_VIS");
                String nombreUor = rs.getString("UOR_NOM");

                OficinaRegistroLanbideVO oficina = new OficinaRegistroLanbideVO();
                oficina.setCodOficina(codUor);
                oficina.setCodVisibleOficina(codUorVis);
                oficina.setNombreOficina(nombreUor);
                oficinas.add(oficina);

            }// while


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return oficinas;
    }

    
    
    /**
     * Recupera la oficina de registro hija de una unidad de registro, sobre la que un determinado usuario tiene permiso
     * @param codUorRegistro: Código de la unidad de registro del usuario
     * @param usuario: UsuarioValueObject con los datos del usuario
     * @param conexion:  Conexion a la base de datos
     * @return OficinaRegistroLanbideVO
     */     
    public OficinaRegistroLanbideVO getOficinaRegistroUsuario(int codUorRegistro,UsuarioValueObject usuario, Connection conexion,String jndi) {
        ResultSet rs = null;
        Statement st = null;
        OficinaRegistroLanbideVO oficina =null;
        
        try {

            /********* PRUEBA *********/
             String sql = "SELECT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO +  "A_UOU,A_UOR " +
                         "WHERE UOU_USU =" + usuario.getIdUsuario() + " AND UOU_ORG =" + usuario.getOrgCod() + " AND UOU_ENT =" + usuario.getEntCod() + " AND UOU_UOR=UOR_COD AND OFICINA_REGISTRO=1";                
            st = conexion.createStatement();
            rs = st.executeQuery(sql);                
            ArrayList<Integer> uors = new ArrayList<Integer>();
            
            while(rs.next()){
                uors.add(rs.getInt("UOU_UOR"));
            }

            st.close();
            rs.close();
            
            for(int i=0;i<uors.size();i++){                
                UORDTO uor = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(jndi,String.valueOf(uors.get(i)));          
                
               if(UsuarioDAO.getInstance().tieneUnidadAncestroTipoRegistroAsignado(uor,codUorRegistro,jndi)){
                    oficina = new OficinaRegistroLanbideVO();
                    
                    oficina.setCodOficina(uor.getUor_cod());
                    oficina.setCodVisibleOficina(uor.getUor_cod_vis());
                    oficina.setNombreOficina(uor.getUor_nom());                
                    break;
                }                          
            }//for
            
            
            /********* PRUEBA *********/
            
            
            /**            
            String sql = "SELECT UOR_COD,UOR_NOM,UOR_TIP,UOR_COD_VIS  "
                    + "FROM A_UOR, " + GlobalNames.ESQUEMA_GENERICO + "A_ORG, " + GlobalNames.ESQUEMA_GENERICO + "A_UOU "
                    + "WHERE UOU_ORG=" + usuario.getOrgCod() + " AND UOU_ENT=" + usuario.getEntCod() + " AND UOU_USU=" + usuario.getIdUsuario()
                    + " AND OFICINA_REGISTRO=1 AND UOR_PAD=" + codUorRegistro 
                    + " AND (UOR_TIP IS NULL OR UOR_TIP=0) AND UOU_UOR	= UOR_COD AND UOU_ORG=ORG_COD";
            log.debug(sql);

            st = conexion.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                String codUor = rs.getString("UOR_COD");
                String codUorVis = rs.getString("UOR_COD_VIS");
                String nombreUor = rs.getString("UOR_NOM");

                oficina = new OficinaRegistroLanbideVO();
                oficina.setCodOficina(codUor);
                oficina.setCodVisibleOficina(codUorVis);
                oficina.setNombreOficina(nombreUor);                

            }// while
            ***/

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return oficina;
    }

    /**
     * Trata una fecha en función si la fecha es desde o hasta. Utilizado para la búsqueda de anotaciones
     * @param fecha:Fecha en formato dd/MM/yyyy
     * @param desde: Si es true significa que la fecha es el límite inferior del intervalo de búsqueda de fecha, en caso contrario es el límite superior
     * @return Fecha tratada correctamente
     */
    private String tratarFecha(String fecha, boolean desde) {
        StringBuffer salida = new StringBuffer();
        if (desde) {
            salida.append(fecha);
            salida.append(" ");
            salida.append("00:00:00");
        } else {
            salida.append(fecha);
            salida.append(" ");
            salida.append("23:59:59");
        }
        return salida.toString();
    }

    /**
     * Recupera las anotaciones de registro de tipo entrada que se encuentran que han sido dadas de alta en un determinado rango de fechas     
     * @param codOficinaOrigen: Código de la oficina de origen
     * @param codOficinaDestino: Código de la oficina de destino
     * @param fechaDesde: Fecha desde
     * @param fechaHasta: Fecha hasta
     * @param estado: Estado
     * @param pagina: Número de página actual
     * @param count: Número de registro a recuperar
     * @param registroInicio: Registro a partir del cual se comienza a recuperar las anotaciones
     * @param Usuario: Objeto UsuarioValueObject con el contenido de la información del usuario
     * @param conexion:  Conexion a la base de datos
     * @return ArrayList<OficinaRegistroLanbideVO> o vacío si no hay anotaciones o el usuario no tiene los permisos pertinentes
     */
    public ArrayList<AnotacionOficinaRegistroVO> getAnotaciones(String codUnidadRegistro,String codOficinaOrigen, String codOficinaDestino,
            String nombreOficinaDestino, String fechaDesde, String fechaHasta, String estado, 
            UsuarioValueObject usuario, int pagina, int numRegistrosPagina, int codAplicacion,
            int codIdioma, Connection conexion, String jndi) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList<AnotacionOficinaRegistroVO> anotaciones = new ArrayList<AnotacionOficinaRegistroVO>();
        
        Vector <Object> coleccionDevuelta=new Vector();
        Vector filtros=new Vector();
        int posfiltros=0;

        try {
            String sql = null;
            TraductorAplicacionBean descriptor = new TraductorAplicacionBean();
            descriptor.setApl_cod(codAplicacion);
            descriptor.setIdi_cod(codIdioma);
            HashMap uorsRegistro = getUorsNiveles(conexion);
            ResourceBundle bundle = ResourceBundle.getBundle("Registro");

            int inferior = 0;
            int superior = 0;
            inferior = (pagina - 1) * numRegistrosPagina + 1;
            superior = inferior + numRegistrosPagina - 1;

            boolean mirarEstado = estado != null && !"".equals(estado) && !"-1".equals(estado);
            coleccionDevuelta = this.construyeSQL(codUnidadRegistro,codOficinaOrigen, codOficinaDestino, estado,
                    codOficinaOrigen.equalsIgnoreCase("-1"), codOficinaDestino.equalsIgnoreCase("-1"),
                    numRegistrosPagina, mirarEstado,conexion,jndi);
            
            sql=(String)coleccionDevuelta.get(0);

            sql = "select * from (" + sql + ") where rn between ? and ? " +
                    " order by rn";

            log.debug(sql);
            ps = conexion.prepareStatement(sql);
            
            

            
            fechaDesde = this.tratarFecha(fechaDesde, true);
            fechaHasta = this.tratarFecha(fechaHasta, false);
            ps.setTimestamp(1, DateOperations.toTimestamp(fechaDesde, "dd/MM/yyyy HH:mm:ss"));
            ps.setTimestamp(2, DateOperations.toTimestamp(fechaHasta, "dd/MM/yyyy HH:mm:ss"));
            
            filtros=(Vector)coleccionDevuelta.get(1);
            
           
            
            int posicion_interrogacion=3;
            for (int i=0;i<filtros.size();i++)
            {
                Object elemento=new Object();
                elemento=filtros.get(i);
                if(elemento instanceof Integer)
                {
                    int e=(Integer)filtros.get(i);
                    ps.setInt(posicion_interrogacion, e);
                    log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                }
                if(elemento instanceof String)
                {
                    String e=(String)filtros.get(i);
                    ps.setString(posicion_interrogacion, e);
                    log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                }
                if(elemento instanceof Long)
                {
                    Long e=(Long)filtros.get(i);
                    ps.setLong(posicion_interrogacion, e);
                    log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                }
                posicion_interrogacion=posicion_interrogacion+1;
                
            }
            
            ps.setInt(posicion_interrogacion++, inferior);
            log.debug("Valor de paso "+(posicion_interrogacion)+": "+inferior);
            ps.setInt(posicion_interrogacion++, superior);
            log.debug("Valor de paso "+(posicion_interrogacion)+": "+superior);
            
            rs = ps.executeQuery();
            
            Hashtable<Integer,UORDTO> lista = new Hashtable<Integer,UORDTO>();
            
            while (rs.next()) {
                AnotacionOficinaRegistroVO anot = new AnotacionOficinaRegistroVO();

                if (codOficinaDestino.equalsIgnoreCase("-1")) {
                    UnidadRegistroLanbideVO auxiliar = this.getNivel1(rs.getString("RES_UOD"), uorsRegistro);
                    if(auxiliar!=null){
                    anot.setCodOficinaDestino(auxiliar.getCodOficina());
                    anot.setDescripcionOficinaDestino(auxiliar.getNombreOficina());
                    anot.setCodVisibleOficinaDestino(auxiliar.getCodVisibleOficina());
                    }else
                    {
                       anot.setCodOficinaDestino(codOficinaDestino);
                       anot.setDescripcionOficinaDestino("");
                       anot.setCodVisibleOficinaDestino("");
                    }
                } else {
                    
                    UnidadRegistroLanbideVO auxiliar = this.getNivel1(rs.getString("RES_UOD"), uorsRegistro);
                    if(auxiliar!=null){
                    anot.setCodOficinaDestino(auxiliar.getCodOficina());
                    anot.setDescripcionOficinaDestino(auxiliar.getNombreOficina());
                    anot.setCodVisibleOficinaDestino(auxiliar.getCodVisibleOficina());
                    }else
                    {
                       anot.setCodOficinaDestino(codOficinaDestino);
                       anot.setDescripcionOficinaDestino("");
                       anot.setCodVisibleOficinaDestino("");
                    }
                    
                    /**** ORIGINAL
                    anot.setCodOficinaDestino(codOficinaDestino);                    
                    anot.setDescripcionOficinaDestino(rs.getString("unidad_destino"));
                    anot.setCodVisibleOficinaDestino("");
                    ****/
                }

                
                anot.setNumeroEntrada(rs.getString("RES_EJE") + ConstantesDatos.BARRA + rs.getString("RES_NUM"));
                anot.setCodDepartamento(rs.getString("RES_DEP"));
                
                anot.setCodUORDestino(rs.getString("RES_UOD"));
                
                /***********/
                int codOficinaRegistro = rs.getInt("RES_OFI");
                String sCodOficinaRegistro = null;
                String sCodVisibleOficinaOrigen = null;
                String sDescripcionOficinaOrigen = null;
                        
                if(lista.containsKey(codOficinaRegistro)){
                    UORDTO uorTO = (UORDTO)lista.get(codOficinaRegistro);
                    sCodOficinaRegistro = Integer.toString(codOficinaRegistro);
                    sCodVisibleOficinaOrigen = uorTO.getUor_cod_vis();
                    sDescripcionOficinaOrigen = uorTO.getUor_nom();                    
                }else{
                    UORDTO uorTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(jndi,String.valueOf(codOficinaRegistro));
                    lista.put(codOficinaRegistro,uorTO);
                    sCodOficinaRegistro = Integer.toString(codOficinaRegistro);
                    sCodVisibleOficinaOrigen = uorTO.getUor_cod_vis();
                    sDescripcionOficinaOrigen = uorTO.getUor_nom();                                        
                }
                anot.setCodOficinaOrigen(sCodOficinaRegistro);
                anot.setCodVisibleOficinaOrigen(sCodVisibleOficinaOrigen);
                anot.setDescripcionOficinaOrigen(sDescripcionOficinaOrigen);
                /*********/
                
                anot.setDescripcionUORDestino(rs.getString("UNIDAD_DESTINO"));
                anot.setEjercicio(rs.getString("RES_EJE"));
                String etiqueta = bundle.getString(ConstantesDatos.PREFIJO_MAPEO_ESTADO_ANOTACION_FLEXIA + ConstantesDatos.BARRA
                        + rs.getString("RES_EST").trim());
                anot.setEstadoAnotacion(descriptor.getDescripcion(etiqueta));
                anot.setExtracto(rs.getString("RES_ASU"));
                Timestamp tFechaAnotacion = rs.getTimestamp("RES_FEC");
                anot.setFechaEntrada(DateOperations.toString(DateOperations.toCalendar(tFechaAnotacion), "dd/MM/yyyy"));
                anot.setInteresado(rs.getString("INTERESADO"));
                anot.setNumero(rs.getString("RES_NUM"));
                anot.setTipoEntrada("E");
                anot.setUor(rs.getString("RES_UOR"));
                anotaciones.add(anot);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return anotaciones;
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
     * @param conexion:  Conexion a la base de datos
     * @return ArrayList<OficinaRegistroLanbideVO> o vacío si no hay anotaciones o el usuario no tiene los permisos pertinentes
     */
    public int getTotalAnotaciones(String codUnidadRegistro,String codOficinaOrigen, String codOficinaDestino, String fechaDesde,
            String fechaHasta, String estado, String jndi, UsuarioValueObject usuario, Connection conexion) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        int num = 0;
        Vector <Object> coleccionDevuelta=new Vector();
        int posfiltros=0;

        try {

            log.debug("codUnidadRegistro origen de las anotaciones a recuperar: " + codOficinaOrigen);
            log.debug("codUnidadDestino origen de las anotaciones a recuperar: " + codOficinaDestino);

            boolean mirarEstado = (estado != null && !"".equals(estado) && !"-1".equals(estado));
            coleccionDevuelta = this.construyeSQL(codUnidadRegistro,codOficinaOrigen, codOficinaDestino, estado,
                    codOficinaOrigen.equalsIgnoreCase("-1"), codOficinaDestino.equalsIgnoreCase("-1"),
                     0, mirarEstado,conexion,jndi);

            String sql=(String)coleccionDevuelta.get(0);
            sql = "select max(rn) num from (" + sql + ")";
            log.debug(sql);
            fechaDesde = this.tratarFecha(fechaDesde, true);
            fechaHasta = this.tratarFecha(fechaHasta, false);
            ps = conexion.prepareStatement(sql);
            
            int indice = 1;
            ps.setTimestamp(indice++, DateOperations.toTimestamp(fechaDesde, "dd/MM/yyyy HH:mm:ss"));
            ps.setTimestamp(indice++, DateOperations.toTimestamp(fechaHasta, "dd/MM/yyyy HH:mm:ss"));
            
            Vector filtros=new Vector();            
            
            filtros=(Vector)coleccionDevuelta.get(1);
                                  
            
            int posicion_interrogacion=3;
            for (int i=0;i<filtros.size();i++)
            {
                Object elemento=new Object();
                elemento=filtros.get(i);
                if(elemento instanceof Integer)
                {
                    //int e=(Integer)filtros.get(i);
                    Integer e = (Integer)filtros.get(i);
                    //ps.setInt(posicion_interrogacion, e.intValue());
                    ps.setInt(indice++, e.intValue());
                    log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                }
                if(elemento instanceof String)
                {
                    String e=(String)filtros.get(i);
                    //ps.setString(posicion_interrogacion, e);
                    ps.setString(indice++, e);
                    log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                }
                if(elemento instanceof Long)
                {
                    Long e=(Long)filtros.get(i);
                    //ps.setLong(posicion_interrogacion, e);
                    ps.setLong(indice++, e);
                    log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                }
                //posicion_interrogacion=posicion_interrogacion+1;
            }
            
            
            
            rs = ps.executeQuery();
            rs.next();
            num = rs.getInt("NUM");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return num;
    }

    public RegistroValueObject getAnotacionRegistro(AnotacionOficinaRegistroVO anotacion, Connection con) {
        RegistroValueObject registro = new RegistroValueObject();
        Statement st = null;
        ResultSet rs = null;

        try {
            String SQL = "SELECT RES_OBS FROM R_RES "
                    + " WHERE RES_DEP=" + anotacion.getCodDepartamento() + " AND RES_UOR=" + anotacion.getUor()
                    + " AND RES_TIP='" + anotacion.getTipoEntrada() + "' AND RES_EJE=" + anotacion.getEjercicio() + " AND RES_NUM=" + anotacion.getNumero();
            log.debug(SQL);
            st = con.createStatement();
            rs = st.executeQuery(SQL);
            while (rs.next()) {
                String obs = rs.getString("RES_OBS");
                if (obs == null || obs.length() == 0) {
                    registro.setObservaciones("");
                } else {
                    registro.setObservaciones(obs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {

                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return registro;
    }

    /**
     * Cambia el estado de una anotación a pendiente
     * @param anotacion
     * @param con: Conexión a la base de datos
     * @return Un boolean
     */
    public boolean cambiarEstadoPendienteAnotacion(AnotacionOficinaRegistroVO anotacion, Connection con) {
        boolean exito = false;
        Statement st = null;
        ResultSet rs = null;

        try {
            String SQL = "UPDATE R_RES SET RES_EST=0 "
                    + " WHERE RES_DEP=" + anotacion.getCodDepartamento() + " AND RES_UOR=" + anotacion.getUor()
                    + " AND RES_TIP='" + anotacion.getTipoEntrada() + "' AND RES_EJE=" + anotacion.getEjercicio() + " AND RES_NUM=" + anotacion.getNumero();
            log.debug(SQL);
            st = con.createStatement();
            int rows = st.executeUpdate(SQL);
            if (rows == 1) {
                exito = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exito;
    }

    public ArrayList<String> getTodasUnidades(String tipoEntrada, String fechaInicio, String fechaFin, String codDepartamento, Connection con, String[] params) throws TechnicalException {

        ArrayList<String> unidades = new ArrayList<String>();
        Statement stmt = null;
        ResultSet rs = null;

        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);

        /*** ORIGINAL 
        String sql = "  SELECT UOR_COD "
                + "  FROM A_UOR "
                + "  WHERE UOR_COD > 0 "
                + "  AND UOR_COD_VIS LIKE '" + ConstantesDatos.PREFIJO_OFICINA_REGISTRO_LANBIDE + "%' "
                + "  AND ( "
                + "     0 < ("
                + "         SELECT count(*) as total "
                + "         FROM R_RES "
                + "         WHERE res_dep =" + codDepartamento
                + "         AND res_uod = A_UOR.UOR_COD "
                + "         AND res_tip = '" + tipoEntrada + "'"
                + "         AND ((res_fec BETWEEN " + adapt.convertir("'" + fechaInicio + "'",
                AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + " AND " + adapt.convertir("'" + fechaFin + "'",
                AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + ") OR ("
                + adapt.convertir(adapt.convertir("res_fec", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"),
                AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + "="
                + adapt.convertir("'" + fechaFin + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + "))))";
        */
        
        String sql = "  SELECT UOR_COD "
                + "  FROM A_UOR "
                + "  WHERE UOR_COD > 0 "
                + "  AND OFICINA_REGISTRO=1 " 
                + "  AND ( "
                + "     0 < ("
                + "         SELECT count(*) as total "
                + "         FROM R_RES "
                + "         WHERE res_dep =" + codDepartamento
                + "         AND res_uod = A_UOR.UOR_COD "
                + "         AND res_tip = '" + tipoEntrada + "'"
                + "         AND ((res_fec BETWEEN " + adapt.convertir("'" + fechaInicio + "'",
                AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + " AND " + adapt.convertir("'" + fechaFin + "'",
                AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + ") OR ("
                + adapt.convertir(adapt.convertir("res_fec", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"),
                AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + "="
                + adapt.convertir("'" + fechaFin + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + "))))";
        
        log.debug(sql);
        try {

            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                unidades.add(rs.getString("UOR_COD"));
            }
            rs.close();
            stmt.close();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            log.debug("AnotacionOficinaRegistroDAO. Error de SQL en el método getTodasUnidades");
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("AnotacionOficinaRegistroDAO. Error inesperado en el método getTodasUnidades");
        } finally {

            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return unidades;
    }

    public ArrayList<String> getTodasUnidadesPorOficinaPadre(String tipoEntrada, String fechaInicio, String fechaFin, String codDepartamento, String codOficina, Connection con, String[] params) throws TechnicalException {

        ArrayList<String> unidades = new ArrayList<String>();
        Statement stmt = null;
        ResultSet rs = null;

        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);

        String sql = "  SELECT UOR_COD "
                + "  FROM A_UOR "
                + "  WHERE UOR_COD > 0 "
                + "  AND (UOR_COD=" + codOficina + "  OR UOR_COD IN (select UOR_COD FROM A_UOR start with UOR_PAD =" + codOficina + " connect by prior UOR_COD = UOR_PAD))"
                + "  AND ( "
                + "     0 < ("
                + "         SELECT count(*) as total "
                + "         FROM R_RES "
                + "         WHERE res_dep =" + codDepartamento
                + "         AND res_uod = A_UOR.UOR_COD "
                + "         AND res_tip = '" + tipoEntrada + "'"
                + "         AND ((res_fec BETWEEN " + adapt.convertir("'" + fechaInicio + "'",
                AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + " AND " + adapt.convertir("'" + fechaFin + "'",
                AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + ") OR ("
                + adapt.convertir(adapt.convertir("res_fec", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"),
                AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + "="
                + adapt.convertir("'" + fechaFin + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + "))))";

        log.debug(sql);
        try {

            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                unidades.add(rs.getString("UOR_COD"));
            }
            rs.close();
            stmt.close();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            log.debug("AnotacionOficinaRegistroDAO. Error de SQL en el método getTodasUnidades");
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("AnotacionOficinaRegistroDAO. Error inesperado en el método getTodasUnidades");
        } finally {

            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return unidades;
    }

    public boolean isOficinaPadreRaiz(String uor,Connection con, String[] params)throws TechnicalException
    {
        boolean esRaiz=false;
        Statement stmt = null;
        ResultSet rs = null;

        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);

        String sql = "  SELECT UOR_COD_VIS FROM A_UOR WHERE UOR_COD= "+uor;


        log.debug(sql);
        try {

            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String codVis=rs.getString("UOR_COD_VIS");
                if(codVis.equals("OFIC")) esRaiz=true;
            }
            rs.close();
            stmt.close();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            log.debug("AnotacionOficinaRegistroDAO. Error de SQL en el método getTodasUnidades");
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("AnotacionOficinaRegistroDAO. Error inesperado en el método getTodasUnidades");
        } finally {

            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return esRaiz;
    }
    public HashMap getUorsNiveles(Connection con) {

        ArrayList<UnidadRegistroLanbideVO> uorsNiveles = new ArrayList<UnidadRegistroLanbideVO>();
        Statement stmt = null;
        ResultSet rs = null;
        HashMap resultado = new HashMap();



        String sql = "select level,uor_pad,UOR_COD,UOR_NOM,OFICINA_REGISTRO FROM A_UOR "
                + " where uor_pad is not null "
                + "start with UOR_COD_VIS like '" + ConstantesDatos.PREFIJO_OFICINA_REGISTRO_LANBIDE + "%' AND (UOR_TIP IS NULL OR UOR_TIP=0) connect by prior UOR_COD = UOR_PAD";

        log.debug(sql);
        try {

            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            int i = 0;
            while (rs.next()) {

                UnidadRegistroLanbideVO unidadVO = new UnidadRegistroLanbideVO();
                unidadVO.setNivel(rs.getString("level"));
                unidadVO.setCodOficina(rs.getString("UOR_COD"));
                unidadVO.setNombreOficina(rs.getString("UOR_NOM"));
                unidadVO.setCodPadre(rs.getString("UOR_PAD"));
                int oficina = rs.getInt("OFICINA_REGISTRO");
                unidadVO.setOficinaRegistro(false);
                if(oficina==1) unidadVO.setOficinaRegistro(true);
                uorsNiveles.add(i, unidadVO);
                resultado.put(rs.getString("UOR_COD"), unidadVO);
                i++;

            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return resultado;
    }

    /**
    public UnidadRegistroLanbideVO getNivel1(String codUnidad, HashMap unidades) {


        UnidadRegistroLanbideVO resultado = new UnidadRegistroLanbideVO();
        boolean seguir = true;

        while (seguir) {
            resultado = (UnidadRegistroLanbideVO) unidades.get(codUnidad);
            if(resultado!=null){
            if (unidades.get(resultado.getCodPadre()) != null) {
                codUnidad = resultado.getCodPadre();
            } else {
                seguir = false;
            }
            }else seguir=false;
        }
        
        return resultado;
    }*/
    
    
     public UnidadRegistroLanbideVO getNivel1(String codUnidad, HashMap unidades) {

        UnidadRegistroLanbideVO resultado = new UnidadRegistroLanbideVO();
        boolean seguir = false;

        while (!seguir) {
            resultado = (UnidadRegistroLanbideVO) unidades.get(codUnidad);
            if(resultado!=null){
                if(resultado.isOficinaRegistro())
                    seguir = true;
                else
                    codUnidad = resultado.getCodPadre();
            }else
                seguir = false;           
        }
        
        return resultado;
    }

    
   
    
     private ArrayList<String> getListaCodigosUorsHijasDe(int codUor,Connection con){
        ArrayList<String> salida = new ArrayList<String>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            String sql = "SELECT UOR_COD FROM A_UOR WHERE UOR_PAD=?";
            ps = con.prepareStatement(sql);
            int i=1;
            ps.setInt(i++,codUor);
            
            rs = ps.executeQuery();            
            while(rs.next()){
                String uor = rs.getString("UOR_COD");
                salida.add(uor);
            }
            
            return salida;
            
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                log.error("Error al cerrar los recursos asociados a la conexión de base de datos: " + e.getMessage());
                e.printStackTrace();                
            }
        }        
        return salida;
    }
    
    
   /**
    * Devuelve en un String apto para incluir en una sentencia SQL la lista de los códigos internos de todas las oficinas
    * de registro, así como, las de las subunidades hijas de esta
    * @param con: Conexión a la BBDD
    * @return  String 
    */      
   public ArrayList<String> getCodigosUorsDestinoTodasOficinas(Connection con){
        ArrayList<String> salida = null;
        Statement st = null;
        ResultSet rs = null;
        
        try{
            String sql = "SELECT UOR_COD FROM A_UOR WHERE OFICINA_REGISTRO=1";            
            st = con.createStatement();            
            rs = st.executeQuery(sql);
            
            ArrayList<String> oficinasRegistro = new ArrayList<String>();
            while(rs.next()){                
                oficinasRegistro.add(rs.getString("UOR_COD"));                
            }
            
            st.close();
            rs.close();
            
            for(int i=0;i<oficinasRegistro.size();i++){
                String codOficina = oficinasRegistro.get(i);                                
                // Se añaden las uor hija de la oficina de registro
                oficinasRegistro.addAll(getListaCodigosUorsHijasDe(Integer.parseInt(codOficina),con));
                
            }// for
            
            salida=oficinasRegistro;
            log.debug("getCodigosUorsDestinoTodasOficinas: " + salida);
            
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                log.error("Error al cerrar los recursos asociados a la conexión de base de datos: " + e.getMessage());
                e.printStackTrace();                
            }
        }        
        return salida;
    }
            
        
   /************************************************************************************/
     
     private HashMap<String,UORDTO> convertArrayListUORDTOtoHashmapUORTO(ArrayList<UORDTO> lista){
         HashMap<String,UORDTO> salida = new HashMap<String, UORDTO>();
         for(int i=0;i<lista.size();i++){
             UORDTO uor = lista.get(i);
             salida.put(uor.getUor_cod(),uor);             
         }
         
         return salida;
     }
     
     
    private ArrayList<String> getUnidadesDestinoValidasDeOficina(String codOficinaDestino,String jndi){
         ArrayList<String> salida = new ArrayList<String>();
           SortedMap <ArrayList<String>,UORDTO> unidadesOrg = (SortedMap <ArrayList<String>,UORDTO>) CacheDatosFactoria.getImplUnidadesOrganicas().getDatosBD(jndi);	
           
            ArrayList<UORDTO> uors=new ArrayList<UORDTO> ();
            for(Map.Entry<ArrayList<String>,UORDTO> entry : unidadesOrg.entrySet()) {	
                UORDTO unidad = entry.getValue();
                if (!"S".equals(unidad.getUorOculta()))
                    uors.add(unidad);
            }                     
           HashMap<String,UORDTO> hashmapUors = convertArrayListUORDTOtoHashmapUORTO(uors);
             
        if (uors!=null && !uors.isEmpty()) {	
            for(UORDTO unidad : uors) {	
                if(tieneAncestro(unidad.getUor_cod(),codOficinaDestino,hashmapUors))	
                    salida.add(unidad.getUor_cod());
                }
            }	
         // Se añade al menos la oficina de destino si no hay ninguna
         salida.add(codOficinaDestino);
         return salida;
     }
     
    
     /**
      * Comprueba si la unidad "codUnidad" tiene como ancestro a la oficina de registro del parámetro "codOficinaRegistro"
      * @param codOficinaRegistro: Código de la oficina de registro
      * @param codUnidad: Código de la unidad para la que se comprueba si tiene un ancestro como oficina de registro
      * @param unidades:
      * @return  
      */
     public boolean tieneAncestro(String codUnidadBuscada,String codOficinaRegistro, HashMap<String,UORDTO> unidades) {
        
        boolean seguir = false;
        
        boolean encontrado = false;
        while(!seguir){
            UORDTO uor = (UORDTO)unidades.get(codUnidadBuscada);
            
            if(uor!=null){
                if(uor.getUor_pad()!=null){
                    if(uor.getUor_pad()!=null && !"".equals(uor.getUor_pad()) && uor.getUor_pad().equals(codOficinaRegistro)){
                            // La uor actual tiene como padre a la oficina de registro, entonces a la salida
                        seguir = true;
                        encontrado = true;
                    }else
                        codUnidadBuscada = uor.getUor_pad();
                }else{
                    seguir = true;
                    encontrado = false;
                }
                
            }else seguir = false;
            
        }// while   
        
        return encontrado;
        
    }
	
	
	
  
   
    /*****************************************************************/
   
   
    private Vector construyeSQL(String codUnidadRegistro,String codOficinaOrigen, String codOficinaDestino, String estado,
            boolean origenTodas, boolean destinoTodas, int numRegistrosPagina, 
            boolean mirarEstado,Connection con,String jndi) {

        String query = "";
        Vector filtros=new Vector();
        Vector <Object> coleccionDevuelta=new Vector();
        int posfiltros=0;
        
        ArrayList<String> uorTodas = null;
        ArrayList<String> uorDestinoValidas = null;
        

         
        String SQLAnotaciones = "select /*+first_rows(" + numRegistrosPagina + ") */ res_est,res_dep, res_uor, "
                + "HTE_AP1 || NVL(' ','') || NVL(HTE_AP2,'') || ',' || NVL(' ','') || NVL(HTE_nom,'') AS interesado,"
                + " RES_FEC,RES_NUM, RES_EJE, RES_ASU,"
                + " RES_UOD, DESTINO.UOR_NOM as unidad_destino,RES_OFI, "
                + "row_number() over (order by registro.res_eje, registro.res_num) rn "
                + "from r_res registro INNER JOIN A_UOR DESTINO ON (UOR_COD=RES_UOD) "
                + "inner join t_hte on (res_ter=hte_ter and res_tnv=hte_nvr) "        
                //+ " WHERE RES_TIP='E' AND (RES_FEC>=? AND  RES_FEC<=?) ";        
                + " WHERE RES_TIP='E' AND (RES_FEC>= ? AND  RES_FEC<= ? ) AND RES_UOR= ? ";        
          
        query = SQLAnotaciones;
        
        filtros.add(posfiltros++,new Integer(codUnidadRegistro));
        
        if (!origenTodas) {    
            // Si la oficina de registro de origen es una determinada, se realiza el filtro por la misma.
            query +=  " AND RES_OFI= ? " ;
            filtros.add(posfiltros++,new Integer(codOficinaOrigen));
        }
        
        if (destinoTodas) {
            //query += SQLdestinoTodas;
            //query += " AND RES_UOD IN " + SQLdestinoTodas; 
            uorTodas=getCodigosUorsDestinoTodasOficinas(con);
            query += " AND RES_UOD IN " ;  
            
            StringBuffer sb = new StringBuffer();
            sb.append("(");
            for(int j=0;j<uorTodas.size();j++){                
                sb.append(" ? ");
                filtros.add(posfiltros++,new Integer(uorTodas.get(j)));
                if(uorTodas.size()-j>1) sb.append(",");
                   
                /*
                sb.append(uorTodas.get(j));
                if(uorTodas.size()-j>1) sb.append(","); **/
            }            
            sb.append(")");
            
            query += sb.toString();
            
            
        } else {
            // original
            //query += " AND RES_UOD=" + codOficinaDestino;
            
            /*** prueba: LA UNIDAD DE DESTINO TIENE QUE ESTAR ENTRE LA OFICINA DE REGISTRO Y ALGUNA DE SUS SUBUNIDADES HIJAS ***/
            //query+= " AND RES_UOD IN " + getCodigosUorsHijasDe(Integer.parseInt(codOficinaDestino),con);
            uorDestinoValidas= getUnidadesDestinoValidasDeOficina(codOficinaDestino,jndi);     
            query+= " AND RES_UOD IN " ;           
            
            StringBuffer sb = new StringBuffer();
            sb.append("(");
            for(int j=0;j<uorDestinoValidas.size();j++){

                sb.append(" ? ");
                 filtros.add(posfiltros++,new Integer(uorDestinoValidas.get(j)));
                 if(uorDestinoValidas.size()-j>1) sb.append(",");

                /**
                sb.append(uorDestinoValidas.get(j));
                if(uorDestinoValidas.size()-j>1) sb.append(",");
                ***/
      
            }            
            sb.append(")");
            
            query += sb.toString();
            /*** prueba ****/
        }
        
        if (mirarEstado) {
            query += " AND RES_EST= ? ";
            filtros.add(posfiltros++,new Integer(estado));
        }

        coleccionDevuelta.add(query);
        coleccionDevuelta.add(filtros);
        return coleccionDevuelta;


    }
    
    
    
    /***** NUEVO IMPRESION PDF ******/
    
     public ArrayList<String> getListaCodigosUorsDestinoTodasOficinas(Connection con,String jndi){
        ArrayList<String> oficinasRegistro = new ArrayList<String>();        
                 SortedMap <ArrayList<String>,UORDTO> unidadesOrg = (SortedMap <ArrayList<String>,UORDTO>) CacheDatosFactoria.getImplUnidadesOrganicas().getDatosBD(jndi);
        if (unidadesOrg!=null && !unidadesOrg.isEmpty()) {
            for(Map.Entry<ArrayList<String>,UORDTO> entry : unidadesOrg.entrySet()) {	
                UORDTO unidad = entry.getValue();	
                if (!"S".equals(unidad.getUorOculta()) && unidad.isOficinaRegistro())	
                    oficinasRegistro.add(unidad.getUor_cod());       
            }

        }        
        
        return oficinasRegistro;
    }
            
    
    
    
}
