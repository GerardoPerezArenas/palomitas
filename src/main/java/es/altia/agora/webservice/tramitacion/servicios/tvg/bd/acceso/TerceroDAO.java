package es.altia.agora.webservice.tramitacion.servicios.tvg.bd.acceso;

import es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos.DomicilioVO;
import es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos.InteresadoVO;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.webservice.cliente.tercero.datos.DireccionVO;
import es.altia.flexia.webservice.cliente.tercero.datos.InfoConexionVO;
import es.altia.flexia.webservice.cliente.tercero.datos.ResultadoAltaTerceroVO;
import es.altia.flexia.webservice.cliente.tercero.datos.TerceroVO;
import es.altia.flexia.webservice.cliente.tercero.servicio.WSTerceroPortSoapBindingStub;
import es.altia.flexia.webservice.cliente.tercero.servicio.WSTerceroServiceLocator;

import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: marcos.baltar
 * Date: 30-may-2006
 * Time: 17:39:02
 * To change this template use File | Settings | File Templates.
 */
public class TerceroDAO {

      public InteresadoVO getTercero(InteresadoVO terVO, String organizacion)
            throws  TechnicalException, RemoteException {
           WSTerceroPortSoapBindingStub binding = null;
           InfoConexionVO info = new InfoConexionVO();
        try {

            Config m_ConfigTechnical = ConfigServiceHelper.getConfig("formulariosPdf");
            URL url = new URL(m_ConfigTechnical.getString("WS.TERCERO"));

           info.setAplicacion("portal_formularios");
           info.setOrganizacion(organizacion);

            binding = (WSTerceroPortSoapBindingStub)
                          new WSTerceroServiceLocator().getWSTerceroPort(url);
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
           jre.printStackTrace();
            throw new TechnicalException(jre.getMessage());
        }catch (Exception e){
            throw new TechnicalException(e.getMessage());
        }

           TerceroVO ter = convertToWSTercero(terVO);
        ResultadoAltaTerceroVO salida = binding.altaTercero(ter, true, true, info);
        terVO.setCodigo(String.valueOf(salida.getCodTercero()));
        terVO.setCoddomicilio(String.valueOf(salida.getCodDomicilio()));
        terVO.setVersion(String.valueOf(salida.getNumVersTercero()));
        return terVO;

    }

      private TerceroVO convertToWSTercero (InteresadoVO inter){
          TerceroVO ter = new TerceroVO();
          es.altia.flexia.webservice.cliente.tercero.datos.DireccionVO dir =
                  new es.altia.flexia.webservice.cliente.tercero.datos.DireccionVO();
          DomicilioVO dom = inter.getDomicilio();
          ter.setAp1(inter.getAp1());
          ter.setAp2(inter.getAp2());
          ter.setDoc(inter.getDoc());
          ter.setEmail(inter.getEmail());
          ter.setNombre(inter.getNombre());
          ter.setTelefono(inter.getTelefono());
          ter.setTipoDoc(inter.getTipoDoc());
          dir.setBloque(dom.getBloque());
          dir.setCodMunicipio(dom.getCodMunicipio());
          dir.setCodPais(dom.getCodPais());
          dir.setCodPostal(dom.getCodPostal());
          dir.setCodProvincia(dom.getCodProvincia());
          dir.setEmplazamiento(dom.getEmplazamiento());
          dir.setEsPrincipal(true);
          dir.setEscalera(dom.getEscalera());
          dir.setNombreVia(dom.getNombreVia());
          dir.setPlanta(dom.getPlanta());
          dir.setPortal(dom.getPortal());
          dir.setPrimerNumero(dom.getPrimerNumero());
          dir.setPrimeraLetra(dom.getPrimeraLetra());
          dir.setPuerta(dom.getPuerta());
          dir.setTipoVia(dom.getTipoVia());
          dir.setUltimaLetra(dom.getUltimaLetra());
          dir.setUltimoNumero(dom.getUltimoNumero());
          ter.setDomicilio(dir);
          return ter;
          
      }

      private InteresadoVO convertoToFlexia (TerceroVO inter){
          InteresadoVO ter = new InteresadoVO();
          DomicilioVO dir = new DomicilioVO();
          DireccionVO dom = inter.getDomicilio();
          ter.setAp1(inter.getAp1());
          ter.setAp2(inter.getAp2());
          ter.setDoc(inter.getDoc());
          ter.setEmail(inter.getEmail());
          ter.setNombre(inter.getNombre());
          ter.setTelefono(inter.getTelefono());
          ter.setTipoDoc(inter.getTipoDoc());
          dir.setBloque(dom.getBloque());
          dir.setCodMunicipio(dom.getCodMunicipio());
          dir.setCodPais(dom.getCodPais());
          dir.setCodPostal(dom.getCodPostal());
          dir.setCodProvincia(dom.getCodProvincia());
          dir.setEmplazamiento(dom.getEmplazamiento());
          dir.setEsPrincipal(true);
          dir.setEscalera(dom.getEscalera());
          dir.setNombreVia(dom.getNombreVia());
          dir.setPlanta(dom.getPlanta());
          dir.setPortal(dom.getPortal());
          dir.setPrimerNumero(dom.getPrimerNumero());
          dir.setPrimeraLetra(dom.getPrimeraLetra());
          dir.setPuerta(dom.getPuerta());
          dir.setTipoVia(dom.getTipoVia());
          dir.setUltimaLetra(dom.getUltimaLetra());
          dir.setUltimoNumero(dom.getUltimoNumero());
          ter.setDomicilio(dir);
          return ter;

      }

}
