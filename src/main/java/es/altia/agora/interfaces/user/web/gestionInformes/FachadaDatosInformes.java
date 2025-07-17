package es.altia.agora.interfaces.user.web.gestionInformes;

import es.altia.agora.business.gestionInformes.FichaInformeValueObject;
import es.altia.agora.business.gestionInformes.SolicitudInformeValueObject;

import java.util.Vector;

public interface FachadaDatosInformes {

    public String getOrigen();

    public void setOrigen(String origen);

    public Vector getListaCampos(FichaInformeValueObject fiVO, String[] params);
    public Vector getListaCriteriosDisponibles(FichaInformeValueObject fiVO, String[] params);
    public Vector getListaPermisos(String[] params);
    public Vector getListaCriInforme(FichaInformeValueObject fiVO, String[] params);
    public Vector getListaCriSeleccionados(FichaInformeValueObject fiVO, String[] params);
    public Vector getListaCriFinal(FichaInformeValueObject fiVO, String[] params);

    public Vector getDatosExpedientes(SolicitudInformeValueObject siVO, String codOrganizacion, String[] params) throws Exception;

}
