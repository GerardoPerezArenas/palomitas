<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject,
         es.altia.common.service.config.Config,
         es.altia.common.service.config.ConfigServiceHelper,
         es.altia.util.struts.StrutsUtilOperations"%>
<%

            String[] params = null;
            if (session != null) {
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    params = usuario.getParamsCon();
                }
            }

            String urlHost = "";
            Config configuracion = ConfigServiceHelper.getConfig("common");
            String hostVirtual = configuracion.getString("hostVirtual");


            try {

                // Si se utiliza la anterior, hay que definir en el fichero de hosts, la propiedad que se defina en el common.properties
                String protocolo = StrutsUtilOperations.getProtocol(request);
                if (hostVirtual.equals("")) urlHost = protocolo + "://" + request.getHeader("host")+ request.getContextPath() + "/portafirmas/documentoportafirmas/SearchDocumentoPortafirmas.do";
                else urlHost =hostVirtual+ request.getContextPath() + "/portafirmas/documentoportafirmas/SearchDocumentoPortafirmas.do";
                             
            } catch (Exception e) {
                e.printStackTrace();
            }
%>

                function btnClose_click(theSender,theForm) { 
                    if ( (theSender) && (theForm) ) {                        
                        theForm.name   = "SearchDocumentoPortafirmasForm";
                        theForm.action = "<%=urlHost%>";
                        //theForm.idMunicipio.disabled = true;
                        //theForm.idProcedimiento.disabled = true;
                        //theForm.idEjercicio.disabled = true;
                        //theForm.idNumeroExpediente.disabled = true;
                        //theForm.idTramite.disabled = true;
                        //theForm.idOcurrenciaTramite.disabled = true;
                        theForm.idNumeroDocumento.disabled = true;
                        theForm.usuarioFirmante.disabled = true;
                        pleaseWait1("on",this);
                        theForm.submit();
                    }
                }

