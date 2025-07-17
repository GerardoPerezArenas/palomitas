package es.altia.technical;

import javax.naming.*;
import javax.rmi.*;
import javax.sql.*;
import java.text.*;
import es.altia.common.exception.*;

/**
 * Classe encapsulant les méthodes d'accès au service de nommage JNDI (lookup) pour WebLogic.
 * <ui>
 * 		<li>Recherche de connection à la BD (via un objet DataSource)</li>
 * 		<li>Recherche de la référence d'un EJB</li>
 * </ui>
 */
public class PortableContext2_0Tomcat extends PortableContext {

	private InitialContext m_InitialContext;
/**
 * Commentaire relatif au constructeur PortableContext2_0WebLogic.
 */
public PortableContext2_0Tomcat() {
	super();
}
/**
 * Crée un contexte JNDI qui sera utilisé par les méthodes <code>lookup()</code>.
 * 
 * @return le context JNDI à utiliser
 * @exception TechnicalException Si problème lors de l'initialisation du contexte JNDI
 *
 */
private InitialContext getInitialContext() throws TechnicalException {

	if (m_InitialContext == null) {
	
		// Gestion du multi-threading. Plusieurs clients peuvent avoir la même
		// instance de cette classe car cette classe est retournée par la méthode
		// statique <code>PortableContext.getInstance()</code>. Il faut donc
		// gérer la synchronisation.
		
		synchronized (this) {
			if (m_InitialContext == null) {
				try {
                    m_InitialContext = new InitialContext();
				} catch (Exception e) {
					throw new TechnicalException(
						m_ConfigError.getString("Erreur.PortableContext2_0.ParamJNDI"), e);
				}

			}
			
		}
	}
	
	return m_InitialContext;
}
/**
 * Méthode permettant de récupérer la référence d'un objet dans le service de nommage.
 * Aujourd'hui, utilisée seulement pour les sources de données (jdbc) et les ejb.
 *
 * @param clé le nom de l'objet recherché dans le service de nommage (JNDI).
 * @param type la classe de l'objet recherché.
 * @return l'objet recherché.
 * @exception <{TechnicalException}>
 */
public Object lookup(String cle, Class type) throws TechnicalException {
	
	try {
		if (m_Log.isDebugEnabled()) m_Log.debug("Recuperacion JNDI del objeto [" + cle + "].");
		
		InitialContext jndiContext = getInitialContext();
        Context envCtx = (Context) jndiContext.lookup("java:comp/env");
        Object valeur = (Object) envCtx.lookup(cle);
    
		if (m_Log.isDebugEnabled()) m_Log.debug("Objeto JNDI [" + cle + "] recuperado.");
		
		if (cle.startsWith("jdbc")) {
			return (DataSource) valeur;
		} else
			return PortableRemoteObject.narrow(valeur,type);
	} catch (TechnicalException e) {
		
		throw e;
		
	} catch (NamingException e) {
		
		String message = MessageFormat.format(
			m_ConfigError.getString("Erreur.PortableContext2_0.ErreurJNDI"), new Object[] { cle });
		
		throw new TechnicalException( message, e);
		
	} catch (Exception e) {
		
		throw new TechnicalException(
			m_ConfigError.getString("Erreur.PortableContext2_0.ErreurLookup"), e);
	}
}
}
