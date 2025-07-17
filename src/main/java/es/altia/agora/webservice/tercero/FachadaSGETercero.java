package es.altia.agora.webservice.tercero;

import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.TiposViasDAO;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.PaisesDAO;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.ProvinciasDAO;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.MunicipiosDAO;
import es.altia.agora.business.terceros.mantenimiento.MunicipioVO;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.TipoDocumentosDAO;
import es.altia.util.conexion.BDException;

import java.sql.SQLException;

public class FachadaSGETercero {

    public GeneralValueObject getTipoViaByAbreviatura(String abrvTipoVia, String[] params) throws BDException, SQLException {
        return TiposViasDAO.getInstance().getTipoViaByAbreviatura(abrvTipoVia, params);
    }

    public GeneralValueObject getPaisByDescription(String descripcionPais, String[] params) throws BDException, SQLException {
        return PaisesDAO.getInstance().getPaisByDescription(descripcionPais, params);
    }

    public GeneralValueObject getProvinciaByPaisAndDesc(int codPais, String descProvincia, String params[])
    throws BDException, SQLException {
        return ProvinciasDAO.getInstance().getProvinciaByPaisAndDesc(codPais, descProvincia, params);
    }

    public MunicipioVO getMunicipioByPaisAndProvAndDesc(int codPais, int codProvincia, String descMunicipio, String params[])
    throws BDException, SQLException {
        return MunicipiosDAO.getInstance().getMunicipioByPaisAndProvAndDesc(codPais, codProvincia, descMunicipio, params);
    }

    public GeneralValueObject getTipoViaByDescripcion(String descTipoVia, String[] params)
    throws BDException, SQLException {
        return TiposViasDAO.getInstance().getTipoViaByDescripcion(descTipoVia, params);
    }

    public GeneralValueObject getPaisByCodigo(int codigoPais, String[] params) throws BDException, SQLException {
        return PaisesDAO.getInstance().getPaisByCodigo(codigoPais, params);
    }

    public GeneralValueObject getProvinciaByPaisAndCodigo(int codPais, int codProvincia, String params[])
    throws BDException, SQLException {
        return ProvinciasDAO.getInstance().getProvinciaByPaisAndCodigo(codPais, codProvincia, params);
    }

    public MunicipioVO getMunicipioByPaisAndProvAndCodigo(int codPais, int codProvincia, int codMunicipio, String params[])
    throws BDException, SQLException {
        return MunicipiosDAO.getInstance().getMunicipioByPaisAndProvAndCodigo(codPais, codProvincia, codMunicipio, params);
    }
    
    public String getDescripcionTipoDocumentoByCodigo(String codigo, String[] params) {
        return TipoDocumentosDAO.getInstance().getByPrimaryKey(params, codigo);
    }
}
