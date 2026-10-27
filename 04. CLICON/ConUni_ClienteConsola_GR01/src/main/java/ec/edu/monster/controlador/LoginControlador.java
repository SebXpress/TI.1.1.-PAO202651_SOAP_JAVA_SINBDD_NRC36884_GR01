/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.controlador;

import ec.edu.monster.client.ws.ClienteWSConversor;
import ec.edu.monster.modelo.LoginModelo;

/**
 *
 * @author sebas
 */
public class LoginControlador {

    private final ClienteWSConversor clienteWs;

    public LoginControlador() {
        this.clienteWs = new ClienteWSConversor();
    }

    public String autenticar(LoginModelo loginModelo) {
        return clienteWs.login(loginModelo.getUsuario(), loginModelo.getContrasenia());
    }

    public ConversionControlador crearControladorConversion() {
        return new ConversionControlador(clienteWs);
    }

    public String getEndpointActivo() {
        return clienteWs.getEndpointActivo();
    }
}
