package es.altia.agora.webservice.registro.pisa.regexterno.controller;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author ivan.perez
 */
public class AnotacionRegistroExternoForm extends ActionForm {
    private String numeroAnotacion;
    private String ejercicioAnotacion;
    private String fechaAnotacion; //fechaEntrada
    private String horaMinAnotacion; //horaEntrada
    private String fechaDocumento; //fechaPresentacion
    private String horaPresentacion;
    private String asunto;
    private String codUor; //codigoUnidad;
    private String descUor; //nombreUnidad;
    private String codTipoDoc; //codigoTipoDocumento;
    private String descTipoDoc; //nombreTipoDocumento;
    private String txtDNI; //documento;
    private String txtInteresado; //nombreCompleto;
    private String nombre;
    private String txtPart; //particula
    private String txtApell1; //primerApellido;
    private String txtPart2; //particula
    private String txtApell2; //segundoApellido;
    private String codigoMunicipio;
    private String txtMuni; //nombreMunicipio;
    private String txtTelefono; //telefono;
    private String txtCorreo; //email;
    private String codigoProvincia;
    private String txtProv; //nombreProvincia;
    private String txtDomicilio; //domicilio;
    private String txtCP; //codigoPostal;        
    
    private String cod_intUOR;
    private String codProcedimiento;
    private String descProcedimiento;
    private String txtExp1; //Expediente relacionado
    private String txtPais; //pais
    private String txtPoblacion; //poblacion

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCodTipoDoc() {
        return codTipoDoc;
    }

    public void setCodTipoDoc(String cbTipoDoc) {
        this.codTipoDoc = cbTipoDoc;
    }

    public String getCodUor() {
        return codUor;
    }

    public void setCodUor(String codUor) {
        this.codUor = codUor;
    }

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    public String getDescProcedimiento() {
        return descProcedimiento;
    }

    public void setDescProcedimiento(String descProcedimiento) {
        this.descProcedimiento = descProcedimiento;
    }

    public String getCod_intUOR() {
        return cod_intUOR;
    }

    public void setCod_intUOR(String cod_intUOR) {
        this.cod_intUOR = cod_intUOR;
    }






    public String getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(String codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public String getCodigoProvincia() {
        return codigoProvincia;
    }

    public void setCodigoProvincia(String codigoProvincia) {
        this.codigoProvincia = codigoProvincia;
    }

    public String getDescTipoDoc() {
        return descTipoDoc;
    }

    public void setDescTipoDoc(String descTipoDoc) {
        this.descTipoDoc = descTipoDoc;
    }


    public String getDescUor() {
        return descUor;
    }

    public void setDescUor(String descUor) {
        this.descUor = descUor;
    }


    public String getEjercicioAnotacion() {
        return ejercicioAnotacion;
    }

    public void setEjercicioAnotacion(String ejercicioAnotacion) {
        this.ejercicioAnotacion = ejercicioAnotacion;
    }

    public String getFechaAnotacion() {
        return fechaAnotacion;
    }

    public void setFechaAnotacion(String fechaAnotacion) {
        this.fechaAnotacion = fechaAnotacion;
    }

    public String getFechaDocumento() {
        return fechaDocumento;
    }

    public void setFechaDocumento(String fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }

    public String getHoraMinAnotacion() {
        return horaMinAnotacion;
    }

    public void setHoraMinAnotacion(String horaMinAnotacion) {
        this.horaMinAnotacion = horaMinAnotacion;
    }

    public String getHoraPresentacion() {
        return horaPresentacion;
    }

    public void setHoraPresentacion(String horaPresentacion) {
        this.horaPresentacion = horaPresentacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroAnotacion() {
        return numeroAnotacion;
    }

    public void setNumeroAnotacion(String numeroAnotacion) {
        this.numeroAnotacion = numeroAnotacion;
    }

    public String getTxtApell1() {
        return txtApell1;
    }

    public void setTxtApell1(String txtApell1) {
        this.txtApell1 = txtApell1;
    }

    public String getTxtApell2() {
        return txtApell2;
    }

    public void setTxtApell2(String txtApell2) {
        this.txtApell2 = txtApell2;
    }

    public String getTxtCP() {
        return txtCP;
    }

    public void setTxtCP(String txtCP) {
        this.txtCP = txtCP;
    }

    public String getTxtCorreo() {
        return txtCorreo;
    }

    public void setTxtCorreo(String txtCorreo) {
        this.txtCorreo = txtCorreo;
    }

    public String getTxtDNI() {
        return txtDNI;
    }

    public void setTxtDNI(String txtDNI) {
        this.txtDNI = txtDNI;
    }

    public String getTxtDomicilio() {
        return txtDomicilio;
    }

    public void setTxtDomicilio(String txtDomicilio) {
        this.txtDomicilio = txtDomicilio;
    }

    public String getTxtExp1() {
        return txtExp1;
    }

    public void setTxtExp1(String txtExp1) {
        this.txtExp1 = txtExp1;
    }

    public String getTxtInteresado() {
        return txtInteresado;
    }

    public void setTxtInteresado(String txtInteresado) {
        this.txtInteresado = txtInteresado;
    }

    public String getTxtMuni() {
        return txtMuni;
    }

    public void setTxtMuni(String txtMuni) {
        this.txtMuni = txtMuni;
    }

    public String getTxtPais() {
        return txtPais;
    }

    public void setTxtPais(String txtPais) {
        this.txtPais = txtPais;
    }

    public String getTxtPart() {
        return txtPart;
    }

    public void setTxtPart(String txtPart) {
        this.txtPart = txtPart;
    }

    public String getTxtPart2() {
        return txtPart2;
    }

    public void setTxtPart2(String txtPart2) {
        this.txtPart2 = txtPart2;
    }

    public String getTxtPoblacion() {
        return txtPoblacion;
    }

    public void setTxtPoblacion(String txtPoblacion) {
        this.txtPoblacion = txtPoblacion;
    }

    public String getTxtProv() {
        return txtProv;
    }

    public void setTxtProv(String txtProv) {
        this.txtProv = txtProv;
    }

    public String getTxtTelefono() {
        return txtTelefono;
    }

    public void setTxtTelefono(String txtTelefono) {
        this.txtTelefono = txtTelefono;
    }

    
}
