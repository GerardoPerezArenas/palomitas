package es.altia.agora.interfaces.user.web.integracionsw;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.gestionInformes.persistence.CampoValueObject;
import es.altia.agora.business.integracionsw.InfoConfTramSWVO;
import es.altia.agora.business.integracionsw.ParametroConfigurableVO;
import es.altia.agora.business.integracionsw.persistence.DefinicionOperacionesSWManager;
import es.altia.agora.business.integracionsw.persistence.DefinicionSWTramitacionManager;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.util.exceptions.InternalErrorException;

public class ConfiguracionSWTramiteAction extends ActionSession {


    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws IOException, ServletException {

        if (m_Log.isDebugEnabled()) m_Log.debug("ENTRAMOS EN EL ACTION DE ConfiguracionSWTramite");

        // Recuperamos el usuario y los datos de conexion.
        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        String[] params = usuario.getParamsCon();
        
        // Recuperamos el formulario asociado.
        if (form == null) {
            form = new ConfiguracionSWTramiteForm();
            if ("request".equals(mapping.getScope())) request.setAttribute(mapping.getAttribute(), form);
            else session.setAttribute(mapping.getAttribute(), form);
        }
//        DefinicionTramitesForm defTramForm = (DefinicionTramitesForm) form;
        ConfiguracionSWTramiteForm confSWTramForm = (ConfiguracionSWTramiteForm) form;
        // Recuperamos la opcion de la request.
        String opcion = request.getParameter("opcion");
        
        if (m_Log.isDebugEnabled()) m_Log.debug("LA OPCION EN EL ACTION ES " + opcion);

        if (opcion.equals("inicioConfiguracion")) {

            // Leer parametros.
            String strCodSW = request.getParameter("codSW");
            if (strCodSW!=null) session.setAttribute("codSW", strCodSW);
            else strCodSW = (String) session.getAttribute("codSW");
            m_Log.debug(strCodSW);
            long codigoSW = Long.parseLong(strCodSW);

            // Recuperar los diferentes datos de las tablas.
            try {
            	confSWTramForm.setCfo(codigoSW);
            	
            	int codOp = DefinicionOperacionesSWManager.getInstance().getCodigoOp(codigoSW, params);
            	confSWTramForm.setCodigoOperacion(codOp);
            	
            	String titOp = DefinicionOperacionesSWManager.getInstance().getTituloOp(codOp, params);
            	confSWTramForm.setTituloOperacion(titOp);
            	
            	Vector paramsEntrada = DefinicionOperacionesSWManager.getInstance().getParamsEntrada(codigoSW,codOp, params);            	
            	confSWTramForm.setListaParamsEntrada(paramsEntrada);
            	
            	
            	
            	Vector paramsSalida = DefinicionOperacionesSWManager.getInstance().getParamsSalida(codigoSW,codOp, params);
            	confSWTramForm.setListaParamsSalida(paramsSalida);
            	
                session.setAttribute("listaParamsE",paramsEntrada);
                session.setAttribute("listaParamsS",paramsSalida);
                
            	InfoConfTramSWVO info = DefinicionOperacionesSWManager.getInstance().getConfVO(codigoSW,params);
            	int codMunicipio = info.getCodMunicipio();
            	String strCodMunicipio = (new Integer(codMunicipio)).toString();
            	int codigoTramite = info.getCodigoTramite();
            	String strCodigoTramite = (new Integer(codigoTramite)).toString();            	
            	confSWTramForm.setCodMunicipio(strCodMunicipio);
            	confSWTramForm.setCodigoTramite(strCodigoTramite);
            	confSWTramForm.setTxtCodigo(info.getTxtCodigo());
            	confSWTramForm.setIsObligatoria(info.isObligatorio());
            	
            	
           
            	
            	m_Log.debug(confSWTramForm);
            	
//                DefinicionOperacionesSWManager defManager = DefinicionOperacionesSWManager.getInstance();
//
//                InfoConfTramSWVO infoServicioWeb;
//                if (avanzar) infoServicioWeb = defTramForm.getInfoSWAvanzar();
//                else infoServicioWeb = defTramForm.getInfoSWRetroceder();
//
//                if (infoServicioWeb == null) {
//                    infoServicioWeb = new InfoConfTramSWVO();
//                    infoServicioWeb.setCodOpSW(-1);
//                }
//                
//                if (infoServicioWeb.getCodOpSW() != codigoOp) {
//                    infoServicioWeb.setCodOpSW(codigoOp);
//                    infoServicioWeb.setObligatorio(false);
//                    GeneralValueObject gVO = defManager.getInfoGeneralOperacion(codigoOp, params);
//                    infoServicioWeb.setTituloOperacion((String)gVO.getAtributo("tituloOp"));
//                    Vector paramsIn = defManager.getParamsEntradaTramitacion(codigoOp, params);
//                    infoServicioWeb.setListaParamsEntrada(paramsIn);
//                    Vector paramsOut = defManager.getParamsSalidaTramitacion(codigoOp, params);
//                    infoServicioWeb.setListaParamsSalida(paramsOut);
//                } 
//
//                defTramForm.setInfoSWActual(infoServicioWeb);
//                m_Log.debug(infoServicioWeb.getListaParamsEntrada());
//                defTramForm.setAvanzarActual(avanzar);
            	request.setAttribute("opcion", "inicioConfiguracion");
            } catch (InternalErrorException e) {
                e.printStackTrace();
            }

            
        }

        if (opcion.equals("guardarCambios")) {
//            InfoConfTramSWVO infoServicioWeb = defTramForm.getInfoSWActual();
//            session.setAttribute("infoServicioWeb", infoServicioWeb);
//            session.setAttribute("avanzar", Boolean.toString(defTramForm.isAvanzarActual()));
        	request.setAttribute("opcion", "guardarCambios");
        	String strLpe = request.getParameter("valYTiposE");
        	String strLps = request.getParameter("valoresS");

        	


        	
        	String[] tokensEntrada = strLpe.split(",");
        	String[] tokensSalida = strLps.split(",");
        	
//        	StringTokenizer strtokE = new StringTokenizer(strLpe , ",");
//        	StringTokenizer strtokS = new StringTokenizer(strLps , ",");

        	boolean relleno = true;
        	
        	
        	Vector paramsEntrada = (Vector) session.getAttribute("listaParamsE");
            Vector paramsSalida = (Vector) session.getAttribute("listaParamsS");
            m_Log.debug(strLpe);
            m_Log.debug("tokensentrada: " + tokensEntrada);
            paramsEntrada = actualizaParamsEntrada(paramsEntrada,tokensEntrada);
        	paramsSalida = actualizaParamsSalida(paramsSalida,tokensSalida);
     	
//            paramsEntrada = actualizaParamsEntrada2(paramsEntrada,strtokE);
//        	paramsSalida = actualizaParamsSalida2(paramsSalida,strtokS);
        	

            session.setAttribute("listaParamsE", paramsEntrada);
            session.setAttribute("listaParamsS", paramsSalida);
            
            confSWTramForm.setListaParamsEntrada(paramsEntrada);
            confSWTramForm.setListaParamsSalida(paramsSalida);
        	
        	
        	int ob =0 ;
        	if (confSWTramForm.getIsObligatoria()){
        		ob = 1;
        	}        
        	

        	
        	m_Log.debug(paramsEntrada);
            m_Log.debug(paramsSalida);
            for(Iterator it = paramsEntrada.iterator();it.hasNext();){
            	ParametroConfigurableVO p = (ParametroConfigurableVO) it.next();
            	int obligatorio = p.getObligatorio();
            	String codCampoExp = p.getCodCampoExp();
            	String valorConstante = p.getValorConstante();

        		if ((obligatorio == 1)&& ("".equals(codCampoExp))&& "".equals(valorConstante)) {
        			relleno = false;
        			break;
        		}
        		m_Log.debug("dentro del bucle: " + relleno);
            	
            }

            if (relleno) {
	            try {
	            	DefinicionOperacionesSWManager.getInstance().setObligatoria(confSWTramForm.getCfo(),ob,params);
					DefinicionOperacionesSWManager.getInstance().grabaParametros(paramsEntrada, paramsSalida,params);
				} catch (InternalErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setAttribute("ConfiguracionSWTramiteForm", confSWTramForm);
				request.removeAttribute("errorOb");
				session.removeAttribute("codSW");
	            session.removeAttribute("listaParamsE");
	            session.removeAttribute("listaParamsS");
            }
            else {
            	m_Log.debug("entra en el else");
            	request.setAttribute("errorOb", "FALTA ALGUN PARAMETRO OBLIGATORIO");
            	
            }
            
        }

        if (opcion.equals("modificarParametro")) {

            String strIndex = request.getParameter("selectedIndex");
            String strEsEntrada = request.getParameter("esEntrada");
            String strTipo = request.getParameter("tipo");
            String strValor = request.getParameter("valor");
            int index = Integer.parseInt(strIndex);
            boolean esEntrada = Boolean.parseBoolean(strEsEntrada);
            Vector paramsEntrada = (Vector) session.getAttribute("listaParamsE");
            Vector paramsSalida = (Vector) session.getAttribute("listaParamsS");

            m_Log.debug("Tipo: " + strTipo);
            m_Log.debug("Valor: " + strValor);

            ParametroConfigurableVO param;
            if (esEntrada) {
                param = (ParametroConfigurableVO) paramsEntrada.get(index);
            } else {
                param = (ParametroConfigurableVO) paramsSalida.get(index);
                param.setCodCampoExp(strValor);
            }

            request.setAttribute("parametro", param);

            Vector<CampoValueObject> campos = null;
            DefinicionSWTramitacionManager defManager = DefinicionSWTramitacionManager.getInstance();
            int codMunicipio = Integer.parseInt(confSWTramForm.getCodMunicipio());
            String codProcedimiento = confSWTramForm.getTxtCodigo();
            int codTramite = Integer.parseInt(confSWTramForm.getCodigoTramite());

            try {
                if (esEntrada) campos = defManager.getListaCampos(codMunicipio, codProcedimiento, true, 0, params);
                else campos = defManager.getListaCampos(codMunicipio, codProcedimiento, false, codTramite, params);
            } catch (InternalErrorException iee) {
                iee.printStackTrace();
            }

            if (esEntrada) {
                request.setAttribute("campos", campos);
            } else {
                Vector<CampoValueObject> camposSalida = new Vector<CampoValueObject>();
                for (CampoValueObject campo : campos) {
                    String tablaCampo = campo.getTabla();
                    if (!tablaCampo.equals("V_CRO") && !tablaCampo.equals("INT")) {
                        if (tablaCampo.length() == 6) {
                            String codigoCampo = campo.getOrigen();
                            String[] datosCampo = codigoCampo.split("_");
                            codTramite = Integer.parseInt(datosCampo[1]);
                            if (codTramite == Integer.parseInt(confSWTramForm.getCodigoTramite()))
                                camposSalida.add(campo);
                        } else camposSalida.add(campo);
                    }
                }
                request.setAttribute("campos", camposSalida);
            }

            request.setAttribute("selectedIndex", strIndex);
            session.setAttribute("selectedIndex", strIndex);
            session.setAttribute("esEntrada", strEsEntrada);
            session.setAttribute("parametro", param);
            m_Log.debug(" | " + param.getCodCampoExp() + " | ");
        }

        if (opcion.equals("guardarCambiosParametro")) {
            String strCodTipo = request.getParameter("codigoTipoPaso");
            int codTipo = Integer.parseInt(strCodTipo);
            String strIndex = (String)session.getAttribute("selectedIndex");
            int index = Integer.parseInt(strIndex);
            String strEsEntrada = (String)session.getAttribute("esEntrada");
            boolean esEntrada = Boolean.parseBoolean(strEsEntrada);     

        	Vector paramsEntrada = (Vector) session.getAttribute("listaParamsE");
            Vector paramsSalida = (Vector) session.getAttribute("listaParamsS");
            

            ParametroConfigurableVO param = new ParametroConfigurableVO();
            
            if (esEntrada) {
            	param = (ParametroConfigurableVO)paramsEntrada.get(index);
            }
            else {
            	param = (ParametroConfigurableVO)paramsSalida.get(index);
            }
            if (codTipo == 1) {
                String codigoCampo = request.getParameter("descTipo");
                param.setTipoValorPaso(codTipo);
                param.setCodCampoExp(codigoCampo);
                param.setValorConstante("");
            } else {
                String valorConstante = request.getParameter("constante");
                param.setTipoValorPaso(codTipo);
                param.setValorConstante(valorConstante);
                param.setCodCampoExp("");
            }

          m_Log.debug(paramsEntrada);  
          m_Log.debug("Ahora los de salida: " + paramsSalida);
          if (esEntrada) paramsEntrada.setElementAt(param, index);
          else paramsSalida.setElementAt(param, index);
          
          confSWTramForm.setListaParamsSalida(paramsSalida);
          confSWTramForm.setListaParamsEntrada(paramsEntrada);
//          session.setAttribute("listaParamsE",paramsEntrada);
//          session.setAttribute("listaParamsS",paramsSalida);
          session.removeAttribute("selectedIndex");
          session.removeAttribute("esEntrada");
          request.removeAttribute("parametro");
          m_Log.debug("index: " + index);
          m_Log.debug(confSWTramForm);
        }


        
        return mapping.findForward(opcion);
    }
    
    private Vector actualizaParamsEntrada (Vector paramsEntrada, String[] tokensEntrada) {
    	int cont =0;
    	int indexParam = 0;
    	Iterator itIn = paramsEntrada.iterator();
    	while (cont<tokensEntrada.length) {
    		ParametroConfigurableVO param = (ParametroConfigurableVO)itIn.next();
		
    		String valor = tokensEntrada[cont++];
    		String strtipo = tokensEntrada[cont++];
    		

    		m_Log.debug("Valor: " + valor + "\n");        		
    		m_Log.debug("Tipo: " +  strtipo + "\n");
    		
    		int tipo = Integer.parseInt(strtipo);
    		if (tipo==0) {
    			param.setCodCampoExp("");
    			param.setValorConstante(valor);
    		} else {
    			param.setCodCampoExp(valor);
    			param.setValorConstante("");        			
    		}
//    		if ((param.getObligatorio()==1) && "".equals(param.getCodCampoExp()) && "".equals(param.getValorConstante())) {
//    			relleno = false;
//    		}
    		param.setTipoValorPaso(tipo);
    		paramsEntrada.setElementAt(param, indexParam);
    		indexParam++;
		
    	}
    	return paramsEntrada;
    }
    
    private Vector actualizaParamsEntrada2 (Vector paramsEntrada, StringTokenizer strtokE) {
    	
    	int indexParam = 0;
    	Iterator itIn = paramsEntrada.iterator();
    	for(StringTokenizer strtok=strtokE;strtok.hasMoreTokens();) {
    		ParametroConfigurableVO param = (ParametroConfigurableVO)itIn.next();
		
    		String valor = strtok.nextToken();
    		
    		String strtipo = strtok.nextToken();
    		
    		

    		m_Log.debug("Valor: " + valor + "\n");        		
    		m_Log.debug("Tipo: " +  strtipo + "\n");
    		
    		int tipo = Integer.parseInt(strtipo);
    		if (tipo==1) {
    			param.setCodCampoExp(valor);
    			param.setValorConstante("");        			
    		}
    		else {
    			param.setCodCampoExp("");
    			param.setValorConstante(valor);
    		}
//    		if ((param.getObligatorio()==1) && "".equals(param.getCodCampoExp()) && "".equals(param.getValorConstante())) {
//    			relleno = false;
//    		}
    		param.setTipoValorPaso(tipo);
    		paramsEntrada.setElementAt(param, indexParam);
    		indexParam++;
		
    	}
    	return paramsEntrada;
    }
    private Vector actualizaParamsSalida (Vector paramsSalida, String[] tokensSalida) {
		Iterator itOut = paramsSalida.iterator();
	
        int cont = 0;
		while (cont<tokensSalida.length) {
            if(itOut.hasNext()){
                ParametroConfigurableVO param = (ParametroConfigurableVO)itOut.next();
                m_Log.debug("\n\n");

                String valor = tokensSalida[cont++];
                m_Log.debug("Valor: " + valor + "\n");


                param.setCodCampoExp(valor);
                paramsSalida.setElementAt(param, cont-1);
            } else cont++;
		}

		return paramsSalida;
    }
    private Vector actualizaParamsSalida2 (Vector paramsSalida, StringTokenizer strtokS) {
		Iterator itOut = paramsSalida.iterator();
		
	
	    int cont = 0;
	    for(StringTokenizer strtok=strtokS;strtok.hasMoreTokens();) {
			ParametroConfigurableVO param = (ParametroConfigurableVO)itOut.next();
			m_Log.debug("\n\n");
			
			String valor = strtok.nextToken();
			cont++;
			m_Log.debug("Valor: " + valor + "\n");   
			
		
			param.setCodCampoExp(valor);       			       			
			paramsSalida.setElementAt(param, cont-1);   
			
		}
		return paramsSalida;
    }    
}
