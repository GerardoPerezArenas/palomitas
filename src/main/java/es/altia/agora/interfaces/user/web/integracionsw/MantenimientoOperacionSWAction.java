package es.altia.agora.interfaces.user.web.integracionsw;

import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.integracionsw.persistence.DefinicionOperacionesSWManager;
import es.altia.agora.business.integracionsw.DefinicionParametroEntradaVO;
import es.altia.agora.business.integracionsw.DefinicionParametroSalidaVO;
import es.altia.agora.business.integracionsw.DefinicionArraySWVO;
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
import java.util.Collection;
import java.util.Vector;

public class MantenimientoOperacionSWAction extends ActionSession {

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws IOException, ServletException {

        if (m_Log.isDebugEnabled()) m_Log.debug("ENTRAMOS EN EL ACTION DE MantenimientoOperacionSW");

        // Recuperamos la opcion de la request.
        String opcion = request.getParameter("opcion");

        // Recuperamos el usuario y los datos de conexion.
        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        String[] params = usuario.getParamsCon();

        // Recuperamos el formulario asociado.
        if (form == null) {
            m_Log.debug("Rellenamos el form de MantenimientoOperacionSW");
            form = new MantenimientoOperacionSWForm();
            if ("request".equals(mapping.getScope())) request.setAttribute(mapping.getAttribute(), form);
            else session.setAttribute(mapping.getAttribute(), form);
        }
        MantenimientoOperacionSWForm mantOpForm = (MantenimientoOperacionSWForm)form;

        if (m_Log.isDebugEnabled()) m_Log.debug("LA OPCION EN EL ACTION ES " + opcion);

        if (opcion.equals("cargaMantenimientoOp")) {
            String strCodDefOp = request.getParameter("codigoOpDef");
            int codDefOp = Integer.parseInt(strCodDefOp);

            try {
                DefinicionOperacionesSWManager manager = DefinicionOperacionesSWManager.getInstance();

                GeneralValueObject infoOpDef = manager.getInfoGeneralOperacion(codDefOp, params);

                mantOpForm.setCodDefOp(strCodDefOp);
                mantOpForm.setCodigoSW((String)infoOpDef.getAtributo("codigoSW"));
                mantOpForm.setTituloSW((String)infoOpDef.getAtributo("tituloSW"));
                mantOpForm.setTituloOp((String)infoOpDef.getAtributo("tituloOp"));
                mantOpForm.setNombreOpSW((String)infoOpDef.getAtributo("nombreOpSW"));

                // Comprobamos si faltan arrays por definir.
                if (manager.areArraysUndefined(codDefOp, params)) {

                    Collection listaArrays = manager.getArraysByOpCod(codDefOp, params);
                    mantOpForm.setListaArrays(new Vector(listaArrays));

                    opcion = "cargaMantArrays";

                } else {
                    // Comprobamos si existe una estructura de datos ya definida. Si no existe, la definimos.
                    if (!manager.existeEstructuraDatos(codDefOp, params)) {
                        manager.creaEstructuraDatos(codDefOp, params);
                    }

                    Collection listaParamsEntrada = manager.getParamsDefEntrada(codDefOp, params);
                    mantOpForm.setListaParamsIn(new Vector(listaParamsEntrada));

                    Collection listaParamsSalida = manager.getParamsDefSalida(codDefOp, params);
                    mantOpForm.setListaParamsOut(new Vector(listaParamsSalida));

                    opcion = "cargaMantParametros";
                }

            } catch (InternalErrorException e) {
                e.printStackTrace();
            }
        }

        if (opcion.equals("modificarParamIn")) {
            try {
                DefinicionParametroEntradaVO paramIn = new DefinicionParametroEntradaVO();
                paramIn.setDescParam(request.getParameter("descParamNew"));
                paramIn.setTituloParam(request.getParameter("tituloParamNew"));
                String strObligatorio = request.getParameter("obligParamNew");
                paramIn.setEsObligatorio(Boolean.parseBoolean(strObligatorio));
                String strTipoParam = request.getParameter("tipoAsignNew");
                paramIn.setTipoParametro(Integer.parseInt(strTipoParam));
                paramIn.setValorDefecto(request.getParameter("valorDefectoNew"));
                String strCodigoOpDef = request.getParameter("codigoOpDef");
                paramIn.setCodigoOpDef(Integer.parseInt(strCodigoOpDef));
                String strCodParam = request.getParameter("codParam");
                paramIn.setCodigoParam(Integer.parseInt(strCodParam));

                DefinicionOperacionesSWManager manager = DefinicionOperacionesSWManager.getInstance();
                manager.updateDefParamIn(paramIn, params);
                
                Collection listaParamsEntrada = manager.getParamsDefEntrada(paramIn.getCodigoOpDef(), params);
                mantOpForm.setListaParamsIn(new Vector(listaParamsEntrada));

            } catch (InternalErrorException e) {
                e.printStackTrace();
            }
        }

        if (opcion.equals("modificarParamOut")) {
            try {
                DefinicionParametroSalidaVO paramOut = new DefinicionParametroSalidaVO();
                paramOut.setDescParam(request.getParameter("descParamNew"));
                paramOut.setTituloParam(request.getParameter("tituloParamNew"));
                String strTipoDato = request.getParameter("tipoDatoNew");
                paramOut.setTipoDato(Integer.parseInt(strTipoDato));
                paramOut.setValorCorrecto(request.getParameter("valorCorrectoNew"));
                String strCodigoOpDef = request.getParameter("codigoOpDef");
                paramOut.setCodOpDef(Integer.parseInt(strCodigoOpDef));

                DefinicionOperacionesSWManager manager = DefinicionOperacionesSWManager.getInstance();
                manager.updateDefParamOut(paramOut, params);

                Collection listaParamsSalida = manager.getParamsDefSalida(paramOut.getCodOpDef(), params);
                mantOpForm.setListaParamsOut(new Vector(listaParamsSalida));

            } catch (InternalErrorException e) {
                e.printStackTrace();
            }
        }

        if (opcion.equals("modificarArray")) {
            try {
                DefinicionArraySWVO defArray = new DefinicionArraySWVO();
                defArray.setDescParamArray(request.getParameter("descParamArray"));
                String numInstancias = request.getParameter("numRepsNew");
                defArray.setNumRepeticiones(Integer.parseInt(numInstancias));
                String strCodigoOpDef = request.getParameter("codigoOpDef");
                defArray.setCodigoOpDef(Integer.parseInt(strCodigoOpDef));

                DefinicionOperacionesSWManager manager = DefinicionOperacionesSWManager.getInstance();
                manager.updateNumRepsArray(defArray, params);

                Collection listaArrays = manager.getArraysByOpCod(defArray.getCodigoOpDef(), params);
                mantOpForm.setListaArrays(new Vector(listaArrays));

            } catch (InternalErrorException e) {
                e.printStackTrace();
            }
        }

        return mapping.findForward(opcion);
    }


}
