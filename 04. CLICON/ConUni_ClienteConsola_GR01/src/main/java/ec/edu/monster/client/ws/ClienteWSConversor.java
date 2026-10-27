/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.client.ws;

import jakarta.xml.ws.Binding;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.handler.Handler;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;

/**
 *
 * @author sebas
 */
public class ClienteWSConversor {

    private static final String ESPACIO_NOMBRES = "http://ws.controladores.monster.edu.ec/";
    private static final QName QNOMBRE_SERVICIO = new QName(ESPACIO_NOMBRES, "WSConversorUnidades");
    private static final QName QNOMBRE_PUERTO = new QName(ESPACIO_NOMBRES, "WSConversorUnidadesPort");

    private static final List<String> BASES_URL_POR_DEFECTO = List.of(
            "http://localhost:8080/WS_ConUni_Java_SOAP_GR01",
            "http://localhost:8080/WS_ConUni_Java_SOAP_GR01-1.0-SNAPSHOT",
            "http://localhost:8080/04.SERVIDOR"
    );

    private final ContratoWSConversor puerto;
    private String tokenSesion;
    private String endpointActivo;

    public ClienteWSConversor() {
        this.puerto = inicializarPuerto();
    }

    private ContratoWSConversor inicializarPuerto() {
        List<String> wsdlsIntentados = new ArrayList<>();
        for (String baseUrl : BASES_URL_POR_DEFECTO) {
            String endpoint = baseUrl + "/WSConversorUnidades";
            List<String> wsdlsCandidatos = List.of(endpoint + "?WSDL", endpoint + "?wsdl");
            
            for (String wsdl : wsdlsCandidatos) {
                try {
                    Service servicio = Service.create(new URL(wsdl), QNOMBRE_SERVICIO);
                    ContratoWSConversor puertoTemporal = servicio.getPort(QNOMBRE_PUERTO, ContratoWSConversor.class);
                    BindingProvider proveedor = (BindingProvider) puertoTemporal;
                    proveedor.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
                    
                    configurarManejadorToken(proveedor);
                    this.endpointActivo = endpoint;
                    return puertoTemporal;
                } catch (Exception ex) {
                    wsdlsIntentados.add(wsdl);
                }
            }
        }
        throw new IllegalStateException("No fue posible conectar con el servidor SOAP en ninguna URL: " + wsdlsIntentados);
    }

    private void configurarManejadorToken(BindingProvider proveedor) {
        Binding enlace = proveedor.getBinding();
        List<Handler> cadenaManejadores = new ArrayList<>(enlace.getHandlerChain());
        cadenaManejadores.add(new ManejadorTokenSOAP(() -> tokenSesion));
        enlace.setHandlerChain(cadenaManejadores);
    }

    public String login(String usuario, String contrasenia) {
        this.tokenSesion = puerto.login(usuario, contrasenia);
        return this.tokenSesion;
    }

    public double convertirLongitud(double valor, String unidadInicial, String unidadFinal) {
        return puerto.convertirLongitud(valor, unidadInicial, unidadFinal);
    }

    public double convertirMasa(double valor, String unidadInicial, String unidadFinal) {
        return puerto.convertirMasa(valor, unidadInicial, unidadFinal);
    }

    public double convertirTemperatura(double valor, String unidadInicial, String unidadFinal) {
        return puerto.convertirTemperatura(valor, unidadInicial, unidadFinal);
    }

    public String getEndpointActivo() { return endpointActivo; }
    public void cerrarSesion() { this.tokenSesion = null; }
}