package es.altia.common.service.jndi;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.common.exception.CriticalException;
import es.altia.common.exception.FunctionalException;
import es.altia.common.exception.TechnicalException;

import java.util.*;

import javax.naming.*;
import javax.naming.directory.*;




/**
 * Wrapper class around the LDAP directory.
 *
 * @version 1.0
 */
public class BaseJNDIWrapper implements JNDIWrapper
{
    // --------------------------------------------------------------
    // Members
    // --------------------------------------------------------------

    // Class variables
    private final static Config messages = ConfigServiceHelper.getConfig("es.altia.common.service.jndi.messages");
    protected static Log log =
            LogFactory.getLog(BaseJNDIWrapper.class.getName());

    // Instance variables
    private InitialDirContext initDirContext;

    private String initContextFactory;
    private String providerURL;
    private String securityAuthentication;
    private String securityPrincipal;
    private String securityCredentials;



    // --------------------------------------------------------------
    // Constructor
    // --------------------------------------------------------------

    /**
     * Default constructor
     */
    public BaseJNDIWrapper(String theInitContextFactory,
                           String theProviderURL, String theSecurityAuthentication,
                           String theSecurityPrincipal, String theSecurityCredentials)
    {
        // Initial context factory class
        initContextFactory = theInitContextFactory;

        // URL to the LDAP server
        providerURL = theProviderURL;

        // Authentication method
        securityAuthentication = theSecurityAuthentication;

        // Login name to the ldap server
        securityPrincipal = theSecurityPrincipal;

        // Password associated with the login name
        securityCredentials = theSecurityCredentials;      
    }



    // --------------------------------------------------------------
    // Methods
    // --------------------------------------------------------------

    /**
     * Open a connection to the LDAP server
     */
    public void connect()
    {
        log.debug("connect()");

        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, initContextFactory);
        env.put(Context.PROVIDER_URL, providerURL);
        env.put(Context.SECURITY_AUTHENTICATION, securityAuthentication);
        env.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
        env.put(Context.SECURITY_CREDENTIALS, securityCredentials);

        try {
            initDirContext = new InitialDirContext(env);
        } catch (NamingException ex) {
            throw new CriticalException(this.getClass().getName(),
                                        messages.getMessage("Error.BaseJNDIWrapper.CannotConnectLdapServer", new Object[]{initContextFactory, providerURL, securityAuthentication, securityPrincipal, securityCredentials}),
                                        ex);
        }

        log.debug("connect");
    }



    /**
     * Close the connection to the LDAP server
     */
    public void disconnect()
    {
        if (initDirContext != null) {
            try {
                initDirContext.close();
            } catch (NamingException ex) {
                throw new CriticalException(this.getClass().getName(),
                                            messages.getMessage("Error.BaseJNDIWrapper.CannotDisconnectLdapServer", new Object[]{providerURL}),
                                            ex);
            } finally {
                initDirContext = null;
            }
        }
    }

    /**
     * Authenticate an entry thanks to its credentials (dn, userpassword) with a BIND
     *
     * @param theRDN                the relative distinguished name for the entry
     * @param thePassword           the userpassword value for the entry
     * @return boolean              'true' if entry is bound, 'false' otherwise
     */
     public boolean authenticateEntry(String theRdn, String thePassword)
     {
        log.debug("authenticateEntry()");

        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, initContextFactory);
        env.put(Context.PROVIDER_URL, providerURL);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, theRdn);
        env.put(Context.SECURITY_CREDENTIALS, thePassword);

        try {
            initDirContext = new InitialDirContext(env);
        } catch (NamingException ex) {
            return false;
        }
        try {
            initDirContext.close();
        } catch(NamingException ne) {
            throw new CriticalException(this.getClass().getName(),
                                        messages.getMessage("Error.BaseJNDIWrapper.CannotDisconnectLdapServer", new Object[]{""}),
                                        ne);
        }
        log.debug("authenticateEntry()");
        return true;
     }

    /**
     * Create a new entry in the LDAP directory
     *
     * @param  theDN                the distinguished name
     * @param  theObjectClasses     list of object classes that inherit this entry
     * @param  theAttributes        list (key=value) of the attributes for this entry
     * @throws TechnicalException   if a naming exception is encountered or the attributes
     *                              are not valid
     * @throws FunctionalException  if you try to create a duplicate entry
     */
    public void createEntry(String theDN, String[] theObjectClasses,
                            Map theAttributes)
            throws TechnicalException, FunctionalException
    {
        // DEBUG stuff
        if (log.isDebugEnabled()) {
            log.debug("createEntry(...)");
            log.debug(makeCreateEntryString(theDN, theObjectClasses, theAttributes));
        }

        // Be sure that we are connect
        checkConnection();

        // arg=true for ingore case
        Attributes attrs = new BasicAttributes(true);

        // Object class attribute
        Attribute oc = new BasicAttribute("objectclass");
        for (int i=0; i < theObjectClasses.length; i++) {
            oc.add(theObjectClasses[i]);
        }
        attrs.put(oc);

        // Add the attributes
        Iterator attrNames = theAttributes.keySet().iterator();
        while (attrNames.hasNext()) {

            String attrName  = attrNames.next().toString();
            Object attrValue = theAttributes.get(attrName);

            if (attrValue instanceof Vector) {

                // Multivaluate attribute
                Vector values = (Vector)attrValue;
                BasicAttribute multAttr = new BasicAttribute(attrName);
                int size = values.size();
                for (int i=0; i < size; i++) {
                    multAttr.add(values.elementAt(i).toString());
                }
                attrs.put(multAttr);
            } else {

                // Single attribute
                attrs.put(attrName, attrValue);
            }
        }

        // Create the new entry in ldap
        try {
            initDirContext.createSubcontext(theDN, attrs);
        } catch (NameAlreadyBoundException ex) {
            throw new FunctionalException(messages.getMessage("Error.BaseJNDIWrapper.CreateDuplicateEntry", new Object[]{theDN}),
                                          ex);
        } catch (InvalidAttributesException ex) {
            throw new TechnicalException(messages.getMessage("Error.BaseJNDIWrapper.InvalidAttributes", new Object[]{theAttributes}),
                                         ex);
        } catch (NamingException ex) {
            throw new TechnicalException(messages.getMessage("Error.BaseJNDIWrapper.InsertException", new Object[]{theDN}),
                                         ex);
        }
        log.debug("createdebug");
    }


    /**
     * Create a debug string use by the createEntry() method.
     *
     * @param theDN             the distinguished name
     * @param theObjectClasses  list of object classes that inherit this entry
     * @param theAttributes     list (key=value) of the attributes for this entry
     */
    private String makeCreateEntryString(String theDN, String[] theObjectClasses,
                                         Map theAttributes)
    {
        StringBuffer sb = new StringBuffer(128);
        sb.append("entry=[dn=");
        sb.append(theDN);

        sb.append(", oc={");
        for (int i=0; i < theObjectClasses.length; i++) {
            sb.append(theObjectClasses[i]);
            sb.append(", ");
        }
        sb.append("}, attrs={");

        Iterator iter = theAttributes.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next().toString();
            String val = theAttributes.get(key).toString();
            sb.append("(");
            sb.append(key);
            sb.append("=");
            sb.append(val);
            sb.append("), ");
        }
        sb.append("}]");

        return sb.toString();
    }


    /**
     * Delete an entry and all it's children in the LDAP directory
     *
     * @param  theDN                the distinguished name
     * @throws FunctionalException  if trying to delete an entry that does not exist
     * @throws TechnicalException   if trying to delete an entry that is not a context
     *                              or the context is not empty or if a naming exception
     *                              is encountered
     */
    public void deleteEntry(String theDN)
            throws TechnicalException, FunctionalException
    {
        // Debug stuff
        if (log.isDebugEnabled()) {
            log.debug("deleteEntry(" + theDN +")");
        }

        // Be sure that we are connect
        checkConnection();

        // Delete the entry
        try {
            NamingEnumeration enum1 = initDirContext.list(theDN);
            while (enum1.hasMore()) {
                NameClassPair pair = (NameClassPair)enum1.next();
                deleteEntry(pair.getName() + "," + theDN);
            }
            enum1.close();
            initDirContext.destroySubcontext(theDN);
        } catch (NameNotFoundException ex) {
            throw new FunctionalException(messages.getMessage("Error.BaseJNDIWrapper.DeleteNotFoundDN", new Object[]{theDN}),
                                          ex);
        } catch (NotContextException ex) {
            throw new TechnicalException(messages.getMessage("Error.BaseJNDIWrapper.DeleteNotContext", new Object[]{theDN}),
                                         ex);
        } catch (ContextNotEmptyException ex) {
            throw new TechnicalException(messages.getMessage("Error.BaseJNDIWrapper.DeleteContextNotEmpty", new Object[]{theDN}),
                                         ex);
        } catch (NamingException ex) {
            throw new TechnicalException(messages.getMessage("Error.BaseJNDIWrapper.DeleteException", new Object[]{theDN}),
                                         ex);
        }

        log.debug("deleteEntry");
    }



    /**
     * Retrieves selected attributes associated with a named object.
     *
     * @param  theDN                the distinguished name
     * @param  theAttrNames         the attributes name list to be retrieve
     * @throws TechnicalException   if a naming exception is encountered
     */
    public Hashtable getAttributes(String theDN, String[] theAttrNames)
            throws TechnicalException
    {
        // Debug stuff
        if (log.isDebugEnabled()) {
            log.debug("getAttributes(...)");
            log.debug(makeAttrListString(theDN, theAttrNames));
        }

        // Be sure that we are connect
        checkConnection();

        Hashtable returnValues = new Hashtable();
        try {
            Attributes attrs = initDirContext.getAttributes(theDN, theAttrNames);
            NamingEnumeration enum1 = attrs.getAll();
            while(enum1.hasMoreElements()) {

                Attribute attr = (Attribute)enum1.nextElement();
                if (attr.size() < 2) {

                    // Single attribute
                    returnValues.put(attr.getID(), attr.get());
                } else {

                    // Multivaluate attribute
                    Vector values = new Vector();
                    for (Enumeration enumValues = attr.getAll(); enumValues.hasMoreElements(); ) {
                        values.addElement(enumValues.nextElement());
                    }
                    returnValues.put(attr.getID(), values);
                }
            }
        } catch (NamingException ex) {
            throw new TechnicalException(messages.getMessage("Error.BaseJNDIWrapper.FetchException", new Object[]{theDN}),
                                         ex);
        }

        log.debug("getAttributes");

        return returnValues;
    }


    /**
     * Create a debug string use by getAttributes() method
     *
     * @param theDN         the distinguished name
     * @param theAttrNames  the attributes name list to be retrieve
     */
    private String makeAttrListString(String theDN, String[] theAttrNames)
    {
        StringBuffer sb = new StringBuffer(64);
        sb.append("search attrs=[dn=");
        sb.append(theDN);
        sb.append(", attrs={");

        for (int i=0; i < theAttrNames.length; i++) {
            sb.append(theAttrNames[i]);
            sb.append(", ");
        }
        sb.append("}]");

        return sb.toString();
    }




    /**
     * Retrieves selected attributes associated with a named object.
     *
     * @param  theDN                the distinguished name
     * @param  theAttrName          the attribute name to be retrieve
     * @throws TechnicalException   if a naming exception is encountered
     */
    public Object getAttribute(String theDN, String theAttrName)
            throws TechnicalException
    {
        Hashtable attr = getAttributes(theDN, new String[]{theAttrName});
        return attr.get(theAttrName);
    }

    /**
     * Modifies the attribute associated with a named object
     *
     * @param  theDN                the distinguished name
     * @param  theAttrName          the attribute name to be modify
     * @param  theAttrValue         the new value of the attribute
     * @throws TechnicalException   if the modifications cannot be completed successfully
     */
    public void setAttribute(String theDN, String theAttrName, Object theAttrValue)
            throws TechnicalException
    {
        // DEBUG stuff
        if (log.isDebugEnabled()) {
            String str = "setAttribute(theDN=" + theDN
                            + ", theAttrName=" + theAttrName
                            + ", theAttrValue=" + theAttrValue + ")";
            log.debug(str);
        }

        // Be sure that we are connect
        checkConnection();

        HashMap attr = new HashMap();
        attr.put(theAttrName, theAttrValue);
        setAttributes(theDN, attr);

        log.debug("setAttribute");
    }


    /**
     * Modifies the attributes associated with a named object
     *
     * @param  theDN                the distinguished name
     * @param  theAttributes        the attributes list that should be modify
     * @throws TechnicalException   if the modifications cannot be completed successfully
     */
    public void setAttributes(String theDN, Map theAttributes)
            throws TechnicalException
    {
        log.debug("setAttributes(...)");

        // Be sure that we are connect
        checkConnection();

        int attsize = theAttributes.size();
        ModificationItem[] mods = new ModificationItem[attsize];
        Iterator iter = theAttributes.keySet().iterator();
        for (int i=0; i < attsize; i++) {

            String attrName = iter.next().toString();
            Object attrValue = theAttributes.get(attrName);

            if (attrValue instanceof Vector) {

                // Multivaluate attribute
                Vector values = (Vector)attrValue;
                BasicAttribute multiAttr = new BasicAttribute(attrName);
                int vsize = values.size();
                for (int j=0; j < vsize; j++) {
                    multiAttr.add(values.elementAt(j));
                }
                mods[i] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, multiAttr);
            } else {

                // Single attribute
                mods[i] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                                               new BasicAttribute(attrName, attrValue));
            }
        }

        try {
            initDirContext.modifyAttributes(theDN, mods);
        } catch (AttributeModificationException ex) {
            throw new TechnicalException(messages.getMessage("Error.BaseJNDIWrapper.SetAttributesException", new Object[]{theDN, theAttributes}),
                                         ex);
        } catch (NamingException ex) {
            throw new TechnicalException(messages.getMessage("Error.BaseJNDIWrapper.SetAttributesException", new Object[]{theDN, theAttributes}),
                                         ex);
        }

        log.debug("setAttributes");
    }



    /**
     * Searches in a single context for objects that contain a specified
     * set of attributes, and retrieves selected attributes.
     *
     * @param  theDN                the distinguished name (or Relative DN)
     * @param  theMatchingAttr      the attributes to search for
     * @param  theAttrValues        the value of each attribute
     * @param  theSearchControls    the search controls
     * @return                      an Enumeration of SearchResult
     * @throws TechnicalException   if a naming exception is encountered
     */
    public NamingEnumeration search(String theDN, String theMatchingAttr, Object[] theAttrValues, SearchControls theSearchControls)
            throws TechnicalException
    {
        // Be sure that we are connect
        checkConnection();
        try {
            NamingEnumeration ne = initDirContext.search(theDN, theMatchingAttr, theAttrValues, theSearchControls);
            return ne;
        } catch(NamingException ne) {
            throw new TechnicalException("",ne);
        }
    }



    /**
     * If the connection is not open, we create it.
     */
    private void checkConnection()
    {
        if (initDirContext == null) {
            connect();
            return;
        }
        try {
            initDirContext.getEnvironment();
        } catch(NamingException ne) {
            connect();
        } catch(NullPointerException npe) {
            connect();
        }
    }
}
