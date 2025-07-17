package es.altia.flexia.expedientes.relacionados.plugin.artemis.action;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.flexia.expedientes.relacionados.plugin.PluginExpedientesRelacionadosArtemis;
import es.altia.flexia.expedientes.relacionados.plugin.artemis.form.FichaExpedienteArtemisForm;
import es.altia.flexia.expedientes.relacionados.plugin.artemis.util.Reflection;
import es.altia.flexia.expedientes.relacionados.plugin.artemis.vo.ExpedienteArtemisVO;
import es.altia.flexia.expedientes.relacionados.plugin.util.ConstantesDatosExpRelacionados;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Recupera la información relativa a un expediente de Artemis y redirige a la jsp correspondiente para mostrarla.
 * Se llama cuando se haya cargado el plugin de expedientes relacionados para artemis en su integración con registro
 * @author oscar.rodriguez
 */
public class CargarFichaExpedienteArtemisAction extends ActionSession{

    private Logger log = Logger.getLogger(CargarFichaExpedienteArtemisAction.class);
    private final String SUCCESS = "exito";
    
    public ActionForward performSession(ActionMapping mapping,
                                        ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws IOException, ServletException {

        log.debug("=========> CargarFichaExpedienteArtemisAction - init <=======================");
        UsuarioValueObject usuarioVO = (UsuarioValueObject) request.getSession().getAttribute("usuario");        
        String codExp = request.getParameter("codExp");  // Código del exp. relacionado a una anotación del registro de entrada
        log.debug("Código expediente de Artemis a recuperar  " + codExp);
        
        PluginExpedientesRelacionadosArtemis plugin = new PluginExpedientesRelacionadosArtemis();
        ExpedienteArtemisVO exp = plugin.getInfoExpediente(codExp, Integer.toString(usuarioVO.getOrgCod()));

        FichaExpedienteArtemisForm fichaExpedienteForm = (FichaExpedienteArtemisForm)form;
        this.rellenarFormulario(usuarioVO.getOrgCod(),fichaExpedienteForm, exp);
                
        return mapping.findForward(SUCCESS);
  }


  /**
   * Método que rellena el formulario con los datos existentes en un objeto de la clase ExpedienteArtemisVO
   * @param codOrganizacion: Código de la organización
   * @param formulario: Representa el formulario de tipo FichaExpedienteArtemisForm
   * @param exp: ExpedienteArtemisVO
   * @return FichaExpedienteArtemisForm
   */
  private FichaExpedienteArtemisForm rellenarFormulario(int codOrganizacion,FichaExpedienteArtemisForm formulario,ExpedienteArtemisVO exp){

      ResourceBundle configRegistro  = ResourceBundle.getBundle("Registro");
      String plugin = configRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS);
      
      String campoAlcanceMaximo    = configRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CON_BARRA
              + plugin + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CAMPO_ALCANCE_MAXIMO_MOSTRAR);
      String campoDuracionContrato = configRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CON_BARRA
              + plugin + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CAMPO_DURACION_CONTRATO_MOSTRAR);
      String campoFechaFin               = configRegistro.getString(codOrganizacion + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CON_BARRA
              + plugin + ConstantesDatosExpRelacionados.PLUGIN_EXPEDIENTES_RELACIONADOS_CAMPO_FEC_FIN_MOSTRAR);

      String valorAlcanceMaximo    = Reflection.getValorMetodo(exp, campoAlcanceMaximo);
      String valorDuracionContrato = Reflection.getValorMetodo(exp, campoDuracionContrato);
      String valorFechaFin              = Reflection.getValorMetodo(exp, campoFechaFin);

      log.debug("rellenarFormulario plugin: " + plugin);
      log.debug("rellenarFormulario campoAlcanceMaximo: " + campoAlcanceMaximo);
      log.debug("rellenarFormulario campoDuracionContrato: " + campoDuracionContrato);
      log.debug("rellenarFormulario campoFechaFin: " + campoFechaFin);

      log.debug("rellenarFormulario valorAlcanceMaximo: " + valorAlcanceMaximo);
      log.debug("rellenarFormulario valorDuracionContrato: " + valorDuracionContrato);
      log.debug("rellenarFormulario valorFechaFin: " + valorFechaFin);

      formulario.setAlcanceMaximo(valorAlcanceMaximo);
      formulario.setDuracionContrato(valorDuracionContrato);
      formulario.setFechaFin(valorFechaFin);
      formulario.setFechaInicio(exp.getFechaInicio());
      formulario.setAreaAdquisicionesResponsable(exp.getAreaAdquisicionesResponsable());
      formulario.setCodigosCPV(exp.getCodigosCPV());
      formulario.setDepartamentoResponsable(exp.getDepartamentoResponsable());      
      formulario.setEmpresasAdjudicatarias(exp.getEmpresasAdjudicatarias());
      formulario.setEstadoExpediente(exp.getEstadoExpediente());
      formulario.setImporteModeloOfertaConIVA(exp.getImporteModeloOfertaConIVA());
      formulario.setImporteModeloOfertaSinIVA(exp.getImporteModeloOfertaSinIVA());
      formulario.setNaturalezaContrato(exp.getNaturalezaContrato());
      formulario.setNombreExpediente(exp.getNombreExpediente());
      formulario.setNumExpediente(exp.getNumExpediente());
      formulario.setNumeroLotes(exp.getNumeroLotes());
      formulario.setObjeto(exp.getObjeto());
      return formulario;
  }
  
}