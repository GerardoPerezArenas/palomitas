package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.administracion.mantenimiento.persistence.CargosManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.UsuariosGruposManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.FirmasDocumentoProcedimientoVO;
import es.altia.agora.business.sge.persistence.FirmasDocumentoProcedimientoManager;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Tiffany
 */
public class ListadoFirmasDocumentoProcedimientoAction extends ActionSession {

    private Logger log = Logger.getLogger(ListadoFirmasDocumentoProcedimientoAction.class);

    @Override
    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String opcion = request.getParameter("opcion");
        String codProcedimiento = request.getParameter("codProcedimiento");
        String codMunicipio = request.getParameter("codMunicipio");
        String codDocumento = request.getParameter("codDocumento");
        String listaFirmasStr = request.getParameter("listaFirmasStr");
        log.debug("Firmas de documento de procedimiento ================================>");
        log.debug("El parámetro opción es : " + opcion);
        log.debug("El parámetro codProcedimiento es : " + codProcedimiento);
        log.debug("El parámetro codMunicipio es : " + codMunicipio);
        log.debug("El parámetro listaFirmasStr es : " + listaFirmasStr);
        log.debug("Documento a modificar: " + codDocumento);

        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        String[] params = usuario.getParamsCon();

        if (opcion.equalsIgnoreCase("entrada")) {
            ArrayList<FirmasDocumentoProcedimientoVO> listaFirmas =
                    FirmasDocumentoProcedimientoManager.getInstance().
                    getFirmasDocumento(codDocumento, codMunicipio, codProcedimiento, params);
            request.setAttribute("listaFirmas", listaFirmas);            
            
        } else if (opcion.equalsIgnoreCase("nuevaFirma")) {
            
            Vector<UORDTO> unidades = UORsManager.getInstance().getListaUORsDeAlta(params);
            m_Log.debug("Cargadas " + unidades.size() + " UORs");
            request.setAttribute("listaUORs", unidades);

            Vector nuevosCargos = CargosManager.getInstance().getListaUOROrdenPorDesc(params);
            m_Log.debug("Cargados " + nuevosCargos.size() + " Cargos");
            request.setAttribute("listaCargos", nuevosCargos);
            
            Vector usuarios = UsuariosGruposManager.getInstance().getUsuariosFirmantesUnidadCargo(codMunicipio, null, null, params);
            log.debug("Recuperados usuarios con poder de firma en la unidad/cargo: " + usuarios.size());
            request.setAttribute("listaUsuarios", usuarios);
            
        } else if (opcion.equalsIgnoreCase("verUsuarios")) {
            String uor = request.getParameter("codigoUOR");
            String cargo = request.getParameter("codigoCargo");
            log.debug("Buscar usuario en unidad y cargo: " + uor + "::" + cargo);
            Vector usuarios = UsuariosGruposManager.getInstance().getUsuariosFirmantesUnidadCargo(codMunicipio, uor, cargo, params);
            log.debug("Recuperados usuarios con poder de firma en la unidad/cargo: " + usuarios.size());
            request.setAttribute("listaUsuarios", usuarios);
            
        }  else if (opcion.equalsIgnoreCase("guardarCircuito")) {                        
            ArrayList<FirmasDocumentoProcedimientoVO> listaFirmas = crearListadoFirmasGuardar(listaFirmasStr);
            FirmasDocumentoProcedimientoManager.getInstance().guardarListaFirmas(listaFirmas,codMunicipio, codProcedimiento, codDocumento,params);
            request.setAttribute("exito", "SI");
            request.setAttribute("numeroFirmas", ""+listaFirmas.size());
        }



        return mapping.findForward(opcion);
    }

    private ArrayList<FirmasDocumentoProcedimientoVO> crearListadoFirmasGuardar(String listaFirmasStr) {
        ArrayList<FirmasDocumentoProcedimientoVO> resultado = new ArrayList<FirmasDocumentoProcedimientoVO>();
        if (listaFirmasStr==null || "".equals(listaFirmasStr)) return resultado;
        //String[] firmas = listaFirmasStr.split("¬¬");
        String[] firmas = listaFirmasStr.split("§¥");
        //for (int i=0;i<firmas.length;i=i+5) {
        for (int i=0;i<firmas.length;i++) {
            
            String[] datosFirma = firmas[i].split("¬¬");
            if(datosFirma!=null && datosFirma.length==7){
                FirmasDocumentoProcedimientoVO auxVO = new FirmasDocumentoProcedimientoVO();
                //auxVO.setUor(firmas[i]);
                auxVO.setUor(datosFirma[0]);
                //auxVO.setCargo(firmas[i+1]);
                auxVO.setCargo(datosFirma[1]);
                //auxVO.setUsuario(firmas[i+2]);
                auxVO.setUsuario(datosFirma[2]);
                //auxVO.setOrden(firmas[i+3]);
                auxVO.setOrden(datosFirma[3]);
                            // revisar el orden en el que llegan			
                //auxVO.setFinalizaRechazo(firmas[i+5]);
                auxVO.setFinalizaRechazo(datosFirma[5]);
                //auxVO.setTramitar(firmas[i+6]);
                auxVO.setTramitar(datosFirma[6]);
                resultado.add(auxVO);
            }
        }
        return resultado;
    }
}
