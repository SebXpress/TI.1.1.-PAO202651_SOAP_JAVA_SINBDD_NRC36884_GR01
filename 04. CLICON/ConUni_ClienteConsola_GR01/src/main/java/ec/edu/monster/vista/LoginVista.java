/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.vista;

import ec.edu.monster.controlador.ConversionControlador;
import ec.edu.monster.controlador.LoginControlador;
import ec.edu.monster.modelo.LoginModelo;
import ec.edu.monster.modelo.MensajesModelo;
import ec.edu.monster.util.EntradaUtil;

/**
 *
 * @author sebas
 */
public class LoginVista {
   private final MensajesModelo mensajes = new MensajesModelo();

     
    public void iniciar(){
        mensajes.mostrarTitulo("SISTEMA CONVERSOR - CLIENTE CONSOLA");
        mensajes.mostrarEncabezado();
        LoginControlador loginC;
        
        try {
            loginC = new LoginControlador();
            mensajes.mostrarMensaje("Servidor SOAP encontrado en: " + loginC.getEndpointActivo());
            
        } catch (Exception ex) {
            mensajes.mostrarError("No se pudo conectar al servidor SOAP:  " + ex.getMessage());
            return ;
        }
        
        
        boolean autenticado = false;
        
        while (!autenticado) {
            try {
                
            mensajes.mostrarTitulo("INICIO DE SESION");        
            String usuario = EntradaUtil.leerTextoNoVacio("Usuario:");
            String contrasenia = EntradaUtil.leerTextoNoVacio("Contrasenia:");
            
            String token = loginC.autenticar(new LoginModelo(usuario,contrasenia));
            mensajes.mostrarMensaje("Login exitoso, Token obtenido: " + token);
            
            autenticado = true; // Permite salir del bucle de login
            
            ConversionControlador conversionControlador = loginC.crearControladorConversion();
            
            new ConversionVista(conversionControlador).ejecutarMenu();
            
            } catch (Exception ex) {
                mensajes.mostrarError("Fallo la autenticacion: " + ex.getMessage());
                mensajes.mostrarMensaje("Por favor, intente nuevamente con las credenciales correctas.\n");
            }
        }
    }
}