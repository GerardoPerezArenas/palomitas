/*______________________________BOF_________________________________*/
package es.altia.agora.business.portafirmas.documentofirmafacade.plainimpl;

import es.altia.util.persistance.facades.defaultimpl.requests.DefaultTxRequest;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.SQLDAO;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;
import es.altia.util.persistance.exceptions.StaleUpdateException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.commons.DebugOperations;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoFirmaVO;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoRelacionFirmaVO;
import es.altia.agora.business.portafirmas.documentofirma.dao.CustomDocumentoFirmaDAO;
import es.altia.agora.business.portafirmas.documentofirma.dao.CustomDocumentoRelacionFirmaDAO;

/**
 * @version $\Date$ $\Revision$
 */
public class FirmaDocumentoTramitacionRequest extends DefaultTxRequest {
    /*_______Constants______________________________________________*/
    private static final String CLSNAME = DebugOperations.getShortNameForClass(FirmaDocumentoTramitacionRequest.class);

	/*_______Attributes_____________________________________________*/
	/*input*/
	private PersistentObject vo=null;
	/*output*/
	/*exceptions*/
    Exception ex = null;

	/*_______Operations_____________________________________________*/
	public FirmaDocumentoTramitacionRequest(PersistentObject vo) {
		this.vo = vo;
	}//constructor

    public FirmaDocumentoTramitacionRequest(String dsKey, PersistentObject vo) {
        txSetDataSourceKey(dsKey);
        this.vo = vo;
    }//constructor

	protected void doExecute()
		throws ModelException, InternalErrorException {
		try {
            if (vo == null) throwInternalError(CLSNAME+".doExecute() Null arguments!");
            if (vo instanceof DocumentoFirmaVO) {
            /* Actualizar documento firma (E_CRD_FIR) */
			final SQLDAO dao = getSQLDAOFactory().getDAOInstance(DocumentoFirmaVO.class);
			vo = dao.create(txGetConnection(), vo);
            /* Actualizar documento en tramitación (E_CRD) */
            final CustomDocumentoFirmaDAO concreteDao = (CustomDocumentoFirmaDAO) dao;
            concreteDao.updateEstadoDocumentoTramitacion(txGetConnection(),vo);
            } else if (vo instanceof DocumentoRelacionFirmaVO) {
                /* Actualizar documento firma (G_CRD_FIR) */
                final SQLDAO dao = getSQLDAOFactory().getDAOInstance(DocumentoRelacionFirmaVO.class);
                vo = dao.create(txGetConnection(), vo);
                /* Actualizar documento en tramitación (E_CRD) */
                final CustomDocumentoRelacionFirmaDAO concreteDao = (CustomDocumentoRelacionFirmaDAO) dao;
                concreteDao.updateEstadoDocumentoTramitacion(txGetConnection(),vo);
            }

		} catch (InstanceNotFoundException e) {
            this.ex = e;
			throw e;
        } catch (StaleUpdateException e) {
            this.ex = e;
            throw e;
		} catch (InternalErrorException e) {
            this.ex = e;
			throw e;
        } catch (Exception e) {
            this.ex = e;
            throw new InternalErrorException(e);
		}//try-catch
	}//doExecute

	public void getResult() {
	}//getResult

    /*_______Operations inherited from LoggableRequest____________*/
    public String getParamsLoggingInfo() {
        final StringBuffer result = new StringBuffer(CLSNAME+"(VO=");
        result.append(vo);
        result.append(")");
        return result.toString();
    }

    public String getResultLoggingInfo() {
        if (ex==null)
            return "Updated VO="+vo;
        else
            return "Exception="+DebugOperations.getShortNameForClass(ex.getClass());
    }

}//class
/*______________________________EOF_________________________________*/
