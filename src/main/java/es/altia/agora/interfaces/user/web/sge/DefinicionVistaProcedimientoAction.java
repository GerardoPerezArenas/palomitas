package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.sge.DefinicionAgrupacionCamposValueObject;
import es.altia.agora.business.sge.DefinicionCampoValueObject;
import es.altia.agora.interfaces.user.web.helper.ActionHelper;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author david.caamano
 */
public class DefinicionVistaProcedimientoAction extends ActionSession {

    private static Logger log = Logger.getLogger(DefinicionVistaProcedimientoAction.class);
    
    @Override
    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        if(log.isDebugEnabled()) log.debug("performSession() : BEGIN");
        ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));

        // Validaremos los parametros del request especificados
        HttpSession session = request.getSession();
        String opcion ="";
        
        if ((session.getAttribute("usuario") != null)) {
            // Si usuario en sesion es nulo --> error.
            ActionErrors errors = new ActionErrors();

            DefinicionCampoVistaForm defCampoVistaform = null;
            Vector listaCampos = new Vector();
            Vector listaAgrupaciones = new Vector();
            if (form == null) {
                if(log.isDebugEnabled()) log.debug("Rellenamos el form de Definicion de Campo");
                form = new DefinicionCampoVistaForm();
                if ("request".equals(mapping.getScope()))
                    request.setAttribute(mapping.getAttribute(), form);
                else
                    session.setAttribute(mapping.getAttribute(), form);
            }//if (form == null) 
            defCampoVistaform = (DefinicionCampoVistaForm)form;

            opcion = request.getParameter("opcion");
            if(log.isDebugEnabled()) log.debug("la opcion en el action es " + opcion);
            
              if(opcion.equals("guardarDatosVistaSesion")){
                
                String codCampo = request.getParameter("codCampo");
                String descCampo = request.getParameter("descCampo");
                String codTipoDato = request.getParameter("codTipoDato");
                String campoActivo = request.getParameter("campoActivo");
                String posX = request.getParameter("posX");
                String posY = request.getParameter("posY");
                String agrupacionCampo = request.getParameter("agrupacionCampo");
                String codsAgrupacion = request.getParameter("codAgrupaciones");
                String descAgrupacion = request.getParameter("descAgrupaciones");
                String ordenAgrupacion = request.getParameter("ordenAgrupaciones");
                String oculto=request.getParameter("oculto");
                String tamano=request.getParameter("tamano");
                 if(log.isDebugEnabled()) log.debug("tamano-- " + tamano);
                
                try{
                    Vector codigosCampos = splitListasSeparadorAlmohadilla(codCampo);
                    Vector descsCampos = splitListasSeparadorAlmohadilla(descCampo);
                    Vector codsTipoDato = splitListasSeparadorAlmohadilla(codTipoDato);
                    Vector camposActivos = splitListasSeparadorAlmohadilla(campoActivo);
                    Vector posicionesX = splitListasSeparadorAlmohadilla(posX);
                    Vector posicionesY = splitListasSeparadorAlmohadilla(posY);
                    Vector codAgrupaciones = splitListasSeparadorAlmohadilla(codsAgrupacion);
                    Vector descAgrupaciones = splitListasSeparadorAlmohadilla(descAgrupacion);
                    Vector ordenAgrupaciones = splitListasSeparadorAlmohadilla(ordenAgrupacion);
                    Vector ocultos = splitListasSeparadorAlmohadilla(oculto);
                    Vector tamanos = splitListasSeparadorAlmohadilla(tamano);
                      if(log.isDebugEnabled()) log.debug("tamanos  "+tamanos);
                    Vector agrupacionesCampos = splitListasSeparadorAlmohadilla(agrupacionCampo);                    
                    String codigo ="";
                    
                    if(codigosCampos.size()==0){
                        // No hay campos suplementarios
                        codigo = "2";
                    }else
                    if(codigosCampos.size()==agrupacionesCampos.size()){
                        // Todos los campos tienen una agrupación asociada
                        
                        borrarVariablesSesion(session);                      
                        
                        session.setAttribute("CODIGOS_CAMPOS_SUPLEMENTARIOS",codigosCampos);
                        session.setAttribute("DESCRIPCIONES_CAMPOS_SUPLEMENTARIOS",descsCampos);
                        session.setAttribute("CODIGOS_TIPO_DATO_CAMPOS_SUPLEMENTARIOS",codsTipoDato);
                        session.setAttribute("CODIGOS_CAMPOS_ACTIVOS_SUPLEMENTARIOS",camposActivos);
                        session.setAttribute("POSICIONES_X_CAMPOS_SUPLEMENTARIOS",posicionesX);
                        session.setAttribute("POSICIONES_Y_CAMPOS_SUPLEMENTARIOS",posicionesY);
                        session.setAttribute("AGRUPACIONES_CAMPOS_SUPLEMENTARIOS",agrupacionesCampos);                                                
                        session.setAttribute("CODIGOS_AGRUPACIONES_CAMPOS_SUPLEMENTARIOS",codAgrupaciones);                        
                        session.setAttribute("DESCRIPCIONES_AGRUPACIONES_CAMPOS_SUPLEMENTARIOS",descAgrupaciones);                        
                        session.setAttribute("ORDEN_AGRUPACIONES_CAMPOS_SUPLEMENTARIOS",ordenAgrupaciones); 
                        session.setAttribute("CAMPOS_OCULTOS",ocultos);    
                         session.setAttribute("CAMPOS_TAMANOS",tamanos); 
                        codigo= "0";
                    }else
                    if(codigosCampos.size()>agrupacionesCampos.size()){
                        codigo="1";
                    }
                    
                    PrintWriter out =response.getWriter();
                    response.setContentType("text/html");
                    out.println(codigo);                    
                    out.flush();
                    out.close();
                    
                    
                }catch(Exception e){
                    e.printStackTrace();
                }
                
            }else            
            if (opcion.equals("inicio")){
                               
               Vector codigosCampos = (Vector)session.getAttribute("CODIGOS_CAMPOS_SUPLEMENTARIOS");
               Vector descsCampos   = (Vector)session.getAttribute("DESCRIPCIONES_CAMPOS_SUPLEMENTARIOS");
               Vector codsTipoDato  = (Vector)session.getAttribute("CODIGOS_TIPO_DATO_CAMPOS_SUPLEMENTARIOS");
               Vector camposActivos = (Vector)session.getAttribute("CODIGOS_CAMPOS_ACTIVOS_SUPLEMENTARIOS");
               Vector posicionesX   = (Vector)session.getAttribute("POSICIONES_X_CAMPOS_SUPLEMENTARIOS");
               Vector posicionesY   = (Vector)session.getAttribute("POSICIONES_Y_CAMPOS_SUPLEMENTARIOS");
               Vector agrupacionesCampos = (Vector)session.getAttribute("AGRUPACIONES_CAMPOS_SUPLEMENTARIOS");      
               
               Vector listaCodsAgrupacion = (Vector)session.getAttribute("CODIGOS_AGRUPACIONES_CAMPOS_SUPLEMENTARIOS");                        
               Vector listaDescAgrupacion = (Vector)session.getAttribute("DESCRIPCIONES_AGRUPACIONES_CAMPOS_SUPLEMENTARIOS");                        
               Vector listaOrdenAgrupacion = (Vector)session.getAttribute("ORDEN_AGRUPACIONES_CAMPOS_SUPLEMENTARIOS"); 
               Vector listaOcultos = (Vector)session.getAttribute("CAMPOS_OCULTOS"); 
               Vector listaTamanos = (Vector)session.getAttribute("CAMPOS_TAMANOS"); 
               
            
                
                    
                if(codigosCampos.size() > agrupacionesCampos.size()){
                    //Devolvemos un error ya que hay campos sin agrupacion.
                    defCampoVistaform.setErrorCargando(true);
                }else{
                    Boolean existeAgrupacionDEF = false;
                    for(int i = 0; i<codigosCampos.size(); i++){
                        DefinicionCampoValueObject defCampoVO = new DefinicionCampoValueObject();
                            defCampoVO.setCodCampo((String)codigosCampos.get(i));
                            defCampoVO.setDescCampo((String)descsCampos.get(i));
                            defCampoVO.setCodTipoDato((String)codsTipoDato.get(i));
                            String codAgrupacion = (String)agrupacionesCampos.get(i);
                            String tamano = (String)listaTamanos.get(i);
                            defCampoVO.setCodAgrupacion(codAgrupacion);
                            if(codAgrupacion.equalsIgnoreCase("DEF")){
                                existeAgrupacionDEF = true;
                            }//if(codAgrupacion.equalsIgnoreCase("DEF"))
                            String tipoDato = (String)codsTipoDato.get(i);
                            if(tipoDato != null && !"".equalsIgnoreCase(tipoDato) && ("1".equalsIgnoreCase(tipoDato) 
                                    || "3".equalsIgnoreCase(tipoDato)
                                    || "8".equalsIgnoreCase(tipoDato) 
                                    || "9".equalsIgnoreCase(tipoDato))){
                                defCampoVO.setTamanho("50");
                            }else   if(tipoDato != null && !"".equalsIgnoreCase(tipoDato) && ("2".equalsIgnoreCase(tipoDato) || "6".equalsIgnoreCase(tipoDato))) {
                                
                                try {
                                    if (Integer.parseInt(tamano) > 50) {
                                        defCampoVO.setTamanho("100");
                                    } else {
                                        defCampoVO.setTamanho("50");
                                    }
                                } catch (Exception e) {
                                    defCampoVO.setTamanho("100");
                                }
                            }else
                            {
                                defCampoVO.setTamanho("100");
                            }
                            if(tipoDato != null && !"".equalsIgnoreCase(tipoDato) && ("4").equalsIgnoreCase(tipoDato)){
                                defCampoVO.setAlto(("140"));
                            }else{
                                defCampoVO.setAlto(("30"));
                            }//if(tipoDato != null && !"".equalsIgnoreCase(tipoDato) && ("4").equalsIgnoreCase(tipoDato))                                   
                            defCampoVO.setPosX((String) posicionesX.get(i));
                            defCampoVO.setPosY((String) posicionesY.get(i));
                            defCampoVO.setActivo((String) camposActivos.get(i));
                            defCampoVO.setOculto((String) listaOcultos.get(i));
                            defCampoVO.setTamano((String) listaTamanos.get(i));
                        listaCampos.add(defCampoVO);
                    }//for(int i = 0; i<codigosCampos.size(); i++)

                    if(existeAgrupacionDEF){
                        DefinicionAgrupacionCamposValueObject agrupacionCampoVO = new DefinicionAgrupacionCamposValueObject();
                        agrupacionCampoVO.setCodAgrupacion("DEF");
                        agrupacionCampoVO.setDescAgrupacion(" ");
                        agrupacionCampoVO.setOrdenAgrupacion(0);
                        listaAgrupaciones.add(agrupacionCampoVO);
                    }//if(existeAgrupacionDEF)
                    
                                for(int i = 0; i < listaCodsAgrupacion.size(); i++){
                                    DefinicionAgrupacionCamposValueObject agrupacionCampoVO = new DefinicionAgrupacionCamposValueObject();
                                        agrupacionCampoVO.setCodAgrupacion((String)listaCodsAgrupacion.get(i));
                                        agrupacionCampoVO.setDescAgrupacion((String)listaDescAgrupacion.get(i));
                                        agrupacionCampoVO.setOrdenAgrupacion(Integer.valueOf((String)listaOrdenAgrupacion.get(i)));
                                    listaAgrupaciones.add(agrupacionCampoVO);
                                }//for(int i = 0; i < listaCodsAgrupacion.size(); i++)
                           
                    defCampoVistaform.setListaCampos(listaCampos);
                    defCampoVistaform.setAgrupacionesCampos(listaAgrupaciones);
                    defCampoVistaform.setErrorCargando(false);
                }//if (opcion.equals("inicio"))
                
                 borrarVariablesSesion(session);         
            }
               
                          
        }else{
            if(log.isDebugEnabled()) log.debug("No hay usuario");
            opcion = "no_usuario";
        }
        if(log.isDebugEnabled()) log.debug("performSession() : END");
        return (mapping.findForward(opcion));
    }//performSession
    
    private Vector splitListas(String listaEntrada) {
        if(log.isDebugEnabled()) log.debug("splitListas() : BEGIN");
        Vector lista = new Vector();
        StringTokenizer valores = null;
        if (listaEntrada != null) {
            valores = new StringTokenizer(listaEntrada,"§¥",false);
            while (valores.hasMoreTokens()) {
                String valor = valores.nextToken();
                lista.addElement(valor);
            }//while (valores.hasMoreTokens())        
        }//if (listaEntrada != null) 
        if(log.isDebugEnabled()) log.debug("splitListas() : END");
        return lista;
    }//splitListas
    
    
    private Vector splitListasSeparadorAlmohadilla(String listaEntrada) {
        if(log.isDebugEnabled()) log.debug("splitListasSeparadorAlmohadilla() : BEGIN");
        Vector lista = new Vector();
        StringTokenizer valores = null;
        if (listaEntrada != null) {
            valores = new StringTokenizer(listaEntrada,"##",false);
            while (valores.hasMoreTokens()) {
                String valor = valores.nextToken();
                lista.addElement(valor);
            }//while (valores.hasMoreTokens())        
        }//if (listaEntrada != null) 
        if(log.isDebugEnabled()) log.debug("splitListasSeparadorAlmohadilla() : END");
        return lista;
    }//splitListas
    
    
    private void borrarVariablesSesion(HttpSession session){
        session.removeAttribute("CODIGOS_CAMPOS_SUPLEMENTARIOS");
        session.removeAttribute("DESCRIPCIONES_CAMPOS_SUPLEMENTARIOS");
        session.removeAttribute("CODIGOS_TIPO_DATO_CAMPOS_SUPLEMENTARIOS");
        session.removeAttribute("CODIGOS_CAMPOS_ACTIVOS_SUPLEMENTARIOS");
        session.removeAttribute("POSICIONES_X_CAMPOS_SUPLEMENTARIOS");
        session.removeAttribute("POSICIONES_Y_CAMPOS_SUPLEMENTARIOS");
        session.removeAttribute("AGRUPACIONES_CAMPOS_SUPLEMENTARIOS");
        session.removeAttribute("CODIGOS_AGRUPACIONES_CAMPOS_SUPLEMENTARIOS");                        
        session.removeAttribute("DESCRIPCIONES_AGRUPACIONES_CAMPOS_SUPLEMENTARIOS");                        
        session.removeAttribute("ORDEN_AGRUPACIONES_CAMPOS_SUPLEMENTARIOS");
        session.removeAttribute("CAMPOS_OCULTOS"); 
    }
    
}//class


