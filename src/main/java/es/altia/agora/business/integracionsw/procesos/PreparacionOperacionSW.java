package es.altia.agora.business.integracionsw.procesos;

import es.altia.agora.business.integracionsw.*;
import es.altia.agora.business.integracionsw.exception.TipoNoValidoException;
import es.altia.agora.business.integracionsw.exception.EjecucionSWException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PreparacionOperacionSW {

    protected static Log m_Log = LogFactory.getLog(PreparacionOperacionSW.class.getName());
    
   protected static Config m_Conf = ConfigServiceHelper.getConfig("common");

    private InfoServicioWebVO infoSW;

    public PreparacionOperacionSW(InfoServicioWebVO infoSW) {
        this.infoSW = infoSW;
    }

    public InfoServicioWebVO rellenarDatosParametros(HashMap<String, String> params, String nombreOp) {

        OperacionServicioWebVO operacionSW = getOperacionByName(nombreOp);

        for (String descParam: params.keySet()) {
            String value = params.get(descParam);
            m_Log.debug("rellenarDatosParametros: " + descParam+" valor: "+value);
            guardarValorDato(operacionSW, descParam, value);
        }
        return infoSW;
    }

    private void guardarValorDato(OperacionServicioWebVO operacionSW, String descParam, String value) {

        StringTokenizer partePorAnalizar = new StringTokenizer(descParam, ".[");

        m_Log.debug("ANALIZANDO EL PARAMETRO: " + descParam);
        m_Log.debug("ANALIZANDO EL PARAMETRO value: " + value);

        String nombreParam = partePorAnalizar.nextToken();

        ParametroSWVO paramVO = getParametroEntrada(operacionSW, nombreParam);
        colocarValor(paramVO.getTipoParametro(), partePorAnalizar, value);
    }

    private void colocarValor(TipoServicioWebVO tipoParametro, StringTokenizer partePorAnalizar, String valor) {

        
        String  controlaSiEsNull = "SI";
        try{
            m_Log.debug("Obtenemos del common si se controla el envio de null: "+controlaSiEsNull);
            controlaSiEsNull = m_Conf.getString("controlNuloValorWSPreparacionSW");
        }catch(Exception e){
            m_Log.error("no existe la propiedad controlNuloValorWSPreparacionSW en el common.properties");
            controlaSiEsNull="SI";
        }
    
        m_Log.debug("colocar valor : " + tipoParametro+" "+valor);
        if("SI".equals(controlaSiEsNull)) 
        {
            if (valor == null || "".equals(valor)) return;
        }
        
        if (tipoParametro.esTipoBase()) {
            TipoBasicoVO tipoBasico = (TipoBasicoVO)tipoParametro;
            try {
                tipoBasico.setValor(valor);
            } catch (TipoNoValidoException e) {
                e.printStackTrace();
            }
        } else if (tipoParametro.esTipoArray()) {
            m_Log.debug("ES DE TIPO ARRAY. VEMOS EN QUE POSICION DEL ARRAY SE ENCUENTRA");
            m_Log.debug("FALTA POR ANALIZAR: " + partePorAnalizar.countTokens());
            String idParamArray = partePorAnalizar.nextToken();
            int posArray = Integer.parseInt(idParamArray.substring(0, idParamArray.indexOf(']')));

            m_Log.debug("SE HA OBTENIDO EL VALOR " + posArray);
            TipoArrayVO tipoArray = (TipoArrayVO)tipoParametro;
            if (tipoArray.getArray().size() < posArray) {
                tipoArray.getArray().setSize(posArray);
            }
            TipoServicioWebVO tipoContenido = tipoArray.getArray().get(posArray - 1);
            if (tipoContenido == null) {
                tipoContenido = tipoArray.getTipoContenido().copy();
                tipoArray.getArray().setElementAt(tipoContenido, posArray-1);
            }
            colocarValor(tipoContenido, partePorAnalizar, valor);
            m_Log.debug("EL TIPO CONTENIDO ES: " + tipoContenido);
        } else if (tipoParametro.esTipoComplejo()) {
            TipoCompuestoVO tipoCompuesto = (TipoCompuestoVO)tipoParametro;
            m_Log.debug("ES DE TIPO COMPUESTO. BUSCAMOS EL IDENTIFICADOR DEL SIGUIENTE PARAMETRO");
            m_Log.debug("FALTA POR ANALIZAR: " + partePorAnalizar.countTokens());
            String nombreParam = partePorAnalizar.nextToken();
            colocarValor(getTipoCampo(tipoCompuesto, nombreParam), partePorAnalizar, valor);
        }
    }

    private TipoServicioWebVO getTipoCampo(TipoCompuestoVO tipoCompuesto, String nombreParam) {
        m_Log.debug("BUSCAMOS EL CAMPO QUE TENGA EL IDENTIFICADOR: " + nombreParam);
        return tipoCompuesto.getCampo(nombreParam).getTipoCampo();
    }

    private ParametroSWVO getParametroEntrada(OperacionServicioWebVO operacionSW, String nombreParam) {

        m_Log.debug("BUSCAMOS EL PARAMETRO: " + nombreParam);
        Vector<ParametroSWVO> params = operacionSW.getParamsEntrada();
        for (ParametroSWVO paramIn: params) {
            if (nombreParam.equals(paramIn.getNombreParametro())) return paramIn;
        }
        return null;
    }

    private OperacionServicioWebVO getOperacionByName(String nombreOp) {

        for (Object objOper : infoSW.getOperacionesSW()) {
            OperacionServicioWebVO operacion = (OperacionServicioWebVO) objOper;
            if (nombreOp.equals(operacion.getNombreOperacion())) return operacion;
        }
        return null;
    }

    public Vector recuperarDatosSalida(TipoServicioWebVO tipoRetorno, Vector paramsOut) throws EjecucionSWException {

        Vector valoresSalida = new Vector();
        Iterator itParams = paramsOut.iterator();
        while (itParams.hasNext()) {
            DefinicionParametroSalidaVO paramSalida = (DefinicionParametroSalidaVO)itParams.next();
            StringTokenizer idParam = new StringTokenizer(paramSalida.getDescParam(), ".[");
            idParam.nextToken();

            valoresSalida.add(recuperaDato(idParam, tipoRetorno));
        }
        return valoresSalida;
    }

    private String recuperaDato(StringTokenizer idParam, TipoServicioWebVO tipoRetorno) throws EjecucionSWException {

        m_Log.debug("RECUPERAMOS LOS DATOS DEL TIPO " + tipoRetorno);
        if (tipoRetorno.esTipoBase()) {
            TipoBasicoVO tipoBase = (TipoBasicoVO)tipoRetorno;
            return tipoBase.getValorAsString();
        }

        if (tipoRetorno.esTipoArray()) {
            String idParamArray = idParam.nextToken();
            int posArray = Integer.parseInt(idParamArray.substring(0, idParamArray.indexOf(']')));
            TipoArrayVO tipoArray = (TipoArrayVO)tipoRetorno;
            if (tipoArray.getArray().size() > 0) {
                TipoServicioWebVO tipoContenido = tipoArray.getArray().get(posArray - 1);
                if (tipoContenido == null) return null;
                else return recuperaDato(idParam, tipoContenido);
            } else return null;
        }

        if (tipoRetorno.esTipoComplejo()) {
            String idCampo = idParam.nextToken();
            m_Log.debug("BUSCAMOS EL CAMPO: " + idCampo);
            TipoCompuestoVO tipoCompuesto = (TipoCompuestoVO)tipoRetorno;
            if (tipoCompuesto.getCampo(idCampo) != null) {
                TipoServicioWebVO tipoCampo = tipoCompuesto.getCampo(idCampo).getTipoCampo();
                return recuperaDato(idParam, tipoCampo);
            } else return null;
        }

        throw new EjecucionSWException(new Exception("NO EXCEPCION"), "SE HA PRODUCIDO UN ERROR AL INTENTAR " +
                "RECUPERAR LOS DATOS DE SALIDA");
    }

}
