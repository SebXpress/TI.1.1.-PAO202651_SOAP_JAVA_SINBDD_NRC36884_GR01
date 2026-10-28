package ec.edu.monster.controladores.manejadoresws;

import ec.edu.monster.seguridad.AdministradorTokens;
import ec.edu.monster.servicios.MonitorServidor;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import java.util.*;
import javax.xml.namespace.QName;

public class ManejadorAutenticacion implements SOAPHandler<SOAPMessageContext> {
    private static final String CLIENTE = "monster.monitor.cliente";
    private static final String OPERACION = "monster.monitor.operacion";
    private static final String REGISTRADO = "monster.monitor.registrado";

    @Override public boolean handleMessage(SOAPMessageContext contexto) {
        if (Boolean.TRUE.equals(contexto.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY))) {
            registrar(contexto, true);
            return true;
        }
        QName nombre = (QName) contexto.get(MessageContext.WSDL_OPERATION);
        String operacion = nombre == null ? "desconocida" : nombre.getLocalPart();
        if ("obtenerEstado".equals(operacion)) return true;
        String cliente = "OTRO";
        Object headers = contexto.get(MessageContext.HTTP_REQUEST_HEADERS);
        if (headers instanceof Map<?, ?> mapa) {
            for (var entry : mapa.entrySet()) {
                if ("X-Cliente".equalsIgnoreCase(String.valueOf(entry.getKey())) && entry.getValue() instanceof List<?> valores && !valores.isEmpty()) {
                    String valor = String.valueOf(valores.get(0)).toUpperCase(Locale.ROOT);
                    if (Set.of("WEB", "MOVIL", "ESCRITORIO", "CONSOLA", "PANEL").contains(valor)) cliente = valor;
                }
            }
        }
        contexto.put(CLIENTE, cliente); contexto.put(OPERACION, operacion);
        MonitorServidor.registrarPeticion(cliente);
        if ("login".equals(operacion)) return true;
        try {
            var encabezado = contexto.getMessage().getSOAPHeader();
            var nodos = encabezado == null ? null : encabezado.getElementsByTagNameNS("*", "token");
            if (nodos == null || nodos.getLength() == 0 || !AdministradorTokens.validarToken(nodos.item(0).getTextContent()))
                throw new IllegalArgumentException("Acceso denegado: token invalido o expirado. Inicie sesion.");
        } catch (Exception ex) {
            registrar(contexto, false);
            throw new RuntimeException("Acceso denegado: token invalido o expirado. Inicie sesion.");
        }
        return true;
    }
    private void registrar(SOAPMessageContext contexto, boolean exito) {
        if (contexto.containsKey(OPERACION) && !Boolean.TRUE.equals(contexto.get(REGISTRADO))) {
            MonitorServidor.registrarResultado((String) contexto.get(CLIENTE), (String) contexto.get(OPERACION), exito);
            contexto.put(REGISTRADO, true);
        }
    }
    @Override public boolean handleFault(SOAPMessageContext contexto) { registrar(contexto, false); return true; }
    @Override public Set<QName> getHeaders() { return Collections.emptySet(); }
    @Override public void close(MessageContext contexto) { }
}
