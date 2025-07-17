package es.altia.agora.business.sge;

/**
 *
 * @author Tiffany
 */
public class FirmasDocumentoProcedimientoVO {
    
    private String codigo;
    private String codDocumento;
    private String orden;
    private String usuario;
    private String nomUsuario;
    private String municipio;
    private String procedimiento;
    private String cargo;
    private String nomCargo;
    private String uor;
    private String nomUor;
    private String finalizaRechazo;
    private String tramitar;

    public FirmasDocumentoProcedimientoVO() {
    }

    public String getNomCargo() {
        return nomCargo;
    }

    public void setNomCargo(String nomCargo) {
        this.nomCargo = nomCargo;
    }

    public String getNomUsuario() {
        return nomUsuario;
    }

    public void setNomUsuario(String nomUsuario) {
        this.nomUsuario = nomUsuario;
    }

    public String getNomUor() {
        return nomUor;
    }

    public void setNomUor(String nombrUor) {
        this.nomUor = nombrUor;
    }
    
    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getCodDocumento() {
        return codDocumento;
    }

    public void setCodDocumento(String codDocumento) {
        this.codDocumento = codDocumento;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    public String getUor() {
        return uor;
    }

    public void setUor(String uor) {
        this.uor = uor;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTramitar() {
        return tramitar;
    }
    
    public void setTramitar(String tramitar) {
        this.tramitar = tramitar;
    }


    public String getFinalizaRechazo() {
        return finalizaRechazo;
    }

    public void setFinalizaRechazo(String finalizaRechazo) {
        this.finalizaRechazo = finalizaRechazo;
    }
    


    public String toString (){
        return this.getUor()+"-"+this.getCargo()+"-"+this.getUsuario()+"-"+this.getOrden();
    }
    
    
}
