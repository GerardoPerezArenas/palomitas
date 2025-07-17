<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.FusionDivisionForm" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<html>
<head>
  <title>Fusion y Division de secciones</title>
  <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css">
  <%
    UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int idioma=1;
    int apl=3;
    if (session.getAttribute("usuario") != null){
      usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
      apl = usuarioVO.getAppCod();
      idioma = usuarioVO.getIdioma();
    }
  %>
    
  <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
  <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
  <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
  
  <script src="<%=request.getContextPath()%>/scripts/general.js"></script>
  <script type="text/javascript">
    // VARIABLES GLOBALES
    <%
      FusionDivisionForm fdForm = (FusionDivisionForm)session.getAttribute("FusionDivisionForm");
      String opcion = fdForm.getOpcion();
      Log m_log = LogFactory.getLog(this.getClass().getName());
      if(m_log.isInfoEnabled()) m_log.info(opcion);
    %>
    var opcion = "<%=opcion%>";
    var operacion = "<%=fdForm.getOperacion()%>";
	var iniciar = "<%=fdForm.getInicializar()%>";
    var frame = parent.mainFrame;
    var lista = new Array();
    var codUsuarios = new Array();
    var descUsuarios = new Array();
    var codEstados = new Array();
    var descEstados = new Array();
    
    // INICIALIZACIÓN Y CARGA DE DATOS
    function inicializar(){
      if(opcion=="buscarProcesos"){
        cargarListaProcesos();
      }else if((opcion=="cargarSeccionesOrigen")||
        (opcion=="cargarSeccionesDestino")){
        cargarListaSecciones();
      }else if(opcion=="buscarTramosProceso"){
        cargarTramosProceso();
      }else if(opcion=="procesarProceso"){
        if(operacion=="SI"){
          jsp_alerta("A","Proceso realizado");
		  if (iniciar=="SI"){
		  	frame.volverAiniciar();
		  }
        }else if(operacion=="NO"){
          jsp_alerta("A","Proceso non realizado");
        } else if (operacion=="DSU_SIN_TRAMO"){
		      jsp_alerta("A","Dirección de chan sen tramo");
		    }
        //frame.self.close();
      }else if (opcion=="insertarProceso"){
	  	frame.procesoCreado();
	  } else {
        frame.pulsarMostrar();
      }
    }

    function cargarListaProcesos(){
      <% 
        Vector listaProcesos = fdForm.getListaProcesos();
        int lengthProcesos = listaProcesos.size();
        int i = 0;
        String lista="";
        if(lengthProcesos>0){
          for(i=0;i<lengthProcesos-1;i++){
            GeneralValueObject procesos = (GeneralValueObject)listaProcesos.get(i);
            lista+="[\""+procesos.getAtributo("codProceso")+"\","+
              "\""+procesos.getAtributo("descripcion")+"\","+
              "\""+procesos.getAtributo("fechaProceso")+"\","+
              "\""+procesos.getAtributo("codUsuario")+"\","+
              "\""+procesos.getAtributo("descUsuario")+"\","+
              "\""+procesos.getAtributo("estado")+"\","+
              "\""+procesos.getAtributo("codDistritoOrigen")+"\","+
              "\""+procesos.getAtributo("descDistritoOrigen")+"\","+
              "\""+procesos.getAtributo("codSeccionOrigen")+"\","+
              "\""+procesos.getAtributo("descSeccionOrigen")+"\","+
              "\""+procesos.getAtributo("letraOrigen")+"\","+
              "\""+procesos.getAtributo("codDistritoDestino")+"\","+
              "\""+procesos.getAtributo("descDistritoDestino")+"\","+
              "\""+procesos.getAtributo("codSeccionDestino")+"\","+
              "\""+procesos.getAtributo("descSeccionDestino")+"\","+
              "\""+procesos.getAtributo("letraDestino")+"\","+
              "\""+procesos.getAtributo("iniciado")+"\"],";
          }
          GeneralValueObject procesos = (GeneralValueObject)listaProcesos.get(i);
          lista+="[\""+procesos.getAtributo("codProceso")+"\","+
            "\""+procesos.getAtributo("descripcion")+"\","+
            "\""+procesos.getAtributo("fechaProceso")+"\","+
            "\""+procesos.getAtributo("codUsuario")+"\","+
            "\""+procesos.getAtributo("descUsuario")+"\","+
            "\""+procesos.getAtributo("estado")+"\","+
            "\""+procesos.getAtributo("codDistritoOrigen")+"\","+
            "\""+procesos.getAtributo("descDistritoOrigen")+"\","+
            "\""+procesos.getAtributo("codSeccionOrigen")+"\","+
            "\""+procesos.getAtributo("descSeccionOrigen")+"\","+
            "\""+procesos.getAtributo("letraOrigen")+"\","+
            "\""+procesos.getAtributo("codDistritoDestino")+"\","+
            "\""+procesos.getAtributo("descDistritoDestino")+"\","+
            "\""+procesos.getAtributo("codSeccionDestino")+"\","+
            "\""+procesos.getAtributo("descSeccionDestino")+"\","+
            "\""+procesos.getAtributo("letraDestino")+"\","+
            "\""+procesos.getAtributo("iniciado")+"\"]";
        }
      %>
      frame.lista = [<%=lista%>];
      frame.actualizarTablaProcesos();
    }

    function cargarTramosProceso(){
      <% 
        GeneralValueObject datosVO = fdForm.getDatosVO();
        String listaTOrigen = "";
        String listaTOrigenOriginal = "";
        String listaTDestino = "";
        String listaTDestinoOriginal = "";
        if(datosVO.getSize()>0){
          Vector tramosOrigen = (Vector)datosVO.getAtributo("tramosOrigen");
          Vector tramosDestino = (Vector)datosVO.getAtributo("tramosDestino");
          if((tramosOrigen!=null)&&(tramosDestino!=null)){ 
          int lengthTOrigen = tramosOrigen.size();
          int lengthTDestino = tramosDestino.size();
          if(lengthTOrigen>0){
            for(i=0;i<lengthTOrigen-1;i++){
              GeneralValueObject tramo = (GeneralValueObject)tramosOrigen.get(i);
              listaTOrigen+="[\""+tramo.getAtributo("codTramo")+"\","+
                "\""+tramo.getAtributo("numDesde")+"-"+
                tramo.getAtributo("letraDesde")+"\","+
                "\""+tramo.getAtributo("numHasta")+"-"+
                tramo.getAtributo("letraHasta")+"\",\"S\"],";
              listaTOrigenOriginal+="[\""+tramo.getAtributo("codTramo")+"\","+
                "\""+tramo.getAtributo("distrito")+"\","+
                "\""+tramo.getAtributo("seccion")+"\","+
                "\""+tramo.getAtributo("letra")+"\","+
                "\""+tramo.getAtributo("numDesde")+"\","+
                "\""+tramo.getAtributo("letraDesde")+"\","+
                "\""+tramo.getAtributo("numHasta")+"\","+
                "\""+tramo.getAtributo("letraHasta")+"\",\"S\"],";
            }
            GeneralValueObject tramo = (GeneralValueObject)tramosOrigen.get(i);
            listaTOrigen+="[\""+tramo.getAtributo("codTramo")+"\","+
              "\""+tramo.getAtributo("numDesde")+"-"+
              tramo.getAtributo("letraDesde")+"\","+
              "\""+tramo.getAtributo("numHasta")+"-"+
              tramo.getAtributo("letraHasta")+"\",\"S\"]";
            listaTOrigenOriginal+="[\""+tramo.getAtributo("codTramo")+"\","+
              "\""+tramo.getAtributo("distrito")+"\","+
              "\""+tramo.getAtributo("seccion")+"\","+
              "\""+tramo.getAtributo("letra")+"\","+
              "\""+tramo.getAtributo("numDesde")+"\","+
              "\""+tramo.getAtributo("letraDesde")+"\","+
              "\""+tramo.getAtributo("numHasta")+"\","+
              "\""+tramo.getAtributo("letraHasta")+"\",\"S\"]";
          }
          if(lengthTDestino>0){
            for(i=0;i<lengthTDestino-1;i++){
              GeneralValueObject tramo = (GeneralValueObject)tramosDestino.get(i);
              listaTDestino+="[\""+tramo.getAtributo("codTramo")+"\","+
                "\""+tramo.getAtributo("numDesde")+"-"+
                tramo.getAtributo("letraDesde")+"\","+
                "\""+tramo.getAtributo("numHasta")+"-"+
                tramo.getAtributo("letraHasta")+"\",\"P\"],";
              listaTDestinoOriginal+="[\""+tramo.getAtributo("codTramo")+"\","+
                "\""+tramo.getAtributo("distrito")+"\","+
                "\""+tramo.getAtributo("seccion")+"\","+
                "\""+tramo.getAtributo("letra")+"\","+
                "\""+tramo.getAtributo("numDesde")+"\","+
                "\""+tramo.getAtributo("letraDesde")+"\","+
                "\""+tramo.getAtributo("numHasta")+"\","+
                "\""+tramo.getAtributo("letraHasta")+"\",\"P\"],";
            }
            GeneralValueObject tramo = (GeneralValueObject)tramosDestino.get(i);
            listaTDestino+="[\""+tramo.getAtributo("codTramo")+"\","+
              "\""+tramo.getAtributo("numDesde")+"-"+
              tramo.getAtributo("letraDesde")+"\","+
              "\""+tramo.getAtributo("numHasta")+"-"+
              tramo.getAtributo("letraHasta")+"\",\"P\"]";
            listaTDestinoOriginal+="[\""+tramo.getAtributo("codTramo")+"\","+
              "\""+tramo.getAtributo("distrito")+"\","+
              "\""+tramo.getAtributo("seccion")+"\","+
              "\""+tramo.getAtributo("letra")+"\","+
              "\""+tramo.getAtributo("numDesde")+"\","+
              "\""+tramo.getAtributo("letraDesde")+"\","+
              "\""+tramo.getAtributo("numHasta")+"\","+
              "\""+tramo.getAtributo("letraHasta")+"\",\"P\"]";
          }
          }
        }
      %>
      frame.listaTOrigen = [<%=listaTOrigen%>];
      frame.listaTOrigenOriginal = [<%=listaTOrigenOriginal%>];
      frame.listaTDestino = [<%=listaTDestino%>];
      frame.listaTDestinoOriginal = [<%=listaTDestinoOriginal%>];
      frame.actualizarTablas();
    }

    function cargarListaSecciones(){
      <%
        Vector listaSecciones = fdForm.getListaSecciones();
        int lengthSecciones = listaSecciones.size();
        String codSeccionesC = "";
        String codSecciones = "";
        String descSecciones = "";
        String letraSecciones = "";
        if(lengthSecciones>0){
          for(i=0;i<lengthSecciones-1;i++){
            GeneralValueObject seccion = (GeneralValueObject)listaSecciones.get(i);
            codSeccionesC += "\""+seccion.getAtributo("codSeccion")+seccion.getAtributo("letraSeccion")+"\",";
            codSecciones += "\""+seccion.getAtributo("codSeccion")+"\",";
            descSecciones += "\""+seccion.getAtributo("descSeccion")+"\",";
            letraSecciones += "\""+seccion.getAtributo("letraSeccion")+"\",";
          }
          GeneralValueObject seccion = (GeneralValueObject)listaSecciones.get(i);
          codSeccionesC += "\""+seccion.getAtributo("codSeccion")+seccion.getAtributo("letraSeccion")+"\"";
          codSecciones += "\""+seccion.getAtributo("codSeccion")+"\"";
          descSecciones += "\""+seccion.getAtributo("descSeccion")+"\"";
          letraSecciones += "\""+seccion.getAtributo("letraSeccion")+"\"";
        }
      %>
    var codSeccionesC = [<%=codSeccionesC%>];
    var codSecciones = [<%=codSecciones%>];
    var descSecciones = [<%=descSecciones%>];
    var letraSecciones = [<%=letraSecciones%>];
    if(opcion=="cargarSeccionesOrigen"){
      frame.codSeccionesOrigen = codSecciones;
      frame.descSeccionesOrigen = descSecciones;
      frame.letraSeccionesOrigen = letraSecciones;
    }else{
      frame.codSeccionesDestino = codSecciones;
      frame.descSeccionesDestino = descSecciones;
      frame.letraSeccionesDestino = letraSecciones;
    }
    frame.cargarComboBox(codSeccionesC,descSecciones);
  }

  </script> 

</head>
<body onload="inicializar()">
</body>
</html>
