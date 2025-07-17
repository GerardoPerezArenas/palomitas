package es.altia.technical;

import es.altia.common.exception.*;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Classe abstraite encapsulant les m�thodes d'acc�s au service de nommage JNDI (lookup).
 */
public abstract class PortableContext {

    /**
     * Nom de la propri�t� indiquant la classe � utiliser pour g�rer le PortableContext
     * (suivant version spec EJB implement�e), figurant dans le fichier '.properties' sp�cifi�
     */
    protected final static String CONFIG_PROPERTY_NAME = "java.ejb.PortableContext";

    /**
     * Factory JNDI � utiliser pour ce serveur d'application pour r�cup�rer un
     * contexte initial.
     */
    protected final static String INITIAL_CONTEXT_FACTORY_PROPERTY = "javax.naming.Context.INITIAL_CONTEXT_FACTORY";

    /**
     * URL du service de nommage JNDI pour ce serveur d'application
     */
    protected final static String PROVIDER_URL_PROPERTY = "javax.naming.Context.PROVIDER_URL";

    /**
     * R�cup�ration des donn�es de configuration.
     */
    protected static Config m_ConfigCommon = ConfigServiceHelper.getConfig("common");
    protected static Config m_ConfigError = ConfigServiceHelper.getConfig("error");
    
    /**
     * R�cup�ration instance de log.
     */
    protected static Log m_Log =
            LogFactory.getLog(PortableContext.class.getName());

    /**
     * Cache de la classe d'impl�mentation d�finie dans le fichier de propri�t�s.
     * C'est la classe retourn�e par <code>getInstance()</code>
     */
    private static PortableContext m_PortableContext;

    /**
     * Constructeur de la classe <code>PortableContext</code>.
     *
     * P00049 VMA init. variables de classe.
     *
     */
    public PortableContext() {

        // R�cup�ration des fichiers de propri�t�s.

    }
    /**
     * Recup�re une instance sur la classe PortableContext correspondant � l'implementation
     * de la specification EJB (param�tr�e dans le fichier '.properties'
     *
     * @return 		PortableContext			Instancia de la classe PortableContext1_0 o PortableContext1_1 o PortableContext2_0
     * @exception 	TechnicalException
     *
     */
    public static PortableContext getInstance() throws TechnicalException {

        if (m_PortableContext == null) {

            // On sychronise pour g�rer le multi threading. On aurait pu synchroniser la
            // m�thode toute enti�re mais pour des meilleures performances je pr�f�re
            // synchroniser que dans le cas ou l'instance m_PortableContext n'existe pas
            // (ce qui est rare : une seule � priori dans la vie de l'application).

            synchronized (PortableContext.class) {

                if (m_PortableContext == null) {

                    String className = m_ConfigCommon.getString(CONFIG_PROPERTY_NAME);

                    if (className == null)
                        throw new TechnicalException(m_ConfigError.getString("Erreur.PortableContext.ClassName"));
                    try {

                        Class c = Class.forName(className);
                        m_PortableContext = (PortableContext) c.newInstance();

                        if(m_Log.isDebugEnabled()) m_Log.debug("Instancia [" + m_PortableContext.getClass().getName() + "] creada.");

                    } catch (Exception e) {
                        throw new TechnicalException(m_ConfigError.getString("Erreur.PortableContext.Instance"), e);
                    }
                }
            }

        }

        return m_PortableContext;
    }
    /**
     * Methode de lookup pour r�cup�rer une information dans le service de nommage (pass�e en param�tre) :
     *
     * @param param		 			Param�tre recherch�
     * @param type 					Classe du type de l'information recherch�e
     *
     * @return 						L'information recherch�e : le client devra la 'caster' dans le type pass� en param�tre 'type'
     * @exception TechnicalException
     */
    public abstract Object lookup(String param, Class type) throws TechnicalException;
}