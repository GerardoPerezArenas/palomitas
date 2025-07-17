<%-- 
    Document   : selectUnidadDestinoDIR3-SIR_Modal.jsp
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

        <jsp:useBean id="descriptorModal" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptorModal"  property="idi_cod" param="idiomaUsuarioModal" /> 
        <jsp:setProperty name="descriptorModal"  property="apl_cod" param="aplModal" />

        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()+request.getParameter("cssModal")%>" />
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap413/bootstrap.min.css" media="all"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/sir/sirEstilos.css"/>

        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/bootstrap413/bootstrap.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        
        <title><%=descriptorModal.getDescripcion("etiqRegistro")%>::<%=descriptorModal.getDescripcion("msgSelecDestinoSalidaSIR")%>::</title>
    </head>
    <body>
        <!-- Variables comunes -->
        <input type="hidden" name="idiomaUsuarioModal" id="idiomaUsuarioModal" value="<%=request.getParameter("idiomaUsuarioModal")%>"/>
        <input type="hidden" name="mensajeSeleccionar" id="mensajeSeleccionar" value="<%=descriptorModal.getDescripcion("gbSeleccionar")%>"/>
        <!--Modales-->
        <div class="modal fade" id="modalSelectDestinoDIR3SIR" tabindex="-1" role="dialog" aria-labelledby="modalMensajeLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalMensajeLabel"><%=descriptorModal.getDescripcion("msgSelecDestinoSalidaSIR")%></h5>
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
                                    <div id="resultadoBusquedaUnidadesDIR">
                                        <div id="contenedorUnidadesDIR3" >
                                            <div id="filtrosUnidadesDIR3" class="sub3titulo">
                                                <div class="form">
                                                    <div class="form-row">
                                                        <div class="form-group col-md-6">
                                                            <label for="filtroCodigoDIR3">Codigo Unidad</label>
                                                            <input type="text" class="form-control" id="filtroCodigoDIR3" placeholder="A12345678">
                                                        </div>
                                                        <div class="form-group col-md-6">
                                                            <label for="filtroNombreDIR3">Nombre Unidad</label>
                                                            <input type="text" class="form-control" id="filtroNombreDIR3" placeholder="Nombre Unidad DIR3">
                                                        </div>
                                                    </div>
                                                    <div class="form-row">
                                                        <div class="form-group col-md-6">
                                                            <label for="filtroCodigoOficina">Codigo Oficina</label>
                                                            <input type="text" class="form-control" id="filtroCodigoOficina" placeholder="O12345678">
                                                        </div>
                                                        <div class="form-group col-md-6">
                                                            <label for="filtroNombreOficina">Nombre Oficina</label>
                                                            <input type="text" class="form-control" id="filtroNombreOficina" placeholder="Nombre Oficina">
                                                        </div>
                                                    </div>
                                                    <div class="form-row">
                                                        <div class="form-group col-md-6">
                                                            <label for="filtroCodigoRaiz">Codigo Raiz</label>
                                                            <input type="text" class="form-control" id="filtroCodigoRaiz" placeholder="L12345678">
                                                        </div>
                                                        <div class="form-group col-md-6">
                                                            <label for="filtroNombreRaiz">Nombre Raiz</label>
                                                            <input type="text" class="form-control" id="filtroNombreRaiz" placeholder="Nombre Raiz">
                                                        </div>
                                                    </div>
                                                    <div class="form-row">
                                                        <div class="form-group col-md-6">
                                                            <label for="filtroCodigoOrganismo">Organismo</label>
                                                            <Select class="form-control" id="filtroCodigoOrganismo" placeholder="Selecciona Organismo..."></select>
                                                        </div>
                                                        <div class="form-group col-md-6">
                                                            <label for="filtroNivelAdministrativo">Nivel Administrativo</label>
                                                            <Select class="form-control" id="filtroNivelAdministrativo" placeholder="Selecciona Nivel Administrativo..."></select>
                                                        </div>
                                                    </div>
                                                    <div class="form-row">
                                                        <div class="form-group col-md-6">
                                                            <label for="filtroComunidadAutonoma">Comunidad Autonoma</label>
                                                            <Select class="form-control" id="filtroComunidadAutonoma" placeholder="Selecciona Comunidad Autonoma..."></select>
                                                        </div>
                                                        <div class="form-group col-md-6">
                                                            <label for="filtroProvincia">Provincia</label>
                                                            <Select class="form-control" id="filtroProvincia" placeholder="Selecciona Provincia..."></select>
                                                        </div>
                                                    </div>
                                                    <div class="form-row">
                                                        <div class="form-group col-md-6" style="align-self: flex-end;">
                                                            <button type="button" id="btnBuscarUnidadDIR3" class="btn btn-light" ><%=descriptorModal.getDescripcion("gbBuscar")%></button>
                                                            <button type="button" id="btnLimpiarFiltrosUnidadDIR3" class="btn btn-light" ><%=descriptorModal.getDescripcion("gbLimpiar")%></button>
                                                            <button type="button" class="btn btn-light" data-dismiss="modal"><%=descriptorModal.getDescripcion("gbVolver")%></button>
                                                        </div>
                                                        <div class="form-group col-md-6" style="display: none;">
                                                            <label>Fecha Activacion</label><br/>
                                                            <label for="filtroFechaActivacionDesde">Desde</label>
                                                            <input type="text" class="form-control" id="filtroFechaActivacionDesde" placeholder="dd/MM/yyyy" size="10">
                                                            <label for="filtroFechaActivacionHasta">Hasta</label>
                                                            <input type="text" class="form-control" id="filtroFechaActivacionHasta" placeholder="dd/MM/yyyy" size="10">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div id="paginaUnidadesDIR3"></div>
                                            <div id="paginacionUnidadesDIR3"></div>
                                        </div>
                                    </div>                                    
<!--                                    <div class="col">
                                        <label for="listaUORDestino">< %=descriptorModal.getDescripcion("res_etiqDestino")%></label> 
                                        <select class="selectpicker form-control" data-actions-box="true" 
                                                name="listaUORDestino" id="listaUORDestino" title="< %=descriptorModal.getDescripcion("res_etiqDestino")%>" 
                                                >
                                             style="width:auto;"
                                        </select>
                                    </div>-->
<!--                                    <div class="col-auto my-auto">
                                        <button type="button" id="modalAceptar"  class="btn btn-primary"> < %=descriptorModal.getDescripcion("gbAceptar")%></button>
                                    </div>-->
                                </div>
                            </div>
                            <hr class="mt-2 mb-3">        
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" data-dismiss="modal"><%=descriptorModal.getDescripcion("gbVolver")%></button>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>