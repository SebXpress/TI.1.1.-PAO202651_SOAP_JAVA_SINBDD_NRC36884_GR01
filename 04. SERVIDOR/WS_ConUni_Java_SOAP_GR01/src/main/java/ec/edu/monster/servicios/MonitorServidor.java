package ec.edu.monster.servicios;

import java.time.Instant;
import java.util.*;
import ec.edu.monster.seguridad.AdministradorTokens;

/** Monitor acotado en memoria. No almacena cuerpos SOAP, credenciales ni tokens. */
public final class MonitorServidor {
    private static final Instant INICIO = Instant.now();
    private static long peticiones;
    private static long conversiones;
    private static final Map<String, Long> CLIENTES = new LinkedHashMap<>();
    private static final Map<String, Long> MAGNITUDES = new LinkedHashMap<>();
    private static final Deque<Evento> EVENTOS = new ArrayDeque<>();
    static {
        for (String c : List.of("WEB", "MOVIL", "ESCRITORIO", "CONSOLA", "PANEL", "OTRO")) CLIENTES.put(c, 0L);
        for (String m : List.of("Longitud", "Masa", "Temperatura")) MAGNITUDES.put(m, 0L);
    }
    private MonitorServidor() { }
    public static synchronized void registrarPeticion(String cliente) {
        peticiones++;
        CLIENTES.merge(cliente, 1L, Long::sum);
    }
    public static synchronized void registrarResultado(String cliente, String operacion, boolean exito) {
        if (exito && operacion.startsWith("convertir")) {
            String magnitud = operacion.substring("convertir".length());
            if (MAGNITUDES.containsKey(magnitud)) {
                conversiones++;
                MAGNITUDES.merge(magnitud, 1L, Long::sum);
            }
        }
        Evento evento = new Evento();
        evento.hora = Instant.now().toString(); evento.cliente = cliente;
        evento.operacion = operacion; evento.exito = exito;
        EVENTOS.addFirst(evento);
        while (EVENTOS.size() > 60) EVENTOS.removeLast();
    }
    public static synchronized Estado obtenerEstado() {
        Estado e = new Estado();
        e.inicio = INICIO.toString(); e.segundosActivo = java.time.Duration.between(INICIO, Instant.now()).getSeconds();
        e.peticiones = peticiones; e.conversiones = conversiones;
        e.sesiones = AdministradorTokens.contarActivos(); e.protocolo = "SOAP 1.1 / XML";
        CLIENTES.forEach((k,v) -> e.porCliente.add(new Conteo(k,v)));
        MAGNITUDES.forEach((k,v) -> e.porMagnitud.add(new Conteo(k,v)));
        e.eventos.addAll(EVENTOS);
        return e;
    }
    public static class Estado {
        public String inicio, protocolo;
        public long segundosActivo, peticiones, conversiones;
        public int sesiones;
        public List<Conteo> porCliente = new ArrayList<>();
        public List<Conteo> porMagnitud = new ArrayList<>();
        public List<Evento> eventos = new ArrayList<>();
    }
    public static class Conteo {
        public String nombre; public long cantidad;
        public Conteo() { }
        public Conteo(String nombre, long cantidad) { this.nombre = nombre; this.cantidad = cantidad; }
    }
    public static class Evento {
        public String hora, cliente, operacion;
        public boolean exito;
    }
}
