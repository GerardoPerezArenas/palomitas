package es.altia.flexia.tramitacion.externa.plugin;

import java.io.Serializable;
import java.util.ArrayList;
import org.apache.log4j.Logger;


public class TramitacionExternaBase implements Serializable{
    private String codOrganizacion;
    private String codUsuario;
    private String codTramite;
    private String ocurrenciaTramite;
    private String numExpediente;
    private String codProcedimiento;
    private String codUorTramitadora;
    private String ejercicio;
    private String descripcionPluginDefinicionTramite;
    private String nombrePlugin;
    private ArrayList<EnlaceTramitacionBase> enlaces = null;
    private EnlaceTramitacionBase pantallaDefinitiva = null;
    private String implClass = null;
    private boolean tramiteAbierto;
    private boolean bloqueadoFinalizarTramite;
    private String parametrosVentana;
    private Logger log = Logger.getLogger(TramitacionExternaBase.class);
    private boolean bloqueadoRetrocesoTramite;
    
    /**
     * @return the codOrganizacion
     */
    public String getCodOrganizacion() {
        return codOrganizacion;
    }

    /**
     * @param codOrganizacion the codOrganizacion to set
     */
    public void setCodOrganizacion(String codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    /**
     * @return the codUsuario
     */
    public String getCodUsuario() {
        return codUsuario;
    }

    /**
     * @param codUsuario the codUsuario to set
     */
    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    /**
     * @return the codTramite
     */
    public String getCodTramite() {
        return codTramite;
    }

    /**
     * @param codTramite the codTramite to set
     */
    public void setCodTramite(String codTramite) {
        this.codTramite = codTramite;
    }

    /**
     * @return the ocurrenciaOrganizacion
     */
    public String getOcurrenciaTramite() {
        return ocurrenciaTramite;
    }

    /**
     * @param ocurrenciaOrganizacion the ocurrenciaOrganizacion to set
     */
    public void setOcurrenciaTramite(String ocurrenciaTramite) {
        this.ocurrenciaTramite = ocurrenciaTramite;
    }

    /**
     * @return the numExpediente
     */
    public String getNumExpediente() {
        return numExpediente;
    }

    /**
     * @param numExpediente the numExpediente to set
     */
    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    /**
     * @return the codUorTramitadora
     */
    public String getCodUorTramitadora() {
        return codUorTramitadora;
    }

    /**
     * @param codUorTramitadora the codUorTramitadora to set
     */
    public void setCodUorTramitadora(String codUorTramitadora) {
        this.codUorTramitadora = codUorTramitadora;
    }

    /**
     * @return the descripcionPluginDefinicionTramite
     */
    public String getDescripcionPluginDefinicionTramite() {
        return descripcionPluginDefinicionTramite;
    }

    /**
     * @param descripcionPluginDefinicionTramite the descripcionPluginDefinicionTramite to set
     */
    public void setDescripcionPluginDefinicionTramite(String descripcionPluginDefinicionTramite) {
        this.descripcionPluginDefinicionTramite = descripcionPluginDefinicionTramite;
    }

    /**
     * @return the nombrePlugin
     */
    public String getNombrePlugin() {
        return nombrePlugin;
    }

    /**
     * @param nombrePlugin the nombrePlugin to set
     */
    public void setNombrePlugin(String nombrePlugin) {
        this.nombrePlugin = nombrePlugin;
    }

   
    /**
     * @return the enlaces
     */
    public ArrayList<EnlaceTramitacionBase> getEnlaces() {
        return enlaces;
    }

    /**
     * @param enlaces the enlaces to set
     */
    public void setEnlaces(ArrayList<EnlaceTramitacionBase> enlaces) {
        this.enlaces = enlaces;
    }

    /**
     * @return the implClass
     */
    public String getImplClass() {
        return implClass;
    }

    /**
     * @param implClass the implClass to set
     */
    public void setImplClass(String implClass) {
        this.implClass = implClass;
    }

    /**
     * Devuelve el objeto EnlaceTramitacionBase que contiene la lista de parámetros, métodos del plugin a llamar y
     * url de la ficha de tramitación externa.
     * @return EnlaceTramitacionBase
     */
    public EnlaceTramitacionBase getPantallaDefinitiva() {
        return pantallaDefinitiva;
    }

    /**
     * Establece el objeto EnlaceTramitacionBase que contiene la lista de parámetros, métodos del plugin a llamar y
     * url de la ficha de tramitación externa.
     * @param pantallaDefinitiva: EnlaceTramitacionBase
     */
    public void setPantallaDefinitiva(EnlaceTramitacionBase pantallaDefinitiva) {
        this.pantallaDefinitiva = pantallaDefinitiva;
    }

   
   /**
     * Devuelve la url de la pantalla completa
     * @return
     */
    public String getUrlLlamadaCompleta(){
        String salida = null;
        if(this.pantallaDefinitiva!=null){
            String[] parametros = pantallaDefinitiva.getParametros();
            String[] metodos    = pantallaDefinitiva.getMetodos();
            String url          = pantallaDefinitiva.getUrl();

            TramitacionExternaCargador cargador = TramitacionExternaCargador.getInstance();
            StringBuffer aux = new StringBuffer();
            aux.append("?");
            for(int i=0;parametros!=null && i<parametros.length;i++){
                String parametro = parametros[i];
                String metodo    = metodos[i];
                Class[] tipoParametros = null;
                Object[] valoresParametros =  null;            
                String valor = null;
                Object oValor = cargador.ejecutarMetodo(this,metodo, tipoParametros, valoresParametros);
                if(oValor instanceof String)
                    valor = (String)oValor;
                else
                if(oValor instanceof Boolean) valor = oValor.toString();

                if(valor!=null){
                    log.debug("Se agrega el par " + parametro + "=" + valor);
                    aux.append(parametro + "=" + valor);
                    if(parametros.length-i>1)
                        aux.append("&");                    
                }
            }// for
            if(aux!=null && !"".equals(aux.toString()))
                salida = url + aux.toString();
        }// if

        return salida;
    }


    /**
     * @return the codProcedimiento
     */
    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    /**
     * @param codProcedimiento the codProcedimiento to set
     */
    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    /**
     * @return the tramiteAbierto
     */
    public boolean isTramiteAbierto() {
        return tramiteAbierto;
    }

    /**
     * @param tramiteAbierto the tramiteAbierto to set
     */
    public void setTramiteAbierto(boolean tramiteAbierto) {
        this.tramiteAbierto = tramiteAbierto;
    }

    /**
     * @return the ejercicio
     */
    public String getEjercicio() {
        return ejercicio;
    }

    /**
     * @param ejercicio the ejercicio to set
     */
    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    /**
     * @return the bloqueadoFinalizarTramite
     */
    public boolean isBloqueadoFinalizarTramite() {
        return bloqueadoFinalizarTramite;
    }

    /**
     * @param bloqueadoFinalizarTramite the bloqueadoFinalizarTramite to set
     */
    public void setBloqueadoFinalizarTramite(boolean bloqueadoFinalizarTramite) {
        this.bloqueadoFinalizarTramite = bloqueadoFinalizarTramite;
    }

    /**
     * @return the parametrosVentana
     */
    public String getParametrosVentana() {
        return parametrosVentana;
    }

    /**
     * @param parametrosVentana the parametrosVentana to set
     */
    public void setParametrosVentana(String parametrosVentana) {
        this.parametrosVentana = parametrosVentana;
    }

    /**
     * @return the bloqueadoRetrocesoTramite
     */
    public boolean isBloqueadoRetrocesoTramite() {
        return bloqueadoRetrocesoTramite;
    }

    /**
     * @param bloqueadoRetrocesoTramite the bloqueadoRetrocesoTramite to set
     */
    public void setBloqueadoRetrocesoTramite(boolean bloqueadoRetrocesoTramite) {
        this.bloqueadoRetrocesoTramite = bloqueadoRetrocesoTramite;
    }
   
}