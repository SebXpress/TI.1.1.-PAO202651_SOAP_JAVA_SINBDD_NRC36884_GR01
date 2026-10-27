/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.controlador;

import ec.edu.monster.client.ws.ClienteWSConversor;
import ec.edu.monster.modelo.ConversionModelo;

/**
 *
 * @author sebas
 */
public class ConversionControlador {
    private final ClienteWSConversor clienteWs;

    public ConversionControlador(ClienteWSConversor clienteWs) {
        this.clienteWs = clienteWs;
    }
   
    public double convertir(ConversionModelo modelo){
        return switch(modelo.getTipoConversion()){
            case LONGITUD -> clienteWs.convertirLongitud(modelo.getValor(), modelo.getValorInicial(), modelo.getValorFinal());
            case MASA -> clienteWs.convertirMasa(modelo.getValor(), modelo.getValorInicial(), modelo.getValorFinal());
            case TEMPERATURA -> clienteWs.convertirTemperatura(modelo.getValor(), modelo.getValorInicial(), modelo.getValorFinal());
        };
    }
}
