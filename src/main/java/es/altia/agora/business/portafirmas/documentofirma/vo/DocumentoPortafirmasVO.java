/*______________________________BOF_________________________________*/
package es.altia.agora.business.portafirmas.documentofirma.vo;

import es.altia.util.commons.DateOperations;

import java.util.Calendar;
import java.util.Date;


/**
  * @version      $\Revision$ $\Date$
  *
  * This is the value object for the DocumentoFirma 
  * in our system.
  *
  **/
public class DocumentoPortafirmasVO extends DocumentoFirmaVO {
    /*_______Constants______________________________________________*/

	/*_______Attributes_____________________________________________*/
    protected String pNombreProcedimiento = null;
    protected String pNombreTramite = null;
    protected String pNombreDocumento = null;
    protected Calendar pFechaEnvioFirma = null;

    /*_______Operations_____________________________________________*/
    public DocumentoPortafirmasVO( DocumentoFirmaPK pk,  String theEstadoFirma,
                             byte[] theFirma,  Calendar theFechaFirma, String theObservaciones,
                             String theNombreProcedimiento,
                             String theNombreTramite,
                             String theNombreDocumento,
                             Calendar theFechaEnvioFirma) {
        pPrimaryKey=pk;
        pEstadoFirma = theEstadoFirma;
        pFirma = theFirma;
        pFechaFirma = theFechaFirma;
        pObservaciones = theObservaciones;

        pNombreProcedimiento = theNombreProcedimiento;
        pNombreTramite = theNombreTramite;
        pNombreDocumento = theNombreDocumento;
        pFechaEnvioFirma = theFechaEnvioFirma;
    }//constructor

    public String getNombreProcedimiento() {
        return pNombreProcedimiento;
    }
    public void setNombreProcedimiento(String nombreProcedimiento) {
        pNombreProcedimiento = nombreProcedimiento;
    }

    public String getNombreTramite() {
        return pNombreTramite;
    }
    public void setNombreTramite(String nombreTramite) {
        pNombreTramite = nombreTramite;
    }

    public String getNombreDocumento() {
        return pNombreDocumento;
    }
    public void setNombreDocumento(String nombreDocumento) {
        pNombreDocumento = nombreDocumento;
    }

    public Calendar getFechaEnvioFirma() {
        return pFechaEnvioFirma;
    }
    public Date getFechaEnvioFirmaAsDate() {
        return DateOperations.toDate(pFechaEnvioFirma);
    }
    public void setFechaEnvioFirma(Calendar fechaEnvioFirma) {
        pFechaEnvioFirma = fechaEnvioFirma;
    }

  
    

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append(super.toString()).append(" ::: ");
        buf.append("DocumentoPortafirmasVO");
        buf.append("{NombreProcedimiento=").append(pNombreProcedimiento);
        buf.append(",NombreTramite=").append(pNombreTramite);
        buf.append(",NombreDocumento=").append(pNombreDocumento);
        buf.append(",FechaEnvioFirma=").append(DateOperations.toString(pFechaEnvioFirma,"yyyy-MM-dd"));
        buf.append('}');
        return buf.toString();
    }
}//class
/*______________________________EOF_________________________________*/

