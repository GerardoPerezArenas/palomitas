package es.altia.agora.webservice.via;

import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.TiposViasDAO;
import es.altia.util.conexion.BDException;

import java.sql.SQLException;

public class FachadaSGEVia {

    public GeneralValueObject getTipoViaByDescripcion(String descTipoVia, String[] params) throws BDException, SQLException {
        return TiposViasDAO.getInstance().getTipoViaByDescripcion(descTipoVia, params);
    }

}
