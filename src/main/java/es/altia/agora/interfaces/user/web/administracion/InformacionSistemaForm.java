package es.altia.agora.interfaces.user.web.administracion;


import java.util.Vector;
import org.apache.struts.action.ActionForm;



/** Clase utilizada para capturar o mostrar el estado de una Tramitacion */
public class InformacionSistemaForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    private String pConfigParam = null;
    private String serverNodeLocalName = null;
    private String serverNodeLocalIP = null;
    private String osName = null;
    private String osVersion = null;
    private String cpuInfo = null;
    private String VMVersion = null;
    private String VMVendor = null;
    private String VMModeInfo = null;
    private String JVMFreeMemory = null;
    private String JVMTotalMemory = null;
    private String JVMUsedMemory = null;
    private String serverInfo = null;
    private String upTime = null;    
    private String rutaFicheroLog = null;
    private String webAppBaseDir = null;
    private String hostVirtual = null;
    private String jndi = null;
    private String gestor = null;
    private String version = null;
    private String operacion=null;
    private String nivelLog=null;
    private String salidaLog=null;
    private String sql=null;
    private Vector resultado=null;
    private String resultadoString="";
    private String resultadoStringExp="";
    private boolean activarConsultaADMG=false;
    
    Vector listaOrganizaciones = new Vector();




    public String getpConfigParam() {
        return pConfigParam;
    }

    public void setpConfigParam(String pConfigParam) {
        this.pConfigParam = pConfigParam;
    }

    public String getServerNodeLocalIP() {
        return serverNodeLocalIP;
    }

    public void setServerNodeLocalIP(String serverNodeLocalIP) {
        this.serverNodeLocalIP = serverNodeLocalIP;
    }

    public String getServerNodeLocalName() {
        return serverNodeLocalName;
    }

    public void setServerNodeLocalName(String serverNodeLocalName) {
        this.serverNodeLocalName = serverNodeLocalName;
    }

    public String getCpuInfo() {
        return cpuInfo;
    }

    public void setCpuInfo(String cpuInfo) {
        this.cpuInfo = cpuInfo;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getVMModeInfo() {
        return VMModeInfo;
    }

    public void setVMModeInfo(String vMModeInfo) {
        this.VMModeInfo = vMModeInfo;
    }

    public String getVMVendor() {
        return VMVendor;
    }

    public void setVMVendor(String vMVendor) {
        this.VMVendor = vMVendor;
    }

    public String getVMVersion() {
        return VMVersion;
    }

    public void setVMVersion(String vMVersion) {
        this.VMVersion = vMVersion;
    }

    public String getJVMFreeMemory() {
        return JVMFreeMemory;
    }

    public void setJVMFreeMemory(String JVMFreeMemory) {
        this.JVMFreeMemory = JVMFreeMemory;
    }

    public String getJVMTotalMemory() {
        return JVMTotalMemory;
    }

    public void setJVMTotalMemory(String JVMTotalMemory) {
        this.JVMTotalMemory = JVMTotalMemory;
    }

    public String getJVMUsedMemory() {
        return JVMUsedMemory;
    }

    public void setJVMUsedMemory(String JVMUsedMemory) {
        this.JVMUsedMemory = JVMUsedMemory;
    }

    public String getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(String serverInfo) {
        this.serverInfo = serverInfo;
    }
    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    public String getRutaFicheroLog() {
        return rutaFicheroLog;
    }

    public void setRutaFicheroLog(String rutaFicheroLog) {
        this.rutaFicheroLog = rutaFicheroLog;
    }

    public String getGestor() {
        return gestor;
    }

    public void setGestor(String gestor) {
        this.gestor = gestor;
    }

    public String getHostVirtual() {
        return hostVirtual;
    }

    public void setHostVirtual(String hostVirtual) {
        this.hostVirtual = hostVirtual;
    }

    public String getJndi() {
        return jndi;
    }

    public void setJndi(String jndi) {
        this.jndi = jndi;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getWebAppBaseDir() {
        return webAppBaseDir;
    }

    public void setWebAppBaseDir(String webAppBaseDir) {
        this.webAppBaseDir = webAppBaseDir;
    }

    public String getOperacion() {
        return operacion; 
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public String getNivelLog() {
        return nivelLog;
    }

    public void setNivelLog(String nivelLog) {
        this.nivelLog = nivelLog;
    }

    public String getSalidaLog() {
        return salidaLog;
    }

    public void setSalidaLog(String salidaLog) {
        this.salidaLog = salidaLog;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Vector getResultado() {
        return resultado;
    }

    public void setResultado(Vector resultado) {
        this.resultado = resultado;
    }
    
    

    public Vector getListaOrganizaciones() {
        return listaOrganizaciones;
    }

    public void setListaOrganizaciones(Vector listaOrganizaciones) {
        this.listaOrganizaciones = listaOrganizaciones;
    }

    public String getResultadoString() {
        return resultadoString;
    }

    public void setResultadoString(String resultadoString) {
        this.resultadoString = resultadoString;
    }
    
        public String getResultadoStringExp() {
        return resultadoStringExp;
    }

    public void setResultadoStringExp(String resultadoStringExp) {
        this.resultadoStringExp = resultadoStringExp;
    }
     
    public boolean getActivarConsultaADMG() {
        return activarConsultaADMG;
    }

    public void setActivarConsultaADMG(boolean activarConsultaADMG) {
        this.activarConsultaADMG = activarConsultaADMG;
    }

    





}
