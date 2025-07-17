/*______________________________BOF_________________________________*/
package es.altia.util.cache;

import es.altia.util.exceptions.InternalErrorException;

/**
 * @version $\Date$ $\Revision$
 */
public interface RetrievalMechanism {
    /*_______Operations_____________________________________________*/
    public Object retrieve(KeyObject key) throws InternalErrorException;

}//class
/*______________________________EOF_________________________________*/
