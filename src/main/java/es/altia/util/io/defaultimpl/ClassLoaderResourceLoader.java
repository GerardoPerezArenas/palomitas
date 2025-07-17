/*______________________________BOF_________________________________*/
package es.altia.util.io.defaultimpl;

import es.altia.util.io.ResourceLoader;

import java.io.InputStream;

/**
 * @version $\Date$ $\Revision$
 */
public class ClassLoaderResourceLoader implements ResourceLoader {
    /*_______Atributes______________________________________________*/
    private static ClassLoader ldr;

    /*_______Operations_____________________________________________*/
    public ClassLoaderResourceLoader() {
        ldr = ClassLoaderResourceLoader.class.getClassLoader();
    }//constructor

    public ClassLoaderResourceLoader(ClassLoader loader) {
        ldr = loader;
    }//constructor

    public InputStream getResourceAsStream(String name) {
        return ldr.getResourceAsStream(name);
    }//getResourceAsStream
}//class
/*______________________________EOF_________________________________*/
