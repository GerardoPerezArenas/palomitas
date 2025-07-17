/*______________________________BOF_________________________________*/
package es.altia.util.jdbc.unwrapper;

import es.altia.util.jdbc.logdecorator.LogUnwrapper;

/**
 * @version $\Date$ $\Revision$
 */
public class JdbcUnwrapperFactory {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
    private static JdbcUnwrapperFactory SINGLETON = null;

    private CompositeJdbcUnwrapper defaultUnwrapper = null;

    /*_______Operations_____________________________________________*/
    private JdbcUnwrapperFactory() {}

    public static JdbcUnwrapperFactory getInstance() {
        if (SINGLETON == null) initSingleton();
        return SINGLETON;
    }//getInstance

    private synchronized static void initSingleton() {
        if (SINGLETON == null) SINGLETON = new JdbcUnwrapperFactory();
    }//initSingleton

    public JdbcUnwrapper newUnwrapper() {
        if (defaultUnwrapper==null) {
            synchronized(this) {
                if (defaultUnwrapper==null) {
                    defaultUnwrapper = new CompositeJdbcUnwrapper();
                    defaultUnwrapper.addUnwrapper(new LogUnwrapper());
                }//if
            }//synchronized
        }//if
        return defaultUnwrapper;
    }//newUnwrapper

}//class
/*______________________________EOF_________________________________*/