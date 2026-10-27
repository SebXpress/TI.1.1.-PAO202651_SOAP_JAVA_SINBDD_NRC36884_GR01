/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelos.utilidades;

import ec.edu.monster.modelos.utilidades.unidades.UnidadMasa;
import java.util.Map;

/**
 *
 * @author sebas
 */
public class ConversorMasa implements IConversor<UnidadMasa> {

    private final Map<UnidadMasa,Double> factor = Map.of(
            UnidadMasa.MILIGRAMO, 0.001,
            UnidadMasa.GRAMO, 1.0,
            UnidadMasa.KILOGRAMO, 1000.0,
            UnidadMasa.TONELADA, 1_000_000.0,
            UnidadMasa.ONZA, 28.3495
    );
    @Override
    public double convertir(double valor, UnidadMasa unidadInicial, UnidadMasa unidadFinal) {
        if(unidadInicial == null || unidadFinal == null){
            throw new IllegalArgumentException("Las unidades no pueden ser nulas");
        }
        if(!factor.containsKey(unidadInicial)||!factor.containsKey(unidadFinal)){
            throw new IllegalArgumentException("Unidades no validas");
        }
        // conversion de unidades base a finales
        double valorEnBase = valor * factor.get(unidadInicial);
        return valorEnBase / factor.get(unidadFinal);
    }
}
