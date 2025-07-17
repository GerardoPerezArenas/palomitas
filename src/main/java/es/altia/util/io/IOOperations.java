/*______________________________BOF_________________________________*/
package es.altia.util.io;

import es.altia.util.collections.CollectionsFactory;

import java.io.*;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * @version $\Revision$ $\Date$
 */
public final class IOOperations {
	public static final int BUFFER_SIZE = 4096;

    private IOOperations() {
    }

    public static final byte[] toByteArray(InputStream is)
            throws IOException {
        int c;
        final PushbackInputStream input = new PushbackInputStream(is, 128); //128=buffer size
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((c = input.read()) >= 0)
            baos.write(c);
        return baos.toByteArray();
    }//toByteArray

    public static final void closeReaderSilently(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (Exception e) {
                /*Ignore Exception*/
            }//try-catch
        }//if
    }//closeReaderSilently

    public static final void closeInputStreamSilently(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                /*Ignore Exception*/
            }//try-catch
        }//if
    }//closeInputStreamSilently

    public static final void closeOutputStreamSilently(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                /*Ignore Exception*/
            }//try-catch
        }//if
    }//closeOutputStreamSilently

    public static final List getLines(String str) {
        final List result = CollectionsFactory.getInstance().newArrayList();
        final LineNumberReader reader = new LineNumberReader(new StringReader(str));
        try {
            String line;
            do {
                line = reader.readLine();
                if (line != null) result.add(line);
            } while (line != null);
        } catch (Exception e) {
            /* ignore it */
        } finally {
            closeReaderSilently(reader);
            return result;
        }//try-catch
    }//getLines

    public static final String convertEncoding(String input, String currentEncoding, String newEncoding)
            throws UnsupportedEncodingException {
        byte[] bytes;
        String conResult = new String();
        bytes = input.getBytes(currentEncoding);
        conResult = new String(bytes, newEncoding);
        return conResult;
    }//convertEncoding

    public static final String getSystemDefaultEncoding() {
        return new InputStreamReader(new ByteArrayInputStream(new byte[0])).getEncoding();
    }//getSystemDefaultEncoding

    public static final String gunzipText(InputStream is)
            throws IOException {
        GZIPInputStream gzipInputStream = new GZIPInputStream(is);
        BufferedReader isr = new BufferedReader(new InputStreamReader(gzipInputStream));
        StringWriter sw = new StringWriter();
        char[] buf = new char[1024];
        int len;
        do {
            len = isr.read(buf);
            if (len>0) sw.write(buf,0,len);
        } while(len>0);//while
        isr.close();
        gzipInputStream.close();
        String result = sw.toString();
        sw.close();
        return result;
    }//gunzipText

    public static final void gzipText(String str, OutputStream os)
            throws IOException {
        GZIPOutputStream gzipOS = new GZIPOutputStream(os);
        PrintWriter pw = new PrintWriter(gzipOS);
        pw.print(str);
        pw.flush();
        pw.close();
        gzipOS.close();
    }//gzipText

    public static final String readToString(InputStream inputStream)
        throws IOException {
        StringBuffer convertedString = new StringBuffer();
        String theNewString=null;

        BufferedReader dataIn = new BufferedReader(new InputStreamReader(inputStream));
        boolean bContinue = true;
            while (bContinue){
                theNewString=dataIn.readLine();
                if (theNewString!=null) {
                    convertedString.append(theNewString).append("\n");
                } else {
                    bContinue = false;
                }//if
            }//while
        return convertedString.toString();
    }//readToString

	public static final void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		int nrOfBytes = -1;
		while ((nrOfBytes = in.read(buffer)) != -1) {
			out.write(buffer, 0, nrOfBytes);
		}
		out.flush();
	}

	public static final void copy(Reader in, Writer out) throws IOException {
		char[] buffer = new char[BUFFER_SIZE];
		int nrOfBytes = -1;
		while ((nrOfBytes = in.read(buffer)) != -1) {
			out.write(buffer, 0, nrOfBytes);
		}
		out.flush();
	}

	public static final String readToString(Reader in) throws IOException {
		StringWriter out = new StringWriter();
		copy(in, out);
		final String result = out.toString();
		out.close();		
		return result;
	}

	public static final String readURL(String theURL)
		throws MalformedURLException, IOException {
	    URL url = new URL(theURL);
	    URLConnection urlConnection = url.openConnection();
	    InputStream input = urlConnection.getInputStream();
		String result = readToString(input);
	    input.close();
		return result;
	}//callURL


    
}//class
/*______________________________EOF_________________________________*/