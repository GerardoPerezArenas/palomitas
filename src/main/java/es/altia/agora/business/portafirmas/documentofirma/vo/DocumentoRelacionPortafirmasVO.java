package es.altia.agora.business.portafirmas.documentofirma.vo;

import es.altia.util.commons.DateOperations;

import java.util.Calendar;
import java.util.Date;

public class DocumentoRelacionPortafirmasVO extends DocumentoRelacionFirmaVO {
    /*_______Constants______________________________________________*/

	/*_______Attributes_____________________________________________*/
    protected String pNombreProcedimiento = null;
    protected String pNombreTramite = null;
    protected String pNombreDocumento = null;
    protected Calendar pFechaEnvioFirma = null;
    protected String pFinalizaRechazo = null;

    /*_______Operations_____________________________________________*/
    public DocumentoRelacionPortafirmasVO(DocumentoRelacionFirmaPK pk,  String theEstadoFirma, byte[] theFirma,
                                          Calendar theFechaFirma, String theObservaciones, String theNombreProcedimiento,
                                          String theNombreTramite, String theNombreDocumento, Calendar theFechaEnvioFirma) {
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

    public String getFinalizaRechazo() {
        return pFinalizaRechazo;
    }

    public void setFinalizaRechazo(String pFinalizaRechazo) {
        this.pFinalizaRechazo = pFinalizaRechazo;
    }
    

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append(super.toString()).append(" ::: ");
        buf.append("DocumentoPortafirmasVO");
        buf.append("{NombreProcedimiento=").append(pNombreProcedimiento);
        buf.append(",NombreTramite=").append(pNombreTramite);
        buf.append(",NombreDocumento=").append(pNombreDocumento);
        buf.append(",FechaEnvioFirma=").append(DateOperations.toString(pFechaEnvioFirma,"yyyy-MM-dd"));
        buf.append(",FinalizaRechazo").append(pFinalizaRechazo);
        buf.append('}');
        return buf.toString();
    }
}
