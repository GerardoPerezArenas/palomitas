package es.altia.agora.interfaces.user.web.administracion.mantenimiento;

import es.altia.agora.business.escritorio.*;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.*;
import es.altia.agora.business.administracion.mantenimiento.persistence.*;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.escritorio.persistence.manual.UsuarioDAO;
import es.altia.agora.business.util.*;
import es.altia.arboles.impl.ArbolImpl;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.jdbc.GeneralOperations;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase UORsAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES</p>
 * @version 1.0
 */


public class UORsAction extends ActionSession  {

    private final String COD_TODOS_LOS_CARGOS = "0";
    
    public ActionForward performSession(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        m_Log.debug("perform");
        ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
        HttpSession session = request.getSession();

        // Validaremos los parametros del request especificados
        ActionErrors errors = new ActionErrors();

        // decidir que acción tomar
        String opcion = request.getParameter("opcion");

        if (m_Log.isInfoEnabled()) {
            m_Log.info("la opción en el action es: " + opcion);
        }

        // Rellenamos el form de MantenimientosAdminForm
        if (form == null) {
            m_Log.debug("Rellenamos el form de MantenimientosAdminForm");
            form = new MantenimientosAdminForm();
            if ("request".equals(mapping.getScope())){
                request.setAttribute(mapping.getAttribute(), form);
            }
            else{
                session.setAttribute(mapping.getAttribute(), form);
            }
        }


        UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        String[] params = usuario.getParamsCon();
        MantenimientosAdminForm mantForm = (MantenimientosAdminForm)form;

        UORsManager uorManager = UORsManager.getInstance();
        GeneralValueObject gVO = recogerParametros(request);

        Config m_Conf = ConfigServiceHelper.getConfig("common");
        String accede = m_Conf.getString("aytos.registro");

        if ("cargar".equalsIgnoreCase(opcion)) {
            // =======================
            // nuevas UORs
            m_Log.debug("Recargando datos de las UORs en la sesión");
            Vector nuevasUOR = UORsManager.getInstance().getListaUORs(true,params);
            /*for(int i=0; i<nuevasUOR.size(); i++) {
                m_Log.info(((UORDTO)nuevasUOR.get(i)).toString());
            }*/
            m_Log.debug("Cargadas " + nuevasUOR.size() + " UORs");
            mantForm.setListaNuevasUORs(nuevasUOR);
            // arbol de jerarquía de uors
            ArbolImpl arbol = UORsManager.getInstance().getArbolUORs(true,false,false, params);
            m_Log.debug("Cargado árbol:" + arbol.contarNodos());
            //m_Log.debug(arbol.toString());
            mantForm.setArbol(arbol);
            // =======================

            mantForm.setOtrosDatos(null);
            opcion = "cargar";
            }
        
        else if ("cargarNombre".equalsIgnoreCase(opcion)) {
            // =======================
            // nuevas UORs
            m_Log.debug("Recargando datos de las UORs en la sesión");
            Vector nuevasUOR = UORsManager.getInstance().getListaUORs(true,params);
            
            m_Log.debug("Cargadas " + nuevasUOR.size() + " UORs");
            mantForm.setListaNuevasUORs(nuevasUOR);
            // arbol de jerarquía de uors
            ArbolImpl arbol = UORsManager.getInstance().getArbolUORs(true,false,true, params);
            m_Log.debug("Cargado árbol:" + arbol.contarNodos());
            //m_Log.debug(arbol.toString());
            mantForm.setArbol(arbol);
            // =======================

            mantForm.setOtrosDatos(null);
            opcion = "cargarNombre";
        }
        else if("eliminar".equalsIgnoreCase(opcion)) {
            int codigo = Integer.parseInt(request.getParameter("codUOR"));
            eliminarUorUsuarioFormularios(String.valueOf(usuario.getOrgCod()),params,codigo);
            int eliminada = UORsManager.getInstance().eliminarUOR(codigo, params,String.valueOf(usuario.getOrgCod()));
            
            // EL DAO devuelve un codigo numerico, posibles valores :
            // >0: no hay problema
            if (eliminada > 0)
                mantForm.setOtrosDatos(null);
            // -2: hay registros asociados a la UOR o a alguna hija
            else if (eliminada == -2) {
                m_Log.debug("problema con eliminar: " + eliminada);
                GeneralValueObject gVO1 = new GeneralValueObject();
                gVO1.setAtributo("respuesta","hayRegistros");
                mantForm.setOtrosDatos(gVO1);
            }
            // -3: hay usuarios asociados a la UOR o a alguna hija
            else if (eliminada == -3) {
                m_Log.debug("problema con eliminar: " + eliminada);
                GeneralValueObject gVO1 = new GeneralValueObject();
                gVO1.setAtributo("respuesta","hayUsuarios");
                mantForm.setOtrosDatos(gVO1);
            }
            // -4: hay tramites asociados a la UOR
            else if (eliminada == -4) {
                m_Log.debug("problema con eliminar: " + eliminada);
                GeneralValueObject gVO1 = new GeneralValueObject();
                gVO1.setAtributo("respuesta","hayTramites");
                mantForm.setOtrosDatos(gVO1);
            }
            // -5: hay permisos para informes asociados a la UOR
            else if (eliminada == -5) {
                m_Log.debug("problema con eliminar: " + eliminada);
                GeneralValueObject gVO1 = new GeneralValueObject();
                gVO1.setAtributo("respuesta","hayInformes");
                mantForm.setOtrosDatos(gVO1);
            }
            // -6: hay anotaciones de registro que tiene a la UOR como oficina de registro
            else if (eliminada == -6) {
                m_Log.debug("problema con eliminar: " + eliminada);
                GeneralValueObject gVO1 = new GeneralValueObject();
                gVO1.setAtributo("respuesta","hayOficinasRegistro");
                mantForm.setOtrosDatos(gVO1);
            }
            // otro: problema tecnico
            else {
                m_Log.debug("problema con eliminar: " + eliminada);
                GeneralValueObject gVO1 = new GeneralValueObject();
                gVO1.setAtributo("respuesta","noEliminada");
                mantForm.setOtrosDatos(gVO1);
            }

            if (eliminada<=0) establecerPermisosUsuarioTodo(String.valueOf(usuario.getOrgCod()),params,codigo);

            // recargar datos en la sesión
            m_Log.debug("Recargando datos de las UORs en la sesión");
            Vector nuevasUOR = UORsManager.getInstance().getListaUORs(true,params);
            m_Log.debug("Cargadas " + nuevasUOR.size() + " UORs");
            mantForm.setListaNuevasUORs(nuevasUOR);
            // arbol de jerarquía de uors
            ArbolImpl arbol = UORsManager.getInstance().getArbolUORs(true,false,false, params);
            m_Log.debug("Cargado árbol:" + arbol.contarNodos());
            mantForm.setArbol(arbol);

            request.setAttribute("operacion_realizada", "eliminar");
            opcion = "vuelveCargar";
        }
        else if("modificar".equals(opcion)){
            // recuperar los datos del UOR de la sesión y rellenar el dto
            UORDTO dto = new UORDTO();
            // uor cod
            dto.setUor_cod(request.getParameter("codUOR"));
            m_Log.debug("request.getParameter(\"codUOR\"): " + request.getParameter("codUOR"));
            // uor pad
            dto.setUor_pad(request.getParameter("codPad"));
            m_Log.debug("request.getParameter(\"codPad\"): " + request.getParameter("codPad"));
            // uor cod visible
            dto.setUor_cod_vis(request.getParameter("codigo_visible").trim());
            m_Log.debug("request.getParameter(\"codigo_visible\"): " + request.getParameter("codigo_visible"));
            // uor cod accede
              m_Log.debug("request.getParameter(\"codigo_accede\"): " + request.getParameter("codigo_accede"));
           String codigo_accede=null;
           codigo_accede=request.getParameter("codigo_accede");
            if((codigo_accede!=null)||(codigo_accede==" ")){
                  m_Log.debug("** entro");
                 dto.setUor_cod_accede(request.getParameter("codigo_accede").trim());
            }  
           
            // estado
            dto.setUor_estado(request.getParameter("estado"));
            m_Log.debug("request.getParameter(\"estado\"): " + request.getParameter("estado"));
            
            if ("B".equals(dto.getUor_estado()))
                dto.setUorOculta("S".equals(request.getParameter("uorOculta"))?"S":"N");
            else 
                dto.setUorOculta("N");
                
            // nombre/descripción
            dto.setUor_nom(request.getParameter("nombre").trim());
            m_Log.debug("request.getParameter(\"nombre\"): " + request.getParameter("nombre"));
            // tipo
            dto.setUor_tipo(request.getParameter("tipo"));
            //rexistro xeral
            m_Log.debug("request.getParameter(\"tipo\"): " + request.getParameter("tipo"));
             dto.setUor_rexistro_xeral(request.getParameter("rexistro_xeral"));
            m_Log.debug("request.getParameter(\"rexistro_xeral\"): " + request.getParameter("rexistro_xeral"));
            // fecha alta
            dto.setUor_fecha_alta(request.getParameter("fecha_alta").trim());
            m_Log.debug("request.getParameter(\"fecha_alta\"): " + request.getParameter("fecha_alta"));
            // fecha baja
            dto.setUor_fecha_baja(request.getParameter("fecha_baja").trim());
            m_Log.debug("request.getParameter(\"fecha_baja\"): " + request.getParameter("fecha_baja"));
             // ordenacion
            dto.setUor_ordenar(request.getParameter("ordenar").trim());
            m_Log.debug("request.getParameter(\"ordenar\"): " + request.getParameter("ordenar"));
            
            // email
            dto.setUor_email(request.getParameter("email").trim());
            m_Log.debug("request.getParameter(\"email\"): " + request.getParameter("email"));
            // no visible en registro
            if((request.getParameter("no_visible_registro") != null)&&(request.getParameter("no_visible_registro") != "")) {
                m_Log.debug("request.getParameter(\"no_visible_registro\"): " + request.getParameter("no_visible_registro"));
                dto.setUor_no_registro(request.getParameter("no_visible_registro").charAt(0));
            }
            else {
                dto.setUor_no_registro('0');
                m_Log.debug("request.getParameter(\"no_visible_registro\"): 0");
            }
            
            String oficina_registro = request.getParameter("oficina_registro");
            dto.setOficinaRegistro(false);
            if(oficina_registro!=null && !"".equals(oficina_registro) && "on".equalsIgnoreCase(oficina_registro))
                dto.setOficinaRegistro(true);
            
            

            if (dto.getUor_estado().equals("B")) eliminarUorUsuarioFormularios(String.valueOf(usuario.getOrgCod()),params,Integer.parseInt(dto.getUor_cod()));
            //int actualizada = uorManager.modificarUOR(gVO, params);
            int actualizada = UORsManager.getInstance().modificarUOR(dto, params, String.valueOf(usuario.getOrgCod()));
         // EL DAO devuelve un codigo numerico, posibles valores :
            // > 0:  no hay problema
            if (actualizada > 0)
                mantForm.setOtrosDatos(null);            
            // -4: hay tramites asociados a la UOR
            else if (actualizada == -4) {
                m_Log.debug("problema con modificar: " + actualizada);
                GeneralValueObject gVO1 = new GeneralValueObject();
                gVO1.setAtributo("respuesta","hayTramites");
                mantForm.setOtrosDatos(gVO1);
            }          
            // otro: problema tecnico
            else {
            	m_Log.debug("problema con modificar: " + actualizada);
                GeneralValueObject gVO1 = new GeneralValueObject();
                gVO1.setAtributo("respuesta","noModificada");
                mantForm.setOtrosDatos(gVO1);                                            
            }

            if (actualizada<=0)
                establecerPermisosUsuarioTodo(String.valueOf(usuario.getOrgCod()),params,Integer.parseInt(dto.getUor_cod()));
                if (accede.equals("si")) UORsManager.getInstance().modificarUORCodAccede(dto, params);                
            // recargar datos en la sesión
            m_Log.debug("Recargando datos de las UORs en la sesión");
            Vector nuevasUOR = UORsManager.getInstance().getListaUORs(true,params);
            m_Log.debug("Cargadas " + nuevasUOR.size() + " UORs");
            mantForm.setListaNuevasUORs(nuevasUOR);
            // arbol de jerarquía de uors
            ArbolImpl arbol = UORsManager.getInstance().getArbolUORs(true,false,false, params);
            m_Log.debug("Cargado árbol:" + arbol.contarNodos());
            mantForm.setArbol(arbol);
            
            request.setAttribute("operacion_realizada", "modificar");            
            opcion = "vuelveCargar";
        }
        else if("alta".equals(opcion)){

            UORDTO dto = new UORDTO();
            // uor cod
            //dto.setUor_cod(request.getParameter("codUOR"));
            //m_Log.debug("request.getParameter(\"codUOR\"): " + request.getParameter("codUOR"));
            // uor pad
            m_Log.debug("request.getParameter(\"codPad\"): " + request.getParameter("codPad"));
            dto.setUor_pad(request.getParameter("codPad"));
            // uor cod visible
            m_Log.debug("request.getParameter(\"codigo_visible\"): " + request.getParameter("codigo_visible"));
            dto.setUor_cod_vis(request.getParameter("codigo_visible").trim());
            // uor cod accede
            m_Log.debug("request.getParameter(\"codigo_accede\"): " + request.getParameter("codigo_accede"));
            dto.setUor_cod_accede(request.getParameter("codigo_accede"));
            // estado
            m_Log.debug("request.getParameter(\"estado\"): " + request.getParameter("estado"));
            dto.setUor_estado(request.getParameter("estado"));
            // nombre/descripción
            m_Log.debug("request.getParameter(\"nombre\"): " + request.getParameter("nombre"));
            dto.setUor_nom(request.getParameter("nombre").trim());
            // tipo
            m_Log.debug("request.getParameter(\"tipo\"): " + request.getParameter("tipo"));
            dto.setUor_tipo(request.getParameter("tipo"));
            // fecha alta
            m_Log.debug("request.getParameter(\"fecha_alta\"): " + request.getParameter("fecha_alta"));
            dto.setUor_fecha_alta(request.getParameter("fecha_alta").trim());
            // fecha baja
            m_Log.debug("request.getParameter(\"fecha_baja\"): " + request.getParameter("fecha_baja"));
            dto.setUor_fecha_baja(request.getParameter("fecha_baja").trim());
            // email
            m_Log.debug("request.getParameter(\"email\"): " + request.getParameter("email"));
            dto.setUor_email(request.getParameter("email").trim());
            // no visible en registro
            if((request.getParameter("no_visible_registro") != null)&&(request.getParameter("no_visible_registro") != "")) {
                m_Log.debug("request.getParameter(\"no_visible_registro\"): " + request.getParameter("no_visible_registro"));
                dto.setUor_no_registro(request.getParameter("no_visible_registro").charAt(0));
            }
            else {
                dto.setUor_no_registro('0');
                m_Log.debug("request.getParameter(\"no_visible_registro\"): 0");
            }
            
            
            /**** prueba ****/
            // Oficina registro
            String oficina_registro = request.getParameter("oficina_registro");
            m_Log.debug("oficina_registro: " + oficina_registro);
            
            dto.setOficinaRegistro(false);
            if(oficina_registro!=null && "on".equalsIgnoreCase(oficina_registro)){
                dto.setOficinaRegistro(true);
            }
            /**** prueba ****/    


            //int insertada = uorManager.altaUOR(gVO, params);
            int insertada = UORsManager.getInstance().altaUOR(dto, params);
            if (accede.equals("si")) UORsManager.getInstance().modificarUORCodAccede(dto, params);
            if (insertada ==-1) {
                m_Log.debug("Problema con el alta");
                GeneralValueObject gVO1 = new GeneralValueObject();
                gVO1.setAtributo("respuesta","alta_existe");
                mantForm.setOtrosDatos(gVO1);
            }
            else mantForm.setOtrosDatos(null);

            opcion = "cargar";

            // recargar datos en la sesión
            m_Log.debug("Recargando datos de las UORs en la sesión");
            Vector nuevasUOR = UORsManager.getInstance().getListaUORs(true,params);
            m_Log.debug("Cargadas " + nuevasUOR.size() + " UORs");
            mantForm.setListaNuevasUORs(nuevasUOR);
            // arbol de jerarquía de uors
            ArbolImpl arbol = UORsManager.getInstance().getArbolUORs(true,false,false, params);
            m_Log.debug("Cargado árbol:" + arbol.contarNodos());
            mantForm.setArbol(arbol);
            
            //////////// SE DA PERMISOS AL USUARIO ESPECIAL SOBRE TODAS LAS ORGANIZACIONES, ENTIDADES Y UNIDAD ORGANIZATIVA RECIEN CREADA                   
                  
            // Hay que recuperar el código máximo             
            if(!this.establecerPermisosUsuarioTodo(String.valueOf(usuario.getOrgCod()),params,UORsManager.getInstance().getLastUnidadOrganizativaCreada()))
                m_Log.debug("No se puede establecer permiso al usuario especial de formularios sobre la unidad organizativa recien creada");
            else
                m_Log.debug("SE HA ESTABLECIDO PERMISO AL USUARIO DE FORMUJLARIOS SOBRE LA UNIDAD ORGANIZATIVA RECIEN CREADA");
            m_Log.debug(">>>>>>>>>>>>>>> Después de establecer permisos usuario");
            //////////// SE DA PERMISOS AL USUARIO ESPECIAL SOBRE TODAS LAS ORGANIZACIONES, ENTIDADES Y UNIDAD ORGANIZATIVA RECIEN CREADA            
        }
        else if("salir".equals(opcion)){
            if ((session.getAttribute(mapping.getAttribute()) != null))
                session.removeAttribute(mapping.getAttribute());
        }
        else{
            opcion = mapping.getInput();
            m_Log.debug("opcion en else: " + opcion);
        }
        
        return (mapping.findForward(opcion));
    }

    /**
     * Coge los nombres de las columnas de una UOR de la sesión y los escribe en el GeneralValueObject
     * @param request La request
     * @return GeneralValueObject con los nombres de las columnas
     */
    private GeneralValueObject recogerParametros(HttpServletRequest request){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codDepartamento",request.getParameter("codigoDepartamento"));
        gVO.setAtributo("codUOR",request.getParameter("codigo"));
        gVO.setAtributo("descUOR",request.getParameter("descripcion"));
        gVO.setAtributo("tipoUOR",request.getParameter("tipo"));
        gVO.setAtributo("codUORAntiguo",request.getParameter("codUORAntiguo"));
        if (m_Log.isDebugEnabled())
            m_Log.debug("VALORES " + gVO.getAtributo("codDepartamento")+"//"+
            gVO.getAtributo("codUOR")+"//"+gVO.getAtributo("descUOR")+"//"+
            gVO.getAtributo("tipoUOR")+"//"+gVO.getAtributo("codUORAntiguo"));

        return gVO;
    }
    
    
    /**
     * Este método es llamado para que cada vez que se da de alta una unidad organizativa
     * se asocie al usuario SW.FORMULARIOS permiso sobre dicha unidad 
     * @param params: Parámetros de conexión a la base de datos
     * @param idOrganizacion: Id de la organización
     * @param idEntidad: Id de la entidad
     * @param idUnidad: Id de la unidad
     * @return void
     */
    private boolean establecerPermisosUsuarioTodo(String organizacionActual,String[] params,int idUnidad) throws TechnicalException
    {
        boolean exito = false;        
        Statement st = null;
        UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
        Connection conexion = null;

        Config configuracion = ConfigServiceHelper.getConfig("formulariosPdf");
        String login = configuracion.getString("usuarioParaSGE");
        // Se obtiene el id del usuario que dispone de todos los permisos
        int idUsuario = usuarioDAO.getIdUsuario(login, params);
        m_Log.debug(">>>>>>>>>>>>>>>>>> Login del usuario especial: " + login);        
        
        String uou_uor = m_ConfigTechnical.getString("SQL.A_UOU.unidadOrg");
        String uou_usu = m_ConfigTechnical.getString("SQL.A_UOU.usuario");
        String uou_org = m_ConfigTechnical.getString("SQL.A_UOU.organizacion");
        String uou_ent = m_ConfigTechnical.getString("SQL.A_UOU.entidad");
        String uou_car = m_ConfigTechnical.getString("SQL.A_UOU.cargo");      
        String uor_cod = m_ConfigTechnical.getString("SQL.A_UOR.codigo");

        
        //Necesito insertar permisos de usuario usando el jndi del esquema generico
        String[] conexionGenerica = (String []) params.clone();
        String esqGenerico = configuracion.getString("genDataSource");
        conexionGenerica[6]=esqGenerico;
        AdaptadorSQLBD abd = new AdaptadorSQLBD(conexionGenerica);
        
        EntidadesManager entManager = EntidadesManager.getInstance();
              
        try{            
            conexion = abd.getConnection();
            conexion.setTransactionIsolation(conexion.TRANSACTION_SERIALIZABLE);
            conexion.setAutoCommit(false);
                  
            m_Log.debug(">>>>>>>>>>>>>> Insertando permisos usuario especial en todas las entidades de la organización actual");
             String sql = null;                                 
                
                // Si no existe el usuario-organización se da de alta en ESQUEMA_GENERICO.A_OUS
                if(!this.existeUsuarioOrganizacion(idUsuario, Integer.parseInt(organizacionActual), conexionGenerica)){
                    // Se inserta en A_OUS para asociar el usuario especial a la organización
                    sql = "INSERT INTO " + GlobalNames.ESQUEMA_GENERICO + "A_OUS (OUS_ORG,OUS_USU) VALUES ('" + 
                            organizacionActual + "','"  + idUsuario  + "')";
                    m_Log.debug("Consulta sql A_OUS: " + sql);
                    conexion.createStatement();
                    st = conexion.createStatement();
                    st.execute(sql);
                }                     
                
                int pos = 0;
                // Se recuperan las entidades de una determinada organización
                Vector entidades = entManager.getListaEntidades(Integer.parseInt(organizacionActual), conexionGenerica);
                m_Log.debug("Num entidades de la organización  " + organizacionActual +
                        " son: " + entidades.size());
                
                for(int j=0;j<entidades.size();j++)
                {
                    GeneralValueObject entidad = (GeneralValueObject)entidades.get(j);
                    int idEntidad = Integer.parseInt((String)entidad.getAtributo("codEntidad"));
                   // Se insertan los permisos sobre 
                    sql = "INSERT INTO " + GlobalNames.ESQUEMA_GENERICO +  
                            "A_UOU(" + uou_usu + "," + uou_org + "," + uou_ent  +
                            "," + uou_uor + "," + uou_car + ") VALUES ("
                            + idUsuario + "," + organizacionActual + "," + idEntidad + "," +
                            idUnidad + "," + COD_TODOS_LOS_CARGOS + ")";
                    
                    m_Log.debug(">>>>>>>>>>>>>>>> ejecutando: " + sql);
                    st = conexion.createStatement();
                    int rowsInserted = st.executeUpdate(sql);                    
                }
                          
                          
           conexion.commit();
           exito = true;          
        }
        catch(SQLException e){
            e.printStackTrace();
             if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());                        
             try{
                conexion.rollback();
            }
            catch(SQLException g){
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());    
            }            
        } 
        catch(BDException e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());            
        }        
        finally {
                try{
                    GeneralOperations.closeStatement(st);                    
                    abd.devolverConexion(conexion);
                } catch(Exception ex) {
                    ex.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
                }
        }
        
        return exito;
    }

    /**
     * Este método es llamado para que cada vez que se da de alta una unidad organizativa
     * se asocie al usuario SW.FORMULARIOS permiso sobre dicha unidad
     * @param params: Parámetros de conexión a la base de datos
     * @param idOrganizacion: Id de la organización
     * @param idEntidad: Id de la entidad
     * @param idUnidad: Id de la unidad
     * @return void
     */
    private boolean eliminarUorUsuarioFormularios(String organizacionActual,String[] params,int idUnidad) throws TechnicalException
    {
        boolean exito = false;
        Statement st = null;
        UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
        Connection conexion = null;

        Config configuracion = ConfigServiceHelper.getConfig("formulariosPdf");
        String login = configuracion.getString("usuarioParaSGE");
        // Se obtiene el id del usuario que dispone de todos los permisos
        int idUsuario = usuarioDAO.getIdUsuario(login, params);
        m_Log.debug(">>>>>>>>>>>>>>>>>> Login del usuario especial: " + login);

        String uou_uor = m_ConfigTechnical.getString("SQL.A_UOU.unidadOrg");
        String uou_usu = m_ConfigTechnical.getString("SQL.A_UOU.usuario");
        String uou_org = m_ConfigTechnical.getString("SQL.A_UOU.organizacion");
        String uou_ent = m_ConfigTechnical.getString("SQL.A_UOU.entidad");
        String uou_car = m_ConfigTechnical.getString("SQL.A_UOU.cargo");
        String uor_cod = m_ConfigTechnical.getString("SQL.A_UOR.codigo");


        //Necesito insertar permisos de usuario usando el jndi del esquema generico
        String[] conexionGenerica = (String []) params.clone();        
        String esqGenerico = configuracion.getString("genDataSource");
        conexionGenerica[6]=esqGenerico;
        AdaptadorSQLBD abd = new AdaptadorSQLBD(conexionGenerica);

        EntidadesManager entManager = EntidadesManager.getInstance();

        try{
            conexion = abd.getConnection();
            conexion.setTransactionIsolation(conexion.TRANSACTION_SERIALIZABLE);
            conexion.setAutoCommit(false);

            m_Log.debug(">>>>>>>>>>>>>> Eliminamos la UOR tratada para el usuario especial de formularios");
             String sql = null;

                // Si no existe el usuario-organización se da de alta en ESQUEMA_GENERICO.A_OUS
                if(this.existeUsuarioOrganizacion(idUsuario, Integer.parseInt(organizacionActual), conexionGenerica)){
                    int pos = 0;
                // Se recuperan las entidades de una determinada organización
                Vector entidades = entManager.getListaEntidades(Integer.parseInt(organizacionActual), conexionGenerica);
                m_Log.debug("Num entidades de la organización  " + organizacionActual +
                        " son: " + entidades.size());

                for(int j=0;j<entidades.size();j++)
                {
                    GeneralValueObject entidad = (GeneralValueObject)entidades.get(j);
                    int idEntidad = Integer.parseInt((String)entidad.getAtributo("codEntidad"));
                   // Se insertan los permisos sobre
                    sql = "DELETE FROM " + GlobalNames.ESQUEMA_GENERICO +
                            "A_UOU WHERE UOU_USU="+ idUsuario +" AND UOU_ORG="+ organizacionActual +
                            " AND UOU_UOR=" + idUnidad;

                    m_Log.debug(">>>>>>>>>>>>>>>> ejecutando: " + sql);
                    st = conexion.createStatement();
                    int rowsInserted = st.executeUpdate(sql);
                }
                }

           conexion.commit();
           exito = true;
        }
        catch(SQLException e){
            e.printStackTrace();
             if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
             try{
                conexion.rollback();
            }
            catch(SQLException g){
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
        }
        catch(BDException e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }
        finally {
                try{
                    GeneralOperations.closeStatement(st);
                    abd.devolverConexion(conexion);
                } catch(Exception ex) {
                    ex.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
                }
        }

        return exito;
    }
    
    
    /**
     * Comprueba si existe en la tabla A_OUS del esquema genérico un usuario y organización
     * @param idUsuario: Id del usuario
     * @param idOrganizacion: Id de la organización
     * @param params: Parámetros de conexión
     * @return Un boolean
     */
    private boolean existeUsuarioOrganizacion(int idUsuario,int idOrganizacion,String[] params){
        boolean exito = false;
        
        Connection conexion = null;
        Statement st = null;
        ResultSet rs = null;
        
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        
        OrganizacionesManager orgManager = OrganizacionesManager.getInstance();
        EntidadesManager entManager = EntidadesManager.getInstance();
        
        Vector organizaciones = orgManager.getListaOrganizaciones(params);
              
        try{            
            conexion = abd.getConnection();               
            String sql = null;       
          
            m_Log.debug("Tratando organización: " + idOrganizacion);
            // Se inserta en A_OUS para asociar el usuario especial a todas las organizaciones
            sql = "SELECT COUNT(*) AS TOTAL FROM " + GlobalNames.ESQUEMA_GENERICO + "A_OUS A_OUS WHERE OUS_ORG='" + idOrganizacion + "' AND OUS_USU='" + idUsuario + "'";
            m_Log.debug("Consulta sql A_OUS: " + sql);            
            st = conexion.createStatement();
            rs = st.executeQuery(sql);

            int num = 0;
            while(rs.next()){
                num = rs.getInt(1);
            }

            if(num>=1)
                exito = true;                           
        }
        catch(SQLException e){
            e.printStackTrace();
             if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());                        
             try{
                conexion.rollback();
            }
            catch(SQLException g){
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());    
            }            
        } 
        catch(BDException e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());            
        }        
        finally {
                try{
                    GeneralOperations.closeStatement(st);                    
                    abd.devolverConexion(conexion);
                } catch(Exception ex) {
                    ex.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
                }
        }        
        
        return exito;
    }
}