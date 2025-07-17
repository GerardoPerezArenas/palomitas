// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.administracion.mantenimiento;

// PAQUETES IMPORTADOS
import es.altia.agora.business.administracion.mantenimiento.CamposListadosParametrizablesVO;
import es.altia.agora.business.escritorio.*;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.*;
import es.altia.agora.business.administracion.mantenimiento.persistence.*;
import es.altia.agora.business.util.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.commons.fileupload.*;


/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase OrganizacionesAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */


public class ListadosAction extends ActionSession  {

  public ActionForward performSession(ActionMapping mapping, ActionForm form, 
                      HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {

    m_Log.debug("================= ListadosAction ======================>");
    ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
    HttpSession session = request.getSession();

    // Validaremos los parametros del request especificados
    ActionErrors errors = new ActionErrors();
    String opcion = request.getParameter("opcion");
    if (m_Log.isInfoEnabled()) m_Log.info("la opcion en el action es " + opcion);

    // Rellenamos el form de BusquedaTerceros
    if (form == null) {
      m_Log.debug("Rellenamos el form de MantenimientosAdminForm");
      form = new MantenimientosAdminForm();
      if ("request".equals(mapping.getScope())){
        request.setAttribute(mapping.getAttribute(), form);
      }else{
        session.setAttribute(mapping.getAttribute(), form);
      }
    }
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    String[] params = usuario.getParamsCon();
    MantenimientosAdminForm mantForm = (MantenimientosAdminForm)form;
    ListadosManager mantManager = ListadosManager.getInstance(); 
    mantForm.setOperacion("");
    if ("cargar".equalsIgnoreCase(opcion)){
          m_Log.debug("************   cargar listados parametrizables");
        Vector listados = mantManager.getListadosParametrizables(params);
        mantForm.setListadosParametrizables(listados);
    
    }else if("consultarCampos".equalsIgnoreCase(opcion)){
        m_Log.debug("************   Cargar pagina listado campos");

        CamposListadosParametrizablesVO gVO1 = new CamposListadosParametrizablesVO();
        gVO1.setCodCampo(Integer.parseInt(request.getParameter("codigo")));
       
       Vector lista = mantManager.getListaCamposListados(gVO1,params);
        mantForm.setCamposListados(lista);
        m_Log.debug("************   Cargar pagina listado campos  "+gVO1.getCodCampo());
        mantForm.setCodlistado(Integer.toString(gVO1.getCodCampo()));
        String descr=request.getParameter("descripcion");
     
        mantForm.setNomCampo(descr.toLowerCase());

    }else if("grabar".equalsIgnoreCase(opcion)){ 
        // m_Log.debug("************   Grabar listado campos  "+request.getParameter("listaCampos"));
        CamposListadosParametrizablesVO gVO1 = new CamposListadosParametrizablesVO();
        gVO1.setCodListado(Integer.parseInt(mantForm.getCodlistado()));
        m_Log.debug("Grabar pagina listado campos  " + gVO1.getCodListado());
        String listaCodCampo=request.getParameter("listaCodCampo");
        gVO1.setvCodCampo(listaTemasSeleccionados(listaCodCampo));
        String listaNomCampo=request.getParameter("listaNomCampo");
        gVO1.setvNomCampo(listaTemasSeleccionados(listaNomCampo));
        String listTamanoCampo=request.getParameter("listTamanoCampo");
        gVO1.setvTamanoCampo(listaTemasSeleccionados(listTamanoCampo));
        String listaActCampo=request.getParameter("listaActCampo");
        gVO1.setvActCampo(listaTemasSeleccionados(listaActCampo));
 
      int res=mantManager.grabarListaCamposListados(gVO1,params);
      opcion = "grabar";
     }else if("salir".equalsIgnoreCase(opcion)){ 
          m_Log.debug("** salir  ");
           opcion = "salir";
    }else{
      opcion = mapping.getInput();
    }
    m_Log.debug("<================= ListadosAction ======================");
    return (mapping.findForward(opcion));
  }
private Vector listaTemasSeleccionados(String listTemasSelecc) {
		Vector lista = new Vector();
		StringTokenizer valores = null;
		if (listTemasSelecc != null) {
			valores = new StringTokenizer(listTemasSelecc,"§¥",false);
			while (valores.hasMoreTokens()) {
				String valor = valores.nextToken();
				lista.addElement(valor);
			}
		}
		return lista;
	}

}