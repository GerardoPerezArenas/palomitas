/*______________________________BOF_________________________________*/
package es.altia.util.io;

import java.io.InputStream;

/**
 * @version $\Date$ $\Revision$
 **/
public interface ResourceLoader  {
	/*_______Operations_____________________________________________*/
	public InputStream getResourceAsStream(String name);	
}//interface
/*______________________________EOF_________________________________*/
