<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@ page import="org.apache.struts.upload.FormFile"%>
<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="es.altia.agora.business.registro.RegistroValueObject"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.agora.interfaces.user.web.registro.DocumentoRegistroForm"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import=" es.altia.agora.technical.ConstantesDatos"%>
<%@ page import="es.altia.agora.business.registro.RegistroValueObject"%>
<html>
<head><jsp:include page="/jsp/formularios/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Gestión de Ficheros</title>
  <script type="text/javascript">
    var frame;
    if(parent.mainFrame){
        frame = parent.mainFrame;
    } else {
        frame = parent;
    }
	// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function inicializar(){
        <%DocumentoRegistroForm fichaForm = (DocumentoRegistroForm)session.getAttribute("DocumentoRegistroForm");
        Log _log = LogFactory.getLog(this.getClass());
        String codigo = fichaForm.getCodigo();
        if (session.getAttribute("DocumentoRegistroForm") != null) {
            MantAnotacionRegistroForm regESForm = (MantAnotacionRegistroForm)session.getAttribute("MantAnotacionRegistroForm");
            Vector docs = regESForm.getListaDocsAsignados();
            _log.debug("............................. CODIGO: "+codigo);
            _log.debug("............................. DOCS (antes): "+docs.size());
            
            
            // #231150: creamos una lista auxiliar con los documentos asignados al registro con estado no eliminado
            ArrayList<RegistroValueObject> listaDocsAux = new ArrayList<RegistroValueObject>();
            for(int cont=0; cont<docs.size(); cont++){
                RegistroValueObject docAux = (RegistroValueObject)docs.get(cont);
                if(docAux.getEstadoDocumentoRegistro()!=ConstantesDatos.ESTADO_DOCUMENTO_ELIMINADO)
                    listaDocsAux.add(docAux);
            }

            //docs.removeElementAt(Integer.parseInt(codigo));
            // #231150: obtenemos los objetos de documento de la lista original y de la auxiliar, si no coinciden se busca en que posicion está realmente
            // el elemento en la lista original a partir del elemento de la lista auxiliar
            RegistroValueObject registroAux = (RegistroValueObject)listaDocsAux.get(Integer.parseInt(codigo));
            RegistroValueObject reg = (RegistroValueObject)docs.get(Integer.parseInt(codigo));
            if(registroAux!=reg){
                for(int cont=0; cont<docs.size(); cont++){
                    RegistroValueObject doc = (RegistroValueObject)docs.get(cont);
                    if(doc==registroAux){
                        codigo=String.valueOf(cont);
                        break;
                    }
                }
            }

            RegistroValueObject registro = (RegistroValueObject)docs.get(Integer.parseInt(codigo));
            registro.setEstadoDocumentoRegistro(ConstantesDatos.ESTADO_DOCUMENTO_ELIMINADO);
            
            _log.debug("............................. DOCS (despues): "+docs.size());
            regESForm.setListaDocsAsignados(docs);%>
        <%}%>
        frame.listaDocEntregados[<%=codigo%>]='N';
        frame.actualizaEliminarDoc();

    }
  </script>
</head>
<body onload="inicializar();">
</body>
</html>
