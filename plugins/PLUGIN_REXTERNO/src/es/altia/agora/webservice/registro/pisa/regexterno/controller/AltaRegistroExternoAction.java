package es.altia.agora.webservice.registro.pisa.regexterno.controller;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.terceros.mantenimiento.MunicipioVO;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.MunicipiosDAO;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.ProvinciasDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.webservice.registro.pisa.regexterno.model.persistence.RegistroExternoManager;
import es.altia.util.conexion.BDException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 *
 * @author ivan.perez
 */
public class AltaRegistroExternoAction extends ActionSession{




    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response)
            throws IOException, ServletException {
        
                if (m_Log.isDebugEnabled()) m_Log.debug("ENTRAMOS EN EL ACTION DE AltaRegistroExterno");

        // Recuperamos el usuario y los datos de conexion.
        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        String[] params = usuario.getParamsCon();

        // Recuperamos la opcion de la request.
        String opcion = request.getParameter("opcion");
        if (m_Log.isDebugEnabled()) m_Log.debug("LA OPCION EN EL ACTION ES " + opcion);

        // Recuperamos el formulario asociado.
        if (form == null) {
            form = new AnotacionRegistroExternoForm();
            if ("request".equals(mapping.getScope())) request.setAttribute(mapping.getAttribute(), form);
            else session.setAttribute(mapping.getAttribute(), form);
        }
        AnotacionRegistroExternoForm anotacionForm = (AnotacionRegistroExternoForm) form;
        
        if (opcion.equals("aceptarAlta")) {
            try {
            RegistroValueObject registroVO = new RegistroValueObject();
            
//Datos necesarios para la llamada al webservice            
            registroVO.setAnoReg(getEjercicio(anotacionForm.getFechaAnotacion()));
            registroVO.setUnidadOrgan(Integer.parseInt(anotacionForm.getCod_intUOR()));
            registroVO.setIdOrganizacion(usuario.getOrgCod());
            registroVO.setTipoDocInteresado(anotacionForm.getCodTipoDoc());
            registroVO.setDocumentoInteresado(anotacionForm.getTxtDNI());
            registroVO.setNomCompletoInteresado(anotacionForm.getTxtInteresado());
            registroVO.setDomCompletoInteresado(anotacionForm.getTxtDomicilio());
            registroVO.setAsunto(anotacionForm.getAsunto());
            registroVO.setNumExpediente(anotacionForm.getTxtExp1());
                registroVO.setCodProcedimiento(anotacionForm.getCodProcedimiento());
                if (registroVO.getTipoDocInteresado().equals("0")) {
                    registroVO.setDocumentoInteresado(" ");
                }
                GeneralValueObject provinciaVO = new GeneralValueObject();
                MunicipioVO municipioVO = new MunicipioVO();
                String codigoProvincia = "" ;
                if (anotacionForm.getTxtProv().equals("")) {
                    provinciaVO.setAtributo("paisProvincia", "108");
                    provinciaVO.setAtributo("codigoProvincia", "");                
                    provinciaVO.setAtributo("nombreProvincia", "");
                } else {
                    provinciaVO = ProvinciasDAO.getInstance().getProvinciaByPaisAndDesc(108, anotacionForm.getTxtProv(), params);
                    codigoProvincia = (String)provinciaVO.getAtributo("codigoProvincia");
                    if (anotacionForm.getTxtMuni().equals("")) {
                        municipioVO.setCodigoMunicipio(-1);
                        municipioVO.setNombreOficial("");
                        municipioVO.setNombreLargo("");
                        
                    } else
                    municipioVO = MunicipiosDAO.getInstance().getMunicipioByPaisAndProvAndDesc(108, Integer.parseInt(codigoProvincia), anotacionForm.getTxtMuni(), params);
                }    
            
//Otros datos            
            registroVO.setTlfInteresado(anotacionForm.getTxtTelefono());
            registroVO.setEmailInteresado(anotacionForm.getTxtCorreo());

                    registroVO.setProvInteresado(codigoProvincia);
                    registroVO.setMunInteresado(municipioVO.getCodigoMunicipio()+"");
            registroVO.setCpInteresado(anotacionForm.getTxtCP());
            
            m_Log.debug("DATOS ENVIADOS PARA altaAnotacionRegistroExterno");
            m_Log.debug("Ejercicio: " +registroVO.getAnoReg());
            m_Log.debug("Unidad Organica (SGE): " + registroVO.getUnidadOrgan());
            m_Log.debug("Organizacion (SGE): " + registroVO.getIdOrganizacion());
            m_Log.debug("Tipo de documento Interesado: " + registroVO.getTipoDocInteresado());
            m_Log.debug("Documento interesado: " + registroVO.getDocumentoInteresado());
            m_Log.debug("Nombre Completo Interesado: " + registroVO.getNomCompletoInteresado());
            m_Log.debug("Nombre Completo Interesado: " + request.getParameter("txtInteresado"));
            
            m_Log.debug("Domicilio completo interesado: " + registroVO.getDomCompletoInteresado());
            m_Log.debug("Asunto: " + registroVO.getAsunto());
            m_Log.debug("Expediente relacionado: " + registroVO.getNumExpediente());            
            RegistroExternoManager regExtManager = RegistroExternoManager.getInstance();
            
            try {
                registroVO = regExtManager.altaAnotacionRegistroExterno(registroVO, params);

            } catch (AnotacionRegistroException anotacionRegistroException) {
                    String errorAlta = "errorAlta";
                request.setAttribute("errorAlta", errorAlta);
                opcion = "errorDandoAlta";
                return mapping.findForward(opcion);
            }
            long reg = registroVO.getNumReg();
            m_Log.debug("DATOS RECIBIDOS DESPUES DE altaAnotacionRegistroExterno");
            m_Log.debug("Fecha Anotacion: " + registroVO.getFecEntrada());
            m_Log.debug("Numero de Anotacion: " + reg);
            
            anotacionForm.setHoraMinAnotacion(getSoloHora(registroVO.getFecEntrada()));
            anotacionForm.setNumeroAnotacion(registroVO.getNumReg()+"");
            anotacionForm.setFechaAnotacion(getSoloFecha(registroVO.getFecEntrada()));
            } catch (BDException ex) {
                throw new ServletException(ex);
            } catch (SQLException ex) {
                throw new ServletException(ex);
            }

        }
       
        
        return mapping.findForward(opcion);
    }
    
    
    public String getSoloFecha(String strfecha) throws IOException {
        String soloFecha = "";
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat dateformatAlternative = new SimpleDateFormat("dd/MM/yyyy");
        try {            
            Date datefecha = dateFormat.parse(strfecha);
            soloFecha = dateformatAlternative.format(datefecha);
        } catch (ParseException ex) {
            throw new IOException("Problema parseando fechas");
        }
        return soloFecha;
    }
    
    public String getSoloHora(String strfecha) throws IOException {
        String soloHora = "";
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat dateformatAlternative = new SimpleDateFormat("HH:mm");
        try {            
            Date datefecha = dateFormat.parse(strfecha);
            soloHora = dateformatAlternative.format(datefecha);
        } catch (ParseException ex) {
            throw new IOException("Problema parseando fechas");
        }
        return soloHora;        
    }
    
    public int getEjercicio(String strfecha) throws IOException {
        
            int anho = -1;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {            
            Date datefecha = dateFormat.parse(strfecha);
            Calendar fecha = Calendar.getInstance();
            fecha.setTime(datefecha);
            anho = fecha.get(Calendar.YEAR);
        } catch (ParseException ex) {
            throw new IOException("Problema parseando fechas");
        }
            return anho;
    }
}
