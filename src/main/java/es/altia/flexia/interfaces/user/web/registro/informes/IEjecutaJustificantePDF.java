package es.altia.flexia.interfaces.user.web.registro.informes;

import java.util.Map;

public interface IEjecutaJustificantePDF {

    public String ejecutaJustificantePDF(String origenPlantilla, String nombrePlantilla,String descUorRegistro, String xml, boolean printBarcode, String destinoPdf) throws Exception;
    public byte[] generaJustificantePDF(String origenPlantilla, String nombrePlantilla,String descUorRegistro, String xml, boolean printBarcode) throws Exception;

    public byte[] generaJustificantePDF_SIR(String origenPlantilla, String nombrePlantilla, Map<String,Object> parametros) throws Exception;
    public String ejecutaJustificantePDF_SIR(String origenPlantilla, String nombrePlantilla, String descUorRegistro, Map<String,Object> parameters, String destinoPdf) throws Exception;
    
}
