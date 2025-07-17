package es.altia.agora.business.escritorio.persistence;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.exception.UsuarioNoEncontradoException;
import es.altia.agora.business.escritorio.persistence.manual.UsuarioDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.*;
import es.altia.common.service.config.*;
import es.altia.jaas.facade.AuthenticationFacade;
import es.altia.jaas.facade.AuthenticationModuleFacade;
import es.altia.jaas.exception.ProcessAuthenticationException;
import es.altia.jaas.exception.ConfigAuthenticationException;
import es.altia.jaas.exception.AuthenticationException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.exceptions.InternalErrorException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.util.Vector;

public class UsuarioManager {

	// Mi propia instancia usada en el metodo getInstance.
	private static UsuarioManager instance = null;

	// Para el fichero de configuracion technical.
	protected static Config m_ConfigTechnical;
	// Para el fichero de mensajes de error localizados.
    protected static Config m_ConfigError;
    protected static Log m_Log =
            LogFactory.getLog(UsuarioManager.class.getName());


    protected UsuarioManager() {
	  //Queremos usar el fichero de configuración technical	  
	  m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
	  //Queremos tener acceso a los mensajes de error localizados
	  m_ConfigError = ConfigServiceHelper.getConfig("error");
    }


    /**
     * Factory method para el <code>Singelton</code>.
     * @return La unica instancia de UsuarioManager
     */
    public static UsuarioManager getInstance() {
	  //Si no hay una instancia de esta clase tenemos que crear una.
	  if (instance == null) {
		// Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
		synchronized(UsuarioManager.class) {
		    if (instance == null)
			  instance = new UsuarioManager();
		}
	  }
	  return instance;
    }

    public UsuarioEscritorioValueObject buscaUsuario(UsuarioEscritorioValueObject usuarioEscritorioVO) {

        return buscaUsuario(usuarioEscritorioVO, false);
    }
    
    public UsuarioEscritorioValueObject buscaUsuario(UsuarioEscritorioValueObject usuarioEscritorioVO, boolean usuarioNoBaja) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("buscaUsuario");

        try{
                usuarioEscritorioVO = UsuarioDAO.getInstance().loadUsuario(usuarioEscritorioVO, usuarioNoBaja);
        }catch (TechnicalException te) {
                m_Log.error("JDBC Technical problem " + te.getMessage());
        }catch (UsuarioNoEncontradoException unee) {
                m_Log.error("User not found problem " + unee.getMessage());
        }finally{
                return usuarioEscritorioVO;
        }
    }

    public int getNumeroIdiomas(UsuarioEscritorioValueObject usuarioEscritorioVO) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("getNumeroIdiomas");
        int numIdiomas = 0;
		try{
			numIdiomas = UsuarioDAO.getInstance().getNumeroIdiomas(usuarioEscritorioVO);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}finally{
			return numIdiomas;
		}
	}
    

    public UsuarioEscritorioValueObject buscaUsuario(String nif) throws UsuarioNoEncontradoException, TechnicalException {
        //queremos estar informados de cuando este metodo es ejecutado
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("buscaUsuario(" + nif + ")");
        }
        
        try {
            return UsuarioDAO.getInstance().loadUsuarioForCert(nif);
            
        } catch (InternalErrorException iee) {
            throw new TechnicalException(iee.getMessage(), iee);
        }            
    }//buscaUsuario
    
    
    public UsuarioEscritorioValueObject buscaUsuario(int idUsuario) {
		//queremos estar informados de cuando este metodo es ejecutado
		if(m_Log.isDebugEnabled()) m_Log.debug("buscaUsuario("+idUsuario+")");
        UsuarioEscritorioValueObject usuarioEscritorioVO = null;
		try{
			usuarioEscritorioVO = UsuarioDAO.getInstance().loadUsuarioWithId(idUsuario);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}catch (UsuarioNoEncontradoException unee) {
			m_Log.error("User not found problem " + unee.getMessage());
		}finally{
			return usuarioEscritorioVO;
		}//try-catch
	}//buscaUsuario
    /* ******************************************************** */


     public UsuarioEscritorioValueObject buscaUsuarioDniE(String nif) throws UsuarioNoEncontradoException, TechnicalException {
        //queremos estar informados de cuando este metodo es ejecutado
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("buscaUsuario(" + nif + ")");
        }

        try {
            return UsuarioDAO.getInstance().loadUsuarioForDniE(nif);

        } catch (InternalErrorException iee) {
            throw new TechnicalException(iee.getMessage(), iee);
        }
    }//buscaUsuario



    public Vector getListaUsuarios(String[] params) throws TechnicalException {
        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("getListaUsuarios");
        return UsuarioDAO.getInstance().getListaUsuarios(params);
    }

	public UsuarioValueObject buscaAppEnCurso(UsuarioValueObject usuarioVO) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("buscaAplicacionEnCurso");

		try{
			usuarioVO = UsuarioDAO.getInstance().loadAppEnCurso(usuarioVO);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}finally{
			return usuarioVO;
		}
	}

	 public UsuarioEscritorioValueObject buscaApp(UsuarioEscritorioValueObject usuarioEscritorioVO) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("buscaAplicacion");

		try{
			usuarioEscritorioVO = UsuarioDAO.getInstance().loadApp(usuarioEscritorioVO);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}finally{
			return usuarioEscritorioVO;
		}
	}
	
          public UsuarioEscritorioValueObject buscaCssGeneral(UsuarioEscritorioValueObject usuarioEscritorioVO) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("buscaAplicacion");

		try{
			usuarioEscritorioVO = UsuarioDAO.getInstance().buscaCssGeneral(usuarioEscritorioVO);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}finally{
			return usuarioEscritorioVO;
		}
	}
            public UsuarioValueObject buscaCss(UsuarioValueObject usuarioVO) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("buscaAplicacion");
		try{
			usuarioVO = UsuarioDAO.getInstance().buscaCss(usuarioVO);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}finally{
			return usuarioVO;
		}
	}
	
	
	public int comprobarClave(UsuarioEscritorioValueObject usuarioEscritorioVO,String claveAntigua) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("comprobarClave");
		int resultado = 0;

		try{
			resultado = UsuarioDAO.getInstance().comprobarClave(usuarioEscritorioVO,claveAntigua);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}finally{
			return resultado;
		}
	}
        public boolean esAdmin(UsuarioValueObject usuarioVO){
            //queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug(" Estamos en el UsuarioManager esAdmin??");
              
                boolean encontrado=false;
                try{
                    
                    
             encontrado=UsuarioDAO.getInstance().esAdmin(usuarioVO);
             m_Log.debug(" Valor de Admin en UsuarioManager :"+encontrado);
                    
               }catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}finally{
			return encontrado;
		}
            
        }
	
	public int modificarClave(UsuarioEscritorioValueObject usuarioEscritorioVO,String claveNueva) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("modificarClave");
		int resultado = 0;

		try{
			resultado = UsuarioDAO.getInstance().modificarClave(usuarioEscritorioVO,claveNueva);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}finally{
			return resultado;
		}
	}

	public Vector buscaOrg(UsuarioValueObject usuarioVO, String filtro) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("buscaOrganizaciones_Entidades");

		Vector org_ent = new Vector();

		try{
			org_ent = UsuarioDAO.getInstance().loadOrg(usuarioVO, filtro);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}finally{
			return org_ent;
		}
	}

	public String soloConsultarExpedientes(UsuarioValueObject usuarioVO) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("soloConsultarExpedientes");

		String resultado = "";

		try{
			resultado = UsuarioDAO.getInstance().soloConsultarExpedientes(usuarioVO);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
			resultado = null;
		}finally{
			return resultado;
		}
	}
	
	public String soloConsultarPadron(UsuarioValueObject usuarioVO) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("soloConsultarPadron");

		String resultado = "";

		try{
			resultado = UsuarioDAO.getInstance().soloConsultarPadron(usuarioVO);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
			resultado = null;
		}finally{
			return resultado;
		}
	}

	public Vector buscaUnidadOrg(UsuarioValueObject usuarioVO) {
		Vector vecUOR = new Vector();

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("buscaUnidadesOrganicas");

		try{
			vecUOR = UsuarioDAO.getInstance().loadUnidadOrg(usuarioVO);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}finally{
			return vecUOR;
		}
	}
	
        
        /**
         * Recupera las unidades organizativas sobre las que tiene permiso un usuario
         * @param usuarioVO
         * @return
         */
        public Vector<UORDTO> buscaUnidadOrgInforme(UsuarioValueObject usuarioVO) {
		Vector vecUOR = new Vector();

		//queremos estar informados de cuando este metodo es ejecutado
		try{
			vecUOR = UsuarioDAO.getInstance().cargarUnidadOrganizativa(usuarioVO);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}finally{
			return vecUOR;
		}
	}

	public String buscaURL(UsuarioValueObject usuarioVO) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("buscaURL");

		String url = null;

		try{
			url = UsuarioDAO.getInstance().loadURL(usuarioVO);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}finally{
			return url;
		}
	}
	

	public UsuarioValueObject buscaMantenimiento(UsuarioValueObject usuarioVO) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("buscaMantenimiento");

		try{
			usuarioVO = UsuarioDAO.getInstance().loadMantenimiento(usuarioVO);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}finally{
			return usuarioVO;
		}
	}

	public int getGrupo(UsuarioValueObject usuarioVO) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("getGrupo");
		int grupo = 0;

		try{
			grupo = UsuarioDAO.getInstance().getGrupo(usuarioVO);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}finally{
			return grupo;
		}
	}

	public String getNombre(String login) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("getNombre");
		String nombre = "";

		try{
			nombre = UsuarioDAO.getInstance().getNombre(login);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}finally{
			return nombre;
		}
	}

    /**
     * Audita el acceso del usuario a la aplicacion.
     * 
     * @param idUsuario
     * @return 
     */
    public boolean auditarAccesoAplicacion(Integer idUsuario) {
        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("auditarAccesoAplicacion");
        boolean insertado = false;

        try {
            if (UsuarioDAO.getInstance().
                    insertarAuditoriaAccesoModulo(null, ConstantesDatos.CODIGO_ACCESO_FLEXIA, idUsuario) > 0) {
                
                insertado = true;
            }
        } catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
        }
        
        return insertado;
    }
    
    /**
     * Audita el acceso del usuario a los modulos.
     * 
     * @param idOrganizacion
     * @param idAplicacion
     * @param idUsuario
     * @return 
     */
    public boolean auditarAccesoModulo(Integer idOrganizacion, Integer idAplicacion, Integer idUsuario) {
        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("auditarAccesoModulo");
        boolean insertado = false;

        try {
            if (UsuarioDAO.getInstance().
                    insertarAuditoriaAccesoModulo(idOrganizacion, idAplicacion, idUsuario) > 0) {
                
                insertado = true;
            }
        } catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
        }
        
        return insertado;
    }
    
	public int bloquearUsuario(UsuarioEscritorioValueObject usuarioVO, int estado) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("bloquearUsuario");
		int resultado = -1;

		try{
			resultado = UsuarioDAO.getInstance().bloquearUsuario(usuarioVO, estado);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}finally{
			return resultado;
		}
	}
	
	public int getBloqueado(UsuarioEscritorioValueObject usuarioVO) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("getBloqueado");
		int resultado = -1;

		try{
			resultado = UsuarioDAO.getInstance().getBloqueado(usuarioVO);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}finally{
			return resultado;
		}
	}

        public int getEliminado (UsuarioEscritorioValueObject usuarioVO) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("getBloqueado");
		int resultado = -1;

		try{
			resultado = UsuarioDAO.getInstance().getEliminado(usuarioVO);
		}catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}finally{
			return resultado;
		}
	}

	public String[] loadParametrosConexion(UsuarioValueObject usuarioVO) throws TechnicalException {
        return UsuarioDAO.getInstance().loadParametrosConexion(usuarioVO);        
    }

    public UsuarioEscritorioValueObject validarUsuario(UsuarioEscritorioValueObject userVO) {

        try {

            AuthenticationFacade facade = new AuthenticationModuleFacade();
            String loginValidated = facade.authenticate(userVO.getLogin(), userVO.getPassword());
            userVO.setLogin(loginValidated);
            userVO = UsuarioDAO.getInstance().loadUsuario(userVO);
            
        } catch (UsuarioNoEncontradoException unee) {
            m_Log.error("NO SE HA ENCONTRADO EL USUARIO");
            //unee.printStackTrace();
        } catch (TechnicalException te) {
            m_Log.error("HA OCURRIDO UN ERROR TÉCNICO");
            te.printStackTrace();
        } catch (ProcessAuthenticationException pae) {
            //pae.printStackTrace();
        } catch (ConfigAuthenticationException cae) {
            cae.printStackTrace();
        }

        return userVO;
    }

    public void validarUsuarioAlta(UsuarioEscritorioValueObject userVO) throws AuthenticationException {

        AuthenticationFacade facade = new AuthenticationModuleFacade();
        facade.authenticate(userVO.getLogin(), userVO.getPassword());

    }

    public Calendar getFechaAltaPasswordReciente (UsuarioEscritorioValueObject usuario){

       Calendar fechaAltaPasswordReciente=null;

       m_Log.debug("fechaAltaPasswordReciente");

       try {

           fechaAltaPasswordReciente = UsuarioDAO.getInstance().getFechaAltaPasswordReciente(usuario);

       m_Log.debug("Fecha de Alta Password obtenida");

       //We want to be informed when this method has finalized
       m_Log.debug("getFechaAltaPasswordReciente");

       } catch (Exception ce) {
               fechaAltaPasswordReciente = null;
       m_Log.error("JDBC Technical problem " + ce.getMessage());
      }

        return fechaAltaPasswordReciente;
  }

    /**
     * Obtiene la fecha del ultimo acceso a la aplicacion por el usuario.
     * 
     * @param idUsuario
     * @return 
     */
    public Calendar getFechaUltimoAcceso(Integer idUsuario) {
        Calendar fechaUltimoAcceso = null;
        m_Log.debug("fechaUltimoAcceso");

        try {
            fechaUltimoAcceso = UsuarioDAO.getInstance().getFechaUltimoAcceso(idUsuario);
            
            m_Log.debug("Fecha de ultimo acceso obtenida");
            m_Log.debug("getFechaUltimoAcceso");
        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
        }

        return fechaUltimoAcceso;
    }

   public boolean  modificarContrasenhaCaducada(String login, String password){

          boolean modificarContrasenhaCaducada = false;
   
         m_Log.debug("modificarContrasenhaCaducada");
    try {
     
       modificarContrasenhaCaducada = UsuarioDAO.getInstance().modificarContrasenhaCaducada(login, password);

     } catch (Exception ce) {
        m_Log.error("JDBC Technical problem " + ce.getMessage());
     }
    return modificarContrasenhaCaducada;
 }



 public boolean  existeContrasenha(String login, String password){

          boolean existeContrasenha = false;

         m_Log.debug("existeContrasenha");
    try {

       existeContrasenha = UsuarioDAO.getInstance().existeContrasenha(login, password);

     } catch (Exception ce) {
        m_Log.error("JDBC Technical problem " + ce.getMessage());
     }
    return existeContrasenha;
 }

 
 public boolean existeLogin(String login){
     m_Log.debug("exiteLogin()");
     boolean existeLogin = false;
     
     try{
      
         existeLogin = UsuarioDAO.getInstance().existeLogin(login);
     
     } catch(Exception e){
         m_Log.error("JDBC Technical problem " + e.getMessage());
     } 
     return existeLogin;
 }

    /**
     * Recupera el login de un usuario a partir de su código interno
     * @param codUsuario: Código del usuario
     * @param params: Parámetros de conexión a la base de datos
     * @return String con el login o null sino se ha podido recuperar
     */
    public String getLoginUsuario(int codUsuario,String[] params){
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        String login = null;
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            login = UsuarioDAO.getInstance().getLoginUsuario(codUsuario, con);

        }catch(BDException e){
            e.printStackTrace();
            
        }finally{
            try{
                adapt.devolverConexion(con);
                
            }catch(Exception e){
                m_Log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }

        return login;
    }

    /**
     * Recupera mediante la operación getCodOficinaRegistro de la clase UsuarioDAO el código de la oficina de registro asociada a la unidad
     * de registro sobre la cual tenemos permisos
     * 
     * En el caso de que no tengamos permisos en una oficina o tengamos permisos en mas de una se devolvera un null por que esto no es correcto.
     * 
     * @param codUsuario Código del usuario en Flexia
     * @param codUnidadRegistro Código de la unidad de registro seleccionada
     * @param params Parámetros de conexión a la BBDD
     * @return Integer con el código de la oficina o un null en caso de que existan permisos en mas de una oficina o no existan permisos
     */
    public Integer getCodOficinaRegistro (int codUsuario, int codUnidadRegistro, String[] params){
        if(m_Log.isDebugEnabled()) m_Log.debug("getCodOficinaRegistro ( codUsuario = " + codUsuario + " codUnidadRegistro = " + codUnidadRegistro + " ) : BEGIN");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        Integer codOficinaRegistro = null;
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            codOficinaRegistro = UsuarioDAO.getInstance().getCodOficinaRegistro(codUsuario, codUnidadRegistro, con, params[6]);
        }catch(BDException e){
            m_Log.error("Se ha producido un error recuperando la oficina de registro del usuario = " + codUsuario, e);
        }finally{
            try{
                adapt.devolverConexion(con);       
            }catch(Exception e){
                m_Log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("código oficina registro = " + codOficinaRegistro);
        if(m_Log.isDebugEnabled()) m_Log.debug("getCodOficinaRegistro() : END");
        return codOficinaRegistro;
    }//getCodOficinaRegistro
    
    /**
     * Recupera mediante la función getNumOficinasRegistroPermiso de la clase UsuarioDAO el número de oficinas hijas de la unidad de registro
     * sobre las cuales tenemos permisos.
     * @param codOrganizacion: Código de la organización
     * @param codUsuario Código de usuario en Flexia
     * @param codUnidadRegistro Código de la unidad de registro seleccionada
     * @param params Parámetros de conexión a la BBDD
     * @return Integer con el número de oficinas sobre las que tenemos permiso
     */
    public Integer getNumOficinasRegistroPermiso (int codOrganizacion,int codUsuario, int codUnidadRegistro, String[] params){
        if(m_Log.isDebugEnabled()) m_Log.debug("getNumOficinasRegistroPermiso ( codUsuario = " + codUsuario + " codUnidadRegistro = " + codUnidadRegistro + " ) : BEGIN");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        Integer numOficinasRegistroPermiso = null;
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            numOficinasRegistroPermiso = UsuarioDAO.getInstance().getNumOficinasRegistroPermiso(codOrganizacion,codUsuario, codUnidadRegistro, con);
        }catch(BDException e){
            m_Log.error("Se ha producido un error recuperando la oficina de registro del usuario = " + codUsuario, e);
        }finally{
            try{
                adapt.devolverConexion(con);       
            }catch(Exception e){
                m_Log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("numero de permisos en oficinas de registro = " + numOficinasRegistroPermiso);
        if(m_Log.isDebugEnabled()) m_Log.debug("getNumOficinasRegistroPermiso() : END");
        return numOficinasRegistroPermiso;
    }//getCodOficinaRegistro
 

    
    public ArrayList<UORDTO> loadUnidadTipoRegistro(UsuarioValueObject usuarioVO,String[] params) throws TechnicalException {
        
        ArrayList<UORDTO> uors = new ArrayList<UORDTO>();                
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();         
            uors = UsuarioDAO.getInstance().loadUnidadTipoRegistro(usuarioVO, con);
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                m_Log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }            
        }        
        return uors;
    }
 
    
    
    /**
    * Comprueba si un usuario tiene permiso sobre determinadas unidades organizativas para las que se restringue la posibilidad
    * de visualizar unas determinadas pantallas definidas a nivel de expediente o de módulo
    * @return 
    */
    public boolean tieneUsuarioPermisoSobreUnidadesOrganizativas(int codOrganizacion,int codUsuario,String[] unidades,String[] params){        
        boolean exito = false;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
        try{
            
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            
            for(int i=0;i<unidades.length;i++){
                String codUnidad = unidades[i];
                if(UsuarioDAO.getInstance().tienePermisoUsuarioSobreUnidad(codOrganizacion,codUsuario, Integer.parseInt(codUnidad), con)){
                    exito = true;
                    break;
                }
            }// for
            
       }catch(BDException e){
           e.printStackTrace();
           m_Log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
       }finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                e.printStackTrace();
                m_Log.error("Error al cerrar una conexión a la BBDD: " + e.getMessage());
            }
        }
       
       return exito;
   }
    
    
    
    /**
    * Comprueba si un usuario tiene permiso sobre determinadas unidades organizativas para las que se restringue la posibilidad
    * de visualizar unas determinadas pantallas definidas a nivel de expediente o de módulo
    * @return 
    */
    public boolean tieneUsuarioPermisoSobreUnidadesOrganizativas(int codOrganizacion,int codUsuario,String[] unidades,Connection con){        
        boolean exito = false;

        
        try{
            
            for(int i=0;i<unidades.length;i++){
                String codUnidad = unidades[i];
                if(UsuarioDAO.getInstance().tienePermisoUsuarioSobreUnidad(codOrganizacion,codUsuario, Integer.parseInt(codUnidad), con)){
                    exito = true;
                    break;
                }
            }// for
            
       }catch(Exception e){
           e.printStackTrace();
           m_Log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
       }
       
       return exito;
   }
    
    
    
    
     /**
      * Comprueba si existen dos logins iguales (uno en maiusculas y otro en minusculas)
      * @param login, el login que queremos ver si existe en maiúsculas y minúsculas
      * @return boolean, true si hay dos iguales (uno en maiusculas, otro en minusculas), false en caso contrario
      */
    public boolean comprobarSiHayDos(String login) throws TechnicalException {  
      
        m_Log.debug("ComprobarSiHayDos.BEGIN.El login pasado es:"+ login);
        boolean resultado = false;
        
        
        try{
            resultado=UsuarioDAO.getInstance().comprobarSiHayDos(login);
       
       }catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}
       catch(Exception e){
           e.printStackTrace();
           m_Log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
      }    
       m_Log.debug("ComprobarSiHayDos.END.El resultado devuelto es: "+ resultado);
       return resultado;
   }
    
    
     /**
      * Método que se utiliza para la funcionalidad de login insensitivo
      * La precondición es que ya se ha comprobado que en el sistema exista el
      * login bien sea en mayúsculas o en minúsculas.
      * Se devuelve el login tal y como está en el sistema, si está en mayúsculas
      * lo devuelve en mayúsculas, si está en minúsculas lo devuelve en minúsculas.
      * @param login login que queremos devolver tal cual está en el sistema
      * @param String  login tan cual está en el sistema
      */
    public String dameElCorrecto(String login) throws TechnicalException {  
      
        m_Log.debug("dameElCorrecto.BEGIN.El login pasado es:"+ login);
        String resultado = "";
        
        String loginMaiusculas=login.toUpperCase();
        String loginMinusculas=login.toLowerCase();
        boolean maiusculas=false;
        boolean minusculas=false;
       try{
           
           maiusculas=UsuarioDAO.getInstance().existeLogin(loginMaiusculas);
           minusculas=UsuarioDAO.getInstance().existeLogin(loginMinusculas); 
            
       
       }catch (TechnicalException te) {
			m_Log.error("JDBC Technical problem " + te.getMessage());
		}
       catch(Exception e){
           e.printStackTrace();
           m_Log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
      }    
      if(maiusculas){
          resultado=loginMaiusculas;
      } else if (minusculas){
          resultado=loginMinusculas;
      } else return null;
       m_Log.debug("dameElcorrectosEND.El resultado devuelto es: "+ resultado);
       return resultado;
   }
    
    
    /**
     * Recupera el nombre completo de un usuario
     * @param codUsuario: Código del usuario
     * @param params Parametros para obtener la conexion a la BD
     * @return String el nombre completo de un usuario
     * @throws es.altia.common.exception.TechnicalException
     */
     public String getNombreUsuario(String[] params,int codUsuario)
            throws TechnicalException {  
         
        String resultado=""; 
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();         
            resultado = UsuarioDAO.getInstance().getNombreUsuario(params, codUsuario);
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                m_Log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }            
        }        
        return resultado;
    }
     
     public ArrayList<GeneralValueObject> getListaUsuariosFiltrados(String codOrg, String codProc, String codUOR, String[] params) throws BDException, SQLException{
         ArrayList<GeneralValueObject> listado = null;
         AdaptadorSQLBD adapt = null;
         Connection con = null;
         
         try {
             adapt = new AdaptadorSQLBD(params);
             con = adapt.getConnection();
             
             if(codUOR!=null && !codUOR.equals(""))
                 listado = UsuarioDAO.getInstance().getListaUsuariosPorUOR(codOrg, codUOR, con);
             else if(codProc!=null && !codProc.equals("")){
                 listado = UsuarioDAO.getInstance().getListaUsuariosPorProcedimiento(codProc, con);
             }
        } catch(BDException bdex){
            m_Log.error("Error al obtener una conexión a la BBDD");
            bdex.printStackTrace();
            throw bdex;
        } catch(SQLException sqlex){
            m_Log.error("Error al recuperar la lista de usuarios: "+sqlex.getMessage());
            sqlex.printStackTrace();
            throw sqlex;
        } finally {
            try {
                if(con!=null) con.close();
            } catch (Exception e){
                m_Log.debug("Se ha producido un error al cerrar la conexión a la BBDD");
                throw new SQLException("Se ha producido un error al cerrar la conexión a la BBDD");
            }
         }
         return listado;
     }

     
}