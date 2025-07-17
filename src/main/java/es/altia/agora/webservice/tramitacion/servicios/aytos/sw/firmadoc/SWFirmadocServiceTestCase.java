/**
 * SWFirmadocServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc;

public class SWFirmadocServiceTestCase extends junit.framework.TestCase {
    public SWFirmadocServiceTestCase(java.lang.String name) {
        super(name);
    }

    public void testfirmadocWSDL() throws Exception {
        javax.xml.rpc.ServiceFactory serviceFactory = javax.xml.rpc.ServiceFactory.newInstance();
        java.net.URL url = new java.net.URL(new es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.SWFirmadocServiceLocator().getfirmadocAddress() + "?WSDL");
        javax.xml.rpc.Service service = serviceFactory.createService(url, new es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.SWFirmadocServiceLocator().getServiceName());
        assertTrue(service != null);
    }

    public void test1firmadocSWFirmadoc() throws Exception {
        es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.FirmadocSoapBindingStub binding;
        try {
            binding = (es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.FirmadocSoapBindingStub)
                          new es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.SWFirmadocServiceLocator().getfirmadoc();
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
        es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.SWFirmadoc_RetornoBean value = null;
        value = binding.SWFirmadoc(0, new es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.SWFirmadoc_ParametrosBean());
        // TBD - validate results
    }

}
