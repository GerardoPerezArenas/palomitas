/*______________________________BOF_________________________________*/
package es.altia.util.persistance.daocommands.stdsql;

/**
  * @author
  * @version      $\Date$ $\Revision$
  **/
public final class SQLDAOCommandsHelper {
	private SQLDAOCommandsHelper() {}

	public static final String composeCommasList(String[] items) {
		final int l = items.length;
        final StringBuffer buff = new StringBuffer( l * (items[0].length()+2) );
        for (int i=0; i < (l-1) ; i++) buff.append(items[i]).append(", ");
        if (l > 0) buff.append(items[l-1]);
		return buff.toString();
	}//composeCommasList

	public static final String composeWhereCriteriaList(String[] items) {
        final int l = items.length;
        final StringBuffer buff = new StringBuffer( l * (items[0].length()+10) );
        for (int i=0; i < (l-1) ; i++) buff.append("(").append(items[i]).append("=?) AND ");
        if (l > 0) buff.append("(").append(items[l-1]).append("=?)");
		return buff.toString();
	}//composeCommasList

	public static final String composePlaceholderCommasList(int l) {
		if (l > 0) {
            final StringBuffer buff = new StringBuffer(2*l);
			for (int i=0; i < (l-1) ; i++) buff.append("?,");
			buff.append("?");
            return buff.toString();
        } else {
            return "";
		}//if
	}//composePlaceholderCommasList

}//class
/*______________________________EOF_________________________________*/
