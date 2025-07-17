<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="es.altia.agora.business.registro.RegistroValueObject" %>
<%@ page import="es.altia.agora.business.registro.SimpleRegistroValueObject" %>
<%@ page import="es.altia.agora.business.terceros.TercerosValueObject" %>
<%@ page import="es.altia.agora.business.terceros.DomicilioSimpleValueObject" %>
<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm" %>
<%@ page import="es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="es.altia.agora.business.util.TransformacionAtributoSelect"%>
<%@page import="es.altia.util.conexion.AdaptadorSQLBD"%>
<%@ page contentType="text/html;charset=ISO-8859-15" language="java" pageEncoding="ISO-8859-15"%>

<%

    response.setHeader("Cache-control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    String error = (String)request.getAttribute("errorCargaDatos");
    
    if (error == null) {
        RegistroValueObject regBusqueda = (RegistroValueObject) request.getAttribute("regCargaBusqueda");
        BusquedaTercerosForm tercBusqueda = (BusquedaTercerosForm) request.getAttribute("tercCargaBusqueda");
        MantAnotacionRegistroForm mantForm = (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm");

        JSONArray datosGenerales = new JSONArray();

        datosGenerales.put(regBusqueda.getUorCodVisible());
        datosGenerales.put(StringEscapeUtils.unescapeHtml(regBusqueda.getAsunto()));
        // Tal como está ahora, esto siempre va a ser 0.
        datosGenerales.put("0");
        datosGenerales.put(regBusqueda.getCodTipoDoc());
        if (regBusqueda.getDescTipoRem() == null) datosGenerales.put("");
        else datosGenerales.put(regBusqueda.getIdTipoPers());
        datosGenerales.put(regBusqueda.getCodInter());
        datosGenerales.put(regBusqueda.getDomicInter());
        datosGenerales.put(regBusqueda.getNumModInfInt());
        if (regBusqueda.getNumTransporte() == null) datosGenerales.put("");
        else datosGenerales.put(regBusqueda.getNumTransporte());
        if (regBusqueda.getCodTipoTransp() == null) datosGenerales.put("");
        else datosGenerales.put(regBusqueda.getCodTipoTransp());
        if (regBusqueda.getCodAct() == null) datosGenerales.put("");
        else datosGenerales.put(regBusqueda.getCodAct());
        if (regBusqueda.getAutoridad() == null) datosGenerales.put("");
        else datosGenerales.put(StringEscapeUtils.escapeJavaScript(regBusqueda.getAutoridad()));
        datosGenerales.put(regBusqueda.getCodProcedimiento());
        datosGenerales.put(regBusqueda.getDescProcedimiento());
        datosGenerales.put(regBusqueda.getMunProcedimiento());
        if (regBusqueda.getObservaciones() == null) datosGenerales.put("");
        else datosGenerales.put(regBusqueda.getObservaciones());

        // Numero del expediente relacionado.
        if (regBusqueda.getNumExpediente() == null) datosGenerales.put(""); 
        else datosGenerales.put(regBusqueda.getNumExpediente());
        mantForm.setDatosGenerales(datosGenerales);

        // Temas.
        JSONArray datosTemas = new JSONArray();
        for (Object obj: regBusqueda.getListaTemasAsignados()) {
            RegistroValueObject regTema = (RegistroValueObject)obj;
            datosTemas.put(regTema.getCodigoTema());
            datosTemas.put(regTema.getDescTema());
        }
        mantForm.setDatosTemas(datosTemas);

        // Relaciones.
        JSONArray datosRelaciones = new JSONArray();
        SimpleRegistroValueObject simpleRegRel = regBusqueda.getRelaciones().elementAt(0);
        datosRelaciones.put(simpleRegRel.getTipo());
        datosRelaciones.put(simpleRegRel.getEjercicio());
        datosRelaciones.put(simpleRegRel.getNumero());
        mantForm.setDatosRelaciones(datosRelaciones);

        // Documentos.
        JSONArray datosDocumentos = new JSONArray();
        for (Object obj: regBusqueda.getListaDocsAsignados()) {
            RegistroValueObject regDoc = (RegistroValueObject)obj;
            datosDocumentos.put(regDoc.getEntregado());
            datosDocumentos.put(regDoc.getNombreDoc());
            datosDocumentos.put(regDoc.getTipoDoc());
            datosDocumentos.put(regDoc.getFechaDoc());
            datosDocumentos.put(regDoc.getDoc());
        }
        mantForm.setListaDocsAsignados(regBusqueda.getListaDocsAsignados());
        mantForm.setDatosDocs(datosDocumentos);
        
        //Entregados Anterior 	
        JSONArray datosAnteriores=new JSONArray();	
        for(Object obj:regBusqueda.getListaDocsAnteriores()){	
            RegistroValueObject regDoc=(RegistroValueObject)obj;	
            datosAnteriores.put(regDoc.getTipoDocAnterior());	
            datosAnteriores.put(regDoc.getNombreDocAnterior());	
            datosAnteriores.put(regDoc.getOrganoDocAnterior());	
            datosAnteriores.put(regDoc.getFechaDocAnterior());	
        }	
        mantForm.setListaDocsAnteriores(regBusqueda.getListaDocsAnteriores());	
        mantForm.setDatosAnts(datosAnteriores);

        // Terceros interesados
        JSONArray datosInteresados = new JSONArray();    
        for (Object objTercero: tercBusqueda.getListaInteresados()) {
            GeneralValueObject interesado = (GeneralValueObject)objTercero;
            JSONArray datosUnTercero = new JSONArray();
            datosUnTercero.put(interesado.getAtributo("codigoTercero"));
            datosUnTercero.put(interesado.getAtributo("versionTercero"));
            String interesadoJS = AdaptadorSQLBD.js_escape(TransformacionAtributoSelect.replace((String)(interesado.getAtributo("titular")), "'", "\'"));
            datosUnTercero.put(StringEscapeUtils.escapeJavaScript(interesadoJS));
            datosUnTercero.put(interesado.getAtributo("descRol"));
            datosUnTercero.put(interesado.getAtributo("domicilio"));
            String domicilioJS = AdaptadorSQLBD.js_escape(TransformacionAtributoSelect.replace((String)(interesado.getAtributo("descDomicilio")), "'", "\'"));            
            datosUnTercero.put(StringEscapeUtils.escapeJavaScript(domicilioJS));
            datosUnTercero.put(interesado.getAtributo("porDefecto"));
            datosUnTercero.put(interesado.getAtributo("rol"));
            datosUnTercero.put(interesado.getAtributo("telefono"));
            datosUnTercero.put(interesado.getAtributo("email"));
            datosUnTercero.put(interesado.getAtributo("pais"));
            datosUnTercero.put(interesado.getAtributo("provincia"));
            datosUnTercero.put(StringEscapeUtils.escapeJavaScript((String)(interesado.getAtributo("municipio"))));
            datosUnTercero.put(interesado.getAtributo("cp"));
            datosUnTercero.put(interesado.getAtributo("tip"));
            datosUnTercero.put(interesado.getAtributo("doc"));
            datosInteresados.put(datosUnTercero);
        }

        mantForm.setDatosInteresados(datosInteresados);

        response.setCharacterEncoding("ISO-8859-1");
        
        
        out.println(mantForm.toJSONStringDescripcion());
    } else {
        JSONObject jsonError = new JSONObject();
        jsonError.put("codigoError", error);
        out.println(jsonError.toString());
    }
%>