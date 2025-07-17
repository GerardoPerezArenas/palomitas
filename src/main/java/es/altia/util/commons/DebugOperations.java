/*______________________________BOF_________________________________*/
package es.altia.util.commons;

import es.altia.util.jdbc.JdbcOperations;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

/**
 * @version $\Date$ $\Revision$
 */
public final class DebugOperations {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/

    /*_______Operations_____________________________________________*/
    private DebugOperations(){}

    public static final String getStackTraceAsString(Throwable ex) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.getBuffer().toString();
    }//getStackTraceAsString

    public static final void appendDebugStringFor(Collection c, StringBuffer result) {
        if (c!=null) {
            result.append("{");
            for (Iterator iterator = c.iterator(); iterator.hasNext();) {
                final Object o = iterator.next();
                result.append(o);
                if (iterator.hasNext()) result.append(", ");
            }//for
            result.append("}");
        } else {
            result.append("{null}");
        }//if
    }//appendDebugStringFor

    public static final void appendDebugStringFor(int[] c, StringBuffer result) {
        if (c!=null) {
            result.append("{");
            for (int i = 0; i < c.length; i++) {
                result.append(c[i]);
                if (i<c.length-1) result.append(", ");
            }//for
            result.append("}");
        } else {
            result.append("{null}");
        }//if
    }//appendDebugStringFor

    public static final void appendDebugStringFor(Object[] c, StringBuffer result) {
        if (c!=null) {
            result.append("{");
            for (int i = 0; i < c.length; i++) {
                result.append(c[i]);
                if (i<c.length-1) result.append(", ");
            }//for
            result.append("}");
        } else {
            result.append("{null}");
        }//if
    }//appendDebugStringFor

    public static final void appendDebugStringFor(byte[] c, StringBuffer result) {
        if (c!=null) {
            result.append("{byte[].length=");
            result.append(c.length);
            result.append("}");
        } else {
            result.append("{null}");
        }//if
    }//appendDebugStringFor

    public static final String getDetailedMessage(Throwable e) {
        final StringBuffer result = new StringBuffer("[Throwable:");
        result.append("\n| Message=");
        result.append(e.getMessage());
        result.append("\n| ExceptionClass=");
        result.append(e.getClass().getName());
        result.append("\n| StackTrace=");
        result.append(getStackTraceAsString(e));
//        Throwable t = e.getCause();
//        while ( (t!=null) && (t.getCause()!=null) ) {
//            t = t.getCause();
//        }//while
//        if (t!=null) {
//            result.append("\n| RootCause=");
//            result.append(getDetailedMessage(t));
//        }//if
        result.append("\n|]");
        return result.toString();
    }//getDetailedMessage

    public static final String getDetailedMessage(SQLException e) {
        return JdbcOperations.toString(e);
    }//getDetailedMessage

    public static final String getShortNameForClass(Class c) {
        final String fqn = c.getName();
        String result = fqn;
        if ( (fqn.lastIndexOf('.') > 0) ) {
            result = fqn.substring(fqn.lastIndexOf('.')+1);
        }//if
        return result;
    }//getShortNameForClass

    public static final String getPackageNameForClass(Class c) {
        return c.getPackage().getName();
    }//getPackageNameForClass

}//class
/*______________________________EOF_________________________________*/
