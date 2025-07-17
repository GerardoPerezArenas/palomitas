/**
 * WSExpedienteImplServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.tramitacion.servicios.tvg.servicios.expediente;

public class WSExpedienteImplServiceTestCase extends junit.framework.TestCase {
    public WSExpedienteImplServiceTestCase(java.lang.String name) {
        super(name);
    }

    public void testWSExpedienteImplWSDL() throws Exception {
        javax.xml.rpc.ServiceFactory serviceFactory = javax.xml.rpc.ServiceFactory.newInstance();
        java.net.URL url = new java.net.URL(new es.altia.agora.webservice.tramitacion.servicios.tvg.servicios.expediente.WSExpedienteImplServiceLocator().getWSExpedienteImplAddress() + "?WSDL");
        javax.xml.rpc.Service service = serviceFactory.createService(url, new es.altia.agora.webservice.tramitacion.servicios.tvg.servicios.expediente.WSExpedienteImplServiceLocator().getServiceName());
        assertTrue(service != null);
    }

    public void test1WSExpedienteImplSetExpediente() throws Exception {
        es.altia.agora.webservice.tramitacion.servicios.tvg.servicios.expediente.WSExpedienteImplSoapBindingStub binding;
        try {
            binding = (es.altia.agora.webservice.tramitacion.servicios.tvg.servicios.expediente.WSExpedienteImplSoapBindingStub)
                          new es.altia.agora.webservice.tramitacion.servicios.tvg.servicios.expediente.WSExpedienteImplServiceLocator().getWSExpedienteImpl();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos.SalidaBooleanExpediente value = null;
        value = binding.setExpediente(new es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos.ExpedienteVO(), new java.lang.String());
        // TBD - validate results
    }

}
