package es.altia.util.io.nullimpl;

import java.io.OutputStream;

/**
 * This class represents an null-outputstream
 *
 * @author Patric Kabus
 */
public class NullOutputStream extends OutputStream {
    /**
     * Class constructor
     */
    public NullOutputStream() {
        super();
    }

    /**
     * This method sends a byte to NULL
     *
     * @param b byte to be sent
     */
    public void write(int b) {
    }

    /**
     * This method sends an array of bytes to NULL
     *
     * @param b byte array to be sent
     */
    public void write(byte[] b) {
    }

    /**
     * This method sends an array of bytes starting
     * at offset with length len to NULL
     *
     * @param b   byte array to be sent
     * @param off start stream at specified offset
     * @param len length of stream
     */
    public void write(byte[] b, int off, int len) {
    }
}//class



