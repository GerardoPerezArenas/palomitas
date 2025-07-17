package es.altia.agora.interfaces.user.web.administracion.mantenimiento;

import es.altia.agora.business.administracion.mantenimiento.DirectivaVO;
import es.altia.agora.business.administracion.mantenimiento.UsuariosGruposValueObject;
import es.altia.common.service.config.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;

import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;


/** Clase utilizada para capturar o mostrar el estado de una Tramitacion */
public class UsuariosGruposForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(UsuariosGruposForm.class.getName());

    //Reutilizamos
    UsuariosGruposValueObject ugVO = new UsuariosGruposValueObject();
    
    // Lista de aplicaciones con alguna directiva
    Vector<String> codAplDirectivas;
    Vector<String> descAplDirectivas;

    private String  listaProcedimientosRestringidos=null;

    public UsuariosGruposValueObject getUsuariosGrupos() {
        return ugVO;
    }

    public void setUsuariosGrupos(UsuariosGruposValueObject ugVO) {
        this.ugVO = ugVO;
    }

    /* Seccion donde metemos los metods get y set de los campos del formulario */
    
  public void setPaginaListado(String paginaListado) {
		ugVO.setPaginaListado(paginaListado); 
	}

	public void setNumLineasPaginaListado(String numLineasPaginaListado) {
		ugVO.setNumLineasPaginaListado(numLineasPaginaListado); 
	}

	public void setPaginaListadoG(String paginaListadoG) {
		ugVO.setPaginaListadoG(paginaListadoG); 
	}

	public void setNumLineasPaginaListadoG(String numLineasPaginaListadoG) {
		ugVO.setNumLineasPaginaListadoG(numLineasPaginaListadoG); 
	}

	public boolean getIsValid() {
		return ugVO.getIsValid(); 
	}

	public String getPaginaListado() {
		return ugVO.getPaginaListado(); 
	}

	public String getNumLineasPaginaListado() {
		return ugVO.getNumLineasPaginaListado(); 
	}

	public String getPaginaListadoG() {
		return ugVO.getPaginaListadoG(); 
	}

	public String getNumLineasPaginaListadoG() {
		return ugVO.getNumLineasPaginaListadoG(); 
	}
	
	public void setNombreUsuario(String nombreUsuario) {
		ugVO.setNombreUsuario(nombreUsuario); 
	}

	public void setLogin(String login) {
		ugVO.setLogin(login); 
	}

	public void setContrasena(String contrasena) {
		ugVO.setContrasena(contrasena); 
	}

    public void setContrasena2(String contrasena2) {
        ugVO.setContrasena2(contrasena2);
    }

	public void setCodIdioma(String codIdioma) {
		ugVO.setCodIdioma(codIdioma); 
	}

	public void setDescIdioma(String descIdioma) {
		ugVO.setDescIdioma(descIdioma); 
	}

	public String getNombreUsuario() {
		return ugVO.getNombreUsuario(); 
	}

	public String getLogin() {
		return ugVO.getLogin(); 
	}

	public String getContrasena() {
		return ugVO.getContrasena(); 
	}

    public String getContrasena2() {
        return ugVO.getContrasena2();
    }

	public String getCodIdioma() {
		return ugVO.getCodIdioma(); 
	}

	public String getDescIdioma() {
		return ugVO.getDescIdioma(); 
	}

	public Vector getListaOrganizaciones() {
		return ugVO.getListaOrganizaciones(); 
	}
	
	public Vector getListaIdiomas() {
		return ugVO.getListaIdiomas(); 
	}
	
	public String getCodOrganizacion() {
		return ugVO.getCodOrganizacion(); 
	}

	public String getNombreOrganizacion() {
		return ugVO.getNombreOrganizacion(); 
	}

	public String getAutorizacion() {
		return ugVO.getAutorizacion(); 
	}
	public void setNombreGrupo(String nombreGrupo) {
		ugVO.setNombreGrupo(nombreGrupo); 
	}
	public String getNombreGrupo() {
		return ugVO.getNombreGrupo(); 
	}
	public Vector getListaUsuariosGrupos() {
		return ugVO.getListaUsuariosGrupos(); 
	}
	
	public void setCodUsuario(String codUsuario) {
		ugVO.setCodUsuario(codUsuario); 
	}

	public void setCodAplicacion(String codAplicacion) {
		ugVO.setCodAplicacion(codAplicacion); 
	}

	public void setNombreAplicacion(String nombreAplicacion) {
		ugVO.setNombreAplicacion(nombreAplicacion); 
	}

	public void setCodEntidad(String codEntidad) {
		ugVO.setCodEntidad(codEntidad); 
	}

	public void setNombreEntidad(String nombreEntidad) {
		ugVO.setNombreEntidad(nombreEntidad); 
	}

	public String getCodUsuario() {
		return ugVO.getCodUsuario(); 
	}

	public String getCodAplicacion() {
		return ugVO.getCodAplicacion(); 
	}

	public String getNombreAplicacion() {
		return ugVO.getNombreAplicacion(); 
	}

	public String getCodEntidad() {
		return ugVO.getCodEntidad(); 
	}

	public String getNombreEntidad() {
		return ugVO.getNombreEntidad(); 
	}
	
	public void setCodGrupo(String codGrupo) {
		ugVO.setCodGrupo(codGrupo); 
	}

	public String getCodGrupo() {
		return ugVO.getCodGrupo(); 
	}
	
	public void setRespOpcion(String respOpcion) {
		ugVO.setRespOpcion(respOpcion); 
	}

	public String getRespOpcion() {
		return ugVO.getRespOpcion(); 
	}

	public Vector getListaGrupos() {
		return ugVO.getListaGrupos(); 
	}
	public Vector getListaAplicaciones() {
		return ugVO.getListaAplicaciones(); 
	}

	public String getCodUnidOrganica() {
		return ugVO.getCodUnidOrganica(); 
	}

	public String getNombreUnidOrganica() {
		return ugVO.getNombreUnidOrganica(); 
	}

	public Vector getListaUnidOrganicas() {
		return ugVO.getListaUnidOrganicas(); 
	}
	public Vector getListaUnidadesOrganicas() {
		return ugVO.getListaUnidadesOrganicas(); 
	}
	
    public Vector getListaCargosUOR() {
        return ugVO.getListaCargosUOR();
    }

	public void setListaOrg(Vector listaOrg) {
		ugVO.setListaOrg(listaOrg); 
	}

	public Vector getListaOrg() {
		return ugVO.getListaOrg(); 
	}
	
	public Vector getListaUsuariosTodos() {
		return ugVO.getListaUsuariosTodos(); 
	}

    public String getNif() {
        return ugVO.getNif();
    }
    public void setNif(String nif) {
        ugVO.setNif(nif);
    }

    public int getEstado() {
    	return ugVO.getEstado();
    }
    
    public void setEstado(int estado) {
    	ugVO.setEstado(estado);
    }

    public int getFirmante() {
        return ugVO.getFirmante();
    }
    public void setFirmante(int firmante) {
        ugVO.setFirmante(firmante);
    }
    
    public String getBuzonFirma() {
        return ugVO.getBuzonFirma();
    }
    
     public void setBuzonFirma(String buzonFirma) {
        ugVO.setBuzonFirma(buzonFirma);
    }
    
    public String getEmail() {
        return ugVO.getEmail();
    }
    public void setEmail(String email) {
        ugVO.setEmail(email);
    }

    public FormFile getFicheroFirma() {
        return ugVO.getFicheroFirma();
    }
    public void setFicheroFirma(FormFile ficheroFirma) {
        ugVO.setFicheroFirma(ficheroFirma);
    }

    public byte[] getFicheroFirmaFisico() {
        return ugVO.getFicheroFirmaFisico();
    }
    public void setFicheroFirma(byte[] ficheroFirmaFisico) {
        ugVO.setFicheroFirmaFisico(ficheroFirmaFisico);
    }

    public String getTipoFirma() {
        return ugVO.getTipoFirma();
    }
    public void setTipoFirma(String tipoFirma) {
        ugVO.setTipoFirma(tipoFirma);
    }

    public Vector<String> getCodAplDirectivas() {
        return codAplDirectivas;
    }

    public void setCodAplDirectivas(Vector<String> codAplDirectivas) {
        this.codAplDirectivas = codAplDirectivas;
    }

    public Vector<String> getDescAplDirectivas() {
        return descAplDirectivas;
    }

    public void setDescAplDirectivas(Vector<String> descAplDirectivas) {
        this.descAplDirectivas = descAplDirectivas;
    }

    public Vector<DirectivaVO> getDirectivas() {
        return ugVO.getDirectivas();
    }

    public String getFechaEliminado(){
        return ugVO.getFechaEliminado();
    }

    public void setFechaEliminado (String fechaEliminado){
        ugVO.setFechaEliminado(fechaEliminado);
    }

    /**
     * Transforma un string consistente en una lista de codigos de directivas
     * separados por "§¥" en un vector de DirectivaVO con sus codigos.
     * @param listaDirectivasTxt
     */
    public void setListaDirectivasTxt(String listaDirectivasTxt) {
        Vector<DirectivaVO> listaDirs = new Vector<DirectivaVO>();
        if (!listaDirectivasTxt.equals("")) {
            String[] lista = listaDirectivasTxt.split("§¥");
            for (int i=0; i<lista.length; i++) {
                DirectivaVO dir = new DirectivaVO();
                dir.setCodigo(lista[i]);
                listaDirs.add(dir);
            }
        }
        ugVO.setDirectivas(listaDirs);
    }


  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            ugVO.validate(idioma);
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

    /**
     * @return the listaProcedimientosRestringidos
     */
    public String getListaProcedimientosRestringidos() {
        return listaProcedimientosRestringidos;
    }

    /**
     * @param listaProcedimientosRestringidos the listaProcedimientosRestringidos to set
     */
    public void setListaProcedimientosRestringidos(String listaProcedimientosRestringidos) {
        this.listaProcedimientosRestringidos = listaProcedimientosRestringidos;
    }
        
    
    public String getCambioPantallaObligatorio(){        
        return this.ugVO.getCambioPantallaObligatorio();
    }
    
    public void setCambioPantallaObligatorio(String dato){
        this.ugVO.setCambioPantallaObligatorio(dato);
    }
     
     

}
