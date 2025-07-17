/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexiaWS.documentos.bd.util;

import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.registro.persistence.AnotacionRegistroManager;
import es.altia.agora.business.registro.persistence.manual.AnotacionRegistroDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.registro.justificante.persistence.bd.JustificanteRegistroPersonalizadoManager;
import es.altia.flexia.registro.justificante.vo.JustificanteRegistroPersonalizadoVO;
import es.altia.flexiaWS.documentos.bd.datos.AnotacionVO;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author milagros.noya
 */
public class JusticanteRegistro {
    private static Log m_Log = LogFactory.getLog(es.altia.flexiaWS.documentos.bd.util.JusticanteRegistro.class);
    
    public static JustificanteRegistroPersonalizadoVO getPlantillaActiva(String[] params) throws BDException, TechnicalException{
        return JustificanteRegistroPersonalizadoManager.getInstance().getJustificanteActivo("justificante", params);
    }
    
    public static String getRutaPlantillas(int codOrganizacion){
        String propiedadRuta = codOrganizacion + ConstantesDatos.RUTA_PLANTILLAS_JUSTIFICANTE;
        
        return getValorPropiedad(propiedadRuta);
    }
    
    public static String getFormatoFecha(int codOrganizacion){
        String propiedadFormatoFecha = codOrganizacion + "/FORMATO_FECHA_JUSTIFICANTE_REGISTRO";
        
        return getValorPropiedad(propiedadFormatoFecha);
    }
    
    public static String getValorPropiedad(String propiedad){
        Config registroConf = ConfigServiceHelper.getConfig("Registro");
        
        String valor = null;
        try {
            valor = registroConf.getString(propiedad);
            m_Log.debug(String.format("Valor recuperado para la propiedad '%s': %s", propiedad, valor));
        } catch (Exception e) {
            m_Log.error(String.format("Error al recuperar la propiedad '%s': %s", propiedad, e.getMessage()));
        }
        
        return valor;
    }
    
    public static String getOficinaRegistro(AnotacionVO anotacion, String[] params) throws Exception{
        String oficina = null;
        try {
            oficina = AnotacionRegistroDAO.getInstance().getOficinaUorRegistro(anotacion.getTipo(), anotacion.getEjercicio(), anotacion.getNumero(), anotacion.getDepartamento(), anotacion.getUnidadRegistro(), params);
        } catch (Exception e){
            throw new Exception("Ha ocurrido un error al obtener la oficina de registro de la anotación");
        }
        
        return oficina;
    }
    
    public static String generarXml(AnotacionVO anotacion, String codOrganizacion, String formatoFecha, String[] params) throws AnotacionRegistroException{
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("formatoFecha", formatoFecha);
        gVO.setAtributo("codAplicacion", String.valueOf(ConstantesDatos.APP_REGISTRO_ENTRADA_SALIDA));
        gVO.setAtributo("idioma", Configuracion.getCodigoIdioma(codOrganizacion));
        gVO.setAtributo("codOur", String.valueOf(anotacion.getUnidadRegistro()));
        gVO.setAtributo("codTip", String.valueOf(anotacion.getTipo()));
        gVO.setAtributo("ejercicio", String.valueOf(anotacion.getEjercicio()));
        gVO.setAtributo("numero", String.valueOf(anotacion.getNumero()));
        gVO.setAtributo("tipoInforme", "justificante");

        return AnotacionRegistroManager.getInstance().consultaXML(gVO, params);
    }

    public static JustificanteRegistroPersonalizadoVO getJustificantebyName(String nombreJustificante, String[] params) throws BDException, TechnicalException{
        return JustificanteRegistroPersonalizadoManager.getInstance().getJustificanteByName(nombreJustificante, params);
    }

}
