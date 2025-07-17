<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%@ page import="org.apache.struts.upload.FormFile"%>
<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="es.altia.agora.business.sge.DefinicionProcedimientosValueObject"%>
<%@ page import="es.altia.agora.business.registro.RegistroValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.xpdl.ListaProcedimientosXPDLForm"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.agora.interfaces.user.web.xpdl.DocumentoXPDLForm"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Calendar"%>
<html>
<head><jsp:include page="/jsp/formularios/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Gestión de Ficheros</title>
  <script type="text/javascript">

    var error = '<%=(String)request.getAttribute("error_importacion_xpdl")%>';

    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function inicializar(){ 

        if(error!=null && error!="0"){
            // Si hay un error se muestra el mensaje correspondiente y no se actualiza la lista de documentos
            window.close();
            window.opener.mostrarError(error);
        }        
        else{
        <%
           request.removeAttribute("error_importacion_xpdl");
           DocumentoXPDLForm docXPDLForm = (DocumentoXPDLForm)session.getAttribute("DocumentoXPDLForm");
            Log _log = LogFactory.getLog(this.getClass());

            FormFile fichero = docXPDLForm.getFichero();

            byte[] datos = null;
            String titulo = docXPDLForm.getTituloFichero();


                if (session.getAttribute("DocumentoXPDLForm") != null)
                {
                    DefinicionProcedimientosValueObject dpVO = docXPDLForm.getProcedimiento();
                    ListaProcedimientosXPDLForm listaProcsForm = (ListaProcedimientosXPDLForm)session.getAttribute("ListaProcedimientosXPDLForm");
                    Vector<DefinicionProcedimientosValueObject> listaProcedimientos = listaProcsForm.getListaProcedimientos();

                    // Si se ha subido el fichero por POST
                    if(fichero!=null && fichero.getFileData()!=null && fichero.getFileSize()>0){
                         // Si sube por POST un objeto de tipo File
                        //nombre = fichero.getFileName();
                        //reg.setNombreDoc(nombre);
                       if (session.getAttribute("ListaProcedimientosXPDLForm")!=null) _log.debug("Fichero no nulo"+listaProcedimientos.size());

                        datos = fichero.getFileData();


                    } else {
                        // Caso fichero vacío (solo nombre de documento)
                        _log.debug("Sin fichero (fichero nulo)");
                        datos = new byte[0];
                    }

                    listaProcedimientos.add(dpVO);
                    listaProcsForm.setListaProcedimientos(listaProcedimientos);
                 }

           %>

            var listaProcs= new Array();
             var conProc=0;
        <logic:iterate id="proc" name="ListaProcedimientosXPDLForm" property="listaProcedimientos">

            listaProcs[conProc] = ['<bean:write name="proc" property="txtCodigo"/>','<bean:write name="proc" property="txtDescripcion"/>'];
            conProc++;
        </logic:iterate>

            if(error!=null && error=="0")
                window.opener.mostrarMensaje(error);
            
            window.opener.actualizaProcs(listaProcs);

            window.close();
       }
    }
  </script>
</head>
<body onload="inicializar();">
</body>
</html>
