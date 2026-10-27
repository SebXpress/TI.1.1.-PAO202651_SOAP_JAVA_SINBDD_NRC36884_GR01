/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelos.utilidades;

import ec.edu.monster.modelos.utilidades.unidades.UnidadTemperatura;

/**
 *
 * @author sebas
 */
public class ConversorTemperatura implements IConversor<UnidadTemperatura>{
    
    @Override
    public double convertir(double valor, UnidadTemperatura unidadInicial, UnidadTemperatura unidadFinal) {
        if(unidadInicial == null || unidadFinal == null){
            throw new IllegalArgumentException("Las unidades no pueden ser nulas");
        }
        if (unidadInicial == unidadFinal){
            return valor;
        }
    
        // Para temperatura no aplican factores simples, se usan formulas directas
        return switch (unidadInicial) {
            case CELSIUS -> switch (unidadFinal) {
                case FAHRENHEIT -> (valor * 9 / 5) + 32;
                case KELVIN -> valor + 273.15;
                case RANKINE -> (valor + 273.15) * 9 / 5;
                default -> throw new IllegalArgumentException("Conversión no soportada");
            };
            case FAHRENHEIT -> switch (unidadFinal) {
                case CELSIUS -> (valor - 32) * 5 / 9;
                case KELVIN -> (valor - 32) * 5 / 9 + 273.15;
                case RANKINE -> valor + 459.67;
                default -> throw new IllegalArgumentException("Conversión no soportada");
            };
            case KELVIN -> switch (unidadFinal) {
                case CELSIUS -> valor - 273.15;
                case FAHRENHEIT -> (valor - 273.15) * 9 / 5 + 32;
                case RANKINE -> valor * 9 / 5;
                default -> throw new IllegalArgumentException("Conversion no soportada");
            };
            case RANKINE -> switch (unidadFinal) {
                case CELSIUS -> (valor - 491.67) * 5 / 9;
                case FAHRENHEIT -> valor - 459.67;
                case KELVIN -> valor * 5 / 9;
                default -> throw new IllegalArgumentException("Conversion no soportada");
            };
            default -> throw new IllegalStateException("Unexpected value: " + (unidadInicial));
        };
    }
}