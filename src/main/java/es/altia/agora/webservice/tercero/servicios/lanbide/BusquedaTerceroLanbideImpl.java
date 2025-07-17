package es.altia.agora.webservice.tercero.servicios.lanbide;

import es.altia.agora.webservice.tercero.FachadaBusquedaTercero;
import es.altia.agora.webservice.tercero.FachadaSGETercero;

import es.altia.agora.webservice.tercero.servicios.lanbide.cliente.LangaiEECCServiceLocator;
import es.altia.agora.webservice.tercero.servicios.lanbide.cliente.LangaiEECC;
import es.altia.agora.webservice.tercero.servicios.lanbide.cliente.EntidadesComunesWSValueObject;
import es.altia.agora.webservice.tercero.servicios.lanbide.cliente.PersonaFisicaWSValueObject;


import es.altia.agora.webservice.tercero.exception.EjecucionBusquedaTerceroException;
import es.altia.agora.webservice.tercero.exception.DemasiadosResultadosBusquedaTerceroException;
import es.altia.agora.business.terceros.CondicionesBusquedaTerceroVO;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.persistence.manual.DatosSuplementariosTerceroManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.webservice.tercero.servicios.lanbide.cliente.EmpresarioWSValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import java.lang.reflect.Method;

import java.util.Vector;
import java.util.StringTokenizer;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;
import es.altia.agora.technical.EstructuraCampo;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.Logger;

public class BusquedaTerceroLanbideImpl implements FachadaBusquedaTercero {

    private String nombreServicio;
    private String prefijoPropiedad;
    private String[] paramsConexionBD;
    private Logger m_Log = Logger.getLogger(BusquedaTerceroLanbideImpl.class);
    private FachadaSGETercero fachadaSGE = new FachadaSGETercero();

    private static String FICHERO_TERCEROS_LANBIDE = "pluginTercerosLanbide";


    private static String PREFIX_TIPO_DOCUMENTO_SGE = "TipoDocumento/SGE/";
    private static String PREFIX_TIPO_DOCUMENTO_LANBIDE = "TipoDocumento/LANBIDE/";
    private static String PREFIX_PAISES_LANBIDE = "Paises/LANBIDE/";
    private static String PREFIX_COD_PORTAL_LANBIDE = "CodigoPortal/LANBIDE/";

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public String getPrefijoPropiedad() {
        return prefijoPropiedad;
    }

    public void setPrefijoPropiedad(String prefijoPropiedad) {
        this.prefijoPropiedad = prefijoPropiedad;
    }

    public Vector getTercero(CondicionesBusquedaTerceroVO condsBusqueda, String[] params) throws EjecucionBusquedaTerceroException,DemasiadosResultadosBusquedaTerceroException {
        
        paramsConexionBD = params;
        Config m_ConfigTerceros = ConfigServiceHelper.getConfig("Terceros");

        Config m_propertiesLanbide = ConfigServiceHelper.getConfig(FICHERO_TERCEROS_LANBIDE);
        

        String strUrlEndPoint = m_ConfigTerceros.getString(prefijoPropiedad + "urlEndPoint");
        int codOrganizacion = condsBusqueda.getCodOrganizacion();
        String doi = condsBusqueda.getDocumento();
        String nombre = condsBusqueda.getNombre();
        String apellido1 = condsBusqueda.getApellido1();
        String apellido2 = condsBusqueda.getApellido2();
        int tipoDocumento = condsBusqueda.getTipoDocumento();
		String tipoDoc = "";   
		tipoDoc =  docSGEtoDocLanbide(String.valueOf(tipoDocumento));
        try {
            URL urlEndPoint = new URL(strUrlEndPoint);
            LangaiEECCServiceLocator terceroLocator = new LangaiEECCServiceLocator();
            LangaiEECC terceroService = terceroLocator.getLangaiEECC(urlEndPoint);

            
            String codigoCentro=m_propertiesLanbide.getString("codigoCentro");
            String ubicacion=m_propertiesLanbide.getString("ubicacion");

            Vector resultadoSW=new Vector();
            Vector resultadoSW2=new Vector();

            if((!"".equals(doi)) && (doi != null))
            {
                //Busca persona fisica
                resultadoSW = terceroService.consultaPF_Empresario(codigoCentro, ubicacion, tipoDoc, doi, "", "", "", "", "", "", "");
                //Busca empresario
                resultadoSW2 = terceroService.consultaPF_Empresario(codigoCentro, ubicacion, "", "", "", "", "", "", doi, "", "");

                return unirVectores(transformarTerceroArraySW(codOrganizacion,resultadoSW,params),transformarTerceroArraySW(codOrganizacion,resultadoSW2,params));

            }
            else {
                if((("".equals(apellido1))||(apellido1==null))&&(("".equals(apellido2))||(apellido2==null))&&((!"".equals(nombre))&&(nombre!=null)))
                {  //Tiene que consultar por razon social
                    resultadoSW = terceroService.consultaPF_Empresario(codigoCentro, ubicacion, "", "", "", "", "", "","" ,(nombre.trim()).replaceAll("\\*", "")  , "");
                    return transformarTerceroArraySW(codOrganizacion,resultadoSW,params);

                }else if(((!"".equals(apellido1))&&(apellido1!=null))&&(((!"".equals(apellido2))&&(apellido2!=null)))&&(((!"".equals(nombre))&&(nombre!=null))))
                {//Consulta por nombre ap1 y ap2
                    resultadoSW = terceroService.consultaPF_Empresario(codigoCentro, ubicacion, "", "", "", (nombre.trim()).replaceAll("\\*", ""), (apellido1.trim()).replaceAll("\\*", ""), (apellido2.trim()).replaceAll("\\*", ""),"" ,"" , "");
                    return transformarTerceroArraySW(codOrganizacion,resultadoSW,params);
                }
                else if(((!"".equals(apellido1))&&(apellido1!=null))&&((("".equals(apellido2))||(apellido2==null)))&&(((!"".equals(nombre))&&(nombre!=null))))
                {//Consulta por nombre ap1
                    resultadoSW = terceroService.consultaPF_Empresario(codigoCentro, ubicacion, "", "", "", (nombre.trim()).replaceAll("\\*", ""), (apellido1.trim()).replaceAll("\\*", ""), "","" ,"" , "");
                    return transformarTerceroArraySW(codOrganizacion,resultadoSW,params);
                }
                else if((("".equals(apellido1))||(apellido1==null))&&(((!"".equals(apellido2))&&(apellido2!=null)))&&(((!"".equals(nombre))&&(nombre!=null))))
                {//Consulta por nombre  y ap2 
                    resultadoSW = terceroService.consultaPF_Empresario(codigoCentro, ubicacion, "", "", "", (nombre.trim()).replaceAll("\\*", ""), "", (apellido2.trim()).replaceAll("\\*", ""),"","","");
                    return transformarTerceroArraySW(codOrganizacion,resultadoSW,params);
                }
                else if(((!"".equals(apellido1))&&(apellido1!=null))&&(((!"".equals(apellido2))&&(apellido2!=null)))&&((("".equals(nombre))||(nombre==null))))
                {//Consulta  ap1 y ap2
                    resultadoSW = terceroService.consultaPF_Empresario(codigoCentro, ubicacion, "", "", "", "", (apellido1.trim()).replaceAll("\\*", ""), (apellido2.trim()).replaceAll("\\*", ""),"" ,"","");
                    return transformarTerceroArraySW(codOrganizacion,resultadoSW,params);
                }
                 else{
                       return resultadoSW; 
                 }
            }

       }  catch (DemasiadosResultadosBusquedaTerceroException de) {
           m_Log.error("Error en la busqueda del teceros-DemasiadosResultadosBusquedaTerceroException: " + de.getMessage(), de);
           throw new DemasiadosResultadosBusquedaTerceroException("DEMASIADOS VALORES", de);
        }  catch (ServiceException se) {
            m_Log.error("Error en la busqueda del teceros-ServiceException: " + se.getMessage(), se);
            throw new EjecucionBusquedaTerceroException("NO SE HA PODIDO REALIZAR LA LLAMADA AL SERVICIO WEB", se);
        } catch (RemoteException re) {
            m_Log.error("Error en la busqueda del teceros-RemoteException: " + re.getMessage(), re);
            throw new EjecucionBusquedaTerceroException("NO SE HA PODIDO REALIZAR LA LLAMADA AL SERVICIO WEB", re);
        } catch (MalformedURLException mue) {
            m_Log.error("Error en la busqueda del teceros-MalformedURLException: " + mue.getMessage(), mue);
            throw new EjecucionBusquedaTerceroException("NO SE HA PODIDO REALIZAR LA LLAMADA AL SERVICIO WEB", mue);
        }catch(Exception ex){
            m_Log.error("Error General en la busqueda del teceros-Exception: " + ex.getMessage(), ex);
            throw new EjecucionBusquedaTerceroException("NO SE HA PODIDO REALIZAR LA LLAMADA AL SERVICIO WEB. " + ex.getMessage(), ex);
        }
    }

    private String docSGEtoDocLanbide(String tipoDocumentoSGE)
    {
        Config m_propertiesLanbide = ConfigServiceHelper.getConfig(FICHERO_TERCEROS_LANBIDE);
        String tipoDoc=null;

        if ((tipoDocumentoSGE!=null)&&(!"".equals(tipoDocumentoSGE))&&(!"-1".equals(tipoDocumentoSGE)))
        {
            try{
                 tipoDoc= m_propertiesLanbide.getString(PREFIX_TIPO_DOCUMENTO_SGE + tipoDocumentoSGE);
            }catch(Exception e)
            {
                return null;
            }
        }

        return tipoDoc;
    }

    private String docLanbidetoDocSge(String tipoDocumentoLanbide)
    {
        Config m_propertiesLanbide = ConfigServiceHelper.getConfig(FICHERO_TERCEROS_LANBIDE);
        String tipoDoc=null;

        if ((tipoDocumentoLanbide!=null)&&(!"".equals(tipoDocumentoLanbide))&&(!"-1".equals(tipoDocumentoLanbide)))
        {
            try{
                 tipoDoc= m_propertiesLanbide.getString(PREFIX_TIPO_DOCUMENTO_LANBIDE + tipoDocumentoLanbide);
            }catch(Exception e)
            {
                tipoDoc=null;
            }
        }

        return tipoDoc;
    }
    private Vector<TercerosValueObject> transformarTerceroArraySW(int codOrganizacion,Vector resultadoSW,String[] params)  throws DemasiadosResultadosBusquedaTerceroException, EjecucionBusquedaTerceroException{

        
        Vector<TercerosValueObject> arrayTerceroSGE = new Vector<TercerosValueObject>();
        TercerosValueObject terVO=new TercerosValueObject();
        if (resultadoSW!=null)
        {
          m_Log.info("Resultado en String - Prueba - : "
                  +  Arrays.toString(resultadoSW.toArray())
          );
          for (int i=0; i<resultadoSW.size();i++) {
              terVO=new TercerosValueObject();
              
              if (resultadoSW.get(i) instanceof String) {
                  m_Log.info(("Respuesta (resultadoSW.get(i)) del WS Busqueda tercero es un String - Se debio presentar un error : " + resultadoSW.get(i)));
                  Exception ex = new Exception("Eror Generico el WS de Consulta de Terceros: " + resultadoSW.get(i));
                  throw new EjecucionBusquedaTerceroException("Eror Generico el WS de Consulta de Terceros : " +ex.getMessage(),ex);
              }
              EntidadesComunesWSValueObject entidadesComunes = (EntidadesComunesWSValueObject)resultadoSW.get(i);
              if("420".equals((String)entidadesComunes.getLista_errores().get(0))) {     
                  throw new DemasiadosResultadosBusquedaTerceroException("Demasiados valorores",null);
              }    
              
              terVO=transformarTerceroSW(codOrganizacion,entidadesComunes,params);
              if(terVO!=null)arrayTerceroSGE.add(terVO);
             
        }
        }
        return arrayTerceroSGE;
       
    }

    private Vector<TercerosValueObject> unirVectores(Vector<TercerosValueObject> resultadoSW1,Vector<TercerosValueObject> resultadoSW2) {

        Vector<TercerosValueObject> arrayTerceroSGE = new Vector<TercerosValueObject>();
        TercerosValueObject terVO=new TercerosValueObject();
        if (resultadoSW1!=null)
        {
          for (int i=0; i<resultadoSW1.size();i++) {
              terVO=new TercerosValueObject();
              terVO=resultadoSW1.get(i);
              if(terVO!=null)arrayTerceroSGE.add(terVO);

        }
        }
        if (resultadoSW2!=null)
        {
          for (int i=0; i<resultadoSW2.size();i++) {
              terVO=new TercerosValueObject();
              terVO=resultadoSW2.get(i);
              if(terVO!=null)arrayTerceroSGE.add(terVO);

        }
        }
        return arrayTerceroSGE;

    }

    private TercerosValueObject transformarTerceroSW(int codOrganizacion,EntidadesComunesWSValueObject EntidadSW,String[] params)  {

        TercerosValueObject terceroSGE = new TercerosValueObject();
        CamposFormulario cs = null;

        if(!"0".equals((String)EntidadSW.getLista_errores().get(0))) {             
             m_Log.debug("errores en busqueda lanbide "+ EntidadSW.getLista_errores().get(0));
            return null;
        }
        
        

        if(("1".equals(EntidadSW.getTipo_consulta()))&&(EntidadSW.getPersona().getCorr()!=null)){ //Se obtiene persona fisica

            PersonaFisicaWSValueObject personaWSVO=new PersonaFisicaWSValueObject();
            personaWSVO=EntidadSW.getPersona();


            terceroSGE.setApellido1(soloCaracteresValidos(personaWSVO.getApellido1()));
            terceroSGE.setApellido2(soloCaracteresValidos(personaWSVO.getApellido2()));
            terceroSGE.setNombre(soloCaracteresValidos(personaWSVO.getNombre()));
            terceroSGE.setIdentificador("0");
            terceroSGE.setTelefono(personaWSVO.getTlfno1());
            terceroSGE.setTipoDocumento(tranformaTipoDocumento(personaWSVO.getTipo_doc(),personaWSVO.getNum_doc()));
            terceroSGE.setDocumento(personaWSVO.getNum_doc());
            terceroSGE.setOrigen("Lanbide");
            terceroSGE.setDomicilios(transformarDomicilioPersonaFisicaArraySW(personaWSVO));
            terceroSGE.setSituacion('A');
            terceroSGE.setEmail(personaWSVO.getCorrelec());
            terceroSGE.setCodTerceroOrigen(Long.toString(personaWSVO.getCorr()));
			
            ResourceBundle configTercero = ResourceBundle.getBundle("pluginTercerosLanbide");
           
            String prefijo = "BusquedaTercero/" + codOrganizacion + "/LANBIDE";
            
            
            try{
                Hashtable<String,Vector> salida = DatosSuplementariosTerceroManager.getInstance().cargarEstructuraValoresDatosSuplementarios(Integer.toString(codOrganizacion), null,params);
                Vector<EstructuraCampo> estructura = (Vector<EstructuraCampo>) salida.get("ESTRUCTURA_CAMPOS_TERCERO");
                Vector cf = new Vector();
                
                for(int i=0;estructura!=null && i<estructura.size();i++){                    
                    EstructuraCampo ec = estructura.get(i);
                    String codCampo = ec.getCodCampo();
                                
                    String metodoEjecutar = configTercero.getString(prefijo + "/" + codCampo);
                    m_Log.debug("Se debe ejecutar el siguiente método de persona física");
                    if(metodoEjecutar!=null && !"".equals(metodoEjecutar)){
                
                        
                        Object valor = this.ejecutarMetodo(personaWSVO,metodoEjecutar,null,null);
                                                
                        HashMap<String, String> campos2 = new HashMap<String, String>();                        
                        String valorCampo ="";
                         
                         m_Log.debug(" ******************** BusquedaTerceroLanbideImpl campo suplementario posisición " +i + " es " + codCampo);
                         if (valor !=null && valor instanceof Calendar && codCampo.equals("TFECNACIMIENTO") && metodoEjecutar.equals("getFecha_nac")){
                             
                             SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                             valorCampo = sdf.format(((Calendar)valor).getTime());
                             
                             m_Log.debug(" ******************** BusquedaTerceroLanbideImpl el valor del campo " + codCampo+ " es: " + valorCampo);
                             campos2 = new HashMap<String,String>();
                             campos2.put(codCampo,valorCampo);
                             cs = new CamposFormulario(campos2);

                         }else if (valor !=null && valor instanceof String){                             
                             valorCampo = (String)valor;
                             m_Log.debug(" ******************** BusquedaTerceroLanbideImpl el valor del campo " + codCampo + " es: " + valorCampo);
                             campos2 = new HashMap<String,String>();
                             campos2.put(codCampo,valorCampo);
                             cs = new CamposFormulario(campos2);
                         }else{
                             campos2 = new HashMap<String,String>();
                             campos2.put(codCampo,"");
                             cs = new CamposFormulario(campos2);                                     
                          }
                         
                        cf.add(cs);                                                
                    } else
                    {          
                        HashMap<String, String> camposAux = new HashMap<String, String>();   
                        camposAux = new HashMap<String,String>();
                        camposAux.put(codCampo,"");
                        cs = new CamposFormulario(camposAux); 
                        cf.add(cs);
                    }
                }//for
                terceroSGE.setValoresCamposSuplementarios(cf);
                
            }
            catch(Exception e){
                e.printStackTrace();
                m_Log.error("Error al recuperar los datos suplementarios del tercero: " + e.getMessage());
            }
           
            
        }
        else if(("2".equals(EntidadSW.getTipo_consulta()))&&(EntidadSW.getEmpresario().getNum_doc()!=null)&&(!"".equals(EntidadSW.getEmpresario().getNum_doc()))){ //Se obtiene empresario


            EmpresarioWSValueObject empresarioWSVO=new EmpresarioWSValueObject();
            empresarioWSVO=EntidadSW.getEmpresario();

            if(("".equals(empresarioWSVO.getRazon_social()))||(empresarioWSVO.getRazon_social()==null)) return null;

            terceroSGE.setNombre(soloCaracteresValidos(empresarioWSVO.getRazon_social()));
            terceroSGE.setIdentificador("0");
            terceroSGE.setTelefono(empresarioWSVO.getTelefono());
            terceroSGE.setTipoDocumento(tranformaTipoDocumentoJuiridico("",empresarioWSVO.getNum_doc()));
            terceroSGE.setDocumento(empresarioWSVO.getNum_doc());
            terceroSGE.setOrigen("Lanbide");
            terceroSGE.setDomicilios(transformarDomicilioEmpresarioArraySW(empresarioWSVO));
            terceroSGE.setSituacion('A');
            terceroSGE.setEmail(empresarioWSVO.getE_mail());
            if((empresarioWSVO.getEmp_corr()!=null)) terceroSGE.setCodTerceroOrigen(Long.toString(empresarioWSVO.getEmp_corr()));
            else terceroSGE.setCodTerceroOrigen("");

        }
        else 
        {
            //EntidadSW.getLista_errores();
            return null;
        }

       

        return terceroSGE;
    }

    
    private String tranformaTipoDocumento(String tipoDocLanbide,String numDocLanbide) {

        String tipoDocumentoSGE="";

        String tipoDoc="";
        if(!"".equals(tipoDocLanbide)) tipoDoc=docLanbidetoDocSge(tipoDocLanbide);

        if((tipoDoc==null)||("".equals(tipoDoc)))
        {
           
              tipoDocumentoSGE=compruebaDocumento(numDocLanbide);
            
        }
        else tipoDocumentoSGE=tipoDoc;

        return tipoDocumentoSGE;
    }
    private String tranformaTipoDocumentoJuiridico(String tipoDocLanbide,String numDocLanbide) {

        String tipoDocumentoSGE="";

        String tipoDoc="";
        if(!"".equals(tipoDocLanbide)) tipoDoc=docLanbidetoDocSge(tipoDocLanbide);

        if((tipoDoc==null)||("".equals(tipoDoc)))
        {

              tipoDocumentoSGE=compruebaDocumentoJuridico(numDocLanbide);

        }
        else tipoDocumentoSGE=tipoDoc;

        return tipoDocumentoSGE;
    }

    private Vector<DomicilioSimpleValueObject> transformarDomicilioPersonaFisicaArraySW(PersonaFisicaWSValueObject personaFisicaVO) {

        Vector<DomicilioSimpleValueObject> arrayDomicilioSGE = new Vector<DomicilioSimpleValueObject>();

        DomicilioSimpleValueObject dom=new DomicilioSimpleValueObject();
        DomicilioSimpleValueObject domnot=new DomicilioSimpleValueObject();

        dom=transformarDomicilioResidenciaPersonaFisicaSW(personaFisicaVO);
        domnot=transformarDomicilioNotificacionPersonaFisicaSW(personaFisicaVO);

        if(dom!=null)
        arrayDomicilioSGE.add(dom);
        if(domnot!=null)  
        arrayDomicilioSGE.add(domnot);
        
        if (arrayDomicilioSGE.isEmpty() ) {
            DomicilioSimpleValueObject domDefecto= crearDomicilioDefecto();
            arrayDomicilioSGE.add(domDefecto);
        }      

        return arrayDomicilioSGE;
    }
    private Vector<DomicilioSimpleValueObject> transformarDomicilioEmpresarioArraySW(EmpresarioWSValueObject empresarioVO) {

        Vector<DomicilioSimpleValueObject> arrayDomicilioSGE = new Vector<DomicilioSimpleValueObject>();

        DomicilioSimpleValueObject dom=new DomicilioSimpleValueObject();
        DomicilioSimpleValueObject domrs=new DomicilioSimpleValueObject();
        DomicilioSimpleValueObject domett=new DomicilioSimpleValueObject();

        dom=transformarDomicilioEmpresaSW(empresarioVO);
        domrs=transformarDomicilio_RS_EmpresaSW(empresarioVO);
        domett=transformarDomicilio_ETT_EmpresaSW(empresarioVO);

        if(dom!=null)
        arrayDomicilioSGE.add(dom);
        if(domrs!=null)
        {

           if(!((domrs.getDescVia()).equals(dom.getDescVia()))&&
                   ((domrs.getProvincia()).equals(dom.getProvincia()))
                   &&((domrs.getMunicipio()).equals(dom.getMunicipio()))
                   &&((domrs.getNumDesde()).equals(dom.getNumDesde()))
                   &&((domrs.getPlanta()).equals(dom.getPlanta()))
                   &&((domrs.getPuerta()).equals(dom.getPuerta())))
            arrayDomicilioSGE.add(domrs);
        }

        if(domett!=null)
        {
           if(!((domett.getDescVia()).equals(dom.getDescVia()))&&
                   ((domett.getProvincia()).equals(dom.getProvincia()))
                   &&((domett.getMunicipio()).equals(dom.getMunicipio()))
                   &&((domett.getNumDesde()).equals(dom.getNumDesde()))
                   &&((domett.getPlanta()).equals(dom.getPlanta()))
                   &&((domett.getPuerta()).equals(dom.getPuerta())))
           arrayDomicilioSGE.add(domett);
        }
                
        if (arrayDomicilioSGE.isEmpty() ) {
            DomicilioSimpleValueObject domDefecto= crearDomicilioDefecto();
            arrayDomicilioSGE.add(domDefecto);
        }
        return arrayDomicilioSGE;
    }

    private DomicilioSimpleValueObject crearDomicilioDefecto() {
        DomicilioSimpleValueObject domDefecto = new DomicilioSimpleValueObject();
        domDefecto.setIdPais("108");
        domDefecto.setPais("ESPAÑA");
        domDefecto.setIdProvincia("99");
        domDefecto.setIdMunicipio("999");        
        domDefecto.setProvincia("DESCONOCIDA");
        domDefecto.setMunicipio("DESCONOCIDO");
        domDefecto.setDomicilio("DESCONOCIDO");
        return domDefecto;
    }
    
    private DomicilioSimpleValueObject transformarDomicilioResidenciaPersonaFisicaSW(PersonaFisicaWSValueObject personaFisicaVO) {



        if((personaFisicaVO.getRepais()==null)||("".equals(personaFisicaVO.getRepais()))) return null;

        DomicilioSimpleValueObject domicilioSGE = new DomicilioSimpleValueObject();
        domicilioSGE.setCodigoPostal(personaFisicaVO.getRecopos());
        domicilioSGE.setIdDomicilio("0");
        domicilioSGE.setEsDomPrincipal("true");


        String numero = personaFisicaVO.getRenuvp();
        if((numero!=null)&&(!"".equals(numero)))
        {
            try {
                Integer.parseInt(numero);
                domicilioSGE.setNumDesde(numero);
            } catch (NumberFormatException nfe) {
                if (numero.indexOf("-") != -1) {
                    boolean hayNumero=false;
                    StringTokenizer tokenizer = new StringTokenizer(numero.trim(), "-");
                    if (tokenizer.hasMoreTokens())
                    {
                        String elemento=tokenizer.nextToken();
                        try{
                            int numeroLetra=Integer.parseInt(elemento);
                            domicilioSGE.setNumDesde(elemento);
                            hayNumero=true;
                        } catch (NumberFormatException nfe1) {
                            
                            if (elemento.length()==1) domicilioSGE.setLetraDesde(elemento);
                            
                        }
                            
                        
                    }
                    if (tokenizer.hasMoreTokens())
                    {
                        String elemento=tokenizer.nextToken();
                        try{
                            int numeroLetra=Integer.parseInt(elemento);
                            if (hayNumero)domicilioSGE.setNumHasta(elemento);
                            else domicilioSGE.setNumDesde(elemento);
                        } catch (NumberFormatException nfe1) {
                            
                            if (elemento.length()==1) 
                            {
                                if (hayNumero)  domicilioSGE.setLetraDesde(elemento);
                                else domicilioSGE.setLetraHasta(elemento);
                            }
                           
                        }
                        
                    }
                } else {
                    domicilioSGE.setNumDesde("");
                }
            }
        }else domicilioSGE.setNumDesde("");

        String piso= personaFisicaVO.getRepiso();
        if((piso!=null)&&(piso.length() > 5 )) piso=piso.substring(0,5);
        String escalera= personaFisicaVO.getReescale();
        if((escalera!=null)&&(escalera.length() > 2 )) escalera=escalera.substring(0,2);
        String letra= personaFisicaVO.getRelepu();
        if((letra!=null)&&(letra.length() > 4 )) letra=letra.substring(0,4);
        String bisDup= personaFisicaVO.getRebisdup();
        String portal=bisDup;
        if (bisDup != null && (bisDup.length() > 1)) {
            try {
                Config m_propertiesLanbide = ConfigServiceHelper.getConfig(FICHERO_TERCEROS_LANBIDE);
                portal = m_propertiesLanbide.getString(PREFIX_COD_PORTAL_LANBIDE + bisDup);
            } catch (Exception e) {
                portal = bisDup;
            }
        } 

        if((portal!=null)&&(portal.length() > 5 )) portal=portal.substring(0,5);

        domicilioSGE.setPlanta(piso);
        domicilioSGE.setEscalera(escalera);
        //domicilioSGE.setLetraDesde(letra);
        domicilioSGE.setPortal(portal);
        domicilioSGE.setPuerta(letra);


        domicilioSGE.setIdVia("0");
        domicilioSGE.setCodigoVia("0");
		
        String nombreCalle=personaFisicaVO.getRenovp();
        if((nombreCalle!=null)&&(nombreCalle.length() > 99 )) nombreCalle=nombreCalle.substring(0,99);
        domicilioSGE.setDescVia(soloCaracteresValidos(nombreCalle));

        GeneralValueObject datosTipoVia = new GeneralValueObject();
        try {
            String abrvTipoVia = personaFisicaVO.getRetivipu();
            datosTipoVia = fachadaSGE.getTipoViaByAbreviatura(abrvTipoVia, paramsConexionBD);
            if (datosTipoVia.getAtributo("codTipoVia").equals("0")) {
                datosTipoVia.setAtributo("codTipoVia", "0");
                datosTipoVia.setAtributo("abrvTipoVia", "SV");
                datosTipoVia.setAtributo("descTipoVia", "SIN TIPO VIA");
            }
        } catch (Exception e) {
            datosTipoVia.setAtributo("codTipoVia", "0");
            datosTipoVia.setAtributo("abrvTipoVia", "SV");
            datosTipoVia.setAtributo("descTipoVia", "SIN TIPO VIA");
        }
        domicilioSGE.setIdTipoVia((String)datosTipoVia.getAtributo("codTipoVia"));
        domicilioSGE.setTipoVia((String)datosTipoVia.getAtributo("descTipoVia"));

        domicilioSGE.setOrigen(nombreServicio);

       String codigoPaisFlexia="0";



        try{
            Config m_propertiesLanbide = ConfigServiceHelper.getConfig(FICHERO_TERCEROS_LANBIDE);
            if((codigoPaisFlexia!=null)&&(!"".equals(codigoPaisFlexia)))
            {
                codigoPaisFlexia= m_propertiesLanbide.getString(PREFIX_PAISES_LANBIDE + personaFisicaVO.getRepais());
            }
            
        }catch(Exception e)
        {
            codigoPaisFlexia=null;
        }

        try {
            

            if ((!codigoPaisFlexia.equals("0"))&&(codigoPaisFlexia!=null)) {
                //GeneralValueObject infoPais = fachadaSGE.getPaisByCodigo(Integer.parseInt(codigoPaisFlexia), paramsConexionBD);
                
                domicilioSGE.setIdPais(codigoPaisFlexia);
                domicilioSGE.setIdPaisVia(codigoPaisFlexia);
                
                domicilioSGE.setPais("");

                if (!codigoPaisFlexia.equals("108")) {
                    m_Log.debug("SE TRATA DE UN DOMICILIO EXTRANJERO. PONEMOS LA CONFIGURACION PARA DOMICILIOS EXTRANJEROS");
                    GeneralValueObject infoPais = fachadaSGE.getPaisByCodigo(Integer.parseInt(codigoPaisFlexia), paramsConexionBD);

                    domicilioSGE.setIdPais("108");
                    domicilioSGE.setPais("ESPAÑA");
                    domicilioSGE.setIdProvincia("66");
                    domicilioSGE.setProvincia("EXTRANJERO");
                    domicilioSGE.setIdMunicipio(codigoPaisFlexia);
                    domicilioSGE.setMunicipio((String) infoPais.getAtributo("nombrePais"));
                    domicilioSGE.setIdPaisVia("108");
                    domicilioSGE.setIdProvinciaVia("66");
                    domicilioSGE.setIdMunicipioVia(codigoPaisFlexia);

                } else {
                    m_Log.debug("SE TRATA DE UN DOMICILIO EN ESPAÑA. BUSCAMOS LA INFORMACION DE PROVINCIA Y MUNICIPIO");

                    //PROVINCIA
                    String codProv="";
                    if((personaFisicaVO.getReprovin()!=null)&&(!"".equals(personaFisicaVO.getReprovin())))
                    {
                        codProv=personaFisicaVO.getReprovin();
                    }else if((!"".equals(personaFisicaVO.getRemuni()))&&(personaFisicaVO.getRemuni()!=null)){
                        codProv=personaFisicaVO.getRemuni().substring(0,2);
                    }else codProv="99";


                    //String codProvincia = (String) infoProvincia.getAtributo("codigoProvincia");
                    String codigoProvincia = "";
                    try {
                        int intProv=Integer.parseInt(codProv);
                        codigoProvincia=Integer.toString(intProv);
                    } catch (NumberFormatException nfe) {
                        codigoProvincia="99";
                    }

                    domicilioSGE.setIdProvincia(codigoProvincia);
                    domicilioSGE.setIdProvinciaVia(codigoProvincia);

                    if((personaFisicaVO.getDesc_reprov_c()!=null)&&(!"".equals(personaFisicaVO.getDesc_reprov_c())))
                    {
                        domicilioSGE.setProvincia(personaFisicaVO.getDesc_reprov_c());
                    }else domicilioSGE.setProvincia("DESCONOCIDA");


                    //MUNICIPIO
                    String municipio=personaFisicaVO.getRemuni().substring(2, 5);
                    if((municipio!=null)&&(!"".equals(municipio)))
                    {
                        
                    }else
                    {
                        municipio="999";
                    }
                    
                    String codMunicipio="";
                    try {
                        int intMun=Integer.parseInt(municipio);
                        codMunicipio=Integer.toString(intMun);
                    } catch (NumberFormatException nfe) {
                        codMunicipio="999";
                    }

                    domicilioSGE.setIdMunicipio(codMunicipio);
                    domicilioSGE.setIdMunicipioVia(codMunicipio);

                    if((personaFisicaVO.getDesc_remuni_c()!=null)&&(!"".equals(personaFisicaVO.getDesc_remuni_c())))
                    {
                         domicilioSGE.setMunicipio(personaFisicaVO.getDesc_remuni_c());

                    }else domicilioSGE.setMunicipio("DESCONOCIDO");
                    
                    
                  

                }
            } else {
                m_Log.debug("NO SE HA PODIDO RECUPERAR ALGUN CODIGO DE PAIS, PROVINCIA O MUNICIPIO. PONEMOS LOS VALORES POR DEFECTO");
                domicilioSGE.setIdPais("108");
                domicilioSGE.setPais("ESPAÑA");
                domicilioSGE.setIdProvincia("99");
                domicilioSGE.setProvincia("DESCONOCIDA");
                domicilioSGE.setIdMunicipio("999");
                domicilioSGE.setMunicipio("DESCONOCIDO");
                domicilioSGE.setIdPaisVia("108");
                domicilioSGE.setIdProvinciaVia("99");
                domicilioSGE.setIdMunicipioVia("999");
                domicilioSGE.setIdVia("0");
                domicilioSGE.setCodigoVia("0"); 
                domicilioSGE.setDescVia("SIN DOMICILIO");
                
            }
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("NO SE HA PODIDO RECUPERAR ALGUN CODIGO DE PAIS, PROVINCIA O MUNICIPIO. PONEMOS LOS VALORES POR DEFECTO");
            domicilioSGE.setIdPais("108");
            domicilioSGE.setPais("ESPAÑA");
            domicilioSGE.setIdProvincia("99");
            domicilioSGE.setProvincia("DESCONOCIDA");
            domicilioSGE.setIdMunicipio("999");
            domicilioSGE.setMunicipio("DESCONOCIDO");
            domicilioSGE.setIdPaisVia("108");
            domicilioSGE.setIdProvinciaVia("99");
            domicilioSGE.setIdMunicipioVia("999");
            domicilioSGE.setIdVia("0");
            domicilioSGE.setCodigoVia("0"); 
            domicilioSGE.setDescVia("SIN DOMICILIO");
        }

        return domicilioSGE;
    }

    private DomicilioSimpleValueObject transformarDomicilioNotificacionPersonaFisicaSW(PersonaFisicaWSValueObject personaFisicaVO) {

        if((personaFisicaVO.getNopais()==null)||("".equals(personaFisicaVO.getNopais()))) return null;

        DomicilioSimpleValueObject domicilioSGE = new DomicilioSimpleValueObject();
        domicilioSGE.setCodigoPostal(personaFisicaVO.getNocopos());
        domicilioSGE.setIdDomicilio("0");

 
        String numero = personaFisicaVO.getNonuvp();
        if((numero!=null)&&(!"".equals(numero)))
        {
            try {
                Integer.parseInt(numero);
                domicilioSGE.setNumDesde(numero);
            } catch (NumberFormatException nfe) {
                if (numero.indexOf("-") != -1) {
                    boolean hayNumero=false;
                    StringTokenizer tokenizer = new StringTokenizer(numero.trim(), "-");
                    if (tokenizer.hasMoreTokens())
                    {
                        String elemento=tokenizer.nextToken();
                        try{
                            int numeroLetra=Integer.parseInt(elemento);
                            domicilioSGE.setNumDesde(elemento);
                            hayNumero=true;
                        } catch (NumberFormatException nfe1) {
                            
                            if (elemento.length()==1) domicilioSGE.setLetraDesde(elemento);
                            
                        }
                            
                        
                    }
                    if (tokenizer.hasMoreTokens())
                    {
                        String elemento=tokenizer.nextToken();
                        try{
                            int numeroLetra=Integer.parseInt(elemento);
                            if (hayNumero)domicilioSGE.setNumHasta(elemento);
                            else domicilioSGE.setNumDesde(elemento);
                        } catch (NumberFormatException nfe1) {
                            
                            if (elemento.length()==1) 
                            {
                                if (hayNumero)  domicilioSGE.setLetraDesde(elemento);
                                else domicilioSGE.setLetraHasta(elemento);
                            }
                           
                        }
                        
                    }
                } else {
                    domicilioSGE.setNumDesde("");
                }
            }
        }else domicilioSGE.setNumDesde("");

	String piso= personaFisicaVO.getNopiso();
        if((piso!=null)&&(piso.length() > 5 )) piso=piso.substring(0,5);
        String escalera= personaFisicaVO.getNoescale();
        if((escalera!=null)&&(escalera.length() > 2 )) escalera=escalera.substring(0,2);
        String letra= personaFisicaVO.getNolepu();
        if((letra!=null)&&(letra.length() > 4 )) letra=letra.substring(0,4);
        String bisDup= personaFisicaVO.getNobisdup();
        String portal=bisDup;
        if (bisDup != null && (bisDup.length() > 1)) {
            try {
                Config m_propertiesLanbide = ConfigServiceHelper.getConfig(FICHERO_TERCEROS_LANBIDE);
                portal = m_propertiesLanbide.getString(PREFIX_COD_PORTAL_LANBIDE + bisDup);
            } catch (Exception e) {
                portal = bisDup;
            }
        } 
        if((portal!=null)&&(portal.length() > 5 )) portal=portal.substring(0,5);

        domicilioSGE.setPlanta(piso);
        domicilioSGE.setEscalera(escalera);
        //domicilioSGE.setLetraDesde(letra);
	domicilioSGE.setPortal(portal);
        domicilioSGE.setPuerta(letra);
        


        domicilioSGE.setIdVia("0");
        domicilioSGE.setCodigoVia("0");
		
        String nombreCalle=personaFisicaVO.getNonovp();
        if((nombreCalle!=null)&&(nombreCalle.length() > 99 )) nombreCalle=nombreCalle.substring(0,99);
        domicilioSGE.setDescVia(soloCaracteresValidos(nombreCalle));
		
        

        GeneralValueObject datosTipoVia = new GeneralValueObject();
        try {
            String abrvTipoVia = personaFisicaVO.getNotivipu();
            datosTipoVia = fachadaSGE.getTipoViaByAbreviatura(abrvTipoVia, paramsConexionBD);
            if (datosTipoVia.getAtributo("codTipoVia").equals("0")) {
                datosTipoVia.setAtributo("codTipoVia", "0");
                datosTipoVia.setAtributo("abrvTipoVia", "SV");
                datosTipoVia.setAtributo("descTipoVia", "SIN TIPO VIA");
            }
        } catch (Exception e) {
            datosTipoVia.setAtributo("codTipoVia", "0");
            datosTipoVia.setAtributo("abrvTipoVia", "SV");
            datosTipoVia.setAtributo("descTipoVia", "SIN TIPO VIA");
        }
        domicilioSGE.setIdTipoVia((String)datosTipoVia.getAtributo("codTipoVia"));
        domicilioSGE.setTipoVia((String)datosTipoVia.getAtributo("descTipoVia"));

        domicilioSGE.setOrigen(nombreServicio);

         String codigoPaisFlexia="0";

        try{
            Config m_propertiesLanbide = ConfigServiceHelper.getConfig(FICHERO_TERCEROS_LANBIDE);
            if((codigoPaisFlexia!=null)&&(!"".equals(codigoPaisFlexia)))
            {
                codigoPaisFlexia= m_propertiesLanbide.getString(PREFIX_PAISES_LANBIDE + personaFisicaVO.getNopais());
            }

        }catch(Exception e)
        {
            codigoPaisFlexia=null;
        }

        try {
            
            
            if ((!codigoPaisFlexia.equals("0"))&&(codigoPaisFlexia!=null)) {
                //GeneralValueObject infoPais = fachadaSGE.getPaisByCodigo(Integer.parseInt(codigoPaisFlexia), paramsConexionBD);

                domicilioSGE.setIdPais(codigoPaisFlexia);
                domicilioSGE.setIdPaisVia(codigoPaisFlexia);

                domicilioSGE.setPais("");

                if (!codigoPaisFlexia.equals("108")) {
                    m_Log.debug("SE TRATA DE UN DOMICILIO EXTRANJERO. PONEMOS LA CONFIGURACION PARA DOMICILIOS EXTRANJEROS");
                    GeneralValueObject infoPais = fachadaSGE.getPaisByCodigo(Integer.parseInt(codigoPaisFlexia), paramsConexionBD);

                    domicilioSGE.setIdPais("108");
                    domicilioSGE.setPais("ESPAÑA");
                    domicilioSGE.setIdProvincia("66");
                    domicilioSGE.setProvincia("EXTRANJERO");
                    domicilioSGE.setIdMunicipio(codigoPaisFlexia);
                    domicilioSGE.setMunicipio((String) infoPais.getAtributo("nombrePais"));
                    domicilioSGE.setIdPaisVia("108");
                    domicilioSGE.setIdProvinciaVia("66");
                    domicilioSGE.setIdMunicipioVia(codigoPaisFlexia);

                

                } else {
                    m_Log.debug("SE TRATA DE UN DOMICILIO EN ESPAÑA. BUSCAMOS LA INFORMACION DE PROVINCIA Y MUNICIPIO");
                    //PROVINCIA
                    String codProv="";
                    if((personaFisicaVO.getNoprovin()!=null)&&(!"".equals(personaFisicaVO.getNoprovin())))
                    {
                        codProv=personaFisicaVO.getNoprovin();
                    }else if((!"".equals(personaFisicaVO.getNomuni()))&&(personaFisicaVO.getNomuni()!=null)){
                        codProv=personaFisicaVO.getNomuni().substring(0,2);
                    }else codProv="99";

                    String codigoProvincia = "";
                    try {
                        int intProv=Integer.parseInt(codProv);
                        codigoProvincia=Integer.toString(intProv);
                    } catch (NumberFormatException nfe) {
                        codigoProvincia="99";
                    }

                    //String codProvincia = (String) infoProvincia.getAtributo("codigoProvincia");
                    domicilioSGE.setIdProvincia(codigoProvincia);
                    domicilioSGE.setIdProvinciaVia(codigoProvincia);

                    if((personaFisicaVO.getDesc_notprov_c()!=null)&&(!"".equals(personaFisicaVO.getDesc_notprov_c())))
                    {
                        domicilioSGE.setProvincia(personaFisicaVO.getDesc_notprov_c());
                    }else domicilioSGE.setProvincia("DESCONOCIDA");


                    //MUNICIPIO
                    String municipio=personaFisicaVO.getNomuni().substring(2, 5);
                    if((municipio!=null)&&(!"".equals(municipio)))
                    {
                    }else
                    {
                      municipio="999";
                    }

                     String codMunicipio="";
                    try {
                        int intMun=Integer.parseInt(municipio);
                        codMunicipio=Integer.toString(intMun);
                    } catch (NumberFormatException nfe) {
                        codMunicipio="999";
                    }

                    domicilioSGE.setIdMunicipio(codMunicipio);
                    domicilioSGE.setIdMunicipioVia(codMunicipio);


                    if((personaFisicaVO.getDesc_notmuni_c()!=null)&&(!"".equals(personaFisicaVO.getDesc_notmuni_c())))
                    {
                         domicilioSGE.setMunicipio(personaFisicaVO.getDesc_notmuni_c());

                    }else domicilioSGE.setMunicipio("DESCONOCIDO");
                }
                
            } else {
                m_Log.debug("NO SE HA PODIDO RECUPERAR ALGUN CODIGO DE PAIS, PROVINCIA O MUNICIPIO. PONEMOS LOS VALORES POR DEFECTO");
                domicilioSGE.setIdPais("108");
                domicilioSGE.setPais("ESPAÑA");
                domicilioSGE.setIdProvincia("99");
                domicilioSGE.setProvincia("DESCONOCIDA");
                domicilioSGE.setIdMunicipio("999");
                domicilioSGE.setMunicipio("DESCONOCIDO");
                domicilioSGE.setIdPaisVia("108");
                domicilioSGE.setIdProvinciaVia("99");
                domicilioSGE.setIdMunicipioVia("999");
                domicilioSGE.setIdVia("0");
                domicilioSGE.setCodigoVia("0"); 
                domicilioSGE.setDescVia("SIN DOMICILIO");
            }
         
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("NO SE HA PODIDO RECUPERAR ALGUN CODIGO DE PAIS, PROVINCIA O MUNICIPIO. PONEMOS LOS VALORES POR DEFECTO");
            domicilioSGE.setIdPais("108");
            domicilioSGE.setPais("ESPAÑA");
            domicilioSGE.setIdProvincia("99");
            domicilioSGE.setProvincia("DESCONOCIDA");
            domicilioSGE.setIdMunicipio("999");
            domicilioSGE.setMunicipio("DESCONOCIDO");
            domicilioSGE.setIdPaisVia("108");
            domicilioSGE.setIdProvinciaVia("99");
            domicilioSGE.setIdMunicipioVia("999");
            domicilioSGE.setIdVia("0");
            domicilioSGE.setCodigoVia("0"); 
            domicilioSGE.setDescVia("SIN DOMICILIO");
        }

        return domicilioSGE;
    }

    private DomicilioSimpleValueObject transformarDomicilioEmpresaSW(EmpresarioWSValueObject empresarioVO) {


        if((empresarioVO.getCod_provincia()==null)||("".equals(empresarioVO.getCod_provincia()))) return null;

        DomicilioSimpleValueObject domicilioSGE = new DomicilioSimpleValueObject();
        domicilioSGE.setCodigoPostal(empresarioVO.getCp());
        domicilioSGE.setIdDomicilio("0");
        domicilioSGE.setEsDomPrincipal("true");


        String numero = empresarioVO.getN_via();
        if((numero!=null)&&(!"".equals(numero)))
        {
            try {
                Integer.parseInt(numero);
                domicilioSGE.setNumDesde(numero);
            } catch (NumberFormatException nfe) {
                if (numero.indexOf("-") != -1) {
                    boolean hayNumero=false;
                    StringTokenizer tokenizer = new StringTokenizer(numero.trim(), "-");
                    if (tokenizer.hasMoreTokens())
                    {
                        String elemento=tokenizer.nextToken();
                        try{
                            int numeroLetra=Integer.parseInt(elemento);
                            domicilioSGE.setNumDesde(elemento);
                            hayNumero=true;
                        } catch (NumberFormatException nfe1) {
                            
                            if (elemento.length()==1) domicilioSGE.setLetraDesde(elemento);
                            
                        }
                            
                        
                    }
                    if (tokenizer.hasMoreTokens())
                    {
                        String elemento=tokenizer.nextToken();
                        try{
                            int numeroLetra=Integer.parseInt(elemento);
                            if (hayNumero)domicilioSGE.setNumHasta(elemento);
                            else domicilioSGE.setNumDesde(elemento);
                        } catch (NumberFormatException nfe1) {
                            
                            if (elemento.length()==1) 
                            {
                                if (hayNumero)  domicilioSGE.setLetraDesde(elemento);
                                else domicilioSGE.setLetraHasta(elemento);
                            }
                           
                        }
                        
                    }
                } else {
                    domicilioSGE.setNumDesde("");
                }
            }
        }else domicilioSGE.setNumDesde("");

	String piso= empresarioVO.getPiso();
        if((piso!=null)&&(piso.length() > 3 )) piso=piso.substring(0,3);
        String puerta= empresarioVO.getPuerta();
        if((puerta!=null)&&(puerta.length() > 4 )) puerta=puerta.substring(0,4);
        String bisDup= empresarioVO.getBis_duplicado();
        String portal=bisDup;
        if (bisDup != null && (bisDup.length() > 1)) {
            try{
                    Config m_propertiesLanbide = ConfigServiceHelper.getConfig(FICHERO_TERCEROS_LANBIDE);
                    portal= m_propertiesLanbide.getString(PREFIX_COD_PORTAL_LANBIDE + bisDup);
            }catch(Exception e)
            {
                    portal=bisDup;
            }
        }

        if((portal!=null)&&(portal.length() > 2 )) portal=portal.substring(0,2);
        String escalera= empresarioVO.getEscalera();
        if((escalera!=null)&&(escalera.length() > 2 )) escalera=escalera.substring(0,2);
        

        domicilioSGE.setPlanta(piso);
        domicilioSGE.setPuerta(puerta);
        domicilioSGE.setPortal(portal);
        domicilioSGE.setEscalera(escalera);


       
        domicilioSGE.setIdVia("0");
        domicilioSGE.setCodigoVia("0");
        String nombreCalle=empresarioVO.getNombre_via();
        if((nombreCalle!=null)&&(nombreCalle.length() > 99 )) nombreCalle=nombreCalle.substring(0,99);
        domicilioSGE.setDescVia(soloCaracteresValidos(nombreCalle));

        GeneralValueObject datosTipoVia = new GeneralValueObject();
        try {
            String abrvTipoVia = empresarioVO.getCod_tipovia();
            datosTipoVia = fachadaSGE.getTipoViaByAbreviatura(abrvTipoVia, paramsConexionBD);
            if (datosTipoVia.getAtributo("codTipoVia").equals("0")) {
                datosTipoVia.setAtributo("codTipoVia", "0");
                datosTipoVia.setAtributo("abrvTipoVia", "SV");
                datosTipoVia.setAtributo("descTipoVia", "SIN TIPO VIA");
            }
        } catch (Exception e) {
            datosTipoVia.setAtributo("codTipoVia", "0");
            datosTipoVia.setAtributo("abrvTipoVia", "SV");
            datosTipoVia.setAtributo("descTipoVia", "SIN TIPO VIA");
        }
        domicilioSGE.setIdTipoVia((String)datosTipoVia.getAtributo("codTipoVia"));
        domicilioSGE.setTipoVia((String)datosTipoVia.getAtributo("descTipoVia"));

        domicilioSGE.setOrigen(nombreServicio);

       String codigoPaisFlexia="108";  //El código del pais para empresario no lo devuelve el servicio. Pais por defecto España


        try {

                
                String codPais = codigoPaisFlexia;
                domicilioSGE.setIdPais(codPais);
                domicilioSGE.setIdPaisVia(codPais);
                String descPais = "";
                domicilioSGE.setPais(descPais);



                    m_Log.debug("SE TRATA DE UN DOMICILIO EN ESPAÑA. BUSCAMOS LA INFORMACION DE PROVINCIA Y MUNICIPIO");

                    //PROVINCIA
                    String codProv="";
                    if((empresarioVO.getCod_provincia()!=null)&&(!"".equals(empresarioVO.getCod_provincia())))
                    {
                        codProv=empresarioVO.getCod_provincia();
                    }else if((!"".equals(empresarioVO.getCod_municipio()))&&(empresarioVO.getCod_municipio()!=null)){
                        codProv=empresarioVO.getCod_municipio().substring(0,2);
                    }else codProv="99";

                    String codigoProvincia = "";
                    try {
                        int intProv=Integer.parseInt(codProv);
                        codigoProvincia=Integer.toString(intProv);
                    } catch (NumberFormatException nfe) {
                        codigoProvincia="99";
                    }

                    //String codProvincia = (String) infoProvincia.getAtributo("codigoProvincia");
                    domicilioSGE.setIdProvincia(codigoProvincia);
                    domicilioSGE.setIdProvinciaVia(codigoProvincia);


                    if((empresarioVO.getDesc_prov_c()!=null)&&(!"".equals(empresarioVO.getDesc_prov_c())))
                    {
                        domicilioSGE.setProvincia(empresarioVO.getDesc_prov_c());
                    }else domicilioSGE.setProvincia("DESCONOCIDA");


                    //MUNICIPIO
                   String municipio=empresarioVO.getCod_municipio().substring(2, 5);
                    if((municipio!=null)&&(!"".equals(municipio)))
                    {
                        
                    }else
                    {
                       municipio="999";
                    }

                    String codMunicipio="";
                    try {
                        int intMun=Integer.parseInt(municipio);
                        codMunicipio=Integer.toString(intMun);
                    } catch (NumberFormatException nfe) {
                        codMunicipio="999";
                    }

                    domicilioSGE.setIdMunicipio(codMunicipio);
                    domicilioSGE.setIdMunicipioVia(codMunicipio);

                    if((empresarioVO.getDesc_muni_c()!=null)&&(!"".equals(empresarioVO.getDesc_muni_c())))
                    {
                         domicilioSGE.setMunicipio(empresarioVO.getDesc_muni_c());

                    }else domicilioSGE.setMunicipio("DESCONOCIDO");

                  



        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("NO SE HA PODIDO RECUPERAR ALGUN CODIGO DE PAIS, PROVINCIA O MUNICIPIO. PONEMOS LOS VALORES POR DEFECTO");
            domicilioSGE.setIdPais("108");
            domicilioSGE.setPais("ESPAÑA");
            domicilioSGE.setIdProvincia("99");
            domicilioSGE.setProvincia("DESCONOCIDA");
            domicilioSGE.setIdMunicipio("999");
            domicilioSGE.setMunicipio("DESCONOCIDO");
            domicilioSGE.setIdPaisVia("108");
            domicilioSGE.setIdProvinciaVia("99");
            domicilioSGE.setIdMunicipioVia("999");
            domicilioSGE.setIdVia("0");
            domicilioSGE.setCodigoVia("0"); 
            domicilioSGE.setDescVia("SIN DOMICILIO");
        }

        return domicilioSGE;
    }

    private DomicilioSimpleValueObject transformarDomicilio_RS_EmpresaSW(EmpresarioWSValueObject empresarioVO) {


        if((empresarioVO.getCod_provincia_rs()==null)||("".equals(empresarioVO.getCod_provincia_rs()))) return null;

        DomicilioSimpleValueObject domicilioSGE = new DomicilioSimpleValueObject();
        domicilioSGE.setCodigoPostal(empresarioVO.getCp_rs());
        domicilioSGE.setIdDomicilio("0");


        String numero = empresarioVO.getN_via_rs();
        if((numero!=null)&&(!"".equals(numero)))
        {
            try {
                Integer.parseInt(numero);
                domicilioSGE.setNumDesde(numero);
            } catch (NumberFormatException nfe) {
                if (numero.indexOf("-") != -1) {
                    boolean hayNumero=false;
                    StringTokenizer tokenizer = new StringTokenizer(numero.trim(), "-");
                    if (tokenizer.hasMoreTokens())
                    {
                        String elemento=tokenizer.nextToken();
                        try{
                            int numeroLetra=Integer.parseInt(elemento);
                            domicilioSGE.setNumDesde(elemento);
                            hayNumero=true;
                        } catch (NumberFormatException nfe1) {
                            
                            if (elemento.length()==1) domicilioSGE.setLetraDesde(elemento);
                            
                        }
                            
                        
                    }
                    if (tokenizer.hasMoreTokens())
                    {
                        String elemento=tokenizer.nextToken();
                        try{
                            int numeroLetra=Integer.parseInt(elemento);
                            if (hayNumero)domicilioSGE.setNumHasta(elemento);
                            else domicilioSGE.setNumDesde(elemento);
                        } catch (NumberFormatException nfe1) {
                            
                            if (elemento.length()==1) 
                            {
                                if (hayNumero)  domicilioSGE.setLetraDesde(elemento);
                                else domicilioSGE.setLetraHasta(elemento);
                            }
                           
                        }
                        
                    }
                } else {
                    domicilioSGE.setNumDesde("");
                }
            }
        }else domicilioSGE.setNumDesde("");

        String piso= empresarioVO.getPiso_rs();
       if((piso!=null)&&(piso.length() > 3 )) piso=piso.substring(0,3);
       String puerta= empresarioVO.getPuerta_rs();
       if((puerta!=null)&&(puerta.length() > 4 )) puerta=puerta.substring(0,4);
        
        String bisDup= empresarioVO.getBis_duplicado_rs();
        String portal=bisDup;
        if (bisDup != null && (bisDup.length() > 1)) {
            try{
                    Config m_propertiesLanbide = ConfigServiceHelper.getConfig(FICHERO_TERCEROS_LANBIDE);
                    portal= m_propertiesLanbide.getString(PREFIX_COD_PORTAL_LANBIDE + bisDup);
            }catch(Exception e)
            {
                    portal=bisDup;
            }
        }
        if((portal!=null)&&(portal.length() > 2 )) portal=portal.substring(0,2);
        String escalera= empresarioVO.getEscalera_rs();
        if((escalera!=null)&&(escalera.length() > 2 )) escalera=escalera.substring(0,2);


        domicilioSGE.setPlanta(piso);
        domicilioSGE.setPuerta(puerta);
        domicilioSGE.setPortal(portal);
        domicilioSGE.setEscalera(escalera);
       
        domicilioSGE.setIdVia("0");
        domicilioSGE.setCodigoVia("0");
        String nombreCalle=empresarioVO.getNombre_via_rs();
        if((nombreCalle!=null)&&(nombreCalle.length() > 99 )) nombreCalle=nombreCalle.substring(0,99);
        domicilioSGE.setDescVia(soloCaracteresValidos(nombreCalle));
		

        

        GeneralValueObject datosTipoVia = new GeneralValueObject();
        try {
            String abrvTipoVia = empresarioVO.getCod_tipovia_rs();
            datosTipoVia = fachadaSGE.getTipoViaByAbreviatura(abrvTipoVia, paramsConexionBD);
            if (datosTipoVia.getAtributo("codTipoVia").equals("0")) {
                datosTipoVia.setAtributo("codTipoVia", "0");
                datosTipoVia.setAtributo("abrvTipoVia", "SV");
                datosTipoVia.setAtributo("descTipoVia", "SIN TIPO VIA");
            }
        } catch (Exception e) {
            datosTipoVia.setAtributo("codTipoVia", "0");
            datosTipoVia.setAtributo("abrvTipoVia", "SV");
            datosTipoVia.setAtributo("descTipoVia", "SIN TIPO VIA");
        }
        domicilioSGE.setIdTipoVia((String)datosTipoVia.getAtributo("codTipoVia"));
        domicilioSGE.setTipoVia((String)datosTipoVia.getAtributo("descTipoVia"));

        domicilioSGE.setOrigen(nombreServicio);

       String codigoPaisFlexia="108";  //El código del pais para empresario no lo devuelve el servicio. Pais por defecto España


        try {

                String codPais = codigoPaisFlexia;
                domicilioSGE.setIdPais(codPais);
                domicilioSGE.setIdPaisVia(codPais);
                String descPais = "";
                domicilioSGE.setPais(descPais);

                 //PROVINCIA
                    String codProv="";
                    if((empresarioVO.getCod_provincia_rs()!=null)&&(!"".equals(empresarioVO.getCod_provincia_rs())))
                    {
                        codProv=empresarioVO.getCod_provincia_rs();
                    }else if((!"".equals(empresarioVO.getCod_municipio_rs()))&&(empresarioVO.getCod_municipio_rs()!=null)){
                        codProv=empresarioVO.getCod_municipio_rs().substring(0,2);
                    }else codProv="99";

                     String codigoProvincia = "";
                    try {
                        int intProv=Integer.parseInt(codProv);
                        codigoProvincia=Integer.toString(intProv);
                    } catch (NumberFormatException nfe) {
                        codigoProvincia="99";
                    }

                    //String codProvincia = (String) infoProvincia.getAtributo("codigoProvincia");
                    domicilioSGE.setIdProvincia(codigoProvincia);
                    domicilioSGE.setIdProvinciaVia(codigoProvincia);

                    if((empresarioVO.getDesc_prov_rs_c()!=null)&&(!"".equals(empresarioVO.getDesc_prov_rs_c())))
                    {
                        domicilioSGE.setProvincia(empresarioVO.getDesc_prov_rs_c());
                    }else domicilioSGE.setProvincia("DESCONOCIDA");


                    //MUNICIPIO
                   String municipio=empresarioVO.getCod_municipio_rs().substring(2, 5);
                    if((municipio!=null)&&(!"".equals(municipio)))
                    {
                         
                    }else
                    {
                        municipio="999";
                    }

                   String codMunicipio="";
                    try {
                        int intMun=Integer.parseInt(municipio);
                        codMunicipio=Integer.toString(intMun);
                    } catch (NumberFormatException nfe) {
                        codMunicipio="999";
                    }

                    domicilioSGE.setIdMunicipio(codMunicipio);
                    domicilioSGE.setIdMunicipioVia(codMunicipio);

                    if((empresarioVO.getDesc_muni_rs_c()!=null)&&(!"".equals(empresarioVO.getDesc_muni_rs_c())))
                    {
                         domicilioSGE.setMunicipio(empresarioVO.getDesc_muni_rs_c());

                    }else domicilioSGE.setMunicipio("DESCONOCIDO");






        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("NO SE HA PODIDO RECUPERAR ALGUN CODIGO DE PAIS, PROVINCIA O MUNICIPIO. PONEMOS LOS VALORES POR DEFECTO");
            domicilioSGE.setIdPais("108");
            domicilioSGE.setPais("ESPAÑA");
            domicilioSGE.setIdProvincia("99");
            domicilioSGE.setProvincia("DESCONOCIDA");
            domicilioSGE.setIdMunicipio("999");
            domicilioSGE.setMunicipio("DESCONOCIDO");
            domicilioSGE.setIdPaisVia("108");
            domicilioSGE.setIdProvinciaVia("99");
            domicilioSGE.setIdMunicipioVia("999");
            domicilioSGE.setIdVia("0");
            domicilioSGE.setCodigoVia("0"); 
            domicilioSGE.setDescVia("SIN DOMICILIO");
        }

        return domicilioSGE;
    }

    private DomicilioSimpleValueObject transformarDomicilio_ETT_EmpresaSW(EmpresarioWSValueObject empresarioVO) {


        if((empresarioVO.getCod_provincia_ett()==null)||("".equals(empresarioVO.getCod_provincia_ett()))) return null;

        DomicilioSimpleValueObject domicilioSGE = new DomicilioSimpleValueObject();
        domicilioSGE.setCodigoPostal(empresarioVO.getCp_ett());
        domicilioSGE.setIdDomicilio("0");


        String numero = empresarioVO.getN_via_ett();
        if((numero!=null)&&(!"".equals(numero)))
        {
            try {
                Integer.parseInt(numero);
                domicilioSGE.setNumDesde(numero);
            } catch (NumberFormatException nfe) {
                if (numero.indexOf("-") != -1) {
                    boolean hayNumero=false;
                    StringTokenizer tokenizer = new StringTokenizer(numero.trim(), "-");
                    if (tokenizer.hasMoreTokens())
                    {
                        String elemento=tokenizer.nextToken();
                        try{
                            int numeroLetra=Integer.parseInt(elemento);
                            domicilioSGE.setNumDesde(elemento);
                            hayNumero=true;
                        } catch (NumberFormatException nfe1) {
                            
                            if (elemento.length()==1) domicilioSGE.setLetraDesde(elemento);
                            
                        }
                            
                        
                    }
                    if (tokenizer.hasMoreTokens())
                    {
                        String elemento=tokenizer.nextToken();
                        try{
                            int numeroLetra=Integer.parseInt(elemento);
                            if (hayNumero)domicilioSGE.setNumHasta(elemento);
                            else domicilioSGE.setNumDesde(elemento);
                        } catch (NumberFormatException nfe1) {
                            
                            if (elemento.length()==1) 
                            {
                                if (hayNumero)  domicilioSGE.setLetraDesde(elemento);
                                else domicilioSGE.setLetraHasta(elemento);
                            }
                           
                        }
                        
                    }
                } else {
                    domicilioSGE.setNumDesde("");
                }
            }
        }else domicilioSGE.setNumDesde("");

	String piso= empresarioVO.getPiso_ett();
        if((piso!=null)&&(piso.length() > 3 ))  piso=piso.substring(0,3);
        String puerta= empresarioVO.getPuerta_ett();
        if((puerta!=null)&&(puerta.length() > 4 ))  puerta=puerta.substring(0,4);
        
        String bisDup= empresarioVO.getBis_duplicado_ett();
        String portal=bisDup;
        if (bisDup != null && (bisDup.length() > 1)) {
            try{
                    Config m_propertiesLanbide = ConfigServiceHelper.getConfig(FICHERO_TERCEROS_LANBIDE);
                    portal= m_propertiesLanbide.getString(PREFIX_COD_PORTAL_LANBIDE + bisDup);
            }catch(Exception e)
            {
                    portal=bisDup;
            }
        }
        if((portal!=null)&&(portal.length() > 2 )) portal=portal.substring(0,2);
        String escalera= empresarioVO.getEscalera_ett();
        if((escalera!=null)&&(escalera.length() > 2 )) escalera=escalera.substring(0,2);


        domicilioSGE.setPlanta(piso);
        domicilioSGE.setPuerta(puerta);
        domicilioSGE.setPortal(portal);
        domicilioSGE.setEscalera(escalera);
       
        domicilioSGE.setIdVia("0");
        domicilioSGE.setCodigoVia("0");
        String nombreCalle=empresarioVO.getNombre_via_ett();
        if((nombreCalle!=null)&&(nombreCalle.length() > 99 )) nombreCalle=nombreCalle.substring(0,99);
        domicilioSGE.setDescVia(soloCaracteresValidos(nombreCalle));
		        

        GeneralValueObject datosTipoVia = new GeneralValueObject();
        try {
            String abrvTipoVia = empresarioVO.getCod_tipovia_ett();
            datosTipoVia = fachadaSGE.getTipoViaByAbreviatura(abrvTipoVia, paramsConexionBD);
            if (datosTipoVia.getAtributo("codTipoVia").equals("0")) {
                datosTipoVia.setAtributo("codTipoVia", "0");
                datosTipoVia.setAtributo("abrvTipoVia", "SV");
                datosTipoVia.setAtributo("descTipoVia", "SIN TIPO VIA");
            }
        } catch (Exception e) {
            datosTipoVia.setAtributo("codTipoVia", "0");
            datosTipoVia.setAtributo("abrvTipoVia", "SV");
            datosTipoVia.setAtributo("descTipoVia", "SIN TIPO VIA");
        }
        domicilioSGE.setIdTipoVia((String)datosTipoVia.getAtributo("codTipoVia"));
        domicilioSGE.setTipoVia((String)datosTipoVia.getAtributo("descTipoVia"));

        domicilioSGE.setOrigen(nombreServicio);

       String codigoPaisFlexia="108";  //El código del pais para empresario no lo devuelve el servicio. Pais por defecto España


        try {

                GeneralValueObject infoPais = fachadaSGE.getPaisByCodigo(Integer.parseInt("108"), paramsConexionBD);
                String codPais = (String) infoPais.getAtributo("codigoPais");
                domicilioSGE.setIdPais(codPais);
                domicilioSGE.setIdPaisVia(codPais);
                String descPais = (String) infoPais.getAtributo("nombrePais");
                domicilioSGE.setPais(descPais); 


                    m_Log.debug("SE TRATA DE UN DOMICILIO EN ESPAÑA. BUSCAMOS LA INFORMACION DE PROVINCIA Y MUNICIPIO");
                    //PROVINCIA
                    String codProv="";
                    if((empresarioVO.getCod_provincia_ett()!=null)&&(!"".equals(empresarioVO.getCod_provincia_ett())))
                    {
                        codProv=empresarioVO.getCod_provincia_ett();
                    }else if((!"".equals(empresarioVO.getCod_municipio_ett()))&&(empresarioVO.getCod_municipio_ett()!=null)){
                        codProv=empresarioVO.getCod_municipio_ett().substring(0,2);
                    }else codProv="99";

                    String codigoProvincia = "";
                    try {
                        int intProv=Integer.parseInt(codProv);
                        codigoProvincia=Integer.toString(intProv);
                    } catch (NumberFormatException nfe) {
                        codigoProvincia="99";
                    }

                    //String codProvincia = (String) infoProvincia.getAtributo("codigoProvincia");
                    domicilioSGE.setIdProvincia(codigoProvincia);
                    domicilioSGE.setIdProvinciaVia(codigoProvincia);

                    if((empresarioVO.getDesc_prov_ett_c()!=null)&&(!"".equals(empresarioVO.getDesc_prov_ett_c())))
                    {
                        domicilioSGE.setProvincia(empresarioVO.getDesc_prov_ett_c());
                    }else domicilioSGE.setProvincia("DESCONOCIDA");


                    //MUNICIPIO
                   String municipio=empresarioVO.getCod_municipio_ett().substring(2, 5);
                    if((municipio!=null)&&(!"".equals(municipio)))
                    {
                        

                    }else
                    {
                        municipio="999";
                    }
                    String codMunicipio="";
                    try {
                        int intMun=Integer.parseInt(municipio);
                        codMunicipio=Integer.toString(intMun);
                    } catch (NumberFormatException nfe) {
                        codMunicipio="999";
                    }

                    domicilioSGE.setIdMunicipio(codMunicipio);
                    domicilioSGE.setIdMunicipioVia(codMunicipio);

                    if((empresarioVO.getDesc_muni_ett_c()!=null)&&(!"".equals(empresarioVO.getDesc_muni_ett_c())))
                    {
                         domicilioSGE.setMunicipio(empresarioVO.getDesc_muni_ett_c());

                    }else domicilioSGE.setMunicipio("DESCONOCIDO");



        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("NO SE HA PODIDO RECUPERAR ALGUN CODIGO DE PAIS, PROVINCIA O MUNICIPIO. PONEMOS LOS VALORES POR DEFECTO");
            domicilioSGE.setIdPais("108");
            domicilioSGE.setPais("ESPAÑA");
            domicilioSGE.setIdProvincia("99");
            domicilioSGE.setProvincia("DESCONOCIDA");
            domicilioSGE.setIdMunicipio("999");
            domicilioSGE.setMunicipio("DESCONOCIDO");
            domicilioSGE.setIdPaisVia("108");
            domicilioSGE.setIdProvinciaVia("99");
            domicilioSGE.setIdMunicipioVia("999");
            domicilioSGE.setIdVia("0");
            domicilioSGE.setCodigoVia("0"); 
            domicilioSGE.setDescVia("SIN DOMICILIO");
        }

        return domicilioSGE;
    }



    private boolean validarNif(String Nif) {

        String valores_numeros="0123456789";
        String correspondencia="TRWAGMYFPDXBNJZSQVHLCKE";
        char letra;
        int longitud=Nif.length();
        Nif=Nif.toUpperCase();

        if(longitud==9)
        {
            letra=Nif.charAt(8);
            int numero=0;
            try
            {
                numero=Integer.parseInt(Nif.substring(0,longitud-1));
            }
            catch (NumberFormatException e)
            {
                return false;
            }
            char letraReal=correspondencia.charAt(numero % 23);

            if(letra==letraReal) return true;
            else return false;

        }
        else return false;

    }

    private boolean validarNie(String Nie)
    {

        int LONGITUD = 9;

    // Si se trata de un NIF
    // Primero comprobamos la longitud, si es distinta de la esperada, rechazamos.
    if (Nie.length() != LONGITUD) {

        return false;
    }

    // Comprobas que el formato se corresponde con el de un NIE
    char primeraLetra = Nie.charAt(0);
    String numero = Nie.substring(1,8);
    char ultimaLetra = Nie.charAt(8);
    int numeros=0;
    try
     {
       numeros=Integer.parseInt(numero);
     }
     catch (NumberFormatException e)
     {
        return false;
     }

    // Comprobamos que la primera letra es X, Y, o Z modificando el numero como corresponda.
    if (primeraLetra =='Y') numeros = numeros + 10000000;
    else if (primeraLetra == 'Z') numeros = numeros + 20000000;
    else if (primeraLetra != 'X') {

        return false;
    }

    // Validamos el caracter de control.
    char letraCorrecta = getLetraNif(numeros);
    if (ultimaLetra != letraCorrecta) {

        return false;
    }
    return true;
    }

    private char getLetraNif(int dni) {
    String lockup = "TRWAGMYFPDXBNJZSQVHLCKE";

    char letraReal=lockup.charAt(dni % 23);
    return letraReal;
    }


    private String compruebaDocumentoJuridico(String documento)
    {
        if(validarNif(documento)) return ("1");
        else if (validarNie(documento)) return ("3");
        else return ("4"); //Es un CIF
    }
    private String compruebaDocumento(String documento)
    {
        if(validarNif(documento)) return ("1");
        else if (validarNie(documento)) return ("3");
        else return ("2"); //Es un Pasaporte
    }


//Devuelve los caracteres validos y pasa a mayuscula
    private String soloCaracteresValidos(String cadena)
    {
        String validos="0123456789abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZÁÉÍÓÚáéíóú-_@.,;'";
        String salida="";

        if((!"".equals(cadena))&&(cadena!=null))
        {
            cadena = reemplazar(cadena);

            for(int i=0;i<cadena.length();i++)
            {
                if(cadena.charAt(i)==' ')salida = salida + cadena.charAt(i);
                else if(validos.indexOf(cadena.charAt(i)) != -1)
                {
                salida = salida + cadena.charAt(i);
                }
                else  salida = salida + "_";
            }
        }
        return salida.toUpperCase();
    }
     private String reemplazar(String valor)
    {
        valor= valor.replace('\"', '\'');
        valor = valor.replace('Á', 'A');
        valor = valor.replace('É', 'E');
        valor = valor.replace('Í', 'I');
        valor = valor.replace('Ó', 'O');
        valor = valor.replace('Ú', 'U');
        valor = valor.replace('á', 'a');
        valor = valor.replace('é', 'e');
        valor = valor.replace('í', 'i');
        valor = valor.replace('ó', 'o');
        valor = valor.replace('ú', 'u');
        return valor;
     }

public Object ejecutarMetodo(Object objeto,String nombreMetodo,Class[] tipoParametros,Object[] valoresParametros){

    Object valor ="";

      try{
      	Class clase = Class.forName(objeto.getClass().getName());
            clase.newInstance();

        Method metodo = clase.getMethod(nombreMetodo,tipoParametros);
            valor         = metodo.invoke(objeto,valoresParametros);

        }catch(ClassNotFoundException e){
            m_Log.error(e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
             m_Log.error(e.getMessage());
        }

        return valor;
}




}
