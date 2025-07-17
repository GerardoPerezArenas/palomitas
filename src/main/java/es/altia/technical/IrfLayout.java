
package es.altia.technical;

import org.apache.log4j.Layout;
import org.apache.log4j.helpers.ISO8601DateFormat;
import org.apache.log4j.spi.LoggingEvent;


public class IrfLayout extends Layout {

    public IrfLayout() {}

    public void activateOptions() {}

    /**
     * Return the a log statement in a format consisting of the
     * <code>priority</code>, folloed by " - " and then the
     * <code>message</code>. For example, <pre> INFO - "A message"
     * </pre>
     * <p/>
     * <p>The <code>category</code> parameter is ignored.
     * <p/>
     *
     * @return A byte array in SimpleLayout format.
     */
    public String format(LoggingEvent event) {
        StringBuffer sbuf = new StringBuffer(256);
        sbuf.setLength(0);
        ISO8601DateFormat date = new ISO8601DateFormat();
        sbuf.append(date.format(new java.util.Date()));
        sbuf.append(" - ");
        sbuf.append(event.getThreadName());
        sbuf.append(" - ");
        sbuf.append(event.getLevel());
        sbuf.append(" - ");
        sbuf.append(event.getLoggerName());
        sbuf.append(" - ");
        sbuf.append(event.getMessage());
        sbuf.append(LINE_SEP);
        return sbuf.toString();
    }

    public String[] getOptionStrings() {
        return new String[0];
    }

    /**
     * The SimpleLayout does not handle the throwable contained within
     * {@link LoggingEvent LoggingEvents}. Thus, it returns
     * <code>true</code>.
     *
     * @since version 0.8.4
     */
    public boolean ignoresThrowable() {
        return true;
    }

    public void setOption(String option, String value) {
    }

}
