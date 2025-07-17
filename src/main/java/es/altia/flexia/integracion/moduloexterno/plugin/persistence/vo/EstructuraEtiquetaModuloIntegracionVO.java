package es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo;

import org.apache.log4j.Logger;

public class EstructuraEtiquetaModuloIntegracionVO {
    
    private String codigoEtiqueta; //código de la etiqueta, siempre ha de empezar por EXPEDIENTE_
    private Integer tipoEtiqueta;  //representa el tipo del campo (1->numérico, 2->texto, 3->fecha, 4->listas(varios datos))
    private String nombreEtiqueta; // nombre de la etiqueta, ha de ser similar al código, pero sin el prefijo EXPEDIENTE_ 
    private String nombreColumna; //sólo, permitimos una columna en la consulta, es el nombre de la columna del SELECT
    private String sqlS; //contiene parte de la consulta Sql, asociada para recuperar el valor de la etiqueta (sólo SELECT Y FROM)
    private String  whereS; //Contiene la primera parte de la cláusula WHERE (ha de filtrar por numeroExpediente, codigoTramite u ocurrencia Tramite)
    private String and1; //Primera clausula AND, puede tener o no valor (ha de filtrar por numeroExpediente, codigoTramite u ocurrenciaTramite)
    private String and2; //Segunda clausula AND, puede tener o no valor (ha de filtrar por numeroExpediente, codigoTramite u ocurrenciaTramite)
    private String and3; //Tercera clausula AND, es libre, para joins.
    private String campoWhere;
    private String campoAnd1;
    private String campoAnd2;
    private String formatoFecha; //Si la etiqueta es de tipo fecha, querremos saber en que formato hemos de mostrarla
    private String formatoNumero;//Si la etiquet es de tipo número, querremos saber en que formato hemos de mostrarla
    // El logger debe ser static para convertir el objeto a json
    private static Logger log = Logger.getLogger(EstructuraEtiquetaModuloIntegracionVO.class);

    public String getCodigoEtiqueta() {
        return codigoEtiqueta;
    }

    public void setCodigoEtiqueta(String codigoEtiqueta) {
        this.codigoEtiqueta = codigoEtiqueta;
    }

    public Integer getTipoEtiqueta() {
        return tipoEtiqueta;
    }

    public void setTipoEtiqueta(Integer tipoEtiqueta) {
        this.tipoEtiqueta = tipoEtiqueta;
    }

    public String getNombreEtiqueta() {
        return nombreEtiqueta;
    }

    public void setNombreEtiqueta(String nombreEtiqueta) {
        this.nombreEtiqueta = nombreEtiqueta;
    }

    public String getNombreColumna() {
        return nombreColumna;
    }

    public void setNombreColumna(String nombreColumna) {
        this.nombreColumna = nombreColumna;
    }

    public String getSqlS() {
        return sqlS;
    }

    public void setSqlS(String sqlS) {
        this.sqlS = sqlS;
    }

    public String getWhereS() {
        return whereS;
    }

    public void setWhereS(String whereS) {
        this.whereS = whereS;
    }

    public String getAnd1() {
        return and1;
    }

    public void setAnd1(String and1) {
        this.and1 = and1;
    }

    public String getAnd2() {
        return and2;
    }

    public void setAnd2(String and2) {
        this.and2 = and2;
    }

    public String getAnd3() {
        return and3;
    }

    public void setAnd3(String and3) {
        this.and3 = and3;
    }

    public Logger getLog() {
        return log;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public String getCampoWhere() {
        return campoWhere;
    }

    public void setCampoWhere(String campoWhere) {
        this.campoWhere = campoWhere;
    }

    public String getCampoAnd1() {
        return campoAnd1;
    }

    public void setCampoAnd1(String campoAnd1) {
        this.campoAnd1 = campoAnd1;
    }

    public String getCampoAnd2() {
        return campoAnd2;
    }

    public void setCampoAnd2(String campoAnd2) {
        this.campoAnd2 = campoAnd2;
    }

    public String getFormatoFecha() {
        return formatoFecha;
    }

    public void setFormatoFecha(String formatoFecha) {
        this.formatoFecha = formatoFecha;
    }

    public String getFormatoNumero() {
        return formatoNumero;
    }

    public void setFormatoNumero(String formatoNumero) {
        this.formatoNumero = formatoNumero;
    }
    
    
  
    
    public String toString() {
        String sc = "\n"; // salto de carro

        StringBuffer resultado = new StringBuffer("");

        resultado.append("codigoEtiqueta: " + getCodigoEtiqueta() + sc);
        resultado.append("tipoEtiqueta: " + getTipoEtiqueta() + sc);
        resultado.append("formatoFecha:"+ getFormatoFecha()+ sc);
        resultado.append("formatoNumero:"+ getFormatoNumero()+ sc);
        resultado.append("nombreEtiqueta: " + getNombreEtiqueta() + sc);
        resultado.append("nombrecolumna: " + getNombreColumna() + sc);
        resultado.append("Sql: " + getSqlS() + sc);
        resultado.append("Where: "+ getWhereS() + sc);
        resultado.append("Campo Where: "+ getCampoWhere() + sc);
        resultado.append("And1: "+ getAnd1() + sc);
        resultado.append("Campo And1: " +getCampoAnd1()+sc);
        resultado.append("And2: "+ getAnd2() + sc);
        resultado.append("CampoAnd2: " +getCampoAnd2()+sc);
        resultado.append("And3: " +getAnd3()+sc);
        
        return resultado.toString();
    }

   
}