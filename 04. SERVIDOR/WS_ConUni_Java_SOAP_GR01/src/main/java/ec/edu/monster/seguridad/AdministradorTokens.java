package ec.edu.monster.seguridad;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AdministradorTokens {
    private static final Map<String, Instant> tokensActivos = new HashMap<>();
    private static void depurar() { tokensActivos.values().removeIf(fin -> !fin.isAfter(Instant.now())); }
    public static synchronized String generarToken() {
        depurar();
        String token = UUID.randomUUID().toString();
        tokensActivos.put(token, Instant.now().plusSeconds(1800));
        return token;
    }
    public static synchronized boolean validarToken(String token) {
        depurar(); return token != null && tokensActivos.containsKey(token);
    }
    public static synchronized void InvalidarToken(String token) { tokensActivos.remove(token); }
    public static synchronized int contarActivos() { depurar(); return tokensActivos.size(); }
}
