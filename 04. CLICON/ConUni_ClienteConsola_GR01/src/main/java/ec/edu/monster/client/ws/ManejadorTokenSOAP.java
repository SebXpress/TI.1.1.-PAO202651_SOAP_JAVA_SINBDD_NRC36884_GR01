/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.client.ws;

import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Set;
import java.util.function.Supplier;
import javax.xml.namespace.QName;

/**
 *
 * @author sebas
 */
public class ManejadorTokenSOAP implements SOAPHandler<SOAPMessageContext> {

    private final Supplier<String> tokenSupplier;

    public ManejadorTokenSOAP(Supplier<String> tokenSupplier) {
        this.tokenSupplier = tokenSupplier;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (Boolean.TRUE.equals(outbound)) {
            String token = tokenSupplier.get();
            
            if (token != null && !token.trim().isEmpty()) {
                try {
                    SOAPMessage message = context.getMessage();
                    SOAPHeader header = message.getSOAPHeader();
                    if (header == null) {
                        header = message.getSOAPPart().getEnvelope().addHeader();
                    }

                    // Limpiamos cabeceras anteriores para evitar acumulación
                    header.removeContents();

                    // Creamos el nodo <token> exactamente con el nombre que busca el servidor
                    QName qname = new QName("http://ws.controladores.monster.edu.ec/", "token");
                    SOAPHeaderElement tokenElement = header.addHeaderElement(qname);
                    tokenElement.setTextContent(token);

                    message.saveChanges();
                } catch (Exception e) {
                    System.err.println("Error al adjuntar el token SOAP: " + e.getMessage());
                }
            }
        }
        return true;
    }

  
    @Override
    public boolean handleFault(SOAPMessageContext context) { return true; }

    @Override
    public void close(MessageContext context) {}

    @Override
    public Set<QName> getHeaders() { return null; }
}