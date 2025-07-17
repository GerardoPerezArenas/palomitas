package es.altia.agora.interfaces.user.web.xpdl;

import es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.sge.DefinicionProcedimientosValueObject;
import es.altia.agora.business.sge.DefinicionTramitesValueObject;
import es.altia.agora.business.sge.FirmasDocumentoProcedimientoVO;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.firma.vo.FirmaFlujoVO;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.DefinicionTramitesManager;
import es.altia.agora.business.sge.persistence.FirmasDocumentoProcedimientoManager;
import es.altia.agora.business.sge.persistence.ImportacionProcedimientoManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.util.xpdl.ConversorXPDL;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


public class  GenerarXPDLServlet extends HttpServlet {
    private Logger log = Logger.getLogger(GenerarXPDLServlet.class);
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            log.debug(" ===============> GenerarXPDLServlet");

            String codigo =request.getParameter("codigo");
            String descripcion = request.getParameter("descripcion");
            String params[] =  request.getParameterValues("params");
            String opcion = request.getParameter("opcion");
            Vector<DefinicionTramitesValueObject> tramites;
			List<FirmaFlujoVO> flujos;

            HttpSession session = request.getSession();
            UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
            log.debug(" ===> codMunicipio usuario: " + usuario.getOrgCod());
            int codMunicipio = usuario.getOrgCod();

            try {				
                DefinicionProcedimientosValueObject defProcVO = new DefinicionProcedimientosValueObject();                    
                defProcVO.setTxtCodigo(codigo);
                defProcVO.setTxtDescripcion(descripcion);
                defProcVO.setCodMunicipio(Integer.toString(codMunicipio));

                defProcVO =recuperarProcedimiento(params,defProcVO);
                Vector listaUORs = UORsManager.getInstance().getListaUORs(false,params);
                tramites = recuperarTramites( params, defProcVO,listaUORs);            
				flujos = ImportacionProcedimientoManager.getInstance().getListaFlujosYCircuitosFirmasProc(defProcVO.getTxtCodigo(), params);
                ArrayList<FirmasDocumentoProcedimientoVO> firmasDocumentoProcedimiento = new ArrayList<FirmasDocumentoProcedimientoVO>();
                firmasDocumentoProcedimiento=FirmasDocumentoProcedimientoManager.getInstance().getTodasFirmasDocumentoPorProcedimiento(Integer.toString(codMunicipio),  codigo, params);
                defProcVO.setFirmasDocumentosProcedimiento(firmasDocumentoProcedimiento);

                if (opcion.equals("generar")) {
                    OutputStream out = response.getOutputStream();
                    ConversorXPDL.procedimientoToXPDL(defProcVO,tramites,flujos,out,usuario.getParamsCon());
                    out.close();
                } else {
                    Element procedimientoElement =ConversorXPDL.procedimientoToXPDL_XML(defProcVO,tramites,flujos,usuario.getParamsCon());                                        
                    Document document = new Document(procedimientoElement);                            
                    
                    //String tamano = document.toString();                    
                    BufferedOutputStream bos = null;                    
                    
                    Format format = Format.getPrettyFormat();                 
                    format.setEncoding("ISO-8859-1"); 
                    XMLOutputter outputter = new XMLOutputter(format); 
                    // XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
                    String tamano = outputter.outputString(document);

                    
                    byte[] fichero=null;
                    fichero = tamano.getBytes();                    
                    
                    ServletOutputStream out = response.getOutputStream();
                    response.setHeader("Content-disposition","attachment;filename="+"Export_" + defProcVO.getTxtCodigo() + "."+"xml");
                    response.setContentType("text/xml");                                          
                    response.setContentLength(fichero.length);                                                                 
                    bos = new BufferedOutputStream(out);
                    
                    bos.write(fichero, 0, fichero.length);
                    bos.flush();
                    bos.close();                                                            
                }

            } catch(Exception e){
                e.printStackTrace();
                log.error("Error al mostrarDocumentoPlantillaGen:  "+e.getMessage());
            }
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
                doGet(request,response);
        }

        private DefinicionProcedimientosValueObject recuperarProcedimiento(String params[],DefinicionProcedimientosValueObject defProcVO) 
                throws AnotacionRegistroException{
        
        DefinicionProcedimientosManager defProcManager=DefinicionProcedimientosManager.getInstance();
      //  Vector listaUor = defProcManager.getListaUnidadInicio(params);
        //defProcVO.setListaUnidadInicio(listaUor);
        //m_Log.debug("numero unidades de inicio "+listaUor.size());
           return defProcManager.buscar(defProcVO, params);
           
        }
        private Vector<DefinicionTramitesValueObject> recuperarTramites (String params[],
                DefinicionProcedimientosValueObject defProcVO,Vector listaUORs)
            throws AnotacionRegistroException,TramitacionException{
            
            Vector<DefinicionTramitesValueObject> resultado = new Vector<DefinicionTramitesValueObject>();
           
            Vector tramites = new Vector();
            tramites =DefinicionProcedimientosManager.getInstance().catalogoProcedimientosTramites(defProcVO, params);
            defProcVO.setTramites(tramites);
            Iterator<DefinicionTramitesValueObject> iterador=defProcVO.getTramites().iterator();
            while (iterador.hasNext()){
                DefinicionTramitesValueObject defTramVO = (DefinicionTramitesValueObject) iterador.next();
                defTramVO = DefinicionTramitesManager.getInstance().getTramite(defTramVO, defTramVO.getCodMunicipio(), params, true);
                // Se recupera la lista de enlaces del trámite
                defTramVO.setListaEnlaces(convertEnlacesTramites((Vector<GeneralValueObject>)DefinicionTramitesManager.getInstance().getListaEnlaces(defTramVO, params)));
                defTramVO.setListaUnidadesTramitadoras(listaUORs);
                
                //public ArrayList<FlujoSalidaTramiteVO> getFlujoSalidaTramiteImportacion(int codTramite,String codProcedimiento,int codMunicipio,String[] params) {                
                defTramVO.setFlujoSalidaTramiteImportacion(DefinicionTramitesManager.getInstance().getFlujoSalidaTramiteImportacion(Integer.parseInt(defTramVO.getCodigoTramite()), defTramVO.getTxtCodigo(),Integer.parseInt(defTramVO.getCodMunicipio()), params));
                resultado.add(defTramVO);
            }
            return resultado;
        }

        /**
         * Convierte una colección de objetos GeneralValueObject en una colección de objetos
         * DefinicionProcedimientosValueObject para pasar los enlaces
         * @return
         */
        private Vector<DefinicionProcedimientosValueObject> convertEnlacesTramites(Vector<GeneralValueObject> enlaces){
            Vector<DefinicionProcedimientosValueObject> salida = new Vector<DefinicionProcedimientosValueObject>();
            
            for(int i=0;enlaces!=null && i<enlaces.size();i++){
                GeneralValueObject gEnlace = enlaces.get(i);
                DefinicionProcedimientosValueObject defProcVO = new DefinicionProcedimientosValueObject();
                defProcVO.setCodEnlace((String)gEnlace.getAtributo("codigo"));
                defProcVO.setDescEnlace((String)gEnlace.getAtributo("descripcion"));
                defProcVO.setUrlEnlace((String)gEnlace.getAtributo("url"));
                defProcVO.setEstadoEnlace((String)gEnlace.getAtributo("estado"));
                salida.add(defProcVO);
            }
            return salida;
        }

}
