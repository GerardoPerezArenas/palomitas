package es.altia.agora.interfaces.user.web.escritorio;

import es.altia.agora.business.escritorio.UserPreferences;
import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.business.escritorio.persistence.manual.UsuarioDAO;
import es.altia.agora.business.geninformes.utils.Utilidades;
import es.altia.agora.interfaces.user.web.util.CambioIdiomaAction;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.data.Licencia;
import es.altia.agora.technical.data.LicenciaAplicacion;
import es.altia.agora.technical.xmldata.LicenseFactory;
import es.altia.agora.util.Constantes;
import es.altia.common.service.config.*;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.service.mail.MailHelper;
import es.altia.util.StringUtils;

import es.altia.util.date.DateOperations;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import org.apache.struts.action.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Vector;
import java.util.UUID;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import org.apache.struts.actions.DispatchAction;

public final class LogonAction extends DispatchAction {

    protected static Log mlog =
            LogFactory.getLog(EntradaAction.class.getName());
    protected static Config m_Conf = ConfigServiceHelper.getConfig("common");

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UsuarioManager usuarioManager = UsuarioManager.getInstance();
        Config configParams = ConfigServiceHelper.getConfig("ApplicationResources");
        int idUsuario = -1;
        String nombreUsu = null;
        int idiomaEsc = -1;
        String username = null;
        String login=null;
        String password = null;
        UserPreferences preferences = new UserPreferences();
        Vector iconos = null;
        int intentos = 1;
        int maxIntentos = m_Conf.getInt("LOGON.maxIntentos");
        //mlog.error("******** maxIntentos= " + maxIntentos);
        int estadoBloqueo, estadoEliminado;
        String usuarioSesion;
        Config m_Config = ConfigServiceHelper.getConfig("authentication");
        String mmm = m_Config.getString("Auth/modules");
        StringTokenizer tokens=new StringTokenizer(mmm,";");
        boolean loginSIGP = false;
        while (tokens.hasMoreElements()) {
            //m_Log.debug (tokens.nextToken());
            if ("SGE".equals(tokens.nextToken())) loginSIGP=true;
        }
    
        //Se comprueba si el login es insensible a maiusculas o no
        String insensitivo=m_Config.getString("Auth/insentivoMaiusculas");
        mlog.debug("LogonAction. Propiedad Login Insensitivo Maiusculas: "+insensitivo);
        
       //El login ALTIA Y altia tiene que ser el mismo  
       username = ((LogonForm) form).getLogin();
       Boolean hayDos=true;
      
       try {
           
        if("SI".equals(insensitivo)){
       
            login = ((LogonForm) form).getLogin();
           //Tenemos que ir a BD a comprobar si existen dos logins que sean iguales en maiusculas y minusculas Y si sí lanzar error
           
            hayDos=usuarioManager.comprobarSiHayDos(login);
             //Si aparece dos veces el usuario, lanzamos el error  
            if (hayDos){
            
                ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.username.ExistTwo"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
           
           } else {
                // Si sólo aparece una vez el usuario, tenemos que 
                //pasar a la aplicación el login, en la forma correcta
                //es decir, si el login está en la BD en mayúsculas en mayúsculas
                //y si está en minúsculas en minúsculas.
                username=usuarioManager.dameElCorrecto(login);
            }
       } 
     
            // Comprobamos si la validacion del usuario es a traves de LOGIN/PASSWORD
            String pass = ((LogonForm) form).getPassword();
            if (pass != null) {
               
                password = pass;
            }
            UsuarioEscritorioValueObject usuarioEscritorioVO = new UsuarioEscritorioValueObject(idUsuario, nombreUsu, idiomaEsc, username, password, preferences, iconos);
            usuarioEscritorioVO = usuarioManager.validarUsuario(usuarioEscritorioVO);

            usuarioEscritorioVO = usuarioManager.buscaCssGeneral(usuarioEscritorioVO);
            usuarioEscritorioVO.setTipoLogin(loginSIGP);

            estadoBloqueo = usuarioManager.getBloqueado(usuarioEscritorioVO);
            estadoEliminado = usuarioManager.getEliminado(usuarioEscritorioVO);

            if (estadoBloqueo == 0 && estadoEliminado==0) {
                Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
                String passwordCaducidad = m_ConfigTechnical.getString(ConstantesDatos.PROPIEDAD_PASSWORD_CADUCIDAD);
                String passwordPeriodoUnidad = m_ConfigTechnical.getString(ConstantesDatos.PROPIEDAD_PERIODO_UNIDAD);
                String passwordPeriodoCantidad =  m_ConfigTechnical.getString(ConstantesDatos.PROPIEDAD_PERIDODO_CANTIDAD);

                int passUnidad = Integer.parseInt(passwordPeriodoUnidad);
                int passCantidad = Integer.parseInt(passwordPeriodoCantidad);
        // Si usuario validado se mete en la session
        if (usuarioEscritorioVO.getIdUsuario() != -1) {
            // si esta activada la caducidad de la contraseña
            if(!passwordCaducidad.equals("no")){

                Calendar fechaActual = Calendar.getInstance();
                Calendar fechaAltaPasswordReciente =  usuarioManager.getFechaAltaPasswordReciente(usuarioEscritorioVO);
 
                switch (passUnidad) {
                    //dias
                    case 0: {
                          fechaAltaPasswordReciente.add(Calendar.DAY_OF_MONTH, passCantidad);
                    break;
                    }
                    //meses
                    case 1: {
                        fechaAltaPasswordReciente.add(Calendar.MONTH, passCantidad);
                    break;
                    }
                    //años
                    case 2: {
                        fechaAltaPasswordReciente.add(Calendar.YEAR, passCantidad);
                    break;
                    }

                   }
               
                //La fecha de la ultima password es mayor que la fecha actual
                if (fechaActual.after(fechaAltaPasswordReciente)) {
                    //se le redirige hacia la pantalla de cambio de contraseña
                    session.setAttribute("usuarioEscritorio", usuarioEscritorioVO);
                    return mapping.findForward("cambiaPass");
                }
            }
            
            
            
            /********************************************************************************************/
            /**** Se comprueba si hay que obligar a que la password tenga un nº minimo de caracteres ****/
            /********************************************************************************************/
            String RESTRICCION_LONGITUD_PASSWORD = "NO";
            try{
                RESTRICCION_LONGITUD_PASSWORD = m_Conf.getString("password_longitud");
                if(RESTRICCION_LONGITUD_PASSWORD!=null && !"".equals(RESTRICCION_LONGITUD_PASSWORD)){
                    int num = Integer.parseInt(RESTRICCION_LONGITUD_PASSWORD);
                    // Si no salta ninguna excepción, es porque el valor de la propiedad es numérico
                }
                
            }catch(Exception e){
                // Si se ha producido algún error al recuperar el valor de la propiedad, no se tiene
                // en cuenta una longitud mínima de password
                RESTRICCION_LONGITUD_PASSWORD = "NO";
            }            
            request.setAttribute("LONGITUD_MINIMA_PASSWORD",RESTRICCION_LONGITUD_PASSWORD);
            
            
            
            /********************************************************************************/
            /**** Se comprueba si hay que obligar al usuario a que cambie su contraseña  ****/
            /********************************************************************************/
            if(UsuarioDAO.getInstance().esNecesarioRenovarPasswordUsuario(username)){
                session.setAttribute("usuarioEscritorio", usuarioEscritorioVO);
                request.setAttribute("RENOVACION_PASSWORD_PROXIMO_ACCESO","SI");
                return mapping.findForward("cambiaPass");                
            }
            
            
            
           
            

                    // Fijamos el locale en función del idioma del usuario)
                    Locale oLocale = CambioIdiomaAction.getCadenaLocale(usuarioEscritorioVO.getIdiomaEsc());
                    session.setAttribute("org.apache.struts.action.LOCALE", oLocale);           // para que tenga efecto en new ActionError ...
                    session.setAttribute("javax.servlet.jsp.jstl.fmt.locale.session", oLocale); // para que tenga efecto en <fmt:message ...

                    usuarioEscritorioVO = usuarioManager.buscaApp(usuarioEscritorioVO);
                    this.numeroIdiomas(usuarioManager, usuarioEscritorioVO);
                    
                    File file = new File("licenses.xml");
                    if(file!=null && file.exists()){
                        mlog.debug("@@@@@LoginAction: El fichero licenses.xml existe en: " + file.getPath());
                    }
                    else 
                        mlog.debug("@@@@@LoginAction: El fichero licenses.xml no existe");
                    
                    /*********************/
                    mlog.debug("@@@@@@ Recuperando factoria del servlet context");
                    LicenseFactory factoria = (LicenseFactory) getServlet().getServletContext().getAttribute(ConstantesDatos.APPLICATION_LICENSE);
                    Vector permisosAplicaciones = usuarioEscritorioVO.getIconos();
                    mlog.debug("@@@@@@ Recuperando permisos sobre modulos de la licencia");
                    //ArrayList<Licencia> modulos = factoria.getLicenses(Constantes.LICENCIA_MODULO);
                    session.setAttribute(pass, pass);
                    int salto = 3;
                    
                    Vector nuevosPermisos = new Vector();
                    if(factoria!=null)
                    {
                        ArrayList<Licencia> modulos = factoria.getLicenses(Constantes.LICENCIA_MODULO);
                                                                       
                        for (Iterator<String> it = permisosAplicaciones.iterator(); it.hasNext();) 
                        {
                            String cod = it.next();
                            String nombre = it.next();
                            String ico = it.next();

                            
                             boolean guardarPermiso = false;
                            for(int z=0;z<modulos.size();z++)
                            {                               
                                LicenciaAplicacion lapp = (LicenciaAplicacion)modulos.get(z);
                                if(lapp.getFirma()!=null && factoria.verifyEncryption(lapp))
                                {
                                    if(lapp.getCodAplicacion().equals(cod))
                                    {                                        
                                        String fechaExpiracion = lapp.getFechaExpiracion();                                        
                                        if(Utilidades.isLong(fechaExpiracion) && fechaExpiracion.length()==8){
                                          
                                            // Se genera el calendar
                                            Calendar fecha = Calendar.getInstance();
                                            fecha.clear();
                                            fecha.clear(Calendar.DAY_OF_MONTH);
                                            fecha.clear(Calendar.MONTH);
                                            fecha.clear(Calendar.YEAR);                                           
                                            
                                            int day   = Integer.parseInt(fechaExpiracion.substring(0,2));
                                            int month = Integer.parseInt(fechaExpiracion.substring(2,4));
                                            int year  = Integer.parseInt(fechaExpiracion.substring(4,8));                                            
                                            
                                            fecha.set(Calendar.DAY_OF_MONTH,day);                                           
                                            fecha.set(Calendar.MONTH,DateOperations.getMonthCalendar(month));                                            
                                            fecha.set(Calendar.YEAR,year);                                            
                                            fecha.set(Calendar.HOUR_OF_DAY,0);                                            
                                            fecha.set(Calendar.MINUTE,0);                                            
                                            fecha.set(Calendar.SECOND,0);
                                            
                                            // Se la fecha no ha caducado se muestra el modulo
                                            if(Utilidades.isDateGreaterOrEqualThanActual(fecha)){                                                
                                                guardarPermiso = true;
                                            }
                                            else
                                                mlog.debug("El modulo " + cod + " ha caducado");
                                        }
                                        else 
                                        if(fechaExpiracion.equals(ConstantesDatos.FECHA_NUNCA_LICENCIA_UPPER) || fechaExpiracion.equals(ConstantesDatos.FECHA_NUNCA_LICENCIA_LOWER))    
                                        {                                          
                                            guardarPermiso =true;
                                        }
                                        
                                        if(guardarPermiso){
                                            nuevosPermisos.add(cod);                                           
                                            nuevosPermisos.add(nombre);
                                            nuevosPermisos.add(ico);
                                            break;
                                        }
                                    }     
                                }
                                else mlog.debug("para modulo " + cod + " la firma no es correcta => Modulo no accesible");
                            }
                                                       
                        }
                    }// if factoria !=null
                    else mlog.debug("Factoria nula");
                   
                    // Se establecen los nuevos permisos en base a los que están definidos en la licencia
                    if(nuevosPermisos!=null && nuevosPermisos.size()>0)
                        usuarioEscritorioVO.setIconos(nuevosPermisos);  
                    //usuarioEscritorioVO.setIconos(permisosAplicaciones);
                    
                    // Se audita el acceso a la aplicacion
                    usuarioEscritorioVO.setFechaUltimoAcceso(usuarioManager.getFechaUltimoAcceso(usuarioEscritorioVO.getIdUsuario()));
                    usuarioManager.auditarAccesoAplicacion(usuarioEscritorioVO.getIdUsuario());
                    
                    /*********************/
                    session.setAttribute("usuarioEscritorio", usuarioEscritorioVO);
                    // Remove the obsolete form bean
                    if (mapping.getAttribute() != null) {
                        if ("request".equals(mapping.getScope())) {
                            request.removeAttribute(mapping.getAttribute());
                        } else {
                            session.removeAttribute(mapping.getAttribute());
                    }
                    }

                    mlog.debug("LogonAction: Antes de findForward a successEscritorio");
                    return mapping.findForward("successEscritorio");
                } else {

                    try {
                        usuarioSesion = session.getAttribute("usuario").toString();

                        if (usuarioSesion.equals(username)) {
                            intentos = 1 + Integer.parseInt((String) session.getAttribute("intentosLogin"));
                        } else {
                            intentos = 1;
                        }

                        mlog.debug("El usuario: " + username + " lleva " + intentos + " intentos fallidos de login.");

                    } catch (Exception e) {
                        mlog.debug("El usuario: " + username + " lleva 1 intento fallido de login.");
                    }

                    session.setAttribute("usuario", username);
                    session.setAttribute("intentosLogin", Integer.toString(intentos));

                    try {
                        maxIntentos = configParams.getInt("Escritorio.login.MaxIntentos");
                    } catch (Exception e) {
                        mlog.error("No se ha podido resolver el numero maximo de intentos de login.");
                    }

                    if (intentos < maxIntentos) {
                        mlog.debug("USUARIO/PASSWORD NO VALIDOS");
                        ActionErrors errors = new ActionErrors();
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.username.notExist"));
                        saveErrors(request, errors);

                    } else {

                        // Se bloquea el usuario al tercer intento fallido de login
                        if (intentos == maxIntentos) {
                            try {
                                usuarioManager.bloquearUsuario(usuarioEscritorioVO, 1);
                            } catch (Exception e) {
                                mlog.error("El usuario con login " + username + " ha sido bloqueado.");
                            }
                        }

                        mlog.debug("USUARIO/PASSWORD NO VALIDOS");
                        ActionErrors errors = new ActionErrors();
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.username.blocked"));
                        saveErrors(request, errors);

                    }
                }

            } else if ((estadoEliminado!=0) && (estadoBloqueo != 0) && (usuarioEscritorioVO.getNombreUsu() != null)) { //usuario válido pero bloqueado
                mlog.debug("USUARIO/PASSWORD NO VALIDOS");
                ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.username.blocked"));
                saveErrors(request, errors);
            } else if ((estadoBloqueo != 0) && (usuarioEscritorioVO.getNombreUsu() == null)
                    || (estadoBloqueo==0 && estadoEliminado!=0)) {//usuario no válido
                mlog.debug("USUARIO/PASSWORD NO VALIDOS");
                ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.username.notExist"));
                saveErrors(request, errors);
            }

            return (new ActionForward(mapping.getInput()));

        } catch (Exception e) {
            mlog.error("Excepcion validando usuario en LogonAction");
            e.printStackTrace();
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.validate.exception"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
    }

    /**
     * Metodo utilizado para introducir en la sesión el número de idiomas existentes para que en caso de que exista
     * más de uno pueda seleccionarse dentro de la aplicación el correcto
     *
     * @param usuarioVO Datos del usuario en donde se encuentran los parametros de conexion
     * @param usuarioManager Manager para acceder a los datos del usuario.
     */
    private void numeroIdiomas(UsuarioManager usuarioManager, UsuarioEscritorioValueObject usuarioVO) {
        int numIdiomas = usuarioManager.getNumeroIdiomas(usuarioVO);
        if (numIdiomas > 1) {
            usuarioVO.setMultipleIdioma(true);
        } else {
            usuarioVO.setMultipleIdioma(false);
        }
    }

    public boolean validarNumeroIntentos(HttpSession session){
            int intentosMaximos  = m_Conf.getInt("LOGON.maxIntentos");
            int intentosActuales = (Integer) session.getAttribute("intentosCodVerificacion");
            if(intentosMaximos >= intentosActuales){
                    return true;
            }else{
                    return false;
            }
    }

}
