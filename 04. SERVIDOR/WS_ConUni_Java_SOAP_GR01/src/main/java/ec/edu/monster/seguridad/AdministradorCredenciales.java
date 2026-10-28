/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.seguridad;

/**
 *
 * @author sebas
 */
public class AdministradorCredenciales {
    
    // credenciales por defecto para almacenar sin usar BDD
    private static String usuarioValido = "monster";
    private static String contraseniaValida = "monster69";

    public static synchronized boolean validarCredenciales(String usuario,String contrasenia){
        if(usuario == null || contrasenia == null){
            return false;
        }
        return usuarioValido.equals(usuario) && contraseniaValida.equals(contrasenia);
    }
    
    public static synchronized void cambiarContrasenia(String contraseniaActual, String contraseniaNueva){
        if(!contraseniaValida.equals(contraseniaActual)){
            throw new RuntimeException("Contraseña invalida");
        }
        if(contraseniaNueva == null|| contraseniaNueva.trim().isEmpty()){
            throw new RuntimeException("La nueva contraseña no puede estar vacia");
        }
        contraseniaValida = contraseniaNueva;        
    }
}
