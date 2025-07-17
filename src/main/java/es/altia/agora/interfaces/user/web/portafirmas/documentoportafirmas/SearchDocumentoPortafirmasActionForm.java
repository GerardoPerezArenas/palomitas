/*______________________________BOF_________________________________*/
package es.altia.agora.interfaces.user.web.portafirmas.documentoportafirmas;

import es.altia.util.struts.DefaultSearchActionForm;

/**
 * @version $\Date$ $\Revision$
 */
public class SearchDocumentoPortafirmasActionForm extends DefaultSearchActionForm {
    /*_______Constants______________________________________________*/
    public static final String CHK_ESTADO="chkEstado";
    public static final String ESTADO_FIRMA="estadoFirma";

    /*_______Atributes______________________________________________*/
    protected boolean pChkEstado;
    protected String pEstadoFirma;
    protected String pCodEstadoFirma;
    protected String pDecEstadoFirma;

    /*_______Operations_____________________________________________*/
    protected void doReset() {
        pChkEstado = false;
        pEstadoFirma = null;
    }

    public boolean getChkEstado() {
        return pChkEstado;
    }
    public void setChkEstado(boolean chkEstado) {
        pChkEstado = chkEstado;
    }

    public String getEstadoFirma() {
        return pEstadoFirma;
    }
    public void setEstadoFirma(String estadoFirma) {
        pEstadoFirma = estadoFirma;
    }

    public String getDescEstadoFirma() {
        return pDecEstadoFirma;
    }
    public void setDescEstadoFirma(String descEstadoFirma) {

        pDecEstadoFirma = descEstadoFirma;
    }

    public String getCodEstadoFirma() {
        return pCodEstadoFirma;
    }
    public void setCodEstadoFirma(String estadoFirma) {
        pCodEstadoFirma = estadoFirma;
    }
}//class
/*______________________________EOF_________________________________*/
