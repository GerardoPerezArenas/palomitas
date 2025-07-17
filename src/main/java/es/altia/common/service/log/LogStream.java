/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.common.service.log;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author milagros.noya
 */
public class LogStream extends FilterOutputStream{
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(LogStream.class);
    private static final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    
    public LogStream(OutputStream out) {
        // initialize parent with my bytearray (which was never used)
        super(bos);
    }

    @Override
    public void flush() throws IOException {
        // this was never called in my test
        bos.flush();
        if (bos.size() > 0) LOG.debug(bos.toString());
        bos.reset();
    }

    @Override
    public void write(byte[] b) throws IOException {
        LOG.debug(new String(b));
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        LOG.debug(new String(b, off, len));
    }

    @Override
    public void write(int b) throws IOException {
        write(new byte[] { (byte) b });
    }
}
