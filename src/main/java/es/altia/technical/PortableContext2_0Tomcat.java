package es.altia.technical;

import javax.naming.*;
import javax.rmi.*;
import javax.sql.*;
import java.text.*;
import es.altia.common.exception.*;

/**
 * Classe encapsulant les m�thodes d'acc�s au service de nommage JNDI (lookup) pour WebLogic.
 * <ui>
 * 		<li>Recherche de connection � la BD (via un objet DataSource)</li>
 * 		<li>Recherche de la r�f�rence d'un EJB</li>
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
 * Cr�e un contexte JNDI qui sera utilis� par les m�thodes <code>lookup()</code>.
 * 
 * @return le context JNDI � utiliser
 * @exception TechnicalException Si probl�me lors de l'initialisation du contexte JNDI
 *
 */
private InitialContext getInitialContext() throws TechnicalException {

	if (m_InitialContext == null) {
	
		// Gestion du multi-threading. Plusieurs clients peuvent avoir la m�me
		// instance de cette classe car cette classe est retourn�e par la m�thode
		// statique <code>PortableContext.getInstance()</code>. Il faut donc
		// g�rer la synchronisation.
		
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
 * M�thode permettant de r�cup�rer la r�f�rence d'un objet dans le service de nommage.
 * Aujourd'hui, utilis�e seulement pour les sources de donn�es (jdbc) et les ejb.
 *
 * @param cl� le nom de l'objet recherch� dans le service de nommage (JNDI).
 * @param type la classe de l'objet recherch�.
 * @return l'objet recherch�.
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
