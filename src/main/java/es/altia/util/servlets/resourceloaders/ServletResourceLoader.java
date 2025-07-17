/*______________________________BOF_________________________________*/
package es.altia.util.servlets.resourceloaders;

import es.altia.util.io.ResourceLoader;

import javax.servlet.ServletContext;
import java.io.InputStream;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class ServletResourceLoader implements ResourceLoader  {
	/*_______Attributes_____________________________________________*/
	ServletContext ctx=null;

	/*_______Operations_____________________________________________*/
	public ServletResourceLoader(ServletContext ctx){
		this.ctx=ctx;
	}//constructor

	public InputStream getResourceAsStream(String name){
		return ctx.getResourceAsStream(name);
	}//getResourceAsStream
}//class
/*______________________________EOF_________________________________*/
