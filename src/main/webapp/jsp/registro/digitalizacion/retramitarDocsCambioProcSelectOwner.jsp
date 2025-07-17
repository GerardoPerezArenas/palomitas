<%-- 
    Document   : retramitarDocsCambioProcSelectOwner
    Created on : 06-Jan-2021, 17:00:00
    Author     : INGDGC
--%>
<!DOCTYPE html>
<%@page language="java" contentType="text/html" pageEncoding="ISO-8859-15"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>


<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.sge.DefinicionProcedimientosValueObject" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Registro::Retramitacion Documentos::</title>

        <jsp:useBean id="descriptorModal" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptorModal"  property="idi_cod" param="idiomaUsuarioModal" /> 
        <jsp:setProperty name="descriptorModal"  property="apl_cod" param="aplModal" />

        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()+request.getParameter("cssModal")%>" />
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap413/bootstrap.min.css" media="all"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>

        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/bootstrap413/bootstrap.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
    </head>
    <body>
        <!-- Variables comunes -->
            <input type="hidden" name="idiomaUsuarioModal" id="idiomaUsuarioModal" value="<%=request.getParameter("idiomaUsuarioModal")%>"/>
        <!--Modales-->
        <div class="modal fade" id="modalGestionMensajeOperacion" tabindex="-1" role="dialog" aria-labelledby="modalMensajeLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalMensajeLabel"><%=descriptorModal.getDescripcion("msgRetramiDocCambioProTitulo")%></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <h5 id="textoBodyModal"></h5>
                        <div  class="container align-self-center">
                            <div class="alert alert-danger alert-dismissible fade show" id="textoValidacionCampos" role="alert" style="display:none;"></div>
                            <input type="hidden" id="msgRetramiDocCambioCampoOblig" name="msgRetramiDocCambioCampoOblig" value="<%=descriptorModal.getDescripcion("msgRetramiDocCambioCampoOblig")%>"/>
                            <div class="form-group-lg">
                                <div class="row">
                                    <div class="col">
                                        <label for="listaProcedimiento"><%=descriptorModal.getDescripcion("gEtiqProc")%></label> 
                                        <select class="selectpicker form-control" data-actions-box="true" 
                                                name="listaProcedimiento" id="listaProcedimiento" title="<%=descriptorModal.getDescripcion("gEtiqProc")%>" 
                                                style="width:auto;"
                                                >
                                        </select>
                                    </div>
                                    <div class="col-auto my-auto">
                                        <button type="button" id="modalAceptarLanzarRetramitar" onclick="retramitarSeleccionandoOwner();" class="btn btn-primary"><%=descriptorModal.getDescripcion("msgRetramiDocCambioRetramitar")%></button>
                                    </div>
                                </div>
                            </div>
                            <hr class="mt-2 mb-3">        
                            <div class="form-group-lg" style="margin-top: 5px;">
                                <div class="alert alert-info alert-dismissible fade show"><span><%=descriptorModal.getDescripcion("msgRetramiDocCambioRegisInfo")%></span></div>
                                <div class="form-group-lg">
                                    <div class="form-group">
                                        <div class="col-md-7">
                                            <input type="radio" class="form-check-input" id="registrarDokusiTodosDocs" name="registrarDokusi" checked="true" title="<%=descriptorModal.getDescripcion("msgRetramiDocRegistrarTodos")%>" onchange="mostrarOcultarDetallesRegistrarDocEspecifico();">
                                            <label class="form-check-label" for="registrarDokusiTodosDocs"><%=descriptorModal.getDescripcion("msgRetramiDocRegistrarTodos")%></label>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-md-7">
                                            <input type="radio" class="form-check-input" id="registrarDokusiUnDocsEspecifico" name="registrarDokusi" title="<%=descriptorModal.getDescripcion("msgRetramiDocRegistrarDocEspec")%>" onchange="mostrarOcultarDetallesRegistrarDocEspecifico();" >
                                            <label class="form-check-label" for="registrarDokusiUnDocsEspecifico"><%=descriptorModal.getDescripcion("msgRetramiDocRegistrarDocEspec")%></label>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group-lg" id="divRegistarDocExpecifico" style="display: none;">
                                    <div class="form-row">
                                        <div class="col">
                                            <label for="registrarDokusiOidDocumento"><%=descriptorModal.getDescripcion("msgRetramiDocCambioRegisOID")%></label>
                                            <input type="text" class="form-text" id="registrarDokusiOidDocumento" name="registrarDokusiOidDocumento" title="<%=descriptorModal.getDescripcion("msgRetramiDocCambioRegisOID")%>" >
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col">
                                            <label for="registrarDokusiDniUsuario"><%=descriptorModal.getDescripcion("msgRetramiDocCambioRegisDatUsu")%></label>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col">
                                            <input type="text" class="form-text" style="float: left;" id="registrarDokusiDniUsuario" name="registrarDokusiDniUsuario" value="ADMIN" title="Dni" >
                                            <span class="form-text" style="float: left;"><i class="fa fa-hashtag" style="color: darkgray;"></i></span>
                                            <input type="text" class="form-text" style="float: left;width:300px;" id="registrarDokusiNombreApellidosUsuario" name="registrarDokusiNombreApellidosUsuario" value="ADMIN"  title="Nombres y Apellidos(Sin Espacios)" >
                                            <span class="form-text" style="float: left;"><i class="fa fa-hashtag" style="color: darkgray;"></i></span>
                                            <input type="text" class="form-text" style="float: left;   " id="registrarDokusiLoginUsuario" name="registrarDokusiLoginUsuario" value="ADMIN" title="Login del Usuario">
                                        </div>
                                    </div><small id="registrarDokusiUnDocsEspecificoHelpBlock" class="form-text text-muted">("dni#nombreapellidos#usuario")</small>
                                </div>
                                <button type="button" id="modalLanzarRegistrar" onclick="registrarDocumentos();" class="btn btn-primary" style="margin-top: 15px;"><%=descriptorModal.getDescripcion("msgRetramiDocCambioRegistrar")%></button>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" data-dismiss="modal"><%=descriptorModal.getDescripcion("gbCancelar")%></button>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>