<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject" %>
<%@page import="java.util.Vector" %>
<%@page import="java.util.ArrayList" %>
<%@page import="es.altia.agora.business.administracion.mantenimiento.PermisoProcedimientosRestringidosVO" %>
<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<title> Oculto Directiva Procedimientos Restringidos </title>

<script type="text/javascript">
function redirecciona(){
    <%
        String accion = (String)request.getAttribute("opcion_oculto");
    %>
    var contador = 0;
    var listaCodEntidades = new Array();
    var listaDescEntidades = new Array();
    var accion = "<%=accion%>";

    if(accion!=null && accion=="cargarEntidades"){
    // Se carga el combo de entidades
        <%
            Vector entidades = (Vector)request.getAttribute("entidades_seleccion_procedimientos_restringidos");
            for(int i=0;entidades!=null && i<entidades.size();i++)
            {
                GeneralValueObject entidad = (GeneralValueObject)entidades.get(i);
                String codEntidad = (String)entidad.getAtributo("codEntidad");
                String descEntidad = (String)entidad.getAtributo("descEntidad");
         %>
                listaCodEntidades[contador]  = "<%=codEntidad%>";
                listaDescEntidades[contador] = "<%=descEntidad%>";
                contador++;
        <%
            }// for
        %>
        
        parent.mainFrame.cargarComboEntidades(listaCodEntidades,listaDescEntidades);
    }
    else
    if(accion!=null && accion=="cargarProcedimientos"){
    // Se carga el combo de procedimientos
        var listaCodProcedimientos = new Array();
        var listaDescProcedimientos = new Array();
        var contadorProc = 0;
        <%
            //ArrayList procedimientos = (ArrayList)request.getAttribute("procedimientos_restringidos");
            ArrayList procedimientos = (ArrayList)request.getAttribute("procedimientos_restringidos_combo");

            for(int i=0;procedimientos!=null && i<procedimientos.size();i++)
            {
                PermisoProcedimientosRestringidosVO proc = (PermisoProcedimientosRestringidosVO)procedimientos.get(i);
                String codProcedimiento   = proc.getCodProcedimiento().trim();
                String codMunicipio          = proc.getCodMunicipio();
                String codEntidad            = proc.getCodEntidad();

                String descOrganizacion   = proc.getDescOrganizacion();
                String descEntidad           = proc.getDescEntidad();
                String descProcedimiento = proc.getDescProcedimiento().trim();
                String valor                     = codMunicipio + ";" + codEntidad + ";" + codProcedimiento;
        %>                            
                listaCodProcedimientos[contadorProc]  = "<%=codProcedimiento%>";
                listaDescProcedimientos[contadorProc] = "<%=descProcedimiento%>";

                contadorProc++;
        <%
            }// for
        %>        
        parent.mainFrame.cargarComboProcedimientos(listaCodProcedimientos,listaDescProcedimientos);
    }


  }
</script>
</head>
<body onLoad="redirecciona();">
</body>
</html>