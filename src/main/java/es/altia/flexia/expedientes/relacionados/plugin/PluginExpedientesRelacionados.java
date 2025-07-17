package es.altia.flexia.expedientes.relacionados.plugin;

import es.altia.common.exception.TechnicalException;
import es.altia.flexia.expedientes.relacionados.plugin.vo.ExpedienteRelacionadoVO;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Interfaz que implementan el plugin de expedientes relacionados para el módulod de Registro
 * @author oscar.rodriguez
 */
public interface PluginExpedientesRelacionados {

    public String cargar(Hashtable<String,String> argumentos,String url,String contexto);
    public int insertarExpedienteRelacionado(ExpedienteRelacionadoVO expediente);
    public int existeExpediente(ExpedienteRelacionadoVO expediente) throws TechnicalException;
    public ExpedienteRelacionadoVO getNumeroExpedienteRelacionado(ExpedienteRelacionadoVO expediente);
}
