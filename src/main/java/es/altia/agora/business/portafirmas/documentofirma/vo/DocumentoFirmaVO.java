/*______________________________BOF_________________________________*/
package es.altia.agora.business.portafirmas.documentofirma.vo;

import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.PersistanceContext;
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
public class DocumentoFirmaVO implements PersistentObject {
    /*_______Constants______________________________________________*/
    public static final String ESTADO_FIRMA_PENDIENTE = "O";
    public static final String ESTADO_FIRMA_FIRMADO = "F";
    public static final String ESTADO_FIRMA_RECHAZADO = "R";

	/*_______Attributes_____________________________________________*/
	protected PersistanceContext pPersistanceContext = null;
	protected DocumentoFirmaPK pPrimaryKey;
	protected String pEstadoFirma;
    protected byte[] pFirma;
	protected Calendar pFechaFirma;
	protected String pObservaciones;
    protected Integer pIdUsuarioDelegadoFirmante;

    /*_______Operations_____________________________________________*/
    protected DocumentoFirmaVO() {}//constructor

	public DocumentoFirmaVO( DocumentoFirmaPK pk,  String theEstadoFirma,  byte[] theFirma,  Calendar theFechaFirma,  String theObservaciones ) {
        this(pk,theEstadoFirma,theFirma, theFechaFirma,  theObservaciones, null);
	}//constructor

    public DocumentoFirmaVO( int theIdMunicipio,  String theIdProcedimiento,  int theIdEjercicio,  String theIdNumeroExpediente,  int theIdTramite,  int theIdOcurrenciaTramite,  int theIdNumeroDocumento,  int theUsuarioFirmante ) {
        this(new DocumentoFirmaPK(theIdMunicipio,  theIdProcedimiento,
                                    theIdEjercicio,  theIdNumeroExpediente,
                                    theIdTramite,  theIdOcurrenciaTramite,
                                    theIdNumeroDocumento,  theUsuarioFirmante),
            ESTADO_FIRMA_PENDIENTE,null,null,null);
    }//constructor
    
    public DocumentoFirmaVO( int theIdMunicipio,  String theIdProcedimiento,  int theIdEjercicio,  String theIdNumeroExpediente,  int theIdTramite,  int theIdOcurrenciaTramite,  int theIdNumeroDocumento,  int theUsuarioFirmante, String theFinalizaRechazo ) {
        this(new DocumentoFirmaPK(theIdMunicipio,  theIdProcedimiento,
                                    theIdEjercicio,  theIdNumeroExpediente,
                                    theIdTramite,  theIdOcurrenciaTramite,
                                    theIdNumeroDocumento,  theUsuarioFirmante, theFinalizaRechazo),
            ESTADO_FIRMA_PENDIENTE,null,null,null);
    }//constructor

    public DocumentoFirmaVO( DocumentoFirmaPK pk,  String theEstadoFirma,  byte[] theFirma,  Calendar theFechaFirma,  String theObservaciones , Integer idUsuarioFirmante) {
        pPrimaryKey=pk;
        pEstadoFirma = theEstadoFirma;
        pFirma = theFirma;
        pFechaFirma = theFechaFirma;
        pObservaciones = theObservaciones;
        this.pIdUsuarioDelegadoFirmante = idUsuarioFirmante;
    }//constructor

	public PrimaryKey getPrimaryKey() {
		return pPrimaryKey;
	}//getPrimaryKey

	public PersistanceContext getPersistanceContext() {
		return pPersistanceContext;
	}//getPersistanceContext

	public void setPersistanceContext(PersistanceContext ctx) {
		pPersistanceContext = ctx;
	}//setPersistanceContext

                    
	public int getIdMunicipio() {
		return pPrimaryKey.getIdMunicipio();
	}//getIdMunicipio
                            
	public String getIdProcedimiento() {
		return pPrimaryKey.getIdProcedimiento();
	}//getIdProcedimiento
           
        public String getFinalizaRechazo() {
            return pPrimaryKey.getFinalizaRechazo();
        }

       
	public int getIdEjercicio() {
		return pPrimaryKey.getIdEjercicio();
	}//getIdEjercicio
                            
	public String getIdNumeroExpediente() {
		return pPrimaryKey.getIdNumeroExpediente();
	}//getIdNumeroExpediente
                            
	public int getIdTramite() {
		return pPrimaryKey.getIdTramite();
	}//getIdTramite
                            
	public int getIdOcurrenciaTramite() {
		return pPrimaryKey.getIdOcurrenciaTramite();
	}//getIdOcurrenciaTramite
                            
	public int getIdNumeroDocumento() {
		return pPrimaryKey.getIdNumeroDocumento();
	}//getIdNumeroDocumento
                            
	public int getUsuarioFirmante() {
		return pPrimaryKey.getUsuarioFirmante();
	}//getUsuarioFirmante
            
	public String getEstadoFirma() {
		return pEstadoFirma;
	}//getEstadoFirma
	public void setEstadoFirma(String newValue) {
		pEstadoFirma = newValue;
	}//setEstadoFirma

	public Calendar getFechaFirma() {
		return pFechaFirma;
	}//getFechaFirma
    public Date getFechaFirmaAsDate() {
        return DateOperations.toDate(pFechaFirma);
    }//getFechaFirma
	public void setFechaFirma(Calendar newValue) {
		pFechaFirma = newValue;
	}//setFechaFirma


	public String getObservaciones() {
		return pObservaciones;
	}//getObservaciones
	public void setObservaciones(String newValue) {
		pObservaciones = newValue;
	}//setObservaciones

    public byte[] getFirma() {
        return pFirma;
    }
    public void setFirma(byte[] firma) {
        pFirma = firma;
    }

    public Integer getIdUsuarioDelegadoFirmante() {
        return pIdUsuarioDelegadoFirmante;
    }
    public void setIdUsuarioDelegadoFirmante(Integer idUsuarioDelegadoFirmante) {
        pIdUsuarioDelegadoFirmante = idUsuarioDelegadoFirmante;
    }

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("DocumentoFirmaVO");
        buf.append("{pPersistanceContext=").append(pPersistanceContext);
        buf.append(",pPrimaryKey=").append(pPrimaryKey);
        buf.append(",pEstadoFirma=").append(pEstadoFirma);
        buf.append(",pFirma=").append(pFirma == null ? "null" : "("+pFirma.length+" bytes)");
        buf.append(",pFechaFirma=").append(DateOperations.toString(pFechaFirma,"yyyy-MM-dd"));
        buf.append(",pObservaciones=").append(pObservaciones);
        buf.append(",pIdUsuarioDelegadoFirmante=").append(pIdUsuarioDelegadoFirmante);
        buf.append('}');
        return buf.toString();
    }
}//class
/*______________________________EOF_________________________________*/

