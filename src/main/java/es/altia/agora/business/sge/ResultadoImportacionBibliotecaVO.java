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
     * @return La etiqueta del error en la bbdd según el código
     */
    public static String getEtiquetaError(int codigo){
        String etiqueta;
        
        switch(codigo){
            case 10: // No se ha podido obtener conexión a la BBDD
            case 20: // Ha ocurrido un error al recuperar los campos suplementarios del procedimiento biblioteca
            case 30: // Ha ocurrido un error al grabar agrupaciones de campos suplementarios en el procedimiento destino
            case 31: // Ha ocurrido un error al comprobar la existencia en el procedimiento destino de los códigos de las agrupaciones a importar
            case 40: // Hay códigos de agrupaciones de la biblioteca que ya existen en el procedimiento destino
            case 50: // Ha ocurrido un error al comprobar la existencia en el procedimiento destino de los códigos de los campos suplementarios a importar
            case 60: // Hay códigos de campo suplementario de la biblioteca que ya existen en el procedimiento destino
            case 70: // Ha ocurrido un error al grabar campos suplementarios  en el procedimiento destino
            case 80: // Ha ocurrido un error al recuperar el flujo de tramitación a importar
            case 81: // Ha ocurrido un error al recuperar datos generales de un trámite a importar
            case 82: // Ha ocurrido un error al recuperar datos de documentos de un trámite a importar
            case 83: // Ha ocurrido un error al recuperar datos de enlaces de un trámite a importar
            case 84: // Ha ocurrido un error al recuperar datos de condiciones de entrada de un trámite a importar
            case 85: // Ha ocurrido un error al recuperar datos de condiciones de salida de un trámite a importar
            case 86: // Ha ocurrido un error al recuperar datos de campos suplementarios de un trámite a importar
            case 87: // Ha ocurrido un error al recuperar datos de integraciones con servicios web de un trámite a importar
            case 90: // Ha ocurrido un error al recuperar los códigos de trámite en el procedimiento destino
            case 91: // Ha ocurrido un error al intentar grabar en el procedimiento destino datos generales de un trámite importado
            case 92: // Ha ocurrido un error al intentar grabar en el procedimiento destino datos de documentos de un trámite importado
            case 93: // Ha ocurrido un error al intentar grabar en el procedimiento destino datos de enlaces de un trámite importado
            case 94: // Ha ocurrido un error al intentar grabar en el procedimiento destino datos de condiciones de entrada de un trámite importado
            case 95: // Ha ocurrido un error al intentar grabar en el procedimiento destino datos de condiciones de salida de un trámite importado
            case 96: // Ha ocurrido un error al intentar grabar en el procedimiento destino datos de campos suplementarios de un trámite importado
            case 97: // Ha ocurrido un error al intentar grabar en el procedimiento destino datos de integraciones con servicios web de un trámite importado
            case 981: // Ha ocurrido un error al grabar la correspondencia de códigos de los trámites importados y de sus procedimientos
            case 982: // Ha ocurrido un error al recuperar el código asignado a un trámite importado en el procedimiento destino
            case 99: // Ha ocurrido un error al intentar grabar el flujo de tramitación recuperado de la biblioteca en el procedimiento destino
                etiqueta = "msgFalloImpFlujo"+codigo;
                break;
            default:
                etiqueta = "";
                break;
        }
        
        return etiqueta;
    }
}
