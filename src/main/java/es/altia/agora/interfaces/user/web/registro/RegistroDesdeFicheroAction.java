// NOMBRE DEL PAQUETE 
package es.altia.agora.interfaces.user.web.registro;


import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.interfaces.user.web.helper.ActionHelper;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.service.config.Config;
import java.io.File;
import org.apache.struts.upload.FormFile;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;

import es.altia.agora.business.escritorio.RegistroUsuarioValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.registro.persistence.RegistroDesdeFicheroManager;
import es.altia.agora.business.terceros.mantenimiento.persistence.ParametrosTercerosManager;
import es.altia.agora.business.terceros.ParametrosTerceroValueObject;
import es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.UsuariosGruposManager;
import es.altia.agora.business.registro.mantenimiento.persistence.MantRolesManager;
import es.altia.agora.technical.ConstantesDatos;


public final class RegistroDesdeFicheroAction extends ActionSession {

	protected static Log m_Log =
            LogFactory.getLog(MantAnotacionRegistroAction.class.getName());
    
    protected static Config m_Conf = ConfigServiceHelper.getConfig("common");


	public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  	throws IOException, ServletException {

 		m_Log.debug("performSession");
 		
  		ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
  		HttpSession session = request.getSession();

  		String opcion="";
  		UsuarioValueObject usuarioVO=null;
  		RegistroUsuarioValueObject regUsuarioVO=null;
		String provincia=null;
		String municipio=null;
		String codPais=null;
		String codProvincia=null;
		String codMunicipio=null;
		
  		if ((session.getAttribute("usuario") != null) && (session.getAttribute("registroUsuario") != null)){

    		int cod_org;
    		int cod_ent;
		    int cod_dep;
		    int cod_uni;
		    String usuarioQRegistra;
		    String dptoUsuarioQRegistra;
			String unidOrgUsuarioQRegistra;

		    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
			regUsuarioVO = (RegistroUsuarioValueObject)session.getAttribute("registroUsuario");
		    cod_org= usuarioVO.getOrgCod();
			cod_ent= usuarioVO.getEntCod();
			cod_dep= regUsuarioVO.getDepCod();
		    cod_uni= regUsuarioVO.getUnidadOrgCod();
    		usuarioQRegistra= String.valueOf(usuarioVO.getIdUsuario());
    		dptoUsuarioQRegistra= String.valueOf(usuarioVO.getUnidadOrgCod());
    		unidOrgUsuarioQRegistra= String.valueOf(usuarioVO.getDepCod());
    		String[] params = usuarioVO.getParamsCon();
			
			ParametrosTerceroValueObject ptVO = ParametrosTercerosManager.getInstance().getParametrosTerceros(usuarioVO.getOrgCod(), usuarioVO.getParamsCon());
			provincia = ptVO.getNomProvincia();	
			municipio = ptVO.getNomMunicipio();
			codPais = ptVO.getPais();						
			codProvincia = ptVO.getProvincia();
			codMunicipio = ptVO.getMunicipio();


    		ActionErrors errors = new ActionErrors();


    		RegistroDesdeFicheroForm registroForm=null;
    
    		if (form == null) {
      			m_Log.debug("Rellenamos el form de RegistroDesdeFichero");
      			form = new MantAnotacionRegistroForm();
      			if ("request".equals(mapping.getScope()))
         			request.setAttribute(mapping.getAttribute(), form);
      			else
         			session.setAttribute(mapping.getAttribute(), form);
    		}

    		registroForm = (RegistroDesdeFicheroForm) form;

	    	opcion = request.getParameter("opcion");
    		if(m_Log.isInfoEnabled()) m_Log.info("RegistroDesdeFicheroAction --> opcion: "+opcion);
    		
			if ("iniciar_entrada".equals(opcion) || "iniciar_salida".equals(opcion) ){
                            String consultaUorsPermisoUsu="NO";
				if ("iniciar_entrada".equals(opcion)){
					registroForm.setTipoRegistro("E");
				}else if ("iniciar_salida".equals(opcion)){
					registroForm.setTipoRegistro("S");
                                         if (UsuariosGruposManager.getInstance().tienePermisoDirectiva(ConstantesDatos.REGISTRO_S_SOLO_UORS_USUARIO, usuarioVO.getIdUsuario(), params)) {
                                             consultaUorsPermisoUsu="SI";
                                             registroForm.setDirectivaUsuario("REGISTRO_S_SOLO_UORS_USUARIO");
                                             session.setAttribute("directiva_salidas_uor_usuario", "SI");
                                             
                                             
                                        }
				}
                                
                                
                                if("SI".equals(consultaUorsPermisoUsu)) {
                                    registroForm.setListaUORs( UORsManager.getInstance().getListaUORsPermisoUsuario(usuarioVO,params));
                                }
                                else {
                                    registroForm.setListaUORs( UORsManager.getInstance().getListaUORs(false,params));
                                }
                                
				opcion = "iniciar";
			} else if ("cargar".equals(opcion) ){
                                String baseDir = m_Conf.getString("PDF.base_dir");
				String usuDir = usuarioVO.getDtr();							
				FormFile fichero = registroForm.getFicheroUp();
				byte[] datos = fichero.getFileData();
				
				GeneralValueObject ficheroVO = new GeneralValueObject();				
				String nombreFichero = request.getParameter("nombreFicheroFisico");
				ficheroVO.setAtributo("usuarioQRegistra",usuarioQRegistra);
				ficheroVO.setAtributo("orgCod",Integer.toString(cod_org));
				ficheroVO.setAtributo("entCod",Integer.toString(cod_ent));
				ficheroVO.setAtributo("depCod",Integer.toString(cod_dep));
				ficheroVO.setAtributo("unidadOrgCod",Integer.toString(cod_uni));				
				ficheroVO.setAtributo("dptoUsuarioQRegistra", dptoUsuarioQRegistra);
				ficheroVO.setAtributo("unidOrgUsuarioQRegistra", unidOrgUsuarioQRegistra);    		
				ficheroVO.setAtributo("codPais", codPais);
				ficheroVO.setAtributo("codProvincia", codProvincia);
				ficheroVO.setAtributo("codMunicipio", codMunicipio);
				ficheroVO.setAtributo("tipoRegistro", registroForm.getTipoRegistro());
				ficheroVO.setAtributo("ejercicioDesde", registroForm.getEjReservaD());
				ficheroVO.setAtributo("numeroDesde", registroForm.getNumReservaD());										
				ficheroVO.setAtributo("numeroHasta", registroForm.getNumReservaH());
                                ficheroVO.setAtributo("asunto", registroForm.getAsunto());
                                String rolPorDefecto = MantRolesManager.getInstance().getRolDefecto(params);
                                ficheroVO.setAtributo("rolPorDefecto", rolPorDefecto);

	  			if(datos.length>0){
					  									 
					nombreFichero = fichero.getFileName();		
					String filePath = baseDir+"/"+usuDir+"/"+nombreFichero;
					ficheroVO.setAtributo("fichero",filePath);
					ficheroVO.setAtributo("datos",datos);
					upLoadFicheroINE(ficheroVO);
					
					/*
					Vector lineas = leerFicheroINE(ficheroVO);
					for (int i=0; i<lineas.size(); i++){
					}
					*/
					
					if (RegistroDesdeFicheroManager.getInstance().comprobarNumeroReservados(ficheroVO)){
						Vector noReservados = RegistroDesdeFicheroManager.getInstance().comprobarReservados(ficheroVO,params); 
						if (noReservados!=null){ 
							if (noReservados.size()==0){												
                                ficheroVO.setAtributo("codUOR", request.getParameter("codUOR"));//Cogemos la UOR indicada en la jsp
								Vector registrosNoIns = RegistroDesdeFicheroManager.getInstance().insertRegistrosDesdeFichero(ficheroVO,params);
								registroForm.setRegistros(registrosNoIns);
								registroForm.setRespOpcion("ficheroProcesado");
							} else { // Alguno no esta reservado
								registroForm.setRespOpcion("numerosNoReservados");
								registroForm.setRegistros(noReservados);
							}					
						}
					}else { // El numero de reservas no es igual al numero de lineas del fichero
						registroForm.setRespOpcion("numerosReservadosDistintoLineasFichero");
					}
					File file = new File(filePath);
					file.delete();
        			
	  			}
      				

			}else {			
				if(m_Log.isDebugEnabled()) m_Log.debug("Cualquier otra opcion....");
			}
    		    
		} // Fin hay usuario y sesion...		
  		/* Redirigimos al JSP de salida */
  		return (mapping.findForward(opcion));
  		
	}
	
	private int upLoadFicheroINE(GeneralValueObject gVO){
		String fichero = (String)gVO.getAtributo("fichero");
		byte[] datos = (byte[])gVO.getAtributo("datos");
		int correcto = -1;
		try{
	  		File file = new File(fichero);
	  		FileOutputStream fOS = new FileOutputStream(fichero);
	  		DataOutputStream dataOS = new DataOutputStream(fOS);
	  		dataOS.write(datos);
	  		fOS.close();
	  		correcto = 0;
		}catch(Exception e){
	  		e.printStackTrace();
	  		correcto = -1;
		}
		return correcto;
  	}
  	
	private Vector leerFicheroINE(GeneralValueObject gVO){
		String fichero = (String)gVO.getAtributo("fichero");
		Vector resultado = new Vector();
		try{
		  File file = new File(fichero);
		  FileInputStream fIS = new FileInputStream(fichero);
		  BufferedReader bufferR = new BufferedReader(new InputStreamReader(fIS));
		  String linea = new String();
		  while((linea = bufferR.readLine())!=null){
			resultado.add(linea);
		  }
		  fIS.close();
		}catch(Exception e){
		  e.printStackTrace();
		}
		return resultado;
	  }
  
  
}
