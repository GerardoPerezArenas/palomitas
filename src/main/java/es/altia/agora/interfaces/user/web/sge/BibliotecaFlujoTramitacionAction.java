/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.sge.DefinicionProcedimientosValueObject;
import es.altia.agora.business.sge.ResultadoImportacionBibliotecaVO;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.DefinicionTramitesImportacionManager;
import es.altia.agora.business.sge.persistence.manual.DefinicionProcedimientosDAO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class BibliotecaFlujoTramitacionAction extends DispatchAction{
    private Logger log = Logger.getLogger(BibliotecaFlujoTramitacionAction.class);
    
    /**
     * Método que se llama para comprobar si el procedimiento tiene expedientes
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception 
     */
    public ActionForward comprobarExpedientesProcedimiento(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception{
        log.debug("================= BibliotecaFlujoTramitacionAction : comprobarExpedientesProcedimiento() ======================>");
        
        HttpSession session = request.getSession();
        
        if(session.getAttribute("usuario")!=null){
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            String[] params = usuario.getParamsCon();
            String codProcedimiento = request.getParameter("codProcedimiento");
            String respuesta;
            
            boolean existenExpProc = (DefinicionProcedimientosManager.getInstance()).existeExpedientesProcedimiento(codProcedimiento, params);
            if(!existenExpProc) respuesta="0";
            else respuesta="1";
            
            response.setContentType("text/html");                    
            PrintWriter out = response.getWriter();
            out.print(respuesta);
            out.flush();
            out.close();
        }
        
        return null;
    }
    
    public ActionForward getProcedimientosConsulta(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception{
        log.debug("================= BibliotecaFlujoTramitacionAction : getProcedimientosConsulta() ======================>");
        
        HttpSession session = request.getSession();
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        ArrayList<DefinicionProcedimientosValueObject> lista = null;
        
        if(session.getAttribute("usuario")!=null){
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            String[] params = usuario.getParamsCon();
            
            try{
                adapt = new AdaptadorSQLBD(params);
                con   = adapt.getConnection();
                
                String codProcDestino=request.getParameter("txtCodigo");
                lista = DefinicionProcedimientosDAO.getInstance().getProcedimientosBiblioteca(con);
                request.setAttribute("RelacionBibliotecas", lista);
                request.setAttribute("codProcDestino",codProcDestino);
            }catch(BDException bde){
                log.error("Error al recuperar una conexión a la BBDD: " + bde.getMessage());

            }catch(SQLException sqle){
                log.error("Error al recuperar una conexión a la BBDD: " + sqle.getMessage());

            }
            finally{
                try{
                   adapt.devolverConexion(con);
                }catch(Exception e){
                    log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
                }
            }
        }
        
        return mapping.findForward("listarLibrerias");
    }
    
    public ActionForward importarFlujoBiblioteca(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response){
        log.debug("================= BibliotecaFlujoTramitacionAction : importarFlujoBiblioteca() ======================>");
        
        HttpSession session = request.getSession();
        String opcion = "";
            
        if(session.getAttribute("usuario")!=null){
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            String[] params = usuario.getParamsCon();
            int codMunicipio = usuario.getOrgCod();

            String codProcDestino = request.getParameter("codProcDest");
            String codBiblioteca = request.getParameter("codBiblioteca");

            ResultadoImportacionBibliotecaVO resultado = DefinicionTramitesImportacionManager.getInstance().importarFlujo(codProcDestino, codBiblioteca, codMunicipio, params);
            int codigoError = resultado.getEstado();
            if(codigoError != 0){
                request.setAttribute("importacionCodError", codigoError);
                request.setAttribute("importacionDescError", ResultadoImportacionBibliotecaVO.getEtiquetaError(codigoError));
                request.setAttribute("agrupacionesEnConflicto", resultado.getAgrupCamposConflicto());
                request.setAttribute("camposEnConflicto", resultado.getCamposSuplConflicto());
                request.setAttribute("codProcedimiento", codProcDestino);
                opcion = "falloImportacion";
            } else {
                DefinicionProcedimientosValueObject defProcVO = null;
                DefinicionProcedimientosForm defProcForm = null;
                String[] paramsDiputacion = new String[7];      
                    
                try {
                    
                    
                    /****/
                    // Hay que recargar la lista de procedimientos para que después se puede cargar correctamente
                    // el procedimiento recién importado, ya que sino existe en esta lista, no se puede cargar
                    // directamente su información en modo consulta.
                    DefinicionProcedimientosValueObject aux = new DefinicionProcedimientosValueObject();
                    aux.setCodMunicipio(String.valueOf(codMunicipio));
                    Vector consulta = DefinicionProcedimientosManager.getInstance().consultar(aux, params);                    
                    session.setAttribute("RelacionProcedimientos", consulta);
                    /*****/
                    
                    defProcForm = (DefinicionProcedimientosForm)session.getAttribute("DefinicionProcedimientosForm");
                    defProcVO = defProcForm.getDefinicionProcedimientos();
                    defProcVO.setTxtCodigo(codProcDestino);
                    defProcVO.setCodMunicipio(String.valueOf(codMunicipio));
                    defProcVO.setImportar("no");
                    defProcVO.setListaCodUnidadInicio(new Vector());
                    
                    if ("0".equals(codMunicipio)) {
                        String jndi = DefinicionProcedimientosManager.getInstance().obtenerJndi(defProcVO, params);
                        paramsDiputacion[0] = params[0]; // "oracle";
                        paramsDiputacion[6] = jndi;
                        defProcVO = DefinicionProcedimientosManager.getInstance().buscar(defProcVO, paramsDiputacion);
                    } else {
                        defProcVO = DefinicionProcedimientosManager.getInstance().buscar(defProcVO, params);
                    }
                    defProcForm.setDefinicionProcedimientos(defProcVO);
                    
                    session.setAttribute("DefinicionProcedimientosForm",defProcForm);
                    request.setAttribute("importacionBiblioteca", "si");
                    return new ActionForward("/sge/DefinicionProcedimientos.do?opcion=recargaConsulta&codProcedimiento="+ codProcDestino + "&codMunicipio=" + codMunicipio + "&importar=no");
                    
                } catch (AnotacionRegistroException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        return mapping.findForward(opcion);
    } 
}
