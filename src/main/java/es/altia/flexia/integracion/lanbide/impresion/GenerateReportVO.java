/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.lanbide.impresion;

import java.util.ArrayList;

/**
 * Clase VO para generar el XML del report
 * 
 * @author david.caamano
 * @version 15/10/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 15-10-2012 * </li>
 * </ol> 
 */
public class GenerateReportVO {
    
    private ArrayList<InteresadoGenerateReportVO> interesados;

    public ArrayList<InteresadoGenerateReportVO> getInteresados() {
        return interesados;
    }//getInteresados
    public void setInteresados(ArrayList<InteresadoGenerateReportVO> interesados) {
        this.interesados = interesados;
    }//setInteresados
    
}