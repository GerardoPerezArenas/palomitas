/*______________________________BOF_________________________________*/
package es.altia.util.collections;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;


/**
 * @version $\Date$ $\Revision$
 */
public class CollectionsOperations {
    /*_______Constants______________________________________________*/
    private static final List EMPTY_INMUTABLE_LIST = Collections.unmodifiableList(new ArrayList(0));

    /*_______Atributes______________________________________________*/

    /*_______Operations_____________________________________________*/
    private CollectionsOperations() {}

    public static final Map toMap(List l, KeyRetrievalMethod rm) {
        Map result = null;
        if (l!=null) {
            result = CollectionsFactory.getInstance().newHashMap(l.size());
            for (Iterator iterator = l.iterator(); iterator.hasNext();) {
                final Object o = iterator.next();
                result.put(rm.getKey(o),o);
            }//for
        }//if
        return result;
    }//toMap

    public static final List getEmptyInmutableList() {
        return EMPTY_INMUTABLE_LIST;
    }//getEmptyInmutableList

    public static final Set toSet(List l) {
        Set result = null;
		if (l == null) return null;
        result = CollectionsFactory.getInstance().newSet();
        Iterator ite = l.iterator();
        while (ite.hasNext()) result.add(ite.next());
        return result;
    }//toSet

    public static final List toList(Set s) {
        List result = null;
        if (s == null) return null;
        result = CollectionsFactory.getInstance().newArrayList(s.size());
        Iterator ite = s.iterator();
        while (ite.hasNext()) result.add(ite.next());
        return result;
    }//toList
}//class
/*______________________________EOF_________________________________*/
