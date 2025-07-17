/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.interfaces.user.web.xpdl;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.sge.DefinicionProcedimientosValueObject;
import es.altia.agora.business.sge.DefinicionTramitesValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.util.xpdl.ConversorXPDL;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.io.FileOutputStream;
import java.util.Iterator;




/**
 *
 * @author ricardo.iglesias
 */
public class GenerarProcedimientoXPDLAction extends ActionSession {
    
    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws IOException, ServletException {

        if (m_Log.isDebugEnabled()) m_Log.debug("ENTRAMOS EN EL ACTION DE generarProcedimientoXPDL");

        // Recuperamos el usuario y los datos de conexion.
        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        String[] params = usuario.getParamsCon();
       
        Vector<DefinicionTramitesValueObject> tramites;
         // Recuperamos la opcion de la request.
        String opcion = request.getParameter("opcion");
        if (m_Log.isDebugEnabled()) m_Log.debug("LA OPCION EN EL ACTION ES " + opcion);
        
           if (opcion.equals("generarXPDL")) {
            

            try {
                DefinicionProcedimientosValueObject defProcVO = new DefinicionProcedimientosValueObject();
                String codigo =(String) request.getParameter("codigo");
                String descripcion =(String) request.getParameter("descripcion");
          
                defProcVO.setTxtCodigo(codigo);
                defProcVO.setTxtDescripcion(descripcion);
                defProcVO.setCqUnidadInicio("1");
                defProcVO.setCodEstado("0");
                defProcVO.setDeCatalogoDeProcedimiento("no");
                defProcVO.setCodMunicipio("0");
                defProcVO.setCodAplicacion("19");
                defProcVO.setLocalizacion("0");
                defProcVO.setCodTipoInicio("0");
                defProcVO =recuperarProcedimiento(params,defProcVO);
                tramites = recuperarTramites( params, defProcVO);
                String path = this.getServlet().getServletContext().getRealPath("");
                path = path+"/xpdl/"+descripcion+".xpdl";
                m_Log.debug("path"+path);
                FileOutputStream output=new FileOutputStream(path);
                
   

                Vector listaUORs = new Vector();
                
              //  ConversorXPDL.procedimientoToXPDL(defProcVO, tramites,listaUORs,output);
                output.close();
                String exportado = "si";
                request.setAttribute("exportado", exportado);
                opcion = "procedimientoExportado";
                
             }catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "ERROR: " + e.getMessage());
            }

        }
      
        m_Log.debug(mapping.findForward(opcion));
        return mapping.findForward(opcion);

    }
    private DefinicionProcedimientosValueObject recuperarProcedimiento(String params[],DefinicionProcedimientosValueObject defProcVO)
            throws AnotacionRegistroException{
        
       DefinicionProcedimientosManager defProcManager=DefinicionProcedimientosManager.getInstance();
       return defProcManager.buscar(defProcVO, params);
           
    
    }

    
     private Vector<DefinicionTramitesValueObject> recuperarTramites (String params[],DefinicionProcedimientosValueObject defProcVO)
            throws AnotacionRegistroException,TramitacionException{
            
            Vector<DefinicionTramitesValueObject> resultado = new Vector<DefinicionTramitesValueObject>();
            Vector tramites = new Vector();
            tramites =(DefinicionProcedimientosManager.getInstance().catalogoProcedimientosTramites(defProcVO, params));
            defProcVO.setTramites(tramites);
            Iterator<DefinicionTramitesValueObject> iterador=defProcVO.getTramites().iterator();
            while (iterador.hasNext()){
                DefinicionTramitesValueObject defTramVO = (DefinicionTramitesValueObject) iterador.next();
                resultado.add(defTramVO);
            }
            return resultado;
        }
}
    
