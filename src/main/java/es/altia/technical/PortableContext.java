package es.altia.technical;

import es.altia.common.exception.*;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Classe abstraite encapsulant les méthodes d'accès au service de nommage JNDI (lookup).
 */
public abstract class PortableContext {

    /**
     * Nom de la propriété indiquant la classe à utiliser pour gérer le PortableContext
     * (suivant version spec EJB implementée), figurant dans le fichier '.properties' spécifié
     */
    protected final static String CONFIG_PROPERTY_NAME = "java.ejb.PortableContext";

    /**
     * Factory JNDI à utiliser pour ce serveur d'application pour récupérer un
     * contexte initial.
     */
    protected final static String INITIAL_CONTEXT_FACTORY_PROPERTY = "javax.naming.Context.INITIAL_CONTEXT_FACTORY";

    /**
     * URL du service de nommage JNDI pour ce serveur d'application
     */
    protected final static String PROVIDER_URL_PROPERTY = "javax.naming.Context.PROVIDER_URL";

    /**
     * Récupération des données de configuration.
     */
    protected static Config m_ConfigCommon = ConfigServiceHelper.getConfig("common");
    protected static Config m_ConfigError = ConfigServiceHelper.getConfig("error");
    
    /**
     * Récupération instance de log.
     */
    protected static Log m_Log =
            LogFactory.getLog(PortableContext.class.getName());

    /**
     * Cache de la classe d'implémentation définie dans le fichier de propriétés.
     * C'est la classe retournée par <code>getInstance()</code>
     */
    private static PortableContext m_PortableContext;

    /**
     * Constructeur de la classe <code>PortableContext</code>.
     *
     * P00049 VMA init. variables de classe.
     *
     */
    public PortableContext() {

        // Récupération des fichiers de propriétés.

    }
    /**
     * Recupère une instance sur la classe PortableContext correspondant à l'implementation
     * de la specification EJB (paramètrée dans le fichier '.properties'
     *
     * @return 		PortableContext			Instancia de la classe PortableContext1_0 o PortableContext1_1 o PortableContext2_0
     * @exception 	TechnicalException
     *
     */
    public static PortableContext getInstance() throws TechnicalException {

        if (m_PortableContext == null) {

            // On sychronise pour gérer le multi threading. On aurait pu synchroniser la
            // méthode toute entière mais pour des meilleures performances je préfère
            // synchroniser que dans le cas ou l'instance m_PortableContext n'existe pas
            // (ce qui est rare : une seule à priori dans la vie de l'application).

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
     * Methode de lookup pour récupérer une information dans le service de nommage (passée en paramètre) :
     *
     * @param param		 			Paramètre recherché
     * @param type 					Classe du type de l'information recherchée
     *
     * @return 						L'information recherchée : le client devra la 'caster' dans le type passé en paramètre 'type'
     * @exception TechnicalException
     */
    public abstract Object lookup(String param, Class type) throws TechnicalException;
}