package es.altia.common.service.jndi;

import es.altia.common.exception.FunctionalException;
import es.altia.common.exception.TechnicalException;

import java.util.Map;
import java.util.Hashtable;
import javax.naming.*;
import javax.naming.directory.*;


/**
 * Interface for wrapping ldap implementation class that is returned
 * by JNDIService.getLog() method
 *
 * @version 1.0
 */
public interface JNDIWrapper
{
    /**
     * Open a connection to the LDAP server
     */
    public void connect();


    /**
     * Close the connection to the LDAP server
     */
    public void disconnect();


    /**
     * Authenticate an entry thanks to its credentials (dn, userpassword) with a BIND
     *
     * @param theRDN                the relative distinguished name for the entry
     * @param thePassword           the userpassword value for the entry
     * @return boolean              'true' if entry is bound, 'false' otherwise
     */
     public boolean authenticateEntry(String theRDN, String thePassword);

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
            throws TechnicalException, FunctionalException;


    /**
     * Delete an entry in the LDAP directory
     *
     * @param  theDN                the distinguished name
     * @throws FunctionalException  if trying to delete an entry that does not exist
     * @throws TechnicalException   if trying to delete an entry that is not a context
     *                              or the context is not empty or if a naming exception
     *                              is encountered
     */
    public void deleteEntry(String theDN)
            throws TechnicalException, FunctionalException;


    /**
     * Retrieves selected attributes associated with a named object.
     *
     * @param  theDN                the distinguished name
     * @param  theAttrNames         the attributes name list to be retrieve
     * @throws TechnicalException   if a naming exception is encountered
     */
    public Hashtable getAttributes(String theDN, String[] theAttrNames)
            throws TechnicalException;

    /**
     * Retrieves selected attributes associated with a named object.
     *
     * @param  theDN                the distinguished name
     * @param  theAttrName          the attribute name to be retrieve
     * @throws TechnicalException   if a naming exception is encountered
     */
    public Object getAttribute(String theDN, String theAttrName)
            throws TechnicalException;


    /**
     * Modifies the attribute associated with a named object
     *
     * @param  theDN                the distinguished name
     * @param  theAttrName          the attribute name to be modify
     * @param  theAttrValue         the new value of the attribute
     * @throws TechnicalException   if the modifications cannot be completed successfully
     */
    public void setAttribute(String theDN, String theAttrName, Object theAttrValue)
            throws TechnicalException;


    /**
     * Modifies the attributes associated with a named object
     *
     * @param  theDN                the distinguished name
     * @param  theAttributes        the attributes list that should be modify
     * @throws TechnicalException   if the modifications cannot be completed successfully
     */
    public void setAttributes(String theDN, Map theAttributes)
            throws TechnicalException;


    /**
     * Searches in a single context for objects that contain a specified
     * set of attributes, and retrieves selected attributes.
     * theMatchingAttr parameter can have some parameterized values
     * Each parameter of this String is put with {0}, {1},...
     * And Each parameter value must be contained by theAttrValues parameter
     *
     * Example of using:
     * SearchControls sc = new SearchControls();
     * // Search in the Sub-tree of LDAP and not in the Base DN
     * sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
     * // Return only the DN and the class object
     * sc.setReturningObjFlag(false);
     * // Return only one element
     * sc.setCountLimit(1);
     * // Bind an user with the login theUserLogin and password theUserPassword
     * Vector result = ldapMapper.search("cn=theUserLogin",
     *                                   "(objectclass=irfUser)(userpassword={0})",
     *                                   new String[] {"theUserPassword"},
     *                                   sc);
     *
     * @param  theDN                the distinguished name (or Relative DN)
     * @param  theMatchingAttr      the attributes to search for
     * @param  theAttrValues        the value of each attribute
     * @param  theSearchControls    the search controls
     * @return                      an Enumeration of SearchResult
     * @throws TechnicalException   if a naming exception is encountered
     */
    public NamingEnumeration search(String theDN, String theMatchingAttr, Object[] theAttrValues, SearchControls theSearchControls)
            throws TechnicalException;
}
