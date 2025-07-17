/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.registro.sir.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * BD: SIR_DESTINO_R_RES
 * @author INGDGC
 */
public class SirDestinoRRes {
    
    private static final SimpleDateFormat formatDateddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    
    private int RES_DEP; //     NUMBER(3)   
    private int RES_UOR; //    NUMBER(5)  
    private String RES_TIP; //     VARCHAR2(1) 
    private int RES_EJE; //    NUMBER(4)
    private int RES_NUM; //    NUMBER(6) 
    private String codigoUnidad; //    VARCHAR2 ( 100 BYTE) 
    private String oficinaRegistroSIR; //    VARCHAR2 ( 100 BYTE) 
    private String numeroRegistroSIR; //    VARCHAR2 ( 100 BYTE) 
    private String usuarioRegistroSIR; //    VARCHAR2 ( 100 BYTE) 
    private Date fechaRegistroSIR; //    DATE
    private Date fechaRegistroSistemaSIR; //    DATE
    private Date fechaRegistro; // date DEFAULT SYSDATE  

    public SirDestinoRRes() {}

    public SirDestinoRRes(int RES_DEP, int RES_UOR, String RES_TIP, int RES_EJE, int RES_NUM, String codigoUnidad, String oficinaRegistroSIR, String numeroRegistroSIR, String usuarioRegistroSIR, Date fechaRegistroSIR, Date fechaRegistroSistemaSIR, Date fechaRegistro) {
        this.RES_DEP = RES_DEP;
        this.RES_UOR = RES_UOR;
        this.RES_TIP = RES_TIP;
        this.RES_EJE = RES_EJE;
        this.RES_NUM = RES_NUM;
        this.codigoUnidad = codigoUnidad;
        this.oficinaRegistroSIR = oficinaRegistroSIR;
        this.numeroRegistroSIR = numeroRegistroSIR;
        this.usuarioRegistroSIR = usuarioRegistroSIR;
        this.fechaRegistroSIR = fechaRegistroSIR;
        this.fechaRegistroSistemaSIR = fechaRegistroSistemaSIR;
        this.fechaRegistro = fechaRegistro;
    }
    
    

    public SirDestinoRRes(int RES_DEP, int RES_UOR, String RES_TIP, int RES_EJE, int RES_NUM) {
        this.RES_DEP = RES_DEP;
        this.RES_UOR = RES_UOR;
        this.RES_TIP = RES_TIP;
        this.RES_EJE = RES_EJE;
        this.RES_NUM = RES_NUM;
    }

    public int getRES_DEP() {
        return RES_DEP;
    }

    public void setRES_DEP(int RES_DEP) {
        this.RES_DEP = RES_DEP;
    }

    public int getRES_UOR() {
        return RES_UOR;
    }

    public void setRES_UOR(int RES_UOR) {
        this.RES_UOR = RES_UOR;
    }

    public String getRES_TIP() {
        return RES_TIP;
    }

    public void setRES_TIP(String RES_TIP) {
        this.RES_TIP = RES_TIP;
    }

    public int getRES_EJE() {
        return RES_EJE;
    }

    public void setRES_EJE(int RES_EJE) {
        this.RES_EJE = RES_EJE;
    }

    public int getRES_NUM() {
        return RES_NUM;
    }

    public void setRES_NUM(int RES_NUM) {
        this.RES_NUM = RES_NUM;
    }

    public String getCodigoUnidad() {
        return codigoUnidad;
    }

    public void setCodigoUnidad(String codigoUnidad) {
        this.codigoUnidad = codigoUnidad;
    }

    public String getOficinaRegistroSIR() {
        return oficinaRegistroSIR;
    }

    public void setOficinaRegistroSIR(String oficinaRegistroSIR) {
        this.oficinaRegistroSIR = oficinaRegistroSIR;
    }

    public String getNumeroRegistroSIR() {
        return numeroRegistroSIR;
    }

    public void setNumeroRegistroSIR(String numeroRegistroSIR) {
        this.numeroRegistroSIR = numeroRegistroSIR;
    }

    public String getUsuarioRegistroSIR() {
        return usuarioRegistroSIR;
    }

    public void setUsuarioRegistroSIR(String usuarioRegistroSIR) {
        this.usuarioRegistroSIR = usuarioRegistroSIR;
    }

    public Date getFechaRegistroSIR() {
        return fechaRegistroSIR;
    }

    public void setFechaRegistroSIR(Date fechaRegistroSIR) {
        this.fechaRegistroSIR = fechaRegistroSIR;
    }

    public Date getFechaRegistroSistemaSIR() {
        return fechaRegistroSistemaSIR;
    }

    public void setFechaRegistroSistemaSIR(Date fechaRegistroSistemaSIR) {
        this.fechaRegistroSistemaSIR = fechaRegistroSistemaSIR;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    
    @Override
    public String toString() {
        return "SirDestinoRRes{" + "RES_DEP=" + RES_DEP + ", RES_UOR=" + RES_UOR + ", RES_TIP=" + RES_TIP + ", RES_EJE=" + RES_EJE + ", RES_NUM=" + RES_NUM + ", codigoUnidad=" + codigoUnidad + ", oficinaRegistroSIR=" + oficinaRegistroSIR + ", numeroRegistroSIR=" + numeroRegistroSIR + ", usuarioRegistroSIR=" + usuarioRegistroSIR + ", fechaRegistroSIR=" + (fechaRegistroSIR!=null?formatDateddMMyyyy.format(fechaRegistroSIR):"") + ", fechaRegistroSistemaSIR=" + (fechaRegistroSistemaSIR!=null?formatDateddMMyyyy.format(fechaRegistroSistemaSIR):"") + ", fechaRegistro=" + (fechaRegistro!=null?formatDateddMMyyyy.format(fechaRegistro):"") + '}';
    }

    
    
}
