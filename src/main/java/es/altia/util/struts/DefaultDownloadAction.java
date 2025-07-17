/*______________________________BOF_________________________________*/
package es.altia.util.struts;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.OutputStream;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.commons.BasicValidations;
//import es.altia.util.files.MimeTypes;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public abstract class DefaultDownloadAction extends DefaultAction {
    /*_______Constants______________________________________________*/
    private static final String CLSNAME = "DefaultDownloadAction";

    private static final String CONTENT_DISPOSITION_INLINE = "inline";
    private static final String CONTENT_DISPOSITION_ATTACHMENT = "attachment";

    /*_______Atributes______________________________________________*/
    protected String pFileMimeType = "application/x-pdf";
    protected String pFileName = "file.pdf";
    protected String pFileDescription = null;
    protected int pFileLength = -1;
    protected byte[] pFileContent = new byte[0];
    protected boolean pInline = true;

    /*_______Operations_____________________________________________*/
    protected ActionForward doPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        ActionForward result = null;
        /*Do the job*/
        download(form, request);

        /*Get file properties*/
        String fileName = getFileName();
        String fileMimeType = getFileMimeType();
        int fileLength = getFileLength();
        byte[] fileContentArray = getFileContent();
        String fileDisposition = isInline()?CONTENT_DISPOSITION_INLINE:CONTENT_DISPOSITION_ATTACHMENT;
        String fileDescription = getFileDescription();
        if (_log.isInfoEnabled()) _log.info(CLSNAME+".download() About to write file with name='"+fileName+"', mime='"+fileMimeType+"', length='"+fileLength+"', byte[].length="+((fileContentArray!=null)?(fileContentArray.length):(-1)));

        /*Change Headers*/
        response.setContentType(fileMimeType);
        response.setHeader("Cache-Control", "");
        response.setHeader("Pragma", "");
        response.setHeader("Expires", "");
        response.setHeader("Content-Disposition", fileDisposition + "; filename=\"" + fileName + "\";");
        if (!BasicValidations.isEmpty(fileDescription)) response.setHeader("Content-Description",fileDescription);
        if (fileLength>0) response.setContentLength(fileLength);

        /*Write file contents*/
        OutputStream os = response.getOutputStream();
        if (fileContentArray!=null) {
            os.write(fileContentArray);
        } else {
            writeFileContentStream(form,request,response);
        }//if
        os.close();
        return result;
    }//doPerform

    public String getFileMimeType() {
        return pFileMimeType;
    }
    public void setFileMimeType(String fileMimeType) {
        pFileMimeType = fileMimeType;
    }

    public String getFileName() {
        return pFileName;
    }
    public void setFileName(String fileName) {
        pFileName = fileName;
    }

    public boolean isInline() {
        return pInline;
    }
    public void setInline(boolean newValue) {
        pInline = newValue;
    }

    public int getFileLength() {
        return pFileLength;
    }
    public void setFileLength(int fileLength) {
        pFileLength = fileLength;
    }

    public byte[] getFileContent() {
        return pFileContent;
    }
    public void setFileContent(byte[] fileContent) {
        pFileContent = fileContent;
    }

    public String getFileDescription() {
        return pFileDescription;
    }
    public void setFileDescription(String fileDescription) {
        pFileDescription = fileDescription;
    }

    protected void writeFileContentStream(ActionForm form, HttpServletRequest request, HttpServletResponse response) throws InternalErrorException {
    }//getFileContentStream

    protected abstract void download(ActionForm form, HttpServletRequest request) throws IOException, ServletException, InternalErrorException, ModelException;
}//class
/*______________________________EOF_________________________________*/
