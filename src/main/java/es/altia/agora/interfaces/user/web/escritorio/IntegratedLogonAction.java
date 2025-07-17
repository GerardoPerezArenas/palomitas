package es.altia.agora.interfaces.user.web.escritorio;

import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.DERValue;
import es.altia.jaas.exception.ConfigAuthenticationException;
import es.altia.jaas.exception.ProcessAuthenticationException;
import es.altia.jaas.facade.AuthenticationFacade;
import es.altia.jaas.facade.AuthenticationModuleFacade;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import sun.misc.BASE64Decoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

public class IntegratedLogonAction extends Action {

    protected static Log mlog = LogFactory.getLog(IntegratedLogonAction.class.getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) {
        
        UsuarioManager usuarioManager = UsuarioManager.getInstance();
        ActionErrors errors = new ActionErrors();                                 
        String auth = request.getHeader("Authorization");

        if (auth == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("WWW-Authenticate", "Negotiate");
            return null;
        }

        if (auth.startsWith("Negotiate ")) {

            try {

                byte[] msg = new BASE64Decoder().decodeBuffer(auth.substring(10));
                if (isKerberosToken(msg)) {

                    String opcionRedirect;
                    try {
                        mlog.debug("********************************************************************************************************HEMOS RECUPERADO EL TOKEN SPNEGO NECESARIO");
                        mlog.debug("AUTENTICAMOS...");
                        AuthenticationFacade facade = new AuthenticationModuleFacade();
                        String userAuth = facade.authenticate(msg);
                        mlog.debug("USUARIO AUTENTICADO: " + userAuth);

                        UsuarioEscritorioValueObject usuarioEscritorioVO = new UsuarioEscritorioValueObject();
                        usuarioEscritorioVO.setLogin(userAuth);

                        usuarioEscritorioVO = usuarioManager.buscaUsuario(usuarioEscritorioVO);
                        if (usuarioEscritorioVO.getIdUsuario() != 0) {
                            usuarioEscritorioVO = usuarioManager.buscaApp(usuarioEscritorioVO);
                            usuarioEscritorioVO = usuarioManager.buscaCssGeneral(usuarioEscritorioVO);
                              mlog.debug("************    CSS USUARIO ..."+usuarioEscritorioVO.getCss());
                            
                            // Se audita el acceso a la aplicacion
                            usuarioEscritorioVO.setFechaUltimoAcceso(usuarioManager.getFechaUltimoAcceso(usuarioEscritorioVO.getIdUsuario()));
                            usuarioManager.auditarAccesoAplicacion(usuarioEscritorioVO.getIdUsuario());
                            
                            request.getSession().setAttribute("usuarioEscritorio", usuarioEscritorioVO);
                            request.getSession().setAttribute("usuarioLoginNT", "yes");
                            opcionRedirect = "exito";
                        } else {
                            opcionRedirect = "error";
                        }
                    } catch (ConfigAuthenticationException cae) {
                        cae.printStackTrace();
                        opcionRedirect = "error";
                    } catch (ProcessAuthenticationException pae) {
                        pae.printStackTrace();
                        opcionRedirect = "error";
                    }

                    return mapping.findForward(opcionRedirect);


                } else if (msg[8] == 1) { // first step of authentication
                    // this part is for full hand-shaking, just tested, didn't care about result passwords
                    byte z = 0;
                    byte[] msg1 = {(byte) 'N', (byte) 'T', (byte) 'L', (byte) 'M', (byte) 'S', (byte) 'S', (byte) 'P', z,
                            (byte) 2, z, z, z, z, z, z, z,
                            (byte) 40, z, z, z, (byte) 1, (byte) 130, z, z,
                            z, (byte) 2, (byte) 2, (byte) 2, z, z, z, z, // this line is 'nonce'
                            z, z, z, z, z, z, z, z};
                    // remove next lines if you want see the result of first step
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setHeader("WWW-Authenticate", "Negotiate " + new sun.misc.BASE64Encoder().encodeBuffer(msg1).trim());
                    return null;
                } else if (msg[8] == 3) { // third step of authentization - takes long time, nod needed if zou care only for loginname

                    String opcionRedirect;
                    try {
                        mlog.debug("HEMOS RECUPERADO EL TOKEN NTLM NECESARIO");
                        mlog.debug("AUTENTICAMOS...");
                        AuthenticationFacade facade = new AuthenticationModuleFacade();
                        String userAuth = facade.authenticate(msg);
                        mlog.debug("USUARIO AUTENTICADO: " + userAuth);
                        UsuarioEscritorioValueObject usuarioEscritorioVO = new UsuarioEscritorioValueObject();
                        usuarioEscritorioVO.setLogin(userAuth);

                       /////////////////Se comprueba si el login es insensible a maiusculas o no
                        Config m_Config = ConfigServiceHelper.getConfig("authentication");
                        String insensitivo=m_Config.getString("Auth/insentivoMaiusculas");
                        mlog.debug("IntegratedLogonAction. Propiedad Login Insensitivo Maiusculas: "+insensitivo);

                        //El login ALTIA y altia tiene que ser el mismo  
                        Boolean hayDos=true;
                         if("SI".equals(insensitivo)){
     
                           //Tenemos que ir a BD a comprobar si existen dos logins que sean iguales en maiuscula y minuscula Y si sí lanzar error
                            hayDos=usuarioManager.comprobarSiHayDos(userAuth);
                             //Si aparece dos veces el usuario, lanzamos el error  
                            if (hayDos){
                                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.username.ExistTwo"));
                                opcionRedirect="error";
                                saveErrors(request, errors);
                                return mapping.findForward(opcionRedirect);

                           }else {
                                // Si sólo aparece una vez el usuario, tenemos que 
                                //pasar a la aplicación el login, en la forma correcta
                                //es decir, si el login está en la BD en mayúsculas en mayúsculas
                                //y si está en minúsculas en minúsculas.
                                userAuth=usuarioManager.dameElCorrecto(userAuth);
                                usuarioEscritorioVO.setLogin(userAuth);
                            }
                       } 
                        
                        ///////////////////////////////////////////////////////
                        usuarioEscritorioVO = usuarioManager.buscaUsuario(usuarioEscritorioVO);
                        
                        if (usuarioEscritorioVO.getIdUsuario() != 0) {
                            usuarioEscritorioVO = usuarioManager.buscaApp(usuarioEscritorioVO);
                            
                            // Se audita el acceso a la aplicacion
                            usuarioEscritorioVO.setFechaUltimoAcceso(usuarioManager.getFechaUltimoAcceso(usuarioEscritorioVO.getIdUsuario()));
                            usuarioManager.auditarAccesoAplicacion(usuarioEscritorioVO.getIdUsuario());
                            
                            request.getSession().setAttribute("usuarioEscritorio", usuarioEscritorioVO);
                            request.getSession().setAttribute("usuarioLoginNT", "yes");
                            opcionRedirect = "exito";
                        } else {
                            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.validate.exception"));
                            opcionRedirect = "error";
                        }

                    } catch (ConfigAuthenticationException cae) {
                        opcionRedirect = "error";
                    } catch (ProcessAuthenticationException pae) {
                        opcionRedirect = "error";
                    }                    
                    saveErrors(request, errors);
                    return mapping.findForward(opcionRedirect);

                } else return null;

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    private boolean isKerberosToken(byte[] rawToken) {

        DERValue tmpToken;
        try {
            tmpToken = new DERValue(rawToken);

            if (tmpToken.getTag() != (byte) 0x60) {
                mlog.debug("EL TOKEN NO COMIENZA POR 0x60");
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }





}
