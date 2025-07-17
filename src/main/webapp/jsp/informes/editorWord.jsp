<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map,
                                                                             java.util.Vector" %>

<%@page import="es.altia.common.util.*"%>
<%@page import="es.altia.agora.business.util.*"%>
<%@page import="es.altia.util.conexion.*"%>

<%
  String opcion = (String)request.getAttribute("opcion");
  Registro parametros = null;

  Registro datos = null;
  Registro datosRegistro = null;
  Registro paresDES_CBD = null;
  Vector vectorEtiquetas = null;

  if(opcion.trim().equals("CARGA_INICIAL"))
    vectorEtiquetas = (Vector)request.getAttribute("Etiquetas");

  if(opcion.trim().equals("OBTENER_DATOS_PLANTILLA") || opcion.trim().equals("OBTENER_DATOS_PLANTILLA_DESDE_CRONOLOGIA"))
  {
     datos=(Registro)request.getAttribute("datosPlantilla");
     datosRegistro = (Registro)datos.get("datosRegistro");
     vectorEtiquetas = (Vector)datos.get("vectorEtiquetas");
     paresDES_CBD = (Registro)datos.get("paresDES_CBD");
     parametros = (Registro)datos.get("plantillaSinDatos");
  }
  else if(opcion.trim().equals("OBTENER_PLANTILLA") || opcion.trim().equals("OBTENER_PLANTILLA_DESDE_CRONOLOGIA"))
  {
    parametros = (Registro)request.getAttribute("datosPlantilla");
    vectorEtiquetas = (Vector)request.getAttribute("Etiquetas");
  }
  else
    parametros = (Registro)request.getAttribute("parametros");

  if (opcion == null)
    opcion ="";

  String host = parametros.getString("host");
  if(host == null)
    host = "";

  String sello;
  sello = (String)request.getAttribute("sello");
  if(sello == null)
    sello = "";
%>

<html:html>

<head>
    <title>Generación de documentos Word</title>

    <jsp:include page="/jsp/informes/tpls/app-constants.jsp" />
    
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>

    <SCRIPT LANGUAGE="JavaScript">
    function atras() {
      document.forms[0].opcion.value ='cargarAplicaciones';
      document.forms[0].target='mainFrame';
      document.forms[0].aplicacion.value = document.forms[0].plt_apl.value;
      document.forms[0].action = "<c:url value='/editor/DocumentosAplicacion.do'/>";
      document.forms[0].submit();
    }

    function GuardarEnBD(){
      if(document.forms[0].plt_des.value==''){
        alert('Es necesario introducir el nombre de la plantilla.');
        return;
      }
      else if(document.forms[0].fichero.value==''){
        alert('Debe de seleccionar un fichero con el boton "Examinar".');
        return;
      }else if (document.forms[0].fichero.value.lastIndexOf('.dot') == -1){
        alert('El documento debe de ser una plantilla de Word.');
        return;
      }

      if(document.forms[0].opcion.value=="MODIFICAR_PLANTILLA" || document.forms[0].opcion.value=="GRABAR_PLANTILLA")
        document.forms[0].target='oculto';

        document.forms[0].submit();
    }

    function pulsarGuardarCRD() {
      if(document.forms[0].nombreDocumento.value=='')
      {
        alert('Es necesario introducir el nombre del documento.');
        return;
      }
    <% if(opcion.trim().equals("OBTENER_DATOS_PLANTILLA_DESDE_CRONOLOGIA")) { %>
      else if(document.forms[0].fichero.value=='')
      {
        alert('Debe de seleccionar un fichero con el boton "Examinar".');
        return;
      }
      else if (document.forms[0].fichero.value.lastIndexOf('.dot') == -1)
      {
          alert('El documento debe de ser una plantilla de Word.');
        return;
      }
    <% } %>
    <%if(opcion.trim().equals("OBTENER_DATOS_PLANTILLA_DESDE_CRONOLOGIA")) {%>
      document.forms[0].opcion.value = "grabarCRD";
    <%} else {%>
      document.forms[0].opcion.value = "modificarCRD";
    <% } %>
      document.forms[0].target='oculto';
      document.forms[0].submit();
    }



    <%if(opcion.trim().equals("OBTENER_DATOS_PLANTILLA") || opcion.trim().equals("OBTENER_DATOS_PLANTILLA_DESDE_CRONOLOGIA"))
    {
    %>
      function construirPlantillaConDatos()
      {
        word = new ActiveXObject("word.application");
        var doc = word.documents.open('<%=host%>'+'documentos/temp/'+<%=sello%>+'.doc');
        word.Visible = true;

        doc.limpiarCombo();

          var findText = '';
          var matchCase = false;
          var matchWholeWord = true;
          var matchWildcards = false;
          var matchSoundLike = false;
          var matchAllWordForms = false;
          var forward = true;

          var wdFindAsk = 2;
          var wdFindContinue = 1;
          var wdFindStop = 0;
          var wrap = wdFindContinue;

          var format = false;
          var replaceWith = ''

          var wdReplaceAll = 2;
          var wdReplaceOne = 1;
          var wdReplaceNone = 0;
          var replace = wdReplaceAll;

          var matchKashida = false;
          var matchDiacritics = false;
          var matchAlefHamza = false;
          var matchControl = false;

        <%
          // Sustituimos las claves por los valores
          java.util.Enumeration atributos = paresDES_CBD.keys();
          while(atributos.hasMoreElements())
          {
            String atributo = atributos.nextElement().toString();
            String clave = paresDES_CBD.getString(atributo);
            String valor = datosRegistro.getString(clave);
          %>
            findText = '<%=atributo%>';
            var aux = '<%=valor%>';
            replaceWith = unescape(aux);
          word.Selection.Find.Execute(findText,matchCase,matchWholeWord,matchWildcards,matchSoundLike,matchAllWordForms,forward,wrap,format,replaceWith,replace,matchKashida,matchDiacritics,matchAlefHamza,matchControl);
          <%
          }
        %>
      }

    <%}%>


    function cargarFormulario()
    {
      <% if(opcion.equals("OBTENER_DATOS_PLANTILLA") || opcion.equals("OBTENER_DATOS_PLANTILLA_DESDE_CRONOLOGIA")){%>
        construirPlantillaConDatos();
      <%}%>

      <%
      if(opcion.equals("CARGA_INICIAL")){
      %>
        document.forms[0].opcion.value='GRABAR_PLANTILLA';
        obtenerPlantillaBasica();
      <%
      }else if(opcion.equals("OBTENER_PLANTILLA")){%>
        document.forms[0].opcion.value='MODIFICAR_PLANTILLA';
        recuperarFormulario();
      <%} else if(opcion.equals("OBTENER_PLANTILLA_DESDE_CRONOLOGIA")){%>
        recuperarFormulario();
      <% } %>
    }

    <% if(opcion.equals("OBTENER_PLANTILLA") || opcion.equals("OBTENER_PLANTILLA_DESDE_CRONOLOGIA")){%>

      function recuperarFormulario()
      {
        word = new ActiveXObject("word.application");
        var doc = word.documents.open('<%=host%>'+'documentos/temp/'+'<%=sello%>'+'.doc');

        // Tenemos que sustituir las variables ya definidas con los nuevos valores.
        // Actualizamos las variables del documento recuperado
        if (doc.Variables.Count  > 1)
        {
          var indices = (doc.Variables.Count -1 ) / 2;
          doc.Variables("tuplas").Value = '<%=vectorEtiquetas.size()%>';

          // Primero limpiamos todos los valores de las variables que ya hay en el documento
          var j=0;
          for(j=0; j < indices;j++)
          {
            var nom = "nom"+j;
            var desc = "desc"+j;
            doc.Variables(desc).Value = "";
            doc.Variables(nom).Value = "";
          }

          // Ahora cargamos los valores que hay en la B.D.
          <%
          for(int i=0;i<vectorEtiquetas.size();i++)
          {
            GeneralValueObject gVO = (GeneralValueObject)vectorEtiquetas.elementAt(i);
            String desc = "desc"+i;
            String nom = "nom"+i;
          %>
            if(indices >= <%=i%>)
            {
              doc.Variables("<%=desc%>").Value = '<%=(String)gVO.getAtributo("ETI_DES")%>';
              doc.Variables("<%=nom%>").Value = '<%=(String)gVO.getAtributo("ETI_NOM")%>';
            }
            else // Si tiene menos variables en el documento que en la B.D añadimos las que falten
            {
              doc.Variables.Add('<%=desc%>','<%=(String)gVO.getAtributo("ETI_DES")%>');
              doc.Variables.Add('<%=nom%>','<%=(String)gVO.getAtributo("ETI_NOM")%>');
            }
          <%
          }//del for
          %>

        }

        word.Visible = true;

        doc.limpiarCombo();
        doc.cargarCombo();
      }
    <%}%>

    <%
    if(opcion.equals("CARGA_INICIAL")){
    %>

      function obtenerPlantillaBasica() {
        word = new ActiveXObject("word.application");
        <%if (parametros.getString("plt_apl").equals("1")){%>
          var doc = word.documents.open('<%=host%>'+'documentos/plantillas/PlantillaRes.dot');
        <%}else if (parametros.getString("plt_apl").equals("2")){%>
          var doc = word.documents.open('<%=host%>'+'documentos/plantillas/PlantillaPadron.dot');
        <%}else if (parametros.getString("plt_apl").equals("4")){%>
          var doc = word.documents.open('<%=host%>'+'documentos/plantillas/PlantillaExpedientes.dot');
        <%}%>

        doc.Variables.Add('tuplas','<%=vectorEtiquetas.size()%>');

        <%
        for(int i=0;i<vectorEtiquetas.size();i++) {
          GeneralValueObject gVO = (GeneralValueObject)vectorEtiquetas.elementAt(i);
          String desc = "desc"+i;
          String nom = "nom"+i;
        %>
          doc.Variables.Add('<%=desc%>','<%=(String)gVO.getAtributo("ETI_DES")%>');
          doc.Variables.Add('<%=nom%>','<%=(String)gVO.getAtributo("ETI_NOM")%>');
        <%
        }//del for
        %>

        word.Visible = true;

        doc.limpiarCombo();
        doc.cargarCombo();
      }
    <%
    }//END IF
    %>

    function pulsarCerrar() {
      self.close();
    }
    </SCRIPT>
</head>

<body onLoad="cargarFormulario();" >
    <html:form action="/XeradorInformesAction.do" method="POST" target="oculto" styleId="XeradorInformesForm" enctype="multipart/form-data">
         <html:hidden property="textOperacion" styleId="textOperacion" />
         <html:hidden property="COD_INFORMEXERADOROculto" styleId="COD_INFORMEXERADOROculto" />
    <center>

    <input type="hidden" name="opcion">
    <input type="hidden" name="plt_cod" value="<%=parametros.getString("plt_cod")%>">
    <input type="hidden" name="plt_apl" value="<%=parametros.getString("plt_apl")%>">


    <input type="hidden" name="aplicacion">
    <input type="hidden" name="ventana" value='normal'>
    <input type="hidden" name="modo" value='2'>

    <%if(opcion.trim().equals("OBTENER_DATOS_PLANTILLA")){%>
    <table width="550" align="center" class="tablaP">
      <tr><td>&nbsp;</td></tr>

      <tr>
        <td class="titulo" colspan="2" align="left"><b>Visualizador de Plantillas</b></td>
      </tr>

      <tr>
      <td style="text-align: center;">
        <input type="button" class="boton" name="Salir" value="Salir" onclick="javascript:window.close();">
      </td>
      </tr>

    </table>

    <% } else if(opcion.trim().equals("OBTENER_DATOS_PLANTILLA_DESDE_CRONOLOGIA") || opcion.trim().equals("OBTENER_PLANTILLA_DESDE_CRONOLOGIA")) { %>
    <table width="450" align="center" class="tablaP" cellspacing="0px" cellpadding="0px">
      <tr>
        <td height="40px" colspan="2" >&nbsp;
        </td>
      </tr>
      <tr>
        <td class="titulo" colspan="2" align="left"><b>Mantenimiento de Documentos</b>
        </td>
      </tr>
      <tr>
        <td>&nbsp;
        </td>
      </tr>
      <tr>
        <td  colspan="2" style="text-align: left;" class="etiqueta"><b>Nombre del Documento</b>&nbsp;
          <input class="inputTexto" type="text" name="nombreDocumento" size="40" MAXLENGTH="80" value="<%=parametros.getString("nombreDocumento")%>"
            onkeypress="javascript:PasaAMayusculas(event);" >
        </td>
      </tr>
      <tr>
        <td>&nbsp;
        </td>
      </tr>
      <td colspan="2" style="text-align: left;">
        <html:file property="fichero" name="fichero" styleId="fichero" styleClass="inputTexto" size="53" />
      </td>
      <tr>
        <td>&nbsp;
        </td>
      </tr>
      <tr>
        <td width="50%" align="center">
          <input type="button" class="boton" name="Actualizar" value="Actualizar" onclick="javascript:pulsarGuardarCRD();">
        </td>
        <td width="50%" align="center">
          <input type="button" class="boton" name="Volver" value="Cerrar" onclick="javascript:pulsarCerrar();">
        </td>
      </tr>

    </table>


    <%}else{%>
    <table width="550" align="center" class="tablaP">
      <tr><td>&nbsp;</td></tr>
      <tr>
        <td class="titulo" colspan="2" align="left"><b>Mantenimiento de plantillas</b></td>
      </tr>
      <tr><td>&nbsp;</td></tr>

      <%
      if(parametros.getString("plt_apl").equals("4"))
      {
      %>
        <tr>
          <td style="text-align: left;" class="etiqueta"><b>Clasif. Documento&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b>
            <input class="inputTexto" type="text" name="codClasif" size="3" value="<%=parametros.getString("codClasif")%>" readonly>
            <input class="inputTexto" type="text" name="descClasif" size="34" value="<%=parametros.getString("descClasif")%>" readonly>
          </td>
          <td>&nbsp;</td>
        </tr>
      <%
      }
      %>

      <tr>
        <td  style="text-align: left;" class="etiqueta"><b>Nombre de la plantilla</b> <input class="inputTexto" type="text" name="plt_des" size="40" MAXLENGTH="80"  value="<%=parametros.getString("plt_des")%>"></td>
        <%
        if(opcion.equals("CARGA_INICIAL")){
        %>
            <td align="left"><input type="button" class="boton" name="verDoc" value="Plant. Básica" onclick="javascript:obtenerPlantillaBasica();"> </td>
        <%
        }else if(opcion.equals("OBTENER_PLANTILLA")){
        %>
            <td align="left"><input type="button" class="boton" name="verDoc" value="Plantilla" onclick="javascript:recuperarFormulario();"> </td>
        <%
        }
        %>
      </tr>

      <tr>
      <td style="text-align: left;">
        <INPUT type="file" NAME="fichero" class="inputTexto" size="51">
      </td>
      <td align="left">
        <input type="button" class="boton" name="Actualizar" value="Actualizar" onclick="javascript:GuardarEnBD();">
      </td>
      </tr>
      <tr>
        <td colspan=2 align="center"><input type="button" class="boton" name="Volver" value="Volver" onclick="javascript:atras();"></td>
      </tr>

    </table>
    <%}%>

    </center>
    </html:form>
<body>
</html:html>
