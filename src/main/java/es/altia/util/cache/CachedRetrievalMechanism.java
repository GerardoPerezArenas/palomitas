/*______________________________BOF_________________________________*/
package es.altia.util.cache;



/**
 * @version $\Date$ $\Revision$
 */
public interface CachedRetrievalMechanism extends RetrievalMechanism {
    /*_______Operations_____________________________________________*/
    public void init(RetrievalMechanism decoratedMechanism, int cacheElementsNumber);
    public boolean isCached(KeyObject key);
    public void flush();
    public void reset();
    public void destroy();

}//class
/*______________________________EOF_________________________________*/
