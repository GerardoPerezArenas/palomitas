
<%@page import="es.altia.agora.interfaces.user.web.registro.EntregadosAnteriorForm"%>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>

<%@ page import="org.apache.struts.upload.FormFile"%>
<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="es.altia.agora.business.registro.RegistroValueObject"%>
<%@ page import="org.apache.commons.logging.LogFactory"%> 
<%@ page import="es.altia.agora.interfaces.user.web.registro.DocumentoRegistroForm"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.*"%>
<%@ page import="es.altia.agora.technical.Fecha"%>
<%@ page import="es.altia.agora.technical.ConstantesDatos"%>



<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>

<%@ page import="org.apache.struts.upload.FormFile"%>
<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="es.altia.agora.business.registro.RegistroValueObject"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.agora.interfaces.user.web.registro.DocumentoRegistroForm"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.*"%>
<%@ page import="es.altia.agora.technical.Fecha"%>
<%@ page import="es.altia.agora.technical.ConstantesDatos"%>



<html>
<head><jsp:include page="/jsp/formularios/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Gestión de Ficheros</title>
  <script type="text/javascript">

    var ESTADO_DOCUMENTO_REGISTRO_ELIMINADO = '<%=ConstantesDatos.ESTADO_DOCUMENTO_ELIMINADO%>';
        
    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function inicializar(){
        <%
        EntregadosAnteriorForm fichaForm = (EntregadosAnteriorForm)session.getAttribute("EntregadosAnteriorForm");
        Log _log = LogFactory.getLog(this.getClass());
            
        String fecha= fichaForm.getFechaDocumento();
        String titulo = fichaForm.getNombreDocumento();
        String organo = fichaForm.getOrgano();
        String tipo=fichaForm.getTipoDocumento();

         
                
        if(titulo!=null && titulo.length()>=1){               
            if (session.getAttribute("EntregadosAnteriorForm") != null){   
                MantAnotacionRegistroForm regESForm = (MantAnotacionRegistroForm)session.getAttribute("MantAnotacionRegistroForm");
                Vector docs =regESForm.getListaDocsAnteriores();    
                RegistroValueObject reg = new RegistroValueObject();
                
                if(!fichaForm.isModificando()){
                    reg.setNombreDocAnterior(titulo);
                    reg.setTipoDocAnterior(tipo);
                    reg.setFechaDocAnterior(fecha);
                    reg.setOrganoDocAnterior(organo);
                    reg.setEstadoEntregadoAnterior(ConstantesDatos.ESTADO_DOCUMENTO_NUEVO);
                    _log.debug(reg);
                    docs.add(reg);
                // Si se esta modificando un documento agregado anteriormente
                }else if (fichaForm.isModificando()){
                    
                    _log.debug("codigoOculto "+session.getAttribute("codigo"));
                    int codigo =(Integer)session.getAttribute("codigo");
                         
                    Vector docsAux = regESForm.getListaDocsAnteriores();
                    RegistroValueObject registroAux = new RegistroValueObject();
                    registroAux = (RegistroValueObject)docsAux.get(codigo);
                    String nombreMod=registroAux.getNombreDocAnterior();                              
                    reg.setNombreDocAnterior(titulo);
                    registroAux.setFechaDocAnterior(fecha);
                    registroAux.setNombreDocAnterior(titulo);
                    registroAux.setTipoDocAnterior(tipo);
                    registroAux.setOrganoDocAnterior(organo);
                    registroAux.setEstadoEntregadoAnterior(ConstantesDatos.ESTADO_DOCUMENTO_MODIFICADO);
                    registroAux.setNombreDocAnteriorMod(nombreMod);
                    fichaForm.setModificando(false);
                                   
                        int posicion=0;
                        for(int h=0;h<docs.size();h++){
                            RegistroValueObject doc = (RegistroValueObject)docs.get(h);
                            if (doc.getNombreDocAnterior().equals(registroAux.getNombreDocAnterior())) {
                                posicion=docs.indexOf(doc);
                                reg.setNombreDocAnterior(titulo);
                                break;
                             }
                        }
                     docs.set(posicion, registroAux);   
                }
                regESForm.setListaDocsAnteriores(docs); 
                _log.debug("lista Anteriores"+regESForm.getListaDocsAnteriores());
            }
         } 
         
       %>            
       // Se vuelve a crear la lista para mostrar en la tabla                
        var listaDocsAnt = new Array();
        cont=0;
        
        <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaDocsAnteriores">
       
       
        
                       
            listaDocsAnt[cont]= [ '<bean:write name="elemento" property="tipoDocAnterior" filter="false"/>',
                               '<bean:write name="elemento" property="nombreDocAnterior"/>', 
                               '<bean:write name="elemento" property="organoDocAnterior"/>',
                               '<bean:write name="elemento" property="fechaDocAnterior"/>'];                
            cont= cont+1;
         
        </logic:iterate>
                
        self.parent.opener.retornoXanelaAuxiliar(listaDocsAnt);
    }
  </script>
</head>
<body onload="inicializar();">
</body>
</html>
