package es.altia.agora.business.sge;

import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.EstructuraCampoAgrupado;
import java.util.ArrayList;

public class ResultadoImportacionBibliotecaVO {
  private int estado;  
  private ArrayList<DefinicionAgrupacionCamposValueObject> agrupCamposConflicto;
  private ArrayList<EstructuraCampoAgrupado> camposSuplConflicto;

    public ResultadoImportacionBibliotecaVO() {
    }

    /**
     * @return the estado
     */
    public int getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(int estado) {
        this.estado = estado;
    }

    /**
     * @return the agrupCamposConflicto
     */
    public ArrayList<DefinicionAgrupacionCamposValueObject> getAgrupCamposConflicto() {
        return agrupCamposConflicto;
    }

    /**
     * @param agrupCamposConflicto the agrupCamposConflicto to set
     */
    public void setAgrupCamposConflicto(ArrayList<DefinicionAgrupacionCamposValueObject> agrupCamposConflicto) {
        this.agrupCamposConflicto = agrupCamposConflicto;
    }

    /**
     * @return the camposSuplConflicto
     */
    public ArrayList<EstructuraCampoAgrupado> getCamposSuplConflicto() {
        return camposSuplConflicto;
    }

    /**
     * @param camposSuplConflicto the camposSuplConflicto to set
     */
    public void setCamposSuplConflicto(ArrayList<EstructuraCampoAgrupado> camposSuplConflicto) {
        this.camposSuplConflicto = camposSuplConflicto;
    }
    
    /**
     *
     * @param codigo
     * @return La etiqueta del error en la bbdd seg�n el c�digo
     */
    public static String getEtiquetaError(int codigo){
        String etiqueta;
        
        switch(codigo){
            case 10: // No se ha podido obtener conexi�n a la BBDD
            case 20: // Ha ocurrido un error al recuperar los campos suplementarios del procedimiento biblioteca
            case 30: // Ha ocurrido un error al grabar agrupaciones de campos suplementarios en el procedimiento destino
            case 31: // Ha ocurrido un error al comprobar la existencia en el procedimiento destino de los c�digos de las agrupaciones a importar
            case 40: // Hay c�digos de agrupaciones de la biblioteca que ya existen en el procedimiento destino
            case 50: // Ha ocurrido un error al comprobar la existencia en el procedimiento destino de los c�digos de los campos suplementarios a importar
            case 60: // Hay c�digos de campo suplementario de la biblioteca que ya existen en el procedimiento destino
            case 70: // Ha ocurrido un error al grabar campos suplementarios  en el procedimiento destino
            case 80: // Ha ocurrido un error al recuperar el flujo de tramitaci�n a importar
            case 81: // Ha ocurrido un error al recuperar datos generales de un tr�mite a importar
            case 82: // Ha ocurrido un error al recuperar datos de documentos de un tr�mite a importar
            case 83: // Ha ocurrido un error al recuperar datos de enlaces de un tr�mite a importar
            case 84: // Ha ocurrido un error al recuperar datos de condiciones de entrada de un tr�mite a importar
            case 85: // Ha ocurrido un error al recuperar datos de condiciones de salida de un tr�mite a importar
            case 86: // Ha ocurrido un error al recuperar datos de campos suplementarios de un tr�mite a importar
            case 87: // Ha ocurrido un error al recuperar datos de integraciones con servicios web de un tr�mite a importar
            case 90: // Ha ocurrido un error al recuperar los c�digos de tr�mite en el procedimiento destino
            case 91: // Ha ocurrido un error al intentar grabar en el procedimiento destino datos generales de un tr�mite importado
            case 92: // Ha ocurrido un error al intentar grabar en el procedimiento destino datos de documentos de un tr�mite importado
            case 93: // Ha ocurrido un error al intentar grabar en el procedimiento destino datos de enlaces de un tr�mite importado
            case 94: // Ha ocurrido un error al intentar grabar en el procedimiento destino datos de condiciones de entrada de un tr�mite importado
            case 95: // Ha ocurrido un error al intentar grabar en el procedimiento destino datos de condiciones de salida de un tr�mite importado
            case 96: // Ha ocurrido un error al intentar grabar en el procedimiento destino datos de campos suplementarios de un tr�mite importado
            case 97: // Ha ocurrido un error al intentar grabar en el procedimiento destino datos de integraciones con servicios web de un tr�mite importado
            case 981: // Ha ocurrido un error al grabar la correspondencia de c�digos de los tr�mites importados y de sus procedimientos
            case 982: // Ha ocurrido un error al recuperar el c�digo asignado a un tr�mite importado en el procedimiento destino
            case 99: // Ha ocurrido un error al intentar grabar el flujo de tramitaci�n recuperado de la biblioteca en el procedimiento destino
                etiqueta = "msgFalloImpFlujo"+codigo;
                break;
            default:
                etiqueta = "";
                break;
        }
        
        return etiqueta;
    }
}
