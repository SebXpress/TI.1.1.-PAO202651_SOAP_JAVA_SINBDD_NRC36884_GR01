/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelos.utilidades;

import ec.edu.monster.modelos.utilidades.unidades.UnidadLongitud;
import java.util.Map;

/**
 *
 * @author sebas
 */
public class ConversorLongitud implements IConversor<UnidadLongitud>{

    private final Map<UnidadLongitud, Double> factores = Map.of(
        UnidadLongitud.MILIMETRO, 0.001,
        UnidadLongitud.CENTIMETRO, 0.01,
        UnidadLongitud.METRO, 1.0,
        UnidadLongitud.KILOMETRO, 1000.0,
        UnidadLongitud.YARDA, 0.9144
    );

    @Override
    public double convertir(double valor, UnidadLongitud unidadInicial, UnidadLongitud unidadFinal) {
        if (unidadInicial == null || unidadFinal == null ){
            throw new IllegalArgumentException("Las unidades no pueden ser nulas");
        }
        if (!factores.containsKey(unidadInicial)||!factores.containsKey(unidadFinal)){
            throw new IllegalArgumentException("Unidad no soportada");
        }
        
        // convierte el valor de la unidad base y luego a la unidad de destino
        double valorEnBase = valor * factores.get(unidadInicial);
        return valorEnBase / factores.get(unidadFinal);
    }
    
 
}
