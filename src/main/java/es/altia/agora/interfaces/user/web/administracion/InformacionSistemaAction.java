// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.administracion;

// PAQUETES IMPORTADOS

import es.altia.agora.business.escritorio.*;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.*;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import es.altia.common.service.mail.MailHelper;

import java.net.InetAddress;

import es.altia.agora.business.administracion.mantenimiento.persistence.*;
import es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm;
import java.util.Vector;


/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase TextosFijosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Fernando Rueda Rueda
 * @version 1.0
 */


public class InformacionSistemaAction extends ActionSession
{

    protected static Config confLog = ConfigServiceHelper.getConfig("log4j");
    protected static Config confCommon = ConfigServiceHelper.getConfig("common");
    protected static Config confTech = ConfigServiceHelper.getConfig("techserver");
    protected static Config confVersion = ConfigServiceHelper.getConfig("version");
    protected static Config confConsultas = ConfigServiceHelper.getConfig("consultasAdministrador");

  public ActionForward performSession(ActionMapping mapping, ActionForm form,
                      HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {

    m_Log.debug("perform");
    ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
    HttpSession session = request.getSession();





    // Validaremos los parametros del request especificados
    ActionErrors errors = new ActionErrors();
    String opcion = request.getParameter("opcion");
    if (m_Log.isInfoEnabled()) m_Log.info("la opcion en el action es " + opcion);

    // Rellenamos el form de Mantenimiento

      m_Log.info("Rellenamos el form ");
      InformacionSistemaForm formulario = (InformacionSistemaForm)form;
      if ("request".equals(mapping.getScope()))
      {
        request.setAttribute(mapping.getAttribute(), form);

      }
      else
      {
        session.setAttribute(mapping.getAttribute(), form);
      }//del if

    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    String[] params = usuario.getParamsCon();
    
     InfoSistemaManager infoSistemasManager = InfoSistemaManager.getInstance(); 

    if ("cargar".equalsIgnoreCase(opcion))
    {
      String ipAddr = null;
      String hostname = null;
      try {
            final InetAddress addr = InetAddress.getLocalHost();
            Runtime runtime = Runtime.getRuntime();
            // Get IP Address
            ipAddr = addr.getHostAddress();
            // Get hostname
            hostname = addr.getHostName();
            formulario.setServerNodeLocalName(hostname);
            formulario.setServerNodeLocalIP(ipAddr);

            //Propiedades JVM y SO
            formulario.setOsName(System.getProperty ("os.name"));
            formulario.setOsVersion(System.getProperty ("os.version"));
            formulario.setVMModeInfo(System.getProperty ("java.vm.info"));
            formulario.setVMVendor(System.getProperty ("java.vm.vendor"));
            formulario.setVMVersion(System.getProperty ("java.vm.version"));
            //Memoria JVM
            long mb=1024*1024;
            long memoriaUsada=(runtime.totalMemory() - runtime.freeMemory()) / mb;
            long memoriatotal=(runtime.totalMemory()) / mb;

            formulario.setJVMUsedMemory(Long.toString(memoriaUsada));
            formulario.setJVMTotalMemory(Long.toString(memoriatotal));
            //Info del servidor de aplicaciones
            formulario.setServerInfo(session.getServletContext().getServerInfo());

            //Localizacion fichero de log
            formulario.setRutaFicheroLog(confLog.getString("log4j.appender.RollingFile.File"));

            //Obtener propiedades configuracion Flexia
            formulario.setGestor(confTech.getString("CON.gestor"));
            formulario.setJndi(confTech.getString("CON.jndi"));
            formulario.setHostVirtual(confCommon.getString("hostVirtual"));
            formulario.setWebAppBaseDir(confCommon.getString("SIGP.webapp_base_dir"));
            formulario.setVersion(confVersion.getString("version.aplicacion"));
            formulario.setNivelLog(Logger.getRootLogger().getLevel().toString());
            formulario.setSalidaLog(confLog.getString("log4j.rootCategory"));
            formulario.setActivarConsultaADMG(false);
            
            try
            {
                String activar=confCommon.getString("activarConsultaADMG");
                if (("SI".equals(activar))||("si".equals(activar)))
                formulario.setActivarConsultaADMG(true);
            }catch (es.altia.common.exception.CriticalException ex)
            {
                 if (m_Log.isDebugEnabled()) m_Log.debug("No existe la propiedad activarConsultaADMG en el common");
            }


            //Obtener informacion sesiones activas
           // System.out.println("\n\n sesiones activas  "+MyHttpSessionListener.getActiveSessions());
            //HashMap mapa=MyHttpSessionListener.getInfoSessions();
            //System.out.println("\n\n sesiones activas");
            //Collection coleccionSesiones=mapa.values();




            /*Iterator iter = coleccionSesiones.iterator();
            while (iter.hasNext()){
                
                Vector v=(Vector) iter.next();
                System.out.println("\ndentro del vector sesion "+v.toString());
                 System.out.println("\ndentro del vector sesion "+v.get(0));

            }*/
            
             OrganizacionesManager orgManager = OrganizacionesManager.getInstance();
             
             
             InformacionSistemaForm mantForm = (InformacionSistemaForm)form;
       
             Vector lista = orgManager.getListaOrganizaciones(params);
             mantForm.setListaOrganizaciones(lista);
             
             session.setAttribute("InformacionSistemaForm", mantForm);



        } catch (Exception e) {
             e.printStackTrace();
        }//try-catch
        //if (hostname!=null) formulario.setServerNodeLocalName(hostname);


    }
    else if ("recolectarMemoria".equalsIgnoreCase(opcion))
    {
        if (m_Log.isDebugEnabled()) m_Log.debug("Recolector de memoria");
        try{
            long mb=1024*1024;
            Runtime runtime0 = Runtime.getRuntime();
            
            System.gc();

            Runtime runtime = Runtime.getRuntime();
            // Get IP Address

            //Memoria JVM
            
            long memoriaUsada=(runtime.totalMemory() - runtime.freeMemory()) / mb;
            long memoriatotal=(runtime.totalMemory()) / mb;
            

            formulario.setJVMUsedMemory(Long.toString(memoriaUsada));
            formulario.setJVMTotalMemory(Long.toString(memoriatotal));

        }catch (Exception e) {
            if (m_Log.isErrorEnabled()) m_Log.error("Recolector de memoria no realizado");
             e.printStackTrace();
        }
         formulario.setOperacion(opcion);
         opcion = "oculto";
         
    }
    else if ("invalidar".equalsIgnoreCase(opcion))
    {
        String idSesionString = request.getParameter("idSesion");

        try {

            final HashMap activeSessions=MyHttpSessionListener.getInfoSessions();


            if (activeSessions != null) {
                final SessionInfo sessionInfo = (SessionInfo) activeSessions.get(idSesionString);
                if (sessionInfo != null) {
                    final boolean ok = sessionInfo.invalidate();
                    if (ok) {
                        if (m_Log.isDebugEnabled()) m_Log.debug("Sesion con id "+idSesionString+" invalidada ");
                        formulario.setOperacion("invalidarOk");
                    } else {
                       if (m_Log.isErrorEnabled()) m_Log.error("La Sesion con id "+idSesionString+" no ha podido ser invalidada ");
                       formulario.setOperacion("invalidarNoOk");
                    }//if
                } else {
                    if (m_Log.isErrorEnabled()) m_Log.error("No se han invalidado las sesiones");
                    formulario.setOperacion("invalidarNoOk");
                }//if
            } else {
                if (m_Log.isErrorEnabled()) m_Log.error("No se han encontrado sesiones activas");
                formulario.setOperacion("invalidarNoOk");
            }//if
            

           opcion = "oculto";
           
        }catch (Exception e) {
             e.printStackTrace();
        }
    }
    else if("descargar".equalsIgnoreCase(opcion))
    {
        String nomFile = "";
        String ruta="";
        String fileSeparator=System.getProperty("file.separator");

        String fichero = request.getParameter("ficheroDescarga");


        if("ficheroLog".equals(fichero))
        {
            nomFile ="sge.log" ;
            ruta=confLog.getString("log4j.appender.RollingFile.File");
            response.setHeader("Content-Disposition","attachment;filename=sge.log");
        }
        else if("web".equals(fichero))
        {
            nomFile ="web.xml" ;
            ruta=confCommon.getString("SIGP.webapp_base_dir")+fileSeparator+"WEB-INF"+fileSeparator+nomFile;
            response.setHeader("Content-Disposition","attachment;filename=web.xml");
        }
        else if("context".equals(fichero))
        {
            nomFile ="context.xml" ;
            ruta=confCommon.getString("SIGP.webapp_base_dir")+fileSeparator+"META-INF"+fileSeparator+nomFile;
            response.setHeader("Content-Disposition","attachment;filename=context.xml");
        }
        else
        {
            nomFile = fichero+".properties";
            ruta=confCommon.getString("SIGP.webapp_base_dir")+fileSeparator+"WEB-INF"+fileSeparator+"classes"+fileSeparator+nomFile;
            response.setHeader("Content-Disposition","attachment;filename=fichero.txt");

        }
        try{


        FileInputStream archivo = new FileInputStream(ruta);
        int longitud = archivo.available();
        byte[] datos = new byte[longitud];
        archivo.read(datos);
        archivo.close();
        response.setContentType("application/octet-stream");
        ServletOutputStream ouputStream = response.getOutputStream();
        ouputStream.write(datos);
        ouputStream.flush();
        ouputStream.close();
      }catch(Exception e){ e.printStackTrace(); }
    }
    
    else if("otroFicheroRuta".equalsIgnoreCase(opcion))
    {
         String nomFile = "";
        String ruta="";
        String fileSeparator=System.getProperty("file.separator");

        String fichero = request.getParameter("ficheroDescarga");
        nomFile = fichero;
        ruta=confCommon.getString("SIGP.webapp_base_dir")+nomFile;
        response.setHeader("Content-Disposition","attachment;filename=fichero.txt");
        
        if (m_Log.isDebugEnabled()) m_Log.debug("ruta y fichero "+ruta);
        
         try{


        FileInputStream archivo = new FileInputStream(ruta);
        int longitud = archivo.available();
        byte[] datos = new byte[longitud];
        archivo.read(datos);
        archivo.close();
        response.setContentType("application/octet-stream");
        ServletOutputStream ouputStream = response.getOutputStream();
        ouputStream.write(datos);
        ouputStream.flush();
        ouputStream.close();
      }catch(Exception e){ e.printStackTrace(); }

    }
    else if("nivelLog".equalsIgnoreCase(opcion))
    {
        try{
            String LogLevel = request.getParameter("descLevelLog");

            Logger.getRootLogger().setLevel(Level.toLevel(LogLevel));
            formulario.setNivelLog(Logger.getRootLogger().getLevel().toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        opcion = "oculto";
        formulario.setOperacion("modificarNivelLog");


    }
    else if("enviarMail".equalsIgnoreCase(opcion))
    {
        try{
            String email = request.getParameter("emailPrueba");

            MailHelper correo=new MailHelper();

            correo.sendMail(email, "Flexia test mail", "");
            
            formulario.setOperacion("envioEmailOk");
        }
        catch (Exception e)
        {
            formulario.setOperacion("envioEmailNoOk");
            e.printStackTrace();
        }

        opcion = "oculto";

 

    }
    
    else if ("ejecutarConsulta".equalsIgnoreCase(opcion))
    {
        if (m_Log.isDebugEnabled()) m_Log.debug("Recolector de memoria");
        try{
           
             String sql = request.getParameter("sql");
             String organizacion=request.getParameter("codOrganizacion");
             if (m_Log.isDebugEnabled()) m_Log.debug("sql recogida "+sql);
             
             String jndi = UsuariosGruposManager.getInstance().obtenerJNDI(
                        "1", organizacion, String.valueOf(usuario.getAppCod()), params);
             
                String[] paramsNuevos = new String[7];
                paramsNuevos[0] = params[0];
                paramsNuevos[6] = jndi;
                
             Vector resultados=new Vector();
             
             resultados=infoSistemasManager.ejecutarConsulta(paramsNuevos, sql);
             
             
             String resultadoString="";
             
             if (resultados.size()==0) resultadoString="0 resultados";
             
             
             for( int i=0; i<resultados.size();i++)
             {
               Vector fila=(Vector)resultados.get(i);
               for( int j=0; j<fila.size();j++)
                {
                    String columna=(String) fila.get(j);
                    resultadoString=resultadoString+columna+";";
                }
                if((i+1)<resultados.size())resultadoString=resultadoString+"§¥";
                        
                      
             }
             
            formulario.setResultadoString(resultadoString);
            formulario.setResultadoStringExp("");

        }catch (Exception e) {
            if (m_Log.isErrorEnabled()) m_Log.error("Error al ejecutar la consulta");
             e.printStackTrace();
        }
         formulario.setOperacion(opcion);
         opcion = "oculto";
         
    }
 
    else if ("ConsultaListTramOrig".equalsIgnoreCase(opcion)){
        
        try{      
            if (m_Log.isInfoEnabled()) m_Log.info("Opción: "+opcion);
        String organizacion  = request.getParameter("codOrganizacionExp");
                //request.getParameter("codOrganizacion");
        String numExpediente = request.getParameter("numExpediente");
        numExpediente = numExpediente.toUpperCase();
        String sql = confConsultas.getString("SQL_LIST_TRAM_ORIG");
        sql = sql.replaceAll("@EXPEDIENTE",numExpediente);
        if (m_Log.isDebugEnabled()) m_Log.debug("Consulta:"+sql);
        
        // organizacion = request.getParameter("codOrganizacion");
        String jndi = UsuariosGruposManager.getInstance().obtenerJNDI(
                        "1", organizacion, String.valueOf(usuario.getAppCod()), params);
             
                String[] paramsNuevos = new String[7];
                paramsNuevos[0] = params[0];
                paramsNuevos[6] = jndi;
                
             Vector resultadosExp=new Vector();
             resultadosExp=infoSistemasManager.ConsultasAdministrador(paramsNuevos, sql);
              String resultadoStringExp="";
 
             for( int i=0; i<resultadosExp.size();i++){
               Vector fila=(Vector)resultadosExp.get(i);
               for( int j=0; j<fila.size();j++)
                {
                    String columna=(String) fila.get(j);
                    resultadoStringExp=resultadoStringExp+columna+ ";";
                }
                if((i+1)<resultadosExp.size())resultadoStringExp=resultadoStringExp+"§¥";
             }
                  
            if (resultadosExp.size()==1) resultadoStringExp="0 resultados";
             
            formulario.setResultadoString("");
            formulario.setResultadoStringExp(resultadoStringExp);
        }catch (Exception e) {
            if (m_Log.isErrorEnabled()) m_Log.error("Error al ejecutar la consulta");
             e.printStackTrace();
        }
         formulario.setOperacion(opcion);
         opcion = "oculto";       
    }
    else if("ConsultaCodTramite".equalsIgnoreCase(opcion)){
        try{
            if (m_Log.isInfoEnabled()) m_Log.info("Opción: "+opcion);
            String organizacion  = request.getParameter("codOrganizacionExp");
            String procedimiento = (request.getParameter("procedimiento"));
            procedimiento = procedimiento.toUpperCase();
            String sql = confConsultas.getString("SQL_CODIGOS_TRAMITES");
            sql = sql.replaceAll("@PROCEDIMIENTO",procedimiento);
            if (m_Log.isDebugEnabled()) m_Log.debug("Consulta:"+sql);
        
            String jndi = UsuariosGruposManager.getInstance().obtenerJNDI(
            "1", organizacion, String.valueOf(usuario.getAppCod()), params);
             
                String[] paramsNuevos = new String[7];
                paramsNuevos[0] = params[0];
                paramsNuevos[6] = jndi;
                
             Vector resultadosExp=new Vector();
             resultadosExp=infoSistemasManager.ConsultasAdministrador(paramsNuevos, sql);
             String resultadoStringExp="";
 
             for( int i=0; i<resultadosExp.size();i++){
               Vector fila=(Vector)resultadosExp.get(i);
               for( int j=0; j<fila.size();j++)
                {
                    String columna=(String) fila.get(j);
                    resultadoStringExp=resultadoStringExp+columna+ ";";
                }
                if((i+1)<resultadosExp.size())resultadoStringExp=resultadoStringExp+"§¥";
             }
             
            if (resultadosExp.size()==1) resultadoStringExp="0 resultados";
            
            formulario.setResultadoString("");
            formulario.setResultadoStringExp(resultadoStringExp);
        }catch (Exception e) {
            if (m_Log.isErrorEnabled()) m_Log.error("Error al ejecutar la consulta");
             e.printStackTrace();
        }
         formulario.setOperacion(opcion);
         opcion = "oculto"; 
    }
    else if("ConsultaFlujoExp".equalsIgnoreCase(opcion)){
        try{
            if (m_Log.isInfoEnabled()) m_Log.info("Opción: "+opcion);
            String organizacion  = request.getParameter("codOrganizacionExp");
            String procedimiento = (request.getParameter("procedimiento"));
            String numExpediente = (request.getParameter("numExpediente"));
            procedimiento = procedimiento.toUpperCase();
            numExpediente = numExpediente.toUpperCase();
            String sql = confConsultas.getString("SQL_FUJO_EXPEDIENTE");
            sql = sql.replaceAll("@PROCEDIMIENTO",procedimiento);
            sql = sql.replaceAll("@EXPEDIENTE",numExpediente);
            if (m_Log.isDebugEnabled()) m_Log.debug("Consulta:"+sql);
        
            String jndi = UsuariosGruposManager.getInstance().obtenerJNDI
            ("1", organizacion, String.valueOf(usuario.getAppCod()), params);

                String[] paramsNuevos = new String[7];
                paramsNuevos[0] = params[0];
                paramsNuevos[6] = jndi;
                
             Vector resultadosExp=new Vector();
             resultadosExp=infoSistemasManager.ConsultasAdministrador(paramsNuevos, sql);
             String resultadoStringExp="";
 
             for( int i=0; i<resultadosExp.size();i++){
               Vector fila=(Vector)resultadosExp.get(i);
               for( int j=0; j<fila.size();j++)
                {
                    String columna=(String) fila.get(j);
                    resultadoStringExp=resultadoStringExp+columna+ ";";
                }
                if((i+1)<resultadosExp.size())resultadoStringExp=resultadoStringExp+"§¥";
             }

            if (resultadosExp.size()==1) resultadoStringExp="0 resultados";

            formulario.setResultadoString("");
            formulario.setResultadoStringExp(resultadoStringExp);
        }catch (Exception e) {
            if (m_Log.isErrorEnabled()) m_Log.error("Error al ejecutar la consulta");
             e.printStackTrace();
        }
         formulario.setOperacion(opcion);
         opcion = "oculto";       
    }
    else{
      opcion = mapping.getInput();
    }
    request.setAttribute("InformacionSistemaForm",formulario);
    m_Log.debug("perform");
    return (mapping.findForward(opcion));
  }//de la funcion




}//de la clase