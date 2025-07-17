package es.altia.agora.business.sge.plugin.documentos;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.util.configuration.ConfigurationParametersManager;
import java.io.Serializable;
import java.util.ResourceBundle;

/**
 *
 * @author oscar
 */
public class CachePropiedadesConfiguracionGestor implements Serializable{

    private static String urlGestor  = null;
    private static String usuarioGestor = null;
    private static String passwordGestor = null;
    private static String carpetaRaiz    = null;
    private static String implClassGestor = null;
    private static int longitudCodigoTramiteVisible = 4;
    private static int longitudNumeroDocumento = 4;
    private static int longitudOcurrenciaTramiteInterno  = 4;
    private static CachePropiedadesConfiguracionGestor instance = null;
    
    private CachePropiedadesConfiguracionGestor(){
        
    }
    
    
    public static CachePropiedadesConfiguracionGestor getInstance(int codMunicipio,String nombreServicio){
        
        if(instance==null){            
            ResourceBundle config = ResourceBundle.getBundle("documentos");            
            urlGestor                        = config.getString("Almacenamiento/" + codMunicipio + "/" + nombreServicio + "/urlGestor");
            usuarioGestor                    = config.getString("Almacenamiento/" + codMunicipio + "/" + nombreServicio + "/usuarioGestor");
            passwordGestor                   = config.getString("Almacenamiento/" + codMunicipio + "/" + nombreServicio + "/passwordGestor");
            carpetaRaiz                      = config.getString("Almacenamiento/" + codMunicipio + "/" + nombreServicio + "/carpetaRaiz");
            implClassGestor                  = config.getString("Almacenamiento/" + codMunicipio + "/" + nombreServicio + "/implClass");
            implClassGestor                  = config.getString("Almacenamiento/" + codMunicipio + "/" + nombreServicio + "/implClass");
            longitudCodigoTramiteVisible     = Integer.parseInt(config.getString(ConstantesDatos.PROPIEDAD_LONGITUD_CODIGO_TRAMITE_VISIBLE_BD));
            longitudNumeroDocumento          = Integer.parseInt(config.getString(ConstantesDatos.PROPIEDAD_LONGITUD_NUMERO_DOCUMENTO_BD));
            longitudOcurrenciaTramiteInterno = Integer.parseInt(config.getString(ConstantesDatos.PROPIEDAD_LONGITUD_OCURRENCIA_TRAMITE_INTERNO_BD));                        
            instance = new CachePropiedadesConfiguracionGestor();
        }        
        return instance;
    }
    
    
    /**
     * @return the urlGestor
     */
    public String getUrlGestor() {
        return urlGestor;
    }

    /**
     * @param urlGestor the urlGestor to set
     */
    public void setUrlGestor(String urlGestor) {
        this.urlGestor = urlGestor;
    }

    /**
     * @return the usuarioGestor
     */
    public String getUsuarioGestor() {
        return usuarioGestor;
    }

    /**
     * @param usuarioGestor the usuarioGestor to set
     */
    public void setUsuarioGestor(String usuarioGestor) {
        this.usuarioGestor = usuarioGestor;
    }

    /**
     * @return the passwordGestor
     */
    public String getPasswordGestor() {
        return passwordGestor;
    }

    /**
     * @param passwordGestor the passwordGestor to set
     */
    public void setPasswordGestor(String passwordGestor) {
        this.passwordGestor = passwordGestor;
    }

    /**
     * @return the carpetaRaiz
     */
    public String getCarpetaRaiz() {
        return carpetaRaiz;
    }

    /**
     * @param carpetaRaiz the carpetaRaiz to set
     */
    public void setCarpetaRaiz(String carpetaRaiz) {
        this.carpetaRaiz = carpetaRaiz;
    }

    /**
     * @return the implClassGestor
     */
    public String getImplClassGestor() {
        return implClassGestor;
    }

    /**
     * @param implClassGestor the implClassGestor to set
     */
    public void setImplClassGestor(String implClassGestor) {
        this.implClassGestor = implClassGestor;
    }

    /**
     * @return the longitudCodigoTramiteVisible
     */
    public int getLongitudCodigoTramiteVisible() {
        return longitudCodigoTramiteVisible;
    }

    /**
     * @param longitudCodigoTramiteVisible the longitudCodigoTramiteVisible to set
     */
    public void setLongitudCodigoTramiteVisible(int longitudCodigoTramiteVisible) {
        this.longitudCodigoTramiteVisible = longitudCodigoTramiteVisible;
    }

    /**
     * @return the longitudNumeroDocumento
     */
    public int getLongitudNumeroDocumento() {
        return longitudNumeroDocumento;
    }

    /**
     * @param longitudNumeroDocumento the longitudNumeroDocumento to set
     */
    public void setLongitudNumeroDocumento(int longitudNumeroDocumento) {
        this.longitudNumeroDocumento = longitudNumeroDocumento;
    }

    /**
     * @return the longitudOcurrenciaTramiteInterno
     */
    public int getLongitudOcurrenciaTramiteInterno() {
        return longitudOcurrenciaTramiteInterno;
    }

    /**
     * @param longitudOcurrenciaTramiteInterno the longitudOcurrenciaTramiteInterno to set
     */
    public void setLongitudOcurrenciaTramiteInterno(int longitudOcurrenciaTramiteInterno) {
        this.longitudOcurrenciaTramiteInterno = longitudOcurrenciaTramiteInterno;
    }        
    
}