package es.altia.agora.interfaces.user.web.escritorio;

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
import es.altia.util.date.DateOperations;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import org.apache.struts.action.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.StringTokenizer;
import java.util.Vector;

public final class ModificarContrasenhaCaducadaAction extends Action {

    protected static Log mlog =
            LogFactory.getLog(EntradaAction.class.getName());
    protected static Config m_Conf = ConfigServiceHelper.getConfig("common");
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Config configParams = ConfigServiceHelper.getConfig("ApplicationResources");
        String username = null;
        String password = null;
        Vector iconos = null;
        int maxIntentos = m_Conf.getInt("LOGON.maxIntentos");
        //mlog.error("******** maxIntentos= " + maxIntentos);
        Config m_Config = ConfigServiceHelper.getConfig("authentication");
        String mmm = m_Config.getString("Auth/modules");
        StringTokenizer tokens=new StringTokenizer(mmm,";");
        boolean loginSIGP = false;
        while (tokens.hasMoreElements()) {
            //m_Log.debug (tokens.nextToken());
            if ("SGE".equals(tokens.nextToken())) loginSIGP=true;
        }
        

        try {

            UsuarioEscritorioValueObject usuarioEscritorioVO = (UsuarioEscritorioValueObject)session.getAttribute("usuarioEscritorio");
          
            // Comprobamos si la validacion del usuario es a traves de LOGIN/PASSWORD
            String pass = ((ModificarContrasenhaCaducadaForm) form).getPassword();
//            if (pass != null) {
//                username = usuarioEscritorioVO.getLogin();
//                password = pass;
//            }
            // Comprobamos si la validacion del usuario es a traves de LOGIN/PASSWORD
            String NuevaPassword = ((ModificarContrasenhaCaducadaForm) form).getNuevaPassword();
            usuarioEscritorioVO.setPassword(password);
           
            UsuarioManager usuarioManager = UsuarioManager.getInstance();
            usuarioEscritorioVO = UsuarioManager.getInstance().buscaCssGeneral(usuarioEscritorioVO);
            usuarioEscritorioVO.setTipoLogin(loginSIGP);
            
            String codUsuario = String.valueOf(usuarioEscritorioVO.getIdUsuario());
            String PASSWORD_USUARIO_RENOVADA = ((ModificarContrasenhaCaducadaForm) form).getRenovacion();

            request.setAttribute("RENOVACION_PASSWORD_PROXIMO_ACCESO",PASSWORD_USUARIO_RENOVADA);
            
              if(!usuarioManager.existeContrasenha(codUsuario, NuevaPassword)){

                    usuarioManager.modificarContrasenhaCaducada(codUsuario, NuevaPassword);

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
                    
                    
                    /***************************************************************************************/
                    /** SI AL USUARIO SE LE HA RENOVADO LA CONTRASEÑA TRAS UN PRIMER ACCESO, HAY QUE 
                        ACTUALIZAR EL CAMPO A_USU.USU_CAMBIO_PASSWORD PARA QUE, EN EL PROXIMO ACCESO NO SE LE 
                        SOLICITE LA INTRODUCCIÓN DE UNA NUEVA CONTRASEÑA 
                    /***************************************************************************************/
                    if(PASSWORD_USUARIO_RENOVADA!=null && "SI".equalsIgnoreCase(PASSWORD_USUARIO_RENOVADA)){
                        UsuarioDAO.getInstance().cambiarCambioPasswordObligatorio(usuarioEscritorioVO.getIdUsuario(),false);
                    }
                    
                    
                    /***************************************************************************************/
                    /***************************************************************************************/

                    mlog.debug("LogonAction: Antes de findForward a successEscritorio");
                    return mapping.findForward("successEscritorio");


                   
                }else{
                     mlog.debug("EXISTE CONTRASEÑA");
                     ActionErrors errors = new ActionErrors();
                     errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.username.existePass"));
                     saveErrors(request, errors);
                      return mapping.findForward("cambiaPass");

                }
                

        } catch (Exception e) {
            mlog.error("Excepcion validando usuario en LogonAction");
            e.printStackTrace();
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.validate.exception"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
    }


      private void numeroIdiomas(UsuarioManager usuarioManager, UsuarioEscritorioValueObject usuarioVO) {
        int numIdiomas = usuarioManager.getNumeroIdiomas(usuarioVO);
        if (numIdiomas > 1) {
            usuarioVO.setMultipleIdioma(true);
        } else {
            usuarioVO.setMultipleIdioma(false);
        }
    }
      
      
            }
            
