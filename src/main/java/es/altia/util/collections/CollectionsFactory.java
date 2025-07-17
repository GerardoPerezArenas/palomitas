/*______________________________BOF_________________________________*/
package es.altia.util.collections;

import es.altia.util.commons.DebugOperations;
import es.altia.util.commons.ReflectionOperations;
import es.altia.util.configuration.ConfigurationParametersManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Set;


/**
 * @version $\Date$ $\Revision$
 */
public class CollectionsFactory {
    /*_______Constants______________________________________________*/
    private static final String FACTORY_IMPL_PARAMETER = "CollectionsFactory/impl";
    protected static final int MAP_INITIAL_CAPACITY_THRESHOLD = 10;

    /*_______Atributes______________________________________________*/
    private static CollectionsFactory SINGLETON = null;
    private static final Log _log =
            LogFactory.getLog(CollectionsFactory.class.getName());

    /*_______Operations_____________________________________________*/
    public List newArrayList() {
        return new ArrayList();
    }//newArrayList

    public List newArrayList(int initialCapacity) {
        return new ArrayList(initialCapacity);
    }//newArrayList

    public List newLinkedList() {
        return new LinkedList();
    }//newLinkedList

    public Map newHashMap() {
        return new HashMap();
    }//newHashMap

    public Map newHashMap(int initialCapacity) {
        return new HashMap(initialCapacity);
    }//newHashMap

    public Map newHashMapForSizeLowerThan(int maxSize) {
        if (maxSize > MAP_INITIAL_CAPACITY_THRESHOLD)
            return newHashMap();
        else
            return new HashMap(maxSize);
    }//newHashMap

    public Set newSet() {
        return newHashSet();
    }//newHashSet

    public Set newHashSet() {
        return new HashSet();
    }//newHashSet



    public CollectionsFactory() {
    }
    
    public static CollectionsFactory getInstance() {
        if (SINGLETON == null) initSingleton();
        return SINGLETON;
    }//getInstance
    private synchronized static void initSingleton() {
        if (_log.isInfoEnabled()) _log.info("CollectionsFactory: initSingleton() BEGIN");
        if (SINGLETON == null) {
            try {
                final String implClassName = ConfigurationParametersManager.getParameter(FACTORY_IMPL_PARAMETER);
                SINGLETON = (CollectionsFactory) ReflectionOperations.loadClassAndGetInstance(implClassName);
            } catch (Exception e) {
                if (_log.isWarnEnabled()) _log.warn("CollectionsFactory: initSingleton() Exception .... " + DebugOperations.getDetailedMessage(e));
            }//try-catch
            if (SINGLETON == null) SINGLETON = new CollectionsFactory();
        }//if
        if (_log.isInfoEnabled()) _log.info("CollectionsFactory: initSingleton() END");
    }//initSingleton
}//class
/*______________________________EOF_________________________________*/