package es.altia.agora.business.gestionInformes.particular;

import es.altia.common.exception.TechnicalException;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;

import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;

public interface InformeParticularFacade {

    public ArrayList<CodigoEtiqueta> getCodigosEtiquetasSelect(String idCampo, UsuarioValueObject usuario) throws TechnicalException;

    public GeneralValueObject recuperarDatosRequest(HttpServletRequest request);

    public Collection recuperaDatosInforme(GeneralValueObject datosEntrada, UsuarioValueObject usuario) throws TechnicalException;

    public String generarInforme(GeneralValueObject datosEntrada, Collection datosInforme,
    		UsuarioValueObject usuVO, String url, String servlet) throws TechnicalException;

    public void setParamsBD(String[] params);
}
