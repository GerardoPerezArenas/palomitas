package es.altia.flexia.integracion.moduloexterno.plugin;

import es.altia.agora.business.util.GeneralValueObject;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.DatosPantallaModuloVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.EstructuraCampoModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.EstructuraEtiquetaModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.OperacionModuloIntegracionExternoVO;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Clase padre que deben extender los diferentes módulos
 */
public class ModuloIntegracionExterno {
    private String nombreModulo;
    private String descripcionModulo;
    private ArrayList<OperacionModuloIntegracionExternoVO> listaOperacionesDisponibles;        
    // Lista de pantallas a nivel de expediente del módulo a cargar para un determinado procedimiento
    private ArrayList<DatosPantallaModuloVO> listaPantallasExpediente = new ArrayList<DatosPantallaModuloVO>();
    // Lista de pantallas a nivel de trámite del módulo a cargar para un determinado procedimiento y trámite
    private ArrayList<DatosPantallaModuloVO> listaPantallasTramite = new ArrayList<DatosPantallaModuloVO>();
	// Lista de pantallas a nivel de dfinicion de procedimiento
    private ArrayList<DatosPantallaModuloVO>listaPantallasDefinicionProcedimiento = new ArrayList<DatosPantallaModuloVO>();
    
    //Representan campos del módulo, por los que luego podremos filtrar
    //desde Gestión de Expedientes/Consulta de Expedientes/Consulta campos suplementarios
    private ArrayList<EstructuraCampoModuloIntegracionVO> camposConsulta=new ArrayList<EstructuraCampoModuloIntegracionVO>();
    
    //Etiquetas para para exportar a las plantillas
    private ArrayList<EstructuraEtiquetaModuloIntegracionVO> etiquetas= new ArrayList<EstructuraEtiquetaModuloIntegracionVO>();
    
    /** Devuelve una descripción más completa del módulo
        @return String */
    public String getDescripcionModulo(){
        return this.descripcionModulo;
    }

    public void setDescripcionModulo(String descripcion){
        this.descripcionModulo = descripcion;
    }
        
    /** Devuelve el nombre del módulo tal y como aparece en el fichero de configuración
        @return String */            
    public String getNombreModulo(){
        return this.nombreModulo;
    }

    public void setNombreModulo(String nombre){
        this.nombreModulo = nombre;
    }

    /**
     * @return the listaOperacionesDisponibles
     */
    public ArrayList<OperacionModuloIntegracionExternoVO> getListaOperacionesDisponibles() {
        return listaOperacionesDisponibles;
    }

    /**
     * @param listaOperacionesDisponibles the listaOperacionesDisponibles to set
     */
    public void setListaOperacionesDisponibles(ArrayList<OperacionModuloIntegracionExternoVO> listaOperacionesDisponibles) {
        this.listaOperacionesDisponibles = listaOperacionesDisponibles;
    }

    /**
     * @return the listaPantallasExpediente
     */
    public ArrayList<DatosPantallaModuloVO> getListaPantallasExpediente() {
        return listaPantallasExpediente;
    }

    /**
     * @param listaPantallasExpediente the listaPantallasExpediente to set
     */
    public void setListaPantallasExpediente(ArrayList<DatosPantallaModuloVO> listaPantallasExpediente) {
        this.listaPantallasExpediente = listaPantallasExpediente;
    }

    /**
     * @return the listaPantallasTramite
     */
    public ArrayList<DatosPantallaModuloVO> getListaPantallasTramite() {
        return listaPantallasTramite;
    }

    /**
     * @param listaPantallasTramite the listaPantallasTramite to set
     */
    public void setListaPantallasTramite(ArrayList<DatosPantallaModuloVO> listaPantallasTramite) {
        this.listaPantallasTramite = listaPantallasTramite;
 }

    public ArrayList<DatosPantallaModuloVO> getListaPantallasDefinicionProcedimiento() {
        return listaPantallasDefinicionProcedimiento;
    }

    public void setListaPantallasDefinicionProcedimiento(ArrayList<DatosPantallaModuloVO> listaPantallasDefinicionProcedimiento) {
        this.listaPantallasDefinicionProcedimiento = listaPantallasDefinicionProcedimiento;
    }    

    public ArrayList<EstructuraCampoModuloIntegracionVO> getCamposConsulta() {
        return camposConsulta;
    }

    public void setCamposConsulta(ArrayList<EstructuraCampoModuloIntegracionVO> camposConsulta) {
        this.camposConsulta = camposConsulta;
    }

    public ArrayList<EstructuraEtiquetaModuloIntegracionVO> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(ArrayList<EstructuraEtiquetaModuloIntegracionVO> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public void cargarExpedienteExtension(int codOrganizacion,String numExpediente, String xml)throws Exception{
        
    }
    
    


}