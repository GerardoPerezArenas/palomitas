package es.altia.flexia.portafirmas.documentos.externos.localizacion.expediente.action;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.ExpedienteOtroDocumentoVO;
import es.altia.agora.business.sge.FicheroVO;
import es.altia.agora.business.sge.persistence.FichaExpedienteManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.technical.EstructuraCampo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Action encargado de recuperar la lista de documentos anexos a un expediente determinado. Es invocado desde el portafirmas.
 * Si el action recibe el número de expediente, ya se sabe cual es, en caso contrario, si el documento sobre el 
 * que se consulta es un documento externo, y se desea ver la documentación anexa al expediente al que pertenece, si es que está
 * asociado a alguno, entonces, se invoca al servicio de localización de expediente que intenta localizar, entre los dados de alta, 
 * el expediente al que está asociado a dicho documento.
 *
 * Una vez que se sepa el expediente, se pueden recuperar toda la documentación asociada al mismo para mostrarla en un listado, y permitir
 * descargarla 
 * @author oscar
 */
public class ListadoDocumentoExpedientePortafirmasAction extends ActionSession{
    
    
    public ActionForward performSession(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws IOException {
        
        HttpSession session  = request.getSession();        
        String numExpediente = request.getParameter("numExpediente");
        
        
        // Se recupera el usuario de la sesión
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        
        
        if(numExpediente!=null && !"".equals(numExpediente)){
            
           // Parámetro de conexión a la BBDD
           String[] params = usuario.getParamsCon();

           
           String[] datos = numExpediente.split("/");
           String ejercicio = datos[0];
           String codProcedimiento = datos[1];
           String codMunicipio = Integer.toString(usuario.getOrgCod());
           
            // Se recupera los documentos anexos al expediente
           GeneralValueObject gVODocumentos = new GeneralValueObject();
           gVODocumentos.setAtributo("codMunicipio",codMunicipio);
           gVODocumentos.setAtributo("codProcedimiento",codProcedimiento);
           gVODocumentos.setAtributo("ejercicio",ejercicio);
           gVODocumentos.setAtributo("numero",numExpediente);
           
           
           
           /********************************************************************************/           
           /********* Se recupera la lista de documentos de inicio del expediente **********/
           /********************************************************************************/
           Vector<FicheroVO> documentos = FichaExpedienteManager.getInstance().cargaDocumentosExpediente(gVODocumentos,false, params);
           Vector<FicheroVO> auxiliar = new Vector<FicheroVO>();
           for(int i=0;documentos!=null && i<documentos.size();i++){
               FicheroVO f = documentos.get(i);
               f.setCodigo(f.getCodigoDocumentoPresentado()+ "-" + f.getMunicipio() + "-" +  f.getEjercicio() + "-" + f.getNumero());
               auxiliar.add(f);
           }
           
           request.setAttribute("documentosExpediente",auxiliar);
           
           /********************************************************************************/           
           /********* Se recupera la lista de documentos asociados a anotaciones ***********/
           /********* de registro relacionadas con el expediente, tanto de entrada *********/
           /********* como de salida                                               *********/
           /********************************************************************************/
          Vector<FicheroVO> ficherosRegistroEntrada = 
              FichaExpedienteManager.getInstance().cargarFicherosRegistro("E",numExpediente,codMunicipio,params);         

          
          for (FicheroVO fichero : ficherosRegistroEntrada) {                            
              fichero.setCodigo(fichero.getEjercicio()+ "-" + fichero.getNumero() + "-" + fichero.getTipo() + "-" + fichero.getNombre() + "-" + fichero.getDep() + "-" + fichero.getUor() + "-" + fichero.getTipoContenido());
              fichero.setNombreAsiento(fichero.getEjercicio()+ "/" + fichero.getNumero());
              //relacionFicheros.put(fichero.getCodigo(), fichero);              
          }
          request.setAttribute("ficherosRegistroEntrada",ficherosRegistroEntrada);
          
          // Registro de salida
          Vector<FicheroVO> ficherosRegistroSalida = FichaExpedienteManager.getInstance().cargarFicherosRegistro("S",numExpediente,codMunicipio,params);
          
          for (FicheroVO fichero : ficherosRegistroSalida) {                            
              fichero.setCodigo(fichero.getEjercicio()+ "-" + fichero.getNumero() + "-" + 
                                fichero.getTipo() + "-" + fichero.getNombre());
              fichero.setNombreAsiento(fichero.getEjercicio()+ "/" + fichero.getNumero());
              //relacionFicheros.put(fichero.getCodigo(), fichero);              
          }
          
          request.setAttribute("ficherosRegistroSalida",ficherosRegistroSalida);
          
          
          /*******************************************************************************************/           
          /********* Se recupera la lista de documentos asociados a trámites del expediente. *********/
          /********* Tanto campos suplementarios de tipo fichero como documentos             *********/
          /********* de tramitación                                                          *********/
          /*******************************************************************************************/
          Vector<FicheroVO> ficherosTramites = FichaExpedienteManager.getInstance().cargarFicherosTramites(usuario,numExpediente,codMunicipio,false,params);
         
          String tramiteAnterior = "";
          for (FicheroVO fichero : ficherosTramites) {
              if (fichero.getTipoContenido().equals("none")) {
                  fichero.setCodigo("DOC-" + fichero.getMunicipio() + "-" + fichero.getExpediente() + "-" + fichero.getTramite() + "-" +
                          fichero.getOcurrencia() + "-" + fichero.getCodigoFicheroTramite());
              } else {
                  fichero.setCodigo("ANEXO-" + fichero.getMunicipio() + "-" + fichero.getExpediente() + "-" + fichero.getTramite() + "-" +
                          fichero.getOcurrencia() + "-" + fichero.getCodigoFicheroTramite());
              }
              
              // Se fija el valor para indicar en la jsp si hay que dibujar un nuevo tramite
              if (!tramiteAnterior.equals(fichero.getNombreTramite())) {
                  fichero.setNuevoTramite("true");
                  tramiteAnterior = fichero.getNombreTramite();
              }
              //relacionFicheros.put(fichero.getCodigo(), fichero);
              
          }// for
          request.setAttribute("ficherosTramites",ficherosTramites);
 
          
          /*******************************************************************************************/           
          /********* Se recupera la lista de documentos asociados a trámites del expediente. *********/
          /********* Tanto campos suplementarios de tipo fichero como documentos             *********/
          /********* de tramitación                                                          *********/
          /*******************************************************************************************/
          GeneralValueObject gVO2 = new GeneralValueObject();
          gVO2.setAtributo("codMunicipio",codMunicipio);
          gVO2.setAtributo("codProcedimiento",codProcedimiento);
          gVO2.setAtributo("ejercicio",ejercicio);
          //gVO2.setAtributo("codTramite");
          gVO2.setAtributo("numero",numExpediente);
          gVO2.setAtributo("desdeJsp","SI");
          gVO2.setAtributo("consultaCampos","SI");

          
          Vector estructuraDatosSuplementarios = FichaExpedienteManager.getInstance().cargaEstructuraDatosSuplementarios(gVO2, params);  
          GeneralValueObject nombresFicheros = FichaExpedienteManager.getInstance().cargaNombresFicheros(gVO2, estructuraDatosSuplementarios, params);

          Vector<FicheroVO> ficherosExpediente = new Vector<FicheroVO>();
          for (Object objEst: estructuraDatosSuplementarios) {
                EstructuraCampo estCampo = (EstructuraCampo)objEst;
                if ("5".equals(estCampo.getCodTipoDato())) {            
                    if (estCampo.getCodTramite() == null || "".equals(estCampo.getCodTramite())) {
                        String codigo = estCampo.getCodCampo();              

                        String nombreFichero = (String)nombresFicheros.getAtributo(codigo);
                        if (nombreFichero != null && !nombreFichero.equals("")) {
                            m_Log.debug("NOMBRE FICHERO: " + nombreFichero);
                            FicheroVO fichero = new FicheroVO();
                            fichero.setCodigo(codigo);                            
                            fichero.setNombre(nombreFichero);              
                            ficherosExpediente.add(fichero); 
                        }
                    }
                }// if
          }// for
          request.setAttribute("ficherosExpediente",ficherosExpediente);
          request.setAttribute("numExpediente",numExpediente);
          
          
          
          ArrayList<ExpedienteOtroDocumentoVO> otrosDocumentosExpediente = new ArrayList<ExpedienteOtroDocumentoVO>();
          otrosDocumentosExpediente = FichaExpedienteManager.getInstance().obtenerOtrosDocumentosExpediente(codMunicipio, ejercicio, numExpediente, params);
          
          request.setAttribute("otrosDocumentosExpediente",otrosDocumentosExpediente);
          
          
          
          
        }

        return mapping.findForward("listado");
    }
    
    
}
