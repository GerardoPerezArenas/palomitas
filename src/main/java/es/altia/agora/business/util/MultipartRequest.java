package es.altia.agora.business.util;

import javax.servlet.http.*;
import java.util.*;
import javax.mail.internet.*;
import javax.mail.*;
import java.io.*;

/**
 * This class accepts a request from an HTML form with ENCTYPE="multipart/form-data".
 * It uses the Java Mail and Java Activation Framework classes to parse all of this
 * information (basically just one line of code!).
 * <p>
 * The remainder of the class extracts the information from the parsed form and 
 * provides methods to present it to the user.  These methods follow those
 * of the Servlet 2.2 spec or API quite closely.
 * <p>
 * In this version I have made the following decisions:
 * <ul>
 * <li> It does not handle nested multipart requests, because I am not
 *      aware of a way to make a browser produce them. (Actually the
 *      parsing probably works fine, but I don't check for the possibility
 *      while extracting the parsed results.) It would not be hard to change
 *      this.
 * <li> Any uploaded files are saved in memory as byte arrays, rather than 
 *      writing them to disk. I leave it to the user of this routine to
 *      write the file to disk if necessary.  This decision avoids the
 *      problem of a filename on the remote system being invalid on the
 *      server's system.  Again, it is simple to change if necessary.
 * <li> If the user leaves an entry in the form blank, then (as per 
 *      servlet 2.2) that parameter will be "", not null. If the blank
 *      entry corresponds to a file, then the corresponding FileInfo
 *      will be created, but the filename will be "" and the content
 *      will be a zero-length array.
 * <li> If the user supplies a filename that doesn't exist on their system,
 *      the FileInfo will be created with that filename, but the content
 *      will be a zero-length array.
 * </ul>
 *
 * @author Nick Newman, SCIENTECH Inc (www.scientech.com)
 */

public class MultipartRequest
{
    private MimeMultipart mimeparts;  // To hold the parsed results
    private HashMap params = new HashMap();  // To hold the uploaded parameters
    private HashMap files = new HashMap();  // To hold the uploaded files
    private byte [] buf = new byte[8096]; // Scratch buffer space
    
    /**
     * This private inner class is a simple implementation of the
     * DataSource interface. It provides the bridge between the
     * HttpServletRequest and the Java Mail classes.
     */
    private class Source implements javax.activation.DataSource{
        private InputStream stream;
        private String mimetype;
        
        Source(HttpServletRequest req) throws IOException {
            stream = req.getInputStream();
            mimetype = req.getHeader("CONTENT-TYPE");
        }
        
        public InputStream getInputStream(){ return stream; }
        public String getContentType(){ return mimetype; }
        public OutputStream getOutputStream(){throw new RuntimeException();} // Not used
        public String getName(){throw new RuntimeException();} // Not used
    };
    
    /**
     * This public inner class is used to store information about uploaded
     * files.  Users of the MultipartRequest class should generally
     * refer to this class as MultipartRequest.FileInfo (as per usual
     * Java rules).
     */
    public class FileInfo 
    {
        private byte[] content;  // The byte-copy of the file's contents
        private String sourcename; // The name of the file on the browser's system
        private String mimetype; // The mimetype supplied by the browser
        public FileInfo(byte [] content, String sourcename, String mimetype)
        {
            this.content = content;
            this.sourcename = sourcename;
            this.mimetype = mimetype;
        }
        public byte [] getContent(){ return content; }
        public String getSourceFilename(){ return sourcename; }
        public String getMimeType(){ return mimetype; }
    }
    
    /**
     * The constructor. This accepts an HttpServletRequest (which it assumes to be
     * from a post of a ENCTYPE="multipart/form-data" form) and parses all the
     * information into a MimeMultipart object.
     * <p>
     * It then iterates through that parsed object, extracting the parameters and
     * files from it for the user.
     *
     * @param req a request from a form post with ENCTYPE="multipart/form-data".
     * @throws MessagingException if there are problems with parsing the MIME
     *      information.
     * @throws IOException if there are problems reading the input stream.
     */
    public MultipartRequest(HttpServletRequest req) throws MessagingException, IOException 
    {
        // Here's the line which does all of the parsing.
        // The request size and content type could be checked before calling, if desired.
        mimeparts = new MimeMultipart( new Source(req) );
        
        // Now iterate over the parsed results
        int partCount = mimeparts.getCount();
        for(int i=0; i<partCount; ++i){
            MimeBodyPart bp = (MimeBodyPart) mimeparts.getBodyPart(i);
            String disposition = bp.getHeader("Content-Disposition","");
            // I use the filename to indicate if this is a file or a parameter.
            // Could instead use bp.getContent().getClass() to indicate if we
            // have a String, an InputStream, or a (nested) MultiPart.
            String filename = bp.getFileName(); // This filename appears to lack "\" chars.
            if( filename == null ) doParameter(bp, disposition);
            else doFile(bp, disposition);
        }
    }

    /**
     * @return an iterator for the parameter names, as per servlet 2.2 spec
     *      except for using Iterator rather than Enumeration.
     */
    public Iterator getParameterNames()
    {
        return params.keySet().iterator();
    }
    
    /**
     * Return the (only) parameter with the given name, or null
     * if no such parameters exist. If there are more than one
     * parameters with this name, return the first (per servlet 2.2 API)
     *
     * @param name the HTML name of the input field for the parameter
     * @return the value of the parameter with the given name
     */
    public String getParameter(String name)
    {
        List valuelist = (List) params.get(name);
        if( valuelist == null ) return null;
        return (String) valuelist.get( 0 );  // Return first value, as per servlet 2.2 API
    }
    
    /**
     * Return an array of all the parameters with the given name,
     * or null if no parameters with this name exist.
     *
     * @param name the HTML name of the input field for the parameter
     * @return the array of values of parameters with this name.
     */
    public String [] getParameterValues(String name)
    {
        List valuelist = (List) params.get(name);
        if( valuelist == null ) return null;
        return (String[]) valuelist.toArray( new String[valuelist.size()] );
    }
    
    /**
     * @return an Iterator for the FileInfo items describing the 
     * files encapsulated in the request.
     */
    public Iterator getFileInfoNames()
    {
        return files.keySet().iterator();
    }
    
    /**
     * Return the (only) FileInfo object describing the uploaded
     * files with a given HTML name, or null if no such name exists
     * in the request.  If there are several files uploaded under the
     * name, return the first.
     *
     * @param name the HTML name of the input field for the file
     * @return the FileInfo object for the file.
     */
    public FileInfo getFileInfo(String name)
    {
        List filelist = (List) files.get(name);
        if( filelist == null ) return null;
        return (FileInfo) filelist.get( 0 );
    }
    
    /**
     * Return an array of all the FileInfo objects representing the
     * files uploaded under the given HTML name, or null if no such
     * name exists.
     *
     * @param name the HTML name of the input field for the files
     * @return the array of FileInfo objects for files uploaded
     *      under this name.
     */
    public FileInfo [] getFileInfoValues(String name)
    {
        List filelist = (List) files.get(name);
        if( filelist == null ) return null;
        return (FileInfo[]) filelist.toArray( new FileInfo[filelist.size()] );
    }
    
    /**
     * Do whatever processing is needed for a parameter.
     */
    private void doParameter(MimeBodyPart bp, String disposition) throws MessagingException, IOException 
    {
        String name = findValue("name", disposition);
        String value = (String) bp.getContent();
        List valuelist = (List) params.get(name);
        if( valuelist==null ){
            valuelist = new LinkedList();
            params.put(name, valuelist);
        }
        valuelist.add(value);
    }
    
    /**
     * Do whatever processing is needed for a file.
     */
    private void doFile(MimeBodyPart bp, String disposition) throws MessagingException, IOException 
    {
        String name = findValue("name", disposition);
        String filename = findValue("filename", disposition);
        BufferedInputStream in = new  BufferedInputStream(bp.getInputStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream( in.available() );
        int k;
        while( (k=in.read(buf)) != -1 ) out.write(buf,0,k);
        out.close();
        FileInfo f = new FileInfo(out.toByteArray(), filename, bp.getContentType());
        List filelist = (List) files.get(name);
        if( filelist==null ){
            filelist = new LinkedList();
            files.put(name, filelist);
        }
        filelist.add(f);
    }
    
    /**
     * Utiltity to extract a parameter value from a header line, since the
     * Java library routines don't seem to let us do that.
     */
    private String findValue(String parm, String header)
    {
        StringTokenizer st = new StringTokenizer(header, "; =");
        while( st.hasMoreTokens() ){
            String token = st.nextToken();
            if( token.equalsIgnoreCase(parm) ){
                try { return st.nextToken("\"="); }
                catch( NoSuchElementException e ){ return ""; } // e.g. filename=""
            }
        }
        return null;
    }
    
}