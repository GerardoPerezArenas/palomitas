/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.cargaExpediente;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import javax.servlet.http.HttpServletRequest;

/**
 * Interfaz que se utiliza para cargar los expedientes
 * 
 * @author jesus.cordoba-perez
 */
public interface CargaExpediente {
    
    /**
     * Metodo que se utiliza para cargar un expediente pendiente. 
     * Se invoca cuando la opcion que se envia es "cargar"
     * @param request
     * @param usuarioVO
     * @param codOrganizacion
     * @param numExpediente
     * @throws Exception 
     */
    public void opcionCargarExpediente (HttpServletRequest request, UsuarioValueObject usuarioVO, String codOrganizacion, String numExpediente) throws Exception;
    
}
