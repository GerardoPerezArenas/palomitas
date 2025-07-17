package es.altia.agora.business.gestionInformes;
import java.io.Serializable;
import java.util.Vector;

public class InformeValueObject implements Serializable {

    private String codPlantilla;
    private String titulo;
    private String procedimiento;
    private String fecha;
    private Vector criterios;
    private String publicado;
    private String papel;
    private String orientacion;
    private String margenSuperior;
    private String margenInferior;
    private String margenIzquierdo;
    private String margenDerecho;
    private Vector vectorCabInformeVO;
    private Vector vectorCabPaginaInformeVO;
    private Vector vectorCuerpoInformeVO;
    private Vector vectorPiePaginaInformeVO;
    private Vector vectorPieInformeVO;
    private Vector vectorPermisos;
    private String ambito;

    public InformeValueObject(){
        super();
    }

    public InformeValueObject(String codPlantilla, String titulo, String procedimiento, String fecha, Vector criterios,
                            String publicado, String papel, String orientacion, String margenSuperior,
                            String margenInferior, String margenIzquierdo, String margenDerecho,
                            Vector vectorCabInformeVO, Vector vectorCabPaginaInformeVO,
                            Vector vectorCuerpoInformeVO, Vector vectorPiePaginaInformeVO,
                            Vector vectorPieInformeVO){
        this.codPlantilla = codPlantilla;
        this.titulo = titulo;
        this.procedimiento = procedimiento;
        this.fecha = fecha;
        this.criterios = criterios;
        this.publicado = publicado;
        this.papel = papel;
        this.orientacion = orientacion;
        this.margenSuperior = margenSuperior;
        this.margenInferior = margenInferior;
        this.margenIzquierdo = margenIzquierdo;
        this.margenDerecho = margenDerecho;
        this.vectorCabInformeVO = vectorCabInformeVO;
        this.vectorCabPaginaInformeVO = vectorCabPaginaInformeVO;
        this.vectorCuerpoInformeVO = vectorCuerpoInformeVO;
        this.vectorPiePaginaInformeVO = vectorPiePaginaInformeVO;
        this.vectorPieInformeVO = vectorPieInformeVO;
    }

    public Vector getVectorPermisos() {
        return vectorPermisos;
    }

    public void setVectorPermisos(Vector vectorPermisos) {
        this.vectorPermisos = vectorPermisos;
    }

    public String getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(String codPlantilla) {
        this.codPlantilla = codPlantilla;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Vector getCriterios() {
        return criterios;
    }

    public void setCriterios(Vector criterios) {
        this.criterios = criterios;
    }

    public String getPublicado() {
        return publicado;
    }

    public void setPublicado(String publicado) {
        this.publicado = publicado;
    }

    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = papel;
    }

    public String getOrientacion() {
        return orientacion;
    }

    public void setOrientacion(String orientacion) {
        this.orientacion = orientacion;
    }

    public String getMargenSuperior() {
        return margenSuperior;
    }

    public void setMargenSuperior(String margenSuperior) {
        this.margenSuperior = margenSuperior;
    }

    public String getMargenInferior() {
        return margenInferior;
    }

    public void setMargenInferior(String margenInferior) {
        this.margenInferior = margenInferior;
    }

    public String getMargenIzquierdo() {
        return margenIzquierdo;
    }

    public void setMargenIzquierdo(String margenIzquierdo) {
        this.margenIzquierdo = margenIzquierdo;
    }

    public String getMargenDerecho() {
        return margenDerecho;
    }

    public void setMargenDerecho(String margenDerecho) {
        this.margenDerecho = margenDerecho;
    }

    public Vector getVectorCabInformeVO() {
        return vectorCabInformeVO;
    }

    public void setVectorCabInformeVO(Vector vectorcabInfomeVO) {
        this.vectorCabInformeVO = vectorcabInfomeVO;
    }

    public Vector getVectorCabPaginaInformeVO() {
        return vectorCabPaginaInformeVO;
    }

    public void setVectorCabPaginaInformeVO(Vector vectorcabPaginaInformeVO) {
        this.vectorCabPaginaInformeVO = vectorcabPaginaInformeVO;
    }

    public Vector getVectorCuerpoInformeVO() {
        return vectorCuerpoInformeVO;
    }

    public void setVectorCuerpoInformeVO(Vector vectorCuerpoInformeVO) {
        this.vectorCuerpoInformeVO = vectorCuerpoInformeVO;
    }

    public Vector getVectorPiePaginaInformeVO() {
        return vectorPiePaginaInformeVO;
    }

    public void setVectorPiePaginaInformeVO(Vector vectorPiePaginaInformeVO) {
        this.vectorPiePaginaInformeVO = vectorPiePaginaInformeVO;
    }

    public Vector getVectorPieInformeVO() {
        return vectorPieInformeVO;
    }

    public void setVectorPieInformeVO(Vector vectorPieInformeVO) {
        this.vectorPieInformeVO = vectorPieInformeVO;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public String toString() {
        String salida = "\n\n| codPlantilla = " + codPlantilla + " |\n" +
            "| titulo = " + titulo + " |\n" +
            "| criterios = " + criterios + " |\n" +
            "| papel = " + papel + " |\n" +
            "| orientacion = " + orientacion + " |\n" +
            "| margenSuperior = " + margenSuperior + " |\n" +
            "| margenInferior = " + margenInferior + " |\n" +
            "| margenIzquierdo = " + margenIzquierdo + " |\n" +
            "| margenDerecho = " + margenDerecho + " |\n\n" +
            "CABINFORME VO = \n";
        for (int i=0;i<vectorCabInformeVO.size();i++) {
            salida = salida.concat("\n"+vectorCabInformeVO.get(i).toString());
        }
        salida = salida.concat("\n CABPAGINAINFORME VO = \n");
        for (int i=0;i<vectorCabPaginaInformeVO.size();i++) {
            salida = salida.concat("\n"+vectorCabPaginaInformeVO.get(i).toString());
        }
        salida = salida.concat("\n CUERPOINFORME VO = \n");
        for (int i=0;i<vectorCuerpoInformeVO.size();i++) {
            salida = salida.concat(vectorCuerpoInformeVO.get(i).toString());
        }
        salida = salida.concat("\n PIEPAGINAINFORME VO = \n");
        for (int i=0;i<vectorPiePaginaInformeVO.size();i++) {
            salida = salida.concat("\n"+vectorPiePaginaInformeVO.get(i).toString());
        }
        salida = salida.concat("\n PIEINFORME VO = \n");
        for (int i=0;i<vectorPieInformeVO.size();i++) {
            salida = salida.concat("\n"+vectorPieInformeVO.get(i).toString());
        }
        return salida;
    }

}
