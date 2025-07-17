package es.altia.agora.interfaces.user.web.registro;

import es.altia.agora.business.registro.ReservaOrdenValueObject;
import es.altia.agora.technical.Fecha;
import es.altia.common.service.config.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** Clase utilizada para capturar o mostrar el estado de un RegistroEntrada */
public class ReservaOrdenForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(ReservaOrdenForm.class.getName());
    //Reutilizamos

    ReservaOrdenValueObject reservaVO = new ReservaOrdenValueObject();
    String diligencia = null;
    String fechaTxt = null;
    boolean reservasPorUsuario;

    public ReservaOrdenValueObject getReserva() {
        return reservaVO;
    }

    public void setReservaOrden(ReservaOrdenValueObject reservaVO) {
        this.reservaVO = reservaVO;
    }

/*
    //Create es el action por defecto
    private String action = "create";

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
*/
    /* Seccion donde metemos los metods get y set de los campos del formulario */

    public String getDiligencia() {
        return diligencia;
    }

    public void setDiligencia(String diligencia) {
        this.diligencia = diligencia;
    }
    
    public String getFechaTxt() {
        return "";
    }

    public void setFechaTxt(String fechaTxt) {
        Fecha f= new Fecha();
        if (fechaTxt != null && !fechaTxt.equals("")) {
            reservaVO.setFecha(f.obtenerDateCompleto2(fechaTxt));
        }
    }

    public boolean getReservasPorUsuario() {
        return reservasPorUsuario;
    }

    public void setReservasPorUsuario(boolean reservasPorUsuario) {
        this.reservasPorUsuario = reservasPorUsuario;
    }



    
    public String getEjercicio(){
        return reservaVO.getEjercicio();
    }

    public void setEjercicio(String param) {
        reservaVO.setEjercicio(param);
    }

    public int getTxtNumRegistrado(){
        return reservaVO.getTxtNumRegistrado();
    }

    public void setTxtNumRegistrado(int param) {
        reservaVO.setTxtNumRegistrado(param);
    }

    public String getTxtDiaEntrada(){
        return reservaVO.getTxtDiaEntrada();
    }

    public void setTxtDiaEntrada(String param) {
        reservaVO.setTxtDiaEntrada(param);
    }

    public String getTxtMesEntrada(){
        return reservaVO.getTxtMesEntrada();
    }

    public void setTxtMesEntrada(String param) {
        reservaVO.setTxtMesEntrada(param);
    }

    public String getTxtAnoEntrada(){
        return reservaVO.getTxtAnoEntrada();
    }

    public void setTxtAnoEntrada(String param) {
        reservaVO.setTxtAnoEntrada(param);
    }

    public int getCodDepto() {
        return reservaVO.getCodDepto();
    }

    public void setCodDepto(int param) {
        reservaVO.setCodDepto(param);
    }

    public int getCodUnidad() {
        return reservaVO.getCodUnidad();
    }

    public void setCodUnidad(int param) {
        reservaVO.setCodUnidad(param);
    }

    public String getTipoReg() {
        return reservaVO.getTipoReg();
    }

    public void setTipoReg(String param) {
        reservaVO.setTipoReg(param);
    }

    public Date getFecha() {
        return reservaVO.getFecha();
    }

    public void setFecha(Date param) {
        reservaVO.setFecha(param);
    }

    public boolean getA() {
        return reservaVO.getA();
    }

    public void setA(boolean param) {
        reservaVO.setA(param);
    }

    public Vector getCodigos(){
        return reservaVO.getCodigos();
    }

    public void setCodigos(Vector param){
             reservaVO.setCodigos(param);
    }

    public String getFec() {
      return reservaVO.getFec();
    }

    public void setFec(String param) {
      reservaVO.setFec(param);
    }

    public String getDia() {
      return reservaVO.getDia();
    }

    public String getMes() {
      return reservaVO.getMes();
    }

    public String getAno() {
      return reservaVO.getAno();
    }

    public String getHora() {
      return reservaVO.getHora();
    }

    public String getMin() {
      return reservaVO.getMin();
    }

    public String getCantidad() {
      return reservaVO.getCantidad();
    }

    public void getCantidad(String param) {
      reservaVO.setCantidad(param);
   }

   public Vector getNumeros() {
      return reservaVO.getNumeros();
    }

   public void setNumeros(Vector param) {
      reservaVO.setNumeros(param);
   }
   
   public String getNombreUsuario() {
      return reservaVO.getNombreUsuario();
    }

   public void setNombreUsuario(String nombreUsuario) {
      reservaVO.setNombreUsuario(nombreUsuario);
   }



    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            reservaVO.validate(idioma);
        } catch (ValidationException ve) {
          //Hay errores...
          //Tenemos que traducirlos a formato struts
          errors=validationException(ve,errors);
        }
        return errors;
    }

    /* Función que procesa los errores de validación a formato struts */
    private ActionErrors validationException(ValidationException ve,ActionErrors errors){
      Iterator iter = ve.getMessages().get();
      while (iter.hasNext()) {
        Message message = (Message)iter.next();
        errors.add(message.getProperty(), new ActionError(message.getMessageKey()));
      }
      return errors;
    }

   // private String num_doc  = "";
    
    public String getFechaString() {
        return Fecha.obtenerString(reservaVO.getFecha());
    }
    
}
