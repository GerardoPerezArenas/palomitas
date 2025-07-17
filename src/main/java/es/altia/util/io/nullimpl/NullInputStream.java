package es.altia.util.io.nullimpl;

import java.io.InputStream;

public class NullInputStream extends InputStream {
    /**
     * Class constructor
     */
    public NullInputStream() {
        super();
    }

    /**
     * This method always returns -1 (an empty stream)
     *
     * @return -1 (empty stream)
     */
    public int read() {
        return -1;
    }
}//class
