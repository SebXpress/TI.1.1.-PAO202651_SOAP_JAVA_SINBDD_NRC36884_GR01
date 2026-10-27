/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.seguridad;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author sebas
 */
public class AdministradorTokens {

    // conjunto de tokens que funcionan en el servidor o activos en el seridor
    private static final Set<String> tokensActivos = new HashSet<>();
    
    public static synchronized String generarToken(){
        String token = UUID.randomUUID().toString();
        tokensActivos.add(token);
        return token;
    }
    
    public static synchronized boolean validarToken(String token){
        if(token == null || token.trim().isEmpty()){
            return false;
        }
        return tokensActivos.contains(token);
    }
    
    public static synchronized void InvalidarToken(String token){
        tokensActivos.remove(token);
    }
}
