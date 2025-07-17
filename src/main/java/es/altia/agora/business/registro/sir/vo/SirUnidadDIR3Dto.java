/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.registro.sir.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Visualizacion datos en pantalla resultados de busqueda al filtrar unidades
 * @author INGDGC
 */
public class SirUnidadDIR3Dto extends SirUnidadDIR3{
    
    private static final SimpleDateFormat formatDateddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    
    public SirUnidadDIR3Dto(String codigoUnidad, String nombreUnidad_ES, String nombreUnidad_EU, String codigoOficina, String codigoOrganismo, String codigoRaiz, String codigoNivelAdministrativo, String codigoComunidadAutonoma, String codigoProvincia, String codVisibleUorFlexia, Date fechaActivacion) {
        super(codigoUnidad, nombreUnidad_ES, nombreUnidad_EU, codigoOficina, codigoOrganismo, codigoRaiz, codigoNivelAdministrativo, codigoComunidadAutonoma, codigoProvincia, codVisibleUorFlexia,fechaActivacion);
    }
    
    private String oficina;
    private String organismo;
    private String raiz;
    private String nivelAdministrativo;
    private String comunidadAutonoma;
    private String provincia;
    private int numResultadosTotal;

    public String getOficina() {
        return oficina;
    }

    public void setOficina(String oficina) {
        this.oficina = oficina;
    }

    public String getOrganismo() {
        return organismo;
    }

    public void setOrganismo(String organismo) {
        this.organismo = organismo;
    }

    public String getRaiz() {
        return raiz;
    }

    public void setRaiz(String raiz) {
        this.raiz = raiz;
    }

    public String getNivelAdministrativo() {
        return nivelAdministrativo;
    }

    public void setNivelAdministrativo(String nivelAdministrativo) {
        this.nivelAdministrativo = nivelAdministrativo;
    }

    public String getComunidadAutonoma() {
        return comunidadAutonoma;
    }

    public void setComunidadAutonoma(String comunidadAutonoma) {
        this.comunidadAutonoma = comunidadAutonoma;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public int getNumResultadosTotal() {
        return numResultadosTotal;
    }

    public void setNumResultadosTotal(int numResultadosTotal) {
        this.numResultadosTotal = numResultadosTotal;
    }

    @Override
    public String toString() {
        return "SirUnidadDIR3Dto{" 
                + "SirUnidadDIR3 = " + super.toString()
                + " oficina=" + oficina + ", organismo=" + organismo + ", raiz=" + raiz + ", nivelAdministrativo=" + nivelAdministrativo + ", comunidadAutonoma=" + comunidadAutonoma + ", provincia=" + provincia + ", numResultadosTotal=" + numResultadosTotal
                + '}'
                ;
    }
        
}
