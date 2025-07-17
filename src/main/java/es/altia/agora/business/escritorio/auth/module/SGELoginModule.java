package es.altia.agora.business.escritorio.auth.module;

import es.altia.agora.business.escritorio.persistence.manual.UsuarioDAO;
import es.altia.agora.business.escritorio.exception.UsuarioNoValidadoException;
import es.altia.common.exception.TechnicalException;
import es.altia.jaas.module.NamePrincipal;

import javax.security.auth.spi.LoginModule;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import javax.security.auth.callback.*;
import java.util.Map;
import java.io.IOException;

public class SGELoginModule implements LoginModule {

    private CallbackHandler callbackHandler;

    private String userLogin;
    private String userPassword;
    private Subject subject;

    private boolean suceeded;

    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState,
                           Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
    }

    public boolean login() throws LoginException {
        suceeded = attemptAuthentication();
        return suceeded;
    }

    public boolean commit() throws LoginException {
        return suceeded;
    }

    public boolean abort() throws LoginException {
        return suceeded;
    }

    public boolean logout() throws LoginException {
        return true;
    }

    private boolean attemptAuthentication() throws LoginException {

        retrievePassFromHandler();
        retrieveLoginFromHandler();

        try {
            UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
            boolean isValidated = usuarioDAO.validarUsuario(userLogin, userPassword);

            subject.getPrincipals().add(new NamePrincipal(userLogin));

            return isValidated;

        } catch (UsuarioNoValidadoException unve) {
            throw new LoginException(unve.getMessage());
        } catch (TechnicalException te) {
            throw new LoginException(te.getMessage());
        }
    }

    private void retrievePassFromHandler() throws LoginException {

        try {
            Callback[] callbacks = new Callback[1];
            callbacks[0] = new PasswordCallback("User Password", false);

            callbackHandler.handle(callbacks);
            userPassword = new String(((PasswordCallback)callbacks[0]).getPassword());

        } catch (IOException ioe) {
            throw new LoginException(ioe.getMessage());
        } catch (UnsupportedCallbackException uce) {
            throw new LoginException(uce.getMessage());
        }
    }

    private void retrieveLoginFromHandler() throws LoginException {

        try {
            Callback[] callbacks = new Callback[1];
            callbacks[0] = new NameCallback("User Login:");

            callbackHandler.handle(callbacks);
            userLogin = ((NameCallback)callbacks[0]).getName();

        } catch (IOException ioe) {
            throw new LoginException(ioe.getMessage());
        } catch (UnsupportedCallbackException uce) {
            throw new LoginException(uce.getMessage());
        }
    }

}
