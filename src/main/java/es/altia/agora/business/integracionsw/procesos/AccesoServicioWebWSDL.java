package es.altia.agora.business.integracionsw.procesos;

import java.util.*;

import javax.imageio.metadata.IIOMetadataNode;
import javax.wsdl.*;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;

import org.apache.axis.wsdl.gen.Parser;
import org.apache.axis.wsdl.symbolTable.*;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.message.MessageElement;
import org.apache.axis.AxisFault;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.axis.wsdl.toJava.Utils;

import es.altia.agora.business.integracionsw.exception.TipoNoValidoException;
import es.altia.agora.business.integracionsw.*;

public class AccesoServicioWebWSDL {

    private String URL;
    private String nombreSW;
    private SymbolTable tabla;

    Log m_Log = LogFactory.getLog(this.getClass().getName());

    public AccesoServicioWebWSDL(String URL, String nombreSW) throws Exception {
        this.URL = URL;
        this.nombreSW = nombreSW;
        Parser parser = new Parser();
        parser.run(URL);
        tabla = parser.getSymbolTable();
    }

    public InfoServicioWebVO getInfoServicioWebVO() {

        // TODO: Extraer la direccion de acceso al servicio web de la info del wsdl.
        String nombreSW = this.nombreSW;
        String wsdl = this.URL;
        int index = wsdl.indexOf("?wsdl");
        String urlAcceso;
        if (index != -1) {
            urlAcceso = wsdl.substring(0, index);
        } else {
            urlAcceso = wsdl;
        }
        Collection<OperacionServicioWebVO> operaciones = getOperacionServicioWebVOs();
        InfoServicioWebVO infoSW = new InfoServicioWebVO(-1, nombreSW, urlAcceso, wsdl, false);
        infoSW.setOperacionesSW(operaciones);
        return infoSW;

    }

    public Collection<OperacionServicioWebVO> getOperacionServicioWebVOs() {
        Vector<OperacionServicioWebVO> opSwVOs = new Vector<OperacionServicioWebVO>();
        for (Operation operation : getOperations()) {
            OperacionServicioWebVO op = this.createOperacionServicioWebVO(operation);
            opSwVOs.add(op);
        }

        return opSwVOs;
    }

    public Vector<Operation> getOperations() {

        //PortTypes
        Map<String, PortType> mptypes = tabla.getDefinition().getPortTypes();
        Collection ptypes = mptypes.values();

        //Creo una coleccion operations donde metere todas las operaciones de cada PortType
        Vector<Operation> operations = new Vector<Operation>();
        for (Object ptype : ptypes) {
            PortType singlePType = (PortType) ptype;
            operations.addAll(singlePType.getOperations());
        }
        m_Log.debug(mptypes);
        return (operations);
    }

    public TypeEntry getTipo(int index) {
        return (TypeEntry) tabla.getTypeIndex().values().toArray()[index];
    }

    public SymbolTable getTabla() {
        return tabla;
    }

    public void setTabla(SymbolTable tabla) {
        this.tabla = tabla;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String url) {
        URL = url;
    }

    public boolean esTipoBasico(TypeEntry tipo) {
        //es la unica diferencia que vi con un tipo compuesto
        return (tipo.isBaseType());
    }

    public TipoServicioWebVO createTipoServicioWebVO(TypeEntry tipo) {
        String nombreTipo = tipo.getQName().getLocalPart();
        if (nombreTipo.startsWith(">")) nombreTipo = nombreTipo.substring(1);

        m_Log.debug("TIPO: " + tipo);
        m_Log.debug("DIMENSIONES DEL TIPO: " + tipo.getDimensions());

        if (this.esTipoBasico(tipo)) {
            m_Log.debug("ES UN TIPO BASICO");
            return new TipoBasicoVO(nombreTipo);
        } else if (tipo.getDimensions().equals("")) {//es un tipo compuesto
            m_Log.debug("ES UN TIPO COMPUESTO");

            Vector<CampoTipoCompuestoVO> fields = new Vector<CampoTipoCompuestoVO>();
            Vector camposTipo = tipo.getContainedElements();
            for (int i = 0; i < camposTipo.size(); i++) {
                String campo = ((ElementDecl) tipo.getContainedElements().
                        get(i)).getQName().toString();
                int comienzoCampo = campo.lastIndexOf(">") + 1;
                campo = campo.substring(comienzoCampo);
                TipoServicioWebVO tipoCampo = createTipoServicioWebVO(((ElementDecl)
                        camposTipo.get(i)).getType());


                fields.add(new CampoTipoCompuestoVO(campo, tipoCampo));
            }
            return new TipoCompuestoVO(nombreTipo, fields, tipo.getQName().getNamespaceURI());
        } else {//es un array
            m_Log.debug("ES UN TIPO ARRAY");
            int dimComponent = getDim(tipo.getDimensions()) - 1;
            if (dimComponent == 0) { //Es un array de una dimension
                QName qNameComponentType = tipo.getComponentType();
                TypeEntry componentType;
                if (qNameComponentType == null) componentType = tipo.getRefType();
                else componentType = getTypeEntry(qNameComponentType);

                TipoServicioWebVO tipoContenido = createTipoServicioWebVO(componentType);
                String nombreTipoArray = tipo.getQName().getLocalPart();
                if (nombreTipoArray.startsWith(">")) nombreTipoArray = nombreTipoArray.substring(1);
                return new TipoArrayVO(nombreTipoArray, tipoContenido);
            } else { //es de varias dimensiones
                TypeEntry typeEntry = new DefinedType(tipo.getComponentType(),
                        tipo.getRefType(), new IIOMetadataNode(),
                        getInt2StrDims(dimComponent));
                typeEntry.setComponentType(tipo.getComponentType());
                typeEntry.setBaseType(false);
                typeEntry.setIsReferenced(true);
                TipoServicioWebVO tipoContenido = createTipoServicioWebVO(typeEntry);
                String nombreTipoArray = tipo.getQName().getLocalPart();
                if (nombreTipoArray.startsWith(">")) nombreTipoArray = nombreTipoArray.substring(1);
                return new TipoArrayVO(nombreTipoArray, tipoContenido);
            }
        }


    }

    public String getInt2StrDims(int dim) {
        String strdim = "";
        for (int i = 0; i < dim; i++) {
            strdim = strdim + "[]";
        }
        return strdim;
    }

    public int getDim(String corchetes) {
        int dim = 0;
        char[] caracteres = corchetes.toCharArray();
        for (char caracter : caracteres) {
            if (caracter == '[') dim++;
        }
        return dim;
    }

    public ParametroSWVO createParametroVO(Parameter parametro, String style) {
        TypeEntry tipo = parametro.getType();
        TipoServicioWebVO tipoSw = this.createTipoServicioWebVO(tipo);
        String nombre;
        if (style.equals("wrapped")) {
            nombre = tipo.getQName().getLocalPart();
            if (nombre.startsWith(">")) nombre = nombre.substring(1);
        } else {
            nombre = parametro.getName();
        }
        return new ParametroSWVO(tipoSw, nombre);
    }

    //rellena un parameter basandome en part
    //mode puede se IN, OUT o INOUT  (1, 2 o 3)
    public Parameter createParameter(Part part, byte mode) {
        Parameter parameter = new Parameter();
        QName qName = part.getTypeName();
        if (qName == null) qName = part.getElementName();
        
        TypeEntry typeEntry = getTypeEntry(qName);
        m_Log.debug("SE HA RECUPERADO EL TIPO: " + typeEntry.getQName());

        parameter.setName(part.getName());
        parameter.setType(typeEntry);
        parameter.setMode(mode);

        return parameter;
    }

    //Entra el QName de un parametro y sale su TypeEntry Asociado
    public TypeEntry getTypeEntry(QName qName) {
        m_Log.debug("BUSCAMOS EL TIPO: " + qName);
        Map elements = tabla.getElementIndex();
        Collection elementCollection = elements.values();

        for (Object anElementCollection : elementCollection) {
            TypeEntry type = (TypeEntry) anElementCollection;
            m_Log.debug("TIPO RECORRIDO: " + type + " - TIPO NODO: " + type.getNode());

            // Write out the type if and only if:
            // - we found its definition (getNode())
            // - it is referenced
            // - it is not a base type or an attributeGroup or xs:group
            // - it is a Type (not an Element) or a CollectionElement
            // (Note that types that are arrays are passed to getGenerator
            // because they may require a Holder)
            // A CollectionElement is an array that might need a holder
            boolean isType = ((type instanceof Type) || (type instanceof CollectionElement));

            boolean isValidType = false;
            if ((type.getNode() != null) && !Utils.isXsNode(type.getNode(), "attributeGroup")
                    && !Utils.isXsNode(type.getNode(), "group") && type.isReferenced() && isType
                    && (type.getBaseType() == null)) {

                isValidType = true;
            } else if (type.getNode() == null && type.isReferenced() && isType) {
                isValidType = true;
            }

            if (isValidType) {
                String localPart = type.getQName().getLocalPart();
                if (localPart.startsWith(">")) localPart = localPart.substring(1);
                String namespaceURI = type.getQName().getNamespaceURI();
                if (qName.getLocalPart().equals(localPart) && qName.getNamespaceURI().equals(namespaceURI)) return type;
            }
        }

        Map types = tabla.getTypeIndex();
        Collection typeCollection = types.values();
        for (Object aTypeCollection : typeCollection) {
            TypeEntry type = (TypeEntry) aTypeCollection;
            m_Log.debug("TIPO RECORRIDO: " + type + " - TIPO NODO: " + type.getNode());

            // Write out the type if and only if:
            // - we found its definition (getNode())
            // - it is referenced
            // - it is not a base type or an attributeGroup or xs:group
            // - it is a Type (not an Element) or a CollectionElement
            // (Note that types that are arrays are passed to getGenerator
            // because they may require a Holder)
            // A CollectionElement is an array that might need a holder
            boolean isType = ((type instanceof Type) || (type instanceof CollectionElement));

            boolean isValidType = false;
            if ((type.getNode() != null) && !Utils.isXsNode(type.getNode(), "attributeGroup")
                    && !Utils.isXsNode(type.getNode(), "group") && type.isReferenced() && isType
                    && (type.getBaseType() == null)) {

                isValidType = true;
            } else if (type.getNode() == null && type.isReferenced() && isType) {
                isValidType = true;
            }

            if (isValidType) {
                String localPart = type.getQName().getLocalPart();
                if (localPart.startsWith(">")) localPart = localPart.substring(1);
                String namespaceURI = type.getQName().getNamespaceURI();
                if (qName.getLocalPart().equals(localPart) && qName.getNamespaceURI().equals(namespaceURI)) return type;
            }
        }

        return null;
    }

    public Part getOutPart(Operation operation) {
        Output out = operation.getOutput();
        Message message = out.getMessage();
        Map mapParts = message.getParts();
        Collection parts = (mapParts.values());
        return (Part) parts.toArray()[0];
    }

    public List getInParts(Operation operation) {
        Input in = operation.getInput();
        Message message = in.getMessage();
        return message.getOrderedParts(operation.getParameterOrdering());
    }

    private Vector<ParametroSWVO> createVectorInParams(List listaParts, String style) {
        Vector<ParametroSWVO> inParams = new Vector<ParametroSWVO>();
        for (Object listaPart : listaParts) {
            Parameter parameter = this.createParameter((Part) listaPart, Parameter.IN);
            ParametroSWVO parametroEntrada = createParametroVO(parameter, style);
            //OJO si en Java tenemos un array de bytes, se traduce a WDSL
            //como un tipo base : base64Binary
            //m_Log.debug(parametroEntrada);
            inParams.add(parametroEntrada);
        }
        return inParams;
    }

    public boolean opIsInPorType(Operation op, PortType portType) {

        return portType.getOperations().contains(op);

    }

    //lo mas especifico que pude ser es cogiendo el namespace asociado
    //al portType donde esta es operacion
    public String getNamespace(Operation operation) {
        Collection portTypes = tabla.getDefinition().getPortTypes().values();
        for (Object portType : portTypes) {
            PortType ptype = (PortType) portType;
            if (opIsInPorType(operation, ptype)) {
                return ptype.getQName().getNamespaceURI();
            }
        }
        return null;
    }

    public OperacionServicioWebVO createOperacionServicioWebVO(Operation operation) {

        String soapActionUri = getSOAPActionUri(operation);
        if (soapActionUri == null) soapActionUri = "";
        String style = getStyleId(operation);
        List ordenParams = operation.getParameterOrdering();
        if (style.equals("document") && ordenParams == null) style = "wrapped";

        String nombreOperacion = operation.getName();
        ParametroSWVO salida = createParametroVO(createParameter(getOutPart(operation), Parameter.OUT), style);
        Vector<ParametroSWVO> paramsEntrada = createVectorInParams(this.getInParts(operation), style);

        return new OperacionServicioWebVO(nombreOperacion, salida, paramsEntrada, this.getNamespace(operation), soapActionUri, style);
    }

    private String getStyleId(Operation operation) {

        Collection bindings = tabla.getDefinition().getBindings().values();
        String style = null;
        for (Object object : bindings) {
            Binding bindingEntry = (Binding) object;
            BindingOperation bindingOp = bindingEntry.getBindingOperation(operation.getName(), null, null);
            if (bindingOp != null) {
                List extElementsList = bindingOp.getExtensibilityElements();
                for (Object elemObject : extElementsList) {
                    ExtensibilityElement extElement = (ExtensibilityElement) elemObject;
                    if (extElement instanceof SOAPOperation) {
                        SOAPOperation soapOperation = (SOAPOperation) extElement;
                        style = soapOperation.getStyle();

                        if (style != null) break;
                    }
                }

                if (style == null) {
                    // No hemos encontrado el estilo en la definición de la operación
                    // Lo buscamos en la definicion del binding
                    extElementsList = bindingEntry.getExtensibilityElements();
                    for (Object elemObject : extElementsList) {
                        ExtensibilityElement extElement = (ExtensibilityElement) elemObject;
                        if (extElement instanceof SOAPBinding) {
                            SOAPBinding soapOperation = (SOAPBinding) extElement;
                            style = soapOperation.getStyle();

                            if (style != null) break;
                        }
                    }
                }
            }
        }
        return style;

    }

    private String getSOAPActionUri(Operation operation) {

        Collection bindings = tabla.getDefinition().getBindings().values();
        String soapActionURI = null;
        for (Object object : bindings) {
            Binding bindingEntry = (Binding) object;
            BindingOperation bindingOp = bindingEntry.getBindingOperation(operation.getName(), null, null);
            if (bindingOp != null) {
                List extElementsList = bindingOp.getExtensibilityElements();
                for (Object elemObject : extElementsList) {
                    ExtensibilityElement extElement = (ExtensibilityElement) elemObject;
                    if (extElement instanceof SOAPOperation) {
                        SOAPOperation soapOperation = (SOAPOperation) extElement;
                        soapActionURI = soapOperation.getSoapActionURI();

                        if (soapActionURI != null) break;
                    }
                }
            }
        }
        return soapActionURI;
    }

    public static TipoServicioWebVO generaVector(int longitud) {
        TipoServicioWebVO vector = new TipoArrayVO("vector", new TipoBasicoVO("string"));
        TipoServicioWebVO cadenaO = new TipoBasicoVO("string");
        try {
            ((TipoBasicoVO) cadenaO).setValor("O");
        } catch (TipoNoValidoException e) {
            e.printStackTrace();
        }
        Vector<TipoServicioWebVO> vacias = new Vector<TipoServicioWebVO>();
        for (int i = 0; i < longitud; i++) {
            vacias.add(cadenaO);
        }


        ((TipoArrayVO) vector).setArray(vacias);
        return vector;
    }

    // genera una matriz de tamaño filasxcolumnas rellenada con "O"
    public static TipoServicioWebVO generaMatriz(int filas, int columnas) {

        TipoServicioWebVO fila = new TipoArrayVO("fila", new TipoBasicoVO("string"));

        TipoServicioWebVO cadenaO = new TipoBasicoVO("string");
        try {
            ((TipoBasicoVO) cadenaO).setValor("O");
        } catch (TipoNoValidoException e) {
            e.printStackTrace();
        }
        Vector<TipoServicioWebVO> vacias = new Vector<TipoServicioWebVO>();


        for (int i = 0; i < columnas; i++) {
            vacias.add(cadenaO); //genero 1 fila de columnas elementos
        }


        ((TipoArrayVO) fila).setArray(vacias);

        Vector<TipoServicioWebVO> vector = new Vector<TipoServicioWebVO>();

        for (int i = 0; i < filas; i++) {
            vector.add(fila);//añado las filas a la matriz
        }


        TipoServicioWebVO matriz = new TipoArrayVO("matriz", fila);
        ((TipoArrayVO) matriz).setArray(vector);
        return matriz;
    }

    public SOAPBodyElement analizaTipo(TipoServicioWebVO tipo, SOAPBodyElement bodyElement) {

        try {
            if (tipo.esTipoBase()) {
                bodyElement.setObjectValue(((TipoBasicoVO) tipo).getValor());
            } else if (tipo.esTipoComplejo()) {

                for (CampoTipoCompuestoVO campo : ((TipoCompuestoVO) tipo).getFields()) {
                    TipoServicioWebVO tipoCampo = campo.getTipoCampo();
                    String nombreCampo = campo.getIdCampo();
                    SOAPBodyElement hijo = new SOAPBodyElement(new QName("", nombreCampo));
                    hijo = analizaTipo(tipoCampo, hijo);
                    bodyElement.addChildElement(hijo);
                }
            } else if (tipo.esTipoArray()) {
                for (Object o : ((TipoArrayVO) tipo).getArray()) {
                    TipoServicioWebVO elemento = (TipoServicioWebVO) o;
                    SOAPBodyElement hijo = new SOAPBodyElement(new
                            QName("", elemento.getNombreTipo()));
                    hijo = analizaTipo(elemento, hijo);
                    bodyElement.addChildElement(hijo);
                }
            }
        } catch (SOAPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TipoNoValidoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return bodyElement;

    }

    public SOAPBodyElement analizaParam(ParametroSWVO inParam, SOAPBodyElement bodyElement) {

        try {
            SOAPBodyElement paramElement = new SOAPBodyElement(new
                    javax.xml.namespace.QName("", inParam.getNombreParametro()));
            TipoServicioWebVO tipo = inParam.getTipoParametro();
            paramElement = analizaTipo(tipo, paramElement);
            bodyElement.addChildElement(paramElement);
        } catch (SOAPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bodyElement;
    }

    public SOAPBodyElement decodeEnv(SOAPEnvelope ret, String uri, String local) {

        SOAPBodyElement bodyElement = null;
        try {
            bodyElement = ret.getBodyByName(uri, local);
        } catch (AxisFault e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bodyElement;
    }

    public TipoServicioWebVO decodeMessage(MessageElement message) {
        try {
            SOAPEnvelope env = message.getEnvelope();
            if (message.getHref() == null) {
                String queEs = message.getAttributeNS("http://www.w3.org/2001/XMLSchema-instance"
                        , "type");
                if (queEs.substring(0, 3).equals("xsd")) {   //Es un tipo basico
                    TipoBasicoVO tipoBasico = new TipoBasicoVO(queEs.substring(4));
                    tipoBasico.setValor(message.getValue());
                    return tipoBasico;
                } else if (queEs.equals("soapenc:Array")) {    //Es un array
                    Vector<TipoServicioWebVO> children = new Vector<TipoServicioWebVO>();
                    for (Iterator it_message = message.getChildElements(); it_message.hasNext();) {
                        MessageElement me = (MessageElement) it_message.next();
                        me.setEnvelope(env);
                        children.add(decodeMessage(me));
                    }

                    TipoServicioWebVO tipoArray = new TipoArrayVO();
                    ((TipoArrayVO) tipoArray).setArray(children);
                    return (tipoArray);
                }
            } else {  //es un tipo complejo
                MessageElement hijo = new MessageElement();
                String href = message.getHref().substring(1);
                hijo.setEnvelope(env);
                Vector v = env.getBodyElements();
                for (Object aV : v) {
                    MessageElement aux = (MessageElement) aV;
                    if (href.equals(aux.getID())) {
                        hijo = aux;
                        break;
                    }
                }
                String nombreTipo = hijo.getAttributeNS("http://www.w3.org/2001/XMLSchema-instance"
                        , "type");
                nombreTipo = nombreTipo.substring(4);

//				for (Iterator it_attrs= hijo.getAllAttributes();it_attrs.hasNext();){
//					m_Log.debug(it_attrs.next());
//				}
                String namespace = "";
                for (Iterator it_prefix = hijo.getNamespacePrefixes(); it_prefix.hasNext();) {
                    String candidato = (String) it_prefix.next();
                    String subCandidato = candidato.substring(0, 2);
                    if (subCandidato.equals("ns")) {
                        namespace = hijo.getNamespaceURI(candidato);
                        break;
                    }

                }


                List fields = hijo.getChildren();

                Vector<CampoTipoCompuestoVO> hashFields = new Vector<CampoTipoCompuestoVO>();
                for (Object field : fields) {
                    MessageElement mess = (MessageElement) field;
                    mess.setEnvelope(env);
                    TipoServicioWebVO tipoCampo = decodeMessage(mess);
                    String nombreCampo = mess.getName();
                    hashFields.add(new CampoTipoCompuestoVO(nombreCampo, tipoCampo));
                }
                return new TipoCompuestoVO(nombreTipo, hashFields, namespace);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public OperacionServicioWebVO eligeOperacionServicioWebVO(String nombreOperacion) {
        Vector operations = this.getOperations();
        Operation operation;
        for (Object operation1 : operations) {
            operation = (Operation) operation1;
            if (operation.getName().equals(nombreOperacion)) {
                return (createOperacionServicioWebVO(operation));
            }
        }
        return null;
    }

}



