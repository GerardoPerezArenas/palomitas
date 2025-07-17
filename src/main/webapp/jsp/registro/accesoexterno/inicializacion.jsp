<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ page import="es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm"%>
<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm" %>
<%@ page import="es.altia.catalogoformularios.model.solicitudes.vo.FormularioTramitadoVO"%>
<%@ page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@ page import="es.altia.catalogoformularios.util.DateOperations"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>
<script type="text/javascript">
    <% 
    BusquedaTercerosForm busqTercerosForm = (BusquedaTercerosForm) session.getAttribute("BusquedaTercerosForm");
    MantAnotacionRegistroForm mantAntForm = (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm");
    %>
     
    var listaDocsAsignados = new Array();
    var listaDocsAnteriores=new Array();
    var listaCodAsuntos = new Array();
    var listaDescAsuntos = new Array();
    var listaUniAsuntos = new Array();
    var listaRelAnot = new Array();
    var listaTemasAsignados = new Array();
    var listaInteresadosAnot = new Array();
    var listaNumsExpedientessRel = new Array();
    var listaIdsExpedientesRel = new Array();
    var cod_tiposDocInt = new Array();
    var desc_tiposDocInt = new Array();
    var codsProcs = new Array();
    var descsProcs = new Array();
    var orgsProcs = new Array();
    var listaDigitProcs = new Array();
    var hayAnexosForms='no';
    
    function tratarDatosRecibidos(){
        var datos  = new Array();           

        datos[1] = '<bean:write name="MantAnotacionRegistroForm" property="diaAnotacion"/>';
        datos[2] = '<bean:write name="MantAnotacionRegistroForm" property="mesAnotacion"/>';
        datos[3] = '<bean:write name="MantAnotacionRegistroForm" property="anoAnotacion"/>';
        datos[4] = '<bean:write name="MantAnotacionRegistroForm" property="dia_doc"/>';
        datos[5] = '<bean:write name="MantAnotacionRegistroForm" property="mes_doc"/>';
        datos[6] = '<bean:write name="MantAnotacionRegistroForm" property="ano_doc"/>';
        datos[7] = '<bean:write name="MantAnotacionRegistroForm" property="txtHoraDoc"/>';
        datos[8] = '<bean:write name="MantAnotacionRegistroForm" property="txtMinDoc"/>';
        datos[9] = unescape('<bean:write name="MantAnotacionRegistroForm" property="asunto"/>');  
        datos[10] = '<bean:write name="MantAnotacionRegistroForm" property="cod_tipoTransporte"/>';
        datos[11] = '<bean:write name="MantAnotacionRegistroForm" property="txtCodigoDocumento"/>';
        datos[12] = '<bean:write name="MantAnotacionRegistroForm" property="cbTipoEntrada"/>';
        datos[13] = '<bean:write name="MantAnotacionRegistroForm" property="cod_tipoRemitente"/>';
        datos[14] = '<bean:write name="MantAnotacionRegistroForm" property="txtNumTransp"/>';
        datos[15] = '<bean:write name="MantAnotacionRegistroForm" property="cod_dptoDestino"/>';
        datos[16] = '<bean:write name="MantAnotacionRegistroForm" property="cod_uniRegDestino"/>';
        datos[17] = '<bean:write name="MantAnotacionRegistroForm" property="cod_departamentoOrixe"/>';
        datos[18] = '<bean:write name="MantAnotacionRegistroForm" property="cod_unidadeRexistroOrixe"/>';
        datos[19] = '<bean:write name="MantAnotacionRegistroForm" property="cod_orgDestino"/>';
        datos[20] = '<bean:write name="MantAnotacionRegistroForm" property="cod_entDestino"/>';
        datos[21] = '<bean:write name="MantAnotacionRegistroForm" property="cod_orgOrigen"/>';
        datos[22] = '<bean:write name="MantAnotacionRegistroForm" property="cod_entidadOrigen"/>';
        datos[23] = '<bean:write name="MantAnotacionRegistroForm" property="txtNomeDocumento"/>';
        datos[24] = '<bean:write name="MantAnotacionRegistroForm" property="txtNomeTipoRemitente"/>';
        datos[25] = '<bean:write name="MantAnotacionRegistroForm" property="desc_tipoTransporte"/>';
        datos[26] = '<bean:write name="MantAnotacionRegistroForm" property="cod_actuacion"/>';
        datos[27] = '<bean:write name="MantAnotacionRegistroForm" property="txtNomeActuacion"/>';
        datos[28] = '<bean:write name="MantAnotacionRegistroForm" property="desc_orgDestino"/>';
        datos[29] = '<bean:write name="MantAnotacionRegistroForm" property="desc_entDestino"/>';
        datos[30] = '<bean:write name="MantAnotacionRegistroForm" property="desc_dptoDestino"/>';
        datos[31] = '<bean:write name="MantAnotacionRegistroForm" property="desc_uniRegDestino"/>';
        datos[32] = '<bean:write name="MantAnotacionRegistroForm" property="desc_orgOrigen"/>';
        datos[33] = '<bean:write name="MantAnotacionRegistroForm" property="desc_entidadOrigen"/>';
        datos[34] = '<bean:write name="MantAnotacionRegistroForm" property="desc_departamentoOrixe"/>';
        datos[35] = '<bean:write name="MantAnotacionRegistroForm" property="desc_unidadeRexistroOrixe"/>';
        datos[36] = '<bean:write name="MantAnotacionRegistroForm" property="tipoRegistroOrigen"/>';
        datos[37] = '<bean:write name="MantAnotacionRegistroForm" property="txtExp2Orixe"/>';
        datos[38] = '<bean:write name="MantAnotacionRegistroForm" property="txtExp1Orixe"/>';
        datos[39] = '<bean:write name="MantAnotacionRegistroForm" property="codTerc"/>';
        datos[40] = '<bean:write name="MantAnotacionRegistroForm" property="codDomTerc"/>';
        datos[41] = '<bean:write name="MantAnotacionRegistroForm" property="numModifTerc"/>';
        datos[42] = '<bean:write name="MantAnotacionRegistroForm" property="cod_uniRegDestinoORD"/>';
        datos[43] = '<bean:write name="MantAnotacionRegistroForm" property="cod_uor"/>';      
        datos[44] = '<bean:write name="MantAnotacionRegistroForm" property="ano"/>';
        datos[45] = '<bean:write name="MantAnotacionRegistroForm" property="numero"/>';      
        datos[46] = '<bean:write name="MantAnotacionRegistroForm" property="txtHoraEnt"/>';
        datos[47] = '<bean:write name="MantAnotacionRegistroForm" property="txtMinEnt"/>';
        datos[48] = '<bean:write name="MantAnotacionRegistroForm" property="abiertCerrado"/>';
        datos[49] = '<bean:write name="MantAnotacionRegistroForm" property="desc_dptoDestinoORD"/>';
        datos[50] = "<str:escape><bean:write name="MantAnotacionRegistroForm" property="desc_uniRegDestinoORD" filter="false"/></str:escape>";
        datos[51] = '<bean:write name="MantAnotacionRegistroForm" property="hayTexto"/>';
        datos[52] = unescape('<bean:write name="MantAnotacionRegistroForm" property="textoDiligencia"/>');
        datos[53] = '<bean:write name="MantAnotacionRegistroForm" property="estadoAnotacion"/>';
        datos[54] = unescape('<bean:write name="MantAnotacionRegistroForm" property="txtDiligenciasAnulacion"/>');
        datos[55] = '<bean:write name="MantAnotacionRegistroForm" property="ejercicioAnotacionContestada"/>';
        datos[56] = '<bean:write name="MantAnotacionRegistroForm" property="numeroAnotacionContestada"/>';
        datos[57] = '<bean:write name="MantAnotacionRegistroForm" property="txtExp1"/>';
        datos[58] = '<bean:write name="MantAnotacionRegistroForm" property="cod_procedimiento"/>';
        datos[59] = "<str:escape><bean:write name="MantAnotacionRegistroForm" property="desc_procedimiento"  filter="false"/></str:escape>";    
        datos[60] = '<bean:write name="MantAnotacionRegistroForm" property="observaciones" />';
        datos[61] = '<bean:write name="MantAnotacionRegistroForm" property="autoridad" />'
        datos[62] = '<bean:write name="MantAnotacionRegistroForm" property="codRolDefecto" />';
        datos[63] = "<str:escape><bean:write name="MantAnotacionRegistroForm" property="descRolDefecto" filter="false"/></str:escape>";
        datos[64] = '<bean:write name="MantAnotacionRegistroForm" property="mun_procedimiento"/>';
        datos[65] = '<bean:write name="MantAnotacionRegistroForm" property="codAsunto"/>';
        if (datos[65] == null || datos[65] == 'null') datos[65] = '';
        datos[66] = '<bean:write name="MantAnotacionRegistroForm" property="codProcedimientoRoles"/>';
        datos[67] = '<bean:write name="MantAnotacionRegistroForm" property="fechaDoc"/>';
        datos[68] = '<bean:write name="MantAnotacionRegistroForm" property="asuntoAnotacionBaja"/>';
        datos[69] = '<bean:write name="MantAnotacionRegistroForm" property="fechaBajaAsuntoCodificadoRegistro"/>';
        datos[70] = '<bean:write name="MantAnotacionRegistroForm" property="txtSegEnt"/>';
        datos[71]='<bean:write name="MantAnotacionRegistroForm" property="codigoSga"/>';	
        datos[72]='<bean:write name="MantAnotacionRegistroForm" property="expedienteSga"/>';

        var mostrarGenerarModeloVal     = '<bean:write name="MantAnotacionRegistroForm" property="mostrarGenerarModelo"/>';
        document.forms[0].mostrarGenerarModelo.value = mostrarGenerarModeloVal;
        mostrarGenerarModelo(tipoActual);
        
        var tipoDocInteresadoOblig     = '<bean:write name="MantAnotacionRegistroForm" property="tipoDocInteresadoOblig"/>';
        document.forms[0].asuntoConTipoDocOblig.value = tipoDocInteresadoOblig;

        var bloquearDestino=<bean:write name="MantAnotacionRegistroForm" property="bloquearDestino"/>;
        var bloquearProcedimiento=<bean:write name="MantAnotacionRegistroForm" property="bloquearProcedimiento"/>
        var fecha = '<bean:write name="MantAnotacionRegistroForm" property="fechaHoraHoy"/>';

        var aux = '<bean:write name="MantAnotacionRegistroForm" property="procesarBuzon"/>';
        if(aux)
          document.forms[0].pendienteBuzon.value="si";
        else
            document.forms[0].pendienteBuzon.value="no";
        
        // Comprobamos si el registro es telematico, no tiene expedientes asociados y es modificable
        var esAnotTelemModificable = false;        
        <logic:present name="MantAnotacionRegistroForm" property="registroTelematicoModicable">
            esAnotTelemModificable = <bean:write name="MantAnotacionRegistroForm" property="registroTelematicoModicable"/>
        </logic:present>
        
        // Documentos
        recuperarDatosDocsAsignados();
        // Documentos aportados anteriormente
        recuperarDatosDocsAnteriores();
        // Asuntos
        recuperarListadoAsuntos();
        // Procedimientos
        recuperarListadoProcedimientos();
        // Tipos documento interesado
        recuperarListadoTiposDocInt();
        // Relaciones entre anotaciones
        recuperarListaRelaciones();
        // Temas asignados
        recuperarListaTemasAsignados();
        // Interesados
        recuperarListaInteresados();
        // Expedientes relacionados
        recuperarListaExpedientesRel();
                 

        // Llamamos a la funcion de la jsp principal encargada de carga los datos en la interfaz
        recuperaDatos(datos,new Array(),listaTemasAsignados,cod_tiposDocumentos,
            desc_tiposDocumentos, act_tiposDocumentos,cod_tiposDocumentosAlta, desc_tiposDocumentosAlta, act_tiposDocumentosAlta,	
            new Array(), new Array(), cod_tiposTransportes, desc_tiposTransportes, act_tiposTransportes,
            cod_tiposRemitentes, desc_tiposRemitentes, act_tiposRemitentes, cod_temas, desc_temas,cod_tiposDocInt, desc_tiposDocInt, 
            0, 0, fecha, listaDocsAsignados,listaDocsAnteriores,new Array(), new Array(),listaInteresadosAnot, listaRelAnot,
            listaUniAsuntos, listaCodAsuntos, listaDescAsuntos, cod_roles, desc_roles, defecto_roles,false,false, bloquearDestino,listaNumsExpedientessRel,
            listaIdsExpedientesRel,<bean:write name="MantAnotacionRegistroForm" property="registroTelematico"/>,
            esAnotTelemModificable,<bean:write name="MantAnotacionRegistroForm" property="procedimientoDigitalizacion"/>,
            <bean:write name="MantAnotacionRegistroForm" property="finDigitalizacion"/>,bloquearProcedimiento);
    }
    
    function recuperarDatosDocsAsignados(){
        var index=0;
        <logic:iterate id="elementoDoc" name="MantAnotacionRegistroForm" property="listaDocsAsignados">

            var str='';
            if('<bean:write name="elementoDoc" property="entregado"/>'=='S') str='SI';
            else if('<bean:write name="elementoDoc" property="entregado"/>'=='N') str='NO';
            else str='';
            if(tipoActual == "E"){
                listaDocsAsignados[index]= [str, '<bean:write name="elementoDoc" property="nombreDoc"/>',
                    '<bean:write name="elementoDoc" property="tipoDoc"/>','<bean:write name="elementoDoc" property="fechaDoc"/>',
                    '<bean:write name="elementoDoc" property="compulsado"/>','<bean:write name="elementoDoc" property="doc"/>',
                    '<bean:write name="elementoDoc" property="descripcionTipoDocumental"/>','<bean:write name="elementoDoc" property="idDocumento"/>'];                
            } else {
                listaDocsAsignados[index]= [str, '<bean:write name="elementoDoc" property="nombreDoc"/>',
                    '<bean:write name="elementoDoc" property="tipoDoc"/>','<bean:write name="elementoDoc" property="fechaDoc"/>',
                    '<bean:write name="elementoDoc" property="cotejado"/>','<bean:write name="elementoDoc" property="doc"/>',
                    '<bean:write name="elementoDoc" property="idDocumento"/>'];
            }
            index= index +1;
        </logic:iterate>
    }
    
    function recuperarDatosDocsAnteriores(){
        var index=0;	
        <logic:iterate id="elementoDocAnt" name="MantAnotacionRegistroForm" property="listaDocsAnteriores">	
           listaDocsAnteriores[index]=['<bean:write name="elementoDocAnt" property="tipoDocAnterior"/>',	
               '<bean:write name="elementoDocAnt" property="nombreDocAnterior"/>',	
               '<bean:write name="elementoDocAnt" property="organoDocAnterior"/>',	
               '<bean:write name="elementoDocAnt" property="fechaDocAnterior"/>'];	
           index=index+1;	
        </logic:iterate>
    }
    
    function recuperarListadoAsuntos(){
        var index = 0;

        <logic:present name="MantAnotacionRegistroForm" property="listaAsuntos">
            <logic:iterate id="elementoAsunto" name="MantAnotacionRegistroForm" property="listaAsuntos">
                listaUniAsuntos[index] = '<bean:write name="elementoAsunto" property="unidadRegistro"/>';
                listaCodAsuntos[index]  ='<bean:write name="elementoAsunto" property="codigo"/>';
                listaDescAsuntos[index] ="<str:escape><bean:write name="elementoAsunto" property="descripcion" filter="false"/></str:escape>";
                index++;
            </logic:iterate>
        </logic:present>
    }
        
    function recuperarListadoProcedimientos(){
        var index = 0;

        <logic:present name="MantAnotacionRegistroForm" property="listaProcedimientos">
            <logic:iterate id="elemProc" name="MantAnotacionRegistroForm" property="listaProcedimientos"> 
                codsProcs[index]='<bean:write name="elemProc" property="txtCodigo"/>';
                descsProcs[index]='<bean:write name="elemProc" property="txtDescripcion"/>';
                orgsProcs[index]='<bean:write name="elemProc" property="codMunicipio"/>';
                listaDigitProcs[index]= '<bean:write name="elemProc" property="procedimientoDigit"/>';
                index++;
            </logic:iterate>
            recuperaListaProcedimientos(codsProcs,descsProcs,orgsProcs, listaDigitProcs);
        </logic:present>
    }
    
    function recuperarListadoTiposDocInt(){
        <logic:present name="MantAnotacionRegistroForm" property="listaTiposIdInteresado">
            <logic:iterate id="elTipoDocInt" name="MantAnotacionRegistroForm" property="listaTiposIdInteresado">
            cod_tiposDocInt['<bean:write name="elTipoDocInt" property="orden"/>']='<bean:write name="elTipoDocInt" property="codigo"/>';
            desc_tiposDocInt['<bean:write name="elTipoDocInt" property="orden"/>']='<bean:write name="elTipoDocInt" property="descripcion"/>';
            </logic:iterate>
        </logic:present>
    }
    
    function recuperarListaRelaciones(){
        var i=0;
        <logic:iterate id="elementoRel" name="MantAnotacionRegistroForm" property="relaciones">
            listaRelAnot[i] = ['<bean:write name="elementoRel" property="tipo"/>',
                '<bean:write name="elementoRel" property="ejercicio"/>',
                '<bean:write name="elementoRel" property="numero"/>'];
            i++;
        </logic:iterate>
    }
    
    function recuperarListaTemasAsignados(){
        var i=0;
        <logic:iterate id="elementoTema" name="MantAnotacionRegistroForm" property="listaTemasAsignados">
        listaTemasAsignados[i++]= [ '<bean:write name="elementoTema" property="codigoTema"/>', '<bean:write name="elementoTema" property="descTema"/>'];
        </logic:iterate>
    }
    
    function recuperarListaInteresados(){
        var j=0;
        
        <%
        Vector listaInterAnot = (Vector)busqTercerosForm.getListaInteresados();
        int indexInter = 0;
        
        for(indexInter=0;indexInter<listaInterAnot.size();indexInter++){
        %>
            listaInteresadosAnot[j] = ['<%=(String)((GeneralValueObject)listaInterAnot.get(indexInter)).getAtributo("codigoTercero")%>',
                '<%=(String)((GeneralValueObject)listaInterAnot.get(indexInter)).getAtributo("versionTercero")%>',
                '<%=StringEscapeUtils.escapeJavaScript((String)((GeneralValueObject)listaInterAnot.get(indexInter)).getAtributo("titular"))%>',
                '<%=(String)((GeneralValueObject)listaInterAnot.get(indexInter)).getAtributo("descRol")%>',
                '<%=(String)((GeneralValueObject)listaInterAnot.get(indexInter)).getAtributo("domicilio")%>',
                '<%=StringEscapeUtils.escapeJavaScript((String)((GeneralValueObject)listaInterAnot.get(indexInter)).getAtributo("descDomicilio"))%>',
                '<%=(String)((GeneralValueObject)listaInterAnot.get(indexInter)).getAtributo("porDefecto")%>',
                '<%=(String)((GeneralValueObject)listaInterAnot.get(indexInter)).getAtributo("rol")%>',
                '<%=(String)((GeneralValueObject)listaInterAnot.get(indexInter)).getAtributo("telefono")%>',
                '<%=(String)((GeneralValueObject)listaInterAnot.get(indexInter)).getAtributo("email")%>',
                '<%=(String)((GeneralValueObject)listaInterAnot.get(indexInter)).getAtributo("pais")%>',
                '<%=(String)((GeneralValueObject)listaInterAnot.get(indexInter)).getAtributo("provincia")%>',
                '<%=StringEscapeUtils.escapeJavaScript((String)((GeneralValueObject)listaInterAnot.get(indexInter)).getAtributo("municipio"))%>',
                '<%=(String)((GeneralValueObject)listaInterAnot.get(indexInter)).getAtributo("cp")%>',
                '<%=(String)((GeneralValueObject)listaInterAnot.get(indexInter)).getAtributo("tip")%>',
                '<%=(String)((GeneralValueObject)listaInterAnot.get(indexInter)).getAtributo("doc")%>',
            ];

            j++;
        <% } %>
    }
    
    function recuperarListaExpedientesRel(){
        var contNumExpedientes=0;
        <logic:iterate id="elementoNumExp" name="MantAnotacionRegistroForm" property="listaNumExpedientesRelacionados">
           listaNumsExpedientessRel[contNumExpedientes] = ['<bean:write name="elementoNumExp"/>'];
           listaIdsExpedientesRel[contNumExpedientes] = contNumExpedientes;
           contNumExpedientes = contNumExpedientes + 1;
       </logic:iterate>
    }
</script>
