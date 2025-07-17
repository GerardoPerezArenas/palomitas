package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.*;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import java.util.ArrayList;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.util.Vector;


import org.apache.struts.action.ActionForm;


/** Clase utilizada para capturar o mostrar el estado de un RegistroEntrada */
public class FichaRelacionExpedientesForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(FichaRelacionExpedientesForm.class.getName());

    private String numExpediente;
    private String numeroRelacion;
    private String numeroRelacionMostrar;
    private String procedimiento;
    private String titular;
    private String fechaInicio;
    private String fechaFin;
    private String usuario;
    private String codUnidadOrganicaExp;
    private String descUnidadOrganicaExp;
    private String observaciones;
    private String asunto;

    private String respOpcion;
    private Vector permisosTramites;

    private Vector expedientes;
    private Vector tramites;
    private Vector documentos;
    private Vector enlaces;
    private Vector estructuraDatosSuplementarios;
    private Vector valoresDatosSuplementarios;
    private Vector tramitesDisponibles;
    private Vector listaUnidadesUsuario;
    private GeneralValueObject listaFicheros = new GeneralValueObject();
    private GeneralValueObject listaTiposFicheros = new GeneralValueObject();

    private String usuarioIni;
    private String codUORIni;
    private String tramiteIni;
    private Vector listaMunExp;
    private Vector listaProExp;
    private Vector listaEjeExp;
    private Vector listaNumExp;

    private String mensajeSW;

    private String bloqueo;

    private ArrayList<ModuloIntegracionExterno> modulosExternos = null;
    private ArrayList<TramitacionExpedientesValueObject> tramitesDestino = null;

    public String getUsuarioIni (){return (String) fichaRelExpVO.getAtributo("usuarioIni");}
    public void setUsuarioIni (String valor) { fichaRelExpVO.setAtributo("usuarioIni",valor);}

    public String getCodUORIni (){return (String) fichaRelExpVO.getAtributo("codUORIni");}
    public void setCodUORIni (String valor) { fichaRelExpVO.setAtributo("codUORIni",valor);}

    public String getTramiteIni (){return (String) fichaRelExpVO.getAtributo("tramiteIni");}
    public void setTramiteIni (String valor) { fichaRelExpVO.setAtributo("tramiteIni",valor);}

    public Vector getListaMunExp (){return this.listaMunExp;}
    public void setListaMunExp (Vector valor) { this.listaMunExp=valor;}

    public Vector getListaProExp (){return this.listaProExp;}
    public void setListaProExp (Vector valor) { this.listaProExp=valor;}

    public Vector getListaEjeExp (){return this.listaEjeExp;}
    public void setListaEjeExp (Vector valor) { this.listaEjeExp=valor;}

    public Vector getListaNumExp (){return this.listaNumExp;}
    public void setListaNumExp (Vector valor) { this.listaNumExp=valor;}








    private GeneralValueObject fichaRelExpVO = new GeneralValueObject();

    public String getRefCatastral (){return (String) fichaRelExpVO.getAtributo("refCatastral");}
    public void setRefCatastral (String valor) { fichaRelExpVO.setAtributo("refCatastral",valor);}

    public String getNumExpediente(){ return (String)fichaRelExpVO.getAtributo("numero"); }
    public void setNumExpediente(String numExpediente){ fichaRelExpVO.setAtributo("numero",numExpediente); }

    public String getNumeroRelacion(){ return (String)fichaRelExpVO.getAtributo("numeroRelacion"); }
    public void setNumeroRelacion(String numeroRelacion){ fichaRelExpVO.setAtributo("numeroRelacion",numeroRelacion); }

    public String getNumeroRelacionMostrar(){ return (String)fichaRelExpVO.getAtributo("numeroRelacionMostrar"); }
    public void setNumeroRelacionMostrar(String numeroRelacionMostrar){ fichaRelExpVO.setAtributo("numeroRelacionMostrar",numeroRelacionMostrar); }

    public String getProcedimiento(){ return (String)fichaRelExpVO.getAtributo("nombreProcedimiento"); }
    public void setProcedimiento(String procedimiento){ fichaRelExpVO.setAtributo("nombreProcedimiento",procedimiento); }

    public String getTitular(){ return (String)fichaRelExpVO.getAtributo("titular"); }
    public void setTitular(String titular){ fichaRelExpVO.setAtributo("titular",titular); }

    public String getLocalizacion(){ return (String)fichaRelExpVO.getAtributo("localizacion"); }
    public void setLocalizacion(String localizacion){ fichaRelExpVO.setAtributo("localizacion",localizacion); }

    public String getFechaInicio(){ return (String)fichaRelExpVO.getAtributo("fechaInicioRelacion"); }
    public void setFechaInicio(String fechaInicio){ fichaRelExpVO.setAtributo("fechaInicioRelacion",fechaInicio); }

    public String getFechaFin(){ return (String)fichaRelExpVO.getAtributo("fechaFinRelacion"); }
    public void setFechaFin(String fechaFin){ fichaRelExpVO.setAtributo("fechaFinRelacion",fechaFin); }

    public String getUsuario(){ return (String)fichaRelExpVO.getAtributo("usuarioRelacion"); }
    public void setUsuario(String usuario){ fichaRelExpVO.setAtributo("usuarioRelacion",usuario); }

    public String getCodUnidadOrganicaExp(){ return (String)fichaRelExpVO.getAtributo("codUnidadOrganicaExp"); }
    public void setCodUnidadOrganicaExp(String codUnidadOrganicaExp){ fichaRelExpVO.setAtributo("codUnidadOrganicaExp",codUnidadOrganicaExp); }

    public String getDescUnidadOrganicaExp(){ return (String)fichaRelExpVO.getAtributo("descUnidadOrganicaExp"); }
    public void setDescUnidadOrganicaExp(String descUnidadOrganicaExp){ fichaRelExpVO.setAtributo("descUnidadOrganicaExp",descUnidadOrganicaExp); }

    public String getObservaciones(){return (String)fichaRelExpVO.getAtributo("observaciones");}
    public void setObservaciones(String observaciones){ fichaRelExpVO.setAtributo("observaciones",observaciones); }

    public String getAsunto(){return (String)fichaRelExpVO.getAtributo("asunto");}
    public void setAsunto(String asunto){ fichaRelExpVO.setAtributo("asunto",asunto); }

    public String getRespOpcion(){ return (String)fichaRelExpVO.getAtributo("respOpcion"); }
    public void setRespOpcion(String respOpcion){ fichaRelExpVO.setAtributo("respOpcion",respOpcion); }


    public String getPoseeLocalizacion(){ return (String)fichaRelExpVO.getAtributo("poseeLocalizacion"); }
    public void setPoseeLocalizacion(String poseeLocalizacion){ fichaRelExpVO.setAtributo("poseeLocalizacion",poseeLocalizacion); }

    public String getCodLocalizacion(){ return (String)fichaRelExpVO.getAtributo("codLocalizacion"); }
    public void setCodLocalizacion(String codLocalizacion){ fichaRelExpVO.setAtributo("codLocalizacion",codLocalizacion); }

    public Vector getExpedientes(){ return expedientes; }
    public void setExpedientes(Vector expedientes){ this.expedientes=expedientes; }

    public Vector getTramites(){ return tramites; }
    public void setTramites(Vector tramites){ this.tramites=tramites; }

    public Vector getDocumentos(){ return documentos; }
    public void setDocumentos(Vector documentos){ this.documentos=documentos; }

    public Vector getEnlaces(){ return enlaces; }
    public void setEnlaces(Vector enlaces){ this.enlaces=enlaces; }

    public Vector getEstructuraDatosSuplementarios(){ return estructuraDatosSuplementarios; }
    public void setEstructuraDatosSuplementarios(Vector estructuraDatosSuplementarios){ this.estructuraDatosSuplementarios=estructuraDatosSuplementarios; }

    public Vector getValoresDatosSuplementarios(){ return valoresDatosSuplementarios; }
    public void setValoresDatosSuplementarios(Vector valoresDatosSuplementarios){ this.valoresDatosSuplementarios=valoresDatosSuplementarios; }

    public Vector getTramitesDisponibles(){ return tramitesDisponibles; }
    public void setTramitesDisponibles(Vector tramitesDisponibles){ this.tramitesDisponibles=tramitesDisponibles; }

    public String getCodMunicipio(){ return (String)fichaRelExpVO.getAtributo("codMunicipio"); }
    public void setCodMunicipio(String codMunicipio){ fichaRelExpVO.setAtributo("codMunicipio",codMunicipio); }

    public String getCodProcedimiento(){ return (String)fichaRelExpVO.getAtributo("codProcedimiento"); }
    public void setCodProcedimiento(String codProcedimiento){ fichaRelExpVO.setAtributo("codProcedimiento",codProcedimiento); }

    public String getCodTramite(){ return (String)fichaRelExpVO.getAtributo("codTramite"); }
    public void setCodTramite(String codTramite){ fichaRelExpVO.setAtributo("codTramite",codTramite); }

    public String getEjercicio(){ return (String)fichaRelExpVO.getAtributo("ejercicio"); }
    public void setEjercicio(String ejercicio){ fichaRelExpVO.setAtributo("ejercicio",ejercicio); }

    public String getNumero(){ return (String)fichaRelExpVO.getAtributo("numero"); }
    public void setNumero(String numero){ fichaRelExpVO.setAtributo("numero",numero); }

    public String getCodMunExpIni(){ return (String)fichaRelExpVO.getAtributo("codMunExpIni"); }
    public void setCodMunExpIni(String codMunExpIni){ fichaRelExpVO.setAtributo("codMunExpIni",codMunExpIni); }

    public String getEjercicioExpIni(){ return (String)fichaRelExpVO.getAtributo("ejercicioExpIni"); }
    public void setEjercicioExpIni(String ejercicioExpIni){ fichaRelExpVO.setAtributo("ejercicioExpIni",ejercicioExpIni); }

    public String getNumeroExpIni(){ return (String)fichaRelExpVO.getAtributo("numeroExpIni"); }
    public void setNumeroExpIni(String numeroExpIni){ fichaRelExpVO.setAtributo("numeroExpIni",numeroExpIni); }

    public String getNotificacionRealizada(){ return (String)fichaRelExpVO.getAtributo("notificacionRealizada"); }
    public void setNotificacionRealizada(String notificacionRealizada){ fichaRelExpVO.setAtributo("notificacionRealizada",notificacionRealizada); }

    public Vector getListaUnidadesUsuario(){ return listaUnidadesUsuario; }
    public void setListaUnidadesUsuario(Vector listaUnidadesUsuario){ this.listaUnidadesUsuario=listaUnidadesUsuario; }

    public GeneralValueObject getListaFicheros(){ return listaFicheros; }
    public void setListaFicheros(GeneralValueObject listaFicheros){ this.listaFicheros=listaFicheros; }

    public GeneralValueObject getListaTiposFicheros(){ return listaTiposFicheros; }
    public void setListaTiposFicheros(GeneralValueObject listaTiposFicheros){ this.listaTiposFicheros=listaTiposFicheros; }

    public GeneralValueObject getFichaRelExpVO(){ return fichaRelExpVO; }
    public void setFichaRelExpVO(GeneralValueObject fichaRelExpVO){ this.fichaRelExpVO=fichaRelExpVO;}

    public Vector getPermisosTramites() { return permisosTramites; }
    public void setPermisosTramites(Vector permisosTramites) { this.permisosTramites = permisosTramites; }

    public String getMensajeSW() {
        return mensajeSW;
    }

    public void setMensajeSW(String mensajeSW) {
        this.mensajeSW = mensajeSW;
    }

    public String getBloqueo(){ return (String)fichaRelExpVO.getAtributo("bloqueo"); }
    public void setBloqueo(String bloqueo){ fichaRelExpVO.setAtributo("bloqueo",bloqueo); }

     /**
     * @return the tramitesAbiertos
     */
    public ArrayList<TramitacionExpedientesValueObject> getTramitesDestino() {
        return tramitesDestino;
    }

    /**
     * @param tramitesAbiertos the tramitesAbiertos to set
     */
    public void setTramitesDestino(ArrayList<TramitacionExpedientesValueObject> tramitesAbiertos) {
        this.tramitesDestino = tramitesAbiertos;
    }

    /**
     * @return the modulos
     */
    public ArrayList<ModuloIntegracionExterno> getModulosExternos() {
        return modulosExternos;
    }

    /**
     * @param modulos the modulos to set
     */
    public void setModulosExternos(ArrayList<ModuloIntegracionExterno> modulos) {
        this.modulosExternos = modulos;
    }
}