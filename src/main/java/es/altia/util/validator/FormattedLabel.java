/*______________________________BOF_________________________________*/
package es.altia.util.validator;

import es.altia.util.commons.DebugOperations;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public class FormattedLabel {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
    protected String pLabel = null;
    protected Object[] pParameters = null;

    /*_______Operations_____________________________________________*/
    public FormattedLabel(final String label, final Object[] parameters) {
        pLabel = label;
        pParameters = parameters;
    }

    public FormattedLabel(final String label) {
        pLabel = label;
    }

    public String getLabel() {
        return pLabel;
    }

    public void setLabel(final String label) {
        pLabel = label;
    }

    public Object[] getParameters() {
        return pParameters;
    }

    public void setParameters(final Object[] parameters) {
        pParameters = parameters;
    }

    public String toString() {
        final StringBuffer buff = new StringBuffer("[FormattedLabel:");
        buff.append("| Label=");
        buff.append(pLabel);
        buff.append("| Parameters=");
        DebugOperations.appendDebugStringFor(pParameters,buff);
        buff.append("|]");
        return buff.toString();
    }//toString

}//class
/*______________________________EOF_________________________________*/
