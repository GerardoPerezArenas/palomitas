package es.altia.agora.interfaces.user.web.integracionsw;

import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.integracionsw.persistence.DefinicionOperacionesSWManager;
import es.altia.agora.business.integracionsw.persistence.MantenimientoSWManager;
import es.altia.agora.business.integracionsw.persistence.ReconstruccionSWManager;
import es.altia.agora.business.integracionsw.*;
import es.altia.agora.business.integracionsw.exception.EjecucionSWException;
import es.altia.agora.business.integracionsw.procesos.PreparacionOperacionSW;
import es.altia.agora.business.integracionsw.procesos.LanzadorServicioWeb;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.util.exceptions.InternalErrorException;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.*;

public class TestOperacionSWAction extends ActionSession {

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws IOException, ServletException {

        if (m_Log.isDebugEnabled()) m_Log.debug("ENTRAMOS EN EL ACTION DE TestOperacionSWAction");

        // Recuperamos la opcion de la request.
        String opcion = request.getParameter("opcion");

        // Recuperamos el usuario y los datos de conexion.
        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        String[] params = usuario.getParamsCon();

        // Recuperamos el formulario asociado.
        if (form == null) {
            m_Log.debug("Rellenamos el form de TestOperacionSWAction");
            form = new TestOperacionSWForm();
            if ("request".equals(mapping.getScope())) request.setAttribute(mapping.getAttribute(), form);
            else session.setAttribute(mapping.getAttribute(), form);
        }
        TestOperacionSWForm testOpForm = (TestOperacionSWForm)form;

        if (m_Log.isDebugEnabled()) m_Log.debug("LA OPCION EN EL ACTION ES " + opcion);
        
        if (opcion.equals("cargaTest") || testOpForm.getCodOpDef() == null) {
            String strCodDefOp = request.getParameter("codigoOpDef");
            int codDefOp = Integer.parseInt(strCodDefOp);

            try {
                DefinicionOperacionesSWManager manager = DefinicionOperacionesSWManager.getInstance();

                GeneralValueObject infoOpDef = manager.getInfoGeneralOperacion(codDefOp, params);

                testOpForm.setCodOpDef(strCodDefOp);

                testOpForm.setTituloSW((String) infoOpDef.getAtributo("tituloSW"));
                testOpForm.setTituloOpSW((String) infoOpDef.getAtributo("tituloOp"));
                testOpForm.setNombreOpSW((String) infoOpDef.getAtributo("nombreOpSW"));
                testOpForm.setWsdlSW((String) infoOpDef.getAtributo("wsdlSW"));
                testOpForm.setCodigoSW((String) infoOpDef.getAtributo("codigoSW"));

                Collection<DefinicionParametroEntradaVO> listaParamsEntrada = manager.getParamsDefEntrada(codDefOp, params);
                testOpForm.setListaParamsIn(new Vector<DefinicionParametroEntradaVO>(listaParamsEntrada));

                Collection<DefinicionParametroSalidaVO> listaParamsSalida = manager.getParamsDefSalida(codDefOp, params);
                testOpForm.setListaParamsOut(new Vector<DefinicionParametroSalidaVO>(listaParamsSalida));

                // Reasociamos el 
            } catch (InternalErrorException e) {
                e.printStackTrace();
            }
        }

        if (opcion.equals("ejecutarTest")) {

            String strParamsIn = request.getParameter("cadenaParams");
            
            try {

                int codigoSW = Integer.parseInt(testOpForm.getCodigoSW());
                int codigoOpDef = Integer.parseInt(testOpForm.getCodOpDef());

                MantenimientoSWManager mantManager = MantenimientoSWManager.getInstance();
                InfoServicioWebVO infoSW = mantManager.getInfoGeneralSWByCodigo(codigoSW, params);

                ReconstruccionSWManager reconManager = ReconstruccionSWManager.getInstance();
                Collection<OperacionServicioWebVO> oneOperation = new ArrayList<OperacionServicioWebVO>();
                oneOperation.add(reconManager.reconstruirOperacionVO(codigoOpDef, params));
                infoSW.setOperacionesSW(oneOperation);

                HashMap<String, String> paramsSW = processCadenaParams(strParamsIn);
                m_Log.debug("HASHMAP DE PARAMETROS: " + paramsSW);
                PreparacionOperacionSW preparacion = new PreparacionOperacionSW(infoSW);
                infoSW = preparacion.rellenarDatosParametros(paramsSW, testOpForm.getNombreOpSW());
                m_Log.debug("OPERACION: " + infoSW);
                LanzadorServicioWeb launcher = new LanzadorServicioWeb(infoSW);
                TipoServicioWebVO tipoRetorno = launcher.llamarServicioWeb(testOpForm.getNombreOpSW(), params);

                Vector resultados = preparacion.recuperarDatosSalida(tipoRetorno, testOpForm.getListaParamsOut());

                m_Log.debug(resultados);

                testOpForm.setResultados(resultados);
                testOpForm.setEstadoEjecucion("LA EJECUCION HA TERMINADO CORRECTAMENTE");

            } catch (InternalErrorException e) {
                e.printStackTrace();                
            } catch (EjecucionSWException e) {
                e.printStackTrace();
                testOpForm.setEstadoEjecucion(e.stackTraceToString());
            }
        }
        return mapping.findForward(opcion);
    }

    private HashMap<String, String> processCadenaParams(String strParams) {
        strParams += "|";
        HashMap<String, String> params = new HashMap<String, String>();
        while(strParams.length() > 1) {
            int j = strParams.indexOf('|');
            String key = strParams.substring(0, j);
            strParams = strParams.substring(j + 1, strParams.length());
            j = strParams.indexOf('|');
            String value = strParams.substring(0, j);
            strParams = strParams.substring(j + 1, strParams.length());
            params.put(key, value);
        }
        return params;
    }
}
