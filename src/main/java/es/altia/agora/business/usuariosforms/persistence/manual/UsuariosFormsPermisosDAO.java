/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.usuariosforms.persistence.manual;

import es.altia.agora.business.usuariosforms.UsuariosFormsPermisosVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author manuel.bahamonde
 */
public class UsuariosFormsPermisosDAO {

    private static UsuariosFormsPermisosDAO instance = null;
    protected static Log m_Log = LogFactory.getLog(UsuariosFormsPermisosDAO.class.getName());

    public static UsuariosFormsPermisosDAO getInstance() {
        // si no hay ninguna instancia de esta clase tenemos que crear una
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread)
            // Las invocaciones de este metodo
            synchronized (UsuariosFormsPermisosDAO.class) {
                if (instance == null) {
                    instance = new UsuariosFormsPermisosDAO();
                }
            }
        }
        return instance;
    }


    public int insertarPermisoUsuario(UsuariosFormsPermisosVO usuariosFormsPermisosVO, String[] params) {
      AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        String sql = "";
        int resultado = 0;

        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);

            sql = "INSERT INTO USUARIO_TEMP_PERMISOS (USU_UOR_LOGIN,USU_UOR_COD,USU_UOR_CARGO) VALUES ( " +
                    "'" + usuariosFormsPermisosVO.getLoginUsuarioPermiso() +"'," +
                    usuariosFormsPermisosVO.getCodUnidadOrganicaPermiso() + "," + usuariosFormsPermisosVO.getCodCargoPermiso() + ")";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();

        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
            resultado = -1;
        }finally{
            commitTransaction(abd,conexion);
            return resultado;
        }

    }


    // Elimina todos los permisos de un determinado usuario del catalogo  de formularios
    public int eliminarPermisosUsuario(String loginUsuario, String[] params) {
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        String sql = "";

         int resultado = 0;
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);

            sql = "DELETE FROM USUARIO_TEMP_PERMISOS WHERE USU_UOR_LOGIN='" + loginUsuario + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }

            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();

        } catch (Exception e) {
            resultado = -1;
            rollBackTransaction(abd, conexion, e);

        } finally {
            commitTransaction(abd, conexion);
            return resultado;
        }
    }


    public Vector obtenerListaPermisosUsuario(String loginUsuario, String[] params) {
        Vector listaPermisosUsuario = new Vector();

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";

        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();

            sql = "SELECT   usu_uor_login, uor_cod,uor_cod_vis, uor_nom, car_cod, car_nom, car_cod_vis " +
            " FROM usuario_temp_permisos " +
            " INNER JOIN a_uor on usu_uor_cod=uor_cod " +
            " INNER JOIN a_car on usu_uor_cargo = car_cod  where usu_uor_login='" + loginUsuario+ "'" ;

            String[] orden = {"uor_nom", "1"};
            sql += abd.orderUnion(orden);
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("obtenerListaPermisosUsuario -------> " + sql);
            }
            stmt = conexion.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                UsuariosFormsPermisosVO usuariosFormsPermisosVO = new UsuariosFormsPermisosVO();
                usuariosFormsPermisosVO.setLoginUsuarioPermiso(rs.getString(1));
                usuariosFormsPermisosVO.setCodUnidadOrganicaPermiso(rs.getString(2));
                usuariosFormsPermisosVO.setCodVisibleUnidadOrganicaPermiso(rs.getString(3));
                usuariosFormsPermisosVO.setNombreUnidadOrganicaPermiso(rs.getString(4));
                usuariosFormsPermisosVO.setCodCargoPermiso(rs.getString(5));
                usuariosFormsPermisosVO.setNombreCargoPermiso(rs.getString(6));
                usuariosFormsPermisosVO.setCodVisibleCargoPermiso(rs.getString(7));

                listaPermisosUsuario.addElement(usuariosFormsPermisosVO);
            }
            rs.close();
            stmt.close();
            conexion.close();

        } catch (SQLException e) {
            listaPermisosUsuario = null;
            abd.devolverConexion(conexion);
            if (m_Log.isErrorEnabled()) {
                m_Log.error("obtenerListaPermisosUsuario - SQLException : " + e.getMessage());
            }
        } catch (Exception ex) {
            listaPermisosUsuario = null;
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("obtenerListaPermisosUsuario  - Exception : " + ex.getMessage());
            }
        } finally {

            return listaPermisosUsuario;
        }
    }




     public Vector obtenerListaPermisosUsuarioEnvioFormulario(String codFormulario, String loginUsuario, String[] params) {
        Vector listaPermisosUsuario = new Vector();

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        String tipoRestriccionFormulario = "";
        String todasUORs ="";

        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();

            sql = " SELECT FDF_TIPO_RESTRICCION, FDF_UNIDADES  FROM F_DEF_FORM WHERE FDF_CODIGO= " + codFormulario;

            stmt = conexion.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                tipoRestriccionFormulario = rs.getString(1);
                todasUORs=rs.getString(2);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("obtenerListaPermisosUsuarioEnvioFormulario - tipoRestriccionFormulario = " + tipoRestriccionFormulario);
                    m_Log.debug("obtenerListaPermisosUsuarioEnvioFormulario - accesible a todos = " + todasUORs);
                }
            }
            stmt.close();

            sql = "SELECT   usu_uor_login, uor_cod,uor_cod_vis, uor_nom, car_cod, car_nom, car_cod_vis "
                        + " FROM usuario_temp_permisos  INNER JOIN a_uor on usu_uor_cod=uor_cod "
                        + " INNER JOIN a_car on usu_uor_cargo = car_cod  "
                        + " where usu_uor_login='" + loginUsuario + "' ";

            // obtenemos aquellas uors que no estan restringidas en la definicion del formulario
            if (tipoRestriccionFormulario.equals("0") && todasUORs.equals("0")) {
                sql += "AND NOT EXISTS (SELECT * FROM F_RESTRICCION WHERE (R_UOR=USU_UOR_COD AND R_CAR=USU_UOR_CARGO) OR " +
                        "(R_UOR=USU_UOR_COD AND R_CAR=0) AND R_FORM = " + codFormulario + " )";


            } else if (tipoRestriccionFormulario.equals("1")&& todasUORs.equals("0")) { // obtenemos aquellas uors que estan permitidas
                sql += "AND EXISTS (SELECT * FROM F_RESTRICCION WHERE (R_UOR=USU_UOR_COD AND R_CAR=USU_UOR_CARGO) OR " +
                        "(R_UOR=USU_UOR_COD AND R_CAR=0) AND R_FORM = " + codFormulario + " )";
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(" - listaPermisosEnvioFormulario = " + sql);
            }

            stmt = conexion.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                UsuariosFormsPermisosVO usuariosFormsPermisosVO = new UsuariosFormsPermisosVO();
                usuariosFormsPermisosVO.setLoginUsuarioPermiso(rs.getString(1));
                usuariosFormsPermisosVO.setCodUnidadOrganicaPermiso(rs.getString(2));
                usuariosFormsPermisosVO.setCodVisibleUnidadOrganicaPermiso(rs.getString(3));
                usuariosFormsPermisosVO.setNombreUnidadOrganicaPermiso(rs.getString(4));
                usuariosFormsPermisosVO.setCodCargoPermiso(rs.getString(5));
                usuariosFormsPermisosVO.setNombreCargoPermiso(rs.getString(6));
                usuariosFormsPermisosVO.setCodVisibleCargoPermiso(rs.getString(7));

                listaPermisosUsuario.addElement(usuariosFormsPermisosVO);
            }
            rs.close();
            stmt.close();
            conexion.close();

        } catch (SQLException e) {
            listaPermisosUsuario = null;
            abd.devolverConexion(conexion);
            if (m_Log.isErrorEnabled()) {
                m_Log.error("obtenerListaPermisosUsuarioEnvioFormulario - SQLException : " + e.getMessage());
            }
        } catch (Exception ex) {
            listaPermisosUsuario = null;
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("obtenerListaPermisosUsuarioEnvioFormulario  - Exception : " + ex.getMessage());
            }
        } finally {
          
            return listaPermisosUsuario;
        }
    }


    private void rollBackTransaction(AdaptadorSQLBD bd, Connection con, Exception e) {
        try {
            bd.rollBack(con);
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            e.printStackTrace();
            m_Log.error(e.getMessage());
        }
    }

    private void commitTransaction(AdaptadorSQLBD bd, Connection con) {
        try {
            bd.finTransaccion(con);
            bd.devolverConexion(con);
        } catch (Exception ex) {
            ex.printStackTrace();
            m_Log.error(ex.getMessage());
        }
    }
}
