/*______________________________BOF_________________________________*/
package es.altia.util.commons;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.configuration.ConfigurationParametersManager;
import es.altia.util.configuration.MissingConfigurationParameterException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @version $\Date$ $\Revision$
 */
public class ReflectionOperations {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
    private static final Log _log =
            LogFactory.getLog(ReflectionOperations.class.getName());

    /*_______Operations_____________________________________________*/
    private ReflectionOperations() {   }

    public static final Object loadClassAndGetInstance(String className)
            throws InternalErrorException {
        try {
            return Class.forName(className).newInstance();
        } catch (Exception e) {
            _log.error("ReflectionOperations: loadClassAndGetInstance() exception thrown ",e);
            throw new InternalErrorException(e);
        }//try-catch
    }//loadClassAndGetInstance

    public static final Object loadClassAndGetInstanceFromParameter(String classNameParameter)
            throws InternalErrorException {
        try {
            final String className = ConfigurationParametersManager.getParameter(classNameParameter);
            return loadClassAndGetInstance(className);
        } catch (MissingConfigurationParameterException e) {
            _log.error("ReflectionOperations: loadClassAndGetInstanceFromParameter() missing parameter exception thrown ",e);
            throw new InternalErrorException(e);
        } catch (Exception e) {
            _log.error("ReflectionOperations: loadClassAndGetInstanceFromParameter() exception thrown ",e);
            throw new InternalErrorException(e);
        }//try-catch
    }//loadClassAndGetInstanceFromParameter

    /**
     * Tries to load classNames[0], if can't... classNames[i]
     *
     * Use only when necessary because it's a heavy method because
     * of Class.forName and exception handling into a loop.
     *
     * @param classNames the array with classes names
     * @return the first class it could load... if none null is returned
     */
    public static final Class tryToLoadOneClass(String[] classNames) {
        boolean found = false;
        Class result = null;
        for (int i = 0; ( (!found) && (i < classNames.length)); i++) {
            final String name = classNames[i];
            try {
                result = Class.forName(name);
            } catch (ClassNotFoundException e) {
                _log.warn("ReflectionOperations: tryToLoadOneClass() not found class '"+name+"'");
            }//try-catch
            found = (result!=null);
        }//for
        return result;
    }//tryToLoadOneClass

    public static final Class loadClassQuietly(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            _log.warn("ReflectionOperations: tryToLoadOneClass() not found class '"+className+"'");
            return null;
        } catch (Exception e) {
            _log.warn("ReflectionOperations: tryToLoadOneClass() Unknown error loading class '"+className+"'",e);
            return null;
        }//try-catch
    }//loadClassQuietly

    public static final Object newInstanceQuietly(Class cls) {
        try {
            return cls.newInstance();
        } catch (Exception e) {
            _log.error("ReflectionOperations: tryToLoadOneClass() cannot instanciate class '"+cls.getName()+"'");
            return null;
        }//try-catch
    }//newInstanceQuietly

}//class
/*______________________________EOF_________________________________*/
