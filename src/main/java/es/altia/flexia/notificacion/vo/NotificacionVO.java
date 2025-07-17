package es.altia.flexia.notificacion.vo;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NotificacionVO {

    private String aplicacion;
    private String actoNotificado;
    private String codigoTipoNotificacion;
    private int caducidadNotificacion=-1 ;
    private String textoNotificacion;
    private String nombreExpediente;
    private String firma;
    private ArrayList<AutorizadoNotificacionVO> autorizados = null;
    private ArrayList<AdjuntoNotificacionVO> adjuntos = null;
    private ArrayList<AdjuntoNotificacionVO> adjuntosExternos = null;
    private String numExpediente;
    private String codigoProcedimiento;
    private int ejercicio=-1;
    private int codigoMunicipio=-1;
    private int codigoTramite=-1;
    private int ocurrenciaTramite=-1;
    private int codigoNotificacion=-1;
    private String estadoNotificacion;
    private String codDepartamento;
    private String[] params;

    private int claseProcedimiento;
    private String departamento;
    private String nombreTramite;
    private String numeroRegistroTelematico;
    private Calendar fechaEnvio;
    private String fechaEnvioAsString;
    private String nombreEmisor;
    private String idEmisor;
    private Calendar fechaAcuse;
    private String fechaAcuseAsString;
    private String resultado;
    private String personaAcuseDNI;
    private String personaAcuseNombre;



    //#297244
    private Calendar fechaSolEnvio;
    private String fechaSolEnvioAsString = "--";
    private String identificadorPlatea = "--";

//aplicacion: De tipo String. Representa el nombre de la aplicación que hay que pasar al servicio web.
//actoNotificado: De tipo String. Representa el acto notificado de la notificación.
//codigoTipoNotificacion: De tipo String. Representa el código del tipo de notificación.
//caducidadNotificacion: De tipo int. Representa el número de días, transcurridos los cuales, la notificación ya no es visible en la bandeja de entrada del sistema de notificaciones.
//textoNotificacion: De tipo String. Representa el texto de la notificación.
//nombreExpediente:  Nombre del expediente.
//firma: De tipo String. Contiene la firma de la notación en Base64.
//ArrayList<AutorizadoNotificacionVO> autorizados: Colección con los autorizados a los que se les dirige la notificación.
//ArrayList<AdjuntoNotificacionVO> adjuntos: Colección con los adjuntos asociados a la notificación.
//numExpediente: De tipo String. Representa el número de expediente al que está asociado la notificación.
//codigoProcedimiento: De tipo String. Representa el código del procedimiento.
//ejercicio: De tipo int. Representa el ejercicio del expediente.
//codigoMunicipio: De tipo int. Representa el código del municipio.
//codigoTramite: De tipo int. Representa el código del trámite al que está asociada la notificación.
//ocurrenciaTramite: De tipo int. Representa la ocurrencia del trámite.
//codigoNotificacion: De tipo int. Representa el código de la notificación.
//estadoNotificacion: De tipo String. Representa el estado de la notificación.
//params: De tipo String[]. Contiene los parámetros de conexión a la base de datos.

    public String getActoNotificado() {
        if (actoNotificado==null)return("");
        return actoNotificado;
    }

    public void setActoNotificado(String actoNotificado) {
        this.actoNotificado = actoNotificado;
    }

    public ArrayList<AdjuntoNotificacionVO> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(ArrayList<AdjuntoNotificacionVO> adjuntos) {
        this.adjuntos = adjuntos;
    }

    public String getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    public ArrayList<AutorizadoNotificacionVO> getAutorizados() {
        return autorizados;
    }

    public void setAutorizados(ArrayList<AutorizadoNotificacionVO> autorizados) {
        this.autorizados = autorizados;
    }

    public int getCaducidadNotificacion() {

        return caducidadNotificacion;
    }

    public void setCaducidadNotificacion(int caducidadNotificacion) {
        this.caducidadNotificacion = caducidadNotificacion;
    }

    public int getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(int codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public int getCodigoNotificacion() {
        return codigoNotificacion;
    }

    public void setCodigoNotificacion(int codigoNotificacion) {
        this.codigoNotificacion = codigoNotificacion;
    }

    public String getCodigoProcedimiento() {
        return codigoProcedimiento;
    }

    public void setCodigoProcedimiento(String codigoProcedimiento) {
        this.codigoProcedimiento = codigoProcedimiento;
    }

    public String getCodigoTipoNotificacion() {
        return codigoTipoNotificacion;
    }

    public void setCodigoTipoNotificacion(String codigoTipoNotificacion) {
        this.codigoTipoNotificacion = codigoTipoNotificacion;
    }

    public int getCodigoTramite() {
        return codigoTramite;
    }

    public void setCodigoTramite(int codigoTramite) {
        this.codigoTramite = codigoTramite;
    }

    public int getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(int ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getEstadoNotificacion() {
        return estadoNotificacion;
    }

    public void setEstadoNotificacion(String estadoNotificacion) {
        this.estadoNotificacion = estadoNotificacion;
    }

    public String getFirma() {
        if (firma==null)return("");
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getNombreExpediente() {
        return nombreExpediente;
    }

    public void setNombreExpediente(String nombreExpediente) {
        this.nombreExpediente = nombreExpediente;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public int getOcurrenciaTramite() {
        return ocurrenciaTramite;
    }

    public void setOcurrenciaTramite(int ocurrenciaTramite) {
        this.ocurrenciaTramite = ocurrenciaTramite;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public String getTextoNotificacion() {
        if (textoNotificacion==null)return("");
        return textoNotificacion;
    }

    public void setTextoNotificacion(String textoNotificacion) {
        this.textoNotificacion = textoNotificacion;
    }

    public String getCodDepartamento() {
        return codDepartamento;
    }

    public void setCodDepartamento(String codDepartamento) {
        this.codDepartamento = codDepartamento;
    }

    public int getClaseProcedimiento() {
        return claseProcedimiento;
    }

    public void setClaseProcedimiento(int claseProcedimiento) {
        this.claseProcedimiento = claseProcedimiento;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    /**
     * @return the adjuntosExternos
     */
    public ArrayList<AdjuntoNotificacionVO> getAdjuntosExternos() {
        return adjuntosExternos;
    }

    /**
     * @param adjuntosExternos the adjuntosExternos to set
     */
    public void setAdjuntosExternos(ArrayList<AdjuntoNotificacionVO> adjuntosExternos) {
        this.adjuntosExternos = adjuntosExternos;
    }

    /**
     * @return the nombreTramite
     */
    public String getNombreTramite() {
        return nombreTramite;
    }

    /**
     * @param nombreTramite the nombreTramite to set
     */
    public void setNombreTramite(String nombreTramite) {
        this.nombreTramite = nombreTramite;
    }

    /**
     * @return the numeroRegistroTelematico
     */
    public String getNumeroRegistroTelematico() {
        return numeroRegistroTelematico;
    }

    /**
     * @param numeroRegistroTelematico the numeroRegistroTelematico to set
     */
    public void setNumeroRegistroTelematico(String numeroRegistroTelematico) {
        this.numeroRegistroTelematico = numeroRegistroTelematico;
    }

    /**
     * @return the fechaEnvio
     */
    public Calendar getFechaEnvio() {
        return fechaEnvio;
    }

    /**
     * @param fechaEnvio the fechaEnvio to set
     */
    public void setFechaEnvio(Calendar fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }


    public String getFechaEnvioAsString() {              
        return this.fechaEnvioAsString;
    }
    
    
    public void setFechaEnvioAsString(String fecha) {
        this.fechaEnvioAsString = fecha;
    }

    /**
     * @return the nombreEmisor
     */
    public String getNombreEmisor() {
        return nombreEmisor;
    }

    /**
     * @param nombreEmisor the nombreEmisor to set
     */
    public void setNombreEmisor(String nombreEmisor) {
        this.nombreEmisor = nombreEmisor;
    }

    /**
     * @return the idEmisor
     */
    public String getIdEmisor() {
        return idEmisor;
    }

    /**
     * @param idEmisor the idEmisor to set
     */
    public void setIdEmisor(String idEmisor) {
        this.idEmisor = idEmisor;
    }

    /**
     * @return the fechaAcuse
     */
    public Calendar getFechaAcuse() {
        return fechaAcuse;
    }

    /**
     * @param fechaAcuse the fechaAcuse to set
     */
    public void setFechaAcuse(Calendar fechaAcuse) {
        this.fechaAcuse = fechaAcuse;
    }

    /**
     * @return the fechaAcuseAsString
     */
    public String getFechaAcuseAsString() {
        return fechaAcuseAsString;
    }

    /**
     * @param fechaAcuseAsString the fechaAcuseAsString to set
     */
    public void setFechaAcuseAsString(String fechaAcuseAsString) {
        this.fechaAcuseAsString = fechaAcuseAsString;
    }

    /**
     * @return the resultado
     */
    public String getResultado() {
        return resultado;
    }

    /**
     * @param resultado the resultado to set
     */
    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
    public String getPersonaAcuseNombre() {
        return personaAcuseNombre;
    }

    public void setPersonaAcuseNombre(String personaAcuseNombre) {
        this.personaAcuseNombre = personaAcuseNombre;
    }

    public String getPersonaAcuseDNI() {
        return personaAcuseDNI;
    }

    public void setPersonaAcuseDNI(String personaAcuseDNI) {
        this.personaAcuseDNI = personaAcuseDNI;
    }

    public Calendar getFechaSolEnvio() {
        return fechaSolEnvio;
    }

    public void setFechaSolEnvio(Calendar fechaSolEnvio) {
        this.fechaSolEnvio = fechaSolEnvio;
    }

    public String getFechaSolEnvioAsString() {
        return fechaSolEnvioAsString;
    }

    public void setFechaSolEnvioAsString(String fechaSolEnvioAsString) {
        this.fechaSolEnvioAsString = fechaSolEnvioAsString;
    }

    

    public String getIdentificadorPlatea() {
        return identificadorPlatea;
    }

    public void setIdentificadorPlatea(String identificadorPlatea) {
        this.identificadorPlatea = identificadorPlatea;
    }
    
    

}
