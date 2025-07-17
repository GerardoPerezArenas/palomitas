/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.historico.expediente.vo;

import es.altia.flexia.historico.expediente.vo.AdjuntoComunicacionVO;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author santiagoc
 * 
 * Tabla COMUNICACION
 */
public class ComunicacionVO
{
    private long idProceso; //Se corresponde con el campo ID_PROCESO.
    private int id; //Se corresponde con el campo ID
    private String asunto = null; //Se corresponde con el campo ASUNTO
    private String texto = null; //Se corresponde con el campo TEXTO
    private int tipoDocumento; //Se corresponde con el campo TIPO_DOCUMENTO
    private String documento = null; //Se corresponde con el campo DOCUMENTO
    private String nombre = null; //Se corresponde con el campo NOMBRE
    private Calendar fecha = null; //Se corresponde con el campo FECHA
    private String numRegistro = null; //Se corresponde con el campo NUM_REGISTRO
    private String origenRegistro = null; //Se corresponde con el campo ORIGEN_REGISTRO
    private String xmlComunicacion = null; //Se corresponde con el campo XML_COMUNICACION
    private String firma = null; //Se corresponde con el campo FIRMA
    private String plataformaFirma = null; //Se corresponde con el campo PLATAFORMA_FIRMA
    private int codOrganizacion; //Se corresponde con el campo COD_ORGANIZACION
    private int ejercicio; //Se corresponde con el campo EJERCICIO
    private String numExpediente = null; //Se corresponde con el campo NUM_EXPEDIENTE
    private Integer leida = null; //Se corresponde con el campo LEIDA
    private ArrayList<AdjuntoComunicacionVO> adjuntos = null; //Lista de adjuntos asociados a la notificación.
    
    public ComunicacionVO()
    {
        
    }

    public long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(long idProceso) {
        this.idProceso = idProceso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(int tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    public String getNumRegistro() {
        return numRegistro;
    }

    public void setNumRegistro(String numRegistro) {
        this.numRegistro = numRegistro;
    }

    public String getOrigenRegistro() {
        return origenRegistro;
    }

    public void setOrigenRegistro(String origenRegistro) {
        this.origenRegistro = origenRegistro;
    }

    public String getXmlComunicacion() {
        return xmlComunicacion;
    }

    public void setXmlComunicacion(String xmlComunicacion) {
        this.xmlComunicacion = xmlComunicacion;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getPlataformaFirma() {
        return plataformaFirma;
    }

    public void setPlataformaFirma(String plataformaFirma) {
        this.plataformaFirma = plataformaFirma;
    }

    public int getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(int codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    public int getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(int ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public Integer getLeida() {
        return leida;
    }

    public void setLeida(Integer leida) {
        this.leida = leida;
    }

    public ArrayList<AdjuntoComunicacionVO> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(ArrayList<AdjuntoComunicacionVO> adjuntos) {
        this.adjuntos = adjuntos;
    }
}
